import java.util.HashMap;

/**
 * Technical exercise for Whiteboard Federal Technologies.
 * 
 * Note: There's a commented out section of code from lines 85-89 that can be
 * used to test each frame's total score.
 *
 * @author Alicia Dao
 * @version January 25, 2020
 * 
 */
public class BowlingGame {

	// HashMap that keeps track of the index and an array that keeps track of the
	// frame score and how many bonus throws it has (one extra for spares and two
	// extra for strikes).
	private HashMap<Integer, Integer[]> framesMap;
	private String[] frames;
	private int firstThrow = 0;
	private boolean strike = false;
	private boolean spare = false;

	/**
	 * Main Method.
	 * 
	 * @param args String representation of a valid sequence of throws for one game
	 *             of American Ten-Pin Bowling
	 */
	public static void main(String[] args) {
		BowlingGame bowl = new BowlingGame();
		int finalScore = bowl.calculate(args);

		// prints the final score.
		System.out.println("Final Score: " + finalScore);
	}

	/**
	 * calculateScore - sums up the integers to get the final score.
	 * 
	 * @return int representation of final score.
	 */
	private int calculate(String[] input) {
		// split input array on dashes to separate each frame.
		frames = input[0].split("-");
		framesMap = new HashMap<>();

		// made it a local variable as it's not used in any other method.
		int secondThrow = 0;

		// loop through the frames 1-9.
		for (int i = 0; i < frames.length - 1; i++) {
			// strike
			if (frames[i].equals("X")) {
				firstThrow = 10;
				Integer[] temp = { 10, 2 };
				framesMap.put(i, temp);
				strikeOrSpare(i, 10, false);
				strike = true;
			}
			// spare
			else if (frames[i].contains("/")) {
				firstThrow = Integer.parseInt(frames[i].substring(0, 1));
				Integer[] temp = { 10, 1 };
				framesMap.put(i, temp);
				strikeOrSpare(i, 10, false);
				spare = true;
			}
			// two ints
			else {
				firstThrow = Integer.parseInt(frames[i].substring(0, 1));
				secondThrow = Integer.parseInt(frames[i].substring(1, 2));
				int frameScore = firstThrow + secondThrow;
				Integer[] temp = { frameScore, 0 };
				framesMap.put(i, temp);
				strikeOrSpare(i, frameScore, false);
			}

		}

		// calculate the last frame's score.
		lastFrame();

		// Used to test each frame's score. UNCOMMENT BELOW LINES TO USE.
//		int sum = 0;
//		for (int key : framesMap.keySet()) {
//			sum += framesMap.get(key)[0];
//			System.out.println("Frame: " + (key + 1) + " Score: " + framesMap.get(key)[0]);
//		}

		return framesMap.values().stream().map(array -> array[0]).reduce(0, Integer::sum);
	}

	/**
	 * lastFrame - returns the last frame's score.
	 */
	private void lastFrame() {
		// split the last frame's throws
		String[] last = frames[9].split("");
		Integer[] temp = { 0, 0 };
		framesMap.put(9, temp);

		// loop through the last frames throws.
		for (int i = 0; i < last.length; i++) {
			// strike
			if (last[i].equals("X")) {
				firstThrow = 10;
				framesMap.get(9)[0] += 10;
				strikeOrSpare(9, 10, true);
				strike = true;
			}
			// spare
			else if (last[i].equals("/")) {
				firstThrow = 10 - firstThrow;
				framesMap.get(9)[0] += firstThrow;
				strikeOrSpare(9, 10, true);
				spare = true;
			}
			// int
			else {
				firstThrow = Integer.parseInt(last[i]);
				framesMap.get(9)[0] += firstThrow;
				strikeOrSpare(9, firstThrow, true);

			}
		}
	}

	/**
	 * strikeOrSpare - determines if there's additional points to be added if there
	 * was a strike or spare from the previous frame.
	 * 
	 * @param index     of current frame.
	 * @param score     of current frame.
	 * @param lastFrame boolean to see if this method is being called from the last
	 *                  frame.
	 */
	private void strikeOrSpare(int index, int score, boolean lastFrame) {
		if (index > 0) {
			// check if previous frame was a strike or spare or it's the last frame.
			if (strike || spare || lastFrame) {
				// if it's the last frame and/or previous frame has an extra throw that still
				// needs to be added to it's score.
				if (lastFrame && framesMap.get(index - 1)[1] > 0 || framesMap.get(index - 1)[1] == 1) {
					framesMap.get(index - 1)[0] += firstThrow;
					framesMap.get(index - 1)[1]--;
				}
				// previous frame has two extra throws that still need to be added to it's
				// score.
				else if (framesMap.get(index - 1)[1] == 2) {

					// if this current frame is a strike, add 10 to previous frame and decrement
					// throws by one.
					if (firstThrow == 10) {
						framesMap.get(index - 1)[0] += score;
						framesMap.get(index - 1)[1]--;
					}
					// if current frame is not a strike, add the current frame's score to the
					// previous frame's score and decrement previous frame's throws by 2.
					else {
						framesMap.get(index - 1)[0] += score;
						framesMap.get(index - 1)[1] -= 2;
					}
				}
			}

			if (index > 1) {
				// if there was a strike two frames ago and that frame still has one extra throw
				// to be added to it, then add the current frame's score to it and then
				// decrement its throws.
				if (framesMap.get(index - 2)[1] == 1) {
					framesMap.get(index - 2)[0] += firstThrow;
					framesMap.get(index - 2)[1]--;
				}
			}

			// reset strike and spares for current frame.
			strike = false;
			spare = false;
		}

	}

}
