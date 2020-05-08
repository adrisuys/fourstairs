package be.adrisuys.thegamecolor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewInterface {

    private LinearLayout[] containers;
    private Button[] cards;
    private LinearLayout[] middleContainers;
    private Button[] middleCards;
    private TextView remainingCards;
    private LinearLayout jokersLayout;
    private ImageView jokerOne, jokerTwo, jokerThree;

    private Presenter presenter;
    private int chosenCardIndex;
    private int difficulty;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getApplicationContext().getSharedPreferences("4stairs", MODE_PRIVATE);
        editor = sp.edit();
        difficulty = getIntent().getIntExtra("difficulty", 2);
        setupLinearLayouts();
        setupButton();
        setUpMiddleElement();
        presenter = new Presenter(this, difficulty);
        chosenCardIndex = -1;
        displayPlayerCards();
        int remainingCards = presenter.getRemainingCards();
        this.remainingCards.setText("Remaining cards in deck : " + remainingCards);
    }

    public void onSelectCard(View view){
        if (chosenCardIndex != -1){
            containers[chosenCardIndex].setBackgroundColor(Color.TRANSPARENT);
        }
        chosenCardIndex = Integer.parseInt(view.getTag().toString());
        containers[chosenCardIndex].setBackgroundColor(generateColor(85, 255, 0));
        presenter.onPlayerCardSelected(chosenCardIndex);
    }

    public void onSelectCluster(View view) {
        containers[chosenCardIndex].setBackgroundColor(Color.TRANSPARENT);
        int clusterIndex = Integer.parseInt(view.getTag().toString());
        presenter.play(chosenCardIndex, clusterIndex);
    }

    public void onDrawCard(View view){
        presenter.drawCard();
    }

    public void onJokerUsed(View v){
        if (presenter.isJokerActive()){
            Toast.makeText(this, "A joker is already active", Toast.LENGTH_LONG).show();
            return;
        }
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
        Toast.makeText(this, "You cannot play that card on that pile !", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onValidPlay() {
        hideClusterHighlights();
        displayPlayerCards();
        displayMiddlePiles();
        int remainingCards = presenter.getRemainingCards();
        this.remainingCards.setText("Remaining cards in deck : " + remainingCards);
    }

    @Override
    public void displayWin() {
        saveHS(0);
        showDialog(true, -1);
    }

    @Override
    public void displayLoss() {
        int currentHS = getCurrentHS();
        if (presenter.getNonPlayedCards() < currentHS){
            saveHS(presenter.getNonPlayedCards());
            showDialog(false, presenter.getNonPlayedCards());
        }
        showDialog(false, -1);
    }

    @Override
    public void displayCannotDraw() {
        Toast.makeText(this, "You have to play at least " + difficulty + " cards to draw new ones !", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showValidClusters(List<Integer> validClustersIndex) {
        hideClusterHighlights();
        for (int i : validClustersIndex){
            middleContainers[i].setBackgroundColor(generateColor(255,0,255));
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
            switch (integer / 10){
                case 0:
                    card.setBackgroundResource(R.drawable.zeros);
                    card.setTextColor(Color.BLUE);
                    break;
                case 1:
                    card.setBackgroundResource(R.drawable.tens);
                    card.setTextColor(Color.BLACK);
                    break;
                case 2:
                    card.setBackgroundResource(R.drawable.twenties);
                    card.setTextColor(Color.WHITE);
                    break;
                case 3:
                    card.setBackgroundResource(R.drawable.thirties);
                    card.setTextColor(Color.WHITE);
                    break;
                case 4:
                    card.setBackgroundResource(R.drawable.forties);
                    card.setTextColor(Color.RED);
                    break;
                case 5:
                    card.setBackgroundResource(R.drawable.fifties);
                    card.setTextColor(Color.WHITE);
                    break;
                case 6:
                    card.setBackgroundResource(R.drawable.sixties);
                    card.setTextColor(Color.WHITE);
                    break;
                case 7:
                    card.setBackgroundResource(R.drawable.seventies);
                    card.setTextColor(Color.BLACK);
                    break;
                case 8:
                    card.setBackgroundResource(R.drawable.eighties);
                    card.setTextColor(Color.WHITE);
                    break;
                case 9:
                    card.setBackgroundResource(R.drawable.nineties);
                    card.setTextColor(Color.WHITE);
                    break;
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
        if (difficulty == 2){
            return sp.getInt("hs_easy", 98);
        } else if (difficulty == 3){
            return sp.getInt("hs_avg", 98);
        } else {
            return sp.getInt("hs_hard", 98);
        }
    }

    private void showDialog(boolean won, int hs){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        TextView title = dialog.findViewById(R.id.title);
        TextView message = dialog.findViewById(R.id.message);
        ImageView icon = dialog.findViewById(R.id.icon);
        TextView button = dialog.findViewById(R.id.btn);
        title.setText(won ? "Congrats !" : "Too bad..");
        String msg = won ? "You won !" : "No move possible.";
        if (hs != -1 && !won){
            msg += "\nNew highscore : " + hs;
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
        if (difficulty == 1){
          editor.putInt("hs_super_easy", newScore);
        } else if (difficulty == 2){
            editor.putInt("hs_easy", newScore);
        } else if (difficulty == 3){
            editor.putInt("hs_avg", newScore);
        } else {
            editor.putInt("hs_hard", newScore);
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
        joker.setBackgroundResource(0);
        joker.setEnabled(false);
        presenter.activateJoker();
        for (LinearLayout ll : middleContainers){
            ll.setBackgroundColor(generateColor(255,0,255));
        }
    }
}
