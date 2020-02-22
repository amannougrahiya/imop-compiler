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

extern int bots_sequential_flag;
extern int bots_benchmark_flag;
extern int bots_check_flag;
extern int bots_result;
extern int bots_output_format;
extern int bots_print_header;
extern char bots_name[];
extern char bots_parameters[];
extern char bots_model[];
extern char bots_resources[];
extern char bots_exec_date[];
extern char bots_exec_message[];
extern char bots_comp_date[];
extern char bots_comp_message[];
extern char bots_cc[];
extern char bots_cflags[];
extern char bots_ld[];
extern char bots_ldflags[];
extern double bots_time_program;
extern double bots_time_sequential;
extern unsigned long long bots_number_of_tasks;
extern char bots_cutoff[];
extern int bots_cutoff_value;
extern int bots_app_cutoff_value;
extern int bots_app_cutoff_value_1;
extern int bots_app_cutoff_value_2;
extern int bots_arg_size;
extern int bots_arg_size_1;
extern int bots_arg_size_2;
long bots_usecs();
void bots_error(int error, char *message);
void bots_warning(int warning, char *message);
typedef enum { BOTS_VERBOSE_NONE=0,
               BOTS_VERBOSE_DEFAULT,
               BOTS_VERBOSE_DEBUG } bots_verbose_mode_t;
