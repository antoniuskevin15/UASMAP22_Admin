package id.ac.umn.uasmap22_admind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView loginBtn = (TextView) findViewById(R.id.button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                out();
                Intent intentHome = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intentHome);
            }
        });
    }

    public void out(){
        this.finish();
    }
}