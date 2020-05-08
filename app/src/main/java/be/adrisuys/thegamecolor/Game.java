package be.adrisuys.thegamecolor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private List<Integer> deck;
    private List<Integer> cards;
    private int[] clusters;
    private Presenter presenter;
    private int nbCardPlayedPerRound;
    private int minCardPlayedPerRound;

    public Game(Presenter presenter, int i){
        clusters = new int[]{-1, -1, -1, -1};
        deck = new ArrayList<>();
        cards = new ArrayList<>();
        this.presenter = presenter;
        generateDeck();
        drawCards();
        nbCardPlayedPerRound = 0;
        minCardPlayedPerRound = i;
    }

    public List<Integer> getCards() {
        return cards;
    }

    public int[] getClusters() {
        return clusters;
    }

    public void play(int chosenCardIndex, int clusterIndex) {
        boolean isChoiceValid;
        if (clusterIndex == 0 || clusterIndex == 1){
            isChoiceValid = canIncrease(chosenCardIndex, clusterIndex);
        } else {
            isChoiceValid = canDecrease(chosenCardIndex, clusterIndex);
        }
        if (isChoiceValid){
            clusters[clusterIndex] = cards.remove(chosenCardIndex);
            nbCardPlayedPerRound++;
            checkForEnd();
        } else {
            presenter.displayWrongCard();
        }
    }

    public int getRemainingCards(){
        return deck.size();
    }

    public boolean isGameBlocked(){
        for (int i = 0; i < cards.size(); i++){
            for (int j = 0; j < clusters.length; j++){
                if (j == 0 || j == 1){
                    if (canIncrease(i, j)) return false;
                } else {
                    if (canDecrease(i, j)) return false;
                }
            }
        }
        return true;
    }

    private boolean canIncrease(int chosenCardIndex, int clusterIndex) {
        if (clusters[clusterIndex] == -1) {
            return true;
        } else if (cards.get(chosenCardIndex) + 10 == clusters[clusterIndex]){
            return true;
        } else {
            if (cards.get(chosenCardIndex) > clusters[clusterIndex]){
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean canDecrease(int chosenCardIndex, int clusterIndex) {
        if (clusters[clusterIndex] == -1){
            return true;
        } else if (cards.get(chosenCardIndex) - 10 == clusters[clusterIndex]){
            return true;
        } else {
            if (cards.get(chosenCardIndex) < clusters[clusterIndex]){
                return true;
            } else {
                return false;
            }
        }
    }

    private void drawCards() {
        for (int i = 0; i < 8; i++){
            cards.add(deck.remove(0));
        }
    }

    void drawCard(){
        if (!deck.isEmpty()){
            if (nbCardPlayedPerRound < minCardPlayedPerRound){
                presenter.displayCannotDraw();
            } else {
                for (int i = 0; i < nbCardPlayedPerRound; i++){
                    cards.add(deck.remove(0));
                }
                nbCardPlayedPerRound = 0;
                presenter.onValidPlay();
            }
        }
    }

    private void generateDeck() {
        for (int i = 2; i < 100; i++){
            deck.add(i);
        }
        Collections.shuffle(deck);
    }

    private void checkForEnd() {
        if (cards.isEmpty() && deck.isEmpty()){
            presenter.displayWin();
            return;
        }
        if (isGameBlocked()){
            presenter.displayLoss();
            return;
        }
        presenter.onValidPlay();
    }

    public int getNonPlayedCards() {
        return deck.size() + cards.size();
    }
}
