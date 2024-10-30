import java.io.IOException;
import java.lang.Thread;
import java.lang.Integer;
import java.util.*;

public class Main {

	public static final String RED_FG = "\u001B[31m";
	public static final String GREEN_BG = "\u001B[42m";
	public static final String YELLOW_BG = "\u001B[43m";
	public static final String COLOR_RESET = "\u001B[0m";

	public static void header() throws IOException, InterruptedException {
		new ProcessBuilder("clear").inheritIO().start().waitFor();
		System.out.println("ML-M4: ACT4 - LRU");
		System.out.println("DELA CRUZ, JOHN LORENZ N.\n");
	}

	public static void pressEnterToContinue() {
		System.out.print("\nPress \'Enter\' key to continue...");
		try {
			Scanner scanner = new Scanner(System.in);
			scanner.nextLine();
		} catch (Exception e) {
		}
	}

  public static void subHeader(int frameQuantity, int pageQuantity, int pageReqQuantity) {
		System.out.println("Enter no. of Frames: " + frameQuantity);
    System.out.println("Enter no. of Pages: " + pageQuantity);
    System.out.println("Enter no. of Req. Pages: " + pageReqQuantity);
    System.out.println("\nLegend:");
    System.out.println("\t" + YELLOW_BG + "YELLOW" + COLOR_RESET + " - Allocation");
    System.out.println("\t" + GREEN_BG + "GREEN" + COLOR_RESET + " - Deallocation\n");
	}

	public static void lettersDisplay(int pageQuantity, char[] letters) {
		System.out.print("Available Page Name: ");
		for (int i = 0; i < pageQuantity; i++) {
			if (i == (pageQuantity - 1)) {
				System.out.println(letters[i]);
			} else {
				System.out.print(letters[i] + ", ");
			}
		}
	}

	public static void table(int frameQuantity, int pageReqQuantity, String requestInput[], String oldData[][],
			String dataHighlight[][], String frameTaken[], String frameHighlight[][], String pageFault[],
			int currentPage) {
		Formatter table = new Formatter();
		table.format("\n%8s", "");
		for (int i = 0; i < pageReqQuantity; i++) {
			table.format("%5s", "|" + RED_FG + " " + requestInput[i] + " " + COLOR_RESET);
		}
		table.format("|");

		for (int i = 0; i < frameQuantity; i++) {
			if (frameHighlight[currentPage][i] == "1") {
				table.format("\n%17s", GREEN_BG + "Frame " + (i + 1) + COLOR_RESET + " ");
			} else {
				table.format("\n%16s", COLOR_RESET + "Frame " + (i + 1) + COLOR_RESET + " ");
			}

			for (int j = 0; j < pageReqQuantity; j++) {
				if (dataHighlight[j][i] == "1") {
					table.format("%5s", "|" + YELLOW_BG + " " + oldData[j][i] + " " + COLOR_RESET);
				} else {
					table.format("%5s", "|" + COLOR_RESET + " " + oldData[j][i] + " " + COLOR_RESET);
				}
			}
			table.format("|");
		}
		table.format("\n%16s", COLOR_RESET + "PF " + COLOR_RESET);
		for (int i = 0; i < pageReqQuantity; i++) {
			table.format("%5s", "|" + COLOR_RESET + " " + pageFault[i] + " " + COLOR_RESET);
		}
		table.format("|");
		System.out.println(table);
	}

