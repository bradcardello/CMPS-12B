// Brad Cardello (bcardell
// Kara Ekiss (kekiss))
// $Id: dllist.java,v 1.40 2014-04-29 03:09:53-07 - - $

import java.util.*;
import static java.lang.System.*;

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
   private int nodecount = 0;
   
   public void setposition (position pos) {
      switch (pos){
         case FIRST:
            currentposition = 0;
            current = first;
            break;
         case PREVIOUS:
            currentposition -= 1;
            current = current.prev;
            break;
         case FOLLOWING:
            currentposition += 1;
            current = current.next;
            break;
         case LAST:
            currentposition = nodecount - 1;
            current = last;
            break;
         default:
            auxlib.exit_status = auxlib.EXIT_FAILURE;
            auxlib.die();
            throw new UnsupportedOperationException();  
      }
   }

   public boolean isempty () {
      return (nodecount == 0);
   }

   public String getitem () {
      if (current.item == null){
         throw new NoSuchElementException();
      }
      else{
         return current.item;
      }
   }

   public int getposition () {
      if (nodecount == 0) {
         throw new NoSuchElementException();
      }
      else {
         return currentposition;
      }
   }

   public void delete () {
      if (nodecount == 0){
         throw new NoSuchElementException();
      }
      else{
         current.prev.next = current.next;
         current.next.prev = current.prev;
         if (current.next == null){
            current = current.prev;
         }
         else{
            current = current.next;
         }
         nodecount--;
      }
   }

   public void insert (String item, position pos) {
      node n = new node();
      node l;
      node r;
      n.item = item;
      nodecount++;
      if (first == null){
         n.prev = null;
         n.next = null;
         first = n;
         last = n;
         current = n;
         return;
      }
      switch (pos){
         case FIRST:
            n.next = first;
            first.prev = n;
            first = n;
            current = n;
            break;
         case PREVIOUS:
            n.prev = current.prev;
            n.next = current;
            if (current.next != null){
               current.prev.next = n;
            }
            current.prev = n;
            current = n;
            break;
         case FOLLOWING:
            n.prev = current;
            n.next = current.next;
            if (current.next != null){
               current.next.prev = n;
            }
            current.next = n;
            current = n;
            break;
         case LAST:
            n.prev = last;
            n.next = null;
            last.next = n;
            last = n;
            current = n;
            break;
         default:
            auxlib.exit_status = auxlib.EXIT_FAILURE;
            auxlib.die();
            throw new UnsupportedOperationException();
      }
   }

}

