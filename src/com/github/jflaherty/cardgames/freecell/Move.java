package com.github.jflaherty.cardgames.freecell;
/**
 * Keeps track of a Move. This class contains methods to undo a Move or to show
 * or not show a Move
 * 
 * @author ICS4U and Veronica Huang
 * @version November 2014
 */
public class Move
{
	private FreeCellHand from;
	private FreeCellHand to;
	private Movable moved;

	/**
	 * Creates a new Move object keeping track of its moves
	 * 
	 * @param from the Hand that the Movable was moved from
	 * @param to the Hand that the Movable was moved to
	 * @param moved the Movable that was moved
	 */
	public Move(FreeCellHand from, FreeCellHand to, Movable moved)
	{
		this.from = from;
		this.to = to;
		this.moved = moved;
	}

	/**
	 * Undo a move
	 */
	public void undo()
	{
		// If only one Card was moved, move the top Card back to the place it
		// was moved from
		if (moved instanceof FreeCellCard)
		{
			to.removeTopCard();
			from.addCard((FreeCellCard) moved);
			return;
		}

		// Move the tableau back to where it was from
		Tableau tableau = (Tableau) moved;
		tableau.placeOn(from);
		for (int index = 0; index < tableau.cardsLeft(); index++)
		{
			to.removeTopCard();
		}

	}

	/**
	 * Show the Move by flashing the Cards that can be moved and the place to
	 * move the Cards to
	 */
	public void showMove()
	{
		moved.flash();

		// Flash the top Card if there are cards in the hand to place on
		if (to.cardsLeft() > 0)
			to.getTopCard().flash();
		else
			// Flash the base of the hand
			to.flash();
	}

	/**
	 * Stop flashing the Cards that can moved and the place to move the Cards to
	 */
	public void notShowMove()
	{
		moved.flash();

		// Stop flashing the top Card if there are cards in the hand to place on
		if (to.cardsLeft() > 0)
			to.getTopCard().flash();
		else
			// Stop flashing the base of the hand
			to.flash();
	}

}
