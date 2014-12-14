package com.carranza.upi;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {

    private Context context;
    private List<DrawerItem> drawerItemList;
    private int layoutResID;

    public CustomDrawerAdapter(Context context, int layoutResourceID, List<DrawerItem> listItems) {
        super(context, layoutResourceID, listItems);

        this.context = context;
        this.drawerItemList = listItems;
        this.layoutResID = layoutResourceID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DrawerItemHolder drawerHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            drawerHolder = new DrawerItemHolder();

            view = inflater.inflate(layoutResID, parent, false);

            drawerHolder.itemLayout = (LinearLayout) view.findViewById(R.id.itemLayout);
            drawerHolder.icon = (ImageView) view.findViewById(R.id.drawer_icon);
            drawerHolder.ItemName = (TextView) view.findViewById(R.id.drawer_item);

            drawerHolder.headerLayout = (LinearLayout) view.findViewById(R.id.headerLayout);
            drawerHolder.title = (TextView) view.findViewById(R.id.drawerTitle);

            drawerHolder.spinnerLayout = (LinearLayout) view.findViewById(R.id.spinnerLayout);
            drawerHolder.name = (TextView) view.findViewById(R.id.text_main_name);
            drawerHolder.email = (TextView) view.findViewById(R.id.text_sub_email);

            view.setTag(drawerHolder);

        } else {
            drawerHolder = (DrawerItemHolder) view.getTag();

        }

        DrawerItem dItem = (DrawerItem) this.drawerItemList.get(position);

        if (dItem.isUser()) {
            drawerHolder.headerLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.itemLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.spinnerLayout.setVisibility(LinearLayout.VISIBLE);

            //drawerHolder.name.setText(User.getUser().getSurname());
            //drawerHolder.email.setText(User.getUser().getEmail());

        } else if (dItem.getTitle() != null) {
            drawerHolder.headerLayout.setVisibility(LinearLayout.VISIBLE);
            drawerHolder.itemLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.spinnerLayout.setVisibility(LinearLayout.INVISIBLE);

            drawerHolder.title.setText(dItem.getTitle());
        } else {
            drawerHolder.headerLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.spinnerLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.itemLayout.setVisibility(LinearLayout.VISIBLE);

            drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(dItem.getImgResID()));
            drawerHolder.ItemName.setText(dItem.getItemName());
        }

        return view;
    }

    private static class DrawerItemHolder {

        protected TextView ItemName, title, name, email;
        protected ImageView icon;
        protected LinearLayout headerLayout, itemLayout, spinnerLayout;
    }
}