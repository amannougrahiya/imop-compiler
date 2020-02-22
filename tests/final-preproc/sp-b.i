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
static double tx1, tx2, tx3, ty1, ty2, ty3, tz1, tz2, tz3,
              dx1, dx2, dx3, dx4, dx5, dy1, dy2, dy3, dy4,
              dy5, dz1, dz2, dz3, dz4, dz5, dssp, dt,
              ce[13][5], dxmax, dymax, dzmax, xxcon1, xxcon2,
              xxcon3, xxcon4, xxcon5, dx1tx1, dx2tx1, dx3tx1,
              dx4tx1, dx5tx1, yycon1, yycon2, yycon3, yycon4,
              yycon5, dy1ty1, dy2ty1, dy3ty1, dy4ty1, dy5ty1,
              zzcon1, zzcon2, zzcon3, zzcon4, zzcon5, dz1tz1,
              dz2tz1, dz3tz1, dz4tz1, dz5tz1, dnxm1, dnym1,
              dnzm1, c1c2, c1c5, c3c4, c1345, conz1, c1, c2,
              c3, c4, c5, c4dssp, c5dssp, dtdssp, dttx1, bt,
              dttx2, dtty1, dtty2, dttz1, dttz2, c2dttx1,
              c2dtty1, c2dttz1, comz1, comz4, comz5, comz6,
              c3c4tx3, c3c4ty3, c3c4tz3, c2iv, con43, con16;
static double u [5][102/2*2+1][102/2*2+1][102/2*2+1],
       us [102/2*2+1][102/2*2+1][102/2*2+1],
              vs [102/2*2+1][102/2*2+1][102/2*2+1],
              ws [102/2*2+1][102/2*2+1][102/2*2+1],
              qs [102/2*2+1][102/2*2+1][102/2*2+1],
              ainv [102/2*2+1][102/2*2+1][102/2*2+1],
              rho_i [102/2*2+1][102/2*2+1][102/2*2+1],
              speed [102/2*2+1][102/2*2+1][102/2*2+1],
       square [102/2*2+1][102/2*2+1][102/2*2+1],
              rhs [5][102/2*2+1][102/2*2+1][102/2*2+1],
              forcing [5][102/2*2+1][102/2*2+1][102/2*2+1],
              lhs [15][102/2*2+1][102/2*2+1][102/2*2+1];
