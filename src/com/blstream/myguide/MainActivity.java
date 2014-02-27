package com.blstream.myguide;

import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.blstream.myguide.dialog.ConfirmationDialogFragment;
import com.blstream.myguide.dialog.ConfirmationDialogFragment.ConfirmationDialogHolder;

public class MainActivity extends FragmentActivity implements ConfirmationDialogHolder {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpListeners();
    }
    

    /** Sets options menu. Called when options button is first clicked.*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    
    /** Called when one of items in options menu is clicked.*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options_menu:
                goOptions();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    /** Called when btnGoTickets ("Tickets and times") is clicked. Opens activity with information about the Zoo. */
    private void goAbout(){
    	Intent intent = new Intent(this, AboutActivity.class);
    	startActivity(intent);
    }
    
    /** Called when btnSightseeing ("Sightseeing") is clicked. Opens activity with sightseeing chooser screen. */
    private void goSightseeing(){
    	Intent intent = new Intent(this, SightseeingActivity.class);
    	startActivity(intent);
    }
    
    
    /** Called when btnHowToGet ("How to get") is clicked. Opens activity that shows how to get to the Zoo. */
    private void goHowToGet(){
    	Intent intent = new Intent(this, HowToGetActivity.class);
    	startActivity(intent);
    }
    
    /** Opens activity with options screen when "Options" is clicked in options menu. */
    private void goOptions(){
    	Intent intent = new Intent(this, OptionsActivity.class);
    	startActivity(intent);
    }
    
    /** Sets all listeners when Activity opens. */
    private void setUpListeners(){
    	this.findViewById(R.id.btnGoTickets).setOnClickListener(mGlobalOnClickListener);
    	this.findViewById(R.id.btnHowToGet).setOnClickListener(mGlobalOnClickListener);
    	this.findViewById(R.id.btnSightseeing).setOnClickListener(mGlobalOnClickListener);
    }
    
    /** Global onClickListener for all views. */
    final OnClickListener mGlobalOnClickListener = new OnClickListener() {
    	public void onClick(final View v) {
    		switch(v.getId()) {
    			case R.id.btnGoTickets:
    				goAbout();
    				break;
                case R.id.btnHowToGet:
                	goHowToGet();
                	break;
                case R.id.btnSightseeing:
                	goSightseeing();
                	break;
                default:
                	break;
    		}
    	}
    };

    /** Invoke after confirmation about closing an application is made. */
	@Override
	public void onDialogConfirm() {
		super.onBackPressed();
	}
	
    /** Override back-button functionality - show dialog asking about confirmation of closing an application. */	
	@Override
	public void onBackPressed() {
		ConfirmationDialogFragment
				.newInstance(getResources().getString(R.string.confirmation_exit))
				.show(getSupportFragmentManager(),ConfirmationDialogFragment.class.getSimpleName() );
	}

}

