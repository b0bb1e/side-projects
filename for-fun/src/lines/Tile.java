package lines;

// for drawing
import java.awt.Color;
import java.awt.Graphics;

/**
 * A cell in a grid for playing Lines, can have a path through self.
 * Contains various helper static methods.
 * @author faith
 *
 */
public abstract class Tile {
	/**
	 * the row of this Tile
	 */
	private int row;
	/**
	 * the column of this Tile
	 */
	private int col;
	/**
	 * the side that this Tile has a path coming IN
	 */
	private byte in;
	/**
	 * the side that this Tile has a path going OUT
	 */
	private byte out;
	/**
	 * the color of the path through this Tile
	 */
	private Color color;
	
	// directions, meant to be used for in and out values
	
	/**
	 * no path in/out
	 */
	public static final byte NOT_SET = -1;
	/**
	 * path from the top side in/out
	 */
	public static final byte UP = 0;
	/**
	 * path from the bottom side in/out
	 */
	public static final byte DOWN = 1;
	/**
	 * path from the left side in/out
	 */
	public static final byte LEFT = 2;
	/**
	 * path from the right side in/out
	 */
	public static final byte RIGHT = 3;
	/**
	 * all valid directions in an array
	 */
	public static final byte[] DIRECTIONS = {UP, DOWN, LEFT, RIGHT};
	
	/**
	 * the background color for all Tiles
	 */
	public static final Color BLANK = new Color(240, 218, 163);
	/**
	 * the side length of Tiles
	 */
	public static final int SIZE = 25;
	
	/**
	 * Initializes location of a Tile with no color or path
	 * @param row the row of this Tile
	 * @param col the column of this Tile
	 */
	public Tile(int row, int col) {
		this(row, col, null);
	}
	
	/**
	 * Initializes the location and color of a Tile with no path
	 * @param row the row of this Tile
	 * @param col the column of this Tile
	 * @param color the color of this Tile
	 */
	public Tile(int row, int col, Color color) {
		this.row = row;
		this.col = col;
		this.color = color;
		// no path
		in = NOT_SET;
		out = NOT_SET;
	}
	
	/**
	 * Gets this.row
	 * @return the row of this Tile
	 */
	public int getRow() {return row;}
	
	/**
	 * Gets this.col
	 * @return the column of this Tile
	 */
	public int getCol() {return col;}
	
	/**
	 * Gets this.in
	 * @return the side which a path enters this Tile
	 */
	public byte getIn() {return in;}
	
	/**
	 * Gets this.out
	 * @return the side which a path exits this Tile
	 */
	public byte getOut() {return out;}
	
	/**
	 * Gets this.color
	 * @return the color of the path through this Tile
	 */
	public Color getColor() {return color;}
	
	/**
	 * Sets the color of this Tile
	 * @param color the color of the path through this Tile
	 */
	public void setColor(Color color) {this.color = color;}
	
	/**
	 * Sets the in-side of this Tile
	 * @param side the side a path enters this Tile
	 * @throws InvalidPathException if a path already enters this Tile
	 */
	public void setIn(byte side) throws InvalidPathException {
		if (hasIn()) throw new InvalidPathException(side, this);
		if (side != UP && side != DOWN && side != LEFT && side != RIGHT)
			throw new IllegalArgumentException("Invalid side #");
		in = side;
	}
	
	/**
	 * Removes the in-side of this Tile
	 */
	public void removeIn() {in = NOT_SET;}
	
	/**
	 * Checks if there is an in-side for this Tile
	 * @return if a path enters this Tile
	 */
	public boolean hasIn() {return in != NOT_SET;}
	
	/**
	 * Sets the out-side of this Tile
	 * @param side the side by which a path exits this Tile
	 * @throws InvalidPathException
	 */
	public void setOut(byte side) throws InvalidPathException {
		if (hasOut()) throw new InvalidPathException(this, side);
		if (side != UP && side != DOWN && side != LEFT && side != RIGHT)
			throw new IllegalArgumentException("Invalid side #");
		out = side;
	}
	
	/**
	 * Removes the out-side of this Tile
	 */
	public void removeOut() {out = NOT_SET;}
	
	/**
	 * Checks if there is an out-side for this Tile
	 * @return if a path exits this Tile
	 */
	public boolean hasOut() {return out != NOT_SET;}
	
	/**
	 * Resets the Tile's state
	 */
	public void clear() {
		// remove path through, and nullify color
		removeIn();
		removeOut();
		setColor(null);
	}
	
