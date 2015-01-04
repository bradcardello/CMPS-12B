// Brad Cardello (bcardell)
// Kara Ekiss (kekiss)
// $Id: hashset.c,v 1.13 2014-06-07 04:18:23-07 - - $

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "debug.h"
#include "hashset.h"
#include "strhash.h"

#define HASH_NEW_SIZE 15

typedef struct hashnode hashnode;
struct hashnode {
   char *word;
   hashnode *link;
};

struct hashset {
   size_t size;               // array size
   size_t load;               // number of chains
   hashnode **chains;         // chains are for collision resolution
                              // every array index is the head of a
                              // linked list
};

hashset *new_hashset (void) {
   hashset *this = malloc (sizeof (struct hashset));
   assert (this != NULL);
   this->size = HASH_NEW_SIZE;
   this->load = 0;
   size_t sizeof_chains = this->size * sizeof (hashnode *);
   this->chains = malloc (sizeof_chains);
   assert (this->chains != NULL);
   memset (this->chains, 0, sizeof_chains);
   DEBUGF ('h', "%p -> struct hashset {size = %zd, chains=%p}\n",
                this, this->size, this->chains);
   return this;
}

void free_hashset (hashset *this) {
   DEBUGF ('h', "free (%p)\n", this);
   free (this);
}  

void double_hashset (hashset *this) {
   size_t new_size = this->size * 2 + 1;
   hashnode **new_chains = calloc (new_size,
                                   sizeof (hashnode *));
   assert (new_chains != NULL);
   for (size_t i = 0; i < this->size; i++) {
      hashnode *temp = this->chains[i];
      while (temp != NULL) {
         size_t hash_index = strhash (temp->word) % new_size;
         hashnode *nlink = temp->link;
         temp->link = new_chains[hash_index];
         new_chains[hash_index] = temp;
         temp = nlink;
      }
   }
   free (this->chains);
   this->chains = new_chains;
   this->size = new_size;
}

// loading every word from the dictionary into the hashset
// when spellchecking, checks to see if that word is already in the
// hashset.  If it isn't, then it's printed out
void put_hashset (hashset *this, const char *item) { // searches until
   //it finds null array slot, and inserts word; allocates new hashnode
   size_t hash_index = strhash (item) % this->size;
   hashnode *curr = this->chains[hash_index];
   for (; curr != NULL; curr = curr->link) {
      int cmp = strcmp (curr->word, item);
      if (cmp == 0) return;
   }
   curr = this->chains[hash_index];
   hashnode *n = malloc (sizeof (struct hashnode));
   assert (n != NULL);
   n->link = curr;
   n->word = strdup (item);
   this->chains[hash_index] = n;
   this->load++;
   if (this->load * 2 > this->size) double_hashset (this);
   else return;
}

bool has_hashset (hashset *this, const char *item) { // searches to see
   // if item is already in the hashset
   //STUBPRINTF ("hashset=%p, item=%s\n", this, item);
   size_t hash_index = strhash (item) % this->size;
   for (hashnode *curr = this->chains[hash_index]; curr != NULL;
                                                   curr = curr->link) {
      int cmp = strcmp (curr->word, item);
      if (cmp == 0) return true;
   }
   return false;
}

void chain_count (hashset *this, int index) {
   char *chains = "chains of size";
   size_t count = 0;
   int numchains = 0;
   for (size_t i = 0; i < this->size; i++) {
      for (hashnode *curr = this->chains[i]; curr != NULL;
                                             curr = curr->link) {
         hashnode *n = this->chains[i];
         numchains++;
         n = n->link;
      }
      if (numchains == index) count++;
      numchains = 0;
   }
   if (count != 0) printf ("%7zu %11s %d\n", count, chains, index);
}

void big_dump (hashset *this) {
   char* eq = "=";
   for (size_t i = 0; i < this->size; i++) {
      hashnode *n = this->chains[i];
      if (n == NULL) continue;
      printf ("array[%10zu]",i);
      printf (" = %20lu \"%s\"\n", strhash(n->word), n->word);
      for (hashnode *curr = this->chains[i + 1]; curr !=NULL;
                                                 curr = curr->link) {
         printf ("%19s %20lu \"%s\"\n", eq, strhash (n->word), n->word);
      }
      n = n->link;
   }
}

void debug_dump (hashset *this, int debug_num) {
   printf ("%7zu words in the hash set\n", this->load);
   printf ("%7zu size of the hash array\n", this->size);
   
   for (int i = 1; i < 20; i++) {
      chain_count (this, i);
   }
   if (debug_num == 2) {
      big_dump (this);
      debug_num = 0;
   }
}
