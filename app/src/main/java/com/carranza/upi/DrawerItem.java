package com.carranza.upi;

public class DrawerItem {

    private String ItemName;
    private int imgResID;
    private String title;
    private boolean isUser;

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getImgResID() {
        return imgResID;
    }

    public void setImgResID(int imgResID) {
        this.imgResID = imgResID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean isUser) {
        this.isUser = isUser;
    }

    public DrawerItem(String itemName, int imgResID) {
        super();

        this.ItemName = itemName;
        this.imgResID = imgResID;
        this.isUser = false;
    }

    public DrawerItem(String title) {
        this(null, 0);

        this.title = title;
    }

    public DrawerItem(boolean isUser) {
        this(null, 0);

        this.isUser = isUser;
    }
}
