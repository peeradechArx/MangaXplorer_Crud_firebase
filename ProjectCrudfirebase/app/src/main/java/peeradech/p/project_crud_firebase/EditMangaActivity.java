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

import java.util.HashMap;
import java.util.Map;

public class EditMangaActivity extends AppCompatActivity {

    //creating variables for our edit text, firebase database, database reference, Manga rv modal,progress bar.
    private TextInputEditText MangaNameEdt, MangaDescEdt, MangaPriceEdt, bestSuitedEdt, MangaImgEdt, MangaLinkEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    peeradech.p.project_crud_firebase.MangaRVModal MangaRVModal;
    private ProgressBar loadingPB;
    //creating a string for our Manga id.
    private String MangaID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_manga);
        // Initializing all our variables on below line.
        Button addMangaBtn = findViewById(R.id.idBtnAddManga);
        MangaNameEdt = findViewById(R.id.idEdtMangaName);
        MangaDescEdt = findViewById(R.id.idEdtMangaDescription);
        MangaPriceEdt = findViewById(R.id.idEdtMangaPrice);
        bestSuitedEdt = findViewById(R.id.idEdtSuitedFor);
        MangaImgEdt = findViewById(R.id.idEdtMangaImageLink);
        MangaLinkEdt = findViewById(R.id.idEdtMangaLink);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // On below line we are getting our modal class on which we have passed.
        MangaRVModal = getIntent().getParcelableExtra("Manga");
        Button deleteMangaBtn = findViewById(R.id.idBtnDeleteManga);

        if (MangaRVModal != null) {
            // On below line we are setting data to our edit text from our modal class.
            MangaNameEdt.setText(MangaRVModal.getMangaName());
            MangaPriceEdt.setText(MangaRVModal.getMangaPrice());
            bestSuitedEdt.setText(MangaRVModal.getBestSuitedFor());
            MangaImgEdt.setText(MangaRVModal.getMangaImg());
            MangaLinkEdt.setText(MangaRVModal.getMangaLink());
            MangaDescEdt.setText(MangaRVModal.getMangaDescription());
            MangaID = MangaRVModal.getMangaId();
        }

        // On below line we are initializing our database reference and we are adding a child as our Manga id.
        databaseReference = firebaseDatabase.getReference("Mangas").child(MangaID);
        // On below line we are adding click listener for our add Manga button.
        addMangaBtn.setOnClickListener(v -> {
            // On below line we are making our progress bar as visible.
            loadingPB.setVisibility(View.VISIBLE);
            // On below line we are getting data from our edit text.
            String MangaName = MangaNameEdt.getText().toString();
            String MangaDesc = MangaDescEdt.getText().toString();
            String MangaPrice = MangaPriceEdt.getText().toString();
            String bestSuited = bestSuitedEdt.getText().toString();
            String MangaImg = MangaImgEdt.getText().toString();
            String MangaLink = MangaLinkEdt.getText().toString();
            // On below line we are creating a map for passing a data using key and value pair.
            Map<String, Object> map = new HashMap<>();
            map.put("MangaName", MangaName);
            map.put("MangaDescription", MangaDesc);
            map.put("MangaPrice", MangaPrice);
            map.put("bestSuitedFor", bestSuited);
            map.put("MangaImg", MangaImg);
            map.put("MangaLink", MangaLink);
            map.put("MangaId", MangaID);

            // On below line we are adding a single event listener to check if the data exists before updating.
            // Check if MangaID is not null or empty before proceeding.
            if (MangaID != null && !MangaID.isEmpty()) {
                // On below line we are adding a single event listener to check if the data exists before updating.
                // On below line we are adding a single event listener to check if the data exists before updating.
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Making progress bar visibility as gone.
                        loadingPB.setVisibility(View.GONE);
                        if (snapshot.exists()) {
                            // Updating the database with new values using setValue method.
                            databaseReference.setValue(map)
                                    .addOnSuccessListener(aVoid -> {
                                        // Displaying a toast message.
                                        Toast.makeText(EditMangaActivity.this, "Manga Updated..", Toast.LENGTH_SHORT).show();
                                        // Opening a new activity after updating our Manga.
                                        startActivity(new Intent(EditMangaActivity.this, MainActivity.class));
                                    })
                                    .addOnFailureListener(e -> {
                                        // Displaying a failure message on toast.
                                        Toast.makeText(EditMangaActivity.this, "Fail to update Manga..", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            // Displaying a failure message on toast if the data doesn't exist.
                            Toast.makeText(EditMangaActivity.this, "Manga data doesn't exist..", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Displaying a failure message on toast.
                        Toast.makeText(EditMangaActivity.this, "Fail to update Manga..", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                // Display toast message indicating MangaID is missing.
                Toast.makeText(EditMangaActivity.this, "Manga ID is missing..", Toast.LENGTH_SHORT).show();
            }
        });

        // Adding a click listener for our delete Manga button.
        deleteMangaBtn.setOnClickListener(v -> {
            // Calling a method to delete a Manga.
            deleteManga();
        });
    }

    private void deleteManga() {
        // Removing the Manga value from the database.
        databaseReference.removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Displaying a toast message.
                    Toast.makeText(this, "Manga Deleted..", Toast.LENGTH_SHORT).show();
                    // Opening MainActivity.
                    startActivity(new Intent(EditMangaActivity.this, MainActivity.class));
                })
                .addOnFailureListener(e -> {
                    // Displaying a failure message on toast.
                    Toast.makeText(EditMangaActivity.this, "Fail to delete Manga..", Toast.LENGTH_SHORT).show();
                });
    }
}