static double cv[102], rhon[102],
              rhos[102], rhoq[102],
              cuf[102], q[102],
              ue[5][102], buf[5][102];
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
static void ninvr(void);
static void pinvr(void);
static void compute_rhs(void);
static void set_constants(void);
static void txinvr(void);
static void tzetar(void);
static void verify(int no_time_steps, char *class, boolean *verified);
static void x_solve(void);
static void y_solve(void);
static void z_solve(void);
int main(int argc, char **argv) {
  int niter, step;
  double mflops, tmax;
  int nthreads = 1;
  boolean verified;
  char class;
  FILE *fp;
  printf("\n\n NAS Parallel Benchmarks 3.0 structured OpenMP C version"
  " - SP Benchmark\n\n");
  fp = fopen("inputsp.data", "r");
  if (fp != ((void *)0)) {
      printf(" Reading from input file inputsp.data\n");
      fscanf(fp, "%d", &niter);
      while (fgetc(fp) != '\n');
      fscanf(fp, "%lf", &dt);
      while (fgetc(fp) != '\n');
      fscanf(fp, "%d%d%d",
      &grid_points[0], &grid_points[1], &grid_points[2]);
      fclose(fp);
  } else {
      printf(" No input file inputsp.data. Using compiled defaults");
      niter = 400;
      dt = 0.001;
      grid_points[0] = 102;
      grid_points[1] = 102;
      grid_points[2] = 102;
  }
  printf(" Size: %3dx%3dx%3d\n",
  grid_points[0], grid_points[1], grid_points[2]);
  printf(" Iterations: %3d   dt: %10.6f\n", niter, dt);
  if ( (grid_points[0] > 102) ||
       (grid_points[1] > 102) ||
       (grid_points[2] > 102) ) {
    printf("%d, %d, %d\n", grid_points[0], grid_points[1], grid_points[2]);
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
    if (step % 20 == 0 || step == 1) {
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
  if (tmax != 0) {
    mflops = ( 881.174 * pow((double)102, 3.0)
        - 4683.91 * (((double)102)*((double)102))
        + 11484.5 * (double)102
        - 19272.4) * (double)niter / (tmax*1000000.0);
  } else {
    mflops = 0.0;
  }
  c_print_results("SP", class, grid_points[0],
    grid_points[1], grid_points[2], niter, nthreads,
    tmax, mflops, "          floating point",
    verified, "3.0 structured", "01 Dec 2019", "kinst-ompp gcc", "kinst-ompp gcc", "-lm", "-I../common", "-O3 -fopenmp",
    "-O3 -fopenmp -fPIC", "(none)");
}
static void add(void) {
  int i, j, k, m;
#pragma omp for
  for (m = 0; m < 5; m++) {
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (j = 1; j <= grid_points[1]-2; j++) {
 for (k = 1; k <= grid_points[2]-2; k++) {
   u[m][i][j][k] = u[m][i][j][k] + rhs[m][i][j][k];
 }
      }
    }
  }
}
static void adi(void) {
  compute_rhs();
  txinvr();
  x_solve();
  y_solve();
  z_solve();
  add();
}
static void error_norm(double rms[5]) {
  int i, j, k, m, d;
  double xi, eta, zeta, u_exact[5], add;
  for (m = 0; m < 5; m++) {
    rms[m] = 0.0;
  }
  for (i = 0; i <= grid_points[0]-1; i++) {
    xi = (double)i * dnxm1;
    for (j = 0; j <= grid_points[1]-1; j++) {
      eta = (double)j * dnym1;
      for (k = 0; k <= grid_points[2]-1; k++) {
 zeta = (double)k * dnzm1;
 exact_solution(xi, eta, zeta, u_exact);
 for (m = 0; m < 5; m++) {
   add = u[m][i][j][k] - u_exact[m];
   rms[m] = rms[m] + add*add;
 }
      }
    }
  }
  for (m = 0; m < 5; m++) {
    for (d = 0; d < 3; d++) {
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
  for (i = 0; i <= grid_points[0]-2; i++) {
    for (j = 0; j <= grid_points[1]-2; j++) {
      for (k = 0; k <= grid_points[2]-2; k++) {
 for (m = 0; m < 5; m++) {
   add = rhs[m][i][j][k];
   rms[m] = rms[m] + add*add;
 }
      }
    }
  }
  for (m = 0; m < 5; m++) {
    for (d = 0; d < 3; d++) {
      rms[m] = rms[m] / (double)(grid_points[d]-2);
    }
    rms[m] = sqrt(rms[m]);
  }
}
static void exact_rhs(void) {
  double dtemp[5], xi, eta, zeta, dtpp;
  int m, i, j, k, ip1, im1, jp1, jm1, km1, kp1;
  for (m = 0; m < 5; m++) {
    for (i = 0; i <= grid_points[0]-1; i++) {
      for (j = 0; j <= grid_points[1]-1; j++) {
 for (k= 0; k <= grid_points[2]-1; k++) {
   forcing[m][i][j][k] = 0.0;
 }
      }
    }
  }
  for (k = 1; k <= grid_points[2]-2; k++) {
    zeta = (double)k * dnzm1;
    for (j = 1; j <= grid_points[1]-2; j++) {
      eta = (double)j * dnym1;
      for (i = 0; i <= grid_points[0]-1; i++) {
 xi = (double)i * dnxm1;
 exact_solution(xi, eta, zeta, dtemp);
 for (m = 0; m < 5; m++) {
   ue[m][i] = dtemp[m];
 }
 dtpp = 1.0 / dtemp[0];
 for (m = 1; m < 5; m++) {
   buf[m][i] = dtpp * dtemp[m];
 }
 cuf[i] = buf[1][i] * buf[1][i];
 buf[0][i] = cuf[i] + buf[2][i] * buf[2][i] + buf[3][i] * buf[3][i];
 q[i] = 0.5 * (buf[1][i]*ue[1][i] + buf[2][i]*ue[2][i]
        + buf[3][i]*ue[3][i]);
      }
      for (i = 1; i <= grid_points[0]-2; i++) {
 im1 = i-1;
 ip1 = i+1;
 forcing[0][i][j][k] = forcing[0][i][j][k] -
   tx2*( ue[1][ip1]-ue[1][im1] )+
   dx1tx1*(ue[0][ip1]-2.0*ue[0][i]+ue[0][im1]);
 forcing[1][i][j][k] = forcing[1][i][j][k]
   - tx2 * ((ue[1][ip1]*buf[1][ip1]+c2*(ue[4][ip1]-q[ip1]))-
                   (ue[1][im1]*buf[1][im1]+c2*(ue[4][im1]-q[im1])))+
   xxcon1*(buf[1][ip1]-2.0*buf[1][i]+buf[1][im1])+
   dx2tx1*( ue[1][ip1]-2.0* ue[1][i]+ue[1][im1]);
 forcing[2][i][j][k] = forcing[2][i][j][k]
   - tx2 * (ue[2][ip1]*buf[1][ip1]-ue[2][im1]*buf[1][im1])+
   xxcon2*(buf[2][ip1]-2.0*buf[2][i]+buf[2][im1])+
   dx3tx1*( ue[2][ip1]-2.0*ue[2][i] +ue[2][im1]);
 forcing[3][i][j][k] = forcing[3][i][j][k]
   - tx2*(ue[3][ip1]*buf[1][ip1]-ue[3][im1]*buf[1][im1])+
   xxcon2*(buf[3][ip1]-2.0*buf[3][i]+buf[3][im1])+
   dx4tx1*( ue[3][ip1]-2.0* ue[3][i]+ ue[3][im1]);
 forcing[4][i][j][k] = forcing[4][i][j][k]
   - tx2*(buf[1][ip1]*(c1*ue[4][ip1]-c2*q[ip1])-
   buf[1][im1]*(c1*ue[4][im1]-c2*q[im1]))+
   0.5*xxcon3*(buf[0][ip1]-2.0*buf[0][i]+
        buf[0][im1])+
   xxcon4*(cuf[ip1]-2.0*cuf[i]+cuf[im1])+
   xxcon5*(buf[4][ip1]-2.0*buf[4][i]+buf[4][im1])+
   dx5tx1*( ue[4][ip1]-2.0* ue[4][i]+ ue[4][im1]);
      }
      for (m = 0; m < 5; m++) {
 i = 1;
 forcing[m][i][j][k] = forcing[m][i][j][k] - dssp *
   (5.0*ue[m][i] - 4.0*ue[m][i+1] +ue[m][i+2]);
 i = 2;
 forcing[m][i][j][k] = forcing[m][i][j][k] - dssp *
   (-4.0*ue[m][i-1] + 6.0*ue[m][i] -
      4.0*ue[m][i+1] + ue[m][i+2]);
      }
      for (m = 0; m < 5; m++) {
 for (i = 3; i <= grid_points[0]-4; i++) {
   forcing[m][i][j][k] = forcing[m][i][j][k] - dssp*
     (ue[m][i-2] - 4.0*ue[m][i-1] +
      6.0*ue[m][i] - 4.0*ue[m][i+1] + ue[m][i+2]);
 }
      }
      for (m = 0; m < 5; m++) {
 i = grid_points[0]-3;
 forcing[m][i][j][k] = forcing[m][i][j][k] - dssp *
   (ue[m][i-2] - 4.0*ue[m][i-1] +
    6.0*ue[m][i] - 4.0*ue[m][i+1]);
 i = grid_points[0]-2;
 forcing[m][i][j][k] = forcing[m][i][j][k] - dssp *
   (ue[m][i-2] - 4.0*ue[m][i-1] + 5.0*ue[m][i]);
      }
    }
  }
  for (k = 1; k <= grid_points[2]-2; k++) {
    zeta = (double)k * dnzm1;
    for (i = 1; i <= grid_points[0]-2; i++) {
      xi = (double)i * dnxm1;
      for (j = 0; j <= grid_points[1]-1; j++) {
 eta = (double)j * dnym1;
 exact_solution(xi, eta, zeta, dtemp);
 for (m = 0; m < 5; m++) {
   ue[m][j] = dtemp[m];
 }
 dtpp = 1.0/dtemp[0];
 for (m = 1; m < 5; m++) {
   buf[m][j] = dtpp * dtemp[m];
 }
 cuf[j] = buf[2][j] * buf[2][j];
 buf[0][j] = cuf[j] + buf[1][j] * buf[1][j] +
   buf[3][j] * buf[3][j];
 q[j] = 0.5*(buf[1][j]*ue[1][j] + buf[2][j]*ue[2][j] +
      buf[3][j]*ue[3][j]);
      }
      for (j = 1; j <= grid_points[1]-2; j++) {
 jm1 = j-1;
 jp1 = j+1;
 forcing[0][i][j][k] = forcing[0][i][j][k] -
   ty2*( ue[2][jp1]-ue[2][jm1] )+
   dy1ty1*(ue[0][jp1]-2.0*ue[0][j]+ue[0][jm1]);
 forcing[1][i][j][k] = forcing[1][i][j][k]
   - ty2*(ue[1][jp1]*buf[2][jp1]-ue[1][jm1]*buf[2][jm1])+
   yycon2*(buf[1][jp1]-2.0*buf[1][j]+buf[1][jm1])+
   dy2ty1*( ue[1][jp1]-2.0* ue[1][j]+ ue[1][jm1]);
 forcing[2][i][j][k] = forcing[2][i][j][k]
   - ty2*((ue[2][jp1]*buf[2][jp1]+c2*(ue[4][jp1]-q[jp1]))-
   (ue[2][jm1]*buf[2][jm1]+c2*(ue[4][jm1]-q[jm1])))+
   yycon1*(buf[2][jp1]-2.0*buf[2][j]+buf[2][jm1])+
   dy3ty1*( ue[2][jp1]-2.0*ue[2][j] +ue[2][jm1]);
 forcing[3][i][j][k] = forcing[3][i][j][k]
   - ty2*(ue[3][jp1]*buf[2][jp1]-ue[3][jm1]*buf[2][jm1])+
   yycon2*(buf[3][jp1]-2.0*buf[3][j]+buf[3][jm1])+
   dy4ty1*( ue[3][jp1]-2.0*ue[3][j]+ ue[3][jm1]);
 forcing[4][i][j][k] = forcing[4][i][j][k]
   - ty2*(buf[2][jp1]*(c1*ue[4][jp1]-c2*q[jp1])-
   buf[2][jm1]*(c1*ue[4][jm1]-c2*q[jm1]))+
   0.5*yycon3*(buf[0][jp1]-2.0*buf[0][j]+
        buf[0][jm1])+
   yycon4*(cuf[jp1]-2.0*cuf[j]+cuf[jm1])+
   yycon5*(buf[4][jp1]-2.0*buf[4][j]+buf[4][jm1])+
   dy5ty1*(ue[4][jp1]-2.0*ue[4][j]+ue[4][jm1]);
      }
      for (m = 0; m < 5; m++) {
 j = 1;
 forcing[m][i][j][k] = forcing[m][i][j][k] - dssp *
   (5.0*ue[m][j] - 4.0*ue[m][j+1] +ue[m][j+2]);
 j = 2;
 forcing[m][i][j][k] = forcing[m][i][j][k] - dssp *
   (-4.0*ue[m][j-1] + 6.0*ue[m][j] -
    4.0*ue[m][j+1] + ue[m][j+2]);
      }
      for (m = 0; m < 5; m++) {
 for (j = 3; j <= grid_points[1]-4; j++) {
   forcing[m][i][j][k] = forcing[m][i][j][k] - dssp*
     (ue[m][j-2] - 4.0*ue[m][j-1] +
      6.0*ue[m][j] - 4.0*ue[m][j+1] + ue[m][j+2]);
 }
      }
      for (m = 0; m < 5; m++) {
 j = grid_points[1]-3;
 forcing[m][i][j][k] = forcing[m][i][j][k] - dssp *
   (ue[m][j-2] - 4.0*ue[m][j-1] +
    6.0*ue[m][j] - 4.0*ue[m][j+1]);
 j = grid_points[1]-2;
 forcing[m][i][j][k] = forcing[m][i][j][k] - dssp *
   (ue[m][j-2] - 4.0*ue[m][j-1] + 5.0*ue[m][j]);
      }
    }
  }
  for (j = 1; j <= grid_points[1]-2; j++) {
    eta = (double)j * dnym1;
    for (i = 1; i <= grid_points[0]-2; i++) {
      xi = (double)i * dnxm1;
      for (k = 0; k <= grid_points[2]-1; k++) {
 zeta = (double)k * dnzm1;
 exact_solution(xi, eta, zeta, dtemp);
 for (m = 0; m < 5; m++) {
   ue[m][k] = dtemp[m];
 }
 dtpp = 1.0/dtemp[0];
 for (m = 1; m < 5; m++) {
   buf[m][k] = dtpp * dtemp[m];
 }
 cuf[k] = buf[3][k] * buf[3][k];
 buf[0][k] = cuf[k] + buf[1][k] * buf[1][k] +
   buf[2][k] * buf[2][k];
 q[k] = 0.5*(buf[1][k]*ue[1][k] + buf[2][k]*ue[2][k] +
      buf[3][k]*ue[3][k]);
      }
      for (k = 1; k <= grid_points[2]-2; k++) {
 km1 = k-1;
 kp1 = k+1;
 forcing[0][i][j][k] = forcing[0][i][j][k] -
   tz2*( ue[3][kp1]-ue[3][km1] )+
   dz1tz1*(ue[0][kp1]-2.0*ue[0][k]+ue[0][km1]);
 forcing[1][i][j][k] = forcing[1][i][j][k]
   - tz2 * (ue[1][kp1]*buf[3][kp1]-ue[1][km1]*buf[3][km1])+
   zzcon2*(buf[1][kp1]-2.0*buf[1][k]+buf[1][km1])+
   dz2tz1*( ue[1][kp1]-2.0* ue[1][k]+ ue[1][km1]);
 forcing[2][i][j][k] = forcing[2][i][j][k]
   - tz2 * (ue[2][kp1]*buf[3][kp1]-ue[2][km1]*buf[3][km1])+
   zzcon2*(buf[2][kp1]-2.0*buf[2][k]+buf[2][km1])+
   dz3tz1*(ue[2][kp1]-2.0*ue[2][k]+ue[2][km1]);
 forcing[3][i][j][k] = forcing[3][i][j][k]
   - tz2 * ((ue[3][kp1]*buf[3][kp1]+c2*(ue[4][kp1]-q[kp1]))-
     (ue[3][km1]*buf[3][km1]+c2*(ue[4][km1]-q[km1])))+
   zzcon1*(buf[3][kp1]-2.0*buf[3][k]+buf[3][km1])+
   dz4tz1*( ue[3][kp1]-2.0*ue[3][k] +ue[3][km1]);
 forcing[4][i][j][k] = forcing[4][i][j][k]
   - tz2 * (buf[3][kp1]*(c1*ue[4][kp1]-c2*q[kp1])-
     buf[3][km1]*(c1*ue[4][km1]-c2*q[km1]))+
   0.5*zzcon3*(buf[0][kp1]-2.0*buf[0][k]
        +buf[0][km1])+
   zzcon4*(cuf[kp1]-2.0*cuf[k]+cuf[km1])+
   zzcon5*(buf[4][kp1]-2.0*buf[4][k]+buf[4][km1])+
   dz5tz1*( ue[4][kp1]-2.0*ue[4][k]+ ue[4][km1]);
      }
      for (m = 0; m < 5; m++) {
 k = 1;
 forcing[m][i][j][k] = forcing[m][i][j][k] - dssp *
   (5.0*ue[m][k] - 4.0*ue[m][k+1] +ue[m][k+2]);
 k = 2;
 forcing[m][i][j][k] = forcing[m][i][j][k] - dssp *
   (-4.0*ue[m][k-1] + 6.0*ue[m][k] -
    4.0*ue[m][k+1] + ue[m][k+2]);
      }
      for (m = 0; m < 5; m++) {
 for (k = 3; k <= grid_points[2]-4; k++) {
   forcing[m][i][j][k] = forcing[m][i][j][k] - dssp*
     (ue[m][k-2] - 4.0*ue[m][k-1] +
      6.0*ue[m][k] - 4.0*ue[m][k+1] + ue[m][k+2]);
 }
      }
      for (m = 0; m < 5; m++) {
 k = grid_points[2]-3;
 forcing[m][i][j][k] = forcing[m][i][j][k] - dssp *
   (ue[m][k-2] - 4.0*ue[m][k-1] +
    6.0*ue[m][k] - 4.0*ue[m][k+1]);
 k = grid_points[2]-2;
 forcing[m][i][j][k] = forcing[m][i][j][k] - dssp *
   (ue[m][k-2] - 4.0*ue[m][k-1] + 5.0*ue[m][k]);
      }
    }
  }
  for (m = 0; m < 5; m++) {
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (j = 1; j <= grid_points[1]-2; j++) {
 for (k = 1; k <= grid_points[2]-2; k++) {
   forcing[m][i][j][k] = -1.0 * forcing[m][i][j][k];
 }
      }
    }
  }
}
static void exact_solution(double xi, double eta, double zeta,
      double dtemp[5]) {
  int m;
  for (m = 0; m < 5; m++) {
    dtemp[m] = ce[0][m] +
      xi*(ce[1][m] + xi*(ce[4][m] +
        xi*(ce[7][m] + xi*ce[10][m]))) +
      eta*(ce[2][m] + eta*(ce[5][m] +
          eta*(ce[8][m] + eta*ce[11][m])))+
      zeta*(ce[3][m] + zeta*(ce[6][m] +
     zeta*(ce[9][m] +
           zeta*ce[12][m])));
  }
}
static void initialize(void) {
  int i, j, k, m, ix, iy, iz;
  double xi, eta, zeta, Pface[2][3][5], Pxi, Peta, Pzeta, temp[5];
  for (i = 0; i <= 102 -1; i++) {
    for (j = 0; j <= 102 -1; j++) {
      for (k = 0; k <= 102 -1; k++) {
 u[0][i][j][k] = 1.0;
 u[1][i][j][k] = 0.0;
 u[2][i][j][k] = 0.0;
 u[3][i][j][k] = 0.0;
 u[4][i][j][k] = 1.0;
      }
    }
  }
  for (i = 0; i <= grid_points[0]-1; i++) {
    xi = (double)i * dnxm1;
    for (j = 0; j <= grid_points[1]-1; j++) {
      eta = (double)j * dnym1;
      for (k = 0; k <= grid_points[2]-1; k++) {
 zeta = (double)k * dnzm1;
 for (ix = 0; ix < 2; ix++) {
   exact_solution((double)ix, eta, zeta,
    &Pface[ix][0][0]);
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
   u[m][i][j][k] = Pxi + Peta + Pzeta -
     Pxi*Peta - Pxi*Pzeta - Peta*Pzeta +
     Pxi*Peta*Pzeta;
 }
      }
    }
  }
  xi = 0.0;
  i = 0;
  for (j = 0; j < grid_points[1]; j++) {
    eta = (double)j * dnym1;
    for (k = 0; k < grid_points[2]; k++) {
      zeta = (double)k * dnzm1;
      exact_solution(xi, eta, zeta, temp);
      for (m = 0; m < 5; m++) {
 u[m][i][j][k] = temp[m];
      }
    }
  }
  xi = 1.0;
  i = grid_points[0]-1;
  for (j = 0; j < grid_points[1]; j++) {
    eta = (double)j * dnym1;
    for (k = 0; k < grid_points[2]; k++) {
      zeta = (double)k * dnzm1;
      exact_solution(xi, eta, zeta, temp);
      for (m = 0; m < 5; m++) {
 u[m][i][j][k] = temp[m];
      }
    }
  }
  eta = 0.0;
  j = 0;
  for (i = 0; i < grid_points[0]; i++) {
    xi = (double)i * dnxm1;
    for (k = 0; k < grid_points[2]; k++) {
      zeta = (double)k * dnzm1;
      exact_solution(xi, eta, zeta, temp);
      for (m = 0; m < 5; m++) {
 u[m][i][j][k] = temp[m];
      }
    }
  }
  eta = 1.0;
  j = grid_points[1]-1;
  for (i = 0; i < grid_points[0]; i++) {
    xi = (double)i * dnxm1;
    for (k = 0; k < grid_points[2]; k++) {
      zeta = (double)k * dnzm1;
      exact_solution(xi, eta, zeta, temp);
      for (m = 0; m < 5; m++) {
 u[m][i][j][k] = temp[m];
      }
    }
  }
  zeta = 0.0;
  k = 0;
  for (i = 0; i < grid_points[0]; i++) {
    xi = (double)i *dnxm1;
    for (j = 0; j < grid_points[1]; j++) {
      eta = (double)j * dnym1;
      exact_solution(xi, eta, zeta, temp);
      for (m = 0; m < 5; m++) {
 u[m][i][j][k] = temp[m];
      }
    }
  }
  zeta = 1.0;
  k = grid_points[2]-1;
  for (i = 0; i < grid_points[0]; i++) {
    xi = (double)i * dnxm1;
    for (j = 0; j < grid_points[1]; j++) {
      eta = (double)j * dnym1;
      exact_solution(xi, eta, zeta, temp);
      for (m = 0; m < 5; m++) {
 u[m][i][j][k] = temp[m];
      }
    }
  }
}
static void lhsinit(void) {
  int i, j, k, n;
  for (n = 0; n < 15; n++) {
#pragma omp for nowait
    for (i = 0; i < grid_points[0]; i++) {
      for (j = 0; j < grid_points[1]; j++) {
 for (k = 0; k < grid_points[2]; k++) {
   lhs[n][i][j][k] = 0.0;
 }
      }
    }
  }
#pragma omp barrier
  for (n = 0; n < 3; n++) {
#pragma omp for
    for (i = 0; i < grid_points[0]; i++) {
      for (j = 0; j < grid_points[1]; j++) {
 for (k = 0; k < grid_points[2]; k++) {
   lhs[5*n+2][i][j][k] = 1.0;
 }
      }
    }
  }
}
static void lhsx(void) {
  double ru1;
  int i, j, k;
  for (j = 1; j <= grid_points[1]-2; j++) {
    for (k = 1; k <= grid_points[2]-2; k++) {
#pragma omp for
      for (i = 0; i <= grid_points[0]-1; i++) {
 ru1 = c3c4*rho_i[i][j][k];
 cv[i] = us[i][j][k];
 rhon[i] = (((dx2+con43*ru1) > ((((dx5+c1c5*ru1) > ((((dxmax+ru1) > (dx1)) ? (dxmax+ru1) : (dx1)))) ? (dx5+c1c5*ru1) : ((((dxmax+ru1) > (dx1)) ? (dxmax+ru1) : (dx1)))))) ? (dx2+con43*ru1) : ((((dx5+c1c5*ru1) > ((((dxmax+ru1) > (dx1)) ? (dxmax+ru1) : (dx1)))) ? (dx5+c1c5*ru1) : ((((dxmax+ru1) > (dx1)) ? (dxmax+ru1) : (dx1))))));
      }
#pragma omp for
      for (i = 1; i <= grid_points[0]-2; i++) {
 lhs[0][i][j][k] = 0.0;
 lhs[1][i][j][k] = - dttx2 * cv[i-1] - dttx1 * rhon[i-1];
 lhs[2][i][j][k] = 1.0 + c2dttx1 * rhon[i];
 lhs[3][i][j][k] = dttx2 * cv[i+1] - dttx1 * rhon[i+1];
 lhs[4][i][j][k] = 0.0;
      }
    }
  }
  i = 1;
#pragma omp for nowait
  for (j = 1; j <= grid_points[1]-2; j++) {
    for (k = 1; k <= grid_points[2]-2; k++) {
      lhs[2][i][j][k] = lhs[2][i][j][k] + comz5;
      lhs[3][i][j][k] = lhs[3][i][j][k] - comz4;
      lhs[4][i][j][k] = lhs[4][i][j][k] + comz1;
      lhs[1][i+1][j][k] = lhs[1][i+1][j][k] - comz4;
      lhs[2][i+1][j][k] = lhs[2][i+1][j][k] + comz6;
      lhs[3][i+1][j][k] = lhs[3][i+1][j][k] - comz4;
      lhs[4][i+1][j][k] = lhs[4][i+1][j][k] + comz1;
    }
  }
#pragma omp for nowait
  for (i = 3; i <= grid_points[0]-4; i++) {
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 lhs[0][i][j][k] = lhs[0][i][j][k] + comz1;
 lhs[1][i][j][k] = lhs[1][i][j][k] - comz4;
 lhs[2][i][j][k] = lhs[2][i][j][k] + comz6;
 lhs[3][i][j][k] = lhs[3][i][j][k] - comz4;
 lhs[4][i][j][k] = lhs[4][i][j][k] + comz1;
      }
    }
  }
  i = grid_points[0]-3;
#pragma omp for
  for (j = 1; j <= grid_points[1]-2; j++) {
    for (k = 1; k <= grid_points[2]-2; k++) {
      lhs[0][i][j][k] = lhs[0][i][j][k] + comz1;
      lhs[1][i][j][k] = lhs[1][i][j][k] - comz4;
      lhs[2][i][j][k] = lhs[2][i][j][k] + comz6;
      lhs[3][i][j][k] = lhs[3][i][j][k] - comz4;
      lhs[0][i+1][j][k] = lhs[0][i+1][j][k] + comz1;
      lhs[1][i+1][j][k] = lhs[1][i+1][j][k] - comz4;
      lhs[2][i+1][j][k] = lhs[2][i+1][j][k] + comz5;
    }
  }
#pragma omp for
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 lhs[0+5][i][j][k] = lhs[0][i][j][k];
 lhs[1+5][i][j][k] = lhs[1][i][j][k] -
   dttx2 * speed[i-1][j][k];
 lhs[2+5][i][j][k] = lhs[2][i][j][k];
 lhs[3+5][i][j][k] = lhs[3][i][j][k] +
   dttx2 * speed[i+1][j][k];
 lhs[4+5][i][j][k] = lhs[4][i][j][k];
 lhs[0+10][i][j][k] = lhs[0][i][j][k];
 lhs[1+10][i][j][k] = lhs[1][i][j][k] +
   dttx2 * speed[i-1][j][k];
 lhs[2+10][i][j][k] = lhs[2][i][j][k];
 lhs[3+10][i][j][k] = lhs[3][i][j][k] -
   dttx2 * speed[i+1][j][k];
 lhs[4+10][i][j][k] = lhs[4][i][j][k];
      }
    }
  }
}
static void lhsy(void) {
  double ru1;
  int i, j, k;
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (k = 1; k <= grid_points[2]-2; k++) {
#pragma omp for
      for (j = 0; j <= grid_points[1]-1; j++) {
 ru1 = c3c4*rho_i[i][j][k];
 cv[j] = vs[i][j][k];
 rhoq[j] = (((dy3 + con43 * ru1) > ((((dy5 + c1c5*ru1) > ((((dymax + ru1) > (dy1)) ? (dymax + ru1) : (dy1)))) ? (dy5 + c1c5*ru1) : ((((dymax + ru1) > (dy1)) ? (dymax + ru1) : (dy1)))))) ? (dy3 + con43 * ru1) : ((((dy5 + c1c5*ru1) > ((((dymax + ru1) > (dy1)) ? (dymax + ru1) : (dy1)))) ? (dy5 + c1c5*ru1) : ((((dymax + ru1) > (dy1)) ? (dymax + ru1) : (dy1))))));
      }
#pragma omp for
      for (j = 1; j <= grid_points[1]-2; j++) {
 lhs[0][i][j][k] = 0.0;
 lhs[1][i][j][k] = -dtty2 * cv[j-1] - dtty1 * rhoq[j-1];
 lhs[2][i][j][k] = 1.0 + c2dtty1 * rhoq[j];
 lhs[3][i][j][k] = dtty2 * cv[j+1] - dtty1 * rhoq[j+1];
 lhs[4][i][j][k] = 0.0;
      }
    }
  }
  j = 1;
