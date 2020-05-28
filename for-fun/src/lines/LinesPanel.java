package lines;

// for drawing
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JPanel;

// for accepting mouse clicks
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A JPanel which holds a Lines game
 * @author faith
 */
@SuppressWarnings("serial")
public class LinesPanel extends JPanel {
	/**
	 * the current Grid of Lines Tiles
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
	 * Sets up the Grid for Level #0 and the winMessage
	 */
	public LinesPanel() {
		// get rid of layout
		this.setLayout(null);
		
		// set up winMessage, it is invisible at first
		winMessage = new JLabel("You Win!");
		winMessage.setBounds(12 * Tile.SIZE, 200, 50, 2);
		winMessage.setVisible(false);
		add(winMessage);
		
		// set up the info-message with game instructions
		infoMessage = new JLabel("<html>Click on a tile to extend/start a path into it<br>"
				+ "If multiple paths could be extended, the one most recently modified is used<br>"
				+ "To delete a path, click on either end-tile (the ones with circles)<br>"
				+ "To back a path up to a certain point, click on a tile in the middle of it<br>"
				+ "Your goal is to connect all of the end-tiles and cover all tiles with your paths<br>"
				+ "Black tiles cannot have paths go through them; paths need not go through to win"
				+ "Click once done to progress to the next level</html>");
		infoMessage.setBounds(0, 0, 500, 250);
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
	 * Moves the game to the next level
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
		window.setColor(Tile.BLANK.darker());
		window.fillRect(0, 0, getWidth(), getHeight());
		
		// if there is a grid
		if (grid != null) {
			// if the current Grid is complete, move to the next Level
			if (grid.isComplete()) nextLevel();
			// otherwise just draw the Grid
			grid.draw(window);
		}
	}

	/**
	 * A MouseListener with mouseClicked overridden for Lines
	 * @author faith
	 */
	private class MyMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			if (active) {
				// calculate the Tile row and column of the mouse click
				int row = e.getY() / Tile.SIZE;
				int col = e.getX() / Tile.SIZE;
				
				// if it was valid
				if (grid.isValidTile(row, col)) {
					// grab the clicked Tile
					Tile tile = grid.getTile(row, col);
					
					// if this Tile is in the middle of a Path, remove the Path back to it
					if (tile.hasOut()) grid.removePathUpTo(tile);
					// if this Tile is the end of a Path, remove the whole Path
					else if (tile.hasIn() && tile.isComplete()) 
						grid.removePath(grid.getPath(tile));
					// attempt to connect current path to another path, move on if fail
					else if (!tile.hasOut() && grid.connectSomePath(tile)) {}
						
					// Tile is not on any path yet
					
					// if there is an active Path
					else if (grid.getActivePath() != null) {
						// try to extend it by this Tile
						try {grid.getActivePath().extend(tile);}
						// if that failed (since it couldn't be extended), try to put on any path
						catch (InvalidPathException pathE) {grid.tryToPutOnPath(tile);}
					}
					// if there is not active path, just try to put on any path
					else grid.tryToPutOnPath(tile);
				}
			}
			// or if not playing
			else {
				// move to next level, currently playing
				active = true;
				nextLevel();
			}
			
			// since something could've happened, repaint
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