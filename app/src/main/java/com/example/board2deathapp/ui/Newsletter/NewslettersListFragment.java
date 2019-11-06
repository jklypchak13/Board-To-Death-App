package com.example.board2deathapp.ui.Newsletter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.board2deathapp.LandingActivity;
import com.example.board2deathapp.R;
import com.example.board2deathapp.models.Newsletter;
import com.example.board2deathapp.models.DBResponse;
import com.example.board2deathapp.models.ModelCollection;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class NewslettersListFragment extends Fragment {


    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = "NEWSLETTER";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ModelCollection<Newsletter> My_Newsletters;
    private ModelCollection<Newsletter> All_Newsletters;
    private OnListFragmentInteractionListener mListener;
    private MyNewsletterRecyclerViewAdapter adpt;
    private RecyclerView recycleView;
    private String current_user;
    private static String CLUB_USER = "Board2Death";
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewslettersListFragment() {
    }

    public static NewslettersListFragment newInstance(int columnCount) {


        NewslettersListFragment fragment = new NewslettersListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        current_user = ((LandingActivity)getActivity()).getUser().getUsername();


        My_Newsletters = new ModelCollection<Newsletter>(Newsletter.class);
        All_Newsletters = new ModelCollection<Newsletter>(Newsletter.class);

        this.adpt = new MyNewsletterRecyclerViewAdapter(this.My_Newsletters.getItems(),mListener);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsletters_list, container, false);

        // Set the adapter
        View rview = view.findViewById(R.id.list);
        if (rview instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) rview;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(this.adpt);
            Query q = FirebaseFirestore.getInstance().collection("newsletter").orderBy("date",  Query.Direction.DESCENDING);
            My_Newsletters.read_current(q, new DBResponse(getActivity()) {
                @Override
                public <T> void onSuccess(T t) {
                    adpt.notifyDataSetChanged();
                }
                @Override
                public <T> void onFailure(T t){

                }

            });
            recycleView = recyclerView;
            view.findViewById(R.id.addFab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    DialogFragment temp = new AddNewsletterFragment();
                    temp.show(fm,"ADD_NEWSLETTER");
                }
            });



        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

         */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Newsletter item);
    }


}
