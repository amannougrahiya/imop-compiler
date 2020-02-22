#!/bin/bash
for FILE in $(ls)
do
	[ "$(echo $FILE | cut -f2 -d.)" != "c" ] && continue
	echo "Creating dotGraph for $FILE"
	PW=$(pwd)
	cd ~/Dropbox/openmp/Frame/bin
	java imop/Main < $PW/$FILE
	cd $PW
	echo "Press any key to continue... (NOTE: dotGraph.v will be overwritten)"
	read
done
