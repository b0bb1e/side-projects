package lines;

// for arrays that can change size
import java.util.ArrayList;

// for drawing
import java.awt.Color;
import java.awt.Graphics;

/**
 * A grid of Tiles for playing Lines
 * @author faith
 */
public final class Grid {
	/**
	 * the Tiles in this Grid
	 */
	private final Tile[][] tiles;
	/**
	 * the EndTiles in this Grid
	 */
	private final EndTile[] ends;
	/**
	 * the Paths currently present between Tiles
	 */
	private ArrayList<Path> paths;
	/**
	 * the Path being added to currently
	 */
	private Path activePath;
	
	/**
	 * the minimum difference in RGB values to count as a contrasting color
	 */
	public static final int CONTRAST = 50;
	
	/**
	 * Initializes a Grid to the specifications of a Level
	 * @param level the Level to use
	 */
	public Grid(Level level) {
		// get this Level's information
		this(level.getRows(), level.getCols(), level.getSets(), level.getBlocks());
	}
	
	/**
	 * Initializes the Grid's Tiles
	 * @param rows the number of rows in the Grid
	 * @param cols the number of columns in the Grid
	 * @param sets the locations of path end-points in this Grid
	 * @param blocks the locations of BlockTiles in this Grid
	 */
	public Grid(int rows, int cols, int[][] sets, int[][] blocks) {
		// check for argument validity
		if (rows <= 0 || cols <= 0)
			throw new IllegalArgumentException("Invalid grid dimensions: " + rows + "x" + cols);
		if (sets == null || sets.length == 0)
			throw new IllegalArgumentException("Have to have at least 1 set of endpoints");
		if (blocks == null)
			throw new IllegalArgumentException("Can't have null BlockTile coordinates");
		
		// initialize all instance variables to the right size
		tiles = new Tile[rows][cols];
		ends = new EndTile[sets.length * 2];
		paths = new ArrayList<Path>();
		// there is not currently an active path
		activePath = null;
		
		// loop over all cells in the tiles-grid
		for (int row = 0; row < rows; ++row)
			for (int col = 0; col < cols; ++col)
				// put a PathTile (default) into this cell
				tiles[row][col] = new PathTile(row, col);
		
		// initialize the list of colors already used
		ArrayList<Color> colors = new ArrayList<Color>();
		colors.add(Tile.BLANK);
		
		// loop over all end-point sets
		for (int set = 0; set < sets.length; ++set) {
			// get a random color which contrasts all previous colors
			Color cur = randomContrastColor(colors);
			// add this color to the list of contrasting colors
			
			colors.add(cur);
			// create the EndTiles for this set, and save in ends
			ends[set * 2] = new EndTile(sets[set][0], sets[set][1], cur);
			ends[set * 2 + 1] = new EndTile(sets[set][2], sets[set][3], cur);
			// set the proper spots in the tiles-grid to this EndTiles
			setTile(ends[set * 2]);
			setTile(ends[set * 2 + 1]);
		}
		
		// loop over all block-tile coordinates, setting that spot to a BlockTile
		for (int[] block : blocks) setTile(new BlockTile(block[0], block[1]));
	}
	
	/**
	 * Get the columns in the Grid
	 * @return the number of columns in tiles
	 */
	public int getCols() {return tiles[0].length;}
	
	/**
	 * Grabs the Tile connected to a given Tile by a side
	 * @param tile the Tile to start from
	 * @param side the side of the given Tile to go to
	 * @return the Tile on that side, if it exists
	 * @throws InvalidPathException if no Tile exists on that side
	 */
	public Tile nextTile(Tile tile, byte side) throws InvalidPathException {
		// if the side is UP, and there is a Tile above, return that Tile
		if (side == Tile.UP && isValidTile(tile.getRow() - 1, tile.getCol()))
			return getTile(tile.getRow() - 1, tile.getCol());
		
		// similar logic for DOWN, LEFT, and RIGHT
		
		else if (side == Tile.DOWN && isValidTile(tile.getRow() + 1, tile.getCol()))
			return getTile(tile.getRow() + 1, tile.getCol());
		else if (side == Tile.LEFT && isValidTile(tile.getRow(), tile.getCol() - 1))
			return getTile(tile.getRow(), tile.getCol() - 1);
		else if (side == Tile.RIGHT && isValidTile(tile.getRow(), tile.getCol() + 1))
			return getTile(tile.getRow(), tile.getCol() + 1);
		
		// if no Tile was found, throw an exception
		throw new InvalidPathException(tile, side);
	}
	
