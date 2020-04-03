package com.example.lansiasmart;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KebutuhanKaloriFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KebutuhanKaloriFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ServerURL = "https://lansiasmart.000webhostapp.com/setKebutuhanKalori.php" ;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private float height;
    private float weight;
    private float imt;
    private float bbi;
    private float bmr;
    private float akg;
    private UserData userData;
    private UserDataInterface mUserDataInterface;
    private Context mContext;

    private TextInputLayout heightTextInputEditTextLayout;
    private TextInputEditText heightTextInputEditText;
    private TextInputLayout weightTextInputEditTextLayout;
    private TextInputEditText weightTextInputEditText;
    private TextView imtTextView;
    private TextView bbiTextView;
    private TextView bmrTextView;
    private TextView akgTextView;
    private TextView categoryResult;
    private TextView categoryResultDesc;
    private TextView normalWeight;
    private AppCompatButton hitungMaterialButton;

    public KebutuhanKaloriFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KebutuhanKaloriFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KebutuhanKaloriFragment newInstance(String param1, String param2) {
        KebutuhanKaloriFragment fragment = new KebutuhanKaloriFragment();
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
        View view = inflater.inflate(R.layout.fragment_kebutuhan_kalori, container, false);

        heightTextInputEditTextLayout = (TextInputLayout) view.findViewById(R.id.heightEditTextLayout);
        heightTextInputEditText = (TextInputEditText) view.findViewById(R.id.heightEditText);
        weightTextInputEditTextLayout = (TextInputLayout) view.findViewById(R.id.weightEditTextLayout);
        weightTextInputEditText = (TextInputEditText) view.findViewById(R.id.weightEditText);
        imtTextView = (TextView) view.findViewById(R.id.imtDesc);
        bbiTextView = (TextView) view.findViewById(R.id.bbiDesc);
        bmrTextView = (TextView) view.findViewById(R.id.bmrDesc);
        akgTextView = (TextView) view.findViewById(R.id.akgDesc);
        categoryResult = (TextView) view.findViewById(R.id.categoryResultTitle);
        categoryResultDesc = (TextView) view.findViewById(R.id.categoryResultDesc);
        normalWeight = (TextView) view.findViewById(R.id.normalWeightDesc);
        hitungMaterialButton = (AppCompatButton) view.findViewById((R.id.kebutuhanKaloriButton));

        heightTextInputEditText.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        weightTextInputEditText.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        hitungMaterialButton.setOnClickListener(mOnClickListener);

        userData = mUserDataInterface.getUserData();
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

    private class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabase = database.getReference();

            String heightInput = heightTextInputEditText.getText().toString();
            String weightInput = weightTextInputEditText.getText().toString();

            String name = userData.getName().getValue();
            String gender = userData.getGender();
            int age = userData.getAge();

            if (!TextUtils.isEmpty(heightInput) && !TextUtils.isEmpty(weightInput)) {
                height = Integer.parseInt(heightInput);
                weight = Integer.parseInt(weightInput);

                imt = (weight * 10000) / (height * height);

                float upperLimitWeight = (24.9f / 10000) * (height * height);
                float floorLimitWeight = (18.5f / 10000) * (height * height);
                float overWeightDifference = weight - upperLimitWeight;
                float underWeightDifference = floorLimitWeight - weight;

                DecimalFormat df = new DecimalFormat("#.#");
                df.setRoundingMode(RoundingMode.CEILING);

                String overWeightOutput = "+ " + df.format(overWeightDifference) + " kg";
                String underWeightOutput = "- " + df.format(underWeightDifference) + " kg";
                String normalWeightOutput = df.format(floorLimitWeight) + " - " + df.format(upperLimitWeight);

                normalWeight.setText(normalWeightOutput);

                if (imt < 18.5) {
                    categoryResult.setText(R.string.categoryKurus);
                    categoryResult.setTextColor(ContextCompat.getColor(mContext, R.color.errorColor));
                    categoryResultDesc.setText(underWeightOutput);
                    categoryResultDesc.setTextColor(ContextCompat.getColor(mContext, R.color.errorColor));
                }
                else if (imt >= 18.5 && imt < 25) {
                    categoryResult.setText(R.string.categoryNormal);
                    categoryResult.setTextColor(ContextCompat.getColor(mContext, R.color.primaryColor));
                    categoryResultDesc.setText("\u2713");
                    categoryResultDesc.setTextColor(ContextCompat.getColor(mContext, R.color.primaryColor));
                }
                else if (imt >= 25 && imt < 30) {
                    categoryResult.setText(R.string.categoryGemuk);
                    categoryResult.setTextColor(ContextCompat.getColor(mContext, R.color.errorColor));
                    categoryResultDesc.setText(overWeightOutput);
                    categoryResultDesc.setTextColor(ContextCompat.getColor(mContext, R.color.errorColor));
                }
                else {
                    categoryResult.setText(R.string.categoryObesitas);
                    categoryResult.setTextColor(ContextCompat.getColor(mContext, R.color.errorColor));
                    categoryResultDesc.setText(overWeightOutput);
                    categoryResultDesc.setTextColor(ContextCompat.getColor(mContext, R.color.errorColor));
                }

                bbi = (height - 100) - 10 * (height - 100) / 100;

                if (gender.equals(getString(R.string.gender_input_edit_text_male))) {
                    bmr = (13.5f * bbi) + 487;
                    akg = 1.56f * bmr;
                }
                else if (gender.equals(getString(R.string.gender_input_edit_text_female))){
                    bmr = (10.5f * bbi) + 596;
                    akg = 1.55f * bmr;
                }
                else {
                    throw new Error("Checking gender error");
                }

                imtTextView.setText(String.valueOf(imt));
                bbiTextView.setText(String.valueOf(bbi));
                bmrTextView.setText(String.valueOf(bmr));
                akgTextView.setText(String.valueOf(akg));

                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();

                user.put("Nama", name);
                user.put("Umur", age);
                user.put("Jenis Kelamin", gender);
                user.put("IMT", imt);
                user.put("BBI", bbi);
                user.put("BMR", bmr);
                user.put("AKG", akg);
                user.put("Tinggi Badan", height);
                user.put("Berat Badan", weight);

                mDatabase.child("kebutuhanKalori").child(name+age).setValue(user);

                InsertData(user, mContext);
            }
            else {
                if (TextUtils.isEmpty(heightInput)) {
                    heightTextInputEditTextLayout.setError("Tinggi badan tidak boleh kosong");
                }
                if (TextUtils.isEmpty(weightInput)) {
                    weightTextInputEditTextLayout.setError("Berat badan tidak boleh kosong");
                }
            }
        }
    };

    static void InsertData(final Map<String, Object> data, Context mContext){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = data.get("Nama").toString();
                String AgelHolder = data.get("Umur").toString();
                String genderHolder = data.get("Jenis Kelamin").toString();
                String heightHolder = data.get("Tinggi Badan").toString();
                String weightHolder = data.get("Berat Badan").toString();
                String imtHolder = data.get("IMT").toString();
                String bbiHolder = data.get("BBI").toString();
                String bmrHolder = data.get("BMR").toString();
                String akgHolder = data.get("AKG").toString();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("nama", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("umur", AgelHolder));
                nameValuePairs.add(new BasicNameValuePair("jenisKelamin", genderHolder));
                nameValuePairs.add(new BasicNameValuePair("tinggi", heightHolder));
                nameValuePairs.add(new BasicNameValuePair("berat", weightHolder));
                nameValuePairs.add(new BasicNameValuePair("imt", imtHolder));
                nameValuePairs.add(new BasicNameValuePair("bbi", bbiHolder));
                nameValuePairs.add(new BasicNameValuePair("bmr", bmrHolder));
                nameValuePairs.add(new BasicNameValuePair("akg", akgHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ServerURL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();

                    int code = httpResponse.getStatusLine().getStatusCode();
                    if (code == HttpURLConnection.HTTP_OK) {
                        return "Data Submitted Successfully";
                    } else {
                        return "Data Failed to Submit";
                    }
                } catch (ClientProtocolException e) {
                    return "Data Submit Failed: " + e.getMessage();
                } catch (IOException e) {
                    return "Data Submit Failed: " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

                Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute();
    }
}
