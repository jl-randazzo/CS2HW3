/*
 * I used the standard frequency counter from the book, but I modified it to read the file through my own scanner rather than StdIn
 * because it was becoming a huge hassle the other way.
 */

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

public class FrequencyCounter {

    // Do not instantiate.
    private FrequencyCounter() { }

    /**
     * Reads in a command-line integer and sequence of words from
     * standard input and prints out a word (whose length exceeds
     * the threshold) that occurs most frequently to standard output.
     * It also prints out the number of words whose length exceeds
     * the threshold and the number of distinct such words.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        int distinct = 0, words = 0, puts = 0, gets = 0;
        File file = new File("tale.txt");
        Scanner scn = new Scanner(file);
        int minlen = Integer.parseInt(args[0]);
        ST<String, Integer> st = new ST<String, Integer>();
        String last = " ";

        // compute frequency counts
        while (scn.hasNext()) {
            String key = scn.next();
            if (key.length() < minlen) continue;
            words++;
            gets++; //contains method calls get method unless key is null, which is impossible in this situation
            if (st.contains(key)) {
                st.put(key, st.get(key) + 1);
                puts++; gets++; //one put one get to iterate the value of symbols already in the table
                last = key;
            }
            else {
                st.put(key, 1);
                puts++; //one put for new symbols
                distinct++;
                last = key;
            }

        }
        scn.close();

        // find a key with the highest frequency count
        String max = "";
        st.put(max, 0); //this accounts for the +1 in the (D+1) in the formula.
        // We have to add to the symbol table so that the first get() call for max in the loop below will work
        puts++;//for the extra put above
        for (String word : st.keys()) {
            gets += 2; //2 gets per symbol in the table for the condition below
            if (st.get(word) > st.get(max))
                max = word;
        }

        //I removed all of these for now as they were just cluttering the output, you can remove the
        //slashes if you want to see the output I used for the other answers
        //StdOut.println(max + " " + st.get(max)); gets++;
        //StdOut.println("distinct = " + distinct);
        //StdOut.println("words processed = " + words);
        //StdOut.println("last word processed = " + last);
        //StdOut.println("Gets = " + gets);
        //StdOut.println("Puts = " + puts);

        /* I originally engineered this to delete the last key retrieved from the Symbol Table
        *  and it WAS working, but unfortunately once I dipped beneath words of a minimum of 10 characters
        *  I started running into memory issues on my computer and the program was crashing, so instead
        *  I made it so that the program kept track of the last value and wouldn't repeat the symbol
        *  that had the last value. Theoretically, this leaves open the possibility that if two words
        *  had the same value, they wouldn't be accounted for in this program, but in a book of
        *  135,000 words, I viewed that as so unbelievably unlikely that it wasn't even worth
        *  accounting for that case, but if I were to engineer this to search a poem or something shorter
        *  I would use the method I initially outlined where I'd delete the Symbol from the ST altogether
        *  once I'd inserted it into my dual String array. I just commented out the old code so you can see it
        *  where it appeared. Keep in mind that the second condition within the if statement would be removed
        *  if I were actually deleting the symbols from the table.
        *  This is definitely a brute force method as it examines even the symbols with small
        *  values over and over and over again. Even then, it should be proportional to O(10N) since the outer for loop
        *  is only as long as it needs to be to find the top ten most repeated words.
        */
        String[][] table = new String[11][2]; //will be fed to my printTable method later
        String mostFrequent = " ";
        int maxVal = 0;

        //lastVal is initialized to be the number of words in the entire book minus the number of distinct words
        //because that's the most number of times any one word can possibly be repeated by definition
        //(although even that's also impossible if the author intends for the book to be at all readable)

        int lastVal = words-distinct;
        table[0]= new String[]{"word", "number"}; //inputing table header

        for(int i = 1; i <= 10; i++){
            for(String word : st.keys()){
                if(st.get(word) > maxVal && st.get(word) < lastVal) {
                    maxVal = st.get(word);
                    mostFrequent = word;
                }
            }
            table[i]= new String[]{mostFrequent, "" + maxVal};//filling the table
            //st.delete(mostFrequent);
            lastVal = maxVal;
            maxVal = 0;
        }
        printTable(table);
    }

    public static void printTable(String[][] table){
        /*
        * Pretty straightforward, I went ahead and formatted it to be receptive of different types of inputs
        * if I had different local methods that wanted to print tables of different sizes. It doesn't assume
        * that I'm trying to print a table of 10 rows and two columns. Just for practice.
        */

        for (int i = 0; i < table.length; i++){
            for(int j = 0; j < table[i].length; j++){
                System.out.printf("%-14.14s" + "  |", table[i][j]);
            }
            System.out.println();
        }
    }
}