	/**
	 * Gets the current active path
	 * @return the active path
	 */
	public Path getActivePath() {
		// checks if the Path is complete, if so updates it to null
		if (activePath != null && activePath.isComplete()) activePath = null;
		
		return activePath;
	}
	
	/**
	 * Gets a Path by a Tile in it
	 * @param tile the Tile to use
	 * @return the Path that contains this Tile
	 */
	public Path getPath(Tile tile) {
		// loop over all Paths, and if they contain the Tile return them
		for (Path path : paths) if (path.containsTile(tile)) return path;
		// if not Path contains the Tile, throw an exception
		throw new IllegalArgumentException(tile + " is not on any path");
	}
	
	/**
	 * Start a Path with a given EndTile and a side to out on
	 * @param tile the Tile to start the Path with
	 * @param side the side of the Tile to go out on
	 * @throws InvalidPathException if the new Path would go in an invalid Tile
	 */
	public void startPath(EndTile tile, byte side) throws InvalidPathException {
		// grab next Tile by side, and attempt to make a Path connecting the two
		Path newPath = new Path(tile, nextTile(tile, side), this);
		// if no exception was thrown making the Path, add it to the list of Paths
		paths.add(newPath);
		// this new Path is active
		activePath = newPath;
	}
	
	/**
	 * Removes a Path from the list of Paths
	 * @param path the Path to remove
	 */
	public void removePath(Path path) {
		// make the Path clear all its Tiles
		path.clear();
		// remove it from the list of Paths
		paths.remove(path);
		// if this Path was previously active, it is active no more
		if (path == activePath) activePath = null;
	}
	
	/**
	 * Removes a Path up to a certain Tile
	 * @param tile the last Tile to keep on the Path
	 */
	public void removePathUpTo(Tile tile) {
		// get the Path with this Tile
		Path remove = getPath(tile);
		// since this Path is being modified, it is active
		activePath = remove;
		
		// while there are Tiles before this one in the Path
		while (remove.lastTile() != tile) {
			// if the Path still exists (it hasn't self-removed)
			if (paths.contains(remove)) 
				// back up one more
				remove.backUp();
			// or if the back-up triggered a self-remove
			else {
				// no path is active, and leave
				activePath = null;
				break;
			}
		}
	}
	
	/**
	 * Attempt to extend an already-present Path by a Tile
	 * @param tile the Tile to attempt to extend by
	 * @return whether the extension succeeded
	 */
	public boolean extendSomePath(Tile tile) {
		// loop over all Paths
		for (Path path : paths) {
			// if this Path isn't already complete
			if (!path.isComplete()) {
				try {
					// attempt to extend
					path.extend(tile);
					// if the extension didn't throw an exception
					// this Path is now active
					activePath = path;
					// note that an extension succeeded
					return true;
				}
				// if the extension failed
				catch (InvalidPathException pathE) {}
			}
		}
		
		// if all Paths couldn't be extended, note that
		return false;
	}
	
	/**
	 * Attempt to start a new Path from a Tile
	 * @param tile the Tile to start a Path with
	 * @return whether the start succeeded
	 */
	public boolean startSomePath(Tile tile) {
		// loop over all possible starting spots
		for (EndTile start : ends) {
			// if this start can be used
			if (!start.isComplete()) {
				try {
					// attempt to start a path using this start and the tile
					Path newPath = new Path(start, tile, this);
					// if the start didn't throw an exception
					
					// add this new path
					paths.add(newPath);
					// this new path is active
					activePath = newPath;
					// note that the start succeeded
					return true;
				}
				// if the start failed
				catch (InvalidPathException pathE) {}
			}
		}
		
		// note that the start failed
		return false;
	}
	
	/**
	 * Attempts to put the given Tile onto some path
	 * @param tile the tile to attempt with
	 */
	public void tryToPutOnPath(Tile tile) {
		// first try to extend a path, if that fails try to start a path
		if (!extendSomePath(tile)) startSomePath(tile);
	}
	
