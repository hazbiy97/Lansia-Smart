package com.example.lansiasmart;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AktifitasFisikFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AktifitasFisikFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ServerURL = "https://lansiasmart.000webhostapp.com/setAktifitasFisik.php" ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String isDayQuestion = "Pagi";
    private Context mContext;
    private UserDataInterface mUserDataInterface;
    private UserData userData;

    private TextView questionTitleTextView;
    private TextView questionTextView;
    private RadioGroup answerRadioGroup;

    public AktifitasFisikFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AktifitasFisikFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AktifitasFisikFragment newInstance(String param1, String param2) {
        AktifitasFisikFragment fragment = new AktifitasFisikFragment();
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
        userData = mUserDataInterface.getUserData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_aktifitas_fisik, container, false);

        answerRadioGroup = (RadioGroup) view.findViewById(R.id.answerRadioGroup);
        questionTitleTextView = (TextView) view.findViewById(R.id.questionTitle);
        questionTextView = (TextView) view.findViewById(R.id.question);

        RadioGroup toggleRadioGroup = (RadioGroup) view.findViewById(R.id.toggle);
        toggleRadioGroup.setOnCheckedChangeListener(mToggleOnCheckedChangeListener);

        AppCompatButton submitButton = (AppCompatButton) view.findViewById(R.id.aktifitasFisikSubmitButton);
        submitButton.setOnClickListener(mOnClickListener);
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

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabase = database.getReference();

            String name = userData.getName().getValue();
            String gender = userData.getGender();
            int age = userData.getAge();
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String formattedDate = df.format(c);

            String message;
            String title;
            String answer;
            final boolean edukasiNextFragment;

            int answerId = answerRadioGroup.getCheckedRadioButtonId();
            switch (answerId){
                case R.id.answer1:
                    title = getString(R.string.aktifivitasKurangTitle);
                    message = getString(R.string.aktifivitasKurang);
                    answer = getString(R.string.dayAnswer1);
                    edukasiNextFragment = true;
                    break;
                case R.id.answer2:
                    title = getString(R.string.aktifivitasSedangTitle);
                    message = getString(R.string.aktifivitasSedang);
                    answer = getString(R.string.dayAnswer2);
                    edukasiNextFragment = false;
                    break;
                case R.id.answer3:
                    title = getString(R.string.aktifivitasSedangTitle);
                    message = getString(R.string.aktifivitasSedang);
                    answer = getString(R.string.dayAnswer3);
                    edukasiNextFragment = false;
                    break;
                case R.id.answer4:
                    title = getString(R.string.aktifivitasBanyakTitle);
                    message = getString(R.string.aktifivitasBanyak);
                    answer = getString(R.string.dayAnswer4);
                    edukasiNextFragment = false;
                    break;
                default:
                    title = "Error";
                    message = "Error #01, please report to us";
                    answer = "Error #01, please report to us";
                    edukasiNextFragment = false;
                    break;
            }

            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();

            user.put("Nama", name);
            user.put("Umur", age);
            user.put("Jenis Kelamin", gender);
            user.put("Jawaban", answer);
            user.put("Hasil", title);;
            user.put("Deskripsi Hasil", message);
            user.put("Date", formattedDate);
            user.put("Waktu", isDayQuestion);

            mDatabase.child("aktifitasFisik").child(formattedDate).child(name+age+isDayQuestion).setValue(user);
            InsertData(user, mContext);

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

            builder.setMessage(message)
                    .setTitle(title)
                    .setOnDismissListener(dialog -> {
                        final Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            if (edukasiNextFragment) {
                                BottomNavigationView bottomNavigationView;
                                bottomNavigationView = (BottomNavigationView) ((Activity) mContext).findViewById(R.id.nav_view);
                                bottomNavigationView.setSelectedItemId(R.id.navigation_edukasi);

                                mUserDataInterface.showTopLevelFragment(new EdukasiFragment(), "EDUKASI");
                            }
                            else {
                                ((Activity) mContext).onBackPressed();
                            }
                        }, getResources().getInteger(android.R.integer.config_shortAnimTime));
                    })
                    .show();
        }
    };

    private RadioGroup.OnCheckedChangeListener mToggleOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch(checkedId){
                case R.id.dayQuestionSwitch:
                    questionTitleTextView.setText(R.string.dayQuestionTitle);
                    questionTextView.setText(R.string.dayQuestion);
                    ((RadioButton) answerRadioGroup.getChildAt(0)).setText(R.string.dayAnswer1);
                    ((RadioButton) answerRadioGroup.getChildAt(1)).setText(R.string.dayAnswer2);
                    ((RadioButton) answerRadioGroup.getChildAt(2)).setText(R.string.dayAnswer3);
                    ((RadioButton) answerRadioGroup.getChildAt(3)).setText(R.string.dayAnswer4);

                    isDayQuestion = "Pagi";
                    break;
                case R.id.nightQuestionSwitch:
                    questionTitleTextView.setText(R.string.nightQuestionTitle);
                    questionTextView.setText(R.string.nightQuestion);
                    ((RadioButton) answerRadioGroup.getChildAt(0)).setText(R.string.nightAnswer1);
                    ((RadioButton) answerRadioGroup.getChildAt(1)).setText(R.string.nightAnswer2);
                    ((RadioButton) answerRadioGroup.getChildAt(2)).setText(R.string.nightAnswer3);
                    ((RadioButton) answerRadioGroup.getChildAt(3)).setText(R.string.nightAnswer4);

                    isDayQuestion = "Malam";
                    break;
            }
        }
    };

    static void InsertData(final Map<String, Object> data, Context mContext){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String DateHolder = data.get("Date").toString();
                String NameHolder = data.get("Nama").toString();
                String AgeHolder = data.get("Umur").toString();
                String GenderHolder = data.get("Jenis Kelamin").toString();
                String TimeHolder = data.get("Waktu").toString();
                String AnswerHolder = data.get("Jawaban").toString();
                String ResultHolder = data.get("Hasil").toString();
                String DescriptionHolder = data.get("Deskripsi Hasil").toString();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("tanggal", DateHolder));
                nameValuePairs.add(new BasicNameValuePair("nama", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("umur", AgeHolder));
                nameValuePairs.add(new BasicNameValuePair("jenisKelamin", GenderHolder));
                nameValuePairs.add(new BasicNameValuePair("waktu", TimeHolder));
                nameValuePairs.add(new BasicNameValuePair("jawaban", AnswerHolder));
                nameValuePairs.add(new BasicNameValuePair("hasil", ResultHolder));
                nameValuePairs.add(new BasicNameValuePair("deskripsi", DescriptionHolder));

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
