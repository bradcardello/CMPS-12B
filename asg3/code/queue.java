// Brad Cardello (bcardell)
// Kara Ekiss (kekiss)
// $Id: queue.java,v 1.5 2014-05-13 00:45:58-07 - - $

import java.util.Iterator;
import java.util.NoSuchElementException;

class queue <item_t> implements Iterable <item_t> {

   private class node {
      item_t item;
      node link;
   }
   private node head = null;
   private node tail = null;

   public boolean isempty () {
      return (head == null);
   }

   public void insert (item_t newitem) {
      node n = new node();
      n.item = newitem;
      n.link = null;
      if (isempty()) {
         head = n;
      }
      else {
         tail.link = n;
      }
      tail = n;
      return;
   }

   public Iterator <item_t> iterator () {
      return new itor ();
   }

   class itor implements Iterator <item_t> {
      node next = head;
      public boolean hasNext () {
         return next != null;
      }
      public item_t next () {
         if (! hasNext ()) throw new NoSuchElementException ();
         item_t result = next.item;
         next = next.link;
         return result;
      }
      public void remove () {
         if (isempty()) return;
         if (head != null) head.link = head;
         if (isempty()) tail.link = null;
      }
   }

}
