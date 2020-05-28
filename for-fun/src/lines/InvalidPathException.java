package lines;

/**
 * An exception to be thrown if a path attempt is invalid
 * @author faith
 */
@SuppressWarnings("serial")
public class InvalidPathException extends RuntimeException {
	/**
	 * Constructs an exception when a path OUT of a Tile is invalid
	 * @param start the Tile a path cannot go out of
	 * @param side the side that the path attempt went through
	 */
	public InvalidPathException(Tile start, byte side) {
		super("Attempt to extend path from (" + start + ") " + Tile.sideToString(side) + " failed");
	}
	
	/**
	 * Constructs an exception when a path IN a Tile is invalid
	 * @param side the side that the path attempt cannot go in
	 * @param end the Tile a path cannot go in to
	 */
	public InvalidPathException(byte side, Tile end) {
		super("Attempt to extend path to (" + end + ") " + Tile.sideToString(side) + " failed");
	}
	
	/**
	 * Constructs an exception when a path between to Tiles is invalid
	 * @param from the Tile the failed path tried to go from
	 * @param to the Tile the failed path tried to go to
	 */
	public InvalidPathException(Tile from, Tile to) {
		super("Attempt to connect " + from + " and " + to + " failed");
	}

	/**
	 * Copies an exception
	 * @param e the InvalidPathException to copy
	 */
	public InvalidPathException(InvalidPathException e) {
		super(e.getMessage());
	}
}