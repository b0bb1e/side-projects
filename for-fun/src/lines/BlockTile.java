package lines;

// for overridden draw method
import java.awt.Color;
import java.awt.Graphics;

/**
 * A Tile which blocks all paths through it
 * @author faith
 */
public class BlockTile extends Tile {
	/**
	 * Initializes position
	 * @param row the row this Tile is in
	 * @param col the column this Tile is in
	 */
	public BlockTile(int row, int col) {
		super(row, col);
	}
	
	/**
	 * Always throws an InvalidPathException
	 */
	public void setIn(byte side) throws InvalidPathException {
		throw new InvalidPathException(side, this);
	}
	
	/**
	 * Always throws an InvalidPathException
	 */
	public void setOut(byte side) throws InvalidPathException {
		throw new InvalidPathException(side, this);
	}
	
	/**
	 * Color should never change
	 */
	public void setColor(Color color) {}
	
	public void draw(Graphics window) {
		// to signify how this Tile blocks all paths, draws as a black block
		window.setColor(Color.black);
		window.fillRect(getCol() * SIZE, getRow() * SIZE, SIZE, SIZE);
	}
	
	public boolean isComplete() {
		// always complete, since state never changes
		return true;
	}
	
	public boolean equals(Object other) {
		// check for self-ness and null-ness
		if (other == this) return true;
		else if (other == null) return false;
		// only run equals() if class matches
		else if (other.getClass().equals(BlockTile.class)) return super.equals(other);
		else return false;
	}
	
	public String toString() {
		// append an identifier for the kind of Tile this is
		return "Block" + super.toString();
	}
}