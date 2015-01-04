// Brad Cardello (bcardell)
// Kara Ekiss (kekiss)
// $Id: edfile.java,v 1.47 2014-04-28 19:09:11-07 - - $

import java.io.*;
import java.util.Scanner;
import static java.lang.System.*;

class edfile{
   static void scanfile (Scanner input, String filename, dllist lines){
      while (input.hasNextLine()) {
         String line = input.nextLine();
         out.println( line);
         lines.insert(line, dllist.position.FOLLOWING);
      }
   }

   private static boolean wants_echo (String[] args){
      boolean wants_echo = false;
      if (args[0].equals("-e")){
         wants_echo = true;
      }
      return wants_echo;
   }

   private static int file_pos (boolean wants_echo){
      if (wants_echo == true){
         return 1;
      }
      else{
         return 0;
      }
   }

   public static void main (String[] args) {
      boolean want_echo;
      dllist lines = new dllist ();
      String option = null;
      auxlib.STUB ("Check for -e option");
      want_echo = wants_echo (args);
      String filename = args[file_pos (want_echo)];
      try {
         Scanner input = new Scanner (new File (filename));
         scanfile (input, filename, lines);
      }catch (IOException error) {
         auxlib.die ();
      }

      auxlib.STUB ("Load file from args filename, if any.");
      Scanner stdin = new Scanner (in);
      for (;;) {
         out.printf ("%s: ", auxlib.program_name());
         if (! stdin.hasNextLine()) break;
         String inputline = stdin.nextLine();
         if (want_echo) out.printf ("%s%n", inputline);
         if (inputline.matches ("^\\s*$")) continue;
         char command = inputline.charAt(0);
         
         switch (command) {
            case '#':
               break;
            case '$':
               lines.setposition (dllist.position.LAST);
               out.printf("%s%n", lines.getitem());
               break;
            case '*':
               auxlib.STUB ("Call * command function.");
               break;
            case '.':
               out.printf("%s%n", lines.getitem());
               break;
            case '0':
               lines.setposition(dllist.position.FIRST);
               out.printf("%s%n", lines.getitem());
               break;
            case '<':
               lines.setposition(dllist.position.PREVIOUS);
               out.printf("%s%n", lines.getitem());
               break;
            case '>':
               lines.setposition(dllist.position.FOLLOWING);
               out.printf("%s%n", lines.getitem());
               break;
            case 'a': 
               lines.insert(inputline.substring(1),
                            dllist.position.FIRST);
               lines.setposition(dllist.position.FOLLOWING);
               out.printf("%s%n", lines.getitem());
               break;
            case 'd':
               lines.delete();
               break;
            case 'i': 
               lines.insert(inputline.substring(1),
                            dllist.position.PREVIOUS);
               lines.setposition(dllist.position.PREVIOUS);
               out.printf("%s%n", lines.getitem());
               break;
            case 'r':
               lines.insert(inputline.substring(1),
                            dllist.position.FOLLOWING);
               break;
            case 'w':
               auxlib.STUB ("Call w command function.");
               break;
            default :
               auxlib.exit_status = auxlib.EXIT_FAILURE;
               auxlib.die();
               break;
         }
         
      }

   }
}

