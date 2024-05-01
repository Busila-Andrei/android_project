package com.example.frontend;


import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Referințe la butoane
        Button btn1 = findViewById(R.id.button1);
        Button btn2 = findViewById(R.id.button2);
        Button btn3 = findViewById(R.id.button3);
        Button btn4 = findViewById(R.id.button4);
        Button btn5 = findViewById(R.id.button5);

        // Listeneri pentru butoane
        btn1.setOnClickListener(v -> loadFragment(new SectionsFragment()));
//        btn2.setOnClickListener(v -> loadFragment(new Fragment2()));
//        btn3.setOnClickListener(v -> loadFragment(new Fragment3()));
//        btn4.setOnClickListener(v -> loadFragment(new Fragment4()));
//        btn5.setOnClickListener(v -> loadFragment(new Fragment5()));
//
//        // Încarcă implicit primul fragment
//        if (savedInstanceState == null) {
//            loadFragment(new Fragment1());
//        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }
}