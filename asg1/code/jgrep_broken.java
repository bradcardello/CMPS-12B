// $Id: jgrep.java,v 1.24 2014-04-10 21:46:55-07 - - $

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
         line_number += 1;
         boolean matches = pattern.matcher (line).find();
         if (opts.reverse_match == true){
            matches = !matches;
         }
         if (matches == false && opts.reverse_match == false){
           continue;
         }
         if ((matches == true && opts.reverse_match == false) || (matches == false && opts.reverse_match == true)){
            if (opts.filename_only == true && opts.number_lines == false){
               out.printf ("%s%n", filename);
               continue;
            }
            else if (opts.filename_only == true && opts.number_lines == true){
               out.printf ("%s:%d%n", filename, line_number);
               continue;
            }
            else if (opts.number_lines == true && opts.filename_only == false){
               out.printf ("%s:%d:%s%n", filename, line_number, line);
               continue;
            }
            else{
               out.printf ("%s:%s%n", filename, line);
               continue;
            }
         } //*********************************************************************
         if (matches == true && opts.filename_only == false && opts.number_lines == false && opts.reverse_match == true) {
            out.printf ("%s:%s%n", filename, line);
         }
         else if (matches == true && opts.filename_only == true && opts.number_lines == false && opts.reverse_match == true){
            out.printf ("%s%n", filename);
         }
         else if (matches == true && opts.filename_only == true && opts.number_lines == true && opts.reverse_match == true){
            out.printf ("%s:%d%n", filename, line_number);
         }
         else if (matches == true && opts.number_lines == true && opts.filename_only == false && opts.reverse_match == true){
            out.printf ("%s:%d:%s%n", filename, line_number, line);
         }
         if (matches == true && opts.filename_only == false && opts.number_lines == false && opts.reverse_match == false){
            out.printf ("%s:%s%n", filename, line);
         }
         else if (matches == true && opts.filename_only == true && opts.number_lines == false && opts.reverse_match == false){
            continue;
         }
         else if (matches == true && opts.filename_only == true && opts.number_lines == true && opts.reverse_match == false){
            continue;
         }
         else if (matches == true && opts.number_lines == true && opts.filename_only == false && opts.reverse_match == false){
            continue;
         }
      }
   }

   public static void main (String[] args) {
      options opts = new options (args);
      Pattern pattern;
      try {
         if (opts.insensitive){
            pattern = Pattern.compile (opts.regex, Pattern.CASE_INSENSITIVE);      // should opts.regex just be regex? why?
         }
         else{
            pattern = Pattern.compile (opts.regex);
         }
         if (opts.filenames.length == 0) {
            scanfile (new Scanner (in), "<stdin>", pattern, opts);
         }else {
            for (int argi = 1; argi < opts.filenames.length; ++argi) {
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

