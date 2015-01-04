// Brad Cardello (bcardell)
// Kara Ekiss (kekiss)
// $Id: treemap.java,v 1.45 2014-05-13 00:45:58-07 - - $

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
      node n = new node();
      n.key = key;
      n.value = value;
      n.left = null;
      n.right = null;
      if (root == null) {
         root = n;
         return null;
      }else {
         node curr = root;
         while (curr != null) {
            int comp = key.compareTo (curr.key);
            if (comp < 0) {
               if (curr.left == null) {
                  curr.left = n;
                  break;
               }else {
                  curr = curr.left;
               }
            }else if (comp == 0) {
               value_t tmp = curr.value;
               curr.value = value;
               tmp = value;
               return tmp;
            }else if (comp > 0) {
               if (curr.right == null) {
                  curr.right = n;
                  break;
               }else {
                  curr = curr.right;
               }
            }
         }
      }
      return null;
   }

   public void debug_dump () {
      debug_dump_rec (root, 0);
   }

   public void do_visit (visitor <key_t, value_t> visit_fn) {
      do_visit_rec (visit_fn, root);
   }

}
