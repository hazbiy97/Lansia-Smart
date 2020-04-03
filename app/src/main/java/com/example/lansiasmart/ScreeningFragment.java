package com.example.lansiasmart;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScreeningFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScreeningFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean cardViewEnabled;

    private MaterialCardView card1;
    private MaterialCardView card2;
    private MaterialCardView card3;

    private FloatingActionButton floatingActionButton;

    private TextView userNameTextView;

    private Context mContext;

    private UserData userData;

    private UserDataInterface mUserDataInterface;

    public ScreeningFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScreeningFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScreeningFragment newInstance(String param1, String param2) {
        ScreeningFragment fragment = new ScreeningFragment();
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
        View view = inflater.inflate(R.layout.fragment_screening, container, false);

        card1 = (MaterialCardView) view.findViewById(R.id.card1);
        card2 = (MaterialCardView) view.findViewById(R.id.card2);
        card3 = (MaterialCardView) view.findViewById(R.id.card3);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab1);
        userNameTextView = (TextView) view.findViewById(R.id.textCardUser);

        userData = mUserDataInterface.getUserData();

        card1.setOnClickListener(mCardsOnClickListener);
        card2.setOnClickListener(mCardsOnClickListener);
        card3.setOnClickListener(mCardsOnClickListener);
        floatingActionButton.setOnClickListener(mFABOnClickListener);

        onUserDataChanged(userData);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mUserDataInterface = (UserDataInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement UserDataInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mContext = null;
        this.mUserDataInterface = null;
    }

    public void onUserDataChanged (UserData userData)
    {
//        Log.i("Live Data using getValu", userData.getTitle().getValue());
        String userName = userData.getName().getValue();
        if (userData.getName() != null && userName != null) {
            enableCardView(true);

            String userNameText = "Hi "+ userName + "!";
            userNameTextView.setText(userNameText);
        } else {
            enableCardView(false);

            String userNameText = "Pasien belum terdaftar";
            userNameTextView.setText(userNameText);
        }
    }

    public void enableCardView (boolean enable) {
        // disable card if no title
        if (enable) {
            card1.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.card1Color));
            card2.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.card2Color));
            card3.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.card3Color));
        } else {
            card1.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.disabledCardColor));
            card2.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.disabledCardColor));
            card3.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.disabledCardColor));
        }

        cardViewEnabled = enable;
    };

    private void fragmentChanger (int cardId)
    {
        Fragment fragment = new Fragment();

        switch (cardId) {
            case R.id.card1:
                fragment = new KebutuhanKaloriFragment();
                break;
            case R.id.card2:
                fragment = new AktifitasFisikFragment();
                break;
            case R.id.card3:
                fragment = new SkalaNyeriFragment();
                break;
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                R.anim.slide_in_right, R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.fragment_host, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private View.OnClickListener mCardsOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            final int cardId = v.getId();

            if (cardViewEnabled){
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                builder.setMessage("Pasien terdaftar memiliki data sebagai berikut: \nNama: " + userData.getName().getValue() + "\nUmur: " + userData.getAge() + " tahun\nJenis Kelamin: " + userData.getGender() + "\n\n Apakah data sudah benar?")
                        .setTitle("Konfirmasi Data Pasien")
                        .setPositiveButton("Benar", mOnDialogClickListener(cardId))
                        .setNegativeButton("Ubah Data", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                fragmentChanger(cardId);
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                builder.setMessage(R.string.screening_no_user_dialog)
                        .setTitle(R.string.screening_no_user_title)
                        .setPositiveButton(R.string.confirm, null);

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        }
    };

    private View.OnClickListener mFABOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            UserDataBottomSheetFragment userDataBottomSheetFragment = UserDataBottomSheetFragment.newInstance();
            userDataBottomSheetFragment.show(getFragmentManager(),
                    UserDataBottomSheetFragment.TAG);
        }
    };

    private DialogInterface.OnClickListener mOnDialogClickListener (final int cardId) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fragmentChanger(cardId);
                    }
                }, getResources().getInteger(android.R.integer.config_shortAnimTime));
            }
        };
    }
}
