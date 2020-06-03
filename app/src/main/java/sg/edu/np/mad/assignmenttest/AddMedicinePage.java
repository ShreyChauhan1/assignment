package sg.edu.np.mad.assignmenttest;

import android.app.Activity;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddMedicinePage extends AppCompatActivity {

    public static EditText searchMed;
    TextView dose;
    String medName;
    ImageButton plus,minus,breakfast,lunch,dinner;

    Integer doseNumber,breakfastValid,lunchValid,dinnerValid;


    DatabaseReference databaseReference;
    ArrayList<String> med_list;
    ArrayList<String> id_list;
    SearchAdapter searchAdapter;
    RadioGroup radioGroup;
    public static RecyclerView recyclerView;
    TimePickerDialog picker;

//git

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_med);
        radioGroup=findViewById(R.id.radioGroup);
        dose=findViewById(R.id.dose);
        plus=findViewById(R.id.plus);
        minus=findViewById(R.id.minus);
        doseNumber=0;
        recyclerView = findViewById(R.id.recyclerView);
        searchMed = findViewById(R.id.search_med);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        recyclerView.addItemDecoration((new DividerItemDecoration(this, LinearLayoutManager.VERTICAL)));

        med_list = new ArrayList<>();
        id_list = new ArrayList<>();
        Spinner spinner=findViewById(R.id.spinner1);
        // Create an ArrayAdapter using the string array and a default spinner layout

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.medType,android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doseNumber++;
                Log.v("dose number:",doseNumber.toString());
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doseNumber!=0){
                doseNumber--;
                Log.v("dose number:",doseNumber.toString());
                }
            }
        });
        String doseString=doseNumber.toString();
        dose.setText(doseString);

        searchMed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_DONE){
                    //Clear focus here from edittext

                    searchMed.clearFocus();


                }
                return false;
            }
        });

        searchMed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("Test","1");
                if (!s.toString().isEmpty()) {
                    medName=searchMed.getText().toString();
                    if(searchMed.hasFocus()){
                    setAdapter(s.toString());}
                    else{
                        med_list.clear();
                        id_list.clear();
                        searchAdapter.notifyDataSetChanged();
                    }

                }

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();


        //continue from here
        /*submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if all tasks has been selected.
                //
                checkValid();
            }
        });*/

    }

    private void setAdapter(final String searchedString) {

        databaseReference.child("Medicine").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                med_list.clear();
                id_list.clear();
                recyclerView.removeAllViews();
                int counter = 0;

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    //String med_num=snapshot.getKey();
                    String med_name=snapshot.child("Name").getValue().toString();
                    String id_num=snapshot.child("ID").getValue().toString();
                    if(med_name.toLowerCase().contains(searchedString.toLowerCase())){
                        med_list.add(med_name);
                        id_list.add(id_num);
                        counter++;
                    }
                    else if (id_num.contains(searchedString)){
                        med_list.add(med_name);
                        id_list.add(id_num);
                        counter++;
                    }
                    if(counter==5){
                        break;
                    }
                }
                searchAdapter=new SearchAdapter(AddMedicinePage.this,med_list,id_list);
                recyclerView.setAdapter(searchAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddMedicinePage.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void checkValid(){
        databaseReference.child("Medicine").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int correctMed = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String med_name = snapshot.child("Name").getValue().toString();
                    if (med_name == medName) {
                        Log.v("Test", "1");
                        correctMed=2;
                        break;




                    }
                    else {
                        Log.d("Test","2");
                        correctMed=1;


                    }

                }
                if (correctMed==1)
                {
                    searchMed.setError("Invalid Medicine Name");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Please select one of the options for medicine intake period",
                    Toast.LENGTH_SHORT);

            toast.show();
        }
        else {
        }






    }
}
