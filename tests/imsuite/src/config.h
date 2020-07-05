#ifndef _CONFIG_H_
#define _CONFIG_H_

/*
 * What scheduling technique should be used in parallelized for-routines?
 *
 * Allowed values:
 *  - dynamic
 *  - static
 *  - auto
 */
#define SCHEDULING_METHOD static

/*
 * LOG_LEVEL - Determines what goes into the screen and what doesn't.
 *
 * 0 - Suppress all non-essential output.
 * 1 - Display only warnings.
 * 2 - Display only warnings and information.
 * 3 - Display everything: warnings, information, and debug statements
 */
#define LOG_LEVEL 3

#endif
