package com.example.lansiasmart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
 * Use the {@link EdukasiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EdukasiFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Context mContext;
    private static ArrayList<EdukasiCardsDataModel> data;

    private static RecyclerView recyclerView;

    public EdukasiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EdukasiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EdukasiFragment newInstance(String param1, String param2) {
        EdukasiFragment fragment = new EdukasiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        recyclerView = (RecyclerView) view.findViewById(R.id.edukasiRecyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerViewAdapter = new RecyclerCustomAdapter(data);
        data = new ArrayList<EdukasiCardsDataModel>();

        for (int i = 0; i < EdukasiCardsData.titleArray.length; i++) {
            data.add(new EdukasiCardsDataModel(
                    EdukasiCardsData.titleArray[i],
                    EdukasiCardsData.id_[i],
                    EdukasiCardsData.drawableArray[i]
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
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForAdapterPosition(selectedItemPosition);
            TextView textViewName
                    = (TextView) viewHolder.itemView.findViewById(R.id.textCardTitleLayout);

            String selectedName = (String) textViewName.getText();
            int selectedItemId = -1;
            for (int i = 0; i < EdukasiCardsData.titleArray.length; i++) {
                if (selectedName.equals(EdukasiCardsData.titleArray[i])) {
                    selectedItemId = EdukasiCardsData.id_[i];
                }
            }
            Intent intent = new Intent(((Activity) mContext), PdfRenderActivity.class);
            Fragment fragment = new Fragment();
            switch (selectedItemId) {
                case 0:
                    fragment = new EdukasiMakananSehatFragment();
                    fragmentChanger(fragment);
                    break;
                case 1:
                    fragment = new EdukasiAktifitasFisikFragment();
                    fragmentChanger(fragment);
                    break;
                case 2:
                    intent = new Intent(((Activity) mContext), PdfRenderActivity.class);
                    intent.putExtra("pdfFilename","lingkungan_rumah_aman.pdf");
                    startActivity(intent);
                    break;
                case 3:
//                    intent = new Intent(((Activity) mContext), VideoPlayerActivity.class);
                    intent = new Intent(((Activity) mContext), YoutubePlayerActivity.class);
                    intent.putExtra("videoUrl", getString(R.string.relaksasi_nafas_dalam_link));
                    startActivity(intent);
                    break;
                case 4:
                    intent = new Intent(((Activity) mContext), PdfRenderActivity.class);
                    intent.putExtra("pdfFilename","edukasi_covid.pdf");
                    startActivity(intent);
                    break;
            }
        }
    };

    private void fragmentChanger (Fragment fragment)
    {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                R.anim.slide_in_right, R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.fragment_host, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
