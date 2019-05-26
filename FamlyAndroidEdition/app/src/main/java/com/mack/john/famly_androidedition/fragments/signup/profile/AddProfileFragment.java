package com.mack.john.famly_androidedition.fragments.signup.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.NavigationActivity;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.objects.account.profile.Parent;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;
import com.mack.john.famly_androidedition.signup.profile.child.AddChild1Activity;
import com.mack.john.famly_androidedition.signup.profile.parent.AddParent1Activity;
import com.mack.john.famly_androidedition.utils.AccountUtils;
import com.mack.john.famly_androidedition.utils.ButtonUtils;

public class AddProfileFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "AddProfileFragment";

    Account mAccount;



    // System generated methods
    public static AddProfileFragment newInstance() {
        Bundle args = new Bundle();

        AddProfileFragment fragment = new AddProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_profile, container, false);

        mAccount = AccountUtils.loadAccount(getActivity());

        setClickListener(view);
        toggleParentButton(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_add_parent) {
            Intent addParentIntent = new Intent(getActivity(), AddParent1Activity.class);
            startActivity(addParentIntent);
        }

        else if(view.getId() == R.id.button_add_child) {
            Intent addChildIntent = new Intent(getActivity(), AddChild1Activity.class);
            startActivity(addChildIntent);
        }

        else if(view.getId() == R.id.button_finish) {
            Intent finishIntent = new Intent(getActivity(), NavigationActivity.class);
            startActivity(finishIntent);
        }
    }



    // Custom methods
    private void setClickListener(View view) {
        Button addParentButton = view.findViewById(R.id.button_add_parent);
        Button addChildButton = view.findViewById(R.id.button_add_child);
        Button finishButton = view.findViewById(R.id.button_finish);

        addParentButton.setOnClickListener(this);
        addChildButton.setOnClickListener(this);
        finishButton.setOnClickListener(this);
    }

    private void toggleParentButton(View view) {
        int parentCount = 0;

        for(Profile profile : mAccount.getProfiles()) {
            if(profile instanceof Parent) {
                parentCount += 1;

                if(parentCount == 2) {
                    ButtonUtils.disableParentButton(getActivity(), view);
                    break;
                }

                else {
                    ButtonUtils.enableParentButton(getActivity(), view);
                }
            }
        }
    }
}
