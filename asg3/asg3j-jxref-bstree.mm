.so Tmac.mm-etc
.if t .Newcentury-fonts
.SIZE 12 14
.INITR* \n[.F]
.TITLE CMPS-012B Spring\~2014 Program\~3 \
"Xref Binary Search Tree"
.RCS "$Id: asg3j-jxref-bstree.mm,v 1.10 2014-04-23 21:04:53-07 - - $"
.PWD
.URL
.EQ
delim $$
.EN
.H 1 "Overview"
In this assignment,
you will implement a cross reference utility that will scan files
for words and for each word found,
print a list of those words in lexicographic order along with a
list of line numbers indicating where the words were found.
.P
This program will make use of a binary search tree data structure,
which means that each search and insertion of a word will run in 
$ O ( log sub 2 n ) $ time.
No balancing will be performed,
as that is beyond the scope of this course.
As a value in each tree node,
there will be a queue of line numbers,
with the linked implementation of the queue where each operation
runs in $ O ( 1 ) $ time.
.H 1 "Program Specification"
Once again we present the specification in the form of a Unix
.V= man (1)
page.
.SH=BVL
.MANPAGE=LI "NAME"
jxref \[em] cross reference utility
.MANPAGE=LI "SYNOPSIS"
.V= jxref
.=V \|[ -df ]
.RI \|[ filename \|.\|.\|.]
.MANPAGE=LI "DESCRIPTION"
Each file is read in sequence and a printout of the words found in the
file is generated at the end of each file,
one word per line,
followed by a list of line numbers where the word occurred.
All normal output is sent to
.V= stdout .
Error messages are printed to
.V= stderr .
.MANPAGE=LI "OPTIONS"
All options, if any, must appear as the first word following the
name of the command, unlike the standard
.V= getopt (3c)
used for C programs.
.VL \n[Pi]
.MANOPT=LI -d
Instead of producing normal output,
the tree is dumped in debug format,
showing each key along with its level within the tree.
.MANOPT=LI -f
Upper case letters are folded into lower case before insertion into
the binary search tree.
.LE
.MANPAGE=LI "OPERANDS"
Each operand is a filename,
which is processed in sequence,
each file causing to be created a new tree.
If any filename is specified as a minus sign
.=V ( - ),
.V= stdin
is read at that point.
If no filenames are specified,
.V= stdin 
is read.
As an output filename, the minus sign is used as the name of
.V= stdin .
.MANPAGE=LI "EXIT STATUS"
.VL \n[Pi]
.LI 0
No errors occurred.
.LI 1
Errors occurred, either in scanning options or opening files.
.LE
.LE
.H 1 "Implementation Sequence"
With every non-trivial program (and this is one of them),
you should develop the program a small piece at a time,
adding a small cluster of working code to a working program.
You thus have a working program at all times,
albeit only a partially complete one.
Here is one approach to completion of your program.
.ALX 1 ()
.LI
Print out the Postscript or PDF version of this file and also print out
all of the source code provided.
You have been given some starter code.
.LI
Study
.V= pxref.perl ,
which is an implementation of your project written in the Perl
language.
Your program should exactly duplicate its functionality,
except for some slight differences in the error messages.
Note also that the Perl program does not handle options.
.LI
Study how it handls input file names when they are given,
how it handles the input file name given as a minus sign
.=V ( - ),
and what happens where there are no file names given.
What happens when filenames are incorrect\(??
What output does it produce to
.V= stdout
and to
.V= stderr \(??
What exit status codes does it return\(??
.LI
The file
.V= auxlib.java
provides some useful functions for printing error messages,
and handling the exit status.
.LI
As provided,
.V= jxref.java
iterates over the files given on the command line,
and for each file, extracts words from each line.
.LI
The function
.V= String.split
is given the regular expression string
.V= \[Dq]\[rs]\[rs]W+\[Dq] ,
which represents the regex
.V= \[rs]W+ ,
which matches one or more characters outside the set
.V= [a-zA-Z_0-9] .
I.e.,
it eliminates all characters outside that set and returns an
array of words.
.LI
The function
.V= String.matches
is given the argument string
.V= \[Dq]\[ha]\[rs]\[rs]d*\[Do]\[Dq] ,
which matches the beginning of a word,
zero or more digits,
up to the end of a word.
I.e., 
If the word is empty or consists only of digits,
it is skipped.
.LI
Otherwise it is printed.
This print statement is a debug statement which you must remove
before submitting your program.
.LI
Write an options analysis function similar to 
.V= getopt (3c),
except that yours is not as complicated.
If 
.V= args[0]
starts with the character minus
.=V ( - ),
then it is an option string.
Scan it for the flags
.V= -d
and
.V= -f
and print an error message for anything else.
Note that the flags may appear in any order, 
but not separately.
Thus
.V= -d ,
.V= -f ,
.V= -df ,
or
.V= -fd
are all acceptable, but not
.V= "-d -f" .
.LI
Pass a lower case flag parameter into
.V= xref_file 
and convert words to lower case
.=V ( String.toLowerCase )
if it is true.
.LI
Begin debugging your program by implementing
.V= debug_dump_rec ,
which performs an inorder traversal of the tree,
printing all key and value pairs,
indented to show the level of each node.
.LI
For the debug dump,
the tree should be printed in the following format\(::
.DS
.ft C
    2 aaaaa queue@9cab16
  1 bar queue@1a46e30
    2 eeeee queue@3e25a5
0 foo queue@19821f
    2 hello queue@addbf1
  1 qux queue@42e816
    2 world queue@9304b1
.DE
.LI
First print an integer which indicates the depth of a node in the tree,
with the root being at depth 0,
then print the key followed by the value.
Note that in this case we are depending on the
.V= toString
function of each,
and since class
.V= queue
does not have one, its identity is printed as the default.
Note that each line is also indented with two spaces per level down
in the tree.
The root is not indented,
its children are indented 2 spaces,
its grandchildren 4 spaces, etc.
.LI
Implement the
.V= treemap.put
function.
Do a binary search on the tree for a node whose key
.=V ( compareTo
returns 0)
is equal to the node.
If found,
replace the old value by the new one,
and return the old value.
If not found,
create a new node as a leaf node,
and insert the key and value pair,
and return 
.V= null .
.LI
.E= "Do not do any balancing."
Balancing a tree is beyond the scope of this course.
.LI
Implement
.V= treemap.get
using a binary search.
Return the value if found and
.V= null
if not found.
.LI
As you work through this project,
you must eventually replace all occurrences of throwing an
.V= UnsupportedOperationException
with working code.
.LI
Go back to 
.V= jxref.java
and implement the
.V= -d
option.
If
.V= -d
is specified,
continue to dump in the debug format.
For normal output, call
.V= treemap.do_visit
to print out the tree.
.LI
Complete
.V= do_visit_rec
to apply the 
.V= visit_fn
to each node in the tree using an inorder traversal.
This function in 
.V= treemap 
has no print statements.
.LI
Finish the implementation of
.V= queue
by replacing the 
.V= UnsupportedOperationException
throws with working code for the linked list implementation of the
queue.
.LI
For each word found in 
.V= xref_file ,
get the queue from the tree using the word as a key.
If found,
insert the new integer line number into that queue.
If not found,
create a new queue, insert the line number into it,
and then put the word and the queue into the tree.
.LI
Finish
.V= printer
in the main class so that it produces the same output as the perl
program.
To do this,
the tree will apply the visitor to each queue,
using an iterator.
.LI
Test your program thoroughly.
Delete the reference to
.V= pxref.perl
from the
.V= Makefile ,
since
.V= pxref.perl
should not be submitted.
Delete the
.V= lis
target as well.
.LI
Make sure that all inner classes and only those are listed in the
.V= Makefile .
Make sure that all inner class names have a 
.V= \[rs]\[Do]\[Do]
in place of the dollar sign.
Are all inner classes being placed in the jar\(??
.LI
Do
.V= "gmake spotless"
and
.V= "gmake"
work\(??
Finish the
.V= submit
target.
.LE
.H 1 "Submit"
Submit the
.V= Makefile
and all necessary Java source files.
.E= "Verify the submit\(!!"
Submit
.V= PARTNER
if you do pair programming.
.FINISH
