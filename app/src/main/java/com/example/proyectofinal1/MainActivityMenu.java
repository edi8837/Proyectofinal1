package com.example.proyectofinal1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivityMenu extends AppCompatActivity {

Button dep;
    Button hog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        dep = (Button) findViewById(R.id.btnDepp);
        hog = (Button) findViewById(R.id.btnHog);
        dep.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivityMenu.this,MainActivity.class);
                startActivity(i);
            }
        });
        hog.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivityMenu.this,Hog.class);
                startActivity(i);
            }
        });


    }


}