#pragma omp for nowait
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (k = 1; k <= grid_points[2]-2; k++) {
      lhs[2][i][j][k] = lhs[2][i][j][k] + comz5;
      lhs[3][i][j][k] = lhs[3][i][j][k] - comz4;
      lhs[4][i][j][k] = lhs[4][i][j][k] + comz1;
      lhs[1][i][j+1][k] = lhs[1][i][j+1][k] - comz4;
      lhs[2][i][j+1][k] = lhs[2][i][j+1][k] + comz6;
      lhs[3][i][j+1][k] = lhs[3][i][j+1][k] - comz4;
      lhs[4][i][j+1][k] = lhs[4][i][j+1][k] + comz1;
    }
  }
#pragma omp for nowait
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (j = 3; j <= grid_points[1]-4; j++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 lhs[0][i][j][k] = lhs[0][i][j][k] + comz1;
 lhs[1][i][j][k] = lhs[1][i][j][k] - comz4;
 lhs[2][i][j][k] = lhs[2][i][j][k] + comz6;
 lhs[3][i][j][k] = lhs[3][i][j][k] - comz4;
 lhs[4][i][j][k] = lhs[4][i][j][k] + comz1;
      }
    }
  }
  j = grid_points[1]-3;
#pragma omp for
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (k = 1; k <= grid_points[2]-2; k++) {
      lhs[0][i][j][k] = lhs[0][i][j][k] + comz1;
      lhs[1][i][j][k] = lhs[1][i][j][k] - comz4;
      lhs[2][i][j][k] = lhs[2][i][j][k] + comz6;
      lhs[3][i][j][k] = lhs[3][i][j][k] - comz4;
      lhs[0][i][j+1][k] = lhs[0][i][j+1][k] + comz1;
      lhs[1][i][j+1][k] = lhs[1][i][j+1][k] - comz4;
      lhs[2][i][j+1][k] = lhs[2][i][j+1][k] + comz5;
    }
  }
