import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main Frame for a Simple Free cell Game Sets up the menus and places a
 * CardPanel in the Frame. This class constructs a JFrame that contains the menu and its items.
 * 
 * @author Ridout and Veronica Huang
 * @version November 2014
 */
public class FreeCellMain extends JFrame implements ActionListener
{
	private CardPanel cardArea;
	private JMenuItem newMenuItem, statisticsOption, quitMenuItem;
	JCheckBoxMenuItem autoCompleteOption, animationOption;
	private JMenuItem undoOption, hint, aboutMenuItem;

	/**
	 * Creates a FreeCellMain from object
	 */
	public FreeCellMain()
	{
		super("Freecell");
		setResizable(false);

		// Add in an Icon - Ace of Spades
		setIconImage(new ImageIcon("images\\ace.png").getImage());

		addWindowListener(new CloseWindow());

		// Add in a Simple Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic('G');
		newMenuItem = new JMenuItem("New Game");
		newMenuItem.addActionListener(this);

		statisticsOption = new JMenuItem("Statistics");
		statisticsOption.addActionListener(this);

		quitMenuItem = new JMenuItem("Exit");
		quitMenuItem.addActionListener(this);

		undoOption = new JMenuItem("Undo Move");
		undoOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				InputEvent.CTRL_MASK));
		undoOption.addActionListener(this);
		undoOption.setEnabled(false);

		hint = new JMenuItem("Hint");
		hint.addActionListener(this);
		hint.setEnabled(false);

		gameMenu.add(newMenuItem);
		gameMenu.add(statisticsOption);
		gameMenu.add(undoOption);
		gameMenu.add(hint);
		gameMenu.addSeparator();
		gameMenu.add(quitMenuItem);
		menuBar.add(gameMenu);

		autoCompleteOption = new JCheckBoxMenuItem("Auto Complete", true);
		autoCompleteOption.addActionListener(this);

		animationOption = new JCheckBoxMenuItem("Animate", true);
		animationOption.addActionListener(this);

		JMenu settingMenu = new JMenu("Settings");
		settingMenu.setMnemonic('S');
		settingMenu.add(autoCompleteOption);
		settingMenu.add(animationOption);
		menuBar.add(settingMenu);

		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		aboutMenuItem = new JMenuItem("About...");
		aboutMenuItem.addActionListener(this);
		helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);

		// Set up the layout and add in a CardPanel for the card area
		setLayout(new BorderLayout());
		cardArea = new CardPanel(this);
		add(cardArea, BorderLayout.CENTER);

		// Centre the frame in the middle (almost) of the screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setVisible(true);
		setLocation((screen.width - CardPanel.WIDTH) / 2 - this.getWidth(),
				(screen.height - CardPanel.HEIGHT) / 2 - this.getHeight());
	}

	/**
	 * Method that deals with the menu options
	 * 
	 * @param event the event that triggered this method
	 */
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == newMenuItem)
		{
			cardArea.endGame();
			cardArea.newGame();
		}
		else if (event.getSource() == statisticsOption)
		{
			JOptionPane.showMessageDialog(cardArea, cardArea.getStatistic(),
					"Statistics", JOptionPane.INFORMATION_MESSAGE);
		}
		else if (event.getSource() == quitMenuItem)
		{
			cardArea.endGame();
			System.exit(0);
		}
		else if (event.getSource() == undoOption)
		{
			cardArea.undo();
			if (!cardArea.canUndo())
				setUndoOption(false);
		}
		else if (event.getSource() == hint)
		{
			cardArea.showHint();
			if (!cardArea.canShowHint())
				setHint(false);
		}
		else if (event.getSource() == autoCompleteOption)
		{
			CardPanel.changeAutoComplete();
		}
		else if (event.getSource() == animationOption)
		{
			CardPanel.changeAnimation();
		}
		else if (event.getSource() == aboutMenuItem)
		{
			JOptionPane.showMessageDialog(cardArea,
					"Freecell by Ridout\nand Veronica Huang\n\u00a9 2014",
					"About Freecell", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Sets the Undo object in the Menu
	 * 
	 * @param canUndo if you can undo or not
	 */
	public void setUndoOption(boolean canUndo)
	{
		this.undoOption.setEnabled(canUndo);
	}

	/**
	 * Sets the Hint in the Menu
	 * 
	 * @param hint if you can give a hint or not
	 */
	public void setHint(boolean hint)
	{
		this.hint.setEnabled(hint);
	}

	/**
	 * An inner class for a Window Closing event
	 */
	public class CloseWindow extends WindowAdapter
	{
		/**
		 * End the current game and close the window
		 * 
		 * @param e the Window Closing Event
		 */
		public void windowClosing(WindowEvent e)
		{
			cardArea.endGame();
			System.exit(0);
		}
	}

	public static void main(String[] args)
	{
		FreeCellMain frame = new FreeCellMain();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

	}

}