	/**
	 * Connects two Tiles
	 * @param from the Tile to make a path from
	 * @param to the Tile to make a path to
	 * @throws InvalidPathException if it is impossible to connect the Tiles
	 */
	public static final void connect(Tile from, Tile to) throws InvalidPathException {
		// find connecting side, and set from's out to that
		from.setOut(Tile.sideConnecting(from, to));
		// set to's in to from's out opposite
		to.setIn(Tile.oppoSide(from.getOut()));
	}
	
	/**
	 * Reverses a side value
	 * @param side the side to find the opposite of
	 * @return the opposite side
	 */
	public static final byte oppoSide(byte side) {
		// up <-> down, right <-> left
		if (side == UP) return DOWN;
		else if (side == DOWN) return UP;
		else if (side == LEFT) return RIGHT;
		else if (side == RIGHT) return LEFT;
		// if none of that worked, this is an invalid side
		throw new IllegalArgumentException("Invalid side #");
	}
	
	/**
	 * Converts a side-value to its string
	 * @param side the side to convert
	 * @return the String representing that side-value
	 */
	public static final String sideToString(byte side) {
		// check for all static final direction variables
		if (side == UP) return "UP";
		else if (side == DOWN) return "DOWN";
		else if (side == LEFT) return "LEFT";
		else if (side == RIGHT) return "RIGHT";
		// if none of those work, this is in invalid side #
		throw new IllegalArgumentException("Invalid side #");
	}
	
	/**
	 * Find a side connecting two Tiles
	 * @param from the Tile the path will exit from
	 * @param to the Tile the path will enter
	 * @return the value of the side connecting the Tiles 
	 * @throws InvalidPathException if no side connects these Tiles
	 */
	public static final byte sideConnecting(Tile from, Tile to) throws InvalidPathException {
		// if the rows match
		if (from.getRow() == to.getRow()) {
			// determine left or right if off by a column
			if (from.getCol() + 1 == to.getCol()) return RIGHT;
			else if (from.getCol() - 1 == to.getCol()) return LEFT;
		}
		// or if the columns match
		else if (from.getCol() == to.getCol()) {
			// determine down or up if off by a row
			if (from.getRow() + 1 == to.getRow()) return DOWN;
			if (from.getRow() - 1 == to.getRow()) return UP;
		}
		// if none of that worked, no path exists
		throw new InvalidPathException(from, to);
	}
	
	public void draw(Graphics window) {
		// anchor coordinates (upper left corner)
		int anchorX = getCol() * SIZE;
		int anchorY = getRow() *  SIZE;
		// convenience fractions of SIZE 
		int third = SIZE / 3;
		int half = SIZE / 2;
		
		// draw background of tile
		window.setColor(BLANK);
		window.fillRect(anchorX, anchorY,  SIZE,  SIZE);
		// draw edges of tile
		window.setColor(Color.black);
		window.drawRect(anchorX, anchorY,  SIZE,  SIZE);
		// set color to the path-color through this Tile
		window.setColor(getColor());
		// check for path through any side, and draw rectangle if found
		if (in == UP || out == UP)
			window.fillRect(anchorX + third, anchorY, third, half);
		if (in == DOWN || out == DOWN)
			window.fillRect(anchorX + third, anchorY + half, third, half);
		if (in == LEFT || out == LEFT)
			window.fillRect(anchorX, anchorY + third, half, third);
		if (in == RIGHT || out == RIGHT)
			window.fillRect(anchorX + half, anchorY + third, half, third);
	}

	/**
	 * Determines if this Tile is in an win-state
	 * @return whether this Tile requires a state change to be finished
	 */
	public abstract boolean isComplete();
	
	public boolean equals(Object other) {
		// only check if a Tile or subclass of Tile
		if (other instanceof Tile) {
			// cast to Tile for convienence
			Tile t = (Tile) other;
			// all instance variables must match
			return row == t.getRow() && col == t.getCol() && in == t.getIn() 
					&& out == t.getOut() && color.equals(t.getColor());
		}
		
		else return false;
	}
	
	public String toString() {
		// start with location
		String ret = "Tile at (" + row + ", " + col + ")";
		// if in/out are present, add those
		if (hasIn()) ret += " in at " + Tile.sideToString(in);
		if (hasOut()) ret += " out at " + Tile.sideToString(out);
		
		return ret;
	}
}