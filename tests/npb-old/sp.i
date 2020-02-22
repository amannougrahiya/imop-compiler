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
static double u [5][PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1],
              us [PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1],
              vs [PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1],
              ws [PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1],
              qs [PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1],
              ainv [PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1],
              rho_i [PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1],
              speed [PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1],
              square [PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1],
              rhs [5][PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1],
              forcing [5][PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1],
              lhs [15][PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1][PROBLEM_SIZE/2*2+1];
static double cv[PROBLEM_SIZE], rhon[PROBLEM_SIZE],
              rhos[PROBLEM_SIZE], rhoq[PROBLEM_SIZE],
              cuf[PROBLEM_SIZE], q[PROBLEM_SIZE],
              ue[5][PROBLEM_SIZE], buf[5][PROBLEM_SIZE];
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
    printf("\n\n NAS Parallel Benchmarks 2.3 OpenMP C version"
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
        niter = 6;
        dt = DT_DEFAULT;
        grid_points[0] = PROBLEM_SIZE;
        grid_points[1] = PROBLEM_SIZE;
        grid_points[2] = PROBLEM_SIZE;
    }
    printf(" Size: %3dx%3dx%3d\n",
            grid_points[0], grid_points[1], grid_points[2]);
    printf(" Iterations: %3d   dt: %10.6f\n", niter, dt);
    if ( (grid_points[0] > PROBLEM_SIZE) ||
            (grid_points[1] > PROBLEM_SIZE) ||
            (grid_points[2] > PROBLEM_SIZE) ) {
        printf("%d, %d, %d\n", grid_points[0], grid_points[1], grid_points[2]);
        printf(" Problem size too big for compiled array sizes\n");
        exit(1);
    }
    set_constants();
    initialize();
    lhsinit();
    exact_rhs();
#pragma omp parallel
    {
        adi();
    }
    initialize();
    timer_clear(1);
    timer_start(1);
#pragma omp parallel private(step)
    {
        for (step = 1; step <= niter; step++) {
            if (step % 20 == 0 || step == 1) {
#pragma omp master
                printf(" Time step %4d\n", step);
            }
            adi();
        }
    }
    timer_stop(1);
    tmax = timer_read(1);
    verify(niter, &class, &verified);
    if (tmax != 0) {
        mflops = ( 881.174 * pow((double)PROBLEM_SIZE, 3.0)
                - 4683.91 * (((double)PROBLEM_SIZE)*((double)PROBLEM_SIZE))
                + 11484.5 * (double)PROBLEM_SIZE
                - 19272.4) * (double)niter / (tmax*1000000.0);
    } else {
        mflops = 0.0;
    }
    c_print_results("SP", class, grid_points[0],
            grid_points[1], grid_points[2], niter, nthreads,
            tmax, mflops, "          floating point",
            verified, "2.3", "16 May 2013", "gcc", "gcc", "(none)", "-I../common", "-O3 -lm -fopenmp ",
            "(none)", "(none)");
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
    for (i = 0; i <= PROBLEM_SIZE-1; i++) {
        for (j = 0; j <= PROBLEM_SIZE-1; j++) {
            for (k = 0; k <= PROBLEM_SIZE-1; k++) {
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
#pragma omp for
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
#pragma omp for
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
#pragma omp for
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
    ninvr();
}
static void y_solve(void) {
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
    pinvr();
}
static void z_solve(void) {
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
    tzetar();
}
