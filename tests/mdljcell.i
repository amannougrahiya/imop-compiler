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

typedef __darwin_clock_t clock_t;
typedef __darwin_time_t time_t;
struct timespec
{
    __darwin_time_t tv_sec;
    long tv_nsec;
};
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

void *memchr(const void *__s, int __c, size_t __n);
int memcmp(const void *__s1, const void *__s2, size_t __n);
void *memcpy(void *__dst, const void *__src, size_t __n);
void *memmove(void *__dst, const void *__src, size_t __len);
void *memset(void *__b, int __c, size_t __len);
char *strcat(char *__s1, const char *__s2);
char *strchr(const char *__s, int __c);
int strcmp(const char *__s1, const char *__s2);
int strcoll(const char *__s1, const char *__s2);
char *strcpy(char *__dst, const char *__src);
size_t strcspn(const char *__s, const char *__charset);
char *strerror(int __errnum) __asm("_" "strerror" );
size_t strlen(const char *__s);
char *strncat(char *__s1, const char *__s2, size_t __n);
int strncmp(const char *__s1, const char *__s2, size_t __n);
char *strncpy(char *__dst, const char *__src, size_t __n);
char *strpbrk(const char *__s, const char *__charset);
char *strrchr(const char *__s, int __c);
size_t strspn(const char *__s, const char *__charset);
char *strstr(const char *__big, const char *__little);
char *strtok(char *__str, const char *__sep);
size_t strxfrm(char *__s1, const char *__s2, size_t __n);


char *strtok_r(char *__str, const char *__sep, char **__lasts);


int strerror_r(int __errnum, char *__strerrbuf, size_t __buflen);
char *strdup(const char *__s1);
void *memccpy(void *__dst, const void *__src, int __c, size_t __n);


char *stpcpy(char *__dst, const char *__src);
char *stpncpy(char *__dst, const char *__src, size_t __n) ;
char *strndup(const char *__s1, size_t __n) ;
size_t strnlen(const char *__s1, size_t __n) ;
char *strsignal(int __sig);

typedef __darwin_size_t rsize_t;
typedef int errno_t;

errno_t memset_s(void *__s, rsize_t __smax, int __c, rsize_t __n) ;


void *memmem(const void *__big, size_t __big_len, const void *__little, size_t __little_len) ;
void memset_pattern4(void *__b, const void *__pattern4, size_t __len) ;
void memset_pattern8(void *__b, const void *__pattern8, size_t __len) ;
void memset_pattern16(void *__b, const void *__pattern16, size_t __len) ;
char *strcasestr(const char *__big, const char *__little);
char *strnstr(const char *__big, const char *__little, size_t __len);
size_t strlcat(char *__dst, const char *__source, size_t __size);
size_t strlcpy(char *__dst, const char *__source, size_t __size);
void strmode(int __mode, char *__bp);
char *strsep(char **__stringp, const char *__delim);
void swab(const void * restrict, void * restrict, ssize_t);


int timingsafe_bcmp(const void *__b1, const void *__b2, size_t __len);


int bcmp(const void *, const void *, size_t) ;
void bcopy(const void *, void *, size_t) ;
void bzero(void *, size_t) ;
char *index(const char *, int) ;
char *rindex(const char *, int) ;
int ffs(int);
int strcasecmp(const char *, const char *);
int strncasecmp(const char *, const char *, size_t);


int ffsl(long) ;
int ffsll(long long) ;
int fls(int) ;
int flsl(long) ;
int flsll(long long) ;

