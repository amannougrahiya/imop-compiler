unsigned short __builtin_bswap16 (unsigned short x) ;
unsigned int __builtin_bswap32 (unsigned int x) ;
unsigned long long __builtin_bswap64 (unsigned long long x) ;
extern float __builtin_inff();
extern double __builtin_inf();
extern long double __builtin_infl();
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
int atexit_b(void (^)(void)) __attribute__((visibility("default")));
void *bsearch_b(const void *, const void *, size_t,
     size_t, int (^)(const void *, const void *)) __attribute__((visibility("default")));
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
     int (^)(const void *, const void *)) __attribute__((visibility("default")));
int mergesort(void *, size_t, size_t,
     int (*)(const void *, const void *));
int mergesort_b(void *, size_t, size_t,
     int (^)(const void *, const void *)) __attribute__((visibility("default")));
void psort(void *, size_t, size_t,
     int (*)(const void *, const void *)) __attribute__((visibility("default")));
void psort_b(void *, size_t, size_t,
     int (^)(const void *, const void *)) __attribute__((visibility("default")));
void psort_r(void *, size_t, size_t, void *,
     int (*)(void *, const void *, const void *)) __attribute__((visibility("default")));
void qsort_b(void *, size_t, size_t,
     int (^)(const void *, const void *)) __attribute__((visibility("default")));
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
static int nx[11 +1], ny[11 +1], nz[11 +1];
static char Class;
static int debug_vec[8];
static int m1[11 +1], m2[11 +1], m3[11 +1];
static int lt, lb;
static int is1, is2, is3, ie1, ie2, ie3;
static void setup(int *n1, int *n2, int *n3, int lt);
static void mg3P(double ****u, double ***v, double ****r, double a[4],
   double c[4], int n1, int n2, int n3, int k);
static void psinv( double ***r, double ***u, int n1, int n2, int n3,
     double c[4], int k);
static void resid( double ***u, double ***v, double ***r,
     int n1, int n2, int n3, double a[4], int k );
static void rprj3( double ***r, int m1k, int m2k, int m3k,
     double ***s, int m1j, int m2j, int m3j, int k );
static void interp( double ***z, int mm1, int mm2, int mm3,
      double ***u, int n1, int n2, int n3, int k );
static void norm2u3(double ***r, int n1, int n2, int n3,
      double *rnm2, double *rnmu, int nx, int ny, int nz);
static void rep_nrm(double ***u, int n1, int n2, int n3,
      char *title, int kk);
static void comm3(double ***u, int n1, int n2, int n3, int kk);
static void zran3(double ***z, int n1, int n2, int n3, int nx, int ny, int k);
static void showall(double ***z, int n1, int n2, int n3);
static double power( double a, int n );
static void bubble( double ten[1037][2], int j1[1037][2], int j2[1037][2],
      int j3[1037][2], int m, int ind );
