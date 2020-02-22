
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
static int grid_points[3];
static double tx1, tx2, tx3, ty1, ty2, ty3, tz1, tz2, tz3;
static double dx1, dx2, dx3, dx4, dx5;
static double dy1, dy2, dy3, dy4, dy5;
static double dz1, dz2, dz3, dz4, dz5;
static double dssp, dt;
static double ce[5][13];
static double dxmax, dymax, dzmax;
static double xxcon1, xxcon2, xxcon3, xxcon4, xxcon5;
static double dx1tx1, dx2tx1, dx3tx1, dx4tx1, dx5tx1;
static double yycon1, yycon2, yycon3, yycon4, yycon5;
static double dy1ty1, dy2ty1, dy3ty1, dy4ty1, dy5ty1;
static double zzcon1, zzcon2, zzcon3, zzcon4, zzcon5;
static double dz1tz1, dz2tz1, dz3tz1, dz4tz1, dz5tz1;
static double dnxm1, dnym1, dnzm1, c1c2, c1c5, c3c4, c1345;
static double conz1, c1, c2, c3, c4, c5, c4dssp, c5dssp, dtdssp;
static double dttx1, dttx2, dtty1, dtty2, dttz1, dttz2;
static double c2dttx1, c2dtty1, c2dttz1, comz1, comz4, comz5, comz6;
static double c3c4tx3, c3c4ty3, c3c4tz3, c2iv, con43, con16;
static double us[102/2*2+1][102/2*2+1][102/2*2+1];
static double vs[102/2*2+1][102/2*2+1][102/2*2+1];
static double ws[102/2*2+1][102/2*2+1][102/2*2+1];
static double qs[102/2*2+1][102/2*2+1][102/2*2+1];
static double rho_i[102/2*2+1][102/2*2+1][102/2*2+1];
static double square[102/2*2+1][102/2*2+1][102/2*2+1];
static double forcing[102/2*2+1][102/2*2+1][102/2*2+1][5+1];
static double u[(102 +1)/2*2+1][(102 +1)/2*2+1][(102 +1)/2*2+1][5];
static double rhs[102/2*2+1][102/2*2+1][102/2*2+1][5];
static double lhs[102/2*2+1][102/2*2+1][102/2*2+1][3][5][5];
static double cuf[102];
static double q[102];
static double ue[102][5];
static double buf[102][5];
        
#pragma omp threadprivate(cuf, q, ue, buf)
static double fjac[102/2*2+1][102/2*2+1][102 -1+1][5][5];
static double njac[102/2*2+1][102/2*2+1][102 -1+1][5][5];
static double tmp1, tmp2, tmp3;
static void add(void);
static void adi(void);
static void error_norm(double rms[5]);
static void rhs_norm(double rms[5]);
static void exact_rhs(void);
static void exact_solution(double xi, double eta, double zeta,
      double dtemp[5]);
static void initialize(void);
static void lhsinit(void);
static void lhsx(void);
static void lhsy(void);
static void lhsz(void);
static void compute_rhs(void);
static void set_constants(void);
static void verify(int no_time_steps, char *class, boolean *verified);
static void x_solve(void);
static void x_backsubstitute(void);
static void x_solve_cell(void);
static void matvec_sub(double ablock[5][5], double avec[5], double bvec[5]);
static void matmul_sub(double ablock[5][5], double bblock[5][5],
         double cblock[5][5]);
