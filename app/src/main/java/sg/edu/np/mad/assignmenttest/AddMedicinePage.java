package sg.edu.np.mad.assignmenttest;

import android.app.SearchManager;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddMedicinePage extends AppCompatActivity {
    EditText searchMed,editTime;
    ImageButton submit;

    DatabaseReference databaseReference;
    ArrayList<String> med_list;
    ArrayList<String> id_list;

    SearchAdapter searchAdapter;
    RecyclerView recyclerView;
//git
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_med);
        recyclerView = findViewById(R.id.recyclerView);

        searchMed = findViewById(R.id.search_med);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        recyclerView.addItemDecoration((new DividerItemDecoration(this, LinearLayoutManager.VERTICAL)));

        med_list = new ArrayList<>();
        id_list = new ArrayList<>();

        editTime = findViewById(R.id.editText_time);
        submit = findViewById(R.id.btn_submit);


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
                    setAdapter(s.toString());
                }
                else{
                    med_list.clear();
                    id_list.clear();
                    recyclerView.removeAllViews();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();
                new SingleDateAndTimePickerDialog.Builder(AddMedicinePage.this)
                        .defaultDate(c)
                        .minDateRange(c)
                        .minutesStep(15)
                        .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                            @Override
                            public void onDisplayed(SingleDateAndTimePicker picker) {

                            }
                        })
                        .title("Allocate Medicine Timing")
                        .listener(new SingleDateAndTimePickerDialog.Listener() {
                            @Override
                            public void onDateSelected(Date date) {
                                Date selectedDate = date;
                                editTime.setText(selectedDate.toString());
                                //Toast.makeText(AddMedicinePage.this, selectedDate.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }).display();
            }
        });
        //continue from here
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if all tasks has been selected.
                //
            }
        });

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
                    String med_num=snapshot.getKey();
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
                    if(counter==10){
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
}
