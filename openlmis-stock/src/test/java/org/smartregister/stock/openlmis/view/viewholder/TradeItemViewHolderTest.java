package org.smartregister.stock.openlmis.view.viewholder;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowIntent;
import org.smartregister.stock.openlmis.BaseUnitTest;
import org.smartregister.stock.openlmis.R;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.Dispensable;
import org.smartregister.stock.openlmis.view.StockDetailsActivity;
import org.smartregister.stock.openlmis.wrapper.TradeItemWrapper;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by samuelgithengi on 8/13/18.
 */
public class TradeItemViewHolderTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();


    private Context context = RuntimeEnvironment.application;

    private TradeItemViewHolder viewHolder;


    private View view;


    @Before
    public void setUp() {
        view = LayoutInflater.from(context).inflate(R.layout.trade_item_row, null);
        viewHolder = new TradeItemViewHolder(view);
        TradeItem tradeItem = new TradeItem(UUID.randomUUID().toString());
        tradeItem.setName("Intervax BCG 20");
        tradeItem.setCommodityTypeId("305730154758");
        tradeItem.setDispensable(new Dispensable(UUID.randomUUID().toString(), "vials", null, null));
        TradeItemWrapper tradeItemWrapper = new TradeItemWrapper(tradeItem);
        tradeItemWrapper.setNumberOfLots(10);
        tradeItemWrapper.setTotalStock(50);
        viewHolder.setTradeItemWrapper(tradeItemWrapper);
    }

    @Test
    public void testOnClick() {
        view.performClick();
        ContextWrapper contextWrapper = new ContextWrapper(viewHolder.getContext());
        Intent startedIntent = shadowOf(contextWrapper).getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertEquals(StockDetailsActivity.class, shadowIntent.getIntentClass());
    }
}
