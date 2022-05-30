package edu.weber.cs.w01378454.findyourway;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListLocationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View root;
    private int requestCode = 1;

    Context mContext;
    Activity activity;

    private FirebaseRecyclerOptions<ListLocationViewModel> ListLocation;
    private FirebaseRecyclerAdapter<ListLocationViewModel, ListLocationViewHolder> adapter;

    DatabaseReference ref;

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;



    public ListLocationFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListLocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListLocationFragment newInstance(String param1, String param2)
    {
        ListLocationFragment fragment = new ListLocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mContext = getContext();
        activity = getActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_list_location, container, false);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        ref = FirebaseDatabase.getInstance().getReference().child("Locations");

        recyclerView = root.findViewById(R.id.LocationListRecyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        ListLocation = new FirebaseRecyclerOptions.Builder<ListLocationViewModel>().setQuery(ref,ListLocationViewModel.class).build();
        adapter = new FirebaseRecyclerAdapter<ListLocationViewModel, ListLocationViewHolder>(ListLocation) {

            @Override
            protected void onBindViewHolder(@NonNull ListLocationViewHolder holder, int position, @NonNull ListLocationViewModel model)
            {
                if(model != null)
                {

                    holder.textViewLocationName.setText(model.getLocationName() + "");

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String uri = String.format(Locale.ENGLISH, "geo:%s", model.getGPSCoordinates());
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            mContext.startActivity(intent);

                        }
                    });
                }

                if(model != null)
                {
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {


                            return false;
                        }
                    });
                }

            }

            @NonNull
            @Override
            public ListLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list_layout_view, parent, false);

                return new ListLocationViewHolder(v);
            }


        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);



        FloatingActionButton SaveLocationFab = root.findViewById(R.id.fabSaveLocation);
        SaveLocationFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(ContextCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.ACCESS_FINE_LOCATION))
                    {

                    }else{
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},requestCode);
                    }
                }


                FragmentManager fm = getFragmentManager();
                FullScreenSaveLocationDialogFragment d = new FullScreenSaveLocationDialogFragment();
                d.show(fm, "FullScreenSaveLocationDialogFragment");



            }
        });



    }
}