package com.mack.john.crypjoy_androidedition.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mack.john.crypjoy_androidedition.CreateAccountActivity2;
import com.mack.john.crypjoy_androidedition.MainActivity;
import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.utilities.InputUtils;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

public class CreateAccountFragment1 extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {



    // Class properties
    public static final String TAG = "CreateAccountFragment1";

    public static final String EXTRA_FIRST_NAME = "FIRST_NAME";
    public static final String EXTRA_LAST_NAME = "LAST_NAME";

    String mFirstName;
    String mLastName;

    private boolean mValidFirstName = false;
    private boolean mValidLastName = false;

    View mView;



    // System generated methods
    public static CreateAccountFragment1 newInstance() {
        Bundle args = new Bundle();

        CreateAccountFragment1 fragment = new CreateAccountFragment1();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_account1, container, false);

        mView = view;

        setClickListener(view);
        setTextChangeListener(view);
        setKeyboardListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_next) {
            if(mValidFirstName && mValidLastName) {
                Intent nextIntent = new Intent(getActivity(), CreateAccountActivity2.class);

                nextIntent.putExtra(EXTRA_FIRST_NAME, mFirstName);
                nextIntent.putExtra(EXTRA_LAST_NAME, mLastName);

                startActivity(nextIntent);
                getActivity().finish();
            }

            else {
                Toast.makeText(getActivity(), getString(R.string.invalid_input), Toast.LENGTH_SHORT).show();
            }
        }

        else if(view.getId() == R.id.button_cancel) {
            Intent cancelIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(cancelIntent);
            getActivity().finish();
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        TextView header1 = mView.findViewById(R.id.header1);
        TextView header2 = mView.findViewById(R.id.header2);
        ImageView heartIcon = mView.findViewById(R.id.heart_icon);
        ImageView background = mView.findViewById(R.id.background);

        if((view.getId() == R.id.input_firstName && hasFocus) || (view.getId() == R.id.input_lastName && hasFocus)) {
            header1.setVisibility(View.GONE);
            header2.setVisibility(View.GONE);
            heartIcon.setVisibility(View.GONE);
            background.setVisibility(View.INVISIBLE);
        }

        else {
            header1.setVisibility(View.VISIBLE);
            header2.setVisibility(View.VISIBLE);
            heartIcon.setVisibility(View.VISIBLE);
            background.setVisibility(View.VISIBLE);
        }
    }




    // Custom methods
    private void setClickListener(View view) {
        Button cancelButton = view.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(this);

        Button nextButton = view.findViewById(R.id.button_next);
        nextButton.setOnClickListener(this);
    }

    private void setTextChangeListener(View view) {
        final EditText firstNameInput = view.findViewById(R.id.input_firstName);
        firstNameInput.setOnFocusChangeListener(this);
        firstNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(InputUtils.validName(firstNameInput.getText().toString()) == true) {
                    firstNameInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputValid)));
                    mFirstName = firstNameInput.getText().toString();

                    mValidFirstName = true;
                }

                else {
                    firstNameInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputInvalid)));
                    mValidFirstName = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        final EditText lastNameInput = view.findViewById(R.id.input_lastName);
        lastNameInput.setOnFocusChangeListener(this);
        lastNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(InputUtils.validName(lastNameInput.getText().toString()) == true) {
                    lastNameInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputValid)));
                    mLastName = lastNameInput.getText().toString();

                    mValidLastName = true;
                }

                else {
                    lastNameInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputInvalid)));
                    mValidLastName = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setKeyboardListener(View v) {
        final View view = v;

        KeyboardVisibilityEvent.setEventListener(getActivity(), new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if(!isOpen) {
                    EditText firstNameInput = view.findViewById(R.id.input_firstName);
                    EditText lastNameInput = view.findViewById(R.id.input_lastName);

                    firstNameInput.clearFocus();
                    lastNameInput.clearFocus();
                }
            }
        });
    }
}
