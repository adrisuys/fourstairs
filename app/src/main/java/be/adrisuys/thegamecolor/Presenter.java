package be.adrisuys.thegamecolor;

import android.view.View;

import java.util.List;

public class Presenter {

    private Game game;
    private ViewInterface view;

    public Presenter(ViewInterface view, int difficulty){
        this.view = view;
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
}
