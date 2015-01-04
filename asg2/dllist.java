// Brad Cardello (bcardell)
// $Id: dllist.java,v 1.17 2014-04-25 13:55:47-07 - - $

// GO TO http://www.dreamincode.net/forums/topic/273905-double-linked-lists-dll/ for info on doubly linked lists
// need to look up generics (for example, <E>)

import java.util.*;

class dllist {

   public enum position {FIRST, PREVIOUS, FOLLOWING, LAST};

   private class node {
      String item;
      node prev;
      node next;
   }

   private node first = null;
   private node current = null;
   private node last = null;
   private int currentposition = 0;
   private int nodecount = 0;          // he wrote this down by hand, might be wrong
   
   // UNSURE OF WHAT CURRENTPOSITION SHOULD BE, EXCEPT FOR FIRST
   public void setposition (position pos) {           // requires switch statement, the way it is now is wrong
      switch (pos){
         case FIRST:
            currentposition = 0;
         case PREVIOUS:
            currentposition -= 1;
         case FOLLOWING:
            currentposition += 1;
         case LAST:
            currentposition = nodecount - 1;                      // what would last be?
         default:                                           // if it's supposed to be silently ignored, should I throw an exception at all?
            throw new UnsupportedOperationException();      // how do we know which position it's supposed to go to?  is it in the text file?
      }
   }

   // 99% SURE IT'S WORKING
   public boolean isempty () {
      return (nodecount == 0);
   }

   // PROBABLY WORKING
   public String getitem () {
      if (current.item == null){
         throw new NoSuchElementException();      // why doesn't this work?
      }
      else{
         return current.item;
      }
   }

   // MIGHT WANT TO CHANGE EXCEPTION, OTHER THAN THAT, IT SHOULD BE OK
   public int getposition () {
      if (nodecount == 0) {
         throw new NoSuchElementException();
      }
      else {
         return currentposition;
      }
   }

   // MIGHT WORK
   public void delete () {
      if (nodecount == 0){                               // might wanna reorder these conditionals
         throw new NoSuchElementException();
      }
      else{
         current.prev.next = current.next;               // goes to prev, then next
         current.next.prev = current.prev;               // should current be currentposition?  do I need to
         if (current.next == null){                   // create a new node named current and assign its position to currentposition?  seems redundant
            current = current.prev;
         }
         else{
            current = current.next;
         }
         nodecount--;
      }
   }

   // PROBABLY WORKING
   public void insert (String item, position pos) {
      node n = new node();
      n.item = item;
      nodecount++;
      switch (pos){
         case FIRST:
            n.prev = null;
            n.next = first;
            first.prev = n;
            first = n;
            current = n;
         case PREVIOUS:
            n.prev = current.prev;
            n.next = current;
            current.prev.next = n;
            current.prev = n;
            current = n;
         case FOLLOWING:
            n.prev = current;
            n.next = current.next;
            current.next.prev = n;
            current.next = n;
            current = n;
         case LAST:
            n.prev = last;
            n.next = null;
            last.next = n;
            last = n;
            current = n;
         default:                         // what do I do for default?  throw an exception?
            throw new UnsupportedOperationException();
      }
   }

}
