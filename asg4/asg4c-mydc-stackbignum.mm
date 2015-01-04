.so Tmac.mm-etc
.if t .Newcentury-fonts
.INITR* \n[.F]
.SIZE 12
.TITLE CMPS-012B Spring\~2014 Program\~4 \
"Arbitrary Precision Math"
.RCS \
"$Id: asg4c-mydc-stackbignum.mm,v 1.32 2014-05-06 20:27:09-07 - - $"
.PWD
.URL
.nr Si \n[Pi]
.EQ
delim $$
define uvec  |{ bold u }|
define vvec  |{ bold v }|
define pvec  |{ bold p }|
define rem   |{ type binary "\f[R]remainder\f[P]" }|
define ...   |{ type binary { roman "..." } }|
.EN
.de code
.   H 1 "Code\(:) Module \f[CB]\\$1\f[P]"
..
.de proto
.   LI
.   V= "\\$*"
.   br
..
.H 1 "Overview"
In this assignment you will implement a subset of the
.V= dc (1)
arbitrary precision calculator.
For specifications, read the man page for that utility,
and experiment by running
.V= dc
itself.
You will implement six of its operators\(::
.V= + ,
.V= - ,
.V= * ,
.V= c ,
.V= f ,
.V= p .
Your program will be an executable image called
.V= mydc .
.H 1 "Modules in the program"
The following modules are part of the program.
For all but
.V= main
a header
.=V ( \&.h )
file specifies the interface which is accompanied by an
implementation
.=V ( \&.c ) 
file.
.ALX a ()
.LI
Module
.V= debug
contains useful debugging and tracing information.
.LI
Module
.V= stack
is a parameterized stack using an array implementation with
array doubling to take care of a full stack.
.LI
Module
.V= bigint
is the important part of this project and handles multiprecision
integer arithmetic using an array of characters.
.LI
Module
.V= token
reads long integers providing input to the rest of the program.
.LI
Module
.V= main
handles user interface, input and output.
.LE
.H 1 "Big integer implementation"
Following is a more detailed discussion of how to implement the
.V= bigint
module.
.ALX a ()
.LI
Before attempting to implement
.V= bigint ,
perform each of the three operations on paper,
reminding yourself how to perform the operations without a
calculator.
.LI
A
.V= bigint
consists of an array of digits.
Index 0 has the least significant digit,
and the end of the array has the most significant digit.
Each byte contains a single digit in the range $  0 ... 9  $,
inclusive.
The capacity field specifies the dimension of the array,
and the size field specifies the number of significant digits in
the array,
with leading zeros suppressed.
.LI
Addition,
if the signs are the same\(::
call
.V= do_add
to actually perform the addition and return a new
.V= bigint .
Then set the sign to be the sign of one of the arguments.
.LI
Addition, if the signs are different\(::
call
.V= do_sub
with the larger number as its left operand and the smaller number
as the right operand.
Then set the sign to that of the larger number.
.LI
Subtraction\(::
if the signs are different, call 
.V= do_add ,
otherwise call
.V= do_sub .
.LI
.V= Do_add
and
.V= do_sub
are called from either the addition or subtraction function to do
the array work.
Note that it is marked
.V= static
and is not called outside of the module.
.LI
.V= Do_add
allocates a new
.V= bigint
with space for a number of digits one larger
than the largest operand.
Then it loops across each array from index [0] to the end,
adding and carrying as is done by hand\(::
.DS I
.ft CB
digit = this->digits[index] + that->digits[index] + carry;
result->digits[index] = digit % 10;
carry = digit / 10;
.DE
There is a little extra trickiness at the high end of the shorter
number.
.LI
.V= Do_sub
allocates a new
.V= bigint
whose size is the same as the left operand,
and then performs the subtraction instead of addition\(::
.DS I
.ft CB
digit = this->digits[index] - that->digits[index] - borrow + 10;
result->digits[index] = digit % 10;
borrow = 1 - digit / 10;
.DE
After computing the result,
trim off high-order zeros.
.LI
Multiplication proceeds by allocating a new
.V= bigint
whose size is
equal to the sum of the sizes of the other two operands.
If $uvec$ is a vector of size $m$
and $vvec$ is a vector of size $n$,
then in $ O ( m n ) $ speed,
perform an outer loop over one argument and an inner loop over
the other argument, adding the new partial products to the product
$pvec$ as you would by hand.
The algorithm can be described mathematically as follows\(::
.DS I
.br
.S +1 +1
.br
$ pvec <- PHI $
$ for ~ i elem left [ 0 ... m - 1 right ] ~ : $
$ TAB c <- 0 $
$ TAB for ~ j elem left [ 0 ... n - 1 right ] ~ : $
$ TAB TAB d <- pvec sub { i + j } + uvec sub i vvec sub j + c $
$ TAB TAB pvec sub { i + j } <- d rem 10 $
$ TAB TAB c <- left floor d div 10 right floor $
$ TAB pvec sub { i + n } <- c $
.br
.S -1 -1
.br
.DE
.LI
The division and remainder algorithms are actually a single
algorithm which produces both results, 
then discards the one not needed.
This algorithm is complicated and not part of this assignment.
.LI
Note that
.V= malloc (3)
returns uninitialized storage, but
.V= calloc (3)
sets its allocated storage to 0,
so
.V= new_bigint
calls 
.V= calloc ,
not 
.V= malloc ,
to allocate the underlying arrays.
From the synopsis of
.V= malloc (3)\(::
.DS I
.ft CB
#include <stdlib.h>
void *calloc (size_t nmemb, size_t size);
void *malloc (size_t size);
void *realloc (void *ptr, size_t size);
void free (void *ptr);
.DE
.LE
.H 1 "Testing your program"
Your program should write exactly the same output to both
.V= stdout
and
.V= stderr
as does
.V= dc (1),
provided that inputs do not contain those facilities of
.V= dc
that your program is not expected to imitate.
For example\(::
.DS I
.ft CB
dc <test.in >test-dc.out 2>test-dc.err
mydc <test.in >test-mydc.out 2>test-mydc.err
diff test-dc.out test-mydc.out
diff test-dc.err test-mydc.err
.DE
Both of the
.V= diff (1)
commands should produce no output for comparing
.V= stdout ,
and only a difference in the name of the ELF for
.V= diff ing
.V= stderr .
.H 1 "What to submit"
Submit
.V= Makefile ,
.V= README ,
and all C source and header files necessary for the grader to
build your program with the command
.V= make .
If you are doing pair programming,
see the additional requirements.
.FINISH
