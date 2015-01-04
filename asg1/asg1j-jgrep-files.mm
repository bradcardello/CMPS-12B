.so Tmac.mm-etc
.if t .Newcentury-fonts
.INITR* \n[.F]
.SIZE 12 14
.TITLE CMPS-012B Spring\~2014 Program\~1 "Grep, args, and files"
.RCS "$Id: asg1j-jgrep-files.mm,v 1.2 2014-03-24 18:33:58-07 - - $"
.PWD
.URL
.H 1 "Introduction"
In this assignemnt you will perform options analysis and read
sequences of files, printing out lines that match a regular
expression.
Your Java program will be placed in a jar file.
Your program should behave in a manner similar to
.V= grep (1).
.H 1 "Program specification"
The program is presented in the form of a Unix
.V= man (1)
page.
.SH=BVL
.MANPAGE=LI "NAME"
jgrep \[em] search files for a pattern
.MANPAGE=LI "SYNOPSIS"
.V= jgrep
.=V \|[ -ilnv ]
.IR pattern
.RI \|[ filename \|.\|.\|.]
.MANPAGE=LI "DESCRIPTION"
The
.V= jgrep
utility searches text files for a pattern and prints out all lines
that match that pattern.
Patterns are specified as regular expressions and make use of
.V= java.util.regex.Pattern .
.MANPAGE=LI "OPTIONS"
The options word must be the first word and begin with a minus sign
.=V ( - )
if present.
Option letters in the options word may occur in any order.
Operands appear thereafter.
.VL \n[Pi]
.MANOPT=LI -i
Ignore upper- and lower-case distinctions in the specified pattern.
.MANOPT=LI -l
Prints only the names of the files with matching lines,
one filename per line.
A filename is printed only once.
.MANOPT=LI -n
Precede each line by its line number within the file,
with the first line being line 1.
.MANOPT=LI -v
Prints all lines except for those that match the pattern.
The sense of the match is complemented.
.LE
.MANPAGE=LI "OPERANDS"
The first operand is required and is a regular expression according to
the syntax recognized by the
.V= java.util.regex.Pattern
class.
All other operands are filenames.
Each file is opened for input in turn.
if no filenames are specified, then
.V= stdin
is read instead.
.MANPAGE=LI "EXIT STATUS"
The following exit status values are returned when the program
exits\(::
.VL \n[Pi]
.LI 0
One or more matches were found.
.LI 1
No matches were found.
.LI 2
Syntax errors or inaccessible files were found.
.LE
.MANPAGE=LI "SEE ALSO"
.V= grep (1),
.V= egrep (1),
.V= fgrep (1).
.LE
.H 1 "Starter code"
In the
.V= code/
subdirectory, you have been provided with some starter code which
you should read and understand.
Also, read the man page for
.V= grep (1),
to see how it works.
.ALX 1 ()
.LI
The main function begins by compiling the regular expression specified
on the command line into a
.V= Pattern .
If there is a syntax error,
that compilation will generate a
.V= PatternSyntaxException ,
which is then reported,
causing the program to terminate.
.LI
If that succeeds, a 
.V= Scanner
is created either for reading
.V= stdin ,
or a loop is performed to open each file in sequence.
Opening a file can cause an
.V= IOException ,
which is then reported.
.LI
Note that a pattern error is fatal,
while a file access error allows the program to continue.
.LI
The function
.V= scanfile
iterates over all lines in the file using
.V= hasNextLine
and
.V= nextLine .
The line
.VCODE* 1 "boolean matches = regex.matcher (line).find();"
sets the boolean variable to true or false, 
depending on whether the line matches the regex.
.LI
Error messages are always printed to
.V= System.err ,
not to 
.V= System.out .
An error message always comes in three parts\(::
the name of the program issuing the error,
what it is complaining about,
and the reason for the complaint.
For example\(::
.VTCODE 1 "jgrep: somefile.foo (No Such File)
.LE
.H 1 "Development sequence"
You will need to develop the program a little at a time,
in some stages.
At each point in your development,
compare the behaviour of your program with that of
.V= grep (1).
.ALX 1 ()
.LI
Begin by studying the man page for
.V= grep (1).
Also study Java's regular expressions
.V= java.util.regex.Pattern .
Following are some examples of regexes.
.nr Pi2 \n[Pi]*2
.VL \n[Pi2]
.V=LI abcd
matches any line containing the string
.=V `` abcd ''.
.V=LI \[ha]abcd\[Do]
matches any line containing exactly the string
.=V `` abcd ''.
The
.=V `` \[ha] ''
matches the beginning of the line and
.=V `` \[Do] ''
matches the end of the line.
.V=LI ab.*cd
matches any line containing the string
.=V `` ab ''
followed somewhere later by
.=V `` cd ''.
The dot
.=V ( . )
matches any character and the asterisk
.V= ( * )
matches zero or more occurrences of whatever preceds it.
.V=LI ab|cd
matches any line containing an
.=V `` ab ''
or a
.=V `` cd ''.
.V=LI [a-z]
matches any line containing any lower case letter,
i.e.,
the letter
.=V `` a ''
or
.=V `` z ''
or any character lexicographically in between.
The brackets indicate that a set of characters is specified.
.LE
.LI
Modify the program to recognize the four flags specified in the
assignment.
Unlike normal Unix commands, this java version requires the options to
be clustered, but in any order.
So the following mean the same\(::
.DS
.VCODE* 1 "jgrep -inv patt file"
.VCODE* 1 "jgrep -nvi patt file"
.DE
Flags may occur in any order but always must precede the pattern
and filenames.
.LI
Use a small convenience class
.V= flags
which you can pass around to the necessary parts of the program\(::
.DS
.VCODE* 1 "class options {"
.VCODE* 1 "   boolean insensitive;"
.VCODE* 1 "   boolean filename_only;"
.VCODE* 1 "   boolean number_lines;"
.VCODE* 1 "   boolean reverse_match;"
.VCODE* 1 "   String regex;"
.VCODE* 1 "   String[] filenames;"
.VCODE* 1 "}"
.DE
Put this in a file called
.V= options.java .
Its constructor should have 
.V= args
passed into it.
.LI
If the
.V= -i
flag is specified,
the pattern is compiled with
.VCODE* 1 "Pattern pattern = Pattern.compile (regex,
.VCODE* 1 "        Pattern.CASE_INSENSITIVE);
.LI
If the
.V= -l
option is specified,
only the names of the files that match are printed.
And as soon as a match is found,
the rest of the file is ignored.
What name is printed by
.V= grep (1)
for
.V= stdin \(??
.LI
Then modify your code to handle the
.V= -n
option.
.LI
If
.V= -v
is specified,
the boolean variable
.V= matches
is flipped.
.LI
Test your program extensively against
.V= grep .
.LI
Print error messages if invalid options are specified.
The usage message is already done for you.
And for bad filenames, print the results of 
.V= getMessage ,
which is more complete than what
.V= grep
shows.
.LI
Modify
.V= messages
to make sure that the exit status in the program specification
is handled correctly.
.LI
Modify
.V= Makefile
to add these extra classes.
.LI
Note that normal output goes to
.V= stdout ,
while error messages go to
.V= stderr .
To verify the exit status of your program,
the command
.V= "echo \[Do]?"
(bash) or
.V= "echo \[Do]status"
(csh) can be used.
.LI
The
.V= README
should have a
.E= "very brief"
note in it if there is something you want the grader to know
before starting the grading.
Every file you submit should have your name and username in a
comment at the top of the file.
.LI
Run
.V= checksource
on all files you submit.
It should be silent.
.LE
.H 1 "What to submit"
.V= README ,
.V= Makefile ,
.V= jgrep.java ,
.V= messages.java ,
.V= options.java .
.P
Make sure that the directory
.V= /afs/cats.ucsc.edu/courses/cmps012b-wm/bin/
is in your
.V= \[Do]PATH
or
.V= \[Do]path
(depending on which shell you use).
You will find the scripts
.V= cid ,
.V= checksource ,
and
.V= testsubmit 
useful.
.P
If you are doing pair programming, 
follow the Syllabus instructions detailing the pair programming
requirements and submit the
.V= PARTNER
file .
.FINISH
