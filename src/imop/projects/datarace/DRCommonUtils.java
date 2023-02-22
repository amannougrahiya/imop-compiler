/*
 * Copyright (c) 2019 Aman Nougrahiya, V Krishna Nandivada, IIT Madras.
 * This file is a part of the project IMOP, licensed under the MIT license.
 * See LICENSE.md for the full text of the license.
 * 
 * The above notice shall be included in all copies or substantial
 * portions of this file.
 */
package imop.projects.datarace;

import java.io.FileNotFoundException;
import java.util.Vector;

import imop.ast.node.Node;
import imop.lib.getter.LineNumGetter;
import imop.lib.util.Misc;
import imop.parser.ParseException;

public class DRCommonUtils extends Misc {

	public DRCommonUtils() {
	}

	/**
	 * This method returns the line number of the first token
	 * encountered in the node n.
	 * 
	 * @param n
	 * @return
	 */
	public static int getLineNum(Node n) {
		LineNumGetter lineGetter = new LineNumGetter();
		n.accept(lineGetter);
		return lineGetter.lineNum;
	}

	/**
	 * This method takes an access list of variables accessed, and the location
	 * of access.
	 * Returns a Statement which prints a log of these accesses.
	 * Format of the log (all entries are tab separated):::
	 * 
	 * location mode phaseNumber linenumber locationString
	 * 
	 * @param accessList
	 * @param location
	 */
	public String getPrintLog(Vector<Access> accessList, int lineNumber) {
		String result = new String();
		for (Access acc : accessList) {
			result += "fprintf(fpTemp[omp_get_thread_num()], \"%p\\t%c\\t%d\\t%d\\t%s\\n\"";
			result += ", (void*)&" + acc.location;
			result += ", " + ((acc.mode == 0) ? "\'r\'" : "\'w\'");
			result += ", phaseCounter";
			result += ", " + lineNumber;
			result += ", \"" + putEscapesRemoveSpaces(acc.location) + "\"";
			result += ");\n";	// Real \n here
		}
		return result;
	}

