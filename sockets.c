/*
    Copyright (c) 1991-2002, The Numerical Algorithms Group Ltd.
    All rights reserved.

    Copyright (C) 2007-2008, Gabriel Dos Reis.
    All rights reserved.

    Copyright (C) 2008, Alfredo Portes.
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are
    met:

        - Redistributions of source code must retain the above copyright
          notice, this list of conditions and the following disclaimer.

        - Redistributions in binary form must reproduce the above copyright
         notice, this list of conditions and the following disclaimer in
          the documentation and/or other materials provided with the
          distribution.

        - Neither the name of The Numerical ALgorithms Group Ltd. nor the
          names of its contributors may be used to endorse or promote products
          derived from this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
    IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
    TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
    PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
    OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
    EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
    PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
    PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
    LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
    NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
    SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

#include "sockets.h"

openaxiom_sio *server;	/* The socket file descriptor for our "listening"
						   socket */
int connectlist[5];  	/* Array of connected sockets so we know who
						   we are talking to */
fd_set socks;        	/* Socket file descriptors we want to wake
						   up for, using select() */
int highsock;	     	/* Highest #'d file descriptor, needed for select() */

void
init_socks(void)
{
	//int i;
	//FD_ZERO(&socket_mask);
	//FD_ZERO(&server_mask);
	//init_purpose_table();
	if (server != NULL)
		free(server);

	server = (openaxiom_sio *) malloc(sizeof(openaxiom_sio));
	server -> socket = 0;
	//for(i=0; i<MaxClients; i++) clients[i].socket = 0;
}

/**
 *
 */

static void
openaxiom_load_socket_module(void)
{
#ifdef __WIN32__
   WSADATA wsaData;

   /* Request version 2.0 of WinSock DLL. */
   if (WSAStartup(MAKEWORD(2, 0), &wsaData) != 0) {
      perror("could not find suitable WinSock DLL.");
      exit(WSAGetLastError());
   }

   if (LOBYTE(wsaData.wVersion) != 2 || HIBYTE(wsaData.wVersion) != 0) {
      perror("could not find suitable WinSock DLL.");
      WSACleanup();
      exit(WSAGetLastError());
   }
#endif
}

/* Get a socket identifier to a local server.
 * We take whatever protocol is the default for
 * the address family in the SOCK_STREAM type.  */

static inline openaxiom_socket
openaxiom_communication_link(int family)
{
   openaxiom_load_socket_module();
   return socket(family, SOCK_STREAM, 0);
}

/* Returns 1 if SOCKET is an invalid socket.
 * Otherwise return 0.  */

static inline int
is_invalid_socket(const openaxiom_sio* s)
{
#ifdef __WIN32__
   return s->socket == INVALID_SOCKET;
#else
   return s->socket < 0;
#endif
}

/* Returns 1 if SOCKET is a valid socket.
 * Otherwise return 0.  */

static inline int
is_valid_socket(const openaxiom_sio* s)
{
#ifdef __WIN32__
   return s->socket != INVALID_SOCKET;
#else
   return s->socket > 0;
#endif
}

/* Because a socket on Windows platform is a not just a simple file
 * descriptor as it is in the Unix world, it is invalid to use
 * a socket identifier as argument for read(), or close, or
 * any other file descriptor function.  Furthermore, Windows
 * requires cleanup.  */

OPENAXIOM_EXPORT void
openaxiom_close_socket(openaxiom_socket s)
{
#ifdef __WIN32__
   shutdown(s, SD_BOTH);
   closesocket(s);
   WSACleanup();
#else
   close(s);
#endif
}

/* It is idiomatic in the Unix/POSIX world to use the standard
   read() and write() functions on sockets.  However, in the Windows
   world, that is invalid.  Consequently, portability suggests that
   we restrict ourselves to the POSIX standard functions recv() and
   send().  */

static inline int
openaxiom_write(openaxiom_sio* s, const openaxiom_byte* buf, size_t n)
{
	return send(s->socket, buf, n, 0);
}

