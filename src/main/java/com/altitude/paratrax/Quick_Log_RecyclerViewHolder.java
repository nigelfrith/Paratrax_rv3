package com.altitude.paratrax;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.altitude.paratrax.Interfaces.IItemClickListener;

public class Quick_Log_RecyclerViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
    //    TextView txt_title, txt_comment;
    EditText txt_fname,txt_lname, edit_weight,txt_pax_age,txt_email,txt_phone,txt_additional;
    CheckBox chk_medical;
    Switch switch_pics, switch_sherpa;
    Spinner companySelect;
    Button btn_post;

    IItemClickListener iItemClickListener;

    public void setiItemClickListener(IItemClickListener iItemClickListener) {
        this.iItemClickListener = iItemClickListener;
    }

    public Quick_Log_RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_fname = (EditText) itemView.findViewById(R.id.txt_fname);
        txt_lname = (EditText) itemView.findViewById(R.id.txt_lname);
        edit_weight = (EditText) itemView.findViewById(R.id.txt_weight);
        txt_pax_age= (EditText) itemView.findViewById(R.id.txt_pax_age);
        txt_email= (EditText) itemView.findViewById(R.id.txt_email);
        txt_phone= (EditText) itemView.findViewById(R.id.txt_phone);
        txt_additional= (EditText) itemView.findViewById(R.id.txt_additional);
//
//        chk_medical = (CheckBox)itemView.findViewById(R.id.chk_medical);
//        //Find Switch
//        switch_pics = (Switch) itemView.findViewById(R.id.switch_pics);
//        switch_sherpa =(Switch) itemView.findViewById(R.id.switch_sherpa);
//        //find Spinner
//        companySelect =(Spinner)itemView.findViewById(R.id.companySelect);


        itemView.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        iItemClickListener.OnClick(v, getAdapterPosition());
    }

}
