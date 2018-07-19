package org.smartregister.stock.openlmis.view.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.stock.openlmis.BaseUnitTest;
import org.smartregister.stock.openlmis.R;

import static org.junit.Assert.assertEquals;

/**
 * Created by samuelgithengi on 7/19/18.
 */
public class CommodityTypeViewHolderTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();


    private Context context = RuntimeEnvironment.application;

    private CommodityTypeViewHolder viewHolder;

    @Before
    public void setup() {
        View view = LayoutInflater.from(context).inflate(R.layout.commodity_type_row, null);
        viewHolder = new CommodityTypeViewHolder(view);
    }

    @Test
    public void testCollapseExpandOnClick() {
        //should expand
        viewHolder.getCollapseExpandButton().performClick();
        assertEquals(View.VISIBLE, viewHolder.getTradeItemsRecyclerView().getVisibility());
        assertEquals(context.getDrawable(R.drawable.ic_keyboard_arrow_up), viewHolder.getCollapseExpandButton().getDrawable());

        //should collapse
        viewHolder.getCollapseExpandButton().performClick();
        assertEquals(View.GONE, viewHolder.getTradeItemsRecyclerView().getVisibility());
        assertEquals(context.getDrawable(R.drawable.ic_keyboard_arrow_down), viewHolder.getCollapseExpandButton().getDrawable());

    }


}
