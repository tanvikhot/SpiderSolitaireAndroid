package com.pixelround.spidersolitaire.game;

import java.util.List;

public class P2_Khot_Tanvi_CardTester
{
    public static void main(String[] args) {
        /* *** TO BE IMPLEMENTED IN ACTIVITY 2 *** */
        P2_Khot_Tanvi_Card card = new P2_Khot_Tanvi_Card ("A", 1);
        System.out.println(card);
        card.setFaceUp(true);
        System.out.println(card);
        
        P2_Khot_Tanvi_Card card2 = new P2_Khot_Tanvi_Card("T", 10);
        card2.setFaceUp(true);
        System.out.println(card2);
        System.out.println(card2.equals(card));
        System.out.println(card2.compareTo(card));
        
        P2_Khot_Tanvi_Card card3 = new P2_Khot_Tanvi_Card ("A", 1);
        card3.setFaceUp(true);
        System.out.println(card3.equals(card));
        System.out.println(card3.compareTo(card));
        
        
    }
}
