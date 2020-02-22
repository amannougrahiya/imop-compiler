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
typedef signed char int8_t;
typedef short int16_t;
typedef int int32_t;
typedef long long int64_t;
typedef unsigned char u_int8_t;
typedef unsigned short u_int16_t;
typedef unsigned int u_int32_t;
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
struct __darwin_ymm_reg {
    char __ymm_reg[32];
} ;
struct __darwin_zmm_reg {
    char __zmm_reg[64];
} ;
struct __darwin_opmask_reg {
    char __opmask_reg[8];
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
struct __darwin_x86_cpmu_state64 {
    __uint64_t __ctrs[16];
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
struct __darwin_mcontext_avx512_32 {
    struct __darwin_i386_exception_state __es;
    struct __darwin_i386_thread_state __ss;
    struct __darwin_i386_avx512_state __fs;
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
struct __darwin_mcontext_avx512_64 {
    struct __darwin_x86_exception_state64 __es;
    struct __darwin_x86_thread_state64 __ss;
    struct __darwin_x86_avx512_state64 __fs;
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
typedef struct rusage_info_v4 rusage_info_current;
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
int printf(const char *restrict , ...);
typedef __darwin_off_t off_t;
typedef __darwin_ssize_t ssize_t;
typedef float float_t;
typedef double double_t;
extern double fabs(double );
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
struct stUn_imopVarPre11 {
    unsigned char _x[64];
} ;
typedef struct stUn_imopVarPre11 omp_lock_t;
struct stUn_imopVarPre12 {
    unsigned char _x[80];
} ;
typedef struct stUn_imopVarPre12 omp_nest_lock_t;
enum omp_sched_t {
    omp_sched_static = 1, omp_sched_dynamic = 2 , omp_sched_guided = 3 , omp_sched_auto = 4
} ;
typedef enum omp_sched_t omp_sched_t;
enum omp_proc_bind_t {
    omp_proc_bind_false = 0, omp_proc_bind_true = 1 , omp_proc_bind_master = 2 , omp_proc_bind_close = 3 , omp_proc_bind_spread = 4
} ;
typedef enum omp_proc_bind_t omp_proc_bind_t;
enum omp_lock_hint_t {
    omp_lock_hint_none = 0, omp_lock_hint_uncontended = 1 , omp_lock_hint_contended = 2 , omp_lock_hint_nonspeculative = 4 , omp_lock_hint_speculative = 8
} ;
typedef enum omp_lock_hint_t omp_lock_hint_t;
extern int omp_get_max_threads(void );
extern int omp_get_num_procs(void );
extern double omp_get_wtime(void );
int main(int argc, char *argv[]);
int main(int argc, char *argv[]) {
    double **u_imopVarPre89;
    double **u_imopVarPre12;
    double diff_imopVarPre11;
    double **w_imopVarPre10;
    double **q_imopVarPre9;
    double diff_imopVarPre7;
    double **q_imopVarPre6;
    double diff_imopVarPre2;
    double diff;
    double mean;
    double **u;
    double **w;
    double **q;
#pragma omp parallel shared(w, mean, u, diff)
    {
        double wtime;
        double epsilon = 0.001;
        int i;
        int iterations;
        int iterations_print;
        int j;
        double my_diff;
        double _imopVarPre173;
        unsigned long int _imopVarPre147;
        void *_imopVarPre148;
#pragma omp master
        {
            _imopVarPre147 = 500 * sizeof(double *);
            _imopVarPre148 = malloc(_imopVarPre147);
            u = (double **) _imopVarPre148;
        }
        unsigned long int _imopVarPre151;
        void *_imopVarPre152;
#pragma omp master
        {
            _imopVarPre151 = 500 * sizeof(double *);
            _imopVarPre152 = malloc(_imopVarPre151);
            w = (double **) _imopVarPre152;
        }
        int p;
#pragma omp master
        {
            for (p = 0; p < 500; p++) {
                unsigned long int _imopVarPre155;
                void *_imopVarPre156;
                _imopVarPre155 = 500 * sizeof(double);
                _imopVarPre156 = malloc(_imopVarPre155);
                u[p] = (double *) _imopVarPre156;
                unsigned long int _imopVarPre159;
                void *_imopVarPre160;
                _imopVarPre159 = 500 * sizeof(double);
                _imopVarPre160 = malloc(_imopVarPre159);
                w[p] = (double *) _imopVarPre160;
            }
            printf("\n");
            printf("HEATED_PLATE_OPENMP\n");
            printf("  C/OpenMP version\n");
            printf("  A program to solve for the steady state temperature distribution\n");
            printf("  over a rectangular plate.\n");
            printf("\n");
            printf("  Spatial grid of %d by %d points.\n", 500, 500);
            printf("  The iteration will be repeated until the change is <= %e\n", epsilon);
        }
        int _imopVarPre162;
#pragma omp master
        {
            _imopVarPre162 = omp_get_num_procs();
            printf("  Number of processors available = %d\n", _imopVarPre162);
        }
        int _imopVarPre164;
#pragma omp master
        {
            _imopVarPre164 = omp_get_max_threads();
            printf("  Number of threads =              %d\n", _imopVarPre164);
            mean = 0.0;
        }
#pragma omp master
        {
            iterations = 0;
            iterations_print = 1;
            printf("\n");
            printf(" Iteration  Change\n");
            printf("\n");
            wtime = omp_get_wtime();
            diff = epsilon;
        }
        /*diff:epsilon;*/
        int whilePred_imopVarPre0;
        /*diff:epsilon;*/
        // #pragma omp dummyFlush BARRIER_START written() read([heapCell#2, i, u, w, diff, heapCell#1])
        /*diff:epsilon;*/
#pragma omp barrier
#pragma omp for nowait
        /*diff:epsilon;*/
        /*diff:epsilon;*/
        /*diff:epsilon;*/
        for (i = 1; i < 500 - 1; i++) {
            /*diff:epsilon;*/
            w[i][0] = 100.0;
            /*diff:epsilon;*/
            u[i][0] = 100.0;
        }
        /*diff:epsilon;*/
        whilePred_imopVarPre0 = epsilon <= diff;
        /*diff:epsilon;*/
        // #pragma omp dummyFlush BARRIER_START written() read([heapCell#2, i, u, w, diff, heapCell#1])
        /*diff:epsilon;*/
#pragma omp barrier
#pragma omp for nowait
        /*diff:epsilon;*/
        /*diff:epsilon;*/
        /*diff:epsilon;*/
        for (i = 1; i < 500 - 1; i++) {
            /*diff:epsilon;*/
            w[i][500 - 1] = 100.0;
            /*diff:epsilon;*/
            u[i][500 - 1] = 100.0;
        }
        /*diff:epsilon;*/
        // #pragma omp dummyFlush BARRIER_START written() read([heapCell#2, j, u, w, diff, heapCell#1])
        /*diff:epsilon;*/
#pragma omp barrier
#pragma omp for nowait
        /*diff:epsilon;*/
        /*diff:epsilon;*/
        /*diff:epsilon;*/
        for (j = 0; j < 500; j++) {
            /*diff:epsilon;*/
            w[500 - 1][j] = 100.0;
            /*diff:epsilon;*/
            u[500 - 1][j] = 100.0;
        }
        /*diff:epsilon;*/
        // #pragma omp dummyFlush BARRIER_START written() read([heapCell#2, j, u, w, diff, heapCell#1])
        /*diff:epsilon;*/
#pragma omp barrier
#pragma omp for nowait
        /*diff:epsilon;*/
        /*diff:epsilon;*/
        /*diff:epsilon;*/
        for (j = 0; j < 500; j++) {
            /*diff:epsilon;*/
            w[0][j] = 0.0;
            /*diff:epsilon;*/
            u[0][j] = 0.0;
        }
        /*diff:epsilon;*/
        // #pragma omp dummyFlush BARRIER_START written() read([heapCell#2, i, mean, w, heapCell#4, diff])
        /*diff:epsilon;*/
#pragma omp barrier
#pragma omp for reduction(+:mean) nowait
        /*diff:epsilon;*/
        /*diff:epsilon;*/
        /*diff:epsilon;*/
        for (i = 1; i < 500 - 1; i++) {
            /*diff:epsilon;*/
            mean = mean + w[i][0] + w[i][500 - 1];
        }
        /*diff:epsilon;*/
        // #pragma omp dummyFlush BARRIER_START written() read([heapCell#2, mean, j, w, heapCell#4, diff])
        /*diff:epsilon;*/
#pragma omp barrier
#pragma omp for reduction(+:mean) nowait
        /*diff:epsilon;*/
        /*diff:epsilon;*/
        /*diff:epsilon;*/
        for (j = 0; j < 500; j++) {
            /*diff:epsilon;*/
            mean = mean + w[500 - 1][j] + w[0][j];
        }
        /*diff:epsilon;*/
        // #pragma omp dummyFlush BARRIER_START written() read([printf, mean, diff])
        /*diff:epsilon;*/
#pragma omp barrier
#pragma omp master
        {
            /*diff:epsilon;*/
            mean = mean / (double) (2 * 500 + 2 * 500 - 4);
            /*diff:epsilon;*/
            printf("\n");
            /*diff:epsilon;*/
            /*diff:epsilon;*/
            printf("  MEAN = %f\n", mean);
            /*diff:epsilon;*/
        }
        /*diff:epsilon;*/
        // #pragma omp dummyFlush BARRIER_START written() read([heapCell#2, i, mean, w, diff])
        /*diff:epsilon;*/
#pragma omp barrier
#pragma omp for nowait
        /*diff:epsilon;*/
        /*diff:epsilon;*/
        /*diff:epsilon;*/
        for (i = 1; i < 500 - 1; i++) {
            /*diff:epsilon;*/
            /*diff:epsilon;*/
            /*diff:epsilon;*/
            for (j = 1; j < 500 - 1; j++) {
                /*diff:epsilon;*/
                w[i][j] = mean;
            }
        }
        /*diff:epsilon;*/
        // #pragma omp dummyFlush BARRIER_START written() read([printf, whilePred_imopVarPre0, u, w, omp_get_wtime, diff])
        /*diff:epsilon;*/
#pragma omp barrier
        /*diff:epsilon;*/
        if (whilePred_imopVarPre0) {
#pragma omp single nowait
            {
                /*diff:epsilon;*/
                q = u;
                /*q:u; diff:epsilon;*/
                u_imopVarPre89 = w;
            }
            /*u_imopVarPre89:w; q:u; diff:epsilon;*/
            my_diff = 0.0;
            /*u_imopVarPre89:w; q:u; diff:epsilon;*/
            ;
#pragma omp master
            {
                /*u_imopVarPre89:w; q:u; diff:epsilon;*/
                diff_imopVarPre2 = 0.0;
            }
            /*u_imopVarPre89:w; q:u; diff:epsilon;*/
            ;
#pragma omp master
            {
                /*u_imopVarPre89:w; q:u; diff:epsilon;*/
                diff_imopVarPre7 = 0.0;
            }
#pragma omp master
            {
                /*u_imopVarPre89:w; q:u; diff:epsilon;*/
                diff_imopVarPre11 = 0.0;
            }
#pragma omp master
            {
                /*u_imopVarPre89:w; q:u; diff:epsilon;*/
                diff = 0.0;
            }
#pragma omp for nowait
            /*u_imopVarPre89:w; q:u;*/
            /*u_imopVarPre89:w; q:u;*/
            /*u_imopVarPre89:w; q:u;*/
            for (i = 1; i < 500 - 1; i++) {
                /*u_imopVarPre89:w; q:u;*/
                /*u_imopVarPre89:w; q:u;*/
                /*u_imopVarPre89:w; q:u;*/
                for (j = 1; j < 500 - 1; j++) {
                    /*u_imopVarPre89:w; q:u;*/
                    u[i][j] = (w[i - 1][j] + w[i + 1][j] + w[i][j - 1] + w[i][j + 1]) / 4.0;
                }
            }
#pragma omp single nowait
            {
                /*u_imopVarPre89:w; q:u;*/
                q_imopVarPre6 = u;
            }
            /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
            // #pragma omp dummyFlush BARRIER_START written() read()
            /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
#pragma omp barrier
#pragma omp for nowait
            /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
            /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
            /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
            for (i = 1; i < 500 - 1; i++) {
                /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                for (j = 1; j < 500 - 1; j++) {
                    /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                    double _imopVarPre167;
                    /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                    double _imopVarPre168;
                    /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                    _imopVarPre167 = u[i][j] - w[i][j];
                    /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                    _imopVarPre168 = fabs(_imopVarPre167);
                    /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                    /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                    if (my_diff < _imopVarPre168) {
                        /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                        double _imopVarPre170;
                        /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                        double _imopVarPre171;
                        /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                        _imopVarPre170 = u[i][j] - w[i][j];
                        /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                        _imopVarPre171 = fabs(_imopVarPre170);
                        /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                        /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                        my_diff = _imopVarPre171;
                    }
                }
            }
            /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
            // #pragma omp dummyFlush CRITICAL_START written([]) read([diff])
#pragma omp critical
            {
                /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                if (diff < my_diff) {
                    /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                    diff = my_diff;
                }
            }
            /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
            // #pragma omp dummyFlush CRITICAL_END written([diff]) read([heapCell#2, heapCell#3, i, u, w, heapCell#1])
            /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
            my_diff = 0.0;
#pragma omp for nowait
            /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
            /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
            /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
            for (i = 1; i < 500 - 1; i++) {
                /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                for (j = 1; j < 500 - 1; j++) {
                    /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
                    w[i][j] = (u[i - 1][j] + u[i + 1][j] + u[i][j - 1] + u[i][j + 1]) / 4.0;
                }
            }
            /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
            // #pragma omp dummyFlush BARRIER_START written([heapCell#4]) read([fabs, heapCell#2, printf, i, heapCell#3, u, w, omp_get_wtime, heapCell#4, nullCell, diff, heapCell#1])
            /*u_imopVarPre89:w; q:u; q_imopVarPre6:u;*/
#pragma omp barrier
            /*q_imopVarPre6:u;*/
            while (1) {
#pragma omp master
                {
                    /*q_imopVarPre6:u;*/
                    iterations++;
                    /*q_imopVarPre6:u;*/
                    if (iterations == iterations_print) {
                        /*q_imopVarPre6:u;*/
                        printf("  %8d  %f\n", iterations, diff);
                        /*q_imopVarPre6:u;*/
                        /*q_imopVarPre6:u;*/
                        iterations_print = 2 * iterations_print;
                    }
                }
                /*q_imopVarPre6:u;*/
                whilePred_imopVarPre0 = epsilon <= diff;
                /*q_imopVarPre6:u;*/
                if (!whilePred_imopVarPre0) {
                    /*q_imopVarPre6:u;*/
                    break;
                }
#pragma omp for nowait
                /*q_imopVarPre6:u;*/
                /*q_imopVarPre6:u;*/
                /*q_imopVarPre6:u;*/
                for (i = 1; i < 500 - 1; i++) {
                    /*q_imopVarPre6:u;*/
                    /*q_imopVarPre6:u;*/
                    /*q_imopVarPre6:u;*/
                    for (j = 1; j < 500 - 1; j++) {
                        /*q_imopVarPre6:u;*/
                        double _imopVarPre167;
                        /*q_imopVarPre6:u;*/
                        double _imopVarPre168;
                        /*q_imopVarPre6:u;*/
                        _imopVarPre167 = w[i][j] - u[i][j];
                        /*q_imopVarPre6:u;*/
                        _imopVarPre168 = fabs(_imopVarPre167);
                        /*q_imopVarPre6:u;*/
                        /*q_imopVarPre6:u;*/
                        if (my_diff < _imopVarPre168) {
                            /*q_imopVarPre6:u;*/
                            double _imopVarPre170;
                            /*q_imopVarPre6:u;*/
                            double _imopVarPre171;
                            /*q_imopVarPre6:u;*/
                            _imopVarPre170 = w[i][j] - u[i][j];
                            /*q_imopVarPre6:u;*/
                            _imopVarPre171 = fabs(_imopVarPre170);
                            /*q_imopVarPre6:u;*/
                            /*q_imopVarPre6:u;*/
                            my_diff = _imopVarPre171;
                        }
                    }
                }
                /*q_imopVarPre6:u;*/
                // #pragma omp dummyFlush CRITICAL_START written([]) read([diff_imopVarPre2])
#pragma omp critical
                {
                    /*q_imopVarPre6:u;*/
                    if (diff_imopVarPre2 < my_diff) {
                        /*q_imopVarPre6:u;*/
                        diff_imopVarPre2 = my_diff;
                    }
                }
                /*q_imopVarPre6:u;*/
                // #pragma omp dummyFlush CRITICAL_END written([diff_imopVarPre2]) read([])
                /*q_imopVarPre6:u;*/
                my_diff = 0.0;
                /*q_imopVarPre6:u;*/
                // #pragma omp dummyFlush BARRIER_START written([]) read([heapCell#2, printf, i, u_imopVarPre89, q_imopVarPre6, heapCell#4, diff_imopVarPre2, q, heapCell#1])
                /*q_imopVarPre6:u;*/
#pragma omp barrier
#pragma omp master
                {
                    /*q_imopVarPre6:u;*/
                    diff = 0.0;
                }
#pragma omp master
                {
                    /*q_imopVarPre6:u;*/
                    iterations++;
                    /*q_imopVarPre6:u;*/
                    if (iterations == iterations_print) {
                        /*q_imopVarPre6:u;*/
                        printf("  %8d  %f\n", iterations, diff_imopVarPre2);
                        /*q_imopVarPre6:u;*/
                        /*q_imopVarPre6:u;*/
                        iterations_print = 2 * iterations_print;
                    }
                }
                /*q_imopVarPre6:u;*/
                whilePred_imopVarPre0 = epsilon <= diff_imopVarPre2;
#pragma omp for nowait
                /*q_imopVarPre6:u;*/
                /*q_imopVarPre6:u;*/
                /*q_imopVarPre6:u;*/
                for (i = 1; i < 500 - 1; i++) {
                    /*q_imopVarPre6:u;*/
                    /*q_imopVarPre6:u;*/
                    /*q_imopVarPre6:u;*/
                    for (j = 1; j < 500 - 1; j++) {
                        /*q_imopVarPre6:u;*/
                        q[i][j] = (u_imopVarPre89[i - 1][j] + u_imopVarPre89[i + 1][j] + u_imopVarPre89[i][j - 1] + u_imopVarPre89[i][j + 1]) / 4.0;
                    }
                }
#pragma omp single nowait
                {
                    /*q_imopVarPre6:u;*/
                    u = u_imopVarPre89;
                    /*u:u_imopVarPre89;*/
                    w = q_imopVarPre6;
                }
                /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                if (!whilePred_imopVarPre0) {
                    /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                    // #pragma omp dummyFlush BARRIER_START written([heapCell#3, u, w, heapCell#4, nullCell, diff]) read([diff_imopVarPre2])
                    /*w:q_imopVarPre6; u:u_imopVarPre89;*/
#pragma omp barrier
#pragma omp single nowait
                    {
                        /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                        diff = diff_imopVarPre2;
                    }
                    /*w:q_imopVarPre6; u:u_imopVarPre89; diff:diff_imopVarPre2;*/
                    // #pragma omp dummyFlush BARRIER_START written([diff]) read([printf, omp_get_wtime, diff])
                    /*w:q_imopVarPre6; u:u_imopVarPre89; diff:diff_imopVarPre2;*/
#pragma omp barrier
                    /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                    break;
                }
                /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                // #pragma omp dummyFlush BARRIER_START written([heapCell#3, u, w, diff]) read([fabs, heapCell#2, i, heapCell#3, q_imopVarPre6, u_imopVarPre89, heapCell#4, heapCell#1, q])
                /*w:q_imopVarPre6; u:u_imopVarPre89;*/
#pragma omp barrier
#pragma omp for nowait
                /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                for (i = 1; i < 500 - 1; i++) {
                    /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                    /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                    /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                    for (j = 1; j < 500 - 1; j++) {
                        /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                        double _imopVarPre167;
                        /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                        double _imopVarPre168;
                        /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                        _imopVarPre167 = q[i][j] - u_imopVarPre89[i][j];
                        /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                        _imopVarPre168 = fabs(_imopVarPre167);
                        /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                        /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                        if (my_diff < _imopVarPre168) {
                            /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                            double _imopVarPre170;
                            /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                            double _imopVarPre171;
                            /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                            _imopVarPre170 = q_imopVarPre6[i][j] - u_imopVarPre89[i][j];
                            /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                            _imopVarPre171 = fabs(_imopVarPre170);
                            /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                            /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                            my_diff = _imopVarPre171;
                        }
                    }
                }
                /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                // #pragma omp dummyFlush CRITICAL_START written([]) read([diff_imopVarPre7])
#pragma omp critical
                {
                    /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                    if (diff_imopVarPre7 < my_diff) {
                        /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                        diff_imopVarPre7 = my_diff;
                    }
                }
                /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                // #pragma omp dummyFlush CRITICAL_END written([diff_imopVarPre7]) read([u, w])
#pragma omp single nowait
                {
                    /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                    q_imopVarPre9 = u;
                    /*q_imopVarPre9:u; w:q_imopVarPre6; u:u_imopVarPre89;*/
                    u_imopVarPre12 = w;
                    /*q_imopVarPre9:u; w:q_imopVarPre6; u:u_imopVarPre89; u_imopVarPre12:w;*/
                    w_imopVarPre10 = u;
                }
                /*q_imopVarPre9:u; w:q_imopVarPre6; w_imopVarPre10:u; u:u_imopVarPre89; u_imopVarPre12:w;*/
                my_diff = 0.0;
#pragma omp master
                {
                    /*q_imopVarPre9:u; w:q_imopVarPre6; w_imopVarPre10:u; u:u_imopVarPre89; u_imopVarPre12:w;*/
                    diff_imopVarPre2 = 0.0;
                }
                /*q_imopVarPre9:u; w:q_imopVarPre6; w_imopVarPre10:u; u:u_imopVarPre89; u_imopVarPre12:w;*/
                // #pragma omp dummyFlush BARRIER_START written([q_imopVarPre9, u_imopVarPre12, diff_imopVarPre2, w_imopVarPre10]) read([heapCell#2, diff_imopVarPre7, printf, heapCell#3, i, q_imopVarPre9, u_imopVarPre12, heapCell#4, nullCell, w_imopVarPre10, heapCell#1])
                /*q_imopVarPre9:u; w:q_imopVarPre6; w_imopVarPre10:u; u:u_imopVarPre89; u_imopVarPre12:w;*/
#pragma omp barrier
#pragma omp master
                {
                    /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                    iterations++;
                    /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                    if (iterations == iterations_print) {
                        /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                        printf("  %8d  %f\n", iterations, diff_imopVarPre7);
                        /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                        /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                        iterations_print = 2 * iterations_print;
                    }
                }
                /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                whilePred_imopVarPre0 = epsilon <= diff_imopVarPre7;
                /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                if (!whilePred_imopVarPre0) {
#pragma omp single nowait
                    {
                        /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                        diff = diff_imopVarPre7;
                    }
// #pragma omp dummyFlush BARRIER_START written([diff]) read([printf, omp_get_wtime, diff])
#pragma omp barrier
                    break;
                }
#pragma omp for nowait
                /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                for (i = 1; i < 500 - 1; i++) {
                    /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                    /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                    /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                    for (j = 1; j < 500 - 1; j++) {
                        /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                        w_imopVarPre10[i][j] = (u_imopVarPre12[i - 1][j] + u_imopVarPre12[i + 1][j] + u_imopVarPre12[i][j - 1] + u_imopVarPre12[i][j + 1]) / 4.0;
                    }
                }
#pragma omp single nowait
                {
                    /*w:q_imopVarPre6; u:u_imopVarPre89;*/
                    u = u_imopVarPre12;
                    /*w:q_imopVarPre6; u:u_imopVarPre12;*/
                    w = q_imopVarPre9;
                }
                /*w:q_imopVarPre9; u:u_imopVarPre12;*/
                // #pragma omp dummyFlush BARRIER_START written([heapCell#3, u, w, heapCell#4, nullCell]) read([fabs, heapCell#2, i, heapCell#3, u_imopVarPre12, heapCell#4, nullCell, w_imopVarPre10, heapCell#1])
                /*w:q_imopVarPre9; u:u_imopVarPre12;*/
#pragma omp barrier
#pragma omp master
                {
                    diff_imopVarPre7 = 0.0;
                }
#pragma omp for nowait
                for (i = 1; i < 500 - 1; i++) {
                    for (j = 1; j < 500 - 1; j++) {
                        double _imopVarPre167;
                        double _imopVarPre168;
                        _imopVarPre167 = w_imopVarPre10[i][j] - u_imopVarPre12[i][j];
                        _imopVarPre168 = fabs(_imopVarPre167);
                        if (my_diff < _imopVarPre168) {
                            double _imopVarPre170;
                            double _imopVarPre171;
                            _imopVarPre170 = w_imopVarPre10[i][j] - u_imopVarPre12[i][j];
                            _imopVarPre171 = fabs(_imopVarPre170);
                            my_diff = _imopVarPre171;
                        }
                    }
                }
// #pragma omp dummyFlush CRITICAL_START written([diff_imopVarPre7]) read([diff_imopVarPre11])
#pragma omp critical
                {
                    if (diff_imopVarPre11 < my_diff) {
                        diff_imopVarPre11 = my_diff;
                    }
                }
// #pragma omp dummyFlush CRITICAL_END written([diff_imopVarPre11]) read([u, w])
                my_diff = 0.0;
#pragma omp single nowait
                {
                    q = u;
                    /*q:u;*/
                    u_imopVarPre89 = w;
                }
                /*u_imopVarPre89:w; q:u;*/
                // #pragma omp dummyFlush BARRIER_START written([u_imopVarPre89, q]) read([heapCell#2, printf, i, heapCell#3, u, w, heapCell#4, diff_imopVarPre11, nullCell, heapCell#1])
                /*u_imopVarPre89:w; q:u;*/
#pragma omp barrier
#pragma omp master
                {
                    iterations++;
                    if (iterations == iterations_print) {
                        printf("  %8d  %f\n", iterations, diff_imopVarPre11);
                        iterations_print = 2 * iterations_print;
                    }
                }
                whilePred_imopVarPre0 = epsilon <= diff_imopVarPre11;
#pragma omp for nowait
                for (i = 1; i < 500 - 1; i++) {
                    for (j = 1; j < 500 - 1; j++) {
                        u[i][j] = (w[i - 1][j] + w[i + 1][j] + w[i][j - 1] + w[i][j + 1]) / 4.0;
                    }
                }
                if (!whilePred_imopVarPre0) {
#pragma omp single nowait
                    {
                        diff = diff_imopVarPre11;
                    }
// #pragma omp dummyFlush BARRIER_START written([heapCell#3, heapCell#4, nullCell, diff]) read([printf, omp_get_wtime, diff])
#pragma omp barrier
                    break;
                }
// #pragma omp dummyFlush BARRIER_START written([heapCell#3, heapCell#4, nullCell]) read([fabs, heapCell#2, heapCell#3, i, u, w, heapCell#4, nullCell, heapCell#1])
#pragma omp barrier
#pragma omp master
                {
                    diff_imopVarPre11 = 0.0;
                }
#pragma omp for nowait
                for (i = 1; i < 500 - 1; i++) {
                    for (j = 1; j < 500 - 1; j++) {
                        double _imopVarPre167;
                        double _imopVarPre168;
                        _imopVarPre167 = u[i][j] - w[i][j];
                        _imopVarPre168 = fabs(_imopVarPre167);
                        if (my_diff < _imopVarPre168) {
                            double _imopVarPre170;
                            double _imopVarPre171;
                            _imopVarPre170 = u[i][j] - w[i][j];
                            _imopVarPre171 = fabs(_imopVarPre170);
                            my_diff = _imopVarPre171;
                        }
                    }
                }
// #pragma omp dummyFlush CRITICAL_START written([diff_imopVarPre11]) read([diff])
#pragma omp critical
                {
                    if (diff < my_diff) {
                        diff = my_diff;
                    }
                }
// #pragma omp dummyFlush CRITICAL_END written([diff]) read([heapCell#2, i, heapCell#3, u, w, heapCell#4, nullCell, heapCell#1])
                my_diff = 0.0;
#pragma omp single nowait
                {
                    q_imopVarPre6 = u;
                }
#pragma omp for nowait
                /*q_imopVarPre6:u;*/
                /*q_imopVarPre6:u;*/
                /*q_imopVarPre6:u;*/
                for (i = 1; i < 500 - 1; i++) {
                    /*q_imopVarPre6:u;*/
                    /*q_imopVarPre6:u;*/
                    /*q_imopVarPre6:u;*/
                    for (j = 1; j < 500 - 1; j++) {
                        /*q_imopVarPre6:u;*/
                        w[i][j] = (u[i - 1][j] + u[i + 1][j] + u[i][j - 1] + u[i][j + 1]) / 4.0;
                    }
                }
                /*q_imopVarPre6:u;*/
                // #pragma omp dummyFlush BARRIER_START written([heapCell#3, q_imopVarPre6, heapCell#4, nullCell]) read([fabs, heapCell#2, printf, i, heapCell#3, u, w, omp_get_wtime, heapCell#4, nullCell, diff, heapCell#1])
                /*q_imopVarPre6:u;*/
#pragma omp barrier
            }
        }
#pragma omp master
        {
            _imopVarPre173 = omp_get_wtime();
            wtime = _imopVarPre173 - wtime;
            printf("\n");
            printf("  %8d  %f\n", iterations, diff);
            printf("\n");
            printf("  Error tolerance achieved.\n");
            printf("  Wallclock time = %f\n", wtime);
            printf("\n");
            printf("HEATED_PLATE_OPENMP:\n");
            printf("  Normal end of execution.\n");
        }
    }
    return 0;
}
