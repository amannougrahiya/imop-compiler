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
void clearerr(FILE *);
int fclose(FILE *);
int feof(FILE *);
int ferror(FILE *);
int fflush(FILE *);
int fgetc(FILE *);
int fgetpos(FILE * , fpos_t *);
char *fgets(char * , int, FILE *);
FILE *fopen(const char * __filename, const char * __mode) __asm("_" "fopen" );
int fprintf(FILE * , const char * , ...) __attribute__((__format__ (__printf__, 2, 3)));
int fputc(int, FILE *);
int fputs(const char * , FILE * ) __asm("_" "fputs" );
size_t fread(void * __ptr, size_t __size, size_t __nitems, FILE * __stream);
FILE *freopen(const char * , const char * ,
        FILE * ) __asm("_" "freopen" );
int fscanf(FILE * , const char * , ...) __attribute__((__format__ (__scanf__, 2, 3)));
int fseek(FILE *, long, int);
int fsetpos(FILE *, const fpos_t *);
long ftell(FILE *);
size_t fwrite(const void * __ptr, size_t __size, size_t __nitems, FILE * __stream) __asm("_" "fwrite" );
int getc(FILE *);
int getchar(void);
char *gets(char *);
void perror(const char *);
int printf(const char * , ...) __attribute__((__format__ (__printf__, 1, 2)));
int putc(int, FILE *);
int putchar(int);
int puts(const char *);
int remove(const char *);
int rename (const char *__old, const char *__new);
void rewind(FILE *);
int scanf(const char * , ...) __attribute__((__format__ (__scanf__, 1, 2)));
void setbuf(FILE * , char * );
int setvbuf(FILE * , char * , int, size_t);
int sprintf(char * , const char * , ...) __attribute__((__format__ (__printf__, 2, 3))) ;
int sscanf(const char * , const char * , ...) __attribute__((__format__ (__scanf__, 2, 3)));
FILE *tmpfile(void);

