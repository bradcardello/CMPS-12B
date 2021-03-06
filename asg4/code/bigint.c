// Brad Cardello (bcardell)
// $Id: bigint.c,v 1.3 2014-05-28 02:56:56-07 - - $

#include <assert.h>
#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

#include "bigint.h"
#include "debug.h"

#define MIN_CAPACITY 16

struct bigint {
   size_t capacity;
   size_t size;
   bool negative;
   char *digits;
};

static void trim_zeros (bigint *this) {
   while (this->size > 1) {
      size_t digitpos = this->size - 1;
      if (this->digits[digitpos] != 0) break;
      --this->size;
   }
}

bigint *new_bigint (size_t capacity) {
   bigint *this = malloc (sizeof (bigint));
   assert (this != NULL);
   this->capacity = capacity;
   this->size = 0;
   this->negative = false;
   this->digits = calloc (this->capacity, sizeof (char));
   assert (this->digits != NULL);
   DEBUGS ('b', show_bigint (this));
   return this;
}

bigint *new_string_bigint (char *string) {
   assert (string != NULL);
   size_t length = strlen (string);
   bigint *this = new_bigint (length > MIN_CAPACITY
                            ? length : MIN_CAPACITY);
   char *strdigit = &string[length - 1];
   if (*string == '_') {
      this->negative = true;
      ++string;
   }
   char *thisdigit = this->digits;
   while (strdigit >= string) {
      assert (isdigit (*strdigit));
      *thisdigit++ = *strdigit-- - '0';
   }
   this->size = thisdigit - this->digits;
   trim_zeros (this);
   DEBUGS ('b', show_bigint (this));
   return this;
}

static size_t bigger (size_t this, size_t that) {
   if (this > that) return this;
   else             return that;
}

static bool bigger_int (bigint *this, bigint *that) {
   size_t length = bigger (this->size, that->size);
   for (size_t i = 0; i <= length; ++i) {
      size_t index = length - 1 - i;
      if (this->digits[index] > that->digits[index]) return true;
      else if (that->digits[index] > this->digits[index]) return false;
   }
   return false; 
}

// Addition, if the signs are the same (STEP C)
// Subtraction if the signs are different
static bigint *do_add (bigint *this, bigint *that) {
   DEBUGS ('b', show_bigint (this));
   DEBUGS ('b', show_bigint (that));

   size_t length = bigger (this->size, that->size);
   bigint *result = new_bigint (length);
   result->size = this->size + 1;
   if (this->size < that->size) result->size = that->size + 1;
   int carry = 0;
   int digit = 0;
   for (size_t index = 0; index <= length; ++index) {
         digit = this->digits[index] + that->digits[index] + carry;
         result->digits[index] = digit % 10;
         carry = digit / 10;
   }
   trim_zeros (result);
   return result;
}

// Addition, if the signs are different (STEP D)
// Subtraction if the signs are same
static bigint *do_sub (bigint *this, bigint *that) {
   DEBUGS ('b', show_bigint (this));
   DEBUGS ('b', show_bigint (that));
   
   size_t capacity = 0;
   if (this->size > that->size) {
      if (bigger_int (this, that) == true)
         capacity = this->size;
      else {
         bigint *tmp = this;
         this = that;
         that = tmp;
      }
   }else {
      if (bigger_int (this, that) == true)
         capacity = this->size;
      else {
         bigint *tmp = this;
         this = that;
         that = tmp;
      }
   }
   size_t length = bigger (this->size, that->size);
   bigint *result = new_bigint (length);
   result->size = this->size + 1;
   if (this->size < that->size) {
      result->size = that->size + 1;
      result->negative = false;
   }
   int borrow = 0;
   int digit = 0;
   for (size_t index = 0; index <= length; ++index) {
      digit = this->digits[index] - that->digits[index] - borrow + 10;
      result->digits[index] = digit % 10;
      borrow = 1 - digit / 10;
   }
   trim_zeros (result);
   return result;
}
void free_bigint (bigint *this) {
   free (this->digits);
   free (this);
}

void print_bigint (bigint *this, FILE *file) {
   DEBUGS ('b', show_bigint (this));
   trim_zeros (this);
   if (this->negative == true)
      printf("-");
   for (char *byte = &this->digits[this->size-1];
        byte >= this->digits; --byte) {
      fprintf (file, "%d", *byte);
   }
   printf("\n");
}

// Do_add() and do_sub() are called from either the 
// addition or subtraction function to do the array work.
bigint *add_bigint (bigint *this, bigint *that) {
   DEBUGS ('b', show_bigint (this));
   DEBUGS ('b', show_bigint (that));

   bigint *newbigint = NULL;
   if (this->negative == that->negative) {
      newbigint = do_add (this, that);
      newbigint->negative = this->negative;
   }else {
      newbigint = do_sub (this, that);
      newbigint->negative = that->negative;
   }
   return newbigint;
}

// Do_add() and do_sub() are called from either the 
// addition or subtraction function to do the array work.
bigint *sub_bigint (bigint *this, bigint *that) {
   DEBUGS ('b', show_bigint (this));
   DEBUGS ('b', show_bigint (that));
   
   bigint *newbigint = NULL;
   if (this->negative == that->negative) {
      newbigint = do_sub (this, that);
   }else {
      newbigint = do_add (this, that);
   }
   return newbigint;
}

bigint *mul_bigint (bigint *this, bigint *that) {
   DEBUGS ('b', show_bigint (this));
   DEBUGS ('b', show_bigint (that));
   bigint* result = new_bigint(this->size + that->size);
   int carry = 0;
   int digit = 0;
   for (size_t i = 0; i < this->size; ++i) {
      carry = 0;
      for (size_t j = 0; j < that->size; ++j) {
         digit = result->digits[i + j] +
                 (this->digits[i]*that->digits[j]) +
                 carry;
         result->digits[i+j] = digit % 10;
         carry = digit/10;
      }
      result->digits[i + that->size] = carry;
   }
   result->size = this->size + that->size;
   if (this->negative != that->negative) result->negative = true;
   trim_zeros (result);
   return result;
}

void show_bigint (bigint *this) {
   fprintf (stderr, "bigint@%p->{%lu,%lu,%c,%p->", this,
            this->capacity, this->size, this->negative ? '-' : '+',
            this->digits);
   for (char *byte = &this->digits[this->size - 1];
        byte >= this->digits; --byte) {
      fprintf (stderr, "%d", *byte);
   }
   fprintf (stderr, "}\n");
}

