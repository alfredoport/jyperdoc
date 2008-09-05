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

#ifndef SOCKETS_H_
#define SOCKETS_H_

/**
 * Include Files.
 */

#include <stdio.h>
#include <stdlib.h>

#include "open-axiom.h"

#ifdef __MINGW32__	/* If we are building in Windows */
	#include <winsock2.h>
	#include <windows.h>
#else
	#include <sys/socket.h>
	#include <sys/types.h>
	#include <fcntl.h>
	#include <netinet/in.h>
	#include <errno.h>
#endif

/**
 * Variable Declarations.
 */
#ifdef __MINGW32__
	typedef SOCKET openaxiom_socket;
#else
	typedef int openaxiom_socket;
#endif

#define BufSize         4096    /* size of communication buffer */
#define MaxClients      150		/* Maximum number of clients */

/**
 *
 */
typedef struct openaxiom_sio {
  openaxiom_socket socket; /* descriptor of this socket I/O endpoint.  */
  int type;                /* socket type (AF_UNIX or AF_INET) */
  int purpose;             /* can be SessionManager, GraphicsServer, etc. */
  int pid;                 /* process ID of connected socket */
  int frame;               /* spad interpreter frame. */
  openaxiom_socket remote; /* descriptor of socket at the other endpoint. */
  union {
    struct sockaddr u_addr;
    struct sockaddr_in i_addr;
  } addr;
  char *host_name;         /* name of foreign host if type == AF_INET */
  size_t nbytes_pending;   /* pending bytes for read.  */
} openaxiom_sio;

/**
 *
 */
typedef struct sock_list {      /* linked list of Sock */
  	openaxiom_sio Socket;
  	struct sock_list *next;
} Sock_List;

/**
 * Function Proto-types Declarations.
 */
OPENAXIOM_EXPORT int open_tcp_server(const char*, int);
OPENAXIOM_EXPORT void setnonblocking(void);
OPENAXIOM_EXPORT void init_socks(void);
OPENAXIOM_EXPORT int sselect(int, fd_set*, fd_set*, fd_set*, void*);
OPENAXIOM_EXPORT void openaxiom_close_socket(openaxiom_socket);

#endif /* SOCKETS_H_ */
