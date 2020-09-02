#!/bin/bash
# Obtain the root folder containing the test-programs to be run.
BASE=$(pwd)
TESTROOT=$1
if [ "${TESTROOT}" == "" ]
then
	echo -e "usage: bash post-pass-only.sh <path-to-test-programs-folder> \nExiting..."
	exit
fi
TESTROOT="$BASE/$1"
if [ ! -d $TESTROOT ]
then
	echo "Could not find the test folder $TESTROOT"
	exit
fi

# Ensure that Z3 has been set correctly.
if [ "${Z3HOME}" == "" ]
then
	echo -e "Set the environment variable Z3HOME to point to the directory z3.\nExiting..."
	exit
fi
if [ ! -d $Z3HOME ]
then
	echo "$Z3HOME does not exist."
	exit
fi

# Obtain IMOPHOME.
SCRPATH="${BASE}/$(dirname $0)"
cd ${SCRPATH}/..
IMOPHOME=$(pwd)

# Create a temporary file for stderr.
DATE=$(date | tr ' ' '_' | tr ':' '_')
ERRFILE="/tmp/errStream${DATE}.err"
echo "Saving standard error stream at ${ERRFILE}."

# Jump into the test-folder, and process each test at a time.
cd $TESTROOT
for FILE in $(ls *.i)
do
	echo -e "*** Processing $FILE"
	echo "* Processing $FILE" >> $ERRFILE
	cd $TESTROOT
	FILENAME=$(echo $FILE | cut -f1 -d.)
	PREFILE=/tmp/${FILENAME}.i
	cat $FILE > $PREFILE
	cd ${IMOPHOME}/bin
	java -ea -Xms2048M -Xmx4096M -cp ${IMOPHOME}/third-party-tools/com.microsoft.z3.jar:. \
		-Djava.library.path=${Z3HOME}/build  -verbose:gc imop.Main --noPrepass -dln -nru -f $PREFILE 2>> $ERRFILE
	echo -e "======================"
done
echo -e "DUMP: $(cat $ERRFILE)"
