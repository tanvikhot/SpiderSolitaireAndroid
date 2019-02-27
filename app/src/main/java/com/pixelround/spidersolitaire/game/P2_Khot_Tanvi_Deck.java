package com.pixelround.spidersolitaire.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This lab was difficult at first because I had to figure out which methods
 * are needed in each class as I had to use my imagination and think thru to the fully
 * implemented game. Eventually I what was being asked was simple enough for me to
 * understand since it is broken down very nicely and I came up with some useful methods.
 */
public class P2_Khot_Tanvi_Deck {
    public ArrayList<P2_Khot_Tanvi_Card> cards;
    String[] symbols = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K"};
    int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

    public P2_Khot_Tanvi_Deck() {
        this.cards = new ArrayList<P2_Khot_Tanvi_Card>();
    }
    
    public P2_Khot_Tanvi_Deck(String saveState) {
        this.setState(saveState);
    }

    /* *** TO BE IMPLEMENTED IN ACTIVITY 3 *** */

    public void add(P2_Khot_Tanvi_Card card) {
        this.cards.add(card);
    }

    public void add(int index, P2_Khot_Tanvi_Card card) {
        this.cards.add(index, card);
    }

    public void addAll(ArrayList<P2_Khot_Tanvi_Card> cards) { this.cards.addAll(cards); }

    public void shuffle() {
        Random rand = new Random();
        int times = rand.nextInt(cards.size() * 20) + 1;
        for (int i = 0; i < times; i++) {
            int firstCard = rand.nextInt(cards.size());
            int secondCard = rand.nextInt(cards.size());
            //            System.out.println("Time number " + i + " switching " + firstCard + "  and " + secondCard);
            if (firstCard != secondCard) {
                P2_Khot_Tanvi_Card temp = cards.get(firstCard);
                cards.set(firstCard, cards.get(secondCard));
                cards.set(secondCard, temp);
            }
        }
    }

    /**
     * This method returns a list of requested cards from the top deck
     */
    public List<P2_Khot_Tanvi_Card> deal(int numberOfCards) {
        List<P2_Khot_Tanvi_Card> dealtCards = new ArrayList<P2_Khot_Tanvi_Card>();
        for (int i = 0; i < numberOfCards; i++) {
            if (this.cards.size() > 0) {
                P2_Khot_Tanvi_Card card = this.cards.remove(this.cards.size() - 1);
                dealtCards.add(0, card);
            } else {
                break;
            }
        }
        return dealtCards;
    }

    @Override
    public String toString() {
        return cards.toString();
    }

    public String toList(int highlighted) {
        highlighted = cards.size() - highlighted;
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (P2_Khot_Tanvi_Card card : cards) {
            if (i == highlighted) {
                sb.append("-->");
            }
            sb.append(card.toString());
            sb.append(" ");
            i++;
        }
        return sb.toString();
    }

    public P2_Khot_Tanvi_Card draw() {
        if (cards.size() > 0) {
            P2_Khot_Tanvi_Card card = cards.remove(cards.size() - 1);
            return card;
        }
        return null;
    }

    public P2_Khot_Tanvi_Card getTopCard() {
        if (cards.size() > 0) {
            P2_Khot_Tanvi_Card card = cards.get(cards.size() - 1);
            return card;
        }
        return null;
    }

    public P2_Khot_Tanvi_Card getCard(int index) {
        if (cards.size() > index) {
            P2_Khot_Tanvi_Card card = cards.get(index);
            return card;
        }
        return null;
    }

    public int size() {
        return cards.size();
    }

    public void clear() {
        this.cards.clear();
    }

    public boolean run() {
        int a = 0;
        if (cards.size() < 13) {
            return false;
        }
        while (cards.get(a).getValue() != 13) {
            a++;
        }
        for (int i = a; i < cards.size() - 1; i++) {
            if (cards.get(i + 1).getValue() + 1 != cards.get(i).getValue()) {
                return false;
            }
        }
        return true;
    }

    public int getRunStartForSymbol(String symbol) {
        int lastCardValue = -1;
        int indexRequestedCard = -1;
        for (int i = cards.size() - 1; i >= 0; i--) {
            P2_Khot_Tanvi_Card card = cards.get(i);

            if (!card.isFaceUp()) {
                break;
            }
            if (lastCardValue == -1) {
                lastCardValue = card.getValue();
                if (card.getSymbol().equals(symbol)) {
                    indexRequestedCard = i;
                    break;
                }
            } else if (card.getValue() == (lastCardValue + 1)) {
                lastCardValue = card.getValue();
                if (card.getSymbol().equals(symbol)) {
                    indexRequestedCard = i;
                    break;
                }
            } else {
                break;
            }
        }
        return indexRequestedCard;
    }

    public boolean hasRun() {
        boolean hasRun = false;
        if (cards.size() < 13) {
            return false;
        }

        int lastCardValue = -1;
        int countCards = 0;
        for (int i = cards.size() - 1; i >= 0; i--) {
            P2_Khot_Tanvi_Card card = cards.get(i);
            if (i == cards.size() - 1 && card.isFaceUp() && card.getValue() == 1) {
                countCards++;
                lastCardValue = 1;
            } else if (card.isFaceUp() && lastCardValue == card.getValue() - 1) {
                countCards++;
                lastCardValue = card.getValue();
            } else {
                hasRun = false;
            }
        }
        if (countCards == 13) {
            return true;
        }
        return hasRun;
    }

    public ArrayList<P2_Khot_Tanvi_Card> removeRun() {
        ArrayList<P2_Khot_Tanvi_Card> run = new ArrayList<>();
        if (cards.size() < 13) {
            return null;
        }

        for (int i = cards.size() - 1; i >= 0; i--) {
            if (run.size() == 0 && cards.get(i).getValue() == 1) {
                run.add(cards.remove(i));
            } else if (run.get(run.size() - 1).getValue() == cards.get(i).getValue() - 1) {
                run.add(cards.remove(i));
            } else {
                break;
            }
        }
        return run;
    }

    public ArrayList<P2_Khot_Tanvi_Card> getRun(int startingIndex) {
        ArrayList<P2_Khot_Tanvi_Card> run = new ArrayList<>();
        int count = cards.size() - startingIndex;
        for (int i = 0; i < count; i++) {
            run.add(cards.remove(startingIndex));
        }
        return run;
    }

    public void fillDeck() {
        //        for(int a = 1; a <= 4; a++){
        for (int i = 0; i < 13; i++) {
            cards.add(new P2_Khot_Tanvi_Card(symbols[i], values[i]));
        }
        //        }
    }

    public void unclearDeck() {
        for (int i = 12; i >= 0; i--) {
            P2_Khot_Tanvi_Card card = new P2_Khot_Tanvi_Card(symbols[i], values[i]);
            card.setFaceUp(true);
            cards.add(card);
        }
    }
    
    public String getSaveState() {
        String str = "";
        for (P2_Khot_Tanvi_Card card : cards) {
            if (str.length() > 0) {
                str += ",";
            }
            str += card.getSaveState();
        }
        return str;
    }
    
    public void setState(String saveState) {
        String[] stateParts = saveState.split(",");
        this.cards = new ArrayList<P2_Khot_Tanvi_Card>();
        if (saveState.trim().length() > 0) {
            for (String cardState : stateParts) {
                P2_Khot_Tanvi_Card card = new P2_Khot_Tanvi_Card(cardState);
                this.cards.add(card);
            }
        }
    }
}
