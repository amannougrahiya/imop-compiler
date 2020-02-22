typedef signed char __int8_t;
typedef unsigned char __uint8_t;
typedef short __int16_t;
typedef unsigned short __uint16_t;
typedef int __int32_t;
typedef unsigned int __uint32_t;
typedef long long __int64_t;
typedef unsigned long long __uint64_t;
typedef long __darwin_intptr_t;
typedef unsigned int __darwin_natural_t;
typedef int __darwin_ct_rune_t;
typedef union {
 char __mbstate8[128];
 long long _mbstateL;
} __mbstate_t;
typedef __mbstate_t __darwin_mbstate_t;
typedef long int __darwin_ptrdiff_t;
typedef long unsigned int __darwin_size_t;
typedef __builtin_va_list __darwin_va_list;
typedef int __darwin_wchar_t;
typedef __darwin_wchar_t __darwin_rune_t;
typedef int __darwin_wint_t;
typedef unsigned long __darwin_clock_t;
typedef __uint32_t __darwin_socklen_t;
typedef long __darwin_ssize_t;
typedef long __darwin_time_t;
typedef __int64_t __darwin_blkcnt_t;
typedef __int32_t __darwin_blksize_t;
typedef __int32_t __darwin_dev_t;
typedef unsigned int __darwin_fsblkcnt_t;
typedef unsigned int __darwin_fsfilcnt_t;
typedef __uint32_t __darwin_gid_t;
typedef __uint32_t __darwin_id_t;
typedef __uint64_t __darwin_ino64_t;
typedef __darwin_ino64_t __darwin_ino_t;
typedef __darwin_natural_t __darwin_mach_port_name_t;
typedef __darwin_mach_port_name_t __darwin_mach_port_t;
typedef __uint16_t __darwin_mode_t;
typedef __int64_t __darwin_off_t;
typedef __int32_t __darwin_pid_t;
typedef __uint32_t __darwin_sigset_t;
typedef __int32_t __darwin_suseconds_t;
typedef __uint32_t __darwin_uid_t;
typedef __uint32_t __darwin_useconds_t;
typedef unsigned char __darwin_uuid_t[16];
typedef char __darwin_uuid_string_t[37];
struct __darwin_pthread_handler_rec {
 void (*__routine)(void *);
 void *__arg;
 struct __darwin_pthread_handler_rec *__next;
};
struct _opaque_pthread_attr_t {
 long __sig;
 char __opaque[56];
};
struct _opaque_pthread_cond_t {
 long __sig;
 char __opaque[40];
};
struct _opaque_pthread_condattr_t {
 long __sig;
 char __opaque[8];
};
struct _opaque_pthread_mutex_t {
 long __sig;
 char __opaque[56];
};
struct _opaque_pthread_mutexattr_t {
 long __sig;
 char __opaque[8];
};
struct _opaque_pthread_once_t {
 long __sig;
 char __opaque[8];
};
struct _opaque_pthread_rwlock_t {
 long __sig;
 char __opaque[192];
};
struct _opaque_pthread_rwlockattr_t {
 long __sig;
 char __opaque[16];
};
struct _opaque_pthread_t {
 long __sig;
 struct __darwin_pthread_handler_rec *__cleanup_stack;
 char __opaque[8176];
};
typedef struct _opaque_pthread_attr_t __darwin_pthread_attr_t;
typedef struct _opaque_pthread_cond_t __darwin_pthread_cond_t;
typedef struct _opaque_pthread_condattr_t __darwin_pthread_condattr_t;
typedef unsigned long __darwin_pthread_key_t;
typedef struct _opaque_pthread_mutex_t __darwin_pthread_mutex_t;
typedef struct _opaque_pthread_mutexattr_t __darwin_pthread_mutexattr_t;
typedef struct _opaque_pthread_once_t __darwin_pthread_once_t;
typedef struct _opaque_pthread_rwlock_t __darwin_pthread_rwlock_t;
typedef struct _opaque_pthread_rwlockattr_t __darwin_pthread_rwlockattr_t;
typedef struct _opaque_pthread_t *__darwin_pthread_t;
typedef int __darwin_nl_item;
typedef int __darwin_wctrans_t;
typedef __uint32_t __darwin_wctype_t;
typedef signed char int8_t;
typedef short int16_t;
typedef int int32_t;
typedef long long int64_t;
typedef unsigned char u_int8_t;
typedef unsigned short u_int16_t;
typedef unsigned int u_int32_t;
typedef unsigned long long u_int64_t;
typedef int64_t register_t;
typedef __darwin_intptr_t intptr_t;
typedef unsigned long uintptr_t;
typedef u_int64_t user_addr_t;
typedef u_int64_t user_size_t;
typedef int64_t user_ssize_t;
typedef int64_t user_long_t;
typedef u_int64_t user_ulong_t;
typedef int64_t user_time_t;
typedef int64_t user_off_t;
typedef u_int64_t syscall_arg_t;
typedef __darwin_va_list va_list;
typedef __darwin_size_t size_t;

int renameat(int, const char *, int, const char *) ;
int renamex_np(const char *, const char *, unsigned int) ;
int renameatx_np(int, const char *, int, const char *, unsigned int) ;

typedef __darwin_off_t fpos_t;
struct __sbuf {
 unsigned char *_base;
 int _size;
};
struct __sFILEX;
typedef struct __sFILE {
 unsigned char *_p;
 int _r;
 int _w;
 short _flags;
 short _file;
 struct __sbuf _bf;
 int _lbfsize;
 void *_cookie;
 int (* _close)(void *);
 int (* _read) (void *, char *, int);
 fpos_t (* _seek) (void *, fpos_t, int);
 int (* _write)(void *, const char *, int);
 struct __sbuf _ub;
 struct __sFILEX *_extra;
 int _ur;
 unsigned char _ubuf[3];
 unsigned char _nbuf[1];
 struct __sbuf _lb;
 int _blksize;
 fpos_t _offset;
} FILE;

extern FILE *__stdinp;
extern FILE *__stdoutp;
extern FILE *__stderrp;


void clearerr(FILE *);
int fclose(FILE *);
int feof(FILE *);
int ferror(FILE *);
int fflush(FILE *);
int fgetc(FILE *);
int fgetpos(FILE * restrict, fpos_t *);
char *fgets(char * restrict, int, FILE *);
FILE *fopen(const char * restrict __filename, const char * restrict __mode) __asm("_" "fopen" );
int fprintf(FILE * restrict, const char * restrict, ...) __attribute__((__format__ (__printf__, 2, 3)));
int fputc(int, FILE *);
int fputs(const char * restrict, FILE * restrict) __asm("_" "fputs" );
size_t fread(void * restrict __ptr, size_t __size, size_t __nitems, FILE * restrict __stream);
FILE *freopen(const char * restrict, const char * restrict,
                 FILE * restrict) __asm("_" "freopen" );
int fscanf(FILE * restrict, const char * restrict, ...) __attribute__((__format__ (__scanf__, 2, 3)));
int fseek(FILE *, long, int);
int fsetpos(FILE *, const fpos_t *);
long ftell(FILE *);
size_t fwrite(const void * restrict __ptr, size_t __size, size_t __nitems, FILE * restrict __stream) __asm("_" "fwrite" );
int getc(FILE *);
int getchar(void);
char *gets(char *);
void perror(const char *);
int printf(const char * restrict, ...) __attribute__((__format__ (__printf__, 1, 2)));
int putc(int, FILE *);
int putchar(int);
int puts(const char *);
int remove(const char *);
int rename (const char *__old, const char *__new);
void rewind(FILE *);
int scanf(const char * restrict, ...) __attribute__((__format__ (__scanf__, 1, 2)));
void setbuf(FILE * restrict, char * restrict);
int setvbuf(FILE * restrict, char * restrict, int, size_t);
int sprintf(char * restrict, const char * restrict, ...) __attribute__((__format__ (__printf__, 2, 3))) ;
int sscanf(const char * restrict, const char * restrict, ...) __attribute__((__format__ (__scanf__, 2, 3)));
FILE *tmpfile(void);

__attribute__((deprecated("This function is provided for compatibility reasons only.  Due to security concerns inherent in the design of tmpnam(3), it is highly recommended that you use mkstemp(3) instead.")))
char *tmpnam(char *);
int ungetc(int, FILE *);
int vfprintf(FILE * restrict, const char * restrict, va_list) __attribute__((__format__ (__printf__, 2, 0)));
int vprintf(const char * restrict, va_list) __attribute__((__format__ (__printf__, 1, 0)));
int vsprintf(char * restrict, const char * restrict, va_list) __attribute__((__format__ (__printf__, 2, 0))) ;


char *ctermid(char *);
FILE *fdopen(int, const char *) __asm("_" "fdopen" );
int fileno(FILE *);


int pclose(FILE *) ;
FILE *popen(const char *, const char *) __asm("_" "popen" ) ;


int __srget(FILE *);
int __svfscanf(FILE *, const char *, va_list) __attribute__((__format__ (__scanf__, 2, 0)));
int __swbuf(int, FILE *);

extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __sputc(int _c, FILE *_p) {
 if (--_p->_w >= 0 || (_p->_w >= _p->_lbfsize && (char)_c != '\n'))
  return (*_p->_p++ = _c);
 else
  return (__swbuf(_c, _p));
}

void flockfile(FILE *);
int ftrylockfile(FILE *);
void funlockfile(FILE *);
int getc_unlocked(FILE *);
int getchar_unlocked(void);
int putc_unlocked(int, FILE *);
int putchar_unlocked(int);
int getw(FILE *);
int putw(int, FILE *);

__attribute__((deprecated("This function is provided for compatibility reasons only.  Due to security concerns inherent in the design of tempnam(3), it is highly recommended that you use mkstemp(3) instead.")))
char *tempnam(const char *__dir, const char *__prefix) __asm("_" "tempnam" );

typedef __darwin_off_t off_t;

int fseeko(FILE * __stream, off_t __offset, int __whence);
off_t ftello(FILE * __stream);


int snprintf(char * restrict __str, size_t __size, const char * restrict __format, ...) __attribute__((__format__ (__printf__, 3, 4)));
int vfscanf(FILE * restrict __stream, const char * restrict __format, va_list) __attribute__((__format__ (__scanf__, 2, 0)));
int vscanf(const char * restrict __format, va_list) __attribute__((__format__ (__scanf__, 1, 0)));
int vsnprintf(char * restrict __str, size_t __size, const char * restrict __format, va_list) __attribute__((__format__ (__printf__, 3, 0)));
int vsscanf(const char * restrict __str, const char * restrict __format, va_list) __attribute__((__format__ (__scanf__, 2, 0)));

typedef __darwin_ssize_t ssize_t;

int dprintf(int, const char * restrict, ...) __attribute__((__format__ (__printf__, 2, 3))) ;
int vdprintf(int, const char * restrict, va_list) __attribute__((__format__ (__printf__, 2, 0))) ;
ssize_t getdelim(char ** restrict __linep, size_t * restrict __linecapp, int __delimiter, FILE * restrict __stream) ;
ssize_t getline(char ** restrict __linep, size_t * restrict __linecapp, FILE * restrict __stream) ;
FILE *fmemopen(void * restrict __buf, size_t __size, const char * restrict __mode) ;
FILE *open_memstream(char **__bufp, size_t *__sizep) ;


extern const int sys_nerr;
extern const char *const sys_errlist[];
int asprintf(char ** restrict, const char * restrict, ...) __attribute__((__format__ (__printf__, 2, 3)));
char *ctermid_r(char *);
char *fgetln(FILE *, size_t *);
const char *fmtcheck(const char *, const char *);
int fpurge(FILE *);
void setbuffer(FILE *, char *, int);
int setlinebuf(FILE *);
int vasprintf(char ** restrict, const char * restrict, va_list) __attribute__((__format__ (__printf__, 2, 0)));
FILE *zopen(const char *, const char *, int);
FILE *funopen(const void *,
                 int (* )(void *, char *, int),
                 int (* )(void *, const char *, int),
                 fpos_t (* )(void *, fpos_t, int),
                 int (* )(void *));

extern int __sprintf_chk (char * restrict, int, size_t,
     const char * restrict, ...);
extern int __snprintf_chk (char * restrict, size_t, int, size_t,
      const char * restrict, ...);
extern int __vsprintf_chk (char * restrict, int, size_t,
      const char * restrict, va_list);
extern int __vsnprintf_chk (char * restrict, size_t, int, size_t,
       const char * restrict, va_list);