#pragma omp for
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 lhs[0+5][i][j][k] = lhs[0][i][j][k];
 lhs[1+5][i][j][k] = lhs[1][i][j][k] -
   dtty2 * speed[i][j-1][k];
 lhs[2+5][i][j][k] = lhs[2][i][j][k];
 lhs[3+5][i][j][k] = lhs[3][i][j][k] +
   dtty2 * speed[i][j+1][k];
 lhs[4+5][i][j][k] = lhs[4][i][j][k];
 lhs[0+10][i][j][k] = lhs[0][i][j][k];
 lhs[1+10][i][j][k] = lhs[1][i][j][k] +
   dtty2 * speed[i][j-1][k];
 lhs[2+10][i][j][k] = lhs[2][i][j][k];
 lhs[3+10][i][j][k] = lhs[3][i][j][k] -
   dtty2 * speed[i][j+1][k];
 lhs[4+10][i][j][k] = lhs[4][i][j][k];
      }
    }
  }
}
static void lhsz(void) {
  double ru1;
  int i, j, k;
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (j = 1; j <= grid_points[1]-2; j++) {
#pragma omp for
      for (k = 0; k <= grid_points[2]-1; k++) {
 ru1 = c3c4*rho_i[i][j][k];
 cv[k] = ws[i][j][k];
 rhos[k] = (((dz4 + con43 * ru1) > ((((dz5 + c1c5 * ru1) > ((((dzmax + ru1) > (dz1)) ? (dzmax + ru1) : (dz1)))) ? (dz5 + c1c5 * ru1) : ((((dzmax + ru1) > (dz1)) ? (dzmax + ru1) : (dz1)))))) ? (dz4 + con43 * ru1) : ((((dz5 + c1c5 * ru1) > ((((dzmax + ru1) > (dz1)) ? (dzmax + ru1) : (dz1)))) ? (dz5 + c1c5 * ru1) : ((((dzmax + ru1) > (dz1)) ? (dzmax + ru1) : (dz1))))));
      }
#pragma omp for
      for (k = 1; k <= grid_points[2]-2; k++) {
 lhs[0][i][j][k] = 0.0;
 lhs[1][i][j][k] = -dttz2 * cv[k-1] - dttz1 * rhos[k-1];
 lhs[2][i][j][k] = 1.0 + c2dttz1 * rhos[k];
 lhs[3][i][j][k] = dttz2 * cv[k+1] - dttz1 * rhos[k+1];
 lhs[4][i][j][k] = 0.0;
      }
    }
  }
  k = 1;
#pragma omp for nowait
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (j = 1; j <= grid_points[1]-2; j++) {
      lhs[2][i][j][k] = lhs[2][i][j][k] + comz5;
      lhs[3][i][j][k] = lhs[3][i][j][k] - comz4;
      lhs[4][i][j][k] = lhs[4][i][j][k] + comz1;
      lhs[1][i][j][k+1] = lhs[1][i][j][k+1] - comz4;
      lhs[2][i][j][k+1] = lhs[2][i][j][k+1] + comz6;
      lhs[3][i][j][k+1] = lhs[3][i][j][k+1] - comz4;
      lhs[4][i][j][k+1] = lhs[4][i][j][k+1] + comz1;
    }
  }
#pragma omp for nowait
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 3; k <= grid_points[2]-4; k++) {
 lhs[0][i][j][k] = lhs[0][i][j][k] + comz1;
 lhs[1][i][j][k] = lhs[1][i][j][k] - comz4;
 lhs[2][i][j][k] = lhs[2][i][j][k] + comz6;
 lhs[3][i][j][k] = lhs[3][i][j][k] - comz4;
 lhs[4][i][j][k] = lhs[4][i][j][k] + comz1;
      }
    }
  }
  k = grid_points[2]-3;
#pragma omp for
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (j = 1; j <= grid_points[1]-2; j++) {
      lhs[0][i][j][k] = lhs[0][i][j][k] + comz1;
      lhs[1][i][j][k] = lhs[1][i][j][k] - comz4;
      lhs[2][i][j][k] = lhs[2][i][j][k] + comz6;
      lhs[3][i][j][k] = lhs[3][i][j][k] - comz4;
      lhs[0][i][j][k+1] = lhs[0][i][j][k+1] + comz1;
      lhs[1][i][j][k+1] = lhs[1][i][j][k+1] - comz4;
      lhs[2][i][j][k+1] = lhs[2][i][j][k+1] + comz5;
    }
  }
#pragma omp for
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 lhs[0+5][i][j][k] = lhs[0][i][j][k];
 lhs[1+5][i][j][k] = lhs[1][i][j][k] -
   dttz2 * speed[i][j][k-1];
 lhs[2+5][i][j][k] = lhs[2][i][j][k];
 lhs[3+5][i][j][k] = lhs[3][i][j][k] +
   dttz2 * speed[i][j][k+1];
 lhs[4+5][i][j][k] = lhs[4][i][j][k];
 lhs[0+10][i][j][k] = lhs[0][i][j][k];
 lhs[1+10][i][j][k] = lhs[1][i][j][k] +
   dttz2 * speed[i][j][k-1];
 lhs[2+10][i][j][k] = lhs[2][i][j][k];
 lhs[3+10][i][j][k] = lhs[3][i][j][k] -
   dttz2 * speed[i][j][k+1];
 lhs[4+10][i][j][k] = lhs[4][i][j][k];
      }
    }
  }
}
static void ninvr(void) {
  int i, j, k;
  double r1, r2, r3, r4, r5, t1, t2;
#pragma omp parallel for default(shared) private(i,j,k,r1,r2,r3,r4,r5,t1,t2)
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 r1 = rhs[0][i][j][k];
 r2 = rhs[1][i][j][k];
 r3 = rhs[2][i][j][k];
 r4 = rhs[3][i][j][k];
 r5 = rhs[4][i][j][k];
 t1 = bt * r3;
 t2 = 0.5 * ( r4 + r5 );
 rhs[0][i][j][k] = -r2;
 rhs[1][i][j][k] = r1;
 rhs[2][i][j][k] = bt * ( r4 - r5 );
 rhs[3][i][j][k] = -t1 + t2;
 rhs[4][i][j][k] = t1 + t2;
      }
    }
  }
}
static void pinvr(void) {
  int i, j, k;
  double r1, r2, r3, r4, r5, t1, t2;
#pragma omp parallel for default(shared) private(i,j,k,r1,r2,r3,r4,r5,t1,t2)
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 r1 = rhs[0][i][j][k];
 r2 = rhs[1][i][j][k];
 r3 = rhs[2][i][j][k];
 r4 = rhs[3][i][j][k];
 r5 = rhs[4][i][j][k];
 t1 = bt * r1;
 t2 = 0.5 * ( r4 + r5 );
 rhs[0][i][j][k] = bt * ( r4 - r5 );
 rhs[1][i][j][k] = -r3;
 rhs[2][i][j][k] = r2;
 rhs[3][i][j][k] = -t1 + t2;
 rhs[4][i][j][k] = t1 + t2;
      }
    }
  }
}
static void compute_rhs(void) {
#pragma omp parallel
{
  int i, j, k, m;
  double aux, rho_inv, uijk, up1, um1, vijk, vp1, vm1,
    wijk, wp1, wm1;
#pragma omp for nowait
  for (i = 0; i <= grid_points[0]-1; i++) {
    for (j = 0; j <= grid_points[1]-1; j++) {
      for (k = 0; k <= grid_points[2]-1; k++) {
 rho_inv = 1.0/u[0][i][j][k];
 rho_i[i][j][k] = rho_inv;
 us[i][j][k] = u[1][i][j][k] * rho_inv;
 vs[i][j][k] = u[2][i][j][k] * rho_inv;
 ws[i][j][k] = u[3][i][j][k] * rho_inv;
 square[i][j][k] = 0.5* (u[1][i][j][k]*u[1][i][j][k] +
    u[2][i][j][k]*u[2][i][j][k] +
    u[3][i][j][k]*u[3][i][j][k] ) * rho_inv;
 qs[i][j][k] = square[i][j][k] * rho_inv;
 aux = c1c2*rho_inv* (u[4][i][j][k] - square[i][j][k]);
 aux = sqrt(aux);
 speed[i][j][k] = aux;
 ainv[i][j][k] = 1.0/aux;
      }
    }
  }
  for (m = 0; m < 5; m++) {
#pragma omp for
    for (i = 0; i <= grid_points[0]-1; i++) {
      for (j = 0; j <= grid_points[1]-1; j++) {
 for (k = 0; k <= grid_points[2]-1; k++) {
   rhs[m][i][j][k] = forcing[m][i][j][k];
 }
      }
    }
  }
#pragma omp for
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 uijk = us[i][j][k];
 up1 = us[i+1][j][k];
 um1 = us[i-1][j][k];
 rhs[0][i][j][k] = rhs[0][i][j][k] + dx1tx1 *
   (u[0][i+1][j][k] - 2.0*u[0][i][j][k] +
    u[0][i-1][j][k]) -
   tx2 * (u[1][i+1][j][k] - u[1][i-1][j][k]);
 rhs[1][i][j][k] = rhs[1][i][j][k] + dx2tx1 *
   (u[1][i+1][j][k] - 2.0*u[1][i][j][k] +
    u[1][i-1][j][k]) +
   xxcon2*con43 * (up1 - 2.0*uijk + um1) -
   tx2 * (u[1][i+1][j][k]*up1 -
   u[1][i-1][j][k]*um1 +
   (u[4][i+1][j][k]- square[i+1][j][k]-
    u[4][i-1][j][k]+ square[i-1][j][k])*
   c2);
 rhs[2][i][j][k] = rhs[2][i][j][k] + dx3tx1 *
   (u[2][i+1][j][k] - 2.0*u[2][i][j][k] +
    u[2][i-1][j][k]) +
   xxcon2 * (vs[i+1][j][k] - 2.0*vs[i][j][k] +
      vs[i-1][j][k]) -
   tx2 * (u[2][i+1][j][k]*up1 -
   u[2][i-1][j][k]*um1);
 rhs[3][i][j][k] = rhs[3][i][j][k] + dx4tx1 *
   (u[3][i+1][j][k] - 2.0*u[3][i][j][k] +
    u[3][i-1][j][k]) +
   xxcon2 * (ws[i+1][j][k] - 2.0*ws[i][j][k] +
      ws[i-1][j][k]) -
   tx2 * (u[3][i+1][j][k]*up1 -
   u[3][i-1][j][k]*um1);
 rhs[4][i][j][k] = rhs[4][i][j][k] + dx5tx1 *
   (u[4][i+1][j][k] - 2.0*u[4][i][j][k] +
    u[4][i-1][j][k]) +
   xxcon3 * (qs[i+1][j][k] - 2.0*qs[i][j][k] +
      qs[i-1][j][k]) +
   xxcon4 * (up1*up1 - 2.0*uijk*uijk +
      um1*um1) +
   xxcon5 * (u[4][i+1][j][k]*rho_i[i+1][j][k] -
      2.0*u[4][i][j][k]*rho_i[i][j][k] +
      u[4][i-1][j][k]*rho_i[i-1][j][k]) -
   tx2 * ( (c1*u[4][i+1][j][k] -
     c2*square[i+1][j][k])*up1 -
    (c1*u[4][i-1][j][k] -
     c2*square[i-1][j][k])*um1 );
      }
    }
  }
  i = 1;
  for (m = 0; m < 5; m++) {
#pragma omp for
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 rhs[m][i][j][k] = rhs[m][i][j][k]- dssp *
   ( 5.0*u[m][i][j][k] - 4.0*u[m][i+1][j][k] +
     u[m][i+2][j][k]);
      }
    }
  }
  i = 2;
  for (m = 0; m < 5; m++) {
#pragma omp for
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 rhs[m][i][j][k] = rhs[m][i][j][k] - dssp *
   (-4.0*u[m][i-1][j][k] + 6.0*u[m][i][j][k] -
    4.0*u[m][i+1][j][k] + u[m][i+2][j][k]);
      }
    }
  }
  for (m = 0; m < 5; m++) {
#pragma omp for
    for (i = 3*1; i <= grid_points[0]-3*1-1; i++) {
      for (j = 1; j <= grid_points[1]-2; j++) {
 for (k = 1; k <= grid_points[2]-2; k++) {
   rhs[m][i][j][k] = rhs[m][i][j][k] - dssp *
     ( u[m][i-2][j][k] - 4.0*u[m][i-1][j][k] +
        6.0*u[m][i][j][k] - 4.0*u[m][i+1][j][k] +
        u[m][i+2][j][k] );
 }
      }
    }
  }
  i = grid_points[0]-3;
  for (m = 0; m < 5; m++) {
#pragma omp for
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 rhs[m][i][j][k] = rhs[m][i][j][k] - dssp *
   ( u[m][i-2][j][k] - 4.0*u[m][i-1][j][k] +
     6.0*u[m][i][j][k] - 4.0*u[m][i+1][j][k] );
      }
    }
  }
  i = grid_points[0]-2;
  for (m = 0; m < 5; m++) {
#pragma omp for
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 rhs[m][i][j][k] = rhs[m][i][j][k] - dssp *
   ( u[m][i-2][j][k] - 4.0*u[m][i-1][j][k] +
     5.0*u[m][i][j][k] );
      }
    }
  }
