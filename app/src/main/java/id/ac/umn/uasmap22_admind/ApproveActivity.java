package id.ac.umn.uasmap22_admind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ApproveActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Data Order
    String company, user, harga, photo, ruang, date, status, time;

    // Data Company
    String photoCompany;

    // Data User
    String namaUser, telephone, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve);
        Intent curr = getIntent();
        Order currOrder = (Order) curr.getSerializableExtra("ORDER");
        String id = currOrder.getId();
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
                    harga = doc.getString("harga");
                    photo = doc.getString("photo");
                    ruang = doc.getString("ruang");
                    date = doc.getString("date");
                    status = doc.getString("status");
                    time = doc.getString("time");
                }
            }
        });

        DocumentReference companyRef = db.collection("partner").document(company);
        companyRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    photoCompany = doc.getString("photoURL");
                }
            }
        });
        DocumentSnapshot user = currOrder.getUser();
        namaUser = user.getString("nama");
        telephone = user.getString("telephone");
        email = user.getString("email");
    }
}