package com.oQp.marathonapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String DATABASE_NAME="marathonDb";

    SQLiteDatabase myDb;

    TextView textViewViewCandidate;
    EditText editTextName, editTextAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewViewCandidate = findViewById(R.id.textViewViewCandidate);
        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);


        findViewById(R.id.buttonAddCandidate).setOnClickListener(this);
        textViewViewCandidate.setOnClickListener(this);

        myDb= openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        createTable();


    }
    private void createTable(){

     myDb.execSQL(
                "CREATE TABLE IF NOT EXISTS candidate (\n" +
                        "    id INTEGER NOT NULL CONSTRAINT candidate_pk PRIMARY KEY AUTOINCREMENT,\n" +
                        "    name varchar(200) NOT NULL,\n" +
                        "    age INTEGER NOT NULL\n" +
                        ");"
        );
    }

    //this method will validate the name and salary
    //dept does not need validation as it is a spinner and it cannot be empty
    private boolean inputsAreCorrect(String name, String age) {
        if (name.isEmpty()) {
            editTextName.setError("Please enter a name");
            editTextName.requestFocus();
            return false;
        }

        if (age.isEmpty() || Integer.parseInt(age) <= 0) {
            editTextAge.setError("Please enter age");
            editTextAge.requestFocus();
            return false;
        }
        return true;
    }


    //In this method we will do the create operation
    private void addCandidate() {

        String name = editTextName.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();



        //validating the inptus
        if (inputsAreCorrect(name, age)) {

            String insertSQL = "INSERT INTO candidate \n" +
                    "(name,  age)\n" +
                    "VALUES \n" +
                    "(?, ?);";

            //using the same method execsql for inserting values
            //first is the sql string and second is the parameters that is to be binded with the query
            myDb.execSQL(insertSQL, new String[]{name, age});

            Toast.makeText(this, "Candidate Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddCandidate:

                addCandidate();

                break;

            case R.id.textViewViewCandidate:

                startActivity(new Intent(this, CandidateActivity.class));

                break;


        }
    }
}