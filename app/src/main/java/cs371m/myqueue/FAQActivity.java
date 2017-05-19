package cs371m.myqueue;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class FAQActivity extends AppCompatActivity {

    private final String TAG = "FAQActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_layout);

        Toolbar faqToolbar = (Toolbar)findViewById(R.id.faq_toolbar);
        faqToolbar.setTitle(R.string.activity_faq);
        setSupportActionBar(faqToolbar);

    }
}
