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
import org.smartregister.stock.openlmis.view.viewholder.ReviewViewHolder;
import org.smartregister.stock.openlmis.widget.helper.LotDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by samuelgithengi on 8/31/18.
 */
public class ReviewAdapterTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private Context context = RuntimeEnvironment.application;

    private ReviewAdapter reviewAdapter;

    @Before
    public void setUp() {
        LotDto lotDto = new LotDto(UUID.randomUUID().toString(), 12, "VVM1", "LC2018L Exp. 20-08-2019");
        List<LotDto> lotDtos = new ArrayList<>();
        lotDtos.add(lotDto);
        reviewAdapter = new ReviewAdapter("Intervax BcG 20", lotDtos);
    }

    @Test
    public void testOnCreateViewHolder() {
        ReviewViewHolder viewHolder = reviewAdapter.onCreateViewHolder(new LinearLayout(context), 0);
        assertNotNull(viewHolder.getLotCodeTextView());
        assertNotNull(viewHolder.getTradeItemTextView());
        assertNotNull(viewHolder.getQuantityTextView());
        assertNotNull(viewHolder.getStatusTextView());
    }

    @Test
    public void testOnBindViewHolder() {
        ReviewViewHolder viewHolder = reviewAdapter.onCreateViewHolder(new LinearLayout(context), 0);
        reviewAdapter.onBindViewHolder(viewHolder, 0);
        assertEquals("Intervax BcG 20", viewHolder.getTradeItemTextView().getText());
        assertEquals("LC2018L Exp. 20-08-2019", viewHolder.getLotCodeTextView().getText());
        assertEquals("12", viewHolder.getQuantityTextView().getText());
        assertEquals("VVM1", viewHolder.getStatusTextView().getText());

    }

    public void testGetItemCount() {
        assertEquals(1, reviewAdapter.getItemCount());
    }

}