	/**
	 * Attempt to connect some Path to a Tile's Path
	 * @param tile the Tile to use the Path of
	 * @return whether the connection was successful
	 */
	public boolean connectSomePath(Tile tile) {
		try {
			// attempt to get the Path
			Path path = getPath(tile);
			
			// loop over all other Paths available
			for (int other = 0; other < paths.size(); ++other) {
				// if this Path has the same color (can connect)
				if (paths.get(other) != path && paths.get(other).getColor().equals(path.getColor())) {
					try {
						// try to connect it
						path.connect(paths.get(other));
						// note that the connection succeeded
						return true;
					}
					// if the connections failed
					catch (InvalidPathException e) {}
				}
			}
		}
		// if this Tile isn't on a Path, give up
		catch (IllegalArgumentException e) {}
		
		// note that the connection failed
		return false;
	}

	/**
	 * Checks if the Grid is complete
	 * @return if the Grid is complete
	 */
	public boolean isComplete() {
		// loop over all Tiles, and if any is incomplete return false
		for (Tile[] row : tiles) for (Tile tile : row)
			if (!tile.isComplete()) return false;
		return true;
	}
	
	/**
	 * Draw the Grid
	 * @param window the window to draw on
	 */
	public void draw(Graphics window) {
		// loop over all Tiles, drawing each
		for (Tile[] row : tiles) for (Tile tile : row) tile.draw(window);
	}
	
	/**
	 * Set a Tile to its proper location
	 * @param tile the Tile to set
	 */
	public void setTile(Tile tile) {
		tiles[tile.getRow()][tile.getCol()] = tile;
	}
	
	/**
	 * Get a Tile from a location
	 * @param row the row of the Tile
	 * @param col the column of the Tile
	 * @return the Tile in that spot
	 */
	public Tile getTile(int row, int col) {
		if (!isValidTile(row, col))
				throw new IllegalArgumentException("Invalid tile location ("
						+ row + ", " + col + ") in grid " + 
						tiles.length + "x" + tiles[0].length);
		return tiles[row][col];
	}
	
	/**
	 * Checks if a Tile's location is valid
	 * @param row the row of the location
	 * @param col the column of the location
	 * @return whether this location is valid
	 */
	public boolean isValidTile(int row, int col) {
		return row < tiles.length && col < tiles[0].length && row >= 0 && col >= 0;
	}
	
	/**
	 * Generate a random color
	 * @return a Color with random RBG values in 0-255 range
	 */
	public static Color randomColor() {
		return new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
	}
	
	/**
	 * Checks if two numbers are different enough
	 * @param one the first number
	 * @param two the second number
	 * @return whether the numbers differ by more than CONTRAST
	 */
	public static boolean differ(int one, int two) {
		return Math.abs(one - two) >= CONTRAST;
	}
	
	/**
	 * Checks if two colors are contrasting (only 1 of RGB can be within CONTRAST)
	 * @param one the first color
	 * @param two the second color
	 * @return whether the colors contrast enough
	 */
	public static boolean areContrasting(Color one, Color two) {
		// if R contrasts, only one of B or G must contrast
		if (differ(one.getRed(), two.getRed()))
			return differ(one.getBlue(), two.getBlue()) || 
					differ(one.getGreen(), two.getGreen());
		// if R doesn't contrast, both B and G must contrast
		else
			return differ(one.getBlue(), two.getBlue()) &&
					differ(one.getGreen(), two.getGreen());
	}
	
	/**
	 * Checks if a color contrasts against all colors in an ArrayList
	 * @param check the color to check for contrasting
	 * @param contrast the colors that must be contrasted against
	 * @return whether the color contrasts each color in contrast
	 */
	public static boolean allContrast(Color check, ArrayList<Color> contrast) {
		// loop over all colors to contrast against, and if any don't contrast return false
		for (Color color : contrast) if (!areContrasting(check, color)) return false;
		// if all colors contrasted, return so
		return true;
	}
	
	/**
	 * Generate a random color with sufficient contrast to other colors
	 * @param contrast the color to contrast with
	 * @return a random contrasting color
	 */
	public static Color randomContrastColor(ArrayList<Color> contrast) {
		// declare return variable
		Color cur;
		
		// get a random color
		do {cur = randomColor();}
		// if it doesn't contrast enough, run the loop again
		while (!allContrast(cur, contrast));
		
		return cur;
	}
	
	public String toString() {
		// declare class on own line
		String ret = "Grid\n";
		// loop over all rows of tiles
		for (Tile[] row : tiles) {
			// add in tiles, tab-separated
			for (Tile tile : row) ret += tile + "\t";
			// newline between rows
			ret += '\n';
		}
		// add in active path at the end
		return ret + "active path = " + activePath;
	}
}