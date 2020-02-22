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
typedef __darwin_va_list va_list;
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
int printf(const char *, ...);
typedef __darwin_off_t off_t;
typedef __darwin_ssize_t ssize_t;
typedef float float_t;
typedef double double_t;
extern double sin(double );
extern double sqrt(double );
struct __float2 {
    float __sinval;
    float __cosval;
} ;
struct __double2 {
    double __sinval;
    double __cosval;
} ;
struct exception {
    int type;
    char *name;
    double arg1;
    double arg2;
    double retval;
} ;
typedef __darwin_clock_t clock_t;
typedef __darwin_time_t time_t;
struct timespec {
    __darwin_time_t tv_sec;
    long tv_nsec;
} ;
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
} ;
struct tm *localtime(const time_t *);
size_t strftime(char *, size_t  , const char * , const struct tm *);
time_t time(time_t *);
enum enum_imopVarPre11 {
    _CLOCK_REALTIME = 0, _CLOCK_MONOTONIC = 6 , _CLOCK_MONOTONIC_RAW = 4 , _CLOCK_MONOTONIC_RAW_APPROX = 5 , _CLOCK_UPTIME_RAW = 8 , _CLOCK_UPTIME_RAW_APPROX = 9 , _CLOCK_PROCESS_CPUTIME_ID = 12 , _CLOCK_THREAD_CPUTIME_ID = 16
} ;
typedef enum enum_imopVarPre11 clockid_t;
struct stUn_imopVarPre12 {
    unsigned char _x[64];
} ;
typedef struct stUn_imopVarPre12 omp_lock_t;
struct stUn_imopVarPre13 {
    unsigned char _x[80];
} ;
typedef struct stUn_imopVarPre13 omp_nest_lock_t;
enum omp_sched_t {
    omp_sched_static = 1, omp_sched_dynamic = 2 , omp_sched_guided = 3 , omp_sched_auto = 4
} ;
typedef enum omp_sched_t omp_sched_t;
extern int omp_get_num_threads(void );
extern int omp_get_thread_num(void );
extern int omp_get_num_procs(void );
extern double omp_get_wtime(void );
int main(int argc, char *argv[]);
double r8mat_rms(int nx, int ny , double a[161][161]);
void rhs(int nx, int ny , double f[161][161]);
void sweep(int nx, int ny , double dx , double dy , double f[161][161] , int itold , int itnew , double u[161][161] , double unew[161][161]);
void timestamp(void );
double u_exact(double x, double y);
double uxxyy_exact(double x, double y);
int main(int argc, char *argv[]) {
    double uexact[161][161];
    double unew[161][161];
    double unew_norm;
    double wtime;
    int converged;
    double diff;
    double dx;
    double dy;
    double error;
    double f[161][161];
    int i;
    int id;
    int itnew;
    int itold;
    int j;
    int nx = 161;
    int ny = 161;
    double tolerance = 0.000001;
    double u[161][161];
    double u_norm;
    double udiff[161][161];
#pragma omp parallel
    {
#pragma omp master
        {
            20;
        }
        double x;
        double y;
#pragma omp master
        {
            dx = 1.0 / (double) (nx - 1);
            dy = 1.0 / (double) (ny - 1);
            timestamp();
            printf("\n");
            printf("POISSON_OPENMP:\n");
            printf("  C version\n");
            printf("  A program for solving the Poisson equation.\n");
            printf("\n");
            printf("  Use OpenMP for parallel execution.\n");
        }
        int _imopVarPre147;
#pragma omp master
        {
            _imopVarPre147 = omp_get_num_procs();
            printf("  The number of processors is %d\n", _imopVarPre147);
        }
        id = omp_get_thread_num();
        if (id == 0) {
            int _imopVarPre149;
            _imopVarPre149 = omp_get_num_threads();
            printf("  The maximum number of threads is %d\n", _imopVarPre149);
        }
#pragma omp master
        {
            printf("\n");
            printf("  -DEL^2 U = F(X,Y)\n");
            printf("\n");
            printf("  on the rectangle 0 <= X <= 1, 0 <= Y <= 1.\n");
            printf("\n");
            printf("  F(X,Y) = pi^2 * ( x^2 + y^2 ) * sin ( pi * x * y )\n");
            printf("\n");
            printf("  The number of interior X grid points is %d\n", nx);
            printf("  The number of interior Y grid points is %d\n", ny);
            printf("  The X grid spacing is %f\n", dx);
            printf("  The Y grid spacing is %f\n", dy);
            rhs(nx, ny, f);
            for (j = 0; j < ny; j++) {
                for (i = 0; i < nx; i++) {
                    int _imopVarPre150;
                    int _imopVarPre151;
                    int _imopVarPre152;
                    _imopVarPre150 = i == 0;
                    if (!_imopVarPre150) {
                        _imopVarPre151 = i == nx - 1;
                        if (!_imopVarPre151) {
                            _imopVarPre152 = j == 0;
                            if (!_imopVarPre152) {
                                _imopVarPre152 = j == ny - 1;
                            }
                            _imopVarPre151 = _imopVarPre152;
                        }
                        _imopVarPre150 = _imopVarPre151;
                    }
                    if (_imopVarPre150) {
                        unew[i][j] = f[i][j];
                    } else {
                        unew[i][j] = 0.0;
                    }
                }
            }
            unew_norm = r8mat_rms(nx, ny, unew);
            for (j = 0; j < ny; j++) {
                y = (double) j / (double) (ny - 1);
                for (i = 0; i < nx; i++) {
                    x = (double) i / (double) (nx - 1);
                    double _imopVarPre153;
                    _imopVarPre153 = u_exact(x, y);
                    uexact[i][j] = _imopVarPre153;
                }
            }
            u_norm = r8mat_rms(nx, ny, uexact);
            printf("  RMS of exact solution = %g\n", u_norm);
            converged = 0;
            printf("\n");
            printf("  Step    ||Unew||     ||Unew-U||     ||Unew-Exact||\n");
            printf("\n");
            for (j = 0; j < ny; j++) {
                for (i = 0; i < nx; i++) {
                    udiff[i][j] = unew[i][j] - uexact[i][j];
                }
            }
            error = r8mat_rms(nx, ny, udiff);
            printf("  %4d  %14g                  %14g\n", 0, unew_norm, error);
            wtime = omp_get_wtime();
            itnew = 0;
        }
    }
#pragma omp parallel
    {
        int i;
        int it;
        int j;
        while (1) {
#pragma omp master
            {
                itold = itnew;
                itnew = itold + 500;
            }
            // #pragma omp dummyFlush BARRIER_START written([itold, u_norm, error, diff, itnew, i, uexact.f, j, unew.f, udiff.f, converged, wtime, unew_norm]) read()
#pragma omp barrier
            for (it = itold + 1; it <= itnew; it++) {
#pragma omp for nowait
                for (j = 0; j < ny; j++) {
                    for (i = 0; i < nx; i++) {
                        u[i][j] = unew[i][j];
                    }
                }
#pragma omp for nowait
                for (j = 0; j < ny; j++) {
                    for (i = 0; i < nx; i++) {
                        int _imopVarPre165;
                        int _imopVarPre166;
                        int _imopVarPre167;
                        _imopVarPre165 = i == 0;
                        if (!_imopVarPre165) {
                            _imopVarPre166 = j == 0;
                            if (!_imopVarPre166) {
                                _imopVarPre167 = i == nx - 1;
                                if (!_imopVarPre167) {
                                    _imopVarPre167 = j == ny - 1;
                                }
                                _imopVarPre166 = _imopVarPre167;
                            }
                            _imopVarPre165 = _imopVarPre166;
                        }
                        if (_imopVarPre165) {
                            unew[i][j] = f[i][j];
                        } else {
                            unew[i][j] = 0.25 * (u[i - 1][j] + u[i][j + 1] + u[i][j - 1] + u[i + 1][j] + f[i][j] * dx * dy);
                        }
                    }
                }
            }
#pragma omp barrier
#pragma omp master
            {
                u_norm = unew_norm;
                unew_norm = r8mat_rms(nx, ny, unew);
                for (j = 0; j < ny; j++) {
                    for (i = 0; i < nx; i++) {
                        udiff[i][j] = unew[i][j] - u[i][j];
                    }
                }
                diff = r8mat_rms(nx, ny, udiff);
                for (j = 0; j < ny; j++) {
                    for (i = 0; i < nx; i++) {
                        udiff[i][j] = unew[i][j] - uexact[i][j];
                    }
                }
                error = r8mat_rms(nx, ny, udiff);
                printf("  %4d  %14g  %14g  %14g\n", itnew, unew_norm, diff, error);
            }
#pragma omp barrier
            if (diff <= tolerance) {
#pragma omp master
                {
                    converged = 1;

                }
                break;
            }
        }
    }
    if (converged) {
        printf("  The iteration has converged.\n");
    } else {
        printf("  The iteration has NOT converged.\n");
    }
    double _imopVarPre155;
    _imopVarPre155 = omp_get_wtime();
    wtime = _imopVarPre155 - wtime;
    printf("\n");
    printf("  Elapsed seconds = %g\n", wtime);
    printf("\n");
    printf("POISSON_OPENMP:\n");
    printf("  Normal end of execution.\n");
    printf("\n");
    timestamp();
    return 0;
}
double r8mat_rms(int nx, int ny , double a[161][161]) {
    int i;
    int j;
    double v;
    v = 0.0;
    for (j = 0; j < ny; j++) {
        for (i = 0; i < nx; i++) {
            v = v + a[i][j] * a[i][j];
        }
    }
    double _imopVarPre157;
    double _imopVarPre158;
    _imopVarPre157 = v / (double) (nx * ny);
    _imopVarPre158 = sqrt(_imopVarPre157);
    v = _imopVarPre158;
    return v;
}
void rhs(int nx, int ny , double f[161][161]) {
    double fnorm;
    int i;
    int j;
    double x;
    double y;
    for (j = 0; j < ny; j++) {
        y = (double) j / (double) (ny - 1);
        for (i = 0; i < nx; i++) {
            x = (double) i / (double) (nx - 1);
            int _imopVarPre159;
            int _imopVarPre160;
            int _imopVarPre161;
            _imopVarPre159 = i == 0;
            if (!_imopVarPre159) {
                _imopVarPre160 = i == nx - 1;
                if (!_imopVarPre160) {
                    _imopVarPre161 = j == 0;
                    if (!_imopVarPre161) {
                        _imopVarPre161 = j == ny - 1;
                    }
                    _imopVarPre160 = _imopVarPre161;
                }
                _imopVarPre159 = _imopVarPre160;
            }
            if (_imopVarPre159) {
                double _imopVarPre162;
                _imopVarPre162 = u_exact(x, y);
                f[i][j] = _imopVarPre162;
            } else {
                double _imopVarPre164;
                _imopVarPre164 = uxxyy_exact(x, y);
                f[i][j] = -_imopVarPre164;
            }
        }
    }
    fnorm = r8mat_rms(nx, ny, f);
    printf("  RMS of F = %g\n", fnorm);
    return;
}
void sweep(int nx, int ny , double dx , double dy , double f[161][161] , int itold , int itnew , double u[161][161] , double unew[161][161]) {
    {
        int i;
        int it;
        int j;
        for (it = itold + 1; it <= itnew; it++) {
#pragma omp for nowait
            for (j = 0; j < ny; j++) {
                for (i = 0; i < nx; i++) {
                    u[i][j] = unew[i][j];
                }
            }
#pragma omp for nowait
            for (j = 0; j < ny; j++) {
                for (i = 0; i < nx; i++) {
                    int _imopVarPre165;
                    int _imopVarPre166;
                    int _imopVarPre167;
                    _imopVarPre165 = i == 0;
                    if (!_imopVarPre165) {
                        _imopVarPre166 = j == 0;
                        if (!_imopVarPre166) {
                            _imopVarPre167 = i == nx - 1;
                            if (!_imopVarPre167) {
                                _imopVarPre167 = j == ny - 1;
                            }
                            _imopVarPre166 = _imopVarPre167;
                        }
                        _imopVarPre165 = _imopVarPre166;
                    }
                    if (_imopVarPre165) {
                        unew[i][j] = f[i][j];
                    } else {
                        unew[i][j] = 0.25 * (u[i - 1][j] + u[i][j + 1] + u[i][j - 1] + u[i + 1][j] + f[i][j] * dx * dy);
                    }
                }
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written([]) read([f.f, itnew, omp_get_wtime, i, uexact, nullCell, localtime, nx, u_exact, unew, sqrt, udiff.f, dy, u.f, time_buffer.f, unew_norm, uxxyy_exact, id, omp_get_num_threads, itold, u_norm, r8mat_rms, error, diff, f, timestamp, time, _imopVarPre159, uexact.f, j, ny, printf, _imopVarPre150, unew.f, udiff, dx, omp_get_num_procs, sin, u, strftime, converged, rhs, time_buffer, omp_get_thread_num])
#pragma omp barrier
    return;
}
void timestamp(void ) {
    static char time_buffer[40];
    const struct tm *tm;
    time_t now;
    void *_imopVarPre169;
    signed long int _imopVarPre170;
    _imopVarPre169 = ((void *) 0);
    _imopVarPre170 = time(_imopVarPre169);
    now = _imopVarPre170;
    signed long int *_imopVarPre172;
    struct tm *_imopVarPre173;
    _imopVarPre172 = &now;
    _imopVarPre173 = localtime(_imopVarPre172);
    tm = _imopVarPre173;
    strftime(time_buffer, 40, "%d %B %Y %I:%M:%S %p", tm);
    printf("%s\n", time_buffer);
    return;
}
double u_exact(double x, double y) {
    double pi = 3.141592653589793;
    double value;
    double _imopVarPre175;
    double _imopVarPre176;
    _imopVarPre175 = pi * x * y;
    _imopVarPre176 = sin(_imopVarPre175);
    value = _imopVarPre176;
    return value;
}
double uxxyy_exact(double x, double y) {
    double pi = 3.141592653589793;
    double value;
    double _imopVarPre179;
    double _imopVarPre180;
    _imopVarPre179 = pi * x * y;
    _imopVarPre180 = sin(_imopVarPre179);
    value = -pi * pi * (x * x + y * y) * _imopVarPre180;
    return value;
}
