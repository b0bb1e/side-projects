package puzzleHelp;

// for the data structures used
import java.util.ArrayList;
import java.util.HashMap;

// for reading from words.dat
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Finds letters adjacent to each other in words
 * @author faith
 */
public class AdjLetters {
	/**
	 * Stores the number of times letters appear adjacent to each other in a HashMap
	 * @param words the words (converted to char arrays) to find adjacent letters from
	 * @return the completed letter-count map
	 */
	public static HashMap<Character, HashMap<Character, Integer>> countAdjLetters(ArrayList<char[]> words) {
		// initialize return variable
		HashMap<Character, HashMap<Character, Integer>> count = 
				new HashMap<Character, HashMap<Character, Integer>>();
		
		// loop over all words
		for (char[] word : words) {
			// loop over all letters that start a pair in this word
			for (int i = 0; i < word.length - 1; ++i) {
				// if either letter is missing an entry in count, add an entry
				count.putIfAbsent(word[i], new HashMap<Character, Integer>());
				count.putIfAbsent(word[i + 1], new HashMap<Character, Integer>());
				// if these letters were adjacent to each other before 
				// (they have count reference to each other)
				if (count.get(word[i]).containsKey(word[i + 1])) {
					// update each count reference to add another adjacency count
					count.get(word[i]).put(word[i + 1], count.get(word[i]).get(word[i + 1]) + 1);
					count.get(word[i + 1]).put(word[i], count.get(word[i]).get(word[i + 1]));
				}
				// or if these letters have not been adjacent before
				else {
					// create a new count reference for each, initializing to 1
					count.get(word[i]).put(word[i + 1], 1);
					count.get(word[i + 1]).put(word[i], 1);
				}
			}
		}
		
		return count;
	}
	
	/**
	 * Prints out the adjacencies in a nice form
	 * @param words the words to find letter adjacencies in
	 */
	public static void printAdjLetters(ArrayList<char[]> words) {
		// get the count map
		HashMap<Character, HashMap<Character, Integer>> count = countAdjLetters(words);
		
		// loop over all letters which appear in map
		for (Character start : count.keySet()) {
			// start line with that letter
			System.out.print(start + ": ");
			// loop over all letters adjacent to this letter
			for (Character end : count.get(start).keySet())
				// print out the letter and the adjacency count
				System.out.print(end + "(" + count.get(start).get(end) + ") ");
			// newline for next letter
			System.out.println();
		}
	}
	
	/**
	 * Reads words from file and prints out adjacencies
	 * @param args not used
	 * @throws FileNotFoundException if the file words.dat is not where it is expected
	 */
	public static void main(String args[]) throws FileNotFoundException {
		// initialize list of words
		ArrayList<char[]> words = new ArrayList<char[]>();
		// point scanner at file
		Scanner reader = new Scanner(new File("src/puzzleHelp/words.dat"));
		// add each word's char array to the words list
		while (reader.hasNext()) words.add(reader.next().toCharArray());
		// clean up
		reader.close();
		// print out adjacencies
		printAdjLetters(words);
	}
}