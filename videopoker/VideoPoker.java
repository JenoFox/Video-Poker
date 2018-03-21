package PJ4;

import java.util.*;

/*
 * Ref: http://en.wikipedia.org/wiki/Video_poker
 *      http://www.freeslots.com/poker.htm
 *
 *
 * Short Description and Poker rules:
 *
 * Video poker is also known as draw poker. 
 * The dealer uses a 52-card deck, which is played fresh after each playerHand. 
 * The player is dealt one five-card poker playerHand. 
 * After the first draw, which is automatic, you may hold any of the cards and draw 
 * again to replace the cards that you haven't chosen to hold. 
 * Your cards are compared to a table of winning combinations. 
 * The object is to get the best possible combination so that you earn the highest 
 * payout on the bet you placed. 
 *
 * Winning Combinations
 *  
 * 1. One Pair: one pair of the same card
 * 2. Two Pair: two sets of pairs of the same card denomination. 
 * 3. Three of a Kind: three cards of the same denomination. 
 * 4. Straight: five consecutive denomination cards of different suit. 
 * 5. Flush: five non-consecutive denomination cards of the same suit. 
 * 6. Full House: a set of three cards of the same denomination plus 
 * 	a set of two cards of the same denomination. 
 * 7. Four of a kind: four cards of the same denomination. 
 * 8. Straight Flush: five consecutive denomination cards of the same suit. 
 * 9. Royal Flush: five consecutive denomination cards of the same suit, 
 * 	starting from 10 and ending with an ace
 *
 */

/* This is the video poker game class.
 * It uses Decks and Card objects to implement video poker game.
 * Please do not modify any data fields or defined methods
 * You may add new data fields and methods
 * Note: You must implement defined methods
 */

public class VideoPoker {

	// default constant values
	private static final int startingBalance = 100;
	private static final int numberOfCards = 5;

