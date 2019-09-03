package com.altitude.paratrax.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altitude.paratrax.R;
import com.altitude.paratrax.ResideMenu.ResideMenu;

public class Home_Fragment extends Fragment  {


    private View parentView;
    private ResideMenu resideMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_home, container, false);
        //force the whole app orientation..
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //if you want to individually let fragmnet s choose orientation then:
        //        getActivity().setRequestedOrientation(
        //                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        return parentView;
    }

}
