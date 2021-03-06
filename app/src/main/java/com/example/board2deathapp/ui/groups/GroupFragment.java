package com.example.board2deathapp.ui.groups;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;

import com.example.board2deathapp.LandingActivity;
import com.example.board2deathapp.R;
import com.example.board2deathapp.models.DBResponse;
import com.example.board2deathapp.models.Group;
import com.example.board2deathapp.models.ModelCollection;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class GroupFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private MyGroupRecyclerViewAdapter adpt;
    private OnListFragmentInteractionListener mListener;
    private ModelCollection<Group> all_groups;
    private ModelCollection<Group> my_groups;
    private RecyclerView recycleView;
    private static String TAG = "GROUPS";

    private Button allGroupsButton;
    private Button myGroupsButton;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroupFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static GroupFragment newInstance(int columnCount) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        all_groups = new ModelCollection<>(Group.class);
        my_groups = new ModelCollection<>(Group.class);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_list, container, false);
        Query q = FirebaseFirestore.getInstance().collection("group").orderBy("groupName");

        RecyclerView recyclerView = view.findViewById(R.id.list);
        // Set the adapter
        if (recyclerView != null) {
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adpt = new MyGroupRecyclerViewAdapter(all_groups.getItems(), mListener);
            all_groups.read_current(q, new DBResponse(getActivity()) {
                @Override
                public <T> void onSuccess(T t) {
                    Log.d(TAG, "Reading Groups");
                    GroupFragment.this.adpt.notifyDataSetChanged();
                }
            });
            recyclerView.setAdapter(adpt);
            recycleView = recyclerView;
            FloatingActionButton b = view.findViewById(R.id.addFab);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                    DialogFragment temp = new AddGroupFragment();
                    temp.show(fm, "ADD_GROUP");
                }
            });

            myGroupsButton = view.findViewById(R.id.myGroups);
            allGroupsButton = view.findViewById(R.id.allGroups);
            changeTabColor(myGroupsButton);
            final String current_user = ((LandingActivity) getActivity()).getUser().getUsername();
            myGroupsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adpt = new MyGroupRecyclerViewAdapter(GroupFragment.this.my_groups.getItems(), mListener);
                    changeTabColor(myGroupsButton);
                    recycleView.setAdapter(GroupFragment.this.adpt);
                    Query q = FirebaseFirestore.getInstance().collection("group").whereArrayContains("users", current_user);
                    my_groups.read_current(q, new DBResponse(getActivity()) {
                        @Override
                        public <T> void onSuccess(T t) {
                            adpt.notifyDataSetChanged();
                        }

                        @Override
                        public <T> void onFailure(T t) {

                        }

                    });
                }
            });
            allGroupsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adpt = new MyGroupRecyclerViewAdapter(GroupFragment.this.all_groups.getItems(), mListener);
                    changeTabColor(allGroupsButton);
                    recycleView.setAdapter(GroupFragment.this.adpt);
                    Query q = FirebaseFirestore.getInstance().collection("group").orderBy("groupName");
                    all_groups.read_current(q, new DBResponse(getActivity()) {
                        @Override
                        public <T> void onSuccess(T t) {
                            adpt.notifyDataSetChanged();
                        }

                        @Override
                        public <T> void onFailure(T t) {

                        }

                    });
                }
            });
        }
        return view;
    }

    private void changeTabColor(Button button) {
        allGroupsButton.setBackgroundColor(Color.TRANSPARENT);
        myGroupsButton.setBackgroundColor(Color.TRANSPARENT);
        allGroupsButton.setTextColor(getResources().getColor(R.color.colorAccent));
        myGroupsButton.setTextColor(getResources().getColor(R.color.colorAccent));
        button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        button.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Group item);
    }
}