	public static void pageRankSorter(int frameQuantity, int highestRankIndex, int pageRank[]) {
		boolean lowestIndex = false;
		boolean similarRank = false;
		int indexCounter = 0;

		for (int j = 0; j < frameQuantity; j++) {
			if (pageRank[j] == 1) {
				lowestIndex = true;
				break;
			}
		}

		while (indexCounter < (frameQuantity - 1)) {
			for (int j = (indexCounter + 1); j < frameQuantity; j++) {
				if (pageRank[indexCounter] == pageRank[j]) {
					similarRank = true;
					break;
				}
			}
			indexCounter++;
		}

		for (int j = 0; j < frameQuantity; j++) {
			if (pageRank[j] != 1 && j != highestRankIndex) {
				if (pageRank[j] >= 2) {
					if (!lowestIndex) {
						pageRank[j] -= 1;
					} else if (similarRank) {
						pageRank[j] -= 1;
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		header();

		int frameQuantity = 0;
		int pageQuantity = 0;
		int pageReqQuantity = 0;
		int pageReqCounter = 0;

		boolean validFrameQuantity = false;
		boolean validPageQuantity = false;
		boolean validPageReq = false;
		boolean pageReqInput = true;
		boolean flag = false;

		char letter = ' ';
		char[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
				'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

		System.out.print("Enter no. of Frames: ");

		do {
			try {
				do {
					Scanner input = new Scanner(System.in);
					frameQuantity = input.nextInt();
					if (frameQuantity <= 0) {
						System.out.println("Invalid input. Please enter a positive number.");
						Thread.sleep(1250);
						header();
						System.out.print("Enter no. of Frames: ");
					}
				} while (frameQuantity <= 0);
				validFrameQuantity = true;
			} catch (InputMismatchException ex) {
				validFrameQuantity = false;
				System.out.println("Invalid input. Please enter a valid number.");
				Thread.sleep(1250);
				header();
				System.out.print("Enter no. of Frames: ");
			}
		} while (!validFrameQuantity);

		System.out.print("Enter no. of Pages: ");

		do {
			try {
				do {
					Scanner input = new Scanner(System.in);
					pageQuantity = input.nextInt();
					if (pageQuantity <= 0) {
						System.out.println("Invalid input. Please enter a positive number.");
						Thread.sleep(1250);
						header();
						System.out.println("Enter no. of Frames: " + frameQuantity);
						System.out.print("Enter no. of Pages: ");
					} else if (pageQuantity > 26) {
            System.out.println("Invalid input. Page quantity ranges from 26 letters only (A-Z).");
						Thread.sleep(1500);
						header();
						System.out.println("Enter no. of Frames: " + frameQuantity);
						System.out.print("Enter no. of Pages: ");
          }
				} while (pageQuantity <= 0 || pageQuantity > 26);
				validPageQuantity = true;
			} catch (InputMismatchException ex) {
				validPageQuantity = false;
				System.out.println("Invalid input. Please enter a valid number.");
				Thread.sleep(1250);
				header();
				System.out.println("Enter no. of Frames: " + frameQuantity);
				System.out.print("Enter no. of Pages: ");
			}
		} while (!validPageQuantity);

		System.out.print("Enter no. of Req. Pages: ");

		do {
			try {
				do {
					Scanner input = new Scanner(System.in);
					pageReqQuantity = input.nextInt();
					if (pageReqQuantity <= 0) {
						System.out.println("Invalid input. Please enter a positive number.");
						Thread.sleep(1250);
						header();
						System.out.println("Enter no. of Frames: " + frameQuantity);
						System.out.println("Enter no. of Pages: " + pageQuantity);
						System.out.print("Enter no. of Req. Pages: ");
					}
				} while (pageReqQuantity <= 0);
				validPageReq = true;
			} catch (InputMismatchException ex) {
				validPageReq = false;
				System.out.println("Invalid input. Please enter a valid number.");
				Thread.sleep(1250);
				header();
				System.out.println("Enter no. of Frames: " + frameQuantity);
				System.out.println("Enter no. of Pages: " + pageQuantity);
				System.out.print("Enter no. of Req. Pages: ");
			}
		} while (!validPageReq);

		char availableLetters[] = new char[pageQuantity];
		String requestInput[] = new String[pageReqQuantity];

		for (int i = 0; i < pageQuantity; i++) {
			availableLetters[i] = letters[i];
		}

		do {
			boolean validLetter = false;
			if (pageReqInput) {
				System.out.println("\nThe Requested Pages:");
				lettersDisplay(pageQuantity, letters);
				System.out.print("Input Request Page (" + (pageReqCounter + 1) + "/" + pageReqQuantity + "): ");
			}
			try {
				do {
					Scanner input = new Scanner(System.in);
					letter = input.next(".").charAt(0);
					letter = Character.toUpperCase(letter);
					for (int i = 0; i < availableLetters.length; i++) {
						if (letter == availableLetters[i]) {
							validLetter = true;
							break;
						}
					}
					if (!validLetter) {
						pageReqInput = false;
						System.out.println("Invalid input. The inputted page name is not available.");
						Thread.sleep(1500);
						header();
						System.out.println("Enter no. of Frames: " + frameQuantity);
						System.out.println("Enter no. of Pages: " + pageQuantity);
						System.out.println("Enter no. of Req. Pages: " + pageReqQuantity);
						System.out.println("\nThe Requested Pages:");
						lettersDisplay(pageQuantity, letters);
						System.out
								.print("Input Request Page (" + (pageReqCounter + 1) + "/" + pageReqQuantity + "): ");
					} else {

						pageReqInput = true;
						header();
						System.out.println("Enter no. of Frames: " + frameQuantity);
						System.out.println("Enter no. of Pages: " + pageQuantity);
						System.out.println("Enter no. of Req. Pages: " + pageReqQuantity);
					}
				} while (!pageReqInput);
				requestInput[pageReqCounter] = String.valueOf(letter);
				pageReqCounter++;
			} catch (InputMismatchException ex) {
				pageReqInput = false;
				System.out.println("Invalid input. Please enter a single character only.");
				Thread.sleep(1250);
				header();
				System.out.println("Enter no. of Frames: " + frameQuantity);
				System.out.println("Enter no. of Pages: " + pageQuantity);
				System.out.println("Enter no. of Req. Pages: " + pageReqQuantity);
				System.out.println("\nThe Requested Pages:");
				lettersDisplay(pageQuantity, letters);
				System.out.print("Input Request Page (" + (pageReqCounter + 1) + "/" + pageReqQuantity + "): ");
			}

		} while (pageReqCounter < pageReqQuantity);
    
    System.out.println("\nThe Requested Pages:");
    System.out.print("Page List: ");
    for (int i = 0; i < pageReqQuantity; i++){
      if (i == (pageReqQuantity-1)){
        System.out.println(requestInput[i]);
      } else {
        System.out.print(requestInput[i] + ", ");
      }
    }
		pressEnterToContinue();

		int currentRank = 1;
		int pageFaultCounter = 0;
		int pageRank[] = new int[frameQuantity];
		String frameTaken[] = new String[frameQuantity];

		String temporaryPage[] = new String[frameQuantity];
		String pageFault[] = new String[pageReqQuantity];

		for (int i = 0; i < frameQuantity; i++) {
			frameTaken[i] = " ";
		}

		for (int i = 0; i < pageReqQuantity; i++) {
			pageFault[i] = " ";
		}

		int currentPage = 0;
		int temporaryRankHolder = 0;
		int availableFrames = frameQuantity;

		String temporaryPageHolder = " ";

		String oldData[][] = new String[pageReqQuantity][frameQuantity];
		String dataHighlight[][] = new String[pageReqQuantity][frameQuantity];
		String frameHighlight[][] = new String[pageReqQuantity][frameQuantity];
		for (int i = 0; i < frameQuantity; i++) {
			for (int j = 0; j < pageReqQuantity; j++) {
				oldData[j][i] = " ";
				dataHighlight[j][i] = " ";
				frameHighlight[j][i] = " ";
			}
		}

		header();
		subHeader(frameQuantity, pageQuantity, pageReqQuantity);
		table(frameQuantity, pageReqQuantity, requestInput, oldData, dataHighlight, frameTaken, frameHighlight,
				pageFault, 0);

		for (int counter = 0; counter < pageReqQuantity; counter++) {
			boolean isRepeated = false;

			for (int i = 0; i < frameQuantity; i++) {
				boolean isValidFrame = false;
				int highestRankIndex = 0;

				if (frameTaken[i] == " ") {
					for (int j = 0; j < frameQuantity; j++) {
						if (frameTaken[j].equals(requestInput[counter])) {
							highestRankIndex = j;
							break;
						} else {
							highestRankIndex = i;
						}
					}
					frameTaken[i] = requestInput[counter];

					if (counter > 0) {
						boolean endLoop = false;
						int whileCounter = 0;
						int startingIndex = (counter - 1);

						for (int indexRepeat = 0; indexRepeat < frameQuantity; indexRepeat++) {
							temporaryPage[indexRepeat] = " ";
						}

						while (whileCounter < frameQuantity) {
							boolean invalid = false;
							if (whileCounter < counter) {
								for (int j = 0; j < frameQuantity; j++) {
									if (requestInput[startingIndex].equals(temporaryPage[j])) {
										invalid = true;
									}
								}

								if (!invalid) {
									temporaryPage[whileCounter] = requestInput[startingIndex];
									whileCounter++;
								}

								if (requestInput[startingIndex].equals(requestInput[counter])) {
									isValidFrame = false;
									isRepeated = true;
									frameTaken[i] = temporaryPageHolder;

									break;
								} else {
									isValidFrame = true;
									isRepeated = false;
								}

								if (startingIndex > 0) {
									startingIndex--;
								} else {
									break;
								}
							} else {
								break;
							}
						}
					}

					if (!isRepeated) {
						pageRank[i] = currentRank;
						dataHighlight[counter][i] = "1";
						pageFault[counter] = "*";
						pageFaultCounter++;

						if (flag) {
							frameHighlight[counter][i] = "1";
						}
						if (currentRank == frameQuantity) {
							flag = true;
						}

						pageRankSorter(frameQuantity, highestRankIndex, pageRank);

					} else {
						pageRank[i] = temporaryRankHolder;
						for (int j = 0; j < frameQuantity; j++) {
							if (frameTaken[j].equals(requestInput[counter])) {
								pageRank[j] = currentRank;
								break;
							}
						}
						pageRankSorter(frameQuantity, highestRankIndex, pageRank);
					}
					if (currentRank < frameQuantity) {
						currentRank++;
					}

					if ((!isRepeated || isValidFrame) && availableFrames != 0) {
						availableFrames--;
					}
					break;
				}
			}
			for (int anotherCounter = 0; anotherCounter < frameQuantity; anotherCounter++) {
				oldData[counter][anotherCounter] = frameTaken[anotherCounter];
			}

			pressEnterToContinue();
			header();
      subHeader(frameQuantity, pageQuantity, pageReqQuantity);
			table(frameQuantity, pageReqQuantity, requestInput, oldData, dataHighlight, frameTaken, frameHighlight,
					pageFault, counter);

			if (availableFrames == 0) {
				for (int i = 0; i < frameQuantity; i++) {
					if (pageRank[i] == 1) {
						temporaryPageHolder = frameTaken[i];
						temporaryRankHolder = pageRank[i];
						frameTaken[i] = " ";

						break;
					}
				}
			}
			currentPage++;
		}
		System.out.println("\n\tPage Fault - " + pageFaultCounter);
		System.out.println("\tPage Hit - " + (pageReqQuantity - pageFaultCounter));
	}
}