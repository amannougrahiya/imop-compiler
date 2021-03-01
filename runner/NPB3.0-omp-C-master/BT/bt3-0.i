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

typedef signed char int8_t;
typedef short int16_t;
typedef int int32_t;
typedef long long int64_t;
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
typedef __darwin_intptr_t intptr_t;
typedef unsigned long uintptr_t;
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
typedef struct rusage_info_v3 rusage_info_current;
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
void *calloc(size_t __count, size_t __size) __attribute__((__warn_unused_result__));
div_t div(int, int) __attribute__((const));
void exit(int) __attribute__((noreturn));
void free(void *);
char *getenv(const char *);
long labs(long) __attribute__((const));
ldiv_t ldiv(long, long) __attribute__((const));
long long
  llabs(long long);
lldiv_t lldiv(long long, long long);
void *malloc(size_t __size) __attribute__((__warn_unused_result__));
int mblen(const char *__s, size_t __n);
size_t mbstowcs(wchar_t * restrict , const char * restrict, size_t);
int mbtowc(wchar_t * restrict, const char * restrict, size_t);
int posix_memalign(void **__memptr, size_t __alignment, size_t __size) ;
void qsort(void *__base, size_t __nel, size_t __width,
     int (* __compar)(const void *, const void *));
int rand(void) ;
void *realloc(void *__ptr, size_t __size) __attribute__((__warn_unused_result__));
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
typedef unsigned char u_int8_t;
typedef unsigned short u_int16_t;
typedef unsigned int u_int32_t;
typedef unsigned long long u_int64_t;
typedef int64_t register_t;
typedef u_int64_t user_addr_t;
typedef u_int64_t user_size_t;
typedef int64_t user_ssize_t;
typedef int64_t user_long_t;
typedef u_int64_t user_ulong_t;
typedef int64_t user_time_t;
typedef int64_t user_off_t;
typedef u_int64_t syscall_arg_t;
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
void *reallocf(void *__ptr, size_t __size);
long long
  strtoq(const char *__str, char **__endptr, int __base);
unsigned long long
  strtouq(const char *__str, char **__endptr, int __base);
extern char *suboptarg;
void *valloc(size_t);


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
static double us[12/2*2+1][12/2*2+1][12/2*2+1];
static double vs[12/2*2+1][12/2*2+1][12/2*2+1];
static double ws[12/2*2+1][12/2*2+1][12/2*2+1];
static double qs[12/2*2+1][12/2*2+1][12/2*2+1];
static double rho_i[12/2*2+1][12/2*2+1][12/2*2+1];
static double square[12/2*2+1][12/2*2+1][12/2*2+1];
static double forcing[12/2*2+1][12/2*2+1][12/2*2+1][5+1];
static double u[(12 +1)/2*2+1][(12 +1)/2*2+1][(12 +1)/2*2+1][5];
static double rhs[12/2*2+1][12/2*2+1][12/2*2+1][5];
static double lhs[12/2*2+1][12/2*2+1][12/2*2+1][3][5][5];
static double cuf[12];
static double q[12];
static double ue[12][5];
static double buf[12][5];
#pragma omp threadprivate(cuf, q, ue, buf)
static double fjac[12/2*2+1][12/2*2+1][12 -1+1][5][5];
static double njac[12/2*2+1][12/2*2+1][12 -1+1][5][5];
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
    niter = 60;
    dt = 0.010;
    grid_points[0] = 12;
    grid_points[1] = 12;
    grid_points[2] = 12;
  }
  printf(" Size: %3dx%3dx%3d\n",
  grid_points[0], grid_points[1], grid_points[2]);
  printf(" Iterations: %3d   dt: %10.6f\n", niter, dt);
  if (grid_points[0] > 12 ||
      grid_points[1] > 12 ||
      grid_points[2] > 12) {
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
    verified, "3.0 structured","15 Jul 2017", "gcc", "gcc", "(none)", "-I../common", "-O3 -fopenmp",
    "-O3 -fopenmp", "(none)");
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
  for (i = 0; i < 12; i++) {
    for (j = 0; j < 12; j++) {
      for (k = 0; k < 12; k++) {
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