typedef enum {
 P_ALL,
 P_PID,
 P_PGID
} idtype_t;
typedef __darwin_pid_t pid_t;
typedef __darwin_id_t id_t;
typedef int sig_atomic_t;
struct __darwin_i386_thread_state
{
    unsigned int __eax;
    unsigned int __ebx;
    unsigned int __ecx;
    unsigned int __edx;
    unsigned int __edi;
    unsigned int __esi;
    unsigned int __ebp;
    unsigned int __esp;
    unsigned int __ss;
    unsigned int __eflags;
    unsigned int __eip;
    unsigned int __cs;
    unsigned int __ds;
    unsigned int __es;
    unsigned int __fs;
    unsigned int __gs;
};
struct __darwin_fp_control
{
    unsigned short __invalid :1,
        __denorm :1,
    __zdiv :1,
    __ovrfl :1,
    __undfl :1,
    __precis :1,
      :2,
    __pc :2,
    __rc :2,
             :1,
      :3;
};
typedef struct __darwin_fp_control __darwin_fp_control_t;
struct __darwin_fp_status
{
    unsigned short __invalid :1,
        __denorm :1,
    __zdiv :1,
    __ovrfl :1,
    __undfl :1,
    __precis :1,
    __stkflt :1,
    __errsumm :1,
    __c0 :1,
    __c1 :1,
    __c2 :1,
    __tos :3,
    __c3 :1,
    __busy :1;
};
typedef struct __darwin_fp_status __darwin_fp_status_t;
struct __darwin_mmst_reg
{
 char __mmst_reg[10];
 char __mmst_rsrv[6];
};
struct __darwin_xmm_reg
{
 char __xmm_reg[16];
};
struct __darwin_ymm_reg
{
 char __ymm_reg[32];
};
struct __darwin_zmm_reg
{
 char __zmm_reg[64];
};
struct __darwin_opmask_reg
{
 char __opmask_reg[8];
};
struct __darwin_i386_float_state
{
 int __fpu_reserved[2];
 struct __darwin_fp_control __fpu_fcw;
 struct __darwin_fp_status __fpu_fsw;
 __uint8_t __fpu_ftw;
 __uint8_t __fpu_rsrv1;
 __uint16_t __fpu_fop;
 __uint32_t __fpu_ip;
 __uint16_t __fpu_cs;
 __uint16_t __fpu_rsrv2;
 __uint32_t __fpu_dp;
 __uint16_t __fpu_ds;
 __uint16_t __fpu_rsrv3;
 __uint32_t __fpu_mxcsr;
 __uint32_t __fpu_mxcsrmask;
 struct __darwin_mmst_reg __fpu_stmm0;
 struct __darwin_mmst_reg __fpu_stmm1;
 struct __darwin_mmst_reg __fpu_stmm2;
 struct __darwin_mmst_reg __fpu_stmm3;
 struct __darwin_mmst_reg __fpu_stmm4;
 struct __darwin_mmst_reg __fpu_stmm5;
 struct __darwin_mmst_reg __fpu_stmm6;
 struct __darwin_mmst_reg __fpu_stmm7;
 struct __darwin_xmm_reg __fpu_xmm0;
 struct __darwin_xmm_reg __fpu_xmm1;
 struct __darwin_xmm_reg __fpu_xmm2;
 struct __darwin_xmm_reg __fpu_xmm3;
 struct __darwin_xmm_reg __fpu_xmm4;
 struct __darwin_xmm_reg __fpu_xmm5;
 struct __darwin_xmm_reg __fpu_xmm6;
 struct __darwin_xmm_reg __fpu_xmm7;
 char __fpu_rsrv4[14*16];
 int __fpu_reserved1;
};
struct __darwin_i386_avx_state
{
 int __fpu_reserved[2];
 struct __darwin_fp_control __fpu_fcw;
 struct __darwin_fp_status __fpu_fsw;
 __uint8_t __fpu_ftw;
 __uint8_t __fpu_rsrv1;
 __uint16_t __fpu_fop;
 __uint32_t __fpu_ip;
 __uint16_t __fpu_cs;
 __uint16_t __fpu_rsrv2;
 __uint32_t __fpu_dp;
 __uint16_t __fpu_ds;
 __uint16_t __fpu_rsrv3;
 __uint32_t __fpu_mxcsr;
 __uint32_t __fpu_mxcsrmask;
 struct __darwin_mmst_reg __fpu_stmm0;
 struct __darwin_mmst_reg __fpu_stmm1;
 struct __darwin_mmst_reg __fpu_stmm2;
 struct __darwin_mmst_reg __fpu_stmm3;
 struct __darwin_mmst_reg __fpu_stmm4;
 struct __darwin_mmst_reg __fpu_stmm5;
 struct __darwin_mmst_reg __fpu_stmm6;
 struct __darwin_mmst_reg __fpu_stmm7;
 struct __darwin_xmm_reg __fpu_xmm0;
 struct __darwin_xmm_reg __fpu_xmm1;
 struct __darwin_xmm_reg __fpu_xmm2;
 struct __darwin_xmm_reg __fpu_xmm3;
 struct __darwin_xmm_reg __fpu_xmm4;
 struct __darwin_xmm_reg __fpu_xmm5;
 struct __darwin_xmm_reg __fpu_xmm6;
 struct __darwin_xmm_reg __fpu_xmm7;
 char __fpu_rsrv4[14*16];
 int __fpu_reserved1;
 char __avx_reserved1[64];
 struct __darwin_xmm_reg __fpu_ymmh0;
 struct __darwin_xmm_reg __fpu_ymmh1;
 struct __darwin_xmm_reg __fpu_ymmh2;
 struct __darwin_xmm_reg __fpu_ymmh3;
 struct __darwin_xmm_reg __fpu_ymmh4;
 struct __darwin_xmm_reg __fpu_ymmh5;
 struct __darwin_xmm_reg __fpu_ymmh6;
 struct __darwin_xmm_reg __fpu_ymmh7;
};
struct __darwin_i386_avx512_state
{
 int __fpu_reserved[2];
 struct __darwin_fp_control __fpu_fcw;
 struct __darwin_fp_status __fpu_fsw;
 __uint8_t __fpu_ftw;
 __uint8_t __fpu_rsrv1;
 __uint16_t __fpu_fop;
 __uint32_t __fpu_ip;
 __uint16_t __fpu_cs;
 __uint16_t __fpu_rsrv2;
 __uint32_t __fpu_dp;
 __uint16_t __fpu_ds;
 __uint16_t __fpu_rsrv3;
 __uint32_t __fpu_mxcsr;
 __uint32_t __fpu_mxcsrmask;
 struct __darwin_mmst_reg __fpu_stmm0;
 struct __darwin_mmst_reg __fpu_stmm1;
 struct __darwin_mmst_reg __fpu_stmm2;
 struct __darwin_mmst_reg __fpu_stmm3;
 struct __darwin_mmst_reg __fpu_stmm4;
 struct __darwin_mmst_reg __fpu_stmm5;
 struct __darwin_mmst_reg __fpu_stmm6;
 struct __darwin_mmst_reg __fpu_stmm7;
 struct __darwin_xmm_reg __fpu_xmm0;
 struct __darwin_xmm_reg __fpu_xmm1;
 struct __darwin_xmm_reg __fpu_xmm2;
 struct __darwin_xmm_reg __fpu_xmm3;
 struct __darwin_xmm_reg __fpu_xmm4;
 struct __darwin_xmm_reg __fpu_xmm5;
 struct __darwin_xmm_reg __fpu_xmm6;
 struct __darwin_xmm_reg __fpu_xmm7;
 char __fpu_rsrv4[14*16];
 int __fpu_reserved1;
 char __avx_reserved1[64];
 struct __darwin_xmm_reg __fpu_ymmh0;
 struct __darwin_xmm_reg __fpu_ymmh1;
 struct __darwin_xmm_reg __fpu_ymmh2;
 struct __darwin_xmm_reg __fpu_ymmh3;
 struct __darwin_xmm_reg __fpu_ymmh4;
 struct __darwin_xmm_reg __fpu_ymmh5;
 struct __darwin_xmm_reg __fpu_ymmh6;
 struct __darwin_xmm_reg __fpu_ymmh7;
 struct __darwin_opmask_reg __fpu_k0;
 struct __darwin_opmask_reg __fpu_k1;
 struct __darwin_opmask_reg __fpu_k2;
 struct __darwin_opmask_reg __fpu_k3;
 struct __darwin_opmask_reg __fpu_k4;
 struct __darwin_opmask_reg __fpu_k5;
 struct __darwin_opmask_reg __fpu_k6;
 struct __darwin_opmask_reg __fpu_k7;
 struct __darwin_ymm_reg __fpu_zmmh0;
 struct __darwin_ymm_reg __fpu_zmmh1;
 struct __darwin_ymm_reg __fpu_zmmh2;
 struct __darwin_ymm_reg __fpu_zmmh3;
 struct __darwin_ymm_reg __fpu_zmmh4;
 struct __darwin_ymm_reg __fpu_zmmh5;
 struct __darwin_ymm_reg __fpu_zmmh6;
 struct __darwin_ymm_reg __fpu_zmmh7;
};
struct __darwin_i386_exception_state
{
 __uint16_t __trapno;
 __uint16_t __cpu;
 __uint32_t __err;
 __uint32_t __faultvaddr;
};
struct __darwin_x86_debug_state32
{
 unsigned int __dr0;
 unsigned int __dr1;
 unsigned int __dr2;
 unsigned int __dr3;
 unsigned int __dr4;
 unsigned int __dr5;
 unsigned int __dr6;
 unsigned int __dr7;
};
struct __darwin_x86_thread_state64
{
 __uint64_t __rax;
 __uint64_t __rbx;
 __uint64_t __rcx;
 __uint64_t __rdx;
 __uint64_t __rdi;
 __uint64_t __rsi;
 __uint64_t __rbp;
 __uint64_t __rsp;
 __uint64_t __r8;
 __uint64_t __r9;
 __uint64_t __r10;
 __uint64_t __r11;
 __uint64_t __r12;
 __uint64_t __r13;
 __uint64_t __r14;
 __uint64_t __r15;
 __uint64_t __rip;
 __uint64_t __rflags;
 __uint64_t __cs;
 __uint64_t __fs;
 __uint64_t __gs;
};
struct __darwin_x86_float_state64
{
 int __fpu_reserved[2];
 struct __darwin_fp_control __fpu_fcw;
 struct __darwin_fp_status __fpu_fsw;
 __uint8_t __fpu_ftw;
 __uint8_t __fpu_rsrv1;
 __uint16_t __fpu_fop;
 __uint32_t __fpu_ip;
 __uint16_t __fpu_cs;
 __uint16_t __fpu_rsrv2;
 __uint32_t __fpu_dp;
 __uint16_t __fpu_ds;
 __uint16_t __fpu_rsrv3;
 __uint32_t __fpu_mxcsr;
 __uint32_t __fpu_mxcsrmask;
 struct __darwin_mmst_reg __fpu_stmm0;
 struct __darwin_mmst_reg __fpu_stmm1;
 struct __darwin_mmst_reg __fpu_stmm2;
 struct __darwin_mmst_reg __fpu_stmm3;
 struct __darwin_mmst_reg __fpu_stmm4;
 struct __darwin_mmst_reg __fpu_stmm5;
 struct __darwin_mmst_reg __fpu_stmm6;
 struct __darwin_mmst_reg __fpu_stmm7;
 struct __darwin_xmm_reg __fpu_xmm0;
 struct __darwin_xmm_reg __fpu_xmm1;
 struct __darwin_xmm_reg __fpu_xmm2;
 struct __darwin_xmm_reg __fpu_xmm3;
 struct __darwin_xmm_reg __fpu_xmm4;
 struct __darwin_xmm_reg __fpu_xmm5;
 struct __darwin_xmm_reg __fpu_xmm6;
 struct __darwin_xmm_reg __fpu_xmm7;
 struct __darwin_xmm_reg __fpu_xmm8;
 struct __darwin_xmm_reg __fpu_xmm9;
 struct __darwin_xmm_reg __fpu_xmm10;
 struct __darwin_xmm_reg __fpu_xmm11;
 struct __darwin_xmm_reg __fpu_xmm12;
 struct __darwin_xmm_reg __fpu_xmm13;
 struct __darwin_xmm_reg __fpu_xmm14;
 struct __darwin_xmm_reg __fpu_xmm15;
 char __fpu_rsrv4[6*16];
 int __fpu_reserved1;
};
struct __darwin_x86_avx_state64
{
 int __fpu_reserved[2];
 struct __darwin_fp_control __fpu_fcw;
 struct __darwin_fp_status __fpu_fsw;
 __uint8_t __fpu_ftw;
 __uint8_t __fpu_rsrv1;
 __uint16_t __fpu_fop;
 __uint32_t __fpu_ip;
 __uint16_t __fpu_cs;
 __uint16_t __fpu_rsrv2;
 __uint32_t __fpu_dp;
 __uint16_t __fpu_ds;
 __uint16_t __fpu_rsrv3;
 __uint32_t __fpu_mxcsr;
 __uint32_t __fpu_mxcsrmask;
 struct __darwin_mmst_reg __fpu_stmm0;
 struct __darwin_mmst_reg __fpu_stmm1;
 struct __darwin_mmst_reg __fpu_stmm2;
 struct __darwin_mmst_reg __fpu_stmm3;
 struct __darwin_mmst_reg __fpu_stmm4;
 struct __darwin_mmst_reg __fpu_stmm5;
 struct __darwin_mmst_reg __fpu_stmm6;
 struct __darwin_mmst_reg __fpu_stmm7;
 struct __darwin_xmm_reg __fpu_xmm0;
 struct __darwin_xmm_reg __fpu_xmm1;
 struct __darwin_xmm_reg __fpu_xmm2;
 struct __darwin_xmm_reg __fpu_xmm3;
 struct __darwin_xmm_reg __fpu_xmm4;
 struct __darwin_xmm_reg __fpu_xmm5;
 struct __darwin_xmm_reg __fpu_xmm6;
 struct __darwin_xmm_reg __fpu_xmm7;
 struct __darwin_xmm_reg __fpu_xmm8;
 struct __darwin_xmm_reg __fpu_xmm9;
 struct __darwin_xmm_reg __fpu_xmm10;
 struct __darwin_xmm_reg __fpu_xmm11;
 struct __darwin_xmm_reg __fpu_xmm12;
 struct __darwin_xmm_reg __fpu_xmm13;
 struct __darwin_xmm_reg __fpu_xmm14;
 struct __darwin_xmm_reg __fpu_xmm15;
 char __fpu_rsrv4[6*16];
 int __fpu_reserved1;
 char __avx_reserved1[64];
 struct __darwin_xmm_reg __fpu_ymmh0;
 struct __darwin_xmm_reg __fpu_ymmh1;
 struct __darwin_xmm_reg __fpu_ymmh2;
 struct __darwin_xmm_reg __fpu_ymmh3;
 struct __darwin_xmm_reg __fpu_ymmh4;
 struct __darwin_xmm_reg __fpu_ymmh5;
 struct __darwin_xmm_reg __fpu_ymmh6;
 struct __darwin_xmm_reg __fpu_ymmh7;
 struct __darwin_xmm_reg __fpu_ymmh8;
 struct __darwin_xmm_reg __fpu_ymmh9;
 struct __darwin_xmm_reg __fpu_ymmh10;
 struct __darwin_xmm_reg __fpu_ymmh11;
 struct __darwin_xmm_reg __fpu_ymmh12;
 struct __darwin_xmm_reg __fpu_ymmh13;
 struct __darwin_xmm_reg __fpu_ymmh14;
 struct __darwin_xmm_reg __fpu_ymmh15;
};
struct __darwin_x86_avx512_state64
{
 int __fpu_reserved[2];
 struct __darwin_fp_control __fpu_fcw;
 struct __darwin_fp_status __fpu_fsw;
 __uint8_t __fpu_ftw;
 __uint8_t __fpu_rsrv1;
 __uint16_t __fpu_fop;
 __uint32_t __fpu_ip;
 __uint16_t __fpu_cs;
 __uint16_t __fpu_rsrv2;
 __uint32_t __fpu_dp;
 __uint16_t __fpu_ds;
 __uint16_t __fpu_rsrv3;
 __uint32_t __fpu_mxcsr;
 __uint32_t __fpu_mxcsrmask;
 struct __darwin_mmst_reg __fpu_stmm0;
 struct __darwin_mmst_reg __fpu_stmm1;
 struct __darwin_mmst_reg __fpu_stmm2;
 struct __darwin_mmst_reg __fpu_stmm3;
 struct __darwin_mmst_reg __fpu_stmm4;
 struct __darwin_mmst_reg __fpu_stmm5;
 struct __darwin_mmst_reg __fpu_stmm6;
 struct __darwin_mmst_reg __fpu_stmm7;
 struct __darwin_xmm_reg __fpu_xmm0;
 struct __darwin_xmm_reg __fpu_xmm1;
 struct __darwin_xmm_reg __fpu_xmm2;
 struct __darwin_xmm_reg __fpu_xmm3;
 struct __darwin_xmm_reg __fpu_xmm4;
 struct __darwin_xmm_reg __fpu_xmm5;
 struct __darwin_xmm_reg __fpu_xmm6;
 struct __darwin_xmm_reg __fpu_xmm7;
 struct __darwin_xmm_reg __fpu_xmm8;
 struct __darwin_xmm_reg __fpu_xmm9;
 struct __darwin_xmm_reg __fpu_xmm10;
 struct __darwin_xmm_reg __fpu_xmm11;
 struct __darwin_xmm_reg __fpu_xmm12;
 struct __darwin_xmm_reg __fpu_xmm13;
 struct __darwin_xmm_reg __fpu_xmm14;
 struct __darwin_xmm_reg __fpu_xmm15;
 char __fpu_rsrv4[6*16];
 int __fpu_reserved1;
 char __avx_reserved1[64];
 struct __darwin_xmm_reg __fpu_ymmh0;
 struct __darwin_xmm_reg __fpu_ymmh1;
 struct __darwin_xmm_reg __fpu_ymmh2;
 struct __darwin_xmm_reg __fpu_ymmh3;
 struct __darwin_xmm_reg __fpu_ymmh4;
 struct __darwin_xmm_reg __fpu_ymmh5;
 struct __darwin_xmm_reg __fpu_ymmh6;
 struct __darwin_xmm_reg __fpu_ymmh7;
 struct __darwin_xmm_reg __fpu_ymmh8;
 struct __darwin_xmm_reg __fpu_ymmh9;
 struct __darwin_xmm_reg __fpu_ymmh10;
 struct __darwin_xmm_reg __fpu_ymmh11;
 struct __darwin_xmm_reg __fpu_ymmh12;
 struct __darwin_xmm_reg __fpu_ymmh13;
 struct __darwin_xmm_reg __fpu_ymmh14;
 struct __darwin_xmm_reg __fpu_ymmh15;
 struct __darwin_opmask_reg __fpu_k0;
 struct __darwin_opmask_reg __fpu_k1;
 struct __darwin_opmask_reg __fpu_k2;
 struct __darwin_opmask_reg __fpu_k3;
 struct __darwin_opmask_reg __fpu_k4;
 struct __darwin_opmask_reg __fpu_k5;
 struct __darwin_opmask_reg __fpu_k6;
 struct __darwin_opmask_reg __fpu_k7;
 struct __darwin_ymm_reg __fpu_zmmh0;
 struct __darwin_ymm_reg __fpu_zmmh1;
 struct __darwin_ymm_reg __fpu_zmmh2;
 struct __darwin_ymm_reg __fpu_zmmh3;
 struct __darwin_ymm_reg __fpu_zmmh4;
 struct __darwin_ymm_reg __fpu_zmmh5;
 struct __darwin_ymm_reg __fpu_zmmh6;
 struct __darwin_ymm_reg __fpu_zmmh7;
 struct __darwin_ymm_reg __fpu_zmmh8;
 struct __darwin_ymm_reg __fpu_zmmh9;
 struct __darwin_ymm_reg __fpu_zmmh10;
 struct __darwin_ymm_reg __fpu_zmmh11;
 struct __darwin_ymm_reg __fpu_zmmh12;
 struct __darwin_ymm_reg __fpu_zmmh13;
 struct __darwin_ymm_reg __fpu_zmmh14;
 struct __darwin_ymm_reg __fpu_zmmh15;
 struct __darwin_zmm_reg __fpu_zmm16;
 struct __darwin_zmm_reg __fpu_zmm17;
 struct __darwin_zmm_reg __fpu_zmm18;
 struct __darwin_zmm_reg __fpu_zmm19;
 struct __darwin_zmm_reg __fpu_zmm20;
 struct __darwin_zmm_reg __fpu_zmm21;
 struct __darwin_zmm_reg __fpu_zmm22;
 struct __darwin_zmm_reg __fpu_zmm23;
 struct __darwin_zmm_reg __fpu_zmm24;
 struct __darwin_zmm_reg __fpu_zmm25;
 struct __darwin_zmm_reg __fpu_zmm26;
 struct __darwin_zmm_reg __fpu_zmm27;
 struct __darwin_zmm_reg __fpu_zmm28;
 struct __darwin_zmm_reg __fpu_zmm29;
 struct __darwin_zmm_reg __fpu_zmm30;
 struct __darwin_zmm_reg __fpu_zmm31;
};
struct __darwin_x86_exception_state64
{
    __uint16_t __trapno;
    __uint16_t __cpu;
    __uint32_t __err;
    __uint64_t __faultvaddr;
};
struct __darwin_x86_debug_state64
{
 __uint64_t __dr0;
 __uint64_t __dr1;
 __uint64_t __dr2;
 __uint64_t __dr3;
 __uint64_t __dr4;
 __uint64_t __dr5;
 __uint64_t __dr6;
 __uint64_t __dr7;
};
struct __darwin_x86_cpmu_state64
{
 __uint64_t __ctrs[16];
};
struct __darwin_mcontext32
{
 struct __darwin_i386_exception_state __es;
 struct __darwin_i386_thread_state __ss;
 struct __darwin_i386_float_state __fs;
};
struct __darwin_mcontext_avx32
{
 struct __darwin_i386_exception_state __es;
 struct __darwin_i386_thread_state __ss;
 struct __darwin_i386_avx_state __fs;
};
struct __darwin_mcontext_avx512_32
{
 struct __darwin_i386_exception_state __es;
 struct __darwin_i386_thread_state __ss;
 struct __darwin_i386_avx512_state __fs;
};
struct __darwin_mcontext64
{
 struct __darwin_x86_exception_state64 __es;
 struct __darwin_x86_thread_state64 __ss;
 struct __darwin_x86_float_state64 __fs;
};
struct __darwin_mcontext_avx64
{
 struct __darwin_x86_exception_state64 __es;
 struct __darwin_x86_thread_state64 __ss;
 struct __darwin_x86_avx_state64 __fs;
};
struct __darwin_mcontext_avx512_64
{
 struct __darwin_x86_exception_state64 __es;
 struct __darwin_x86_thread_state64 __ss;
 struct __darwin_x86_avx512_state64 __fs;
};
typedef struct __darwin_mcontext64 *mcontext_t;
typedef __darwin_pthread_attr_t pthread_attr_t;
struct __darwin_sigaltstack
{
 void *ss_sp;
 __darwin_size_t ss_size;
 int ss_flags;
};
typedef struct __darwin_sigaltstack stack_t;
struct __darwin_ucontext
{
 int uc_onstack;
 __darwin_sigset_t uc_sigmask;
 struct __darwin_sigaltstack uc_stack;
 struct __darwin_ucontext *uc_link;
 __darwin_size_t uc_mcsize;
 struct __darwin_mcontext64 *uc_mcontext;
};
typedef struct __darwin_ucontext ucontext_t;
typedef __darwin_sigset_t sigset_t;
typedef __darwin_uid_t uid_t;
union sigval {
 int sival_int;
 void *sival_ptr;
};
struct sigevent {
 int sigev_notify;
 int sigev_signo;
 union sigval sigev_value;
 void (*sigev_notify_function)(union sigval);
 pthread_attr_t *sigev_notify_attributes;
};
typedef struct __siginfo {
 int si_signo;
 int si_errno;
 int si_code;
 pid_t si_pid;
 uid_t si_uid;
 int si_status;
 void *si_addr;
 union sigval si_value;
 long si_band;
 unsigned long __pad[7];
} siginfo_t;
union __sigaction_u {
 void (*__sa_handler)(int);
 void (*__sa_sigaction)(int, struct __siginfo *,
         void *);
};
struct __sigaction {
 union __sigaction_u __sigaction_u;
 void (*sa_tramp)(void *, int, int, siginfo_t *, void *);
 sigset_t sa_mask;
 int sa_flags;
};
struct sigaction {
 union __sigaction_u __sigaction_u;
 sigset_t sa_mask;
 int sa_flags;
};
typedef void (*sig_t)(int);
struct sigvec {
 void (*sv_handler)(int);
 int sv_mask;
 int sv_flags;
};
struct sigstack {
 char *ss_sp;
 int ss_onstack;
};

