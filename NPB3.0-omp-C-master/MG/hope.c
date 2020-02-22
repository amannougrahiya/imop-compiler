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
int fclose(FILE *);
int fgetc(FILE *);
FILE *fopen(const char *restrict __filename, const char *restrict __mode);
int fscanf(FILE *restrict , const char *restrict , ...);
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
void *malloc(size_t __size);
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
static int nx[11 + 1];
static int ny[11 + 1];
static int nz[11 + 1];
static char Class;
static int debug_vec[8];
static int m1[11 + 1];
static int m2[11 + 1];
static int m3[11 + 1];
static int lt;
static int lb;
static int is1;
static int is2;
static int is3;
static int ie1;
static int ie2;
static int ie3;
static void setup(int *n1, int *n2 , int *n3 , int lt);
static void mg3P(double ****u, double ***v , double ****r , double a[4] , double c[4] , int n1 , int n2 , int n3 , int k);
static void psinv(double ***r, double ***u , int n1 , int n2 , int n3 , double c[4] , int k);
static void resid(double ***u, double ***v , double ***r , int n1 , int n2 , int n3 , double a[4] , int k);
static void rprj3(double ***r, int m1k , int m2k , int m3k , double ***s , int m1j , int m2j , int m3j , int k);
static void interp(double ***z, int mm1 , int mm2 , int mm3 , double ***u , int n1 , int n2 , int n3 , int k);
static void norm2u3(double ***r, int n1 , int n2 , int n3 , double *rnm2 , double *rnmu , int nx , int ny , int nz);
static void rep_nrm(double ***u, int n1 , int n2 , int n3 , char *title , int kk);
static void comm3(double ***u, int n1 , int n2 , int n3 , int kk);
static void zran3(double ***z, int n1 , int n2 , int n3 , int nx , int ny , int k);
static void showall(double ***z, int n1 , int n2 , int n3);
static double power(double a, int n);
static void bubble(double ten[1037][2], int j1[1037][2] , int j2[1037][2] , int j3[1037][2] , int m , int ind);
static void zero3(double ***z, int n1 , int n2 , int n3);
int main(int argc, char *argv[]) {
    int k;
    int it;
    double t;
    double tinit;
    double mflops;
    int nthreads = 1;
    double ****u;
    double ***v;
    double ****r;
    double a[4];
    double c[4];
    double rnm2;
    double rnmu;
    double epsilon = 1.0e-8;
    int n1;
    int n2;
    int n3;
    int nit;
    double verify_value;
    boolean verified;
    int i;
    int j;
    int l;
    FILE *fp;
    timer_clear(1);
    timer_clear(2);
    timer_start(2);
    printf("\n\n NAS Parallel Benchmarks 3.0 structured OpenMP C version" " - MG Benchmark\n\n");
    fp = fopen("mg.input", "r");
    if (fp != ((void *) 0)) {
        printf(" Reading from input file mg.input\n");
        int *_imopVarPre145;
        _imopVarPre145 = &lt;
        fscanf(fp, "%d", _imopVarPre145);
        int _imopVarPre147;
        _imopVarPre147 = fgetc(fp);
        while (_imopVarPre147 != '\n') {
            ;
            _imopVarPre147 = fgetc(fp);
        }
        int *_imopVarPre151;
        int *_imopVarPre152;
        int *_imopVarPre153;
        _imopVarPre151 = &nz[lt];
        _imopVarPre152 = &ny[lt];
        _imopVarPre153 = &nx[lt];
        fscanf(fp, "%d%d%d", _imopVarPre153, _imopVarPre152, _imopVarPre151);
        int _imopVarPre155;
        _imopVarPre155 = fgetc(fp);
        while (_imopVarPre155 != '\n') {
            ;
            _imopVarPre155 = fgetc(fp);
        }
        int *_imopVarPre157;
        _imopVarPre157 = &nit;
        fscanf(fp, "%d", _imopVarPre157);
        int _imopVarPre159;
        _imopVarPre159 = fgetc(fp);
        while (_imopVarPre159 != '\n') {
            ;
            _imopVarPre159 = fgetc(fp);
        }
        for (i = 0; i <= 7; i++) {
            int *_imopVarPre161;
            _imopVarPre161 = &debug_vec[i];
            fscanf(fp, "%d", _imopVarPre161);
        }
        fclose(fp);
    } else {
        printf(" No input file. Using compiled defaults\n");
        lt = 5;
        nit = 4;
        nx[lt] = 32;
        ny[lt] = 32;
        nz[lt] = 32;
        for (i = 0; i <= 7; i++) {
            debug_vec[i] = 0;
        }
    }
    int _imopVarPre162;
    _imopVarPre162 = (nx[lt] != ny[lt]);
    if (!_imopVarPre162) {
        _imopVarPre162 = (nx[lt] != nz[lt]);
    }
    if (_imopVarPre162) {
        Class = 'U';
    } else {
        int _imopVarPre164;
        _imopVarPre164 = nx[lt] == 32;
        if (_imopVarPre164) {
            _imopVarPre164 = nit == 4;
        }
        if (_imopVarPre164) {
            Class = 'S';
        } else {
            int _imopVarPre166;
            _imopVarPre166 = nx[lt] == 64;
            if (_imopVarPre166) {
                _imopVarPre166 = nit == 40;
            }
            if (_imopVarPre166) {
                Class = 'W';
            } else {
                int _imopVarPre168;
                _imopVarPre168 = nx[lt] == 256;
                if (_imopVarPre168) {
                    _imopVarPre168 = nit == 20;
                }
                if (_imopVarPre168) {
                    Class = 'B';
                } else {
                    int _imopVarPre170;
                    _imopVarPre170 = nx[lt] == 512;
                    if (_imopVarPre170) {
                        _imopVarPre170 = nit == 20;
                    }
                    if (_imopVarPre170) {
                        Class = 'C';
                    } else {
                        int _imopVarPre172;
                        _imopVarPre172 = nx[lt] == 256;
                        if (_imopVarPre172) {
                            _imopVarPre172 = nit == 4;
                        }
                        if (_imopVarPre172) {
                            Class = 'A';
                        } else {
                            Class = 'U';
                        }
                    }
                }
            }
        }
    }
    a[0] = -8.0 / 3.0;
    a[1] = 0.0;
    a[2] = 1.0 / 6.0;
    a[3] = 1.0 / 12.0;
    int _imopVarPre173;
    int _imopVarPre174;
    _imopVarPre173 = Class == 'A';
    if (!_imopVarPre173) {
        _imopVarPre174 = Class == 'S';
        if (!_imopVarPre174) {
            _imopVarPre174 = Class == 'W';
        }
        _imopVarPre173 = _imopVarPre174;
    }
    if (_imopVarPre173) {
        c[0] = -3.0 / 8.0;
        c[1] = 1.0 / 32.0;
        c[2] = -1.0 / 64.0;
        c[3] = 0.0;
    } else {
        c[0] = -3.0 / 17.0;
        c[1] = 1.0 / 33.0;
        c[2] = -1.0 / 61.0;
        c[3] = 0.0;
    }
    lb = 1;
    int *_imopVarPre178;
    int *_imopVarPre179;
    int *_imopVarPre180;
    _imopVarPre178 = &n3;
    _imopVarPre179 = &n2;
    _imopVarPre180 = &n1;
    setup(_imopVarPre180, _imopVarPre179, _imopVarPre178, lt);
    unsigned long int _imopVarPre183;
    void *_imopVarPre184;
    _imopVarPre183 = (lt + 1) * sizeof(double ***);
    _imopVarPre184 = malloc(_imopVarPre183);
    u = (double ****) _imopVarPre184;
    for (l = lt; l >= 1; l--) {
        unsigned long int _imopVarPre187;
        void *_imopVarPre188;
        _imopVarPre187 = m3[l] * sizeof(double **);
        _imopVarPre188 = malloc(_imopVarPre187);
        u[l] = (double ***) _imopVarPre188;
        for (k = 0; k < m3[l]; k++) {
            unsigned long int _imopVarPre191;
            void *_imopVarPre192;
            _imopVarPre191 = m2[l] * sizeof(double *);
            _imopVarPre192 = malloc(_imopVarPre191);
            u[l][k] = (double **) _imopVarPre192;
            for (j = 0; j < m2[l]; j++) {
                unsigned long int _imopVarPre195;
                void *_imopVarPre196;
                _imopVarPre195 = m1[l] * sizeof(double);
                _imopVarPre196 = malloc(_imopVarPre195);
                u[l][k][j] = (double *) _imopVarPre196;
            }
        }
    }
    unsigned long int _imopVarPre199;
    void *_imopVarPre200;
    _imopVarPre199 = m3[lt] * sizeof(double **);
    _imopVarPre200 = malloc(_imopVarPre199);
    v = (double ***) _imopVarPre200;
    for (k = 0; k < m3[lt]; k++) {
        unsigned long int _imopVarPre203;
        void *_imopVarPre204;
        _imopVarPre203 = m2[lt] * sizeof(double *);
        _imopVarPre204 = malloc(_imopVarPre203);
        v[k] = (double **) _imopVarPre204;
        for (j = 0; j < m2[lt]; j++) {
            unsigned long int _imopVarPre207;
            void *_imopVarPre208;
            _imopVarPre207 = m1[lt] * sizeof(double);
            _imopVarPre208 = malloc(_imopVarPre207);
            v[k][j] = (double *) _imopVarPre208;
        }
    }
    unsigned long int _imopVarPre211;
    void *_imopVarPre212;
    _imopVarPre211 = (lt + 1) * sizeof(double ***);
    _imopVarPre212 = malloc(_imopVarPre211);
    r = (double ****) _imopVarPre212;
    for (l = lt; l >= 1; l--) {
        unsigned long int _imopVarPre215;
        void *_imopVarPre216;
        _imopVarPre215 = m3[l] * sizeof(double **);
        _imopVarPre216 = malloc(_imopVarPre215);
        r[l] = (double ***) _imopVarPre216;
        for (k = 0; k < m3[l]; k++) {
            unsigned long int _imopVarPre219;
            void *_imopVarPre220;
            _imopVarPre219 = m2[l] * sizeof(double *);
            _imopVarPre220 = malloc(_imopVarPre219);
            r[l][k] = (double **) _imopVarPre220;
            for (j = 0; j < m2[l]; j++) {
                unsigned long int _imopVarPre223;
                void *_imopVarPre224;
                _imopVarPre223 = m1[l] * sizeof(double);
                _imopVarPre224 = malloc(_imopVarPre223);
                r[l][k][j] = (double *) _imopVarPre224;
            }
        }
    }
    double ***_imopVarPre226;
    _imopVarPre226 = u[lt];
    zero3(_imopVarPre226, n1, n2, n3);
    int _imopVarPre229;
    int _imopVarPre230;
    _imopVarPre229 = ny[lt];
    _imopVarPre230 = nx[lt];
    zran3(v, n1, n2, n3, _imopVarPre230, _imopVarPre229, lt);
    int _imopVarPre236;
    int _imopVarPre237;
    int _imopVarPre238;
    double *_imopVarPre239;
    double *_imopVarPre240;
    _imopVarPre236 = nz[lt];
    _imopVarPre237 = ny[lt];
    _imopVarPre238 = nx[lt];
    _imopVarPre239 = &rnmu;
    _imopVarPre240 = &rnm2;
    norm2u3(v, n1, n2, n3, _imopVarPre240, _imopVarPre239, _imopVarPre238, _imopVarPre237, _imopVarPre236);
    int _imopVarPre244;
    int _imopVarPre245;
    int _imopVarPre246;
    _imopVarPre244 = nz[lt];
    _imopVarPre245 = ny[lt];
    _imopVarPre246 = nx[lt];
    printf(" Size: %3dx%3dx%3d (class %1c)\n", _imopVarPre246, _imopVarPre245, _imopVarPre244, Class);
    printf(" Iterations: %3d\n", nit);
    double ***_imopVarPre249;
    double ***_imopVarPre250;
    _imopVarPre249 = r[lt];
    _imopVarPre250 = u[lt];
    resid(_imopVarPre250, v, _imopVarPre249, n1, n2, n3, a, lt);
    int _imopVarPre257;
    int _imopVarPre258;
    int _imopVarPre259;
    double *_imopVarPre260;
    double *_imopVarPre261;
    double ***_imopVarPre262;
    _imopVarPre257 = nz[lt];
    _imopVarPre258 = ny[lt];
    _imopVarPre259 = nx[lt];
    _imopVarPre260 = &rnmu;
    _imopVarPre261 = &rnm2;
    _imopVarPre262 = r[lt];
    norm2u3(_imopVarPre262, n1, n2, n3, _imopVarPre261, _imopVarPre260, _imopVarPre259, _imopVarPre258, _imopVarPre257);
    mg3P(u, v, r, a, c, n1, n2, n3, lt);
    double ***_imopVarPre265;
    double ***_imopVarPre266;
    _imopVarPre265 = r[lt];
    _imopVarPre266 = u[lt];
    resid(_imopVarPre266, v, _imopVarPre265, n1, n2, n3, a, lt);
    int *_imopVarPre270;
    int *_imopVarPre271;
    int *_imopVarPre272;
    _imopVarPre270 = &n3;
    _imopVarPre271 = &n2;
    _imopVarPre272 = &n1;
    setup(_imopVarPre272, _imopVarPre271, _imopVarPre270, lt);
    double ***_imopVarPre274;
    _imopVarPre274 = u[lt];
    zero3(_imopVarPre274, n1, n2, n3);
    int _imopVarPre277;
    int _imopVarPre278;
    _imopVarPre277 = ny[lt];
    _imopVarPre278 = nx[lt];
    zran3(v, n1, n2, n3, _imopVarPre278, _imopVarPre277, lt);
    timer_stop(2);
    timer_start(1);
    double ***_imopVarPre281;
    double ***_imopVarPre282;
    _imopVarPre281 = r[lt];
    _imopVarPre282 = u[lt];
    resid(_imopVarPre282, v, _imopVarPre281, n1, n2, n3, a, lt);
    int _imopVarPre289;
    int _imopVarPre290;
    int _imopVarPre291;
    double *_imopVarPre292;
    double *_imopVarPre293;
    double ***_imopVarPre294;
    _imopVarPre289 = nz[lt];
    _imopVarPre290 = ny[lt];
    _imopVarPre291 = nx[lt];
    _imopVarPre292 = &rnmu;
    _imopVarPre293 = &rnm2;
    _imopVarPre294 = r[lt];
    norm2u3(_imopVarPre294, n1, n2, n3, _imopVarPre293, _imopVarPre292, _imopVarPre291, _imopVarPre290, _imopVarPre289);
    for (it = 1; it <= nit; it++) {
        mg3P(u, v, r, a, c, n1, n2, n3, lt);
        double ***_imopVarPre297;
        double ***_imopVarPre298;
        _imopVarPre297 = r[lt];
        _imopVarPre298 = u[lt];
        resid(_imopVarPre298, v, _imopVarPre297, n1, n2, n3, a, lt);
    }
    int _imopVarPre305;
    int _imopVarPre306;
    int _imopVarPre307;
    double *_imopVarPre308;
    double *_imopVarPre309;
    double ***_imopVarPre310;
    _imopVarPre305 = nz[lt];
    _imopVarPre306 = ny[lt];
    _imopVarPre307 = nx[lt];
    _imopVarPre308 = &rnmu;
    _imopVarPre309 = &rnm2;
    _imopVarPre310 = r[lt];
    norm2u3(_imopVarPre310, n1, n2, n3, _imopVarPre309, _imopVarPre308, _imopVarPre307, _imopVarPre306, _imopVarPre305);
#pragma omp parallel
    {
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
        } else {
            if (Class == 'W') {
                verify_value = 0.250391406439e-17;
            } else {
                if (Class == 'A') {
                    verify_value = 0.2433365309e-5;
                } else {
                    if (Class == 'B') {
                        verify_value = 0.180056440132e-5;
                    } else {
                        if (Class == 'C') {
                            verify_value = 0.570674826298e-06;
                        }
                    }
                }
            }
        }
        double _imopVarPre313;
        double _imopVarPre314;
        _imopVarPre313 = rnm2 - verify_value;
        _imopVarPre314 = fabs(_imopVarPre313);
        if (_imopVarPre314 <= epsilon) {
            verified = 1;
            printf(" VERIFICATION SUCCESSFUL\n");
            printf(" L2 Norm is %20.12e\n", rnm2);
            double _imopVarPre316;
            _imopVarPre316 = rnm2 - verify_value;
            printf(" Error is   %20.12e\n", _imopVarPre316);
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
    if (t != 0.0) {
        int nn = nx[lt] * ny[lt] * nz[lt];
        mflops = 58. * nit * nn * 1.0e-6 / t;
    } else {
        mflops = 0.0;
    }
    int _imopVarPre320;
    int _imopVarPre321;
    int _imopVarPre322;
    _imopVarPre320 = nz[lt];
    _imopVarPre321 = ny[lt];
    _imopVarPre322 = nx[lt];
    c_print_results("MG", Class, _imopVarPre322, _imopVarPre321, _imopVarPre320, nit, nthreads, t, mflops, "          floating point", verified, "3.0 structured", "21 Jul 2017", "gcc", "gcc", "(none)", "-I../common", "-O3 -fopenmp", "-O3 -fopenmp", "randdp");
}
static void setup(int *n1, int *n2 , int *n3 , int lt) {
    int k;
    for (k = lt - 1; k >= 1; k--) {
        nx[k] = nx[k + 1] / 2;
        ny[k] = ny[k + 1] / 2;
        nz[k] = nz[k + 1] / 2;
    }
    for (k = 1; k <= lt; k++) {
        m1[k] = nx[k] + 2;
        m2[k] = nz[k] + 2;
        m3[k] = ny[k] + 2;
    }
    is1 = 1;
    ie1 = nx[lt];
    *n1 = nx[lt] + 2;
    is2 = 1;
    ie2 = ny[lt];
    *n2 = ny[lt] + 2;
    is3 = 1;
    ie3 = nz[lt];
    *n3 = nz[lt] + 2;
    if (debug_vec[1] >= 1) {
        printf(" in setup, \n");
        printf("  lt  nx  ny  nz  n1  n2  n3 is1 is2 is3 ie1 ie2 ie3\n");
        int _imopVarPre329;
        int _imopVarPre330;
        int _imopVarPre331;
        int _imopVarPre332;
        int _imopVarPre333;
        int _imopVarPre334;
        _imopVarPre329 = *n3;
        _imopVarPre330 = *n2;
        _imopVarPre331 = *n1;
        _imopVarPre332 = nz[lt];
        _imopVarPre333 = ny[lt];
        _imopVarPre334 = nx[lt];
        printf("%4d%4d%4d%4d%4d%4d%4d%4d%4d%4d%4d%4d%4d\n", lt, _imopVarPre334, _imopVarPre333, _imopVarPre332, _imopVarPre331, _imopVarPre330, _imopVarPre329, is1, is2, is3, ie1, ie2, ie3);
    }
}
static void mg3P(double ****u, double ***v , double ****r , double a[4] , double c[4] , int n1 , int n2 , int n3 , int k) {
    int j;
    for (k = lt; k >= lb + 1; k--) {
        j = k - 1;
        int _imopVarPre343;
        int _imopVarPre344;
        int _imopVarPre345;
        double ***_imopVarPre346;
        int _imopVarPre347;
        int _imopVarPre348;
        int _imopVarPre349;
        double ***_imopVarPre350;
        _imopVarPre343 = m3[j];
        _imopVarPre344 = m2[j];
        _imopVarPre345 = m1[j];
        _imopVarPre346 = r[j];
        _imopVarPre347 = m3[k];
        _imopVarPre348 = m2[k];
        _imopVarPre349 = m1[k];
        _imopVarPre350 = r[k];
        rprj3(_imopVarPre350, _imopVarPre349, _imopVarPre348, _imopVarPre347, _imopVarPre346, _imopVarPre345, _imopVarPre344, _imopVarPre343, k);
    }
    k = lb;
    int _imopVarPre355;
    int _imopVarPre356;
    int _imopVarPre357;
    double ***_imopVarPre358;
    _imopVarPre355 = m3[lb];
    _imopVarPre356 = m2[lb];
    _imopVarPre357 = m1[lb];
    _imopVarPre358 = u[lb];
    zero3(_imopVarPre358, _imopVarPre357, _imopVarPre356, _imopVarPre355);
    int _imopVarPre364;
    int _imopVarPre365;
    int _imopVarPre366;
    double ***_imopVarPre367;
    double ***_imopVarPre368;
    _imopVarPre364 = m3[k];
    _imopVarPre365 = m2[k];
    _imopVarPre366 = m1[k];
    _imopVarPre367 = u[k];
    _imopVarPre368 = r[k];
    psinv(_imopVarPre368, _imopVarPre367, _imopVarPre366, _imopVarPre365, _imopVarPre364, c, k);
    for (k = lb + 1; k <= lt - 1; k++) {
        j = k - 1;
        int _imopVarPre373;
        int _imopVarPre374;
        int _imopVarPre375;
        double ***_imopVarPre376;
        _imopVarPre373 = m3[k];
        _imopVarPre374 = m2[k];
        _imopVarPre375 = m1[k];
        _imopVarPre376 = u[k];
        zero3(_imopVarPre376, _imopVarPre375, _imopVarPre374, _imopVarPre373);
        int _imopVarPre385;
        int _imopVarPre386;
        int _imopVarPre387;
        double ***_imopVarPre388;
        int _imopVarPre389;
        int _imopVarPre390;
        int _imopVarPre391;
        double ***_imopVarPre392;
        _imopVarPre385 = m3[k];
        _imopVarPre386 = m2[k];
        _imopVarPre387 = m1[k];
        _imopVarPre388 = u[k];
        _imopVarPre389 = m3[j];
        _imopVarPre390 = m2[j];
        _imopVarPre391 = m1[j];
        _imopVarPre392 = u[j];
        interp(_imopVarPre392, _imopVarPre391, _imopVarPre390, _imopVarPre389, _imopVarPre388, _imopVarPre387, _imopVarPre386, _imopVarPre385, k);
        int _imopVarPre399;
        int _imopVarPre400;
        int _imopVarPre401;
        double ***_imopVarPre402;
        double ***_imopVarPre403;
        double ***_imopVarPre404;
        _imopVarPre399 = m3[k];
        _imopVarPre400 = m2[k];
        _imopVarPre401 = m1[k];
        _imopVarPre402 = r[k];
        _imopVarPre403 = r[k];
        _imopVarPre404 = u[k];
        resid(_imopVarPre404, _imopVarPre403, _imopVarPre402, _imopVarPre401, _imopVarPre400, _imopVarPre399, a, k);
        int _imopVarPre410;
        int _imopVarPre411;
        int _imopVarPre412;
        double ***_imopVarPre413;
        double ***_imopVarPre414;
        _imopVarPre410 = m3[k];
        _imopVarPre411 = m2[k];
        _imopVarPre412 = m1[k];
        _imopVarPre413 = u[k];
        _imopVarPre414 = r[k];
        psinv(_imopVarPre414, _imopVarPre413, _imopVarPre412, _imopVarPre411, _imopVarPre410, c, k);
    }
    j = lt - 1;
    double ***_imopVarPre420;
    int _imopVarPre421;
    int _imopVarPre422;
    int _imopVarPre423;
    double ***_imopVarPre424;
    _imopVarPre420 = u[lt];
    _imopVarPre421 = m3[j];
    _imopVarPre422 = m2[j];
    _imopVarPre423 = m1[j];
    _imopVarPre424 = u[j];
    interp(_imopVarPre424, _imopVarPre423, _imopVarPre422, _imopVarPre421, _imopVarPre420, n1, n2, n3, lt);
    double ***_imopVarPre427;
    double ***_imopVarPre428;
    _imopVarPre427 = r[lt];
    _imopVarPre428 = u[lt];
    resid(_imopVarPre428, v, _imopVarPre427, n1, n2, n3, a, lt);
    double ***_imopVarPre431;
    double ***_imopVarPre432;
    _imopVarPre431 = u[lt];
    _imopVarPre432 = r[lt];
    psinv(_imopVarPre432, _imopVarPre431, n1, n2, n3, c, k);
}
static void psinv(double ***r, double ***u , int n1 , int n2 , int n3 , double c[4] , int k) {
    int i3;
    int i2;
    int i1;
    double r1[1037];
    double r2[1037];
#pragma omp parallel default(shared) private(i1, i2, i3, r1, r2)
    {
#pragma omp for nowait
        for (i3 = 1; i3 < n3 - 1; i3++) {
            for (i2 = 1; i2 < n2 - 1; i2++) {
                for (i1 = 0; i1 < n1; i1++) {
                    r1[i1] = r[i3][i2 - 1][i1] + r[i3][i2 + 1][i1] + r[i3 - 1][i2][i1] + r[i3 + 1][i2][i1];
                    r2[i1] = r[i3 - 1][i2 - 1][i1] + r[i3 - 1][i2 + 1][i1] + r[i3 + 1][i2 - 1][i1] + r[i3 + 1][i2 + 1][i1];
                }
                for (i1 = 1; i1 < n1 - 1; i1++) {
                    u[i3][i2][i1] = u[i3][i2][i1] + c[0] * r[i3][i2][i1] + c[1] * (r[i3][i2][i1 - 1] + r[i3][i2][i1 + 1] + r1[i1]) + c[2] * (r2[i1] + r1[i1 - 1] + r1[i1 + 1]);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    }
    comm3(u, n1, n2, n3, k);
    if (debug_vec[0] >= 1) {
        rep_nrm(u, n1, n2, n3, "   psinv", k);
    }
    if (debug_vec[3] >= k) {
        showall(u, n1, n2, n3);
    }
}
static void resid(double ***u, double ***v , double ***r , int n1 , int n2 , int n3 , double a[4] , int k) {
    int i3;
    int i2;
    int i1;
    double u1[1037];
    double u2[1037];
#pragma omp parallel default(shared) private(i1, i2, i3, u1, u2)
    {
#pragma omp for nowait
        for (i3 = 1; i3 < n3 - 1; i3++) {
            for (i2 = 1; i2 < n2 - 1; i2++) {
                for (i1 = 0; i1 < n1; i1++) {
                    u1[i1] = u[i3][i2 - 1][i1] + u[i3][i2 + 1][i1] + u[i3 - 1][i2][i1] + u[i3 + 1][i2][i1];
                    u2[i1] = u[i3 - 1][i2 - 1][i1] + u[i3 - 1][i2 + 1][i1] + u[i3 + 1][i2 - 1][i1] + u[i3 + 1][i2 + 1][i1];
                }
                for (i1 = 1; i1 < n1 - 1; i1++) {
                    r[i3][i2][i1] = v[i3][i2][i1] - a[0] * u[i3][i2][i1] - a[2] * (u2[i1] + u1[i1 - 1] + u1[i1 + 1]) - a[3] * (u2[i1 - 1] + u2[i1 + 1]);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    }
    comm3(r, n1, n2, n3, k);
    if (debug_vec[0] >= 1) {
        rep_nrm(r, n1, n2, n3, "   resid", k);
    }
    if (debug_vec[2] >= k) {
        showall(r, n1, n2, n3);
    }
}
static void rprj3(double ***r, int m1k , int m2k , int m3k , double ***s , int m1j , int m2j , int m3j , int k) {
    int j3;
    int j2;
    int j1;
    int i3;
    int i2;
    int i1;
    int d1;
    int d2;
    int d3;
    double x1[1037];
    double y1[1037];
    double x2;
    double y2;
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
#pragma omp parallel default(shared) private(j1, j2, j3, i1, i2, i3, x1, y1, x2, y2)
    {
#pragma omp for nowait
        for (j3 = 1; j3 < m3j - 1; j3++) {
            i3 = 2 * j3 - d3;
            for (j2 = 1; j2 < m2j - 1; j2++) {
                i2 = 2 * j2 - d2;
                for (j1 = 1; j1 < m1j; j1++) {
                    i1 = 2 * j1 - d1;
                    x1[i1] = r[i3 + 1][i2][i1] + r[i3 + 1][i2 + 2][i1] + r[i3][i2 + 1][i1] + r[i3 + 2][i2 + 1][i1];
                    y1[i1] = r[i3][i2][i1] + r[i3 + 2][i2][i1] + r[i3][i2 + 2][i1] + r[i3 + 2][i2 + 2][i1];
                }
                for (j1 = 1; j1 < m1j - 1; j1++) {
                    i1 = 2 * j1 - d1;
                    y2 = r[i3][i2][i1 + 1] + r[i3 + 2][i2][i1 + 1] + r[i3][i2 + 2][i1 + 1] + r[i3 + 2][i2 + 2][i1 + 1];
                    x2 = r[i3 + 1][i2][i1 + 1] + r[i3 + 1][i2 + 2][i1 + 1] + r[i3][i2 + 1][i1 + 1] + r[i3 + 2][i2 + 1][i1 + 1];
                    s[j3][j2][j1] = 0.5 * r[i3 + 1][i2 + 1][i1 + 1] + 0.25 * (r[i3 + 1][i2 + 1][i1] + r[i3 + 1][i2 + 1][i1 + 2] + x2) + 0.125 * (x1[i1] + x1[i1 + 2] + y2) + 0.0625 * (y1[i1] + y1[i1 + 2]);
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    }
    int _imopVarPre434;
    _imopVarPre434 = k - 1;
    comm3(s, m1j, m2j, m3j, _imopVarPre434);
    if (debug_vec[0] >= 1) {
        int _imopVarPre436;
        _imopVarPre436 = k - 1;
        rep_nrm(s, m1j, m2j, m3j, "   rprj3", _imopVarPre436);
    }
    if (debug_vec[4] >= k) {
        showall(s, m1j, m2j, m3j);
    }
}
static void interp(double ***z, int mm1 , int mm2 , int mm3 , double ***u , int n1 , int n2 , int n3 , int k) {
    int i3;
    int i2;
    int i1;
    int d1;
    int d2;
    int d3;
    int t1;
    int t2;
    int t3;
    double z1[1037];
    double z2[1037];
    double z3[1037];
    int _imopVarPre439;
    int _imopVarPre440;
    _imopVarPre439 = n1 != 3;
    if (_imopVarPre439) {
        _imopVarPre440 = n2 != 3;
        if (_imopVarPre440) {
            _imopVarPre440 = n3 != 3;
        }
        _imopVarPre439 = _imopVarPre440;
    }
    if (_imopVarPre439) {
#pragma omp parallel default(shared) private(i1, i2, i3, z1, z2, z3)
        {
#pragma omp for nowait
            for (i3 = 0; i3 < mm3 - 1; i3++) {
                for (i2 = 0; i2 < mm2 - 1; i2++) {
                    for (i1 = 0; i1 < mm1; i1++) {
                        z1[i1] = z[i3][i2 + 1][i1] + z[i3][i2][i1];
                        z2[i1] = z[i3 + 1][i2][i1] + z[i3][i2][i1];
                        z3[i1] = z[i3 + 1][i2 + 1][i1] + z[i3 + 1][i2][i1] + z1[i1];
                    }
                    for (i1 = 0; i1 < mm1 - 1; i1++) {
                        u[2 * i3][2 * i2][2 * i1] = u[2 * i3][2 * i2][2 * i1] + z[i3][i2][i1];
                        u[2 * i3][2 * i2][2 * i1 + 1] = u[2 * i3][2 * i2][2 * i1 + 1] + 0.5 * (z[i3][i2][i1 + 1] + z[i3][i2][i1]);
                    }
                    for (i1 = 0; i1 < mm1 - 1; i1++) {
                        u[2 * i3][2 * i2 + 1][2 * i1] = u[2 * i3][2 * i2 + 1][2 * i1] + 0.5 * z1[i1];
                        u[2 * i3][2 * i2 + 1][2 * i1 + 1] = u[2 * i3][2 * i2 + 1][2 * i1 + 1] + 0.25 * (z1[i1] + z1[i1 + 1]);
                    }
                    for (i1 = 0; i1 < mm1 - 1; i1++) {
                        u[2 * i3 + 1][2 * i2][2 * i1] = u[2 * i3 + 1][2 * i2][2 * i1] + 0.5 * z2[i1];
                        u[2 * i3 + 1][2 * i2][2 * i1 + 1] = u[2 * i3 + 1][2 * i2][2 * i1 + 1] + 0.25 * (z2[i1] + z2[i1 + 1]);
                    }
                    for (i1 = 0; i1 < mm1 - 1; i1++) {
                        u[2 * i3 + 1][2 * i2 + 1][2 * i1] = u[2 * i3 + 1][2 * i2 + 1][2 * i1] + 0.25 * z3[i1];
                        u[2 * i3 + 1][2 * i2 + 1][2 * i1 + 1] = u[2 * i3 + 1][2 * i2 + 1][2 * i1 + 1] + 0.125 * (z3[i1] + z3[i1 + 1]);
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read([i3, m2.f, u, m3, heapCell#5, nz.f, _imopVarPre491, n1, resid, tmp, n3, norm2u3, r, heapCell#3, nullCell, m2, m1.f, nz, ny.f, n2, n2, heapCell#9, _imopVarPre495, showall, i3, debug_vec.f, _imopVarPre487, lb, heapCell#1, heapCell#0, m1, s, n3, n1, nx.f, ny, debug_vec, heapCell#10, r, a.f, heapCell#7, v, m3.f, nx, a, heapCell#8, heapCell#2, printf, fabs, heapCell#4, rep_nrm, heapCell#6])
#pragma omp barrier
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
#pragma omp parallel default(shared) private(i1, i2, i3)
        {
#pragma omp for nowait
            for (i3 = d3; i3 <= mm3 - 1; i3++) {
                for (i2 = d2; i2 <= mm2 - 1; i2++) {
                    for (i1 = d1; i1 <= mm1 - 1; i1++) {
                        u[2 * i3 - d3 - 1][2 * i2 - d2 - 1][2 * i1 - d1 - 1] = u[2 * i3 - d3 - 1][2 * i2 - d2 - 1][2 * i1 - d1 - 1] + z[i3 - 1][i2 - 1][i1 - 1];
                    }
                    for (i1 = 1; i1 <= mm1 - 1; i1++) {
                        u[2 * i3 - d3 - 1][2 * i2 - d2 - 1][2 * i1 - t1 - 1] = u[2 * i3 - d3 - 1][2 * i2 - d2 - 1][2 * i1 - t1 - 1] + 0.5 * (z[i3 - 1][i2 - 1][i1] + z[i3 - 1][i2 - 1][i1 - 1]);
                    }
                }
                for (i2 = 1; i2 <= mm2 - 1; i2++) {
                    for (i1 = d1; i1 <= mm1 - 1; i1++) {
                        u[2 * i3 - d3 - 1][2 * i2 - t2 - 1][2 * i1 - d1 - 1] = u[2 * i3 - d3 - 1][2 * i2 - t2 - 1][2 * i1 - d1 - 1] + 0.5 * (z[i3 - 1][i2][i1 - 1] + z[i3 - 1][i2 - 1][i1 - 1]);
                    }
                    for (i1 = 1; i1 <= mm1 - 1; i1++) {
                        u[2 * i3 - d3 - 1][2 * i2 - t2 - 1][2 * i1 - t1 - 1] = u[2 * i3 - d3 - 1][2 * i2 - t2 - 1][2 * i1 - t1 - 1] + 0.25 * (z[i3 - 1][i2][i1] + z[i3 - 1][i2 - 1][i1] + z[i3 - 1][i2][i1 - 1] + z[i3 - 1][i2 - 1][i1 - 1]);
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written() read([i3, u, m2.f, m3, heapCell#5, nz.f, u, _imopVarPre491, n1, resid, t3, tmp, n3, r, norm2u3, heapCell#3, nullCell, m2, m1.f, mm1, nz, ny.f, n2, n2, heapCell#9, z, t2, _imopVarPre495, showall, i3, debug_vec.f, _imopVarPre487, lb, heapCell#1, heapCell#0, m1, d2, mm2, s, n3, n1, nx.f, ny, t1, debug_vec, heapCell#10, r, a.f, d1, heapCell#7, i3, v, mm3, m3.f, a, nx, heapCell#8, heapCell#2, printf, fabs, heapCell#4, rep_nrm, heapCell#6])
#pragma omp barrier
#pragma omp for nowait
            for (i3 = 1; i3 <= mm3 - 1; i3++) {
                for (i2 = d2; i2 <= mm2 - 1; i2++) {
                    for (i1 = d1; i1 <= mm1 - 1; i1++) {
                        u[2 * i3 - t3 - 1][2 * i2 - d2 - 1][2 * i1 - d1 - 1] = u[2 * i3 - t3 - 1][2 * i2 - d2 - 1][2 * i1 - d1 - 1] + 0.5 * (z[i3][i2 - 1][i1 - 1] + z[i3 - 1][i2 - 1][i1 - 1]);
                    }
                    for (i1 = 1; i1 <= mm1 - 1; i1++) {
                        u[2 * i3 - t3 - 1][2 * i2 - d2 - 1][2 * i1 - t1 - 1] = u[2 * i3 - t3 - 1][2 * i2 - d2 - 1][2 * i1 - t1 - 1] + 0.25 * (z[i3][i2 - 1][i1] + z[i3][i2 - 1][i1 - 1] + z[i3 - 1][i2 - 1][i1] + z[i3 - 1][i2 - 1][i1 - 1]);
                    }
                }
                for (i2 = 1; i2 <= mm2 - 1; i2++) {
                    for (i1 = d1; i1 <= mm1 - 1; i1++) {
                        u[2 * i3 - t3 - 1][2 * i2 - t2 - 1][2 * i1 - d1 - 1] = u[2 * i3 - t3 - 1][2 * i2 - t2 - 1][2 * i1 - d1 - 1] + 0.25 * (z[i3][i2][i1 - 1] + z[i3][i2 - 1][i1 - 1] + z[i3 - 1][i2][i1 - 1] + z[i3 - 1][i2 - 1][i1 - 1]);
                    }
                    for (i1 = 1; i1 <= mm1 - 1; i1++) {
                        u[2 * i3 - t3 - 1][2 * i2 - t2 - 1][2 * i1 - t1 - 1] = u[2 * i3 - t3 - 1][2 * i2 - t2 - 1][2 * i1 - t1 - 1] + 0.125 * (z[i3][i2][i1] + z[i3][i2 - 1][i1] + z[i3][i2][i1 - 1] + z[i3][i2 - 1][i1 - 1] + z[i3 - 1][i2][i1] + z[i3 - 1][i2 - 1][i1] + z[i3 - 1][i2][i1 - 1] + z[i3 - 1][i2 - 1][i1 - 1]);
                    }
                }
            }
        }
    }
    if (debug_vec[0] >= 1) {
        int _imopVarPre442;
        _imopVarPre442 = k - 1;
        rep_nrm(z, mm1, mm2, mm3, "z: inter", _imopVarPre442);
        rep_nrm(u, n1, n2, n3, "u: inter", k);
    }
    if (debug_vec[5] >= k) {
        showall(z, mm1, mm2, mm3);
        showall(u, n1, n2, n3);
    }
}
static void norm2u3(double ***r, int n1 , int n2 , int n3 , double *rnm2 , double *rnmu , int nx , int ny , int nz) {
    double s = 0.0;
    int i3;
    int i2;
    int i1;
    int n;
    double a = 0.0;
    double tmp = 0.0;
    n = nx * ny * nz;
#pragma omp parallel default(shared) private(i1, i2, i3, a) reduction(+:s) reduction(max:tmp)
    {
#pragma omp for nowait
        for (i3 = 1; i3 < n3 - 1; i3++) {
            for (i2 = 1; i2 < n2 - 1; i2++) {
                for (i1 = 1; i1 < n1 - 1; i1++) {
                    s = s + r[i3][i2][i1] * r[i3][i2][i1];
                    double _imopVarPre444;
                    double _imopVarPre445;
                    _imopVarPre444 = r[i3][i2][i1];
                    _imopVarPre445 = fabs(_imopVarPre444);
                    if (_imopVarPre445 > tmp) {
                        tmp = _imopVarPre445;
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written([tmp, heapCell#7, s, heapCell#10, heapCell#6, nullCell]) read([ie1, i3, r, sqrt, m2.f, u, m3, timer_stop, heapCell#5, nz.f, m3j, _imopVarPre491, n2, n1, resid, d2, d1, n1, tmp, n3, c.f, norm2u3, r, heapCell#3, nullCell, m2, u, m1.f, ie2, nz, ny.f, n2, n2, heapCell#9, n3, _imopVarPre495, showall, i3, malloc, debug_vec.f, lb, is1, _imopVarPre487, heapCell#1, zero3, heapCell#0, i3, m1, mg3P, z, ie3, s, n3, m2j, n1, ny, r, nx.f, n3, psinv, j3, debug_vec, is2, heapCell#10, setup, r, i3, a.f, heapCell#7, lt, c_print_results, Class, v, m3.f, rprj3, a, nx, s, n1, heapCell#8, heapCell#2, d3, printf, c, n2, fabs, is3, m1j, heapCell#4, rep_nrm, heapCell#6, timer_read])
#pragma omp barrier
    }
    *rnmu = tmp;
    double _imopVarPre447;
    double _imopVarPre448;
    _imopVarPre447 = s / (double) n;
    _imopVarPre448 = sqrt(_imopVarPre447);
    *rnm2 = _imopVarPre448;
}
static void rep_nrm(double ***u, int n1 , int n2 , int n3 , char *title , int kk) {
    double rnm2;
    double rnmu;
    int _imopVarPre454;
    int _imopVarPre455;
    int _imopVarPre456;
    double *_imopVarPre457;
    double *_imopVarPre458;
    _imopVarPre454 = nz[kk];
    _imopVarPre455 = ny[kk];
    _imopVarPre456 = nx[kk];
    _imopVarPre457 = &rnmu;
    _imopVarPre458 = &rnm2;
    norm2u3(u, n1, n2, n3, _imopVarPre458, _imopVarPre457, _imopVarPre456, _imopVarPre455, _imopVarPre454);
    printf(" Level%2d in %8s: norms =%21.14e%21.14e\n", kk, title, rnm2, rnmu);
}
static void comm3(double ***u, int n1 , int n2 , int n3 , int kk) {
    int i1;
    int i2;
    int i3;
#pragma omp parallel default(shared) private(i1, i2, i3)
    {
#pragma omp for nowait
        for (i3 = 1; i3 < n3 - 1; i3++) {
            for (i2 = 1; i2 < n2 - 1; i2++) {
                u[i3][i2][n1 - 1] = u[i3][i2][1];
                u[i3][i2][0] = u[i3][i2][n1 - 2];
            }
            for (i1 = 0; i1 < n1; i1++) {
                u[i3][n2 - 1][i1] = u[i3][1][i1];
                u[i3][0][i1] = u[i3][n2 - 2][i1];
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read([ie1, i3, r, n2, m2.f, u, m3, heapCell#5, nz.f, m3j, _imopVarPre491, n2, n1, resid, d2, d1, n1, tmp, n3, c.f, norm2u3, r, heapCell#3, nullCell, m2, u, m1.f, n3, ie2, nz, ny.f, n2, n2, heapCell#9, n3, _imopVarPre495, showall, i3, malloc, debug_vec.f, lb, is1, _imopVarPre487, heapCell#1, zero3, heapCell#0, i3, m1, mg3P, z, ie3, s, n3, m2j, n1, ny, r, nx.f, u, n3, psinv, j3, debug_vec, is2, i2, heapCell#10, setup, r, i3, a.f, heapCell#7, lt, v, m3.f, rprj3, n1, a, nx, s, n1, heapCell#8, heapCell#2, d3, printf, c, n2, fabs, is3, m1j, heapCell#4, rep_nrm, heapCell#6])
#pragma omp barrier
#pragma omp for nowait
        for (i2 = 0; i2 < n2; i2++) {
            for (i1 = 0; i1 < n1; i1++) {
                u[n3 - 1][i2][i1] = u[1][i2][i1];
                u[0][i2][i1] = u[n3 - 2][i2][i1];
            }
        }
    }
}
static void zran3(double ***z, int n1 , int n2 , int n3 , int nx , int ny , int k) {
    int i0;
    int m0;
    int m1;
    int i1;
    int i2;
    int i3;
    int d1;
    int e1;
    int e2;
    int e3;
    double xx;
    double x0;
    double x1;
    double a1;
    double a2;
    double ai;
    double ten[10][2];
    double best;
    int i;
    int j1[10][2];
    int j2[10][2];
    int j3[10][2];
    int jg[4][10][2];
    double rdummy;
    double _imopVarPre460;
    double _imopVarPre461;
    _imopVarPre460 = pow(5.0, 13);
    _imopVarPre461 = power(_imopVarPre460, nx);
    a1 = _imopVarPre461;
    int _imopVarPre464;
    double _imopVarPre465;
    double _imopVarPre466;
    _imopVarPre464 = nx * ny;
    _imopVarPre465 = pow(5.0, 13);
    _imopVarPre466 = power(_imopVarPre465, _imopVarPre464);
    a2 = _imopVarPre466;
    zero3(z, n1, n2, n3);
    i = is1 - 1 + nx * (is2 - 1 + ny * (is3 - 1));
    double _imopVarPre468;
    double _imopVarPre469;
    _imopVarPre468 = pow(5.0, 13);
    _imopVarPre469 = power(_imopVarPre468, i);
    d1 = ie1 - is1 + 1;
    e1 = ie1 - is1 + 2;
    e2 = ie2 - is2 + 2;
    e3 = ie3 - is3 + 2;
    x0 = 314159265.e0;
    double *_imopVarPre471;
    double _imopVarPre472;
    _imopVarPre471 = &x0;
    _imopVarPre472 = randlc(_imopVarPre471, _imopVarPre469);
    rdummy = _imopVarPre472;
    for (i3 = 1; i3 < e3; i3++) {
        x1 = x0;
        for (i2 = 1; i2 < e2; i2++) {
            xx = x0;
            double *_imopVarPre476;
            double _imopVarPre477;
            double *_imopVarPre478;
            _imopVarPre476 = &(z[i3][i2][0]);
            _imopVarPre477 = pow(5.0, 13);
            _imopVarPre478 = &xx;
            vranlc(d1, _imopVarPre478, _imopVarPre477, _imopVarPre476);
            double *_imopVarPre480;
            double _imopVarPre481;
            _imopVarPre480 = &x1;
            _imopVarPre481 = randlc(_imopVarPre480, a1);
            rdummy = _imopVarPre481;
        }
        double *_imopVarPre483;
        double _imopVarPre484;
        _imopVarPre483 = &x0;
        _imopVarPre484 = randlc(_imopVarPre483, a2);
        rdummy = _imopVarPre484;
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
    for (i3 = 1; i3 < n3 - 1; i3++) {
        for (i2 = 1; i2 < n2 - 1; i2++) {
            for (i1 = 1; i1 < n1 - 1; i1++) {
                if (z[i3][i2][i1] > ten[0][1]) {
                    ten[0][1] = z[i3][i2][i1];
                    j1[0][1] = i1;
                    j2[0][1] = i2;
                    j3[0][1] = i3;
                    bubble(ten, j1, j2, j3, 10, 1);
                }
                if (z[i3][i2][i1] < ten[0][0]) {
                    ten[0][0] = z[i3][i2][i1];
                    j1[0][0] = i1;
                    j2[0][0] = i2;
                    j3[0][0] = i3;
                    bubble(ten, j1, j2, j3, 10, 0);
                }
            }
        }
    }
    i1 = 10 - 1;
    i0 = 10 - 1;
    for (i = 10 - 1; i >= 0; i--) {
        best = z[j3[i1][1]][j2[i1][1]][j1[i1][1]];
        if (best == z[j3[i1][1]][j2[i1][1]][j1[i1][1]]) {
            jg[0][i][1] = 0;
            jg[1][i][1] = is1 - 1 + j1[i1][1];
            jg[2][i][1] = is2 - 1 + j2[i1][1];
            jg[3][i][1] = is3 - 1 + j3[i1][1];
            i1 = i1 - 1;
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
            i0 = i0 - 1;
        } else {
            jg[0][i][0] = 0;
            jg[1][i][0] = 0;
            jg[2][i][0] = 0;
            jg[3][i][0] = 0;
        }
        ten[i][0] = best;
    }
    m1 = i1 + 1;
    m0 = i0 + 1;
#pragma omp parallel private(i2, i1)
    {
#pragma omp for nowait
        for (i3 = 0; i3 < n3; i3++) {
            for (i2 = 0; i2 < n2; i2++) {
                for (i1 = 0; i1 < n1; i1++) {
                    z[i3][i2][i1] = 0.0;
                }
            }
        }
        // #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    }
    for (i = 10 - 1; i >= m0; i--) {
        z[j3[i][0]][j2[i][0]][j1[i][0]] = -1.0;
    }
    for (i = 10 - 1; i >= m1; i--) {
        z[j3[i][1]][j2[i][1]][j1[i][1]] = 1.0;
    }
    comm3(z, n1, n2, n3, lt);
}
static void showall(double ***z, int n1 , int n2 , int n3) {
    int i1;
    int i2;
    int i3;
    int m1;
    int m2;
    int m3;
    int _imopVarPre487;
    int _imopVarPre488;
    _imopVarPre487 = (n1 < 18);
    if (_imopVarPre487) {
        _imopVarPre488 = n1;
    } else {
        _imopVarPre488 = 18;
    }
    int _imopVarPre491;
    int _imopVarPre492;
    _imopVarPre491 = (n2 < 14);
    if (_imopVarPre491) {
        _imopVarPre492 = n2;
    } else {
        _imopVarPre492 = 14;
    }
    int _imopVarPre495;
    int _imopVarPre496;
    _imopVarPre495 = (n3 < 18);
    if (_imopVarPre495) {
        _imopVarPre496 = n3;
    } else {
        _imopVarPre496 = 18;
    }
    printf("\n");
    for (i3 = 0; i3 < _imopVarPre496; i3++) {
        for (i1 = 0; i1 < _imopVarPre488; i1++) {
            for (i2 = 0; i2 < _imopVarPre492; i2++) {
                double _imopVarPre498;
                _imopVarPre498 = z[i3][i2][i1];
                printf("%6.3f", _imopVarPre498);
            }
            printf("\n");
        }
        printf(" - - - - - - - \n");
    }
    printf("\n");
}
static double power(double a, int n) {
    double aj;
    int nj;
    double rdummy;
    double power;
    power = 1.0;
    nj = n;
    while (nj != 0) {
        if ((nj % 2) == 1) {
            double *_imopVarPre500;
            double _imopVarPre501;
            _imopVarPre500 = &power;
            _imopVarPre501 = randlc(_imopVarPre500, a);
            rdummy = _imopVarPre501;
        }
        double *_imopVarPre503;
        double _imopVarPre504;
        _imopVarPre503 = &aj;
        _imopVarPre504 = randlc(_imopVarPre503, a);
        rdummy = _imopVarPre504;
        nj = nj / 2;
    }
    return power;
}
static void bubble(double ten[1037][2], int j1[1037][2] , int j2[1037][2] , int j3[1037][2] , int m , int ind) {
    double temp;
    int i;
    int j_temp;
    if (ind == 1) {
        for (i = 0; i < m - 1; i++) {
            if (ten[i][ind] > ten[i + 1][ind]) {
                temp = ten[i + 1][ind];
                ten[i + 1][ind] = ten[i][ind];
                ten[i][ind] = temp;
                j_temp = j1[i + 1][ind];
                j1[i + 1][ind] = j1[i][ind];
                j1[i][ind] = j_temp;
                j_temp = j2[i + 1][ind];
                j2[i + 1][ind] = j2[i][ind];
                j2[i][ind] = j_temp;
                j_temp = j3[i + 1][ind];
                j3[i + 1][ind] = j3[i][ind];
                j3[i][ind] = j_temp;
            } else {
                return;
            }
        }
    } else {
        for (i = 0; i < m - 1; i++) {
            if (ten[i][ind] < ten[i + 1][ind]) {
                temp = ten[i + 1][ind];
                ten[i + 1][ind] = ten[i][ind];
                ten[i][ind] = temp;
                j_temp = j1[i + 1][ind];
                j1[i + 1][ind] = j1[i][ind];
                j1[i][ind] = j_temp;
                j_temp = j2[i + 1][ind];
                j2[i + 1][ind] = j2[i][ind];
                j2[i][ind] = j_temp;
                j_temp = j3[i + 1][ind];
                j3[i + 1][ind] = j3[i][ind];
                j3[i][ind] = j_temp;
            } else {
                return;
            }
        }
    }
}
static void zero3(double ***z, int n1 , int n2 , int n3) {
    int i1;
    int i2;
    int i3;
#pragma omp parallel private(i1, i2, i3)
    {
#pragma omp for nowait
        for (i3 = 0; i3 < n3; i3++) {
            for (i2 = 0; i2 < n2; i2++) {
                for (i1 = 0; i1 < n1; i1++) {
                    z[i3][i2][i1] = 0.0;
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written() read()
#pragma omp barrier
    }
}
