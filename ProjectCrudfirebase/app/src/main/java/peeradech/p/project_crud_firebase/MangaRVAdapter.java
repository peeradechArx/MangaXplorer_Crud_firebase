package peeradech.p.project_crud_firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MangaRVAdapter extends RecyclerView.Adapter<MangaRVAdapter.ViewHolder> {
    //creating variables for our list, context, interface and position.
    private final ArrayList<MangaRVModal> MangaRVModalArrayList;
    private final Context context;
    private final MangaClickInterface MangaClickInterface;
    int lastPos = -1;

    //creating a constructor.
    public MangaRVAdapter(ArrayList<MangaRVModal> MangaRVModalArrayList, Context context, MangaClickInterface MangaClickInterface) {
        this.MangaRVModalArrayList = MangaRVModalArrayList;
        this.context = context;
        this.MangaClickInterface = MangaClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating our layout file on below line.
        View view = LayoutInflater.from(context).inflate(R.layout.manga_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //setting data to our recycler view item on below line.
        MangaRVModal MangaRVModal = MangaRVModalArrayList.get(position);
        holder.MangaTV.setText(MangaRVModal.getMangaName());
        holder.MangaPriceTV.setText("Price. " + MangaRVModal.getMangaPrice());
        Picasso.get().load(MangaRVModal.getMangaImg()).into(holder.MangaIV);
        //adding animation to recycler view item on below line.
        setAnimation(holder.itemView, position);
        holder.MangaIV.setOnClickListener(v -> MangaClickInterface.onMangaClick(position));
    }

    private void setAnimation(View itemView, int position) {
        if (position > lastPos) {
            //on below line we are setting animation.
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPos = position;
        }
    }

    @Override
    public int getItemCount() {
        return MangaRVModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //creating variable for our image view and text view on below line.
        private final ImageView MangaIV;
        private final TextView MangaTV;
        private final TextView MangaPriceTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //initializing all our variables on below line.
            MangaIV = itemView.findViewById(R.id.idIVManga);
            MangaTV = itemView.findViewById(R.id.idTVMangaName);
            MangaPriceTV = itemView.findViewById(R.id.idTVCousePrice);
        }
    }

    //creating a interface for on click
    public interface MangaClickInterface {
        void onMangaClick(int position);
    }
}
