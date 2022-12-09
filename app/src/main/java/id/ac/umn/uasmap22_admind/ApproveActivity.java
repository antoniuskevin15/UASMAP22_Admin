package id.ac.umn.uasmap22_admind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.squareup.picasso.Picasso;


import org.w3c.dom.Text;

public class ApproveActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Data Order
    String company, user, photo, ruang, date, status, time;
    int harga;

    // Data Company
    String photoCompany;

    // Data User
    String namaUser, telephone, email;

    // Layout
    TextView penyewa;
    TextView tanggalSewa;
    TextView jamSewa;
    TextView ruangan;
    TextView totalBayar;
    TextView btnApprove;
    TextView btnDecline;
    ImageView imgBukti;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve);


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        ImageView backBtn = findViewById(R.id.backbutton);
        backBtn.setOnClickListener(view -> finish());

        Intent curr = getIntent();

//        Order currOrder = (Order) curr.getSerializableExtra("ORDER");
        String id = curr.getStringExtra("ORDER-ID");

        penyewa = (TextView) findViewById(R.id.penyewa);
        tanggalSewa = (TextView) findViewById(R.id.tanggalSewa);
        jamSewa = (TextView) findViewById(R.id.jamSewa);
        ruangan = (TextView) findViewById(R.id.ruangan);
        totalBayar = (TextView) findViewById(R.id.totalBayar);

        btnApprove = (TextView) findViewById(R.id.approveBtn);
        btnDecline = (TextView) findViewById(R.id.declineBtn);

        imgBukti = (ImageView) findViewById(R.id.imgBukti);

        // Collect data from DB (Firestore)
        DocumentReference docRef = db.collection("order").document(id);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    company = doc.getString("company");
                    user = doc.getString("user");
                    long hargaData = (long) doc.get("harga");
                    photo = doc.getString("photo");
                    ruang = doc.getString("ruang");
                    date = doc.getString("date");
                    status = doc.getString("status");
                    time = doc.getString("time");
                    DocumentReference companyRef = db.collection("partner").document(company);
                    companyRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot doc = task.getResult();
                                photoCompany = doc.getString("photoURL");
                                DocumentReference userRef = db.collection("user").document(user);
                                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot doc = task.getResult();
                                            namaUser = doc.getString("nama");
                                            telephone = doc.getString("telephone");
                                            email = doc.getString("email");
                                            harga = (int) hargaData;

                                            penyewa.setText(namaUser);
                                            tanggalSewa.setText(date);
                                            jamSewa.setText(time);
                                            ruangan.setText(ruang);
                                            totalBayar.setText(String.valueOf(harga));
                                            Picasso.get().load(photo).into(imgBukti);
                                        }
                                    }
                                });
                            }
                        }
                    });


                }
            }
        });

        btnApprove.setOnClickListener(v-> {
            docRef.update("status", "Approved");
            finish();
        });

        btnDecline.setOnClickListener(v -> {
            docRef.update("status", "Declined");
            finish();
        });


    }
}