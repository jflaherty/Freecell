package com.github.jflaherty.cardgames.freecell;
import com.github.jflaherty.cardgames.playingcards.exceptions.EmptyDeckException;
import com.github.jflaherty.cardgames.playingcards.french.Card;
import com.github.jflaherty.cardgames.playingcards.french.Deck;
import com.github.jflaherty.cardgames.playingcards.french.Rank;
import com.github.jflaherty.cardgames.playingcards.french.Suit;

import java.awt.Graphics;
import java.awt.Point;

/**
 * Keeps track of a Graphical Deck (GDeck). Inherits data and methods from Deck.
 * Keeps track of this Deck's position.  Includes methods to construct this Deck,
 * shuffle the Deck, deal GCards from this Deck and to draw this Deck.
 * @author Ridout
 * @version November 2014
 */
public class FreeCellDeck extends Deck
{
	private Point position;
	
	public FreeCellDeck() {
		this(new Point(400 - Card.WIDTH / 2, 470));
	}

	/**
	 * Constructs a new Graphical Deck (GDeck)
	 * @param x the x coordinate of the upper left corner of the GDeck
	 * @param y the y coordinate of the upper left corner of the GDeck
	 */
	public FreeCellDeck(Point position)
	{
		super();
		this.position = position;
		for(Card card : deck) {
			FreeCellCard nextCard = (FreeCellCard) card;
			nextCard.setPosition(position);
		}
		
	}
	
	protected void initialize() {
		for (Suit s : Suit.values()) {
			for (Rank r : Rank.values()) {
				if(r.equals(Rank.ACE_H))
					continue;
				deck.add(new FreeCellCard(r, s));
			}
		}
	}

	/**
	 * Shuffles the GDeck by shuffling all of the Cards. Also sets the position
	 * of all of the Cards in the GDeck back to the GDecks's original position
	 */
	public void shuffle()
	{
		super.shuffleDeck();

		// Put the cards back to the position of the Deck
		
		for (Card card : deck) {
			FreeCellCard nextCard = (FreeCellCard) card;
			nextCard.setPosition(position);
			if(nextCard.isFaceUp()) {
				nextCard.flip();
			}
		}
	}

	/**
	 * Deals a GCard from this GDeck
	 * @return the dealt GCard
	 * @throws EmptyDeckException 
	 */
	public FreeCellCard dealCard()
	{
		try {
			return (FreeCellCard) super.drawTopCard();
		} catch (EmptyDeckException e) {
			return null;
		}
	}

	/**
	 * Displays the un-dealt Cards in this Hand
	 * @param g Graphics context to display the deck
	 */
	public void draw(Graphics g)
	{
		for (Card card : deck) 
		{
			FreeCellCard nextCard = (FreeCellCard) card;
			nextCard.draw(g);
		}
	}
}
