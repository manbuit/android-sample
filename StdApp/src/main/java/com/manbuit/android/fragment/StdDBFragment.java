package com.manbuit.android.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StdDBFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StdDBFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StdDBFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.fragment_std_db, container, false);
        return messageLayout;
    }
}