#pragma omp barrier
#pragma omp for
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 vijk = vs[i][j][k];
 vp1 = vs[i][j+1][k];
 vm1 = vs[i][j-1][k];
 rhs[0][i][j][k] = rhs[0][i][j][k] + dy1ty1 *
   (u[0][i][j+1][k] - 2.0*u[0][i][j][k] +
    u[0][i][j-1][k]) -
   ty2 * (u[2][i][j+1][k] - u[2][i][j-1][k]);
 rhs[1][i][j][k] = rhs[1][i][j][k] + dy2ty1 *
   (u[1][i][j+1][k] - 2.0*u[1][i][j][k] +
    u[1][i][j-1][k]) +
   yycon2 * (us[i][j+1][k] - 2.0*us[i][j][k] +
      us[i][j-1][k]) -
   ty2 * (u[1][i][j+1][k]*vp1 -
   u[1][i][j-1][k]*vm1);
 rhs[2][i][j][k] = rhs[2][i][j][k] + dy3ty1 *
   (u[2][i][j+1][k] - 2.0*u[2][i][j][k] +
    u[2][i][j-1][k]) +
   yycon2*con43 * (vp1 - 2.0*vijk + vm1) -
   ty2 * (u[2][i][j+1][k]*vp1 -
   u[2][i][j-1][k]*vm1 +
   (u[4][i][j+1][k] - square[i][j+1][k] -
    u[4][i][j-1][k] + square[i][j-1][k])
   *c2);
 rhs[3][i][j][k] = rhs[3][i][j][k] + dy4ty1 *
   (u[3][i][j+1][k] - 2.0*u[3][i][j][k] +
    u[3][i][j-1][k]) +
   yycon2 * (ws[i][j+1][k] - 2.0*ws[i][j][k] +
      ws[i][j-1][k]) -
   ty2 * (u[3][i][j+1][k]*vp1 -
   u[3][i][j-1][k]*vm1);
 rhs[4][i][j][k] = rhs[4][i][j][k] + dy5ty1 *
   (u[4][i][j+1][k] - 2.0*u[4][i][j][k] +
    u[4][i][j-1][k]) +
   yycon3 * (qs[i][j+1][k] - 2.0*qs[i][j][k] +
      qs[i][j-1][k]) +
   yycon4 * (vp1*vp1 - 2.0*vijk*vijk +
      vm1*vm1) +
   yycon5 * (u[4][i][j+1][k]*rho_i[i][j+1][k] -
      2.0*u[4][i][j][k]*rho_i[i][j][k] +
      u[4][i][j-1][k]*rho_i[i][j-1][k]) -
   ty2 * ((c1*u[4][i][j+1][k] -
    c2*square[i][j+1][k]) * vp1 -
   (c1*u[4][i][j-1][k] -
    c2*square[i][j-1][k]) * vm1);
      }
    }
  }
  j = 1;
  for (m = 0; m < 5; m++) {
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 rhs[m][i][j][k] = rhs[m][i][j][k]- dssp *
   ( 5.0*u[m][i][j][k] - 4.0*u[m][i][j+1][k] +
     u[m][i][j+2][k]);
      }
    }
  }
  j = 2;
  for (m = 0; m < 5; m++) {
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 rhs[m][i][j][k] = rhs[m][i][j][k] - dssp *
   (-4.0*u[m][i][j-1][k] + 6.0*u[m][i][j][k] -
    4.0*u[m][i][j+1][k] + u[m][i][j+2][k]);
      }
    }
  }
  for (m = 0; m < 5; m++) {
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (j = 3*1; j <= grid_points[1]-3*1-1; j++) {
 for (k = 1; k <= grid_points[2]-2; k++) {
   rhs[m][i][j][k] = rhs[m][i][j][k] - dssp *
     ( u[m][i][j-2][k] - 4.0*u[m][i][j-1][k] +
        6.0*u[m][i][j][k] - 4.0*u[m][i][j+1][k] +
        u[m][i][j+2][k] );
 }
      }
    }
  }
  j = grid_points[1]-3;
  for (m = 0; m < 5; m++) {
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 rhs[m][i][j][k] = rhs[m][i][j][k] - dssp *
   ( u[m][i][j-2][k] - 4.0*u[m][i][j-1][k] +
     6.0*u[m][i][j][k] - 4.0*u[m][i][j+1][k] );
      }
    }
  }
  j = grid_points[1]-2;
  for (m = 0; m < 5; m++) {
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 rhs[m][i][j][k] = rhs[m][i][j][k] - dssp *
   ( u[m][i][j-2][k] - 4.0*u[m][i][j-1][k] +
     5.0*u[m][i][j][k] );
      }
    }
  }