static void zero3(double ***z, int n1, int n2, int n3);
static void nonzero(double ***z, int n1, int n2, int n3);
int main(int argc, char *argv[]) {
    int k, it;
    double t, tinit, mflops;
    int nthreads = 1;
    double ****u, ***v, ****r;
    double a[4], c[4];
    double rnm2, rnmu;
    double epsilon = 1.0e-8;
    int n1, n2, n3, nit;
    double verify_value;
    boolean verified;
    int i, j, l;
    FILE *fp;
    timer_clear(1);
    timer_clear(2);
    timer_start(2);
    printf("\n\n NAS Parallel Benchmarks 2.3 OpenMP C version"
    " - MG Benchmark\n\n");
    fp = fopen("mg.input", "r");
    if (fp != ((void *)0)) {
 printf(" Reading from input file mg.input\n");
 fscanf(fp, "%d", &lt);
 while(fgetc(fp) != '\n');
 fscanf(fp, "%d%d%d", &nx[lt], &ny[lt], &nz[lt]);
 while(fgetc(fp) != '\n');
 fscanf(fp, "%d", &nit);
 while(fgetc(fp) != '\n');
 for (i = 0; i <= 7; i++) {
     fscanf(fp, "%d", &debug_vec[i]);
 }
 fclose(fp);
    } else {
 printf(" No input file. Using compiled defaults\n");
 lt = LT_DEFAULT;
 nit = NIT_DEFAULT;
 nx[lt] = NX_DEFAULT;
 ny[lt] = NY_DEFAULT;
 nz[lt] = NZ_DEFAULT;
 for (i = 0; i <= 7; i++) {
     debug_vec[i] = DEBUG_DEFAULT;
 }
    }
    if ( (nx[lt] != ny[lt]) || (nx[lt] != nz[lt]) ) {
 Class = 'U';
    } else if( nx[lt] == 32 && nit == 4 ) {
 Class = 'S';
    } else if( nx[lt] == 64 && nit == 40 ) {
 Class = 'W';
    } else if( nx[lt] == 256 && nit == 20 ) {
 Class = 'B';
    } else if( nx[lt] == 512 && nit == 20 ) {
 Class = 'C';
    } else if( nx[lt] == 256 && nit == 4 ) {
 Class = 'A';
    } else {
 Class = 'U';
    }
    a[0] = -8.0/3.0;
    a[1] = 0.0;
    a[2] = 1.0/6.0;
    a[3] = 1.0/12.0;
    if (Class == 'A' || Class == 'S' || Class =='W') {
 c[0] = -3.0/8.0;
 c[1] = 1.0/32.0;
 c[2] = -1.0/64.0;
 c[3] = 0.0;
    } else {
 c[0] = -3.0/17.0;
 c[1] = 1.0/33.0;
 c[2] = -1.0/61.0;
 c[3] = 0.0;
    }
    lb = 1;
    setup(&n1,&n2,&n3,lt);
    u = (double ****)malloc((lt+1)*sizeof(double ***));
    for (l = lt; l >=1; l--) {
 u[l] = (double ***)malloc(m3[l]*sizeof(double **));
 for (k = 0; k < m3[l]; k++) {
     u[l][k] = (double **)malloc(m2[l]*sizeof(double *));
     for (j = 0; j < m2[l]; j++) {
  u[l][k][j] = (double *)malloc(m1[l]*sizeof(double));
     }
 }
    }
    v = (double ***)malloc(m3[lt]*sizeof(double **));
    for (k = 0; k < m3[lt]; k++) {
 v[k] = (double **)malloc(m2[lt]*sizeof(double *));
 for (j = 0; j < m2[lt]; j++) {
     v[k][j] = (double *)malloc(m1[lt]*sizeof(double));
 }
    }
    r = (double ****)malloc((lt+1)*sizeof(double ***));
    for (l = lt; l >=1; l--) {
 r[l] = (double ***)malloc(m3[l]*sizeof(double **));
 for (k = 0; k < m3[l]; k++) {
     r[l][k] = (double **)malloc(m2[l]*sizeof(double *));
     for (j = 0; j < m2[l]; j++) {
  r[l][k][j] = (double *)malloc(m1[l]*sizeof(double));
     }
 }
    }
#pragma omp parallel
{
    zero3(u[lt],n1,n2,n3);
}
    zran3(v,n1,n2,n3,nx[lt],ny[lt],lt);
#pragma omp parallel
{
    norm2u3(v,n1,n2,n3,&rnm2,&rnmu,nx[lt],ny[lt],nz[lt]);
#pragma omp single
{
    printf(" Size: %3dx%3dx%3d (class %1c)\n",
    nx[lt], ny[lt], nz[lt], Class);
    printf(" Iterations: %3d\n", nit);
}
    resid(u[lt],v,r[lt],n1,n2,n3,a,lt);
    norm2u3(r[lt],n1,n2,n3,&rnm2,&rnmu,nx[lt],ny[lt],nz[lt]);
    mg3P(u,v,r,a,c,n1,n2,n3,lt);
    resid(u[lt],v,r[lt],n1,n2,n3,a,lt);
#pragma omp single
    setup(&n1,&n2,&n3,lt);
    zero3(u[lt],n1,n2,n3);
  }
    zran3(v,n1,n2,n3,nx[lt],ny[lt],lt);
    timer_stop(2);
    timer_start(1);
#pragma omp parallel firstprivate(nit) private(it)
  {
    resid(u[lt],v,r[lt],n1,n2,n3,a,lt);
    norm2u3(r[lt],n1,n2,n3,&rnm2,&rnmu,nx[lt],ny[lt],nz[lt]);
    for ( it = 1; it <= nit; it++) {
 mg3P(u,v,r,a,c,n1,n2,n3,lt);
 resid(u[lt],v,r[lt],n1,n2,n3,a,lt);
    }
    norm2u3(r[lt],n1,n2,n3,&rnm2,&rnmu,nx[lt],ny[lt],nz[lt]);
  }
    timer_stop(1);
    t = timer_read(1);
    tinit = timer_read(2);
    verified = 0;
    verify_value = 0.0;
    printf(" Initialization time: %15.3f seconds\n", tinit);
    printf(" Benchmark completed\n");
    if (Class != 'U') {
 if (Class == 'S') {
            verify_value = 0.530770700573e-04;
 } else if (Class == 'W') {
            verify_value = 0.250391406439e-17;
 } else if (Class == 'A') {
            verify_value = 0.2433365309e-5;
        } else if (Class == 'B') {
            verify_value = 0.180056440132e-5;
        } else if (Class == 'C') {
            verify_value = 0.570674826298e-06;
 }
 if ( fabs( rnm2 - verify_value ) <= epsilon ) {
            verified = 1;
     printf(" VERIFICATION SUCCESSFUL\n");
     printf(" L2 Norm is %20.12e\n", rnm2);
     printf(" Error is   %20.12e\n", rnm2 - verify_value);
 } else {
            verified = 0;
     printf(" VERIFICATION FAILED\n");
     printf(" L2 Norm is             %20.12e\n", rnm2);
     printf(" The correct L2 Norm is %20.12e\n", verify_value);
 }
    } else {
 verified = 0;
 printf(" Problem size unknown\n");
 printf(" NO VERIFICATION PERFORMED\n");
    }
    if ( t != 0.0 ) {
 int nn = nx[lt]*ny[lt]*nz[lt];
 mflops = 58.*nit*nn*1.0e-6 / t;
    } else {
 mflops = 0.0;
    }
    c_print_results("MG", Class, nx[lt], ny[lt], nz[lt],
      nit, nthreads, t, mflops, "          floating point",
      verified, "2.3", "16 May 2013",
      "gcc", "gcc", "(none)", "-I../common", "-O3 -lm -fopenmp ", "(none)", "randdp");
}
static void setup(int *n1, int *n2, int *n3, int lt) {
    int k;
    for ( k = lt-1; k >= 1; k--) {
 nx[k] = nx[k+1]/2;
 ny[k] = ny[k+1]/2;
 nz[k] = nz[k+1]/2;
    }
    for (k = 1; k <= lt; k++) {
 m1[k] = nx[k]+2;
 m2[k] = nz[k]+2;
 m3[k] = ny[k]+2;
    }
    is1 = 1;
    ie1 = nx[lt];
    *n1 = nx[lt]+2;
    is2 = 1;
    ie2 = ny[lt];
    *n2 = ny[lt]+2;
    is3 = 1;
    ie3 = nz[lt];
    *n3 = nz[lt]+2;
    if (debug_vec[1] >= 1 ) {
 printf(" in setup, \n");
 printf("  lt  nx  ny  nz  n1  n2  n3 is1 is2 is3 ie1 ie2 ie3\n");
 printf("%4d%4d%4d%4d%4d%4d%4d%4d%4d%4d%4d%4d%4d\n",
        lt,nx[lt],ny[lt],nz[lt],*n1,*n2,*n3,is1,is2,is3,ie1,ie2,ie3);
    }
}
static void mg3P(double ****u, double ***v, double ****r, double a[4],
   double c[4], int n1, int n2, int n3, int k) {
    int j;
    for (k = lt; k >= lb+1; k--) {
 j = k-1;
 rprj3(r[k], m1[k], m2[k], m3[k],
       r[j], m1[j], m2[j], m3[j], k);
    }
    k = lb;
    zero3(u[k], m1[k], m2[k], m3[k]);
    psinv(r[k], u[k], m1[k], m2[k], m3[k], c, k);
    for (k = lb+1; k <= lt-1; k++) {
 j = k-1;
 zero3(u[k], m1[k], m2[k], m3[k]);
 interp(u[j], m1[j], m2[j], m3[j],
        u[k], m1[k], m2[k], m3[k], k);
 resid(u[k], r[k], r[k], m1[k], m2[k], m3[k], a, k);
 psinv(r[k], u[k], m1[k], m2[k], m3[k], c, k);
    }
    j = lt - 1;
    k = lt;
    interp(u[j], m1[j], m2[j], m3[j], u[lt], n1, n2, n3, k);
    resid(u[lt], v, r[lt], n1, n2, n3, a, k);
    psinv(r[lt], u[lt], n1, n2, n3, c, k);
}
static void psinv( double ***r, double ***u, int n1, int n2, int n3,
     double c[4], int k) {
    int i3, i2, i1;
    double r1[1037], r2[1037];
#pragma omp for
    for (i3 = 1; i3 < n3-1; i3++) {
 for (i2 = 1; i2 < n2-1; i2++) {
            for (i1 = 0; i1 < n1; i1++) {
  r1[i1] = r[i3][i2-1][i1] + r[i3][i2+1][i1]
      + r[i3-1][i2][i1] + r[i3+1][i2][i1];
  r2[i1] = r[i3-1][i2-1][i1] + r[i3-1][i2+1][i1]
      + r[i3+1][i2-1][i1] + r[i3+1][i2+1][i1];
     }
            for (i1 = 1; i1 < n1-1; i1++) {
  u[i3][i2][i1] = u[i3][i2][i1]
      + c[0] * r[i3][i2][i1]
      + c[1] * ( r[i3][i2][i1-1] + r[i3][i2][i1+1]
          + r1[i1] )
      + c[2] * ( r2[i1] + r1[i1-1] + r1[i1+1] );
     }
 }
    }
    comm3(u,n1,n2,n3,k);
    if (debug_vec[0] >= 1 ) {
#pragma omp single
 rep_nrm(u,n1,n2,n3,"   psinv",k);
    }
    if ( debug_vec[3] >= k ) {
#pragma omp single
 showall(u,n1,n2,n3);
    }
}
static void resid( double ***u, double ***v, double ***r,
     int n1, int n2, int n3, double a[4], int k ) {
    int i3, i2, i1;
    double u1[1037], u2[1037];
#pragma omp for
    for (i3 = 1; i3 < n3-1; i3++) {
 for (i2 = 1; i2 < n2-1; i2++) {
            for (i1 = 0; i1 < n1; i1++) {
  u1[i1] = u[i3][i2-1][i1] + u[i3][i2+1][i1]
         + u[i3-1][i2][i1] + u[i3+1][i2][i1];
  u2[i1] = u[i3-1][i2-1][i1] + u[i3-1][i2+1][i1]
         + u[i3+1][i2-1][i1] + u[i3+1][i2+1][i1];
     }
     for (i1 = 1; i1 < n1-1; i1++) {
  r[i3][i2][i1] = v[i3][i2][i1]
      - a[0] * u[i3][i2][i1]
  - a[2] * ( u2[i1] + u1[i1-1] + u1[i1+1] )
        - a[3] * ( u2[i1-1] + u2[i1+1] );
     }
 }
    }
    comm3(r,n1,n2,n3,k);
    if (debug_vec[0] >= 1 ) {
#pragma omp single
 rep_nrm(r,n1,n2,n3,"   resid",k);
    }
    if ( debug_vec[2] >= k ) {
#pragma omp single
 showall(r,n1,n2,n3);
    }
}
static void rprj3( double ***r, int m1k, int m2k, int m3k,
     double ***s, int m1j, int m2j, int m3j, int k ) {
    int j3, j2, j1, i3, i2, i1, d1, d2, d3;
    double x1[1037], y1[1037], x2, y2;
    if (m1k == 3) {
        d1 = 2;
    } else {
        d1 = 1;
    }
    if (m2k == 3) {
        d2 = 2;
    } else {
        d2 = 1;
    }
    if (m3k == 3) {
        d3 = 2;
    } else {
        d3 = 1;
    }
#pragma omp for
    for (j3 = 1; j3 < m3j-1; j3++) {
 i3 = 2*j3-d3;
 for (j2 = 1; j2 < m2j-1; j2++) {
            i2 = 2*j2-d2;
            for (j1 = 1; j1 < m1j; j1++) {
  i1 = 2*j1-d1;
  x1[i1] = r[i3+1][i2][i1] + r[i3+1][i2+2][i1]
      + r[i3][i2+1][i1] + r[i3+2][i2+1][i1];
  y1[i1] = r[i3][i2][i1] + r[i3+2][i2][i1]
      + r[i3][i2+2][i1] + r[i3+2][i2+2][i1];
     }
            for (j1 = 1; j1 < m1j-1; j1++) {
  i1 = 2*j1-d1;
  y2 = r[i3][i2][i1+1] + r[i3+2][i2][i1+1]
      + r[i3][i2+2][i1+1] + r[i3+2][i2+2][i1+1];
  x2 = r[i3+1][i2][i1+1] + r[i3+1][i2+2][i1+1]
      + r[i3][i2+1][i1+1] + r[i3+2][i2+1][i1+1];
  s[j3][j2][j1] =
      0.5 * r[i3+1][i2+1][i1+1]
      + 0.25 * ( r[i3+1][i2+1][i1] + r[i3+1][i2+1][i1+2] + x2)
      + 0.125 * ( x1[i1] + x1[i1+2] + y2)
      + 0.0625 * ( y1[i1] + y1[i1+2] );
     }
 }
    }
    comm3(s,m1j,m2j,m3j,k-1);
    if (debug_vec[0] >= 1 ) {
#pragma omp single
 rep_nrm(s,m1j,m2j,m3j,"   rprj3",k-1);
    }
    if (debug_vec[4] >= k ) {
#pragma omp single
 showall(s,m1j,m2j,m3j);
    }
}
static void interp( double ***z, int mm1, int mm2, int mm3,
      double ***u, int n1, int n2, int n3, int k ) {
    int i3, i2, i1, d1, d2, d3, t1, t2, t3;
    double z1[1037], z2[1037], z3[1037];
    if ( n1 != 3 && n2 != 3 && n3 != 3 ) {
#pragma omp for
 for (i3 = 0; i3 < mm3-1; i3++) {
            for (i2 = 0; i2 < mm2-1; i2++) {
  for (i1 = 0; i1 < mm1; i1++) {
      z1[i1] = z[i3][i2+1][i1] + z[i3][i2][i1];
      z2[i1] = z[i3+1][i2][i1] + z[i3][i2][i1];
      z3[i1] = z[i3+1][i2+1][i1] + z[i3+1][i2][i1] + z1[i1];
  }
  for (i1 = 0; i1 < mm1-1; i1++) {
      u[2*i3][2*i2][2*i1] = u[2*i3][2*i2][2*i1]
   +z[i3][i2][i1];
      u[2*i3][2*i2][2*i1+1] = u[2*i3][2*i2][2*i1+1]
   +0.5*(z[i3][i2][i1+1]+z[i3][i2][i1]);
  }
  for (i1 = 0; i1 < mm1-1; i1++) {
      u[2*i3][2*i2+1][2*i1] = u[2*i3][2*i2+1][2*i1]
   +0.5 * z1[i1];
      u[2*i3][2*i2+1][2*i1+1] = u[2*i3][2*i2+1][2*i1+1]
   +0.25*( z1[i1] + z1[i1+1] );
  }
  for (i1 = 0; i1 < mm1-1; i1++) {
      u[2*i3+1][2*i2][2*i1] = u[2*i3+1][2*i2][2*i1]
   +0.5 * z2[i1];
      u[2*i3+1][2*i2][2*i1+1] = u[2*i3+1][2*i2][2*i1+1]
   +0.25*( z2[i1] + z2[i1+1] );
  }
  for (i1 = 0; i1 < mm1-1; i1++) {
      u[2*i3+1][2*i2+1][2*i1] = u[2*i3+1][2*i2+1][2*i1]
   +0.25* z3[i1];
      u[2*i3+1][2*i2+1][2*i1+1] = u[2*i3+1][2*i2+1][2*i1+1]
   +0.125*( z3[i1] + z3[i1+1] );
  }
     }
 }
    } else {
 if (n1 == 3) {
            d1 = 2;
            t1 = 1;
 } else {
            d1 = 1;
            t1 = 0;
 }
 if (n2 == 3) {
            d2 = 2;
            t2 = 1;
 } else {
            d2 = 1;
            t2 = 0;
 }
 if (n3 == 3) {
            d3 = 2;
            t3 = 1;
 } else {
            d3 = 1;
            t3 = 0;
 }
#pragma omp for
 for ( i3 = d3; i3 <= mm3-1; i3++) {
            for ( i2 = d2; i2 <= mm2-1; i2++) {
  for ( i1 = d1; i1 <= mm1-1; i1++) {
      u[2*i3-d3-1][2*i2-d2-1][2*i1-d1-1] =
   u[2*i3-d3-1][2*i2-d2-1][2*i1-d1-1]
   +z[i3-1][i2-1][i1-1];
  }
  for ( i1 = 1; i1 <= mm1-1; i1++) {
      u[2*i3-d3-1][2*i2-d2-1][2*i1-t1-1] =
   u[2*i3-d3-1][2*i2-d2-1][2*i1-t1-1]
   +0.5*(z[i3-1][i2-1][i1]+z[i3-1][i2-1][i1-1]);
  }
     }
            for ( i2 = 1; i2 <= mm2-1; i2++) {
  for ( i1 = d1; i1 <= mm1-1; i1++) {
      u[2*i3-d3-1][2*i2-t2-1][2*i1-d1-1] =
   u[2*i3-d3-1][2*i2-t2-1][2*i1-d1-1]
   +0.5*(z[i3-1][i2][i1-1]+z[i3-1][i2-1][i1-1]);
  }
  for ( i1 = 1; i1 <= mm1-1; i1++) {
      u[2*i3-d3-1][2*i2-t2-1][2*i1-t1-1] =
   u[2*i3-d3-1][2*i2-t2-1][2*i1-t1-1]
   +0.25*(z[i3-1][i2][i1]+z[i3-1][i2-1][i1]
          +z[i3-1][i2][i1-1]+z[i3-1][i2-1][i1-1]);
  }
     }
 }
#pragma omp for
 for ( i3 = 1; i3 <= mm3-1; i3++) {
            for ( i2 = d2; i2 <= mm2-1; i2++) {
  for ( i1 = d1; i1 <= mm1-1; i1++) {
      u[2*i3-t3-1][2*i2-d2-1][2*i1-d1-1] =
   u[2*i3-t3-1][2*i2-d2-1][2*i1-d1-1]
   +0.5*(z[i3][i2-1][i1-1]+z[i3-1][i2-1][i1-1]);
  }
  for ( i1 = 1; i1 <= mm1-1; i1++) {
      u[2*i3-t3-1][2*i2-d2-1][2*i1-t1-1] =
   u[2*i3-t3-1][2*i2-d2-1][2*i1-t1-1]
   +0.25*(z[i3][i2-1][i1]+z[i3][i2-1][i1-1]
          +z[i3-1][i2-1][i1]+z[i3-1][i2-1][i1-1]);
  }
     }
     for ( i2 = 1; i2 <= mm2-1; i2++) {
  for ( i1 = d1; i1 <= mm1-1; i1++) {
      u[2*i3-t3-1][2*i2-t2-1][2*i1-d1-1] =
   u[2*i3-t3-1][2*i2-t2-1][2*i1-d1-1]
   +0.25*(z[i3][i2][i1-1]+z[i3][i2-1][i1-1]
          +z[i3-1][i2][i1-1]+z[i3-1][i2-1][i1-1]);
  }
  for ( i1 = 1; i1 <= mm1-1; i1++) {
      u[2*i3-t3-1][2*i2-t2-1][2*i1-t1-1] =
   u[2*i3-t3-1][2*i2-t2-1][2*i1-t1-1]
   +0.125*(z[i3][i2][i1]+z[i3][i2-1][i1]
    +z[i3][i2][i1-1]+z[i3][i2-1][i1-1]
    +z[i3-1][i2][i1]+z[i3-1][i2-1][i1]
    +z[i3-1][i2][i1-1]+z[i3-1][i2-1][i1-1]);
  }
     }
 }
    }
#pragma omp single
  {
    if (debug_vec[0] >= 1 ) {
 rep_nrm(z,mm1,mm2,mm3,"z: inter",k-1);
 rep_nrm(u,n1,n2,n3,"u: inter",k);
    }
    if ( debug_vec[5] >= k ) {
 showall(z,mm1,mm2,mm3);
 showall(u,n1,n2,n3);
    }
  }
}
static void norm2u3(double ***r, int n1, int n2, int n3,
      double *rnm2, double *rnmu, int nx, int ny, int nz) {
    static double s = 0.0;
    double tmp;
    int i3, i2, i1, n;
    double p_s = 0.0, p_a = 0.0;
    n = nx*ny*nz;
#pragma omp for
    for (i3 = 1; i3 < n3-1; i3++) {
 for (i2 = 1; i2 < n2-1; i2++) {
            for (i1 = 1; i1 < n1-1; i1++) {
  p_s = p_s + r[i3][i2][i1] * r[i3][i2][i1];
  tmp = fabs(r[i3][i2][i1]);
  if (tmp > p_a) p_a = tmp;
     }
 }
    }
#pragma omp critical
    {
 s += p_s;
 if (p_a > *rnmu) *rnmu = p_a;
    }
#pragma omp barrier
#pragma omp single
    {
 *rnm2 = sqrt(s/(double)n);
 s = 0.0;
    }
}
static void rep_nrm(double ***u, int n1, int n2, int n3,
      char *title, int kk) {
    double rnm2, rnmu;
    norm2u3(u,n1,n2,n3,&rnm2,&rnmu,nx[kk],ny[kk],nz[kk]);
    printf(" Level%2d in %8s: norms =%21.14e%21.14e\n",
    kk, title, rnm2, rnmu);
}
static void comm3(double ***u, int n1, int n2, int n3, int kk) {
    int i1, i2, i3;
#pragma omp for
    for ( i3 = 1; i3 < n3-1; i3++) {
 for ( i2 = 1; i2 < n2-1; i2++) {
     u[i3][i2][n1-1] = u[i3][i2][1];
     u[i3][i2][0] = u[i3][i2][n1-2];
 }
    }
#pragma omp for
    for ( i3 = 1; i3 < n3-1; i3++) {
 for ( i1 = 0; i1 < n1; i1++) {
     u[i3][n2-1][i1] = u[i3][1][i1];
     u[i3][0][i1] = u[i3][n2-2][i1];
 }
    }
#pragma omp for
    for ( i2 = 0; i2 < n2; i2++) {
 for ( i1 = 0; i1 < n1; i1++) {
     u[n3-1][i2][i1] = u[1][i2][i1];
     u[0][i2][i1] = u[n3-2][i2][i1];
 }
    }
}
static void zran3(double ***z, int n1, int n2, int n3, int nx, int ny, int k) {
    int i0, m0, m1;
    int i1, i2, i3, d1, e1, e2, e3;
    double xx, x0, x1, a1, a2, ai;
    double ten[10][2], best;
    int i, j1[10][2], j2[10][2], j3[10][2];
    int jg[4][10][2];
    double rdummy;
    a1 = power( pow(5.0,13), nx );
    a2 = power( pow(5.0,13), nx*ny );
#pragma omp parallel
  {
    zero3(z,n1,n2,n3);
  }
    i = is1-1+nx*(is2-1+ny*(is3-1));
    ai = power( pow(5.0,13), i );
    d1 = ie1 - is1 + 1;
    e1 = ie1 - is1 + 2;
    e2 = ie2 - is2 + 2;
    e3 = ie3 - is3 + 2;
    x0 = 314159265.e0;
    rdummy = randlc( &x0, ai );
    for (i3 = 1; i3 < e3; i3++) {
 x1 = x0;
 for (i2 = 1; i2 < e2; i2++) {
            xx = x1;
            vranlc( d1, &xx, pow(5.0,13), &(z[i3][i2][0]));
            rdummy = randlc( &x1, a1 );
 }
 rdummy = randlc( &x0, a2 );
    }
    for (i = 0; i < 10; i++) {
 ten[i][1] = 0.0;
 j1[i][1] = 0;
 j2[i][1] = 0;
 j3[i][1] = 0;
 ten[i][0] = 1.0;
 j1[i][0] = 0;
 j2[i][0] = 0;
 j3[i][0] = 0;
    }
    for (i3 = 1; i3 < n3-1; i3++) {
 for (i2 = 1; i2 < n2-1; i2++) {
            for (i1 = 1; i1 < n1-1; i1++) {
  if ( z[i3][i2][i1] > ten[0][1] ) {
      ten[0][1] = z[i3][i2][i1];
      j1[0][1] = i1;
      j2[0][1] = i2;
      j3[0][1] = i3;
      bubble( ten, j1, j2, j3, 10, 1 );
  }
  if ( z[i3][i2][i1] < ten[0][0] ) {
      ten[0][0] = z[i3][i2][i1];
      j1[0][0] = i1;
      j2[0][0] = i2;
      j3[0][0] = i3;
      bubble( ten, j1, j2, j3, 10, 0 );
  }
     }
 }
    }
    i1 = 10 - 1;
    i0 = 10 - 1;
    for (i = 10 - 1 ; i >= 0; i--) {
 best = z[j3[i1][1]][j2[i1][1]][j1[i1][1]];
 if (best == z[j3[i1][1]][j2[i1][1]][j1[i1][1]]) {
            jg[0][i][1] = 0;
            jg[1][i][1] = is1 - 1 + j1[i1][1];
            jg[2][i][1] = is2 - 1 + j2[i1][1];
            jg[3][i][1] = is3 - 1 + j3[i1][1];
            i1 = i1-1;
 } else {
            jg[0][i][1] = 0;
            jg[1][i][1] = 0;
            jg[2][i][1] = 0;
            jg[3][i][1] = 0;
 }
 ten[i][1] = best;
 best = z[j3[i0][0]][j2[i0][0]][j1[i0][0]];
 if (best == z[j3[i0][0]][j2[i0][0]][j1[i0][0]]) {
            jg[0][i][0] = 0;
            jg[1][i][0] = is1 - 1 + j1[i0][0];
            jg[2][i][0] = is2 - 1 + j2[i0][0];
            jg[3][i][0] = is3 - 1 + j3[i0][0];
            i0 = i0-1;
 } else {
            jg[0][i][0] = 0;
            jg[1][i][0] = 0;
            jg[2][i][0] = 0;
            jg[3][i][0] = 0;
 }
 ten[i][0] = best;
    }
    m1 = i1+1;
    m0 = i0+1;
#pragma omp parallel for private(i2, i1)
    for (i3 = 0; i3 < n3; i3++) {
 for (i2 = 0; i2 < n2; i2++) {
            for (i1 = 0; i1 < n1; i1++) {
  z[i3][i2][i1] = 0.0;
     }
 }
    }
    for (i = 10 -1; i >= m0; i--) {
 z[j3[i][0]][j2[i][0]][j1[i][0]] = -1.0;
    }
    for (i = 10 -1; i >= m1; i--) {
 z[j3[i][1]][j2[i][1]][j1[i][1]] = 1.0;
    }
#pragma omp parallel
    comm3(z,n1,n2,n3,k);
}
static void showall(double ***z, int n1, int n2, int n3) {
    int i1,i2,i3;
    int m1, m2, m3;
    m1 = (((n1) < (18)) ? (n1) : (18));
    m2 = (((n2) < (14)) ? (n2) : (14));
    m3 = (((n3) < (18)) ? (n3) : (18));
    printf("\n");
    for (i3 = 0; i3 < m3; i3++) {
 for (i1 = 0; i1 < m1; i1++) {
     for (i2 = 0; i2 < m2; i2++) {
  printf("%6.3f", z[i3][i2][i1]);
     }
     printf("\n");
 }
 printf(" - - - - - - - \n");
    }
    printf("\n");
}
static double power( double a, int n ) {
    double aj;
    int nj;
    double rdummy;
    double power;
    power = 1.0;
    nj = n;
    aj = a;
    while (nj != 0) {
 if( (nj%2) == 1 ) rdummy = randlc( &power, aj );
 rdummy = randlc( &aj, aj );
 nj = nj/2;
    }
    return (power);
}
static void bubble( double ten[1037][2], int j1[1037][2], int j2[1037][2],
      int j3[1037][2], int m, int ind ) {
    double temp;
    int i, j_temp;
    if ( ind == 1 ) {
 for (i = 0; i < m-1; i++) {
            if ( ten[i][ind] > ten[i+1][ind] ) {
  temp = ten[i+1][ind];
  ten[i+1][ind] = ten[i][ind];
  ten[i][ind] = temp;
  j_temp = j1[i+1][ind];
  j1[i+1][ind] = j1[i][ind];
  j1[i][ind] = j_temp;
  j_temp = j2[i+1][ind];
  j2[i+1][ind] = j2[i][ind];
  j2[i][ind] = j_temp;
  j_temp = j3[i+1][ind];
  j3[i+1][ind] = j3[i][ind];
  j3[i][ind] = j_temp;
     } else {
  return;
     }
 }
    } else {
 for (i = 0; i < m-1; i++) {
            if ( ten[i][ind] < ten[i+1][ind] ) {
  temp = ten[i+1][ind];
  ten[i+1][ind] = ten[i][ind];
  ten[i][ind] = temp;
  j_temp = j1[i+1][ind];
  j1[i+1][ind] = j1[i][ind];
  j1[i][ind] = j_temp;
  j_temp = j2[i+1][ind];
  j2[i+1][ind] = j2[i][ind];
  j2[i][ind] = j_temp;
  j_temp = j3[i+1][ind];
  j3[i+1][ind] = j3[i][ind];
  j3[i][ind] = j_temp;
     } else {
  return;
     }
 }
    }
}
static void zero3(double ***z, int n1, int n2, int n3) {
    int i1, i2, i3;
#pragma omp for
    for (i3 = 0;i3 < n3; i3++) {
 for (i2 = 0; i2 < n2; i2++) {
            for (i1 = 0; i1 < n1; i1++) {
  z[i3][i2][i1] = 0.0;
     }
 }
    }
}
