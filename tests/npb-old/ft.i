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
union stUn_imopVarPre0 {
    char __mbstate8[128];
    long long _mbstateL;
} ;
typedef union stUn_imopVarPre0 __mbstate_t;
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
    void ( *__routine )(void *);
    void *__arg;
    struct __darwin_pthread_handler_rec *__next;
} ;
struct _opaque_pthread_attr_t {
    long __sig;
    char __opaque[56];
} ;
struct _opaque_pthread_cond_t {
    long __sig;
    char __opaque[40];
} ;
struct _opaque_pthread_condattr_t {
    long __sig;
    char __opaque[8];
} ;
struct _opaque_pthread_mutex_t {
    long __sig;
    char __opaque[56];
} ;
struct _opaque_pthread_mutexattr_t {
    long __sig;
    char __opaque[8];
} ;
struct _opaque_pthread_once_t {
    long __sig;
    char __opaque[8];
} ;
struct _opaque_pthread_rwlock_t {
    long __sig;
    char __opaque[192];
} ;
struct _opaque_pthread_rwlockattr_t {
    long __sig;
    char __opaque[16];
} ;
struct _opaque_pthread_t {
    long __sig;
    struct __darwin_pthread_handler_rec *__cleanup_stack;
    char __opaque[8176];
} ;
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
int renameat(int , const char * , int , const char *);
int renamex_np(const char *, const char * , unsigned int );
int renameatx_np(int , const char * , int , const char * , unsigned int );
typedef __darwin_off_t fpos_t;
struct __sbuf {
    unsigned char *_base;
    int _size;
} ;
struct __sFILEX ;
struct __sFILE {
    unsigned char *_p;
    int _r;
    int _w;
    short _flags;
    short _file;
    struct __sbuf _bf;
    int _lbfsize;
    void *_cookie;
    int ( *_close )(void *);
    int ( *_read )(void *, char * , int );
    fpos_t ( *_seek )(void *, fpos_t , int );
    int ( *_write )(void *, const char * , int );
    struct __sbuf _ub;
    struct __sFILEX *_extra;
    int _ur;
    unsigned char _ubuf[3];
    unsigned char _nbuf[1];
    struct __sbuf _lb;
    int _blksize;
    fpos_t _offset;
} ;
typedef struct __sFILE FILE;
extern FILE *__stdinp;
extern FILE *__stdoutp;
extern FILE *__stderrp;
void clearerr(FILE *);
int fclose(FILE *);
int feof(FILE *);
int ferror(FILE *);
int fflush(FILE *);
int fgetc(FILE *);
int fgetpos(FILE *restrict , fpos_t *);
char *fgets(char *restrict , int , FILE *);
FILE *fopen(const char *restrict __filename, const char *restrict __mode);
int fprintf(FILE *restrict , const char *restrict , ...);
int fputc(int , FILE *);
int fputs(const char *restrict , FILE *restrict );
size_t fread(void *restrict __ptr, size_t __size , size_t __nitems , FILE *restrict __stream);
FILE *freopen(const char *restrict , const char *restrict , FILE *restrict );
int fscanf(FILE *restrict , const char *restrict , ...);
int fseek(FILE *, long , int );
int fsetpos(FILE *, const fpos_t *);
long ftell(FILE *);
size_t fwrite(const void *restrict __ptr, size_t __size , size_t __nitems , FILE *restrict __stream);
int getc(FILE *);
int getchar(void );
char *gets(char *);
void perror(const char *);
int printf(const char *restrict , ...);
int putc(int , FILE *);
int putchar(int );
int puts(const char *);
int remove(const char *);
int rename(const char *__old, const char *__new);
void rewind(FILE *);
int scanf(const char *restrict , ...);
void setbuf(FILE *restrict , char *restrict );
int setvbuf(FILE *restrict , char *restrict , int , size_t );
int sprintf(char *restrict , const char *restrict , ...);
int sscanf(const char *restrict , const char *restrict , ...);
FILE *tmpfile(void );
char *tmpnam(char *);
int ungetc(int , FILE *);
int vfprintf(FILE *restrict , const char *restrict , va_list );
int vprintf(const char *restrict , va_list );
int vsprintf(char *restrict , const char *restrict , va_list );
char *ctermid(char *);
FILE *fdopen(int , const char *);
int fileno(FILE *);
int pclose(FILE *);
FILE *popen(const char *, const char *);
int __srget(FILE *);
int __svfscanf(FILE *, const char * , va_list );
int __swbuf(int , FILE *);
extern __inline int __sputc(int _c, FILE *_p) {
    int _imopVarPre12;
    int _imopVarPre16;
    _imopVarPre12 = --_p->_w >= 0;
    if (!_imopVarPre12) {
        _imopVarPre16 = _p->_w >= _p->_lbfsize;
        if (_imopVarPre16) {
            _imopVarPre16 = (char) _c != '\n';
        }
        _imopVarPre12 = _imopVarPre16;
    }
    if (_imopVarPre12) {
        return (*_p->_p++ = _c);
    } else {
        int _imopVarPre18;
        _imopVarPre18 = __swbuf(_c, _p);
        return _imopVarPre18;
    }
}
void flockfile(FILE *);
int ftrylockfile(FILE *);
void funlockfile(FILE *);
int getc_unlocked(FILE *);
int getchar_unlocked(void );
int putc_unlocked(int , FILE *);
int putchar_unlocked(int );
int getw(FILE *);
int putw(int , FILE *);
char *tempnam(const char *__dir, const char *__prefix);
typedef __darwin_off_t off_t;
int fseeko(FILE *__stream, off_t __offset , int __whence);
off_t ftello(FILE *__stream);
int snprintf(char *restrict __str, size_t __size , const char *restrict __format, ...);
int vfscanf(FILE *restrict __stream, const char *restrict __format , va_list );
int vscanf(const char *restrict __format, va_list );
int vsnprintf(char *restrict __str, size_t __size , const char *restrict __format , va_list );
int vsscanf(const char *restrict __str, const char *restrict __format , va_list );
typedef __darwin_ssize_t ssize_t;
int dprintf(int , const char *restrict , ...);
int vdprintf(int , const char *restrict , va_list );
ssize_t getdelim(char **restrict __linep, size_t *restrict __linecapp , int __delimiter , FILE *restrict __stream);
ssize_t getline(char **restrict __linep, size_t *restrict __linecapp , FILE *restrict __stream);
extern const int sys_nerr;
extern const char *const sys_errlist[];
int asprintf(char **restrict , const char *restrict , ...);
char *ctermid_r(char *);
char *fgetln(FILE *, size_t *);
const char *fmtcheck(const char *, const char *);
int fpurge(FILE *);
void setbuffer(FILE *, char * , int );
int setlinebuf(FILE *);
int vasprintf(char **restrict , const char *restrict , va_list );
FILE *zopen(const char *, const char * , int );
FILE *funopen(const void *, int (*)(void *, char * , int ) , int (*)(void *, const char * , int ) , fpos_t (*)(void *, fpos_t , int ) , int (*)(void *));
extern int __sprintf_chk(char *restrict , int , size_t , const char *restrict , ...);
extern int __snprintf_chk(char *restrict , size_t , int , size_t , const char *restrict , ...);
extern int __vsprintf_chk(char *restrict , int , size_t , const char *restrict , va_list );
extern int __vsnprintf_chk(char *restrict , size_t , int , size_t , const char *restrict , va_list );
enum enum_imopVarPre1 {
    P_ALL, P_PID , P_PGID
} ;
typedef enum enum_imopVarPre1 idtype_t;
typedef __darwin_pid_t pid_t;
typedef __darwin_id_t id_t;
typedef int sig_atomic_t;
struct __darwin_i386_thread_state {
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
} ;
struct __darwin_fp_control {
    unsigned short __invalid: 1, __denorm: 1 , __zdiv: 1 , __ovrfl: 1 , __undfl: 1 , __precis: 1 , :2 , __pc: 2 , __rc: 2 , :1 , :3;
} ;
typedef struct __darwin_fp_control __darwin_fp_control_t;
struct __darwin_fp_status {
    unsigned short __invalid: 1, __denorm: 1 , __zdiv: 1 , __ovrfl: 1 , __undfl: 1 , __precis: 1 , __stkflt: 1 , __errsumm: 1 , __c0: 1 , __c1: 1 , __c2: 1 , __tos: 3 , __c3: 1 , __busy: 1;
} ;
typedef struct __darwin_fp_status __darwin_fp_status_t;
struct __darwin_mmst_reg {
    char __mmst_reg[10];
    char __mmst_rsrv[6];
} ;
struct __darwin_xmm_reg {
    char __xmm_reg[16];
} ;
struct __darwin_i386_float_state {
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
    char __fpu_rsrv4[14 * 16];
    int __fpu_reserved1;
} ;
struct __darwin_i386_avx_state {
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
    char __fpu_rsrv4[14 * 16];
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
} ;
struct __darwin_i386_exception_state {
    __uint16_t __trapno;
    __uint16_t __cpu;
    __uint32_t __err;
    __uint32_t __faultvaddr;
} ;
struct __darwin_x86_debug_state32 {
    unsigned int __dr0;
    unsigned int __dr1;
    unsigned int __dr2;
    unsigned int __dr3;
    unsigned int __dr4;
    unsigned int __dr5;
    unsigned int __dr6;
    unsigned int __dr7;
} ;
struct __darwin_x86_thread_state64 {
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
} ;
struct __darwin_x86_float_state64 {
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
    char __fpu_rsrv4[6 * 16];
    int __fpu_reserved1;
} ;
struct __darwin_x86_avx_state64 {
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
    char __fpu_rsrv4[6 * 16];
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
} ;
struct __darwin_x86_exception_state64 {
    __uint16_t __trapno;
    __uint16_t __cpu;
    __uint32_t __err;
    __uint64_t __faultvaddr;
} ;
struct __darwin_x86_debug_state64 {
    __uint64_t __dr0;
    __uint64_t __dr1;
    __uint64_t __dr2;
    __uint64_t __dr3;
    __uint64_t __dr4;
    __uint64_t __dr5;
    __uint64_t __dr6;
    __uint64_t __dr7;
} ;
struct __darwin_mcontext32 {
    struct __darwin_i386_exception_state __es;
    struct __darwin_i386_thread_state __ss;
    struct __darwin_i386_float_state __fs;
} ;
struct __darwin_mcontext_avx32 {
    struct __darwin_i386_exception_state __es;
    struct __darwin_i386_thread_state __ss;
    struct __darwin_i386_avx_state __fs;
} ;
struct __darwin_mcontext64 {
    struct __darwin_x86_exception_state64 __es;
    struct __darwin_x86_thread_state64 __ss;
    struct __darwin_x86_float_state64 __fs;
} ;
struct __darwin_mcontext_avx64 {
    struct __darwin_x86_exception_state64 __es;
    struct __darwin_x86_thread_state64 __ss;
    struct __darwin_x86_avx_state64 __fs;
} ;
typedef struct __darwin_mcontext64 *mcontext_t;
typedef __darwin_pthread_attr_t pthread_attr_t;
struct __darwin_sigaltstack {
    void *ss_sp;
    __darwin_size_t ss_size;
    int ss_flags;
} ;
typedef struct __darwin_sigaltstack stack_t;
struct __darwin_ucontext {
    int uc_onstack;
    __darwin_sigset_t uc_sigmask;
    struct __darwin_sigaltstack uc_stack;
    struct __darwin_ucontext *uc_link;
    __darwin_size_t uc_mcsize;
    struct __darwin_mcontext64 *uc_mcontext;
} ;
typedef struct __darwin_ucontext ucontext_t;
typedef __darwin_sigset_t sigset_t;
typedef __darwin_uid_t uid_t;
union sigval {
    int sival_int;
    void *sival_ptr;
} ;
struct sigevent {
    int sigev_notify;
    int sigev_signo;
    union sigval sigev_value;
    void ( *sigev_notify_function )(union sigval );
    pthread_attr_t *sigev_notify_attributes;
} ;
struct __siginfo {
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
} ;
typedef struct __siginfo siginfo_t;
union __sigaction_u {
    void ( *__sa_handler )(int );
    void ( *__sa_sigaction )(int , struct __siginfo * , void *);
} ;
struct __sigaction {
    union __sigaction_u __sigaction_u;
    void ( *sa_tramp )(void *, int , int , siginfo_t * , void *);
    sigset_t sa_mask;
    int sa_flags;
} ;
struct sigaction {
    union __sigaction_u __sigaction_u;
    sigset_t sa_mask;
    int sa_flags;
} ;
typedef void ( *sig_t )(int );
struct sigvec {
    void ( *sv_handler )(int );
    int sv_mask;
    int sv_flags;
} ;
struct sigstack {
    char *ss_sp;
    int ss_onstack;
} ;
void ( *signal(int , void (*)(int )) )(int );
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
struct timeval {
    __darwin_time_t tv_sec;
    __darwin_suseconds_t tv_usec;
} ;
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
} ;
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
} ;
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
} ;
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
} ;
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
} ;
typedef struct rusage_info_v3 rusage_info_current;
struct rlimit {
    rlim_t rlim_cur;
    rlim_t rlim_max;
} ;
struct proc_rlimit_control_wakeupmon {
    uint32_t wm_flags;
    int32_t wm_rate;
} ;
int getpriority(int , id_t );
int getiopolicy_np(int , int );
int getrlimit(int , struct rlimit *);
int getrusage(int , struct rusage *);
int setpriority(int , id_t , int );
int setiopolicy_np(int , int , int );
int setrlimit(int , const struct rlimit *);
static inline __uint16_t _OSSwapInt16(__uint16_t _data) {
    return ((__uint16_t) ((_data << 8) | (_data >> 8)));
}
static inline __uint32_t _OSSwapInt32(__uint32_t _data) {
    ;
    return _data;
}
static inline __uint64_t _OSSwapInt64(__uint64_t _data) {
    ;
    return _data;
}
union wait {
    int w_status;
    struct stUn_imopVarPre2 {
        unsigned int w_Termsig: 7, w_Coredump: 1 , w_Retcode: 8 , w_Filler: 16;
    } w_T;
    struct stUn_imopVarPre3 {
        unsigned int w_Stopval: 8, w_Stopsig: 8 , w_Filler: 16;
    } w_S;
} ;
pid_t wait(int *);
pid_t waitpid(pid_t , int * , int );
int waitid(idtype_t , id_t , siginfo_t * , int );
pid_t wait3(int *, int , struct rusage *);
pid_t wait4(pid_t , int * , int , struct rusage *);
void *alloca(size_t );
typedef __darwin_ct_rune_t ct_rune_t;
typedef __darwin_rune_t rune_t;
typedef __darwin_wchar_t wchar_t;
struct stUn_imopVarPre4 {
    int quot;
    int rem;
} ;
typedef struct stUn_imopVarPre4 div_t;
struct stUn_imopVarPre5 {
    long quot;
    long rem;
} ;
typedef struct stUn_imopVarPre5 ldiv_t;
struct stUn_imopVarPre6 {
    long long quot;
    long long rem;
} ;
typedef struct stUn_imopVarPre6 lldiv_t;
extern int __mb_cur_max;
void abort(void );
int abs(int );
int atexit(void (*)(void ));
double atof(const char *);
int atoi(const char *);
long atol(const char *);
long long atoll(const char *);
void *bsearch(const void *__key, const void *__base , size_t __nel , size_t __width , int ( *__compar )(const void *, const void *));
void *calloc(size_t __count, size_t __size);
div_t div(int , int );
void exit(int );
void free(void *);
char *getenv(const char *);
long labs(long );
ldiv_t ldiv(long , long );
long long llabs(long long );
lldiv_t lldiv(long long , long long );
void *malloc(size_t __size);
int mblen(const char *__s, size_t __n);
size_t mbstowcs(wchar_t *restrict , const char *restrict , size_t );
int mbtowc(wchar_t *restrict , const char *restrict , size_t );
int posix_memalign(void **__memptr, size_t __alignment , size_t __size);
void qsort(void *__base, size_t __nel , size_t __width , int ( *__compar )(const void *, const void *));
int rand(void );
void *realloc(void *__ptr, size_t __size);
void srand(unsigned );
double strtod(const char *, char **);
float strtof(const char *, char **);
long strtol(const char *__str, char **__endptr , int __base);
long double strtold(const char *, char **);
long long strtoll(const char *__str, char **__endptr , int __base);
unsigned long strtoul(const char *__str, char **__endptr , int __base);
unsigned long long strtoull(const char *__str, char **__endptr , int __base);
int system(const char *);
size_t wcstombs(char *restrict , const wchar_t *restrict , size_t );
int wctomb(char *, wchar_t );
void _Exit(int );
long a64l(const char *);
double drand48(void );
char *ecvt(double , int , int *restrict , int *restrict );
double erand48(unsigned short [3]);
char *fcvt(double , int , int *restrict , int *restrict );
char *gcvt(double , int , char *);
int getsubopt(char **, char *const * , char **);
int grantpt(int );
char *initstate(unsigned , char * , size_t );
long jrand48(unsigned short [3]);
char *l64a(long );
void lcong48(unsigned short [7]);
long lrand48(void );
char *mktemp(char *);
int mkstemp(char *);
long mrand48(void );
long nrand48(unsigned short [3]);
int posix_openpt(int );
char *ptsname(int );
int putenv(char *);
long random(void );
int rand_r(unsigned *);
char *realpath(const char *restrict , char *restrict );
unsigned short *seed48(unsigned short [3]);
int setenv(const char *__name, const char *__value , int __overwrite);
void setkey(const char *);
char *setstate(const char *);
void srand48(long );
void srandom(unsigned );
int unlockpt(int );
int unsetenv(const char *);
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
uint32_t arc4random(void );
void arc4random_addrandom(unsigned char *, int );
void arc4random_buf(void *__buf, size_t __nbytes);
void arc4random_stir(void );
uint32_t arc4random_uniform(uint32_t __upper_bound);
char *cgetcap(char *, const char * , int );
int cgetclose(void );
int cgetent(char **, char ** , const char *);
int cgetfirst(char **, char **);
int cgetmatch(const char *, const char *);
int cgetnext(char **, char **);
int cgetnum(char *, const char * , long *);
int cgetset(const char *);
int cgetstr(char *, const char * , char **);
int cgetustr(char *, const char * , char **);
int daemon(int , int );
char *devname(dev_t , mode_t );
char *devname_r(dev_t , mode_t , char *buf , int len);
char *getbsize(int *, long *);
int getloadavg(double [], int );
const char *getprogname(void );
int heapsort(void *__base, size_t __nel , size_t __width , int ( *__compar )(const void *, const void *));
int mergesort(void *__base, size_t __nel , size_t __width , int ( *__compar )(const void *, const void *));
void psort(void *__base, size_t __nel , size_t __width , int ( *__compar )(const void *, const void *));
void psort_r(void *__base, size_t __nel , size_t __width , void * , int ( *__compar )(void *, const void * , const void *));
void qsort_r(void *__base, size_t __nel , size_t __width , void * , int ( *__compar )(void *, const void * , const void *));
int radixsort(const unsigned char **__base, int __nel , const unsigned char *__table , unsigned __endbyte);
void setprogname(const char *);
int sradixsort(const unsigned char **__base, int __nel , const unsigned char *__table , unsigned __endbyte);
void sranddev(void );
void srandomdev(void );
void *reallocf(void *__ptr, size_t __size);
long long strtoq(const char *__str, char **__endptr , int __base);
unsigned long long strtouq(const char *__str, char **__endptr , int __base);
extern char *suboptarg;
void *valloc(size_t );
typedef float float_t;
typedef double double_t;
extern int __math_errhandling(void );
extern int __fpclassifyf(float );
extern int __fpclassifyd(double );
extern int __fpclassifyl(long double );
extern __inline int __inline_isfinitef(float );
extern __inline int __inline_isfinited(double );
extern __inline int __inline_isfinitel(long double );
extern __inline int __inline_isinff(float );
extern __inline int __inline_isinfd(double );
extern __inline int __inline_isinfl(long double );
extern __inline int __inline_isnanf(float );
extern __inline int __inline_isnand(double );
extern __inline int __inline_isnanl(long double );
extern __inline int __inline_isnormalf(float );
extern __inline int __inline_isnormald(double );
extern __inline int __inline_isnormall(long double );
extern __inline int __inline_signbitf(float );
extern __inline int __inline_signbitd(double );
extern __inline int __inline_signbitl(long double );
extern __inline int __inline_isfinitef(float __x) {
    int _imopVarPre97;
    float _imopVarPre98;
    float _imopVarPre99;
    _imopVarPre97 = __x == __x;
    if (_imopVarPre97) {
        _imopVarPre98 = __builtin_fabsf(__x);
        _imopVarPre99 = __builtin_inff();
        _imopVarPre97 = _imopVarPre98 != _imopVarPre99;
    }
    return _imopVarPre97;
}
extern __inline int __inline_isfinited(double __x) {
    int _imopVarPre103;
    double _imopVarPre104;
    double _imopVarPre105;
    _imopVarPre103 = __x == __x;
    if (_imopVarPre103) {
        _imopVarPre104 = __builtin_fabs(__x);
        _imopVarPre105 = __builtin_inf();
        _imopVarPre103 = _imopVarPre104 != _imopVarPre105;
    }
    return _imopVarPre103;
}
extern __inline int __inline_isfinitel(long double __x) {
    int _imopVarPre109;
    long double _imopVarPre110;
    long double _imopVarPre111;
    _imopVarPre109 = __x == __x;
    if (_imopVarPre109) {
        _imopVarPre110 = __builtin_fabsl(__x);
        _imopVarPre111 = __builtin_infl();
        _imopVarPre109 = _imopVarPre110 != _imopVarPre111;
    }
    return _imopVarPre109;
}
extern __inline int __inline_isinff(float __x) {
    float _imopVarPre114;
    float _imopVarPre115;
    _imopVarPre114 = __builtin_fabsf(__x);
    _imopVarPre115 = __builtin_inff();
    return _imopVarPre114 == _imopVarPre115;
}
extern __inline int __inline_isinfd(double __x) {
    double _imopVarPre118;
    double _imopVarPre119;
    _imopVarPre118 = __builtin_fabs(__x);
    _imopVarPre119 = __builtin_inf();
    return _imopVarPre118 == _imopVarPre119;
}
extern __inline int __inline_isinfl(long double __x) {
    long double _imopVarPre122;
    long double _imopVarPre123;
    _imopVarPre122 = __builtin_fabsl(__x);
    _imopVarPre123 = __builtin_infl();
    return _imopVarPre122 == _imopVarPre123;
}
extern __inline int __inline_isnanf(float __x) {
    return __x != __x;
}
extern __inline int __inline_isnand(double __x) {
    return __x != __x;
}
extern __inline int __inline_isnanl(long double __x) {
    return __x != __x;
}
extern __inline int __inline_signbitf(float __x) {
    union stUn_imopVarPre7 {
        float __f;
        unsigned int __u;
    } ;
    union stUn_imopVarPre7 __u;
    __u.__f = __x;
    return (int) (__u.__u >> 31);
}
extern __inline int __inline_signbitd(double __x) {
    union stUn_imopVarPre8 {
        double __f;
        unsigned long long __u;
    } ;
    union stUn_imopVarPre8 __u;
    __u.__f = __x;
    return (int) (__u.__u >> 63);
}
extern __inline int __inline_signbitl(long double __x) {
    union stUn_imopVarPre10 {
        long double __ld;
        struct stUn_imopVarPre9 {
            unsigned long long __m;
            unsigned short __sexp;
        } __p;
    } ;
    union stUn_imopVarPre10 __u;
    __u.__ld = __x;
    return (int) (__u.__p.__sexp >> 15);
}
extern __inline int __inline_isnormalf(float __x) {
    int _imopVarPre126;
    float _imopVarPre127;
    _imopVarPre126 = __inline_isfinitef(__x);
    if (_imopVarPre126) {
        _imopVarPre127 = __builtin_fabsf(__x);
        _imopVarPre126 = _imopVarPre127 >= 1.17549435082228750797e-38F;
    }
    return _imopVarPre126;
}
extern __inline int __inline_isnormald(double __x) {
    int _imopVarPre130;
    double _imopVarPre131;
    _imopVarPre130 = __inline_isfinited(__x);
    if (_imopVarPre130) {
        _imopVarPre131 = __builtin_fabs(__x);
        _imopVarPre130 = _imopVarPre131 >= ((double) 2.22507385850720138309e-308L);
    }
    return _imopVarPre130;
}
extern __inline int __inline_isnormall(long double __x) {
    int _imopVarPre134;
    long double _imopVarPre135;
    _imopVarPre134 = __inline_isfinitel(__x);
    if (_imopVarPre134) {
        _imopVarPre135 = __builtin_fabsl(__x);
        _imopVarPre134 = _imopVarPre135 >= 3.36210314311209350626e-4932L;
    }
    return _imopVarPre134;
}
extern float acosf(float );
extern double acos(double );
extern long double acosl(long double );
extern float asinf(float );
extern double asin(double );
extern long double asinl(long double );
extern float atanf(float );
extern double atan(double );
extern long double atanl(long double );
extern float atan2f(float , float );
extern double atan2(double , double );
extern long double atan2l(long double , long double );
extern float cosf(float );
extern double cos(double );
extern long double cosl(long double );
extern float sinf(float );
extern double sin(double );
extern long double sinl(long double );
extern float tanf(float );
extern double tan(double );
extern long double tanl(long double );
extern float acoshf(float );
extern double acosh(double );
extern long double acoshl(long double );
extern float asinhf(float );
extern double asinh(double );
extern long double asinhl(long double );
extern float atanhf(float );
extern double atanh(double );
extern long double atanhl(long double );
extern float coshf(float );
extern double cosh(double );
extern long double coshl(long double );
extern float sinhf(float );
extern double sinh(double );
extern long double sinhl(long double );
extern float tanhf(float );
extern double tanh(double );
extern long double tanhl(long double );
extern float expf(float );
extern double exp(double );
extern long double expl(long double );
extern float exp2f(float );
extern double exp2(double );
extern long double exp2l(long double );
extern float expm1f(float );
extern double expm1(double );
extern long double expm1l(long double );
extern float logf(float );
extern double log(double );
extern long double logl(long double );
extern float log10f(float );
extern double log10(double );
extern long double log10l(long double );
extern float log2f(float );
extern double log2(double );
extern long double log2l(long double );
extern float log1pf(float );
extern double log1p(double );
extern long double log1pl(long double );
extern float logbf(float );
extern double logb(double );
extern long double logbl(long double );
extern float modff(float , float *);
extern double modf(double , double *);
extern long double modfl(long double , long double *);
extern float ldexpf(float , int );
extern double ldexp(double , int );
extern long double ldexpl(long double , int );
extern float frexpf(float , int *);
extern double frexp(double , int *);
extern long double frexpl(long double , int *);
extern int ilogbf(float );
extern int ilogb(double );
extern int ilogbl(long double );
extern float scalbnf(float , int );
extern double scalbn(double , int );
extern long double scalbnl(long double , int );
extern float scalblnf(float , long int );
extern double scalbln(double , long int );
extern long double scalblnl(long double , long int );
extern float fabsf(float );
extern double fabs(double );
extern long double fabsl(long double );
extern float cbrtf(float );
extern double cbrt(double );
extern long double cbrtl(long double );
extern float hypotf(float , float );
extern double hypot(double , double );
extern long double hypotl(long double , long double );
extern float powf(float , float );
extern double pow(double , double );
extern long double powl(long double , long double );
extern float sqrtf(float );
extern double sqrt(double );
extern long double sqrtl(long double );
extern float erff(float );
extern double erf(double );
extern long double erfl(long double );
extern float erfcf(float );
extern double erfc(double );
extern long double erfcl(long double );
extern float lgammaf(float );
extern double lgamma(double );
extern long double lgammal(long double );
extern float tgammaf(float );
extern double tgamma(double );
extern long double tgammal(long double );
extern float ceilf(float );
extern double ceil(double );
extern long double ceill(long double );
extern float floorf(float );
extern double floor(double );
extern long double floorl(long double );
extern float nearbyintf(float );
extern double nearbyint(double );
extern long double nearbyintl(long double );
extern float rintf(float );
extern double rint(double );
extern long double rintl(long double );
extern long int lrintf(float );
extern long int lrint(double );
extern long int lrintl(long double );
extern float roundf(float );
extern double round(double );
extern long double roundl(long double );
extern long int lroundf(float );
extern long int lround(double );
extern long int lroundl(long double );
extern long long int llrintf(float );
extern long long int llrint(double );
extern long long int llrintl(long double );
extern long long int llroundf(float );
extern long long int llround(double );
extern long long int llroundl(long double );
extern float truncf(float );
extern double trunc(double );
extern long double truncl(long double );
extern float fmodf(float , float );
extern double fmod(double , double );
extern long double fmodl(long double , long double );
extern float remainderf(float , float );
extern double remainder(double , double );
extern long double remainderl(long double , long double );
extern float remquof(float , float , int *);
extern double remquo(double , double , int *);
extern long double remquol(long double , long double , int *);
extern float copysignf(float , float );
extern double copysign(double , double );
extern long double copysignl(long double , long double );
extern float nanf(const char *);
extern double nan(const char *);
extern long double nanl(const char *);
extern float nextafterf(float , float );
extern double nextafter(double , double );
extern long double nextafterl(long double , long double );
extern double nexttoward(double , long double );
extern float nexttowardf(float , long double );
extern long double nexttowardl(long double , long double );
extern float fdimf(float , float );
extern double fdim(double , double );
extern long double fdiml(long double , long double );
extern float fmaxf(float , float );
extern double fmax(double , double );
extern long double fmaxl(long double , long double );
extern float fminf(float , float );
extern double fmin(double , double );
extern long double fminl(long double , long double );
extern float fmaf(float , float , float );
extern double fma(double , double , double );
extern long double fmal(long double , long double , long double );
extern float __inff(void );
extern double __inf(void );
extern long double __infl(void );
extern float __nan(void );
extern float __exp10f(float );
extern double __exp10(double );
extern __inline void __sincosf(float __x, float *__sinp , float *__cosp);
extern __inline void __sincos(double __x, double *__sinp , double *__cosp);
extern float __cospif(float );
extern double __cospi(double );
extern float __sinpif(float );
extern double __sinpi(double );
extern float __tanpif(float );
extern double __tanpi(double );
extern __inline void __sincospif(float __x, float *__sinp , float *__cosp);
extern __inline void __sincospi(double __x, double *__sinp , double *__cosp);
struct __float2 {
    float __sinval;
    float __cosval;
} ;
struct __double2 {
    double __sinval;
    double __cosval;
} ;
extern struct __float2 __sincosf_stret(float );
extern struct __double2 __sincos_stret(double );
extern struct __float2 __sincospif_stret(float );
extern struct __double2 __sincospi_stret(double );
extern __inline void __sincosf(float __x, float *__sinp , float *__cosp) {
    struct __float2 _imopVarPre137;
    _imopVarPre137 = __sincosf_stret(__x);
    const struct __float2 __stret = _imopVarPre137;
    *__sinp = __stret.__sinval;
    *__cosp = __stret.__cosval;
}
extern __inline void __sincos(double __x, double *__sinp , double *__cosp) {
    struct __double2 _imopVarPre139;
    _imopVarPre139 = __sincos_stret(__x);
    const struct __double2 __stret = _imopVarPre139;
    *__sinp = __stret.__sinval;
    *__cosp = __stret.__cosval;
}
extern __inline void __sincospif(float __x, float *__sinp , float *__cosp) {
    struct __float2 _imopVarPre141;
    _imopVarPre141 = __sincospif_stret(__x);
    const struct __float2 __stret = _imopVarPre141;
    *__sinp = __stret.__sinval;
    *__cosp = __stret.__cosval;
}
extern __inline void __sincospi(double __x, double *__sinp , double *__cosp) {
    struct __double2 _imopVarPre143;
    _imopVarPre143 = __sincospi_stret(__x);
    const struct __double2 __stret = _imopVarPre143;
    *__sinp = __stret.__sinval;
    *__cosp = __stret.__cosval;
}
extern double j0(double );
extern double j1(double );
extern double jn(int , double );
extern double y0(double );
extern double y1(double );
extern double yn(int , double );
extern double scalb(double , double );
extern int signgam;
extern long int rinttol(double );
extern long int roundtol(double );
extern double drem(double , double );
extern int finite(double );
extern double gamma(double );
extern double significand(double );
struct exception {
    int type;
    char *name;
    double arg1;
    double arg2;
    double retval;
} ;
extern int matherr(struct exception *);
typedef int boolean;
struct stUn_imopVarPre11 {
    double real;
    double imag;
} ;
typedef struct stUn_imopVarPre11 dcomplex;
extern double randlc(double *, double );
extern void vranlc(int , double * , double , double *);
extern void timer_clear(int );
extern void timer_start(int );
extern void timer_stop(int );
extern double timer_read(int );
extern void c_print_results(char *name, char class , int n1 , int n2 , int n3 , int niter , int nthreads , double t , double mops , char *optype , int passed_verification , char *npbversion , char *compiletime , char *cc , char *clink , char *c_lib , char *c_inc , char *cflags , char *clinkflags , char *rand);
int fftblock;
int fftblockpad;
static int dims[3][3];
static int xstart[3];
static int ystart[3];
static int zstart[3];
static int xend[3];
static int yend[3];
static int zend[3];
static double ex[(6 * (64 * 64 / 4 + 64 * 64 / 4 + 64 * 64 / 4)) + 1];
static dcomplex u[64];
static dcomplex sums[6 + 1];
static int niter;
static void evolve(dcomplex u0[64][64][64], dcomplex u1[64][64][64] , int t , int indexmap[64][64][64] , int d[3]);
static void compute_initial_conditions(dcomplex u0[64][64][64], int d[3]);
static void ipow46(double a, int exponent , double *result);
static void setup(void );
static void compute_indexmap(int indexmap[64][64][64], int d[3]);
static void print_timers(void );
static void fft(int dir, dcomplex x1[64][64][64] , dcomplex x2[64][64][64]);
static void cffts1(int is, int d[3] , dcomplex x[64][64][64] , dcomplex xout[64][64][64] , dcomplex y0[64][18] , dcomplex y1[64][18]);
static void cffts2(int is, int d[3] , dcomplex x[64][64][64] , dcomplex xout[64][64][64] , dcomplex y0[64][18] , dcomplex y1[64][18]);
static void cffts3(int is, int d[3] , dcomplex x[64][64][64] , dcomplex xout[64][64][64] , dcomplex y0[64][18] , dcomplex y1[64][18]);
static void fft_init(int n);
static void cfftz(int is, int m , int n , dcomplex x[64][18] , dcomplex y[64][18]);
static void fftz2(int is, int l , int m , int n , int ny , int ny1 , dcomplex u[64] , dcomplex x[64][18] , dcomplex y[64][18]);
static int ilog2(int n);
static void checksum(int i, dcomplex u1[64][64][64] , int d[3]);
static void verify(int d1, int d2 , int d3 , int nt , boolean *verified , char *class);
int main(int argc, char **argv) {
    int i;
    int ierr;
    static dcomplex u0[64][64][64];
    static dcomplex pad1[3];
    static dcomplex u1[64][64][64];
    static dcomplex pad2[3];
    static dcomplex u2[64][64][64];
    static dcomplex pad3[3];
    static int indexmap[64][64][64];
    int iter;
    int nthreads = 1;
    double total_time;
    double mflops;
    boolean verified;
    char class;
    for (i = 0; i < 7; i++) {
        timer_clear(i);
    }
    setup();
    int ( *_imopVarPre145 );
    _imopVarPre145 = dims[2];
    compute_indexmap(indexmap, _imopVarPre145);
    int ( *_imopVarPre147 );
    _imopVarPre147 = dims[0];
    compute_initial_conditions(u1, _imopVarPre147);
    int _imopVarPre149;
    _imopVarPre149 = dims[0][0];
    fft_init(_imopVarPre149);
    fft(1, u1, u0);
    for (i = 0; i < 7; i++) {
        timer_clear(i);
    }
    timer_start(0);
    if (0 == 1) {
        timer_start(1);
    }
    int ( *_imopVarPre151 );
    _imopVarPre151 = dims[2];
    compute_indexmap(indexmap, _imopVarPre151);
    int ( *_imopVarPre153 );
    _imopVarPre153 = dims[0];
    compute_initial_conditions(u1, _imopVarPre153);
    int _imopVarPre155;
    _imopVarPre155 = dims[0][0];
    fft_init(_imopVarPre155);
    if (0 == 1) {
        timer_stop(1);
    }
    if (0 == 1) {
        timer_start(2);
    }
    fft(1, u1, u0);
    if (0 == 1) {
        timer_stop(2);
    }
    for (iter = 1; iter <= niter; iter++) {
        if (0 == 1) {
            timer_start(3);
        }
        int ( *_imopVarPre157 );
        _imopVarPre157 = dims[0];
        evolve(u0, u1, iter, indexmap, _imopVarPre157);
        if (0 == 1) {
            timer_stop(3);
        }
        if (0 == 1) {
            timer_start(2);
        }
        int _imopVarPre159;
        _imopVarPre159 = -1;
        fft(_imopVarPre159, u1, u2);
        if (0 == 1) {
            timer_stop(2);
        }
        if (0 == 1) {
            timer_start(4);
        }
        int ( *_imopVarPre161 );
        _imopVarPre161 = dims[0];
        checksum(iter, u2, _imopVarPre161);
        if (0 == 1) {
            timer_stop(4);
        }
    }
    char *_imopVarPre164;
    int *_imopVarPre165;
    _imopVarPre164 = &class;
    _imopVarPre165 = &verified;
    verify(64, 64, 64, niter, _imopVarPre165, _imopVarPre164);
        
#pragma omp parallel
    {
    }
    timer_stop(0);
    total_time = timer_read(0);
    if (total_time != 0.0) {
        double _imopVarPre184;
        double _imopVarPre185;
        double _imopVarPre188;
        double _imopVarPre189;
        _imopVarPre184 = (double) 262144;
        _imopVarPre185 = log(_imopVarPre184);
        _imopVarPre188 = (double) 262144;
        _imopVarPre189 = log(_imopVarPre188);
        mflops = 1.0e-6 * (double) 262144 * (14.8157 + 7.19641 * _imopVarPre185 + (5.23518 + 7.21113 * _imopVarPre189) * niter) / total_time;
    } else {
        mflops = 0.0;
    }
    c_print_results("FT", class, 64, 64, 64, niter, nthreads, total_time, mflops, "          floating point", verified, "3.0 structured", "21 Jul 2017", "gcc", "gcc", "(none)", "-I../common", "-O3 -fopenmp", "-O3 -fopenmp", "randdp");
    if (0 == 1) {
        print_timers();
    }
}
static void evolve(dcomplex u0[64][64][64], dcomplex u1[64][64][64] , int t , int indexmap[64][64][64] , int d[3]) {
    int i;
    int j;
    int k;
        
#pragma omp parallel default(shared) private(i, j, k)
    {
        
#pragma omp for nowait
        for (k = 0; k < d[2]; k++) {
            for (j = 0; j < d[1]; j++) {
                for (i = 0; i < d[0]; i++) {
                    u1[k][j][i].real = u0[k][j][i].real * ex[t * indexmap[k][j][i]];
                    (u1[k][j][i].imag = u0[k][j][i].imag * ex[t * indexmap[k][j][i]]);
                }
            }
        }
        
#pragma omp barrier
    }
}
static void compute_initial_conditions(dcomplex u0[64][64][64], int d[3]) {
    int k;
    double x0;
    double start;
    double an;
    double dummy;
    static double tmp[64 * 2 * 64 + 1];
    int i;
    int j;
    int t;
    start = 314159265.0;
    double *_imopVarPre192;
    int _imopVarPre193;
    _imopVarPre192 = &an;
    _imopVarPre193 = (zstart[0] - 1) * 2 * 64 * 64 + (ystart[0] - 1) * 2 * 64;
    ipow46(1220703125.0, _imopVarPre193, _imopVarPre192);
    double *_imopVarPre195;
    double _imopVarPre196;
    _imopVarPre195 = &start;
    _imopVarPre196 = randlc(_imopVarPre195, an);
    dummy = _imopVarPre196;
    double *_imopVarPre199;
    int _imopVarPre200;
    _imopVarPre199 = &an;
    _imopVarPre200 = 2 * 64 * 64;
    ipow46(1220703125.0, _imopVarPre200, _imopVarPre199);
    for (k = 0; k < dims[0][2]; k++) {
        x0 = start;
        double *_imopVarPre203;
        int _imopVarPre204;
        _imopVarPre203 = &x0;
        _imopVarPre204 = 2 * 64 * dims[0][1];
        vranlc(_imopVarPre204, _imopVarPre203, 1220703125.0, tmp);
        t = 1;
        for (j = 0; j < dims[0][1]; j++) {
            for (i = 0; i < 64; i++) {
                u0[k][j][i].real = tmp[t++];
                u0[k][j][i].imag = tmp[t++];
            }
        }
        if (k != dims[0][2]) {
            double *_imopVarPre206;
            double _imopVarPre207;
            _imopVarPre206 = &start;
            _imopVarPre207 = randlc(_imopVarPre206, an);
            dummy = _imopVarPre207;
        }
    }
}
static void ipow46(double a, int exponent , double *result) {
    double dummy;
    double q;
    double r;
    int n;
    int n2;
    *result = 1;
    if (exponent == 0) {
        return;
    }
    q = a;
    r = 1;
    n = exponent;
    while (n > 1) {
        n2 = n / 2;
        if (n2 * 2 == n) {
            double *_imopVarPre209;
            double _imopVarPre210;
            _imopVarPre209 = &q;
            _imopVarPre210 = randlc(_imopVarPre209, q);
            dummy = _imopVarPre210;
            n = n2;
        } else {
            double *_imopVarPre212;
            double _imopVarPre213;
            _imopVarPre212 = &r;
            _imopVarPre213 = randlc(_imopVarPre212, q);
            dummy = _imopVarPre213;
            n = n - 1;
        }
    }
    double *_imopVarPre215;
    double _imopVarPre216;
    _imopVarPre215 = &r;
    _imopVarPre216 = randlc(_imopVarPre215, q);
    dummy = _imopVarPre216;
    *result = r;
}
static void setup(void ) {
    int ierr;
    int i;
    int j;
    int fstatus;
    printf("\n\n NAS Parallel Benchmarks 3.0 structured OpenMP C version" " - FT Benchmark\n\n");
    niter = 6;
    printf(" Size                : %3dx%3dx%3d\n", 64, 64, 64);
    printf(" Iterations          :     %7d\n", niter);
    for (i = 0; i < 3; i++) {
        dims[i][0] = 64;
        dims[i][1] = 64;
        dims[i][2] = 64;
    }
    for (i = 0; i < 3; i++) {
        xstart[i] = 1;
        xend[i] = 64;
        ystart[i] = 1;
        yend[i] = 64;
        zstart[i] = 1;
        zend[i] = 64;
    }
    fftblock = 16;
    fftblockpad = 18;
    if (fftblock != 16) {
        fftblockpad = fftblock + 3;
    }
}
static void compute_indexmap(int indexmap[64][64][64], int d[3]) {
    int i;
    int j;
    int k;
    int ii;
    int ii2;
    int jj;
    int ij2;
    int kk;
    double ap;
        
#pragma omp parallel default(shared) private(i, j, k, ii, ii2, jj, ij2, kk)
    {
        
#pragma omp for nowait
        for (i = 0; i < dims[2][0]; i++) {
            ii = (i + 1 + xstart[2] - 2 + 64 / 2) % 64 - 64 / 2;
            ii2 = ii * ii;
            for (j = 0; j < dims[2][1]; j++) {
                jj = (j + 1 + ystart[2] - 2 + 64 / 2) % 64 - 64 / 2;
                ij2 = jj * jj + ii2;
                for (k = 0; k < dims[2][2]; k++) {
                    kk = (k + 1 + zstart[2] - 2 + 64 / 2) % 64 - 64 / 2;
                    indexmap[k][j][i] = kk * kk + ij2;
                }
            }
        }
        
#pragma omp barrier
    }
    ap = -4.0 * 1.0e-6 * 3.141592653589793238 * 3.141592653589793238;
    ex[0] = 1.0;
    double _imopVarPre217;
    _imopVarPre217 = exp(ap);
    ex[1] = _imopVarPre217;
    for (i = 2; i <= (6 * (64 * 64 / 4 + 64 * 64 / 4 + 64 * 64 / 4)); i++) {
        ex[i] = ex[i - 1] * ex[1];
    }
}
static void print_timers(void ) {
    int i;
    char *tstrings[] = {"          total ", "          setup " , "            fft " , "         evolve " , "       checksum " , "         fftlow " , "        fftcopy "};
    for (i = 0; i < 7; i++) {
        double _imopVarPre219;
        _imopVarPre219 = timer_read(i);
        if (_imopVarPre219 != 0.0) {
            double _imopVarPre222;
            char *_imopVarPre223;
            _imopVarPre222 = timer_read(i);
            _imopVarPre223 = tstrings[i];
            printf("timer %2d(%16s( :%10.6f\n", i, _imopVarPre223, _imopVarPre222);
        }
    }
}
static void fft(int dir, dcomplex x1[64][64][64] , dcomplex x2[64][64][64]) {
    dcomplex y0[64][18];
    dcomplex y1[64][18];
    if (dir == 1) {
        int ( *_imopVarPre225 );
        _imopVarPre225 = dims[0];
        cffts1(1, _imopVarPre225, x1, x1, y0, y1);
        int ( *_imopVarPre227 );
        _imopVarPre227 = dims[1];
        cffts2(1, _imopVarPre227, x1, x1, y0, y1);
        int ( *_imopVarPre229 );
        _imopVarPre229 = dims[2];
        cffts3(1, _imopVarPre229, x1, x2, y0, y1);
    } else {
        int ( *_imopVarPre232 );
        int _imopVarPre233;
        _imopVarPre232 = dims[2];
        _imopVarPre233 = -1;
        cffts3(_imopVarPre233, _imopVarPre232, x1, x1, y0, y1);
        int ( *_imopVarPre236 );
        int _imopVarPre237;
        _imopVarPre236 = dims[1];
        _imopVarPre237 = -1;
        cffts2(_imopVarPre237, _imopVarPre236, x1, x1, y0, y1);
        int ( *_imopVarPre240 );
        int _imopVarPre241;
        _imopVarPre240 = dims[0];
        _imopVarPre241 = -1;
        cffts1(_imopVarPre241, _imopVarPre240, x1, x2, y0, y1);
    }
}
static void cffts1(int is, int d[3] , dcomplex x[64][64][64] , dcomplex xout[64][64][64] , dcomplex y0[64][18] , dcomplex y1[64][18]) {
    int logd[3];
    int i;
    int j;
    int k;
    int jj;
    for (i = 0; i < 3; i++) {
        int _imopVarPre243;
        int _imopVarPre244;
        _imopVarPre243 = d[i];
        _imopVarPre244 = ilog2(_imopVarPre243);
        logd[i] = _imopVarPre244;
    }
        
#pragma omp parallel default(shared) private(i, j, k, jj) shared(is)
    {
        dcomplex y0[64][18];
        dcomplex y1[64][18];
        
#pragma omp for nowait
        for (k = 0; k < d[2]; k++) {
            for (jj = 0; jj <= d[1] - fftblock; jj += fftblock) {
                for (j = 0; j < fftblock; j++) {
                    for (i = 0; i < d[0]; i++) {
                        y0[i][j].real = x[k][j + jj][i].real;
                        y0[i][j].imag = x[k][j + jj][i].imag;
                    }
                }
                int _imopVarPre247;
                int _imopVarPre248;
                _imopVarPre247 = d[0];
                _imopVarPre248 = logd[0];
                cfftz(is, _imopVarPre248, _imopVarPre247, y0, y1);
                for (j = 0; j < fftblock; j++) {
                    for (i = 0; i < d[0]; i++) {
                        xout[k][j + jj][i].real = y0[i][j].real;
                        xout[k][j + jj][i].imag = y0[i][j].imag;
                    }
                }
            }
        }
        
#pragma omp barrier
    }
}
static void cffts2(int is, int d[3] , dcomplex x[64][64][64] , dcomplex xout[64][64][64] , dcomplex y0[64][18] , dcomplex y1[64][18]) {
    int logd[3];
    int i;
    int j;
    int k;
    int ii;
    for (i = 0; i < 3; i++) {
        int _imopVarPre250;
        int _imopVarPre251;
        _imopVarPre250 = d[i];
        _imopVarPre251 = ilog2(_imopVarPre250);
        logd[i] = _imopVarPre251;
    }
        
#pragma omp parallel default(shared) private(i, j, k, ii) shared(is)
    {
        dcomplex y0[64][18];
        dcomplex y1[64][18];
        
#pragma omp for nowait
        for (k = 0; k < d[2]; k++) {
            for (ii = 0; ii <= d[0] - fftblock; ii += fftblock) {
                for (j = 0; j < d[1]; j++) {
                    for (i = 0; i < fftblock; i++) {
                        y0[j][i].real = x[k][j][i + ii].real;
                        y0[j][i].imag = x[k][j][i + ii].imag;
                    }
                }
                int _imopVarPre254;
                int _imopVarPre255;
                _imopVarPre254 = d[1];
                _imopVarPre255 = logd[1];
                cfftz(is, _imopVarPre255, _imopVarPre254, y0, y1);
                for (j = 0; j < d[1]; j++) {
                    for (i = 0; i < fftblock; i++) {
                        xout[k][j][i + ii].real = y0[j][i].real;
                        xout[k][j][i + ii].imag = y0[j][i].imag;
                    }
                }
            }
        }
        
#pragma omp barrier
    }
}
static void cffts3(int is, int d[3] , dcomplex x[64][64][64] , dcomplex xout[64][64][64] , dcomplex y0[64][18] , dcomplex y1[64][18]) {
    int logd[3];
    int i;
    int j;
    int k;
    int ii;
    for (i = 0; i < 3; i++) {
        int _imopVarPre257;
        int _imopVarPre258;
        _imopVarPre257 = d[i];
        _imopVarPre258 = ilog2(_imopVarPre257);
        logd[i] = _imopVarPre258;
    }
        
#pragma omp parallel default(shared) private(i, j, k, ii) shared(is)
    {
        dcomplex y0[64][18];
        dcomplex y1[64][18];
        
#pragma omp for nowait
        for (j = 0; j < d[1]; j++) {
            for (ii = 0; ii <= d[0] - fftblock; ii += fftblock) {
                for (k = 0; k < d[2]; k++) {
                    for (i = 0; i < fftblock; i++) {
                        y0[k][i].real = x[k][j][i + ii].real;
                        y0[k][i].imag = x[k][j][i + ii].imag;
                    }
                }
                int _imopVarPre261;
                int _imopVarPre262;
                _imopVarPre261 = d[2];
                _imopVarPre262 = logd[2];
                cfftz(is, _imopVarPre262, _imopVarPre261, y0, y1);
                for (k = 0; k < d[2]; k++) {
                    for (i = 0; i < fftblock; i++) {
                        xout[k][j][i + ii].real = y0[k][i].real;
                        xout[k][j][i + ii].imag = y0[k][i].imag;
                    }
                }
            }
        }
        
#pragma omp barrier
    }
}
static void fft_init(int n) {
    int m;
    int nu;
    int ku;
    int i;
    int j;
    int ln;
    double t;
    double ti;
    nu = n;
    m = ilog2(n);
    u[0].real = (double) m;
    u[0].imag = 0.0;
    ku = 1;
    ln = 1;
    for (j = 1; j <= m; j++) {
        t = 3.141592653589793238 / ln;
        for (i = 0; i <= ln - 1; i++) {
            ti = i * t;
            double _imopVarPre263;
            _imopVarPre263 = cos(ti);
            u[i + ku].real = _imopVarPre263;
            double _imopVarPre264;
            _imopVarPre264 = sin(ti);
            u[i + ku].imag = _imopVarPre264;
        }
        ku = ku + ln;
        ln = 2 * ln;
    }
}
static void cfftz(int is, int m , int n , dcomplex x[64][18] , dcomplex y[64][18]) {
    int i;
    int j;
    int l;
    int mx;
    mx = (int) (u[0].real);
    int _imopVarPre266;
    int _imopVarPre267;
    int _imopVarPre268;
    _imopVarPre266 = is != 1;
    if (_imopVarPre266) {
        _imopVarPre266 = is != -1;
    }
    _imopVarPre267 = _imopVarPre266;
    if (!_imopVarPre267) {
        _imopVarPre268 = m < 1;
        if (!_imopVarPre268) {
            _imopVarPre268 = m > mx;
        }
        _imopVarPre267 = _imopVarPre268;
    }
    if (_imopVarPre267) {
        printf("CFFTZ: Either U has not been initialized, or else\n" "one of the input parameters is invalid%5d%5d%5d\n", is, m, mx);
        exit(1);
    }
    for (l = 1; l <= m; l += 2) {
        fftz2(is, l, m, n, fftblock, fftblockpad, u, x, y);
        if (l == m) {
            break;
        }
        int _imopVarPre270;
        _imopVarPre270 = l + 1;
        fftz2(is, _imopVarPre270, m, n, fftblock, fftblockpad, u, y, x);
    }
    if (m % 2 == 1) {
        for (j = 0; j < n; j++) {
            for (i = 0; i < fftblock; i++) {
                x[j][i].real = y[j][i].real;
                x[j][i].imag = y[j][i].imag;
            }
        }
    }
}
static void fftz2(int is, int l , int m , int n , int ny , int ny1 , dcomplex u[64] , dcomplex x[64][18] , dcomplex y[64][18]) {
    int k;
    int n1;
    int li;
    int lj;
    int lk;
    int ku;
    int i;
    int j;
    int i11;
    int i12;
    int i21;
    int i22;
    dcomplex u1;
    dcomplex x11;
    dcomplex x21;
    n1 = n / 2;
    if (l - 1 == 0) {
        lk = 1;
    } else {
        lk = 2 << ((l - 1) - 1);
    }
    if (m - l == 0) {
        li = 1;
    } else {
        li = 2 << ((m - l) - 1);
    }
    lj = 2 * lk;
    ku = li;
    for (i = 0; i < li; i++) {
        i11 = i * lk;
        i12 = i11 + n1;
        i21 = i * lj;
        i22 = i21 + lk;
        if (is >= 1) {
            u1.real = u[ku + i].real;
            u1.imag = u[ku + i].imag;
        } else {
            u1.real = u[ku + i].real;
            u1.imag = -u[ku + i].imag;
        }
        for (k = 0; k < lk; k++) {
            for (j = 0; j < ny; j++) {
                double x11real;
                double x11imag;
                double x21real;
                double x21imag;
                x11real = x[i11 + k][j].real;
                x11imag = x[i11 + k][j].imag;
                x21real = x[i12 + k][j].real;
                x21imag = x[i12 + k][j].imag;
                y[i21 + k][j].real = x11real + x21real;
                y[i21 + k][j].imag = x11imag + x21imag;
                y[i22 + k][j].real = u1.real * (x11real - x21real) - u1.imag * (x11imag - x21imag);
                y[i22 + k][j].imag = u1.real * (x11imag - x21imag) + u1.imag * (x11real - x21real);
            }
        }
    }
}
static int ilog2(int n) {
    int nn;
    int lg;
    if (n == 1) {
        return 0;
    }
    lg = 1;
    nn = 2;
    while (nn < n) {
        nn = nn << 1;
        lg++;
    }
    return lg;
}
static void checksum(int i, dcomplex u1[64][64][64] , int d[3]) {
        
#pragma omp parallel default(shared)
    {
        int j;
        int q;
        int r;
        int s;
        int ierr;
        dcomplex chk;
        dcomplex allchk;
        chk.real = 0.0;
        chk.imag = 0.0;
        
#pragma omp for nowait
        for (j = 1; j <= 1024; j++) {
            q = j % 64 + 1;
            int _imopVarPre272;
            _imopVarPre272 = q >= xstart[0];
            if (_imopVarPre272) {
                _imopVarPre272 = q <= xend[0];
            }
            if (_imopVarPre272) {
                r = (3 * j) % 64 + 1;
                int _imopVarPre274;
                _imopVarPre274 = r >= ystart[0];
                if (_imopVarPre274) {
                    _imopVarPre274 = r <= yend[0];
                }
                if (_imopVarPre274) {
                    s = (5 * j) % 64 + 1;
                    int _imopVarPre276;
                    _imopVarPre276 = s >= zstart[0];
                    if (_imopVarPre276) {
                        _imopVarPre276 = s <= zend[0];
                    }
                    if (_imopVarPre276) {
                        chk.real = chk.real + u1[s - zstart[0]][r - ystart[0]][q - xstart[0]].real;
                        (chk.imag = chk.imag + u1[s - zstart[0]][r - ystart[0]][q - xstart[0]].imag);
                    }
                }
            }
        }
        
#pragma omp critical
        {
            sums[i].real += chk.real;
            sums[i].imag += chk.imag;
        }
        
#pragma omp barrier
        
#pragma omp single nowait
        {
            sums[i].real = sums[i].real / (double) 262144;
            sums[i].imag = sums[i].imag / (double) 262144;
            double _imopVarPre279;
            double _imopVarPre280;
            _imopVarPre279 = sums[i].imag;
            _imopVarPre280 = sums[i].real;
            printf("T = %5d     Checksum = %22.12e %22.12e\n", i, _imopVarPre280, _imopVarPre279);
        }
        
#pragma omp barrier
    }
}
static void verify(int d1, int d2 , int d3 , int nt , boolean *verified , char *class) {
    int ierr;
    int size;
    int i;
    double err;
    double epsilon;
    double vdata_real_s[6 + 1] = {0.0, 5.546087004964e+02 , 5.546385409189e+02 , 5.546148406171e+02 , 5.545423607415e+02 , 5.544255039624e+02 , 5.542683411902e+02};
    double vdata_imag_s[6 + 1] = {0.0, 4.845363331978e+02 , 4.865304269511e+02 , 4.883910722336e+02 , 4.901273169046e+02 , 4.917475857993e+02 , 4.932597244941e+02};
    double vdata_real_w[6 + 1] = {0.0, 5.673612178944e+02 , 5.631436885271e+02 , 5.594024089970e+02 , 5.560698047020e+02 , 5.530898991250e+02 , 5.504159734538e+02};
    double vdata_imag_w[6 + 1] = {0.0, 5.293246849175e+02 , 5.282149986629e+02 , 5.270996558037e+02 , 5.260027904925e+02 , 5.249400845633e+02 , 5.239212247086e+02};
    double vdata_real_a[6 + 1] = {0.0, 5.046735008193e+02 , 5.059412319734e+02 , 5.069376896287e+02 , 5.077892868474e+02 , 5.085233095391e+02 , 5.091487099959e+02};
    double vdata_imag_a[6 + 1] = {0.0, 5.114047905510e+02 , 5.098809666433e+02 , 5.098144042213e+02 , 5.101336130759e+02 , 5.104914655194e+02 , 5.107917842803e+02};
    double vdata_real_b[20 + 1] = {0.0, 5.177643571579e+02 , 5.154521291263e+02 , 5.146409228649e+02 , 5.142378756213e+02 , 5.139626667737e+02 , 5.137423460082e+02 , 5.135547056878e+02 , 5.133910925466e+02 , 5.132470705390e+02 , 5.131197729984e+02 , 5.130070319283e+02 , 5.129070537032e+02 , 5.128182883502e+02 , 5.127393733383e+02 , 5.126691062020e+02 , 5.126064276004e+02 , 5.125504076570e+02 , 5.125002331720e+02 , 5.124551951846e+02 , 5.124146770029e+02};
    double vdata_imag_b[20 + 1] = {0.0, 5.077803458597e+02 , 5.088249431599e+02 , 5.096208912659e+02 , 5.101023387619e+02 , 5.103976610617e+02 , 5.105948019802e+02 , 5.107404165783e+02 , 5.108576573661e+02 , 5.109577278523e+02 , 5.110460304483e+02 , 5.111252433800e+02 , 5.111968077718e+02 , 5.112616233064e+02 , 5.113203605551e+02 , 5.113735928093e+02 , 5.114218460548e+02 , 5.114656139760e+02 , 5.115053595966e+02 , 5.115415130407e+02 , 5.115744692211e+02};
    double vdata_real_c[20 + 1] = {0.0, 5.195078707457e+02 , 5.155422171134e+02 , 5.144678022222e+02 , 5.140150594328e+02 , 5.137550426810e+02 , 5.135811056728e+02 , 5.134569343165e+02 , 5.133651975661e+02 , 5.132955192805e+02 , 5.132410471738e+02 , 5.131971141679e+02 , 5.131605205716e+02 , 5.131290734194e+02 , 5.131012720314e+02 , 5.130760908195e+02 , 5.130528295923e+02 , 5.130310107773e+02 , 5.130103090133e+02 , 5.129905029333e+02 , 5.129714421109e+02};
    double vdata_imag_c[20 + 1] = {0.0, 5.149019699238e+02 , 5.127578201997e+02 , 5.122251847514e+02 , 5.121090289018e+02 , 5.121143685824e+02 , 5.121496764568e+02 , 5.121870921893e+02 , 5.122193250322e+02 , 5.122454735794e+02 , 5.122663649603e+02 , 5.122830879827e+02 , 5.122965869718e+02 , 5.123075927445e+02 , 5.123166486553e+02 , 5.123241541685e+02 , 5.123304037599e+02 , 5.123356167976e+02 , 5.123399592211e+02 , 5.123435588985e+02 , 5.123465164008e+02};
    epsilon = 1.0e-12;
    *verified = 1;
    *class = 'U';
    int _imopVarPre284;
    int _imopVarPre285;
    int _imopVarPre286;
    _imopVarPre284 = d1 == 64;
    if (_imopVarPre284) {
        _imopVarPre285 = d2 == 64;
        if (_imopVarPre285) {
            _imopVarPre286 = d3 == 64;
            if (_imopVarPre286) {
                _imopVarPre286 = nt == 6;
            }
            _imopVarPre285 = _imopVarPre286;
        }
        _imopVarPre284 = _imopVarPre285;
    }
    if (_imopVarPre284) {
        *class = 'S';
        for (i = 1; i <= nt; i++) {
            err = (sums[i].real - vdata_real_s[i]) / vdata_real_s[i];
            double _imopVarPre288;
            _imopVarPre288 = fabs(err);
            if (_imopVarPre288 > epsilon) {
                *verified = 0;
                break;
            }
            err = (sums[i].imag - vdata_imag_s[i]) / vdata_imag_s[i];
            double _imopVarPre290;
            _imopVarPre290 = fabs(err);
            if (_imopVarPre290 > epsilon) {
                *verified = 0;
                break;
            }
        }
    } else {
        int _imopVarPre294;
        int _imopVarPre295;
        int _imopVarPre296;
        _imopVarPre294 = d1 == 128;
        if (_imopVarPre294) {
            _imopVarPre295 = d2 == 128;
            if (_imopVarPre295) {
                _imopVarPre296 = d3 == 32;
                if (_imopVarPre296) {
                    _imopVarPre296 = nt == 6;
                }
                _imopVarPre295 = _imopVarPre296;
            }
            _imopVarPre294 = _imopVarPre295;
        }
        if (_imopVarPre294) {
            *class = 'W';
            for (i = 1; i <= nt; i++) {
                err = (sums[i].real - vdata_real_w[i]) / vdata_real_w[i];
                double _imopVarPre298;
                _imopVarPre298 = fabs(err);
                if (_imopVarPre298 > epsilon) {
                    *verified = 0;
                    break;
                }
                err = (sums[i].imag - vdata_imag_w[i]) / vdata_imag_w[i];
                double _imopVarPre300;
                _imopVarPre300 = fabs(err);
                if (_imopVarPre300 > epsilon) {
                    *verified = 0;
                    break;
                }
            }
        } else {
            int _imopVarPre304;
            int _imopVarPre305;
            int _imopVarPre306;
            _imopVarPre304 = d1 == 256;
            if (_imopVarPre304) {
                _imopVarPre305 = d2 == 256;
                if (_imopVarPre305) {
                    _imopVarPre306 = d3 == 128;
                    if (_imopVarPre306) {
                        _imopVarPre306 = nt == 6;
                    }
                    _imopVarPre305 = _imopVarPre306;
                }
                _imopVarPre304 = _imopVarPre305;
            }
            if (_imopVarPre304) {
                *class = 'A';
                for (i = 1; i <= nt; i++) {
                    err = (sums[i].real - vdata_real_a[i]) / vdata_real_a[i];
                    double _imopVarPre308;
                    _imopVarPre308 = fabs(err);
                    if (_imopVarPre308 > epsilon) {
                        *verified = 0;
                        break;
                    }
                    err = (sums[i].imag - vdata_imag_a[i]) / vdata_imag_a[i];
                    double _imopVarPre310;
                    _imopVarPre310 = fabs(err);
                    if (_imopVarPre310 > epsilon) {
                        *verified = 0;
                        break;
                    }
                }
            } else {
                int _imopVarPre314;
                int _imopVarPre315;
                int _imopVarPre316;
                _imopVarPre314 = d1 == 512;
                if (_imopVarPre314) {
                    _imopVarPre315 = d2 == 256;
                    if (_imopVarPre315) {
                        _imopVarPre316 = d3 == 256;
                        if (_imopVarPre316) {
                            _imopVarPre316 = nt == 20;
                        }
                        _imopVarPre315 = _imopVarPre316;
                    }
                    _imopVarPre314 = _imopVarPre315;
                }
                if (_imopVarPre314) {
                    *class = 'B';
                    for (i = 1; i <= nt; i++) {
                        err = (sums[i].real - vdata_real_b[i]) / vdata_real_b[i];
                        double _imopVarPre318;
                        _imopVarPre318 = fabs(err);
                        if (_imopVarPre318 > epsilon) {
                            *verified = 0;
                            break;
                        }
                        err = (sums[i].imag - vdata_imag_b[i]) / vdata_imag_b[i];
                        double _imopVarPre320;
                        _imopVarPre320 = fabs(err);
                        if (_imopVarPre320 > epsilon) {
                            *verified = 0;
                            break;
                        }
                    }
                } else {
                    int _imopVarPre324;
                    int _imopVarPre325;
                    int _imopVarPre326;
                    _imopVarPre324 = d1 == 512;
                    if (_imopVarPre324) {
                        _imopVarPre325 = d2 == 512;
                        if (_imopVarPre325) {
                            _imopVarPre326 = d3 == 512;
                            if (_imopVarPre326) {
                                _imopVarPre326 = nt == 20;
                            }
                            _imopVarPre325 = _imopVarPre326;
                        }
                        _imopVarPre324 = _imopVarPre325;
                    }
                    if (_imopVarPre324) {
                        *class = 'C';
                        for (i = 1; i <= nt; i++) {
                            err = (sums[i].real - vdata_real_c[i]) / vdata_real_c[i];
                            double _imopVarPre328;
                            _imopVarPre328 = fabs(err);
                            if (_imopVarPre328 > epsilon) {
                                *verified = 0;
                                break;
                            }
                            err = (sums[i].imag - vdata_imag_c[i]) / vdata_imag_c[i];
                            double _imopVarPre330;
                            _imopVarPre330 = fabs(err);
                            if (_imopVarPre330 > epsilon) {
                                *verified = 0;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    if (*class != 'U') {
        printf("Result verification successful\n");
    } else {
        printf("Result verification failed\n");
    }
    char _imopVarPre332;
    _imopVarPre332 = *class;
    printf("class = %1c\n", _imopVarPre332);
}
