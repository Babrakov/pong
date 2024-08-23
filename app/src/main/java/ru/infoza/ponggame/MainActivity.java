package ru.infoza.ponggame;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private PongView pongView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pongView = new PongView(this, null);
        setContentView(pongView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pongView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pongView.resume();
    }

}