package com.example.knowtheword;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScoreSheet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_sheet);

        try {
            TextView scoreShow = findViewById(R.id.score_show);

            Intent intent = getIntent();
            String score = intent.getStringExtra("Score");
            scoreShow.setText(score);
        }catch (Exception e){
            Log.e("error", "onCreate: "+e.getMessage() );
        }
        Button tryAgain = findViewById(R.id.tryAgain);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}