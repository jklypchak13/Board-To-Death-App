package com.example.board2deathapp.ui.Newsletter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.board2deathapp.R;
import com.example.board2deathapp.models.Newsletter;
import com.example.board2deathapp.models.DBResponse;

public class EditNewsletterFragment extends DialogFragment {

    private static String TAG = "EDIT_NEWSLETTER";

    private Newsletter newsletter;
    public EditNewsletterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "Creating Dialog");
        final View v = inflater.inflate(R.layout.fragment_edit_newsletter, container, false);

        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.fragment_edit_newsletter,null);
        final EditText title = v.findViewById(R.id.postTitle);
        final EditText description= v.findViewById(R.id.postContent);

        final Button delete_button = v.findViewById(R.id.deletePost);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsletter.delete(new DBResponse(getActivity()) {
                    @Override
                    public <T> void onSuccess(T t) {
                        EditNewsletterFragment.this.getDialog().cancel();
                    }
                });
            }
        });
        title.setText(newsletter.getTitle());
        description.setText(newsletter.getDescription());
        builder.setView(v);
        builder.setPositiveButton("Edit Newsletter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String desc = description.getText().toString();
                String new_title = title.getText().toString();

                newsletter.setDescription(desc);
                newsletter.setTitle(new_title);

                newsletter.update(new DBResponse(getActivity()) {
                    @Override
                    public <T> void onSuccess(T t) {
                        Log.d("EDIT", "Edited the Newsletter!");
                    }

                    @Override
                    public <T> void onFailure(T t) {
                        Log.d("EDIT","Failed =(");
                    }
                });
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditNewsletterFragment.this.getDialog().cancel();
            }
        });
        return builder.create();
    }

    public void setNewsletter(Newsletter n){
        this.newsletter = n;
    }

}
