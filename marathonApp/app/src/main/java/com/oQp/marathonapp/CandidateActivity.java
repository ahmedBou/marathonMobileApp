  package com.oQp.marathonapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

  public class CandidateActivity extends AppCompatActivity {
    SQLiteDatabase myDb;

    List<Candidate> candidateList;
    ListView listView;
    CandidateLayout cLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate);

        myDb= openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        candidateList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listViewCandidate);

        fecthCandidate();
    }
    
    private void fecthCandidate(){
        String sql = "SELECT * FROM candidate";

        Cursor cursor = myDb.rawQuery(sql, null);
        // checking if having any data or not
        if(cursor.moveToFirst()){
            do{
                candidateList.add(new Candidate(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2))
                );
            }while(cursor.moveToNext());

        cLayout = new CandidateLayout(this, R.layout.list_candidate, candidateList, myDb);

        listView.setAdapter(cLayout);
        }
    }
}