package org.smartregister.stock.openlmis.shadow;

import android.support.design.widget.TextInputLayout;

import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadows.ShadowViewGroup;

/**
 * Created by samuelgithengi on 9/3/18.
 */
@Implements(TextInputLayout.class)
public class ShadowTextInputLayout extends ShadowViewGroup {

    @RealObject
    private TextInputLayout realTextInputLayout;
}