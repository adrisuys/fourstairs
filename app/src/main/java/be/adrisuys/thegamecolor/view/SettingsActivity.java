package be.adrisuys.thegamecolor.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private Spinner spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sp = getApplicationContext().getSharedPreferences("4stairs", MODE_PRIVATE);
        editor = sp.edit();
        mode = sp.getInt("mode", MainActivity.FOALS);
        spinner = findViewById(R.id.spinner);
        setupSpinner();
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
                String item = parent.getItemAtPosition(position).toString();
                if (item.equals("FOALS")){
                    mode = MainActivity.FOALS;
                } else if (item.equals("PLAYING CARDS")){
                    mode = MainActivity.CARD;
                } else if (item.equals("CATS")){
                    mode = MainActivity.CATS;
                }
                saveMode();
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
    }
}
