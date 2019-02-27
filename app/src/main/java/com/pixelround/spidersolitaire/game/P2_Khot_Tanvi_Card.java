package com.pixelround.spidersolitaire.game;

/**
 * Card.java
 *
 * <code>Card</code> represents a basic playing card.
 */
public class P2_Khot_Tanvi_Card implements Comparable<P2_Khot_Tanvi_Card>
{
    /** String value that holds the symbol of the card.
    Examples: "A", "Ace", "10", "Ten", "Wild", "Pikachu"
     */
    private String symbol;

    /** int value that holds the value this card is worth */
    private int value;

    /** boolean value that determines whether this card is face up or down */
    private boolean isFaceUp;

    /**
     * Creates a new <code>Card</code> instance.
     *
     * @param symbol  a <code>String</code> value representing the symbol of the card
     * @param value an <code>int</code> value containing the point value of the card
     */    
    public P2_Khot_Tanvi_Card(String symbol, int value) {
        /* *** TO BE IMPLEMENTED IN ACTIVITY 2 *** */
        this.symbol = symbol;
        this.value = value;
        this.isFaceUp = false;
    }

    public P2_Khot_Tanvi_Card(String saveState) {
        this.setState(saveState);
    }
    
    /**
     * Getter method to access this <code>Card</code>'s symbol.
     * 
     * @return this <code>Card</code>'s symbol.
     */
    public String getSymbol() {
        /* *** TO BE IMPLEMENTED IN ACTIVITY 2 *** */
        return symbol;
    }

    public int getValue() {
        /* *** TO BE IMPLEMENTED IN ACTIVITY 2 *** */
        return value;
    }

    public boolean isFaceUp() {
        return isFaceUp;
    }

    public void setFaceUp(boolean state) {
        isFaceUp = state;
    }

    /**
     * Returns whether or not this <code>Card</code> is equal to another
     *  
     *  @return whether or not this Card is equal to other.
     */
    public boolean equals(P2_Khot_Tanvi_Card other) {
        /* *** TO BE IMPLEMENTED IN ACTIVITY 2 *** */
        return other.value == this.value;
    }

    /**
     * Returns this card as a String.  If the card is face down, "X"
     * is returned.  Otherwise the symbol of the card is returned.
     *
     * @return a <code>String</code> containing the symbol and point
     *         value of the card.
     */
    @Override
    public String toString() {
        /* *** TO BE IMPLEMENTED IN ACTIVITY 2 *** */
        if (isFaceUp) {
            return symbol;
        }
        return "X";
    }

    /**
     * Returns 0 if this card's value and suit are the same
     * as the parameter class value and suit. This method returns -1 if the value 
     */
    public int compareTo(P2_Khot_Tanvi_Card card) {
        return this.value - card.value;
    }
    
    public String getSaveState() {
        return symbol + "|" + value + "|" + (isFaceUp?"Y":"N");
    }
    
    public void setState(String saveState) {
        String[] stateParts = saveState.split("\\|");
        this.symbol = stateParts[0];
        this.value = Integer.parseInt(stateParts[1]);
        this.isFaceUp = stateParts[2].equals("Y");
    }
}
