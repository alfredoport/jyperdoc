/*
 *	Sockets.c
 *
 *	Author: Jose Alfredo Perez
 */

#include "sockets.h"

openaxiom_sio *server;	/* The socket file descriptor for our "listening"
						   socket */
int connectlist[5];  	/* Array of connected sockets so we know who
						   we are talking to */
fd_set socks;        	/* Socket file descriptors we want to wake
						   up for, using select() */
int highsock;	     	/* Highest #'d file descriptor, needed for select() */

/**
 *
 */
static void
openaxiom_load_socket_module()
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

void
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

/*
 *
 */
int
open_tcp_server(const char* server_name, int port, int purpose)
{
	int reuse_addr = 1;  /* Used so we can re-bind to our port
							while a previous connection is still
							in TIME_WAIT state. */

	/* create the socket internet socket */
	server->socket = openaxiom_communication_link(AF_INET);

	if (is_invalid_socket(server)) {
		server->socket = 0;
	}
	else {

		/* So that we can re-bind to it without
		 * TIME_WAIT problems */
		setsockopt(server->socket, SOL_SOCKET, SO_REUSEADDR, &reuse_addr,
			sizeof(reuse_addr));

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
			server[0].socket = 0;
			return (EXIT_FAILURE);
    }

    int length = sizeof(server->addr.i_addr);
    if (getsockname(server->socket, &server->addr.i_addr, &length)) {
      perror("getting INET server socket name");
      server->socket = 0;
      return -1;
    }

    /*FD_SET(server->socket, &socket_mask);
    FD_SET(server->socket, &server_mask);*/

    listen(server->socket,5);
    server->purpose = purpose;
  }

  return 0;
}

/**
 *
 */
void setnonblocking()
{

#ifdef __WIN32__
	ioctlsocket( server->socket, FIONBIO, 0 );
#else
	int opts;
	opts = fcntl(server,F_GETFL);
	if (opts < 0) {
		perror("fcntl(F_GETFL)");
		exit(EXIT_FAILURE);
	}
	opts = (opts | O_NONBLOCK);
	if (fcntl(server,F_SETFL,opts) < 0) {
		perror("fcntl(F_SETFL)");
		exit(EXIT_FAILURE);
	}
#endif
}

int main() {

  open_tcp_server("test", 8080, 8080);
  return 0;
}
