package com.carranza.upi;

import android.app.Dialog;
import android.app.MediaRouteActionProvider;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRouter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private MediaRouter mMediaRouter;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private CustomDrawerAdapter adapter;

    private List<DrawerItem> dataList;

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();








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

        mMediaRouter = (MediaRouter) getSystemService(Context.MEDIA_ROUTER_SERVICE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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

    /*
    public void openpdf(View view) {
        Intent i = new Intent(MainActivity.this, OpenPdf.class);
        startActivity(i);
        System.out.println("adios");
    }
    */

    /******************************************************************/
    /******************************************************************/
    /******************************************************************/
    /******************************************************************/
    // MediaRouter
    private final MediaRouter.SimpleCallback mMediaRouterCallback =
            new MediaRouter.SimpleCallback() {

                // BEGIN_INCLUDE(SimpleCallback)
                /**
                 * A new route has been selected as active. Disable the current
                 * route and enable the new one.
                 */
                @Override
                public void onRouteSelected(MediaRouter router, int type, MediaRouter.RouteInfo info) {
                    // updatePresentation();
                }

                /**
                 * The route has been unselected.
                 */
                @Override
                public void onRouteUnselected(MediaRouter router, int type, MediaRouter.RouteInfo info) {
                    // updatePresentation();

                }

                /**
                 * The route's presentation display has changed. This callback
                 * is called when the presentation has been activated, removed
                 * or its properties have changed.
                 */
                @Override
                public void onRoutePresentationDisplayChanged(MediaRouter router, MediaRouter.RouteInfo info) {
                    // updatePresentation();
                }
                // END_INCLUDE(SimpleCallback)
            };

    /**
     * Updates the displayed presentation to enable a secondary screen if it has
     * been selected in the {@link android.media.MediaRouter} for the
     * {@link android.media.MediaRouter#ROUTE_TYPE_LIVE_VIDEO} type. If no screen has been
     * selected by the {@link android.media.MediaRouter}, the current screen is disabled.
     * Otherwise a new {@link // SamplePresentation} is initialized and shown on
     * the secondary screen.
     */
    /*
    private void updatePresentation() {
        /*

        // BEGIN_INCLUDE(updatePresentationInit)
        // Get the selected route for live video
        MediaRouter.RouteInfo selectedRoute = mMediaRouter.getSelectedRoute(
                MediaRouter.ROUTE_TYPE_LIVE_VIDEO);

        // Get its Display if a valid route has been selected
        Display selectedDisplay = null;
        if (selectedRoute != null) {
            selectedDisplay = selectedRoute.getPresentationDisplay();
        }
        // END_INCLUDE(updatePresentationInit)

        // BEGIN_INCLUDE(updatePresentationDismiss)
        /*
         * Dismiss the current presentation if the display has changed or no new
         * route has been selected
         */
        /*
        if (mPresentation != null && mPresentation.getDisplay() != selectedDisplay) {
            mPresentation.dismiss();
            mPresentation = null;
            mButton.setEnabled(false);
            mTextStatus.setText(R.string.secondary_notconnected);
        }
        // END_INCLUDE(updatePresentationDismiss)

        // BEGIN_INCLUDE(updatePresentationNew)
        /*
         * Show a new presentation if the previous one has been dismissed and a
         * route has been selected.
         */
        /*
        if (mPresentation == null && selectedDisplay != null) {

            // Initialise a new Presentation for the Display
            mPresentation = new SamplePresentation(this, selectedDisplay);
            mPresentation.setOnDismissListener(mOnDismissListener);

            // Try to show the presentation, this might fail if the display has
            // gone away in the mean time
            try {
                mPresentation.show();
                mTextStatus.setText(getResources().getString(R.string.secondary_connected,
                        selectedRoute.getName(MainActivity.this)));
                mButton.setEnabled(true);
                showNextColor();
            } catch (WindowManager.InvalidDisplayException ex) {
                // Couldn't show presentation - display was already removed
                mPresentation = null;
            }
        }
        // END_INCLUDE(updatePresentationNew)
    }*/

    @Override
    protected void onPause() {
        super.onPause();

        // BEGIN_INCLUDE(onPause)
        // Stop listening for changes to media routes.
        mMediaRouter.removeCallback(mMediaRouterCallback);
        // END_INCLUDE(onPause)
    }

    @Override
    protected void onStop() {
        super.onStop();

        /*
        // BEGIN_INCLUDE(onStop)
        // Dismiss the presentation when the activity is not visible.
        if (mPresentation != null) {
            mPresentation.dismiss();
            mPresentation = null;
        }
        // BEGIN_INCLUDE(onStop)*/
    }

    /**
     * Inflates the ActionBar or options menu. The menu file defines an item for
     * the {@link android.app.MediaRouteActionProvider}, which is registered here for all
     * live video devices using {@link android.media.MediaRouter#ROUTE_TYPE_LIVE_VIDEO}.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);

        // BEGIN_INCLUDE(MediaRouteActionProvider)
        // Configure the media router action provider
        MenuItem mediaRouteMenuItem = menu.findItem(R.id.menu_media_route);
        MediaRouteActionProvider mediaRouteActionProvider =
                (MediaRouteActionProvider) mediaRouteMenuItem.getActionProvider();
        mediaRouteActionProvider.setRouteTypes(MediaRouter.ROUTE_TYPE_LIVE_VIDEO);
        // BEGIN_INCLUDE(MediaRouteActionProvider)

        return true;
    }

    /**
     * Listens for dismissal of the {@link // SamplePresentation} and removes its
     * reference.
     */
    /*
    private final DialogInterface.OnDismissListener mOnDismissListener =
            new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (dialog == mPresentation) {
                        mPresentation = null;
                    }
                }
            };

    // Views used to display status information on the primary screen
    private TextView mTextStatus;
    private Button mButton;

    // selected color index
    private int mColor = 0;

    // background colors
    public int[] mColors;

    /**
     * Displays the next color on the secondary screen if it is activate.
     */
    /*
    private void showNextColor() {
        if (mPresentation != null) {
            // a second screen is active and initialized, show the next color
            mPresentation.setColor(mColors[mColor]);
            mColor = (mColor + 1) % mColors.length;
        }
    }
    */

    /*************************************************************/
    /*********************************************/

}