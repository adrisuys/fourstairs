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
    private boolean isJokerActivated;

    public Game(Presenter presenter, int i, int challenge){
        clusters = new int[]{-1, -1, -1, -1};
        deck = new ArrayList<>();
        cards = new ArrayList<>();
        this.presenter = presenter;
        if (challenge == -1) {
            generateDeck(98);
        } else {
            generateDeck(challenge);
        }
        drawCards();
        nbCardPlayedPerRound = 0;
        minCardPlayedPerRound = i;
        isJokerActivated = false;
    }

    public List<Integer> getCards() {
        return cards;
    }

    public int[] getClusters() {
        return clusters;
    }

    public void play(int chosenCardIndex, int clusterIndex) {
        if (chosenCardIndex >= cards.size()) return;
        boolean isChoiceValid = checkForValidation(chosenCardIndex, clusterIndex);
        if (isChoiceValid){
            clusters[clusterIndex] = cards.remove(chosenCardIndex);
            isJokerActivated = false;
            nbCardPlayedPerRound++;
            presenter.onValidPlay();
        } else {
            presenter.displayWrongCard();
        }
    }

    public int getNonPlayedCards() {
        return deck.size() + cards.size();
    }

    public int getRemainingCards(){
        return deck.size();
    }

    public List<Integer> getValidClusters(int chosenCardIndex){
        List<Integer> valids = new ArrayList<>();
        if (chosenCardIndex >= cards.size()){
            return null;
        }
        for (int i = 0; i < clusters.length; i++){
            if (checkForValidation(chosenCardIndex, i)){
                valids.add(i);
            }
        }
        return valids;
    }

    public void activateJoker() {
        isJokerActivated = true;
    }

    public boolean isJokerActive() {
        return isJokerActivated;
    }

    public void drawCard(){
        if (!deck.isEmpty()){
            if (nbCardPlayedPerRound < minCardPlayedPerRound){
                presenter.displayCannotDraw();
            } else {
                for (int i = 0; i < nbCardPlayedPerRound; i++){
                    if (!deck.isEmpty()){
                        cards.add(deck.remove(0));
                    }
                }
                nbCardPlayedPerRound = 0;
                presenter.updateUI();
            }
        }
    }

    public void checkForEnd(boolean jokerAvailable) {
        if (cards.isEmpty() && deck.isEmpty()){
            presenter.displayWin();
            return;
        }
        if (jokerAvailable){
            return;
        }
        if (isGameBlocked()){
            presenter.displayLoss();
            return;
        }
    }

    private boolean isGameBlocked() {
        boolean canStillPlay = checkIfPlayerCardUseful();
        if (canStillPlay){
            return false;
        } else {
            if (cards.size() == 8){
                // il ne peut pas piocher car il a déjà 8 cartes donc c'est mort
                return true;
            } else {
                if (nbCardPlayedPerRound >= minCardPlayedPerRound){
                    // il peut piocher
                    if (deck.isEmpty()){
                        // le deck est vide
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    // il ne peut pas piocher car il doit encore jouer X cartes
                    return true;
                }
            }
        }
    }

    private boolean checkIfPlayerCardUseful() {
        for (int i = 0; i < cards.size(); i++){
            for (int j = 0; j < clusters.length; j++){
                if (j == 0 || j == 1){
                    if (canIncrease(i, j)) return true;
                } else {
                    if (canDecrease(i, j)) return true;
                }
            }
        }
        return false;
    }

    private boolean canIncrease(int chosenCardIndex, int clusterIndex) {
        if (clusters[clusterIndex] == -1) {
            return true;
        } else if (cards.get(chosenCardIndex) + 10 == clusters[clusterIndex]){
            return true;
        } else {
            return cards.get(chosenCardIndex) > clusters[clusterIndex];
        }
    }

    private boolean canDecrease(int chosenCardIndex, int clusterIndex) {
        if (clusters[clusterIndex] == -1){
            return true;
        } else if (cards.get(chosenCardIndex) - 10 == clusters[clusterIndex]){
            return true;
        } else {
            return cards.get(chosenCardIndex) < clusters[clusterIndex];
        }
    }

    private void drawCards() {
        for (int i = 0; i < 8; i++){
            cards.add(deck.remove(0));
        }
    }

    private void generateDeck(int max) {
        for (int i = 2; i < max + 2; i++){
            deck.add(i);
        }
        Collections.shuffle(deck);
    }

    private boolean checkForValidation(int chosenCardIndex, int clusterIndex) {
        if (isJokerActivated){
            return true;
        }
        boolean isChoiceValid;
        if (clusterIndex == 0 || clusterIndex == 1){
            isChoiceValid = canIncrease(chosenCardIndex, clusterIndex);
        } else {
            isChoiceValid = canDecrease(chosenCardIndex, clusterIndex);
        }
        return isChoiceValid;
    }

}
