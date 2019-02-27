package com.pixelround.spidersolitaire.game;

import com.pixelround.spidersolitaire.UndoService;

import java.util.*;

public class P2_Khot_Tanvi_Board
{
    /* *** TO BE IMPLEMENTED IN ACTIVITY 4 *** */
    // Attributes
    ArrayList<P2_Khot_Tanvi_Deck> stacks;
    P2_Khot_Tanvi_Deck drawPile;
    ArrayList<P2_Khot_Tanvi_Deck> completedStacks;

    public ArrayList<P2_Khot_Tanvi_Deck> getStacks() {
        return stacks;
    }

    public P2_Khot_Tanvi_Deck getDrawPile() {
        return drawPile;
    }

    /**
     *  Sets up the Board and fills the stacks and draw pile from a Deck
     *  consisting of numDecks Decks.  The number of Cards in a Deck
     *  depends on the number of suits. Here are examples:
     *
     *  # suits     # numDecks      #cards in overall Deck
     *      1            1          13 (all same suit)
     *      1            2          26 (all same suit)
     *      2            1          26 (one of each suit)
     *      2            2          52 (two of each suit)
     *      4            2          104 (two of each suit)
     *
     *  Once the overall Deck is built, it is shuffled and half the cards
     *  are placed as evenly as possible into the stacks.  The other half
     *  of the cards remain in the draw pile.  If you'd like to specify
     *  more than one suit, feel free to add to the parameter list.
     */
    public P2_Khot_Tanvi_Board(int numStacks, int numDecks) {
        /* *** TO BE IMPLEMENTED IN ACTIVITY 4 *** */
        // Build the deck
        P2_Khot_Tanvi_Deck deck = new P2_Khot_Tanvi_Deck();
        for (int i = 0; i < numDecks; i++) {
            deck.fillDeck();
        }
        deck.shuffle();

        // Draw cards from deck and move to stacks
        stacks = new ArrayList<>(numStacks);
        for (int i=0; i < numStacks; i++) {
            stacks.add(new P2_Khot_Tanvi_Deck());
        }

        int cardsForDrawPile = deck.size()/2/numStacks*numStacks;
        int cardsForStacks = deck.size() - cardsForDrawPile;
        for (int i = 0; i < cardsForStacks; i++) {
            int currentStack = i % numStacks;
            P2_Khot_Tanvi_Card card = deck.draw();
            if (card != null) {
                stacks.get(currentStack).add(card);
            }
        }
        for (P2_Khot_Tanvi_Deck stack : stacks) {
            stack.getTopCard().setFaceUp(true);
        }

        // remaining cards go to the drawPile
        drawPile = deck;
    }
    
    public P2_Khot_Tanvi_Board(String saveState) {
        this.setState(saveState);
    }

    /**
     *  Moves a run of cards from src to dest (if possible) and flips the
     *  next card if one is available.  Change the parameter list to match
     *  your implementation of Card if you need to.
     */
    public boolean makeMove(String symbol, int src, int dest) {
        /* *** TO BE IMPLEMENTED IN ACTIVITY 5 *** */
        if (src >= stacks.size() || dest >= stacks.size()) {
            System.out.println("****Invalid move****\n\n");
            return false;
        }
        P2_Khot_Tanvi_Deck stackSrc = stacks.get(src);
        P2_Khot_Tanvi_Deck stackDest = stacks.get(dest);

        int indexRequestedCard = stackSrc.getRunStartForSymbol(symbol);

        if (indexRequestedCard == -1) {
            System.out.println("****Invalid move****\n\n");
            return false;
        }

        int lastCardValue = stackSrc.getCard(indexRequestedCard).getValue();

        if (stackDest.size() > 0 && stackDest.getTopCard().getValue() != lastCardValue + 1) {
            System.out.println("****Invalid move****\n\n");
            return false;
        }

        String undoCommands = String.format("m%d%d%d", dest, src, stackDest.size());
        ArrayList<P2_Khot_Tanvi_Card> run = stackSrc.getRun(indexRequestedCard);
        stackDest.addAll(run);
        if (stackSrc.size() > 0) {
            P2_Khot_Tanvi_Card topCard = stackSrc.getTopCard();
            if (!topCard.isFaceUp())
                undoCommands += ";f";
            topCard.setFaceUp(true);
        }
        UndoService.instance.addCommand(undoCommands);
        return true;
    }

    // Undo methods

