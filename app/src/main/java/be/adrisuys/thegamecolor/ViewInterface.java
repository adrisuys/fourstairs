package be.adrisuys.thegamecolor;

import java.util.List;

public interface ViewInterface {
    void displayWrongCard();

    void onValidPlay();

    void displayWin();

    void displayLoss();

    void displayCannotDraw();

    void showValidClusters(List<Integer> validClustersIndex);
}
