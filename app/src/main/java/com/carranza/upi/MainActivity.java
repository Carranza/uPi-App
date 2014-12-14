package com.carranza.upi;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private CustomDrawerAdapter adapter;

    private List<DrawerItem> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataList = new ArrayList<DrawerItem>();
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // add DrawerItem to dataList
        dataList.add(new DrawerItem(true));

        dataList.add(new DrawerItem("Main Sections"));
        dataList.add(new DrawerItem("Home", R.drawable.ic_action_view_as_grid));
        dataList.add(new DrawerItem("Documents", R.drawable.ic_action_copy));

        dataList.add(new DrawerItem("Other Sections"));
        dataList.add(new DrawerItem("About", R.drawable.ic_action_about));
        dataList.add(new DrawerItem("Help", R.drawable.ic_action_help));

        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item, dataList);

        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
            this,
            mDrawerLayout,
            R.drawable.ic_drawer,
            R.string.drawer_open,
            R.string.drawer_close) {

                public void onDrawerClosed(View view) {
                    getActionBar().setTitle(mTitle);
                    invalidateOptionsMenu();
                    // onPrepareOptionsMenu()
                }

                public void onDrawerOpened(View drawerView) {
                    getActionBar().setTitle(mDrawerTitle);
                    invalidateOptionsMenu();
                    // onPrepareOptionsMenu()
                }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {

            if (dataList.get(0).isUser() & dataList.get(1).getTitle() != null) {
                SelectItem(2);
            } else if (dataList.get(0).getTitle() != null) {
                SelectItem(1);
            } else {
                SelectItem(0);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_log_out:
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);

                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void SelectItem(int position) {

        Fragment fragment = null;
        Bundle args = new Bundle();
        switch (position) {
            case 2:
                fragment = new HomeFragment();
                args.putString(HomeFragment.ITEM_NAME, dataList.get(position).getItemName());
                args.putInt(HomeFragment.IMAGE_RESOURCE_ID, dataList.get(position).getImgResID());
                break;
            case 3:
                fragment = new DocumentFragment();
                args.putString(DocumentFragment.ITEM_NAME, dataList.get(position).getItemName());
                args.putInt(DocumentFragment.IMAGE_RESOURCE_ID, dataList.get(position).getImgResID());
                break;
            case 5:
                fragment = new AboutFragment();
                args.putString(AboutFragment.ITEM_NAME, dataList.get(position).getItemName());
                args.putInt(AboutFragment.IMAGE_RESOURCE_ID, dataList.get(position).getImgResID());
                break;
            case 6:
                fragment = new HelpFragment();
                args.putString(HelpFragment.ITEM_NAME, dataList.get(position).getItemName());
                args.putInt(HelpFragment.IMAGE_RESOURCE_ID, dataList.get(position).getImgResID());
                break;
            default:
                break;
        }

        fragment.setArguments(args);
        FragmentManager frgManager = getFragmentManager();
        frgManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(dataList.get(position).getItemName());
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (dataList.get(position).getTitle() == null && !dataList.get(position).isUser()) {
                SelectItem(position);
            }
        }
    }

    public void openpdf(View view) {
        Intent i = new Intent(MainActivity.this, OpenPdf.class);
        startActivity(i);
        // System.out.println("prueba");
    }
}