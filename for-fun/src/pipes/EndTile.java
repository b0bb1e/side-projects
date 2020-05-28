package pipes;

// for drawing
import java.awt.Color;
import java.awt.Graphics;

/**
 * A Tile at the end of a pipe network
 * @author faith
 */
public class EndTile extends Tile {
	/**
	 * the side that has an exiting pipe
	 */
	private int side;
	
	/**
	 * Initializes location, exiting side, and color
	 * @param row the row of this EndTile
	 * @param col the column of this EndTile
	 * @param side the side out of which the pipe exits
	 * @param color the required color of this EndTile's pipe
	 */
	public EndTile(int row, int col, int side, Color color) {
		super(row, col, sidesArr(side), color);
		this.side = side;
	}

	/**
	 * Sets up the sides array, with one exiting side
	 * @param side the side with an exiting pipe
	 * @return the completed side array
	 */
	private static boolean[] sidesArr(int side) {
		// initialize all sides as false
		boolean[] sides = new boolean[4];
		// set the exiting side to true
		sides[side] = true;
		
		return sides;
	}
	
	/**
	 * Cannot rotate
	 */
	public void rotate() {}
	
	/**
	 * Cannot change color
	 */
	public void setColor(Color color) {}
	
	/**
	 * Gets the side with an exiting pipe
	 * @return the int value of the side
	 */
	public int getSide() {return side;}
	
	public void draw(Graphics window) {
		// draw default
		super.draw(window);
		// draw a circle to indicate end-ness in the middle
		window.setColor(getColor());
		window.fillOval(getCol() * SIZE + SIZE / 3, getRow() * SIZE + SIZE / 3, SIZE / 3, SIZE / 3);
	}
	
	public String toString() {
		// append a class identifier
		return "End" + super.toString();
	}
}