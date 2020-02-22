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
static double ce[13][5];
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
static double bt;
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
static double u[5][12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1];
static double us[12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1];
static double vs[12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1];
static double ws[12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1];
static double qs[12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1];
static double ainv[12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1];
static double rho_i[12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1];
static double speed[12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1];
static double square[12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1];
static double rhs[5][12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1];
static double forcing[5][12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1];
static double lhs[15][12 / 2 * 2 + 1][12 / 2 * 2 + 1][12 / 2 * 2 + 1];
static double cv[12];
static double rhon[12];
static double rhos[12];
static double rhoq[12];
static double cuf[12];
static double q[12];
static double ue[5][12];
static double buf[5][12];
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
static void ninvr(void );
static void pinvr(void );
static void compute_rhs(void );
static void set_constants(void );
static void txinvr(void );
static void tzetar(void );
static void verify(int no_time_steps, char *class , boolean *verified);
static void x_solve(void );
static void y_solve(void );
static void z_solve(void );
int main(int argc, char **argv) {
    int niter;
    int step;
    double mflops;
    double tmax;
    int nthreads = 1;
    boolean verified;
    char class;
    FILE *fp;
    printf("\n\n NAS Parallel Benchmarks 3.0 structured OpenMP C version" " - SP Benchmark\n\n");
    fp = fopen("inputsp.data", "r");
    if (fp != ((void *) 0)) {
        printf(" Reading from input file inputsp.data\n");
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
        fscanf(fp, "%lf", _imopVarPre149);
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
        printf(" No input file inputsp.data. Using compiled defaults");
        niter = 100;
        dt = 0.015;
        grid_points[0] = 12;
        grid_points[1] = 12;
        grid_points[2] = 12;
    }
    int _imopVarPre161;
    int _imopVarPre162;
    int _imopVarPre163;
    _imopVarPre161 = grid_points[2];
    _imopVarPre162 = grid_points[1];
    _imopVarPre163 = grid_points[0];
    printf(" Size: %3dx%3dx%3d\n", _imopVarPre163, _imopVarPre162, _imopVarPre161);
    printf(" Iterations: %3d   dt: %10.6f\n", niter, dt);
    int _imopVarPre164;
    int _imopVarPre165;
    _imopVarPre164 = (grid_points[0] > 12);
    if (!_imopVarPre164) {
        _imopVarPre165 = (grid_points[1] > 12);
        if (!_imopVarPre165) {
            _imopVarPre165 = (grid_points[2] > 12);
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
        printf("%d, %d, %d\n", _imopVarPre171, _imopVarPre170, _imopVarPre169);
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
        int _imopVarPre172;
        _imopVarPre172 = step % 20 == 0;
        if (!_imopVarPre172) {
            _imopVarPre172 = step == 1;
        }
        if (_imopVarPre172) {
            printf(" Time step %4d\n", step);
        }
        adi();
    }
#pragma omp parallel
    {
    }
    timer_stop(1);
    tmax = timer_read(1);
    int *_imopVarPre175;
    char *_imopVarPre176;
    _imopVarPre175 = &verified;
    _imopVarPre176 = &class;
    verify(niter, _imopVarPre176, _imopVarPre175);
    if (tmax != 0) {
        double _imopVarPre183;
        double _imopVarPre184;
        _imopVarPre183 = (double) 12;
        _imopVarPre184 = pow(_imopVarPre183, 3.0);
        mflops = (881.174 * _imopVarPre184 - 4683.91 * (((double) 12) * ((double) 12)) + 11484.5 * (double) 12 - 19272.4) * (double) niter / (tmax * 1000000.0);
    } else {
        mflops = 0.0;
    }
    int _imopVarPre188;
    int _imopVarPre189;
    int _imopVarPre190;
    _imopVarPre188 = grid_points[2];
    _imopVarPre189 = grid_points[1];
    _imopVarPre190 = grid_points[0];
    c_print_results("SP", class, _imopVarPre190, _imopVarPre189, _imopVarPre188, niter, nthreads, tmax, mflops, "          floating point", verified, "3.0 structured", "21 Jul 2017", "gcc", "gcc", "(none)", "-I../common", "-O3 -fopenmp", "-O3 -fopenmp", "(none)");
}
static void add(void ) {
    int i;
    int j;
    int k;
    int m;
#pragma omp for nowait
    for (m = 0; m < 5; m++) {
        for (i = 1; i <= grid_points[0] - 2; i++) {
            for (j = 1; j <= grid_points[1] - 2; j++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    u[m][i][j][k] = u[m][i][j][k] + rhs[m][i][j][k];
                }
            }
        }
    }
}
static void adi(void ) {
    compute_rhs();
    txinvr();
    x_solve();
    y_solve();
    z_solve();
    add();
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
    for (i = 0; i <= grid_points[0] - 1; i++) {
        xi = (double) i * dnxm1;
        for (j = 0; j <= grid_points[1] - 1; j++) {
            eta = (double) j * dnym1;
            for (k = 0; k <= grid_points[2] - 1; k++) {
                zeta = (double) k * dnzm1;
                exact_solution(xi, eta, zeta, u_exact);
                for (m = 0; m < 5; m++) {
                    add = u[m][i][j][k] - u_exact[m];
                    rms[m] = rms[m] + add * add;
                }
            }
        }
    }
    for (m = 0; m < 5; m++) {
        for (d = 0; d < 3; d++) {
            rms[m] = rms[m] / (double) (grid_points[d] - 2);
        }
        double _imopVarPre192;
        double _imopVarPre193;
        _imopVarPre192 = rms[m];
        _imopVarPre193 = sqrt(_imopVarPre192);
        rms[m] = _imopVarPre193;
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
    for (i = 0; i <= grid_points[0] - 2; i++) {
        for (j = 0; j <= grid_points[1] - 2; j++) {
            for (k = 0; k <= grid_points[2] - 2; k++) {
                for (m = 0; m < 5; m++) {
                    add = rhs[m][i][j][k];
                    rms[m] = rms[m] + add * add;
                }
            }
        }
    }
    for (m = 0; m < 5; m++) {
        for (d = 0; d < 3; d++) {
            rms[m] = rms[m] / (double) (grid_points[d] - 2);
        }
        double _imopVarPre195;
        double _imopVarPre196;
        _imopVarPre195 = rms[m];
        _imopVarPre196 = sqrt(_imopVarPre195);
        rms[m] = _imopVarPre196;
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
    for (m = 0; m < 5; m++) {
        for (i = 0; i <= grid_points[0] - 1; i++) {
            for (j = 0; j <= grid_points[1] - 1; j++) {
                for (k = 0; k <= grid_points[2] - 1; k++) {
                    forcing[m][i][j][k] = 0.0;
                }
            }
        }
    }
    for (k = 1; k <= grid_points[2] - 2; k++) {
        zeta = (double) k * dnzm1;
        for (j = 1; j <= grid_points[1] - 2; j++) {
            eta = (double) j * dnym1;
            for (i = 0; i <= grid_points[0] - 1; i++) {
                xi = (double) i * dnxm1;
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
                q[i] = 0.5 * (buf[1][i] * ue[1][i] + buf[2][i] * ue[2][i] + buf[3][i] * ue[3][i]);
            }
            for (i = 1; i <= grid_points[0] - 2; i++) {
                im1 = i - 1;
                ip1 = i + 1;
                forcing[0][i][j][k] = forcing[0][i][j][k] - tx2 * (ue[1][ip1] - ue[1][im1]) + dx1tx1 * (ue[0][ip1] - 2.0 * ue[0][i] + ue[0][im1]);
                forcing[1][i][j][k] = forcing[1][i][j][k] - tx2 * ((ue[1][ip1] * buf[1][ip1] + c2 * (ue[4][ip1] - q[ip1])) - (ue[1][im1] * buf[1][im1] + c2 * (ue[4][im1] - q[im1]))) + xxcon1 * (buf[1][ip1] - 2.0 * buf[1][i] + buf[1][im1]) + dx2tx1 * (ue[1][ip1] - 2.0 * ue[1][i] + ue[1][im1]);
                forcing[2][i][j][k] = forcing[2][i][j][k] - tx2 * (ue[2][ip1] * buf[1][ip1] - ue[2][im1] * buf[1][im1]) + xxcon2 * (buf[2][ip1] - 2.0 * buf[2][i] + buf[2][im1]) + dx3tx1 * (ue[2][ip1] - 2.0 * ue[2][i] + ue[2][im1]);
                forcing[3][i][j][k] = forcing[3][i][j][k] - tx2 * (ue[3][ip1] * buf[1][ip1] - ue[3][im1] * buf[1][im1]) + xxcon2 * (buf[3][ip1] - 2.0 * buf[3][i] + buf[3][im1]) + dx4tx1 * (ue[3][ip1] - 2.0 * ue[3][i] + ue[3][im1]);
                forcing[4][i][j][k] = forcing[4][i][j][k] - tx2 * (buf[1][ip1] * (c1 * ue[4][ip1] - c2 * q[ip1]) - buf[1][im1] * (c1 * ue[4][im1] - c2 * q[im1])) + 0.5 * xxcon3 * (buf[0][ip1] - 2.0 * buf[0][i] + buf[0][im1]) + xxcon4 * (cuf[ip1] - 2.0 * cuf[i] + cuf[im1]) + xxcon5 * (buf[4][ip1] - 2.0 * buf[4][i] + buf[4][im1]) + dx5tx1 * (ue[4][ip1] - 2.0 * ue[4][i] + ue[4][im1]);
            }
            for (m = 0; m < 5; m++) {
                i = 1;
                forcing[m][i][j][k] = forcing[m][i][j][k] - dssp * (5.0 * ue[m][i] - 4.0 * ue[m][i + 1] + ue[m][i + 2]);
                i = 2;
                forcing[m][i][j][k] = forcing[m][i][j][k] - dssp * (-4.0 * ue[m][i - 1] + 6.0 * ue[m][i] - 4.0 * ue[m][i + 1] + ue[m][i + 2]);
            }
            for (m = 0; m < 5; m++) {
                for (i = 3; i <= grid_points[0] - 4; i++) {
                    forcing[m][i][j][k] = forcing[m][i][j][k] - dssp * (ue[m][i - 2] - 4.0 * ue[m][i - 1] + 6.0 * ue[m][i] - 4.0 * ue[m][i + 1] + ue[m][i + 2]);
                }
            }
            for (m = 0; m < 5; m++) {
                i = grid_points[0] - 3;
                forcing[m][i][j][k] = forcing[m][i][j][k] - dssp * (ue[m][i - 2] - 4.0 * ue[m][i - 1] + 6.0 * ue[m][i] - 4.0 * ue[m][i + 1]);
                i = grid_points[0] - 2;
                forcing[m][i][j][k] = forcing[m][i][j][k] - dssp * (ue[m][i - 2] - 4.0 * ue[m][i - 1] + 5.0 * ue[m][i]);
            }
        }
    }
    for (k = 1; k <= grid_points[2] - 2; k++) {
        zeta = (double) k * dnzm1;
        for (i = 1; i <= grid_points[0] - 2; i++) {
            xi = (double) i * dnxm1;
            for (j = 0; j <= grid_points[1] - 1; j++) {
                eta = (double) j * dnym1;
                exact_solution(xi, eta, zeta, dtemp);
                for (m = 0; m < 5; m++) {
                    ue[m][j] = dtemp[m];
                }
                dtpp = 1.0 / dtemp[0];
                for (m = 1; m < 5; m++) {
                    buf[m][j] = dtpp * dtemp[m];
                }
                cuf[j] = buf[2][j] * buf[2][j];
                buf[0][j] = cuf[j] + buf[1][j] * buf[1][j] + buf[3][j] * buf[3][j];
                q[j] = 0.5 * (buf[1][j] * ue[1][j] + buf[2][j] * ue[2][j] + buf[3][j] * ue[3][j]);
            }
            for (j = 1; j <= grid_points[1] - 2; j++) {
                jm1 = j - 1;
                jp1 = j + 1;
                forcing[0][i][j][k] = forcing[0][i][j][k] - ty2 * (ue[2][jp1] - ue[2][jm1]) + dy1ty1 * (ue[0][jp1] - 2.0 * ue[0][j] + ue[0][jm1]);
                forcing[1][i][j][k] = forcing[1][i][j][k] - ty2 * (ue[1][jp1] * buf[2][jp1] - ue[1][jm1] * buf[2][jm1]) + yycon2 * (buf[1][jp1] - 2.0 * buf[1][j] + buf[1][jm1]) + dy2ty1 * (ue[1][jp1] - 2.0 * ue[1][j] + ue[1][jm1]);
                forcing[2][i][j][k] = forcing[2][i][j][k] - ty2 * ((ue[2][jp1] * buf[2][jp1] + c2 * (ue[4][jp1] - q[jp1])) - (ue[2][jm1] * buf[2][jm1] + c2 * (ue[4][jm1] - q[jm1]))) + yycon1 * (buf[2][jp1] - 2.0 * buf[2][j] + buf[2][jm1]) + dy3ty1 * (ue[2][jp1] - 2.0 * ue[2][j] + ue[2][jm1]);
                forcing[3][i][j][k] = forcing[3][i][j][k] - ty2 * (ue[3][jp1] * buf[2][jp1] - ue[3][jm1] * buf[2][jm1]) + yycon2 * (buf[3][jp1] - 2.0 * buf[3][j] + buf[3][jm1]) + dy4ty1 * (ue[3][jp1] - 2.0 * ue[3][j] + ue[3][jm1]);
                forcing[4][i][j][k] = forcing[4][i][j][k] - ty2 * (buf[2][jp1] * (c1 * ue[4][jp1] - c2 * q[jp1]) - buf[2][jm1] * (c1 * ue[4][jm1] - c2 * q[jm1])) + 0.5 * yycon3 * (buf[0][jp1] - 2.0 * buf[0][j] + buf[0][jm1]) + yycon4 * (cuf[jp1] - 2.0 * cuf[j] + cuf[jm1]) + yycon5 * (buf[4][jp1] - 2.0 * buf[4][j] + buf[4][jm1]) + dy5ty1 * (ue[4][jp1] - 2.0 * ue[4][j] + ue[4][jm1]);
            }
            for (m = 0; m < 5; m++) {
                j = 1;
                forcing[m][i][j][k] = forcing[m][i][j][k] - dssp * (5.0 * ue[m][j] - 4.0 * ue[m][j + 1] + ue[m][j + 2]);
                j = 2;
                forcing[m][i][j][k] = forcing[m][i][j][k] - dssp * (-4.0 * ue[m][j - 1] + 6.0 * ue[m][j] - 4.0 * ue[m][j + 1] + ue[m][j + 2]);
            }
            for (m = 0; m < 5; m++) {
                for (j = 3; j <= grid_points[1] - 4; j++) {
                    forcing[m][i][j][k] = forcing[m][i][j][k] - dssp * (ue[m][j - 2] - 4.0 * ue[m][j - 1] + 6.0 * ue[m][j] - 4.0 * ue[m][j + 1] + ue[m][j + 2]);
                }
            }
            for (m = 0; m < 5; m++) {
                j = grid_points[1] - 3;
                forcing[m][i][j][k] = forcing[m][i][j][k] - dssp * (ue[m][j - 2] - 4.0 * ue[m][j - 1] + 6.0 * ue[m][j] - 4.0 * ue[m][j + 1]);
                j = grid_points[1] - 2;
                forcing[m][i][j][k] = forcing[m][i][j][k] - dssp * (ue[m][j - 2] - 4.0 * ue[m][j - 1] + 5.0 * ue[m][j]);
            }
        }
    }
    for (j = 1; j <= grid_points[1] - 2; j++) {
        eta = (double) j * dnym1;
        for (i = 1; i <= grid_points[0] - 2; i++) {
            xi = (double) i * dnxm1;
            for (k = 0; k <= grid_points[2] - 1; k++) {
                zeta = (double) k * dnzm1;
                exact_solution(xi, eta, zeta, dtemp);
                for (m = 0; m < 5; m++) {
                    ue[m][k] = dtemp[m];
                }
                dtpp = 1.0 / dtemp[0];
                for (m = 1; m < 5; m++) {
                    buf[m][k] = dtpp * dtemp[m];
                }
                cuf[k] = buf[3][k] * buf[3][k];
                buf[0][k] = cuf[k] + buf[1][k] * buf[1][k] + buf[2][k] * buf[2][k];
                q[k] = 0.5 * (buf[1][k] * ue[1][k] + buf[2][k] * ue[2][k] + buf[3][k] * ue[3][k]);
            }
            for (k = 1; k <= grid_points[2] - 2; k++) {
                km1 = k - 1;
                kp1 = k + 1;
                forcing[0][i][j][k] = forcing[0][i][j][k] - tz2 * (ue[3][kp1] - ue[3][km1]) + dz1tz1 * (ue[0][kp1] - 2.0 * ue[0][k] + ue[0][km1]);
                forcing[1][i][j][k] = forcing[1][i][j][k] - tz2 * (ue[1][kp1] * buf[3][kp1] - ue[1][km1] * buf[3][km1]) + zzcon2 * (buf[1][kp1] - 2.0 * buf[1][k] + buf[1][km1]) + dz2tz1 * (ue[1][kp1] - 2.0 * ue[1][k] + ue[1][km1]);
                forcing[2][i][j][k] = forcing[2][i][j][k] - tz2 * (ue[2][kp1] * buf[3][kp1] - ue[2][km1] * buf[3][km1]) + zzcon2 * (buf[2][kp1] - 2.0 * buf[2][k] + buf[2][km1]) + dz3tz1 * (ue[2][kp1] - 2.0 * ue[2][k] + ue[2][km1]);
                forcing[3][i][j][k] = forcing[3][i][j][k] - tz2 * ((ue[3][kp1] * buf[3][kp1] + c2 * (ue[4][kp1] - q[kp1])) - (ue[3][km1] * buf[3][km1] + c2 * (ue[4][km1] - q[km1]))) + zzcon1 * (buf[3][kp1] - 2.0 * buf[3][k] + buf[3][km1]) + dz4tz1 * (ue[3][kp1] - 2.0 * ue[3][k] + ue[3][km1]);
                forcing[4][i][j][k] = forcing[4][i][j][k] - tz2 * (buf[3][kp1] * (c1 * ue[4][kp1] - c2 * q[kp1]) - buf[3][km1] * (c1 * ue[4][km1] - c2 * q[km1])) + 0.5 * zzcon3 * (buf[0][kp1] - 2.0 * buf[0][k] + buf[0][km1]) + zzcon4 * (cuf[kp1] - 2.0 * cuf[k] + cuf[km1]) + zzcon5 * (buf[4][kp1] - 2.0 * buf[4][k] + buf[4][km1]) + dz5tz1 * (ue[4][kp1] - 2.0 * ue[4][k] + ue[4][km1]);
            }
            for (m = 0; m < 5; m++) {
                k = 1;
                forcing[m][i][j][k] = forcing[m][i][j][k] - dssp * (5.0 * ue[m][k] - 4.0 * ue[m][k + 1] + ue[m][k + 2]);
                k = 2;
                forcing[m][i][j][k] = forcing[m][i][j][k] - dssp * (-4.0 * ue[m][k - 1] + 6.0 * ue[m][k] - 4.0 * ue[m][k + 1] + ue[m][k + 2]);
            }
            for (m = 0; m < 5; m++) {
                for (k = 3; k <= grid_points[2] - 4; k++) {
                    forcing[m][i][j][k] = forcing[m][i][j][k] - dssp * (ue[m][k - 2] - 4.0 * ue[m][k - 1] + 6.0 * ue[m][k] - 4.0 * ue[m][k + 1] + ue[m][k + 2]);
                }
            }
            for (m = 0; m < 5; m++) {
                k = grid_points[2] - 3;
                forcing[m][i][j][k] = forcing[m][i][j][k] - dssp * (ue[m][k - 2] - 4.0 * ue[m][k - 1] + 6.0 * ue[m][k] - 4.0 * ue[m][k + 1]);
                k = grid_points[2] - 2;
                forcing[m][i][j][k] = forcing[m][i][j][k] - dssp * (ue[m][k - 2] - 4.0 * ue[m][k - 1] + 5.0 * ue[m][k]);
            }
        }
    }
    for (m = 0; m < 5; m++) {
        for (i = 1; i <= grid_points[0] - 2; i++) {
            for (j = 1; j <= grid_points[1] - 2; j++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    forcing[m][i][j][k] = -1.0 * forcing[m][i][j][k];
                }
            }
        }
    }
}
static void exact_solution(double xi, double eta , double zeta , double dtemp[5]) {
    int m;
    for (m = 0; m < 5; m++) {
        dtemp[m] = ce[0][m] + xi * (ce[1][m] + xi * (ce[4][m] + xi * (ce[7][m] + xi * ce[10][m]))) + eta * (ce[2][m] + eta * (ce[5][m] + eta * (ce[8][m] + eta * ce[11][m]))) + zeta * (ce[3][m] + zeta * (ce[6][m] + zeta * (ce[9][m] + zeta * ce[12][m])));
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
    for (i = 0; i <= 12 - 1; i++) {
        for (j = 0; j <= 12 - 1; j++) {
            for (k = 0; k <= 12 - 1; k++) {
                u[0][i][j][k] = 1.0;
                u[1][i][j][k] = 0.0;
                u[2][i][j][k] = 0.0;
                u[3][i][j][k] = 0.0;
                u[4][i][j][k] = 1.0;
            }
        }
    }
    for (i = 0; i <= grid_points[0] - 1; i++) {
        xi = (double) i * dnxm1;
        for (j = 0; j <= grid_points[1] - 1; j++) {
            eta = (double) j * dnym1;
            for (k = 0; k <= grid_points[2] - 1; k++) {
                zeta = (double) k * dnzm1;
                for (ix = 0; ix < 2; ix++) {
                    double *_imopVarPre199;
                    double _imopVarPre200;
                    _imopVarPre199 = &Pface[ix][0][0];
                    _imopVarPre200 = (double) ix;
                    exact_solution(_imopVarPre200, eta, zeta, _imopVarPre199);
                }
                for (iy = 0; iy < 2; iy++) {
                    double *_imopVarPre203;
                    double _imopVarPre204;
                    _imopVarPre203 = &Pface[iy][1][0];
                    _imopVarPre204 = (double) iy;
                    exact_solution(xi, _imopVarPre204, zeta, _imopVarPre203);
                }
                for (iz = 0; iz < 2; iz++) {
                    double *_imopVarPre207;
                    double _imopVarPre208;
                    _imopVarPre207 = &Pface[iz][2][0];
                    _imopVarPre208 = (double) iz;
                    exact_solution(xi, eta, _imopVarPre208, _imopVarPre207);
                }
                for (m = 0; m < 5; m++) {
                    Pxi = xi * Pface[1][0][m] + (1.0 - xi) * Pface[0][0][m];
                    Peta = eta * Pface[1][1][m] + (1.0 - eta) * Pface[0][1][m];
                    Pzeta = zeta * Pface[1][2][m] + (1.0 - zeta) * Pface[0][2][m];
                    u[m][i][j][k] = Pxi + Peta + Pzeta - Pxi * Peta - Pxi * Pzeta - Peta * Pzeta + Pxi * Peta * Pzeta;
                }
            }
        }
    }
    xi = 0.0;
    i = 0;
    for (j = 0; j < grid_points[1]; j++) {
        eta = (double) j * dnym1;
        for (k = 0; k < grid_points[2]; k++) {
            zeta = (double) k * dnzm1;
            exact_solution(xi, eta, zeta, temp);
            for (m = 0; m < 5; m++) {
                u[m][i][j][k] = temp[m];
            }
        }
    }
    xi = 1.0;
    i = grid_points[0] - 1;
    for (j = 0; j < grid_points[1]; j++) {
        eta = (double) j * dnym1;
        for (k = 0; k < grid_points[2]; k++) {
            zeta = (double) k * dnzm1;
            exact_solution(xi, eta, zeta, temp);
            for (m = 0; m < 5; m++) {
                u[m][i][j][k] = temp[m];
            }
        }
    }
    eta = 0.0;
    j = 0;
    for (i = 0; i < grid_points[0]; i++) {
        xi = (double) i * dnxm1;
        for (k = 0; k < grid_points[2]; k++) {
            zeta = (double) k * dnzm1;
            exact_solution(xi, eta, zeta, temp);
            for (m = 0; m < 5; m++) {
                u[m][i][j][k] = temp[m];
            }
        }
    }
    eta = 1.0;
    j = grid_points[1] - 1;
    for (i = 0; i < grid_points[0]; i++) {
        xi = (double) i * dnxm1;
        for (k = 0; k < grid_points[2]; k++) {
            zeta = (double) k * dnzm1;
            exact_solution(xi, eta, zeta, temp);
            for (m = 0; m < 5; m++) {
                u[m][i][j][k] = temp[m];
            }
        }
    }
    zeta = 0.0;
    k = 0;
    for (i = 0; i < grid_points[0]; i++) {
        xi = (double) i * dnxm1;
        for (j = 0; j < grid_points[1]; j++) {
            eta = (double) j * dnym1;
            exact_solution(xi, eta, zeta, temp);
            for (m = 0; m < 5; m++) {
                u[m][i][j][k] = temp[m];
            }
        }
    }
    zeta = 1.0;
    k = grid_points[2] - 1;
    for (i = 0; i < grid_points[0]; i++) {
        xi = (double) i * dnxm1;
        for (j = 0; j < grid_points[1]; j++) {
            eta = (double) j * dnym1;
            exact_solution(xi, eta, zeta, temp);
            for (m = 0; m < 5; m++) {
                u[m][i][j][k] = temp[m];
            }
        }
    }
}
static void lhsinit(void ) {
    int i;
    int j;
    int k;
    int n;
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
    for (n = 0; n < 3; n++) {
#pragma omp for nowait
        for (i = 0; i < grid_points[0]; i++) {
            for (j = 0; j < grid_points[1]; j++) {
                for (k = 0; k < grid_points[2]; k++) {
                    lhs[5 * n + 2][i][j][k] = 1.0;
                }
            }
        }
    }
}
static void lhsx(void ) {
    double ru1;
    int i;
    int j;
    int k;
    for (j = 1; j <= grid_points[1] - 2; j++) {
        for (k = 1; k <= grid_points[2] - 2; k++) {
#pragma omp for nowait
            for (i = 0; i <= grid_points[0] - 1; i++) {
                ru1 = c3c4 * rho_i[i][j][k];
                cv[i] = us[i][j][k];
                int _imopVarPre719;
                double _imopVarPre720;
                int _imopVarPre721;
                double _imopVarPre722;
                int _imopVarPre729;
                double _imopVarPre730;
                int _imopVarPre731;
                double _imopVarPre732;
                int _imopVarPre825;
                double _imopVarPre826;
                int _imopVarPre827;
                double _imopVarPre828;
                int _imopVarPre835;
                double _imopVarPre836;
                _imopVarPre719 = ((dxmax + ru1) > dx1);
                if (_imopVarPre719) {
                    _imopVarPre720 = (dxmax + ru1);
                } else {
                    _imopVarPre720 = dx1;
                }
                _imopVarPre721 = ((dx5 + c1c5 * ru1) > _imopVarPre720);
                if (_imopVarPre721) {
                    _imopVarPre722 = (dx5 + c1c5 * ru1);
                } else {
                    _imopVarPre729 = ((dxmax + ru1) > dx1);
                    if (_imopVarPre729) {
                        _imopVarPre730 = (dxmax + ru1);
                    } else {
                        _imopVarPre730 = dx1;
                    }
                    _imopVarPre722 = _imopVarPre730;
                }
                _imopVarPre731 = ((dx2 + con43 * ru1) > _imopVarPre722);
                if (_imopVarPre731) {
                    _imopVarPre732 = (dx2 + con43 * ru1);
                } else {
                    _imopVarPre825 = ((dxmax + ru1) > dx1);
                    if (_imopVarPre825) {
                        _imopVarPre826 = (dxmax + ru1);
                    } else {
                        _imopVarPre826 = dx1;
                    }
                    _imopVarPre827 = ((dx5 + c1c5 * ru1) > _imopVarPre826);
                    if (_imopVarPre827) {
                        _imopVarPre828 = (dx5 + c1c5 * ru1);
                    } else {
                        _imopVarPre835 = ((dxmax + ru1) > dx1);
                        if (_imopVarPre835) {
                            _imopVarPre836 = (dxmax + ru1);
                        } else {
                            _imopVarPre836 = dx1;
                        }
                        _imopVarPre828 = _imopVarPre836;
                    }
                    _imopVarPre732 = _imopVarPre828;
                }
                rhon[i] = _imopVarPre732;
            }
// #pragma omp dummyFlush BARRIER_START written([rhs.f, lhs.f, rhon.f, cv.f]) read([cv, i, dttx2, rhon, lhs.f, dttx1, rhon.f, grid_points.f, c2dttx1, grid_points, lhs, cv.f])
#pragma omp barrier
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                lhs[0][i][j][k] = 0.0;
                lhs[1][i][j][k] = -dttx2 * cv[i - 1] - dttx1 * rhon[i - 1];
                lhs[2][i][j][k] = 1.0 + c2dttx1 * rhon[i];
                lhs[3][i][j][k] = dttx2 * cv[i + 1] - dttx1 * rhon[i + 1];
                lhs[4][i][j][k] = 0.0;
            }
// #pragma omp dummyFlush BARRIER_START written([lhs.f]) read([_imopVarPre731, j, _imopVarPre721, comz1, rho_i, rhon, _imopVarPre719, lhs.f, comz5, _imopVarPre729, grid_points.f, dxmax, us, dx1, c1c5, dx5, cv, i, _imopVarPre825, comz4, _imopVarPre835, comz6, _imopVarPre827, con43, grid_points, lhs, us.f, c3c4, rho_i.f, dx2])
#pragma omp barrier
        }
    }
    i = 1;
#pragma omp for nowait
    for (j = 1; j <= grid_points[1] - 2; j++) {
        for (k = 1; k <= grid_points[2] - 2; k++) {
            lhs[2][i][j][k] = lhs[2][i][j][k] + comz5;
            lhs[3][i][j][k] = lhs[3][i][j][k] - comz4;
            lhs[4][i][j][k] = lhs[4][i][j][k] + comz1;
            lhs[1][i + 1][j][k] = lhs[1][i + 1][j][k] - comz4;
            lhs[2][i + 1][j][k] = lhs[2][i + 1][j][k] + comz6;
            lhs[3][i + 1][j][k] = lhs[3][i + 1][j][k] - comz4;
            lhs[4][i + 1][j][k] = lhs[4][i + 1][j][k] + comz1;
        }
    }
#pragma omp for nowait
    for (i = 3; i <= grid_points[0] - 4; i++) {
        for (j = 1; j <= grid_points[1] - 2; j++) {
            for (k = 1; k <= grid_points[2] - 2; k++) {
                lhs[0][i][j][k] = lhs[0][i][j][k] + comz1;
                lhs[1][i][j][k] = lhs[1][i][j][k] - comz4;
                lhs[2][i][j][k] = lhs[2][i][j][k] + comz6;
                lhs[3][i][j][k] = lhs[3][i][j][k] - comz4;
                lhs[4][i][j][k] = lhs[4][i][j][k] + comz1;
            }
        }
    }
    i = grid_points[0] - 3;
#pragma omp for nowait
    for (j = 1; j <= grid_points[1] - 2; j++) {
        for (k = 1; k <= grid_points[2] - 2; k++) {
            lhs[0][i][j][k] = lhs[0][i][j][k] + comz1;
            lhs[1][i][j][k] = lhs[1][i][j][k] - comz4;
            lhs[2][i][j][k] = lhs[2][i][j][k] + comz6;
            lhs[3][i][j][k] = lhs[3][i][j][k] - comz4;
            lhs[0][i + 1][j][k] = lhs[0][i + 1][j][k] + comz1;
            lhs[1][i + 1][j][k] = lhs[1][i + 1][j][k] - comz4;
            lhs[2][i + 1][j][k] = lhs[2][i + 1][j][k] + comz5;
        }
    }
// #pragma omp dummyFlush BARRIER_START written([rhs.f, lhs.f, rhon.f, cv.f]) read([i, dttx2, lhs.f, speed, grid_points.f, grid_points, speed.f, lhs])
#pragma omp barrier
#pragma omp for nowait
    for (i = 1; i <= grid_points[0] - 2; i++) {
        for (j = 1; j <= grid_points[1] - 2; j++) {
            for (k = 1; k <= grid_points[2] - 2; k++) {
                lhs[0 + 5][i][j][k] = lhs[0][i][j][k];
                lhs[1 + 5][i][j][k] = lhs[1][i][j][k] - dttx2 * speed[i - 1][j][k];
                lhs[2 + 5][i][j][k] = lhs[2][i][j][k];
                lhs[3 + 5][i][j][k] = lhs[3][i][j][k] + dttx2 * speed[i + 1][j][k];
                lhs[4 + 5][i][j][k] = lhs[4][i][j][k];
                lhs[0 + 10][i][j][k] = lhs[0][i][j][k];
                lhs[1 + 10][i][j][k] = lhs[1][i][j][k] + dttx2 * speed[i - 1][j][k];
                lhs[2 + 10][i][j][k] = lhs[2][i][j][k];
                lhs[3 + 10][i][j][k] = lhs[3][i][j][k] - dttx2 * speed[i + 1][j][k];
                lhs[4 + 10][i][j][k] = lhs[4][i][j][k];
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written([lhs.f]) read([rhs.f, rhs, lhs.f, j, grid_points.f, grid_points, lhs])
#pragma omp barrier
}
static void lhsy(void ) {
    double ru1;
    int i;
    int j;
    int k;
    for (i = 1; i <= grid_points[0] - 2; i++) {
        for (k = 1; k <= grid_points[2] - 2; k++) {
#pragma omp for nowait
            for (j = 0; j <= grid_points[1] - 1; j++) {
                ru1 = c3c4 * rho_i[i][j][k];
                cv[j] = vs[i][j][k];
                int _imopVarPre1347;
                double _imopVarPre1348;
                int _imopVarPre1349;
                double _imopVarPre1350;
                int _imopVarPre1357;
                double _imopVarPre1358;
                int _imopVarPre1359;
                double _imopVarPre1360;
                int _imopVarPre1453;
                double _imopVarPre1454;
                int _imopVarPre1455;
                double _imopVarPre1456;
                int _imopVarPre1463;
                double _imopVarPre1464;
                _imopVarPre1347 = ((dymax + ru1) > dy1);
                if (_imopVarPre1347) {
                    _imopVarPre1348 = (dymax + ru1);
                } else {
                    _imopVarPre1348 = dy1;
                }
                _imopVarPre1349 = ((dy5 + c1c5 * ru1) > _imopVarPre1348);
                if (_imopVarPre1349) {
                    _imopVarPre1350 = (dy5 + c1c5 * ru1);
                } else {
                    _imopVarPre1357 = ((dymax + ru1) > dy1);
                    if (_imopVarPre1357) {
                        _imopVarPre1358 = (dymax + ru1);
                    } else {
                        _imopVarPre1358 = dy1;
                    }
                    _imopVarPre1350 = _imopVarPre1358;
                }
                _imopVarPre1359 = ((dy3 + con43 * ru1) > _imopVarPre1350);
                if (_imopVarPre1359) {
                    _imopVarPre1360 = (dy3 + con43 * ru1);
                } else {
                    _imopVarPre1453 = ((dymax + ru1) > dy1);
                    if (_imopVarPre1453) {
                        _imopVarPre1454 = (dymax + ru1);
                    } else {
                        _imopVarPre1454 = dy1;
                    }
                    _imopVarPre1455 = ((dy5 + c1c5 * ru1) > _imopVarPre1454);
                    if (_imopVarPre1455) {
                        _imopVarPre1456 = (dy5 + c1c5 * ru1);
                    } else {
                        _imopVarPre1463 = ((dymax + ru1) > dy1);
                        if (_imopVarPre1463) {
                            _imopVarPre1464 = (dymax + ru1);
                        } else {
                            _imopVarPre1464 = dy1;
                        }
                        _imopVarPre1456 = _imopVarPre1464;
                    }
                    _imopVarPre1360 = _imopVarPre1456;
                }
                rhoq[j] = _imopVarPre1360;
            }
// #pragma omp dummyFlush BARRIER_START written([rhs.f, lhs.f, rhoq.f, cv.f]) read([cv, j, lhs.f, dtty1, dtty2, rhoq.f, grid_points.f, rhoq, grid_points, lhs, cv.f, c2dtty1])
#pragma omp barrier
#pragma omp for nowait
            for (j = 1; j <= grid_points[1] - 2; j++) {
                lhs[0][i][j][k] = 0.0;
                lhs[1][i][j][k] = -dtty2 * cv[j - 1] - dtty1 * rhoq[j - 1];
                lhs[2][i][j][k] = 1.0 + c2dtty1 * rhoq[j];
                lhs[3][i][j][k] = dtty2 * cv[j + 1] - dtty1 * rhoq[j + 1];
                lhs[4][i][j][k] = 0.0;
            }
// #pragma omp dummyFlush BARRIER_START written([lhs.f]) read([comz1, rho_i, lhs.f, comz5, grid_points.f, vs, j, c1c5, dy1, dy3, _imopVarPre1349, dy5, _imopVarPre1359, _imopVarPre1347, _imopVarPre1357, cv, dymax, comz4, comz6, con43, grid_points, rhoq, lhs, vs.f, _imopVarPre1455, i, c3c4, _imopVarPre1453, rho_i.f, _imopVarPre1463])
#pragma omp barrier
        }
    }
    j = 1;
#pragma omp for nowait
    for (i = 1; i <= grid_points[0] - 2; i++) {
        for (k = 1; k <= grid_points[2] - 2; k++) {
            lhs[2][i][j][k] = lhs[2][i][j][k] + comz5;
            lhs[3][i][j][k] = lhs[3][i][j][k] - comz4;
            lhs[4][i][j][k] = lhs[4][i][j][k] + comz1;
            lhs[1][i][j + 1][k] = lhs[1][i][j + 1][k] - comz4;
            lhs[2][i][j + 1][k] = lhs[2][i][j + 1][k] + comz6;
            lhs[3][i][j + 1][k] = lhs[3][i][j + 1][k] - comz4;
            lhs[4][i][j + 1][k] = lhs[4][i][j + 1][k] + comz1;
        }
    }
#pragma omp for nowait
    for (i = 1; i <= grid_points[0] - 2; i++) {
        for (j = 3; j <= grid_points[1] - 4; j++) {
            for (k = 1; k <= grid_points[2] - 2; k++) {
                lhs[0][i][j][k] = lhs[0][i][j][k] + comz1;
                lhs[1][i][j][k] = lhs[1][i][j][k] - comz4;
                lhs[2][i][j][k] = lhs[2][i][j][k] + comz6;
                lhs[3][i][j][k] = lhs[3][i][j][k] - comz4;
                lhs[4][i][j][k] = lhs[4][i][j][k] + comz1;
            }
        }
    }
    j = grid_points[1] - 3;
#pragma omp for nowait
    for (i = 1; i <= grid_points[0] - 2; i++) {
        for (k = 1; k <= grid_points[2] - 2; k++) {
            lhs[0][i][j][k] = lhs[0][i][j][k] + comz1;
            lhs[1][i][j][k] = lhs[1][i][j][k] - comz4;
            lhs[2][i][j][k] = lhs[2][i][j][k] + comz6;
            lhs[3][i][j][k] = lhs[3][i][j][k] - comz4;
            lhs[0][i][j + 1][k] = lhs[0][i][j + 1][k] + comz1;
            lhs[1][i][j + 1][k] = lhs[1][i][j + 1][k] - comz4;
            lhs[2][i][j + 1][k] = lhs[2][i][j + 1][k] + comz5;
        }
    }
// #pragma omp dummyFlush BARRIER_START written([rhs.f, lhs.f, rhoq.f, cv.f]) read([lhs.f, i, speed, dtty2, grid_points.f, grid_points, speed.f, lhs])
#pragma omp barrier
#pragma omp for nowait
    for (i = 1; i <= grid_points[0] - 2; i++) {
        for (j = 1; j <= grid_points[1] - 2; j++) {
            for (k = 1; k <= grid_points[2] - 2; k++) {
                lhs[0 + 5][i][j][k] = lhs[0][i][j][k];
                lhs[1 + 5][i][j][k] = lhs[1][i][j][k] - dtty2 * speed[i][j - 1][k];
                lhs[2 + 5][i][j][k] = lhs[2][i][j][k];
                lhs[3 + 5][i][j][k] = lhs[3][i][j][k] + dtty2 * speed[i][j + 1][k];
                lhs[4 + 5][i][j][k] = lhs[4][i][j][k];
                lhs[0 + 10][i][j][k] = lhs[0][i][j][k];
                lhs[1 + 10][i][j][k] = lhs[1][i][j][k] + dtty2 * speed[i][j - 1][k];
                lhs[2 + 10][i][j][k] = lhs[2][i][j][k];
                lhs[3 + 10][i][j][k] = lhs[3][i][j][k] - dtty2 * speed[i][j + 1][k];
                lhs[4 + 10][i][j][k] = lhs[4][i][j][k];
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written([lhs.f]) read([rhs.f, i, rhs, lhs.f, grid_points.f, grid_points, lhs])
#pragma omp barrier
}
static void lhsz(void ) {
    double ru1;
    int i;
    int j;
    int k;
    for (i = 1; i <= grid_points[0] - 2; i++) {
        for (j = 1; j <= grid_points[1] - 2; j++) {
#pragma omp for nowait
            for (k = 0; k <= grid_points[2] - 1; k++) {
                ru1 = c3c4 * rho_i[i][j][k];
                cv[k] = ws[i][j][k];
                int _imopVarPre1975;
                double _imopVarPre1976;
                int _imopVarPre1977;
                double _imopVarPre1978;
                int _imopVarPre1985;
                double _imopVarPre1986;
                int _imopVarPre1987;
                double _imopVarPre1988;
                int _imopVarPre2081;
                double _imopVarPre2082;
                int _imopVarPre2083;
                double _imopVarPre2084;
                int _imopVarPre2091;
                double _imopVarPre2092;
                _imopVarPre1975 = ((dzmax + ru1) > dz1);
                if (_imopVarPre1975) {
                    _imopVarPre1976 = (dzmax + ru1);
                } else {
                    _imopVarPre1976 = dz1;
                }
                _imopVarPre1977 = ((dz5 + c1c5 * ru1) > _imopVarPre1976);
                if (_imopVarPre1977) {
                    _imopVarPre1978 = (dz5 + c1c5 * ru1);
                } else {
                    _imopVarPre1985 = ((dzmax + ru1) > dz1);
                    if (_imopVarPre1985) {
                        _imopVarPre1986 = (dzmax + ru1);
                    } else {
                        _imopVarPre1986 = dz1;
                    }
                    _imopVarPre1978 = _imopVarPre1986;
                }
                _imopVarPre1987 = ((dz4 + con43 * ru1) > _imopVarPre1978);
                if (_imopVarPre1987) {
                    _imopVarPre1988 = (dz4 + con43 * ru1);
                } else {
                    _imopVarPre2081 = ((dzmax + ru1) > dz1);
                    if (_imopVarPre2081) {
                        _imopVarPre2082 = (dzmax + ru1);
                    } else {
                        _imopVarPre2082 = dz1;
                    }
                    _imopVarPre2083 = ((dz5 + c1c5 * ru1) > _imopVarPre2082);
                    if (_imopVarPre2083) {
                        _imopVarPre2084 = (dz5 + c1c5 * ru1);
                    } else {
                        _imopVarPre2091 = ((dzmax + ru1) > dz1);
                        if (_imopVarPre2091) {
                            _imopVarPre2092 = (dzmax + ru1);
                        } else {
                            _imopVarPre2092 = dz1;
                        }
                        _imopVarPre2084 = _imopVarPre2092;
                    }
                    _imopVarPre1988 = _imopVarPre2084;
                }
                rhos[k] = _imopVarPre1988;
            }
// #pragma omp dummyFlush BARRIER_START written([rhs.f, lhs.f, rhos.f, cv.f]) read([cv, k, dttz1, lhs.f, dttz2, grid_points.f, grid_points, lhs, rhos.f, c2dttz1, cv.f, rhos])
#pragma omp barrier
#pragma omp for nowait
            for (k = 1; k <= grid_points[2] - 2; k++) {
                lhs[0][i][j][k] = 0.0;
                lhs[1][i][j][k] = -dttz2 * cv[k - 1] - dttz1 * rhos[k - 1];
                lhs[2][i][j][k] = 1.0 + c2dttz1 * rhos[k];
                lhs[3][i][j][k] = dttz2 * cv[k + 1] - dttz1 * rhos[k + 1];
                lhs[4][i][j][k] = 0.0;
            }
// #pragma omp dummyFlush BARRIER_START written([lhs.f]) read([k, rho_i, comz1, i, lhs.f, comz5, grid_points.f, ws, dz1, c1c5, dz5, cv, comz4, comz6, dzmax, con43, grid_points, lhs, rhos, _imopVarPre1977, _imopVarPre1987, _imopVarPre1975, _imopVarPre1985, ws.f, c3c4, _imopVarPre2081, rho_i.f, _imopVarPre2083, dz4, _imopVarPre2091])
#pragma omp barrier
        }
    }
    k = 1;
#pragma omp for nowait
    for (i = 1; i <= grid_points[0] - 2; i++) {
        for (j = 1; j <= grid_points[1] - 2; j++) {
            lhs[2][i][j][k] = lhs[2][i][j][k] + comz5;
            lhs[3][i][j][k] = lhs[3][i][j][k] - comz4;
            lhs[4][i][j][k] = lhs[4][i][j][k] + comz1;
            lhs[1][i][j][k + 1] = lhs[1][i][j][k + 1] - comz4;
            lhs[2][i][j][k + 1] = lhs[2][i][j][k + 1] + comz6;
            lhs[3][i][j][k + 1] = lhs[3][i][j][k + 1] - comz4;
            lhs[4][i][j][k + 1] = lhs[4][i][j][k + 1] + comz1;
        }
    }
#pragma omp for nowait
    for (i = 1; i <= grid_points[0] - 2; i++) {
        for (j = 1; j <= grid_points[1] - 2; j++) {
            for (k = 3; k <= grid_points[2] - 4; k++) {
                lhs[0][i][j][k] = lhs[0][i][j][k] + comz1;
                lhs[1][i][j][k] = lhs[1][i][j][k] - comz4;
                lhs[2][i][j][k] = lhs[2][i][j][k] + comz6;
                lhs[3][i][j][k] = lhs[3][i][j][k] - comz4;
                lhs[4][i][j][k] = lhs[4][i][j][k] + comz1;
            }
        }
    }
    k = grid_points[2] - 3;
#pragma omp for nowait
    for (i = 1; i <= grid_points[0] - 2; i++) {
        for (j = 1; j <= grid_points[1] - 2; j++) {
            lhs[0][i][j][k] = lhs[0][i][j][k] + comz1;
            lhs[1][i][j][k] = lhs[1][i][j][k] - comz4;
            lhs[2][i][j][k] = lhs[2][i][j][k] + comz6;
            lhs[3][i][j][k] = lhs[3][i][j][k] - comz4;
            lhs[0][i][j][k + 1] = lhs[0][i][j][k + 1] + comz1;
            lhs[1][i][j][k + 1] = lhs[1][i][j][k + 1] - comz4;
            lhs[2][i][j][k + 1] = lhs[2][i][j][k + 1] + comz5;
        }
    }
// #pragma omp dummyFlush BARRIER_START written([rhs.f, lhs.f, rhos.f, cv.f]) read([i, lhs.f, dttz2, speed, grid_points.f, grid_points, speed.f, lhs])
#pragma omp barrier
#pragma omp for nowait
    for (i = 1; i <= grid_points[0] - 2; i++) {
        for (j = 1; j <= grid_points[1] - 2; j++) {
            for (k = 1; k <= grid_points[2] - 2; k++) {
                lhs[0 + 5][i][j][k] = lhs[0][i][j][k];
                lhs[1 + 5][i][j][k] = lhs[1][i][j][k] - dttz2 * speed[i][j][k - 1];
                lhs[2 + 5][i][j][k] = lhs[2][i][j][k];
                lhs[3 + 5][i][j][k] = lhs[3][i][j][k] + dttz2 * speed[i][j][k + 1];
                lhs[4 + 5][i][j][k] = lhs[4][i][j][k];
                lhs[0 + 10][i][j][k] = lhs[0][i][j][k];
                lhs[1 + 10][i][j][k] = lhs[1][i][j][k] + dttz2 * speed[i][j][k - 1];
                lhs[2 + 10][i][j][k] = lhs[2][i][j][k];
                lhs[3 + 10][i][j][k] = lhs[3][i][j][k] - dttz2 * speed[i][j][k + 1];
                lhs[4 + 10][i][j][k] = lhs[4][i][j][k];
            }
        }
    }
// #pragma omp dummyFlush BARRIER_START written([lhs.f]) read([rhs.f, rhs, i, lhs.f, grid_points.f, grid_points, lhs])
#pragma omp barrier
}
static void ninvr(void ) {
    int i;
    int j;
    int k;
    double r1;
    double r2;
    double r3;
    double r4;
    double r5;
    double t1;
    double t2;
#pragma omp parallel default(shared) private(i, j, k, r1, r2, r3, r4, r5, t1, t2)
    {
#pragma omp for nowait
        for (i = 1; i <= grid_points[0] - 2; i++) {
            for (j = 1; j <= grid_points[1] - 2; j++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    r1 = rhs[0][i][j][k];
                    r2 = rhs[1][i][j][k];
                    r3 = rhs[2][i][j][k];
                    r4 = rhs[3][i][j][k];
                    r5 = rhs[4][i][j][k];
                    t1 = bt * r3;
                    t2 = 0.5 * (r4 + r5);
                    rhs[0][i][j][k] = -r2;
                    rhs[1][i][j][k] = r1;
                    rhs[2][i][j][k] = bt * (r4 - r5);
                    rhs[3][i][j][k] = -t1 + t2;
                    rhs[4][i][j][k] = t1 + t2;
                }
            }
        }
    }
}
static void pinvr(void ) {
    int i;
    int j;
    int k;
    double r1;
    double r2;
    double r3;
    double r4;
    double r5;
    double t1;
    double t2;
#pragma omp parallel default(shared) private(i, j, k, r1, r2, r3, r4, r5, t1, t2)
    {
#pragma omp for nowait
        for (i = 1; i <= grid_points[0] - 2; i++) {
            for (j = 1; j <= grid_points[1] - 2; j++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    r1 = rhs[0][i][j][k];
                    r2 = rhs[1][i][j][k];
                    r3 = rhs[2][i][j][k];
                    r4 = rhs[3][i][j][k];
                    r5 = rhs[4][i][j][k];
                    t1 = bt * r1;
                    t2 = 0.5 * (r4 + r5);
                    rhs[0][i][j][k] = bt * (r4 - r5);
                    rhs[1][i][j][k] = -r3;
                    rhs[2][i][j][k] = r2;
                    rhs[3][i][j][k] = -t1 + t2;
                    rhs[4][i][j][k] = t1 + t2;
                }
            }
        }
    }
}
static void compute_rhs(void ) {
#pragma omp parallel
    {
        int i;
        int j;
        int k;
        int m;
        double aux;
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
        for (i = 0; i <= grid_points[0] - 1; i++) {
            for (j = 0; j <= grid_points[1] - 1; j++) {
                for (k = 0; k <= grid_points[2] - 1; k++) {
                    rho_inv = 1.0 / u[0][i][j][k];
                    rho_i[i][j][k] = rho_inv;
                    us[i][j][k] = u[1][i][j][k] * rho_inv;
                    vs[i][j][k] = u[2][i][j][k] * rho_inv;
                    ws[i][j][k] = u[3][i][j][k] * rho_inv;
                    square[i][j][k] = 0.5 * (u[1][i][j][k] * u[1][i][j][k] + u[2][i][j][k] * u[2][i][j][k] + u[3][i][j][k] * u[3][i][j][k]) * rho_inv;
                    qs[i][j][k] = square[i][j][k] * rho_inv;
                    aux = c1c2 * rho_inv * (u[4][i][j][k] - square[i][j][k]);
                    aux = sqrt(aux);
                    speed[i][j][k] = aux;
                    ainv[i][j][k] = 1.0 / aux;
                }
            }
        }
        for (m = 0; m < 5; m++) {
#pragma omp for nowait
            for (i = 0; i <= grid_points[0] - 1; i++) {
                for (j = 0; j <= grid_points[1] - 1; j++) {
                    for (k = 0; k <= grid_points[2] - 1; k++) {
                        rhs[m][i][j][k] = forcing[m][i][j][k];
                    }
                }
            }
        }
#pragma omp for nowait
        for (i = 1; i <= grid_points[0] - 2; i++) {
            for (j = 1; j <= grid_points[1] - 2; j++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    uijk = us[i][j][k];
                    up1 = us[i + 1][j][k];
                    um1 = us[i - 1][j][k];
                    rhs[0][i][j][k] = rhs[0][i][j][k] + dx1tx1 * (u[0][i + 1][j][k] - 2.0 * u[0][i][j][k] + u[0][i - 1][j][k]) - tx2 * (u[1][i + 1][j][k] - u[1][i - 1][j][k]);
                    rhs[1][i][j][k] = rhs[1][i][j][k] + dx2tx1 * (u[1][i + 1][j][k] - 2.0 * u[1][i][j][k] + u[1][i - 1][j][k]) + xxcon2 * con43 * (up1 - 2.0 * uijk + um1) - tx2 * (u[1][i + 1][j][k] * up1 - u[1][i - 1][j][k] * um1 + (u[4][i + 1][j][k] - square[i + 1][j][k] - u[4][i - 1][j][k] + square[i - 1][j][k]) * c2);
                    rhs[2][i][j][k] = rhs[2][i][j][k] + dx3tx1 * (u[2][i + 1][j][k] - 2.0 * u[2][i][j][k] + u[2][i - 1][j][k]) + xxcon2 * (vs[i + 1][j][k] - 2.0 * vs[i][j][k] + vs[i - 1][j][k]) - tx2 * (u[2][i + 1][j][k] * up1 - u[2][i - 1][j][k] * um1);
                    rhs[3][i][j][k] = rhs[3][i][j][k] + dx4tx1 * (u[3][i + 1][j][k] - 2.0 * u[3][i][j][k] + u[3][i - 1][j][k]) + xxcon2 * (ws[i + 1][j][k] - 2.0 * ws[i][j][k] + ws[i - 1][j][k]) - tx2 * (u[3][i + 1][j][k] * up1 - u[3][i - 1][j][k] * um1);
                    rhs[4][i][j][k] = rhs[4][i][j][k] + dx5tx1 * (u[4][i + 1][j][k] - 2.0 * u[4][i][j][k] + u[4][i - 1][j][k]) + xxcon3 * (qs[i + 1][j][k] - 2.0 * qs[i][j][k] + qs[i - 1][j][k]) + xxcon4 * (up1 * up1 - 2.0 * uijk * uijk + um1 * um1) + xxcon5 * (u[4][i + 1][j][k] * rho_i[i + 1][j][k] - 2.0 * u[4][i][j][k] * rho_i[i][j][k] + u[4][i - 1][j][k] * rho_i[i - 1][j][k]) - tx2 * ((c1 * u[4][i + 1][j][k] - c2 * square[i + 1][j][k]) * up1 - (c1 * u[4][i - 1][j][k] - c2 * square[i - 1][j][k]) * um1);
                }
            }
        }
        i = 1;
        for (m = 0; m < 5; m++) {
#pragma omp for nowait
            for (j = 1; j <= grid_points[1] - 2; j++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    rhs[m][i][j][k] = rhs[m][i][j][k] - dssp * (5.0 * u[m][i][j][k] - 4.0 * u[m][i + 1][j][k] + u[m][i + 2][j][k]);
                }
            }
// #pragma omp dummyFlush BARRIER_START written([c5, Pface.f, u_exact.f, comz5, dy3ty1, rhoq.f, yycon2, ty1, tz3, c1345, xxcon4, dz3, dy1, dtdssp, conz1, comz4, dttx2, zzcon1, dzmax, c2dttz1, dx2tx1, qs.f, tz2, dy1ty1, dnzm1, us.f, yycon1, xxcon5, dz2, c3c4, c1c2, speed.f, q.f, dx4tx1, dttz1, lhs.f, xce.f, square.f, grid_points.f, zzcon2, ty3, dz2tz1, tx1, xxcon2, dx1, dz5, dy3, c2iv, dt, c5dssp, comz6, dnym1, rhon.f, zzcon3, cuf.f, c3c4tx3, ty2, xxcon3, dz4tz1, dy2, dz4, rhs.f, ue.f, dy2ty1, c1, comz1, dtty1, temp.f, zzcon4, tx3, con16, c1c5, dy4ty1, dx3, forcing.f, dy5, dx1tx1, dymax, c2, ainv.f, dttz2, con43, c2dttx1, zzcon5, tx2, c3c4ty3, yycon5, xxcon1, ws.f, dy4, rho_i.f, dx2, dx3tx1, u.f, c3, dz1tz1, dttx1, dnxm1, c4dssp, rhos.f, dxmax, buf.f, dx5tx1, yycon4, dz1, xcr.f, dx5, tz1, bt, c4, dz3tz1, dtty2, dy5ty1, c2dtty1, ce.f, yycon3, dssp, c3c4tz3, vs.f, dz5tz1, dx4, cv.f]) read([j, rhs.f, dssp, u, rhs, u.f, i, grid_points.f, grid_points])
#pragma omp barrier
        }
        i = 2;
        for (m = 0; m < 5; m++) {
#pragma omp for nowait
            for (j = 1; j <= grid_points[1] - 2; j++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    rhs[m][i][j][k] = rhs[m][i][j][k] - dssp * (-4.0 * u[m][i - 1][j][k] + 6.0 * u[m][i][j][k] - 4.0 * u[m][i + 1][j][k] + u[m][i + 2][j][k]);
                }
            }
// #pragma omp dummyFlush BARRIER_START written([c5, Pface.f, u_exact.f, comz5, dy3ty1, rhoq.f, yycon2, ty1, tz3, c1345, xxcon4, dz3, dy1, dtdssp, conz1, comz4, dttx2, zzcon1, dzmax, c2dttz1, dx2tx1, qs.f, tz2, dy1ty1, dnzm1, us.f, yycon1, xxcon5, dz2, c3c4, c1c2, speed.f, q.f, dx4tx1, dttz1, lhs.f, xce.f, square.f, grid_points.f, zzcon2, ty3, dz2tz1, tx1, xxcon2, dx1, dz5, dy3, c2iv, dt, c5dssp, comz6, dnym1, rhon.f, zzcon3, cuf.f, c3c4tx3, ty2, xxcon3, dz4tz1, dy2, dz4, rhs.f, ue.f, dy2ty1, c1, comz1, dtty1, temp.f, zzcon4, tx3, con16, c1c5, dy4ty1, dx3, forcing.f, dy5, dx1tx1, dymax, c2, ainv.f, dttz2, con43, c2dttx1, zzcon5, tx2, c3c4ty3, yycon5, xxcon1, ws.f, dy4, rho_i.f, dx2, dx3tx1, u.f, c3, dz1tz1, dttx1, dnxm1, c4dssp, rhos.f, dxmax, buf.f, dx5tx1, yycon4, dz1, xcr.f, dx5, tz1, bt, c4, dz3tz1, dtty2, dy5ty1, c2dtty1, ce.f, yycon3, dssp, c3c4tz3, vs.f, dz5tz1, dx4, cv.f]) read([j, rhs.f, dssp, u, rhs, u.f, i, grid_points.f, grid_points])
#pragma omp barrier
        }
        for (m = 0; m < 5; m++) {
#pragma omp for nowait
            for (i = 3 * 1; i <= grid_points[0] - 3 * 1 - 1; i++) {
                for (j = 1; j <= grid_points[1] - 2; j++) {
                    for (k = 1; k <= grid_points[2] - 2; k++) {
                        rhs[m][i][j][k] = rhs[m][i][j][k] - dssp * (u[m][i - 2][j][k] - 4.0 * u[m][i - 1][j][k] + 6.0 * u[m][i][j][k] - 4.0 * u[m][i + 1][j][k] + u[m][i + 2][j][k]);
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written([c5, Pface.f, u_exact.f, comz5, dy3ty1, rhoq.f, yycon2, ty1, tz3, c1345, xxcon4, dz3, dy1, dtdssp, conz1, comz4, dttx2, zzcon1, dzmax, c2dttz1, dx2tx1, qs.f, tz2, dy1ty1, dnzm1, us.f, yycon1, xxcon5, dz2, c3c4, c1c2, speed.f, q.f, dx4tx1, dttz1, lhs.f, xce.f, square.f, grid_points.f, zzcon2, ty3, dz2tz1, tx1, xxcon2, dx1, dz5, dy3, c2iv, dt, c5dssp, comz6, dnym1, rhon.f, zzcon3, cuf.f, c3c4tx3, ty2, xxcon3, dz4tz1, dy2, dz4, rhs.f, ue.f, dy2ty1, c1, comz1, dtty1, temp.f, zzcon4, tx3, con16, c1c5, dy4ty1, dx3, forcing.f, dy5, dx1tx1, dymax, c2, ainv.f, dttz2, con43, c2dttx1, zzcon5, tx2, c3c4ty3, yycon5, xxcon1, ws.f, dy4, rho_i.f, dx2, dx3tx1, u.f, c3, dz1tz1, dttx1, dnxm1, c4dssp, rhos.f, dxmax, buf.f, dx5tx1, yycon4, dz1, xcr.f, dx5, tz1, bt, c4, dz3tz1, dtty2, dy5ty1, c2dtty1, ce.f, yycon3, dssp, c3c4tz3, vs.f, dz5tz1, dx4, cv.f]) read([j, rhs.f, dssp, u, rhs, u.f, i, grid_points.f, grid_points])
#pragma omp barrier
        }
        i = grid_points[0] - 3;
        for (m = 0; m < 5; m++) {
#pragma omp for nowait
            for (j = 1; j <= grid_points[1] - 2; j++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    rhs[m][i][j][k] = rhs[m][i][j][k] - dssp * (u[m][i - 2][j][k] - 4.0 * u[m][i - 1][j][k] + 6.0 * u[m][i][j][k] - 4.0 * u[m][i + 1][j][k]);
                }
            }
// #pragma omp dummyFlush BARRIER_START written([c5, Pface.f, u_exact.f, comz5, dy3ty1, rhoq.f, yycon2, ty1, tz3, c1345, xxcon4, dz3, dy1, dtdssp, conz1, comz4, dttx2, zzcon1, dzmax, c2dttz1, dx2tx1, qs.f, tz2, dy1ty1, dnzm1, us.f, yycon1, xxcon5, dz2, c3c4, c1c2, speed.f, q.f, dx4tx1, dttz1, lhs.f, xce.f, square.f, grid_points.f, zzcon2, ty3, dz2tz1, tx1, xxcon2, dx1, dz5, dy3, c2iv, dt, c5dssp, comz6, dnym1, rhon.f, zzcon3, cuf.f, c3c4tx3, ty2, xxcon3, dz4tz1, dy2, dz4, rhs.f, ue.f, dy2ty1, c1, comz1, dtty1, temp.f, zzcon4, tx3, con16, c1c5, dy4ty1, dx3, forcing.f, dy5, dx1tx1, dymax, c2, ainv.f, dttz2, con43, c2dttx1, zzcon5, tx2, c3c4ty3, yycon5, xxcon1, ws.f, dy4, rho_i.f, dx2, dx3tx1, u.f, c3, dz1tz1, dttx1, dnxm1, c4dssp, rhos.f, dxmax, buf.f, dx5tx1, yycon4, dz1, xcr.f, dx5, tz1, bt, c4, dz3tz1, dtty2, dy5ty1, c2dtty1, ce.f, yycon3, dssp, c3c4tz3, vs.f, dz5tz1, dx4, cv.f]) read([j, rhs.f, dssp, u, rhs, u.f, grid_points.f, grid_points])
#pragma omp barrier
        }
        i = grid_points[0] - 2;
        for (m = 0; m < 5; m++) {
#pragma omp for nowait
            for (j = 1; j <= grid_points[1] - 2; j++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    rhs[m][i][j][k] = rhs[m][i][j][k] - dssp * (u[m][i - 2][j][k] - 4.0 * u[m][i - 1][j][k] + 5.0 * u[m][i][j][k]);
                }
            }
// #pragma omp dummyFlush BARRIER_START written([c5, Pface.f, u_exact.f, comz5, dy3ty1, rhoq.f, yycon2, ty1, tz3, c1345, xxcon4, dz3, dy1, dtdssp, conz1, comz4, dttx2, zzcon1, dzmax, c2dttz1, dx2tx1, qs.f, tz2, dy1ty1, dnzm1, us.f, yycon1, xxcon5, dz2, c3c4, c1c2, speed.f, q.f, dx4tx1, dttz1, lhs.f, xce.f, square.f, grid_points.f, zzcon2, ty3, dz2tz1, tx1, xxcon2, dx1, dz5, dy3, c2iv, dt, c5dssp, comz6, dnym1, rhon.f, zzcon3, cuf.f, c3c4tx3, ty2, xxcon3, dz4tz1, dy2, dz4, rhs.f, ue.f, dy2ty1, c1, comz1, dtty1, temp.f, zzcon4, tx3, con16, c1c5, dy4ty1, dx3, forcing.f, dy5, dx1tx1, dymax, c2, ainv.f, dttz2, con43, c2dttx1, zzcon5, tx2, c3c4ty3, yycon5, xxcon1, ws.f, dy4, rho_i.f, dx2, dx3tx1, u.f, c3, dz1tz1, dttx1, dnxm1, c4dssp, rhos.f, dxmax, buf.f, dx5tx1, yycon4, dz1, xcr.f, dx5, tz1, bt, c4, dz3tz1, dtty2, dy5ty1, c2dtty1, ce.f, yycon3, dssp, c3c4tz3, vs.f, dz5tz1, dx4, cv.f]) read([j, rhs.f, dssp, u, rhs, u.f, grid_points.f, grid_points])
#pragma omp barrier
        }
// #pragma omp dummyFlush BARRIER_START written([c5, Pface.f, u_exact.f, comz5, dy3ty1, rhoq.f, yycon2, ty1, tz3, c1345, xxcon4, dz3, dy1, dtdssp, conz1, comz4, dttx2, zzcon1, dzmax, c2dttz1, dx2tx1, qs.f, tz2, dy1ty1, dnzm1, us.f, yycon1, xxcon5, dz2, c3c4, c1c2, speed.f, q.f, dx4tx1, dttz1, lhs.f, xce.f, square.f, grid_points.f, zzcon2, ty3, dz2tz1, tx1, xxcon2, dx1, dz5, dy3, c2iv, dt, c5dssp, comz6, dnym1, rhon.f, zzcon3, cuf.f, c3c4tx3, ty2, xxcon3, dz4tz1, dy2, dz4, rhs.f, ue.f, dy2ty1, c1, comz1, dtty1, temp.f, zzcon4, tx3, con16, c1c5, dy4ty1, dx3, forcing.f, dy5, dx1tx1, dymax, c2, ainv.f, dttz2, con43, c2dttx1, zzcon5, tx2, c3c4ty3, yycon5, xxcon1, ws.f, dy4, rho_i.f, dx2, dx3tx1, u.f, c3, dz1tz1, dttx1, dnxm1, c4dssp, rhos.f, dxmax, buf.f, dx5tx1, yycon4, dz1, xcr.f, dx5, tz1, bt, c4, dz3tz1, dtty2, dy5ty1, c2dtty1, ce.f, yycon3, dssp, c3c4tz3, vs.f, dz5tz1, dx4, cv.f]) read([verified, comz5, dy3ty1, _imopVarPre2174, qs, lhsx, yycon2, _imopVarPre2166, us, speed, comz4, dttx2, _imopVarPre2173, qs.f, tz2, dy1ty1, _imopVarPre2186, us.f, _imopVarPre2167, c3c4, _imopVarPre2179, speed.f, _imopVarPre731, _imopVarPre2162, lhs.f, square.f, rhs_norm, grid_points.f, zzcon2, _imopVarPre2172, dz2tz1, dx1, sqrt, dt, _imopVarPre835, comz6, i, rhon.f, ninvr, c_print_results, zzcon3, lhs, ty2, dz4tz1, j, txinvr, rhs.f, _imopVarPre721, ainv, dy2ty1, c1, comz1, _imopVarPre2160, printf, rhon, _imopVarPre729, zzcon4, i, _imopVarPre2180, ws, c1c5, dy4ty1, m, rhs, c2, _imopVarPre825, _imopVarPre2161, ainv.f, square, pow, con43, c2dttx1, grid_points, zzcon5, yycon5, x_solve, ws.f, rho_i.f, dx2, u.f, j, i, dz1tz1, rho_i, _imopVarPre719, dttx1, dxmax, _imopVarPre2185, yycon4, _imopVarPre2168, vs, _imopVarPre2178, dx5, xcr.f, cv, u, bt, i, dz3tz1, _imopVarPre827, fabs, dy5ty1, yycon3, dssp, vs.f, _imopVarPre2184, dz5tz1, cv.f])
#pragma omp barrier
#pragma omp for nowait
        for (i = 1; i <= grid_points[0] - 2; i++) {
            for (j = 1; j <= grid_points[1] - 2; j++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    vijk = vs[i][j][k];
                    vp1 = vs[i][j + 1][k];
                    vm1 = vs[i][j - 1][k];
                    rhs[0][i][j][k] = rhs[0][i][j][k] + dy1ty1 * (u[0][i][j + 1][k] - 2.0 * u[0][i][j][k] + u[0][i][j - 1][k]) - ty2 * (u[2][i][j + 1][k] - u[2][i][j - 1][k]);
                    rhs[1][i][j][k] = rhs[1][i][j][k] + dy2ty1 * (u[1][i][j + 1][k] - 2.0 * u[1][i][j][k] + u[1][i][j - 1][k]) + yycon2 * (us[i][j + 1][k] - 2.0 * us[i][j][k] + us[i][j - 1][k]) - ty2 * (u[1][i][j + 1][k] * vp1 - u[1][i][j - 1][k] * vm1);
                    rhs[2][i][j][k] = rhs[2][i][j][k] + dy3ty1 * (u[2][i][j + 1][k] - 2.0 * u[2][i][j][k] + u[2][i][j - 1][k]) + yycon2 * con43 * (vp1 - 2.0 * vijk + vm1) - ty2 * (u[2][i][j + 1][k] * vp1 - u[2][i][j - 1][k] * vm1 + (u[4][i][j + 1][k] - square[i][j + 1][k] - u[4][i][j - 1][k] + square[i][j - 1][k]) * c2);
                    rhs[3][i][j][k] = rhs[3][i][j][k] + dy4ty1 * (u[3][i][j + 1][k] - 2.0 * u[3][i][j][k] + u[3][i][j - 1][k]) + yycon2 * (ws[i][j + 1][k] - 2.0 * ws[i][j][k] + ws[i][j - 1][k]) - ty2 * (u[3][i][j + 1][k] * vp1 - u[3][i][j - 1][k] * vm1);
                    rhs[4][i][j][k] = rhs[4][i][j][k] + dy5ty1 * (u[4][i][j + 1][k] - 2.0 * u[4][i][j][k] + u[4][i][j - 1][k]) + yycon3 * (qs[i][j + 1][k] - 2.0 * qs[i][j][k] + qs[i][j - 1][k]) + yycon4 * (vp1 * vp1 - 2.0 * vijk * vijk + vm1 * vm1) + yycon5 * (u[4][i][j + 1][k] * rho_i[i][j + 1][k] - 2.0 * u[4][i][j][k] * rho_i[i][j][k] + u[4][i][j - 1][k] * rho_i[i][j - 1][k]) - ty2 * ((c1 * u[4][i][j + 1][k] - c2 * square[i][j + 1][k]) * vp1 - (c1 * u[4][i][j - 1][k] - c2 * square[i][j - 1][k]) * vm1);
                }
            }
        }
        j = 1;
        for (m = 0; m < 5; m++) {
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    rhs[m][i][j][k] = rhs[m][i][j][k] - dssp * (5.0 * u[m][i][j][k] - 4.0 * u[m][i][j + 1][k] + u[m][i][j + 2][k]);
                }
            }
        }
        j = 2;
        for (m = 0; m < 5; m++) {
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    rhs[m][i][j][k] = rhs[m][i][j][k] - dssp * (-4.0 * u[m][i][j - 1][k] + 6.0 * u[m][i][j][k] - 4.0 * u[m][i][j + 1][k] + u[m][i][j + 2][k]);
                }
            }
        }
        for (m = 0; m < 5; m++) {
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (j = 3 * 1; j <= grid_points[1] - 3 * 1 - 1; j++) {
                    for (k = 1; k <= grid_points[2] - 2; k++) {
                        rhs[m][i][j][k] = rhs[m][i][j][k] - dssp * (u[m][i][j - 2][k] - 4.0 * u[m][i][j - 1][k] + 6.0 * u[m][i][j][k] - 4.0 * u[m][i][j + 1][k] + u[m][i][j + 2][k]);
                    }
                }
            }
        }
        j = grid_points[1] - 3;
        for (m = 0; m < 5; m++) {
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    rhs[m][i][j][k] = rhs[m][i][j][k] - dssp * (u[m][i][j - 2][k] - 4.0 * u[m][i][j - 1][k] + 6.0 * u[m][i][j][k] - 4.0 * u[m][i][j + 1][k]);
                }
            }
        }
        j = grid_points[1] - 2;
        for (m = 0; m < 5; m++) {
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    rhs[m][i][j][k] = rhs[m][i][j][k] - dssp * (u[m][i][j - 2][k] - 4.0 * u[m][i][j - 1][k] + 5.0 * u[m][i][j][k]);
                }
            }
        }
