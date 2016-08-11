import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

/**
 * Keeps track of a Graphical Card (GCard). Inherits data and methods from Card.
 * Keeps track of a position and an Image for each GCard. Also keep track of
 * some static variables for the background image and the width and height of
 * each Card. Includes methods to construct a new Card, look at and change a
 * Card's position and draw this Card. Also has a contains and intersects method
 * to check if the GCard contains a point or intersects with a Hand. GCards can
 * also flash to show hints. There is also a method that check if the GCard can
 * be placed on another Hand and place the GCard on top
 * 
 * @author Ridout and Veronica Huang
 * @version November 2014
 *
 */
public class GCard extends Card implements Movable
{
	public final static Image BACK_IMAGE = new ImageIcon("images\\blueback.png")
			.getImage();
	public final static int WIDTH = BACK_IMAGE.getWidth(null);
	public final static int HEIGHT = BACK_IMAGE.getHeight(null);

	private Point position;
	private Image image;
	private boolean flash;

	/**
	 * Constructs a graphical Card
	 * 
	 * @param rank the rank of the Card
	 * @param suit the suit of the Card
	 * @param position the initial position of the Card
	 */
	public GCard(int rank, int suit, Point position)
	{
		super(rank, suit);
		this.position = position;
		// Load up the appropriate image file for this card
		String imageFileName = "" + " cdhs".charAt(suit) + rank + ".png";
		imageFileName = "images\\" + imageFileName;
		image = new ImageIcon(imageFileName).getImage();

	}

	/**
	 * Sets the current position of this GCard
	 * 
	 * @param position the Card's current position
	 */
	public void setPosition(Point position)
	{
		this.position = position;
	}

	/**
	 * Gets the current position of this GCard
	 * 
	 * @return the Card's current position
	 */
	public Point getPosition()
	{
		return position;
	}

	/**
	 * Returns the Rectangle of the current GCard
	 * 
	 * @return the Rectangle of the current GCard
	 */
	public Rectangle getRectangle()
	{
		return new Rectangle(position.x, position.y, WIDTH, HEIGHT);
	}

	/**
	 * Draws a card in a Graphics context
	 * 
	 * @param g Graphics to draw the card in
	 */
	public void draw(Graphics g)
	{
		// Draw a outer border if the Card is flashing
		if (flash)
		{
			g.setColor(Color.getHSBColor(200, 075, 225));
			g.fillRoundRect(position.x - 5, position.y - 5, WIDTH + 10,
					HEIGHT + 10, 8, 8);
		}

		if (isFaceUp)
			g.drawImage(image, position.x, position.y, null);
		else
			g.drawImage(BACK_IMAGE, position.x, position.y, null);
	}

	/**
	 * Moves a Card by the amount between the initial and final position
	 * 
	 * @param initialPos the initial position to start dragging this Card
	 * @param initialPos the final position to keep dragging this Card
	 */
	public void move(Point initialPos, Point finalPos)
	{
		position.x += finalPos.x - initialPos.x;
		position.y += finalPos.y - initialPos.y;
	}

	/**
	 * Checks to see if the given point is contained within this GCard
	 * 
	 * @param point the point to check
	 * @return true if the point is in this object, false if not
	 */
	public boolean contains(Point point)
	{
		return (getRectangle().contains(point));
	}

	/**
	 * Checks to see if this GCard intersects the given GHand
	 * 
	 * @param hand the GHand to check for intersection
	 * @return true if this GCard intersects the given GHand, false if not
	 */
	public boolean intersects(GHand hand)
	{
		// Checks if the Hand has any cards in it, if yes, check if the GCard
		// intersects the topCard
		if (hand.cardsLeft() > 0)
			return (hand.getTopCard().getRectangle().intersects(this
					.getRectangle()));

		// If there are no Cards in the hand, check if the current Card
		// intersects with the hand's base Rectangle
		return hand.getRectangle().intersects(this.getRectangle());
	}

	/**
	 * Checks to see if you can place this GCard on the given GHand. Behaviour
	 * will depend on what kind of GHand we are placing this object on
	 * 
	 * @param hand the GHand we want to place this GCard on
	 * @return true if this GHand can be placed on the given GHand, false if not
	 */
	public boolean canPlaceOn(GHand hand)
	{
		// If there are Cards in the Hand, check based on the type of the Hand
		if (hand.cardsLeft() > 0)
		{
			if (hand instanceof Cascade)
				return canPlaceOnCascade(hand.getTopCard());
			else if (hand instanceof Foundation)
				return canPlaceOnFoundation(hand.getTopCard());
			// Cannot place on a FreeCell that contains Cards
			return false;
		}

		if (hand instanceof Cascade)
			return true;
		else if (hand instanceof Foundation)
			// In order to place on an empty Foundation, the current Card must
			// be an ace
			return isAce();
		return true;

	}

	/**
	 * Places this GCard on the given GHand
	 * 
	 * @param otherHand the GHand to place this GCard on
	 */
	public void placeOn(GHand otherHand)
	{
		otherHand.addCard(this);
	}

	/**
	 * Flashes the current GCard for showing of hints
	 */
	public void flash()
	{
		flash = !flash;
	}

}
