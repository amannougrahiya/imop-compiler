typedef unsigned int size_t;
typedef unsigned char __u_char;
typedef unsigned short int __u_short;
typedef unsigned int __u_int;
typedef unsigned long int __u_long;
typedef signed char __int8_t;
typedef unsigned char __uint8_t;
typedef signed short int __int16_t;
typedef unsigned short int __uint16_t;
typedef signed int __int32_t;
typedef unsigned int __uint32_t;
__extension__ typedef signed long long int __int64_t;
__extension__ typedef unsigned long long int __uint64_t;
__extension__ typedef long long int __quad_t;
__extension__ typedef unsigned long long int __u_quad_t;
__extension__ typedef __u_quad_t __dev_t;
__extension__ typedef unsigned int __uid_t;
__extension__ typedef unsigned int __gid_t;
__extension__ typedef unsigned long int __ino_t;
__extension__ typedef __u_quad_t __ino64_t;
__extension__ typedef unsigned int __mode_t;
__extension__ typedef unsigned int __nlink_t;
__extension__ typedef long int __off_t;
__extension__ typedef __quad_t __off64_t;
__extension__ typedef int __pid_t;
__extension__ struct stUn_imopVarPre0 {
    int __val[2];
} ;
__extension__ typedef struct stUn_imopVarPre0 __fsid_t;
__extension__ typedef long int __clock_t;
__extension__ typedef unsigned long int __rlim_t;
__extension__ typedef __u_quad_t __rlim64_t;
__extension__ typedef unsigned int __id_t;
__extension__ typedef long int __time_t;
__extension__ typedef unsigned int __useconds_t;
__extension__ typedef long int __suseconds_t;
__extension__ typedef int __daddr_t;
__extension__ typedef int __key_t;
__extension__ typedef int __clockid_t;
__extension__ typedef void *__timer_t;
__extension__ typedef long int __blksize_t;
__extension__ typedef long int __blkcnt_t;
__extension__ typedef __quad_t __blkcnt64_t;
__extension__ typedef unsigned long int __fsblkcnt_t;
__extension__ typedef __u_quad_t __fsblkcnt64_t;
__extension__ typedef unsigned long int __fsfilcnt_t;
__extension__ typedef __u_quad_t __fsfilcnt64_t;
__extension__ typedef int __fsword_t;
__extension__ typedef int __ssize_t;
__extension__ typedef long int __syscall_slong_t;
__extension__ typedef unsigned long int __syscall_ulong_t;
typedef __off64_t __loff_t;
typedef __quad_t *__qaddr_t;
typedef char *__caddr_t;
__extension__ typedef int __intptr_t;
__extension__ typedef unsigned int __socklen_t;
struct _IO_FILE ;
typedef struct _IO_FILE FILE;
typedef struct _IO_FILE __FILE;
struct stUn_imopVarPre2 {
    int __count;
    union stUn_imopVarPre1 {
        unsigned int __wch;
        char __wchb[4];
    } __value;
} ;
typedef struct stUn_imopVarPre2 __mbstate_t;
struct stUn_imopVarPre3 {
    __off_t __pos;
    __mbstate_t __state;
} ;
typedef struct stUn_imopVarPre3 _G_fpos_t;
struct stUn_imopVarPre4 {
    __off64_t __pos;
    __mbstate_t __state;
} ;
typedef struct stUn_imopVarPre4 _G_fpos64_t;
typedef __builtin_va_list __gnuc_va_list;
struct _IO_jump_t ;
struct _IO_FILE ;
typedef void _IO_lock_t;
struct _IO_marker {
    struct _IO_marker *_next;
    struct _IO_FILE *_sbuf;
    int _pos;
} ;
enum __codecvt_result {
    __codecvt_ok, __codecvt_partial , __codecvt_error , __codecvt_noconv
} ;
struct _IO_FILE {
    int _flags;
    char *_IO_read_ptr;
    char *_IO_read_end;
    char *_IO_read_base;
    char *_IO_write_base;
    char *_IO_write_ptr;
    char *_IO_write_end;
    char *_IO_buf_base;
    char *_IO_buf_end;
    char *_IO_save_base;
    char *_IO_backup_base;
    char *_IO_save_end;
    struct _IO_marker *_markers;
    struct _IO_FILE *_chain;
    int _fileno;
    int _flags2;
    __off_t _old_offset;
    unsigned short _cur_column;
    signed char _vtable_offset;
    char _shortbuf[1];
    _IO_lock_t *_lock;
    __off64_t _offset;
    void *__pad1;
    void *__pad2;
    void *__pad3;
    void *__pad4;
    size_t __pad5;
    int _mode;
    char _unused2[15 * sizeof(int) - 4 * sizeof(void *) - sizeof(size_t)];
} ;
typedef struct _IO_FILE _IO_FILE;
struct _IO_FILE_plus ;
typedef __gnuc_va_list va_list;
typedef __off_t off_t;
typedef __ssize_t ssize_t;
typedef _G_fpos_t fpos_t;
extern struct _IO_FILE *stdout;
extern struct _IO_FILE *stderr;
extern int fclose(FILE *__stream);
extern int fflush(FILE *__stream);
extern FILE *fopen(const char *__restrict __filename, const char *__restrict __modes);
extern int fprintf(FILE *__restrict __stream, const char *__restrict __format, ...);
extern int sprintf(char *__restrict __s, const char *__restrict __format, ...);
extern int snprintf(char *__restrict __s, size_t __maxlen , const char *__restrict __format, ...);
extern int fscanf(FILE *__restrict __stream, const char *__restrict __format, ...);
extern int fscanf(FILE *__restrict __stream, const char *__restrict __format, ...);
typedef unsigned int wchar_t;
enum enum_imopVarPre5 {
    P_ALL, P_PID , P_PGID
} ;
typedef enum enum_imopVarPre5 idtype_t;
union wait {
    int w_status;
    struct stUn_imopVarPre6 {
        unsigned int __w_termsig: 7;
        unsigned int __w_coredump: 1;
        unsigned int __w_retcode: 8;
        unsigned int :16;
    } __wait_terminated;
    struct stUn_imopVarPre7 {
        unsigned int __w_stopval: 8;
        unsigned int __w_stopsig: 8;
        unsigned int :16;
    } __wait_stopped;
} ;
union stUn_imopVarPre8 {
    union wait *__uptr;
    int *__iptr;
} ;
typedef union stUn_imopVarPre8 __WAIT_STATUS;
struct stUn_imopVarPre9 {
    int quot;
    int rem;
} ;
typedef struct stUn_imopVarPre9 div_t;
struct stUn_imopVarPre10 {
    long int quot;
    long int rem;
} ;
typedef struct stUn_imopVarPre10 ldiv_t;
__extension__ struct stUn_imopVarPre11 {
    long long int quot;
    long long int rem;
} ;
__extension__ typedef struct stUn_imopVarPre11 lldiv_t;
extern int atoi(const char *__nptr);
typedef __u_char u_char;
typedef __u_short u_short;
typedef __u_int u_int;
typedef __u_long u_long;
typedef __quad_t quad_t;
typedef __u_quad_t u_quad_t;
typedef __fsid_t fsid_t;
typedef __loff_t loff_t;
typedef __ino_t ino_t;
typedef __dev_t dev_t;
typedef __gid_t gid_t;
typedef __mode_t mode_t;
typedef __nlink_t nlink_t;
typedef __uid_t uid_t;
typedef __pid_t pid_t;
typedef __id_t id_t;
typedef __daddr_t daddr_t;
typedef __caddr_t caddr_t;
typedef __key_t key_t;
typedef __clock_t clock_t;
typedef __time_t time_t;
typedef __clockid_t clockid_t;
typedef __timer_t timer_t;
typedef unsigned long int ulong;
typedef unsigned short int ushort;
typedef unsigned int uint;
typedef int int8_t;
typedef int int16_t;
typedef int int32_t;
typedef int int64_t;
typedef unsigned int u_int8_t;
typedef unsigned int u_int16_t;
typedef unsigned int u_int32_t;
typedef unsigned int u_int64_t;
typedef int register_t;
typedef int __sig_atomic_t;
struct stUn_imopVarPre12 {
    unsigned long int __val[(1024 / (8 * sizeof(unsigned long int)))];
} ;
typedef struct stUn_imopVarPre12 __sigset_t;
typedef __sigset_t sigset_t;
struct timespec {
    __time_t tv_sec;
    __syscall_slong_t tv_nsec;
} ;
struct timeval {
    __time_t tv_sec;
    __suseconds_t tv_usec;
} ;
typedef __suseconds_t suseconds_t;
typedef long int __fd_mask;
struct stUn_imopVarPre13 {
    __fd_mask __fds_bits[1024 / (8 * (int) sizeof(__fd_mask))];
} ;
typedef struct stUn_imopVarPre13 fd_set;
typedef __fd_mask fd_mask;
typedef __blksize_t blksize_t;
typedef __blkcnt_t blkcnt_t;
typedef __fsblkcnt_t fsblkcnt_t;
typedef __fsfilcnt_t fsfilcnt_t;
typedef unsigned long int pthread_t;
union pthread_attr_t {
    char __size[36];
    long int __align;
} ;
typedef union pthread_attr_t pthread_attr_t;
struct __pthread_internal_slist {
    struct __pthread_internal_slist *__next;
} ;
typedef struct __pthread_internal_slist __pthread_slist_t;
union stUn_imopVarPre14 {
    struct __pthread_mutex_s {
        int __lock;
        unsigned int __count;
        int __owner;
        int __kind;
        unsigned int __nusers;
    } __data;
    char __size[24];
    long int __align;
} ;
typedef union stUn_imopVarPre14 pthread_mutex_t;
union stUn_imopVarPre15 {
    char __size[4];
    long int __align;
} ;
typedef union stUn_imopVarPre15 pthread_mutexattr_t;
union stUn_imopVarPre17 {
    struct stUn_imopVarPre16 {
        int __lock;
        unsigned int __futex;
        __extension__ unsigned long long int __total_seq;
        __extension__ unsigned long long int __wakeup_seq;
        __extension__ unsigned long long int __woken_seq;
        void *__mutex;
        unsigned int __nwaiters;
        unsigned int __broadcast_seq;
    } __data;
    char __size[48];
    __extension__ long long int __align;
} ;
typedef union stUn_imopVarPre17 pthread_cond_t;
union stUn_imopVarPre18 {
    char __size[4];
    long int __align;
} ;
typedef union stUn_imopVarPre18 pthread_condattr_t;
typedef unsigned int pthread_key_t;
typedef int pthread_once_t;
union stUn_imopVarPre20 {
    struct stUn_imopVarPre19 {
        int __lock;
        unsigned int __nr_readers;
        unsigned int __readers_wakeup;
        unsigned int __writer_wakeup;
        unsigned int __nr_readers_queued;
        unsigned int __nr_writers_queued;
        unsigned char __flags;
        unsigned char __shared;
        unsigned char __pad1;
        unsigned char __pad2;
        int __writer;
    } __data;
    char __size[32];
    long int __align;
} ;
typedef union stUn_imopVarPre20 pthread_rwlock_t;
union stUn_imopVarPre21 {
    char __size[8];
    long int __align;
} ;
typedef union stUn_imopVarPre21 pthread_rwlockattr_t;
typedef volatile int pthread_spinlock_t;
union stUn_imopVarPre22 {
    char __size[20];
    long int __align;
} ;
typedef union stUn_imopVarPre22 pthread_barrier_t;
union stUn_imopVarPre23 {
    char __size[4];
    int __align;
} ;
typedef union stUn_imopVarPre23 pthread_barrierattr_t;
struct random_data {
    int32_t *fptr;
    int32_t *rptr;
    int32_t *state;
    int rand_type;
    int rand_deg;
    int rand_sep;
    int32_t *end_ptr;
} ;
struct drand48_data {
    unsigned short int __x[3];
    unsigned short int __old_x[3];
    unsigned short int __c;
    unsigned short int __init;
    __extension__ unsigned long long int __a;
} ;
extern void *malloc(size_t __size);
extern void exit(int __status);
typedef int ( *__compar_fn_t )(const void *, const void *);
extern int getloadavg(double __loadavg[], int __nelem);
typedef float float_t;
typedef double double_t;
extern double pow(double __x, double __y);
enum enum_imopVarPre24 {
    FP_NAN = 0, FP_INFINITE = 1 , FP_ZERO = 2 , FP_SUBNORMAL = 3 , FP_NORMAL = 4
} ;
enum enum_imopVarPre25 {
    _IEEE_ = -1, _SVID_ , _XOPEN_ , _POSIX_ , _ISOC_
} ;
typedef enum enum_imopVarPre25 _LIB_VERSION_TYPE;
struct exception {
    int type;
    char *name;
    double arg1;
    double arg2;
    double retval;
} ;
typedef int ptrdiff_t;
struct stUn_imopVarPre26 {
    long long __max_align_ll;
    long double __max_align_ld;
} ;
typedef struct stUn_imopVarPre26 max_align_t;
extern char *strcpy(char *__restrict __dest, const char *__restrict __src);
struct __locale_struct {
    struct __locale_data *__locales[13];
    const unsigned short int *__ctype_b;
    const int *__ctype_tolower;
    const int *__ctype_toupper;
    const char *__names[13];
} ;
typedef struct __locale_struct *__locale_t;
typedef __locale_t locale_t;
struct timezone {
    int tz_minuteswest;
    int tz_dsttime;
} ;
typedef struct timezone *__restrict __timezone_ptr_t;
extern int gettimeofday(struct timeval *__restrict __tv, __timezone_ptr_t __tz);
enum __itimer_which {
    ITIMER_REAL = 0, ITIMER_VIRTUAL = 1 , ITIMER_PROF = 2
} ;
struct itimerval {
    struct timeval it_interval;
    struct timeval it_value;
} ;
typedef int __itimer_which_t;
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
    long int tm_gmtoff;
    const char *tm_zone;
} ;
struct itimerspec {
    struct timespec it_interval;
    struct timespec it_value;
} ;
struct sigevent ;
extern time_t time(time_t *__timer);
extern size_t strftime(char *__restrict __s, size_t __maxsize , const char *__restrict __format , const struct tm *__restrict __tp);
extern struct tm *gmtime(const time_t *__timer);
struct utsname {
    char sysname[65];
    char nodename[65];
    char release[65];
    char version[65];
    char machine[65];
    char __domainname[65];
} ;
extern int uname(struct utsname *__name);
enum __rlimit_resource {
    RLIMIT_CPU = 0, RLIMIT_FSIZE = 1 , RLIMIT_DATA = 2 , RLIMIT_STACK = 3 , RLIMIT_CORE = 4 , __RLIMIT_RSS = 5 , RLIMIT_NOFILE = 7 , __RLIMIT_OFILE = RLIMIT_NOFILE , RLIMIT_AS = 9 , __RLIMIT_NPROC = 6 , __RLIMIT_MEMLOCK = 8 , __RLIMIT_LOCKS = 10 , __RLIMIT_SIGPENDING = 11 , __RLIMIT_MSGQUEUE = 12 , __RLIMIT_NICE = 13 , __RLIMIT_RTPRIO = 14 , __RLIMIT_RTTIME = 15 , __RLIMIT_NLIMITS = 16 , __RLIM_NLIMITS = __RLIMIT_NLIMITS
} ;
typedef __rlim_t rlim_t;
struct rlimit {
    rlim_t rlim_cur;
    rlim_t rlim_max;
} ;
enum __rusage_who {
    RUSAGE_SELF = 0, RUSAGE_CHILDREN = -1
} ;
struct rusage {
    struct timeval ru_utime;
    struct timeval ru_stime;
} ;
enum __priority_which {
    PRIO_PROCESS = 0, PRIO_PGRP = 1 , PRIO_USER = 2
} ;
typedef int __rlimit_resource_t;
typedef int __rusage_who_t;
typedef int __priority_which_t;
extern char *__xpg_basename(char *__path);
typedef __useconds_t useconds_t;
typedef __intptr_t intptr_t;
typedef __socklen_t socklen_t;
enum enum_imopVarPre27 {
    _PC_LINK_MAX, _PC_MAX_CANON , _PC_MAX_INPUT , _PC_NAME_MAX , _PC_PATH_MAX , _PC_PIPE_BUF , _PC_CHOWN_RESTRICTED , _PC_NO_TRUNC , _PC_VDISABLE , _PC_SYNC_IO , _PC_ASYNC_IO , _PC_PRIO_IO , _PC_SOCK_MAXBUF , _PC_FILESIZEBITS , _PC_REC_INCR_XFER_SIZE , _PC_REC_MAX_XFER_SIZE , _PC_REC_MIN_XFER_SIZE , _PC_REC_XFER_ALIGN , _PC_ALLOC_SIZE_MIN , _PC_SYMLINK_MAX , _PC_2_SYMLINKS
} ;
enum enum_imopVarPre28 {
    _SC_ARG_MAX, _SC_CHILD_MAX , _SC_CLK_TCK , _SC_NGROUPS_MAX , _SC_OPEN_MAX , _SC_STREAM_MAX , _SC_TZNAME_MAX , _SC_JOB_CONTROL , _SC_SAVED_IDS , _SC_REALTIME_SIGNALS , _SC_PRIORITY_SCHEDULING , _SC_TIMERS , _SC_ASYNCHRONOUS_IO , _SC_PRIORITIZED_IO , _SC_SYNCHRONIZED_IO , _SC_FSYNC , _SC_MAPPED_FILES , _SC_MEMLOCK , _SC_MEMLOCK_RANGE , _SC_MEMORY_PROTECTION , _SC_MESSAGE_PASSING , _SC_SEMAPHORES , _SC_SHARED_MEMORY_OBJECTS , _SC_AIO_LISTIO_MAX , _SC_AIO_MAX , _SC_AIO_PRIO_DELTA_MAX , _SC_DELAYTIMER_MAX , _SC_MQ_OPEN_MAX , _SC_MQ_PRIO_MAX , _SC_VERSION , _SC_PAGESIZE , _SC_RTSIG_MAX , _SC_SEM_NSEMS_MAX , _SC_SEM_VALUE_MAX , _SC_SIGQUEUE_MAX , _SC_TIMER_MAX , _SC_BC_BASE_MAX , _SC_BC_DIM_MAX , _SC_BC_SCALE_MAX , _SC_BC_STRING_MAX , _SC_COLL_WEIGHTS_MAX , _SC_EQUIV_CLASS_MAX , _SC_EXPR_NEST_MAX , _SC_LINE_MAX , _SC_RE_DUP_MAX , _SC_CHARCLASS_NAME_MAX , _SC_2_VERSION , _SC_2_C_BIND , _SC_2_C_DEV , _SC_2_FORT_DEV , _SC_2_FORT_RUN , _SC_2_SW_DEV , _SC_2_LOCALEDEF , _SC_PII , _SC_PII_XTI , _SC_PII_SOCKET , _SC_PII_INTERNET , _SC_PII_OSI , _SC_POLL , _SC_SELECT , _SC_UIO_MAXIOV , _SC_IOV_MAX = _SC_UIO_MAXIOV , _SC_PII_INTERNET_STREAM , _SC_PII_INTERNET_DGRAM , _SC_PII_OSI_COTS , _SC_PII_OSI_CLTS , _SC_PII_OSI_M , _SC_T_IOV_MAX , _SC_THREADS , _SC_THREAD_SAFE_FUNCTIONS , _SC_GETGR_R_SIZE_MAX , _SC_GETPW_R_SIZE_MAX , _SC_LOGIN_NAME_MAX , _SC_TTY_NAME_MAX , _SC_THREAD_DESTRUCTOR_ITERATIONS , _SC_THREAD_KEYS_MAX , _SC_THREAD_STACK_MIN , _SC_THREAD_THREADS_MAX , _SC_THREAD_ATTR_STACKADDR , _SC_THREAD_ATTR_STACKSIZE , _SC_THREAD_PRIORITY_SCHEDULING , _SC_THREAD_PRIO_INHERIT , _SC_THREAD_PRIO_PROTECT , _SC_THREAD_PROCESS_SHARED , _SC_NPROCESSORS_CONF , _SC_NPROCESSORS_ONLN , _SC_PHYS_PAGES , _SC_AVPHYS_PAGES , _SC_ATEXIT_MAX , _SC_PASS_MAX , _SC_XOPEN_VERSION , _SC_XOPEN_XCU_VERSION , _SC_XOPEN_UNIX , _SC_XOPEN_CRYPT , _SC_XOPEN_ENH_I18N , _SC_XOPEN_SHM , _SC_2_CHAR_TERM , _SC_2_C_VERSION , _SC_2_UPE , _SC_XOPEN_XPG2 , _SC_XOPEN_XPG3 , _SC_XOPEN_XPG4 , _SC_CHAR_BIT , _SC_CHAR_MAX , _SC_CHAR_MIN , _SC_INT_MAX , _SC_INT_MIN , _SC_LONG_BIT , _SC_WORD_BIT , _SC_MB_LEN_MAX , _SC_NZERO , _SC_SSIZE_MAX , _SC_SCHAR_MAX , _SC_SCHAR_MIN , _SC_SHRT_MAX , _SC_SHRT_MIN , _SC_UCHAR_MAX , _SC_UINT_MAX , _SC_ULONG_MAX , _SC_USHRT_MAX , _SC_NL_ARGMAX , _SC_NL_LANGMAX , _SC_NL_MSGMAX , _SC_NL_NMAX , _SC_NL_SETMAX , _SC_NL_TEXTMAX , _SC_XBS5_ILP32_OFF32 , _SC_XBS5_ILP32_OFFBIG , _SC_XBS5_LP64_OFF64 , _SC_XBS5_LPBIG_OFFBIG , _SC_XOPEN_LEGACY , _SC_XOPEN_REALTIME , _SC_XOPEN_REALTIME_THREADS , _SC_ADVISORY_INFO , _SC_BARRIERS , _SC_BASE , _SC_C_LANG_SUPPORT , _SC_C_LANG_SUPPORT_R , _SC_CLOCK_SELECTION , _SC_CPUTIME , _SC_THREAD_CPUTIME , _SC_DEVICE_IO , _SC_DEVICE_SPECIFIC , _SC_DEVICE_SPECIFIC_R , _SC_FD_MGMT , _SC_FIFO , _SC_PIPE , _SC_FILE_ATTRIBUTES , _SC_FILE_LOCKING , _SC_FILE_SYSTEM , _SC_MONOTONIC_CLOCK , _SC_MULTI_PROCESS , _SC_SINGLE_PROCESS , _SC_NETWORKING , _SC_READER_WRITER_LOCKS , _SC_SPIN_LOCKS , _SC_REGEXP , _SC_REGEX_VERSION , _SC_SHELL , _SC_SIGNALS , _SC_SPAWN , _SC_SPORADIC_SERVER , _SC_THREAD_SPORADIC_SERVER , _SC_SYSTEM_DATABASE , _SC_SYSTEM_DATABASE_R , _SC_TIMEOUTS , _SC_TYPED_MEMORY_OBJECTS , _SC_USER_GROUPS , _SC_USER_GROUPS_R , _SC_2_PBS , _SC_2_PBS_ACCOUNTING , _SC_2_PBS_LOCATE , _SC_2_PBS_MESSAGE , _SC_2_PBS_TRACK , _SC_SYMLOOP_MAX , _SC_STREAMS , _SC_2_PBS_CHECKPOINT , _SC_V6_ILP32_OFF32 , _SC_V6_ILP32_OFFBIG , _SC_V6_LP64_OFF64 , _SC_V6_LPBIG_OFFBIG , _SC_HOST_NAME_MAX , _SC_TRACE , _SC_TRACE_EVENT_FILTER , _SC_TRACE_INHERIT , _SC_TRACE_LOG , _SC_LEVEL1_ICACHE_SIZE , _SC_LEVEL1_ICACHE_ASSOC , _SC_LEVEL1_ICACHE_LINESIZE , _SC_LEVEL1_DCACHE_SIZE , _SC_LEVEL1_DCACHE_ASSOC , _SC_LEVEL1_DCACHE_LINESIZE , _SC_LEVEL2_CACHE_SIZE , _SC_LEVEL2_CACHE_ASSOC , _SC_LEVEL2_CACHE_LINESIZE , _SC_LEVEL3_CACHE_SIZE , _SC_LEVEL3_CACHE_ASSOC , _SC_LEVEL3_CACHE_LINESIZE , _SC_LEVEL4_CACHE_SIZE , _SC_LEVEL4_CACHE_ASSOC , _SC_LEVEL4_CACHE_LINESIZE , _SC_IPV6 = _SC_LEVEL1_ICACHE_SIZE + 50 , _SC_RAW_SOCKETS , _SC_V7_ILP32_OFF32 , _SC_V7_ILP32_OFFBIG , _SC_V7_LP64_OFF64 , _SC_V7_LPBIG_OFFBIG , _SC_SS_REPL_MAX , _SC_TRACE_EVENT_NAME_MAX , _SC_TRACE_NAME_MAX , _SC_TRACE_SYS_MAX , _SC_TRACE_USER_EVENT_MAX , _SC_XOPEN_STREAMS , _SC_THREAD_ROBUST_PRIO_INHERIT , _SC_THREAD_ROBUST_PRIO_PROTECT
} ;
enum enum_imopVarPre29 {
    _CS_PATH, _CS_V6_WIDTH_RESTRICTED_ENVS , _CS_GNU_LIBC_VERSION , _CS_GNU_LIBPTHREAD_VERSION , _CS_V5_WIDTH_RESTRICTED_ENVS , _CS_V7_WIDTH_RESTRICTED_ENVS , _CS_LFS_CFLAGS = 1000 , _CS_LFS_LDFLAGS , _CS_LFS_LIBS , _CS_LFS_LINTFLAGS , _CS_LFS64_CFLAGS , _CS_LFS64_LDFLAGS , _CS_LFS64_LIBS , _CS_LFS64_LINTFLAGS , _CS_XBS5_ILP32_OFF32_CFLAGS = 1100 , _CS_XBS5_ILP32_OFF32_LDFLAGS , _CS_XBS5_ILP32_OFF32_LIBS , _CS_XBS5_ILP32_OFF32_LINTFLAGS , _CS_XBS5_ILP32_OFFBIG_CFLAGS , _CS_XBS5_ILP32_OFFBIG_LDFLAGS , _CS_XBS5_ILP32_OFFBIG_LIBS , _CS_XBS5_ILP32_OFFBIG_LINTFLAGS , _CS_XBS5_LP64_OFF64_CFLAGS , _CS_XBS5_LP64_OFF64_LDFLAGS , _CS_XBS5_LP64_OFF64_LIBS , _CS_XBS5_LP64_OFF64_LINTFLAGS , _CS_XBS5_LPBIG_OFFBIG_CFLAGS , _CS_XBS5_LPBIG_OFFBIG_LDFLAGS , _CS_XBS5_LPBIG_OFFBIG_LIBS , _CS_XBS5_LPBIG_OFFBIG_LINTFLAGS , _CS_POSIX_V6_ILP32_OFF32_CFLAGS , _CS_POSIX_V6_ILP32_OFF32_LDFLAGS , _CS_POSIX_V6_ILP32_OFF32_LIBS , _CS_POSIX_V6_ILP32_OFF32_LINTFLAGS , _CS_POSIX_V6_ILP32_OFFBIG_CFLAGS , _CS_POSIX_V6_ILP32_OFFBIG_LDFLAGS , _CS_POSIX_V6_ILP32_OFFBIG_LIBS , _CS_POSIX_V6_ILP32_OFFBIG_LINTFLAGS , _CS_POSIX_V6_LP64_OFF64_CFLAGS , _CS_POSIX_V6_LP64_OFF64_LDFLAGS , _CS_POSIX_V6_LP64_OFF64_LIBS , _CS_POSIX_V6_LP64_OFF64_LINTFLAGS , _CS_POSIX_V6_LPBIG_OFFBIG_CFLAGS , _CS_POSIX_V6_LPBIG_OFFBIG_LDFLAGS , _CS_POSIX_V6_LPBIG_OFFBIG_LIBS , _CS_POSIX_V6_LPBIG_OFFBIG_LINTFLAGS , _CS_POSIX_V7_ILP32_OFF32_CFLAGS , _CS_POSIX_V7_ILP32_OFF32_LDFLAGS , _CS_POSIX_V7_ILP32_OFF32_LIBS , _CS_POSIX_V7_ILP32_OFF32_LINTFLAGS , _CS_POSIX_V7_ILP32_OFFBIG_CFLAGS , _CS_POSIX_V7_ILP32_OFFBIG_LDFLAGS , _CS_POSIX_V7_ILP32_OFFBIG_LIBS , _CS_POSIX_V7_ILP32_OFFBIG_LINTFLAGS , _CS_POSIX_V7_LP64_OFF64_CFLAGS , _CS_POSIX_V7_LP64_OFF64_LDFLAGS , _CS_POSIX_V7_LP64_OFF64_LIBS , _CS_POSIX_V7_LP64_OFF64_LINTFLAGS , _CS_POSIX_V7_LPBIG_OFFBIG_CFLAGS , _CS_POSIX_V7_LPBIG_OFFBIG_LDFLAGS , _CS_POSIX_V7_LPBIG_OFFBIG_LIBS , _CS_POSIX_V7_LPBIG_OFFBIG_LINTFLAGS , _CS_V6_ENV , _CS_V7_ENV
} ;
extern long int sysconf(int __name);
void bots_get_date(char *str);
void bots_get_architecture(char *str);
void bots_get_load_average(char *str);
void bots_print_results(void );
struct stUn_imopVarPre30 {
    unsigned char _x[4];
} ;
typedef struct stUn_imopVarPre30 omp_lock_t;
struct stUn_imopVarPre31 {
    unsigned char _x[8 + sizeof(void *)];
} ;
typedef struct stUn_imopVarPre31 omp_nest_lock_t;
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
extern void omp_init_lock(omp_lock_t *);
extern void omp_set_lock(omp_lock_t *);
extern void omp_unset_lock(omp_lock_t *);
void bots_print_usage(void );
void bots_set_info();
void bots_get_params_common(int argc, char **argv);
void bots_get_params(int argc, char **argv);
void energymonitor__setfilename(char *profFileName);
void energymonitor__init(int cores, float sleeptime);
void energymonitor__startprofiling();
void energymonitor__stopprofiling();
void energymonitor__trackpoweronly();
struct Results {
    long hosps_number;
    long hosps_personnel;
    long total_patients;
    long total_in_village;
    long total_waiting;
    long total_assess;
    long total_inside;
    long total_time;
    long total_hosps_v;
} ;
extern int sim_level;
struct Patient {
    int id;
    int32_t seed;
    int time;
    int time_left;
    int hosps_visited;
    struct Village *home_village;
    struct Patient *back;
    struct Patient *forward;
} ;
struct Hosp {
    int personnel;
    int free_personnel;
    struct Patient *waiting;
    struct Patient *assess;
    struct Patient *inside;
    struct Patient *realloc;
    omp_lock_t realloc_lock;
} ;
struct Village {
    int id;
    struct Village *back;
    struct Village *next;
    struct Village *forward;
    struct Patient *population;
    struct Hosp hosp;
    int level;
    int32_t seed;
} ;
float my_rand(int32_t *seed);
void put_in_hosp(struct Hosp *hosp, struct Patient *patient);
void addList(struct Patient **list, struct Patient *patient);
void removeList(struct Patient **list, struct Patient *patient);
void check_patients_inside(struct Village *village);
void check_patients_waiting(struct Village *village);
void check_patients_realloc(struct Village *village);
void check_patients_assess_par(struct Village *village);
struct Results get_results(struct Village *village);
void read_input_data(char *filename);
void allocate_village(struct Village **capital, struct Village *back , struct Village *next , int level , int32_t vid);
void sim_village_main_par(struct Village *top);
void sim_village_par(struct Village *village);
int check_village(struct Village *top);
void check_patients_population(struct Village *village);
void my_print(struct Village *village);
extern int bots_sequential_flag;
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
long bots_usecs();
void bots_error(int error, char *message);
enum enum_imopVarPre32 {
    BOTS_VERBOSE_NONE = 0, BOTS_VERBOSE_DEFAULT , BOTS_VERBOSE_DEBUG
} ;
typedef enum enum_imopVarPre32 bots_verbose_mode_t;
extern bots_verbose_mode_t bots_verbose_mode;
int sim_level;
int sim_cities;
int sim_population_ratio;
int sim_time;
int sim_assess_time;
int sim_convalescence_time;
int32_t sim_seed;
float sim_get_sick_p;
float sim_convalescence_p;
float sim_realloc_p;
int sim_pid = 0;
int res_population;
int res_hospitals;
int res_personnel;
int res_checkin;
int res_village;
int res_waiting;
int res_assess;
int res_inside;
float res_avg_stay;
float my_rand(int32_t *seed) {
    int32_t k;
    int32_t idum = *seed;
    idum ^= 123459876;
    k = idum / 127773;
    idum = 16807 * (idum - k * 127773) - 2836 * k;
    idum ^= 123459876;
    if (idum < 0) {
        idum += 2147483647;
    }
    *seed = idum * 2147483647;
    return (float) (1.0 / 2147483647) * idum;
}
void addList(struct Patient **list, struct Patient *patient) {
    if (*list == ((void *) 0)) {
        *list = patient;
        patient->back = ((void *) 0);
        patient->forward = ((void *) 0);
    } else {
        struct Patient *aux = *list;
        while (aux->forward != ((void *) 0)) {
            aux = aux->forward;
        }
        aux->forward = patient;
        patient->back = aux;
        patient->forward = ((void *) 0);
    }
}
void removeList(struct Patient **list, struct Patient *patient) {
    if (patient->back != ((void *) 0)) {
        patient->back->forward = patient->forward;
    } else {
        *list = patient->forward;
    }
    if (patient->forward != ((void *) 0)) {
        patient->forward->back = patient->back;
    }
}
void allocate_village(struct Village **capital, struct Village *back , struct Village *next , int level , int32_t vid) {
    int i;
    int population;
    int personnel;
    struct Village *current;
    struct Village *inext;
    struct Patient *patient;
    if (level == 0) {
        *capital = ((void *) 0);
    } else {
        double _imopVarPre111;
        _imopVarPre111 = pow(2, level);
        personnel = (int) _imopVarPre111;
        population = personnel * sim_population_ratio;
        unsigned long int _imopVarPre114;
        void *_imopVarPre115;
        _imopVarPre114 = sizeof(struct Village);
        _imopVarPre115 = malloc(_imopVarPre114);
        *capital = (struct Village *) _imopVarPre115;
        (*capital)->back = back;
        (*capital)->next = next;
        (*capital)->level = level;
        (*capital)->id = vid;
        (*capital)->seed = vid * (127773 + sim_seed);
        (*capital)->population = ((void *) 0);
        for (i = 0; i < population; i++) {
            unsigned long int _imopVarPre118;
            void *_imopVarPre119;
            _imopVarPre118 = sizeof(struct Patient);
            _imopVarPre119 = malloc(_imopVarPre118);
            patient = (struct Patient *) _imopVarPre119;
            patient->id = sim_pid++;
            patient->seed = (*capital)->seed;
            int *_imopVarPre121;
            _imopVarPre121 = &((*capital)->seed);
            my_rand(_imopVarPre121);
            patient->hosps_visited = 0;
            patient->time = 0;
            patient->time_left = 0;
            patient->home_village = *capital;
            struct Patient {
                int id;
                int seed;
                int time;
                int time_left;
                int hosps_visited;
                struct Village *home_village;
                struct Patient *back;
                struct Patient *forward;
            } **_imopVarPre123;
            _imopVarPre123 = &((*capital)->population);
            addList(_imopVarPre123, patient);
        }
        (*capital)->hosp.personnel = personnel;
        (*capital)->hosp.free_personnel = personnel;
        (*capital)->hosp.assess = ((void *) 0);
        (*capital)->hosp.waiting = ((void *) 0);
        (*capital)->hosp.inside = ((void *) 0);
        (*capital)->hosp.realloc = ((void *) 0);
        struct stUn_imopVarPre30 *_imopVarPre125;
        _imopVarPre125 = &(*capital)->hosp.realloc_lock;
        omp_init_lock(_imopVarPre125);
// #pragma omp dummyFlush LOCK_WRITE_END written([heapCell#1, sim_seed, sim_pid, bots_exec_message.f, bots_output_format, bots_time_program, heapCell#2, sim_assess_time, res_personnel, bots_comp_message.f, bots_cutoff_value, bots_resources.f, sim_get_sick_p, res_waiting, bots_verbose_mode, nullCell, bots_arg_file.f, res_avg_stay, sim_convalescence_p, bots_ld.f, res_population, bots_check_flag, top, res_village, res_assess, bots_parameters.f, bots_model.f, sim_convalescence_time, res_inside, res_hospitals, sim_population_ratio, res_checkin, bots_result, bots_comp_date.f, bots_exec_date.f, sim_time, bots_cutoff.f, bots_cc.f, bots_cflags.f, sim_level, bots_name.f, sim_cities, bots_print_header, sim_realloc_p, bots_ldflags.f, bots_execname.f]) read([&sim_convalescence_time, sim_seed, bots_exec_date, bots_exec_message.f, &res_checkin, heapCell#2, &sim_level, removeList, bots_verbose_mode, bots_usecs, check_patients_waiting, top, &sim_realloc_p, res_assess, bots_parameters.f, heapCell#0, bots_result, bots_exec_date.f, &sim_assess_time, &sim_get_sick_p, bots_sequential_flag, fprintf, bots_get_architecture, check_patients_inside, &res_waiting, _imopVarPre144, bots_parameters, heapCell#1, &sim_population_ratio, &sim_time, bots_error, atoi, &heapCell#1, bots_cutoff_value, bots_execname, stderr, vlist, sim_get_sick_p, free_BOTS_VERBOSE_DEFAULT, bots_name, res_avg_stay, vlist, read_input_data, bots_check_flag, put_in_hosp, res_village, get_results, getloadavg, check_patients_population, sim_village_main_par, res_hospitals, malloc, fclose, bots_cflags, bots_print_results, bots_cflags.f, sim_level, pow, energymonitor__startprofiling, bots_arg_file, energymonitor__init, allocate_village, sim_cities, snprintf, bots_print_usage, &res_population, error, gettimeofday, &sim_convalescence_p, bots_ld, bots_execname.f, &res_hospitals, &res_assess, stdout, bots_cutoff, bots_time_program, bots_output_format, fopen, bots_arg_file.f, bots_cc, check_village, bots_ld.f, bots_model.f, sim_convalescence_time, res_inside, fscanf, sim_population_ratio, res_checkin, &sim_seed, bots_number_of_tasks, bots_cutoff.f, &res_personnel, bots_comp_message, bots_cc.f, bots_get_load_average, sysconf, bots_resources, bots_name.f, energymonitor__setfilename, sim_realloc_p, free__SC_NPROCESSORS_CONF, bots_ldflags.f, &res_village, sim_village_par, bots_model, energymonitor__trackpoweronly, addList, sim_pid, sim_assess_time, res_personnel, bots_comp_date, bots_comp_message.f, bots_set_info, i, my_print, bots_resources.f, res_waiting, nullCell, &sim_cities, sim_convalescence_p, res_population, top, bots_ldflags, omp_get_max_threads, my_rand, exit, bots_comp_date.f, bots_exec_message, sim_time, uname, omp_unset_lock, omp_init_lock, energymonitor__stopprofiling, vlist, fflush, &res_avg_stay, sprintf, bots_time_sequential, strcpy, bots_print_header, &res_inside, check_patients_assess_par])
        inext = ((void *) 0);
        for (i = sim_cities; i > 0; i--) {
            int32_t city = (int32_t) sim_cities;
            int _imopVarPre130;
            int _imopVarPre131;
            struct Village *_imopVarPre132;
            struct Village {
                int id;
                struct Village *back;
                struct Village *next;
                struct Village *forward;
                struct Patient {
                    int id;
                    int seed;
                    int time;
                    int time_left;
                    int hosps_visited;
                    struct Village *home_village;
                    struct Patient *back;
                    struct Patient *forward;
                } *population;
                struct Hosp {
                    int personnel;
                    int free_personnel;
                    struct Patient {
                        int id;
                        int seed;
                        int time;
                        int time_left;
                        int hosps_visited;
                        struct Village *home_village;
                        struct Patient *back;
                        struct Patient *forward;
                    } *waiting;
                    struct Patient {
                        int id;
                        int seed;
                        int time;
                        int time_left;
                        int hosps_visited;
                        struct Village *home_village;
                        struct Patient *back;
                        struct Patient *forward;
                    } *assess;
                    struct Patient {
                        int id;
                        int seed;
                        int time;
                        int time_left;
                        int hosps_visited;
                        struct Village *home_village;
                        struct Patient *back;
                        struct Patient *forward;
                    } *inside;
                    struct Patient {
                        int id;
                        int seed;
                        int time;
                        int time_left;
                        int hosps_visited;
                        struct Village *home_village;
                        struct Patient *back;
                        struct Patient *forward;
                    } *realloc;
                    struct stUn_imopVarPre30 {
                        unsigned char _x[4];
                    } realloc_lock;
                } hosp;
                int level;
                int seed;
            } **_imopVarPre133;
            _imopVarPre130 = (vid * city) + (int32_t) i;
            _imopVarPre131 = level - 1;
            _imopVarPre132 = *capital;
            _imopVarPre133 = &current;
            allocate_village(_imopVarPre133, _imopVarPre132, inext, _imopVarPre131, _imopVarPre130);
            inext = current;
        }
        (*capital)->forward = current;
    }
}
struct Results get_results(struct Village *village) {
    struct Village *vlist;
    struct Patient *p;
    struct Results t_res;
    struct Results p_res;
    t_res.hosps_number = 0.0;
    t_res.hosps_personnel = 0.0;
    t_res.total_patients = 0.0;
    t_res.total_in_village = 0.0;
    t_res.total_waiting = 0.0;
    t_res.total_assess = 0.0;
    t_res.total_inside = 0.0;
    t_res.total_hosps_v = 0.0;
    t_res.total_time = 0.0;
    if (village == ((void *) 0)) {
        return t_res;
    }
    vlist = village->forward;
    while (vlist) {
        p_res = get_results(vlist);
        t_res.hosps_number += p_res.hosps_number;
        t_res.hosps_personnel += p_res.hosps_personnel;
        t_res.total_patients += p_res.total_patients;
        t_res.total_in_village += p_res.total_in_village;
        t_res.total_waiting += p_res.total_waiting;
        t_res.total_assess += p_res.total_assess;
        t_res.total_inside += p_res.total_inside;
        t_res.total_hosps_v += p_res.total_hosps_v;
        t_res.total_time += p_res.total_time;
        vlist = vlist->next;
    }
    t_res.hosps_number += 1.0;
    t_res.hosps_personnel += village->hosp.personnel;
    p = village->population;
    while (p != ((void *) 0)) {
        t_res.total_patients += 1.0;
        t_res.total_in_village += 1.0;
        t_res.total_hosps_v += (float) (p->hosps_visited);
        t_res.total_time += (float) (p->time);
        p = p->forward;
    }
    p = village->hosp.waiting;
    while (p != ((void *) 0)) {
        t_res.total_patients += 1.0;
        t_res.total_waiting += 1.0;
        t_res.total_hosps_v += (float) (p->hosps_visited);
        t_res.total_time += (float) (p->time);
        p = p->forward;
    }
    p = village->hosp.assess;
    while (p != ((void *) 0)) {
        t_res.total_patients += 1.0;
        t_res.total_assess += 1.0;
        t_res.total_hosps_v += (float) (p->hosps_visited);
        t_res.total_time += (float) (p->time);
        p = p->forward;
    }
    p = village->hosp.inside;
    while (p != ((void *) 0)) {
        t_res.total_patients += 1.0;
        t_res.total_inside += 1.0;
        t_res.total_hosps_v += (float) (p->hosps_visited);
        t_res.total_time += (float) (p->time);
        p = p->forward;
    }
    return t_res;
}
void check_patients_inside(struct Village *village) {
    struct Patient *list = village->hosp.inside;
    struct Patient *p;
    while (list != ((void *) 0)) {
        p = list;
        list = list->forward;
        p->time_left--;
        if (p->time_left == 0) {
            village->hosp.free_personnel++;
            struct Patient {
                int id;
                int seed;
                int time;
                int time_left;
                int hosps_visited;
                struct Village *home_village;
                struct Patient *back;
                struct Patient *forward;
            } **_imopVarPre135;
            _imopVarPre135 = &(village->hosp.inside);
            removeList(_imopVarPre135, p);
            struct Patient {
                int id;
                int seed;
                int time;
                int time_left;
                int hosps_visited;
                struct Village *home_village;
                struct Patient *back;
                struct Patient *forward;
            } **_imopVarPre137;
            _imopVarPre137 = &(village->population);
            addList(_imopVarPre137, p);
        }
    }
}
void check_patients_assess_par(struct Village *village) {
    struct Patient *list = village->hosp.assess;
    float rand;
    struct Patient *p;
    while (list != ((void *) 0)) {
        p = list;
        list = list->forward;
        p->time_left--;
        if (p->time_left == 0) {
            int *_imopVarPre139;
            float _imopVarPre140;
            _imopVarPre139 = &(p->seed);
            _imopVarPre140 = my_rand(_imopVarPre139);
            rand = _imopVarPre140;
            if (rand < sim_convalescence_p) {
                int *_imopVarPre142;
                float _imopVarPre143;
                _imopVarPre142 = &(p->seed);
                _imopVarPre143 = my_rand(_imopVarPre142);
                rand = _imopVarPre143;
                int _imopVarPre144;
                _imopVarPre144 = rand > sim_realloc_p;
                if (!_imopVarPre144) {
                    _imopVarPre144 = village->level == sim_level;
                }
                if (_imopVarPre144) {
                    struct Patient {
                        int id;
                        int seed;
                        int time;
                        int time_left;
                        int hosps_visited;
                        struct Village *home_village;
                        struct Patient *back;
                        struct Patient *forward;
                    } **_imopVarPre146;
                    _imopVarPre146 = &(village->hosp.assess);
                    removeList(_imopVarPre146, p);
                    struct Patient {
                        int id;
                        int seed;
                        int time;
                        int time_left;
                        int hosps_visited;
                        struct Village *home_village;
                        struct Patient *back;
                        struct Patient *forward;
                    } **_imopVarPre148;
                    _imopVarPre148 = &(village->hosp.inside);
                    addList(_imopVarPre148, p);
                    p->time_left = sim_convalescence_time;
                    p->time += p->time_left;
                } else {
                    village->hosp.free_personnel++;
                    struct Patient {
                        int id;
                        int seed;
                        int time;
                        int time_left;
                        int hosps_visited;
                        struct Village *home_village;
                        struct Patient *back;
                        struct Patient *forward;
                    } **_imopVarPre150;
                    _imopVarPre150 = &(village->hosp.assess);
                    removeList(_imopVarPre150, p);
                    struct stUn_imopVarPre30 *_imopVarPre152;
                    _imopVarPre152 = &(village->hosp.realloc_lock);
// #pragma omp dummyFlush LOCK_MODIFY_START written([heapCell#1, sim_seed, sim_pid, bots_exec_message.f, bots_output_format, bots_time_program, heapCell#2, sim_assess_time, res_personnel, bots_comp_message.f, bots_cutoff_value, i, bots_resources.f, sim_get_sick_p, res_waiting, bots_verbose_mode, nullCell, bots_arg_file.f, res_avg_stay, sim_convalescence_p, bots_ld.f, res_population, bots_check_flag, top, res_village, res_assess, bots_parameters.f, bots_model.f, sim_convalescence_time, res_inside, res_hospitals, sim_population_ratio, res_checkin, bots_result, bots_comp_date.f, bots_exec_date.f, sim_time, bots_cutoff.f, bots_cc.f, bots_cflags.f, sim_level, bots_name.f, sim_cities, bots_print_header, sim_realloc_p, bots_ldflags.f, bots_execname.f]) read([omp_set_lock])
                    omp_set_lock(_imopVarPre152);
// #pragma omp dummyFlush LOCK_MODIFY_END written([]) read([addList, omp_unset_lock])
                    struct Village *backvill = village->back;
                    struct Patient {
                        int id;
                        int seed;
                        int time;
                        int time_left;
                        int hosps_visited;
                        struct Village *home_village;
                        struct Patient *back;
                        struct Patient *forward;
                    } **_imopVarPre154;
                    _imopVarPre154 = &(backvill->hosp.realloc);
                    addList(_imopVarPre154, p);
                    struct stUn_imopVarPre30 *_imopVarPre156;
                    _imopVarPre156 = &(village->hosp.realloc_lock);
                    omp_unset_lock(_imopVarPre156);
// #pragma omp dummyFlush LOCK_WRITE_END written([]) read([sim_village_par, heapCell#1, addList, sim_pid, heapCell#2, sim_assess_time, &heapCell#1, i, removeList, sim_get_sick_p, check_patients_waiting, nullCell, top, sim_convalescence_p, top, put_in_hosp, check_patients_population, sim_convalescence_time, my_rand, malloc, sim_time, omp_unset_lock, omp_init_lock, sim_level, vlist, sim_realloc_p, check_patients_inside, _imopVarPre144, check_patients_assess_par])
                }
            } else {
                village->hosp.free_personnel++;
                struct Patient {
                    int id;
                    int seed;
                    int time;
                    int time_left;
                    int hosps_visited;
                    struct Village *home_village;
                    struct Patient *back;
                    struct Patient *forward;
                } **_imopVarPre158;
                _imopVarPre158 = &(village->hosp.assess);
                removeList(_imopVarPre158, p);
                struct Patient {
                    int id;
                    int seed;
                    int time;
                    int time_left;
                    int hosps_visited;
                    struct Village *home_village;
                    struct Patient *back;
                    struct Patient *forward;
                } **_imopVarPre160;
                _imopVarPre160 = &(village->population);
                addList(_imopVarPre160, p);
            }
        }
    }
}
void check_patients_waiting(struct Village *village) {
    struct Patient *list = village->hosp.waiting;
    struct Patient *p;
    while (list != ((void *) 0)) {
        p = list;
        list = list->forward;
        if (village->hosp.free_personnel > 0) {
            village->hosp.free_personnel--;
            p->time_left = sim_assess_time;
            p->time += p->time_left;
            struct Patient {
                int id;
                int seed;
                int time;
                int time_left;
                int hosps_visited;
                struct Village *home_village;
                struct Patient *back;
                struct Patient *forward;
            } **_imopVarPre162;
            _imopVarPre162 = &(village->hosp.waiting);
            removeList(_imopVarPre162, p);
            struct Patient {
                int id;
                int seed;
                int time;
                int time_left;
                int hosps_visited;
                struct Village *home_village;
                struct Patient *back;
                struct Patient *forward;
            } **_imopVarPre164;
            _imopVarPre164 = &(village->hosp.assess);
            addList(_imopVarPre164, p);
        } else {
            p->time++;
        }
    }
}
void check_patients_realloc(struct Village *village) {
    struct Patient *p;
    struct Patient *s;
    while (village->hosp.realloc != ((void *) 0)) {
        p = s = village->hosp.realloc;
        while (p != ((void *) 0)) {
            if (p->id < s->id) {
                s = p;
            }
            p = p->forward;
        }
        struct Patient {
            int id;
            int seed;
            int time;
            int time_left;
            int hosps_visited;
            struct Village *home_village;
            struct Patient *back;
            struct Patient *forward;
        } **_imopVarPre166;
        _imopVarPre166 = &(village->hosp.realloc);
        removeList(_imopVarPre166, s);
        struct Hosp *_imopVarPre168;
        _imopVarPre168 = &(village->hosp);
        put_in_hosp(_imopVarPre168, s);
    }
}
void check_patients_population(struct Village *village) {
    struct Patient *list = village->population;
    struct Patient *p;
    float rand;
    while (list != ((void *) 0)) {
        p = list;
        list = list->forward;
        int *_imopVarPre170;
        float _imopVarPre171;
        _imopVarPre170 = &(p->seed);
        _imopVarPre171 = my_rand(_imopVarPre170);
        rand = _imopVarPre171;
        if (rand < sim_get_sick_p) {
            struct Patient {
                int id;
                int seed;
                int time;
                int time_left;
                int hosps_visited;
                struct Village *home_village;
                struct Patient *back;
                struct Patient *forward;
            } **_imopVarPre173;
            _imopVarPre173 = &(village->population);
            removeList(_imopVarPre173, p);
            struct Hosp *_imopVarPre175;
            _imopVarPre175 = &(village->hosp);
            put_in_hosp(_imopVarPre175, p);
        }
    }
}
void put_in_hosp(struct Hosp *hosp, struct Patient *patient) {
    (patient->hosps_visited)++;
    if (hosp->free_personnel > 0) {
        hosp->free_personnel--;
        struct Patient {
            int id;
            int seed;
            int time;
            int time_left;
            int hosps_visited;
            struct Village *home_village;
            struct Patient *back;
            struct Patient *forward;
        } **_imopVarPre177;
        _imopVarPre177 = &(hosp->assess);
        addList(_imopVarPre177, patient);
        patient->time_left = sim_assess_time;
        patient->time += patient->time_left;
    } else {
        struct Patient {
            int id;
            int seed;
            int time;
            int time_left;
            int hosps_visited;
            struct Village *home_village;
            struct Patient *back;
            struct Patient *forward;
        } **_imopVarPre179;
        _imopVarPre179 = &(hosp->waiting);
        addList(_imopVarPre179, patient);
    }
}
void sim_village_par(struct Village *village) {
    struct Village *vlist;
    if (village == ((void *) 0)) {
        return;
    }
    vlist = village->forward;
    while (vlist) {
// #pragma omp dummyFlush TASK_START written([heapCell#1, sim_seed, sim_pid, bots_exec_message.f, bots_output_format, bots_time_program, heapCell#2, sim_assess_time, res_personnel, bots_comp_message.f, bots_cutoff_value, i, bots_resources.f, sim_get_sick_p, res_waiting, bots_verbose_mode, nullCell, bots_arg_file.f, res_avg_stay, sim_convalescence_p, bots_ld.f, res_population, bots_check_flag, top, res_village, res_assess, bots_parameters.f, bots_model.f, sim_convalescence_time, res_inside, res_hospitals, sim_population_ratio, res_checkin, bots_result, bots_comp_date.f, bots_exec_date.f, sim_time, bots_cutoff.f, bots_cc.f, bots_cflags.f, sim_level, bots_name.f, sim_cities, bots_print_header, sim_realloc_p, bots_ldflags.f, bots_execname.f]) read([sim_village_par, sim_convalescence_time, addList, my_rand, sim_assess_time, bots_cutoff_value, removeList, sim_level, vlist, check_patients_waiting, sim_convalescence_p, sim_realloc_p, check_patients_inside, _imopVarPre144, check_patients_assess_par])
#pragma omp task if((sim_level - village->level) < bots_cutoff_value)
        {
            sim_village_par(vlist);
        }
// #pragma omp dummyFlush TASK_END written([]) read([sim_convalescence_time, addList, my_rand, sim_assess_time, removeList, sim_level, vlist, check_patients_waiting, sim_convalescence_p, sim_realloc_p, check_patients_inside, _imopVarPre144, check_patients_assess_par])
        vlist = vlist->next;
    }
    check_patients_inside(village);
    check_patients_assess_par(village);
    check_patients_waiting(village);
// #pragma omp dummyFlush TASKWAIT_START written([heapCell#1, sim_seed, sim_pid, bots_exec_message.f, bots_output_format, bots_time_program, heapCell#2, sim_assess_time, res_personnel, bots_comp_message.f, bots_cutoff_value, i, bots_resources.f, sim_get_sick_p, res_waiting, bots_verbose_mode, nullCell, bots_arg_file.f, res_avg_stay, sim_convalescence_p, bots_ld.f, res_population, bots_check_flag, top, res_village, res_assess, bots_parameters.f, bots_model.f, sim_convalescence_time, res_inside, res_hospitals, sim_population_ratio, res_checkin, bots_result, bots_comp_date.f, bots_exec_date.f, sim_time, bots_cutoff.f, bots_cc.f, bots_cflags.f, sim_level, bots_name.f, sim_cities, bots_print_header, sim_realloc_p, bots_ldflags.f, bots_execname.f]) read([sim_village_par, heapCell#1, addList, sim_pid, heapCell#2, sim_assess_time, &heapCell#1, i, removeList, sim_get_sick_p, check_patients_waiting, nullCell, top, sim_convalescence_p, top, put_in_hosp, check_patients_population, check_patients_realloc, sim_convalescence_time, my_rand, malloc, sim_time, omp_unset_lock, omp_init_lock, sim_level, vlist, sim_realloc_p, check_patients_inside, _imopVarPre144, check_patients_assess_par])
#pragma omp taskwait
    check_patients_realloc(village);
    check_patients_population(village);
}
void my_print(struct Village *village) {
    struct Village *vlist;
    struct Patient *plist;
    if (village == ((void *) 0)) {
        return;
    }
    vlist = village->forward;
    while (vlist) {
        my_print(vlist);
        vlist = vlist->next;
    }
    plist = village->population;
    while (plist != ((void *) 0)) {
        ;
        plist = plist->forward;
    }
    ;
}
void read_input_data(char *filename) {
    FILE *fin;
    int res;
    struct _IO_FILE *_imopVarPre181;
    _imopVarPre181 = fopen(filename, "r");
    if ((fin = _imopVarPre181) == ((void *) 0)) {
        if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
            fprintf(stdout, "Could not open sequence file (%s)\n", filename);
        }
        ;
        int _imopVarPre183;
        _imopVarPre183 = -1;
        exit(_imopVarPre183);
    }
    float *_imopVarPre203;
    int *_imopVarPre204;
    int *_imopVarPre205;
    int *_imopVarPre206;
    int *_imopVarPre207;
    int *_imopVarPre208;
    int *_imopVarPre209;
    int *_imopVarPre210;
    int *_imopVarPre211;
    float *_imopVarPre212;
    float *_imopVarPre213;
    float *_imopVarPre214;
    int *_imopVarPre215;
    int *_imopVarPre216;
    int *_imopVarPre217;
    int *_imopVarPre218;
    int *_imopVarPre219;
    int *_imopVarPre220;
    int *_imopVarPre221;
    int _imopVarPre222;
    _imopVarPre203 = &res_avg_stay;
    _imopVarPre204 = &res_inside;
    _imopVarPre205 = &res_assess;
    _imopVarPre206 = &res_waiting;
    _imopVarPre207 = &res_village;
    _imopVarPre208 = &res_checkin;
    _imopVarPre209 = &res_personnel;
    _imopVarPre210 = &res_hospitals;
    _imopVarPre211 = &res_population;
    _imopVarPre212 = &sim_realloc_p;
    _imopVarPre213 = &sim_convalescence_p;
    _imopVarPre214 = &sim_get_sick_p;
    _imopVarPre215 = &sim_seed;
    _imopVarPre216 = &sim_convalescence_time;
    _imopVarPre217 = &sim_assess_time;
    _imopVarPre218 = &sim_time;
    _imopVarPre219 = &sim_population_ratio;
    _imopVarPre220 = &sim_cities;
    _imopVarPre221 = &sim_level;
    _imopVarPre222 = fscanf(fin, "%d %d %d %d %d %d %ld %f %f %f %d %d %d %d %d %d %d %d %f", _imopVarPre221, _imopVarPre220, _imopVarPre219, _imopVarPre218, _imopVarPre217, _imopVarPre216, _imopVarPre215, _imopVarPre214, _imopVarPre213, _imopVarPre212, _imopVarPre211, _imopVarPre210, _imopVarPre209, _imopVarPre208, _imopVarPre207, _imopVarPre206, _imopVarPre205, _imopVarPre204, _imopVarPre203);
    res = _imopVarPre222;
    if (res == (-1)) {
        if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
            fprintf(stdout, "Bogus input file (%s)\n", filename);
        }
        ;
        int _imopVarPre224;
        _imopVarPre224 = -1;
        exit(_imopVarPre224);
    }
    fclose(fin);
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        fprintf(stdout, "\n");
    }
    ;
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        int _imopVarPre226;
        _imopVarPre226 = (int) sim_level;
        fprintf(stdout, "Number of levels    = %d\n", _imopVarPre226);
    }
    ;
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        int _imopVarPre228;
        _imopVarPre228 = (int) sim_cities;
        fprintf(stdout, "Cities per level    = %d\n", _imopVarPre228);
    }
    ;
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        int _imopVarPre230;
        _imopVarPre230 = (int) sim_population_ratio;
        fprintf(stdout, "Population ratio    = %d\n", _imopVarPre230);
    }
    ;
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        int _imopVarPre232;
        _imopVarPre232 = (int) sim_time;
        fprintf(stdout, "Simulation time     = %d\n", _imopVarPre232);
    }
    ;
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        int _imopVarPre234;
        _imopVarPre234 = (int) sim_assess_time;
        fprintf(stdout, "Assess time         = %d\n", _imopVarPre234);
    }
    ;
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        int _imopVarPre236;
        _imopVarPre236 = (int) sim_convalescence_time;
        fprintf(stdout, "Convalescence time  = %d\n", _imopVarPre236);
    }
    ;
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        int _imopVarPre238;
        _imopVarPre238 = (int) sim_seed;
        fprintf(stdout, "Initial seed        = %d\n", _imopVarPre238);
    }
    ;
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        float _imopVarPre240;
        _imopVarPre240 = (float) sim_get_sick_p;
        fprintf(stdout, "Get sick prob.      = %f\n", _imopVarPre240);
    }
    ;
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        float _imopVarPre242;
        _imopVarPre242 = (float) sim_convalescence_p;
        fprintf(stdout, "Convalescence prob. = %f\n", _imopVarPre242);
    }
    ;
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        float _imopVarPre244;
        _imopVarPre244 = (float) sim_realloc_p;
        fprintf(stdout, "Realloc prob.       = %f\n", _imopVarPre244);
    }
    ;
}
int check_village(struct Village *top) {
    struct Results _imopVarPre246;
    _imopVarPre246 = get_results(top);
    struct Results result = _imopVarPre246;
    int answer = 1;
    if (res_population != result.total_patients) {
        answer = 2;
    }
    if (res_hospitals != result.hosps_number) {
        answer = 2;
    }
    if (res_personnel != result.hosps_personnel) {
        answer = 2;
    }
    if (res_checkin != result.total_hosps_v) {
        answer = 2;
    }
    if (res_village != result.total_in_village) {
        answer = 2;
    }
    if (res_waiting != result.total_waiting) {
        answer = 2;
    }
    if (res_assess != result.total_assess) {
        answer = 2;
    }
    if (res_inside != result.total_inside) {
        answer = 2;
    }
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        fprintf(stdout, "\n");
    }
    ;
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        fprintf(stdout, "Sim. Variables      = expect / result\n");
    }
    ;
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        int _imopVarPre249;
        int _imopVarPre250;
        _imopVarPre249 = (int) result.total_patients;
        _imopVarPre250 = (int) res_population;
        fprintf(stdout, "Total population    = %6d / %6d people\n", _imopVarPre250, _imopVarPre249);
    }
    ;
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        int _imopVarPre253;
        int _imopVarPre254;
        _imopVarPre253 = (int) result.hosps_number;
        _imopVarPre254 = (int) res_hospitals;
        fprintf(stdout, "Hospitals           = %6d / %6d people\n", _imopVarPre254, _imopVarPre253);
    }
    ;
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        int _imopVarPre257;
        int _imopVarPre258;
        _imopVarPre257 = (int) result.hosps_personnel;
        _imopVarPre258 = (int) res_personnel;
        fprintf(stdout, "Personnel           = %6d / %6d people\n", _imopVarPre258, _imopVarPre257);
    }
    ;
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        int _imopVarPre261;
        int _imopVarPre262;
        _imopVarPre261 = (int) result.total_hosps_v;
        _imopVarPre262 = (int) res_checkin;
        fprintf(stdout, "Check-in's          = %6d / %6d people\n", _imopVarPre262, _imopVarPre261);
    }
    ;
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        int _imopVarPre265;
        int _imopVarPre266;
        _imopVarPre265 = (int) result.total_in_village;
        _imopVarPre266 = (int) res_village;
        fprintf(stdout, "In Villages         = %6d / %6d people\n", _imopVarPre266, _imopVarPre265);
    }
    ;
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        int _imopVarPre269;
        int _imopVarPre270;
        _imopVarPre269 = (int) result.total_waiting;
        _imopVarPre270 = (int) res_waiting;
        fprintf(stdout, "In Waiting List     = %6d / %6d people\n", _imopVarPre270, _imopVarPre269);
    }
    ;
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        int _imopVarPre273;
        int _imopVarPre274;
        _imopVarPre273 = (int) result.total_assess;
        _imopVarPre274 = (int) res_assess;
        fprintf(stdout, "In Assess           = %6d / %6d people\n", _imopVarPre274, _imopVarPre273);
    }
    ;
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        int _imopVarPre277;
        int _imopVarPre278;
        _imopVarPre277 = (int) result.total_inside;
        _imopVarPre278 = (int) res_inside;
        fprintf(stdout, "Inside Hospital     = %6d / %6d people\n", _imopVarPre278, _imopVarPre277);
    }
    ;
    if (bots_verbose_mode >= BOTS_VERBOSE_DEFAULT) {
        float _imopVarPre281;
        float _imopVarPre282;
        _imopVarPre281 = (float) result.total_time / result.total_patients;
        _imopVarPre282 = (float) res_avg_stay;
        fprintf(stdout, "Average Stay        = %6f / %6f u/time\n", _imopVarPre282, _imopVarPre281);
    }
    ;
    my_print(top);
    return answer;
}
void sim_village_main_par(struct Village *top) {
    long i;
#pragma omp parallel
    {
#pragma omp single nowait
        {
// #pragma omp dummyFlush TASK_START written([sim_seed, bots_exec_message.f, bots_output_format, bots_time_program, sim_assess_time, res_personnel, bots_comp_message.f, bots_cutoff_value, bots_resources.f, sim_get_sick_p, res_waiting, bots_verbose_mode, nullCell, bots_arg_file.f, res_avg_stay, sim_convalescence_p, bots_ld.f, res_population, bots_check_flag, top, res_village, res_assess, bots_parameters.f, bots_model.f, sim_convalescence_time, res_inside, res_hospitals, sim_population_ratio, res_checkin, bots_result, bots_comp_date.f, bots_exec_date.f, sim_time, bots_cutoff.f, bots_cc.f, bots_cflags.f, sim_level, bots_name.f, sim_cities, bots_print_header, sim_realloc_p, bots_ldflags.f, bots_execname.f]) read([sim_village_par, sim_convalescence_time, addList, my_rand, sim_time, sim_assess_time, i, removeList, sim_level, vlist, check_patients_waiting, top, sim_convalescence_p, sim_realloc_p, check_patients_inside, _imopVarPre144, check_patients_assess_par])
#pragma omp task
            {
                for (i = 0; i < sim_time; i++) {
                    sim_village_par(top);
                }
            }
// #pragma omp dummyFlush TASK_END written([i]) read([])
// #pragma omp dummyFlush TASK_START written([]) read([])
        }
// #pragma omp dummyFlush BARRIER_START written([]) read([&sim_convalescence_time, sim_seed, bots_exec_date, bots_exec_message.f, &res_checkin, heapCell#2, &sim_level, removeList, bots_verbose_mode, bots_usecs, check_patients_waiting, top, &sim_realloc_p, res_assess, bots_parameters.f, heapCell#0, bots_result, bots_exec_date.f, &sim_assess_time, &sim_get_sick_p, bots_sequential_flag, fprintf, bots_get_architecture, check_patients_inside, &res_waiting, _imopVarPre144, bots_parameters, heapCell#1, &sim_population_ratio, &sim_time, bots_error, atoi, &heapCell#1, bots_cutoff_value, bots_execname, stderr, vlist, sim_get_sick_p, free_BOTS_VERBOSE_DEFAULT, bots_name, res_avg_stay, vlist, read_input_data, bots_check_flag, put_in_hosp, res_village, get_results, getloadavg, check_patients_population, sim_village_main_par, res_hospitals, malloc, fclose, bots_cflags, bots_print_results, bots_cflags.f, sim_level, pow, energymonitor__startprofiling, bots_arg_file, energymonitor__init, allocate_village, sim_cities, snprintf, bots_print_usage, &res_population, error, gettimeofday, &sim_convalescence_p, bots_ld, bots_execname.f, &res_hospitals, &res_assess, stdout, bots_cutoff, bots_time_program, bots_output_format, fopen, bots_arg_file.f, bots_cc, check_village, bots_ld.f, bots_model.f, sim_convalescence_time, res_inside, fscanf, sim_population_ratio, res_checkin, &sim_seed, bots_number_of_tasks, bots_cutoff.f, &res_personnel, bots_comp_message, bots_cc.f, bots_get_load_average, sysconf, bots_resources, bots_name.f, energymonitor__setfilename, sim_realloc_p, free__SC_NPROCESSORS_CONF, bots_ldflags.f, &res_village, sim_village_par, bots_model, energymonitor__trackpoweronly, addList, sim_pid, sim_assess_time, res_personnel, bots_comp_date, bots_comp_message.f, bots_set_info, i, my_print, bots_resources.f, res_waiting, nullCell, &sim_cities, sim_convalescence_p, res_population, top, bots_ldflags, omp_get_max_threads, my_rand, exit, bots_comp_date.f, bots_exec_message, sim_time, uname, omp_unset_lock, omp_init_lock, energymonitor__stopprofiling, vlist, fflush, &res_avg_stay, sprintf, bots_time_sequential, strcpy, bots_print_header, &res_inside, check_patients_assess_par])
#pragma omp barrier
    }
}
int bots_sequential_flag = 0;
int bots_check_flag = 0;
bots_verbose_mode_t bots_verbose_mode = BOTS_VERBOSE_DEFAULT;
int bots_result = 3;
int bots_output_format = 1;
int bots_print_header = 0;
char bots_name[256];
char bots_execname[256];
char bots_parameters[256];
char bots_model[256];
char bots_resources[256];
char bots_exec_date[256];
char bots_exec_message[256];
char bots_comp_date[256];
char bots_comp_message[256];
char bots_cc[256];
char bots_cflags[256];
char bots_ld[256];
char bots_ldflags[256];
char bots_cutoff[256];
double bots_time_program = 0.0;
double bots_time_sequential = 0.0;
unsigned long long bots_number_of_tasks = 0;
char bots_arg_file[255] = "";
int bots_cutoff_value = 2;
void bots_print_usage() {
    fprintf(stderr, "\n");
    fprintf(stderr, "Usage: %s -[options]\n", bots_execname);
    fprintf(stderr, "\n");
    fprintf(stderr, "Where options are:\n");
    fprintf(stderr, "  -f <file>  : " "Health input file (mandatory)" "\n");
    fprintf(stderr, "  -x <value> : OpenMP tasks cut-off value (default=%d)\n", 2);
    fprintf(stderr, "\n");
    fprintf(stderr, "  -e <str>   : Include 'str' execution message.\n");
    fprintf(stderr, "  -v <level> : Set verbose level (default = 1).\n");
    fprintf(stderr, "               0 - none.\n");
    fprintf(stderr, "               1 - default.\n");
    fprintf(stderr, "               2 - debug.\n");
    fprintf(stderr, "  -o <value> : Set output format mode (default = 1).\n");
    fprintf(stderr, "               0 - no benchmark output.\n");
    fprintf(stderr, "               1 - detailed list format.\n");
    fprintf(stderr, "               2 - detailed row format.\n");
    fprintf(stderr, "               3 - abridged list format.\n");
    fprintf(stderr, "               4 - abridged row format.\n");
    fprintf(stderr, "  -z         : Print row header (if output format is a row variant).\n");
    fprintf(stderr, "\n");
    fprintf(stderr, "  -c         : Check mode ON.\n");
    fprintf(stderr, "\n");
    fprintf(stderr, "  -h         : Print program's usage (this help).\n");
    fprintf(stderr, "\n");
}
void bots_get_params_common(int argc, char **argv) {
    int i;
    char *_imopVarPre287;
    char *_imopVarPre288;
    _imopVarPre287 = argv[0];
    _imopVarPre288 = __xpg_basename(_imopVarPre287);
    strcpy(bots_execname, _imopVarPre288);
    bots_get_date(bots_exec_date);
    strcpy(bots_exec_message, "");
    for (i = 1; i < argc; i++) {
        if (argv[i][0] == '-') {
            switch (argv[i][1]) {
                case 'c': argv[i][1] = '*';
                bots_check_flag = 1;
                break;
                case 'e': argv[i][1] = '*';
                i++;
                if (argc == i) {
                    bots_print_usage();
                    exit(100);
                }
                char *_imopVarPre290;
                _imopVarPre290 = argv[i];
                strcpy(bots_exec_message, _imopVarPre290);
                break;
                case 'f': argv[i][1] = '*';
                i++;
                if (argc == i) {
                    bots_print_usage();
                    exit(100);
                }
                char *_imopVarPre292;
                _imopVarPre292 = argv[i];
                strcpy(bots_arg_file, _imopVarPre292);
                break;
                case 'h': argv[i][1] = '*';
                bots_print_usage();
                exit(100);
                case 'o': argv[i][1] = '*';
                i++;
                if (argc == i) {
                    bots_print_usage();
                    exit(100);
                }
                char *_imopVarPre294;
                int _imopVarPre295;
                _imopVarPre294 = argv[i];
                _imopVarPre295 = atoi(_imopVarPre294);
                bots_output_format = _imopVarPre295;
                break;
                case 'v': argv[i][1] = '*';
                i++;
                if (argc == i) {
                    bots_print_usage();
                    exit(100);
                }
                char *_imopVarPre298;
                int _imopVarPre299;
                _imopVarPre298 = argv[i];
                _imopVarPre299 = atoi(_imopVarPre298);
                bots_verbose_mode = (bots_verbose_mode_t) _imopVarPre299;
                if (bots_verbose_mode > 1) {
                    fprintf(stderr, "Error: Configure the suite using '--debug' option in order to use a verbose level greather than 1.\n");
                    exit(100);
                }
                break;
                case 'x': argv[i][1] = '*';
                i++;
                if (argc == i) {
                    bots_print_usage();
                    exit(100);
                }
                char *_imopVarPre301;
                int _imopVarPre302;
                _imopVarPre301 = argv[i];
                _imopVarPre302 = atoi(_imopVarPre301);
                bots_cutoff_value = _imopVarPre302;
                break;
                case 'z': argv[i][1] = '*';
                bots_print_header = 1;
                break;
                default: fprintf(stderr, "Error: Unrecognized parameter.\n");
                bots_print_usage();
                exit(100);
            }
        } else {
            fprintf(stderr, "Error: Unrecognized parameter.\n");
            bots_print_usage();
            exit(100);
        }
    }
}
void bots_get_params(int argc, char **argv) {
    bots_get_params_common(argc, argv);
}
void bots_set_info() {
    snprintf(bots_name, 256, "Health");
    snprintf(bots_parameters, 256, "%s", bots_arg_file);
    snprintf(bots_model, 256, "OpenMP (using tasks)");
    int _imopVarPre304;
    _imopVarPre304 = omp_get_max_threads();
    snprintf(bots_resources, 256, "%d", _imopVarPre304);
    snprintf(bots_comp_date, 256, "7MAY2018");
    snprintf(bots_comp_message, 256, "bots");
    snprintf(bots_cc, 256, "gcc");
    snprintf(bots_cflags, 256, "-fopenmp");
    snprintf(bots_ld, 256, "gcc");
    snprintf(bots_ldflags, 256, "-lm");
    snprintf(bots_cutoff, 256, "pragma-if (%d)", bots_cutoff_value);
}
int main(int argc, char *argv[]) {
    long bots_t_start;
    long bots_t_end;
    bots_get_params(argc, argv);
    struct Village *top;
    read_input_data(bots_arg_file);
    ;
    bots_set_info();
    void *_imopVarPre308;
    void *_imopVarPre309;
    struct Village {
        int id;
        struct Village *back;
        struct Village *next;
        struct Village *forward;
        struct Patient {
            int id;
            int seed;
            int time;
            int time_left;
            int hosps_visited;
            struct Village *home_village;
            struct Patient *back;
            struct Patient *forward;
        } *population;
        struct Hosp {
            int personnel;
            int free_personnel;
            struct Patient {
                int id;
                int seed;
                int time;
                int time_left;
                int hosps_visited;
                struct Village *home_village;
                struct Patient *back;
                struct Patient *forward;
            } *waiting;
            struct Patient {
                int id;
                int seed;
                int time;
                int time_left;
                int hosps_visited;
                struct Village *home_village;
                struct Patient *back;
                struct Patient *forward;
            } *assess;
            struct Patient {
                int id;
                int seed;
                int time;
                int time_left;
                int hosps_visited;
                struct Village *home_village;
                struct Patient *back;
                struct Patient *forward;
            } *inside;
            struct Patient {
                int id;
                int seed;
                int time;
                int time_left;
                int hosps_visited;
                struct Village *home_village;
                struct Patient *back;
                struct Patient *forward;
            } *realloc;
            struct stUn_imopVarPre30 {
                unsigned char _x[4];
            } realloc_lock;
        } hosp;
        int level;
        int seed;
    } **_imopVarPre310;
    _imopVarPre308 = ((void *) 0);
    _imopVarPre309 = ((void *) 0);
    _imopVarPre310 = &top;
    allocate_village(_imopVarPre310, _imopVarPre309, _imopVarPre308, sim_level, 0);
    ;
    int cores = 255;
    energymonitor__setfilename("prof.csv");
    energymonitor__init(cores, 1);
    energymonitor__trackpoweronly();
    energymonitor__startprofiling();
    bots_t_start = bots_usecs();
    sim_village_main_par(top);
    ;
    bots_t_end = bots_usecs();
    energymonitor__stopprofiling();
    bots_time_program = ((double) (bots_t_end - bots_t_start)) / 1000000;
    ;
    if (bots_check_flag) {
        bots_result = check_village(top);
        ;
    }
    ;
    bots_print_results();
    return 0;
}
void bots_error(int error, char *message) {
    if (message == ((void *) 0)) {
        switch (error) {
            case 0: fprintf(stderr, "Error (%d): %s\n", error, "Unspecified error.");
            break;
            case 1: fprintf(stderr, "Error (%d): %s\n", error, "Not enough memory.");
            break;
            case 2: fprintf(stderr, "Error (%d): %s\n", error, "Unrecognized parameter.");
            bots_print_usage();
            break;
            default: fprintf(stderr, "Error (%d): %s\n", error, "Invalid error code.");
            break;
        }
    } else {
        fprintf(stderr, "Error (%d): %s\n", error, message);
    }
    int _imopVarPre312;
    _imopVarPre312 = 100 + error;
    exit(_imopVarPre312);
}
long bots_usecs(void ) {
    struct timeval t;
    void *_imopVarPre315;
    struct timeval *_imopVarPre316;
    _imopVarPre315 = ((void *) 0);
    _imopVarPre316 = &t;
    gettimeofday(_imopVarPre316, _imopVarPre315);
    return t.tv_sec * 1000000 + t.tv_usec;
}
void bots_get_date(char *str) {
    time_t now;
    signed long int *_imopVarPre318;
    _imopVarPre318 = &now;
    time(_imopVarPre318);
    signed long int *_imopVarPre323;
    struct tm *_imopVarPre324;
    _imopVarPre323 = &now;
    _imopVarPre324 = gmtime(_imopVarPre323);
    strftime(str, 32, "%Y/%m/%d;%H:%M", _imopVarPre324);
}
void bots_get_architecture(char *str) {
    signed long int _imopVarPre326;
    _imopVarPre326 = sysconf(_SC_NPROCESSORS_CONF);
    int ncpus = _imopVarPre326;
    struct utsname architecture;
    struct utsname *_imopVarPre328;
    _imopVarPre328 = &architecture;
    uname(_imopVarPre328);
    char ( *_imopVarPre331 );
    char ( *_imopVarPre332 );
    _imopVarPre331 = architecture.machine;
    _imopVarPre332 = architecture.sysname;
    snprintf(str, 256, "%s-%s;%d", _imopVarPre332, _imopVarPre331, ncpus);
}
void bots_get_load_average(char *str) {
    double loadavg[3];
    getloadavg(loadavg, 3);
    double _imopVarPre336;
    double _imopVarPre337;
    double _imopVarPre338;
    _imopVarPre336 = loadavg[2];
    _imopVarPre337 = loadavg[1];
    _imopVarPre338 = loadavg[0];
    snprintf(str, 256, "%.2f;%.2f;%.2f", _imopVarPre338, _imopVarPre337, _imopVarPre336);
}
void bots_print_results() {
    char str_name[256];
    char str_parameters[256];
    char str_model[256];
    char str_resources[256];
    char str_result[15];
    char str_time_program[15];
    char str_time_sequential[15];
    char str_speed_up[15];
    char str_number_of_tasks[15];
    char str_number_of_tasks_per_second[15];
    char str_exec_date[256];
    char str_exec_message[256];
    char str_architecture[256];
    char str_load_avg[256];
    char str_comp_date[256];
    char str_comp_message[256];
    char str_cc[256];
    char str_cflags[256];
    char str_ld[256];
    char str_ldflags[256];
    char str_cutoff[256];
    sprintf(str_name, "%s", bots_name);
    sprintf(str_parameters, "%s", bots_parameters);
    sprintf(str_model, "%s", bots_model);
    sprintf(str_cutoff, "%s", bots_cutoff);
    sprintf(str_resources, "%s", bots_resources);
    switch (bots_result) {
        case 0: sprintf(str_result, "n/a");
        break;
        case 1: sprintf(str_result, "successful");
        break;
        case 2: sprintf(str_result, "UNSUCCESSFUL");
        break;
        case 3: sprintf(str_result, "Not requested");
        break;
        default: sprintf(str_result, "error");
        break;
    }
    sprintf(str_time_program, "%f", bots_time_program);
    if (bots_sequential_flag) {
        sprintf(str_time_sequential, "%f", bots_time_sequential);
    } else {
        sprintf(str_time_sequential, "n/a");
    }
    if (bots_sequential_flag) {
        double _imopVarPre340;
        _imopVarPre340 = bots_time_sequential / bots_time_program;
        sprintf(str_speed_up, "%3.2f", _imopVarPre340);
    } else {
        sprintf(str_speed_up, "n/a");
    }
    float _imopVarPre342;
    _imopVarPre342 = (float) bots_number_of_tasks;
    sprintf(str_number_of_tasks, "%3.2f", _imopVarPre342);
    double _imopVarPre344;
    _imopVarPre344 = (float) bots_number_of_tasks / bots_time_program;
    sprintf(str_number_of_tasks_per_second, "%3.2f", _imopVarPre344);
    sprintf(str_exec_date, "%s", bots_exec_date);
    sprintf(str_exec_message, "%s", bots_exec_message);
    bots_get_architecture(str_architecture);
    bots_get_load_average(str_load_avg);
    sprintf(str_comp_date, "%s", bots_comp_date);
    sprintf(str_comp_message, "%s", bots_comp_message);
    sprintf(str_cc, "%s", bots_cc);
    sprintf(str_cflags, "%s", bots_cflags);
    sprintf(str_ld, "%s", bots_ld);
    sprintf(str_ldflags, "%s", bots_ldflags);
    if (bots_print_header) {
        switch (bots_output_format) {
            case 0: break;
            case 1: break;
            case 2: fprintf(stdout, "Benchmark;Parameters;Model;Cutoff;Resources;Result;Time;Sequential;Speed-up;Nodes;Nodes/Sec;Exec Date;Exec Time;Exec Message;Architecture;Processors;Load Avg-1;Load Avg-5;Load Avg-15;Comp Date;Comp Time;Comp Message;CC;CFLAGS;LD;LDFLAGS\n");
            break;
            case 3: break;
            case 4: fprintf(stdout, "Benchmark;Parameters;Model;Cutoff;Resources;Result;Time;Sequential;Speed-up;Nodes;Nodes/Sec;\n");
            break;
            default: break;
        }
    }
    switch (bots_output_format) {
        case 0: break;
        case 1: fprintf(stdout, "\n");
        fprintf(stdout, "Program             = %s\n", str_name);
        fprintf(stdout, "Parameters          = %s\n", str_parameters);
        fprintf(stdout, "Model               = %s\n", str_model);
        fprintf(stdout, "Embedded cut-off    = %s\n", str_cutoff);
        fprintf(stdout, "# of Threads        = %s\n", str_resources);
        fprintf(stdout, "Verification        = %s\n", str_result);
        fprintf(stdout, "Time Program        = %s seconds\n", str_time_program);
        if (bots_sequential_flag) {
            fprintf(stdout, "Time Sequential     = %s seconds\n", str_time_sequential);
            fprintf(stdout, "Speed-up            = %s\n", str_speed_up);
        }
        if (bots_number_of_tasks > 0) {
            fprintf(stdout, "Nodes               = %s\n", str_number_of_tasks);
            fprintf(stdout, "Nodes/Sec           = %s\n", str_number_of_tasks_per_second);
        }
        fprintf(stdout, "Execution Date      = %s\n", str_exec_date);
        fprintf(stdout, "Execution Message   = %s\n", str_exec_message);
        fprintf(stdout, "Architecture        = %s\n", str_architecture);
        fprintf(stdout, "Load Avg [1:5:15]   = %s\n", str_load_avg);
        fprintf(stdout, "Compilation Date    = %s\n", str_comp_date);
        fprintf(stdout, "Compilation Message = %s\n", str_comp_message);
        fprintf(stdout, "Compiler            = %s\n", str_cc);
        fprintf(stdout, "Compiler Flags      = %s\n", str_cflags);
        fprintf(stdout, "Linker              = %s\n", str_ld);
        fprintf(stdout, "Linker Flags        = %s\n", str_ldflags);
        fflush(stdout);
        break;
        case 2: fprintf(stdout, "%s;%s;%s;%s;%s;%s;", str_name, str_parameters, str_model, str_cutoff, str_resources, str_result);
        fprintf(stdout, "%s;%s;%s;", str_time_program, str_time_sequential, str_speed_up);
        fprintf(stdout, "%s;%s;", str_number_of_tasks, str_number_of_tasks_per_second);
        fprintf(stdout, "%s;%s;", str_exec_date, str_exec_message);
        fprintf(stdout, "%s;%s;", str_architecture, str_load_avg);
        fprintf(stdout, "%s;%s;", str_comp_date, str_comp_message);
        fprintf(stdout, "%s;%s;%s;%s;", str_cc, str_cflags, str_ld, str_ldflags);
        fprintf(stdout, "\n");
        break;
        case 3: fprintf(stdout, "\n");
        fprintf(stdout, "Program             = %s\n", str_name);
        fprintf(stdout, "Parameters          = %s\n", str_parameters);
        fprintf(stdout, "Model               = %s\n", str_model);
        fprintf(stdout, "Embedded cut-off    = %s\n", str_cutoff);
        fprintf(stdout, "# of Threads        = %s\n", str_resources);
        fprintf(stdout, "Verification        = %s\n", str_result);
        fprintf(stdout, "Time Program        = %s seconds\n", str_time_program);
        if (bots_sequential_flag) {
            fprintf(stdout, "Time Sequential     = %s seconds\n", str_time_sequential);
            fprintf(stdout, "Speed-up            = %s\n", str_speed_up);
        }
        if (bots_number_of_tasks > 0) {
            fprintf(stdout, "Nodes               = %s\n", str_number_of_tasks);
            fprintf(stdout, "Nodes/Sec           = %s\n", str_number_of_tasks_per_second);
        }
        break;
        case 4: fprintf(stdout, "%s;%s;%s;%s;%s;%s;", str_name, str_parameters, str_model, str_cutoff, str_resources, str_result);
        fprintf(stdout, "%s;%s;%s;", str_time_program, str_time_sequential, str_speed_up);
        fprintf(stdout, "%s;%s;", str_number_of_tasks, str_number_of_tasks_per_second);
        fprintf(stdout, "\n");
        break;
        default: bots_error(0, "No valid output format\n");
        break;
    }
}
