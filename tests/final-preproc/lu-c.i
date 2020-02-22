
typedef long unsigned int size_t;
typedef unsigned char __u_char;
typedef unsigned short int __u_short;
typedef unsigned int __u_int;
typedef unsigned long int __u_long;
typedef signed char __int8_t;
typedef unsigned char __uint8_t;
typedef signed short int __int16_t;
typedef unsigned short int __uint16_t;
typedef signed int __int32_t;
typedef unsigned int __uint32_t;
typedef signed long int __int64_t;
typedef unsigned long int __uint64_t;
typedef long int __quad_t;
typedef unsigned long int __u_quad_t;
typedef unsigned long int __dev_t;
typedef unsigned int __uid_t;
typedef unsigned int __gid_t;
typedef unsigned long int __ino_t;
typedef unsigned long int __ino64_t;
typedef unsigned int __mode_t;
typedef unsigned long int __nlink_t;
typedef long int __off_t;
typedef long int __off64_t;
typedef int __pid_t;
typedef struct { int __val[2]; } __fsid_t;
typedef long int __clock_t;
typedef unsigned long int __rlim_t;
typedef unsigned long int __rlim64_t;
typedef unsigned int __id_t;
typedef long int __time_t;
typedef unsigned int __useconds_t;
typedef long int __suseconds_t;
typedef int __daddr_t;
typedef long int __swblk_t;
typedef int __key_t;
typedef int __clockid_t;
typedef void * __timer_t;
typedef long int __blksize_t;
typedef long int __blkcnt_t;
typedef long int __blkcnt64_t;
typedef unsigned long int __fsblkcnt_t;
typedef unsigned long int __fsblkcnt64_t;
typedef unsigned long int __fsfilcnt_t;
typedef unsigned long int __fsfilcnt64_t;
typedef long int __ssize_t;
typedef __off64_t __loff_t;
typedef __quad_t *__qaddr_t;
typedef char *__caddr_t;
typedef long int __intptr_t;
typedef unsigned int __socklen_t;
struct _IO_FILE;

typedef struct _IO_FILE FILE;


typedef struct _IO_FILE __FILE;
typedef struct
{
  int __count;
  union
  {
    unsigned int __wch;
    char __wchb[4];
  } __value;
} __mbstate_t;
typedef struct
{
  __off_t __pos;
  __mbstate_t __state;
} _G_fpos_t;
typedef struct
{
  __off64_t __pos;
  __mbstate_t __state;
} _G_fpos64_t;
typedef int _G_int16_t __attribute__ ((__mode__ (__HI__)));
typedef int _G_int32_t __attribute__ ((__mode__ (__SI__)));
typedef unsigned int _G_uint16_t __attribute__ ((__mode__ (__HI__)));
typedef unsigned int _G_uint32_t __attribute__ ((__mode__ (__SI__)));
typedef __builtin_va_list __gnuc_va_list;
struct _IO_jump_t; struct _IO_FILE;
typedef void _IO_lock_t;
struct _IO_marker {
  struct _IO_marker *_next;
  struct _IO_FILE *_sbuf;
  int _pos;
};
enum __codecvt_result
{
  __codecvt_ok,
  __codecvt_partial,
  __codecvt_error,
  __codecvt_noconv
};
struct _IO_FILE {
  int _flags;
  char* _IO_read_ptr;
  char* _IO_read_end;
  char* _IO_read_base;
  char* _IO_write_base;
  char* _IO_write_ptr;
  char* _IO_write_end;
  char* _IO_buf_base;
  char* _IO_buf_end;
  char *_IO_save_base;
  char *_IO_backup_base;
  char *_IO_save_end;
  struct _IO_marker *_markers;
  struct _IO_FILE *_chain;
  int _fileno;
  int _flags2;
  __off_t _old_offset;
  unsigned short _cur_column;
  signed char _vtable_offset;
  char _shortbuf[1];
  _IO_lock_t *_lock;
  __off64_t _offset;
  void *__pad1;
  void *__pad2;
  void *__pad3;
  void *__pad4;
  size_t __pad5;
  int _mode;
  char _unused2[15 * sizeof (int) - 4 * sizeof (void *) - sizeof (size_t)];
};
typedef struct _IO_FILE _IO_FILE;
struct _IO_FILE_plus;
extern struct _IO_FILE_plus _IO_2_1_stdin_;
extern struct _IO_FILE_plus _IO_2_1_stdout_;
extern struct _IO_FILE_plus _IO_2_1_stderr_;
typedef __ssize_t __io_read_fn (void *__cookie, char *__buf, size_t __nbytes);
typedef __ssize_t __io_write_fn (void *__cookie, __const char *__buf,
     size_t __n);
typedef int __io_seek_fn (void *__cookie, __off64_t *__pos, int __w);
typedef int __io_close_fn (void *__cookie);
extern int __underflow (_IO_FILE *);
extern int __uflow (_IO_FILE *);
extern int __overflow (_IO_FILE *, int);
extern int _IO_getc (_IO_FILE *__fp);
extern int _IO_putc (int __c, _IO_FILE *__fp);
extern int _IO_feof (_IO_FILE *__fp) __attribute__ ((__nothrow__));
extern int _IO_ferror (_IO_FILE *__fp) __attribute__ ((__nothrow__));
extern int _IO_peekc_locked (_IO_FILE *__fp);
extern void _IO_flockfile (_IO_FILE *) __attribute__ ((__nothrow__));
extern void _IO_funlockfile (_IO_FILE *) __attribute__ ((__nothrow__));
extern int _IO_ftrylockfile (_IO_FILE *) __attribute__ ((__nothrow__));
extern int _IO_vfscanf (_IO_FILE * __restrict, const char * __restrict,
   __gnuc_va_list, int *__restrict);
extern int _IO_vfprintf (_IO_FILE *__restrict, const char *__restrict,
    __gnuc_va_list);
extern __ssize_t _IO_padn (_IO_FILE *, int, __ssize_t);
extern size_t _IO_sgetn (_IO_FILE *, void *, size_t);
extern __off64_t _IO_seekoff (_IO_FILE *, __off64_t, int, int);
extern __off64_t _IO_seekpos (_IO_FILE *, __off64_t, int);
extern void _IO_free_backup_area (_IO_FILE *) __attribute__ ((__nothrow__));
typedef __gnuc_va_list va_list;
typedef __off_t off_t;
typedef __ssize_t ssize_t;

typedef _G_fpos_t fpos_t;

extern struct _IO_FILE *stdin;
extern struct _IO_FILE *stdout;
extern struct _IO_FILE *stderr;

extern int remove (__const char *__filename) __attribute__ ((__nothrow__));
extern int rename (__const char *__old, __const char *__new) __attribute__ ((__nothrow__));

extern int renameat (int __oldfd, __const char *__old, int __newfd,
       __const char *__new) __attribute__ ((__nothrow__));

extern FILE *tmpfile (void) ;
extern char *tmpnam (char *__s) __attribute__ ((__nothrow__)) ;

extern char *tmpnam_r (char *__s) __attribute__ ((__nothrow__)) ;
extern char *tempnam (__const char *__dir, __const char *__pfx)
     __attribute__ ((__nothrow__)) __attribute__ ((__malloc__)) ;

extern int fclose (FILE *__stream);
extern int fflush (FILE *__stream);

extern int fflush_unlocked (FILE *__stream);

extern FILE *fopen (__const char *__restrict __filename,
      __const char *__restrict __modes) ;
extern FILE *freopen (__const char *__restrict __filename,
        __const char *__restrict __modes,
        FILE *__restrict __stream) ;

extern FILE *fdopen (int __fd, __const char *__modes) __attribute__ ((__nothrow__)) ;
extern FILE *fmemopen (void *__s, size_t __len, __const char *__modes)
  __attribute__ ((__nothrow__)) ;
extern FILE *open_memstream (char **__bufloc, size_t *__sizeloc) __attribute__ ((__nothrow__)) ;

extern void setbuf (FILE *__restrict __stream, char *__restrict __buf) __attribute__ ((__nothrow__));
extern int setvbuf (FILE *__restrict __stream, char *__restrict __buf,
      int __modes, size_t __n) __attribute__ ((__nothrow__));

extern void setbuffer (FILE *__restrict __stream, char *__restrict __buf,
         size_t __size) __attribute__ ((__nothrow__));
extern void setlinebuf (FILE *__stream) __attribute__ ((__nothrow__));

extern int fprintf (FILE *__restrict __stream,
      __const char *__restrict __format, ...);
extern int printf (__const char *__restrict __format, ...);
extern int sprintf (char *__restrict __s,
      __const char *__restrict __format, ...) __attribute__ ((__nothrow__));
extern int vfprintf (FILE *__restrict __s, __const char *__restrict __format,
       __gnuc_va_list __arg);
extern int vprintf (__const char *__restrict __format, __gnuc_va_list __arg);
extern int vsprintf (char *__restrict __s, __const char *__restrict __format,
       __gnuc_va_list __arg) __attribute__ ((__nothrow__));


extern int snprintf (char *__restrict __s, size_t __maxlen,
       __const char *__restrict __format, ...)
     __attribute__ ((__nothrow__)) __attribute__ ((__format__ (__printf__, 3, 4)));
extern int vsnprintf (char *__restrict __s, size_t __maxlen,
        __const char *__restrict __format, __gnuc_va_list __arg)
     __attribute__ ((__nothrow__)) __attribute__ ((__format__ (__printf__, 3, 0)));

extern int vdprintf (int __fd, __const char *__restrict __fmt,
       __gnuc_va_list __arg)
     __attribute__ ((__format__ (__printf__, 2, 0)));
extern int dprintf (int __fd, __const char *__restrict __fmt, ...)
     __attribute__ ((__format__ (__printf__, 2, 3)));

extern int fscanf (FILE *__restrict __stream,
     __const char *__restrict __format, ...) ;
extern int scanf (__const char *__restrict __format, ...) ;
extern int sscanf (__const char *__restrict __s,
     __const char *__restrict __format, ...) __attribute__ ((__nothrow__));
extern int fscanf (FILE *__restrict __stream, __const char *__restrict __format, ...) __asm__ ("" "__isoc99_fscanf") ;
extern int scanf (__const char *__restrict __format, ...) __asm__ ("" "__isoc99_scanf") ;
extern int sscanf (__const char *__restrict __s, __const char *__restrict __format, ...) __asm__ ("" "__isoc99_sscanf") __attribute__ ((__nothrow__));


extern int vfscanf (FILE *__restrict __s, __const char *__restrict __format,
      __gnuc_va_list __arg)
     __attribute__ ((__format__ (__scanf__, 2, 0))) ;
extern int vscanf (__const char *__restrict __format, __gnuc_va_list __arg)
     __attribute__ ((__format__ (__scanf__, 1, 0))) ;
extern int vsscanf (__const char *__restrict __s,
      __const char *__restrict __format, __gnuc_va_list __arg)
     __attribute__ ((__nothrow__)) __attribute__ ((__format__ (__scanf__, 2, 0)));
extern int vfscanf (FILE *__restrict __s, __const char *__restrict __format, __gnuc_va_list __arg) __asm__ ("" "__isoc99_vfscanf")
     __attribute__ ((__format__ (__scanf__, 2, 0))) ;
extern int vscanf (__const char *__restrict __format, __gnuc_va_list __arg) __asm__ ("" "__isoc99_vscanf")
     __attribute__ ((__format__ (__scanf__, 1, 0))) ;
extern int vsscanf (__const char *__restrict __s, __const char *__restrict __format, __gnuc_va_list __arg) __asm__ ("" "__isoc99_vsscanf")
     __attribute__ ((__nothrow__)) __attribute__ ((__format__ (__scanf__, 2, 0)));


extern int fgetc (FILE *__stream);
extern int getc (FILE *__stream);
extern int getchar (void);

extern int getc_unlocked (FILE *__stream);
extern int getchar_unlocked (void);
extern int fgetc_unlocked (FILE *__stream);

extern int fputc (int __c, FILE *__stream);
extern int putc (int __c, FILE *__stream);
extern int putchar (int __c);

extern int fputc_unlocked (int __c, FILE *__stream);
extern int putc_unlocked (int __c, FILE *__stream);
extern int putchar_unlocked (int __c);
extern int getw (FILE *__stream);
extern int putw (int __w, FILE *__stream);

extern char *fgets (char *__restrict __s, int __n, FILE *__restrict __stream)
     ;
extern char *gets (char *__s) ;

extern __ssize_t __getdelim (char **__restrict __lineptr,
          size_t *__restrict __n, int __delimiter,
          FILE *__restrict __stream) ;
extern __ssize_t getdelim (char **__restrict __lineptr,
        size_t *__restrict __n, int __delimiter,
        FILE *__restrict __stream) ;
extern __ssize_t getline (char **__restrict __lineptr,
       size_t *__restrict __n,
       FILE *__restrict __stream) ;

extern int fputs (__const char *__restrict __s, FILE *__restrict __stream);
extern int puts (__const char *__s);
extern int ungetc (int __c, FILE *__stream);
extern size_t fread (void *__restrict __ptr, size_t __size,
       size_t __n, FILE *__restrict __stream) ;
extern size_t fwrite (__const void *__restrict __ptr, size_t __size,
        size_t __n, FILE *__restrict __s) ;

extern size_t fread_unlocked (void *__restrict __ptr, size_t __size,
         size_t __n, FILE *__restrict __stream) ;
extern size_t fwrite_unlocked (__const void *__restrict __ptr, size_t __size,
          size_t __n, FILE *__restrict __stream) ;

extern int fseek (FILE *__stream, long int __off, int __whence);
extern long int ftell (FILE *__stream) ;
extern void rewind (FILE *__stream);

extern int fseeko (FILE *__stream, __off_t __off, int __whence);
extern __off_t ftello (FILE *__stream) ;

extern int fgetpos (FILE *__restrict __stream, fpos_t *__restrict __pos);
extern int fsetpos (FILE *__stream, __const fpos_t *__pos);


extern void clearerr (FILE *__stream) __attribute__ ((__nothrow__));
extern int feof (FILE *__stream) __attribute__ ((__nothrow__)) ;
extern int ferror (FILE *__stream) __attribute__ ((__nothrow__)) ;

extern void clearerr_unlocked (FILE *__stream) __attribute__ ((__nothrow__));
extern int feof_unlocked (FILE *__stream) __attribute__ ((__nothrow__)) ;
extern int ferror_unlocked (FILE *__stream) __attribute__ ((__nothrow__)) ;

extern void perror (__const char *__s);

extern int sys_nerr;
extern __const char *__const sys_errlist[];
extern int fileno (FILE *__stream) __attribute__ ((__nothrow__)) ;
extern int fileno_unlocked (FILE *__stream) __attribute__ ((__nothrow__)) ;
extern FILE *popen (__const char *__command, __const char *__modes) ;
extern int pclose (FILE *__stream);
extern char *ctermid (char *__s) __attribute__ ((__nothrow__));
extern void flockfile (FILE *__stream) __attribute__ ((__nothrow__));
extern int ftrylockfile (FILE *__stream) __attribute__ ((__nothrow__)) ;
extern void funlockfile (FILE *__stream) __attribute__ ((__nothrow__));
extern __inline __attribute__ ((__gnu_inline__)) int
vprintf (__const char *__restrict __fmt, __gnuc_va_list __arg)
{
  return vfprintf (stdout, __fmt, __arg);
}
extern __inline __attribute__ ((__gnu_inline__)) int
getchar (void)
{
  return _IO_getc (stdin);
}
extern __inline __attribute__ ((__gnu_inline__)) int
fgetc_unlocked (FILE *__fp)
{
  return (__builtin_expect (((__fp)->_IO_read_ptr >= (__fp)->_IO_read_end), 0) ? __uflow (__fp) : *(unsigned char *) (__fp)->_IO_read_ptr++);
}
extern __inline __attribute__ ((__gnu_inline__)) int
getc_unlocked (FILE *__fp)
{
  return (__builtin_expect (((__fp)->_IO_read_ptr >= (__fp)->_IO_read_end), 0) ? __uflow (__fp) : *(unsigned char *) (__fp)->_IO_read_ptr++);
}
extern __inline __attribute__ ((__gnu_inline__)) int
getchar_unlocked (void)
{
  return (__builtin_expect (((stdin)->_IO_read_ptr >= (stdin)->_IO_read_end), 0) ? __uflow (stdin) : *(unsigned char *) (stdin)->_IO_read_ptr++);
}
extern __inline __attribute__ ((__gnu_inline__)) int
putchar (int __c)
{
  return _IO_putc (__c, stdout);
}
extern __inline __attribute__ ((__gnu_inline__)) int
fputc_unlocked (int __c, FILE *__stream)
{
  return (__builtin_expect (((__stream)->_IO_write_ptr >= (__stream)->_IO_write_end), 0) ? __overflow (__stream, (unsigned char) (__c)) : (unsigned char) (*(__stream)->_IO_write_ptr++ = (__c)));
}
extern __inline __attribute__ ((__gnu_inline__)) int
putc_unlocked (int __c, FILE *__stream)
{
  return (__builtin_expect (((__stream)->_IO_write_ptr >= (__stream)->_IO_write_end), 0) ? __overflow (__stream, (unsigned char) (__c)) : (unsigned char) (*(__stream)->_IO_write_ptr++ = (__c)));
}
extern __inline __attribute__ ((__gnu_inline__)) int
putchar_unlocked (int __c)
{
  return (__builtin_expect (((stdout)->_IO_write_ptr >= (stdout)->_IO_write_end), 0) ? __overflow (stdout, (unsigned char) (__c)) : (unsigned char) (*(stdout)->_IO_write_ptr++ = (__c)));
}
extern __inline __attribute__ ((__gnu_inline__)) int
__attribute__ ((__nothrow__)) feof_unlocked (FILE *__stream)
{
  return (((__stream)->_flags & 0x10) != 0);
}
extern __inline __attribute__ ((__gnu_inline__)) int
__attribute__ ((__nothrow__)) ferror_unlocked (FILE *__stream)
{
  return (((__stream)->_flags & 0x20) != 0);
}

typedef int wchar_t;

union wait
  {
    int w_status;
    struct
      {
 unsigned int __w_termsig:7;
 unsigned int __w_coredump:1;
 unsigned int __w_retcode:8;
 unsigned int:16;
      } __wait_terminated;
    struct
      {
 unsigned int __w_stopval:8;
 unsigned int __w_stopsig:8;
 unsigned int:16;
      } __wait_stopped;
  };
typedef union
  {
    union wait *__uptr;
    int *__iptr;
  } __WAIT_STATUS __attribute__ ((__transparent_union__));

typedef struct
  {
    int quot;
    int rem;
  } div_t;
typedef struct
  {
    long int quot;
    long int rem;
  } ldiv_t;


__extension__ typedef struct
  {
    long long int quot;
    long long int rem;
  } lldiv_t;

extern size_t __ctype_get_mb_cur_max (void) __attribute__ ((__nothrow__)) ;

extern double atof (__const char *__nptr)
     __attribute__ ((__nothrow__)) __attribute__ ((__pure__)) __attribute__ ((__nonnull__ (1))) ;
extern int atoi (__const char *__nptr)
     __attribute__ ((__nothrow__)) __attribute__ ((__pure__)) __attribute__ ((__nonnull__ (1))) ;
extern long int atol (__const char *__nptr)
     __attribute__ ((__nothrow__)) __attribute__ ((__pure__)) __attribute__ ((__nonnull__ (1))) ;


__extension__ extern long long int atoll (__const char *__nptr)
     __attribute__ ((__nothrow__)) __attribute__ ((__pure__)) __attribute__ ((__nonnull__ (1))) ;


extern double strtod (__const char *__restrict __nptr,
        char **__restrict __endptr)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1))) ;


extern float strtof (__const char *__restrict __nptr,
       char **__restrict __endptr) __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1))) ;
extern long double strtold (__const char *__restrict __nptr,
       char **__restrict __endptr)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1))) ;


extern long int strtol (__const char *__restrict __nptr,
   char **__restrict __endptr, int __base)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1))) ;
extern unsigned long int strtoul (__const char *__restrict __nptr,
      char **__restrict __endptr, int __base)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1))) ;

__extension__
extern long long int strtoq (__const char *__restrict __nptr,
        char **__restrict __endptr, int __base)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1))) ;
__extension__
extern unsigned long long int strtouq (__const char *__restrict __nptr,
           char **__restrict __endptr, int __base)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1))) ;

__extension__
extern long long int strtoll (__const char *__restrict __nptr,
         char **__restrict __endptr, int __base)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1))) ;
__extension__
extern unsigned long long int strtoull (__const char *__restrict __nptr,
     char **__restrict __endptr, int __base)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1))) ;


extern __inline __attribute__ ((__gnu_inline__)) double
__attribute__ ((__nothrow__)) atof (__const char *__nptr)
{
  return strtod (__nptr, (char **) ((void *)0));
}
extern __inline __attribute__ ((__gnu_inline__)) int
__attribute__ ((__nothrow__)) atoi (__const char *__nptr)
{
  return (int) strtol (__nptr, (char **) ((void *)0), 10);
}
extern __inline __attribute__ ((__gnu_inline__)) long int
__attribute__ ((__nothrow__)) atol (__const char *__nptr)
{
  return strtol (__nptr, (char **) ((void *)0), 10);
}


__extension__ extern __inline __attribute__ ((__gnu_inline__)) long long int
__attribute__ ((__nothrow__)) atoll (__const char *__nptr)
{
  return strtoll (__nptr, (char **) ((void *)0), 10);
}

extern char *l64a (long int __n) __attribute__ ((__nothrow__)) ;
extern long int a64l (__const char *__s)
     __attribute__ ((__nothrow__)) __attribute__ ((__pure__)) __attribute__ ((__nonnull__ (1))) ;

typedef __u_char u_char;
typedef __u_short u_short;
typedef __u_int u_int;
typedef __u_long u_long;
typedef __quad_t quad_t;
typedef __u_quad_t u_quad_t;
typedef __fsid_t fsid_t;
typedef __loff_t loff_t;
typedef __ino_t ino_t;
typedef __dev_t dev_t;
typedef __gid_t gid_t;
typedef __mode_t mode_t;
typedef __nlink_t nlink_t;
typedef __uid_t uid_t;
typedef __pid_t pid_t;
typedef __id_t id_t;
typedef __daddr_t daddr_t;
typedef __caddr_t caddr_t;
typedef __key_t key_t;

typedef __clock_t clock_t;



typedef __time_t time_t;


typedef __clockid_t clockid_t;
typedef __timer_t timer_t;
typedef unsigned long int ulong;
typedef unsigned short int ushort;
typedef unsigned int uint;
typedef int int8_t __attribute__ ((__mode__ (__QI__)));
typedef int int16_t __attribute__ ((__mode__ (__HI__)));
typedef int int32_t __attribute__ ((__mode__ (__SI__)));
typedef int int64_t __attribute__ ((__mode__ (__DI__)));
typedef unsigned int u_int8_t __attribute__ ((__mode__ (__QI__)));
typedef unsigned int u_int16_t __attribute__ ((__mode__ (__HI__)));
typedef unsigned int u_int32_t __attribute__ ((__mode__ (__SI__)));
typedef unsigned int u_int64_t __attribute__ ((__mode__ (__DI__)));
typedef int register_t __attribute__ ((__mode__ (__word__)));
typedef int __sig_atomic_t;
typedef struct
  {
    unsigned long int __val[(1024 / (8 * sizeof (unsigned long int)))];
  } __sigset_t;
typedef __sigset_t sigset_t;
struct timespec
  {
    __time_t tv_sec;
    long int tv_nsec;
  };
struct timeval
  {
    __time_t tv_sec;
    __suseconds_t tv_usec;
  };
typedef __suseconds_t suseconds_t;
typedef long int __fd_mask;
typedef struct
  {
    __fd_mask __fds_bits[1024 / (8 * (int) sizeof (__fd_mask))];
  } fd_set;
typedef __fd_mask fd_mask;

extern int select (int __nfds, fd_set *__restrict __readfds,
     fd_set *__restrict __writefds,
     fd_set *__restrict __exceptfds,
     struct timeval *__restrict __timeout);
extern int pselect (int __nfds, fd_set *__restrict __readfds,
      fd_set *__restrict __writefds,
      fd_set *__restrict __exceptfds,
      const struct timespec *__restrict __timeout,
      const __sigset_t *__restrict __sigmask);

__extension__
extern unsigned int gnu_dev_major (unsigned long long int __dev)
     __attribute__ ((__nothrow__));
__extension__
extern unsigned int gnu_dev_minor (unsigned long long int __dev)
     __attribute__ ((__nothrow__));
__extension__
extern unsigned long long int gnu_dev_makedev (unsigned int __major,
            unsigned int __minor)
     __attribute__ ((__nothrow__));
