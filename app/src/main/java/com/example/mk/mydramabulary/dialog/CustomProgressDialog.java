package com.example.mk.mydramabulary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.example.mk.mydramabulary.R;

/**
 * Created by mk on 2017-02-20.
 */

public class CustomProgressDialog extends Dialog {



    public CustomProgressDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_progress_dialog);


    }
}
