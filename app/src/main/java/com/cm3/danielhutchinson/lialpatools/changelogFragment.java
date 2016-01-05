package com.cm3.danielhutchinson.lialpatools;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


public class changelogFragment extends Fragment {
    changelog  Changelog_class= new changelog();
    String about_changelog = "";
    TextView About_changelog_txtview;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_changelog,
                container, false);
        about_changelog = Changelog_class.ChangeLogRaw(getActivity());

        About_changelog_txtview = (TextView) view.findViewById(R.id.textview_changelog_frag);
        About_changelog_txtview.setText("Change Log:- \n\n" + about_changelog);


        return view;
    } //end of oncreate





} //end of main class
