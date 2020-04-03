package com.example.lansiasmart;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerCustomAdapter extends RecyclerView.Adapter<RecyclerCustomAdapter.MyViewHolder> {
    private ArrayList<EdukasiCardsDataModel> dataSet;
    private View.OnClickListener onClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        ImageView imageViewIcon;
        protected CardView cv;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.cv = (CardView) itemView.findViewById(R.id.cardView);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.textCardTitleLayout);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageViewCardLayout);
        }
    }

    public RecyclerCustomAdapter(ArrayList<EdukasiCardsDataModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edukasi_cards_layout, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        holder.cv.setTag(dataSet.get(listPosition).getId());
        TextView textViewName = holder.textViewTitle;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(dataSet.get(listPosition).getTitle());
//        Picasso.get().load(dataSet.get(listPosition).getImage()).into(imageView);
        imageView.setImageResource(dataSet.get(listPosition).getImage());

        if (onClickListener != null) {
            holder.itemView.setOnClickListener(onClickListener);
        }


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setOnCLickListener (View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
