package pipes;

/**
 * A Tile in the middle of a pipe network
 * @author faith
 */
public class MiddleTile extends Tile {
	/**
	 * Initialize a MiddleTile with a random orientation
	 * @param row the row of the MiddleTile
	 * @param col the column of the MiddleTile
	 * @param sides the number of consecutive sides
	 */
	private MiddleTile(int row, int col, int sides) {
		super(row, col, getConsecSides(sides));
	}
	
	/**
	 * Initialize a MiddleTile with opposite sides
	 * @param row the row of the MiddleTile
	 * @param col the column of the MiddleTile
	 */
	private MiddleTile(int row, int col) {
		super(row, col, getOppSides());
	}
	
	/**
	 * Get a Tile with a bend/corner
	 * @param row the row of the MiddleTile
	 * @param col the column of the MiddleTile
	 * @return the completed Tile
	 */
	public static MiddleTile getBendTile(int row, int col) {
		return new MiddleTile(row, col, 2);
	}
	
	/**
	 * Get a Tile with a straight pipe
	 * @param row the row of the MiddleTile
	 * @param col the column of the MiddleTile
	 * @return the completed Tile
	 */
	public static MiddleTile getStraightTile(int row, int col) {
		return new MiddleTile(row, col);
	}
	
	/**
	 * Get a Tile with a fork
	 * @param row the row of the MiddleTile
	 * @param col the column of the MiddleTile
	 * @return the completed Tile
	 */
	public static MiddleTile getForkTile(int row, int col) {
		return new MiddleTile(row, col, 3);
	}
	
	/**
	 * Get a Tile with all pipes available
	 * @param row the row of the MiddleTile
	 * @param col the column of the MiddleTile
	 * @return the completed Tile
	 */
	public static MiddleTile getAllTile(int row, int col) {
		return new MiddleTile(row, col, 4);
	}
	
	public void rotate() {
		// assume all sides will not be there
		boolean[] newSides = new boolean[4];
		// loop over all sides
		for (int side : directions)
			// set this new side to true if a side counterclockwise is
			newSides[side] = hasSide((side + 1) % 4);
		
		setSides(newSides);
	}
	
	/**
	 * Get an array of random consecutive sides
	 * @param num the number of sides
	 * @return the completed sides array
	 */
	public static boolean[] getConsecSides(int num) {
		// assume all sides have no pipe
		boolean[] sides = new boolean[4];
		// start on a random side
		int start = randomSide();
		// loop for each side to add, set the next one to true
		for (int i = start; i < start + num; ++i)
			sides[i % 4] = true;
		
		return sides;
	}
	
	/**
	 * Get an array of two opposite sides
	 * @return the completed sides array
	 */
	public static boolean[] getOppSides() {
		// assume all sides have no pipes
		boolean[] sides = new boolean[4];
		// get a random side
		int start = randomSide();
		// set random side and opposite random side to pipes
		sides[start] = true;
		sides[(start + 2) % 4] = true;
		
		return sides;
	}
	
	public String toString() {
		// append a class identifier
		return "Middle" + super.toString();
	}
}