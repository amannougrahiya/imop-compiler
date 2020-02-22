unsigned short __builtin_bswap16 (unsigned short x) ;
unsigned int __builtin_bswap32 (unsigned int x) ;
unsigned long long __builtin_bswap64 (unsigned long long x) ;
extern double __builtin_inff(void);
extern float __builtin_inf(void);
extern long double __builtin_infl(void);
extern float __builtin_fabsf(float);
extern double __builtin_fabs(double);
extern long double __builtin_fabsl(long double);
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
struct __darwin_pthread_handler_rec
{
 void (*__routine)(void *);
 void *__arg;
 struct __darwin_pthread_handler_rec *__next;
};
struct _opaque_pthread_attr_t { long __sig; char __opaque[56]; };
struct _opaque_pthread_cond_t { long __sig; char __opaque[40]; };
struct _opaque_pthread_condattr_t { long __sig; char __opaque[8]; };
struct _opaque_pthread_mutex_t { long __sig; char __opaque[56]; };
struct _opaque_pthread_mutexattr_t { long __sig; char __opaque[8]; };
struct _opaque_pthread_once_t { long __sig; char __opaque[8]; };
struct _opaque_pthread_rwlock_t { long __sig; char __opaque[192]; };
struct _opaque_pthread_rwlockattr_t { long __sig; char __opaque[16]; };
struct _opaque_pthread_t { long __sig; struct __darwin_pthread_handler_rec *__cleanup_stack; char __opaque[1168]; };
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
typedef struct _opaque_pthread_attr_t
   __darwin_pthread_attr_t;
typedef struct _opaque_pthread_cond_t
   __darwin_pthread_cond_t;
typedef struct _opaque_pthread_condattr_t
   __darwin_pthread_condattr_t;
typedef unsigned long __darwin_pthread_key_t;
typedef struct _opaque_pthread_mutex_t
   __darwin_pthread_mutex_t;
typedef struct _opaque_pthread_mutexattr_t
   __darwin_pthread_mutexattr_t;
typedef struct _opaque_pthread_once_t
   __darwin_pthread_once_t;
typedef struct _opaque_pthread_rwlock_t
   __darwin_pthread_rwlock_t;
typedef struct _opaque_pthread_rwlockattr_t
   __darwin_pthread_rwlockattr_t;
typedef struct _opaque_pthread_t
   *__darwin_pthread_t;
typedef __uint32_t __darwin_sigset_t;
typedef __int32_t __darwin_suseconds_t;
typedef __uint32_t __darwin_uid_t;
typedef __uint32_t __darwin_useconds_t;
typedef unsigned char __darwin_uuid_t[16];
typedef char __darwin_uuid_string_t[37];
typedef int __darwin_nl_item;
typedef int __darwin_wctrans_t;
typedef __uint32_t __darwin_wctype_t;
typedef __darwin_va_list va_list;
typedef __darwin_size_t size_t;
extern int __builtin___sprintf_chk (char * , int, size_t,
     const char * , ...);

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
 int (*_close)(void *);
 int (*_read) (void *, char *, int);
 fpos_t (*_seek) (void *, fpos_t, int);
 int (*_write)(void *, const char *, int);
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
int fgetpos(FILE * , fpos_t *);
char *fgets(char * , int, FILE *);
FILE *fopen(const char * , const char * ) __asm("_" "fopen" );
int fprintf(FILE * , const char * , ...) __attribute__((__format__ (__printf__, 2, 3)));
int fputc(int, FILE *);
int fputs(const char * , FILE * ) __asm("_" "fputs" );
size_t fread(void * , size_t, size_t, FILE * );
FILE *freopen(const char * , const char * ,
                 FILE * ) __asm("_" "freopen" );
int fscanf(FILE * , const char * , ...) __attribute__((__format__ (__scanf__, 2, 3)));
int fseek(FILE *, long, int);
int fsetpos(FILE *, const fpos_t *);
long ftell(FILE *);
size_t fwrite(const void * , size_t, size_t, FILE * ) __asm("_" "fwrite" );
int getc(FILE *);
int getchar(void);
char *gets(char *);
void perror(const char *);
int printf(const char * , ...) __attribute__((__format__ (__printf__, 1, 2)));
int putc(int, FILE *);
int putchar(int);
int puts(const char *);
int remove(const char *);
int rename (const char *, const char *);
void rewind(FILE *);
int scanf(const char * , ...) __attribute__((__format__ (__scanf__, 1, 2)));
void setbuf(FILE * , char * );
int setvbuf(FILE * , char * , int, size_t);
int sprintf(char * , const char * , ...) __attribute__((__format__ (__printf__, 2, 3)));
int sscanf(const char * , const char * , ...) __attribute__((__format__ (__scanf__, 2, 3)));
FILE *tmpfile(void);
char *tmpnam(char *);
int ungetc(int, FILE *);
int vfprintf(FILE * , const char * , va_list) __attribute__((__format__ (__printf__, 2, 0)));
int vprintf(const char * , va_list) __attribute__((__format__ (__printf__, 1, 0)));
int vsprintf(char * , const char * , va_list) __attribute__((__format__ (__printf__, 2, 0)));


char *ctermid(char *);
FILE *fdopen(int, const char *) __asm("_" "fdopen" );
int fileno(FILE *);