struct icord{
    int ix;
    int iy;
    int iz;
};
struct cord{
    double x;
    double y;
    double z;
};
double ljf(double **r,int np,double bl, int i, int j)
{
    double rc=6.25;
    double ds2,dsi6,vij,fij,fijx,fijy,fijz,dx,dy,dz;
    dx=r[j][0]-r[i][0];
    dy=r[j][1]-r[i][1];
    dz=r[j][2]-r[i][2];
    dx-=bl*rint(dx/bl);
    dy-=bl*rint(dy/bl);
    dz-=bl*rint(dz/bl);
    ds2=dx*dx+dy*dy+dz*dz;
    if(ds2<=rc){
        dsi6=1.0/ds2/ds2/ds2;
        vij=(dsi6-1.00)*dsi6*4.0;
        fij=(48.0*dsi6-24.00)*dsi6/ds2;
        if(fabs(ds2)<0.25){
            printf("%lf %lf %lf\n",fij,vij,ds2);
            printf("%lf %lf %lf\n",dx,dy,dz);
            printf("%d %d\n",i,j);
        }
        fijx=fij*dx;
        fijy=fij*dy;
        fijz=fij*dz;
        r[i][3]-=fijx;
        r[i][4]-=fijy;
        r[i][5]-=fijz;
        r[j][3]+=fijx;
        r[j][4]+=fijy;
        r[j][5]+=fijz;
        return vij;
    }
    else return 0.0;
}
void gencell(int mci, int ncx,int **cn)
{
    int i,j,k,l;
    l=0;
    for(i=0;i<ncx;i++){
        for(j=0;j<ncx;j++){
            for(k=0;k<ncx;k++){
                l=(i+ncx)%ncx+((j+ncx)%ncx)*ncx+((k+ncx)%ncx)*ncx*ncx;
                cn[l][0]=(i+1 +ncx)%ncx+((j+ncx)%ncx)*ncx+((k+ncx)%ncx)*ncx*ncx;
                cn[l][1]=(i+1 +ncx)%ncx+((j+1 +ncx)%ncx)*ncx+((k+ncx)%ncx)*ncx*ncx;
                cn[l][2]=(i+ncx)%ncx+((j+1 +ncx)%ncx)*ncx+((k+ncx)%ncx)*ncx*ncx;
                cn[l][3]=(i-1 +ncx)%ncx+((j+1 +ncx)%ncx)*ncx+((k+ncx)%ncx)*ncx*ncx;
                cn[l][4]=(i+1 +ncx)%ncx+((j+ncx)%ncx)*ncx+((k-1 +ncx)%ncx)*ncx*ncx;
                cn[l][5]=(i+1 +ncx)%ncx+((j+1 +ncx)%ncx)*ncx+((k-1 +ncx)%ncx)*ncx*ncx;
                cn[l][6]=(i+ncx)%ncx+((j+1 +ncx)%ncx)*ncx+((k-1 +ncx)%ncx)*ncx*ncx;
                cn[l][7]=(i-1 +ncx)%ncx+((j+1 +ncx)%ncx)*ncx+((k-1 +ncx)%ncx)*ncx*ncx;
                cn[l][8]=(i+1 +ncx)%ncx+((j+ncx)%ncx)*ncx+((k+1 +ncx)%ncx)*ncx*ncx;
                cn[l][9]=(i+1 +ncx)%ncx+((j+1 +ncx)%ncx)*ncx+((k+1 +ncx)%ncx)*ncx*ncx;
                cn[l][10]=(i+ncx)%ncx+((j+1 +ncx)%ncx)*ncx+((k+1 +ncx)%ncx)*ncx*ncx;
                cn[l][11]=(i-1 +ncx)%ncx+((j+1 +ncx)%ncx)*ncx+((k+1 +ncx)%ncx)*ncx*ncx;
                cn[l][12]=(i+ncx)%ncx+((j+ncx)%ncx)*ncx+((k+1 +ncx)%ncx)*ncx*ncx;
            }
        }
    }
}
double forcecell(int np, double bl, double **r)
{
    static double cl,***g;
    double v,dx,dy,dz,de;
    int i,j,k,l,m,n,o,ix,iy,iz,tn;
    static int ncx,mpc,mci,**cn,p=0,**cli,mcl,lm;
    if(p==0){
        cl=2.5+0.0;
        ncx=(int)(bl/cl);
        cl=bl/(double)ncx;
        if(ncx<4){
            printf(" the box size is small for cell list\n ");
            exit(1);
        }
        mci=ncx*ncx*ncx;
        cn=(int**)calloc(mci,sizeof(int*));
        for(i=0;i<mci;i++)cn[i]=(int*)calloc(13,sizeof(int));
        gencell(mci,ncx,cn);
        de=(double)np/(bl*bl*bl)*3.0;
        de=2.0;
        mcl=(int)(de*cl*cl*cl);
        cli=(int **)calloc(mci,sizeof(int*));
        for(i=0;i<mci;i++)cli[i]=(int*)calloc(mcl+1,sizeof(int));
        lm=omp_get_max_threads();
        g=(double***)calloc(lm,sizeof(double**));
        for(i=0;i<lm;i++){
            g[i]=(double**)calloc(np,sizeof(double*));
            for(j=0;j<np;j++)g[i][j]=(double*)calloc(6,sizeof(double));
        }
    }
    p++;
    for(i=0;i<mci;i++){
        cli[i][mcl]=0;
    }
    for(i=0;i<np;i++){
        dx=(r[i][0]-bl*(rint(r[i][0]/bl)-0.5))/cl;
        dy=(r[i][1]-bl*(rint(r[i][1]/bl)-0.5))/cl;
        dz=(r[i][2]-bl*(rint(r[i][2]/bl)-0.5))/cl;
        ix=(int)dx;
        iy=(int)dy;
        iz=(int)dz;
        j=(ix+ncx)%ncx+((iy+ncx)%ncx)*ncx+((iz+ncx)%ncx)*ncx*ncx;
        cli[j][mcl]++;
        if(cli[j][mcl]>=mcl){
            printf("forcecell.c: cell size crossed dimension of the array\n");
            exit(1);
        }
        k=cli[j][mcl]-1;
        cli[j][k]=i;
    }
    for(i=0;i<np;i++){
        r[i][6]=0.0;
        r[i][7]=0.0;
        r[i][8]=0.0;
    }
    for(i=0;i<lm;i++){
        for(j=0;j<np;j++){
            g[i][j][0]=r[j][0];
            g[i][j][1]=r[j][1];
            g[i][j][2]=r[j][2];
            g[i][j][3]=0.0;
            g[i][j][4]=0.0;
            g[i][j][5]=0.0;
        }
    }
    v=0;
#pragma omp parallel for firstprivate(np,bl,mci,mcl) private(j,k,tn,l,m,n,o) reduction(+:v) schedule(static)
    for(i=0;i<mci;i++){
        tn=omp_get_thread_num();
        for(j=0;j<cli[i][mcl];j++){
            k=cli[i][j];
            for(l=j+1;l<cli[i][mcl];l++){
                m=cli[i][l];
                v+=ljf(g[tn],np,bl,m,k);
            }
            for(n=0;n<13;n++){
                o=cn[i][n];
                for(l=0;l<cli[o][mcl];l++){
                    m=cli[o][l];
                    v+=ljf(g[tn],np,bl,m,k);
                }
            }
        }
    }
    for(i=0;i<np;i++){
        for(j=0;j<lm;j++){
            r[i][6]+=g[j][i][3];
            r[i][7]+=g[j][i][4];
            r[i][8]+=g[j][i][5];
        }
    }
    return v;
}
float ran3(long *idum)
{
    static int inext,inextp;
    static long ma[56];
    static int iff=0;
    long mj,mk;
    int i,ii,k;
    if (*idum < 0 || iff == 0) {
        iff=1;
        mj=labs(161803398 -labs(*idum));
        mj %= 1000000000;
        ma[55]=mj;
        mk=1;
        for (i=1;i<=54;i++) {
            ii=(21*i) % 55;
            ma[ii]=mk;
            mk=mj-mk;
            if (mk < 0) mk += 1000000000;
            mj=ma[ii];
        }
        for (k=1;k<=4;k++)
            for (i=1;i<=55;i++) {
                ma[i] -= ma[1+(i+30) % 55];
                if (ma[i] < 0) ma[i] += 1000000000;
            }
        inext=0;
        inextp=31;
        *idum=1;
    }
    if (++inext == 56) inext=1;
    if (++inextp == 56) inextp=1;
    mj=ma[inext]-ma[inextp];
    if (mj < 0) mj += 1000000000;
    ma[inext]=mj;
    return mj*(1.0/1000000000);
}
double gau(long int *idum)
{
    static int iset=0,i,j;
    static float gset;
    float fac,rsq,v1,v2;
    if (idum < 0) iset=0;
    if (iset == 0){
        do {
            v1=2.0*ran3(idum)-1.0;
            v2=2.0*ran3(idum)-1.0;
            rsq=v1*v1+v2*v2;
        } while (rsq >= 1.0 || rsq == 0.0);
        fac=sqrt(-2.0*log(rsq)/rsq);
        gset=v1*fac;
        iset=1;
        return(v2*fac);
    } else {
        iset=0;
        return(gset);
    }
}
void grand(int np, struct cord *c)
{
    static long idum;
    static int ctr=0;
    int x,is,i;
    if(ctr==0){
        idum=(long)time(((void *)0));
    }
    for(i=0;i<np;i++){
        c[i].x=gau(&idum);
        c[i].y=gau(&idum);
        c[i].z=gau(&idum);
    }
}
void rdfc(double bl,int np,int mbil,double dr,double **r,double *gr)
{
    int i,j,k1,jj,ii;
    double dis1,dis2,dis3,dis4;
    double ds2,ds,rr[3],rd[3],dry;
    for(i=0;i<np-1;i++){
        for(j=i+1;j<np;j++){
            rd[0]=r[i][0]-r[j][0];
            rd[1]=r[i][1]-r[j][1];
            rd[2]=r[i][2]-r[j][2];
            rd[0]-=bl*rint(rd[0]/bl);
            rd[1]-=bl*rint(rd[1]/bl);
            rd[2]-=bl*rint(rd[2]/bl);
            ds2=rd[0]*rd[0]+rd[1]*rd[1]+rd[2]*rd[2];
            ds=sqrt(ds2);
            k1=(int)(ds/dr);
            if(k1<mbil){
                gr[k1]+=2.0;
            }
        }
    }
    return;
}
void rdf(double bl,int np,double de,double **r)
{
    int i,j,k1,jj,ii,tn;
    static int k=0,l=0,mbil,nt,scnf,cnt;
    double cvn,dis1,dis2,dis3,dis4;
    double ds2,ds,rr[3],rd[3],dry;
    static double *cbn,*gr,**grp,dr=0.01,***g;
    k++;
    if(k==1){
        nt=omp_get_max_threads();
        scnf=nt*100;
        g=(double***)calloc(scnf,sizeof(double**));
        for(i=0;i<scnf;i++){
            g[i]=(double**)calloc(np,sizeof(double*));
            for(j=0;j<np;j++)g[i][j]=(double*)calloc(3,sizeof(double));
        }
        mbil=(bl/2.0/dr);
        cbn=(double*)calloc(mbil,sizeof(double));
        gr=(double*)calloc(mbil,sizeof(double));
        grp=(double**)calloc(mbil,sizeof(double*));
        for(i=0;i<nt;i++)grp[i]=(double*)calloc(mbil,sizeof(double));
        for(i=0;i<mbil;i++)gr[i]=cbn[i]=0.0;
        cvn=4.0*3.14159*de/3.0;
        dis1=dr;
        dis2=0;
        for(j=0;j<mbil;j++){
            dis3=dis1*dis1;
            dis4=dis2*dis2;
            cbn[j]=cvn*(dis3*dis1-dis4*dis2);
            dis1+=dr;
            dis2+=dr;
        }
    }
    printf("grp.c:%d %d\n",k,scnf);
    if(cnt<scnf){
        for(i=0;i<np;i++){
            g[cnt][i][0]=r[i][0];
            g[cnt][i][1]=r[i][1];
            g[cnt][i][2]=r[i][2];
        }
        cnt++;
        return;
    }
    l++;
#pragma omp parallel for firstprivate(bl,np,mbil,dr) private(tn) schedule(static)
    for(i=0;i<scnf;i++){
        tn=omp_get_thread_num();
        rdfc(bl,np,mbil,dr,g[i],grp[tn]);
    }
    for(i=0;i<mbil;i++){
        gr[i]=0.0;
        for(j=0;j<nt;j++){
            gr[i]+=grp[j][i];
        }
    }
    FILE *fprdf=fopen("rdf.dat","w");
    for(i=0;i<mbil;i++){
        dis1=((double)i+0.5)*dr;
        dis2=gr[i]/cbn[i]/((double)(l*scnf*np));
        fprintf(fprdf,"%lf %lf\n",dis1,dis2);
    }
    fclose(fprdf);
    cnt=0;
    return ;
}
void mvvlt(int np, double **r,double dt2b2, double dt, double dtb2)
{
    int i;
#pragma omp parallel for firstprivate(np,dtb2,dt,dt2b2) schedule(static)
    for(i=0;i<np;i++){
        r[i][3]+=dtb2*(r[i][6]+r[i][9]);
        r[i][4]+=dtb2*(r[i][7]+r[i][10]);
        r[i][5]+=dtb2*(r[i][8]+r[i][11]);
        r[i][0]+=r[i][3]*dt+r[i][6]*dt2b2;
        r[i][1]+=r[i][4]*dt+r[i][7]*dt2b2;
        r[i][2]+=r[i][5]*dt+r[i][8]*dt2b2;
        r[i][9]=r[i][6];
        r[i][10]=r[i][7];
        r[i][11]=r[i][8];
    }
}
void traj(double *tc,long *stp, int np, double **r, int term,double dt )
{
    static int fsize=268435456,rsize,nr,k=0,m=0,n=0;
    static long *st;
    static double *ts;
    static double ***g;
    static char fname[20], fno[5],pre[]="trj/t",post[]=".bdt";
    static char rfname[20], rpre[]="trj/r";
    int i,j;
    if(k==0){
        rsize=(int)sizeof(long)+(int)sizeof(double)+6*np*(int)(sizeof(double));
        nr=fsize/rsize;
        printf("trjrst.c: rsize = %d nr= %d \n",rsize, nr);
        g=(double***)calloc(nr,sizeof(double**));
        st=(long*)calloc(nr,sizeof(long));
        ts=(double*)calloc(nr,sizeof(double));
        for(i=0;i<nr;i++){
            g[i]=(double**)calloc(np,sizeof(double*));
            for(j=0;j<np;j++)g[i][j]=(double*)calloc(6,sizeof(double));
        }
        k++;
        i=system("if [ ! -d trj ]; then mkdir trj; fi");
        FILE *inp=fopen("trj/res.dat","r");
        if(inp==((void *)0))return;
        else {
            fscanf(inp,"%d %d %d %d",&i,&n,&m,&nr);
            fclose(inp);
            if(i==1){
                __builtin___sprintf_chk (fno, 0, __builtin_object_size (fno, 1), "%d",n);
                __builtin___strcpy_chk (rfname, rpre, __builtin_object_size (rfname, 1));
                __builtin___strcat_chk (rfname, fno, __builtin_object_size (rfname, 1));
                __builtin___strcat_chk (rfname, post, __builtin_object_size (rfname, 1));
                printf("trjrst.c: file to restore %s\n",rfname);
                inp=fopen(rfname,"r");
                if(inp==((void *)0)){
                    printf(" trjrst.c: %s :restart file not found exiting",fname);
                    exit(1);
                }
                fread(stp,sizeof(long),1,inp);
                fread(tc,sizeof(double),1,inp);
                for(i=0;i<np;i++)fread(r[i],sizeof(double),12,inp);
                fclose(inp);
                *stp++;
                *tc+=dt;
                if(m<nr){
                    __builtin___sprintf_chk (fno, 0, __builtin_object_size (fno, 1), "%d",n);
                    __builtin___strcpy_chk (fname, pre, __builtin_object_size (fname, 1));
                    __builtin___strcat_chk (fname, fno, __builtin_object_size (fname, 1));
                    __builtin___strcat_chk (fname, post, __builtin_object_size (fname, 1));
                    printf("trjrst.c: storing %s\n",fname);
                    inp=fopen(fname,"r");
                    for(i=0;i<m;i++){
                        fread(&st[i],sizeof(long),1,inp);
                        fread(&ts[i],sizeof(double),1,inp);
                        for(j=0;j<np;j++){
                            fread(g[i][j],sizeof(double),6,inp);
                        }
                    }
                    fclose(inp);
                }else
                {
                    n++;
                    m=0;
                }
            }
            return;
        }
    }
    for(i=0;i<np;i++){
        g[m][i][0]=(double)r[i][0];
        g[m][i][1]=(double)r[i][1];
        g[m][i][2]=(double)r[i][2];
        g[m][i][3]=(double)r[i][3];
        g[m][i][4]=(double)r[i][4];
        g[m][i][5]=(double)r[i][5];
    }
    st[m]=*stp;
    ts[m]=*tc;
    m++;
    if(m==nr || term==1 ){
        __builtin___sprintf_chk (fno, 0, __builtin_object_size (fno, 1), "%d",n);
        __builtin___strcpy_chk (fname, pre, __builtin_object_size (fname, 1));
        __builtin___strcat_chk (fname, fno, __builtin_object_size (fname, 1));
        __builtin___strcat_chk (fname, post, __builtin_object_size (fname, 1));
        printf("trjrst.c: storing %s\n",fname);
        FILE *inp=fopen(fname,"w");
        FILE *chkf=fopen("out.dat","a");
        for(i=0;i<m;i++){
            fwrite(&st[i],sizeof(long),1,inp);
            fprintf(chkf,"step: %ld\n",st[i]);
            fwrite(&ts[i],sizeof(double),1,inp);
            for(j=0;j<np;j++){
                fwrite(g[i][j],sizeof(double),6,inp);
            }
        }
        fclose(inp);
        fclose(chkf);
        __builtin___sprintf_chk (fno, 0, __builtin_object_size (fno, 1), "%d",n);
        __builtin___strcpy_chk (rfname, rpre, __builtin_object_size (rfname, 1));
        __builtin___strcat_chk (rfname, fno, __builtin_object_size (rfname, 1));
        __builtin___strcat_chk (rfname, post, __builtin_object_size (rfname, 1));
        printf("%s\n",rfname);
        inp=fopen(rfname,"w");
        fwrite(stp,sizeof(long),1,inp);
        fwrite(tc,sizeof(double),1,inp);
        for(i=0;i<np;i++)fwrite(r[i],sizeof(double),12,inp);
        fclose(inp);
        inp=fopen("trj/res.dat","w");
        fprintf(inp,"%d %d %d %d\n",1,n,m,nr);
        fclose(inp);
        n++;
        m=0;
    }
    return;
}
double vscl(int np, double **r, double te, int neq, int stp )
{
    int i;
    double v2,sc,temp;
    v2=0.0;
    for(i=0;i<np;i++){
        v2+=r[i][3]*r[i][3]+r[i][4]*r[i][4]+r[i][5]*r[i][5];
    }
    temp=v2/(double)np/3.0;
    sc=sqrt(temp/te);
    if(stp<neq){
        for(i=0;i<np;i++){
            r[i][3]/=sc;
            r[i][4]/=sc;
            r[i][5]/=sc;
        }
    }
    return v2;
}
int main()
{
    int ns,nsi,np,neq,rest;
    double tf,ti=0.0,tc,si,teq;
    long int i,j,k;
    double ke,pe,te,de,dt,bl,dt2b2,dtb2,sx,sy,sz;
    FILE *inp;
    double **r;
    inp=fopen("inp.dat","r");
    if(inp!=((void *)0)){
        fscanf(inp,"%d %lf %lf %lf %lf %lf %lf",&np,&tf,&si,&teq,&te,&de,&dt);
        fclose(inp);
    }
    else{
        printf("mds.c:ERROR: input parameter file not found\n");
    }
    bl=pow((double)np/de,1.0/3.0);
    r=(double**)calloc(np,sizeof(double*));
    for(i=0;i<np;i++)r[i]=(double*)calloc(12,sizeof(double));
    inp=fopen("crd.dat","r");
    if(inp==((void *)0))
        sx=0.0;sy=0.0;sz=0.0;
    for(i=0;i<np;i++){
        for(j=0;j<6;j++){
            k=fscanf(inp,"%lf ",&r[i][j]);
            if(k!=1){
                printf("Coordinates are missing run init\n");
                exit(1);
            }
        }
        sx+=r[i][3];
        sy+=r[i][4];
        sz+=r[i][5];
    }
    sx/=-(double)np;
    sy/=-(double)np;
    sz/=-(double)np;
    for(i=0;i<np;i++){
        r[i][3]+=sx;
        r[i][4]+=sy;
        r[i][5]+=sz;
    }
    fclose(inp);
    inp=fopen("pcheck.dat","w");
    fprintf(inp,"Paticles= %d\n",np);
    fprintf(inp,"Time initial= %lf\n",ti);
    fprintf(inp,"Time final= %lf\n",ti);
    fprintf(inp,"Time steps= %lf\n",dt);
    fprintf(inp,"Density= %lf\n",de);
    fprintf(inp,"Temperature= %lf\n",te);
    fprintf(inp,"Box Length= %lf\n",bl);
    fclose(inp);
    tf+=teq;
    dt2b2=dt*dt/2.0;
    dtb2=dt/2.0;
    i=0;
    nsi=(int)(si/dt);
    neq=(int)(teq/dt);
    tc=ti;
    traj( &tc,&i,np,r,0,dt);
    while(tc<=tf){
        pe=forcecell(np,bl,r);
        ke=vscl(np,r,te,neq,i);
        mvvlt(np,r,dt2b2,dt,dtb2);
        if(i%nsi==0){
            printf("%ld %lf %lf %lf %lf\n",i,tc,pe/(double)np,ke/(double)np,(ke+pe)/(double)np);
            if(i>neq){
                traj( &tc,&i,np,r,0,dt);
                rdf(bl,np,de,r);
            }
        }
        tc+=dt;
        i++;
    }
    traj( &tc,&i,np,r,1,dt);
    inp=fopen("fcrd.dat","w");
    for(i=0;i<np;i++){
        fprintf(inp,"%lf %lf %lf %lf %lf %lf\n",r[i][0],r[i][1],r[i][2],r[i][3],r[i][4],r[i][5]);
    }
    fclose(inp);
    return 0 ;
}