	/**
	 * This method returns the initial external declarations to be added,
	 * which are obtained upon:
	 * 1.) preprocessing o omp.h
	 * 2.) preprocessing of stdio.h
	 * 3.) declaration of FILE *fp;
	 * 4.) declaration of int phaseCounter;
	 *
	 * The preprocessed text is hardcoded in the return, as it will rarely
	 * change.
	 * 
	 * @return
	 * @throws ParseException
	 * @throws FileNotFoundException
	 */
	public String getHeader(int numThreads) throws FileNotFoundException, ParseException {
		String result = "typedef struct\n{\n  unsigned char _x[64]\n    __attribute__((__aligned__(8)));\n} omp_lock_t;\ntypedef struct\n{\n  unsigned char _x[80]\n    __attribute__((__aligned__(8)));\n} omp_nest_lock_t;\ntypedef enum omp_sched_t\n{\n  omp_sched_static = 1,\n  omp_sched_dynamic = 2,\n  omp_sched_guided = 3,\n  omp_sched_auto = 4\n} omp_sched_t;\nextern void omp_set_num_threads (int) __attribute__((__nothrow__));\nextern int omp_get_num_threads (void) __attribute__((__nothrow__));\nextern int omp_get_max_threads (void) __attribute__((__nothrow__));\nextern int omp_get_thread_num (void) __attribute__((__nothrow__));\nextern int omp_get_num_procs (void) __attribute__((__nothrow__));\nextern int omp_in_parallel (void) __attribute__((__nothrow__));\nextern void omp_set_dynamic (int) __attribute__((__nothrow__));\nextern int omp_get_dynamic (void) __attribute__((__nothrow__));\nextern void omp_set_nested (int) __attribute__((__nothrow__));\nextern int omp_get_nested (void) __attribute__((__nothrow__));\nextern void omp_init_lock (omp_lock_t *) __attribute__((__nothrow__));\nextern void omp_destroy_lock (omp_lock_t *) __attribute__((__nothrow__));\nextern void omp_set_lock (omp_lock_t *) __attribute__((__nothrow__));\nextern void omp_unset_lock (omp_lock_t *) __attribute__((__nothrow__));\nextern int omp_test_lock (omp_lock_t *) __attribute__((__nothrow__));\nextern void omp_init_nest_lock (omp_nest_lock_t *) __attribute__((__nothrow__));\nextern void omp_destroy_nest_lock (omp_nest_lock_t *) __attribute__((__nothrow__));\nextern void omp_set_nest_lock (omp_nest_lock_t *) __attribute__((__nothrow__));\nextern void omp_unset_nest_lock (omp_nest_lock_t *) __attribute__((__nothrow__));\nextern int omp_test_nest_lock (omp_nest_lock_t *) __attribute__((__nothrow__));\nextern double omp_get_wtime (void) __attribute__((__nothrow__));\nextern double omp_get_wtick (void) __attribute__((__nothrow__));\nvoid omp_set_schedule (omp_sched_t, int) __attribute__((__nothrow__));\nvoid omp_get_schedule (omp_sched_t *, int *) __attribute__((__nothrow__));\nint omp_get_thread_limit (void) __attribute__((__nothrow__));\nvoid omp_set_max_active_levels (int) __attribute__((__nothrow__));\nint omp_get_max_active_levels (void) __attribute__((__nothrow__));\nint omp_get_level (void) __attribute__((__nothrow__));\nint omp_get_ancestor_thread_num (int) __attribute__((__nothrow__));\nint omp_get_team_size (int) __attribute__((__nothrow__));\nint omp_get_active_level (void) __attribute__((__nothrow__));\nint omp_in_final (void) __attribute__((__nothrow__));\ntypedef signed char __int8_t;\ntypedef unsigned char __uint8_t;\ntypedef short __int16_t;\ntypedef unsigned short __uint16_t;\ntypedef int __int32_t;\ntypedef unsigned int __uint32_t;\ntypedef long long __int64_t;\ntypedef unsigned long long __uint64_t;\ntypedef long __darwin_intptr_t;\ntypedef unsigned int __darwin_natural_t;\ntypedef int __darwin_ct_rune_t;\ntypedef union {\n char __mbstate8[128];\n long long _mbstateL;\n} __mbstate_t;\ntypedef __mbstate_t __darwin_mbstate_t;\ntypedef long int __darwin_ptrdiff_t;\ntypedef long unsigned int __darwin_size_t;\ntypedef __builtin_va_list __darwin_va_list;\ntypedef int __darwin_wchar_t;\ntypedef __darwin_wchar_t __darwin_rune_t;\ntypedef int __darwin_wint_t;\ntypedef unsigned long __darwin_clock_t;\ntypedef __uint32_t __darwin_socklen_t;\ntypedef long __darwin_ssize_t;\ntypedef long __darwin_time_t;\nstruct __darwin_pthread_handler_rec\n{\n void (*__routine)(void *);\n void *__arg;\n struct __darwin_pthread_handler_rec *__next;\n};\nstruct _opaque_pthread_attr_t { long __sig; char __opaque[56]; };\nstruct _opaque_pthread_cond_t { long __sig; char __opaque[40]; };\nstruct _opaque_pthread_condattr_t { long __sig; char __opaque[8]; };\nstruct _opaque_pthread_mutex_t { long __sig; char __opaque[56]; };\nstruct _opaque_pthread_mutexattr_t { long __sig; char __opaque[8]; };\nstruct _opaque_pthread_once_t { long __sig; char __opaque[8]; };\nstruct _opaque_pthread_rwlock_t { long __sig; char __opaque[192]; };\nstruct _opaque_pthread_rwlockattr_t { long __sig; char __opaque[16]; };\nstruct _opaque_pthread_t { long __sig; struct __darwin_pthread_handler_rec *__cleanup_stack; char __opaque[1168]; };\ntypedef __int64_t __darwin_blkcnt_t;\ntypedef __int32_t __darwin_blksize_t;\ntypedef __int32_t __darwin_dev_t;\ntypedef unsigned int __darwin_fsblkcnt_t;\ntypedef unsigned int __darwin_fsfilcnt_t;\ntypedef __uint32_t __darwin_gid_t;\ntypedef __uint32_t __darwin_id_t;\ntypedef __uint64_t __darwin_ino64_t;\ntypedef __darwin_ino64_t __darwin_ino_t;\ntypedef __darwin_natural_t __darwin_mach_port_name_t;\ntypedef __darwin_mach_port_name_t __darwin_mach_port_t;\ntypedef __uint16_t __darwin_mode_t;\ntypedef __int64_t __darwin_off_t;\ntypedef __int32_t __darwin_pid_t;\ntypedef struct _opaque_pthread_attr_t\n   __darwin_pthread_attr_t;\ntypedef struct _opaque_pthread_cond_t\n   __darwin_pthread_cond_t;\ntypedef struct _opaque_pthread_condattr_t\n   __darwin_pthread_condattr_t;\ntypedef unsigned long __darwin_pthread_key_t;\ntypedef struct _opaque_pthread_mutex_t\n   __darwin_pthread_mutex_t;\ntypedef struct _opaque_pthread_mutexattr_t\n   __darwin_pthread_mutexattr_t;\ntypedef struct _opaque_pthread_once_t\n   __darwin_pthread_once_t;\ntypedef struct _opaque_pthread_rwlock_t\n   __darwin_pthread_rwlock_t;\ntypedef struct _opaque_pthread_rwlockattr_t\n   __darwin_pthread_rwlockattr_t;\ntypedef struct _opaque_pthread_t\n   *__darwin_pthread_t;\ntypedef __uint32_t __darwin_sigset_t;\ntypedef __int32_t __darwin_suseconds_t;\ntypedef __uint32_t __darwin_uid_t;\ntypedef __uint32_t __darwin_useconds_t;\ntypedef unsigned char __darwin_uuid_t[16];\ntypedef char __darwin_uuid_string_t[37];\ntypedef int __darwin_nl_item;\ntypedef int __darwin_wctrans_t;\ntypedef __uint32_t __darwin_wctype_t;\ntypedef __darwin_va_list va_list;\ntypedef __darwin_size_t size_t;\ntypedef __darwin_off_t fpos_t;\nstruct __sbuf {\n unsigned char *_base;\n int _size;\n};\nstruct __sFILEX;\ntypedef struct __sFILE {\n unsigned char *_p;\n int _r;\n int _w;\n short _flags;\n short _file;\n struct __sbuf _bf;\n int _lbfsize;\n void *_cookie;\n int (*_close)(void *);\n int (*_read) (void *, char *, int);\n fpos_t (*_seek) (void *, fpos_t, int);\n int (*_write)(void *, const char *, int);\n struct __sbuf _ub;\n struct __sFILEX *_extra;\n int _ur;\n unsigned char _ubuf[3];\n unsigned char _nbuf[1];\n struct __sbuf _lb;\n int _blksize;\n fpos_t _offset;\n} FILE;\n\nextern FILE *__stdinp;\nextern FILE *__stdoutp;\nextern FILE *__stderrp;\n\n\nvoid clearerr(FILE *);\nint fclose(FILE *);\nint feof(FILE *);\nint ferror(FILE *);\nint fflush(FILE *);\nint fgetc(FILE *);\nint fgetpos(FILE * , fpos_t *);\nchar *fgets(char * , int, FILE *);\nFILE *fopen(const char * , const char * ) __asm(\"_\" \"fopen\" );\nint fprintf(FILE * , const char * , ...) __attribute__((__format__ (__printf__, 2, 3)));\nint fputc(int, FILE *);\nint fputs(const char * , FILE * ) __asm(\"_\" \"fputs\" );\nsize_t fread(void * , size_t, size_t, FILE * );\nFILE *freopen(const char * , const char * ,\n                 FILE * ) __asm(\"_\" \"freopen\" );\nint fscanf(FILE * , const char * , ...) __attribute__((__format__ (__scanf__, 2, 3)));\nint fseek(FILE *, long, int);\nint fsetpos(FILE *, const fpos_t *);\nlong ftell(FILE *);\nsize_t fwrite(const void * , size_t, size_t, FILE * ) __asm(\"_\" \"fwrite\" );\nint getc(FILE *);\nint getchar(void);\nchar *gets(char *);\nvoid perror(const char *);\nint printf(const char * , ...) __attribute__((__format__ (__printf__, 1, 2)));\nint putc(int, FILE *);\nint putchar(int);\nint puts(const char *);\nint remove(const char *);\nint rename (const char *, const char *);\nvoid rewind(FILE *);\nint scanf(const char * , ...) __attribute__((__format__ (__scanf__, 1, 2)));\nvoid setbuf(FILE * , char * );\nint setvbuf(FILE * , char * , int, size_t);\nint sprintf(char * , const char * , ...) __attribute__((__format__ (__printf__, 2, 3)));\nint sscanf(const char * , const char * , ...) __attribute__((__format__ (__scanf__, 2, 3)));\nFILE *tmpfile(void);\nchar *tmpnam(char *);\nint ungetc(int, FILE *);\nint vfprintf(FILE * , const char * , va_list) __attribute__((__format__ (__printf__, 2, 0)));\nint vprintf(const char * , va_list) __attribute__((__format__ (__printf__, 1, 0)));\nint vsprintf(char * , const char * , va_list) __attribute__((__format__ (__printf__, 2, 0)));\n\n\nchar *ctermid(char *);\nFILE *fdopen(int, const char *) __asm(\"_\" \"fdopen\" );\nint fileno(FILE *);\n\n\nint pclose(FILE *);\nFILE *popen(const char *, const char *) __asm(\"_\" \"popen\" );\n\n\nint __srget(FILE *);\nint __svfscanf(FILE *, const char *, va_list) __attribute__((__format__ (__scanf__, 2, 0)));\nint __swbuf(int, FILE *);\n\nstatic __inline int __sputc(int _c, FILE *_p) {\n if (--_p::=_w >= 0 || (_p::=_w >= _p::=_lbfsize && (char)_c != \'\\n\'))\n  return (*_p::=_p++ = _c);\n else\n  return (__swbuf(_c, _p));\n}\n\nvoid flockfile(FILE *);\nint ftrylockfile(FILE *);\nvoid funlockfile(FILE *);\nint getc_unlocked(FILE *);\nint getchar_unlocked(void);\nint putc_unlocked(int, FILE *);\nint putchar_unlocked(int);\nint getw(FILE *);\nint putw(int, FILE *);\nchar *tempnam(const char *, const char *) __asm(\"_\" \"tempnam\" );\n\ntypedef __darwin_off_t off_t;\n\nint fseeko(FILE *, off_t, int);\noff_t ftello(FILE *);\n\n\nint snprintf(char * , size_t, const char * , ...) __attribute__((__format__ (__printf__, 3, 4)));\nint vfscanf(FILE * , const char * , va_list) __attribute__((__format__ (__scanf__, 2, 0)));\nint vscanf(const char * , va_list) __attribute__((__format__ (__scanf__, 1, 0)));\nint vsnprintf(char * , size_t, const char * , va_list) __attribute__((__format__ (__printf__, 3, 0)));\nint vsscanf(const char * , const char * , va_list) __attribute__((__format__ (__scanf__, 2, 0)));\n\ntypedef __darwin_ssize_t ssize_t;\n\nint dprintf(int, const char * , ...) __attribute__((__format__ (__printf__, 2, 3))) __attribute__((visibility(\"default\")));\nint vdprintf(int, const char * , va_list) __attribute__((__format__ (__printf__, 2, 0))) __attribute__((visibility(\"default\")));\nssize_t getdelim(char ** , size_t * , int, FILE * ) __attribute__((visibility(\"default\")));\nssize_t getline(char ** , size_t * , FILE * ) __attribute__((visibility(\"default\")));\n\n\nextern const int sys_nerr;\nextern const char *const sys_errlist[];\nint asprintf(char **, const char *, ...) __attribute__((__format__ (__printf__, 2, 3)));\nchar *ctermid_r(char *);\nchar *fgetln(FILE *, size_t *);\nconst char *fmtcheck(const char *, const char *);\nint fpurge(FILE *);\nvoid setbuffer(FILE *, char *, int);\nint setlinebuf(FILE *);\nint vasprintf(char **, const char *, va_list) __attribute__((__format__ (__printf__, 2, 0)));\nFILE *zopen(const char *, const char *, int);\nFILE *funopen(const void *,\n                 int (*)(void *, char *, int),\n                 int (*)(void *, const char *, int),\n                 fpos_t (*)(void *, fpos_t, int),\n                 int (*)(void *));\n\nextern int __sprintf_chk (char * , int, size_t,\n     const char * , ...)\n  ;\nextern int __snprintf_chk (char * , size_t, int, size_t,\n      const char * , ...)\n  ;\nextern int __vsprintf_chk (char * , int, size_t,\n      const char * , va_list)\n  ;\nextern int __vsnprintf_chk (char * , size_t, int, size_t,\n       const char * , va_list)\n  ;\n";
		result += "FILE *fpTemp[" + numThreads + "];\nint phaseCounter = 0;\n";
		return result;
	}

