package org.smartregister.stock.openlmis.adapter;

import android.content.Context;
import android.widget.LinearLayout;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.stock.openlmis.BaseUnitTest;
import org.smartregister.stock.openlmis.domain.TradeItem;
import org.smartregister.stock.openlmis.domain.openlmis.Dispensable;
import org.smartregister.stock.openlmis.view.viewholder.TradeItemViewHolder;
import org.smartregister.stock.openlmis.wrapper.TradeItemWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by samuelgithengi on 7/19/18.
 */
public class ListTradeItemAdapterTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private Context context = RuntimeEnvironment.application;

    private ListTradeItemAdapter listTradeItemAdapter;

    @Before
    public void setUp() {
        List<TradeItemWrapper> expectedTradeItems = new ArrayList<>();
        TradeItem tradeItem = new TradeItem(UUID.randomUUID().toString());
        tradeItem.setName("Intervax BCG 20");
        tradeItem.setCommodityTypeId("305730154758");
        tradeItem.setDispensable(new Dispensable(UUID.randomUUID().toString(), "vials", null, null));
        TradeItemWrapper tradeItemWrapper = new TradeItemWrapper(tradeItem);
        tradeItemWrapper.setNumberOfLots(10);
        tradeItemWrapper.setTotalStock(50);
        expectedTradeItems.add(tradeItemWrapper);
        listTradeItemAdapter = new ListTradeItemAdapter(expectedTradeItems, context);
    }

    @Test
    public void testOnCreateViewHolder() {
        LinearLayout vg = new LinearLayout(context);
        TradeItemViewHolder holder = listTradeItemAdapter.onCreateViewHolder(vg, 0);
        assertNotNull(holder.getNameTextView());
        assertNotNull(holder.getLotsTextView());
        assertNotNull(holder.getDispensableTextView());
        assertNotNull(holder.getExpiringTextView());

    }

    @Test
    public void testOnBindViewHolder() {
        LinearLayout vg = new LinearLayout(context);
        TradeItemViewHolder holder = listTradeItemAdapter.onCreateViewHolder(vg, 0);

        listTradeItemAdapter.onBindViewHolder(holder, 0);
        assertEquals("Intervax BCG 20", holder.getNameTextView().getText());
        assertEquals("10 lots", holder.getLotsTextView().getText());
        assertEquals("50 vials", holder.getDispensableTextView().getText());

    }

    @Test
    public void testGetItemCount() {
        assertEquals(1, listTradeItemAdapter.getItemCount());
    }

}
