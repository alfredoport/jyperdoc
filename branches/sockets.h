/*
 * Sockets.h
 *
 *  Created on: Sep 4, 2008
 *  Author: Alfredo Portes
 */

#ifndef SOCKETS_H_
#define SOCKETS_H_

#ifdef __MINGW32__
  #include <winsock2.h>
  #include <windows.h>
  typedef SOCKET openaxiom_socket;
#else
  typedef int openaxiom_socket;
  #include <sys/socket.h>
#endif

#define BufSize         4096    /* size of communication buffer */
#define MaxClients      150

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

typedef struct sock_list {      /* linked list of Sock */
  	openaxiom_sio Socket;
  	struct sock_list *next;
} Sock_List;

int open_tcp_server(const char*, int, int);
void setnonblocking(void);

#endif /* SOCKETS_H_ */
