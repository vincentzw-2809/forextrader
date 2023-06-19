package vincentzw202203378.forexx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import android.support.v4.widget.SwipeRefreshLayout;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.ResponseHandler;

public class MainActivity extends AppCompatActivity {
    private ProgressBar loadingProgressBar;
    private SwipeRefreshLayout swipeRefreshLayout1;
    private TextView audTextView, bndTextView, btcTextView, eurTextView, gbpTextView, hkdTextView, inrTextView, jpyTextView;
    private TextView myrTextView, usdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout1 = findViewById(R.id.swipeRefreshLayout1);
        audTextView = findViewById(R.id.audTextView);
        bndTextView = findViewById(R.id.bndTextView);
        btcTextView = findViewById(R.id.btcTextView);
        eurTextView = findViewById(R.id.eurTextView);
        gbpTextView = findViewById(R.id.gbpTextView);
        hkdTextView = findViewById(R.id.hkdTextView);
        inrTextView = findViewById(R.id.inrTextView);
        jpyTextView = findViewById(R.id.jpyTextView);
        myrTextView = findViewById(R.id.myrTextView);
        usdTextView = findViewById(R.id.usdTextView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        initSwipeRefreshLayout();
        initForex();
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout1.setOnRefreshListener(() -> {
            initForex();
            swipeRefreshLayout1.setRefreshing(false);
        });
    }

    public String formatNumber(double number, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(number);
    }

    public void initForex() {
        loadingProgressBar.setVisibility(TextView.VISIBLE);

        String url = "https://openexchangerates.org/api/latest.json?app_id=7a3fdea91c4e4088bef0eeb3952d4157";

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("*tw*", new String(responseBody));
                Gson gson = new Gson();
                RootModel rootModel = gson.fromJson(new String(responseBody), RootModel.class);
                RatesModel ratesModel = rootModel.getRatesModel();

                double aud = ratesModel.getIDR() / ratesModel.getAUD();
                double bnd = ratesModel.getIDR() / ratesModel.getBND();
                double btc = ratesModel.getIDR() / ratesModel.getBTC();
                double eur = ratesModel.getIDR() / ratesModel.getEUR();
                double gbp = ratesModel.getIDR() / ratesModel.getGBP();
                double hkd = ratesModel.getIDR() / ratesModel.getHKD();
                double inr = ratesModel.getIDR() / ratesModel.getINR();
                double jpy = ratesModel.getIDR() / ratesModel.getJPY();
                double myr = ratesModel.getIDR() / ratesModel.getMYR();
                double idr = ratesModel.getIDR();

                audTextView.setText(formatNumber(aud, "###,##0.##"));
                bndTextView.setText(formatNumber(bnd, "###,##0.##"));
                btcTextView.setText(formatNumber(btc, "###,##0.##"));
                eurTextView.setText(formatNumber(eur, "###,##0.##"));
                gbpTextView.setText(formatNumber(gbp, "###,##0.##"));
                hkdTextView.setText(formatNumber(hkd, "###,##0.##"));
                inrTextView.setText(formatNumber(inr, "###,##0.##"));
                jpyTextView.setText(formatNumber(jpy, "###,##0.##"));
                myrTextView.setText(formatNumber(myr, "###,##0.##"));
                usdTextView.setText(formatNumber(idr, "###,##0.##"));

                loadingProgressBar.setVisibility(TextView.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                loadingProgressBar.setVisibility(TextView.INVISIBLE);
            }
        });
    }
}