package com.github.jflaherty.cardgames.freecell;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import com.github.jflaherty.cardgames.playingcards.french.Card;

/**
 * An abstract class for a Graphical Hand (GHand). Inherits data and methods
 * from Hand. A Graphical Hand is a Hand of Graphical Cards (GCard). Includes
 * variables to keep track of the Hand's position, current width and height
 * based on the Cards in the Hand and the horizontal spacing of the Hand's
 * Cards. Includes methods to construct a new GHand, get the current position of
 * the Hand, add and remove GCards from this Hand, check if a point is contained
 * within this Hand, get the underlining Rectangle of this Hand and to draw this
 * Hand. Also includes two abstract methods to see if you can pick up a Movable
 * object from this Hand given a selection point and to pick up a Movable object
 * from this Hand, given a selection point. This class can also flash at the
 * base of the Card for hints that needs to be given
 * 
 * 
 * @author Ridout and Veronica
 * @version November 2014
 */

public abstract class FreeCellHand extends Hand
{
	protected Point position;
	private int width, height;
	private int spacing;
	private boolean flash;

	/**
	 * Constructs a new GHand with the given x and y position and horizontal
	 * spacing
	 * 
	 * @param x x position of upper left corner of the Hand
	 * @param y y position of upper left corner of the Hand
	 * @param spacing horizontal spacing between Cards
	 */
	public FreeCellHand(int x, int y, int spacing)
	{
		super();
		position = new Point(x, y);
		width = Card.WIDTH;
		height = Card.HEIGHT;
		this.spacing = spacing;
		flash = false;
	}

	/**
	 * Returns the position of the top left corner of this Hand
	 * 
	 * @return the position of the top left corner of this Hand
	 */
	public Point getPosition()
	{
		return position;
	}

	/**
	 * Adds a GCard to this Hand updating the position of the new Card and
	 * adjusting the spacing of the Cards accordingly
	 * 
	 * @param card the Card to add
	 */
	public void addCard(FreeCellCard card)
	{
		if (hand.size() > 0)
			card.setPosition(new Point(position.x, position.y + hand.size()
					* spacing));
		else
			card.setPosition(new Point(position));

		hand.add(card);
		updateHeight();
	}

	/**
	 * Removes a GCard from this Hand at the given index
	 * 
	 * @param index the index of the GCard to remove
	 * @return the GCard removed from the Hand
	 */
	public FreeCellCard removeCard(int index)
	{
		FreeCellCard cardToRemove = (FreeCellCard) hand.remove(index);
		updateHeight();
		return cardToRemove;
	}

	/**
	 * Looks at the top Card in this GHand
	 * 
	 * @return the top GCard is this Hand
	 */
	public FreeCellCard getTopCard()
	{
		return (FreeCellCard) hand.get(hand.size() - 1);
	}

	/**
	 * Removes the top Card from this GHand
	 * 
	 * @return the top GCard removed from the Hand
	 */
	public FreeCellCard removeTopCard()
	{
		return removeCard(hand.size() - 1);
	}

	/**
	 * Adjusts the height of this Hand after adding or removing a Card
	 */
	private void updateHeight()
	{
		if (hand.size() > 1)
			height = Card.HEIGHT + (hand.size() - 1) * spacing;
		else
			height = Card.HEIGHT;
	}

	/**
	 * Checks to see if the given point is contained within this Hand
	 * 
	 * @param point the point to check
	 * @return true if the point is in this Hand, false if not
	 */
	public boolean contains(Point point)
	{
		return (new Rectangle(position.x, position.y, width, height))
				.contains(point);
	}

	/**
	 * Returns the outlining Rectangle of this GHand
	 * 
	 * @return the outlining Rectangle of this GHand
	 */
	public Rectangle getRectangle()
	{
		return new Rectangle(position.x, position.y, width, height);
	}

	/**
	 * Displays the Cards in this Hand in this Hand's position
	 * 
	 * @param g Graphics context to display this GHand
	 */
	public void draw(Graphics g)
	{
		g.setColor(Color.getHSBColor(200, 075, 225));
		if (this instanceof FreeCell)
			g.setColor(Color.white);
		else if (this instanceof Foundation)
			g.setColor(Color.DARK_GRAY);
		g.fillRoundRect(position.x, position.y, Card.WIDTH, Card.HEIGHT, 8, 8);

		// Paint the base of the GHand a different color if flash for the hint
		// is needed
		if (flash)
		{
			g.setColor(Color.getHSBColor(200, 075, 225));
			if (this instanceof Cascade)
				g.setColor(Color.YELLOW);
			g.fillRoundRect(position.x - 5, position.y - 5, Card.WIDTH + 10,
					Card.HEIGHT + 10, 8, 8);
		}

		for (Card next : hand)
		{
			((FreeCellCard) next).draw(g);
		}
	}

	/**
	 * Flashes the place where the base of the Cards are when the GHand is drawn
	 */
	public void flash()
	{
		flash = !flash;
	}

	/**
	 * Checks if you can pick up a single Card or a tableau of Cards from this
	 * Hand based on the given point that is being selected.
	 * 
	 * @param point the point of the Hand that is selected
	 * @return true if you can pick up a single Card or a tableau of Cards based
	 *         on the given point, false if not
	 */
	public abstract boolean canPickUp(Point point);

	/**
	 * Based on the point of selection returns the Movable Card or Tableau that
	 * you can pick up.
	 * 
	 * @param point the point of the Hand that is selected
	 * @return the Movable Card or Tableau based on the point of selection
	 */
	public abstract Movable pickUp(Point point);

}
