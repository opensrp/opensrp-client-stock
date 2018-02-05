package smartregister.org.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.smartregister.stock.util.Utils;

public class SampleStockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_stock);

        TextView view = (TextView) findViewById(R.id.testView);
        view.setText("Testing Library Module 3 + 2 = " + Utils.sampleAdditionMethod(3, 2));
    }
}
