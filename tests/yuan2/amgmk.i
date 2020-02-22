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
    void (*__routine)(void *);
    void *__arg;
    struct __darwin_pthread_handler_rec *__next;
};
struct _opaque_pthread_attr_t {
    long __sig;
    char __opaque[56];
};
struct _opaque_pthread_cond_t {
    long __sig;
    char __opaque[40];
};
struct _opaque_pthread_condattr_t {
    long __sig;
    char __opaque[8];
};
struct _opaque_pthread_mutex_t {
    long __sig;
    char __opaque[56];
};
struct _opaque_pthread_mutexattr_t {
    long __sig;
    char __opaque[8];
};
struct _opaque_pthread_once_t {
    long __sig;
    char __opaque[8];
};
struct _opaque_pthread_rwlock_t {
    long __sig;
    char __opaque[192];
};
struct _opaque_pthread_rwlockattr_t {
    long __sig;
    char __opaque[16];
};
struct _opaque_pthread_t {
    long __sig;
    struct __darwin_pthread_handler_rec *__cleanup_stack;
    char __opaque[8176];
};
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
typedef __darwin_va_list va_list;
typedef __darwin_size_t size_t;

int renameat(int, const char *, int, const char *) ;
int renamex_np(const char *, const char *, unsigned int) ;
int renameatx_np(int, const char *, int, const char *, unsigned int) ;

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
    int (* _close)(void *);
    int (* _read) (void *, char *, int);
    fpos_t (* _seek) (void *, fpos_t, int);
    int (* _write)(void *, const char *, int);
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
int fgetpos(FILE * restrict, fpos_t *);
char *fgets(char * restrict, int, FILE *);
FILE *fopen(const char * restrict __filename, const char * restrict __mode) __asm("_" "fopen" );
int fprintf(FILE * restrict, const char * restrict, ...) __attribute__((__format__ (__printf__, 2, 3)));
int fputc(int, FILE *);
int fputs(const char * restrict, FILE * restrict) __asm("_" "fputs" );
size_t fread(void * restrict __ptr, size_t __size, size_t __nitems, FILE * restrict __stream);
FILE *freopen(const char * restrict, const char * restrict,
        FILE * restrict) __asm("_" "freopen" );
int fscanf(FILE * restrict, const char * restrict, ...) __attribute__((__format__ (__scanf__, 2, 3)));
int fseek(FILE *, long, int);
int fsetpos(FILE *, const fpos_t *);
long ftell(FILE *);
size_t fwrite(const void * restrict __ptr, size_t __size, size_t __nitems, FILE * restrict __stream) __asm("_" "fwrite" );
int getc(FILE *);
int getchar(void);
char *gets(char *);
void perror(const char *);
int printf(const char * restrict, ...) __attribute__((__format__ (__printf__, 1, 2)));
int putc(int, FILE *);
int putchar(int);
int puts(const char *);
int remove(const char *);
int rename (const char *__old, const char *__new);
void rewind(FILE *);
int scanf(const char * restrict, ...) __attribute__((__format__ (__scanf__, 1, 2)));
void setbuf(FILE * restrict, char * restrict);
int setvbuf(FILE * restrict, char * restrict, int, size_t);
int sprintf(char * restrict, const char * restrict, ...) __attribute__((__format__ (__printf__, 2, 3))) ;
int sscanf(const char * restrict, const char * restrict, ...) __attribute__((__format__ (__scanf__, 2, 3)));
FILE *tmpfile(void);

__attribute__((deprecated("This function is provided for compatibility reasons only.  Due to security concerns inherent in the design of tmpnam(3), it is highly recommended that you use mkstemp(3) instead.")))
char *tmpnam(char *);
int ungetc(int, FILE *);
int vfprintf(FILE * restrict, const char * restrict, va_list) __attribute__((__format__ (__printf__, 2, 0)));
int vprintf(const char * restrict, va_list) __attribute__((__format__ (__printf__, 1, 0)));
int vsprintf(char * restrict, const char * restrict, va_list) __attribute__((__format__ (__printf__, 2, 0))) ;


char *ctermid(char *);
FILE *fdopen(int, const char *) __asm("_" "fdopen" );
int fileno(FILE *);


int pclose(FILE *) ;
FILE *popen(const char *, const char *) __asm("_" "popen" ) ;


int __srget(FILE *);
int __svfscanf(FILE *, const char *, va_list) __attribute__((__format__ (__scanf__, 2, 0)));
int __swbuf(int, FILE *);

extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __sputc(int _c, FILE *_p) {
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

__attribute__((deprecated("This function is provided for compatibility reasons only.  Due to security concerns inherent in the design of tempnam(3), it is highly recommended that you use mkstemp(3) instead.")))
char *tempnam(const char *__dir, const char *__prefix) __asm("_" "tempnam" );

typedef __darwin_off_t off_t;

int fseeko(FILE * __stream, off_t __offset, int __whence);
off_t ftello(FILE * __stream);


int snprintf(char * restrict __str, size_t __size, const char * restrict __format, ...) __attribute__((__format__ (__printf__, 3, 4)));
int vfscanf(FILE * restrict __stream, const char * restrict __format, va_list) __attribute__((__format__ (__scanf__, 2, 0)));
int vscanf(const char * restrict __format, va_list) __attribute__((__format__ (__scanf__, 1, 0)));
int vsnprintf(char * restrict __str, size_t __size, const char * restrict __format, va_list) __attribute__((__format__ (__printf__, 3, 0)));
int vsscanf(const char * restrict __str, const char * restrict __format, va_list) __attribute__((__format__ (__scanf__, 2, 0)));

typedef __darwin_ssize_t ssize_t;

int dprintf(int, const char * restrict, ...) __attribute__((__format__ (__printf__, 2, 3))) ;
int vdprintf(int, const char * restrict, va_list) __attribute__((__format__ (__printf__, 2, 0))) ;
ssize_t getdelim(char ** restrict __linep, size_t * restrict __linecapp, int __delimiter, FILE * restrict __stream) ;
ssize_t getline(char ** restrict __linep, size_t * restrict __linecapp, FILE * restrict __stream) ;
FILE *fmemopen(void * restrict __buf, size_t __size, const char * restrict __mode) ;
FILE *open_memstream(char **__bufp, size_t *__sizep) ;


extern const int sys_nerr;
extern const char *const sys_errlist[];
int asprintf(char ** restrict, const char * restrict, ...) __attribute__((__format__ (__printf__, 2, 3)));
char *ctermid_r(char *);
char *fgetln(FILE *, size_t *);
const char *fmtcheck(const char *, const char *);
int fpurge(FILE *);
void setbuffer(FILE *, char *, int);
int setlinebuf(FILE *);
int vasprintf(char ** restrict, const char * restrict, va_list) __attribute__((__format__ (__printf__, 2, 0)));
FILE *zopen(const char *, const char *, int);
FILE *funopen(const void *,
        int (* )(void *, char *, int),
        int (* )(void *, const char *, int),
        fpos_t (* )(void *, fpos_t, int),
        int (* )(void *));

extern int __sprintf_chk (char * restrict, int, size_t,
        const char * restrict, ...);
extern int __snprintf_chk (char * restrict, size_t, int, size_t,
        const char * restrict, ...);
extern int __vsprintf_chk (char * restrict, int, size_t,
        const char * restrict, va_list);
extern int __vsnprintf_chk (char * restrict, size_t, int, size_t,
        const char * restrict, va_list);
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
struct __darwin_ymm_reg
{
    char __ymm_reg[32];
};
struct __darwin_zmm_reg
{
    char __zmm_reg[64];
};
struct __darwin_opmask_reg
{
    char __opmask_reg[8];
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
struct __darwin_i386_avx512_state
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
struct __darwin_x86_avx512_state64
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
struct __darwin_x86_cpmu_state64
{
    __uint64_t __ctrs[16];
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
struct __darwin_mcontext_avx512_32
{
    struct __darwin_i386_exception_state __es;
    struct __darwin_i386_thread_state __ss;
    struct __darwin_i386_avx512_state __fs;
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
struct __darwin_mcontext_avx512_64
{
    struct __darwin_x86_exception_state64 __es;
    struct __darwin_x86_thread_state64 __ss;
    struct __darwin_x86_avx512_state64 __fs;
};
typedef struct __darwin_mcontext64 *mcontext_t;
typedef __darwin_pthread_attr_t pthread_attr_t;
struct __darwin_sigaltstack
{
    void *ss_sp;
    __darwin_size_t ss_size;
    int ss_flags;
};
typedef struct __darwin_sigaltstack stack_t;
struct __darwin_ucontext
{
    int uc_onstack;
    __darwin_sigset_t uc_sigmask;
    struct __darwin_sigaltstack uc_stack;
    struct __darwin_ucontext *uc_link;
    __darwin_size_t uc_mcsize;
    struct __darwin_mcontext64 *uc_mcontext;
};
typedef struct __darwin_ucontext ucontext_t;
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
};
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
};
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
};
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
};
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
};
typedef struct rusage_info_v4 rusage_info_current;
struct rlimit {
    rlim_t rlim_cur;
    rlim_t rlim_max;
};
struct proc_rlimit_control_wakeupmon {
    uint32_t wm_flags;
    int32_t wm_rate;
};

int getpriority(int, id_t);
int getiopolicy_np(int, int) ;
int getrlimit(int, struct rlimit *) __asm("_" "getrlimit" );
int getrusage(int, struct rusage *);
int setpriority(int, id_t, int);
int setiopolicy_np(int, int, int) ;
int setrlimit(int, const struct rlimit *) __asm("_" "setrlimit" );

static inline
    __uint16_t
_OSSwapInt16(
        __uint16_t _data
        )
{
    return ((__uint16_t)((_data << 8) | (_data >> 8)));
}
static inline
    __uint32_t
_OSSwapInt32(
        __uint32_t _data
        )
{
    __asm__ ("bswap   %0" : "+r" (_data));
    return _data;
}
static inline
    __uint64_t
