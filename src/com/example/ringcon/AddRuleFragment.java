package com.example.ringcon;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class AddRuleFragment extends DialogFragment {

	RelativeLayout containerView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		setRetainInstance(true);
//		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.SoleDialog);

		super.onCreate(savedInstanceState);
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		containerView = (RelativeLayout) inflater.inflate(R.layout.dailog_add_rule, null);
		return containerView;
	}

}
