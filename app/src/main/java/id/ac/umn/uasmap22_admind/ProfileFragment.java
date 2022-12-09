package id.ac.umn.uasmap22_admind;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView logout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    public void logout() {
        Log.i("LOGOUT", "MASUK FUNCTION LOGOUT");
        FirebaseAuth.getInstance().signOut();
        getActivity().finish();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        logout = view.findViewById(R.id.logout);
        if (user != null) {
            String uid = user.getUid();
            getUser(uid);
            logout.setOnClickListener(v -> {
                logout();
            });
        } else {
            // No user is signed in
        }

        // Inflate the layout for this fragment
        return view;
    }

    public void getUser(String uid){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("partner").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getString("name"));

                        TextView tv_nama = (TextView) getView().findViewById(R.id.profile_nama);
                        tv_nama.setText(document.getString("nama"));

                        TextView tv_alamat = (TextView) getView().findViewById(R.id.profile_alamat);
                        tv_alamat.setText(document.getString("alamat"));

                        TextView tv_type = (TextView) getView().findViewById(R.id.profile_type);
                        tv_type.setText(": " + document.getString("kategori"));

                        TextView tv_phone = (TextView) getView().findViewById(R.id.profile_phone);
                        tv_phone.setText(": " + document.getString("phone"));

                        TextView tv_email = (TextView) getView().findViewById(R.id.profile_email);
                        tv_email.setText(": " + user.getEmail());
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