#pragma omp for nowait
        for (i = 1; i <= grid_points[0] - 2; i++) {
            for (j = 1; j <= grid_points[1] - 2; j++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    wijk = ws[i][j][k];
                    wp1 = ws[i][j][k + 1];
                    wm1 = ws[i][j][k - 1];
                    rhs[0][i][j][k] = rhs[0][i][j][k] + dz1tz1 * (u[0][i][j][k + 1] - 2.0 * u[0][i][j][k] + u[0][i][j][k - 1]) - tz2 * (u[3][i][j][k + 1] - u[3][i][j][k - 1]);
                    rhs[1][i][j][k] = rhs[1][i][j][k] + dz2tz1 * (u[1][i][j][k + 1] - 2.0 * u[1][i][j][k] + u[1][i][j][k - 1]) + zzcon2 * (us[i][j][k + 1] - 2.0 * us[i][j][k] + us[i][j][k - 1]) - tz2 * (u[1][i][j][k + 1] * wp1 - u[1][i][j][k - 1] * wm1);
                    rhs[2][i][j][k] = rhs[2][i][j][k] + dz3tz1 * (u[2][i][j][k + 1] - 2.0 * u[2][i][j][k] + u[2][i][j][k - 1]) + zzcon2 * (vs[i][j][k + 1] - 2.0 * vs[i][j][k] + vs[i][j][k - 1]) - tz2 * (u[2][i][j][k + 1] * wp1 - u[2][i][j][k - 1] * wm1);
                    rhs[3][i][j][k] = rhs[3][i][j][k] + dz4tz1 * (u[3][i][j][k + 1] - 2.0 * u[3][i][j][k] + u[3][i][j][k - 1]) + zzcon2 * con43 * (wp1 - 2.0 * wijk + wm1) - tz2 * (u[3][i][j][k + 1] * wp1 - u[3][i][j][k - 1] * wm1 + (u[4][i][j][k + 1] - square[i][j][k + 1] - u[4][i][j][k - 1] + square[i][j][k - 1]) * c2);
                    rhs[4][i][j][k] = rhs[4][i][j][k] + dz5tz1 * (u[4][i][j][k + 1] - 2.0 * u[4][i][j][k] + u[4][i][j][k - 1]) + zzcon3 * (qs[i][j][k + 1] - 2.0 * qs[i][j][k] + qs[i][j][k - 1]) + zzcon4 * (wp1 * wp1 - 2.0 * wijk * wijk + wm1 * wm1) + zzcon5 * (u[4][i][j][k + 1] * rho_i[i][j][k + 1] - 2.0 * u[4][i][j][k] * rho_i[i][j][k] + u[4][i][j][k - 1] * rho_i[i][j][k - 1]) - tz2 * ((c1 * u[4][i][j][k + 1] - c2 * square[i][j][k + 1]) * wp1 - (c1 * u[4][i][j][k - 1] - c2 * square[i][j][k - 1]) * wm1);
                }
            }
        }
        k = 1;
        for (m = 0; m < 5; m++) {
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (j = 1; j <= grid_points[1] - 2; j++) {
                    rhs[m][i][j][k] = rhs[m][i][j][k] - dssp * (5.0 * u[m][i][j][k] - 4.0 * u[m][i][j][k + 1] + u[m][i][j][k + 2]);
                }
            }
        }
        k = 2;
        for (m = 0; m < 5; m++) {
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (j = 1; j <= grid_points[1] - 2; j++) {
                    rhs[m][i][j][k] = rhs[m][i][j][k] - dssp * (-4.0 * u[m][i][j][k - 1] + 6.0 * u[m][i][j][k] - 4.0 * u[m][i][j][k + 1] + u[m][i][j][k + 2]);
                }
            }
        }
        for (m = 0; m < 5; m++) {
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (j = 1; j <= grid_points[1] - 2; j++) {
                    for (k = 3 * 1; k <= grid_points[2] - 3 * 1 - 1; k++) {
                        rhs[m][i][j][k] = rhs[m][i][j][k] - dssp * (u[m][i][j][k - 2] - 4.0 * u[m][i][j][k - 1] + 6.0 * u[m][i][j][k] - 4.0 * u[m][i][j][k + 1] + u[m][i][j][k + 2]);
                    }
                }
            }
        }
        k = grid_points[2] - 3;
        for (m = 0; m < 5; m++) {
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (j = 1; j <= grid_points[1] - 2; j++) {
                    rhs[m][i][j][k] = rhs[m][i][j][k] - dssp * (u[m][i][j][k - 2] - 4.0 * u[m][i][j][k - 1] + 6.0 * u[m][i][j][k] - 4.0 * u[m][i][j][k + 1]);
                }
            }
        }
        k = grid_points[2] - 2;
        for (m = 0; m < 5; m++) {
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (j = 1; j <= grid_points[1] - 2; j++) {
                    rhs[m][i][j][k] = rhs[m][i][j][k] - dssp * (u[m][i][j][k - 2] - 4.0 * u[m][i][j][k - 1] + 5.0 * u[m][i][j][k]);
                }
            }
        }
        for (m = 0; m < 5; m++) {
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (j = 1; j <= grid_points[1] - 2; j++) {
                    for (k = 1; k <= grid_points[2] - 2; k++) {
                        rhs[m][i][j][k] = rhs[m][i][j][k] * dt;
                    }
                }
            }
        }
    }
}
static void set_constants(void ) {
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
    int _imopVarPre2095;
    double _imopVarPre2096;
    _imopVarPre2095 = (dx3 > dx4);
    if (_imopVarPre2095) {
        _imopVarPre2096 = dx3;
    } else {
        _imopVarPre2096 = dx4;
    }
    dxmax = _imopVarPre2096;
    int _imopVarPre2099;
    double _imopVarPre2100;
    _imopVarPre2099 = (dy2 > dy4);
    if (_imopVarPre2099) {
        _imopVarPre2100 = dy2;
    } else {
        _imopVarPre2100 = dy4;
    }
    dymax = _imopVarPre2100;
    int _imopVarPre2103;
    double _imopVarPre2104;
    _imopVarPre2103 = (dz2 > dz3);
    if (_imopVarPre2103) {
        _imopVarPre2104 = dz2;
    } else {
        _imopVarPre2104 = dz3;
    }
    dzmax = _imopVarPre2104;
    int _imopVarPre2145;
    double _imopVarPre2146;
    int _imopVarPre2147;
    double _imopVarPre2148;
    int _imopVarPre2155;
    double _imopVarPre2156;
    _imopVarPre2145 = (dy1 > dz1);
    if (_imopVarPre2145) {
        _imopVarPre2146 = dy1;
    } else {
        _imopVarPre2146 = dz1;
    }
    _imopVarPre2147 = (dx1 > _imopVarPre2146);
    if (_imopVarPre2147) {
        _imopVarPre2148 = dx1;
    } else {
        _imopVarPre2155 = (dy1 > dz1);
        if (_imopVarPre2155) {
            _imopVarPre2156 = dy1;
        } else {
            _imopVarPre2156 = dz1;
        }
        _imopVarPre2148 = _imopVarPre2156;
    }
    dssp = 0.25 * _imopVarPre2148;
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
static void txinvr(void ) {
    int i;
    int j;
    int k;
    double t1;
    double t2;
    double t3;
    double ac;
    double ru1;
    double uu;
    double vv;
    double ww;
    double r1;
    double r2;
    double r3;
    double r4;
    double r5;
    double ac2inv;
#pragma omp for nowait
    for (i = 1; i <= grid_points[0] - 2; i++) {
        for (j = 1; j <= grid_points[1] - 2; j++) {
            for (k = 1; k <= grid_points[2] - 2; k++) {
                ru1 = rho_i[i][j][k];
                uu = us[i][j][k];
                vv = vs[i][j][k];
                ww = ws[i][j][k];
                ac = speed[i][j][k];
                ac2inv = ainv[i][j][k] * ainv[i][j][k];
                r1 = rhs[0][i][j][k];
                r2 = rhs[1][i][j][k];
                r3 = rhs[2][i][j][k];
                r4 = rhs[3][i][j][k];
                r5 = rhs[4][i][j][k];
                t1 = c2 * ac2inv * (qs[i][j][k] * r1 - uu * r2 - vv * r3 - ww * r4 + r5);
                t2 = bt * ru1 * (uu * r1 - r2);
                t3 = (bt * ru1 * ac) * t1;
                rhs[0][i][j][k] = r1 - t1;
                rhs[1][i][j][k] = -ru1 * (ww * r1 - r4);
                rhs[2][i][j][k] = ru1 * (vv * r1 - r3);
                rhs[3][i][j][k] = -t2 + t3;
                rhs[4][i][j][k] = t2 + t3;
            }
        }
    }
}
static void tzetar(void ) {
    int i;
    int j;
    int k;
    double t1;
    double t2;
    double t3;
    double ac;
    double xvel;
    double yvel;
    double zvel;
    double r1;
    double r2;
    double r3;
    double r4;
    double r5;
    double btuz;
    double acinv;
    double ac2u;
    double uzik1;
#pragma omp for private(i, j, k, t1, t2, t3, ac, xvel, yvel, zvel, r1, r2, r3, r4, r5, btuz, ac2u, uzik1) nowait
    for (i = 1; i <= grid_points[0] - 2; i++) {
        for (j = 1; j <= grid_points[1] - 2; j++) {
            for (k = 1; k <= grid_points[2] - 2; k++) {
                xvel = us[i][j][k];
                yvel = vs[i][j][k];
                zvel = ws[i][j][k];
                ac = speed[i][j][k];
                acinv = ainv[i][j][k];
                ac2u = ac * ac;
                r1 = rhs[0][i][j][k];
                r2 = rhs[1][i][j][k];
                r3 = rhs[2][i][j][k];
                r4 = rhs[3][i][j][k];
                r5 = rhs[4][i][j][k];
                uzik1 = u[0][i][j][k];
                btuz = bt * uzik1;
                t1 = btuz * acinv * (r4 + r5);
                t2 = r3 + t1;
                t3 = btuz * (r4 - r5);
                rhs[0][i][j][k] = t2;
                rhs[1][i][j][k] = -uzik1 * r2 + xvel * t2;
                rhs[2][i][j][k] = uzik1 * r1 + yvel * t2;
                rhs[3][i][j][k] = zvel * t2 + t3;
                rhs[4][i][j][k] = uzik1 * (-xvel * r2 + yvel * r1) + qs[i][j][k] * t2 + c2iv * ac2u * t1 + zvel * t3;
            }
        }
    }
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
    int _imopVarPre2160;
    int _imopVarPre2161;
    int _imopVarPre2162;
    _imopVarPre2160 = grid_points[0] == 12;
    if (_imopVarPre2160) {
        _imopVarPre2161 = grid_points[1] == 12;
        if (_imopVarPre2161) {
            _imopVarPre2162 = grid_points[2] == 12;
            if (_imopVarPre2162) {
                _imopVarPre2162 = no_time_steps == 100;
            }
            _imopVarPre2161 = _imopVarPre2162;
        }
        _imopVarPre2160 = _imopVarPre2161;
    }
    if (_imopVarPre2160) {
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
    } else {
        int _imopVarPre2166;
        int _imopVarPre2167;
        int _imopVarPre2168;
        _imopVarPre2166 = grid_points[0] == 36;
        if (_imopVarPre2166) {
            _imopVarPre2167 = grid_points[1] == 36;
            if (_imopVarPre2167) {
                _imopVarPre2168 = grid_points[2] == 36;
                if (_imopVarPre2168) {
                    _imopVarPre2168 = no_time_steps == 400;
                }
                _imopVarPre2167 = _imopVarPre2168;
            }
            _imopVarPre2166 = _imopVarPre2167;
        }
        if (_imopVarPre2166) {
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
        } else {
            int _imopVarPre2172;
            int _imopVarPre2173;
            int _imopVarPre2174;
            _imopVarPre2172 = grid_points[0] == 64;
            if (_imopVarPre2172) {
                _imopVarPre2173 = grid_points[1] == 64;
                if (_imopVarPre2173) {
                    _imopVarPre2174 = grid_points[2] == 64;
                    if (_imopVarPre2174) {
                        _imopVarPre2174 = no_time_steps == 400;
                    }
                    _imopVarPre2173 = _imopVarPre2174;
                }
                _imopVarPre2172 = _imopVarPre2173;
            }
            if (_imopVarPre2172) {
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
            } else {
                int _imopVarPre2178;
                int _imopVarPre2179;
                int _imopVarPre2180;
                _imopVarPre2178 = grid_points[0] == 102;
                if (_imopVarPre2178) {
                    _imopVarPre2179 = grid_points[1] == 102;
                    if (_imopVarPre2179) {
                        _imopVarPre2180 = grid_points[2] == 102;
                        if (_imopVarPre2180) {
                            _imopVarPre2180 = no_time_steps == 400;
                        }
                        _imopVarPre2179 = _imopVarPre2180;
                    }
                    _imopVarPre2178 = _imopVarPre2179;
                }
                if (_imopVarPre2178) {
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
                } else {
                    int _imopVarPre2184;
                    int _imopVarPre2185;
                    int _imopVarPre2186;
                    _imopVarPre2184 = grid_points[0] == 162;
                    if (_imopVarPre2184) {
                        _imopVarPre2185 = grid_points[1] == 162;
                        if (_imopVarPre2185) {
                            _imopVarPre2186 = grid_points[2] == 162;
                            if (_imopVarPre2186) {
                                _imopVarPre2186 = no_time_steps == 400;
                            }
                            _imopVarPre2185 = _imopVarPre2186;
                        }
                        _imopVarPre2184 = _imopVarPre2185;
                    }
                    if (_imopVarPre2184) {
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
                }
            }
        }
    }
    for (m = 0; m < 5; m++) {
        double _imopVarPre2188;
        double _imopVarPre2189;
        _imopVarPre2188 = (xcr[m] - xcrref[m]) / xcrref[m];
        _imopVarPre2189 = fabs(_imopVarPre2188);
        xcrdif[m] = _imopVarPre2189;
        double _imopVarPre2191;
        double _imopVarPre2192;
        _imopVarPre2191 = (xce[m] - xceref[m]) / xceref[m];
        _imopVarPre2192 = fabs(_imopVarPre2191);
        xcedif[m] = _imopVarPre2192;
    }
    if (*class != 'U') {
        char _imopVarPre2194;
        _imopVarPre2194 = *class;
        printf(" Verification being performed for class %1c\n", _imopVarPre2194);
        printf(" accuracy setting for epsilon = %20.13e\n", epsilon);
        double _imopVarPre2197;
        double _imopVarPre2198;
        _imopVarPre2197 = dt - dtref;
        _imopVarPre2198 = fabs(_imopVarPre2197);
        if (_imopVarPre2198 > epsilon) {
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
            double _imopVarPre2200;
            _imopVarPre2200 = xcr[m];
            printf("          %2d%20.13e\n", m, _imopVarPre2200);
        } else {
            if (xcrdif[m] > epsilon) {
                *verified = 0;
                double _imopVarPre2204;
                double _imopVarPre2205;
                double _imopVarPre2206;
                _imopVarPre2204 = xcrdif[m];
                _imopVarPre2205 = xcrref[m];
                _imopVarPre2206 = xcr[m];
                printf(" FAILURE: %2d%20.13e%20.13e%20.13e\n", m, _imopVarPre2206, _imopVarPre2205, _imopVarPre2204);
            } else {
                double _imopVarPre2210;
                double _imopVarPre2211;
                double _imopVarPre2212;
                _imopVarPre2210 = xcrdif[m];
                _imopVarPre2211 = xcrref[m];
                _imopVarPre2212 = xcr[m];
                printf("          %2d%20.13e%20.13e%20.13e\n", m, _imopVarPre2212, _imopVarPre2211, _imopVarPre2210);
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
            double _imopVarPre2214;
            _imopVarPre2214 = xce[m];
            printf("          %2d%20.13e\n", m, _imopVarPre2214);
        } else {
            if (xcedif[m] > epsilon) {
                *verified = 0;
                double _imopVarPre2218;
                double _imopVarPre2219;
                double _imopVarPre2220;
                _imopVarPre2218 = xcedif[m];
                _imopVarPre2219 = xceref[m];
                _imopVarPre2220 = xce[m];
                printf(" FAILURE: %2d%20.13e%20.13e%20.13e\n", m, _imopVarPre2220, _imopVarPre2219, _imopVarPre2218);
            } else {
                double _imopVarPre2224;
                double _imopVarPre2225;
                double _imopVarPre2226;
                _imopVarPre2224 = xcedif[m];
                _imopVarPre2225 = xceref[m];
                _imopVarPre2226 = xce[m];
                printf("          %2d%20.13e%20.13e%20.13e\n", m, _imopVarPre2226, _imopVarPre2225, _imopVarPre2224);
            }
        }
    }
    if (*class == 'U') {
        printf(" No reference values provided\n");
        printf(" No verification performed\n");
    } else {
        if (*verified) {
            printf(" Verification Successful\n");
        } else {
            printf(" Verification failed\n");
        }
    }
}
static void x_solve(void ) {
#pragma omp parallel
    {
        int i;
        int j;
        int k;
        int n;
        int i1;
        int i2;
        int m;
        double fac1;
        double fac2;
        lhsx();
        n = 0;
        for (i = 0; i <= grid_points[0] - 3; i++) {
            i1 = i + 1;
            i2 = i + 2;
#pragma omp for nowait
            for (j = 1; j <= grid_points[1] - 2; j++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    fac1 = 1. / lhs[n + 2][i][j][k];
                    lhs[n + 3][i][j][k] = fac1 * lhs[n + 3][i][j][k];
                    lhs[n + 4][i][j][k] = fac1 * lhs[n + 4][i][j][k];
                    for (m = 0; m < 3; m++) {
                        rhs[m][i][j][k] = fac1 * rhs[m][i][j][k];
                    }
                    lhs[n + 2][i1][j][k] = lhs[n + 2][i1][j][k] - lhs[n + 1][i1][j][k] * lhs[n + 3][i][j][k];
                    lhs[n + 3][i1][j][k] = lhs[n + 3][i1][j][k] - lhs[n + 1][i1][j][k] * lhs[n + 4][i][j][k];
                    for (m = 0; m < 3; m++) {
                        rhs[m][i1][j][k] = rhs[m][i1][j][k] - lhs[n + 1][i1][j][k] * rhs[m][i][j][k];
                    }
                    lhs[n + 1][i2][j][k] = lhs[n + 1][i2][j][k] - lhs[n + 0][i2][j][k] * lhs[n + 3][i][j][k];
                    lhs[n + 2][i2][j][k] = lhs[n + 2][i2][j][k] - lhs[n + 0][i2][j][k] * lhs[n + 4][i][j][k];
                    for (m = 0; m < 3; m++) {
                        rhs[m][i2][j][k] = rhs[m][i2][j][k] - lhs[n + 0][i2][j][k] * rhs[m][i][j][k];
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written([rhs.f, lhs.f]) read([rhs.f, rhs, lhs.f, j, grid_points.f, grid_points, lhs])
#pragma omp barrier
        }
        i = grid_points[0] - 2;
        i1 = grid_points[0] - 1;
#pragma omp for nowait
        for (j = 1; j <= grid_points[1] - 2; j++) {
            for (k = 1; k <= grid_points[2] - 2; k++) {
                fac1 = 1.0 / lhs[n + 2][i][j][k];
                lhs[n + 3][i][j][k] = fac1 * lhs[n + 3][i][j][k];
                lhs[n + 4][i][j][k] = fac1 * lhs[n + 4][i][j][k];
                for (m = 0; m < 3; m++) {
                    rhs[m][i][j][k] = fac1 * rhs[m][i][j][k];
                }
                lhs[n + 2][i1][j][k] = lhs[n + 2][i1][j][k] - lhs[n + 1][i1][j][k] * lhs[n + 3][i][j][k];
                lhs[n + 3][i1][j][k] = lhs[n + 3][i1][j][k] - lhs[n + 1][i1][j][k] * lhs[n + 4][i][j][k];
                for (m = 0; m < 3; m++) {
                    rhs[m][i1][j][k] = rhs[m][i1][j][k] - lhs[n + 1][i1][j][k] * rhs[m][i][j][k];
                }
                fac2 = 1. / lhs[n + 2][i1][j][k];
                for (m = 0; m < 3; m++) {
                    rhs[m][i1][j][k] = fac2 * rhs[m][i1][j][k];
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written([rhs.f, lhs.f]) read([rhs.f, comz1, comz5, dtty1, rhoq.f, j, dy1, c1c5, speed, pinvr, dy5, _imopVarPre1359, _imopVarPre1347, m, rhs, dymax, comz4, con43, grid_points, rhoq, i, c3c4, _imopVarPre1453, rho_i.f, speed.f, i, rho_i, i, lhs.f, grid_points.f, vs, dy3, _imopVarPre1349, _imopVarPre1357, cv, bt, comz6, i, ninvr, dtty2, lhs, c2dtty1, lhsy, y_solve, vs.f, _imopVarPre1455, j, _imopVarPre1463, cv.f])
#pragma omp barrier
        for (m = 3; m < 5; m++) {
            n = (m - 3 + 1) * 5;
            for (i = 0; i <= grid_points[0] - 3; i++) {
                i1 = i + 1;
                i2 = i + 2;
#pragma omp for nowait
                for (j = 1; j <= grid_points[1] - 2; j++) {
                    for (k = 1; k <= grid_points[2] - 2; k++) {
                        fac1 = 1. / lhs[n + 2][i][j][k];
                        lhs[n + 3][i][j][k] = fac1 * lhs[n + 3][i][j][k];
                        lhs[n + 4][i][j][k] = fac1 * lhs[n + 4][i][j][k];
                        rhs[m][i][j][k] = fac1 * rhs[m][i][j][k];
                        lhs[n + 2][i1][j][k] = lhs[n + 2][i1][j][k] - lhs[n + 1][i1][j][k] * lhs[n + 3][i][j][k];
                        lhs[n + 3][i1][j][k] = lhs[n + 3][i1][j][k] - lhs[n + 1][i1][j][k] * lhs[n + 4][i][j][k];
                        rhs[m][i1][j][k] = rhs[m][i1][j][k] - lhs[n + 1][i1][j][k] * rhs[m][i][j][k];
                        lhs[n + 1][i2][j][k] = lhs[n + 1][i2][j][k] - lhs[n + 0][i2][j][k] * lhs[n + 3][i][j][k];
                        lhs[n + 2][i2][j][k] = lhs[n + 2][i2][j][k] - lhs[n + 0][i2][j][k] * lhs[n + 4][i][j][k];
                        rhs[m][i2][j][k] = rhs[m][i2][j][k] - lhs[n + 0][i2][j][k] * rhs[m][i][j][k];
                    }
                }
// #pragma omp dummyFlush BARRIER_START written([rhs.f, lhs.f]) read([rhs.f, rhs, lhs.f, j, grid_points.f, grid_points, lhs])
#pragma omp barrier
            }
            i = grid_points[0] - 2;
            i1 = grid_points[0] - 1;
#pragma omp for nowait
            for (j = 1; j <= grid_points[1] - 2; j++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    fac1 = 1. / lhs[n + 2][i][j][k];
                    lhs[n + 3][i][j][k] = fac1 * lhs[n + 3][i][j][k];
                    lhs[n + 4][i][j][k] = fac1 * lhs[n + 4][i][j][k];
                    rhs[m][i][j][k] = fac1 * rhs[m][i][j][k];
                    lhs[n + 2][i1][j][k] = lhs[n + 2][i1][j][k] - lhs[n + 1][i1][j][k] * lhs[n + 3][i][j][k];
                    lhs[n + 3][i1][j][k] = lhs[n + 3][i1][j][k] - lhs[n + 1][i1][j][k] * lhs[n + 4][i][j][k];
                    rhs[m][i1][j][k] = rhs[m][i1][j][k] - lhs[n + 1][i1][j][k] * rhs[m][i][j][k];
                    fac2 = 1. / lhs[n + 2][i1][j][k];
                    rhs[m][i1][j][k] = fac2 * rhs[m][i1][j][k];
                }
            }
// #pragma omp dummyFlush BARRIER_START written([rhs.f, lhs.f]) read([rhs.f, comz1, comz5, dtty1, rhoq.f, j, dy1, c1c5, speed, pinvr, dy5, _imopVarPre1359, _imopVarPre1347, m, rhs, dymax, comz4, con43, grid_points, rhoq, i, c3c4, _imopVarPre1453, rho_i.f, speed.f, i, rho_i, i, lhs.f, grid_points.f, vs, dy3, _imopVarPre1349, _imopVarPre1357, cv, bt, comz6, i, ninvr, dtty2, lhs, c2dtty1, lhsy, y_solve, vs.f, _imopVarPre1455, j, _imopVarPre1463, cv.f])
#pragma omp barrier
        }
        i = grid_points[0] - 2;
        i1 = grid_points[0] - 1;
        n = 0;
        for (m = 0; m < 3; m++) {
#pragma omp for nowait
            for (j = 1; j <= grid_points[1] - 2; j++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    rhs[m][i][j][k] = rhs[m][i][j][k] - lhs[n + 3][i][j][k] * rhs[m][i1][j][k];
                }
            }
// #pragma omp dummyFlush BARRIER_START written([rhs.f]) read([rhs.f, comz1, comz5, dtty1, rhoq.f, j, dy1, c1c5, speed, pinvr, dy5, _imopVarPre1359, _imopVarPre1347, m, rhs, dymax, comz4, con43, grid_points, rhoq, i, c3c4, _imopVarPre1453, rho_i.f, speed.f, i, rho_i, i, lhs.f, grid_points.f, vs, dy3, _imopVarPre1349, _imopVarPre1357, cv, bt, comz6, i, ninvr, dtty2, lhs, c2dtty1, lhsy, y_solve, vs.f, _imopVarPre1455, j, _imopVarPre1463, cv.f])
#pragma omp barrier
        }
        for (m = 3; m < 5; m++) {
#pragma omp for nowait
            for (j = 1; j <= grid_points[1] - 2; j++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    n = (m - 3 + 1) * 5;
                    rhs[m][i][j][k] = rhs[m][i][j][k] - lhs[n + 3][i][j][k] * rhs[m][i1][j][k];
                }
            }
// #pragma omp dummyFlush BARRIER_START written([rhs.f]) read([rhs.f, comz1, comz5, dtty1, rhoq.f, j, dy1, c1c5, speed, pinvr, dy5, _imopVarPre1359, _imopVarPre1347, m, rhs, dymax, comz4, con43, grid_points, rhoq, i, c3c4, _imopVarPre1453, rho_i.f, speed.f, i, rho_i, i, lhs.f, grid_points.f, vs, dy3, _imopVarPre1349, _imopVarPre1357, cv, bt, comz6, i, ninvr, dtty2, lhs, c2dtty1, lhsy, y_solve, vs.f, _imopVarPre1455, j, _imopVarPre1463, cv.f])
#pragma omp barrier
        }
        n = 0;
        for (i = grid_points[0] - 3; i >= 0; i--) {
            i1 = i + 1;
            i2 = i + 2;
#pragma omp for nowait
            for (m = 0; m < 3; m++) {
                for (j = 1; j <= grid_points[1] - 2; j++) {
                    for (k = 1; k <= grid_points[2] - 2; k++) {
                        rhs[m][i][j][k] = rhs[m][i][j][k] - lhs[n + 3][i][j][k] * rhs[m][i1][j][k] - lhs[n + 4][i][j][k] * rhs[m][i2][j][k];
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written([rhs.f]) read([rhs.f, comz1, comz5, dtty1, rhoq.f, j, dy1, c1c5, speed, pinvr, dy5, _imopVarPre1359, _imopVarPre1347, m, rhs, dymax, comz4, con43, grid_points, rhoq, i, c3c4, _imopVarPre1453, rho_i.f, speed.f, i, rho_i, i, lhs.f, grid_points.f, vs, dy3, _imopVarPre1349, _imopVarPre1357, cv, bt, comz6, i, ninvr, dtty2, lhs, c2dtty1, lhsy, y_solve, vs.f, _imopVarPre1455, j, _imopVarPre1463, cv.f])
#pragma omp barrier
        }
        for (m = 3; m < 5; m++) {
            n = (m - 3 + 1) * 5;
            for (i = grid_points[0] - 3; i >= 0; i--) {
                i1 = i + 1;
                i2 = i + 2;
#pragma omp for nowait
                for (j = 1; j <= grid_points[1] - 2; j++) {
                    for (k = 1; k <= grid_points[2] - 2; k++) {
                        rhs[m][i][j][k] = rhs[m][i][j][k] - lhs[n + 3][i][j][k] * rhs[m][i1][j][k] - lhs[n + 4][i][j][k] * rhs[m][i2][j][k];
                    }
                }
// #pragma omp dummyFlush BARRIER_START written([rhs.f]) read([rhs.f, i, comz1, rho_i, i, lhs.f, comz5, dtty1, grid_points.f, rhoq.f, vs, j, dy1, c1c5, speed, dy3, pinvr, _imopVarPre1349, dy5, _imopVarPre1359, _imopVarPre1347, _imopVarPre1357, cv, bt, rhs, dymax, comz4, comz6, i, ninvr, dtty2, con43, grid_points, rhoq, lhs, c2dtty1, lhsy, y_solve, vs.f, _imopVarPre1455, i, c3c4, j, _imopVarPre1453, rho_i.f, _imopVarPre1463, speed.f, cv.f])
#pragma omp barrier
            }
        }
    }
    ninvr();
}
static void y_solve(void ) {
#pragma omp parallel
    {
        int i;
        int j;
        int k;
        int n;
        int j1;
        int j2;
        int m;
        double fac1;
        double fac2;
        lhsy();
        n = 0;
        for (j = 0; j <= grid_points[1] - 3; j++) {
            j1 = j + 1;
            j2 = j + 2;
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    fac1 = 1. / lhs[n + 2][i][j][k];
                    lhs[n + 3][i][j][k] = fac1 * lhs[n + 3][i][j][k];
                    lhs[n + 4][i][j][k] = fac1 * lhs[n + 4][i][j][k];
                    for (m = 0; m < 3; m++) {
                        rhs[m][i][j][k] = fac1 * rhs[m][i][j][k];
                    }
                    lhs[n + 2][i][j1][k] = lhs[n + 2][i][j1][k] - lhs[n + 1][i][j1][k] * lhs[n + 3][i][j][k];
                    lhs[n + 3][i][j1][k] = lhs[n + 3][i][j1][k] - lhs[n + 1][i][j1][k] * lhs[n + 4][i][j][k];
                    for (m = 0; m < 3; m++) {
                        rhs[m][i][j1][k] = rhs[m][i][j1][k] - lhs[n + 1][i][j1][k] * rhs[m][i][j][k];
                    }
                    lhs[n + 1][i][j2][k] = lhs[n + 1][i][j2][k] - lhs[n + 0][i][j2][k] * lhs[n + 3][i][j][k];
                    lhs[n + 2][i][j2][k] = lhs[n + 2][i][j2][k] - lhs[n + 0][i][j2][k] * lhs[n + 4][i][j][k];
                    for (m = 0; m < 3; m++) {
                        rhs[m][i][j2][k] = rhs[m][i][j2][k] - lhs[n + 0][i][j2][k] * rhs[m][i][j][k];
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written([rhs.f, lhs.f]) read([rhs.f, i, rhs, lhs.f, grid_points.f, grid_points, lhs])
#pragma omp barrier
        }
        j = grid_points[1] - 2;
        j1 = grid_points[1] - 1;
#pragma omp for nowait
        for (i = 1; i <= grid_points[0] - 2; i++) {
            for (k = 1; k <= grid_points[2] - 2; k++) {
                fac1 = 1. / lhs[n + 2][i][j][k];
                lhs[n + 3][i][j][k] = fac1 * lhs[n + 3][i][j][k];
                lhs[n + 4][i][j][k] = fac1 * lhs[n + 4][i][j][k];
                for (m = 0; m < 3; m++) {
                    rhs[m][i][j][k] = fac1 * rhs[m][i][j][k];
                }
                lhs[n + 2][i][j1][k] = lhs[n + 2][i][j1][k] - lhs[n + 1][i][j1][k] * lhs[n + 3][i][j][k];
                lhs[n + 3][i][j1][k] = lhs[n + 3][i][j1][k] - lhs[n + 1][i][j1][k] * lhs[n + 4][i][j][k];
                for (m = 0; m < 3; m++) {
                    rhs[m][i][j1][k] = rhs[m][i][j1][k] - lhs[n + 1][i][j1][k] * rhs[m][i][j][k];
                }
                fac2 = 1. / lhs[n + 2][i][j1][k];
                for (m = 0; m < 3; m++) {
                    rhs[m][i][j1][k] = fac2 * rhs[m][i][j1][k];
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written([rhs.f, lhs.f]) read([rhs.f, ainv, k, comz1, comz5, qs, us, ws, c1c5, speed, pinvr, rhs, ainv.f, comz4, dttz2, dzmax, con43, grid_points, c2dttz1, qs.f, z_solve, _imopVarPre1987, _imopVarPre1975, us.f, ws.f, _imopVarPre2081, c3c4, rho_i.f, speed.f, i, u.f, dttz1, rho_i, i, i, lhs.f, tzetar, grid_points.f, rhos.f, lhsz, dz1, i, vs, dz5, c2iv, cv, bt, u, comz6, lhs, rhos, _imopVarPre1977, vs.f, _imopVarPre1985, _imopVarPre2083, dz4, _imopVarPre2091, cv.f, i])
#pragma omp barrier
        for (m = 3; m < 5; m++) {
            n = (m - 3 + 1) * 5;
            for (j = 0; j <= grid_points[1] - 3; j++) {
                j1 = j + 1;
                j2 = j + 2;
#pragma omp for nowait
                for (i = 1; i <= grid_points[0] - 2; i++) {
                    for (k = 1; k <= grid_points[2] - 2; k++) {
                        fac1 = 1. / lhs[n + 2][i][j][k];
                        lhs[n + 3][i][j][k] = fac1 * lhs[n + 3][i][j][k];
                        lhs[n + 4][i][j][k] = fac1 * lhs[n + 4][i][j][k];
                        rhs[m][i][j][k] = fac1 * rhs[m][i][j][k];
                        lhs[n + 2][i][j1][k] = lhs[n + 2][i][j1][k] - lhs[n + 1][i][j1][k] * lhs[n + 3][i][j][k];
                        lhs[n + 3][i][j1][k] = lhs[n + 3][i][j1][k] - lhs[n + 1][i][j1][k] * lhs[n + 4][i][j][k];
                        rhs[m][i][j1][k] = rhs[m][i][j1][k] - lhs[n + 1][i][j1][k] * rhs[m][i][j][k];
                        lhs[n + 1][i][j2][k] = lhs[n + 1][i][j2][k] - lhs[n + 0][i][j2][k] * lhs[n + 3][i][j][k];
                        lhs[n + 2][i][j2][k] = lhs[n + 2][i][j2][k] - lhs[n + 0][i][j2][k] * lhs[n + 4][i][j][k];
                        rhs[m][i][j2][k] = rhs[m][i][j2][k] - lhs[n + 0][i][j2][k] * rhs[m][i][j][k];
                    }
                }
// #pragma omp dummyFlush BARRIER_START written([rhs.f, lhs.f]) read([rhs.f, i, rhs, lhs.f, grid_points.f, grid_points, lhs])
#pragma omp barrier
            }
            j = grid_points[1] - 2;
            j1 = grid_points[1] - 1;
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    fac1 = 1. / lhs[n + 2][i][j][k];
                    lhs[n + 3][i][j][k] = fac1 * lhs[n + 3][i][j][k];
                    lhs[n + 4][i][j][k] = fac1 * lhs[n + 4][i][j][k];
                    rhs[m][i][j][k] = fac1 * rhs[m][i][j][k];
                    lhs[n + 2][i][j1][k] = lhs[n + 2][i][j1][k] - lhs[n + 1][i][j1][k] * lhs[n + 3][i][j][k];
                    lhs[n + 3][i][j1][k] = lhs[n + 3][i][j1][k] - lhs[n + 1][i][j1][k] * lhs[n + 4][i][j][k];
                    rhs[m][i][j1][k] = rhs[m][i][j1][k] - lhs[n + 1][i][j1][k] * rhs[m][i][j][k];
                    fac2 = 1. / lhs[n + 2][i][j1][k];
                    rhs[m][i][j1][k] = fac2 * rhs[m][i][j1][k];
                }
            }
// #pragma omp dummyFlush BARRIER_START written([rhs.f, lhs.f]) read([rhs.f, ainv, k, comz1, comz5, qs, us, ws, c1c5, speed, pinvr, rhs, ainv.f, comz4, dttz2, dzmax, con43, grid_points, c2dttz1, qs.f, z_solve, _imopVarPre1987, _imopVarPre1975, us.f, ws.f, _imopVarPre2081, c3c4, rho_i.f, speed.f, i, u.f, dttz1, rho_i, i, i, lhs.f, tzetar, grid_points.f, rhos.f, lhsz, dz1, i, vs, dz5, c2iv, cv, bt, u, comz6, lhs, rhos, _imopVarPre1977, vs.f, _imopVarPre1985, _imopVarPre2083, dz4, _imopVarPre2091, cv.f, i])
#pragma omp barrier
        }
        j = grid_points[1] - 2;
        j1 = grid_points[1] - 1;
        n = 0;
        for (m = 0; m < 3; m++) {
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    rhs[m][i][j][k] = rhs[m][i][j][k] - lhs[n + 3][i][j][k] * rhs[m][i][j1][k];
                }
            }
// #pragma omp dummyFlush BARRIER_START written([rhs.f]) read([rhs.f, k, ainv, comz1, comz5, qs, us, ws, c1c5, speed, pinvr, rhs, ainv.f, comz4, dttz2, dzmax, con43, grid_points, c2dttz1, qs.f, z_solve, _imopVarPre1987, _imopVarPre1975, us.f, ws.f, _imopVarPre2081, c3c4, rho_i.f, speed.f, i, u.f, rho_i, dttz1, i, i, lhs.f, tzetar, grid_points.f, rhos.f, lhsz, dz1, i, vs, dz5, c2iv, cv, bt, u, comz6, lhs, rhos, _imopVarPre1977, vs.f, _imopVarPre1985, dz4, _imopVarPre2083, _imopVarPre2091, cv.f, i])
#pragma omp barrier
        }
        for (m = 3; m < 5; m++) {
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (k = 1; k <= grid_points[2] - 2; k++) {
                    n = (m - 3 + 1) * 5;
                    rhs[m][i][j][k] = rhs[m][i][j][k] - lhs[n + 3][i][j][k] * rhs[m][i][j1][k];
                }
            }
// #pragma omp dummyFlush BARRIER_START written([rhs.f]) read([rhs.f, k, ainv, comz1, comz5, qs, us, ws, c1c5, speed, pinvr, rhs, ainv.f, comz4, dttz2, dzmax, con43, grid_points, c2dttz1, qs.f, z_solve, _imopVarPre1987, _imopVarPre1975, us.f, ws.f, _imopVarPre2081, c3c4, rho_i.f, speed.f, i, u.f, rho_i, dttz1, i, i, lhs.f, tzetar, grid_points.f, rhos.f, lhsz, dz1, i, vs, dz5, c2iv, cv, bt, u, comz6, lhs, rhos, _imopVarPre1977, vs.f, _imopVarPre1985, dz4, _imopVarPre2083, _imopVarPre2091, cv.f, i])
#pragma omp barrier
        }
        n = 0;
        for (m = 0; m < 3; m++) {
            for (j = grid_points[1] - 3; j >= 0; j--) {
                j1 = j + 1;
                j2 = j + 2;
#pragma omp for nowait
                for (i = 1; i <= grid_points[0] - 2; i++) {
                    for (k = 1; k <= grid_points[2] - 2; k++) {
                        rhs[m][i][j][k] = rhs[m][i][j][k] - lhs[n + 3][i][j][k] * rhs[m][i][j1][k] - lhs[n + 4][i][j][k] * rhs[m][i][j2][k];
                    }
                }
// #pragma omp dummyFlush BARRIER_START written([rhs.f]) read([rhs.f, k, ainv, comz1, comz5, qs, us, ws, c1c5, speed, pinvr, rhs, ainv.f, comz4, dttz2, dzmax, con43, grid_points, c2dttz1, qs.f, z_solve, _imopVarPre1987, _imopVarPre1975, us.f, ws.f, _imopVarPre2081, c3c4, rho_i.f, speed.f, i, u.f, rho_i, dttz1, i, i, lhs.f, tzetar, grid_points.f, rhos.f, lhsz, dz1, i, vs, dz5, c2iv, cv, bt, u, comz6, lhs, rhos, _imopVarPre1977, vs.f, _imopVarPre1985, dz4, _imopVarPre2083, _imopVarPre2091, cv.f, i])
#pragma omp barrier
            }
        }
        for (m = 3; m < 5; m++) {
            n = (m - 3 + 1) * 5;
            for (j = grid_points[1] - 3; j >= 0; j--) {
                j1 = j + 1;
                j2 = j1 + 1;
#pragma omp for nowait
                for (i = 1; i <= grid_points[0] - 2; i++) {
                    for (k = 1; k <= grid_points[2] - 2; k++) {
                        rhs[m][i][j][k] = rhs[m][i][j][k] - lhs[n + 3][i][j][k] * rhs[m][i][j1][k] - lhs[n + 4][i][j][k] * rhs[m][i][j2][k];
                    }
                }
// #pragma omp dummyFlush BARRIER_START written([rhs.f]) read([rhs.f, k, ainv, comz1, comz5, qs, us, ws, c1c5, speed, pinvr, rhs, ainv.f, comz4, dttz2, dzmax, con43, grid_points, c2dttz1, qs.f, z_solve, _imopVarPre1987, _imopVarPre1975, us.f, ws.f, _imopVarPre2081, c3c4, rho_i.f, speed.f, i, u.f, rho_i, dttz1, i, i, lhs.f, tzetar, grid_points.f, rhos.f, lhsz, dz1, i, vs, dz5, c2iv, cv, bt, u, comz6, lhs, rhos, _imopVarPre1977, vs.f, _imopVarPre1985, dz4, _imopVarPre2083, _imopVarPre2091, cv.f, i])
#pragma omp barrier
            }
        }
    }
    pinvr();
}
static void z_solve(void ) {
#pragma omp parallel
    {
        int i;
        int j;
        int k;
        int n;
        int k1;
        int k2;
        int m;
        double fac1;
        double fac2;
        lhsz();
        n = 0;
#pragma omp for nowait
        for (i = 1; i <= grid_points[0] - 2; i++) {
            for (j = 1; j <= grid_points[1] - 2; j++) {
                for (k = 0; k <= grid_points[2] - 3; k++) {
                    k1 = k + 1;
                    k2 = k + 2;
                    fac1 = 1. / lhs[n + 2][i][j][k];
                    lhs[n + 3][i][j][k] = fac1 * lhs[n + 3][i][j][k];
                    lhs[n + 4][i][j][k] = fac1 * lhs[n + 4][i][j][k];
                    for (m = 0; m < 3; m++) {
                        rhs[m][i][j][k] = fac1 * rhs[m][i][j][k];
                    }
                    lhs[n + 2][i][j][k1] = lhs[n + 2][i][j][k1] - lhs[n + 1][i][j][k1] * lhs[n + 3][i][j][k];
                    lhs[n + 3][i][j][k1] = lhs[n + 3][i][j][k1] - lhs[n + 1][i][j][k1] * lhs[n + 4][i][j][k];
                    for (m = 0; m < 3; m++) {
                        rhs[m][i][j][k1] = rhs[m][i][j][k1] - lhs[n + 1][i][j][k1] * rhs[m][i][j][k];
                    }
                    lhs[n + 1][i][j][k2] = lhs[n + 1][i][j][k2] - lhs[n + 0][i][j][k2] * lhs[n + 3][i][j][k];
                    lhs[n + 2][i][j][k2] = lhs[n + 2][i][j][k2] - lhs[n + 0][i][j][k2] * lhs[n + 4][i][j][k];
                    for (m = 0; m < 3; m++) {
                        rhs[m][i][j][k2] = rhs[m][i][j][k2] - lhs[n + 0][i][j][k2] * rhs[m][i][j][k];
                    }
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written([rhs.f, lhs.f]) read([rhs.f, rhs, i, lhs.f, grid_points.f, grid_points, lhs])
#pragma omp barrier
        k = grid_points[2] - 2;
        k1 = grid_points[2] - 1;
#pragma omp for nowait
        for (i = 1; i <= grid_points[0] - 2; i++) {
            for (j = 1; j <= grid_points[1] - 2; j++) {
                fac1 = 1. / lhs[n + 2][i][j][k];
                lhs[n + 3][i][j][k] = fac1 * lhs[n + 3][i][j][k];
                lhs[n + 4][i][j][k] = fac1 * lhs[n + 4][i][j][k];
                for (m = 0; m < 3; m++) {
                    rhs[m][i][j][k] = fac1 * rhs[m][i][j][k];
                }
                lhs[n + 2][i][j][k1] = lhs[n + 2][i][j][k1] - lhs[n + 1][i][j][k1] * lhs[n + 3][i][j][k];
                lhs[n + 3][i][j][k1] = lhs[n + 3][i][j][k1] - lhs[n + 1][i][j][k1] * lhs[n + 4][i][j][k];
                for (m = 0; m < 3; m++) {
                    rhs[m][i][j][k1] = rhs[m][i][j][k1] - lhs[n + 1][i][j][k1] * rhs[m][i][j][k];
                }
                fac2 = 1. / lhs[n + 2][i][j][k1];
                for (m = 0; m < 3; m++) {
                    rhs[m][i][j][k1] = fac2 * rhs[m][i][j][k1];
                }
            }
        }
// #pragma omp dummyFlush BARRIER_START written([rhs.f, lhs.f]) read([dy3ty1, lhsx, yycon2, us, dy1, timer_start, dttx2, dzmax, dx2tx1, tz2, _imopVarPre2186, dnzm1, us.f, yycon1, c1c2, i, _imopVarPre731, i, xce.f, timer_stop, lhsz, dx1, dz5, dy3, add, ue, dt, i, dnym1, _imopVarPre1977, ty2, lhsy, y_solve, dz4tz1, dz4, _imopVarPre1463, m, ue.f, dy2ty1, ainv, c1, comz1, _imopVarPre2160, rhon, compute_rhs, _imopVarPre729, zzcon4, i, j, forcing.f, dy5, c2, _imopVarPre825, _imopVarPre2161, ainv.f, con43, zzcon5, tx2, z_solve, xxcon1, i, rho_i.f, _imopVarPre1453, dx2, forcing, dx3tx1, u.f, i, rho_i, i, _imopVarPre719, dnxm1, buf, ce, buf.f, _imopVarPre2168, i, _imopVarPre2178, dx5, _imopVarPre1357, j, cv, u, bt, dz3tz1, _imopVarPre827, initialize, ce.f, dssp, _imopVarPre1455, _imopVarPre172, cv.f, verified, k, comz5, rhoq.f, _imopVarPre2174, qs, cuf, _imopVarPre2166, xxcon4, speed, pinvr, exact_rhs, _imopVarPre1359, _imopVarPre1347, adi, comz4, exact_solution, zzcon1, rhoq, _imopVarPre2173, c2dttz1, error_norm, qs.f, dy1ty1, _imopVarPre2167, xxcon5, _imopVarPre2081, c3c4, _imopVarPre2179, speed.f, q.f, dx4tx1, _imopVarPre2162, dttz1, lhs.f, i, square.f, tzetar, rhs_norm, grid_points.f, zzcon2, _imopVarPre2172, dz2tz1, xxcon2, sqrt, _imopVarPre1349, q, c2iv, _imopVarPre835, comz6, ninvr, rhon.f, zzcon3, lhs, cuf.f, timer_read, lhsinit, xxcon3, verify, j, txinvr, _imopVarPre2091, i, rhs.f, _imopVarPre721, printf, dtty1, _imopVarPre2180, ws, c1c5, dy4ty1, dx1tx1, m, rhs, dymax, square, dttz2, c2dttx1, grid_points, yycon5, _imopVarPre1987, x_solve, _imopVarPre1975, ws.f, j, dz1tz1, dttx1, rhos.f, dxmax, dx5tx1, _imopVarPre2185, yycon4, dz1, vs, timer_clear, xcr.f, i, dtty2, dy5ty1, fabs, rhos, c2dtty1, yycon3, vs.f, _imopVarPre1985, _imopVarPre2184, dz5tz1, _imopVarPre2083])
#pragma omp barrier
        for (m = 3; m < 5; m++) {
            n = (m - 3 + 1) * 5;
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (j = 1; j <= grid_points[1] - 2; j++) {
                    for (k = 0; k <= grid_points[2] - 3; k++) {
                        k1 = k + 1;
                        k2 = k + 2;
                        fac1 = 1. / lhs[n + 2][i][j][k];
                        lhs[n + 3][i][j][k] = fac1 * lhs[n + 3][i][j][k];
                        lhs[n + 4][i][j][k] = fac1 * lhs[n + 4][i][j][k];
                        rhs[m][i][j][k] = fac1 * rhs[m][i][j][k];
                        lhs[n + 2][i][j][k1] = lhs[n + 2][i][j][k1] - lhs[n + 1][i][j][k1] * lhs[n + 3][i][j][k];
                        lhs[n + 3][i][j][k1] = lhs[n + 3][i][j][k1] - lhs[n + 1][i][j][k1] * lhs[n + 4][i][j][k];
                        rhs[m][i][j][k1] = rhs[m][i][j][k1] - lhs[n + 1][i][j][k1] * rhs[m][i][j][k];
                        lhs[n + 1][i][j][k2] = lhs[n + 1][i][j][k2] - lhs[n + 0][i][j][k2] * lhs[n + 3][i][j][k];
                        lhs[n + 2][i][j][k2] = lhs[n + 2][i][j][k2] - lhs[n + 0][i][j][k2] * lhs[n + 4][i][j][k];
                        rhs[m][i][j][k2] = rhs[m][i][j][k2] - lhs[n + 0][i][j][k2] * rhs[m][i][j][k];
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written([rhs.f, lhs.f]) read([rhs.f, rhs, i, lhs.f, grid_points.f, grid_points, lhs])
#pragma omp barrier
            k = grid_points[2] - 2;
            k1 = grid_points[2] - 1;
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (j = 1; j <= grid_points[1] - 2; j++) {
                    fac1 = 1. / lhs[n + 2][i][j][k];
                    lhs[n + 3][i][j][k] = fac1 * lhs[n + 3][i][j][k];
                    lhs[n + 4][i][j][k] = fac1 * lhs[n + 4][i][j][k];
                    rhs[m][i][j][k] = fac1 * rhs[m][i][j][k];
                    lhs[n + 2][i][j][k1] = lhs[n + 2][i][j][k1] - lhs[n + 1][i][j][k1] * lhs[n + 3][i][j][k];
                    lhs[n + 3][i][j][k1] = lhs[n + 3][i][j][k1] - lhs[n + 1][i][j][k1] * lhs[n + 4][i][j][k];
                    rhs[m][i][j][k1] = rhs[m][i][j][k1] - lhs[n + 1][i][j][k1] * rhs[m][i][j][k];
                    fac2 = 1. / lhs[n + 2][i][j][k1];
                    rhs[m][i][j][k1] = fac2 * rhs[m][i][j][k1];
                }
            }
// #pragma omp dummyFlush BARRIER_START written([rhs.f, lhs.f]) read([dy3ty1, lhsx, yycon2, us, dy1, timer_start, dttx2, dzmax, dx2tx1, tz2, _imopVarPre2186, dnzm1, us.f, yycon1, c1c2, i, _imopVarPre731, i, xce.f, timer_stop, lhsz, dx1, dz5, dy3, add, ue, dt, i, dnym1, _imopVarPre1977, ty2, lhsy, y_solve, dz4tz1, dz4, _imopVarPre1463, m, ue.f, dy2ty1, ainv, c1, comz1, _imopVarPre2160, rhon, compute_rhs, _imopVarPre729, zzcon4, i, j, forcing.f, dy5, c2, _imopVarPre825, _imopVarPre2161, ainv.f, con43, zzcon5, tx2, z_solve, xxcon1, i, rho_i.f, _imopVarPre1453, dx2, forcing, dx3tx1, u.f, i, rho_i, i, _imopVarPre719, dnxm1, buf, ce, buf.f, _imopVarPre2168, i, _imopVarPre2178, dx5, _imopVarPre1357, j, cv, u, bt, dz3tz1, _imopVarPre827, initialize, ce.f, dssp, _imopVarPre1455, _imopVarPre172, cv.f, verified, k, comz5, rhoq.f, _imopVarPre2174, qs, cuf, _imopVarPre2166, xxcon4, speed, pinvr, exact_rhs, _imopVarPre1359, _imopVarPre1347, adi, comz4, exact_solution, zzcon1, rhoq, _imopVarPre2173, c2dttz1, error_norm, qs.f, dy1ty1, _imopVarPre2167, xxcon5, _imopVarPre2081, c3c4, _imopVarPre2179, speed.f, q.f, dx4tx1, _imopVarPre2162, dttz1, lhs.f, i, square.f, tzetar, rhs_norm, grid_points.f, zzcon2, _imopVarPre2172, dz2tz1, xxcon2, sqrt, _imopVarPre1349, q, c2iv, _imopVarPre835, comz6, ninvr, rhon.f, zzcon3, lhs, cuf.f, timer_read, lhsinit, xxcon3, verify, j, txinvr, _imopVarPre2091, i, rhs.f, _imopVarPre721, printf, dtty1, _imopVarPre2180, ws, c1c5, dy4ty1, dx1tx1, m, rhs, dymax, square, dttz2, c2dttx1, grid_points, yycon5, _imopVarPre1987, x_solve, _imopVarPre1975, ws.f, j, dz1tz1, dttx1, rhos.f, dxmax, dx5tx1, _imopVarPre2185, yycon4, dz1, vs, timer_clear, xcr.f, i, dtty2, dy5ty1, fabs, rhos, c2dtty1, yycon3, vs.f, _imopVarPre1985, _imopVarPre2184, dz5tz1, _imopVarPre2083])
#pragma omp barrier
        }
        k = grid_points[2] - 2;
        k1 = grid_points[2] - 1;
        n = 0;
        for (m = 0; m < 3; m++) {
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (j = 1; j <= grid_points[1] - 2; j++) {
                    rhs[m][i][j][k] = rhs[m][i][j][k] - lhs[n + 3][i][j][k] * rhs[m][i][j][k1];
                }
            }
// #pragma omp dummyFlush BARRIER_START written([rhs.f]) read([dy3ty1, lhsx, yycon2, us, dy1, timer_start, dttx2, dzmax, dx2tx1, tz2, _imopVarPre2186, dnzm1, us.f, yycon1, c1c2, i, _imopVarPre731, i, xce.f, timer_stop, lhsz, dx1, dz5, dy3, add, ue, dt, i, dnym1, _imopVarPre1977, ty2, lhsy, y_solve, dz4tz1, dz4, _imopVarPre1463, m, ue.f, dy2ty1, ainv, c1, comz1, _imopVarPre2160, rhon, compute_rhs, _imopVarPre729, zzcon4, i, j, forcing.f, dy5, c2, _imopVarPre825, _imopVarPre2161, ainv.f, con43, zzcon5, tx2, z_solve, xxcon1, i, rho_i.f, _imopVarPre1453, dx2, forcing, dx3tx1, u.f, i, rho_i, i, _imopVarPre719, dnxm1, buf, ce, buf.f, _imopVarPre2168, i, _imopVarPre2178, dx5, _imopVarPre1357, j, cv, u, bt, dz3tz1, _imopVarPre827, initialize, ce.f, dssp, _imopVarPre1455, _imopVarPre172, cv.f, verified, k, comz5, rhoq.f, _imopVarPre2174, qs, cuf, _imopVarPre2166, xxcon4, speed, pinvr, exact_rhs, _imopVarPre1359, _imopVarPre1347, adi, comz4, exact_solution, zzcon1, rhoq, _imopVarPre2173, c2dttz1, error_norm, qs.f, dy1ty1, _imopVarPre2167, xxcon5, _imopVarPre2081, c3c4, _imopVarPre2179, speed.f, q.f, dx4tx1, _imopVarPre2162, dttz1, lhs.f, i, square.f, tzetar, rhs_norm, grid_points.f, zzcon2, _imopVarPre2172, dz2tz1, xxcon2, sqrt, _imopVarPre1349, q, c2iv, _imopVarPre835, comz6, ninvr, rhon.f, zzcon3, lhs, cuf.f, timer_read, lhsinit, xxcon3, verify, j, txinvr, _imopVarPre2091, i, rhs.f, _imopVarPre721, printf, dtty1, _imopVarPre2180, ws, c1c5, dy4ty1, dx1tx1, m, rhs, dymax, square, dttz2, c2dttx1, grid_points, yycon5, _imopVarPre1987, x_solve, _imopVarPre1975, ws.f, j, dz1tz1, dttx1, rhos.f, dxmax, dx5tx1, _imopVarPre2185, yycon4, dz1, vs, timer_clear, xcr.f, i, dtty2, dy5ty1, fabs, rhos, c2dtty1, yycon3, vs.f, _imopVarPre1985, _imopVarPre2184, dz5tz1, _imopVarPre2083])
#pragma omp barrier
        }
        for (m = 3; m < 5; m++) {
            n = (m - 3 + 1) * 5;
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (j = 1; j <= grid_points[1] - 2; j++) {
                    rhs[m][i][j][k] = rhs[m][i][j][k] - lhs[n + 3][i][j][k] * rhs[m][i][j][k1];
                }
            }
// #pragma omp dummyFlush BARRIER_START written([rhs.f]) read([dy3ty1, lhsx, yycon2, us, dy1, timer_start, dttx2, dzmax, dx2tx1, tz2, _imopVarPre2186, dnzm1, us.f, yycon1, c1c2, i, _imopVarPre731, i, xce.f, timer_stop, lhsz, dx1, dz5, dy3, add, ue, dt, i, dnym1, _imopVarPre1977, ty2, lhsy, y_solve, dz4tz1, dz4, _imopVarPre1463, m, ue.f, dy2ty1, ainv, c1, comz1, _imopVarPre2160, rhon, compute_rhs, _imopVarPre729, zzcon4, i, j, forcing.f, dy5, c2, _imopVarPre825, _imopVarPre2161, ainv.f, con43, zzcon5, tx2, z_solve, xxcon1, i, rho_i.f, _imopVarPre1453, dx2, forcing, dx3tx1, u.f, i, rho_i, i, _imopVarPre719, dnxm1, buf, ce, buf.f, _imopVarPre2168, i, _imopVarPre2178, dx5, _imopVarPre1357, j, cv, u, bt, dz3tz1, _imopVarPre827, initialize, ce.f, dssp, _imopVarPre1455, _imopVarPre172, cv.f, verified, k, comz5, rhoq.f, _imopVarPre2174, qs, cuf, _imopVarPre2166, xxcon4, speed, pinvr, exact_rhs, _imopVarPre1359, _imopVarPre1347, adi, comz4, exact_solution, zzcon1, rhoq, _imopVarPre2173, c2dttz1, error_norm, qs.f, dy1ty1, _imopVarPre2167, xxcon5, _imopVarPre2081, c3c4, _imopVarPre2179, speed.f, q.f, dx4tx1, _imopVarPre2162, dttz1, lhs.f, i, square.f, tzetar, rhs_norm, grid_points.f, zzcon2, _imopVarPre2172, dz2tz1, xxcon2, sqrt, _imopVarPre1349, q, c2iv, _imopVarPre835, comz6, ninvr, rhon.f, zzcon3, lhs, cuf.f, timer_read, lhsinit, xxcon3, verify, j, txinvr, _imopVarPre2091, i, rhs.f, _imopVarPre721, printf, dtty1, _imopVarPre2180, ws, c1c5, dy4ty1, dx1tx1, m, rhs, dymax, square, dttz2, c2dttx1, grid_points, yycon5, _imopVarPre1987, x_solve, _imopVarPre1975, ws.f, j, dz1tz1, dttx1, rhos.f, dxmax, dx5tx1, _imopVarPre2185, yycon4, dz1, vs, timer_clear, xcr.f, i, dtty2, dy5ty1, fabs, rhos, c2dtty1, yycon3, vs.f, _imopVarPre1985, _imopVarPre2184, dz5tz1, _imopVarPre2083])
#pragma omp barrier
        }
        n = 0;
        for (m = 0; m < 3; m++) {
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (j = 1; j <= grid_points[1] - 2; j++) {
                    for (k = grid_points[2] - 3; k >= 0; k--) {
                        k1 = k + 1;
                        k2 = k + 2;
                        rhs[m][i][j][k] = rhs[m][i][j][k] - lhs[n + 3][i][j][k] * rhs[m][i][j][k1] - lhs[n + 4][i][j][k] * rhs[m][i][j][k2];
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written([rhs.f]) read([dy3ty1, lhsx, yycon2, us, dy1, timer_start, dttx2, dzmax, dx2tx1, tz2, _imopVarPre2186, dnzm1, us.f, yycon1, c1c2, i, _imopVarPre731, i, xce.f, timer_stop, lhsz, dx1, dz5, dy3, add, ue, dt, i, dnym1, _imopVarPre1977, ty2, lhsy, y_solve, dz4tz1, dz4, _imopVarPre1463, m, ue.f, dy2ty1, ainv, c1, comz1, _imopVarPre2160, rhon, compute_rhs, _imopVarPre729, zzcon4, i, j, forcing.f, dy5, c2, _imopVarPre825, _imopVarPre2161, ainv.f, con43, zzcon5, tx2, z_solve, xxcon1, i, rho_i.f, _imopVarPre1453, dx2, forcing, dx3tx1, u.f, i, rho_i, i, _imopVarPre719, dnxm1, buf, ce, buf.f, _imopVarPre2168, i, _imopVarPre2178, dx5, _imopVarPre1357, j, cv, u, bt, dz3tz1, _imopVarPre827, initialize, ce.f, dssp, _imopVarPre1455, _imopVarPre172, cv.f, verified, k, comz5, rhoq.f, _imopVarPre2174, qs, cuf, _imopVarPre2166, xxcon4, speed, pinvr, exact_rhs, _imopVarPre1359, _imopVarPre1347, adi, comz4, exact_solution, zzcon1, rhoq, _imopVarPre2173, c2dttz1, error_norm, qs.f, dy1ty1, _imopVarPre2167, xxcon5, _imopVarPre2081, c3c4, _imopVarPre2179, speed.f, q.f, dx4tx1, _imopVarPre2162, dttz1, lhs.f, i, square.f, tzetar, rhs_norm, grid_points.f, zzcon2, _imopVarPre2172, dz2tz1, xxcon2, sqrt, _imopVarPre1349, q, c2iv, _imopVarPre835, comz6, ninvr, rhon.f, zzcon3, lhs, cuf.f, timer_read, lhsinit, xxcon3, verify, j, txinvr, _imopVarPre2091, i, rhs.f, _imopVarPre721, printf, dtty1, _imopVarPre2180, ws, c1c5, dy4ty1, dx1tx1, m, rhs, dymax, square, dttz2, c2dttx1, grid_points, yycon5, _imopVarPre1987, x_solve, _imopVarPre1975, ws.f, j, dz1tz1, dttx1, rhos.f, dxmax, dx5tx1, _imopVarPre2185, yycon4, dz1, vs, timer_clear, xcr.f, i, dtty2, dy5ty1, fabs, rhos, c2dtty1, yycon3, vs.f, _imopVarPre1985, _imopVarPre2184, dz5tz1, _imopVarPre2083])
#pragma omp barrier
        }
        for (m = 3; m < 5; m++) {
            n = (m - 3 + 1) * 5;
#pragma omp for nowait
            for (i = 1; i <= grid_points[0] - 2; i++) {
                for (j = 1; j <= grid_points[1] - 2; j++) {
                    for (k = grid_points[2] - 3; k >= 0; k--) {
                        k1 = k + 1;
                        k2 = k + 2;
                        rhs[m][i][j][k] = rhs[m][i][j][k] - lhs[n + 3][i][j][k] * rhs[m][i][j][k1] - lhs[n + 4][i][j][k] * rhs[m][i][j][k2];
                    }
                }
            }
// #pragma omp dummyFlush BARRIER_START written([rhs.f]) read([dy3ty1, lhsx, yycon2, us, dy1, timer_start, dttx2, dzmax, dx2tx1, tz2, _imopVarPre2186, dnzm1, us.f, yycon1, c1c2, i, _imopVarPre731, i, xce.f, timer_stop, lhsz, dx1, dz5, dy3, add, ue, dt, i, dnym1, _imopVarPre1977, ty2, lhsy, y_solve, dz4tz1, dz4, _imopVarPre1463, m, ue.f, dy2ty1, ainv, c1, comz1, _imopVarPre2160, rhon, compute_rhs, _imopVarPre729, zzcon4, i, j, forcing.f, dy5, c2, _imopVarPre825, _imopVarPre2161, ainv.f, con43, zzcon5, tx2, z_solve, xxcon1, i, rho_i.f, _imopVarPre1453, dx2, forcing, dx3tx1, u.f, i, rho_i, i, _imopVarPre719, dnxm1, buf, ce, buf.f, _imopVarPre2168, i, _imopVarPre2178, dx5, _imopVarPre1357, j, cv, u, bt, dz3tz1, _imopVarPre827, initialize, ce.f, dssp, _imopVarPre1455, _imopVarPre172, cv.f, verified, k, comz5, rhoq.f, _imopVarPre2174, qs, cuf, _imopVarPre2166, xxcon4, speed, pinvr, exact_rhs, _imopVarPre1359, _imopVarPre1347, adi, comz4, exact_solution, zzcon1, rhoq, _imopVarPre2173, c2dttz1, error_norm, qs.f, dy1ty1, _imopVarPre2167, xxcon5, _imopVarPre2081, c3c4, _imopVarPre2179, speed.f, q.f, dx4tx1, _imopVarPre2162, dttz1, lhs.f, i, square.f, tzetar, rhs_norm, grid_points.f, zzcon2, _imopVarPre2172, dz2tz1, xxcon2, sqrt, _imopVarPre1349, q, c2iv, _imopVarPre835, comz6, ninvr, rhon.f, zzcon3, lhs, cuf.f, timer_read, lhsinit, xxcon3, verify, j, txinvr, _imopVarPre2091, i, rhs.f, _imopVarPre721, printf, dtty1, _imopVarPre2180, ws, c1c5, dy4ty1, dx1tx1, m, rhs, dymax, square, dttz2, c2dttx1, grid_points, yycon5, _imopVarPre1987, x_solve, _imopVarPre1975, ws.f, j, dz1tz1, dttx1, rhos.f, dxmax, dx5tx1, _imopVarPre2185, yycon4, dz1, vs, timer_clear, xcr.f, i, dtty2, dy5ty1, fabs, rhos, c2dtty1, yycon3, vs.f, _imopVarPre1985, _imopVarPre2184, dz5tz1, _imopVarPre2083])
#pragma omp barrier
        }
    }
    tzetar();
}
