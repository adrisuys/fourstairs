package be.adrisuys.thegamecolor.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import be.adrisuys.thegamecolor.R;

public class HomeActivity extends AppCompatActivity {

    private Intent badges;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("4stairs", MODE_PRIVATE);
        TextView highScoreSuperEasyTv = findViewById(R.id.hs_super_easy);
        TextView highScoreEasyTv = findViewById(R.id.hs_easy);
        TextView highScoreAvgTv = findViewById(R.id.hs_avg);
        TextView highScoreHardTv = findViewById(R.id.hs_difficult);
        int highScoreSuperEasy = sp.getInt("hs_super_easy", 98);
        int highScoreEasy = sp.getInt("hs_easy", 98);
        int highScoreAvg = sp.getInt("hs_avg", 98);
        int highScoreHard = sp.getInt("hs_hard", 98);
        highScoreSuperEasyTv.setText(String.valueOf(highScoreSuperEasy));
        highScoreEasyTv.setText(String.valueOf(highScoreEasy));
        highScoreAvgTv.setText(String.valueOf(highScoreAvg));
        highScoreHardTv.setText(String.valueOf(highScoreHard));
        badges = prepareIntent(sp);
    }

    public void newGame(View v){
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("difficulty", Integer.parseInt(v.getTag().toString()));
        startActivity(i);
    }

    public void seeInfo(View view){
        startActivity(new Intent(this, InfoActivity.class));
    }

    public void seeBadges(View view){
        startActivity(badges);
    }

    private Intent prepareIntent(SharedPreferences sp){
        Intent i = new Intent(this, BadgesActivity.class);
        i.putExtra("hs_super_easy", sp.getInt("hs_super_easy", 98));
        i.putExtra("hs_easy", sp.getInt("hs_easy", 98));
        i.putExtra("hs_avg", sp.getInt("hs_avg", 98));
        i.putExtra("hs_hard", sp.getInt("hs_hard", 98));
        i.putExtra("win_super_easy", sp.getInt("win_super_easy", 0));
        i.putExtra("win_easy", sp.getInt("win_easy", 0));
        i.putExtra("win_avg", sp.getInt("win_avg", 0));
        i.putExtra("win_hard", sp.getInt("win_hard", 0));
        return i;
    }
}
