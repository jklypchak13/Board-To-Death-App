package com.example.board2deathapp.ui.boardgame;

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
import com.example.board2deathapp.models.BoardGame;
import com.example.board2deathapp.models.DBResponse;
import com.example.board2deathapp.models.Model;

public class EditBoardGameFragment extends DialogFragment {
    private static String TAG = "ADDGAME";

    private DialogInterface something;
    private BoardGame game;
    public EditBoardGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_add_board_game, container, false);

        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.fragment_edit_board_game,null);
        builder.setView(v);

        // Get Text Fields
        final EditText name_field = v.findViewById(R.id.e_game_name);
        final EditText description_field= v.findViewById(R.id.e_game_desc);
        final EditText time_field= v.findViewById(R.id.e_game_time);
        final EditText player_count_field= v.findViewById(R.id.e_game_count);

        // Set Up the Delete Button
        final Button delete_button = v.findViewById(R.id.delete_game);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.delete(new DBResponse(getActivity()) {
                    @Override
                    public <T> void onSuccess(T t) {
                        EditBoardGameFragment.this.getDialog().cancel();
                    }
                });
            }
        });

        //Set Up Initial Text Fields
        name_field.setText(game.getTitle());
        description_field.setText(game.getDescription());
        player_count_field.setText(Integer.toString(game.getCount()));
        time_field.setText(Double.toString(game.getTime()));

        builder.setPositiveButton("Edit Game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        // Get Values from the Text Fields
                        String name = name_field.getText().toString();
                        String description = description_field.getText().toString();
                        String players = player_count_field.getText().toString();
                        int player_count  = Integer.parseInt(players);
                        double play_time = Double.parseDouble(time_field.getText().toString());

                        //Update Game Values
                        game.setName(name);
                        game.setDescription(description);
                        game.setCount(player_count);
                        game.setTime(play_time);
                        game.update(new DBResponse(getActivity()) {
                            @Override
                            public <T> void onSuccess(T t) {
                                Log.d("EDIT", "Editted the game!");
                            }

                            @Override
                            public <T> void onFailure(T t) {
                                Log.d("EDIT","Failed =(");
                            }
                        });
                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditBoardGameFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public void setGame(BoardGame g){
        this.game = g;
    }

}
