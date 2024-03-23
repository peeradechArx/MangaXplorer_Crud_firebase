package peeradech.p.project_crud_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class AddMangaActivity extends AppCompatActivity {

    // creating variables for our button, edit text,
    // firebase database, database reference, progress bar.
    public Button addMangaBtn;
    private TextInputEditText MangaNameEdt, MangaDescEdt, MangaPriceEdt, bestSuitedEdt, MangaImgEdt, MangaLinkEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ProgressBar loadingPB;
    private String MangaID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manga);
        // Write a message to the database
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("message");
        //myRef.setValue("Hello, World1254522!");
        // initializing all our variables.
        addMangaBtn = findViewById(R.id.idBtnAddManga);
        MangaNameEdt = findViewById(R.id.idEdtMangaName);
        MangaDescEdt = findViewById(R.id.idEdtMangaDescription);
        MangaPriceEdt = findViewById(R.id.idEdtMangaPrice);
        bestSuitedEdt = findViewById(R.id.idEdtSuitedFor);
        MangaImgEdt = findViewById(R.id.idEdtMangaImageLink);
        MangaLinkEdt = findViewById(R.id.idEdtMangaLink);
        loadingPB = findViewById(R.id.idPBLoading);
        // getting instance of FirebaseDatabase
        firebaseDatabase = FirebaseDatabase.getInstance();
        // creating reference to "Mangas" node in the database
        databaseReference = firebaseDatabase.getReference("Mangas");

        // adding click listener for our add Manga button.
        addMangaBtn.setOnClickListener(v -> {
            // making progress bar visible
            loadingPB.setVisibility(View.VISIBLE);

            // getting data from edit text fields
            String MangaName = MangaNameEdt.getText() != null ? MangaNameEdt.getText().toString() : "";
            String MangaDesc = MangaDescEdt.getText() != null ? MangaDescEdt.getText().toString() : "";
            String MangaPrice = MangaPriceEdt.getText() != null ? MangaPriceEdt.getText().toString() : "";
            String bestSuited = bestSuitedEdt.getText() != null ? bestSuitedEdt.getText().toString() : "";
            String MangaImg = MangaImgEdt.getText() != null ? MangaImgEdt.getText().toString() : "";
            String MangaLink = MangaLinkEdt.getText() != null ? MangaLinkEdt.getText().toString() : "";
            MangaID = UUID.randomUUID().toString();

            // on below line we are passing all data to our modal class.
            MangaRVModal MangaRVModal = new MangaRVModal(MangaID, MangaName, MangaDesc, MangaPrice, bestSuited, MangaImg, MangaLink);
            // on below line we are calling a add value event
            // to pass data to firebase database.
            // Replace addValueEventListener with addListenerForSingleValueEvent
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Check if the MangaID is not null and if it doesn't exist in the database
                    if (MangaID != null && !snapshot.hasChild(MangaID)) {
                        // Set data in Firebase Database only when adding a new Manga
                        databaseReference.child(MangaID).setValue(MangaRVModal);
                        // Display a toast message.
                        Toast.makeText(AddMangaActivity.this, "Manga Added..", Toast.LENGTH_SHORT).show();
                        // Start MainActivity.
                        startActivity(new Intent(AddMangaActivity.this, MainActivity.class));
                    } else {
                        // Handle the case where the Manga already exists or the MangaID is null.
                        // Display a message or perform any necessary action.
                        Toast.makeText(AddMangaActivity.this, "Manga already exists or MangaID is null", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Display a failure message on below line.
                    Toast.makeText(AddMangaActivity.this, "Fail to add Manga..", Toast.LENGTH_SHORT).show();
                }
            });

        });
    }
}
