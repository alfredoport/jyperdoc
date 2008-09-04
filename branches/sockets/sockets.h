#ifdef __MINGW32__
  #include <winsock2.h>
#else
  #include <sys/types.h>
  #include <sys/socket.h>
#endif

#ifdef __MINGW32__
typedef SOCKET openaxiom_socket;
#else
typedef int openaxiom_socket;
#endif

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
  char *host_name;      /* name of foreign host if type == AF_INET */
   size_t nbytes_pending;       /* pending bytes for read.  */
} openaxiom_sio;

static void openaxiom_load_socket_module();
int open_tcp_server(const char*, int, int);
static void openaxiom_load_socket_module();