_OSSwapInt64(
        __uint64_t _data
        )
{
    __asm__ ("bswap   %0" : "+r" (_data));
    return _data;
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

void abort(void) __attribute__((noreturn));
int abs(int) __attribute__((const));
int atexit(void (* )(void));
double atof(const char *);
int atoi(const char *);
long atol(const char *);
long long
atoll(const char *);
void *bsearch(const void *__key, const void *__base, size_t __nel,
        size_t __width, int (* __compar)(const void *, const void *));
void *calloc(size_t __count, size_t __size) __attribute__((__warn_unused_result__)) __attribute__((alloc_size(1,2)));
div_t div(int, int) __attribute__((const));
void exit(int) __attribute__((noreturn));
void free(void *);
char *getenv(const char *);
long labs(long) __attribute__((const));
ldiv_t ldiv(long, long) __attribute__((const));
long long
llabs(long long);
lldiv_t lldiv(long long, long long);
void *malloc(size_t __size) __attribute__((__warn_unused_result__)) __attribute__((alloc_size(1)));
int mblen(const char *__s, size_t __n);
size_t mbstowcs(wchar_t * restrict , const char * restrict, size_t);
int mbtowc(wchar_t * restrict, const char * restrict, size_t);
int posix_memalign(void **__memptr, size_t __alignment, size_t __size) ;
void qsort(void *__base, size_t __nel, size_t __width,
        int (* __compar)(const void *, const void *));
int rand(void) ;
void *realloc(void *__ptr, size_t __size) __attribute__((__warn_unused_result__)) __attribute__((alloc_size(2)));
void srand(unsigned) ;
double strtod(const char *, char **) __asm("_" "strtod" );
float strtof(const char *, char **) __asm("_" "strtof" );
long strtol(const char *__str, char **__endptr, int __base);
long double
strtold(const char *, char **);
long long
strtoll(const char *__str, char **__endptr, int __base);
unsigned long
strtoul(const char *__str, char **__endptr, int __base);
unsigned long long
strtoull(const char *__str, char **__endptr, int __base);



int system(const char *) __asm("_" "system" );
size_t wcstombs(char * restrict, const wchar_t * restrict, size_t);
int wctomb(char *, wchar_t);
void _Exit(int) __attribute__((noreturn));
long a64l(const char *);
double drand48(void);
char *ecvt(double, int, int *restrict, int *restrict);
double erand48(unsigned short[3]);
char *fcvt(double, int, int *restrict, int *restrict);
char *gcvt(double, int, char *);
int getsubopt(char **, char * const *, char **);
int grantpt(int);
char *initstate(unsigned, char *, size_t);
long jrand48(unsigned short[3]) ;
char *l64a(long);
void lcong48(unsigned short[7]);
long lrand48(void) ;
char *mktemp(char *);
int mkstemp(char *);
long mrand48(void) ;
long nrand48(unsigned short[3]) ;
int posix_openpt(int);
char *ptsname(int);
int ptsname_r(int fildes, char *buffer, size_t buflen) ;
int putenv(char *) __asm("_" "putenv" );
long random(void) ;
int rand_r(unsigned *) ;
char *realpath(const char * restrict, char * restrict) __asm("_" "realpath" "$DARWIN_EXTSN");
unsigned short
*seed48(unsigned short[3]);
int setenv(const char * __name, const char * __value, int __overwrite) __asm("_" "setenv" );
void setkey(const char *) __asm("_" "setkey" );
char *setstate(const char *);
void srand48(long);
void srandom(unsigned);
int unlockpt(int);
int unsetenv(const char *) __asm("_" "unsetenv" );
typedef __darwin_dev_t dev_t;
typedef __darwin_mode_t mode_t;
uint32_t arc4random(void);
void arc4random_addrandom(unsigned char * , int )



    ;
    void arc4random_buf(void * __buf, size_t __nbytes) ;
    void arc4random_stir(void);
    uint32_t
    arc4random_uniform(uint32_t __upper_bound) ;
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
    int daemon(int, int) __asm("_" "daemon" "$1050") __attribute__((deprecated)) ;
    char *devname(dev_t, mode_t);
    char *devname_r(dev_t, mode_t, char *buf, int len);
    char *getbsize(int *, long *);
    int getloadavg(double [], int);
    const char
    *getprogname(void);
    int heapsort(void *__base, size_t __nel, size_t __width,
            int (* __compar)(const void *, const void *));
int mergesort(void *__base, size_t __nel, size_t __width,
        int (* __compar)(const void *, const void *));
void psort(void *__base, size_t __nel, size_t __width,
        int (* __compar)(const void *, const void *)) ;
void psort_r(void *__base, size_t __nel, size_t __width, void *,
        int (* __compar)(void *, const void *, const void *)) ;
void qsort_r(void *__base, size_t __nel, size_t __width, void *,
        int (* __compar)(void *, const void *, const void *));
int radixsort(const unsigned char **__base, int __nel, const unsigned char *__table,
        unsigned __endbyte);
void setprogname(const char *);
int sradixsort(const unsigned char **__base, int __nel, const unsigned char *__table,
        unsigned __endbyte);
void sranddev(void);
void srandomdev(void);
void *reallocf(void *__ptr, size_t __size) __attribute__((alloc_size(2)));
long long
strtoq(const char *__str, char **__endptr, int __base);
unsigned long long
strtouq(const char *__str, char **__endptr, int __base);
extern char *suboptarg;
void *valloc(size_t) __attribute__((alloc_size(1)));


typedef struct fd_set {
    __int32_t fds_bits[((((1024) % ((sizeof(__int32_t) * 8))) == 0) ? ((1024) / ((sizeof(__int32_t) * 8))) : (((1024) / ((sizeof(__int32_t) * 8))) + 1))];
} fd_set;

    static __inline int
__darwin_fd_isset(int _n, const struct fd_set *_p)
{
    return (_p->fds_bits[(unsigned long)_n/(sizeof(__int32_t) * 8)] & ((__int32_t)(((unsigned long)1)<<((unsigned long)_n % (sizeof(__int32_t) * 8)))));
}
struct timespec
{
    __darwin_time_t tv_sec;
    long tv_nsec;
};
struct timeval64
{
    __int64_t tv_sec;
    __int64_t tv_usec;
};
typedef __darwin_time_t time_t;
typedef __darwin_suseconds_t suseconds_t;
struct itimerval {
    struct timeval it_interval;
    struct timeval it_value;
};
struct timezone {
    int tz_minuteswest;
    int tz_dsttime;
};
struct clockinfo {
    int hz;
    int tick;
    int tickadj;
    int stathz;
    int profhz;
};
typedef __darwin_clock_t clock_t;
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
};
extern char *tzname[];
extern int getdate_err;
extern long timezone __asm("_" "timezone" );
extern int daylight;

char *asctime(const struct tm *);
clock_t clock(void) __asm("_" "clock" );
char *ctime(const time_t *);
double difftime(time_t, time_t);
struct tm *getdate(const char *);
struct tm *gmtime(const time_t *);
struct tm *localtime(const time_t *);
time_t mktime(struct tm *) __asm("_" "mktime" );
size_t strftime(char * restrict, size_t, const char * restrict, const struct tm * restrict) __asm("_" "strftime" );
char *strptime(const char * restrict, const char * restrict, struct tm * restrict) __asm("_" "strptime" );
time_t time(time_t *);
void tzset(void);
char *asctime_r(const struct tm * restrict, char * restrict);
char *ctime_r(const time_t *, char *);
struct tm *gmtime_r(const time_t * restrict, struct tm * restrict);
struct tm *localtime_r(const time_t * restrict, struct tm * restrict);
time_t posix2time(time_t);
void tzsetwall(void);
time_t time2posix(time_t);
time_t timelocal(struct tm * const);
time_t timegm(struct tm * const);
int nanosleep(const struct timespec *__rqtp, struct timespec *__rmtp) __asm("_" "nanosleep" );
typedef enum {
    _CLOCK_REALTIME = 0,
    _CLOCK_MONOTONIC = 6,
    _CLOCK_MONOTONIC_RAW = 4,
    _CLOCK_MONOTONIC_RAW_APPROX = 5,
    _CLOCK_UPTIME_RAW = 8,
    _CLOCK_UPTIME_RAW_APPROX = 9,
    _CLOCK_PROCESS_CPUTIME_ID = 12,
    _CLOCK_THREAD_CPUTIME_ID = 16
} clockid_t;

int clock_getres(clockid_t __clock_id, struct timespec *__res);

int clock_gettime(clockid_t __clock_id, struct timespec *__tp);

__uint64_t clock_gettime_nsec_np(clockid_t __clock_id);


int clock_settime(clockid_t __clock_id, const struct timespec *__tp);


int adjtime(const struct timeval *, struct timeval *);
int futimes(int, const struct timeval *);
int lutimes(const char *, const struct timeval *) ;
int settimeofday(const struct timeval *, const struct timezone *);
int getitimer(int, struct itimerval *);
int gettimeofday(struct timeval * restrict, void * restrict);
int select(int, fd_set * restrict, fd_set * restrict,
        fd_set * restrict, struct timeval * restrict)
    __asm("_" "select" "$1050")
    ;
    int setitimer(int, const struct itimerval * restrict,
            struct itimerval * restrict);
int utimes(const char *, const struct timeval *);


