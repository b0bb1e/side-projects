package sudoku;

// for drawing
import java.awt.Color;
import java.awt.Graphics;

/**
 * A Sudoku tile which 
 * <ul>
 * 	<li>knows its number (if it has one)</li> 
 * 	<li>or the numbers it could be (if it doesn't)</li>
 * 	<li>can be highlighted</li>
 * 	<li>knows its position</li>
 * 	<li>can reset itself</li>
 * 	<li>and can draw itself</li>
 * @author faith
 */
public class Tile {
	/**
	 * the single number of this Tile (may be NO_NUM)
	 */
	private int num;
	/**
	 * an array indicating if this Tile could be i if canBe[i - 1] == true
	 */
	private boolean[] canBe;
	
	/**
	 * the row of this Tile
	 */
	private int row;
	/**
	 * the column of this Tile
	 */
	private int col;
	
	/**
	 * whether this Tile is highlighted
	 */
	private boolean highlight;
	
	/**
	 * a value indicating that this Tile has no single number
	 */
	public static final int NO_NUM = -1;
	
	/**
	 * the background color of a regular Tile
	 */
	public static final Color NORMAL = new Color(214, 210, 159);
	/**
	 * the background color of a highlighted Tile
	 */
	public static final Color HIGHLIGHT = new Color(140, 236, 237);
	
	/**
	 * the side length, in pixels, of Tiles
	 */
	public static final int SIZE = 51;
	/**
	 * the thickness, in pixels, of between-group thick lines
	 */
	public static final int THICK = 4;
	
	/**
	 * Initializes a default Tile
	 * @param row the row of this Tile
	 * @param col the column of this Tile
	 */
	public Tile(int row, int col) {
		// move num and canBe to default state
		reset();
		// tiles start un-highlighted
		highlight = false;
		// save position
		this.row = row;
		this.col = col;
	}
	
	/**
	 * @return if this Tile has a single number
	 */
	public boolean hasNum() {return num != NO_NUM;}
	
	/**
	 * @return this Tile's single number
	 */
	public int getNum() {return num;}

	/**
	 * @param num a number this Tile could possibliy have
	 * @return whether this Tile could have num as its single number
	 */
	public boolean couldBe(int num) {return canBe[num - 1];}
	
	/**
	 * @param num a single number for this Tile
	 */
	public void setNum(int num) {
		// only set if possibly
		if (couldBe(num)) {
			// otherwise set the single number
			this.num = num;
			// only canBe this number
			canBe = new boolean[Board.SIZE];
			canBe[num - 1] = true;
		}
	}
	
	/**
	 * @return this Tile's row
	 */
	public int getRow() {return row;}
	
	/**
	 * @return this Tile's column
	 */
	public int getCol() {return col;}
	
	/**
	 * @return an array of possible values
	 */
	public int[] getAllPos() {
		// count the number of possibles
		int count = 0;
		for (boolean pos : canBe)
			if (pos) ++count;
		// copy int-values into new array
		int[] allPos = new int[count];
		int addIndex = 0;
		for (int i = 0; i < canBe.length; ++i)
			if (canBe[i]) allPos[addIndex++] = (i + 1);
		return allPos;
	}
	
	/**
	 * @param whether this Tile should be highlighted
	 */
	public void setHighlight(boolean highlight) {this.highlight = highlight;}
	
	/**
	 * Sets the Tile to default state - could be any number
	 */
	public void reset() {
		// no single number
		num = NO_NUM;
		// set up all-true boolean array, since canBe any number
		canBe = new boolean[Board.SIZE];
		for (int i = 0; i < canBe.length; ++i) canBe[i] = true;
	}
	
	/*
	 * @param num the number to set as possible
	 */
	public void addPos(int num) {canBe[num - 1] = true;}
	
	/**
	 * @param num the number to set as impossible
	 */
	public boolean removePos(int num) {
		if (canBe[num - 1]) {
			canBe[num - 1] = false;
			return true;
		}
		return false;
	}
	
	/**
	 * Draws the Tile in its current state
	 * @param window the window to draw on
	 */
	public void draw(Graphics window) {
		// set background color depending on highlight
		if (highlight) window.setColor(HIGHLIGHT);
		else window.setColor(NORMAL);
		window.fillRect(col * SIZE, row * SIZE, SIZE, SIZE);
		
		// use black to outline the Tile
		window.setColor(Color.BLACK);
		window.drawRect(col * SIZE, row * SIZE, SIZE, SIZE);
		
		// thicken the proper side if this Tile is on the edge of a block
		
		// top edge
		if (row % Board.ROWS == 0)
			window.fillRect(col * SIZE, row * SIZE - THICK / 2, SIZE, THICK);
		// bottom edge
		else if (row % Board.ROWS == Board.ROWS - 1)
			window.fillRect(col * SIZE, (row + 1) * SIZE + THICK / 2, SIZE, THICK);
		
		// left side
		if (col % Board.COLS == 0)
			window.fillRect(col * SIZE - THICK / 2, row * SIZE, THICK, SIZE);
		// right side
		else if (col % Board.COLS == Board.COLS - 1)
			window.fillRect((col + 1) * SIZE - THICK, row * SIZE, THICK, SIZE);
		
		// if this Tile has a single number
		if (hasNum()) {
			// draw the number in big font
			window.setFont(window.getFont().deriveFont(60.0f));
			window.drawString(num + "", col * SIZE, (row + 1) * SIZE);
		}
		// or if it has only possibilities
		else {
			// use smaller font
			window.setFont(window.getFont().deriveFont(15.0f));
			// loop over all possibilities, and if canBe this number
			for (int i = 0; i < Board.SIZE; ++i) if (canBe[i])
				// draw the number in the proper spot (I know, lots of math)
				window.drawString((i + 1) + "", col * SIZE + (i % Board.COLS) * (SIZE / Board.COLS), 
					row * SIZE + (i / Board.COLS + 1) * (SIZE / Board.ROWS));
		}
	}
}