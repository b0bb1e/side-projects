package sudoku;

// for graphics
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Insets;

// for listening to button clicks
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// for listening to typing
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// for listening to mouse clicks
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// for taking screenshots and saving them
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

/**
 * A fully functional Sudoku GUI which
 * <ul>
 * 	<li>can undo</li>
 * 	<li>can screenshot itself into new image files</li>
 * 	<li>can save & load game states</li>
 * 	<li>and, obviously, enter numbers on clicked tiles</li>
 * </ul>
 * @author faith
 */
@SuppressWarnings("serial")
public class SudokuPanel extends JPanel implements ActionListener {
	/**
	 *  the actual Sudoku board
	 */
	private Board board;
	
	/**
	 * the undo button
	 */
	private JButton undoButton;
	/**
	 * the screenshot button
	 */
	private JButton pictureButton;
	/**
	 * the load-save-game button
	 */
	private JButton loadButton;
	
	/**
	 * the number of saves made so far
	 */
	private int saveNum;
	
	/**
	 * Sets up the GUI
	 */
	public SudokuPanel() {
		// no layout (since we're drawing directly
		setLayout(null);
		
		// set up the Board
		board = new Board();
		
		// set up all buttons
		
		undoButton = new JButton("Undo");
		addButton(undoButton, 20);
		
		pictureButton = new JButton("<html>Take a<br>screenshot</html>");
		addButton(pictureButton, 130);
		
		loadButton = new JButton("<html>Load previous<br>save game</html>");
		addButton(loadButton, 240);
		
		// no saves made yet
		saveNum = 0;
		
		// set up mouse and key listeners
		addMouseListener(new MyMouseListener());
		addKeyListener(new MyKeyListener());
		// make self visible
		setVisible(true);
	}
	
	/**
	 * Basic button set-up
	 * @param button the button to set up
	 * @param y the y-coordinate to use
	 */
	private void addButton(JButton button, int y) {
		// position button
		button.setBounds(Board.SIZE * Tile.SIZE + 20, y, 100, 100);
		// decrease margins
		button.setMargin(new Insets(5, 5, 5,5));
		// make useable but not focusable
		button.addActionListener(this);
		button.setFocusable(false);
		// add to panel
		add(button);
	}
	
	public void paintComponent(Graphics window) {
		// just draw the board
		board.draw(window);
	}
	
	public void actionPerformed(ActionEvent e) {
		// if the undo button was clicked
		if (e.getSource().equals(undoButton)) {
			// undo and repaint
			board.undo();
			repaint();
		}
		// if the screenshot button was clicked
		else if (e.getSource().equals(pictureButton)) {
			try {
				// set up the image
				BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
				// paint a screenshot onto image
				super.paint(image.createGraphics());
				
				// declare file to save to
				File saveTo = new File("src/sudoku/saved-states/save" + (++saveNum) + ".jpeg");
				// if needed, create the file
				saveTo.createNewFile();
				// save the image
				ImageIO.write(image,"jpeg", saveTo);
			}
			// if something goes wrong, print out
			catch (Exception ex) {
				System.out.println("Could not take screenshot");
				ex.printStackTrace();
			}
		}
		else if (e.getSource().equals(loadButton)) {
			board.load();
			repaint();
		}
	}
	
	private class MyMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			// calculate coordinates of Tile and set active
			board.setActive(e.getY() / Tile.SIZE, e.getX() / Tile.SIZE);
			// repaint new board
			repaint();
		}

		public void mousePressed(MouseEvent e) {}

		public void mouseReleased(MouseEvent e) {}

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}
	}
	
	private class MyKeyListener implements KeyListener {
		public void keyTyped(KeyEvent e) {
			// if a number was typed
			if (Character.isDigit(e.getKeyChar())) {
				// tell board to set this number
				board.setNum(Character.getNumericValue(e.getKeyChar()));
				// repaint new board
				repaint();
			}
		}

		public void keyPressed(KeyEvent e) {}

		public void keyReleased(KeyEvent e) {}
	}
	
	/**
	 * Save by saving the underlying Board
	 */
	public void save() {board.save();}
}