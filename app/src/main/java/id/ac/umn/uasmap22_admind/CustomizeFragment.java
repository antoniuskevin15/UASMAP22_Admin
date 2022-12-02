package id.ac.umn.uasmap22_admind;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomizeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomizeFragment extends Fragment {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomizeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomizeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomizeFragment newInstance(String param1, String param2) {
        CustomizeFragment fragment = new CustomizeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (user != null) {
            String uid = user.getUid();
            getUser(uid);
        } else {
            // No user is signed in
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customize, container, false);
    }

    private int mHour, mMinute;
    private final LinkedList<Ruangan> mRuangan = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private RuanganAdapter mAdapter;


    public void getUser(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ruangRef = db
                .collection("partner").document(uid)
                .collection("ruang");

        db.collection("partner").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        TextView tv_jumlah = (TextView) getView().findViewById(R.id.custom_et_jumlahLap);
                        ruangRef.count().get(AggregateSource.SERVER).addOnCompleteListener(tasks -> {
                            if (task.isSuccessful()) {
                                AggregateQuerySnapshot snapshot = tasks.getResult();
                                tv_jumlah.setText(""+snapshot.getCount());
                                Log.d("TAG", "Count: " + snapshot.getCount());
                            } else {
                                Log.d("TAG", "Count failed: ", tasks.getException());
                            }
                        });

                        ruangRef.get().addOnCompleteListener(datas -> {
                           if(datas.isSuccessful()){
                               datas.getResult().forEach(data -> {
                                   mRuangan.add(new Ruangan(data.getString("nama"), Integer.valueOf(data.get("harga").toString())));
                               });

                               mRecyclerView = (RecyclerView) getView().findViewById(R.id.recyclerview);
                               mAdapter = new RuanganAdapter(getContext(), mRuangan);
                               mRecyclerView.setAdapter(mAdapter);
                               mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                           }
                        });

                        Spinner spinner = getView().findViewById(R.id.customize_hariBuka);
                        Spinner spinnerTutup = getView().findViewById(R.id.customize_hariTutup);

                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.hari_array, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                String text = adapterView.getItemAtPosition(i).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        spinnerTutup.setAdapter(adapter);
                        spinnerTutup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                String text = adapterView.getItemAtPosition(i).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        TextView btnTimeOpenPicker=(TextView) getView().findViewById(R.id.in_time_open);
                        TextView btnTimeClosePicker=(TextView) getView().findViewById(R.id.in_time_close);
                        TextView et_jamBuka=(TextView) getView().findViewById(R.id.in_time_open);
                        et_jamBuka.setText(document.getString("jamBuka"));
                        TextView et_jamTutup=(TextView) getView().findViewById(R.id.in_time_close);
                        et_jamTutup.setText(document.getString("jamTutup"));

                        btnTimeOpenPicker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Calendar c = Calendar.getInstance();
                                mHour = c.get(Calendar.HOUR_OF_DAY);
                                mMinute = c.get(Calendar.MINUTE);

                                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                        new TimePickerDialog.OnTimeSetListener() {

                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                                  int minute) {

                                                et_jamBuka.setText(hourOfDay + ":" + minute);
                                            }
                                        }, mHour, mMinute, false);
                                timePickerDialog.show();
                            }
                        });

                        btnTimeClosePicker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Calendar c = Calendar.getInstance();
                                mHour = c.get(Calendar.HOUR_OF_DAY);
                                mMinute = c.get(Calendar.MINUTE);

                                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                        new TimePickerDialog.OnTimeSetListener() {

                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                                  int minute) {

                                                et_jamTutup.setText(hourOfDay + ":" + minute);
                                            }
                                        }, mHour, mMinute, false);
                                timePickerDialog.show();
                            }
                        });

                        TextView btnSave = (TextView) getView().findViewById(R.id.customize_save);
                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Map<String, Object> partner = new HashMap<>();
//
                                if(TextUtils.isEmpty(spinner.getSelectedItem().toString()) || TextUtils.isEmpty(spinnerTutup.getSelectedItem().toString()) || TextUtils.isEmpty(et_jamBuka.getText()) || TextUtils.isEmpty(et_jamTutup.getText())) {
                                    Toast.makeText(getContext(), "Masih ada imput yang kosong!!", Toast.LENGTH_SHORT).show();
                                }else{
                                    partner.put("hariBuka", spinner.getSelectedItem().toString());
                                    partner.put("jamBuka", et_jamBuka.getText().toString());
                                    partner.put("hariTutup", spinnerTutup.getSelectedItem().toString());
                                    partner.put("jamTutup", et_jamTutup.getText().toString());
                                    partner.put("alamat", document.getString("alamat"));
                                    partner.put("fasilitas", document.getString("fasilitas"));
                                    partner.put("kategori", document.getString("kategori"));
                                    partner.put("nama", document.getString("nama"));
                                    partner.put("phone", document.getString("phone"));
                                    partner.put("photoName", document.getString("photoName"));
                                    partner.put("photoURL", document.getString("photoURL"));

                                    db.collection("partner").document(uid)
                                            .set(partner)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getContext(), "Your information is successfully changed!", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("TAG", "Error writing document", e);
                                                }
                                            });
                                }
                            }
                        });
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }
}