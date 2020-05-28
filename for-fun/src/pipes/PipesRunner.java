package pipes;

// for the graphics
import java.awt.Component;
import javax.swing.JFrame;

/**
 * Runs a Pipes game when instantiated
 * @author faith
 */
@SuppressWarnings("serial")
public class PipesRunner extends JFrame {
	/**
	 * Initializes a Pipes game
	 */
	public PipesRunner() {
		// set up the Frame
		super("Pipes");
		setSize(500, 500);
		
		// create a PipesPanel, focus, and add to Frame
		PipesPanel game = new PipesPanel();
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
		PipesRunner run = new PipesRunner();
	}
}