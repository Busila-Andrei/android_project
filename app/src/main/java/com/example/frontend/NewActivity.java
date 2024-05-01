package com.example.frontend;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Termină activitatea actuală și revine la activitatea anterioară
                finish();
            }
        });
    }
}
