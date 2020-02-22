
/*[]*/
typedef signed char __int8_t;
/*[]*/
typedef unsigned char __uint8_t;
/*[]*/
typedef short __int16_t;
/*[]*/
typedef unsigned short __uint16_t;
/*[]*/
typedef int __int32_t;
/*[]*/
typedef unsigned int __uint32_t;
/*[]*/
typedef long long __int64_t;
/*[]*/
typedef unsigned long long __uint64_t;
/*[]*/
typedef long __darwin_intptr_t;
/*[]*/
typedef unsigned int __darwin_natural_t;
/*[]*/
typedef int __darwin_ct_rune_t;
/*[]*/
union stUn_imopVarPre0 {
    char __mbstate8[128];
    long long _mbstateL;
} ;
/*[]*/
typedef union stUn_imopVarPre0 __mbstate_t;
/*[]*/
typedef __mbstate_t __darwin_mbstate_t;
/*[]*/
typedef long int __darwin_ptrdiff_t;
/*[]*/
typedef long unsigned int __darwin_size_t;
/*[]*/
typedef __builtin_va_list __darwin_va_list;
/*[]*/
typedef int __darwin_wchar_t;
/*[]*/
typedef __darwin_wchar_t __darwin_rune_t;
/*[]*/
typedef int __darwin_wint_t;
/*[]*/
typedef unsigned long __darwin_clock_t;
/*[]*/
typedef __uint32_t __darwin_socklen_t;
/*[]*/
typedef long __darwin_ssize_t;
/*[]*/
typedef long __darwin_time_t;
/*[]*/
typedef __int64_t __darwin_blkcnt_t;
/*[]*/
typedef __int32_t __darwin_blksize_t;
/*[]*/
typedef __int32_t __darwin_dev_t;
/*[]*/
typedef unsigned int __darwin_fsblkcnt_t;
/*[]*/
typedef unsigned int __darwin_fsfilcnt_t;
/*[]*/
typedef __uint32_t __darwin_gid_t;
/*[]*/
typedef __uint32_t __darwin_id_t;
/*[]*/
typedef __uint64_t __darwin_ino64_t;
/*[]*/
typedef __darwin_ino64_t __darwin_ino_t;
/*[]*/
typedef __darwin_natural_t __darwin_mach_port_name_t;
/*[]*/
typedef __darwin_mach_port_name_t __darwin_mach_port_t;
/*[]*/
typedef __uint16_t __darwin_mode_t;
/*[]*/
typedef __int64_t __darwin_off_t;
/*[]*/
typedef __int32_t __darwin_pid_t;
/*[]*/
typedef __uint32_t __darwin_sigset_t;
/*[]*/
typedef __int32_t __darwin_suseconds_t;
/*[]*/
typedef __uint32_t __darwin_uid_t;
/*[]*/
typedef __uint32_t __darwin_useconds_t;
/*[]*/
typedef unsigned char __darwin_uuid_t[16];
/*[]*/
typedef char __darwin_uuid_string_t[37];
/*[]*/
struct __darwin_pthread_handler_rec {
    void ( *__routine )(void *);
    void *__arg;
    struct __darwin_pthread_handler_rec *__next;
} ;
/*[]*/
struct _opaque_pthread_attr_t {
    long __sig;
    char __opaque[56];
} ;
/*[]*/
struct _opaque_pthread_cond_t {
    long __sig;
    char __opaque[40];
} ;
/*[]*/
struct _opaque_pthread_condattr_t {
    long __sig;
    char __opaque[8];
} ;
/*[]*/
struct _opaque_pthread_mutex_t {
    long __sig;
    char __opaque[56];
} ;
/*[]*/
struct _opaque_pthread_mutexattr_t {
    long __sig;
    char __opaque[8];
} ;
/*[]*/
struct _opaque_pthread_once_t {
    long __sig;
    char __opaque[8];
} ;
/*[]*/
struct _opaque_pthread_rwlock_t {
    long __sig;
    char __opaque[192];
} ;
/*[]*/
struct _opaque_pthread_rwlockattr_t {
    long __sig;
    char __opaque[16];
} ;
/*[]*/
struct _opaque_pthread_t {
    long __sig;
    struct __darwin_pthread_handler_rec *__cleanup_stack;
    char __opaque[8176];
} ;
/*[]*/
typedef struct _opaque_pthread_attr_t __darwin_pthread_attr_t;
/*[]*/
typedef struct _opaque_pthread_cond_t __darwin_pthread_cond_t;
/*[]*/
typedef struct _opaque_pthread_condattr_t __darwin_pthread_condattr_t;
/*[]*/
typedef unsigned long __darwin_pthread_key_t;
/*[]*/
typedef struct _opaque_pthread_mutex_t __darwin_pthread_mutex_t;
/*[]*/
typedef struct _opaque_pthread_mutexattr_t __darwin_pthread_mutexattr_t;
/*[]*/
typedef struct _opaque_pthread_once_t __darwin_pthread_once_t;
/*[]*/
typedef struct _opaque_pthread_rwlock_t __darwin_pthread_rwlock_t;
/*[]*/
typedef struct _opaque_pthread_rwlockattr_t __darwin_pthread_rwlockattr_t;
/*[]*/
typedef struct _opaque_pthread_t *__darwin_pthread_t;
/*[]*/
typedef int __darwin_nl_item;
/*[]*/
typedef int __darwin_wctrans_t;
/*[]*/
typedef __uint32_t __darwin_wctype_t;
/*[]*/
enum enum_imopVarPre1 {
    P_ALL, P_PID , P_PGID
} ;
/*[]*/
typedef enum enum_imopVarPre1 idtype_t;
/*[]*/
typedef __darwin_pid_t pid_t;
/*[]*/
typedef __darwin_id_t id_t;
/*[]*/
typedef int sig_atomic_t;
/*[]*/
typedef signed char int8_t;
/*[]*/
typedef short int16_t;
/*[]*/
typedef int int32_t;
/*[]*/
typedef long long int64_t;
/*[]*/
typedef unsigned char u_int8_t;
/*[]*/
typedef unsigned short u_int16_t;
/*[]*/
typedef unsigned int u_int32_t;
/*[]*/
typedef unsigned long long u_int64_t;
/*[]*/
typedef int64_t register_t;
/*[]*/
typedef __darwin_intptr_t intptr_t;
/*[]*/
typedef unsigned long uintptr_t;
/*[]*/
typedef u_int64_t user_addr_t;
/*[]*/
typedef u_int64_t user_size_t;
/*[]*/
typedef int64_t user_ssize_t;
/*[]*/
typedef int64_t user_long_t;
/*[]*/
typedef u_int64_t user_ulong_t;
/*[]*/
typedef int64_t user_time_t;
/*[]*/
typedef int64_t user_off_t;
/*[]*/
typedef u_int64_t syscall_arg_t;
/*[]*/
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
/*[]*/
struct __darwin_fp_control {
    unsigned short __invalid: 1, __denorm: 1 , __zdiv: 1 , __ovrfl: 1 , __undfl: 1 , __precis: 1 , :2 , __pc: 2 , __rc: 2 , :1 , :3;
} ;
/*[]*/
typedef struct __darwin_fp_control __darwin_fp_control_t;
/*[]*/
struct __darwin_fp_status {
    unsigned short __invalid: 1, __denorm: 1 , __zdiv: 1 , __ovrfl: 1 , __undfl: 1 , __precis: 1 , __stkflt: 1 , __errsumm: 1 , __c0: 1 , __c1: 1 , __c2: 1 , __tos: 3 , __c3: 1 , __busy: 1;
} ;
/*[]*/
typedef struct __darwin_fp_status __darwin_fp_status_t;
/*[]*/
struct __darwin_mmst_reg {
    char __mmst_reg[10];
    char __mmst_rsrv[6];
} ;
/*[]*/
struct __darwin_xmm_reg {
    char __xmm_reg[16];
} ;
/*[]*/
struct __darwin_ymm_reg {
    char __ymm_reg[32];
} ;
/*[]*/
struct __darwin_zmm_reg {
    char __zmm_reg[64];
} ;
/*[]*/
struct __darwin_opmask_reg {
    char __opmask_reg[8];
} ;
/*[]*/
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
/*[]*/
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
/*[]*/
struct __darwin_i386_avx512_state {
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
    struct __darwin_opmask_reg __fpu_k0;
    struct __darwin_opmask_reg __fpu_k1;
    struct __darwin_opmask_reg __fpu_k2;
    struct __darwin_opmask_reg __fpu_k3;
    struct __darwin_opmask_reg __fpu_k4;
    struct __darwin_opmask_reg __fpu_k5;
    struct __darwin_opmask_reg __fpu_k6;
    struct __darwin_opmask_reg __fpu_k7;
    struct __darwin_ymm_reg __fpu_zmmh0;
    struct __darwin_ymm_reg __fpu_zmmh1;
    struct __darwin_ymm_reg __fpu_zmmh2;
    struct __darwin_ymm_reg __fpu_zmmh3;
    struct __darwin_ymm_reg __fpu_zmmh4;
    struct __darwin_ymm_reg __fpu_zmmh5;
    struct __darwin_ymm_reg __fpu_zmmh6;
    struct __darwin_ymm_reg __fpu_zmmh7;
} ;
/*[]*/
struct __darwin_i386_exception_state {
    __uint16_t __trapno;
    __uint16_t __cpu;
    __uint32_t __err;
    __uint32_t __faultvaddr;
} ;
/*[]*/
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
/*[]*/
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
/*[]*/
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
/*[]*/
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
/*[]*/
struct __darwin_x86_avx512_state64 {
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
    struct __darwin_opmask_reg __fpu_k0;
    struct __darwin_opmask_reg __fpu_k1;
    struct __darwin_opmask_reg __fpu_k2;
    struct __darwin_opmask_reg __fpu_k3;
    struct __darwin_opmask_reg __fpu_k4;
    struct __darwin_opmask_reg __fpu_k5;
    struct __darwin_opmask_reg __fpu_k6;
    struct __darwin_opmask_reg __fpu_k7;
    struct __darwin_ymm_reg __fpu_zmmh0;
    struct __darwin_ymm_reg __fpu_zmmh1;
    struct __darwin_ymm_reg __fpu_zmmh2;
    struct __darwin_ymm_reg __fpu_zmmh3;
    struct __darwin_ymm_reg __fpu_zmmh4;
    struct __darwin_ymm_reg __fpu_zmmh5;
    struct __darwin_ymm_reg __fpu_zmmh6;
    struct __darwin_ymm_reg __fpu_zmmh7;
    struct __darwin_ymm_reg __fpu_zmmh8;
    struct __darwin_ymm_reg __fpu_zmmh9;
    struct __darwin_ymm_reg __fpu_zmmh10;
    struct __darwin_ymm_reg __fpu_zmmh11;
    struct __darwin_ymm_reg __fpu_zmmh12;
    struct __darwin_ymm_reg __fpu_zmmh13;
    struct __darwin_ymm_reg __fpu_zmmh14;
    struct __darwin_ymm_reg __fpu_zmmh15;
    struct __darwin_zmm_reg __fpu_zmm16;
    struct __darwin_zmm_reg __fpu_zmm17;
    struct __darwin_zmm_reg __fpu_zmm18;
    struct __darwin_zmm_reg __fpu_zmm19;
    struct __darwin_zmm_reg __fpu_zmm20;
    struct __darwin_zmm_reg __fpu_zmm21;
    struct __darwin_zmm_reg __fpu_zmm22;
    struct __darwin_zmm_reg __fpu_zmm23;
    struct __darwin_zmm_reg __fpu_zmm24;
    struct __darwin_zmm_reg __fpu_zmm25;
    struct __darwin_zmm_reg __fpu_zmm26;
    struct __darwin_zmm_reg __fpu_zmm27;
    struct __darwin_zmm_reg __fpu_zmm28;
    struct __darwin_zmm_reg __fpu_zmm29;
    struct __darwin_zmm_reg __fpu_zmm30;
    struct __darwin_zmm_reg __fpu_zmm31;
} ;
/*[]*/
struct __darwin_x86_exception_state64 {
    __uint16_t __trapno;
    __uint16_t __cpu;
    __uint32_t __err;
    __uint64_t __faultvaddr;
} ;
/*[]*/
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
/*[]*/
struct __darwin_x86_cpmu_state64 {
    __uint64_t __ctrs[16];
} ;
/*[]*/
struct __darwin_mcontext32 {
    struct __darwin_i386_exception_state __es;
    struct __darwin_i386_thread_state __ss;
    struct __darwin_i386_float_state __fs;
} ;
/*[]*/
struct __darwin_mcontext_avx32 {
    struct __darwin_i386_exception_state __es;
    struct __darwin_i386_thread_state __ss;
    struct __darwin_i386_avx_state __fs;
} ;
/*[]*/
struct __darwin_mcontext_avx512_32 {
    struct __darwin_i386_exception_state __es;
    struct __darwin_i386_thread_state __ss;
    struct __darwin_i386_avx512_state __fs;
} ;
/*[]*/
struct __darwin_mcontext64 {
    struct __darwin_x86_exception_state64 __es;
    struct __darwin_x86_thread_state64 __ss;
    struct __darwin_x86_float_state64 __fs;
} ;
/*[]*/
struct __darwin_mcontext_avx64 {
    struct __darwin_x86_exception_state64 __es;
    struct __darwin_x86_thread_state64 __ss;
    struct __darwin_x86_avx_state64 __fs;
} ;
/*[]*/
struct __darwin_mcontext_avx512_64 {
    struct __darwin_x86_exception_state64 __es;
    struct __darwin_x86_thread_state64 __ss;
    struct __darwin_x86_avx512_state64 __fs;
} ;
/*[]*/
typedef struct __darwin_mcontext64 *mcontext_t;
/*[]*/
typedef __darwin_pthread_attr_t pthread_attr_t;
/*[]*/
struct __darwin_sigaltstack {
    void *ss_sp;
    __darwin_size_t ss_size;
    int ss_flags;
} ;
/*[]*/
typedef struct __darwin_sigaltstack stack_t;
/*[]*/
struct __darwin_ucontext {
    int uc_onstack;
    __darwin_sigset_t uc_sigmask;
    struct __darwin_sigaltstack uc_stack;
    struct __darwin_ucontext *uc_link;
    __darwin_size_t uc_mcsize;
    struct __darwin_mcontext64 *uc_mcontext;
} ;
/*[]*/
typedef struct __darwin_ucontext ucontext_t;
/*[]*/
typedef __darwin_sigset_t sigset_t;
/*[]*/
typedef __darwin_size_t size_t;
/*[]*/
typedef __darwin_uid_t uid_t;
/*[]*/
union sigval {
    int sival_int;
    void *sival_ptr;
} ;
/*[]*/
struct sigevent {
    int sigev_notify;
    int sigev_signo;
    union sigval sigev_value;
    void ( *sigev_notify_function )(union sigval );
    pthread_attr_t *sigev_notify_attributes;
} ;
/*[]*/
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
/*[]*/
typedef struct __siginfo siginfo_t;
/*[]*/
union __sigaction_u {
    void ( *__sa_handler )(int );
    void ( *__sa_sigaction )(int , struct __siginfo * , void *);
} ;
/*[]*/
struct __sigaction {
    union __sigaction_u __sigaction_u;
    void ( *sa_tramp )(void *, int  , int  , siginfo_t * , void *);
    sigset_t sa_mask;
    int sa_flags;
} ;
/*[]*/
struct sigaction {
    union __sigaction_u __sigaction_u;
    sigset_t sa_mask;
    int sa_flags;
} ;
/*[]*/
typedef void ( *sig_t )(int );
/*[]*/
struct sigvec {
    void ( *sv_handler )(int );
    int sv_mask;
    int sv_flags;
} ;
/*[]*/
struct sigstack {
    char *ss_sp;
    int ss_onstack;
} ;
/*[]*/
typedef unsigned char uint8_t;
/*[]*/
typedef unsigned short uint16_t;
/*[]*/
typedef unsigned int uint32_t;
/*[]*/
typedef unsigned long long uint64_t;
/*[]*/
typedef int8_t int_least8_t;
/*[]*/
typedef int16_t int_least16_t;
/*[]*/
typedef int32_t int_least32_t;
/*[]*/
typedef int64_t int_least64_t;
/*[]*/
typedef uint8_t uint_least8_t;
/*[]*/
typedef uint16_t uint_least16_t;
/*[]*/
typedef uint32_t uint_least32_t;
/*[]*/
typedef uint64_t uint_least64_t;
/*[]*/
typedef int8_t int_fast8_t;
/*[]*/
typedef int16_t int_fast16_t;
/*[]*/
typedef int32_t int_fast32_t;
/*[]*/
typedef int64_t int_fast64_t;
/*[]*/
typedef uint8_t uint_fast8_t;
/*[]*/
typedef uint16_t uint_fast16_t;
/*[]*/
typedef uint32_t uint_fast32_t;
/*[]*/
typedef uint64_t uint_fast64_t;
/*[]*/
typedef long int intmax_t;
/*[]*/
typedef long unsigned int uintmax_t;
/*[]*/
struct timeval {
    __darwin_time_t tv_sec;
    __darwin_suseconds_t tv_usec;
} ;
/*[]*/
typedef __uint64_t rlim_t;
/*[]*/
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
/*[]*/
typedef void *rusage_info_t;
/*[]*/
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
/*[]*/
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
/*[]*/
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
/*[]*/
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
/*[]*/
struct rusage_info_v4 {
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
    uint64_t ri_logical_writes;
    uint64_t ri_lifetime_max_phys_footprint;
    uint64_t ri_instructions;
    uint64_t ri_cycles;
    uint64_t ri_billed_energy;
    uint64_t ri_serviced_energy;
    uint64_t ri_unused[2];
} ;
/*[]*/
typedef struct rusage_info_v4 rusage_info_current;
/*[]*/
struct rlimit {
    rlim_t rlim_cur;
    rlim_t rlim_max;
} ;
/*[]*/
struct proc_rlimit_control_wakeupmon {
    uint32_t wm_flags;
    int32_t wm_rate;
} ;
/*[]*/
union wait {
    int w_status;
    struct stUn_imopVarPre2 {
        unsigned int w_Termsig: 7, w_Coredump: 1 , w_Retcode: 8 , w_Filler: 16;
    } w_T;
    struct stUn_imopVarPre3 {
        unsigned int w_Stopval: 8, w_Stopsig: 8 , w_Filler: 16;
    } w_S;
} ;
/*[]*/
typedef __darwin_ct_rune_t ct_rune_t;
/*[]*/
typedef __darwin_rune_t rune_t;
/*[]*/
typedef __darwin_wchar_t wchar_t;
/*[]*/
struct stUn_imopVarPre4 {
    int quot;
    int rem;
} ;
/*[]*/
typedef struct stUn_imopVarPre4 div_t;
/*[]*/
struct stUn_imopVarPre5 {
    long quot;
    long rem;
} ;
/*[]*/
typedef struct stUn_imopVarPre5 ldiv_t;
/*[]*/
struct stUn_imopVarPre6 {
    long long quot;
    long long rem;
} ;
/*[]*/
typedef struct stUn_imopVarPre6 lldiv_t;
/*[]*/
void *malloc(size_t __size);
/*[]*/
typedef __darwin_dev_t dev_t;
/*[]*/
typedef __darwin_mode_t mode_t;
/*[]*/
typedef __darwin_va_list va_list;
/*[]*/
typedef __darwin_off_t fpos_t;
/*[]*/
struct __sbuf {
    unsigned char *_base;
    int _size;
} ;
/*[]*/
struct __sFILEX ;
/*[]*/
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
/*[]*/
typedef struct __sFILE FILE;
/*[]*/
int printf(const char *restrict , ...);
/*[]*/
typedef __darwin_off_t off_t;
/*[]*/
typedef __darwin_ssize_t ssize_t;
/*[]*/
typedef float float_t;
/*[]*/
typedef double double_t;
/*[]*/
extern double fabs(double );
/*[]*/
struct __float2 {
    float __sinval;
    float __cosval;
} ;
/*[]*/
struct __double2 {
    double __sinval;
    double __cosval;
} ;
/*[]*/
struct exception {
    int type;
    char *name;
    double arg1;
    double arg2;
    double retval;
} ;
/*[]*/
struct stUn_imopVarPre11 {
    unsigned char _x[64];
} ;
/*[]*/
typedef struct stUn_imopVarPre11 omp_lock_t;
/*[]*/
struct stUn_imopVarPre12 {
    unsigned char _x[80];
} ;
/*[]*/
typedef struct stUn_imopVarPre12 omp_nest_lock_t;
/*[]*/
enum omp_sched_t {
    omp_sched_static = 1, omp_sched_dynamic = 2 , omp_sched_guided = 3 , omp_sched_auto = 4
} ;
/*[]*/
typedef enum omp_sched_t omp_sched_t;
/*[]*/
enum omp_proc_bind_t {
    omp_proc_bind_false = 0, omp_proc_bind_true = 1 , omp_proc_bind_master = 2 , omp_proc_bind_close = 3 , omp_proc_bind_spread = 4
} ;
/*[]*/
typedef enum omp_proc_bind_t omp_proc_bind_t;
/*[]*/
enum omp_lock_hint_t {
    omp_lock_hint_none = 0, omp_lock_hint_uncontended = 1 , omp_lock_hint_contended = 2 , omp_lock_hint_nonspeculative = 4 , omp_lock_hint_speculative = 8
} ;
/*[]*/
typedef enum omp_lock_hint_t omp_lock_hint_t;
/*[]*/
extern int omp_get_max_threads(void );
/*[]*/
extern int omp_get_num_procs(void );
/*[]*/
extern double omp_get_wtime(void );
/*[]*/
int main(int argc, char *argv[]);
/*[]*/
/*[]*/
/*[]*/
int main(int argc, char *argv[]) {
/*[]*/
    /*[]*/
    double **u_imopVarPre89;
    /*[]*/
    double **u_imopVarPre12;
    /*[]*/
    double diff_imopVarPre11;
    /*[]*/
    double **w_imopVarPre10;
    /*[]*/
    double **q_imopVarPre9;
    /*[]*/
    double diff_imopVarPre7;
    /*[]*/
    double **q_imopVarPre6;
    /*[]*/
    double diff_imopVarPre2;
    /*[]*/
    double diff;
    /*[]*/
    double mean;
    /*[]*/
    double **u;
    /*[]*/
    double **w;
    /*[]*/
    double **q;
    /*[]*/
#pragma omp parallel shared(w, mean, u, diff)
    {
    /*[1; ]*/
        /*[1; ]*/
        double wtime;
        /*[1; ]*/
        double epsilon = 0.001;
        /*[1; ]*/
        int i;
        /*[1; ]*/
        int iterations;
        /*[1; ]*/
        int iterations_print;
        /*[1; ]*/
        int j;
        /*[1; ]*/
        double my_diff;
        /*[1; ]*/
        double _imopVarPre173;
        /*[1; ]*/
        unsigned long int _imopVarPre147;
        /*[1; ]*/
        void *_imopVarPre148;
        /*[1; ]*/
#pragma omp master
        {
        /*[1; ]*/
            /*[1; ]*/
            _imopVarPre147 = 500 * sizeof(double *);
            /*[1; ]*/
            _imopVarPre148 = malloc(_imopVarPre147);
            /*[1; ]*/
            /*[1; ]*/
            u = (double **) _imopVarPre148;
        }
        /*[1; ]*/
        unsigned long int _imopVarPre151;
        /*[1; ]*/
        void *_imopVarPre152;
        /*[1; ]*/
#pragma omp master
        {
        /*[1; ]*/
            /*[1; ]*/
            _imopVarPre151 = 500 * sizeof(double *);
            /*[1; ]*/
            _imopVarPre152 = malloc(_imopVarPre151);
            /*[1; ]*/
            /*[1; ]*/
            w = (double **) _imopVarPre152;
        }
        /*[1; ]*/
        int p;
        /*[1; ]*/
#pragma omp master
        {
        /*[1; ]*/
            /*[1; ]*/
            /*[1; ]*/
            /*[1; ]*/
            /*[1; ]*/
            for (p = 0; p < 500; p++) {
            /*[1; ]*/
                /*[1; ]*/
                unsigned long int _imopVarPre155;
                /*[1; ]*/
                void *_imopVarPre156;
                /*[1; ]*/
                _imopVarPre155 = 500 * sizeof(double);
                /*[1; ]*/
                _imopVarPre156 = malloc(_imopVarPre155);
                /*[1; ]*/
                /*[1; ]*/
                u[p] = (double *) _imopVarPre156;
                /*[1; ]*/
                unsigned long int _imopVarPre159;
                /*[1; ]*/
                void *_imopVarPre160;
                /*[1; ]*/
                _imopVarPre159 = 500 * sizeof(double);
                /*[1; ]*/
                _imopVarPre160 = malloc(_imopVarPre159);
                /*[1; ]*/
                /*[1; ]*/
                w[p] = (double *) _imopVarPre160;
            }
            /*[1; ]*/
            printf("\n");
            /*[1; ]*/
            /*[1; ]*/
            printf("HEATED_PLATE_OPENMP\n");
            /*[1; ]*/
            /*[1; ]*/
            printf("  C/OpenMP version\n");
            /*[1; ]*/
            /*[1; ]*/
            printf("  A program to solve for the steady state temperature distribution\n");
            /*[1; ]*/
            /*[1; ]*/
            printf("  over a rectangular plate.\n");
            /*[1; ]*/
            /*[1; ]*/
            printf("\n");
            /*[1; ]*/
            /*[1; ]*/
            printf("  Spatial grid of %d by %d points.\n", 500, 500);
            /*[1; ]*/
            /*[1; ]*/
            printf("  The iteration will be repeated until the change is <= %e\n", epsilon);
            /*[1; ]*/
        }
        /*[1; ]*/
        int _imopVarPre162;
        /*[1; ]*/
#pragma omp master
        {
        /*[1; ]*/
            /*[1; ]*/
            _imopVarPre162 = omp_get_num_procs();
            /*[1; ]*/
            /*[1; ]*/
            printf("  Number of processors available = %d\n", _imopVarPre162);
            /*[1; ]*/
        }
        /*[1; ]*/
        int _imopVarPre164;
        /*[1; ]*/
#pragma omp master
        {
        /*[1; ]*/
            /*[1; ]*/
            _imopVarPre164 = omp_get_max_threads();
            /*[1; ]*/
            /*[1; ]*/
            printf("  Number of threads =              %d\n", _imopVarPre164);
            /*[1; ]*/
            /*[1; ]*/
            mean = 0.0;
        }
        /*[1; ]*/
#pragma omp master
        {
        /*[1; ]*/
            /*[1; ]*/
            iterations = 0;
            /*[1; ]*/
            iterations_print = 1;
            /*[1; ]*/
            printf("\n");
            /*[1; ]*/
            /*[1; ]*/
            printf(" Iteration  Change\n");
            /*[1; ]*/
            /*[1; ]*/
            printf("\n");
            /*[1; ]*/
            /*[1; ]*/
            wtime = omp_get_wtime();
            /*[1; ]*/
            /*[1; ]*/
            diff = epsilon;
        }
        /*[1; ]*/
        int whilePred_imopVarPre0;
        /*[1; ]*/
        // #pragma omp dummyFlush BARRIER_START written() read([heapCell#2, i, u, w, diff, heapCell#1])
        /*[1; ]*/
#pragma omp barrier
        /*[6; ]*/
#pragma omp for nowait
        /*[6; ]*/
        /*[6; ]*/
        /*[6; ]*/
        for (i = 1; i < 500 - 1; i++) {
        /*[6; ]*/
            /*[6; ]*/
            w[i][0] = 100.0;
            /*[6; ]*/
            u[i][0] = 100.0;
        }
        /*[6; ]*/
        whilePred_imopVarPre0 = epsilon <= diff;
        /*[6; ]*/
        // #pragma omp dummyFlush BARRIER_START written() read([heapCell#2, i, u, w, diff, heapCell#1])
        /*[6; ]*/
#pragma omp barrier
        /*[7; ]*/
#pragma omp for nowait
        /*[7; ]*/
        /*[7; ]*/
        /*[7; ]*/
        for (i = 1; i < 500 - 1; i++) {
        /*[7; ]*/
            /*[7; ]*/
            w[i][500 - 1] = 100.0;
            /*[7; ]*/
            u[i][500 - 1] = 100.0;
        }
        /*[7; ]*/
        // #pragma omp dummyFlush BARRIER_START written() read([heapCell#2, j, u, w, diff, heapCell#1])
        /*[7; ]*/
#pragma omp barrier
        /*[8; ]*/
#pragma omp for nowait
        /*[8; ]*/
        /*[8; ]*/
        /*[8; ]*/
        for (j = 0; j < 500; j++) {
        /*[8; ]*/
            /*[8; ]*/
            w[500 - 1][j] = 100.0;
            /*[8; ]*/
            u[500 - 1][j] = 100.0;
        }
        /*[8; ]*/
        // #pragma omp dummyFlush BARRIER_START written() read([heapCell#2, j, u, w, diff, heapCell#1])
        /*[8; ]*/
#pragma omp barrier
        /*[9; ]*/
#pragma omp for nowait
        /*[9; ]*/
        /*[9; ]*/
        /*[9; ]*/
        for (j = 0; j < 500; j++) {
        /*[9; ]*/
            /*[9; ]*/
            w[0][j] = 0.0;
            /*[9; ]*/
            u[0][j] = 0.0;
        }
        /*[9; ]*/
        // #pragma omp dummyFlush BARRIER_START written() read([heapCell#2, i, mean, w, heapCell#4, diff])
        /*[9; ]*/
#pragma omp barrier
        /*[10; ]*/
#pragma omp for reduction(+:mean) nowait
        /*[10; ]*/
        /*[10; ]*/
        /*[10; ]*/
        for (i = 1; i < 500 - 1; i++) {
        /*[10; ]*/
            /*[10; ]*/
            mean = mean + w[i][0] + w[i][500 - 1];
        }
        /*[10; ]*/
        // #pragma omp dummyFlush BARRIER_START written() read([heapCell#2, mean, j, w, heapCell#4, diff])
        /*[10; ]*/
#pragma omp barrier
        /*[11; ]*/
#pragma omp for reduction(+:mean) nowait
        /*[11; ]*/
        /*[11; ]*/
        /*[11; ]*/
        for (j = 0; j < 500; j++) {
        /*[11; ]*/
            /*[11; ]*/
            mean = mean + w[500 - 1][j] + w[0][j];
        }
        /*[11; ]*/
        // #pragma omp dummyFlush BARRIER_START written() read([printf, mean, diff])
        /*[11; ]*/
#pragma omp barrier
        /*[13; ]*/
#pragma omp master
        {
        /*[13; ]*/
            /*[13; ]*/
            mean = mean / (double) (2 * 500 + 2 * 500 - 4);
            /*[13; ]*/
            printf("\n");
            /*[13; ]*/
            /*[13; ]*/
            printf("  MEAN = %f\n", mean);
            /*[13; ]*/
        }
        /*[13; ]*/
        // #pragma omp dummyFlush BARRIER_START written() read([heapCell#2, i, mean, w, diff])
        /*[13; ]*/
#pragma omp barrier
        /*[14; ]*/
#pragma omp for nowait
        /*[14; ]*/
        /*[14; ]*/
        /*[14; ]*/
        for (i = 1; i < 500 - 1; i++) {
        /*[14; ]*/
            /*[14; ]*/
            /*[14; ]*/
            /*[14; ]*/
            /*[14; ]*/
            for (j = 1; j < 500 - 1; j++) {
            /*[14; ]*/
                /*[14; ]*/
                w[i][j] = mean;
            }
        }
        /*[14; ]*/
        // #pragma omp dummyFlush BARRIER_START written() read([printf, whilePred_imopVarPre0, u, w, omp_get_wtime, diff])
        /*[14; ]*/
#pragma omp barrier
        /*[17; ]*/
        /*[17; ]*/
        if (whilePred_imopVarPre0) {
        /*[17; ]*/
            /*[17; ]*/
#pragma omp single nowait
            {
            /*[17; ]*/
                /*[17; ]*/
                q = u;
                /*[17; ]*/
                u_imopVarPre89 = w;
            }
            /*[17; ]*/
            my_diff = 0.0;
            /*[17; ]*/
            ;
            /*[17; ]*/
#pragma omp master
            {
            /*[17; ]*/
                /*[17; ]*/
                diff_imopVarPre2 = 0.0;
            }
            /*[17; ]*/
            ;
            /*[17; ]*/
#pragma omp master
            {
            /*[17; ]*/
                /*[17; ]*/
                diff_imopVarPre7 = 0.0;
            }
            /*[17; ]*/
#pragma omp master
            {
            /*[17; ]*/
                /*[17; ]*/
                diff_imopVarPre11 = 0.0;
            }
            /*[17; ]*/
#pragma omp master
            {
            /*[17; ]*/
                /*[17; ]*/
                diff = 0.0;
            }
            /*[17; ]*/
#pragma omp for nowait
            /*[17; ]*/
            /*[17; ]*/
            /*[17; ]*/
            for (i = 1; i < 500 - 1; i++) {
            /*[17; ]*/
                /*[17; ]*/
                /*[17; ]*/
                /*[17; ]*/
                /*[17; ]*/
                for (j = 1; j < 500 - 1; j++) {
                /*[17; ]*/
                    /*[17; ]*/
                    u[i][j] = (w[i - 1][j] + w[i + 1][j] + w[i][j - 1] + w[i][j + 1]) / 4.0;
                }
            }
            /*[17; ]*/
#pragma omp single nowait
            {
            /*[17; ]*/
                /*[17; ]*/
                q_imopVarPre6 = u;
            }
            /*[17; ]*/
            // #pragma omp dummyFlush BARRIER_START written() read()
            /*[17; ]*/
#pragma omp barrier
            /*[129; ]*/
#pragma omp for nowait
            /*[129; ]*/
            /*[129; ]*/
            /*[129; ]*/
            for (i = 1; i < 500 - 1; i++) {
            /*[129; ]*/
                /*[129; ]*/
                /*[129; ]*/
                /*[129; ]*/
                /*[129; ]*/
                for (j = 1; j < 500 - 1; j++) {
                /*[129; ]*/
                    /*[129; ]*/
                    double _imopVarPre167;
                    /*[129; ]*/
                    double _imopVarPre168;
                    /*[129; ]*/
                    _imopVarPre167 = u[i][j] - w[i][j];
                    /*[129; ]*/
                    _imopVarPre168 = fabs(_imopVarPre167);
                    /*[129; ]*/
                    /*[129; ]*/
                    /*[129; ]*/
                    if (my_diff < _imopVarPre168) {
                    /*[129; ]*/
                        /*[129; ]*/
                        double _imopVarPre170;
                        /*[129; ]*/
                        double _imopVarPre171;
                        /*[129; ]*/
                        _imopVarPre170 = u[i][j] - w[i][j];
                        /*[129; ]*/
                        _imopVarPre171 = fabs(_imopVarPre170);
                        /*[129; ]*/
                        /*[129; ]*/
                        my_diff = _imopVarPre171;
                    }
                }
            }
            /*[129; ]*/
            // #pragma omp dummyFlush CRITICAL_START written([]) read([diff])
            /*[129; ]*/
#pragma omp critical
            {
            /*[129; ]*/
                /*[129; ]*/
                /*[129; ]*/
                if (diff < my_diff) {
                /*[129; ]*/
                    /*[129; ]*/
                    diff = my_diff;
                }
            }
            /*[129; ]*/
            // #pragma omp dummyFlush CRITICAL_END written([diff]) read([heapCell#2, heapCell#3, i, u, w, heapCell#1])
            /*[129; ]*/
            my_diff = 0.0;
            /*[129; ]*/
#pragma omp for nowait
            /*[129; ]*/
            /*[129; ]*/
            /*[129; ]*/
            for (i = 1; i < 500 - 1; i++) {
            /*[129; ]*/
                /*[129; ]*/
                /*[129; ]*/
                /*[129; ]*/
                /*[129; ]*/
                for (j = 1; j < 500 - 1; j++) {
                /*[129; ]*/
                    /*[129; ]*/
                    w[i][j] = (u[i - 1][j] + u[i + 1][j] + u[i][j - 1] + u[i][j + 1]) / 4.0;
                }
            }
            /*[129; ]*/
            // #pragma omp dummyFlush BARRIER_START written([heapCell#4]) read([fabs, heapCell#2, printf, i, heapCell#3, u, w, omp_get_wtime, heapCell#4, nullCell, diff, heapCell#1])
            /*[129; ]*/
#pragma omp barrier
            /*[157; 78; ]*/
            /*[157; ]*/
            while (1) {
            /*[157; 78; ]*/
                /*[157; 78; ]*/
#pragma omp master
                {
                /*[157; 78; ]*/
                    /*[157; 78; ]*/
                    iterations++;
                    /*[157; 78; ]*/
                    /*[157; 78; ]*/
                    if (iterations == iterations_print) {
                    /*[157; 78; ]*/
                        /*[157; 78; ]*/
                        printf("  %8d  %f\n", iterations, diff);
                        /*[157; 78; ]*/
                        /*[157; 78; ]*/
                        iterations_print = 2 * iterations_print;
                    }
                }
                /*[157; 78; ]*/
                whilePred_imopVarPre0 = epsilon <= diff;
                /*[157; 78; ]*/
                /*[157; 78; ]*/
                if (!whilePred_imopVarPre0) {
                /*[157; 78; ]*/
                    /*[157; 78; ]*/
                    break;
                }
                /*[157; 78; ]*/
#pragma omp for nowait
                /*[157; 78; ]*/
                /*[157; 78; ]*/
                /*[157; 78; ]*/
                for (i = 1; i < 500 - 1; i++) {
                /*[157; 78; ]*/
                    /*[157; 78; ]*/
                    /*[157; 78; ]*/
                    /*[157; 78; ]*/
                    /*[157; 78; ]*/
                    for (j = 1; j < 500 - 1; j++) {
                    /*[157; 78; ]*/
                        /*[157; 78; ]*/
                        double _imopVarPre167;
                        /*[157; 78; ]*/
                        double _imopVarPre168;
                        /*[157; 78; ]*/
                        _imopVarPre167 = w[i][j] - u[i][j];
                        /*[157; 78; ]*/
                        _imopVarPre168 = fabs(_imopVarPre167);
                        /*[157; 78; ]*/
                        /*[157; 78; ]*/
                        /*[157; 78; ]*/
                        if (my_diff < _imopVarPre168) {
                        /*[157; 78; ]*/
                            /*[157; 78; ]*/
                            double _imopVarPre170;
                            /*[157; 78; ]*/
                            double _imopVarPre171;
                            /*[157; 78; ]*/
                            _imopVarPre170 = w[i][j] - u[i][j];
                            /*[157; 78; ]*/
                            _imopVarPre171 = fabs(_imopVarPre170);
                            /*[157; 78; ]*/
                            /*[157; 78; ]*/
                            my_diff = _imopVarPre171;
                        }
                    }
                }
                /*[157; 78; ]*/
                // #pragma omp dummyFlush CRITICAL_START written([]) read([diff_imopVarPre2])
                /*[157; 78; ]*/
#pragma omp critical
                {
                /*[157; 78; ]*/
                    /*[157; 78; ]*/
                    /*[157; 78; ]*/
                    if (diff_imopVarPre2 < my_diff) {
                    /*[157; 78; ]*/
                        /*[157; 78; ]*/
                        diff_imopVarPre2 = my_diff;
                    }
                }
                /*[157; 78; ]*/
                // #pragma omp dummyFlush CRITICAL_END written([diff_imopVarPre2]) read([])
                /*[157; 78; ]*/
                my_diff = 0.0;
                /*[157; 78; ]*/
                // #pragma omp dummyFlush BARRIER_START written([]) read([heapCell#2, printf, i, u_imopVarPre89, q_imopVarPre6, heapCell#4, diff_imopVarPre2, q, heapCell#1])
                /*[157; 78; ]*/
#pragma omp barrier
                /*[130; ]*/
#pragma omp master
                {
                /*[130; ]*/
                    /*[130; ]*/
                    diff = 0.0;
                }
                /*[130; ]*/
#pragma omp master
                {
                /*[130; ]*/
                    /*[130; ]*/
                    iterations++;
                    /*[130; ]*/
                    /*[130; ]*/
                    if (iterations == iterations_print) {
                    /*[130; ]*/
                        /*[130; ]*/
                        printf("  %8d  %f\n", iterations, diff_imopVarPre2);
                        /*[130; ]*/
                        /*[130; ]*/
                        iterations_print = 2 * iterations_print;
                    }
                }
                /*[130; ]*/
                whilePred_imopVarPre0 = epsilon <= diff_imopVarPre2;
                /*[130; ]*/
#pragma omp for nowait
                /*[130; ]*/
                /*[130; ]*/
                /*[130; ]*/
                for (i = 1; i < 500 - 1; i++) {
                /*[130; ]*/
                    /*[130; ]*/
                    /*[130; ]*/
                    /*[130; ]*/
                    /*[130; ]*/
                    for (j = 1; j < 500 - 1; j++) {
                    /*[130; ]*/
                        /*[130; ]*/
                        q[i][j] = (u_imopVarPre89[i - 1][j] + u_imopVarPre89[i + 1][j] + u_imopVarPre89[i][j - 1] + u_imopVarPre89[i][j + 1]) / 4.0;
                    }
                }
                /*[130; ]*/
#pragma omp single nowait
                {
                /*[130; ]*/
                    /*[130; ]*/
                    u = u_imopVarPre89;
                    /*[130; ]*/
                    w = q_imopVarPre6;
                }
                /*[130; ]*/
                /*[130; ]*/
                if (!whilePred_imopVarPre0) {
                /*[130; ]*/
                    /*[130; ]*/
                    // #pragma omp dummyFlush BARRIER_START written([heapCell#3, u, w, heapCell#4, nullCell, diff]) read([diff_imopVarPre2])
                    /*[130; ]*/
#pragma omp barrier
                    /*[138; 141; ]*/
#pragma omp single nowait
                    {
                    /*[138; 141; ]*/
                        /*[138; 141; ]*/
                        diff = diff_imopVarPre2;
                    }
                    /*[138; 141; ]*/
                    // #pragma omp dummyFlush BARRIER_START written([diff]) read([printf, omp_get_wtime, diff])
                    /*[138; 141; ]*/
#pragma omp barrier
                    /*[142; ]*/
                    break;
                }
                /*[130; ]*/
                // #pragma omp dummyFlush BARRIER_START written([heapCell#3, u, w, diff]) read([fabs, heapCell#2, i, heapCell#3, q_imopVarPre6, u_imopVarPre89, heapCell#4, heapCell#1, q])
                /*[130; ]*/
#pragma omp barrier
                /*[141; ]*/
#pragma omp for nowait
                /*[141; ]*/
                /*[141; ]*/
                /*[141; ]*/
                for (i = 1; i < 500 - 1; i++) {
                /*[141; ]*/
                    /*[141; ]*/
                    /*[141; ]*/
                    /*[141; ]*/
                    /*[141; ]*/
                    for (j = 1; j < 500 - 1; j++) {
                    /*[141; ]*/
                        /*[141; ]*/
                        double _imopVarPre167;
                        /*[141; ]*/
                        double _imopVarPre168;
                        /*[141; ]*/
                        _imopVarPre167 = q[i][j] - u_imopVarPre89[i][j];
                        /*[141; ]*/
                        _imopVarPre168 = fabs(_imopVarPre167);
                        /*[141; ]*/
                        /*[141; ]*/
                        /*[141; ]*/
                        if (my_diff < _imopVarPre168) {
                        /*[141; ]*/
                            /*[141; ]*/
                            double _imopVarPre170;
                            /*[141; ]*/
                            double _imopVarPre171;
                            /*[141; ]*/
                            _imopVarPre170 = q_imopVarPre6[i][j] - u_imopVarPre89[i][j];
                            /*[141; ]*/
                            _imopVarPre171 = fabs(_imopVarPre170);
                            /*[141; ]*/
                            /*[141; ]*/
                            my_diff = _imopVarPre171;
                        }
                    }
                }
                /*[141; ]*/
                // #pragma omp dummyFlush CRITICAL_START written([]) read([diff_imopVarPre7])
                /*[141; ]*/
#pragma omp critical
                {
                /*[141; ]*/
                    /*[141; ]*/
                    /*[141; ]*/
                    if (diff_imopVarPre7 < my_diff) {
                    /*[141; ]*/
                        /*[141; ]*/
                        diff_imopVarPre7 = my_diff;
                    }
                }
                /*[141; ]*/
                // #pragma omp dummyFlush CRITICAL_END written([diff_imopVarPre7]) read([u, w])
                /*[141; ]*/
#pragma omp single nowait
                {
                /*[141; ]*/
                    /*[141; ]*/
                    q_imopVarPre9 = u;
                    /*[141; ]*/
                    u_imopVarPre12 = w;
                    /*[141; ]*/
                    w_imopVarPre10 = u;
                }
                /*[141; ]*/
                my_diff = 0.0;
                /*[141; ]*/
#pragma omp master
                {
                /*[141; ]*/
                    /*[141; ]*/
                    diff_imopVarPre2 = 0.0;
                }
                /*[141; ]*/
                // #pragma omp dummyFlush BARRIER_START written([q_imopVarPre9, u_imopVarPre12, diff_imopVarPre2, w_imopVarPre10]) read([heapCell#2, diff_imopVarPre7, printf, heapCell#3, i, q_imopVarPre9, u_imopVarPre12, heapCell#4, nullCell, w_imopVarPre10, heapCell#1])
                /*[141; ]*/
#pragma omp barrier
                /*[142; ]*/
#pragma omp master
                {
                /*[142; ]*/
                    /*[142; ]*/
                    iterations++;
                    /*[142; ]*/
                    /*[142; ]*/
                    if (iterations == iterations_print) {
                    /*[142; ]*/
                        /*[142; ]*/
                        printf("  %8d  %f\n", iterations, diff_imopVarPre7);
                        /*[142; ]*/
                        /*[142; ]*/
                        iterations_print = 2 * iterations_print;
                    }
                }
                /*[142; ]*/
                whilePred_imopVarPre0 = epsilon <= diff_imopVarPre7;
                /*[142; ]*/
                /*[142; ]*/
                if (!whilePred_imopVarPre0) {
                /*[142; ]*/
                    /*[147; 142; ]*/
#pragma omp single nowait
                    {
                    /*[147; 142; ]*/
                        /*[147; 142; ]*/
                        diff = diff_imopVarPre7;
                    }
                    /*[147; 142; ]*/
                    // #pragma omp dummyFlush BARRIER_START written([diff]) read([printf, omp_get_wtime, diff])
                    /*[147; 142; ]*/
#pragma omp barrier
                    /*[158; ]*/
                    break;
                }
                /*[142; ]*/
#pragma omp for nowait
                /*[142; ]*/
                /*[142; ]*/
                /*[142; ]*/
                for (i = 1; i < 500 - 1; i++) {
                /*[142; ]*/
                    /*[142; ]*/
                    /*[142; ]*/
                    /*[142; ]*/
                    /*[142; ]*/
                    for (j = 1; j < 500 - 1; j++) {
                    /*[142; ]*/
                        /*[142; ]*/
                        w_imopVarPre10[i][j] = (u_imopVarPre12[i - 1][j] + u_imopVarPre12[i + 1][j] + u_imopVarPre12[i][j - 1] + u_imopVarPre12[i][j + 1]) / 4.0;
                    }
                }
                /*[142; ]*/
#pragma omp single nowait
                {
                /*[142; ]*/
                    /*[142; ]*/
                    u = u_imopVarPre12;
                    /*[142; ]*/
                    w = q_imopVarPre9;
                }
                /*[142; ]*/
                // #pragma omp dummyFlush BARRIER_START written([heapCell#3, u, w, heapCell#4, nullCell]) read([fabs, heapCell#2, i, heapCell#3, u_imopVarPre12, heapCell#4, nullCell, w_imopVarPre10, heapCell#1])
                /*[142; ]*/
#pragma omp barrier
                /*[158; ]*/
#pragma omp master
                {
                /*[158; ]*/
                    /*[158; ]*/
                    diff_imopVarPre7 = 0.0;
                }
                /*[158; ]*/
#pragma omp for nowait
                /*[158; ]*/
                /*[158; ]*/
                /*[158; ]*/
                for (i = 1; i < 500 - 1; i++) {
                /*[158; ]*/
                    /*[158; ]*/
                    /*[158; ]*/
                    /*[158; ]*/
                    /*[158; ]*/
                    for (j = 1; j < 500 - 1; j++) {
                    /*[158; ]*/
                        /*[158; ]*/
                        double _imopVarPre167;
                        /*[158; ]*/
                        double _imopVarPre168;
                        /*[158; ]*/
                        _imopVarPre167 = w_imopVarPre10[i][j] - u_imopVarPre12[i][j];
                        /*[158; ]*/
                        _imopVarPre168 = fabs(_imopVarPre167);
                        /*[158; ]*/
                        /*[158; ]*/
                        /*[158; ]*/
                        if (my_diff < _imopVarPre168) {
                        /*[158; ]*/
                            /*[158; ]*/
                            double _imopVarPre170;
                            /*[158; ]*/
                            double _imopVarPre171;
                            /*[158; ]*/
                            _imopVarPre170 = w_imopVarPre10[i][j] - u_imopVarPre12[i][j];
                            /*[158; ]*/
                            _imopVarPre171 = fabs(_imopVarPre170);
                            /*[158; ]*/
                            /*[158; ]*/
                            my_diff = _imopVarPre171;
                        }
                    }
                }
                /*[158; ]*/
                // #pragma omp dummyFlush CRITICAL_START written([diff_imopVarPre7]) read([diff_imopVarPre11])
                /*[158; ]*/
#pragma omp critical
                {
                /*[158; ]*/
                    /*[158; ]*/
                    /*[158; ]*/
                    if (diff_imopVarPre11 < my_diff) {
                    /*[158; ]*/
                        /*[158; ]*/
                        diff_imopVarPre11 = my_diff;
                    }
                }
                /*[158; ]*/
                // #pragma omp dummyFlush CRITICAL_END written([diff_imopVarPre11]) read([u, w])
                /*[158; ]*/
                my_diff = 0.0;
                /*[158; ]*/
#pragma omp single nowait
                {
                /*[158; ]*/
                    /*[158; ]*/
                    q = u;
                    /*[158; ]*/
                    u_imopVarPre89 = w;
                }
                /*[158; ]*/
                // #pragma omp dummyFlush BARRIER_START written([u_imopVarPre89, q]) read([heapCell#2, printf, i, heapCell#3, u, w, heapCell#4, diff_imopVarPre11, nullCell, heapCell#1])
                /*[158; ]*/
#pragma omp barrier
                /*[159; ]*/
#pragma omp master
                {
                /*[159; ]*/
                    /*[159; ]*/
                    iterations++;
                    /*[159; ]*/
                    /*[159; ]*/
                    if (iterations == iterations_print) {
                    /*[159; ]*/
                        /*[159; ]*/
                        printf("  %8d  %f\n", iterations, diff_imopVarPre11);
                        /*[159; ]*/
                        /*[159; ]*/
                        iterations_print = 2 * iterations_print;
                    }
                }
                /*[159; ]*/
                whilePred_imopVarPre0 = epsilon <= diff_imopVarPre11;
                /*[159; ]*/
#pragma omp for nowait
                /*[159; ]*/
                /*[159; ]*/
                /*[159; ]*/
                for (i = 1; i < 500 - 1; i++) {
                /*[159; ]*/
                    /*[159; ]*/
                    /*[159; ]*/
                    /*[159; ]*/
                    /*[159; ]*/
                    for (j = 1; j < 500 - 1; j++) {
                    /*[159; ]*/
                        /*[159; ]*/
                        u[i][j] = (w[i - 1][j] + w[i + 1][j] + w[i][j - 1] + w[i][j + 1]) / 4.0;
                    }
                }
                /*[159; ]*/
                /*[159; ]*/
                if (!whilePred_imopVarPre0) {
                /*[159; ]*/
                    /*[153; 94; 159; ]*/
#pragma omp single nowait
                    {
                    /*[153; 94; 159; ]*/
                        /*[153; 94; 159; ]*/
                        diff = diff_imopVarPre11;
                    }
                    /*[153; 94; 159; ]*/
                    // #pragma omp dummyFlush BARRIER_START written([heapCell#3, heapCell#4, nullCell, diff]) read([printf, omp_get_wtime, diff])
                    /*[153; 94; 159; ]*/
#pragma omp barrier
                    /*[160; ]*/
                    break;
                }
                /*[159; ]*/
                // #pragma omp dummyFlush BARRIER_START written([heapCell#3, heapCell#4, nullCell]) read([fabs, heapCell#2, heapCell#3, i, u, w, heapCell#4, nullCell, heapCell#1])
                /*[159; ]*/
#pragma omp barrier
                /*[160; ]*/
#pragma omp master
                {
                /*[160; ]*/
                    /*[160; ]*/
                    diff_imopVarPre11 = 0.0;
                }
                /*[160; ]*/
#pragma omp for nowait
                /*[160; ]*/
                /*[160; ]*/
                /*[160; ]*/
                for (i = 1; i < 500 - 1; i++) {
                /*[160; ]*/
                    /*[160; ]*/
                    /*[160; ]*/
                    /*[160; ]*/
                    /*[160; ]*/
                    for (j = 1; j < 500 - 1; j++) {
                    /*[160; ]*/
                        /*[160; ]*/
                        double _imopVarPre167;
                        /*[160; ]*/
                        double _imopVarPre168;
                        /*[160; ]*/
                        _imopVarPre167 = u[i][j] - w[i][j];
                        /*[160; ]*/
                        _imopVarPre168 = fabs(_imopVarPre167);
                        /*[160; ]*/
                        /*[160; ]*/
                        /*[160; ]*/
                        if (my_diff < _imopVarPre168) {
                        /*[160; ]*/
                            /*[160; ]*/
                            double _imopVarPre170;
                            /*[160; ]*/
                            double _imopVarPre171;
                            /*[160; ]*/
                            _imopVarPre170 = u[i][j] - w[i][j];
                            /*[160; ]*/
                            _imopVarPre171 = fabs(_imopVarPre170);
                            /*[160; ]*/
                            /*[160; ]*/
                            my_diff = _imopVarPre171;
                        }
                    }
                }
                /*[160; ]*/
                // #pragma omp dummyFlush CRITICAL_START written([diff_imopVarPre11]) read([diff])
                /*[160; ]*/
#pragma omp critical
                {
                /*[160; ]*/
                    /*[160; ]*/
                    /*[160; ]*/
                    if (diff < my_diff) {
                    /*[160; ]*/
                        /*[160; ]*/
                        diff = my_diff;
                    }
                }
                /*[160; ]*/
                // #pragma omp dummyFlush CRITICAL_END written([diff]) read([heapCell#2, i, heapCell#3, u, w, heapCell#4, nullCell, heapCell#1])
                /*[160; ]*/
                my_diff = 0.0;
                /*[160; ]*/
#pragma omp single nowait
                {
                /*[160; ]*/
                    /*[160; ]*/
                    q_imopVarPre6 = u;
                }
                /*[160; ]*/
#pragma omp for nowait
                /*[160; ]*/
                /*[160; ]*/
                /*[160; ]*/
                for (i = 1; i < 500 - 1; i++) {
                /*[160; ]*/
                    /*[160; ]*/
                    /*[160; ]*/
                    /*[160; ]*/
                    /*[160; ]*/
                    for (j = 1; j < 500 - 1; j++) {
                    /*[160; ]*/
                        /*[160; ]*/
                        w[i][j] = (u[i - 1][j] + u[i + 1][j] + u[i][j - 1] + u[i][j + 1]) / 4.0;
                    }
                }
                /*[160; ]*/
                // #pragma omp dummyFlush BARRIER_START written([heapCell#3, q_imopVarPre6, heapCell#4, nullCell]) read([fabs, heapCell#2, printf, i, heapCell#3, u, w, omp_get_wtime, heapCell#4, nullCell, diff, heapCell#1])
                /*[160; ]*/
#pragma omp barrier
            }
        }
        /*[17; 157; 78; 142; 158; 160; ]*/
#pragma omp master
        {
        /*[17; 157; 78; 142; 158; 160; ]*/
            /*[17; 157; 78; 142; 158; 160; ]*/
            _imopVarPre173 = omp_get_wtime();
            /*[17; 157; 78; 142; 158; 160; ]*/
            /*[17; 157; 78; 142; 158; 160; ]*/
            wtime = _imopVarPre173 - wtime;
            /*[17; 157; 78; 142; 158; 160; ]*/
            printf("\n");
            /*[17; 157; 78; 142; 158; 160; ]*/
            /*[17; 157; 78; 142; 158; 160; ]*/
            printf("  %8d  %f\n", iterations, diff);
            /*[17; 157; 78; 142; 158; 160; ]*/
            /*[17; 157; 78; 142; 158; 160; ]*/
            printf("\n");
            /*[17; 157; 78; 142; 158; 160; ]*/
            /*[17; 157; 78; 142; 158; 160; ]*/
            printf("  Error tolerance achieved.\n");
            /*[17; 157; 78; 142; 158; 160; ]*/
            /*[17; 157; 78; 142; 158; 160; ]*/
            printf("  Wallclock time = %f\n", wtime);
            /*[17; 157; 78; 142; 158; 160; ]*/
            /*[17; 157; 78; 142; 158; 160; ]*/
            printf("\n");
            /*[17; 157; 78; 142; 158; 160; ]*/
            /*[17; 157; 78; 142; 158; 160; ]*/
            printf("HEATED_PLATE_OPENMP:\n");
            /*[17; 157; 78; 142; 158; 160; ]*/
            /*[17; 157; 78; 142; 158; 160; ]*/
            printf("  Normal end of execution.\n");
            /*[17; 157; 78; 142; 158; 160; ]*/
        }
    }
    /*[]*/
    return 0;
}