__extension__ extern __inline __attribute__ ((__gnu_inline__)) unsigned int
__attribute__ ((__nothrow__)) gnu_dev_major (unsigned long long int __dev)
{
  return ((__dev >> 8) & 0xfff) | ((unsigned int) (__dev >> 32) & ~0xfff);
}
__extension__ extern __inline __attribute__ ((__gnu_inline__)) unsigned int
__attribute__ ((__nothrow__)) gnu_dev_minor (unsigned long long int __dev)
{
  return (__dev & 0xff) | ((unsigned int) (__dev >> 12) & ~0xff);
}
__extension__ extern __inline __attribute__ ((__gnu_inline__)) unsigned long long int
__attribute__ ((__nothrow__)) gnu_dev_makedev (unsigned int __major, unsigned int __minor)
{
  return ((__minor & 0xff) | ((__major & 0xfff) << 8)
   | (((unsigned long long int) (__minor & ~0xff)) << 12)
   | (((unsigned long long int) (__major & ~0xfff)) << 32));
}
typedef __blksize_t blksize_t;
typedef __blkcnt_t blkcnt_t;
typedef __fsblkcnt_t fsblkcnt_t;
typedef __fsfilcnt_t fsfilcnt_t;
typedef unsigned long int pthread_t;
typedef union
{
  char __size[56];
  long int __align;
} pthread_attr_t;
typedef struct __pthread_internal_list
{
  struct __pthread_internal_list *__prev;
  struct __pthread_internal_list *__next;
} __pthread_list_t;
typedef union
{
  struct __pthread_mutex_s
  {
    int __lock;
    unsigned int __count;
    int __owner;
    unsigned int __nusers;
    int __kind;
    int __spins;
    __pthread_list_t __list;
  } __data;
  char __size[40];
  long int __align;
} pthread_mutex_t;
typedef union
{
  char __size[4];
  int __align;
} pthread_mutexattr_t;
typedef union
{
  struct
  {
    int __lock;
    unsigned int __futex;
    __extension__ unsigned long long int __total_seq;
    __extension__ unsigned long long int __wakeup_seq;
    __extension__ unsigned long long int __woken_seq;
    void *__mutex;
    unsigned int __nwaiters;
    unsigned int __broadcast_seq;
  } __data;
  char __size[48];
  __extension__ long long int __align;
} pthread_cond_t;
typedef union
{
  char __size[4];
  int __align;
} pthread_condattr_t;
typedef unsigned int pthread_key_t;
typedef int pthread_once_t;
typedef union
{
  struct
  {
    int __lock;
    unsigned int __nr_readers;
    unsigned int __readers_wakeup;
    unsigned int __writer_wakeup;
    unsigned int __nr_readers_queued;
    unsigned int __nr_writers_queued;
    int __writer;
    int __shared;
    unsigned long int __pad1;
    unsigned long int __pad2;
    unsigned int __flags;
  } __data;
  char __size[56];
  long int __align;
} pthread_rwlock_t;
typedef union
{
  char __size[8];
  long int __align;
} pthread_rwlockattr_t;
typedef volatile int pthread_spinlock_t;
typedef union
{
  char __size[32];
  long int __align;
} pthread_barrier_t;
typedef union
{
  char __size[4];
  int __align;
} pthread_barrierattr_t;

extern long int random (void) __attribute__ ((__nothrow__));
extern void srandom (unsigned int __seed) __attribute__ ((__nothrow__));
extern char *initstate (unsigned int __seed, char *__statebuf,
   size_t __statelen) __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (2)));
extern char *setstate (char *__statebuf) __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1)));
struct random_data
  {
    int32_t *fptr;
    int32_t *rptr;
    int32_t *state;
    int rand_type;
    int rand_deg;
    int rand_sep;
    int32_t *end_ptr;
  };
extern int random_r (struct random_data *__restrict __buf,
       int32_t *__restrict __result) __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1, 2)));
extern int srandom_r (unsigned int __seed, struct random_data *__buf)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (2)));
extern int initstate_r (unsigned int __seed, char *__restrict __statebuf,
   size_t __statelen,
   struct random_data *__restrict __buf)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (2, 4)));
extern int setstate_r (char *__restrict __statebuf,
         struct random_data *__restrict __buf)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1, 2)));

extern int rand (void) __attribute__ ((__nothrow__));
extern void srand (unsigned int __seed) __attribute__ ((__nothrow__));

extern int rand_r (unsigned int *__seed) __attribute__ ((__nothrow__));
extern double drand48 (void) __attribute__ ((__nothrow__));
extern double erand48 (unsigned short int __xsubi[3]) __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1)));
extern long int lrand48 (void) __attribute__ ((__nothrow__));
extern long int nrand48 (unsigned short int __xsubi[3])
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1)));
extern long int mrand48 (void) __attribute__ ((__nothrow__));
extern long int jrand48 (unsigned short int __xsubi[3])
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1)));
extern void srand48 (long int __seedval) __attribute__ ((__nothrow__));
extern unsigned short int *seed48 (unsigned short int __seed16v[3])
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1)));
extern void lcong48 (unsigned short int __param[7]) __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1)));
struct drand48_data
  {
    unsigned short int __x[3];
    unsigned short int __old_x[3];
    unsigned short int __c;
    unsigned short int __init;
    unsigned long long int __a;
  };
extern int drand48_r (struct drand48_data *__restrict __buffer,
        double *__restrict __result) __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1, 2)));
extern int erand48_r (unsigned short int __xsubi[3],
        struct drand48_data *__restrict __buffer,
        double *__restrict __result) __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1, 2)));
extern int lrand48_r (struct drand48_data *__restrict __buffer,
        long int *__restrict __result)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1, 2)));
extern int nrand48_r (unsigned short int __xsubi[3],
        struct drand48_data *__restrict __buffer,
        long int *__restrict __result)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1, 2)));
extern int mrand48_r (struct drand48_data *__restrict __buffer,
        long int *__restrict __result)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1, 2)));
extern int jrand48_r (unsigned short int __xsubi[3],
        struct drand48_data *__restrict __buffer,
        long int *__restrict __result)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1, 2)));
extern int srand48_r (long int __seedval, struct drand48_data *__buffer)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (2)));
extern int seed48_r (unsigned short int __seed16v[3],
       struct drand48_data *__buffer) __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1, 2)));
extern int lcong48_r (unsigned short int __param[7],
        struct drand48_data *__buffer)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1, 2)));

extern void *malloc (size_t __size) __attribute__ ((__nothrow__)) __attribute__ ((__malloc__)) ;
extern void *calloc (size_t __nmemb, size_t __size)
     __attribute__ ((__nothrow__)) __attribute__ ((__malloc__)) ;


extern void *realloc (void *__ptr, size_t __size)
     __attribute__ ((__nothrow__)) __attribute__ ((__warn_unused_result__));
extern void free (void *__ptr) __attribute__ ((__nothrow__));

extern void cfree (void *__ptr) __attribute__ ((__nothrow__));

extern void *alloca (size_t __size) __attribute__ ((__nothrow__));

extern void *valloc (size_t __size) __attribute__ ((__nothrow__)) __attribute__ ((__malloc__)) ;
extern int posix_memalign (void **__memptr, size_t __alignment, size_t __size)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1))) ;

extern void abort (void) __attribute__ ((__nothrow__)) __attribute__ ((__noreturn__));
extern int atexit (void (*__func) (void)) __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1)));

extern int on_exit (void (*__func) (int __status, void *__arg), void *__arg)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1)));

extern void exit (int __status) __attribute__ ((__nothrow__)) __attribute__ ((__noreturn__));


extern void _Exit (int __status) __attribute__ ((__nothrow__)) __attribute__ ((__noreturn__));


extern char *getenv (__const char *__name) __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1))) ;

extern char *__secure_getenv (__const char *__name)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1))) ;
extern int putenv (char *__string) __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1)));
extern int setenv (__const char *__name, __const char *__value, int __replace)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (2)));
extern int unsetenv (__const char *__name) __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1)));
extern int clearenv (void) __attribute__ ((__nothrow__));
extern char *mktemp (char *__template) __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1))) ;
extern int mkstemp (char *__template) __attribute__ ((__nonnull__ (1))) ;
extern int mkstemps (char *__template, int __suffixlen) __attribute__ ((__nonnull__ (1))) ;
extern char *mkdtemp (char *__template) __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1))) ;

extern int system (__const char *__command) ;

extern char *realpath (__const char *__restrict __name,
         char *__restrict __resolved) __attribute__ ((__nothrow__)) ;
typedef int (*__compar_fn_t) (__const void *, __const void *);

extern void *bsearch (__const void *__key, __const void *__base,
        size_t __nmemb, size_t __size, __compar_fn_t __compar)
     __attribute__ ((__nonnull__ (1, 2, 5))) ;
extern void qsort (void *__base, size_t __nmemb, size_t __size,
     __compar_fn_t __compar) __attribute__ ((__nonnull__ (1, 4)));
extern int abs (int __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__)) ;
extern long int labs (long int __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__)) ;

__extension__ extern long long int llabs (long long int __x)
     __attribute__ ((__nothrow__)) __attribute__ ((__const__)) ;

extern div_t div (int __numer, int __denom)
     __attribute__ ((__nothrow__)) __attribute__ ((__const__)) ;
extern ldiv_t ldiv (long int __numer, long int __denom)
     __attribute__ ((__nothrow__)) __attribute__ ((__const__)) ;


__extension__ extern lldiv_t lldiv (long long int __numer,
        long long int __denom)
     __attribute__ ((__nothrow__)) __attribute__ ((__const__)) ;

extern char *ecvt (double __value, int __ndigit, int *__restrict __decpt,
     int *__restrict __sign) __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (3, 4))) ;
extern char *fcvt (double __value, int __ndigit, int *__restrict __decpt,
     int *__restrict __sign) __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (3, 4))) ;
extern char *gcvt (double __value, int __ndigit, char *__buf)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (3))) ;
extern char *qecvt (long double __value, int __ndigit,
      int *__restrict __decpt, int *__restrict __sign)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (3, 4))) ;
extern char *qfcvt (long double __value, int __ndigit,
      int *__restrict __decpt, int *__restrict __sign)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (3, 4))) ;
extern char *qgcvt (long double __value, int __ndigit, char *__buf)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (3))) ;
extern int ecvt_r (double __value, int __ndigit, int *__restrict __decpt,
     int *__restrict __sign, char *__restrict __buf,
     size_t __len) __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (3, 4, 5)));
extern int fcvt_r (double __value, int __ndigit, int *__restrict __decpt,
     int *__restrict __sign, char *__restrict __buf,
     size_t __len) __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (3, 4, 5)));
extern int qecvt_r (long double __value, int __ndigit,
      int *__restrict __decpt, int *__restrict __sign,
      char *__restrict __buf, size_t __len)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (3, 4, 5)));
extern int qfcvt_r (long double __value, int __ndigit,
      int *__restrict __decpt, int *__restrict __sign,
      char *__restrict __buf, size_t __len)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (3, 4, 5)));

extern int mblen (__const char *__s, size_t __n) __attribute__ ((__nothrow__)) ;
extern int mbtowc (wchar_t *__restrict __pwc,
     __const char *__restrict __s, size_t __n) __attribute__ ((__nothrow__)) ;
extern int wctomb (char *__s, wchar_t __wchar) __attribute__ ((__nothrow__)) ;
extern size_t mbstowcs (wchar_t *__restrict __pwcs,
   __const char *__restrict __s, size_t __n) __attribute__ ((__nothrow__));
extern size_t wcstombs (char *__restrict __s,
   __const wchar_t *__restrict __pwcs, size_t __n)
     __attribute__ ((__nothrow__));

extern int rpmatch (__const char *__response) __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1))) ;
extern int getsubopt (char **__restrict __optionp,
        char *__const *__restrict __tokens,
        char **__restrict __valuep)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1, 2, 3))) ;
extern int getloadavg (double __loadavg[], int __nelem)
     __attribute__ ((__nothrow__)) __attribute__ ((__nonnull__ (1)));


typedef float float_t;
typedef double double_t;

extern double acos (double __x) __attribute__ ((__nothrow__)); extern double __acos (double __x) __attribute__ ((__nothrow__));
extern double asin (double __x) __attribute__ ((__nothrow__)); extern double __asin (double __x) __attribute__ ((__nothrow__));
extern double atan (double __x) __attribute__ ((__nothrow__)); extern double __atan (double __x) __attribute__ ((__nothrow__));
extern double atan2 (double __y, double __x) __attribute__ ((__nothrow__)); extern double __atan2 (double __y, double __x) __attribute__ ((__nothrow__));
extern double cos (double __x) __attribute__ ((__nothrow__)); extern double __cos (double __x) __attribute__ ((__nothrow__));
extern double sin (double __x) __attribute__ ((__nothrow__)); extern double __sin (double __x) __attribute__ ((__nothrow__));
extern double tan (double __x) __attribute__ ((__nothrow__)); extern double __tan (double __x) __attribute__ ((__nothrow__));
extern double cosh (double __x) __attribute__ ((__nothrow__)); extern double __cosh (double __x) __attribute__ ((__nothrow__));
extern double sinh (double __x) __attribute__ ((__nothrow__)); extern double __sinh (double __x) __attribute__ ((__nothrow__));
extern double tanh (double __x) __attribute__ ((__nothrow__)); extern double __tanh (double __x) __attribute__ ((__nothrow__));


extern double acosh (double __x) __attribute__ ((__nothrow__)); extern double __acosh (double __x) __attribute__ ((__nothrow__));
extern double asinh (double __x) __attribute__ ((__nothrow__)); extern double __asinh (double __x) __attribute__ ((__nothrow__));
extern double atanh (double __x) __attribute__ ((__nothrow__)); extern double __atanh (double __x) __attribute__ ((__nothrow__));


extern double exp (double __x) __attribute__ ((__nothrow__)); extern double __exp (double __x) __attribute__ ((__nothrow__));
extern double frexp (double __x, int *__exponent) __attribute__ ((__nothrow__)); extern double __frexp (double __x, int *__exponent) __attribute__ ((__nothrow__));
extern double ldexp (double __x, int __exponent) __attribute__ ((__nothrow__)); extern double __ldexp (double __x, int __exponent) __attribute__ ((__nothrow__));
extern double log (double __x) __attribute__ ((__nothrow__)); extern double __log (double __x) __attribute__ ((__nothrow__));
extern double log10 (double __x) __attribute__ ((__nothrow__)); extern double __log10 (double __x) __attribute__ ((__nothrow__));
extern double modf (double __x, double *__iptr) __attribute__ ((__nothrow__)); extern double __modf (double __x, double *__iptr) __attribute__ ((__nothrow__));


extern double expm1 (double __x) __attribute__ ((__nothrow__)); extern double __expm1 (double __x) __attribute__ ((__nothrow__));
extern double log1p (double __x) __attribute__ ((__nothrow__)); extern double __log1p (double __x) __attribute__ ((__nothrow__));
extern double logb (double __x) __attribute__ ((__nothrow__)); extern double __logb (double __x) __attribute__ ((__nothrow__));


extern double exp2 (double __x) __attribute__ ((__nothrow__)); extern double __exp2 (double __x) __attribute__ ((__nothrow__));
extern double log2 (double __x) __attribute__ ((__nothrow__)); extern double __log2 (double __x) __attribute__ ((__nothrow__));


extern double pow (double __x, double __y) __attribute__ ((__nothrow__)); extern double __pow (double __x, double __y) __attribute__ ((__nothrow__));
extern double sqrt (double __x) __attribute__ ((__nothrow__)); extern double __sqrt (double __x) __attribute__ ((__nothrow__));


extern double hypot (double __x, double __y) __attribute__ ((__nothrow__)); extern double __hypot (double __x, double __y) __attribute__ ((__nothrow__));


extern double cbrt (double __x) __attribute__ ((__nothrow__)); extern double __cbrt (double __x) __attribute__ ((__nothrow__));


