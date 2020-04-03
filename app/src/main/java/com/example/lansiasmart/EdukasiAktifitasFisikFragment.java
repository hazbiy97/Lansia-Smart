package com.example.lansiasmart;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EdukasiAktifitasFisikFragment extends Fragment {
    private static RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Context mContext;
    private static ArrayList<EdukasiCardsDataModel> data;
    private static RecyclerView recyclerView;
    private TextView title;
    private TextView description;

    public EdukasiAktifitasFisikFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edukasi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.edukasiTitle);
        description = view.findViewById(R.id.edukasiDescription);

        title.setText(R.string.edukasi_makanan_title);
        description.setText(R.string.edukasi_makanan_description);

        recyclerView = (RecyclerView) view.findViewById(R.id.edukasiRecyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerViewAdapter = new RecyclerCustomAdapter(data);
        data = new ArrayList<EdukasiCardsDataModel>();

        for (int i = 0; i < EdukasiAktifitasFisikData.titleArray.length; i++) {
            data.add(new EdukasiCardsDataModel(
                    EdukasiAktifitasFisikData.titleArray[i],
                    EdukasiAktifitasFisikData.id_[i],
                    EdukasiAktifitasFisikData.drawableArray[i]
            ));
        }

        recyclerViewAdapter = new RecyclerCustomAdapter(data);
        ((RecyclerCustomAdapter) recyclerViewAdapter).setOnCLickListener(myOnClickListener);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    private View.OnClickListener myOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int selectedItemPosition = recyclerView.getChildAdapterPosition(v);
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(selectedItemPosition);
            CardView cv = viewHolder.itemView.findViewById(R.id.cardView);

            Intent intent = new Intent(((Activity) mContext), PdfRenderActivity.class);;
            int id = (int) cv.getTag();
            switch (id) {
                case 0:
                    intent = new Intent(((Activity) mContext), PdfRenderActivity.class);
                    intent.putExtra("pdfFilename","latihan_peregangan_sendi.pdf");
                    break;
                case 1:
                    intent = new Intent(((Activity) mContext), YoutubePlayerActivity.class);
//                    intent.putExtra("videoFilename",R.raw.relaksasi_peregangan_sendi_video);
                    intent.putExtra("videoUrl", getString(R.string.relaksasi_peregangan_sendi_link));
                    break;
            }
            startActivity(intent);
        }
    };
}