typedef float float_t;
typedef double double_t;
extern int __math_errhandling(void);
extern int __fpclassifyf(float);
extern int __fpclassifyd(double);
extern int __fpclassifyl(long double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isfinitef(float);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isfinited(double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isfinitel(long double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isinff(float);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isinfd(double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isinfl(long double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnanf(float);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnand(double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnanl(long double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnormalf(float);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnormald(double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnormall(long double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_signbitf(float);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_signbitd(double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_signbitl(long double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isfinitef(float __x) {
    return __x == __x && __builtin_fabsf(__x) != __builtin_inff();
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isfinited(double __x) {
    return __x == __x && __builtin_fabs(__x) != __builtin_inf();
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isfinitel(long double __x) {
    return __x == __x && __builtin_fabsl(__x) != __builtin_infl();
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isinff(float __x) {
    return __builtin_fabsf(__x) == __builtin_inff();
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isinfd(double __x) {
    return __builtin_fabs(__x) == __builtin_inf();
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isinfl(long double __x) {
    return __builtin_fabsl(__x) == __builtin_infl();
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnanf(float __x) {
    return __x != __x;
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnand(double __x) {
    return __x != __x;
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnanl(long double __x) {
    return __x != __x;
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_signbitf(float __x) {
    union { float __f; unsigned int __u; } __u;
    __u.__f = __x;
    return (int)(__u.__u >> 31);
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_signbitd(double __x) {
    union { double __f; unsigned long long __u; } __u;
    __u.__f = __x;
    return (int)(__u.__u >> 63);
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_signbitl(long double __x) {
    union {
        long double __ld;
        struct{ unsigned long long __m; unsigned short __sexp; } __p;
    } __u;
    __u.__ld = __x;
    return (int)(__u.__p.__sexp >> 15);
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnormalf(float __x) {
    return __inline_isfinitef(__x) && __builtin_fabsf(__x) >= 1.17549435082228750797e-38F;
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnormald(double __x) {
    return __inline_isfinited(__x) && __builtin_fabs(__x) >= ((double)2.22507385850720138309e-308L);
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) int __inline_isnormall(long double __x) {
    return __inline_isfinitel(__x) && __builtin_fabsl(__x) >= 3.36210314311209350626e-4932L;
}
extern float acosf(float);
extern double acos(double);
extern long double acosl(long double);
extern float asinf(float);
extern double asin(double);
extern long double asinl(long double);
extern float atanf(float);
extern double atan(double);
extern long double atanl(long double);
extern float atan2f(float, float);
extern double atan2(double, double);
extern long double atan2l(long double, long double);
extern float cosf(float);
extern double cos(double);
extern long double cosl(long double);
extern float sinf(float);
extern double sin(double);
extern long double sinl(long double);
extern float tanf(float);
extern double tan(double);
extern long double tanl(long double);
extern float acoshf(float);
extern double acosh(double);
extern long double acoshl(long double);
extern float asinhf(float);
extern double asinh(double);
extern long double asinhl(long double);
extern float atanhf(float);
extern double atanh(double);
extern long double atanhl(long double);
extern float coshf(float);
extern double cosh(double);
extern long double coshl(long double);
extern float sinhf(float);
extern double sinh(double);
extern long double sinhl(long double);
extern float tanhf(float);
extern double tanh(double);
extern long double tanhl(long double);
extern float expf(float);
extern double exp(double);
extern long double expl(long double);
extern float exp2f(float);
extern double exp2(double);
extern long double exp2l(long double);
extern float expm1f(float);
extern double expm1(double);
extern long double expm1l(long double);
extern float logf(float);
extern double log(double);
extern long double logl(long double);
extern float log10f(float);
extern double log10(double);
extern long double log10l(long double);
extern float log2f(float);
extern double log2(double);
extern long double log2l(long double);
extern float log1pf(float);
extern double log1p(double);
extern long double log1pl(long double);
extern float logbf(float);
extern double logb(double);
extern long double logbl(long double);
extern float modff(float, float *);
extern double modf(double, double *);
extern long double modfl(long double, long double *);
extern float ldexpf(float, int);
extern double ldexp(double, int);
extern long double ldexpl(long double, int);
extern float frexpf(float, int *);
extern double frexp(double, int *);
extern long double frexpl(long double, int *);
extern int ilogbf(float);
extern int ilogb(double);
extern int ilogbl(long double);
extern float scalbnf(float, int);
extern double scalbn(double, int);
extern long double scalbnl(long double, int);
extern float scalblnf(float, long int);
extern double scalbln(double, long int);
extern long double scalblnl(long double, long int);
extern float fabsf(float);
extern double fabs(double);
extern long double fabsl(long double);
extern float cbrtf(float);
extern double cbrt(double);
extern long double cbrtl(long double);
extern float hypotf(float, float);
extern double hypot(double, double);
extern long double hypotl(long double, long double);
extern float powf(float, float);
extern double pow(double, double);
extern long double powl(long double, long double);
extern float sqrtf(float);
extern double sqrt(double);
extern long double sqrtl(long double);
extern float erff(float);
extern double erf(double);
extern long double erfl(long double);
extern float erfcf(float);
extern double erfc(double);
extern long double erfcl(long double);
extern float lgammaf(float);
extern double lgamma(double);
extern long double lgammal(long double);
extern float tgammaf(float);
extern double tgamma(double);
extern long double tgammal(long double);
extern float ceilf(float);
extern double ceil(double);
extern long double ceill(long double);
extern float floorf(float);
extern double floor(double);
extern long double floorl(long double);
extern float nearbyintf(float);
extern double nearbyint(double);
extern long double nearbyintl(long double);
extern float rintf(float);
extern double rint(double);
extern long double rintl(long double);
extern long int lrintf(float);
extern long int lrint(double);
extern long int lrintl(long double);
extern float roundf(float);
extern double round(double);
extern long double roundl(long double);
extern long int lroundf(float);
extern long int lround(double);
extern long int lroundl(long double);
extern long long int llrintf(float);
extern long long int llrint(double);
extern long long int llrintl(long double);
extern long long int llroundf(float);
extern long long int llround(double);
extern long long int llroundl(long double);
extern float truncf(float);
extern double trunc(double);
extern long double truncl(long double);
extern float fmodf(float, float);
extern double fmod(double, double);
extern long double fmodl(long double, long double);
extern float remainderf(float, float);
extern double remainder(double, double);
extern long double remainderl(long double, long double);
extern float remquof(float, float, int *);
extern double remquo(double, double, int *);
extern long double remquol(long double, long double, int *);
extern float copysignf(float, float);
extern double copysign(double, double);
extern long double copysignl(long double, long double);
extern float nanf(const char *);
extern double nan(const char *);
extern long double nanl(const char *);
extern float nextafterf(float, float);
extern double nextafter(double, double);
extern long double nextafterl(long double, long double);
extern double nexttoward(double, long double);
extern float nexttowardf(float, long double);
extern long double nexttowardl(long double, long double);
extern float fdimf(float, float);
extern double fdim(double, double);
extern long double fdiml(long double, long double);
extern float fmaxf(float, float);
extern double fmax(double, double);
extern long double fmaxl(long double, long double);
extern float fminf(float, float);
extern double fmin(double, double);
extern long double fminl(long double, long double);
extern float fmaf(float, float, float);
extern double fma(double, double, double);
extern long double fmal(long double, long double, long double);
extern float __inff(void) __attribute__((deprecated));
extern double __inf(void) __attribute__((deprecated));
extern long double __infl(void) __attribute__((deprecated));
extern float __nan(void) ;
extern float __exp10f(float) ;
extern double __exp10(double) ;
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) void __sincosf(float __x, float *__sinp, float *__cosp);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) void __sincos(double __x, double *__sinp, double *__cosp);
extern float __cospif(float) ;
extern double __cospi(double) ;
extern float __sinpif(float) ;
extern double __sinpi(double) ;
extern float __tanpif(float) ;
extern double __tanpi(double) ;
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) void __sincospif(float __x, float *__sinp, float *__cosp);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) void __sincospi(double __x, double *__sinp, double *__cosp);
struct __float2 { float __sinval; float __cosval; };
struct __double2 { double __sinval; double __cosval; };
extern struct __float2 __sincosf_stret(float);
extern struct __double2 __sincos_stret(double);
extern struct __float2 __sincospif_stret(float);
extern struct __double2 __sincospi_stret(double);
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) void __sincosf(float __x, float *__sinp, float *__cosp) {
    const struct __float2 __stret = __sincosf_stret(__x);
    *__sinp = __stret.__sinval; *__cosp = __stret.__cosval;
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) void __sincos(double __x, double *__sinp, double *__cosp) {
    const struct __double2 __stret = __sincos_stret(__x);
    *__sinp = __stret.__sinval; *__cosp = __stret.__cosval;
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) void __sincospif(float __x, float *__sinp, float *__cosp) {
    const struct __float2 __stret = __sincospif_stret(__x);
    *__sinp = __stret.__sinval; *__cosp = __stret.__cosval;
}
extern __inline __attribute__((__gnu_inline__)) __attribute__ ((__always_inline__)) void __sincospi(double __x, double *__sinp, double *__cosp) {
    const struct __double2 __stret = __sincospi_stret(__x);
    *__sinp = __stret.__sinval; *__cosp = __stret.__cosval;
}
extern double j0(double) ;
extern double j1(double) ;
extern double jn(int, double) ;
extern double y0(double) ;
extern double y1(double) ;
extern double yn(int, double) ;
extern double scalb(double, double);
extern int signgam;
extern long int rinttol(double) __attribute__((deprecated));
extern long int roundtol(double) __attribute__((deprecated));
extern double drem(double, double) __attribute__((deprecated));
extern int finite(double) __attribute__((deprecated));
extern double gamma(double) __attribute__((deprecated));
extern double significand(double) __attribute__((deprecated));
struct exception {
    int type;
    char *name;
    double arg1;
    double arg2;
    double retval;
};
extern int matherr(struct exception *) __attribute__((deprecated));


void __assert_rtn(const char *, const char *, int, const char *) __attribute__((noreturn)) ;

typedef struct
{
    unsigned char _x[64]
        __attribute__((__aligned__(8)));
} omp_lock_t;
typedef struct
{
    unsigned char _x[80]
        __attribute__((__aligned__(8)));
} omp_nest_lock_t;
typedef enum omp_sched_t
{
    omp_sched_static = 1,
    omp_sched_dynamic = 2,
    omp_sched_guided = 3,
    omp_sched_auto = 4
} omp_sched_t;
typedef enum omp_proc_bind_t
{
    omp_proc_bind_false = 0,
    omp_proc_bind_true = 1,
    omp_proc_bind_master = 2,
    omp_proc_bind_close = 3,
    omp_proc_bind_spread = 4
} omp_proc_bind_t;
typedef enum omp_lock_hint_t
{
    omp_lock_hint_none = 0,
    omp_lock_hint_uncontended = 1,
    omp_lock_hint_contended = 2,
    omp_lock_hint_nonspeculative = 4,
    omp_lock_hint_speculative = 8
} omp_lock_hint_t;
extern void omp_set_num_threads (int) __attribute__((__nothrow__));
extern int omp_get_num_threads (void) __attribute__((__nothrow__));
extern int omp_get_max_threads (void) __attribute__((__nothrow__));
extern int omp_get_thread_num (void) __attribute__((__nothrow__));
extern int omp_get_num_procs (void) __attribute__((__nothrow__));
extern int omp_in_parallel (void) __attribute__((__nothrow__));
extern void omp_set_dynamic (int) __attribute__((__nothrow__));
extern int omp_get_dynamic (void) __attribute__((__nothrow__));
extern void omp_set_nested (int) __attribute__((__nothrow__));
extern int omp_get_nested (void) __attribute__((__nothrow__));
extern void omp_init_lock (omp_lock_t *) __attribute__((__nothrow__));
extern void omp_init_lock_with_hint (omp_lock_t *, omp_lock_hint_t)
    __attribute__((__nothrow__));
    extern void omp_destroy_lock (omp_lock_t *) __attribute__((__nothrow__));
    extern void omp_set_lock (omp_lock_t *) __attribute__((__nothrow__));
    extern void omp_unset_lock (omp_lock_t *) __attribute__((__nothrow__));
    extern int omp_test_lock (omp_lock_t *) __attribute__((__nothrow__));
    extern void omp_init_nest_lock (omp_nest_lock_t *) __attribute__((__nothrow__));
extern void omp_init_nest_lock_with_hint (omp_lock_t *, omp_lock_hint_t)
    __attribute__((__nothrow__));
    extern void omp_destroy_nest_lock (omp_nest_lock_t *) __attribute__((__nothrow__));
    extern void omp_set_nest_lock (omp_nest_lock_t *) __attribute__((__nothrow__));
    extern void omp_unset_nest_lock (omp_nest_lock_t *) __attribute__((__nothrow__));
    extern int omp_test_nest_lock (omp_nest_lock_t *) __attribute__((__nothrow__));
    extern double omp_get_wtime (void) __attribute__((__nothrow__));
    extern double omp_get_wtick (void) __attribute__((__nothrow__));
    extern void omp_set_schedule (omp_sched_t, int) __attribute__((__nothrow__));
    extern void omp_get_schedule (omp_sched_t *, int *) __attribute__((__nothrow__));
    extern int omp_get_thread_limit (void) __attribute__((__nothrow__));
    extern void omp_set_max_active_levels (int) __attribute__((__nothrow__));
    extern int omp_get_max_active_levels (void) __attribute__((__nothrow__));
    extern int omp_get_level (void) __attribute__((__nothrow__));
    extern int omp_get_ancestor_thread_num (int) __attribute__((__nothrow__));
    extern int omp_get_team_size (int) __attribute__((__nothrow__));
    extern int omp_get_active_level (void) __attribute__((__nothrow__));
    extern int omp_in_final (void) __attribute__((__nothrow__));
    extern int omp_get_cancellation (void) __attribute__((__nothrow__));
    extern omp_proc_bind_t omp_get_proc_bind (void) __attribute__((__nothrow__));
    extern int omp_get_num_places (void) __attribute__((__nothrow__));
    extern int omp_get_place_num_procs (int) __attribute__((__nothrow__));
    extern void omp_get_place_proc_ids (int, int *) __attribute__((__nothrow__));
    extern int omp_get_place_num (void) __attribute__((__nothrow__));
    extern int omp_get_partition_num_places (void) __attribute__((__nothrow__));
    extern void omp_get_partition_place_nums (int *) __attribute__((__nothrow__));
    extern void omp_set_default_device (int) __attribute__((__nothrow__));
    extern int omp_get_default_device (void) __attribute__((__nothrow__));
    extern int omp_get_num_devices (void) __attribute__((__nothrow__));
    extern int omp_get_num_teams (void) __attribute__((__nothrow__));
    extern int omp_get_team_num (void) __attribute__((__nothrow__));
    extern int omp_is_initial_device (void) __attribute__((__nothrow__));
    extern int omp_get_initial_device (void) __attribute__((__nothrow__));
    extern int omp_get_max_task_priority (void) __attribute__((__nothrow__));
    extern void *omp_target_alloc (long unsigned int, int) __attribute__((__nothrow__));
    extern void omp_target_free (void *, int) __attribute__((__nothrow__));
    extern int omp_target_is_present (void *, int) __attribute__((__nothrow__));
    extern int omp_target_memcpy (void *, void *, long unsigned int, long unsigned int,
            long unsigned int, int, int) __attribute__((__nothrow__));
extern int omp_target_memcpy_rect (void *, void *, long unsigned int, int,
        const long unsigned int *,
        const long unsigned int *,
        const long unsigned int *,
        const long unsigned int *,
        const long unsigned int *, int, int)
    __attribute__((__nothrow__));
    extern int omp_target_associate_ptr (void *, void *, long unsigned int,
            long unsigned int, int) __attribute__((__nothrow__));
extern int omp_target_disassociate_ptr (void *, int) __attribute__((__nothrow__));
int hypre_OutOfMemory( int size );
char *hypre_MAlloc( int size );
char *hypre_CAlloc( int count , int elt_size );
char *hypre_ReAlloc( char *ptr , int size );
void hypre_Free( char *ptr );
char *hypre_SharedMAlloc( int size );
char *hypre_SharedCAlloc( int count , int elt_size );
char *hypre_SharedReAlloc( char *ptr , int size );
void hypre_SharedFree( char *ptr );
double *hypre_IncrementSharedDataPtr( double *ptr , int size );
extern int hypre__global_error;
void hypre_error_handler(char *filename, int line, int ierr);
int HYPRE_GetError();
int HYPRE_CheckError(int hypre_ierr, int hypre_error_code);
int HYPRE_GetErrorArg();
void HYPRE_DescribeError(int hypre_ierr, char *descr);
typedef struct
{
    double *data;
    int *i;
    int *j;
    int num_rows;
    int num_cols;
    int num_nonzeros;
    int *rownnz;
    int num_rownnz;
    int owns_data;
} hypre_CSRMatrix;
typedef struct
{
    double *data;
    int size;
    int owns_data;
    int num_vectors;
    int multivec_storage_method;
    int vecstride, idxstride;
} hypre_Vector;
hypre_CSRMatrix *hypre_CSRMatrixCreate ( int num_rows , int num_cols , int num_nonzeros );
int hypre_CSRMatrixDestroy ( hypre_CSRMatrix *matrix );
int hypre_CSRMatrixInitialize ( hypre_CSRMatrix *matrix );
int hypre_CSRMatrixSetDataOwner ( hypre_CSRMatrix *matrix , int owns_data );
int hypre_CSRMatrixSetRownnz ( hypre_CSRMatrix *matrix );
hypre_CSRMatrix *hypre_CSRMatrixRead ( char *file_name );
int hypre_CSRMatrixPrint ( hypre_CSRMatrix *matrix , char *file_name );
int hypre_CSRMatrixCopy ( hypre_CSRMatrix *A , hypre_CSRMatrix *B , int copy_data );
hypre_CSRMatrix *hypre_CSRMatrixClone ( hypre_CSRMatrix *A );
hypre_CSRMatrix *hypre_CSRMatrixUnion ( hypre_CSRMatrix *A , hypre_CSRMatrix *B , int *col_map_offd_A , int *col_map_offd_B , int **col_map_offd_C );
int hypre_CSRMatrixMatvec ( double alpha , hypre_CSRMatrix *A , hypre_Vector *x , double beta , hypre_Vector *y );
int hypre_CSRMatrixMatvecT ( double alpha , hypre_CSRMatrix *A , hypre_Vector *x , double beta , hypre_Vector *y );
int hypre_CSRMatrixMatvec_FF( double alpha , hypre_CSRMatrix *A , hypre_Vector *x , double beta , hypre_Vector *y , int *CF_marker_x , int *CF_marker_y , int fpt );
hypre_Vector *hypre_SeqVectorCreate ( int size );
hypre_Vector *hypre_SeqMultiVectorCreate ( int size , int num_vectors );
int hypre_SeqVectorDestroy ( hypre_Vector *vector );
int hypre_SeqVectorInitialize ( hypre_Vector *vector );
int hypre_SeqVectorSetDataOwner ( hypre_Vector *vector , int owns_data );
hypre_Vector *hypre_SeqVectorRead ( char *file_name );
int hypre_SeqVectorPrint ( hypre_Vector *vector , char *file_name );
int hypre_SeqVectorSetConstantValues ( hypre_Vector *v , double value );
int hypre_SeqVectorCopy ( hypre_Vector *x , hypre_Vector *y );
hypre_Vector *hypre_SeqVectorCloneDeep ( hypre_Vector *x );
hypre_Vector *hypre_SeqVectorCloneShallow ( hypre_Vector *x );
int hypre_SeqVectorScale ( double alpha , hypre_Vector *y );
int hypre_SeqVectorAxpy ( double alpha , hypre_Vector *x , hypre_Vector *y );
double hypre_SeqVectorInnerProd ( hypre_Vector *x , hypre_Vector *y );
double hypre_VectorSumElts ( hypre_Vector *vector );
hypre_CSRMatrix *GenerateSeqLaplacian(int nx, int ny , int nz , double *values , hypre_Vector **x_ptr, hypre_Vector **y_ptr, hypre_Vector **sol_ptr) ;
int hypre_BoomerAMGSeqRelax( hypre_CSRMatrix *A , hypre_Vector *x, hypre_Vector *y) ;
    hypre_CSRMatrix *
hypre_CSRMatrixCreate( int num_rows,
        int num_cols,
        int num_nonzeros )
{
    hypre_CSRMatrix *matrix;
    matrix = ( (hypre_CSRMatrix *)hypre_CAlloc((unsigned int)(1), (unsigned int)sizeof(hypre_CSRMatrix)) );
    ((matrix) -> data) = ((void *)0);
    ((matrix) -> i) = ((void *)0);
    ((matrix) -> j) = ((void *)0);
    ((matrix) -> rownnz) = ((void *)0);
    ((matrix) -> num_rows) = num_rows;
    ((matrix) -> num_cols) = num_cols;
    ((matrix) -> num_nonzeros) = num_nonzeros;
    ((matrix) -> owns_data) = 1;
    ((matrix) -> num_rownnz) = num_rows;
    return matrix;
}
    int
hypre_CSRMatrixDestroy( hypre_CSRMatrix *matrix )
{
    int ierr=0;
    if (matrix)
    {
        ( hypre_Free((char *)((matrix) -> i)), ((matrix) -> i) = ((void *)0) );
        if (((matrix) -> rownnz))
            ( hypre_Free((char *)((matrix) -> rownnz)), ((matrix) -> rownnz) = ((void *)0) );
        if ( ((matrix) -> owns_data) )
        {
            ( hypre_Free((char *)((matrix) -> data)), ((matrix) -> data) = ((void *)0) );
            ( hypre_Free((char *)((matrix) -> j)), ((matrix) -> j) = ((void *)0) );
        }
        ( hypre_Free((char *)matrix), matrix = ((void *)0) );
    }
    return ierr;
}
    int
hypre_CSRMatrixInitialize( hypre_CSRMatrix *matrix )
{
    int num_rows = ((matrix) -> num_rows);
    int num_nonzeros = ((matrix) -> num_nonzeros);
    int ierr=0;
    if ( ! ((matrix) -> data) && num_nonzeros )
        ((matrix) -> data) = ( (double *)hypre_CAlloc((unsigned int)(num_nonzeros), (unsigned int)sizeof(double)) );
    if ( ! ((matrix) -> i) )
        ((matrix) -> i) = ( (int *)hypre_CAlloc((unsigned int)(num_rows + 1), (unsigned int)sizeof(int)) );
    if ( ! ((matrix) -> j) && num_nonzeros )
        ((matrix) -> j) = ( (int *)hypre_CAlloc((unsigned int)(num_nonzeros), (unsigned int)sizeof(int)) );
    return ierr;
}
    int
hypre_CSRMatrixSetDataOwner( hypre_CSRMatrix *matrix,
        int owns_data )
{
    int ierr=0;
    ((matrix) -> owns_data) = owns_data;
    return ierr;
}
    int
hypre_CSRMatrixSetRownnz( hypre_CSRMatrix *matrix )
{
    int ierr=0;
    int num_rows = ((matrix) -> num_rows);
    int *A_i = ((matrix) -> i);
    int *Arownnz;
    int i, adiag;
    int irownnz=0;
    for (i=0; i < num_rows; i++)
    {
        adiag = (A_i[i+1] - A_i[i]);
        if(adiag > 0) irownnz++;
    }
    ((matrix) -> num_rownnz) = irownnz;
    if ((irownnz == 0) || (irownnz == num_rows))
    {
        ((matrix) -> rownnz) = ((void *)0);
    }
    else
    {
        Arownnz = ( (int *)hypre_CAlloc((unsigned int)(irownnz), (unsigned int)sizeof(int)) );
        irownnz = 0;
        for (i=0; i < num_rows; i++)
        {
            adiag = A_i[i+1]-A_i[i];
            if(adiag > 0) Arownnz[irownnz++] = i;
        }
        ((matrix) -> rownnz) = Arownnz;
    }
    return ierr;
}
    hypre_CSRMatrix *
hypre_CSRMatrixRead( char *file_name )
{
    hypre_CSRMatrix *matrix;
    FILE *fp;
    double *matrix_data;
    int *matrix_i;
    int *matrix_j;
    int num_rows;
    int num_nonzeros;
    int max_col = 0;
    int file_base = 1;
    int j;
    fp = fopen(file_name, "r");
    fscanf(fp, "%d", &num_rows);
    matrix_i = ( (int *)hypre_CAlloc((unsigned int)(num_rows + 1), (unsigned int)sizeof(int)) );
    for (j = 0; j < num_rows+1; j++)
    {
        fscanf(fp, "%d", &matrix_i[j]);
        matrix_i[j] -= file_base;
    }
    num_nonzeros = matrix_i[num_rows];
    matrix = hypre_CSRMatrixCreate(num_rows, num_rows, matrix_i[num_rows]);
    ((matrix) -> i) = matrix_i;
    hypre_CSRMatrixInitialize(matrix);
    matrix_j = ((matrix) -> j);
    for (j = 0; j < num_nonzeros; j++)
    {
        fscanf(fp, "%d", &matrix_j[j]);
        matrix_j[j] -= file_base;
        if (matrix_j[j] > max_col)
        {
            max_col = matrix_j[j];
        }
    }
    matrix_data = ((matrix) -> data);
    for (j = 0; j < matrix_i[num_rows]; j++)
    {
        fscanf(fp, "%le", &matrix_data[j]);
    }
    fclose(fp);
    ((matrix) -> num_nonzeros) = num_nonzeros;
    ((matrix) -> num_cols) = ++max_col;
    return matrix;
}
    int
hypre_CSRMatrixPrint( hypre_CSRMatrix *matrix,
        char *file_name )
{
    FILE *fp;
    double *matrix_data;
    int *matrix_i;
    int *matrix_j;
    int num_rows;
    int file_base = 1;
    int j;
    int ierr = 0;
    matrix_data = ((matrix) -> data);
    matrix_i = ((matrix) -> i);
    matrix_j = ((matrix) -> j);
    num_rows = ((matrix) -> num_rows);
    fp = fopen(file_name, "w");
    fprintf(fp, "%d\n", num_rows);
    for (j = 0; j <= num_rows; j++)
    {
        fprintf(fp, "%d\n", matrix_i[j] + file_base);
    }
    for (j = 0; j < matrix_i[num_rows]; j++)
    {
        fprintf(fp, "%d\n", matrix_j[j] + file_base);
    }
    if (matrix_data)
    {
        for (j = 0; j < matrix_i[num_rows]; j++)
        {
            fprintf(fp, "%.14e\n", matrix_data[j]);
        }
    }
    else
    {
        fprintf(fp, "Warning: No matrix data!\n");
    }
    fclose(fp);
    return ierr;
}
    int
hypre_CSRMatrixCopy( hypre_CSRMatrix *A, hypre_CSRMatrix *B, int copy_data )
{
    int ierr=0;
    int num_rows = ((A) -> num_rows);
    int *A_i = ((A) -> i);
    int *A_j = ((A) -> j);
    double *A_data;
    int *B_i = ((B) -> i);
    int *B_j = ((B) -> j);
    double *B_data;
    int i, j;
    for (i=0; i < num_rows; i++)
    {
        B_i[i] = A_i[i];
        for (j=A_i[i]; j < A_i[i+1]; j++)
        {
            B_j[j] = A_j[j];
        }
    }
    B_i[num_rows] = A_i[num_rows];
    if (copy_data)
    {
        A_data = ((A) -> data);
        B_data = ((B) -> data);
        for (i=0; i < num_rows; i++)
        {
            for (j=A_i[i]; j < A_i[i+1]; j++)
            {
                B_data[j] = A_data[j];
            }
        }
    }
    return ierr;
}
hypre_CSRMatrix * hypre_CSRMatrixClone( hypre_CSRMatrix * A )
{
    int num_rows = ((A) -> num_rows);
    int num_cols = ((A) -> num_cols);
    int num_nonzeros = ((A) -> num_nonzeros);
    hypre_CSRMatrix * B = hypre_CSRMatrixCreate( num_rows, num_cols, num_nonzeros );
    int * A_i;
    int * A_j;
    int * B_i;
    int * B_j;
    int i, j;
    hypre_CSRMatrixInitialize( B );
    A_i = ((A) -> i);
    A_j = ((A) -> j);
    B_i = ((B) -> i);
    B_j = ((B) -> j);
    for ( i=0; i<num_rows+1; ++i ) B_i[i] = A_i[i];
    for ( j=0; j<num_nonzeros; ++j ) B_j[j] = A_j[j];
    ((B) -> num_rownnz) = ((A) -> num_rownnz);
    if ( ((A) -> rownnz) ) hypre_CSRMatrixSetRownnz( B );
    return B;
}
hypre_CSRMatrix * hypre_CSRMatrixUnion(
        hypre_CSRMatrix * A, hypre_CSRMatrix * B,
        int * col_map_offd_A, int * col_map_offd_B, int ** col_map_offd_C )
{
    int num_rows = ((A) -> num_rows);
    int num_cols_A = ((A) -> num_cols);
    int num_cols_B = ((B) -> num_cols);
    int num_cols;
    int num_nonzeros;
    int * A_i = ((A) -> i);
    int * A_j = ((A) -> j);
    int * B_i = ((B) -> i);
    int * B_j = ((B) -> j);
    int * C_i;
    int * C_j;
    int * jC = ((void *)0);
    int i, jA, jB, jBg;
    int ma, mb, mc, ma_min, ma_max, match;
    hypre_CSRMatrix * C;
    if (!(num_rows == ((B) -> num_rows))) {fprintf(__stderrp,"hypre_assert failed: %s\n", "num_rows == hypre_CSRMatrixNumRows(B)"); hypre_error_handler("amgmk.c", 934, 1);};
    if ( col_map_offd_B ) if (!(col_map_offd_A)) {fprintf(__stderrp,"hypre_assert failed: %s\n", "col_map_offd_A"); hypre_error_handler("amgmk.c", 935, 1);};
    if ( col_map_offd_A ) if (!(col_map_offd_B)) {fprintf(__stderrp,"hypre_assert failed: %s\n", "col_map_offd_B"); hypre_error_handler("amgmk.c", 936, 1);};
    if ( col_map_offd_A==0 )
    {
        num_cols = (((num_cols_A)<(num_cols_B)) ? (num_cols_B) : (num_cols_A));
    }
    else
    {
        jC = ( (int *)hypre_CAlloc((unsigned int)(num_cols_B), (unsigned int)sizeof(int)) );
        num_cols = num_cols_A;
        for ( jB=0; jB<num_cols_B; ++jB )
        {
            match = 0;
            jBg = col_map_offd_B[jB];
            for ( ma=0; ma<num_cols_A; ++ma )
            {
                if ( col_map_offd_A[ma]==jBg )
                    match = 1;
            }
            if ( match==0 )
            {
                jC[jB] = num_cols;
                ++num_cols;
            }
        }
    }
    if ( col_map_offd_A )
    {
        *col_map_offd_C = ( (int *)hypre_CAlloc((unsigned int)(num_cols), (unsigned int)sizeof(int)) );
        for ( jA=0; jA<num_cols_A; ++jA )
            (*col_map_offd_C)[jA] = col_map_offd_A[jA];
        for ( jB=0; jB<num_cols_B; ++jB )
        {
            match = 0;
            jBg = col_map_offd_B[jB];
            for ( ma=0; ma<num_cols_A; ++ma )
            {
                if ( col_map_offd_A[ma]==jBg )
                    match = 1;
            }
            if ( match==0 )
                (*col_map_offd_C)[ jC[jB] ] = jBg;
        }
    }
    num_nonzeros = ((A) -> num_nonzeros);
    for ( i=0; i<num_rows; ++i )
    {
        ma_min = A_i[i]; ma_max = A_i[i+1];
        for ( mb=B_i[i]; mb<B_i[i+1]; ++mb )
        {
            jB = B_j[mb];
            if ( col_map_offd_B ) jB = col_map_offd_B[jB];
            match = 0;
            for ( ma=ma_min; ma<ma_max; ++ma )
            {
                jA = A_j[ma];
                if ( col_map_offd_A ) jA = col_map_offd_A[jA];
                if ( jB == jA )
                {
                    match = 1;
                    if( ma==ma_min ) ++ma_min;
                    break;
                }
            }
            if ( match==0 )
                ++num_nonzeros;
        }
    }
    C = hypre_CSRMatrixCreate( num_rows, num_cols, num_nonzeros );
    hypre_CSRMatrixInitialize( C );
    C_i = ((C) -> i);
    C_i[0] = 0;
    C_j = ((C) -> j);
    mc = 0;
    for ( i=0; i<num_rows; ++i )
    {
        ma_min = A_i[i]; ma_max = A_i[i+1];
        for ( ma=ma_min; ma<ma_max; ++ma )
        {
            C_j[mc] = A_j[ma];
            ++mc;
        }
        for ( mb=B_i[i]; mb<B_i[i+1]; ++mb )
        {
            jB = B_j[mb];
            if ( col_map_offd_B ) jB = col_map_offd_B[jB];
            match = 0;
            for ( ma=ma_min; ma<ma_max; ++ma )
            {
                jA = A_j[ma];
                if ( col_map_offd_A ) jA = col_map_offd_A[jA];
                if ( jB == jA )
                {
                    match = 1;
                    if( ma==ma_min ) ++ma_min;
                    break;
                }
            }
            if ( match==0 )
            {
                C_j[mc] = jC[ B_j[mb] ];
                ++mc;
            }
        }
        C_i[i+1] = mc;
    }
    if (!(mc == num_nonzeros)) {fprintf(__stderrp,"hypre_assert failed: %s\n", "mc == num_nonzeros"); hypre_error_handler("amgmk.c", 1058, 1);};
    if (jC) ( hypre_Free((char *)jC), jC = ((void *)0) );
    return C;
}
    int
hypre_CSRMatrixMatvec( double alpha,
        hypre_CSRMatrix *A,
        hypre_Vector *x,
        double beta,
        hypre_Vector *y )
{
    double *A_data = ((A) -> data);
    int *A_i = ((A) -> i);
    int *A_j = ((A) -> j);
    int num_rows = ((A) -> num_rows);
    int num_cols = ((A) -> num_cols);
    int *A_rownnz = ((A) -> rownnz);
    int num_rownnz = ((A) -> num_rownnz);
    double *x_data = ((x) -> data);
    double *y_data = ((y) -> data);
    int x_size = ((x) -> size);
    int y_size = ((y) -> size);
    int num_vectors = ((x) -> num_vectors);
    int idxstride_y = ((y) -> idxstride );
    int vecstride_y = ((y) -> vecstride );
    int idxstride_x = ((x) -> idxstride );
    int vecstride_x = ((x) -> vecstride );
    double temp, tempx;
    int i, j, jj;
    int m;
    double xpar=0.7;
    int ierr = 0;
    if (!(num_vectors == ((y) -> num_vectors))) {fprintf(__stderrp,"hypre_assert failed: %s\n", "num_vectors == hypre_VectorNumVectors(y)"); hypre_error_handler("amgmk.c", 1112, 1);};
    if (num_cols != x_size)
        ierr = 1;
    if (num_rows != y_size)
        ierr = 2;
    if (num_cols != x_size && num_rows != y_size)
        ierr = 3;
    if (alpha == 0.0)
    {
        for (i = 0; i < num_rows*num_vectors; i++)
            y_data[i] *= beta;
        return ierr;
    }
    temp = beta / alpha;
    if (temp != 1.0)
    {
        if (temp == 0.0)
        {
            for (i = 0; i < num_rows*num_vectors; i++)
                y_data[i] = 0.0;
        }
        else
        {
            for (i = 0; i < num_rows*num_vectors; i++)
                y_data[i] *= temp;
        }
    }
    if (num_rownnz < xpar*(num_rows))
    {
        for (i = 0; i < num_rownnz; i++)
        {
            m = A_rownnz[i];
            if ( num_vectors==1 )
            {
                tempx = y_data[m];
                for (jj = A_i[m]; jj < A_i[m+1]; jj++)
                    tempx += A_data[jj] * x_data[A_j[jj]];
                y_data[m] = tempx;
            }
            else
                for ( j=0; j<num_vectors; ++j )
                {
                    tempx = y_data[ j*vecstride_y + m*idxstride_y ];
                    for (jj = A_i[m]; jj < A_i[m+1]; jj++)
                        tempx += A_data[jj] * x_data[ j*vecstride_x + A_j[jj]*idxstride_x ];
                    y_data[ j*vecstride_y + m*idxstride_y] = tempx;
                }
        }
    }
    else
    {
#pragma omp parallel for private(i,jj,temp) schedule(static)
        for (i = 0; i < num_rows; i++)
        {
            if ( num_vectors==1 )
            {
                temp = y_data[i];
                for (jj = A_i[i]; jj < A_i[i+1]; jj++)
                    temp += A_data[jj] * x_data[A_j[jj]];
                y_data[i] = temp;
            }
            else
                for ( j=0; j<num_vectors; ++j )
                {
                    temp = y_data[ j*vecstride_y + i*idxstride_y ];
                    for (jj = A_i[i]; jj < A_i[i+1]; jj++)
                    {
                        temp += A_data[jj] * x_data[ j*vecstride_x + A_j[jj]*idxstride_x ];
                    }
                    y_data[ j*vecstride_y + i*idxstride_y ] = temp;
                }
        }
    }
    if (alpha != 1.0)
    {
        for (i = 0; i < num_rows*num_vectors; i++)
            y_data[i] *= alpha;
    }
    return ierr;
}
    int
hypre_CSRMatrixMatvecT( double alpha,
        hypre_CSRMatrix *A,
        hypre_Vector *x,
        double beta,
        hypre_Vector *y )
{
    double *A_data = ((A) -> data);
    int *A_i = ((A) -> i);
    int *A_j = ((A) -> j);
    int num_rows = ((A) -> num_rows);
    int num_cols = ((A) -> num_cols);
    double *x_data = ((x) -> data);
    double *y_data = ((y) -> data);
    int x_size = ((x) -> size);
    int y_size = ((y) -> size);
    int num_vectors = ((x) -> num_vectors);
    int idxstride_y = ((y) -> idxstride );
    int vecstride_y = ((y) -> vecstride );
    int idxstride_x = ((x) -> idxstride );
    int vecstride_x = ((x) -> vecstride );
    double temp;
    int i, i1, j, jv, jj, ns, ne, size, rest;
    int numThreads;
    int ierr = 0;
    if (!(num_vectors == ((y) -> num_vectors))) {fprintf(__stderrp,"hypre_assert failed: %s\n", "num_vectors == hypre_VectorNumVectors(y)"); hypre_error_handler("amgmk.c", 1279, 1);};
    if (num_rows != x_size)
        ierr = 1;
    if (num_cols != y_size)
        ierr = 2;
    if (num_rows != x_size && num_cols != y_size)
        ierr = 3;
    if (alpha == 0.0)
    {
        for (i = 0; i < num_cols*num_vectors; i++)
            y_data[i] *= beta;
        return ierr;
    }
    temp = beta / alpha;
    if (temp != 1.0)
    {
        if (temp == 0.0)
        {
            for (i = 0; i < num_cols*num_vectors; i++)
                y_data[i] = 0.0;
        }
        else
        {
            for (i = 0; i < num_cols*num_vectors; i++)
                y_data[i] *= temp;
        }
    }
    numThreads = 1;
    if (numThreads > 1)
    {
        for (i1 = 0; i1 < numThreads; i1++)
        {
            size = num_cols/numThreads;
            rest = num_cols - size*numThreads;
            if (i1 < rest)
            {
                ns = i1*size+i1-1;
                ne = (i1+1)*size+i1+1;
            }
            else
            {
                ns = i1*size+rest-1;
                ne = (i1+1)*size+rest;
            }
            if ( num_vectors==1 )
            {
                for (i = 0; i < num_rows; i++)
                {
                    for (jj = A_i[i]; jj < A_i[i+1]; jj++)
                    {
                        j = A_j[jj];
                        if (j > ns && j < ne)
                            y_data[j] += A_data[jj] * x_data[i];
                    }
                }
            }
            else
            {
                for (i = 0; i < num_rows; i++)
                {
                    for ( jv=0; jv<num_vectors; ++jv )
                    {
                        for (jj = A_i[i]; jj < A_i[i+1]; jj++)
                        {
                            j = A_j[jj];
                            if (j > ns && j < ne)
                                y_data[ j*idxstride_y + jv*vecstride_y ] +=
                                    A_data[jj] * x_data[ i*idxstride_x + jv*vecstride_x];
                        }
                    }
                }
            }
        }
    }
    else
    {
        for (i = 0; i < num_rows; i++)
        {
            if ( num_vectors==1 )
            {
                for (jj = A_i[i]; jj < A_i[i+1]; jj++)
                {
                    j = A_j[jj];
                    y_data[j] += A_data[jj] * x_data[i];
                }
            }
            else
            {
                for ( jv=0; jv<num_vectors; ++jv )
                {
                    for (jj = A_i[i]; jj < A_i[i+1]; jj++)
                    {
                        j = A_j[jj];
                        y_data[ j*idxstride_y + jv*vecstride_y ] +=
                            A_data[jj] * x_data[ i*idxstride_x + jv*vecstride_x ];
                    }
                }
            }
        }
    }
    if (alpha != 1.0)
    {
        for (i = 0; i < num_cols*num_vectors; i++)
            y_data[i] *= alpha;
    }
    return ierr;
}
    int
hypre_CSRMatrixMatvec_FF( double alpha,
        hypre_CSRMatrix *A,
        hypre_Vector *x,
        double beta,
        hypre_Vector *y,
        int *CF_marker_x,
        int *CF_marker_y,
        int fpt )
{
    double *A_data = ((A) -> data);
    int *A_i = ((A) -> i);
    int *A_j = ((A) -> j);
    int num_rows = ((A) -> num_rows);
    int num_cols = ((A) -> num_cols);
    double *x_data = ((x) -> data);
    double *y_data = ((y) -> data);
    int x_size = ((x) -> size);
    int y_size = ((y) -> size);
    double temp;
    int i, jj;
    int ierr = 0;
    if (num_cols != x_size)
        ierr = 1;
    if (num_rows != y_size)
        ierr = 2;
    if (num_cols != x_size && num_rows != y_size)
        ierr = 3;
    if (alpha == 0.0)
    {
        for (i = 0; i < num_rows; i++)
            if (CF_marker_x[i] == fpt) y_data[i] *= beta;
        return ierr;
    }
    temp = beta / alpha;
    if (temp != 1.0)
    {
        if (temp == 0.0)
        {
            for (i = 0; i < num_rows; i++)
                if (CF_marker_x[i] == fpt) y_data[i] = 0.0;
        }
        else
        {
            for (i = 0; i < num_rows; i++)
                if (CF_marker_x[i] == fpt) y_data[i] *= temp;
        }
    }
    for (i = 0; i < num_rows; i++)
    {
        if (CF_marker_x[i] == fpt)
        {
            temp = y_data[i];
            for (jj = A_i[i]; jj < A_i[i+1]; jj++)
                if (CF_marker_y[A_j[jj]] == fpt) temp += A_data[jj] * x_data[A_j[jj]];
            y_data[i] = temp;
        }
    }
    if (alpha != 1.0)
    {
        for (i = 0; i < num_rows; i++)
            if (CF_marker_x[i] == fpt) y_data[i] *= alpha;
    }
    return ierr;
}
    hypre_Vector *
hypre_SeqVectorCreate( int size )
{
    hypre_Vector *vector;
    vector = ( (hypre_Vector *)hypre_CAlloc((unsigned int)(1), (unsigned int)sizeof(hypre_Vector)) );
    ((vector) -> data) = ((void *)0);
    ((vector) -> size) = size;
    ((vector) -> num_vectors) = 1;
    ((vector) -> multivec_storage_method) = 0;
    ((vector) -> owns_data) = 1;
    return vector;
}
    hypre_Vector *
hypre_SeqMultiVectorCreate( int size, int num_vectors )
{
    hypre_Vector *vector = hypre_SeqVectorCreate(size);
    ((vector) -> num_vectors) = num_vectors;
    return vector;
}
    int
hypre_SeqVectorDestroy( hypre_Vector *vector )
{
    int ierr=0;
    if (vector)
    {
        if ( ((vector) -> owns_data) )
        {
            ( hypre_Free((char *)((vector) -> data)), ((vector) -> data) = ((void *)0) );
        }
        ( hypre_Free((char *)vector), vector = ((void *)0) );
    }
    return ierr;
}
    int
hypre_SeqVectorInitialize( hypre_Vector *vector )
{
    int size = ((vector) -> size);
    int ierr = 0;
    int num_vectors = ((vector) -> num_vectors);
    int multivec_storage_method = ((vector) -> multivec_storage_method);
    if ( ! ((vector) -> data) )
        ((vector) -> data) = ( (double *)hypre_CAlloc((unsigned int)(num_vectors*size), (unsigned int)sizeof(double)) );
    if ( multivec_storage_method == 0 )
    {
        ((vector) -> vecstride ) = size;
        ((vector) -> idxstride ) = 1;
    }
    else if ( multivec_storage_method == 1 )
    {
        ((vector) -> vecstride ) = 1;
        ((vector) -> idxstride ) = num_vectors;
    }
    else
        ++ierr;
    return ierr;
}
    int
hypre_SeqVectorSetDataOwner( hypre_Vector *vector,
        int owns_data )
{
    int ierr=0;
    ((vector) -> owns_data) = owns_data;
    return ierr;
}
    hypre_Vector *
hypre_SeqVectorRead( char *file_name )
{
    hypre_Vector *vector;
    FILE *fp;
    double *data;
    int size;
    int j;
    fp = fopen(file_name, "r");
    fscanf(fp, "%d", &size);
    vector = hypre_SeqVectorCreate(size);
    hypre_SeqVectorInitialize(vector);
    data = ((vector) -> data);
    for (j = 0; j < size; j++)
    {
        fscanf(fp, "%le", &data[j]);
    }
    fclose(fp);
    if (!(((vector) -> num_vectors) == 1)) {fprintf(__stderrp,"hypre_assert failed: %s\n", "hypre_VectorNumVectors(vector) == 1"); hypre_error_handler("amgmk.c", 1671, 1);};
    return vector;
}
    int
hypre_SeqVectorPrint( hypre_Vector *vector,
        char *file_name )
{
    FILE *fp;
    double *data;
    int size, num_vectors, vecstride, idxstride;
    int i, j;
    int ierr = 0;
    num_vectors = ((vector) -> num_vectors);
    vecstride = ((vector) -> vecstride );
    idxstride = ((vector) -> idxstride );
    data = ((vector) -> data);
    size = ((vector) -> size);
    fp = fopen(file_name, "w");
    if ( ((vector) -> num_vectors) == 1 )
    {
        fprintf(fp, "%d\n", size);
    }
    else
    {
        fprintf(fp, "%d vectors of size %d\n", num_vectors, size );
    }
    if ( num_vectors>1 )
    {
        for ( j=0; j<num_vectors; ++j )
        {
            fprintf(fp, "vector %d\n", j );
            for (i = 0; i < size; i++)
            {
                fprintf(fp, "%.14e\n", data[ j*vecstride + i*idxstride ] );
            }
        }
    }
    else
    {
        for (i = 0; i < size; i++)
        {
            fprintf(fp, "%.14e\n", data[i]);
        }
    }
    fclose(fp);
    return ierr;
}
    int
hypre_SeqVectorSetConstantValues( hypre_Vector *v,
        double value )
{
    double *vector_data = ((v) -> data);
    int size = ((v) -> size);
    int i;
    int ierr = 0;
    size *=((v) -> num_vectors);
    for (i = 0; i < size; i++)
        vector_data[i] = value;
    return ierr;
}
    int
hypre_SeqVectorCopy( hypre_Vector *x,
        hypre_Vector *y )
{
    double *x_data = ((x) -> data);
    double *y_data = ((y) -> data);
    int size = ((x) -> size);
    int i;
    int ierr = 0;
    size *=((x) -> num_vectors);
    for (i = 0; i < size; i++)
        y_data[i] = x_data[i];
    return ierr;
}
    hypre_Vector *
hypre_SeqVectorCloneDeep( hypre_Vector *x )
{
    int size = ((x) -> size);
    int num_vectors = ((x) -> num_vectors);
    hypre_Vector * y = hypre_SeqMultiVectorCreate( size, num_vectors );
    ((y) -> multivec_storage_method) = ((x) -> multivec_storage_method);
    ((y) -> vecstride ) = ((x) -> vecstride );
    ((y) -> idxstride ) = ((x) -> idxstride );
    hypre_SeqVectorInitialize(y);
    hypre_SeqVectorCopy( x, y );
    return y;
}
    hypre_Vector *
hypre_SeqVectorCloneShallow( hypre_Vector *x )
{
    int size = ((x) -> size);
    int num_vectors = ((x) -> num_vectors);
    hypre_Vector * y = hypre_SeqMultiVectorCreate( size, num_vectors );
    ((y) -> multivec_storage_method) = ((x) -> multivec_storage_method);
    ((y) -> vecstride ) = ((x) -> vecstride );
    ((y) -> idxstride ) = ((x) -> idxstride );
    ((y) -> data) = ((x) -> data);
    hypre_SeqVectorSetDataOwner( y, 0 );
    hypre_SeqVectorInitialize(y);
    return y;
}
    int
hypre_SeqVectorScale( double alpha,
        hypre_Vector *y )
{
    double *y_data = ((y) -> data);
    int size = ((y) -> size);
    int i;
    int ierr = 0;
    size *=((y) -> num_vectors);
    for (i = 0; i < size; i++)
        y_data[i] *= alpha;
    return ierr;
}
    int
hypre_SeqVectorAxpy( double alpha,
        hypre_Vector *x,
        hypre_Vector *y )
{
    double *x_data = ((x) -> data);
    double *y_data = ((y) -> data);
    int size = ((x) -> size);
    int i;
    int ierr = 0;
    size *=((x) -> num_vectors);
    for (i = 0; i < size; i++)
        y_data[i] += alpha * x_data[i];
    return ierr;
}
double hypre_SeqVectorInnerProd( hypre_Vector *x,
        hypre_Vector *y )
{
    double *x_data = ((x) -> data);
    double *y_data = ((y) -> data);
    int size = ((x) -> size);
    int i;
    double result = 0.0;
    size *=((x) -> num_vectors);
    for (i = 0; i < size; i++)
        result += y_data[i] * x_data[i];
    return result;
}
double hypre_VectorSumElts( hypre_Vector *vector )
{
    double sum = 0;
    double * data = ((vector) -> data);
    int size = ((vector) -> size);
    int i;
    for ( i=0; i<size; ++i ) sum += data[i];
    return sum;
}
    hypre_CSRMatrix *
GenerateSeqLaplacian( int nx,
        int ny,
        int nz,
        double *value,
        hypre_Vector **rhs_ptr,
        hypre_Vector **x_ptr,
        hypre_Vector **sol_ptr)
{
    hypre_CSRMatrix *A;
    hypre_Vector *rhs;
    hypre_Vector *x;
    hypre_Vector *sol;
    double *rhs_data;
    double *x_data;
    double *sol_data;
    int *A_i;
    int *A_j;
    double *A_data;
    int ix, iy, iz;
    int cnt;
    int row_index;
    int i, j;
    int grid_size;
    grid_size = nx*ny*nz;
    A_i = ( (int *)hypre_CAlloc((unsigned int)(grid_size+1), (unsigned int)sizeof(int)) );
    rhs_data = ( (double *)hypre_CAlloc((unsigned int)(grid_size), (unsigned int)sizeof(double)) );
    x_data = ( (double *)hypre_CAlloc((unsigned int)(grid_size), (unsigned int)sizeof(double)) );
    sol_data = ( (double *)hypre_CAlloc((unsigned int)(grid_size), (unsigned int)sizeof(double)) );
    for (i=0; i < grid_size; i++)
    {
        x_data[i] = 0.0;
        sol_data[i] = 0.0;
        rhs_data[i] = 1.0;
    }
    cnt = 1;
    A_i[0] = 0;
    for (iz = 0; iz < nz; iz++)
    {
        for (iy = 0; iy < ny; iy++)
        {
            for (ix = 0; ix < nx; ix++)
            {
                A_i[cnt] = A_i[cnt-1];
                A_i[cnt]++;
                if (iz)
                    A_i[cnt]++;
                if (iy)
                    A_i[cnt]++;
                if (ix)
                    A_i[cnt]++;
                if (ix+1 < nx)
                    A_i[cnt]++;
                if (iy+1 < ny)
                    A_i[cnt]++;
                if (iz+1 < nz)
                    A_i[cnt]++;
                cnt++;
            }
        }
    }
    A_j = ( (int *)hypre_CAlloc((unsigned int)(A_i[grid_size]), (unsigned int)sizeof(int)) );
    A_data = ( (double *)hypre_CAlloc((unsigned int)(A_i[grid_size]), (unsigned int)sizeof(double)) );
    row_index = 0;
    cnt = 0;
    for (iz = 0; iz < nz; iz++)
    {
        for (iy = 0; iy < ny; iy++)
        {
            for (ix = 0; ix < nx; ix++)
            {
                A_j[cnt] = row_index;
                A_data[cnt++] = value[0];
                if (iz)
                {
                    A_j[cnt] = row_index-nx*ny;
                    A_data[cnt++] = value[3];
                }
                if (iy)
                {
                    A_j[cnt] = row_index-nx;
                    A_data[cnt++] = value[2];
                }
                if (ix)
                {
                    A_j[cnt] = row_index-1;
                    A_data[cnt++] = value[1];
                }
                if (ix+1 < nx)
                {
                    A_j[cnt] = row_index+1;
                    A_data[cnt++] = value[1];
                }
                if (iy+1 < ny)
                {
                    A_j[cnt] = row_index+nx;
                    A_data[cnt++] = value[2];
                }
                if (iz+1 < nz)
                {
                    A_j[cnt] = row_index+nx*ny;
                    A_data[cnt++] = value[3];
                }
                row_index++;
            }
        }
    }
    A = hypre_CSRMatrixCreate(grid_size, grid_size,
            A_i[grid_size]);
    rhs = hypre_SeqVectorCreate(grid_size);
    ((rhs) -> data) = rhs_data;
    x = hypre_SeqVectorCreate(grid_size);
    ((x) -> data) = x_data;
    for (i=0; i < grid_size; i++)
        for (j=A_i[i]; j < A_i[i+1]; j++)
            sol_data[i] += A_data[j];
    sol = hypre_SeqVectorCreate(grid_size);
    ((sol) -> data) = sol_data;
    ((A) -> i) = A_i;
    ((A) -> j) = A_j;
    ((A) -> data) = A_data;
    *rhs_ptr = rhs;
    *x_ptr = x;
    *sol_ptr = sol;
    return A;
}
int hypre_BoomerAMGSeqRelax( hypre_CSRMatrix *A,
        hypre_Vector *f,
        hypre_Vector *u)
{
    double *A_diag_data = ((A) -> data);
    int *A_diag_i = ((A) -> i);
    int *A_diag_j = ((A) -> j);
    int n = ((A) -> num_rows);
    double *u_data = ((u) -> data);
    double *f_data = ((f) -> data);
    double *tmp_data;
    double res;
    int i, j;
    int ii, jj;
    int ns, ne, size, rest;
    int relax_error = 0;
    int numThreads;
    numThreads = 1;
    if (1)
    {
        tmp_data = ( (double *)hypre_CAlloc((unsigned int)(n), (unsigned int)sizeof(double)) );
#pragma omp parallel private(numThreads)
        {
            numThreads = omp_get_num_threads();
#pragma omp for private(i)
            for (i = 0; i < n; i++)
                tmp_data[i] = u_data[i];
#pragma omp for private(i,ii,j,jj,ns,ne,res,rest,size)
            for (j = 0; j < numThreads; j++)
            {
                size = n/numThreads;
                rest = n - size*numThreads;
                if (j < rest)
                {
                    ns = j*size+j;
                    ne = (j+1)*size+j+1;
                }
                else
                {
                    ns = j*size+rest;
                    ne = (j+1)*size+rest;
                }
                for (i = ns; i < ne; i++)
                {
                    if ( A_diag_data[A_diag_i[i]] != 0.0)
                    {
                        res = f_data[i];
                        for (jj = A_diag_i[i]+1; jj < A_diag_i[i+1]; jj++)
                        {
                            ii = A_diag_j[jj];
                            if (ii >= ns && ii < ne)
                                res -= A_diag_data[jj] * u_data[ii];
                            else
                                res -= A_diag_data[jj] * tmp_data[ii];
                        }
                        u_data[i] = res / A_diag_data[A_diag_i[i]];
                    }
                }
            }
        }
        ( hypre_Free((char *)tmp_data), tmp_data = ((void *)0) );
    }
    else
    {
        for (i = 0; i < n; i++)
        {
            if ( A_diag_data[A_diag_i[i]] != 0.0)
            {
                res = f_data[i];
                for (jj = A_diag_i[i]+1; jj < A_diag_i[i+1]; jj++)
                {
                    ii = A_diag_j[jj];
                    res -= A_diag_data[jj] * u_data[ii];
                }
                u_data[i] = res / A_diag_data[A_diag_i[i]];
            }
        }
    }
    return(relax_error);
}
int hypre__global_error = 0;
void hypre_error_handler(char *filename, int line, int ierr)
{
    hypre__global_error |= ierr;
}
int HYPRE_GetError()
{
    return hypre__global_error;
}
int HYPRE_CheckError(int ierr, int hypre_error_code)
{
    return ierr & hypre_error_code;
}
void HYPRE_DescribeError(int ierr, char *msg)
{
    if (ierr == 0)
        __builtin___sprintf_chk (msg, 0, __builtin_object_size (msg, 2 > 1 ? 1 : 0), "[No error] ");
    if (ierr & 1)
        __builtin___sprintf_chk (msg, 0, __builtin_object_size (msg, 2 > 1 ? 1 : 0), "[Generic error] ");
    if (ierr & 2)
        __builtin___sprintf_chk (msg, 0, __builtin_object_size (msg, 2 > 1 ? 1 : 0), "[Memory error] ");
    if (ierr & 4)
        __builtin___sprintf_chk (msg, 0, __builtin_object_size (msg, 2 > 1 ? 1 : 0), "[Error in argument %d] ", HYPRE_GetErrorArg());
    if (ierr & 256)
        __builtin___sprintf_chk (msg, 0, __builtin_object_size (msg, 2 > 1 ? 1 : 0), "[Method did not converge] ");
}
int HYPRE_GetErrorArg()
{
    return (hypre__global_error>>3 & 31);
}
    int
hypre_OutOfMemory( int size )
{
    printf("Out of memory trying to allocate %d bytes\n", size);
    fflush(__stdoutp);
    hypre_error_handler("amgmk.c", 2270, 2);
    return 0;
}
    char *
hypre_MAlloc( int size )
{
    char *ptr;
    if (size > 0)
    {
        ptr = malloc(size);
        if (ptr == ((void *)0))
        {
            hypre_OutOfMemory(size);
        }
    }
    else
    {
        ptr = ((void *)0);
    }
    return ptr;
}
    char *
hypre_CAlloc( int count,
        int elt_size )
{
    char *ptr;
    int size = count*elt_size;
    if (size > 0)
    {
        ptr = calloc(count, elt_size);
        if (ptr == ((void *)0))
        {
            hypre_OutOfMemory(size);
        }
    }
    else
    {
        ptr = ((void *)0);
    }
    return ptr;
}
    char *
hypre_ReAlloc( char *ptr,
        int size )
{
    if (ptr == ((void *)0))
    {
        ptr = malloc(size);
    }
    else
    {
        ptr = realloc(ptr, size);
    }
    if ((ptr == ((void *)0)) && (size > 0))
    {
        hypre_OutOfMemory(size);
    }
    return ptr;
}
    void
hypre_Free( char *ptr )
{
    if (ptr)
    {
        free(ptr);
    }
}
const int testIter = 500;
double totalWallTime = 0.0;
double totalCPUTime = 0.0;
void test_Matvec();
void test_Relax();
void test_Axpy();
int main(int argc, char *argv[])
{
    struct timeval t0, t1;
    clock_t t0_cpu = 0,
            t1_cpu = 0;
    double del_wtime = 0.0;
    printf("\n");
    printf("//------------ \n");
    printf("// \n");
    printf("//  Sequoia Benchmark Version 1.0 \n");
    printf("// \n");
    printf("//------------ \n");
    gettimeofday(&t0, ((void *)0));
    t0_cpu = clock();
    totalWallTime = 0.0;
    totalCPUTime = 0.0;
    test_Matvec();
    printf("\n");
    printf("//------------ \n");
    printf("// \n");
    printf("//   MATVEC\n");
    printf("// \n");
    printf("//------------ \n");
    printf("\nTotal Wall time = %f seconds. \n", totalWallTime);
    printf("\nTotal CPU  time = %f seconds. \n\n", totalCPUTime);
    totalWallTime = 0.0;
    totalCPUTime = 0.0;
    test_Relax();
    printf("\n");
    printf("//------------ \n");
    printf("// \n");
    printf("//   Relax\n");
    printf("// \n");
    printf("//------------ \n");
    printf("\nTotal Wall time = %f seconds. \n", totalWallTime);
    printf("\nTotal CPU  time = %f seconds. \n\n", totalCPUTime);
    totalWallTime = 0.0;
    totalCPUTime = 0.0;
    test_Axpy();
    printf("\n");
    printf("//------------ \n");
    printf("// \n");
    printf("//   Axpy\n");
    printf("// \n");
    printf("//------------ \n");
    printf("\nTotal Wall time = %f seconds. \n", totalWallTime);
    printf("\nTotal CPU  time = %f seconds. \n\n", totalCPUTime);
    gettimeofday(&t1, ((void *)0));
    t1_cpu = clock();
    del_wtime = (double)(t1.tv_sec - t0.tv_sec) +
        (double)(t1.tv_usec - t0.tv_usec)/1000000.0;
    printf("\nTotal Wall time = %f seconds. \n", del_wtime);
    printf("\nTotal CPU  time = %f seconds. \n", ((double) (t1_cpu - t0_cpu))/1000000);
    return 0;
}
void test_Matvec()
{
    struct timeval t0, t1;
    clock_t t0_cpu = 0,
            t1_cpu = 0;
    hypre_CSRMatrix *A;
    hypre_Vector *x, *y, *sol;
    int nx, ny, nz, i;
    double *values;
    double *y_data, *sol_data;
    double error, diff;
    nx = 50;
    ny = 50;
    nz = 50;
    values = ( (double *)hypre_CAlloc((unsigned int)(4), (unsigned int)sizeof(double)) );
    values[0] = 6;
    values[1] = -1;
    values[2] = -1;
    values[3] = -1;
    A = GenerateSeqLaplacian(nx, ny, nz, values, &y, &x, &sol);
    hypre_SeqVectorSetConstantValues(x,1);
    hypre_SeqVectorSetConstantValues(y,0);
    gettimeofday(&t0, ((void *)0));
    t0_cpu = clock();
    for (i=0; i<testIter; ++i)
        hypre_CSRMatrixMatvec(1,A,x,0,y);
    gettimeofday(&t1, ((void *)0));
    t1_cpu = clock();
    totalWallTime += (double)(t1.tv_sec - t0.tv_sec) +
        (double)(t1.tv_usec - t0.tv_usec)/1000000.0;
    totalCPUTime += ((double) (t1_cpu - t0_cpu))/1000000;
    y_data = ((y) -> data);
    sol_data = ((sol) -> data);
    error = 0;
    for (i=0; i < nx*ny*nz; i++)
    {
        diff = fabs(y_data[i]-sol_data[i]);
        if (diff > error) error = diff;
    }
    if (error > 0) printf(" \n Matvec: error: %e\n", error);
    ( hypre_Free((char *)values), values = ((void *)0) );
    hypre_CSRMatrixDestroy(A);
    hypre_SeqVectorDestroy(x);
    hypre_SeqVectorDestroy(y);
    hypre_SeqVectorDestroy(sol);
}
void test_Relax()
{
    struct timeval t0, t1;
    clock_t t0_cpu = 0,
            t1_cpu = 0;
    hypre_CSRMatrix *A;
    hypre_Vector *x, *y, *sol;
    int nx, ny, nz, i;
    double *values;
    double *x_data;
    double diff, error;
    nx = 50;
    ny = 50;
    nz = 50;
    values = ( (double *)hypre_CAlloc((unsigned int)(4), (unsigned int)sizeof(double)) );
    values[0] = 6;
    values[1] = -1;
    values[2] = -1;
    values[3] = -1;
    A = GenerateSeqLaplacian(nx, ny, nz, values, &y, &x, &sol);
    hypre_SeqVectorSetConstantValues(x,1);
    gettimeofday(&t0, ((void *)0));
    t0_cpu = clock();
    for (i=0; i<testIter; ++i)
        hypre_BoomerAMGSeqRelax(A, sol, x);
    gettimeofday(&t1, ((void *)0));
    t1_cpu = clock();
    totalWallTime += (double)(t1.tv_sec - t0.tv_sec) +
        (double)(t1.tv_usec - t0.tv_usec)/1000000.0;
    totalCPUTime += ((double) (t1_cpu - t0_cpu))/1000000;
    x_data = ((x) -> data);
    error = 0;
    for (i=0; i < nx*ny*nz; i++)
    {
        diff = fabs(x_data[i]-1);
        if (diff > error) error = diff;
    }
    if (error > 0) printf(" \n Relax: error: %e\n", error);
    ( hypre_Free((char *)values), values = ((void *)0) );
    hypre_CSRMatrixDestroy(A);
    hypre_SeqVectorDestroy(x);
    hypre_SeqVectorDestroy(y);
    hypre_SeqVectorDestroy(sol);
}
void test_Axpy()
{
    struct timeval t0, t1;
    clock_t t0_cpu = 0,
            t1_cpu = 0;
    hypre_Vector *x, *y;
    int nx, i;
    double alpha=0.5;
    double diff, error;
    double *y_data;
    nx = 125000;
    x = hypre_SeqVectorCreate(nx);
    y = hypre_SeqVectorCreate(nx);
    hypre_SeqVectorInitialize(x);
    hypre_SeqVectorInitialize(y);
    hypre_SeqVectorSetConstantValues(x,1);
    hypre_SeqVectorSetConstantValues(y,1);
    gettimeofday(&t0, ((void *)0));
    t0_cpu = clock();
    for (i=0; i<testIter; ++i)
        hypre_SeqVectorAxpy(alpha,x,y);
    gettimeofday(&t1, ((void *)0));
    t1_cpu = clock();
    y_data = ((y) -> data);
    error = 0;
    for (i=0; i < nx; i++)
    {
        diff = fabs(y_data[i]-1-0.5*(double)testIter);
        if (diff > error) error = diff;
    }
    if (error > 0) printf(" \n Axpy: error: %e\n", error);
    totalWallTime += (double)(t1.tv_sec - t0.tv_sec) +
        (double)(t1.tv_usec - t0.tv_usec)/1000000.0;
    totalCPUTime += ((double) (t1_cpu - t0_cpu))/1000000;
    hypre_SeqVectorDestroy(x);
    hypre_SeqVectorDestroy(y);
}
