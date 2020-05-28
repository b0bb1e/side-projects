package lines;

// for reading from levels.dat
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

/**
 * Stores information about a level of Lines, and can read that information
 * @author faith
 */
public class Level {
	/**
	 * the # of rows in this Level's Grid
	 */
	private int rows;
	/**
	 * the # of columns in this Level's Grid
	 */
	private int cols;
	/**
	 * the sets of end-points in this Level's Grid
	 */
	private int[][] sets;
	/**
	 * the coordinates of this Level's block-tiles
	 */
	private int[][] blocks;
	
	/**
	 * Initializes all instance variables
	 * @param rows the # of rows in this Level's Grid
	 * @param cols the # of columns in this Level's Grid
	 * @param sets the sets of end-points in this Level's Grid
	 * @param blocks the coordinates of this Level's block-tiles
	 */
	public Level(int rows, int cols, int[][] sets, int[][] blocks) {
		this.rows = rows;
		this.cols = cols;
		this.sets = sets;
		this.blocks = blocks;
	}
	
	/**
	 * Getter for this.rows
	 * @return the # of rows in this Level's Grid
	 */
	public int getRows() {return rows;}
	
	/**
	 * Getter for this.cols
	 * @return the # of columns in this Level's Grid
	 */
	public int getCols() {return cols;}
	
	/**
	 * Getter for this.sets
	 * @return the sets of end-points in this Level's Grid
	 */
	public int[][] getSets() {return sets;}
	
	/**
	 * Getter for this.blocks
	 * @return the coordinates of this Level's block-tiles
	 */
	public int[][] getBlocks() {return blocks;}
	
	/**
	 * Reads information about Levels from levels.dat
	 * @return an array of the read Levels, or null if an exception occurs
	 */
	public static Level[] readLevels() {
		// initialize return variable
		Level[] levels = null;
		
		try {
			// point a Scanner at the file
			Scanner reader = new Scanner(new File("src/lines/levels.dat"));
			// read the number of levels
			levels = new Level[reader.nextInt()];
			
			// loop over all Levels to read
			for (int level = 0; level < levels.length; ++level) {
				// read the # of rows for this Level
				int rows = reader.nextInt();
				// read the # of columns for this Level
				int cols = reader.nextInt();
				// read the # of end-point sets for this Level
				int numSets = reader.nextInt();
				// read the # of block-tile coordinates for this Level
				int numBlocks = reader.nextInt();
				
				// initialize the sets & blocks arrays to the proper size
				int[][] sets = new int[numSets][4];
				int[][] blocks = new int[numBlocks][2];
				
				// loop over all sets of end-points to read
				for (int set = 0; set < numSets; ++set) {
					// read the coordinates of the first set
					sets[set][0] = reader.nextInt();
					sets[set][1] = reader.nextInt();
					// skip over arrow in the middle
					reader.next();
					// read the coordinates of the second set
					sets[set][2] = reader.nextInt();
					sets[set][3] = reader.nextInt();
				}
				
				// loop over all block-tile coordinates to read
				for (int block = 0; block < numBlocks; ++block) {
					// read the coordinates of this block-tile
					blocks[block][0] = reader.nextInt();
					blocks[block][1] = reader.nextInt();
				}
				
				// initialize a Level with this information
				levels[level] = new Level(rows, cols, sets, blocks);
			}
			
			// clean up
			reader.close();
		}
		
		// catch some exceptions
		catch (IOException e) {
			System.out.println("Something went wrong with the I/O levels.dat file-read");
			e.printStackTrace();
		}
		catch (Exception e) {
			System.out.println("Something besides I/O went wrong when reading levels.dat");
			e.printStackTrace();
		}
		
		return levels;
	}
	
	public String toString() {
		// initialize return variable with grid size
		String ret = rows + "x" + cols + "\nstarts at\n";
		// for each set of end-point coordinates, add as coordinate pairs with an arrow
		for (int[] set : sets)
			ret += "(" + set[0] + ", " + set[1] + ") -> (" + set[3] + ", " + set[4] + ")\n"; 
		// if there are blocks, add an identifier of the shift
		if (blocks.length > 0) ret += "blocks at\n";
		// for each block-tile coordinate, add as a coordinate pair
		for (int[] block : blocks) ret += "(" + block[0] + ", " + block[1] + ")\n";
		
		return ret;
	}
}