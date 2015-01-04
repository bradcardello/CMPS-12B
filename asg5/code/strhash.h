// Brad Cardello (bcardell)
// Kara Ekiss (kekiss)
// $Id: strhash.h,v 1.2 2014-06-07 04:18:23-07 - - $

//
// NAME
//    strhash - return an unsigned 32-bit hash code for a string
//
// SYNOPSIS
//    size_t strhash (const char *string);
//

#ifndef __STRHASH_H__
#define __STRHASH_H__

#include <inttypes.h>

size_t strhash (const char *string);

#endif

