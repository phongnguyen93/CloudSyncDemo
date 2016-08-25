/**
 *   ownCloud Android client application
 *
 *   Copyright (C) 2015 ownCloud Inc.
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License version 2,
 *   as published by the Free Software Foundation.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.phongnguyen.cloudsyncdemo.ui.dialog;

import android.app.Dialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.phongnguyen.cloudsyncdemo.R;

public class LoadingDialog extends DialogFragment {

    private static final String DISPLAY_TEXT = "displayText";
    private String mMessage;
    
    public LoadingDialog() {
        super();
    }

    public static LoadingDialog newInstance(String displayText) {
        LoadingDialog fragment = new LoadingDialog();
        Bundle args = new Bundle();
        args.putString(DISPLAY_TEXT, displayText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setCancelable(false);
        this.mMessage = getArguments().getString(DISPLAY_TEXT);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create a view by inflating desired layout
        View v = inflater.inflate(R.layout.loading_dialog, container,  false);
        
        // set value
        TextView tv  = (TextView) v.findViewById(R.id.loadingText);
        tv.setText(mMessage);

        // set progress wheel color
        ProgressBar progressBar  = (ProgressBar) v.findViewById(R.id.loadingBar);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.color_accent), PorterDuff.Mode.SRC_IN);
        
        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }
}
