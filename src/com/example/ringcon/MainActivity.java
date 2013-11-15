package com.example.ringcon;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onAddRule(View v) {
		AddRuleFragment mContent = new AddRuleFragment();
		mContent.show(getSupportFragmentManager(), "add_rule");
	}
}
