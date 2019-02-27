package com.pixelround.spidersolitaire.game;

import java.util.*;
import java.io.IOException;
import java.io.*;
import java.util.concurrent.atomic.*;
import java.util.regex.Pattern;
import java.lang.reflect.*;

public class P2_Khot_Tanvi_SpiderSolitaire {
    /** Number of stacks on the board **/
    public final int NUM_STACKS = 7;

    /**
     * Number of complete decks used in the game. The number of cards in a deck
     * depends on the type of Card used. For example, a 1-suit deck of standard
     * playing cards consists of only 13 cards whereas a 4-suit deck consists of 52
     * cards.
     */
    public final int NUM_DECKS = 4;

    /** A Board contains stacks and a draw pile **/
    private P2_Khot_Tanvi_Board board;

    /** Used for keyboard input **/
    private Scanner input;

    public P2_Khot_Tanvi_SpiderSolitaire() {
        // Start a new game with NUM_STACKS stacks and NUM_DECKS of cards
        board = new P2_Khot_Tanvi_Board(NUM_STACKS, NUM_DECKS);
        input = new Scanner(System.in);
    }

    /** Main game loop that plays games until user wins or quits **/
    public void play() {

        Pattern pattern = Pattern.compile("[A23456789TJQK]");

        board.printBoard();
        boolean gameOver = false;

        while (!gameOver) {
            System.out.println("\nCommands:");
            System.out.println("   move [card] [source_stack] [destination_stack]");
            System.out.println("   draw");
            System.out.println("   clear [source_stack]");
            System.out.println("   restart");
            System.out.println("   save");
            System.out.println("   load");
            System.out.println("   quit");
            System.out.print(">");
            String line = input.nextLine();

            if (line.trim().length() == 0) {
                board.printBoard();
                continue;
            }
            Scanner parser = new Scanner(line);

            if (parser.hasNext(pattern)) {
                try {
                    String symbol = parser.next(pattern);
                    int sourceStack = parser.nextInt();
                    int destinationStack = parser.nextInt();
                    board.makeMove(symbol, sourceStack - 1, destinationStack - 1);
                } catch (InputMismatchException e) {
                    displayMoveUsage();
                }
            } else {

                String command = parser.next();

                if (command.equals("move")) {
                    /* *** TO BE MODIFIED IN ACTIVITY 5 *** */
                    try {
                        String symbol = parser.next();
                        int sourceStack = parser.nextInt();
                        int destinationStack = parser.nextInt();
                        board.makeMove(symbol, sourceStack - 1, destinationStack - 1);
                    } catch (InputMismatchException e) {
                        displayMoveUsage();
                    }
                } else if (command.equals("draw")) {
                    board.drawCards();
                } else if (command.equals("clear")) {
                    /* *** TO BE MODIFIED IN ACTIVITY 5 *** */
                    try {
                        int sourceStack = parser.nextInt();
                        board.clear(sourceStack - 1);
                    } catch (InputMismatchException e) {
                        displayClearUsage();
                    }
                } else if (command.equals("restart")) {
                    board = new P2_Khot_Tanvi_Board(NUM_STACKS, NUM_DECKS);
                } else if (command.equals("save")) {
                    saveState();
                } else if (command.equals("load")) {
                    this.loadSavedState();
                } else if (command.equals("quit")) {
                    System.out.println("Goodbye!");
                    System.exit(0);
                } else {
                    System.err.println("Invalid command.");
                }
            }

            if (parser.hasNextLine()) {
                System.err.println("Ignoring rest " + parser.nextLine());
            }

            board.printBoard();

            // If all stacks and the draw pile are clear, you win!
            if (board.isEmpty()) {
                gameOver = true;
            }
        }
        System.out.println("Congratulations!  You win!");

    }

    private void displayClearUsage() {
        System.out.println("Error: Please use the correct format for clear command");
        System.out.println("   clear [source_stack]");
    }

    private void displayMoveUsage() {
        System.out.println("Error: Please use the correct format for move command");
        System.out.println("    move [card] [source_stack] [destination_stack]");
    }

    // The JFileChooser was not working on my Mac. I found the following article
    // on stackoverflow.com :
    // https://stackoverflow.com/questions/33599014/jfilechooser-not-showing
    // from which I got the code to work successfully
    private void saveState() {
//        try {
//            JFileChooser fileChooser = new JFileChooser(".");
//            final AtomicReference<Integer> reference = new AtomicReference<>();
//            EventQueue.invokeAndWait(new Runnable() {
//                @Override
//                public void run() {
//                    reference.set(fileChooser.showSaveDialog(null));
//                }
//            });
//            // int result = fileChooser.showOpenDialog(dialog);
//            if (reference.get() == JFileChooser.APPROVE_OPTION) {
//                try {
//                    File saveFile = fileChooser.getSelectedFile();
//                    FileWriter out = new FileWriter(saveFile);
//                    out.write(board.getSaveState());
//                    out.close();
//                } catch (IOException e) {
//                    System.out.println("There was an error writing to the file.");
//                }
//            }
//        } catch (InterruptedException e) {
//            System.out.println("There was an error reading from the file");
//        } catch (InvocationTargetException e) {
//            System.out.println("There was an error reading from the file");
//        }
    }

    private void loadSavedState() {
//        try {
//            JFileChooser fileChooser = new JFileChooser(".");
//            final AtomicReference<Integer> reference = new AtomicReference<>();
//            EventQueue.invokeAndWait(new Runnable() {
//                @Override
//                public void run() {
//                    reference.set(fileChooser.showOpenDialog(null));
//                }
//            });
//            // int result = fileChooser.showOpenDialog(dialog);
//            if (reference.get() == JFileChooser.APPROVE_OPTION) {
//                try {
//                    File openFile = fileChooser.getSelectedFile();
//                    BufferedReader br = new BufferedReader(new FileReader(openFile));
//                    String str = "";
//                    String line;
//
//                    while ((line = br.readLine()) != null) {
//                        str += line;
//                        str += "\n";
//                    }
//                    br.close();
//                    board = new P2_Khot_Tanvi_Board(str);
//                } catch (IOException e) {
//                    System.out.println("There was an error reading from the file.");
//                }
//            }
//        } catch (InterruptedException e) {
//            System.out.println("There was an error reading from the file");
//        } catch (InvocationTargetException e) {
//            System.out.println("There was an error reading from the file");
//        }
    }
}
