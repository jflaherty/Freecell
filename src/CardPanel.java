import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Looks after most of the FreeCell Game. The moves made in the game can be undo
 * with the methods. This class handles all the mouse movement events. This
 * class also shows the hints for possible moves. Cards moves are also animated
 * in this class.
 * 
 * @author Ridout and Veronica Huang
 * @version November 2014
 */
public class CardPanel extends JPanel implements MouseListener,
		MouseMotionListener
{
	private static final long serialVersionUID = 1L;
	
	// Constants for the table layout
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	private final Color TABLE_COLOUR = new Color(140, 225, 140);
	private final int ANIMATION_FRAMES = 6;

	// Load up the fireworks image
	public static final Image fireWork = new ImageIcon("firework.png")
			.getImage();

	// Constants for layout of card area
	private final int NO_OF_CASCADES = 8;
	private final int NO_OF_FREECELLS = 4;
	private final int NO_OF_FOUNDATIONS = 4;
	private final int CASCADE_X = 30;
	private final int CASCADE_Y = 150;
	private final int CASCADE_SPACING = 95;
	private final int FREECELL_X = 30;
	private final int FREECELL_Y = 30;
	private final int TOP_SPACING = 90;
	private final int FOUNDATION_X = 425;
	private final int FOUNDATION_Y = 30;

	// Variables for the Freecell Game
	private FreeCellMain parentFrame;
	private LinkedList<Move> moves;

	private GDeck myDeck;
	private ArrayList<GHand> allHands;
	private Movable selectedItem;
	private GHand sourceHand;
	private Point lastPoint;
	private GCard movingCard;

	private Statistics statistic;

	// Variables for the player to customize in the Settings menu
	private static boolean animate = true;
	private static boolean autoComplete = true;

	// Variable for the display of the hint
	private boolean noMorePossibleMoves;

	/**
	 * Constructs a CardPanel by setting up the Panel and the Deck and all of
	 * required Hands to keep track of the free cells, foundations and cascades.
	 * Also sets up listeners for mouse events and a move list
	 * 
	 * @param parentFrame the main Frame that holds this panel
	 */
	public CardPanel(FreeCellMain parentFrame)
	{
		// Set up the size and background colour
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(TABLE_COLOUR);
		this.parentFrame = parentFrame;

		// Add mouse listeners to the card panel
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		// Set up the deck, cascades, foundations and free cells
		myDeck = new GDeck(400 - GCard.WIDTH / 2, 470);
		allHands = new ArrayList<GHand>();

		// Create Cascades
		int xCascade = CASCADE_X;
		int yCascade = CASCADE_Y;
		for (int i = 0; i < NO_OF_CASCADES; i++)
		{
			allHands.add(new Cascade(xCascade, yCascade));
			xCascade += CASCADE_SPACING;
		}

		// Create Free cells
		int xFreecell = this.FREECELL_X;
		int yFreecell = this.FREECELL_Y;
		for (int i = 0; i < this.NO_OF_FREECELLS; i++)
		{
			allHands.add(new FreeCell(xFreecell, yFreecell));
			xFreecell += TOP_SPACING;
		}

		// Create Foundations
		int xFoundation = FOUNDATION_X;
		int yFoundation = FOUNDATION_Y;
		for (int i = 0; i < this.NO_OF_FOUNDATIONS; i++)
		{
			allHands.add(new Foundation(xFoundation, yFoundation));
			xFoundation += TOP_SPACING;
		}

		movingCard = null;
		moves = new LinkedList<Move>();

		// Set up the Statistics object to record the results
		statistic = Statistics.readFromFile("stat.dat");

		noMorePossibleMoves = false;
	}

	/**
	 * Starts up a new game by clearing all of the Hands, shuffling the Deck and
	 * dealing new Cards to the Cascades. Also resets the move list
	 */
	public void newGame()
	{
		// Clear out all of the Hands
		for (Hand next : allHands)
			next.clear();

		myDeck.shuffle();

		// Deal the Cards to the Cascades (first 8 Hands)
		int cascasdeIndex = 0;
		while (myDeck.cardsLeft() > 0)
		{
			GCard dealtCard = myDeck.dealCard();
			Point pos = new Point(dealtCard.getPosition());
			allHands.get(cascasdeIndex).addCard(dealtCard);
			Point finalPos = new Point(dealtCard.getPosition());
			if (animate)
				moveACard(dealtCard, pos, finalPos);
			if (!dealtCard.isFaceUp())
				dealtCard.flip();
			cascasdeIndex++;
			if (cascasdeIndex == NO_OF_CASCADES)
				cascasdeIndex = 0;
		}
		moves.clear();
		parentFrame.setUndoOption(false);
		parentFrame.setHint(true);
		repaint();
	}

	/**
	 * Returns the current Statistics's information
	 * 
	 * @return the information about the current Statistics
	 */
	public String getStatistic()
	{
		return statistic.info();
	}

	/**
	 * Static class to change the auto complete on or off
	 */
	public static void changeAutoComplete()
	{
		autoComplete = !autoComplete;
	}

	/**
	 * Static class to change the animation on or off
	 */
	public static void changeAnimation()
	{
		animate = !animate;
	}

	/**
	 * Undoes the last move
	 */
	public void undo()
	{
		if (canUndo())
		{
			Move lastMove = moves.removeLast();
			lastMove.undo();
			repaint();
		}
	}

	/**
	 * Ends the current game of Freecell and record the Statistics
	 */
	public void endGame()
	{
		for (GHand hand : allHands)
		{
			if (hand.cardsLeft() != 0)
			{
				// Checks if the current game is a winner then record the
				// winning data, else record the losing data
				if (!checkForWinner())
				{
					statistic.lose();
				}
				else
					statistic.win(moves.size());
				statistic.writeToFile("stat.dat");
				return;
			}
		}
	}

	/**
	 * Checks if there are any moves in the moves list so that we can see if it
	 * is ok to undo a move
	 * 
	 * @return true if we can undo, false if not
	 */
	public boolean canUndo()
	{
		if (!checkForWinner())
			return !moves.isEmpty();
		return false;
	}

	/**
	 * Returns all the Moves possible between Cascades
	 * 
	 * @return an ArrayList of all the possible Moves between Cascades
	 */
	public ArrayList<Move> allCascadeMoves()
	{
		ArrayList<Move> allMoves = new ArrayList<Move>();
		// Get all moves from each of the Cascades
		for (GHand from : allHands.subList(0, NO_OF_CASCADES))
		{
			for (Movable movable : ((Cascade) from).getAllMovables())
			{
				// See if any of these Movables can be placed on another
				// Cascade. It turns out the "from != to" check is not
				// needed but it is less work than calling canPlaceOn
				for (GHand to : allHands.subList(0, NO_OF_CASCADES))
				{
					if (from != to && movable.canPlaceOn(to))
						allMoves.add(new Move(from, to, movable));
				}
			}
		}
		return allMoves;
	}

	/**
	 * Draws the information in this CardPanel. Draws the Deck, all of the
	 * hands, the moving Card in an animation and the selected Card or GHand.
	 * Also draws the String for no more possible moves when needed
	 * 
	 * @param g the Graphics context to do the drawing
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		if (checkForWinner())
			g.drawImage(fireWork, 0, 0, null);

		// Draw the deck if there are cards left
		if (myDeck.cardsLeft() > 0)
			myDeck.draw(g);

		// Draw all of the Hands
		for (GHand next : allHands)
			next.draw(g);

		// For animation to draw the moving Card
		if (movingCard != null)
			movingCard.draw(g);

		// Draw selected GHand or Card on top
		if (selectedItem != null)
			selectedItem.draw(g);

		// To display that there are no more possible Hints when there are no
		// more possible moves found in the hint
		if (noMorePossibleMoves)
		{
			g.setColor(Color.BLACK);
			g.drawString("There are no more possible moves.", 50, 550);
		}
	}

	/**
	 * Auto moves any Cards up to the Foundations when possible
	 */
	private void autoComplete()
	{
		// Return if the user does not want auto complete
		if (!autoComplete)
			return;

		// Search through the Cascades and FreeCells to see if any cards can be
		// placed onto a foundation
		for (GHand hand : allHands.subList(0, NO_OF_CASCADES + NO_OF_FREECELLS))
		{
			for (GHand foundation : allHands.subList(NO_OF_CASCADES
					+ NO_OF_FREECELLS, allHands.size()))
			{
				if (hand.cardsLeft() > 0)
				{
					GCard topCard = hand.getTopCard();

					// If the top Card can be placed on Foundation and the rank
					// of the top Card is less than or equal to two, or there
					// are no more Cards that can be placed on top of the top
					// Card, move the Card to the corresponding foundation
					if (topCard.canPlaceOn(foundation)
							&& (topCard.getRank() <= 2 || !canPlaceBelowCard(topCard)))
					{
						sourceHand = hand;
						selectedItem = hand.removeTopCard();

						moveACard(topCard, topCard.getPosition(),
								foundation.getPosition());

						// Add the auto complete move to the LinkedList
						moves.addLast(new Move(sourceHand, foundation,
								selectedItem));

						selectedItem.placeOn(foundation);
						selectedItem = null;
						repaint();

						// Calls itself again to make sure that no more Cards
						// can be auto completed
						autoComplete();
					}
				}

			}
		}

	}

	/**
	 * Determines if any other Cards can be placed on the current GCard
	 * 
	 * @param topCard the GCard to check if any other Cards can be placed onto
	 * @return true if there are Cards that can be placed onto it, false
	 *         otherwise
	 */
	private boolean canPlaceBelowCard(GCard topCard)
	{
		for (GHand cascade : allHands.subList(0, NO_OF_CASCADES
				+ NO_OF_FREECELLS))
		{
			for (Card card : cascade.hand)
			{
				// If any other Card can be placed on the current Card, return
				// true
				if (card.canPlaceOnCascade(topCard))
					return true;
			}
		}
		return false;
	}

	/**
	 * Checks to see if the player has won by completing all of the foundations
	 * 
	 * @return true if they have won, false if not
	 */
	private boolean checkForWinner()
	{
		for (GHand nextFoundation : allHands.subList(NO_OF_CASCADES
				+ NO_OF_FREECELLS, allHands.size()))
		{
			if (nextFoundation.cardsLeft() < 13)
				return false;
		}
		return true;
	}

	/**
	 * Checks if a hint can be shown
	 * 
	 * @return true if a hint can be shown, false otherwise
	 */
	public boolean canShowHint()
	{
		// If the player has already won or there are no Cards on the table,
		// return false
		if (!checkForWinner())
		{
			for (Hand hand : allHands)
				if (hand.cardsLeft() > 0)
					return true;
		}

		return false;
	}

	/**
	 * Displays a hint when possible
	 */
	public void showHint()
	{
		// Checks if any card on any of the Cards in FreeCell can be placed on
		// Cascades or Foundations
		for (GHand freecell : allHands.subList(NO_OF_CASCADES, NO_OF_CASCADES
				+ NO_OF_FREECELLS))
		{
			if (freecell.cardsLeft() > 0)
				for (GHand hand : allHands.subList(NO_OF_CASCADES,
						NO_OF_CASCADES + NO_OF_FREECELLS + NO_OF_FOUNDATIONS))
				{
					if (hand.cardsLeft() > 0
							&& freecell.getTopCard().canPlaceOn(hand))
					{
						// Flash the hint if any moves are found
						hand.getTopCard().flash();
						freecell.getTopCard().flash();
						paintImmediately(0, 0, getWidth(), getHeight());
						delay(300);
						hand.getTopCard().flash();
						freecell.getTopCard().flash();
						paintImmediately(0, 0, getWidth(), getHeight());
						return;
					}

				}
		}

		// Check for Moves between Cascades
		ArrayList<Move> move = allCascadeMoves();
		int size = move.size();
		// If any Move are found, display the hint
		if (size > 0)
		{
			// Display a random Move
			int random = (int) (Math.random() * size);

			move.get(random).showMove();
			paintImmediately(0, 0, getWidth(), getHeight());

			delay(300);

			move.get(random).notShowMove();
			paintImmediately(0, 0, getWidth(), getHeight());
			return;
		}
		// If no Moves are found, try to place Cards onto empty FreeCells
		else if (FreeCell.getNoOfEmptyFreecells() > 0)
		{
			// Find the Cascade that contains the least number of Cards
			int leastCardsCascade = 0;
			for (int cascade = 1; cascade < NO_OF_CASCADES; cascade++)
			{
				if (allHands.get(cascade).cardsLeft() < allHands.get(
						leastCardsCascade).cardsLeft())
					leastCardsCascade = cascade;
			}

			for (GHand freecell : allHands.subList(NO_OF_CASCADES,
					NO_OF_CASCADES + NO_OF_FREECELLS))
			{
				// Show the hint of placing Cards onto empty FreeCells
				if (freecell.cardsLeft() == 0)
				{
					allHands.get(leastCardsCascade).getTopCard().flash();
					freecell.flash();
					paintImmediately(0, 0, getWidth(), getHeight());

					delay(300);

					allHands.get(leastCardsCascade).getTopCard().flash();
					freecell.flash();
					paintImmediately(0, 0, getWidth(), getHeight());

					return;
				}
			}
		}
		else
		{
			// Show the player that there are no more possible Moves if no more
			// moves can be made
			noMorePossibleMoves = true;
			paintImmediately(0, 0, getWidth(), getHeight());
			delay(1000);
			noMorePossibleMoves = false;
			paintImmediately(0, 0, getWidth(), getHeight());
		}
	}

	/**
	 * Moves a Card during the animation
	 * 
	 * @param cardToMove Card that you want to move
	 * @param fromPos initial position of the Card
	 * @param toPos final position of the Card
	 */
	public void moveACard(final GCard cardToMove, Point fromPos, Point toPos)
	{
		int dx = (toPos.x - fromPos.x) / ANIMATION_FRAMES;
		int dy = (toPos.y - fromPos.y) / ANIMATION_FRAMES;

		for (int times = 1; times <= ANIMATION_FRAMES; times++)
		{
			fromPos.x += dx;
			fromPos.y += dy;
			cardToMove.setPosition(fromPos);

			// Update the drawing area
			paintImmediately(0, 0, getWidth(), getHeight());
			delay(25);

		}
		cardToMove.setPosition(toPos);
	}

	/**
	 * Delays the given number of milliseconds
	 * 
	 * @param milliSec number of milliseconds to delay
	 */
	private void delay(int milliSec)
	{
		try
		{
			Thread.sleep(milliSec);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * Handles the mouse pressed events to pick a Card or a Tableau
	 * 
	 * @param event event information for mouse pressed
	 */
	public void mousePressed(MouseEvent event)
	{
		if (selectedItem != null)
			return;
		Point selectedPoint = event.getPoint();

		// Pick up one of cards from a Hand (Freecell or Cascade)
		// Could also pick up from a Foundation if you want

		for (GHand nextHand : allHands)
		{
			if (nextHand.contains(selectedPoint)
					&& nextHand.canPickUp(selectedPoint))
			{
				// Split off a section of the Cascade or pick up a Card
				selectedItem = nextHand.pickUp(selectedPoint);

				// In case our move is not valid, we want to return the
				// Card(s) to where they initially came from
				sourceHand = nextHand;
				lastPoint = selectedPoint;
				repaint();
				return;
			}
		}
	}

	/**
	 * Handles the mouse released events to drop a Card or a Tableau
	 * 
	 * @param event event information for mouse released
	 */
	public void mouseReleased(MouseEvent event)
	{
		if (selectedItem != null)
		{
			// Check to see if we can add this to another cascade
			// foundation or free cell
			for (GHand nextHand : allHands)
			{
				if (selectedItem.intersects(nextHand)
						&& selectedItem.canPlaceOn(nextHand))
				{
					selectedItem.placeOn(nextHand);

					// Count this move if you didn't place it on the same spot
					if (nextHand != sourceHand)
					{
						moves.addLast(new Move(sourceHand, nextHand,
								selectedItem));
						parentFrame.setUndoOption(true);
					}
					selectedItem = null;
					repaint();

					// Check these things after a Card is dropped
					autoComplete();
					if (checkForWinner())
					{
						endGame();
						JOptionPane.showMessageDialog(parentFrame,
								"Congratulations \n" + statistic.info(),
								"You win", JOptionPane.INFORMATION_MESSAGE);
					}
					return;
				}
			}
			// Return to original spot if not a valid move
			selectedItem.placeOn(sourceHand);
			selectedItem = null;
			repaint();
		}
	}

	/**
	 * Handles the mouse dragged events to drag the moving card(s)
	 * 
	 * @param event event information for mouse dragged
	 */
	public void mouseDragged(MouseEvent event)
	{
		Point currentPoint = event.getPoint();

		if (selectedItem != null)
		{
			// We use the difference between the lastPoint and the
			// currentPoint to move the Cascade or Card so that the position of
			// the mouse on the Cascade/Card doesn't matter.
			// i.e. we can drag the card from any point on the card image
			selectedItem.move(lastPoint, currentPoint);
			lastPoint = currentPoint;
			repaint();
		}

	}

	/**
	 * Handles the mouse moved events to show which Cards can be picked up
	 * 
	 * @param event event information for mouse moved
	 */
	public void mouseMoved(MouseEvent event)
	{
		// Set the cursor to the hand if we are on a card that we can pick up
		Point currentPoint = event.getPoint();
		for (GHand nextHand : allHands)
		{
			if (nextHand.contains(currentPoint)
					&& nextHand.canPickUp(currentPoint))
			{

				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				return;
			}
		}
		// Otherwise we just use the default cursor
		setCursor(Cursor.getDefaultCursor());

	}

	// Extra methods needed since we implemented MouseListener
	// Not implemented in this class

	@Override
	public void mouseClicked(MouseEvent event)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent event)
	{
		// TODO Auto-generated method stub

	}
}
