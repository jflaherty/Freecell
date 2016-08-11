package com.github.jflaherty.cardgames.freecell;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import com.github.jflaherty.cardgames.playingcards.exceptions.IllegalCardException;
import com.github.jflaherty.cardgames.playingcards.french.Card;

/**
 * A Hand Class that contains the information about the Cards in the current
 * Hand. This class allows Cards to be added to the hand. It also sorts the hand
 * by rank or sorts by suit. This class can check the value of the Hand and if
 * it is a BlackJack.
 * 
 * @author Veronica Huang
 * @version November 2014
 */
public class Hand
{
	protected ArrayList<Card> hand;

	/**
	 * Constructs a new empty hand with no Cards
	 */
	public Hand()
	{
		hand = new ArrayList<Card>(11);
	}

	/**
	 * Constructs a new hand with the given Cards in a String
	 * 
	 * @param handString the String that contains information about the Cards in
	 *            the Hand
	 * @throws IllegalCardException 
	 */
	public Hand(String handString) throws IllegalCardException
	{
		StringTokenizer card = new StringTokenizer(handString);
		hand = new ArrayList<Card>(11);
		while (card.hasMoreTokens())
		{
			Card newCard = new Card(card.nextToken());
			hand.add(newCard);
		}
	}

	/**
	 * Adds one Card to the current hand
	 * 
	 * @param card the Card to add to the current Hand
	 */
	public void addCard(Card card)
	{
		hand.add(card);
	}

	/**
	 * Deletes a Card from the current Hand
	 * 
	 * @param card the Card to remove from the hand
	 */
	public void removeCard(Card card)
	{
		int index = hand.indexOf(card);
		if (index >= 0)
			hand.remove(index);
	}

	/**
	 * Checks for the number of Cards left in the hand
	 * 
	 * @return the number of Cards left in the hand
	 */
	public int cardsLeft()
	{
		return hand.size();
	}

	/**
	 * Sorts the Hand by rank
	 */
	public void sortByRank()
	{
		Collections.sort(hand);
	}


	/**
	 * Returns the Hand's Cards in a String
	 * 
	 * @return the Hand's Cards in a String
	 */
	public String toString()
	{
		StringBuilder str = new StringBuilder(hand.size() * 3);
		for (int index = 0; index < hand.size(); index++)
		{
			str.append(hand.get(index) + " ");
		}
		return str.toString();
	}

	/**
	 * Clears the current Hand
	 */
	public void clear()
	{
		this.hand.clear();
	}
}