#pragma omp barrier
#pragma omp for
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 wijk = ws[i][j][k];
 wp1 = ws[i][j][k+1];
 wm1 = ws[i][j][k-1];
 rhs[0][i][j][k] = rhs[0][i][j][k] + dz1tz1 *
   (u[0][i][j][k+1] - 2.0*u[0][i][j][k] +
    u[0][i][j][k-1]) -
   tz2 * (u[3][i][j][k+1] - u[3][i][j][k-1]);
 rhs[1][i][j][k] = rhs[1][i][j][k] + dz2tz1 *
   (u[1][i][j][k+1] - 2.0*u[1][i][j][k] +
    u[1][i][j][k-1]) +
   zzcon2 * (us[i][j][k+1] - 2.0*us[i][j][k] +
      us[i][j][k-1]) -
   tz2 * (u[1][i][j][k+1]*wp1 -
   u[1][i][j][k-1]*wm1);
 rhs[2][i][j][k] = rhs[2][i][j][k] + dz3tz1 *
   (u[2][i][j][k+1] - 2.0*u[2][i][j][k] +
    u[2][i][j][k-1]) +
   zzcon2 * (vs[i][j][k+1] - 2.0*vs[i][j][k] +
      vs[i][j][k-1]) -
   tz2 * (u[2][i][j][k+1]*wp1 -
   u[2][i][j][k-1]*wm1);
 rhs[3][i][j][k] = rhs[3][i][j][k] + dz4tz1 *
   (u[3][i][j][k+1] - 2.0*u[3][i][j][k] +
    u[3][i][j][k-1]) +
   zzcon2*con43 * (wp1 - 2.0*wijk + wm1) -
   tz2 * (u[3][i][j][k+1]*wp1 -
   u[3][i][j][k-1]*wm1 +
   (u[4][i][j][k+1] - square[i][j][k+1] -
    u[4][i][j][k-1] + square[i][j][k-1])
   *c2);
 rhs[4][i][j][k] = rhs[4][i][j][k] + dz5tz1 *
   (u[4][i][j][k+1] - 2.0*u[4][i][j][k] +
    u[4][i][j][k-1]) +
   zzcon3 * (qs[i][j][k+1] - 2.0*qs[i][j][k] +
      qs[i][j][k-1]) +
   zzcon4 * (wp1*wp1 - 2.0*wijk*wijk +
      wm1*wm1) +
   zzcon5 * (u[4][i][j][k+1]*rho_i[i][j][k+1] -
      2.0*u[4][i][j][k]*rho_i[i][j][k] +
      u[4][i][j][k-1]*rho_i[i][j][k-1]) -
   tz2 * ( (c1*u[4][i][j][k+1] -
     c2*square[i][j][k+1])*wp1 -
    (c1*u[4][i][j][k-1] -
     c2*square[i][j][k-1])*wm1);
      }
    }
  }
  k = 1;
  for (m = 0; m < 5; m++) {
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (j = 1; j <= grid_points[1]-2; j++) {
 rhs[m][i][j][k] = rhs[m][i][j][k]- dssp *
   ( 5.0*u[m][i][j][k] - 4.0*u[m][i][j][k+1] +
     u[m][i][j][k+2]);
      }
    }
  }
  k = 2;
  for (m = 0; m < 5; m++) {
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (j = 1; j <= grid_points[1]-2; j++) {
 rhs[m][i][j][k] = rhs[m][i][j][k] - dssp *
   (-4.0*u[m][i][j][k-1] + 6.0*u[m][i][j][k] -
    4.0*u[m][i][j][k+1] + u[m][i][j][k+2]);
      }
    }
  }
  for (m = 0; m < 5; m++) {
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (j = 1; j <= grid_points[1]-2; j++) {
 for (k = 3*1; k <= grid_points[2]-3*1-1; k++) {
   rhs[m][i][j][k] = rhs[m][i][j][k] - dssp *
     ( u[m][i][j][k-2] - 4.0*u[m][i][j][k-1] +
        6.0*u[m][i][j][k] - 4.0*u[m][i][j][k+1] +
        u[m][i][j][k+2] );
 }
      }
    }
  }
  k = grid_points[2]-3;
  for (m = 0; m < 5; m++) {
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (j = 1; j <= grid_points[1]-2; j++) {
 rhs[m][i][j][k] = rhs[m][i][j][k] - dssp *
   ( u[m][i][j][k-2] - 4.0*u[m][i][j][k-1] +
     6.0*u[m][i][j][k] - 4.0*u[m][i][j][k+1] );
      }
    }
  }
  k = grid_points[2]-2;
  for (m = 0; m < 5; m++) {
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (j = 1; j <= grid_points[1]-2; j++) {
 rhs[m][i][j][k] = rhs[m][i][j][k] - dssp *
   ( u[m][i][j][k-2] - 4.0*u[m][i][j][k-1] +
     5.0*u[m][i][j][k] );
      }
    }
  }
  for (m = 0; m < 5; m++) {
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (j = 1; j <= grid_points[1]-2; j++) {
 for (k = 1; k <= grid_points[2]-2; k++) {
   rhs[m][i][j][k] = rhs[m][i][j][k] * dt;
 }
      }
    }
  }
}
}
static void set_constants(void) {
  ce[0][0] = 2.0;
  ce[1][0] = 0.0;
  ce[2][0] = 0.0;
  ce[3][0] = 4.0;
  ce[4][0] = 5.0;
  ce[5][0] = 3.0;
  ce[6][0] = 0.5;
  ce[7][0] = 0.02;
  ce[8][0] = 0.01;
  ce[9][0] = 0.03;
  ce[10][0] = 0.5;
  ce[11][0] = 0.4;
  ce[12][0] = 0.3;
  ce[0][1] = 1.0;
  ce[1][1] = 0.0;
  ce[2][1] = 0.0;
  ce[3][1] = 0.0;
  ce[4][1] = 1.0;
  ce[5][1] = 2.0;
  ce[6][1] = 3.0;
  ce[7][1] = 0.01;
  ce[8][1] = 0.03;
  ce[9][1] = 0.02;
  ce[10][1] = 0.4;
  ce[11][1] = 0.3;
  ce[12][1] = 0.5;
  ce[0][2] = 2.0;
  ce[1][2] = 2.0;
  ce[2][2] = 0.0;
  ce[3][2] = 0.0;
  ce[4][2] = 0.0;
  ce[5][2] = 2.0;
  ce[6][2] = 3.0;
  ce[7][2] = 0.04;
  ce[8][2] = 0.03;
  ce[9][2] = 0.05;
  ce[10][2] = 0.3;
  ce[11][2] = 0.5;
  ce[12][2] = 0.4;
  ce[0][3] = 2.0;
  ce[1][3] = 2.0;
  ce[2][3] = 0.0;
  ce[3][3] = 0.0;
  ce[4][3] = 0.0;
  ce[5][3] = 2.0;
  ce[6][3] = 3.0;
  ce[7][3] = 0.03;
  ce[8][3] = 0.05;
  ce[9][3] = 0.04;
  ce[10][3] = 0.2;
  ce[11][3] = 0.1;
  ce[12][3] = 0.3;
  ce[0][4] = 5.0;
  ce[1][4] = 4.0;
  ce[2][4] = 3.0;
  ce[3][4] = 2.0;
  ce[4][4] = 0.1;
  ce[5][4] = 0.4;
  ce[6][4] = 0.3;
  ce[7][4] = 0.05;
  ce[8][4] = 0.04;
  ce[9][4] = 0.03;
  ce[10][4] = 0.1;
  ce[11][4] = 0.3;
  ce[12][4] = 0.2;
  c1 = 1.4;
  c2 = 0.4;
  c3 = 0.1;
  c4 = 1.0;
  c5 = 1.4;
  bt = sqrt(0.5);
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
static void txinvr(void) {
  int i, j, k;
  double t1, t2, t3, ac, ru1, uu, vv, ww, r1, r2, r3,
    r4, r5, ac2inv;
#pragma omp for
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 ru1 = rho_i[i][j][k];
 uu = us[i][j][k];
 vv = vs[i][j][k];
 ww = ws[i][j][k];
 ac = speed[i][j][k];
 ac2inv = ainv[i][j][k]*ainv[i][j][k];
 r1 = rhs[0][i][j][k];
 r2 = rhs[1][i][j][k];
 r3 = rhs[2][i][j][k];
 r4 = rhs[3][i][j][k];
 r5 = rhs[4][i][j][k];
 t1 = c2 * ac2inv * ( qs[i][j][k]*r1 - uu*r2 -
        vv*r3 - ww*r4 + r5 );
 t2 = bt * ru1 * ( uu * r1 - r2 );
 t3 = ( bt * ru1 * ac ) * t1;
 rhs[0][i][j][k] = r1 - t1;
 rhs[1][i][j][k] = - ru1 * ( ww*r1 - r4 );
 rhs[2][i][j][k] = ru1 * ( vv*r1 - r3 );
 rhs[3][i][j][k] = - t2 + t3;
 rhs[4][i][j][k] = t2 + t3;
      }
    }
  }
}
static void tzetar(void) {
  int i, j, k;
  double t1, t2, t3, ac, xvel, yvel, zvel, r1, r2, r3,
    r4, r5, btuz, acinv, ac2u, uzik1;
#pragma omp for private(i,j,k,t1,t2,t3,ac,xvel,yvel,zvel,r1,r2,r3,r4,r5,btuz,ac2u,uzik1)
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 xvel = us[i][j][k];
 yvel = vs[i][j][k];
 zvel = ws[i][j][k];
 ac = speed[i][j][k];
 acinv = ainv[i][j][k];
 ac2u = ac*ac;
 r1 = rhs[0][i][j][k];
 r2 = rhs[1][i][j][k];
 r3 = rhs[2][i][j][k];
 r4 = rhs[3][i][j][k];
 r5 = rhs[4][i][j][k];
 uzik1 = u[0][i][j][k];
 btuz = bt * uzik1;
 t1 = btuz*acinv * (r4 + r5);
 t2 = r3 + t1;
 t3 = btuz * (r4 - r5);
 rhs[0][i][j][k] = t2;
 rhs[1][i][j][k] = -uzik1*r2 + xvel*t2;
 rhs[2][i][j][k] = uzik1*r1 + yvel*t2;
 rhs[3][i][j][k] = zvel*t2 + t3;
 rhs[4][i][j][k] = uzik1*(-xvel*r2 + yvel*r1) +
   qs[i][j][k]*t2 + c2iv*ac2u*t1 + zvel*t3;
      }
    }
  }
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
  if ( grid_points[0] == 12 &&
       grid_points[1] == 12 &&
       grid_points[2] == 12 &&
       no_time_steps == 100) {
    *class = 'S';
    dtref = 1.5e-2;
    xcrref[0] = 2.7470315451339479e-02;
    xcrref[1] = 1.0360746705285417e-02;
    xcrref[2] = 1.6235745065095532e-02;
    xcrref[3] = 1.5840557224455615e-02;
    xcrref[4] = 3.4849040609362460e-02;
    xceref[0] = 2.7289258557377227e-05;
    xceref[1] = 1.0364446640837285e-05;
    xceref[2] = 1.6154798287166471e-05;
    xceref[3] = 1.5750704994480102e-05;
    xceref[4] = 3.4177666183390531e-05;
  } else if (grid_points[0] == 36 &&
      grid_points[1] == 36 &&
      grid_points[2] == 36 &&
      no_time_steps == 400) {
    *class = 'W';
    dtref = 1.5e-3;
    xcrref[0] = 0.1893253733584e-02;
    xcrref[1] = 0.1717075447775e-03;
    xcrref[2] = 0.2778153350936e-03;
    xcrref[3] = 0.2887475409984e-03;
    xcrref[4] = 0.3143611161242e-02;
    xceref[0] = 0.7542088599534e-04;
    xceref[1] = 0.6512852253086e-05;
    xceref[2] = 0.1049092285688e-04;
    xceref[3] = 0.1128838671535e-04;
    xceref[4] = 0.1212845639773e-03;
  } else if (grid_points[0] == 64 &&
      grid_points[1] == 64 &&
      grid_points[2] == 64 &&
      no_time_steps == 400 ) {
    *class = 'A';
    dtref = 1.5e-3;
    xcrref[0] = 2.4799822399300195;
    xcrref[1] = 1.1276337964368832;
    xcrref[2] = 1.5028977888770491;
    xcrref[3] = 1.4217816211695179;
    xcrref[4] = 2.1292113035138280;
    xceref[0] = 1.0900140297820550e-04;
    xceref[1] = 3.7343951769282091e-05;
    xceref[2] = 5.0092785406541633e-05;
    xceref[3] = 4.7671093939528255e-05;
    xceref[4] = 1.3621613399213001e-04;
  } else if (grid_points[0] == 102 &&
      grid_points[1] == 102 &&
      grid_points[2] == 102 &&
      no_time_steps == 400) {
    *class = 'B';
    dtref = 1.0e-3;
    xcrref[0] = 0.6903293579998e+02;
    xcrref[1] = 0.3095134488084e+02;
    xcrref[2] = 0.4103336647017e+02;
    xcrref[3] = 0.3864769009604e+02;
    xcrref[4] = 0.5643482272596e+02;
    xceref[0] = 0.9810006190188e-02;
    xceref[1] = 0.1022827905670e-02;
    xceref[2] = 0.1720597911692e-02;
    xceref[3] = 0.1694479428231e-02;
    xceref[4] = 0.1847456263981e-01;
  } else if (grid_points[0] == 162 &&
      grid_points[1] == 162 &&
      grid_points[2] == 162 &&
      no_time_steps == 400) {
    *class = 'C';
    dtref = 0.67e-3;
    xcrref[0] = 0.5881691581829e+03;
    xcrref[1] = 0.2454417603569e+03;
    xcrref[2] = 0.3293829191851e+03;
    xcrref[3] = 0.3081924971891e+03;
    xcrref[4] = 0.4597223799176e+03;
    xceref[0] = 0.2598120500183e+00;
    xceref[1] = 0.2590888922315e-01;
    xceref[2] = 0.5132886416320e-01;
    xceref[3] = 0.4806073419454e-01;
    xceref[4] = 0.5483377491301e+00;
  } else {
    *verified = 0;
  }
  for (m = 0; m < 5; m++) {
    xcrdif[m] = fabs((xcr[m]-xcrref[m])/xcrref[m]) ;
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
      m,xcr[m],xcrref[m],xcrdif[m]);
    } else {
      printf("          %2d%20.13e%20.13e%20.13e\n",
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
      printf("          %2d%20.13e\n", m, xce[m]);
    } else if (xcedif[m] > epsilon) {
      *verified = 0;
      printf(" FAILURE: %2d%20.13e%20.13e%20.13e\n",
      m,xce[m],xceref[m],xcedif[m]);
    } else {
      printf("          %2d%20.13e%20.13e%20.13e\n",
      m,xce[m],xceref[m],xcedif[m]);
    }
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
static void x_solve(void) {
#pragma omp parallel
{
  int i, j, k, n, i1, i2, m;
  double fac1, fac2;
  lhsx();
  n = 0;
  for (i = 0; i <= grid_points[0]-3; i++) {
    i1 = i + 1;
    i2 = i + 2;
#pragma omp for
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 fac1 = 1./lhs[n+2][i][j][k];
 lhs[n+3][i][j][k] = fac1*lhs[n+3][i][j][k];
 lhs[n+4][i][j][k] = fac1*lhs[n+4][i][j][k];
 for (m = 0; m < 3; m++) {
   rhs[m][i][j][k] = fac1*rhs[m][i][j][k];
 }
 lhs[n+2][i1][j][k] = lhs[n+2][i1][j][k] -
   lhs[n+1][i1][j][k]*lhs[n+3][i][j][k];
 lhs[n+3][i1][j][k] = lhs[n+3][i1][j][k] -
   lhs[n+1][i1][j][k]*lhs[n+4][i][j][k];
 for (m = 0; m < 3; m++) {
   rhs[m][i1][j][k] = rhs[m][i1][j][k] -
     lhs[n+1][i1][j][k]*rhs[m][i][j][k];
 }
 lhs[n+1][i2][j][k] = lhs[n+1][i2][j][k] -
   lhs[n+0][i2][j][k]*lhs[n+3][i][j][k];
 lhs[n+2][i2][j][k] = lhs[n+2][i2][j][k] -
   lhs[n+0][i2][j][k]*lhs[n+4][i][j][k];
 for (m = 0; m < 3; m++) {
   rhs[m][i2][j][k] = rhs[m][i2][j][k] -
     lhs[n+0][i2][j][k]*rhs[m][i][j][k];
 }
      }
    }
  }
  i = grid_points[0]-2;
  i1 = grid_points[0]-1;
#pragma omp for
  for (j = 1; j <= grid_points[1]-2; j++) {
    for (k = 1; k <= grid_points[2]-2; k++) {
      fac1 = 1.0/lhs[n+2][i][j][k];
      lhs[n+3][i][j][k] = fac1*lhs[n+3][i][j][k];
      lhs[n+4][i][j][k] = fac1*lhs[n+4][i][j][k];
      for (m = 0; m < 3; m++) {
 rhs[m][i][j][k] = fac1*rhs[m][i][j][k];
      }
      lhs[n+2][i1][j][k] = lhs[n+2][i1][j][k] -
 lhs[n+1][i1][j][k]*lhs[n+3][i][j][k];
      lhs[n+3][i1][j][k] = lhs[n+3][i1][j][k] -
 lhs[n+1][i1][j][k]*lhs[n+4][i][j][k];
      for (m = 0; m < 3; m++) {
 rhs[m][i1][j][k] = rhs[m][i1][j][k] -
   lhs[n+1][i1][j][k]*rhs[m][i][j][k];
      }
      fac2 = 1./lhs[n+2][i1][j][k];
      for (m = 0; m < 3; m++) {
 rhs[m][i1][j][k] = fac2*rhs[m][i1][j][k];
      }
    }
  }
  for (m = 3; m < 5; m++) {
    n = (m-3+1)*5;
    for (i = 0; i <= grid_points[0]-3; i++) {
      i1 = i + 1;
      i2 = i + 2;
#pragma omp for
      for (j = 1; j <= grid_points[1]-2; j++) {
 for (k = 1; k <= grid_points[2]-2; k++) {
   fac1 = 1./lhs[n+2][i][j][k];
   lhs[n+3][i][j][k] = fac1*lhs[n+3][i][j][k];
   lhs[n+4][i][j][k] = fac1*lhs[n+4][i][j][k];
   rhs[m][i][j][k] = fac1*rhs[m][i][j][k];
   lhs[n+2][i1][j][k] = lhs[n+2][i1][j][k] -
     lhs[n+1][i1][j][k]*lhs[n+3][i][j][k];
   lhs[n+3][i1][j][k] = lhs[n+3][i1][j][k] -
     lhs[n+1][i1][j][k]*lhs[n+4][i][j][k];
   rhs[m][i1][j][k] = rhs[m][i1][j][k] -
     lhs[n+1][i1][j][k]*rhs[m][i][j][k];
   lhs[n+1][i2][j][k] = lhs[n+1][i2][j][k] -
     lhs[n+0][i2][j][k]*lhs[n+3][i][j][k];
   lhs[n+2][i2][j][k] = lhs[n+2][i2][j][k] -
     lhs[n+0][i2][j][k]*lhs[n+4][i][j][k];
   rhs[m][i2][j][k] = rhs[m][i2][j][k] -
     lhs[n+0][i2][j][k]*rhs[m][i][j][k];
 }
      }
    }
    i = grid_points[0]-2;
    i1 = grid_points[0]-1;
#pragma omp for
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 fac1 = 1./lhs[n+2][i][j][k];
 lhs[n+3][i][j][k] = fac1*lhs[n+3][i][j][k];
 lhs[n+4][i][j][k] = fac1*lhs[n+4][i][j][k];
 rhs[m][i][j][k] = fac1*rhs[m][i][j][k];
 lhs[n+2][i1][j][k] = lhs[n+2][i1][j][k] -
   lhs[n+1][i1][j][k]*lhs[n+3][i][j][k];
 lhs[n+3][i1][j][k] = lhs[n+3][i1][j][k] -
   lhs[n+1][i1][j][k]*lhs[n+4][i][j][k];
 rhs[m][i1][j][k] = rhs[m][i1][j][k] -
   lhs[n+1][i1][j][k]*rhs[m][i][j][k];
 fac2 = 1./lhs[n+2][i1][j][k];
 rhs[m][i1][j][k] = fac2*rhs[m][i1][j][k];
      }
    }
  }
  i = grid_points[0]-2;
  i1 = grid_points[0]-1;
  n = 0;
  for (m = 0; m < 3; m++) {
#pragma omp for
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 rhs[m][i][j][k] = rhs[m][i][j][k] -
   lhs[n+3][i][j][k]*rhs[m][i1][j][k];
      }
    }
  }
  for (m = 3; m < 5; m++) {
#pragma omp for
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 n = (m-3+1)*5;
 rhs[m][i][j][k] = rhs[m][i][j][k] -
   lhs[n+3][i][j][k]*rhs[m][i1][j][k];
      }
    }
  }
  n = 0;
  for (i = grid_points[0]-3; i >= 0; i--) {
    i1 = i + 1;
    i2 = i + 2;
#pragma omp for
    for (m = 0; m < 3; m++) {
      for (j = 1; j <= grid_points[1]-2; j++) {
 for (k = 1; k <= grid_points[2]-2; k++) {
   rhs[m][i][j][k] = rhs[m][i][j][k] -
     lhs[n+3][i][j][k]*rhs[m][i1][j][k] -
     lhs[n+4][i][j][k]*rhs[m][i2][j][k];
 }
      }
    }
  }
  for (m = 3; m < 5; m++) {
    n = (m-3+1)*5;
    for (i = grid_points[0]-3; i >= 0; i--) {
      i1 = i + 1;
      i2 = i + 2;
#pragma omp for
      for (j = 1; j <= grid_points[1]-2; j++) {
 for (k = 1; k <= grid_points[2]-2; k++) {
   rhs[m][i][j][k] = rhs[m][i][j][k] -
     lhs[n+3][i][j][k]*rhs[m][i1][j][k] -
     lhs[n+4][i][j][k]*rhs[m][i2][j][k];
 }
      }
    }
  }
}
  ninvr();
}
static void y_solve(void) {
#pragma omp parallel
{
  int i, j, k, n, j1, j2, m;
  double fac1, fac2;
  lhsy();
  n = 0;
  for (j = 0; j <= grid_points[1]-3; j++) {
    j1 = j + 1;
    j2 = j + 2;
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 fac1 = 1./lhs[n+2][i][j][k];
 lhs[n+3][i][j][k] = fac1*lhs[n+3][i][j][k];
 lhs[n+4][i][j][k] = fac1*lhs[n+4][i][j][k];
        for (m = 0; m < 3; m++) {
   rhs[m][i][j][k] = fac1*rhs[m][i][j][k];
 }
 lhs[n+2][i][j1][k] = lhs[n+2][i][j1][k] -
   lhs[n+1][i][j1][k]*lhs[n+3][i][j][k];
 lhs[n+3][i][j1][k] = lhs[n+3][i][j1][k] -
   lhs[n+1][i][j1][k]*lhs[n+4][i][j][k];
 for (m = 0; m < 3; m++) {
   rhs[m][i][j1][k] = rhs[m][i][j1][k] -
     lhs[n+1][i][j1][k]*rhs[m][i][j][k];
 }
 lhs[n+1][i][j2][k] = lhs[n+1][i][j2][k] -
   lhs[n+0][i][j2][k]*lhs[n+3][i][j][k];
 lhs[n+2][i][j2][k] = lhs[n+2][i][j2][k] -
   lhs[n+0][i][j2][k]*lhs[n+4][i][j][k];
 for (m = 0; m < 3; m++) {
   rhs[m][i][j2][k] = rhs[m][i][j2][k] -
     lhs[n+0][i][j2][k]*rhs[m][i][j][k];
 }
      }
    }
  }
  j = grid_points[1]-2;
  j1 = grid_points[1]-1;
