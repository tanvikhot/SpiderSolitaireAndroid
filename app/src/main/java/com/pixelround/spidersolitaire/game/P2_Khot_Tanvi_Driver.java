package com.pixelround.spidersolitaire.game;

/**
 * Reflection (Activity 4 to 5): The lab gets harder and harder but the challenge provided
 * becomes more interesting with every step. I was able to complete the 
 * makeMove method to my satisfaction. I have tested all the test cases but was not able to spend 
 * the time to complete a full games. 
 * 
 * Reflection (Activity 6 to 7): The activity 7 was a bit challenging and I was not able to complete it. I 
 * have added code for activity 6 and I have a very good idea of what needs to be done for activity 7.
 * I did not get time this past few days since I was away at a basketball tournament that
 * I took your permission 2 weeks ago.
 * 
 * Reflection (Activity 7): The unfinished part of loadState and saveState were implemented using means to save
 * state of card, deck and then board. I initially used a simple makeshift saveState but it was not complete since I
 * was using the toString method. Instead of toString I created the a getSaveState, setState and new constructor in 
 * the Card, Deck and Board classes.
 * I also had to use some code from a developer site to make JFileChooser work on a Mac. 
 * Please refer to this article: https://stackoverflow.com/questions/33599014/jfilechooser-not-showing
 * 
 */

public class P2_Khot_Tanvi_Driver
{
    public static void main(String[] args) {
        new P2_Khot_Tanvi_SpiderSolitaire().play();

    }
}
