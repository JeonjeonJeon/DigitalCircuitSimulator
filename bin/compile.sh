#!/bin/bash

# this script is to comile java source code and excute it using ecj, dx, dalvikvm command
# this file should located at src directory

# source code at src/"package name"
# class file at class/"package name"
# dex file at dex/"project name".dex

# WARNING: dex/ directory must exist before this script executes.

# <command line usage>
# ecj -cp [classpath] -d [output file] [packageName.fileName].java
# dx --dex --output=[dex file name].dex hello.class
# dalvikvm -cp [dex file name].dex [main class file name]

# variables
projectdir=$(dirname $PWD)
parentdir=$(dirname $projectdir)
startnum=${#parentdir}
projectname=${projectdir:startnum+1}

function ecj_compile() {
	for file in ./*
	do
		if [[ -d ${file} ]]; then
			for sub in ${file}/*
			do
				echo "*** compiling package ${sub:2} ***"
				ecj -cp ./ -d ../class/ ${sub:2}
			done
		fi
	done
	echo "ecj done"
}

function dex_convert() {
	echo "*** convert class files to ${projectname}.dex ***"
	dx --dex --output=${projectdir}/dex/${projectname}.dex ${projectdir}/class/
	echo "dx done"
}

ecj_compile
dex_convert
echo ""
echo "---------- console output ----------"
dalvikvm -cp "${projectdir}/dex/javaExample.dex" "main.Main" 
