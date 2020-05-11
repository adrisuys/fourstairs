package be.adrisuys.thegamecolor;

import java.util.List;

import be.adrisuys.thegamecolor.view.ViewInterface;

public class Presenter {

    private Game game;
    private ViewInterface view;

    public Presenter(ViewInterface view, int difficulty){
        this.view = view;
        if (difficulty == 1) difficulty = 2;
        game = new Game(this, difficulty);
    }

    public List<Integer> getCards(){
        return game.getCards();
    }

    public int[] getClusters(){
        return game.getClusters();
    }

    public void play(int chosenCardIndex, int clusterIndex) {
        game.play(chosenCardIndex, clusterIndex);
    }

    public void onValidPlay() {
        view.onValidPlay();
    }

    public void displayWrongCard() {
        view.displayWrongCard();
    }

    public int getRemainingCards(){
        return game.getRemainingCards();
    }

    public int getNonPlayedCards(){
        return game.getNonPlayedCards();
    }

    public void displayWin() {
        view.displayWin();
    }

    public void displayLoss() {
        view.displayLoss();
    }

    public void displayCannotDraw() {
        view.displayCannotDraw();
    }

    public void drawCard() {
        game.drawCard();
    }

    public void onPlayerCardSelected(int chosenCardIndex) {
        List<Integer> validClustersIndex = game.getValidClusters(chosenCardIndex);
        view.showValidClusters(validClustersIndex);
    }

    public void activateJoker() {
        game.activateJoker();
    }

    public boolean isJokerActive(){
        return game.isJokerActive();
    }

    public void updateUI() {
        view.updateUI();
    }

    public void checkForEnd(boolean jokerAvailable) {
        game.checkForEnd(jokerAvailable);
    }
}
