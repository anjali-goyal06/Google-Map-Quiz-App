package com.example.knowtheword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button startButton = findViewById(R.id.startButton);
        EditText numOfQues = findViewById(R.id.numOfQues);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int num = Integer.parseInt(numOfQues.getText().toString());

                    if(num<=0 || num>30){
                        numOfQues.setText("");
                        Toast.makeText(MainActivity.this,"Please enter any number from 1 to 30 ",Toast.LENGTH_SHORT).show();
                    }


                    Intent intent = new Intent(getApplicationContext(),GuessTheLocation.class);
                    intent.putExtra("Number",num);
                    startActivity(intent);

                }catch (Exception e){
                    numOfQues.setText("");
                    Toast.makeText(MainActivity.this,"Please enter valid number ",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}