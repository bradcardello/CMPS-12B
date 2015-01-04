// Brad Cardello (bcardell)
// $Id: edfile.java,v 1.3 2014-04-24 15:37:55-07 - - $

import java.util.Scanner;
import static java.lang.System.*;

class edfile{

   // main question is: HOW DO YOU WRITE IT TO A NEW FILE (AND DOING STUFF WITH FILES IN GENERAL)
   public static void main (String[] args) {
      boolean want_echo = true;
      dllist lines = new dllist ();
      auxlib.STUB ("Check for -e option");
      auxlib.STUB ("Load file from args filename, if any.");
      Scanner stdin = new Scanner (in);
      for (;;) {
         out.printf ("%s: ", auxlib.program_name());
         if (! stdin.hasNextLine()) break;
         String inputline = stdin.nextLine();
         if (want_echo) out.printf ("%s%n", inputline);
         if (inputline.matches ("^\\s*$")) continue;
         char command = inputline.charAt(0);
         String inptln_substr = inputline.substring(1);
         switch (command) {
            case '#': break;
            case '$': lines.setposition(dllist.position.LAST); break;        // current line set to be the last line
            case '*': auxlib.STUB ("Call * command function."); break;           // all lines in the list are printed, current line is set to be last
            case '.': out.printf("%s", inputline); break;        // current line is printed
            case '0': lines.setposition(dllist.position.FIRST); break;           // current line is set to be first
            case '<': lines.setposition(dllist.position.PREVIOUS); break;        // current line is set to be previous
            case '>': lines.setposition(dllist.position.FOLLOWING); break;           // current line is set to be following
            case 'a': auxlib.STUB ("Call a command function."); break;        // The text following the letter a is inserted after the current line
            case 'd': lines.delete(); break;           // The current line in the list is deleted
            case 'i': 
               lines.insert(inptln_substr, dllist.position.PREVIOUS);
               break;                                                   // The text following the letter i is inserted before the current line
            case 'r': 
               lines.insert(inptln_substr, dllist.position.FOLLOWING);
               break;           // The contents of the specified file are read in and inserted 
                                                                                 // after the current line (this is where the function in step 7 comes in????)
            case 'w': auxlib.STUB ("Call w command function."); break;        // All of the lines in the file are written to the specified file.
            default :
               auxlib.usage(command);              // is this right?
               break;             // print something saying that this was an invalid command
         }
      }
      auxlib.STUB ("(eof)");                                                  // what do I do here?  get rid of it?
   }

}
