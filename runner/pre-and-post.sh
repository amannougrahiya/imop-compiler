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
for FILE in $(ls *.c)
do
	echo -e "Preprocessing $FILE using GCC"
	echo -e "Preprocessing $FILE using GCC" >> $ERRFILE
	FILENAME=$(echo $FILE | cut -f1 -d.)
	PREFILE=${DIRROOT}/${FILENAME}.i
	if [ -f $PREFILE ]
	then
		continue
	fi
	gcc -P -E $FILE -o $PREFILE
done

cd $DIRROOT
for FILE in $(ls *.i)
do
	echo -e "Processing $FILE"
	echo "Processing $FILE" >> $ERRFILE
	cd $DIRROOT
	FILENAME=$(echo $FILE | cut -f1 -d.)
	cd ${IMOPHOME}/bin
	java -ea -Xms2048M -Xmx4096M -cp ${IMOPHOME}/third-party-tools/com.microsoft.z3.jar:. \
		-Djava.library.path=${Z3HOME}/build imop.Main --prepass -ru -dln -f $DIRROOT/$FILE 2>> $ERRFILE

	if [ ! -f "../output-dump/${FILENAME}-useful.i" ]
	then
		echo "Could not generate the postpass file. Exiting.."
		echo -e "DUMP: $(cat $ERRFILE)"
		continue
	fi

	PREFILE=/tmp/${FILENAME}.i
	mv ../output-dump/${FILENAME}-useful.i $PREFILE
	echo -e "\n\t\t *** After pre-pass ***"
	gtimeout 15m java -ea -Xms2048M -Xmx4096M -cp ${IMOPHOME}/third-party-tools/com.microsoft.z3.jar:.\
		-Djava.library.path=${Z3HOME}/build imop.Main --noPrepass -dln -f $PREFILE 2>> $ERRFILE
	echo -e "======================"
	#echo -e "DUMP: $(cat $ERRFILE)"
done
