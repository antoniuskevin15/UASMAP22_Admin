package id.ac.umn.uasmap22_admind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CustomizeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);

        ImageView backButton = (ImageView) findViewById(R.id.backbutton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                out();
                Intent intentHome = new Intent(CustomizeActivity.this, HomeActivity.class);
                startActivity(intentHome);
            }
        });
    }
    public void out(){
        this.finish();
    }
}