void (*signal(int, void (*)(int)))(int);

typedef unsigned char uint8_t;
typedef unsigned short uint16_t;
typedef unsigned int uint32_t;
typedef unsigned long long uint64_t;
typedef int8_t int_least8_t;
typedef int16_t int_least16_t;
typedef int32_t int_least32_t;
typedef int64_t int_least64_t;
typedef uint8_t uint_least8_t;
typedef uint16_t uint_least16_t;
typedef uint32_t uint_least32_t;
typedef uint64_t uint_least64_t;
typedef int8_t int_fast8_t;
typedef int16_t int_fast16_t;
typedef int32_t int_fast32_t;
typedef int64_t int_fast64_t;
typedef uint8_t uint_fast8_t;
typedef uint16_t uint_fast16_t;
typedef uint32_t uint_fast32_t;
typedef uint64_t uint_fast64_t;
typedef long int intmax_t;
typedef long unsigned int uintmax_t;
struct timeval
{
 __darwin_time_t tv_sec;
 __darwin_suseconds_t tv_usec;
};
typedef __uint64_t rlim_t;
struct rusage {
 struct timeval ru_utime;
 struct timeval ru_stime;
 long ru_maxrss;
 long ru_ixrss;
 long ru_idrss;
 long ru_isrss;
 long ru_minflt;
 long ru_majflt;
 long ru_nswap;
 long ru_inblock;
 long ru_oublock;
 long ru_msgsnd;
 long ru_msgrcv;
 long ru_nsignals;
 long ru_nvcsw;
 long ru_nivcsw;
};
typedef void *rusage_info_t;
struct rusage_info_v0 {
 uint8_t ri_uuid[16];
 uint64_t ri_user_time;
 uint64_t ri_system_time;
 uint64_t ri_pkg_idle_wkups;
 uint64_t ri_interrupt_wkups;
 uint64_t ri_pageins;
 uint64_t ri_wired_size;
 uint64_t ri_resident_size;
 uint64_t ri_phys_footprint;
 uint64_t ri_proc_start_abstime;
 uint64_t ri_proc_exit_abstime;
};
struct rusage_info_v1 {
 uint8_t ri_uuid[16];
 uint64_t ri_user_time;
 uint64_t ri_system_time;
 uint64_t ri_pkg_idle_wkups;
 uint64_t ri_interrupt_wkups;
 uint64_t ri_pageins;
 uint64_t ri_wired_size;
 uint64_t ri_resident_size;
 uint64_t ri_phys_footprint;
 uint64_t ri_proc_start_abstime;
 uint64_t ri_proc_exit_abstime;
 uint64_t ri_child_user_time;
 uint64_t ri_child_system_time;
 uint64_t ri_child_pkg_idle_wkups;
 uint64_t ri_child_interrupt_wkups;
 uint64_t ri_child_pageins;
 uint64_t ri_child_elapsed_abstime;
};
struct rusage_info_v2 {
 uint8_t ri_uuid[16];
 uint64_t ri_user_time;
 uint64_t ri_system_time;
 uint64_t ri_pkg_idle_wkups;
 uint64_t ri_interrupt_wkups;
 uint64_t ri_pageins;
 uint64_t ri_wired_size;
 uint64_t ri_resident_size;
 uint64_t ri_phys_footprint;
 uint64_t ri_proc_start_abstime;
 uint64_t ri_proc_exit_abstime;
 uint64_t ri_child_user_time;
 uint64_t ri_child_system_time;
 uint64_t ri_child_pkg_idle_wkups;
 uint64_t ri_child_interrupt_wkups;
 uint64_t ri_child_pageins;
 uint64_t ri_child_elapsed_abstime;
 uint64_t ri_diskio_bytesread;
 uint64_t ri_diskio_byteswritten;
};
struct rusage_info_v3 {
 uint8_t ri_uuid[16];
 uint64_t ri_user_time;
 uint64_t ri_system_time;
 uint64_t ri_pkg_idle_wkups;
 uint64_t ri_interrupt_wkups;
 uint64_t ri_pageins;
 uint64_t ri_wired_size;
 uint64_t ri_resident_size;
 uint64_t ri_phys_footprint;
 uint64_t ri_proc_start_abstime;
 uint64_t ri_proc_exit_abstime;
 uint64_t ri_child_user_time;
 uint64_t ri_child_system_time;
 uint64_t ri_child_pkg_idle_wkups;
 uint64_t ri_child_interrupt_wkups;
 uint64_t ri_child_pageins;
 uint64_t ri_child_elapsed_abstime;
 uint64_t ri_diskio_bytesread;
 uint64_t ri_diskio_byteswritten;
 uint64_t ri_cpu_time_qos_default;
 uint64_t ri_cpu_time_qos_maintenance;
 uint64_t ri_cpu_time_qos_background;
 uint64_t ri_cpu_time_qos_utility;
 uint64_t ri_cpu_time_qos_legacy;
 uint64_t ri_cpu_time_qos_user_initiated;
 uint64_t ri_cpu_time_qos_user_interactive;
 uint64_t ri_billed_system_time;
 uint64_t ri_serviced_system_time;
};
struct rusage_info_v4 {
 uint8_t ri_uuid[16];
 uint64_t ri_user_time;
 uint64_t ri_system_time;
 uint64_t ri_pkg_idle_wkups;
 uint64_t ri_interrupt_wkups;
 uint64_t ri_pageins;
 uint64_t ri_wired_size;
 uint64_t ri_resident_size;
 uint64_t ri_phys_footprint;
 uint64_t ri_proc_start_abstime;
 uint64_t ri_proc_exit_abstime;
 uint64_t ri_child_user_time;
 uint64_t ri_child_system_time;
 uint64_t ri_child_pkg_idle_wkups;
 uint64_t ri_child_interrupt_wkups;
 uint64_t ri_child_pageins;
 uint64_t ri_child_elapsed_abstime;
 uint64_t ri_diskio_bytesread;
 uint64_t ri_diskio_byteswritten;
 uint64_t ri_cpu_time_qos_default;
 uint64_t ri_cpu_time_qos_maintenance;
 uint64_t ri_cpu_time_qos_background;
 uint64_t ri_cpu_time_qos_utility;
 uint64_t ri_cpu_time_qos_legacy;
 uint64_t ri_cpu_time_qos_user_initiated;
 uint64_t ri_cpu_time_qos_user_interactive;
 uint64_t ri_billed_system_time;
 uint64_t ri_serviced_system_time;
 uint64_t ri_logical_writes;
 uint64_t ri_lifetime_max_phys_footprint;
 uint64_t ri_instructions;
 uint64_t ri_cycles;
 uint64_t ri_billed_energy;
 uint64_t ri_serviced_energy;
 uint64_t ri_unused[2];
};
typedef struct rusage_info_v4 rusage_info_current;
struct rlimit {
 rlim_t rlim_cur;
 rlim_t rlim_max;
};
struct proc_rlimit_control_wakeupmon {
 uint32_t wm_flags;
 int32_t wm_rate;
};

int getpriority(int, id_t);
int getiopolicy_np(int, int) ;
int getrlimit(int, struct rlimit *) __asm("_" "getrlimit" );
int getrusage(int, struct rusage *);
int setpriority(int, id_t, int);
int setiopolicy_np(int, int, int) ;
int setrlimit(int, const struct rlimit *) __asm("_" "setrlimit" );

static inline
__uint16_t
_OSSwapInt16(
    __uint16_t _data
)
{
    return ((__uint16_t)((_data << 8) | (_data >> 8)));
}
static inline
__uint32_t
_OSSwapInt32(
    __uint32_t _data
)
{
    __asm__ ("bswap   %0" : "+r" (_data));
    return _data;
}
static inline
__uint64_t
_OSSwapInt64(
    __uint64_t _data
)
{
    __asm__ ("bswap   %0" : "+r" (_data));
    return _data;
}
union wait {
 int w_status;
 struct {
  unsigned int w_Termsig:7,
    w_Coredump:1,
    w_Retcode:8,
    w_Filler:16;
 } w_T;
 struct {
  unsigned int w_Stopval:8,
    w_Stopsig:8,
    w_Filler:16;
 } w_S;
};

pid_t wait(int *) __asm("_" "wait" );
pid_t waitpid(pid_t, int *, int) __asm("_" "waitpid" );
int waitid(idtype_t, id_t, siginfo_t *, int) __asm("_" "waitid" );
pid_t wait3(int *, int, struct rusage *);
pid_t wait4(pid_t, int *, int, struct rusage *);


void *alloca(size_t);

typedef __darwin_ct_rune_t ct_rune_t;
typedef __darwin_rune_t rune_t;
typedef __darwin_wchar_t wchar_t;
typedef struct {
 int quot;
 int rem;
} div_t;
typedef struct {
 long quot;
 long rem;
} ldiv_t;
typedef struct {
 long long quot;
 long long rem;
} lldiv_t;
extern int __mb_cur_max;

void abort(void) __attribute__((noreturn));
int abs(int) __attribute__((const));
int atexit(void (* )(void));
double atof(const char *);
int atoi(const char *);
long atol(const char *);
long long
  atoll(const char *);
void *bsearch(const void *__key, const void *__base, size_t __nel,
     size_t __width, int (* __compar)(const void *, const void *));
void *calloc(size_t __count, size_t __size) __attribute__((__warn_unused_result__)) __attribute__((alloc_size(1,2)));
div_t div(int, int) __attribute__((const));
void exit(int) __attribute__((noreturn));
void free(void *);
char *getenv(const char *);
long labs(long) __attribute__((const));
ldiv_t ldiv(long, long) __attribute__((const));
long long
  llabs(long long);
lldiv_t lldiv(long long, long long);
void *malloc(size_t __size) __attribute__((__warn_unused_result__)) __attribute__((alloc_size(1)));
int mblen(const char *__s, size_t __n);
size_t mbstowcs(wchar_t * restrict , const char * restrict, size_t);
int mbtowc(wchar_t * restrict, const char * restrict, size_t);
int posix_memalign(void **__memptr, size_t __alignment, size_t __size) ;
void qsort(void *__base, size_t __nel, size_t __width,
     int (* __compar)(const void *, const void *));
int rand(void) ;
void *realloc(void *__ptr, size_t __size) __attribute__((__warn_unused_result__)) __attribute__((alloc_size(2)));
void srand(unsigned) ;
double strtod(const char *, char **) __asm("_" "strtod" );
float strtof(const char *, char **) __asm("_" "strtof" );
long strtol(const char *__str, char **__endptr, int __base);
long double
  strtold(const char *, char **);
long long
  strtoll(const char *__str, char **__endptr, int __base);
unsigned long
  strtoul(const char *__str, char **__endptr, int __base);
unsigned long long
  strtoull(const char *__str, char **__endptr, int __base);



int system(const char *) __asm("_" "system" );
size_t wcstombs(char * restrict, const wchar_t * restrict, size_t);
int wctomb(char *, wchar_t);
void _Exit(int) __attribute__((noreturn));
long a64l(const char *);
double drand48(void);
char *ecvt(double, int, int *restrict, int *restrict);
double erand48(unsigned short[3]);
char *fcvt(double, int, int *restrict, int *restrict);
char *gcvt(double, int, char *);
int getsubopt(char **, char * const *, char **);
int grantpt(int);
char *initstate(unsigned, char *, size_t);
long jrand48(unsigned short[3]) ;
char *l64a(long);
void lcong48(unsigned short[7]);
long lrand48(void) ;
char *mktemp(char *);
int mkstemp(char *);
long mrand48(void) ;
long nrand48(unsigned short[3]) ;
int posix_openpt(int);
char *ptsname(int);
int ptsname_r(int fildes, char *buffer, size_t buflen) ;
int putenv(char *) __asm("_" "putenv" );
long random(void) ;
int rand_r(unsigned *) ;
char *realpath(const char * restrict, char * restrict) __asm("_" "realpath" "$DARWIN_EXTSN");
unsigned short
 *seed48(unsigned short[3]);
int setenv(const char * __name, const char * __value, int __overwrite) __asm("_" "setenv" );
void setkey(const char *) __asm("_" "setkey" );
char *setstate(const char *);
void srand48(long);
void srandom(unsigned);
int unlockpt(int);
int unsetenv(const char *) __asm("_" "unsetenv" );
typedef __darwin_dev_t dev_t;
typedef __darwin_mode_t mode_t;
uint32_t arc4random(void);
void arc4random_addrandom(unsigned char * , int )
   
   
   
    ;
void arc4random_buf(void * __buf, size_t __nbytes) ;
void arc4random_stir(void);
uint32_t
  arc4random_uniform(uint32_t __upper_bound) ;
char *cgetcap(char *, const char *, int);
int cgetclose(void);
int cgetent(char **, char **, const char *);
int cgetfirst(char **, char **);
int cgetmatch(const char *, const char *);
int cgetnext(char **, char **);
int cgetnum(char *, const char *, long *);
int cgetset(const char *);
int cgetstr(char *, const char *, char **);
int cgetustr(char *, const char *, char **);
int daemon(int, int) __asm("_" "daemon" "$1050") __attribute__((deprecated)) ;
char *devname(dev_t, mode_t);
char *devname_r(dev_t, mode_t, char *buf, int len);
char *getbsize(int *, long *);
int getloadavg(double [], int);
const char
 *getprogname(void);
int heapsort(void *__base, size_t __nel, size_t __width,
     int (* __compar)(const void *, const void *));
int mergesort(void *__base, size_t __nel, size_t __width,
     int (* __compar)(const void *, const void *));
void psort(void *__base, size_t __nel, size_t __width,
     int (* __compar)(const void *, const void *)) ;
void psort_r(void *__base, size_t __nel, size_t __width, void *,
     int (* __compar)(void *, const void *, const void *)) ;
void qsort_r(void *__base, size_t __nel, size_t __width, void *,
     int (* __compar)(void *, const void *, const void *));
int radixsort(const unsigned char **__base, int __nel, const unsigned char *__table,
     unsigned __endbyte);
void setprogname(const char *);
int sradixsort(const unsigned char **__base, int __nel, const unsigned char *__table,
     unsigned __endbyte);
void sranddev(void);
void srandomdev(void);
void *reallocf(void *__ptr, size_t __size) __attribute__((alloc_size(2)));
long long
  strtoq(const char *__str, char **__endptr, int __base);
unsigned long long
  strtouq(const char *__str, char **__endptr, int __base);
extern char *suboptarg;
void *valloc(size_t) __attribute__((alloc_size(1)));


typedef struct fd_set {
 __int32_t fds_bits[((((1024) % ((sizeof(__int32_t) * 8))) == 0) ? ((1024) / ((sizeof(__int32_t) * 8))) : (((1024) / ((sizeof(__int32_t) * 8))) + 1))];
} fd_set;

static __inline int
__darwin_fd_isset(int _n, const struct fd_set *_p)
{
 return (_p->fds_bits[(unsigned long)_n/(sizeof(__int32_t) * 8)] & ((__int32_t)(((unsigned long)1)<<((unsigned long)_n % (sizeof(__int32_t) * 8)))));
}
struct timespec
{
 __darwin_time_t tv_sec;
 long tv_nsec;
};
struct timeval64
{
 __int64_t tv_sec;
 __int64_t tv_usec;
};
typedef __darwin_time_t time_t;
typedef __darwin_suseconds_t suseconds_t;
struct itimerval {
 struct timeval it_interval;
 struct timeval it_value;
};
struct timezone {
 int tz_minuteswest;
 int tz_dsttime;
};
struct clockinfo {
 int hz;
 int tick;
 int tickadj;
 int stathz;
 int profhz;
};
typedef __darwin_clock_t clock_t;
struct tm {
 int tm_sec;
 int tm_min;
 int tm_hour;
 int tm_mday;
 int tm_mon;
 int tm_year;
 int tm_wday;
 int tm_yday;
 int tm_isdst;
 long tm_gmtoff;
 char *tm_zone;
};
extern char *tzname[];
extern int getdate_err;
extern long timezone __asm("_" "timezone" );
extern int daylight;

