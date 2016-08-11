package com.github.jflaherty.cardgames.freecell;
import java.awt.Point;

/**
 * Keeps track of a FreeCell. FreeCells are assumed to be GHands. This class
 * also records the number of Empty FreeCells in the game
 * 
 * @author Ridout and Veronica Huang
 * @version November 2014
 */
public class FreeCell extends FreeCellHand
{
	static int noOfEmptyFreecells;

	/**
	 * Creates a new FreeCell object with the given x and y points
	 * 
	 * @param x the x position of the FreeCell
	 * @param y the y position of the FreeCell
	 */
	public FreeCell(int x, int y)
	{
		super(x, y, 0);
		noOfEmptyFreecells++;
	}

	/**
	 * Checks if a Card can be picked up
	 */
	public boolean canPickUp(Point point)
	{
		return cardsLeft() == 1;
	}

	/**
	 * Returns the number of empty FreeCells
	 * 
	 * @return the number of empty FreeCells
	 */
	public static int getNoOfEmptyFreecells()
	{
		return noOfEmptyFreecells;
	}

	/**
	 * Picks up a Card from the FreeCell
	 */
	public Movable pickUp(Point point)
	{
		FreeCellCard card = getTopCard();
		removeTopCard();
		return card;
	}

	/**
	 * Adds a Card to the FreeCell while updating the number of empty FreeCells
	 */
	public void addCard(FreeCellCard card)
	{
		super.addCard(card);
		noOfEmptyFreecells--;
	}

	/**
	 * Clears the Cards in the FreeCell while updating the number of empty
	 * FreeCells
	 */
	public void clear()
	{
		super.clear();
		// Updates the number of Empty FreeCells to the Number of FreeCells in
		// the game since the only time clear() is used is at the beginning of a
		// new game
		noOfEmptyFreecells = 4;
	}

	/**
	 * Removes a GCard from this Hand at the given index while updating the
	 * number of empty FreeCells
	 * 
	 * @param index the index of the GCard to remove
	 * @return the GCard removed from the Hand
	 */
	public FreeCellCard removeCard(int index)
	{
		FreeCellCard returnCard = super.removeCard(index);
		noOfEmptyFreecells++;
		return returnCard;
	}
}