	// default constant payout value and playerHand types
	private static final int[] multipliers = { 1, 2, 3, 5, 6, 10, 25, 50, 1000 };
	private static final String[] goodHandTypes = { "One Pair", "Two Pairs", "Three of a Kind", "Straight", "Flush	",
			"Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };

	// must use only one deck
	private final Decks oneDeck;

	// holding current poker 5-card hand, balance, bet
	private List<Card> playerHand;
	private int playerBalance;
	private int playerBet;

	/** default constructor, set balance = startingBalance */
	public VideoPoker() {
		this(startingBalance);
	}

	/** constructor, set given balance */
	public VideoPoker(int balance) {
		this.playerBalance = balance;
		oneDeck = new Decks(1, false);
	}

	/**
	 * This display the payout table based on multipliers and goodHandTypes
	 * arrays
	 */
	private void showPayoutTable() {
		System.out.println("\n\n");
		System.out.println("Payout Table   	      Multiplier   ");
		System.out.println("=======================================");
		int size = multipliers.length;
		for (int i = size - 1; i >= 0; i--) {
			System.out.println(goodHandTypes[i] + "\t|\t" + multipliers[i]);
		}
		System.out.println("\n\n");
	}

	/**
	 * Check current playerHand using multipliers and goodHandTypes arrays Must
	 * print yourHandType (default is "Sorry, you lost") at the end of function.
	 * This can be checked by testCheckHands() and main() method.
	 */
	private void checkHands() {
		// implement this method!

		if (checkRoyalFlush()) {
			System.out.println("\nYou have a Royal Flush!");
			playerBalance += playerBet * 1000;
		} else if (checkStraight() && checkFlush()) {
			System.out.println("\nYou have a Straight Flush!");
			playerBalance += playerBet * 50;
		} else if (checkFlush()) {
			System.out.println("\nYou have a Flush!");
			playerBalance += playerBet * 6;
		} else if (checkStraight()) {
			System.out.println("\nYou have a Straight!");
			playerBalance += playerBet * 5;
		} else if (checkFourOfKind()) {
			System.out.println("\nYou have a four of a kind!");
			playerBalance += playerBet * 25;
		} else if (checkFullHouse()) {
			System.out.println("\nYou have a Full House!");
			playerBalance += playerBet * 10;
		} else if (checkThreeOfKind()) {
			System.out.println("\nYou have three of a kind!");
			playerBalance += playerBet * 3;
		} else if (checkTwoPair()) {
			System.out.println("\nYou have two pairs!");
			playerBalance += playerBet * 2;
		} else if (checkPair()) {
			System.out.println("\nYou have a pair!");
		} else {
			System.out.println("\nYou do not have any winning hands!");
			playerBalance = playerBalance - playerBet;
		}

	}

	/*************************************************
	 * add new private methods here ....
	 *
	 *************************************************/

	public void play() {
		/**
		 * The main algorithm for single player poker game
		 *
		 * Steps: showPayoutTable()
		 *
		 * ++ show balance, get bet verify bet value, update balance reset deck,
		 * shuffle deck, deal cards and display cards ask for positions of cards
		 * to replace get positions in one input line update cards check hands,
		 * display proper messages update balance if there is a payout if
		 * balance = O: end of program else ask if the player wants to play a
		 * new game if the answer is "no" : end of program else :
		 * showPayoutTable() if user wants to see it goto ++
		 */

		// implement this method!
		playerBalance = startingBalance;
		showPayoutTable();
		Scanner betScanner = new Scanner(System.in);
		Scanner lineInput = new Scanner(System.in);
		Scanner response = new Scanner(System.in);
		List<Card> temp = new ArrayList<Card>();
		String replace;
		String answer;
		while (playerBalance > 0) {
			System.out.println("--------------------------------------------------");
			System.out.println("Balance: $" + playerBalance);
			System.out.print("Please enter an amount to bet: ");
			playerBet = betScanner.nextInt();
			if (playerBet <= playerBalance) {
				try {
					oneDeck.reset();
					oneDeck.shuffle();
					playerHand = oneDeck.deal(numberOfCards);
					System.out.println(playerHand);
					System.out.print("Select positions of cards to replace (e.g. 1 4 5): ");
					replace = lineInput.nextLine();
					Scanner lineScanner = new Scanner(replace);
					while (lineScanner.hasNextInt()) {
						temp = oneDeck.deal(1);
						playerHand.set(lineScanner.nextInt() - 1, temp.get(0));
					}
					lineScanner.close();
					System.out.print(playerHand);
					checkHands();
					if (playerBalance != 0) {
						System.out.print("\nYour balance: $" + playerBalance + ", one more game? (y or n): ");
						answer = response.next();
						if (answer.equals("n")) {
							System.out.println("\nBye");
							betScanner.close();
							lineInput.close();
							response.close();
							System.exit(0);
						} else {
							System.out.print("\nWould you like to see the payout table? (y or n): ");
							answer = response.next();
							if (answer.equals("y")) {
								showPayoutTable();
							}
							continue;
						}
					}
				} catch (PlayingCardException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println("Insufficient funds.");
				continue;
			}

		}
		Scanner response2 = new Scanner(System.in);
		System.out.print("\nYour balance is 0. Would you like to play again? (y or n): ");
		String answer2 = response2.next();
		if (answer2.equals("y")) {
			play();
			response.close();
			betScanner.close();
			lineInput.close();
			response2.close();
		} else {
			System.out.println("\nBye");
			System.exit(0);
		}
		
	}

	/*************************************************
	 * Do not modify methods below
	 * /*************************************************
	 * 
	 * /** testCheckHands() is used to test checkHands() method checkHands()
	 * should print your current hand type
	 */
	private boolean checkPair() {
		Card t1 = playerHand.get(0);
		Card t2 = playerHand.get(1);
		Card t3 = playerHand.get(2);
		Card t4 = playerHand.get(3);
		Card t5 = playerHand.get(4);
		boolean isPair = false;
		int x[] = { t1.getRank(), t2.getRank(), t3.getRank(), t4.getRank(), t5.getRank() };
		Arrays.sort(x);
		if ((x[0] == x[1] && x[0] < x[2] && x[3] < x[4]) || (x[1] == x[2] && x[1] < x[3] && x[1] > x[0])
				|| (x[2] == x[3] && x[3] < x[4] && x[1] < x[2] && x[0] < x[1])
				|| (x[3] == x[4] && (x[2] != x[1] && x[1] != x[0]))) {
			isPair = true;
		}
		return isPair;
	}

	private boolean checkTwoPair() {
		Card t1 = playerHand.get(0);
		Card t2 = playerHand.get(1);
		Card t3 = playerHand.get(2);
		Card t4 = playerHand.get(3);
		Card t5 = playerHand.get(4);
		boolean isTwoPair = false;
		int x[] = { t1.getRank(), t2.getRank(), t3.getRank(), t4.getRank(), t5.getRank() };
		Arrays.sort(x);
		if ((x[0] == x[1] && x[1] != x[2] && x[2] == x[3])
				|| (x[1] == x[2] && x[2] != x[3] && x[3] == x[4] && x[1] != x[0])
				|| (x[0] == x[1] && x[1] != x[2] && x[3] == x[4] && x[2] < x[3])) {
			isTwoPair = true;
		}

		return isTwoPair;
	}

	private boolean checkThreeOfKind() {
		Card t1 = playerHand.get(0);
		Card t2 = playerHand.get(1);
		Card t3 = playerHand.get(2);
		Card t4 = playerHand.get(3);
		Card t5 = playerHand.get(4);
		boolean isThreeKind = false;
		int x[] = { t1.getRank(), t2.getRank(), t3.getRank(), t4.getRank(), t5.getRank() };
		Arrays.sort(x);
		if ((x[0] == x[1] && x[1] == x[2] && x[0] < x[3] && x[3] != x[4])
				|| (x[1] == x[2] && x[2] == x[3] && x[1] < x[4] && x[1] > x[0])
				|| (x[2] == x[3] && x[3] == x[4] && x[2] > x[1] && x[1] != x[0])) {
			isThreeKind = true;
		}

		return isThreeKind;
	}

	private boolean checkFullHouse() {
		Card t1 = playerHand.get(0);
		Card t2 = playerHand.get(1);
		Card t3 = playerHand.get(2);
		Card t4 = playerHand.get(3);
		Card t5 = playerHand.get(4);
		boolean isFullHouse = false;
		int x[] = { t1.getRank(), t2.getRank(), t3.getRank(), t4.getRank(), t5.getRank() };
		Arrays.sort(x);
		if ((x[0] == x[1] && x[1] == x[2] && x[3] == x[4]) || (x[0] == x[1] && x[2] == x[3] && x[3] == x[4])) {
			isFullHouse = true;
		}

		return isFullHouse;
	}

	private boolean checkFourOfKind() {
		Card t1 = playerHand.get(0);
		Card t2 = playerHand.get(1);
		Card t3 = playerHand.get(2);
		Card t4 = playerHand.get(3);
		Card t5 = playerHand.get(4);
		boolean isFour = false;
		int x[] = { t1.getRank(), t2.getRank(), t3.getRank(), t4.getRank(), t5.getRank() };
		Arrays.sort(x);
		if ((x[0] == x[1] && x[1] == x[2] && x[2] == x[3] && x[3] != x[4])
				|| (x[0] != x[1] && x[1] == x[2] && x[2] == x[3] && x[3] == x[4])) {
			isFour = true;
		}

		return isFour;
	}

	private boolean checkFlush() {
		Card t1 = playerHand.get(0);
		Card t2 = playerHand.get(1);
		Card t3 = playerHand.get(2);
		Card t4 = playerHand.get(3);
		Card t5 = playerHand.get(4);
		boolean isFlush = false;
		int x[] = { t1.getSuit(), t2.getSuit(), t3.getSuit(), t4.getSuit(), t5.getSuit() };
		Arrays.sort(x);
		if ((x[0] == x[1] && x[1] == x[2] && x[2] == x[3] && x[3] == x[4])) {
			isFlush = true;
		}

		return isFlush;
	}

	private boolean checkStraight() {
		Card t1 = playerHand.get(0);
		Card t2 = playerHand.get(1);
		Card t3 = playerHand.get(2);
		Card t4 = playerHand.get(3);
		Card t5 = playerHand.get(4);
		boolean isStraight = false;
		int x[] = { t1.getRank(), t2.getRank(), t3.getRank(), t4.getRank(), t5.getRank() };
		Arrays.sort(x);
		if ((x[1] == (x[0] + 1)) && (x[2] == (x[1] + 1)) && (x[3] == (x[2] + 1)) && (x[4] == (x[3] + 1))
				|| ((x[0] == 1) && (x[1] == 10) && (x[2] == 11) && (x[3] == 12) && (x[4] == 13))) {
			isStraight = true;
		}

		return isStraight;
	}

	private boolean checkRoyalFlush() {
		Card t1 = playerHand.get(0);
		Card t2 = playerHand.get(1);
		Card t3 = playerHand.get(2);
		Card t4 = playerHand.get(3);
		Card t5 = playerHand.get(4);
		boolean isRoyalFlush = false;
		int x[] = { t1.getRank(), t2.getRank(), t3.getRank(), t4.getRank(), t5.getRank() };
		Arrays.sort(x);
		if (checkFlush() && ((x[0] == 1) && (x[1] == 10) && (x[2] == 11) && (x[3] == 12) && (x[4] == 13))) {
			isRoyalFlush = true;
		}

		return isRoyalFlush;
	}

	public void testCheckHands() {
		try {
			playerHand = new ArrayList<Card>();

			// set Royal Flush
			playerHand.add(new Card(3, 1));
			playerHand.add(new Card(3, 10));
			playerHand.add(new Card(3, 12));
			playerHand.add(new Card(3, 11));
			playerHand.add(new Card(3, 13));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");

			// set Straight Flush
			playerHand.set(0, new Card(3, 9));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");

			// set Straight
			playerHand.set(4, new Card(1, 8));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");

			// set Flush
			playerHand.set(4, new Card(3, 5));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");

			// "Royal Pair" , "Two Pairs" , "Three of a Kind", "Straight",
			// "Flush ",
			// "Full House", "Four of a Kind", "Straight Flush", "Royal Flush"
			// };

			// set Four of a Kind
			playerHand.clear();
			playerHand.add(new Card(4, 8));
			playerHand.add(new Card(1, 8));
			playerHand.add(new Card(4, 12));
			playerHand.add(new Card(2, 8));
			playerHand.add(new Card(3, 8));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");

			// set Three of a Kind
			playerHand.set(4, new Card(4, 11));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");

			// set Full House
			playerHand.set(2, new Card(2, 11));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");

			// set Two Pairs
			playerHand.set(1, new Card(2, 9));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");

			// set One Pair
			playerHand.set(0, new Card(2, 3));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");

			// set One Pair
			playerHand.set(2, new Card(4, 3));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");

			// set no Pair
			playerHand.set(2, new Card(4, 6));
			System.out.println(playerHand);
			checkHands();
			System.out.println("-----------------------------------");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/* Quick testCheckHands() */
	public static void main(String args[]) {
		VideoPoker pokergame = new VideoPoker();
		pokergame.testCheckHands();
	}
}
