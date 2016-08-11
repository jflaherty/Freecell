package com.github.jflaherty.cardgames.freecell;
import com.github.jflaherty.cardgames.playingcards.french.Card;
import com.github.jflaherty.cardgames.playingcards.french.Rank;
import com.github.jflaherty.cardgames.playingcards.french.Suit;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

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
public class FreeCellCard extends Card implements Movable
{
	private Point position;
	private Image image;
	private boolean flash;
	private boolean isFaceUp;

	/**
	 * Constructs a graphical Card
	 * 
	 * @param rank the rank of the Card
	 * @param suit the suit of the Card
	 * @param position the initial position of the Card
	 */
	public FreeCellCard(Rank rank, Suit suit, Point position)
	{
		this(rank, suit);
		this.position = position;
		
	}

	public FreeCellCard(Rank rank, Suit suit) {
		super(rank, suit);
		this.position = new Point(400 - WIDTH / 2, 470);
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
	public boolean intersects(FreeCellHand hand)
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
	 * Checks to see if you can place this GCard on the given GHand. Behavior
	 * will depend on what kind of GHand we are placing this object on
	 * 
	 * @param hand the GHand we want to place this GCard on
	 * @return true if this GHand can be placed on the given GHand, false if not
	 */
	public boolean canPlaceOn(FreeCellHand hand)
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
	public void placeOn(FreeCellHand otherHand)
	{
		otherHand.addCard(this);
	}
	
	/**
	 * Checks if the current Card can be placed on another Card. Assumes the
	 * other Card is in a cascade
	 * 
	 * @param otherCard the other Card to check if the current Card can be
	 *            placed on it
	 * @return true if the current Card can be placed on Cascade, false
	 *         otherwise
	 */
	public boolean canPlaceOnCascade(Card otherCard)
	{
		return getRank().asInt() + 1 == otherCard.getRank().asInt() 
				&& this.getSuit().getSuitColor() != otherCard.getSuit().getSuitColor();
	}

	/**
	 * Checks if the current Card can be on top of the other Card on the
	 * Foundation
	 * 
	 * @param otherCard the other Card on the Foundation to check if the current
	 *            Card can be on top of
	 * @return true if the current Card can be placed on top of the other Card,
	 *         false otherwise
	 */
	public boolean canPlaceOnFoundation(Card otherCard)
	{
		return this.getSuit() == otherCard.getSuit() 
				&& this.getRank().asInt() == otherCard.getRank().asInt() + 1;
	}

	/**
	 * Flashes the current GCard for showing of hints
	 */
	public void flash()
	{
		flash = !flash;
	}
	
	/**
	 * Flips the Card to the opposite side
	 */
	public void flip()
	{
		isFaceUp = !isFaceUp;
	}

	/**
	 * Checks if a Card is faced up
	 * 
	 * @return true if the Card is faced up and false if the Card is faced down
	 */
	public boolean isFaceUp()
	{
		return isFaceUp;
	}
	
	public String toString() {
		return super.getHandNotation();
		
	}

}
