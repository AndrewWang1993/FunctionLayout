package xiaoming.website.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {
    public final static String KEY = "key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText(getIntent().getStringExtra(KEY));
        setContentView(textView);
    }

}
