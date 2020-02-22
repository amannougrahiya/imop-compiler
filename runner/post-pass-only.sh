#!/bin/bash
if [ "${IMOPHOME}" == "" ]
then
	echo -e "Set the environment variable IMOPHOME to point to the directory imop-compiler.\nExiting..."
	exit
fi

if [ "${Z3HOME}" == "" ]
then
	echo -e "Set the environment variable Z3HOME to point to the directory z3.\nExiting..."
	exit
fi

DIRROOT=$1

if [ "${DIRROOT}" == "" ]
then
	echo -e "usage: bash post-pass-only.sh <path-to-test-programs-directory> \nExiting..."
	exit
fi

DATE=$(date | tr ' ' '_' | tr ':' '_')
ERRFILE="/tmp/errStream${DATE}.err"
echo "Saving standard error stream at ${ERRFILE}."
PWDMINE=$(pwd)
cd /tmp
if [ ! -d $DIRROOT ]
then
	DIRROOT="${PWDMINE}/${DIRROOT}"
fi
if [ ! -d $DIRROOT ]
then
	echo "Could not find the directory $DIRROOT"
	exit
fi
cd $DIRROOT
for FILE in $(ls *.i)
do
	echo -e "Processing $FILE"
	echo "* Processing $FILE" >> $ERRFILE
	cd $DIRROOT
	FILENAME=$(echo $FILE | cut -f1 -d.)
	PREFILE=/tmp/${FILENAME}.i
	cat $FILE > $PREFILE
	cd ${IMOPHOME}/bin
	java -ea -Xms2048M -Xmx4096M -cp ${IMOPHOME}/third-party-tools/com.microsoft.z3.jar:. \
		-Djava.library.path=${Z3HOME}/build  -verbose:gc imop.Main --noPrepass -nru -f $PREFILE 2>> $ERRFILE
	echo -e "======================"
	echo -e "DUMP: $(cat $ERRFILE)"
done
