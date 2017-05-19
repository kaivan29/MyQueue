package cs371m.myqueue;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class AboutActivity extends AppCompatActivity {

    private final String TAG = "AboutActivity";
    ImageView image;
    boolean x = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);

        image = (ImageView) findViewById(R.id.about_image);
        Picasso.with(this)
                .load(R.drawable.about)
                .resize(1000, 700)
                .into(image);

        image.setOnTouchListener( new OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Log.d(TAG, "onTouchEvent");
                int eventaction = event.getAction();
                Log.d(TAG, "onTouchEvent1 " + eventaction);

            if (eventaction == MotionEvent.ACTION_DOWN) {
                //get system current milliseconds
                long time= System.currentTimeMillis();


                //if it is the first time, or if it has been more than 3 seconds since the first tap ( so it is like a new try), we reset everything
                if (startMillis==0 || (time-startMillis> 6000) ) {
                    Log.d(TAG, "onTouchEvent3");
                    startMillis=time;
                    count=1;
                }
                //it is not the first, and it has been  less than 3 seconds since the first
                else{ //  time-startMillis< 3000
                    Log.d(TAG, "onTouchEvent4");
                    count++;
                }

                if (count==5) {
                    if(!x) {
                        Log.d(TAG, "onTouchEvent5");
                        Picasso.with(AboutActivity.this)
                                .load(R.drawable.error2).centerInside()
                                .resize(1000, 700)
                                .into(image);
                        x = !x;
                    }
                    else{
                        Log.d(TAG, "onTouchEvent6");
                        Picasso.with(AboutActivity.this)
                                .load(R.drawable.about)
                                .resize(1000, 700)
                                .into(image);
                        x = !x;
                    }
                    image.invalidate();
                }
                return true;
            }
            return false;
        } });

        Toolbar aboutToolbar = (Toolbar)findViewById(R.id.about_toolbar);
        aboutToolbar.setTitle("About");
        setSupportActionBar(aboutToolbar);

        findViewById(R.id.git_pic).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("https://github.com/almto3/MyQueue")));
            }
        });
    }


    //source for the following
    //http://stackoverflow.com/a/21104386
    private int count = 0;
    private long startMillis=0;

    //detect any touch event in the screen (instead of an specific view)


}
