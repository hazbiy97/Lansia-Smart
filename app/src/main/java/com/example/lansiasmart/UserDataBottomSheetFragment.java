package com.example.lansiasmart;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

public class UserDataBottomSheetFragment extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";

    private Context mContext;
    private UserDataInterface userDataInterface;

    private TextInputLayout nameInputEditTextInputLayout;
    private AppCompatEditText nameInputEditText;
    private TextInputLayout ageInputEditTextInputLayout;
    private AppCompatEditText ageInputEditText;
    private TextInputLayout genderInputEditTextInputLayout;
    private AppCompatButton userDataSubmitButton;
    private AutoCompleteTextView editTextGenderExposedDropdown;

    public UserDataBottomSheetFragment() {
        // Required empty public constructor
    }

    public static UserDataBottomSheetFragment newInstance() {
        UserDataBottomSheetFragment fragment = new UserDataBottomSheetFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_data_bottom_sheet, container, false);

        nameInputEditTextInputLayout = view.findViewById(R.id.nameInputEditTextLayout);
        nameInputEditText = view.findViewById(R.id.nameInputEditText);
        ageInputEditTextInputLayout = view.findViewById(R.id.ageInputEditTextLayout);
        ageInputEditText = view.findViewById(R.id.ageInputEditText);
        genderInputEditTextInputLayout = view.findViewById(R.id.genderExposedMenuLayout);
        editTextGenderExposedDropdown = view.findViewById(R.id.gender_exposed_dropdown);
        userDataSubmitButton = view.findViewById(R.id.userDataSubmit);

        ageInputEditText.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        editTextGenderExposedDropdown.setAdapter(genderInputAdapter());
        userDataSubmitButton.setOnClickListener(mOnClickListener);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            userDataInterface = (UserDataInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement UserDataInterface");
        }
    }

    @NonNull @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        // Do something with your dialog like setContentView() or whatever
        return dialog;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = nameInputEditText.getText().toString();
            String age = ageInputEditText.getText().toString();
            String gender = editTextGenderExposedDropdown.getText().toString();

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(gender)) {
                int ageInteger = Integer.parseInt(age);

                userDataInterface.setUserData(name, ageInteger, gender);
                dismiss();
            } else {
                if (TextUtils.isEmpty(name)) {
                    nameInputEditTextInputLayout.setError("Nama tidak boleh kosong");
                }
                if (TextUtils.isEmpty(age)) {
                    ageInputEditTextInputLayout.setError("Umur tidak boleh kosong");
                }
                if (TextUtils.isEmpty(gender)) {
                    genderInputEditTextInputLayout.setError("Jenis kelamin tidak boleh kosong");
                }
            }

        }
    };

    private ArrayAdapter<String> genderInputAdapter() {
        String[] GENDER = new String[] {getString(R.string.gender_input_edit_text_male), getString(R.string.gender_input_edit_text_female)};

        return new ArrayAdapter<>(
                        mContext,
                        R.layout.dropdown_menu_popup_item,
                        GENDER);
    }

    private class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }
}
