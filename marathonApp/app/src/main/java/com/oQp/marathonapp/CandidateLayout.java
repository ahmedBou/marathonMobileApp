package com.oQp.marathonapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CandidateLayout extends ArrayAdapter<Candidate> {

    Context mCtx;
    int listLayoutRes;
    List<Candidate> candidateList;
    SQLiteDatabase mDatabase;

    public CandidateLayout(Context mCtx, int listLayoutRes, List<Candidate> candidateList, SQLiteDatabase mDatabase) {
        super(mCtx, listLayoutRes, candidateList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.candidateList = candidateList;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        //getting employee of the specified position
        Candidate candidate = candidateList.get(position);


        //getting views
        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewAge = view.findViewById(R.id.textViewAge);


        //adding data to views
        textViewName.setText(candidate.getName());
        textViewAge.setText(String.valueOf(candidate.getAge()));


        //we will use these buttons later for update and delete operation
        Button buttonDelete = view.findViewById(R.id.buttonDeleteCandidate);
        Button buttonEdit = view.findViewById(R.id.buttonEditCandidate);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCandidate(candidate);
            }
        });

        //delete
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM candidate WHERE id = ?";
                        mDatabase.execSQL(sql, new Integer[]{candidate.getId()});
                        reloadEmployeesFromDatabase();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }


    private void updateCandidate(final Candidate candidate) {
    final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

    LayoutInflater inflater = LayoutInflater.from(mCtx);
    View view = inflater.inflate(R.layout.dialogue_update_candidate, null);
        builder.setView(view);


    final EditText editTextName = view.findViewById(R.id.editTextName);
    final EditText editTextAge = view.findViewById(R.id.editTextAge);


        editTextName.setText(candidate.getName());
        editTextAge.setText(String.valueOf(candidate.getAge()));

    final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.buttonUpdateCandidate).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name = editTextName.getText().toString().trim();
            String age = editTextAge.getText().toString().trim();

            if (name.isEmpty()) {
                editTextName.setError("Name can't be blank");
                editTextName.requestFocus();
                return;
            }

            if (age.isEmpty()) {
                editTextAge.setError("Salary can't be blank");
                editTextAge.requestFocus();
                return;
            }

            String sql = "UPDATE candidate SET name = ?, age= ? WHERE id = ?";

            mDatabase.execSQL(sql, new String[]{name, age, String.valueOf(candidate.getId())});
            Toast.makeText(mCtx, "Employee Updated", Toast.LENGTH_SHORT).show();
            reloadEmployeesFromDatabase();

            dialog.dismiss();
        }
    });


}

    private void reloadEmployeesFromDatabase() {
        Cursor cursorCandidate = mDatabase.rawQuery("SELECT * FROM candidate", null);
        if (cursorCandidate.moveToFirst()) {
            candidateList.clear();
            do {
                candidateList.add(new Candidate(
                        cursorCandidate.getInt(0),
                        cursorCandidate.getString(1),
                        cursorCandidate.getInt(2)
                ));
            } while (cursorCandidate.moveToNext());
        }
        cursorCandidate.close();
        notifyDataSetChanged();
    }

}