static void binvcrhs(double lhs[5][5], double c[5][5], double r[5]);
static void binvrhs(double lhs[5][5], double r[5]);
static void y_solve(void);
static void y_backsubstitute(void);
static void y_solve_cell(void);
static void z_solve(void);
static void z_backsubstitute(void);
static void z_solve_cell(void);
int main(int argc, char **argv) {
  int niter, step, n3;
  int nthreads = 1;
  double navg, mflops;
  double tmax;
  boolean verified;
  char class;
  FILE *fp;
  printf("\n\n NAS Parallel Benchmarks 3.0 structured OpenMP C version"
  " - BT Benchmark\n\n");
  fp = fopen("inputbt.data", "r");
  if (fp != ((void *)0)) {
    printf(" Reading from input file inputbt.data");
    fscanf(fp, "%d", &niter);
    while (fgetc(fp) != '\n');
    fscanf(fp, "%lg", &dt);
    while (fgetc(fp) != '\n');
    fscanf(fp, "%d%d%d",
    &grid_points[0], &grid_points[1], &grid_points[2]);
    fclose(fp);
  } else {
    printf(" No input file inputbt.data. Using compiled defaults\n");
    niter = 200;
    dt = 0.0003;
    grid_points[0] = 102;
    grid_points[1] = 102;
    grid_points[2] = 102;
  }
  printf(" Size: %3dx%3dx%3d\n",
  grid_points[0], grid_points[1], grid_points[2]);
  printf(" Iterations: %3d   dt: %10.6f\n", niter, dt);
  if (grid_points[0] > 102 ||
      grid_points[1] > 102 ||
      grid_points[2] > 102) {
    printf(" %dx%dx%d\n", grid_points[0], grid_points[1], grid_points[2]);
    printf(" Problem size too big for compiled array sizes\n");
    exit(1);
  }
  set_constants();
  initialize();
  lhsinit();
  exact_rhs();
  adi();
  initialize();
  timer_clear(1);
  timer_start(1);
  for (step = 1; step <= niter; step++) {
    if (step%20 == 0 || step == 1) {
      printf(" Time step %4d\n", step);
    }
    adi();
  }
        
#pragma omp parallel
{
        
#pragma omp master
  nthreads = omp_get_num_threads();
}
  timer_stop(1);
  tmax = timer_read(1);
  verify(niter, &class, &verified);
  n3 = grid_points[0]*grid_points[1]*grid_points[2];
  navg = (grid_points[0]+grid_points[1]+grid_points[2])/3.0;
  if ( tmax != 0.0 ) {
    mflops = 1.0e-6*(double)niter*
 (3478.8*(double)n3-17655.7*((navg)*(navg))+28023.7*navg) / tmax;
  } else {
    mflops = 0.0;
  }
  c_print_results("BT", class, grid_points[0],
    grid_points[1], grid_points[2], niter, nthreads,
    tmax, mflops, "          floating point",
    verified, "3.0 structured","30 Nov 2019", "gcc", "gcc", "-lm", "-I../common", "-O3 -fopenmp",
    "-O3 -fopenmp -mcmodel=large -fPIC", "(none)");
}
static void add(void) {
  int i, j, k, m;
        
#pragma omp for
  for (i = 1; i < grid_points[0]-1; i++) {
    for (j = 1; j < grid_points[1]-1; j++) {
      for (k = 1; k < grid_points[2]-1; k++) {
 for (m = 0; m < 5; m++) {
   u[i][j][k][m] = u[i][j][k][m] + rhs[i][j][k][m];
 }
      }
    }
  }
}
static void adi(void) {
        
#pragma omp parallel
    compute_rhs();
        
#pragma omp parallel
    x_solve();
        
#pragma omp parallel
    y_solve();
        
#pragma omp parallel
    z_solve();
        
#pragma omp parallel
    add();
}
static void error_norm(double rms[5]) {
  int i, j, k, m, d;
  double xi, eta, zeta, u_exact[5], add;
  for (m = 0; m < 5; m++) {
    rms[m] = 0.0;
  }
  for (i = 0; i < grid_points[0]; i++) {
    xi = (double)i * dnxm1;
    for (j = 0; j < grid_points[1]; j++) {
      eta = (double)j * dnym1;
      for (k = 0; k < grid_points[2]; k++) {
 zeta = (double)k * dnzm1;
 exact_solution(xi, eta, zeta, u_exact);
 for (m = 0; m < 5; m++) {
   add = u[i][j][k][m] - u_exact[m];
   rms[m] = rms[m] + add*add;
 }
      }
    }
  }
  for (m = 0; m < 5; m++) {
    for (d = 0; d <= 2; d++) {
      rms[m] = rms[m] / (double)(grid_points[d]-2);
    }
    rms[m] = sqrt(rms[m]);
  }
}
static void rhs_norm(double rms[5]) {
  int i, j, k, d, m;
  double add;
  for (m = 0; m < 5; m++) {
    rms[m] = 0.0;
  }
  for (i = 1; i < grid_points[0]-1; i++) {
    for (j = 1; j < grid_points[1]-1; j++) {
      for (k = 1; k < grid_points[2]-1; k++) {
 for (m = 0; m < 5; m++) {
   add = rhs[i][j][k][m];
   rms[m] = rms[m] + add*add;
 }
      }
    }
  }
  for (m = 0; m < 5; m++) {
    for (d = 0; d <= 2; d++) {
      rms[m] = rms[m] / (double)(grid_points[d]-2);
    }
    rms[m] = sqrt(rms[m]);
  }
}
static void exact_rhs(void) {
        
#pragma omp parallel
{
  double dtemp[5], xi, eta, zeta, dtpp;
  int m, i, j, k, ip1, im1, jp1, jm1, km1, kp1;
        
#pragma omp for
  for (i = 0; i < grid_points[0]; i++) {
    for (j = 0; j < grid_points[1]; j++) {
      for (k = 0; k < grid_points[2]; k++) {
 for (m = 0; m < 5; m++) {
   forcing[i][j][k][m] = 0.0;
 }
      }
    }
  }
        
#pragma omp for
  for (j = 1; j < grid_points[1]-1; j++) {
    eta = (double)j * dnym1;
    for (k = 1; k < grid_points[2]-1; k++) {
      zeta = (double)k * dnzm1;
      for (i = 0; i < grid_points[0]; i++) {
 xi = (double)i * dnxm1;
 exact_solution(xi, eta, zeta, dtemp);
 for (m = 0; m < 5; m++) {
   ue[i][m] = dtemp[m];
 }
 dtpp = 1.0 / dtemp[0];
 for (m = 1; m <= 4; m++) {
   buf[i][m] = dtpp * dtemp[m];
 }
 cuf[i] = buf[i][1] * buf[i][1];
 buf[i][0] = cuf[i] + buf[i][2] * buf[i][2] +
   buf[i][3] * buf[i][3];
 q[i] = 0.5*(buf[i][1]*ue[i][1] + buf[i][2]*ue[i][2] +
      buf[i][3]*ue[i][3]);
      }
      for (i = 1; i < grid_points[0]-1; i++) {
 im1 = i-1;
 ip1 = i+1;
 forcing[i][j][k][0] = forcing[i][j][k][0] -
   tx2*(ue[ip1][1]-ue[im1][1])+
   dx1tx1*(ue[ip1][0]-2.0*ue[i][0]+ue[im1][0]);
 forcing[i][j][k][1] = forcing[i][j][k][1] -
   tx2 * ((ue[ip1][1]*buf[ip1][1]+c2*(ue[ip1][4]-q[ip1]))-
   (ue[im1][1]*buf[im1][1]+c2*(ue[im1][4]-q[im1])))+
   xxcon1*(buf[ip1][1]-2.0*buf[i][1]+buf[im1][1])+
   dx2tx1*( ue[ip1][1]-2.0* ue[i][1]+ ue[im1][1]);
 forcing[i][j][k][2] = forcing[i][j][k][2] -
   tx2 * (ue[ip1][2]*buf[ip1][1]-ue[im1][2]*buf[im1][1])+
   xxcon2*(buf[ip1][2]-2.0*buf[i][2]+buf[im1][2])+
   dx3tx1*( ue[ip1][2]-2.0* ue[i][2]+ ue[im1][2]);
 forcing[i][j][k][3] = forcing[i][j][k][3] -
   tx2*(ue[ip1][3]*buf[ip1][1]-ue[im1][3]*buf[im1][1])+
   xxcon2*(buf[ip1][3]-2.0*buf[i][3]+buf[im1][3])+
   dx4tx1*( ue[ip1][3]-2.0* ue[i][3]+ ue[im1][3]);
 forcing[i][j][k][4] = forcing[i][j][k][4] -
   tx2*(buf[ip1][1]*(c1*ue[ip1][4]-c2*q[ip1])-
        buf[im1][1]*(c1*ue[im1][4]-c2*q[im1]))+
   0.5*xxcon3*(buf[ip1][0]-2.0*buf[i][0]+buf[im1][0])+
   xxcon4*(cuf[ip1]-2.0*cuf[i]+cuf[im1])+
   xxcon5*(buf[ip1][4]-2.0*buf[i][4]+buf[im1][4])+
   dx5tx1*( ue[ip1][4]-2.0* ue[i][4]+ ue[im1][4]);
      }
      for (m = 0; m < 5; m++) {
 i = 1;
 forcing[i][j][k][m] = forcing[i][j][k][m] - dssp *
   (5.0*ue[i][m] - 4.0*ue[i+1][m] +ue[i+2][m]);
 i = 2;
 forcing[i][j][k][m] = forcing[i][j][k][m] - dssp *
   (-4.0*ue[i-1][m] + 6.0*ue[i][m] -
     4.0*ue[i+1][m] + ue[i+2][m]);
      }
      for (m = 0; m < 5; m++) {
 for (i = 1*3; i <= grid_points[0]-3*1-1; i++) {
   forcing[i][j][k][m] = forcing[i][j][k][m] - dssp*
     (ue[i-2][m] - 4.0*ue[i-1][m] +
      6.0*ue[i][m] - 4.0*ue[i+1][m] + ue[i+2][m]);
 }
      }
      for (m = 0; m < 5; m++) {
 i = grid_points[0]-3;
 forcing[i][j][k][m] = forcing[i][j][k][m] - dssp *
   (ue[i-2][m] - 4.0*ue[i-1][m] +
    6.0*ue[i][m] - 4.0*ue[i+1][m]);
 i = grid_points[0]-2;
 forcing[i][j][k][m] = forcing[i][j][k][m] - dssp *
   (ue[i-2][m] - 4.0*ue[i-1][m] + 5.0*ue[i][m]);
      }
    }
  }
        
#pragma omp for
  for (i = 1; i < grid_points[0]-1; i++) {
    xi = (double)i * dnxm1;
    for (k = 1; k < grid_points[2]-1; k++) {
      zeta = (double)k * dnzm1;
      for (j = 0; j < grid_points[1]; j++) {
 eta = (double)j * dnym1;
 exact_solution(xi, eta, zeta, dtemp);
 for (m = 0; m < 5; m++) {
   ue[j][m] = dtemp[m];
 }
 dtpp = 1.0/dtemp[0];
 for (m = 1; m <= 4; m++) {
   buf[j][m] = dtpp * dtemp[m];
 }
 cuf[j] = buf[j][2] * buf[j][2];
 buf[j][0] = cuf[j] + buf[j][1] * buf[j][1] +
   buf[j][3] * buf[j][3];
 q[j] = 0.5*(buf[j][1]*ue[j][1] + buf[j][2]*ue[j][2] +
      buf[j][3]*ue[j][3]);
      }
      for (j = 1; j < grid_points[1]-1; j++) {
 jm1 = j-1;
 jp1 = j+1;
 forcing[i][j][k][0] = forcing[i][j][k][0] -
   ty2*( ue[jp1][2]-ue[jm1][2] )+
   dy1ty1*(ue[jp1][0]-2.0*ue[j][0]+ue[jm1][0]);
 forcing[i][j][k][1] = forcing[i][j][k][1] -
   ty2*(ue[jp1][1]*buf[jp1][2]-ue[jm1][1]*buf[jm1][2])+
   yycon2*(buf[jp1][1]-2.0*buf[j][1]+buf[jm1][1])+
   dy2ty1*( ue[jp1][1]-2.0* ue[j][1]+ ue[jm1][1]);
 forcing[i][j][k][2] = forcing[i][j][k][2] -
   ty2*((ue[jp1][2]*buf[jp1][2]+c2*(ue[jp1][4]-q[jp1]))-
        (ue[jm1][2]*buf[jm1][2]+c2*(ue[jm1][4]-q[jm1])))+
   yycon1*(buf[jp1][2]-2.0*buf[j][2]+buf[jm1][2])+
   dy3ty1*( ue[jp1][2]-2.0*ue[j][2] +ue[jm1][2]);
 forcing[i][j][k][3] = forcing[i][j][k][3] -
   ty2*(ue[jp1][3]*buf[jp1][2]-ue[jm1][3]*buf[jm1][2])+
   yycon2*(buf[jp1][3]-2.0*buf[j][3]+buf[jm1][3])+
   dy4ty1*( ue[jp1][3]-2.0*ue[j][3]+ ue[jm1][3]);
 forcing[i][j][k][4] = forcing[i][j][k][4] -
   ty2*(buf[jp1][2]*(c1*ue[jp1][4]-c2*q[jp1])-
        buf[jm1][2]*(c1*ue[jm1][4]-c2*q[jm1]))+
   0.5*yycon3*(buf[jp1][0]-2.0*buf[j][0]+
                      buf[jm1][0])+
   yycon4*(cuf[jp1]-2.0*cuf[j]+cuf[jm1])+
   yycon5*(buf[jp1][4]-2.0*buf[j][4]+buf[jm1][4])+
   dy5ty1*(ue[jp1][4]-2.0*ue[j][4]+ue[jm1][4]);
      }
      for (m = 0; m < 5; m++) {
 j = 1;
 forcing[i][j][k][m] = forcing[i][j][k][m] - dssp *
   (5.0*ue[j][m] - 4.0*ue[j+1][m] +ue[j+2][m]);
 j = 2;
 forcing[i][j][k][m] = forcing[i][j][k][m] - dssp *
   (-4.0*ue[j-1][m] + 6.0*ue[j][m] -
    4.0*ue[j+1][m] + ue[j+2][m]);
      }
      for (m = 0; m < 5; m++) {
 for (j = 1*3; j <= grid_points[1]-3*1-1; j++) {
   forcing[i][j][k][m] = forcing[i][j][k][m] - dssp*
     (ue[j-2][m] - 4.0*ue[j-1][m] +
      6.0*ue[j][m] - 4.0*ue[j+1][m] + ue[j+2][m]);
 }
      }
      for (m = 0; m < 5; m++) {
 j = grid_points[1]-3;
 forcing[i][j][k][m] = forcing[i][j][k][m] - dssp *
   (ue[j-2][m] - 4.0*ue[j-1][m] +
    6.0*ue[j][m] - 4.0*ue[j+1][m]);
 j = grid_points[1]-2;
 forcing[i][j][k][m] = forcing[i][j][k][m] - dssp *
   (ue[j-2][m] - 4.0*ue[j-1][m] + 5.0*ue[j][m]);
      }
    }
  }
        
#pragma omp for
  for (i = 1; i < grid_points[0]-1; i++) {
    xi = (double)i * dnxm1;
    for (j = 1; j < grid_points[1]-1; j++) {
      eta = (double)j * dnym1;
      for (k = 0; k < grid_points[2]; k++) {
 zeta = (double)k * dnzm1;
 exact_solution(xi, eta, zeta, dtemp);
 for (m = 0; m < 5; m++) {
   ue[k][m] = dtemp[m];
 }
 dtpp = 1.0/dtemp[0];
 for (m = 1; m <= 4; m++) {
   buf[k][m] = dtpp * dtemp[m];
 }
 cuf[k] = buf[k][3] * buf[k][3];
 buf[k][0] = cuf[k] + buf[k][1] * buf[k][1] +
   buf[k][2] * buf[k][2];
 q[k] = 0.5*(buf[k][1]*ue[k][1] + buf[k][2]*ue[k][2] +
      buf[k][3]*ue[k][3]);
      }
      for (k = 1; k < grid_points[2]-1; k++) {
 km1 = k-1;
 kp1 = k+1;
 forcing[i][j][k][0] = forcing[i][j][k][0] -
   tz2*( ue[kp1][3]-ue[km1][3] )+
   dz1tz1*(ue[kp1][0]-2.0*ue[k][0]+ue[km1][0]);
 forcing[i][j][k][1] = forcing[i][j][k][1] -
   tz2 * (ue[kp1][1]*buf[kp1][3]-ue[km1][1]*buf[km1][3])+
   zzcon2*(buf[kp1][1]-2.0*buf[k][1]+buf[km1][1])+
   dz2tz1*( ue[kp1][1]-2.0* ue[k][1]+ ue[km1][1]);
 forcing[i][j][k][2] = forcing[i][j][k][2] -
   tz2 * (ue[kp1][2]*buf[kp1][3]-ue[km1][2]*buf[km1][3])+
   zzcon2*(buf[kp1][2]-2.0*buf[k][2]+buf[km1][2])+
   dz3tz1*(ue[kp1][2]-2.0*ue[k][2]+ue[km1][2]);
 forcing[i][j][k][3] = forcing[i][j][k][3] -
   tz2 * ((ue[kp1][3]*buf[kp1][3]+c2*(ue[kp1][4]-q[kp1]))-
   (ue[km1][3]*buf[km1][3]+c2*(ue[km1][4]-q[km1])))+
   zzcon1*(buf[kp1][3]-2.0*buf[k][3]+buf[km1][3])+
   dz4tz1*( ue[kp1][3]-2.0*ue[k][3] +ue[km1][3]);
 forcing[i][j][k][4] = forcing[i][j][k][4] -
   tz2 * (buf[kp1][3]*(c1*ue[kp1][4]-c2*q[kp1])-
   buf[km1][3]*(c1*ue[km1][4]-c2*q[km1]))+
   0.5*zzcon3*(buf[kp1][0]-2.0*buf[k][0]
                      +buf[km1][0])+
   zzcon4*(cuf[kp1]-2.0*cuf[k]+cuf[km1])+
   zzcon5*(buf[kp1][4]-2.0*buf[k][4]+buf[km1][4])+
   dz5tz1*( ue[kp1][4]-2.0*ue[k][4]+ ue[km1][4]);
      }
      for (m = 0; m < 5; m++) {
 k = 1;
 forcing[i][j][k][m] = forcing[i][j][k][m] - dssp *
   (5.0*ue[k][m] - 4.0*ue[k+1][m] +ue[k+2][m]);
 k = 2;
 forcing[i][j][k][m] = forcing[i][j][k][m] - dssp *
   (-4.0*ue[k-1][m] + 6.0*ue[k][m] -
    4.0*ue[k+1][m] + ue[k+2][m]);
      }
      for (m = 0; m < 5; m++) {
 for (k = 1*3; k <= grid_points[2]-3*1-1; k++) {
   forcing[i][j][k][m] = forcing[i][j][k][m] - dssp*
     (ue[k-2][m] - 4.0*ue[k-1][m] +
      6.0*ue[k][m] - 4.0*ue[k+1][m] + ue[k+2][m]);
 }
      }
      for (m = 0; m < 5; m++) {
 k = grid_points[2]-3;
 forcing[i][j][k][m] = forcing[i][j][k][m] - dssp *
   (ue[k-2][m] - 4.0*ue[k-1][m] +
    6.0*ue[k][m] - 4.0*ue[k+1][m]);
 k = grid_points[2]-2;
 forcing[i][j][k][m] = forcing[i][j][k][m] - dssp *
   (ue[k-2][m] - 4.0*ue[k-1][m] + 5.0*ue[k][m]);
      }
    }
  }
        
#pragma omp for
  for (i = 1; i < grid_points[0]-1; i++) {
    for (j = 1; j < grid_points[1]-1; j++) {
      for (k = 1; k < grid_points[2]-1; k++) {
 for (m = 0; m < 5; m++) {
   forcing[i][j][k][m] = -1.0 * forcing[i][j][k][m];
 }
      }
    }
  }
}
}
static void exact_solution(double xi, double eta, double zeta,
      double dtemp[5]) {
  int m;
  for (m = 0; m < 5; m++) {
    dtemp[m] = ce[m][0] +
      xi*(ce[m][1] + xi*(ce[m][4] + xi*(ce[m][7]
     + xi*ce[m][10]))) +
      eta*(ce[m][2] + eta*(ce[m][5] + eta*(ce[m][8]
        + eta*ce[m][11])))+
      zeta*(ce[m][3] + zeta*(ce[m][6] + zeta*(ce[m][9] +
           zeta*ce[m][12])));
  }
}
static void initialize(void) {
        
#pragma omp parallel
{
  int i, j, k, m, ix, iy, iz;
  double xi, eta, zeta, Pface[2][3][5], Pxi, Peta, Pzeta, temp[5];
        
#pragma omp for
  for (i = 0; i < 102; i++) {
    for (j = 0; j < 102; j++) {
      for (k = 0; k < 102; k++) {
 for (m = 0; m < 5; m++) {
   u[i][j][k][m] = 1.0;
 }
      }
    }
  }
        
#pragma omp for
  for (i = 0; i < grid_points[0]; i++) {
    xi = (double)i * dnxm1;
    for (j = 0; j < grid_points[1]; j++) {
      eta = (double)j * dnym1;
      for (k = 0; k < grid_points[2]; k++) {
 zeta = (double)k * dnzm1;
 for (ix = 0; ix < 2; ix++) {
   exact_solution((double)ix, eta, zeta,
                         &(Pface[ix][0][0]));
 }
 for (iy = 0; iy < 2; iy++) {
   exact_solution(xi, (double)iy , zeta,
                         &Pface[iy][1][0]);
 }
 for (iz = 0; iz < 2; iz++) {
   exact_solution(xi, eta, (double)iz,
                         &Pface[iz][2][0]);
 }
 for (m = 0; m < 5; m++) {
   Pxi = xi * Pface[1][0][m] +
     (1.0-xi) * Pface[0][0][m];
   Peta = eta * Pface[1][1][m] +
     (1.0-eta) * Pface[0][1][m];
   Pzeta = zeta * Pface[1][2][m] +
     (1.0-zeta) * Pface[0][2][m];
   u[i][j][k][m] = Pxi + Peta + Pzeta -
     Pxi*Peta - Pxi*Pzeta - Peta*Pzeta +
     Pxi*Peta*Pzeta;
 }
      }
    }
  }
  i = 0;
  xi = 0.0;
        
#pragma omp for nowait
  for (j = 0; j < grid_points[1]; j++) {
    eta = (double)j * dnym1;
    for (k = 0; k < grid_points[2]; k++) {
      zeta = (double)k * dnzm1;
      exact_solution(xi, eta, zeta, temp);
      for (m = 0; m < 5; m++) {
 u[i][j][k][m] = temp[m];
      }
    }
  }
  i = grid_points[0]-1;
  xi = 1.0;
        
#pragma omp for
  for (j = 0; j < grid_points[1]; j++) {
    eta = (double)j * dnym1;
    for (k = 0; k < grid_points[2]; k++) {
      zeta = (double)k * dnzm1;
      exact_solution(xi, eta, zeta, temp);
      for (m = 0; m < 5; m++) {
 u[i][j][k][m] = temp[m];
      }
    }
  }
  j = 0;
  eta = 0.0;
        
#pragma omp for nowait
  for (i = 0; i < grid_points[0]; i++) {
    xi = (double)i * dnxm1;
    for (k = 0; k < grid_points[2]; k++) {
      zeta = (double)k * dnzm1;
      exact_solution(xi, eta, zeta, temp);
      for (m = 0; m < 5; m++) {
 u[i][j][k][m] = temp[m];
      }
    }
  }
  j = grid_points[1]-1;
  eta = 1.0;
        
#pragma omp for
  for (i = 0; i < grid_points[0]; i++) {
    xi = (double)i * dnxm1;
    for (k = 0; k < grid_points[2]; k++) {
      zeta = (double)k * dnzm1;
      exact_solution(xi, eta, zeta, temp);
      for (m = 0; m < 5; m++) {
 u[i][j][k][m] = temp[m];
      }
    }
  }
  k = 0;
  zeta = 0.0;
        
#pragma omp for nowait
  for (i = 0; i < grid_points[0]; i++) {
    xi = (double)i *dnxm1;
    for (j = 0; j < grid_points[1]; j++) {
      eta = (double)j * dnym1;
      exact_solution(xi, eta, zeta, temp);
      for (m = 0; m < 5; m++) {
 u[i][j][k][m] = temp[m];
      }
    }
  }
  k = grid_points[2]-1;
  zeta = 1.0;
        
#pragma omp for
  for (i = 0; i < grid_points[0]; i++) {
    xi = (double)i * dnxm1;
    for (j = 0; j < grid_points[1]; j++) {
      eta = (double)j * dnym1;
      exact_solution(xi, eta, zeta, temp);
      for (m = 0; m < 5; m++) {
 u[i][j][k][m] = temp[m];
      }
    }
  }
}
}
static void lhsinit(void) {
        
#pragma omp parallel
{
  int i, j, k, m, n;
        
#pragma omp for
  for (i = 0; i < grid_points[0]; i++) {
    for (j = 0; j < grid_points[1]; j++) {
      for (k = 0; k < grid_points[2]; k++) {
 for (m = 0; m < 5; m++) {
   for (n = 0; n < 5; n++) {
     lhs[i][j][k][0][m][n] = 0.0;
     lhs[i][j][k][1][m][n] = 0.0;
     lhs[i][j][k][2][m][n] = 0.0;
   }
 }
      }
    }
  }
        
#pragma omp for
  for (i = 0; i < grid_points[0]; i++) {
    for (j = 0; j < grid_points[1]; j++) {
      for (k = 0; k < grid_points[2]; k++) {
 for (m = 0; m < 5; m++) {
   lhs[i][j][k][1][m][m] = 1.0;
 }
      }
    }
  }
}
}
static void lhsx(void) {
  int i, j, k;
        
#pragma omp for
  for (j = 1; j < grid_points[1]-1; j++) {
    for (k = 1; k < grid_points[2]-1; k++) {
      for (i = 0; i < grid_points[0]; i++) {
 tmp1 = 1.0 / u[i][j][k][0];
 tmp2 = tmp1 * tmp1;
 tmp3 = tmp1 * tmp2;
 fjac[ i][ j][ k][0][0] = 0.0;
 fjac[ i][ j][ k][0][1] = 1.0;
 fjac[ i][ j][ k][0][2] = 0.0;
 fjac[ i][ j][ k][0][3] = 0.0;
 fjac[ i][ j][ k][0][4] = 0.0;
 fjac[ i][ j][ k][1][0] = -(u[i][j][k][1] * tmp2 *
        u[i][j][k][1])
   + c2 * 0.50 * (u[i][j][k][1] * u[i][j][k][1]
         + u[i][j][k][2] * u[i][j][k][2]
         + u[i][j][k][3] * u[i][j][k][3] ) * tmp2;
 fjac[i][j][k][1][1] = ( 2.0 - c2 )
   * ( u[i][j][k][1] / u[i][j][k][0] );
 fjac[i][j][k][1][2] = - c2 * ( u[i][j][k][2] * tmp1 );
 fjac[i][j][k][1][3] = - c2 * ( u[i][j][k][3] * tmp1 );
 fjac[i][j][k][1][4] = c2;
 fjac[i][j][k][2][0] = - ( u[i][j][k][1]*u[i][j][k][2] ) * tmp2;
 fjac[i][j][k][2][1] = u[i][j][k][2] * tmp1;
 fjac[i][j][k][2][2] = u[i][j][k][1] * tmp1;
 fjac[i][j][k][2][3] = 0.0;
 fjac[i][j][k][2][4] = 0.0;
 fjac[i][j][k][3][0] = - ( u[i][j][k][1]*u[i][j][k][3] ) * tmp2;
 fjac[i][j][k][3][1] = u[i][j][k][3] * tmp1;
 fjac[i][j][k][3][2] = 0.0;
 fjac[i][j][k][3][3] = u[i][j][k][1] * tmp1;
 fjac[i][j][k][3][4] = 0.0;
 fjac[i][j][k][4][0] = ( c2 * ( u[i][j][k][1] * u[i][j][k][1]
         + u[i][j][k][2] * u[i][j][k][2]
         + u[i][j][k][3] * u[i][j][k][3] ) * tmp2
    - c1 * ( u[i][j][k][4] * tmp1 ) )
   * ( u[i][j][k][1] * tmp1 );
 fjac[i][j][k][4][1] = c1 * u[i][j][k][4] * tmp1
   - 0.50 * c2
   * ( 3.0*u[i][j][k][1]*u[i][j][k][1]
        + u[i][j][k][2]*u[i][j][k][2]
        + u[i][j][k][3]*u[i][j][k][3] ) * tmp2;
 fjac[i][j][k][4][2] = - c2 * ( u[i][j][k][2]*u[i][j][k][1] )
   * tmp2;
 fjac[i][j][k][4][3] = - c2 * ( u[i][j][k][3]*u[i][j][k][1] )
   * tmp2;
 fjac[i][j][k][4][4] = c1 * ( u[i][j][k][1] * tmp1 );
 njac[i][j][k][0][0] = 0.0;
 njac[i][j][k][0][1] = 0.0;
 njac[i][j][k][0][2] = 0.0;
 njac[i][j][k][0][3] = 0.0;
 njac[i][j][k][0][4] = 0.0;
 njac[i][j][k][1][0] = - con43 * c3c4 * tmp2 * u[i][j][k][1];
 njac[i][j][k][1][1] = con43 * c3c4 * tmp1;
 njac[i][j][k][1][2] = 0.0;
 njac[i][j][k][1][3] = 0.0;
 njac[i][j][k][1][4] = 0.0;
 njac[i][j][k][2][0] = - c3c4 * tmp2 * u[i][j][k][2];
 njac[i][j][k][2][1] = 0.0;
 njac[i][j][k][2][2] = c3c4 * tmp1;
 njac[i][j][k][2][3] = 0.0;
 njac[i][j][k][2][4] = 0.0;
 njac[i][j][k][3][0] = - c3c4 * tmp2 * u[i][j][k][3];
 njac[i][j][k][3][1] = 0.0;
 njac[i][j][k][3][2] = 0.0;
 njac[i][j][k][3][3] = c3c4 * tmp1;
 njac[i][j][k][3][4] = 0.0;
 njac[i][j][k][4][0] = - ( con43 * c3c4
   - c1345 ) * tmp3 * (((u[i][j][k][1])*(u[i][j][k][1])))
   - ( c3c4 - c1345 ) * tmp3 * (((u[i][j][k][2])*(u[i][j][k][2])))
   - ( c3c4 - c1345 ) * tmp3 * (((u[i][j][k][3])*(u[i][j][k][3])))
   - c1345 * tmp2 * u[i][j][k][4];
 njac[i][j][k][4][1] = ( con43 * c3c4
    - c1345 ) * tmp2 * u[i][j][k][1];
 njac[i][j][k][4][2] = ( c3c4 - c1345 ) * tmp2 * u[i][j][k][2];
 njac[i][j][k][4][3] = ( c3c4 - c1345 ) * tmp2 * u[i][j][k][3];
 njac[i][j][k][4][4] = ( c1345 ) * tmp1;
      }
      for (i = 1; i < grid_points[0]-1; i++) {
 tmp1 = dt * tx1;
 tmp2 = dt * tx2;
 lhs[i][j][k][0][0][0] = - tmp2 * fjac[i-1][j][k][0][0]
   - tmp1 * njac[i-1][j][k][0][0]
   - tmp1 * dx1;
 lhs[i][j][k][0][0][1] = - tmp2 * fjac[i-1][j][k][0][1]
   - tmp1 * njac[i-1][j][k][0][1];
 lhs[i][j][k][0][0][2] = - tmp2 * fjac[i-1][j][k][0][2]
   - tmp1 * njac[i-1][j][k][0][2];
 lhs[i][j][k][0][0][3] = - tmp2 * fjac[i-1][j][k][0][3]
   - tmp1 * njac[i-1][j][k][0][3];
 lhs[i][j][k][0][0][4] = - tmp2 * fjac[i-1][j][k][0][4]
   - tmp1 * njac[i-1][j][k][0][4];
 lhs[i][j][k][0][1][0] = - tmp2 * fjac[i-1][j][k][1][0]
   - tmp1 * njac[i-1][j][k][1][0];
 lhs[i][j][k][0][1][1] = - tmp2 * fjac[i-1][j][k][1][1]
   - tmp1 * njac[i-1][j][k][1][1]
   - tmp1 * dx2;
 lhs[i][j][k][0][1][2] = - tmp2 * fjac[i-1][j][k][1][2]
   - tmp1 * njac[i-1][j][k][1][2];
 lhs[i][j][k][0][1][3] = - tmp2 * fjac[i-1][j][k][1][3]
   - tmp1 * njac[i-1][j][k][1][3];
 lhs[i][j][k][0][1][4] = - tmp2 * fjac[i-1][j][k][1][4]
   - tmp1 * njac[i-1][j][k][1][4];
 lhs[i][j][k][0][2][0] = - tmp2 * fjac[i-1][j][k][2][0]
   - tmp1 * njac[i-1][j][k][2][0];
 lhs[i][j][k][0][2][1] = - tmp2 * fjac[i-1][j][k][2][1]
   - tmp1 * njac[i-1][j][k][2][1];
 lhs[i][j][k][0][2][2] = - tmp2 * fjac[i-1][j][k][2][2]
   - tmp1 * njac[i-1][j][k][2][2]
   - tmp1 * dx3;
 lhs[i][j][k][0][2][3] = - tmp2 * fjac[i-1][j][k][2][3]
   - tmp1 * njac[i-1][j][k][2][3];
 lhs[i][j][k][0][2][4] = - tmp2 * fjac[i-1][j][k][2][4]
   - tmp1 * njac[i-1][j][k][2][4];
 lhs[i][j][k][0][3][0] = - tmp2 * fjac[i-1][j][k][3][0]
   - tmp1 * njac[i-1][j][k][3][0];
 lhs[i][j][k][0][3][1] = - tmp2 * fjac[i-1][j][k][3][1]
   - tmp1 * njac[i-1][j][k][3][1];
 lhs[i][j][k][0][3][2] = - tmp2 * fjac[i-1][j][k][3][2]
   - tmp1 * njac[i-1][j][k][3][2];
 lhs[i][j][k][0][3][3] = - tmp2 * fjac[i-1][j][k][3][3]
   - tmp1 * njac[i-1][j][k][3][3]
   - tmp1 * dx4;
 lhs[i][j][k][0][3][4] = - tmp2 * fjac[i-1][j][k][3][4]
   - tmp1 * njac[i-1][j][k][3][4];
 lhs[i][j][k][0][4][0] = - tmp2 * fjac[i-1][j][k][4][0]
   - tmp1 * njac[i-1][j][k][4][0];
 lhs[i][j][k][0][4][1] = - tmp2 * fjac[i-1][j][k][4][1]
   - tmp1 * njac[i-1][j][k][4][1];
 lhs[i][j][k][0][4][2] = - tmp2 * fjac[i-1][j][k][4][2]
   - tmp1 * njac[i-1][j][k][4][2];
 lhs[i][j][k][0][4][3] = - tmp2 * fjac[i-1][j][k][4][3]
   - tmp1 * njac[i-1][j][k][4][3];
 lhs[i][j][k][0][4][4] = - tmp2 * fjac[i-1][j][k][4][4]
   - tmp1 * njac[i-1][j][k][4][4]
   - tmp1 * dx5;
 lhs[i][j][k][1][0][0] = 1.0
   + tmp1 * 2.0 * njac[i][j][k][0][0]
   + tmp1 * 2.0 * dx1;
 lhs[i][j][k][1][0][1] = tmp1 * 2.0 * njac[i][j][k][0][1];
 lhs[i][j][k][1][0][2] = tmp1 * 2.0 * njac[i][j][k][0][2];
 lhs[i][j][k][1][0][3] = tmp1 * 2.0 * njac[i][j][k][0][3];
 lhs[i][j][k][1][0][4] = tmp1 * 2.0 * njac[i][j][k][0][4];
 lhs[i][j][k][1][1][0] = tmp1 * 2.0 * njac[i][j][k][1][0];
 lhs[i][j][k][1][1][1] = 1.0
   + tmp1 * 2.0 * njac[i][j][k][1][1]
   + tmp1 * 2.0 * dx2;
 lhs[i][j][k][1][1][2] = tmp1 * 2.0 * njac[i][j][k][1][2];
 lhs[i][j][k][1][1][3] = tmp1 * 2.0 * njac[i][j][k][1][3];
 lhs[i][j][k][1][1][4] = tmp1 * 2.0 * njac[i][j][k][1][4];
 lhs[i][j][k][1][2][0] = tmp1 * 2.0 * njac[i][j][k][2][0];
 lhs[i][j][k][1][2][1] = tmp1 * 2.0 * njac[i][j][k][2][1];
 lhs[i][j][k][1][2][2] = 1.0
   + tmp1 * 2.0 * njac[i][j][k][2][2]
   + tmp1 * 2.0 * dx3;
 lhs[i][j][k][1][2][3] = tmp1 * 2.0 * njac[i][j][k][2][3];
 lhs[i][j][k][1][2][4] = tmp1 * 2.0 * njac[i][j][k][2][4];
 lhs[i][j][k][1][3][0] = tmp1 * 2.0 * njac[i][j][k][3][0];
 lhs[i][j][k][1][3][1] = tmp1 * 2.0 * njac[i][j][k][3][1];
 lhs[i][j][k][1][3][2] = tmp1 * 2.0 * njac[i][j][k][3][2];
 lhs[i][j][k][1][3][3] = 1.0
   + tmp1 * 2.0 * njac[i][j][k][3][3]
   + tmp1 * 2.0 * dx4;
 lhs[i][j][k][1][3][4] = tmp1 * 2.0 * njac[i][j][k][3][4];
 lhs[i][j][k][1][4][0] = tmp1 * 2.0 * njac[i][j][k][4][0];
 lhs[i][j][k][1][4][1] = tmp1 * 2.0 * njac[i][j][k][4][1];
 lhs[i][j][k][1][4][2] = tmp1 * 2.0 * njac[i][j][k][4][2];
 lhs[i][j][k][1][4][3] = tmp1 * 2.0 * njac[i][j][k][4][3];
 lhs[i][j][k][1][4][4] = 1.0
   + tmp1 * 2.0 * njac[i][j][k][4][4]
   + tmp1 * 2.0 * dx5;
 lhs[i][j][k][2][0][0] = tmp2 * fjac[i+1][j][k][0][0]
   - tmp1 * njac[i+1][j][k][0][0]
   - tmp1 * dx1;
 lhs[i][j][k][2][0][1] = tmp2 * fjac[i+1][j][k][0][1]
   - tmp1 * njac[i+1][j][k][0][1];
 lhs[i][j][k][2][0][2] = tmp2 * fjac[i+1][j][k][0][2]
   - tmp1 * njac[i+1][j][k][0][2];
 lhs[i][j][k][2][0][3] = tmp2 * fjac[i+1][j][k][0][3]
   - tmp1 * njac[i+1][j][k][0][3];
 lhs[i][j][k][2][0][4] = tmp2 * fjac[i+1][j][k][0][4]
   - tmp1 * njac[i+1][j][k][0][4];
 lhs[i][j][k][2][1][0] = tmp2 * fjac[i+1][j][k][1][0]
   - tmp1 * njac[i+1][j][k][1][0];
 lhs[i][j][k][2][1][1] = tmp2 * fjac[i+1][j][k][1][1]
   - tmp1 * njac[i+1][j][k][1][1]
   - tmp1 * dx2;
 lhs[i][j][k][2][1][2] = tmp2 * fjac[i+1][j][k][1][2]
   - tmp1 * njac[i+1][j][k][1][2];
 lhs[i][j][k][2][1][3] = tmp2 * fjac[i+1][j][k][1][3]
   - tmp1 * njac[i+1][j][k][1][3];
 lhs[i][j][k][2][1][4] = tmp2 * fjac[i+1][j][k][1][4]
   - tmp1 * njac[i+1][j][k][1][4];
 lhs[i][j][k][2][2][0] = tmp2 * fjac[i+1][j][k][2][0]
   - tmp1 * njac[i+1][j][k][2][0];
 lhs[i][j][k][2][2][1] = tmp2 * fjac[i+1][j][k][2][1]
   - tmp1 * njac[i+1][j][k][2][1];
 lhs[i][j][k][2][2][2] = tmp2 * fjac[i+1][j][k][2][2]
   - tmp1 * njac[i+1][j][k][2][2]
   - tmp1 * dx3;
 lhs[i][j][k][2][2][3] = tmp2 * fjac[i+1][j][k][2][3]
   - tmp1 * njac[i+1][j][k][2][3];
 lhs[i][j][k][2][2][4] = tmp2 * fjac[i+1][j][k][2][4]
   - tmp1 * njac[i+1][j][k][2][4];
 lhs[i][j][k][2][3][0] = tmp2 * fjac[i+1][j][k][3][0]
   - tmp1 * njac[i+1][j][k][3][0];
 lhs[i][j][k][2][3][1] = tmp2 * fjac[i+1][j][k][3][1]
   - tmp1 * njac[i+1][j][k][3][1];
 lhs[i][j][k][2][3][2] = tmp2 * fjac[i+1][j][k][3][2]
   - tmp1 * njac[i+1][j][k][3][2];
 lhs[i][j][k][2][3][3] = tmp2 * fjac[i+1][j][k][3][3]
   - tmp1 * njac[i+1][j][k][3][3]
   - tmp1 * dx4;
 lhs[i][j][k][2][3][4] = tmp2 * fjac[i+1][j][k][3][4]
   - tmp1 * njac[i+1][j][k][3][4];
 lhs[i][j][k][2][4][0] = tmp2 * fjac[i+1][j][k][4][0]
   - tmp1 * njac[i+1][j][k][4][0];
 lhs[i][j][k][2][4][1] = tmp2 * fjac[i+1][j][k][4][1]
   - tmp1 * njac[i+1][j][k][4][1];
 lhs[i][j][k][2][4][2] = tmp2 * fjac[i+1][j][k][4][2]
   - tmp1 * njac[i+1][j][k][4][2];
 lhs[i][j][k][2][4][3] = tmp2 * fjac[i+1][j][k][4][3]
   - tmp1 * njac[i+1][j][k][4][3];
 lhs[i][j][k][2][4][4] = tmp2 * fjac[i+1][j][k][4][4]
   - tmp1 * njac[i+1][j][k][4][4]
   - tmp1 * dx5;
      }
    }
  }
}
static void lhsy(void) {
  int i, j, k;
        
#pragma omp for
  for (i = 1; i < grid_points[0]-1; i++) {
    for (j = 0; j < grid_points[1]; j++) {
      for (k = 1; k < grid_points[2]-1; k++) {
 tmp1 = 1.0 / u[i][j][k][0];
 tmp2 = tmp1 * tmp1;
 tmp3 = tmp1 * tmp2;
 fjac[ i][ j][ k][0][0] = 0.0;
 fjac[ i][ j][ k][0][1] = 0.0;
 fjac[ i][ j][ k][0][2] = 1.0;
 fjac[ i][ j][ k][0][3] = 0.0;
 fjac[ i][ j][ k][0][4] = 0.0;
 fjac[i][j][k][1][0] = - ( u[i][j][k][1]*u[i][j][k][2] )
   * tmp2;
 fjac[i][j][k][1][1] = u[i][j][k][2] * tmp1;
 fjac[i][j][k][1][2] = u[i][j][k][1] * tmp1;
 fjac[i][j][k][1][3] = 0.0;
 fjac[i][j][k][1][4] = 0.0;
 fjac[i][j][k][2][0] = - ( u[i][j][k][2]*u[i][j][k][2]*tmp2)
   + 0.50 * c2 * ( ( u[i][j][k][1] * u[i][j][k][1]
        + u[i][j][k][2] * u[i][j][k][2]
        + u[i][j][k][3] * u[i][j][k][3] )
     * tmp2 );
 fjac[i][j][k][2][1] = - c2 * u[i][j][k][1] * tmp1;
 fjac[i][j][k][2][2] = ( 2.0 - c2 )
   * u[i][j][k][2] * tmp1;
 fjac[i][j][k][2][3] = - c2 * u[i][j][k][3] * tmp1;
 fjac[i][j][k][2][4] = c2;
 fjac[i][j][k][3][0] = - ( u[i][j][k][2]*u[i][j][k][3] )
   * tmp2;
 fjac[i][j][k][3][1] = 0.0;
 fjac[i][j][k][3][2] = u[i][j][k][3] * tmp1;
 fjac[i][j][k][3][3] = u[i][j][k][2] * tmp1;
 fjac[i][j][k][3][4] = 0.0;
 fjac[i][j][k][4][0] = ( c2 * ( u[i][j][k][1] * u[i][j][k][1]
     + u[i][j][k][2] * u[i][j][k][2]
     + u[i][j][k][3] * u[i][j][k][3] )
    * tmp2
    - c1 * u[i][j][k][4] * tmp1 )
   * u[i][j][k][2] * tmp1;
 fjac[i][j][k][4][1] = - c2 * u[i][j][k][1]*u[i][j][k][2]
   * tmp2;
 fjac[i][j][k][4][2] = c1 * u[i][j][k][4] * tmp1
   - 0.50 * c2
   * ( ( u[i][j][k][1]*u[i][j][k][1]
   + 3.0 * u[i][j][k][2]*u[i][j][k][2]
   + u[i][j][k][3]*u[i][j][k][3] )
       * tmp2 );
 fjac[i][j][k][4][3] = - c2 * ( u[i][j][k][2]*u[i][j][k][3] )
   * tmp2;
 fjac[i][j][k][4][4] = c1 * u[i][j][k][2] * tmp1;
 njac[i][j][k][0][0] = 0.0;
 njac[i][j][k][0][1] = 0.0;
 njac[i][j][k][0][2] = 0.0;
 njac[i][j][k][0][3] = 0.0;
 njac[i][j][k][0][4] = 0.0;
 njac[i][j][k][1][0] = - c3c4 * tmp2 * u[i][j][k][1];
 njac[i][j][k][1][1] = c3c4 * tmp1;
 njac[i][j][k][1][2] = 0.0;
 njac[i][j][k][1][3] = 0.0;
 njac[i][j][k][1][4] = 0.0;
 njac[i][j][k][2][0] = - con43 * c3c4 * tmp2 * u[i][j][k][2];
 njac[i][j][k][2][1] = 0.0;
 njac[i][j][k][2][2] = con43 * c3c4 * tmp1;
 njac[i][j][k][2][3] = 0.0;
 njac[i][j][k][2][4] = 0.0;
 njac[i][j][k][3][0] = - c3c4 * tmp2 * u[i][j][k][3];
 njac[i][j][k][3][1] = 0.0;
 njac[i][j][k][3][2] = 0.0;
 njac[i][j][k][3][3] = c3c4 * tmp1;
 njac[i][j][k][3][4] = 0.0;
 njac[i][j][k][4][0] = - ( c3c4
          - c1345 ) * tmp3 * (((u[i][j][k][1])*(u[i][j][k][1])))
   - ( con43 * c3c4
       - c1345 ) * tmp3 * (((u[i][j][k][2])*(u[i][j][k][2])))
   - ( c3c4 - c1345 ) * tmp3 * (((u[i][j][k][3])*(u[i][j][k][3])))
   - c1345 * tmp2 * u[i][j][k][4];
 njac[i][j][k][4][1] = ( c3c4 - c1345 ) * tmp2 * u[i][j][k][1];
 njac[i][j][k][4][2] = ( con43 * c3c4
    - c1345 ) * tmp2 * u[i][j][k][2];
 njac[i][j][k][4][3] = ( c3c4 - c1345 ) * tmp2 * u[i][j][k][3];
 njac[i][j][k][4][4] = ( c1345 ) * tmp1;
      }
    }
  }
        
#pragma omp for
  for (i = 1; i < grid_points[0]-1; i++) {
    for (j = 1; j < grid_points[1]-1; j++) {
      for (k = 1; k < grid_points[2]-1; k++) {
 tmp1 = dt * ty1;
 tmp2 = dt * ty2;
 lhs[i][j][k][0][0][0] = - tmp2 * fjac[i][j-1][k][0][0]
   - tmp1 * njac[i][j-1][k][0][0]
   - tmp1 * dy1;
 lhs[i][j][k][0][0][1] = - tmp2 * fjac[i][j-1][k][0][1]
   - tmp1 * njac[i][j-1][k][0][1];
 lhs[i][j][k][0][0][2] = - tmp2 * fjac[i][j-1][k][0][2]
   - tmp1 * njac[i][j-1][k][0][2];
 lhs[i][j][k][0][0][3] = - tmp2 * fjac[i][j-1][k][0][3]
   - tmp1 * njac[i][j-1][k][0][3];
 lhs[i][j][k][0][0][4] = - tmp2 * fjac[i][j-1][k][0][4]
   - tmp1 * njac[i][j-1][k][0][4];
 lhs[i][j][k][0][1][0] = - tmp2 * fjac[i][j-1][k][1][0]
   - tmp1 * njac[i][j-1][k][1][0];
 lhs[i][j][k][0][1][1] = - tmp2 * fjac[i][j-1][k][1][1]
   - tmp1 * njac[i][j-1][k][1][1]
   - tmp1 * dy2;
 lhs[i][j][k][0][1][2] = - tmp2 * fjac[i][j-1][k][1][2]
   - tmp1 * njac[i][j-1][k][1][2];
 lhs[i][j][k][0][1][3] = - tmp2 * fjac[i][j-1][k][1][3]
   - tmp1 * njac[i][j-1][k][1][3];
 lhs[i][j][k][0][1][4] = - tmp2 * fjac[i][j-1][k][1][4]
   - tmp1 * njac[i][j-1][k][1][4];
 lhs[i][j][k][0][2][0] = - tmp2 * fjac[i][j-1][k][2][0]
   - tmp1 * njac[i][j-1][k][2][0];
 lhs[i][j][k][0][2][1] = - tmp2 * fjac[i][j-1][k][2][1]
   - tmp1 * njac[i][j-1][k][2][1];
 lhs[i][j][k][0][2][2] = - tmp2 * fjac[i][j-1][k][2][2]
   - tmp1 * njac[i][j-1][k][2][2]
   - tmp1 * dy3;
 lhs[i][j][k][0][2][3] = - tmp2 * fjac[i][j-1][k][2][3]
   - tmp1 * njac[i][j-1][k][2][3];
 lhs[i][j][k][0][2][4] = - tmp2 * fjac[i][j-1][k][2][4]
   - tmp1 * njac[i][j-1][k][2][4];
 lhs[i][j][k][0][3][0] = - tmp2 * fjac[i][j-1][k][3][0]
   - tmp1 * njac[i][j-1][k][3][0];
 lhs[i][j][k][0][3][1] = - tmp2 * fjac[i][j-1][k][3][1]
   - tmp1 * njac[i][j-1][k][3][1];
 lhs[i][j][k][0][3][2] = - tmp2 * fjac[i][j-1][k][3][2]
   - tmp1 * njac[i][j-1][k][3][2];
 lhs[i][j][k][0][3][3] = - tmp2 * fjac[i][j-1][k][3][3]
   - tmp1 * njac[i][j-1][k][3][3]
   - tmp1 * dy4;
 lhs[i][j][k][0][3][4] = - tmp2 * fjac[i][j-1][k][3][4]
   - tmp1 * njac[i][j-1][k][3][4];
 lhs[i][j][k][0][4][0] = - tmp2 * fjac[i][j-1][k][4][0]
   - tmp1 * njac[i][j-1][k][4][0];
 lhs[i][j][k][0][4][1] = - tmp2 * fjac[i][j-1][k][4][1]
   - tmp1 * njac[i][j-1][k][4][1];
 lhs[i][j][k][0][4][2] = - tmp2 * fjac[i][j-1][k][4][2]
   - tmp1 * njac[i][j-1][k][4][2];
 lhs[i][j][k][0][4][3] = - tmp2 * fjac[i][j-1][k][4][3]
   - tmp1 * njac[i][j-1][k][4][3];
 lhs[i][j][k][0][4][4] = - tmp2 * fjac[i][j-1][k][4][4]
   - tmp1 * njac[i][j-1][k][4][4]
   - tmp1 * dy5;
 lhs[i][j][k][1][0][0] = 1.0
   + tmp1 * 2.0 * njac[i][j][k][0][0]
   + tmp1 * 2.0 * dy1;
 lhs[i][j][k][1][0][1] = tmp1 * 2.0 * njac[i][j][k][0][1];
 lhs[i][j][k][1][0][2] = tmp1 * 2.0 * njac[i][j][k][0][2];
 lhs[i][j][k][1][0][3] = tmp1 * 2.0 * njac[i][j][k][0][3];
 lhs[i][j][k][1][0][4] = tmp1 * 2.0 * njac[i][j][k][0][4];
 lhs[i][j][k][1][1][0] = tmp1 * 2.0 * njac[i][j][k][1][0];
 lhs[i][j][k][1][1][1] = 1.0
   + tmp1 * 2.0 * njac[i][j][k][1][1]
   + tmp1 * 2.0 * dy2;
 lhs[i][j][k][1][1][2] = tmp1 * 2.0 * njac[i][j][k][1][2];
 lhs[i][j][k][1][1][3] = tmp1 * 2.0 * njac[i][j][k][1][3];
 lhs[i][j][k][1][1][4] = tmp1 * 2.0 * njac[i][j][k][1][4];
 lhs[i][j][k][1][2][0] = tmp1 * 2.0 * njac[i][j][k][2][0];
 lhs[i][j][k][1][2][1] = tmp1 * 2.0 * njac[i][j][k][2][1];
 lhs[i][j][k][1][2][2] = 1.0
   + tmp1 * 2.0 * njac[i][j][k][2][2]
   + tmp1 * 2.0 * dy3;
 lhs[i][j][k][1][2][3] = tmp1 * 2.0 * njac[i][j][k][2][3];
 lhs[i][j][k][1][2][4] = tmp1 * 2.0 * njac[i][j][k][2][4];
 lhs[i][j][k][1][3][0] = tmp1 * 2.0 * njac[i][j][k][3][0];
 lhs[i][j][k][1][3][1] = tmp1 * 2.0 * njac[i][j][k][3][1];
 lhs[i][j][k][1][3][2] = tmp1 * 2.0 * njac[i][j][k][3][2];
 lhs[i][j][k][1][3][3] = 1.0
   + tmp1 * 2.0 * njac[i][j][k][3][3]
   + tmp1 * 2.0 * dy4;
 lhs[i][j][k][1][3][4] = tmp1 * 2.0 * njac[i][j][k][3][4];
 lhs[i][j][k][1][4][0] = tmp1 * 2.0 * njac[i][j][k][4][0];
 lhs[i][j][k][1][4][1] = tmp1 * 2.0 * njac[i][j][k][4][1];
 lhs[i][j][k][1][4][2] = tmp1 * 2.0 * njac[i][j][k][4][2];
 lhs[i][j][k][1][4][3] = tmp1 * 2.0 * njac[i][j][k][4][3];
 lhs[i][j][k][1][4][4] = 1.0
   + tmp1 * 2.0 * njac[i][j][k][4][4]
   + tmp1 * 2.0 * dy5;
 lhs[i][j][k][2][0][0] = tmp2 * fjac[i][j+1][k][0][0]
   - tmp1 * njac[i][j+1][k][0][0]
   - tmp1 * dy1;
 lhs[i][j][k][2][0][1] = tmp2 * fjac[i][j+1][k][0][1]
   - tmp1 * njac[i][j+1][k][0][1];
 lhs[i][j][k][2][0][2] = tmp2 * fjac[i][j+1][k][0][2]
   - tmp1 * njac[i][j+1][k][0][2];
 lhs[i][j][k][2][0][3] = tmp2 * fjac[i][j+1][k][0][3]
   - tmp1 * njac[i][j+1][k][0][3];
 lhs[i][j][k][2][0][4] = tmp2 * fjac[i][j+1][k][0][4]
   - tmp1 * njac[i][j+1][k][0][4];
 lhs[i][j][k][2][1][0] = tmp2 * fjac[i][j+1][k][1][0]
   - tmp1 * njac[i][j+1][k][1][0];
 lhs[i][j][k][2][1][1] = tmp2 * fjac[i][j+1][k][1][1]
   - tmp1 * njac[i][j+1][k][1][1]
   - tmp1 * dy2;
 lhs[i][j][k][2][1][2] = tmp2 * fjac[i][j+1][k][1][2]
   - tmp1 * njac[i][j+1][k][1][2];
 lhs[i][j][k][2][1][3] = tmp2 * fjac[i][j+1][k][1][3]
   - tmp1 * njac[i][j+1][k][1][3];
 lhs[i][j][k][2][1][4] = tmp2 * fjac[i][j+1][k][1][4]
   - tmp1 * njac[i][j+1][k][1][4];
 lhs[i][j][k][2][2][0] = tmp2 * fjac[i][j+1][k][2][0]
   - tmp1 * njac[i][j+1][k][2][0];
 lhs[i][j][k][2][2][1] = tmp2 * fjac[i][j+1][k][2][1]
   - tmp1 * njac[i][j+1][k][2][1];
 lhs[i][j][k][2][2][2] = tmp2 * fjac[i][j+1][k][2][2]
   - tmp1 * njac[i][j+1][k][2][2]
   - tmp1 * dy3;
 lhs[i][j][k][2][2][3] = tmp2 * fjac[i][j+1][k][2][3]
   - tmp1 * njac[i][j+1][k][2][3];
 lhs[i][j][k][2][2][4] = tmp2 * fjac[i][j+1][k][2][4]
   - tmp1 * njac[i][j+1][k][2][4];
 lhs[i][j][k][2][3][0] = tmp2 * fjac[i][j+1][k][3][0]
   - tmp1 * njac[i][j+1][k][3][0];
 lhs[i][j][k][2][3][1] = tmp2 * fjac[i][j+1][k][3][1]
   - tmp1 * njac[i][j+1][k][3][1];
 lhs[i][j][k][2][3][2] = tmp2 * fjac[i][j+1][k][3][2]
   - tmp1 * njac[i][j+1][k][3][2];
 lhs[i][j][k][2][3][3] = tmp2 * fjac[i][j+1][k][3][3]
   - tmp1 * njac[i][j+1][k][3][3]
   - tmp1 * dy4;
 lhs[i][j][k][2][3][4] = tmp2 * fjac[i][j+1][k][3][4]
   - tmp1 * njac[i][j+1][k][3][4];
 lhs[i][j][k][2][4][0] = tmp2 * fjac[i][j+1][k][4][0]
   - tmp1 * njac[i][j+1][k][4][0];
 lhs[i][j][k][2][4][1] = tmp2 * fjac[i][j+1][k][4][1]
   - tmp1 * njac[i][j+1][k][4][1];
 lhs[i][j][k][2][4][2] = tmp2 * fjac[i][j+1][k][4][2]
   - tmp1 * njac[i][j+1][k][4][2];
 lhs[i][j][k][2][4][3] = tmp2 * fjac[i][j+1][k][4][3]
   - tmp1 * njac[i][j+1][k][4][3];
 lhs[i][j][k][2][4][4] = tmp2 * fjac[i][j+1][k][4][4]
   - tmp1 * njac[i][j+1][k][4][4]
   - tmp1 * dy5;
      }
    }
  }
}
static void lhsz(void) {
  int i, j, k;
        
#pragma omp for
  for (i = 1; i < grid_points[0]-1; i++) {
    for (j = 1; j < grid_points[1]-1; j++) {
      for (k = 0; k < grid_points[2]; k++) {
 tmp1 = 1.0 / u[i][j][k][0];
 tmp2 = tmp1 * tmp1;
 tmp3 = tmp1 * tmp2;
 fjac[i][j][k][0][0] = 0.0;
 fjac[i][j][k][0][1] = 0.0;
 fjac[i][j][k][0][2] = 0.0;
 fjac[i][j][k][0][3] = 1.0;
 fjac[i][j][k][0][4] = 0.0;
 fjac[i][j][k][1][0] = - ( u[i][j][k][1]*u[i][j][k][3] )
   * tmp2;
 fjac[i][j][k][1][1] = u[i][j][k][3] * tmp1;
 fjac[i][j][k][1][2] = 0.0;
 fjac[i][j][k][1][3] = u[i][j][k][1] * tmp1;
 fjac[i][j][k][1][4] = 0.0;
 fjac[i][j][k][2][0] = - ( u[i][j][k][2]*u[i][j][k][3] )
   * tmp2;
 fjac[i][j][k][2][1] = 0.0;
 fjac[i][j][k][2][2] = u[i][j][k][3] * tmp1;
 fjac[i][j][k][2][3] = u[i][j][k][2] * tmp1;
 fjac[i][j][k][2][4] = 0.0;
 fjac[i][j][k][3][0] = - (u[i][j][k][3]*u[i][j][k][3] * tmp2 )
   + 0.50 * c2 * ( ( u[i][j][k][1] * u[i][j][k][1]
        + u[i][j][k][2] * u[i][j][k][2]
        + u[i][j][k][3] * u[i][j][k][3] ) * tmp2 );
 fjac[i][j][k][3][1] = - c2 * u[i][j][k][1] * tmp1;
 fjac[i][j][k][3][2] = - c2 * u[i][j][k][2] * tmp1;
 fjac[i][j][k][3][3] = ( 2.0 - c2 )
   * u[i][j][k][3] * tmp1;
 fjac[i][j][k][3][4] = c2;
 fjac[i][j][k][4][0] = ( c2 * ( u[i][j][k][1] * u[i][j][k][1]
     + u[i][j][k][2] * u[i][j][k][2]
     + u[i][j][k][3] * u[i][j][k][3] )
    * tmp2
    - c1 * ( u[i][j][k][4] * tmp1 ) )
   * ( u[i][j][k][3] * tmp1 );
 fjac[i][j][k][4][1] = - c2 * ( u[i][j][k][1]*u[i][j][k][3] )
   * tmp2;
 fjac[i][j][k][4][2] = - c2 * ( u[i][j][k][2]*u[i][j][k][3] )
   * tmp2;
 fjac[i][j][k][4][3] = c1 * ( u[i][j][k][4] * tmp1 )
   - 0.50 * c2
   * ( ( u[i][j][k][1]*u[i][j][k][1]
   + u[i][j][k][2]*u[i][j][k][2]
   + 3.0*u[i][j][k][3]*u[i][j][k][3] )
       * tmp2 );
 fjac[i][j][k][4][4] = c1 * u[i][j][k][3] * tmp1;
 njac[i][j][k][0][0] = 0.0;
 njac[i][j][k][0][1] = 0.0;
 njac[i][j][k][0][2] = 0.0;
 njac[i][j][k][0][3] = 0.0;
 njac[i][j][k][0][4] = 0.0;
 njac[i][j][k][1][0] = - c3c4 * tmp2 * u[i][j][k][1];
 njac[i][j][k][1][1] = c3c4 * tmp1;
 njac[i][j][k][1][2] = 0.0;
 njac[i][j][k][1][3] = 0.0;
 njac[i][j][k][1][4] = 0.0;
 njac[i][j][k][2][0] = - c3c4 * tmp2 * u[i][j][k][2];
 njac[i][j][k][2][1] = 0.0;
 njac[i][j][k][2][2] = c3c4 * tmp1;
 njac[i][j][k][2][3] = 0.0;
 njac[i][j][k][2][4] = 0.0;
 njac[i][j][k][3][0] = - con43 * c3c4 * tmp2 * u[i][j][k][3];
 njac[i][j][k][3][1] = 0.0;
 njac[i][j][k][3][2] = 0.0;
 njac[i][j][k][3][3] = con43 * c3 * c4 * tmp1;
 njac[i][j][k][3][4] = 0.0;
 njac[i][j][k][4][0] = - ( c3c4
   - c1345 ) * tmp3 * (((u[i][j][k][1])*(u[i][j][k][1])))
   - ( c3c4 - c1345 ) * tmp3 * (((u[i][j][k][2])*(u[i][j][k][2])))
   - ( con43 * c3c4
       - c1345 ) * tmp3 * (((u[i][j][k][3])*(u[i][j][k][3])))
   - c1345 * tmp2 * u[i][j][k][4];
 njac[i][j][k][4][1] = ( c3c4 - c1345 ) * tmp2 * u[i][j][k][1];
 njac[i][j][k][4][2] = ( c3c4 - c1345 ) * tmp2 * u[i][j][k][2];
 njac[i][j][k][4][3] = ( con43 * c3c4
    - c1345 ) * tmp2 * u[i][j][k][3];
 njac[i][j][k][4][4] = ( c1345 )* tmp1;
      }
    }
  }
        
#pragma omp for
  for (i = 1; i < grid_points[0]-1; i++) {
    for (j = 1; j < grid_points[1]-1; j++) {
      for (k = 1; k < grid_points[2]-1; k++) {
 tmp1 = dt * tz1;
 tmp2 = dt * tz2;
 lhs[i][j][k][0][0][0] = - tmp2 * fjac[i][j][k-1][0][0]
   - tmp1 * njac[i][j][k-1][0][0]
   - tmp1 * dz1;
 lhs[i][j][k][0][0][1] = - tmp2 * fjac[i][j][k-1][0][1]
   - tmp1 * njac[i][j][k-1][0][1];
 lhs[i][j][k][0][0][2] = - tmp2 * fjac[i][j][k-1][0][2]
   - tmp1 * njac[i][j][k-1][0][2];
 lhs[i][j][k][0][0][3] = - tmp2 * fjac[i][j][k-1][0][3]
   - tmp1 * njac[i][j][k-1][0][3];
 lhs[i][j][k][0][0][4] = - tmp2 * fjac[i][j][k-1][0][4]
   - tmp1 * njac[i][j][k-1][0][4];
 lhs[i][j][k][0][1][0] = - tmp2 * fjac[i][j][k-1][1][0]
   - tmp1 * njac[i][j][k-1][1][0];
 lhs[i][j][k][0][1][1] = - tmp2 * fjac[i][j][k-1][1][1]
   - tmp1 * njac[i][j][k-1][1][1]
   - tmp1 * dz2;
 lhs[i][j][k][0][1][2] = - tmp2 * fjac[i][j][k-1][1][2]
   - tmp1 * njac[i][j][k-1][1][2];
 lhs[i][j][k][0][1][3] = - tmp2 * fjac[i][j][k-1][1][3]
   - tmp1 * njac[i][j][k-1][1][3];
 lhs[i][j][k][0][1][4] = - tmp2 * fjac[i][j][k-1][1][4]
   - tmp1 * njac[i][j][k-1][1][4];
 lhs[i][j][k][0][2][0] = - tmp2 * fjac[i][j][k-1][2][0]
   - tmp1 * njac[i][j][k-1][2][0];
 lhs[i][j][k][0][2][1] = - tmp2 * fjac[i][j][k-1][2][1]
   - tmp1 * njac[i][j][k-1][2][1];
 lhs[i][j][k][0][2][2] = - tmp2 * fjac[i][j][k-1][2][2]
   - tmp1 * njac[i][j][k-1][2][2]
   - tmp1 * dz3;
 lhs[i][j][k][0][2][3] = - tmp2 * fjac[i][j][k-1][2][3]
   - tmp1 * njac[i][j][k-1][2][3];
 lhs[i][j][k][0][2][4] = - tmp2 * fjac[i][j][k-1][2][4]
   - tmp1 * njac[i][j][k-1][2][4];
 lhs[i][j][k][0][3][0] = - tmp2 * fjac[i][j][k-1][3][0]
   - tmp1 * njac[i][j][k-1][3][0];
 lhs[i][j][k][0][3][1] = - tmp2 * fjac[i][j][k-1][3][1]
   - tmp1 * njac[i][j][k-1][3][1];
 lhs[i][j][k][0][3][2] = - tmp2 * fjac[i][j][k-1][3][2]
   - tmp1 * njac[i][j][k-1][3][2];
 lhs[i][j][k][0][3][3] = - tmp2 * fjac[i][j][k-1][3][3]
   - tmp1 * njac[i][j][k-1][3][3]
   - tmp1 * dz4;
 lhs[i][j][k][0][3][4] = - tmp2 * fjac[i][j][k-1][3][4]
   - tmp1 * njac[i][j][k-1][3][4];
 lhs[i][j][k][0][4][0] = - tmp2 * fjac[i][j][k-1][4][0]
   - tmp1 * njac[i][j][k-1][4][0];
 lhs[i][j][k][0][4][1] = - tmp2 * fjac[i][j][k-1][4][1]
   - tmp1 * njac[i][j][k-1][4][1];
 lhs[i][j][k][0][4][2] = - tmp2 * fjac[i][j][k-1][4][2]
   - tmp1 * njac[i][j][k-1][4][2];
 lhs[i][j][k][0][4][3] = - tmp2 * fjac[i][j][k-1][4][3]
   - tmp1 * njac[i][j][k-1][4][3];
 lhs[i][j][k][0][4][4] = - tmp2 * fjac[i][j][k-1][4][4]
   - tmp1 * njac[i][j][k-1][4][4]
   - tmp1 * dz5;
 lhs[i][j][k][1][0][0] = 1.0
   + tmp1 * 2.0 * njac[i][j][k][0][0]
   + tmp1 * 2.0 * dz1;
 lhs[i][j][k][1][0][1] = tmp1 * 2.0 * njac[i][j][k][0][1];
 lhs[i][j][k][1][0][2] = tmp1 * 2.0 * njac[i][j][k][0][2];
 lhs[i][j][k][1][0][3] = tmp1 * 2.0 * njac[i][j][k][0][3];
 lhs[i][j][k][1][0][4] = tmp1 * 2.0 * njac[i][j][k][0][4];
 lhs[i][j][k][1][1][0] = tmp1 * 2.0 * njac[i][j][k][1][0];
 lhs[i][j][k][1][1][1] = 1.0
   + tmp1 * 2.0 * njac[i][j][k][1][1]
   + tmp1 * 2.0 * dz2;
 lhs[i][j][k][1][1][2] = tmp1 * 2.0 * njac[i][j][k][1][2];
 lhs[i][j][k][1][1][3] = tmp1 * 2.0 * njac[i][j][k][1][3];
 lhs[i][j][k][1][1][4] = tmp1 * 2.0 * njac[i][j][k][1][4];
 lhs[i][j][k][1][2][0] = tmp1 * 2.0 * njac[i][j][k][2][0];
 lhs[i][j][k][1][2][1] = tmp1 * 2.0 * njac[i][j][k][2][1];
 lhs[i][j][k][1][2][2] = 1.0
   + tmp1 * 2.0 * njac[i][j][k][2][2]
   + tmp1 * 2.0 * dz3;
 lhs[i][j][k][1][2][3] = tmp1 * 2.0 * njac[i][j][k][2][3];
 lhs[i][j][k][1][2][4] = tmp1 * 2.0 * njac[i][j][k][2][4];
 lhs[i][j][k][1][3][0] = tmp1 * 2.0 * njac[i][j][k][3][0];
 lhs[i][j][k][1][3][1] = tmp1 * 2.0 * njac[i][j][k][3][1];
 lhs[i][j][k][1][3][2] = tmp1 * 2.0 * njac[i][j][k][3][2];
 lhs[i][j][k][1][3][3] = 1.0
   + tmp1 * 2.0 * njac[i][j][k][3][3]
   + tmp1 * 2.0 * dz4;
 lhs[i][j][k][1][3][4] = tmp1 * 2.0 * njac[i][j][k][3][4];
 lhs[i][j][k][1][4][0] = tmp1 * 2.0 * njac[i][j][k][4][0];
 lhs[i][j][k][1][4][1] = tmp1 * 2.0 * njac[i][j][k][4][1];
 lhs[i][j][k][1][4][2] = tmp1 * 2.0 * njac[i][j][k][4][2];
 lhs[i][j][k][1][4][3] = tmp1 * 2.0 * njac[i][j][k][4][3];
 lhs[i][j][k][1][4][4] = 1.0
   + tmp1 * 2.0 * njac[i][j][k][4][4]
   + tmp1 * 2.0 * dz5;
 lhs[i][j][k][2][0][0] = tmp2 * fjac[i][j][k+1][0][0]
   - tmp1 * njac[i][j][k+1][0][0]
   - tmp1 * dz1;
 lhs[i][j][k][2][0][1] = tmp2 * fjac[i][j][k+1][0][1]
   - tmp1 * njac[i][j][k+1][0][1];
 lhs[i][j][k][2][0][2] = tmp2 * fjac[i][j][k+1][0][2]
   - tmp1 * njac[i][j][k+1][0][2];
 lhs[i][j][k][2][0][3] = tmp2 * fjac[i][j][k+1][0][3]
   - tmp1 * njac[i][j][k+1][0][3];
 lhs[i][j][k][2][0][4] = tmp2 * fjac[i][j][k+1][0][4]
   - tmp1 * njac[i][j][k+1][0][4];
 lhs[i][j][k][2][1][0] = tmp2 * fjac[i][j][k+1][1][0]
   - tmp1 * njac[i][j][k+1][1][0];
 lhs[i][j][k][2][1][1] = tmp2 * fjac[i][j][k+1][1][1]
   - tmp1 * njac[i][j][k+1][1][1]
   - tmp1 * dz2;
 lhs[i][j][k][2][1][2] = tmp2 * fjac[i][j][k+1][1][2]
   - tmp1 * njac[i][j][k+1][1][2];
 lhs[i][j][k][2][1][3] = tmp2 * fjac[i][j][k+1][1][3]
   - tmp1 * njac[i][j][k+1][1][3];
 lhs[i][j][k][2][1][4] = tmp2 * fjac[i][j][k+1][1][4]
   - tmp1 * njac[i][j][k+1][1][4];
 lhs[i][j][k][2][2][0] = tmp2 * fjac[i][j][k+1][2][0]
   - tmp1 * njac[i][j][k+1][2][0];
 lhs[i][j][k][2][2][1] = tmp2 * fjac[i][j][k+1][2][1]
   - tmp1 * njac[i][j][k+1][2][1];
 lhs[i][j][k][2][2][2] = tmp2 * fjac[i][j][k+1][2][2]
   - tmp1 * njac[i][j][k+1][2][2]
   - tmp1 * dz3;
 lhs[i][j][k][2][2][3] = tmp2 * fjac[i][j][k+1][2][3]
   - tmp1 * njac[i][j][k+1][2][3];
 lhs[i][j][k][2][2][4] = tmp2 * fjac[i][j][k+1][2][4]
   - tmp1 * njac[i][j][k+1][2][4];
 lhs[i][j][k][2][3][0] = tmp2 * fjac[i][j][k+1][3][0]
   - tmp1 * njac[i][j][k+1][3][0];
 lhs[i][j][k][2][3][1] = tmp2 * fjac[i][j][k+1][3][1]
   - tmp1 * njac[i][j][k+1][3][1];
 lhs[i][j][k][2][3][2] = tmp2 * fjac[i][j][k+1][3][2]
   - tmp1 * njac[i][j][k+1][3][2];
 lhs[i][j][k][2][3][3] = tmp2 * fjac[i][j][k+1][3][3]
   - tmp1 * njac[i][j][k+1][3][3]
   - tmp1 * dz4;
 lhs[i][j][k][2][3][4] = tmp2 * fjac[i][j][k+1][3][4]
   - tmp1 * njac[i][j][k+1][3][4];
 lhs[i][j][k][2][4][0] = tmp2 * fjac[i][j][k+1][4][0]
   - tmp1 * njac[i][j][k+1][4][0];
 lhs[i][j][k][2][4][1] = tmp2 * fjac[i][j][k+1][4][1]
   - tmp1 * njac[i][j][k+1][4][1];
 lhs[i][j][k][2][4][2] = tmp2 * fjac[i][j][k+1][4][2]
   - tmp1 * njac[i][j][k+1][4][2];
 lhs[i][j][k][2][4][3] = tmp2 * fjac[i][j][k+1][4][3]
   - tmp1 * njac[i][j][k+1][4][3];
 lhs[i][j][k][2][4][4] = tmp2 * fjac[i][j][k+1][4][4]
   - tmp1 * njac[i][j][k+1][4][4]
   - tmp1 * dz5;
      }
    }
  }
}
static void compute_rhs(void) {
  int i, j, k, m;
  double rho_inv, uijk, up1, um1, vijk, vp1, vm1, wijk, wp1, wm1;
        
#pragma omp for nowait
  for (i = 0; i < grid_points[0]; i++) {
    for (j = 0; j < grid_points[1]; j++) {
      for (k = 0; k < grid_points[2]; k++) {
 rho_inv = 1.0/u[i][j][k][0];
 rho_i[i][j][k] = rho_inv;
 us[i][j][k] = u[i][j][k][1] * rho_inv;
 vs[i][j][k] = u[i][j][k][2] * rho_inv;
 ws[i][j][k] = u[i][j][k][3] * rho_inv;
 square[i][j][k] = 0.5 * (u[i][j][k][1]*u[i][j][k][1] +
     u[i][j][k][2]*u[i][j][k][2] +
     u[i][j][k][3]*u[i][j][k][3] ) * rho_inv;
 qs[i][j][k] = square[i][j][k] * rho_inv;
      }
    }
  }
        
#pragma omp for
  for (i = 0; i < grid_points[0]; i++) {
    for (j = 0; j < grid_points[1]; j++) {
      for (k = 0; k < grid_points[2]; k++) {
 for (m = 0; m < 5; m++) {
   rhs[i][j][k][m] = forcing[i][j][k][m];
 }
      }
    }
  }
        
#pragma omp for
  for (i = 1; i < grid_points[0]-1; i++) {
    for (j = 1; j < grid_points[1]-1; j++) {
      for (k = 1; k < grid_points[2]-1; k++) {
 uijk = us[i][j][k];
 up1 = us[i+1][j][k];
 um1 = us[i-1][j][k];
 rhs[i][j][k][0] = rhs[i][j][k][0] + dx1tx1 *
   (u[i+1][j][k][0] - 2.0*u[i][j][k][0] +
    u[i-1][j][k][0]) -
   tx2 * (u[i+1][j][k][1] - u[i-1][j][k][1]);
 rhs[i][j][k][1] = rhs[i][j][k][1] + dx2tx1 *
   (u[i+1][j][k][1] - 2.0*u[i][j][k][1] +
    u[i-1][j][k][1]) +
   xxcon2*con43 * (up1 - 2.0*uijk + um1) -
   tx2 * (u[i+1][j][k][1]*up1 -
   u[i-1][j][k][1]*um1 +
   (u[i+1][j][k][4]- square[i+1][j][k]-
    u[i-1][j][k][4]+ square[i-1][j][k])*
   c2);
 rhs[i][j][k][2] = rhs[i][j][k][2] + dx3tx1 *
   (u[i+1][j][k][2] - 2.0*u[i][j][k][2] +
    u[i-1][j][k][2]) +
   xxcon2 * (vs[i+1][j][k] - 2.0*vs[i][j][k] +
      vs[i-1][j][k]) -
   tx2 * (u[i+1][j][k][2]*up1 -
   u[i-1][j][k][2]*um1);
 rhs[i][j][k][3] = rhs[i][j][k][3] + dx4tx1 *
   (u[i+1][j][k][3] - 2.0*u[i][j][k][3] +
    u[i-1][j][k][3]) +
   xxcon2 * (ws[i+1][j][k] - 2.0*ws[i][j][k] +
      ws[i-1][j][k]) -
   tx2 * (u[i+1][j][k][3]*up1 -
   u[i-1][j][k][3]*um1);
 rhs[i][j][k][4] = rhs[i][j][k][4] + dx5tx1 *
   (u[i+1][j][k][4] - 2.0*u[i][j][k][4] +
    u[i-1][j][k][4]) +
   xxcon3 * (qs[i+1][j][k] - 2.0*qs[i][j][k] +
      qs[i-1][j][k]) +
   xxcon4 * (up1*up1 - 2.0*uijk*uijk +
      um1*um1) +
   xxcon5 * (u[i+1][j][k][4]*rho_i[i+1][j][k] -
      2.0*u[i][j][k][4]*rho_i[i][j][k] +
      u[i-1][j][k][4]*rho_i[i-1][j][k]) -
   tx2 * ( (c1*u[i+1][j][k][4] -
     c2*square[i+1][j][k])*up1 -
    (c1*u[i-1][j][k][4] -
     c2*square[i-1][j][k])*um1 );
      }
    }
  }
  i = 1;
        
#pragma omp for nowait
  for (j = 1; j < grid_points[1]-1; j++) {
    for (k = 1; k < grid_points[2]-1; k++) {
      for (m = 0; m < 5; m++) {
 rhs[i][j][k][m] = rhs[i][j][k][m]- dssp *
   ( 5.0*u[i][j][k][m] - 4.0*u[i+1][j][k][m] +
     u[i+2][j][k][m]);
      }
    }
  }
  i = 2;
        
#pragma omp for nowait
  for (j = 1; j < grid_points[1]-1; j++) {
    for (k = 1; k < grid_points[2]-1; k++) {
      for (m = 0; m < 5; m++) {
 rhs[i][j][k][m] = rhs[i][j][k][m] - dssp *
   (-4.0*u[i-1][j][k][m] + 6.0*u[i][j][k][m] -
    4.0*u[i+1][j][k][m] + u[i+2][j][k][m]);
      }
    }
  }
        
#pragma omp for nowait
  for (i = 3; i < grid_points[0]-3; i++) {
    for (j = 1; j < grid_points[1]-1; j++) {
      for (k = 1; k < grid_points[2]-1; k++) {
 for (m = 0; m < 5; m++) {
   rhs[i][j][k][m] = rhs[i][j][k][m] - dssp *
     ( u[i-2][j][k][m] - 4.0*u[i-1][j][k][m] +
        6.0*u[i][j][k][m] - 4.0*u[i+1][j][k][m] +
        u[i+2][j][k][m] );
 }
      }
    }
  }
  i = grid_points[0]-3;
        
#pragma omp for nowait
  for (j = 1; j < grid_points[1]-1; j++) {
    for (k = 1; k < grid_points[2]-1; k++) {
      for (m = 0; m < 5; m++) {
 rhs[i][j][k][m] = rhs[i][j][k][m] - dssp *
   ( u[i-2][j][k][m] - 4.0*u[i-1][j][k][m] +
     6.0*u[i][j][k][m] - 4.0*u[i+1][j][k][m] );
      }
    }
  }
  i = grid_points[0]-2;
        
#pragma omp for
  for (j = 1; j < grid_points[1]-1; j++) {
    for (k = 1; k < grid_points[2]-1; k++) {
      for (m = 0; m < 5; m++) {
 rhs[i][j][k][m] = rhs[i][j][k][m] - dssp *
   ( u[i-2][j][k][m] - 4.*u[i-1][j][k][m] +
     5.0*u[i][j][k][m] );
      }
    }
  }
        
#pragma omp for
  for (i = 1; i < grid_points[0]-1; i++) {
    for (j = 1; j < grid_points[1]-1; j++) {
      for (k = 1; k < grid_points[2]-1; k++) {
 vijk = vs[i][j][k];
 vp1 = vs[i][j+1][k];
 vm1 = vs[i][j-1][k];
 rhs[i][j][k][0] = rhs[i][j][k][0] + dy1ty1 *
   (u[i][j+1][k][0] - 2.0*u[i][j][k][0] +
    u[i][j-1][k][0]) -
   ty2 * (u[i][j+1][k][2] - u[i][j-1][k][2]);
 rhs[i][j][k][1] = rhs[i][j][k][1] + dy2ty1 *
   (u[i][j+1][k][1] - 2.0*u[i][j][k][1] +
    u[i][j-1][k][1]) +
   yycon2 * (us[i][j+1][k] - 2.0*us[i][j][k] +
      us[i][j-1][k]) -
   ty2 * (u[i][j+1][k][1]*vp1 -
   u[i][j-1][k][1]*vm1);
 rhs[i][j][k][2] = rhs[i][j][k][2] + dy3ty1 *
   (u[i][j+1][k][2] - 2.0*u[i][j][k][2] +
    u[i][j-1][k][2]) +
   yycon2*con43 * (vp1 - 2.0*vijk + vm1) -
   ty2 * (u[i][j+1][k][2]*vp1 -
   u[i][j-1][k][2]*vm1 +
   (u[i][j+1][k][4] - square[i][j+1][k] -
    u[i][j-1][k][4] + square[i][j-1][k])
   *c2);
 rhs[i][j][k][3] = rhs[i][j][k][3] + dy4ty1 *
   (u[i][j+1][k][3] - 2.0*u[i][j][k][3] +
    u[i][j-1][k][3]) +
   yycon2 * (ws[i][j+1][k] - 2.0*ws[i][j][k] +
      ws[i][j-1][k]) -
   ty2 * (u[i][j+1][k][3]*vp1 -
   u[i][j-1][k][3]*vm1);
 rhs[i][j][k][4] = rhs[i][j][k][4] + dy5ty1 *
   (u[i][j+1][k][4] - 2.0*u[i][j][k][4] +
    u[i][j-1][k][4]) +
   yycon3 * (qs[i][j+1][k] - 2.0*qs[i][j][k] +
      qs[i][j-1][k]) +
   yycon4 * (vp1*vp1 - 2.0*vijk*vijk +
      vm1*vm1) +
   yycon5 * (u[i][j+1][k][4]*rho_i[i][j+1][k] -
      2.0*u[i][j][k][4]*rho_i[i][j][k] +
      u[i][j-1][k][4]*rho_i[i][j-1][k]) -
   ty2 * ((c1*u[i][j+1][k][4] -
    c2*square[i][j+1][k]) * vp1 -
   (c1*u[i][j-1][k][4] -
    c2*square[i][j-1][k]) * vm1);
      }
    }
  }
  j = 1;
        
#pragma omp for nowait
  for (i = 1; i < grid_points[0]-1; i++) {
    for (k = 1; k < grid_points[2]-1; k++) {
      for (m = 0; m < 5; m++) {
 rhs[i][j][k][m] = rhs[i][j][k][m]- dssp *
   ( 5.0*u[i][j][k][m] - 4.0*u[i][j+1][k][m] +
     u[i][j+2][k][m]);
      }
    }
  }
  j = 2;
        
#pragma omp for nowait
  for (i = 1; i < grid_points[0]-1; i++) {
    for (k = 1; k < grid_points[2]-1; k++) {
      for (m = 0; m < 5; m++) {
 rhs[i][j][k][m] = rhs[i][j][k][m] - dssp *
   (-4.0*u[i][j-1][k][m] + 6.0*u[i][j][k][m] -
    4.0*u[i][j+1][k][m] + u[i][j+2][k][m]);
      }
    }
  }
        
#pragma omp for nowait
  for (i = 1; i < grid_points[0]-1; i++) {
    for (j = 3; j < grid_points[1]-3; j++) {
      for (k = 1; k < grid_points[2]-1; k++) {
 for (m = 0; m < 5; m++) {
   rhs[i][j][k][m] = rhs[i][j][k][m] - dssp *
     ( u[i][j-2][k][m] - 4.0*u[i][j-1][k][m] +
        6.0*u[i][j][k][m] - 4.0*u[i][j+1][k][m] +
        u[i][j+2][k][m] );
 }
      }
    }
  }
  j = grid_points[1]-3;
        
#pragma omp for nowait
  for (i = 1; i < grid_points[0]-1; i++) {
    for (k = 1; k < grid_points[2]-1; k++) {
      for (m = 0; m < 5; m++) {
 rhs[i][j][k][m] = rhs[i][j][k][m] - dssp *
   ( u[i][j-2][k][m] - 4.0*u[i][j-1][k][m] +
     6.0*u[i][j][k][m] - 4.0*u[i][j+1][k][m] );
      }
    }
  }
  j = grid_points[1]-2;
        
#pragma omp for
  for (i = 1; i < grid_points[0]-1; i++) {
    for (k = 1; k < grid_points[2]-1; k++) {
      for (m = 0; m < 5; m++) {
 rhs[i][j][k][m] = rhs[i][j][k][m] - dssp *
   ( u[i][j-2][k][m] - 4.*u[i][j-1][k][m] +
     5.*u[i][j][k][m] );
      }
    }
  }
        
#pragma omp for
  for (i = 1; i < grid_points[0]-1; i++) {
    for (j = 1; j < grid_points[1]-1; j++) {
      for (k = 1; k < grid_points[2]-1; k++) {
 wijk = ws[i][j][k];
 wp1 = ws[i][j][k+1];
 wm1 = ws[i][j][k-1];
 rhs[i][j][k][0] = rhs[i][j][k][0] + dz1tz1 *
   (u[i][j][k+1][0] - 2.0*u[i][j][k][0] +
    u[i][j][k-1][0]) -
   tz2 * (u[i][j][k+1][3] - u[i][j][k-1][3]);
 rhs[i][j][k][1] = rhs[i][j][k][1] + dz2tz1 *
   (u[i][j][k+1][1] - 2.0*u[i][j][k][1] +
    u[i][j][k-1][1]) +
   zzcon2 * (us[i][j][k+1] - 2.0*us[i][j][k] +
      us[i][j][k-1]) -
   tz2 * (u[i][j][k+1][1]*wp1 -
   u[i][j][k-1][1]*wm1);
 rhs[i][j][k][2] = rhs[i][j][k][2] + dz3tz1 *
   (u[i][j][k+1][2] - 2.0*u[i][j][k][2] +
    u[i][j][k-1][2]) +
   zzcon2 * (vs[i][j][k+1] - 2.0*vs[i][j][k] +
      vs[i][j][k-1]) -
   tz2 * (u[i][j][k+1][2]*wp1 -
   u[i][j][k-1][2]*wm1);
 rhs[i][j][k][3] = rhs[i][j][k][3] + dz4tz1 *
   (u[i][j][k+1][3] - 2.0*u[i][j][k][3] +
    u[i][j][k-1][3]) +
   zzcon2*con43 * (wp1 - 2.0*wijk + wm1) -
   tz2 * (u[i][j][k+1][3]*wp1 -
   u[i][j][k-1][3]*wm1 +
   (u[i][j][k+1][4] - square[i][j][k+1] -
    u[i][j][k-1][4] + square[i][j][k-1])
   *c2);
 rhs[i][j][k][4] = rhs[i][j][k][4] + dz5tz1 *
   (u[i][j][k+1][4] - 2.0*u[i][j][k][4] +
    u[i][j][k-1][4]) +
   zzcon3 * (qs[i][j][k+1] - 2.0*qs[i][j][k] +
      qs[i][j][k-1]) +
   zzcon4 * (wp1*wp1 - 2.0*wijk*wijk +
      wm1*wm1) +
   zzcon5 * (u[i][j][k+1][4]*rho_i[i][j][k+1] -
      2.0*u[i][j][k][4]*rho_i[i][j][k] +
      u[i][j][k-1][4]*rho_i[i][j][k-1]) -
   tz2 * ( (c1*u[i][j][k+1][4] -
     c2*square[i][j][k+1])*wp1 -
    (c1*u[i][j][k-1][4] -
     c2*square[i][j][k-1])*wm1);
      }
    }
  }
  k = 1;
        
#pragma omp for nowait
  for (i = 1; i < grid_points[0]-1; i++) {
    for (j = 1; j < grid_points[1]-1; j++) {
      for (m = 0; m < 5; m++) {
 rhs[i][j][k][m] = rhs[i][j][k][m]- dssp *
   ( 5.0*u[i][j][k][m] - 4.0*u[i][j][k+1][m] +
     u[i][j][k+2][m]);
      }
    }
  }
  k = 2;
        
#pragma omp for nowait
  for (i = 1; i < grid_points[0]-1; i++) {
    for (j = 1; j < grid_points[1]-1; j++) {
      for (m = 0; m < 5; m++) {
 rhs[i][j][k][m] = rhs[i][j][k][m] - dssp *
   (-4.0*u[i][j][k-1][m] + 6.0*u[i][j][k][m] -
    4.0*u[i][j][k+1][m] + u[i][j][k+2][m]);
      }
    }
  }
        
#pragma omp for nowait
  for (i = 1; i < grid_points[0]-1; i++) {
    for (j = 1; j < grid_points[1]-1; j++) {
      for (k = 3; k < grid_points[2]-3; k++) {
 for (m = 0; m < 5; m++) {
   rhs[i][j][k][m] = rhs[i][j][k][m] - dssp *
     ( u[i][j][k-2][m] - 4.0*u[i][j][k-1][m] +
        6.0*u[i][j][k][m] - 4.0*u[i][j][k+1][m] +
        u[i][j][k+2][m] );
 }
      }
    }
  }
  k = grid_points[2]-3;
        
#pragma omp for nowait
  for (i = 1; i < grid_points[0]-1; i++) {
    for (j = 1; j < grid_points[1]-1; j++) {
      for (m = 0; m < 5; m++) {
 rhs[i][j][k][m] = rhs[i][j][k][m] - dssp *
   ( u[i][j][k-2][m] - 4.0*u[i][j][k-1][m] +
     6.0*u[i][j][k][m] - 4.0*u[i][j][k+1][m] );
      }
    }
  }
  k = grid_points[2]-2;
        
#pragma omp for
  for (i = 1; i < grid_points[0]-1; i++) {
    for (j = 1; j < grid_points[1]-1; j++) {
      for (m = 0; m < 5; m++) {
 rhs[i][j][k][m] = rhs[i][j][k][m] - dssp *
   ( u[i][j][k-2][m] - 4.0*u[i][j][k-1][m] +
     5.0*u[i][j][k][m] );
      }
    }
  }
        
#pragma omp for
  for (j = 1; j < grid_points[1]-1; j++) {
    for (k = 1; k < grid_points[2]-1; k++) {
      for (m = 0; m < 5; m++) {
 for (i = 1; i < grid_points[0]-1; i++) {
   rhs[i][j][k][m] = rhs[i][j][k][m] * dt;
 }
      }
    }
  }
}
static void set_constants(void) {
  ce[0][0] = 2.0;
  ce[0][1] = 0.0;
  ce[0][2] = 0.0;
  ce[0][3] = 4.0;
  ce[0][4] = 5.0;
  ce[0][5] = 3.0;
  ce[0][6] = 0.5;
  ce[0][7] = 0.02;
  ce[0][8] = 0.01;
  ce[0][9] = 0.03;
  ce[0][10] = 0.5;
  ce[0][11] = 0.4;
  ce[0][12] = 0.3;
  ce[1][0] = 1.0;
  ce[1][1] = 0.0;
  ce[1][2] = 0.0;
  ce[1][3] = 0.0;
  ce[1][4] = 1.0;
  ce[1][5] = 2.0;
  ce[1][6] = 3.0;
  ce[1][7] = 0.01;
  ce[1][8] = 0.03;
  ce[1][9] = 0.02;
  ce[1][10] = 0.4;
  ce[1][11] = 0.3;
  ce[1][12] = 0.5;
  ce[2][0] = 2.0;
  ce[2][1] = 2.0;
  ce[2][2] = 0.0;
  ce[2][3] = 0.0;
  ce[2][4] = 0.0;
  ce[2][5] = 2.0;
  ce[2][6] = 3.0;
  ce[2][7] = 0.04;
  ce[2][8] = 0.03;
  ce[2][9] = 0.05;
  ce[2][10] = 0.3;
  ce[2][11] = 0.5;
  ce[2][12] = 0.4;
  ce[3][0] = 2.0;
  ce[3][1] = 2.0;
  ce[3][2] = 0.0;
  ce[3][3] = 0.0;
  ce[3][4] = 0.0;
  ce[3][5] = 2.0;
  ce[3][6] = 3.0;
  ce[3][7] = 0.03;
  ce[3][8] = 0.05;
  ce[3][9] = 0.04;
  ce[3][10] = 0.2;
  ce[3][11] = 0.1;
  ce[3][12] = 0.3;
  ce[4][0] = 5.0;
  ce[4][1] = 4.0;
  ce[4][2] = 3.0;
  ce[4][3] = 2.0;
  ce[4][4] = 0.1;
  ce[4][5] = 0.4;
  ce[4][6] = 0.3;
  ce[4][7] = 0.05;
  ce[4][8] = 0.04;
  ce[4][9] = 0.03;
  ce[4][10] = 0.1;
  ce[4][11] = 0.3;
  ce[4][12] = 0.2;
  c1 = 1.4;
  c2 = 0.4;
  c3 = 0.1;
  c4 = 1.0;
  c5 = 1.4;
  dnxm1 = 1.0 / (double)(grid_points[0]-1);
  dnym1 = 1.0 / (double)(grid_points[1]-1);
  dnzm1 = 1.0 / (double)(grid_points[2]-1);
  c1c2 = c1 * c2;
  c1c5 = c1 * c5;
  c3c4 = c3 * c4;
  c1345 = c1c5 * c3c4;
  conz1 = (1.0-c1c5);
  tx1 = 1.0 / (dnxm1 * dnxm1);
  tx2 = 1.0 / (2.0 * dnxm1);
  tx3 = 1.0 / dnxm1;
  ty1 = 1.0 / (dnym1 * dnym1);
  ty2 = 1.0 / (2.0 * dnym1);
  ty3 = 1.0 / dnym1;
  tz1 = 1.0 / (dnzm1 * dnzm1);
  tz2 = 1.0 / (2.0 * dnzm1);
  tz3 = 1.0 / dnzm1;
  dx1 = 0.75;
  dx2 = 0.75;
  dx3 = 0.75;
  dx4 = 0.75;
  dx5 = 0.75;
  dy1 = 0.75;
  dy2 = 0.75;
  dy3 = 0.75;
  dy4 = 0.75;
  dy5 = 0.75;
  dz1 = 1.0;
  dz2 = 1.0;
  dz3 = 1.0;
  dz4 = 1.0;
  dz5 = 1.0;
  dxmax = (((dx3) > (dx4)) ? (dx3) : (dx4));
  dymax = (((dy2) > (dy4)) ? (dy2) : (dy4));
  dzmax = (((dz2) > (dz3)) ? (dz2) : (dz3));
  dssp = 0.25 * (((dx1) > ((((dy1) > (dz1)) ? (dy1) : (dz1)))) ? (dx1) : ((((dy1) > (dz1)) ? (dy1) : (dz1))));
  c4dssp = 4.0 * dssp;
  c5dssp = 5.0 * dssp;
  dttx1 = dt*tx1;
  dttx2 = dt*tx2;
  dtty1 = dt*ty1;
  dtty2 = dt*ty2;
  dttz1 = dt*tz1;
  dttz2 = dt*tz2;
  c2dttx1 = 2.0*dttx1;
  c2dtty1 = 2.0*dtty1;
  c2dttz1 = 2.0*dttz1;
  dtdssp = dt*dssp;
  comz1 = dtdssp;
  comz4 = 4.0*dtdssp;
  comz5 = 5.0*dtdssp;
  comz6 = 6.0*dtdssp;
  c3c4tx3 = c3c4*tx3;
  c3c4ty3 = c3c4*ty3;
  c3c4tz3 = c3c4*tz3;
  dx1tx1 = dx1*tx1;
  dx2tx1 = dx2*tx1;
  dx3tx1 = dx3*tx1;
  dx4tx1 = dx4*tx1;
  dx5tx1 = dx5*tx1;
  dy1ty1 = dy1*ty1;
  dy2ty1 = dy2*ty1;
  dy3ty1 = dy3*ty1;
  dy4ty1 = dy4*ty1;
  dy5ty1 = dy5*ty1;
  dz1tz1 = dz1*tz1;
  dz2tz1 = dz2*tz1;
  dz3tz1 = dz3*tz1;
  dz4tz1 = dz4*tz1;
  dz5tz1 = dz5*tz1;
  c2iv = 2.5;
  con43 = 4.0/3.0;
  con16 = 1.0/6.0;
  xxcon1 = c3c4tx3*con43*tx3;
  xxcon2 = c3c4tx3*tx3;
  xxcon3 = c3c4tx3*conz1*tx3;
  xxcon4 = c3c4tx3*con16*tx3;
  xxcon5 = c3c4tx3*c1c5*tx3;
  yycon1 = c3c4ty3*con43*ty3;
  yycon2 = c3c4ty3*ty3;
  yycon3 = c3c4ty3*conz1*ty3;
  yycon4 = c3c4ty3*con16*ty3;
  yycon5 = c3c4ty3*c1c5*ty3;
  zzcon1 = c3c4tz3*con43*tz3;
  zzcon2 = c3c4tz3*tz3;
  zzcon3 = c3c4tz3*conz1*tz3;
  zzcon4 = c3c4tz3*con16*tz3;
  zzcon5 = c3c4tz3*c1c5*tz3;
}
static void verify(int no_time_steps, char *class, boolean *verified) {
  double xcrref[5],xceref[5],xcrdif[5],xcedif[5],
    epsilon, xce[5], xcr[5], dtref;
  int m;
  epsilon = 1.0e-08;
  error_norm(xce);
  compute_rhs();
  rhs_norm(xcr);
  for (m = 0; m < 5; m++) {
    xcr[m] = xcr[m] / dt;
  }
  *class = 'U';
  *verified = 1;
  for (m = 0; m < 5; m++) {
    xcrref[m] = 1.0;
    xceref[m] = 1.0;
  }
  if (grid_points[0] == 12 &&
      grid_points[1] == 12 &&
      grid_points[2] == 12 &&
      no_time_steps == 60) {
    *class = 'S';
    dtref = 1.0e-2;
    xcrref[0] = 1.7034283709541311e-01;
    xcrref[1] = 1.2975252070034097e-02;
    xcrref[2] = 3.2527926989486055e-02;
    xcrref[3] = 2.6436421275166801e-02;
    xcrref[4] = 1.9211784131744430e-01;
    xceref[0] = 4.9976913345811579e-04;
    xceref[1] = 4.5195666782961927e-05;
    xceref[2] = 7.3973765172921357e-05;
    xceref[3] = 7.3821238632439731e-05;
    xceref[4] = 8.9269630987491446e-04;
    } else if (grid_points[0] == 24 &&
        grid_points[1] == 24 &&
        grid_points[2] == 24 &&
        no_time_steps == 200) {
      *class = 'W';
      dtref = 0.8e-3;
      xcrref[0] = 0.1125590409344e+03;
      xcrref[1] = 0.1180007595731e+02;
      xcrref[2] = 0.2710329767846e+02;
      xcrref[3] = 0.2469174937669e+02;
      xcrref[4] = 0.2638427874317e+03;
      xceref[0] = 0.4419655736008e+01;
      xceref[1] = 0.4638531260002e+00;
      xceref[2] = 0.1011551749967e+01;
      xceref[3] = 0.9235878729944e+00;
      xceref[4] = 0.1018045837718e+02;
    } else if (grid_points[0] == 64 &&
        grid_points[1] == 64 &&
        grid_points[2] == 64 &&
        no_time_steps == 200) {
      *class = 'A';
      dtref = 0.8e-3;
      xcrref[0] = 1.0806346714637264e+02;
      xcrref[1] = 1.1319730901220813e+01;
      xcrref[2] = 2.5974354511582465e+01;
      xcrref[3] = 2.3665622544678910e+01;
      xcrref[4] = 2.5278963211748344e+02;
      xceref[0] = 4.2348416040525025e+00;
      xceref[1] = 4.4390282496995698e-01;
      xceref[2] = 9.6692480136345650e-01;
      xceref[3] = 8.8302063039765474e-01;
      xceref[4] = 9.7379901770829278e+00;
    } else if (grid_points[0] == 102 &&
        grid_points[1] == 102 &&
        grid_points[2] == 102 &&
        no_time_steps == 200) {
      *class = 'B';
      dtref = 3.0e-4;
      xcrref[0] = 1.4233597229287254e+03;
      xcrref[1] = 9.9330522590150238e+01;
      xcrref[2] = 3.5646025644535285e+02;
      xcrref[3] = 3.2485447959084092e+02;
      xcrref[4] = 3.2707541254659363e+03;
      xceref[0] = 5.2969847140936856e+01;
      xceref[1] = 4.4632896115670668e+00;
      xceref[2] = 1.3122573342210174e+01;
      xceref[3] = 1.2006925323559144e+01;
      xceref[4] = 1.2459576151035986e+02;
    } else if (grid_points[0] == 162 &&
        grid_points[1] == 162 &&
        grid_points[2] == 162 &&
        no_time_steps == 200) {
      *class = 'C';
      dtref = 1.0e-4;
      xcrref[0] = 0.62398116551764615e+04;
      xcrref[1] = 0.50793239190423964e+03;
      xcrref[2] = 0.15423530093013596e+04;
      xcrref[3] = 0.13302387929291190e+04;
      xcrref[4] = 0.11604087428436455e+05;
      xceref[0] = 0.16462008369091265e+03;
      xceref[1] = 0.11497107903824313e+02;
      xceref[2] = 0.41207446207461508e+02;
      xceref[3] = 0.37087651059694167e+02;
      xceref[4] = 0.36211053051841265e+03;
    } else {
      *verified = 0;
    }
  for (m = 0; m < 5; m++) {
    xcrdif[m] = fabs((xcr[m]-xcrref[m])/xcrref[m]);
    xcedif[m] = fabs((xce[m]-xceref[m])/xceref[m]);
  }
  if (*class != 'U') {
    printf(" Verification being performed for class %1c\n", *class);
    printf(" accuracy setting for epsilon = %20.13e\n", epsilon);
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
      printf("          %2d%20.13e\n", m, xcr[m]);
    } else if (xcrdif[m] > epsilon) {
      *verified = 0;
      printf(" FAILURE: %2d%20.13e%20.13e%20.13e\n",
      m, xcr[m], xcrref[m], xcrdif[m]);
    } else {
      printf("          %2d%20.13e%20.13e%20.13e\n",
      m, xcr[m], xcrref[m], xcrdif[m]);
    }
  }
  if (*class != 'U') {
    printf(" Comparison of RMS-norms of solution error\n");
  } else {
    printf(" RMS-norms of solution error\n");
  }
  for (m = 0; m < 5; m++) {
    if (*class == 'U') {
      printf("          %2d%20.13e\n", m, xce[m]);
    } else if (xcedif[m] > epsilon) {
      *verified = 0;
      printf(" FAILURE: %2d%20.13e%20.13e%20.13e\n",
      m, xce[m], xceref[m], xcedif[m]);
    } else {
      printf("          %2d%20.13e%20.13e%20.13e\n",
      m, xce[m], xceref[m], xcedif[m]);
    }
  }
  if (*class == 'U') {
    printf(" No reference values provided\n");
    printf(" No verification performed\n");
  } else if (*verified == 1) {
    printf(" Verification Successful\n");
  } else {
    printf(" Verification failed\n");
  }
}
static void x_solve(void) {
  lhsx();
  x_solve_cell();
  x_backsubstitute();
}
static void x_backsubstitute(void) {
  int i, j, k, m, n;
  for (i = grid_points[0]-2; i >= 0; i--) {
        
#pragma omp for
    for (j = 1; j < grid_points[1]-1; j++) {
      for (k = 1; k < grid_points[2]-1; k++) {
 for (m = 0; m < 5; m++) {
   for (n = 0; n < 5; n++) {
     rhs[i][j][k][m] = rhs[i][j][k][m]
       - lhs[i][j][k][2][m][n]*rhs[i+1][j][k][n];
   }
 }
      }
    }
  }
}
static void x_solve_cell(void) {
  int i,j,k,isize;
  isize = grid_points[0]-1;
        
#pragma omp for
  for (j = 1; j < grid_points[1]-1; j++) {
    for (k = 1; k < grid_points[2]-1; k++) {
      binvcrhs( lhs[0][j][k][1],
  lhs[0][j][k][2],
  rhs[0][j][k] );
    }
  }
  for (i = 1; i < isize; i++) {
        
#pragma omp for
    for (j = 1; j < grid_points[1]-1; j++) {
      for (k = 1; k < grid_points[2]-1; k++) {
 matvec_sub(lhs[i][j][k][0],
     rhs[i-1][j][k], rhs[i][j][k]);
 matmul_sub(lhs[i][j][k][0],
     lhs[i-1][j][k][2],
     lhs[i][j][k][1]);
 binvcrhs( lhs[i][j][k][1],
    lhs[i][j][k][2],
    rhs[i][j][k] );
      }
    }
  }
        
#pragma omp for
  for (j = 1; j < grid_points[1]-1; j++) {
    for (k = 1; k < grid_points[2]-1; k++) {
      matvec_sub(lhs[isize][j][k][0],
   rhs[isize-1][j][k], rhs[isize][j][k]);
      matmul_sub(lhs[isize][j][k][0],
   lhs[isize-1][j][k][2],
   lhs[isize][j][k][1]);
      binvrhs( lhs[i][j][k][1],
        rhs[i][j][k] );
    }
  }
}
static void matvec_sub(double ablock[5][5], double avec[5], double bvec[5]) {
  int i;
  for (i = 0; i < 5; i++) {
    bvec[i] = bvec[i] - ablock[i][0]*avec[0]
      - ablock[i][1]*avec[1]
      - ablock[i][2]*avec[2]
      - ablock[i][3]*avec[3]
      - ablock[i][4]*avec[4];
  }
}
static void matmul_sub(double ablock[5][5], double bblock[5][5],
         double cblock[5][5]) {
  int j;
  for (j = 0; j < 5; j++) {
    cblock[0][j] = cblock[0][j] - ablock[0][0]*bblock[0][j]
      - ablock[0][1]*bblock[1][j]
      - ablock[0][2]*bblock[2][j]
      - ablock[0][3]*bblock[3][j]
      - ablock[0][4]*bblock[4][j];
    cblock[1][j] = cblock[1][j] - ablock[1][0]*bblock[0][j]
      - ablock[1][1]*bblock[1][j]
      - ablock[1][2]*bblock[2][j]
      - ablock[1][3]*bblock[3][j]
      - ablock[1][4]*bblock[4][j];
    cblock[2][j] = cblock[2][j] - ablock[2][0]*bblock[0][j]
      - ablock[2][1]*bblock[1][j]
      - ablock[2][2]*bblock[2][j]
      - ablock[2][3]*bblock[3][j]
      - ablock[2][4]*bblock[4][j];
    cblock[3][j] = cblock[3][j] - ablock[3][0]*bblock[0][j]
      - ablock[3][1]*bblock[1][j]
      - ablock[3][2]*bblock[2][j]
      - ablock[3][3]*bblock[3][j]
      - ablock[3][4]*bblock[4][j];
    cblock[4][j] = cblock[4][j] - ablock[4][0]*bblock[0][j]
      - ablock[4][1]*bblock[1][j]
      - ablock[4][2]*bblock[2][j]
      - ablock[4][3]*bblock[3][j]
      - ablock[4][4]*bblock[4][j];
  }
}
static void binvcrhs(double lhs[5][5], double c[5][5], double r[5]) {
  double pivot, coeff;
  pivot = 1.00/lhs[0][0];
  lhs[0][1] = lhs[0][1]*pivot;
  lhs[0][2] = lhs[0][2]*pivot;
  lhs[0][3] = lhs[0][3]*pivot;
  lhs[0][4] = lhs[0][4]*pivot;
  c[0][0] = c[0][0]*pivot;
  c[0][1] = c[0][1]*pivot;
  c[0][2] = c[0][2]*pivot;
  c[0][3] = c[0][3]*pivot;
  c[0][4] = c[0][4]*pivot;
  r[0] = r[0] *pivot;
  coeff = lhs[1][0];
  lhs[1][1]= lhs[1][1] - coeff*lhs[0][1];
  lhs[1][2]= lhs[1][2] - coeff*lhs[0][2];
  lhs[1][3]= lhs[1][3] - coeff*lhs[0][3];
  lhs[1][4]= lhs[1][4] - coeff*lhs[0][4];
  c[1][0] = c[1][0] - coeff*c[0][0];
  c[1][1] = c[1][1] - coeff*c[0][1];
  c[1][2] = c[1][2] - coeff*c[0][2];
  c[1][3] = c[1][3] - coeff*c[0][3];
  c[1][4] = c[1][4] - coeff*c[0][4];
  r[1] = r[1] - coeff*r[0];
  coeff = lhs[2][0];
  lhs[2][1]= lhs[2][1] - coeff*lhs[0][1];
  lhs[2][2]= lhs[2][2] - coeff*lhs[0][2];
  lhs[2][3]= lhs[2][3] - coeff*lhs[0][3];
  lhs[2][4]= lhs[2][4] - coeff*lhs[0][4];
  c[2][0] = c[2][0] - coeff*c[0][0];
  c[2][1] = c[2][1] - coeff*c[0][1];
  c[2][2] = c[2][2] - coeff*c[0][2];
  c[2][3] = c[2][3] - coeff*c[0][3];
  c[2][4] = c[2][4] - coeff*c[0][4];
  r[2] = r[2] - coeff*r[0];
  coeff = lhs[3][0];
  lhs[3][1]= lhs[3][1] - coeff*lhs[0][1];
  lhs[3][2]= lhs[3][2] - coeff*lhs[0][2];
  lhs[3][3]= lhs[3][3] - coeff*lhs[0][3];
  lhs[3][4]= lhs[3][4] - coeff*lhs[0][4];
  c[3][0] = c[3][0] - coeff*c[0][0];
  c[3][1] = c[3][1] - coeff*c[0][1];
  c[3][2] = c[3][2] - coeff*c[0][2];
  c[3][3] = c[3][3] - coeff*c[0][3];
  c[3][4] = c[3][4] - coeff*c[0][4];
  r[3] = r[3] - coeff*r[0];
  coeff = lhs[4][0];
  lhs[4][1]= lhs[4][1] - coeff*lhs[0][1];
  lhs[4][2]= lhs[4][2] - coeff*lhs[0][2];
  lhs[4][3]= lhs[4][3] - coeff*lhs[0][3];
  lhs[4][4]= lhs[4][4] - coeff*lhs[0][4];
  c[4][0] = c[4][0] - coeff*c[0][0];
  c[4][1] = c[4][1] - coeff*c[0][1];
  c[4][2] = c[4][2] - coeff*c[0][2];
  c[4][3] = c[4][3] - coeff*c[0][3];
  c[4][4] = c[4][4] - coeff*c[0][4];
  r[4] = r[4] - coeff*r[0];
  pivot = 1.00/lhs[1][1];
  lhs[1][2] = lhs[1][2]*pivot;
  lhs[1][3] = lhs[1][3]*pivot;
  lhs[1][4] = lhs[1][4]*pivot;
  c[1][0] = c[1][0]*pivot;
  c[1][1] = c[1][1]*pivot;
  c[1][2] = c[1][2]*pivot;
  c[1][3] = c[1][3]*pivot;
  c[1][4] = c[1][4]*pivot;
  r[1] = r[1] *pivot;
  coeff = lhs[0][1];
  lhs[0][2]= lhs[0][2] - coeff*lhs[1][2];
  lhs[0][3]= lhs[0][3] - coeff*lhs[1][3];
  lhs[0][4]= lhs[0][4] - coeff*lhs[1][4];
  c[0][0] = c[0][0] - coeff*c[1][0];
  c[0][1] = c[0][1] - coeff*c[1][1];
  c[0][2] = c[0][2] - coeff*c[1][2];
  c[0][3] = c[0][3] - coeff*c[1][3];
  c[0][4] = c[0][4] - coeff*c[1][4];
  r[0] = r[0] - coeff*r[1];
  coeff = lhs[2][1];
  lhs[2][2]= lhs[2][2] - coeff*lhs[1][2];
  lhs[2][3]= lhs[2][3] - coeff*lhs[1][3];
  lhs[2][4]= lhs[2][4] - coeff*lhs[1][4];
  c[2][0] = c[2][0] - coeff*c[1][0];
  c[2][1] = c[2][1] - coeff*c[1][1];
  c[2][2] = c[2][2] - coeff*c[1][2];
  c[2][3] = c[2][3] - coeff*c[1][3];
  c[2][4] = c[2][4] - coeff*c[1][4];
  r[2] = r[2] - coeff*r[1];
  coeff = lhs[3][1];
  lhs[3][2]= lhs[3][2] - coeff*lhs[1][2];
  lhs[3][3]= lhs[3][3] - coeff*lhs[1][3];
  lhs[3][4]= lhs[3][4] - coeff*lhs[1][4];
  c[3][0] = c[3][0] - coeff*c[1][0];
  c[3][1] = c[3][1] - coeff*c[1][1];
  c[3][2] = c[3][2] - coeff*c[1][2];
  c[3][3] = c[3][3] - coeff*c[1][3];
  c[3][4] = c[3][4] - coeff*c[1][4];
  r[3] = r[3] - coeff*r[1];
  coeff = lhs[4][1];
  lhs[4][2]= lhs[4][2] - coeff*lhs[1][2];
  lhs[4][3]= lhs[4][3] - coeff*lhs[1][3];
  lhs[4][4]= lhs[4][4] - coeff*lhs[1][4];
  c[4][0] = c[4][0] - coeff*c[1][0];
  c[4][1] = c[4][1] - coeff*c[1][1];
  c[4][2] = c[4][2] - coeff*c[1][2];
  c[4][3] = c[4][3] - coeff*c[1][3];
  c[4][4] = c[4][4] - coeff*c[1][4];
  r[4] = r[4] - coeff*r[1];
  pivot = 1.00/lhs[2][2];
  lhs[2][3] = lhs[2][3]*pivot;
  lhs[2][4] = lhs[2][4]*pivot;
  c[2][0] = c[2][0]*pivot;
  c[2][1] = c[2][1]*pivot;
  c[2][2] = c[2][2]*pivot;
  c[2][3] = c[2][3]*pivot;
  c[2][4] = c[2][4]*pivot;
  r[2] = r[2] *pivot;
  coeff = lhs[0][2];
  lhs[0][3]= lhs[0][3] - coeff*lhs[2][3];
  lhs[0][4]= lhs[0][4] - coeff*lhs[2][4];
  c[0][0] = c[0][0] - coeff*c[2][0];
  c[0][1] = c[0][1] - coeff*c[2][1];
  c[0][2] = c[0][2] - coeff*c[2][2];
  c[0][3] = c[0][3] - coeff*c[2][3];
  c[0][4] = c[0][4] - coeff*c[2][4];
  r[0] = r[0] - coeff*r[2];
  coeff = lhs[1][2];
  lhs[1][3]= lhs[1][3] - coeff*lhs[2][3];
  lhs[1][4]= lhs[1][4] - coeff*lhs[2][4];
  c[1][0] = c[1][0] - coeff*c[2][0];
  c[1][1] = c[1][1] - coeff*c[2][1];
  c[1][2] = c[1][2] - coeff*c[2][2];
  c[1][3] = c[1][3] - coeff*c[2][3];
  c[1][4] = c[1][4] - coeff*c[2][4];
  r[1] = r[1] - coeff*r[2];
  coeff = lhs[3][2];
  lhs[3][3]= lhs[3][3] - coeff*lhs[2][3];
  lhs[3][4]= lhs[3][4] - coeff*lhs[2][4];
  c[3][0] = c[3][0] - coeff*c[2][0];
  c[3][1] = c[3][1] - coeff*c[2][1];
  c[3][2] = c[3][2] - coeff*c[2][2];
  c[3][3] = c[3][3] - coeff*c[2][3];
  c[3][4] = c[3][4] - coeff*c[2][4];
  r[3] = r[3] - coeff*r[2];
  coeff = lhs[4][2];
  lhs[4][3]= lhs[4][3] - coeff*lhs[2][3];
  lhs[4][4]= lhs[4][4] - coeff*lhs[2][4];
  c[4][0] = c[4][0] - coeff*c[2][0];
  c[4][1] = c[4][1] - coeff*c[2][1];
  c[4][2] = c[4][2] - coeff*c[2][2];
  c[4][3] = c[4][3] - coeff*c[2][3];
  c[4][4] = c[4][4] - coeff*c[2][4];
  r[4] = r[4] - coeff*r[2];
  pivot = 1.00/lhs[3][3];
  lhs[3][4] = lhs[3][4]*pivot;
  c[3][0] = c[3][0]*pivot;
  c[3][1] = c[3][1]*pivot;
  c[3][2] = c[3][2]*pivot;
  c[3][3] = c[3][3]*pivot;
  c[3][4] = c[3][4]*pivot;
  r[3] = r[3] *pivot;
  coeff = lhs[0][3];
  lhs[0][4]= lhs[0][4] - coeff*lhs[3][4];
  c[0][0] = c[0][0] - coeff*c[3][0];
  c[0][1] = c[0][1] - coeff*c[3][1];
  c[0][2] = c[0][2] - coeff*c[3][2];
  c[0][3] = c[0][3] - coeff*c[3][3];
  c[0][4] = c[0][4] - coeff*c[3][4];
  r[0] = r[0] - coeff*r[3];
  coeff = lhs[1][3];
  lhs[1][4]= lhs[1][4] - coeff*lhs[3][4];
  c[1][0] = c[1][0] - coeff*c[3][0];
  c[1][1] = c[1][1] - coeff*c[3][1];
  c[1][2] = c[1][2] - coeff*c[3][2];
  c[1][3] = c[1][3] - coeff*c[3][3];
  c[1][4] = c[1][4] - coeff*c[3][4];
  r[1] = r[1] - coeff*r[3];
  coeff = lhs[2][3];
  lhs[2][4]= lhs[2][4] - coeff*lhs[3][4];
  c[2][0] = c[2][0] - coeff*c[3][0];
  c[2][1] = c[2][1] - coeff*c[3][1];
  c[2][2] = c[2][2] - coeff*c[3][2];
  c[2][3] = c[2][3] - coeff*c[3][3];
  c[2][4] = c[2][4] - coeff*c[3][4];
  r[2] = r[2] - coeff*r[3];
  coeff = lhs[4][3];
  lhs[4][4]= lhs[4][4] - coeff*lhs[3][4];
  c[4][0] = c[4][0] - coeff*c[3][0];
  c[4][1] = c[4][1] - coeff*c[3][1];
  c[4][2] = c[4][2] - coeff*c[3][2];
  c[4][3] = c[4][3] - coeff*c[3][3];
  c[4][4] = c[4][4] - coeff*c[3][4];
  r[4] = r[4] - coeff*r[3];
  pivot = 1.00/lhs[4][4];
  c[4][0] = c[4][0]*pivot;
  c[4][1] = c[4][1]*pivot;
  c[4][2] = c[4][2]*pivot;
  c[4][3] = c[4][3]*pivot;
  c[4][4] = c[4][4]*pivot;
  r[4] = r[4] *pivot;
  coeff = lhs[0][4];
  c[0][0] = c[0][0] - coeff*c[4][0];
  c[0][1] = c[0][1] - coeff*c[4][1];
  c[0][2] = c[0][2] - coeff*c[4][2];
  c[0][3] = c[0][3] - coeff*c[4][3];
  c[0][4] = c[0][4] - coeff*c[4][4];
  r[0] = r[0] - coeff*r[4];
  coeff = lhs[1][4];
  c[1][0] = c[1][0] - coeff*c[4][0];
  c[1][1] = c[1][1] - coeff*c[4][1];
  c[1][2] = c[1][2] - coeff*c[4][2];
  c[1][3] = c[1][3] - coeff*c[4][3];
  c[1][4] = c[1][4] - coeff*c[4][4];
  r[1] = r[1] - coeff*r[4];
  coeff = lhs[2][4];
  c[2][0] = c[2][0] - coeff*c[4][0];
  c[2][1] = c[2][1] - coeff*c[4][1];
  c[2][2] = c[2][2] - coeff*c[4][2];
  c[2][3] = c[2][3] - coeff*c[4][3];
  c[2][4] = c[2][4] - coeff*c[4][4];
  r[2] = r[2] - coeff*r[4];
  coeff = lhs[3][4];
  c[3][0] = c[3][0] - coeff*c[4][0];
  c[3][1] = c[3][1] - coeff*c[4][1];
  c[3][2] = c[3][2] - coeff*c[4][2];
  c[3][3] = c[3][3] - coeff*c[4][3];
  c[3][4] = c[3][4] - coeff*c[4][4];
  r[3] = r[3] - coeff*r[4];
}
static void binvrhs( double lhs[5][5], double r[5] ) {
  double pivot, coeff;
  pivot = 1.00/lhs[0][0];
  lhs[0][1] = lhs[0][1]*pivot;
  lhs[0][2] = lhs[0][2]*pivot;
  lhs[0][3] = lhs[0][3]*pivot;
  lhs[0][4] = lhs[0][4]*pivot;
  r[0] = r[0] *pivot;
  coeff = lhs[1][0];
  lhs[1][1]= lhs[1][1] - coeff*lhs[0][1];
  lhs[1][2]= lhs[1][2] - coeff*lhs[0][2];
  lhs[1][3]= lhs[1][3] - coeff*lhs[0][3];
  lhs[1][4]= lhs[1][4] - coeff*lhs[0][4];
  r[1] = r[1] - coeff*r[0];
  coeff = lhs[2][0];
  lhs[2][1]= lhs[2][1] - coeff*lhs[0][1];
  lhs[2][2]= lhs[2][2] - coeff*lhs[0][2];
  lhs[2][3]= lhs[2][3] - coeff*lhs[0][3];
  lhs[2][4]= lhs[2][4] - coeff*lhs[0][4];
  r[2] = r[2] - coeff*r[0];
  coeff = lhs[3][0];
  lhs[3][1]= lhs[3][1] - coeff*lhs[0][1];
  lhs[3][2]= lhs[3][2] - coeff*lhs[0][2];
  lhs[3][3]= lhs[3][3] - coeff*lhs[0][3];
  lhs[3][4]= lhs[3][4] - coeff*lhs[0][4];
  r[3] = r[3] - coeff*r[0];
  coeff = lhs[4][0];
  lhs[4][1]= lhs[4][1] - coeff*lhs[0][1];
  lhs[4][2]= lhs[4][2] - coeff*lhs[0][2];
  lhs[4][3]= lhs[4][3] - coeff*lhs[0][3];
  lhs[4][4]= lhs[4][4] - coeff*lhs[0][4];
  r[4] = r[4] - coeff*r[0];
  pivot = 1.00/lhs[1][1];
  lhs[1][2] = lhs[1][2]*pivot;
  lhs[1][3] = lhs[1][3]*pivot;
  lhs[1][4] = lhs[1][4]*pivot;
  r[1] = r[1] *pivot;
  coeff = lhs[0][1];
  lhs[0][2]= lhs[0][2] - coeff*lhs[1][2];
  lhs[0][3]= lhs[0][3] - coeff*lhs[1][3];
  lhs[0][4]= lhs[0][4] - coeff*lhs[1][4];
  r[0] = r[0] - coeff*r[1];
  coeff = lhs[2][1];
  lhs[2][2]= lhs[2][2] - coeff*lhs[1][2];
  lhs[2][3]= lhs[2][3] - coeff*lhs[1][3];
  lhs[2][4]= lhs[2][4] - coeff*lhs[1][4];
  r[2] = r[2] - coeff*r[1];
  coeff = lhs[3][1];
  lhs[3][2]= lhs[3][2] - coeff*lhs[1][2];
  lhs[3][3]= lhs[3][3] - coeff*lhs[1][3];
  lhs[3][4]= lhs[3][4] - coeff*lhs[1][4];
  r[3] = r[3] - coeff*r[1];
  coeff = lhs[4][1];
  lhs[4][2]= lhs[4][2] - coeff*lhs[1][2];
  lhs[4][3]= lhs[4][3] - coeff*lhs[1][3];
  lhs[4][4]= lhs[4][4] - coeff*lhs[1][4];
  r[4] = r[4] - coeff*r[1];
  pivot = 1.00/lhs[2][2];
  lhs[2][3] = lhs[2][3]*pivot;
  lhs[2][4] = lhs[2][4]*pivot;
  r[2] = r[2] *pivot;
  coeff = lhs[0][2];
  lhs[0][3]= lhs[0][3] - coeff*lhs[2][3];
  lhs[0][4]= lhs[0][4] - coeff*lhs[2][4];
  r[0] = r[0] - coeff*r[2];
  coeff = lhs[1][2];
  lhs[1][3]= lhs[1][3] - coeff*lhs[2][3];
  lhs[1][4]= lhs[1][4] - coeff*lhs[2][4];
  r[1] = r[1] - coeff*r[2];
  coeff = lhs[3][2];
  lhs[3][3]= lhs[3][3] - coeff*lhs[2][3];
  lhs[3][4]= lhs[3][4] - coeff*lhs[2][4];
  r[3] = r[3] - coeff*r[2];
  coeff = lhs[4][2];
  lhs[4][3]= lhs[4][3] - coeff*lhs[2][3];
  lhs[4][4]= lhs[4][4] - coeff*lhs[2][4];
  r[4] = r[4] - coeff*r[2];
  pivot = 1.00/lhs[3][3];
  lhs[3][4] = lhs[3][4]*pivot;
  r[3] = r[3] *pivot;
  coeff = lhs[0][3];
  lhs[0][4]= lhs[0][4] - coeff*lhs[3][4];
  r[0] = r[0] - coeff*r[3];
  coeff = lhs[1][3];
  lhs[1][4]= lhs[1][4] - coeff*lhs[3][4];
  r[1] = r[1] - coeff*r[3];
  coeff = lhs[2][3];
  lhs[2][4]= lhs[2][4] - coeff*lhs[3][4];
  r[2] = r[2] - coeff*r[3];
  coeff = lhs[4][3];
  lhs[4][4]= lhs[4][4] - coeff*lhs[3][4];
  r[4] = r[4] - coeff*r[3];
  pivot = 1.00/lhs[4][4];
  r[4] = r[4] *pivot;
  coeff = lhs[0][4];
  r[0] = r[0] - coeff*r[4];
  coeff = lhs[1][4];
  r[1] = r[1] - coeff*r[4];
  coeff = lhs[2][4];
  r[2] = r[2] - coeff*r[4];
  coeff = lhs[3][4];
  r[3] = r[3] - coeff*r[4];
}
static void y_solve(void) {
  lhsy();
  y_solve_cell();
  y_backsubstitute();
}
static void y_backsubstitute(void) {
  int i, j, k, m, n;
  for (j = grid_points[1]-2; j >= 0; j--) {
        
#pragma omp for
    for (i = 1; i < grid_points[0]-1; i++) {
      for (k = 1; k < grid_points[2]-1; k++) {
 for (m = 0; m < 5; m++) {
   for (n = 0; n < 5; n++) {
     rhs[i][j][k][m] = rhs[i][j][k][m]
       - lhs[i][j][k][2][m][n]*rhs[i][j+1][k][n];
   }
 }
      }
    }
  }
}
static void y_solve_cell(void) {
  int i, j, k, jsize;
  jsize = grid_points[1]-1;
        
#pragma omp for
  for (i = 1; i < grid_points[0]-1; i++) {
    for (k = 1; k < grid_points[2]-1; k++) {
      binvcrhs( lhs[i][0][k][1],
  lhs[i][0][k][2],
  rhs[i][0][k] );
    }
  }
  for (j = 1; j < jsize; j++) {
        
#pragma omp for
    for (i = 1; i < grid_points[0]-1; i++) {
      for (k = 1; k < grid_points[2]-1; k++) {
 matvec_sub(lhs[i][j][k][0],
     rhs[i][j-1][k], rhs[i][j][k]);
 matmul_sub(lhs[i][j][k][0],
     lhs[i][j-1][k][2],
     lhs[i][j][k][1]);
 binvcrhs( lhs[i][j][k][1],
    lhs[i][j][k][2],
    rhs[i][j][k] );
      }
    }
  }
        
#pragma omp for
  for (i = 1; i < grid_points[0]-1; i++) {
    for (k = 1; k < grid_points[2]-1; k++) {
      matvec_sub(lhs[i][jsize][k][0],
   rhs[i][jsize-1][k], rhs[i][jsize][k]);
      matmul_sub(lhs[i][jsize][k][0],
   lhs[i][jsize-1][k][2],
   lhs[i][jsize][k][1]);
      binvrhs( lhs[i][jsize][k][1],
        rhs[i][jsize][k] );
    }
  }
}
static void z_solve(void) {
  lhsz();
  z_solve_cell();
  z_backsubstitute();
}
static void z_backsubstitute(void) {
  int i, j, k, m, n;
        
#pragma omp for
  for (i = 1; i < grid_points[0]-1; i++) {
    for (j = 1; j < grid_points[1]-1; j++) {
      for (k = grid_points[2]-2; k >= 0; k--) {
 for (m = 0; m < 5; m++) {
   for (n = 0; n < 5; n++) {
     rhs[i][j][k][m] = rhs[i][j][k][m]
       - lhs[i][j][k][2][m][n]*rhs[i][j][k+1][n];
   }
 }
      }
    }
  }
}
static void z_solve_cell(void) {
  int i,j,k,ksize;
  ksize = grid_points[2]-1;
        
#pragma omp for
  for (i = 1; i < grid_points[0]-1; i++) {
    for (j = 1; j < grid_points[1]-1; j++) {
      binvcrhs( lhs[i][j][0][1],
  lhs[i][j][0][2],
  rhs[i][j][0] );
    }
  }
  for (k = 1; k < ksize; k++) {
        
#pragma omp for
      for (i = 1; i < grid_points[0]-1; i++) {
   for (j = 1; j < grid_points[1]-1; j++) {
 matvec_sub(lhs[i][j][k][0],
     rhs[i][j][k-1], rhs[i][j][k]);
 matmul_sub(lhs[i][j][k][0],
     lhs[i][j][k-1][2],
     lhs[i][j][k][1]);
 binvcrhs( lhs[i][j][k][1],
    lhs[i][j][k][2],
    rhs[i][j][k] );
      }
    }
  }
        
#pragma omp for
  for (i = 1; i < grid_points[0]-1; i++) {
    for (j = 1; j < grid_points[1]-1; j++) {
      matvec_sub(lhs[i][j][ksize][0],
   rhs[i][j][ksize-1], rhs[i][j][ksize]);
      matmul_sub(lhs[i][j][ksize][0],
   lhs[i][j][ksize-1][2],
   lhs[i][j][ksize][1]);
      binvrhs( lhs[i][j][ksize][1],
        rhs[i][j][ksize] );
    }
  }
}
