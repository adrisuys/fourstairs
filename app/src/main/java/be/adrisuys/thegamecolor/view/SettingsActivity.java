package be.adrisuys.thegamecolor.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import be.adrisuys.thegamecolor.R;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private int mode;
    private boolean soundOn;
    private Spinner spinner;
    private ImageView soundIcon;

    private boolean isInit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sp = getApplicationContext().getSharedPreferences("4stairs", MODE_PRIVATE);
        editor = sp.edit();
        mode = sp.getInt("mode", MainActivity.FOALS);
        soundOn = sp.getBoolean("sound", false);
        spinner = findViewById(R.id.spinner);
        soundIcon = findViewById(R.id.sound);
        isInit = false;
        setupSpinner();
        setupSoundIcon();
    }

    public void onSoundChanged(View v){
        soundOn = !soundOn;
        saveSound();
        setupSoundIcon();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mode == MainActivity.FOALS){
            spinner.setSelection(0);
        } else if (mode == MainActivity.CARD){
            spinner.setSelection(1);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
    }

    private void setupSoundIcon(){
        if (soundOn){
            soundIcon.setBackgroundResource(R.drawable.ic_volume_up_black_24dp);
        } else {
            soundIcon.setBackgroundResource(R.drawable.ic_volume_off_black_24dp);
        }
    }

    private void setupSpinner() {
        List<String> categories = new ArrayList<>();
        categories.add("FOALS");
        categories.add("PLAYING CARDS");
        categories.add("CATS");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isInit){
                    String item = parent.getItemAtPosition(position).toString();
                    if (item.equals("FOALS")){
                        mode = MainActivity.FOALS;
                    } else if (item.equals("PLAYING CARDS")){
                        mode = MainActivity.CARD;
                    } else if (item.equals("CATS")){
                        mode = MainActivity.CATS;
                    }
                    saveMode();
                } else {
                    isInit = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void saveMode(){
        editor.putInt("mode", mode);
        editor.commit();
    }

    private void saveSound(){
        editor.putBoolean("sound", soundOn);
        editor.commit();
    }

}