extern double ceil (double __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern double __ceil (double __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern double fabs (double __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern double __fabs (double __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern double floor (double __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern double __floor (double __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern double fmod (double __x, double __y) __attribute__ ((__nothrow__)); extern double __fmod (double __x, double __y) __attribute__ ((__nothrow__));
extern int __isinf (double __value) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern int __finite (double __value) __attribute__ ((__nothrow__)) __attribute__ ((__const__));

extern int isinf (double __value) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern int finite (double __value) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern double drem (double __x, double __y) __attribute__ ((__nothrow__)); extern double __drem (double __x, double __y) __attribute__ ((__nothrow__));
extern double significand (double __x) __attribute__ ((__nothrow__)); extern double __significand (double __x) __attribute__ ((__nothrow__));

extern double copysign (double __x, double __y) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern double __copysign (double __x, double __y) __attribute__ ((__nothrow__)) __attribute__ ((__const__));


extern double nan (__const char *__tagb) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern double __nan (__const char *__tagb) __attribute__ ((__nothrow__)) __attribute__ ((__const__));

extern int __isnan (double __value) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern int isnan (double __value) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern double j0 (double) __attribute__ ((__nothrow__)); extern double __j0 (double) __attribute__ ((__nothrow__));
extern double j1 (double) __attribute__ ((__nothrow__)); extern double __j1 (double) __attribute__ ((__nothrow__));
extern double jn (int, double) __attribute__ ((__nothrow__)); extern double __jn (int, double) __attribute__ ((__nothrow__));
extern double y0 (double) __attribute__ ((__nothrow__)); extern double __y0 (double) __attribute__ ((__nothrow__));
extern double y1 (double) __attribute__ ((__nothrow__)); extern double __y1 (double) __attribute__ ((__nothrow__));
extern double yn (int, double) __attribute__ ((__nothrow__)); extern double __yn (int, double) __attribute__ ((__nothrow__));

extern double erf (double) __attribute__ ((__nothrow__)); extern double __erf (double) __attribute__ ((__nothrow__));
extern double erfc (double) __attribute__ ((__nothrow__)); extern double __erfc (double) __attribute__ ((__nothrow__));
extern double lgamma (double) __attribute__ ((__nothrow__)); extern double __lgamma (double) __attribute__ ((__nothrow__));


extern double tgamma (double) __attribute__ ((__nothrow__)); extern double __tgamma (double) __attribute__ ((__nothrow__));

extern double gamma (double) __attribute__ ((__nothrow__)); extern double __gamma (double) __attribute__ ((__nothrow__));
extern double lgamma_r (double, int *__signgamp) __attribute__ ((__nothrow__)); extern double __lgamma_r (double, int *__signgamp) __attribute__ ((__nothrow__));

extern double rint (double __x) __attribute__ ((__nothrow__)); extern double __rint (double __x) __attribute__ ((__nothrow__));
extern double nextafter (double __x, double __y) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern double __nextafter (double __x, double __y) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern double nexttoward (double __x, long double __y) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern double __nexttoward (double __x, long double __y) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern double remainder (double __x, double __y) __attribute__ ((__nothrow__)); extern double __remainder (double __x, double __y) __attribute__ ((__nothrow__));
extern double scalbn (double __x, int __n) __attribute__ ((__nothrow__)); extern double __scalbn (double __x, int __n) __attribute__ ((__nothrow__));
extern int ilogb (double __x) __attribute__ ((__nothrow__)); extern int __ilogb (double __x) __attribute__ ((__nothrow__));
extern double scalbln (double __x, long int __n) __attribute__ ((__nothrow__)); extern double __scalbln (double __x, long int __n) __attribute__ ((__nothrow__));
extern double nearbyint (double __x) __attribute__ ((__nothrow__)); extern double __nearbyint (double __x) __attribute__ ((__nothrow__));
extern double round (double __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern double __round (double __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern double trunc (double __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern double __trunc (double __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern double remquo (double __x, double __y, int *__quo) __attribute__ ((__nothrow__)); extern double __remquo (double __x, double __y, int *__quo) __attribute__ ((__nothrow__));
extern long int lrint (double __x) __attribute__ ((__nothrow__)); extern long int __lrint (double __x) __attribute__ ((__nothrow__));
extern long long int llrint (double __x) __attribute__ ((__nothrow__)); extern long long int __llrint (double __x) __attribute__ ((__nothrow__));
extern long int lround (double __x) __attribute__ ((__nothrow__)); extern long int __lround (double __x) __attribute__ ((__nothrow__));
extern long long int llround (double __x) __attribute__ ((__nothrow__)); extern long long int __llround (double __x) __attribute__ ((__nothrow__));
extern double fdim (double __x, double __y) __attribute__ ((__nothrow__)); extern double __fdim (double __x, double __y) __attribute__ ((__nothrow__));
extern double fmax (double __x, double __y) __attribute__ ((__nothrow__)); extern double __fmax (double __x, double __y) __attribute__ ((__nothrow__));
extern double fmin (double __x, double __y) __attribute__ ((__nothrow__)); extern double __fmin (double __x, double __y) __attribute__ ((__nothrow__));
extern int __fpclassify (double __value) __attribute__ ((__nothrow__))
     __attribute__ ((__const__));
extern int __signbit (double __value) __attribute__ ((__nothrow__))
     __attribute__ ((__const__));
extern double fma (double __x, double __y, double __z) __attribute__ ((__nothrow__)); extern double __fma (double __x, double __y, double __z) __attribute__ ((__nothrow__));

extern double scalb (double __x, double __n) __attribute__ ((__nothrow__)); extern double __scalb (double __x, double __n) __attribute__ ((__nothrow__));

extern float acosf (float __x) __attribute__ ((__nothrow__)); extern float __acosf (float __x) __attribute__ ((__nothrow__));
extern float asinf (float __x) __attribute__ ((__nothrow__)); extern float __asinf (float __x) __attribute__ ((__nothrow__));
extern float atanf (float __x) __attribute__ ((__nothrow__)); extern float __atanf (float __x) __attribute__ ((__nothrow__));
extern float atan2f (float __y, float __x) __attribute__ ((__nothrow__)); extern float __atan2f (float __y, float __x) __attribute__ ((__nothrow__));
extern float cosf (float __x) __attribute__ ((__nothrow__)); extern float __cosf (float __x) __attribute__ ((__nothrow__));
extern float sinf (float __x) __attribute__ ((__nothrow__)); extern float __sinf (float __x) __attribute__ ((__nothrow__));
extern float tanf (float __x) __attribute__ ((__nothrow__)); extern float __tanf (float __x) __attribute__ ((__nothrow__));
extern float coshf (float __x) __attribute__ ((__nothrow__)); extern float __coshf (float __x) __attribute__ ((__nothrow__));
extern float sinhf (float __x) __attribute__ ((__nothrow__)); extern float __sinhf (float __x) __attribute__ ((__nothrow__));
extern float tanhf (float __x) __attribute__ ((__nothrow__)); extern float __tanhf (float __x) __attribute__ ((__nothrow__));


extern float acoshf (float __x) __attribute__ ((__nothrow__)); extern float __acoshf (float __x) __attribute__ ((__nothrow__));
extern float asinhf (float __x) __attribute__ ((__nothrow__)); extern float __asinhf (float __x) __attribute__ ((__nothrow__));
extern float atanhf (float __x) __attribute__ ((__nothrow__)); extern float __atanhf (float __x) __attribute__ ((__nothrow__));


extern float expf (float __x) __attribute__ ((__nothrow__)); extern float __expf (float __x) __attribute__ ((__nothrow__));
extern float frexpf (float __x, int *__exponent) __attribute__ ((__nothrow__)); extern float __frexpf (float __x, int *__exponent) __attribute__ ((__nothrow__));
extern float ldexpf (float __x, int __exponent) __attribute__ ((__nothrow__)); extern float __ldexpf (float __x, int __exponent) __attribute__ ((__nothrow__));
extern float logf (float __x) __attribute__ ((__nothrow__)); extern float __logf (float __x) __attribute__ ((__nothrow__));
extern float log10f (float __x) __attribute__ ((__nothrow__)); extern float __log10f (float __x) __attribute__ ((__nothrow__));
extern float modff (float __x, float *__iptr) __attribute__ ((__nothrow__)); extern float __modff (float __x, float *__iptr) __attribute__ ((__nothrow__));


extern float expm1f (float __x) __attribute__ ((__nothrow__)); extern float __expm1f (float __x) __attribute__ ((__nothrow__));
extern float log1pf (float __x) __attribute__ ((__nothrow__)); extern float __log1pf (float __x) __attribute__ ((__nothrow__));
extern float logbf (float __x) __attribute__ ((__nothrow__)); extern float __logbf (float __x) __attribute__ ((__nothrow__));


extern float exp2f (float __x) __attribute__ ((__nothrow__)); extern float __exp2f (float __x) __attribute__ ((__nothrow__));
extern float log2f (float __x) __attribute__ ((__nothrow__)); extern float __log2f (float __x) __attribute__ ((__nothrow__));


extern float powf (float __x, float __y) __attribute__ ((__nothrow__)); extern float __powf (float __x, float __y) __attribute__ ((__nothrow__));
extern float sqrtf (float __x) __attribute__ ((__nothrow__)); extern float __sqrtf (float __x) __attribute__ ((__nothrow__));


extern float hypotf (float __x, float __y) __attribute__ ((__nothrow__)); extern float __hypotf (float __x, float __y) __attribute__ ((__nothrow__));


extern float cbrtf (float __x) __attribute__ ((__nothrow__)); extern float __cbrtf (float __x) __attribute__ ((__nothrow__));


extern float ceilf (float __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern float __ceilf (float __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern float fabsf (float __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern float __fabsf (float __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern float floorf (float __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern float __floorf (float __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern float fmodf (float __x, float __y) __attribute__ ((__nothrow__)); extern float __fmodf (float __x, float __y) __attribute__ ((__nothrow__));
extern int __isinff (float __value) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern int __finitef (float __value) __attribute__ ((__nothrow__)) __attribute__ ((__const__));

extern int isinff (float __value) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern int finitef (float __value) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern float dremf (float __x, float __y) __attribute__ ((__nothrow__)); extern float __dremf (float __x, float __y) __attribute__ ((__nothrow__));
extern float significandf (float __x) __attribute__ ((__nothrow__)); extern float __significandf (float __x) __attribute__ ((__nothrow__));

extern float copysignf (float __x, float __y) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern float __copysignf (float __x, float __y) __attribute__ ((__nothrow__)) __attribute__ ((__const__));


extern float nanf (__const char *__tagb) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern float __nanf (__const char *__tagb) __attribute__ ((__nothrow__)) __attribute__ ((__const__));

extern int __isnanf (float __value) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern int isnanf (float __value) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern float j0f (float) __attribute__ ((__nothrow__)); extern float __j0f (float) __attribute__ ((__nothrow__));
extern float j1f (float) __attribute__ ((__nothrow__)); extern float __j1f (float) __attribute__ ((__nothrow__));
extern float jnf (int, float) __attribute__ ((__nothrow__)); extern float __jnf (int, float) __attribute__ ((__nothrow__));
extern float y0f (float) __attribute__ ((__nothrow__)); extern float __y0f (float) __attribute__ ((__nothrow__));
extern float y1f (float) __attribute__ ((__nothrow__)); extern float __y1f (float) __attribute__ ((__nothrow__));
extern float ynf (int, float) __attribute__ ((__nothrow__)); extern float __ynf (int, float) __attribute__ ((__nothrow__));

extern float erff (float) __attribute__ ((__nothrow__)); extern float __erff (float) __attribute__ ((__nothrow__));
extern float erfcf (float) __attribute__ ((__nothrow__)); extern float __erfcf (float) __attribute__ ((__nothrow__));
extern float lgammaf (float) __attribute__ ((__nothrow__)); extern float __lgammaf (float) __attribute__ ((__nothrow__));


extern float tgammaf (float) __attribute__ ((__nothrow__)); extern float __tgammaf (float) __attribute__ ((__nothrow__));

extern float gammaf (float) __attribute__ ((__nothrow__)); extern float __gammaf (float) __attribute__ ((__nothrow__));
extern float lgammaf_r (float, int *__signgamp) __attribute__ ((__nothrow__)); extern float __lgammaf_r (float, int *__signgamp) __attribute__ ((__nothrow__));

extern float rintf (float __x) __attribute__ ((__nothrow__)); extern float __rintf (float __x) __attribute__ ((__nothrow__));
extern float nextafterf (float __x, float __y) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern float __nextafterf (float __x, float __y) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern float nexttowardf (float __x, long double __y) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern float __nexttowardf (float __x, long double __y) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern float remainderf (float __x, float __y) __attribute__ ((__nothrow__)); extern float __remainderf (float __x, float __y) __attribute__ ((__nothrow__));
extern float scalbnf (float __x, int __n) __attribute__ ((__nothrow__)); extern float __scalbnf (float __x, int __n) __attribute__ ((__nothrow__));
extern int ilogbf (float __x) __attribute__ ((__nothrow__)); extern int __ilogbf (float __x) __attribute__ ((__nothrow__));
extern float scalblnf (float __x, long int __n) __attribute__ ((__nothrow__)); extern float __scalblnf (float __x, long int __n) __attribute__ ((__nothrow__));
extern float nearbyintf (float __x) __attribute__ ((__nothrow__)); extern float __nearbyintf (float __x) __attribute__ ((__nothrow__));
extern float roundf (float __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern float __roundf (float __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern float truncf (float __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern float __truncf (float __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern float remquof (float __x, float __y, int *__quo) __attribute__ ((__nothrow__)); extern float __remquof (float __x, float __y, int *__quo) __attribute__ ((__nothrow__));
extern long int lrintf (float __x) __attribute__ ((__nothrow__)); extern long int __lrintf (float __x) __attribute__ ((__nothrow__));
extern long long int llrintf (float __x) __attribute__ ((__nothrow__)); extern long long int __llrintf (float __x) __attribute__ ((__nothrow__));
extern long int lroundf (float __x) __attribute__ ((__nothrow__)); extern long int __lroundf (float __x) __attribute__ ((__nothrow__));
extern long long int llroundf (float __x) __attribute__ ((__nothrow__)); extern long long int __llroundf (float __x) __attribute__ ((__nothrow__));
extern float fdimf (float __x, float __y) __attribute__ ((__nothrow__)); extern float __fdimf (float __x, float __y) __attribute__ ((__nothrow__));
extern float fmaxf (float __x, float __y) __attribute__ ((__nothrow__)); extern float __fmaxf (float __x, float __y) __attribute__ ((__nothrow__));
extern float fminf (float __x, float __y) __attribute__ ((__nothrow__)); extern float __fminf (float __x, float __y) __attribute__ ((__nothrow__));
extern int __fpclassifyf (float __value) __attribute__ ((__nothrow__))
     __attribute__ ((__const__));
extern int __signbitf (float __value) __attribute__ ((__nothrow__))
     __attribute__ ((__const__));
extern float fmaf (float __x, float __y, float __z) __attribute__ ((__nothrow__)); extern float __fmaf (float __x, float __y, float __z) __attribute__ ((__nothrow__));

extern float scalbf (float __x, float __n) __attribute__ ((__nothrow__)); extern float __scalbf (float __x, float __n) __attribute__ ((__nothrow__));

extern long double acosl (long double __x) __attribute__ ((__nothrow__)); extern long double __acosl (long double __x) __attribute__ ((__nothrow__));
extern long double asinl (long double __x) __attribute__ ((__nothrow__)); extern long double __asinl (long double __x) __attribute__ ((__nothrow__));
extern long double atanl (long double __x) __attribute__ ((__nothrow__)); extern long double __atanl (long double __x) __attribute__ ((__nothrow__));
extern long double atan2l (long double __y, long double __x) __attribute__ ((__nothrow__)); extern long double __atan2l (long double __y, long double __x) __attribute__ ((__nothrow__));
extern long double cosl (long double __x) __attribute__ ((__nothrow__)); extern long double __cosl (long double __x) __attribute__ ((__nothrow__));
extern long double sinl (long double __x) __attribute__ ((__nothrow__)); extern long double __sinl (long double __x) __attribute__ ((__nothrow__));
extern long double tanl (long double __x) __attribute__ ((__nothrow__)); extern long double __tanl (long double __x) __attribute__ ((__nothrow__));
extern long double coshl (long double __x) __attribute__ ((__nothrow__)); extern long double __coshl (long double __x) __attribute__ ((__nothrow__));
extern long double sinhl (long double __x) __attribute__ ((__nothrow__)); extern long double __sinhl (long double __x) __attribute__ ((__nothrow__));
extern long double tanhl (long double __x) __attribute__ ((__nothrow__)); extern long double __tanhl (long double __x) __attribute__ ((__nothrow__));


extern long double acoshl (long double __x) __attribute__ ((__nothrow__)); extern long double __acoshl (long double __x) __attribute__ ((__nothrow__));
extern long double asinhl (long double __x) __attribute__ ((__nothrow__)); extern long double __asinhl (long double __x) __attribute__ ((__nothrow__));
extern long double atanhl (long double __x) __attribute__ ((__nothrow__)); extern long double __atanhl (long double __x) __attribute__ ((__nothrow__));


extern long double expl (long double __x) __attribute__ ((__nothrow__)); extern long double __expl (long double __x) __attribute__ ((__nothrow__));
extern long double frexpl (long double __x, int *__exponent) __attribute__ ((__nothrow__)); extern long double __frexpl (long double __x, int *__exponent) __attribute__ ((__nothrow__));
extern long double ldexpl (long double __x, int __exponent) __attribute__ ((__nothrow__)); extern long double __ldexpl (long double __x, int __exponent) __attribute__ ((__nothrow__));
extern long double logl (long double __x) __attribute__ ((__nothrow__)); extern long double __logl (long double __x) __attribute__ ((__nothrow__));
extern long double log10l (long double __x) __attribute__ ((__nothrow__)); extern long double __log10l (long double __x) __attribute__ ((__nothrow__));
extern long double modfl (long double __x, long double *__iptr) __attribute__ ((__nothrow__)); extern long double __modfl (long double __x, long double *__iptr) __attribute__ ((__nothrow__));


extern long double expm1l (long double __x) __attribute__ ((__nothrow__)); extern long double __expm1l (long double __x) __attribute__ ((__nothrow__));
extern long double log1pl (long double __x) __attribute__ ((__nothrow__)); extern long double __log1pl (long double __x) __attribute__ ((__nothrow__));
extern long double logbl (long double __x) __attribute__ ((__nothrow__)); extern long double __logbl (long double __x) __attribute__ ((__nothrow__));


extern long double exp2l (long double __x) __attribute__ ((__nothrow__)); extern long double __exp2l (long double __x) __attribute__ ((__nothrow__));
extern long double log2l (long double __x) __attribute__ ((__nothrow__)); extern long double __log2l (long double __x) __attribute__ ((__nothrow__));


extern long double powl (long double __x, long double __y) __attribute__ ((__nothrow__)); extern long double __powl (long double __x, long double __y) __attribute__ ((__nothrow__));
extern long double sqrtl (long double __x) __attribute__ ((__nothrow__)); extern long double __sqrtl (long double __x) __attribute__ ((__nothrow__));


extern long double hypotl (long double __x, long double __y) __attribute__ ((__nothrow__)); extern long double __hypotl (long double __x, long double __y) __attribute__ ((__nothrow__));


extern long double cbrtl (long double __x) __attribute__ ((__nothrow__)); extern long double __cbrtl (long double __x) __attribute__ ((__nothrow__));


extern long double ceill (long double __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern long double __ceill (long double __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern long double fabsl (long double __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern long double __fabsl (long double __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern long double floorl (long double __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern long double __floorl (long double __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern long double fmodl (long double __x, long double __y) __attribute__ ((__nothrow__)); extern long double __fmodl (long double __x, long double __y) __attribute__ ((__nothrow__));
extern int __isinfl (long double __value) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern int __finitel (long double __value) __attribute__ ((__nothrow__)) __attribute__ ((__const__));

extern int isinfl (long double __value) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern int finitel (long double __value) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern long double dreml (long double __x, long double __y) __attribute__ ((__nothrow__)); extern long double __dreml (long double __x, long double __y) __attribute__ ((__nothrow__));
extern long double significandl (long double __x) __attribute__ ((__nothrow__)); extern long double __significandl (long double __x) __attribute__ ((__nothrow__));

extern long double copysignl (long double __x, long double __y) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern long double __copysignl (long double __x, long double __y) __attribute__ ((__nothrow__)) __attribute__ ((__const__));


extern long double nanl (__const char *__tagb) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern long double __nanl (__const char *__tagb) __attribute__ ((__nothrow__)) __attribute__ ((__const__));

extern int __isnanl (long double __value) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern int isnanl (long double __value) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern long double j0l (long double) __attribute__ ((__nothrow__)); extern long double __j0l (long double) __attribute__ ((__nothrow__));
extern long double j1l (long double) __attribute__ ((__nothrow__)); extern long double __j1l (long double) __attribute__ ((__nothrow__));
extern long double jnl (int, long double) __attribute__ ((__nothrow__)); extern long double __jnl (int, long double) __attribute__ ((__nothrow__));
extern long double y0l (long double) __attribute__ ((__nothrow__)); extern long double __y0l (long double) __attribute__ ((__nothrow__));
extern long double y1l (long double) __attribute__ ((__nothrow__)); extern long double __y1l (long double) __attribute__ ((__nothrow__));
extern long double ynl (int, long double) __attribute__ ((__nothrow__)); extern long double __ynl (int, long double) __attribute__ ((__nothrow__));

extern long double erfl (long double) __attribute__ ((__nothrow__)); extern long double __erfl (long double) __attribute__ ((__nothrow__));
extern long double erfcl (long double) __attribute__ ((__nothrow__)); extern long double __erfcl (long double) __attribute__ ((__nothrow__));
extern long double lgammal (long double) __attribute__ ((__nothrow__)); extern long double __lgammal (long double) __attribute__ ((__nothrow__));


extern long double tgammal (long double) __attribute__ ((__nothrow__)); extern long double __tgammal (long double) __attribute__ ((__nothrow__));

extern long double gammal (long double) __attribute__ ((__nothrow__)); extern long double __gammal (long double) __attribute__ ((__nothrow__));
extern long double lgammal_r (long double, int *__signgamp) __attribute__ ((__nothrow__)); extern long double __lgammal_r (long double, int *__signgamp) __attribute__ ((__nothrow__));

extern long double rintl (long double __x) __attribute__ ((__nothrow__)); extern long double __rintl (long double __x) __attribute__ ((__nothrow__));
extern long double nextafterl (long double __x, long double __y) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern long double __nextafterl (long double __x, long double __y) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern long double nexttowardl (long double __x, long double __y) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern long double __nexttowardl (long double __x, long double __y) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern long double remainderl (long double __x, long double __y) __attribute__ ((__nothrow__)); extern long double __remainderl (long double __x, long double __y) __attribute__ ((__nothrow__));
extern long double scalbnl (long double __x, int __n) __attribute__ ((__nothrow__)); extern long double __scalbnl (long double __x, int __n) __attribute__ ((__nothrow__));
extern int ilogbl (long double __x) __attribute__ ((__nothrow__)); extern int __ilogbl (long double __x) __attribute__ ((__nothrow__));
extern long double scalblnl (long double __x, long int __n) __attribute__ ((__nothrow__)); extern long double __scalblnl (long double __x, long int __n) __attribute__ ((__nothrow__));
extern long double nearbyintl (long double __x) __attribute__ ((__nothrow__)); extern long double __nearbyintl (long double __x) __attribute__ ((__nothrow__));
extern long double roundl (long double __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern long double __roundl (long double __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern long double truncl (long double __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__)); extern long double __truncl (long double __x) __attribute__ ((__nothrow__)) __attribute__ ((__const__));
extern long double remquol (long double __x, long double __y, int *__quo) __attribute__ ((__nothrow__)); extern long double __remquol (long double __x, long double __y, int *__quo) __attribute__ ((__nothrow__));
extern long int lrintl (long double __x) __attribute__ ((__nothrow__)); extern long int __lrintl (long double __x) __attribute__ ((__nothrow__));
extern long long int llrintl (long double __x) __attribute__ ((__nothrow__)); extern long long int __llrintl (long double __x) __attribute__ ((__nothrow__));
extern long int lroundl (long double __x) __attribute__ ((__nothrow__)); extern long int __lroundl (long double __x) __attribute__ ((__nothrow__));
extern long long int llroundl (long double __x) __attribute__ ((__nothrow__)); extern long long int __llroundl (long double __x) __attribute__ ((__nothrow__));
extern long double fdiml (long double __x, long double __y) __attribute__ ((__nothrow__)); extern long double __fdiml (long double __x, long double __y) __attribute__ ((__nothrow__));
extern long double fmaxl (long double __x, long double __y) __attribute__ ((__nothrow__)); extern long double __fmaxl (long double __x, long double __y) __attribute__ ((__nothrow__));
extern long double fminl (long double __x, long double __y) __attribute__ ((__nothrow__)); extern long double __fminl (long double __x, long double __y) __attribute__ ((__nothrow__));
extern int __fpclassifyl (long double __value) __attribute__ ((__nothrow__))
     __attribute__ ((__const__));
extern long double fmal (long double __x, long double __y, long double __z) __attribute__ ((__nothrow__)); extern long double __fmal (long double __x, long double __y, long double __z) __attribute__ ((__nothrow__));

extern long double scalbl (long double __x, long double __n) __attribute__ ((__nothrow__)); extern long double __scalbl (long double __x, long double __n) __attribute__ ((__nothrow__));
extern int signgam;
enum
  {
    FP_NAN,
    FP_INFINITE,
    FP_ZERO,
    FP_SUBNORMAL,
    FP_NORMAL
  };
typedef enum
{
  _IEEE_ = -1,
  _SVID_,
  _XOPEN_,
  _POSIX_,
  _ISOC_
} _LIB_VERSION_TYPE;
extern _LIB_VERSION_TYPE _LIB_VERSION;
struct exception
  {
    int type;
    char *name;
    double arg1;
    double arg2;
    double retval;
  };
extern int matherr (struct exception *__exc);
extern __inline __attribute__ ((__gnu_inline__)) int
__attribute__ ((__nothrow__)) __signbitf (float __x)
{
  int __m;
  __asm ("pmovmskb %1, %0" : "=r" (__m) : "x" (__x));
  return __m & 0x8;
}
extern __inline __attribute__ ((__gnu_inline__)) int
__attribute__ ((__nothrow__)) __signbit (double __x)
{
  int __m;
  __asm ("pmovmskb %1, %0" : "=r" (__m) : "x" (__x));
  return __m & 0x80;
}
typedef struct
{
  unsigned char _x[4]
    __attribute__((__aligned__(4)));
} omp_lock_t;
typedef struct
{
  unsigned char _x[16]
    __attribute__((__aligned__(8)));
} omp_nest_lock_t;
typedef enum omp_sched_t
{
  omp_sched_static = 1,
  omp_sched_dynamic = 2,
  omp_sched_guided = 3,
  omp_sched_auto = 4
} omp_sched_t;
typedef enum omp_proc_bind_t
{
  omp_proc_bind_false = 0,
  omp_proc_bind_true = 1,
  omp_proc_bind_master = 2,
  omp_proc_bind_close = 3,
  omp_proc_bind_spread = 4
} omp_proc_bind_t;
typedef enum omp_lock_hint_t
{
  omp_lock_hint_none = 0,
  omp_lock_hint_uncontended = 1,
  omp_lock_hint_contended = 2,
  omp_lock_hint_nonspeculative = 4,
  omp_lock_hint_speculative = 8
} omp_lock_hint_t;
extern void omp_set_num_threads (int) __attribute__((__nothrow__));
extern int omp_get_num_threads (void) __attribute__((__nothrow__));
extern int omp_get_max_threads (void) __attribute__((__nothrow__));
extern int omp_get_thread_num (void) __attribute__((__nothrow__));
extern int omp_get_num_procs (void) __attribute__((__nothrow__));
extern int omp_in_parallel (void) __attribute__((__nothrow__));
extern void omp_set_dynamic (int) __attribute__((__nothrow__));
extern int omp_get_dynamic (void) __attribute__((__nothrow__));
extern void omp_set_nested (int) __attribute__((__nothrow__));
extern int omp_get_nested (void) __attribute__((__nothrow__));
extern void omp_init_lock (omp_lock_t *) __attribute__((__nothrow__));
extern void omp_init_lock_with_hint (omp_lock_t *, omp_lock_hint_t)
  __attribute__((__nothrow__));
extern void omp_destroy_lock (omp_lock_t *) __attribute__((__nothrow__));
extern void omp_set_lock (omp_lock_t *) __attribute__((__nothrow__));
extern void omp_unset_lock (omp_lock_t *) __attribute__((__nothrow__));
extern int omp_test_lock (omp_lock_t *) __attribute__((__nothrow__));
extern void omp_init_nest_lock (omp_nest_lock_t *) __attribute__((__nothrow__));
extern void omp_init_nest_lock_with_hint (omp_nest_lock_t *, omp_lock_hint_t)
  __attribute__((__nothrow__));
extern void omp_destroy_nest_lock (omp_nest_lock_t *) __attribute__((__nothrow__));
extern void omp_set_nest_lock (omp_nest_lock_t *) __attribute__((__nothrow__));
extern void omp_unset_nest_lock (omp_nest_lock_t *) __attribute__((__nothrow__));
extern int omp_test_nest_lock (omp_nest_lock_t *) __attribute__((__nothrow__));
extern double omp_get_wtime (void) __attribute__((__nothrow__));
extern double omp_get_wtick (void) __attribute__((__nothrow__));
extern void omp_set_schedule (omp_sched_t, int) __attribute__((__nothrow__));
extern void omp_get_schedule (omp_sched_t *, int *) __attribute__((__nothrow__));
extern int omp_get_thread_limit (void) __attribute__((__nothrow__));
extern void omp_set_max_active_levels (int) __attribute__((__nothrow__));
extern int omp_get_max_active_levels (void) __attribute__((__nothrow__));
extern int omp_get_level (void) __attribute__((__nothrow__));
extern int omp_get_ancestor_thread_num (int) __attribute__((__nothrow__));
extern int omp_get_team_size (int) __attribute__((__nothrow__));
extern int omp_get_active_level (void) __attribute__((__nothrow__));
extern int omp_in_final (void) __attribute__((__nothrow__));
extern int omp_get_cancellation (void) __attribute__((__nothrow__));
extern omp_proc_bind_t omp_get_proc_bind (void) __attribute__((__nothrow__));
extern int omp_get_num_places (void) __attribute__((__nothrow__));
extern int omp_get_place_num_procs (int) __attribute__((__nothrow__));
extern void omp_get_place_proc_ids (int, int *) __attribute__((__nothrow__));
extern int omp_get_place_num (void) __attribute__((__nothrow__));
extern int omp_get_partition_num_places (void) __attribute__((__nothrow__));
extern void omp_get_partition_place_nums (int *) __attribute__((__nothrow__));
extern void omp_set_default_device (int) __attribute__((__nothrow__));
extern int omp_get_default_device (void) __attribute__((__nothrow__));
extern int omp_get_num_devices (void) __attribute__((__nothrow__));
extern int omp_get_num_teams (void) __attribute__((__nothrow__));
extern int omp_get_team_num (void) __attribute__((__nothrow__));
extern int omp_is_initial_device (void) __attribute__((__nothrow__));
extern int omp_get_initial_device (void) __attribute__((__nothrow__));
extern int omp_get_max_task_priority (void) __attribute__((__nothrow__));
extern void *omp_target_alloc (long unsigned int, int) __attribute__((__nothrow__));
extern void omp_target_free (void *, int) __attribute__((__nothrow__));
extern int omp_target_is_present (void *, int) __attribute__((__nothrow__));
extern int omp_target_memcpy (void *, void *, long unsigned int, long unsigned int,
         long unsigned int, int, int) __attribute__((__nothrow__));
extern int omp_target_memcpy_rect (void *, void *, long unsigned int, int,
       const long unsigned int *,
       const long unsigned int *,
       const long unsigned int *,
       const long unsigned int *,
       const long unsigned int *, int, int)
  __attribute__((__nothrow__));
extern int omp_target_associate_ptr (void *, void *, long unsigned int,
         long unsigned int, int) __attribute__((__nothrow__));
extern int omp_target_disassociate_ptr (void *, int) __attribute__((__nothrow__));
typedef int boolean;
typedef struct { double real; double imag; } dcomplex;
extern double randlc(double *, double);
extern void vranlc(int, double *, double, double *);
extern void timer_clear(int);
extern void timer_start(int);
extern void timer_stop(int);
extern double timer_read(int);
extern void c_print_results(char *name, char class, int n1, int n2,
       int n3, int niter, int nthreads, double t,
       double mops, char *optype, int passed_verification,
       char *npbversion, char *compiletime, char *cc,
       char *clink, char *c_lib, char *c_inc,
       char *cflags, char *clinkflags, char *rand);
static int nx, ny, nz;
static int nx0, ny0, nz0;
static int ist, iend;
static int jst, jend;
static int ii1, ii2;
static int ji1, ji2;
static int ki1, ki2;
static double dxi, deta, dzeta;
static double tx1, tx2, tx3;
static double ty1, ty2, ty3;
static double tz1, tz2, tz3;
static double dx1, dx2, dx3, dx4, dx5;
static double dy1, dy2, dy3, dy4, dy5;
static double dz1, dz2, dz3, dz4, dz5;
static double dssp;
static double u[162][162/2*2+1][162/2*2+1][5];
static double rsd[162][162/2*2+1][162/2*2+1][5];
static double frct[162][162/2*2+1][162/2*2+1][5];
static double flux[162][162/2*2+1][162/2*2+1][5];
static int ipr, inorm;
static int itmax, invert;
static double dt, omega, tolrsd[5], rsdnm[5], errnm[5], frc, ttotal;
static double a[162][162][5][5];
static double b[162][162][5][5];
static double c[162][162][5][5];
static double d[162][162][5][5];
static double ce[5][13];
static double maxtime;
static boolean flag[162/2*2+1];
static void blts (int nx, int ny, int nz, int k,
    double omega,
    double v[162][162/2*2+1][162/2*2+1][5],
    double ldz[162][162][5][5],
    double ldy[162][162][5][5],
    double ldx[162][162][5][5],
    double d[162][162][5][5],
    int ist, int iend, int jst, int jend,
    int nx0, int ny0 );
static void buts(int nx, int ny, int nz, int k,
   double omega,
   double v[162][162/2*2+1][162/2*2+1][5],
   double tv[162][162][5],
   double d[162][162][5][5],
   double udx[162][162][5][5],
   double udy[162][162][5][5],
   double udz[162][162][5][5],
   int ist, int iend, int jst, int jend,
   int nx0, int ny0 );
static void domain(void);
static void erhs(void);
static void error(void);
static void exact( int i, int j, int k, double u000ijk[5] );
static void jacld(int k);
static void jacu(int k);
static void l2norm (int nx0, int ny0, int nz0,
      int ist, int iend,
      int jst, int jend,
      double v[162][162/2*2+1][162/2*2+1][5],
      double sum[5]);
static void pintgr(void);
static void read_input(void);
static void rhs(void);
static void setbv(void);
static void setcoeff(void);
static void setiv(void);
static void ssor(void);
static void verify(double xcr[5], double xce[5], double xci,
     char *class, boolean *verified);
int main(int argc, char **argv) {
  char class;
  boolean verified;
  double mflops;
  int nthreads = 1;
  read_input();
  domain();
  setcoeff();
  setbv();
  setiv();
  erhs();
        
#pragma omp parallel
{
        
#pragma omp master
  nthreads = omp_get_num_threads();
}
  ssor();
  error();
  pintgr();
  verify ( rsdnm, errnm, frc, &class, &verified );
  mflops = (double)itmax*(1984.77*(double)nx0
    *(double)ny0
    *(double)nz0
    -10923.3*(((double)( nx0+ny0+nz0 )/3.0)*((double)( nx0+ny0+nz0 )/3.0))
    +27770.9* (double)( nx0+ny0+nz0 )/3.0
    -144010.0)
    / (maxtime*1000000.0);
  c_print_results("LU", class, nx0,
    ny0, nz0, itmax, nthreads,
    maxtime, mflops, "          floating point", verified,
    "3.0 structured", "30 Nov 2019", "gcc", "gcc", "-lm", "-I../common", "-O3 -fopenmp", "-O3 -fopenmp -mcmodel=large -fPIC",
    "(none)");
}
static void blts (int nx, int ny, int nz, int k,
    double omega,
    double v[162][162/2*2+1][162/2*2+1][5],
    double ldz[162][162][5][5],
    double ldy[162][162][5][5],
    double ldx[162][162][5][5],
    double d[162][162][5][5],
    int ist, int iend, int jst, int jend,
    int nx0, int ny0 ) {
  int i, j, m;
  double tmp, tmp1;
  double tmat[5][5];
        
#pragma omp for nowait schedule(static)
  for (i = ist; i <= iend; i++) {
    for (j = jst; j <= jend; j++) {
      for (m = 0; m < 5; m++) {
 v[i][j][k][m] = v[i][j][k][m]
   - omega * ( ldz[i][j][m][0] * v[i][j][k-1][0]
         + ldz[i][j][m][1] * v[i][j][k-1][1]
         + ldz[i][j][m][2] * v[i][j][k-1][2]
         + ldz[i][j][m][3] * v[i][j][k-1][3]
         + ldz[i][j][m][4] * v[i][j][k-1][4] );
      }
    }
  }
        
#pragma omp for nowait schedule(static)
  for (i = ist; i <= iend; i++) {
    if (i != ist) {
 while (flag[i-1] == 0) {
        
#pragma omp flush(flag)
     ;
 }
    }
    if (i != iend) {
 while (flag[i] == 1) {
        
#pragma omp flush(flag)
     ;
 }
    }
    for (j = jst; j <= jend; j++) {
      for (m = 0; m < 5; m++) {
 v[i][j][k][m] = v[i][j][k][m]
   - omega * ( ldy[i][j][m][0] * v[i][j-1][k][0]
        + ldx[i][j][m][0] * v[i-1][j][k][0]
        + ldy[i][j][m][1] * v[i][j-1][k][1]
        + ldx[i][j][m][1] * v[i-1][j][k][1]
        + ldy[i][j][m][2] * v[i][j-1][k][2]
        + ldx[i][j][m][2] * v[i-1][j][k][2]
        + ldy[i][j][m][3] * v[i][j-1][k][3]
        + ldx[i][j][m][3] * v[i-1][j][k][3]
        + ldy[i][j][m][4] * v[i][j-1][k][4]
        + ldx[i][j][m][4] * v[i-1][j][k][4] );
      }
      for (m = 0; m < 5; m++) {
 tmat[m][0] = d[i][j][m][0];
 tmat[m][1] = d[i][j][m][1];
 tmat[m][2] = d[i][j][m][2];
 tmat[m][3] = d[i][j][m][3];
 tmat[m][4] = d[i][j][m][4];
      }
      tmp1 = 1.0 / tmat[0][0];
      tmp = tmp1 * tmat[1][0];
      tmat[1][1] = tmat[1][1]
 - tmp * tmat[0][1];
      tmat[1][2] = tmat[1][2]
 - tmp * tmat[0][2];
      tmat[1][3] = tmat[1][3]
 - tmp * tmat[0][3];
      tmat[1][4] = tmat[1][4]
 - tmp * tmat[0][4];
      v[i][j][k][1] = v[i][j][k][1]
 - v[i][j][k][0] * tmp;
      tmp = tmp1 * tmat[2][0];
      tmat[2][1] = tmat[2][1]
 - tmp * tmat[0][1];
      tmat[2][2] = tmat[2][2]
 - tmp * tmat[0][2];
      tmat[2][3] = tmat[2][3]
 - tmp * tmat[0][3];
      tmat[2][4] = tmat[2][4]
 - tmp * tmat[0][4];
      v[i][j][k][2] = v[i][j][k][2]
 - v[i][j][k][0] * tmp;
      tmp = tmp1 * tmat[3][0];
      tmat[3][1] = tmat[3][1]
 - tmp * tmat[0][1];
      tmat[3][2] = tmat[3][2]
 - tmp * tmat[0][2];
      tmat[3][3] = tmat[3][3]
 - tmp * tmat[0][3];
      tmat[3][4] = tmat[3][4]
 - tmp * tmat[0][4];
      v[i][j][k][3] = v[i][j][k][3]
 - v[i][j][k][0] * tmp;
      tmp = tmp1 * tmat[4][0];
      tmat[4][1] = tmat[4][1]
 - tmp * tmat[0][1];
      tmat[4][2] = tmat[4][2]
 - tmp * tmat[0][2];
      tmat[4][3] = tmat[4][3]
 - tmp * tmat[0][3];
      tmat[4][4] = tmat[4][4]
 - tmp * tmat[0][4];
      v[i][j][k][4] = v[i][j][k][4]
 - v[i][j][k][0] * tmp;
      tmp1 = 1.0 / tmat[ 1][1];
      tmp = tmp1 * tmat[ 2][1];
      tmat[2][2] = tmat[2][2]
 - tmp * tmat[1][2];
      tmat[2][3] = tmat[2][3]
 - tmp * tmat[1][3];
      tmat[2][4] = tmat[2][4]
 - tmp * tmat[1][4];
      v[i][j][k][2] = v[i][j][k][2]
 - v[i][j][k][1] * tmp;
      tmp = tmp1 * tmat[3][1];
      tmat[3][2] = tmat[3][2]
 - tmp * tmat[1][2];
      tmat[3][3] = tmat[3][3]
 - tmp * tmat[1][3];
      tmat[3][4] = tmat[3][4]
 - tmp * tmat[1][4];
      v[i][j][k][3] = v[i][j][k][3]
 - v[i][j][k][1] * tmp;
      tmp = tmp1 * tmat[4][1];
      tmat[4][2] = tmat[4][2]
 - tmp * tmat[1][2];
      tmat[4][3] = tmat[4][3]
 - tmp * tmat[1][3];
      tmat[4][4] = tmat[4][4]
 - tmp * tmat[1][4];
      v[i][j][k][4] = v[i][j][k][4]
 - v[i][j][k][1] * tmp;
      tmp1 = 1.0 / tmat[2][2];
      tmp = tmp1 * tmat[3][2];
      tmat[3][3] = tmat[3][3]
 - tmp * tmat[2][3];
      tmat[3][4] = tmat[3][4]
 - tmp * tmat[2][4];
      v[i][j][k][3] = v[i][j][k][3]
        - v[i][j][k][2] * tmp;
      tmp = tmp1 * tmat[4][2];
      tmat[4][3] = tmat[4][3]
 - tmp * tmat[2][3];
      tmat[4][4] = tmat[4][4]
 - tmp * tmat[2][4];
      v[i][j][k][4] = v[i][j][k][4]
 - v[i][j][k][2] * tmp;
      tmp1 = 1.0 / tmat[3][3];
      tmp = tmp1 * tmat[4][3];
      tmat[4][4] = tmat[4][4]
 - tmp * tmat[3][4];
      v[i][j][k][4] = v[i][j][k][4]
 - v[i][j][k][3] * tmp;
      v[i][j][k][4] = v[i][j][k][4]
 / tmat[4][4];
      v[i][j][k][3] = v[i][j][k][3]
 - tmat[3][4] * v[i][j][k][4];
      v[i][j][k][3] = v[i][j][k][3]
 / tmat[3][3];
      v[i][j][k][2] = v[i][j][k][2]
 - tmat[2][3] * v[i][j][k][3]
 - tmat[2][4] * v[i][j][k][4];
      v[i][j][k][2] = v[i][j][k][2]
 / tmat[2][2];
      v[i][j][k][1] = v[i][j][k][1]
 - tmat[1][2] * v[i][j][k][2]
 - tmat[1][3] * v[i][j][k][3]
 - tmat[1][4] * v[i][j][k][4];
      v[i][j][k][1] = v[i][j][k][1]
 / tmat[1][1];
      v[i][j][k][0] = v[i][j][k][0]
 - tmat[0][1] * v[i][j][k][1]
 - tmat[0][2] * v[i][j][k][2]
 - tmat[0][3] * v[i][j][k][3]
 - tmat[0][4] * v[i][j][k][4];
      v[i][j][k][0] = v[i][j][k][0]
 / tmat[0][0];
    }
    if (i != ist) flag[i-1] = 0;
    if (i != iend) flag[i] = 1;
        
#pragma omp flush(flag)
  }
}
static void buts(int nx, int ny, int nz, int k,
   double omega,
   double v[162][162/2*2+1][162/2*2+1][5],
   double tv[162][162][5],
   double d[162][162][5][5],
   double udx[162][162][5][5],
   double udy[162][162][5][5],
   double udz[162][162][5][5],
   int ist, int iend, int jst, int jend,
   int nx0, int ny0 ) {
  int i, j, m;
  double tmp, tmp1;
  double tmat[5][5];
        
#pragma omp for nowait schedule(static)
  for (i = iend; i >= ist; i--) {
    for (j = jend; j >= jst; j--) {
      for (m = 0; m < 5; m++) {
 tv[i][j][m] =
   omega * ( udz[i][j][m][0] * v[i][j][k+1][0]
       + udz[i][j][m][1] * v[i][j][k+1][1]
       + udz[i][j][m][2] * v[i][j][k+1][2]
       + udz[i][j][m][3] * v[i][j][k+1][3]
       + udz[i][j][m][4] * v[i][j][k+1][4] );
      }
    }
  }
        
#pragma omp for nowait schedule(static)
  for (i = iend; i >= ist; i--) {
    if (i != iend) {
      while (flag[i+1] == 0) {
        
#pragma omp flush(flag)
 ;
      }
    }
    if (i != ist) {
      while (flag[i] == 1) {
        
#pragma omp flush(flag)
 ;
      }
    }
    for (j = jend; j >= jst; j--) {
      for (m = 0; m < 5; m++) {
 tv[i][j][m] = tv[i][j][m]
   + omega * ( udy[i][j][m][0] * v[i][j+1][k][0]
        + udx[i][j][m][0] * v[i+1][j][k][0]
        + udy[i][j][m][1] * v[i][j+1][k][1]
        + udx[i][j][m][1] * v[i+1][j][k][1]
        + udy[i][j][m][2] * v[i][j+1][k][2]
        + udx[i][j][m][2] * v[i+1][j][k][2]
        + udy[i][j][m][3] * v[i][j+1][k][3]
        + udx[i][j][m][3] * v[i+1][j][k][3]
        + udy[i][j][m][4] * v[i][j+1][k][4]
        + udx[i][j][m][4] * v[i+1][j][k][4] );
      }
      for (m = 0; m < 5; m++) {
 tmat[m][0] = d[i][j][m][0];
 tmat[m][1] = d[i][j][m][1];
 tmat[m][2] = d[i][j][m][2];
 tmat[m][3] = d[i][j][m][3];
 tmat[m][4] = d[i][j][m][4];
      }
      tmp1 = 1.0 / tmat[0][0];
      tmp = tmp1 * tmat[1][0];
      tmat[1][1] = tmat[1][1]
 - tmp * tmat[0][1];
      tmat[1][2] = tmat[1][2]
 - tmp * tmat[0][2];
      tmat[1][3] = tmat[1][3]
 - tmp * tmat[0][3];
      tmat[1][4] = tmat[1][4]
 - tmp * tmat[0][4];
      tv[i][j][1] = tv[i][j][1]
 - tv[i][j][0] * tmp;
      tmp = tmp1 * tmat[2][0];
      tmat[2][1] = tmat[2][1]
 - tmp * tmat[0][1];
      tmat[2][2] = tmat[2][2]
 - tmp * tmat[0][2];
      tmat[2][3] = tmat[2][3]
 - tmp * tmat[0][3];
      tmat[2][4] = tmat[2][4]
 - tmp * tmat[0][4];
      tv[i][j][2] = tv[i][j][2]
 - tv[i][j][0] * tmp;
      tmp = tmp1 * tmat[3][0];
      tmat[3][1] = tmat[3][1]
 - tmp * tmat[0][1];
      tmat[3][2] = tmat[3][2]
 - tmp * tmat[0][2];
      tmat[3][3] = tmat[3][3]
 - tmp * tmat[0][3];
      tmat[3][4] = tmat[3][4]
 - tmp * tmat[0][4];
      tv[i][j][3] = tv[i][j][3]
 - tv[i][j][0] * tmp;
      tmp = tmp1 * tmat[4][0];
      tmat[4][1] = tmat[4][1]
 - tmp * tmat[0][1];
      tmat[4][2] = tmat[4][2]
 - tmp * tmat[0][2];
      tmat[4][3] = tmat[4][3]
 - tmp * tmat[0][3];
      tmat[4][4] = tmat[4][4]
 - tmp * tmat[0][4];
      tv[i][j][4] = tv[i][j][4]
 - tv[i][j][0] * tmp;
      tmp1 = 1.0 / tmat[1][1];
      tmp = tmp1 * tmat[2][1];
      tmat[2][2] = tmat[2][2]
 - tmp * tmat[1][2];
      tmat[2][3] = tmat[2][3]
 - tmp * tmat[1][3];
      tmat[2][4] = tmat[2][4]
 - tmp * tmat[1][4];
      tv[i][j][2] = tv[i][j][2]
 - tv[i][j][1] * tmp;
      tmp = tmp1 * tmat[3][1];
      tmat[3][2] = tmat[3][2]
 - tmp * tmat[1][2];
      tmat[3][3] = tmat[3][3]
 - tmp * tmat[1][3];
      tmat[3][4] = tmat[3][4]
 - tmp * tmat[1][4];
      tv[i][j][3] = tv[i][j][3]
 - tv[i][j][1] * tmp;
      tmp = tmp1 * tmat[4][1];
      tmat[4][2] = tmat[4][2]
 - tmp * tmat[1][2];
      tmat[4][3] = tmat[4][3]
 - tmp * tmat[1][3];
      tmat[4][4] = tmat[4][4]
 - tmp * tmat[1][4];
      tv[i][j][4] = tv[i][j][4]
 - tv[i][j][1] * tmp;
      tmp1 = 1.0 / tmat[2][2];
      tmp = tmp1 * tmat[3][2];
      tmat[3][3] = tmat[3][3]
 - tmp * tmat[2][3];
      tmat[3][4] = tmat[3][4]
 - tmp * tmat[2][4];
      tv[i][j][3] = tv[i][j][3]
 - tv[i][j][2] * tmp;
      tmp = tmp1 * tmat[4][2];
      tmat[4][3] = tmat[4][3]
 - tmp * tmat[2][3];
      tmat[4][4] = tmat[4][4]
 - tmp * tmat[2][4];
      tv[i][j][4] = tv[i][j][4]
 - tv[i][j][2] * tmp;
      tmp1 = 1.0 / tmat[3][3];
      tmp = tmp1 * tmat[4][3];
      tmat[4][4] = tmat[4][4]
 - tmp * tmat[3][4];
      tv[i][j][4] = tv[i][j][4]
 - tv[i][j][3] * tmp;
      tv[i][j][4] = tv[i][j][4]
 / tmat[4][4];
      tv[i][j][3] = tv[i][j][3]
 - tmat[3][4] * tv[i][j][4];
      tv[i][j][3] = tv[i][j][3]
 / tmat[3][3];
      tv[i][j][2] = tv[i][j][2]
 - tmat[2][3] * tv[i][j][3]
 - tmat[2][4] * tv[i][j][4];
      tv[i][j][2] = tv[i][j][2]
 / tmat[2][2];
      tv[i][j][1] = tv[i][j][1]
 - tmat[1][2] * tv[i][j][2]
 - tmat[1][3] * tv[i][j][3]
 - tmat[1][4] * tv[i][j][4];
      tv[i][j][1] = tv[i][j][1]
 / tmat[1][1];
      tv[i][j][0] = tv[i][j][0]
 - tmat[0][1] * tv[i][j][1]
 - tmat[0][2] * tv[i][j][2]
 - tmat[0][3] * tv[i][j][3]
 - tmat[0][4] * tv[i][j][4];
      tv[i][j][0] = tv[i][j][0]
 / tmat[0][0];
      v[i][j][k][0] = v[i][j][k][0] - tv[i][j][0];
      v[i][j][k][1] = v[i][j][k][1] - tv[i][j][1];
      v[i][j][k][2] = v[i][j][k][2] - tv[i][j][2];
      v[i][j][k][3] = v[i][j][k][3] - tv[i][j][3];
      v[i][j][k][4] = v[i][j][k][4] - tv[i][j][4];
    }
    if (i != iend) flag[i+1] = 0;
    if (i != ist) flag[i] = 1;
        
#pragma omp flush(flag)
  }
}
static void domain(void) {
  nx = nx0;
  ny = ny0;
  nz = nz0;
  if ( nx < 4 || ny < 4 || nz < 4 ) {
    printf("     SUBDOMAIN SIZE IS TOO SMALL - \n"
    "     ADJUST PROBLEM SIZE OR NUMBER OF PROCESSORS\n"
    "     SO THAT NX, NY AND NZ ARE GREATER THAN OR EQUAL\n"
    "     TO 4 THEY ARE CURRENTLY%3d%3d%3d\n", nx, ny, nz);
    exit(1);
  }
  if ( nx > 162 || ny > 162 || nz > 162 ) {
    printf("     SUBDOMAIN SIZE IS TOO LARGE - \n"
    "     ADJUST PROBLEM SIZE OR NUMBER OF PROCESSORS\n"
    "     SO THAT NX, NY AND NZ ARE LESS THAN OR EQUAL TO \n"
    "     ISIZ1, ISIZ2 AND ISIZ3 RESPECTIVELY.  THEY ARE\n"
    "     CURRENTLY%4d%4d%4d\n", nx, ny, nz);
    exit(1);
  }
  ist = 1;
  iend = nx - 2;
  jst = 1;
  jend = ny - 2;
}
static void erhs(void) {
        
#pragma omp parallel
{
  int i, j, k, m;
  int iglob, jglob;
  int L1, L2;
  int ist1, iend1;
  int jst1, jend1;
  double dsspm;
  double xi, eta, zeta;
  double q;
  double u21, u31, u41;
  double tmp;
  double u21i, u31i, u41i, u51i;
  double u21j, u31j, u41j, u51j;
  double u21k, u31k, u41k, u51k;
  double u21im1, u31im1, u41im1, u51im1;
  double u21jm1, u31jm1, u41jm1, u51jm1;
  double u21km1, u31km1, u41km1, u51km1;
  dsspm = dssp;
        
#pragma omp for
  for (i = 0; i < nx; i++) {
    for (j = 0; j < ny; j++) {
      for (k = 0; k < nz; k++) {
 for (m = 0; m < 5; m++) {
   frct[i][j][k][m] = 0.0;
 }
      }
    }
  }
        
#pragma omp for
  for (i = 0; i < nx; i++) {
    iglob = i;
    xi = ( (double)(iglob) ) / ( nx0 - 1 );
    for (j = 0; j < ny; j++) {
      jglob = j;
      eta = ( (double)(jglob) ) / ( ny0 - 1 );
      for (k = 0; k < nz; k++) {
 zeta = ( (double)(k) ) / ( nz - 1 );
 for (m = 0; m < 5; m++) {
   rsd[i][j][k][m] = ce[m][0]
     + ce[m][1] * xi
     + ce[m][2] * eta
     + ce[m][3] * zeta
     + ce[m][4] * xi * xi
     + ce[m][5] * eta * eta
     + ce[m][6] * zeta * zeta
     + ce[m][7] * xi * xi * xi
     + ce[m][8] * eta * eta * eta
     + ce[m][9] * zeta * zeta * zeta
     + ce[m][10] * xi * xi * xi * xi
     + ce[m][11] * eta * eta * eta * eta
     + ce[m][12] * zeta * zeta * zeta * zeta;
 }
      }
    }
  }
  L1 = 0;
  L2 = nx-1;
        
#pragma omp for
  for (i = L1; i <= L2; i++) {
    for (j = jst; j <= jend; j++) {
      for (k = 1; k < nz - 1; k++) {
 flux[i][j][k][0] = rsd[i][j][k][1];
 u21 = rsd[i][j][k][1] / rsd[i][j][k][0];
 q = 0.50 * ( rsd[i][j][k][1] * rsd[i][j][k][1]
        + rsd[i][j][k][2] * rsd[i][j][k][2]
        + rsd[i][j][k][3] * rsd[i][j][k][3] )
   / rsd[i][j][k][0];
 flux[i][j][k][1] = rsd[i][j][k][1] * u21 + 0.40e+00 *
   ( rsd[i][j][k][4] - q );
 flux[i][j][k][2] = rsd[i][j][k][2] * u21;
 flux[i][j][k][3] = rsd[i][j][k][3] * u21;
 flux[i][j][k][4] = ( 1.40e+00 * rsd[i][j][k][4] - 0.40e+00 * q ) * u21;
      }
    }
  }
        
#pragma omp for
  for (j = jst; j <= jend; j++) {
    for (k = 1; k <= nz - 2; k++) {
      for (i = ist; i <= iend; i++) {
 for (m = 0; m < 5; m++) {
   frct[i][j][k][m] = frct[i][j][k][m]
     - tx2 * ( flux[i+1][j][k][m] - flux[i-1][j][k][m] );
 }
      }
      for (i = ist; i <= L2; i++) {
 tmp = 1.0 / rsd[i][j][k][0];
 u21i = tmp * rsd[i][j][k][1];
 u31i = tmp * rsd[i][j][k][2];
 u41i = tmp * rsd[i][j][k][3];
 u51i = tmp * rsd[i][j][k][4];
 tmp = 1.0 / rsd[i-1][j][k][0];
 u21im1 = tmp * rsd[i-1][j][k][1];
 u31im1 = tmp * rsd[i-1][j][k][2];
 u41im1 = tmp * rsd[i-1][j][k][3];
 u51im1 = tmp * rsd[i-1][j][k][4];
 flux[i][j][k][1] = (4.0/3.0) * tx3 *
   ( u21i - u21im1 );
 flux[i][j][k][2] = tx3 * ( u31i - u31im1 );
 flux[i][j][k][3] = tx3 * ( u41i - u41im1 );
 flux[i][j][k][4] = 0.50 * ( 1.0 - 1.40e+00*1.40e+00 )
   * tx3 * ( ( u21i * u21i + u31i * u31i + u41i * u41i )
      - ( u21im1*u21im1 + u31im1*u31im1 + u41im1*u41im1 ) )
   + (1.0/6.0)
   * tx3 * ( u21i*u21i - u21im1*u21im1 )
   + 1.40e+00 * 1.40e+00 * tx3 * ( u51i - u51im1 );
      }
      for (i = ist; i <= iend; i++) {
 frct[i][j][k][0] = frct[i][j][k][0]
   + dx1 * tx1 * ( rsd[i-1][j][k][0]
         - 2.0 * rsd[i][j][k][0]
         + rsd[i+1][j][k][0] );
 frct[i][j][k][1] = frct[i][j][k][1]
   + tx3 * 1.00e-01 * 1.00e+00 * ( flux[i+1][j][k][1] - flux[i][j][k][1] )
   + dx2 * tx1 * ( rsd[i-1][j][k][1]
         - 2.0 * rsd[i][j][k][1]
         + rsd[i+1][j][k][1] );
 frct[i][j][k][2] = frct[i][j][k][2]
   + tx3 * 1.00e-01 * 1.00e+00 * ( flux[i+1][j][k][2] - flux[i][j][k][2] )
   + dx3 * tx1 * ( rsd[i-1][j][k][2]
         - 2.0 * rsd[i][j][k][2]
         + rsd[i+1][j][k][2] );
 frct[i][j][k][3] = frct[i][j][k][3]
   + tx3 * 1.00e-01 * 1.00e+00 * ( flux[i+1][j][k][3] - flux[i][j][k][3] )
   + dx4 * tx1 * ( rsd[i-1][j][k][3]
         - 2.0 * rsd[i][j][k][3]
         + rsd[i+1][j][k][3] );
 frct[i][j][k][4] = frct[i][j][k][4]
   + tx3 * 1.00e-01 * 1.00e+00 * ( flux[i+1][j][k][4] - flux[i][j][k][4] )
   + dx5 * tx1 * ( rsd[i-1][j][k][4]
         - 2.0 * rsd[i][j][k][4]
         + rsd[i+1][j][k][4] );
      }
      for (m = 0; m < 5; m++) {
 frct[1][j][k][m] = frct[1][j][k][m]
   - dsspm * ( + 5.0 * rsd[1][j][k][m]
        - 4.0 * rsd[2][j][k][m]
        + rsd[3][j][k][m] );
 frct[2][j][k][m] = frct[2][j][k][m]
   - dsspm * ( - 4.0 * rsd[1][j][k][m]
        + 6.0 * rsd[2][j][k][m]
        - 4.0 * rsd[3][j][k][m]
        + rsd[4][j][k][m] );
      }
      ist1 = 3;
      iend1 = nx - 4;
      for (i = ist1; i <=iend1; i++) {
 for (m = 0; m < 5; m++) {
   frct[i][j][k][m] = frct[i][j][k][m]
     - dsspm * ( rsd[i-2][j][k][m]
       - 4.0 * rsd[i-1][j][k][m]
       + 6.0 * rsd[i][j][k][m]
       - 4.0 * rsd[i+1][j][k][m]
       + rsd[i+2][j][k][m] );
 }
      }
      for (m = 0; m < 5; m++) {
 frct[nx-3][j][k][m] = frct[nx-3][j][k][m]
   - dsspm * ( rsd[nx-5][j][k][m]
      - 4.0 * rsd[nx-4][j][k][m]
      + 6.0 * rsd[nx-3][j][k][m]
      - 4.0 * rsd[nx-2][j][k][m] );
 frct[nx-2][j][k][m] = frct[nx-2][j][k][m]
   - dsspm * ( rsd[nx-4][j][k][m]
      - 4.0 * rsd[nx-3][j][k][m]
      + 5.0 * rsd[nx-2][j][k][m] );
      }
    }
  }
  L1 = 0;
  L2 = ny-1;
        
#pragma omp for
  for (i = ist; i <= iend; i++) {
    for (j = L1; j <= L2; j++) {
      for (k = 1; k <= nz - 2; k++) {
 flux[i][j][k][0] = rsd[i][j][k][2];
 u31 = rsd[i][j][k][2] / rsd[i][j][k][0];
 q = 0.50 * ( rsd[i][j][k][1] * rsd[i][j][k][1]
        + rsd[i][j][k][2] * rsd[i][j][k][2]
        + rsd[i][j][k][3] * rsd[i][j][k][3] )
   / rsd[i][j][k][0];
 flux[i][j][k][1] = rsd[i][j][k][1] * u31;
 flux[i][j][k][2] = rsd[i][j][k][2] * u31 + 0.40e+00 *
   ( rsd[i][j][k][4] - q );
 flux[i][j][k][3] = rsd[i][j][k][3] * u31;
 flux[i][j][k][4] = ( 1.40e+00 * rsd[i][j][k][4] - 0.40e+00 * q ) * u31;
      }
    }
  }
        
#pragma omp for
  for (i = ist; i <= iend; i++) {
    for (k = 1; k <= nz - 2; k++) {
      for (j = jst; j <= jend; j++) {
 for (m = 0; m < 5; m++) {
   frct[i][j][k][m] = frct[i][j][k][m]
     - ty2 * ( flux[i][j+1][k][m] - flux[i][j-1][k][m] );
 }
      }
      for (j = jst; j <= L2; j++) {
 tmp = 1.0 / rsd[i][j][k][0];
 u21j = tmp * rsd[i][j][k][1];
 u31j = tmp * rsd[i][j][k][2];
 u41j = tmp * rsd[i][j][k][3];
 u51j = tmp * rsd[i][j][k][4];
 tmp = 1.0 / rsd[i][j-1][k][0];
 u21jm1 = tmp * rsd[i][j-1][k][1];
 u31jm1 = tmp * rsd[i][j-1][k][2];
 u41jm1 = tmp * rsd[i][j-1][k][3];
 u51jm1 = tmp * rsd[i][j-1][k][4];
 flux[i][j][k][1] = ty3 * ( u21j - u21jm1 );
 flux[i][j][k][2] = (4.0/3.0) * ty3 *
   ( u31j - u31jm1 );
 flux[i][j][k][3] = ty3 * ( u41j - u41jm1 );
 flux[i][j][k][4] = 0.50 * ( 1.0 - 1.40e+00*1.40e+00 )
   * ty3 * ( ( u21j *u21j + u31j *u31j + u41j *u41j )
      - ( u21jm1*u21jm1 + u31jm1*u31jm1 + u41jm1*u41jm1 ) )
   + (1.0/6.0)
   * ty3 * ( u31j*u31j - u31jm1*u31jm1 )
   + 1.40e+00 * 1.40e+00 * ty3 * ( u51j - u51jm1 );
      }
      for (j = jst; j <= jend; j++) {
 frct[i][j][k][0] = frct[i][j][k][0]
   + dy1 * ty1 * ( rsd[i][j-1][k][0]
         - 2.0 * rsd[i][j][k][0]
         + rsd[i][j+1][k][0] );
 frct[i][j][k][1] = frct[i][j][k][1]
   + ty3 * 1.00e-01 * 1.00e+00 * ( flux[i][j+1][k][1] - flux[i][j][k][1] )
   + dy2 * ty1 * ( rsd[i][j-1][k][1]
         - 2.0 * rsd[i][j][k][1]
         + rsd[i][j+1][k][1] );
 frct[i][j][k][2] = frct[i][j][k][2]
   + ty3 * 1.00e-01 * 1.00e+00 * ( flux[i][j+1][k][2] - flux[i][j][k][2] )
   + dy3 * ty1 * ( rsd[i][j-1][k][2]
         - 2.0 * rsd[i][j][k][2]
         + rsd[i][j+1][k][2] );
 frct[i][j][k][3] = frct[i][j][k][3]
   + ty3 * 1.00e-01 * 1.00e+00 * ( flux[i][j+1][k][3] - flux[i][j][k][3] )
   + dy4 * ty1 * ( rsd[i][j-1][k][3]
         - 2.0 * rsd[i][j][k][3]
         + rsd[i][j+1][k][3] );
 frct[i][j][k][4] = frct[i][j][k][4]
   + ty3 * 1.00e-01 * 1.00e+00 * ( flux[i][j+1][k][4] - flux[i][j][k][4] )
   + dy5 * ty1 * ( rsd[i][j-1][k][4]
         - 2.0 * rsd[i][j][k][4]
         + rsd[i][j+1][k][4] );
      }
      for (m = 0; m < 5; m++) {
 frct[i][1][k][m] = frct[i][1][k][m]
   - dsspm * ( + 5.0 * rsd[i][1][k][m]
        - 4.0 * rsd[i][2][k][m]
        + rsd[i][3][k][m] );
 frct[i][2][k][m] = frct[i][2][k][m]
   - dsspm * ( - 4.0 * rsd[i][1][k][m]
        + 6.0 * rsd[i][2][k][m]
        - 4.0 * rsd[i][3][k][m]
        + rsd[i][4][k][m] );
      }
      jst1 = 3;
      jend1 = ny - 4;
      for (j = jst1; j <= jend1; j++) {
 for (m = 0; m < 5; m++) {
   frct[i][j][k][m] = frct[i][j][k][m]
     - dsspm * ( rsd[i][j-2][k][m]
       - 4.0 * rsd[i][j-1][k][m]
       + 6.0 * rsd[i][j][k][m]
       - 4.0 * rsd[i][j+1][k][m]
       + rsd[i][j+2][k][m] );
 }
      }
      for (m = 0; m < 5; m++) {
 frct[i][ny-3][k][m] = frct[i][ny-3][k][m]
   - dsspm * ( rsd[i][ny-5][k][m]
      - 4.0 * rsd[i][ny-4][k][m]
      + 6.0 * rsd[i][ny-3][k][m]
      - 4.0 * rsd[i][ny-2][k][m] );
 frct[i][ny-2][k][m] = frct[i][ny-2][k][m]
   - dsspm * ( rsd[i][ny-4][k][m]
      - 4.0 * rsd[i][ny-3][k][m]
      + 5.0 * rsd[i][ny-2][k][m] );
      }
    }
  }
        
#pragma omp for
  for (i = ist; i <= iend; i++) {
    for (j = jst; j <= jend; j++) {
      for (k = 0; k <= nz-1; k++) {
 flux[i][j][k][0] = rsd[i][j][k][3];
 u41 = rsd[i][j][k][3] / rsd[i][j][k][0];
 q = 0.50 * ( rsd[i][j][k][1] * rsd[i][j][k][1]
        + rsd[i][j][k][2] * rsd[i][j][k][2]
        + rsd[i][j][k][3] * rsd[i][j][k][3] )
   / rsd[i][j][k][0];
 flux[i][j][k][1] = rsd[i][j][k][1] * u41;
 flux[i][j][k][2] = rsd[i][j][k][2] * u41;
 flux[i][j][k][3] = rsd[i][j][k][3] * u41 + 0.40e+00 *
   ( rsd[i][j][k][4] - q );
 flux[i][j][k][4] = ( 1.40e+00 * rsd[i][j][k][4] - 0.40e+00 * q ) * u41;
      }
      for (k = 1; k <= nz - 2; k++) {
 for (m = 0; m < 5; m++) {
   frct[i][j][k][m] = frct[i][j][k][m]
     - tz2 * ( flux[i][j][k+1][m] - flux[i][j][k-1][m] );
 }
      }
      for (k = 1; k <= nz-1; k++) {
 tmp = 1.0 / rsd[i][j][k][0];
 u21k = tmp * rsd[i][j][k][1];
 u31k = tmp * rsd[i][j][k][2];
 u41k = tmp * rsd[i][j][k][3];
 u51k = tmp * rsd[i][j][k][4];
 tmp = 1.0 / rsd[i][j][k-1][0];
 u21km1 = tmp * rsd[i][j][k-1][1];
 u31km1 = tmp * rsd[i][j][k-1][2];
 u41km1 = tmp * rsd[i][j][k-1][3];
 u51km1 = tmp * rsd[i][j][k-1][4];
 flux[i][j][k][1] = tz3 * ( u21k - u21km1 );
 flux[i][j][k][2] = tz3 * ( u31k - u31km1 );
 flux[i][j][k][3] = (4.0/3.0) * tz3 * ( u41k
         - u41km1 );
 flux[i][j][k][4] = 0.50 * ( 1.0 - 1.40e+00*1.40e+00 )
   * tz3 * ( ( u21k *u21k + u31k *u31k + u41k *u41k )
      - ( u21km1*u21km1 + u31km1*u31km1 + u41km1*u41km1 ) )
   + (1.0/6.0)
   * tz3 * ( u41k*u41k - u41km1*u41km1 )
   + 1.40e+00 * 1.40e+00 * tz3 * ( u51k - u51km1 );
      }
      for (k = 1; k <= nz - 2; k++) {
 frct[i][j][k][0] = frct[i][j][k][0]
   + dz1 * tz1 * ( rsd[i][j][k+1][0]
         - 2.0 * rsd[i][j][k][0]
         + rsd[i][j][k-1][0] );
 frct[i][j][k][1] = frct[i][j][k][1]
   + tz3 * 1.00e-01 * 1.00e+00 * ( flux[i][j][k+1][1] - flux[i][j][k][1] )
   + dz2 * tz1 * ( rsd[i][j][k+1][1]
         - 2.0 * rsd[i][j][k][1]
         + rsd[i][j][k-1][1] );
 frct[i][j][k][2] = frct[i][j][k][2]
   + tz3 * 1.00e-01 * 1.00e+00 * ( flux[i][j][k+1][2] - flux[i][j][k][2] )
   + dz3 * tz1 * ( rsd[i][j][k+1][2]
         - 2.0 * rsd[i][j][k][2]
         + rsd[i][j][k-1][2] );
 frct[i][j][k][3] = frct[i][j][k][3]
   + tz3 * 1.00e-01 * 1.00e+00 * ( flux[i][j][k+1][3] - flux[i][j][k][3] )
   + dz4 * tz1 * ( rsd[i][j][k+1][3]
         - 2.0 * rsd[i][j][k][3]
         + rsd[i][j][k-1][3] );
 frct[i][j][k][4] = frct[i][j][k][4]
   + tz3 * 1.00e-01 * 1.00e+00 * ( flux[i][j][k+1][4] - flux[i][j][k][4] )
   + dz5 * tz1 * ( rsd[i][j][k+1][4]
         - 2.0 * rsd[i][j][k][4]
         + rsd[i][j][k-1][4] );
      }
      for (m = 0; m < 5; m++) {
 frct[i][j][1][m] = frct[i][j][1][m]
   - dsspm * ( + 5.0 * rsd[i][j][1][m]
        - 4.0 * rsd[i][j][2][m]
        + rsd[i][j][3][m] );
 frct[i][j][2][m] = frct[i][j][2][m]
   - dsspm * (- 4.0 * rsd[i][j][1][m]
       + 6.0 * rsd[i][j][2][m]
       - 4.0 * rsd[i][j][3][m]
       + rsd[i][j][4][m] );
      }
      for (k = 3; k <= nz - 4; k++) {
 for (m = 0; m < 5; m++) {
   frct[i][j][k][m] = frct[i][j][k][m]
     - dsspm * ( rsd[i][j][k-2][m]
      - 4.0 * rsd[i][j][k-1][m]
      + 6.0 * rsd[i][j][k][m]
      - 4.0 * rsd[i][j][k+1][m]
      + rsd[i][j][k+2][m] );
 }
      }
      for (m = 0; m < 5; m++) {
 frct[i][j][nz-3][m] = frct[i][j][nz-3][m]
   - dsspm * ( rsd[i][j][nz-5][m]
     - 4.0 * rsd[i][j][nz-4][m]
     + 6.0 * rsd[i][j][nz-3][m]
     - 4.0 * rsd[i][j][nz-2][m] );
        frct[i][j][nz-2][m] = frct[i][j][nz-2][m]
   - dsspm * ( rsd[i][j][nz-4][m]
      - 4.0 * rsd[i][j][nz-3][m]
      + 5.0 * rsd[i][j][nz-2][m] );
      }
    }
  }
}
}
static void error(void) {
  int i, j, k, m;
  int iglob, jglob;
  double tmp;
  double u000ijk[5];
  for (m = 0; m < 5; m++) {
    errnm[m] = 0.0;
  }
  for (i = ist; i <= iend; i++) {
    iglob = i;
    for (j = jst; j <= jend; j++) {
      jglob = j;
      for (k = 1; k <= nz-2; k++) {
 exact( iglob, jglob, k, u000ijk );
 for (m = 0; m < 5; m++) {
   tmp = ( u000ijk[m] - u[i][j][k][m] );
   errnm[m] = errnm[m] + tmp *tmp;
 }
      }
    }
  }
  for (m = 0; m < 5; m++) {
    errnm[m] = sqrt ( errnm[m] / ( (nx0-2)*(ny0-2)*(nz0-2) ) );
  }
}
static void exact( int i, int j, int k, double u000ijk[5] ) {
  int m;
  double xi, eta, zeta;
  xi = ((double)i) / (nx0 - 1);
  eta = ((double)j) / (ny0 - 1);
  zeta = ((double)k) / (nz - 1);
  for (m = 0; m < 5; m++) {
    u000ijk[m] = ce[m][0]
      + ce[m][1] * xi
      + ce[m][2] * eta
      + ce[m][3] * zeta
      + ce[m][4] * xi * xi
      + ce[m][5] * eta * eta
      + ce[m][6] * zeta * zeta
      + ce[m][7] * xi * xi * xi
      + ce[m][8] * eta * eta * eta
      + ce[m][9] * zeta * zeta * zeta
      + ce[m][10] * xi * xi * xi * xi
      + ce[m][11] * eta * eta * eta * eta
      + ce[m][12] * zeta * zeta * zeta * zeta;
  }
}
static void jacld(int k) {
  int i, j;
  double r43;
  double c1345;
  double c34;
  double tmp1, tmp2, tmp3;
  r43 = ( 4.0 / 3.0 );
  c1345 = 1.40e+00 * 1.00e-01 * 1.00e+00 * 1.40e+00;
  c34 = 1.00e-01 * 1.00e+00;
        
#pragma omp for nowait schedule(static)
  for (i = ist; i <= iend; i++) {
    for (j = jst; j <= jend; j++) {
      tmp1 = 1.0 / u[i][j][k][0];
      tmp2 = tmp1 * tmp1;
      tmp3 = tmp1 * tmp2;
      d[i][j][0][0] = 1.0
 + dt * 2.0 * ( tx1 * dx1
    + ty1 * dy1
    + tz1 * dz1 );
      d[i][j][0][1] = 0.0;
      d[i][j][0][2] = 0.0;
      d[i][j][0][3] = 0.0;
      d[i][j][0][4] = 0.0;
      d[i][j][1][0] = dt * 2.0
 * ( tx1 * ( - r43 * c34 * tmp2 * u[i][j][k][1] )
      + ty1 * ( - c34 * tmp2 * u[i][j][k][1] )
      + tz1 * ( - c34 * tmp2 * u[i][j][k][1] ) );
      d[i][j][1][1] = 1.0
 + dt * 2.0
 * ( tx1 * r43 * c34 * tmp1
      + ty1 * c34 * tmp1
      + tz1 * c34 * tmp1 )
 + dt * 2.0 * ( tx1 * dx2
    + ty1 * dy2
    + tz1 * dz2 );
      d[i][j][1][2] = 0.0;
      d[i][j][1][3] = 0.0;
      d[i][j][1][4] = 0.0;
      d[i][j][2][0] = dt * 2.0
 * ( tx1 * ( - c34 * tmp2 * u[i][j][k][2] )
      + ty1 * ( - r43 * c34 * tmp2 * u[i][j][k][2] )
      + tz1 * ( - c34 * tmp2 * u[i][j][k][2] ) );
      d[i][j][2][1] = 0.0;
      d[i][j][2][2] = 1.0
 + dt * 2.0
 * ( tx1 * c34 * tmp1
      + ty1 * r43 * c34 * tmp1
      + tz1 * c34 * tmp1 )
 + dt * 2.0 * ( tx1 * dx3
   + ty1 * dy3
   + tz1 * dz3 );
      d[i][j][2][3] = 0.0;
      d[i][j][2][4] = 0.0;
      d[i][j][3][0] = dt * 2.0
 * ( tx1 * ( - c34 * tmp2 * u[i][j][k][3] )
      + ty1 * ( - c34 * tmp2 * u[i][j][k][3] )
      + tz1 * ( - r43 * c34 * tmp2 * u[i][j][k][3] ) );
      d[i][j][3][1] = 0.0;
      d[i][j][3][2] = 0.0;
      d[i][j][3][3] = 1.0
 + dt * 2.0
 * ( tx1 * c34 * tmp1
      + ty1 * c34 * tmp1
      + tz1 * r43 * c34 * tmp1 )
 + dt * 2.0 * ( tx1 * dx4
   + ty1 * dy4
   + tz1 * dz4 );
      d[i][j][3][4] = 0.0;
      d[i][j][4][0] = dt * 2.0
 * ( tx1 * ( - ( r43*c34 - c1345 ) * tmp3 * ( ((u[i][j][k][1])*(u[i][j][k][1])) )
      - ( c34 - c1345 ) * tmp3 * ( ((u[i][j][k][2])*(u[i][j][k][2])) )
      - ( c34 - c1345 ) * tmp3 * ( ((u[i][j][k][3])*(u[i][j][k][3])) )
      - ( c1345 ) * tmp2 * u[i][j][k][4] )
     + ty1 * ( - ( c34 - c1345 ) * tmp3 * ( ((u[i][j][k][1])*(u[i][j][k][1])) )
        - ( r43*c34 - c1345 ) * tmp3 * ( ((u[i][j][k][2])*(u[i][j][k][2])) )
        - ( c34 - c1345 ) * tmp3 * ( ((u[i][j][k][3])*(u[i][j][k][3])) )
        - ( c1345 ) * tmp2 * u[i][j][k][4] )
     + tz1 * ( - ( c34 - c1345 ) * tmp3 * ( ((u[i][j][k][1])*(u[i][j][k][1])) )
        - ( c34 - c1345 ) * tmp3 * ( ((u[i][j][k][2])*(u[i][j][k][2])) )
        - ( r43*c34 - c1345 ) * tmp3 * ( ((u[i][j][k][3])*(u[i][j][k][3])) )
        - ( c1345 ) * tmp2 * u[i][j][k][4] ) );
      d[i][j][4][1] = dt * 2.0
 * ( tx1 * ( r43*c34 - c1345 ) * tmp2 * u[i][j][k][1]
     + ty1 * ( c34 - c1345 ) * tmp2 * u[i][j][k][1]
     + tz1 * ( c34 - c1345 ) * tmp2 * u[i][j][k][1] );
      d[i][j][4][2] = dt * 2.0
 * ( tx1 * ( c34 - c1345 ) * tmp2 * u[i][j][k][2]
     + ty1 * ( r43*c34 -c1345 ) * tmp2 * u[i][j][k][2]
     + tz1 * ( c34 - c1345 ) * tmp2 * u[i][j][k][2] );
      d[i][j][4][3] = dt * 2.0
 * ( tx1 * ( c34 - c1345 ) * tmp2 * u[i][j][k][3]
     + ty1 * ( c34 - c1345 ) * tmp2 * u[i][j][k][3]
     + tz1 * ( r43*c34 - c1345 ) * tmp2 * u[i][j][k][3] );
      d[i][j][4][4] = 1.0
 + dt * 2.0 * ( tx1 * c1345 * tmp1
         + ty1 * c1345 * tmp1
         + tz1 * c1345 * tmp1 )
        + dt * 2.0 * ( tx1 * dx5
   + ty1 * dy5
   + tz1 * dz5 );
      tmp1 = 1.0 / u[i][j][k-1][0];
      tmp2 = tmp1 * tmp1;
      tmp3 = tmp1 * tmp2;
      a[i][j][0][0] = - dt * tz1 * dz1;
      a[i][j][0][1] = 0.0;
      a[i][j][0][2] = 0.0;
      a[i][j][0][3] = - dt * tz2;
      a[i][j][0][4] = 0.0;
      a[i][j][1][0] = - dt * tz2
 * ( - ( u[i][j][k-1][1]*u[i][j][k-1][3] ) * tmp2 )
 - dt * tz1 * ( - c34 * tmp2 * u[i][j][k-1][1] );
      a[i][j][1][1] = - dt * tz2 * ( u[i][j][k-1][3] * tmp1 )
 - dt * tz1 * c34 * tmp1
 - dt * tz1 * dz2 ;
      a[i][j][1][2] = 0.0;
      a[i][j][1][3] = - dt * tz2 * ( u[i][j][k-1][1] * tmp1 );
      a[i][j][1][4] = 0.0;
      a[i][j][2][0] = - dt * tz2
 * ( - ( u[i][j][k-1][2]*u[i][j][k-1][3] ) * tmp2 )
 - dt * tz1 * ( - c34 * tmp2 * u[i][j][k-1][2] );
      a[i][j][2][1] = 0.0;
      a[i][j][2][2] = - dt * tz2 * ( u[i][j][k-1][3] * tmp1 )
 - dt * tz1 * ( c34 * tmp1 )
 - dt * tz1 * dz3;
      a[i][j][2][3] = - dt * tz2 * ( u[i][j][k-1][2] * tmp1 );
      a[i][j][2][4] = 0.0;
      a[i][j][3][0] = - dt * tz2
 * ( - ( u[i][j][k-1][3] * tmp1 ) *( u[i][j][k-1][3] * tmp1 )
     + 0.50 * 0.40e+00
     * ( ( u[i][j][k-1][1] * u[i][j][k-1][1]
    + u[i][j][k-1][2] * u[i][j][k-1][2]
    + u[i][j][k-1][3] * u[i][j][k-1][3] ) * tmp2 ) )
 - dt * tz1 * ( - r43 * c34 * tmp2 * u[i][j][k-1][3] );
      a[i][j][3][1] = - dt * tz2
 * ( - 0.40e+00 * ( u[i][j][k-1][1] * tmp1 ) );
      a[i][j][3][2] = - dt * tz2
 * ( - 0.40e+00 * ( u[i][j][k-1][2] * tmp1 ) );
      a[i][j][3][3] = - dt * tz2 * ( 2.0 - 0.40e+00 )
 * ( u[i][j][k-1][3] * tmp1 )
 - dt * tz1 * ( r43 * c34 * tmp1 )
 - dt * tz1 * dz4;
      a[i][j][3][4] = - dt * tz2 * 0.40e+00;
      a[i][j][4][0] = - dt * tz2
 * ( ( 0.40e+00 * ( u[i][j][k-1][1] * u[i][j][k-1][1]
                      + u[i][j][k-1][2] * u[i][j][k-1][2]
                      + u[i][j][k-1][3] * u[i][j][k-1][3] ) * tmp2
       - 1.40e+00 * ( u[i][j][k-1][4] * tmp1 ) )
     * ( u[i][j][k-1][3] * tmp1 ) )
 - dt * tz1
 * ( - ( c34 - c1345 ) * tmp3 * (u[i][j][k-1][1]*u[i][j][k-1][1])
     - ( c34 - c1345 ) * tmp3 * (u[i][j][k-1][2]*u[i][j][k-1][2])
     - ( r43*c34 - c1345 )* tmp3 * (u[i][j][k-1][3]*u[i][j][k-1][3])
     - c1345 * tmp2 * u[i][j][k-1][4] );
      a[i][j][4][1] = - dt * tz2
 * ( - 0.40e+00 * ( u[i][j][k-1][1]*u[i][j][k-1][3] ) * tmp2 )
 - dt * tz1 * ( c34 - c1345 ) * tmp2 * u[i][j][k-1][1];
      a[i][j][4][2] = - dt * tz2
 * ( - 0.40e+00 * ( u[i][j][k-1][2]*u[i][j][k-1][3] ) * tmp2 )
 - dt * tz1 * ( c34 - c1345 ) * tmp2 * u[i][j][k-1][2];
      a[i][j][4][3] = - dt * tz2
 * ( 1.40e+00 * ( u[i][j][k-1][4] * tmp1 )
            - 0.50 * 0.40e+00
            * ( ( u[i][j][k-1][1]*u[i][j][k-1][1]
     + u[i][j][k-1][2]*u[i][j][k-1][2]
     + 3.0*u[i][j][k-1][3]*u[i][j][k-1][3] ) * tmp2 ) )
 - dt * tz1 * ( r43*c34 - c1345 ) * tmp2 * u[i][j][k-1][3];
      a[i][j][4][4] = - dt * tz2
 * ( 1.40e+00 * ( u[i][j][k-1][3] * tmp1 ) )
 - dt * tz1 * c1345 * tmp1
 - dt * tz1 * dz5;
      tmp1 = 1.0 / u[i][j-1][k][0];
      tmp2 = tmp1 * tmp1;
      tmp3 = tmp1 * tmp2;
      b[i][j][0][0] = - dt * ty1 * dy1;
      b[i][j][0][1] = 0.0;
      b[i][j][0][2] = - dt * ty2;
      b[i][j][0][3] = 0.0;
      b[i][j][0][4] = 0.0;
      b[i][j][1][0] = - dt * ty2
 * ( - ( u[i][j-1][k][1]*u[i][j-1][k][2] ) * tmp2 )
 - dt * ty1 * ( - c34 * tmp2 * u[i][j-1][k][1] );
      b[i][j][1][1] = - dt * ty2 * ( u[i][j-1][k][2] * tmp1 )
 - dt * ty1 * ( c34 * tmp1 )
 - dt * ty1 * dy2;
      b[i][j][1][2] = - dt * ty2 * ( u[i][j-1][k][1] * tmp1 );
      b[i][j][1][3] = 0.0;
      b[i][j][1][4] = 0.0;
      b[i][j][2][0] = - dt * ty2
 * ( - ( u[i][j-1][k][2] * tmp1 ) *( u[i][j-1][k][2] * tmp1 )
     + 0.50 * 0.40e+00 * ( ( u[i][j-1][k][1] * u[i][j-1][k][1]
          + u[i][j-1][k][2] * u[i][j-1][k][2]
          + u[i][j-1][k][3] * u[i][j-1][k][3] )
       * tmp2 ) )
 - dt * ty1 * ( - r43 * c34 * tmp2 * u[i][j-1][k][2] );
      b[i][j][2][1] = - dt * ty2
 * ( - 0.40e+00 * ( u[i][j-1][k][1] * tmp1 ) );
      b[i][j][2][2] = - dt * ty2 * ( ( 2.0 - 0.40e+00 )
      * ( u[i][j-1][k][2] * tmp1 ) )
 - dt * ty1 * ( r43 * c34 * tmp1 )
 - dt * ty1 * dy3;
      b[i][j][2][3] = - dt * ty2
 * ( - 0.40e+00 * ( u[i][j-1][k][3] * tmp1 ) );
      b[i][j][2][4] = - dt * ty2 * 0.40e+00;
      b[i][j][3][0] = - dt * ty2
 * ( - ( u[i][j-1][k][2]*u[i][j-1][k][3] ) * tmp2 )
 - dt * ty1 * ( - c34 * tmp2 * u[i][j-1][k][3] );
      b[i][j][3][1] = 0.0;
      b[i][j][3][2] = - dt * ty2 * ( u[i][j-1][k][3] * tmp1 );
      b[i][j][3][3] = - dt * ty2 * ( u[i][j-1][k][2] * tmp1 )
 - dt * ty1 * ( c34 * tmp1 )
 - dt * ty1 * dy4;
      b[i][j][3][4] = 0.0;
      b[i][j][4][0] = - dt * ty2
 * ( ( 0.40e+00 * ( u[i][j-1][k][1] * u[i][j-1][k][1]
        + u[i][j-1][k][2] * u[i][j-1][k][2]
        + u[i][j-1][k][3] * u[i][j-1][k][3] ) * tmp2
       - 1.40e+00 * ( u[i][j-1][k][4] * tmp1 ) )
     * ( u[i][j-1][k][2] * tmp1 ) )
 - dt * ty1
 * ( - ( c34 - c1345 )*tmp3*(((u[i][j-1][k][1])*(u[i][j-1][k][1])))
     - ( r43*c34 - c1345 )*tmp3*(((u[i][j-1][k][2])*(u[i][j-1][k][2])))
     - ( c34 - c1345 )*tmp3*(((u[i][j-1][k][3])*(u[i][j-1][k][3])))
     - c1345*tmp2*u[i][j-1][k][4] );
      b[i][j][4][1] = - dt * ty2
 * ( - 0.40e+00 * ( u[i][j-1][k][1]*u[i][j-1][k][2] ) * tmp2 )
 - dt * ty1
 * ( c34 - c1345 ) * tmp2 * u[i][j-1][k][1];
      b[i][j][4][2] = - dt * ty2
 * ( 1.40e+00 * ( u[i][j-1][k][4] * tmp1 )
     - 0.50 * 0.40e+00
     * ( ( u[i][j-1][k][1]*u[i][j-1][k][1]
                   + 3.0 * u[i][j-1][k][2]*u[i][j-1][k][2]
     + u[i][j-1][k][3]*u[i][j-1][k][3] ) * tmp2 ) )
 - dt * ty1
 * ( r43*c34 - c1345 ) * tmp2 * u[i][j-1][k][2];
      b[i][j][4][3] = - dt * ty2
 * ( - 0.40e+00 * ( u[i][j-1][k][2]*u[i][j-1][k][3] ) * tmp2 )
 - dt * ty1 * ( c34 - c1345 ) * tmp2 * u[i][j-1][k][3];
      b[i][j][4][4] = - dt * ty2
 * ( 1.40e+00 * ( u[i][j-1][k][2] * tmp1 ) )
 - dt * ty1 * c1345 * tmp1
 - dt * ty1 * dy5;
      tmp1 = 1.0 / u[i-1][j][k][0];
      tmp2 = tmp1 * tmp1;
      tmp3 = tmp1 * tmp2;
      c[i][j][0][0] = - dt * tx1 * dx1;
      c[i][j][0][1] = - dt * tx2;
      c[i][j][0][2] = 0.0;
      c[i][j][0][3] = 0.0;
      c[i][j][0][4] = 0.0;
      c[i][j][1][0] = - dt * tx2
 * ( - ( u[i-1][j][k][1] * tmp1 ) *( u[i-1][j][k][1] * tmp1 )
     + 0.40e+00 * 0.50 * ( u[i-1][j][k][1] * u[i-1][j][k][1]
                             + u[i-1][j][k][2] * u[i-1][j][k][2]
                             + u[i-1][j][k][3] * u[i-1][j][k][3] ) * tmp2 )
 - dt * tx1 * ( - r43 * c34 * tmp2 * u[i-1][j][k][1] );
      c[i][j][1][1] = - dt * tx2
 * ( ( 2.0 - 0.40e+00 ) * ( u[i-1][j][k][1] * tmp1 ) )
 - dt * tx1 * ( r43 * c34 * tmp1 )
 - dt * tx1 * dx2;
      c[i][j][1][2] = - dt * tx2
 * ( - 0.40e+00 * ( u[i-1][j][k][2] * tmp1 ) );
      c[i][j][1][3] = - dt * tx2
 * ( - 0.40e+00 * ( u[i-1][j][k][3] * tmp1 ) );
      c[i][j][1][4] = - dt * tx2 * 0.40e+00;
      c[i][j][2][0] = - dt * tx2
 * ( - ( u[i-1][j][k][1] * u[i-1][j][k][2] ) * tmp2 )
 - dt * tx1 * ( - c34 * tmp2 * u[i-1][j][k][2] );
      c[i][j][2][1] = - dt * tx2 * ( u[i-1][j][k][2] * tmp1 );
      c[i][j][2][2] = - dt * tx2 * ( u[i-1][j][k][1] * tmp1 )
 - dt * tx1 * ( c34 * tmp1 )
 - dt * tx1 * dx3;
      c[i][j][2][3] = 0.0;
      c[i][j][2][4] = 0.0;
      c[i][j][3][0] = - dt * tx2
 * ( - ( u[i-1][j][k][1]*u[i-1][j][k][3] ) * tmp2 )
 - dt * tx1 * ( - c34 * tmp2 * u[i-1][j][k][3] );
      c[i][j][3][1] = - dt * tx2 * ( u[i-1][j][k][3] * tmp1 );
      c[i][j][3][2] = 0.0;
      c[i][j][3][3] = - dt * tx2 * ( u[i-1][j][k][1] * tmp1 )
 - dt * tx1 * ( c34 * tmp1 )
 - dt * tx1 * dx4;
      c[i][j][3][4] = 0.0;
      c[i][j][4][0] = - dt * tx2
 * ( ( 0.40e+00 * ( u[i-1][j][k][1] * u[i-1][j][k][1]
        + u[i-1][j][k][2] * u[i-1][j][k][2]
        + u[i-1][j][k][3] * u[i-1][j][k][3] ) * tmp2
       - 1.40e+00 * ( u[i-1][j][k][4] * tmp1 ) )
     * ( u[i-1][j][k][1] * tmp1 ) )
 - dt * tx1
 * ( - ( r43*c34 - c1345 ) * tmp3 * ( ((u[i-1][j][k][1])*(u[i-1][j][k][1])) )
     - ( c34 - c1345 ) * tmp3 * ( ((u[i-1][j][k][2])*(u[i-1][j][k][2])) )
     - ( c34 - c1345 ) * tmp3 * ( ((u[i-1][j][k][3])*(u[i-1][j][k][3])) )
     - c1345 * tmp2 * u[i-1][j][k][4] );
      c[i][j][4][1] = - dt * tx2
 * ( 1.40e+00 * ( u[i-1][j][k][4] * tmp1 )
     - 0.50 * 0.40e+00
     * ( ( 3.0*u[i-1][j][k][1]*u[i-1][j][k][1]
     + u[i-1][j][k][2]*u[i-1][j][k][2]
     + u[i-1][j][k][3]*u[i-1][j][k][3] ) * tmp2 ) )
 - dt * tx1
 * ( r43*c34 - c1345 ) * tmp2 * u[i-1][j][k][1];
      c[i][j][4][2] = - dt * tx2
 * ( - 0.40e+00 * ( u[i-1][j][k][2]*u[i-1][j][k][1] ) * tmp2 )
 - dt * tx1
 * ( c34 - c1345 ) * tmp2 * u[i-1][j][k][2];
      c[i][j][4][3] = - dt * tx2
 * ( - 0.40e+00 * ( u[i-1][j][k][3]*u[i-1][j][k][1] ) * tmp2 )
 - dt * tx1
 * ( c34 - c1345 ) * tmp2 * u[i-1][j][k][3];
      c[i][j][4][4] = - dt * tx2
 * ( 1.40e+00 * ( u[i-1][j][k][1] * tmp1 ) )
 - dt * tx1 * c1345 * tmp1
 - dt * tx1 * dx5;
    }
  }
}
static void jacu(int k) {
  int i, j;
  double r43;
  double c1345;
  double c34;
  double tmp1, tmp2, tmp3;
  r43 = ( 4.0 / 3.0 );
  c1345 = 1.40e+00 * 1.00e-01 * 1.00e+00 * 1.40e+00;
  c34 = 1.00e-01 * 1.00e+00;
        
#pragma omp for nowait schedule(static)
  for (i = iend; i >= ist; i--) {
      for (j = jend; j >= jst; j--) {
      tmp1 = 1.0 / u[i][j][k][0];
      tmp2 = tmp1 * tmp1;
      tmp3 = tmp1 * tmp2;
      d[i][j][0][0] = 1.0
 + dt * 2.0 * ( tx1 * dx1
    + ty1 * dy1
    + tz1 * dz1 );
      d[i][j][0][1] = 0.0;
      d[i][j][0][2] = 0.0;
      d[i][j][0][3] = 0.0;
      d[i][j][0][4] = 0.0;
      d[i][j][1][0] = dt * 2.0
 * ( tx1 * ( - r43 * c34 * tmp2 * u[i][j][k][1] )
      + ty1 * ( - c34 * tmp2 * u[i][j][k][1] )
      + tz1 * ( - c34 * tmp2 * u[i][j][k][1] ) );
      d[i][j][1][1] = 1.0
 + dt * 2.0
 * ( tx1 * r43 * c34 * tmp1
      + ty1 * c34 * tmp1
      + tz1 * c34 * tmp1 )
 + dt * 2.0 * ( tx1 * dx2
    + ty1 * dy2
    + tz1 * dz2 );
      d[i][j][1][2] = 0.0;
      d[i][j][1][3] = 0.0;
      d[i][j][1][4] = 0.0;
      d[i][j][2][0] = dt * 2.0
 * ( tx1 * ( - c34 * tmp2 * u[i][j][k][2] )
      + ty1 * ( - r43 * c34 * tmp2 * u[i][j][k][2] )
      + tz1 * ( - c34 * tmp2 * u[i][j][k][2] ) );
      d[i][j][2][1] = 0.0;
      d[i][j][2][2] = 1.0
 + dt * 2.0
 * ( tx1 * c34 * tmp1
      + ty1 * r43 * c34 * tmp1
      + tz1 * c34 * tmp1 )
 + dt * 2.0 * ( tx1 * dx3
   + ty1 * dy3
   + tz1 * dz3 );
      d[i][j][2][3] = 0.0;
      d[i][j][2][4] = 0.0;
      d[i][j][3][0] = dt * 2.0
 * ( tx1 * ( - c34 * tmp2 * u[i][j][k][3] )
      + ty1 * ( - c34 * tmp2 * u[i][j][k][3] )
      + tz1 * ( - r43 * c34 * tmp2 * u[i][j][k][3] ) );
      d[i][j][3][1] = 0.0;
      d[i][j][3][2] = 0.0;
      d[i][j][3][3] = 1.0
 + dt * 2.0
 * ( tx1 * c34 * tmp1
      + ty1 * c34 * tmp1
      + tz1 * r43 * c34 * tmp1 )
 + dt * 2.0 * ( tx1 * dx4
   + ty1 * dy4
   + tz1 * dz4 );
      d[i][j][3][4] = 0.0;
      d[i][j][4][0] = dt * 2.0
 * ( tx1 * ( - ( r43*c34 - c1345 ) * tmp3 * ( ((u[i][j][k][1])*(u[i][j][k][1])) )
      - ( c34 - c1345 ) * tmp3 * ( ((u[i][j][k][2])*(u[i][j][k][2])) )
      - ( c34 - c1345 ) * tmp3 * ( ((u[i][j][k][3])*(u[i][j][k][3])) )
      - ( c1345 ) * tmp2 * u[i][j][k][4] )
     + ty1 * ( - ( c34 - c1345 ) * tmp3 * ( ((u[i][j][k][1])*(u[i][j][k][1])) )
        - ( r43*c34 - c1345 ) * tmp3 * ( ((u[i][j][k][2])*(u[i][j][k][2])) )
        - ( c34 - c1345 ) * tmp3 * ( ((u[i][j][k][3])*(u[i][j][k][3])) )
        - ( c1345 ) * tmp2 * u[i][j][k][4] )
     + tz1 * ( - ( c34 - c1345 ) * tmp3 * ( ((u[i][j][k][1])*(u[i][j][k][1])) )
        - ( c34 - c1345 ) * tmp3 * ( ((u[i][j][k][2])*(u[i][j][k][2])) )
        - ( r43*c34 - c1345 ) * tmp3 * ( ((u[i][j][k][3])*(u[i][j][k][3])) )
        - ( c1345 ) * tmp2 * u[i][j][k][4] ) );
      d[i][j][4][1] = dt * 2.0
 * ( tx1 * ( r43*c34 - c1345 ) * tmp2 * u[i][j][k][1]
     + ty1 * ( c34 - c1345 ) * tmp2 * u[i][j][k][1]
     + tz1 * ( c34 - c1345 ) * tmp2 * u[i][j][k][1] );
      d[i][j][4][2] = dt * 2.0
 * ( tx1 * ( c34 - c1345 ) * tmp2 * u[i][j][k][2]
     + ty1 * ( r43*c34 -c1345 ) * tmp2 * u[i][j][k][2]
     + tz1 * ( c34 - c1345 ) * tmp2 * u[i][j][k][2] );
      d[i][j][4][3] = dt * 2.0
 * ( tx1 * ( c34 - c1345 ) * tmp2 * u[i][j][k][3]
     + ty1 * ( c34 - c1345 ) * tmp2 * u[i][j][k][3]
     + tz1 * ( r43*c34 - c1345 ) * tmp2 * u[i][j][k][3] );
      d[i][j][4][4] = 1.0
        + dt * 2.0 * ( tx1 * c1345 * tmp1
         + ty1 * c1345 * tmp1
         + tz1 * c1345 * tmp1 )
        + dt * 2.0 * ( tx1 * dx5
   + ty1 * dy5
   + tz1 * dz5 );
      tmp1 = 1.0 / u[i+1][j][k][0];
      tmp2 = tmp1 * tmp1;
      tmp3 = tmp1 * tmp2;
      a[i][j][0][0] = - dt * tx1 * dx1;
      a[i][j][0][1] = dt * tx2;
      a[i][j][0][2] = 0.0;
      a[i][j][0][3] = 0.0;
      a[i][j][0][4] = 0.0;
      a[i][j][1][0] = dt * tx2
 * ( - ( u[i+1][j][k][1] * tmp1 ) *( u[i+1][j][k][1] * tmp1 )
     + 0.40e+00 * 0.50 * ( u[i+1][j][k][1] * u[i+1][j][k][1]
                             + u[i+1][j][k][2] * u[i+1][j][k][2]
                             + u[i+1][j][k][3] * u[i+1][j][k][3] ) * tmp2 )
 - dt * tx1 * ( - r43 * c34 * tmp2 * u[i+1][j][k][1] );
      a[i][j][1][1] = dt * tx2
 * ( ( 2.0 - 0.40e+00 ) * ( u[i+1][j][k][1] * tmp1 ) )
 - dt * tx1 * ( r43 * c34 * tmp1 )
 - dt * tx1 * dx2;
      a[i][j][1][2] = dt * tx2
 * ( - 0.40e+00 * ( u[i+1][j][k][2] * tmp1 ) );
      a[i][j][1][3] = dt * tx2
 * ( - 0.40e+00 * ( u[i+1][j][k][3] * tmp1 ) );
      a[i][j][1][4] = dt * tx2 * 0.40e+00 ;
      a[i][j][2][0] = dt * tx2
 * ( - ( u[i+1][j][k][1] * u[i+1][j][k][2] ) * tmp2 )
 - dt * tx1 * ( - c34 * tmp2 * u[i+1][j][k][2] );
      a[i][j][2][1] = dt * tx2 * ( u[i+1][j][k][2] * tmp1 );
      a[i][j][2][2] = dt * tx2 * ( u[i+1][j][k][1] * tmp1 )
 - dt * tx1 * ( c34 * tmp1 )
 - dt * tx1 * dx3;
      a[i][j][2][3] = 0.0;
      a[i][j][2][4] = 0.0;
      a[i][j][3][0] = dt * tx2
 * ( - ( u[i+1][j][k][1]*u[i+1][j][k][3] ) * tmp2 )
 - dt * tx1 * ( - c34 * tmp2 * u[i+1][j][k][3] );
      a[i][j][3][1] = dt * tx2 * ( u[i+1][j][k][3] * tmp1 );
      a[i][j][3][2] = 0.0;
      a[i][j][3][3] = dt * tx2 * ( u[i+1][j][k][1] * tmp1 )
 - dt * tx1 * ( c34 * tmp1 )
 - dt * tx1 * dx4;
      a[i][j][3][4] = 0.0;
      a[i][j][4][0] = dt * tx2
 * ( ( 0.40e+00 * ( u[i+1][j][k][1] * u[i+1][j][k][1]
        + u[i+1][j][k][2] * u[i+1][j][k][2]
        + u[i+1][j][k][3] * u[i+1][j][k][3] ) * tmp2
       - 1.40e+00 * ( u[i+1][j][k][4] * tmp1 ) )
     * ( u[i+1][j][k][1] * tmp1 ) )
 - dt * tx1
 * ( - ( r43*c34 - c1345 ) * tmp3 * ( ((u[i+1][j][k][1])*(u[i+1][j][k][1])) )
     - ( c34 - c1345 ) * tmp3 * ( ((u[i+1][j][k][2])*(u[i+1][j][k][2])) )
     - ( c34 - c1345 ) * tmp3 * ( ((u[i+1][j][k][3])*(u[i+1][j][k][3])) )
     - c1345 * tmp2 * u[i+1][j][k][4] );
      a[i][j][4][1] = dt * tx2
 * ( 1.40e+00 * ( u[i+1][j][k][4] * tmp1 )
     - 0.50 * 0.40e+00
     * ( ( 3.0*u[i+1][j][k][1]*u[i+1][j][k][1]
     + u[i+1][j][k][2]*u[i+1][j][k][2]
     + u[i+1][j][k][3]*u[i+1][j][k][3] ) * tmp2 ) )
 - dt * tx1
 * ( r43*c34 - c1345 ) * tmp2 * u[i+1][j][k][1];
      a[i][j][4][2] = dt * tx2
 * ( - 0.40e+00 * ( u[i+1][j][k][2]*u[i+1][j][k][1] ) * tmp2 )
 - dt * tx1
 * ( c34 - c1345 ) * tmp2 * u[i+1][j][k][2];
      a[i][j][4][3] = dt * tx2
 * ( - 0.40e+00 * ( u[i+1][j][k][3]*u[i+1][j][k][1] ) * tmp2 )
 - dt * tx1
 * ( c34 - c1345 ) * tmp2 * u[i+1][j][k][3];
      a[i][j][4][4] = dt * tx2
 * ( 1.40e+00 * ( u[i+1][j][k][1] * tmp1 ) )
 - dt * tx1 * c1345 * tmp1
 - dt * tx1 * dx5;
      tmp1 = 1.0 / u[i][j+1][k][0];
      tmp2 = tmp1 * tmp1;
      tmp3 = tmp1 * tmp2;
      b[i][j][0][0] = - dt * ty1 * dy1;
      b[i][j][0][1] = 0.0;
      b[i][j][0][2] = dt * ty2;
      b[i][j][0][3] = 0.0;
      b[i][j][0][4] = 0.0;
      b[i][j][1][0] = dt * ty2
 * ( - ( u[i][j+1][k][1]*u[i][j+1][k][2] ) * tmp2 )
 - dt * ty1 * ( - c34 * tmp2 * u[i][j+1][k][1] );
      b[i][j][1][1] = dt * ty2 * ( u[i][j+1][k][2] * tmp1 )
 - dt * ty1 * ( c34 * tmp1 )
 - dt * ty1 * dy2;
      b[i][j][1][2] = dt * ty2 * ( u[i][j+1][k][1] * tmp1 );
      b[i][j][1][3] = 0.0;
      b[i][j][1][4] = 0.0;
      b[i][j][2][0] = dt * ty2
 * ( - ( u[i][j+1][k][2] * tmp1 ) *( u[i][j+1][k][2] * tmp1 )
     + 0.50 * 0.40e+00 * ( ( u[i][j+1][k][1] * u[i][j+1][k][1]
          + u[i][j+1][k][2] * u[i][j+1][k][2]
          + u[i][j+1][k][3] * u[i][j+1][k][3] )
       * tmp2 ) )
 - dt * ty1 * ( - r43 * c34 * tmp2 * u[i][j+1][k][2] );
      b[i][j][2][1] = dt * ty2
 * ( - 0.40e+00 * ( u[i][j+1][k][1] * tmp1 ) );
      b[i][j][2][2] = dt * ty2 * ( ( 2.0 - 0.40e+00 )
     * ( u[i][j+1][k][2] * tmp1 ) )
 - dt * ty1 * ( r43 * c34 * tmp1 )
 - dt * ty1 * dy3;
      b[i][j][2][3] = dt * ty2
 * ( - 0.40e+00 * ( u[i][j+1][k][3] * tmp1 ) );
      b[i][j][2][4] = dt * ty2 * 0.40e+00;
      b[i][j][3][0] = dt * ty2
 * ( - ( u[i][j+1][k][2]*u[i][j+1][k][3] ) * tmp2 )
 - dt * ty1 * ( - c34 * tmp2 * u[i][j+1][k][3] );
      b[i][j][3][1] = 0.0;
      b[i][j][3][2] = dt * ty2 * ( u[i][j+1][k][3] * tmp1 );
      b[i][j][3][3] = dt * ty2 * ( u[i][j+1][k][2] * tmp1 )
 - dt * ty1 * ( c34 * tmp1 )
 - dt * ty1 * dy4;
      b[i][j][3][4] = 0.0;
      b[i][j][4][0] = dt * ty2
 * ( ( 0.40e+00 * ( u[i][j+1][k][1] * u[i][j+1][k][1]
        + u[i][j+1][k][2] * u[i][j+1][k][2]
        + u[i][j+1][k][3] * u[i][j+1][k][3] ) * tmp2
       - 1.40e+00 * ( u[i][j+1][k][4] * tmp1 ) )
     * ( u[i][j+1][k][2] * tmp1 ) )
 - dt * ty1
 * ( - ( c34 - c1345 )*tmp3*( ((u[i][j+1][k][1])*(u[i][j+1][k][1])) )
     - ( r43*c34 - c1345 )*tmp3*( ((u[i][j+1][k][2])*(u[i][j+1][k][2])) )
     - ( c34 - c1345 )*tmp3*( ((u[i][j+1][k][3])*(u[i][j+1][k][3])) )
     - c1345*tmp2*u[i][j+1][k][4] );
      b[i][j][4][1] = dt * ty2
 * ( - 0.40e+00 * ( u[i][j+1][k][1]*u[i][j+1][k][2] ) * tmp2 )
 - dt * ty1
 * ( c34 - c1345 ) * tmp2 * u[i][j+1][k][1];
      b[i][j][4][2] = dt * ty2
 * ( 1.40e+00 * ( u[i][j+1][k][4] * tmp1 )
     - 0.50 * 0.40e+00
     * ( ( u[i][j+1][k][1]*u[i][j+1][k][1]
     + 3.0 * u[i][j+1][k][2]*u[i][j+1][k][2]
     + u[i][j+1][k][3]*u[i][j+1][k][3] ) * tmp2 ) )
 - dt * ty1
 * ( r43*c34 - c1345 ) * tmp2 * u[i][j+1][k][2];
      b[i][j][4][3] = dt * ty2
 * ( - 0.40e+00 * ( u[i][j+1][k][2]*u[i][j+1][k][3] ) * tmp2 )
 - dt * ty1 * ( c34 - c1345 ) * tmp2 * u[i][j+1][k][3];
      b[i][j][4][4] = dt * ty2
 * ( 1.40e+00 * ( u[i][j+1][k][2] * tmp1 ) )
 - dt * ty1 * c1345 * tmp1
 - dt * ty1 * dy5;
      tmp1 = 1.0 / u[i][j][k+1][0];
      tmp2 = tmp1 * tmp1;
      tmp3 = tmp1 * tmp2;
      c[i][j][0][0] = - dt * tz1 * dz1;
      c[i][j][0][1] = 0.0;
      c[i][j][0][2] = 0.0;
      c[i][j][0][3] = dt * tz2;
      c[i][j][0][4] = 0.0;
      c[i][j][1][0] = dt * tz2
 * ( - ( u[i][j][k+1][1]*u[i][j][k+1][3] ) * tmp2 )
 - dt * tz1 * ( - c34 * tmp2 * u[i][j][k+1][1] );
      c[i][j][1][1] = dt * tz2 * ( u[i][j][k+1][3] * tmp1 )
 - dt * tz1 * c34 * tmp1
 - dt * tz1 * dz2 ;
      c[i][j][1][2] = 0.0;
      c[i][j][1][3] = dt * tz2 * ( u[i][j][k+1][1] * tmp1 );
      c[i][j][1][4] = 0.0;
      c[i][j][2][0] = dt * tz2
 * ( - ( u[i][j][k+1][2]*u[i][j][k+1][3] ) * tmp2 )
 - dt * tz1 * ( - c34 * tmp2 * u[i][j][k+1][2] );
      c[i][j][2][1] = 0.0;
      c[i][j][2][2] = dt * tz2 * ( u[i][j][k+1][3] * tmp1 )
 - dt * tz1 * ( c34 * tmp1 )
 - dt * tz1 * dz3;
      c[i][j][2][3] = dt * tz2 * ( u[i][j][k+1][2] * tmp1 );
      c[i][j][2][4] = 0.0;
      c[i][j][3][0] = dt * tz2
 * ( - ( u[i][j][k+1][3] * tmp1 ) *( u[i][j][k+1][3] * tmp1 )
     + 0.50 * 0.40e+00
     * ( ( u[i][j][k+1][1] * u[i][j][k+1][1]
    + u[i][j][k+1][2] * u[i][j][k+1][2]
    + u[i][j][k+1][3] * u[i][j][k+1][3] ) * tmp2 ) )
 - dt * tz1 * ( - r43 * c34 * tmp2 * u[i][j][k+1][3] );
      c[i][j][3][1] = dt * tz2
 * ( - 0.40e+00 * ( u[i][j][k+1][1] * tmp1 ) );
      c[i][j][3][2] = dt * tz2
 * ( - 0.40e+00 * ( u[i][j][k+1][2] * tmp1 ) );
      c[i][j][3][3] = dt * tz2 * ( 2.0 - 0.40e+00 )
 * ( u[i][j][k+1][3] * tmp1 )
 - dt * tz1 * ( r43 * c34 * tmp1 )
 - dt * tz1 * dz4;
      c[i][j][3][4] = dt * tz2 * 0.40e+00;
      c[i][j][4][0] = dt * tz2
 * ( ( 0.40e+00 * ( u[i][j][k+1][1] * u[i][j][k+1][1]
                      + u[i][j][k+1][2] * u[i][j][k+1][2]
                      + u[i][j][k+1][3] * u[i][j][k+1][3] ) * tmp2
       - 1.40e+00 * ( u[i][j][k+1][4] * tmp1 ) )
     * ( u[i][j][k+1][3] * tmp1 ) )
 - dt * tz1
 * ( - ( c34 - c1345 ) * tmp3 * ( ((u[i][j][k+1][1])*(u[i][j][k+1][1])) )
     - ( c34 - c1345 ) * tmp3 * ( ((u[i][j][k+1][2])*(u[i][j][k+1][2])) )
     - ( r43*c34 - c1345 )* tmp3 * ( ((u[i][j][k+1][3])*(u[i][j][k+1][3])) )
     - c1345 * tmp2 * u[i][j][k+1][4] );
      c[i][j][4][1] = dt * tz2
 * ( - 0.40e+00 * ( u[i][j][k+1][1]*u[i][j][k+1][3] ) * tmp2 )
 - dt * tz1 * ( c34 - c1345 ) * tmp2 * u[i][j][k+1][1];
      c[i][j][4][2] = dt * tz2
 * ( - 0.40e+00 * ( u[i][j][k+1][2]*u[i][j][k+1][3] ) * tmp2 )
 - dt * tz1 * ( c34 - c1345 ) * tmp2 * u[i][j][k+1][2];
      c[i][j][4][3] = dt * tz2
 * ( 1.40e+00 * ( u[i][j][k+1][4] * tmp1 )
            - 0.50 * 0.40e+00
            * ( ( u[i][j][k+1][1]*u[i][j][k+1][1]
     + u[i][j][k+1][2]*u[i][j][k+1][2]
     + 3.0*u[i][j][k+1][3]*u[i][j][k+1][3] ) * tmp2 ) )
 - dt * tz1 * ( r43*c34 - c1345 ) * tmp2 * u[i][j][k+1][3];
      c[i][j][4][4] = dt * tz2
 * ( 1.40e+00 * ( u[i][j][k+1][3] * tmp1 ) )
 - dt * tz1 * c1345 * tmp1
 - dt * tz1 * dz5;
    }
  }
}
static void l2norm (int nx0, int ny0, int nz0,
      int ist, int iend,
      int jst, int jend,
      double v[162][162/2*2+1][162/2*2+1][5],
      double sum[5]) {
        
#pragma omp parallel
{
  int i, j, k, m;
  double sum0=0.0, sum1=0.0, sum2=0.0, sum3=0.0, sum4=0.0;
        
#pragma omp single
  for (m = 0; m < 5; m++) {
    sum[m] = 0.0;
  }
        
#pragma omp for nowait
  for (i = ist; i <= iend; i++) {
    for (j = jst; j <= jend; j++) {
      for (k = 1; k <= nz0-2; k++) {
   sum0 = sum0 + v[i][j][k][0] * v[i][j][k][0];
   sum1 = sum1 + v[i][j][k][1] * v[i][j][k][1];
   sum2 = sum2 + v[i][j][k][2] * v[i][j][k][2];
   sum3 = sum3 + v[i][j][k][3] * v[i][j][k][3];
   sum4 = sum4 + v[i][j][k][4] * v[i][j][k][4];
      }
    }
  }
        
#pragma omp critical
  {
      sum[0] += sum0;
      sum[1] += sum1;
      sum[2] += sum2;
      sum[3] += sum3;
      sum[4] += sum4;
  }
        
#pragma omp barrier
        
#pragma omp single
  for (m = 0; m < 5; m++) {
    sum[m] = sqrt ( sum[m] / ( (nx0-2)*(ny0-2)*(nz0-2) ) );
  }
}
}
static void pintgr(void) {
  int i, j, k;
  int ibeg, ifin, ifin1;
  int jbeg, jfin, jfin1;
  int iglob, iglob1, iglob2;
  int jglob, jglob1, jglob2;
  double phi1[162 +2][162 +2];
  double phi2[162 +2][162 +2];
  double frc1, frc2, frc3;
  ibeg = nx;
  ifin = 0;
  iglob1 = -1;
  iglob2 = nx-1;
  if (iglob1 >= ii1 && iglob2 < ii2+nx) ibeg = 0;
  if (iglob1 >= ii1-nx && iglob2 <= ii2) ifin = nx;
  if (ii1 >= iglob1 && ii1 <= iglob2) ibeg = ii1;
  if (ii2 >= iglob1 && ii2 <= iglob2) ifin = ii2;
  jbeg = ny;
  jfin = -1;
  jglob1 = 0;
  jglob2 = ny-1;
  if (jglob1 >= ji1 && jglob2 < ji2+ny) jbeg = 0;
  if (jglob1 > ji1-ny && jglob2 <= ji2) jfin = ny;
  if (ji1 >= jglob1 && ji1 <= jglob2) jbeg = ji1;
  if (ji2 >= jglob1 && ji2 <= jglob2) jfin = ji2;
  ifin1 = ifin;
  jfin1 = jfin;
  if (ifin1 == ii2) ifin1 = ifin -1;
  if (jfin1 == ji2) jfin1 = jfin -1;
  for (i = 0; i <= 162 +1; i++) {
    for (k = 0; k <= 162 +1; k++) {
      phi1[i][k] = 0.0;
      phi2[i][k] = 0.0;
    }
  }
  for (i = ibeg; i <= ifin; i++) {
    iglob = i;
    for (j = jbeg; j <= jfin; j++) {
      jglob = j;
      k = ki1;
      phi1[i][j] = 0.40e+00*( u[i][j][k][4]
   - 0.50 * ( ((u[i][j][k][1])*(u[i][j][k][1]))
        + ((u[i][j][k][2])*(u[i][j][k][2]))
        + ((u[i][j][k][3])*(u[i][j][k][3])) )
   / u[i][j][k][0] );
      k = ki2;
      phi2[i][j] = 0.40e+00*( u[i][j][k][4]
   - 0.50 * ( ((u[i][j][k][1])*(u[i][j][k][1]))
        + ((u[i][j][k][2])*(u[i][j][k][2]))
        + ((u[i][j][k][3])*(u[i][j][k][3])) )
   / u[i][j][k][0] );
    }
  }
  frc1 = 0.0;
  for (i = ibeg; i <= ifin1; i++) {
    for (j = jbeg; j <= jfin1; j++) {
      frc1 = frc1 + ( phi1[i][j]
         + phi1[i+1][j]
         + phi1[i][j+1]
         + phi1[i+1][j+1]
         + phi2[i][j]
         + phi2[i+1][j]
         + phi2[i][j+1]
         + phi2[i+1][j+1] );
    }
  }
  frc1 = dxi * deta * frc1;
  for (i = 0; i <= 162 +1; i++) {
    for (k = 0; k <= 162 +1; k++) {
      phi1[i][k] = 0.0;
      phi2[i][k] = 0.0;
    }
  }
  jglob = jbeg;
  if (jglob == ji1) {
    for (i = ibeg; i <= ifin; i++) {
      iglob = i;
      for (k = ki1; k <= ki2; k++) {
 phi1[i][k] = 0.40e+00*( u[i][jbeg][k][4]
     - 0.50 * ( ((u[i][jbeg][k][1])*(u[i][jbeg][k][1]))
          + ((u[i][jbeg][k][2])*(u[i][jbeg][k][2]))
          + ((u[i][jbeg][k][3])*(u[i][jbeg][k][3])) )
     / u[i][jbeg][k][0] );
      }
    }
  }
  jglob = jfin;
  if (jglob == ji2) {
    for (i = ibeg; i <= ifin; i++) {
      iglob = i;
      for (k = ki1; k <= ki2; k++) {
 phi2[i][k] = 0.40e+00*( u[i][jfin][k][4]
     - 0.50 * ( ((u[i][jfin][k][1])*(u[i][jfin][k][1]))
          + ((u[i][jfin][k][2])*(u[i][jfin][k][2]))
          + ((u[i][jfin][k][3])*(u[i][jfin][k][3])) )
     / u[i][jfin][k][0] );
      }
    }
  }
  frc2 = 0.0;
  for (i = ibeg; i <= ifin1; i++) {
    for (k = ki1; k <= ki2-1; k++) {
      frc2 = frc2 + ( phi1[i][k]
         + phi1[i+1][k]
         + phi1[i][k+1]
         + phi1[i+1][k+1]
         + phi2[i][k]
         + phi2[i+1][k]
         + phi2[i][k+1]
         + phi2[i+1][k+1] );
    }
  }
  frc2 = dxi * dzeta * frc2;
  for (i = 0; i <= 162 +1; i++) {
    for (k = 0; k <= 162 +1; k++) {
      phi1[i][k] = 0.0;
      phi2[i][k] = 0.0;
    }
  }
  iglob = ibeg;
  if (iglob == ii1) {
    for (j = jbeg; j <= jfin; j++) {
      jglob = j;
      for (k = ki1; k <= ki2; k++) {
 phi1[j][k] = 0.40e+00*( u[ibeg][j][k][4]
     - 0.50 * ( ((u[ibeg][j][k][1])*(u[ibeg][j][k][1]))
          + ((u[ibeg][j][k][2])*(u[ibeg][j][k][2]))
          + ((u[ibeg][j][k][3])*(u[ibeg][j][k][3])) )
     / u[ibeg][j][k][0] );
      }
    }
  }
  iglob = ifin;
  if (iglob == ii2) {
    for (j = jbeg; j <= jfin; j++) {
      jglob = j;
      for (k = ki1; k <= ki2; k++) {
 phi2[j][k] = 0.40e+00*( u[ifin][j][k][4]
     - 0.50 * ( ((u[ifin][j][k][1])*(u[ifin][j][k][1]))
          + ((u[ifin][j][k][2])*(u[ifin][j][k][2]))
          + ((u[ifin][j][k][3])*(u[ifin][j][k][3])) )
     / u[ifin][j][k][0] );
      }
    }
  }
  frc3 = 0.0;
  for (j = jbeg; j <= jfin1; j++) {
    for (k = ki1; k <= ki2-1; k++) {
      frc3 = frc3 + ( phi1[j][k]
         + phi1[j+1][k]
         + phi1[j][k+1]
         + phi1[j+1][k+1]
         + phi2[j][k]
         + phi2[j+1][k]
         + phi2[j][k+1]
         + phi2[j+1][k+1] );
    }
  }
  frc3 = deta * dzeta * frc3;
  frc = 0.25 * ( frc1 + frc2 + frc3 );
}
static void read_input(void) {
  FILE *fp;
  printf("\n\n NAS Parallel Benchmarks 3.0 structured OpenMP C version"
  " - LU Benchmark\n\n");
  fp = fopen("inputlu.data", "r");
  if (fp != ((void *)0)) {
    printf(" Reading from input file inputlu.data\n");
    while(fgetc(fp) != '\n'); while(fgetc(fp) != '\n');
    fscanf(fp, "%d%d", &ipr, &inorm);
    while(fgetc(fp) != '\n');
    while(fgetc(fp) != '\n'); while(fgetc(fp) != '\n');
    fscanf(fp, "%d", &itmax);
    while(fgetc(fp) != '\n');
    while(fgetc(fp) != '\n'); while(fgetc(fp) != '\n');
    fscanf(fp, "%lf", &dt);
    while(fgetc(fp) != '\n');
    while(fgetc(fp) != '\n'); while(fgetc(fp) != '\n');
    fscanf(fp, "%lf", &omega);
    while(fgetc(fp) != '\n');
    while(fgetc(fp) != '\n'); while(fgetc(fp) != '\n');
    fscanf(fp, "%lf%lf%lf%lf%lf",
    &tolrsd[0], &tolrsd[1], &tolrsd[2], &tolrsd[3], &tolrsd[4]);
    while(fgetc(fp) != '\n');
    while(fgetc(fp) != '\n'); while(fgetc(fp) != '\n');
    fscanf(fp, "%d%d%d", &nx0, &ny0, &nz0);
    while(fgetc(fp) != '\n');
    fclose(fp);
  } else {
    ipr = 1;
    inorm = 250;
    itmax = 250;
    dt = 2.0;
    omega = 1.2;
    tolrsd[0] = 1.0e-8;
    tolrsd[1] = 1.0e-8;
    tolrsd[2] = 1.0e-8;
    tolrsd[3] = 1.0e-8;
    tolrsd[4] = 1.0e-8;
    nx0 = 162;
    ny0 = 162;
    nz0 = 162;
  }
  if ( nx0 < 4 || ny0 < 4 || nz0 < 4 ) {
    printf("     PROBLEM SIZE IS TOO SMALL - \n"
    "     SET EACH OF NX, NY AND NZ AT LEAST EQUAL TO 5\n");
    exit(1);
  }
  if ( nx0 > 162 || ny0 > 162 || nz0 > 162 ) {
    printf("     PROBLEM SIZE IS TOO LARGE - \n"
    "     NX, NY AND NZ SHOULD BE EQUAL TO \n"
    "     ISIZ1, ISIZ2 AND ISIZ3 RESPECTIVELY\n");
    exit(1);
  }
  printf(" Size: %3dx%3dx%3d\n", nx0, ny0, nz0);
  printf(" Iterations: %3d\n", itmax);
}
static void rhs(void) {
        
#pragma omp parallel
{
  int i, j, k, m;
  int L1, L2;
  int ist1, iend1;
  int jst1, jend1;
  double q;
  double u21, u31, u41;
  double tmp;
  double u21i, u31i, u41i, u51i;
  double u21j, u31j, u41j, u51j;
  double u21k, u31k, u41k, u51k;
  double u21im1, u31im1, u41im1, u51im1;
  double u21jm1, u31jm1, u41jm1, u51jm1;
  double u21km1, u31km1, u41km1, u51km1;
        
#pragma omp for
  for (i = 0; i <= nx-1; i++) {
    for (j = 0; j <= ny-1; j++) {
      for (k = 0; k <= nz-1; k++) {
 for (m = 0; m < 5; m++) {
   rsd[i][j][k][m] = - frct[i][j][k][m];
 }
      }
    }
  }
  L1 = 0;
  L2 = nx-1;
        
#pragma omp for
  for (i = L1; i <= L2; i++) {
    for (j = jst; j <= jend; j++) {
      for (k = 1; k <= nz - 2; k++) {
 flux[i][j][k][0] = u[i][j][k][1];
 u21 = u[i][j][k][1] / u[i][j][k][0];
 q = 0.50 * ( u[i][j][k][1] * u[i][j][k][1]
        + u[i][j][k][2] * u[i][j][k][2]
        + u[i][j][k][3] * u[i][j][k][3] )
   / u[i][j][k][0];
 flux[i][j][k][1] = u[i][j][k][1] * u21 + 0.40e+00 *
   ( u[i][j][k][4] - q );
 flux[i][j][k][2] = u[i][j][k][2] * u21;
 flux[i][j][k][3] = u[i][j][k][3] * u21;
 flux[i][j][k][4] = ( 1.40e+00 * u[i][j][k][4] - 0.40e+00 * q ) * u21;
      }
    }
  }
        
#pragma omp for
  for (j = jst; j <= jend; j++) {
    for (k = 1; k <= nz - 2; k++) {
      for (i = ist; i <= iend; i++) {
 for (m = 0; m < 5; m++) {
   rsd[i][j][k][m] = rsd[i][j][k][m]
     - tx2 * ( flux[i+1][j][k][m] - flux[i-1][j][k][m] );
 }
      }
      L2 = nx-1;
      for (i = ist; i <= L2; i++) {
 tmp = 1.0 / u[i][j][k][0];
 u21i = tmp * u[i][j][k][1];
 u31i = tmp * u[i][j][k][2];
 u41i = tmp * u[i][j][k][3];
 u51i = tmp * u[i][j][k][4];
 tmp = 1.0 / u[i-1][j][k][0];
 u21im1 = tmp * u[i-1][j][k][1];
 u31im1 = tmp * u[i-1][j][k][2];
 u41im1 = tmp * u[i-1][j][k][3];
 u51im1 = tmp * u[i-1][j][k][4];
 flux[i][j][k][1] = (4.0/3.0) * tx3 * (u21i-u21im1);
 flux[i][j][k][2] = tx3 * ( u31i - u31im1 );
 flux[i][j][k][3] = tx3 * ( u41i - u41im1 );
 flux[i][j][k][4] = 0.50 * ( 1.0 - 1.40e+00*1.40e+00 )
   * tx3 * ( ( ((u21i)*(u21i)) + ((u31i)*(u31i)) + ((u41i)*(u41i)) )
      - ( ((u21im1)*(u21im1)) + ((u31im1)*(u31im1)) + ((u41im1)*(u41im1)) ) )
   + (1.0/6.0)
   * tx3 * ( ((u21i)*(u21i)) - ((u21im1)*(u21im1)) )
   + 1.40e+00 * 1.40e+00 * tx3 * ( u51i - u51im1 );
      }
      for (i = ist; i <= iend; i++) {
 rsd[i][j][k][0] = rsd[i][j][k][0]
   + dx1 * tx1 * ( u[i-1][j][k][0]
         - 2.0 * u[i][j][k][0]
         + u[i+1][j][k][0] );
 rsd[i][j][k][1] = rsd[i][j][k][1]
   + tx3 * 1.00e-01 * 1.00e+00 * ( flux[i+1][j][k][1] - flux[i][j][k][1] )
   + dx2 * tx1 * ( u[i-1][j][k][1]
         - 2.0 * u[i][j][k][1]
         + u[i+1][j][k][1] );
 rsd[i][j][k][2] = rsd[i][j][k][2]
   + tx3 * 1.00e-01 * 1.00e+00 * ( flux[i+1][j][k][2] - flux[i][j][k][2] )
   + dx3 * tx1 * ( u[i-1][j][k][2]
         - 2.0 * u[i][j][k][2]
         + u[i+1][j][k][2] );
 rsd[i][j][k][3] = rsd[i][j][k][3]
   + tx3 * 1.00e-01 * 1.00e+00 * ( flux[i+1][j][k][3] - flux[i][j][k][3] )
   + dx4 * tx1 * ( u[i-1][j][k][3]
         - 2.0 * u[i][j][k][3]
         + u[i+1][j][k][3] );
 rsd[i][j][k][4] = rsd[i][j][k][4]
   + tx3 * 1.00e-01 * 1.00e+00 * ( flux[i+1][j][k][4] - flux[i][j][k][4] )
   + dx5 * tx1 * ( u[i-1][j][k][4]
         - 2.0 * u[i][j][k][4]
         + u[i+1][j][k][4] );
      }
      for (m = 0; m < 5; m++) {
 rsd[1][j][k][m] = rsd[1][j][k][m]
   - dssp * ( + 5.0 * u[1][j][k][m]
       - 4.0 * u[2][j][k][m]
       + u[3][j][k][m] );
 rsd[2][j][k][m] = rsd[2][j][k][m]
   - dssp * ( - 4.0 * u[1][j][k][m]
       + 6.0 * u[2][j][k][m]
       - 4.0 * u[3][j][k][m]
       + u[4][j][k][m] );
      }
      ist1 = 3;
      iend1 = nx - 4;
      for (i = ist1; i <= iend1; i++) {
 for (m = 0; m < 5; m++) {
   rsd[i][j][k][m] = rsd[i][j][k][m]
     - dssp * ( u[i-2][j][k][m]
      - 4.0 * u[i-1][j][k][m]
      + 6.0 * u[i][j][k][m]
      - 4.0 * u[i+1][j][k][m]
      + u[i+2][j][k][m] );
 }
      }
      for (m = 0; m < 5; m++) {
 rsd[nx-3][j][k][m] = rsd[nx-3][j][k][m]
   - dssp * ( u[nx-5][j][k][m]
     - 4.0 * u[nx-4][j][k][m]
     + 6.0 * u[nx-3][j][k][m]
     - 4.0 * u[nx-2][j][k][m] );
 rsd[nx-2][j][k][m] = rsd[nx-2][j][k][m]
   - dssp * ( u[nx-4][j][k][m]
     - 4.0 * u[nx-3][j][k][m]
     + 5.0 * u[nx-2][j][k][m] );
      }
    }
  }
  L1 = 0;
  L2 = ny-1;
        
#pragma omp for
  for (i = ist; i <= iend; i++) {
    for (j = L1; j <= L2; j++) {
      for (k = 1; k <= nz - 2; k++) {
 flux[i][j][k][0] = u[i][j][k][2];
 u31 = u[i][j][k][2] / u[i][j][k][0];
 q = 0.50 * ( u[i][j][k][1] * u[i][j][k][1]
        + u[i][j][k][2] * u[i][j][k][2]
        + u[i][j][k][3] * u[i][j][k][3] )
   / u[i][j][k][0];
 flux[i][j][k][1] = u[i][j][k][1] * u31;
 flux[i][j][k][2] = u[i][j][k][2] * u31 + 0.40e+00 * (u[i][j][k][4]-q);
 flux[i][j][k][3] = u[i][j][k][3] * u31;
 flux[i][j][k][4] = ( 1.40e+00 * u[i][j][k][4] - 0.40e+00 * q ) * u31;
      }
    }
  }
        
#pragma omp for
  for (i = ist; i <= iend; i++) {
    for (k = 1; k <= nz - 2; k++) {
      for (j = jst; j <= jend; j++) {
 for (m = 0; m < 5; m++) {
   rsd[i][j][k][m] = rsd[i][j][k][m]
     - ty2 * ( flux[i][j+1][k][m] - flux[i][j-1][k][m] );
 }
      }
      L2 = ny-1;
      for (j = jst; j <= L2; j++) {
 tmp = 1.0 / u[i][j][k][0];
 u21j = tmp * u[i][j][k][1];
 u31j = tmp * u[i][j][k][2];
 u41j = tmp * u[i][j][k][3];
 u51j = tmp * u[i][j][k][4];
 tmp = 1.0 / u[i][j-1][k][0];
 u21jm1 = tmp * u[i][j-1][k][1];
 u31jm1 = tmp * u[i][j-1][k][2];
 u41jm1 = tmp * u[i][j-1][k][3];
 u51jm1 = tmp * u[i][j-1][k][4];
 flux[i][j][k][1] = ty3 * ( u21j - u21jm1 );
 flux[i][j][k][2] = (4.0/3.0) * ty3 * (u31j-u31jm1);
 flux[i][j][k][3] = ty3 * ( u41j - u41jm1 );
 flux[i][j][k][4] = 0.50 * ( 1.0 - 1.40e+00*1.40e+00 )
   * ty3 * ( ( ((u21j)*(u21j)) + ((u31j)*(u31j)) + ((u41j)*(u41j)) )
      - ( ((u21jm1)*(u21jm1)) + ((u31jm1)*(u31jm1)) + ((u41jm1)*(u41jm1)) ) )
   + (1.0/6.0)
   * ty3 * ( ((u31j)*(u31j)) - ((u31jm1)*(u31jm1)) )
   + 1.40e+00 * 1.40e+00 * ty3 * ( u51j - u51jm1 );
      }
      for (j = jst; j <= jend; j++) {
 rsd[i][j][k][0] = rsd[i][j][k][0]
   + dy1 * ty1 * ( u[i][j-1][k][0]
         - 2.0 * u[i][j][k][0]
         + u[i][j+1][k][0] );
 rsd[i][j][k][1] = rsd[i][j][k][1]
   + ty3 * 1.00e-01 * 1.00e+00 * ( flux[i][j+1][k][1] - flux[i][j][k][1] )
   + dy2 * ty1 * ( u[i][j-1][k][1]
         - 2.0 * u[i][j][k][1]
         + u[i][j+1][k][1] );
 rsd[i][j][k][2] = rsd[i][j][k][2]
   + ty3 * 1.00e-01 * 1.00e+00 * ( flux[i][j+1][k][2] - flux[i][j][k][2] )
   + dy3 * ty1 * ( u[i][j-1][k][2]
         - 2.0 * u[i][j][k][2]
         + u[i][j+1][k][2] );
 rsd[i][j][k][3] = rsd[i][j][k][3]
   + ty3 * 1.00e-01 * 1.00e+00 * ( flux[i][j+1][k][3] - flux[i][j][k][3] )
   + dy4 * ty1 * ( u[i][j-1][k][3]
         - 2.0 * u[i][j][k][3]
         + u[i][j+1][k][3] );
 rsd[i][j][k][4] = rsd[i][j][k][4]
   + ty3 * 1.00e-01 * 1.00e+00 * ( flux[i][j+1][k][4] - flux[i][j][k][4] )
   + dy5 * ty1 * ( u[i][j-1][k][4]
         - 2.0 * u[i][j][k][4]
         + u[i][j+1][k][4] );
      }
      for (m = 0; m < 5; m++) {
 rsd[i][1][k][m] = rsd[i][1][k][m]
   - dssp * ( + 5.0 * u[i][1][k][m]
       - 4.0 * u[i][2][k][m]
       + u[i][3][k][m] );
 rsd[i][2][k][m] = rsd[i][2][k][m]
   - dssp * ( - 4.0 * u[i][1][k][m]
       + 6.0 * u[i][2][k][m]
       - 4.0 * u[i][3][k][m]
       + u[i][4][k][m] );
      }
      jst1 = 3;
      jend1 = ny - 4;
      for (j = jst1; j <= jend1; j++) {
 for (m = 0; m < 5; m++) {
   rsd[i][j][k][m] = rsd[i][j][k][m]
     - dssp * ( u[i][j-2][k][m]
      - 4.0 * u[i][j-1][k][m]
      + 6.0 * u[i][j][k][m]
      - 4.0 * u[i][j+1][k][m]
      + u[i][j+2][k][m] );
 }
      }
      for (m = 0; m < 5; m++) {
 rsd[i][ny-3][k][m] = rsd[i][ny-3][k][m]
   - dssp * ( u[i][ny-5][k][m]
     - 4.0 * u[i][ny-4][k][m]
     + 6.0 * u[i][ny-3][k][m]
     - 4.0 * u[i][ny-2][k][m] );
 rsd[i][ny-2][k][m] = rsd[i][ny-2][k][m]
   - dssp * ( u[i][ny-4][k][m]
     - 4.0 * u[i][ny-3][k][m]
     + 5.0 * u[i][ny-2][k][m] );
      }
    }
  }
        
#pragma omp for
  for (i = ist; i <= iend; i++) {
    for (j = jst; j <= jend; j++) {
      for (k = 0; k <= nz-1; k++) {
 flux[i][j][k][0] = u[i][j][k][3];
 u41 = u[i][j][k][3] / u[i][j][k][0];
 q = 0.50 * ( u[i][j][k][1] * u[i][j][k][1]
        + u[i][j][k][2] * u[i][j][k][2]
        + u[i][j][k][3] * u[i][j][k][3] )
   / u[i][j][k][0];
 flux[i][j][k][1] = u[i][j][k][1] * u41;
 flux[i][j][k][2] = u[i][j][k][2] * u41;
 flux[i][j][k][3] = u[i][j][k][3] * u41 + 0.40e+00 * (u[i][j][k][4]-q);
 flux[i][j][k][4] = ( 1.40e+00 * u[i][j][k][4] - 0.40e+00 * q ) * u41;
      }
      for (k = 1; k <= nz - 2; k++) {
 for (m = 0; m < 5; m++) {
   rsd[i][j][k][m] = rsd[i][j][k][m]
     - tz2 * ( flux[i][j][k+1][m] - flux[i][j][k-1][m] );
 }
      }
      for (k = 1; k <= nz-1; k++) {
 tmp = 1.0 / u[i][j][k][0];
 u21k = tmp * u[i][j][k][1];
 u31k = tmp * u[i][j][k][2];
 u41k = tmp * u[i][j][k][3];
 u51k = tmp * u[i][j][k][4];
 tmp = 1.0 / u[i][j][k-1][0];
 u21km1 = tmp * u[i][j][k-1][1];
 u31km1 = tmp * u[i][j][k-1][2];
 u41km1 = tmp * u[i][j][k-1][3];
 u51km1 = tmp * u[i][j][k-1][4];
 flux[i][j][k][1] = tz3 * ( u21k - u21km1 );
 flux[i][j][k][2] = tz3 * ( u31k - u31km1 );
 flux[i][j][k][3] = (4.0/3.0) * tz3 * (u41k-u41km1);
 flux[i][j][k][4] = 0.50 * ( 1.0 - 1.40e+00*1.40e+00 )
   * tz3 * ( ( ((u21k)*(u21k)) + ((u31k)*(u31k)) + ((u41k)*(u41k)) )
      - ( ((u21km1)*(u21km1)) + ((u31km1)*(u31km1)) + ((u41km1)*(u41km1)) ) )
   + (1.0/6.0)
   * tz3 * ( ((u41k)*(u41k)) - ((u41km1)*(u41km1)) )
   + 1.40e+00 * 1.40e+00 * tz3 * ( u51k - u51km1 );
      }
      for (k = 1; k <= nz - 2; k++) {
 rsd[i][j][k][0] = rsd[i][j][k][0]
   + dz1 * tz1 * ( u[i][j][k-1][0]
         - 2.0 * u[i][j][k][0]
         + u[i][j][k+1][0] );
 rsd[i][j][k][1] = rsd[i][j][k][1]
   + tz3 * 1.00e-01 * 1.00e+00 * ( flux[i][j][k+1][1] - flux[i][j][k][1] )
   + dz2 * tz1 * ( u[i][j][k-1][1]
         - 2.0 * u[i][j][k][1]
         + u[i][j][k+1][1] );
 rsd[i][j][k][2] = rsd[i][j][k][2]
   + tz3 * 1.00e-01 * 1.00e+00 * ( flux[i][j][k+1][2] - flux[i][j][k][2] )
   + dz3 * tz1 * ( u[i][j][k-1][2]
         - 2.0 * u[i][j][k][2]
         + u[i][j][k+1][2] );
 rsd[i][j][k][3] = rsd[i][j][k][3]
   + tz3 * 1.00e-01 * 1.00e+00 * ( flux[i][j][k+1][3] - flux[i][j][k][3] )
   + dz4 * tz1 * ( u[i][j][k-1][3]
         - 2.0 * u[i][j][k][3]
         + u[i][j][k+1][3] );
 rsd[i][j][k][4] = rsd[i][j][k][4]
   + tz3 * 1.00e-01 * 1.00e+00 * ( flux[i][j][k+1][4] - flux[i][j][k][4] )
   + dz5 * tz1 * ( u[i][j][k-1][4]
         - 2.0 * u[i][j][k][4]
         + u[i][j][k+1][4] );
      }
      for (m = 0; m < 5; m++) {
 rsd[i][j][1][m] = rsd[i][j][1][m]
   - dssp * ( + 5.0 * u[i][j][1][m]
       - 4.0 * u[i][j][2][m]
       + u[i][j][3][m] );
 rsd[i][j][2][m] = rsd[i][j][2][m]
   - dssp * ( - 4.0 * u[i][j][1][m]
       + 6.0 * u[i][j][2][m]
       - 4.0 * u[i][j][3][m]
       + u[i][j][4][m] );
      }
      for (k = 3; k <= nz - 4; k++) {
 for (m = 0; m < 5; m++) {
   rsd[i][j][k][m] = rsd[i][j][k][m]
     - dssp * ( u[i][j][k-2][m]
      - 4.0 * u[i][j][k-1][m]
      + 6.0 * u[i][j][k][m]
      - 4.0 * u[i][j][k+1][m]
      + u[i][j][k+2][m] );
 }
      }
      for (m = 0; m < 5; m++) {
 rsd[i][j][nz-3][m] = rsd[i][j][nz-3][m]
   - dssp * ( u[i][j][nz-5][m]
     - 4.0 * u[i][j][nz-4][m]
     + 6.0 * u[i][j][nz-3][m]
     - 4.0 * u[i][j][nz-2][m] );
 rsd[i][j][nz-2][m] = rsd[i][j][nz-2][m]
   - dssp * ( u[i][j][nz-4][m]
     - 4.0 * u[i][j][nz-3][m]
     + 5.0 * u[i][j][nz-2][m] );
      }
    }
  }
}
}
static void setbv(void) {
        
#pragma omp parallel
{
  int i, j, k;
  int iglob, jglob;
        
#pragma omp for
  for (i = 0; i < nx; i++) {
    iglob = i;
    for (j = 0; j < ny; j++) {
      jglob = j;
      exact( iglob, jglob, 0, &u[i][j][0][0] );
      exact( iglob, jglob, nz-1, &u[i][j][nz-1][0] );
    }
  }
        
#pragma omp for
  for (i = 0; i < nx; i++) {
    iglob = i;
    for (k = 0; k < nz; k++) {
      exact( iglob, 0, k, &u[i][0][k][0] );
    }
  }
        
#pragma omp for
  for (i = 0; i < nx; i++) {
    iglob = i;
    for (k = 0; k < nz; k++) {
      exact( iglob, ny0-1, k, &u[i][ny-1][k][0] );
    }
  }
        
#pragma omp for
  for (j = 0; j < ny; j++) {
    jglob = j;
    for (k = 0; k < nz; k++) {
      exact( 0, jglob, k, &u[0][j][k][0] );
    }
  }
        
#pragma omp for
  for (j = 0; j < ny; j++) {
    jglob = j;
    for (k = 0; k < nz; k++) {
      exact( nx0-1, jglob, k, &u[nx-1][j][k][0] );
    }
  }
}
}
static void setcoeff(void) {
  dxi = 1.0 / ( nx0 - 1 );
  deta = 1.0 / ( ny0 - 1 );
  dzeta = 1.0 / ( nz0 - 1 );
  tx1 = 1.0 / ( dxi * dxi );
  tx2 = 1.0 / ( 2.0 * dxi );
  tx3 = 1.0 / dxi;
  ty1 = 1.0 / ( deta * deta );
  ty2 = 1.0 / ( 2.0 * deta );
  ty3 = 1.0 / deta;
  tz1 = 1.0 / ( dzeta * dzeta );
  tz2 = 1.0 / ( 2.0 * dzeta );
  tz3 = 1.0 / dzeta;
  ii1 = 1;
  ii2 = nx0 - 2;
  ji1 = 1;
  ji2 = ny0 - 3;
  ki1 = 2;
  ki2 = nz0 - 2;
  dx1 = 0.75;
  dx2 = dx1;
  dx3 = dx1;
  dx4 = dx1;
  dx5 = dx1;
  dy1 = 0.75;
  dy2 = dy1;
  dy3 = dy1;
  dy4 = dy1;
  dy5 = dy1;
  dz1 = 1.00;
  dz2 = dz1;
  dz3 = dz1;
  dz4 = dz1;
  dz5 = dz1;
  dssp = ( (((dx1) > ((((dy1) > (dz1)) ? (dy1) : (dz1)))) ? (dx1) : ((((dy1) > (dz1)) ? (dy1) : (dz1)))) ) / 4.0;
  ce[0][0] = 2.0;
  ce[0][1] = 0.0;
  ce[0][2] = 0.0;
  ce[0][3] = 4.0;
  ce[0][4] = 5.0;
  ce[0][5] = 3.0;
  ce[0][6] = 5.0e-01;
  ce[0][7] = 2.0e-02;
  ce[0][8] = 1.0e-02;
  ce[0][9] = 3.0e-02;
  ce[0][10] = 5.0e-01;
  ce[0][11] = 4.0e-01;
  ce[0][12] = 3.0e-01;
  ce[1][0] = 1.0;
  ce[1][1] = 0.0;
  ce[1][2] = 0.0;
  ce[1][3] = 0.0;
  ce[1][4] = 1.0;
  ce[1][5] = 2.0;
  ce[1][6] = 3.0;
  ce[1][7] = 1.0e-02;
  ce[1][8] = 3.0e-02;
  ce[1][9] = 2.0e-02;
  ce[1][10] = 4.0e-01;
  ce[1][11] = 3.0e-01;
  ce[1][12] = 5.0e-01;
  ce[2][0] = 2.0;
  ce[2][1] = 2.0;
  ce[2][2] = 0.0;
  ce[2][3] = 0.0;
  ce[2][4] = 0.0;
  ce[2][5] = 2.0;
  ce[2][6] = 3.0;
  ce[2][7] = 4.0e-02;
  ce[2][8] = 3.0e-02;
  ce[2][9] = 5.0e-02;
  ce[2][10] = 3.0e-01;
  ce[2][11] = 5.0e-01;
  ce[2][12] = 4.0e-01;
  ce[3][0] = 2.0;
  ce[3][1] = 2.0;
  ce[3][2] = 0.0;
  ce[3][3] = 0.0;
  ce[3][4] = 0.0;
  ce[3][5] = 2.0;
  ce[3][6] = 3.0;
  ce[3][7] = 3.0e-02;
  ce[3][8] = 5.0e-02;
  ce[3][9] = 4.0e-02;
  ce[3][10] = 2.0e-01;
  ce[3][11] = 1.0e-01;
  ce[3][12] = 3.0e-01;
  ce[4][0] = 5.0;
  ce[4][1] = 4.0;
  ce[4][2] = 3.0;
  ce[4][3] = 2.0;
  ce[4][4] = 1.0e-01;
  ce[4][5] = 4.0e-01;
  ce[4][6] = 3.0e-01;
  ce[4][7] = 5.0e-02;
  ce[4][8] = 4.0e-02;
  ce[4][9] = 3.0e-02;
  ce[4][10] = 1.0e-01;
  ce[4][11] = 3.0e-01;
  ce[4][12] = 2.0e-01;
}
static void setiv(void) {
        
#pragma omp parallel
{
  int i, j, k, m;
  int iglob, jglob;
  double xi, eta, zeta;
  double pxi, peta, pzeta;
  double ue_1jk[5],ue_nx0jk[5],ue_i1k[5],
    ue_iny0k[5],ue_ij1[5],ue_ijnz[5];
        
#pragma omp for
  for (j = 0; j < ny; j++) {
    jglob = j;
    for (k = 1; k < nz - 1; k++) {
      zeta = ((double)k) / (nz-1);
      if (jglob != 0 && jglob != ny0-1) {
 eta = ( (double) (jglob) ) / (ny0-1);
 for (i = 0; i < nx; i++) {
   iglob = i;
   if(iglob != 0 && iglob != nx0-1) {
     xi = ( (double) (iglob) ) / (nx0-1);
     exact (0,jglob,k,ue_1jk);
     exact (nx0-1,jglob,k,ue_nx0jk);
     exact (iglob,0,k,ue_i1k);
     exact (iglob,ny0-1,k,ue_iny0k);
     exact (iglob,jglob,0,ue_ij1);
     exact (iglob,jglob,nz-1,ue_ijnz);
     for (m = 0; m < 5; m++) {
       pxi = ( 1.0 - xi ) * ue_1jk[m]
  + xi * ue_nx0jk[m];
       peta = ( 1.0 - eta ) * ue_i1k[m]
  + eta * ue_iny0k[m];
       pzeta = ( 1.0 - zeta ) * ue_ij1[m]
  + zeta * ue_ijnz[m];
       u[i][j][k][m] = pxi + peta + pzeta
  - pxi * peta - peta * pzeta - pzeta * pxi
  + pxi * peta * pzeta;
     }
   }
 }
      }
    }
  }
}
}
static void ssor(void) {
  int i, j, k, m;
  int istep;
  double tmp;
  double delunm[5], tv[162][162][5];
  tmp = 1.0 / ( omega * ( 2.0 - omega ) ) ;
        
#pragma omp parallel private(i,j,k,m)
{
        
#pragma omp for
  for (i = 0; i < 162; i++) {
    for (j = 0; j < 162; j++) {
      for (k = 0; k < 5; k++) {
 for (m = 0; m < 5; m++) {
   a[i][j][k][m] = 0.0;
   b[i][j][k][m] = 0.0;
   c[i][j][k][m] = 0.0;
   d[i][j][k][m] = 0.0;
 }
      }
    }
  }
}
  rhs();
  l2norm( nx0, ny0, nz0,
   ist, iend, jst, jend,
   rsd, rsdnm );
  timer_clear(1);
  timer_start(1);
  for (istep = 1; istep <= itmax; istep++) {
    if (istep%20 == 0 || istep == itmax || istep == 1) {
        
#pragma omp master
      printf(" Time step %4d\n", istep);
    }
        
#pragma omp parallel private(istep,i,j,k,m)
{
        
#pragma omp for
    for (i = ist; i <= iend; i++) {
      for (j = jst; j <= jend; j++) {
 for (k = 1; k <= nz - 2; k++) {
   for (m = 0; m < 5; m++) {
     rsd[i][j][k][m] = dt * rsd[i][j][k][m];
   }
 }
      }
    }
    for (k = 1; k <= nz - 2; k++) {
      jacld(k);
      blts(nx, ny, nz, k,
    omega,
    rsd,
    a, b, c, d,
    ist, iend, jst, jend,
    nx0, ny0 );
    }
        
#pragma omp barrier
    for (k = nz - 2; k >= 1; k--) {
      jacu(k);
      buts(nx, ny, nz, k,
    omega,
    rsd, tv,
    d, a, b, c,
    ist, iend, jst, jend,
    nx0, ny0 );
    }
        
#pragma omp barrier
        
#pragma omp for
    for (i = ist; i <= iend; i++) {
      for (j = jst; j <= jend; j++) {
 for (k = 1; k <= nz-2; k++) {
   for (m = 0; m < 5; m++) {
     u[i][j][k][m] = u[i][j][k][m]
       + tmp * rsd[i][j][k][m];
   }
 }
      }
    }
}
    if ( istep % inorm == 0 ) {
      l2norm( nx0, ny0, nz0,
       ist, iend, jst, jend,
       rsd, delunm );
    }
    rhs();
    if ( ( istep % inorm == 0 ) ||
  ( istep == itmax ) ) {
      l2norm( nx0, ny0, nz0,
       ist, iend, jst, jend,
       rsd, rsdnm );
    }
    if ( ( rsdnm[0] < tolrsd[0] ) &&
  ( rsdnm[1] < tolrsd[1] ) &&
  ( rsdnm[2] < tolrsd[2] ) &&
  ( rsdnm[3] < tolrsd[3] ) &&
  ( rsdnm[4] < tolrsd[4] ) ) {
 exit(1);
    }
  }
  timer_stop(1);
  maxtime= timer_read(1);
}
static void verify(double xcr[5], double xce[5], double xci,
     char *class, boolean *verified) {
  double xcrref[5],xceref[5],xciref,
    xcrdif[5],xcedif[5],xcidif,
    epsilon, dtref;
  int m;
  epsilon = 1.0e-08;
  *class = 'U';
  *verified = 1;
  for (m = 0; m < 5; m++) {
    xcrref[m] = 1.0;
    xceref[m] = 1.0;
  }
  xciref = 1.0;
  if ( nx0 == 12 && ny0 == 12 && nz0 == 12 && itmax == 50) {
    *class = 'S';
    dtref = 5.0e-1;
    xcrref[0] = 1.6196343210976702e-02;
    xcrref[1] = 2.1976745164821318e-03;
    xcrref[2] = 1.5179927653399185e-03;
    xcrref[3] = 1.5029584435994323e-03;
    xcrref[4] = 3.4264073155896461e-02;
    xceref[0] = 6.4223319957960924e-04;
    xceref[1] = 8.4144342047347926e-05;
    xceref[2] = 5.8588269616485186e-05;
    xceref[3] = 5.8474222595157350e-05;
    xceref[4] = 1.3103347914111294e-03;
    xciref = 7.8418928865937083;
  } else if ( nx0 == 33 && ny0 == 33 && nz0 == 33 && itmax == 300) {
    *class = 'W';
    dtref = 1.5e-3;
    xcrref[0] = 0.1236511638192e+02;
    xcrref[1] = 0.1317228477799e+01;
    xcrref[2] = 0.2550120713095e+01;
    xcrref[3] = 0.2326187750252e+01;
    xcrref[4] = 0.2826799444189e+02;
    xceref[0] = 0.4867877144216;
    xceref[1] = 0.5064652880982e-01;
    xceref[2] = 0.9281818101960e-01;
    xceref[3] = 0.8570126542733e-01;
    xceref[4] = 0.1084277417792e+01;
    xciref = 0.1161399311023e+02;
  } else if ( nx0 == 64 && ny0 == 64 && nz0 == 64 && itmax == 250) {
    *class = 'A';
    dtref = 2.0e+0;
    xcrref[0] = 7.7902107606689367e+02;
    xcrref[1] = 6.3402765259692870e+01;
    xcrref[2] = 1.9499249727292479e+02;
    xcrref[3] = 1.7845301160418537e+02;
    xcrref[4] = 1.8384760349464247e+03;
    xceref[0] = 2.9964085685471943e+01;
    xceref[1] = 2.8194576365003349;
    xceref[2] = 7.3473412698774742;
    xceref[3] = 6.7139225687777051;
    xceref[4] = 7.0715315688392578e+01;
    xciref = 2.6030925604886277e+01;
    } else if ( nx0 == 102 && ny0 == 102 && nz0 == 102 && itmax == 250) {
      *class = 'B';
      dtref = 2.0e+0;
      xcrref[0] = 3.5532672969982736e+03;
      xcrref[1] = 2.6214750795310692e+02;
      xcrref[2] = 8.8333721850952190e+02;
      xcrref[3] = 7.7812774739425265e+02;
      xcrref[4] = 7.3087969592545314e+03;
      xceref[0] = 1.1401176380212709e+02;
      xceref[1] = 8.1098963655421574;
      xceref[2] = 2.8480597317698308e+01;
      xceref[3] = 2.5905394567832939e+01;
      xceref[4] = 2.6054907504857413e+02;
      xciref = 4.7887162703308227e+01;
      } else if ( nx0 == 162 && ny0 == 162 && nz0 == 162 && itmax == 250) {
 *class = 'C';
 dtref = 2.0e+0;
 xcrref[0] = 1.03766980323537846e+04;
 xcrref[1] = 8.92212458801008552e+02;
 xcrref[2] = 2.56238814582660871e+03;
 xcrref[3] = 2.19194343857831427e+03;
 xcrref[4] = 1.78078057261061185e+04;
 xceref[0] = 2.15986399716949279e+02;
 xceref[1] = 1.55789559239863600e+01;
 xceref[2] = 5.41318863077207766e+01;
 xceref[3] = 4.82262643154045421e+01;
 xceref[4] = 4.55902910043250358e+02;
 xciref = 6.66404553572181300e+01;
      } else {
 *verified = 0;
      }
  for (m = 0; m < 5; m++) {
    xcrdif[m] = fabs((xcr[m]-xcrref[m])/xcrref[m]);
    xcedif[m] = fabs((xce[m]-xceref[m])/xceref[m]);
  }
  xcidif = fabs((xci - xciref)/xciref);
  if (*class != 'U') {
    printf("\n Verification being performed for class %1c\n", *class);
    printf(" Accuracy setting for epsilon = %20.13e\n", epsilon);
    if (fabs(dt-dtref) > epsilon) {
      *verified = 0;
      *class = 'U';
      printf(" DT does not match the reference value of %15.8e\n", dtref);
    }
  } else {
    printf(" Unknown class\n");
  }
  if (*class != 'U') {
    printf(" Comparison of RMS-norms of residual\n");
  } else {
    printf(" RMS-norms of residual\n");
  }
  for (m = 0; m < 5; m++) {
    if (*class == 'U') {
      printf("          %2d  %20.13e\n", m, xcr[m]);
    } else if (xcrdif[m] > epsilon) {
      *verified = 0;
      printf(" FAILURE: %2d  %20.13e%20.13e%20.13e\n",
      m,xcr[m],xcrref[m],xcrdif[m]);
    } else {
      printf("          %2d  %20.13e%20.13e%20.13e\n",
      m,xcr[m],xcrref[m],xcrdif[m]);
    }
  }
  if (*class != 'U') {
    printf(" Comparison of RMS-norms of solution error\n");
  } else {
    printf(" RMS-norms of solution error\n");
  }
  for (m = 0; m < 5; m++) {
    if (*class == 'U') {
      printf("          %2d  %20.13e\n", m, xce[m]);
    } else if (xcedif[m] > epsilon) {
      *verified = 0;
      printf(" FAILURE: %2d  %20.13e%20.13e%20.13e\n",
      m,xce[m],xceref[m],xcedif[m]);
    } else {
      printf("          %2d  %20.13e%20.13e%20.13e\n",
      m,xce[m],xceref[m],xcedif[m]);
    }
  }
  if (*class != 'U') {
    printf(" Comparison of surface integral\n");
  } else {
    printf(" Surface integral\n");
  }
  if (*class == 'U') {
    printf("              %20.13e\n", xci);
  } else if (xcidif > epsilon) {
    *verified = 0;
    printf(" FAILURE:     %20.13e%20.13e%20.13e\n",
    xci, xciref, xcidif);
  } else {
    printf("              %20.13e%20.13e%20.13e\n",
    xci, xciref, xcidif);
  }
  if (*class == 'U') {
    printf(" No reference values provided\n");
    printf(" No verification performed\n");
  } else if (*verified) {
    printf(" Verification Successful\n");
  } else {
    printf(" Verification failed\n");
  }
}
