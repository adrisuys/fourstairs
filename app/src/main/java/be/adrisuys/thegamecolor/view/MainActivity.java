package be.adrisuys.thegamecolor.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import be.adrisuys.thegamecolor.Presenter;
import be.adrisuys.thegamecolor.R;

public class MainActivity extends AppCompatActivity implements ViewInterface {

    private LinearLayout[] containers;
    private Button[] cards;
    private LinearLayout[] middleContainers;
    private Button[] middleCards;
    private TextView remainingCards;
    private LinearLayout jokersLayout;
    private ImageView jokerOne, jokerTwo, jokerThree;
    private Button deck;

    private int[] foalsRes;
    private int[] foalsColor;
    private int[] playingCardRes;
    private int[] playingCardColor;
    private int[] catsRes;
    private int[] catsColor;

    private int[] currentSetOfDesign;
    private int[] currentSetOfColors;
    public static int FOALS = 1;
    public static int CARD = 2;
    public static int CATS = 3;
    private int backgroundCardMode;

    private int challenge;
    private int playMode;
    public static int CLASSIC = 1;
    public static int CHALLENGE = 2;

    private int jokerCounter;
    private Presenter presenter;
    private int chosenCardIndex;
    private int difficulty;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private MediaPlayer mpWin;
    private MediaPlayer mpLose;
    private MediaPlayer mpJoker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getApplicationContext().getSharedPreferences("4stairs", MODE_PRIVATE);
        editor = sp.edit();
        difficulty = getIntent().getIntExtra("difficulty", 2);
        challenge = getIntent().getIntExtra("challenge", -1);
        if (challenge == -1){
            playMode = CLASSIC;
        } else {
            playMode = CHALLENGE;
            difficulty = 2;
        }
        backgroundCardMode = sp.getInt("mode", FOALS);
        setCardDesignGroups();
        setupLinearLayouts();
        setupButton();
        setUpMiddleElement();
        setUpMp();
        setupGame();
    }

    public void onSelectCard(View view){
        if (chosenCardIndex != - 1){
            containers[chosenCardIndex].setBackgroundColor(Color.TRANSPARENT);
        }
        chosenCardIndex = Integer.parseInt(view.getTag().toString());
        containers[chosenCardIndex].setBackgroundColor(generateColor(85, 255, 0));
        presenter.onPlayerCardSelected(chosenCardIndex);
    }

    public void onSelectCluster(View view) {
        if (chosenCardIndex == -1) return;
        containers[chosenCardIndex].setBackgroundColor(Color.TRANSPARENT);
        int clusterIndex = Integer.parseInt(view.getTag().toString());
        presenter.play(chosenCardIndex, clusterIndex);
    }

    public void onDrawCard(View view){
        presenter.drawCard();
    }

    public void onJokerUsed(View v){
        if (presenter.isJokerActive()){
            Toast.makeText(this, "A joker is already active", Toast.LENGTH_SHORT).show();
            return;
        }
        jokerCounter++;
        int index = Integer.parseInt(v.getTag().toString());
        if (index == 1){
            handleJoker(jokerOne);
        } else if (index == 2){
            handleJoker(jokerTwo);
        } else {
            handleJoker(jokerThree);
        }
    }

    @Override
    public void displayWrongCard() {
        Toast.makeText(this, "You cannot play that card on that pile !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidPlay() {
        if (chosenCardIndex != -1){
            containers[chosenCardIndex].setBackgroundColor(Color.TRANSPARENT);
            chosenCardIndex = -1;
        }
        updateUI();
    }

    @Override
    public void displayWin() {
        saveHS(0);
        if (sp.getBoolean("sound", false)) mpWin.start();
        showDialog(true, -1);
    }

    @Override
    public void displayLoss() {
        int currentHS = getCurrentHS();
        if (sp.getBoolean("sound", false)) mpLose.start();
        if (presenter.getNonPlayedCards() < currentHS){
            saveHS(presenter.getNonPlayedCards());
            showDialog(false, presenter.getNonPlayedCards());
        } else {
            showDialog(false, -1);
        }
    }

    @Override
    public void displayCannotDraw() {
        int diff = difficulty;
        if (difficulty == 1) diff = 2;
        Toast.makeText(this, "You have to play at least " + diff + " cards to draw new ones !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showValidClusters(List<Integer> validClustersIndex) {
        if (validClustersIndex == null){
            hideClusterHighlights();
            containers[chosenCardIndex].setBackgroundColor(Color.TRANSPARENT);
        } else {
            containers[chosenCardIndex].setBackgroundColor(generateColor(85, 255, 0));
            hideClusterHighlights();
            for (int i : validClustersIndex){
                middleContainers[i].setBackgroundColor(generateColor(255,0,255));
            }
        }

    }

    @Override
    public void updateUI() {
        hideClusterHighlights();
        displayPlayerCards();
        displayMiddlePiles();
        int remainingCards = presenter.getRemainingCards();
        this.remainingCards.setText("Remaining cards in deck : " + remainingCards);
        if (remainingCards > 0){
            deck.setBackgroundResource(R.drawable.back);
        } else {
            deck.setBackgroundResource(0);
        }
        if (difficulty == 1){
            if (jokerCounter >= 3){
                presenter.checkForEnd(false);
            } else {
                presenter.checkForEnd(true);
            }
        } else {
            presenter.checkForEnd(false);
        }
    }

    @Override
    public void onBackPressed() {
        showExitWarning();
    }

    private void displayPlayerCards() {
        for (int i = 0; i < presenter.getCards().size(); i++){
            cards[i].setText(String.valueOf(presenter.getCards().get(i)));
            setBackground(cards[i], presenter.getCards().get(i));
        }
        for (int i = presenter.getCards().size(); i < 8; i++){
            cards[i].setText("");
            cards[i].setBackgroundResource(0);
        }
    }

    private void setBackground(Button card, Integer integer) {
        if (integer == -1){
            card.setText("");
        } else {
            int tmp = integer / 10;
            if (tmp >= 0 && tmp < 10){
                card.setBackgroundResource(currentSetOfDesign[tmp]);
                card.setTextColor(currentSetOfColors[tmp]);
            }
        }
    }

    private void displayMiddlePiles() {
        for (int i = 0; i < presenter.getClusters().length; i++){
            middleCards[i].setText(String.valueOf(presenter.getClusters()[i]));
            setBackground(middleCards[i], presenter.getClusters()[i]);
        }
    }

    private void setupLinearLayouts() {
        LinearLayout containerOne = findViewById(R.id.container_one);
        LinearLayout containerTwo = findViewById(R.id.container_two);
        LinearLayout containerThree = findViewById(R.id.container_three);
        LinearLayout containerFour = findViewById(R.id.container_four);
        LinearLayout containerFive = findViewById(R.id.container_five);
        LinearLayout containerSix = findViewById(R.id.container_six);
        LinearLayout containerSeven = findViewById(R.id.container_seven);
        LinearLayout containerEight = findViewById(R.id.container_eight);
        containers = new LinearLayout[]{containerOne, containerTwo, containerThree, containerFour, containerFive, containerSix, containerSeven, containerEight};
        jokersLayout = findViewById(R.id.jokers);
        if (difficulty == 1){
            jokersLayout.setVisibility(View.VISIBLE);
        } else {
            jokersLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void setupButton() {
        Button cardOne = findViewById(R.id.card_one);
        Button cardTwo = findViewById(R.id.card_two);
        Button cardThree = findViewById(R.id.card_three);
        Button cardFour = findViewById(R.id.card_four);
        Button cardFive = findViewById(R.id.card_five);
        Button cardSix = findViewById(R.id.card_six);
        Button cardSeven = findViewById(R.id.card_seven);
        Button cardEight = findViewById(R.id.card_eight);
        cards = new Button[]{cardOne, cardTwo, cardThree, cardFour, cardFive, cardSix, cardSeven, cardEight};
        jokerOne = findViewById(R.id.joker_one);
        jokerTwo = findViewById(R.id.joker_two);
        jokerThree = findViewById(R.id.joker_three);
        deck = findViewById(R.id.deck);
    }

    private void setUpMiddleElement(){
        LinearLayout ll1 = findViewById(R.id.middle_container_one);
        LinearLayout ll2 = findViewById(R.id.middle_container_two);
        LinearLayout ll3 = findViewById(R.id.middle_container_three);
        LinearLayout ll4 = findViewById(R.id.middle_container_four);
        middleContainers = new LinearLayout[]{ll1, ll2, ll3, ll4};
        Button iv1 = findViewById(R.id.middle_card_one);
        Button iv2 = findViewById(R.id.middle_card_two);
        Button iv3 = findViewById(R.id.middle_card_three);
        Button iv4 = findViewById(R.id.middle_card_four);
        middleCards = new Button[]{iv1, iv2, iv3, iv4};
        remainingCards = findViewById(R.id.remaining_card);
    }

    private int getCurrentHS(){
        if (difficulty == 1){
            return sp.getInt("hs_super_easy", 98);
        } else if (difficulty == 2){
            return sp.getInt("hs_easy", 98);
        } else if (difficulty == 3){
            return sp.getInt("hs_avg", 98);
        } else {
            return sp.getInt("hs_hard", 98);
        }
    }

    private void showDialog(boolean won, int hs){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_win_lose);
        TextView title = dialog.findViewById(R.id.title);
        TextView message = dialog.findViewById(R.id.message);
        ImageView icon = dialog.findViewById(R.id.icon);
        TextView button = dialog.findViewById(R.id.btn);
        title.setText(won ? "Congrats !" : "No move possible");
        String msg;
        if (playMode == CHALLENGE && won){
            msg = "Challenge n°" + challenge + " done!";
        } else {
            if (won){
                msg = "";
            } else {
                if (hs == -1){
                    msg = "";
                } else {
                    msg = "New Highscore : " + hs;
                }
            }
        }
        message.setText(msg);
        icon.setBackgroundResource(won ? R.drawable.ic_sentiment_very_satisfied_black_24dp : R.drawable.ic_sentiment_dissatisfied_black_24dp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void showExitWarning(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_exit);
        TextView buttonYes = dialog.findViewById(R.id.btnYes);
        TextView buttonNo = dialog.findViewById(R.id.btnNo);
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            }
        });
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void saveHS(int newScore){
        if (playMode == CHALLENGE){
            int previousChallengeMade = sp.getInt("challenge", 15);
            if (challenge > previousChallengeMade){
                editor.putInt("challenge", challenge);
            }

        } else {
            if (difficulty == 1){
                editor.putInt("hs_super_easy", newScore);
                if (newScore == 0){
                    editor.putInt("win_super_easy", sp.getInt("win_super_easy", 0) + 1);
                }
            } else if (difficulty == 2){
                editor.putInt("hs_easy", newScore);
                if (newScore == 0){
                    editor.putInt("win_easy", sp.getInt("win_easy", 0) + 1);
                }
            } else if (difficulty == 3){
                editor.putInt("hs_avg", newScore);
                if (newScore == 0){
                    editor.putInt("win_avg", sp.getInt("win_avg", 0) + 1);
                }
            } else {
                editor.putInt("hs_hard", newScore);
                if (newScore == 0){
                    editor.putInt("win_hard", sp.getInt("win_hard", 0) + 1);
                }
            }
        }
        editor.commit();
    }

    private void hideClusterHighlights(){
        for (LinearLayout ll : middleContainers){
            ll.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private int generateColor(int r, int g, int b){
        Color color = new Color();
        return color.rgb(r, g, b);
    }

    private void handleJoker(ImageView joker){
        if (sp.getBoolean("sound", false)) mpJoker.start();
        joker.setBackgroundResource(0);
        joker.setEnabled(false);
        presenter.activateJoker();
        for (LinearLayout ll : middleContainers){
            ll.setBackgroundColor(generateColor(255,0,255));
        }
    }

    private void setCardDesignGroups(){
        foalsRes = new int[]{R.drawable.foals_zeros, R.drawable.foals_tens, R.drawable.foals_twenties, R.drawable.foals_thirties, R.drawable.foals_forties, R.drawable.foals_fifties, R.drawable.foals_sixties, R.drawable.foals_seventies, R.drawable.foals_eighties, R.drawable.foals_nineties};
        foalsColor = new int[]{Color.BLUE, Color.BLACK, Color.WHITE, Color.WHITE, generateColor(230, 32, 101), Color.WHITE, Color.WHITE, Color.BLACK, Color.WHITE, Color.WHITE};
        playingCardRes = new int[]{R.drawable.card_ases, R.drawable.card_kings, R.drawable.card_queens, R.drawable.card_jays, R.drawable.card_tens, R.drawable.card_nines, R.drawable.card_eights, R.drawable.card_sevens, R.drawable.card_sixes, R.drawable.card_fives};
        playingCardColor = new int[]{Color.GREEN, Color.BLUE, Color.WHITE, Color.WHITE, Color.BLUE, Color.WHITE, Color.WHITE, Color.YELLOW, Color.RED, Color.BLUE};
        catsRes = new int[]{R.drawable.cats_0, R.drawable.cats_1, R.drawable.cats_2, R.drawable.cats_3, R.drawable.cats_4, R.drawable.cats_5, R.drawable.cats_6, R.drawable.cats_7, R.drawable.cats_8, R.drawable.cats_9};
        catsColor = new int[]{Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
        if (backgroundCardMode == FOALS){
            currentSetOfDesign = foalsRes;
            currentSetOfColors = foalsColor;
        } else if (backgroundCardMode == CARD){
            currentSetOfDesign = playingCardRes;
            currentSetOfColors = playingCardColor;
        } else if (backgroundCardMode == CATS){
            currentSetOfColors = catsColor;
            currentSetOfDesign = catsRes;
        }
    }

    private void setupGame(){
        presenter = new Presenter(this, difficulty, challenge);
        chosenCardIndex = -1;
        jokerCounter = 0;
        displayPlayerCards();
        int remainingCards = presenter.getRemainingCards();
        this.remainingCards.setText("Remaining cards in deck : " + remainingCards);
    }

    private void setUpMp(){
        mpWin = MediaPlayer.create(this, R.raw.win);
        mpWin.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mpWin.release();
            }
        });
        mpLose = MediaPlayer.create(this, R.raw.lose);
        mpLose.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mpLose.release();
            }
        });
        mpJoker = MediaPlayer.create(this, R.raw.joker);
        mpJoker.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mpJoker.release();
            }
        });
    }
}