int pclose(FILE *);
FILE *popen(const char *, const char *) __asm("_" "popen" );


int __srget(FILE *);
int __svfscanf(FILE *, const char *, va_list) __attribute__((__format__ (__scanf__, 2, 0)));
int __swbuf(int, FILE *);

static __inline int __sputc(int _c, FILE *_p) {
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
char *tempnam(const char *, const char *) __asm("_" "tempnam" );

typedef __darwin_off_t off_t;

int fseeko(FILE *, off_t, int);
off_t ftello(FILE *);


int snprintf(char * , size_t, const char * , ...) __attribute__((__format__ (__printf__, 3, 4)));
int vfscanf(FILE * , const char * , va_list) __attribute__((__format__ (__scanf__, 2, 0)));
int vscanf(const char * , va_list) __attribute__((__format__ (__scanf__, 1, 0)));
int vsnprintf(char * , size_t, const char * , va_list) __attribute__((__format__ (__printf__, 3, 0)));
int vsscanf(const char * , const char * , va_list) __attribute__((__format__ (__scanf__, 2, 0)));

typedef __darwin_ssize_t ssize_t;

int dprintf(int, const char * , ...) __attribute__((__format__ (__printf__, 2, 3))) __attribute__((visibility("default")));
int vdprintf(int, const char * , va_list) __attribute__((__format__ (__printf__, 2, 0))) __attribute__((visibility("default")));
ssize_t getdelim(char ** , size_t * , int, FILE * ) __attribute__((visibility("default")));
ssize_t getline(char ** , size_t * , FILE * ) __attribute__((visibility("default")));


extern const int sys_nerr;
extern const char *const sys_errlist[];
int asprintf(char **, const char *, ...) __attribute__((__format__ (__printf__, 2, 3)));
char *ctermid_r(char *);
char *fgetln(FILE *, size_t *);
const char *fmtcheck(const char *, const char *);
int fpurge(FILE *);
void setbuffer(FILE *, char *, int);
int setlinebuf(FILE *);
int vasprintf(char **, const char *, va_list) __attribute__((__format__ (__printf__, 2, 0)));
FILE *zopen(const char *, const char *, int);
FILE *funopen(const void *,
                 int (*)(void *, char *, int),
                 int (*)(void *, const char *, int),
                 fpos_t (*)(void *, fpos_t, int),
                 int (*)(void *));

extern int __sprintf_chk (char * , int, size_t,
     const char * , ...)
  ;
extern int __snprintf_chk (char * , size_t, int, size_t,
      const char * , ...)
  ;
extern int __vsprintf_chk (char * , int, size_t,
      const char * , va_list)
  ;
extern int __vsnprintf_chk (char * , size_t, int, size_t,
       const char * , va_list)
  ;
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
struct __darwin_sigaltstack
{
 void *ss_sp;
 __darwin_size_t ss_size;
 int ss_flags;
};
struct __darwin_ucontext
{
 int uc_onstack;
 __darwin_sigset_t uc_sigmask;
 struct __darwin_sigaltstack uc_stack;
 struct __darwin_ucontext *uc_link;
 __darwin_size_t uc_mcsize;
 struct __darwin_mcontext64 *uc_mcontext;
};
typedef struct __darwin_sigaltstack stack_t;
typedef struct __darwin_ucontext ucontext_t;
typedef __darwin_pthread_attr_t pthread_attr_t;
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
struct rlimit {
 rlim_t rlim_cur;
 rlim_t rlim_max;
};

int getpriority(int, id_t);
int getiopolicy_np(int, int) __attribute__((visibility("default")));
int getrlimit(int, struct rlimit *) __asm("_" "getrlimit" );
int getrusage(int, struct rusage *);
int setpriority(int, id_t, int);
int setiopolicy_np(int, int, int) __attribute__((visibility("default")));
int setrlimit(int, const struct rlimit *) __asm("_" "setrlimit" );

static __inline__
__uint16_t
_OSSwapInt16(
    __uint16_t _data
)
{
    return ((_data << 8) | (_data >> 8));
}
static __inline__
__uint32_t
_OSSwapInt32(
    __uint32_t _data
)
{
    return __builtin_bswap32(_data);
}
static __inline__
__uint64_t
_OSSwapInt64(
    __uint64_t _data
)
{
    return __builtin_bswap64(_data);
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

void abort(void) __attribute__((__noreturn__));
int abs(int) __attribute__((__const__));
int atexit(void (*)(void));
double atof(const char *);
int atoi(const char *);
long atol(const char *);
long long
  atoll(const char *);
void *bsearch(const void *, const void *, size_t,
     size_t, int (*)(const void *, const void *));
void *calloc(size_t, size_t);
div_t div(int, int) __attribute__((__const__));
void exit(int) __attribute__((__noreturn__));
void free(void *);
char *getenv(const char *);
long labs(long) __attribute__((__const__));
ldiv_t ldiv(long, long) __attribute__((__const__));
long long
  llabs(long long);
lldiv_t lldiv(long long, long long);
void *malloc(size_t);
int mblen(const char *, size_t);
size_t mbstowcs(wchar_t * , const char * , size_t);
int mbtowc(wchar_t * , const char * , size_t);
int posix_memalign(void **, size_t, size_t) __attribute__((visibility("default")));
void qsort(void *, size_t, size_t,
     int (*)(const void *, const void *));
int rand(void);
void *realloc(void *, size_t);
void srand(unsigned);
double strtod(const char *, char **) __asm("_" "strtod" );
float strtof(const char *, char **) __asm("_" "strtof" );
long strtol(const char *, char **, int);
long double
  strtold(const char *, char **) ;
long long
  strtoll(const char *, char **, int);
unsigned long
  strtoul(const char *, char **, int);
unsigned long long
  strtoull(const char *, char **, int);
int system(const char *) __asm("_" "system" );
size_t wcstombs(char * , const wchar_t * , size_t);
int wctomb(char *, wchar_t);
void _Exit(int) __attribute__((__noreturn__));
long a64l(const char *);
double drand48(void);
char *ecvt(double, int, int *, int *);
double erand48(unsigned short[3]);
char *fcvt(double, int, int *, int *);
char *gcvt(double, int, char *);
int getsubopt(char **, char * const *, char **);
int grantpt(int);
char *initstate(unsigned, char *, size_t);
long jrand48(unsigned short[3]);
char *l64a(long);
void lcong48(unsigned short[7]);
long lrand48(void);
char *mktemp(char *);
int mkstemp(char *);
long mrand48(void);
long nrand48(unsigned short[3]);
int posix_openpt(int);
char *ptsname(int);
int putenv(char *) __asm("_" "putenv" );
long random(void);
int rand_r(unsigned *);
char *realpath(const char * , char * ) __asm("_" "realpath" "$DARWIN_EXTSN");
unsigned short
 *seed48(unsigned short[3]);
int setenv(const char *, const char *, int) __asm("_" "setenv" );
void setkey(const char *) __asm("_" "setkey" );
char *setstate(const char *);
void srand48(long);
void srandom(unsigned);
int unlockpt(int);
int unsetenv(const char *) __asm("_" "unsetenv" );
typedef signed char int8_t;
typedef unsigned char u_int8_t;
typedef short int16_t;
typedef unsigned short u_int16_t;
typedef int int32_t;
typedef unsigned int u_int32_t;
typedef long long int64_t;
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
typedef __darwin_dev_t dev_t;
typedef __darwin_mode_t mode_t;
u_int32_t
  arc4random(void);
void arc4random_addrandom(unsigned char * , int );
void arc4random_buf(void * , size_t ) __attribute__((visibility("default")));
void arc4random_stir(void);
u_int32_t
  arc4random_uniform(u_int32_t ) __attribute__((visibility("default")));
int atexit_b(void (*)(void)) __attribute__((visibility("default")));
void *bsearch_b(const void *, const void *, size_t,
     size_t, int (*)(const void *, const void *)) __attribute__((visibility("default")));
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
int daemon(int, int) __asm("_" "daemon" "$1050") __attribute__((deprecated,visibility("default")));
char *devname(dev_t, mode_t);
char *devname_r(dev_t, mode_t, char *buf, int len);
char *getbsize(int *, long *);
int getloadavg(double [], int);
const char
 *getprogname(void);
int heapsort(void *, size_t, size_t,
     int (*)(const void *, const void *));
int heapsort_b(void *, size_t, size_t,
     int (*)(const void *, const void *)) __attribute__((visibility("default")));
int mergesort(void *, size_t, size_t,
     int (*)(const void *, const void *));
int mergesort_b(void *, size_t, size_t,
     int (*)(const void *, const void *)) __attribute__((visibility("default")));
void psort(void *, size_t, size_t,
     int (*)(const void *, const void *)) __attribute__((visibility("default")));
void psort_b(void *, size_t, size_t,
     int (*)(const void *, const void *)) __attribute__((visibility("default")));
void psort_r(void *, size_t, size_t, void *,
     int (*)(void *, const void *, const void *)) __attribute__((visibility("default")));
void qsort_b(void *, size_t, size_t,
     int (*)(const void *, const void *)) __attribute__((visibility("default")));
void qsort_r(void *, size_t, size_t, void *,
     int (*)(void *, const void *, const void *));
int radixsort(const unsigned char **, int, const unsigned char *,
     unsigned);
void setprogname(const char *);
int sradixsort(const unsigned char **, int, const unsigned char *,
     unsigned);
void sranddev(void);
void srandomdev(void);
void *reallocf(void *, size_t);
long long
  strtoq(const char *, char **, int);
unsigned long long
  strtouq(const char *, char **, int);
extern char *suboptarg;
void *valloc(size_t);

 typedef float float_t;
 typedef double double_t;
extern int __math_errhandling ( void );
extern int __fpclassifyf(float );
extern int __fpclassifyd(double );
extern int __fpclassify (long double);
 static __inline__ int __inline_isfinitef (float ) __attribute__ ((always_inline));
 static __inline__ int __inline_isfinited (double ) __attribute__ ((always_inline));
 static __inline__ int __inline_isfinite (long double) __attribute__ ((always_inline));
 static __inline__ int __inline_isinff (float ) __attribute__ ((always_inline));
 static __inline__ int __inline_isinfd (double ) __attribute__ ((always_inline));
 static __inline__ int __inline_isinf (long double) __attribute__ ((always_inline));
 static __inline__ int __inline_isnanf (float ) __attribute__ ((always_inline));
 static __inline__ int __inline_isnand (double ) __attribute__ ((always_inline));
 static __inline__ int __inline_isnan (long double) __attribute__ ((always_inline));
 static __inline__ int __inline_isnormalf (float ) __attribute__ ((always_inline));
 static __inline__ int __inline_isnormald (double ) __attribute__ ((always_inline));
 static __inline__ int __inline_isnormal (long double) __attribute__ ((always_inline));
 static __inline__ int __inline_signbitf (float ) __attribute__ ((always_inline));
 static __inline__ int __inline_signbitd (double ) __attribute__ ((always_inline));
 static __inline__ int __inline_signbit (long double) __attribute__ ((always_inline));
 static __inline__ int __inline_isinff( float __x ) { return __builtin_fabsf(__x) == __builtin_inff(); }
 static __inline__ int __inline_isinfd( double __x ) { return __builtin_fabs(__x) == __builtin_inf(); }
 static __inline__ int __inline_isinf( long double __x ) { return __builtin_fabsl(__x) == __builtin_infl(); }
 static __inline__ int __inline_isfinitef( float __x ) { return __x == __x && __builtin_fabsf(__x) != __builtin_inff(); }
 static __inline__ int __inline_isfinited( double __x ) { return __x == __x && __builtin_fabs(__x) != __builtin_inf(); }
 static __inline__ int __inline_isfinite( long double __x ) { return __x == __x && __builtin_fabsl(__x) != __builtin_infl(); }
 static __inline__ int __inline_isnanf( float __x ) { return __x != __x; }
 static __inline__ int __inline_isnand( double __x ) { return __x != __x; }
 static __inline__ int __inline_isnan( long double __x ) { return __x != __x; }
 static __inline__ int __inline_signbitf( float __x ) { union{ float __f; unsigned int __u; }__u; __u.__f = __x; return (int)(__u.__u >> 31); }
 static __inline__ int __inline_signbitd( double __x ) { union{ double __f; unsigned int __u[2]; }__u; __u.__f = __x; return (int)(__u.__u[1] >> 31); }
 static __inline__ int __inline_signbit( long double __x ){ union{ long double __ld; struct{ unsigned int __m[2]; short __sexp; }__p; }__u; __u.__ld = __x; return (int) (((unsigned short) __u.__p.__sexp) >> 15); }
 static __inline__ int __inline_isnormalf( float __x ) { float fabsf = __builtin_fabsf(__x); if( __x != __x ) return 0; return fabsf < __builtin_inff() && fabsf >= 1.17549435e-38F; }
 static __inline__ int __inline_isnormald( double __x ) { double fabsf = __builtin_fabs(__x); if( __x != __x ) return 0; return fabsf < __builtin_inf() && fabsf >= 2.2250738585072014e-308; }
 static __inline__ int __inline_isnormal( long double __x ) { long double fabsf = __builtin_fabsl(__x); if( __x != __x ) return 0; return fabsf < __builtin_infl() && fabsf >= 3.36210314311209350626e-4932L; }
extern double acos( double );
extern float acosf( float );
extern double asin( double );
extern float asinf( float );
extern double atan( double );
extern float atanf( float );
extern double atan2( double, double );
extern float atan2f( float, float );
extern double cos( double );
extern float cosf( float );
extern double sin( double );
extern float sinf( float );
extern double tan( double );
extern float tanf( float );
extern double acosh( double );
extern float acoshf( float );
extern double asinh( double );
extern float asinhf( float );
extern double atanh( double );
extern float atanhf( float );
extern double cosh( double );
extern float coshf( float );
extern double sinh( double );
extern float sinhf( float );
extern double tanh( double );
extern float tanhf( float );
extern double exp ( double );
extern float expf ( float );
extern double exp2 ( double );
extern float exp2f ( float );
extern double expm1 ( double );
extern float expm1f ( float );
extern double log ( double );
extern float logf ( float );
extern double log10 ( double );
extern float log10f ( float );
extern double log2 ( double );
extern float log2f ( float );
extern double log1p ( double );
extern float log1pf ( float );
extern double logb ( double );
extern float logbf ( float );
extern double modf ( double, double * );
extern float modff ( float, float * );
extern double ldexp ( double, int );
extern float ldexpf ( float, int );
extern double frexp ( double, int * );
extern float frexpf ( float, int * );
extern int ilogb ( double );
extern int ilogbf ( float );
extern double scalbn ( double, int );
extern float scalbnf ( float, int );
extern double scalbln ( double, long int );
extern float scalblnf ( float, long int );
extern double fabs( double );
extern float fabsf( float );
extern double cbrt( double );
extern float cbrtf( float );
extern double hypot ( double, double );
extern float hypotf ( float, float );
extern double pow ( double, double );
extern float powf ( float, float );
extern double sqrt( double );
extern float sqrtf( float );
extern double erf( double );
extern float erff( float );
extern double erfc( double );
extern float erfcf( float );
extern double lgamma( double );
extern float lgammaf( float );
extern double tgamma( double );
extern float tgammaf( float );
extern double ceil ( double );
extern float ceilf ( float );
extern double floor ( double );
extern float floorf ( float );
extern double nearbyint ( double );
extern float nearbyintf ( float );
extern double rint ( double );
extern float rintf ( float );
extern long int lrint ( double );
extern long int lrintf ( float );
extern double round ( double );
extern float roundf ( float );
extern long int lround ( double );
extern long int lroundf ( float );
    extern long long int llrint ( double );
    extern long long int llrintf ( float );
    extern long long int llround ( double );
    extern long long int llroundf ( float );
extern double trunc ( double );
extern float truncf ( float );
extern double fmod ( double, double );
extern float fmodf ( float, float );
extern double remainder ( double, double );
extern float remainderf ( float, float );
extern double remquo ( double, double, int * );
extern float remquof ( float, float, int * );
extern double copysign ( double, double );
extern float copysignf ( float, float );
extern double nan( const char * );
extern float nanf( const char * );
extern double nextafter ( double, double );
extern float nextafterf ( float, float );
extern double fdim ( double, double );
extern float fdimf ( float, float );
extern double fmax ( double, double );
extern float fmaxf ( float, float );
extern double fmin ( double, double );
extern float fminf ( float, float );
extern double fma ( double, double, double );
extern float fmaf ( float, float, float );
extern long double acosl(long double);
extern long double asinl(long double);
extern long double atanl(long double);
extern long double atan2l(long double, long double);
extern long double cosl(long double);
extern long double sinl(long double);
extern long double tanl(long double);
extern long double acoshl(long double);
extern long double asinhl(long double);
extern long double atanhl(long double);
extern long double coshl(long double);
extern long double sinhl(long double);
extern long double tanhl(long double);
extern long double expl(long double);
extern long double exp2l(long double);
extern long double expm1l(long double);
extern long double logl(long double);
extern long double log10l(long double);
extern long double log2l(long double);
extern long double log1pl(long double);
extern long double logbl(long double);
extern long double modfl(long double, long double *);
extern long double ldexpl(long double, int);
extern long double frexpl(long double, int *);
extern int ilogbl(long double);
extern long double scalbnl(long double, int);
extern long double scalblnl(long double, long int);
extern long double fabsl(long double);
extern long double cbrtl(long double);
extern long double hypotl(long double, long double);
extern long double powl(long double, long double);
extern long double sqrtl(long double);
extern long double erfl(long double);
extern long double erfcl(long double);
extern long double lgammal(long double);
extern long double tgammal(long double);
extern long double ceill(long double);
extern long double floorl(long double);
extern long double nearbyintl(long double);
extern long double rintl(long double);
extern long int lrintl(long double);
extern long double roundl(long double);
extern long int lroundl(long double);
    extern long long int llrintl(long double);
    extern long long int llroundl(long double);
extern long double truncl(long double);
extern long double fmodl(long double, long double);
extern long double remainderl(long double, long double);
extern long double remquol(long double, long double, int *);
extern long double copysignl(long double, long double);
extern long double nanl(const char *);
extern long double nextafterl(long double, long double);
extern double nexttoward(double, long double);
extern float nexttowardf(float, long double);
extern long double nexttowardl(long double, long double);
extern long double fdiml(long double, long double);
extern long double fmaxl(long double, long double);
extern long double fminl(long double, long double);
extern long double fmal(long double, long double, long double);
extern double __inf( void );
extern float __inff( void );
extern long double __infl( void );
extern float __nan( void );
extern double j0 ( double );
extern double j1 ( double );
extern double jn ( int, double );
extern double y0 ( double );
extern double y1 ( double );
extern double yn ( int, double );
extern double scalb ( double, double );
extern int signgam;
extern long int rinttol ( double );
extern long int roundtol ( double );
struct exception {
 int type;
 char *name;
 double arg1;
 double arg2;
 double retval;
};
extern int finite ( double );
extern double gamma ( double );
extern int matherr ( struct exception * );
extern double significand ( double );
extern double drem ( double, double );
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
static int naa;
static int nzz;
static int firstrow;
static int lastrow;
static int firstcol;
static int lastcol;
static int colidx[NA*(NONZER+1)*(NONZER+1)+NA*(NONZER+2)+1];
static int rowstr[NA+1+1];
static int iv[2*NA+1+1];
static int arow[NA*(NONZER+1)*(NONZER+1)+NA*(NONZER+2)+1];
static int acol[NA*(NONZER+1)*(NONZER+1)+NA*(NONZER+2)+1];
static double v[NA+1+1];
static double aelt[NA*(NONZER+1)*(NONZER+1)+NA*(NONZER+2)+1];
static double a[NA*(NONZER+1)*(NONZER+1)+NA*(NONZER+2)+1];
static double x[NA+2+1];
static double z[NA+2+1];
static double p[NA+2+1];
static double q[NA+2+1];
static double r[NA+2+1];
static double w[NA+2+1];
static double amult;
static double tran;
static void conj_grad (int colidx[], int rowstr[], double x[], double z[],
         double a[], double p[], double q[], double r[],
         double w[], double *rnorm);
static void makea(int n, int nz, double a[], int colidx[], int rowstr[],
    int nonzer, int firstrow, int lastrow, int firstcol,
    int lastcol, double rcond, int arow[], int acol[],
    double aelt[], double v[], int iv[], double shift );
static void sparse(double a[], int colidx[], int rowstr[], int n,
     int arow[], int acol[], double aelt[],
     int firstrow, int lastrow,
     double x[], boolean mark[], int nzloc[], int nnza);
static void sprnvc(int n, int nz, double v[], int iv[], int nzloc[],
     int mark[]);
static int icnvrt(double x, int ipwr2);
static void vecset(int n, double v[], int iv[], int *nzv, int i, double val);
int main(int argc, char **argv) {
    int i, j, k, it;
    int nthreads = 1;
    double zeta;
    double rnorm;
    double norm_temp11;
    double norm_temp12;
    double t, mflops;
    char class;
    boolean verified;
    double zeta_verify_value, epsilon;
    firstrow = 1;
    lastrow = NA;
    firstcol = 1;
    lastcol = NA;
    if (NA == 1400 && NONZER == 7 && NITER == 15 && SHIFT == 10.0) {
 class = 'S';
 zeta_verify_value = 8.5971775078648;
    } else if (NA == 7000 && NONZER == 8 && NITER == 15 && SHIFT == 12.0) {
 class = 'W';
 zeta_verify_value = 10.362595087124;
    } else if (NA == 14000 && NONZER == 11 && NITER == 15 && SHIFT == 20.0) {
 class = 'A';
 zeta_verify_value = 17.130235054029;
    } else if (NA == 75000 && NONZER == 13 && NITER == 75 && SHIFT == 60.0) {
 class = 'B';
 zeta_verify_value = 22.712745482631;
    } else if (NA == 150000 && NONZER == 15 && NITER == 75 && SHIFT == 110.0) {
 class = 'C';
 zeta_verify_value = 28.973605592845;
    } else {
 class = 'U';
    }
    printf("\n\n NAS Parallel Benchmarks 2.3 OpenMP C version"
    " - CG Benchmark\n");
    printf(" Size: %10d\n", NA);
    printf(" Iterations: %5d\n", NITER);
    naa = NA;
    nzz = NA*(NONZER+1)*(NONZER+1)+NA*(NONZER+2);
    tran = 314159265.0;
    amult = 1220703125.0;
    zeta = randlc( &tran, amult );
    makea(naa, nzz, a, colidx, rowstr, NONZER,
   firstrow, lastrow, firstcol, lastcol,
   RCOND, arow, acol, aelt, v, iv, SHIFT);
#pragma omp parallel private(it,i,j,k)
{
#pragma omp for nowait
    for (j = 1; j <= lastrow - firstrow + 1; j++) {
 for (k = rowstr[j]; k < rowstr[j+1]; k++) {
            colidx[k] = colidx[k] - firstcol + 1;
 }
    }
#pragma omp for nowait
    for (i = 1; i <= NA+1; i++) {
 x[i] = 1.0;
    }
#pragma omp single
    zeta = 0.0;
    for (it = 1; it <= 1; it++) {
 conj_grad (colidx, rowstr, x, z, a, p, q, r, w, &rnorm);
#pragma omp single
{
 norm_temp11 = 0.0;
 norm_temp12 = 0.0;
}
#pragma omp for reduction(+:norm_temp11,norm_temp12)
 for (j = 1; j <= lastcol-firstcol+1; j++) {
            norm_temp11 = norm_temp11 + x[j]*z[j];
            norm_temp12 = norm_temp12 + z[j]*z[j];
 }
#pragma omp single
 norm_temp12 = 1.0 / sqrt( norm_temp12 );
#pragma omp for
 for (j = 1; j <= lastcol-firstcol+1; j++) {
            x[j] = norm_temp12*z[j];
 }
    }
#pragma omp for nowait
    for (i = 1; i <= NA+1; i++) {
         x[i] = 1.0;
    }
#pragma omp single
    zeta = 0.0;
}
    timer_clear( 1 );
    timer_start( 1 );
#pragma omp parallel private(it,i,j,k)
{
    for (it = 1; it <= NITER; it++) {
 conj_grad(colidx, rowstr, x, z, a, p, q, r, w, &rnorm);
#pragma omp single
{
 norm_temp11 = 0.0;
 norm_temp12 = 0.0;
}
#pragma omp for reduction(+:norm_temp11,norm_temp12)
 for (j = 1; j <= lastcol-firstcol+1; j++) {
            norm_temp11 = norm_temp11 + x[j]*z[j];
            norm_temp12 = norm_temp12 + z[j]*z[j];
 }
#pragma omp single
{
 norm_temp12 = 1.0 / sqrt( norm_temp12 );
 zeta = SHIFT + 1.0 / norm_temp11;
}
#pragma omp master
{
 if( it == 1 ) {
   printf("   iteration           ||r||                 zeta\n");
 }
 printf("    %5d       %20.14e%20.13e\n", it, rnorm, zeta);
}
#pragma omp for
 for (j = 1; j <= lastcol-firstcol+1; j++) {
            x[j] = norm_temp12*z[j];
 }
    }
}
    timer_stop( 1 );
    t = timer_read( 1 );
    printf(" Benchmark completed\n");
    epsilon = 1.0e-10;
    if (class != 'U') {
 if (fabs(zeta - zeta_verify_value) <= epsilon) {
            verified = 1;
     printf(" VERIFICATION SUCCESSFUL\n");
     printf(" Zeta is    %20.12e\n", zeta);
     printf(" Error is   %20.12e\n", zeta - zeta_verify_value);
 } else {
            verified = 0;
     printf(" VERIFICATION FAILED\n");
     printf(" Zeta                %20.12e\n", zeta);
     printf(" The correct zeta is %20.12e\n", zeta_verify_value);
 }
    } else {
 verified = 0;
 printf(" Problem size unknown\n");
 printf(" NO VERIFICATION PERFORMED\n");
    }
    if ( t != 0.0 ) {
 mflops = (2.0*NITER*NA)
     * (3.0+(NONZER*(NONZER+1)) + 25.0*(5.0+(NONZER*(NONZER+1))) + 3.0 )
     / t / 1000000.0;
    } else {
 mflops = 0.0;
    }
    c_print_results("CG", class, NA, 0, 0, NITER, nthreads, t,
      mflops, "          floating point",
      verified, "2.3", "16 May 2013",
      "gcc", "gcc", "(none)", "-I../common", "-O3 -lm -fopenmp ", "(none)", "randdp");
}
static void conj_grad (
    int colidx[],
    int rowstr[],
    double x[],
    double z[],
    double a[],
    double p[],
    double q[],
    double r[],
    double w[],
    double *rnorm )
{
    static double d, sum, rho, rho0, alpha, beta;
    int i, j, k;
    int cgit, cgitmax = 25;
#pragma omp single nowait
    rho = 0.0;
#pragma omp for nowait
    for (j = 1; j <= naa+1; j++) {
 q[j] = 0.0;
 z[j] = 0.0;
 r[j] = x[j];
 p[j] = r[j];
 w[j] = 0.0;
    }
#pragma omp for reduction(+:rho)
    for (j = 1; j <= lastcol-firstcol+1; j++) {
 rho = rho + x[j]*x[j];
    }
    for (cgit = 1; cgit <= cgitmax; cgit++) {
#pragma omp single nowait
{
      rho0 = rho;
      d = 0.0;
      rho = 0.0;
}
#pragma omp for private(sum,k)
 for (j = 1; j <= lastrow-firstrow+1; j++) {
            sum = 0.0;
     for (k = rowstr[j]; k < rowstr[j+1]; k++) {
  sum = sum + a[k]*p[colidx[k]];
     }
            w[j] = sum;
 }
#pragma omp for
 for (j = 1; j <= lastcol-firstcol+1; j++) {
            q[j] = w[j];
 }
#pragma omp for nowait
 for (j = 1; j <= lastcol-firstcol+1; j++) {
            w[j] = 0.0;
 }
#pragma omp for reduction(+:d)
 for (j = 1; j <= lastcol-firstcol+1; j++) {
            d = d + p[j]*q[j];
 }
#pragma omp single
 alpha = rho0 / d;
#pragma omp for
 for (j = 1; j <= lastcol-firstcol+1; j++) {
            z[j] = z[j] + alpha*p[j];
            r[j] = r[j] - alpha*q[j];
 }
#pragma omp for reduction(+:rho)
 for (j = 1; j <= lastcol-firstcol+1; j++) {
            rho = rho + r[j]*r[j];
 }
#pragma omp single
 beta = rho / rho0;
#pragma omp for
 for (j = 1; j <= lastcol-firstcol+1; j++) {
            p[j] = r[j] + beta*p[j];
 }
    }
#pragma omp single nowait
    sum = 0.0;
#pragma omp for private(d, k)
    for (j = 1; j <= lastrow-firstrow+1; j++) {
 d = 0.0;
 for (k = rowstr[j]; k <= rowstr[j+1]-1; k++) {
            d = d + a[k]*z[colidx[k]];
 }
 w[j] = d;
    }
#pragma omp for
    for (j = 1; j <= lastcol-firstcol+1; j++) {
 r[j] = w[j];
    }
#pragma omp for reduction(+:sum) private(d)
    for (j = 1; j <= lastcol-firstcol+1; j++) {
 d = x[j] - r[j];
 sum = sum + d*d;
    }
#pragma omp single
  {
    (*rnorm) = sqrt(sum);
  }
}
static void makea(
    int n,
    int nz,
    double a[],
    int colidx[],
    int rowstr[],
    int nonzer,
    int firstrow,
    int lastrow,
    int firstcol,
    int lastcol,
    double rcond,
    int arow[],
    int acol[],
    double aelt[],
    double v[],
    int iv[],
    double shift )
{
    int i, nnza, iouter, ivelt, ivelt1, irow, nzv;
    double size, ratio, scale;
    int jcol;
    size = 1.0;
    ratio = pow(rcond, (1.0 / (double)n));
    nnza = 0;
#pragma omp parallel for
    for (i = 1; i <= n; i++) {
 colidx[n+i] = 0;
    }
    for (iouter = 1; iouter <= n; iouter++) {
 nzv = nonzer;
 sprnvc(n, nzv, v, iv, &(colidx[0]), &(colidx[n]));
 vecset(n, v, iv, &nzv, iouter, 0.5);
 for (ivelt = 1; ivelt <= nzv; ivelt++) {
     jcol = iv[ivelt];
     if (jcol >= firstcol && jcol <= lastcol) {
  scale = size * v[ivelt];
  for (ivelt1 = 1; ivelt1 <= nzv; ivelt1++) {
             irow = iv[ivelt1];
                    if (irow >= firstrow && irow <= lastrow) {
   nnza = nnza + 1;
   if (nnza > nz) {
       printf("Space for matrix elements exceeded in"
       " makea\n");
       printf("nnza, nzmax = %d, %d\n", nnza, nz);
       printf("iouter = %d\n", iouter);
       exit(1);
   }
   acol[nnza] = jcol;
   arow[nnza] = irow;
   aelt[nnza] = v[ivelt1] * scale;
      }
  }
     }
 }
 size = size * ratio;
    }
    for (i = firstrow; i <= lastrow; i++) {
 if (i >= firstcol && i <= lastcol) {
     iouter = n + i;
     nnza = nnza + 1;
     if (nnza > nz) {
  printf("Space for matrix elements exceeded in makea\n");
  printf("nnza, nzmax = %d, %d\n", nnza, nz);
  printf("iouter = %d\n", iouter);
  exit(1);
     }
     acol[nnza] = i;
     arow[nnza] = i;
     aelt[nnza] = rcond - shift;
 }
    }
    sparse(a, colidx, rowstr, n, arow, acol, aelt,
    firstrow, lastrow, v, &(iv[0]), &(iv[n]), nnza);
}
static void sparse(
    double a[],
    int colidx[],
    int rowstr[],
    int n,
    int arow[],
    int acol[],
    double aelt[],
    int firstrow,
    int lastrow,
    double x[],
    boolean mark[],
    int nzloc[],
    int nnza)
{
    int nrows;
    int i, j, jajp1, nza, k, nzrow;
    double xi;
    nrows = lastrow - firstrow + 1;
#pragma omp parallel for
    for (j = 1; j <= n; j++) {
 rowstr[j] = 0;
 mark[j] = 0;
    }
    rowstr[n+1] = 0;
    for (nza = 1; nza <= nnza; nza++) {
 j = (arow[nza] - firstrow + 1) + 1;
 rowstr[j] = rowstr[j] + 1;
    }
    rowstr[1] = 1;
    for (j = 2; j <= nrows+1; j++) {
 rowstr[j] = rowstr[j] + rowstr[j-1];
    }
    for (nza = 1; nza <= nnza; nza++) {
 j = arow[nza] - firstrow + 1;
 k = rowstr[j];
 a[k] = aelt[nza];
 colidx[k] = acol[nza];
 rowstr[j] = rowstr[j] + 1;
    }
    for (j = nrows; j >= 1; j--) {
 rowstr[j+1] = rowstr[j];
    }
    rowstr[1] = 1;
    nza = 0;
#pragma omp parallel for
    for (i = 1; i <= n; i++) {
 x[i] = 0.0;
 mark[i] = 0;
    }
    jajp1 = rowstr[1];
    for (j = 1; j <= nrows; j++) {
 nzrow = 0;
 for (k = jajp1; k < rowstr[j+1]; k++) {
            i = colidx[k];
            x[i] = x[i] + a[k];
            if ( mark[i] == 0 && x[i] != 0.0) {
  mark[i] = 1;
  nzrow = nzrow + 1;
  nzloc[nzrow] = i;
     }
 }
 for (k = 1; k <= nzrow; k++) {
            i = nzloc[k];
            mark[i] = 0;
            xi = x[i];
            x[i] = 0.0;
            if (xi != 0.0) {
  nza = nza + 1;
  a[nza] = xi;
  colidx[nza] = i;
     }
 }
 jajp1 = rowstr[j+1];
 rowstr[j+1] = nza + rowstr[1];
    }
}
static void sprnvc(
    int n,
    int nz,
    double v[],
    int iv[],
    int nzloc[],
    int mark[] )
{
    int nn1;
    int nzrow, nzv, ii, i;
    double vecelt, vecloc;
    nzv = 0;
    nzrow = 0;
    nn1 = 1;
    do {
 nn1 = 2 * nn1;
    } while (nn1 < n);
    while (nzv < nz) {
 vecelt = randlc(&tran, amult);
 vecloc = randlc(&tran, amult);
 i = icnvrt(vecloc, nn1) + 1;
 if (i > n) continue;
 if (mark[i] == 0) {
     mark[i] = 1;
     nzrow = nzrow + 1;
     nzloc[nzrow] = i;
     nzv = nzv + 1;
     v[nzv] = vecelt;
     iv[nzv] = i;
 }
    }
    for (ii = 1; ii <= nzrow; ii++) {
 i = nzloc[ii];
 mark[i] = 0;
    }
}
static int icnvrt(double x, int ipwr2) {
    return ((int)(ipwr2 * x));
}
static void vecset(
    int n,
    double v[],
    int iv[],
    int *nzv,
    int i,
    double val)
{
    int k;
    boolean set;
    set = 0;
    for (k = 1; k <= *nzv; k++) {
 if (iv[k] == i) {
            v[k] = val;
            set = 1;
 }
    }
    if (set == 0) {
 *nzv = *nzv + 1;
 v[*nzv] = val;
 iv[*nzv] = i;
    }
}
