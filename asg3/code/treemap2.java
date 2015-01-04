// $Id: treemap.java,v 1.35 2014-05-12 21:22:14-07 - - $

import static java.lang.System.*;

class treemap <key_t extends Comparable <key_t>, value_t> {

   private class node {
      key_t key;
      value_t value;
      node left;
      node right;
   }
   private node root;

   private void debug_dump_rec (node tree, int depth) {
      if (tree.left != null)
         debug_dump_rec (tree.left, depth + 1);
      for (int indent = 0; indent < depth; indent++)
         out.printf ("  ");
      out.printf ("%d %s %s%n", depth, tree.key, tree.value);
      if (tree.right != null)
         debug_dump_rec (tree.right, depth + 1);
   }

   private void do_visit_rec (visitor <key_t, value_t> visit_fn,
                              node tree) {
      if (tree == null) return;
      do_visit_rec (visit_fn, tree.left);
      visit_fn.visit (tree.key, tree.value);
      do_visit_rec (visit_fn, tree.right);
   }

   public value_t get (key_t key) {
      node curr = root;
      while (curr != null) {
         int comp = key.compareTo (curr.key);
         if (comp < 0)
            curr = curr.left;
         else if (comp == 0)
            return curr.value;
         else 
            curr = curr.right;
      }
      return null;
   }

   public value_t put (key_t key, value_t value) {
      value_t newvalue = null;
      boolean l = false;
      node parent;
      node curr = root;
      while (curr != null) {
         int comp = key.compareTo (curr.key);
         if (comp < 0) {
            l = true;
            if (curr.left == null) {
               node leftnode = new node();
               leftnode.key = key;
               leftnode.value = value;
               leftnode.left = null;
               leftnode.right = null;
               curr.left = leftnode;
               curr = curr.left;
               newvalue = curr.value;
            }else {
               curr.left.value = value;   //might give errors
               newvalue = curr.left.value;
            }
         }else if (comp == 0) {
            // Push new value onto queue
            // 
            // Return old value
            value_t oldvalue = curr.value;
            curr.value = value;
            newvalue = oldvalue;
            //return oldvalue
         }else if (comp > 0) {
            l = false;
            if (curr.right == null) {
               node rightnode = new node();
               rightnode.key = key;
               rightnode.value = value;
               rightnode.left = null;
               rightnode.right = null;
               curr.right = rightnode;
               curr = curr.right;
               newvalue = curr.value;
            }else {
               curr.right.value = value;
               newvalue = curr.left.value;
            }
         }
         node newnode = new node();
         newnode.key = key;
         newnode.value = value;
         newnode.left = null;
         newnode.right = null;
         if (root == null)
            root = newnode;
         else if (l == true)
            parent.left = newnode;
         else
            parent.right = newnode;
      }
      return newvalue;
   }

   public void debug_dump () {
      debug_dump_rec (root, 0);
   }

   public void do_visit (visitor <key_t, value_t> visit_fn) {
      do_visit_rec (visit_fn, root);
   }

}
