import java.util.Comparator;

/**
 * Keeps track of a Card object's information including the rank, suit and
 * whether it is faced up or down. This class allows the Card to be flipped,
 * checks for the value of the card in a BlackJack game and it can also compare
 * Cards by rank and then suit.
 * 
 * @author Veronica Huang
 * @version November 2015
 */
public class Card implements Comparable<Card>
{
	private static final String RANKS = " A23456789TJQK";
	private static final String SUITS = " CDHS";

	// Constant Comparator object for comparing Cards by suits
	public static final Comparator<Card> SUIT_ORDER = new SuitOrder();

	private int rank;
	private int suit;
	protected boolean isFaceUp;

	/**
	 * Constructs a new faced down Card with the given integer rank and integer
	 * suit
	 * 
	 * @param rank the rank of the Card (1-13, where Jack=11, Queen=12, King=13)
	 * @param suit the suit of the Card (1-4, where 1=Clubs, 2=Diamonds,
	 *            3=Hearts, 4=Spades)
	 */
	public Card(int rank, int suit)
	{
		this.rank = rank;
		this.suit = suit;
	}

	/**
	 * Constructs a new faced down Card with the given rank and suit in a String
	 * 
	 * @param cardInfo the String that contains the rank and the suit of the
	 *            Card
	 */
	public Card(String cardInfo)
	{
		this.rank = RANKS.indexOf(Character.toUpperCase(cardInfo.charAt(0)));
		char cardSuit = cardInfo.charAt(1);
		if (Character.isUpperCase(cardSuit))
			isFaceUp = true;
		else
			cardSuit = Character.toUpperCase(cardSuit);
		this.suit = SUITS.indexOf(cardSuit);
	}

	/**
	 * Returns the Card's information including the rank and the suit in a
	 * String
	 * 
	 * @return the rank and the suit of the Card in the format of "RS"
	 */
	public String toString()
	{
		if (isFaceUp)
			return RANKS.charAt(rank) + "" + SUITS.charAt(suit);
		return Character.toLowerCase(RANKS.charAt(rank)) + ""
				+ Character.toLowerCase(SUITS.charAt(suit));
	}

	/**
	 * Checks if the object is equal to the current Card
	 * 
	 * @param object the object to compare to the current Card
	 * @return true if the two Objects are equal, otherwise false
	 */
	public boolean equals(Object object)
	{
		if (object instanceof Card)
			return ((Card) object).rank == this.rank
					&& ((Card) object).suit == this.suit;
		return false;
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

	/**
	 * Compares the current Card to another card by rank first then by suit
	 * 
	 * @param other the Card to compare the current Card to
	 * @return a negative if the current Card is smaller than the other Card, a
	 *         positive value if the current Card is larger than the other Card,
	 *         and 0 if they are the same
	 */
	public int compareTo(Card other)
	{
		if (this.rank != other.rank)
			return this.rank - other.rank;
		if (this.suit != other.suit)
			return this.suit - other.suit;
		return 0;
	}

	/**
	 * Checks if the current Card is an ace
	 * 
	 * @return true if the current Card is an ace, false otherwise
	 */
	public boolean isAce()
	{
		return rank == 1;
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
		return this.rank + 1 == otherCard.rank && this.suit != otherCard.suit
				&& this.suit + otherCard.suit != 5;
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
		return this.suit == otherCard.suit && this.rank == otherCard.rank + 1;
	}

	/**
	 * Returns the rank of the current Card
	 * 
	 * @return the rank of the current Card
	 */
	public int getRank()
	{
		return rank;
	}

	/**
	 * An inner Comparator class that compares two Cards by suits then rank
	 */
	private static class SuitOrder implements Comparator<Card>
	{
		/**
		 * Compares two Cards by the suits first then rank
		 * 
		 * @param first the first Card to be compared
		 * @param second the second Card to be compared
		 * @return a negative if the current Card is smaller than the other
		 *         Card, a positive if the current Card is larger than the other
		 *         Card, and 0 if they are the same
		 */
		public int compare(Card first, Card second)
		{
			if (first.suit != second.suit)
				return first.suit - second.suit;
			if (first.rank != second.rank)
				return first.rank - second.rank;
			return 0;
		}
	}
}
