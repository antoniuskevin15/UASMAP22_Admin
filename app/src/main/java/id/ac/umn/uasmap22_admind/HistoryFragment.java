package id.ac.umn.uasmap22_admind;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
            getOrder(uid);
        } else {
            // No user is signed in
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    private final LinkedList<Order> mOrder = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private HistoryAdapter mAdapter;
    private DocumentSnapshot docUser;
    private String filterRuang = "Ruangan";
    private String filterTanggal = "Tanggal";

    public void getOrder(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String timeStamp = new SimpleDateFormat("dd MMMM yyyy").format(Calendar.getInstance().getTime());
        Log.d("DATE", timeStamp);
        Query orderRef = db
                .collection("order").whereLessThan("date", timeStamp);

        ArrayList<String> spinnerArray = new ArrayList<String>();
        ArrayList<String> spinnerArrayTanggal = new ArrayList<String>();
        CollectionReference ruangRef = db
                .collection("partner").document(uid)
                .collection("ruang");

        ruangRef.get().addOnCompleteListener(datas -> {
            if(datas.isSuccessful()){
                spinnerArray.add("Ruangan");
                datas.getResult().forEach(data -> {
                    spinnerArray.add(data.getString("nama"));
                });
            }
        });
        db.collection("order").whereLessThan("date", timeStamp).orderBy("date").get().addOnCompleteListener(datas -> {
            if(datas.isSuccessful()){
                spinnerArrayTanggal.add("Tanggal");
                datas.getResult().forEach(data -> {
                    if(spinnerArrayTanggal.contains(data.getString("date"))){

                    }else{
                        spinnerArrayTanggal.add(data.getString("date"));
                    }
                });
            }
        });
        orderRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            Spinner spinnerRuang = getView().findViewById(R.id.filter_ruang);

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerRuang.setAdapter(adapter);
                            spinnerRuang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    filterRuang = adapterView.getItemAtPosition(i).toString();
                                    mOrder.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String docRefUser = document.get("user").toString();

                                        db.collection("user").document(docRefUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> taskUser) {
                                                docUser = taskUser.getResult();
                                                if(filterTanggal.equals("Tanggal")){
                                                    if(filterRuang.equals("Ruangan")){
                                                        if(document.getString("company").equals(uid)){
                                                            mOrder.add(new Order(document.get("ruang").toString(), document.get("harga").toString(), document.getString("date"),  document.getString("time"), docUser));
                                                            mRecyclerView = (RecyclerView) getView().findViewById(R.id.history_recycler);
                                                            mAdapter = new HistoryAdapter(getContext(), mOrder);
                                                            mRecyclerView.setAdapter(mAdapter);
                                                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                                        }
                                                    }else{
                                                        if(document.getString("company").equals(uid) && document.getString("ruang").equals(filterRuang)){
                                                            mOrder.add(new Order(document.get("ruang").toString(), document.get("harga").toString(), document.getString("date"),  document.getString("time"), docUser));
                                                            mRecyclerView = (RecyclerView) getView().findViewById(R.id.history_recycler);
                                                            mAdapter = new HistoryAdapter(getContext(), mOrder);
                                                            mRecyclerView.setAdapter(mAdapter);
                                                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                                        }
                                                    }
                                                }else {
                                                    if (filterRuang.equals("Ruangan")) {
                                                        if (document.getString("company").equals(uid) && document.getString("date").equals(filterTanggal)) {
                                                            mOrder.add(new Order(document.get("ruang").toString(), document.get("harga").toString(), document.getString("date"), document.getString("time"), docUser));
                                                            mRecyclerView = (RecyclerView) getView().findViewById(R.id.history_recycler);
                                                            mAdapter = new HistoryAdapter(getContext(), mOrder);
                                                            mRecyclerView.setAdapter(mAdapter);
                                                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                                        }
                                                    } else {
                                                        if (document.getString("company").equals(uid) && document.getString("ruang").equals(filterRuang) && document.getString("date").equals(filterTanggal)) {
                                                            mOrder.add(new Order(document.get("ruang").toString(), document.get("harga").toString(), document.getString("date"), document.getString("time"), docUser));
                                                            mRecyclerView = (RecyclerView) getView().findViewById(R.id.history_recycler);
                                                            mAdapter = new HistoryAdapter(getContext(), mOrder);
                                                            mRecyclerView.setAdapter(mAdapter);
                                                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                                        }
                                                    }
                                                }
                                                Log.d("cmplt", document.getId() + " => " + taskUser.getResult().getString("nama"));
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                            Spinner spinnerTanggal = getView().findViewById(R.id.filter_tanggal);

                            ArrayAdapter<String> adapterTanggal = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArrayTanggal);
                            adapterTanggal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerTanggal.setAdapter(adapterTanggal);
                            spinnerTanggal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    filterTanggal = adapterView.getItemAtPosition(i).toString();
                                    mOrder.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String docRefUser = document.get("user").toString();

                                        db.collection("user").document(docRefUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> taskUser) {
                                                docUser = taskUser.getResult();
                                                if(filterTanggal.equals("Tanggal")){
                                                    if(filterRuang.equals("Ruangan")){
                                                        if(document.getString("company").equals(uid)){
                                                            if(mOrder.size() == 0){
                                                                mOrder.add(new Order(document.get("ruang").toString(), document.get("harga").toString(), document.getString("date"),  document.getString("time"), docUser));
                                                                mRecyclerView = (RecyclerView) getView().findViewById(R.id.history_recycler);
                                                                mAdapter = new HistoryAdapter(getContext(), mOrder);
                                                                mRecyclerView.setAdapter(mAdapter);
                                                                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                                            }
                                                        }
                                                    }else{
                                                        if(document.getString("company").equals(uid) && document.getString("ruang").equals(filterRuang)){
                                                            mOrder.add(new Order(document.get("ruang").toString(), document.get("harga").toString(), document.getString("date"),  document.getString("time"), docUser));
                                                            mRecyclerView = (RecyclerView) getView().findViewById(R.id.history_recycler);
                                                            mAdapter = new HistoryAdapter(getContext(), mOrder);
                                                            mRecyclerView.setAdapter(mAdapter);
                                                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                                        }
                                                    }
                                                }else {
                                                    if (filterRuang.equals("Ruangan")) {
                                                        if (document.getString("company").equals(uid) && document.getString("date").equals(filterTanggal)) {
                                                            mOrder.add(new Order(document.get("ruang").toString(), document.get("harga").toString(), document.getString("date"), document.getString("time"), docUser));
                                                            mRecyclerView = (RecyclerView) getView().findViewById(R.id.history_recycler);
                                                            mAdapter = new HistoryAdapter(getContext(), mOrder);
                                                            mRecyclerView.setAdapter(mAdapter);
                                                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                                        }
                                                    } else {
                                                        if (document.getString("company").equals(uid) && document.getString("ruang").equals(filterRuang) && document.getString("date").equals(filterTanggal)) {
                                                            mOrder.add(new Order(document.get("ruang").toString(), document.get("harga").toString(), document.getString("date"), document.getString("time"), docUser));
                                                            mRecyclerView = (RecyclerView) getView().findViewById(R.id.history_recycler);
                                                            mAdapter = new HistoryAdapter(getContext(), mOrder);
                                                            mRecyclerView.setAdapter(mAdapter);
                                                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                                        }
                                                    }
                                                }
                                                Log.d("cmplt", document.getId() + " => " + taskUser.getResult().getString("nama"));
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}