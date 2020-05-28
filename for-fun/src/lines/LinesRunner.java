package lines;

// for the graphics
import java.awt.Component;
import javax.swing.JFrame;

/**
 * Runs a Lines game when instantiated
 * @author faith
 */
@SuppressWarnings("serial")
public class LinesRunner extends JFrame {
	/**
	 * Initializes a Lines game
	 */
	public LinesRunner() {
		// set up the Frame
		super("Lines");
		setSize(500, 500);
		
		// create a LinesPanel, focus, and add to Frame
		LinesPanel game = new LinesPanel();
		((Component) game).setFocusable(true);
		getContentPane().add(game);

		// make it visible and closable
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Instantiates a LinesRunner
	 * @param args not used
	 */
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		LinesRunner run = new LinesRunner();
	}
}