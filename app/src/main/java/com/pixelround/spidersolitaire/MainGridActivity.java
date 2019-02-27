package com.pixelround.spidersolitaire;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Constraints;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.PathInterpolator;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;
import com.pixelround.spidersolitaire.game.P2_Khot_Tanvi_Board;
import com.pixelround.spidersolitaire.game.P2_Khot_Tanvi_Card;
import com.pixelround.spidersolitaire.game.P2_Khot_Tanvi_Deck;
import com.pixelround.spidersolitaire.messages.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MainGridActivity extends AppCompatActivity {

    private int[] gridViewIds = {R.id.stack1, R.id.stack2, R.id.stack3, R.id.stack4, R.id.stack5, R.id.stack6, R.id.stack7};
    private int[] scrollViewIds = {R.id.scrollview1, R.id.scrollview2, R.id.scrollview3, R.id.scrollview4, R.id.scrollview5, R.id.scrollview6, R.id.scrollview7};
    private int[] cardImages = {R.drawable.cardback1, R.drawable.club_a, R.drawable.club_2, R.drawable.club_3, R.drawable.club_4, R.drawable.club_5, R.drawable.club_6, R.drawable.club_7, R.drawable.club_8, R.drawable.club_9, R.drawable.club_t, R.drawable.club_j, R.drawable.club_q, R.drawable.club_k};
    private P2_Khot_Tanvi_Board board;

    private SharedPreferences sharedPref;
    private Menu optionsMenu;

    private int moveFromStack = -1;
    private int moveCardIndex = -1;
    private View lastTouchedView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        this.board = new P2_Khot_Tanvi_Board(7, 4);

        FlexboxLayout drawPile = (FlexboxLayout) findViewById(R.id.drawpile);
        drawPile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw(v);
            }
        });
        initGrid();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        this.optionsMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_restart:
                restart();
                return true;

            case R.id.action_undo:
                undoMoves();
                initVariables();
                return true;

            case R.id.save_game:
                saveData();
                return true;

            case R.id.load_game:
                startActivity(new Intent(MainGridActivity.this, SaveActivity.class));

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void undoMoves() {
        List<String> commands = UndoService.instance.undoCommand();
        for (String commandLine : commands) {
            String[] commandParts = commandLine.split(";");
            if (commandLine.startsWith("m")) {
                String cardIndexStr = commandParts[0].substring(3);
                int cardIndex = Integer.parseInt(cardIndexStr);
                board.moveBack(commandParts[0].charAt(1) - 48, commandParts[0].charAt(2) - 48, cardIndex, commandParts.length == 2);
            } else if (commandLine.startsWith("c")) {
                board.unclearStack(commandParts[0].charAt(1) - 48, commandParts.length == 2);
            } else if (commandLine.equalsIgnoreCase("rt")) { // remove Top
                board.removeTopCards();
            }
        }
        initGrid();
    }

    private void initVariables() {
        moveFromStack = moveCardIndex = -1;
        lastTouchedView = null;
        FlexboxLayout completedStack = (FlexboxLayout) findViewById(R.id.completepile);
        completedStack.removeAllViews();
    }

    private void restart() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        board = new P2_Khot_Tanvi_Board(7, 4);
                        initGrid();
                        initVariables();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to start a new game?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void drawDeck(final int stackId, int deckNumber) {
        FlexboxLayout stack = (FlexboxLayout) findViewById(stackId);
        stack.removeAllViews();

        P2_Khot_Tanvi_Deck deck = this.board.getStacks().get(deckNumber);
        int cardCounter = 0;
        final int selectedStack = deckNumber;
        if (deck.cards.size() > 8) {
            stack.setJustifyContent(JustifyContent.FLEX_END);
        } else {
            stack.setJustifyContent(JustifyContent.FLEX_START);
        }
        for (final P2_Khot_Tanvi_Card card : deck.cards) {
            ImageView imageView = createImageView(cardImages[card.isFaceUp() ? card.getValue() : 0]);
            stack.addView(imageView);
            if (card.isFaceUp()) {
                final String cardSymbol = card.getSymbol();
                final int cardId = cardCounter;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastTouchedView != null) {
                            lastTouchedView.setBackgroundColor(Color.WHITE);
                            if (moveCardIndex == cardId && moveFromStack == selectedStack) {

                            } else {
                                P2_Khot_Tanvi_Deck stack = board.getStacks().get(moveFromStack);
                                if (stack.cards.size() > 0) {
                                    P2_Khot_Tanvi_Card card = stack.getCard(moveCardIndex);
                                    if (!board.makeMove(card.getSymbol(), moveFromStack, selectedStack)) {
                                        showShortToast("Invalid Move!");
                                    } else {
                                        // try to clear the stack
                                        if (board.clear(selectedStack)) {
                                            addToCompletedPile();
                                        }
                                    }
                                } else {
                                    showShortToast("No cards to move here!");
                                }
                                drawDeck(gridViewIds[moveFromStack], moveFromStack);
                                drawDeck(gridViewIds[selectedStack], selectedStack);
                                showVictoryMessage();
                                lastTouchedView = null;
                                moveFromStack = -1;
                                moveCardIndex = -1;
                            }
                        } else {
                            lastTouchedView = v;
                            moveFromStack = selectedStack;
                            moveCardIndex = cardId;
                            v.setBackgroundColor(Color.BLUE);
                        }

                    }
                });
            }
            cardCounter++;
        }
        final int fDeckNumber = deckNumber;
        stack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moveCardIndex != -1 && moveFromStack != -1) {
                    moveCardTo(selectedStack);
                }
            }
        });
    }

    private void addToCompletedPile() {
        FlexboxLayout completedStack = (FlexboxLayout) findViewById(R.id.completepile);
        completedStack.addView(createImageView(R.drawable.club_a));
        TextView footnote = (TextView) findViewById(R.id.footer);
        footnote.setVisibility(View.VISIBLE);
    }

    private void initGrid() {
        int deckNumber = 0;
        for (int id : gridViewIds) {
            drawDeck(id, deckNumber);
            deckNumber++;
        }

        int stackId = 0;
        for (int viewId : this.scrollViewIds) {
            final int selectedStack = stackId;
            HorizontalScrollView horizontalScrollView = (HorizontalScrollView) findViewById(viewId);
            horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent motionEvent) {
                    if (moveCardIndex != -1 && moveFromStack != -1) {
                        moveCardTo(selectedStack);
                    }
                    return false;
                }
            });
            stackId++;
        }

        FlexboxLayout drawPile = (FlexboxLayout) findViewById(R.id.drawpile);
        drawPile.removeAllViews();
        for (int i = 0; i < board.getDrawPile().cards.size(); i++) {
            ImageView imagebyCode = createImageView(cardImages[0]);
            drawPile.addView(imagebyCode);
        }
        showVictoryMessage();
    }

    private void showVictoryMessage() {
        if (board.isEmpty()) {
//            showLongToast("Congratulations! You win! Click \"Restart\" to play again.");
            showVictoryAnimation();
        }
    }

    private void showVictoryAnimation() {
        FlexboxLayout completedStacks = (FlexboxLayout) findViewById(R.id.completepile);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(toPixels(30, getResources().getDisplayMetrics()), toPixels(150, getResources().getDisplayMetrics()));
        params.bottomToBottom = R.id.mainlayout;
        params.startToStart = R.id.mainlayout;
        params.setMargins(10, 10, 10, 10);

        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.mainlayout);

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels;
        float dpWidth = displayMetrics.widthPixels;

        System.out.println(displayMetrics);
        Random random = new Random();

        int delay = 100;
        while (completedStacks.getChildAt(0) != null) {
            completedStacks.removeViewAt(0);
            for (int i = 1; i < cardImages.length; i++) {
                ImageView iv = createImageView(cardImages[i]);
                iv.setLayoutParams(params);
                layout.addView(iv, 0);
                float x = (float) (random.nextInt((int) dpWidth - 150) + 100);
                float y = (float) (random.nextInt((int) dpHeight - 600) + 100);
                float rotation = (float) (random.nextInt(80 - 15) + 15);
                System.out.println(String.format("%d: %.2f, %.2f, %.2f\n", i, x, y, rotation));
//                iv.animate().setStartDelay(delay).setDuration(1000).x(x).y(y).rotation(rotation).start();

                AnimatorSet set = new AnimatorSet();
                set.playTogether(
                        ObjectAnimator.ofFloat(iv, "rotationX", 0, 360),
                        ObjectAnimator.ofFloat(iv, "rotationY", 0, 180),
                        ObjectAnimator.ofFloat(iv, "translationX", 600f),
//                        ObjectAnimator.ofFloat(iv, "rotation", 0, -90),
//                        ObjectAnimator.ofFloat(iv, "translationX", 0, 90),
//                        ObjectAnimator.ofFloat(iv, "translationY", 0, 90),
                        ObjectAnimator.ofFloat(iv, "scaleX", 1, 100f),
                        ObjectAnimator.ofFloat(iv, "scaleY", 1, 100f),
                        ObjectAnimator.ofFloat(iv, "alpha", 1, 0f, 1)
                );
                set.setDuration(5 * 1000).start();
                return;
//                delay+=500;
            }
        }

