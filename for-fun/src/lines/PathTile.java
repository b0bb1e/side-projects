package lines;

/**
 * A Tile that can be in the middle of a Path
 * @author faith
 */
public class PathTile extends Tile {
	/**
	 * Initializes location
	 * @param row the row of the Tile
	 * @param col the column of the Tile
	 */
	public PathTile(int row, int col) {
		super(row, col);
	}
	
	public boolean isComplete() {
		// both an in and out (path through) are required
		return hasOut() && hasIn();
	}
	
	public boolean equals(Object other) {
		// only check for equality if class is equal
		if (other.getClass().equals(PathTile.class)) return super.equals(other);
		// otherwise assume falseness
		else return false;
	}
	
	public String toString() {
		// append a class identifier
		return "Path" + super.toString();
	}
}