#pragma omp for
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (k = 1; k <= grid_points[2]-2; k++) {
      fac1 = 1./lhs[n+2][i][j][k];
      lhs[n+3][i][j][k] = fac1*lhs[n+3][i][j][k];
      lhs[n+4][i][j][k] = fac1*lhs[n+4][i][j][k];
      for (m = 0; m < 3; m++) {
 rhs[m][i][j][k] = fac1*rhs[m][i][j][k];
      }
      lhs[n+2][i][j1][k] = lhs[n+2][i][j1][k] -
 lhs[n+1][i][j1][k]*lhs[n+3][i][j][k];
      lhs[n+3][i][j1][k] = lhs[n+3][i][j1][k] -
 lhs[n+1][i][j1][k]*lhs[n+4][i][j][k];
      for (m = 0; m < 3; m++) {
 rhs[m][i][j1][k] = rhs[m][i][j1][k] -
   lhs[n+1][i][j1][k]*rhs[m][i][j][k];
      }
      fac2 = 1./lhs[n+2][i][j1][k];
      for (m = 0; m < 3; m++) {
 rhs[m][i][j1][k] = fac2*rhs[m][i][j1][k];
      }
    }
  }
  for (m = 3; m < 5; m++) {
    n = (m-3+1)*5;
    for (j = 0; j <= grid_points[1]-3; j++) {
      j1 = j + 1;
      j2 = j + 2;
#pragma omp for
      for (i = 1; i <= grid_points[0]-2; i++) {
 for (k = 1; k <= grid_points[2]-2; k++) {
   fac1 = 1./lhs[n+2][i][j][k];
   lhs[n+3][i][j][k] = fac1*lhs[n+3][i][j][k];
   lhs[n+4][i][j][k] = fac1*lhs[n+4][i][j][k];
   rhs[m][i][j][k] = fac1*rhs[m][i][j][k];
   lhs[n+2][i][j1][k] = lhs[n+2][i][j1][k] -
     lhs[n+1][i][j1][k]*lhs[n+3][i][j][k];
   lhs[n+3][i][j1][k] = lhs[n+3][i][j1][k] -
     lhs[n+1][i][j1][k]*lhs[n+4][i][j][k];
   rhs[m][i][j1][k] = rhs[m][i][j1][k] -
     lhs[n+1][i][j1][k]*rhs[m][i][j][k];
   lhs[n+1][i][j2][k] = lhs[n+1][i][j2][k] -
     lhs[n+0][i][j2][k]*lhs[n+3][i][j][k];
   lhs[n+2][i][j2][k] = lhs[n+2][i][j2][k] -
     lhs[n+0][i][j2][k]*lhs[n+4][i][j][k];
   rhs[m][i][j2][k] = rhs[m][i][j2][k] -
     lhs[n+0][i][j2][k]*rhs[m][i][j][k];
 }
      }
    }
    j = grid_points[1]-2;
    j1 = grid_points[1]-1;
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 fac1 = 1./lhs[n+2][i][j][k];
 lhs[n+3][i][j][k] = fac1*lhs[n+3][i][j][k];
 lhs[n+4][i][j][k] = fac1*lhs[n+4][i][j][k];
 rhs[m][i][j][k] = fac1*rhs[m][i][j][k];
 lhs[n+2][i][j1][k] = lhs[n+2][i][j1][k] -
   lhs[n+1][i][j1][k]*lhs[n+3][i][j][k];
 lhs[n+3][i][j1][k] = lhs[n+3][i][j1][k] -
   lhs[n+1][i][j1][k]*lhs[n+4][i][j][k];
 rhs[m][i][j1][k] = rhs[m][i][j1][k] -
   lhs[n+1][i][j1][k]*rhs[m][i][j][k];
 fac2 = 1./lhs[n+2][i][j1][k];
 rhs[m][i][j1][k] = fac2*rhs[m][i][j1][k];
      }
    }
  }
  j = grid_points[1]-2;
  j1 = grid_points[1]-1;
  n = 0;
  for (m = 0; m < 3; m++) {
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 rhs[m][i][j][k] = rhs[m][i][j][k] -
   lhs[n+3][i][j][k]*rhs[m][i][j1][k];
      }
    }
  }
  for (m = 3; m < 5; m++) {
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (k = 1; k <= grid_points[2]-2; k++) {
 n = (m-3+1)*5;
 rhs[m][i][j][k] = rhs[m][i][j][k] -
   lhs[n+3][i][j][k]*rhs[m][i][j1][k];
      }
    }
  }
  n = 0;
  for (m = 0; m < 3; m++) {
    for (j = grid_points[1]-3; j >= 0; j--) {
      j1 = j + 1;
      j2 = j + 2;
#pragma omp for
      for (i = 1; i <= grid_points[0]-2; i++) {
 for (k = 1; k <= grid_points[2]-2; k++) {
   rhs[m][i][j][k] = rhs[m][i][j][k] -
     lhs[n+3][i][j][k]*rhs[m][i][j1][k] -
     lhs[n+4][i][j][k]*rhs[m][i][j2][k];
 }
      }
    }
  }
  for (m = 3; m < 5; m++) {
    n = (m-3+1)*5;
    for (j = grid_points[1]-3; j >= 0; j--) {
      j1 = j + 1;
      j2 = j1 + 1;
#pragma omp for
      for (i = 1; i <= grid_points[0]-2; i++) {
 for (k = 1; k <= grid_points[2]-2; k++) {
   rhs[m][i][j][k] = rhs[m][i][j][k] -
     lhs[n+3][i][j][k]*rhs[m][i][j1][k] -
     lhs[n+4][i][j][k]*rhs[m][i][j2][k];
 }
      }
    }
  }
}
  pinvr();
}
static void z_solve(void) {
#pragma omp parallel
{
  int i, j, k, n, k1, k2, m;
  double fac1, fac2;
  lhsz();
  n = 0;
#pragma omp for
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (j = 1; j <= grid_points[1]-2; j++) {
      for (k = 0; k <= grid_points[2]-3; k++) {
 k1 = k + 1;
 k2 = k + 2;
 fac1 = 1./lhs[n+2][i][j][k];
 lhs[n+3][i][j][k] = fac1*lhs[n+3][i][j][k];
 lhs[n+4][i][j][k] = fac1*lhs[n+4][i][j][k];
 for (m = 0; m < 3; m++) {
   rhs[m][i][j][k] = fac1*rhs[m][i][j][k];
 }
 lhs[n+2][i][j][k1] = lhs[n+2][i][j][k1] -
   lhs[n+1][i][j][k1]*lhs[n+3][i][j][k];
 lhs[n+3][i][j][k1] = lhs[n+3][i][j][k1] -
   lhs[n+1][i][j][k1]*lhs[n+4][i][j][k];
 for (m = 0; m < 3; m++) {
   rhs[m][i][j][k1] = rhs[m][i][j][k1] -
     lhs[n+1][i][j][k1]*rhs[m][i][j][k];
 }
 lhs[n+1][i][j][k2] = lhs[n+1][i][j][k2] -
   lhs[n+0][i][j][k2]*lhs[n+3][i][j][k];
 lhs[n+2][i][j][k2] = lhs[n+2][i][j][k2] -
   lhs[n+0][i][j][k2]*lhs[n+4][i][j][k];
 for (m = 0; m < 3; m++) {
   rhs[m][i][j][k2] = rhs[m][i][j][k2] -
     lhs[n+0][i][j][k2]*rhs[m][i][j][k];
 }
      }
    }
  }
  k = grid_points[2]-2;
  k1 = grid_points[2]-1;
#pragma omp for
  for (i = 1; i <= grid_points[0]-2; i++) {
    for (j = 1; j <= grid_points[1]-2; j++) {
      fac1 = 1./lhs[n+2][i][j][k];
      lhs[n+3][i][j][k] = fac1*lhs[n+3][i][j][k];
      lhs[n+4][i][j][k] = fac1*lhs[n+4][i][j][k];
      for (m = 0; m < 3; m++) {
 rhs[m][i][j][k] = fac1*rhs[m][i][j][k];
      }
      lhs[n+2][i][j][k1] = lhs[n+2][i][j][k1] -
 lhs[n+1][i][j][k1]*lhs[n+3][i][j][k];
      lhs[n+3][i][j][k1] = lhs[n+3][i][j][k1] -
 lhs[n+1][i][j][k1]*lhs[n+4][i][j][k];
      for (m = 0; m < 3; m++) {
 rhs[m][i][j][k1] = rhs[m][i][j][k1] -
   lhs[n+1][i][j][k1]*rhs[m][i][j][k];
      }
      fac2 = 1./lhs[n+2][i][j][k1];
      for (m = 0; m < 3; m++) {
 rhs[m][i][j][k1] = fac2*rhs[m][i][j][k1];
      }
    }
  }
  for (m = 3; m < 5; m++) {
    n = (m-3+1)*5;
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (j = 1; j <= grid_points[1]-2; j++) {
 for (k = 0; k <= grid_points[2]-3; k++) {
 k1 = k + 1;
 k2 = k + 2;
   fac1 = 1./lhs[n+2][i][j][k];
   lhs[n+3][i][j][k] = fac1*lhs[n+3][i][j][k];
   lhs[n+4][i][j][k] = fac1*lhs[n+4][i][j][k];
   rhs[m][i][j][k] = fac1*rhs[m][i][j][k];
   lhs[n+2][i][j][k1] = lhs[n+2][i][j][k1] -
     lhs[n+1][i][j][k1]*lhs[n+3][i][j][k];
   lhs[n+3][i][j][k1] = lhs[n+3][i][j][k1] -
     lhs[n+1][i][j][k1]*lhs[n+4][i][j][k];
   rhs[m][i][j][k1] = rhs[m][i][j][k1] -
     lhs[n+1][i][j][k1]*rhs[m][i][j][k];
   lhs[n+1][i][j][k2] = lhs[n+1][i][j][k2] -
     lhs[n+0][i][j][k2]*lhs[n+3][i][j][k];
   lhs[n+2][i][j][k2] = lhs[n+2][i][j][k2] -
     lhs[n+0][i][j][k2]*lhs[n+4][i][j][k];
   rhs[m][i][j][k2] = rhs[m][i][j][k2] -
     lhs[n+0][i][j][k2]*rhs[m][i][j][k];
 }
      }
    }
    k = grid_points[2]-2;
    k1 = grid_points[2]-1;
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (j = 1; j <= grid_points[1]-2; j++) {
 fac1 = 1./lhs[n+2][i][j][k];
 lhs[n+3][i][j][k] = fac1*lhs[n+3][i][j][k];
 lhs[n+4][i][j][k] = fac1*lhs[n+4][i][j][k];
 rhs[m][i][j][k] = fac1*rhs[m][i][j][k];
 lhs[n+2][i][j][k1] = lhs[n+2][i][j][k1] -
   lhs[n+1][i][j][k1]*lhs[n+3][i][j][k];
 lhs[n+3][i][j][k1] = lhs[n+3][i][j][k1] -
   lhs[n+1][i][j][k1]*lhs[n+4][i][j][k];
 rhs[m][i][j][k1] = rhs[m][i][j][k1] -
   lhs[n+1][i][j][k1]*rhs[m][i][j][k];
 fac2 = 1./lhs[n+2][i][j][k1];
 rhs[m][i][j][k1] = fac2*rhs[m][i][j][k1];
      }
    }
  }
  k = grid_points[2]-2;
  k1 = grid_points[2]-1;
  n = 0;
  for (m = 0; m < 3; m++) {
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (j = 1; j <= grid_points[1]-2; j++) {
 rhs[m][i][j][k] = rhs[m][i][j][k] -
   lhs[n+3][i][j][k]*rhs[m][i][j][k1];
      }
    }
  }
  for (m = 3; m < 5; m++) {
    n = (m-3+1)*5;
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (j = 1; j <= grid_points[1]-2; j++) {
 rhs[m][i][j][k] = rhs[m][i][j][k] -
   lhs[n+3][i][j][k]*rhs[m][i][j][k1];
      }
    }
  }
  n = 0;
  for (m = 0; m < 3; m++) {
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (j = 1; j <= grid_points[1]-2; j++) {
 for (k = grid_points[2]-3; k >= 0; k--) {
   k1 = k + 1;
   k2 = k + 2;
   rhs[m][i][j][k] = rhs[m][i][j][k] -
     lhs[n+3][i][j][k]*rhs[m][i][j][k1] -
     lhs[n+4][i][j][k]*rhs[m][i][j][k2];
 }
      }
    }
  }
  for (m = 3; m < 5; m++) {
    n = (m-3+1)*5;
#pragma omp for
    for (i = 1; i <= grid_points[0]-2; i++) {
      for (j = 1; j <= grid_points[1]-2; j++) {
 for (k = grid_points[2]-3; k >= 0; k--) {
   k1 = k + 1;
   k2 = k + 2;
   rhs[m][i][j][k] = rhs[m][i][j][k] -
     lhs[n+3][i][j][k]*rhs[m][i][j][k1] -
     lhs[n+4][i][j][k]*rhs[m][i][j][k2];
 }
      }
    }
  }
}
  tzetar();
}
