// $Id: jgrep.java,v 1.96 2014-04-15 01:07:36-07 - - $

//
// This program is a stub showing how to create a pattern from a
// regular expression.  It does not handle options or files, and
// has some other bugs which you must discover and fix.
//

import java.io.*;
import java.util.Scanner;
import java.util.regex.*;
import static java.lang.System.*;

class jgrep {

   static void scanfile (Scanner input, String filename,
                         Pattern pattern, options opts) {
      int line_number = 0;
      while (input.hasNextLine()) {
         String line = input.nextLine();
         line_number++;
         boolean matches = pattern.matcher (line).find();
         if (opts.reverse_match == true){
            if (!matches) {
               messages.exit_status = messages.EXIT_SUCCESS;
               if (opts.filename_only && !opts.number_lines) {
                  out.printf ("%s%n", filename);
                  break;
               }
               else if (!opts.filename_only && opts.number_lines) {
                  out.printf ("%s:%d:%s%n", filename, line_number,
                              line);
               }
               else if (!opts.filename_only && !opts.number_lines) {
                  out.printf ("%s:%s%n", filename, line);
               }
            }
         }
         else if (opts.reverse_match == false) {
            if (matches) {
               messages.exit_status = messages.EXIT_SUCCESS;
               if (opts.filename_only && !opts.number_lines) {
                  out.printf ("%s%n", filename);
               }
               else if (!opts.filename_only && opts.number_lines) {
                  out.printf ("%s:%d:%s%n", filename, line_number,
                  line);
               }
               else if (!opts.filename_only && !opts.number_lines) {
                  out.printf ("%s:%s%n", filename, line);
               }
            }
         }
      }
   }

   public static void main (String[] args) {
      options opts = new options (args);
      Pattern pattern;
      try {
         if (opts.insensitive){
            pattern = Pattern.compile (opts.regex,
                                       Pattern.CASE_INSENSITIVE);
         }
         else{
            pattern = Pattern.compile (opts.regex);
         }
         if (opts.filenames.length == 0) {
            scanfile (new Scanner (in), "<stdin>", pattern, opts);
         }else {
            for (int argi = opts.hasOption;
                 argi < opts.filenames.length;
                 ++argi) {
               try {
                  String filename = opts.filenames[argi];
                  Scanner input = new Scanner (new File (filename));
                  scanfile (input, filename, pattern, opts);
                  input.close();
               }catch (IOException error) {
                  messages.warn (error.getMessage());
               }
            }
         }
      }catch (PatternSyntaxException error) {
         messages.die (error.getMessage());
      }
      exit (messages.exit_status);
   }
}
