package pipes;

// for reading from levels.dat
import java.util.Scanner;
import java.io.File;

/**
 * Information about a level of Pipes
 * @author faith
 */
public class Level {
	/**
	 * a grid of type-of-tile chars
	 */
	private char[][] grid;
	/**
	 * information about end-pipes
	 */
	private int[][] ends;
	
	/**
	 * Initializes a Level
	 * @param grid a grid of type-of-tile chars
	 * @param ends information about end-pipes
	 */
	public Level(char[][] grid, int[][] ends) {
		this.grid = grid;
		this.ends = ends;
	}
	
	/**
	 * Gets this.grid
	 * @return the grid of type-of-tile chars for this Level
	 */
	public char[][] getGrid() {return grid;}
	
	/**
	 * Gets this.ends
	 * @return information about end-pipes for this Level
	 */
	public int[][] getEnds() {return ends;}
	
	/**
	 * Reads information about Levels from levels.dat
	 * @return the completed array of Levels
	 */
	public static Level[] readLevels() {
		// declare return variable
		Level[] levels = null;
		try {
			// point a Scanner at levels.dat
			Scanner reader = new Scanner(new File("src/pipes/levels.dat"));
			// read the number of levels, initializing the array size
			levels = new Level[reader.nextInt()];
			
			// loop over each level to read
			for (int level = 0; level < levels.length; ++level) {
				// read dimensions of this Level's grid
				int rows = reader.nextInt();
				int cols = reader.nextInt();
				// initialize the grid
				char[][] grid = new char[rows][cols];
				
				// loop over each cell in grid, reading chars into it
				for (int row = 0; row < rows; ++row)
					for (int col = 0; col < cols; ++col)
						grid[row][col] = reader.next().charAt(0);
				
				// read the number of color-sets in this Level
				int sets = reader.nextInt();
				// initialize a ragged array with the proper size
				int[][] ends = new int[sets][];
				// loop over each color-set to read
				for (int set = 0; set < sets; ++set) {
					// read the number of end-pipes to read
					ends[set] = new int[reader.nextInt() * 3];
					// loop over end-pipes to read information about
					for (int end = 0; end < ends[set].length / 3; ++end) {
						// read coordinates of this end-pipe, and exit-side
						ends[set][end * 3] = reader.nextInt();
						ends[set][end * 3 + 1] = reader.nextInt();
						ends[set][end * 3 + 2] = Tile.stringToSide(reader.next());
					}
				}
				
				// add this Level in to levels
				levels[level] = new Level(grid, ends);
			}
			
			// clean up
			reader.close();
		}
		// if something happens, print it out
		catch (Exception e) {e.printStackTrace();}
		
		return levels;
	}
}