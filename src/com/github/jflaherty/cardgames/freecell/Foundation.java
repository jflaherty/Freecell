import java.awt.Point;

/**
 * Keeps track of a Foundation. This class extends all the methods and variables
 * from GHand. Cards can be checked if it can pick up. There is also a method to
 * deal with picking up a Card from a Foundation
 * 
 * @author Ridout and Veronica
 * @version November 2014
 */
public class Foundation extends GHand
{
	/**
	 * Creates a new Foundation with the points given
	 * 
	 * @param x the x point of the Foundation
	 * @param y the y point of the Foundation
	 */
	public Foundation(int x, int y)
	{
		super(x, y, 0);
	}

	/**
	 * Checks if a Card can be picked up
	 */
	public boolean canPickUp(Point point)
	{
		return false;
	}

	/**
	 * Picks up a Card(null) from a Foundation
	 */
	public Movable pickUp(Point point)
	{
		return null;
	}
}
