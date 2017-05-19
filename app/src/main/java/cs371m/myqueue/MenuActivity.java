package cs371m.myqueue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Kaivan on 5/2/2017.
 */

public class MenuActivity extends Activity implements View.OnClickListener{

    // added by saleh to Keep track of context
    // http://stackoverflow.com/questions/14057273/android-singleton-with-global-context
    private static MenuActivity instance;
    public static MenuActivity get() { return instance; }
    private Queue q;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);

        Button browse = (Button) findViewById(R.id.browse_button);
        browse.setOnClickListener(this); //calling onClick method
        Button shows = (Button) findViewById(R.id.shows_button);
        shows.setOnClickListener(this);
        Button myqueue = (Button) findViewById(R.id.myqueue_button);
        myqueue.setOnClickListener(this);
        Button select_services = (Button) findViewById(R.id.select_services_button);
        select_services.setOnClickListener(this);
        Button settings = (Button) findViewById(R.id.settings_button);
        settings.setOnClickListener(this);

        instance = this;
        q = Queue.get();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.browse_button:
                Intent browse_intent = new Intent(MenuActivity.this, MoviesActivity.class);
                browse_intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(browse_intent);
                break;

            case R.id.shows_button:
                Intent shows_intent = new Intent(MenuActivity.this, SeriesActivity.class);
                shows_intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(shows_intent);
                break;

            case R.id.myqueue_button:
                Intent myqueue_intent = new Intent(MenuActivity.this, QueueActivity.class);
                myqueue_intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myqueue_intent);
                break;

            case R.id.select_services_button:
                Intent services_intent = new Intent(MenuActivity.this, SelectSourcesActivity.class);
                services_intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(services_intent);
                break;

            case R.id.settings_button:
                Intent settings_intent = new Intent(MenuActivity.this, SettingsActivity.class);
                settings_intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(settings_intent);
                break;

            default:
                break;
        }
    }
}