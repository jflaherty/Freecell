package com.github.jflaherty.cardgames.freecell;
import java.awt.Point;
import java.util.ArrayList;

import com.github.jflaherty.cardgames.playingcards.french.Card;

/**
 * Keeps track of a Cascade. Cascades inherits all the behaviors from GHand.
 * This class keeps track of the number of open Cascades. Also checks if the
 * Cards can be picked up based on a point, and has a method to pick up the
 * Cards chosen
 * 
 * @author Ridout and Veronica Huang
 * @version November 2014
 */
public class Cascade extends FreeCellHand
{
	static int openCascades;

	/**
	 * Constructs a new Cascade object with the given x and y points
	 * 
	 * @param x the x position of upper left corner of the Hand
	 * @param y the y position of upper left corner of the Hand
	 */
	public Cascade(int x, int y)
	{
		super(x, y, 20);
	}

	/**
	 * Get all the Movables that can be removed from this Cascade
	 * 
	 * @return all the Movables that can be removed from this Cascade
	 */
	public ArrayList<Movable> getAllMovables()
	{
		// Create a new ArrayList of Movable objects
		ArrayList<Movable> movables = new ArrayList<Movable>();
		// With an empty Cascade there are no Movables to add
		if (this.hand.size() == 0)
			return movables;
		// If we have at least one Card, the top Card is a Movable object
		movables.add(this.getTopCard());
		// Next, add all of the Tableaus of 2 or more cards
		// Starting from the top of the Cascade, check for valid Tableaus
		// using canPlaceOnCascade. We check up to the first Card of the
		// Cascade, quitting early if we can no longer create a valid Tableau
		// baseIndex is the index of the base Card of the next Tableau
		for (int baseIndex = this.hand.size() - 2; baseIndex >= 0
				&& ((FreeCellCard) this.hand.get(baseIndex + 1)).canPlaceOnCascade(
						this.hand.get(baseIndex)); baseIndex--)
		{
			// Create and add the next Tableau with all of its Cards
			// The position of the Tableau is the position of the base Card
			Point baseCardPos = ((FreeCellCard) hand.get(baseIndex)).getPosition();
			Tableau nextTableau = new Tableau(baseCardPos.x, baseCardPos.y,
					this);
			for (Card cardToAdd : hand.subList(baseIndex, hand.size()))
				nextTableau.addCard(cardToAdd);
			movables.add(nextTableau);
		}
		return movables;
	}

	/**
	 * Checks if you can pick up a single Card or a tableau of Cards from this
	 * Hand based on the given point that is being selected.
	 * 
	 * @param point the point of the Hand that is selected
	 * @return true if you can pick up a single Card or a tableau of Cards based
	 *         on the given point, false if not
	 */
	public boolean canPickUp(Point point)
	{
		// Find the index of the Card where the point is pointing at
		int index = cardsLeft() - 1;
		while (index >= 0 && !((FreeCellCard) (hand.get(index))).contains(point))
			index--;

		while (index < cardsLeft() - 1)
		{
			if (!((FreeCellCard) hand.get(index + 1)).canPlaceOnCascade(hand.get(index)))
				return false;
			index++;
		}

		return true;
	}

	/**
	 * Adds a GCard to this Hand updating the position of the new Card and
	 * adjusting the spacing of the Cards accordingly while updating the number
	 * of open Cascades
	 * 
	 * @param card the Card to add
	 */
	public void addCard(FreeCellCard card)
	{
		if (this instanceof Tableau)
		{
			super.addCard(card);
			return;
		}
		if (cardsLeft() == 0)
		{
			openCascades--;
		}
		super.addCard(card);

	}

	/**
	 * Removes a GCard from this Hand at the given index while updating the
	 * number of open Cascades
	 * 
	 * @param index the index of the GCard to remove
	 * @return the GCard removed from the Hand
	 */
	public FreeCellCard removeCard(int index)
	{
		FreeCellCard returnCard = super.removeCard(index);
		if (cardsLeft() == 0)
		{
			openCascades++;
		}
		return returnCard;
	}

	/**
	 * Clears the Cascade while updating the number of open Cascades
	 */
	public void clear()
	{
		super.clear();
		if (this instanceof Tableau)
			return;

		// Set the number of Open Cascades to the number of Cascades since the
		// only time a Cascade is cleared is at a beginning of a new game
		openCascades = 8;
	}

	/**
	 * Based on the point of selection returns the Movable Card or Tableau that
	 * you can pick up.
	 * 
	 * @param point the point of the Hand that is selected
	 * @return the Movable Card or Tableau based on the point of selection
	 */
	public Movable pickUp(Point point)
	{
		if (cardsLeft() == 0)
			return null;

		// Find the index of the Card that the point is pointing at
		int index = cardsLeft() - 1;
		while (index > 0 && !((FreeCellCard) (hand.get(index))).contains(point))
			index--;

		// Removes a single Card if the point is pointing at the top Card
		if (index == cardsLeft() - 1)
		{
			FreeCellCard card = getTopCard();
			removeTopCard();
			return card;
		}

		Tableau tableau = new Tableau(
				((FreeCellCard) hand.get(index)).getPosition().x,
				((FreeCellCard) hand.get(index)).getPosition().y, this);
		// Add all the Cards on top the Card chosen and the chosen Card itself
		// to the Tableau created
		while (index < cardsLeft())
		{
			FreeCellCard card = (FreeCellCard) hand.get(index);
			removeCard(index);
			tableau.addCard(card);
		}
		return tableau;

	}
}