char *asctime(const struct tm *);
clock_t clock(void) __asm("_" "clock" );
char *ctime(const time_t *);
double difftime(time_t, time_t);
struct tm *getdate(const char *);
struct tm *gmtime(const time_t *);
struct tm *localtime(const time_t *);
time_t mktime(struct tm *) __asm("_" "mktime" );
size_t strftime(char * restrict, size_t, const char * restrict, const struct tm * restrict) __asm("_" "strftime" );
char *strptime(const char * restrict, const char * restrict, struct tm * restrict) __asm("_" "strptime" );
time_t time(time_t *);
void tzset(void);
char *asctime_r(const struct tm * restrict, char * restrict);
char *ctime_r(const time_t *, char *);
struct tm *gmtime_r(const time_t * restrict, struct tm * restrict);
struct tm *localtime_r(const time_t * restrict, struct tm * restrict);
time_t posix2time(time_t);
void tzsetwall(void);
time_t time2posix(time_t);
time_t timelocal(struct tm * const);
time_t timegm(struct tm * const);
int nanosleep(const struct timespec *__rqtp, struct timespec *__rmtp) __asm("_" "nanosleep" );
typedef enum {
_CLOCK_REALTIME = 0,
_CLOCK_MONOTONIC = 6,
_CLOCK_MONOTONIC_RAW = 4,
_CLOCK_MONOTONIC_RAW_APPROX = 5,
_CLOCK_UPTIME_RAW = 8,
_CLOCK_UPTIME_RAW_APPROX = 9,
_CLOCK_PROCESS_CPUTIME_ID = 12,
_CLOCK_THREAD_CPUTIME_ID = 16
} clockid_t;

int clock_getres(clockid_t __clock_id, struct timespec *__res);

int clock_gettime(clockid_t __clock_id, struct timespec *__tp);

__uint64_t clock_gettime_nsec_np(clockid_t __clock_id);


int clock_settime(clockid_t __clock_id, const struct timespec *__tp);


int adjtime(const struct timeval *, struct timeval *);
int futimes(int, const struct timeval *);
int lutimes(const char *, const struct timeval *) ;
int settimeofday(const struct timeval *, const struct timezone *);
int getitimer(int, struct itimerval *);
int gettimeofday(struct timeval * restrict, void * restrict);
int select(int, fd_set * restrict, fd_set * restrict,
  fd_set * restrict, struct timeval * restrict)
  __asm("_" "select" "$1050")
  ;
int setitimer(int, const struct itimerval * restrict,
  struct itimerval * restrict);
int utimes(const char *, const struct timeval *);

struct tms {
 clock_t tms_utime;
 clock_t tms_stime;
 clock_t tms_cutime;
 clock_t tms_cstime;
};

clock_t times(struct tms *);

struct accessx_descriptor {
 unsigned int ad_name_offset;
 int ad_flags;
 int ad_pad[2];
};

int getattrlistbulk(int, void *, void *, size_t, uint64_t) ;
int getattrlistat(int, const char *, void *, void *, size_t, unsigned long) ;
int setattrlistat(int, const char *, void *, void *, size_t, uint32_t) ;

typedef __darwin_gid_t gid_t;

int faccessat(int, const char *, int, int) ;
int fchownat(int, const char *, uid_t, gid_t, int) ;
int linkat(int, const char *, int, const char *, int) ;
ssize_t readlinkat(int, const char *, char *, size_t) ;
int symlinkat(const char *, int, const char *) ;
int unlinkat(int, const char *, int) ;

typedef __darwin_useconds_t useconds_t;

void _exit(int) __attribute__((noreturn));
int access(const char *, int);
unsigned int
  alarm(unsigned int);
int chdir(const char *);
int chown(const char *, uid_t, gid_t);
int close(int) __asm("_" "close" );
int dup(int);
int dup2(int, int);
int execl(const char * __path, const char * __arg0, ...) ;
int execle(const char * __path, const char * __arg0, ...) ;
int execlp(const char * __file, const char * __arg0, ...) ;
int execv(const char * __path, char * const * __argv) ;
int execve(const char * __file, char * const * __argv, char * const * __envp) ;
int execvp(const char * __file, char * const * __argv) ;
pid_t fork(void) ;
long fpathconf(int, int);
char *getcwd(char *, size_t);
gid_t getegid(void);
uid_t geteuid(void);
gid_t getgid(void);
int getgroups(int, gid_t []);
char *getlogin(void);
pid_t getpgrp(void);
pid_t getpid(void);
pid_t getppid(void);
uid_t getuid(void);
int isatty(int);
int link(const char *, const char *);
off_t lseek(int, off_t, int);
long pathconf(const char *, int);
int pause(void) __asm("_" "pause" );
int pipe(int [2]);
ssize_t read(int, void *, size_t) __asm("_" "read" );
int rmdir(const char *);
int setgid(gid_t);
int setpgid(pid_t, pid_t);
pid_t setsid(void);
int setuid(uid_t);
unsigned int
  sleep(unsigned int) __asm("_" "sleep" );
long sysconf(int);
pid_t tcgetpgrp(int);
int tcsetpgrp(int, pid_t);
char *ttyname(int);
int ttyname_r(int, char *, size_t) __asm("_" "ttyname_r" );
int unlink(const char *);
ssize_t write(int __fd, const void * __buf, size_t __nbyte) __asm("_" "write" );


size_t confstr(int, char *, size_t) __asm("_" "confstr" );
int getopt(int, char * const [], const char *) __asm("_" "getopt" );
extern char *optarg;
extern int optind, opterr, optopt;


__attribute__((deprecated))
void *brk(const void *);
int chroot(const char *) ;
char *crypt(const char *, const char *);
void encrypt(char *, int) __asm("_" "encrypt" );
int fchdir(int);
long gethostid(void);
pid_t getpgid(pid_t);
pid_t getsid(pid_t);
int getdtablesize(void) ;
int getpagesize(void) __attribute__((const)) ;
char *getpass(const char *) ;
char *getwd(char *) ;
int lchown(const char *, uid_t, gid_t) __asm("_" "lchown" );
int lockf(int, int, off_t) __asm("_" "lockf" );
int nice(int) __asm("_" "nice" );
ssize_t pread(int __fd, void * __buf, size_t __nbyte, off_t __offset) __asm("_" "pread" );
ssize_t pwrite(int __fd, const void * __buf, size_t __nbyte, off_t __offset) __asm("_" "pwrite" );
__attribute__((deprecated))
void *sbrk(int);
pid_t setpgrp(void) __asm("_" "setpgrp" );
int setregid(gid_t, gid_t) __asm("_" "setregid" );
int setreuid(uid_t, uid_t) __asm("_" "setreuid" );
void swab(const void * restrict, void * restrict, ssize_t);
void sync(void);
int truncate(const char *, off_t);
useconds_t ualarm(useconds_t, useconds_t);
int usleep(useconds_t) __asm("_" "usleep" );
pid_t vfork(void) ;
int fsync(int) __asm("_" "fsync" );
int ftruncate(int, off_t);
int getlogin_r(char *, size_t);


int fchown(int, uid_t, gid_t);
int gethostname(char *, size_t);
ssize_t readlink(const char * restrict, char * restrict, size_t);
int setegid(gid_t);
int seteuid(uid_t);
int symlink(const char *, const char *);


int pselect(int, fd_set * restrict, fd_set * restrict,
  fd_set * restrict, const struct timespec * restrict,
  const sigset_t * restrict)
  __asm("_" "pselect" "$1050")
  ;

typedef __darwin_uuid_t uuid_t;

void _Exit(int) __attribute__((noreturn));
int accessx_np(const struct accessx_descriptor *, size_t, int *, uid_t);
int acct(const char *);
int add_profil(char *, size_t, unsigned long, unsigned int) ;
void endusershell(void);
int execvP(const char * __file, const char * __searchpath, char * const * __argv) ;
char *fflagstostr(unsigned long);
int getdomainname(char *, int);
int getgrouplist(const char *, int, int *, int *);
int gethostuuid(uuid_t, const struct timespec *) ;
mode_t getmode(const void *, mode_t);
int getpeereid(int, uid_t *, gid_t *);
int getsgroups_np(int *, uuid_t);
char *getusershell(void);
int getwgroups_np(int *, uuid_t);
int initgroups(const char *, int);
int issetugid(void);
char *mkdtemp(char *);
int mknod(const char *, mode_t, dev_t);
int mkpath_np(const char *path, mode_t omode) ;
int mkpathat_np(int dfd, const char *path, mode_t omode)
 
  ;
int mkstemp(char *);
int mkstemps(char *, int);
char *mktemp(char *);
int mkostemp(char *path, int oflags)
 
  ;
int mkostemps(char *path, int slen, int oflags)
 
  ;
int mkstemp_dprotected_np(char *path, int dpclass, int dpflags)
 
  ;
char *mkdtempat_np(int dfd, char *path)
 
  ;
int mkstempsat_np(int dfd, char *path, int slen)
 
  ;
int mkostempsat_np(int dfd, char *path, int slen, int oflags)
 
  ;
int nfssvc(int, void *);
int profil(char *, size_t, unsigned long, unsigned int);
__attribute__((deprecated("Use of per-thread security contexts is error-prone and discouraged.")))
int pthread_setugid_np(uid_t, gid_t);
int pthread_getugid_np( uid_t *, gid_t *);
int reboot(int);
int revoke(const char *);
__attribute__((deprecated)) int rcmd(char **, int, const char *, const char *, const char *, int *);
__attribute__((deprecated)) int rcmd_af(char **, int, const char *, const char *, const char *, int *,
  int);
__attribute__((deprecated)) int rresvport(int *);
__attribute__((deprecated)) int rresvport_af(int *, int);
__attribute__((deprecated)) int iruserok(unsigned long, int, const char *, const char *);
__attribute__((deprecated)) int iruserok_sa(const void *, int, int, const char *, const char *);
__attribute__((deprecated)) int ruserok(const char *, int, const char *, const char *);
int setdomainname(const char *, int);
int setgroups(int, const gid_t *);
void sethostid(long);
int sethostname(const char *, int);
void setkey(const char *) __asm("_" "setkey" );
int setlogin(const char *);
void *setmode(const char *) __asm("_" "setmode" );
int setrgid(gid_t);
int setruid(uid_t);
int setsgroups_np(int, const uuid_t);
void setusershell(void);
int setwgroups_np(int, const uuid_t);
int strtofflags(char **, unsigned long *, unsigned long *);
int swapon(const char *);
int ttyslot(void);
int undelete(const char *);
int unwhiteout(const char *);
void *valloc(size_t);



int syscall(int, ...);
extern char *suboptarg;
int getsubopt(char **, char * const *, char **);
int fgetattrlist(int,void*,void*,size_t,unsigned int) ;
int fsetattrlist(int,void*,void*,size_t,unsigned int) ;
int getattrlist(const char*,void*,void*,size_t,unsigned int) __asm("_" "getattrlist" );
int setattrlist(const char*,void*,void*,size_t,unsigned int) __asm("_" "setattrlist" );
int exchangedata(const char*,const char*,unsigned int) ;
int getdirentriesattr(int,void*,void*,size_t,unsigned int*,unsigned int*,unsigned int*,unsigned int) ;
struct fssearchblock;
struct searchstate;
int searchfs(const char *, struct fssearchblock *, unsigned long *, unsigned int, unsigned int, struct searchstate *) ;
int fsctl(const char *,unsigned long,void*,unsigned int);
int ffsctl(int,unsigned long,void*,unsigned int) ;
int fsync_volume_np(int, int) ;
int sync_volume_np(const char *, int) ;
extern int optreset;


    typedef float float_t;
    typedef double double_t;
