package com.busktimachu.icommute.icommute;

import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String S_URL = "com.busktimachu.icommute.icommute.URL";
    public static final String UNIQUE_ID = "com.busktimachu.icommute.icommute.UID";
    private final String logTag = "iCommute mainActivity";

    private SharedPreferences sharepref;
    private String prefile = "STOR_FILE";
    private String prekey = "u_id";
    private String uid;
    private int REQUEST_CODE = 1;
    private String server_url;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(logTag, "In onCreate...");

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            SelectRouteFragment routeFragment = new SelectRouteFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            routeFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, routeFragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        sharepref = getSharedPreferences(prefile,MODE_PRIVATE);
        uid= sharepref.getString(prekey, "");
        SharedPreferences settingPref = PreferenceManager.getDefaultSharedPreferences(this);
        server_url = settingPref.getString(SettingsActivity.KEY_PREF_SERVER_ADDR, "");

        Log.d(logTag, "In onResume...");
        if (uid.isEmpty()) {
            Intent register = new Intent(MainActivity.this, RegisterActivity.class);
            register.putExtra(S_URL,server_url);
            startActivityForResult(register,REQUEST_CODE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(logTag, "in onActivityResult");
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                uid = data.getStringExtra(UNIQUE_ID);
                SharedPreferences.Editor edit = sharepref.edit();
                edit.putString(prekey,uid);
                edit.apply();
            }
            if (resultCode == RESULT_CANCELED) {
                //TODO: handle registration failures
            }
        }
    }


        @Override
    public void onNavigationDrawerItemSelected(int position) {
        Log.d(logTag, "in onNavigationDrawerItemSelected...arg: Position="+position);
            onSectionAttached(position+1);
    }

    public void onSectionAttached(int number) {
        Log.d(logTag,"in onSectionAttached...,Arg: num="+ number);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);

                SelectRouteFragment routeFragment = new SelectRouteFragment();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.container, routeFragment);
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                SelectAreaFragment areaFragment = new SelectAreaFragment();

                transaction.replace(R.id.container, areaFragment);
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        Log.d(logTag,"in restoreActionBar...");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(logTag,"in onCreateOptionsMenu...");
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d(logTag,"in onOptionsItemSelected...");
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
