package id.ac.umn.uasmap22_admind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import id.ac.umn.uasmap22_admind.databinding.ActivityHomeBinding;
import id.ac.umn.uasmap22_admind.databinding.ActivityMainBinding;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.action_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.action_customize:
                    replaceFragment(new CustomizeFragment());
                    break;
                case R.id.action_order:
                    replaceFragment(new OrderFragment());
                    break;
                case R.id.action_history:
                    replaceFragment(new HistoryFragment());
                    break;
                case R.id.action_profile:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}