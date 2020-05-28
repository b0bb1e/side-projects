package pipes;

// for drawing
import java.awt.Color;
import java.awt.Graphics;

// for arrays that can change size
import java.util.ArrayList;

/**
 * A grid of Tiles for playing Pipes, 
 * which can rotate tiles, recalculate water flow, and check for completeness
 * @author faith
 */
public class Grid {
	/**
	 * the matrix of Tiles
	 */
	private Tile[][] tiles;
	/**
	 * all end-pipes in this Grid
	 */
	private EndTile[] ends;
	
	/**
	 * Initializes a Grid given a Level
	 * @param level the Level to use information from
	 */
	public Grid(Level level) {
		this(level.getGrid(), level.getEnds());
	}
	
	/**
	 * Initializes a Grid
	 * @param grid a matrix of chars indicating type of Tile in that cell
	 * @param ends the coordinates and open-sides of end-pipes
	 */
	public Grid(char[][] grid, int[][] ends) {
		// initialize the Tile matrix
		tiles = new Tile[grid.length][grid[0].length];
		
		// initialize the list of used colors to Tile background and pipe colors
		ArrayList<Color> colors = new ArrayList<Color>();
		colors.add(Tile.BACKGROUND);
		colors.add(Tile.PIPE);
		
		// loop over all cells of Tiles
		for (int row = 0; row < tiles.length; ++row) 
			for (int col = 0; col < tiles[0].length; ++col) {
				// if a 'B', set up a Bend tile
				if (grid[row][col] == 'B') 
					tiles[row][col] = MiddleTile.getBendTile(row, col);
				// if a 'S', set up a Straight tile
				else if (grid[row][col] == 'S')
					tiles[row][col] = MiddleTile.getStraightTile(row, col);
				// if a 'F', set up a Fork tile
				else if (grid[row][col] == 'F')
					tiles[row][col] = MiddleTile.getForkTile(row, col);
				// if a 'A', set up a All tile
				else if (grid[row][col] == 'A')
					tiles[row][col] = MiddleTile.getAllTile(row, col);
			}
		
		// initialize counter for # of end-pipes
		int numEnds = 0;
		// loop over all color-sets in ends
		for (int[] set : ends) {
			// get a random contrasting color, and add it to the list of colors
			Color cur = lines.Grid.randomContrastColor(colors);
			colors.add(cur);
			// loop each end-pipe's information
			for (int i = 0; i < set.length; i += 3) {
				// initialize the EndTile, and increment end-pipe counter
				tiles[set[i]][set[i + 1]] = new EndTile(set[i], set[i + 1], set[i + 2], cur);
				++numEnds;
			}
		}
		
		// initialize the size of the end-pipe list
		this.ends = new EndTile[numEnds];
		// the index to add an EndTile to
		int index = 0;
		// loop over each EndTile's information
		for (int[] set : ends) for (int i = 0; i < set.length; i += 3)
			// add this EndTile to the array of EndTiles
			this.ends[index++] = (EndTile) tiles[set[i]][set[i + 1]];
	}

	/**
	 * Get the columns in the Grid
	 * @return the number of columns in tiles
	 */
	public int getCols() {return tiles[0].length;}
	
	/**
	 * Rotate the Tile in the given position
	 * @param row the row of the Tile to rotate
	 * @param col the column of the Tile to rotate
	 */
	public void rotateTile(int row, int col) {
		// only rotate if this tile is valid
		if (isValidTile(row, col)) tiles[row][col].rotate();
	}
	
	/**
	 * Check if this Grid is complete
	 * @return whether any moves are required to complete this Grid
	 */
	public boolean isComplete() {
		// loop over each EndTile
		for (EndTile end : ends)
			// if the Tile right out of this one has the wrong color, not complete
			if (!end.getColor().equals(nextTile(end, end.getSide()).getColor()))
				return false;
		
		// loop over all Tiles
		for (Tile[] row : tiles) for (Tile tile : row) {
			// if this Tile has no water, not complete
			if (tile.getColor() == null) return false;
			
			// loop over all directions to go out
			for (int dir : Tile.directions) {
				// if this side has a pipe
				if (tile.hasSide(dir)) {
					try {
						// try to grab the Tile next to it
						Tile next = nextTile(tile, dir);
						// if this Tile isn't connected, not complete
						if (!tile.isConnected(next)) return false;
					}
					// if there was no Tile next to it, not complete
					catch (IllegalArgumentException e) {return false;}
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Get the Tile next to a given Tile
	 * @param tile the Tile to start at
	 * @param direction the direction to go in
	 * @return the Tile in that direction
	 */
	public Tile nextTile(Tile tile, int direction) {
		try {
			// if UP, try to go up (subtract a row)
			if (direction == Tile.UP)
				return tiles[tile.getRow() - 1][tile.getCol()];
			// similar for DOWN
			else if (direction == Tile.DOWN)
				return tiles[tile.getRow() + 1][tile.getCol()];
			// similar for LEFT
			else if (direction == Tile.LEFT)
				return tiles[tile.getRow()][tile.getCol() - 1];
			// similar for RIGHT
			else if (direction == Tile.RIGHT)
				return tiles[tile.getRow()][tile.getCol() + 1];
		}
		// if any of those indexes were invalid, then the direction went off the grid
		catch (IndexOutOfBoundsException e) 
			{throw new IllegalArgumentException("No Tile in that direction");}
		// if no direction matched, then the direction was invalid
		throw new IllegalArgumentException("Invalid direction");
	}

	/**
	 * Draws the Grid
	 * @param window the window to draw on
	 */
	public void draw(Graphics window) {
		// recalculate water flow
		refillPipes();
		// draw each Tile
		for (Tile[] row : tiles) for (Tile tile : row) tile.draw(window);
	}
	
	/**
	 * Recalculates water flow through all Tiles
	 */
	private void refillPipes() {
		// erase all colors 
		for (Tile[] row : tiles) for (Tile tile : row) tile.setColor(null);
		// flow out from each EndTile
		for (EndTile end : ends) colorAllConnected(end);
	}
	
	/**
	 * Colors all connected, uncolored pipes for this Tile
	 * @param tile the Tile to color from
	 */
	private void colorAllConnected(Tile tile) {
		// loop over all possible directions
		for (int dir : Tile.directions) {
			try {
				// try to grab the Tile in this direction
				Tile next = nextTile(tile, dir);
				// if this Tile is connected and has not water,
				if (tile.isConnected(next) && next.getColor() == null) {
					// have water flow into it, and color all connected to it
					next.setColor(tile.getColor());
					colorAllConnected(next);
				}
			}
			// if there was no Tile in that direction, move on
			catch (IllegalArgumentException e) {}
		}
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
}