	/**
	 * This method returns the string for a set of statements to be added to the
	 * beginning of main().
	 * 
	 * @return
	 */
	public String getStartMain(int numThreads) {
		String result = new String();
		for (int i = 0; i < numThreads; i++) {
			result += "fpTemp[" + i + "] = fopen(\"dataRaceOut_" + i + ".txt\", \"w\");";
			result += "if (fpTemp[ " + i + "] == 0) {";
			result += "printf(\"Can\'t open file dataRaceOut_" + i + ".txt!\\n\" );";
			result += "exit(1);";
			result += "}";
		}
		return result;
	}

	/**
	 * This method returns the string for the statements to be added to the end
	 * of main().
	 * 
	 * @return
	 */
	public String getEndMain(int numThreads) {
		String result = new String();
		for (int i = 0; i < numThreads; i++) {
			result += "fclose(fpTemp[" + i + "]);";
		}
		return result;
	}

	/**
	 * This method returns the string to be added after implicit barriers,
	 * which can increment the value of phaseCounter by 1.
	 * 
	 * @return
	 */
	public String getPhaseIncrementer() {
		String ret = new String();
		ret += "{if(omp_get_thread_num() == 0) {phaseCounter++;}";
		ret += "#pragma omp barrier\n}";
		return ret;
	}
}
