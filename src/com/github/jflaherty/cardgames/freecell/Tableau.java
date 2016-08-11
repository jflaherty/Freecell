package com.github.jflaherty.cardgames.freecell;
import java.awt.Point;

import com.github.jflaherty.cardgames.playingcards.french.Card;

/**
 * Keeps track of a moving Cascade know as a Tableau. This Object is Movable and
 * inherits behavior from GHand. It can be checked for intersections or if it is
 * valid to be placed on another Hand. Also has a method to place this Tableau
 * onto another Hand. Tableaus can also flash to show possible moves for hints
 * 
 * @author Ridout and Veronica Huang
 * @version November 2014
 */
public class Tableau extends Cascade implements Movable
{
	private Hand sourceHand;

	/**
	 * Creates a new Tableau object with the given x and y points and the source
	 * hand
	 * 
	 * @param x the x point of the Tableau
	 * @param y the y point of the Tableau
	 * @param sourceHand the Hand that this Tableau of Cards was taken from
	 */
	public Tableau(int x, int y, Hand sourceHand)
	{
		super(x, y);
		this.sourceHand = sourceHand;
	}

	/**
	 * Moves a Tableau of Cards
	 * 
	 * @param initialPos the initial position of the Tableau
	 * @param finalPos the final position of the Tableau
	 */
	public void move(Point initialPos, Point finalPos)
	{
		for (int index = 0; index < cardsLeft(); index++)
			((FreeCellCard) (hand.get(index))).move(initialPos, finalPos);

		position.x += finalPos.x - initialPos.x;
		position.y += finalPos.y - initialPos.y;
	}

	/**
	 * Checks if the current tableau intersects with another Hand
	 * 
	 * @param otherHand the Hand to check for an intersection
	 */
	public boolean intersects(FreeCellHand otherHand)
	{
		// Checks if the tableau intersects the other Hand's top Card
		if (otherHand.cardsLeft() > 0)
			return (otherHand.getTopCard().getRectangle().intersects(this
					.getRectangle()));

		// If there is no top Card, check if the Tableau intersects with the
		// base of the other Hand
		return otherHand.getRectangle().intersects(this.getRectangle());
	}

	/**
	 * Checks if the current Tableau can be placed on another Hand
	 * 
	 * @param otherHand the Hand to check if this Tableau can be placed onto
	 */
	public boolean canPlaceOn(FreeCellHand otherHand)
	{
		// Calculate the number of Cards that can be Moved
		int cardsCanMove = 1 + FreeCell.getNoOfEmptyFreecells();
		for (int i = openCascades; i > 0; i--)
			cardsCanMove *= 2;
		// If the Tableau is to be placed on an empty Cascade, do not count the
		// Cascades to move to as an open Cascade
		if (openCascades > 0)
		{
			if (otherHand.cardsLeft() == 0)
				cardsCanMove /= 2;

			if (sourceHand.cardsLeft() == 0)
				cardsCanMove /= 2;
		}

		if (cardsLeft() > cardsCanMove)
			return false;

		// Check if the Tableau can be placed on the top of the otherHand based
		// on what the other Hand's type is
		if (otherHand instanceof Cascade)
			// Check if the base Card of the Tableau can be placed on the other
			// Hand
			return ((FreeCellCard) hand.get(0)).canPlaceOn(otherHand);

		// Check if the Tableau can be placed on Freecells or Tableaus
		if (cardsLeft() != 1)
			// Only one Card can be placed on FreeCells or Tableaus
			return false;
		return getTopCard().canPlaceOn(otherHand);
	}

	/**
	 * Places the current Tableau of Cards on top of another Hand
	 * 
	 * @param otherHand the Hand to place the Tableau onto
	 */
	public void placeOn(FreeCellHand otherHand)
	{
		// Place all the Cards in the Tableau to the other Hand
		for (int index = 0; index < cardsLeft(); index++)
		{
			FreeCellCard card = (FreeCellCard) hand.get(index);
			card.placeOn(otherHand);
		}
	}

	/**
	 * Flashes all the GCards in the Tableau to show for hints when the GCards
	 * are drawn
	 */
	public void flash()
	{
		for (Card card : hand)
			((FreeCellCard) card).flash();
	}

}
