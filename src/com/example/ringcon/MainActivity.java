package com.example.ringcon;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ringcon.structure.Rule;

public class MainActivity extends FragmentActivity {

	private Rule[] rules ={};//new Rule(new Date(), new Date(), true),new Rule(new Date(), new Date(), true),new Rule(new Date(), new Date(), true),};
	private ListView ruleList;
	private RulesAdapter ruleAdapter;
	private SilanceManagerReceiver silanceManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ArrayList<Rule> assetList = new ArrayList<Rule>(Arrays.asList(rules));
		ruleAdapter = new RulesAdapter(this, assetList);

		ruleList = (ListView) findViewById(R.id.ruleList);
		ruleList.setAdapter(ruleAdapter);
	}

	
	//test for review manager responce. will be changed to commented lines on finish DialogFragment part; 
	public void onAddRule(View v) {
		
		Context context = this.getApplicationContext();

    	if (silanceManager != null) {
    		Rule rule = new Rule(1384797600, 1384798900, 63, true);
    		rule.setId(12);
    		silanceManager.setRule(context, rule);
    	} else {
    		Toast.makeText(context, "Crap! Can't do this :(", Toast.LENGTH_SHORT).show();
    	}
//		AddRuleFragment mContent = new AddRuleFragment();
//		mContent.show(getSupportFragmentManager(), "add_rule");
	}
}
