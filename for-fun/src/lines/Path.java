package lines;

// for arrays that can change size
import java.util.ArrayList;
// for passing colors between Tiles
import java.awt.Color;

/**
 * A path on a Lines grid, with Tiles an a color
 * @author faith
 *
 */
public class Path {
	/**
	 * the Grid that this Path is on (so that it can trigger its self-removal)
	 */
	private Grid grid;
	/**
	 * the Tiles that are on this Path
	 */
	private ArrayList<Tile> tiles;
	/**
	 * the color of this Path
	 */
	private Color color;
	
	/**
	 * Attempts to create a path between a starting Tile and another
	 * @param start the Tile to start the path with
	 * @param next the Tile to use as the 2nd on the path
	 * @param grid the Grid that this Path is on
	 * @throws InvalidPathException if next cannot be on a path with start
	 */
	public Path(EndTile start, Tile next, Grid grid) throws InvalidPathException {
		// initialize the list of Tiles with start
		tiles = new ArrayList<Tile>();
		tiles.add(start);
		this.grid = grid;
		// paths are colored by their starting tiles
		color = start.getColor();
		// attempt to extend start by next
		extend(next);
	}

	/**
	 * Extends the Path by adding a Tile to the end
	 * @param next the Tile to extend by
	 * @throws InvalidPathException if this extension is not possible
	 */
	public void extend(Tile next) throws InvalidPathException {
		// only attempt an extension with a blank Tile (no path) or same color Tile (end dot)
		// also, do not extend by a Tile already on the path
		if ((next.getColor() == null || next.getColor().equals(color)) && !tiles.contains(next)) {
			// attempt to connect the last Tile to this new Tile
			Tile.connect(lastTile(), next);
			// if no exception thrown, set next Tile's color and add it
			next.setColor(color);
			tiles.add(next);
		}
		// if above conditions not fulfilled, this is an automatic invalid path
		else throw new InvalidPathException(lastTile(), next);
	}
	
	/**
	 * Connect this Path to another Path
	 * @param connect a Path to connect to
	 * @throws InvalidPathException if the connection fails
	 */
	public void connect(Path connect) throws InvalidPathException {
		// only connect same-color Paths
		if (connect.getColor().equals(color)) {
			Tile last = connect.lastTile();
			try {
				// make sure that this lastTiles can connect safely
				connect.backUp();
				extend(last);
			}
			catch (InvalidPathException e) {
				// if they can't, reset the connect-path and throw exception
				connect.extend(last);
				throw new InvalidPathException(e);
			}
			
			// save Tiles of connect
			ArrayList<Tile> connectTiles = connect.getTiles();
			// remove connect
			grid.removePath(connect);
			// add each Tile to current path backwards
			for (int i = connectTiles.size() - 1; i >= 0; --i)
				extend(connectTiles.get(i));
				
		}
		// attempting to connect different-color Paths is invalid
		else throw new InvalidPathException(lastTile(), connect.lastTile());
	}
	
	/**
	 * Backs a Path up 1 Tile
	 */
	public void backUp() {
		// if there will be at least 2 Tiles left
		if (tiles.size() > 2) {
			// remove and clear last Tile
			tiles.remove(tiles.size() - 1).clear();
			// the new last Tile has no path out
			lastTile().removeOut();
		}
		// or if too few Tiles left, just remove this Path
		else grid.removePath(this);
	}
	
	/**
	 * Allows Paths to access each other's Tiles
	 * @return a copy of this.tiles
	 */
	private ArrayList<Tile> getTiles() {return new ArrayList<Tile>(tiles);}
	
	/**
	 * Gets this.color
	 * @return the color of this Path's tiles
	 */
	public Color getColor() {return color;}
	
	/**
	 * Gets the last Tile added to the Path
	 * @return the last element in tiles
	 */
	public Tile lastTile() {return tiles.get(tiles.size() - 1);}
	
	/**
	 * Checks if a certain Tile is contained in this Path
	 * @param tile the Tile to check
	 * @return if the Tile is contained in this.tiles
	 */
	public boolean containsTile(Tile tile) {
		// loop over all Tiles, if a match is found return that
		for (Tile mine : tiles) if (mine.equals(tile)) return true;
		// if no match was found return that
		return false;
	}
	
	/**
	 * Clears a Path's graphics by clearing each Tile
	 */
	public void clear() {for (Tile tile : tiles) tile.clear();}
	
	/**
	 * Checks if a Path is complete (requires no further extensions)
	 * @return if the last Tile requires an extension
	 */
	public boolean isComplete() {return lastTile().isComplete();}
	
	public String toString() {
		// initialize return variable
		String ret = "";
		// add each Tile with an arrow after
		for (Tile tile : tiles) ret += tile + " -> ";
		// if complete, remove last arrow
		if (isComplete()) return ret.substring(0, ret.length() - 4);
		// otherwise just return
		else return ret;
	}
}