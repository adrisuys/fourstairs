package be.adrisuys.thegamecolor.view;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import be.adrisuys.thegamecolor.R;

public class BadgesActivity extends AppCompatActivity {

    private ImageView[] superEasyBadges;
    private TextView[] superEasyText;
    private int[] superEasyRes;
    private ImageView[] easyBadges;
    private TextView[] easyText;
    private int[] easyRes;
    private ImageView[] avgBadges;
    private TextView[] avgText;
    private int[] avgRes;
    private ImageView[] hardBadges;
    private TextView[] hardText;
    private int[] hardRes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges);
        setupSuperEasy();
        setupEasy();
        setupAverage();
        setupHard();
    }

    private int getLightBlue(){
        Color color = new Color();
        return color.rgb(183, 194, 228);
    }

    private void setupSuperEasy() {
        ImageView img1 = findViewById(R.id.super_easy_1);
        ImageView img5 = findViewById(R.id.super_easy_5);
        ImageView img10 = findViewById(R.id.super_easy_10);
        ImageView img15 = findViewById(R.id.super_easy_15);
        ImageView img20 = findViewById(R.id.super_easy_20);
        ImageView imgHs49 = findViewById(R.id.super_easy_hs_49);
        ImageView imgHs = findViewById(R.id.super_easy_hs_10);
        superEasyBadges = new ImageView[]{img1, img5, img10, img15, img20, imgHs, imgHs49};
        TextView tv1 = findViewById(R.id.super_easy_1_text);
        TextView tv5 = findViewById(R.id.super_easy_5_text);
        TextView tv10 = findViewById(R.id.super_easy_10_text);
        TextView tv15 = findViewById(R.id.super_easy_15_text);
        TextView tv20 = findViewById(R.id.super_easy_20_text);
        TextView tvHs = findViewById(R.id.super_easy_hs_10_text);
        TextView tvHs49 = findViewById(R.id.super_easy_hs_49_text);
        superEasyText = new TextView[]{tv1, tv5, tv10, tv15, tv20, tvHs, tvHs49};
        superEasyRes = new int[]{R.drawable.super_easy_1, R.drawable.super_easy_5, R.drawable.super_easy_10, R.drawable.super_easy_15, R.drawable.super_easy_20, R.drawable.super_easy_hs_10, R.drawable.super_easy_hs_49};
        displayModeBadges("win_super_easy", "hs_super_easy", superEasyBadges, superEasyText, superEasyRes);
    }

    private void setupEasy() {
        ImageView img1 = findViewById(R.id.easy_1);
        ImageView img5 = findViewById(R.id.easy_5);
        ImageView img10 = findViewById(R.id.easy_10);
        ImageView img15 = findViewById(R.id.easy_15);
        ImageView img20 = findViewById(R.id.easy_20);
        ImageView imgHs = findViewById(R.id.easy_hs_10);
        ImageView imgHs49 = findViewById(R.id.easy_hs_49);
        easyBadges = new ImageView[]{img1, img5, img10, img15, img20, imgHs, imgHs49};
        TextView tv1 = findViewById(R.id.easy_1_text);
        TextView tv5 = findViewById(R.id.easy_5_text);
        TextView tv10 = findViewById(R.id.easy_10_text);
        TextView tv15 = findViewById(R.id.easy_15_text);
        TextView tv20 = findViewById(R.id.easy_20_text);
        TextView tvHs = findViewById(R.id.easy_hs_10_text);
        TextView tvHs49 = findViewById(R.id.easy_hs_49_text);
        easyText = new TextView[]{tv1, tv5, tv10, tv15, tv20, tvHs, tvHs49};
        easyRes = new int[]{R.drawable.easy_1, R.drawable.easy_5, R.drawable.easy_10, R.drawable.easy_15, R.drawable.easy_20, R.drawable.easy_hs_10, R.drawable.easy_hs_49};
        displayModeBadges("win_easy", "hs_easy", easyBadges, easyText, easyRes);
    }

    private void setupAverage() {
        ImageView img1 = findViewById(R.id.avg_1);
        ImageView img5 = findViewById(R.id.avg_5);
        ImageView img10 = findViewById(R.id.avg_10);
        ImageView img15 = findViewById(R.id.avg_15);
        ImageView img20 = findViewById(R.id.avg_20);
        ImageView imgHs = findViewById(R.id.avg_hs_10);
        ImageView imgHs49 = findViewById(R.id.avg_hs_49);
        avgBadges = new ImageView[]{img1, img5, img10, img15, img20, imgHs, imgHs49};
        TextView tv1 = findViewById(R.id.avg_1_text);
        TextView tv5 = findViewById(R.id.avg_5_text);
        TextView tv10 = findViewById(R.id.avg_10_text);
        TextView tv15 = findViewById(R.id.avg_15_text);
        TextView tv20 = findViewById(R.id.avg_20_text);
        TextView tvHs = findViewById(R.id.avg_hs_10_text);
        TextView tvHs49 = findViewById(R.id.avg_hs_49_text);
        avgText = new TextView[]{tv1, tv5, tv10, tv15, tv20, tvHs, tvHs49};
        avgRes = new int[]{R.drawable.avg_1, R.drawable.avg_5, R.drawable.avg_10, R.drawable.avg_15, R.drawable.avg_20, R.drawable.avg_hs_10, R.drawable.avg_hs_49};
        displayModeBadges("win_avg", "hs_avg", avgBadges, avgText, avgRes);
    }

    private void setupHard() {
        ImageView img1 = findViewById(R.id.hard_1);
        ImageView img5 = findViewById(R.id.hard_5);
        ImageView img10 = findViewById(R.id.hard_10);
        ImageView img15 = findViewById(R.id.hard_15);
        ImageView img20 = findViewById(R.id.hard_20);
        ImageView imgHs = findViewById(R.id.hard_hs_10);
        ImageView imgHs49 = findViewById(R.id.hard_hs_49);
        hardBadges = new ImageView[]{img1, img5, img10, img15, img20, imgHs, imgHs49};
        TextView tv1 = findViewById(R.id.hard_1_text);
        TextView tv5 = findViewById(R.id.hard_5_text);
        TextView tv10 = findViewById(R.id.hard_10_text);
        TextView tv15 = findViewById(R.id.hard_15_text);
        TextView tv20 = findViewById(R.id.hard_20_text);
        TextView tvHs = findViewById(R.id.hard_hs_10_text);
        TextView tvHs49 = findViewById(R.id.hard_hs_49_text);
        hardText = new TextView[]{tv1, tv5, tv10, tv15, tv20, tvHs, tvHs49};
        hardRes = new int[]{R.drawable.hard_1, R.drawable.hard_5, R.drawable.hard_10, R.drawable.hard_15, R.drawable.hard_20, R.drawable.hard_hs_10, R.drawable.hard_hs_49};
        displayModeBadges("win_hard", "hs_hard", hardBadges, hardText, hardRes);
    }

    private void displayModeBadges(String winTxt, String hsText, ImageView[] badges, TextView[] texts, int[] drawables) {
        int nbWin = getIntent().getIntExtra(winTxt, 0);
        for (int i = 0; i < 5; i++){
            int tmp;
            if (i == 0) {
                tmp = 1;
            } else {
                tmp = i * 5;
            }
            if (nbWin >= tmp){
                badges[i].setBackgroundResource(drawables[i]);
                texts[i].setTextColor(Color.BLACK);
            } else {
                badges[i].setBackgroundResource(R.drawable.badge_round_off);
                texts[i].setTextColor(getLightBlue());
            }
        }
        int highscore = getIntent().getIntExtra(hsText, 98);
        if (highscore <= 10){
            badges[5].setBackgroundResource(drawables[5]);
            texts[5].setTextColor(Color.BLACK);
        } else {
            badges[5].setBackgroundResource(R.drawable.badge_macaro_off);
            texts[5].setTextColor(getLightBlue());
        }
        if (highscore <= 49){
            badges[6].setBackgroundResource(drawables[6]);
            texts[6].setTextColor(Color.BLACK);
        } else {
            badges[6].setBackgroundResource(R.drawable.badge_macaro_off);
            texts[6].setTextColor(getLightBlue());
        }
    }

}
