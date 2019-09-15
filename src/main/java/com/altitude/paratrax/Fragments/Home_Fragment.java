package com.altitude.paratrax.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;

import com.altitude.paratrax.R;
import com.altitude.paratrax.ResideMenu.ResideMenu;

public class Home_Fragment extends Fragment {


    private View parentView;
    private ResideMenu resideMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_home, container, false);
        setupToolbar(parentView);
        return parentView;
    }

    public void setupToolbar(View v) {
        Toolbar t = v.findViewById(R.id.app_bar);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity != null)
            appCompatActivity.setSupportActionBar(t);

    }

}