__attribute__((deprecated("This function is provided for compatibility reasons only.  Due to security concerns inherent in the design of tmpnam(3), it is highly recommended that you use mkstemp(3) instead.")))
char *tmpnam(char *);
int ungetc(int, FILE *);
int vfprintf(FILE * , const char * , va_list) __attribute__((__format__ (__printf__, 2, 0)));
int vprintf(const char * , va_list) __attribute__((__format__ (__printf__, 1, 0)));
int vsprintf(char * , const char * , va_list) __attribute__((__format__ (__printf__, 2, 0))) ;
char *ctermid(char *);
FILE *fdopen(int, const char *) __asm("_" "fdopen" );
int fileno(FILE *);
int pclose(FILE *) ;
FILE *popen(const char *, const char *) __asm("_" "popen" ) ;
int __srget(FILE *);
int __svfscanf(FILE *, const char *, va_list) __attribute__((__format__ (__scanf__, 2, 0)));
int __swbuf(int, FILE *);
inline __attribute__ ((__always_inline__)) int __sputc(int _c, FILE *_p) {
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
int snprintf(char * __str, size_t __size, const char * __format, ...) __attribute__((__format__ (__printf__, 3, 4)));
int vfscanf(FILE * __stream, const char * __format, va_list) __attribute__((__format__ (__scanf__, 2, 0)));
int vscanf(const char * __format, va_list) __attribute__((__format__ (__scanf__, 1, 0)));
int vsnprintf(char * __str, size_t __size, const char * __format, va_list) __attribute__((__format__ (__printf__, 3, 0)));
int vsscanf(const char * __str, const char * __format, va_list) __attribute__((__format__ (__scanf__, 2, 0)));
typedef __darwin_ssize_t ssize_t;
int dprintf(int, const char * , ...) __attribute__((__format__ (__printf__, 2, 3))) ;
int vdprintf(int, const char * , va_list) __attribute__((__format__ (__printf__, 2, 0))) ;
ssize_t getdelim(char ** __linep, size_t * __linecapp, int __delimiter, FILE * __stream) ;
ssize_t getline(char ** __linep, size_t * __linecapp, FILE * __stream) ;
FILE *fmemopen(void * __buf, size_t __size, const char * __mode) ;
FILE *open_memstream(char **__bufp, size_t *__sizep) ;
extern const int sys_nerr;
extern const char *const sys_errlist[];
int asprintf(char ** , const char * , ...) __attribute__((__format__ (__printf__, 2, 3)));
char *ctermid_r(char *);
char *fgetln(FILE *, size_t *);
const char *fmtcheck(const char *, const char *);
int fpurge(FILE *);
void setbuffer(FILE *, char *, int);
int setlinebuf(FILE *);
int vasprintf(char ** , const char * , va_list) __attribute__((__format__ (__printf__, 2, 0)));
FILE *zopen(const char *, const char *, int);
FILE *funopen(const void *,
        int (* )(void *, char *, int),
        int (* )(void *, const char *, int),
        fpos_t (* )(void *, fpos_t, int),
        int (* )(void *));

    typedef long unsigned int size_t;
    typedef long int ptrdiff_t;
    typedef decltype(nullptr) nullptr_t;
    inline namespace __cxx11 __attribute__((__abi_tag__ ("cxx11"))) { }
    inline namespace __cxx11 __attribute__((__abi_tag__ ("cxx11"))) { }
typedef float float_t;
typedef double double_t;
extern int __math_errhandling(void);
extern int __fpclassifyf(float);
extern int __fpclassifyd(double);
extern int __fpclassifyl(long double);
inline __attribute__ ((__always_inline__)) int __inline_isfinitef(float);
inline __attribute__ ((__always_inline__)) int __inline_isfinited(double);
inline __attribute__ ((__always_inline__)) int __inline_isfinitel(long double);
inline __attribute__ ((__always_inline__)) int __inline_isinff(float);
inline __attribute__ ((__always_inline__)) int __inline_isinfd(double);
inline __attribute__ ((__always_inline__)) int __inline_isinfl(long double);
inline __attribute__ ((__always_inline__)) int __inline_isnanf(float);
inline __attribute__ ((__always_inline__)) int __inline_isnand(double);
inline __attribute__ ((__always_inline__)) int __inline_isnanl(long double);
inline __attribute__ ((__always_inline__)) int __inline_isnormalf(float);
inline __attribute__ ((__always_inline__)) int __inline_isnormald(double);
inline __attribute__ ((__always_inline__)) int __inline_isnormall(long double);
inline __attribute__ ((__always_inline__)) int __inline_signbitf(float);
inline __attribute__ ((__always_inline__)) int __inline_signbitd(double);
inline __attribute__ ((__always_inline__)) int __inline_signbitl(long double);
inline __attribute__ ((__always_inline__)) int __inline_isfinitef(float __x) {
    return __x == __x && __builtin_fabsf(__x) != __builtin_inff();
    inline __attribute__ ((__always_inline__)) int __inline_isfinited(double __x) {
        return __x == __x && __builtin_fabs(__x) != __builtin_inf();
    }
    inline __attribute__ ((__always_inline__)) int __inline_isfinitel(long double __x) {
        return __x == __x && __builtin_fabsl(__x) != __builtin_infl();
    }
    inline __attribute__ ((__always_inline__)) int __inline_isinff(float __x) {
        return __builtin_fabsf(__x) == __builtin_inff();
    }
    inline __attribute__ ((__always_inline__)) int __inline_isinfd(double __x) {
        return __builtin_fabs(__x) == __builtin_inf();
    }
    inline __attribute__ ((__always_inline__)) int __inline_isinfl(long double __x) {
        return __builtin_fabsl(__x) == __builtin_infl();
    }
    inline __attribute__ ((__always_inline__)) int __inline_isnanf(float __x) {
        return __x != __x;
    }
    inline __attribute__ ((__always_inline__)) int __inline_isnand(double __x) {
        return __x != __x;
    }
    inline __attribute__ ((__always_inline__)) int __inline_isnanl(long double __x) {
        return __x != __x;
    }
    inline __attribute__ ((__always_inline__)) int __inline_signbitf(float __x) {
        union { float __f; unsigned int __u; } __u;
        __u.__f = __x;
        return (int)(__u.__u >> 31);
    }
    inline __attribute__ ((__always_inline__)) int __inline_signbitd(double __x) {
        union { double __f; unsigned long long __u; } __u;
        __u.__f = __x;
        return (int)(__u.__u >> 63);
    }
    inline __attribute__ ((__always_inline__)) int __inline_signbitl(long double __x) {
        union {
            long double __ld;
            struct{ unsigned long long __m; unsigned short __sexp; } __p;
        } __u;
        __u.__ld = __x;
        return (int)(__u.__p.__sexp >> 15);
    }
    inline __attribute__ ((__always_inline__)) int __inline_isnormalf(float __x) {
        return __inline_isfinitef(__x) && __builtin_fabsf(__x) >= 1.17549435082228750797e-38F;
    }
    inline __attribute__ ((__always_inline__)) int __inline_isnormald(double __x) {
        return __inline_isfinited(__x) && __builtin_fabs(__x) >= double(2.22507385850720138309e-308L);
    }
    inline __attribute__ ((__always_inline__)) int __inline_isnormall(long double __x) {
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
    inline __attribute__ ((__always_inline__)) void __sincosf(float __x, float *__sinp, float *__cosp);
    inline __attribute__ ((__always_inline__)) void __sincos(double __x, double *__sinp, double *__cosp);
    extern float __cospif(float) ;
    extern double __cospi(double) ;
    extern float __sinpif(float) ;
    extern double __sinpi(double) ;
    extern float __tanpif(float) ;
    extern double __tanpi(double) ;
    inline __attribute__ ((__always_inline__)) void __sincospif(float __x, float *__sinp, float *__cosp);
    inline __attribute__ ((__always_inline__)) void __sincospi(double __x, double *__sinp, double *__cosp);
    struct __float2 { float __sinval; float __cosval; };
    struct __double2 { double __sinval; double __cosval; };
    extern struct __float2 __sincosf_stret(float);
    extern struct __double2 __sincos_stret(double);
    extern struct __float2 __sincospif_stret(float);
    extern struct __double2 __sincospi_stret(double);
    inline __attribute__ ((__always_inline__)) void __sincosf(float __x, float *__sinp, float *__cosp) {
        const struct __float2 __stret = __sincosf_stret(__x);
        *__sinp = __stret.__sinval; *__cosp = __stret.__cosval;
    }
    inline __attribute__ ((__always_inline__)) void __sincos(double __x, double *__sinp, double *__cosp) {
        const struct __double2 __stret = __sincos_stret(__x);
        *__sinp = __stret.__sinval; *__cosp = __stret.__cosval;
    }
    inline __attribute__ ((__always_inline__)) void __sincospif(float __x, float *__sinp, float *__cosp) {
        const struct __float2 __stret = __sincospif_stret(__x);
        *__sinp = __stret.__sinval; *__cosp = __stret.__cosval;
    }
    inline __attribute__ ((__always_inline__)) void __sincospi(double __x, double *__sinp, double *__cosp) {
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
}
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
size_t mbstowcs(wchar_t * , const char * , size_t);
int mbtowc(wchar_t * , const char * , size_t);
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
size_t wcstombs(char * , const wchar_t * , size_t);
int wctomb(char *, wchar_t);
void _Exit(int) __attribute__((noreturn));
long a64l(const char *);
double drand48(void);
char *ecvt(double, int, int *, int *);
double erand48(unsigned short[3]);
char *fcvt(double, int, int *, int *);
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
int putenv(char *) __asm("_" "putenv" );
long random(void) ;
int rand_r(unsigned *) ;
char *realpath(const char * , char * ) __asm("_" "realpath" "$DARWIN_EXTSN");
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
struct GlobalMemory {
    double *t_in_fac;
    double *t_in_solve;
    double *t_in_mod;
    double *t_in_bar;
    double *completion;
    unsigned int starttime;
    unsigned int rf;
    unsigned int rs;
    unsigned int done;
    int id;
    BARDEC(start)
        LOCKDEC(idlock)
} *Global;
struct LocalCopies {
    double t_in_fac;
    double t_in_solve;
    double t_in_mod;
    double t_in_bar;
};
int n = 512;
int P = 1;
int block_size = 16;
int nblocks;
int num_rows;
int num_cols;
double **a;
double *rhs;
int *proc_bytes;
double **last_malloc;
int test_result = 0;
int doprint = 0;
int dostats = 0;
void SlaveStart();
void OneSolve(int, int, double **, int, int);
void lu0(double *,int, int);
void bdiv(double *, double *, int, int, int, int);
void bmodd(double *, double*, int, int, int, int);
void bmod(double *, double *, double *, int, int, int, int, int, int);
void daxpy(double *, double *, int, double);
int BlockOwner(int, int);
void lu(int, int, int, struct LocalCopies *, int);
void InitA(double *);
double TouchA(int, int);
void PrintA();
void CheckResult(int, double **, double *);
void printerr(char *);
main(argc, argv)
    int argc;
    char *argv[];
{
    int i, j;
    int ch;
    extern char *optarg;
    int MyNum=0;
    double mint, maxt, avgt;
    double min_fac, min_solve, min_mod, min_bar;
    double max_fac, max_solve, max_mod, max_bar;
    double avg_fac, avg_solve, avg_mod, avg_bar;
    int last_page;
    int proc_num;
    int edge;
    int size;
    unsigned int start;
    CLOCK(start)
        while ((ch = getopt(argc, argv, "n:p:b:cstoh")) != -1) {
            switch(ch) {
                case 'n': n = atoi(optarg); break;
                case 'p': P = atoi(optarg); break;
                case 'b': block_size = atoi(optarg); break;
                case 's': dostats = 1; break;
                case 't': test_result = !test_result; break;
                case 'o': doprint = !doprint; break;
                case 'h': printf("Usage: LU <options>\n\n");
                          printf("options:\n");
                          printf("  -nN : Decompose NxN matrix.\n");
                          printf("  -pP : P = number of processors.\n");
                          printf("  -bB : Use a block size of B. BxB elements should fit in cache for \n");
                          printf("        good performance. Small block sizes (B=8, B=16) work well.\n");
                          printf("  -c  : Copy non-locally allocated blocks to local memory before use.\n");
                          printf("  -s  : Print individual processor timing statistics.\n");
                          printf("  -t  : Test output.\n");
                          printf("  -o  : Print out matrix values.\n");
                          printf("  -h  : Print out command line options.\n\n");
                          printf("Default: LU -n%1d -p%1d -b%1d\n",
                                  512,1,16);
                          exit(0);
                          break;
            }
        }
    MAIN_INITENV(,150000000)
        printf("\n");
    printf("Blocked Dense LU Factorization\n");
    printf("     %d by %d Matrix\n",n,n);
    printf("     %d Processors\n",P);
    printf("     %d by %d Element Blocks\n",block_size,block_size);
    printf("\n");
    printf("\n");
    num_rows = (int) sqrt((double) P);
    for (;;) {
        num_cols = P/num_rows;
        if (num_rows*num_cols == P)
            break;
        num_rows--;
    }
    nblocks = n/block_size;
    if (block_size * nblocks != n) {
        nblocks++;
    }
    edge = n%block_size;
    if (edge == 0) {
        edge = block_size;
    }
    proc_bytes = (int *) malloc(P*sizeof(int));
    last_malloc = (double **) G_MALLOC(P*sizeof(double *));
    for (i=0;i<P;i++) {
        proc_bytes[i] = 0;
        last_malloc[i] = __null;
    }
    for (i=0;i<nblocks;i++) {
        for (j=0;j<nblocks;j++) {
            proc_num = BlockOwner(i,j);
            if ((i == nblocks-1) && (j == nblocks-1)) {
                size = edge*edge;
            } else if ((i == nblocks-1) || (j == nblocks-1)) {
                size = edge*block_size;
            } else {
                size = block_size*block_size;
            }
            proc_bytes[proc_num] += size*sizeof(double);
        }
    }
    for (i=0;i<P;i++) {
        last_malloc[i] = (double *) G_MALLOC(proc_bytes[i] + PAGE_SIZE)
            if (last_malloc[i] == __null) {
                fprintf(__stderrp,"Could not malloc memory blocks for proc %d\n",i);
                exit(-1);
            }
        last_malloc[i] = (double *) (((unsigned) last_malloc[i]) + PAGE_SIZE -
                ((unsigned) last_malloc[i]) % PAGE_SIZE);
    }
    a = (double **) G_MALLOC(nblocks*nblocks*sizeof(double *));
    if (a == __null) {
        printerr("Could not malloc memory for a\n");
        exit(-1);
    }
    for (i=0;i<nblocks;i++) {
        for (j=0;j<nblocks;j++) {
            proc_num = BlockOwner(i,j);
            a[i+j*nblocks] = last_malloc[proc_num];
            if ((i == nblocks-1) && (j == nblocks-1)) {
                size = edge*edge;
            } else if ((i == nblocks-1) || (j == nblocks-1)) {
                size = edge*block_size;
            } else {
                size = block_size*block_size;
            }
            last_malloc[proc_num] += size;
        }
    }
    rhs = (double *) G_MALLOC(n*sizeof(double));
    if (rhs == __null) {
        printerr("Could not malloc memory for rhs\n");
        exit(-1);
    }
    Global = (struct GlobalMemory *) G_MALLOC(sizeof(struct GlobalMemory));
    Global->t_in_fac = (double *) G_MALLOC(P*sizeof(double));
    Global->t_in_mod = (double *) G_MALLOC(P*sizeof(double));
    Global->t_in_solve = (double *) G_MALLOC(P*sizeof(double));
    Global->t_in_bar = (double *) G_MALLOC(P*sizeof(double));
    Global->completion = (double *) G_MALLOC(P*sizeof(double));
    if (Global == __null) {
        printerr("Could not malloc memory for Global\n");
        exit(-1);
    } else if (Global->t_in_fac == __null) {
        printerr("Could not malloc memory for Global->t_in_fac\n");
        exit(-1);
    } else if (Global->t_in_mod == __null) {
        printerr("Could not malloc memory for Global->t_in_mod\n");
        exit(-1);
    } else if (Global->t_in_solve == __null) {
        printerr("Could not malloc memory for Global->t_in_solve\n");
        exit(-1);
    } else if (Global->t_in_bar == __null) {
        printerr("Could not malloc memory for Global->t_in_bar\n");
        exit(-1);
    } else if (Global->completion == __null) {
        printerr("Could not malloc memory for Global->completion\n");
        exit(-1);
    }
    BARINIT(Global->start);
    LOCKINIT(Global->idlock);
    Global->id = 0;
    for (i=1; i<P; i++) {
        CREATE(SlaveStart);
    }
    InitA(rhs);
    if (doprint) {
        printf("Matrix before decomposition:\n");
        PrintA();
    }
    SlaveStart(MyNum);
    WAIT_FOR_END(P-1)
        if (doprint) {
            printf("\nMatrix after decomposition:\n");
            PrintA();
        }
    if (dostats) {
        maxt = avgt = mint = Global->completion[0];
        for (i=1; i<P; i++) {
            if (Global->completion[i] > maxt) {
                maxt = Global->completion[i];
            }
            if (Global->completion[i] < mint) {
                mint = Global->completion[i];
            }
            avgt += Global->completion[i];
        }
        avgt = avgt / P;
        min_fac = max_fac = avg_fac = Global->t_in_fac[0];
        min_solve = max_solve = avg_solve = Global->t_in_solve[0];
        min_mod = max_mod = avg_mod = Global->t_in_mod[0];
        min_bar = max_bar = avg_bar = Global->t_in_bar[0];
        for (i=1; i<P; i++) {
            if (Global->t_in_fac[i] > max_fac) {
                max_fac = Global->t_in_fac[i];
            }
            if (Global->t_in_fac[i] < min_fac) {
                min_fac = Global->t_in_fac[i];
            }
            if (Global->t_in_solve[i] > max_solve) {
                max_solve = Global->t_in_solve[i];
            }
            if (Global->t_in_solve[i] < min_solve) {
                min_solve = Global->t_in_solve[i];
            }
            if (Global->t_in_mod[i] > max_mod) {
                max_mod = Global->t_in_mod[i];
            }
            if (Global->t_in_mod[i] < min_mod) {
                min_mod = Global->t_in_mod[i];
            }
            if (Global->t_in_bar[i] > max_bar) {
                max_bar = Global->t_in_bar[i];
            }
            if (Global->t_in_bar[i] < min_bar) {
                min_bar = Global->t_in_bar[i];
            }
            avg_fac += Global->t_in_fac[i];
            avg_solve += Global->t_in_solve[i];
            avg_mod += Global->t_in_mod[i];
            avg_bar += Global->t_in_bar[i];
        }
        avg_fac = avg_fac/P;
        avg_solve = avg_solve/P;
        avg_mod = avg_mod/P;
        avg_bar = avg_bar/P;
    }
    printf("                            PROCESS STATISTICS\n");
    printf("              Total      Diagonal     Perimeter      Interior       Barrier\n");
    printf(" Proc         Time         Time         Time           Time          Time\n");
    printf("    0    %10.0f    %10.0f    %10.0f    %10.0f    %10.0f\n",
            Global->completion[0],Global->t_in_fac[0],
            Global->t_in_solve[0],Global->t_in_mod[0],
            Global->t_in_bar[0]);
    if (dostats) {
        for (i=1; i<P; i++) {
            printf("  %3d    %10.0f    %10.0f    %10.0f    %10.0f    %10.0f\n",
                    i,Global->completion[i],Global->t_in_fac[i],
                    Global->t_in_solve[i],Global->t_in_mod[i],
                    Global->t_in_bar[i]);
        }
        printf("  Avg    %10.0f    %10.0f    %10.0f    %10.0f    %10.0f\n",
                avgt,avg_fac,avg_solve,avg_mod,avg_bar);
        printf("  Min    %10.0f    %10.0f    %10.0f    %10.0f    %10.0f\n",
                mint,min_fac,min_solve,min_mod,min_bar);
        printf("  Max    %10.0f    %10.0f    %10.0f    %10.0f    %10.0f\n",
                maxt,max_fac,max_solve,max_mod,max_bar);
    }
    printf("\n");
    Global->starttime = start;
    printf("                            TIMING INFORMATION\n");
    printf("Start time                        : %16d\n",
            Global->starttime);
    printf("Initialization finish time        : %16d\n",
            Global->rs);
    printf("Overall finish time               : %16d\n",
            Global->rf);
    printf("Total time with initialization    : %16d\n",
            Global->rf-Global->starttime);
    printf("Total time without initialization : %16d\n",
            Global->rf-Global->rs);
    printf("\n");
    if (test_result) {
        printf("                             TESTING RESULTS\n");
        CheckResult(n, a, rhs);
    }
    MAIN_END;
}
void SlaveStart()
{
    int i;
    int j;
    int cluster;
    int max_block;
    int MyNum;
    LOCK(Global->idlock)
        MyNum = Global->id;
    Global->id ++;
    UNLOCK(Global->idlock)
        OneSolve(n, block_size, a, MyNum, dostats);
}
void OneSolve(n, block_size, a, MyNum, dostats)
    double **a;
    int n;
    int block_size;
    int MyNum;
    int dostats;
{
    unsigned int i;
    unsigned int myrs;
    unsigned int myrf;
    unsigned int mydone;
    struct LocalCopies *lc;
    lc = (struct LocalCopies *) malloc(sizeof(struct LocalCopies));
    if (lc == __null) {
        fprintf(__stderrp,"Proc %d could not malloc memory for lc\n",MyNum);
        exit(-1);
    }
    lc->t_in_fac = 0.0;
    lc->t_in_solve = 0.0;
    lc->t_in_mod = 0.0;
    lc->t_in_bar = 0.0;
    BARRIER(Global->start, P);
    TouchA(block_size, MyNum);
    BARRIER(Global->start, P);
    if ((MyNum == 0) || (dostats)) {
        CLOCK(myrs);
    }
    lu(n, block_size, MyNum, lc, dostats);
    if ((MyNum == 0) || (dostats)) {
        CLOCK(mydone);
    }
    BARRIER(Global->start, P);
    if ((MyNum == 0) || (dostats)) {
        Global->t_in_fac[MyNum] = lc->t_in_fac;
        Global->t_in_solve[MyNum] = lc->t_in_solve;
        Global->t_in_mod[MyNum] = lc->t_in_mod;
        Global->t_in_bar[MyNum] = lc->t_in_bar;
        Global->completion[MyNum] = mydone-myrs;
    }
    if (MyNum == 0) {
        CLOCK(myrf);
        Global->rs = myrs;
        Global->done = mydone;
        Global->rf = myrf;
    }
}
void lu0(a, n, stride)
    double *a;
    int n;
    int stride;
{
    int j;
    int k;
    int length;
    double alpha;
    for (k=0; k<n; k++) {
        for (j=k+1; j<n; j++) {
            a[k+j*stride] /= a[k+k*stride];
            alpha = -a[k+j*stride];
            length = n-k-1;
            daxpy(&a[k+1+j*stride], &a[k+1+k*stride], n-k-1, alpha);
        }
    }
}
void bdiv(a, diag, stride_a, stride_diag, dimi, dimk)
    double *a;
    double *diag;
    int stride_a;
    int stride_diag;
    int dimi;
    int dimk;
{
    int j;
    int k;
    double alpha;
    for (k=0; k<dimk; k++) {
        for (j=k+1; j<dimk; j++) {
            alpha = -diag[k+j*stride_diag];
            daxpy(&a[j*stride_a], &a[k*stride_a], dimi, alpha);
        }
    }
}
void bmodd(a, c, dimi, dimj, stride_a, stride_c)
    double *a;
    double *c;
    int dimi;
    int dimj;
    int stride_a;
    int stride_c;
{
    int i;
    int j;
    int k;
    int length;
    double alpha;
    for (k=0; k<dimi; k++) {
        for (j=0; j<dimj; j++) {
            c[k+j*stride_c] /= a[k+k*stride_a];
            alpha = -c[k+j*stride_c];
            length = dimi - k - 1;
            daxpy(&c[k+1+j*stride_c], &a[k+1+k*stride_a], dimi-k-1, alpha);
        }
    }
}
void bmod(a, b, c, dimi, dimj, dimk, stridea, strideb, stridec)
    double *a;
    double *b;
    double *c;
    int dimi;
    int dimj;
    int dimk;
    int stridea;
    int strideb;
    int stridec;
{
    int i;
    int j;
    int k;
    double alpha;
    for (k=0; k<dimk; k++) {
        for (j=0; j<dimj; j++) {
            alpha = -b[k+j*strideb];
            daxpy(&c[j*stridec], &a[k*stridea], dimi, alpha);
        }
    }
}
void daxpy(a, b, n, alpha)
    double *a;
    double *b;
    double alpha;
    int n;
{
    int i;
    for (i=0; i<n; i++) {
        a[i] += alpha*b[i];
    }
}
int BlockOwner(I, J)
    int I;
    int J;
{
    return((J%num_cols) + (I%num_rows)*num_cols);
}
void lu(n, bs, MyNum, lc, dostats)
    int n;
    int bs;
    int MyNum;
    struct LocalCopies *lc;
    int dostats;
{
    int i, il, j, jl, k, kl;
    int I, J, K;
    double *A, *B, *C, *D;
    int dimI, dimJ, dimK;
    int strI, strJ, strK;
    unsigned int t1, t2, t3, t4, t11, t22;
    int diagowner;
    int colowner;
    for (k=0, K=0; k<n; k+=bs, K++) {
        kl = k + bs;
        if (kl > n) {
            kl = n;
            strK = kl - k;
        } else {
            strK = bs;
        }
        if ((MyNum == 0) || (dostats)) {
            CLOCK(t1);
        }
        diagowner = BlockOwner(K, K);
        if (diagowner == MyNum) {
            A = a[K+K*nblocks];
            lu0(A, strK, strK);
        }
        if ((MyNum == 0) || (dostats)) {
            CLOCK(t11);
        }
        BARRIER(Global->start, P);
        if ((MyNum == 0) || (dostats)) {
            CLOCK(t2);
        }
        D = a[K+K*nblocks];
        for (i=kl, I=K+1; i<n; i+=bs, I++) {
            if (BlockOwner(I, K) == MyNum) {
                il = i + bs;
                if (il > n) {
                    il = n;
                    strI = il - i;
                } else {
                    strI = bs;
                }
                A = a[I+K*nblocks];
                bdiv(A, D, strI, strK, strI, strK);
            }
        }
        for (j=kl, J=K+1; j<n; j+=bs, J++) {
            if (BlockOwner(K, J) == MyNum) {
                jl = j+bs;
                if (jl > n) {
                    jl = n;
                    strJ = jl - j;
                } else {
                    strJ = bs;
                }
                A = a[K+J*nblocks];
                bmodd(D, A, strK, strJ, strK, strK);
            }
        }
        if ((MyNum == 0) || (dostats)) {
            CLOCK(t22);
        }
        BARRIER(Global->start, P);
        if ((MyNum == 0) || (dostats)) {
            CLOCK(t3);
        }
        for (i=kl, I=K+1; i<n; i+=bs, I++) {
            il = i+bs;
            if (il > n) {
                il = n;
                strI = il - i;
            } else {
                strI = bs;
            }
            colowner = BlockOwner(I,K);
            A = a[I+K*nblocks];
            for (j=kl, J=K+1; j<n; j+=bs, J++) {
                jl = j + bs;
                if (jl > n) {
                    jl = n;
                    strJ= jl - j;
                } else {
                    strJ = bs;
                }
                if (BlockOwner(I, J) == MyNum) {
                    B = a[K+J*nblocks];
                    C = a[I+J*nblocks];
                    bmod(A, B, C, strI, strJ, strK, strI, strK, strI);
                }
            }
        }
        if ((MyNum == 0) || (dostats)) {
            CLOCK(t4);
            lc->t_in_fac += (t11-t1);
            lc->t_in_solve += (t22-t2);
            lc->t_in_mod += (t4-t3);
            lc->t_in_bar += (t2-t11) + (t3-t22);
        }
    }
}
void InitA(rhs)
    double *rhs;
{
    int i, j;
    int ii, jj;
    int edge;
    int ibs;
    int jbs, skip;
    srand48((long) 1);
    edge = n%block_size;
    for (j=0; j<n; j++) {
        for (i=0; i<n; i++) {
            if ((n - i) <= edge) {
                ibs = edge;
                ibs = n-edge;
                skip = edge;
            } else {
                ibs = block_size;
                skip = block_size;
            }
            if ((n - j) <= edge) {
                jbs = edge;
                jbs = n-edge;
            } else {
                jbs = block_size;
            }
            ii = (i/block_size) + (j/block_size)*nblocks;
            jj = (i%ibs)+(j%jbs)*skip;
            a[ii][jj] = ((double) lrand48())/32767.0;
            if (i == j) {
                a[ii][jj] *= 10;
            }
        }
    }
    for (j=0; j<n; j++) {
        rhs[j] = 0.0;
    }
    for (j=0; j<n; j++) {
        for (i=0; i<n; i++) {
            if ((n - i) <= edge) {
                ibs = edge;
                ibs = n-edge;
                skip = edge;
            } else {
                ibs = block_size;
                skip = block_size;
            }
            if ((n - j) <= edge) {
                jbs = edge;
                jbs = n-edge;
            } else {
                jbs = block_size;
            }
            ii = (i/block_size) + (j/block_size)*nblocks;
            jj = (i%ibs)+(j%jbs)*skip;
            rhs[i] += a[ii][jj];
        }
    }
}
double TouchA(bs, MyNum)
    int bs;
    int MyNum;
{
    int i, j, I, J;
    double tot = 0.0;
    int ibs;
    int jbs;
    for (J=0; J<nblocks; J++) {
        for (I=0; I<nblocks; I++) {
            if (BlockOwner(I, J) == MyNum) {
                if (J == nblocks-1) {
                    jbs = n%bs;
                    if (jbs == 0) {
                        jbs = bs;
                    }
                } else {
                    jbs = bs;
                }
                if (I == nblocks-1) {
                    ibs = n%bs;
                    if (ibs == 0) {
                        ibs = bs;
                    }
                } else {
                    ibs = bs;
                }
                for (j=0; j<jbs; j++) {
                    for (i=0; i<ibs; i++) {
                        tot += a[I+J*nblocks][i+j*ibs];
                    }
                }
            }
        }
    }
    return(tot);
}
void PrintA()
{
    int i, j;
    int ii, jj;
    int edge;
    int ibs, jbs, skip;
    edge = n%block_size;
    for (i=0; i<n; i++) {
        for (j=0; j<n; j++) {
            if ((n - i) <= edge) {
                ibs = edge;
                ibs = n-edge;
                skip = edge;
            } else {
                ibs = block_size;
                skip = block_size;
            }
            if ((n - j) <= edge) {
                jbs = edge;
                jbs = n-edge;
            } else {
                jbs = block_size;
            }
            ii = (i/block_size) + (j/block_size)*nblocks;
            jj = (i%ibs)+(j%jbs)*skip;
            printf("%8.1f ", a[ii][jj]);
        }
        printf("\n");
    }
    fflush(__stdoutp);
}
void CheckResult(n, a, rhs)
    int n;
    double **a;
    double *rhs;
{
    int i, j, bogus = 0;
    double *y, diff, max_diff;
    int ii, jj;
    int edge;
    int ibs, jbs, skip;
    edge = n%block_size;
    y = (double *) malloc(n*sizeof(double));
    if (y == __null) {
        printerr("Could not malloc memory for y\n");
        exit(-1);
    }
    for (j=0; j<n; j++) {
        y[j] = rhs[j];
    }
    for (j=0; j<n; j++) {
        if ((n - j) <= edge) {
            jbs = edge;
            jbs = n-edge;
            skip = edge;
        } else {
            jbs = block_size;
            skip = block_size;
        }
        ii = (j/block_size) + (j/block_size)*nblocks;
        jj = (j%jbs)+(j%jbs)*skip;
        y[j] = y[j]/a[ii][jj];
        for (i=j+1; i<n; i++) {
            if ((n - i) <= edge) {
                ibs = edge;
                ibs = n-edge;
                skip = edge;
            } else {
                ibs = block_size;
                skip = block_size;
            }
            ii = (i/block_size) + (j/block_size)*nblocks;
            jj = (i%ibs)+(j%jbs)*skip;
            y[i] -= a[ii][jj]*y[j];
        }
    }
    for (j=n-1; j>=0; j--) {
        for (i=0; i<j; i++) {
            if ((n - i) <= edge) {
                ibs = edge;
                ibs = n-edge;
                skip = edge;
            } else {
                ibs = block_size;
                skip = block_size;
            }
            if ((n - j) <= edge) {
                jbs = edge;
                jbs = n-edge;
            } else {
                jbs = block_size;
            }
            ii = (i/block_size) + (j/block_size)*nblocks;
            jj = (i%ibs)+(j%jbs)*skip;
            y[i] -= a[ii][jj]*y[j];
        }
    }
    max_diff = 0.0;
    for (j=0; j<n; j++) {
        diff = y[j] - 1.0;
        if (fabs(diff) > 0.00001) {
            bogus = 1;
            max_diff = diff;
        }
    }
    if (bogus) {
        printf("TEST FAILED: (%.5f diff)\n", max_diff);
    } else {
        printf("TEST PASSED\n");
    }
    free(y);
}
void printerr(s)
    char *s;
{
    fprintf(__stderrp,"ERROR: %s\n",s);
}
