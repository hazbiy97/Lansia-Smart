package com.example.lansiasmart;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KonselingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KonselingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private TextView versionTextView;
    private TextView contactNumberTextView;
    private TextView contactNumberTextView2;

    public KonselingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KonselingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KonselingFragment newInstance(String param1, String param2) {
        KonselingFragment fragment = new KonselingFragment();
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
        View view = inflater.inflate(R.layout.fragment_konsultasi, container, false);

        String versionName = BuildConfig.VERSION_NAME;
        versionName = "V " + versionName;

        versionTextView = (TextView) view.findViewById(R.id.appVersion);
        contactNumberTextView = (TextView) view.findViewById(R.id.contactNumber);
        contactNumberTextView2 = (TextView) view.findViewById(R.id.contactNumber2);

        String contactNumber = getString(R.string.contact_number);
        String contactNumber2 = getString(R.string.contact_number2);
        String formattedNumber = PhoneNumberUtils.formatNumber(contactNumber, "IDN");
        String formattedNumber2 = PhoneNumberUtils.formatNumber(contactNumber2, "IDN");
        String whatsappUrlHtml = "<a href=\"https://wa.me/" + contactNumber.replaceAll("[^0-9]", "") + "\">" + formattedNumber + "</a>";;
        String whatsappUrlHtml2 = "<a href=\"https://wa.me/" + contactNumber2.replaceAll("[^0-9]", "") + "\">" + formattedNumber2 + "</a>";;

        contactNumberTextView.setText(HtmlCompat.fromHtml(whatsappUrlHtml, HtmlCompat.FROM_HTML_MODE_LEGACY));
        contactNumberTextView.setMovementMethod(new LinkMovementMethod());
        contactNumberTextView.setLinkTextColor(ContextCompat.getColor(mContext, R.color.primaryColor));
        contactNumberTextView2.setText(HtmlCompat.fromHtml(whatsappUrlHtml2, HtmlCompat.FROM_HTML_MODE_LEGACY));
        contactNumberTextView2.setMovementMethod(new LinkMovementMethod());
        contactNumberTextView2.setLinkTextColor(ContextCompat.getColor(mContext, R.color.primaryColor));
        versionTextView.setText(versionName);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }
}
