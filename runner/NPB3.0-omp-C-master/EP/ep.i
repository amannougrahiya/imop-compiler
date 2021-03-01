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
int printf(const char *restrict , ...);
typedef __darwin_off_t off_t;
typedef __darwin_ssize_t ssize_t;
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
typedef float float_t;
typedef double double_t;
extern double log(double );
extern double fabs(double );
extern double pow(double , double );
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
typedef int boolean;
struct stUn_imopVarPre11 {
    double real;
    double imag;
} ;
typedef struct stUn_imopVarPre11 dcomplex;
extern double randlc(double *, double );
extern void vranlc(int , double * , double  , double *);
extern void timer_clear(int );
extern void timer_start(int );
extern void timer_stop(int );
extern double timer_read(int );
extern void c_print_results(char *name, char class , int n1 , int n2 , int n3 , int niter , int nthreads , double t , double mops , char *optype , int passed_verification , char *npbversion , char *compiletime , char *cc , char *clink , char *c_lib , char *c_inc , char *cflags , char *clinkflags , char *rand);
static double x[2 * (1 << 16)];
#pragma omp threadprivate(x)
static double q[10];
int main(int argc, char **argv) {
    double Mops;
    double t1;
    double t2;
    double sx;
    double sy;
    double tm;
    double an;
    double tt;
    double gc;
    double dum[3] = {1.0, 1.0 , 1.0};
    int np;
    int i;
    int k;
    int nit;
    int k_offset;
    int j;
    int nthreads = 1;
    boolean verified;
    char size[13 + 1];
    printf("\n\n NAS Parallel Benchmarks 3.0 structured OpenMP C version" " - EP Benchmark\n");
    int _imopVarPre153;
    double _imopVarPre154;
    int _imopVarPre157;
    int _imopVarPre158;
    unsigned int _imopVarPre159;
    _imopVarPre153 = 24 + 1;
    _imopVarPre154 = pow(2.0, _imopVarPre153);
    _imopVarPre157 = 2 > 1;
    if (_imopVarPre157) {
        _imopVarPre158 = 1;
    } else {
        _imopVarPre158 = 0;
    }
    _imopVarPre159 = __builtin_object_size(size, _imopVarPre158);
    __builtin___sprintf_chk(size, 0, _imopVarPre159, "%12.0f", _imopVarPre154);
    for (j = 13; j >= 1; j--) {
        if (size[j] == '.') {
            size[j] = ' ';
        }
    }
    printf(" Number of random numbers generated: %13s\n", size);
    verified = 0;
    np = (1 << (24 - 16));
    double *_imopVarPre163;
    double _imopVarPre164;
    double *_imopVarPre165;
    _imopVarPre163 = &(dum[2]);
    _imopVarPre164 = dum[1];
    _imopVarPre165 = &(dum[0]);
    vranlc(0, _imopVarPre165, _imopVarPre164, _imopVarPre163);
    double _imopVarPre168;
    double *_imopVarPre169;
    double _imopVarPre170;
    _imopVarPre168 = dum[2];
    _imopVarPre169 = &(dum[1]);
    _imopVarPre170 = randlc(_imopVarPre169, _imopVarPre168);
    dum[0] = _imopVarPre170;
#pragma omp parallel default(shared) private(i)
    {
        signed long long int barrCounter_imopVarPre0;
        barrCounter_imopVarPre0 = 2;
#pragma omp for nowait
        for (i = 0; i < 2 * (1 << 16); i++) {
            x[i] = -1.0e99;
        }
        barrCounter_imopVarPre0++;
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
#pragma omp master
        {
            printf("The master thread encountered %d barriers.\n", barrCounter_imopVarPre0);
        }
    }
    int _imopVarPre205;
    int _imopVarPre206;
    double _imopVarPre207;
    double _imopVarPre208;
    double _imopVarPre209;
    _imopVarPre205 = (1.0 > 1.0);
    if (_imopVarPre205) {
        _imopVarPre206 = 1.0;
    } else {
        _imopVarPre206 = 1.0;
    }
    _imopVarPre207 = fabs(_imopVarPre206);
    _imopVarPre208 = sqrt(_imopVarPre207);
    _imopVarPre209 = log(_imopVarPre208);
    Mops = _imopVarPre209;
    timer_clear(1);
    timer_clear(2);
    timer_clear(3);
    timer_start(1);
    double *_imopVarPre211;
    _imopVarPre211 = &t1;
    vranlc(0, _imopVarPre211, 1220703125.0, x);
    t1 = 1220703125.0;
    for (i = 1; i <= 16 + 1; i++) {
        double *_imopVarPre213;
        double _imopVarPre214;
        _imopVarPre213 = &t1;
        _imopVarPre214 = randlc(_imopVarPre213, t1);
        t2 = _imopVarPre214;
    }
    an = t1;
    tt = 271828183.0;
    gc = 0.0;
    sx = 0.0;
    sy = 0.0;
    for (i = 0; i <= 10 - 1; i++) {
        q[i] = 0.0;
    }
    k_offset = -1;
#pragma omp parallel copyin(x)
    {
        signed long long int barrCounter_imopVarPre1;
        barrCounter_imopVarPre1 = 2;
        double t1;
        double t2;
        double t3;
        double t4;
        double x1;
        double x2;
        int kk;
        int i;
        int ik;
        int l;
        double qq[10];
        for (i = 0; i < 10; i++) {
            qq[i] = 0.0;
        }
#pragma omp for reduction(+:sx, sy) schedule(static) nowait
        for (k = 1; k <= np; k++) {
            kk = k_offset + k;
            t1 = 271828183.0;
            t2 = an;
            for (i = 1; i <= 100; i++) {
                ik = kk / 2;
                if (2 * ik != kk) {
                    double *_imopVarPre216;
                    double _imopVarPre217;
                    _imopVarPre216 = &t1;
                    _imopVarPre217 = randlc(_imopVarPre216, t2);
                    t3 = _imopVarPre217;
                }
                if (ik == 0) {
                    break;
                }
                double *_imopVarPre219;
                double _imopVarPre220;
                _imopVarPre219 = &t2;
                _imopVarPre220 = randlc(_imopVarPre219, t2);
                t3 = _imopVarPre220;
                kk = ik;
            }
            if (0 == 1) {
                timer_start(3);
            }
            double *_imopVarPre224;
            double *_imopVarPre225;
            int _imopVarPre226;
            _imopVarPre224 = x - 1;
            _imopVarPre225 = &t1;
            _imopVarPre226 = 2 * (1 << 16);
            vranlc(_imopVarPre226, _imopVarPre225, 1220703125.0, _imopVarPre224);
            if (0 == 1) {
                timer_stop(3);
            }
            if (0 == 1) {
                timer_start(2);
            }
            for (i = 0; i < (1 << 16); i++) {
                x1 = 2.0 * x[2 * i] - 1.0;
                x2 = 2.0 * x[2 * i + 1] - 1.0;
                t1 = (x1 * x1) + (x2 * x2);
                if (t1 <= 1.0) {
                    double _imopVarPre231;
                    double _imopVarPre232;
                    double _imopVarPre233;
                    _imopVarPre231 = log(t1);
                    _imopVarPre232 = -2.0 * _imopVarPre231 / t1;
                    _imopVarPre233 = sqrt(_imopVarPre232);
                    t2 = _imopVarPre233;
                    t3 = (x1 * t2);
                    t4 = (x2 * t2);
                    double _imopVarPre254;
                    double _imopVarPre255;
                    int _imopVarPre256;
                    double _imopVarPre257;
                    double _imopVarPre259;
                    double _imopVarPre261;
                    _imopVarPre254 = fabs(t3);
                    _imopVarPre255 = fabs(t4);
                    _imopVarPre256 = (_imopVarPre254 > _imopVarPre255);
                    if (_imopVarPre256) {
                        _imopVarPre259 = fabs(t3);
                        _imopVarPre257 = _imopVarPre259;
                    } else {
                        _imopVarPre261 = fabs(t4);
                        _imopVarPre257 = _imopVarPre261;
                    }
                    l = _imopVarPre257;
                    qq[l] += 1.0;
                    sx = sx + t3;
                    sy = sy + t4;
                }
            }
            if (0 == 1) {
                timer_stop(2);
            }
        }
        barrCounter_imopVarPre1++;
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
// #pragma omp dummyFlush CRITICAL_START written([]) read([q])
#pragma omp critical
        {
            for (i = 0; i <= 10 - 1; i++) {
                q[i] += qq[i];
            }
        }
// #pragma omp dummyFlush CRITICAL_END written([q]) read([_imopVarPre293, printf, fabs, _imopVarPre329, c_print_results, timer_stop, pow, _imopVarPre275, q, _imopVarPre347, _imopVarPre311, timer_read])
#pragma omp master
        {
            printf("The master thread encountered %d barriers.\n", barrCounter_imopVarPre1);
        }
    }
    for (i = 0; i <= 10 - 1; i++) {
        gc = gc + q[i];
    }
    timer_stop(1);
    tm = timer_read(1);
    nit = 0;
    if (24 == 24) {
        double _imopVarPre273;
        double _imopVarPre274;
        int _imopVarPre275;
        double _imopVarPre278;
        double _imopVarPre279;
        _imopVarPre273 = (sx - (-3.247834652034740e3)) / sx;
        _imopVarPre274 = fabs(_imopVarPre273);
        _imopVarPre275 = (_imopVarPre274 <= 1.0e-8);
        if (_imopVarPre275) {
            _imopVarPre278 = (sy - (-6.958407078382297e3)) / sy;
            _imopVarPre279 = fabs(_imopVarPre278);
            _imopVarPre275 = (_imopVarPre279 <= 1.0e-8);
        }
        if (_imopVarPre275) {
            verified = 1;
        }
    } else {
        if (24 == 25) {
            double _imopVarPre291;
            double _imopVarPre292;
            int _imopVarPre293;
            double _imopVarPre296;
            double _imopVarPre297;
            _imopVarPre291 = (sx - (-2.863319731645753e3)) / sx;
            _imopVarPre292 = fabs(_imopVarPre291);
            _imopVarPre293 = (_imopVarPre292 <= 1.0e-8);
            if (_imopVarPre293) {
                _imopVarPre296 = (sy - (-6.320053679109499e3)) / sy;
                _imopVarPre297 = fabs(_imopVarPre296);
                _imopVarPre293 = (_imopVarPre297 <= 1.0e-8);
            }
            if (_imopVarPre293) {
                verified = 1;
            }
        } else {
            if (24 == 28) {
                double _imopVarPre309;
                double _imopVarPre310;
                int _imopVarPre311;
                double _imopVarPre314;
                double _imopVarPre315;
                _imopVarPre309 = (sx - (-4.295875165629892e3)) / sx;
                _imopVarPre310 = fabs(_imopVarPre309);
                _imopVarPre311 = (_imopVarPre310 <= 1.0e-8);
                if (_imopVarPre311) {
                    _imopVarPre314 = (sy - (-1.580732573678431e4)) / sy;
                    _imopVarPre315 = fabs(_imopVarPre314);
                    _imopVarPre311 = (_imopVarPre315 <= 1.0e-8);
                }
                if (_imopVarPre311) {
                    verified = 1;
                }
            } else {
                if (24 == 30) {
                    double _imopVarPre327;
                    double _imopVarPre328;
                    int _imopVarPre329;
                    double _imopVarPre332;
                    double _imopVarPre333;
                    _imopVarPre327 = (sx - 4.033815542441498e4) / sx;
                    _imopVarPre328 = fabs(_imopVarPre327);
                    _imopVarPre329 = (_imopVarPre328 <= 1.0e-8);
                    if (_imopVarPre329) {
                        _imopVarPre332 = (sy - (-2.660669192809235e4)) / sy;
                        _imopVarPre333 = fabs(_imopVarPre332);
                        _imopVarPre329 = (_imopVarPre333 <= 1.0e-8);
                    }
                    if (_imopVarPre329) {
                        verified = 1;
                    }
                } else {
                    if (24 == 32) {
                        double _imopVarPre345;
                        double _imopVarPre346;
                        int _imopVarPre347;
                        double _imopVarPre350;
                        double _imopVarPre351;
                        _imopVarPre345 = (sx - 4.764367927995374e4) / sx;
                        _imopVarPre346 = fabs(_imopVarPre345);
                        _imopVarPre347 = (_imopVarPre346 <= 1.0e-8);
                        if (_imopVarPre347) {
                            _imopVarPre350 = (sy - (-8.084072988043731e4)) / sy;
                            _imopVarPre351 = fabs(_imopVarPre350);
                            _imopVarPre347 = (_imopVarPre351 <= 1.0e-8);
                        }
                        if (_imopVarPre347) {
                            verified = 1;
                        }
                    }
                }
            }
        }
    }
    int _imopVarPre354;
    double _imopVarPre355;
    _imopVarPre354 = 24 + 1;
    _imopVarPre355 = pow(2.0, _imopVarPre354);
    Mops = _imopVarPre355 / tm / 1000000.0;
    printf("EP Benchmark Results: \n" "CPU Time = %10.4f\n" "N = 2^%5d\n" "No. Gaussian Pairs = %15.0f\n" "Sums = %25.15e %25.15e\n" "Counts:\n", tm, 24, gc, sx, sy);
    for (i = 0; i <= 10 - 1; i++) {
        double _imopVarPre357;
        _imopVarPre357 = q[i];
        printf("%3d %15.0f\n", i, _imopVarPre357);
    }
    int _imopVarPre359;
    _imopVarPre359 = 24 + 1;
    c_print_results("EP", 'S', _imopVarPre359, 0, 0, nit, nthreads, tm, Mops, "Random numbers generated", verified, "3.0 structured", "21 Jul 2017", "gcc", "gcc", "(none)", "-I../common", "-O3 -fopenmp", "-O3 -fopenmp", "randdp");
    if (0 == 1) {
        double _imopVarPre361;
        _imopVarPre361 = timer_read(1);
        printf("Total time:     %f", _imopVarPre361);
        double _imopVarPre363;
        _imopVarPre363 = timer_read(2);
        printf("Gaussian pairs: %f", _imopVarPre363);
        double _imopVarPre365;
        _imopVarPre365 = timer_read(3);
        printf("Random numbers: %f", _imopVarPre365);
    }
}