static inline int
openaxiom_read(openaxiom_sio* s, openaxiom_byte* buf, size_t n)
{
	return recv(s->socket, buf, n, 0);
}

/* Return 1 is last connect() was refused.  */

static inline int
openaxiom_connection_refused(void)
{
#ifdef __WIN32__
	return WSAGetLastError() == WSAECONNREFUSED;
#else
	return errno == ECONNREFUSED;
#endif
}

/* Return 1 is the last call was cancelled. */

static inline int
openaxiom_syscall_was_cancelled(void)
{
#ifdef __WIN32__
    return WSAGetLastError() == WSAEINTR;
#else
   return errno == EINTR;
#endif
}

/**
 *
 */

OPENAXIOM_EXPORT int
sselect(int n,fd_set  *rd, fd_set  *wr, fd_set *ex, void *timeout)
{
  int ret_val;
  do {
    ret_val = select(n, (void *)rd, (void *)wr, (void *)ex, (struct timeval *) timeout);
  } while (ret_val == -1 && openaxiom_syscall_was_cancelled());
  return ret_val;
}

/* The function sleep() is not available under Windows.  Instead, they
   have Sleep(); with capital S, please.  Furthermore, it does not
   take argument in second, but in milliseconds, three order
   of magnitude of difference when compared to the Unix world.
   We abstract over that difference here.  */

static inline void
openaxiom_sleep(int n)
{
#ifdef __WIN32__
  Sleep(n * 1000);
#else
  sleep(n);
#endif
}

/*
 *
 */

OPENAXIOM_EXPORT int
open_tcp_server(const char* server_name, int port)
{
	/* Used so we can re-bind to our port
	   while a previous connection is still
	   in TIME_WAIT state. */
	const char* reuse = malloc(sizeof(char));

	/* create the socket internet socket */
	init_socks();
	server -> socket = openaxiom_communication_link(AF_INET);

	if (is_invalid_socket(server)) {
		server->socket = 0;
	}
	else {

		/* So that we can re-bind to it without
		 * TIME_WAIT problems */
		setsockopt(server->socket, SOL_SOCKET, SO_REUSEADDR, reuse, sizeof(reuse));

		/* Set socket to non-blocking with our
		 * setnonblocking routine */
		setnonblocking();

		memset(server->addr.u_addr.sa_data, 0,
		           sizeof(server->addr.u_addr.sa_data));
		server->addr.i_addr.sin_family = AF_INET;

		strcpy(server->addr.u_addr.sa_data, "tcp");
		server->addr.i_addr.sin_addr.s_addr = htonl(INADDR_ANY);
		server->addr.i_addr.sin_port = htons(port);
		if (bind(server->socket, &server->addr.i_addr,
             sizeof(server->addr.i_addr)) < 0) {
			perror("binding INET stream socket");
			server->socket = 0;
			return (EXIT_FAILURE);
    }

    int length = sizeof(server->addr.i_addr);
    if (getsockname(server->socket, &server->addr.i_addr,
    		&length)) {
      perror("getting INET server socket name");
      server->socket = 0;
      return (EXIT_FAILURE);
    }

    listen(server->socket,5);
  }

  return 0;
}

/**
 *
 */
OPENAXIOM_EXPORT void setnonblocking(void)
{
#ifdef __WIN32__
	ioctlsocket( server->socket, FIONBIO, 0 );
#else
	int opts;
	opts = fcntl(server->socket,F_GETFL);
	if (opts < 0) {
		perror("fcntl(F_GETFL)");
		exit(EXIT_FAILURE);
	}
	opts = (opts | O_NONBLOCK);
	if (fcntl(server->socket,F_SETFL,opts) < 0) {
		perror("fcntl(F_SETFL)");
		exit(EXIT_FAILURE);
	}
#endif
}

int main(void) {

  if (open_tcp_server("TBD", 8080) == EXIT_FAILURE)
	  return -1;
  while(1);
  return 0;
}
