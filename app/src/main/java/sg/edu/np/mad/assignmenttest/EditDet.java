package sg.edu.np.mad.assignmenttest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EditDet extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Button cancel=findViewById(R.id.button2);
        Button save=findViewById(R.id.button);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditDet.this,MainActivity.class));

            }
        });
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                TextView email=findViewById(R.id.editText2);
                String emailstring=email.toString();
                TextView number=findViewById(R.id.editText3);
                String numberstring=number.toString();
                Intent intent = new Intent(getBaseContext(), EditDet.class);
                intent.putExtra("email",emailstring );
                intent.putExtra("number",numberstring);
                startActivity(intent);
                startActivity(new Intent(EditDet.this,MainActivity.class));


            }
        });
    }
}


