package com.example.tsdw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference epRef = db.document("episodes/1");
    private TextView episodeData;
    private Button newRandomEpisode;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        episodeData = findViewById(R.id.textView);
        newRandomEpisode = findViewById(R.id.button);

        displayRandomEpisode();

        newRandomEpisode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayRandomEpisode();
            }
        });
    }

    public void displayRandomEpisode() {
        epRef = db.document("episodes/collectionSize");

        epRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            long numberOfEpisodes = documentSnapshot.getLong("numberOfEpisodes");
                            Log.d(TAG, String.valueOf(numberOfEpisodes));
                            Random rand = new Random();
                            int upperbound = (int) (numberOfEpisodes);
                            int randomNumber = (rand.nextInt(upperbound) + 1);
                            String randomNumberAsString = String.valueOf(randomNumber);
                            String documentPath = "episodes/" + randomNumberAsString;
                            Log.i(TAG, randomNumberAsString);
                            Log.d(TAG, documentPath);
                            epRef = db.document(documentPath);

                            epRef.get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                String randomEpisode = documentSnapshot.getString("film");
                                                Date episodeDate = documentSnapshot.getDate("date");
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                                                String episodeDateAsString = dateFormat.format(episodeDate);
                                                Log.d(TAG, randomEpisode);
                                                episodeData.setText(randomEpisode + "\n" + episodeDateAsString);
                                            } else {
                                                Toast.makeText(MainActivity.this, "Document Does Not Exist", Toast.LENGTH_SHORT);
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        } else {
                            Toast.makeText(MainActivity.this, "Document Does Not Exist", Toast.LENGTH_SHORT);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}