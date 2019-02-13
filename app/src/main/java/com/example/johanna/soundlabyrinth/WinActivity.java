package com.example.johanna.soundlabyrinth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WinActivity extends AppCompatActivity {

    private Button button_startagain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        button_startagain = findViewById(R.id.button_tostart);

        button_startagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toStart = new Intent(WinActivity.this, StartActivity.class);
                startActivity(toStart);
            }
        });

    }
}