    public void moveBack(int sourceStack, int destStack, int startCardIndex, boolean flipCard) {
        P2_Khot_Tanvi_Deck stackSrc = stacks.get(sourceStack);
        P2_Khot_Tanvi_Deck stackDest = stacks.get(destStack);

        if (stackDest.getTopCard() != null)
            stackDest.getTopCard().setFaceUp(!flipCard);

        ArrayList<P2_Khot_Tanvi_Card> run = stackSrc.getRun(startCardIndex);
        stackDest.addAll(run);
    }

    public void unclearStack(int sourceStack, boolean flipCard) {
        P2_Khot_Tanvi_Deck stackSrc = stacks.get(sourceStack);
        if (stackSrc.getTopCard() != null)
            stackSrc.getTopCard().setFaceUp(!flipCard);
        stackSrc.unclearDeck();
    }

    public void removeTopCards() {
        ArrayList<P2_Khot_Tanvi_Card> topCards = new ArrayList<>();
        for (P2_Khot_Tanvi_Deck deck : stacks) {
            topCards.addAll(deck.deal(1));
        }
        Collections.reverse(topCards);
        drawPile.addAll(topCards);
    }

    /**
     *  Moves one card onto each stack, or as many as are available
     */
    public void drawCards() {
        /* *** TO BE IMPLEMENTED IN ACTIVITY 5 *** */
        for (int i = 0; i < stacks.size(); i++) {
            P2_Khot_Tanvi_Card card = drawPile.draw();
            if (card != null) {
                card.setFaceUp(true);
                stacks.get(i).add(card);
            }
        }
        UndoService.instance.addCommand("rt");
    }

    /**
     *  Returns true if all stacks and the draw pile are all empty
     */
    public boolean isEmpty() {
        /* *** TO BE IMPLEMENTED IN ACTIVITY 5 *** */
        for (P2_Khot_Tanvi_Deck stack : stacks) {
            if (stack.size() > 0) {
                return false;
            }
        }

        if (drawPile.size() > 0) {
            return false;
        }
        return true;
    }

    /**
     *  If there is a run of A through K starting at the end of sourceStack
     *  then the run is removed from the game or placed into a completed
     *  stacks area.
     *
     *  If there is not a run of A through K starting at the end of sourceStack
     *  then an invalid move message is displayed and the Board is not changed.
     */
    public boolean clear(int sourceStack) {
        /* *** TO BE IMPLEMENTED IN ACTIVITY 5 *** */
        if (sourceStack >= stacks.size()) {
            return false;
        }
        if (stacks.get(sourceStack).hasRun()) {
            stacks.get(sourceStack).removeRun();
        } else {
            return false;
        }
        String undoCommands = "c" + sourceStack;
        if (stacks.get(sourceStack).size() > 0) {
            P2_Khot_Tanvi_Card topCard = stacks.get(sourceStack).getTopCard();
            if (!topCard.isFaceUp())
                undoCommands += ";f";
            topCard.setFaceUp(true);
        }
        UndoService.instance.addCommand(undoCommands);
        return true;
    }

    /**
     * Prints the board to the terminal window by displaying the stacks, draw
     * pile, and done stacks (if you chose to have them)
     */
    public void printBoard() {
        /* *** TO BE IMPLEMENTED IN ACTIVITY 4 *** */
        int i=1;
        for (P2_Khot_Tanvi_Deck stack : stacks) {
            System.out.println(i + ": " + stack.toString());
            i++;
        }
        System.out.println();
        System.out.println("Draw Pile:");
        System.out.println(drawPile);
    }
    
    @Override
    public String toString() {
        String str = "";
        for (P2_Khot_Tanvi_Deck stack : stacks) {
            str += stack.toString();
            str += "\n";
        }
        str += "DrawPile: ";
        str += drawPile.toString();
        return str;
    }
    
    public String getSaveState() {
        String str = "";
        for (P2_Khot_Tanvi_Deck deck : stacks) {
            str += deck.getSaveState();
            str += "\n";
        }
        String drawPileStr = this.drawPile.getSaveState();
        if (drawPileStr == null || drawPileStr.trim().length() == 0) {
            str += "\n";
        } else {
            str += this.drawPile.getSaveState();
        }
        return str;
    }
    
    public void setState(String saveState) {
        String[] stateParts = saveState.split("\n", -1);
        System.out.println("State Parts: " + stateParts.length);
        this.stacks = new ArrayList<P2_Khot_Tanvi_Deck>();
        for (String deckState : stateParts) {
            P2_Khot_Tanvi_Deck deck = new P2_Khot_Tanvi_Deck(deckState);
            this.stacks.add(deck);
        }
        this.stacks.remove(this.stacks.size() - 1);
        this.drawPile = this.stacks.remove(this.stacks.size() - 1);
    }
}
