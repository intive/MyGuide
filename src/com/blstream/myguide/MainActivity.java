package com.blstream.myguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    /** Called when btnGoTickets ("Tickets and times") is clicked. Opens activity with information about the Zoo. */
    public void goAbout(View view){
    	Intent intent = new Intent(this, AboutActivity.class);
    	startActivity(intent);
    }
    
    /** Called when btnSightseeing ("Sightseeing") is clicked. Opens activity with sightseeing chooser screen. */
    public void goSightseeing(View view){
    	Intent intent = new Intent(this, SightseeingActivity.class);
    	startActivity(intent);
    }
    
    
    /** Called when btnHowToGet ("How to get") is clicked. Opens activity that shows how to get to the Zoo. */
    public void goHowToGet(View view){
    	Intent intent = new Intent(this, HowToGetActivity.class);
    	startActivity(intent);
    }
    
    /** Opens activity with options screen when "Options" is clicked in options menu. */
    private void goOptions(){
    	Intent intent = new Intent(this, OptionsActivity.class);
    	startActivity(intent);
    }
}
