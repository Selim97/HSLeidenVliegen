package ikpmd.dursun.hsleidenvliegen;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

// Fungeert als adapter tussen de Firebase Database en de Recycle View in een 'single row'
public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder>{
    ArrayList<TravelDeal> deals;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ImageView imageDeal;

    // 'Luistert' wanneer er een nieuwe item wordt toegevoegd, vervolgens wordt deze getoond
    private ChildEventListener mChildListener;

    public DealAdapter() {
        // Creeert de reference en vult het in
        //FirebaseUtil.openFbReference("traveldeals");
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        this.deals = FirebaseUtil.mDeals;
        mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                // DataSnapshot bevat data uit de Firebase Database, elke keer dat je data uitleest
                // krijgt de gebruiker dat als een DataSnapshot
                // Door de getValue methode wordt de data geserilazed en wordt het in de TravelDeal klasse geplaatst
                TravelDeal td = dataSnapshot.getValue(TravelDeal.class);
                Log.d("Deal: ", td.getTitle());

                // Id van de vlucht wordt gezet naar pushId de gegenereerde id door Firebase, kan later makkelijker opgevraagd worden
                td.setId(dataSnapshot.getKey());

                // Voegt aan de deals array de meegegeven item
                deals.add(td);

                // Toont dat er een item is toegevoegd zodat de UI wordt geupdate
                notifyItemInserted(deals.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildListener);
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.rv_row, parent, false);
        return new DealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        TravelDeal deal = deals.get(position);
        holder.bind(deal);
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public class DealViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        TextView tvTitle;
        TextView tvDescription;
        TextView tvPrice;

        public DealViewHolder(@NonNull View itemView) {
            super(itemView);

            // Om de bestemming, beschrijving en prijs van de vlucht te vinden
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);

            imageDeal = (ImageView) itemView.findViewById(R.id.imageDeal);

            itemView.setOnClickListener(this);

        }

        // Neemt een vlucht als parameter en plaatst de Titel (van de vlucht) in TextView
        public void bind(TravelDeal deal) {
            tvTitle.setText(deal.getTitle());
            tvDescription.setText(deal.getDescription());
            tvPrice.setText(deal.getPrice());
            showImage(deal.getImageUrl());

        }

        @Override
        public void onClick(View view) {
            // Haalt de positie van de item op
            int position = getAdapterPosition();
            Log.d("Click", String.valueOf(position));

            TravelDeal selectedDeal = deals.get(position);
            Intent intent = new Intent(view.getContext(), DealActivity.class);
            intent.putExtra("Deal", selectedDeal);
            view.getContext().startActivity(intent);
        }

        private void showImage(String url) {
            // Controleert of er een url bestaat en of deze leeg is of niet
            if (url != null && url.isEmpty() == false) {
                // Vult Picasso in met de afbeelding als thumbnail, hoogte en breedte zijn 160
                Picasso.with(imageDeal.getContext())
                        .load(url)
                        .resize(160,160)
                        .centerCrop()
                        .into(imageDeal);


            }
        }
    }

}
