#!/bin/bash
# 
# The rendering settings are stored as display_and_comments.txt within each
# image directory. This script just collects (copies) them and renames 
# them as xyz_mm_rend.json (where xyz is the image directory)

for i in /uod/idr/filesets/idr0040-aymoz-singlecell/20180215/**/display_and_comments.txt
do
	IFS='/' read -ra arr <<< "$i"
	cp $i /home/dlindner/${arr[6]}_mm_rend.json
done
