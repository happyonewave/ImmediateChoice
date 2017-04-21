package com.qzct.immediatechoice.util;

import android.support.design.internal.NavigationMenu;
import android.view.MenuItem;


/**
 * Created by tsh2 on 2017/4/20.
 */

public class SimpleMenuListenerAdapter implements FabSpeedDial.MenuListener {

    @Override
    public boolean onPrepareMenu(NavigationMenu navigationMenu) {

        return true;
    }

    @Override
    public boolean onMenuItemSelected(MenuItem menuItem) {
        return false;
    }

    @Override
    public void onMenuClosed() {
    }

    @Override
    public void onOpenMenu() {

    }

    @Override
    public void onCloseMenu() {

    }

}
