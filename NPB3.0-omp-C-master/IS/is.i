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
typedef union stUn_imopVar75 {
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
typedef enum enum_imopVar76 {
    P_ALL, P_PID , P_PGID
} idtype_t;
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
typedef __darwin_size_t size_t;
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

/*writes(signal; );*/
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

/*writes(getpriority; );*/
int getpriority(int , id_t );

/*writes(getiopolicy_np; );*/
int getiopolicy_np(int , int );

/*writes(getrlimit; );*/
int getrlimit(int , struct rlimit *);

/*writes(getrusage; );*/
int getrusage(int , struct rusage *);

/*writes(setpriority; );*/
int setpriority(int , id_t  , int );

/*writes(setiopolicy_np; );*/
int setiopolicy_np(int , int  , int );

/*writes(setrlimit; );*/
int setrlimit(int , const struct rlimit *);

/*writes(_data; );*/
static inline __uint16_t _OSSwapInt16(__uint16_t _data) {
    
    /*reads(_data; _data; ); */
    return ((__uint16_t) ((_data << 8) | (_data >> 8)));
}

/*writes(_data; );*/
static inline __uint32_t _OSSwapInt32(__uint32_t _data) {
    ;
    
    /*reads(_data; ); */
    return _data;
}

/*writes(_data; );*/
static inline __uint64_t _OSSwapInt64(__uint64_t _data) {
    ;
    
    /*reads(_data; ); */
    return _data;
}
union wait {
    int w_status;
    struct stUn_imopVar77 {
        unsigned int w_Termsig: 7, w_Coredump: 1 , w_Retcode: 8 , w_Filler: 16;
    } w_T;
    struct stUn_imopVar78 {
        unsigned int w_Stopval: 8, w_Stopsig: 8 , w_Filler: 16;
    } w_S;
} ;

/*writes(wait; );*/
pid_t wait(int *);

/*writes(waitpid; );*/
pid_t waitpid(pid_t , int * , int );

/*writes(waitid; );*/
int waitid(idtype_t , id_t  , siginfo_t * , int );

/*writes(wait3; );*/
pid_t wait3(int *, int  , struct rusage *);

/*writes(wait4; );*/
pid_t wait4(pid_t , int * , int  , struct rusage *);

/*writes(alloca; );*/
void *alloca(size_t );
typedef __darwin_ct_rune_t ct_rune_t;
typedef __darwin_rune_t rune_t;
typedef __darwin_wchar_t wchar_t;
typedef struct stUn_imopVar79 {
    int quot;
    int rem;
} div_t;
typedef struct stUn_imopVar80 {
    long quot;
    long rem;
} ldiv_t;
typedef struct stUn_imopVar81 {
    long long quot;
    long long rem;
} lldiv_t;

/*writes(__mb_cur_max; );*/
extern int __mb_cur_max;

/*writes(abort; );*/
void abort(void );

/*writes(abs; );*/
int abs(int );

/*writes(atexit; );*/
int atexit(void (*)(void ));

/*writes(atof; );*/
double atof(const char *);

/*writes(atoi; );*/
int atoi(const char *);

/*writes(atol; );*/
long atol(const char *);

/*writes(atoll; );*/
long long atoll(const char *);

/*writes(bsearch; );*/
void *bsearch(const void *__key, const void *__base , size_t __nel , size_t __width , int ( *__compar )(const void *, const void *));

/*writes(calloc; );*/
void *calloc(size_t __count, size_t __size);

/*writes(div; );*/
div_t div(int , int );

/*writes(exit; );*/
void exit(int );

/*writes(free; );*/
void free(void *);

/*writes(getenv; );*/
char *getenv(const char *);

/*writes(labs; );*/
long labs(long );

/*writes(ldiv; );*/
ldiv_t ldiv(long , long );

/*writes(llabs; );*/
long long llabs(long long );

/*writes(lldiv; );*/
lldiv_t lldiv(long long , long long );

/*writes(malloc; );*/
void *malloc(size_t __size);

/*writes(mblen; );*/
int mblen(const char *__s, size_t __n);

/*writes(mbstowcs; );*/
size_t mbstowcs(wchar_t *restrict, const char *restrict , size_t );

/*writes(mbtowc; );*/
int mbtowc(wchar_t *restrict, const char *restrict , size_t );

/*writes(posix_memalign; );*/
int posix_memalign(void **__memptr, size_t __alignment , size_t __size);

/*writes(qsort; );*/
void qsort(void *__base, size_t __nel , size_t __width , int ( *__compar )(const void *, const void *));

/*writes(rand; );*/
int rand(void );

/*writes(realloc; );*/
void *realloc(void *__ptr, size_t __size);

/*writes(srand; );*/
void srand(unsigned );

/*writes(strtod; );*/
double strtod(const char *, char **);

/*writes(strtof; );*/
float strtof(const char *, char **);

/*writes(strtol; );*/
long strtol(const char *__str, char **__endptr , int __base);

/*writes(strtold; );*/
long double strtold(const char *, char **);

/*writes(strtoll; );*/
long long strtoll(const char *__str, char **__endptr , int __base);

/*writes(strtoul; );*/
unsigned long strtoul(const char *__str, char **__endptr , int __base);

/*writes(strtoull; );*/
unsigned long long strtoull(const char *__str, char **__endptr , int __base);

/*writes(system; );*/
int system(const char *);

/*writes(wcstombs; );*/
size_t wcstombs(char *restrict, const wchar_t *restrict , size_t );

/*writes(wctomb; );*/
int wctomb(char *, wchar_t );

/*writes(_Exit; );*/
void _Exit(int );

/*writes(a64l; );*/
long a64l(const char *);

/*writes(drand48; );*/
double drand48(void );

/*writes(ecvt; );*/
char *ecvt(double , int  , int *restrict , int *restrict);

/*writes(erand48; );*/
double erand48(unsigned short [3]);

/*writes(fcvt; );*/
char *fcvt(double , int  , int *restrict , int *restrict);

/*writes(gcvt; );*/
char *gcvt(double , int  , char *);

/*writes(getsubopt; );*/
int getsubopt(char **, char *const* , char **);

/*writes(grantpt; );*/
int grantpt(int );

/*writes(initstate; );*/
char *initstate(unsigned , char * , size_t );

/*writes(jrand48; );*/
long jrand48(unsigned short [3]);

/*writes(l64a; );*/
char *l64a(long );

/*writes(lcong48; );*/
void lcong48(unsigned short [7]);

/*writes(lrand48; );*/
long lrand48(void );

/*writes(mktemp; );*/
char *mktemp(char *);

/*writes(mkstemp; );*/
int mkstemp(char *);

/*writes(mrand48; );*/
long mrand48(void );

/*writes(nrand48; );*/
long nrand48(unsigned short [3]);

/*writes(posix_openpt; );*/
int posix_openpt(int );

/*writes(ptsname; );*/
char *ptsname(int );

/*writes(putenv; );*/
int putenv(char *);

/*writes(random; );*/
long random(void );

/*writes(rand_r; );*/
int rand_r(unsigned *);

/*writes(realpath; );*/
char *realpath(const char *restrict, char *restrict);

/*writes(seed48; );*/
unsigned short *seed48(unsigned short [3]);

/*writes(setenv; );*/
int setenv(const char *__name, const char *__value , int __overwrite);

/*writes(setkey; );*/
void setkey(const char *);

/*writes(setstate; );*/
char *setstate(const char *);

/*writes(srand48; );*/
void srand48(long );

/*writes(srandom; );*/
void srandom(unsigned );

/*writes(unlockpt; );*/
int unlockpt(int );

/*writes(unsetenv; );*/
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

/*writes(arc4random; );*/
uint32_t arc4random(void );

/*writes(arc4random_addrandom; );*/
void arc4random_addrandom(unsigned char *, int );

/*writes(arc4random_buf; );*/
void arc4random_buf(void *__buf, size_t __nbytes);

/*writes(arc4random_stir; );*/
void arc4random_stir(void );

/*writes(arc4random_uniform; );*/
uint32_t arc4random_uniform(uint32_t __upper_bound);

/*writes(cgetcap; );*/
char *cgetcap(char *, const char * , int );

/*writes(cgetclose; );*/
int cgetclose(void );

/*writes(cgetent; );*/
int cgetent(char **, char ** , const char *);

/*writes(cgetfirst; );*/
int cgetfirst(char **, char **);

/*writes(cgetmatch; );*/
int cgetmatch(const char *, const char *);

/*writes(cgetnext; );*/
int cgetnext(char **, char **);

/*writes(cgetnum; );*/
int cgetnum(char *, const char * , long *);

/*writes(cgetset; );*/
int cgetset(const char *);

/*writes(cgetstr; );*/
int cgetstr(char *, const char * , char **);

/*writes(cgetustr; );*/
int cgetustr(char *, const char * , char **);

/*writes(daemon; );*/
int daemon(int , int );

/*writes(devname; );*/
char *devname(dev_t , mode_t );

/*writes(devname_r; );*/
char *devname_r(dev_t , mode_t  , char *buf , int len);

/*writes(getbsize; );*/
char *getbsize(int *, long *);

/*writes(getloadavg; );*/
int getloadavg(double [], int );

/*writes(getprogname; );*/
const char *getprogname(void );

/*writes(heapsort; );*/
int heapsort(void *__base, size_t __nel , size_t __width , int ( *__compar )(const void *, const void *));

/*writes(mergesort; );*/
int mergesort(void *__base, size_t __nel , size_t __width , int ( *__compar )(const void *, const void *));

/*writes(psort; );*/
void psort(void *__base, size_t __nel , size_t __width , int ( *__compar )(const void *, const void *));

/*writes(psort_r; );*/
void psort_r(void *__base, size_t __nel , size_t __width , void * , int ( *__compar )(void *, const void * , const void *));

/*writes(qsort_r; );*/
void qsort_r(void *__base, size_t __nel , size_t __width , void * , int ( *__compar )(void *, const void * , const void *));

/*writes(radixsort; );*/
int radixsort(const unsigned char **__base, int __nel , const unsigned char *__table , unsigned __endbyte);

/*writes(setprogname; );*/
void setprogname(const char *);

/*writes(sradixsort; );*/
int sradixsort(const unsigned char **__base, int __nel , const unsigned char *__table , unsigned __endbyte);

/*writes(sranddev; );*/
void sranddev(void );

/*writes(srandomdev; );*/
void srandomdev(void );

/*writes(reallocf; );*/
void *reallocf(void *__ptr, size_t __size);

/*writes(strtoq; );*/
long long strtoq(const char *__str, char **__endptr , int __base);

/*writes(strtouq; );*/
unsigned long long strtouq(const char *__str, char **__endptr , int __base);

/*writes(suboptarg; );*/
extern char *suboptarg;

/*writes(valloc; );*/
void *valloc(size_t );
typedef __darwin_va_list va_list;

/*writes(renameat; );*/
int renameat(int , const char * , int  , const char *);

/*writes(renamex_np; );*/
int renamex_np(const char *, const char * , unsigned int );

/*writes(renameatx_np; );*/
int renameatx_np(int , const char * , int  , const char * , unsigned int );
typedef __darwin_off_t fpos_t;
struct __sbuf {
    unsigned char *_base;
    int _size;
} ;
struct __sFILEX ;
typedef struct __sFILE {
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
} FILE;

/*writes(__stdinp; );*/
extern FILE *__stdinp;

/*writes(__stdoutp; );*/
extern FILE *__stdoutp;

/*writes(__stderrp; );*/
extern FILE *__stderrp;

/*writes(clearerr; );*/
void clearerr(FILE *);

/*writes(fclose; );*/
int fclose(FILE *);

/*writes(feof; );*/
int feof(FILE *);

/*writes(ferror; );*/
int ferror(FILE *);

/*writes(fflush; );*/
int fflush(FILE *);

/*writes(fgetc; );*/
int fgetc(FILE *);

/*writes(fgetpos; );*/
int fgetpos(FILE *restrict, fpos_t *);

/*writes(fgets; );*/
char *fgets(char *restrict, int  , FILE *);

/*writes(fopen; );*/
FILE *fopen(const char *restrict__filename, const char *restrict__mode);

/*writes(fprintf; );*/
int fprintf(FILE *restrict, const char *restrict, ...);

/*writes(fputc; );*/
int fputc(int , FILE *);

/*writes(fputs; );*/
int fputs(const char *restrict, FILE *restrict);

/*writes(fread; );*/
size_t fread(void *restrict__ptr, size_t __size , size_t __nitems , FILE *restrict__stream);

/*writes(freopen; );*/
FILE *freopen(const char *restrict, const char *restrict , FILE *restrict);

/*writes(fscanf; );*/
int fscanf(FILE *restrict, const char *restrict, ...);

/*writes(fseek; );*/
int fseek(FILE *, long  , int );

/*writes(fsetpos; );*/
int fsetpos(FILE *, const fpos_t *);

/*writes(ftell; );*/
long ftell(FILE *);

/*writes(fwrite; );*/
size_t fwrite(const void *restrict__ptr, size_t __size , size_t __nitems , FILE *restrict__stream);

/*writes(getc; );*/
int getc(FILE *);

/*writes(getchar; );*/
int getchar(void );

/*writes(gets; );*/
char *gets(char *);

/*writes(perror; );*/
void perror(const char *);

/*writes(printf; );*/
int printf(const char *restrict, ...);

/*writes(putc; );*/
int putc(int , FILE *);

/*writes(putchar; );*/
int putchar(int );

/*writes(puts; );*/
int puts(const char *);

/*writes(remove; );*/
int remove(const char *);

/*writes(rename; );*/
int rename(const char *__old, const char *__new);

/*writes(rewind; );*/
void rewind(FILE *);

/*writes(scanf; );*/
int scanf(const char *restrict, ...);

/*writes(setbuf; );*/
void setbuf(FILE *restrict, char *restrict);

/*writes(setvbuf; );*/
int setvbuf(FILE *restrict, char *restrict , int  , size_t );

/*writes(sprintf; );*/
int sprintf(char *restrict, const char *restrict, ...);

/*writes(sscanf; );*/
int sscanf(const char *restrict, const char *restrict, ...);

/*writes(tmpfile; );*/
FILE *tmpfile(void );

/*writes(tmpnam; );*/
char *tmpnam(char *);

/*writes(ungetc; );*/
int ungetc(int , FILE *);

/*writes(vfprintf; );*/
int vfprintf(FILE *restrict, const char *restrict , va_list );

/*writes(vprintf; );*/
int vprintf(const char *restrict, va_list );

/*writes(vsprintf; );*/
int vsprintf(char *restrict, const char *restrict , va_list );

/*writes(ctermid; );*/
char *ctermid(char *);

/*writes(fdopen; );*/
FILE *fdopen(int , const char *);

/*writes(fileno; );*/
int fileno(FILE *);

/*writes(pclose; );*/
int pclose(FILE *);

/*writes(popen; );*/
FILE *popen(const char *, const char *);

/*writes(__srget; );*/
int __srget(FILE *);

/*writes(__svfscanf; );*/
int __svfscanf(FILE *, const char * , va_list );

/*writes(__swbuf; );*/
int __swbuf(int , FILE *);

/*writes(_c; );*/

/*writes(_p; );*/
extern __inline int __sputc(int _c, FILE *_p) {
    {
        
        /*writes(_imopVar82; );*/
        signed int _imopVar82;
        
        /*writes(_imopVar86; );*/
        signed int _imopVar86;
        
        /*reads(_p; (generic_stack); (generic_stack); ); writes(_imopVar82; generic_stack; );*/
        _imopVar82 = --_p->_w >= 0;
        
        /*reads(_imopVar82; ); */
        if (!_imopVar82) {
            
            /*reads(_p; (generic_stack); (generic_stack); _p; (generic_stack); (generic_stack); ); writes(_imopVar86; );*/
            _imopVar86 = _p->_w >= _p->_lbfsize;
            
            /*reads(_imopVar86; ); */
            if (_imopVar86) {
                
                /*reads(_c; ); writes(_imopVar86; );*/
                _imopVar86 = (char) _c != '\n';
            }
            
            /*reads(_imopVar86; ); writes(_imopVar82; );*/
            _imopVar82 = _imopVar86;
        }
        
        /*reads(_imopVar82; ); */
        if (_imopVar82)
            
        /*reads(_p; (generic_stack); (generic_stack); _c; ); writes(generic_stack; );*/
        return (*_p->_p++ = _c);
        else {
            
            /*writes(_imopVar88; );*/
            signed int _imopVar88;
            
            /*reads(_c; _p; ); writes(_imopVar88; );*/
            _imopVar88 = __swbuf(_c, _p);
            
            /*reads(_imopVar88; ); */
            return (_imopVar88);
        }
    }
}

/*writes(flockfile; );*/
void flockfile(FILE *);

/*writes(ftrylockfile; );*/
int ftrylockfile(FILE *);

/*writes(funlockfile; );*/
void funlockfile(FILE *);

/*writes(getc_unlocked; );*/
int getc_unlocked(FILE *);

/*writes(getchar_unlocked; );*/
int getchar_unlocked(void );

/*writes(putc_unlocked; );*/
int putc_unlocked(int , FILE *);

/*writes(putchar_unlocked; );*/
int putchar_unlocked(int );

/*writes(getw; );*/
int getw(FILE *);

/*writes(putw; );*/
int putw(int , FILE *);

/*writes(tempnam; );*/
char *tempnam(const char *__dir, const char *__prefix);
typedef __darwin_off_t off_t;

/*writes(fseeko; );*/
int fseeko(FILE *__stream, off_t __offset , int __whence);

/*writes(ftello; );*/
off_t ftello(FILE *__stream);

/*writes(snprintf; );*/
int snprintf(char *restrict__str, size_t __size , const char *restrict__format, ...);

/*writes(vfscanf; );*/
int vfscanf(FILE *restrict__stream, const char *restrict__format , va_list );

/*writes(vscanf; );*/
int vscanf(const char *restrict__format, va_list );

/*writes(vsnprintf; );*/
int vsnprintf(char *restrict__str, size_t __size , const char *restrict__format , va_list );

/*writes(vsscanf; );*/
int vsscanf(const char *restrict__str, const char *restrict__format , va_list );
typedef __darwin_ssize_t ssize_t;

/*writes(dprintf; );*/
int dprintf(int , const char *restrict, ...);

/*writes(vdprintf; );*/
int vdprintf(int , const char *restrict , va_list );

/*writes(getdelim; );*/
ssize_t getdelim(char **restrict__linep, size_t *restrict__linecapp , int __delimiter , FILE *restrict__stream);

/*writes(getline; );*/
ssize_t getline(char **restrict__linep, size_t *restrict__linecapp , FILE *restrict__stream);

/*writes(sys_nerr; );*/
extern const int sys_nerr;

/*writes(constsys_errlist; );*/
extern const char *constsys_errlist[];

/*writes(asprintf; );*/
int asprintf(char **restrict, const char *restrict, ...);

/*writes(ctermid_r; );*/
char *ctermid_r(char *);

/*writes(fgetln; );*/
char *fgetln(FILE *, size_t *);

/*writes(fmtcheck; );*/
const char *fmtcheck(const char *, const char *);

/*writes(fpurge; );*/
int fpurge(FILE *);

/*writes(setbuffer; );*/
void setbuffer(FILE *, char * , int );

/*writes(setlinebuf; );*/
int setlinebuf(FILE *);

/*writes(vasprintf; );*/
int vasprintf(char **restrict, const char *restrict , va_list );

/*writes(zopen; );*/
FILE *zopen(const char *, const char * , int );

/*writes(funopen; );*/
FILE *funopen(const void *, int (*)(void *, char * , int ) , int (*)(void *, const char * , int ) , fpos_t (*)(void *, fpos_t  , int ) , int (*)(void *));

/*writes(__sprintf_chk; );*/
extern int __sprintf_chk(char *restrict, int  , size_t  , const char *restrict, ...);

/*writes(__snprintf_chk; );*/
extern int __snprintf_chk(char *restrict, size_t  , int  , size_t  , const char *restrict, ...);

/*writes(__vsprintf_chk; );*/
extern int __vsprintf_chk(char *restrict, int  , size_t  , const char *restrict , va_list );

/*writes(__vsnprintf_chk; );*/
extern int __vsnprintf_chk(char *restrict, size_t  , int  , size_t  , const char *restrict , va_list );

/*writes(timer_clear; );*/
extern void timer_clear(int );

/*writes(timer_start; );*/
extern void timer_start(int );

/*writes(timer_stop; );*/
extern void timer_stop(int );

/*writes(timer_read; );*/
extern double timer_read(int );

/*writes(c_print_results; );*/
extern void c_print_results(char *name, char class , int n1 , int n2 , int n3 , int niter , int nthreads , double t , double mops , char *optype , int passed_verification , char *npbversion , char *compiletime , char *cc , char *clink , char *c_lib , char *c_inc , char *cflags , char *clinkflags , char *rand);
typedef int INT_TYPE;

/*writes(key_buff_ptr_global; );*/
INT_TYPE *key_buff_ptr_global;

/*writes(passed_verification; );*/
int passed_verification;

/*writes(key_array; );*/
INT_TYPE key_array[(1 << 16)];

/*writes(key_buff1; );*/
INT_TYPE key_buff1[(1 << 16)];

/*writes(key_buff2; );*/
INT_TYPE key_buff2[(1 << 16)];

/*writes(partial_verify_vals; );*/
INT_TYPE partial_verify_vals[5];

/*writes(test_index_array; );*/
INT_TYPE test_index_array[5];

/*writes(test_rank_array; );*/
INT_TYPE test_rank_array[5];

/*writes(S_test_index_array; );*/
INT_TYPE S_test_index_array[5] = {48427, 17148 , 23627 , 62548 , 4431};

/*writes(S_test_rank_array; );*/
INT_TYPE S_test_rank_array[5] = {0, 18 , 346 , 64917 , 65463};

/*writes(W_test_index_array; );*/
INT_TYPE W_test_index_array[5] = {357773, 934767 , 875723 , 898999 , 404505};

/*writes(W_test_rank_array; );*/
INT_TYPE W_test_rank_array[5] = {1249, 11698 , 1039987 , 1043896 , 1048018};

/*writes(A_test_index_array; );*/
INT_TYPE A_test_index_array[5] = {2112377, 662041 , 5336171 , 3642833 , 4250760};

/*writes(A_test_rank_array; );*/
INT_TYPE A_test_rank_array[5] = {104, 17523 , 123928 , 8288932 , 8388264};

/*writes(B_test_index_array; );*/
INT_TYPE B_test_index_array[5] = {41869, 812306 , 5102857 , 18232239 , 26860214};

/*writes(B_test_rank_array; );*/
INT_TYPE B_test_rank_array[5] = {33422937, 10244 , 59149 , 33135281 , 99};

/*writes(C_test_index_array; );*/
INT_TYPE C_test_index_array[5] = {44172927, 72999161 , 74326391 , 129606274 , 21736814};

/*writes(C_test_rank_array; );*/
INT_TYPE C_test_rank_array[5] = {61147, 882988 , 266290 , 133997595 , 133525895};

/*writes(randlc; );*/
double randlc(double *X, double *A);

/*writes(full_verify; );*/
void full_verify(void );

/*writes(X; );*/

/*writes(A; );*/
double randlc(double *X, double *A) {
    
    /*writes(KS; );*/
    static int KS = 0;
    
    /*writes(R23; );*/
    static double R23;
    
    /*writes(R46; );*/
    static double R46;
    
    /*writes(T23; );*/
    static double T23;
    
    /*writes(T46; );*/
    static double T46;
    
    /*writes(T1; );*/
    double T1;
    
    /*writes(T2; );*/
    double T2;
    
    /*writes(T3; );*/
    double T3;
    
    /*writes(T4; );*/
    double T4;
    
    /*writes(A1; );*/
    double A1;
    
    /*writes(A2; );*/
    double A2;
    
    /*writes(X1; );*/
    double X1;
    
    /*writes(X2; );*/
    double X2;
    
    /*writes(Z; );*/
    double Z;
    
    /*writes(i; );*/
    int i;
    
    /*writes(j; );*/
    int j;
    {
        
        /*reads(KS; ); */
        if (KS == 0) {
            
            /*writes(R23; );*/
            R23 = 1.0;
            
            /*writes(R46; );*/
            R46 = 1.0;
            
            /*writes(T23; );*/
            T23 = 1.0;
            
            /*writes(T46; );*/
            T46 = 1.0;
            {
                for (i = 1; i <= 23; i++) {
                    {
                        
                        /*reads(R23; ); writes(R23; );*/
                        R23 = 0.50 * R23;
                        
                        /*reads(T23; ); writes(T23; );*/
                        T23 = 2.0 * T23;
                    }
                }
            }
            {
                for (i = 1; i <= 46; i++) {
                    {
                        
                        /*reads(R46; ); writes(R46; );*/
                        R46 = 0.50 * R46;
                        
                        /*reads(T46; ); writes(T46; );*/
                        T46 = 2.0 * T46;
                    }
                }
            }
            
            /*writes(KS; );*/
            KS = 1;
        }
    }
    
    /*reads(R23; A; (generic_stack); ); writes(T1; );*/
    T1 = R23 * *A;
    
    /*reads(T1; ); writes(j; );*/
    j = T1;
    
    /*reads(j; ); writes(A1; );*/
    A1 = j;
    
    /*reads(A; (generic_stack); T23; A1; ); writes(A2; );*/
    A2 = *A - T23 * A1;
    
    /*reads(R23; X; (generic_stack); ); writes(T1; );*/
    T1 = R23 * *X;
    
    /*reads(T1; ); writes(j; );*/
    j = T1;
    
    /*reads(j; ); writes(X1; );*/
    X1 = j;
    
    /*reads(X; (generic_stack); T23; X1; ); writes(X2; );*/
    X2 = *X - T23 * X1;
    
    /*reads(A1; X2; A2; X1; ); writes(T1; );*/
    T1 = A1 * X2 + A2 * X1;
    
    /*reads(R23; T1; ); writes(j; );*/
    j = R23 * T1;
    
    /*reads(j; ); writes(T2; );*/
    T2 = j;
    
    /*reads(T1; T23; T2; ); writes(Z; );*/
    Z = T1 - T23 * T2;
    
    /*reads(T23; Z; A2; X2; ); writes(T3; );*/
    T3 = T23 * Z + A2 * X2;
    
    /*reads(R46; T3; ); writes(j; );*/
    j = R46 * T3;
    
    /*reads(j; ); writes(T4; );*/
    T4 = j;
    
    /*reads(X; T3; T46; T4; ); writes(generic_stack; );*/
    *X = T3 - T46 * T4;
    
    /*reads(R46; X; (generic_stack); ); */
    return (R46 * *X);
}

/*writes(seed; );*/

/*writes(a; );*/
void create_seq(double seed, double a) {
    
    /*writes(x; );*/
    double x;
    
    /*writes(i; );*/
    int i;
    
    /*writes(j; );*/
    int j;
    
    /*writes(k; );*/
    int k;
    
    /*writes(k; );*/
    k = (1 << 11) / 4;
    {
        for (i = 0; i < (1 << 16); i++) {
            {
                {
                    
                    /*writes(_imopVar91; );*/
                    double *_imopVar91;
                    
                    /*writes(_imopVar92; );*/
                    double *_imopVar92;
                    
                    /*writes(_imopVar93; );*/
                    double _imopVar93;
                    
                    /*reads((&):a; ); writes(_imopVar91; );*/
                    _imopVar91 = &a;
                    
                    /*reads((&):seed; ); writes(_imopVar92; );*/
                    _imopVar92 = &seed;
                    
                    /*reads(_imopVar92; _imopVar91; ); writes(_imopVar93; );*/
                    _imopVar93 = randlc(_imopVar92, _imopVar91);
                    
                    /*reads(_imopVar93; ); writes(x; );*/
                    x = _imopVar93;
                }
                {
                    
                    /*writes(_imopVar96; );*/
                    double *_imopVar96;
                    
                    /*writes(_imopVar97; );*/
                    double *_imopVar97;
                    
                    /*writes(_imopVar98; );*/
                    double _imopVar98;
                    
                    /*reads((&):a; ); writes(_imopVar96; );*/
                    _imopVar96 = &a;
                    
                    /*reads((&):seed; ); writes(_imopVar97; );*/
                    _imopVar97 = &seed;
                    
                    /*reads(_imopVar97; _imopVar96; ); writes(_imopVar98; );*/
                    _imopVar98 = randlc(_imopVar97, _imopVar96);
                    
                    /*reads(x; _imopVar98; ); writes(x; );*/
                    x += _imopVar98;
                }
                {
                    
                    /*writes(_imopVar101; );*/
                    double *_imopVar101;
                    
                    /*writes(_imopVar102; );*/
                    double *_imopVar102;
                    
                    /*writes(_imopVar103; );*/
                    double _imopVar103;
                    
                    /*reads((&):a; ); writes(_imopVar101; );*/
                    _imopVar101 = &a;
                    
                    /*reads((&):seed; ); writes(_imopVar102; );*/
                    _imopVar102 = &seed;
                    
                    /*reads(_imopVar102; _imopVar101; ); writes(_imopVar103; );*/
                    _imopVar103 = randlc(_imopVar102, _imopVar101);
                    
                    /*reads(x; _imopVar103; ); writes(x; );*/
                    x += _imopVar103;
                }
                {
                    
                    /*writes(_imopVar106; );*/
                    double *_imopVar106;
                    
                    /*writes(_imopVar107; );*/
                    double *_imopVar107;
                    
                    /*writes(_imopVar108; );*/
                    double _imopVar108;
                    
                    /*reads((&):a; ); writes(_imopVar106; );*/
                    _imopVar106 = &a;
                    
                    /*reads((&):seed; ); writes(_imopVar107; );*/
                    _imopVar107 = &seed;
                    
                    /*reads(_imopVar107; _imopVar106; ); writes(_imopVar108; );*/
                    _imopVar108 = randlc(_imopVar107, _imopVar106);
                    
                    /*reads(x; _imopVar108; ); writes(x; );*/
                    x += _imopVar108;
                }
                
                /*reads(key_array; i; k; x; ); writes(generic_stack; generic_stack; );*/
                key_array[i] = k * x;
            }
        }
    }
}
void full_verify() {
    
    /*writes(i; );*/
    INT_TYPE i;
    
    /*writes(j; );*/
    INT_TYPE j;
    
    /*writes(k; );*/
    INT_TYPE k;
    
    /*writes(m; );*/
    INT_TYPE m;
    
    /*writes(unique_keys; );*/
    INT_TYPE unique_keys;
    {
        for (i = 0; i < (1 << 16); i++) {
            
            /*reads(key_buff2; i; key_buff_ptr_global; (generic_stack); (generic_stack); (generic_stack); (generic_stack); (generic_stack); key_array; key_buff2; i; (generic_stack); (generic_stack); ); writes(generic_stack; generic_stack; generic_stack; generic_stack; );*/
            key_array[--key_buff_ptr_global[key_buff2[i]]] = key_buff2[i];
        }
    }
    
    /*writes(j; );*/
    j = 0;
    {
        for (i = 1; i < (1 << 16); i++) {
            {
                
                /*reads(i; key_array; (generic_stack); key_array; i; (generic_stack); (generic_stack); ); */
                if (key_array[i - 1] > key_array[i])
                    
                /*reads(j; ); writes(j; );*/
                j++;
            }
        }
    }
    {
        
        /*reads(j; ); */
        if (j != 0) {
            
            /*reads(j; ); */
            printf("Full_verify: number of keys out of sort: %d\n", j);
        } else
            
        /*reads(passed_verification; ); writes(passed_verification; );*/
        passed_verification++;
    }
}

/*writes(iteration; );*/
void rank(int iteration) {
    
    /*writes(i; );*/
    INT_TYPE i;
    
    /*writes(j; );*/
    INT_TYPE j;
    
    /*writes(k; );*/
    INT_TYPE k;
    
    /*writes(l; );*/
    INT_TYPE l;
    
    /*writes(m; );*/
    INT_TYPE m;
    
    /*writes(shift; );*/
    INT_TYPE shift = 11 - 9;
    
    /*writes(key; );*/
    INT_TYPE key;
    
    /*writes(min_key_val; );*/
    INT_TYPE min_key_val;
    
    /*writes(max_key_val; );*/
    INT_TYPE max_key_val;
    
    /*writes(prv_buff1; );*/
    INT_TYPE prv_buff1[(1 << 11)];
#pragma omp master
    {
        
        /*reads(key_array; iteration; iteration; ); writes(generic_stack; generic_stack; );*/
        key_array[iteration] = iteration;
        
        /*reads(iteration; key_array; iteration; ); writes(generic_stack; );*/
        key_array[iteration + 10] = (1 << 11) - iteration;
        {
            for (i = 0; i < 5; i++) {
                
                /*reads(partial_verify_vals; i; test_index_array; i; key_array; (generic_stack); (generic_stack); (generic_stack); (generic_stack); (generic_stack); ); writes(generic_stack; generic_stack; );*/
                partial_verify_vals[i] = key_array[test_index_array[i]];
            }
        }
        {
            for (i = 0; i < (1 << 11); i++) {
                
                /*reads(key_buff1; i; ); writes(generic_stack; generic_stack; );*/
                key_buff1[i] = 0;
            }
        }
    }
#pragma omp barrier
    
    {
        for (i = 0; i < (1 << 11); i++) {
            
            /*reads(prv_buff1; i; ); writes(generic_stack; generic_stack; );*/
            prv_buff1[i] = 0;
        }
    }
#pragma omp for nowait
    
    /*writes(i; );*/
    
    /*reads(i; ); */
    
    /*reads(i; ); writes(i; );*/
    for (i = 0; i < (1 << 16); i++) {
        
        /*reads(key_buff2; i; key_array; i; (generic_stack); (generic_stack); ); writes(generic_stack; generic_stack; );*/
        key_buff2[i] = key_array[i];
        
        /*reads(key_buff2; i; prv_buff1; (generic_stack); (generic_stack); (generic_stack); (generic_stack); (generic_stack); ); writes(generic_stack; generic_stack; generic_stack; );*/
        prv_buff1[key_buff2[i]]++;
    }
    {
        for (i = 0; i < (1 << 11) - 1; i++) {
            
            /*reads(i; prv_buff1; (generic_stack); prv_buff1; i; (generic_stack); (generic_stack); ); writes(generic_stack; );*/
            prv_buff1[i + 1] += prv_buff1[i];
        }
    }
#pragma omp critical
    {
        {
            for (i = 0; i < (1 << 11); i++) {
                
                /*reads(key_buff1; i; (generic_stack); (generic_stack); prv_buff1; i; (generic_stack); (generic_stack); ); writes(generic_stack; generic_stack; );*/
                key_buff1[i] += prv_buff1[i];
            }
        }
    }
#pragma omp barrier
    
#pragma omp master
    {
        {
            for (i = 0; i < 5; i++) {
                {
                    
                    /*reads(partial_verify_vals; i; (generic_stack); (generic_stack); ); writes(k; );*/
                    k = partial_verify_vals[i];
                    {
                        
                        /*writes(_imopVar110; );*/
                        signed int _imopVar110;
                        
                        /*reads(k; ); writes(_imopVar110; );*/
                        _imopVar110 = 0 <= k;
                        
                        /*reads(_imopVar110; ); */
                        if (_imopVar110) {
                            
                            /*reads(k; ); writes(_imopVar110; );*/
                            _imopVar110 = k <= (1 << 16) - 1;
                        }
                        
                        /*reads(_imopVar110; ); */
                        if (_imopVar110) {
                            switch ('S') {
                                {
                                    case 'S': {
                                        
                                        /*reads(i; ); */
                                        if (i <= 2) {
                                            {
                                                
                                                /*reads(k; key_buff1; (generic_stack); test_rank_array; i; (generic_stack); (generic_stack); iteration; ); */
                                                if (key_buff1[k - 1] != test_rank_array[i] + iteration) {
                                                    
                                                    /*reads(iteration; i; ); */
                                                    printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i);
                                                } else
                                                    
                                                /*reads(passed_verification; ); writes(passed_verification; );*/
                                                passed_verification++;
                                            }
                                        } else {
                                            {
                                                
                                                /*reads(k; key_buff1; (generic_stack); test_rank_array; i; (generic_stack); (generic_stack); iteration; ); */
                                                if (key_buff1[k - 1] != test_rank_array[i] - iteration) {
                                                    
                                                    /*reads(iteration; i; ); */
                                                    printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i);
                                                } else
                                                    
                                                /*reads(passed_verification; ); writes(passed_verification; );*/
                                                passed_verification++;
                                            }
                                        }
                                    }
                                    break;
                                    case 'W': {
                                        
                                        /*reads(i; ); */
                                        if (i < 2) {
                                            {
                                                
                                                /*reads(k; key_buff1; (generic_stack); test_rank_array; i; (generic_stack); (generic_stack); iteration; ); */
                                                if (key_buff1[k - 1] != test_rank_array[i] + (iteration - 2)) {
                                                    
                                                    /*reads(iteration; i; ); */
                                                    printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i);
                                                } else
                                                    
                                                /*reads(passed_verification; ); writes(passed_verification; );*/
                                                passed_verification++;
                                            }
                                        } else {
                                            {
                                                
                                                /*reads(k; key_buff1; (generic_stack); test_rank_array; i; (generic_stack); (generic_stack); iteration; ); */
                                                if (key_buff1[k - 1] != test_rank_array[i] - iteration) {
                                                    
                                                    /*reads(iteration; i; ); */
                                                    printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i);
                                                } else
                                                    
                                                /*reads(passed_verification; ); writes(passed_verification; );*/
                                                passed_verification++;
                                            }
                                        }
                                    }
                                    break;
                                    case 'A': {
                                        
                                        /*reads(i; ); */
                                        if (i <= 2) {
                                            {
                                                
                                                /*reads(k; key_buff1; (generic_stack); test_rank_array; i; (generic_stack); (generic_stack); iteration; ); */
                                                if (key_buff1[k - 1] != test_rank_array[i] + (iteration - 1)) {
                                                    
                                                    /*reads(iteration; i; ); */
                                                    printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i);
                                                } else
                                                    
                                                /*reads(passed_verification; ); writes(passed_verification; );*/
                                                passed_verification++;
                                            }
                                        } else {
                                            {
                                                
                                                /*reads(k; key_buff1; (generic_stack); test_rank_array; i; (generic_stack); (generic_stack); iteration; ); */
                                                if (key_buff1[k - 1] != test_rank_array[i] - (iteration - 1)) {
                                                    
                                                    /*reads(iteration; i; ); */
                                                    printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i);
                                                } else
                                                    
                                                /*reads(passed_verification; ); writes(passed_verification; );*/
                                                passed_verification++;
                                            }
                                        }
                                    }
                                    break;
                                    case 'B': {
                                        
                                        /*writes(_imopVar111; );*/
                                        signed int _imopVar111;
                                        
                                        /*writes(_imopVar112; );*/
                                        signed int _imopVar112;
                                        
                                        /*reads(i; ); writes(_imopVar111; );*/
                                        _imopVar111 = i == 1;
                                        
                                        /*reads(_imopVar111; ); */
                                        if (!_imopVar111) {
                                            
                                            /*reads(i; ); writes(_imopVar112; );*/
                                            _imopVar112 = i == 2;
                                            
                                            /*reads(_imopVar112; ); */
                                            if (!_imopVar112) {
                                                
                                                /*reads(i; ); writes(_imopVar112; );*/
                                                _imopVar112 = i == 4;
                                            }
                                            
                                            /*reads(_imopVar112; ); writes(_imopVar111; );*/
                                            _imopVar111 = _imopVar112;
                                        }
                                        
                                        /*reads(_imopVar111; ); */
                                        if (_imopVar111) {
                                            {
                                                
                                                /*reads(k; key_buff1; (generic_stack); test_rank_array; i; (generic_stack); (generic_stack); iteration; ); */
                                                if (key_buff1[k - 1] != test_rank_array[i] + iteration) {
                                                    
                                                    /*reads(iteration; i; ); */
                                                    printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i);
                                                } else
                                                    
                                                /*reads(passed_verification; ); writes(passed_verification; );*/
                                                passed_verification++;
                                            }
                                        } else {
                                            {
                                                
                                                /*reads(k; key_buff1; (generic_stack); test_rank_array; i; (generic_stack); (generic_stack); iteration; ); */
                                                if (key_buff1[k - 1] != test_rank_array[i] - iteration) {
                                                    
                                                    /*reads(iteration; i; ); */
                                                    printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i);
                                                } else
                                                    
                                                /*reads(passed_verification; ); writes(passed_verification; );*/
                                                passed_verification++;
                                            }
                                        }
                                    }
                                    break;
                                    case 'C': {
                                        
                                        /*reads(i; ); */
                                        if (i <= 2) {
                                            {
                                                
                                                /*reads(k; key_buff1; (generic_stack); test_rank_array; i; (generic_stack); (generic_stack); iteration; ); */
                                                if (key_buff1[k - 1] != test_rank_array[i] + iteration) {
                                                    
                                                    /*reads(iteration; i; ); */
                                                    printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i);
                                                } else
                                                    
                                                /*reads(passed_verification; ); writes(passed_verification; );*/
                                                passed_verification++;
                                            }
                                        } else {
                                            {
                                                
                                                /*reads(k; key_buff1; (generic_stack); test_rank_array; i; (generic_stack); (generic_stack); iteration; ); */
                                                if (key_buff1[k - 1] != test_rank_array[i] - iteration) {
                                                    
                                                    /*reads(iteration; i; ); */
                                                    printf("Failed partial verification: " "iteration %d, test key %d\n", iteration, i);
                                                } else
                                                    
                                                /*reads(passed_verification; ); writes(passed_verification; );*/
                                                passed_verification++;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        {
            
            /*reads(iteration; ); */
            if (iteration == 10)
                
            /*reads(key_buff1; ); writes(key_buff_ptr_global; );*/
            key_buff_ptr_global = key_buff1;
        }
    }
}

/*writes(argc; );*/

/*writes(argv; );*/
int main(int argc, char **argv) {
    
    /*writes(i; );*/
    int i;
    
    /*writes(iteration; );*/
    int iteration;
    
    /*writes(itemp; );*/
    int itemp;
    
    /*writes(nthreads; );*/
    int nthreads = 1;
    
    /*writes(timecounter; );*/
    double timecounter;
    
    /*writes(maxtime; );*/
    double maxtime;
    {
        for (i = 0; i < 5; i++) {
            {
                switch ('S') {
                    {
                        
                        /*reads(test_index_array; i; S_test_index_array; i; (generic_stack); (generic_stack); ); writes(generic_stack; generic_stack; );*/
                        test_index_array[i] = S_test_index_array[i];
                        
                        /*reads(test_rank_array; i; S_test_rank_array; i; (generic_stack); (generic_stack); ); writes(generic_stack; generic_stack; );*/
                        test_rank_array[i] = S_test_rank_array[i];
                        break;
                        
                        /*reads(test_index_array; i; A_test_index_array; i; (generic_stack); (generic_stack); ); writes(generic_stack; generic_stack; );*/
                        test_index_array[i] = A_test_index_array[i];
                        
                        /*reads(test_rank_array; i; A_test_rank_array; i; (generic_stack); (generic_stack); ); writes(generic_stack; generic_stack; );*/
                        test_rank_array[i] = A_test_rank_array[i];
                        break;
                        
                        /*reads(test_index_array; i; W_test_index_array; i; (generic_stack); (generic_stack); ); writes(generic_stack; generic_stack; );*/
                        test_index_array[i] = W_test_index_array[i];
                        
                        /*reads(test_rank_array; i; W_test_rank_array; i; (generic_stack); (generic_stack); ); writes(generic_stack; generic_stack; );*/
                        test_rank_array[i] = W_test_rank_array[i];
                        break;
                        
                        /*reads(test_index_array; i; B_test_index_array; i; (generic_stack); (generic_stack); ); writes(generic_stack; generic_stack; );*/
                        test_index_array[i] = B_test_index_array[i];
                        
                        /*reads(test_rank_array; i; B_test_rank_array; i; (generic_stack); (generic_stack); ); writes(generic_stack; generic_stack; );*/
                        test_rank_array[i] = B_test_rank_array[i];
                        break;
                        
                        /*reads(test_index_array; i; C_test_index_array; i; (generic_stack); (generic_stack); ); writes(generic_stack; generic_stack; );*/
                        test_index_array[i] = C_test_index_array[i];
                        
                        /*reads(test_rank_array; i; C_test_rank_array; i; (generic_stack); (generic_stack); ); writes(generic_stack; generic_stack; );*/
                        test_rank_array[i] = C_test_rank_array[i];
                        break;
                    }
                }
            }
        }
    }
    ;
    printf("\n\n NAS Parallel Benchmarks 2.3 OpenMP C version" " - IS Benchmark\n\n");
    {
        
        /*writes(_imopVar114; );*/
        signed int _imopVar114;
        
        /*writes(_imopVar114; );*/
        _imopVar114 = (1 << 16);
        
        /*reads(_imopVar114; ); */
        printf(" Size:  %d  (class %c)\n", _imopVar114, 'S');
    }
    printf(" Iterations:   %d\n", 10);
    timer_clear(0);
    create_seq(314159265.00, 1220703125.00);
#pragma omp parallel
    rank(1);
    
    /*writes(passed_verification; );*/
    passed_verification = 0;
    {
        if ('S' != 'S')
            printf("\n   iteration\n");
    }
    timer_start(0);
#pragma omp parallel private(iteration)
    {
        for (iteration = 1; iteration <= 10; iteration++) {
            {
#pragma omp master
                {
                    if ('S' != 'S')
                        
                    /*reads(iteration; ); */
                    printf("        %d\n", iteration);
                }
                
                /*reads(iteration; ); */
                rank(iteration);
            }
        }
    }
    timer_stop(0);
    {
        
        /*writes(_imopVar115; );*/
        double _imopVar115;
        
        /*writes(_imopVar115; );*/
        _imopVar115 = timer_read(0);
        
        /*reads(_imopVar115; ); writes(timecounter; );*/
        timecounter = _imopVar115;
    }
    full_verify();
    {
        
        /*reads(passed_verification; ); */
        if (passed_verification != 5 * 10 + 1)
            
        /*writes(passed_verification; );*/
        passed_verification = 0;
    }
    {
        
        /*writes(_imopVar118; );*/
        double _imopVar118;
        
        /*writes(_imopVar119; );*/
        signed int _imopVar119;
        
        /*reads(timecounter; ); writes(_imopVar118; );*/
        _imopVar118 = ((double) (10 * (1 << 16))) / timecounter / 1000000.;
        
        /*writes(_imopVar119; );*/
        _imopVar119 = (1 << 16);
        
        /*reads(_imopVar119; nthreads; timecounter; _imopVar118; passed_verification; ); */
        c_print_results("IS", 'S', _imopVar119, 0, 0, 10, nthreads, timecounter, _imopVar118, "keys ranked", passed_verification, "3.0 structured", "15 Jul 2017", "gcc", "gcc", "(none)", "-I../common", "-O3 -fopenmp", "-O3 -fopenmp", "randlc");
    }
}
