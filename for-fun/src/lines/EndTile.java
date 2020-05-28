package lines;

// for overridden draw method
import java.awt.Color;
import java.awt.Graphics;

/**
 * A Tile which should be at the end of a Path
 * @author faith
 */
public class EndTile extends Tile {
	/**
	 * Initializes position and color
	 * @param row the row of this Tile
	 * @param col the column of this Tile
	 * @param color the color of this Tile
	 */
	public EndTile(int row, int col, Color color) {
		super(row, col, color);
	}
	
	/**
	 * Color should never change
	 */
	public void setColor(Color color) {}
	
	public void setIn(byte side) throws InvalidPathException {
		// can only have in or out, so throw exception if out exists
		if (hasOut()) throw new InvalidPathException(side, this);
		// otherwise just run method as per usual
		else super.setIn(side);
	}
	
	public void setOut(byte side) throws InvalidPathException {
		// can only have in or out, so throw exception if in exists
		if (hasIn()) throw new InvalidPathException(this, side);
		// otherwise just run method as per usual
		else super.setOut(side);
	}
	
	public void draw(Graphics window) {
		// draw per usual
		super.draw(window);
		// save a third-length for easy use
		int third = SIZE / 3;
		// draw a circle with diameter 1/3 side in the center
		window.setColor(getColor());
		window.fillOval(getCol() * SIZE + third, getRow() * SIZE + third, 
				third, third);
	}
	
	public boolean isComplete() {
		// requires either an out or an in
		return hasOut() || hasIn();
	}
	
	public boolean equals(Object other) {
		// check for self-ness and null-ness
		if (other == this) return true;
		else if (other == null) return false;
		// only run equals() if class matches
		else if (other.getClass().equals(EndTile.class)) return super.equals(other);
		else return false;
	}
	
	public String toString() {
		// append an identifier for the kind of Tile this is
		return "End" + super.toString();
	}
}