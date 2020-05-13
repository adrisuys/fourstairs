package be.adrisuys.thegamecolor.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import be.adrisuys.thegamecolor.R;

public class ChallengeActivity extends AppCompatActivity {

    private GridView gridView;
    private int lastChallengeDone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);
        gridView = findViewById(R.id.grid_view);
        List<Integer> challenges = generateChallenges();
        lastChallengeDone = getIntent().getIntExtra("lastChallengeDone", 15);
        gridView.setAdapter(new CustomChallengeAdapter(this, challenges, lastChallengeDone));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleOnClick(position);
            }
        });
    }

    private void handleOnClick(int position) {
        int i = (int) gridView.getItemAtPosition(position);
        if (i <= lastChallengeDone + 1){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("challenge", i);
            startActivity(intent);
        }
    }

    private List<Integer> generateChallenges() {
        List<Integer> list = new ArrayList<>();
        for (int i = 16; i < 98; i++){
            list.add(i);
        }
        return list;
    }

    private class CustomChallengeAdapter extends BaseAdapter {

        private List<Integer> listData;
        private LayoutInflater layoutInflater;
        private Context context;
        private int lastDone;

        public CustomChallengeAdapter(Context context, List<Integer> listData, int lastDone){
            this.context = context;
            this.listData = listData;
            this.lastDone = lastDone;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.challenge_grid_item, null);
                holder = new ViewHolder();
                holder.challengeNumber = convertView.findViewById(R.id.challenge_number);
                holder.challengeDone = convertView.findViewById(R.id.challenge_done);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            convertView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 150));
            int challenge = this.listData.get(position);
            holder.challengeNumber.setText(String.valueOf(challenge));
            if (challenge <= lastDone){
                holder.challengeDone.setVisibility(View.VISIBLE);
                holder.challengeNumber.setTextColor(getColorFromRGB(37, 90, 213));
            } else if (challenge - lastDone == 1) {
                holder.challengeDone.setVisibility(View.GONE);
                holder.challengeNumber.setTextColor(getColorFromRGB(37, 90, 213));
            } else {
                holder.challengeDone.setVisibility(View.GONE);
                holder.challengeNumber.setTextColor(getColorFromRGB(177, 177, 177));
            }
            return convertView;
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private int getColorFromRGB(int r, int g, int b){
            Color color = new Color();
            return color.rgb(r, g, b);
        }
    }

    static class ViewHolder {
        TextView challengeNumber;
        LinearLayout challengeDone;
    }

}
