package sg.edu.rp.c346.c302firebasehelloworld;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class MainActivity extends AppCompatActivity {

    private TextView tvMessage;
    private EditText etMessage;
    private TextView tvPriority;
    private EditText etPriority;
    private Button btnUpdate;

    private FirebaseFirestore db;
    private CollectionReference colRef;
    private DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMessage = findViewById(R.id.textViewMessage);
        etMessage = findViewById(R.id.editTextMessage);
        tvPriority = findViewById(R.id.textViewPriority);
        etPriority = findViewById(R.id.editTextPriority);

        db = FirebaseFirestore.getInstance();

        colRef = db.collection("messages");
        docRef = colRef.document("message");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Message msg = snapshot.toObject(Message.class);
                    tvMessage.setText(msg.getText());
                    tvPriority.setText("Priority is: "+ msg.getPriority());
                }
            }
        });


        btnUpdate = findViewById(R.id.buttonUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnUpdateOnClick(v);
            }
        });


    }

    private void btnUpdateOnClick(View v) {
        String text = etMessage.getText().toString();
        String priority = etPriority.getText().toString();
        Message msg = new Message(text, priority);
        docRef.set(msg);
    }
}
