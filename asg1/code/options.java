// $Id: options.java,v 1.29 2014/04/15 08:07:36 - - $

import static java.lang.System.*;

class options {
   boolean insensitive = false;
   boolean filename_only = false;
   boolean number_lines = false;
   boolean reverse_match = false;
   String regex;
   String[] filenames;
   int hasOption = 0;

   options (String[] args) {
      if (args.length == 0) { 
         err.printf ("Usage: %s [-ilnv] regex [filename...]%n",
                     messages.program_name);
         exit (messages.EXIT_FAILURE);
      }
      else if (args[0].startsWith("-")){
         if(args[0].contains("i")){
            insensitive = true;
            hasOption = 1;
         }
         if (args[0].contains("l")){
            filename_only = true;
            hasOption = 1;
         }
         if (args[0].contains("n")){
            number_lines = true;
            hasOption = 1;
         }
         if (args[0].contains("v")){
            reverse_match = true;
            hasOption = 1;
         }
         else if (!args[0].contains("i") && !args[0].contains("l")
                  && !args[0].contains("n") && !args[0].contains("v")){
            err.printf ("Usage: %s [-ilnv] regex [filename...]%n",
                        messages.program_name);
            exit (messages.EXIT_SYNTAX_ERROR);
         }
         regex = args[1];
         filenames = new String[args.length - 1];
         for (int argi = 2; argi < args.length; ++argi) {
            filenames[argi - 1] = args[argi];
         }
      }
      else if (!args[0].contains("-")){
         hasOption = 0;
         regex = args[0];
         filenames = new String[args.length - 1];
         for (int argi = 1; argi < args.length; ++argi) {
            filenames[argi - 1] = args[argi];
         }  
      }
   }
}

