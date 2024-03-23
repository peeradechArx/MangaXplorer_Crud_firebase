package peeradech.p.project_crud_firebase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements peeradech.p.project_crud_firebase.MangaRVAdapter.MangaClickInterface {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;
    private ArrayList<MangaRVModal> MangaRVModalArrayList;
    private peeradech.p.project_crud_firebase.MangaRVAdapter MangaRVAdapter;
    private RelativeLayout homeRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Write a message to the database

        //initializing all our variables.
        RecyclerView MangaRV = findViewById(R.id.idRVMangas);
        homeRL = findViewById(R.id.idRLHome);
        loadingPB = findViewById(R.id.idPBLoading);
        //creating variables for fab, firebase database, progress bar, list, adapter,firebase auth, recycler view and relative layout.
        FloatingActionButton addMangaFAB = findViewById(R.id.idFABAddManga);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        MangaRVModalArrayList = new ArrayList<>();
        //on below line we are getting database reference.
        databaseReference = firebaseDatabase.getReference("Mangas");
        //on below line adding a click listener for our floating action button.
        addMangaFAB.setOnClickListener(v -> {
            //opening a new activity for adding a Manga.
            Intent i = new Intent(MainActivity.this, AddMangaActivity.class);
            startActivity(i);
        });
        //on below line initializing our adapter class.
        MangaRVAdapter = new MangaRVAdapter(MangaRVModalArrayList, this, this);
        //setting layout malinger to recycler view on below line.
        MangaRV.setLayoutManager(new LinearLayoutManager(this));
        //setting adapter to recycler view on below line.
        MangaRV.setAdapter(MangaRVAdapter);
        //on below line calling a method to fetch Mangas from database.
        getMangas();
    }

    private void getMangas() {
        // Clear the list before adding new data.
        MangaRVModalArrayList.clear();

        // Add a ValueEventListener instead of ChildEventListener to get the initial data.
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loadingPB.setVisibility(View.GONE);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Check if the data has been changed or added.
                    // If the data already exists, update it. Otherwise, add it to the list.
                    MangaRVModal modal = dataSnapshot.getValue(MangaRVModal.class);
                    int index = getMangaIndex(modal.getMangaId());
                    if (index != -1) {
                        // Data already exists, update it.
                        MangaRVModalArrayList.set(index, modal);
                        // Notify adapter about the change in the item.
                        MangaRVAdapter.notifyItemChanged(index);
                    } else {
                        // Data is new, add it to the list.
                        MangaRVModalArrayList.add(modal);
                        // Notify adapter about the insertion of a new item.
                        MangaRVAdapter.notifyItemInserted(MangaRVModalArrayList.size() - 1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event if necessary.
            }
        });
    }

    // Method to get the index of a Manga in the list based on its id.
    private int getMangaIndex(String MangaId) {
        for (int i = 0; i < MangaRVModalArrayList.size(); i++) {
            if (MangaRVModalArrayList.get(i).getMangaId().equals(MangaId)) {
                return i;
            }
        }
        return -1; // Return -1 if the Manga is not found.
    }


    @Override
    public void onMangaClick(int position) {
        //calling a method to display a bottom sheet on below line.
        displayBottomSheet(MangaRVModalArrayList.get(position));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //adding a click listner for option selected on below line.
        int id = item.getItemId();
        if (id == R.id.idLogOut) {//displaying a toast message on user logged out inside on click.
            Toast.makeText(getApplicationContext(), "User Logged Out", Toast.LENGTH_LONG).show();
            //on below line we are signing out our user.
            mAuth.signOut();
            //on below line we are opening our login activity.
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //on below line we are inflating our menu file for displaying our menu options.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void displayBottomSheet(MangaRVModal modal) {
        //on below line we are creating our bottom sheet dialog.
        final BottomSheetDialog bottomSheetTeachersDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        //on below line we are setting content view for bottom sheet dialog.
        bottomSheetTeachersDialog.setContentView(R.layout.bottom_sheet_layout);
        //on below line we are setting a cancelable
        bottomSheetTeachersDialog.setCancelable(false);
        bottomSheetTeachersDialog.setCanceledOnTouchOutside(true);
        //calling a method to display our bottom sheet.
        bottomSheetTeachersDialog.show();
        //on below line we are creating variables for our text view and image view inside bottom sheet
        //and initialing them with their ids.
        TextView MangaNameTV = bottomSheetTeachersDialog.findViewById(R.id.idTVMangaName);
        TextView MangaDescTV = bottomSheetTeachersDialog.findViewById(R.id.idTVMangaDesc);
        TextView suitedForTV = bottomSheetTeachersDialog.findViewById(R.id.idTVSuitedFor);
        TextView priceTV = bottomSheetTeachersDialog.findViewById(R.id.idTVMangaPrice);
        ImageView MangaIV = bottomSheetTeachersDialog.findViewById(R.id.idIVManga);
        //on below line we are setting data to different views on below line.
        MangaNameTV.setText(modal.getMangaName());
        MangaDescTV.setText(modal.getMangaDescription());
        // ใช้ Resource String และ Placeholders ในการกำหนดข้อความให้กับ TextView
        suitedForTV.setText(getString(R.string.suited_for_placeholder, modal.getBestSuitedFor()));
        priceTV.setText(getString(R.string.price_placeholder, modal.getMangaPrice()));
        Picasso.get().load(modal.getMangaImg()).into(MangaIV);
        Button viewBtn = bottomSheetTeachersDialog.findViewById(R.id.idBtnVIewDetails);
        Button editBtn = bottomSheetTeachersDialog.findViewById(R.id.idBtnEditManga);

        //adding on click listener for our edit button.
        editBtn.setOnClickListener(v -> {
            //on below line we are opening our EditMangaActivity on below line.
            Intent i = new Intent(MainActivity.this, EditMangaActivity.class);
            //on below line we are passing our Manga modal
            i.putExtra("Manga", modal);
            startActivity(i);
        });
        //adding click listener for our view button on below line.
        viewBtn.setOnClickListener(v -> {
            //on below line we are navigating to browser for displaying Manga details from its url
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(modal.getMangaLink()));
            startActivity(i);
        });
    }


}