extern bots_verbose_mode_t bots_verbose_mode;
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
typedef double REAL;
typedef struct {
     REAL re, im;
} COMPLEX;
void compute_w_coefficients(int n, int a, int b, COMPLEX * W);
void compute_w_coefficients_seq(int n, int a, int b, COMPLEX * W);
int factor(int n);
void unshuffle(int a, int b, COMPLEX * in, COMPLEX * out, int r, int m);
void unshuffle_seq(int a, int b, COMPLEX * in, COMPLEX * out, int r, int m);
void fft_twiddle_gen1(COMPLEX * in, COMPLEX * out, COMPLEX * W, int r, int m, int nW, int nWdnti, int nWdntm);
void fft_twiddle_gen(int i, int i1, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int r, int m);
void fft_twiddle_gen_seq(int i, int i1, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int r, int m);
void fft_base_2(COMPLEX * in, COMPLEX * out);
void fft_twiddle_2(int a, int b, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int m);
void fft_twiddle_2_seq(int a, int b, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int m);
void fft_unshuffle_2(int a, int b, COMPLEX * in, COMPLEX * out, int m);
void fft_unshuffle_2_seq(int a, int b, COMPLEX * in, COMPLEX * out, int m);
void fft_base_4(COMPLEX * in, COMPLEX * out);
void fft_twiddle_4(int a, int b, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int m);
void fft_twiddle_4_seq(int a, int b, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int m);
void fft_unshuffle_4(int a, int b, COMPLEX * in, COMPLEX * out, int m);
void fft_unshuffle_4_seq(int a, int b, COMPLEX * in, COMPLEX * out, int m);
void fft_base_8(COMPLEX * in, COMPLEX * out);
void fft_twiddle_8(int a, int b, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int m);
void fft_twiddle_8_seq(int a, int b, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int m);
void fft_unshuffle_8(int a, int b, COMPLEX * in, COMPLEX * out, int m);
void fft_unshuffle_8_seq(int a, int b, COMPLEX * in, COMPLEX * out, int m);
void fft_base_16(COMPLEX * in, COMPLEX * out);
void fft_twiddle_16(int a, int b, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int m);
void fft_twiddle_16_seq(int a, int b, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int m);
void fft_unshuffle_16(int a, int b, COMPLEX * in, COMPLEX * out, int m);
void fft_unshuffle_16_seq(int a, int b, COMPLEX * in, COMPLEX * out, int m);
void fft_base_32(COMPLEX * in, COMPLEX * out);
void fft_twiddle_32(int a, int b, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int m);
void fft_twiddle_32_seq(int a, int b, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int m);
void fft_unshuffle_32(int a, int b, COMPLEX * in, COMPLEX * out, int m);
void fft_unshuffle_32_seq(int a, int b, COMPLEX * in, COMPLEX * out, int m);
void fft_aux(int n, COMPLEX * in, COMPLEX * out, int *factors, COMPLEX * W, int nW);
void fft_aux_seq(int n, COMPLEX * in, COMPLEX * out, int *factors, COMPLEX * W, int nW);
void fft(int n, COMPLEX * in, COMPLEX * out);
void fft_seq(int n, COMPLEX * in, COMPLEX * out);
int test_correctness(int n, COMPLEX *out1, COMPLEX *out2);
void compute_w_coefficients(int n, int a, int b, COMPLEX * W)
{
     register double twoPiOverN;
     register int k;
     register REAL s, c;
     if (b - a < 128) {
   twoPiOverN = 2.0 * 3.1415926535897932384626434 / n;
   for (k = a; k <= b; ++k) {
    double temp = twoPiOverN * k;
        c = cos(temp);
        ((W[k]).re) = ((W[n - k]).re) = c;
     double temp2 = twoPiOverN * k;
        s = sin(temp2);
        ((W[k]).im) = -s;
        ((W[n - k]).im) = s;
   }
     } else {
   int ab = (a + b) / 2;
                  
#pragma omp task untied
   compute_w_coefficients(n, a, ab, W);
                  
#pragma omp task untied
   compute_w_coefficients(n, ab + 1, b, W);
                  
#pragma omp taskwait
     }
}
void compute_w_coefficients_seq(int n, int a, int b, COMPLEX * W)
{
     register double twoPiOverN;
     register int k;
     register REAL s, c;
     if (b - a < 128) {
   twoPiOverN = 2.0 * 3.1415926535897932384626434 / n;
   for (k = a; k <= b; ++k) {
        c = cos(twoPiOverN * k);
        ((W[k]).re) = ((W[n - k]).re) = c;
        s = sin(twoPiOverN * k);
        ((W[k]).im) = -s;
        ((W[n - k]).im) = s;
   }
     } else {
   int ab = (a + b) / 2;
   compute_w_coefficients_seq(n, a, ab, W);
   compute_w_coefficients_seq(n, ab + 1, b, W);
     }
}
int factor(int n)
{
     int r;
     if (n < 2) return 1;
     if (n == 64 || n == 128 || n == 256 || n == 1024 || n == 2048 || n == 4096) return 8;
     if ((n & 15) == 0) return 16;
     if ((n & 7) == 0) return 8;
     if ((n & 3) == 0) return 4;
     if ((n & 1) == 0) return 2;
     for (r = 3; r < n; r += 2) if (n % r == 0) return r;
     return n;
}
void unshuffle(int a, int b, COMPLEX * in, COMPLEX * out, int r, int m)
{
     int i, j;
     int r4 = r & (~0x3);
     const COMPLEX *ip;
     COMPLEX *jp;
     if (b - a < 16) {
   ip = in + a * r;
   for (i = a; i < b; ++i) {
        jp = out + i;
        for (j = 0; j < r4; j += 4) {
      jp[0] = ip[0];
      jp[m] = ip[1];
      jp[2 * m] = ip[2];
      jp[3 * m] = ip[3];
      jp += 4 * m;
      ip += 4;
        }
        for (; j < r; ++j) {
      *jp = *ip;
      ip++;
      jp += m;
        }
   }
     } else {
   int ab = (a + b) / 2;
                  
#pragma omp task untied
   unshuffle(a, ab, in, out, r, m);
                  
#pragma omp task untied
   unshuffle(ab, b, in, out, r, m);
                  
#pragma omp taskwait
     }
}
void unshuffle_seq(int a, int b, COMPLEX * in, COMPLEX * out, int r, int m)
{
     int i, j;
     int r4 = r & (~0x3);
     const COMPLEX *ip;
     COMPLEX *jp;
     if (b - a < 16) {
   ip = in + a * r;
   for (i = a; i < b; ++i) {
        jp = out + i;
        for (j = 0; j < r4; j += 4) {
      jp[0] = ip[0];
      jp[m] = ip[1];
      jp[2 * m] = ip[2];
      jp[3 * m] = ip[3];
      jp += 4 * m;
      ip += 4;
        }
        for (; j < r; ++j) {
      *jp = *ip;
      ip++;
      jp += m;
        }
   }
     } else {
   int ab = (a + b) / 2;
   unshuffle_seq(a, ab, in, out, r, m);
   unshuffle_seq(ab, b, in, out, r, m);
     }
}
void fft_twiddle_gen1(COMPLEX * in, COMPLEX * out,
      COMPLEX * W, int r, int m,
      int nW, int nWdnti, int nWdntm)
{
     int j, k;
     COMPLEX *jp, *kp;
     for (k = 0, kp = out; k < r; ++k, kp += m) {
   REAL r0, i0, rt, it, rw, iw;
   int l1 = nWdnti + nWdntm * k;
   int l0;
   r0 = i0 = 0.0;
   for (j = 0, jp = in, l0 = 0; j < r; ++j, jp += m) {
        rw = ((W[l0]).re);
        iw = ((W[l0]).im);
        rt = ((*jp).re);
        it = ((*jp).im);
        r0 += rt * rw - it * iw;
        i0 += rt * iw + it * rw;
        l0 += l1;
        if (l0 > nW)
      l0 -= nW;
   }
   ((*kp).re) = r0;
   ((*kp).im) = i0;
     }
}
void fft_twiddle_gen(int i, int i1, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int r, int m)
{
     if (i == i1 - 1) {
                  
#pragma omp task untied
   fft_twiddle_gen1(in + i, out + i, W,
     r, m, nW, nWdn * i, nWdn * m);
     } else {
   int i2 = (i + i1) / 2;
                  
#pragma omp task untied
   fft_twiddle_gen(i, i2, in, out, W, nW,
    nWdn, r, m);
                  
#pragma omp task untied
   fft_twiddle_gen(i2, i1, in, out, W, nW,
    nWdn, r, m);
     }
             
#pragma omp taskwait
}
void fft_twiddle_gen_seq(int i, int i1, COMPLEX * in, COMPLEX * out, COMPLEX * W,
                         int nW, int nWdn, int r, int m)
{
     if (i == i1 - 1) {
   fft_twiddle_gen1(in + i, out + i, W,
     r, m, nW, nWdn * i, nWdn * m);
     } else {
   int i2 = (i + i1) / 2;
   fft_twiddle_gen_seq(i, i2, in, out, W, nW,
    nWdn, r, m);
   fft_twiddle_gen_seq(i2, i1, in, out, W, nW,
    nWdn, r, m);
     }
}
void fft_base_2(COMPLEX * in, COMPLEX * out)
{
     REAL r1_0, i1_0;
     REAL r1_1, i1_1;
     r1_0 = ((in[0]).re);
     i1_0 = ((in[0]).im);
     r1_1 = ((in[1]).re);
     i1_1 = ((in[1]).im);
     ((out[0]).re) = (r1_0 + r1_1);
     ((out[0]).im) = (i1_0 + i1_1);
     ((out[1]).re) = (r1_0 - r1_1);
     ((out[1]).im) = (i1_0 - i1_1);
}
void fft_twiddle_2(int a, int b, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int m)
{
     int l1, i;
     COMPLEX *jp, *kp;
     REAL tmpr, tmpi, wr, wi;
     if ((b - a) < 128) {
   for (i = a, l1 = nWdn * i, kp = out + i; i < b;
        i++, l1 += nWdn, kp++) {
        jp = in + i;
        {
      REAL r1_0, i1_0;
      REAL r1_1, i1_1;
      r1_0 = ((jp[0 * m]).re);
      i1_0 = ((jp[0 * m]).im);
      wr = ((W[1 * l1]).re);
      wi = ((W[1 * l1]).im);
      tmpr = ((jp[1 * m]).re);
      tmpi = ((jp[1 * m]).im);
      r1_1 = ((wr * tmpr) - (wi * tmpi));
      i1_1 = ((wi * tmpr) + (wr * tmpi));
      ((kp[0 * m]).re) = (r1_0 + r1_1);
      ((kp[0 * m]).im) = (i1_0 + i1_1);
      ((kp[1 * m]).re) = (r1_0 - r1_1);
      ((kp[1 * m]).im) = (i1_0 - i1_1);
        }
   }
     } else {
   int ab = (a + b) / 2;
                  
#pragma omp task untied
   fft_twiddle_2(a, ab, in, out, W, nW, nWdn, m);
                  
#pragma omp task untied
   fft_twiddle_2(ab, b, in, out, W, nW, nWdn, m);
                  
#pragma omp taskwait
     }
}
void fft_twiddle_2_seq(int a, int b, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int m)
{
     int l1, i;
     COMPLEX *jp, *kp;
     REAL tmpr, tmpi, wr, wi;
     if ((b - a) < 128) {
   for (i = a, l1 = nWdn * i, kp = out + i; i < b;
        i++, l1 += nWdn, kp++) {
        jp = in + i;
        {
      REAL r1_0, i1_0;
      REAL r1_1, i1_1;
      r1_0 = ((jp[0 * m]).re);
      i1_0 = ((jp[0 * m]).im);
      wr = ((W[1 * l1]).re);
      wi = ((W[1 * l1]).im);
      tmpr = ((jp[1 * m]).re);
      tmpi = ((jp[1 * m]).im);
      r1_1 = ((wr * tmpr) - (wi * tmpi));
      i1_1 = ((wi * tmpr) + (wr * tmpi));
      ((kp[0 * m]).re) = (r1_0 + r1_1);
      ((kp[0 * m]).im) = (i1_0 + i1_1);
      ((kp[1 * m]).re) = (r1_0 - r1_1);
      ((kp[1 * m]).im) = (i1_0 - i1_1);
        }
   }
     } else {
   int ab = (a + b) / 2;
   fft_twiddle_2_seq(a, ab, in, out, W, nW, nWdn, m);
   fft_twiddle_2_seq(ab, b, in, out, W, nW, nWdn, m);
     }
}
void fft_unshuffle_2(int a, int b, COMPLEX * in, COMPLEX * out, int m)
{
     int i;
     const COMPLEX *ip;
     COMPLEX *jp;
     if ((b - a) < 128) {
   ip = in + a * 2;
   for (i = a; i < b; ++i) {
        jp = out + i;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
   }
     } else {
   int ab = (a + b) / 2;
                  
#pragma omp task untied
   fft_unshuffle_2(a, ab, in, out, m);
                  
#pragma omp task untied
   fft_unshuffle_2(ab, b, in, out, m);
                  
#pragma omp taskwait
     }
}
void fft_unshuffle_2_seq(int a, int b, COMPLEX * in, COMPLEX * out, int m)
{
     int i;
     const COMPLEX *ip;
     COMPLEX *jp;
     if ((b - a) < 128) {
   ip = in + a * 2;
   for (i = a; i < b; ++i) {
        jp = out + i;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
   }
     } else {
   int ab = (a + b) / 2;
   fft_unshuffle_2_seq(a, ab, in, out, m);
   fft_unshuffle_2_seq(ab, b, in, out, m);
     }
}
void fft_base_4(COMPLEX * in, COMPLEX * out)
{
     REAL r1_0, i1_0;
     REAL r1_1, i1_1;
     REAL r1_2, i1_2;
     REAL r1_3, i1_3;
     {
   REAL r2_0, i2_0;
   REAL r2_2, i2_2;
   r2_0 = ((in[0]).re);
   i2_0 = ((in[0]).im);
   r2_2 = ((in[2]).re);
   i2_2 = ((in[2]).im);
   r1_0 = (r2_0 + r2_2);
   i1_0 = (i2_0 + i2_2);
   r1_2 = (r2_0 - r2_2);
   i1_2 = (i2_0 - i2_2);
     }
     {
   REAL r2_1, i2_1;
   REAL r2_3, i2_3;
   r2_1 = ((in[1]).re);
   i2_1 = ((in[1]).im);
   r2_3 = ((in[3]).re);
   i2_3 = ((in[3]).im);
   r1_1 = (r2_1 + r2_3);
   i1_1 = (i2_1 + i2_3);
   r1_3 = (r2_1 - r2_3);
   i1_3 = (i2_1 - i2_3);
     }
     ((out[0]).re) = (r1_0 + r1_1);
     ((out[0]).im) = (i1_0 + i1_1);
     ((out[2]).re) = (r1_0 - r1_1);
     ((out[2]).im) = (i1_0 - i1_1);
     ((out[1]).re) = (r1_2 + i1_3);
     ((out[1]).im) = (i1_2 - r1_3);
     ((out[3]).re) = (r1_2 - i1_3);
     ((out[3]).im) = (i1_2 + r1_3);
}
void fft_twiddle_4(int a, int b, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int m)
{
     int l1, i;
     COMPLEX *jp, *kp;
     REAL tmpr, tmpi, wr, wi;
     if ((b - a) < 128) {
   for (i = a, l1 = nWdn * i, kp = out + i; i < b;
        i++, l1 += nWdn, kp++) {
        jp = in + i;
        {
      REAL r1_0, i1_0;
      REAL r1_1, i1_1;
      REAL r1_2, i1_2;
      REAL r1_3, i1_3;
      {
    REAL r2_0, i2_0;
    REAL r2_2, i2_2;
    r2_0 = ((jp[0 * m]).re);
    i2_0 = ((jp[0 * m]).im);
    wr = ((W[2 * l1]).re);
    wi = ((W[2 * l1]).im);
    tmpr = ((jp[2 * m]).re);
    tmpi = ((jp[2 * m]).im);
    r2_2 = ((wr * tmpr) - (wi * tmpi));
    i2_2 = ((wi * tmpr) + (wr * tmpi));
    r1_0 = (r2_0 + r2_2);
    i1_0 = (i2_0 + i2_2);
    r1_2 = (r2_0 - r2_2);
    i1_2 = (i2_0 - i2_2);
      }
      {
    REAL r2_1, i2_1;
    REAL r2_3, i2_3;
    wr = ((W[1 * l1]).re);
    wi = ((W[1 * l1]).im);
    tmpr = ((jp[1 * m]).re);
    tmpi = ((jp[1 * m]).im);
    r2_1 = ((wr * tmpr) - (wi * tmpi));
    i2_1 = ((wi * tmpr) + (wr * tmpi));
    wr = ((W[3 * l1]).re);
    wi = ((W[3 * l1]).im);
    tmpr = ((jp[3 * m]).re);
    tmpi = ((jp[3 * m]).im);
    r2_3 = ((wr * tmpr) - (wi * tmpi));
    i2_3 = ((wi * tmpr) + (wr * tmpi));
    r1_1 = (r2_1 + r2_3);
    i1_1 = (i2_1 + i2_3);
    r1_3 = (r2_1 - r2_3);
    i1_3 = (i2_1 - i2_3);
      }
      ((kp[0 * m]).re) = (r1_0 + r1_1);
      ((kp[0 * m]).im) = (i1_0 + i1_1);
      ((kp[2 * m]).re) = (r1_0 - r1_1);
      ((kp[2 * m]).im) = (i1_0 - i1_1);
      ((kp[1 * m]).re) = (r1_2 + i1_3);
      ((kp[1 * m]).im) = (i1_2 - r1_3);
      ((kp[3 * m]).re) = (r1_2 - i1_3);
      ((kp[3 * m]).im) = (i1_2 + r1_3);
        }
   }
     } else {
   int ab = (a + b) / 2;
                  
#pragma omp task untied
   fft_twiddle_4(a, ab, in, out, W, nW, nWdn, m);
                  
#pragma omp task untied
   fft_twiddle_4(ab, b, in, out, W, nW, nWdn, m);
                  
#pragma omp taskwait
     }
}
void fft_twiddle_4_seq(int a, int b, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int m)
{
     int l1, i;
     COMPLEX *jp, *kp;
     REAL tmpr, tmpi, wr, wi;
     if ((b - a) < 128) {
   for (i = a, l1 = nWdn * i, kp = out + i; i < b;
        i++, l1 += nWdn, kp++) {
        jp = in + i;
        {
      REAL r1_0, i1_0;
      REAL r1_1, i1_1;
      REAL r1_2, i1_2;
      REAL r1_3, i1_3;
      {
    REAL r2_0, i2_0;
    REAL r2_2, i2_2;
    r2_0 = ((jp[0 * m]).re);
    i2_0 = ((jp[0 * m]).im);
    wr = ((W[2 * l1]).re);
    wi = ((W[2 * l1]).im);
    tmpr = ((jp[2 * m]).re);
    tmpi = ((jp[2 * m]).im);
    r2_2 = ((wr * tmpr) - (wi * tmpi));
    i2_2 = ((wi * tmpr) + (wr * tmpi));
    r1_0 = (r2_0 + r2_2);
    i1_0 = (i2_0 + i2_2);
    r1_2 = (r2_0 - r2_2);
    i1_2 = (i2_0 - i2_2);
      }
      {
    REAL r2_1, i2_1;
    REAL r2_3, i2_3;
    wr = ((W[1 * l1]).re);
    wi = ((W[1 * l1]).im);
    tmpr = ((jp[1 * m]).re);
    tmpi = ((jp[1 * m]).im);
    r2_1 = ((wr * tmpr) - (wi * tmpi));
    i2_1 = ((wi * tmpr) + (wr * tmpi));
    wr = ((W[3 * l1]).re);
    wi = ((W[3 * l1]).im);
    tmpr = ((jp[3 * m]).re);
    tmpi = ((jp[3 * m]).im);
    r2_3 = ((wr * tmpr) - (wi * tmpi));
    i2_3 = ((wi * tmpr) + (wr * tmpi));
    r1_1 = (r2_1 + r2_3);
    i1_1 = (i2_1 + i2_3);
    r1_3 = (r2_1 - r2_3);
    i1_3 = (i2_1 - i2_3);
      }
      ((kp[0 * m]).re) = (r1_0 + r1_1);
      ((kp[0 * m]).im) = (i1_0 + i1_1);
      ((kp[2 * m]).re) = (r1_0 - r1_1);
      ((kp[2 * m]).im) = (i1_0 - i1_1);
      ((kp[1 * m]).re) = (r1_2 + i1_3);
      ((kp[1 * m]).im) = (i1_2 - r1_3);
      ((kp[3 * m]).re) = (r1_2 - i1_3);
      ((kp[3 * m]).im) = (i1_2 + r1_3);
        }
   }
     } else {
   int ab = (a + b) / 2;
   fft_twiddle_4_seq(a, ab, in, out, W, nW, nWdn, m);
   fft_twiddle_4_seq(ab, b, in, out, W, nW, nWdn, m);
     }
}
void fft_unshuffle_4(int a, int b, COMPLEX * in, COMPLEX * out, int m)
{
     int i;
     const COMPLEX *ip;
     COMPLEX *jp;
     if ((b - a) < 128) {
   ip = in + a * 4;
   for (i = a; i < b; ++i) {
        jp = out + i;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
   }
     } else {
   int ab = (a + b) / 2;
                  
#pragma omp task untied
   fft_unshuffle_4(a, ab, in, out, m);
                  
#pragma omp task untied
   fft_unshuffle_4(ab, b, in, out, m);
                  
#pragma omp taskwait
     }
}
void fft_unshuffle_4_seq(int a, int b, COMPLEX * in, COMPLEX * out, int m)
{
     int i;
     const COMPLEX *ip;
     COMPLEX *jp;
     if ((b - a) < 128) {
   ip = in + a * 4;
   for (i = a; i < b; ++i) {
        jp = out + i;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
   }
     } else {
   int ab = (a + b) / 2;
   fft_unshuffle_4_seq(a, ab, in, out, m);
   fft_unshuffle_4_seq(ab, b, in, out, m);
     }
}
void fft_base_8(COMPLEX * in, COMPLEX * out)
{
     REAL tmpr, tmpi;
     {
   REAL r1_0, i1_0;
   REAL r1_1, i1_1;
   REAL r1_2, i1_2;
   REAL r1_3, i1_3;
   REAL r1_4, i1_4;
   REAL r1_5, i1_5;
   REAL r1_6, i1_6;
   REAL r1_7, i1_7;
   {
        REAL r2_0, i2_0;
        REAL r2_2, i2_2;
        REAL r2_4, i2_4;
        REAL r2_6, i2_6;
        {
      REAL r3_0, i3_0;
      REAL r3_4, i3_4;
      r3_0 = ((in[0]).re);
      i3_0 = ((in[0]).im);
      r3_4 = ((in[4]).re);
      i3_4 = ((in[4]).im);
      r2_0 = (r3_0 + r3_4);
      i2_0 = (i3_0 + i3_4);
      r2_4 = (r3_0 - r3_4);
      i2_4 = (i3_0 - i3_4);
        }
        {
      REAL r3_2, i3_2;
      REAL r3_6, i3_6;
      r3_2 = ((in[2]).re);
      i3_2 = ((in[2]).im);
      r3_6 = ((in[6]).re);
      i3_6 = ((in[6]).im);
      r2_2 = (r3_2 + r3_6);
      i2_2 = (i3_2 + i3_6);
      r2_6 = (r3_2 - r3_6);
      i2_6 = (i3_2 - i3_6);
        }
        r1_0 = (r2_0 + r2_2);
        i1_0 = (i2_0 + i2_2);
        r1_4 = (r2_0 - r2_2);
        i1_4 = (i2_0 - i2_2);
        r1_2 = (r2_4 + i2_6);
        i1_2 = (i2_4 - r2_6);
        r1_6 = (r2_4 - i2_6);
        i1_6 = (i2_4 + r2_6);
   }
   {
        REAL r2_1, i2_1;
        REAL r2_3, i2_3;
        REAL r2_5, i2_5;
        REAL r2_7, i2_7;
        {
      REAL r3_1, i3_1;
      REAL r3_5, i3_5;
      r3_1 = ((in[1]).re);
      i3_1 = ((in[1]).im);
      r3_5 = ((in[5]).re);
      i3_5 = ((in[5]).im);
      r2_1 = (r3_1 + r3_5);
      i2_1 = (i3_1 + i3_5);
      r2_5 = (r3_1 - r3_5);
      i2_5 = (i3_1 - i3_5);
        }
        {
      REAL r3_3, i3_3;
      REAL r3_7, i3_7;
      r3_3 = ((in[3]).re);
      i3_3 = ((in[3]).im);
      r3_7 = ((in[7]).re);
      i3_7 = ((in[7]).im);
      r2_3 = (r3_3 + r3_7);
      i2_3 = (i3_3 + i3_7);
      r2_7 = (r3_3 - r3_7);
      i2_7 = (i3_3 - i3_7);
        }
        r1_1 = (r2_1 + r2_3);
        i1_1 = (i2_1 + i2_3);
        r1_5 = (r2_1 - r2_3);
        i1_5 = (i2_1 - i2_3);
        r1_3 = (r2_5 + i2_7);
        i1_3 = (i2_5 - r2_7);
        r1_7 = (r2_5 - i2_7);
        i1_7 = (i2_5 + r2_7);
   }
   ((out[0]).re) = (r1_0 + r1_1);
   ((out[0]).im) = (i1_0 + i1_1);
   ((out[4]).re) = (r1_0 - r1_1);
   ((out[4]).im) = (i1_0 - i1_1);
   tmpr = (0.707106781187 * (r1_3 + i1_3));
   tmpi = (0.707106781187 * (i1_3 - r1_3));
   ((out[1]).re) = (r1_2 + tmpr);
   ((out[1]).im) = (i1_2 + tmpi);
   ((out[5]).re) = (r1_2 - tmpr);
   ((out[5]).im) = (i1_2 - tmpi);
   ((out[2]).re) = (r1_4 + i1_5);
   ((out[2]).im) = (i1_4 - r1_5);
   ((out[6]).re) = (r1_4 - i1_5);
   ((out[6]).im) = (i1_4 + r1_5);
   tmpr = (0.707106781187 * (i1_7 - r1_7));
   tmpi = (0.707106781187 * (r1_7 + i1_7));
   ((out[3]).re) = (r1_6 + tmpr);
   ((out[3]).im) = (i1_6 - tmpi);
   ((out[7]).re) = (r1_6 - tmpr);
   ((out[7]).im) = (i1_6 + tmpi);
     }
}
void fft_twiddle_8(int a, int b, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int m)
{
     int l1, i;
     COMPLEX *jp, *kp;
     REAL tmpr, tmpi, wr, wi;
     if ((b - a) < 128) {
   for (i = a, l1 = nWdn * i, kp = out + i; i < b;
        i++, l1 += nWdn, kp++) {
        jp = in + i;
        {
      REAL r1_0, i1_0;
      REAL r1_1, i1_1;
      REAL r1_2, i1_2;
      REAL r1_3, i1_3;
      REAL r1_4, i1_4;
      REAL r1_5, i1_5;
      REAL r1_6, i1_6;
      REAL r1_7, i1_7;
      {
    REAL r2_0, i2_0;
    REAL r2_2, i2_2;
    REAL r2_4, i2_4;
    REAL r2_6, i2_6;
    {
         REAL r3_0, i3_0;
         REAL r3_4, i3_4;
         r3_0 = ((jp[0 * m]).re);
         i3_0 = ((jp[0 * m]).im);
         wr = ((W[4 * l1]).re);
         wi = ((W[4 * l1]).im);
         tmpr = ((jp[4 * m]).re);
         tmpi = ((jp[4 * m]).im);
         r3_4 = ((wr * tmpr) - (wi * tmpi));
         i3_4 = ((wi * tmpr) + (wr * tmpi));
         r2_0 = (r3_0 + r3_4);
         i2_0 = (i3_0 + i3_4);
         r2_4 = (r3_0 - r3_4);
         i2_4 = (i3_0 - i3_4);
    }
    {
         REAL r3_2, i3_2;
         REAL r3_6, i3_6;
         wr = ((W[2 * l1]).re);
         wi = ((W[2 * l1]).im);
         tmpr = ((jp[2 * m]).re);
         tmpi = ((jp[2 * m]).im);
         r3_2 = ((wr * tmpr) - (wi * tmpi));
         i3_2 = ((wi * tmpr) + (wr * tmpi));
         wr = ((W[6 * l1]).re);
         wi = ((W[6 * l1]).im);
         tmpr = ((jp[6 * m]).re);
         tmpi = ((jp[6 * m]).im);
         r3_6 = ((wr * tmpr) - (wi * tmpi));
         i3_6 = ((wi * tmpr) + (wr * tmpi));
         r2_2 = (r3_2 + r3_6);
         i2_2 = (i3_2 + i3_6);
         r2_6 = (r3_2 - r3_6);
         i2_6 = (i3_2 - i3_6);
    }
    r1_0 = (r2_0 + r2_2);
    i1_0 = (i2_0 + i2_2);
    r1_4 = (r2_0 - r2_2);
    i1_4 = (i2_0 - i2_2);
    r1_2 = (r2_4 + i2_6);
    i1_2 = (i2_4 - r2_6);
    r1_6 = (r2_4 - i2_6);
    i1_6 = (i2_4 + r2_6);
      }
      {
    REAL r2_1, i2_1;
    REAL r2_3, i2_3;
    REAL r2_5, i2_5;
    REAL r2_7, i2_7;
    {
         REAL r3_1, i3_1;
         REAL r3_5, i3_5;
         wr = ((W[1 * l1]).re);
         wi = ((W[1 * l1]).im);
         tmpr = ((jp[1 * m]).re);
         tmpi = ((jp[1 * m]).im);
         r3_1 = ((wr * tmpr) - (wi * tmpi));
         i3_1 = ((wi * tmpr) + (wr * tmpi));
         wr = ((W[5 * l1]).re);
         wi = ((W[5 * l1]).im);
         tmpr = ((jp[5 * m]).re);
         tmpi = ((jp[5 * m]).im);
         r3_5 = ((wr * tmpr) - (wi * tmpi));
         i3_5 = ((wi * tmpr) + (wr * tmpi));
         r2_1 = (r3_1 + r3_5);
         i2_1 = (i3_1 + i3_5);
         r2_5 = (r3_1 - r3_5);
         i2_5 = (i3_1 - i3_5);
    }
    {
         REAL r3_3, i3_3;
         REAL r3_7, i3_7;
         wr = ((W[3 * l1]).re);
         wi = ((W[3 * l1]).im);
         tmpr = ((jp[3 * m]).re);
         tmpi = ((jp[3 * m]).im);
         r3_3 = ((wr * tmpr) - (wi * tmpi));
         i3_3 = ((wi * tmpr) + (wr * tmpi));
         wr = ((W[7 * l1]).re);
         wi = ((W[7 * l1]).im);
         tmpr = ((jp[7 * m]).re);
         tmpi = ((jp[7 * m]).im);
         r3_7 = ((wr * tmpr) - (wi * tmpi));
         i3_7 = ((wi * tmpr) + (wr * tmpi));
         r2_3 = (r3_3 + r3_7);
         i2_3 = (i3_3 + i3_7);
         r2_7 = (r3_3 - r3_7);
         i2_7 = (i3_3 - i3_7);
    }
    r1_1 = (r2_1 + r2_3);
    i1_1 = (i2_1 + i2_3);
    r1_5 = (r2_1 - r2_3);
    i1_5 = (i2_1 - i2_3);
    r1_3 = (r2_5 + i2_7);
    i1_3 = (i2_5 - r2_7);
    r1_7 = (r2_5 - i2_7);
    i1_7 = (i2_5 + r2_7);
      }
      ((kp[0 * m]).re) = (r1_0 + r1_1);
      ((kp[0 * m]).im) = (i1_0 + i1_1);
      ((kp[4 * m]).re) = (r1_0 - r1_1);
      ((kp[4 * m]).im) = (i1_0 - i1_1);
      tmpr = (0.707106781187 * (r1_3 + i1_3));
      tmpi = (0.707106781187 * (i1_3 - r1_3));
      ((kp[1 * m]).re) = (r1_2 + tmpr);
      ((kp[1 * m]).im) = (i1_2 + tmpi);
      ((kp[5 * m]).re) = (r1_2 - tmpr);
      ((kp[5 * m]).im) = (i1_2 - tmpi);
      ((kp[2 * m]).re) = (r1_4 + i1_5);
      ((kp[2 * m]).im) = (i1_4 - r1_5);
      ((kp[6 * m]).re) = (r1_4 - i1_5);
      ((kp[6 * m]).im) = (i1_4 + r1_5);
      tmpr = (0.707106781187 * (i1_7 - r1_7));
      tmpi = (0.707106781187 * (r1_7 + i1_7));
      ((kp[3 * m]).re) = (r1_6 + tmpr);
      ((kp[3 * m]).im) = (i1_6 - tmpi);
      ((kp[7 * m]).re) = (r1_6 - tmpr);
      ((kp[7 * m]).im) = (i1_6 + tmpi);
        }
   }
     } else {
   int ab = (a + b) / 2;
                  
#pragma omp task untied
   fft_twiddle_8(a, ab, in, out, W, nW, nWdn, m);
                  
#pragma omp task untied
   fft_twiddle_8(ab, b, in, out, W, nW, nWdn, m);
                  
#pragma omp taskwait
     }
}
void fft_twiddle_8_seq(int a, int b, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int m)
{
     int l1, i;
     COMPLEX *jp, *kp;
     REAL tmpr, tmpi, wr, wi;
     if ((b - a) < 128) {
   for (i = a, l1 = nWdn * i, kp = out + i; i < b;
        i++, l1 += nWdn, kp++) {
        jp = in + i;
        {
      REAL r1_0, i1_0;
      REAL r1_1, i1_1;
      REAL r1_2, i1_2;
      REAL r1_3, i1_3;
      REAL r1_4, i1_4;
      REAL r1_5, i1_5;
      REAL r1_6, i1_6;
      REAL r1_7, i1_7;
      {
    REAL r2_0, i2_0;
    REAL r2_2, i2_2;
    REAL r2_4, i2_4;
    REAL r2_6, i2_6;
    {
         REAL r3_0, i3_0;
         REAL r3_4, i3_4;
         r3_0 = ((jp[0 * m]).re);
         i3_0 = ((jp[0 * m]).im);
         wr = ((W[4 * l1]).re);
         wi = ((W[4 * l1]).im);
         tmpr = ((jp[4 * m]).re);
         tmpi = ((jp[4 * m]).im);
         r3_4 = ((wr * tmpr) - (wi * tmpi));
         i3_4 = ((wi * tmpr) + (wr * tmpi));
         r2_0 = (r3_0 + r3_4);
         i2_0 = (i3_0 + i3_4);
         r2_4 = (r3_0 - r3_4);
         i2_4 = (i3_0 - i3_4);
    }
    {
         REAL r3_2, i3_2;
         REAL r3_6, i3_6;
         wr = ((W[2 * l1]).re);
         wi = ((W[2 * l1]).im);
         tmpr = ((jp[2 * m]).re);
         tmpi = ((jp[2 * m]).im);
         r3_2 = ((wr * tmpr) - (wi * tmpi));
         i3_2 = ((wi * tmpr) + (wr * tmpi));
         wr = ((W[6 * l1]).re);
         wi = ((W[6 * l1]).im);
         tmpr = ((jp[6 * m]).re);
         tmpi = ((jp[6 * m]).im);
         r3_6 = ((wr * tmpr) - (wi * tmpi));
         i3_6 = ((wi * tmpr) + (wr * tmpi));
         r2_2 = (r3_2 + r3_6);
         i2_2 = (i3_2 + i3_6);
         r2_6 = (r3_2 - r3_6);
         i2_6 = (i3_2 - i3_6);
    }
    r1_0 = (r2_0 + r2_2);
    i1_0 = (i2_0 + i2_2);
    r1_4 = (r2_0 - r2_2);
    i1_4 = (i2_0 - i2_2);
    r1_2 = (r2_4 + i2_6);
    i1_2 = (i2_4 - r2_6);
    r1_6 = (r2_4 - i2_6);
    i1_6 = (i2_4 + r2_6);
      }
      {
    REAL r2_1, i2_1;
    REAL r2_3, i2_3;
    REAL r2_5, i2_5;
    REAL r2_7, i2_7;
    {
         REAL r3_1, i3_1;
         REAL r3_5, i3_5;
         wr = ((W[1 * l1]).re);
         wi = ((W[1 * l1]).im);
         tmpr = ((jp[1 * m]).re);
         tmpi = ((jp[1 * m]).im);
         r3_1 = ((wr * tmpr) - (wi * tmpi));
         i3_1 = ((wi * tmpr) + (wr * tmpi));
         wr = ((W[5 * l1]).re);
         wi = ((W[5 * l1]).im);
         tmpr = ((jp[5 * m]).re);
         tmpi = ((jp[5 * m]).im);
         r3_5 = ((wr * tmpr) - (wi * tmpi));
         i3_5 = ((wi * tmpr) + (wr * tmpi));
         r2_1 = (r3_1 + r3_5);
         i2_1 = (i3_1 + i3_5);
         r2_5 = (r3_1 - r3_5);
         i2_5 = (i3_1 - i3_5);
    }
    {
         REAL r3_3, i3_3;
         REAL r3_7, i3_7;
         wr = ((W[3 * l1]).re);
         wi = ((W[3 * l1]).im);
         tmpr = ((jp[3 * m]).re);
         tmpi = ((jp[3 * m]).im);
         r3_3 = ((wr * tmpr) - (wi * tmpi));
         i3_3 = ((wi * tmpr) + (wr * tmpi));
         wr = ((W[7 * l1]).re);
         wi = ((W[7 * l1]).im);
         tmpr = ((jp[7 * m]).re);
         tmpi = ((jp[7 * m]).im);
         r3_7 = ((wr * tmpr) - (wi * tmpi));
         i3_7 = ((wi * tmpr) + (wr * tmpi));
         r2_3 = (r3_3 + r3_7);
         i2_3 = (i3_3 + i3_7);
         r2_7 = (r3_3 - r3_7);
         i2_7 = (i3_3 - i3_7);
    }
    r1_1 = (r2_1 + r2_3);
    i1_1 = (i2_1 + i2_3);
    r1_5 = (r2_1 - r2_3);
    i1_5 = (i2_1 - i2_3);
    r1_3 = (r2_5 + i2_7);
    i1_3 = (i2_5 - r2_7);
    r1_7 = (r2_5 - i2_7);
    i1_7 = (i2_5 + r2_7);
      }
      ((kp[0 * m]).re) = (r1_0 + r1_1);
      ((kp[0 * m]).im) = (i1_0 + i1_1);
      ((kp[4 * m]).re) = (r1_0 - r1_1);
      ((kp[4 * m]).im) = (i1_0 - i1_1);
      tmpr = (0.707106781187 * (r1_3 + i1_3));
      tmpi = (0.707106781187 * (i1_3 - r1_3));
      ((kp[1 * m]).re) = (r1_2 + tmpr);
      ((kp[1 * m]).im) = (i1_2 + tmpi);
      ((kp[5 * m]).re) = (r1_2 - tmpr);
      ((kp[5 * m]).im) = (i1_2 - tmpi);
      ((kp[2 * m]).re) = (r1_4 + i1_5);
      ((kp[2 * m]).im) = (i1_4 - r1_5);
      ((kp[6 * m]).re) = (r1_4 - i1_5);
      ((kp[6 * m]).im) = (i1_4 + r1_5);
      tmpr = (0.707106781187 * (i1_7 - r1_7));
      tmpi = (0.707106781187 * (r1_7 + i1_7));
      ((kp[3 * m]).re) = (r1_6 + tmpr);
      ((kp[3 * m]).im) = (i1_6 - tmpi);
      ((kp[7 * m]).re) = (r1_6 - tmpr);
      ((kp[7 * m]).im) = (i1_6 + tmpi);
        }
   }
     } else {
   int ab = (a + b) / 2;
   fft_twiddle_8_seq(a, ab, in, out, W, nW, nWdn, m);
   fft_twiddle_8_seq(ab, b, in, out, W, nW, nWdn, m);
     }
}
void fft_unshuffle_8(int a, int b, COMPLEX * in, COMPLEX * out, int m)
{
     int i;
     const COMPLEX *ip;
     COMPLEX *jp;
     if ((b - a) < 128) {
   ip = in + a * 8;
   for (i = a; i < b; ++i) {
        jp = out + i;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
   }
     } else {
   int ab = (a + b) / 2;
                  
#pragma omp task untied
   fft_unshuffle_8(a, ab, in, out, m);
                  
#pragma omp task untied
   fft_unshuffle_8(ab, b, in, out, m);
                  
#pragma omp taskwait
     }
}
void fft_unshuffle_8_seq(int a, int b, COMPLEX * in, COMPLEX * out, int m)
{
     int i;
     const COMPLEX *ip;
     COMPLEX *jp;
     if ((b - a) < 128) {
   ip = in + a * 8;
   for (i = a; i < b; ++i) {
        jp = out + i;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
   }
     } else {
   int ab = (a + b) / 2;
   fft_unshuffle_8_seq(a, ab, in, out, m);
   fft_unshuffle_8_seq(ab, b, in, out, m);
     }
}
void fft_base_16(COMPLEX * in, COMPLEX * out)
{
     REAL tmpr, tmpi;
     {
   REAL r1_0, i1_0;
   REAL r1_1, i1_1;
   REAL r1_2, i1_2;
   REAL r1_3, i1_3;
   REAL r1_4, i1_4;
   REAL r1_5, i1_5;
   REAL r1_6, i1_6;
   REAL r1_7, i1_7;
   REAL r1_8, i1_8;
   REAL r1_9, i1_9;
   REAL r1_10, i1_10;
   REAL r1_11, i1_11;
   REAL r1_12, i1_12;
   REAL r1_13, i1_13;
   REAL r1_14, i1_14;
   REAL r1_15, i1_15;
   {
        REAL r2_0, i2_0;
        REAL r2_2, i2_2;
        REAL r2_4, i2_4;
        REAL r2_6, i2_6;
        REAL r2_8, i2_8;
        REAL r2_10, i2_10;
        REAL r2_12, i2_12;
        REAL r2_14, i2_14;
        {
      REAL r3_0, i3_0;
      REAL r3_4, i3_4;
      REAL r3_8, i3_8;
      REAL r3_12, i3_12;
      {
    REAL r4_0, i4_0;
    REAL r4_8, i4_8;
    r4_0 = ((in[0]).re);
    i4_0 = ((in[0]).im);
    r4_8 = ((in[8]).re);
    i4_8 = ((in[8]).im);
    r3_0 = (r4_0 + r4_8);
    i3_0 = (i4_0 + i4_8);
    r3_8 = (r4_0 - r4_8);
    i3_8 = (i4_0 - i4_8);
      }
      {
    REAL r4_4, i4_4;
    REAL r4_12, i4_12;
    r4_4 = ((in[4]).re);
    i4_4 = ((in[4]).im);
    r4_12 = ((in[12]).re);
    i4_12 = ((in[12]).im);
    r3_4 = (r4_4 + r4_12);
    i3_4 = (i4_4 + i4_12);
    r3_12 = (r4_4 - r4_12);
    i3_12 = (i4_4 - i4_12);
      }
      r2_0 = (r3_0 + r3_4);
      i2_0 = (i3_0 + i3_4);
      r2_8 = (r3_0 - r3_4);
      i2_8 = (i3_0 - i3_4);
      r2_4 = (r3_8 + i3_12);
      i2_4 = (i3_8 - r3_12);
      r2_12 = (r3_8 - i3_12);
      i2_12 = (i3_8 + r3_12);
        }
        {
      REAL r3_2, i3_2;
      REAL r3_6, i3_6;
      REAL r3_10, i3_10;
      REAL r3_14, i3_14;
      {
    REAL r4_2, i4_2;
    REAL r4_10, i4_10;
    r4_2 = ((in[2]).re);
    i4_2 = ((in[2]).im);
    r4_10 = ((in[10]).re);
    i4_10 = ((in[10]).im);
    r3_2 = (r4_2 + r4_10);
    i3_2 = (i4_2 + i4_10);
    r3_10 = (r4_2 - r4_10);
    i3_10 = (i4_2 - i4_10);
      }
      {
    REAL r4_6, i4_6;
    REAL r4_14, i4_14;
    r4_6 = ((in[6]).re);
    i4_6 = ((in[6]).im);
    r4_14 = ((in[14]).re);
    i4_14 = ((in[14]).im);
    r3_6 = (r4_6 + r4_14);
    i3_6 = (i4_6 + i4_14);
    r3_14 = (r4_6 - r4_14);
    i3_14 = (i4_6 - i4_14);
      }
      r2_2 = (r3_2 + r3_6);
      i2_2 = (i3_2 + i3_6);
      r2_10 = (r3_2 - r3_6);
      i2_10 = (i3_2 - i3_6);
      r2_6 = (r3_10 + i3_14);
      i2_6 = (i3_10 - r3_14);
      r2_14 = (r3_10 - i3_14);
      i2_14 = (i3_10 + r3_14);
        }
        r1_0 = (r2_0 + r2_2);
        i1_0 = (i2_0 + i2_2);
        r1_8 = (r2_0 - r2_2);
        i1_8 = (i2_0 - i2_2);
        tmpr = (0.707106781187 * (r2_6 + i2_6));
        tmpi = (0.707106781187 * (i2_6 - r2_6));
        r1_2 = (r2_4 + tmpr);
        i1_2 = (i2_4 + tmpi);
        r1_10 = (r2_4 - tmpr);
        i1_10 = (i2_4 - tmpi);
        r1_4 = (r2_8 + i2_10);
        i1_4 = (i2_8 - r2_10);
        r1_12 = (r2_8 - i2_10);
        i1_12 = (i2_8 + r2_10);
        tmpr = (0.707106781187 * (i2_14 - r2_14));
        tmpi = (0.707106781187 * (r2_14 + i2_14));
        r1_6 = (r2_12 + tmpr);
        i1_6 = (i2_12 - tmpi);
        r1_14 = (r2_12 - tmpr);
        i1_14 = (i2_12 + tmpi);
   }
   {
        REAL r2_1, i2_1;
        REAL r2_3, i2_3;
        REAL r2_5, i2_5;
        REAL r2_7, i2_7;
        REAL r2_9, i2_9;
        REAL r2_11, i2_11;
        REAL r2_13, i2_13;
        REAL r2_15, i2_15;
        {
      REAL r3_1, i3_1;
      REAL r3_5, i3_5;
      REAL r3_9, i3_9;
      REAL r3_13, i3_13;
      {
    REAL r4_1, i4_1;
    REAL r4_9, i4_9;
    r4_1 = ((in[1]).re);
    i4_1 = ((in[1]).im);
    r4_9 = ((in[9]).re);
    i4_9 = ((in[9]).im);
    r3_1 = (r4_1 + r4_9);
    i3_1 = (i4_1 + i4_9);
    r3_9 = (r4_1 - r4_9);
    i3_9 = (i4_1 - i4_9);
      }
      {
    REAL r4_5, i4_5;
    REAL r4_13, i4_13;
    r4_5 = ((in[5]).re);
    i4_5 = ((in[5]).im);
    r4_13 = ((in[13]).re);
    i4_13 = ((in[13]).im);
    r3_5 = (r4_5 + r4_13);
    i3_5 = (i4_5 + i4_13);
    r3_13 = (r4_5 - r4_13);
    i3_13 = (i4_5 - i4_13);
      }
      r2_1 = (r3_1 + r3_5);
      i2_1 = (i3_1 + i3_5);
      r2_9 = (r3_1 - r3_5);
      i2_9 = (i3_1 - i3_5);
      r2_5 = (r3_9 + i3_13);
      i2_5 = (i3_9 - r3_13);
      r2_13 = (r3_9 - i3_13);
      i2_13 = (i3_9 + r3_13);
        }
        {
      REAL r3_3, i3_3;
      REAL r3_7, i3_7;
      REAL r3_11, i3_11;
      REAL r3_15, i3_15;
      {
    REAL r4_3, i4_3;
    REAL r4_11, i4_11;
    r4_3 = ((in[3]).re);
    i4_3 = ((in[3]).im);
    r4_11 = ((in[11]).re);
    i4_11 = ((in[11]).im);
    r3_3 = (r4_3 + r4_11);
    i3_3 = (i4_3 + i4_11);
    r3_11 = (r4_3 - r4_11);
    i3_11 = (i4_3 - i4_11);
      }
      {
    REAL r4_7, i4_7;
    REAL r4_15, i4_15;
    r4_7 = ((in[7]).re);
    i4_7 = ((in[7]).im);
    r4_15 = ((in[15]).re);
    i4_15 = ((in[15]).im);
    r3_7 = (r4_7 + r4_15);
    i3_7 = (i4_7 + i4_15);
    r3_15 = (r4_7 - r4_15);
    i3_15 = (i4_7 - i4_15);
      }
      r2_3 = (r3_3 + r3_7);
      i2_3 = (i3_3 + i3_7);
      r2_11 = (r3_3 - r3_7);
      i2_11 = (i3_3 - i3_7);
      r2_7 = (r3_11 + i3_15);
      i2_7 = (i3_11 - r3_15);
      r2_15 = (r3_11 - i3_15);
      i2_15 = (i3_11 + r3_15);
        }
        r1_1 = (r2_1 + r2_3);
        i1_1 = (i2_1 + i2_3);
        r1_9 = (r2_1 - r2_3);
        i1_9 = (i2_1 - i2_3);
        tmpr = (0.707106781187 * (r2_7 + i2_7));
        tmpi = (0.707106781187 * (i2_7 - r2_7));
        r1_3 = (r2_5 + tmpr);
        i1_3 = (i2_5 + tmpi);
        r1_11 = (r2_5 - tmpr);
        i1_11 = (i2_5 - tmpi);
        r1_5 = (r2_9 + i2_11);
        i1_5 = (i2_9 - r2_11);
        r1_13 = (r2_9 - i2_11);
        i1_13 = (i2_9 + r2_11);
        tmpr = (0.707106781187 * (i2_15 - r2_15));
        tmpi = (0.707106781187 * (r2_15 + i2_15));
        r1_7 = (r2_13 + tmpr);
        i1_7 = (i2_13 - tmpi);
        r1_15 = (r2_13 - tmpr);
        i1_15 = (i2_13 + tmpi);
   }
   ((out[0]).re) = (r1_0 + r1_1);
   ((out[0]).im) = (i1_0 + i1_1);
   ((out[8]).re) = (r1_0 - r1_1);
   ((out[8]).im) = (i1_0 - i1_1);
   tmpr = ((0.923879532511 * r1_3) + (0.382683432365 * i1_3));
   tmpi = ((0.923879532511 * i1_3) - (0.382683432365 * r1_3));
   ((out[1]).re) = (r1_2 + tmpr);
   ((out[1]).im) = (i1_2 + tmpi);
   ((out[9]).re) = (r1_2 - tmpr);
   ((out[9]).im) = (i1_2 - tmpi);
   tmpr = (0.707106781187 * (r1_5 + i1_5));
   tmpi = (0.707106781187 * (i1_5 - r1_5));
   ((out[2]).re) = (r1_4 + tmpr);
   ((out[2]).im) = (i1_4 + tmpi);
   ((out[10]).re) = (r1_4 - tmpr);
   ((out[10]).im) = (i1_4 - tmpi);
   tmpr = ((0.382683432365 * r1_7) + (0.923879532511 * i1_7));
   tmpi = ((0.382683432365 * i1_7) - (0.923879532511 * r1_7));
   ((out[3]).re) = (r1_6 + tmpr);
   ((out[3]).im) = (i1_6 + tmpi);
   ((out[11]).re) = (r1_6 - tmpr);
   ((out[11]).im) = (i1_6 - tmpi);
   ((out[4]).re) = (r1_8 + i1_9);
   ((out[4]).im) = (i1_8 - r1_9);
   ((out[12]).re) = (r1_8 - i1_9);
   ((out[12]).im) = (i1_8 + r1_9);
   tmpr = ((0.923879532511 * i1_11) - (0.382683432365 * r1_11));
   tmpi = ((0.923879532511 * r1_11) + (0.382683432365 * i1_11));
   ((out[5]).re) = (r1_10 + tmpr);
   ((out[5]).im) = (i1_10 - tmpi);
   ((out[13]).re) = (r1_10 - tmpr);
   ((out[13]).im) = (i1_10 + tmpi);
   tmpr = (0.707106781187 * (i1_13 - r1_13));
   tmpi = (0.707106781187 * (r1_13 + i1_13));
   ((out[6]).re) = (r1_12 + tmpr);
   ((out[6]).im) = (i1_12 - tmpi);
   ((out[14]).re) = (r1_12 - tmpr);
   ((out[14]).im) = (i1_12 + tmpi);
   tmpr = ((0.382683432365 * i1_15) - (0.923879532511 * r1_15));
   tmpi = ((0.382683432365 * r1_15) + (0.923879532511 * i1_15));
   ((out[7]).re) = (r1_14 + tmpr);
   ((out[7]).im) = (i1_14 - tmpi);
   ((out[15]).re) = (r1_14 - tmpr);
   ((out[15]).im) = (i1_14 + tmpi);
     }
}
void fft_twiddle_16(int a, int b, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int m)
{
     int l1, i;
     COMPLEX *jp, *kp;
     REAL tmpr, tmpi, wr, wi;
     if ((b - a) < 128) {
   for (i = a, l1 = nWdn * i, kp = out + i; i < b;
        i++, l1 += nWdn, kp++) {
        jp = in + i;
        {
      REAL r1_0, i1_0;
      REAL r1_1, i1_1;
      REAL r1_2, i1_2;
      REAL r1_3, i1_3;
      REAL r1_4, i1_4;
      REAL r1_5, i1_5;
      REAL r1_6, i1_6;
      REAL r1_7, i1_7;
      REAL r1_8, i1_8;
      REAL r1_9, i1_9;
      REAL r1_10, i1_10;
      REAL r1_11, i1_11;
      REAL r1_12, i1_12;
      REAL r1_13, i1_13;
      REAL r1_14, i1_14;
      REAL r1_15, i1_15;
      {
    REAL r2_0, i2_0;
    REAL r2_2, i2_2;
    REAL r2_4, i2_4;
    REAL r2_6, i2_6;
    REAL r2_8, i2_8;
    REAL r2_10, i2_10;
    REAL r2_12, i2_12;
    REAL r2_14, i2_14;
    {
         REAL r3_0, i3_0;
         REAL r3_4, i3_4;
         REAL r3_8, i3_8;
         REAL r3_12, i3_12;
         {
       REAL r4_0, i4_0;
       REAL r4_8, i4_8;
       r4_0 = ((jp[0 * m]).re);
       i4_0 = ((jp[0 * m]).im);
       wr = ((W[8 * l1]).re);
       wi = ((W[8 * l1]).im);
       tmpr = ((jp[8 * m]).re);
       tmpi = ((jp[8 * m]).im);
       r4_8 = ((wr * tmpr) - (wi * tmpi));
       i4_8 = ((wi * tmpr) + (wr * tmpi));
       r3_0 = (r4_0 + r4_8);
       i3_0 = (i4_0 + i4_8);
       r3_8 = (r4_0 - r4_8);
       i3_8 = (i4_0 - i4_8);
         }
         {
       REAL r4_4, i4_4;
       REAL r4_12, i4_12;
       wr = ((W[4 * l1]).re);
       wi = ((W[4 * l1]).im);
       tmpr = ((jp[4 * m]).re);
       tmpi = ((jp[4 * m]).im);
       r4_4 = ((wr * tmpr) - (wi * tmpi));
       i4_4 = ((wi * tmpr) + (wr * tmpi));
       wr = ((W[12 * l1]).re);
       wi = ((W[12 * l1]).im);
       tmpr = ((jp[12 * m]).re);
       tmpi = ((jp[12 * m]).im);
       r4_12 = ((wr * tmpr) - (wi * tmpi));
       i4_12 = ((wi * tmpr) + (wr * tmpi));
       r3_4 = (r4_4 + r4_12);
       i3_4 = (i4_4 + i4_12);
       r3_12 = (r4_4 - r4_12);
       i3_12 = (i4_4 - i4_12);
         }
         r2_0 = (r3_0 + r3_4);
         i2_0 = (i3_0 + i3_4);
         r2_8 = (r3_0 - r3_4);
         i2_8 = (i3_0 - i3_4);
         r2_4 = (r3_8 + i3_12);
         i2_4 = (i3_8 - r3_12);
         r2_12 = (r3_8 - i3_12);
         i2_12 = (i3_8 + r3_12);
    }
    {
         REAL r3_2, i3_2;
         REAL r3_6, i3_6;
         REAL r3_10, i3_10;
         REAL r3_14, i3_14;
         {
       REAL r4_2, i4_2;
       REAL r4_10, i4_10;
       wr = ((W[2 * l1]).re);
       wi = ((W[2 * l1]).im);
       tmpr = ((jp[2 * m]).re);
       tmpi = ((jp[2 * m]).im);
       r4_2 = ((wr * tmpr) - (wi * tmpi));
       i4_2 = ((wi * tmpr) + (wr * tmpi));
       wr = ((W[10 * l1]).re);
       wi = ((W[10 * l1]).im);
       tmpr = ((jp[10 * m]).re);
       tmpi = ((jp[10 * m]).im);
       r4_10 = ((wr * tmpr) - (wi * tmpi));
       i4_10 = ((wi * tmpr) + (wr * tmpi));
       r3_2 = (r4_2 + r4_10);
       i3_2 = (i4_2 + i4_10);
       r3_10 = (r4_2 - r4_10);
       i3_10 = (i4_2 - i4_10);
         }
         {
       REAL r4_6, i4_6;
       REAL r4_14, i4_14;
       wr = ((W[6 * l1]).re);
       wi = ((W[6 * l1]).im);
       tmpr = ((jp[6 * m]).re);
       tmpi = ((jp[6 * m]).im);
       r4_6 = ((wr * tmpr) - (wi * tmpi));
       i4_6 = ((wi * tmpr) + (wr * tmpi));
       wr = ((W[14 * l1]).re);
       wi = ((W[14 * l1]).im);
       tmpr = ((jp[14 * m]).re);
       tmpi = ((jp[14 * m]).im);
       r4_14 = ((wr * tmpr) - (wi * tmpi));
       i4_14 = ((wi * tmpr) + (wr * tmpi));
       r3_6 = (r4_6 + r4_14);
       i3_6 = (i4_6 + i4_14);
       r3_14 = (r4_6 - r4_14);
       i3_14 = (i4_6 - i4_14);
         }
         r2_2 = (r3_2 + r3_6);
         i2_2 = (i3_2 + i3_6);
         r2_10 = (r3_2 - r3_6);
         i2_10 = (i3_2 - i3_6);
         r2_6 = (r3_10 + i3_14);
         i2_6 = (i3_10 - r3_14);
         r2_14 = (r3_10 - i3_14);
         i2_14 = (i3_10 + r3_14);
    }
    r1_0 = (r2_0 + r2_2);
    i1_0 = (i2_0 + i2_2);
    r1_8 = (r2_0 - r2_2);
    i1_8 = (i2_0 - i2_2);
    tmpr = (0.707106781187 * (r2_6 + i2_6));
    tmpi = (0.707106781187 * (i2_6 - r2_6));
    r1_2 = (r2_4 + tmpr);
    i1_2 = (i2_4 + tmpi);
    r1_10 = (r2_4 - tmpr);
    i1_10 = (i2_4 - tmpi);
    r1_4 = (r2_8 + i2_10);
    i1_4 = (i2_8 - r2_10);
    r1_12 = (r2_8 - i2_10);
    i1_12 = (i2_8 + r2_10);
    tmpr = (0.707106781187 * (i2_14 - r2_14));
    tmpi = (0.707106781187 * (r2_14 + i2_14));
    r1_6 = (r2_12 + tmpr);
    i1_6 = (i2_12 - tmpi);
    r1_14 = (r2_12 - tmpr);
    i1_14 = (i2_12 + tmpi);
      }
      {
    REAL r2_1, i2_1;
    REAL r2_3, i2_3;
    REAL r2_5, i2_5;
    REAL r2_7, i2_7;
    REAL r2_9, i2_9;
    REAL r2_11, i2_11;
    REAL r2_13, i2_13;
    REAL r2_15, i2_15;
    {
         REAL r3_1, i3_1;
         REAL r3_5, i3_5;
         REAL r3_9, i3_9;
         REAL r3_13, i3_13;
         {
       REAL r4_1, i4_1;
       REAL r4_9, i4_9;
       wr = ((W[1 * l1]).re);
       wi = ((W[1 * l1]).im);
       tmpr = ((jp[1 * m]).re);
       tmpi = ((jp[1 * m]).im);
       r4_1 = ((wr * tmpr) - (wi * tmpi));
       i4_1 = ((wi * tmpr) + (wr * tmpi));
       wr = ((W[9 * l1]).re);
       wi = ((W[9 * l1]).im);
       tmpr = ((jp[9 * m]).re);
       tmpi = ((jp[9 * m]).im);
       r4_9 = ((wr * tmpr) - (wi * tmpi));
       i4_9 = ((wi * tmpr) + (wr * tmpi));
       r3_1 = (r4_1 + r4_9);
       i3_1 = (i4_1 + i4_9);
       r3_9 = (r4_1 - r4_9);
       i3_9 = (i4_1 - i4_9);
         }
         {
       REAL r4_5, i4_5;
       REAL r4_13, i4_13;
       wr = ((W[5 * l1]).re);
       wi = ((W[5 * l1]).im);
       tmpr = ((jp[5 * m]).re);
       tmpi = ((jp[5 * m]).im);
       r4_5 = ((wr * tmpr) - (wi * tmpi));
       i4_5 = ((wi * tmpr) + (wr * tmpi));
       wr = ((W[13 * l1]).re);
       wi = ((W[13 * l1]).im);
       tmpr = ((jp[13 * m]).re);
       tmpi = ((jp[13 * m]).im);
       r4_13 = ((wr * tmpr) - (wi * tmpi));
       i4_13 = ((wi * tmpr) + (wr * tmpi));
       r3_5 = (r4_5 + r4_13);
       i3_5 = (i4_5 + i4_13);
       r3_13 = (r4_5 - r4_13);
       i3_13 = (i4_5 - i4_13);
         }
         r2_1 = (r3_1 + r3_5);
         i2_1 = (i3_1 + i3_5);
         r2_9 = (r3_1 - r3_5);
         i2_9 = (i3_1 - i3_5);
         r2_5 = (r3_9 + i3_13);
         i2_5 = (i3_9 - r3_13);
         r2_13 = (r3_9 - i3_13);
         i2_13 = (i3_9 + r3_13);
    }
    {
         REAL r3_3, i3_3;
         REAL r3_7, i3_7;
         REAL r3_11, i3_11;
         REAL r3_15, i3_15;
         {
       REAL r4_3, i4_3;
       REAL r4_11, i4_11;
       wr = ((W[3 * l1]).re);
       wi = ((W[3 * l1]).im);
       tmpr = ((jp[3 * m]).re);
       tmpi = ((jp[3 * m]).im);
       r4_3 = ((wr * tmpr) - (wi * tmpi));
       i4_3 = ((wi * tmpr) + (wr * tmpi));
       wr = ((W[11 * l1]).re);
       wi = ((W[11 * l1]).im);
       tmpr = ((jp[11 * m]).re);
       tmpi = ((jp[11 * m]).im);
       r4_11 = ((wr * tmpr) - (wi * tmpi));
       i4_11 = ((wi * tmpr) + (wr * tmpi));
       r3_3 = (r4_3 + r4_11);
       i3_3 = (i4_3 + i4_11);
       r3_11 = (r4_3 - r4_11);
       i3_11 = (i4_3 - i4_11);
         }
         {
       REAL r4_7, i4_7;
       REAL r4_15, i4_15;
       wr = ((W[7 * l1]).re);
       wi = ((W[7 * l1]).im);
       tmpr = ((jp[7 * m]).re);
       tmpi = ((jp[7 * m]).im);
       r4_7 = ((wr * tmpr) - (wi * tmpi));
       i4_7 = ((wi * tmpr) + (wr * tmpi));
       wr = ((W[15 * l1]).re);
       wi = ((W[15 * l1]).im);
       tmpr = ((jp[15 * m]).re);
       tmpi = ((jp[15 * m]).im);
       r4_15 = ((wr * tmpr) - (wi * tmpi));
       i4_15 = ((wi * tmpr) + (wr * tmpi));
       r3_7 = (r4_7 + r4_15);
       i3_7 = (i4_7 + i4_15);
       r3_15 = (r4_7 - r4_15);
       i3_15 = (i4_7 - i4_15);
         }
         r2_3 = (r3_3 + r3_7);
         i2_3 = (i3_3 + i3_7);
         r2_11 = (r3_3 - r3_7);
         i2_11 = (i3_3 - i3_7);
         r2_7 = (r3_11 + i3_15);
         i2_7 = (i3_11 - r3_15);
         r2_15 = (r3_11 - i3_15);
         i2_15 = (i3_11 + r3_15);
    }
    r1_1 = (r2_1 + r2_3);
    i1_1 = (i2_1 + i2_3);
    r1_9 = (r2_1 - r2_3);
    i1_9 = (i2_1 - i2_3);
    tmpr = (0.707106781187 * (r2_7 + i2_7));
    tmpi = (0.707106781187 * (i2_7 - r2_7));
    r1_3 = (r2_5 + tmpr);
    i1_3 = (i2_5 + tmpi);
    r1_11 = (r2_5 - tmpr);
    i1_11 = (i2_5 - tmpi);
    r1_5 = (r2_9 + i2_11);
    i1_5 = (i2_9 - r2_11);
    r1_13 = (r2_9 - i2_11);
    i1_13 = (i2_9 + r2_11);
    tmpr = (0.707106781187 * (i2_15 - r2_15));
    tmpi = (0.707106781187 * (r2_15 + i2_15));
    r1_7 = (r2_13 + tmpr);
    i1_7 = (i2_13 - tmpi);
    r1_15 = (r2_13 - tmpr);
    i1_15 = (i2_13 + tmpi);
      }
      ((kp[0 * m]).re) = (r1_0 + r1_1);
      ((kp[0 * m]).im) = (i1_0 + i1_1);
      ((kp[8 * m]).re) = (r1_0 - r1_1);
      ((kp[8 * m]).im) = (i1_0 - i1_1);
      tmpr = ((0.923879532511 * r1_3) + (0.382683432365 * i1_3));
      tmpi = ((0.923879532511 * i1_3) - (0.382683432365 * r1_3));
      ((kp[1 * m]).re) = (r1_2 + tmpr);
      ((kp[1 * m]).im) = (i1_2 + tmpi);
      ((kp[9 * m]).re) = (r1_2 - tmpr);
      ((kp[9 * m]).im) = (i1_2 - tmpi);
      tmpr = (0.707106781187 * (r1_5 + i1_5));
      tmpi = (0.707106781187 * (i1_5 - r1_5));
      ((kp[2 * m]).re) = (r1_4 + tmpr);
      ((kp[2 * m]).im) = (i1_4 + tmpi);
      ((kp[10 * m]).re) = (r1_4 - tmpr);
      ((kp[10 * m]).im) = (i1_4 - tmpi);
      tmpr = ((0.382683432365 * r1_7) + (0.923879532511 * i1_7));
      tmpi = ((0.382683432365 * i1_7) - (0.923879532511 * r1_7));
      ((kp[3 * m]).re) = (r1_6 + tmpr);
      ((kp[3 * m]).im) = (i1_6 + tmpi);
      ((kp[11 * m]).re) = (r1_6 - tmpr);
      ((kp[11 * m]).im) = (i1_6 - tmpi);
      ((kp[4 * m]).re) = (r1_8 + i1_9);
      ((kp[4 * m]).im) = (i1_8 - r1_9);
      ((kp[12 * m]).re) = (r1_8 - i1_9);
      ((kp[12 * m]).im) = (i1_8 + r1_9);
      tmpr = ((0.923879532511 * i1_11) - (0.382683432365 * r1_11));
      tmpi = ((0.923879532511 * r1_11) + (0.382683432365 * i1_11));
      ((kp[5 * m]).re) = (r1_10 + tmpr);
      ((kp[5 * m]).im) = (i1_10 - tmpi);
      ((kp[13 * m]).re) = (r1_10 - tmpr);
      ((kp[13 * m]).im) = (i1_10 + tmpi);
      tmpr = (0.707106781187 * (i1_13 - r1_13));
      tmpi = (0.707106781187 * (r1_13 + i1_13));
      ((kp[6 * m]).re) = (r1_12 + tmpr);
      ((kp[6 * m]).im) = (i1_12 - tmpi);
      ((kp[14 * m]).re) = (r1_12 - tmpr);
      ((kp[14 * m]).im) = (i1_12 + tmpi);
      tmpr = ((0.382683432365 * i1_15) - (0.923879532511 * r1_15));
      tmpi = ((0.382683432365 * r1_15) + (0.923879532511 * i1_15));
      ((kp[7 * m]).re) = (r1_14 + tmpr);
      ((kp[7 * m]).im) = (i1_14 - tmpi);
      ((kp[15 * m]).re) = (r1_14 - tmpr);
      ((kp[15 * m]).im) = (i1_14 + tmpi);
        }
   }
     } else {
   int ab = (a + b) / 2;
                  
#pragma omp task untied
   fft_twiddle_16(a, ab, in, out, W, nW, nWdn, m);
                  
#pragma omp task untied
   fft_twiddle_16(ab, b, in, out, W, nW, nWdn, m);
                  
#pragma omp taskwait
     }
}
void fft_twiddle_16_seq(int a, int b, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int m)
{
     int l1, i;
     COMPLEX *jp, *kp;
     REAL tmpr, tmpi, wr, wi;
     if ((b - a) < 128) {
   for (i = a, l1 = nWdn * i, kp = out + i; i < b;
        i++, l1 += nWdn, kp++) {
        jp = in + i;
        {
      REAL r1_0, i1_0;
      REAL r1_1, i1_1;
      REAL r1_2, i1_2;
      REAL r1_3, i1_3;
      REAL r1_4, i1_4;
      REAL r1_5, i1_5;
      REAL r1_6, i1_6;
      REAL r1_7, i1_7;
      REAL r1_8, i1_8;
      REAL r1_9, i1_9;
      REAL r1_10, i1_10;
      REAL r1_11, i1_11;
      REAL r1_12, i1_12;
      REAL r1_13, i1_13;
      REAL r1_14, i1_14;
      REAL r1_15, i1_15;
      {
    REAL r2_0, i2_0;
    REAL r2_2, i2_2;
    REAL r2_4, i2_4;
    REAL r2_6, i2_6;
    REAL r2_8, i2_8;
    REAL r2_10, i2_10;
    REAL r2_12, i2_12;
    REAL r2_14, i2_14;
    {
         REAL r3_0, i3_0;
         REAL r3_4, i3_4;
         REAL r3_8, i3_8;
         REAL r3_12, i3_12;
         {
       REAL r4_0, i4_0;
       REAL r4_8, i4_8;
       r4_0 = ((jp[0 * m]).re);
       i4_0 = ((jp[0 * m]).im);
       wr = ((W[8 * l1]).re);
       wi = ((W[8 * l1]).im);
       tmpr = ((jp[8 * m]).re);
       tmpi = ((jp[8 * m]).im);
       r4_8 = ((wr * tmpr) - (wi * tmpi));
       i4_8 = ((wi * tmpr) + (wr * tmpi));
       r3_0 = (r4_0 + r4_8);
       i3_0 = (i4_0 + i4_8);
       r3_8 = (r4_0 - r4_8);
       i3_8 = (i4_0 - i4_8);
         }
         {
       REAL r4_4, i4_4;
       REAL r4_12, i4_12;
       wr = ((W[4 * l1]).re);
       wi = ((W[4 * l1]).im);
       tmpr = ((jp[4 * m]).re);
       tmpi = ((jp[4 * m]).im);
       r4_4 = ((wr * tmpr) - (wi * tmpi));
       i4_4 = ((wi * tmpr) + (wr * tmpi));
       wr = ((W[12 * l1]).re);
       wi = ((W[12 * l1]).im);
       tmpr = ((jp[12 * m]).re);
       tmpi = ((jp[12 * m]).im);
       r4_12 = ((wr * tmpr) - (wi * tmpi));
       i4_12 = ((wi * tmpr) + (wr * tmpi));
       r3_4 = (r4_4 + r4_12);
       i3_4 = (i4_4 + i4_12);
       r3_12 = (r4_4 - r4_12);
       i3_12 = (i4_4 - i4_12);
         }
         r2_0 = (r3_0 + r3_4);
         i2_0 = (i3_0 + i3_4);
         r2_8 = (r3_0 - r3_4);
         i2_8 = (i3_0 - i3_4);
         r2_4 = (r3_8 + i3_12);
         i2_4 = (i3_8 - r3_12);
         r2_12 = (r3_8 - i3_12);
         i2_12 = (i3_8 + r3_12);
    }
    {
         REAL r3_2, i3_2;
         REAL r3_6, i3_6;
         REAL r3_10, i3_10;
         REAL r3_14, i3_14;
         {
       REAL r4_2, i4_2;
       REAL r4_10, i4_10;
       wr = ((W[2 * l1]).re);
       wi = ((W[2 * l1]).im);
       tmpr = ((jp[2 * m]).re);
       tmpi = ((jp[2 * m]).im);
       r4_2 = ((wr * tmpr) - (wi * tmpi));
       i4_2 = ((wi * tmpr) + (wr * tmpi));
       wr = ((W[10 * l1]).re);
       wi = ((W[10 * l1]).im);
       tmpr = ((jp[10 * m]).re);
       tmpi = ((jp[10 * m]).im);
       r4_10 = ((wr * tmpr) - (wi * tmpi));
       i4_10 = ((wi * tmpr) + (wr * tmpi));
       r3_2 = (r4_2 + r4_10);
       i3_2 = (i4_2 + i4_10);
       r3_10 = (r4_2 - r4_10);
       i3_10 = (i4_2 - i4_10);
         }
         {
       REAL r4_6, i4_6;
       REAL r4_14, i4_14;
       wr = ((W[6 * l1]).re);
       wi = ((W[6 * l1]).im);
       tmpr = ((jp[6 * m]).re);
       tmpi = ((jp[6 * m]).im);
       r4_6 = ((wr * tmpr) - (wi * tmpi));
       i4_6 = ((wi * tmpr) + (wr * tmpi));
       wr = ((W[14 * l1]).re);
       wi = ((W[14 * l1]).im);
       tmpr = ((jp[14 * m]).re);
       tmpi = ((jp[14 * m]).im);
       r4_14 = ((wr * tmpr) - (wi * tmpi));
       i4_14 = ((wi * tmpr) + (wr * tmpi));
       r3_6 = (r4_6 + r4_14);
       i3_6 = (i4_6 + i4_14);
       r3_14 = (r4_6 - r4_14);
       i3_14 = (i4_6 - i4_14);
         }
         r2_2 = (r3_2 + r3_6);
         i2_2 = (i3_2 + i3_6);
         r2_10 = (r3_2 - r3_6);
         i2_10 = (i3_2 - i3_6);
         r2_6 = (r3_10 + i3_14);
         i2_6 = (i3_10 - r3_14);
         r2_14 = (r3_10 - i3_14);
         i2_14 = (i3_10 + r3_14);
    }
    r1_0 = (r2_0 + r2_2);
    i1_0 = (i2_0 + i2_2);
    r1_8 = (r2_0 - r2_2);
    i1_8 = (i2_0 - i2_2);
    tmpr = (0.707106781187 * (r2_6 + i2_6));
    tmpi = (0.707106781187 * (i2_6 - r2_6));
    r1_2 = (r2_4 + tmpr);
    i1_2 = (i2_4 + tmpi);
    r1_10 = (r2_4 - tmpr);
    i1_10 = (i2_4 - tmpi);
    r1_4 = (r2_8 + i2_10);
    i1_4 = (i2_8 - r2_10);
    r1_12 = (r2_8 - i2_10);
    i1_12 = (i2_8 + r2_10);
    tmpr = (0.707106781187 * (i2_14 - r2_14));
    tmpi = (0.707106781187 * (r2_14 + i2_14));
    r1_6 = (r2_12 + tmpr);
    i1_6 = (i2_12 - tmpi);
    r1_14 = (r2_12 - tmpr);
    i1_14 = (i2_12 + tmpi);
      }
      {
    REAL r2_1, i2_1;
    REAL r2_3, i2_3;
    REAL r2_5, i2_5;
    REAL r2_7, i2_7;
    REAL r2_9, i2_9;
    REAL r2_11, i2_11;
    REAL r2_13, i2_13;
    REAL r2_15, i2_15;
    {
         REAL r3_1, i3_1;
         REAL r3_5, i3_5;
         REAL r3_9, i3_9;
         REAL r3_13, i3_13;
         {
       REAL r4_1, i4_1;
       REAL r4_9, i4_9;
       wr = ((W[1 * l1]).re);
       wi = ((W[1 * l1]).im);
       tmpr = ((jp[1 * m]).re);
       tmpi = ((jp[1 * m]).im);
       r4_1 = ((wr * tmpr) - (wi * tmpi));
       i4_1 = ((wi * tmpr) + (wr * tmpi));
       wr = ((W[9 * l1]).re);
       wi = ((W[9 * l1]).im);
       tmpr = ((jp[9 * m]).re);
       tmpi = ((jp[9 * m]).im);
       r4_9 = ((wr * tmpr) - (wi * tmpi));
       i4_9 = ((wi * tmpr) + (wr * tmpi));
       r3_1 = (r4_1 + r4_9);
       i3_1 = (i4_1 + i4_9);
       r3_9 = (r4_1 - r4_9);
       i3_9 = (i4_1 - i4_9);
         }
         {
       REAL r4_5, i4_5;
       REAL r4_13, i4_13;
       wr = ((W[5 * l1]).re);
       wi = ((W[5 * l1]).im);
       tmpr = ((jp[5 * m]).re);
       tmpi = ((jp[5 * m]).im);
       r4_5 = ((wr * tmpr) - (wi * tmpi));
       i4_5 = ((wi * tmpr) + (wr * tmpi));
       wr = ((W[13 * l1]).re);
       wi = ((W[13 * l1]).im);
       tmpr = ((jp[13 * m]).re);
       tmpi = ((jp[13 * m]).im);
       r4_13 = ((wr * tmpr) - (wi * tmpi));
       i4_13 = ((wi * tmpr) + (wr * tmpi));
       r3_5 = (r4_5 + r4_13);
       i3_5 = (i4_5 + i4_13);
       r3_13 = (r4_5 - r4_13);
       i3_13 = (i4_5 - i4_13);
         }
         r2_1 = (r3_1 + r3_5);
         i2_1 = (i3_1 + i3_5);
         r2_9 = (r3_1 - r3_5);
         i2_9 = (i3_1 - i3_5);
         r2_5 = (r3_9 + i3_13);
         i2_5 = (i3_9 - r3_13);
         r2_13 = (r3_9 - i3_13);
         i2_13 = (i3_9 + r3_13);
    }
    {
         REAL r3_3, i3_3;
         REAL r3_7, i3_7;
         REAL r3_11, i3_11;
         REAL r3_15, i3_15;
         {
       REAL r4_3, i4_3;
       REAL r4_11, i4_11;
       wr = ((W[3 * l1]).re);
       wi = ((W[3 * l1]).im);
       tmpr = ((jp[3 * m]).re);
       tmpi = ((jp[3 * m]).im);
       r4_3 = ((wr * tmpr) - (wi * tmpi));
       i4_3 = ((wi * tmpr) + (wr * tmpi));
       wr = ((W[11 * l1]).re);
       wi = ((W[11 * l1]).im);
       tmpr = ((jp[11 * m]).re);
       tmpi = ((jp[11 * m]).im);
       r4_11 = ((wr * tmpr) - (wi * tmpi));
       i4_11 = ((wi * tmpr) + (wr * tmpi));
       r3_3 = (r4_3 + r4_11);
       i3_3 = (i4_3 + i4_11);
       r3_11 = (r4_3 - r4_11);
       i3_11 = (i4_3 - i4_11);
         }
         {
       REAL r4_7, i4_7;
       REAL r4_15, i4_15;
       wr = ((W[7 * l1]).re);
       wi = ((W[7 * l1]).im);
       tmpr = ((jp[7 * m]).re);
       tmpi = ((jp[7 * m]).im);
       r4_7 = ((wr * tmpr) - (wi * tmpi));
       i4_7 = ((wi * tmpr) + (wr * tmpi));
       wr = ((W[15 * l1]).re);
       wi = ((W[15 * l1]).im);
       tmpr = ((jp[15 * m]).re);
       tmpi = ((jp[15 * m]).im);
       r4_15 = ((wr * tmpr) - (wi * tmpi));
       i4_15 = ((wi * tmpr) + (wr * tmpi));
       r3_7 = (r4_7 + r4_15);
       i3_7 = (i4_7 + i4_15);
       r3_15 = (r4_7 - r4_15);
       i3_15 = (i4_7 - i4_15);
         }
         r2_3 = (r3_3 + r3_7);
         i2_3 = (i3_3 + i3_7);
         r2_11 = (r3_3 - r3_7);
         i2_11 = (i3_3 - i3_7);
         r2_7 = (r3_11 + i3_15);
         i2_7 = (i3_11 - r3_15);
         r2_15 = (r3_11 - i3_15);
         i2_15 = (i3_11 + r3_15);
    }
    r1_1 = (r2_1 + r2_3);
    i1_1 = (i2_1 + i2_3);
    r1_9 = (r2_1 - r2_3);
    i1_9 = (i2_1 - i2_3);
    tmpr = (0.707106781187 * (r2_7 + i2_7));
    tmpi = (0.707106781187 * (i2_7 - r2_7));
    r1_3 = (r2_5 + tmpr);
    i1_3 = (i2_5 + tmpi);
    r1_11 = (r2_5 - tmpr);
    i1_11 = (i2_5 - tmpi);
    r1_5 = (r2_9 + i2_11);
    i1_5 = (i2_9 - r2_11);
    r1_13 = (r2_9 - i2_11);
    i1_13 = (i2_9 + r2_11);
    tmpr = (0.707106781187 * (i2_15 - r2_15));
    tmpi = (0.707106781187 * (r2_15 + i2_15));
    r1_7 = (r2_13 + tmpr);
    i1_7 = (i2_13 - tmpi);
    r1_15 = (r2_13 - tmpr);
    i1_15 = (i2_13 + tmpi);
      }
      ((kp[0 * m]).re) = (r1_0 + r1_1);
      ((kp[0 * m]).im) = (i1_0 + i1_1);
      ((kp[8 * m]).re) = (r1_0 - r1_1);
      ((kp[8 * m]).im) = (i1_0 - i1_1);
      tmpr = ((0.923879532511 * r1_3) + (0.382683432365 * i1_3));
      tmpi = ((0.923879532511 * i1_3) - (0.382683432365 * r1_3));
      ((kp[1 * m]).re) = (r1_2 + tmpr);
      ((kp[1 * m]).im) = (i1_2 + tmpi);
      ((kp[9 * m]).re) = (r1_2 - tmpr);
      ((kp[9 * m]).im) = (i1_2 - tmpi);
      tmpr = (0.707106781187 * (r1_5 + i1_5));
      tmpi = (0.707106781187 * (i1_5 - r1_5));
      ((kp[2 * m]).re) = (r1_4 + tmpr);
      ((kp[2 * m]).im) = (i1_4 + tmpi);
      ((kp[10 * m]).re) = (r1_4 - tmpr);
      ((kp[10 * m]).im) = (i1_4 - tmpi);
      tmpr = ((0.382683432365 * r1_7) + (0.923879532511 * i1_7));
      tmpi = ((0.382683432365 * i1_7) - (0.923879532511 * r1_7));
      ((kp[3 * m]).re) = (r1_6 + tmpr);
      ((kp[3 * m]).im) = (i1_6 + tmpi);
      ((kp[11 * m]).re) = (r1_6 - tmpr);
      ((kp[11 * m]).im) = (i1_6 - tmpi);
      ((kp[4 * m]).re) = (r1_8 + i1_9);
      ((kp[4 * m]).im) = (i1_8 - r1_9);
      ((kp[12 * m]).re) = (r1_8 - i1_9);
      ((kp[12 * m]).im) = (i1_8 + r1_9);
      tmpr = ((0.923879532511 * i1_11) - (0.382683432365 * r1_11));
      tmpi = ((0.923879532511 * r1_11) + (0.382683432365 * i1_11));
      ((kp[5 * m]).re) = (r1_10 + tmpr);
      ((kp[5 * m]).im) = (i1_10 - tmpi);
      ((kp[13 * m]).re) = (r1_10 - tmpr);
      ((kp[13 * m]).im) = (i1_10 + tmpi);
      tmpr = (0.707106781187 * (i1_13 - r1_13));
      tmpi = (0.707106781187 * (r1_13 + i1_13));
      ((kp[6 * m]).re) = (r1_12 + tmpr);
      ((kp[6 * m]).im) = (i1_12 - tmpi);
      ((kp[14 * m]).re) = (r1_12 - tmpr);
      ((kp[14 * m]).im) = (i1_12 + tmpi);
      tmpr = ((0.382683432365 * i1_15) - (0.923879532511 * r1_15));
      tmpi = ((0.382683432365 * r1_15) + (0.923879532511 * i1_15));
      ((kp[7 * m]).re) = (r1_14 + tmpr);
      ((kp[7 * m]).im) = (i1_14 - tmpi);
      ((kp[15 * m]).re) = (r1_14 - tmpr);
      ((kp[15 * m]).im) = (i1_14 + tmpi);
        }
   }
     } else {
   int ab = (a + b) / 2;
   fft_twiddle_16_seq(a, ab, in, out, W, nW, nWdn, m);
   fft_twiddle_16_seq(ab, b, in, out, W, nW, nWdn, m);
     }
}
void fft_unshuffle_16(int a, int b, COMPLEX * in, COMPLEX * out, int m)
{
     int i;
     const COMPLEX *ip;
     COMPLEX *jp;
     if ((b - a) < 128) {
   ip = in + a * 16;
   for (i = a; i < b; ++i) {
        jp = out + i;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
   }
     } else {
   int ab = (a + b) / 2;
                  
#pragma omp task untied
   fft_unshuffle_16(a, ab, in, out, m);
                  
#pragma omp task untied
   fft_unshuffle_16(ab, b, in, out, m);
                  
#pragma omp taskwait
     }
}
void fft_unshuffle_16_seq(int a, int b, COMPLEX * in, COMPLEX * out, int m)
{
     int i;
     const COMPLEX *ip;
     COMPLEX *jp;
     if ((b - a) < 128) {
   ip = in + a * 16;
   for (i = a; i < b; ++i) {
        jp = out + i;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
   }
     } else {
   int ab = (a + b) / 2;
   fft_unshuffle_16_seq(a, ab, in, out, m);
   fft_unshuffle_16_seq(ab, b, in, out, m);
     }
}
void fft_base_32(COMPLEX * in, COMPLEX * out)
{
     REAL tmpr, tmpi;
     {
   REAL r1_0, i1_0;
   REAL r1_1, i1_1;
   REAL r1_2, i1_2;
   REAL r1_3, i1_3;
   REAL r1_4, i1_4;
   REAL r1_5, i1_5;
   REAL r1_6, i1_6;
   REAL r1_7, i1_7;
   REAL r1_8, i1_8;
   REAL r1_9, i1_9;
   REAL r1_10, i1_10;
   REAL r1_11, i1_11;
   REAL r1_12, i1_12;
   REAL r1_13, i1_13;
   REAL r1_14, i1_14;
   REAL r1_15, i1_15;
   REAL r1_16, i1_16;
   REAL r1_17, i1_17;
   REAL r1_18, i1_18;
   REAL r1_19, i1_19;
   REAL r1_20, i1_20;
   REAL r1_21, i1_21;
   REAL r1_22, i1_22;
   REAL r1_23, i1_23;
   REAL r1_24, i1_24;
   REAL r1_25, i1_25;
   REAL r1_26, i1_26;
   REAL r1_27, i1_27;
   REAL r1_28, i1_28;
   REAL r1_29, i1_29;
   REAL r1_30, i1_30;
   REAL r1_31, i1_31;
   {
        REAL r2_0, i2_0;
        REAL r2_2, i2_2;
        REAL r2_4, i2_4;
        REAL r2_6, i2_6;
        REAL r2_8, i2_8;
        REAL r2_10, i2_10;
        REAL r2_12, i2_12;
        REAL r2_14, i2_14;
        REAL r2_16, i2_16;
        REAL r2_18, i2_18;
        REAL r2_20, i2_20;
        REAL r2_22, i2_22;
        REAL r2_24, i2_24;
        REAL r2_26, i2_26;
        REAL r2_28, i2_28;
        REAL r2_30, i2_30;
        {
      REAL r3_0, i3_0;
      REAL r3_4, i3_4;
      REAL r3_8, i3_8;
      REAL r3_12, i3_12;
      REAL r3_16, i3_16;
      REAL r3_20, i3_20;
      REAL r3_24, i3_24;
      REAL r3_28, i3_28;
      {
    REAL r4_0, i4_0;
    REAL r4_8, i4_8;
    REAL r4_16, i4_16;
    REAL r4_24, i4_24;
    {
         REAL r5_0, i5_0;
         REAL r5_16, i5_16;
         r5_0 = ((in[0]).re);
         i5_0 = ((in[0]).im);
         r5_16 = ((in[16]).re);
         i5_16 = ((in[16]).im);
         r4_0 = (r5_0 + r5_16);
         i4_0 = (i5_0 + i5_16);
         r4_16 = (r5_0 - r5_16);
         i4_16 = (i5_0 - i5_16);
    }
    {
         REAL r5_8, i5_8;
         REAL r5_24, i5_24;
         r5_8 = ((in[8]).re);
         i5_8 = ((in[8]).im);
         r5_24 = ((in[24]).re);
         i5_24 = ((in[24]).im);
         r4_8 = (r5_8 + r5_24);
         i4_8 = (i5_8 + i5_24);
         r4_24 = (r5_8 - r5_24);
         i4_24 = (i5_8 - i5_24);
    }
    r3_0 = (r4_0 + r4_8);
    i3_0 = (i4_0 + i4_8);
    r3_16 = (r4_0 - r4_8);
    i3_16 = (i4_0 - i4_8);
    r3_8 = (r4_16 + i4_24);
    i3_8 = (i4_16 - r4_24);
    r3_24 = (r4_16 - i4_24);
    i3_24 = (i4_16 + r4_24);
      }
      {
    REAL r4_4, i4_4;
    REAL r4_12, i4_12;
    REAL r4_20, i4_20;
    REAL r4_28, i4_28;
    {
         REAL r5_4, i5_4;
         REAL r5_20, i5_20;
         r5_4 = ((in[4]).re);
         i5_4 = ((in[4]).im);
         r5_20 = ((in[20]).re);
         i5_20 = ((in[20]).im);
         r4_4 = (r5_4 + r5_20);
         i4_4 = (i5_4 + i5_20);
         r4_20 = (r5_4 - r5_20);
         i4_20 = (i5_4 - i5_20);
    }
    {
         REAL r5_12, i5_12;
         REAL r5_28, i5_28;
         r5_12 = ((in[12]).re);
         i5_12 = ((in[12]).im);
         r5_28 = ((in[28]).re);
         i5_28 = ((in[28]).im);
         r4_12 = (r5_12 + r5_28);
         i4_12 = (i5_12 + i5_28);
         r4_28 = (r5_12 - r5_28);
         i4_28 = (i5_12 - i5_28);
    }
    r3_4 = (r4_4 + r4_12);
    i3_4 = (i4_4 + i4_12);
    r3_20 = (r4_4 - r4_12);
    i3_20 = (i4_4 - i4_12);
    r3_12 = (r4_20 + i4_28);
    i3_12 = (i4_20 - r4_28);
    r3_28 = (r4_20 - i4_28);
    i3_28 = (i4_20 + r4_28);
      }
      r2_0 = (r3_0 + r3_4);
      i2_0 = (i3_0 + i3_4);
      r2_16 = (r3_0 - r3_4);
      i2_16 = (i3_0 - i3_4);
      tmpr = (0.707106781187 * (r3_12 + i3_12));
      tmpi = (0.707106781187 * (i3_12 - r3_12));
      r2_4 = (r3_8 + tmpr);
      i2_4 = (i3_8 + tmpi);
      r2_20 = (r3_8 - tmpr);
      i2_20 = (i3_8 - tmpi);
      r2_8 = (r3_16 + i3_20);
      i2_8 = (i3_16 - r3_20);
      r2_24 = (r3_16 - i3_20);
      i2_24 = (i3_16 + r3_20);
      tmpr = (0.707106781187 * (i3_28 - r3_28));
      tmpi = (0.707106781187 * (r3_28 + i3_28));
      r2_12 = (r3_24 + tmpr);
      i2_12 = (i3_24 - tmpi);
      r2_28 = (r3_24 - tmpr);
      i2_28 = (i3_24 + tmpi);
        }
        {
      REAL r3_2, i3_2;
      REAL r3_6, i3_6;
      REAL r3_10, i3_10;
      REAL r3_14, i3_14;
      REAL r3_18, i3_18;
      REAL r3_22, i3_22;
      REAL r3_26, i3_26;
      REAL r3_30, i3_30;
      {
    REAL r4_2, i4_2;
    REAL r4_10, i4_10;
    REAL r4_18, i4_18;
    REAL r4_26, i4_26;
    {
         REAL r5_2, i5_2;
         REAL r5_18, i5_18;
         r5_2 = ((in[2]).re);
         i5_2 = ((in[2]).im);
         r5_18 = ((in[18]).re);
         i5_18 = ((in[18]).im);
         r4_2 = (r5_2 + r5_18);
         i4_2 = (i5_2 + i5_18);
         r4_18 = (r5_2 - r5_18);
         i4_18 = (i5_2 - i5_18);
    }
    {
         REAL r5_10, i5_10;
         REAL r5_26, i5_26;
         r5_10 = ((in[10]).re);
         i5_10 = ((in[10]).im);
         r5_26 = ((in[26]).re);
         i5_26 = ((in[26]).im);
         r4_10 = (r5_10 + r5_26);
         i4_10 = (i5_10 + i5_26);
         r4_26 = (r5_10 - r5_26);
         i4_26 = (i5_10 - i5_26);
    }
    r3_2 = (r4_2 + r4_10);
    i3_2 = (i4_2 + i4_10);
    r3_18 = (r4_2 - r4_10);
    i3_18 = (i4_2 - i4_10);
    r3_10 = (r4_18 + i4_26);
    i3_10 = (i4_18 - r4_26);
    r3_26 = (r4_18 - i4_26);
    i3_26 = (i4_18 + r4_26);
      }
      {
    REAL r4_6, i4_6;
    REAL r4_14, i4_14;
    REAL r4_22, i4_22;
    REAL r4_30, i4_30;
    {
         REAL r5_6, i5_6;
         REAL r5_22, i5_22;
         r5_6 = ((in[6]).re);
         i5_6 = ((in[6]).im);
         r5_22 = ((in[22]).re);
         i5_22 = ((in[22]).im);
         r4_6 = (r5_6 + r5_22);
         i4_6 = (i5_6 + i5_22);
         r4_22 = (r5_6 - r5_22);
         i4_22 = (i5_6 - i5_22);
    }
    {
         REAL r5_14, i5_14;
         REAL r5_30, i5_30;
         r5_14 = ((in[14]).re);
         i5_14 = ((in[14]).im);
         r5_30 = ((in[30]).re);
         i5_30 = ((in[30]).im);
         r4_14 = (r5_14 + r5_30);
         i4_14 = (i5_14 + i5_30);
         r4_30 = (r5_14 - r5_30);
         i4_30 = (i5_14 - i5_30);
    }
    r3_6 = (r4_6 + r4_14);
    i3_6 = (i4_6 + i4_14);
    r3_22 = (r4_6 - r4_14);
    i3_22 = (i4_6 - i4_14);
    r3_14 = (r4_22 + i4_30);
    i3_14 = (i4_22 - r4_30);
    r3_30 = (r4_22 - i4_30);
    i3_30 = (i4_22 + r4_30);
      }
      r2_2 = (r3_2 + r3_6);
      i2_2 = (i3_2 + i3_6);
      r2_18 = (r3_2 - r3_6);
      i2_18 = (i3_2 - i3_6);
      tmpr = (0.707106781187 * (r3_14 + i3_14));
      tmpi = (0.707106781187 * (i3_14 - r3_14));
      r2_6 = (r3_10 + tmpr);
      i2_6 = (i3_10 + tmpi);
      r2_22 = (r3_10 - tmpr);
      i2_22 = (i3_10 - tmpi);
      r2_10 = (r3_18 + i3_22);
      i2_10 = (i3_18 - r3_22);
      r2_26 = (r3_18 - i3_22);
      i2_26 = (i3_18 + r3_22);
      tmpr = (0.707106781187 * (i3_30 - r3_30));
      tmpi = (0.707106781187 * (r3_30 + i3_30));
      r2_14 = (r3_26 + tmpr);
      i2_14 = (i3_26 - tmpi);
      r2_30 = (r3_26 - tmpr);
      i2_30 = (i3_26 + tmpi);
        }
        r1_0 = (r2_0 + r2_2);
        i1_0 = (i2_0 + i2_2);
        r1_16 = (r2_0 - r2_2);
        i1_16 = (i2_0 - i2_2);
        tmpr = ((0.923879532511 * r2_6) + (0.382683432365 * i2_6));
        tmpi = ((0.923879532511 * i2_6) - (0.382683432365 * r2_6));
        r1_2 = (r2_4 + tmpr);
        i1_2 = (i2_4 + tmpi);
        r1_18 = (r2_4 - tmpr);
        i1_18 = (i2_4 - tmpi);
        tmpr = (0.707106781187 * (r2_10 + i2_10));
        tmpi = (0.707106781187 * (i2_10 - r2_10));
        r1_4 = (r2_8 + tmpr);
        i1_4 = (i2_8 + tmpi);
        r1_20 = (r2_8 - tmpr);
        i1_20 = (i2_8 - tmpi);
        tmpr = ((0.382683432365 * r2_14) + (0.923879532511 * i2_14));
        tmpi = ((0.382683432365 * i2_14) - (0.923879532511 * r2_14));
        r1_6 = (r2_12 + tmpr);
        i1_6 = (i2_12 + tmpi);
        r1_22 = (r2_12 - tmpr);
        i1_22 = (i2_12 - tmpi);
        r1_8 = (r2_16 + i2_18);
        i1_8 = (i2_16 - r2_18);
        r1_24 = (r2_16 - i2_18);
        i1_24 = (i2_16 + r2_18);
        tmpr = ((0.923879532511 * i2_22) - (0.382683432365 * r2_22));
        tmpi = ((0.923879532511 * r2_22) + (0.382683432365 * i2_22));
        r1_10 = (r2_20 + tmpr);
        i1_10 = (i2_20 - tmpi);
        r1_26 = (r2_20 - tmpr);
        i1_26 = (i2_20 + tmpi);
        tmpr = (0.707106781187 * (i2_26 - r2_26));
        tmpi = (0.707106781187 * (r2_26 + i2_26));
        r1_12 = (r2_24 + tmpr);
        i1_12 = (i2_24 - tmpi);
        r1_28 = (r2_24 - tmpr);
        i1_28 = (i2_24 + tmpi);
        tmpr = ((0.382683432365 * i2_30) - (0.923879532511 * r2_30));
        tmpi = ((0.382683432365 * r2_30) + (0.923879532511 * i2_30));
        r1_14 = (r2_28 + tmpr);
        i1_14 = (i2_28 - tmpi);
        r1_30 = (r2_28 - tmpr);
        i1_30 = (i2_28 + tmpi);
   }
   {
        REAL r2_1, i2_1;
        REAL r2_3, i2_3;
        REAL r2_5, i2_5;
        REAL r2_7, i2_7;
        REAL r2_9, i2_9;
        REAL r2_11, i2_11;
        REAL r2_13, i2_13;
        REAL r2_15, i2_15;
        REAL r2_17, i2_17;
        REAL r2_19, i2_19;
        REAL r2_21, i2_21;
        REAL r2_23, i2_23;
        REAL r2_25, i2_25;
        REAL r2_27, i2_27;
        REAL r2_29, i2_29;
        REAL r2_31, i2_31;
        {
      REAL r3_1, i3_1;
      REAL r3_5, i3_5;
      REAL r3_9, i3_9;
      REAL r3_13, i3_13;
      REAL r3_17, i3_17;
      REAL r3_21, i3_21;
      REAL r3_25, i3_25;
      REAL r3_29, i3_29;
      {
    REAL r4_1, i4_1;
    REAL r4_9, i4_9;
    REAL r4_17, i4_17;
    REAL r4_25, i4_25;
    {
         REAL r5_1, i5_1;
         REAL r5_17, i5_17;
         r5_1 = ((in[1]).re);
         i5_1 = ((in[1]).im);
         r5_17 = ((in[17]).re);
         i5_17 = ((in[17]).im);
         r4_1 = (r5_1 + r5_17);
         i4_1 = (i5_1 + i5_17);
         r4_17 = (r5_1 - r5_17);
         i4_17 = (i5_1 - i5_17);
    }
    {
         REAL r5_9, i5_9;
         REAL r5_25, i5_25;
         r5_9 = ((in[9]).re);
         i5_9 = ((in[9]).im);
         r5_25 = ((in[25]).re);
         i5_25 = ((in[25]).im);
         r4_9 = (r5_9 + r5_25);
         i4_9 = (i5_9 + i5_25);
         r4_25 = (r5_9 - r5_25);
         i4_25 = (i5_9 - i5_25);
    }
    r3_1 = (r4_1 + r4_9);
    i3_1 = (i4_1 + i4_9);
    r3_17 = (r4_1 - r4_9);
    i3_17 = (i4_1 - i4_9);
    r3_9 = (r4_17 + i4_25);
    i3_9 = (i4_17 - r4_25);
    r3_25 = (r4_17 - i4_25);
    i3_25 = (i4_17 + r4_25);
      }
      {
    REAL r4_5, i4_5;
    REAL r4_13, i4_13;
    REAL r4_21, i4_21;
    REAL r4_29, i4_29;
    {
         REAL r5_5, i5_5;
         REAL r5_21, i5_21;
         r5_5 = ((in[5]).re);
         i5_5 = ((in[5]).im);
         r5_21 = ((in[21]).re);
         i5_21 = ((in[21]).im);
         r4_5 = (r5_5 + r5_21);
         i4_5 = (i5_5 + i5_21);
         r4_21 = (r5_5 - r5_21);
         i4_21 = (i5_5 - i5_21);
    }
    {
         REAL r5_13, i5_13;
         REAL r5_29, i5_29;
         r5_13 = ((in[13]).re);
         i5_13 = ((in[13]).im);
         r5_29 = ((in[29]).re);
         i5_29 = ((in[29]).im);
         r4_13 = (r5_13 + r5_29);
         i4_13 = (i5_13 + i5_29);
         r4_29 = (r5_13 - r5_29);
         i4_29 = (i5_13 - i5_29);
    }
    r3_5 = (r4_5 + r4_13);
    i3_5 = (i4_5 + i4_13);
    r3_21 = (r4_5 - r4_13);
    i3_21 = (i4_5 - i4_13);
    r3_13 = (r4_21 + i4_29);
    i3_13 = (i4_21 - r4_29);
    r3_29 = (r4_21 - i4_29);
    i3_29 = (i4_21 + r4_29);
      }
      r2_1 = (r3_1 + r3_5);
      i2_1 = (i3_1 + i3_5);
      r2_17 = (r3_1 - r3_5);
      i2_17 = (i3_1 - i3_5);
      tmpr = (0.707106781187 * (r3_13 + i3_13));
      tmpi = (0.707106781187 * (i3_13 - r3_13));
      r2_5 = (r3_9 + tmpr);
      i2_5 = (i3_9 + tmpi);
      r2_21 = (r3_9 - tmpr);
      i2_21 = (i3_9 - tmpi);
      r2_9 = (r3_17 + i3_21);
      i2_9 = (i3_17 - r3_21);
      r2_25 = (r3_17 - i3_21);
      i2_25 = (i3_17 + r3_21);
      tmpr = (0.707106781187 * (i3_29 - r3_29));
      tmpi = (0.707106781187 * (r3_29 + i3_29));
      r2_13 = (r3_25 + tmpr);
      i2_13 = (i3_25 - tmpi);
      r2_29 = (r3_25 - tmpr);
      i2_29 = (i3_25 + tmpi);
        }
        {
      REAL r3_3, i3_3;
      REAL r3_7, i3_7;
      REAL r3_11, i3_11;
      REAL r3_15, i3_15;
      REAL r3_19, i3_19;
      REAL r3_23, i3_23;
      REAL r3_27, i3_27;
      REAL r3_31, i3_31;
      {
    REAL r4_3, i4_3;
    REAL r4_11, i4_11;
    REAL r4_19, i4_19;
    REAL r4_27, i4_27;
    {
         REAL r5_3, i5_3;
         REAL r5_19, i5_19;
         r5_3 = ((in[3]).re);
         i5_3 = ((in[3]).im);
         r5_19 = ((in[19]).re);
         i5_19 = ((in[19]).im);
         r4_3 = (r5_3 + r5_19);
         i4_3 = (i5_3 + i5_19);
         r4_19 = (r5_3 - r5_19);
         i4_19 = (i5_3 - i5_19);
    }
    {
         REAL r5_11, i5_11;
         REAL r5_27, i5_27;
         r5_11 = ((in[11]).re);
         i5_11 = ((in[11]).im);
         r5_27 = ((in[27]).re);
         i5_27 = ((in[27]).im);
         r4_11 = (r5_11 + r5_27);
         i4_11 = (i5_11 + i5_27);
         r4_27 = (r5_11 - r5_27);
         i4_27 = (i5_11 - i5_27);
    }
    r3_3 = (r4_3 + r4_11);
    i3_3 = (i4_3 + i4_11);
    r3_19 = (r4_3 - r4_11);
    i3_19 = (i4_3 - i4_11);
    r3_11 = (r4_19 + i4_27);
    i3_11 = (i4_19 - r4_27);
    r3_27 = (r4_19 - i4_27);
    i3_27 = (i4_19 + r4_27);
      }
      {
    REAL r4_7, i4_7;
    REAL r4_15, i4_15;
    REAL r4_23, i4_23;
    REAL r4_31, i4_31;
    {
         REAL r5_7, i5_7;
         REAL r5_23, i5_23;
         r5_7 = ((in[7]).re);
         i5_7 = ((in[7]).im);
         r5_23 = ((in[23]).re);
         i5_23 = ((in[23]).im);
         r4_7 = (r5_7 + r5_23);
         i4_7 = (i5_7 + i5_23);
         r4_23 = (r5_7 - r5_23);
         i4_23 = (i5_7 - i5_23);
    }
    {
         REAL r5_15, i5_15;
         REAL r5_31, i5_31;
         r5_15 = ((in[15]).re);
         i5_15 = ((in[15]).im);
         r5_31 = ((in[31]).re);
         i5_31 = ((in[31]).im);
         r4_15 = (r5_15 + r5_31);
         i4_15 = (i5_15 + i5_31);
         r4_31 = (r5_15 - r5_31);
         i4_31 = (i5_15 - i5_31);
    }
    r3_7 = (r4_7 + r4_15);
    i3_7 = (i4_7 + i4_15);
    r3_23 = (r4_7 - r4_15);
    i3_23 = (i4_7 - i4_15);
    r3_15 = (r4_23 + i4_31);
    i3_15 = (i4_23 - r4_31);
    r3_31 = (r4_23 - i4_31);
    i3_31 = (i4_23 + r4_31);
      }
      r2_3 = (r3_3 + r3_7);
      i2_3 = (i3_3 + i3_7);
      r2_19 = (r3_3 - r3_7);
      i2_19 = (i3_3 - i3_7);
      tmpr = (0.707106781187 * (r3_15 + i3_15));
      tmpi = (0.707106781187 * (i3_15 - r3_15));
      r2_7 = (r3_11 + tmpr);
      i2_7 = (i3_11 + tmpi);
      r2_23 = (r3_11 - tmpr);
      i2_23 = (i3_11 - tmpi);
      r2_11 = (r3_19 + i3_23);
      i2_11 = (i3_19 - r3_23);
      r2_27 = (r3_19 - i3_23);
      i2_27 = (i3_19 + r3_23);
      tmpr = (0.707106781187 * (i3_31 - r3_31));
      tmpi = (0.707106781187 * (r3_31 + i3_31));
      r2_15 = (r3_27 + tmpr);
      i2_15 = (i3_27 - tmpi);
      r2_31 = (r3_27 - tmpr);
      i2_31 = (i3_27 + tmpi);
        }
        r1_1 = (r2_1 + r2_3);
        i1_1 = (i2_1 + i2_3);
        r1_17 = (r2_1 - r2_3);
        i1_17 = (i2_1 - i2_3);
        tmpr = ((0.923879532511 * r2_7) + (0.382683432365 * i2_7));
        tmpi = ((0.923879532511 * i2_7) - (0.382683432365 * r2_7));
        r1_3 = (r2_5 + tmpr);
        i1_3 = (i2_5 + tmpi);
        r1_19 = (r2_5 - tmpr);
        i1_19 = (i2_5 - tmpi);
        tmpr = (0.707106781187 * (r2_11 + i2_11));
        tmpi = (0.707106781187 * (i2_11 - r2_11));
        r1_5 = (r2_9 + tmpr);
        i1_5 = (i2_9 + tmpi);
        r1_21 = (r2_9 - tmpr);
        i1_21 = (i2_9 - tmpi);
        tmpr = ((0.382683432365 * r2_15) + (0.923879532511 * i2_15));
        tmpi = ((0.382683432365 * i2_15) - (0.923879532511 * r2_15));
        r1_7 = (r2_13 + tmpr);
        i1_7 = (i2_13 + tmpi);
        r1_23 = (r2_13 - tmpr);
        i1_23 = (i2_13 - tmpi);
        r1_9 = (r2_17 + i2_19);
        i1_9 = (i2_17 - r2_19);
        r1_25 = (r2_17 - i2_19);
        i1_25 = (i2_17 + r2_19);
        tmpr = ((0.923879532511 * i2_23) - (0.382683432365 * r2_23));
        tmpi = ((0.923879532511 * r2_23) + (0.382683432365 * i2_23));
        r1_11 = (r2_21 + tmpr);
        i1_11 = (i2_21 - tmpi);
        r1_27 = (r2_21 - tmpr);
        i1_27 = (i2_21 + tmpi);
        tmpr = (0.707106781187 * (i2_27 - r2_27));
        tmpi = (0.707106781187 * (r2_27 + i2_27));
        r1_13 = (r2_25 + tmpr);
        i1_13 = (i2_25 - tmpi);
        r1_29 = (r2_25 - tmpr);
        i1_29 = (i2_25 + tmpi);
        tmpr = ((0.382683432365 * i2_31) - (0.923879532511 * r2_31));
        tmpi = ((0.382683432365 * r2_31) + (0.923879532511 * i2_31));
        r1_15 = (r2_29 + tmpr);
        i1_15 = (i2_29 - tmpi);
        r1_31 = (r2_29 - tmpr);
        i1_31 = (i2_29 + tmpi);
   }
   ((out[0]).re) = (r1_0 + r1_1);
   ((out[0]).im) = (i1_0 + i1_1);
   ((out[16]).re) = (r1_0 - r1_1);
   ((out[16]).im) = (i1_0 - i1_1);
   tmpr = ((0.980785280403 * r1_3) + (0.195090322016 * i1_3));
   tmpi = ((0.980785280403 * i1_3) - (0.195090322016 * r1_3));
   ((out[1]).re) = (r1_2 + tmpr);
   ((out[1]).im) = (i1_2 + tmpi);
   ((out[17]).re) = (r1_2 - tmpr);
   ((out[17]).im) = (i1_2 - tmpi);
   tmpr = ((0.923879532511 * r1_5) + (0.382683432365 * i1_5));
   tmpi = ((0.923879532511 * i1_5) - (0.382683432365 * r1_5));
   ((out[2]).re) = (r1_4 + tmpr);
   ((out[2]).im) = (i1_4 + tmpi);
   ((out[18]).re) = (r1_4 - tmpr);
   ((out[18]).im) = (i1_4 - tmpi);
   tmpr = ((0.831469612303 * r1_7) + (0.55557023302 * i1_7));
   tmpi = ((0.831469612303 * i1_7) - (0.55557023302 * r1_7));
   ((out[3]).re) = (r1_6 + tmpr);
   ((out[3]).im) = (i1_6 + tmpi);
   ((out[19]).re) = (r1_6 - tmpr);
   ((out[19]).im) = (i1_6 - tmpi);
   tmpr = (0.707106781187 * (r1_9 + i1_9));
   tmpi = (0.707106781187 * (i1_9 - r1_9));
   ((out[4]).re) = (r1_8 + tmpr);
   ((out[4]).im) = (i1_8 + tmpi);
   ((out[20]).re) = (r1_8 - tmpr);
   ((out[20]).im) = (i1_8 - tmpi);
   tmpr = ((0.55557023302 * r1_11) + (0.831469612303 * i1_11));
   tmpi = ((0.55557023302 * i1_11) - (0.831469612303 * r1_11));
   ((out[5]).re) = (r1_10 + tmpr);
   ((out[5]).im) = (i1_10 + tmpi);
   ((out[21]).re) = (r1_10 - tmpr);
   ((out[21]).im) = (i1_10 - tmpi);
   tmpr = ((0.382683432365 * r1_13) + (0.923879532511 * i1_13));
   tmpi = ((0.382683432365 * i1_13) - (0.923879532511 * r1_13));
   ((out[6]).re) = (r1_12 + tmpr);
   ((out[6]).im) = (i1_12 + tmpi);
   ((out[22]).re) = (r1_12 - tmpr);
   ((out[22]).im) = (i1_12 - tmpi);
   tmpr = ((0.195090322016 * r1_15) + (0.980785280403 * i1_15));
   tmpi = ((0.195090322016 * i1_15) - (0.980785280403 * r1_15));
   ((out[7]).re) = (r1_14 + tmpr);
   ((out[7]).im) = (i1_14 + tmpi);
   ((out[23]).re) = (r1_14 - tmpr);
   ((out[23]).im) = (i1_14 - tmpi);
   ((out[8]).re) = (r1_16 + i1_17);
   ((out[8]).im) = (i1_16 - r1_17);
   ((out[24]).re) = (r1_16 - i1_17);
   ((out[24]).im) = (i1_16 + r1_17);
   tmpr = ((0.980785280403 * i1_19) - (0.195090322016 * r1_19));
   tmpi = ((0.980785280403 * r1_19) + (0.195090322016 * i1_19));
   ((out[9]).re) = (r1_18 + tmpr);
   ((out[9]).im) = (i1_18 - tmpi);
   ((out[25]).re) = (r1_18 - tmpr);
   ((out[25]).im) = (i1_18 + tmpi);
   tmpr = ((0.923879532511 * i1_21) - (0.382683432365 * r1_21));
   tmpi = ((0.923879532511 * r1_21) + (0.382683432365 * i1_21));
   ((out[10]).re) = (r1_20 + tmpr);
   ((out[10]).im) = (i1_20 - tmpi);
   ((out[26]).re) = (r1_20 - tmpr);
   ((out[26]).im) = (i1_20 + tmpi);
   tmpr = ((0.831469612303 * i1_23) - (0.55557023302 * r1_23));
   tmpi = ((0.831469612303 * r1_23) + (0.55557023302 * i1_23));
   ((out[11]).re) = (r1_22 + tmpr);
   ((out[11]).im) = (i1_22 - tmpi);
   ((out[27]).re) = (r1_22 - tmpr);
   ((out[27]).im) = (i1_22 + tmpi);
   tmpr = (0.707106781187 * (i1_25 - r1_25));
   tmpi = (0.707106781187 * (r1_25 + i1_25));
   ((out[12]).re) = (r1_24 + tmpr);
   ((out[12]).im) = (i1_24 - tmpi);
   ((out[28]).re) = (r1_24 - tmpr);
   ((out[28]).im) = (i1_24 + tmpi);
   tmpr = ((0.55557023302 * i1_27) - (0.831469612303 * r1_27));
   tmpi = ((0.55557023302 * r1_27) + (0.831469612303 * i1_27));
   ((out[13]).re) = (r1_26 + tmpr);
   ((out[13]).im) = (i1_26 - tmpi);
   ((out[29]).re) = (r1_26 - tmpr);
   ((out[29]).im) = (i1_26 + tmpi);
   tmpr = ((0.382683432365 * i1_29) - (0.923879532511 * r1_29));
   tmpi = ((0.382683432365 * r1_29) + (0.923879532511 * i1_29));
   ((out[14]).re) = (r1_28 + tmpr);
   ((out[14]).im) = (i1_28 - tmpi);
   ((out[30]).re) = (r1_28 - tmpr);
   ((out[30]).im) = (i1_28 + tmpi);
   tmpr = ((0.195090322016 * i1_31) - (0.980785280403 * r1_31));
   tmpi = ((0.195090322016 * r1_31) + (0.980785280403 * i1_31));
   ((out[15]).re) = (r1_30 + tmpr);
   ((out[15]).im) = (i1_30 - tmpi);
   ((out[31]).re) = (r1_30 - tmpr);
   ((out[31]).im) = (i1_30 + tmpi);
     }
}
void fft_twiddle_32(int a, int b, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int m)
{
     int l1, i;
     COMPLEX *jp, *kp;
     REAL tmpr, tmpi, wr, wi;
     if ((b - a) < 128) {
   for (i = a, l1 = nWdn * i, kp = out + i; i < b;
        i++, l1 += nWdn, kp++) {
        jp = in + i;
        {
      REAL r1_0, i1_0;
      REAL r1_1, i1_1;
      REAL r1_2, i1_2;
      REAL r1_3, i1_3;
      REAL r1_4, i1_4;
      REAL r1_5, i1_5;
      REAL r1_6, i1_6;
      REAL r1_7, i1_7;
      REAL r1_8, i1_8;
      REAL r1_9, i1_9;
      REAL r1_10, i1_10;
      REAL r1_11, i1_11;
      REAL r1_12, i1_12;
      REAL r1_13, i1_13;
      REAL r1_14, i1_14;
      REAL r1_15, i1_15;
      REAL r1_16, i1_16;
      REAL r1_17, i1_17;
      REAL r1_18, i1_18;
      REAL r1_19, i1_19;
      REAL r1_20, i1_20;
      REAL r1_21, i1_21;
      REAL r1_22, i1_22;
      REAL r1_23, i1_23;
      REAL r1_24, i1_24;
      REAL r1_25, i1_25;
      REAL r1_26, i1_26;
      REAL r1_27, i1_27;
      REAL r1_28, i1_28;
      REAL r1_29, i1_29;
      REAL r1_30, i1_30;
      REAL r1_31, i1_31;
      {
    REAL r2_0, i2_0;
    REAL r2_2, i2_2;
    REAL r2_4, i2_4;
    REAL r2_6, i2_6;
    REAL r2_8, i2_8;
    REAL r2_10, i2_10;
    REAL r2_12, i2_12;
    REAL r2_14, i2_14;
    REAL r2_16, i2_16;
    REAL r2_18, i2_18;
    REAL r2_20, i2_20;
    REAL r2_22, i2_22;
    REAL r2_24, i2_24;
    REAL r2_26, i2_26;
    REAL r2_28, i2_28;
    REAL r2_30, i2_30;
    {
         REAL r3_0, i3_0;
         REAL r3_4, i3_4;
         REAL r3_8, i3_8;
         REAL r3_12, i3_12;
         REAL r3_16, i3_16;
         REAL r3_20, i3_20;
         REAL r3_24, i3_24;
         REAL r3_28, i3_28;
         {
       REAL r4_0, i4_0;
       REAL r4_8, i4_8;
       REAL r4_16, i4_16;
       REAL r4_24, i4_24;
       {
     REAL r5_0, i5_0;
     REAL r5_16, i5_16;
     r5_0 = ((jp[0 * m]).re);
     i5_0 = ((jp[0 * m]).im);
     wr = ((W[16 * l1]).re);
     wi = ((W[16 * l1]).im);
     tmpr = ((jp[16 * m]).re);
     tmpi = ((jp[16 * m]).im);
     r5_16 = ((wr * tmpr) - (wi * tmpi));
     i5_16 = ((wi * tmpr) + (wr * tmpi));
     r4_0 = (r5_0 + r5_16);
     i4_0 = (i5_0 + i5_16);
     r4_16 = (r5_0 - r5_16);
     i4_16 = (i5_0 - i5_16);
       }
       {
     REAL r5_8, i5_8;
     REAL r5_24, i5_24;
     wr = ((W[8 * l1]).re);
     wi = ((W[8 * l1]).im);
     tmpr = ((jp[8 * m]).re);
     tmpi = ((jp[8 * m]).im);
     r5_8 = ((wr * tmpr) - (wi * tmpi));
     i5_8 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[24 * l1]).re);
     wi = ((W[24 * l1]).im);
     tmpr = ((jp[24 * m]).re);
     tmpi = ((jp[24 * m]).im);
     r5_24 = ((wr * tmpr) - (wi * tmpi));
     i5_24 = ((wi * tmpr) + (wr * tmpi));
     r4_8 = (r5_8 + r5_24);
     i4_8 = (i5_8 + i5_24);
     r4_24 = (r5_8 - r5_24);
     i4_24 = (i5_8 - i5_24);
       }
       r3_0 = (r4_0 + r4_8);
       i3_0 = (i4_0 + i4_8);
       r3_16 = (r4_0 - r4_8);
       i3_16 = (i4_0 - i4_8);
       r3_8 = (r4_16 + i4_24);
       i3_8 = (i4_16 - r4_24);
       r3_24 = (r4_16 - i4_24);
       i3_24 = (i4_16 + r4_24);
         }
         {
       REAL r4_4, i4_4;
       REAL r4_12, i4_12;
       REAL r4_20, i4_20;
       REAL r4_28, i4_28;
       {
     REAL r5_4, i5_4;
     REAL r5_20, i5_20;
     wr = ((W[4 * l1]).re);
     wi = ((W[4 * l1]).im);
     tmpr = ((jp[4 * m]).re);
     tmpi = ((jp[4 * m]).im);
     r5_4 = ((wr * tmpr) - (wi * tmpi));
     i5_4 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[20 * l1]).re);
     wi = ((W[20 * l1]).im);
     tmpr = ((jp[20 * m]).re);
     tmpi = ((jp[20 * m]).im);
     r5_20 = ((wr * tmpr) - (wi * tmpi));
     i5_20 = ((wi * tmpr) + (wr * tmpi));
     r4_4 = (r5_4 + r5_20);
     i4_4 = (i5_4 + i5_20);
     r4_20 = (r5_4 - r5_20);
     i4_20 = (i5_4 - i5_20);
       }
       {
     REAL r5_12, i5_12;
     REAL r5_28, i5_28;
     wr = ((W[12 * l1]).re);
     wi = ((W[12 * l1]).im);
     tmpr = ((jp[12 * m]).re);
     tmpi = ((jp[12 * m]).im);
     r5_12 = ((wr * tmpr) - (wi * tmpi));
     i5_12 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[28 * l1]).re);
     wi = ((W[28 * l1]).im);
     tmpr = ((jp[28 * m]).re);
     tmpi = ((jp[28 * m]).im);
     r5_28 = ((wr * tmpr) - (wi * tmpi));
     i5_28 = ((wi * tmpr) + (wr * tmpi));
     r4_12 = (r5_12 + r5_28);
     i4_12 = (i5_12 + i5_28);
     r4_28 = (r5_12 - r5_28);
     i4_28 = (i5_12 - i5_28);
       }
       r3_4 = (r4_4 + r4_12);
       i3_4 = (i4_4 + i4_12);
       r3_20 = (r4_4 - r4_12);
       i3_20 = (i4_4 - i4_12);
       r3_12 = (r4_20 + i4_28);
       i3_12 = (i4_20 - r4_28);
       r3_28 = (r4_20 - i4_28);
       i3_28 = (i4_20 + r4_28);
         }
         r2_0 = (r3_0 + r3_4);
         i2_0 = (i3_0 + i3_4);
         r2_16 = (r3_0 - r3_4);
         i2_16 = (i3_0 - i3_4);
         tmpr = (0.707106781187 * (r3_12 + i3_12));
         tmpi = (0.707106781187 * (i3_12 - r3_12));
         r2_4 = (r3_8 + tmpr);
         i2_4 = (i3_8 + tmpi);
         r2_20 = (r3_8 - tmpr);
         i2_20 = (i3_8 - tmpi);
         r2_8 = (r3_16 + i3_20);
         i2_8 = (i3_16 - r3_20);
         r2_24 = (r3_16 - i3_20);
         i2_24 = (i3_16 + r3_20);
         tmpr = (0.707106781187 * (i3_28 - r3_28));
         tmpi = (0.707106781187 * (r3_28 + i3_28));
         r2_12 = (r3_24 + tmpr);
         i2_12 = (i3_24 - tmpi);
         r2_28 = (r3_24 - tmpr);
         i2_28 = (i3_24 + tmpi);
    }
    {
         REAL r3_2, i3_2;
         REAL r3_6, i3_6;
         REAL r3_10, i3_10;
         REAL r3_14, i3_14;
         REAL r3_18, i3_18;
         REAL r3_22, i3_22;
         REAL r3_26, i3_26;
         REAL r3_30, i3_30;
         {
       REAL r4_2, i4_2;
       REAL r4_10, i4_10;
       REAL r4_18, i4_18;
       REAL r4_26, i4_26;
       {
     REAL r5_2, i5_2;
     REAL r5_18, i5_18;
     wr = ((W[2 * l1]).re);
     wi = ((W[2 * l1]).im);
     tmpr = ((jp[2 * m]).re);
     tmpi = ((jp[2 * m]).im);
     r5_2 = ((wr * tmpr) - (wi * tmpi));
     i5_2 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[18 * l1]).re);
     wi = ((W[18 * l1]).im);
     tmpr = ((jp[18 * m]).re);
     tmpi = ((jp[18 * m]).im);
     r5_18 = ((wr * tmpr) - (wi * tmpi));
     i5_18 = ((wi * tmpr) + (wr * tmpi));
     r4_2 = (r5_2 + r5_18);
     i4_2 = (i5_2 + i5_18);
     r4_18 = (r5_2 - r5_18);
     i4_18 = (i5_2 - i5_18);
       }
       {
     REAL r5_10, i5_10;
     REAL r5_26, i5_26;
     wr = ((W[10 * l1]).re);
     wi = ((W[10 * l1]).im);
     tmpr = ((jp[10 * m]).re);
     tmpi = ((jp[10 * m]).im);
     r5_10 = ((wr * tmpr) - (wi * tmpi));
     i5_10 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[26 * l1]).re);
     wi = ((W[26 * l1]).im);
     tmpr = ((jp[26 * m]).re);
     tmpi = ((jp[26 * m]).im);
     r5_26 = ((wr * tmpr) - (wi * tmpi));
     i5_26 = ((wi * tmpr) + (wr * tmpi));
     r4_10 = (r5_10 + r5_26);
     i4_10 = (i5_10 + i5_26);
     r4_26 = (r5_10 - r5_26);
     i4_26 = (i5_10 - i5_26);
       }
       r3_2 = (r4_2 + r4_10);
       i3_2 = (i4_2 + i4_10);
       r3_18 = (r4_2 - r4_10);
       i3_18 = (i4_2 - i4_10);
       r3_10 = (r4_18 + i4_26);
       i3_10 = (i4_18 - r4_26);
       r3_26 = (r4_18 - i4_26);
       i3_26 = (i4_18 + r4_26);
         }
         {
       REAL r4_6, i4_6;
       REAL r4_14, i4_14;
       REAL r4_22, i4_22;
       REAL r4_30, i4_30;
       {
     REAL r5_6, i5_6;
     REAL r5_22, i5_22;
     wr = ((W[6 * l1]).re);
     wi = ((W[6 * l1]).im);
     tmpr = ((jp[6 * m]).re);
     tmpi = ((jp[6 * m]).im);
     r5_6 = ((wr * tmpr) - (wi * tmpi));
     i5_6 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[22 * l1]).re);
     wi = ((W[22 * l1]).im);
     tmpr = ((jp[22 * m]).re);
     tmpi = ((jp[22 * m]).im);
     r5_22 = ((wr * tmpr) - (wi * tmpi));
     i5_22 = ((wi * tmpr) + (wr * tmpi));
     r4_6 = (r5_6 + r5_22);
     i4_6 = (i5_6 + i5_22);
     r4_22 = (r5_6 - r5_22);
     i4_22 = (i5_6 - i5_22);
       }
       {
     REAL r5_14, i5_14;
     REAL r5_30, i5_30;
     wr = ((W[14 * l1]).re);
     wi = ((W[14 * l1]).im);
     tmpr = ((jp[14 * m]).re);
     tmpi = ((jp[14 * m]).im);
     r5_14 = ((wr * tmpr) - (wi * tmpi));
     i5_14 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[30 * l1]).re);
     wi = ((W[30 * l1]).im);
     tmpr = ((jp[30 * m]).re);
     tmpi = ((jp[30 * m]).im);
     r5_30 = ((wr * tmpr) - (wi * tmpi));
     i5_30 = ((wi * tmpr) + (wr * tmpi));
     r4_14 = (r5_14 + r5_30);
     i4_14 = (i5_14 + i5_30);
     r4_30 = (r5_14 - r5_30);
     i4_30 = (i5_14 - i5_30);
       }
       r3_6 = (r4_6 + r4_14);
       i3_6 = (i4_6 + i4_14);
       r3_22 = (r4_6 - r4_14);
       i3_22 = (i4_6 - i4_14);
       r3_14 = (r4_22 + i4_30);
       i3_14 = (i4_22 - r4_30);
       r3_30 = (r4_22 - i4_30);
       i3_30 = (i4_22 + r4_30);
         }
         r2_2 = (r3_2 + r3_6);
         i2_2 = (i3_2 + i3_6);
         r2_18 = (r3_2 - r3_6);
         i2_18 = (i3_2 - i3_6);
         tmpr = (0.707106781187 * (r3_14 + i3_14));
         tmpi = (0.707106781187 * (i3_14 - r3_14));
         r2_6 = (r3_10 + tmpr);
         i2_6 = (i3_10 + tmpi);
         r2_22 = (r3_10 - tmpr);
         i2_22 = (i3_10 - tmpi);
         r2_10 = (r3_18 + i3_22);
         i2_10 = (i3_18 - r3_22);
         r2_26 = (r3_18 - i3_22);
         i2_26 = (i3_18 + r3_22);
         tmpr = (0.707106781187 * (i3_30 - r3_30));
         tmpi = (0.707106781187 * (r3_30 + i3_30));
         r2_14 = (r3_26 + tmpr);
         i2_14 = (i3_26 - tmpi);
         r2_30 = (r3_26 - tmpr);
         i2_30 = (i3_26 + tmpi);
    }
    r1_0 = (r2_0 + r2_2);
    i1_0 = (i2_0 + i2_2);
    r1_16 = (r2_0 - r2_2);
    i1_16 = (i2_0 - i2_2);
    tmpr = ((0.923879532511 * r2_6) + (0.382683432365 * i2_6));
    tmpi = ((0.923879532511 * i2_6) - (0.382683432365 * r2_6));
    r1_2 = (r2_4 + tmpr);
    i1_2 = (i2_4 + tmpi);
    r1_18 = (r2_4 - tmpr);
    i1_18 = (i2_4 - tmpi);
    tmpr = (0.707106781187 * (r2_10 + i2_10));
    tmpi = (0.707106781187 * (i2_10 - r2_10));
    r1_4 = (r2_8 + tmpr);
    i1_4 = (i2_8 + tmpi);
    r1_20 = (r2_8 - tmpr);
    i1_20 = (i2_8 - tmpi);
    tmpr = ((0.382683432365 * r2_14) + (0.923879532511 * i2_14));
    tmpi = ((0.382683432365 * i2_14) - (0.923879532511 * r2_14));
    r1_6 = (r2_12 + tmpr);
    i1_6 = (i2_12 + tmpi);
    r1_22 = (r2_12 - tmpr);
    i1_22 = (i2_12 - tmpi);
    r1_8 = (r2_16 + i2_18);
    i1_8 = (i2_16 - r2_18);
    r1_24 = (r2_16 - i2_18);
    i1_24 = (i2_16 + r2_18);
    tmpr = ((0.923879532511 * i2_22) - (0.382683432365 * r2_22));
    tmpi = ((0.923879532511 * r2_22) + (0.382683432365 * i2_22));
    r1_10 = (r2_20 + tmpr);
    i1_10 = (i2_20 - tmpi);
    r1_26 = (r2_20 - tmpr);
    i1_26 = (i2_20 + tmpi);
    tmpr = (0.707106781187 * (i2_26 - r2_26));
    tmpi = (0.707106781187 * (r2_26 + i2_26));
    r1_12 = (r2_24 + tmpr);
    i1_12 = (i2_24 - tmpi);
    r1_28 = (r2_24 - tmpr);
    i1_28 = (i2_24 + tmpi);
    tmpr = ((0.382683432365 * i2_30) - (0.923879532511 * r2_30));
    tmpi = ((0.382683432365 * r2_30) + (0.923879532511 * i2_30));
    r1_14 = (r2_28 + tmpr);
    i1_14 = (i2_28 - tmpi);
    r1_30 = (r2_28 - tmpr);
    i1_30 = (i2_28 + tmpi);
      }
      {
    REAL r2_1, i2_1;
    REAL r2_3, i2_3;
    REAL r2_5, i2_5;
    REAL r2_7, i2_7;
    REAL r2_9, i2_9;
    REAL r2_11, i2_11;
    REAL r2_13, i2_13;
    REAL r2_15, i2_15;
    REAL r2_17, i2_17;
    REAL r2_19, i2_19;
    REAL r2_21, i2_21;
    REAL r2_23, i2_23;
    REAL r2_25, i2_25;
    REAL r2_27, i2_27;
    REAL r2_29, i2_29;
    REAL r2_31, i2_31;
    {
         REAL r3_1, i3_1;
         REAL r3_5, i3_5;
         REAL r3_9, i3_9;
         REAL r3_13, i3_13;
         REAL r3_17, i3_17;
         REAL r3_21, i3_21;
         REAL r3_25, i3_25;
         REAL r3_29, i3_29;
         {
       REAL r4_1, i4_1;
       REAL r4_9, i4_9;
       REAL r4_17, i4_17;
       REAL r4_25, i4_25;
       {
     REAL r5_1, i5_1;
     REAL r5_17, i5_17;
     wr = ((W[1 * l1]).re);
     wi = ((W[1 * l1]).im);
     tmpr = ((jp[1 * m]).re);
     tmpi = ((jp[1 * m]).im);
     r5_1 = ((wr * tmpr) - (wi * tmpi));
     i5_1 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[17 * l1]).re);
     wi = ((W[17 * l1]).im);
     tmpr = ((jp[17 * m]).re);
     tmpi = ((jp[17 * m]).im);
     r5_17 = ((wr * tmpr) - (wi * tmpi));
     i5_17 = ((wi * tmpr) + (wr * tmpi));
     r4_1 = (r5_1 + r5_17);
     i4_1 = (i5_1 + i5_17);
     r4_17 = (r5_1 - r5_17);
     i4_17 = (i5_1 - i5_17);
       }
       {
     REAL r5_9, i5_9;
     REAL r5_25, i5_25;
     wr = ((W[9 * l1]).re);
     wi = ((W[9 * l1]).im);
     tmpr = ((jp[9 * m]).re);
     tmpi = ((jp[9 * m]).im);
     r5_9 = ((wr * tmpr) - (wi * tmpi));
     i5_9 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[25 * l1]).re);
     wi = ((W[25 * l1]).im);
     tmpr = ((jp[25 * m]).re);
     tmpi = ((jp[25 * m]).im);
     r5_25 = ((wr * tmpr) - (wi * tmpi));
     i5_25 = ((wi * tmpr) + (wr * tmpi));
     r4_9 = (r5_9 + r5_25);
     i4_9 = (i5_9 + i5_25);
     r4_25 = (r5_9 - r5_25);
     i4_25 = (i5_9 - i5_25);
       }
       r3_1 = (r4_1 + r4_9);
       i3_1 = (i4_1 + i4_9);
       r3_17 = (r4_1 - r4_9);
       i3_17 = (i4_1 - i4_9);
       r3_9 = (r4_17 + i4_25);
       i3_9 = (i4_17 - r4_25);
       r3_25 = (r4_17 - i4_25);
       i3_25 = (i4_17 + r4_25);
         }
         {
       REAL r4_5, i4_5;
       REAL r4_13, i4_13;
       REAL r4_21, i4_21;
       REAL r4_29, i4_29;
       {
     REAL r5_5, i5_5;
     REAL r5_21, i5_21;
     wr = ((W[5 * l1]).re);
     wi = ((W[5 * l1]).im);
     tmpr = ((jp[5 * m]).re);
     tmpi = ((jp[5 * m]).im);
     r5_5 = ((wr * tmpr) - (wi * tmpi));
     i5_5 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[21 * l1]).re);
     wi = ((W[21 * l1]).im);
     tmpr = ((jp[21 * m]).re);
     tmpi = ((jp[21 * m]).im);
     r5_21 = ((wr * tmpr) - (wi * tmpi));
     i5_21 = ((wi * tmpr) + (wr * tmpi));
     r4_5 = (r5_5 + r5_21);
     i4_5 = (i5_5 + i5_21);
     r4_21 = (r5_5 - r5_21);
     i4_21 = (i5_5 - i5_21);
       }
       {
     REAL r5_13, i5_13;
     REAL r5_29, i5_29;
     wr = ((W[13 * l1]).re);
     wi = ((W[13 * l1]).im);
     tmpr = ((jp[13 * m]).re);
     tmpi = ((jp[13 * m]).im);
     r5_13 = ((wr * tmpr) - (wi * tmpi));
     i5_13 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[29 * l1]).re);
     wi = ((W[29 * l1]).im);
     tmpr = ((jp[29 * m]).re);
     tmpi = ((jp[29 * m]).im);
     r5_29 = ((wr * tmpr) - (wi * tmpi));
     i5_29 = ((wi * tmpr) + (wr * tmpi));
     r4_13 = (r5_13 + r5_29);
     i4_13 = (i5_13 + i5_29);
     r4_29 = (r5_13 - r5_29);
     i4_29 = (i5_13 - i5_29);
       }
       r3_5 = (r4_5 + r4_13);
       i3_5 = (i4_5 + i4_13);
       r3_21 = (r4_5 - r4_13);
       i3_21 = (i4_5 - i4_13);
       r3_13 = (r4_21 + i4_29);
       i3_13 = (i4_21 - r4_29);
       r3_29 = (r4_21 - i4_29);
       i3_29 = (i4_21 + r4_29);
         }
         r2_1 = (r3_1 + r3_5);
         i2_1 = (i3_1 + i3_5);
         r2_17 = (r3_1 - r3_5);
         i2_17 = (i3_1 - i3_5);
         tmpr = (0.707106781187 * (r3_13 + i3_13));
         tmpi = (0.707106781187 * (i3_13 - r3_13));
         r2_5 = (r3_9 + tmpr);
         i2_5 = (i3_9 + tmpi);
         r2_21 = (r3_9 - tmpr);
         i2_21 = (i3_9 - tmpi);
         r2_9 = (r3_17 + i3_21);
         i2_9 = (i3_17 - r3_21);
         r2_25 = (r3_17 - i3_21);
         i2_25 = (i3_17 + r3_21);
         tmpr = (0.707106781187 * (i3_29 - r3_29));
         tmpi = (0.707106781187 * (r3_29 + i3_29));
         r2_13 = (r3_25 + tmpr);
         i2_13 = (i3_25 - tmpi);
         r2_29 = (r3_25 - tmpr);
         i2_29 = (i3_25 + tmpi);
    }
    {
         REAL r3_3, i3_3;
         REAL r3_7, i3_7;
         REAL r3_11, i3_11;
         REAL r3_15, i3_15;
         REAL r3_19, i3_19;
         REAL r3_23, i3_23;
         REAL r3_27, i3_27;
         REAL r3_31, i3_31;
         {
       REAL r4_3, i4_3;
       REAL r4_11, i4_11;
       REAL r4_19, i4_19;
       REAL r4_27, i4_27;
       {
     REAL r5_3, i5_3;
     REAL r5_19, i5_19;
     wr = ((W[3 * l1]).re);
     wi = ((W[3 * l1]).im);
     tmpr = ((jp[3 * m]).re);
     tmpi = ((jp[3 * m]).im);
     r5_3 = ((wr * tmpr) - (wi * tmpi));
     i5_3 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[19 * l1]).re);
     wi = ((W[19 * l1]).im);
     tmpr = ((jp[19 * m]).re);
     tmpi = ((jp[19 * m]).im);
     r5_19 = ((wr * tmpr) - (wi * tmpi));
     i5_19 = ((wi * tmpr) + (wr * tmpi));
     r4_3 = (r5_3 + r5_19);
     i4_3 = (i5_3 + i5_19);
     r4_19 = (r5_3 - r5_19);
     i4_19 = (i5_3 - i5_19);
       }
       {
     REAL r5_11, i5_11;
     REAL r5_27, i5_27;
     wr = ((W[11 * l1]).re);
     wi = ((W[11 * l1]).im);
     tmpr = ((jp[11 * m]).re);
     tmpi = ((jp[11 * m]).im);
     r5_11 = ((wr * tmpr) - (wi * tmpi));
     i5_11 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[27 * l1]).re);
     wi = ((W[27 * l1]).im);
     tmpr = ((jp[27 * m]).re);
     tmpi = ((jp[27 * m]).im);
     r5_27 = ((wr * tmpr) - (wi * tmpi));
     i5_27 = ((wi * tmpr) + (wr * tmpi));
     r4_11 = (r5_11 + r5_27);
     i4_11 = (i5_11 + i5_27);
     r4_27 = (r5_11 - r5_27);
     i4_27 = (i5_11 - i5_27);
       }
       r3_3 = (r4_3 + r4_11);
       i3_3 = (i4_3 + i4_11);
       r3_19 = (r4_3 - r4_11);
       i3_19 = (i4_3 - i4_11);
       r3_11 = (r4_19 + i4_27);
       i3_11 = (i4_19 - r4_27);
       r3_27 = (r4_19 - i4_27);
       i3_27 = (i4_19 + r4_27);
         }
         {
       REAL r4_7, i4_7;
       REAL r4_15, i4_15;
       REAL r4_23, i4_23;
       REAL r4_31, i4_31;
       {
     REAL r5_7, i5_7;
     REAL r5_23, i5_23;
     wr = ((W[7 * l1]).re);
     wi = ((W[7 * l1]).im);
     tmpr = ((jp[7 * m]).re);
     tmpi = ((jp[7 * m]).im);
     r5_7 = ((wr * tmpr) - (wi * tmpi));
     i5_7 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[23 * l1]).re);
     wi = ((W[23 * l1]).im);
     tmpr = ((jp[23 * m]).re);
     tmpi = ((jp[23 * m]).im);
     r5_23 = ((wr * tmpr) - (wi * tmpi));
     i5_23 = ((wi * tmpr) + (wr * tmpi));
     r4_7 = (r5_7 + r5_23);
     i4_7 = (i5_7 + i5_23);
     r4_23 = (r5_7 - r5_23);
     i4_23 = (i5_7 - i5_23);
       }
       {
     REAL r5_15, i5_15;
     REAL r5_31, i5_31;
     wr = ((W[15 * l1]).re);
     wi = ((W[15 * l1]).im);
     tmpr = ((jp[15 * m]).re);
     tmpi = ((jp[15 * m]).im);
     r5_15 = ((wr * tmpr) - (wi * tmpi));
     i5_15 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[31 * l1]).re);
     wi = ((W[31 * l1]).im);
     tmpr = ((jp[31 * m]).re);
     tmpi = ((jp[31 * m]).im);
     r5_31 = ((wr * tmpr) - (wi * tmpi));
     i5_31 = ((wi * tmpr) + (wr * tmpi));
     r4_15 = (r5_15 + r5_31);
     i4_15 = (i5_15 + i5_31);
     r4_31 = (r5_15 - r5_31);
     i4_31 = (i5_15 - i5_31);
       }
       r3_7 = (r4_7 + r4_15);
       i3_7 = (i4_7 + i4_15);
       r3_23 = (r4_7 - r4_15);
       i3_23 = (i4_7 - i4_15);
       r3_15 = (r4_23 + i4_31);
       i3_15 = (i4_23 - r4_31);
       r3_31 = (r4_23 - i4_31);
       i3_31 = (i4_23 + r4_31);
         }
         r2_3 = (r3_3 + r3_7);
         i2_3 = (i3_3 + i3_7);
         r2_19 = (r3_3 - r3_7);
         i2_19 = (i3_3 - i3_7);
         tmpr = (0.707106781187 * (r3_15 + i3_15));
         tmpi = (0.707106781187 * (i3_15 - r3_15));
         r2_7 = (r3_11 + tmpr);
         i2_7 = (i3_11 + tmpi);
         r2_23 = (r3_11 - tmpr);
         i2_23 = (i3_11 - tmpi);
         r2_11 = (r3_19 + i3_23);
         i2_11 = (i3_19 - r3_23);
         r2_27 = (r3_19 - i3_23);
         i2_27 = (i3_19 + r3_23);
         tmpr = (0.707106781187 * (i3_31 - r3_31));
         tmpi = (0.707106781187 * (r3_31 + i3_31));
         r2_15 = (r3_27 + tmpr);
         i2_15 = (i3_27 - tmpi);
         r2_31 = (r3_27 - tmpr);
         i2_31 = (i3_27 + tmpi);
    }
    r1_1 = (r2_1 + r2_3);
    i1_1 = (i2_1 + i2_3);
    r1_17 = (r2_1 - r2_3);
    i1_17 = (i2_1 - i2_3);
    tmpr = ((0.923879532511 * r2_7) + (0.382683432365 * i2_7));
    tmpi = ((0.923879532511 * i2_7) - (0.382683432365 * r2_7));
    r1_3 = (r2_5 + tmpr);
    i1_3 = (i2_5 + tmpi);
    r1_19 = (r2_5 - tmpr);
    i1_19 = (i2_5 - tmpi);
    tmpr = (0.707106781187 * (r2_11 + i2_11));
    tmpi = (0.707106781187 * (i2_11 - r2_11));
    r1_5 = (r2_9 + tmpr);
    i1_5 = (i2_9 + tmpi);
    r1_21 = (r2_9 - tmpr);
    i1_21 = (i2_9 - tmpi);
    tmpr = ((0.382683432365 * r2_15) + (0.923879532511 * i2_15));
    tmpi = ((0.382683432365 * i2_15) - (0.923879532511 * r2_15));
    r1_7 = (r2_13 + tmpr);
    i1_7 = (i2_13 + tmpi);
    r1_23 = (r2_13 - tmpr);
    i1_23 = (i2_13 - tmpi);
    r1_9 = (r2_17 + i2_19);
    i1_9 = (i2_17 - r2_19);
    r1_25 = (r2_17 - i2_19);
    i1_25 = (i2_17 + r2_19);
    tmpr = ((0.923879532511 * i2_23) - (0.382683432365 * r2_23));
    tmpi = ((0.923879532511 * r2_23) + (0.382683432365 * i2_23));
    r1_11 = (r2_21 + tmpr);
    i1_11 = (i2_21 - tmpi);
    r1_27 = (r2_21 - tmpr);
    i1_27 = (i2_21 + tmpi);
    tmpr = (0.707106781187 * (i2_27 - r2_27));
    tmpi = (0.707106781187 * (r2_27 + i2_27));
    r1_13 = (r2_25 + tmpr);
    i1_13 = (i2_25 - tmpi);
    r1_29 = (r2_25 - tmpr);
    i1_29 = (i2_25 + tmpi);
    tmpr = ((0.382683432365 * i2_31) - (0.923879532511 * r2_31));
    tmpi = ((0.382683432365 * r2_31) + (0.923879532511 * i2_31));
    r1_15 = (r2_29 + tmpr);
    i1_15 = (i2_29 - tmpi);
    r1_31 = (r2_29 - tmpr);
    i1_31 = (i2_29 + tmpi);
      }
      ((kp[0 * m]).re) = (r1_0 + r1_1);
      ((kp[0 * m]).im) = (i1_0 + i1_1);
      ((kp[16 * m]).re) = (r1_0 - r1_1);
      ((kp[16 * m]).im) = (i1_0 - i1_1);
      tmpr = ((0.980785280403 * r1_3) + (0.195090322016 * i1_3));
      tmpi = ((0.980785280403 * i1_3) - (0.195090322016 * r1_3));
      ((kp[1 * m]).re) = (r1_2 + tmpr);
      ((kp[1 * m]).im) = (i1_2 + tmpi);
      ((kp[17 * m]).re) = (r1_2 - tmpr);
      ((kp[17 * m]).im) = (i1_2 - tmpi);
      tmpr = ((0.923879532511 * r1_5) + (0.382683432365 * i1_5));
      tmpi = ((0.923879532511 * i1_5) - (0.382683432365 * r1_5));
      ((kp[2 * m]).re) = (r1_4 + tmpr);
      ((kp[2 * m]).im) = (i1_4 + tmpi);
      ((kp[18 * m]).re) = (r1_4 - tmpr);
      ((kp[18 * m]).im) = (i1_4 - tmpi);
      tmpr = ((0.831469612303 * r1_7) + (0.55557023302 * i1_7));
      tmpi = ((0.831469612303 * i1_7) - (0.55557023302 * r1_7));
      ((kp[3 * m]).re) = (r1_6 + tmpr);
      ((kp[3 * m]).im) = (i1_6 + tmpi);
      ((kp[19 * m]).re) = (r1_6 - tmpr);
      ((kp[19 * m]).im) = (i1_6 - tmpi);
      tmpr = (0.707106781187 * (r1_9 + i1_9));
      tmpi = (0.707106781187 * (i1_9 - r1_9));
      ((kp[4 * m]).re) = (r1_8 + tmpr);
      ((kp[4 * m]).im) = (i1_8 + tmpi);
      ((kp[20 * m]).re) = (r1_8 - tmpr);
      ((kp[20 * m]).im) = (i1_8 - tmpi);
      tmpr = ((0.55557023302 * r1_11) + (0.831469612303 * i1_11));
      tmpi = ((0.55557023302 * i1_11) - (0.831469612303 * r1_11));
      ((kp[5 * m]).re) = (r1_10 + tmpr);
      ((kp[5 * m]).im) = (i1_10 + tmpi);
      ((kp[21 * m]).re) = (r1_10 - tmpr);
      ((kp[21 * m]).im) = (i1_10 - tmpi);
      tmpr = ((0.382683432365 * r1_13) + (0.923879532511 * i1_13));
      tmpi = ((0.382683432365 * i1_13) - (0.923879532511 * r1_13));
      ((kp[6 * m]).re) = (r1_12 + tmpr);
      ((kp[6 * m]).im) = (i1_12 + tmpi);
      ((kp[22 * m]).re) = (r1_12 - tmpr);
      ((kp[22 * m]).im) = (i1_12 - tmpi);
      tmpr = ((0.195090322016 * r1_15) + (0.980785280403 * i1_15));
      tmpi = ((0.195090322016 * i1_15) - (0.980785280403 * r1_15));
      ((kp[7 * m]).re) = (r1_14 + tmpr);
      ((kp[7 * m]).im) = (i1_14 + tmpi);
      ((kp[23 * m]).re) = (r1_14 - tmpr);
      ((kp[23 * m]).im) = (i1_14 - tmpi);
      ((kp[8 * m]).re) = (r1_16 + i1_17);
      ((kp[8 * m]).im) = (i1_16 - r1_17);
      ((kp[24 * m]).re) = (r1_16 - i1_17);
      ((kp[24 * m]).im) = (i1_16 + r1_17);
      tmpr = ((0.980785280403 * i1_19) - (0.195090322016 * r1_19));
      tmpi = ((0.980785280403 * r1_19) + (0.195090322016 * i1_19));
      ((kp[9 * m]).re) = (r1_18 + tmpr);
      ((kp[9 * m]).im) = (i1_18 - tmpi);
      ((kp[25 * m]).re) = (r1_18 - tmpr);
      ((kp[25 * m]).im) = (i1_18 + tmpi);
      tmpr = ((0.923879532511 * i1_21) - (0.382683432365 * r1_21));
      tmpi = ((0.923879532511 * r1_21) + (0.382683432365 * i1_21));
      ((kp[10 * m]).re) = (r1_20 + tmpr);
      ((kp[10 * m]).im) = (i1_20 - tmpi);
      ((kp[26 * m]).re) = (r1_20 - tmpr);
      ((kp[26 * m]).im) = (i1_20 + tmpi);
      tmpr = ((0.831469612303 * i1_23) - (0.55557023302 * r1_23));
      tmpi = ((0.831469612303 * r1_23) + (0.55557023302 * i1_23));
      ((kp[11 * m]).re) = (r1_22 + tmpr);
      ((kp[11 * m]).im) = (i1_22 - tmpi);
      ((kp[27 * m]).re) = (r1_22 - tmpr);
      ((kp[27 * m]).im) = (i1_22 + tmpi);
      tmpr = (0.707106781187 * (i1_25 - r1_25));
      tmpi = (0.707106781187 * (r1_25 + i1_25));
      ((kp[12 * m]).re) = (r1_24 + tmpr);
      ((kp[12 * m]).im) = (i1_24 - tmpi);
      ((kp[28 * m]).re) = (r1_24 - tmpr);
      ((kp[28 * m]).im) = (i1_24 + tmpi);
      tmpr = ((0.55557023302 * i1_27) - (0.831469612303 * r1_27));
      tmpi = ((0.55557023302 * r1_27) + (0.831469612303 * i1_27));
      ((kp[13 * m]).re) = (r1_26 + tmpr);
      ((kp[13 * m]).im) = (i1_26 - tmpi);
      ((kp[29 * m]).re) = (r1_26 - tmpr);
      ((kp[29 * m]).im) = (i1_26 + tmpi);
      tmpr = ((0.382683432365 * i1_29) - (0.923879532511 * r1_29));
      tmpi = ((0.382683432365 * r1_29) + (0.923879532511 * i1_29));
      ((kp[14 * m]).re) = (r1_28 + tmpr);
      ((kp[14 * m]).im) = (i1_28 - tmpi);
      ((kp[30 * m]).re) = (r1_28 - tmpr);
      ((kp[30 * m]).im) = (i1_28 + tmpi);
      tmpr = ((0.195090322016 * i1_31) - (0.980785280403 * r1_31));
      tmpi = ((0.195090322016 * r1_31) + (0.980785280403 * i1_31));
      ((kp[15 * m]).re) = (r1_30 + tmpr);
      ((kp[15 * m]).im) = (i1_30 - tmpi);
      ((kp[31 * m]).re) = (r1_30 - tmpr);
      ((kp[31 * m]).im) = (i1_30 + tmpi);
        }
   }
     } else {
   int ab = (a + b) / 2;
                  
#pragma omp task untied
   fft_twiddle_32(a, ab, in, out, W, nW, nWdn, m);
                  
#pragma omp task untied
   fft_twiddle_32(ab, b, in, out, W, nW, nWdn, m);
                  
#pragma omp taskwait
     }
}
void fft_twiddle_32_seq(int a, int b, COMPLEX * in, COMPLEX * out, COMPLEX * W, int nW, int nWdn, int m)
{
     int l1, i;
     COMPLEX *jp, *kp;
     REAL tmpr, tmpi, wr, wi;
     if ((b - a) < 128) {
   for (i = a, l1 = nWdn * i, kp = out + i; i < b;
        i++, l1 += nWdn, kp++) {
        jp = in + i;
        {
      REAL r1_0, i1_0;
      REAL r1_1, i1_1;
      REAL r1_2, i1_2;
      REAL r1_3, i1_3;
      REAL r1_4, i1_4;
      REAL r1_5, i1_5;
      REAL r1_6, i1_6;
      REAL r1_7, i1_7;
      REAL r1_8, i1_8;
      REAL r1_9, i1_9;
      REAL r1_10, i1_10;
      REAL r1_11, i1_11;
      REAL r1_12, i1_12;
      REAL r1_13, i1_13;
      REAL r1_14, i1_14;
      REAL r1_15, i1_15;
      REAL r1_16, i1_16;
      REAL r1_17, i1_17;
      REAL r1_18, i1_18;
      REAL r1_19, i1_19;
      REAL r1_20, i1_20;
      REAL r1_21, i1_21;
      REAL r1_22, i1_22;
      REAL r1_23, i1_23;
      REAL r1_24, i1_24;
      REAL r1_25, i1_25;
      REAL r1_26, i1_26;
      REAL r1_27, i1_27;
      REAL r1_28, i1_28;
      REAL r1_29, i1_29;
      REAL r1_30, i1_30;
      REAL r1_31, i1_31;
      {
    REAL r2_0, i2_0;
    REAL r2_2, i2_2;
    REAL r2_4, i2_4;
    REAL r2_6, i2_6;
    REAL r2_8, i2_8;
    REAL r2_10, i2_10;
    REAL r2_12, i2_12;
    REAL r2_14, i2_14;
    REAL r2_16, i2_16;
    REAL r2_18, i2_18;
    REAL r2_20, i2_20;
    REAL r2_22, i2_22;
    REAL r2_24, i2_24;
    REAL r2_26, i2_26;
    REAL r2_28, i2_28;
    REAL r2_30, i2_30;
    {
         REAL r3_0, i3_0;
         REAL r3_4, i3_4;
         REAL r3_8, i3_8;
         REAL r3_12, i3_12;
         REAL r3_16, i3_16;
         REAL r3_20, i3_20;
         REAL r3_24, i3_24;
         REAL r3_28, i3_28;
         {
       REAL r4_0, i4_0;
       REAL r4_8, i4_8;
       REAL r4_16, i4_16;
       REAL r4_24, i4_24;
       {
     REAL r5_0, i5_0;
     REAL r5_16, i5_16;
     r5_0 = ((jp[0 * m]).re);
     i5_0 = ((jp[0 * m]).im);
     wr = ((W[16 * l1]).re);
     wi = ((W[16 * l1]).im);
     tmpr = ((jp[16 * m]).re);
     tmpi = ((jp[16 * m]).im);
     r5_16 = ((wr * tmpr) - (wi * tmpi));
     i5_16 = ((wi * tmpr) + (wr * tmpi));
     r4_0 = (r5_0 + r5_16);
     i4_0 = (i5_0 + i5_16);
     r4_16 = (r5_0 - r5_16);
     i4_16 = (i5_0 - i5_16);
       }
       {
     REAL r5_8, i5_8;
     REAL r5_24, i5_24;
     wr = ((W[8 * l1]).re);
     wi = ((W[8 * l1]).im);
     tmpr = ((jp[8 * m]).re);
     tmpi = ((jp[8 * m]).im);
     r5_8 = ((wr * tmpr) - (wi * tmpi));
     i5_8 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[24 * l1]).re);
     wi = ((W[24 * l1]).im);
     tmpr = ((jp[24 * m]).re);
     tmpi = ((jp[24 * m]).im);
     r5_24 = ((wr * tmpr) - (wi * tmpi));
     i5_24 = ((wi * tmpr) + (wr * tmpi));
     r4_8 = (r5_8 + r5_24);
     i4_8 = (i5_8 + i5_24);
     r4_24 = (r5_8 - r5_24);
     i4_24 = (i5_8 - i5_24);
       }
       r3_0 = (r4_0 + r4_8);
       i3_0 = (i4_0 + i4_8);
       r3_16 = (r4_0 - r4_8);
       i3_16 = (i4_0 - i4_8);
       r3_8 = (r4_16 + i4_24);
       i3_8 = (i4_16 - r4_24);
       r3_24 = (r4_16 - i4_24);
       i3_24 = (i4_16 + r4_24);
         }
         {
       REAL r4_4, i4_4;
       REAL r4_12, i4_12;
       REAL r4_20, i4_20;
       REAL r4_28, i4_28;
       {
     REAL r5_4, i5_4;
     REAL r5_20, i5_20;
     wr = ((W[4 * l1]).re);
     wi = ((W[4 * l1]).im);
     tmpr = ((jp[4 * m]).re);
     tmpi = ((jp[4 * m]).im);
     r5_4 = ((wr * tmpr) - (wi * tmpi));
     i5_4 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[20 * l1]).re);
     wi = ((W[20 * l1]).im);
     tmpr = ((jp[20 * m]).re);
     tmpi = ((jp[20 * m]).im);
     r5_20 = ((wr * tmpr) - (wi * tmpi));
     i5_20 = ((wi * tmpr) + (wr * tmpi));
     r4_4 = (r5_4 + r5_20);
     i4_4 = (i5_4 + i5_20);
     r4_20 = (r5_4 - r5_20);
     i4_20 = (i5_4 - i5_20);
       }
       {
     REAL r5_12, i5_12;
     REAL r5_28, i5_28;
     wr = ((W[12 * l1]).re);
     wi = ((W[12 * l1]).im);
     tmpr = ((jp[12 * m]).re);
     tmpi = ((jp[12 * m]).im);
     r5_12 = ((wr * tmpr) - (wi * tmpi));
     i5_12 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[28 * l1]).re);
     wi = ((W[28 * l1]).im);
     tmpr = ((jp[28 * m]).re);
     tmpi = ((jp[28 * m]).im);
     r5_28 = ((wr * tmpr) - (wi * tmpi));
     i5_28 = ((wi * tmpr) + (wr * tmpi));
     r4_12 = (r5_12 + r5_28);
     i4_12 = (i5_12 + i5_28);
     r4_28 = (r5_12 - r5_28);
     i4_28 = (i5_12 - i5_28);
       }
       r3_4 = (r4_4 + r4_12);
       i3_4 = (i4_4 + i4_12);
       r3_20 = (r4_4 - r4_12);
       i3_20 = (i4_4 - i4_12);
       r3_12 = (r4_20 + i4_28);
       i3_12 = (i4_20 - r4_28);
       r3_28 = (r4_20 - i4_28);
       i3_28 = (i4_20 + r4_28);
         }
         r2_0 = (r3_0 + r3_4);
         i2_0 = (i3_0 + i3_4);
         r2_16 = (r3_0 - r3_4);
         i2_16 = (i3_0 - i3_4);
         tmpr = (0.707106781187 * (r3_12 + i3_12));
         tmpi = (0.707106781187 * (i3_12 - r3_12));
         r2_4 = (r3_8 + tmpr);
         i2_4 = (i3_8 + tmpi);
         r2_20 = (r3_8 - tmpr);
         i2_20 = (i3_8 - tmpi);
         r2_8 = (r3_16 + i3_20);
         i2_8 = (i3_16 - r3_20);
         r2_24 = (r3_16 - i3_20);
         i2_24 = (i3_16 + r3_20);
         tmpr = (0.707106781187 * (i3_28 - r3_28));
         tmpi = (0.707106781187 * (r3_28 + i3_28));
         r2_12 = (r3_24 + tmpr);
         i2_12 = (i3_24 - tmpi);
         r2_28 = (r3_24 - tmpr);
         i2_28 = (i3_24 + tmpi);
    }
    {
         REAL r3_2, i3_2;
         REAL r3_6, i3_6;
         REAL r3_10, i3_10;
         REAL r3_14, i3_14;
         REAL r3_18, i3_18;
         REAL r3_22, i3_22;
         REAL r3_26, i3_26;
         REAL r3_30, i3_30;
         {
       REAL r4_2, i4_2;
       REAL r4_10, i4_10;
       REAL r4_18, i4_18;
       REAL r4_26, i4_26;
       {
     REAL r5_2, i5_2;
     REAL r5_18, i5_18;
     wr = ((W[2 * l1]).re);
     wi = ((W[2 * l1]).im);
     tmpr = ((jp[2 * m]).re);
     tmpi = ((jp[2 * m]).im);
     r5_2 = ((wr * tmpr) - (wi * tmpi));
     i5_2 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[18 * l1]).re);
     wi = ((W[18 * l1]).im);
     tmpr = ((jp[18 * m]).re);
     tmpi = ((jp[18 * m]).im);
     r5_18 = ((wr * tmpr) - (wi * tmpi));
     i5_18 = ((wi * tmpr) + (wr * tmpi));
     r4_2 = (r5_2 + r5_18);
     i4_2 = (i5_2 + i5_18);
     r4_18 = (r5_2 - r5_18);
     i4_18 = (i5_2 - i5_18);
       }
       {
     REAL r5_10, i5_10;
     REAL r5_26, i5_26;
     wr = ((W[10 * l1]).re);
     wi = ((W[10 * l1]).im);
     tmpr = ((jp[10 * m]).re);
     tmpi = ((jp[10 * m]).im);
     r5_10 = ((wr * tmpr) - (wi * tmpi));
     i5_10 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[26 * l1]).re);
     wi = ((W[26 * l1]).im);
     tmpr = ((jp[26 * m]).re);
     tmpi = ((jp[26 * m]).im);
     r5_26 = ((wr * tmpr) - (wi * tmpi));
     i5_26 = ((wi * tmpr) + (wr * tmpi));
     r4_10 = (r5_10 + r5_26);
     i4_10 = (i5_10 + i5_26);
     r4_26 = (r5_10 - r5_26);
     i4_26 = (i5_10 - i5_26);
       }
       r3_2 = (r4_2 + r4_10);
       i3_2 = (i4_2 + i4_10);
       r3_18 = (r4_2 - r4_10);
       i3_18 = (i4_2 - i4_10);
       r3_10 = (r4_18 + i4_26);
       i3_10 = (i4_18 - r4_26);
       r3_26 = (r4_18 - i4_26);
       i3_26 = (i4_18 + r4_26);
         }
         {
       REAL r4_6, i4_6;
       REAL r4_14, i4_14;
       REAL r4_22, i4_22;
       REAL r4_30, i4_30;
       {
     REAL r5_6, i5_6;
     REAL r5_22, i5_22;
     wr = ((W[6 * l1]).re);
     wi = ((W[6 * l1]).im);
     tmpr = ((jp[6 * m]).re);
     tmpi = ((jp[6 * m]).im);
     r5_6 = ((wr * tmpr) - (wi * tmpi));
     i5_6 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[22 * l1]).re);
     wi = ((W[22 * l1]).im);
     tmpr = ((jp[22 * m]).re);
     tmpi = ((jp[22 * m]).im);
     r5_22 = ((wr * tmpr) - (wi * tmpi));
     i5_22 = ((wi * tmpr) + (wr * tmpi));
     r4_6 = (r5_6 + r5_22);
     i4_6 = (i5_6 + i5_22);
     r4_22 = (r5_6 - r5_22);
     i4_22 = (i5_6 - i5_22);
       }
       {
     REAL r5_14, i5_14;
     REAL r5_30, i5_30;
     wr = ((W[14 * l1]).re);
     wi = ((W[14 * l1]).im);
     tmpr = ((jp[14 * m]).re);
     tmpi = ((jp[14 * m]).im);
     r5_14 = ((wr * tmpr) - (wi * tmpi));
     i5_14 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[30 * l1]).re);
     wi = ((W[30 * l1]).im);
     tmpr = ((jp[30 * m]).re);
     tmpi = ((jp[30 * m]).im);
     r5_30 = ((wr * tmpr) - (wi * tmpi));
     i5_30 = ((wi * tmpr) + (wr * tmpi));
     r4_14 = (r5_14 + r5_30);
     i4_14 = (i5_14 + i5_30);
     r4_30 = (r5_14 - r5_30);
     i4_30 = (i5_14 - i5_30);
       }
       r3_6 = (r4_6 + r4_14);
       i3_6 = (i4_6 + i4_14);
       r3_22 = (r4_6 - r4_14);
       i3_22 = (i4_6 - i4_14);
       r3_14 = (r4_22 + i4_30);
       i3_14 = (i4_22 - r4_30);
       r3_30 = (r4_22 - i4_30);
       i3_30 = (i4_22 + r4_30);
         }
         r2_2 = (r3_2 + r3_6);
         i2_2 = (i3_2 + i3_6);
         r2_18 = (r3_2 - r3_6);
         i2_18 = (i3_2 - i3_6);
         tmpr = (0.707106781187 * (r3_14 + i3_14));
         tmpi = (0.707106781187 * (i3_14 - r3_14));
         r2_6 = (r3_10 + tmpr);
         i2_6 = (i3_10 + tmpi);
         r2_22 = (r3_10 - tmpr);
         i2_22 = (i3_10 - tmpi);
         r2_10 = (r3_18 + i3_22);
         i2_10 = (i3_18 - r3_22);
         r2_26 = (r3_18 - i3_22);
         i2_26 = (i3_18 + r3_22);
         tmpr = (0.707106781187 * (i3_30 - r3_30));
         tmpi = (0.707106781187 * (r3_30 + i3_30));
         r2_14 = (r3_26 + tmpr);
         i2_14 = (i3_26 - tmpi);
         r2_30 = (r3_26 - tmpr);
         i2_30 = (i3_26 + tmpi);
    }
    r1_0 = (r2_0 + r2_2);
    i1_0 = (i2_0 + i2_2);
    r1_16 = (r2_0 - r2_2);
    i1_16 = (i2_0 - i2_2);
    tmpr = ((0.923879532511 * r2_6) + (0.382683432365 * i2_6));
    tmpi = ((0.923879532511 * i2_6) - (0.382683432365 * r2_6));
    r1_2 = (r2_4 + tmpr);
    i1_2 = (i2_4 + tmpi);
    r1_18 = (r2_4 - tmpr);
    i1_18 = (i2_4 - tmpi);
    tmpr = (0.707106781187 * (r2_10 + i2_10));
    tmpi = (0.707106781187 * (i2_10 - r2_10));
    r1_4 = (r2_8 + tmpr);
    i1_4 = (i2_8 + tmpi);
    r1_20 = (r2_8 - tmpr);
    i1_20 = (i2_8 - tmpi);
    tmpr = ((0.382683432365 * r2_14) + (0.923879532511 * i2_14));
    tmpi = ((0.382683432365 * i2_14) - (0.923879532511 * r2_14));
    r1_6 = (r2_12 + tmpr);
    i1_6 = (i2_12 + tmpi);
    r1_22 = (r2_12 - tmpr);
    i1_22 = (i2_12 - tmpi);
    r1_8 = (r2_16 + i2_18);
    i1_8 = (i2_16 - r2_18);
    r1_24 = (r2_16 - i2_18);
    i1_24 = (i2_16 + r2_18);
    tmpr = ((0.923879532511 * i2_22) - (0.382683432365 * r2_22));
    tmpi = ((0.923879532511 * r2_22) + (0.382683432365 * i2_22));
    r1_10 = (r2_20 + tmpr);
    i1_10 = (i2_20 - tmpi);
    r1_26 = (r2_20 - tmpr);
    i1_26 = (i2_20 + tmpi);
    tmpr = (0.707106781187 * (i2_26 - r2_26));
    tmpi = (0.707106781187 * (r2_26 + i2_26));
    r1_12 = (r2_24 + tmpr);
    i1_12 = (i2_24 - tmpi);
    r1_28 = (r2_24 - tmpr);
    i1_28 = (i2_24 + tmpi);
    tmpr = ((0.382683432365 * i2_30) - (0.923879532511 * r2_30));
    tmpi = ((0.382683432365 * r2_30) + (0.923879532511 * i2_30));
    r1_14 = (r2_28 + tmpr);
    i1_14 = (i2_28 - tmpi);
    r1_30 = (r2_28 - tmpr);
    i1_30 = (i2_28 + tmpi);
      }
      {
    REAL r2_1, i2_1;
    REAL r2_3, i2_3;
    REAL r2_5, i2_5;
    REAL r2_7, i2_7;
    REAL r2_9, i2_9;
    REAL r2_11, i2_11;
    REAL r2_13, i2_13;
    REAL r2_15, i2_15;
    REAL r2_17, i2_17;
    REAL r2_19, i2_19;
    REAL r2_21, i2_21;
    REAL r2_23, i2_23;
    REAL r2_25, i2_25;
    REAL r2_27, i2_27;
    REAL r2_29, i2_29;
    REAL r2_31, i2_31;
    {
         REAL r3_1, i3_1;
         REAL r3_5, i3_5;
         REAL r3_9, i3_9;
         REAL r3_13, i3_13;
         REAL r3_17, i3_17;
         REAL r3_21, i3_21;
         REAL r3_25, i3_25;
         REAL r3_29, i3_29;
         {
       REAL r4_1, i4_1;
       REAL r4_9, i4_9;
       REAL r4_17, i4_17;
       REAL r4_25, i4_25;
       {
     REAL r5_1, i5_1;
     REAL r5_17, i5_17;
     wr = ((W[1 * l1]).re);
     wi = ((W[1 * l1]).im);
     tmpr = ((jp[1 * m]).re);
     tmpi = ((jp[1 * m]).im);
     r5_1 = ((wr * tmpr) - (wi * tmpi));
     i5_1 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[17 * l1]).re);
     wi = ((W[17 * l1]).im);
     tmpr = ((jp[17 * m]).re);
     tmpi = ((jp[17 * m]).im);
     r5_17 = ((wr * tmpr) - (wi * tmpi));
     i5_17 = ((wi * tmpr) + (wr * tmpi));
     r4_1 = (r5_1 + r5_17);
     i4_1 = (i5_1 + i5_17);
     r4_17 = (r5_1 - r5_17);
     i4_17 = (i5_1 - i5_17);
       }
       {
     REAL r5_9, i5_9;
     REAL r5_25, i5_25;
     wr = ((W[9 * l1]).re);
     wi = ((W[9 * l1]).im);
     tmpr = ((jp[9 * m]).re);
     tmpi = ((jp[9 * m]).im);
     r5_9 = ((wr * tmpr) - (wi * tmpi));
     i5_9 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[25 * l1]).re);
     wi = ((W[25 * l1]).im);
     tmpr = ((jp[25 * m]).re);
     tmpi = ((jp[25 * m]).im);
     r5_25 = ((wr * tmpr) - (wi * tmpi));
     i5_25 = ((wi * tmpr) + (wr * tmpi));
     r4_9 = (r5_9 + r5_25);
     i4_9 = (i5_9 + i5_25);
     r4_25 = (r5_9 - r5_25);
     i4_25 = (i5_9 - i5_25);
       }
       r3_1 = (r4_1 + r4_9);
       i3_1 = (i4_1 + i4_9);
       r3_17 = (r4_1 - r4_9);
       i3_17 = (i4_1 - i4_9);
       r3_9 = (r4_17 + i4_25);
       i3_9 = (i4_17 - r4_25);
       r3_25 = (r4_17 - i4_25);
       i3_25 = (i4_17 + r4_25);
         }
         {
       REAL r4_5, i4_5;
       REAL r4_13, i4_13;
       REAL r4_21, i4_21;
       REAL r4_29, i4_29;
       {
     REAL r5_5, i5_5;
     REAL r5_21, i5_21;
     wr = ((W[5 * l1]).re);
     wi = ((W[5 * l1]).im);
     tmpr = ((jp[5 * m]).re);
     tmpi = ((jp[5 * m]).im);
     r5_5 = ((wr * tmpr) - (wi * tmpi));
     i5_5 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[21 * l1]).re);
     wi = ((W[21 * l1]).im);
     tmpr = ((jp[21 * m]).re);
     tmpi = ((jp[21 * m]).im);
     r5_21 = ((wr * tmpr) - (wi * tmpi));
     i5_21 = ((wi * tmpr) + (wr * tmpi));
     r4_5 = (r5_5 + r5_21);
     i4_5 = (i5_5 + i5_21);
     r4_21 = (r5_5 - r5_21);
     i4_21 = (i5_5 - i5_21);
       }
       {
     REAL r5_13, i5_13;
     REAL r5_29, i5_29;
     wr = ((W[13 * l1]).re);
     wi = ((W[13 * l1]).im);
     tmpr = ((jp[13 * m]).re);
     tmpi = ((jp[13 * m]).im);
     r5_13 = ((wr * tmpr) - (wi * tmpi));
     i5_13 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[29 * l1]).re);
     wi = ((W[29 * l1]).im);
     tmpr = ((jp[29 * m]).re);
     tmpi = ((jp[29 * m]).im);
     r5_29 = ((wr * tmpr) - (wi * tmpi));
     i5_29 = ((wi * tmpr) + (wr * tmpi));
     r4_13 = (r5_13 + r5_29);
     i4_13 = (i5_13 + i5_29);
     r4_29 = (r5_13 - r5_29);
     i4_29 = (i5_13 - i5_29);
       }
       r3_5 = (r4_5 + r4_13);
       i3_5 = (i4_5 + i4_13);
       r3_21 = (r4_5 - r4_13);
       i3_21 = (i4_5 - i4_13);
       r3_13 = (r4_21 + i4_29);
       i3_13 = (i4_21 - r4_29);
       r3_29 = (r4_21 - i4_29);
       i3_29 = (i4_21 + r4_29);
         }
         r2_1 = (r3_1 + r3_5);
         i2_1 = (i3_1 + i3_5);
         r2_17 = (r3_1 - r3_5);
         i2_17 = (i3_1 - i3_5);
         tmpr = (0.707106781187 * (r3_13 + i3_13));
         tmpi = (0.707106781187 * (i3_13 - r3_13));
         r2_5 = (r3_9 + tmpr);
         i2_5 = (i3_9 + tmpi);
         r2_21 = (r3_9 - tmpr);
         i2_21 = (i3_9 - tmpi);
         r2_9 = (r3_17 + i3_21);
         i2_9 = (i3_17 - r3_21);
         r2_25 = (r3_17 - i3_21);
         i2_25 = (i3_17 + r3_21);
         tmpr = (0.707106781187 * (i3_29 - r3_29));
         tmpi = (0.707106781187 * (r3_29 + i3_29));
         r2_13 = (r3_25 + tmpr);
         i2_13 = (i3_25 - tmpi);
         r2_29 = (r3_25 - tmpr);
         i2_29 = (i3_25 + tmpi);
    }
    {
         REAL r3_3, i3_3;
         REAL r3_7, i3_7;
         REAL r3_11, i3_11;
         REAL r3_15, i3_15;
         REAL r3_19, i3_19;
         REAL r3_23, i3_23;
         REAL r3_27, i3_27;
         REAL r3_31, i3_31;
         {
       REAL r4_3, i4_3;
       REAL r4_11, i4_11;
       REAL r4_19, i4_19;
       REAL r4_27, i4_27;
       {
     REAL r5_3, i5_3;
     REAL r5_19, i5_19;
     wr = ((W[3 * l1]).re);
     wi = ((W[3 * l1]).im);
     tmpr = ((jp[3 * m]).re);
     tmpi = ((jp[3 * m]).im);
     r5_3 = ((wr * tmpr) - (wi * tmpi));
     i5_3 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[19 * l1]).re);
     wi = ((W[19 * l1]).im);
     tmpr = ((jp[19 * m]).re);
     tmpi = ((jp[19 * m]).im);
     r5_19 = ((wr * tmpr) - (wi * tmpi));
     i5_19 = ((wi * tmpr) + (wr * tmpi));
     r4_3 = (r5_3 + r5_19);
     i4_3 = (i5_3 + i5_19);
     r4_19 = (r5_3 - r5_19);
     i4_19 = (i5_3 - i5_19);
       }
       {
     REAL r5_11, i5_11;
     REAL r5_27, i5_27;
     wr = ((W[11 * l1]).re);
     wi = ((W[11 * l1]).im);
     tmpr = ((jp[11 * m]).re);
     tmpi = ((jp[11 * m]).im);
     r5_11 = ((wr * tmpr) - (wi * tmpi));
     i5_11 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[27 * l1]).re);
     wi = ((W[27 * l1]).im);
     tmpr = ((jp[27 * m]).re);
     tmpi = ((jp[27 * m]).im);
     r5_27 = ((wr * tmpr) - (wi * tmpi));
     i5_27 = ((wi * tmpr) + (wr * tmpi));
     r4_11 = (r5_11 + r5_27);
     i4_11 = (i5_11 + i5_27);
     r4_27 = (r5_11 - r5_27);
     i4_27 = (i5_11 - i5_27);
       }
       r3_3 = (r4_3 + r4_11);
       i3_3 = (i4_3 + i4_11);
       r3_19 = (r4_3 - r4_11);
       i3_19 = (i4_3 - i4_11);
       r3_11 = (r4_19 + i4_27);
       i3_11 = (i4_19 - r4_27);
       r3_27 = (r4_19 - i4_27);
       i3_27 = (i4_19 + r4_27);
         }
         {
       REAL r4_7, i4_7;
       REAL r4_15, i4_15;
       REAL r4_23, i4_23;
       REAL r4_31, i4_31;
       {
     REAL r5_7, i5_7;
     REAL r5_23, i5_23;
     wr = ((W[7 * l1]).re);
     wi = ((W[7 * l1]).im);
     tmpr = ((jp[7 * m]).re);
     tmpi = ((jp[7 * m]).im);
     r5_7 = ((wr * tmpr) - (wi * tmpi));
     i5_7 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[23 * l1]).re);
     wi = ((W[23 * l1]).im);
     tmpr = ((jp[23 * m]).re);
     tmpi = ((jp[23 * m]).im);
     r5_23 = ((wr * tmpr) - (wi * tmpi));
     i5_23 = ((wi * tmpr) + (wr * tmpi));
     r4_7 = (r5_7 + r5_23);
     i4_7 = (i5_7 + i5_23);
     r4_23 = (r5_7 - r5_23);
     i4_23 = (i5_7 - i5_23);
       }
       {
     REAL r5_15, i5_15;
     REAL r5_31, i5_31;
     wr = ((W[15 * l1]).re);
     wi = ((W[15 * l1]).im);
     tmpr = ((jp[15 * m]).re);
     tmpi = ((jp[15 * m]).im);
     r5_15 = ((wr * tmpr) - (wi * tmpi));
     i5_15 = ((wi * tmpr) + (wr * tmpi));
     wr = ((W[31 * l1]).re);
     wi = ((W[31 * l1]).im);
     tmpr = ((jp[31 * m]).re);
     tmpi = ((jp[31 * m]).im);
     r5_31 = ((wr * tmpr) - (wi * tmpi));
     i5_31 = ((wi * tmpr) + (wr * tmpi));
     r4_15 = (r5_15 + r5_31);
     i4_15 = (i5_15 + i5_31);
     r4_31 = (r5_15 - r5_31);
     i4_31 = (i5_15 - i5_31);
       }
       r3_7 = (r4_7 + r4_15);
       i3_7 = (i4_7 + i4_15);
       r3_23 = (r4_7 - r4_15);
       i3_23 = (i4_7 - i4_15);
       r3_15 = (r4_23 + i4_31);
       i3_15 = (i4_23 - r4_31);
       r3_31 = (r4_23 - i4_31);
       i3_31 = (i4_23 + r4_31);
         }
         r2_3 = (r3_3 + r3_7);
         i2_3 = (i3_3 + i3_7);
         r2_19 = (r3_3 - r3_7);
         i2_19 = (i3_3 - i3_7);
         tmpr = (0.707106781187 * (r3_15 + i3_15));
         tmpi = (0.707106781187 * (i3_15 - r3_15));
         r2_7 = (r3_11 + tmpr);
         i2_7 = (i3_11 + tmpi);
         r2_23 = (r3_11 - tmpr);
         i2_23 = (i3_11 - tmpi);
         r2_11 = (r3_19 + i3_23);
         i2_11 = (i3_19 - r3_23);
         r2_27 = (r3_19 - i3_23);
         i2_27 = (i3_19 + r3_23);
         tmpr = (0.707106781187 * (i3_31 - r3_31));
         tmpi = (0.707106781187 * (r3_31 + i3_31));
         r2_15 = (r3_27 + tmpr);
         i2_15 = (i3_27 - tmpi);
         r2_31 = (r3_27 - tmpr);
         i2_31 = (i3_27 + tmpi);
    }
    r1_1 = (r2_1 + r2_3);
    i1_1 = (i2_1 + i2_3);
    r1_17 = (r2_1 - r2_3);
    i1_17 = (i2_1 - i2_3);
    tmpr = ((0.923879532511 * r2_7) + (0.382683432365 * i2_7));
    tmpi = ((0.923879532511 * i2_7) - (0.382683432365 * r2_7));
    r1_3 = (r2_5 + tmpr);
    i1_3 = (i2_5 + tmpi);
    r1_19 = (r2_5 - tmpr);
    i1_19 = (i2_5 - tmpi);
    tmpr = (0.707106781187 * (r2_11 + i2_11));
    tmpi = (0.707106781187 * (i2_11 - r2_11));
    r1_5 = (r2_9 + tmpr);
    i1_5 = (i2_9 + tmpi);
    r1_21 = (r2_9 - tmpr);
    i1_21 = (i2_9 - tmpi);
    tmpr = ((0.382683432365 * r2_15) + (0.923879532511 * i2_15));
    tmpi = ((0.382683432365 * i2_15) - (0.923879532511 * r2_15));
    r1_7 = (r2_13 + tmpr);
    i1_7 = (i2_13 + tmpi);
    r1_23 = (r2_13 - tmpr);
    i1_23 = (i2_13 - tmpi);
    r1_9 = (r2_17 + i2_19);
    i1_9 = (i2_17 - r2_19);
    r1_25 = (r2_17 - i2_19);
    i1_25 = (i2_17 + r2_19);
    tmpr = ((0.923879532511 * i2_23) - (0.382683432365 * r2_23));
    tmpi = ((0.923879532511 * r2_23) + (0.382683432365 * i2_23));
    r1_11 = (r2_21 + tmpr);
    i1_11 = (i2_21 - tmpi);
    r1_27 = (r2_21 - tmpr);
    i1_27 = (i2_21 + tmpi);
    tmpr = (0.707106781187 * (i2_27 - r2_27));
    tmpi = (0.707106781187 * (r2_27 + i2_27));
    r1_13 = (r2_25 + tmpr);
    i1_13 = (i2_25 - tmpi);
    r1_29 = (r2_25 - tmpr);
    i1_29 = (i2_25 + tmpi);
    tmpr = ((0.382683432365 * i2_31) - (0.923879532511 * r2_31));
    tmpi = ((0.382683432365 * r2_31) + (0.923879532511 * i2_31));
    r1_15 = (r2_29 + tmpr);
    i1_15 = (i2_29 - tmpi);
    r1_31 = (r2_29 - tmpr);
    i1_31 = (i2_29 + tmpi);
      }
      ((kp[0 * m]).re) = (r1_0 + r1_1);
      ((kp[0 * m]).im) = (i1_0 + i1_1);
      ((kp[16 * m]).re) = (r1_0 - r1_1);
      ((kp[16 * m]).im) = (i1_0 - i1_1);
      tmpr = ((0.980785280403 * r1_3) + (0.195090322016 * i1_3));
      tmpi = ((0.980785280403 * i1_3) - (0.195090322016 * r1_3));
      ((kp[1 * m]).re) = (r1_2 + tmpr);
      ((kp[1 * m]).im) = (i1_2 + tmpi);
      ((kp[17 * m]).re) = (r1_2 - tmpr);
      ((kp[17 * m]).im) = (i1_2 - tmpi);
      tmpr = ((0.923879532511 * r1_5) + (0.382683432365 * i1_5));
      tmpi = ((0.923879532511 * i1_5) - (0.382683432365 * r1_5));
      ((kp[2 * m]).re) = (r1_4 + tmpr);
      ((kp[2 * m]).im) = (i1_4 + tmpi);
      ((kp[18 * m]).re) = (r1_4 - tmpr);
      ((kp[18 * m]).im) = (i1_4 - tmpi);
      tmpr = ((0.831469612303 * r1_7) + (0.55557023302 * i1_7));
      tmpi = ((0.831469612303 * i1_7) - (0.55557023302 * r1_7));
      ((kp[3 * m]).re) = (r1_6 + tmpr);
      ((kp[3 * m]).im) = (i1_6 + tmpi);
      ((kp[19 * m]).re) = (r1_6 - tmpr);
      ((kp[19 * m]).im) = (i1_6 - tmpi);
      tmpr = (0.707106781187 * (r1_9 + i1_9));
      tmpi = (0.707106781187 * (i1_9 - r1_9));
      ((kp[4 * m]).re) = (r1_8 + tmpr);
      ((kp[4 * m]).im) = (i1_8 + tmpi);
      ((kp[20 * m]).re) = (r1_8 - tmpr);
      ((kp[20 * m]).im) = (i1_8 - tmpi);
      tmpr = ((0.55557023302 * r1_11) + (0.831469612303 * i1_11));
      tmpi = ((0.55557023302 * i1_11) - (0.831469612303 * r1_11));
      ((kp[5 * m]).re) = (r1_10 + tmpr);
      ((kp[5 * m]).im) = (i1_10 + tmpi);
      ((kp[21 * m]).re) = (r1_10 - tmpr);
      ((kp[21 * m]).im) = (i1_10 - tmpi);
      tmpr = ((0.382683432365 * r1_13) + (0.923879532511 * i1_13));
      tmpi = ((0.382683432365 * i1_13) - (0.923879532511 * r1_13));
      ((kp[6 * m]).re) = (r1_12 + tmpr);
      ((kp[6 * m]).im) = (i1_12 + tmpi);
      ((kp[22 * m]).re) = (r1_12 - tmpr);
      ((kp[22 * m]).im) = (i1_12 - tmpi);
      tmpr = ((0.195090322016 * r1_15) + (0.980785280403 * i1_15));
      tmpi = ((0.195090322016 * i1_15) - (0.980785280403 * r1_15));
      ((kp[7 * m]).re) = (r1_14 + tmpr);
      ((kp[7 * m]).im) = (i1_14 + tmpi);
      ((kp[23 * m]).re) = (r1_14 - tmpr);
      ((kp[23 * m]).im) = (i1_14 - tmpi);
      ((kp[8 * m]).re) = (r1_16 + i1_17);
      ((kp[8 * m]).im) = (i1_16 - r1_17);
      ((kp[24 * m]).re) = (r1_16 - i1_17);
      ((kp[24 * m]).im) = (i1_16 + r1_17);
      tmpr = ((0.980785280403 * i1_19) - (0.195090322016 * r1_19));
      tmpi = ((0.980785280403 * r1_19) + (0.195090322016 * i1_19));
      ((kp[9 * m]).re) = (r1_18 + tmpr);
      ((kp[9 * m]).im) = (i1_18 - tmpi);
      ((kp[25 * m]).re) = (r1_18 - tmpr);
      ((kp[25 * m]).im) = (i1_18 + tmpi);
      tmpr = ((0.923879532511 * i1_21) - (0.382683432365 * r1_21));
      tmpi = ((0.923879532511 * r1_21) + (0.382683432365 * i1_21));
      ((kp[10 * m]).re) = (r1_20 + tmpr);
      ((kp[10 * m]).im) = (i1_20 - tmpi);
      ((kp[26 * m]).re) = (r1_20 - tmpr);
      ((kp[26 * m]).im) = (i1_20 + tmpi);
      tmpr = ((0.831469612303 * i1_23) - (0.55557023302 * r1_23));
      tmpi = ((0.831469612303 * r1_23) + (0.55557023302 * i1_23));
      ((kp[11 * m]).re) = (r1_22 + tmpr);
      ((kp[11 * m]).im) = (i1_22 - tmpi);
      ((kp[27 * m]).re) = (r1_22 - tmpr);
      ((kp[27 * m]).im) = (i1_22 + tmpi);
      tmpr = (0.707106781187 * (i1_25 - r1_25));
      tmpi = (0.707106781187 * (r1_25 + i1_25));
      ((kp[12 * m]).re) = (r1_24 + tmpr);
      ((kp[12 * m]).im) = (i1_24 - tmpi);
      ((kp[28 * m]).re) = (r1_24 - tmpr);
      ((kp[28 * m]).im) = (i1_24 + tmpi);
      tmpr = ((0.55557023302 * i1_27) - (0.831469612303 * r1_27));
      tmpi = ((0.55557023302 * r1_27) + (0.831469612303 * i1_27));
      ((kp[13 * m]).re) = (r1_26 + tmpr);
      ((kp[13 * m]).im) = (i1_26 - tmpi);
      ((kp[29 * m]).re) = (r1_26 - tmpr);
      ((kp[29 * m]).im) = (i1_26 + tmpi);
      tmpr = ((0.382683432365 * i1_29) - (0.923879532511 * r1_29));
      tmpi = ((0.382683432365 * r1_29) + (0.923879532511 * i1_29));
      ((kp[14 * m]).re) = (r1_28 + tmpr);
      ((kp[14 * m]).im) = (i1_28 - tmpi);
      ((kp[30 * m]).re) = (r1_28 - tmpr);
      ((kp[30 * m]).im) = (i1_28 + tmpi);
      tmpr = ((0.195090322016 * i1_31) - (0.980785280403 * r1_31));
      tmpi = ((0.195090322016 * r1_31) + (0.980785280403 * i1_31));
      ((kp[15 * m]).re) = (r1_30 + tmpr);
      ((kp[15 * m]).im) = (i1_30 - tmpi);
      ((kp[31 * m]).re) = (r1_30 - tmpr);
      ((kp[31 * m]).im) = (i1_30 + tmpi);
        }
   }
     } else {
   int ab = (a + b) / 2;
   fft_twiddle_32_seq(a, ab, in, out, W, nW, nWdn, m);
   fft_twiddle_32_seq(ab, b, in, out, W, nW, nWdn, m);
     }
}
void fft_unshuffle_32(int a, int b, COMPLEX * in, COMPLEX * out, int m)
{
     int i;
     const COMPLEX *ip;
     COMPLEX *jp;
     if ((b - a) < 128) {
   ip = in + a * 32;
   for (i = a; i < b; ++i) {
        jp = out + i;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
   }
     } else {
   int ab = (a + b) / 2;
                  
#pragma omp task untied
   fft_unshuffle_32(a, ab, in, out, m);
                  
#pragma omp task untied
   fft_unshuffle_32(ab, b, in, out, m);
                  
#pragma omp taskwait
     }
}
void fft_unshuffle_32_seq(int a, int b, COMPLEX * in, COMPLEX * out, int m)
{
     int i;
     const COMPLEX *ip;
     COMPLEX *jp;
     if ((b - a) < 128) {
   ip = in + a * 32;
   for (i = a; i < b; ++i) {
        jp = out + i;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
        jp += 2 * m;
        jp[0] = ip[0];
        jp[m] = ip[1];
        ip += 2;
   }
     } else {
   int ab = (a + b) / 2;
   fft_unshuffle_32_seq(a, ab, in, out, m);
   fft_unshuffle_32_seq(ab, b, in, out, m);
     }
}
void fft_aux(int n, COMPLEX * in, COMPLEX * out, int *factors, COMPLEX * W, int nW)
{
     int r, m;
     int k;
     if (n == 32) {
   fft_base_32(in, out);
   return;
     }
     if (n == 16) {
   fft_base_16(in, out);
   return;
     }
     if (n == 8) {
   fft_base_8(in, out);
   return;
     }
     if (n == 4) {
   fft_base_4(in, out);
   return;
     }
     if (n == 2) {
   fft_base_2(in, out);
   return;
     }
     r = *factors;
     m = n / r;
     if (r < n) {
   if (r == 32) {
                       
#pragma omp task untied
        fft_unshuffle_32(0, m, in, out, m);
   } else if (r == 16) {
                       
#pragma omp task untied
        fft_unshuffle_16(0, m, in, out, m);
   } else if (r == 8) {
                       
#pragma omp task untied
        fft_unshuffle_8(0, m, in, out, m);
   } else if (r == 4) {
                       
#pragma omp task untied
        fft_unshuffle_4(0, m, in, out, m);
   } else if (r == 2) {
                       
#pragma omp task untied
        fft_unshuffle_2(0, m, in, out, m);
   } else
        unshuffle(0, m, in, out, r, m);
                  
#pragma omp taskwait
   for (k = 0; k < n; k += m) {
                       
#pragma omp task untied
        fft_aux(m, out + k, in + k, factors + 1, W, nW);
   }
                  
#pragma omp taskwait
     }
     if (r == 2) {
                  
#pragma omp task untied
   fft_twiddle_2(0, m, in, out, W, nW, nW / n, m);
     } else if (r == 4) {
                  
#pragma omp task untied
   fft_twiddle_4(0, m, in, out, W, nW, nW / n, m);
     } else if (r == 8) {
                  
#pragma omp task untied
   fft_twiddle_8(0, m, in, out, W, nW, nW / n, m);
     } else if (r == 16) {
                  
#pragma omp task untied
   fft_twiddle_16(0, m, in, out, W, nW, nW / n, m);
     } else if (r == 32) {
                  
#pragma omp task untied
   fft_twiddle_32(0, m, in, out, W, nW, nW / n, m);
     } else {
                  
#pragma omp task untied
   fft_twiddle_gen(0, m, in, out, W, nW, nW / n, r, m);
     }
             
#pragma omp taskwait
     return;
}
void fft_aux_seq(int n, COMPLEX * in, COMPLEX * out, int *factors, COMPLEX * W, int nW)
{
     int r, m;
     int k;
     if (n == 32) {
   fft_base_32(in, out);
   return;
     }
     if (n == 16) {
   fft_base_16(in, out);
   return;
     }
     if (n == 8) {
   fft_base_8(in, out);
   return;
     }
     if (n == 4) {
   fft_base_4(in, out);
   return;
     }
     if (n == 2) {
   fft_base_2(in, out);
   return;
     }
     r = *factors;
     m = n / r;
     if (r < n) {
   if (r == 32) fft_unshuffle_32_seq(0, m, in, out, m);
   else if (r == 16) fft_unshuffle_16_seq(0, m, in, out, m);
   else if (r == 8) fft_unshuffle_8_seq(0, m, in, out, m);
   else if (r == 4) fft_unshuffle_4_seq(0, m, in, out, m);
   else if (r == 2) fft_unshuffle_2_seq(0, m, in, out, m);
   else unshuffle_seq(0, m, in, out, r, m);
   for (k = 0; k < n; k += m) {
        fft_aux_seq(m, out + k, in + k, factors + 1, W, nW);
   }
     }
     if (r == 2) fft_twiddle_2_seq(0, m, in, out, W, nW, nW / n, m);
     else if (r == 4) fft_twiddle_4_seq(0, m, in, out, W, nW, nW / n, m);
     else if (r == 8) fft_twiddle_8_seq(0, m, in, out, W, nW, nW / n, m);
     else if (r == 16) fft_twiddle_16_seq(0, m, in, out, W, nW, nW / n, m);
     else if (r == 32) fft_twiddle_32_seq(0, m, in, out, W, nW, nW / n, m);
     else fft_twiddle_gen_seq(0, m, in, out, W, nW, nW / n, r, m);
     return;
}
void fft(int n, COMPLEX * in, COMPLEX * out)
{
     int factors[40];
     int *p = factors;
     int l = n;
     int r;
     COMPLEX *W;
     { if ( bots_verbose_mode >= BOTS_VERBOSE_DEFAULT ) { fprintf(__stdoutp, "Computing coefficients "); } };
     W = (COMPLEX *) malloc((n + 1) * sizeof(COMPLEX));
             
#pragma omp parallel
             
#pragma omp single
             
#pragma omp task untied
     compute_w_coefficients(n, 0, n / 2, W);
     { if ( bots_verbose_mode >= BOTS_VERBOSE_DEFAULT ) { fprintf(__stdoutp, " completed!\n"); } };
     do {
   r = factor(l);
   *p++ = r;
   l /= r;
     } while (l > 1);
     { if ( bots_verbose_mode >= BOTS_VERBOSE_DEFAULT ) { fprintf(__stdoutp, "Computing FFT "); } };
             
#pragma omp parallel
             
#pragma omp single
             
#pragma omp task untied
     fft_aux(n, in, out, factors, W, n);
     { if ( bots_verbose_mode >= BOTS_VERBOSE_DEFAULT ) { fprintf(__stdoutp, " completed!\n"); } };
     free(W);
     return;
}
void fft_seq(int n, COMPLEX * in, COMPLEX * out)
{
     int factors[40];
     int *p = factors;
     int l = n;
     int r;
     COMPLEX *W;
     W = (COMPLEX *) malloc((n + 1) * sizeof(COMPLEX));
     compute_w_coefficients_seq(n, 0, n / 2, W);
     do {
   r = factor(l);
   *p++ = r;
   l /= r;
     } while (l > 1);
     fft_aux_seq(n, in, out, factors, W, n);
     free(W);
     return;
}
int test_correctness(int n, COMPLEX *out1, COMPLEX *out2)
{
  int i;
  double a,d,error = 0.0;
  for (i = 0; i < n; ++i) {
       a = sqrt((((out1[i]).re) - ((out2[i]).re)) *
  (((out1[i]).re) - ((out2[i]).re)) +
  (((out1[i]).im) - ((out2[i]).im)) *
  (((out1[i]).im) - ((out2[i]).im)));
       d = sqrt(((out2[i]).re) * ((out2[i]).re) +
   ((out2[i]).im) * ((out2[i]).im));
       if (d < -1.0e-10 || d > 1.0e-10) a /= d;
       if (a > error) error = a;
  }
  { if ( bots_verbose_mode >= BOTS_VERBOSE_DEFAULT ) { fprintf(__stdoutp, "relative error=%e\n" , error); } };
  if (error > 1e-3) return 2;
  else return 1;
}
