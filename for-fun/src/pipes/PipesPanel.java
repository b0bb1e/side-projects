package pipes;

// for the graphics
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JPanel;

// for mouse clicks
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A JPanel which plays Pipes
 * @author faith
 */
@SuppressWarnings("serial")
public class PipesPanel extends JPanel {
	/**
	 * the current Grid of Pipes Tiles
	 */
	private Grid grid;
	/**
	 * the Levels to play through
	 */
	private final Level[] levels = Level.readLevels();
	/**
	 * the index of the current Level in levels
	 */
	private int curLevel;
	/**
	 * the message that pops up at the end to indicate a win
	 */
	private JLabel winMessage;
	/**
	 * the message which provides important information about the current game
	 */
	private JLabel infoMessage;
	/**
	 * whether the game is actively being played
	 */
	private boolean active;
	
	/**
	 * Set up a Pipes game
	 */
	public PipesPanel() {
		// get rid of layout
		this.setLayout(null);
		
		// set up winMessage, it is invisible at first
		winMessage = new JLabel("You Win!");
		winMessage.setBounds(12 * Tile.SIZE, 200, 50, 2);
		winMessage.setVisible(false);
		add(winMessage);
		
		// set up the info-message with game instructions
		infoMessage = new JLabel("<html>Click on a pipe to rotate it<br>"
				+ "End-pipes (only one entrance) cannot be rotated<br>"
				+ "Connect all pipes, matching end-pipe colors<br>"
				+ "Once a level is complete, click to move on</html>");
		infoMessage.setBounds(50, 50, 300, 250);
		infoMessage.setVisible(true);
		add(infoMessage);
		
		// start listening to mouse clicks, and make it visible
		addMouseListener(new MyMouseListener());
		setVisible(true);
		
		// don't start the game yet
		active = false;
		grid = null;
		// curLevel is -1 (so nextLevel will use level #0)
		curLevel = -1;
	}
	
	/**
	 * Move to the next level
	 */
	private void nextLevel() {
		// if this is the last level, show the win-message
		if (curLevel == levels.length - 1) winMessage.setVisible(true);
		// otherwise, move to next level
		else {
			// increment level
			++curLevel;
			// keep re-generating grid until it isn't already done
			do {grid = new Grid(levels[curLevel]);}
			while (grid.isComplete());
			// reset info-message to a level indicator
			infoMessage.setBounds((grid.getCols() + 1) * Tile.SIZE, 100, 50, 25);
			infoMessage.setText("Level " + (curLevel + 1));
		}
	}
	
	public void paintComponent(Graphics window) {
		// draw a slightly darker background
		window.setColor(Tile.BACKGROUND.darker());
		window.fillRect(0, 0, getWidth(), getHeight());
		
		// if there is a Grid to draw
		if (grid != null) {
			// if the current Grid is complete, move to the next Level
			if (grid.isComplete()) active = false;
			grid.draw(window);
		}
	}
	
	private class MyMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			// if currently playing, rotate the clicked Tile
			if (active) grid.rotateTile(e.getY() / Tile.SIZE, e.getX() / Tile.SIZE);
			// or if not playing
			else {
				// move to next level, currently playing
				active = true;
				nextLevel();
			}
			// something happened, repaint
			repaint();
		}

		/**
		 * Ignore mousePressed events
		 */
		public void mousePressed(MouseEvent e) {}
	
		/**
		 * Ignore mouseReleased events
		 */
		public void mouseReleased(MouseEvent e) {}
	
		/**
		 * Ignore mouseEntered events
		 */
		public void mouseEntered(MouseEvent e) {}
	
		/**
		 * Ignore mouseExited events
		 */
		public void mouseExited(MouseEvent e) {}
	}
}