package com.rdlab.subssync;

import java.util.ArrayList;

import com.rdlab.adapters.NavigationDrawerAdapter;
import com.rdlab.dependencyInjection.BaseActivity;
import com.rdlab.fragments.DistrictFragment;
import com.rdlab.model.NavigationDrawerItem;
import com.rdlab.utility.Constants;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends BaseActivity {
	private DrawerLayout _drawerLayout;
	private ListView _drawerList;
	private ActionBarDrawerToggle _drawerToggle;

	private CharSequence _drawerTitle;
	private CharSequence _appTitle;

	private String[] _navItems;
	private TypedArray _navMenuIcons;

	private ArrayList<NavigationDrawerItem> _menuDrawerItems;
	private NavigationDrawerAdapter _drawerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		

		_drawerTitle = _appTitle = getTitle();

		_navItems = getResources().getStringArray(R.array.nav_drawer_items);

		_navMenuIcons = getResources().obtainTypedArray(
				R.array.nav_drawer_icons);

		_drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		_drawerList = (ListView) findViewById(R.id.list_slidermenu);

		_menuDrawerItems = new ArrayList<NavigationDrawerItem>();

		for (int i = 0; i < _navItems.length; i++) {
			_menuDrawerItems.add(new NavigationDrawerItem(_navItems[i],
					_navMenuIcons.getResourceId(i, -1)));
		}

		_navMenuIcons.recycle();

		_drawerAdapter = new NavigationDrawerAdapter(getApplicationContext(),
				_menuDrawerItems);
		_drawerList.setAdapter(_drawerAdapter);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		_drawerToggle = new ActionBarDrawerToggle(this, _drawerLayout,
				R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
			@Override
			public void onDrawerClosed(View drawerView) {
				getActionBar().setTitle(_appTitle);
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(_drawerTitle);
				invalidateOptionsMenu();
			}
		};

		_drawerLayout.setDrawerListener(_drawerToggle);

		if (savedInstanceState == null) {
			// displayView(0);

		}

		_drawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				displayView(arg2);
			}
		});
	}

	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new DistrictFragment();
			break;
		case 1:
			//fragment = new FindPeopleFragment();
			break;
		case 2:
			//fragment = new PhotosFragment();
			break;
		case 3:
			//fragment = new CommunityFragment();
			break;
		case 4:
			//fragment = new PagesFragment();
			break;
		case 5:
			//fragment = new WhatsHotFragment();
			break;

		default:
			break;
		}

		if (fragment != null) {
			
			FragmentTransaction ft = getFragmentManager()
					.beginTransaction();
			ft.replace(R.id.frame_container, fragment);
			ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
			ft.addToBackStack(null);
			ft.commit();
			
			// update selected item and title, then close the drawer
			_drawerList.setItemChecked(position, true);
			_drawerList.setSelection(position);
			setTitle(_navItems[position]);
			_drawerLayout.closeDrawer(_drawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (_drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		case R.id.action_bozova:
			setGlobalConstants("1209",getString(R.string.action_bozova),"com.rdlab.model.Bozova");
			return true;
		case R.id.action_birecik:
			setGlobalConstants("1194",getString(R.string.action_birecik),"com.rdlab.model.Birecik");
			return true;
		case R.id.action_akcakale:
			setGlobalConstants("1115",getString(R.string.action_akcakale),"com.rdlab.model.Akcakale");
			return true;
		case R.id.action_haliliye:
			setGlobalConstants("2092",getString(R.string.action_haliliye),"com.rdlab.model.Haliliye");
			return true;
		case R.id.action_eyyubiye:
			setGlobalConstants("2091",getString(R.string.action_eyyubiye),"com.rdlab.model.Eyyubiye");
			return true;
		case R.id.action_siverek:
			setGlobalConstants("1630",getString(R.string.action_siverek),"com.rdlab.model.Siverek");
			return true;
		case R.id.action_karakopru:
			setGlobalConstants("2093",getString(R.string.action_karakopru),"com.rdlab.model.Karakopru");
			return true;
		case R.id.action_suruc:
			setGlobalConstants("1643",getString(R.string.action_suruc),"com.rdlab.model.Suruc");
			return true;
		case R.id.action_ceylanpinar:
			setGlobalConstants("1220",getString(R.string.action_ceylanpinar),"com.rdlab.model.Ceylanpinar");
			return true;
		case R.id.action_halfeti:
			setGlobalConstants("1378",getString(R.string.action_halfeti),"com.rdlab.model.Halfeti");
			return true;
		case R.id.action_harran:
			setGlobalConstants("1800",getString(R.string.action_harran),"com.rdlab.model.Harran");
			return true;
		case R.id.action_hilvan:
			setGlobalConstants("1393",getString(R.string.action_hilvan),"com.rdlab.model.Hilvan");
			return true;
		case R.id.action_viransehir:
			setGlobalConstants("1713",getString(R.string.action_viransehir),"com.rdlab.model.Viransehir");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setGlobalConstants(String code,String name,String className){
		Constants.SelectedCountyCode = code;                   
		Constants.SelectedCountyName = name;               
		Constants.SelectedClassName = className;
		Toast.makeText(this, "Þehir seçiminiz tamamlandý.", Toast.LENGTH_SHORT).show();
	}
	
	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = _drawerLayout.isDrawerOpen(_drawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void setTitle(CharSequence title) {
		_appTitle = title;
		getActionBar().setTitle(_appTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		_drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		_drawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onBackPressed() {
		FragmentManager fm=getFragmentManager();
		int backStackEntryCount=fm.getBackStackEntryCount();
		if (backStackEntryCount > 0) {
	        getFragmentManager().popBackStack();
	    } else {
	        super.onBackPressed();
	    }
	}
}
