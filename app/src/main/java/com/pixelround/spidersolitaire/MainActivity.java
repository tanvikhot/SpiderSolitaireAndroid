package com.pixelround.spidersolitaire;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pixelround.spidersolitaire.game.P2_Khot_Tanvi_Board;
import com.pixelround.spidersolitaire.game.P2_Khot_Tanvi_Card;
import com.pixelround.spidersolitaire.game.P2_Khot_Tanvi_Deck;

public class MainActivity extends AppCompatActivity {

    static char[] cardFaces = new char[] { 'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k'};
    private P2_Khot_Tanvi_Board board;
    private int[] stackIds = new int[] { R.id.stack01, R.id.stack02, R.id.stack03, R.id.stack04, R.id.stack05, R.id.stack06, R.id.stack07 };
    private TextView moveFromTextView;
    private int moveFromStack = -1;
    private int moveCardIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        setupTextViews();

        this.restart(null);
    }

    private void setupTextViews() {
        for (int i : stackIds) {
            TextView textViewStack = (TextView) findViewById(i);
            textViewStack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    move(v);
                }
            });
        }
    }

    public void draw(View view) {
        board.drawCards();
        redrawBoard();
    }

    public void restart(View view) {
        Log.i("MainActivity", "Restarting");
        this.board = new P2_Khot_Tanvi_Board(7, 4);
        redrawBoard();
    }

    public void gridView(View view) {
        startActivity(new Intent(MainActivity.this, MainGridActivity.class));
    }

    private void redrawBoard() {
        int stack = 0;
        for (P2_Khot_Tanvi_Deck deck : board.getStacks()) {
            TextView textViewStack = (TextView) findViewById(stackIds[stack]);
            textViewStack.setText(deck.toList(-1));
            stack++;
        }
        TextView drawPile = (TextView) findViewById(R.id.drawpile);
        drawPile.setText(board.getDrawPile().cards.toString());
    }

    public void clear(View view) {
        LinearLayout clearButtons = (LinearLayout) findViewById(R.id.clearbuttons);
        clearButtons.setVisibility(View.VISIBLE);
    }

    public void clearWhat(View view) {
        int selectedStack = -1;
        switch (view.getId()) {
            case R.id.clearbutton1:
                System.out.println("Button 1 pressed");
                selectedStack = 0;
                break;
            case R.id.clearbutton2:
                System.out.println("Button 2 pressed");
                selectedStack = 1;
                break;
            case R.id.clearbutton3:
                System.out.println("Button 3 pressed");
                selectedStack = 2;
                break;
            case R.id.clearbutton4:
                System.out.println("Button 4 pressed");
                selectedStack = 3;
                break;
            case R.id.clearbutton5:
                System.out.println("Button 5 pressed");
                selectedStack = 4;
                break;
            case R.id.clearbutton6:
                System.out.println("Button 6 pressed");
                selectedStack = 5;
                break;
            case R.id.clearbutton7:
                System.out.println("Button 7 pressed");
                selectedStack = 6;
                break;
        }
        LinearLayout clearButtons = (LinearLayout) findViewById(R.id.clearbuttons);
        clearButtons.setVisibility(View.INVISIBLE);
        if (board.clear(selectedStack)) {
            redrawBoard();
            if (board.isEmpty()) {
                Toast.makeText(MainActivity.this, "Congratulations! You win! Click \"Restart\" to play again.",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Cannot clear stack " + selectedStack + "!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void move(View view) {
        int selectedStack = -1;
        TextView selectedTextView = null;

        switch (view.getId()) {
            case R.id.stack01:
                selectedTextView = (TextView) findViewById(R.id.stack01); selectedStack = 0; break;
            case R.id.stack02:
                selectedTextView = (TextView) findViewById(R.id.stack02); selectedStack = 1; break;
            case R.id.stack03:
                selectedTextView = (TextView) findViewById(R.id.stack03); selectedStack = 2; break;
            case R.id.stack04:
                selectedTextView = (TextView) findViewById(R.id.stack04); selectedStack = 3; break;
            case R.id.stack05:
                selectedTextView = (TextView) findViewById(R.id.stack05); selectedStack = 4; break;
            case R.id.stack06:
                selectedTextView = (TextView) findViewById(R.id.stack06); selectedStack = 5; break;
            case R.id.stack07:
                selectedTextView = (TextView) findViewById(R.id.stack07); selectedStack = 6; break;
        }

        if (moveFromStack == -1) {
            // new button pressed for move
            moveFromStack = selectedStack;
            moveFromTextView = selectedTextView;
            P2_Khot_Tanvi_Deck stack = board.getStacks().get(moveFromStack);
            if (stack.size() > 0) {
                moveCardIndex = 1;
            }
            moveFromTextView.setText(stack.toList(1));
        } else if (selectedStack == moveFromStack) {
            P2_Khot_Tanvi_Deck stack = board.getStacks().get(moveFromStack);
            moveCardIndex++;
            if (moveCardIndex > stack.size() || !stack.getCard(stack.size() - moveCardIndex).isFaceUp()) {
                moveFromTextView.setText(stack.toList(-1));
                moveFromTextView = null;
                moveFromStack = -1;
            } else {
                moveFromTextView.setText(stack.toList(moveCardIndex));
            }
        } else {
            // move to new stack
            P2_Khot_Tanvi_Deck stack = board.getStacks().get(moveFromStack);
            if (stack.cards.size() > 0) {
                P2_Khot_Tanvi_Card card = stack.getCard(stack.cards.size() - moveCardIndex);
                if (!board.makeMove(card.getSymbol(), moveFromStack, selectedStack)) {
                    Toast.makeText(MainActivity.this, "Invalid Move!",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "No cards to move here!",
                        Toast.LENGTH_SHORT).show();
            }
            this.redrawBoard();
            moveFromTextView = null;
            moveFromStack = -1;
        }

    }

    public static class GameOverDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.gameover)
                    .setPositiveButton(R.string.restart, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    })
                    .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
