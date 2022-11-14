package id.ac.umn.uasmap22_admind;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        ImageView backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(view -> finish());

        ImageView menuBtn = findViewById(R.id.menu_button);
        menuBtn.setOnClickListener(view -> finish());
    }
}