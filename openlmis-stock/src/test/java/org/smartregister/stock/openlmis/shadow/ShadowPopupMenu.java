package org.smartregister.stock.openlmis.shadow;

import android.widget.PopupMenu;

import org.robolectric.annotation.Implements;

/**
 * Created by samuelgithengi on 9/6/18.
 */
@Implements(PopupMenu.class)
public class ShadowPopupMenu extends org.robolectric.shadows.ShadowPopupMenu {

    @Override
    public void show() {
        //do nothing since its on unit test
    }
}
