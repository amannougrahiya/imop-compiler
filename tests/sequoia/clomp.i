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
typedef __darwin_time_t time_t;
typedef __darwin_suseconds_t suseconds_t;

int pselect(int, fd_set * restrict, fd_set * restrict,
  fd_set * restrict, const struct timespec * restrict,
  const sigset_t * restrict)
  __asm("_" "pselect" "$1050")
  ;
int select(int, fd_set * restrict, fd_set * restrict,
  fd_set * restrict, struct timeval * restrict)
  __asm("_" "select" "$1050")
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

struct timeval64
{
 __int64_t tv_sec;
 __int64_t tv_usec;
};
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
int setitimer(int, const struct itimerval * restrict,
  struct itimerval * restrict);
int utimes(const char *, const struct timeval *);


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

long CLOMP_numThreads = -2;
long CLOMP_allocThreads = -2;
long CLOMP_numParts = -1;
long CLOMP_zonesPerPart = -1;
long CLOMP_flopScale = -1;
long CLOMP_timeScale = -1;
long CLOMP_zoneSize = -1;
char *CLOMP_exe_name = ((void *)0);
typedef struct _Zone
{
 long zoneId;
 long partId;
 double value;
 struct _Zone *nextZone;
} Zone;
typedef struct _Part
{
 long partId;
 long zoneCount;
 long update_count;
 Zone *firstZone;
 Zone *lastZone;
 double deposit_ratio;
 double residue;
 double expected_first_value;
 double expected_residue;
} Part;
Part **partArray = ((void *)0);
double CLOMP_partRatio =0.0;
long CLOMP_num_iterations = 0.0;
double CLOMP_max_residue = 0.0;
void print_usage()
{
 fprintf (__stderrp,
   "Usage: %s numThreads allocThreads numParts \\\n"
   "           zonesPerPart zoneSize flopScale timeScale\n",
   CLOMP_exe_name);
 fprintf (__stderrp, "\n");
 fprintf (__stderrp, "  numThreads: Number of OpenMP threads to use (-1 for system default)\n");
 fprintf (__stderrp, "  allocThreads: #threads when allocating data (-1 for numThreads)\n");
 fprintf (__stderrp, "  numParts: Number of independent pieces of work (loop iterations)\n");
 fprintf (__stderrp, "  zonesPerPart: Number of zones in the first part (3 flops/zone/part)\n");
 fprintf (__stderrp, "  zoneSize: Bytes in zone, only first ~32 used (512 nominal, >= 32 valid)\n");
 fprintf (__stderrp, "  flopScale: Scales flops/zone to increase memory reuse (1 nominal, >=1 Valid)\n");
 fprintf (__stderrp, "  timeScale: Scales target time per test (10-100 nominal, 1-10000 Valid)\n");
 fprintf (__stderrp, "\n");
 fprintf (__stderrp, "Some interesting testcases (last number controls run time):\n");
 fprintf (__stderrp, "           Target input:    %s -1 1 64 100 32 1 100\n", CLOMP_exe_name);
 fprintf (__stderrp, "   Target/NUMA friendly:    %s -1 -1 64 100 32 1 100\n", CLOMP_exe_name);
 fprintf (__stderrp, "         Cache friendly:    %s -1 1 64 1 32 100 100\n", CLOMP_exe_name);
 fprintf (__stderrp, "  Cache/OpenMP friendly:    %s -1 1 64 1 32 1000 100\n", CLOMP_exe_name);
 fprintf (__stderrp, "        Mem-bound input:    %s -1 1 64 10000 512 1 100\n", CLOMP_exe_name);
 fprintf (__stderrp, "Mem-bound/NUMA friendly:    %s -1 -1 64 10000 512 1 100\n", CLOMP_exe_name);
}
long convert_to_positive_long (const char *parm_name, const char *parm_val)
{
 long val;
 char *endPtr;
 if ((parm_name == ((void *)0)) || (parm_val == ((void *)0)))
 {
  fprintf (__stderrp,
    "Error in convert_to_positive_long: Passed NULL pointers!\n");
  exit (1);
 }
 val = strtol (parm_val, &endPtr, 0);
 if (endPtr[0] != 0)
 {
  fprintf (__stderrp, "Error converting '%s' parameter value '%s' to long at '%s'!\n",
    parm_name, parm_val, endPtr);
  exit (1);
 }
 if ((strcmp (parm_name, "numThreads") == 0) ||
   (strcmp (parm_name, "allocThreads") == 0))
 {
  if ((val < 1) && (val != -1))
  {
   fprintf (__stderrp, "Invalid value %ld for parameter %s, must be > 0 or -1!\n",
     val, parm_name);
   print_usage();
   exit (1);
  }
 }
 else if (val < 1)
 {
  fprintf (__stderrp, "Invalid value %ld for parameter %s, must be > 0\n",
    val, parm_name);
  print_usage();
  exit (1);
 }
 return (val);
}
void update_part (Part *part, double incoming_deposit)
{
 Zone *zone;
 double deposit_ratio, remaining_deposit, deposit;
 long scale_count;
 part->update_count++;
 deposit_ratio = part->deposit_ratio;
 remaining_deposit = incoming_deposit;
 if (CLOMP_flopScale == 1)
 {
  for (zone = part->firstZone; zone != ((void *)0); zone = zone->nextZone)
  {
   deposit = remaining_deposit * deposit_ratio;
   zone->value += deposit;
   remaining_deposit -= deposit;
  }
 }
 else
 {
  for (zone = part->firstZone; zone != ((void *)0); zone = zone->nextZone)
  {
   for (scale_count = 0; scale_count < CLOMP_flopScale; scale_count++)
   {
    deposit = remaining_deposit * deposit_ratio;
    zone->value += deposit;
    remaining_deposit -= deposit;
   }
  }
 }
 part->residue = remaining_deposit;
}
void reinitialize_parts()
{
 long pidx;
 Zone *zone;
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
 {
  for (zone = partArray[pidx]->firstZone;
    zone != ((void *)0);
    zone = zone->nextZone)
  {
   zone->value = 0.0;
  }
  partArray[pidx]->residue = 0.0;
  partArray[pidx]->update_count = 0;
 }
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
 {
  update_part (partArray[pidx], 0.0);
 }
}
void print_start_message (const char *desc)
{
 time_t starttime;
 char startdate[50];
 time(&starttime);
 ctime_r(&starttime, startdate);
 printf ("%13s  Started: %s", desc, startdate);
 if (strcmp (desc, "calc_deposit") != 0)
 {
  if (strcmp (desc, "Serial Ref") != 0)
  {
   printf ("%13s #Threads: %d\n", desc, omp_get_max_threads());
  }
  else
  {
   printf ("%13s #Threads: N/A\n", desc);
  }
 }
}
void get_timestamp (struct timeval *ts)
{
 if (gettimeofday (ts, ((void *)0)) != 0)
 {
  fprintf (__stderrp, "Unable to get time of day, exiting\n");
  exit (1);
 }
}
void print_pseudocode (const char *desc, const char *pseudocode)
{
 printf ("%13s:| %s\n", desc, pseudocode);
}
double print_timestats (const char *desc, struct timeval *start_ts,
  struct timeval *end_ts, double base_seconds,
  double bestcase_seconds)
{
 double seconds;
 seconds = ((double)end_ts->tv_sec + ((double)end_ts->tv_usec * 1e-6)) -
  ((double)start_ts->tv_sec + ((double)start_ts->tv_usec * 1e-6));
 printf ("%13s  Runtime: %g (wallclock, in seconds)\n", desc, seconds);
 printf ("%13s  us/Loop: %g (wallclock, in microseconds)\n", desc,
   (seconds * 1000000.0)/((double)CLOMP_num_iterations * 10.0));
 if ((base_seconds > 0.0) && (seconds > 0.0))
 {
  if (base_seconds > seconds)
  {
   printf ("%13s  Speedup: %.2f\n", desc, base_seconds/seconds);
  }
  else
  {
   printf ("%13s  Speedup: %.2f (%.2fX slowdown)\n", desc,
     base_seconds/seconds, seconds/base_seconds);
  }
 }
 if ((bestcase_seconds > 0.0) && (seconds > 0.0))
 {
  printf ("%13s Efficacy: %.2f%% (of bestcase %g us/Loop)\n", desc,
    (bestcase_seconds/seconds) * 100.0,
    (bestcase_seconds * 1000000.0)/((double)CLOMP_num_iterations * 10.0));
  printf ("%13s Overhead: %g (versus bestcase, in us/Loop)\n",
    desc, ((seconds - bestcase_seconds) * 1000000.0)/((double)CLOMP_num_iterations * 10.0));
 }
 printf ("---------------------\n");
 return (seconds);
}
void print_data_stats (const char *desc)
{
 double value_sum, residue_sum, last_value, dtotal;
 long pidx;
 Zone *zone;
 int is_reference, error_count;
 value_sum = 0.0;
 residue_sum = 0.0;
 if (strcmp (desc, "Serial Ref") == 0)
  is_reference = 1;
 else
  is_reference = 0;
 error_count = 0;
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
 {
  if (is_reference)
  {
   partArray[pidx]->expected_first_value =
    partArray[pidx]->firstZone->value;
   partArray[pidx]->expected_residue = partArray[pidx]->residue;
  }
  else
  {
   if (partArray[pidx]->expected_first_value !=
     partArray[pidx]->firstZone->value)
   {
    error_count++;
    fprintf (__stderrp,
      "%s check failure: part %i first zone value (%g) != reference value (%g)!\n",
      desc, (int) pidx, partArray[pidx]->firstZone->value,
      partArray[pidx]->expected_first_value);
   }
   if (partArray[pidx]->expected_residue != partArray[pidx]->residue)
   {
    error_count++;
    fprintf (__stderrp,
      "%s check failure: part %i residue (%g) != reference residue (%g)!\n",
      desc, (int) pidx, partArray[pidx]->residue,
      partArray[pidx]->expected_residue);
   }
  }
  last_value = partArray[pidx]->firstZone->value;
  for (zone = partArray[pidx]->firstZone;
    zone != ((void *)0);
    zone = zone->nextZone)
  {
   if (zone->value > last_value)
   {
    fprintf (__stderrp,
      "*** %s check failure (part %i zone %i): "
      "previous (%g) < current (%g)!\n",
      desc, (int)zone->partId,
      (int)zone->zoneId, last_value, zone->value);
    error_count++;
   }
   value_sum += zone->value;
   last_value = zone->value;
  }
  residue_sum += partArray[pidx]->residue;
 }
 dtotal = value_sum + residue_sum;
 if (((dtotal + 0.00001) < ((double)CLOMP_num_iterations * 10.0)) ||
   ((dtotal - 0.00001) > ((double)CLOMP_num_iterations * 10.0)))
 {
  fprintf (__stderrp,
    "*** %s check failure:  Total (%-.15g) != Expected (%.15g)\n",
    desc, dtotal, ((double)CLOMP_num_iterations * 10.0));
  error_count++;
 }
 if ((residue_sum < 0.0) || (residue_sum > (CLOMP_max_residue + 0.000001)))
 {
  fprintf (__stderrp,
    "*** %s check failure: Residue (%-.15g) outside bounds 0 - %.15g\n",
    desc, residue_sum, CLOMP_max_residue);
  error_count++;
 }
 if (partArray[0]->update_count != 1)
 {
  fprintf (__stderrp, "Error in calc_deposit: Part updated %i times since last calc_deposit!\n",
    (int) partArray[0]->update_count);
  fprintf (__stderrp, "Benchmark designed to have calc_deposit called exactly once per update!\n");
  fprintf (__stderrp, "Critical error: Exiting...\n");
  exit (1);
 }
 if (error_count > 0)
 {
  fprintf (__stderrp,
    "ERROR: %i check failures detected in '%s' data. Exiting...\n",
    error_count, desc);
  exit (1);
 }
 printf ("%13s Checksum: Sum=%-8.8g Residue=%-8.8g Total=%-.9g\n",
   desc, value_sum, residue_sum, dtotal);
}
double calc_deposit ()
{
 double residue, deposit;
 long pidx;
 if (partArray[0]->update_count != 1)
 {
  fprintf (__stderrp, "Error in calc_deposit: Part updated %i times since last call!\n",
    (int) partArray[0]->update_count);
  fprintf (__stderrp, "Benchmark designed to have calc_deposit called exactly once per update!\n");
  fprintf (__stderrp, "Critical error: Exiting...\n");
  exit (1);
 }
 partArray[0]->update_count = 0;
 residue = 0.0;
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
 {
  residue += partArray[pidx]->residue;
 }
 deposit = (1.0 + residue) * CLOMP_partRatio;
 return (deposit);
}
void do_calc_deposit_only()
{
 long iteration, subcycle;
 for (iteration = 0; iteration < CLOMP_num_iterations; iteration ++)
 {
  for (subcycle = 0; subcycle < 10; subcycle++)
  {
   partArray[0]->update_count = 1;
   partArray[0]->firstZone->value = calc_deposit();
  }
 }
}
void do_omp_barrier_only(long num_iterations)
{
#pragma omp parallel
 {
  long iteration, subcycle;
  for (iteration = 0; iteration < num_iterations; iteration ++)
  {
   for (subcycle = 0; subcycle < 10; subcycle++)
   {
#pragma omp barrier
   }
  }
 }
}
void serial_ref_module1()
{
 double deposit;
 long pidx;
 deposit = calc_deposit ();
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
}
void serial_ref_module2()
{
 double deposit;
 long pidx;
 deposit = calc_deposit ();
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
 deposit = calc_deposit ();
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
}
void serial_ref_module3()
{
 double deposit;
 long pidx;
 deposit = calc_deposit ();
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
 deposit = calc_deposit ();
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
 deposit = calc_deposit ();
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
}
void serial_ref_module4()
{
 double deposit;
 long pidx;
 deposit = calc_deposit ();
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
 deposit = calc_deposit ();
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
 deposit = calc_deposit ();
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
 deposit = calc_deposit ();
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
}
void serial_ref_cycle()
{
 serial_ref_module1();
 serial_ref_module2();
 serial_ref_module3();
 serial_ref_module4();
}
void do_serial_ref_version()
{
 long iteration;
 for (iteration = 0; iteration < CLOMP_num_iterations; iteration ++)
  serial_ref_cycle();
}
void static_omp_module1()
{
 double deposit;
 long pidx;
 deposit = calc_deposit ();
#pragma omp parallel for private (pidx) schedule(static)
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
}
void static_omp_module2()
{
 double deposit;
 long pidx;
 deposit = calc_deposit ();
#pragma omp parallel for private (pidx) schedule(static)
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
 deposit = calc_deposit ();
#pragma omp parallel for private (pidx) schedule(static)
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
}
void static_omp_module3()
{
 double deposit;
 long pidx;
 deposit = calc_deposit ();
#pragma omp parallel for private (pidx) schedule(static)
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
 deposit = calc_deposit ();
#pragma omp parallel for private (pidx) schedule(static)
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
 deposit = calc_deposit ();
#pragma omp parallel for private (pidx) schedule(static)
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
}
void static_omp_module4()
{
 double deposit;
 long pidx;
 deposit = calc_deposit ();
#pragma omp parallel for private (pidx) schedule(static)
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
 deposit = calc_deposit ();
#pragma omp parallel for private (pidx) schedule(static)
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
 deposit = calc_deposit ();
#pragma omp parallel for private (pidx) schedule(static)
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
 deposit = calc_deposit ();
#pragma omp parallel for private (pidx) schedule(static)
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
}
void static_omp_cycle()
{
 static_omp_module1();
 static_omp_module2();
 static_omp_module3();
 static_omp_module4();
}
void do_static_omp_version()
{
 long iteration;
 for (iteration = 0; iteration < CLOMP_num_iterations; iteration ++)
 {
  static_omp_cycle();
 }
}
void dynamic_omp_module1()
{
 double deposit;
 long pidx;
 deposit = calc_deposit ();
#pragma omp parallel for private (pidx) schedule(dynamic)
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
}
void dynamic_omp_module2()
{
 double deposit;
 long pidx;
 deposit = calc_deposit ();
#pragma omp parallel for private (pidx) schedule(dynamic)
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
 deposit = calc_deposit ();
#pragma omp parallel for private (pidx) schedule(dynamic)
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
}
void dynamic_omp_module3()
{
 double deposit;
 long pidx;
 deposit = calc_deposit ();
#pragma omp parallel for private (pidx) schedule(dynamic)
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
 deposit = calc_deposit ();
#pragma omp parallel for private (pidx) schedule(dynamic)
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
 deposit = calc_deposit ();
#pragma omp parallel for private (pidx) schedule(dynamic)
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
}
void dynamic_omp_module4()
{
 double deposit;
 long pidx;
 deposit = calc_deposit ();
#pragma omp parallel for private (pidx) schedule(dynamic)
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
 deposit = calc_deposit ();
#pragma omp parallel for private (pidx) schedule(dynamic)
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
 deposit = calc_deposit ();
#pragma omp parallel for private (pidx) schedule(dynamic)
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
 deposit = calc_deposit ();
#pragma omp parallel for private (pidx) schedule(dynamic)
 for (pidx = 0; pidx < CLOMP_numParts; pidx++)
  update_part (partArray[pidx], deposit);
}
void dynamic_omp_cycle()
{
 dynamic_omp_module1();
 dynamic_omp_module2();
 dynamic_omp_module3();
 dynamic_omp_module4();
}
void do_dynamic_omp_version()
{
 long iteration;
 for (iteration = 0; iteration < CLOMP_num_iterations; iteration ++)
 {
  dynamic_omp_cycle();
 }
}
void manual_omp_module1(int startPidx, int endPidx)
{
 static double deposit;
 long pidx;
 Part *part;
#pragma omp barrier
#pragma omp single
 {
  deposit = calc_deposit ();
 }
 for (pidx = startPidx; pidx <= endPidx; pidx++)
  update_part (partArray[pidx], deposit);
}
void manual_omp_module2(int startPidx, int endPidx)
{
 static double deposit;
 long pidx;
 Part *part;
#pragma omp barrier
#pragma omp single
 {
  deposit = calc_deposit ();
 }
 for (pidx = startPidx; pidx <= endPidx; pidx++)
  update_part (partArray[pidx], deposit);
#pragma omp barrier
#pragma omp single
 {
  deposit = calc_deposit ();
 }
 for (pidx = startPidx; pidx <= endPidx; pidx++)
  update_part (partArray[pidx], deposit);
}
void manual_omp_module3(int startPidx, int endPidx)
{
 static double deposit;
 long pidx;
 Part *part;
#pragma omp barrier
#pragma omp single
 {
  deposit = calc_deposit ();
 }
 for (pidx = startPidx; pidx <= endPidx; pidx++)
  update_part (partArray[pidx], deposit);
#pragma omp barrier
#pragma omp single
 {
  deposit = calc_deposit ();
 }
 for (pidx = startPidx; pidx <= endPidx; pidx++)
  update_part (partArray[pidx], deposit);
#pragma omp barrier
#pragma omp single
 {
  deposit = calc_deposit ();
 }
 for (pidx = startPidx; pidx <= endPidx; pidx++)
  update_part (partArray[pidx], deposit);
}
void manual_omp_module4(int startPidx, int endPidx)
{
 static double deposit;
 long pidx;
 Part *part;
#pragma omp barrier
#pragma omp single
 {
  deposit = calc_deposit ();
 }
 for (pidx = startPidx; pidx <= endPidx; pidx++)
  update_part (partArray[pidx], deposit);
#pragma omp barrier
#pragma omp single
 {
  deposit = calc_deposit ();
 }
 for (pidx = startPidx; pidx <= endPidx; pidx++)
  update_part (partArray[pidx], deposit);
#pragma omp barrier
#pragma omp single
 {
  deposit = calc_deposit ();
 }
 for (pidx = startPidx; pidx <= endPidx; pidx++)
  update_part (partArray[pidx], deposit);
#pragma omp barrier
#pragma omp single
 {
  deposit = calc_deposit ();
 }
 for (pidx = startPidx; pidx <= endPidx; pidx++)
  update_part (partArray[pidx], deposit);
}
void manual_omp_cycle(int startPidx, int endPidx)
{
 manual_omp_module1(startPidx, endPidx);
 manual_omp_module2(startPidx, endPidx);
 manual_omp_module3(startPidx, endPidx);
 manual_omp_module4(startPidx, endPidx);
}
void do_manual_omp_version(long num_iterations)
{
#pragma omp parallel
 {
  long iteration;
  int startPidx, endPidx;
  double dparts_per_thread;
  int thread_id = omp_get_thread_num();
  int numThreads = omp_get_num_threads();
  dparts_per_thread = ((double)(CLOMP_numParts))/((double)(numThreads));
  if (dparts_per_thread < 1.0)
   dparts_per_thread = 1.0;
  startPidx = (int) nearbyint(((double)thread_id) * dparts_per_thread);
  endPidx = (int) nearbyint(((double)thread_id+1)*dparts_per_thread)-1;
  if (endPidx >= CLOMP_numParts)
   endPidx = CLOMP_numParts-1;
  if (startPidx >= CLOMP_numParts)
  {
   printf ("*** No parts available for thread %i\n", thread_id);
  }
  for (iteration = 0; iteration < num_iterations; iteration ++)
  {
   manual_omp_cycle(startPidx, endPidx);
  }
 }
}
void bestcase_omp_module1(int startPidx, int endPidx, double deposit)
{
 long pidx;
 for (pidx = startPidx; pidx <= endPidx; pidx++)
  update_part (partArray[pidx], deposit);
}
void bestcase_omp_module2(int startPidx, int endPidx, double deposit)
{
 long pidx;
 for (pidx = startPidx; pidx <= endPidx; pidx++)
  update_part (partArray[pidx], deposit);
 for (pidx = startPidx; pidx <= endPidx; pidx++)
  update_part (partArray[pidx], deposit);
}
void bestcase_omp_module3(int startPidx, int endPidx, double deposit)
{
 long pidx;
 for (pidx = startPidx; pidx <= endPidx; pidx++)
  update_part (partArray[pidx], deposit);
 for (pidx = startPidx; pidx <= endPidx; pidx++)
  update_part (partArray[pidx], deposit);
 for (pidx = startPidx; pidx <= endPidx; pidx++)
  update_part (partArray[pidx], deposit);
}
void bestcase_omp_module4(int startPidx, int endPidx, double deposit)
{
 long pidx;
 for (pidx = startPidx; pidx <= endPidx; pidx++)
  update_part (partArray[pidx], deposit);
 for (pidx = startPidx; pidx <= endPidx; pidx++)
  update_part (partArray[pidx], deposit);
 for (pidx = startPidx; pidx <= endPidx; pidx++)
  update_part (partArray[pidx], deposit);
 for (pidx = startPidx; pidx <= endPidx; pidx++)
  update_part (partArray[pidx], deposit);
}
void bestcase_omp_cycle(int startPidx, int endPidx, double deposit)
{
 bestcase_omp_module1(startPidx, endPidx, deposit);
 bestcase_omp_module2(startPidx, endPidx, deposit);
 bestcase_omp_module3(startPidx, endPidx, deposit);
 bestcase_omp_module4(startPidx, endPidx, deposit);
}
void do_bestcase_omp_version(long num_iterations)
{
 long iter, subcycle;
 double deposit;
 deposit = (1.0 + CLOMP_max_residue) / CLOMP_numParts;
#pragma omp parallel
 {
  long iteration;
  int startPidx, endPidx;
  double dparts_per_thread;
  int thread_id = omp_get_thread_num();
  int numThreads = omp_get_num_threads();
  dparts_per_thread = ((double)(CLOMP_numParts))/((double)(numThreads));
  if (dparts_per_thread < 1.0)
   dparts_per_thread = 1.0;
  startPidx = (int) nearbyint(((double)thread_id) * dparts_per_thread);
  endPidx = (int) nearbyint(((double)thread_id+1)*dparts_per_thread)-1;
  if (endPidx >= CLOMP_numParts)
   endPidx = CLOMP_numParts-1;
  if (startPidx >= CLOMP_numParts)
  {
   printf ("*** No parts available for thread %i\n", thread_id);
  }
  for (iteration = 0; iteration < num_iterations; iteration ++)
  {
   bestcase_omp_cycle(startPidx, endPidx, deposit);
  }
 }
 for (iter = 0; iter < num_iterations; iter ++)
 {
  for (subcycle = 0; subcycle < 10; subcycle++)
  {
   partArray[0]->update_count = 1;
   partArray[0]->firstZone->value = calc_deposit();
  }
 }
}
void addPart (Part *part, long partId)
{
 if ((partId < 0) || (partId >= CLOMP_numParts))
 {
  fprintf (__stderrp, "addPart error: partId (%i) out of bounds!\n", (int)partId);
  exit (1);
 }
 if (partArray[partId] != ((void *)0))
 {
  fprintf (__stderrp, "addPart error: partId (%i) already initialized!\n",
    (int) partId);
  exit (1);
 }
 partArray[partId] = part;
 part->partId = partId;
 part->zoneCount = CLOMP_zonesPerPart;
 part->deposit_ratio=((double)(partId + 1))/((double)(20.0*CLOMP_numParts));
 part->residue = 0.0;
 part->firstZone = ((void *)0);
 part->lastZone = ((void *)0);
 part->expected_first_value = -1.0;
 part->expected_residue = -1.0;
}
void addZone (Part *part, Zone *zone)
{
 if (part == ((void *)0))
 {
  fprintf (__stderrp, "addZone error: part NULL!\n");
  exit (1);
 }
 if (zone == ((void *)0))
 {
  fprintf (__stderrp, "addZone error: zone NULL!\n");
  exit (1);
 }
 __builtin___memset_chk (zone, 0xFF, CLOMP_zoneSize, __builtin_object_size (zone, 0));
 if (part->lastZone == ((void *)0))
 {
  zone->zoneId = 1;
  part->firstZone = zone;
  part->lastZone = zone;
 }
 else
 {
  zone->zoneId = part->lastZone->zoneId + 1;
  part->lastZone->nextZone = zone;
  part->lastZone = zone;
 }
 zone->nextZone = ((void *)0);
 zone->partId = part->partId;
 zone->value = 0.0;
}
int main (int argc, char *argv[])
{
 char hostname[200];
 time_t starttime;
 char startdate[50];
 long partId, zoneId;
 double totalZoneCount;
 Zone *zone, *prev_zone;
 double deposit, residue, percent_residue;
 double diterations;
 struct timeval calc_deposit_start_ts, calc_deposit_end_ts;
 double calc_deposit_seconds;
 struct timeval omp_barrier_start_ts, omp_barrier_end_ts;
 double omp_barrier_seconds;
 struct timeval serial_ref_start_ts, serial_ref_end_ts;
 double serial_ref_seconds;
 struct timeval bestcase_omp_start_ts, bestcase_omp_end_ts;
 double bestcase_omp_seconds;
 struct timeval static_omp_start_ts, static_omp_end_ts;
 double static_omp_seconds;
 struct timeval manual_omp_start_ts, manual_omp_end_ts;
 double manual_omp_seconds;
 struct timeval dynamic_omp_start_ts, dynamic_omp_end_ts;
 double dynamic_omp_seconds;
 int bidx, aidx;
 Part *sorted_part_list;
 Part *part;
 CLOMP_exe_name = argv[0];
 printf ("Sequoia Benchmark Version 1.0\n");
 if (argc != 8)
 {
  print_usage();
  exit (1);
 }
 if (gethostname (hostname, sizeof(hostname)) != 0)
  __builtin___strcpy_chk (hostname, "(Unknown host)", __builtin_object_size (hostname, 2 > 1 ? 1 : 0));
 time(&starttime);
 ctime_r(&starttime, startdate);
 CLOMP_numThreads = convert_to_positive_long ("numThreads", argv[1]);
 CLOMP_allocThreads = convert_to_positive_long ("numThreads", argv[2]);
 CLOMP_numParts = convert_to_positive_long ("numParts", argv[3]);
 CLOMP_zonesPerPart = convert_to_positive_long ("zonesPerPart", argv[4]);
 CLOMP_zoneSize = convert_to_positive_long ("zoneSize", argv[5]);
 CLOMP_flopScale = convert_to_positive_long ("flopScale", argv[6]);
 CLOMP_timeScale = convert_to_positive_long ("timeScale", argv[7]);
 if (CLOMP_zoneSize < sizeof (Zone))
 {
  printf ("***Forcing zoneSize (%ld specified) to minimum zone size %ld\n\n",
    CLOMP_zoneSize, (long) sizeof (Zone));
  CLOMP_zoneSize = sizeof(Zone);
 }
 printf ("       Invocation:");
 for (aidx = 0; aidx < argc; aidx ++)
 {
  printf (" %s", argv[aidx]);
 }
 printf ("\n");
 printf ("         Hostname: %s\n", hostname);
 printf ("       Start time: %s", startdate);
 printf ("       Executable: %s\n", CLOMP_exe_name);
 if (CLOMP_numThreads == -1)
 {
  CLOMP_numThreads = omp_get_max_threads();
  printf ("      numThreads: %d (using system default)\n",
    (int) CLOMP_numThreads);
 }
 else
 {
  printf ("      numThreads: %ld\n", CLOMP_numThreads);
 }
 if (CLOMP_allocThreads == -1)
 {
  CLOMP_allocThreads = CLOMP_numThreads;
  printf ("    allocThreads: %ld (using numThreads)\n",
    CLOMP_allocThreads);
 }
 else
 {
  printf ("    allocThreads: %ld\n", CLOMP_allocThreads);
 }
 printf ("        numParts: %ld\n", CLOMP_numParts);
 printf ("    zonesPerPart: %ld\n", CLOMP_zonesPerPart);
 printf ("       flopScale: %ld\n", CLOMP_flopScale);
 printf ("       timeScale: %ld\n", CLOMP_timeScale);
 printf ("        zoneSize: %ld\n", CLOMP_zoneSize);
 omp_set_num_threads ((int)CLOMP_allocThreads);
 partArray = (Part **) malloc (CLOMP_numParts * sizeof (Part*));
 if (partArray == ((void *)0))
 {
  fprintf (__stderrp, "Out of memory allocating part array\n");
  exit (1);
 }
 for (partId = 0; partId < CLOMP_numParts; partId++)
 {
  partArray[partId] = ((void *)0);
 }
 CLOMP_partRatio = 1.0/((double) CLOMP_numParts);
#pragma omp parallel for private(partId) schedule(static)
 for (partId = 0; partId < CLOMP_numParts; partId++)
 {
  Part *part;
  if ((part= (Part *) malloc (sizeof (Part))) == ((void *)0))
  {
   fprintf (__stderrp, "Out of memory allocating part\n");
   exit (1);
  }
  addPart(part, partId);
 }
#pragma omp parallel for private(partId) schedule(static)
 for (partId = 0; partId < CLOMP_numParts; partId++)
 {
  Zone *zoneArray, *zone;
  int zoneId;
  zoneArray = (Zone *)malloc (CLOMP_zoneSize * CLOMP_zonesPerPart);
  if (zoneArray == ((void *)0))
  {
   fprintf (__stderrp, "Out of memory allocate zone array\n");
   exit (1);
  }
  for (zoneId = 0; zoneId < CLOMP_zonesPerPart; zoneId++)
  {
   zone = &zoneArray[zoneId];
   addZone (partArray[partId], zone);
  }
 }
 totalZoneCount = (double)CLOMP_numParts * (double)CLOMP_zonesPerPart;
 printf ("   Zones per Part: %.0f\n", (double)CLOMP_zonesPerPart);
 printf ("      Total Zones: %.0f\n", (double)totalZoneCount);
 printf ("Memory (in bytes): %.0f\n", (double)(totalZoneCount*CLOMP_zoneSize) + (double)(sizeof(Part) * CLOMP_numParts));
 diterations = ceil((((double)1000000) * ((double)CLOMP_timeScale))/
   ((double)totalZoneCount * (double)CLOMP_flopScale));
 if (diterations > 2000000000.0)
 {
  printf ("*** Forcing iterations from (%g) to 2 billion\n",
    diterations);
  diterations = 2000000000.0;
 }
 CLOMP_num_iterations = (long) diterations;
 printf ("Scaled Iterations: %i\n", (int) CLOMP_num_iterations);
 printf ("  Total Subcycles: %.0f\n",
   (double) CLOMP_num_iterations * (double) 10.0);
 percent_residue = 0.0;
 deposit = CLOMP_partRatio;
 for (partId = 0; partId < CLOMP_numParts; partId++)
 {
  update_part (partArray[partId], deposit);
  percent_residue += partArray[partId]->residue;
 }
 printf ("Iteration Residue: %.6f%%\n", percent_residue*100.0);
 CLOMP_max_residue = (1.0*percent_residue)/(1-percent_residue);
 printf ("      Max Residue: %-8.8g\n", CLOMP_max_residue);
 omp_set_num_threads ((int)CLOMP_numThreads);
 printf ("---------------------\n");
 print_pseudocode ("calc_deposit", "------ Start calc_deposit Pseudocode ------");
 print_pseudocode ("calc_deposit", "/* Measure *only* non-threadable calc_deposit() overhead.*/");
 print_pseudocode ("calc_deposit", "/* Expect this overhead to be negligible.*/");
 print_pseudocode ("calc_deposit", "deposit = calc_deposit ();");
 print_pseudocode ("calc_deposit", "------- End calc_deposit Pseudocode -------");
 print_start_message ("calc_deposit");
 get_timestamp (&calc_deposit_start_ts);
 do_calc_deposit_only();
 get_timestamp (&calc_deposit_end_ts);
 calc_deposit_seconds = print_timestats ("calc_deposit",
   &calc_deposit_start_ts,
   &calc_deposit_end_ts, -1.0, -1.0);
 do_omp_barrier_only(1);
 print_pseudocode ("OMP Barrier", "------ Start OMP Barrier Pseudocode ------");
 print_pseudocode ("OMP Barrier", "/* Measure *only* OMP barrier overhead.*/");
 print_pseudocode ("OMP Barrier", "#pragma omp barrier");
 print_pseudocode ("OMP Barrier", "------- End OMP Barrier Pseudocode -------");
 print_start_message ("OMP Barrier");
 get_timestamp (&omp_barrier_start_ts);
 do_omp_barrier_only(CLOMP_num_iterations);
 get_timestamp (&omp_barrier_end_ts);
 omp_barrier_seconds = print_timestats ("OMP Barrier",
   &omp_barrier_start_ts,
   &omp_barrier_end_ts, -1.0, -1.0);
 reinitialize_parts();
 serial_ref_cycle();
 reinitialize_parts();
 print_pseudocode ("Serial Ref", "------ Start Serial Ref Pseudocode ------");
 print_pseudocode ("Serial Ref", "/* Measure serial reference performance */");
 print_pseudocode ("Serial Ref", "deposit = calc_deposit ();");
 print_pseudocode ("Serial Ref", "for (pidx = 0; pidx < numParts; pidx++)");
 print_pseudocode ("Serial Ref", "  update_part (partArray[pidx], deposit);");
 print_pseudocode ("Serial Ref", "------- End Serial Ref Pseudocode -------");
 print_start_message ("Serial Ref");
 get_timestamp (&serial_ref_start_ts);
 do_serial_ref_version();
 get_timestamp (&serial_ref_end_ts);
 print_data_stats ("Serial Ref");
 serial_ref_seconds = print_timestats ("Serial Ref", &serial_ref_start_ts,
   &serial_ref_end_ts, -1.0, -1.0);
 reinitialize_parts();
 do_bestcase_omp_version(1);
 reinitialize_parts();
 print_pseudocode ("Bestcase OMP", "------ Start Bestcase OMP Pseudocode ------");
 print_pseudocode ("Bestcase OMP", "/* Measure the bestcase ref loop runtime for */");
 print_pseudocode ("Bestcase OMP", "/* \"free\" threading the following code: */");
 print_pseudocode ("Bestcase OMP", "deposit = calc_deposit ();");
 print_pseudocode ("Bestcase OMP", "for (pidx = 0; pidx < numParts; pidx++)");
 print_pseudocode ("Bestcase OMP", "  update_part (partArray[pidx], deposit);");
 print_pseudocode ("Bestcase OMP", "------- End Bestcase OMP Pseudocode -------");
 print_start_message ("Bestcase OMP");
 get_timestamp (&bestcase_omp_start_ts);
 do_bestcase_omp_version(CLOMP_num_iterations);
 get_timestamp (&bestcase_omp_end_ts);
 bestcase_omp_seconds = print_timestats ("Bestcase OMP",
   &bestcase_omp_start_ts,
   &bestcase_omp_end_ts,
   serial_ref_seconds,
   -1.0);
 reinitialize_parts();
 static_omp_cycle();
 reinitialize_parts();
 print_pseudocode ("Static OMP", "------ Start Static OMP Pseudocode ------");
 print_pseudocode ("Static OMP", "/* Use OpenMP parallel for schedule(static) on original loop. */");
 print_pseudocode ("Static OMP", "deposit = calc_deposit ();");
 print_pseudocode ("Static OMP", "#pragma omp parallel for private (pidx) schedule(static)");
 print_pseudocode ("Static OMP", "for (pidx = 0; pidx < numParts; pidx++)");
 print_pseudocode ("Static OMP", "  update_part (partArray[pidx], deposit);");
 print_pseudocode ("Static OMP", "------- End Static OMP Pseudocode -------");
 print_start_message ("Static OMP");
 get_timestamp (&static_omp_start_ts);
 do_static_omp_version();
 get_timestamp (&static_omp_end_ts);
 print_data_stats ("Static OMP");
 static_omp_seconds = print_timestats ("Static OMP",
   &static_omp_start_ts,
   &static_omp_end_ts,
   serial_ref_seconds,
   bestcase_omp_seconds);
 reinitialize_parts();
 dynamic_omp_cycle();
 reinitialize_parts();
 print_pseudocode ("Dynamic OMP", "------ Start Dynamic OMP Pseudocode ------");
 print_pseudocode ("Dynamic OMP", "/* Use OpenMP parallel for schedule(dynamic) on orig loop. */");
 print_pseudocode ("Dynamic OMP", "deposit = calc_deposit ();");
 print_pseudocode ("Dynamic OMP", "#pragma omp parallel for private (pidx) schedule(dynamic)");
 print_pseudocode ("Dynamic OMP", "for (pidx = 0; pidx < numParts; pidx++)");
 print_pseudocode ("Dynamic OMP", "  update_part (partArray[pidx], deposit);");
 print_pseudocode ("Dynamic OMP", "------- End Dynamic OMP Pseudocode -------");
 print_start_message ("Dynamic OMP");
 get_timestamp (&dynamic_omp_start_ts);
 do_dynamic_omp_version();
 get_timestamp (&dynamic_omp_end_ts);
 print_data_stats ("Dynamic OMP");
 dynamic_omp_seconds = print_timestats ("Dynamic OMP",
   &dynamic_omp_start_ts,
   &dynamic_omp_end_ts,
   serial_ref_seconds,
   bestcase_omp_seconds);
 reinitialize_parts();
 do_manual_omp_version(1);
 reinitialize_parts();
 print_pseudocode ("Manual OMP", "------ Start Manual OMP Pseudocode ------");
 print_pseudocode ("Manual OMP", "/* At top level, spawn threads and manually partition parts*/");
 print_pseudocode ("Manual OMP", "#pragma omp parallel");
 print_pseudocode ("Manual OMP", "{");
 print_pseudocode ("Manual OMP", "   int startPidx = ... /* slice based on thread_id*/");
 print_pseudocode ("Manual OMP", "   for (iter = 0; iter < num_iterations; iter++) ");
 print_pseudocode ("Manual OMP", "      do_iter(startPidx, endPidx);");
 print_pseudocode ("Manual OMP", "}" );
 print_pseudocode ("Manual OMP", "..." );
 print_pseudocode ("Manual OMP", "do_modN(int startPidx, int endPidx) /*do_iter() calls*/" );
 print_pseudocode ("Manual OMP", "{");
 print_pseudocode ("Manual OMP", "  #pragma omp barrier /* All threads must finish first!*/");
 print_pseudocode ("Manual OMP", "  #pragma omp single  /* Only one thread calcs deposit!*/");
 print_pseudocode ("Manual OMP", "  {");
 print_pseudocode ("Manual OMP", "    deposit = calc_deposit (); /* Deposit shared by threads */");
 print_pseudocode ("Manual OMP", "  }  /* Implicit omp barrier at end of omp single */");
 print_pseudocode ("Manual OMP", "  /* All threads execute loop working just on their parts*/");
 print_pseudocode ("Manual OMP", "  for (pidx = startPidx; pidx <= endPidx; pidx++)");
 print_pseudocode ("Manual OMP", "    update_part (partArray[pidx], deposit);");
 print_pseudocode ("Manual OMP", "}");
 print_pseudocode ("Manual OMP", "------- End Manual OMP Pseudocode -------");
 print_start_message ("Manual OMP");
 get_timestamp (&manual_omp_start_ts);
 do_manual_omp_version(CLOMP_num_iterations);
 get_timestamp (&manual_omp_end_ts);
 print_data_stats ("Manual OMP");
 manual_omp_seconds = print_timestats ("Manual OMP",
   &manual_omp_start_ts,
   &manual_omp_end_ts,
   serial_ref_seconds,
   bestcase_omp_seconds);
 printf ("----------- Comma-delimited summary ----------\n");
 printf ("%s %ld %ld %ld %ld %ld %ld %ld, calc_deposit, OMP Barrier, Serial Ref, Bestcase OMP, Static OMP, Dynamic OMP, Manual OMP\n",
   CLOMP_exe_name,
   CLOMP_numThreads,
   CLOMP_allocThreads,
   CLOMP_numParts,
   CLOMP_zonesPerPart,
   CLOMP_zoneSize,
   CLOMP_flopScale,
   CLOMP_timeScale);
 printf ("Runtime, %g, %g, %g, %g, %g, %g, %g\n",
   calc_deposit_seconds,
   omp_barrier_seconds,
   serial_ref_seconds,
   bestcase_omp_seconds,
   static_omp_seconds,
   dynamic_omp_seconds,
   manual_omp_seconds);
 printf ("us/Loop, %g, %g, %g, %g, %g, %g, %g\n",
   (((calc_deposit_seconds*1000000.0)/((double)CLOMP_num_iterations * 10.0))),
   (((omp_barrier_seconds*1000000.0)/((double)CLOMP_num_iterations * 10.0))),
   (((serial_ref_seconds*1000000.0)/((double)CLOMP_num_iterations * 10.0))),
   (((bestcase_omp_seconds*1000000.0)/((double)CLOMP_num_iterations * 10.0))),
   (((static_omp_seconds*1000000.0)/((double)CLOMP_num_iterations * 10.0))),
   (((dynamic_omp_seconds*1000000.0)/((double)CLOMP_num_iterations * 10.0))),
   (((manual_omp_seconds*1000000.0)/((double)CLOMP_num_iterations * 10.0))));
 printf ("Speedup, N/A, N/A, %g, %g, %g, %g, %g\n",
   ((serial_ref_seconds/serial_ref_seconds)),
   ((serial_ref_seconds/bestcase_omp_seconds)),
   ((serial_ref_seconds/static_omp_seconds)),
   ((serial_ref_seconds/dynamic_omp_seconds)),
   ((serial_ref_seconds/manual_omp_seconds)));
 printf ("Efficacy, N/A, N/A, N/A, %.2f%%, %.2f%%, %.2f%%, %.2f%%\n",
   (((bestcase_omp_seconds/bestcase_omp_seconds)*100.0)),
   (((bestcase_omp_seconds/static_omp_seconds)*100.0)),
   (((bestcase_omp_seconds/dynamic_omp_seconds)*100.0)),
   (((bestcase_omp_seconds/manual_omp_seconds)*100.0)));
 printf ("Overhead, N/A, N/A, N/A, %.2f, %.2f, %.2f, %.2f\n",
   (((bestcase_omp_seconds-bestcase_omp_seconds)*1000000.0)/((double)CLOMP_num_iterations * 10.0)),
   (((static_omp_seconds-bestcase_omp_seconds)*1000000.0)/((double)CLOMP_num_iterations * 10.0)),
   (((dynamic_omp_seconds-bestcase_omp_seconds)*1000000.0)/((double)CLOMP_num_iterations * 10.0)),
   (((manual_omp_seconds-bestcase_omp_seconds)*1000000.0)/((double)CLOMP_num_iterations * 10.0)));
 return (0);
}
