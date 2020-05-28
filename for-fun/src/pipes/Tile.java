package pipes;

// for drawing
import java.awt.Color;
import java.awt.Graphics;

/**
 * A Tile in a Pipes game, which knows the sides it has open pipes and can rotate/draw itself
 * @author faith
 */
public abstract class Tile {
	/**
	 * the row of the Tile
	 */
	private final int row;
	/**
	 * the column of this Tile
	 */
	private final int col;
	/**
	 * whether each side of the Tile has an open pipe
	 */
	private boolean[] sides;
	/**
	 * the color of the water in this Tile's pipes
	 */
	private Color color;
	
	/**
	 * the background color of the Tiles
	 */
	public static final Color BACKGROUND = new Color(240, 218, 163);
	/**
	 * the pipe color of the Tiles
	 */
	public static final Color PIPE = new Color(135, 119, 80);
	
	/**
	 * the length of the Tiles' sides, in coordinates
	 */
	public static final int SIZE = 28;
	
	/**
	 * the index in sides corresponding to the ^ side
	 */
	public static final int UP = 0;
	/**
	 * the index in sides corresponding to the > side
	 */
	public static final int RIGHT = 1;
	/**
	 * the index in sides corresponding to the \/ side
	 */
	public static final int DOWN = 2;
	/**
	 * the index in sides corresponding to the < side
	 */
	public static final int LEFT = 3;
	/**
	 * all available side directions for easy iteration
	 */
	public static final int[] directions = {UP, RIGHT, DOWN, LEFT};
	
	/**
	 * Initializes a Tile with location and sides set-up
	 * @param row the row of this Tile
	 * @param col the column of this Tile
	 * @param sides whether each side of the Tile has an open pipe
	 */
	public Tile(int row, int col, boolean[] sides) {
		this(row, col, sides, null);
	}
	
	/**
	 * Initializes a Tile with location, sides set-up, and color
	 * @param row the row of this Tile
	 * @param col the column of this Tile
	 * @param sides whether each side of the Tile has an open pipe
	 * @param color the color of this Tile's water through the pipes
	 */
	public Tile(int row, int col, boolean[] sides, Color color) {
		// make sure the sides are the right length
		if (sides.length != directions.length) 
			throw new IllegalArgumentException("Wrong number of sides");
		this.row = row;
		this.col = col;
		this.sides = sides;
		this.color = color;
	}
	
	/**
	 * Gets this.row
	 * @return the row of the Tile
	 */
	public int getRow() {return row;}
	
	/**
	 * Gets this.col
	 * @return the column of the Tile
	 */
	public int getCol() {return col;}
	
	/**
	 * Gets this.color
	 * @return the color of the water in this Tile's pipes
	 */
	public Color getColor() {return color;}
	
	/**
	 * Sets this.color
	 * @param color the new color of the water flowing through this Tile's pipe
	 */
	public void setColor(Color color) {this.color = color;}

	/**
	 * Sets this.sides
	 * @param sides whether each side of the Tile has an open pipe
	 */
	protected void setSides(boolean[] sides) {
		// make sure there is the right number of sides
		if (sides.length != directions.length)
			throw new IllegalArgumentException("Wrong number of sides");
		this.sides = sides;
	}
	
	/**
	 * Checks this.sides[side]
	 * @param side the int value of the side to check
	 * @return whether the Tile has a pipe exiting a side 
	 */
	public boolean hasSide(int side) {return sides[side];}

	/**
	 * Gets a random side index
	 * @return a random integer in [0, 3]
	 */
	public static int randomSide() {
		return directions[(int) (Math.random() * directions.length)];
	}
	
	/**
	 * Rotates this Tile
	 */
	public abstract void rotate();
	
	/**
	 * Checks if another Tile is connected to this one
	 * @param other the Tile to check for connection
	 * @return whether the pipes of these Tiles are connected
	 */
	public boolean isConnected(Tile other) {
		// if the columns match
		if (getCol() == other.getCol()) {
			// if this Tile is one above, check for down & up pipes to connect
			if (getRow() + 1 == other.getRow()) 
				return hasSide(DOWN) && other.hasSide(UP);
			// similar for one below
			if (getRow() - 1 == other.getRow())
				return hasSide(UP) && other.hasSide(DOWN);
		}
		
		// if the columns match
		if (getRow() == other.getRow()) {
			// similar for one to the left
			if (getCol() + 1 == other.getCol())
				return hasSide(RIGHT) && other.hasSide(LEFT);
			// similar for one to the right
			if (getCol() - 1 == other.getCol())
				return hasSide(LEFT) && other.hasSide(RIGHT);
		}
		
		// if neither rows or columns match, these Tiles cannot connect
		return false;
	}

	/**
	 * Draws this Tile
	 * @param window the window to draw on
	 */
	public void draw(Graphics window) {
		// set up anchor values
		
		// the x-coordinate of the upper left corner
		int x = col * SIZE;
		// the y-coordinate of the upper left corner
		int y = row * SIZE;
		// the width of pipes
		int pipeW = SIZE / 3;
		// the length of pipes
		int pipeL = SIZE / 2;
		// the width of water flow
		int waterW = SIZE / 7;
		// the offset from the corner for water flow
		int waterOff = SIZE * 3 / 7;
		
		// draw the Tile's background
		window.setColor(BACKGROUND);
		window.fillRect(x, y, SIZE, SIZE);
		// outline the Tile
		window.setColor(Color.BLACK);
		window.drawRect(x, y, SIZE, SIZE);
		
		// if a pipe exits the upper side
		if (hasSide(UP)) {
			// draw the pipe
			window.setColor(PIPE);
			window.fillRect(x + pipeW, y, pipeW, pipeL);
			// if water is flowing
			if (color != null) {
				// draw the water
				window.setColor(color);
				window.fillRect(x + waterOff, y, waterW, pipeL);
			}
		}
		
		// similar for exiting the lower side
		if (hasSide(DOWN)) {
			window.setColor(PIPE);
			window.fillRect(x + pipeW, y + pipeL, pipeW, pipeL);
			if (color != null) {
				window.setColor(color);
				window.fillRect(x + waterOff, y + pipeL, waterW, pipeL);
			}
		}
		
		// similar for exiting the left side
		if (hasSide(LEFT)) {
			window.setColor(PIPE);
			window.fillRect(x, y + pipeW, pipeL, pipeW);
			if (color != null) {
				window.setColor(color);
				window.fillRect(x, y + waterOff, pipeL, waterW);
		
			}
		}
		
		// similar for exiting the right side
		if (hasSide(RIGHT)) {
			window.setColor(PIPE);
			window.fillRect(x + pipeL, y + pipeW, pipeL, pipeW);
			if (color != null) {
				window.setColor(color);
				window.fillRect(x + pipeL, y + waterOff, pipeL, waterW);
		
			}
		}
	}
	
	/**
	 * Converts a string to a side index
	 * @param str the string to convert
	 * @return the int value of the side
	 */
	public static int stringToSide(String str) {
		// lower-case the string
		str = str.toLowerCase();
		// check each side-string and return the side-value
		if (str.equals("up")) return UP;
		if (str.equals("down")) return DOWN;
		if (str.equals("left")) return LEFT;
		if (str.equals("right")) return RIGHT;
		// if none of that worked, this side is invalid
		throw new IllegalArgumentException("Invalid side");
	}
	
	public String toString() {
		String ret = "Tile at (" + row + ", " + col + ") pipes out";
		for (int dir : directions) if (hasSide(dir)) ret += " " + dir;
		return ret;
	}
}