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
    fpos_t ( *_seek )(void *, fpos_t  , int );
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
int fclose(FILE *);
int fgetc(FILE *);
FILE *fopen(const char *restrict __filename, const char *restrict __mode);
int fscanf(FILE *restrict , const char *restrict , ...);
int printf(const char *restrict , ...);
typedef __darwin_off_t off_t;
typedef __darwin_ssize_t ssize_t;
extern const int sys_nerr;
extern const char *const sys_errlist[];
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
    void ( *sa_tramp )(void *, int  , int  , siginfo_t * , void *);
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
union wait {
    int w_status;
    struct stUn_imopVarPre2 {
        unsigned int w_Termsig: 7, w_Coredump: 1 , w_Retcode: 8 , w_Filler: 16;
    } w_T;
    struct stUn_imopVarPre3 {
        unsigned int w_Stopval: 8, w_Stopsig: 8 , w_Filler: 16;
    } w_S;
} ;
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
void exit(int );
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
extern char *suboptarg;
typedef float float_t;
typedef double double_t;
extern double fabs(double );
extern double sqrt(double );
struct __float2 {
    float __sinval;
    float __cosval;
} ;
struct __double2 {
    double __sinval;
    double __cosval;
} ;
extern int signgam;
struct exception {
    int type;
    char *name;
    double arg1;
    double arg2;
    double retval;
} ;
typedef int boolean;
struct stUn_imopVarPre11 {
    double real;
    double imag;
} ;
typedef struct stUn_imopVarPre11 dcomplex;
extern void timer_clear(int );
extern void timer_start(int );
extern void timer_stop(int );
extern double timer_read(int );
extern void c_print_results(char *name, char class , int n1 , int n2 , int n3 , int niter , int nthreads , double t , double mops , char *optype , int passed_verification , char *npbversion , char *compiletime , char *cc , char *clink , char *c_lib , char *c_inc , char *cflags , char *clinkflags , char *rand);
static int grid_points[3];
static double tx1;
static double tx2;
static double tx3;
static double ty1;
static double ty2;
static double ty3;
static double tz1;
static double tz2;
static double tz3;
static double dx1;
static double dx2;
static double dx3;
static double dx4;
static double dx5;
static double dy1;
static double dy2;
static double dy3;
static double dy4;
static double dy5;
static double dz1;
static double dz2;
static double dz3;
static double dz4;
static double dz5;
static double dssp;
static double dt;
static double ce[5][13];
static double dxmax;
static double dymax;
static double dzmax;
static double xxcon1;
static double xxcon2;
static double xxcon3;
static double xxcon4;
static double xxcon5;
static double dx1tx1;
static double dx2tx1;
static double dx3tx1;
static double dx4tx1;
static double dx5tx1;
static double yycon1;
static double yycon2;
static double yycon3;
static double yycon4;
static double yycon5;
static double dy1ty1;
static double dy2ty1;
static double dy3ty1;
static double dy4ty1;
static double dy5ty1;
static double zzcon1;
static double zzcon2;
static double zzcon3;
static double zzcon4;
static double zzcon5;
static double dz1tz1;
static double dz2tz1;
static double dz3tz1;
static double dz4tz1;
static double dz5tz1;
static double dnxm1;
static double dnym1;
static double dnzm1;
static double c1c2;
static double c1c5;
static double c3c4;
static double c1345;
static double conz1;
static double c1;
static double c2;
static double c3;
static double c4;
static double c5;
static double c4dssp;
static double c5dssp;
static double dtdssp;
static double dttx1;
static double dttx2;
static double dtty1;
static double dtty2;
static double dttz1;
static double dttz2;
static double c2dttx1;
static double c2dtty1;
static double c2dttz1;
static double comz1;
static double comz4;
static double comz5;
static double comz6;
static double c3c4tx3;
static double c3c4ty3;
static double c3c4tz3;
static double c2iv;
static double con43;
static double con16;
static double us[12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1];
static double vs[12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1];
static double ws[12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1];
static double qs[12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1];
static double rho_i[12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1];
static double square[12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1];
static double forcing[12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1][5 + 1];
static double u[(12 + 1) / 2 * 2 + 1][(12 + 1) / 2 * 2 + 1][(12 + 1) / 2 * 2 + 1][5];
static double rhs[12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1][5];
static double lhs[12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1][3][5][5];
static double cuf[12];
static double q[12];
static double ue[12][5];
static double buf[12][5];
#pragma omp threadprivate(cuf, q, ue, buf)
static double fjac[12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 - 1 + 1][5][5];
static double njac[12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 - 1 + 1][5][5];
static double tmp1;
static double tmp2;
static double tmp3;
static void add(void );
static void adi(void );
static void error_norm(double rms[5]);
static void rhs_norm(double rms[5]);
static void exact_rhs(void );
static void exact_solution(double xi, double eta , double zeta , double dtemp[5]);
static void initialize(void );
static void lhsinit(void );
static void lhsx(void );
static void lhsy(void );
static void lhsz(void );
static void compute_rhs(void );
static void set_constants(void );
static void verify(int no_time_steps, char *class , boolean *verified);
static void x_solve(void );
static void x_backsubstitute(void );
static void x_solve_cell(void );
static void matvec_sub(double ablock[5][5], double avec[5] , double bvec[5]);
static void matmul_sub(double ablock[5][5], double bblock[5][5] , double cblock[5][5]);
static void binvcrhs(double lhs[5][5], double c[5][5] , double r[5]);
static void binvrhs(double lhs[5][5], double r[5]);
static void y_solve(void );
static void y_backsubstitute(void );
static void y_solve_cell(void );
static void z_solve(void );
static void z_backsubstitute(void );
static void z_solve_cell(void );
int main(int argc, char **argv) {
    double navg;
    double mflops;
    double tmax;
    boolean verified;
    char class;
    int *_imopVarPre175;
    char *_imopVarPre176;
    int niter;
    int step;
    int n3;
    int nthreads = 1;
#pragma omp parallel
    {
        FILE *fp;
#pragma omp master
        {
            printf("\n\n NAS Parallel Benchmarks 3.0 structured OpenMP C version" " - BT Benchmark\n\n");
            fp = fopen("inputbt.data", "r");
            if (fp != ((void *) 0)) {
                printf(" Reading from input file inputbt.data");
                int *_imopVarPre145;
                _imopVarPre145 = &niter;
                fscanf(fp, "%d", _imopVarPre145);
                int _imopVarPre147;
                _imopVarPre147 = fgetc(fp);
                while (_imopVarPre147 != '\n') {
                    ;
                    _imopVarPre147 = fgetc(fp);
                }
                double *_imopVarPre149;
                _imopVarPre149 = &dt;
                fscanf(fp, "%lg", _imopVarPre149);
                int _imopVarPre151;
                _imopVarPre151 = fgetc(fp);
                while (_imopVarPre151 != '\n') {
                    ;
                    _imopVarPre151 = fgetc(fp);
                }
                int *_imopVarPre155;
                int *_imopVarPre156;
                int *_imopVarPre157;
                _imopVarPre155 = &grid_points[2];
                _imopVarPre156 = &grid_points[1];
                _imopVarPre157 = &grid_points[0];
                fscanf(fp, "%d%d%d", _imopVarPre157, _imopVarPre156, _imopVarPre155);
                fclose(fp);
            } else {
                printf(" No input file inputbt.data. Using compiled defaults\n");
                niter = 60;
                dt = 0.010;
                grid_points[0] = 12;
                grid_points[1] = 12;
                grid_points[2] = 12;
            }
        }
        int _imopVarPre161;
        int _imopVarPre162;
        int _imopVarPre163;
#pragma omp master
        {
            _imopVarPre161 = grid_points[2];
            _imopVarPre162 = grid_points[1];
            _imopVarPre163 = grid_points[0];
            printf(" Size: %3dx%3dx%3d\n", _imopVarPre163, _imopVarPre162, _imopVarPre161);
            printf(" Iterations: %3d   dt: %10.6f\n", niter, dt);
        }
        int _imopVarPre164;
        int _imopVarPre165;
#pragma omp master
        {
            _imopVarPre164 = grid_points[0] > 12;
            if (!_imopVarPre164) {
                _imopVarPre165 = grid_points[1] > 12;
                if (!_imopVarPre165) {
                    _imopVarPre165 = grid_points[2] > 12;
                }
                _imopVarPre164 = _imopVarPre165;
            }
            if (_imopVarPre164) {
                int _imopVarPre169;
                int _imopVarPre170;
                int _imopVarPre171;
                _imopVarPre169 = grid_points[2];
                _imopVarPre170 = grid_points[1];
                _imopVarPre171 = grid_points[0];
                printf(" %dx%dx%d\n", _imopVarPre171, _imopVarPre170, _imopVarPre169);
                printf(" Problem size too big for compiled array sizes\n");
                exit(1);
            }
            set_constants();
        }
        int i;
        int j;
        int k;
        int m;
        int ix;
        int iy;
        int iz;
        double xi;
        double eta;
        double zeta;
        double Pface[2][3][5];
        double Pxi;
        double Peta;
        double Pzeta;
        double temp[5];
#pragma omp for nowait
        for (i = 0; i < 12; i++) {
            for (j = 0; j < 12; j++) {
                for (k = 0; k < 12; k++) {
                    for (m = 0; m < 5; m++) {
                        u[i][j][k][m] = 1.0;
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
        for (i = 0; i < grid_points[0]; i++) {
            xi = (double) i * dnxm1;
            for (j = 0; j < grid_points[1]; j++) {
                eta = (double) j * dnym1;
                for (k = 0; k < grid_points[2]; k++) {
                    zeta = (double) k * dnzm1;
                    for (ix = 0; ix < 2; ix++) {
                        double *_imopVarPre191;
                        double _imopVarPre192;
                        _imopVarPre191 = &(Pface[ix][0][0]);
                        _imopVarPre192 = (double) ix;
                        exact_solution(_imopVarPre192, eta, zeta, _imopVarPre191);
                    }
                    for (iy = 0; iy < 2; iy++) {
                        double *_imopVarPre195;
                        double _imopVarPre196;
                        _imopVarPre195 = &Pface[iy][1][0];
                        _imopVarPre196 = (double) iy;
                        exact_solution(xi, _imopVarPre196, zeta, _imopVarPre195);
                    }
                    for (iz = 0; iz < 2; iz++) {
                        double *_imopVarPre199;
                        double _imopVarPre200;
                        _imopVarPre199 = &Pface[iz][2][0];
                        _imopVarPre200 = (double) iz;
                        exact_solution(xi, eta, _imopVarPre200, _imopVarPre199);
                    }
                    for (m = 0; m < 5; m++) {
                        Pxi = xi * Pface[1][0][m] + (1.0 - xi) * Pface[0][0][m];
                        Peta = eta * Pface[1][1][m] + (1.0 - eta) * Pface[0][1][m];
                        Pzeta = zeta * Pface[1][2][m] + (1.0 - zeta) * Pface[0][2][m];
                        u[i][j][k][m] = Pxi + Peta + Pzeta - Pxi * Peta - Pxi * Pzeta - Peta * Pzeta + Pxi * Peta * Pzeta;
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        i = 0;
        xi = 0.0;
#pragma omp for nowait
        for (j = 0; j < grid_points[1]; j++) {
            eta = (double) j * dnym1;
            for (k = 0; k < grid_points[2]; k++) {
                zeta = (double) k * dnzm1;
                exact_solution(xi, eta, zeta, temp);
                for (m = 0; m < 5; m++) {
                    u[i][j][k][m] = temp[m];
                }
            }
        }
        i = grid_points[0] - 1;
        xi = 1.0;
#pragma omp for nowait
        for (j = 0; j < grid_points[1]; j++) {
            eta = (double) j * dnym1;
            for (k = 0; k < grid_points[2]; k++) {
                zeta = (double) k * dnzm1;
                exact_solution(xi, eta, zeta, temp);
                for (m = 0; m < 5; m++) {
                    u[i][j][k][m] = temp[m];
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        j = 0;
        eta = 0.0;
#pragma omp for nowait
        for (i = 0; i < grid_points[0]; i++) {
            xi = (double) i * dnxm1;
            for (k = 0; k < grid_points[2]; k++) {
                zeta = (double) k * dnzm1;
                exact_solution(xi, eta, zeta, temp);
                for (m = 0; m < 5; m++) {
                    u[i][j][k][m] = temp[m];
                }
            }
        }
        j = grid_points[1] - 1;
        eta = 1.0;
#pragma omp for nowait
        for (i = 0; i < grid_points[0]; i++) {
            xi = (double) i * dnxm1;
            for (k = 0; k < grid_points[2]; k++) {
                zeta = (double) k * dnzm1;
                exact_solution(xi, eta, zeta, temp);
                for (m = 0; m < 5; m++) {
                    u[i][j][k][m] = temp[m];
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        k = 0;
        zeta = 0.0;
#pragma omp for nowait
        for (i = 0; i < grid_points[0]; i++) {
            xi = (double) i * dnxm1;
            for (j = 0; j < grid_points[1]; j++) {
                eta = (double) j * dnym1;
                exact_solution(xi, eta, zeta, temp);
                for (m = 0; m < 5; m++) {
                    u[i][j][k][m] = temp[m];
                }
            }
        }
        k = grid_points[2] - 1;
        zeta = 1.0;
#pragma omp for nowait
        for (i = 0; i < grid_points[0]; i++) {
            xi = (double) i * dnxm1;
            for (j = 0; j < grid_points[1]; j++) {
                eta = (double) j * dnym1;
                exact_solution(xi, eta, zeta, temp);
                for (m = 0; m < 5; m++) {
                    u[i][j][k][m] = temp[m];
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        int i_imopVar150;
        int j_imopVar151;
        int k_imopVar152;
        int m_imopVar153;
        int n_imopVar154;
#pragma omp for nowait
        for (i_imopVar150 = 0; i_imopVar150 < grid_points[0]; i_imopVar150++) {
            for (j_imopVar151 = 0; j_imopVar151 < grid_points[1]; j_imopVar151++) {
                for (k_imopVar152 = 0; k_imopVar152 < grid_points[2]; k_imopVar152++) {
                    for (m_imopVar153 = 0; m_imopVar153 < 5; m_imopVar153++) {
                        for (n_imopVar154 = 0; n_imopVar154 < 5; n_imopVar154++) {
                            lhs[i_imopVar150][j_imopVar151][k_imopVar152][0][m_imopVar153][n_imopVar154] = 0.0;
                            lhs[i_imopVar150][j_imopVar151][k_imopVar152][1][m_imopVar153][n_imopVar154] = 0.0;
                            lhs[i_imopVar150][j_imopVar151][k_imopVar152][2][m_imopVar153][n_imopVar154] = 0.0;
                        }
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
        for (i_imopVar150 = 0; i_imopVar150 < grid_points[0]; i_imopVar150++) {
            for (j_imopVar151 = 0; j_imopVar151 < grid_points[1]; j_imopVar151++) {
                for (k_imopVar152 = 0; k_imopVar152 < grid_points[2]; k_imopVar152++) {
                    for (m_imopVar153 = 0; m_imopVar153 < 5; m_imopVar153++) {
                        lhs[i_imopVar150][j_imopVar151][k_imopVar152][1][m_imopVar153][m_imopVar153] = 1.0;
                    }
                }
            }
        }
        double dtemp[5];
        double xi_imopVar75;
        double eta_imopVar76;
        double zeta_imopVar77;
        double dtpp;
        int m_imopVar78;
        int i_imopVar79;
        int j_imopVar80;
        int k_imopVar81;
        int ip1;
        int im1;
        int jp1;
        int jm1;
        int km1;
        int kp1;
#pragma omp for nowait
        for (i_imopVar79 = 0; i_imopVar79 < grid_points[0]; i_imopVar79++) {
            for (j_imopVar80 = 0; j_imopVar80 < grid_points[1]; j_imopVar80++) {
                for (k_imopVar81 = 0; k_imopVar81 < grid_points[2]; k_imopVar81++) {
                    for (m_imopVar78 = 0; m_imopVar78 < 5; m_imopVar78++) {
                        forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] = 0.0;
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
        for (j_imopVar80 = 1; j_imopVar80 < grid_points[1] - 1; j_imopVar80++) {
            eta_imopVar76 = (double) j_imopVar80 * dnym1;
            for (k_imopVar81 = 1; k_imopVar81 < grid_points[2] - 1; k_imopVar81++) {
                zeta_imopVar77 = (double) k_imopVar81 * dnzm1;
                for (i_imopVar79 = 0; i_imopVar79 < grid_points[0]; i_imopVar79++) {
                    xi_imopVar75 = (double) i_imopVar79 * dnxm1;
                    exact_solution(xi_imopVar75, eta_imopVar76, zeta_imopVar77, dtemp);
                    for (m_imopVar78 = 0; m_imopVar78 < 5; m_imopVar78++) {
                        ue[i_imopVar79][m_imopVar78] = dtemp[m_imopVar78];
                    }
                    dtpp = 1.0 / dtemp[0];
                    for (m_imopVar78 = 1; m_imopVar78 <= 4; m_imopVar78++) {
                        buf[i_imopVar79][m_imopVar78] = dtpp * dtemp[m_imopVar78];
                    }
                    cuf[i_imopVar79] = buf[i_imopVar79][1] * buf[i_imopVar79][1];
                    buf[i_imopVar79][0] = cuf[i_imopVar79] + buf[i_imopVar79][2] * buf[i_imopVar79][2] + buf[i_imopVar79][3] * buf[i_imopVar79][3];
                    q[i_imopVar79] = 0.5 * (buf[i_imopVar79][1] * ue[i_imopVar79][1] + buf[i_imopVar79][2] * ue[i_imopVar79][2] + buf[i_imopVar79][3] * ue[i_imopVar79][3]);
                }
                for (i_imopVar79 = 1; i_imopVar79 < grid_points[0] - 1; i_imopVar79++) {
                    im1 = i_imopVar79 - 1;
                    ip1 = i_imopVar79 + 1;
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][0] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][0] - tx2 * (ue[ip1][1] - ue[im1][1]) + dx1tx1 * (ue[ip1][0] - 2.0 * ue[i_imopVar79][0] + ue[im1][0]);
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][1] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][1] - tx2 * ((ue[ip1][1] * buf[ip1][1] + c2 * (ue[ip1][4] - q[ip1])) - (ue[im1][1] * buf[im1][1] + c2 * (ue[im1][4] - q[im1]))) + xxcon1 * (buf[ip1][1] - 2.0 * buf[i_imopVar79][1] + buf[im1][1]) + dx2tx1 * (ue[ip1][1] - 2.0 * ue[i_imopVar79][1] + ue[im1][1]);
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][2] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][2] - tx2 * (ue[ip1][2] * buf[ip1][1] - ue[im1][2] * buf[im1][1]) + xxcon2 * (buf[ip1][2] - 2.0 * buf[i_imopVar79][2] + buf[im1][2]) + dx3tx1 * (ue[ip1][2] - 2.0 * ue[i_imopVar79][2] + ue[im1][2]);
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][3] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][3] - tx2 * (ue[ip1][3] * buf[ip1][1] - ue[im1][3] * buf[im1][1]) + xxcon2 * (buf[ip1][3] - 2.0 * buf[i_imopVar79][3] + buf[im1][3]) + dx4tx1 * (ue[ip1][3] - 2.0 * ue[i_imopVar79][3] + ue[im1][3]);
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][4] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][4] - tx2 * (buf[ip1][1] * (c1 * ue[ip1][4] - c2 * q[ip1]) - buf[im1][1] * (c1 * ue[im1][4] - c2 * q[im1])) + 0.5 * xxcon3 * (buf[ip1][0] - 2.0 * buf[i_imopVar79][0] + buf[im1][0]) + xxcon4 * (cuf[ip1] - 2.0 * cuf[i_imopVar79] + cuf[im1]) + xxcon5 * (buf[ip1][4] - 2.0 * buf[i_imopVar79][4] + buf[im1][4]) + dx5tx1 * (ue[ip1][4] - 2.0 * ue[i_imopVar79][4] + ue[im1][4]);
                }
                for (m_imopVar78 = 0; m_imopVar78 < 5; m_imopVar78++) {
                    i_imopVar79 = 1;
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] - dssp * (5.0 * ue[i_imopVar79][m_imopVar78] - 4.0 * ue[i_imopVar79 + 1][m_imopVar78] + ue[i_imopVar79 + 2][m_imopVar78]);
                    i_imopVar79 = 2;
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] - dssp * (-4.0 * ue[i_imopVar79 - 1][m_imopVar78] + 6.0 * ue[i_imopVar79][m_imopVar78] - 4.0 * ue[i_imopVar79 + 1][m_imopVar78] + ue[i_imopVar79 + 2][m_imopVar78]);
                }
                for (m_imopVar78 = 0; m_imopVar78 < 5; m_imopVar78++) {
                    for (i_imopVar79 = 1 * 3; i_imopVar79 <= grid_points[0] - 3 * 1 - 1; i_imopVar79++) {
                        forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] - dssp * (ue[i_imopVar79 - 2][m_imopVar78] - 4.0 * ue[i_imopVar79 - 1][m_imopVar78] + 6.0 * ue[i_imopVar79][m_imopVar78] - 4.0 * ue[i_imopVar79 + 1][m_imopVar78] + ue[i_imopVar79 + 2][m_imopVar78]);
                    }
                }
                for (m_imopVar78 = 0; m_imopVar78 < 5; m_imopVar78++) {
                    i_imopVar79 = grid_points[0] - 3;
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] - dssp * (ue[i_imopVar79 - 2][m_imopVar78] - 4.0 * ue[i_imopVar79 - 1][m_imopVar78] + 6.0 * ue[i_imopVar79][m_imopVar78] - 4.0 * ue[i_imopVar79 + 1][m_imopVar78]);
                    i_imopVar79 = grid_points[0] - 2;
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] - dssp * (ue[i_imopVar79 - 2][m_imopVar78] - 4.0 * ue[i_imopVar79 - 1][m_imopVar78] + 5.0 * ue[i_imopVar79][m_imopVar78]);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
        for (i_imopVar79 = 1; i_imopVar79 < grid_points[0] - 1; i_imopVar79++) {
            xi_imopVar75 = (double) i_imopVar79 * dnxm1;
            for (k_imopVar81 = 1; k_imopVar81 < grid_points[2] - 1; k_imopVar81++) {
                zeta_imopVar77 = (double) k_imopVar81 * dnzm1;
                for (j_imopVar80 = 0; j_imopVar80 < grid_points[1]; j_imopVar80++) {
                    eta_imopVar76 = (double) j_imopVar80 * dnym1;
                    exact_solution(xi_imopVar75, eta_imopVar76, zeta_imopVar77, dtemp);
                    for (m_imopVar78 = 0; m_imopVar78 < 5; m_imopVar78++) {
                        ue[j_imopVar80][m_imopVar78] = dtemp[m_imopVar78];
                    }
                    dtpp = 1.0 / dtemp[0];
                    for (m_imopVar78 = 1; m_imopVar78 <= 4; m_imopVar78++) {
                        buf[j_imopVar80][m_imopVar78] = dtpp * dtemp[m_imopVar78];
                    }
                    cuf[j_imopVar80] = buf[j_imopVar80][2] * buf[j_imopVar80][2];
                    buf[j_imopVar80][0] = cuf[j_imopVar80] + buf[j_imopVar80][1] * buf[j_imopVar80][1] + buf[j_imopVar80][3] * buf[j_imopVar80][3];
                    q[j_imopVar80] = 0.5 * (buf[j_imopVar80][1] * ue[j_imopVar80][1] + buf[j_imopVar80][2] * ue[j_imopVar80][2] + buf[j_imopVar80][3] * ue[j_imopVar80][3]);
                }
                for (j_imopVar80 = 1; j_imopVar80 < grid_points[1] - 1; j_imopVar80++) {
                    jm1 = j_imopVar80 - 1;
                    jp1 = j_imopVar80 + 1;
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][0] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][0] - ty2 * (ue[jp1][2] - ue[jm1][2]) + dy1ty1 * (ue[jp1][0] - 2.0 * ue[j_imopVar80][0] + ue[jm1][0]);
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][1] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][1] - ty2 * (ue[jp1][1] * buf[jp1][2] - ue[jm1][1] * buf[jm1][2]) + yycon2 * (buf[jp1][1] - 2.0 * buf[j_imopVar80][1] + buf[jm1][1]) + dy2ty1 * (ue[jp1][1] - 2.0 * ue[j_imopVar80][1] + ue[jm1][1]);
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][2] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][2] - ty2 * ((ue[jp1][2] * buf[jp1][2] + c2 * (ue[jp1][4] - q[jp1])) - (ue[jm1][2] * buf[jm1][2] + c2 * (ue[jm1][4] - q[jm1]))) + yycon1 * (buf[jp1][2] - 2.0 * buf[j_imopVar80][2] + buf[jm1][2]) + dy3ty1 * (ue[jp1][2] - 2.0 * ue[j_imopVar80][2] + ue[jm1][2]);
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][3] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][3] - ty2 * (ue[jp1][3] * buf[jp1][2] - ue[jm1][3] * buf[jm1][2]) + yycon2 * (buf[jp1][3] - 2.0 * buf[j_imopVar80][3] + buf[jm1][3]) + dy4ty1 * (ue[jp1][3] - 2.0 * ue[j_imopVar80][3] + ue[jm1][3]);
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][4] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][4] - ty2 * (buf[jp1][2] * (c1 * ue[jp1][4] - c2 * q[jp1]) - buf[jm1][2] * (c1 * ue[jm1][4] - c2 * q[jm1])) + 0.5 * yycon3 * (buf[jp1][0] - 2.0 * buf[j_imopVar80][0] + buf[jm1][0]) + yycon4 * (cuf[jp1] - 2.0 * cuf[j_imopVar80] + cuf[jm1]) + yycon5 * (buf[jp1][4] - 2.0 * buf[j_imopVar80][4] + buf[jm1][4]) + dy5ty1 * (ue[jp1][4] - 2.0 * ue[j_imopVar80][4] + ue[jm1][4]);
                }
                for (m_imopVar78 = 0; m_imopVar78 < 5; m_imopVar78++) {
                    j_imopVar80 = 1;
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] - dssp * (5.0 * ue[j_imopVar80][m_imopVar78] - 4.0 * ue[j_imopVar80 + 1][m_imopVar78] + ue[j_imopVar80 + 2][m_imopVar78]);
                    j_imopVar80 = 2;
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] - dssp * (-4.0 * ue[j_imopVar80 - 1][m_imopVar78] + 6.0 * ue[j_imopVar80][m_imopVar78] - 4.0 * ue[j_imopVar80 + 1][m_imopVar78] + ue[j_imopVar80 + 2][m_imopVar78]);
                }
                for (m_imopVar78 = 0; m_imopVar78 < 5; m_imopVar78++) {
                    for (j_imopVar80 = 1 * 3; j_imopVar80 <= grid_points[1] - 3 * 1 - 1; j_imopVar80++) {
                        forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] - dssp * (ue[j_imopVar80 - 2][m_imopVar78] - 4.0 * ue[j_imopVar80 - 1][m_imopVar78] + 6.0 * ue[j_imopVar80][m_imopVar78] - 4.0 * ue[j_imopVar80 + 1][m_imopVar78] + ue[j_imopVar80 + 2][m_imopVar78]);
                    }
                }
                for (m_imopVar78 = 0; m_imopVar78 < 5; m_imopVar78++) {
                    j_imopVar80 = grid_points[1] - 3;
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] - dssp * (ue[j_imopVar80 - 2][m_imopVar78] - 4.0 * ue[j_imopVar80 - 1][m_imopVar78] + 6.0 * ue[j_imopVar80][m_imopVar78] - 4.0 * ue[j_imopVar80 + 1][m_imopVar78]);
                    j_imopVar80 = grid_points[1] - 2;
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] - dssp * (ue[j_imopVar80 - 2][m_imopVar78] - 4.0 * ue[j_imopVar80 - 1][m_imopVar78] + 5.0 * ue[j_imopVar80][m_imopVar78]);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
        for (i_imopVar79 = 1; i_imopVar79 < grid_points[0] - 1; i_imopVar79++) {
            xi_imopVar75 = (double) i_imopVar79 * dnxm1;
            for (j_imopVar80 = 1; j_imopVar80 < grid_points[1] - 1; j_imopVar80++) {
                eta_imopVar76 = (double) j_imopVar80 * dnym1;
                for (k_imopVar81 = 0; k_imopVar81 < grid_points[2]; k_imopVar81++) {
                    zeta_imopVar77 = (double) k_imopVar81 * dnzm1;
                    exact_solution(xi_imopVar75, eta_imopVar76, zeta_imopVar77, dtemp);
                    for (m_imopVar78 = 0; m_imopVar78 < 5; m_imopVar78++) {
                        ue[k_imopVar81][m_imopVar78] = dtemp[m_imopVar78];
                    }
                    dtpp = 1.0 / dtemp[0];
                    for (m_imopVar78 = 1; m_imopVar78 <= 4; m_imopVar78++) {
                        buf[k_imopVar81][m_imopVar78] = dtpp * dtemp[m_imopVar78];
                    }
                    cuf[k_imopVar81] = buf[k_imopVar81][3] * buf[k_imopVar81][3];
                    buf[k_imopVar81][0] = cuf[k_imopVar81] + buf[k_imopVar81][1] * buf[k_imopVar81][1] + buf[k_imopVar81][2] * buf[k_imopVar81][2];
                    q[k_imopVar81] = 0.5 * (buf[k_imopVar81][1] * ue[k_imopVar81][1] + buf[k_imopVar81][2] * ue[k_imopVar81][2] + buf[k_imopVar81][3] * ue[k_imopVar81][3]);
                }
                for (k_imopVar81 = 1; k_imopVar81 < grid_points[2] - 1; k_imopVar81++) {
                    km1 = k_imopVar81 - 1;
                    kp1 = k_imopVar81 + 1;
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][0] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][0] - tz2 * (ue[kp1][3] - ue[km1][3]) + dz1tz1 * (ue[kp1][0] - 2.0 * ue[k_imopVar81][0] + ue[km1][0]);
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][1] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][1] - tz2 * (ue[kp1][1] * buf[kp1][3] - ue[km1][1] * buf[km1][3]) + zzcon2 * (buf[kp1][1] - 2.0 * buf[k_imopVar81][1] + buf[km1][1]) + dz2tz1 * (ue[kp1][1] - 2.0 * ue[k_imopVar81][1] + ue[km1][1]);
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][2] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][2] - tz2 * (ue[kp1][2] * buf[kp1][3] - ue[km1][2] * buf[km1][3]) + zzcon2 * (buf[kp1][2] - 2.0 * buf[k_imopVar81][2] + buf[km1][2]) + dz3tz1 * (ue[kp1][2] - 2.0 * ue[k_imopVar81][2] + ue[km1][2]);
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][3] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][3] - tz2 * ((ue[kp1][3] * buf[kp1][3] + c2 * (ue[kp1][4] - q[kp1])) - (ue[km1][3] * buf[km1][3] + c2 * (ue[km1][4] - q[km1]))) + zzcon1 * (buf[kp1][3] - 2.0 * buf[k_imopVar81][3] + buf[km1][3]) + dz4tz1 * (ue[kp1][3] - 2.0 * ue[k_imopVar81][3] + ue[km1][3]);
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][4] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][4] - tz2 * (buf[kp1][3] * (c1 * ue[kp1][4] - c2 * q[kp1]) - buf[km1][3] * (c1 * ue[km1][4] - c2 * q[km1])) + 0.5 * zzcon3 * (buf[kp1][0] - 2.0 * buf[k_imopVar81][0] + buf[km1][0]) + zzcon4 * (cuf[kp1] - 2.0 * cuf[k_imopVar81] + cuf[km1]) + zzcon5 * (buf[kp1][4] - 2.0 * buf[k_imopVar81][4] + buf[km1][4]) + dz5tz1 * (ue[kp1][4] - 2.0 * ue[k_imopVar81][4] + ue[km1][4]);
                }
                for (m_imopVar78 = 0; m_imopVar78 < 5; m_imopVar78++) {
                    k_imopVar81 = 1;
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] - dssp * (5.0 * ue[k_imopVar81][m_imopVar78] - 4.0 * ue[k_imopVar81 + 1][m_imopVar78] + ue[k_imopVar81 + 2][m_imopVar78]);
                    k_imopVar81 = 2;
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] - dssp * (-4.0 * ue[k_imopVar81 - 1][m_imopVar78] + 6.0 * ue[k_imopVar81][m_imopVar78] - 4.0 * ue[k_imopVar81 + 1][m_imopVar78] + ue[k_imopVar81 + 2][m_imopVar78]);
                }
                for (m_imopVar78 = 0; m_imopVar78 < 5; m_imopVar78++) {
                    for (k_imopVar81 = 1 * 3; k_imopVar81 <= grid_points[2] - 3 * 1 - 1; k_imopVar81++) {
                        forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] - dssp * (ue[k_imopVar81 - 2][m_imopVar78] - 4.0 * ue[k_imopVar81 - 1][m_imopVar78] + 6.0 * ue[k_imopVar81][m_imopVar78] - 4.0 * ue[k_imopVar81 + 1][m_imopVar78] + ue[k_imopVar81 + 2][m_imopVar78]);
                    }
                }
                for (m_imopVar78 = 0; m_imopVar78 < 5; m_imopVar78++) {
                    k_imopVar81 = grid_points[2] - 3;
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] - dssp * (ue[k_imopVar81 - 2][m_imopVar78] - 4.0 * ue[k_imopVar81 - 1][m_imopVar78] + 6.0 * ue[k_imopVar81][m_imopVar78] - 4.0 * ue[k_imopVar81 + 1][m_imopVar78]);
                    k_imopVar81 = grid_points[2] - 2;
                    forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] = forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] - dssp * (ue[k_imopVar81 - 2][m_imopVar78] - 4.0 * ue[k_imopVar81 - 1][m_imopVar78] + 5.0 * ue[k_imopVar81][m_imopVar78]);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
        for (i_imopVar79 = 1; i_imopVar79 < grid_points[0] - 1; i_imopVar79++) {
            for (j_imopVar80 = 1; j_imopVar80 < grid_points[1] - 1; j_imopVar80++) {
                for (k_imopVar81 = 1; k_imopVar81 < grid_points[2] - 1; k_imopVar81++) {
                    for (m_imopVar78 = 0; m_imopVar78 < 5; m_imopVar78++) {
                        forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78] = -1.0 * forcing[i_imopVar79][j_imopVar80][k_imopVar81][m_imopVar78];
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        int i_imopVar110;
        int j_imopVar111;
        int k_imopVar112;
        int m_imopVar113;
        double rho_inv_imopVar136;
        double uijk_imopVar137;
        double up1_imopVar138;
        double um1_imopVar139;
        double vijk_imopVar140;
        double vp1_imopVar141;
        double vm1_imopVar142;
        double wijk_imopVar143;
        double wp1_imopVar144;
        double wm1_imopVar145;
#pragma omp for nowait
        for (i_imopVar110 = 0; i_imopVar110 < grid_points[0]; i_imopVar110++) {
            for (j_imopVar111 = 0; j_imopVar111 < grid_points[1]; j_imopVar111++) {
                for (k_imopVar112 = 0; k_imopVar112 < grid_points[2]; k_imopVar112++) {
                    rho_inv_imopVar136 = 1.0 / u[i_imopVar110][j_imopVar111][k_imopVar112][0];
                    rho_i[i_imopVar110][j_imopVar111][k_imopVar112] = rho_inv_imopVar136;
                    us[i_imopVar110][j_imopVar111][k_imopVar112] = u[i_imopVar110][j_imopVar111][k_imopVar112][1] * rho_inv_imopVar136;
                    vs[i_imopVar110][j_imopVar111][k_imopVar112] = u[i_imopVar110][j_imopVar111][k_imopVar112][2] * rho_inv_imopVar136;
                    ws[i_imopVar110][j_imopVar111][k_imopVar112] = u[i_imopVar110][j_imopVar111][k_imopVar112][3] * rho_inv_imopVar136;
                    square[i_imopVar110][j_imopVar111][k_imopVar112] = 0.5 * (u[i_imopVar110][j_imopVar111][k_imopVar112][1] * u[i_imopVar110][j_imopVar111][k_imopVar112][1] + u[i_imopVar110][j_imopVar111][k_imopVar112][2] * u[i_imopVar110][j_imopVar111][k_imopVar112][2] + u[i_imopVar110][j_imopVar111][k_imopVar112][3] * u[i_imopVar110][j_imopVar111][k_imopVar112][3]) * rho_inv_imopVar136;
                    qs[i_imopVar110][j_imopVar111][k_imopVar112] = square[i_imopVar110][j_imopVar111][k_imopVar112] * rho_inv_imopVar136;
                }
            }
        }
#pragma omp for nowait
        for (i_imopVar110 = 0; i_imopVar110 < grid_points[0]; i_imopVar110++) {
            for (j_imopVar111 = 0; j_imopVar111 < grid_points[1]; j_imopVar111++) {
                for (k_imopVar112 = 0; k_imopVar112 < grid_points[2]; k_imopVar112++) {
                    for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = forcing[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113];
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
        for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
            for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                    uijk_imopVar137 = us[i_imopVar110][j_imopVar111][k_imopVar112];
                    up1_imopVar138 = us[i_imopVar110 + 1][j_imopVar111][k_imopVar112];
                    um1_imopVar139 = us[i_imopVar110 - 1][j_imopVar111][k_imopVar112];
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][0] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][0] + dx1tx1 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][0] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][0] + u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][0]) - tx2 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][1] - u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][1]);
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][1] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][1] + dx2tx1 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][1] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][1] + u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][1]) + xxcon2 * con43 * (up1_imopVar138 - 2.0 * uijk_imopVar137 + um1_imopVar139) - tx2 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][1] * up1_imopVar138 - u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][1] * um1_imopVar139 + (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][4] - square[i_imopVar110 + 1][j_imopVar111][k_imopVar112] - u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][4] + square[i_imopVar110 - 1][j_imopVar111][k_imopVar112]) * c2);
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][2] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][2] + dx3tx1 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][2] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][2] + u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][2]) + xxcon2 * (vs[i_imopVar110 + 1][j_imopVar111][k_imopVar112] - 2.0 * vs[i_imopVar110][j_imopVar111][k_imopVar112] + vs[i_imopVar110 - 1][j_imopVar111][k_imopVar112]) - tx2 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][2] * up1_imopVar138 - u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][2] * um1_imopVar139);
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][3] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][3] + dx4tx1 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][3] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][3] + u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][3]) + xxcon2 * (ws[i_imopVar110 + 1][j_imopVar111][k_imopVar112] - 2.0 * ws[i_imopVar110][j_imopVar111][k_imopVar112] + ws[i_imopVar110 - 1][j_imopVar111][k_imopVar112]) - tx2 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][3] * up1_imopVar138 - u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][3] * um1_imopVar139);
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][4] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][4] + dx5tx1 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][4] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][4] + u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][4]) + xxcon3 * (qs[i_imopVar110 + 1][j_imopVar111][k_imopVar112] - 2.0 * qs[i_imopVar110][j_imopVar111][k_imopVar112] + qs[i_imopVar110 - 1][j_imopVar111][k_imopVar112]) + xxcon4 * (up1_imopVar138 * up1_imopVar138 - 2.0 * uijk_imopVar137 * uijk_imopVar137 + um1_imopVar139 * um1_imopVar139) + xxcon5 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][4] * rho_i[i_imopVar110 + 1][j_imopVar111][k_imopVar112] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][4] * rho_i[i_imopVar110][j_imopVar111][k_imopVar112] + u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][4] * rho_i[i_imopVar110 - 1][j_imopVar111][k_imopVar112]) - tx2 * ((c1 * u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][4] - c2 * square[i_imopVar110 + 1][j_imopVar111][k_imopVar112]) * up1_imopVar138 - (c1 * u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][4] - c2 * square[i_imopVar110 - 1][j_imopVar111][k_imopVar112]) * um1_imopVar139);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        i_imopVar110 = 1;
#pragma omp for nowait
        for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
            for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (5.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][m_imopVar113] + u[i_imopVar110 + 2][j_imopVar111][k_imopVar112][m_imopVar113]);
                }
            }
        }
        i_imopVar110 = 2;
#pragma omp for nowait
        for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
            for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (-4.0 * u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][m_imopVar113] + u[i_imopVar110 + 2][j_imopVar111][k_imopVar112][m_imopVar113]);
                }
            }
        }
#pragma omp for nowait
        for (i_imopVar110 = 3; i_imopVar110 < grid_points[0] - 3; i_imopVar110++) {
            for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                    for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110 - 2][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][m_imopVar113] + u[i_imopVar110 + 2][j_imopVar111][k_imopVar112][m_imopVar113]);
                    }
                }
            }
        }
        i_imopVar110 = grid_points[0] - 3;
#pragma omp for nowait
        for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
            for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110 - 2][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][m_imopVar113]);
                }
            }
        }
        i_imopVar110 = grid_points[0] - 2;
#pragma omp for nowait
        for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
            for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110 - 2][j_imopVar111][k_imopVar112][m_imopVar113] - 4. * u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][m_imopVar113] + 5.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113]);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
        for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
            for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                    vijk_imopVar140 = vs[i_imopVar110][j_imopVar111][k_imopVar112];
                    vp1_imopVar141 = vs[i_imopVar110][j_imopVar111 + 1][k_imopVar112];
                    vm1_imopVar142 = vs[i_imopVar110][j_imopVar111 - 1][k_imopVar112];
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][0] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][0] + dy1ty1 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][0] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][0] + u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][0]) - ty2 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][2] - u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][2]);
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][1] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][1] + dy2ty1 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][1] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][1] + u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][1]) + yycon2 * (us[i_imopVar110][j_imopVar111 + 1][k_imopVar112] - 2.0 * us[i_imopVar110][j_imopVar111][k_imopVar112] + us[i_imopVar110][j_imopVar111 - 1][k_imopVar112]) - ty2 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][1] * vp1_imopVar141 - u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][1] * vm1_imopVar142);
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][2] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][2] + dy3ty1 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][2] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][2] + u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][2]) + yycon2 * con43 * (vp1_imopVar141 - 2.0 * vijk_imopVar140 + vm1_imopVar142) - ty2 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][2] * vp1_imopVar141 - u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][2] * vm1_imopVar142 + (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][4] - square[i_imopVar110][j_imopVar111 + 1][k_imopVar112] - u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][4] + square[i_imopVar110][j_imopVar111 - 1][k_imopVar112]) * c2);
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][3] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][3] + dy4ty1 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][3] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][3] + u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][3]) + yycon2 * (ws[i_imopVar110][j_imopVar111 + 1][k_imopVar112] - 2.0 * ws[i_imopVar110][j_imopVar111][k_imopVar112] + ws[i_imopVar110][j_imopVar111 - 1][k_imopVar112]) - ty2 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][3] * vp1_imopVar141 - u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][3] * vm1_imopVar142);
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][4] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][4] + dy5ty1 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][4] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][4] + u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][4]) + yycon3 * (qs[i_imopVar110][j_imopVar111 + 1][k_imopVar112] - 2.0 * qs[i_imopVar110][j_imopVar111][k_imopVar112] + qs[i_imopVar110][j_imopVar111 - 1][k_imopVar112]) + yycon4 * (vp1_imopVar141 * vp1_imopVar141 - 2.0 * vijk_imopVar140 * vijk_imopVar140 + vm1_imopVar142 * vm1_imopVar142) + yycon5 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][4] * rho_i[i_imopVar110][j_imopVar111 + 1][k_imopVar112] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][4] * rho_i[i_imopVar110][j_imopVar111][k_imopVar112] + u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][4] * rho_i[i_imopVar110][j_imopVar111 - 1][k_imopVar112]) - ty2 * ((c1 * u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][4] - c2 * square[i_imopVar110][j_imopVar111 + 1][k_imopVar112]) * vp1_imopVar141 - (c1 * u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][4] - c2 * square[i_imopVar110][j_imopVar111 - 1][k_imopVar112]) * vm1_imopVar142);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        j_imopVar111 = 1;
#pragma omp for nowait
        for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
            for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (5.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][m_imopVar113] + u[i_imopVar110][j_imopVar111 + 2][k_imopVar112][m_imopVar113]);
                }
            }
        }
        j_imopVar111 = 2;
#pragma omp for nowait
        for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
            for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (-4.0 * u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][m_imopVar113] + u[i_imopVar110][j_imopVar111 + 2][k_imopVar112][m_imopVar113]);
                }
            }
        }
#pragma omp for nowait
        for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
            for (j_imopVar111 = 3; j_imopVar111 < grid_points[1] - 3; j_imopVar111++) {
                for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                    for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110][j_imopVar111 - 2][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][m_imopVar113] + u[i_imopVar110][j_imopVar111 + 2][k_imopVar112][m_imopVar113]);
                    }
                }
            }
        }
        j_imopVar111 = grid_points[1] - 3;
#pragma omp for nowait
        for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
            for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110][j_imopVar111 - 2][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][m_imopVar113]);
                }
            }
        }
        j_imopVar111 = grid_points[1] - 2;
#pragma omp for nowait
        for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
            for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110][j_imopVar111 - 2][k_imopVar112][m_imopVar113] - 4. * u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][m_imopVar113] + 5. * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113]);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
        for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
            for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                    wijk_imopVar143 = ws[i_imopVar110][j_imopVar111][k_imopVar112];
                    wp1_imopVar144 = ws[i_imopVar110][j_imopVar111][k_imopVar112 + 1];
                    wm1_imopVar145 = ws[i_imopVar110][j_imopVar111][k_imopVar112 - 1];
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][0] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][0] + dz1tz1 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][0] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][0] + u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][0]) - tz2 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][3] - u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][3]);
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][1] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][1] + dz2tz1 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][1] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][1] + u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][1]) + zzcon2 * (us[i_imopVar110][j_imopVar111][k_imopVar112 + 1] - 2.0 * us[i_imopVar110][j_imopVar111][k_imopVar112] + us[i_imopVar110][j_imopVar111][k_imopVar112 - 1]) - tz2 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][1] * wp1_imopVar144 - u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][1] * wm1_imopVar145);
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][2] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][2] + dz3tz1 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][2] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][2] + u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][2]) + zzcon2 * (vs[i_imopVar110][j_imopVar111][k_imopVar112 + 1] - 2.0 * vs[i_imopVar110][j_imopVar111][k_imopVar112] + vs[i_imopVar110][j_imopVar111][k_imopVar112 - 1]) - tz2 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][2] * wp1_imopVar144 - u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][2] * wm1_imopVar145);
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][3] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][3] + dz4tz1 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][3] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][3] + u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][3]) + zzcon2 * con43 * (wp1_imopVar144 - 2.0 * wijk_imopVar143 + wm1_imopVar145) - tz2 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][3] * wp1_imopVar144 - u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][3] * wm1_imopVar145 + (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][4] - square[i_imopVar110][j_imopVar111][k_imopVar112 + 1] - u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][4] + square[i_imopVar110][j_imopVar111][k_imopVar112 - 1]) * c2);
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][4] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][4] + dz5tz1 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][4] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][4] + u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][4]) + zzcon3 * (qs[i_imopVar110][j_imopVar111][k_imopVar112 + 1] - 2.0 * qs[i_imopVar110][j_imopVar111][k_imopVar112] + qs[i_imopVar110][j_imopVar111][k_imopVar112 - 1]) + zzcon4 * (wp1_imopVar144 * wp1_imopVar144 - 2.0 * wijk_imopVar143 * wijk_imopVar143 + wm1_imopVar145 * wm1_imopVar145) + zzcon5 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][4] * rho_i[i_imopVar110][j_imopVar111][k_imopVar112 + 1] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][4] * rho_i[i_imopVar110][j_imopVar111][k_imopVar112] + u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][4] * rho_i[i_imopVar110][j_imopVar111][k_imopVar112 - 1]) - tz2 * ((c1 * u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][4] - c2 * square[i_imopVar110][j_imopVar111][k_imopVar112 + 1]) * wp1_imopVar144 - (c1 * u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][4] - c2 * square[i_imopVar110][j_imopVar111][k_imopVar112 - 1]) * wm1_imopVar145);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        k_imopVar112 = 1;
#pragma omp for nowait
        for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
            for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (5.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][m_imopVar113] + u[i_imopVar110][j_imopVar111][k_imopVar112 + 2][m_imopVar113]);
                }
            }
        }
        k_imopVar112 = 2;
#pragma omp for nowait
        for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
            for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (-4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][m_imopVar113] + u[i_imopVar110][j_imopVar111][k_imopVar112 + 2][m_imopVar113]);
                }
            }
        }
#pragma omp for nowait
        for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
            for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                for (k_imopVar112 = 3; k_imopVar112 < grid_points[2] - 3; k_imopVar112++) {
                    for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110][j_imopVar111][k_imopVar112 - 2][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][m_imopVar113] + u[i_imopVar110][j_imopVar111][k_imopVar112 + 2][m_imopVar113]);
                    }
                }
            }
        }
        k_imopVar112 = grid_points[2] - 3;
#pragma omp for nowait
        for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
            for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110][j_imopVar111][k_imopVar112 - 2][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][m_imopVar113]);
                }
            }
        }
        k_imopVar112 = grid_points[2] - 2;
#pragma omp for nowait
        for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
            for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110][j_imopVar111][k_imopVar112 - 2][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][m_imopVar113] + 5.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113]);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
        for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
            for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                    for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] * dt;
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        int i_imopVar120;
        int j_imopVar121;
        int k_imopVar122;
#pragma omp for nowait
        for (j_imopVar121 = 1; j_imopVar121 < grid_points[1] - 1; j_imopVar121++) {
            for (k_imopVar122 = 1; k_imopVar122 < grid_points[2] - 1; k_imopVar122++) {
                for (i_imopVar120 = 0; i_imopVar120 < grid_points[0]; i_imopVar120++) {
                    tmp1 = 1.0 / u[i_imopVar120][j_imopVar121][k_imopVar122][0];
                    tmp2 = tmp1 * tmp1;
                    tmp3 = tmp1 * tmp2;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][0][0] = 0.0;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][0][1] = 1.0;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][0][2] = 0.0;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][0][3] = 0.0;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][0][4] = 0.0;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][1][0] = -(u[i_imopVar120][j_imopVar121][k_imopVar122][1] * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][1]) + c2 * 0.50 * (u[i_imopVar120][j_imopVar121][k_imopVar122][1] * u[i_imopVar120][j_imopVar121][k_imopVar122][1] + u[i_imopVar120][j_imopVar121][k_imopVar122][2] * u[i_imopVar120][j_imopVar121][k_imopVar122][2] + u[i_imopVar120][j_imopVar121][k_imopVar122][3] * u[i_imopVar120][j_imopVar121][k_imopVar122][3]) * tmp2;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][1][1] = (2.0 - c2) * (u[i_imopVar120][j_imopVar121][k_imopVar122][1] / u[i_imopVar120][j_imopVar121][k_imopVar122][0]);
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][1][2] = -c2 * (u[i_imopVar120][j_imopVar121][k_imopVar122][2] * tmp1);
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][1][3] = -c2 * (u[i_imopVar120][j_imopVar121][k_imopVar122][3] * tmp1);
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][1][4] = c2;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][2][0] = -(u[i_imopVar120][j_imopVar121][k_imopVar122][1] * u[i_imopVar120][j_imopVar121][k_imopVar122][2]) * tmp2;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][2][1] = u[i_imopVar120][j_imopVar121][k_imopVar122][2] * tmp1;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][2][2] = u[i_imopVar120][j_imopVar121][k_imopVar122][1] * tmp1;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][2][3] = 0.0;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][2][4] = 0.0;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][3][0] = -(u[i_imopVar120][j_imopVar121][k_imopVar122][1] * u[i_imopVar120][j_imopVar121][k_imopVar122][3]) * tmp2;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][3][1] = u[i_imopVar120][j_imopVar121][k_imopVar122][3] * tmp1;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][3][2] = 0.0;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][3][3] = u[i_imopVar120][j_imopVar121][k_imopVar122][1] * tmp1;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][3][4] = 0.0;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][4][0] = (c2 * (u[i_imopVar120][j_imopVar121][k_imopVar122][1] * u[i_imopVar120][j_imopVar121][k_imopVar122][1] + u[i_imopVar120][j_imopVar121][k_imopVar122][2] * u[i_imopVar120][j_imopVar121][k_imopVar122][2] + u[i_imopVar120][j_imopVar121][k_imopVar122][3] * u[i_imopVar120][j_imopVar121][k_imopVar122][3]) * tmp2 - c1 * (u[i_imopVar120][j_imopVar121][k_imopVar122][4] * tmp1)) * (u[i_imopVar120][j_imopVar121][k_imopVar122][1] * tmp1);
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][4][1] = c1 * u[i_imopVar120][j_imopVar121][k_imopVar122][4] * tmp1 - 0.50 * c2 * (3.0 * u[i_imopVar120][j_imopVar121][k_imopVar122][1] * u[i_imopVar120][j_imopVar121][k_imopVar122][1] + u[i_imopVar120][j_imopVar121][k_imopVar122][2] * u[i_imopVar120][j_imopVar121][k_imopVar122][2] + u[i_imopVar120][j_imopVar121][k_imopVar122][3] * u[i_imopVar120][j_imopVar121][k_imopVar122][3]) * tmp2;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][4][2] = -c2 * (u[i_imopVar120][j_imopVar121][k_imopVar122][2] * u[i_imopVar120][j_imopVar121][k_imopVar122][1]) * tmp2;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][4][3] = -c2 * (u[i_imopVar120][j_imopVar121][k_imopVar122][3] * u[i_imopVar120][j_imopVar121][k_imopVar122][1]) * tmp2;
                    fjac[i_imopVar120][j_imopVar121][k_imopVar122][4][4] = c1 * (u[i_imopVar120][j_imopVar121][k_imopVar122][1] * tmp1);
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][0][0] = 0.0;
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][0][1] = 0.0;
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][0][2] = 0.0;
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][0][3] = 0.0;
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][0][4] = 0.0;
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][1][0] = -con43 * c3c4 * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][1];
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][1][1] = con43 * c3c4 * tmp1;
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][1][2] = 0.0;
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][1][3] = 0.0;
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][1][4] = 0.0;
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][2][0] = -c3c4 * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][2];
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][2][1] = 0.0;
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][2][2] = c3c4 * tmp1;
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][2][3] = 0.0;
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][2][4] = 0.0;
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][3][0] = -c3c4 * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][3];
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][3][1] = 0.0;
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][3][2] = 0.0;
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][3][3] = c3c4 * tmp1;
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][3][4] = 0.0;
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][4][0] = -(con43 * c3c4 - c1345) * tmp3 * (((u[i_imopVar120][j_imopVar121][k_imopVar122][1]) * (u[i_imopVar120][j_imopVar121][k_imopVar122][1]))) - (c3c4 - c1345) * tmp3 * (((u[i_imopVar120][j_imopVar121][k_imopVar122][2]) * (u[i_imopVar120][j_imopVar121][k_imopVar122][2]))) - (c3c4 - c1345) * tmp3 * (((u[i_imopVar120][j_imopVar121][k_imopVar122][3]) * (u[i_imopVar120][j_imopVar121][k_imopVar122][3]))) - c1345 * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][4];
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][4][1] = (con43 * c3c4 - c1345) * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][1];
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][4][2] = (c3c4 - c1345) * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][2];
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][4][3] = (c3c4 - c1345) * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][3];
                    njac[i_imopVar120][j_imopVar121][k_imopVar122][4][4] = c1345 * tmp1;
                }
                for (i_imopVar120 = 1; i_imopVar120 < grid_points[0] - 1; i_imopVar120++) {
                    tmp1 = dt * tx1;
                    tmp2 = dt * tx2;
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][0][0] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][0] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][0] - tmp1 * dx1;
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][0][1] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][1] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][1];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][0][2] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][2] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][2];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][0][3] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][3] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][3];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][0][4] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][4] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][4];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][1][0] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][0] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][0];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][1][1] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][1] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][1] - tmp1 * dx2;
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][1][2] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][2] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][2];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][1][3] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][3] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][3];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][1][4] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][4] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][4];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][2][0] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][0] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][0];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][2][1] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][1] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][1];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][2][2] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][2] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][2] - tmp1 * dx3;
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][2][3] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][3] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][3];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][2][4] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][4] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][4];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][3][0] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][0] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][0];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][3][1] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][1] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][1];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][3][2] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][2] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][2];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][3][3] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][3] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][3] - tmp1 * dx4;
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][3][4] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][4] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][4];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][4][0] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][0] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][0];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][4][1] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][1] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][1];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][4][2] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][2] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][2];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][4][3] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][3] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][3];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][4][4] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][4] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][4] - tmp1 * dx5;
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][0][0] = 1.0 + tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][0][0] + tmp1 * 2.0 * dx1;
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][0][1] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][0][1];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][0][2] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][0][2];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][0][3] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][0][3];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][0][4] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][0][4];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][1][0] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][1][0];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][1][1] = 1.0 + tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][1][1] + tmp1 * 2.0 * dx2;
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][1][2] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][1][2];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][1][3] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][1][3];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][1][4] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][1][4];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][2][0] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][2][0];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][2][1] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][2][1];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][2][2] = 1.0 + tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][2][2] + tmp1 * 2.0 * dx3;
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][2][3] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][2][3];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][2][4] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][2][4];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][3][0] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][3][0];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][3][1] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][3][1];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][3][2] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][3][2];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][3][3] = 1.0 + tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][3][3] + tmp1 * 2.0 * dx4;
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][3][4] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][3][4];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][4][0] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][4][0];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][4][1] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][4][1];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][4][2] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][4][2];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][4][3] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][4][3];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][4][4] = 1.0 + tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][4][4] + tmp1 * 2.0 * dx5;
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][0][0] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][0] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][0] - tmp1 * dx1;
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][0][1] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][1] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][1];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][0][2] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][2] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][2];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][0][3] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][3] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][3];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][0][4] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][4] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][4];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][1][0] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][0] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][0];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][1][1] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][1] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][1] - tmp1 * dx2;
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][1][2] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][2] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][2];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][1][3] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][3] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][3];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][1][4] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][4] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][4];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][2][0] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][0] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][0];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][2][1] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][1] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][1];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][2][2] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][2] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][2] - tmp1 * dx3;
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][2][3] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][3] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][3];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][2][4] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][4] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][4];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][3][0] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][0] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][0];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][3][1] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][1] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][1];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][3][2] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][2] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][2];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][3][3] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][3] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][3] - tmp1 * dx4;
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][3][4] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][4] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][4];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][4][0] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][0] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][0];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][4][1] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][1] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][1];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][4][2] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][2] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][2];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][4][3] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][3] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][3];
                    lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][4][4] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][4] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][4] - tmp1 * dx5;
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        int i_imopVar114;
        int j_imopVar115;
        int k_imopVar116;
        int isize;
        isize = grid_points[0] - 1;
#pragma omp for nowait
        for (j_imopVar115 = 1; j_imopVar115 < grid_points[1] - 1; j_imopVar115++) {
            for (k_imopVar116 = 1; k_imopVar116 < grid_points[2] - 1; k_imopVar116++) {
                double ( *_imopVarPre338 );
                double ( *_imopVarPre339 )[5];
                double ( *_imopVarPre340 )[5];
                _imopVarPre338 = rhs[0][j_imopVar115][k_imopVar116];
                _imopVarPre339 = lhs[0][j_imopVar115][k_imopVar116][2];
                _imopVarPre340 = lhs[0][j_imopVar115][k_imopVar116][1];
                binvcrhs(_imopVarPre340, _imopVarPre339, _imopVarPre338);
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        for (i_imopVar114 = 1; i_imopVar114 < isize; i_imopVar114++) {
#pragma omp for nowait
            for (j_imopVar115 = 1; j_imopVar115 < grid_points[1] - 1; j_imopVar115++) {
                for (k_imopVar116 = 1; k_imopVar116 < grid_points[2] - 1; k_imopVar116++) {
                    double ( *_imopVarPre344 );
                    double ( *_imopVarPre345 );
                    double ( *_imopVarPre346 )[5];
                    _imopVarPre344 = rhs[i_imopVar114][j_imopVar115][k_imopVar116];
                    _imopVarPre345 = rhs[i_imopVar114 - 1][j_imopVar115][k_imopVar116];
                    _imopVarPre346 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][0];
                    matvec_sub(_imopVarPre346, _imopVarPre345, _imopVarPre344);
                    double ( *_imopVarPre350 )[5];
                    double ( *_imopVarPre351 )[5];
                    double ( *_imopVarPre352 )[5];
                    _imopVarPre350 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][1];
                    _imopVarPre351 = lhs[i_imopVar114 - 1][j_imopVar115][k_imopVar116][2];
                    _imopVarPre352 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][0];
                    matmul_sub(_imopVarPre352, _imopVarPre351, _imopVarPre350);
                    double ( *_imopVarPre356 );
                    double ( *_imopVarPre357 )[5];
                    double ( *_imopVarPre358 )[5];
                    _imopVarPre356 = rhs[i_imopVar114][j_imopVar115][k_imopVar116];
                    _imopVarPre357 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][2];
                    _imopVarPre358 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][1];
                    binvcrhs(_imopVarPre358, _imopVarPre357, _imopVarPre356);
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        }
#pragma omp for nowait
        for (j_imopVar115 = 1; j_imopVar115 < grid_points[1] - 1; j_imopVar115++) {
            for (k_imopVar116 = 1; k_imopVar116 < grid_points[2] - 1; k_imopVar116++) {
                double ( *_imopVarPre362 );
                double ( *_imopVarPre363 );
                double ( *_imopVarPre364 )[5];
                _imopVarPre362 = rhs[isize][j_imopVar115][k_imopVar116];
                _imopVarPre363 = rhs[isize - 1][j_imopVar115][k_imopVar116];
                _imopVarPre364 = lhs[isize][j_imopVar115][k_imopVar116][0];
                matvec_sub(_imopVarPre364, _imopVarPre363, _imopVarPre362);
                double ( *_imopVarPre368 )[5];
                double ( *_imopVarPre369 )[5];
                double ( *_imopVarPre370 )[5];
                _imopVarPre368 = lhs[isize][j_imopVar115][k_imopVar116][1];
                _imopVarPre369 = lhs[isize - 1][j_imopVar115][k_imopVar116][2];
                _imopVarPre370 = lhs[isize][j_imopVar115][k_imopVar116][0];
                matmul_sub(_imopVarPre370, _imopVarPre369, _imopVarPre368);
                double ( *_imopVarPre373 );
                double ( *_imopVarPre374 )[5];
                _imopVarPre373 = rhs[i_imopVar114][j_imopVar115][k_imopVar116];
                _imopVarPre374 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][1];
                binvrhs(_imopVarPre374, _imopVarPre373);
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        int i_imopVar117;
        int j_imopVar118;
        int k_imopVar119;
        int m_imopVar123;
        int n_imopVar124;
        for (i_imopVar117 = grid_points[0] - 2; i_imopVar117 >= 0; i_imopVar117--) {
#pragma omp for nowait
            for (j_imopVar118 = 1; j_imopVar118 < grid_points[1] - 1; j_imopVar118++) {
                for (k_imopVar119 = 1; k_imopVar119 < grid_points[2] - 1; k_imopVar119++) {
                    for (m_imopVar123 = 0; m_imopVar123 < 5; m_imopVar123++) {
                        for (n_imopVar124 = 0; n_imopVar124 < 5; n_imopVar124++) {
                            rhs[i_imopVar117][j_imopVar118][k_imopVar119][m_imopVar123] = rhs[i_imopVar117][j_imopVar118][k_imopVar119][m_imopVar123] - lhs[i_imopVar117][j_imopVar118][k_imopVar119][2][m_imopVar123][n_imopVar124] * rhs[i_imopVar117 + 1][j_imopVar118][k_imopVar119][n_imopVar124];
                        }
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        int i_imopVar85;
        int j_imopVar86;
        int k_imopVar87;
#pragma omp for nowait
        for (i_imopVar85 = 1; i_imopVar85 < grid_points[0] - 1; i_imopVar85++) {
            for (j_imopVar86 = 0; j_imopVar86 < grid_points[1]; j_imopVar86++) {
                for (k_imopVar87 = 1; k_imopVar87 < grid_points[2] - 1; k_imopVar87++) {
                    tmp1 = 1.0 / u[i_imopVar85][j_imopVar86][k_imopVar87][0];
                    tmp2 = tmp1 * tmp1;
                    tmp3 = tmp1 * tmp2;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][0][0] = 0.0;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][0][1] = 0.0;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][0][2] = 1.0;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][0][3] = 0.0;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][0][4] = 0.0;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][1][0] = -(u[i_imopVar85][j_imopVar86][k_imopVar87][1] * u[i_imopVar85][j_imopVar86][k_imopVar87][2]) * tmp2;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][1][1] = u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp1;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][1][2] = u[i_imopVar85][j_imopVar86][k_imopVar87][1] * tmp1;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][1][3] = 0.0;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][1][4] = 0.0;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][2][0] = -(u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp2) + 0.50 * c2 * ((u[i_imopVar85][j_imopVar86][k_imopVar87][1] * u[i_imopVar85][j_imopVar86][k_imopVar87][1] + u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][2] + u[i_imopVar85][j_imopVar86][k_imopVar87][3] * u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * tmp2);
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][2][1] = -c2 * u[i_imopVar85][j_imopVar86][k_imopVar87][1] * tmp1;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][2][2] = (2.0 - c2) * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp1;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][2][3] = -c2 * u[i_imopVar85][j_imopVar86][k_imopVar87][3] * tmp1;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][2][4] = c2;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][3][0] = -(u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * tmp2;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][3][1] = 0.0;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][3][2] = u[i_imopVar85][j_imopVar86][k_imopVar87][3] * tmp1;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][3][3] = u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp1;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][3][4] = 0.0;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][4][0] = (c2 * (u[i_imopVar85][j_imopVar86][k_imopVar87][1] * u[i_imopVar85][j_imopVar86][k_imopVar87][1] + u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][2] + u[i_imopVar85][j_imopVar86][k_imopVar87][3] * u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * tmp2 - c1 * u[i_imopVar85][j_imopVar86][k_imopVar87][4] * tmp1) * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp1;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][4][1] = -c2 * u[i_imopVar85][j_imopVar86][k_imopVar87][1] * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp2;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][4][2] = c1 * u[i_imopVar85][j_imopVar86][k_imopVar87][4] * tmp1 - 0.50 * c2 * ((u[i_imopVar85][j_imopVar86][k_imopVar87][1] * u[i_imopVar85][j_imopVar86][k_imopVar87][1] + 3.0 * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][2] + u[i_imopVar85][j_imopVar86][k_imopVar87][3] * u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * tmp2);
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][4][3] = -c2 * (u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * tmp2;
                    fjac[i_imopVar85][j_imopVar86][k_imopVar87][4][4] = c1 * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp1;
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][0][0] = 0.0;
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][0][1] = 0.0;
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][0][2] = 0.0;
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][0][3] = 0.0;
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][0][4] = 0.0;
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][1][0] = -c3c4 * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][1];
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][1][1] = c3c4 * tmp1;
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][1][2] = 0.0;
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][1][3] = 0.0;
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][1][4] = 0.0;
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][2][0] = -con43 * c3c4 * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][2];
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][2][1] = 0.0;
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][2][2] = con43 * c3c4 * tmp1;
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][2][3] = 0.0;
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][2][4] = 0.0;
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][3][0] = -c3c4 * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][3];
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][3][1] = 0.0;
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][3][2] = 0.0;
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][3][3] = c3c4 * tmp1;
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][3][4] = 0.0;
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][4][0] = -(c3c4 - c1345) * tmp3 * (((u[i_imopVar85][j_imopVar86][k_imopVar87][1]) * (u[i_imopVar85][j_imopVar86][k_imopVar87][1]))) - (con43 * c3c4 - c1345) * tmp3 * (((u[i_imopVar85][j_imopVar86][k_imopVar87][2]) * (u[i_imopVar85][j_imopVar86][k_imopVar87][2]))) - (c3c4 - c1345) * tmp3 * (((u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * (u[i_imopVar85][j_imopVar86][k_imopVar87][3]))) - c1345 * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][4];
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][4][1] = (c3c4 - c1345) * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][1];
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][4][2] = (con43 * c3c4 - c1345) * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][2];
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][4][3] = (c3c4 - c1345) * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][3];
                    njac[i_imopVar85][j_imopVar86][k_imopVar87][4][4] = c1345 * tmp1;
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
        for (i_imopVar85 = 1; i_imopVar85 < grid_points[0] - 1; i_imopVar85++) {
            for (j_imopVar86 = 1; j_imopVar86 < grid_points[1] - 1; j_imopVar86++) {
                for (k_imopVar87 = 1; k_imopVar87 < grid_points[2] - 1; k_imopVar87++) {
                    tmp1 = dt * ty1;
                    tmp2 = dt * ty2;
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][0][0] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][0] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][0] - tmp1 * dy1;
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][0][1] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][1] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][1];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][0][2] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][2] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][2];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][0][3] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][3] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][3];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][0][4] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][4] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][4];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][1][0] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][0] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][0];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][1][1] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][1] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][1] - tmp1 * dy2;
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][1][2] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][2] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][2];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][1][3] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][3] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][3];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][1][4] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][4] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][4];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][2][0] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][0] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][0];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][2][1] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][1] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][1];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][2][2] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][2] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][2] - tmp1 * dy3;
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][2][3] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][3] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][3];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][2][4] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][4] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][4];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][3][0] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][0] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][0];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][3][1] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][1] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][1];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][3][2] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][2] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][2];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][3][3] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][3] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][3] - tmp1 * dy4;
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][3][4] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][4] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][4];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][4][0] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][0] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][0];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][4][1] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][1] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][1];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][4][2] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][2] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][2];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][4][3] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][3] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][3];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][4][4] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][4] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][4] - tmp1 * dy5;
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][0][0] = 1.0 + tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][0][0] + tmp1 * 2.0 * dy1;
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][0][1] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][0][1];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][0][2] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][0][2];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][0][3] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][0][3];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][0][4] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][0][4];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][1][0] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][1][0];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][1][1] = 1.0 + tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][1][1] + tmp1 * 2.0 * dy2;
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][1][2] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][1][2];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][1][3] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][1][3];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][1][4] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][1][4];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][2][0] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][2][0];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][2][1] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][2][1];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][2][2] = 1.0 + tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][2][2] + tmp1 * 2.0 * dy3;
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][2][3] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][2][3];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][2][4] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][2][4];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][3][0] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][3][0];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][3][1] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][3][1];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][3][2] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][3][2];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][3][3] = 1.0 + tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][3][3] + tmp1 * 2.0 * dy4;
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][3][4] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][3][4];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][4][0] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][4][0];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][4][1] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][4][1];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][4][2] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][4][2];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][4][3] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][4][3];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][4][4] = 1.0 + tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][4][4] + tmp1 * 2.0 * dy5;
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][0][0] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][0] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][0] - tmp1 * dy1;
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][0][1] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][1] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][1];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][0][2] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][2] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][2];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][0][3] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][3] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][3];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][0][4] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][4] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][4];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][1][0] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][0] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][0];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][1][1] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][1] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][1] - tmp1 * dy2;
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][1][2] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][2] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][2];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][1][3] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][3] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][3];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][1][4] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][4] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][4];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][2][0] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][0] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][0];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][2][1] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][1] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][1];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][2][2] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][2] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][2] - tmp1 * dy3;
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][2][3] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][3] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][3];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][2][4] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][4] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][4];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][3][0] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][0] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][0];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][3][1] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][1] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][1];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][3][2] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][2] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][2];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][3][3] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][3] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][3] - tmp1 * dy4;
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][3][4] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][4] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][4];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][4][0] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][0] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][0];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][4][1] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][1] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][1];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][4][2] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][2] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][2];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][4][3] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][3] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][3];
                    lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][4][4] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][4] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][4] - tmp1 * dy5;
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        int i_imopVar146;
        int j_imopVar147;
        int k_imopVar148;
        int jsize;
        jsize = grid_points[1] - 1;
#pragma omp for nowait
        for (i_imopVar146 = 1; i_imopVar146 < grid_points[0] - 1; i_imopVar146++) {
            for (k_imopVar148 = 1; k_imopVar148 < grid_points[2] - 1; k_imopVar148++) {
                double ( *_imopVarPre378 );
                double ( *_imopVarPre379 )[5];
                double ( *_imopVarPre380 )[5];
                _imopVarPre378 = rhs[i_imopVar146][0][k_imopVar148];
                _imopVarPre379 = lhs[i_imopVar146][0][k_imopVar148][2];
                _imopVarPre380 = lhs[i_imopVar146][0][k_imopVar148][1];
                binvcrhs(_imopVarPre380, _imopVarPre379, _imopVarPre378);
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        for (j_imopVar147 = 1; j_imopVar147 < jsize; j_imopVar147++) {
#pragma omp for nowait
            for (i_imopVar146 = 1; i_imopVar146 < grid_points[0] - 1; i_imopVar146++) {
                for (k_imopVar148 = 1; k_imopVar148 < grid_points[2] - 1; k_imopVar148++) {
                    double ( *_imopVarPre384 );
                    double ( *_imopVarPre385 );
                    double ( *_imopVarPre386 )[5];
                    _imopVarPre384 = rhs[i_imopVar146][j_imopVar147][k_imopVar148];
                    _imopVarPre385 = rhs[i_imopVar146][j_imopVar147 - 1][k_imopVar148];
                    _imopVarPre386 = lhs[i_imopVar146][j_imopVar147][k_imopVar148][0];
                    matvec_sub(_imopVarPre386, _imopVarPre385, _imopVarPre384);
                    double ( *_imopVarPre390 )[5];
                    double ( *_imopVarPre391 )[5];
                    double ( *_imopVarPre392 )[5];
                    _imopVarPre390 = lhs[i_imopVar146][j_imopVar147][k_imopVar148][1];
                    _imopVarPre391 = lhs[i_imopVar146][j_imopVar147 - 1][k_imopVar148][2];
                    _imopVarPre392 = lhs[i_imopVar146][j_imopVar147][k_imopVar148][0];
                    matmul_sub(_imopVarPre392, _imopVarPre391, _imopVarPre390);
                    double ( *_imopVarPre396 );
                    double ( *_imopVarPre397 )[5];
                    double ( *_imopVarPre398 )[5];
                    _imopVarPre396 = rhs[i_imopVar146][j_imopVar147][k_imopVar148];
                    _imopVarPre397 = lhs[i_imopVar146][j_imopVar147][k_imopVar148][2];
                    _imopVarPre398 = lhs[i_imopVar146][j_imopVar147][k_imopVar148][1];
                    binvcrhs(_imopVarPre398, _imopVarPre397, _imopVarPre396);
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        }
#pragma omp for nowait
        for (i_imopVar146 = 1; i_imopVar146 < grid_points[0] - 1; i_imopVar146++) {
            for (k_imopVar148 = 1; k_imopVar148 < grid_points[2] - 1; k_imopVar148++) {
                double ( *_imopVarPre402 );
                double ( *_imopVarPre403 );
                double ( *_imopVarPre404 )[5];
                _imopVarPre402 = rhs[i_imopVar146][jsize][k_imopVar148];
                _imopVarPre403 = rhs[i_imopVar146][jsize - 1][k_imopVar148];
                _imopVarPre404 = lhs[i_imopVar146][jsize][k_imopVar148][0];
                matvec_sub(_imopVarPre404, _imopVarPre403, _imopVarPre402);
                double ( *_imopVarPre408 )[5];
                double ( *_imopVarPre409 )[5];
                double ( *_imopVarPre410 )[5];
                _imopVarPre408 = lhs[i_imopVar146][jsize][k_imopVar148][1];
                _imopVarPre409 = lhs[i_imopVar146][jsize - 1][k_imopVar148][2];
                _imopVarPre410 = lhs[i_imopVar146][jsize][k_imopVar148][0];
                matmul_sub(_imopVarPre410, _imopVarPre409, _imopVarPre408);
                double ( *_imopVarPre413 );
                double ( *_imopVarPre414 )[5];
                _imopVarPre413 = rhs[i_imopVar146][jsize][k_imopVar148];
                _imopVarPre414 = lhs[i_imopVar146][jsize][k_imopVar148][1];
                binvrhs(_imopVarPre414, _imopVarPre413);
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        int i_imopVar82;
        int j_imopVar83;
        int k_imopVar84;
        int m_imopVar149;
        int n;
        for (j_imopVar83 = grid_points[1] - 2; j_imopVar83 >= 0; j_imopVar83--) {
#pragma omp for nowait
            for (i_imopVar82 = 1; i_imopVar82 < grid_points[0] - 1; i_imopVar82++) {
                for (k_imopVar84 = 1; k_imopVar84 < grid_points[2] - 1; k_imopVar84++) {
                    for (m_imopVar149 = 0; m_imopVar149 < 5; m_imopVar149++) {
                        for (n = 0; n < 5; n++) {
                            rhs[i_imopVar82][j_imopVar83][k_imopVar84][m_imopVar149] = rhs[i_imopVar82][j_imopVar83][k_imopVar84][m_imopVar149] - lhs[i_imopVar82][j_imopVar83][k_imopVar84][2][m_imopVar149][n] * rhs[i_imopVar82][j_imopVar83 + 1][k_imopVar84][n];
                        }
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        int i_imopVar128;
        int j_imopVar129;
        int k_imopVar130;
#pragma omp for nowait
        for (i_imopVar128 = 1; i_imopVar128 < grid_points[0] - 1; i_imopVar128++) {
            for (j_imopVar129 = 1; j_imopVar129 < grid_points[1] - 1; j_imopVar129++) {
                for (k_imopVar130 = 0; k_imopVar130 < grid_points[2]; k_imopVar130++) {
                    tmp1 = 1.0 / u[i_imopVar128][j_imopVar129][k_imopVar130][0];
                    tmp2 = tmp1 * tmp1;
                    tmp3 = tmp1 * tmp2;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][0][0] = 0.0;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][0][1] = 0.0;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][0][2] = 0.0;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][0][3] = 1.0;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][0][4] = 0.0;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][1][0] = -(u[i_imopVar128][j_imopVar129][k_imopVar130][1] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][1][1] = u[i_imopVar128][j_imopVar129][k_imopVar130][3] * tmp1;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][1][2] = 0.0;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][1][3] = u[i_imopVar128][j_imopVar129][k_imopVar130][1] * tmp1;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][1][4] = 0.0;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][2][0] = -(u[i_imopVar128][j_imopVar129][k_imopVar130][2] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][2][1] = 0.0;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][2][2] = u[i_imopVar128][j_imopVar129][k_imopVar130][3] * tmp1;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][2][3] = u[i_imopVar128][j_imopVar129][k_imopVar130][2] * tmp1;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][2][4] = 0.0;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][3][0] = -(u[i_imopVar128][j_imopVar129][k_imopVar130][3] * u[i_imopVar128][j_imopVar129][k_imopVar130][3] * tmp2) + 0.50 * c2 * ((u[i_imopVar128][j_imopVar129][k_imopVar130][1] * u[i_imopVar128][j_imopVar129][k_imopVar130][1] + u[i_imopVar128][j_imopVar129][k_imopVar130][2] * u[i_imopVar128][j_imopVar129][k_imopVar130][2] + u[i_imopVar128][j_imopVar129][k_imopVar130][3] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2);
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][3][1] = -c2 * u[i_imopVar128][j_imopVar129][k_imopVar130][1] * tmp1;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][3][2] = -c2 * u[i_imopVar128][j_imopVar129][k_imopVar130][2] * tmp1;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][3][3] = (2.0 - c2) * u[i_imopVar128][j_imopVar129][k_imopVar130][3] * tmp1;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][3][4] = c2;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][4][0] = (c2 * (u[i_imopVar128][j_imopVar129][k_imopVar130][1] * u[i_imopVar128][j_imopVar129][k_imopVar130][1] + u[i_imopVar128][j_imopVar129][k_imopVar130][2] * u[i_imopVar128][j_imopVar129][k_imopVar130][2] + u[i_imopVar128][j_imopVar129][k_imopVar130][3] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2 - c1 * (u[i_imopVar128][j_imopVar129][k_imopVar130][4] * tmp1)) * (u[i_imopVar128][j_imopVar129][k_imopVar130][3] * tmp1);
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][4][1] = -c2 * (u[i_imopVar128][j_imopVar129][k_imopVar130][1] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][4][2] = -c2 * (u[i_imopVar128][j_imopVar129][k_imopVar130][2] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2;
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][4][3] = c1 * (u[i_imopVar128][j_imopVar129][k_imopVar130][4] * tmp1) - 0.50 * c2 * ((u[i_imopVar128][j_imopVar129][k_imopVar130][1] * u[i_imopVar128][j_imopVar129][k_imopVar130][1] + u[i_imopVar128][j_imopVar129][k_imopVar130][2] * u[i_imopVar128][j_imopVar129][k_imopVar130][2] + 3.0 * u[i_imopVar128][j_imopVar129][k_imopVar130][3] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2);
                    fjac[i_imopVar128][j_imopVar129][k_imopVar130][4][4] = c1 * u[i_imopVar128][j_imopVar129][k_imopVar130][3] * tmp1;
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][0][0] = 0.0;
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][0][1] = 0.0;
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][0][2] = 0.0;
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][0][3] = 0.0;
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][0][4] = 0.0;
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][1][0] = -c3c4 * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][1];
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][1][1] = c3c4 * tmp1;
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][1][2] = 0.0;
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][1][3] = 0.0;
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][1][4] = 0.0;
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][2][0] = -c3c4 * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][2];
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][2][1] = 0.0;
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][2][2] = c3c4 * tmp1;
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][2][3] = 0.0;
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][2][4] = 0.0;
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][3][0] = -con43 * c3c4 * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][3];
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][3][1] = 0.0;
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][3][2] = 0.0;
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][3][3] = con43 * c3 * c4 * tmp1;
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][3][4] = 0.0;
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][4][0] = -(c3c4 - c1345) * tmp3 * (((u[i_imopVar128][j_imopVar129][k_imopVar130][1]) * (u[i_imopVar128][j_imopVar129][k_imopVar130][1]))) - (c3c4 - c1345) * tmp3 * (((u[i_imopVar128][j_imopVar129][k_imopVar130][2]) * (u[i_imopVar128][j_imopVar129][k_imopVar130][2]))) - (con43 * c3c4 - c1345) * tmp3 * (((u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * (u[i_imopVar128][j_imopVar129][k_imopVar130][3]))) - c1345 * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][4];
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][4][1] = (c3c4 - c1345) * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][1];
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][4][2] = (c3c4 - c1345) * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][2];
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][4][3] = (con43 * c3c4 - c1345) * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][3];
                    njac[i_imopVar128][j_imopVar129][k_imopVar130][4][4] = c1345 * tmp1;
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
        for (i_imopVar128 = 1; i_imopVar128 < grid_points[0] - 1; i_imopVar128++) {
            for (j_imopVar129 = 1; j_imopVar129 < grid_points[1] - 1; j_imopVar129++) {
                for (k_imopVar130 = 1; k_imopVar130 < grid_points[2] - 1; k_imopVar130++) {
                    tmp1 = dt * tz1;
                    tmp2 = dt * tz2;
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][0][0] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][0] - tmp1 * dz1;
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][0][1] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][1];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][0][2] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][2];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][0][3] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][3];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][0][4] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][4];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][1][0] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][0];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][1][1] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][1] - tmp1 * dz2;
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][1][2] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][2];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][1][3] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][3];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][1][4] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][4];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][2][0] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][0];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][2][1] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][1];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][2][2] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][2] - tmp1 * dz3;
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][2][3] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][3];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][2][4] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][4];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][3][0] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][0];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][3][1] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][1];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][3][2] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][2];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][3][3] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][3] - tmp1 * dz4;
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][3][4] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][4];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][4][0] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][0];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][4][1] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][1];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][4][2] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][2];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][4][3] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][3];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][4][4] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][4] - tmp1 * dz5;
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][0][0] = 1.0 + tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][0][0] + tmp1 * 2.0 * dz1;
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][0][1] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][0][1];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][0][2] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][0][2];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][0][3] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][0][3];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][0][4] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][0][4];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][1][0] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][1][0];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][1][1] = 1.0 + tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][1][1] + tmp1 * 2.0 * dz2;
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][1][2] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][1][2];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][1][3] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][1][3];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][1][4] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][1][4];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][2][0] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][2][0];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][2][1] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][2][1];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][2][2] = 1.0 + tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][2][2] + tmp1 * 2.0 * dz3;
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][2][3] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][2][3];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][2][4] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][2][4];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][3][0] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][3][0];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][3][1] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][3][1];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][3][2] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][3][2];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][3][3] = 1.0 + tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][3][3] + tmp1 * 2.0 * dz4;
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][3][4] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][3][4];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][4][0] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][4][0];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][4][1] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][4][1];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][4][2] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][4][2];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][4][3] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][4][3];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][4][4] = 1.0 + tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][4][4] + tmp1 * 2.0 * dz5;
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][0][0] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][0] - tmp1 * dz1;
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][0][1] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][1];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][0][2] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][2];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][0][3] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][3];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][0][4] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][4];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][1][0] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][0];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][1][1] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][1] - tmp1 * dz2;
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][1][2] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][2];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][1][3] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][3];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][1][4] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][4];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][2][0] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][0];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][2][1] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][1];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][2][2] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][2] - tmp1 * dz3;
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][2][3] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][3];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][2][4] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][4];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][3][0] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][0];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][3][1] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][1];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][3][2] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][2];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][3][3] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][3] - tmp1 * dz4;
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][3][4] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][4];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][4][0] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][0];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][4][1] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][1];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][4][2] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][2];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][4][3] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][3];
                    lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][4][4] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][4] - tmp1 * dz5;
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        int i_imopVar125;
        int j_imopVar126;
        int k_imopVar127;
        int ksize;
        ksize = grid_points[2] - 1;
#pragma omp for nowait
        for (i_imopVar125 = 1; i_imopVar125 < grid_points[0] - 1; i_imopVar125++) {
            for (j_imopVar126 = 1; j_imopVar126 < grid_points[1] - 1; j_imopVar126++) {
                double ( *_imopVarPre418 );
                double ( *_imopVarPre419 )[5];
                double ( *_imopVarPre420 )[5];
                _imopVarPre418 = rhs[i_imopVar125][j_imopVar126][0];
                _imopVarPre419 = lhs[i_imopVar125][j_imopVar126][0][2];
                _imopVarPre420 = lhs[i_imopVar125][j_imopVar126][0][1];
                binvcrhs(_imopVarPre420, _imopVarPre419, _imopVarPre418);
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        for (k_imopVar127 = 1; k_imopVar127 < ksize; k_imopVar127++) {
#pragma omp for nowait
            for (i_imopVar125 = 1; i_imopVar125 < grid_points[0] - 1; i_imopVar125++) {
                for (j_imopVar126 = 1; j_imopVar126 < grid_points[1] - 1; j_imopVar126++) {
                    double ( *_imopVarPre424 );
                    double ( *_imopVarPre425 );
                    double ( *_imopVarPre426 )[5];
                    _imopVarPre424 = rhs[i_imopVar125][j_imopVar126][k_imopVar127];
                    _imopVarPre425 = rhs[i_imopVar125][j_imopVar126][k_imopVar127 - 1];
                    _imopVarPre426 = lhs[i_imopVar125][j_imopVar126][k_imopVar127][0];
                    matvec_sub(_imopVarPre426, _imopVarPre425, _imopVarPre424);
                    double ( *_imopVarPre430 )[5];
                    double ( *_imopVarPre431 )[5];
                    double ( *_imopVarPre432 )[5];
                    _imopVarPre430 = lhs[i_imopVar125][j_imopVar126][k_imopVar127][1];
                    _imopVarPre431 = lhs[i_imopVar125][j_imopVar126][k_imopVar127 - 1][2];
                    _imopVarPre432 = lhs[i_imopVar125][j_imopVar126][k_imopVar127][0];
                    matmul_sub(_imopVarPre432, _imopVarPre431, _imopVarPre430);
                    double ( *_imopVarPre436 );
                    double ( *_imopVarPre437 )[5];
                    double ( *_imopVarPre438 )[5];
                    _imopVarPre436 = rhs[i_imopVar125][j_imopVar126][k_imopVar127];
                    _imopVarPre437 = lhs[i_imopVar125][j_imopVar126][k_imopVar127][2];
                    _imopVarPre438 = lhs[i_imopVar125][j_imopVar126][k_imopVar127][1];
                    binvcrhs(_imopVarPre438, _imopVarPre437, _imopVarPre436);
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        }
#pragma omp for nowait
        for (i_imopVar125 = 1; i_imopVar125 < grid_points[0] - 1; i_imopVar125++) {
            for (j_imopVar126 = 1; j_imopVar126 < grid_points[1] - 1; j_imopVar126++) {
                double ( *_imopVarPre442 );
                double ( *_imopVarPre443 );
                double ( *_imopVarPre444 )[5];
                _imopVarPre442 = rhs[i_imopVar125][j_imopVar126][ksize];
                _imopVarPre443 = rhs[i_imopVar125][j_imopVar126][ksize - 1];
                _imopVarPre444 = lhs[i_imopVar125][j_imopVar126][ksize][0];
                matvec_sub(_imopVarPre444, _imopVarPre443, _imopVarPre442);
                double ( *_imopVarPre448 )[5];
                double ( *_imopVarPre449 )[5];
                double ( *_imopVarPre450 )[5];
                _imopVarPre448 = lhs[i_imopVar125][j_imopVar126][ksize][1];
                _imopVarPre449 = lhs[i_imopVar125][j_imopVar126][ksize - 1][2];
                _imopVarPre450 = lhs[i_imopVar125][j_imopVar126][ksize][0];
                matmul_sub(_imopVarPre450, _imopVarPre449, _imopVarPre448);
                double ( *_imopVarPre453 );
                double ( *_imopVarPre454 )[5];
                _imopVarPre453 = rhs[i_imopVar125][j_imopVar126][ksize];
                _imopVarPre454 = lhs[i_imopVar125][j_imopVar126][ksize][1];
                binvrhs(_imopVarPre454, _imopVarPre453);
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        int i_imopVar103;
        int j_imopVar104;
        int k_imopVar105;
        int m_imopVar131;
        int n_imopVar132;
#pragma omp for nowait
        for (i_imopVar103 = 1; i_imopVar103 < grid_points[0] - 1; i_imopVar103++) {
            for (j_imopVar104 = 1; j_imopVar104 < grid_points[1] - 1; j_imopVar104++) {
                for (k_imopVar105 = grid_points[2] - 2; k_imopVar105 >= 0; k_imopVar105--) {
                    for (m_imopVar131 = 0; m_imopVar131 < 5; m_imopVar131++) {
                        for (n_imopVar132 = 0; n_imopVar132 < 5; n_imopVar132++) {
                            rhs[i_imopVar103][j_imopVar104][k_imopVar105][m_imopVar131] = rhs[i_imopVar103][j_imopVar104][k_imopVar105][m_imopVar131] - lhs[i_imopVar103][j_imopVar104][k_imopVar105][2][m_imopVar131][n_imopVar132] * rhs[i_imopVar103][j_imopVar104][k_imopVar105 + 1][n_imopVar132];
                        }
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        int i_imopVar106;
        int j_imopVar107;
        int k_imopVar108;
        int m_imopVar109;
#pragma omp for nowait
        for (i_imopVar106 = 1; i_imopVar106 < grid_points[0] - 1; i_imopVar106++) {
            for (j_imopVar107 = 1; j_imopVar107 < grid_points[1] - 1; j_imopVar107++) {
                for (k_imopVar108 = 1; k_imopVar108 < grid_points[2] - 1; k_imopVar108++) {
                    for (m_imopVar109 = 0; m_imopVar109 < 5; m_imopVar109++) {
                        u[i_imopVar106][j_imopVar107][k_imopVar108][m_imopVar109] = u[i_imopVar106][j_imopVar107][k_imopVar108][m_imopVar109] + rhs[i_imopVar106][j_imopVar107][k_imopVar108][m_imopVar109];
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
// #pragma omp dummyFlush BARRIER_START written([]) read([u.f, initialize, u, i])
#pragma omp barrier
        int i_imopVar88;
        int j_imopVar89;
        int k_imopVar90;
        int m_imopVar91;
        int ix_imopVar92;
        int iy_imopVar93;
        int iz_imopVar94;
        double xi_imopVar95;
        double eta_imopVar96;
        double zeta_imopVar97;
        double Pface_imopVar98[2][3][5];
        double Pxi_imopVar99;
        double Peta_imopVar100;
        double Pzeta_imopVar101;
        double temp_imopVar102[5];
#pragma omp for nowait
        for (i_imopVar88 = 0; i_imopVar88 < 12; i_imopVar88++) {
            for (j_imopVar89 = 0; j_imopVar89 < 12; j_imopVar89++) {
                for (k_imopVar90 = 0; k_imopVar90 < 12; k_imopVar90++) {
                    for (m_imopVar91 = 0; m_imopVar91 < 5; m_imopVar91++) {
                        u[i_imopVar88][j_imopVar89][k_imopVar90][m_imopVar91] = 1.0;
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
        for (i_imopVar88 = 0; i_imopVar88 < grid_points[0]; i_imopVar88++) {
            xi_imopVar95 = (double) i_imopVar88 * dnxm1;
            for (j_imopVar89 = 0; j_imopVar89 < grid_points[1]; j_imopVar89++) {
                eta_imopVar96 = (double) j_imopVar89 * dnym1;
                for (k_imopVar90 = 0; k_imopVar90 < grid_points[2]; k_imopVar90++) {
                    zeta_imopVar97 = (double) k_imopVar90 * dnzm1;
                    for (ix_imopVar92 = 0; ix_imopVar92 < 2; ix_imopVar92++) {
                        double *_imopVarPre191;
                        double _imopVarPre192;
                        _imopVarPre191 = &(Pface_imopVar98[ix_imopVar92][0][0]);
                        _imopVarPre192 = (double) ix_imopVar92;
                        exact_solution(_imopVarPre192, eta_imopVar96, zeta_imopVar97, _imopVarPre191);
                    }
                    for (iy_imopVar93 = 0; iy_imopVar93 < 2; iy_imopVar93++) {
                        double *_imopVarPre195;
                        double _imopVarPre196;
                        _imopVarPre195 = &Pface_imopVar98[iy_imopVar93][1][0];
                        _imopVarPre196 = (double) iy_imopVar93;
                        exact_solution(xi_imopVar95, _imopVarPre196, zeta_imopVar97, _imopVarPre195);
                    }
                    for (iz_imopVar94 = 0; iz_imopVar94 < 2; iz_imopVar94++) {
                        double *_imopVarPre199;
                        double _imopVarPre200;
                        _imopVarPre199 = &Pface_imopVar98[iz_imopVar94][2][0];
                        _imopVarPre200 = (double) iz_imopVar94;
                        exact_solution(xi_imopVar95, eta_imopVar96, _imopVarPre200, _imopVarPre199);
                    }
                    for (m_imopVar91 = 0; m_imopVar91 < 5; m_imopVar91++) {
                        Pxi_imopVar99 = xi_imopVar95 * Pface_imopVar98[1][0][m_imopVar91] + (1.0 - xi_imopVar95) * Pface_imopVar98[0][0][m_imopVar91];
                        Peta_imopVar100 = eta_imopVar96 * Pface_imopVar98[1][1][m_imopVar91] + (1.0 - eta_imopVar96) * Pface_imopVar98[0][1][m_imopVar91];
                        Pzeta_imopVar101 = zeta_imopVar97 * Pface_imopVar98[1][2][m_imopVar91] + (1.0 - zeta_imopVar97) * Pface_imopVar98[0][2][m_imopVar91];
                        u[i_imopVar88][j_imopVar89][k_imopVar90][m_imopVar91] = Pxi_imopVar99 + Peta_imopVar100 + Pzeta_imopVar101 - Pxi_imopVar99 * Peta_imopVar100 - Pxi_imopVar99 * Pzeta_imopVar101 - Peta_imopVar100 * Pzeta_imopVar101 + Pxi_imopVar99 * Peta_imopVar100 * Pzeta_imopVar101;
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        i_imopVar88 = 0;
        xi_imopVar95 = 0.0;
#pragma omp for nowait
        for (j_imopVar89 = 0; j_imopVar89 < grid_points[1]; j_imopVar89++) {
            eta_imopVar96 = (double) j_imopVar89 * dnym1;
            for (k_imopVar90 = 0; k_imopVar90 < grid_points[2]; k_imopVar90++) {
                zeta_imopVar97 = (double) k_imopVar90 * dnzm1;
                exact_solution(xi_imopVar95, eta_imopVar96, zeta_imopVar97, temp_imopVar102);
                for (m_imopVar91 = 0; m_imopVar91 < 5; m_imopVar91++) {
                    u[i_imopVar88][j_imopVar89][k_imopVar90][m_imopVar91] = temp_imopVar102[m_imopVar91];
                }
            }
        }
        i_imopVar88 = grid_points[0] - 1;
        xi_imopVar95 = 1.0;
#pragma omp for nowait
        for (j_imopVar89 = 0; j_imopVar89 < grid_points[1]; j_imopVar89++) {
            eta_imopVar96 = (double) j_imopVar89 * dnym1;
            for (k_imopVar90 = 0; k_imopVar90 < grid_points[2]; k_imopVar90++) {
                zeta_imopVar97 = (double) k_imopVar90 * dnzm1;
                exact_solution(xi_imopVar95, eta_imopVar96, zeta_imopVar97, temp_imopVar102);
                for (m_imopVar91 = 0; m_imopVar91 < 5; m_imopVar91++) {
                    u[i_imopVar88][j_imopVar89][k_imopVar90][m_imopVar91] = temp_imopVar102[m_imopVar91];
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        j_imopVar89 = 0;
        eta_imopVar96 = 0.0;
#pragma omp for nowait
        for (i_imopVar88 = 0; i_imopVar88 < grid_points[0]; i_imopVar88++) {
            xi_imopVar95 = (double) i_imopVar88 * dnxm1;
            for (k_imopVar90 = 0; k_imopVar90 < grid_points[2]; k_imopVar90++) {
                zeta_imopVar97 = (double) k_imopVar90 * dnzm1;
                exact_solution(xi_imopVar95, eta_imopVar96, zeta_imopVar97, temp_imopVar102);
                for (m_imopVar91 = 0; m_imopVar91 < 5; m_imopVar91++) {
                    u[i_imopVar88][j_imopVar89][k_imopVar90][m_imopVar91] = temp_imopVar102[m_imopVar91];
                }
            }
        }
        j_imopVar89 = grid_points[1] - 1;
        eta_imopVar96 = 1.0;
#pragma omp for nowait
        for (i_imopVar88 = 0; i_imopVar88 < grid_points[0]; i_imopVar88++) {
            xi_imopVar95 = (double) i_imopVar88 * dnxm1;
            for (k_imopVar90 = 0; k_imopVar90 < grid_points[2]; k_imopVar90++) {
                zeta_imopVar97 = (double) k_imopVar90 * dnzm1;
                exact_solution(xi_imopVar95, eta_imopVar96, zeta_imopVar97, temp_imopVar102);
                for (m_imopVar91 = 0; m_imopVar91 < 5; m_imopVar91++) {
                    u[i_imopVar88][j_imopVar89][k_imopVar90][m_imopVar91] = temp_imopVar102[m_imopVar91];
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        k_imopVar90 = 0;
        zeta_imopVar97 = 0.0;
#pragma omp for nowait
        for (i_imopVar88 = 0; i_imopVar88 < grid_points[0]; i_imopVar88++) {
            xi_imopVar95 = (double) i_imopVar88 * dnxm1;
            for (j_imopVar89 = 0; j_imopVar89 < grid_points[1]; j_imopVar89++) {
                eta_imopVar96 = (double) j_imopVar89 * dnym1;
                exact_solution(xi_imopVar95, eta_imopVar96, zeta_imopVar97, temp_imopVar102);
                for (m_imopVar91 = 0; m_imopVar91 < 5; m_imopVar91++) {
                    u[i_imopVar88][j_imopVar89][k_imopVar90][m_imopVar91] = temp_imopVar102[m_imopVar91];
                }
            }
        }
        k_imopVar90 = grid_points[2] - 1;
        zeta_imopVar97 = 1.0;
#pragma omp for nowait
        for (i_imopVar88 = 0; i_imopVar88 < grid_points[0]; i_imopVar88++) {
            xi_imopVar95 = (double) i_imopVar88 * dnxm1;
            for (j_imopVar89 = 0; j_imopVar89 < grid_points[1]; j_imopVar89++) {
                eta_imopVar96 = (double) j_imopVar89 * dnym1;
                exact_solution(xi_imopVar95, eta_imopVar96, zeta_imopVar97, temp_imopVar102);
                for (m_imopVar91 = 0; m_imopVar91 < 5; m_imopVar91++) {
                    u[i_imopVar88][j_imopVar89][k_imopVar90][m_imopVar91] = temp_imopVar102[m_imopVar91];
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
// #pragma omp dummyFlush BARRIER_START written([]) read([rho_i.f, square, &class, xce.f, _imopVarPre172, grid_points, dnym1, u, rhs, forcing, sqrt, timer_clear, verify, timer_stop, qs, ce, vs, ws, exact_solution, us, square.f, dnxm1, u.f, grid_points.f, timer_start, &verified, dnzm1, rhs.f, forcing.f, step, rho_i, error_norm, timer_read, i, qs.f, ce.f, printf, ws.f, us.f, compute_rhs, vs.f])
#pragma omp barrier
#pragma omp master
        {
            timer_clear(1);
            timer_start(1);
        }
    }
    for (step = 1; step <= niter; step++) {
#pragma omp parallel
        {
            int _imopVarPre172;
#pragma omp master
            {
                _imopVarPre172 = step % 20 == 0;
                if (!_imopVarPre172) {
                    _imopVarPre172 = step == 1;
                }
                if (_imopVarPre172) {
                    printf(" Time step %4d\n", step);
                }
            }
            int i_imopVar110;
            int j_imopVar111;
            int k_imopVar112;
            int m_imopVar113;
            double rho_inv;
            double uijk;
            double up1;
            double um1;
            double vijk;
            double vp1;
            double vm1;
            double wijk;
            double wp1;
            double wm1;
#pragma omp for nowait
            for (i_imopVar110 = 0; i_imopVar110 < grid_points[0]; i_imopVar110++) {
                for (j_imopVar111 = 0; j_imopVar111 < grid_points[1]; j_imopVar111++) {
                    for (k_imopVar112 = 0; k_imopVar112 < grid_points[2]; k_imopVar112++) {
                        rho_inv = 1.0 / u[i_imopVar110][j_imopVar111][k_imopVar112][0];
                        rho_i[i_imopVar110][j_imopVar111][k_imopVar112] = rho_inv;
                        us[i_imopVar110][j_imopVar111][k_imopVar112] = u[i_imopVar110][j_imopVar111][k_imopVar112][1] * rho_inv;
                        vs[i_imopVar110][j_imopVar111][k_imopVar112] = u[i_imopVar110][j_imopVar111][k_imopVar112][2] * rho_inv;
                        ws[i_imopVar110][j_imopVar111][k_imopVar112] = u[i_imopVar110][j_imopVar111][k_imopVar112][3] * rho_inv;
                        square[i_imopVar110][j_imopVar111][k_imopVar112] = 0.5 * (u[i_imopVar110][j_imopVar111][k_imopVar112][1] * u[i_imopVar110][j_imopVar111][k_imopVar112][1] + u[i_imopVar110][j_imopVar111][k_imopVar112][2] * u[i_imopVar110][j_imopVar111][k_imopVar112][2] + u[i_imopVar110][j_imopVar111][k_imopVar112][3] * u[i_imopVar110][j_imopVar111][k_imopVar112][3]) * rho_inv;
                        qs[i_imopVar110][j_imopVar111][k_imopVar112] = square[i_imopVar110][j_imopVar111][k_imopVar112] * rho_inv;
                    }
                }
            }
#pragma omp for nowait
            for (i_imopVar110 = 0; i_imopVar110 < grid_points[0]; i_imopVar110++) {
                for (j_imopVar111 = 0; j_imopVar111 < grid_points[1]; j_imopVar111++) {
                    for (k_imopVar112 = 0; k_imopVar112 < grid_points[2]; k_imopVar112++) {
                        for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                            rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = forcing[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113];
                        }
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
            for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
                for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                    for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                        uijk = us[i_imopVar110][j_imopVar111][k_imopVar112];
                        up1 = us[i_imopVar110 + 1][j_imopVar111][k_imopVar112];
                        um1 = us[i_imopVar110 - 1][j_imopVar111][k_imopVar112];
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][0] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][0] + dx1tx1 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][0] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][0] + u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][0]) - tx2 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][1] - u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][1]);
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][1] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][1] + dx2tx1 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][1] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][1] + u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][1]) + xxcon2 * con43 * (up1 - 2.0 * uijk + um1) - tx2 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][1] * up1 - u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][1] * um1 + (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][4] - square[i_imopVar110 + 1][j_imopVar111][k_imopVar112] - u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][4] + square[i_imopVar110 - 1][j_imopVar111][k_imopVar112]) * c2);
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][2] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][2] + dx3tx1 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][2] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][2] + u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][2]) + xxcon2 * (vs[i_imopVar110 + 1][j_imopVar111][k_imopVar112] - 2.0 * vs[i_imopVar110][j_imopVar111][k_imopVar112] + vs[i_imopVar110 - 1][j_imopVar111][k_imopVar112]) - tx2 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][2] * up1 - u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][2] * um1);
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][3] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][3] + dx4tx1 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][3] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][3] + u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][3]) + xxcon2 * (ws[i_imopVar110 + 1][j_imopVar111][k_imopVar112] - 2.0 * ws[i_imopVar110][j_imopVar111][k_imopVar112] + ws[i_imopVar110 - 1][j_imopVar111][k_imopVar112]) - tx2 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][3] * up1 - u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][3] * um1);
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][4] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][4] + dx5tx1 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][4] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][4] + u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][4]) + xxcon3 * (qs[i_imopVar110 + 1][j_imopVar111][k_imopVar112] - 2.0 * qs[i_imopVar110][j_imopVar111][k_imopVar112] + qs[i_imopVar110 - 1][j_imopVar111][k_imopVar112]) + xxcon4 * (up1 * up1 - 2.0 * uijk * uijk + um1 * um1) + xxcon5 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][4] * rho_i[i_imopVar110 + 1][j_imopVar111][k_imopVar112] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][4] * rho_i[i_imopVar110][j_imopVar111][k_imopVar112] + u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][4] * rho_i[i_imopVar110 - 1][j_imopVar111][k_imopVar112]) - tx2 * ((c1 * u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][4] - c2 * square[i_imopVar110 + 1][j_imopVar111][k_imopVar112]) * up1 - (c1 * u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][4] - c2 * square[i_imopVar110 - 1][j_imopVar111][k_imopVar112]) * um1);
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            i_imopVar110 = 1;
#pragma omp for nowait
            for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                    for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (5.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][m_imopVar113] + u[i_imopVar110 + 2][j_imopVar111][k_imopVar112][m_imopVar113]);
                    }
                }
            }
            i_imopVar110 = 2;
#pragma omp for nowait
            for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                    for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (-4.0 * u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][m_imopVar113] + u[i_imopVar110 + 2][j_imopVar111][k_imopVar112][m_imopVar113]);
                    }
                }
            }
#pragma omp for nowait
            for (i_imopVar110 = 3; i_imopVar110 < grid_points[0] - 3; i_imopVar110++) {
                for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                    for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                        for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                            rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110 - 2][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][m_imopVar113] + u[i_imopVar110 + 2][j_imopVar111][k_imopVar112][m_imopVar113]);
                        }
                    }
                }
            }
            i_imopVar110 = grid_points[0] - 3;
#pragma omp for nowait
            for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                    for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110 - 2][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][m_imopVar113]);
                    }
                }
            }
            i_imopVar110 = grid_points[0] - 2;
#pragma omp for nowait
            for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                    for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110 - 2][j_imopVar111][k_imopVar112][m_imopVar113] - 4. * u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][m_imopVar113] + 5.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113]);
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
            for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
                for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                    for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                        vijk = vs[i_imopVar110][j_imopVar111][k_imopVar112];
                        vp1 = vs[i_imopVar110][j_imopVar111 + 1][k_imopVar112];
                        vm1 = vs[i_imopVar110][j_imopVar111 - 1][k_imopVar112];
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][0] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][0] + dy1ty1 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][0] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][0] + u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][0]) - ty2 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][2] - u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][2]);
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][1] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][1] + dy2ty1 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][1] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][1] + u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][1]) + yycon2 * (us[i_imopVar110][j_imopVar111 + 1][k_imopVar112] - 2.0 * us[i_imopVar110][j_imopVar111][k_imopVar112] + us[i_imopVar110][j_imopVar111 - 1][k_imopVar112]) - ty2 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][1] * vp1 - u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][1] * vm1);
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][2] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][2] + dy3ty1 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][2] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][2] + u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][2]) + yycon2 * con43 * (vp1 - 2.0 * vijk + vm1) - ty2 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][2] * vp1 - u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][2] * vm1 + (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][4] - square[i_imopVar110][j_imopVar111 + 1][k_imopVar112] - u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][4] + square[i_imopVar110][j_imopVar111 - 1][k_imopVar112]) * c2);
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][3] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][3] + dy4ty1 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][3] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][3] + u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][3]) + yycon2 * (ws[i_imopVar110][j_imopVar111 + 1][k_imopVar112] - 2.0 * ws[i_imopVar110][j_imopVar111][k_imopVar112] + ws[i_imopVar110][j_imopVar111 - 1][k_imopVar112]) - ty2 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][3] * vp1 - u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][3] * vm1);
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][4] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][4] + dy5ty1 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][4] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][4] + u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][4]) + yycon3 * (qs[i_imopVar110][j_imopVar111 + 1][k_imopVar112] - 2.0 * qs[i_imopVar110][j_imopVar111][k_imopVar112] + qs[i_imopVar110][j_imopVar111 - 1][k_imopVar112]) + yycon4 * (vp1 * vp1 - 2.0 * vijk * vijk + vm1 * vm1) + yycon5 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][4] * rho_i[i_imopVar110][j_imopVar111 + 1][k_imopVar112] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][4] * rho_i[i_imopVar110][j_imopVar111][k_imopVar112] + u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][4] * rho_i[i_imopVar110][j_imopVar111 - 1][k_imopVar112]) - ty2 * ((c1 * u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][4] - c2 * square[i_imopVar110][j_imopVar111 + 1][k_imopVar112]) * vp1 - (c1 * u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][4] - c2 * square[i_imopVar110][j_imopVar111 - 1][k_imopVar112]) * vm1);
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            j_imopVar111 = 1;
#pragma omp for nowait
            for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
                for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                    for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (5.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][m_imopVar113] + u[i_imopVar110][j_imopVar111 + 2][k_imopVar112][m_imopVar113]);
                    }
                }
            }
            j_imopVar111 = 2;
#pragma omp for nowait
            for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
                for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                    for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (-4.0 * u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][m_imopVar113] + u[i_imopVar110][j_imopVar111 + 2][k_imopVar112][m_imopVar113]);
                    }
                }
            }
#pragma omp for nowait
            for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
                for (j_imopVar111 = 3; j_imopVar111 < grid_points[1] - 3; j_imopVar111++) {
                    for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                        for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                            rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110][j_imopVar111 - 2][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][m_imopVar113] + u[i_imopVar110][j_imopVar111 + 2][k_imopVar112][m_imopVar113]);
                        }
                    }
                }
            }
            j_imopVar111 = grid_points[1] - 3;
#pragma omp for nowait
            for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
                for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                    for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110][j_imopVar111 - 2][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][m_imopVar113]);
                    }
                }
            }
            j_imopVar111 = grid_points[1] - 2;
#pragma omp for nowait
            for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
                for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                    for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110][j_imopVar111 - 2][k_imopVar112][m_imopVar113] - 4. * u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][m_imopVar113] + 5. * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113]);
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
            for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
                for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                    for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                        wijk = ws[i_imopVar110][j_imopVar111][k_imopVar112];
                        wp1 = ws[i_imopVar110][j_imopVar111][k_imopVar112 + 1];
                        wm1 = ws[i_imopVar110][j_imopVar111][k_imopVar112 - 1];
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][0] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][0] + dz1tz1 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][0] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][0] + u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][0]) - tz2 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][3] - u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][3]);
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][1] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][1] + dz2tz1 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][1] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][1] + u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][1]) + zzcon2 * (us[i_imopVar110][j_imopVar111][k_imopVar112 + 1] - 2.0 * us[i_imopVar110][j_imopVar111][k_imopVar112] + us[i_imopVar110][j_imopVar111][k_imopVar112 - 1]) - tz2 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][1] * wp1 - u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][1] * wm1);
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][2] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][2] + dz3tz1 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][2] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][2] + u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][2]) + zzcon2 * (vs[i_imopVar110][j_imopVar111][k_imopVar112 + 1] - 2.0 * vs[i_imopVar110][j_imopVar111][k_imopVar112] + vs[i_imopVar110][j_imopVar111][k_imopVar112 - 1]) - tz2 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][2] * wp1 - u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][2] * wm1);
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][3] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][3] + dz4tz1 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][3] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][3] + u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][3]) + zzcon2 * con43 * (wp1 - 2.0 * wijk + wm1) - tz2 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][3] * wp1 - u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][3] * wm1 + (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][4] - square[i_imopVar110][j_imopVar111][k_imopVar112 + 1] - u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][4] + square[i_imopVar110][j_imopVar111][k_imopVar112 - 1]) * c2);
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][4] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][4] + dz5tz1 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][4] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][4] + u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][4]) + zzcon3 * (qs[i_imopVar110][j_imopVar111][k_imopVar112 + 1] - 2.0 * qs[i_imopVar110][j_imopVar111][k_imopVar112] + qs[i_imopVar110][j_imopVar111][k_imopVar112 - 1]) + zzcon4 * (wp1 * wp1 - 2.0 * wijk * wijk + wm1 * wm1) + zzcon5 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][4] * rho_i[i_imopVar110][j_imopVar111][k_imopVar112 + 1] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][4] * rho_i[i_imopVar110][j_imopVar111][k_imopVar112] + u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][4] * rho_i[i_imopVar110][j_imopVar111][k_imopVar112 - 1]) - tz2 * ((c1 * u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][4] - c2 * square[i_imopVar110][j_imopVar111][k_imopVar112 + 1]) * wp1 - (c1 * u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][4] - c2 * square[i_imopVar110][j_imopVar111][k_imopVar112 - 1]) * wm1);
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            k_imopVar112 = 1;
#pragma omp for nowait
            for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
                for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                    for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (5.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][m_imopVar113] + u[i_imopVar110][j_imopVar111][k_imopVar112 + 2][m_imopVar113]);
                    }
                }
            }
            k_imopVar112 = 2;
#pragma omp for nowait
            for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
                for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                    for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (-4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][m_imopVar113] + u[i_imopVar110][j_imopVar111][k_imopVar112 + 2][m_imopVar113]);
                    }
                }
            }
#pragma omp for nowait
            for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
                for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                    for (k_imopVar112 = 3; k_imopVar112 < grid_points[2] - 3; k_imopVar112++) {
                        for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                            rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110][j_imopVar111][k_imopVar112 - 2][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][m_imopVar113] + u[i_imopVar110][j_imopVar111][k_imopVar112 + 2][m_imopVar113]);
                        }
                    }
                }
            }
            k_imopVar112 = grid_points[2] - 3;
#pragma omp for nowait
            for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
                for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                    for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110][j_imopVar111][k_imopVar112 - 2][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][m_imopVar113]);
                    }
                }
            }
            k_imopVar112 = grid_points[2] - 2;
#pragma omp for nowait
            for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
                for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                    for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                        rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110][j_imopVar111][k_imopVar112 - 2][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][m_imopVar113] + 5.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113]);
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
            for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
                for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                    for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                        for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
                            rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] * dt;
                        }
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            int i_imopVar120;
            int j_imopVar121;
            int k_imopVar122;
#pragma omp for nowait
            for (j_imopVar121 = 1; j_imopVar121 < grid_points[1] - 1; j_imopVar121++) {
                for (k_imopVar122 = 1; k_imopVar122 < grid_points[2] - 1; k_imopVar122++) {
                    for (i_imopVar120 = 0; i_imopVar120 < grid_points[0]; i_imopVar120++) {
                        tmp1 = 1.0 / u[i_imopVar120][j_imopVar121][k_imopVar122][0];
                        tmp2 = tmp1 * tmp1;
                        tmp3 = tmp1 * tmp2;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][0][0] = 0.0;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][0][1] = 1.0;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][0][2] = 0.0;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][0][3] = 0.0;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][0][4] = 0.0;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][1][0] = -(u[i_imopVar120][j_imopVar121][k_imopVar122][1] * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][1]) + c2 * 0.50 * (u[i_imopVar120][j_imopVar121][k_imopVar122][1] * u[i_imopVar120][j_imopVar121][k_imopVar122][1] + u[i_imopVar120][j_imopVar121][k_imopVar122][2] * u[i_imopVar120][j_imopVar121][k_imopVar122][2] + u[i_imopVar120][j_imopVar121][k_imopVar122][3] * u[i_imopVar120][j_imopVar121][k_imopVar122][3]) * tmp2;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][1][1] = (2.0 - c2) * (u[i_imopVar120][j_imopVar121][k_imopVar122][1] / u[i_imopVar120][j_imopVar121][k_imopVar122][0]);
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][1][2] = -c2 * (u[i_imopVar120][j_imopVar121][k_imopVar122][2] * tmp1);
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][1][3] = -c2 * (u[i_imopVar120][j_imopVar121][k_imopVar122][3] * tmp1);
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][1][4] = c2;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][2][0] = -(u[i_imopVar120][j_imopVar121][k_imopVar122][1] * u[i_imopVar120][j_imopVar121][k_imopVar122][2]) * tmp2;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][2][1] = u[i_imopVar120][j_imopVar121][k_imopVar122][2] * tmp1;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][2][2] = u[i_imopVar120][j_imopVar121][k_imopVar122][1] * tmp1;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][2][3] = 0.0;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][2][4] = 0.0;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][3][0] = -(u[i_imopVar120][j_imopVar121][k_imopVar122][1] * u[i_imopVar120][j_imopVar121][k_imopVar122][3]) * tmp2;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][3][1] = u[i_imopVar120][j_imopVar121][k_imopVar122][3] * tmp1;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][3][2] = 0.0;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][3][3] = u[i_imopVar120][j_imopVar121][k_imopVar122][1] * tmp1;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][3][4] = 0.0;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][4][0] = (c2 * (u[i_imopVar120][j_imopVar121][k_imopVar122][1] * u[i_imopVar120][j_imopVar121][k_imopVar122][1] + u[i_imopVar120][j_imopVar121][k_imopVar122][2] * u[i_imopVar120][j_imopVar121][k_imopVar122][2] + u[i_imopVar120][j_imopVar121][k_imopVar122][3] * u[i_imopVar120][j_imopVar121][k_imopVar122][3]) * tmp2 - c1 * (u[i_imopVar120][j_imopVar121][k_imopVar122][4] * tmp1)) * (u[i_imopVar120][j_imopVar121][k_imopVar122][1] * tmp1);
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][4][1] = c1 * u[i_imopVar120][j_imopVar121][k_imopVar122][4] * tmp1 - 0.50 * c2 * (3.0 * u[i_imopVar120][j_imopVar121][k_imopVar122][1] * u[i_imopVar120][j_imopVar121][k_imopVar122][1] + u[i_imopVar120][j_imopVar121][k_imopVar122][2] * u[i_imopVar120][j_imopVar121][k_imopVar122][2] + u[i_imopVar120][j_imopVar121][k_imopVar122][3] * u[i_imopVar120][j_imopVar121][k_imopVar122][3]) * tmp2;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][4][2] = -c2 * (u[i_imopVar120][j_imopVar121][k_imopVar122][2] * u[i_imopVar120][j_imopVar121][k_imopVar122][1]) * tmp2;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][4][3] = -c2 * (u[i_imopVar120][j_imopVar121][k_imopVar122][3] * u[i_imopVar120][j_imopVar121][k_imopVar122][1]) * tmp2;
                        fjac[i_imopVar120][j_imopVar121][k_imopVar122][4][4] = c1 * (u[i_imopVar120][j_imopVar121][k_imopVar122][1] * tmp1);
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][0][0] = 0.0;
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][0][1] = 0.0;
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][0][2] = 0.0;
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][0][3] = 0.0;
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][0][4] = 0.0;
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][1][0] = -con43 * c3c4 * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][1];
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][1][1] = con43 * c3c4 * tmp1;
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][1][2] = 0.0;
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][1][3] = 0.0;
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][1][4] = 0.0;
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][2][0] = -c3c4 * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][2];
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][2][1] = 0.0;
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][2][2] = c3c4 * tmp1;
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][2][3] = 0.0;
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][2][4] = 0.0;
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][3][0] = -c3c4 * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][3];
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][3][1] = 0.0;
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][3][2] = 0.0;
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][3][3] = c3c4 * tmp1;
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][3][4] = 0.0;
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][4][0] = -(con43 * c3c4 - c1345) * tmp3 * (((u[i_imopVar120][j_imopVar121][k_imopVar122][1]) * (u[i_imopVar120][j_imopVar121][k_imopVar122][1]))) - (c3c4 - c1345) * tmp3 * (((u[i_imopVar120][j_imopVar121][k_imopVar122][2]) * (u[i_imopVar120][j_imopVar121][k_imopVar122][2]))) - (c3c4 - c1345) * tmp3 * (((u[i_imopVar120][j_imopVar121][k_imopVar122][3]) * (u[i_imopVar120][j_imopVar121][k_imopVar122][3]))) - c1345 * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][4];
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][4][1] = (con43 * c3c4 - c1345) * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][1];
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][4][2] = (c3c4 - c1345) * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][2];
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][4][3] = (c3c4 - c1345) * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][3];
                        njac[i_imopVar120][j_imopVar121][k_imopVar122][4][4] = c1345 * tmp1;
                    }
                    for (i_imopVar120 = 1; i_imopVar120 < grid_points[0] - 1; i_imopVar120++) {
                        tmp1 = dt * tx1;
                        tmp2 = dt * tx2;
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][0][0] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][0] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][0] - tmp1 * dx1;
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][0][1] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][1] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][1];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][0][2] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][2] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][2];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][0][3] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][3] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][3];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][0][4] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][4] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][4];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][1][0] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][0] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][0];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][1][1] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][1] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][1] - tmp1 * dx2;
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][1][2] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][2] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][2];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][1][3] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][3] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][3];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][1][4] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][4] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][4];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][2][0] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][0] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][0];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][2][1] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][1] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][1];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][2][2] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][2] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][2] - tmp1 * dx3;
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][2][3] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][3] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][3];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][2][4] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][4] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][4];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][3][0] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][0] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][0];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][3][1] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][1] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][1];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][3][2] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][2] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][2];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][3][3] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][3] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][3] - tmp1 * dx4;
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][3][4] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][4] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][4];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][4][0] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][0] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][0];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][4][1] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][1] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][1];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][4][2] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][2] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][2];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][4][3] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][3] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][3];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][4][4] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][4] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][4] - tmp1 * dx5;
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][0][0] = 1.0 + tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][0][0] + tmp1 * 2.0 * dx1;
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][0][1] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][0][1];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][0][2] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][0][2];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][0][3] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][0][3];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][0][4] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][0][4];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][1][0] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][1][0];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][1][1] = 1.0 + tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][1][1] + tmp1 * 2.0 * dx2;
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][1][2] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][1][2];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][1][3] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][1][3];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][1][4] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][1][4];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][2][0] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][2][0];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][2][1] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][2][1];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][2][2] = 1.0 + tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][2][2] + tmp1 * 2.0 * dx3;
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][2][3] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][2][3];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][2][4] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][2][4];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][3][0] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][3][0];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][3][1] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][3][1];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][3][2] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][3][2];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][3][3] = 1.0 + tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][3][3] + tmp1 * 2.0 * dx4;
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][3][4] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][3][4];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][4][0] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][4][0];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][4][1] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][4][1];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][4][2] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][4][2];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][4][3] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][4][3];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][4][4] = 1.0 + tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][4][4] + tmp1 * 2.0 * dx5;
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][0][0] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][0] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][0] - tmp1 * dx1;
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][0][1] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][1] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][1];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][0][2] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][2] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][2];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][0][3] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][3] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][3];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][0][4] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][4] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][4];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][1][0] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][0] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][0];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][1][1] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][1] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][1] - tmp1 * dx2;
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][1][2] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][2] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][2];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][1][3] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][3] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][3];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][1][4] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][4] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][4];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][2][0] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][0] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][0];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][2][1] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][1] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][1];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][2][2] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][2] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][2] - tmp1 * dx3;
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][2][3] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][3] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][3];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][2][4] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][4] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][4];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][3][0] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][0] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][0];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][3][1] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][1] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][1];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][3][2] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][2] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][2];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][3][3] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][3] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][3] - tmp1 * dx4;
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][3][4] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][4] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][4];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][4][0] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][0] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][0];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][4][1] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][1] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][1];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][4][2] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][2] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][2];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][4][3] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][3] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][3];
                        lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][4][4] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][4] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][4] - tmp1 * dx5;
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            int i_imopVar114;
            int j_imopVar115;
            int k_imopVar116;
            int isize;
            isize = grid_points[0] - 1;
#pragma omp for nowait
            for (j_imopVar115 = 1; j_imopVar115 < grid_points[1] - 1; j_imopVar115++) {
                for (k_imopVar116 = 1; k_imopVar116 < grid_points[2] - 1; k_imopVar116++) {
                    double ( *_imopVarPre338 );
                    double ( *_imopVarPre339 )[5];
                    double ( *_imopVarPre340 )[5];
                    _imopVarPre338 = rhs[0][j_imopVar115][k_imopVar116];
                    _imopVarPre339 = lhs[0][j_imopVar115][k_imopVar116][2];
                    _imopVarPre340 = lhs[0][j_imopVar115][k_imopVar116][1];
                    binvcrhs(_imopVarPre340, _imopVarPre339, _imopVarPre338);
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            for (i_imopVar114 = 1; i_imopVar114 < isize; i_imopVar114++) {
#pragma omp for nowait
                for (j_imopVar115 = 1; j_imopVar115 < grid_points[1] - 1; j_imopVar115++) {
                    for (k_imopVar116 = 1; k_imopVar116 < grid_points[2] - 1; k_imopVar116++) {
                        double ( *_imopVarPre344 );
                        double ( *_imopVarPre345 );
                        double ( *_imopVarPre346 )[5];
                        _imopVarPre344 = rhs[i_imopVar114][j_imopVar115][k_imopVar116];
                        _imopVarPre345 = rhs[i_imopVar114 - 1][j_imopVar115][k_imopVar116];
                        _imopVarPre346 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][0];
                        matvec_sub(_imopVarPre346, _imopVarPre345, _imopVarPre344);
                        double ( *_imopVarPre350 )[5];
                        double ( *_imopVarPre351 )[5];
                        double ( *_imopVarPre352 )[5];
                        _imopVarPre350 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][1];
                        _imopVarPre351 = lhs[i_imopVar114 - 1][j_imopVar115][k_imopVar116][2];
                        _imopVarPre352 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][0];
                        matmul_sub(_imopVarPre352, _imopVarPre351, _imopVarPre350);
                        double ( *_imopVarPre356 );
                        double ( *_imopVarPre357 )[5];
                        double ( *_imopVarPre358 )[5];
                        _imopVarPre356 = rhs[i_imopVar114][j_imopVar115][k_imopVar116];
                        _imopVarPre357 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][2];
                        _imopVarPre358 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][1];
                        binvcrhs(_imopVarPre358, _imopVarPre357, _imopVarPre356);
                    }
                }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            }
#pragma omp for nowait
            for (j_imopVar115 = 1; j_imopVar115 < grid_points[1] - 1; j_imopVar115++) {
                for (k_imopVar116 = 1; k_imopVar116 < grid_points[2] - 1; k_imopVar116++) {
                    double ( *_imopVarPre362 );
                    double ( *_imopVarPre363 );
                    double ( *_imopVarPre364 )[5];
                    _imopVarPre362 = rhs[isize][j_imopVar115][k_imopVar116];
                    _imopVarPre363 = rhs[isize - 1][j_imopVar115][k_imopVar116];
                    _imopVarPre364 = lhs[isize][j_imopVar115][k_imopVar116][0];
                    matvec_sub(_imopVarPre364, _imopVarPre363, _imopVarPre362);
                    double ( *_imopVarPre368 )[5];
                    double ( *_imopVarPre369 )[5];
                    double ( *_imopVarPre370 )[5];
                    _imopVarPre368 = lhs[isize][j_imopVar115][k_imopVar116][1];
                    _imopVarPre369 = lhs[isize - 1][j_imopVar115][k_imopVar116][2];
                    _imopVarPre370 = lhs[isize][j_imopVar115][k_imopVar116][0];
                    matmul_sub(_imopVarPre370, _imopVarPre369, _imopVarPre368);
                    double ( *_imopVarPre373 );
                    double ( *_imopVarPre374 )[5];
                    _imopVarPre373 = rhs[i_imopVar114][j_imopVar115][k_imopVar116];
                    _imopVarPre374 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][1];
                    binvrhs(_imopVarPre374, _imopVarPre373);
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            int i_imopVar117;
            int j_imopVar118;
            int k_imopVar119;
            int m_imopVar123;
            int n_imopVar124;
            for (i_imopVar117 = grid_points[0] - 2; i_imopVar117 >= 0; i_imopVar117--) {
#pragma omp for nowait
                for (j_imopVar118 = 1; j_imopVar118 < grid_points[1] - 1; j_imopVar118++) {
                    for (k_imopVar119 = 1; k_imopVar119 < grid_points[2] - 1; k_imopVar119++) {
                        for (m_imopVar123 = 0; m_imopVar123 < 5; m_imopVar123++) {
                            for (n_imopVar124 = 0; n_imopVar124 < 5; n_imopVar124++) {
                                rhs[i_imopVar117][j_imopVar118][k_imopVar119][m_imopVar123] = rhs[i_imopVar117][j_imopVar118][k_imopVar119][m_imopVar123] - lhs[i_imopVar117][j_imopVar118][k_imopVar119][2][m_imopVar123][n_imopVar124] * rhs[i_imopVar117 + 1][j_imopVar118][k_imopVar119][n_imopVar124];
                            }
                        }
                    }
                }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            int i_imopVar85;
            int j_imopVar86;
            int k_imopVar87;
#pragma omp for nowait
            for (i_imopVar85 = 1; i_imopVar85 < grid_points[0] - 1; i_imopVar85++) {
                for (j_imopVar86 = 0; j_imopVar86 < grid_points[1]; j_imopVar86++) {
                    for (k_imopVar87 = 1; k_imopVar87 < grid_points[2] - 1; k_imopVar87++) {
                        tmp1 = 1.0 / u[i_imopVar85][j_imopVar86][k_imopVar87][0];
                        tmp2 = tmp1 * tmp1;
                        tmp3 = tmp1 * tmp2;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][0][0] = 0.0;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][0][1] = 0.0;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][0][2] = 1.0;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][0][3] = 0.0;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][0][4] = 0.0;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][1][0] = -(u[i_imopVar85][j_imopVar86][k_imopVar87][1] * u[i_imopVar85][j_imopVar86][k_imopVar87][2]) * tmp2;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][1][1] = u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp1;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][1][2] = u[i_imopVar85][j_imopVar86][k_imopVar87][1] * tmp1;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][1][3] = 0.0;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][1][4] = 0.0;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][2][0] = -(u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp2) + 0.50 * c2 * ((u[i_imopVar85][j_imopVar86][k_imopVar87][1] * u[i_imopVar85][j_imopVar86][k_imopVar87][1] + u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][2] + u[i_imopVar85][j_imopVar86][k_imopVar87][3] * u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * tmp2);
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][2][1] = -c2 * u[i_imopVar85][j_imopVar86][k_imopVar87][1] * tmp1;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][2][2] = (2.0 - c2) * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp1;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][2][3] = -c2 * u[i_imopVar85][j_imopVar86][k_imopVar87][3] * tmp1;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][2][4] = c2;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][3][0] = -(u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * tmp2;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][3][1] = 0.0;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][3][2] = u[i_imopVar85][j_imopVar86][k_imopVar87][3] * tmp1;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][3][3] = u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp1;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][3][4] = 0.0;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][4][0] = (c2 * (u[i_imopVar85][j_imopVar86][k_imopVar87][1] * u[i_imopVar85][j_imopVar86][k_imopVar87][1] + u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][2] + u[i_imopVar85][j_imopVar86][k_imopVar87][3] * u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * tmp2 - c1 * u[i_imopVar85][j_imopVar86][k_imopVar87][4] * tmp1) * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp1;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][4][1] = -c2 * u[i_imopVar85][j_imopVar86][k_imopVar87][1] * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp2;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][4][2] = c1 * u[i_imopVar85][j_imopVar86][k_imopVar87][4] * tmp1 - 0.50 * c2 * ((u[i_imopVar85][j_imopVar86][k_imopVar87][1] * u[i_imopVar85][j_imopVar86][k_imopVar87][1] + 3.0 * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][2] + u[i_imopVar85][j_imopVar86][k_imopVar87][3] * u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * tmp2);
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][4][3] = -c2 * (u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * tmp2;
                        fjac[i_imopVar85][j_imopVar86][k_imopVar87][4][4] = c1 * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp1;
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][0][0] = 0.0;
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][0][1] = 0.0;
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][0][2] = 0.0;
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][0][3] = 0.0;
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][0][4] = 0.0;
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][1][0] = -c3c4 * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][1];
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][1][1] = c3c4 * tmp1;
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][1][2] = 0.0;
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][1][3] = 0.0;
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][1][4] = 0.0;
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][2][0] = -con43 * c3c4 * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][2];
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][2][1] = 0.0;
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][2][2] = con43 * c3c4 * tmp1;
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][2][3] = 0.0;
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][2][4] = 0.0;
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][3][0] = -c3c4 * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][3];
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][3][1] = 0.0;
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][3][2] = 0.0;
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][3][3] = c3c4 * tmp1;
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][3][4] = 0.0;
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][4][0] = -(c3c4 - c1345) * tmp3 * (((u[i_imopVar85][j_imopVar86][k_imopVar87][1]) * (u[i_imopVar85][j_imopVar86][k_imopVar87][1]))) - (con43 * c3c4 - c1345) * tmp3 * (((u[i_imopVar85][j_imopVar86][k_imopVar87][2]) * (u[i_imopVar85][j_imopVar86][k_imopVar87][2]))) - (c3c4 - c1345) * tmp3 * (((u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * (u[i_imopVar85][j_imopVar86][k_imopVar87][3]))) - c1345 * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][4];
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][4][1] = (c3c4 - c1345) * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][1];
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][4][2] = (con43 * c3c4 - c1345) * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][2];
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][4][3] = (c3c4 - c1345) * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][3];
                        njac[i_imopVar85][j_imopVar86][k_imopVar87][4][4] = c1345 * tmp1;
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
            for (i_imopVar85 = 1; i_imopVar85 < grid_points[0] - 1; i_imopVar85++) {
                for (j_imopVar86 = 1; j_imopVar86 < grid_points[1] - 1; j_imopVar86++) {
                    for (k_imopVar87 = 1; k_imopVar87 < grid_points[2] - 1; k_imopVar87++) {
                        tmp1 = dt * ty1;
                        tmp2 = dt * ty2;
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][0][0] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][0] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][0] - tmp1 * dy1;
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][0][1] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][1] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][1];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][0][2] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][2] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][2];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][0][3] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][3] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][3];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][0][4] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][4] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][4];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][1][0] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][0] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][0];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][1][1] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][1] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][1] - tmp1 * dy2;
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][1][2] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][2] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][2];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][1][3] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][3] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][3];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][1][4] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][4] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][4];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][2][0] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][0] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][0];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][2][1] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][1] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][1];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][2][2] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][2] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][2] - tmp1 * dy3;
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][2][3] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][3] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][3];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][2][4] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][4] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][4];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][3][0] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][0] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][0];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][3][1] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][1] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][1];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][3][2] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][2] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][2];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][3][3] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][3] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][3] - tmp1 * dy4;
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][3][4] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][4] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][4];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][4][0] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][0] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][0];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][4][1] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][1] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][1];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][4][2] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][2] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][2];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][4][3] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][3] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][3];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][4][4] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][4] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][4] - tmp1 * dy5;
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][0][0] = 1.0 + tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][0][0] + tmp1 * 2.0 * dy1;
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][0][1] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][0][1];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][0][2] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][0][2];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][0][3] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][0][3];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][0][4] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][0][4];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][1][0] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][1][0];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][1][1] = 1.0 + tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][1][1] + tmp1 * 2.0 * dy2;
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][1][2] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][1][2];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][1][3] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][1][3];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][1][4] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][1][4];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][2][0] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][2][0];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][2][1] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][2][1];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][2][2] = 1.0 + tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][2][2] + tmp1 * 2.0 * dy3;
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][2][3] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][2][3];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][2][4] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][2][4];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][3][0] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][3][0];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][3][1] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][3][1];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][3][2] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][3][2];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][3][3] = 1.0 + tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][3][3] + tmp1 * 2.0 * dy4;
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][3][4] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][3][4];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][4][0] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][4][0];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][4][1] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][4][1];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][4][2] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][4][2];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][4][3] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][4][3];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][4][4] = 1.0 + tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][4][4] + tmp1 * 2.0 * dy5;
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][0][0] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][0] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][0] - tmp1 * dy1;
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][0][1] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][1] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][1];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][0][2] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][2] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][2];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][0][3] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][3] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][3];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][0][4] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][4] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][4];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][1][0] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][0] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][0];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][1][1] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][1] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][1] - tmp1 * dy2;
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][1][2] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][2] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][2];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][1][3] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][3] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][3];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][1][4] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][4] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][4];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][2][0] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][0] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][0];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][2][1] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][1] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][1];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][2][2] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][2] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][2] - tmp1 * dy3;
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][2][3] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][3] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][3];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][2][4] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][4] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][4];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][3][0] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][0] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][0];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][3][1] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][1] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][1];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][3][2] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][2] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][2];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][3][3] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][3] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][3] - tmp1 * dy4;
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][3][4] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][4] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][4];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][4][0] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][0] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][0];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][4][1] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][1] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][1];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][4][2] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][2] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][2];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][4][3] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][3] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][3];
                        lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][4][4] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][4] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][4] - tmp1 * dy5;
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            int i;
            int j;
            int k;
            int jsize;
            jsize = grid_points[1] - 1;
#pragma omp for nowait
            for (i = 1; i < grid_points[0] - 1; i++) {
                for (k = 1; k < grid_points[2] - 1; k++) {
                    double ( *_imopVarPre378 );
                    double ( *_imopVarPre379 )[5];
                    double ( *_imopVarPre380 )[5];
                    _imopVarPre378 = rhs[i][0][k];
                    _imopVarPre379 = lhs[i][0][k][2];
                    _imopVarPre380 = lhs[i][0][k][1];
                    binvcrhs(_imopVarPre380, _imopVarPre379, _imopVarPre378);
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            for (j = 1; j < jsize; j++) {
#pragma omp for nowait
                for (i = 1; i < grid_points[0] - 1; i++) {
                    for (k = 1; k < grid_points[2] - 1; k++) {
                        double ( *_imopVarPre384 );
                        double ( *_imopVarPre385 );
                        double ( *_imopVarPre386 )[5];
                        _imopVarPre384 = rhs[i][j][k];
                        _imopVarPre385 = rhs[i][j - 1][k];
                        _imopVarPre386 = lhs[i][j][k][0];
                        matvec_sub(_imopVarPre386, _imopVarPre385, _imopVarPre384);
                        double ( *_imopVarPre390 )[5];
                        double ( *_imopVarPre391 )[5];
                        double ( *_imopVarPre392 )[5];
                        _imopVarPre390 = lhs[i][j][k][1];
                        _imopVarPre391 = lhs[i][j - 1][k][2];
                        _imopVarPre392 = lhs[i][j][k][0];
                        matmul_sub(_imopVarPre392, _imopVarPre391, _imopVarPre390);
                        double ( *_imopVarPre396 );
                        double ( *_imopVarPre397 )[5];
                        double ( *_imopVarPre398 )[5];
                        _imopVarPre396 = rhs[i][j][k];
                        _imopVarPre397 = lhs[i][j][k][2];
                        _imopVarPre398 = lhs[i][j][k][1];
                        binvcrhs(_imopVarPre398, _imopVarPre397, _imopVarPre396);
                    }
                }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            }
#pragma omp for nowait
            for (i = 1; i < grid_points[0] - 1; i++) {
                for (k = 1; k < grid_points[2] - 1; k++) {
                    double ( *_imopVarPre402 );
                    double ( *_imopVarPre403 );
                    double ( *_imopVarPre404 )[5];
                    _imopVarPre402 = rhs[i][jsize][k];
                    _imopVarPre403 = rhs[i][jsize - 1][k];
                    _imopVarPre404 = lhs[i][jsize][k][0];
                    matvec_sub(_imopVarPre404, _imopVarPre403, _imopVarPre402);
                    double ( *_imopVarPre408 )[5];
                    double ( *_imopVarPre409 )[5];
                    double ( *_imopVarPre410 )[5];
                    _imopVarPre408 = lhs[i][jsize][k][1];
                    _imopVarPre409 = lhs[i][jsize - 1][k][2];
                    _imopVarPre410 = lhs[i][jsize][k][0];
                    matmul_sub(_imopVarPre410, _imopVarPre409, _imopVarPre408);
                    double ( *_imopVarPre413 );
                    double ( *_imopVarPre414 )[5];
                    _imopVarPre413 = rhs[i][jsize][k];
                    _imopVarPre414 = lhs[i][jsize][k][1];
                    binvrhs(_imopVarPre414, _imopVarPre413);
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            int i_imopVar82;
            int j_imopVar83;
            int k_imopVar84;
            int m;
            int n;
            for (j_imopVar83 = grid_points[1] - 2; j_imopVar83 >= 0; j_imopVar83--) {
#pragma omp for nowait
                for (i_imopVar82 = 1; i_imopVar82 < grid_points[0] - 1; i_imopVar82++) {
                    for (k_imopVar84 = 1; k_imopVar84 < grid_points[2] - 1; k_imopVar84++) {
                        for (m = 0; m < 5; m++) {
                            for (n = 0; n < 5; n++) {
                                rhs[i_imopVar82][j_imopVar83][k_imopVar84][m] = rhs[i_imopVar82][j_imopVar83][k_imopVar84][m] - lhs[i_imopVar82][j_imopVar83][k_imopVar84][2][m][n] * rhs[i_imopVar82][j_imopVar83 + 1][k_imopVar84][n];
                            }
                        }
                    }
                }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            int i_imopVar128;
            int j_imopVar129;
            int k_imopVar130;
#pragma omp for nowait
            for (i_imopVar128 = 1; i_imopVar128 < grid_points[0] - 1; i_imopVar128++) {
                for (j_imopVar129 = 1; j_imopVar129 < grid_points[1] - 1; j_imopVar129++) {
                    for (k_imopVar130 = 0; k_imopVar130 < grid_points[2]; k_imopVar130++) {
                        tmp1 = 1.0 / u[i_imopVar128][j_imopVar129][k_imopVar130][0];
                        tmp2 = tmp1 * tmp1;
                        tmp3 = tmp1 * tmp2;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][0][0] = 0.0;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][0][1] = 0.0;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][0][2] = 0.0;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][0][3] = 1.0;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][0][4] = 0.0;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][1][0] = -(u[i_imopVar128][j_imopVar129][k_imopVar130][1] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][1][1] = u[i_imopVar128][j_imopVar129][k_imopVar130][3] * tmp1;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][1][2] = 0.0;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][1][3] = u[i_imopVar128][j_imopVar129][k_imopVar130][1] * tmp1;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][1][4] = 0.0;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][2][0] = -(u[i_imopVar128][j_imopVar129][k_imopVar130][2] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][2][1] = 0.0;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][2][2] = u[i_imopVar128][j_imopVar129][k_imopVar130][3] * tmp1;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][2][3] = u[i_imopVar128][j_imopVar129][k_imopVar130][2] * tmp1;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][2][4] = 0.0;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][3][0] = -(u[i_imopVar128][j_imopVar129][k_imopVar130][3] * u[i_imopVar128][j_imopVar129][k_imopVar130][3] * tmp2) + 0.50 * c2 * ((u[i_imopVar128][j_imopVar129][k_imopVar130][1] * u[i_imopVar128][j_imopVar129][k_imopVar130][1] + u[i_imopVar128][j_imopVar129][k_imopVar130][2] * u[i_imopVar128][j_imopVar129][k_imopVar130][2] + u[i_imopVar128][j_imopVar129][k_imopVar130][3] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2);
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][3][1] = -c2 * u[i_imopVar128][j_imopVar129][k_imopVar130][1] * tmp1;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][3][2] = -c2 * u[i_imopVar128][j_imopVar129][k_imopVar130][2] * tmp1;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][3][3] = (2.0 - c2) * u[i_imopVar128][j_imopVar129][k_imopVar130][3] * tmp1;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][3][4] = c2;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][4][0] = (c2 * (u[i_imopVar128][j_imopVar129][k_imopVar130][1] * u[i_imopVar128][j_imopVar129][k_imopVar130][1] + u[i_imopVar128][j_imopVar129][k_imopVar130][2] * u[i_imopVar128][j_imopVar129][k_imopVar130][2] + u[i_imopVar128][j_imopVar129][k_imopVar130][3] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2 - c1 * (u[i_imopVar128][j_imopVar129][k_imopVar130][4] * tmp1)) * (u[i_imopVar128][j_imopVar129][k_imopVar130][3] * tmp1);
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][4][1] = -c2 * (u[i_imopVar128][j_imopVar129][k_imopVar130][1] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][4][2] = -c2 * (u[i_imopVar128][j_imopVar129][k_imopVar130][2] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2;
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][4][3] = c1 * (u[i_imopVar128][j_imopVar129][k_imopVar130][4] * tmp1) - 0.50 * c2 * ((u[i_imopVar128][j_imopVar129][k_imopVar130][1] * u[i_imopVar128][j_imopVar129][k_imopVar130][1] + u[i_imopVar128][j_imopVar129][k_imopVar130][2] * u[i_imopVar128][j_imopVar129][k_imopVar130][2] + 3.0 * u[i_imopVar128][j_imopVar129][k_imopVar130][3] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2);
                        fjac[i_imopVar128][j_imopVar129][k_imopVar130][4][4] = c1 * u[i_imopVar128][j_imopVar129][k_imopVar130][3] * tmp1;
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][0][0] = 0.0;
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][0][1] = 0.0;
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][0][2] = 0.0;
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][0][3] = 0.0;
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][0][4] = 0.0;
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][1][0] = -c3c4 * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][1];
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][1][1] = c3c4 * tmp1;
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][1][2] = 0.0;
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][1][3] = 0.0;
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][1][4] = 0.0;
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][2][0] = -c3c4 * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][2];
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][2][1] = 0.0;
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][2][2] = c3c4 * tmp1;
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][2][3] = 0.0;
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][2][4] = 0.0;
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][3][0] = -con43 * c3c4 * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][3];
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][3][1] = 0.0;
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][3][2] = 0.0;
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][3][3] = con43 * c3 * c4 * tmp1;
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][3][4] = 0.0;
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][4][0] = -(c3c4 - c1345) * tmp3 * (((u[i_imopVar128][j_imopVar129][k_imopVar130][1]) * (u[i_imopVar128][j_imopVar129][k_imopVar130][1]))) - (c3c4 - c1345) * tmp3 * (((u[i_imopVar128][j_imopVar129][k_imopVar130][2]) * (u[i_imopVar128][j_imopVar129][k_imopVar130][2]))) - (con43 * c3c4 - c1345) * tmp3 * (((u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * (u[i_imopVar128][j_imopVar129][k_imopVar130][3]))) - c1345 * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][4];
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][4][1] = (c3c4 - c1345) * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][1];
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][4][2] = (c3c4 - c1345) * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][2];
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][4][3] = (con43 * c3c4 - c1345) * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][3];
                        njac[i_imopVar128][j_imopVar129][k_imopVar130][4][4] = c1345 * tmp1;
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
            for (i_imopVar128 = 1; i_imopVar128 < grid_points[0] - 1; i_imopVar128++) {
                for (j_imopVar129 = 1; j_imopVar129 < grid_points[1] - 1; j_imopVar129++) {
                    for (k_imopVar130 = 1; k_imopVar130 < grid_points[2] - 1; k_imopVar130++) {
                        tmp1 = dt * tz1;
                        tmp2 = dt * tz2;
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][0][0] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][0] - tmp1 * dz1;
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][0][1] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][1];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][0][2] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][2];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][0][3] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][3];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][0][4] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][4];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][1][0] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][0];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][1][1] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][1] - tmp1 * dz2;
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][1][2] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][2];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][1][3] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][3];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][1][4] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][4];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][2][0] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][0];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][2][1] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][1];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][2][2] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][2] - tmp1 * dz3;
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][2][3] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][3];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][2][4] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][4];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][3][0] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][0];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][3][1] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][1];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][3][2] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][2];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][3][3] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][3] - tmp1 * dz4;
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][3][4] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][4];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][4][0] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][0];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][4][1] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][1];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][4][2] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][2];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][4][3] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][3];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][4][4] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][4] - tmp1 * dz5;
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][0][0] = 1.0 + tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][0][0] + tmp1 * 2.0 * dz1;
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][0][1] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][0][1];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][0][2] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][0][2];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][0][3] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][0][3];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][0][4] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][0][4];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][1][0] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][1][0];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][1][1] = 1.0 + tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][1][1] + tmp1 * 2.0 * dz2;
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][1][2] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][1][2];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][1][3] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][1][3];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][1][4] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][1][4];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][2][0] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][2][0];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][2][1] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][2][1];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][2][2] = 1.0 + tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][2][2] + tmp1 * 2.0 * dz3;
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][2][3] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][2][3];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][2][4] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][2][4];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][3][0] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][3][0];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][3][1] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][3][1];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][3][2] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][3][2];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][3][3] = 1.0 + tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][3][3] + tmp1 * 2.0 * dz4;
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][3][4] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][3][4];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][4][0] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][4][0];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][4][1] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][4][1];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][4][2] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][4][2];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][4][3] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][4][3];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][4][4] = 1.0 + tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][4][4] + tmp1 * 2.0 * dz5;
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][0][0] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][0] - tmp1 * dz1;
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][0][1] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][1];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][0][2] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][2];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][0][3] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][3];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][0][4] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][4];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][1][0] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][0];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][1][1] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][1] - tmp1 * dz2;
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][1][2] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][2];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][1][3] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][3];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][1][4] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][4];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][2][0] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][0];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][2][1] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][1];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][2][2] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][2] - tmp1 * dz3;
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][2][3] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][3];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][2][4] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][4];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][3][0] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][0];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][3][1] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][1];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][3][2] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][2];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][3][3] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][3] - tmp1 * dz4;
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][3][4] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][4];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][4][0] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][0];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][4][1] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][1];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][4][2] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][2];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][4][3] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][3];
                        lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][4][4] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][4] - tmp1 * dz5;
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            int i_imopVar125;
            int j_imopVar126;
            int k_imopVar127;
            int ksize;
            ksize = grid_points[2] - 1;
#pragma omp for nowait
            for (i_imopVar125 = 1; i_imopVar125 < grid_points[0] - 1; i_imopVar125++) {
                for (j_imopVar126 = 1; j_imopVar126 < grid_points[1] - 1; j_imopVar126++) {
                    double ( *_imopVarPre418 );
                    double ( *_imopVarPre419 )[5];
                    double ( *_imopVarPre420 )[5];
                    _imopVarPre418 = rhs[i_imopVar125][j_imopVar126][0];
                    _imopVarPre419 = lhs[i_imopVar125][j_imopVar126][0][2];
                    _imopVarPre420 = lhs[i_imopVar125][j_imopVar126][0][1];
                    binvcrhs(_imopVarPre420, _imopVarPre419, _imopVarPre418);
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            for (k_imopVar127 = 1; k_imopVar127 < ksize; k_imopVar127++) {
#pragma omp for nowait
                for (i_imopVar125 = 1; i_imopVar125 < grid_points[0] - 1; i_imopVar125++) {
                    for (j_imopVar126 = 1; j_imopVar126 < grid_points[1] - 1; j_imopVar126++) {
                        double ( *_imopVarPre424 );
                        double ( *_imopVarPre425 );
                        double ( *_imopVarPre426 )[5];
                        _imopVarPre424 = rhs[i_imopVar125][j_imopVar126][k_imopVar127];
                        _imopVarPre425 = rhs[i_imopVar125][j_imopVar126][k_imopVar127 - 1];
                        _imopVarPre426 = lhs[i_imopVar125][j_imopVar126][k_imopVar127][0];
                        matvec_sub(_imopVarPre426, _imopVarPre425, _imopVarPre424);
                        double ( *_imopVarPre430 )[5];
                        double ( *_imopVarPre431 )[5];
                        double ( *_imopVarPre432 )[5];
                        _imopVarPre430 = lhs[i_imopVar125][j_imopVar126][k_imopVar127][1];
                        _imopVarPre431 = lhs[i_imopVar125][j_imopVar126][k_imopVar127 - 1][2];
                        _imopVarPre432 = lhs[i_imopVar125][j_imopVar126][k_imopVar127][0];
                        matmul_sub(_imopVarPre432, _imopVarPre431, _imopVarPre430);
                        double ( *_imopVarPre436 );
                        double ( *_imopVarPre437 )[5];
                        double ( *_imopVarPre438 )[5];
                        _imopVarPre436 = rhs[i_imopVar125][j_imopVar126][k_imopVar127];
                        _imopVarPre437 = lhs[i_imopVar125][j_imopVar126][k_imopVar127][2];
                        _imopVarPre438 = lhs[i_imopVar125][j_imopVar126][k_imopVar127][1];
                        binvcrhs(_imopVarPre438, _imopVarPre437, _imopVarPre436);
                    }
                }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            }
#pragma omp for nowait
            for (i_imopVar125 = 1; i_imopVar125 < grid_points[0] - 1; i_imopVar125++) {
                for (j_imopVar126 = 1; j_imopVar126 < grid_points[1] - 1; j_imopVar126++) {
                    double ( *_imopVarPre442 );
                    double ( *_imopVarPre443 );
                    double ( *_imopVarPre444 )[5];
                    _imopVarPre442 = rhs[i_imopVar125][j_imopVar126][ksize];
                    _imopVarPre443 = rhs[i_imopVar125][j_imopVar126][ksize - 1];
                    _imopVarPre444 = lhs[i_imopVar125][j_imopVar126][ksize][0];
                    matvec_sub(_imopVarPre444, _imopVarPre443, _imopVarPre442);
                    double ( *_imopVarPre448 )[5];
                    double ( *_imopVarPre449 )[5];
                    double ( *_imopVarPre450 )[5];
                    _imopVarPre448 = lhs[i_imopVar125][j_imopVar126][ksize][1];
                    _imopVarPre449 = lhs[i_imopVar125][j_imopVar126][ksize - 1][2];
                    _imopVarPre450 = lhs[i_imopVar125][j_imopVar126][ksize][0];
                    matmul_sub(_imopVarPre450, _imopVarPre449, _imopVarPre448);
                    double ( *_imopVarPre453 );
                    double ( *_imopVarPre454 )[5];
                    _imopVarPre453 = rhs[i_imopVar125][j_imopVar126][ksize];
                    _imopVarPre454 = lhs[i_imopVar125][j_imopVar126][ksize][1];
                    binvrhs(_imopVarPre454, _imopVarPre453);
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            int i_imopVar103;
            int j_imopVar104;
            int k_imopVar105;
            int m_imopVar131;
            int n_imopVar132;
#pragma omp for nowait
            for (i_imopVar103 = 1; i_imopVar103 < grid_points[0] - 1; i_imopVar103++) {
                for (j_imopVar104 = 1; j_imopVar104 < grid_points[1] - 1; j_imopVar104++) {
                    for (k_imopVar105 = grid_points[2] - 2; k_imopVar105 >= 0; k_imopVar105--) {
                        for (m_imopVar131 = 0; m_imopVar131 < 5; m_imopVar131++) {
                            for (n_imopVar132 = 0; n_imopVar132 < 5; n_imopVar132++) {
                                rhs[i_imopVar103][j_imopVar104][k_imopVar105][m_imopVar131] = rhs[i_imopVar103][j_imopVar104][k_imopVar105][m_imopVar131] - lhs[i_imopVar103][j_imopVar104][k_imopVar105][2][m_imopVar131][n_imopVar132] * rhs[i_imopVar103][j_imopVar104][k_imopVar105 + 1][n_imopVar132];
                            }
                        }
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
            int i_imopVar106;
            int j_imopVar107;
            int k_imopVar108;
            int m_imopVar109;
#pragma omp for nowait
            for (i_imopVar106 = 1; i_imopVar106 < grid_points[0] - 1; i_imopVar106++) {
                for (j_imopVar107 = 1; j_imopVar107 < grid_points[1] - 1; j_imopVar107++) {
                    for (k_imopVar108 = 1; k_imopVar108 < grid_points[2] - 1; k_imopVar108++) {
                        for (m_imopVar109 = 0; m_imopVar109 < 5; m_imopVar109++) {
                            u[i_imopVar106][j_imopVar107][k_imopVar108][m_imopVar109] = u[i_imopVar106][j_imopVar107][k_imopVar108][m_imopVar109] + rhs[i_imopVar106][j_imopVar107][k_imopVar108][m_imopVar109];
                        }
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
        }
    }
#pragma omp parallel
    {
#pragma omp master
        {
            timer_stop(1);
            tmax = timer_read(1);
            _imopVarPre175 = &verified;
            _imopVarPre176 = &class;
        }
    }
    int no_time_steps;
    char *class_imopVar134;
    int *verified_imopVar135;
    no_time_steps = niter;
    class_imopVar134 = _imopVarPre176;
    verified_imopVar135 = _imopVarPre175;
    double xcrref[5];
    double xceref[5];
    double xcrdif[5];
    double xcedif[5];
    double epsilon;
    double xce[5];
    double xcr[5];
    double dtref;
    int m;
    epsilon = 1.0e-08;
    error_norm(xce);
    int i;
    int j;
    int k;
    int m_imopVar133;
    double rho_inv;
    double uijk;
    double up1;
    double um1;
    double vijk;
    double vp1;
    double vm1;
    double wijk;
    double wp1;
    double wm1;
#pragma omp for nowait
    for (i = 0; i < grid_points[0]; i++) {
        for (j = 0; j < grid_points[1]; j++) {
            for (k = 0; k < grid_points[2]; k++) {
                rho_inv = 1.0 / u[i][j][k][0];
                rho_i[i][j][k] = rho_inv;
                us[i][j][k] = u[i][j][k][1] * rho_inv;
                vs[i][j][k] = u[i][j][k][2] * rho_inv;
                ws[i][j][k] = u[i][j][k][3] * rho_inv;
                square[i][j][k] = 0.5 * (u[i][j][k][1] * u[i][j][k][1] + u[i][j][k][2] * u[i][j][k][2] + u[i][j][k][3] * u[i][j][k][3]) * rho_inv;
                qs[i][j][k] = square[i][j][k] * rho_inv;
            }
        }
    }
#pragma omp for nowait
    for (i = 0; i < grid_points[0]; i++) {
        for (j = 0; j < grid_points[1]; j++) {
            for (k = 0; k < grid_points[2]; k++) {
                for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                    rhs[i][j][k][m_imopVar133] = forcing[i][j][k][m_imopVar133];
                }
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                uijk = us[i][j][k];
                up1 = us[i + 1][j][k];
                um1 = us[i - 1][j][k];
                rhs[i][j][k][0] = rhs[i][j][k][0] + dx1tx1 * (u[i + 1][j][k][0] - 2.0 * u[i][j][k][0] + u[i - 1][j][k][0]) - tx2 * (u[i + 1][j][k][1] - u[i - 1][j][k][1]);
                rhs[i][j][k][1] = rhs[i][j][k][1] + dx2tx1 * (u[i + 1][j][k][1] - 2.0 * u[i][j][k][1] + u[i - 1][j][k][1]) + xxcon2 * con43 * (up1 - 2.0 * uijk + um1) - tx2 * (u[i + 1][j][k][1] * up1 - u[i - 1][j][k][1] * um1 + (u[i + 1][j][k][4] - square[i + 1][j][k] - u[i - 1][j][k][4] + square[i - 1][j][k]) * c2);
                rhs[i][j][k][2] = rhs[i][j][k][2] + dx3tx1 * (u[i + 1][j][k][2] - 2.0 * u[i][j][k][2] + u[i - 1][j][k][2]) + xxcon2 * (vs[i + 1][j][k] - 2.0 * vs[i][j][k] + vs[i - 1][j][k]) - tx2 * (u[i + 1][j][k][2] * up1 - u[i - 1][j][k][2] * um1);
                rhs[i][j][k][3] = rhs[i][j][k][3] + dx4tx1 * (u[i + 1][j][k][3] - 2.0 * u[i][j][k][3] + u[i - 1][j][k][3]) + xxcon2 * (ws[i + 1][j][k] - 2.0 * ws[i][j][k] + ws[i - 1][j][k]) - tx2 * (u[i + 1][j][k][3] * up1 - u[i - 1][j][k][3] * um1);
                rhs[i][j][k][4] = rhs[i][j][k][4] + dx5tx1 * (u[i + 1][j][k][4] - 2.0 * u[i][j][k][4] + u[i - 1][j][k][4]) + xxcon3 * (qs[i + 1][j][k] - 2.0 * qs[i][j][k] + qs[i - 1][j][k]) + xxcon4 * (up1 * up1 - 2.0 * uijk * uijk + um1 * um1) + xxcon5 * (u[i + 1][j][k][4] * rho_i[i + 1][j][k] - 2.0 * u[i][j][k][4] * rho_i[i][j][k] + u[i - 1][j][k][4] * rho_i[i - 1][j][k]) - tx2 * ((c1 * u[i + 1][j][k][4] - c2 * square[i + 1][j][k]) * up1 - (c1 * u[i - 1][j][k][4] - c2 * square[i - 1][j][k]) * um1);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    i = 1;
#pragma omp for nowait
    for (j = 1; j < grid_points[1] - 1; j++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (5.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i + 1][j][k][m_imopVar133] + u[i + 2][j][k][m_imopVar133]);
            }
        }
    }
    i = 2;
#pragma omp for nowait
    for (j = 1; j < grid_points[1] - 1; j++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (-4.0 * u[i - 1][j][k][m_imopVar133] + 6.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i + 1][j][k][m_imopVar133] + u[i + 2][j][k][m_imopVar133]);
            }
        }
    }
#pragma omp for nowait
    for (i = 3; i < grid_points[0] - 3; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                    rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (u[i - 2][j][k][m_imopVar133] - 4.0 * u[i - 1][j][k][m_imopVar133] + 6.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i + 1][j][k][m_imopVar133] + u[i + 2][j][k][m_imopVar133]);
                }
            }
        }
    }
    i = grid_points[0] - 3;
#pragma omp for nowait
    for (j = 1; j < grid_points[1] - 1; j++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (u[i - 2][j][k][m_imopVar133] - 4.0 * u[i - 1][j][k][m_imopVar133] + 6.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i + 1][j][k][m_imopVar133]);
            }
        }
    }
    i = grid_points[0] - 2;
#pragma omp for nowait
    for (j = 1; j < grid_points[1] - 1; j++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (u[i - 2][j][k][m_imopVar133] - 4. * u[i - 1][j][k][m_imopVar133] + 5.0 * u[i][j][k][m_imopVar133]);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                vijk = vs[i][j][k];
                vp1 = vs[i][j + 1][k];
                vm1 = vs[i][j - 1][k];
                rhs[i][j][k][0] = rhs[i][j][k][0] + dy1ty1 * (u[i][j + 1][k][0] - 2.0 * u[i][j][k][0] + u[i][j - 1][k][0]) - ty2 * (u[i][j + 1][k][2] - u[i][j - 1][k][2]);
                rhs[i][j][k][1] = rhs[i][j][k][1] + dy2ty1 * (u[i][j + 1][k][1] - 2.0 * u[i][j][k][1] + u[i][j - 1][k][1]) + yycon2 * (us[i][j + 1][k] - 2.0 * us[i][j][k] + us[i][j - 1][k]) - ty2 * (u[i][j + 1][k][1] * vp1 - u[i][j - 1][k][1] * vm1);
                rhs[i][j][k][2] = rhs[i][j][k][2] + dy3ty1 * (u[i][j + 1][k][2] - 2.0 * u[i][j][k][2] + u[i][j - 1][k][2]) + yycon2 * con43 * (vp1 - 2.0 * vijk + vm1) - ty2 * (u[i][j + 1][k][2] * vp1 - u[i][j - 1][k][2] * vm1 + (u[i][j + 1][k][4] - square[i][j + 1][k] - u[i][j - 1][k][4] + square[i][j - 1][k]) * c2);
                rhs[i][j][k][3] = rhs[i][j][k][3] + dy4ty1 * (u[i][j + 1][k][3] - 2.0 * u[i][j][k][3] + u[i][j - 1][k][3]) + yycon2 * (ws[i][j + 1][k] - 2.0 * ws[i][j][k] + ws[i][j - 1][k]) - ty2 * (u[i][j + 1][k][3] * vp1 - u[i][j - 1][k][3] * vm1);
                rhs[i][j][k][4] = rhs[i][j][k][4] + dy5ty1 * (u[i][j + 1][k][4] - 2.0 * u[i][j][k][4] + u[i][j - 1][k][4]) + yycon3 * (qs[i][j + 1][k] - 2.0 * qs[i][j][k] + qs[i][j - 1][k]) + yycon4 * (vp1 * vp1 - 2.0 * vijk * vijk + vm1 * vm1) + yycon5 * (u[i][j + 1][k][4] * rho_i[i][j + 1][k] - 2.0 * u[i][j][k][4] * rho_i[i][j][k] + u[i][j - 1][k][4] * rho_i[i][j - 1][k]) - ty2 * ((c1 * u[i][j + 1][k][4] - c2 * square[i][j + 1][k]) * vp1 - (c1 * u[i][j - 1][k][4] - c2 * square[i][j - 1][k]) * vm1);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    j = 1;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (5.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i][j + 1][k][m_imopVar133] + u[i][j + 2][k][m_imopVar133]);
            }
        }
    }
    j = 2;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (-4.0 * u[i][j - 1][k][m_imopVar133] + 6.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i][j + 1][k][m_imopVar133] + u[i][j + 2][k][m_imopVar133]);
            }
        }
    }
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 3; j < grid_points[1] - 3; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                    rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (u[i][j - 2][k][m_imopVar133] - 4.0 * u[i][j - 1][k][m_imopVar133] + 6.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i][j + 1][k][m_imopVar133] + u[i][j + 2][k][m_imopVar133]);
                }
            }
        }
    }
    j = grid_points[1] - 3;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (u[i][j - 2][k][m_imopVar133] - 4.0 * u[i][j - 1][k][m_imopVar133] + 6.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i][j + 1][k][m_imopVar133]);
            }
        }
    }
    j = grid_points[1] - 2;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (u[i][j - 2][k][m_imopVar133] - 4. * u[i][j - 1][k][m_imopVar133] + 5. * u[i][j][k][m_imopVar133]);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                wijk = ws[i][j][k];
                wp1 = ws[i][j][k + 1];
                wm1 = ws[i][j][k - 1];
                rhs[i][j][k][0] = rhs[i][j][k][0] + dz1tz1 * (u[i][j][k + 1][0] - 2.0 * u[i][j][k][0] + u[i][j][k - 1][0]) - tz2 * (u[i][j][k + 1][3] - u[i][j][k - 1][3]);
                rhs[i][j][k][1] = rhs[i][j][k][1] + dz2tz1 * (u[i][j][k + 1][1] - 2.0 * u[i][j][k][1] + u[i][j][k - 1][1]) + zzcon2 * (us[i][j][k + 1] - 2.0 * us[i][j][k] + us[i][j][k - 1]) - tz2 * (u[i][j][k + 1][1] * wp1 - u[i][j][k - 1][1] * wm1);
                rhs[i][j][k][2] = rhs[i][j][k][2] + dz3tz1 * (u[i][j][k + 1][2] - 2.0 * u[i][j][k][2] + u[i][j][k - 1][2]) + zzcon2 * (vs[i][j][k + 1] - 2.0 * vs[i][j][k] + vs[i][j][k - 1]) - tz2 * (u[i][j][k + 1][2] * wp1 - u[i][j][k - 1][2] * wm1);
                rhs[i][j][k][3] = rhs[i][j][k][3] + dz4tz1 * (u[i][j][k + 1][3] - 2.0 * u[i][j][k][3] + u[i][j][k - 1][3]) + zzcon2 * con43 * (wp1 - 2.0 * wijk + wm1) - tz2 * (u[i][j][k + 1][3] * wp1 - u[i][j][k - 1][3] * wm1 + (u[i][j][k + 1][4] - square[i][j][k + 1] - u[i][j][k - 1][4] + square[i][j][k - 1]) * c2);
                rhs[i][j][k][4] = rhs[i][j][k][4] + dz5tz1 * (u[i][j][k + 1][4] - 2.0 * u[i][j][k][4] + u[i][j][k - 1][4]) + zzcon3 * (qs[i][j][k + 1] - 2.0 * qs[i][j][k] + qs[i][j][k - 1]) + zzcon4 * (wp1 * wp1 - 2.0 * wijk * wijk + wm1 * wm1) + zzcon5 * (u[i][j][k + 1][4] * rho_i[i][j][k + 1] - 2.0 * u[i][j][k][4] * rho_i[i][j][k] + u[i][j][k - 1][4] * rho_i[i][j][k - 1]) - tz2 * ((c1 * u[i][j][k + 1][4] - c2 * square[i][j][k + 1]) * wp1 - (c1 * u[i][j][k - 1][4] - c2 * square[i][j][k - 1]) * wm1);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    k = 1;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (5.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i][j][k + 1][m_imopVar133] + u[i][j][k + 2][m_imopVar133]);
            }
        }
    }
    k = 2;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (-4.0 * u[i][j][k - 1][m_imopVar133] + 6.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i][j][k + 1][m_imopVar133] + u[i][j][k + 2][m_imopVar133]);
            }
        }
    }
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 3; k < grid_points[2] - 3; k++) {
                for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                    rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (u[i][j][k - 2][m_imopVar133] - 4.0 * u[i][j][k - 1][m_imopVar133] + 6.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i][j][k + 1][m_imopVar133] + u[i][j][k + 2][m_imopVar133]);
                }
            }
        }
    }
    k = grid_points[2] - 3;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (u[i][j][k - 2][m_imopVar133] - 4.0 * u[i][j][k - 1][m_imopVar133] + 6.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i][j][k + 1][m_imopVar133]);
            }
        }
    }
    k = grid_points[2] - 2;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (u[i][j][k - 2][m_imopVar133] - 4.0 * u[i][j][k - 1][m_imopVar133] + 5.0 * u[i][j][k][m_imopVar133]);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (j = 1; j < grid_points[1] - 1; j++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                for (i = 1; i < grid_points[0] - 1; i++) {
                    rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] * dt;
                }
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    rhs_norm(xcr);
    for (m = 0; m < 5; m++) {
        xcr[m] = xcr[m] / dt;
    }
    *class_imopVar134 = 'U';
    *verified_imopVar135 = 1;
    for (m = 0; m < 5; m++) {
        xcrref[m] = 1.0;
        xceref[m] = 1.0;
    }
    int _imopVarPre268;
    int _imopVarPre269;
    int _imopVarPre270;
    _imopVarPre268 = grid_points[0] == 12;
    if (_imopVarPre268) {
        _imopVarPre269 = grid_points[1] == 12;
        if (_imopVarPre269) {
            _imopVarPre270 = grid_points[2] == 12;
            if (_imopVarPre270) {
                _imopVarPre270 = no_time_steps == 60;
            }
            _imopVarPre269 = _imopVarPre270;
        }
        _imopVarPre268 = _imopVarPre269;
    }
    if (_imopVarPre268) {
        *class_imopVar134 = 'S';
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
    } else {
        int _imopVarPre274;
        int _imopVarPre275;
        int _imopVarPre276;
        _imopVarPre274 = grid_points[0] == 24;
        if (_imopVarPre274) {
            _imopVarPre275 = grid_points[1] == 24;
            if (_imopVarPre275) {
                _imopVarPre276 = grid_points[2] == 24;
                if (_imopVarPre276) {
                    _imopVarPre276 = no_time_steps == 200;
                }
                _imopVarPre275 = _imopVarPre276;
            }
            _imopVarPre274 = _imopVarPre275;
        }
        if (_imopVarPre274) {
            *class_imopVar134 = 'W';
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
        } else {
            int _imopVarPre280;
            int _imopVarPre281;
            int _imopVarPre282;
            _imopVarPre280 = grid_points[0] == 64;
            if (_imopVarPre280) {
                _imopVarPre281 = grid_points[1] == 64;
                if (_imopVarPre281) {
                    _imopVarPre282 = grid_points[2] == 64;
                    if (_imopVarPre282) {
                        _imopVarPre282 = no_time_steps == 200;
                    }
                    _imopVarPre281 = _imopVarPre282;
                }
                _imopVarPre280 = _imopVarPre281;
            }
            if (_imopVarPre280) {
                *class_imopVar134 = 'A';
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
            } else {
                int _imopVarPre286;
                int _imopVarPre287;
                int _imopVarPre288;
                _imopVarPre286 = grid_points[0] == 102;
                if (_imopVarPre286) {
                    _imopVarPre287 = grid_points[1] == 102;
                    if (_imopVarPre287) {
                        _imopVarPre288 = grid_points[2] == 102;
                        if (_imopVarPre288) {
                            _imopVarPre288 = no_time_steps == 200;
                        }
                        _imopVarPre287 = _imopVarPre288;
                    }
                    _imopVarPre286 = _imopVarPre287;
                }
                if (_imopVarPre286) {
                    *class_imopVar134 = 'B';
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
                } else {
                    int _imopVarPre292;
                    int _imopVarPre293;
                    int _imopVarPre294;
                    _imopVarPre292 = grid_points[0] == 162;
                    if (_imopVarPre292) {
                        _imopVarPre293 = grid_points[1] == 162;
                        if (_imopVarPre293) {
                            _imopVarPre294 = grid_points[2] == 162;
                            if (_imopVarPre294) {
                                _imopVarPre294 = no_time_steps == 200;
                            }
                            _imopVarPre293 = _imopVarPre294;
                        }
                        _imopVarPre292 = _imopVarPre293;
                    }
                    if (_imopVarPre292) {
                        *class_imopVar134 = 'C';
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
                        *verified_imopVar135 = 0;
                    }
                }
            }
        }
    }
    for (m = 0; m < 5; m++) {
        double _imopVarPre296;
        double _imopVarPre297;
        _imopVarPre296 = (xcr[m] - xcrref[m]) / xcrref[m];
        _imopVarPre297 = fabs(_imopVarPre296);
        xcrdif[m] = _imopVarPre297;
        double _imopVarPre299;
        double _imopVarPre300;
        _imopVarPre299 = (xce[m] - xceref[m]) / xceref[m];
        _imopVarPre300 = fabs(_imopVarPre299);
        xcedif[m] = _imopVarPre300;
    }
    if (*class_imopVar134 != 'U') {
        char _imopVarPre302;
        _imopVarPre302 = *class_imopVar134;
        printf(" Verification being performed for class %1c\n", _imopVarPre302);
        printf(" accuracy setting for epsilon = %20.13e\n", epsilon);
        double _imopVarPre305;
        double _imopVarPre306;
        _imopVarPre305 = dt - dtref;
        _imopVarPre306 = fabs(_imopVarPre305);
        if (_imopVarPre306 > epsilon) {
            *verified_imopVar135 = 0;
            *class_imopVar134 = 'U';
            printf(" DT does not match the reference value of %15.8e\n", dtref);
        }
    } else {
        printf(" Unknown class\n");
    }
    if (*class_imopVar134 != 'U') {
        printf(" Comparison of RMS-norms of residual\n");
    } else {
        printf(" RMS-norms of residual\n");
    }
    for (m = 0; m < 5; m++) {
        if (*class_imopVar134 == 'U') {
            double _imopVarPre308;
            _imopVarPre308 = xcr[m];
            printf("          %2d%20.13e\n", m, _imopVarPre308);
        } else {
            if (xcrdif[m] > epsilon) {
                *verified_imopVar135 = 0;
                double _imopVarPre312;
                double _imopVarPre313;
                double _imopVarPre314;
                _imopVarPre312 = xcrdif[m];
                _imopVarPre313 = xcrref[m];
                _imopVarPre314 = xcr[m];
                printf(" FAILURE: %2d%20.13e%20.13e%20.13e\n", m, _imopVarPre314, _imopVarPre313, _imopVarPre312);
            } else {
                double _imopVarPre318;
                double _imopVarPre319;
                double _imopVarPre320;
                _imopVarPre318 = xcrdif[m];
                _imopVarPre319 = xcrref[m];
                _imopVarPre320 = xcr[m];
                printf("          %2d%20.13e%20.13e%20.13e\n", m, _imopVarPre320, _imopVarPre319, _imopVarPre318);
            }
        }
    }
    if (*class_imopVar134 != 'U') {
        printf(" Comparison of RMS-norms of solution error\n");
    } else {
        printf(" RMS-norms of solution error\n");
    }
    for (m = 0; m < 5; m++) {
        if (*class_imopVar134 == 'U') {
            double _imopVarPre322;
            _imopVarPre322 = xce[m];
            printf("          %2d%20.13e\n", m, _imopVarPre322);
        } else {
            if (xcedif[m] > epsilon) {
                *verified_imopVar135 = 0;
                double _imopVarPre326;
                double _imopVarPre327;
                double _imopVarPre328;
                _imopVarPre326 = xcedif[m];
                _imopVarPre327 = xceref[m];
                _imopVarPre328 = xce[m];
                printf(" FAILURE: %2d%20.13e%20.13e%20.13e\n", m, _imopVarPre328, _imopVarPre327, _imopVarPre326);
            } else {
                double _imopVarPre332;
                double _imopVarPre333;
                double _imopVarPre334;
                _imopVarPre332 = xcedif[m];
                _imopVarPre333 = xceref[m];
                _imopVarPre334 = xce[m];
                printf("          %2d%20.13e%20.13e%20.13e\n", m, _imopVarPre334, _imopVarPre333, _imopVarPre332);
            }
        }
    }
    if (*class_imopVar134 == 'U') {
        printf(" No reference values provided\n");
        printf(" No verification performed\n");
    } else {
        if (*verified_imopVar135 == 1) {
            printf(" Verification Successful\n");
        } else {
            printf(" Verification failed\n");
        }
    }
    n3 = grid_points[0] * grid_points[1] * grid_points[2];
    navg = (grid_points[0] + grid_points[1] + grid_points[2]) / 3.0;
    if (tmax != 0.0) {
        mflops = 1.0e-6 * (double) niter * (3478.8 * (double) n3 - 17655.7 * (navg * navg) + 28023.7 * navg) / tmax;
    } else {
        mflops = 0.0;
    }
    int _imopVarPre180;
    int _imopVarPre181;
    int _imopVarPre182;
    _imopVarPre180 = grid_points[2];
    _imopVarPre181 = grid_points[1];
    _imopVarPre182 = grid_points[0];
    c_print_results("BT", class, _imopVarPre182, _imopVarPre181, _imopVarPre180, niter, nthreads, tmax, mflops, "          floating point", verified, "3.0 structured", "15 Jul 2017", "gcc", "gcc", "(none)", "-I../common", "-O3 -fopenmp", "-O3 -fopenmp", "(none)");
}
static void add(void ) {
    int i;
    int j;
    int k;
    int m;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                for (m = 0; m < 5; m++) {
                    u[i][j][k][m] = u[i][j][k][m] + rhs[i][j][k][m];
                }
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written([u.f]) read([square, &class, zzcon1, grid_points, dnym1, zzcon5, u, dz3tz1, c2, xxcon2, verify, dx5tx1, dz2tz1, qs, ws, yycon4, us, dy3ty1, dz1tz1, u.f, zzcon2, dx4tx1, dnzm1, dy2ty1, rho_i, xxcon5, xxcon1, dx3tx1, tz2, yycon1, error_norm, dy1ty1, tx2, qs.f, ws.f, j, yycon5, us.f, compute_rhs, dx2tx1, rho_i.f, xce.f, _imopVarPre172, dy5ty1, zzcon3, rhs, forcing, xxcon4, sqrt, dy4ty1, dx1tx1, timer_stop, ce, vs, exact_solution, i, yycon2, dnxm1, square.f, grid_points.f, zzcon4, &verified, forcing.f, rhs.f, step, c1, dz5tz1, xxcon3, dssp, timer_read, i, ty2, ce.f, printf, dz4tz1, yycon3, vs.f])
#pragma omp barrier
}
static void adi(void ) {
    int i_imopVar110;
    int j_imopVar111;
    int k_imopVar112;
    int m_imopVar113;
    double rho_inv;
    double uijk;
    double up1;
    double um1;
    double vijk;
    double vp1;
    double vm1;
    double wijk;
    double wp1;
    double wm1;
#pragma omp for nowait
    for (i_imopVar110 = 0; i_imopVar110 < grid_points[0]; i_imopVar110++) {
        for (j_imopVar111 = 0; j_imopVar111 < grid_points[1]; j_imopVar111++) {
            for (k_imopVar112 = 0; k_imopVar112 < grid_points[2]; k_imopVar112++) {
                rho_inv = 1.0 / u[i_imopVar110][j_imopVar111][k_imopVar112][0];
                rho_i[i_imopVar110][j_imopVar111][k_imopVar112] = rho_inv;
                us[i_imopVar110][j_imopVar111][k_imopVar112] = u[i_imopVar110][j_imopVar111][k_imopVar112][1] * rho_inv;
                vs[i_imopVar110][j_imopVar111][k_imopVar112] = u[i_imopVar110][j_imopVar111][k_imopVar112][2] * rho_inv;
                ws[i_imopVar110][j_imopVar111][k_imopVar112] = u[i_imopVar110][j_imopVar111][k_imopVar112][3] * rho_inv;
                square[i_imopVar110][j_imopVar111][k_imopVar112] = 0.5 * (u[i_imopVar110][j_imopVar111][k_imopVar112][1] * u[i_imopVar110][j_imopVar111][k_imopVar112][1] + u[i_imopVar110][j_imopVar111][k_imopVar112][2] * u[i_imopVar110][j_imopVar111][k_imopVar112][2] + u[i_imopVar110][j_imopVar111][k_imopVar112][3] * u[i_imopVar110][j_imopVar111][k_imopVar112][3]) * rho_inv;
                qs[i_imopVar110][j_imopVar111][k_imopVar112] = square[i_imopVar110][j_imopVar111][k_imopVar112] * rho_inv;
            }
        }
    }
#pragma omp for nowait
    for (i_imopVar110 = 0; i_imopVar110 < grid_points[0]; i_imopVar110++) {
        for (j_imopVar111 = 0; j_imopVar111 < grid_points[1]; j_imopVar111++) {
            for (k_imopVar112 = 0; k_imopVar112 < grid_points[2]; k_imopVar112++) {
                for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = forcing[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113];
                }
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
        for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
            for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                uijk = us[i_imopVar110][j_imopVar111][k_imopVar112];
                up1 = us[i_imopVar110 + 1][j_imopVar111][k_imopVar112];
                um1 = us[i_imopVar110 - 1][j_imopVar111][k_imopVar112];
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][0] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][0] + dx1tx1 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][0] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][0] + u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][0]) - tx2 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][1] - u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][1]);
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][1] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][1] + dx2tx1 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][1] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][1] + u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][1]) + xxcon2 * con43 * (up1 - 2.0 * uijk + um1) - tx2 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][1] * up1 - u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][1] * um1 + (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][4] - square[i_imopVar110 + 1][j_imopVar111][k_imopVar112] - u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][4] + square[i_imopVar110 - 1][j_imopVar111][k_imopVar112]) * c2);
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][2] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][2] + dx3tx1 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][2] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][2] + u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][2]) + xxcon2 * (vs[i_imopVar110 + 1][j_imopVar111][k_imopVar112] - 2.0 * vs[i_imopVar110][j_imopVar111][k_imopVar112] + vs[i_imopVar110 - 1][j_imopVar111][k_imopVar112]) - tx2 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][2] * up1 - u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][2] * um1);
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][3] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][3] + dx4tx1 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][3] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][3] + u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][3]) + xxcon2 * (ws[i_imopVar110 + 1][j_imopVar111][k_imopVar112] - 2.0 * ws[i_imopVar110][j_imopVar111][k_imopVar112] + ws[i_imopVar110 - 1][j_imopVar111][k_imopVar112]) - tx2 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][3] * up1 - u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][3] * um1);
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][4] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][4] + dx5tx1 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][4] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][4] + u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][4]) + xxcon3 * (qs[i_imopVar110 + 1][j_imopVar111][k_imopVar112] - 2.0 * qs[i_imopVar110][j_imopVar111][k_imopVar112] + qs[i_imopVar110 - 1][j_imopVar111][k_imopVar112]) + xxcon4 * (up1 * up1 - 2.0 * uijk * uijk + um1 * um1) + xxcon5 * (u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][4] * rho_i[i_imopVar110 + 1][j_imopVar111][k_imopVar112] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][4] * rho_i[i_imopVar110][j_imopVar111][k_imopVar112] + u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][4] * rho_i[i_imopVar110 - 1][j_imopVar111][k_imopVar112]) - tx2 * ((c1 * u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][4] - c2 * square[i_imopVar110 + 1][j_imopVar111][k_imopVar112]) * up1 - (c1 * u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][4] - c2 * square[i_imopVar110 - 1][j_imopVar111][k_imopVar112]) * um1);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    i_imopVar110 = 1;
#pragma omp for nowait
    for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
        for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
            for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (5.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][m_imopVar113] + u[i_imopVar110 + 2][j_imopVar111][k_imopVar112][m_imopVar113]);
            }
        }
    }
    i_imopVar110 = 2;
#pragma omp for nowait
    for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
        for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
            for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (-4.0 * u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][m_imopVar113] + u[i_imopVar110 + 2][j_imopVar111][k_imopVar112][m_imopVar113]);
            }
        }
    }
#pragma omp for nowait
    for (i_imopVar110 = 3; i_imopVar110 < grid_points[0] - 3; i_imopVar110++) {
        for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
            for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110 - 2][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][m_imopVar113] + u[i_imopVar110 + 2][j_imopVar111][k_imopVar112][m_imopVar113]);
                }
            }
        }
    }
    i_imopVar110 = grid_points[0] - 3;
#pragma omp for nowait
    for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
        for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
            for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110 - 2][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110 + 1][j_imopVar111][k_imopVar112][m_imopVar113]);
            }
        }
    }
    i_imopVar110 = grid_points[0] - 2;
#pragma omp for nowait
    for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
        for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
            for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110 - 2][j_imopVar111][k_imopVar112][m_imopVar113] - 4. * u[i_imopVar110 - 1][j_imopVar111][k_imopVar112][m_imopVar113] + 5.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113]);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
        for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
            for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                vijk = vs[i_imopVar110][j_imopVar111][k_imopVar112];
                vp1 = vs[i_imopVar110][j_imopVar111 + 1][k_imopVar112];
                vm1 = vs[i_imopVar110][j_imopVar111 - 1][k_imopVar112];
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][0] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][0] + dy1ty1 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][0] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][0] + u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][0]) - ty2 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][2] - u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][2]);
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][1] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][1] + dy2ty1 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][1] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][1] + u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][1]) + yycon2 * (us[i_imopVar110][j_imopVar111 + 1][k_imopVar112] - 2.0 * us[i_imopVar110][j_imopVar111][k_imopVar112] + us[i_imopVar110][j_imopVar111 - 1][k_imopVar112]) - ty2 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][1] * vp1 - u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][1] * vm1);
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][2] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][2] + dy3ty1 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][2] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][2] + u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][2]) + yycon2 * con43 * (vp1 - 2.0 * vijk + vm1) - ty2 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][2] * vp1 - u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][2] * vm1 + (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][4] - square[i_imopVar110][j_imopVar111 + 1][k_imopVar112] - u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][4] + square[i_imopVar110][j_imopVar111 - 1][k_imopVar112]) * c2);
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][3] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][3] + dy4ty1 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][3] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][3] + u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][3]) + yycon2 * (ws[i_imopVar110][j_imopVar111 + 1][k_imopVar112] - 2.0 * ws[i_imopVar110][j_imopVar111][k_imopVar112] + ws[i_imopVar110][j_imopVar111 - 1][k_imopVar112]) - ty2 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][3] * vp1 - u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][3] * vm1);
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][4] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][4] + dy5ty1 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][4] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][4] + u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][4]) + yycon3 * (qs[i_imopVar110][j_imopVar111 + 1][k_imopVar112] - 2.0 * qs[i_imopVar110][j_imopVar111][k_imopVar112] + qs[i_imopVar110][j_imopVar111 - 1][k_imopVar112]) + yycon4 * (vp1 * vp1 - 2.0 * vijk * vijk + vm1 * vm1) + yycon5 * (u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][4] * rho_i[i_imopVar110][j_imopVar111 + 1][k_imopVar112] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][4] * rho_i[i_imopVar110][j_imopVar111][k_imopVar112] + u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][4] * rho_i[i_imopVar110][j_imopVar111 - 1][k_imopVar112]) - ty2 * ((c1 * u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][4] - c2 * square[i_imopVar110][j_imopVar111 + 1][k_imopVar112]) * vp1 - (c1 * u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][4] - c2 * square[i_imopVar110][j_imopVar111 - 1][k_imopVar112]) * vm1);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    j_imopVar111 = 1;
#pragma omp for nowait
    for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
        for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
            for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (5.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][m_imopVar113] + u[i_imopVar110][j_imopVar111 + 2][k_imopVar112][m_imopVar113]);
            }
        }
    }
    j_imopVar111 = 2;
#pragma omp for nowait
    for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
        for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
            for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (-4.0 * u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][m_imopVar113] + u[i_imopVar110][j_imopVar111 + 2][k_imopVar112][m_imopVar113]);
            }
        }
    }
#pragma omp for nowait
    for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
        for (j_imopVar111 = 3; j_imopVar111 < grid_points[1] - 3; j_imopVar111++) {
            for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110][j_imopVar111 - 2][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][m_imopVar113] + u[i_imopVar110][j_imopVar111 + 2][k_imopVar112][m_imopVar113]);
                }
            }
        }
    }
    j_imopVar111 = grid_points[1] - 3;
#pragma omp for nowait
    for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
        for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
            for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110][j_imopVar111 - 2][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111 + 1][k_imopVar112][m_imopVar113]);
            }
        }
    }
    j_imopVar111 = grid_points[1] - 2;
#pragma omp for nowait
    for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
        for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
            for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110][j_imopVar111 - 2][k_imopVar112][m_imopVar113] - 4. * u[i_imopVar110][j_imopVar111 - 1][k_imopVar112][m_imopVar113] + 5. * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113]);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
        for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
            for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
                wijk = ws[i_imopVar110][j_imopVar111][k_imopVar112];
                wp1 = ws[i_imopVar110][j_imopVar111][k_imopVar112 + 1];
                wm1 = ws[i_imopVar110][j_imopVar111][k_imopVar112 - 1];
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][0] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][0] + dz1tz1 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][0] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][0] + u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][0]) - tz2 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][3] - u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][3]);
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][1] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][1] + dz2tz1 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][1] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][1] + u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][1]) + zzcon2 * (us[i_imopVar110][j_imopVar111][k_imopVar112 + 1] - 2.0 * us[i_imopVar110][j_imopVar111][k_imopVar112] + us[i_imopVar110][j_imopVar111][k_imopVar112 - 1]) - tz2 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][1] * wp1 - u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][1] * wm1);
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][2] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][2] + dz3tz1 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][2] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][2] + u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][2]) + zzcon2 * (vs[i_imopVar110][j_imopVar111][k_imopVar112 + 1] - 2.0 * vs[i_imopVar110][j_imopVar111][k_imopVar112] + vs[i_imopVar110][j_imopVar111][k_imopVar112 - 1]) - tz2 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][2] * wp1 - u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][2] * wm1);
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][3] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][3] + dz4tz1 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][3] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][3] + u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][3]) + zzcon2 * con43 * (wp1 - 2.0 * wijk + wm1) - tz2 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][3] * wp1 - u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][3] * wm1 + (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][4] - square[i_imopVar110][j_imopVar111][k_imopVar112 + 1] - u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][4] + square[i_imopVar110][j_imopVar111][k_imopVar112 - 1]) * c2);
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][4] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][4] + dz5tz1 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][4] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][4] + u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][4]) + zzcon3 * (qs[i_imopVar110][j_imopVar111][k_imopVar112 + 1] - 2.0 * qs[i_imopVar110][j_imopVar111][k_imopVar112] + qs[i_imopVar110][j_imopVar111][k_imopVar112 - 1]) + zzcon4 * (wp1 * wp1 - 2.0 * wijk * wijk + wm1 * wm1) + zzcon5 * (u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][4] * rho_i[i_imopVar110][j_imopVar111][k_imopVar112 + 1] - 2.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][4] * rho_i[i_imopVar110][j_imopVar111][k_imopVar112] + u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][4] * rho_i[i_imopVar110][j_imopVar111][k_imopVar112 - 1]) - tz2 * ((c1 * u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][4] - c2 * square[i_imopVar110][j_imopVar111][k_imopVar112 + 1]) * wp1 - (c1 * u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][4] - c2 * square[i_imopVar110][j_imopVar111][k_imopVar112 - 1]) * wm1);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    k_imopVar112 = 1;
#pragma omp for nowait
    for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
        for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
            for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (5.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][m_imopVar113] + u[i_imopVar110][j_imopVar111][k_imopVar112 + 2][m_imopVar113]);
            }
        }
    }
    k_imopVar112 = 2;
#pragma omp for nowait
    for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
        for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
            for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (-4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][m_imopVar113] + u[i_imopVar110][j_imopVar111][k_imopVar112 + 2][m_imopVar113]);
            }
        }
    }
#pragma omp for nowait
    for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
        for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
            for (k_imopVar112 = 3; k_imopVar112 < grid_points[2] - 3; k_imopVar112++) {
                for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110][j_imopVar111][k_imopVar112 - 2][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][m_imopVar113] + u[i_imopVar110][j_imopVar111][k_imopVar112 + 2][m_imopVar113]);
                }
            }
        }
    }
    k_imopVar112 = grid_points[2] - 3;
#pragma omp for nowait
    for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
        for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
            for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110][j_imopVar111][k_imopVar112 - 2][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][m_imopVar113] + 6.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 + 1][m_imopVar113]);
            }
        }
    }
    k_imopVar112 = grid_points[2] - 2;
#pragma omp for nowait
    for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
        for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
            for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] - dssp * (u[i_imopVar110][j_imopVar111][k_imopVar112 - 2][m_imopVar113] - 4.0 * u[i_imopVar110][j_imopVar111][k_imopVar112 - 1][m_imopVar113] + 5.0 * u[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113]);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (j_imopVar111 = 1; j_imopVar111 < grid_points[1] - 1; j_imopVar111++) {
        for (k_imopVar112 = 1; k_imopVar112 < grid_points[2] - 1; k_imopVar112++) {
            for (m_imopVar113 = 0; m_imopVar113 < 5; m_imopVar113++) {
                for (i_imopVar110 = 1; i_imopVar110 < grid_points[0] - 1; i_imopVar110++) {
                    rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] = rhs[i_imopVar110][j_imopVar111][k_imopVar112][m_imopVar113] * dt;
                }
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    int i_imopVar120;
    int j_imopVar121;
    int k_imopVar122;
#pragma omp for nowait
    for (j_imopVar121 = 1; j_imopVar121 < grid_points[1] - 1; j_imopVar121++) {
        for (k_imopVar122 = 1; k_imopVar122 < grid_points[2] - 1; k_imopVar122++) {
            for (i_imopVar120 = 0; i_imopVar120 < grid_points[0]; i_imopVar120++) {
                tmp1 = 1.0 / u[i_imopVar120][j_imopVar121][k_imopVar122][0];
                tmp2 = tmp1 * tmp1;
                tmp3 = tmp1 * tmp2;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][0][0] = 0.0;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][0][1] = 1.0;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][0][2] = 0.0;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][0][3] = 0.0;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][0][4] = 0.0;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][1][0] = -(u[i_imopVar120][j_imopVar121][k_imopVar122][1] * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][1]) + c2 * 0.50 * (u[i_imopVar120][j_imopVar121][k_imopVar122][1] * u[i_imopVar120][j_imopVar121][k_imopVar122][1] + u[i_imopVar120][j_imopVar121][k_imopVar122][2] * u[i_imopVar120][j_imopVar121][k_imopVar122][2] + u[i_imopVar120][j_imopVar121][k_imopVar122][3] * u[i_imopVar120][j_imopVar121][k_imopVar122][3]) * tmp2;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][1][1] = (2.0 - c2) * (u[i_imopVar120][j_imopVar121][k_imopVar122][1] / u[i_imopVar120][j_imopVar121][k_imopVar122][0]);
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][1][2] = -c2 * (u[i_imopVar120][j_imopVar121][k_imopVar122][2] * tmp1);
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][1][3] = -c2 * (u[i_imopVar120][j_imopVar121][k_imopVar122][3] * tmp1);
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][1][4] = c2;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][2][0] = -(u[i_imopVar120][j_imopVar121][k_imopVar122][1] * u[i_imopVar120][j_imopVar121][k_imopVar122][2]) * tmp2;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][2][1] = u[i_imopVar120][j_imopVar121][k_imopVar122][2] * tmp1;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][2][2] = u[i_imopVar120][j_imopVar121][k_imopVar122][1] * tmp1;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][2][3] = 0.0;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][2][4] = 0.0;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][3][0] = -(u[i_imopVar120][j_imopVar121][k_imopVar122][1] * u[i_imopVar120][j_imopVar121][k_imopVar122][3]) * tmp2;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][3][1] = u[i_imopVar120][j_imopVar121][k_imopVar122][3] * tmp1;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][3][2] = 0.0;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][3][3] = u[i_imopVar120][j_imopVar121][k_imopVar122][1] * tmp1;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][3][4] = 0.0;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][4][0] = (c2 * (u[i_imopVar120][j_imopVar121][k_imopVar122][1] * u[i_imopVar120][j_imopVar121][k_imopVar122][1] + u[i_imopVar120][j_imopVar121][k_imopVar122][2] * u[i_imopVar120][j_imopVar121][k_imopVar122][2] + u[i_imopVar120][j_imopVar121][k_imopVar122][3] * u[i_imopVar120][j_imopVar121][k_imopVar122][3]) * tmp2 - c1 * (u[i_imopVar120][j_imopVar121][k_imopVar122][4] * tmp1)) * (u[i_imopVar120][j_imopVar121][k_imopVar122][1] * tmp1);
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][4][1] = c1 * u[i_imopVar120][j_imopVar121][k_imopVar122][4] * tmp1 - 0.50 * c2 * (3.0 * u[i_imopVar120][j_imopVar121][k_imopVar122][1] * u[i_imopVar120][j_imopVar121][k_imopVar122][1] + u[i_imopVar120][j_imopVar121][k_imopVar122][2] * u[i_imopVar120][j_imopVar121][k_imopVar122][2] + u[i_imopVar120][j_imopVar121][k_imopVar122][3] * u[i_imopVar120][j_imopVar121][k_imopVar122][3]) * tmp2;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][4][2] = -c2 * (u[i_imopVar120][j_imopVar121][k_imopVar122][2] * u[i_imopVar120][j_imopVar121][k_imopVar122][1]) * tmp2;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][4][3] = -c2 * (u[i_imopVar120][j_imopVar121][k_imopVar122][3] * u[i_imopVar120][j_imopVar121][k_imopVar122][1]) * tmp2;
                fjac[i_imopVar120][j_imopVar121][k_imopVar122][4][4] = c1 * (u[i_imopVar120][j_imopVar121][k_imopVar122][1] * tmp1);
                njac[i_imopVar120][j_imopVar121][k_imopVar122][0][0] = 0.0;
                njac[i_imopVar120][j_imopVar121][k_imopVar122][0][1] = 0.0;
                njac[i_imopVar120][j_imopVar121][k_imopVar122][0][2] = 0.0;
                njac[i_imopVar120][j_imopVar121][k_imopVar122][0][3] = 0.0;
                njac[i_imopVar120][j_imopVar121][k_imopVar122][0][4] = 0.0;
                njac[i_imopVar120][j_imopVar121][k_imopVar122][1][0] = -con43 * c3c4 * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][1];
                njac[i_imopVar120][j_imopVar121][k_imopVar122][1][1] = con43 * c3c4 * tmp1;
                njac[i_imopVar120][j_imopVar121][k_imopVar122][1][2] = 0.0;
                njac[i_imopVar120][j_imopVar121][k_imopVar122][1][3] = 0.0;
                njac[i_imopVar120][j_imopVar121][k_imopVar122][1][4] = 0.0;
                njac[i_imopVar120][j_imopVar121][k_imopVar122][2][0] = -c3c4 * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][2];
                njac[i_imopVar120][j_imopVar121][k_imopVar122][2][1] = 0.0;
                njac[i_imopVar120][j_imopVar121][k_imopVar122][2][2] = c3c4 * tmp1;
                njac[i_imopVar120][j_imopVar121][k_imopVar122][2][3] = 0.0;
                njac[i_imopVar120][j_imopVar121][k_imopVar122][2][4] = 0.0;
                njac[i_imopVar120][j_imopVar121][k_imopVar122][3][0] = -c3c4 * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][3];
                njac[i_imopVar120][j_imopVar121][k_imopVar122][3][1] = 0.0;
                njac[i_imopVar120][j_imopVar121][k_imopVar122][3][2] = 0.0;
                njac[i_imopVar120][j_imopVar121][k_imopVar122][3][3] = c3c4 * tmp1;
                njac[i_imopVar120][j_imopVar121][k_imopVar122][3][4] = 0.0;
                njac[i_imopVar120][j_imopVar121][k_imopVar122][4][0] = -(con43 * c3c4 - c1345) * tmp3 * (((u[i_imopVar120][j_imopVar121][k_imopVar122][1]) * (u[i_imopVar120][j_imopVar121][k_imopVar122][1]))) - (c3c4 - c1345) * tmp3 * (((u[i_imopVar120][j_imopVar121][k_imopVar122][2]) * (u[i_imopVar120][j_imopVar121][k_imopVar122][2]))) - (c3c4 - c1345) * tmp3 * (((u[i_imopVar120][j_imopVar121][k_imopVar122][3]) * (u[i_imopVar120][j_imopVar121][k_imopVar122][3]))) - c1345 * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][4];
                njac[i_imopVar120][j_imopVar121][k_imopVar122][4][1] = (con43 * c3c4 - c1345) * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][1];
                njac[i_imopVar120][j_imopVar121][k_imopVar122][4][2] = (c3c4 - c1345) * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][2];
                njac[i_imopVar120][j_imopVar121][k_imopVar122][4][3] = (c3c4 - c1345) * tmp2 * u[i_imopVar120][j_imopVar121][k_imopVar122][3];
                njac[i_imopVar120][j_imopVar121][k_imopVar122][4][4] = c1345 * tmp1;
            }
            for (i_imopVar120 = 1; i_imopVar120 < grid_points[0] - 1; i_imopVar120++) {
                tmp1 = dt * tx1;
                tmp2 = dt * tx2;
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][0][0] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][0] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][0] - tmp1 * dx1;
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][0][1] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][1] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][1];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][0][2] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][2] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][2];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][0][3] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][3] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][3];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][0][4] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][4] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][0][4];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][1][0] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][0] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][0];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][1][1] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][1] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][1] - tmp1 * dx2;
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][1][2] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][2] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][2];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][1][3] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][3] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][3];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][1][4] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][4] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][1][4];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][2][0] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][0] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][0];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][2][1] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][1] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][1];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][2][2] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][2] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][2] - tmp1 * dx3;
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][2][3] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][3] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][3];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][2][4] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][4] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][2][4];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][3][0] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][0] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][0];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][3][1] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][1] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][1];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][3][2] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][2] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][2];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][3][3] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][3] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][3] - tmp1 * dx4;
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][3][4] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][4] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][3][4];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][4][0] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][0] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][0];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][4][1] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][1] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][1];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][4][2] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][2] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][2];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][4][3] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][3] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][3];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][0][4][4] = -tmp2 * fjac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][4] - tmp1 * njac[i_imopVar120 - 1][j_imopVar121][k_imopVar122][4][4] - tmp1 * dx5;
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][0][0] = 1.0 + tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][0][0] + tmp1 * 2.0 * dx1;
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][0][1] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][0][1];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][0][2] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][0][2];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][0][3] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][0][3];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][0][4] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][0][4];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][1][0] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][1][0];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][1][1] = 1.0 + tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][1][1] + tmp1 * 2.0 * dx2;
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][1][2] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][1][2];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][1][3] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][1][3];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][1][4] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][1][4];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][2][0] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][2][0];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][2][1] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][2][1];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][2][2] = 1.0 + tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][2][2] + tmp1 * 2.0 * dx3;
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][2][3] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][2][3];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][2][4] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][2][4];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][3][0] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][3][0];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][3][1] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][3][1];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][3][2] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][3][2];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][3][3] = 1.0 + tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][3][3] + tmp1 * 2.0 * dx4;
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][3][4] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][3][4];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][4][0] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][4][0];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][4][1] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][4][1];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][4][2] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][4][2];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][4][3] = tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][4][3];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][1][4][4] = 1.0 + tmp1 * 2.0 * njac[i_imopVar120][j_imopVar121][k_imopVar122][4][4] + tmp1 * 2.0 * dx5;
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][0][0] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][0] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][0] - tmp1 * dx1;
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][0][1] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][1] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][1];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][0][2] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][2] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][2];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][0][3] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][3] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][3];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][0][4] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][4] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][0][4];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][1][0] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][0] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][0];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][1][1] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][1] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][1] - tmp1 * dx2;
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][1][2] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][2] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][2];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][1][3] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][3] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][3];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][1][4] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][4] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][1][4];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][2][0] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][0] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][0];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][2][1] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][1] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][1];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][2][2] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][2] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][2] - tmp1 * dx3;
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][2][3] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][3] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][3];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][2][4] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][4] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][2][4];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][3][0] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][0] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][0];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][3][1] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][1] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][1];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][3][2] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][2] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][2];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][3][3] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][3] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][3] - tmp1 * dx4;
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][3][4] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][4] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][3][4];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][4][0] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][0] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][0];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][4][1] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][1] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][1];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][4][2] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][2] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][2];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][4][3] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][3] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][3];
                lhs[i_imopVar120][j_imopVar121][k_imopVar122][2][4][4] = tmp2 * fjac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][4] - tmp1 * njac[i_imopVar120 + 1][j_imopVar121][k_imopVar122][4][4] - tmp1 * dx5;
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    int i_imopVar114;
    int j_imopVar115;
    int k_imopVar116;
    int isize;
    isize = grid_points[0] - 1;
#pragma omp for nowait
    for (j_imopVar115 = 1; j_imopVar115 < grid_points[1] - 1; j_imopVar115++) {
        for (k_imopVar116 = 1; k_imopVar116 < grid_points[2] - 1; k_imopVar116++) {
            double ( *_imopVarPre338 );
            double ( *_imopVarPre339 )[5];
            double ( *_imopVarPre340 )[5];
            _imopVarPre338 = rhs[0][j_imopVar115][k_imopVar116];
            _imopVarPre339 = lhs[0][j_imopVar115][k_imopVar116][2];
            _imopVarPre340 = lhs[0][j_imopVar115][k_imopVar116][1];
            binvcrhs(_imopVarPre340, _imopVarPre339, _imopVarPre338);
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    for (i_imopVar114 = 1; i_imopVar114 < isize; i_imopVar114++) {
#pragma omp for nowait
        for (j_imopVar115 = 1; j_imopVar115 < grid_points[1] - 1; j_imopVar115++) {
            for (k_imopVar116 = 1; k_imopVar116 < grid_points[2] - 1; k_imopVar116++) {
                double ( *_imopVarPre344 );
                double ( *_imopVarPre345 );
                double ( *_imopVarPre346 )[5];
                _imopVarPre344 = rhs[i_imopVar114][j_imopVar115][k_imopVar116];
                _imopVarPre345 = rhs[i_imopVar114 - 1][j_imopVar115][k_imopVar116];
                _imopVarPre346 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][0];
                matvec_sub(_imopVarPre346, _imopVarPre345, _imopVarPre344);
                double ( *_imopVarPre350 )[5];
                double ( *_imopVarPre351 )[5];
                double ( *_imopVarPre352 )[5];
                _imopVarPre350 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][1];
                _imopVarPre351 = lhs[i_imopVar114 - 1][j_imopVar115][k_imopVar116][2];
                _imopVarPre352 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][0];
                matmul_sub(_imopVarPre352, _imopVarPre351, _imopVarPre350);
                double ( *_imopVarPre356 );
                double ( *_imopVarPre357 )[5];
                double ( *_imopVarPre358 )[5];
                _imopVarPre356 = rhs[i_imopVar114][j_imopVar115][k_imopVar116];
                _imopVarPre357 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][2];
                _imopVarPre358 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][1];
                binvcrhs(_imopVarPre358, _imopVarPre357, _imopVarPre356);
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    }
#pragma omp for nowait
    for (j_imopVar115 = 1; j_imopVar115 < grid_points[1] - 1; j_imopVar115++) {
        for (k_imopVar116 = 1; k_imopVar116 < grid_points[2] - 1; k_imopVar116++) {
            double ( *_imopVarPre362 );
            double ( *_imopVarPre363 );
            double ( *_imopVarPre364 )[5];
            _imopVarPre362 = rhs[isize][j_imopVar115][k_imopVar116];
            _imopVarPre363 = rhs[isize - 1][j_imopVar115][k_imopVar116];
            _imopVarPre364 = lhs[isize][j_imopVar115][k_imopVar116][0];
            matvec_sub(_imopVarPre364, _imopVarPre363, _imopVarPre362);
            double ( *_imopVarPre368 )[5];
            double ( *_imopVarPre369 )[5];
            double ( *_imopVarPre370 )[5];
            _imopVarPre368 = lhs[isize][j_imopVar115][k_imopVar116][1];
            _imopVarPre369 = lhs[isize - 1][j_imopVar115][k_imopVar116][2];
            _imopVarPre370 = lhs[isize][j_imopVar115][k_imopVar116][0];
            matmul_sub(_imopVarPre370, _imopVarPre369, _imopVarPre368);
            double ( *_imopVarPre373 );
            double ( *_imopVarPre374 )[5];
            _imopVarPre373 = rhs[i_imopVar114][j_imopVar115][k_imopVar116];
            _imopVarPre374 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][1];
            binvrhs(_imopVarPre374, _imopVarPre373);
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    int i_imopVar117;
    int j_imopVar118;
    int k_imopVar119;
    int m_imopVar123;
    int n_imopVar124;
    for (i_imopVar117 = grid_points[0] - 2; i_imopVar117 >= 0; i_imopVar117--) {
#pragma omp for nowait
        for (j_imopVar118 = 1; j_imopVar118 < grid_points[1] - 1; j_imopVar118++) {
            for (k_imopVar119 = 1; k_imopVar119 < grid_points[2] - 1; k_imopVar119++) {
                for (m_imopVar123 = 0; m_imopVar123 < 5; m_imopVar123++) {
                    for (n_imopVar124 = 0; n_imopVar124 < 5; n_imopVar124++) {
                        rhs[i_imopVar117][j_imopVar118][k_imopVar119][m_imopVar123] = rhs[i_imopVar117][j_imopVar118][k_imopVar119][m_imopVar123] - lhs[i_imopVar117][j_imopVar118][k_imopVar119][2][m_imopVar123][n_imopVar124] * rhs[i_imopVar117 + 1][j_imopVar118][k_imopVar119][n_imopVar124];
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    }
// #pragma omp dummyFlush BARRIER_START written([]) read([fjac.f, u.f, grid_points, fjac, con43, grid_points.f, u, y_solve, tmp3, c2, c1345, c1, c3c4, tmp2, tmp1, lhsy, njac.f, njac, i])
#pragma omp barrier
    int i_imopVar85;
    int j_imopVar86;
    int k_imopVar87;
#pragma omp for nowait
    for (i_imopVar85 = 1; i_imopVar85 < grid_points[0] - 1; i_imopVar85++) {
        for (j_imopVar86 = 0; j_imopVar86 < grid_points[1]; j_imopVar86++) {
            for (k_imopVar87 = 1; k_imopVar87 < grid_points[2] - 1; k_imopVar87++) {
                tmp1 = 1.0 / u[i_imopVar85][j_imopVar86][k_imopVar87][0];
                tmp2 = tmp1 * tmp1;
                tmp3 = tmp1 * tmp2;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][0][0] = 0.0;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][0][1] = 0.0;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][0][2] = 1.0;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][0][3] = 0.0;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][0][4] = 0.0;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][1][0] = -(u[i_imopVar85][j_imopVar86][k_imopVar87][1] * u[i_imopVar85][j_imopVar86][k_imopVar87][2]) * tmp2;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][1][1] = u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp1;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][1][2] = u[i_imopVar85][j_imopVar86][k_imopVar87][1] * tmp1;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][1][3] = 0.0;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][1][4] = 0.0;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][2][0] = -(u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp2) + 0.50 * c2 * ((u[i_imopVar85][j_imopVar86][k_imopVar87][1] * u[i_imopVar85][j_imopVar86][k_imopVar87][1] + u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][2] + u[i_imopVar85][j_imopVar86][k_imopVar87][3] * u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * tmp2);
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][2][1] = -c2 * u[i_imopVar85][j_imopVar86][k_imopVar87][1] * tmp1;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][2][2] = (2.0 - c2) * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp1;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][2][3] = -c2 * u[i_imopVar85][j_imopVar86][k_imopVar87][3] * tmp1;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][2][4] = c2;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][3][0] = -(u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * tmp2;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][3][1] = 0.0;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][3][2] = u[i_imopVar85][j_imopVar86][k_imopVar87][3] * tmp1;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][3][3] = u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp1;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][3][4] = 0.0;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][4][0] = (c2 * (u[i_imopVar85][j_imopVar86][k_imopVar87][1] * u[i_imopVar85][j_imopVar86][k_imopVar87][1] + u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][2] + u[i_imopVar85][j_imopVar86][k_imopVar87][3] * u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * tmp2 - c1 * u[i_imopVar85][j_imopVar86][k_imopVar87][4] * tmp1) * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp1;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][4][1] = -c2 * u[i_imopVar85][j_imopVar86][k_imopVar87][1] * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp2;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][4][2] = c1 * u[i_imopVar85][j_imopVar86][k_imopVar87][4] * tmp1 - 0.50 * c2 * ((u[i_imopVar85][j_imopVar86][k_imopVar87][1] * u[i_imopVar85][j_imopVar86][k_imopVar87][1] + 3.0 * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][2] + u[i_imopVar85][j_imopVar86][k_imopVar87][3] * u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * tmp2);
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][4][3] = -c2 * (u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * tmp2;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][4][4] = c1 * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp1;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][0][0] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][0][1] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][0][2] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][0][3] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][0][4] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][1][0] = -c3c4 * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][1];
                njac[i_imopVar85][j_imopVar86][k_imopVar87][1][1] = c3c4 * tmp1;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][1][2] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][1][3] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][1][4] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][2][0] = -con43 * c3c4 * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][2];
                njac[i_imopVar85][j_imopVar86][k_imopVar87][2][1] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][2][2] = con43 * c3c4 * tmp1;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][2][3] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][2][4] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][3][0] = -c3c4 * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][3];
                njac[i_imopVar85][j_imopVar86][k_imopVar87][3][1] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][3][2] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][3][3] = c3c4 * tmp1;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][3][4] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][4][0] = -(c3c4 - c1345) * tmp3 * (((u[i_imopVar85][j_imopVar86][k_imopVar87][1]) * (u[i_imopVar85][j_imopVar86][k_imopVar87][1]))) - (con43 * c3c4 - c1345) * tmp3 * (((u[i_imopVar85][j_imopVar86][k_imopVar87][2]) * (u[i_imopVar85][j_imopVar86][k_imopVar87][2]))) - (c3c4 - c1345) * tmp3 * (((u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * (u[i_imopVar85][j_imopVar86][k_imopVar87][3]))) - c1345 * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][4];
                njac[i_imopVar85][j_imopVar86][k_imopVar87][4][1] = (c3c4 - c1345) * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][1];
                njac[i_imopVar85][j_imopVar86][k_imopVar87][4][2] = (con43 * c3c4 - c1345) * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][2];
                njac[i_imopVar85][j_imopVar86][k_imopVar87][4][3] = (c3c4 - c1345) * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][3];
                njac[i_imopVar85][j_imopVar86][k_imopVar87][4][4] = c1345 * tmp1;
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (i_imopVar85 = 1; i_imopVar85 < grid_points[0] - 1; i_imopVar85++) {
        for (j_imopVar86 = 1; j_imopVar86 < grid_points[1] - 1; j_imopVar86++) {
            for (k_imopVar87 = 1; k_imopVar87 < grid_points[2] - 1; k_imopVar87++) {
                tmp1 = dt * ty1;
                tmp2 = dt * ty2;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][0][0] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][0] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][0] - tmp1 * dy1;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][0][1] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][1] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][0][2] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][2] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][0][3] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][3] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][0][4] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][4] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][1][0] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][0] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][1][1] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][1] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][1] - tmp1 * dy2;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][1][2] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][2] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][1][3] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][3] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][1][4] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][4] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][2][0] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][0] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][2][1] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][1] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][2][2] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][2] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][2] - tmp1 * dy3;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][2][3] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][3] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][2][4] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][4] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][3][0] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][0] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][3][1] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][1] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][3][2] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][2] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][3][3] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][3] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][3] - tmp1 * dy4;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][3][4] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][4] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][4][0] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][0] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][4][1] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][1] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][4][2] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][2] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][4][3] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][3] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][4][4] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][4] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][4] - tmp1 * dy5;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][0][0] = 1.0 + tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][0][0] + tmp1 * 2.0 * dy1;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][0][1] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][0][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][0][2] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][0][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][0][3] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][0][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][0][4] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][0][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][1][0] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][1][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][1][1] = 1.0 + tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][1][1] + tmp1 * 2.0 * dy2;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][1][2] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][1][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][1][3] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][1][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][1][4] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][1][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][2][0] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][2][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][2][1] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][2][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][2][2] = 1.0 + tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][2][2] + tmp1 * 2.0 * dy3;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][2][3] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][2][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][2][4] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][2][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][3][0] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][3][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][3][1] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][3][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][3][2] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][3][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][3][3] = 1.0 + tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][3][3] + tmp1 * 2.0 * dy4;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][3][4] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][3][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][4][0] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][4][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][4][1] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][4][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][4][2] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][4][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][4][3] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][4][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][4][4] = 1.0 + tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][4][4] + tmp1 * 2.0 * dy5;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][0][0] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][0] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][0] - tmp1 * dy1;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][0][1] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][1] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][0][2] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][2] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][0][3] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][3] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][0][4] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][4] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][1][0] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][0] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][1][1] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][1] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][1] - tmp1 * dy2;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][1][2] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][2] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][1][3] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][3] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][1][4] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][4] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][2][0] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][0] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][2][1] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][1] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][2][2] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][2] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][2] - tmp1 * dy3;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][2][3] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][3] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][2][4] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][4] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][3][0] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][0] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][3][1] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][1] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][3][2] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][2] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][3][3] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][3] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][3] - tmp1 * dy4;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][3][4] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][4] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][4][0] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][0] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][4][1] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][1] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][4][2] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][2] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][4][3] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][3] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][4][4] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][4] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][4] - tmp1 * dy5;
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    int i;
    int j;
    int k;
    int jsize;
    jsize = grid_points[1] - 1;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            double ( *_imopVarPre378 );
            double ( *_imopVarPre379 )[5];
            double ( *_imopVarPre380 )[5];
            _imopVarPre378 = rhs[i][0][k];
            _imopVarPre379 = lhs[i][0][k][2];
            _imopVarPre380 = lhs[i][0][k][1];
            binvcrhs(_imopVarPre380, _imopVarPre379, _imopVarPre378);
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    for (j = 1; j < jsize; j++) {
#pragma omp for nowait
        for (i = 1; i < grid_points[0] - 1; i++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                double ( *_imopVarPre384 );
                double ( *_imopVarPre385 );
                double ( *_imopVarPre386 )[5];
                _imopVarPre384 = rhs[i][j][k];
                _imopVarPre385 = rhs[i][j - 1][k];
                _imopVarPre386 = lhs[i][j][k][0];
                matvec_sub(_imopVarPre386, _imopVarPre385, _imopVarPre384);
                double ( *_imopVarPre390 )[5];
                double ( *_imopVarPre391 )[5];
                double ( *_imopVarPre392 )[5];
                _imopVarPre390 = lhs[i][j][k][1];
                _imopVarPre391 = lhs[i][j - 1][k][2];
                _imopVarPre392 = lhs[i][j][k][0];
                matmul_sub(_imopVarPre392, _imopVarPre391, _imopVarPre390);
                double ( *_imopVarPre396 );
                double ( *_imopVarPre397 )[5];
                double ( *_imopVarPre398 )[5];
                _imopVarPre396 = rhs[i][j][k];
                _imopVarPre397 = lhs[i][j][k][2];
                _imopVarPre398 = lhs[i][j][k][1];
                binvcrhs(_imopVarPre398, _imopVarPre397, _imopVarPre396);
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    }
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            double ( *_imopVarPre402 );
            double ( *_imopVarPre403 );
            double ( *_imopVarPre404 )[5];
            _imopVarPre402 = rhs[i][jsize][k];
            _imopVarPre403 = rhs[i][jsize - 1][k];
            _imopVarPre404 = lhs[i][jsize][k][0];
            matvec_sub(_imopVarPre404, _imopVarPre403, _imopVarPre402);
            double ( *_imopVarPre408 )[5];
            double ( *_imopVarPre409 )[5];
            double ( *_imopVarPre410 )[5];
            _imopVarPre408 = lhs[i][jsize][k][1];
            _imopVarPre409 = lhs[i][jsize - 1][k][2];
            _imopVarPre410 = lhs[i][jsize][k][0];
            matmul_sub(_imopVarPre410, _imopVarPre409, _imopVarPre408);
            double ( *_imopVarPre413 );
            double ( *_imopVarPre414 )[5];
            _imopVarPre413 = rhs[i][jsize][k];
            _imopVarPre414 = lhs[i][jsize][k][1];
            binvrhs(_imopVarPre414, _imopVarPre413);
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    int i_imopVar82;
    int j_imopVar83;
    int k_imopVar84;
    int m;
    int n;
    for (j_imopVar83 = grid_points[1] - 2; j_imopVar83 >= 0; j_imopVar83--) {
#pragma omp for nowait
        for (i_imopVar82 = 1; i_imopVar82 < grid_points[0] - 1; i_imopVar82++) {
            for (k_imopVar84 = 1; k_imopVar84 < grid_points[2] - 1; k_imopVar84++) {
                for (m = 0; m < 5; m++) {
                    for (n = 0; n < 5; n++) {
                        rhs[i_imopVar82][j_imopVar83][k_imopVar84][m] = rhs[i_imopVar82][j_imopVar83][k_imopVar84][m] - lhs[i_imopVar82][j_imopVar83][k_imopVar84][2][m][n] * rhs[i_imopVar82][j_imopVar83 + 1][k_imopVar84][n];
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    }
// #pragma omp dummyFlush BARRIER_START written([]) read([fjac.f, u.f, grid_points, fjac, grid_points.f, con43, u, c4, c3, tmp3, z_solve, c2, c1345, c1, i, tmp2, c3c4, tmp1, lhsz, njac.f, njac])
#pragma omp barrier
    int i_imopVar128;
    int j_imopVar129;
    int k_imopVar130;
#pragma omp for nowait
    for (i_imopVar128 = 1; i_imopVar128 < grid_points[0] - 1; i_imopVar128++) {
        for (j_imopVar129 = 1; j_imopVar129 < grid_points[1] - 1; j_imopVar129++) {
            for (k_imopVar130 = 0; k_imopVar130 < grid_points[2]; k_imopVar130++) {
                tmp1 = 1.0 / u[i_imopVar128][j_imopVar129][k_imopVar130][0];
                tmp2 = tmp1 * tmp1;
                tmp3 = tmp1 * tmp2;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][0][0] = 0.0;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][0][1] = 0.0;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][0][2] = 0.0;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][0][3] = 1.0;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][0][4] = 0.0;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][1][0] = -(u[i_imopVar128][j_imopVar129][k_imopVar130][1] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][1][1] = u[i_imopVar128][j_imopVar129][k_imopVar130][3] * tmp1;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][1][2] = 0.0;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][1][3] = u[i_imopVar128][j_imopVar129][k_imopVar130][1] * tmp1;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][1][4] = 0.0;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][2][0] = -(u[i_imopVar128][j_imopVar129][k_imopVar130][2] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][2][1] = 0.0;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][2][2] = u[i_imopVar128][j_imopVar129][k_imopVar130][3] * tmp1;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][2][3] = u[i_imopVar128][j_imopVar129][k_imopVar130][2] * tmp1;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][2][4] = 0.0;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][3][0] = -(u[i_imopVar128][j_imopVar129][k_imopVar130][3] * u[i_imopVar128][j_imopVar129][k_imopVar130][3] * tmp2) + 0.50 * c2 * ((u[i_imopVar128][j_imopVar129][k_imopVar130][1] * u[i_imopVar128][j_imopVar129][k_imopVar130][1] + u[i_imopVar128][j_imopVar129][k_imopVar130][2] * u[i_imopVar128][j_imopVar129][k_imopVar130][2] + u[i_imopVar128][j_imopVar129][k_imopVar130][3] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2);
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][3][1] = -c2 * u[i_imopVar128][j_imopVar129][k_imopVar130][1] * tmp1;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][3][2] = -c2 * u[i_imopVar128][j_imopVar129][k_imopVar130][2] * tmp1;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][3][3] = (2.0 - c2) * u[i_imopVar128][j_imopVar129][k_imopVar130][3] * tmp1;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][3][4] = c2;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][4][0] = (c2 * (u[i_imopVar128][j_imopVar129][k_imopVar130][1] * u[i_imopVar128][j_imopVar129][k_imopVar130][1] + u[i_imopVar128][j_imopVar129][k_imopVar130][2] * u[i_imopVar128][j_imopVar129][k_imopVar130][2] + u[i_imopVar128][j_imopVar129][k_imopVar130][3] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2 - c1 * (u[i_imopVar128][j_imopVar129][k_imopVar130][4] * tmp1)) * (u[i_imopVar128][j_imopVar129][k_imopVar130][3] * tmp1);
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][4][1] = -c2 * (u[i_imopVar128][j_imopVar129][k_imopVar130][1] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][4][2] = -c2 * (u[i_imopVar128][j_imopVar129][k_imopVar130][2] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2;
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][4][3] = c1 * (u[i_imopVar128][j_imopVar129][k_imopVar130][4] * tmp1) - 0.50 * c2 * ((u[i_imopVar128][j_imopVar129][k_imopVar130][1] * u[i_imopVar128][j_imopVar129][k_imopVar130][1] + u[i_imopVar128][j_imopVar129][k_imopVar130][2] * u[i_imopVar128][j_imopVar129][k_imopVar130][2] + 3.0 * u[i_imopVar128][j_imopVar129][k_imopVar130][3] * u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * tmp2);
                fjac[i_imopVar128][j_imopVar129][k_imopVar130][4][4] = c1 * u[i_imopVar128][j_imopVar129][k_imopVar130][3] * tmp1;
                njac[i_imopVar128][j_imopVar129][k_imopVar130][0][0] = 0.0;
                njac[i_imopVar128][j_imopVar129][k_imopVar130][0][1] = 0.0;
                njac[i_imopVar128][j_imopVar129][k_imopVar130][0][2] = 0.0;
                njac[i_imopVar128][j_imopVar129][k_imopVar130][0][3] = 0.0;
                njac[i_imopVar128][j_imopVar129][k_imopVar130][0][4] = 0.0;
                njac[i_imopVar128][j_imopVar129][k_imopVar130][1][0] = -c3c4 * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][1];
                njac[i_imopVar128][j_imopVar129][k_imopVar130][1][1] = c3c4 * tmp1;
                njac[i_imopVar128][j_imopVar129][k_imopVar130][1][2] = 0.0;
                njac[i_imopVar128][j_imopVar129][k_imopVar130][1][3] = 0.0;
                njac[i_imopVar128][j_imopVar129][k_imopVar130][1][4] = 0.0;
                njac[i_imopVar128][j_imopVar129][k_imopVar130][2][0] = -c3c4 * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][2];
                njac[i_imopVar128][j_imopVar129][k_imopVar130][2][1] = 0.0;
                njac[i_imopVar128][j_imopVar129][k_imopVar130][2][2] = c3c4 * tmp1;
                njac[i_imopVar128][j_imopVar129][k_imopVar130][2][3] = 0.0;
                njac[i_imopVar128][j_imopVar129][k_imopVar130][2][4] = 0.0;
                njac[i_imopVar128][j_imopVar129][k_imopVar130][3][0] = -con43 * c3c4 * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][3];
                njac[i_imopVar128][j_imopVar129][k_imopVar130][3][1] = 0.0;
                njac[i_imopVar128][j_imopVar129][k_imopVar130][3][2] = 0.0;
                njac[i_imopVar128][j_imopVar129][k_imopVar130][3][3] = con43 * c3 * c4 * tmp1;
                njac[i_imopVar128][j_imopVar129][k_imopVar130][3][4] = 0.0;
                njac[i_imopVar128][j_imopVar129][k_imopVar130][4][0] = -(c3c4 - c1345) * tmp3 * (((u[i_imopVar128][j_imopVar129][k_imopVar130][1]) * (u[i_imopVar128][j_imopVar129][k_imopVar130][1]))) - (c3c4 - c1345) * tmp3 * (((u[i_imopVar128][j_imopVar129][k_imopVar130][2]) * (u[i_imopVar128][j_imopVar129][k_imopVar130][2]))) - (con43 * c3c4 - c1345) * tmp3 * (((u[i_imopVar128][j_imopVar129][k_imopVar130][3]) * (u[i_imopVar128][j_imopVar129][k_imopVar130][3]))) - c1345 * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][4];
                njac[i_imopVar128][j_imopVar129][k_imopVar130][4][1] = (c3c4 - c1345) * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][1];
                njac[i_imopVar128][j_imopVar129][k_imopVar130][4][2] = (c3c4 - c1345) * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][2];
                njac[i_imopVar128][j_imopVar129][k_imopVar130][4][3] = (con43 * c3c4 - c1345) * tmp2 * u[i_imopVar128][j_imopVar129][k_imopVar130][3];
                njac[i_imopVar128][j_imopVar129][k_imopVar130][4][4] = c1345 * tmp1;
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (i_imopVar128 = 1; i_imopVar128 < grid_points[0] - 1; i_imopVar128++) {
        for (j_imopVar129 = 1; j_imopVar129 < grid_points[1] - 1; j_imopVar129++) {
            for (k_imopVar130 = 1; k_imopVar130 < grid_points[2] - 1; k_imopVar130++) {
                tmp1 = dt * tz1;
                tmp2 = dt * tz2;
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][0][0] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][0] - tmp1 * dz1;
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][0][1] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][1];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][0][2] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][2];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][0][3] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][3];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][0][4] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][0][4];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][1][0] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][0];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][1][1] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][1] - tmp1 * dz2;
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][1][2] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][2];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][1][3] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][3];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][1][4] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][1][4];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][2][0] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][0];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][2][1] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][1];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][2][2] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][2] - tmp1 * dz3;
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][2][3] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][3];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][2][4] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][2][4];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][3][0] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][0];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][3][1] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][1];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][3][2] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][2];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][3][3] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][3] - tmp1 * dz4;
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][3][4] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][3][4];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][4][0] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][0];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][4][1] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][1];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][4][2] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][2];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][4][3] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][3];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][0][4][4] = -tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 - 1][4][4] - tmp1 * dz5;
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][0][0] = 1.0 + tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][0][0] + tmp1 * 2.0 * dz1;
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][0][1] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][0][1];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][0][2] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][0][2];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][0][3] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][0][3];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][0][4] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][0][4];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][1][0] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][1][0];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][1][1] = 1.0 + tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][1][1] + tmp1 * 2.0 * dz2;
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][1][2] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][1][2];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][1][3] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][1][3];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][1][4] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][1][4];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][2][0] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][2][0];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][2][1] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][2][1];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][2][2] = 1.0 + tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][2][2] + tmp1 * 2.0 * dz3;
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][2][3] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][2][3];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][2][4] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][2][4];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][3][0] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][3][0];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][3][1] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][3][1];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][3][2] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][3][2];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][3][3] = 1.0 + tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][3][3] + tmp1 * 2.0 * dz4;
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][3][4] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][3][4];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][4][0] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][4][0];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][4][1] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][4][1];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][4][2] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][4][2];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][4][3] = tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][4][3];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][1][4][4] = 1.0 + tmp1 * 2.0 * njac[i_imopVar128][j_imopVar129][k_imopVar130][4][4] + tmp1 * 2.0 * dz5;
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][0][0] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][0] - tmp1 * dz1;
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][0][1] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][1];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][0][2] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][2];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][0][3] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][3];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][0][4] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][0][4];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][1][0] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][0];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][1][1] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][1] - tmp1 * dz2;
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][1][2] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][2];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][1][3] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][3];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][1][4] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][1][4];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][2][0] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][0];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][2][1] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][1];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][2][2] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][2] - tmp1 * dz3;
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][2][3] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][3];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][2][4] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][2][4];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][3][0] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][0];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][3][1] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][1];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][3][2] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][2];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][3][3] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][3] - tmp1 * dz4;
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][3][4] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][3][4];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][4][0] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][0] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][0];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][4][1] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][1] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][1];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][4][2] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][2] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][2];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][4][3] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][3] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][3];
                lhs[i_imopVar128][j_imopVar129][k_imopVar130][2][4][4] = tmp2 * fjac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][4] - tmp1 * njac[i_imopVar128][j_imopVar129][k_imopVar130 + 1][4][4] - tmp1 * dz5;
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    int i_imopVar125;
    int j_imopVar126;
    int k_imopVar127;
    int ksize;
    ksize = grid_points[2] - 1;
#pragma omp for nowait
    for (i_imopVar125 = 1; i_imopVar125 < grid_points[0] - 1; i_imopVar125++) {
        for (j_imopVar126 = 1; j_imopVar126 < grid_points[1] - 1; j_imopVar126++) {
            double ( *_imopVarPre418 );
            double ( *_imopVarPre419 )[5];
            double ( *_imopVarPre420 )[5];
            _imopVarPre418 = rhs[i_imopVar125][j_imopVar126][0];
            _imopVarPre419 = lhs[i_imopVar125][j_imopVar126][0][2];
            _imopVarPre420 = lhs[i_imopVar125][j_imopVar126][0][1];
            binvcrhs(_imopVarPre420, _imopVarPre419, _imopVarPre418);
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    for (k_imopVar127 = 1; k_imopVar127 < ksize; k_imopVar127++) {
#pragma omp for nowait
        for (i_imopVar125 = 1; i_imopVar125 < grid_points[0] - 1; i_imopVar125++) {
            for (j_imopVar126 = 1; j_imopVar126 < grid_points[1] - 1; j_imopVar126++) {
                double ( *_imopVarPre424 );
                double ( *_imopVarPre425 );
                double ( *_imopVarPre426 )[5];
                _imopVarPre424 = rhs[i_imopVar125][j_imopVar126][k_imopVar127];
                _imopVarPre425 = rhs[i_imopVar125][j_imopVar126][k_imopVar127 - 1];
                _imopVarPre426 = lhs[i_imopVar125][j_imopVar126][k_imopVar127][0];
                matvec_sub(_imopVarPre426, _imopVarPre425, _imopVarPre424);
                double ( *_imopVarPre430 )[5];
                double ( *_imopVarPre431 )[5];
                double ( *_imopVarPre432 )[5];
                _imopVarPre430 = lhs[i_imopVar125][j_imopVar126][k_imopVar127][1];
                _imopVarPre431 = lhs[i_imopVar125][j_imopVar126][k_imopVar127 - 1][2];
                _imopVarPre432 = lhs[i_imopVar125][j_imopVar126][k_imopVar127][0];
                matmul_sub(_imopVarPre432, _imopVarPre431, _imopVarPre430);
                double ( *_imopVarPre436 );
                double ( *_imopVarPre437 )[5];
                double ( *_imopVarPre438 )[5];
                _imopVarPre436 = rhs[i_imopVar125][j_imopVar126][k_imopVar127];
                _imopVarPre437 = lhs[i_imopVar125][j_imopVar126][k_imopVar127][2];
                _imopVarPre438 = lhs[i_imopVar125][j_imopVar126][k_imopVar127][1];
                binvcrhs(_imopVarPre438, _imopVarPre437, _imopVarPre436);
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    }
#pragma omp for nowait
    for (i_imopVar125 = 1; i_imopVar125 < grid_points[0] - 1; i_imopVar125++) {
        for (j_imopVar126 = 1; j_imopVar126 < grid_points[1] - 1; j_imopVar126++) {
            double ( *_imopVarPre442 );
            double ( *_imopVarPre443 );
            double ( *_imopVarPre444 )[5];
            _imopVarPre442 = rhs[i_imopVar125][j_imopVar126][ksize];
            _imopVarPre443 = rhs[i_imopVar125][j_imopVar126][ksize - 1];
            _imopVarPre444 = lhs[i_imopVar125][j_imopVar126][ksize][0];
            matvec_sub(_imopVarPre444, _imopVarPre443, _imopVarPre442);
            double ( *_imopVarPre448 )[5];
            double ( *_imopVarPre449 )[5];
            double ( *_imopVarPre450 )[5];
            _imopVarPre448 = lhs[i_imopVar125][j_imopVar126][ksize][1];
            _imopVarPre449 = lhs[i_imopVar125][j_imopVar126][ksize - 1][2];
            _imopVarPre450 = lhs[i_imopVar125][j_imopVar126][ksize][0];
            matmul_sub(_imopVarPre450, _imopVarPre449, _imopVarPre448);
            double ( *_imopVarPre453 );
            double ( *_imopVarPre454 )[5];
            _imopVarPre453 = rhs[i_imopVar125][j_imopVar126][ksize];
            _imopVarPre454 = lhs[i_imopVar125][j_imopVar126][ksize][1];
            binvrhs(_imopVarPre454, _imopVarPre453);
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    int i_imopVar103;
    int j_imopVar104;
    int k_imopVar105;
    int m_imopVar131;
    int n_imopVar132;
#pragma omp for nowait
    for (i_imopVar103 = 1; i_imopVar103 < grid_points[0] - 1; i_imopVar103++) {
        for (j_imopVar104 = 1; j_imopVar104 < grid_points[1] - 1; j_imopVar104++) {
            for (k_imopVar105 = grid_points[2] - 2; k_imopVar105 >= 0; k_imopVar105--) {
                for (m_imopVar131 = 0; m_imopVar131 < 5; m_imopVar131++) {
                    for (n_imopVar132 = 0; n_imopVar132 < 5; n_imopVar132++) {
                        rhs[i_imopVar103][j_imopVar104][k_imopVar105][m_imopVar131] = rhs[i_imopVar103][j_imopVar104][k_imopVar105][m_imopVar131] - lhs[i_imopVar103][j_imopVar104][k_imopVar105][2][m_imopVar131][n_imopVar132] * rhs[i_imopVar103][j_imopVar104][k_imopVar105 + 1][n_imopVar132];
                    }
                }
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
// #pragma omp dummyFlush BARRIER_START written([]) read([add, grid_points, u.f, grid_points.f, u, rhs, rhs.f, i])
#pragma omp barrier
    int i_imopVar106;
    int j_imopVar107;
    int k_imopVar108;
    int m_imopVar109;
#pragma omp for nowait
    for (i_imopVar106 = 1; i_imopVar106 < grid_points[0] - 1; i_imopVar106++) {
        for (j_imopVar107 = 1; j_imopVar107 < grid_points[1] - 1; j_imopVar107++) {
            for (k_imopVar108 = 1; k_imopVar108 < grid_points[2] - 1; k_imopVar108++) {
                for (m_imopVar109 = 0; m_imopVar109 < 5; m_imopVar109++) {
                    u[i_imopVar106][j_imopVar107][k_imopVar108][m_imopVar109] = u[i_imopVar106][j_imopVar107][k_imopVar108][m_imopVar109] + rhs[i_imopVar106][j_imopVar107][k_imopVar108][m_imopVar109];
                }
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
}
static void error_norm(double rms[5]) {
    int i;
    int j;
    int k;
    int m;
    int d;
    double xi;
    double eta;
    double zeta;
    double u_exact[5];
    double add;
    for (m = 0; m < 5; m++) {
        rms[m] = 0.0;
    }
    for (i = 0; i < grid_points[0]; i++) {
        xi = (double) i * dnxm1;
        for (j = 0; j < grid_points[1]; j++) {
            eta = (double) j * dnym1;
            for (k = 0; k < grid_points[2]; k++) {
                zeta = (double) k * dnzm1;
                exact_solution(xi, eta, zeta, u_exact);
                for (m = 0; m < 5; m++) {
                    add = u[i][j][k][m] - u_exact[m];
                    rms[m] = rms[m] + add * add;
                }
            }
        }
    }
    for (m = 0; m < 5; m++) {
        for (d = 0; d <= 2; d++) {
            rms[m] = rms[m] / (double) (grid_points[d] - 2);
        }
        double _imopVarPre184;
        double _imopVarPre185;
        _imopVarPre184 = rms[m];
        _imopVarPre185 = sqrt(_imopVarPre184);
        rms[m] = _imopVarPre185;
    }
}
static void rhs_norm(double rms[5]) {
    int i;
    int j;
    int k;
    int d;
    int m;
    double add;
    for (m = 0; m < 5; m++) {
        rms[m] = 0.0;
    }
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                for (m = 0; m < 5; m++) {
                    add = rhs[i][j][k][m];
                    rms[m] = rms[m] + add * add;
                }
            }
        }
    }
    for (m = 0; m < 5; m++) {
        for (d = 0; d <= 2; d++) {
            rms[m] = rms[m] / (double) (grid_points[d] - 2);
        }
        double _imopVarPre187;
        double _imopVarPre188;
        _imopVarPre187 = rms[m];
        _imopVarPre188 = sqrt(_imopVarPre187);
        rms[m] = _imopVarPre188;
    }
}
static void exact_rhs(void ) {
    double dtemp[5];
    double xi;
    double eta;
    double zeta;
    double dtpp;
    int m;
    int i;
    int j;
    int k;
    int ip1;
    int im1;
    int jp1;
    int jm1;
    int km1;
    int kp1;
#pragma omp for nowait
    for (i = 0; i < grid_points[0]; i++) {
        for (j = 0; j < grid_points[1]; j++) {
            for (k = 0; k < grid_points[2]; k++) {
                for (m = 0; m < 5; m++) {
                    forcing[i][j][k][m] = 0.0;
                }
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (j = 1; j < grid_points[1] - 1; j++) {
        eta = (double) j * dnym1;
        for (k = 1; k < grid_points[2] - 1; k++) {
            zeta = (double) k * dnzm1;
            for (i = 0; i < grid_points[0]; i++) {
                xi = (double) i * dnxm1;
                exact_solution(xi, eta, zeta, dtemp);
                for (m = 0; m < 5; m++) {
                    ue[i][m] = dtemp[m];
                }
                dtpp = 1.0 / dtemp[0];
                for (m = 1; m <= 4; m++) {
                    buf[i][m] = dtpp * dtemp[m];
                }
                cuf[i] = buf[i][1] * buf[i][1];
                buf[i][0] = cuf[i] + buf[i][2] * buf[i][2] + buf[i][3] * buf[i][3];
                q[i] = 0.5 * (buf[i][1] * ue[i][1] + buf[i][2] * ue[i][2] + buf[i][3] * ue[i][3]);
            }
            for (i = 1; i < grid_points[0] - 1; i++) {
                im1 = i - 1;
                ip1 = i + 1;
                forcing[i][j][k][0] = forcing[i][j][k][0] - tx2 * (ue[ip1][1] - ue[im1][1]) + dx1tx1 * (ue[ip1][0] - 2.0 * ue[i][0] + ue[im1][0]);
                forcing[i][j][k][1] = forcing[i][j][k][1] - tx2 * ((ue[ip1][1] * buf[ip1][1] + c2 * (ue[ip1][4] - q[ip1])) - (ue[im1][1] * buf[im1][1] + c2 * (ue[im1][4] - q[im1]))) + xxcon1 * (buf[ip1][1] - 2.0 * buf[i][1] + buf[im1][1]) + dx2tx1 * (ue[ip1][1] - 2.0 * ue[i][1] + ue[im1][1]);
                forcing[i][j][k][2] = forcing[i][j][k][2] - tx2 * (ue[ip1][2] * buf[ip1][1] - ue[im1][2] * buf[im1][1]) + xxcon2 * (buf[ip1][2] - 2.0 * buf[i][2] + buf[im1][2]) + dx3tx1 * (ue[ip1][2] - 2.0 * ue[i][2] + ue[im1][2]);
                forcing[i][j][k][3] = forcing[i][j][k][3] - tx2 * (ue[ip1][3] * buf[ip1][1] - ue[im1][3] * buf[im1][1]) + xxcon2 * (buf[ip1][3] - 2.0 * buf[i][3] + buf[im1][3]) + dx4tx1 * (ue[ip1][3] - 2.0 * ue[i][3] + ue[im1][3]);
                forcing[i][j][k][4] = forcing[i][j][k][4] - tx2 * (buf[ip1][1] * (c1 * ue[ip1][4] - c2 * q[ip1]) - buf[im1][1] * (c1 * ue[im1][4] - c2 * q[im1])) + 0.5 * xxcon3 * (buf[ip1][0] - 2.0 * buf[i][0] + buf[im1][0]) + xxcon4 * (cuf[ip1] - 2.0 * cuf[i] + cuf[im1]) + xxcon5 * (buf[ip1][4] - 2.0 * buf[i][4] + buf[im1][4]) + dx5tx1 * (ue[ip1][4] - 2.0 * ue[i][4] + ue[im1][4]);
            }
            for (m = 0; m < 5; m++) {
                i = 1;
                forcing[i][j][k][m] = forcing[i][j][k][m] - dssp * (5.0 * ue[i][m] - 4.0 * ue[i + 1][m] + ue[i + 2][m]);
                i = 2;
                forcing[i][j][k][m] = forcing[i][j][k][m] - dssp * (-4.0 * ue[i - 1][m] + 6.0 * ue[i][m] - 4.0 * ue[i + 1][m] + ue[i + 2][m]);
            }
            for (m = 0; m < 5; m++) {
                for (i = 1 * 3; i <= grid_points[0] - 3 * 1 - 1; i++) {
                    forcing[i][j][k][m] = forcing[i][j][k][m] - dssp * (ue[i - 2][m] - 4.0 * ue[i - 1][m] + 6.0 * ue[i][m] - 4.0 * ue[i + 1][m] + ue[i + 2][m]);
                }
            }
            for (m = 0; m < 5; m++) {
                i = grid_points[0] - 3;
                forcing[i][j][k][m] = forcing[i][j][k][m] - dssp * (ue[i - 2][m] - 4.0 * ue[i - 1][m] + 6.0 * ue[i][m] - 4.0 * ue[i + 1][m]);
                i = grid_points[0] - 2;
                forcing[i][j][k][m] = forcing[i][j][k][m] - dssp * (ue[i - 2][m] - 4.0 * ue[i - 1][m] + 5.0 * ue[i][m]);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        xi = (double) i * dnxm1;
        for (k = 1; k < grid_points[2] - 1; k++) {
            zeta = (double) k * dnzm1;
            for (j = 0; j < grid_points[1]; j++) {
                eta = (double) j * dnym1;
                exact_solution(xi, eta, zeta, dtemp);
                for (m = 0; m < 5; m++) {
                    ue[j][m] = dtemp[m];
                }
                dtpp = 1.0 / dtemp[0];
                for (m = 1; m <= 4; m++) {
                    buf[j][m] = dtpp * dtemp[m];
                }
                cuf[j] = buf[j][2] * buf[j][2];
                buf[j][0] = cuf[j] + buf[j][1] * buf[j][1] + buf[j][3] * buf[j][3];
                q[j] = 0.5 * (buf[j][1] * ue[j][1] + buf[j][2] * ue[j][2] + buf[j][3] * ue[j][3]);
            }
            for (j = 1; j < grid_points[1] - 1; j++) {
                jm1 = j - 1;
                jp1 = j + 1;
                forcing[i][j][k][0] = forcing[i][j][k][0] - ty2 * (ue[jp1][2] - ue[jm1][2]) + dy1ty1 * (ue[jp1][0] - 2.0 * ue[j][0] + ue[jm1][0]);
                forcing[i][j][k][1] = forcing[i][j][k][1] - ty2 * (ue[jp1][1] * buf[jp1][2] - ue[jm1][1] * buf[jm1][2]) + yycon2 * (buf[jp1][1] - 2.0 * buf[j][1] + buf[jm1][1]) + dy2ty1 * (ue[jp1][1] - 2.0 * ue[j][1] + ue[jm1][1]);
                forcing[i][j][k][2] = forcing[i][j][k][2] - ty2 * ((ue[jp1][2] * buf[jp1][2] + c2 * (ue[jp1][4] - q[jp1])) - (ue[jm1][2] * buf[jm1][2] + c2 * (ue[jm1][4] - q[jm1]))) + yycon1 * (buf[jp1][2] - 2.0 * buf[j][2] + buf[jm1][2]) + dy3ty1 * (ue[jp1][2] - 2.0 * ue[j][2] + ue[jm1][2]);
                forcing[i][j][k][3] = forcing[i][j][k][3] - ty2 * (ue[jp1][3] * buf[jp1][2] - ue[jm1][3] * buf[jm1][2]) + yycon2 * (buf[jp1][3] - 2.0 * buf[j][3] + buf[jm1][3]) + dy4ty1 * (ue[jp1][3] - 2.0 * ue[j][3] + ue[jm1][3]);
                forcing[i][j][k][4] = forcing[i][j][k][4] - ty2 * (buf[jp1][2] * (c1 * ue[jp1][4] - c2 * q[jp1]) - buf[jm1][2] * (c1 * ue[jm1][4] - c2 * q[jm1])) + 0.5 * yycon3 * (buf[jp1][0] - 2.0 * buf[j][0] + buf[jm1][0]) + yycon4 * (cuf[jp1] - 2.0 * cuf[j] + cuf[jm1]) + yycon5 * (buf[jp1][4] - 2.0 * buf[j][4] + buf[jm1][4]) + dy5ty1 * (ue[jp1][4] - 2.0 * ue[j][4] + ue[jm1][4]);
            }
            for (m = 0; m < 5; m++) {
                j = 1;
                forcing[i][j][k][m] = forcing[i][j][k][m] - dssp * (5.0 * ue[j][m] - 4.0 * ue[j + 1][m] + ue[j + 2][m]);
                j = 2;
                forcing[i][j][k][m] = forcing[i][j][k][m] - dssp * (-4.0 * ue[j - 1][m] + 6.0 * ue[j][m] - 4.0 * ue[j + 1][m] + ue[j + 2][m]);
            }
            for (m = 0; m < 5; m++) {
                for (j = 1 * 3; j <= grid_points[1] - 3 * 1 - 1; j++) {
                    forcing[i][j][k][m] = forcing[i][j][k][m] - dssp * (ue[j - 2][m] - 4.0 * ue[j - 1][m] + 6.0 * ue[j][m] - 4.0 * ue[j + 1][m] + ue[j + 2][m]);
                }
            }
            for (m = 0; m < 5; m++) {
                j = grid_points[1] - 3;
                forcing[i][j][k][m] = forcing[i][j][k][m] - dssp * (ue[j - 2][m] - 4.0 * ue[j - 1][m] + 6.0 * ue[j][m] - 4.0 * ue[j + 1][m]);
                j = grid_points[1] - 2;
                forcing[i][j][k][m] = forcing[i][j][k][m] - dssp * (ue[j - 2][m] - 4.0 * ue[j - 1][m] + 5.0 * ue[j][m]);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        xi = (double) i * dnxm1;
        for (j = 1; j < grid_points[1] - 1; j++) {
            eta = (double) j * dnym1;
            for (k = 0; k < grid_points[2]; k++) {
                zeta = (double) k * dnzm1;
                exact_solution(xi, eta, zeta, dtemp);
                for (m = 0; m < 5; m++) {
                    ue[k][m] = dtemp[m];
                }
                dtpp = 1.0 / dtemp[0];
                for (m = 1; m <= 4; m++) {
                    buf[k][m] = dtpp * dtemp[m];
                }
                cuf[k] = buf[k][3] * buf[k][3];
                buf[k][0] = cuf[k] + buf[k][1] * buf[k][1] + buf[k][2] * buf[k][2];
                q[k] = 0.5 * (buf[k][1] * ue[k][1] + buf[k][2] * ue[k][2] + buf[k][3] * ue[k][3]);
            }
            for (k = 1; k < grid_points[2] - 1; k++) {
                km1 = k - 1;
                kp1 = k + 1;
                forcing[i][j][k][0] = forcing[i][j][k][0] - tz2 * (ue[kp1][3] - ue[km1][3]) + dz1tz1 * (ue[kp1][0] - 2.0 * ue[k][0] + ue[km1][0]);
                forcing[i][j][k][1] = forcing[i][j][k][1] - tz2 * (ue[kp1][1] * buf[kp1][3] - ue[km1][1] * buf[km1][3]) + zzcon2 * (buf[kp1][1] - 2.0 * buf[k][1] + buf[km1][1]) + dz2tz1 * (ue[kp1][1] - 2.0 * ue[k][1] + ue[km1][1]);
                forcing[i][j][k][2] = forcing[i][j][k][2] - tz2 * (ue[kp1][2] * buf[kp1][3] - ue[km1][2] * buf[km1][3]) + zzcon2 * (buf[kp1][2] - 2.0 * buf[k][2] + buf[km1][2]) + dz3tz1 * (ue[kp1][2] - 2.0 * ue[k][2] + ue[km1][2]);
                forcing[i][j][k][3] = forcing[i][j][k][3] - tz2 * ((ue[kp1][3] * buf[kp1][3] + c2 * (ue[kp1][4] - q[kp1])) - (ue[km1][3] * buf[km1][3] + c2 * (ue[km1][4] - q[km1]))) + zzcon1 * (buf[kp1][3] - 2.0 * buf[k][3] + buf[km1][3]) + dz4tz1 * (ue[kp1][3] - 2.0 * ue[k][3] + ue[km1][3]);
                forcing[i][j][k][4] = forcing[i][j][k][4] - tz2 * (buf[kp1][3] * (c1 * ue[kp1][4] - c2 * q[kp1]) - buf[km1][3] * (c1 * ue[km1][4] - c2 * q[km1])) + 0.5 * zzcon3 * (buf[kp1][0] - 2.0 * buf[k][0] + buf[km1][0]) + zzcon4 * (cuf[kp1] - 2.0 * cuf[k] + cuf[km1]) + zzcon5 * (buf[kp1][4] - 2.0 * buf[k][4] + buf[km1][4]) + dz5tz1 * (ue[kp1][4] - 2.0 * ue[k][4] + ue[km1][4]);
            }
            for (m = 0; m < 5; m++) {
                k = 1;
                forcing[i][j][k][m] = forcing[i][j][k][m] - dssp * (5.0 * ue[k][m] - 4.0 * ue[k + 1][m] + ue[k + 2][m]);
                k = 2;
                forcing[i][j][k][m] = forcing[i][j][k][m] - dssp * (-4.0 * ue[k - 1][m] + 6.0 * ue[k][m] - 4.0 * ue[k + 1][m] + ue[k + 2][m]);
            }
            for (m = 0; m < 5; m++) {
                for (k = 1 * 3; k <= grid_points[2] - 3 * 1 - 1; k++) {
                    forcing[i][j][k][m] = forcing[i][j][k][m] - dssp * (ue[k - 2][m] - 4.0 * ue[k - 1][m] + 6.0 * ue[k][m] - 4.0 * ue[k + 1][m] + ue[k + 2][m]);
                }
            }
            for (m = 0; m < 5; m++) {
                k = grid_points[2] - 3;
                forcing[i][j][k][m] = forcing[i][j][k][m] - dssp * (ue[k - 2][m] - 4.0 * ue[k - 1][m] + 6.0 * ue[k][m] - 4.0 * ue[k + 1][m]);
                k = grid_points[2] - 2;
                forcing[i][j][k][m] = forcing[i][j][k][m] - dssp * (ue[k - 2][m] - 4.0 * ue[k - 1][m] + 5.0 * ue[k][m]);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                for (m = 0; m < 5; m++) {
                    forcing[i][j][k][m] = -1.0 * forcing[i][j][k][m];
                }
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
}
static void exact_solution(double xi, double eta , double zeta , double dtemp[5]) {
    int m;
    for (m = 0; m < 5; m++) {
        dtemp[m] = ce[m][0] + xi * (ce[m][1] + xi * (ce[m][4] + xi * (ce[m][7] + xi * ce[m][10]))) + eta * (ce[m][2] + eta * (ce[m][5] + eta * (ce[m][8] + eta * ce[m][11]))) + zeta * (ce[m][3] + zeta * (ce[m][6] + zeta * (ce[m][9] + zeta * ce[m][12])));
    }
}
static void initialize(void ) {
    int i;
    int j;
    int k;
    int m;
    int ix;
    int iy;
    int iz;
    double xi;
    double eta;
    double zeta;
    double Pface[2][3][5];
    double Pxi;
    double Peta;
    double Pzeta;
    double temp[5];
#pragma omp for nowait
    for (i = 0; i < 12; i++) {
        for (j = 0; j < 12; j++) {
            for (k = 0; k < 12; k++) {
                for (m = 0; m < 5; m++) {
                    u[i][j][k][m] = 1.0;
                }
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written([u.f]) read([dnxm1, dnym1, u.f, grid_points, grid_points.f, u, ce.f, dnzm1, ce, i, exact_solution])
#pragma omp barrier
#pragma omp for nowait
    for (i = 0; i < grid_points[0]; i++) {
        xi = (double) i * dnxm1;
        for (j = 0; j < grid_points[1]; j++) {
            eta = (double) j * dnym1;
            for (k = 0; k < grid_points[2]; k++) {
                zeta = (double) k * dnzm1;
                for (ix = 0; ix < 2; ix++) {
                    double *_imopVarPre191;
                    double _imopVarPre192;
                    _imopVarPre191 = &(Pface[ix][0][0]);
                    _imopVarPre192 = (double) ix;
                    exact_solution(_imopVarPre192, eta, zeta, _imopVarPre191);
                }
                for (iy = 0; iy < 2; iy++) {
                    double *_imopVarPre195;
                    double _imopVarPre196;
                    _imopVarPre195 = &Pface[iy][1][0];
                    _imopVarPre196 = (double) iy;
                    exact_solution(xi, _imopVarPre196, zeta, _imopVarPre195);
                }
                for (iz = 0; iz < 2; iz++) {
                    double *_imopVarPre199;
                    double _imopVarPre200;
                    _imopVarPre199 = &Pface[iz][2][0];
                    _imopVarPre200 = (double) iz;
                    exact_solution(xi, eta, _imopVarPre200, _imopVarPre199);
                }
                for (m = 0; m < 5; m++) {
                    Pxi = xi * Pface[1][0][m] + (1.0 - xi) * Pface[0][0][m];
                    Peta = eta * Pface[1][1][m] + (1.0 - eta) * Pface[0][1][m];
                    Pzeta = zeta * Pface[1][2][m] + (1.0 - zeta) * Pface[0][2][m];
                    u[i][j][k][m] = Pxi + Peta + Pzeta - Pxi * Peta - Pxi * Pzeta - Peta * Pzeta + Pxi * Peta * Pzeta;
                }
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written([u_exact.f, Pface.f, u.f, temp.f]) read([grid_points, dnym1, u.f, grid_points.f, u, ce.f, ce, dnzm1, j, exact_solution])
#pragma omp barrier
    i = 0;
    xi = 0.0;
#pragma omp for nowait
    for (j = 0; j < grid_points[1]; j++) {
        eta = (double) j * dnym1;
        for (k = 0; k < grid_points[2]; k++) {
            zeta = (double) k * dnzm1;
            exact_solution(xi, eta, zeta, temp);
            for (m = 0; m < 5; m++) {
                u[i][j][k][m] = temp[m];
            }
        }
    }
    i = grid_points[0] - 1;
    xi = 1.0;
#pragma omp for nowait
    for (j = 0; j < grid_points[1]; j++) {
        eta = (double) j * dnym1;
        for (k = 0; k < grid_points[2]; k++) {
            zeta = (double) k * dnzm1;
            exact_solution(xi, eta, zeta, temp);
            for (m = 0; m < 5; m++) {
                u[i][j][k][m] = temp[m];
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written([u_exact.f, Pface.f, u.f, temp.f]) read([dnxm1, grid_points, u.f, grid_points.f, u, ce.f, dnzm1, ce, i, exact_solution])
#pragma omp barrier
    j = 0;
    eta = 0.0;
#pragma omp for nowait
    for (i = 0; i < grid_points[0]; i++) {
        xi = (double) i * dnxm1;
        for (k = 0; k < grid_points[2]; k++) {
            zeta = (double) k * dnzm1;
            exact_solution(xi, eta, zeta, temp);
            for (m = 0; m < 5; m++) {
                u[i][j][k][m] = temp[m];
            }
        }
    }
    j = grid_points[1] - 1;
    eta = 1.0;
#pragma omp for nowait
    for (i = 0; i < grid_points[0]; i++) {
        xi = (double) i * dnxm1;
        for (k = 0; k < grid_points[2]; k++) {
            zeta = (double) k * dnzm1;
            exact_solution(xi, eta, zeta, temp);
            for (m = 0; m < 5; m++) {
                u[i][j][k][m] = temp[m];
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written([u_exact.f, Pface.f, u.f, temp.f]) read([dnxm1, u.f, grid_points, dnym1, grid_points.f, u, ce.f, ce, exact_solution, i])
#pragma omp barrier
    k = 0;
    zeta = 0.0;
#pragma omp for nowait
    for (i = 0; i < grid_points[0]; i++) {
        xi = (double) i * dnxm1;
        for (j = 0; j < grid_points[1]; j++) {
            eta = (double) j * dnym1;
            exact_solution(xi, eta, zeta, temp);
            for (m = 0; m < 5; m++) {
                u[i][j][k][m] = temp[m];
            }
        }
    }
    k = grid_points[2] - 1;
    zeta = 1.0;
#pragma omp for nowait
    for (i = 0; i < grid_points[0]; i++) {
        xi = (double) i * dnxm1;
        for (j = 0; j < grid_points[1]; j++) {
            eta = (double) j * dnym1;
            exact_solution(xi, eta, zeta, temp);
            for (m = 0; m < 5; m++) {
                u[i][j][k][m] = temp[m];
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written([u_exact.f, Pface.f, u.f, temp.f]) read([])
#pragma omp barrier
}
static void lhsinit(void ) {
    int i;
    int j;
    int k;
    int m;
    int n;
#pragma omp for nowait
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
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
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
static void lhsx(void ) {
    int i;
    int j;
    int k;
#pragma omp for nowait
    for (j = 1; j < grid_points[1] - 1; j++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (i = 0; i < grid_points[0]; i++) {
                tmp1 = 1.0 / u[i][j][k][0];
                tmp2 = tmp1 * tmp1;
                tmp3 = tmp1 * tmp2;
                fjac[i][j][k][0][0] = 0.0;
                fjac[i][j][k][0][1] = 1.0;
                fjac[i][j][k][0][2] = 0.0;
                fjac[i][j][k][0][3] = 0.0;
                fjac[i][j][k][0][4] = 0.0;
                fjac[i][j][k][1][0] = -(u[i][j][k][1] * tmp2 * u[i][j][k][1]) + c2 * 0.50 * (u[i][j][k][1] * u[i][j][k][1] + u[i][j][k][2] * u[i][j][k][2] + u[i][j][k][3] * u[i][j][k][3]) * tmp2;
                fjac[i][j][k][1][1] = (2.0 - c2) * (u[i][j][k][1] / u[i][j][k][0]);
                fjac[i][j][k][1][2] = -c2 * (u[i][j][k][2] * tmp1);
                fjac[i][j][k][1][3] = -c2 * (u[i][j][k][3] * tmp1);
                fjac[i][j][k][1][4] = c2;
                fjac[i][j][k][2][0] = -(u[i][j][k][1] * u[i][j][k][2]) * tmp2;
                fjac[i][j][k][2][1] = u[i][j][k][2] * tmp1;
                fjac[i][j][k][2][2] = u[i][j][k][1] * tmp1;
                fjac[i][j][k][2][3] = 0.0;
                fjac[i][j][k][2][4] = 0.0;
                fjac[i][j][k][3][0] = -(u[i][j][k][1] * u[i][j][k][3]) * tmp2;
                fjac[i][j][k][3][1] = u[i][j][k][3] * tmp1;
                fjac[i][j][k][3][2] = 0.0;
                fjac[i][j][k][3][3] = u[i][j][k][1] * tmp1;
                fjac[i][j][k][3][4] = 0.0;
                fjac[i][j][k][4][0] = (c2 * (u[i][j][k][1] * u[i][j][k][1] + u[i][j][k][2] * u[i][j][k][2] + u[i][j][k][3] * u[i][j][k][3]) * tmp2 - c1 * (u[i][j][k][4] * tmp1)) * (u[i][j][k][1] * tmp1);
                fjac[i][j][k][4][1] = c1 * u[i][j][k][4] * tmp1 - 0.50 * c2 * (3.0 * u[i][j][k][1] * u[i][j][k][1] + u[i][j][k][2] * u[i][j][k][2] + u[i][j][k][3] * u[i][j][k][3]) * tmp2;
                fjac[i][j][k][4][2] = -c2 * (u[i][j][k][2] * u[i][j][k][1]) * tmp2;
                fjac[i][j][k][4][3] = -c2 * (u[i][j][k][3] * u[i][j][k][1]) * tmp2;
                fjac[i][j][k][4][4] = c1 * (u[i][j][k][1] * tmp1);
                njac[i][j][k][0][0] = 0.0;
                njac[i][j][k][0][1] = 0.0;
                njac[i][j][k][0][2] = 0.0;
                njac[i][j][k][0][3] = 0.0;
                njac[i][j][k][0][4] = 0.0;
                njac[i][j][k][1][0] = -con43 * c3c4 * tmp2 * u[i][j][k][1];
                njac[i][j][k][1][1] = con43 * c3c4 * tmp1;
                njac[i][j][k][1][2] = 0.0;
                njac[i][j][k][1][3] = 0.0;
                njac[i][j][k][1][4] = 0.0;
                njac[i][j][k][2][0] = -c3c4 * tmp2 * u[i][j][k][2];
                njac[i][j][k][2][1] = 0.0;
                njac[i][j][k][2][2] = c3c4 * tmp1;
                njac[i][j][k][2][3] = 0.0;
                njac[i][j][k][2][4] = 0.0;
                njac[i][j][k][3][0] = -c3c4 * tmp2 * u[i][j][k][3];
                njac[i][j][k][3][1] = 0.0;
                njac[i][j][k][3][2] = 0.0;
                njac[i][j][k][3][3] = c3c4 * tmp1;
                njac[i][j][k][3][4] = 0.0;
                njac[i][j][k][4][0] = -(con43 * c3c4 - c1345) * tmp3 * (((u[i][j][k][1]) * (u[i][j][k][1]))) - (c3c4 - c1345) * tmp3 * (((u[i][j][k][2]) * (u[i][j][k][2]))) - (c3c4 - c1345) * tmp3 * (((u[i][j][k][3]) * (u[i][j][k][3]))) - c1345 * tmp2 * u[i][j][k][4];
                njac[i][j][k][4][1] = (con43 * c3c4 - c1345) * tmp2 * u[i][j][k][1];
                njac[i][j][k][4][2] = (c3c4 - c1345) * tmp2 * u[i][j][k][2];
                njac[i][j][k][4][3] = (c3c4 - c1345) * tmp2 * u[i][j][k][3];
                njac[i][j][k][4][4] = c1345 * tmp1;
            }
            for (i = 1; i < grid_points[0] - 1; i++) {
                tmp1 = dt * tx1;
                tmp2 = dt * tx2;
                lhs[i][j][k][0][0][0] = -tmp2 * fjac[i - 1][j][k][0][0] - tmp1 * njac[i - 1][j][k][0][0] - tmp1 * dx1;
                lhs[i][j][k][0][0][1] = -tmp2 * fjac[i - 1][j][k][0][1] - tmp1 * njac[i - 1][j][k][0][1];
                lhs[i][j][k][0][0][2] = -tmp2 * fjac[i - 1][j][k][0][2] - tmp1 * njac[i - 1][j][k][0][2];
                lhs[i][j][k][0][0][3] = -tmp2 * fjac[i - 1][j][k][0][3] - tmp1 * njac[i - 1][j][k][0][3];
                lhs[i][j][k][0][0][4] = -tmp2 * fjac[i - 1][j][k][0][4] - tmp1 * njac[i - 1][j][k][0][4];
                lhs[i][j][k][0][1][0] = -tmp2 * fjac[i - 1][j][k][1][0] - tmp1 * njac[i - 1][j][k][1][0];
                lhs[i][j][k][0][1][1] = -tmp2 * fjac[i - 1][j][k][1][1] - tmp1 * njac[i - 1][j][k][1][1] - tmp1 * dx2;
                lhs[i][j][k][0][1][2] = -tmp2 * fjac[i - 1][j][k][1][2] - tmp1 * njac[i - 1][j][k][1][2];
                lhs[i][j][k][0][1][3] = -tmp2 * fjac[i - 1][j][k][1][3] - tmp1 * njac[i - 1][j][k][1][3];
                lhs[i][j][k][0][1][4] = -tmp2 * fjac[i - 1][j][k][1][4] - tmp1 * njac[i - 1][j][k][1][4];
                lhs[i][j][k][0][2][0] = -tmp2 * fjac[i - 1][j][k][2][0] - tmp1 * njac[i - 1][j][k][2][0];
                lhs[i][j][k][0][2][1] = -tmp2 * fjac[i - 1][j][k][2][1] - tmp1 * njac[i - 1][j][k][2][1];
                lhs[i][j][k][0][2][2] = -tmp2 * fjac[i - 1][j][k][2][2] - tmp1 * njac[i - 1][j][k][2][2] - tmp1 * dx3;
                lhs[i][j][k][0][2][3] = -tmp2 * fjac[i - 1][j][k][2][3] - tmp1 * njac[i - 1][j][k][2][3];
                lhs[i][j][k][0][2][4] = -tmp2 * fjac[i - 1][j][k][2][4] - tmp1 * njac[i - 1][j][k][2][4];
                lhs[i][j][k][0][3][0] = -tmp2 * fjac[i - 1][j][k][3][0] - tmp1 * njac[i - 1][j][k][3][0];
                lhs[i][j][k][0][3][1] = -tmp2 * fjac[i - 1][j][k][3][1] - tmp1 * njac[i - 1][j][k][3][1];
                lhs[i][j][k][0][3][2] = -tmp2 * fjac[i - 1][j][k][3][2] - tmp1 * njac[i - 1][j][k][3][2];
                lhs[i][j][k][0][3][3] = -tmp2 * fjac[i - 1][j][k][3][3] - tmp1 * njac[i - 1][j][k][3][3] - tmp1 * dx4;
                lhs[i][j][k][0][3][4] = -tmp2 * fjac[i - 1][j][k][3][4] - tmp1 * njac[i - 1][j][k][3][4];
                lhs[i][j][k][0][4][0] = -tmp2 * fjac[i - 1][j][k][4][0] - tmp1 * njac[i - 1][j][k][4][0];
                lhs[i][j][k][0][4][1] = -tmp2 * fjac[i - 1][j][k][4][1] - tmp1 * njac[i - 1][j][k][4][1];
                lhs[i][j][k][0][4][2] = -tmp2 * fjac[i - 1][j][k][4][2] - tmp1 * njac[i - 1][j][k][4][2];
                lhs[i][j][k][0][4][3] = -tmp2 * fjac[i - 1][j][k][4][3] - tmp1 * njac[i - 1][j][k][4][3];
                lhs[i][j][k][0][4][4] = -tmp2 * fjac[i - 1][j][k][4][4] - tmp1 * njac[i - 1][j][k][4][4] - tmp1 * dx5;
                lhs[i][j][k][1][0][0] = 1.0 + tmp1 * 2.0 * njac[i][j][k][0][0] + tmp1 * 2.0 * dx1;
                lhs[i][j][k][1][0][1] = tmp1 * 2.0 * njac[i][j][k][0][1];
                lhs[i][j][k][1][0][2] = tmp1 * 2.0 * njac[i][j][k][0][2];
                lhs[i][j][k][1][0][3] = tmp1 * 2.0 * njac[i][j][k][0][3];
                lhs[i][j][k][1][0][4] = tmp1 * 2.0 * njac[i][j][k][0][4];
                lhs[i][j][k][1][1][0] = tmp1 * 2.0 * njac[i][j][k][1][0];
                lhs[i][j][k][1][1][1] = 1.0 + tmp1 * 2.0 * njac[i][j][k][1][1] + tmp1 * 2.0 * dx2;
                lhs[i][j][k][1][1][2] = tmp1 * 2.0 * njac[i][j][k][1][2];
                lhs[i][j][k][1][1][3] = tmp1 * 2.0 * njac[i][j][k][1][3];
                lhs[i][j][k][1][1][4] = tmp1 * 2.0 * njac[i][j][k][1][4];
                lhs[i][j][k][1][2][0] = tmp1 * 2.0 * njac[i][j][k][2][0];
                lhs[i][j][k][1][2][1] = tmp1 * 2.0 * njac[i][j][k][2][1];
                lhs[i][j][k][1][2][2] = 1.0 + tmp1 * 2.0 * njac[i][j][k][2][2] + tmp1 * 2.0 * dx3;
                lhs[i][j][k][1][2][3] = tmp1 * 2.0 * njac[i][j][k][2][3];
                lhs[i][j][k][1][2][4] = tmp1 * 2.0 * njac[i][j][k][2][4];
                lhs[i][j][k][1][3][0] = tmp1 * 2.0 * njac[i][j][k][3][0];
                lhs[i][j][k][1][3][1] = tmp1 * 2.0 * njac[i][j][k][3][1];
                lhs[i][j][k][1][3][2] = tmp1 * 2.0 * njac[i][j][k][3][2];
                lhs[i][j][k][1][3][3] = 1.0 + tmp1 * 2.0 * njac[i][j][k][3][3] + tmp1 * 2.0 * dx4;
                lhs[i][j][k][1][3][4] = tmp1 * 2.0 * njac[i][j][k][3][4];
                lhs[i][j][k][1][4][0] = tmp1 * 2.0 * njac[i][j][k][4][0];
                lhs[i][j][k][1][4][1] = tmp1 * 2.0 * njac[i][j][k][4][1];
                lhs[i][j][k][1][4][2] = tmp1 * 2.0 * njac[i][j][k][4][2];
                lhs[i][j][k][1][4][3] = tmp1 * 2.0 * njac[i][j][k][4][3];
                lhs[i][j][k][1][4][4] = 1.0 + tmp1 * 2.0 * njac[i][j][k][4][4] + tmp1 * 2.0 * dx5;
                lhs[i][j][k][2][0][0] = tmp2 * fjac[i + 1][j][k][0][0] - tmp1 * njac[i + 1][j][k][0][0] - tmp1 * dx1;
                lhs[i][j][k][2][0][1] = tmp2 * fjac[i + 1][j][k][0][1] - tmp1 * njac[i + 1][j][k][0][1];
                lhs[i][j][k][2][0][2] = tmp2 * fjac[i + 1][j][k][0][2] - tmp1 * njac[i + 1][j][k][0][2];
                lhs[i][j][k][2][0][3] = tmp2 * fjac[i + 1][j][k][0][3] - tmp1 * njac[i + 1][j][k][0][3];
                lhs[i][j][k][2][0][4] = tmp2 * fjac[i + 1][j][k][0][4] - tmp1 * njac[i + 1][j][k][0][4];
                lhs[i][j][k][2][1][0] = tmp2 * fjac[i + 1][j][k][1][0] - tmp1 * njac[i + 1][j][k][1][0];
                lhs[i][j][k][2][1][1] = tmp2 * fjac[i + 1][j][k][1][1] - tmp1 * njac[i + 1][j][k][1][1] - tmp1 * dx2;
                lhs[i][j][k][2][1][2] = tmp2 * fjac[i + 1][j][k][1][2] - tmp1 * njac[i + 1][j][k][1][2];
                lhs[i][j][k][2][1][3] = tmp2 * fjac[i + 1][j][k][1][3] - tmp1 * njac[i + 1][j][k][1][3];
                lhs[i][j][k][2][1][4] = tmp2 * fjac[i + 1][j][k][1][4] - tmp1 * njac[i + 1][j][k][1][4];
                lhs[i][j][k][2][2][0] = tmp2 * fjac[i + 1][j][k][2][0] - tmp1 * njac[i + 1][j][k][2][0];
                lhs[i][j][k][2][2][1] = tmp2 * fjac[i + 1][j][k][2][1] - tmp1 * njac[i + 1][j][k][2][1];
                lhs[i][j][k][2][2][2] = tmp2 * fjac[i + 1][j][k][2][2] - tmp1 * njac[i + 1][j][k][2][2] - tmp1 * dx3;
                lhs[i][j][k][2][2][3] = tmp2 * fjac[i + 1][j][k][2][3] - tmp1 * njac[i + 1][j][k][2][3];
                lhs[i][j][k][2][2][4] = tmp2 * fjac[i + 1][j][k][2][4] - tmp1 * njac[i + 1][j][k][2][4];
                lhs[i][j][k][2][3][0] = tmp2 * fjac[i + 1][j][k][3][0] - tmp1 * njac[i + 1][j][k][3][0];
                lhs[i][j][k][2][3][1] = tmp2 * fjac[i + 1][j][k][3][1] - tmp1 * njac[i + 1][j][k][3][1];
                lhs[i][j][k][2][3][2] = tmp2 * fjac[i + 1][j][k][3][2] - tmp1 * njac[i + 1][j][k][3][2];
                lhs[i][j][k][2][3][3] = tmp2 * fjac[i + 1][j][k][3][3] - tmp1 * njac[i + 1][j][k][3][3] - tmp1 * dx4;
                lhs[i][j][k][2][3][4] = tmp2 * fjac[i + 1][j][k][3][4] - tmp1 * njac[i + 1][j][k][3][4];
                lhs[i][j][k][2][4][0] = tmp2 * fjac[i + 1][j][k][4][0] - tmp1 * njac[i + 1][j][k][4][0];
                lhs[i][j][k][2][4][1] = tmp2 * fjac[i + 1][j][k][4][1] - tmp1 * njac[i + 1][j][k][4][1];
                lhs[i][j][k][2][4][2] = tmp2 * fjac[i + 1][j][k][4][2] - tmp1 * njac[i + 1][j][k][4][2];
                lhs[i][j][k][2][4][3] = tmp2 * fjac[i + 1][j][k][4][3] - tmp1 * njac[i + 1][j][k][4][3];
                lhs[i][j][k][2][4][4] = tmp2 * fjac[i + 1][j][k][4][4] - tmp1 * njac[i + 1][j][k][4][4] - tmp1 * dx5;
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
}
static void lhsy(void ) {
    int i;
    int j;
    int k;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 0; j < grid_points[1]; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                tmp1 = 1.0 / u[i][j][k][0];
                tmp2 = tmp1 * tmp1;
                tmp3 = tmp1 * tmp2;
                fjac[i][j][k][0][0] = 0.0;
                fjac[i][j][k][0][1] = 0.0;
                fjac[i][j][k][0][2] = 1.0;
                fjac[i][j][k][0][3] = 0.0;
                fjac[i][j][k][0][4] = 0.0;
                fjac[i][j][k][1][0] = -(u[i][j][k][1] * u[i][j][k][2]) * tmp2;
                fjac[i][j][k][1][1] = u[i][j][k][2] * tmp1;
                fjac[i][j][k][1][2] = u[i][j][k][1] * tmp1;
                fjac[i][j][k][1][3] = 0.0;
                fjac[i][j][k][1][4] = 0.0;
                fjac[i][j][k][2][0] = -(u[i][j][k][2] * u[i][j][k][2] * tmp2) + 0.50 * c2 * ((u[i][j][k][1] * u[i][j][k][1] + u[i][j][k][2] * u[i][j][k][2] + u[i][j][k][3] * u[i][j][k][3]) * tmp2);
                fjac[i][j][k][2][1] = -c2 * u[i][j][k][1] * tmp1;
                fjac[i][j][k][2][2] = (2.0 - c2) * u[i][j][k][2] * tmp1;
                fjac[i][j][k][2][3] = -c2 * u[i][j][k][3] * tmp1;
                fjac[i][j][k][2][4] = c2;
                fjac[i][j][k][3][0] = -(u[i][j][k][2] * u[i][j][k][3]) * tmp2;
                fjac[i][j][k][3][1] = 0.0;
                fjac[i][j][k][3][2] = u[i][j][k][3] * tmp1;
                fjac[i][j][k][3][3] = u[i][j][k][2] * tmp1;
                fjac[i][j][k][3][4] = 0.0;
                fjac[i][j][k][4][0] = (c2 * (u[i][j][k][1] * u[i][j][k][1] + u[i][j][k][2] * u[i][j][k][2] + u[i][j][k][3] * u[i][j][k][3]) * tmp2 - c1 * u[i][j][k][4] * tmp1) * u[i][j][k][2] * tmp1;
                fjac[i][j][k][4][1] = -c2 * u[i][j][k][1] * u[i][j][k][2] * tmp2;
                fjac[i][j][k][4][2] = c1 * u[i][j][k][4] * tmp1 - 0.50 * c2 * ((u[i][j][k][1] * u[i][j][k][1] + 3.0 * u[i][j][k][2] * u[i][j][k][2] + u[i][j][k][3] * u[i][j][k][3]) * tmp2);
                fjac[i][j][k][4][3] = -c2 * (u[i][j][k][2] * u[i][j][k][3]) * tmp2;
                fjac[i][j][k][4][4] = c1 * u[i][j][k][2] * tmp1;
                njac[i][j][k][0][0] = 0.0;
                njac[i][j][k][0][1] = 0.0;
                njac[i][j][k][0][2] = 0.0;
                njac[i][j][k][0][3] = 0.0;
                njac[i][j][k][0][4] = 0.0;
                njac[i][j][k][1][0] = -c3c4 * tmp2 * u[i][j][k][1];
                njac[i][j][k][1][1] = c3c4 * tmp1;
                njac[i][j][k][1][2] = 0.0;
                njac[i][j][k][1][3] = 0.0;
                njac[i][j][k][1][4] = 0.0;
                njac[i][j][k][2][0] = -con43 * c3c4 * tmp2 * u[i][j][k][2];
                njac[i][j][k][2][1] = 0.0;
                njac[i][j][k][2][2] = con43 * c3c4 * tmp1;
                njac[i][j][k][2][3] = 0.0;
                njac[i][j][k][2][4] = 0.0;
                njac[i][j][k][3][0] = -c3c4 * tmp2 * u[i][j][k][3];
                njac[i][j][k][3][1] = 0.0;
                njac[i][j][k][3][2] = 0.0;
                njac[i][j][k][3][3] = c3c4 * tmp1;
                njac[i][j][k][3][4] = 0.0;
                njac[i][j][k][4][0] = -(c3c4 - c1345) * tmp3 * (((u[i][j][k][1]) * (u[i][j][k][1]))) - (con43 * c3c4 - c1345) * tmp3 * (((u[i][j][k][2]) * (u[i][j][k][2]))) - (c3c4 - c1345) * tmp3 * (((u[i][j][k][3]) * (u[i][j][k][3]))) - c1345 * tmp2 * u[i][j][k][4];
                njac[i][j][k][4][1] = (c3c4 - c1345) * tmp2 * u[i][j][k][1];
                njac[i][j][k][4][2] = (con43 * c3c4 - c1345) * tmp2 * u[i][j][k][2];
                njac[i][j][k][4][3] = (c3c4 - c1345) * tmp2 * u[i][j][k][3];
                njac[i][j][k][4][4] = c1345 * tmp1;
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written([fjac.f, tmp2, tmp1, tmp3, njac.f]) read([fjac.f, grid_points, fjac, grid_points.f, dt, dy5, dy4, dy3, dy2, dy1, lhs.f, tmp2, lhs, tmp1, ty1, ty2, njac.f, njac, i])
#pragma omp barrier
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                tmp1 = dt * ty1;
                tmp2 = dt * ty2;
                lhs[i][j][k][0][0][0] = -tmp2 * fjac[i][j - 1][k][0][0] - tmp1 * njac[i][j - 1][k][0][0] - tmp1 * dy1;
                lhs[i][j][k][0][0][1] = -tmp2 * fjac[i][j - 1][k][0][1] - tmp1 * njac[i][j - 1][k][0][1];
                lhs[i][j][k][0][0][2] = -tmp2 * fjac[i][j - 1][k][0][2] - tmp1 * njac[i][j - 1][k][0][2];
                lhs[i][j][k][0][0][3] = -tmp2 * fjac[i][j - 1][k][0][3] - tmp1 * njac[i][j - 1][k][0][3];
                lhs[i][j][k][0][0][4] = -tmp2 * fjac[i][j - 1][k][0][4] - tmp1 * njac[i][j - 1][k][0][4];
                lhs[i][j][k][0][1][0] = -tmp2 * fjac[i][j - 1][k][1][0] - tmp1 * njac[i][j - 1][k][1][0];
                lhs[i][j][k][0][1][1] = -tmp2 * fjac[i][j - 1][k][1][1] - tmp1 * njac[i][j - 1][k][1][1] - tmp1 * dy2;
                lhs[i][j][k][0][1][2] = -tmp2 * fjac[i][j - 1][k][1][2] - tmp1 * njac[i][j - 1][k][1][2];
                lhs[i][j][k][0][1][3] = -tmp2 * fjac[i][j - 1][k][1][3] - tmp1 * njac[i][j - 1][k][1][3];
                lhs[i][j][k][0][1][4] = -tmp2 * fjac[i][j - 1][k][1][4] - tmp1 * njac[i][j - 1][k][1][4];
                lhs[i][j][k][0][2][0] = -tmp2 * fjac[i][j - 1][k][2][0] - tmp1 * njac[i][j - 1][k][2][0];
                lhs[i][j][k][0][2][1] = -tmp2 * fjac[i][j - 1][k][2][1] - tmp1 * njac[i][j - 1][k][2][1];
                lhs[i][j][k][0][2][2] = -tmp2 * fjac[i][j - 1][k][2][2] - tmp1 * njac[i][j - 1][k][2][2] - tmp1 * dy3;
                lhs[i][j][k][0][2][3] = -tmp2 * fjac[i][j - 1][k][2][3] - tmp1 * njac[i][j - 1][k][2][3];
                lhs[i][j][k][0][2][4] = -tmp2 * fjac[i][j - 1][k][2][4] - tmp1 * njac[i][j - 1][k][2][4];
                lhs[i][j][k][0][3][0] = -tmp2 * fjac[i][j - 1][k][3][0] - tmp1 * njac[i][j - 1][k][3][0];
                lhs[i][j][k][0][3][1] = -tmp2 * fjac[i][j - 1][k][3][1] - tmp1 * njac[i][j - 1][k][3][1];
                lhs[i][j][k][0][3][2] = -tmp2 * fjac[i][j - 1][k][3][2] - tmp1 * njac[i][j - 1][k][3][2];
                lhs[i][j][k][0][3][3] = -tmp2 * fjac[i][j - 1][k][3][3] - tmp1 * njac[i][j - 1][k][3][3] - tmp1 * dy4;
                lhs[i][j][k][0][3][4] = -tmp2 * fjac[i][j - 1][k][3][4] - tmp1 * njac[i][j - 1][k][3][4];
                lhs[i][j][k][0][4][0] = -tmp2 * fjac[i][j - 1][k][4][0] - tmp1 * njac[i][j - 1][k][4][0];
                lhs[i][j][k][0][4][1] = -tmp2 * fjac[i][j - 1][k][4][1] - tmp1 * njac[i][j - 1][k][4][1];
                lhs[i][j][k][0][4][2] = -tmp2 * fjac[i][j - 1][k][4][2] - tmp1 * njac[i][j - 1][k][4][2];
                lhs[i][j][k][0][4][3] = -tmp2 * fjac[i][j - 1][k][4][3] - tmp1 * njac[i][j - 1][k][4][3];
                lhs[i][j][k][0][4][4] = -tmp2 * fjac[i][j - 1][k][4][4] - tmp1 * njac[i][j - 1][k][4][4] - tmp1 * dy5;
                lhs[i][j][k][1][0][0] = 1.0 + tmp1 * 2.0 * njac[i][j][k][0][0] + tmp1 * 2.0 * dy1;
                lhs[i][j][k][1][0][1] = tmp1 * 2.0 * njac[i][j][k][0][1];
                lhs[i][j][k][1][0][2] = tmp1 * 2.0 * njac[i][j][k][0][2];
                lhs[i][j][k][1][0][3] = tmp1 * 2.0 * njac[i][j][k][0][3];
                lhs[i][j][k][1][0][4] = tmp1 * 2.0 * njac[i][j][k][0][4];
                lhs[i][j][k][1][1][0] = tmp1 * 2.0 * njac[i][j][k][1][0];
                lhs[i][j][k][1][1][1] = 1.0 + tmp1 * 2.0 * njac[i][j][k][1][1] + tmp1 * 2.0 * dy2;
                lhs[i][j][k][1][1][2] = tmp1 * 2.0 * njac[i][j][k][1][2];
                lhs[i][j][k][1][1][3] = tmp1 * 2.0 * njac[i][j][k][1][3];
                lhs[i][j][k][1][1][4] = tmp1 * 2.0 * njac[i][j][k][1][4];
                lhs[i][j][k][1][2][0] = tmp1 * 2.0 * njac[i][j][k][2][0];
                lhs[i][j][k][1][2][1] = tmp1 * 2.0 * njac[i][j][k][2][1];
                lhs[i][j][k][1][2][2] = 1.0 + tmp1 * 2.0 * njac[i][j][k][2][2] + tmp1 * 2.0 * dy3;
                lhs[i][j][k][1][2][3] = tmp1 * 2.0 * njac[i][j][k][2][3];
                lhs[i][j][k][1][2][4] = tmp1 * 2.0 * njac[i][j][k][2][4];
                lhs[i][j][k][1][3][0] = tmp1 * 2.0 * njac[i][j][k][3][0];
                lhs[i][j][k][1][3][1] = tmp1 * 2.0 * njac[i][j][k][3][1];
                lhs[i][j][k][1][3][2] = tmp1 * 2.0 * njac[i][j][k][3][2];
                lhs[i][j][k][1][3][3] = 1.0 + tmp1 * 2.0 * njac[i][j][k][3][3] + tmp1 * 2.0 * dy4;
                lhs[i][j][k][1][3][4] = tmp1 * 2.0 * njac[i][j][k][3][4];
                lhs[i][j][k][1][4][0] = tmp1 * 2.0 * njac[i][j][k][4][0];
                lhs[i][j][k][1][4][1] = tmp1 * 2.0 * njac[i][j][k][4][1];
                lhs[i][j][k][1][4][2] = tmp1 * 2.0 * njac[i][j][k][4][2];
                lhs[i][j][k][1][4][3] = tmp1 * 2.0 * njac[i][j][k][4][3];
                lhs[i][j][k][1][4][4] = 1.0 + tmp1 * 2.0 * njac[i][j][k][4][4] + tmp1 * 2.0 * dy5;
                lhs[i][j][k][2][0][0] = tmp2 * fjac[i][j + 1][k][0][0] - tmp1 * njac[i][j + 1][k][0][0] - tmp1 * dy1;
                lhs[i][j][k][2][0][1] = tmp2 * fjac[i][j + 1][k][0][1] - tmp1 * njac[i][j + 1][k][0][1];
                lhs[i][j][k][2][0][2] = tmp2 * fjac[i][j + 1][k][0][2] - tmp1 * njac[i][j + 1][k][0][2];
                lhs[i][j][k][2][0][3] = tmp2 * fjac[i][j + 1][k][0][3] - tmp1 * njac[i][j + 1][k][0][3];
                lhs[i][j][k][2][0][4] = tmp2 * fjac[i][j + 1][k][0][4] - tmp1 * njac[i][j + 1][k][0][4];
                lhs[i][j][k][2][1][0] = tmp2 * fjac[i][j + 1][k][1][0] - tmp1 * njac[i][j + 1][k][1][0];
                lhs[i][j][k][2][1][1] = tmp2 * fjac[i][j + 1][k][1][1] - tmp1 * njac[i][j + 1][k][1][1] - tmp1 * dy2;
                lhs[i][j][k][2][1][2] = tmp2 * fjac[i][j + 1][k][1][2] - tmp1 * njac[i][j + 1][k][1][2];
                lhs[i][j][k][2][1][3] = tmp2 * fjac[i][j + 1][k][1][3] - tmp1 * njac[i][j + 1][k][1][3];
                lhs[i][j][k][2][1][4] = tmp2 * fjac[i][j + 1][k][1][4] - tmp1 * njac[i][j + 1][k][1][4];
                lhs[i][j][k][2][2][0] = tmp2 * fjac[i][j + 1][k][2][0] - tmp1 * njac[i][j + 1][k][2][0];
                lhs[i][j][k][2][2][1] = tmp2 * fjac[i][j + 1][k][2][1] - tmp1 * njac[i][j + 1][k][2][1];
                lhs[i][j][k][2][2][2] = tmp2 * fjac[i][j + 1][k][2][2] - tmp1 * njac[i][j + 1][k][2][2] - tmp1 * dy3;
                lhs[i][j][k][2][2][3] = tmp2 * fjac[i][j + 1][k][2][3] - tmp1 * njac[i][j + 1][k][2][3];
                lhs[i][j][k][2][2][4] = tmp2 * fjac[i][j + 1][k][2][4] - tmp1 * njac[i][j + 1][k][2][4];
                lhs[i][j][k][2][3][0] = tmp2 * fjac[i][j + 1][k][3][0] - tmp1 * njac[i][j + 1][k][3][0];
                lhs[i][j][k][2][3][1] = tmp2 * fjac[i][j + 1][k][3][1] - tmp1 * njac[i][j + 1][k][3][1];
                lhs[i][j][k][2][3][2] = tmp2 * fjac[i][j + 1][k][3][2] - tmp1 * njac[i][j + 1][k][3][2];
                lhs[i][j][k][2][3][3] = tmp2 * fjac[i][j + 1][k][3][3] - tmp1 * njac[i][j + 1][k][3][3] - tmp1 * dy4;
                lhs[i][j][k][2][3][4] = tmp2 * fjac[i][j + 1][k][3][4] - tmp1 * njac[i][j + 1][k][3][4];
                lhs[i][j][k][2][4][0] = tmp2 * fjac[i][j + 1][k][4][0] - tmp1 * njac[i][j + 1][k][4][0];
                lhs[i][j][k][2][4][1] = tmp2 * fjac[i][j + 1][k][4][1] - tmp1 * njac[i][j + 1][k][4][1];
                lhs[i][j][k][2][4][2] = tmp2 * fjac[i][j + 1][k][4][2] - tmp1 * njac[i][j + 1][k][4][2];
                lhs[i][j][k][2][4][3] = tmp2 * fjac[i][j + 1][k][4][3] - tmp1 * njac[i][j + 1][k][4][3];
                lhs[i][j][k][2][4][4] = tmp2 * fjac[i][j + 1][k][4][4] - tmp1 * njac[i][j + 1][k][4][4] - tmp1 * dy5;
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written([lhs.f, tmp2, tmp1]) read([lhs.f, matvec_sub, binvcrhs, grid_points, grid_points.f, binvrhs, lhs, rhs, i, matmul_sub, rhs.f, y_solve_cell])
#pragma omp barrier
}
static void lhsz(void ) {
    int i;
    int j;
    int k;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 0; k < grid_points[2]; k++) {
                tmp1 = 1.0 / u[i][j][k][0];
                tmp2 = tmp1 * tmp1;
                tmp3 = tmp1 * tmp2;
                fjac[i][j][k][0][0] = 0.0;
                fjac[i][j][k][0][1] = 0.0;
                fjac[i][j][k][0][2] = 0.0;
                fjac[i][j][k][0][3] = 1.0;
                fjac[i][j][k][0][4] = 0.0;
                fjac[i][j][k][1][0] = -(u[i][j][k][1] * u[i][j][k][3]) * tmp2;
                fjac[i][j][k][1][1] = u[i][j][k][3] * tmp1;
                fjac[i][j][k][1][2] = 0.0;
                fjac[i][j][k][1][3] = u[i][j][k][1] * tmp1;
                fjac[i][j][k][1][4] = 0.0;
                fjac[i][j][k][2][0] = -(u[i][j][k][2] * u[i][j][k][3]) * tmp2;
                fjac[i][j][k][2][1] = 0.0;
                fjac[i][j][k][2][2] = u[i][j][k][3] * tmp1;
                fjac[i][j][k][2][3] = u[i][j][k][2] * tmp1;
                fjac[i][j][k][2][4] = 0.0;
                fjac[i][j][k][3][0] = -(u[i][j][k][3] * u[i][j][k][3] * tmp2) + 0.50 * c2 * ((u[i][j][k][1] * u[i][j][k][1] + u[i][j][k][2] * u[i][j][k][2] + u[i][j][k][3] * u[i][j][k][3]) * tmp2);
                fjac[i][j][k][3][1] = -c2 * u[i][j][k][1] * tmp1;
                fjac[i][j][k][3][2] = -c2 * u[i][j][k][2] * tmp1;
                fjac[i][j][k][3][3] = (2.0 - c2) * u[i][j][k][3] * tmp1;
                fjac[i][j][k][3][4] = c2;
                fjac[i][j][k][4][0] = (c2 * (u[i][j][k][1] * u[i][j][k][1] + u[i][j][k][2] * u[i][j][k][2] + u[i][j][k][3] * u[i][j][k][3]) * tmp2 - c1 * (u[i][j][k][4] * tmp1)) * (u[i][j][k][3] * tmp1);
                fjac[i][j][k][4][1] = -c2 * (u[i][j][k][1] * u[i][j][k][3]) * tmp2;
                fjac[i][j][k][4][2] = -c2 * (u[i][j][k][2] * u[i][j][k][3]) * tmp2;
                fjac[i][j][k][4][3] = c1 * (u[i][j][k][4] * tmp1) - 0.50 * c2 * ((u[i][j][k][1] * u[i][j][k][1] + u[i][j][k][2] * u[i][j][k][2] + 3.0 * u[i][j][k][3] * u[i][j][k][3]) * tmp2);
                fjac[i][j][k][4][4] = c1 * u[i][j][k][3] * tmp1;
                njac[i][j][k][0][0] = 0.0;
                njac[i][j][k][0][1] = 0.0;
                njac[i][j][k][0][2] = 0.0;
                njac[i][j][k][0][3] = 0.0;
                njac[i][j][k][0][4] = 0.0;
                njac[i][j][k][1][0] = -c3c4 * tmp2 * u[i][j][k][1];
                njac[i][j][k][1][1] = c3c4 * tmp1;
                njac[i][j][k][1][2] = 0.0;
                njac[i][j][k][1][3] = 0.0;
                njac[i][j][k][1][4] = 0.0;
                njac[i][j][k][2][0] = -c3c4 * tmp2 * u[i][j][k][2];
                njac[i][j][k][2][1] = 0.0;
                njac[i][j][k][2][2] = c3c4 * tmp1;
                njac[i][j][k][2][3] = 0.0;
                njac[i][j][k][2][4] = 0.0;
                njac[i][j][k][3][0] = -con43 * c3c4 * tmp2 * u[i][j][k][3];
                njac[i][j][k][3][1] = 0.0;
                njac[i][j][k][3][2] = 0.0;
                njac[i][j][k][3][3] = con43 * c3 * c4 * tmp1;
                njac[i][j][k][3][4] = 0.0;
                njac[i][j][k][4][0] = -(c3c4 - c1345) * tmp3 * (((u[i][j][k][1]) * (u[i][j][k][1]))) - (c3c4 - c1345) * tmp3 * (((u[i][j][k][2]) * (u[i][j][k][2]))) - (con43 * c3c4 - c1345) * tmp3 * (((u[i][j][k][3]) * (u[i][j][k][3]))) - c1345 * tmp2 * u[i][j][k][4];
                njac[i][j][k][4][1] = (c3c4 - c1345) * tmp2 * u[i][j][k][1];
                njac[i][j][k][4][2] = (c3c4 - c1345) * tmp2 * u[i][j][k][2];
                njac[i][j][k][4][3] = (con43 * c3c4 - c1345) * tmp2 * u[i][j][k][3];
                njac[i][j][k][4][4] = c1345 * tmp1;
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written([fjac.f, tmp2, tmp1, tmp3, njac.f]) read([fjac.f, grid_points, fjac, grid_points.f, tz1, dt, dz5, dz4, dz3, dz2, lhs.f, dz1, i, tmp2, lhs, tmp1, tz2, njac.f, njac])
#pragma omp barrier
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                tmp1 = dt * tz1;
                tmp2 = dt * tz2;
                lhs[i][j][k][0][0][0] = -tmp2 * fjac[i][j][k - 1][0][0] - tmp1 * njac[i][j][k - 1][0][0] - tmp1 * dz1;
                lhs[i][j][k][0][0][1] = -tmp2 * fjac[i][j][k - 1][0][1] - tmp1 * njac[i][j][k - 1][0][1];
                lhs[i][j][k][0][0][2] = -tmp2 * fjac[i][j][k - 1][0][2] - tmp1 * njac[i][j][k - 1][0][2];
                lhs[i][j][k][0][0][3] = -tmp2 * fjac[i][j][k - 1][0][3] - tmp1 * njac[i][j][k - 1][0][3];
                lhs[i][j][k][0][0][4] = -tmp2 * fjac[i][j][k - 1][0][4] - tmp1 * njac[i][j][k - 1][0][4];
                lhs[i][j][k][0][1][0] = -tmp2 * fjac[i][j][k - 1][1][0] - tmp1 * njac[i][j][k - 1][1][0];
                lhs[i][j][k][0][1][1] = -tmp2 * fjac[i][j][k - 1][1][1] - tmp1 * njac[i][j][k - 1][1][1] - tmp1 * dz2;
                lhs[i][j][k][0][1][2] = -tmp2 * fjac[i][j][k - 1][1][2] - tmp1 * njac[i][j][k - 1][1][2];
                lhs[i][j][k][0][1][3] = -tmp2 * fjac[i][j][k - 1][1][3] - tmp1 * njac[i][j][k - 1][1][3];
                lhs[i][j][k][0][1][4] = -tmp2 * fjac[i][j][k - 1][1][4] - tmp1 * njac[i][j][k - 1][1][4];
                lhs[i][j][k][0][2][0] = -tmp2 * fjac[i][j][k - 1][2][0] - tmp1 * njac[i][j][k - 1][2][0];
                lhs[i][j][k][0][2][1] = -tmp2 * fjac[i][j][k - 1][2][1] - tmp1 * njac[i][j][k - 1][2][1];
                lhs[i][j][k][0][2][2] = -tmp2 * fjac[i][j][k - 1][2][2] - tmp1 * njac[i][j][k - 1][2][2] - tmp1 * dz3;
                lhs[i][j][k][0][2][3] = -tmp2 * fjac[i][j][k - 1][2][3] - tmp1 * njac[i][j][k - 1][2][3];
                lhs[i][j][k][0][2][4] = -tmp2 * fjac[i][j][k - 1][2][4] - tmp1 * njac[i][j][k - 1][2][4];
                lhs[i][j][k][0][3][0] = -tmp2 * fjac[i][j][k - 1][3][0] - tmp1 * njac[i][j][k - 1][3][0];
                lhs[i][j][k][0][3][1] = -tmp2 * fjac[i][j][k - 1][3][1] - tmp1 * njac[i][j][k - 1][3][1];
                lhs[i][j][k][0][3][2] = -tmp2 * fjac[i][j][k - 1][3][2] - tmp1 * njac[i][j][k - 1][3][2];
                lhs[i][j][k][0][3][3] = -tmp2 * fjac[i][j][k - 1][3][3] - tmp1 * njac[i][j][k - 1][3][3] - tmp1 * dz4;
                lhs[i][j][k][0][3][4] = -tmp2 * fjac[i][j][k - 1][3][4] - tmp1 * njac[i][j][k - 1][3][4];
                lhs[i][j][k][0][4][0] = -tmp2 * fjac[i][j][k - 1][4][0] - tmp1 * njac[i][j][k - 1][4][0];
                lhs[i][j][k][0][4][1] = -tmp2 * fjac[i][j][k - 1][4][1] - tmp1 * njac[i][j][k - 1][4][1];
                lhs[i][j][k][0][4][2] = -tmp2 * fjac[i][j][k - 1][4][2] - tmp1 * njac[i][j][k - 1][4][2];
                lhs[i][j][k][0][4][3] = -tmp2 * fjac[i][j][k - 1][4][3] - tmp1 * njac[i][j][k - 1][4][3];
                lhs[i][j][k][0][4][4] = -tmp2 * fjac[i][j][k - 1][4][4] - tmp1 * njac[i][j][k - 1][4][4] - tmp1 * dz5;
                lhs[i][j][k][1][0][0] = 1.0 + tmp1 * 2.0 * njac[i][j][k][0][0] + tmp1 * 2.0 * dz1;
                lhs[i][j][k][1][0][1] = tmp1 * 2.0 * njac[i][j][k][0][1];
                lhs[i][j][k][1][0][2] = tmp1 * 2.0 * njac[i][j][k][0][2];
                lhs[i][j][k][1][0][3] = tmp1 * 2.0 * njac[i][j][k][0][3];
                lhs[i][j][k][1][0][4] = tmp1 * 2.0 * njac[i][j][k][0][4];
                lhs[i][j][k][1][1][0] = tmp1 * 2.0 * njac[i][j][k][1][0];
                lhs[i][j][k][1][1][1] = 1.0 + tmp1 * 2.0 * njac[i][j][k][1][1] + tmp1 * 2.0 * dz2;
                lhs[i][j][k][1][1][2] = tmp1 * 2.0 * njac[i][j][k][1][2];
                lhs[i][j][k][1][1][3] = tmp1 * 2.0 * njac[i][j][k][1][3];
                lhs[i][j][k][1][1][4] = tmp1 * 2.0 * njac[i][j][k][1][4];
                lhs[i][j][k][1][2][0] = tmp1 * 2.0 * njac[i][j][k][2][0];
                lhs[i][j][k][1][2][1] = tmp1 * 2.0 * njac[i][j][k][2][1];
                lhs[i][j][k][1][2][2] = 1.0 + tmp1 * 2.0 * njac[i][j][k][2][2] + tmp1 * 2.0 * dz3;
                lhs[i][j][k][1][2][3] = tmp1 * 2.0 * njac[i][j][k][2][3];
                lhs[i][j][k][1][2][4] = tmp1 * 2.0 * njac[i][j][k][2][4];
                lhs[i][j][k][1][3][0] = tmp1 * 2.0 * njac[i][j][k][3][0];
                lhs[i][j][k][1][3][1] = tmp1 * 2.0 * njac[i][j][k][3][1];
                lhs[i][j][k][1][3][2] = tmp1 * 2.0 * njac[i][j][k][3][2];
                lhs[i][j][k][1][3][3] = 1.0 + tmp1 * 2.0 * njac[i][j][k][3][3] + tmp1 * 2.0 * dz4;
                lhs[i][j][k][1][3][4] = tmp1 * 2.0 * njac[i][j][k][3][4];
                lhs[i][j][k][1][4][0] = tmp1 * 2.0 * njac[i][j][k][4][0];
                lhs[i][j][k][1][4][1] = tmp1 * 2.0 * njac[i][j][k][4][1];
                lhs[i][j][k][1][4][2] = tmp1 * 2.0 * njac[i][j][k][4][2];
                lhs[i][j][k][1][4][3] = tmp1 * 2.0 * njac[i][j][k][4][3];
                lhs[i][j][k][1][4][4] = 1.0 + tmp1 * 2.0 * njac[i][j][k][4][4] + tmp1 * 2.0 * dz5;
                lhs[i][j][k][2][0][0] = tmp2 * fjac[i][j][k + 1][0][0] - tmp1 * njac[i][j][k + 1][0][0] - tmp1 * dz1;
                lhs[i][j][k][2][0][1] = tmp2 * fjac[i][j][k + 1][0][1] - tmp1 * njac[i][j][k + 1][0][1];
                lhs[i][j][k][2][0][2] = tmp2 * fjac[i][j][k + 1][0][2] - tmp1 * njac[i][j][k + 1][0][2];
                lhs[i][j][k][2][0][3] = tmp2 * fjac[i][j][k + 1][0][3] - tmp1 * njac[i][j][k + 1][0][3];
                lhs[i][j][k][2][0][4] = tmp2 * fjac[i][j][k + 1][0][4] - tmp1 * njac[i][j][k + 1][0][4];
                lhs[i][j][k][2][1][0] = tmp2 * fjac[i][j][k + 1][1][0] - tmp1 * njac[i][j][k + 1][1][0];
                lhs[i][j][k][2][1][1] = tmp2 * fjac[i][j][k + 1][1][1] - tmp1 * njac[i][j][k + 1][1][1] - tmp1 * dz2;
                lhs[i][j][k][2][1][2] = tmp2 * fjac[i][j][k + 1][1][2] - tmp1 * njac[i][j][k + 1][1][2];
                lhs[i][j][k][2][1][3] = tmp2 * fjac[i][j][k + 1][1][3] - tmp1 * njac[i][j][k + 1][1][3];
                lhs[i][j][k][2][1][4] = tmp2 * fjac[i][j][k + 1][1][4] - tmp1 * njac[i][j][k + 1][1][4];
                lhs[i][j][k][2][2][0] = tmp2 * fjac[i][j][k + 1][2][0] - tmp1 * njac[i][j][k + 1][2][0];
                lhs[i][j][k][2][2][1] = tmp2 * fjac[i][j][k + 1][2][1] - tmp1 * njac[i][j][k + 1][2][1];
                lhs[i][j][k][2][2][2] = tmp2 * fjac[i][j][k + 1][2][2] - tmp1 * njac[i][j][k + 1][2][2] - tmp1 * dz3;
                lhs[i][j][k][2][2][3] = tmp2 * fjac[i][j][k + 1][2][3] - tmp1 * njac[i][j][k + 1][2][3];
                lhs[i][j][k][2][2][4] = tmp2 * fjac[i][j][k + 1][2][4] - tmp1 * njac[i][j][k + 1][2][4];
                lhs[i][j][k][2][3][0] = tmp2 * fjac[i][j][k + 1][3][0] - tmp1 * njac[i][j][k + 1][3][0];
                lhs[i][j][k][2][3][1] = tmp2 * fjac[i][j][k + 1][3][1] - tmp1 * njac[i][j][k + 1][3][1];
                lhs[i][j][k][2][3][2] = tmp2 * fjac[i][j][k + 1][3][2] - tmp1 * njac[i][j][k + 1][3][2];
                lhs[i][j][k][2][3][3] = tmp2 * fjac[i][j][k + 1][3][3] - tmp1 * njac[i][j][k + 1][3][3] - tmp1 * dz4;
                lhs[i][j][k][2][3][4] = tmp2 * fjac[i][j][k + 1][3][4] - tmp1 * njac[i][j][k + 1][3][4];
                lhs[i][j][k][2][4][0] = tmp2 * fjac[i][j][k + 1][4][0] - tmp1 * njac[i][j][k + 1][4][0];
                lhs[i][j][k][2][4][1] = tmp2 * fjac[i][j][k + 1][4][1] - tmp1 * njac[i][j][k + 1][4][1];
                lhs[i][j][k][2][4][2] = tmp2 * fjac[i][j][k + 1][4][2] - tmp1 * njac[i][j][k + 1][4][2];
                lhs[i][j][k][2][4][3] = tmp2 * fjac[i][j][k + 1][4][3] - tmp1 * njac[i][j][k + 1][4][3];
                lhs[i][j][k][2][4][4] = tmp2 * fjac[i][j][k + 1][4][4] - tmp1 * njac[i][j][k + 1][4][4] - tmp1 * dz5;
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written([lhs.f, tmp2, tmp1]) read([lhs.f, matvec_sub, binvcrhs, grid_points, z_solve_cell, grid_points.f, binvrhs, lhs, rhs, matmul_sub, i, rhs.f])
#pragma omp barrier
}
static void compute_rhs(void ) {
    int i;
    int j;
    int k;
    int m;
    double rho_inv;
    double uijk;
    double up1;
    double um1;
    double vijk;
    double vp1;
    double vm1;
    double wijk;
    double wp1;
    double wm1;
#pragma omp for nowait
    for (i = 0; i < grid_points[0]; i++) {
        for (j = 0; j < grid_points[1]; j++) {
            for (k = 0; k < grid_points[2]; k++) {
                rho_inv = 1.0 / u[i][j][k][0];
                rho_i[i][j][k] = rho_inv;
                us[i][j][k] = u[i][j][k][1] * rho_inv;
                vs[i][j][k] = u[i][j][k][2] * rho_inv;
                ws[i][j][k] = u[i][j][k][3] * rho_inv;
                square[i][j][k] = 0.5 * (u[i][j][k][1] * u[i][j][k][1] + u[i][j][k][2] * u[i][j][k][2] + u[i][j][k][3] * u[i][j][k][3]) * rho_inv;
                qs[i][j][k] = square[i][j][k] * rho_inv;
            }
        }
    }
#pragma omp for nowait
    for (i = 0; i < grid_points[0]; i++) {
        for (j = 0; j < grid_points[1]; j++) {
            for (k = 0; k < grid_points[2]; k++) {
                for (m = 0; m < 5; m++) {
                    rhs[i][j][k][m] = forcing[i][j][k][m];
                }
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                uijk = us[i][j][k];
                up1 = us[i + 1][j][k];
                um1 = us[i - 1][j][k];
                rhs[i][j][k][0] = rhs[i][j][k][0] + dx1tx1 * (u[i + 1][j][k][0] - 2.0 * u[i][j][k][0] + u[i - 1][j][k][0]) - tx2 * (u[i + 1][j][k][1] - u[i - 1][j][k][1]);
                rhs[i][j][k][1] = rhs[i][j][k][1] + dx2tx1 * (u[i + 1][j][k][1] - 2.0 * u[i][j][k][1] + u[i - 1][j][k][1]) + xxcon2 * con43 * (up1 - 2.0 * uijk + um1) - tx2 * (u[i + 1][j][k][1] * up1 - u[i - 1][j][k][1] * um1 + (u[i + 1][j][k][4] - square[i + 1][j][k] - u[i - 1][j][k][4] + square[i - 1][j][k]) * c2);
                rhs[i][j][k][2] = rhs[i][j][k][2] + dx3tx1 * (u[i + 1][j][k][2] - 2.0 * u[i][j][k][2] + u[i - 1][j][k][2]) + xxcon2 * (vs[i + 1][j][k] - 2.0 * vs[i][j][k] + vs[i - 1][j][k]) - tx2 * (u[i + 1][j][k][2] * up1 - u[i - 1][j][k][2] * um1);
                rhs[i][j][k][3] = rhs[i][j][k][3] + dx4tx1 * (u[i + 1][j][k][3] - 2.0 * u[i][j][k][3] + u[i - 1][j][k][3]) + xxcon2 * (ws[i + 1][j][k] - 2.0 * ws[i][j][k] + ws[i - 1][j][k]) - tx2 * (u[i + 1][j][k][3] * up1 - u[i - 1][j][k][3] * um1);
                rhs[i][j][k][4] = rhs[i][j][k][4] + dx5tx1 * (u[i + 1][j][k][4] - 2.0 * u[i][j][k][4] + u[i - 1][j][k][4]) + xxcon3 * (qs[i + 1][j][k] - 2.0 * qs[i][j][k] + qs[i - 1][j][k]) + xxcon4 * (up1 * up1 - 2.0 * uijk * uijk + um1 * um1) + xxcon5 * (u[i + 1][j][k][4] * rho_i[i + 1][j][k] - 2.0 * u[i][j][k][4] * rho_i[i][j][k] + u[i - 1][j][k][4] * rho_i[i - 1][j][k]) - tx2 * ((c1 * u[i + 1][j][k][4] - c2 * square[i + 1][j][k]) * up1 - (c1 * u[i - 1][j][k][4] - c2 * square[i - 1][j][k]) * um1);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    i = 1;
#pragma omp for nowait
    for (j = 1; j < grid_points[1] - 1; j++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m = 0; m < 5; m++) {
                rhs[i][j][k][m] = rhs[i][j][k][m] - dssp * (5.0 * u[i][j][k][m] - 4.0 * u[i + 1][j][k][m] + u[i + 2][j][k][m]);
            }
        }
    }
    i = 2;
#pragma omp for nowait
    for (j = 1; j < grid_points[1] - 1; j++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m = 0; m < 5; m++) {
                rhs[i][j][k][m] = rhs[i][j][k][m] - dssp * (-4.0 * u[i - 1][j][k][m] + 6.0 * u[i][j][k][m] - 4.0 * u[i + 1][j][k][m] + u[i + 2][j][k][m]);
            }
        }
    }
#pragma omp for nowait
    for (i = 3; i < grid_points[0] - 3; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                for (m = 0; m < 5; m++) {
                    rhs[i][j][k][m] = rhs[i][j][k][m] - dssp * (u[i - 2][j][k][m] - 4.0 * u[i - 1][j][k][m] + 6.0 * u[i][j][k][m] - 4.0 * u[i + 1][j][k][m] + u[i + 2][j][k][m]);
                }
            }
        }
    }
    i = grid_points[0] - 3;
#pragma omp for nowait
    for (j = 1; j < grid_points[1] - 1; j++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m = 0; m < 5; m++) {
                rhs[i][j][k][m] = rhs[i][j][k][m] - dssp * (u[i - 2][j][k][m] - 4.0 * u[i - 1][j][k][m] + 6.0 * u[i][j][k][m] - 4.0 * u[i + 1][j][k][m]);
            }
        }
    }
    i = grid_points[0] - 2;
#pragma omp for nowait
    for (j = 1; j < grid_points[1] - 1; j++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m = 0; m < 5; m++) {
                rhs[i][j][k][m] = rhs[i][j][k][m] - dssp * (u[i - 2][j][k][m] - 4. * u[i - 1][j][k][m] + 5.0 * u[i][j][k][m]);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                vijk = vs[i][j][k];
                vp1 = vs[i][j + 1][k];
                vm1 = vs[i][j - 1][k];
                rhs[i][j][k][0] = rhs[i][j][k][0] + dy1ty1 * (u[i][j + 1][k][0] - 2.0 * u[i][j][k][0] + u[i][j - 1][k][0]) - ty2 * (u[i][j + 1][k][2] - u[i][j - 1][k][2]);
                rhs[i][j][k][1] = rhs[i][j][k][1] + dy2ty1 * (u[i][j + 1][k][1] - 2.0 * u[i][j][k][1] + u[i][j - 1][k][1]) + yycon2 * (us[i][j + 1][k] - 2.0 * us[i][j][k] + us[i][j - 1][k]) - ty2 * (u[i][j + 1][k][1] * vp1 - u[i][j - 1][k][1] * vm1);
                rhs[i][j][k][2] = rhs[i][j][k][2] + dy3ty1 * (u[i][j + 1][k][2] - 2.0 * u[i][j][k][2] + u[i][j - 1][k][2]) + yycon2 * con43 * (vp1 - 2.0 * vijk + vm1) - ty2 * (u[i][j + 1][k][2] * vp1 - u[i][j - 1][k][2] * vm1 + (u[i][j + 1][k][4] - square[i][j + 1][k] - u[i][j - 1][k][4] + square[i][j - 1][k]) * c2);
                rhs[i][j][k][3] = rhs[i][j][k][3] + dy4ty1 * (u[i][j + 1][k][3] - 2.0 * u[i][j][k][3] + u[i][j - 1][k][3]) + yycon2 * (ws[i][j + 1][k] - 2.0 * ws[i][j][k] + ws[i][j - 1][k]) - ty2 * (u[i][j + 1][k][3] * vp1 - u[i][j - 1][k][3] * vm1);
                rhs[i][j][k][4] = rhs[i][j][k][4] + dy5ty1 * (u[i][j + 1][k][4] - 2.0 * u[i][j][k][4] + u[i][j - 1][k][4]) + yycon3 * (qs[i][j + 1][k] - 2.0 * qs[i][j][k] + qs[i][j - 1][k]) + yycon4 * (vp1 * vp1 - 2.0 * vijk * vijk + vm1 * vm1) + yycon5 * (u[i][j + 1][k][4] * rho_i[i][j + 1][k] - 2.0 * u[i][j][k][4] * rho_i[i][j][k] + u[i][j - 1][k][4] * rho_i[i][j - 1][k]) - ty2 * ((c1 * u[i][j + 1][k][4] - c2 * square[i][j + 1][k]) * vp1 - (c1 * u[i][j - 1][k][4] - c2 * square[i][j - 1][k]) * vm1);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    j = 1;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m = 0; m < 5; m++) {
                rhs[i][j][k][m] = rhs[i][j][k][m] - dssp * (5.0 * u[i][j][k][m] - 4.0 * u[i][j + 1][k][m] + u[i][j + 2][k][m]);
            }
        }
    }
    j = 2;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m = 0; m < 5; m++) {
                rhs[i][j][k][m] = rhs[i][j][k][m] - dssp * (-4.0 * u[i][j - 1][k][m] + 6.0 * u[i][j][k][m] - 4.0 * u[i][j + 1][k][m] + u[i][j + 2][k][m]);
            }
        }
    }
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 3; j < grid_points[1] - 3; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                for (m = 0; m < 5; m++) {
                    rhs[i][j][k][m] = rhs[i][j][k][m] - dssp * (u[i][j - 2][k][m] - 4.0 * u[i][j - 1][k][m] + 6.0 * u[i][j][k][m] - 4.0 * u[i][j + 1][k][m] + u[i][j + 2][k][m]);
                }
            }
        }
    }
    j = grid_points[1] - 3;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m = 0; m < 5; m++) {
                rhs[i][j][k][m] = rhs[i][j][k][m] - dssp * (u[i][j - 2][k][m] - 4.0 * u[i][j - 1][k][m] + 6.0 * u[i][j][k][m] - 4.0 * u[i][j + 1][k][m]);
            }
        }
    }
    j = grid_points[1] - 2;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m = 0; m < 5; m++) {
                rhs[i][j][k][m] = rhs[i][j][k][m] - dssp * (u[i][j - 2][k][m] - 4. * u[i][j - 1][k][m] + 5. * u[i][j][k][m]);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                wijk = ws[i][j][k];
                wp1 = ws[i][j][k + 1];
                wm1 = ws[i][j][k - 1];
                rhs[i][j][k][0] = rhs[i][j][k][0] + dz1tz1 * (u[i][j][k + 1][0] - 2.0 * u[i][j][k][0] + u[i][j][k - 1][0]) - tz2 * (u[i][j][k + 1][3] - u[i][j][k - 1][3]);
                rhs[i][j][k][1] = rhs[i][j][k][1] + dz2tz1 * (u[i][j][k + 1][1] - 2.0 * u[i][j][k][1] + u[i][j][k - 1][1]) + zzcon2 * (us[i][j][k + 1] - 2.0 * us[i][j][k] + us[i][j][k - 1]) - tz2 * (u[i][j][k + 1][1] * wp1 - u[i][j][k - 1][1] * wm1);
                rhs[i][j][k][2] = rhs[i][j][k][2] + dz3tz1 * (u[i][j][k + 1][2] - 2.0 * u[i][j][k][2] + u[i][j][k - 1][2]) + zzcon2 * (vs[i][j][k + 1] - 2.0 * vs[i][j][k] + vs[i][j][k - 1]) - tz2 * (u[i][j][k + 1][2] * wp1 - u[i][j][k - 1][2] * wm1);
                rhs[i][j][k][3] = rhs[i][j][k][3] + dz4tz1 * (u[i][j][k + 1][3] - 2.0 * u[i][j][k][3] + u[i][j][k - 1][3]) + zzcon2 * con43 * (wp1 - 2.0 * wijk + wm1) - tz2 * (u[i][j][k + 1][3] * wp1 - u[i][j][k - 1][3] * wm1 + (u[i][j][k + 1][4] - square[i][j][k + 1] - u[i][j][k - 1][4] + square[i][j][k - 1]) * c2);
                rhs[i][j][k][4] = rhs[i][j][k][4] + dz5tz1 * (u[i][j][k + 1][4] - 2.0 * u[i][j][k][4] + u[i][j][k - 1][4]) + zzcon3 * (qs[i][j][k + 1] - 2.0 * qs[i][j][k] + qs[i][j][k - 1]) + zzcon4 * (wp1 * wp1 - 2.0 * wijk * wijk + wm1 * wm1) + zzcon5 * (u[i][j][k + 1][4] * rho_i[i][j][k + 1] - 2.0 * u[i][j][k][4] * rho_i[i][j][k] + u[i][j][k - 1][4] * rho_i[i][j][k - 1]) - tz2 * ((c1 * u[i][j][k + 1][4] - c2 * square[i][j][k + 1]) * wp1 - (c1 * u[i][j][k - 1][4] - c2 * square[i][j][k - 1]) * wm1);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    k = 1;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (m = 0; m < 5; m++) {
                rhs[i][j][k][m] = rhs[i][j][k][m] - dssp * (5.0 * u[i][j][k][m] - 4.0 * u[i][j][k + 1][m] + u[i][j][k + 2][m]);
            }
        }
    }
    k = 2;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (m = 0; m < 5; m++) {
                rhs[i][j][k][m] = rhs[i][j][k][m] - dssp * (-4.0 * u[i][j][k - 1][m] + 6.0 * u[i][j][k][m] - 4.0 * u[i][j][k + 1][m] + u[i][j][k + 2][m]);
            }
        }
    }
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 3; k < grid_points[2] - 3; k++) {
                for (m = 0; m < 5; m++) {
                    rhs[i][j][k][m] = rhs[i][j][k][m] - dssp * (u[i][j][k - 2][m] - 4.0 * u[i][j][k - 1][m] + 6.0 * u[i][j][k][m] - 4.0 * u[i][j][k + 1][m] + u[i][j][k + 2][m]);
                }
            }
        }
    }
    k = grid_points[2] - 3;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (m = 0; m < 5; m++) {
                rhs[i][j][k][m] = rhs[i][j][k][m] - dssp * (u[i][j][k - 2][m] - 4.0 * u[i][j][k - 1][m] + 6.0 * u[i][j][k][m] - 4.0 * u[i][j][k + 1][m]);
            }
        }
    }
    k = grid_points[2] - 2;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (m = 0; m < 5; m++) {
                rhs[i][j][k][m] = rhs[i][j][k][m] - dssp * (u[i][j][k - 2][m] - 4.0 * u[i][j][k - 1][m] + 5.0 * u[i][j][k][m]);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (j = 1; j < grid_points[1] - 1; j++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m = 0; m < 5; m++) {
                for (i = 1; i < grid_points[0] - 1; i++) {
                    rhs[i][j][k][m] = rhs[i][j][k][m] * dt;
                }
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
}
static void set_constants(void ) {
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
    dnxm1 = 1.0 / (double) (grid_points[0] - 1);
    dnym1 = 1.0 / (double) (grid_points[1] - 1);
    dnzm1 = 1.0 / (double) (grid_points[2] - 1);
    c1c2 = c1 * c2;
    c1c5 = c1 * c5;
    c3c4 = c3 * c4;
    c1345 = c1c5 * c3c4;
    conz1 = (1.0 - c1c5);
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
    int _imopVarPre203;
    double _imopVarPre204;
    _imopVarPre203 = (dx3 > dx4);
    if (_imopVarPre203) {
        _imopVarPre204 = dx3;
    } else {
        _imopVarPre204 = dx4;
    }
    dxmax = _imopVarPre204;
    int _imopVarPre207;
    double _imopVarPre208;
    _imopVarPre207 = (dy2 > dy4);
    if (_imopVarPre207) {
        _imopVarPre208 = dy2;
    } else {
        _imopVarPre208 = dy4;
    }
    dymax = _imopVarPre208;
    int _imopVarPre211;
    double _imopVarPre212;
    _imopVarPre211 = (dz2 > dz3);
    if (_imopVarPre211) {
        _imopVarPre212 = dz2;
    } else {
        _imopVarPre212 = dz3;
    }
    dzmax = _imopVarPre212;
    int _imopVarPre253;
    double _imopVarPre254;
    int _imopVarPre255;
    double _imopVarPre256;
    int _imopVarPre263;
    double _imopVarPre264;
    _imopVarPre253 = (dy1 > dz1);
    if (_imopVarPre253) {
        _imopVarPre254 = dy1;
    } else {
        _imopVarPre254 = dz1;
    }
    _imopVarPre255 = (dx1 > _imopVarPre254);
    if (_imopVarPre255) {
        _imopVarPre256 = dx1;
    } else {
        _imopVarPre263 = (dy1 > dz1);
        if (_imopVarPre263) {
            _imopVarPre264 = dy1;
        } else {
            _imopVarPre264 = dz1;
        }
        _imopVarPre256 = _imopVarPre264;
    }
    dssp = 0.25 * _imopVarPre256;
    c4dssp = 4.0 * dssp;
    c5dssp = 5.0 * dssp;
    dttx1 = dt * tx1;
    dttx2 = dt * tx2;
    dtty1 = dt * ty1;
    dtty2 = dt * ty2;
    dttz1 = dt * tz1;
    dttz2 = dt * tz2;
    c2dttx1 = 2.0 * dttx1;
    c2dtty1 = 2.0 * dtty1;
    c2dttz1 = 2.0 * dttz1;
    dtdssp = dt * dssp;
    comz1 = dtdssp;
    comz4 = 4.0 * dtdssp;
    comz5 = 5.0 * dtdssp;
    comz6 = 6.0 * dtdssp;
    c3c4tx3 = c3c4 * tx3;
    c3c4ty3 = c3c4 * ty3;
    c3c4tz3 = c3c4 * tz3;
    dx1tx1 = dx1 * tx1;
    dx2tx1 = dx2 * tx1;
    dx3tx1 = dx3 * tx1;
    dx4tx1 = dx4 * tx1;
    dx5tx1 = dx5 * tx1;
    dy1ty1 = dy1 * ty1;
    dy2ty1 = dy2 * ty1;
    dy3ty1 = dy3 * ty1;
    dy4ty1 = dy4 * ty1;
    dy5ty1 = dy5 * ty1;
    dz1tz1 = dz1 * tz1;
    dz2tz1 = dz2 * tz1;
    dz3tz1 = dz3 * tz1;
    dz4tz1 = dz4 * tz1;
    dz5tz1 = dz5 * tz1;
    c2iv = 2.5;
    con43 = 4.0 / 3.0;
    con16 = 1.0 / 6.0;
    xxcon1 = c3c4tx3 * con43 * tx3;
    xxcon2 = c3c4tx3 * tx3;
    xxcon3 = c3c4tx3 * conz1 * tx3;
    xxcon4 = c3c4tx3 * con16 * tx3;
    xxcon5 = c3c4tx3 * c1c5 * tx3;
    yycon1 = c3c4ty3 * con43 * ty3;
    yycon2 = c3c4ty3 * ty3;
    yycon3 = c3c4ty3 * conz1 * ty3;
    yycon4 = c3c4ty3 * con16 * ty3;
    yycon5 = c3c4ty3 * c1c5 * ty3;
    zzcon1 = c3c4tz3 * con43 * tz3;
    zzcon2 = c3c4tz3 * tz3;
    zzcon3 = c3c4tz3 * conz1 * tz3;
    zzcon4 = c3c4tz3 * con16 * tz3;
    zzcon5 = c3c4tz3 * c1c5 * tz3;
}
static void verify(int no_time_steps, char *class , boolean *verified) {
    double xcrref[5];
    double xceref[5];
    double xcrdif[5];
    double xcedif[5];
    double epsilon;
    double xce[5];
    double xcr[5];
    double dtref;
    int m;
    epsilon = 1.0e-08;
    error_norm(xce);
    int i;
    int j;
    int k;
    int m_imopVar133;
    double rho_inv;
    double uijk;
    double up1;
    double um1;
    double vijk;
    double vp1;
    double vm1;
    double wijk;
    double wp1;
    double wm1;
#pragma omp for nowait
    for (i = 0; i < grid_points[0]; i++) {
        for (j = 0; j < grid_points[1]; j++) {
            for (k = 0; k < grid_points[2]; k++) {
                rho_inv = 1.0 / u[i][j][k][0];
                rho_i[i][j][k] = rho_inv;
                us[i][j][k] = u[i][j][k][1] * rho_inv;
                vs[i][j][k] = u[i][j][k][2] * rho_inv;
                ws[i][j][k] = u[i][j][k][3] * rho_inv;
                square[i][j][k] = 0.5 * (u[i][j][k][1] * u[i][j][k][1] + u[i][j][k][2] * u[i][j][k][2] + u[i][j][k][3] * u[i][j][k][3]) * rho_inv;
                qs[i][j][k] = square[i][j][k] * rho_inv;
            }
        }
    }
#pragma omp for nowait
    for (i = 0; i < grid_points[0]; i++) {
        for (j = 0; j < grid_points[1]; j++) {
            for (k = 0; k < grid_points[2]; k++) {
                for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                    rhs[i][j][k][m_imopVar133] = forcing[i][j][k][m_imopVar133];
                }
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                uijk = us[i][j][k];
                up1 = us[i + 1][j][k];
                um1 = us[i - 1][j][k];
                rhs[i][j][k][0] = rhs[i][j][k][0] + dx1tx1 * (u[i + 1][j][k][0] - 2.0 * u[i][j][k][0] + u[i - 1][j][k][0]) - tx2 * (u[i + 1][j][k][1] - u[i - 1][j][k][1]);
                rhs[i][j][k][1] = rhs[i][j][k][1] + dx2tx1 * (u[i + 1][j][k][1] - 2.0 * u[i][j][k][1] + u[i - 1][j][k][1]) + xxcon2 * con43 * (up1 - 2.0 * uijk + um1) - tx2 * (u[i + 1][j][k][1] * up1 - u[i - 1][j][k][1] * um1 + (u[i + 1][j][k][4] - square[i + 1][j][k] - u[i - 1][j][k][4] + square[i - 1][j][k]) * c2);
                rhs[i][j][k][2] = rhs[i][j][k][2] + dx3tx1 * (u[i + 1][j][k][2] - 2.0 * u[i][j][k][2] + u[i - 1][j][k][2]) + xxcon2 * (vs[i + 1][j][k] - 2.0 * vs[i][j][k] + vs[i - 1][j][k]) - tx2 * (u[i + 1][j][k][2] * up1 - u[i - 1][j][k][2] * um1);
                rhs[i][j][k][3] = rhs[i][j][k][3] + dx4tx1 * (u[i + 1][j][k][3] - 2.0 * u[i][j][k][3] + u[i - 1][j][k][3]) + xxcon2 * (ws[i + 1][j][k] - 2.0 * ws[i][j][k] + ws[i - 1][j][k]) - tx2 * (u[i + 1][j][k][3] * up1 - u[i - 1][j][k][3] * um1);
                rhs[i][j][k][4] = rhs[i][j][k][4] + dx5tx1 * (u[i + 1][j][k][4] - 2.0 * u[i][j][k][4] + u[i - 1][j][k][4]) + xxcon3 * (qs[i + 1][j][k] - 2.0 * qs[i][j][k] + qs[i - 1][j][k]) + xxcon4 * (up1 * up1 - 2.0 * uijk * uijk + um1 * um1) + xxcon5 * (u[i + 1][j][k][4] * rho_i[i + 1][j][k] - 2.0 * u[i][j][k][4] * rho_i[i][j][k] + u[i - 1][j][k][4] * rho_i[i - 1][j][k]) - tx2 * ((c1 * u[i + 1][j][k][4] - c2 * square[i + 1][j][k]) * up1 - (c1 * u[i - 1][j][k][4] - c2 * square[i - 1][j][k]) * um1);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    i = 1;
#pragma omp for nowait
    for (j = 1; j < grid_points[1] - 1; j++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (5.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i + 1][j][k][m_imopVar133] + u[i + 2][j][k][m_imopVar133]);
            }
        }
    }
    i = 2;
#pragma omp for nowait
    for (j = 1; j < grid_points[1] - 1; j++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (-4.0 * u[i - 1][j][k][m_imopVar133] + 6.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i + 1][j][k][m_imopVar133] + u[i + 2][j][k][m_imopVar133]);
            }
        }
    }
#pragma omp for nowait
    for (i = 3; i < grid_points[0] - 3; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                    rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (u[i - 2][j][k][m_imopVar133] - 4.0 * u[i - 1][j][k][m_imopVar133] + 6.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i + 1][j][k][m_imopVar133] + u[i + 2][j][k][m_imopVar133]);
                }
            }
        }
    }
    i = grid_points[0] - 3;
#pragma omp for nowait
    for (j = 1; j < grid_points[1] - 1; j++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (u[i - 2][j][k][m_imopVar133] - 4.0 * u[i - 1][j][k][m_imopVar133] + 6.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i + 1][j][k][m_imopVar133]);
            }
        }
    }
    i = grid_points[0] - 2;
#pragma omp for nowait
    for (j = 1; j < grid_points[1] - 1; j++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (u[i - 2][j][k][m_imopVar133] - 4. * u[i - 1][j][k][m_imopVar133] + 5.0 * u[i][j][k][m_imopVar133]);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                vijk = vs[i][j][k];
                vp1 = vs[i][j + 1][k];
                vm1 = vs[i][j - 1][k];
                rhs[i][j][k][0] = rhs[i][j][k][0] + dy1ty1 * (u[i][j + 1][k][0] - 2.0 * u[i][j][k][0] + u[i][j - 1][k][0]) - ty2 * (u[i][j + 1][k][2] - u[i][j - 1][k][2]);
                rhs[i][j][k][1] = rhs[i][j][k][1] + dy2ty1 * (u[i][j + 1][k][1] - 2.0 * u[i][j][k][1] + u[i][j - 1][k][1]) + yycon2 * (us[i][j + 1][k] - 2.0 * us[i][j][k] + us[i][j - 1][k]) - ty2 * (u[i][j + 1][k][1] * vp1 - u[i][j - 1][k][1] * vm1);
                rhs[i][j][k][2] = rhs[i][j][k][2] + dy3ty1 * (u[i][j + 1][k][2] - 2.0 * u[i][j][k][2] + u[i][j - 1][k][2]) + yycon2 * con43 * (vp1 - 2.0 * vijk + vm1) - ty2 * (u[i][j + 1][k][2] * vp1 - u[i][j - 1][k][2] * vm1 + (u[i][j + 1][k][4] - square[i][j + 1][k] - u[i][j - 1][k][4] + square[i][j - 1][k]) * c2);
                rhs[i][j][k][3] = rhs[i][j][k][3] + dy4ty1 * (u[i][j + 1][k][3] - 2.0 * u[i][j][k][3] + u[i][j - 1][k][3]) + yycon2 * (ws[i][j + 1][k] - 2.0 * ws[i][j][k] + ws[i][j - 1][k]) - ty2 * (u[i][j + 1][k][3] * vp1 - u[i][j - 1][k][3] * vm1);
                rhs[i][j][k][4] = rhs[i][j][k][4] + dy5ty1 * (u[i][j + 1][k][4] - 2.0 * u[i][j][k][4] + u[i][j - 1][k][4]) + yycon3 * (qs[i][j + 1][k] - 2.0 * qs[i][j][k] + qs[i][j - 1][k]) + yycon4 * (vp1 * vp1 - 2.0 * vijk * vijk + vm1 * vm1) + yycon5 * (u[i][j + 1][k][4] * rho_i[i][j + 1][k] - 2.0 * u[i][j][k][4] * rho_i[i][j][k] + u[i][j - 1][k][4] * rho_i[i][j - 1][k]) - ty2 * ((c1 * u[i][j + 1][k][4] - c2 * square[i][j + 1][k]) * vp1 - (c1 * u[i][j - 1][k][4] - c2 * square[i][j - 1][k]) * vm1);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    j = 1;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (5.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i][j + 1][k][m_imopVar133] + u[i][j + 2][k][m_imopVar133]);
            }
        }
    }
    j = 2;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (-4.0 * u[i][j - 1][k][m_imopVar133] + 6.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i][j + 1][k][m_imopVar133] + u[i][j + 2][k][m_imopVar133]);
            }
        }
    }
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 3; j < grid_points[1] - 3; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                    rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (u[i][j - 2][k][m_imopVar133] - 4.0 * u[i][j - 1][k][m_imopVar133] + 6.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i][j + 1][k][m_imopVar133] + u[i][j + 2][k][m_imopVar133]);
                }
            }
        }
    }
    j = grid_points[1] - 3;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (u[i][j - 2][k][m_imopVar133] - 4.0 * u[i][j - 1][k][m_imopVar133] + 6.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i][j + 1][k][m_imopVar133]);
            }
        }
    }
    j = grid_points[1] - 2;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (u[i][j - 2][k][m_imopVar133] - 4. * u[i][j - 1][k][m_imopVar133] + 5. * u[i][j][k][m_imopVar133]);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                wijk = ws[i][j][k];
                wp1 = ws[i][j][k + 1];
                wm1 = ws[i][j][k - 1];
                rhs[i][j][k][0] = rhs[i][j][k][0] + dz1tz1 * (u[i][j][k + 1][0] - 2.0 * u[i][j][k][0] + u[i][j][k - 1][0]) - tz2 * (u[i][j][k + 1][3] - u[i][j][k - 1][3]);
                rhs[i][j][k][1] = rhs[i][j][k][1] + dz2tz1 * (u[i][j][k + 1][1] - 2.0 * u[i][j][k][1] + u[i][j][k - 1][1]) + zzcon2 * (us[i][j][k + 1] - 2.0 * us[i][j][k] + us[i][j][k - 1]) - tz2 * (u[i][j][k + 1][1] * wp1 - u[i][j][k - 1][1] * wm1);
                rhs[i][j][k][2] = rhs[i][j][k][2] + dz3tz1 * (u[i][j][k + 1][2] - 2.0 * u[i][j][k][2] + u[i][j][k - 1][2]) + zzcon2 * (vs[i][j][k + 1] - 2.0 * vs[i][j][k] + vs[i][j][k - 1]) - tz2 * (u[i][j][k + 1][2] * wp1 - u[i][j][k - 1][2] * wm1);
                rhs[i][j][k][3] = rhs[i][j][k][3] + dz4tz1 * (u[i][j][k + 1][3] - 2.0 * u[i][j][k][3] + u[i][j][k - 1][3]) + zzcon2 * con43 * (wp1 - 2.0 * wijk + wm1) - tz2 * (u[i][j][k + 1][3] * wp1 - u[i][j][k - 1][3] * wm1 + (u[i][j][k + 1][4] - square[i][j][k + 1] - u[i][j][k - 1][4] + square[i][j][k - 1]) * c2);
                rhs[i][j][k][4] = rhs[i][j][k][4] + dz5tz1 * (u[i][j][k + 1][4] - 2.0 * u[i][j][k][4] + u[i][j][k - 1][4]) + zzcon3 * (qs[i][j][k + 1] - 2.0 * qs[i][j][k] + qs[i][j][k - 1]) + zzcon4 * (wp1 * wp1 - 2.0 * wijk * wijk + wm1 * wm1) + zzcon5 * (u[i][j][k + 1][4] * rho_i[i][j][k + 1] - 2.0 * u[i][j][k][4] * rho_i[i][j][k] + u[i][j][k - 1][4] * rho_i[i][j][k - 1]) - tz2 * ((c1 * u[i][j][k + 1][4] - c2 * square[i][j][k + 1]) * wp1 - (c1 * u[i][j][k - 1][4] - c2 * square[i][j][k - 1]) * wm1);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    k = 1;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (5.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i][j][k + 1][m_imopVar133] + u[i][j][k + 2][m_imopVar133]);
            }
        }
    }
    k = 2;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (-4.0 * u[i][j][k - 1][m_imopVar133] + 6.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i][j][k + 1][m_imopVar133] + u[i][j][k + 2][m_imopVar133]);
            }
        }
    }
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 3; k < grid_points[2] - 3; k++) {
                for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                    rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (u[i][j][k - 2][m_imopVar133] - 4.0 * u[i][j][k - 1][m_imopVar133] + 6.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i][j][k + 1][m_imopVar133] + u[i][j][k + 2][m_imopVar133]);
                }
            }
        }
    }
    k = grid_points[2] - 3;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (u[i][j][k - 2][m_imopVar133] - 4.0 * u[i][j][k - 1][m_imopVar133] + 6.0 * u[i][j][k][m_imopVar133] - 4.0 * u[i][j][k + 1][m_imopVar133]);
            }
        }
    }
    k = grid_points[2] - 2;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] - dssp * (u[i][j][k - 2][m_imopVar133] - 4.0 * u[i][j][k - 1][m_imopVar133] + 5.0 * u[i][j][k][m_imopVar133]);
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (j = 1; j < grid_points[1] - 1; j++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (m_imopVar133 = 0; m_imopVar133 < 5; m_imopVar133++) {
                for (i = 1; i < grid_points[0] - 1; i++) {
                    rhs[i][j][k][m_imopVar133] = rhs[i][j][k][m_imopVar133] * dt;
                }
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
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
    int _imopVarPre268;
    int _imopVarPre269;
    int _imopVarPre270;
    _imopVarPre268 = grid_points[0] == 12;
    if (_imopVarPre268) {
        _imopVarPre269 = grid_points[1] == 12;
        if (_imopVarPre269) {
            _imopVarPre270 = grid_points[2] == 12;
            if (_imopVarPre270) {
                _imopVarPre270 = no_time_steps == 60;
            }
            _imopVarPre269 = _imopVarPre270;
        }
        _imopVarPre268 = _imopVarPre269;
    }
    if (_imopVarPre268) {
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
    } else {
        int _imopVarPre274;
        int _imopVarPre275;
        int _imopVarPre276;
        _imopVarPre274 = grid_points[0] == 24;
        if (_imopVarPre274) {
            _imopVarPre275 = grid_points[1] == 24;
            if (_imopVarPre275) {
                _imopVarPre276 = grid_points[2] == 24;
                if (_imopVarPre276) {
                    _imopVarPre276 = no_time_steps == 200;
                }
                _imopVarPre275 = _imopVarPre276;
            }
            _imopVarPre274 = _imopVarPre275;
        }
        if (_imopVarPre274) {
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
        } else {
            int _imopVarPre280;
            int _imopVarPre281;
            int _imopVarPre282;
            _imopVarPre280 = grid_points[0] == 64;
            if (_imopVarPre280) {
                _imopVarPre281 = grid_points[1] == 64;
                if (_imopVarPre281) {
                    _imopVarPre282 = grid_points[2] == 64;
                    if (_imopVarPre282) {
                        _imopVarPre282 = no_time_steps == 200;
                    }
                    _imopVarPre281 = _imopVarPre282;
                }
                _imopVarPre280 = _imopVarPre281;
            }
            if (_imopVarPre280) {
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
            } else {
                int _imopVarPre286;
                int _imopVarPre287;
                int _imopVarPre288;
                _imopVarPre286 = grid_points[0] == 102;
                if (_imopVarPre286) {
                    _imopVarPre287 = grid_points[1] == 102;
                    if (_imopVarPre287) {
                        _imopVarPre288 = grid_points[2] == 102;
                        if (_imopVarPre288) {
                            _imopVarPre288 = no_time_steps == 200;
                        }
                        _imopVarPre287 = _imopVarPre288;
                    }
                    _imopVarPre286 = _imopVarPre287;
                }
                if (_imopVarPre286) {
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
                } else {
                    int _imopVarPre292;
                    int _imopVarPre293;
                    int _imopVarPre294;
                    _imopVarPre292 = grid_points[0] == 162;
                    if (_imopVarPre292) {
                        _imopVarPre293 = grid_points[1] == 162;
                        if (_imopVarPre293) {
                            _imopVarPre294 = grid_points[2] == 162;
                            if (_imopVarPre294) {
                                _imopVarPre294 = no_time_steps == 200;
                            }
                            _imopVarPre293 = _imopVarPre294;
                        }
                        _imopVarPre292 = _imopVarPre293;
                    }
                    if (_imopVarPre292) {
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
                }
            }
        }
    }
    for (m = 0; m < 5; m++) {
        double _imopVarPre296;
        double _imopVarPre297;
        _imopVarPre296 = (xcr[m] - xcrref[m]) / xcrref[m];
        _imopVarPre297 = fabs(_imopVarPre296);
        xcrdif[m] = _imopVarPre297;
        double _imopVarPre299;
        double _imopVarPre300;
        _imopVarPre299 = (xce[m] - xceref[m]) / xceref[m];
        _imopVarPre300 = fabs(_imopVarPre299);
        xcedif[m] = _imopVarPre300;
    }
    if (*class != 'U') {
        char _imopVarPre302;
        _imopVarPre302 = *class;
        printf(" Verification being performed for class %1c\n", _imopVarPre302);
        printf(" accuracy setting for epsilon = %20.13e\n", epsilon);
        double _imopVarPre305;
        double _imopVarPre306;
        _imopVarPre305 = dt - dtref;
        _imopVarPre306 = fabs(_imopVarPre305);
        if (_imopVarPre306 > epsilon) {
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
            double _imopVarPre308;
            _imopVarPre308 = xcr[m];
            printf("          %2d%20.13e\n", m, _imopVarPre308);
        } else {
            if (xcrdif[m] > epsilon) {
                *verified = 0;
                double _imopVarPre312;
                double _imopVarPre313;
                double _imopVarPre314;
                _imopVarPre312 = xcrdif[m];
                _imopVarPre313 = xcrref[m];
                _imopVarPre314 = xcr[m];
                printf(" FAILURE: %2d%20.13e%20.13e%20.13e\n", m, _imopVarPre314, _imopVarPre313, _imopVarPre312);
            } else {
                double _imopVarPre318;
                double _imopVarPre319;
                double _imopVarPre320;
                _imopVarPre318 = xcrdif[m];
                _imopVarPre319 = xcrref[m];
                _imopVarPre320 = xcr[m];
                printf("          %2d%20.13e%20.13e%20.13e\n", m, _imopVarPre320, _imopVarPre319, _imopVarPre318);
            }
        }
    }
    if (*class != 'U') {
        printf(" Comparison of RMS-norms of solution error\n");
    } else {
        printf(" RMS-norms of solution error\n");
    }
    for (m = 0; m < 5; m++) {
        if (*class == 'U') {
            double _imopVarPre322;
            _imopVarPre322 = xce[m];
            printf("          %2d%20.13e\n", m, _imopVarPre322);
        } else {
            if (xcedif[m] > epsilon) {
                *verified = 0;
                double _imopVarPre326;
                double _imopVarPre327;
                double _imopVarPre328;
                _imopVarPre326 = xcedif[m];
                _imopVarPre327 = xceref[m];
                _imopVarPre328 = xce[m];
                printf(" FAILURE: %2d%20.13e%20.13e%20.13e\n", m, _imopVarPre328, _imopVarPre327, _imopVarPre326);
            } else {
                double _imopVarPre332;
                double _imopVarPre333;
                double _imopVarPre334;
                _imopVarPre332 = xcedif[m];
                _imopVarPre333 = xceref[m];
                _imopVarPre334 = xce[m];
                printf("          %2d%20.13e%20.13e%20.13e\n", m, _imopVarPre334, _imopVarPre333, _imopVarPre332);
            }
        }
    }
    if (*class == 'U') {
        printf(" No reference values provided\n");
        printf(" No verification performed\n");
    } else {
        if (*verified == 1) {
            printf(" Verification Successful\n");
        } else {
            printf(" Verification failed\n");
        }
    }
}
static void x_solve(void ) {
    int i;
    int j;
    int k;
#pragma omp for nowait
    for (j = 1; j < grid_points[1] - 1; j++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            for (i = 0; i < grid_points[0]; i++) {
                tmp1 = 1.0 / u[i][j][k][0];
                tmp2 = tmp1 * tmp1;
                tmp3 = tmp1 * tmp2;
                fjac[i][j][k][0][0] = 0.0;
                fjac[i][j][k][0][1] = 1.0;
                fjac[i][j][k][0][2] = 0.0;
                fjac[i][j][k][0][3] = 0.0;
                fjac[i][j][k][0][4] = 0.0;
                fjac[i][j][k][1][0] = -(u[i][j][k][1] * tmp2 * u[i][j][k][1]) + c2 * 0.50 * (u[i][j][k][1] * u[i][j][k][1] + u[i][j][k][2] * u[i][j][k][2] + u[i][j][k][3] * u[i][j][k][3]) * tmp2;
                fjac[i][j][k][1][1] = (2.0 - c2) * (u[i][j][k][1] / u[i][j][k][0]);
                fjac[i][j][k][1][2] = -c2 * (u[i][j][k][2] * tmp1);
                fjac[i][j][k][1][3] = -c2 * (u[i][j][k][3] * tmp1);
                fjac[i][j][k][1][4] = c2;
                fjac[i][j][k][2][0] = -(u[i][j][k][1] * u[i][j][k][2]) * tmp2;
                fjac[i][j][k][2][1] = u[i][j][k][2] * tmp1;
                fjac[i][j][k][2][2] = u[i][j][k][1] * tmp1;
                fjac[i][j][k][2][3] = 0.0;
                fjac[i][j][k][2][4] = 0.0;
                fjac[i][j][k][3][0] = -(u[i][j][k][1] * u[i][j][k][3]) * tmp2;
                fjac[i][j][k][3][1] = u[i][j][k][3] * tmp1;
                fjac[i][j][k][3][2] = 0.0;
                fjac[i][j][k][3][3] = u[i][j][k][1] * tmp1;
                fjac[i][j][k][3][4] = 0.0;
                fjac[i][j][k][4][0] = (c2 * (u[i][j][k][1] * u[i][j][k][1] + u[i][j][k][2] * u[i][j][k][2] + u[i][j][k][3] * u[i][j][k][3]) * tmp2 - c1 * (u[i][j][k][4] * tmp1)) * (u[i][j][k][1] * tmp1);
                fjac[i][j][k][4][1] = c1 * u[i][j][k][4] * tmp1 - 0.50 * c2 * (3.0 * u[i][j][k][1] * u[i][j][k][1] + u[i][j][k][2] * u[i][j][k][2] + u[i][j][k][3] * u[i][j][k][3]) * tmp2;
                fjac[i][j][k][4][2] = -c2 * (u[i][j][k][2] * u[i][j][k][1]) * tmp2;
                fjac[i][j][k][4][3] = -c2 * (u[i][j][k][3] * u[i][j][k][1]) * tmp2;
                fjac[i][j][k][4][4] = c1 * (u[i][j][k][1] * tmp1);
                njac[i][j][k][0][0] = 0.0;
                njac[i][j][k][0][1] = 0.0;
                njac[i][j][k][0][2] = 0.0;
                njac[i][j][k][0][3] = 0.0;
                njac[i][j][k][0][4] = 0.0;
                njac[i][j][k][1][0] = -con43 * c3c4 * tmp2 * u[i][j][k][1];
                njac[i][j][k][1][1] = con43 * c3c4 * tmp1;
                njac[i][j][k][1][2] = 0.0;
                njac[i][j][k][1][3] = 0.0;
                njac[i][j][k][1][4] = 0.0;
                njac[i][j][k][2][0] = -c3c4 * tmp2 * u[i][j][k][2];
                njac[i][j][k][2][1] = 0.0;
                njac[i][j][k][2][2] = c3c4 * tmp1;
                njac[i][j][k][2][3] = 0.0;
                njac[i][j][k][2][4] = 0.0;
                njac[i][j][k][3][0] = -c3c4 * tmp2 * u[i][j][k][3];
                njac[i][j][k][3][1] = 0.0;
                njac[i][j][k][3][2] = 0.0;
                njac[i][j][k][3][3] = c3c4 * tmp1;
                njac[i][j][k][3][4] = 0.0;
                njac[i][j][k][4][0] = -(con43 * c3c4 - c1345) * tmp3 * (((u[i][j][k][1]) * (u[i][j][k][1]))) - (c3c4 - c1345) * tmp3 * (((u[i][j][k][2]) * (u[i][j][k][2]))) - (c3c4 - c1345) * tmp3 * (((u[i][j][k][3]) * (u[i][j][k][3]))) - c1345 * tmp2 * u[i][j][k][4];
                njac[i][j][k][4][1] = (con43 * c3c4 - c1345) * tmp2 * u[i][j][k][1];
                njac[i][j][k][4][2] = (c3c4 - c1345) * tmp2 * u[i][j][k][2];
                njac[i][j][k][4][3] = (c3c4 - c1345) * tmp2 * u[i][j][k][3];
                njac[i][j][k][4][4] = c1345 * tmp1;
            }
            for (i = 1; i < grid_points[0] - 1; i++) {
                tmp1 = dt * tx1;
                tmp2 = dt * tx2;
                lhs[i][j][k][0][0][0] = -tmp2 * fjac[i - 1][j][k][0][0] - tmp1 * njac[i - 1][j][k][0][0] - tmp1 * dx1;
                lhs[i][j][k][0][0][1] = -tmp2 * fjac[i - 1][j][k][0][1] - tmp1 * njac[i - 1][j][k][0][1];
                lhs[i][j][k][0][0][2] = -tmp2 * fjac[i - 1][j][k][0][2] - tmp1 * njac[i - 1][j][k][0][2];
                lhs[i][j][k][0][0][3] = -tmp2 * fjac[i - 1][j][k][0][3] - tmp1 * njac[i - 1][j][k][0][3];
                lhs[i][j][k][0][0][4] = -tmp2 * fjac[i - 1][j][k][0][4] - tmp1 * njac[i - 1][j][k][0][4];
                lhs[i][j][k][0][1][0] = -tmp2 * fjac[i - 1][j][k][1][0] - tmp1 * njac[i - 1][j][k][1][0];
                lhs[i][j][k][0][1][1] = -tmp2 * fjac[i - 1][j][k][1][1] - tmp1 * njac[i - 1][j][k][1][1] - tmp1 * dx2;
                lhs[i][j][k][0][1][2] = -tmp2 * fjac[i - 1][j][k][1][2] - tmp1 * njac[i - 1][j][k][1][2];
                lhs[i][j][k][0][1][3] = -tmp2 * fjac[i - 1][j][k][1][3] - tmp1 * njac[i - 1][j][k][1][3];
                lhs[i][j][k][0][1][4] = -tmp2 * fjac[i - 1][j][k][1][4] - tmp1 * njac[i - 1][j][k][1][4];
                lhs[i][j][k][0][2][0] = -tmp2 * fjac[i - 1][j][k][2][0] - tmp1 * njac[i - 1][j][k][2][0];
                lhs[i][j][k][0][2][1] = -tmp2 * fjac[i - 1][j][k][2][1] - tmp1 * njac[i - 1][j][k][2][1];
                lhs[i][j][k][0][2][2] = -tmp2 * fjac[i - 1][j][k][2][2] - tmp1 * njac[i - 1][j][k][2][2] - tmp1 * dx3;
                lhs[i][j][k][0][2][3] = -tmp2 * fjac[i - 1][j][k][2][3] - tmp1 * njac[i - 1][j][k][2][3];
                lhs[i][j][k][0][2][4] = -tmp2 * fjac[i - 1][j][k][2][4] - tmp1 * njac[i - 1][j][k][2][4];
                lhs[i][j][k][0][3][0] = -tmp2 * fjac[i - 1][j][k][3][0] - tmp1 * njac[i - 1][j][k][3][0];
                lhs[i][j][k][0][3][1] = -tmp2 * fjac[i - 1][j][k][3][1] - tmp1 * njac[i - 1][j][k][3][1];
                lhs[i][j][k][0][3][2] = -tmp2 * fjac[i - 1][j][k][3][2] - tmp1 * njac[i - 1][j][k][3][2];
                lhs[i][j][k][0][3][3] = -tmp2 * fjac[i - 1][j][k][3][3] - tmp1 * njac[i - 1][j][k][3][3] - tmp1 * dx4;
                lhs[i][j][k][0][3][4] = -tmp2 * fjac[i - 1][j][k][3][4] - tmp1 * njac[i - 1][j][k][3][4];
                lhs[i][j][k][0][4][0] = -tmp2 * fjac[i - 1][j][k][4][0] - tmp1 * njac[i - 1][j][k][4][0];
                lhs[i][j][k][0][4][1] = -tmp2 * fjac[i - 1][j][k][4][1] - tmp1 * njac[i - 1][j][k][4][1];
                lhs[i][j][k][0][4][2] = -tmp2 * fjac[i - 1][j][k][4][2] - tmp1 * njac[i - 1][j][k][4][2];
                lhs[i][j][k][0][4][3] = -tmp2 * fjac[i - 1][j][k][4][3] - tmp1 * njac[i - 1][j][k][4][3];
                lhs[i][j][k][0][4][4] = -tmp2 * fjac[i - 1][j][k][4][4] - tmp1 * njac[i - 1][j][k][4][4] - tmp1 * dx5;
                lhs[i][j][k][1][0][0] = 1.0 + tmp1 * 2.0 * njac[i][j][k][0][0] + tmp1 * 2.0 * dx1;
                lhs[i][j][k][1][0][1] = tmp1 * 2.0 * njac[i][j][k][0][1];
                lhs[i][j][k][1][0][2] = tmp1 * 2.0 * njac[i][j][k][0][2];
                lhs[i][j][k][1][0][3] = tmp1 * 2.0 * njac[i][j][k][0][3];
                lhs[i][j][k][1][0][4] = tmp1 * 2.0 * njac[i][j][k][0][4];
                lhs[i][j][k][1][1][0] = tmp1 * 2.0 * njac[i][j][k][1][0];
                lhs[i][j][k][1][1][1] = 1.0 + tmp1 * 2.0 * njac[i][j][k][1][1] + tmp1 * 2.0 * dx2;
                lhs[i][j][k][1][1][2] = tmp1 * 2.0 * njac[i][j][k][1][2];
                lhs[i][j][k][1][1][3] = tmp1 * 2.0 * njac[i][j][k][1][3];
                lhs[i][j][k][1][1][4] = tmp1 * 2.0 * njac[i][j][k][1][4];
                lhs[i][j][k][1][2][0] = tmp1 * 2.0 * njac[i][j][k][2][0];
                lhs[i][j][k][1][2][1] = tmp1 * 2.0 * njac[i][j][k][2][1];
                lhs[i][j][k][1][2][2] = 1.0 + tmp1 * 2.0 * njac[i][j][k][2][2] + tmp1 * 2.0 * dx3;
                lhs[i][j][k][1][2][3] = tmp1 * 2.0 * njac[i][j][k][2][3];
                lhs[i][j][k][1][2][4] = tmp1 * 2.0 * njac[i][j][k][2][4];
                lhs[i][j][k][1][3][0] = tmp1 * 2.0 * njac[i][j][k][3][0];
                lhs[i][j][k][1][3][1] = tmp1 * 2.0 * njac[i][j][k][3][1];
                lhs[i][j][k][1][3][2] = tmp1 * 2.0 * njac[i][j][k][3][2];
                lhs[i][j][k][1][3][3] = 1.0 + tmp1 * 2.0 * njac[i][j][k][3][3] + tmp1 * 2.0 * dx4;
                lhs[i][j][k][1][3][4] = tmp1 * 2.0 * njac[i][j][k][3][4];
                lhs[i][j][k][1][4][0] = tmp1 * 2.0 * njac[i][j][k][4][0];
                lhs[i][j][k][1][4][1] = tmp1 * 2.0 * njac[i][j][k][4][1];
                lhs[i][j][k][1][4][2] = tmp1 * 2.0 * njac[i][j][k][4][2];
                lhs[i][j][k][1][4][3] = tmp1 * 2.0 * njac[i][j][k][4][3];
                lhs[i][j][k][1][4][4] = 1.0 + tmp1 * 2.0 * njac[i][j][k][4][4] + tmp1 * 2.0 * dx5;
                lhs[i][j][k][2][0][0] = tmp2 * fjac[i + 1][j][k][0][0] - tmp1 * njac[i + 1][j][k][0][0] - tmp1 * dx1;
                lhs[i][j][k][2][0][1] = tmp2 * fjac[i + 1][j][k][0][1] - tmp1 * njac[i + 1][j][k][0][1];
                lhs[i][j][k][2][0][2] = tmp2 * fjac[i + 1][j][k][0][2] - tmp1 * njac[i + 1][j][k][0][2];
                lhs[i][j][k][2][0][3] = tmp2 * fjac[i + 1][j][k][0][3] - tmp1 * njac[i + 1][j][k][0][3];
                lhs[i][j][k][2][0][4] = tmp2 * fjac[i + 1][j][k][0][4] - tmp1 * njac[i + 1][j][k][0][4];
                lhs[i][j][k][2][1][0] = tmp2 * fjac[i + 1][j][k][1][0] - tmp1 * njac[i + 1][j][k][1][0];
                lhs[i][j][k][2][1][1] = tmp2 * fjac[i + 1][j][k][1][1] - tmp1 * njac[i + 1][j][k][1][1] - tmp1 * dx2;
                lhs[i][j][k][2][1][2] = tmp2 * fjac[i + 1][j][k][1][2] - tmp1 * njac[i + 1][j][k][1][2];
                lhs[i][j][k][2][1][3] = tmp2 * fjac[i + 1][j][k][1][3] - tmp1 * njac[i + 1][j][k][1][3];
                lhs[i][j][k][2][1][4] = tmp2 * fjac[i + 1][j][k][1][4] - tmp1 * njac[i + 1][j][k][1][4];
                lhs[i][j][k][2][2][0] = tmp2 * fjac[i + 1][j][k][2][0] - tmp1 * njac[i + 1][j][k][2][0];
                lhs[i][j][k][2][2][1] = tmp2 * fjac[i + 1][j][k][2][1] - tmp1 * njac[i + 1][j][k][2][1];
                lhs[i][j][k][2][2][2] = tmp2 * fjac[i + 1][j][k][2][2] - tmp1 * njac[i + 1][j][k][2][2] - tmp1 * dx3;
                lhs[i][j][k][2][2][3] = tmp2 * fjac[i + 1][j][k][2][3] - tmp1 * njac[i + 1][j][k][2][3];
                lhs[i][j][k][2][2][4] = tmp2 * fjac[i + 1][j][k][2][4] - tmp1 * njac[i + 1][j][k][2][4];
                lhs[i][j][k][2][3][0] = tmp2 * fjac[i + 1][j][k][3][0] - tmp1 * njac[i + 1][j][k][3][0];
                lhs[i][j][k][2][3][1] = tmp2 * fjac[i + 1][j][k][3][1] - tmp1 * njac[i + 1][j][k][3][1];
                lhs[i][j][k][2][3][2] = tmp2 * fjac[i + 1][j][k][3][2] - tmp1 * njac[i + 1][j][k][3][2];
                lhs[i][j][k][2][3][3] = tmp2 * fjac[i + 1][j][k][3][3] - tmp1 * njac[i + 1][j][k][3][3] - tmp1 * dx4;
                lhs[i][j][k][2][3][4] = tmp2 * fjac[i + 1][j][k][3][4] - tmp1 * njac[i + 1][j][k][3][4];
                lhs[i][j][k][2][4][0] = tmp2 * fjac[i + 1][j][k][4][0] - tmp1 * njac[i + 1][j][k][4][0];
                lhs[i][j][k][2][4][1] = tmp2 * fjac[i + 1][j][k][4][1] - tmp1 * njac[i + 1][j][k][4][1];
                lhs[i][j][k][2][4][2] = tmp2 * fjac[i + 1][j][k][4][2] - tmp1 * njac[i + 1][j][k][4][2];
                lhs[i][j][k][2][4][3] = tmp2 * fjac[i + 1][j][k][4][3] - tmp1 * njac[i + 1][j][k][4][3];
                lhs[i][j][k][2][4][4] = tmp2 * fjac[i + 1][j][k][4][4] - tmp1 * njac[i + 1][j][k][4][4] - tmp1 * dx5;
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    int i_imopVar114;
    int j_imopVar115;
    int k_imopVar116;
    int isize;
    isize = grid_points[0] - 1;
#pragma omp for nowait
    for (j_imopVar115 = 1; j_imopVar115 < grid_points[1] - 1; j_imopVar115++) {
        for (k_imopVar116 = 1; k_imopVar116 < grid_points[2] - 1; k_imopVar116++) {
            double ( *_imopVarPre338 );
            double ( *_imopVarPre339 )[5];
            double ( *_imopVarPre340 )[5];
            _imopVarPre338 = rhs[0][j_imopVar115][k_imopVar116];
            _imopVarPre339 = lhs[0][j_imopVar115][k_imopVar116][2];
            _imopVarPre340 = lhs[0][j_imopVar115][k_imopVar116][1];
            binvcrhs(_imopVarPre340, _imopVarPre339, _imopVarPre338);
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    for (i_imopVar114 = 1; i_imopVar114 < isize; i_imopVar114++) {
#pragma omp for nowait
        for (j_imopVar115 = 1; j_imopVar115 < grid_points[1] - 1; j_imopVar115++) {
            for (k_imopVar116 = 1; k_imopVar116 < grid_points[2] - 1; k_imopVar116++) {
                double ( *_imopVarPre344 );
                double ( *_imopVarPre345 );
                double ( *_imopVarPre346 )[5];
                _imopVarPre344 = rhs[i_imopVar114][j_imopVar115][k_imopVar116];
                _imopVarPre345 = rhs[i_imopVar114 - 1][j_imopVar115][k_imopVar116];
                _imopVarPre346 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][0];
                matvec_sub(_imopVarPre346, _imopVarPre345, _imopVarPre344);
                double ( *_imopVarPre350 )[5];
                double ( *_imopVarPre351 )[5];
                double ( *_imopVarPre352 )[5];
                _imopVarPre350 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][1];
                _imopVarPre351 = lhs[i_imopVar114 - 1][j_imopVar115][k_imopVar116][2];
                _imopVarPre352 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][0];
                matmul_sub(_imopVarPre352, _imopVarPre351, _imopVarPre350);
                double ( *_imopVarPre356 );
                double ( *_imopVarPre357 )[5];
                double ( *_imopVarPre358 )[5];
                _imopVarPre356 = rhs[i_imopVar114][j_imopVar115][k_imopVar116];
                _imopVarPre357 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][2];
                _imopVarPre358 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][1];
                binvcrhs(_imopVarPre358, _imopVarPre357, _imopVarPre356);
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    }
#pragma omp for nowait
    for (j_imopVar115 = 1; j_imopVar115 < grid_points[1] - 1; j_imopVar115++) {
        for (k_imopVar116 = 1; k_imopVar116 < grid_points[2] - 1; k_imopVar116++) {
            double ( *_imopVarPre362 );
            double ( *_imopVarPre363 );
            double ( *_imopVarPre364 )[5];
            _imopVarPre362 = rhs[isize][j_imopVar115][k_imopVar116];
            _imopVarPre363 = rhs[isize - 1][j_imopVar115][k_imopVar116];
            _imopVarPre364 = lhs[isize][j_imopVar115][k_imopVar116][0];
            matvec_sub(_imopVarPre364, _imopVarPre363, _imopVarPre362);
            double ( *_imopVarPre368 )[5];
            double ( *_imopVarPre369 )[5];
            double ( *_imopVarPre370 )[5];
            _imopVarPre368 = lhs[isize][j_imopVar115][k_imopVar116][1];
            _imopVarPre369 = lhs[isize - 1][j_imopVar115][k_imopVar116][2];
            _imopVarPre370 = lhs[isize][j_imopVar115][k_imopVar116][0];
            matmul_sub(_imopVarPre370, _imopVarPre369, _imopVarPre368);
            double ( *_imopVarPre373 );
            double ( *_imopVarPre374 )[5];
            _imopVarPre373 = rhs[i_imopVar114][j_imopVar115][k_imopVar116];
            _imopVarPre374 = lhs[i_imopVar114][j_imopVar115][k_imopVar116][1];
            binvrhs(_imopVarPre374, _imopVarPre373);
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    int i_imopVar117;
    int j_imopVar118;
    int k_imopVar119;
    int m;
    int n;
    for (i_imopVar117 = grid_points[0] - 2; i_imopVar117 >= 0; i_imopVar117--) {
#pragma omp for nowait
        for (j_imopVar118 = 1; j_imopVar118 < grid_points[1] - 1; j_imopVar118++) {
            for (k_imopVar119 = 1; k_imopVar119 < grid_points[2] - 1; k_imopVar119++) {
                for (m = 0; m < 5; m++) {
                    for (n = 0; n < 5; n++) {
                        rhs[i_imopVar117][j_imopVar118][k_imopVar119][m] = rhs[i_imopVar117][j_imopVar118][k_imopVar119][m] - lhs[i_imopVar117][j_imopVar118][k_imopVar119][2][m][n] * rhs[i_imopVar117 + 1][j_imopVar118][k_imopVar119][n];
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    }
}
static void x_backsubstitute(void ) {
    int i;
    int j;
    int k;
    int m;
    int n;
    for (i = grid_points[0] - 2; i >= 0; i--) {
#pragma omp for nowait
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                for (m = 0; m < 5; m++) {
                    for (n = 0; n < 5; n++) {
                        rhs[i][j][k][m] = rhs[i][j][k][m] - lhs[i][j][k][2][m][n] * rhs[i + 1][j][k][n];
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written([rhs.f]) read([lhs.f, j, grid_points, grid_points.f, lhs, rhs, rhs.f])
#pragma omp barrier
    }
}
static void x_solve_cell(void ) {
    int i;
    int j;
    int k;
    int isize;
    isize = grid_points[0] - 1;
#pragma omp for nowait
    for (j = 1; j < grid_points[1] - 1; j++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            double ( *_imopVarPre338 );
            double ( *_imopVarPre339 )[5];
            double ( *_imopVarPre340 )[5];
            _imopVarPre338 = rhs[0][j][k];
            _imopVarPre339 = lhs[0][j][k][2];
            _imopVarPre340 = lhs[0][j][k][1];
            binvcrhs(_imopVarPre340, _imopVarPre339, _imopVarPre338);
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    for (i = 1; i < isize; i++) {
#pragma omp for nowait
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                double ( *_imopVarPre344 );
                double ( *_imopVarPre345 );
                double ( *_imopVarPre346 )[5];
                _imopVarPre344 = rhs[i][j][k];
                _imopVarPre345 = rhs[i - 1][j][k];
                _imopVarPre346 = lhs[i][j][k][0];
                matvec_sub(_imopVarPre346, _imopVarPre345, _imopVarPre344);
                double ( *_imopVarPre350 )[5];
                double ( *_imopVarPre351 )[5];
                double ( *_imopVarPre352 )[5];
                _imopVarPre350 = lhs[i][j][k][1];
                _imopVarPre351 = lhs[i - 1][j][k][2];
                _imopVarPre352 = lhs[i][j][k][0];
                matmul_sub(_imopVarPre352, _imopVarPre351, _imopVarPre350);
                double ( *_imopVarPre356 );
                double ( *_imopVarPre357 )[5];
                double ( *_imopVarPre358 )[5];
                _imopVarPre356 = rhs[i][j][k];
                _imopVarPre357 = lhs[i][j][k][2];
                _imopVarPre358 = lhs[i][j][k][1];
                binvcrhs(_imopVarPre358, _imopVarPre357, _imopVarPre356);
            }
        }
// #pragma omp dummyFlush BARRIER_START written([lhs.f, rhs.f]) read([lhs.f, matvec_sub, binvcrhs, grid_points, grid_points.f, binvrhs, lhs, rhs, matmul_sub, rhs.f, j])
#pragma omp barrier
    }
#pragma omp for nowait
    for (j = 1; j < grid_points[1] - 1; j++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            double ( *_imopVarPre362 );
            double ( *_imopVarPre363 );
            double ( *_imopVarPre364 )[5];
            _imopVarPre362 = rhs[isize][j][k];
            _imopVarPre363 = rhs[isize - 1][j][k];
            _imopVarPre364 = lhs[isize][j][k][0];
            matvec_sub(_imopVarPre364, _imopVarPre363, _imopVarPre362);
            double ( *_imopVarPre368 )[5];
            double ( *_imopVarPre369 )[5];
            double ( *_imopVarPre370 )[5];
            _imopVarPre368 = lhs[isize][j][k][1];
            _imopVarPre369 = lhs[isize - 1][j][k][2];
            _imopVarPre370 = lhs[isize][j][k][0];
            matmul_sub(_imopVarPre370, _imopVarPre369, _imopVarPre368);
            double ( *_imopVarPre373 );
            double ( *_imopVarPre374 )[5];
            _imopVarPre373 = rhs[i][j][k];
            _imopVarPre374 = lhs[i][j][k][1];
            binvrhs(_imopVarPre374, _imopVarPre373);
        }
    }
// #pragma omp dummyFlush BARRIER_START written([lhs.f, rhs.f]) read([lhs.f, j, grid_points, grid_points.f, x_backsubstitute, lhs, rhs, rhs.f])
#pragma omp barrier
}
static void matvec_sub(double ablock[5][5], double avec[5] , double bvec[5]) {
    int i;
    for (i = 0; i < 5; i++) {
        bvec[i] = bvec[i] - ablock[i][0] * avec[0] - ablock[i][1] * avec[1] - ablock[i][2] * avec[2] - ablock[i][3] * avec[3] - ablock[i][4] * avec[4];
    }
}
static void matmul_sub(double ablock[5][5], double bblock[5][5] , double cblock[5][5]) {
    int j;
    for (j = 0; j < 5; j++) {
        cblock[0][j] = cblock[0][j] - ablock[0][0] * bblock[0][j] - ablock[0][1] * bblock[1][j] - ablock[0][2] * bblock[2][j] - ablock[0][3] * bblock[3][j] - ablock[0][4] * bblock[4][j];
        cblock[1][j] = cblock[1][j] - ablock[1][0] * bblock[0][j] - ablock[1][1] * bblock[1][j] - ablock[1][2] * bblock[2][j] - ablock[1][3] * bblock[3][j] - ablock[1][4] * bblock[4][j];
        cblock[2][j] = cblock[2][j] - ablock[2][0] * bblock[0][j] - ablock[2][1] * bblock[1][j] - ablock[2][2] * bblock[2][j] - ablock[2][3] * bblock[3][j] - ablock[2][4] * bblock[4][j];
        cblock[3][j] = cblock[3][j] - ablock[3][0] * bblock[0][j] - ablock[3][1] * bblock[1][j] - ablock[3][2] * bblock[2][j] - ablock[3][3] * bblock[3][j] - ablock[3][4] * bblock[4][j];
        cblock[4][j] = cblock[4][j] - ablock[4][0] * bblock[0][j] - ablock[4][1] * bblock[1][j] - ablock[4][2] * bblock[2][j] - ablock[4][3] * bblock[3][j] - ablock[4][4] * bblock[4][j];
    }
}
static void binvcrhs(double lhs[5][5], double c[5][5] , double r[5]) {
    double pivot;
    double coeff;
    pivot = 1.00 / lhs[0][0];
    lhs[0][1] = lhs[0][1] * pivot;
    lhs[0][2] = lhs[0][2] * pivot;
    lhs[0][3] = lhs[0][3] * pivot;
    lhs[0][4] = lhs[0][4] * pivot;
    c[0][0] = c[0][0] * pivot;
    c[0][1] = c[0][1] * pivot;
    c[0][2] = c[0][2] * pivot;
    c[0][3] = c[0][3] * pivot;
    c[0][4] = c[0][4] * pivot;
    r[0] = r[0] * pivot;
    coeff = lhs[1][0];
    lhs[1][1] = lhs[1][1] - coeff * lhs[0][1];
    lhs[1][2] = lhs[1][2] - coeff * lhs[0][2];
    lhs[1][3] = lhs[1][3] - coeff * lhs[0][3];
    lhs[1][4] = lhs[1][4] - coeff * lhs[0][4];
    c[1][0] = c[1][0] - coeff * c[0][0];
    c[1][1] = c[1][1] - coeff * c[0][1];
    c[1][2] = c[1][2] - coeff * c[0][2];
    c[1][3] = c[1][3] - coeff * c[0][3];
    c[1][4] = c[1][4] - coeff * c[0][4];
    r[1] = r[1] - coeff * r[0];
    coeff = lhs[2][0];
    lhs[2][1] = lhs[2][1] - coeff * lhs[0][1];
    lhs[2][2] = lhs[2][2] - coeff * lhs[0][2];
    lhs[2][3] = lhs[2][3] - coeff * lhs[0][3];
    lhs[2][4] = lhs[2][4] - coeff * lhs[0][4];
    c[2][0] = c[2][0] - coeff * c[0][0];
    c[2][1] = c[2][1] - coeff * c[0][1];
    c[2][2] = c[2][2] - coeff * c[0][2];
    c[2][3] = c[2][3] - coeff * c[0][3];
    c[2][4] = c[2][4] - coeff * c[0][4];
    r[2] = r[2] - coeff * r[0];
    coeff = lhs[3][0];
    lhs[3][1] = lhs[3][1] - coeff * lhs[0][1];
    lhs[3][2] = lhs[3][2] - coeff * lhs[0][2];
    lhs[3][3] = lhs[3][3] - coeff * lhs[0][3];
    lhs[3][4] = lhs[3][4] - coeff * lhs[0][4];
    c[3][0] = c[3][0] - coeff * c[0][0];
    c[3][1] = c[3][1] - coeff * c[0][1];
    c[3][2] = c[3][2] - coeff * c[0][2];
    c[3][3] = c[3][3] - coeff * c[0][3];
    c[3][4] = c[3][4] - coeff * c[0][4];
    r[3] = r[3] - coeff * r[0];
    coeff = lhs[4][0];
    lhs[4][1] = lhs[4][1] - coeff * lhs[0][1];
    lhs[4][2] = lhs[4][2] - coeff * lhs[0][2];
    lhs[4][3] = lhs[4][3] - coeff * lhs[0][3];
    lhs[4][4] = lhs[4][4] - coeff * lhs[0][4];
    c[4][0] = c[4][0] - coeff * c[0][0];
    c[4][1] = c[4][1] - coeff * c[0][1];
    c[4][2] = c[4][2] - coeff * c[0][2];
    c[4][3] = c[4][3] - coeff * c[0][3];
    c[4][4] = c[4][4] - coeff * c[0][4];
    r[4] = r[4] - coeff * r[0];
    pivot = 1.00 / lhs[1][1];
    lhs[1][2] = lhs[1][2] * pivot;
    lhs[1][3] = lhs[1][3] * pivot;
    lhs[1][4] = lhs[1][4] * pivot;
    c[1][0] = c[1][0] * pivot;
    c[1][1] = c[1][1] * pivot;
    c[1][2] = c[1][2] * pivot;
    c[1][3] = c[1][3] * pivot;
    c[1][4] = c[1][4] * pivot;
    r[1] = r[1] * pivot;
    coeff = lhs[0][1];
    lhs[0][2] = lhs[0][2] - coeff * lhs[1][2];
    lhs[0][3] = lhs[0][3] - coeff * lhs[1][3];
    lhs[0][4] = lhs[0][4] - coeff * lhs[1][4];
    c[0][0] = c[0][0] - coeff * c[1][0];
    c[0][1] = c[0][1] - coeff * c[1][1];
    c[0][2] = c[0][2] - coeff * c[1][2];
    c[0][3] = c[0][3] - coeff * c[1][3];
    c[0][4] = c[0][4] - coeff * c[1][4];
    r[0] = r[0] - coeff * r[1];
    coeff = lhs[2][1];
    lhs[2][2] = lhs[2][2] - coeff * lhs[1][2];
    lhs[2][3] = lhs[2][3] - coeff * lhs[1][3];
    lhs[2][4] = lhs[2][4] - coeff * lhs[1][4];
    c[2][0] = c[2][0] - coeff * c[1][0];
    c[2][1] = c[2][1] - coeff * c[1][1];
    c[2][2] = c[2][2] - coeff * c[1][2];
    c[2][3] = c[2][3] - coeff * c[1][3];
    c[2][4] = c[2][4] - coeff * c[1][4];
    r[2] = r[2] - coeff * r[1];
    coeff = lhs[3][1];
    lhs[3][2] = lhs[3][2] - coeff * lhs[1][2];
    lhs[3][3] = lhs[3][3] - coeff * lhs[1][3];
    lhs[3][4] = lhs[3][4] - coeff * lhs[1][4];
    c[3][0] = c[3][0] - coeff * c[1][0];
    c[3][1] = c[3][1] - coeff * c[1][1];
    c[3][2] = c[3][2] - coeff * c[1][2];
    c[3][3] = c[3][3] - coeff * c[1][3];
    c[3][4] = c[3][4] - coeff * c[1][4];
    r[3] = r[3] - coeff * r[1];
    coeff = lhs[4][1];
    lhs[4][2] = lhs[4][2] - coeff * lhs[1][2];
    lhs[4][3] = lhs[4][3] - coeff * lhs[1][3];
    lhs[4][4] = lhs[4][4] - coeff * lhs[1][4];
    c[4][0] = c[4][0] - coeff * c[1][0];
    c[4][1] = c[4][1] - coeff * c[1][1];
    c[4][2] = c[4][2] - coeff * c[1][2];
    c[4][3] = c[4][3] - coeff * c[1][3];
    c[4][4] = c[4][4] - coeff * c[1][4];
    r[4] = r[4] - coeff * r[1];
    pivot = 1.00 / lhs[2][2];
    lhs[2][3] = lhs[2][3] * pivot;
    lhs[2][4] = lhs[2][4] * pivot;
    c[2][0] = c[2][0] * pivot;
    c[2][1] = c[2][1] * pivot;
    c[2][2] = c[2][2] * pivot;
    c[2][3] = c[2][3] * pivot;
    c[2][4] = c[2][4] * pivot;
    r[2] = r[2] * pivot;
    coeff = lhs[0][2];
    lhs[0][3] = lhs[0][3] - coeff * lhs[2][3];
    lhs[0][4] = lhs[0][4] - coeff * lhs[2][4];
    c[0][0] = c[0][0] - coeff * c[2][0];
    c[0][1] = c[0][1] - coeff * c[2][1];
    c[0][2] = c[0][2] - coeff * c[2][2];
    c[0][3] = c[0][3] - coeff * c[2][3];
    c[0][4] = c[0][4] - coeff * c[2][4];
    r[0] = r[0] - coeff * r[2];
    coeff = lhs[1][2];
    lhs[1][3] = lhs[1][3] - coeff * lhs[2][3];
    lhs[1][4] = lhs[1][4] - coeff * lhs[2][4];
    c[1][0] = c[1][0] - coeff * c[2][0];
    c[1][1] = c[1][1] - coeff * c[2][1];
    c[1][2] = c[1][2] - coeff * c[2][2];
    c[1][3] = c[1][3] - coeff * c[2][3];
    c[1][4] = c[1][4] - coeff * c[2][4];
    r[1] = r[1] - coeff * r[2];
    coeff = lhs[3][2];
    lhs[3][3] = lhs[3][3] - coeff * lhs[2][3];
    lhs[3][4] = lhs[3][4] - coeff * lhs[2][4];
    c[3][0] = c[3][0] - coeff * c[2][0];
    c[3][1] = c[3][1] - coeff * c[2][1];
    c[3][2] = c[3][2] - coeff * c[2][2];
    c[3][3] = c[3][3] - coeff * c[2][3];
    c[3][4] = c[3][4] - coeff * c[2][4];
    r[3] = r[3] - coeff * r[2];
    coeff = lhs[4][2];
    lhs[4][3] = lhs[4][3] - coeff * lhs[2][3];
    lhs[4][4] = lhs[4][4] - coeff * lhs[2][4];
    c[4][0] = c[4][0] - coeff * c[2][0];
    c[4][1] = c[4][1] - coeff * c[2][1];
    c[4][2] = c[4][2] - coeff * c[2][2];
    c[4][3] = c[4][3] - coeff * c[2][3];
    c[4][4] = c[4][4] - coeff * c[2][4];
    r[4] = r[4] - coeff * r[2];
    pivot = 1.00 / lhs[3][3];
    lhs[3][4] = lhs[3][4] * pivot;
    c[3][0] = c[3][0] * pivot;
    c[3][1] = c[3][1] * pivot;
    c[3][2] = c[3][2] * pivot;
    c[3][3] = c[3][3] * pivot;
    c[3][4] = c[3][4] * pivot;
    r[3] = r[3] * pivot;
    coeff = lhs[0][3];
    lhs[0][4] = lhs[0][4] - coeff * lhs[3][4];
    c[0][0] = c[0][0] - coeff * c[3][0];
    c[0][1] = c[0][1] - coeff * c[3][1];
    c[0][2] = c[0][2] - coeff * c[3][2];
    c[0][3] = c[0][3] - coeff * c[3][3];
    c[0][4] = c[0][4] - coeff * c[3][4];
    r[0] = r[0] - coeff * r[3];
    coeff = lhs[1][3];
    lhs[1][4] = lhs[1][4] - coeff * lhs[3][4];
    c[1][0] = c[1][0] - coeff * c[3][0];
    c[1][1] = c[1][1] - coeff * c[3][1];
    c[1][2] = c[1][2] - coeff * c[3][2];
    c[1][3] = c[1][3] - coeff * c[3][3];
    c[1][4] = c[1][4] - coeff * c[3][4];
    r[1] = r[1] - coeff * r[3];
    coeff = lhs[2][3];
    lhs[2][4] = lhs[2][4] - coeff * lhs[3][4];
    c[2][0] = c[2][0] - coeff * c[3][0];
    c[2][1] = c[2][1] - coeff * c[3][1];
    c[2][2] = c[2][2] - coeff * c[3][2];
    c[2][3] = c[2][3] - coeff * c[3][3];
    c[2][4] = c[2][4] - coeff * c[3][4];
    r[2] = r[2] - coeff * r[3];
    coeff = lhs[4][3];
    lhs[4][4] = lhs[4][4] - coeff * lhs[3][4];
    c[4][0] = c[4][0] - coeff * c[3][0];
    c[4][1] = c[4][1] - coeff * c[3][1];
    c[4][2] = c[4][2] - coeff * c[3][2];
    c[4][3] = c[4][3] - coeff * c[3][3];
    c[4][4] = c[4][4] - coeff * c[3][4];
    r[4] = r[4] - coeff * r[3];
    pivot = 1.00 / lhs[4][4];
    c[4][0] = c[4][0] * pivot;
    c[4][1] = c[4][1] * pivot;
    c[4][2] = c[4][2] * pivot;
    c[4][3] = c[4][3] * pivot;
    c[4][4] = c[4][4] * pivot;
    r[4] = r[4] * pivot;
    coeff = lhs[0][4];
    c[0][0] = c[0][0] - coeff * c[4][0];
    c[0][1] = c[0][1] - coeff * c[4][1];
    c[0][2] = c[0][2] - coeff * c[4][2];
    c[0][3] = c[0][3] - coeff * c[4][3];
    c[0][4] = c[0][4] - coeff * c[4][4];
    r[0] = r[0] - coeff * r[4];
    coeff = lhs[1][4];
    c[1][0] = c[1][0] - coeff * c[4][0];
    c[1][1] = c[1][1] - coeff * c[4][1];
    c[1][2] = c[1][2] - coeff * c[4][2];
    c[1][3] = c[1][3] - coeff * c[4][3];
    c[1][4] = c[1][4] - coeff * c[4][4];
    r[1] = r[1] - coeff * r[4];
    coeff = lhs[2][4];
    c[2][0] = c[2][0] - coeff * c[4][0];
    c[2][1] = c[2][1] - coeff * c[4][1];
    c[2][2] = c[2][2] - coeff * c[4][2];
    c[2][3] = c[2][3] - coeff * c[4][3];
    c[2][4] = c[2][4] - coeff * c[4][4];
    r[2] = r[2] - coeff * r[4];
    coeff = lhs[3][4];
    c[3][0] = c[3][0] - coeff * c[4][0];
    c[3][1] = c[3][1] - coeff * c[4][1];
    c[3][2] = c[3][2] - coeff * c[4][2];
    c[3][3] = c[3][3] - coeff * c[4][3];
    c[3][4] = c[3][4] - coeff * c[4][4];
    r[3] = r[3] - coeff * r[4];
}
static void binvrhs(double lhs[5][5], double r[5]) {
    double pivot;
    double coeff;
    pivot = 1.00 / lhs[0][0];
    lhs[0][1] = lhs[0][1] * pivot;
    lhs[0][2] = lhs[0][2] * pivot;
    lhs[0][3] = lhs[0][3] * pivot;
    lhs[0][4] = lhs[0][4] * pivot;
    r[0] = r[0] * pivot;
    coeff = lhs[1][0];
    lhs[1][1] = lhs[1][1] - coeff * lhs[0][1];
    lhs[1][2] = lhs[1][2] - coeff * lhs[0][2];
    lhs[1][3] = lhs[1][3] - coeff * lhs[0][3];
    lhs[1][4] = lhs[1][4] - coeff * lhs[0][4];
    r[1] = r[1] - coeff * r[0];
    coeff = lhs[2][0];
    lhs[2][1] = lhs[2][1] - coeff * lhs[0][1];
    lhs[2][2] = lhs[2][2] - coeff * lhs[0][2];
    lhs[2][3] = lhs[2][3] - coeff * lhs[0][3];
    lhs[2][4] = lhs[2][4] - coeff * lhs[0][4];
    r[2] = r[2] - coeff * r[0];
    coeff = lhs[3][0];
    lhs[3][1] = lhs[3][1] - coeff * lhs[0][1];
    lhs[3][2] = lhs[3][2] - coeff * lhs[0][2];
    lhs[3][3] = lhs[3][3] - coeff * lhs[0][3];
    lhs[3][4] = lhs[3][4] - coeff * lhs[0][4];
    r[3] = r[3] - coeff * r[0];
    coeff = lhs[4][0];
    lhs[4][1] = lhs[4][1] - coeff * lhs[0][1];
    lhs[4][2] = lhs[4][2] - coeff * lhs[0][2];
    lhs[4][3] = lhs[4][3] - coeff * lhs[0][3];
    lhs[4][4] = lhs[4][4] - coeff * lhs[0][4];
    r[4] = r[4] - coeff * r[0];
    pivot = 1.00 / lhs[1][1];
    lhs[1][2] = lhs[1][2] * pivot;
    lhs[1][3] = lhs[1][3] * pivot;
    lhs[1][4] = lhs[1][4] * pivot;
    r[1] = r[1] * pivot;
    coeff = lhs[0][1];
    lhs[0][2] = lhs[0][2] - coeff * lhs[1][2];
    lhs[0][3] = lhs[0][3] - coeff * lhs[1][3];
    lhs[0][4] = lhs[0][4] - coeff * lhs[1][4];
    r[0] = r[0] - coeff * r[1];
    coeff = lhs[2][1];
    lhs[2][2] = lhs[2][2] - coeff * lhs[1][2];
    lhs[2][3] = lhs[2][3] - coeff * lhs[1][3];
    lhs[2][4] = lhs[2][4] - coeff * lhs[1][4];
    r[2] = r[2] - coeff * r[1];
    coeff = lhs[3][1];
    lhs[3][2] = lhs[3][2] - coeff * lhs[1][2];
    lhs[3][3] = lhs[3][3] - coeff * lhs[1][3];
    lhs[3][4] = lhs[3][4] - coeff * lhs[1][4];
    r[3] = r[3] - coeff * r[1];
    coeff = lhs[4][1];
    lhs[4][2] = lhs[4][2] - coeff * lhs[1][2];
    lhs[4][3] = lhs[4][3] - coeff * lhs[1][3];
    lhs[4][4] = lhs[4][4] - coeff * lhs[1][4];
    r[4] = r[4] - coeff * r[1];
    pivot = 1.00 / lhs[2][2];
    lhs[2][3] = lhs[2][3] * pivot;
    lhs[2][4] = lhs[2][4] * pivot;
    r[2] = r[2] * pivot;
    coeff = lhs[0][2];
    lhs[0][3] = lhs[0][3] - coeff * lhs[2][3];
    lhs[0][4] = lhs[0][4] - coeff * lhs[2][4];
    r[0] = r[0] - coeff * r[2];
    coeff = lhs[1][2];
    lhs[1][3] = lhs[1][3] - coeff * lhs[2][3];
    lhs[1][4] = lhs[1][4] - coeff * lhs[2][4];
    r[1] = r[1] - coeff * r[2];
    coeff = lhs[3][2];
    lhs[3][3] = lhs[3][3] - coeff * lhs[2][3];
    lhs[3][4] = lhs[3][4] - coeff * lhs[2][4];
    r[3] = r[3] - coeff * r[2];
    coeff = lhs[4][2];
    lhs[4][3] = lhs[4][3] - coeff * lhs[2][3];
    lhs[4][4] = lhs[4][4] - coeff * lhs[2][4];
    r[4] = r[4] - coeff * r[2];
    pivot = 1.00 / lhs[3][3];
    lhs[3][4] = lhs[3][4] * pivot;
    r[3] = r[3] * pivot;
    coeff = lhs[0][3];
    lhs[0][4] = lhs[0][4] - coeff * lhs[3][4];
    r[0] = r[0] - coeff * r[3];
    coeff = lhs[1][3];
    lhs[1][4] = lhs[1][4] - coeff * lhs[3][4];
    r[1] = r[1] - coeff * r[3];
    coeff = lhs[2][3];
    lhs[2][4] = lhs[2][4] - coeff * lhs[3][4];
    r[2] = r[2] - coeff * r[3];
    coeff = lhs[4][3];
    lhs[4][4] = lhs[4][4] - coeff * lhs[3][4];
    r[4] = r[4] - coeff * r[3];
    pivot = 1.00 / lhs[4][4];
    r[4] = r[4] * pivot;
    coeff = lhs[0][4];
    r[0] = r[0] - coeff * r[4];
    coeff = lhs[1][4];
    r[1] = r[1] - coeff * r[4];
    coeff = lhs[2][4];
    r[2] = r[2] - coeff * r[4];
    coeff = lhs[3][4];
    r[3] = r[3] - coeff * r[4];
}
static void y_solve(void ) {
    int i_imopVar85;
    int j_imopVar86;
    int k_imopVar87;
#pragma omp for nowait
    for (i_imopVar85 = 1; i_imopVar85 < grid_points[0] - 1; i_imopVar85++) {
        for (j_imopVar86 = 0; j_imopVar86 < grid_points[1]; j_imopVar86++) {
            for (k_imopVar87 = 1; k_imopVar87 < grid_points[2] - 1; k_imopVar87++) {
                tmp1 = 1.0 / u[i_imopVar85][j_imopVar86][k_imopVar87][0];
                tmp2 = tmp1 * tmp1;
                tmp3 = tmp1 * tmp2;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][0][0] = 0.0;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][0][1] = 0.0;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][0][2] = 1.0;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][0][3] = 0.0;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][0][4] = 0.0;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][1][0] = -(u[i_imopVar85][j_imopVar86][k_imopVar87][1] * u[i_imopVar85][j_imopVar86][k_imopVar87][2]) * tmp2;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][1][1] = u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp1;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][1][2] = u[i_imopVar85][j_imopVar86][k_imopVar87][1] * tmp1;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][1][3] = 0.0;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][1][4] = 0.0;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][2][0] = -(u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp2) + 0.50 * c2 * ((u[i_imopVar85][j_imopVar86][k_imopVar87][1] * u[i_imopVar85][j_imopVar86][k_imopVar87][1] + u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][2] + u[i_imopVar85][j_imopVar86][k_imopVar87][3] * u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * tmp2);
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][2][1] = -c2 * u[i_imopVar85][j_imopVar86][k_imopVar87][1] * tmp1;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][2][2] = (2.0 - c2) * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp1;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][2][3] = -c2 * u[i_imopVar85][j_imopVar86][k_imopVar87][3] * tmp1;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][2][4] = c2;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][3][0] = -(u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * tmp2;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][3][1] = 0.0;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][3][2] = u[i_imopVar85][j_imopVar86][k_imopVar87][3] * tmp1;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][3][3] = u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp1;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][3][4] = 0.0;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][4][0] = (c2 * (u[i_imopVar85][j_imopVar86][k_imopVar87][1] * u[i_imopVar85][j_imopVar86][k_imopVar87][1] + u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][2] + u[i_imopVar85][j_imopVar86][k_imopVar87][3] * u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * tmp2 - c1 * u[i_imopVar85][j_imopVar86][k_imopVar87][4] * tmp1) * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp1;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][4][1] = -c2 * u[i_imopVar85][j_imopVar86][k_imopVar87][1] * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp2;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][4][2] = c1 * u[i_imopVar85][j_imopVar86][k_imopVar87][4] * tmp1 - 0.50 * c2 * ((u[i_imopVar85][j_imopVar86][k_imopVar87][1] * u[i_imopVar85][j_imopVar86][k_imopVar87][1] + 3.0 * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][2] + u[i_imopVar85][j_imopVar86][k_imopVar87][3] * u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * tmp2);
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][4][3] = -c2 * (u[i_imopVar85][j_imopVar86][k_imopVar87][2] * u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * tmp2;
                fjac[i_imopVar85][j_imopVar86][k_imopVar87][4][4] = c1 * u[i_imopVar85][j_imopVar86][k_imopVar87][2] * tmp1;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][0][0] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][0][1] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][0][2] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][0][3] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][0][4] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][1][0] = -c3c4 * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][1];
                njac[i_imopVar85][j_imopVar86][k_imopVar87][1][1] = c3c4 * tmp1;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][1][2] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][1][3] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][1][4] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][2][0] = -con43 * c3c4 * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][2];
                njac[i_imopVar85][j_imopVar86][k_imopVar87][2][1] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][2][2] = con43 * c3c4 * tmp1;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][2][3] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][2][4] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][3][0] = -c3c4 * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][3];
                njac[i_imopVar85][j_imopVar86][k_imopVar87][3][1] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][3][2] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][3][3] = c3c4 * tmp1;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][3][4] = 0.0;
                njac[i_imopVar85][j_imopVar86][k_imopVar87][4][0] = -(c3c4 - c1345) * tmp3 * (((u[i_imopVar85][j_imopVar86][k_imopVar87][1]) * (u[i_imopVar85][j_imopVar86][k_imopVar87][1]))) - (con43 * c3c4 - c1345) * tmp3 * (((u[i_imopVar85][j_imopVar86][k_imopVar87][2]) * (u[i_imopVar85][j_imopVar86][k_imopVar87][2]))) - (c3c4 - c1345) * tmp3 * (((u[i_imopVar85][j_imopVar86][k_imopVar87][3]) * (u[i_imopVar85][j_imopVar86][k_imopVar87][3]))) - c1345 * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][4];
                njac[i_imopVar85][j_imopVar86][k_imopVar87][4][1] = (c3c4 - c1345) * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][1];
                njac[i_imopVar85][j_imopVar86][k_imopVar87][4][2] = (con43 * c3c4 - c1345) * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][2];
                njac[i_imopVar85][j_imopVar86][k_imopVar87][4][3] = (c3c4 - c1345) * tmp2 * u[i_imopVar85][j_imopVar86][k_imopVar87][3];
                njac[i_imopVar85][j_imopVar86][k_imopVar87][4][4] = c1345 * tmp1;
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (i_imopVar85 = 1; i_imopVar85 < grid_points[0] - 1; i_imopVar85++) {
        for (j_imopVar86 = 1; j_imopVar86 < grid_points[1] - 1; j_imopVar86++) {
            for (k_imopVar87 = 1; k_imopVar87 < grid_points[2] - 1; k_imopVar87++) {
                tmp1 = dt * ty1;
                tmp2 = dt * ty2;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][0][0] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][0] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][0] - tmp1 * dy1;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][0][1] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][1] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][0][2] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][2] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][0][3] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][3] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][0][4] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][4] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][0][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][1][0] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][0] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][1][1] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][1] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][1] - tmp1 * dy2;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][1][2] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][2] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][1][3] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][3] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][1][4] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][4] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][1][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][2][0] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][0] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][2][1] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][1] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][2][2] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][2] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][2] - tmp1 * dy3;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][2][3] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][3] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][2][4] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][4] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][2][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][3][0] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][0] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][3][1] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][1] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][3][2] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][2] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][3][3] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][3] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][3] - tmp1 * dy4;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][3][4] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][4] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][3][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][4][0] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][0] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][4][1] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][1] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][4][2] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][2] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][4][3] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][3] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][0][4][4] = -tmp2 * fjac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][4] - tmp1 * njac[i_imopVar85][j_imopVar86 - 1][k_imopVar87][4][4] - tmp1 * dy5;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][0][0] = 1.0 + tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][0][0] + tmp1 * 2.0 * dy1;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][0][1] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][0][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][0][2] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][0][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][0][3] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][0][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][0][4] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][0][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][1][0] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][1][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][1][1] = 1.0 + tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][1][1] + tmp1 * 2.0 * dy2;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][1][2] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][1][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][1][3] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][1][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][1][4] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][1][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][2][0] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][2][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][2][1] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][2][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][2][2] = 1.0 + tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][2][2] + tmp1 * 2.0 * dy3;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][2][3] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][2][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][2][4] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][2][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][3][0] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][3][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][3][1] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][3][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][3][2] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][3][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][3][3] = 1.0 + tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][3][3] + tmp1 * 2.0 * dy4;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][3][4] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][3][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][4][0] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][4][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][4][1] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][4][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][4][2] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][4][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][4][3] = tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][4][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][1][4][4] = 1.0 + tmp1 * 2.0 * njac[i_imopVar85][j_imopVar86][k_imopVar87][4][4] + tmp1 * 2.0 * dy5;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][0][0] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][0] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][0] - tmp1 * dy1;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][0][1] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][1] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][0][2] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][2] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][0][3] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][3] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][0][4] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][4] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][0][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][1][0] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][0] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][1][1] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][1] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][1] - tmp1 * dy2;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][1][2] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][2] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][1][3] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][3] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][1][4] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][4] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][1][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][2][0] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][0] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][2][1] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][1] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][2][2] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][2] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][2] - tmp1 * dy3;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][2][3] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][3] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][2][4] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][4] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][2][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][3][0] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][0] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][3][1] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][1] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][3][2] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][2] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][3][3] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][3] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][3] - tmp1 * dy4;
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][3][4] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][4] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][3][4];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][4][0] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][0] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][0];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][4][1] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][1] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][1];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][4][2] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][2] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][2];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][4][3] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][3] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][3];
                lhs[i_imopVar85][j_imopVar86][k_imopVar87][2][4][4] = tmp2 * fjac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][4] - tmp1 * njac[i_imopVar85][j_imopVar86 + 1][k_imopVar87][4][4] - tmp1 * dy5;
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    int i;
    int j;
    int k;
    int jsize;
    jsize = grid_points[1] - 1;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            double ( *_imopVarPre378 );
            double ( *_imopVarPre379 )[5];
            double ( *_imopVarPre380 )[5];
            _imopVarPre378 = rhs[i][0][k];
            _imopVarPre379 = lhs[i][0][k][2];
            _imopVarPre380 = lhs[i][0][k][1];
            binvcrhs(_imopVarPre380, _imopVarPre379, _imopVarPre378);
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    for (j = 1; j < jsize; j++) {
#pragma omp for nowait
        for (i = 1; i < grid_points[0] - 1; i++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                double ( *_imopVarPre384 );
                double ( *_imopVarPre385 );
                double ( *_imopVarPre386 )[5];
                _imopVarPre384 = rhs[i][j][k];
                _imopVarPre385 = rhs[i][j - 1][k];
                _imopVarPre386 = lhs[i][j][k][0];
                matvec_sub(_imopVarPre386, _imopVarPre385, _imopVarPre384);
                double ( *_imopVarPre390 )[5];
                double ( *_imopVarPre391 )[5];
                double ( *_imopVarPre392 )[5];
                _imopVarPre390 = lhs[i][j][k][1];
                _imopVarPre391 = lhs[i][j - 1][k][2];
                _imopVarPre392 = lhs[i][j][k][0];
                matmul_sub(_imopVarPre392, _imopVarPre391, _imopVarPre390);
                double ( *_imopVarPre396 );
                double ( *_imopVarPre397 )[5];
                double ( *_imopVarPre398 )[5];
                _imopVarPre396 = rhs[i][j][k];
                _imopVarPre397 = lhs[i][j][k][2];
                _imopVarPre398 = lhs[i][j][k][1];
                binvcrhs(_imopVarPre398, _imopVarPre397, _imopVarPre396);
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    }
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            double ( *_imopVarPre402 );
            double ( *_imopVarPre403 );
            double ( *_imopVarPre404 )[5];
            _imopVarPre402 = rhs[i][jsize][k];
            _imopVarPre403 = rhs[i][jsize - 1][k];
            _imopVarPre404 = lhs[i][jsize][k][0];
            matvec_sub(_imopVarPre404, _imopVarPre403, _imopVarPre402);
            double ( *_imopVarPre408 )[5];
            double ( *_imopVarPre409 )[5];
            double ( *_imopVarPre410 )[5];
            _imopVarPre408 = lhs[i][jsize][k][1];
            _imopVarPre409 = lhs[i][jsize - 1][k][2];
            _imopVarPre410 = lhs[i][jsize][k][0];
            matmul_sub(_imopVarPre410, _imopVarPre409, _imopVarPre408);
            double ( *_imopVarPre413 );
            double ( *_imopVarPre414 )[5];
            _imopVarPre413 = rhs[i][jsize][k];
            _imopVarPre414 = lhs[i][jsize][k][1];
            binvrhs(_imopVarPre414, _imopVarPre413);
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    int i_imopVar82;
    int j_imopVar83;
    int k_imopVar84;
    int m;
    int n;
    for (j_imopVar83 = grid_points[1] - 2; j_imopVar83 >= 0; j_imopVar83--) {
#pragma omp for nowait
        for (i_imopVar82 = 1; i_imopVar82 < grid_points[0] - 1; i_imopVar82++) {
            for (k_imopVar84 = 1; k_imopVar84 < grid_points[2] - 1; k_imopVar84++) {
                for (m = 0; m < 5; m++) {
                    for (n = 0; n < 5; n++) {
                        rhs[i_imopVar82][j_imopVar83][k_imopVar84][m] = rhs[i_imopVar82][j_imopVar83][k_imopVar84][m] - lhs[i_imopVar82][j_imopVar83][k_imopVar84][2][m][n] * rhs[i_imopVar82][j_imopVar83 + 1][k_imopVar84][n];
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    }
}
static void y_backsubstitute(void ) {
    int i;
    int j;
    int k;
    int m;
    int n;
    for (j = grid_points[1] - 2; j >= 0; j--) {
#pragma omp for nowait
        for (i = 1; i < grid_points[0] - 1; i++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                for (m = 0; m < 5; m++) {
                    for (n = 0; n < 5; n++) {
                        rhs[i][j][k][m] = rhs[i][j][k][m] - lhs[i][j][k][2][m][n] * rhs[i][j + 1][k][n];
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written([rhs.f]) read([lhs.f, grid_points, grid_points.f, lhs, rhs, rhs.f, i])
#pragma omp barrier
    }
}
static void y_solve_cell(void ) {
    int i;
    int j;
    int k;
    int jsize;
    jsize = grid_points[1] - 1;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            double ( *_imopVarPre378 );
            double ( *_imopVarPre379 )[5];
            double ( *_imopVarPre380 )[5];
            _imopVarPre378 = rhs[i][0][k];
            _imopVarPre379 = lhs[i][0][k][2];
            _imopVarPre380 = lhs[i][0][k][1];
            binvcrhs(_imopVarPre380, _imopVarPre379, _imopVarPre378);
        }
    }
// #pragma omp dummyFlush BARRIER_START written([lhs.f, rhs.f]) read([lhs.f, matvec_sub, binvcrhs, grid_points, grid_points.f, binvrhs, lhs, rhs, i, matmul_sub, rhs.f])
#pragma omp barrier
    for (j = 1; j < jsize; j++) {
#pragma omp for nowait
        for (i = 1; i < grid_points[0] - 1; i++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                double ( *_imopVarPre384 );
                double ( *_imopVarPre385 );
                double ( *_imopVarPre386 )[5];
                _imopVarPre384 = rhs[i][j][k];
                _imopVarPre385 = rhs[i][j - 1][k];
                _imopVarPre386 = lhs[i][j][k][0];
                matvec_sub(_imopVarPre386, _imopVarPre385, _imopVarPre384);
                double ( *_imopVarPre390 )[5];
                double ( *_imopVarPre391 )[5];
                double ( *_imopVarPre392 )[5];
                _imopVarPre390 = lhs[i][j][k][1];
                _imopVarPre391 = lhs[i][j - 1][k][2];
                _imopVarPre392 = lhs[i][j][k][0];
                matmul_sub(_imopVarPre392, _imopVarPre391, _imopVarPre390);
                double ( *_imopVarPre396 );
                double ( *_imopVarPre397 )[5];
                double ( *_imopVarPre398 )[5];
                _imopVarPre396 = rhs[i][j][k];
                _imopVarPre397 = lhs[i][j][k][2];
                _imopVarPre398 = lhs[i][j][k][1];
                binvcrhs(_imopVarPre398, _imopVarPre397, _imopVarPre396);
            }
        }
// #pragma omp dummyFlush BARRIER_START written([lhs.f, rhs.f]) read([lhs.f, matvec_sub, binvcrhs, grid_points, grid_points.f, binvrhs, lhs, rhs, i, matmul_sub, rhs.f])
#pragma omp barrier
    }
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (k = 1; k < grid_points[2] - 1; k++) {
            double ( *_imopVarPre402 );
            double ( *_imopVarPre403 );
            double ( *_imopVarPre404 )[5];
            _imopVarPre402 = rhs[i][jsize][k];
            _imopVarPre403 = rhs[i][jsize - 1][k];
            _imopVarPre404 = lhs[i][jsize][k][0];
            matvec_sub(_imopVarPre404, _imopVarPre403, _imopVarPre402);
            double ( *_imopVarPre408 )[5];
            double ( *_imopVarPre409 )[5];
            double ( *_imopVarPre410 )[5];
            _imopVarPre408 = lhs[i][jsize][k][1];
            _imopVarPre409 = lhs[i][jsize - 1][k][2];
            _imopVarPre410 = lhs[i][jsize][k][0];
            matmul_sub(_imopVarPre410, _imopVarPre409, _imopVarPre408);
            double ( *_imopVarPre413 );
            double ( *_imopVarPre414 )[5];
            _imopVarPre413 = rhs[i][jsize][k];
            _imopVarPre414 = lhs[i][jsize][k][1];
            binvrhs(_imopVarPre414, _imopVarPre413);
        }
    }
// #pragma omp dummyFlush BARRIER_START written([lhs.f, rhs.f]) read([lhs.f, grid_points, grid_points.f, lhs, rhs, y_backsubstitute, rhs.f, i])
#pragma omp barrier
}
static void z_solve(void ) {
    int i;
    int j;
    int k;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 0; k < grid_points[2]; k++) {
                tmp1 = 1.0 / u[i][j][k][0];
                tmp2 = tmp1 * tmp1;
                tmp3 = tmp1 * tmp2;
                fjac[i][j][k][0][0] = 0.0;
                fjac[i][j][k][0][1] = 0.0;
                fjac[i][j][k][0][2] = 0.0;
                fjac[i][j][k][0][3] = 1.0;
                fjac[i][j][k][0][4] = 0.0;
                fjac[i][j][k][1][0] = -(u[i][j][k][1] * u[i][j][k][3]) * tmp2;
                fjac[i][j][k][1][1] = u[i][j][k][3] * tmp1;
                fjac[i][j][k][1][2] = 0.0;
                fjac[i][j][k][1][3] = u[i][j][k][1] * tmp1;
                fjac[i][j][k][1][4] = 0.0;
                fjac[i][j][k][2][0] = -(u[i][j][k][2] * u[i][j][k][3]) * tmp2;
                fjac[i][j][k][2][1] = 0.0;
                fjac[i][j][k][2][2] = u[i][j][k][3] * tmp1;
                fjac[i][j][k][2][3] = u[i][j][k][2] * tmp1;
                fjac[i][j][k][2][4] = 0.0;
                fjac[i][j][k][3][0] = -(u[i][j][k][3] * u[i][j][k][3] * tmp2) + 0.50 * c2 * ((u[i][j][k][1] * u[i][j][k][1] + u[i][j][k][2] * u[i][j][k][2] + u[i][j][k][3] * u[i][j][k][3]) * tmp2);
                fjac[i][j][k][3][1] = -c2 * u[i][j][k][1] * tmp1;
                fjac[i][j][k][3][2] = -c2 * u[i][j][k][2] * tmp1;
                fjac[i][j][k][3][3] = (2.0 - c2) * u[i][j][k][3] * tmp1;
                fjac[i][j][k][3][4] = c2;
                fjac[i][j][k][4][0] = (c2 * (u[i][j][k][1] * u[i][j][k][1] + u[i][j][k][2] * u[i][j][k][2] + u[i][j][k][3] * u[i][j][k][3]) * tmp2 - c1 * (u[i][j][k][4] * tmp1)) * (u[i][j][k][3] * tmp1);
                fjac[i][j][k][4][1] = -c2 * (u[i][j][k][1] * u[i][j][k][3]) * tmp2;
                fjac[i][j][k][4][2] = -c2 * (u[i][j][k][2] * u[i][j][k][3]) * tmp2;
                fjac[i][j][k][4][3] = c1 * (u[i][j][k][4] * tmp1) - 0.50 * c2 * ((u[i][j][k][1] * u[i][j][k][1] + u[i][j][k][2] * u[i][j][k][2] + 3.0 * u[i][j][k][3] * u[i][j][k][3]) * tmp2);
                fjac[i][j][k][4][4] = c1 * u[i][j][k][3] * tmp1;
                njac[i][j][k][0][0] = 0.0;
                njac[i][j][k][0][1] = 0.0;
                njac[i][j][k][0][2] = 0.0;
                njac[i][j][k][0][3] = 0.0;
                njac[i][j][k][0][4] = 0.0;
                njac[i][j][k][1][0] = -c3c4 * tmp2 * u[i][j][k][1];
                njac[i][j][k][1][1] = c3c4 * tmp1;
                njac[i][j][k][1][2] = 0.0;
                njac[i][j][k][1][3] = 0.0;
                njac[i][j][k][1][4] = 0.0;
                njac[i][j][k][2][0] = -c3c4 * tmp2 * u[i][j][k][2];
                njac[i][j][k][2][1] = 0.0;
                njac[i][j][k][2][2] = c3c4 * tmp1;
                njac[i][j][k][2][3] = 0.0;
                njac[i][j][k][2][4] = 0.0;
                njac[i][j][k][3][0] = -con43 * c3c4 * tmp2 * u[i][j][k][3];
                njac[i][j][k][3][1] = 0.0;
                njac[i][j][k][3][2] = 0.0;
                njac[i][j][k][3][3] = con43 * c3 * c4 * tmp1;
                njac[i][j][k][3][4] = 0.0;
                njac[i][j][k][4][0] = -(c3c4 - c1345) * tmp3 * (((u[i][j][k][1]) * (u[i][j][k][1]))) - (c3c4 - c1345) * tmp3 * (((u[i][j][k][2]) * (u[i][j][k][2]))) - (con43 * c3c4 - c1345) * tmp3 * (((u[i][j][k][3]) * (u[i][j][k][3]))) - c1345 * tmp2 * u[i][j][k][4];
                njac[i][j][k][4][1] = (c3c4 - c1345) * tmp2 * u[i][j][k][1];
                njac[i][j][k][4][2] = (c3c4 - c1345) * tmp2 * u[i][j][k][2];
                njac[i][j][k][4][3] = (con43 * c3c4 - c1345) * tmp2 * u[i][j][k][3];
                njac[i][j][k][4][4] = c1345 * tmp1;
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = 1; k < grid_points[2] - 1; k++) {
                tmp1 = dt * tz1;
                tmp2 = dt * tz2;
                lhs[i][j][k][0][0][0] = -tmp2 * fjac[i][j][k - 1][0][0] - tmp1 * njac[i][j][k - 1][0][0] - tmp1 * dz1;
                lhs[i][j][k][0][0][1] = -tmp2 * fjac[i][j][k - 1][0][1] - tmp1 * njac[i][j][k - 1][0][1];
                lhs[i][j][k][0][0][2] = -tmp2 * fjac[i][j][k - 1][0][2] - tmp1 * njac[i][j][k - 1][0][2];
                lhs[i][j][k][0][0][3] = -tmp2 * fjac[i][j][k - 1][0][3] - tmp1 * njac[i][j][k - 1][0][3];
                lhs[i][j][k][0][0][4] = -tmp2 * fjac[i][j][k - 1][0][4] - tmp1 * njac[i][j][k - 1][0][4];
                lhs[i][j][k][0][1][0] = -tmp2 * fjac[i][j][k - 1][1][0] - tmp1 * njac[i][j][k - 1][1][0];
                lhs[i][j][k][0][1][1] = -tmp2 * fjac[i][j][k - 1][1][1] - tmp1 * njac[i][j][k - 1][1][1] - tmp1 * dz2;
                lhs[i][j][k][0][1][2] = -tmp2 * fjac[i][j][k - 1][1][2] - tmp1 * njac[i][j][k - 1][1][2];
                lhs[i][j][k][0][1][3] = -tmp2 * fjac[i][j][k - 1][1][3] - tmp1 * njac[i][j][k - 1][1][3];
                lhs[i][j][k][0][1][4] = -tmp2 * fjac[i][j][k - 1][1][4] - tmp1 * njac[i][j][k - 1][1][4];
                lhs[i][j][k][0][2][0] = -tmp2 * fjac[i][j][k - 1][2][0] - tmp1 * njac[i][j][k - 1][2][0];
                lhs[i][j][k][0][2][1] = -tmp2 * fjac[i][j][k - 1][2][1] - tmp1 * njac[i][j][k - 1][2][1];
                lhs[i][j][k][0][2][2] = -tmp2 * fjac[i][j][k - 1][2][2] - tmp1 * njac[i][j][k - 1][2][2] - tmp1 * dz3;
                lhs[i][j][k][0][2][3] = -tmp2 * fjac[i][j][k - 1][2][3] - tmp1 * njac[i][j][k - 1][2][3];
                lhs[i][j][k][0][2][4] = -tmp2 * fjac[i][j][k - 1][2][4] - tmp1 * njac[i][j][k - 1][2][4];
                lhs[i][j][k][0][3][0] = -tmp2 * fjac[i][j][k - 1][3][0] - tmp1 * njac[i][j][k - 1][3][0];
                lhs[i][j][k][0][3][1] = -tmp2 * fjac[i][j][k - 1][3][1] - tmp1 * njac[i][j][k - 1][3][1];
                lhs[i][j][k][0][3][2] = -tmp2 * fjac[i][j][k - 1][3][2] - tmp1 * njac[i][j][k - 1][3][2];
                lhs[i][j][k][0][3][3] = -tmp2 * fjac[i][j][k - 1][3][3] - tmp1 * njac[i][j][k - 1][3][3] - tmp1 * dz4;
                lhs[i][j][k][0][3][4] = -tmp2 * fjac[i][j][k - 1][3][4] - tmp1 * njac[i][j][k - 1][3][4];
                lhs[i][j][k][0][4][0] = -tmp2 * fjac[i][j][k - 1][4][0] - tmp1 * njac[i][j][k - 1][4][0];
                lhs[i][j][k][0][4][1] = -tmp2 * fjac[i][j][k - 1][4][1] - tmp1 * njac[i][j][k - 1][4][1];
                lhs[i][j][k][0][4][2] = -tmp2 * fjac[i][j][k - 1][4][2] - tmp1 * njac[i][j][k - 1][4][2];
                lhs[i][j][k][0][4][3] = -tmp2 * fjac[i][j][k - 1][4][3] - tmp1 * njac[i][j][k - 1][4][3];
                lhs[i][j][k][0][4][4] = -tmp2 * fjac[i][j][k - 1][4][4] - tmp1 * njac[i][j][k - 1][4][4] - tmp1 * dz5;
                lhs[i][j][k][1][0][0] = 1.0 + tmp1 * 2.0 * njac[i][j][k][0][0] + tmp1 * 2.0 * dz1;
                lhs[i][j][k][1][0][1] = tmp1 * 2.0 * njac[i][j][k][0][1];
                lhs[i][j][k][1][0][2] = tmp1 * 2.0 * njac[i][j][k][0][2];
                lhs[i][j][k][1][0][3] = tmp1 * 2.0 * njac[i][j][k][0][3];
                lhs[i][j][k][1][0][4] = tmp1 * 2.0 * njac[i][j][k][0][4];
                lhs[i][j][k][1][1][0] = tmp1 * 2.0 * njac[i][j][k][1][0];
                lhs[i][j][k][1][1][1] = 1.0 + tmp1 * 2.0 * njac[i][j][k][1][1] + tmp1 * 2.0 * dz2;
                lhs[i][j][k][1][1][2] = tmp1 * 2.0 * njac[i][j][k][1][2];
                lhs[i][j][k][1][1][3] = tmp1 * 2.0 * njac[i][j][k][1][3];
                lhs[i][j][k][1][1][4] = tmp1 * 2.0 * njac[i][j][k][1][4];
                lhs[i][j][k][1][2][0] = tmp1 * 2.0 * njac[i][j][k][2][0];
                lhs[i][j][k][1][2][1] = tmp1 * 2.0 * njac[i][j][k][2][1];
                lhs[i][j][k][1][2][2] = 1.0 + tmp1 * 2.0 * njac[i][j][k][2][2] + tmp1 * 2.0 * dz3;
                lhs[i][j][k][1][2][3] = tmp1 * 2.0 * njac[i][j][k][2][3];
                lhs[i][j][k][1][2][4] = tmp1 * 2.0 * njac[i][j][k][2][4];
                lhs[i][j][k][1][3][0] = tmp1 * 2.0 * njac[i][j][k][3][0];
                lhs[i][j][k][1][3][1] = tmp1 * 2.0 * njac[i][j][k][3][1];
                lhs[i][j][k][1][3][2] = tmp1 * 2.0 * njac[i][j][k][3][2];
                lhs[i][j][k][1][3][3] = 1.0 + tmp1 * 2.0 * njac[i][j][k][3][3] + tmp1 * 2.0 * dz4;
                lhs[i][j][k][1][3][4] = tmp1 * 2.0 * njac[i][j][k][3][4];
                lhs[i][j][k][1][4][0] = tmp1 * 2.0 * njac[i][j][k][4][0];
                lhs[i][j][k][1][4][1] = tmp1 * 2.0 * njac[i][j][k][4][1];
                lhs[i][j][k][1][4][2] = tmp1 * 2.0 * njac[i][j][k][4][2];
                lhs[i][j][k][1][4][3] = tmp1 * 2.0 * njac[i][j][k][4][3];
                lhs[i][j][k][1][4][4] = 1.0 + tmp1 * 2.0 * njac[i][j][k][4][4] + tmp1 * 2.0 * dz5;
                lhs[i][j][k][2][0][0] = tmp2 * fjac[i][j][k + 1][0][0] - tmp1 * njac[i][j][k + 1][0][0] - tmp1 * dz1;
                lhs[i][j][k][2][0][1] = tmp2 * fjac[i][j][k + 1][0][1] - tmp1 * njac[i][j][k + 1][0][1];
                lhs[i][j][k][2][0][2] = tmp2 * fjac[i][j][k + 1][0][2] - tmp1 * njac[i][j][k + 1][0][2];
                lhs[i][j][k][2][0][3] = tmp2 * fjac[i][j][k + 1][0][3] - tmp1 * njac[i][j][k + 1][0][3];
                lhs[i][j][k][2][0][4] = tmp2 * fjac[i][j][k + 1][0][4] - tmp1 * njac[i][j][k + 1][0][4];
                lhs[i][j][k][2][1][0] = tmp2 * fjac[i][j][k + 1][1][0] - tmp1 * njac[i][j][k + 1][1][0];
                lhs[i][j][k][2][1][1] = tmp2 * fjac[i][j][k + 1][1][1] - tmp1 * njac[i][j][k + 1][1][1] - tmp1 * dz2;
                lhs[i][j][k][2][1][2] = tmp2 * fjac[i][j][k + 1][1][2] - tmp1 * njac[i][j][k + 1][1][2];
                lhs[i][j][k][2][1][3] = tmp2 * fjac[i][j][k + 1][1][3] - tmp1 * njac[i][j][k + 1][1][3];
                lhs[i][j][k][2][1][4] = tmp2 * fjac[i][j][k + 1][1][4] - tmp1 * njac[i][j][k + 1][1][4];
                lhs[i][j][k][2][2][0] = tmp2 * fjac[i][j][k + 1][2][0] - tmp1 * njac[i][j][k + 1][2][0];
                lhs[i][j][k][2][2][1] = tmp2 * fjac[i][j][k + 1][2][1] - tmp1 * njac[i][j][k + 1][2][1];
                lhs[i][j][k][2][2][2] = tmp2 * fjac[i][j][k + 1][2][2] - tmp1 * njac[i][j][k + 1][2][2] - tmp1 * dz3;
                lhs[i][j][k][2][2][3] = tmp2 * fjac[i][j][k + 1][2][3] - tmp1 * njac[i][j][k + 1][2][3];
                lhs[i][j][k][2][2][4] = tmp2 * fjac[i][j][k + 1][2][4] - tmp1 * njac[i][j][k + 1][2][4];
                lhs[i][j][k][2][3][0] = tmp2 * fjac[i][j][k + 1][3][0] - tmp1 * njac[i][j][k + 1][3][0];
                lhs[i][j][k][2][3][1] = tmp2 * fjac[i][j][k + 1][3][1] - tmp1 * njac[i][j][k + 1][3][1];
                lhs[i][j][k][2][3][2] = tmp2 * fjac[i][j][k + 1][3][2] - tmp1 * njac[i][j][k + 1][3][2];
                lhs[i][j][k][2][3][3] = tmp2 * fjac[i][j][k + 1][3][3] - tmp1 * njac[i][j][k + 1][3][3] - tmp1 * dz4;
                lhs[i][j][k][2][3][4] = tmp2 * fjac[i][j][k + 1][3][4] - tmp1 * njac[i][j][k + 1][3][4];
                lhs[i][j][k][2][4][0] = tmp2 * fjac[i][j][k + 1][4][0] - tmp1 * njac[i][j][k + 1][4][0];
                lhs[i][j][k][2][4][1] = tmp2 * fjac[i][j][k + 1][4][1] - tmp1 * njac[i][j][k + 1][4][1];
                lhs[i][j][k][2][4][2] = tmp2 * fjac[i][j][k + 1][4][2] - tmp1 * njac[i][j][k + 1][4][2];
                lhs[i][j][k][2][4][3] = tmp2 * fjac[i][j][k + 1][4][3] - tmp1 * njac[i][j][k + 1][4][3];
                lhs[i][j][k][2][4][4] = tmp2 * fjac[i][j][k + 1][4][4] - tmp1 * njac[i][j][k + 1][4][4] - tmp1 * dz5;
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    int i_imopVar125;
    int j_imopVar126;
    int k_imopVar127;
    int ksize;
    ksize = grid_points[2] - 1;
#pragma omp for nowait
    for (i_imopVar125 = 1; i_imopVar125 < grid_points[0] - 1; i_imopVar125++) {
        for (j_imopVar126 = 1; j_imopVar126 < grid_points[1] - 1; j_imopVar126++) {
            double ( *_imopVarPre418 );
            double ( *_imopVarPre419 )[5];
            double ( *_imopVarPre420 )[5];
            _imopVarPre418 = rhs[i_imopVar125][j_imopVar126][0];
            _imopVarPre419 = lhs[i_imopVar125][j_imopVar126][0][2];
            _imopVarPre420 = lhs[i_imopVar125][j_imopVar126][0][1];
            binvcrhs(_imopVarPre420, _imopVarPre419, _imopVarPre418);
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    for (k_imopVar127 = 1; k_imopVar127 < ksize; k_imopVar127++) {
#pragma omp for nowait
        for (i_imopVar125 = 1; i_imopVar125 < grid_points[0] - 1; i_imopVar125++) {
            for (j_imopVar126 = 1; j_imopVar126 < grid_points[1] - 1; j_imopVar126++) {
                double ( *_imopVarPre424 );
                double ( *_imopVarPre425 );
                double ( *_imopVarPre426 )[5];
                _imopVarPre424 = rhs[i_imopVar125][j_imopVar126][k_imopVar127];
                _imopVarPre425 = rhs[i_imopVar125][j_imopVar126][k_imopVar127 - 1];
                _imopVarPre426 = lhs[i_imopVar125][j_imopVar126][k_imopVar127][0];
                matvec_sub(_imopVarPre426, _imopVarPre425, _imopVarPre424);
                double ( *_imopVarPre430 )[5];
                double ( *_imopVarPre431 )[5];
                double ( *_imopVarPre432 )[5];
                _imopVarPre430 = lhs[i_imopVar125][j_imopVar126][k_imopVar127][1];
                _imopVarPre431 = lhs[i_imopVar125][j_imopVar126][k_imopVar127 - 1][2];
                _imopVarPre432 = lhs[i_imopVar125][j_imopVar126][k_imopVar127][0];
                matmul_sub(_imopVarPre432, _imopVarPre431, _imopVarPre430);
                double ( *_imopVarPre436 );
                double ( *_imopVarPre437 )[5];
                double ( *_imopVarPre438 )[5];
                _imopVarPre436 = rhs[i_imopVar125][j_imopVar126][k_imopVar127];
                _imopVarPre437 = lhs[i_imopVar125][j_imopVar126][k_imopVar127][2];
                _imopVarPre438 = lhs[i_imopVar125][j_imopVar126][k_imopVar127][1];
                binvcrhs(_imopVarPre438, _imopVarPre437, _imopVarPre436);
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    }
#pragma omp for nowait
    for (i_imopVar125 = 1; i_imopVar125 < grid_points[0] - 1; i_imopVar125++) {
        for (j_imopVar126 = 1; j_imopVar126 < grid_points[1] - 1; j_imopVar126++) {
            double ( *_imopVarPre442 );
            double ( *_imopVarPre443 );
            double ( *_imopVarPre444 )[5];
            _imopVarPre442 = rhs[i_imopVar125][j_imopVar126][ksize];
            _imopVarPre443 = rhs[i_imopVar125][j_imopVar126][ksize - 1];
            _imopVarPre444 = lhs[i_imopVar125][j_imopVar126][ksize][0];
            matvec_sub(_imopVarPre444, _imopVarPre443, _imopVarPre442);
            double ( *_imopVarPre448 )[5];
            double ( *_imopVarPre449 )[5];
            double ( *_imopVarPre450 )[5];
            _imopVarPre448 = lhs[i_imopVar125][j_imopVar126][ksize][1];
            _imopVarPre449 = lhs[i_imopVar125][j_imopVar126][ksize - 1][2];
            _imopVarPre450 = lhs[i_imopVar125][j_imopVar126][ksize][0];
            matmul_sub(_imopVarPre450, _imopVarPre449, _imopVarPre448);
            double ( *_imopVarPre453 );
            double ( *_imopVarPre454 )[5];
            _imopVarPre453 = rhs[i_imopVar125][j_imopVar126][ksize];
            _imopVarPre454 = lhs[i_imopVar125][j_imopVar126][ksize][1];
            binvrhs(_imopVarPre454, _imopVarPre453);
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    int i_imopVar103;
    int j_imopVar104;
    int k_imopVar105;
    int m;
    int n;
#pragma omp for nowait
    for (i_imopVar103 = 1; i_imopVar103 < grid_points[0] - 1; i_imopVar103++) {
        for (j_imopVar104 = 1; j_imopVar104 < grid_points[1] - 1; j_imopVar104++) {
            for (k_imopVar105 = grid_points[2] - 2; k_imopVar105 >= 0; k_imopVar105--) {
                for (m = 0; m < 5; m++) {
                    for (n = 0; n < 5; n++) {
                        rhs[i_imopVar103][j_imopVar104][k_imopVar105][m] = rhs[i_imopVar103][j_imopVar104][k_imopVar105][m] - lhs[i_imopVar103][j_imopVar104][k_imopVar105][2][m][n] * rhs[i_imopVar103][j_imopVar104][k_imopVar105 + 1][n];
                    }
                }
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
}
static void z_backsubstitute(void ) {
    int i;
    int j;
    int k;
    int m;
    int n;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            for (k = grid_points[2] - 2; k >= 0; k--) {
                for (m = 0; m < 5; m++) {
                    for (n = 0; n < 5; n++) {
                        rhs[i][j][k][m] = rhs[i][j][k][m] - lhs[i][j][k][2][m][n] * rhs[i][j][k + 1][n];
                    }
                }
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written([rhs.f]) read([])
#pragma omp barrier
}
static void z_solve_cell(void ) {
    int i;
    int j;
    int k;
    int ksize;
    ksize = grid_points[2] - 1;
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            double ( *_imopVarPre418 );
            double ( *_imopVarPre419 )[5];
            double ( *_imopVarPre420 )[5];
            _imopVarPre418 = rhs[i][j][0];
            _imopVarPre419 = lhs[i][j][0][2];
            _imopVarPre420 = lhs[i][j][0][1];
            binvcrhs(_imopVarPre420, _imopVarPre419, _imopVarPre418);
        }
    }
// #pragma omp dummyFlush BARRIER_START written([lhs.f, rhs.f]) read([lhs.f, matvec_sub, binvcrhs, grid_points, grid_points.f, binvrhs, lhs, rhs, i, matmul_sub, rhs.f])
#pragma omp barrier
    for (k = 1; k < ksize; k++) {
#pragma omp for nowait
        for (i = 1; i < grid_points[0] - 1; i++) {
            for (j = 1; j < grid_points[1] - 1; j++) {
                double ( *_imopVarPre424 );
                double ( *_imopVarPre425 );
                double ( *_imopVarPre426 )[5];
                _imopVarPre424 = rhs[i][j][k];
                _imopVarPre425 = rhs[i][j][k - 1];
                _imopVarPre426 = lhs[i][j][k][0];
                matvec_sub(_imopVarPre426, _imopVarPre425, _imopVarPre424);
                double ( *_imopVarPre430 )[5];
                double ( *_imopVarPre431 )[5];
                double ( *_imopVarPre432 )[5];
                _imopVarPre430 = lhs[i][j][k][1];
                _imopVarPre431 = lhs[i][j][k - 1][2];
                _imopVarPre432 = lhs[i][j][k][0];
                matmul_sub(_imopVarPre432, _imopVarPre431, _imopVarPre430);
                double ( *_imopVarPre436 );
                double ( *_imopVarPre437 )[5];
                double ( *_imopVarPre438 )[5];
                _imopVarPre436 = rhs[i][j][k];
                _imopVarPre437 = lhs[i][j][k][2];
                _imopVarPre438 = lhs[i][j][k][1];
                binvcrhs(_imopVarPre438, _imopVarPre437, _imopVarPre436);
            }
        }
// #pragma omp dummyFlush BARRIER_START written([lhs.f, rhs.f]) read([lhs.f, matvec_sub, binvcrhs, grid_points, grid_points.f, binvrhs, lhs, rhs, i, matmul_sub, rhs.f])
#pragma omp barrier
    }
#pragma omp for nowait
    for (i = 1; i < grid_points[0] - 1; i++) {
        for (j = 1; j < grid_points[1] - 1; j++) {
            double ( *_imopVarPre442 );
            double ( *_imopVarPre443 );
            double ( *_imopVarPre444 )[5];
            _imopVarPre442 = rhs[i][j][ksize];
            _imopVarPre443 = rhs[i][j][ksize - 1];
            _imopVarPre444 = lhs[i][j][ksize][0];
            matvec_sub(_imopVarPre444, _imopVarPre443, _imopVarPre442);
            double ( *_imopVarPre448 )[5];
            double ( *_imopVarPre449 )[5];
            double ( *_imopVarPre450 )[5];
            _imopVarPre448 = lhs[i][j][ksize][1];
            _imopVarPre449 = lhs[i][j][ksize - 1][2];
            _imopVarPre450 = lhs[i][j][ksize][0];
            matmul_sub(_imopVarPre450, _imopVarPre449, _imopVarPre448);
            double ( *_imopVarPre453 );
            double ( *_imopVarPre454 )[5];
            _imopVarPre453 = rhs[i][j][ksize];
            _imopVarPre454 = lhs[i][j][ksize][1];
            binvrhs(_imopVarPre454, _imopVarPre453);
        }
    }
// #pragma omp dummyFlush BARRIER_START written([lhs.f, rhs.f]) read([lhs.f, grid_points, grid_points.f, lhs, rhs, i, rhs.f, z_backsubstitute])
#pragma omp barrier
}
