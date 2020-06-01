package sudoku;

// for running save() after close and before exit
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// for the frame itself
import javax.swing.JFrame;

/**
 * A runner for the Sudoku solver
 * @author faith
 */
@SuppressWarnings("serial")
public class SudokuRunner extends JFrame {
	/**
	 * Sets up a Sudoku
	 */
	public SudokuRunner() {
		// set up basic parameters
		super("Sudoku");
		setSize((Board.SIZE + 1) * Tile.SIZE + 100, (Board.SIZE + 2) * Tile.SIZE);
		
		// set up the panel, make it focusable, and add to frame
		SudokuPanel sudoku = new SudokuPanel();
		sudoku.setFocusable(true);
		getContentPane().add(sudoku);
		
		// make self visible
		setVisible(true);
		
		// set up close to save current board before exiting
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				sudoku.save();
				System.exit(0);
			}
		});
	}
	
	public static void main(String[] args) {
		// create (and therefore run) SUDOKU
		@SuppressWarnings("unused")
		SudokuRunner run = new SudokuRunner();
	}
}