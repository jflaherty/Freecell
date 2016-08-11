package com.github.jflaherty.cardgames.freecell;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Statistics implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int noOfGames, noOfWins;
	private int currentStreak, longestStreak;
	private int leastMoves;

	/**
	 * Constructs a new Statics object with least moves to a really large
	 * number, everything else set to 0. Stores the values to "stat.dat"
	 */
	public Statistics()
	{
		leastMoves = Integer.MAX_VALUE;
		noOfGames = 0;
		noOfWins = 0;
		currentStreak = 0;
		longestStreak = 0;
		this.writeToFile("stat.dat");
	}

	/**
	 * Writes the statistics object to a file
	 * 
	 * @param fileName the name of the file
	 */
	public void writeToFile(String fileName)
	{
		// Since we may have trouble writing to the file, we
		// should include a try catch block to catch any errors
		try
		{
			// Write the entire Statistics object to a file
			ObjectOutputStream fileOut = new ObjectOutputStream(
					new FileOutputStream(fileName));
			fileOut.writeObject(this);
			fileOut.close();
		}
		catch (IOException exp)
		{
			System.out.println("Error writing to the file");
		}
	}

	/**
	 * Returns the information including the number of Games, number of wins,
	 * win percentage, current streak, longest stream and least moves of the
	 * Statistic in a String
	 * 
	 * @return the information of the Statistic in a String
	 */
	public String info()
	{
		if (leastMoves != Integer.MAX_VALUE)
			return String
					.format("Number of Games: %d%nNumber of Wins: %d%nWin Percentage: %.0f%%%nCurrent Streak: %d%nLongest Streak: %d%nLeast Moves: %d%n",
							noOfGames, noOfWins, noOfWins * 100.0 / noOfGames,
							currentStreak, longestStreak, leastMoves);
		return String
				.format("Number of Games: %d%nNumber of Wins: %d%nWin Percentage: %d%%%nCurrent Streak: %d%nLongest Streak: %d%nLeast Moves: %s%n",
						noOfGames, noOfWins, 0, currentStreak, longestStreak,
						"N/A");
	}

	/**
	 * Change the statistics when a game is won
	 * 
	 * @param move the number of moves to win the game
	 */
	public void win(int move)
	{
		noOfGames++;
		noOfWins++;
		currentStreak++;
		if (currentStreak > longestStreak)
			longestStreak = currentStreak;
		if (move < leastMoves)
			leastMoves = move;
	}

	/**
	 * Change the statistics when a game is lost
	 */
	public void lose()
	{
		noOfGames++;
		currentStreak = 0;
	}

	/**
	 * Reads the Statistics from a file
	 * 
	 * @param fileName the name of the file
	 * @return the Statistics object read from the file
	 */
	public static Statistics readFromFile(String fileName)
	{

		try
		{
			// Try to open the file and read in the statistics
			// information. Read the entire Statistics object from
			// a file. Since readObject returns a reference to an
			// Object we need to cast it to a Statistics object
			ObjectInputStream fileIn = new ObjectInputStream(
					new FileInputStream(fileName));
			Statistics stats = (Statistics) fileIn.readObject();
			fileIn.close();
			return stats;
		}

		catch (Exception exp)
		{
			// If we had trouble reading the file (e.g. it doesn¡¯t
			// exist or if our file has errors) an Exception will be
			// thrown and we can create and return a new Statistics
			// object with all values reset. This assumes we have a
			// a constructor to do this.
			return new Statistics();
		}
	}

}
