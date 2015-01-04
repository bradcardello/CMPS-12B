// Brad Cardello (bcardell)
// Kara Ekiss (kekiss)
// $Id: hashset.h,v 1.4 2014-06-07 04:18:23-07 - - $

#ifndef __HASHSET_H__
#define __HASHSET_H__

#include <stdbool.h>

typedef struct hashset hashset;

//
// Create a new hashset with a default number of elements.
//
hashset *new_hashset (void);

//
// Frees the hashset, and the words it points at.
//
void free_hashset (hashset*);

//
// Inserts a new string into the hashset.
//
void put_hashset (hashset*, const char*);

//
// Looks up the string in the hashset and returns true if found,
// false if not found.
//
bool has_hashset (hashset*, const char*);

void double_hashset (hashset*);
void chain_count (hashset*, int);
void debug_dump (hashset*, int);


#endif

