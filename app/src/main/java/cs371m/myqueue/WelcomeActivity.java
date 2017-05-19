package cs371m.myqueue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;


public class WelcomeActivity extends Activity {

    private final String TAG = "WelcomeActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        setListener();
    }

    private void setListener(){
        Log.d(TAG, "setListener");
        LinearLayout lLayout = (LinearLayout) findViewById(R.id.welcome_layout);
        lLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "setListener");
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        });

    }
}