extern int __math_errhandling(void);
extern int __fpclassifyf(float);
extern int __fpclassifyd(double);
extern int __fpclassifyl(long double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isfinitef(float);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isfinited(double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isfinitel(long double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isinff(float);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isinfd(double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isinfl(long double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnanf(float);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnand(double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnanl(long double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnormalf(float);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnormald(double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnormall(long double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_signbitf(float);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_signbitd(double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_signbitl(long double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isfinitef(float __x) {
    return __x == __x && __builtin_fabsf(__x) != __builtin_inff();
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isfinited(double __x) {
    return __x == __x && __builtin_fabs(__x) != __builtin_inf();
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isfinitel(long double __x) {
    return __x == __x && __builtin_fabsl(__x) != __builtin_infl();
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isinff(float __x) {
    return __builtin_fabsf(__x) == __builtin_inff();
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isinfd(double __x) {
    return __builtin_fabs(__x) == __builtin_inf();
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isinfl(long double __x) {
    return __builtin_fabsl(__x) == __builtin_infl();
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnanf(float __x) {
    return __x != __x;
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnand(double __x) {
    return __x != __x;
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnanl(long double __x) {
    return __x != __x;
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_signbitf(float __x) {
    union { float __f; unsigned int __u; } __u;
    __u.__f = __x;
    return (int)(__u.__u >> 31);
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_signbitd(double __x) {
    union { double __f; unsigned long long __u; } __u;
    __u.__f = __x;
    return (int)(__u.__u >> 63);
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_signbitl(long double __x) {
    union {
        long double __ld;
        struct{ unsigned long long __m; unsigned short __sexp; } __p;
    } __u;
    __u.__ld = __x;
    return (int)(__u.__p.__sexp >> 15);
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnormalf(float __x) {
    return __inline_isfinitef(__x) && __builtin_fabsf(__x) >= 1.17549435082228750797e-38F;
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnormald(double __x) {
    return __inline_isfinited(__x) && __builtin_fabs(__x) >= ((double)2.22507385850720138309e-308L);
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnormall(long double __x) {
    return __inline_isfinitel(__x) && __builtin_fabsl(__x) >= 3.36210314311209350626e-4932L;
}
extern float acosf(float);
extern double acos(double);
extern long double acosl(long double);
extern float asinf(float);
extern double asin(double);
extern long double asinl(long double);
extern float atanf(float);
extern double atan(double);
extern long double atanl(long double);
extern float atan2f(float, float);
extern double atan2(double, double);
extern long double atan2l(long double, long double);
extern float cosf(float);
extern double cos(double);
extern long double cosl(long double);
extern float sinf(float);
extern double sin(double);
extern long double sinl(long double);
extern float tanf(float);
extern double tan(double);
extern long double tanl(long double);
extern float acoshf(float);
extern double acosh(double);
extern long double acoshl(long double);
extern float asinhf(float);
extern double asinh(double);
extern long double asinhl(long double);
extern float atanhf(float);
extern double atanh(double);
extern long double atanhl(long double);
extern float coshf(float);
extern double cosh(double);
extern long double coshl(long double);
extern float sinhf(float);
extern double sinh(double);
extern long double sinhl(long double);
extern float tanhf(float);
extern double tanh(double);
extern long double tanhl(long double);
extern float expf(float);
extern double exp(double);
extern long double expl(long double);
extern float exp2f(float);
extern double exp2(double);
extern long double exp2l(long double);
extern float expm1f(float);
extern double expm1(double);
extern long double expm1l(long double);
extern float logf(float);
extern double log(double);
extern long double logl(long double);
extern float log10f(float);
extern double log10(double);
extern long double log10l(long double);
extern float log2f(float);
extern double log2(double);
extern long double log2l(long double);
extern float log1pf(float);
extern double log1p(double);
extern long double log1pl(long double);
extern float logbf(float);
extern double logb(double);
extern long double logbl(long double);
extern float modff(float, float *);
extern double modf(double, double *);
extern long double modfl(long double, long double *);
extern float ldexpf(float, int);
extern double ldexp(double, int);
extern long double ldexpl(long double, int);
extern float frexpf(float, int *);
extern double frexp(double, int *);
extern long double frexpl(long double, int *);
extern int ilogbf(float);
extern int ilogb(double);
extern int ilogbl(long double);
extern float scalbnf(float, int);
extern double scalbn(double, int);
extern long double scalbnl(long double, int);
extern float scalblnf(float, long int);
extern double scalbln(double, long int);
extern long double scalblnl(long double, long int);
extern float fabsf(float);
extern double fabs(double);
extern long double fabsl(long double);
extern float cbrtf(float);
extern double cbrt(double);
extern long double cbrtl(long double);
extern float hypotf(float, float);
extern double hypot(double, double);
extern long double hypotl(long double, long double);
extern float powf(float, float);
extern double pow(double, double);
extern long double powl(long double, long double);
extern float sqrtf(float);
extern double sqrt(double);
extern long double sqrtl(long double);
extern float erff(float);
extern double erf(double);
extern long double erfl(long double);
extern float erfcf(float);
extern double erfc(double);
extern long double erfcl(long double);
extern float lgammaf(float);
extern double lgamma(double);
extern long double lgammal(long double);
extern float tgammaf(float);
extern double tgamma(double);
extern long double tgammal(long double);
extern float ceilf(float);
extern double ceil(double);
extern long double ceill(long double);
extern float floorf(float);
extern double floor(double);
extern long double floorl(long double);
extern float nearbyintf(float);
extern double nearbyint(double);
extern long double nearbyintl(long double);
extern float rintf(float);
extern double rint(double);
extern long double rintl(long double);
extern long int lrintf(float);
extern long int lrint(double);
extern long int lrintl(long double);
extern float roundf(float);
extern double round(double);
extern long double roundl(long double);
extern long int lroundf(float);
extern long int lround(double);
extern long int lroundl(long double);
extern long long int llrintf(float);
extern long long int llrint(double);
extern long long int llrintl(long double);
extern long long int llroundf(float);
extern long long int llround(double);
extern long long int llroundl(long double);
extern float truncf(float);
extern double trunc(double);
extern long double truncl(long double);
extern float fmodf(float, float);
extern double fmod(double, double);
extern long double fmodl(long double, long double);
extern float remainderf(float, float);
extern double remainder(double, double);
extern long double remainderl(long double, long double);
extern float remquof(float, float, int *);
extern double remquo(double, double, int *);
extern long double remquol(long double, long double, int *);
extern float copysignf(float, float);
extern double copysign(double, double);
extern long double copysignl(long double, long double);
extern float nanf(const char *);
extern double nan(const char *);
extern long double nanl(const char *);
extern float nextafterf(float, float);
extern double nextafter(double, double);
extern long double nextafterl(long double, long double);
extern double nexttoward(double, long double);
extern float nexttowardf(float, long double);
extern long double nexttowardl(long double, long double);
extern float fdimf(float, float);
extern double fdim(double, double);
extern long double fdiml(long double, long double);
extern float fmaxf(float, float);
extern double fmax(double, double);
extern long double fmaxl(long double, long double);
extern float fminf(float, float);
extern double fmin(double, double);
extern long double fminl(long double, long double);
extern float fmaf(float, float, float);
extern double fma(double, double, double);
extern long double fmal(long double, long double, long double);
extern float __inff(void) __attribute__((deprecated));
extern double __inf(void) __attribute__((deprecated));
extern long double __infl(void) __attribute__((deprecated));
extern float __nan(void) ;
extern float __exp10f(float) ;
extern double __exp10(double) ;
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) void __sincosf(float __x, float *__sinp, float *__cosp);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) void __sincos(double __x, double *__sinp, double *__cosp);
extern float __cospif(float) ;
extern double __cospi(double) ;
extern float __sinpif(float) ;
extern double __sinpi(double) ;
extern float __tanpif(float) ;
extern double __tanpi(double) ;
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) void __sincospif(float __x, float *__sinp, float *__cosp);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) void __sincospi(double __x, double *__sinp, double *__cosp);
struct __float2 { float __sinval; float __cosval; };
struct __double2 { double __sinval; double __cosval; };
extern struct __float2 __sincosf_stret(float);
extern struct __double2 __sincos_stret(double);
extern struct __float2 __sincospif_stret(float);
extern struct __double2 __sincospi_stret(double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) void __sincosf(float __x, float *__sinp, float *__cosp) {
    const struct __float2 __stret = __sincosf_stret(__x);
    *__sinp = __stret.__sinval; *__cosp = __stret.__cosval;
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) void __sincos(double __x, double *__sinp, double *__cosp) {
    const struct __double2 __stret = __sincos_stret(__x);
    *__sinp = __stret.__sinval; *__cosp = __stret.__cosval;
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) void __sincospif(float __x, float *__sinp, float *__cosp) {
    const struct __float2 __stret = __sincospif_stret(__x);
    *__sinp = __stret.__sinval; *__cosp = __stret.__cosval;
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) void __sincospi(double __x, double *__sinp, double *__cosp) {
    const struct __double2 __stret = __sincospi_stret(__x);
    *__sinp = __stret.__sinval; *__cosp = __stret.__cosval;
}
extern double j0(double) ;
extern double j1(double) ;
extern double jn(int, double) ;
extern double y0(double) ;
extern double y1(double) ;
extern double yn(int, double) ;
extern double scalb(double, double);
extern int signgam;
extern long int rinttol(double) __attribute__((deprecated));
extern long int roundtol(double) __attribute__((deprecated));
extern double drem(double, double) __attribute__((deprecated));
extern int finite(double) __attribute__((deprecated));
extern double gamma(double) __attribute__((deprecated));
extern double significand(double) __attribute__((deprecated));
struct exception {
    int type;
    char *name;
    double arg1;
    double arg2;
    double retval;
};
extern int matherr(struct exception *) __attribute__((deprecated));


void *memchr(const void *__s, int __c, size_t __n);
int memcmp(const void *__s1, const void *__s2, size_t __n);
void *memcpy(void *__dst, const void *__src, size_t __n);
void *memmove(void *__dst, const void *__src, size_t __len);
void *memset(void *__b, int __c, size_t __len);
char *strcat(char *__s1, const char *__s2);
char *strchr(const char *__s, int __c);
int strcmp(const char *__s1, const char *__s2);
int strcoll(const char *__s1, const char *__s2);
char *strcpy(char *__dst, const char *__src);
size_t strcspn(const char *__s, const char *__charset);
char *strerror(int __errnum) __asm("_" "strerror" );
size_t strlen(const char *__s);
char *strncat(char *__s1, const char *__s2, size_t __n);
int strncmp(const char *__s1, const char *__s2, size_t __n);
char *strncpy(char *__dst, const char *__src, size_t __n);
char *strpbrk(const char *__s, const char *__charset);
char *strrchr(const char *__s, int __c);
size_t strspn(const char *__s, const char *__charset);
char *strstr(const char *__big, const char *__little);
char *strtok(char *__str, const char *__sep);
size_t strxfrm(char *__s1, const char *__s2, size_t __n);


char *strtok_r(char *__str, const char *__sep, char **__lasts);


int strerror_r(int __errnum, char *__strerrbuf, size_t __buflen);
char *strdup(const char *__s1);
void *memccpy(void *__dst, const void *__src, int __c, size_t __n);


char *stpcpy(char *__dst, const char *__src);
char *stpncpy(char *__dst, const char *__src, size_t __n) ;
char *strndup(const char *__s1, size_t __n) ;
size_t strnlen(const char *__s1, size_t __n) ;
char *strsignal(int __sig);

typedef __darwin_size_t rsize_t;
typedef int errno_t;

errno_t memset_s(void *__s, rsize_t __smax, int __c, rsize_t __n) ;


void *memmem(const void *__big, size_t __big_len, const void *__little, size_t __little_len) ;
void memset_pattern4(void *__b, const void *__pattern4, size_t __len) ;
void memset_pattern8(void *__b, const void *__pattern8, size_t __len) ;
void memset_pattern16(void *__b, const void *__pattern16, size_t __len) ;
char *strcasestr(const char *__big, const char *__little);
char *strnstr(const char *__big, const char *__little, size_t __len);
size_t strlcat(char *__dst, const char *__source, size_t __size);
size_t strlcpy(char *__dst, const char *__source, size_t __size);
void strmode(int __mode, char *__bp);
char *strsep(char **__stringp, const char *__delim);
void swab(const void * restrict, void * restrict, ssize_t);


int timingsafe_bcmp(const void *__b1, const void *__b2, size_t __len);


int bcmp(const void *, const void *, size_t) ;
void bcopy(const void *, void *, size_t) ;
void bzero(void *, size_t) ;
char *index(const char *, int) ;
char *rindex(const char *, int) ;
int ffs(int);
int strcasecmp(const char *, const char *);
int strncasecmp(const char *, const char *, size_t);


int ffsl(long) ;
int ffsll(long long) ;
int fls(int) ;
int flsl(long) ;
int flsll(long long) ;

typedef struct
{
  unsigned char _x[64]
    __attribute__((__aligned__(8)));
} omp_lock_t;
typedef struct
{
  unsigned char _x[80]
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
extern void omp_init_nest_lock_with_hint (omp_lock_t *, omp_lock_hint_t)
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
typedef __darwin_blkcnt_t blkcnt_t;
typedef __darwin_blksize_t blksize_t;
typedef __darwin_ino_t ino_t;
typedef __darwin_ino64_t ino64_t;
typedef __uint16_t nlink_t;
struct ostat {
 __uint16_t st_dev;
 ino_t st_ino;
 mode_t st_mode;
 nlink_t st_nlink;
 __uint16_t st_uid;
 __uint16_t st_gid;
 __uint16_t st_rdev;
 __int32_t st_size;
 struct timespec st_atimespec;
 struct timespec st_mtimespec;
 struct timespec st_ctimespec;
 __int32_t st_blksize;
 __int32_t st_blocks;
 __uint32_t st_flags;
 __uint32_t st_gen;
};
struct stat { dev_t st_dev; mode_t st_mode; nlink_t st_nlink; __darwin_ino64_t st_ino; uid_t st_uid; gid_t st_gid; dev_t st_rdev; struct timespec st_atimespec; struct timespec st_mtimespec; struct timespec st_ctimespec; struct timespec st_birthtimespec; off_t st_size; blkcnt_t st_blocks; blksize_t st_blksize; __uint32_t st_flags; __uint32_t st_gen; __int32_t st_lspare; __int64_t st_qspare[2]; };
struct stat64 { dev_t st_dev; mode_t st_mode; nlink_t st_nlink; __darwin_ino64_t st_ino; uid_t st_uid; gid_t st_gid; dev_t st_rdev; struct timespec st_atimespec; struct timespec st_mtimespec; struct timespec st_ctimespec; struct timespec st_birthtimespec; off_t st_size; blkcnt_t st_blocks; blksize_t st_blksize; __uint32_t st_flags; __uint32_t st_gen; __int32_t st_lspare; __int64_t st_qspare[2]; };

int chmod(const char *, mode_t) __asm("_" "chmod" );
int fchmod(int, mode_t) __asm("_" "fchmod" );
int fstat(int, struct stat *) __asm("_" "fstat" "$INODE64");
int lstat(const char *, struct stat *) __asm("_" "lstat" "$INODE64");
int mkdir(const char *, mode_t);
int mkfifo(const char *, mode_t);
int stat(const char *, struct stat *) __asm("_" "stat" "$INODE64");
int mknod(const char *, mode_t, dev_t);
mode_t umask(mode_t);
int fchmodat(int, const char *, mode_t, int) ;
int fstatat(int, const char *, struct stat *, int) __asm("_" "fstatat" "$INODE64") ;
int mkdirat(int, const char *, mode_t) ;
int futimens(int __fd, const struct timespec __times[2]) ;
int utimensat(int __fd, const char *__path, const struct timespec __times[2],
  int __flag) ;
struct _filesec;
typedef struct _filesec *filesec_t;
int chflags(const char *, __uint32_t);
int chmodx_np(const char *, filesec_t);
int fchflags(int, __uint32_t);
int fchmodx_np(int, filesec_t);
int fstatx_np(int, struct stat *, filesec_t) __asm("_" "fstatx_np" "$INODE64");
int lchflags(const char *, __uint32_t) ;
int lchmod(const char *, mode_t) ;
int lstatx_np(const char *, struct stat *, filesec_t) __asm("_" "lstatx_np" "$INODE64");
int mkdirx_np(const char *, filesec_t);
int mkfifox_np(const char *, filesec_t);
int statx_np(const char *, struct stat *, filesec_t) __asm("_" "statx_np" "$INODE64");
int umaskx_np(filesec_t) __attribute__((deprecated));
int fstatx64_np(int, struct stat64 *, filesec_t) __attribute__((deprecated));
int lstatx64_np(const char *, struct stat64 *, filesec_t) __attribute__((deprecated));
int statx64_np(const char *, struct stat64 *, filesec_t) __attribute__((deprecated));
int fstat64(int, struct stat64 *) __attribute__((deprecated));
int lstat64(const char *, struct stat64 *) __attribute__((deprecated));
int stat64(const char *, struct stat64 *) __attribute__((deprecated));

struct pb_Parameters {
  char *outFile;
  char **inpFiles;
};
struct pb_Parameters *
pb_ReadParameters(int *_argc, char **argv);
void
pb_FreeParameters(struct pb_Parameters *p);
int
pb_Parameters_CountInputs(struct pb_Parameters *p);
typedef unsigned long long pb_Timestamp;
enum pb_TimerState {
  pb_Timer_STOPPED,
  pb_Timer_RUNNING
};
struct pb_Timer {
  enum pb_TimerState state;
  pb_Timestamp elapsed;
  pb_Timestamp init;
};
void
pb_ResetTimer(struct pb_Timer *timer);
void
pb_StartTimer(struct pb_Timer *timer);
void
pb_StopTimer(struct pb_Timer *timer);
double
pb_GetElapsedTime(struct pb_Timer *timer);
enum pb_TimerID {
  pb_TimerID_NONE = 0,
  pb_TimerID_IO,
  pb_TimerID_KERNEL,
  pb_TimerID_COPY,
  pb_TimerID_DRIVER,
  pb_TimerID_COPY_ASYNC,
  pb_TimerID_COMPUTE,
  pb_TimerID_OVERLAP,
  pb_TimerID_LAST
};
struct pb_async_time_marker_list {
  char *label;
  enum pb_TimerID timerID;
  void * marker;
  struct pb_async_time_marker_list *next;
};
struct pb_SubTimer {
  char *label;
  struct pb_Timer timer;
  struct pb_SubTimer *next;
};
struct pb_SubTimerList {
  struct pb_SubTimer *current;
  struct pb_SubTimer *subtimer_list;
};
struct pb_TimerSet {
  enum pb_TimerID current;
  struct pb_async_time_marker_list* async_markers;
  pb_Timestamp async_begin;
  pb_Timestamp wall_begin;
  struct pb_Timer timers[pb_TimerID_LAST];
  struct pb_SubTimerList *sub_timer_list[pb_TimerID_LAST];
};
void
pb_InitializeTimerSet(struct pb_TimerSet *timers);
void
pb_AddSubTimer(struct pb_TimerSet *timers, char *label, enum pb_TimerID pb_Category);
void
pb_SwitchToTimer(struct pb_TimerSet *timers, enum pb_TimerID timer);
void
pb_SwitchToSubTimer(struct pb_TimerSet *timers, char *label, enum pb_TimerID category);
void
pb_PrintTimerSet(struct pb_TimerSet *timers);
void
pb_DestroyTimerSet(struct pb_TimerSet * timers);
void
pb_SetOpenCL(void *clContextPtr, void *clCommandQueuePtr);
typedef struct {
 float timeScale;
 clock_t tickStart, tickStop;
 struct tms timeStart, timeStop;
} MAIN_Time;
typedef enum {NOTHING = 0, COMPARE, STORE} MAIN_Action;
typedef enum {LDC = 0, CHANNEL} MAIN_SimType;
typedef struct {
 int nTimeSteps;
 char* resultFilename;
 MAIN_Action action;
 MAIN_SimType simType;
 char* obstacleFilename;
} MAIN_Param;
void MAIN_parseCommandLine( int nArgs, char* arg[], MAIN_Param* param, struct pb_Parameters* );
void MAIN_printInfo( const MAIN_Param* param );
void MAIN_initialize( const MAIN_Param* param );
void MAIN_finalize( const MAIN_Param* param );
void MAIN_startClock( MAIN_Time* time );
void MAIN_stopClock( MAIN_Time* time, const MAIN_Param* param );
typedef enum {C = 0,
    N, S, E, W, T, B,
    NE, NW, SE, SW,
    NT, NB, ST, SB,
    ET, EB, WT, WB,
    FLAGS, N_CELL_ENTRIES} CELL_ENTRIES;
typedef float LBM_Grid[(150)*(1*(120))*(1*(120))*N_CELL_ENTRIES];
typedef LBM_Grid* LBM_GridPtr;
typedef enum {OBSTACLE = 1 << 0,
              ACCEL = 1 << 1,
              IN_OUT_FLOW = 1 << 2} CELL_FLAGS;
void LBM_allocateGrid( float** ptr );
void LBM_freeGrid( float** ptr );
void LBM_initializeGrid( LBM_Grid grid );
void LBM_initializeSpecialCellsForLDC( LBM_Grid grid );
void LBM_loadObstacleFile( LBM_Grid grid, const char* filename );
void LBM_initializeSpecialCellsForChannel( LBM_Grid grid );
void LBM_swapGrids( LBM_GridPtr* grid1, LBM_GridPtr* grid2 );
void LBM_performStreamCollide( LBM_Grid srcGrid, LBM_Grid dstGrid );
void LBM_handleInOutFlow( LBM_Grid srcGrid );
void LBM_showGridStatistics( LBM_Grid Grid );
void LBM_storeVelocityField( LBM_Grid grid, const char* filename,
                           const int binary );
void LBM_compareVelocityField( LBM_Grid grid, const char* filename,
                             const int binary );
void LBM_allocateGrid( float** ptr ) {
 const size_t margin = 2*(1*(120))*(1*(120))*N_CELL_ENTRIES,
              size = sizeof( LBM_Grid ) + 2*margin*sizeof( float );
 *ptr = malloc( size );
 if( ! *ptr ) {
  printf( "LBM_allocateGrid: could not allocate %.1f MByte\n",
          size / (1024.0*1024.0) );
  exit( 1 );
 }
 printf( "LBM_allocateGrid: allocated %.1f MByte\n",
         size / (1024.0*1024.0) );
 *ptr += margin;
}
void LBM_freeGrid( float** ptr ) {
 const size_t margin = 2*(1*(120))*(1*(120))*N_CELL_ENTRIES;
 free( *ptr-margin );
 *ptr = ((void *)0);
}
void LBM_initializeGrid( LBM_Grid grid ) {
 int i;
        
#pragma omp parallel for
 for( i = ((0)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120)))); i < ((0)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+((150)+2)*(1*(120))*(1*(120)))); i += N_CELL_ENTRIES ) {
  (((grid)[((C)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/ 3.0);
  (((grid)[((N)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/18.0);
  (((grid)[((S)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/18.0);
  (((grid)[((E)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/18.0);
  (((grid)[((W)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/18.0);
  (((grid)[((T)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/18.0);
  (((grid)[((B)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/18.0);
  (((grid)[((NE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0);
  (((grid)[((NW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0);
  (((grid)[((SE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0);
  (((grid)[((SW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0);
  (((grid)[((NT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0);
  (((grid)[((NB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0);
  (((grid)[((ST)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0);
  (((grid)[((SB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0);
  (((grid)[((ET)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0);
  (((grid)[((EB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0);
  (((grid)[((WT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0);
  (((grid)[((WB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0);
  {unsigned int* const _aux_ = ((unsigned int*) ((void*) (&((((grid)[((FLAGS)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))))); (*_aux_) = 0;};
 }
}
void LBM_swapGrids( LBM_GridPtr* grid1, LBM_GridPtr* grid2 ) {
 LBM_GridPtr aux = *grid1;
 *grid1 = *grid2;
 *grid2 = aux;
}
void LBM_loadObstacleFile( LBM_Grid grid, const char* filename ) {
 int x, y, z;
 FILE* file = fopen( filename, "rb" );
 for( z = 0; z < (150); z++ ) {
  for( y = 0; y < (1*(120)); y++ ) {
   for( x = 0; x < (1*(120)); x++ ) {
    if( fgetc( file ) != '.' ) {unsigned int* const _aux_ = ((unsigned int*) ((void*) (&(((grid)[((FLAGS)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]))))); (*_aux_) |= (OBSTACLE);};
   }
   fgetc( file );
  }
  fgetc( file );
 }
 fclose( file );
}
void LBM_initializeSpecialCellsForLDC( LBM_Grid grid ) {
 int x, y, z;
        
#pragma omp parallel for private( x, y )
 for( z = -2; z < (150)+2; z++ ) {
  for( y = 0; y < (1*(120)); y++ ) {
   for( x = 0; x < (1*(120)); x++ ) {
    if( x == 0 || x == (1*(120))-1 ||
        y == 0 || y == (1*(120))-1 ||
        z == 0 || z == (150)-1 ) {
     {unsigned int* const _aux_ = ((unsigned int*) ((void*) (&(((grid)[((FLAGS)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]))))); (*_aux_) |= (OBSTACLE);};
    }
    else {
     if( (z == 1 || z == (150)-2) &&
          x > 1 && x < (1*(120))-2 &&
          y > 1 && y < (1*(120))-2 ) {
      {unsigned int* const _aux_ = ((unsigned int*) ((void*) (&(((grid)[((FLAGS)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]))))); (*_aux_) |= (ACCEL);};
     }
    }
   }
  }
 }
}
void LBM_initializeSpecialCellsForChannel( LBM_Grid grid ) {
 int x, y, z;
        
#pragma omp parallel for private( x, y )
 for( z = -2; z < (150)+2; z++ ) {
  for( y = 0; y < (1*(120)); y++ ) {
   for( x = 0; x < (1*(120)); x++ ) {
    if( x == 0 || x == (1*(120))-1 ||
        y == 0 || y == (1*(120))-1 ) {
     {unsigned int* const _aux_ = ((unsigned int*) ((void*) (&(((grid)[((FLAGS)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]))))); (*_aux_) |= (OBSTACLE);};
     if( (z == 0 || z == (150)-1) &&
         ! ((*((unsigned int*) ((void*) (&(((grid)[((FLAGS)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])))))) & (OBSTACLE)))
      {unsigned int* const _aux_ = ((unsigned int*) ((void*) (&(((grid)[((FLAGS)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]))))); (*_aux_) |= (IN_OUT_FLOW);};
    }
   }
  }
 }
}
void LBM_performStreamCollide( LBM_Grid srcGrid, LBM_Grid dstGrid ) {
 int i;
 float ux, uy, uz, u2, rho;
        
#pragma omp parallel for private( ux, uy, uz, u2, rho )
 for( i = ((0)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120)))); i < ((0)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+((150))*(1*(120))*(1*(120)))); i += N_CELL_ENTRIES ) {
  if( ((*((unsigned int*) ((void*) (&((((srcGrid)[((FLAGS)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))))))) & (OBSTACLE))) {
   ((((dstGrid)[((C)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) = ((((srcGrid)[((C)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
   ((((dstGrid)[((S)+N_CELL_ENTRIES*((0)+ (-1)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) = ((((srcGrid)[((N)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
   ((((dstGrid)[((N)+N_CELL_ENTRIES*((0)+ (+1)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) = ((((srcGrid)[((S)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
   ((((dstGrid)[((W)+N_CELL_ENTRIES*((-1)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) = ((((srcGrid)[((E)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
   ((((dstGrid)[((E)+N_CELL_ENTRIES*((+1)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) = ((((srcGrid)[((W)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
   ((((dstGrid)[((B)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]))) = ((((srcGrid)[((T)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
   ((((dstGrid)[((T)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(+1)*(1*(120))*(1*(120))))+(i)]))) = ((((srcGrid)[((B)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
   ((((dstGrid)[((SW)+N_CELL_ENTRIES*((-1)+ (-1)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) = ((((srcGrid)[((NE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
   ((((dstGrid)[((SE)+N_CELL_ENTRIES*((+1)+ (-1)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) = ((((srcGrid)[((NW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
   ((((dstGrid)[((NW)+N_CELL_ENTRIES*((-1)+ (+1)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) = ((((srcGrid)[((SE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
   ((((dstGrid)[((NE)+N_CELL_ENTRIES*((+1)+ (+1)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) = ((((srcGrid)[((SW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
   ((((dstGrid)[((SB)+N_CELL_ENTRIES*((0)+ (-1)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]))) = ((((srcGrid)[((NT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
   ((((dstGrid)[((ST)+N_CELL_ENTRIES*((0)+ (-1)*(1*(120))+(+1)*(1*(120))*(1*(120))))+(i)]))) = ((((srcGrid)[((NB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
   ((((dstGrid)[((NB)+N_CELL_ENTRIES*((0)+ (+1)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]))) = ((((srcGrid)[((ST)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
   ((((dstGrid)[((NT)+N_CELL_ENTRIES*((0)+ (+1)*(1*(120))+(+1)*(1*(120))*(1*(120))))+(i)]))) = ((((srcGrid)[((SB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
   ((((dstGrid)[((WB)+N_CELL_ENTRIES*((-1)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]))) = ((((srcGrid)[((ET)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
   ((((dstGrid)[((WT)+N_CELL_ENTRIES*((-1)+ (0)*(1*(120))+(+1)*(1*(120))*(1*(120))))+(i)]))) = ((((srcGrid)[((EB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
   ((((dstGrid)[((EB)+N_CELL_ENTRIES*((+1)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]))) = ((((srcGrid)[((WT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
   ((((dstGrid)[((ET)+N_CELL_ENTRIES*((+1)+ (0)*(1*(120))+(+1)*(1*(120))*(1*(120))))+(i)]))) = ((((srcGrid)[((WB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
   continue;
  }
  rho = + ((((srcGrid)[((C)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + ((((srcGrid)[((N)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
        + ((((srcGrid)[((S)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + ((((srcGrid)[((E)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
        + ((((srcGrid)[((W)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + ((((srcGrid)[((T)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
        + ((((srcGrid)[((B)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + ((((srcGrid)[((NE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
        + ((((srcGrid)[((NW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + ((((srcGrid)[((SE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
        + ((((srcGrid)[((SW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + ((((srcGrid)[((NT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
        + ((((srcGrid)[((NB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + ((((srcGrid)[((ST)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
        + ((((srcGrid)[((SB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + ((((srcGrid)[((ET)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
        + ((((srcGrid)[((EB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + ((((srcGrid)[((WT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
        + ((((srcGrid)[((WB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
  ux = + ((((srcGrid)[((E)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) - ((((srcGrid)[((W)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
       + ((((srcGrid)[((NE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) - ((((srcGrid)[((NW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
       + ((((srcGrid)[((SE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) - ((((srcGrid)[((SW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
       + ((((srcGrid)[((ET)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + ((((srcGrid)[((EB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
       - ((((srcGrid)[((WT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) - ((((srcGrid)[((WB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
  uy = + ((((srcGrid)[((N)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) - ((((srcGrid)[((S)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
       + ((((srcGrid)[((NE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + ((((srcGrid)[((NW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
       - ((((srcGrid)[((SE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) - ((((srcGrid)[((SW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
       + ((((srcGrid)[((NT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + ((((srcGrid)[((NB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
       - ((((srcGrid)[((ST)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) - ((((srcGrid)[((SB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
  uz = + ((((srcGrid)[((T)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) - ((((srcGrid)[((B)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
       + ((((srcGrid)[((NT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) - ((((srcGrid)[((NB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
       + ((((srcGrid)[((ST)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) - ((((srcGrid)[((SB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
       + ((((srcGrid)[((ET)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) - ((((srcGrid)[((EB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])))
       + ((((srcGrid)[((WT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) - ((((srcGrid)[((WB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])));
  ux /= rho;
  uy /= rho;
  uz /= rho;
  if( ((*((unsigned int*) ((void*) (&((((srcGrid)[((FLAGS)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))))))) & (ACCEL))) {
   ux = 0.005f;
   uy = 0.002f;
   uz = 0.000f;
  }
  u2 = 1.5f * (ux*ux + uy*uy + uz*uz);
  ((((dstGrid)[((C)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) = (1.0f-(1.95f))*((((srcGrid)[((C)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + (1.0/ 3.0)*(1.95f)*rho*(1.0f - u2);
  ((((dstGrid)[((N)+N_CELL_ENTRIES*((0)+ (+1)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) = (1.0f-(1.95f))*((((srcGrid)[((N)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + (1.0/18.0)*(1.95f)*rho*(1.0f + uy*(4.5f*uy + 3.0f) - u2);
  ((((dstGrid)[((S)+N_CELL_ENTRIES*((0)+ (-1)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) = (1.0f-(1.95f))*((((srcGrid)[((S)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + (1.0/18.0)*(1.95f)*rho*(1.0f + uy*(4.5f*uy - 3.0f) - u2);
  ((((dstGrid)[((E)+N_CELL_ENTRIES*((+1)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) = (1.0f-(1.95f))*((((srcGrid)[((E)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + (1.0/18.0)*(1.95f)*rho*(1.0f + ux*(4.5f*ux + 3.0f) - u2);
  ((((dstGrid)[((W)+N_CELL_ENTRIES*((-1)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) = (1.0f-(1.95f))*((((srcGrid)[((W)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + (1.0/18.0)*(1.95f)*rho*(1.0f + ux*(4.5f*ux - 3.0f) - u2);
  ((((dstGrid)[((T)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(+1)*(1*(120))*(1*(120))))+(i)]))) = (1.0f-(1.95f))*((((srcGrid)[((T)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + (1.0/18.0)*(1.95f)*rho*(1.0f + uz*(4.5f*uz + 3.0f) - u2);
  ((((dstGrid)[((B)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]))) = (1.0f-(1.95f))*((((srcGrid)[((B)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + (1.0/18.0)*(1.95f)*rho*(1.0f + uz*(4.5f*uz - 3.0f) - u2);
  ((((dstGrid)[((NE)+N_CELL_ENTRIES*((+1)+ (+1)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) = (1.0f-(1.95f))*((((srcGrid)[((NE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + (1.0/36.0)*(1.95f)*rho*(1.0f + (+ux+uy)*(4.5f*(+ux+uy) + 3.0f) - u2);
  ((((dstGrid)[((NW)+N_CELL_ENTRIES*((-1)+ (+1)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) = (1.0f-(1.95f))*((((srcGrid)[((NW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + (1.0/36.0)*(1.95f)*rho*(1.0f + (-ux+uy)*(4.5f*(-ux+uy) + 3.0f) - u2);
  ((((dstGrid)[((SE)+N_CELL_ENTRIES*((+1)+ (-1)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) = (1.0f-(1.95f))*((((srcGrid)[((SE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + (1.0/36.0)*(1.95f)*rho*(1.0f + (+ux-uy)*(4.5f*(+ux-uy) + 3.0f) - u2);
  ((((dstGrid)[((SW)+N_CELL_ENTRIES*((-1)+ (-1)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) = (1.0f-(1.95f))*((((srcGrid)[((SW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + (1.0/36.0)*(1.95f)*rho*(1.0f + (-ux-uy)*(4.5f*(-ux-uy) + 3.0f) - u2);
  ((((dstGrid)[((NT)+N_CELL_ENTRIES*((0)+ (+1)*(1*(120))+(+1)*(1*(120))*(1*(120))))+(i)]))) = (1.0f-(1.95f))*((((srcGrid)[((NT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + (1.0/36.0)*(1.95f)*rho*(1.0f + (+uy+uz)*(4.5f*(+uy+uz) + 3.0f) - u2);
  ((((dstGrid)[((NB)+N_CELL_ENTRIES*((0)+ (+1)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]))) = (1.0f-(1.95f))*((((srcGrid)[((NB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + (1.0/36.0)*(1.95f)*rho*(1.0f + (+uy-uz)*(4.5f*(+uy-uz) + 3.0f) - u2);
  ((((dstGrid)[((ST)+N_CELL_ENTRIES*((0)+ (-1)*(1*(120))+(+1)*(1*(120))*(1*(120))))+(i)]))) = (1.0f-(1.95f))*((((srcGrid)[((ST)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + (1.0/36.0)*(1.95f)*rho*(1.0f + (-uy+uz)*(4.5f*(-uy+uz) + 3.0f) - u2);
  ((((dstGrid)[((SB)+N_CELL_ENTRIES*((0)+ (-1)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]))) = (1.0f-(1.95f))*((((srcGrid)[((SB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + (1.0/36.0)*(1.95f)*rho*(1.0f + (-uy-uz)*(4.5f*(-uy-uz) + 3.0f) - u2);
  ((((dstGrid)[((ET)+N_CELL_ENTRIES*((+1)+ (0)*(1*(120))+(+1)*(1*(120))*(1*(120))))+(i)]))) = (1.0f-(1.95f))*((((srcGrid)[((ET)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + (1.0/36.0)*(1.95f)*rho*(1.0f + (+ux+uz)*(4.5f*(+ux+uz) + 3.0f) - u2);
  ((((dstGrid)[((EB)+N_CELL_ENTRIES*((+1)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]))) = (1.0f-(1.95f))*((((srcGrid)[((EB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + (1.0/36.0)*(1.95f)*rho*(1.0f + (+ux-uz)*(4.5f*(+ux-uz) + 3.0f) - u2);
  ((((dstGrid)[((WT)+N_CELL_ENTRIES*((-1)+ (0)*(1*(120))+(+1)*(1*(120))*(1*(120))))+(i)]))) = (1.0f-(1.95f))*((((srcGrid)[((WT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + (1.0/36.0)*(1.95f)*rho*(1.0f + (-ux+uz)*(4.5f*(-ux+uz) + 3.0f) - u2);
  ((((dstGrid)[((WB)+N_CELL_ENTRIES*((-1)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]))) = (1.0f-(1.95f))*((((srcGrid)[((WB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))) + (1.0/36.0)*(1.95f)*rho*(1.0f + (-ux-uz)*(4.5f*(-ux-uz) + 3.0f) - u2);
 }
}
void LBM_handleInOutFlow( LBM_Grid srcGrid ) {
 float ux , uy , uz , rho ,
        ux1, uy1, uz1, rho1,
        ux2, uy2, uz2, rho2,
        u2, px, py;
 int i;
        
#pragma omp parallel for private( ux, uy, uz, rho, ux1, uy1, uz1, rho1, ux2, uy2, uz2, rho2, u2, px, py )
 for( i = ((0)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120)))); i < ((0)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(1)*(1*(120))*(1*(120)))); i += N_CELL_ENTRIES ) {
  rho1 = + ((srcGrid)[((C)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((N)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(1)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((S)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((E)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(1)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((W)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((T)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(1)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((B)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((NE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(1)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((NW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((SE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(1)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((SW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((NT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(1)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((NB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((ST)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(1)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((SB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((ET)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(1)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((EB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((WT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(1)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((WB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(1)*(1*(120))*(1*(120))))+(i)]);
  rho2 = + ((srcGrid)[((C)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((N)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(2)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((S)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((E)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(2)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((W)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((T)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(2)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((B)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((NE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(2)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((NW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((SE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(2)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((SW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((NT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(2)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((NB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((ST)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(2)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((SB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((ET)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(2)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((EB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((WT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(2)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((WB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(2)*(1*(120))*(1*(120))))+(i)]);
  rho = 2.0*rho1 - rho2;
  px = (((i / N_CELL_ENTRIES) % (1*(120))) / (0.5*((1*(120))-1))) - 1.0;
  py = ((((i / N_CELL_ENTRIES) / (1*(120))) % (1*(120))) / (0.5*((1*(120))-1))) - 1.0;
  ux = 0.00;
  uy = 0.00;
  uz = 0.01 * (1.0-px*px) * (1.0-py*py);
  u2 = 1.5 * (ux*ux + uy*uy + uz*uz);
  (((srcGrid)[((C)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/ 3.0)*rho*(1.0 - u2);
  (((srcGrid)[((N)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/18.0)*rho*(1.0 + uy*(4.5*uy + 3.0) - u2);
  (((srcGrid)[((S)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/18.0)*rho*(1.0 + uy*(4.5*uy - 3.0) - u2);
  (((srcGrid)[((E)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/18.0)*rho*(1.0 + ux*(4.5*ux + 3.0) - u2);
  (((srcGrid)[((W)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/18.0)*rho*(1.0 + ux*(4.5*ux - 3.0) - u2);
  (((srcGrid)[((T)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/18.0)*rho*(1.0 + uz*(4.5*uz + 3.0) - u2);
  (((srcGrid)[((B)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/18.0)*rho*(1.0 + uz*(4.5*uz - 3.0) - u2);
  (((srcGrid)[((NE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (+ux+uy)*(4.5*(+ux+uy) + 3.0) - u2);
  (((srcGrid)[((NW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (-ux+uy)*(4.5*(-ux+uy) + 3.0) - u2);
  (((srcGrid)[((SE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (+ux-uy)*(4.5*(+ux-uy) + 3.0) - u2);
  (((srcGrid)[((SW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (-ux-uy)*(4.5*(-ux-uy) + 3.0) - u2);
  (((srcGrid)[((NT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (+uy+uz)*(4.5*(+uy+uz) + 3.0) - u2);
  (((srcGrid)[((NB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (+uy-uz)*(4.5*(+uy-uz) + 3.0) - u2);
  (((srcGrid)[((ST)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (-uy+uz)*(4.5*(-uy+uz) + 3.0) - u2);
  (((srcGrid)[((SB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (-uy-uz)*(4.5*(-uy-uz) + 3.0) - u2);
  (((srcGrid)[((ET)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (+ux+uz)*(4.5*(+ux+uz) + 3.0) - u2);
  (((srcGrid)[((EB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (+ux-uz)*(4.5*(+ux-uz) + 3.0) - u2);
  (((srcGrid)[((WT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (-ux+uz)*(4.5*(-ux+uz) + 3.0) - u2);
  (((srcGrid)[((WB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (-ux-uz)*(4.5*(-ux-uz) + 3.0) - u2);
 }
        
#pragma omp parallel for private( ux, uy, uz, rho, ux1, uy1, uz1, rho1, ux2, uy2, uz2, rho2, u2, px, py )
 for( i = ((0)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+((150)-1)*(1*(120))*(1*(120)))); i < ((0)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+((150))*(1*(120))*(1*(120)))); i += N_CELL_ENTRIES ) {
  rho1 = + ((srcGrid)[((C)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((N)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((S)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((E)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((W)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((T)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((B)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((NE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((NW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((SE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((SW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((NT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((NB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((ST)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((SB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((ET)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((EB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((WT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((WB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]);
  ux1 = + ((srcGrid)[((E)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((W)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
        + ((srcGrid)[((NE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((NW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
        + ((srcGrid)[((SE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((SW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
        + ((srcGrid)[((ET)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((EB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
        - ((srcGrid)[((WT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((WB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]);
  uy1 = + ((srcGrid)[((N)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((S)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
        + ((srcGrid)[((NE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((NW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
        - ((srcGrid)[((SE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((SW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
        + ((srcGrid)[((NT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((NB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
        - ((srcGrid)[((ST)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((SB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]);
  uz1 = + ((srcGrid)[((T)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((B)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
        + ((srcGrid)[((NT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((NB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
        + ((srcGrid)[((ST)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((SB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
        + ((srcGrid)[((ET)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((EB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)])
        + ((srcGrid)[((WT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((WB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-1)*(1*(120))*(1*(120))))+(i)]);
  ux1 /= rho1;
  uy1 /= rho1;
  uz1 /= rho1;
  rho2 = + ((srcGrid)[((C)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((N)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((S)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((E)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((W)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((T)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((B)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((NE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((NW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((SE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((SW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((NT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((NB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((ST)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((SB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((ET)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((EB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((WT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
         + ((srcGrid)[((WB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]);
  ux2 = + ((srcGrid)[((E)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((W)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
        + ((srcGrid)[((NE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((NW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
        + ((srcGrid)[((SE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((SW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
        + ((srcGrid)[((ET)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((EB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
        - ((srcGrid)[((WT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((WB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]);
  uy2 = + ((srcGrid)[((N)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((S)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
        + ((srcGrid)[((NE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((NW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
        - ((srcGrid)[((SE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((SW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
        + ((srcGrid)[((NT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) + ((srcGrid)[((NB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
        - ((srcGrid)[((ST)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((SB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]);
  uz2 = + ((srcGrid)[((T)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((B)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
        + ((srcGrid)[((NT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((NB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
        + ((srcGrid)[((ST)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((SB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
        + ((srcGrid)[((ET)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((EB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)])
        + ((srcGrid)[((WT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]) - ((srcGrid)[((WB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(-2)*(1*(120))*(1*(120))))+(i)]);
  ux2 /= rho2;
  uy2 /= rho2;
  uz2 /= rho2;
  rho = 1.0;
  ux = 2*ux1 - ux2;
  uy = 2*uy1 - uy2;
  uz = 2*uz1 - uz2;
  u2 = 1.5 * (ux*ux + uy*uy + uz*uz);
  (((srcGrid)[((C)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/ 3.0)*rho*(1.0 - u2);
  (((srcGrid)[((N)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/18.0)*rho*(1.0 + uy*(4.5*uy + 3.0) - u2);
  (((srcGrid)[((S)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/18.0)*rho*(1.0 + uy*(4.5*uy - 3.0) - u2);
  (((srcGrid)[((E)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/18.0)*rho*(1.0 + ux*(4.5*ux + 3.0) - u2);
  (((srcGrid)[((W)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/18.0)*rho*(1.0 + ux*(4.5*ux - 3.0) - u2);
  (((srcGrid)[((T)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/18.0)*rho*(1.0 + uz*(4.5*uz + 3.0) - u2);
  (((srcGrid)[((B)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/18.0)*rho*(1.0 + uz*(4.5*uz - 3.0) - u2);
  (((srcGrid)[((NE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (+ux+uy)*(4.5*(+ux+uy) + 3.0) - u2);
  (((srcGrid)[((NW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (-ux+uy)*(4.5*(-ux+uy) + 3.0) - u2);
  (((srcGrid)[((SE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (+ux-uy)*(4.5*(+ux-uy) + 3.0) - u2);
  (((srcGrid)[((SW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (-ux-uy)*(4.5*(-ux-uy) + 3.0) - u2);
  (((srcGrid)[((NT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (+uy+uz)*(4.5*(+uy+uz) + 3.0) - u2);
  (((srcGrid)[((NB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (+uy-uz)*(4.5*(+uy-uz) + 3.0) - u2);
  (((srcGrid)[((ST)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (-uy+uz)*(4.5*(-uy+uz) + 3.0) - u2);
  (((srcGrid)[((SB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (-uy-uz)*(4.5*(-uy-uz) + 3.0) - u2);
  (((srcGrid)[((ET)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (+ux+uz)*(4.5*(+ux+uz) + 3.0) - u2);
  (((srcGrid)[((EB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (+ux-uz)*(4.5*(+ux-uz) + 3.0) - u2);
  (((srcGrid)[((WT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (-ux+uz)*(4.5*(-ux+uz) + 3.0) - u2);
  (((srcGrid)[((WB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) = (1.0/36.0)*rho*(1.0 + (-ux-uz)*(4.5*(-ux-uz) + 3.0) - u2);
 }
}
void LBM_showGridStatistics( LBM_Grid grid ) {
 int nObstacleCells = 0,
     nAccelCells = 0,
     nFluidCells = 0;
 float ux, uy, uz;
 float minU2 = 1e+30, maxU2 = -1e+30, u2;
 float minRho = 1e+30, maxRho = -1e+30, rho;
 float mass = 0;
 int i;
 for( i = ((0)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120)))); i < ((0)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+((150))*(1*(120))*(1*(120)))); i += N_CELL_ENTRIES ) {
  rho = + (((grid)[((C)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) + (((grid)[((N)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        + (((grid)[((S)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) + (((grid)[((E)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        + (((grid)[((W)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) + (((grid)[((T)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        + (((grid)[((B)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) + (((grid)[((NE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        + (((grid)[((NW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) + (((grid)[((SE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        + (((grid)[((SW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) + (((grid)[((NT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        + (((grid)[((NB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) + (((grid)[((ST)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        + (((grid)[((SB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) + (((grid)[((ET)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        + (((grid)[((EB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) + (((grid)[((WT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        + (((grid)[((WB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]));
  if( rho < minRho ) minRho = rho;
  if( rho > maxRho ) maxRho = rho;
  mass += rho;
  if( ((*((unsigned int*) ((void*) (&((((grid)[((FLAGS)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))))))) & (OBSTACLE))) {
   nObstacleCells++;
  }
  else {
   if( ((*((unsigned int*) ((void*) (&((((grid)[((FLAGS)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))))))) & (ACCEL)))
    nAccelCells++;
   else
    nFluidCells++;
   ux = + (((grid)[((E)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) - (((grid)[((W)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        + (((grid)[((NE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) - (((grid)[((NW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        + (((grid)[((SE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) - (((grid)[((SW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        + (((grid)[((ET)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) + (((grid)[((EB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        - (((grid)[((WT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) - (((grid)[((WB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]));
   uy = + (((grid)[((N)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) - (((grid)[((S)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        + (((grid)[((NE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) + (((grid)[((NW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        - (((grid)[((SE)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) - (((grid)[((SW)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        + (((grid)[((NT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) + (((grid)[((NB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        - (((grid)[((ST)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) - (((grid)[((SB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]));
   uz = + (((grid)[((T)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) - (((grid)[((B)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        + (((grid)[((NT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) - (((grid)[((NB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        + (((grid)[((ST)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) - (((grid)[((SB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        + (((grid)[((ET)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) - (((grid)[((EB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]))
        + (((grid)[((WT)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)])) - (((grid)[((WB)+N_CELL_ENTRIES*((0)+ (0)*(1*(120))+(0)*(1*(120))*(1*(120))))+(i)]));
   u2 = (ux*ux + uy*uy + uz*uz) / (rho*rho);
   if( u2 < minU2 ) minU2 = u2;
   if( u2 > maxU2 ) maxU2 = u2;
  }
 }
        printf( "LBM_showGridStatistics:\n"
        "\tnObstacleCells: %7i nAccelCells: %7i nFluidCells: %7i\n"
        "\tminRho: %8.4f maxRho: %8.4f mass: %e\n"
        "\tminU: %e maxU: %e\n\n",
        nObstacleCells, nAccelCells, nFluidCells,
        minRho, maxRho, mass,
        sqrt( minU2 ), sqrt( maxU2 ) );
}
static void storeValue( FILE* file, float* v ) {
 const int litteBigEndianTest = 1;
 if( (*((unsigned char*) &litteBigEndianTest)) == 0 ) {
  const char* vPtr = (char*) v;
  char buffer[sizeof( float )];
  int i;
  for (i = 0; i < sizeof( float ); i++)
   buffer[i] = vPtr[sizeof( float ) - i - 1];
  fwrite( buffer, sizeof( float ), 1, file );
 }
 else {
  fwrite( v, sizeof( float ), 1, file );
 }
}
static void loadValue( FILE* file, float* v ) {
 const int litteBigEndianTest = 1;
 if( (*((unsigned char*) &litteBigEndianTest)) == 0 ) {
  char* vPtr = (char*) v;
  char buffer[sizeof( float )];
  int i;
  fread( buffer, sizeof( float ), 1, file );
  for (i = 0; i < sizeof( float ); i++)
   vPtr[i] = buffer[sizeof( float ) - i - 1];
 }
 else {
  fread( v, sizeof( float ), 1, file );
 }
}
void LBM_storeVelocityField( LBM_Grid grid, const char* filename,
                             const int binary ) {
 int x, y, z;
 float rho, ux, uy, uz;
 FILE* file = fopen( filename, (binary ? "wb" : "w") );
 for( z = 0; z < (150); z++ ) {
  for( y = 0; y < (1*(120)); y++ ) {
   for( x = 0; x < (1*(120)); x++ ) {
    rho = + ((grid)[((C)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((N)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
          + ((grid)[((S)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((E)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
          + ((grid)[((W)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((T)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
          + ((grid)[((B)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((NE)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
          + ((grid)[((NW)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((SE)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
          + ((grid)[((SW)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((NT)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
          + ((grid)[((NB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((ST)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
          + ((grid)[((SB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((ET)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
          + ((grid)[((EB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((WT)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
          + ((grid)[((WB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]);
    ux = + ((grid)[((E)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((W)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         + ((grid)[((NE)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((NW)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         + ((grid)[((SE)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((SW)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         + ((grid)[((ET)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((EB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         - ((grid)[((WT)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((WB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]);
    uy = + ((grid)[((N)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((S)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         + ((grid)[((NE)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((NW)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         - ((grid)[((SE)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((SW)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         + ((grid)[((NT)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((NB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         - ((grid)[((ST)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((SB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]);
    uz = + ((grid)[((T)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((B)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         + ((grid)[((NT)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((NB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         + ((grid)[((ST)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((SB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         + ((grid)[((ET)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((EB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         + ((grid)[((WT)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((WB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]);
    ux /= rho;
    uy /= rho;
    uz /= rho;
    if( binary ) {
     storeValue( file, &ux );
     storeValue( file, &uy );
     storeValue( file, &uz );
    } else
     fprintf( file, "%e %e %e\n", ux, uy, uz );
   }
  }
 }
 fclose( file );
}
void LBM_compareVelocityField( LBM_Grid grid, const char* filename,
                             const int binary ) {
 int x, y, z;
 float rho, ux, uy, uz;
 float fileUx, fileUy, fileUz,
                  dUx, dUy, dUz,
                  diff2, maxDiff2 = -1e+30;
 FILE* file = fopen( filename, (binary ? "rb" : "r") );
 for( z = 0; z < (150); z++ ) {
  for( y = 0; y < (1*(120)); y++ ) {
   for( x = 0; x < (1*(120)); x++ ) {
    rho = + ((grid)[((C)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((N)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
          + ((grid)[((S)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((E)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
          + ((grid)[((W)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((T)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
          + ((grid)[((B)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((NE)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
          + ((grid)[((NW)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((SE)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
          + ((grid)[((SW)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((NT)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
          + ((grid)[((NB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((ST)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
          + ((grid)[((SB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((ET)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
          + ((grid)[((EB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((WT)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
          + ((grid)[((WB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]);
    ux = + ((grid)[((E)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((W)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         + ((grid)[((NE)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((NW)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         + ((grid)[((SE)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((SW)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         + ((grid)[((ET)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((EB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         - ((grid)[((WT)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((WB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]);
    uy = + ((grid)[((N)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((S)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         + ((grid)[((NE)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((NW)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         - ((grid)[((SE)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((SW)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         + ((grid)[((NT)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) + ((grid)[((NB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         - ((grid)[((ST)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((SB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]);
    uz = + ((grid)[((T)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((B)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         + ((grid)[((NT)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((NB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         + ((grid)[((ST)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((SB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         + ((grid)[((ET)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((EB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))])
         + ((grid)[((WT)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]) - ((grid)[((WB)+N_CELL_ENTRIES*((x)+ (y)*(1*(120))+(z)*(1*(120))*(1*(120))))]);
    ux /= rho;
    uy /= rho;
    uz /= rho;
    if( binary ) {
     loadValue( file, &fileUx );
     loadValue( file, &fileUy );
     loadValue( file, &fileUz );
    }
    else {
     if( sizeof( float ) == sizeof( double )) {
      fscanf( file, "%lf %lf %lf\n", &fileUx, &fileUy, &fileUz );
     }
     else {
      fscanf( file, "%f %f %f\n", &fileUx, &fileUy, &fileUz );
     }
    }
    dUx = ux - fileUx;
    dUy = uy - fileUy;
    dUz = uz - fileUz;
    diff2 = dUx*dUx + dUy*dUy + dUz*dUz;
    if( diff2 > maxDiff2 ) maxDiff2 = diff2;
   }
  }
 }
 printf( "LBM_compareVelocityField: maxDiff = %e  ==>  %s\n\n",
         sqrt( maxDiff2 ),
         sqrt( maxDiff2 ) > 1e-5 ? "##### ERROR #####" : "OK" );
 fclose( file );
}
static void
free_string_array(char **string_array)
{
  char **p;
  if (!string_array) return;
  for (p = string_array; *p; p++) free(*p);
  free(string_array);
}
static char **
read_string_array(char *in)
{
  char **ret;
  int i;
  int count;
  char *substring;
  count = 1;
  for (i = 0; in[i]; i++) if (in[i] == ',') count++;
  ret = (char **)malloc((count + 1) * sizeof(char *));
  substring = in;
  for (i = 0; i < count; i++) {
    char *substring_end;
    int substring_length;
    for (substring_end = substring;
  (*substring_end != ',') && (*substring_end != 0);
  substring_end++);
    substring_length = substring_end - substring;
    ret[i] = (char *)malloc(substring_length + 1);
    __builtin___memcpy_chk (ret[i], substring, substring_length, __builtin_object_size (ret[i], 0));
    ret[i][substring_length] = 0;
    substring = substring_end + 1;
  }
  ret[i] = ((void *)0);
  return ret;
}
struct argparse {
  int argc;
  char **argv;
  int argn;
  char **argv_get;
  char **argv_put;
};
static void
initialize_argparse(struct argparse *ap, int argc, char **argv)
{
  ap->argc = argc;
  ap->argn = 0;
  ap->argv_get = ap->argv_put = ap->argv = argv;
}
static void
finalize_argparse(struct argparse *ap)
{
  for(; ap->argn < ap->argc; ap->argn++)
    *ap->argv_put++ = *ap->argv_get++;
}
static void
delete_argument(struct argparse *ap)
{
  if (ap->argn >= ap->argc) {
    fprintf(__stderrp, "delete_argument\n");
  }
  ap->argc--;
  ap->argv_get++;
}
static void
next_argument(struct argparse *ap)
{
  if (ap->argn >= ap->argc) {
    fprintf(__stderrp, "next_argument\n");
  }
  *ap->argv_put++ = *ap->argv_get++;
  ap->argn++;
}
static int
is_end_of_arguments(struct argparse *ap)
{
  return ap->argn == ap->argc;
}
static char *
get_argument(struct argparse *ap)
{
  return *ap->argv_get;
}
static char *
consume_argument(struct argparse *ap)
{
  char *ret = get_argument(ap);
  delete_argument(ap);
  return ret;
}
struct pb_Parameters *
pb_ReadParameters(int *_argc, char **argv)
{
  char *err_message;
  struct argparse ap;
  struct pb_Parameters *ret =
    (struct pb_Parameters *)malloc(sizeof(struct pb_Parameters));
  ret->outFile = ((void *)0);
  ret->inpFiles = (char **)malloc(sizeof(char *));
  ret->inpFiles[0] = ((void *)0);
  initialize_argparse(&ap, *_argc, argv);
  while(!is_end_of_arguments(&ap)) {
    char *arg = get_argument(&ap);
    if ((arg[0] == '-') && (arg[1] != 0) && (arg[2] == 0)) {
      delete_argument(&ap);
      switch(arg[1]) {
      case 'o':
 if (is_end_of_arguments(&ap))
   {
     err_message = "Expecting file name after '-o'\n";
     goto error;
   }
 free(ret->outFile);
 ret->outFile = strdup(consume_argument(&ap));
 break;
      case 'i':
 if (is_end_of_arguments(&ap))
   {
     err_message = "Expecting file name after '-i'\n";
     goto error;
   }
 ret->inpFiles = read_string_array(consume_argument(&ap));
 break;
      case '-':
 goto end_of_options;
      default:
 err_message = "Unexpected command-line parameter\n";
 goto error;
      }
    }
    else {
      next_argument(&ap);
    }
  }
 end_of_options:
  *_argc = ap.argc;
  finalize_argparse(&ap);
  return ret;
 error:
  fputs(err_message, __stderrp);
  pb_FreeParameters(ret);
  return ((void *)0);
}
void
pb_FreeParameters(struct pb_Parameters *p)
{
  char **cpp;
  free(p->outFile);
  free_string_array(p->inpFiles);
  free(p);
}
int
pb_Parameters_CountInputs(struct pb_Parameters *p)
{
  int n;
  for (n = 0; p->inpFiles[n]; n++);
  return n;
}
static void
accumulate_time(pb_Timestamp *accum,
  pb_Timestamp start,
  pb_Timestamp end)
{
  *accum += end - start;
}
static pb_Timestamp get_time()
{
  struct timeval tv;
  gettimeofday(&tv, ((void *)0));
  return (pb_Timestamp) (tv.tv_sec * 1000000l + tv.tv_usec);
}
void
pb_ResetTimer(struct pb_Timer *timer)
{
  timer->state = pb_Timer_STOPPED;
  timer->elapsed = 0;
}
void
pb_StartTimer(struct pb_Timer *timer)
{
  if (timer->state != pb_Timer_STOPPED) {
    fputs("Ignoring attempt to start a running timer\n", __stderrp);
    return;
  }
  timer->state = pb_Timer_RUNNING;
  {
    struct timeval tv;
    gettimeofday(&tv, ((void *)0));
    timer->init = tv.tv_sec * 1000000l + tv.tv_usec;
  }
}
void
pb_StartTimerAndSubTimer(struct pb_Timer *timer, struct pb_Timer *subtimer)
{
  unsigned int numNotStopped = 0x3;
  if (timer->state != pb_Timer_STOPPED) {
    fputs("Warning: Timer was not stopped\n", __stderrp);
    numNotStopped &= 0x1;
  }
  if (subtimer->state != pb_Timer_STOPPED) {
    fputs("Warning: Subtimer was not stopped\n", __stderrp);
    numNotStopped &= 0x2;
  }
  if (numNotStopped == 0x0) {
    fputs("Ignoring attempt to start running timer and subtimer\n", __stderrp);
    return;
  }
  timer->state = pb_Timer_RUNNING;
  subtimer->state = pb_Timer_RUNNING;
  {
    struct timeval tv;
    gettimeofday(&tv, ((void *)0));
    if (numNotStopped & 0x2) {
      timer->init = tv.tv_sec * 1000000l + tv.tv_usec;
    }
    if (numNotStopped & 0x1) {
      subtimer->init = tv.tv_sec * 1000000l + tv.tv_usec;
    }
  }
}
void
pb_StopTimer(struct pb_Timer *timer)
{
  pb_Timestamp fini;
  if (timer->state != pb_Timer_RUNNING) {
    fputs("Ignoring attempt to stop a stopped timer\n", __stderrp);
    return;
  }
  timer->state = pb_Timer_STOPPED;
  {
    struct timeval tv;
    gettimeofday(&tv, ((void *)0));
    fini = tv.tv_sec * 1000000l + tv.tv_usec;
  }
  accumulate_time(&timer->elapsed, timer->init, fini);
  timer->init = fini;
}
void pb_StopTimerAndSubTimer(struct pb_Timer *timer, struct pb_Timer *subtimer) {
  pb_Timestamp fini;
  unsigned int numNotRunning = 0x3;
  if (timer->state != pb_Timer_RUNNING) {
    fputs("Warning: Timer was not running\n", __stderrp);
    numNotRunning &= 0x1;
  }
  if (subtimer->state != pb_Timer_RUNNING) {
    fputs("Warning: Subtimer was not running\n", __stderrp);
    numNotRunning &= 0x2;
  }
  if (numNotRunning == 0x0) {
    fputs("Ignoring attempt to stop stopped timer and subtimer\n", __stderrp);
    return;
  }
  timer->state = pb_Timer_STOPPED;
  subtimer->state = pb_Timer_STOPPED;
  {
    struct timeval tv;
    gettimeofday(&tv, ((void *)0));
    fini = tv.tv_sec * 1000000l + tv.tv_usec;
  }
  if (numNotRunning & 0x2) {
    accumulate_time(&timer->elapsed, timer->init, fini);
    timer->init = fini;
  }
  if (numNotRunning & 0x1) {
    accumulate_time(&subtimer->elapsed, subtimer->init, fini);
    subtimer->init = fini;
  }
}
double
pb_GetElapsedTime(struct pb_Timer *timer)
{
  double ret;
  if (timer->state != pb_Timer_STOPPED) {
    fputs("Elapsed time from a running timer is inaccurate\n", __stderrp);
  }
  ret = timer->elapsed / 1e6;
  return ret;
}
void
pb_InitializeTimerSet(struct pb_TimerSet *timers)
{
  int n;
  timers->wall_begin = get_time();
  timers->current = pb_TimerID_NONE;
  timers->async_markers = ((void *)0);
  for (n = 0; n < pb_TimerID_LAST; n++) {
    pb_ResetTimer(&timers->timers[n]);
    timers->sub_timer_list[n] = ((void *)0);
  }
}
void
pb_AddSubTimer(struct pb_TimerSet *timers, char *label, enum pb_TimerID pb_Category) {
  struct pb_SubTimer *subtimer = (struct pb_SubTimer *) malloc
    (sizeof(struct pb_SubTimer));
  int len = strlen(label);
  subtimer->label = (char *) malloc (sizeof(char)*(len+1));
  __builtin___sprintf_chk (subtimer->label, 0, __builtin_object_size (subtimer->label, 2 > 1 ? 1 : 0), "%s\0", label);
  pb_ResetTimer(&subtimer->timer);
  subtimer->next = ((void *)0);
  struct pb_SubTimerList *subtimerlist = timers->sub_timer_list[pb_Category];
  if (subtimerlist == ((void *)0)) {
    subtimerlist = (struct pb_SubTimerList *) malloc
      (sizeof(struct pb_SubTimerList));
    subtimerlist->subtimer_list = subtimer;
    timers->sub_timer_list[pb_Category] = subtimerlist;
  } else {
    struct pb_SubTimer *element = subtimerlist->subtimer_list;
    while (element->next != ((void *)0)) {
      element = element->next;
    }
    element->next = subtimer;
  }
}
void
pb_SwitchToSubTimer(struct pb_TimerSet *timers, char *label, enum pb_TimerID category)
{
  struct pb_Timer *topLevelToStop = ((void *)0);
  if (timers->current != category && timers->current != pb_TimerID_NONE) {
    topLevelToStop = &timers->timers[timers->current];
  }
  struct pb_SubTimerList *subtimerlist = timers->sub_timer_list[timers->current];
  struct pb_SubTimer *curr = (subtimerlist == ((void *)0)) ? ((void *)0) : subtimerlist->current;
  if (timers->current != pb_TimerID_NONE) {
    if (curr != ((void *)0) && topLevelToStop != ((void *)0)) {
      pb_StopTimerAndSubTimer(topLevelToStop, &curr->timer);
    } else if (curr != ((void *)0)) {
      pb_StopTimer(&curr->timer);
    } else {
      pb_StopTimer(topLevelToStop);
    }
  }
  subtimerlist = timers->sub_timer_list[category];
  struct pb_SubTimer *subtimer = ((void *)0);
  if (label != ((void *)0)) {
    subtimer = subtimerlist->subtimer_list;
    while (subtimer != ((void *)0)) {
      if (strcmp(subtimer->label, label) == 0) {
        break;
      } else {
        subtimer = subtimer->next;
      }
    }
  }
  if (category != pb_TimerID_NONE) {
    if (subtimerlist != ((void *)0)) {
      subtimerlist->current = subtimer;
    }
    if (category != timers->current && subtimer != ((void *)0)) {
      pb_StartTimerAndSubTimer(&timers->timers[category], &subtimer->timer);
    } else if (subtimer != ((void *)0)) {
      pb_StartTimer(&subtimer->timer);
    } else{
      pb_StartTimer(&timers->timers[category]);
    }
  }
  timers->current = category;
}
void
pb_SwitchToTimer(struct pb_TimerSet *timers, enum pb_TimerID timer)
{
  if (timers->current != pb_TimerID_NONE) {
    struct pb_SubTimer *currSubTimer = ((void *)0);
    struct pb_SubTimerList *subtimerlist = timers->sub_timer_list[timers->current];
    if ( subtimerlist != ((void *)0)) {
      currSubTimer = timers->sub_timer_list[timers->current]->current;
    }
    if ( currSubTimer!= ((void *)0)) {
      pb_StopTimerAndSubTimer(&timers->timers[timers->current], &currSubTimer->timer);
    } else {
      pb_StopTimer(&timers->timers[timers->current]);
    }
  }
  timers->current = timer;
  if (timer != pb_TimerID_NONE) {
    pb_StartTimer(&timers->timers[timer]);
  }
}
void
pb_PrintTimerSet(struct pb_TimerSet *timers)
{
  pb_Timestamp wall_end = get_time();
  struct pb_Timer *t = timers->timers;
  struct pb_SubTimer* sub = ((void *)0);
  int maxSubLength;
  const char *categories[] = {
    "IO", "Kernel", "Copy", "Driver", "Copy Async", "Compute"
  };
  const int maxCategoryLength = 10;
  int i;
  for(i = 1; i < pb_TimerID_LAST-1; ++i) {
    if(pb_GetElapsedTime(&t[i]) != 0) {
      printf("%-*s: %f\n", maxCategoryLength, categories[i-1], pb_GetElapsedTime(&t[i]));
      if (timers->sub_timer_list[i] != ((void *)0)) {
        sub = timers->sub_timer_list[i]->subtimer_list;
        maxSubLength = 0;
        while (sub != ((void *)0)) {
          if (strlen(sub->label) > maxSubLength) {
            maxSubLength = strlen(sub->label);
          }
          sub = sub->next;
        }
        if (maxSubLength <= maxCategoryLength) {
         maxSubLength = maxCategoryLength;
        }
        sub = timers->sub_timer_list[i]->subtimer_list;
        while (sub != ((void *)0)) {
          printf(" -%-*s: %f\n", maxSubLength, sub->label, pb_GetElapsedTime(&sub->timer));
          sub = sub->next;
        }
      }
    }
  }
  if(pb_GetElapsedTime(&t[pb_TimerID_OVERLAP]) != 0)
    printf("CPU/Kernel Overlap: %f\n", pb_GetElapsedTime(&t[pb_TimerID_OVERLAP]));
  float walltime = (wall_end - timers->wall_begin)/ 1e6;
  printf("Timer Wall Time: %f\n", walltime);
}
void pb_DestroyTimerSet(struct pb_TimerSet * timers)
{
  struct pb_async_time_marker_list ** event = &(timers->async_markers);
  while( *event != ((void *)0)) {
    struct pb_async_time_marker_list ** next = &((*event)->next);
    free(*event);
    (*event) = ((void *)0);
    event = next;
  }
  int i = 0;
  for(i = 0; i < pb_TimerID_LAST; ++i) {
    if (timers->sub_timer_list[i] != ((void *)0)) {
      struct pb_SubTimer *subtimer = timers->sub_timer_list[i]->subtimer_list;
      struct pb_SubTimer *prev = ((void *)0);
      while (subtimer != ((void *)0)) {
        free(subtimer->label);
        prev = subtimer;
        subtimer = subtimer->next;
        free(prev);
      }
      free(timers->sub_timer_list[i]);
    }
  }
}
static LBM_GridPtr srcGrid, dstGrid;
struct pb_TimerSet timers;
int main( int nArgs, char* arg[] ) {
 MAIN_Param param;
 MAIN_Time time;
 int t;
        pb_InitializeTimerSet(&timers);
        pb_SwitchToTimer(&timers, pb_TimerID_COMPUTE);
        struct pb_Parameters* params;
        params = pb_ReadParameters(&nArgs, arg);
 MAIN_parseCommandLine( nArgs, arg, &param, params );
 MAIN_printInfo( &param );
 MAIN_initialize( &param );
 MAIN_startClock( &time );
 for( t = 1; t <= param.nTimeSteps; t++ ) {
  if( param.simType == CHANNEL ) {
   LBM_handleInOutFlow( *srcGrid );
  }
  LBM_performStreamCollide( *srcGrid, *dstGrid );
  LBM_swapGrids( &srcGrid, &dstGrid );
  if( (t & 63) == 0 ) {
   printf( "timestep: %i\n", t );
  }
 }
 MAIN_stopClock( &time, &param );
 MAIN_finalize( &param );
        pb_SwitchToTimer(&timers, pb_TimerID_NONE);
        pb_PrintTimerSet(&timers);
        pb_FreeParameters(params);
 return 0;
}
void MAIN_parseCommandLine( int nArgs, char* arg[], MAIN_Param* param, struct pb_Parameters * params) {
 struct stat fileStat;
 if( nArgs < 2 ) {
  printf( "syntax: lbm <time steps>\n" );
  exit( 1 );
 }
 param->nTimeSteps = atoi( arg[1] );
 if( params->inpFiles[0] != ((void *)0) ) {
  param->obstacleFilename = params->inpFiles[0];
  if( stat( param->obstacleFilename, &fileStat ) != 0 ) {
   printf( "MAIN_parseCommandLine: cannot stat obstacle file '%s'\n",
            param->obstacleFilename );
   exit( 1 );
  }
  if( fileStat.st_size != (1*(120))*(1*(120))*(150)+((1*(120))+1)*(150) ) {
   printf( "MAIN_parseCommandLine:\n"
           "\tsize of file '%s' is %i bytes\n"
         "\texpected size is %i bytes\n",
           param->obstacleFilename, (int) fileStat.st_size,
           (1*(120))*(1*(120))*(150)+((1*(120))+1)*(150) );
   exit( 1 );
  }
 }
 else param->obstacleFilename = ((void *)0);
 param->resultFilename = params->outFile;
 param->action = STORE;
 param->simType = LDC;
}
void MAIN_printInfo( const MAIN_Param* param ) {
 const char actionString[3][32] = {"nothing", "compare", "store"};
 const char simTypeString[3][32] = {"lid-driven cavity", "channel flow"};
 printf( "MAIN_printInfo:\n"
         "\tgrid size      : %i x %i x %i = %.2f * 10^6 Cells\n"
         "\tnTimeSteps     : %i\n"
         "\tresult file    : %s\n"
         "\taction         : %s\n"
         "\tsimulation type: %s\n"
         "\tobstacle file  : %s\n\n",
         (1*(120)), (1*(120)), (150), 1e-6*(1*(120))*(1*(120))*(150),
         param->nTimeSteps, param->resultFilename,
         actionString[param->action], simTypeString[param->simType],
         (param->obstacleFilename == ((void *)0)) ? "<none>" :
                                             param->obstacleFilename );
}
void MAIN_initialize( const MAIN_Param* param ) {
 LBM_allocateGrid( (float**) &srcGrid );
 LBM_allocateGrid( (float**) &dstGrid );
 LBM_initializeGrid( *srcGrid );
 LBM_initializeGrid( *dstGrid );
 if( param->obstacleFilename != ((void *)0) ) {
  LBM_loadObstacleFile( *srcGrid, param->obstacleFilename );
  LBM_loadObstacleFile( *dstGrid, param->obstacleFilename );
 }
 if( param->simType == CHANNEL ) {
  LBM_initializeSpecialCellsForChannel( *srcGrid );
  LBM_initializeSpecialCellsForChannel( *dstGrid );
 }
 else {
  LBM_initializeSpecialCellsForLDC( *srcGrid );
  LBM_initializeSpecialCellsForLDC( *dstGrid );
 }
 LBM_showGridStatistics( *srcGrid );
}
void MAIN_finalize( const MAIN_Param* param ) {
 LBM_showGridStatistics( *srcGrid );
 if( param->action == COMPARE )
  LBM_compareVelocityField( *srcGrid, param->resultFilename, (-1) );
 if( param->action == STORE )
 LBM_storeVelocityField( *srcGrid, param->resultFilename, (-1) );
 LBM_freeGrid( (float**) &srcGrid );
 LBM_freeGrid( (float**) &dstGrid );
}
void MAIN_startClock( MAIN_Time* time ) {
 time->timeScale = 1.0 / sysconf( 3 );
 time->tickStart = times( &(time->timeStart) );
}
void MAIN_stopClock( MAIN_Time* time, const MAIN_Param* param ) {
 time->tickStop = times( &(time->timeStop) );
 printf( "MAIN_stopClock:\n"
         "\tusr: %7.2f sys: %7.2f tot: %7.2f wct: %7.2f MLUPS: %5.2f\n\n",
         (time->timeStop.tms_utime - time->timeStart.tms_utime) * time->timeScale,
         (time->timeStop.tms_stime - time->timeStart.tms_stime) * time->timeScale,
         (time->timeStop.tms_utime - time->timeStart.tms_utime +
          time->timeStop.tms_stime - time->timeStart.tms_stime) * time->timeScale,
         (time->tickStop - time->tickStart ) * time->timeScale,
         1.0e-6 * (1*(120)) * (1*(120)) * (150) * param->nTimeSteps /
         (time->tickStop - time->tickStart ) / time->timeScale );
}
