package cs371m.myqueue;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class MediaDetailsActivity extends AppCompatActivity {

    private final String TAG = "MediaDetailsActivity";

    private static final int REQUEST_WRITE = 0;
    private static final int REQUEST_READ = 1;

    private String title;
    private String media_type;
    private Long id;
    private Long tMDBid;
    String year;

    private Queue q;
    private String selected_source;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.media_details_layout);
        Log.d(TAG, "onCreate");

        ImageView imageView = (ImageView) findViewById(R.id.movie_poster);
        ImageView imageView_poster = (ImageView) findViewById(R.id.item_details_service);
        TextView titleTextView = (TextView) findViewById(R.id.item_details_title);

        Toolbar detailsToolbar = (Toolbar)findViewById(R.id.item_details_toolbar);
        detailsToolbar.setTitle(R.string.activity_media);
        setSupportActionBar(detailsToolbar);

        selected_source = getIntent().getStringExtra("selected_source");
        Log.d(TAG, "onCreate --> " + selected_source);
        switch (selected_source) {

            case "netflix":
                Picasso.with(this).load(R.drawable.netflix).into(imageView_poster);
                break;
            case "hulu_free,hulu_plus":
                Picasso.with(this).load(R.drawable.hulu).into(imageView_poster);
                break;
            case "hbo":
                Picasso.with(this).load(R.drawable.hbo).into(imageView_poster);
                break;
            case "amazon_prime":
                Picasso.with(this).load(R.drawable.amazon).into(imageView_poster);
                break;
        }

        title = getIntent().getStringExtra("title");
        year = getIntent().getStringExtra("year");
        title = title + "("+year+")";
        media_type = getIntent().getStringExtra("media_type");
        id = getIntent().getLongExtra("id", -1);
        String image = getIntent().getStringExtra("image");
        tMDBid = getIntent().getLongExtra("tMDBid", 0);
        new HttpRequestTask().execute();

        Picasso.with(this).load(image).resize(165, 275).into(imageView);
        titleTextView.setText(Html.fromHtml(title));

        q = Queue.get();

        queueOrDequeue();
        setListeners();
        checkPermissions();
    }

    private void queueOrDequeue() {
        Log.d(TAG, "queueOrDequeue");
        if(q.movieExists(id)){
            TextView t = (TextView) findViewById(R.id.item_details_bookmarkText);
            t.setText(R.string.details_bookmark_delete);

            ImageView i = (ImageView) findViewById(R.id.item_details_bookmarkIcon);
            Picasso.with(this).load(R.drawable.bookmark_delete).into(i);
        }
        else{
            TextView t = (TextView) findViewById(R.id.item_details_bookmarkText);
            t.setText(R.string.details_bookmark);

            ImageView i = (ImageView) findViewById(R.id.item_details_bookmarkIcon);
            Picasso.with(this).load(R.drawable.bookmark).into(i);
        }

    }

    public void setListeners() {
        Log.d(TAG, "setListeners()");

        TableRow row5 = (TableRow) findViewById(R.id.tableRow5);
        row5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "onClick() - item_details_bookmarkIcon");
                if(((TextView) findViewById(R.id.item_details_bookmarkText)).getText().equals(getResources().getString(R.string.details_bookmark))) {
                    boolean x = q.addMovie(id, selected_source, media_type);
                    if (x)
                        Toast.makeText(getBaseContext(), title + " " + getResources().getString(R.string.details_queue_add),
                                Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getBaseContext(), title + " "  + getResources().getString(R.string.details_queue_already),
                                Toast.LENGTH_LONG).show();
                }
                else if (((TextView) findViewById(R.id.item_details_bookmarkText)).getText().equals(getResources().getString(R.string.details_bookmark_delete))){
                    boolean x = q.deleteMovie(id);
                    if (x)
                        Toast.makeText(getBaseContext(), title + " "  + getResources().getString(R.string.details_queue_delete),
                                Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getBaseContext(), title + " "  + getResources().getString(R.string.details_queue_does_not_exist),
                                Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getBaseContext(), title + " "  + getResources().getString(R.string.qué_paso), Toast.LENGTH_LONG).show();
                }
                queueOrDequeue();
            }
        });

        ImageView img1 = (ImageView) findViewById(R.id.item_details_service);
        img1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "onClick() - item_details_service");

                String selected_source = getIntent().getStringExtra("selected_source");
                Intent browserIntent;
                switch (selected_source) {
                    case "netflix":
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.netflix.com"));
                        break;
                    case "hulu_free,hulu_plus":
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.hulu.com"));
                        break;
                    case "hbo":
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.hbogo.com/"));
                        break;
                    case "amazon_prime":
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amazon.com/Prime-Video/b?node=2676882011"));
                        break;
                    default:
                        Toast.makeText(getBaseContext(), R.string.qué_paso ,Toast.LENGTH_LONG).show();
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.aol.com"));
                }
                startActivity(browserIntent);
            }
        });
    }

    protected boolean checkPermissions() {
        int permissionChecka = ContextCompat.checkSelfPermission
                (this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheckb = ContextCompat.checkSelfPermission
                (this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheckc = ContextCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_NETWORK_STATE);
        int permissionCheckd = ContextCompat.checkSelfPermission
                (this, Manifest.permission.INTERNET);

        boolean a = permissionChecka == PackageManager.PERMISSION_GRANTED;
        boolean b = permissionCheckb == PackageManager.PERMISSION_GRANTED;
        boolean c = permissionCheckc == PackageManager.PERMISSION_GRANTED;
        boolean d = permissionCheckd == PackageManager.PERMISSION_GRANTED;

        if(a)
            requestWritePermission();
        if(b)
            requestReadPermission();

        //couldn't figure out how to programatically retrieve the complied sdk version, but i know
        // it's 23, so we should ask for runtime permissions
        return !(a && b && c && d);
    }

    private void requestWritePermission(){
        final String TAG = "requestWritePermission";
        Log.d(TAG, "");

        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE);
    }
    private void requestReadPermission(){
        final String TAG = "requestReadPermission";

        Log.d(TAG, "");
        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ);
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, tMDB> {
        @Override
        protected tMDB doInBackground(Void... params) {
            try {
                Log.d("MediaDetailstMDBidValue", Long.toString(tMDBid));
                String type = "movie";
                if (media_type.equals("shows")) {
                    type = "tv";
                }
                final String url2 = "https://api.themoviedb.org/3/" + type + "/" + Long.toString(tMDBid) +
                        "?api_key=2fb9522ed230e5f6dae69f6206113021";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                tMDB movieDb= restTemplate.getForObject(url2, tMDB.class);

                return movieDb;

            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(tMDB tMDB) {
            TextView rottenTextView = (TextView) findViewById(R.id.item_details_rotten_score);
            TextView plotTextView = (TextView) findViewById(R.id.item_details_plot);
            if (tMDB != null) {
                rottenTextView.setText(Double.toString(tMDB.getRating()));
                plotTextView.setText(tMDB.getOverview());}
        }
    }
}
