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

cd $TESTROOT
for FILE in $(ls *.c)
do
	echo -e "Preprocessing $FILE using GCC"
	echo -e "Preprocessing $FILE using GCC" >> $ERRFILE
	FILENAME=$(echo $FILE | cut -f1 -d.)
	PREFILE=${TESTROOT}/${FILENAME}.i
	if [ -f $PREFILE ]
	then
		continue
	fi
	gcc -P -E $FILE -o $PREFILE
done

cd $TESTROOT
for FILE in $(ls *.i)
do
	echo -e "Processing $FILE"
	echo "Processing $FILE" >> $ERRFILE
	cd $TESTROOT
	FILENAME=$(echo $FILE | cut -f1 -d.)
	cd ${IMOPHOME}/bin
	java -ea -Xms2048M -Xmx40960M -cp ${IMOPHOME}/third-party-tools/com.microsoft.z3.jar:. \
		-Djava.library.path=${Z3HOME}/build imop.Main --prepass -ru -dln -f $TESTROOT/$FILE 2>> $ERRFILE

	if [ ! -f "../output-dump/${FILENAME}-useful.i" ]
	then
		echo "Could not generate the postpass file for ${FILENAME}. Skipping.."
		continue
	fi

	PREFILE=/tmp/${FILENAME}.i
	mv ../output-dump/${FILENAME}-useful.i $PREFILE
	echo -e "\n\t\t *** After pre-pass ***"
	timeout 20m java -ea -Xms2048M -Xmx40960M -cp ${IMOPHOME}/third-party-tools/com.microsoft.z3.jar:.\
		-Djava.library.path=${Z3HOME}/build imop.Main --noPrepass -ru -dln --yuan -cm LZINV -sve -f $PREFILE 2>> $ERRFILE
	timeout 20m java -ea -Xms2048M -Xmx40960M -cp ${IMOPHOME}/third-party-tools/com.microsoft.z3.jar:.\
		-Djava.library.path=${Z3HOME}/build imop.Main --noPrepass -ru -dln --icon -cm LZINV -sve -f $PREFILE 2>> $ERRFILE
	timeout 20m java -ea -Xms2048M -Xmx40960M -cp ${IMOPHOME}/third-party-tools/com.microsoft.z3.jar:.\
		-Djava.library.path=${Z3HOME}/build imop.Main --noPrepass -ru -dln --icon -cm LZUPD -sve -f $PREFILE 2>> $ERRFILE
	timeout 20m java -ea -Xms2048M -Xmx40960M -cp ${IMOPHOME}/third-party-tools/com.microsoft.z3.jar:.\
		-Djava.library.path=${Z3HOME}/build imop.Main --noPrepass -ru -dln --icon -cm LZINV -nsve -f $PREFILE 2>> $ERRFILE
	timeout 20m java -ea -Xms2048M -Xmx40960M -cp ${IMOPHOME}/third-party-tools/com.microsoft.z3.jar:.\
		-Djava.library.path=${Z3HOME}/build imop.Main --noPrepass -ru -dln --icon -cm LZUPD -nsve -f $PREFILE 2>> $ERRFILE
	echo -e "======================"
done