//        iv.setLayoutParams(params);
//        iv.setId(View.generateViewId());
//        layout.addView(iv, 0);
//        iv.animate().setDuration(1000).x(300f).y(300f).rotation(30).start();

    }

    private void moveCardTo(int selectedStack) {
        P2_Khot_Tanvi_Deck stack = board.getStacks().get(moveFromStack);
        if (stack.cards.size() > 0) {
            P2_Khot_Tanvi_Card card = stack.getCard(moveCardIndex);
            if (!board.makeMove(card.getSymbol(), moveFromStack, selectedStack)) {
                showShortToast("Invalid Move!");
            } else {
                if (board.clear(selectedStack)) {
                    addToCompletedPile();
                }
            }
        } else {
            showShortToast("No cards to move here!");
        }
        drawDeck(gridViewIds[moveFromStack], moveFromStack);
        drawDeck(gridViewIds[selectedStack], selectedStack);
        showVictoryMessage();
        lastTouchedView = null;
        moveFromStack = -1;
        moveCardIndex = -1;
    }

    private ImageView createImageView(int imageResourceId) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(toPixels(30, getResources().getDisplayMetrics()), MATCH_PARENT);

        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageResource(imageResourceId);
        imageView.setId(View.generateViewId());

        return imageView;
    }

    private int toPixels(int dp, DisplayMetrics metrics) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public void draw(View view) {
        board.drawCards();
        initGrid();
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
            initGrid();
        } else {
            showShortToast("Cannot clear stack " + selectedStack + "!");
        }
    }

    private void showShortToast(String message) {
        showShortToast(message, Toast.LENGTH_SHORT);
    }

    private void showLongToast(String message) {
        showShortToast(message, Toast.LENGTH_LONG);
    }

    private void showShortToast(String message, int length) {
        Toast toast = Toast.makeText(getApplicationContext(), message, length);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void saveData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Game");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy-HH:mm");
        input.setHint("Give the save a name");
        input.setText(formatter.format(new Date()));
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String saveName = input.getText().toString();
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(saveName, board.getSaveState());
                String savesList = sharedPref.getString("SAVELIST", "");
                savesList += (savesList.trim().length() == 0?"":":_:") + saveName;
                editor.putString("SAVELIST", savesList);
                editor.commit();
                showShortToast("Saved to: " + saveName);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        MenuItem menuItem = this.optionsMenu.findItem(R.id.action_undo);
        if (messageEvent.message.equalsIgnoreCase("undoenable"))
            menuItem.setEnabled(true);
        else if (messageEvent.message.equalsIgnoreCase("undodisable"))
            menuItem.setEnabled(false);
        else if (messageEvent.message.trim().length() > 0) {
            board = new P2_Khot_Tanvi_Board(messageEvent.message);
            initGrid();
        }
    }
}
