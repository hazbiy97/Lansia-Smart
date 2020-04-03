package com.example.lansiasmart;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SkalaNyeriFragment extends Fragment {
    private Context mContext;
    private UserDataInterface mUserDataInterface;
    private UserData userData;
    private static final String ServerURL = "https://lansiasmart.000webhostapp.com/setNyeriSendi.php";

    private AppCompatSeekBar seekBar;
    private TextView textView;
    private AutoCompleteTextView jointLocationTextView;
    private TextInputLayout jointLocationTextViewLayout;
    private AppCompatButton submitButton;
    private MultiSpinner multiSpinner;
    private int jointPainScale;

    public SkalaNyeriFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userData = mUserDataInterface.getUserData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_skala_nyeri, container, false);


        String[] JOIN_LOCATION = new String[] {"Pergelangan Tangan Kanan", "Pergelangan Tangan Kiri",
                "Pinggang", "Panggul", "Lutut Kanan", "Lutut Kiri", "Pergelangan Kaki Kanan",
                "Pergelangan Kaki Kiri"};

        String[] JOIN_TIME = new String[] {"Pagi Hari", "Siang Hari", "Sore Hari", "Malam Hari", "Sepanjang Malam"};

        seekBar = view.findViewById(R.id.joint_pain_seek_bar);
        jointLocationTextView = view.findViewById(R.id.joint_pain_exposed_dropdown);
//        jointTimeTextView = view.findViewById(R.id.joint_pain_time_exposed_dropdown);
        submitButton = view.findViewById(R.id.jointPainSubmitButton);
        jointLocationTextViewLayout = view.findViewById(R.id.jointPainExposedMenuLayout);
//        jointTimeTextViewLayout = view.findViewById(R.id.jointPainTimeExposedMenuLayout);

        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
//        jointTimeTextView.setAdapter(jointLocationAdapter(JOIN_TIME));
        jointLocationTextView.setAdapter(jointLocationAdapter(JOIN_LOCATION));
        submitButton.setOnClickListener(submitOnClickListener);

        List<String> JOIN_TIME_LIST = Arrays.asList(JOIN_TIME);

        multiSpinner = (MultiSpinner) view.findViewById(R.id.jointPainTimeSpinner);
        multiSpinner.setItems(JOIN_TIME_LIST, getString(R.string.joint_pain_time_input_edit_text), multiSpinnerOnClickListener);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
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

    private ArrayAdapter<String> jointLocationAdapter(String[] JOIN_LOCATION) {
        return new ArrayAdapter<>(
                mContext,
                R.layout.dropdown_menu_popup_item,
                JOIN_LOCATION);
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        int stepSize = 1;
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            progress = ((int)Math.round(progress/stepSize))*stepSize;
            seekBar.setProgress(progress);
            jointPainScale = progress;
//                textView.setText(progress + "");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private View.OnClickListener submitOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String jointLocation = jointLocationTextView.getText().toString();
            String jointTime = multiSpinner.getText();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabase = database.getReference();

            String name = userData.getName().getValue();
            String gender = userData.getGender();
            int age = userData.getAge();
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String formattedDate = df.format(c);

            if (!TextUtils.isEmpty(jointLocation)) { // && !TextUtils.isEmpty(jointTime)

                String message;
                String title;

                if (jointPainScale == 0) {
                    title = "Tidak Nyeri";
                    message = getString(R.string.joint_pain_indicator_no_pain_result);
                }
                else if (jointPainScale >= 1 && jointPainScale <= 3){
                    title = "Nyeri Ringan";
                    message = getString(R.string.joint_pain_indicator_mild_result);
                }
                else if (jointPainScale >= 4 && jointPainScale <= 6) {
                    title = "Nyeri Sedang";
                    message = getString(R.string.joint_pain_indicator_moderate_result);
                }
                else if (jointPainScale >= 7 && jointPainScale <= 9) {
                    title = "Nyeri Berat";
                    message = getString(R.string.joint_pain_indicator_severe_result);
                }
                else if (jointPainScale == 10){
                    title = "Nyeri Sangat Berat";
                    message = getString(R.string.joint_pain_indicator_very_severe_result);
                }
                else {
                    title = "Error";
                    message = "Error #01, please report to us";
                }

                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();

                user.put("Tanggal", formattedDate);
                user.put("Nama", name);
                user.put("Umur", age);
                user.put("Jenis Kelamin", gender);
                user.put("Sendi", jointLocation);
                user.put("Skala", jointPainScale);
                user.put("Waktu", jointTime);

                mDatabase.child("Nyeri Sendi").child(formattedDate).child(name+age+jointLocation).setValue(user);

                InsertData(user, mContext);

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                builder.setMessage(message)
                        .setTitle(title)
                        .setOnDismissListener(dialog -> {
                            final Handler handler = new Handler();
                            handler.postDelayed(() -> ((Activity) mContext).onBackPressed(), getResources().getInteger(android.R.integer.config_shortAnimTime));
                        })
                        .show();
            } else {
                if (TextUtils.isEmpty(jointLocation)) {
                    jointLocationTextViewLayout.setError("Lokasi nyeri sendi tidak boleh kosong");
                }
            }
        }
    };

    private MultiSpinner.MultiSpinnerListener multiSpinnerOnClickListener = new MultiSpinner.MultiSpinnerListener() {
        @Override
        public void onItemsSelected(boolean[] selected){
            Log.i("MultiSpinner", "Test");
        }
    };

    static void InsertData(final Map<String, Object> data, Context mContext){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String DateHolder = data.get("Tanggal").toString();
                String NameHolder = data.get("Nama").toString();
                String AgeHolder = data.get("Umur").toString();
                String GenderHolder = data.get("Jenis Kelamin").toString();
                String TimeHolder = data.get("Waktu").toString();
                String JointHolder = data.get("Sendi").toString();
                String ScaleHolder = data.get("Skala").toString();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("tanggal", DateHolder));
                nameValuePairs.add(new BasicNameValuePair("nama", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("umur", AgeHolder));
                nameValuePairs.add(new BasicNameValuePair("jenisKelamin", GenderHolder));
                nameValuePairs.add(new BasicNameValuePair("waktu", TimeHolder));
                nameValuePairs.add(new BasicNameValuePair("sendi", JointHolder));
                nameValuePairs.add(new BasicNameValuePair("skala", ScaleHolder));

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

//                final Handler handler = new Handler();
//                handler.postDelayed(((Activity) mContext)::onBackPressed, mContext.getResources().getInteger(android.R.integer.config_shortAnimTime));

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute();
    }
}
