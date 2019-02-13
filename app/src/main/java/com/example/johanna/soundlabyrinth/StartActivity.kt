package com.example.johanna.soundlabyrinth

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.johanna.soundlabyrinth.game.Mode


class StartActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val button_random = findViewById<Button>(R.id.startButton_random)
        val button_map = findViewById<Button>(R.id.startButton_map)
        val button_straight = findViewById<Button>(R.id.startButton_straight)



        button_random.setOnClickListener {
                val intent = Intent(this@StartActivity, LabyrinthActivity::class.java)
                intent.putExtra("mode", Mode.RANDOM)
                startActivity(intent)
        }

        button_map.setOnClickListener {
            val intent = Intent(this@StartActivity, LabyrinthActivity::class.java)
            intent.putExtra("mode", Mode.MAP)
            startActivity(intent)
        }

        button_straight.setOnClickListener {
            val intent = Intent(this@StartActivity, LabyrinthActivity::class.java)
            intent.putExtra("mode", Mode.STRAIGHT)
            startActivity(intent)
        }

    }

}
