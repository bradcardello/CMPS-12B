// Brad Cardello (bcardell)
// Kara Ekiss (kekiss)
// $Id: jxref.java,v 1.32 2014/05/13 08:02:18 - - $

import java.io.*;
import java.util.Scanner;
import static java.lang.System.*;

class jxref {
   static final String STDIN_NAME = "-";
   static boolean debug_dump = false;
   static boolean fold_letters = false;
   static int hasOpt = 0;

   static class printer implements visitor <String, queue <Integer>> {
      public void visit (String key, queue <Integer> value) { 
         out.printf ("%s %s", key, value);
         for (int linenr: value) out.printf (" %d", linenr);
         out.printf ("%n");
      }
   }

   static void xref_file (String filename, Scanner scan,
                          boolean fold_letters, boolean debug_dump) {
      treemap <String, queue <Integer>> map =
            new treemap <String, queue <Integer>> ();
      for (int linenr = 1; scan.hasNextLine (); ++linenr) {
         for (String word: scan.nextLine().split ("\\W+")) {
            if (word.matches ("^\\d*$") ) continue;
            if (fold_letters) {
               word = word.toLowerCase();
            }
            queue<Integer> q = map.get (word);
            if (q != null) {
               q.insert (linenr);
            }else {
               queue <Integer> new_q = new queue <Integer>();
               new_q.insert (linenr);
               map.put (word, new_q);
            }
         }
      }
      if (debug_dump) {
         print_filename (filename);
         map.debug_dump();
      }else {
         visitor <String, queue <Integer>> print_fn = new printer ();
         print_filename (filename);
         map.do_visit (print_fn);
      }
   }

   static void print_filename (String filename){
      out.printf("\n::::::::::::::::::::::::::::::::");
      out.printf(":::::::::::::::::::::::::::::::::\n");
      out.printf("%s%n", filename);
      out.printf("::::::::::::::::::::::::::::::::");
      out.printf(":::::::::::::::::::::::::::::::::\n\n");
   }

   static void getopt (String opts, String[] args) {
      opts = null;
      if (args[0].startsWith("-")){
         hasOpt = 1;
         opts = args[0].substring(1);
         if (opts.equals("d")) {
            debug_dump = true;
         }
         else if (opts.equals("f")) {
            fold_letters = true;
         }
         else if (opts.equals("df")) {
            debug_dump = true;
            fold_letters = true;
         }
         else if (opts.equals("fd")) {
            debug_dump = true;
            fold_letters = true;
         }
         else {
            auxlib.usage_exit ("[-df] [filename...]");
         }
      }
   }
   
   private static int file_index (boolean hasOpt) {
      if (hasOpt) { return 1; }
      else { return 0; }
   }

   public static void main (String[] args) {
      getopt (args[0], args);
      if (args.length == 0) {
         xref_file (STDIN_NAME, new Scanner (in),
                    fold_letters, debug_dump);
      }else {
         int file_pos = file_index (hasOpt == 1);
         for (int argi = file_pos; argi < args.length; ++argi) {
            String filename = args[argi];
            if (filename.equals (STDIN_NAME)) {
               xref_file (STDIN_NAME, new Scanner (in),
                          fold_letters, debug_dump);
            }else {
               try {
                  Scanner scan = new Scanner (new File (filename));
                  xref_file (filename, scan, fold_letters, debug_dump);
                  scan.close ();
               }catch (IOException error) {
                  auxlib.warn (error.getMessage ());
               }
            }
         }
      }
      auxlib.exit ();
   }

}

