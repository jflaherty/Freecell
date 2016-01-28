/**
 * A Deck class that contains the Decks information including the cards in the
 * Deck and the top Card.
 * 
 * @author Veronica Huang
 * @version November 2014
 */
public class Deck
{
	protected Card[] deck;
	protected int topCard;

	/**
	 * Constructs a new Deck object with the given number of Cards
	 * 
	 * @param noOfDecks the number of decks inside the current Deck
	 */
	public Deck(int noOfDecks)
	{
		this.deck = new Card[noOfDecks * 52];
		this.topCard = 0;

		for (int currentDeck = 0; currentDeck < noOfDecks; currentDeck++)
		{
			for (int suit = 1; suit <= 4; suit++)
				for (int rank = 1; rank <= 13; rank++)
				{
					Card newCard = new Card(rank, suit);
					deck[topCard] = newCard;
					topCard++;
				}
		}
		topCard--;
	}

	/**
	 * Constructs a new Deck object with 52 Cards
	 */
	public Deck()
	{
		this.deck = new Card[52];
		this.topCard = 0;
		for (int suit = 1; suit <= 4; suit++)
			for (int rank = 1; rank <= 13; rank++)
			{
				Card newCard = new Card(rank, suit);
				deck[topCard] = newCard;
				topCard++;
			}
		topCard--;
	}

	/**
	 * Shuffles the Deck of Cards by the Fisher¨CYates shuffle algorithm
	 */
	public void shuffle()
	{
		topCard = deck.length - 1;
		// Flip all the Cards faced down
		for (int index = topCard; index >= 0; index--)
		{
			if (deck[index].isFaceUp())
				deck[index].flip();
		}

		for (int index = topCard; index > 0; index--)
		{
			int random = (int) (Math.random() * (index + 1));
			Card card = deck[index];
			deck[index] = deck[random];
			deck[random] = card;
		}
	}

	/**
	 * Checks for the number of Cards left in the current Deck
	 * 
	 * @return the number of Cards left in the Deck of Cards
	 */
	public int cardsLeft()
	{
		return topCard + 1;
	}

	/**
	 * Deals one Card from the current deck
	 * 
	 * @return the Card Dealt
	 */
	public Card dealCard()
	{
		if (topCard >= 0)
			return deck[topCard--];
		return null;
	}

	/**
	 * Returns the Deck's Cards in a String
	 * 
	 * @return the Deck's Cards in a String
	 */
	public String toString()
	{
		StringBuilder str = new StringBuilder(topCard * 3 + 1);
		for (int index = 0; index <= topCard; index++)
		{
			str.append(deck[index].toString() + " ");
		}
		return str.toString();
	}
}
