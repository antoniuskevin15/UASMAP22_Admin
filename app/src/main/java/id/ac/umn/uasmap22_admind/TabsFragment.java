package id.ac.umn.uasmap22_admind;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class TabsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabsFragment newInstance(String param1, String param2) {
        TabsFragment fragment = new TabsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TabsFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tabs2, container, false);

        LinearLayout customize = (LinearLayout) rootView.findViewById(R.id.linearLayout6);

        customize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRent = new Intent (getActivity(), CustomizeActivity.class);
                startActivity(intentRent);
            }
        });

        LinearLayout home = (LinearLayout) rootView.findViewById(R.id.linearLayout5);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                Intent intentHome = new Intent (getActivity(), HomeActivity.class);
                startActivity(intentHome);
            }
        });
//
//        LinearLayout profile = rootView.findViewById(R.id.linearLayout4);
//
//        profile.setOnClickListener(view -> {
//            Intent intentRent = new Intent (getActivity(), ProfileActivity.class);
//            startActivity(intentRent);
//        });

        return rootView;
    }
}