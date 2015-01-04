# $Id: Makefile,v 1.2 2014-04-29 03:09:53-07 - - $

JAVASRC    = edfile.java dllist.java auxlib.java
SOURCES    = ${JAVASRC} Makefile README PARTNER
MAINCLASS  = edfile
CLASSES    = ${JAVASRC:.java=.class}
JARCLASSES = ${CLASSES} dllist\$$*.class
JARFILE    = edfile
LISTING    = Listing.ps
SUBMITDIR  = cmps012b-wm.s14 asg2

all : ${JARFILE}
	- checksource ${SOURCES}

${JARFILE} : ${CLASSES}
	echo Main-class: ${MAINCLASS} >Manifest
	jar cvfm ${JARFILE} Manifest ${JARCLASSES}
	chmod +x ${JARFILE}
	- rm Manifest

%.class : %.java
	- cid + $<
	javac $<

clean :
	- rm ${JARCLASSES} Manifest

spotless : clean
	- rm ${JARFILE}

ci : ${SOURCES}
	- checksource ${SOURCES}
	- cid + ${SOURCES}

lis : ${SOURCES}
	mkpspdf ${LISTING} ${SOURCES}

submit : ${SOURCES}
	submit ${SUBMITDIR} ${SOURCES}

again:
	gmake --no-print-directory spotless ci all lis

