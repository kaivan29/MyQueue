package cs371m.myqueue;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


public class QueueActivity extends AppCompatActivity {

    private Queue q;

    private GridView gridView;

    private GridViewAdapter gridAdapter;
    private ArrayList<Result> results = new ArrayList<>(100);
    private ArrayList<GridItem> mGridData;

    private final String TAG = "QueueActivity";

    private List<String> selected_sources = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_queue_layout);

        if(!checkInternet()){
            getInternet();
        }

        Toolbar myQueueToolbar = (Toolbar)findViewById(R.id.my_queue_toolbar);
        myQueueToolbar.setTitle(R.string.activity_q);
        myQueueToolbar.findViewById(R.id.my_queue_toolbar_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Clicked");

                new AlertDialog.Builder(QueueActivity.this)
                        .setTitle("Empty Queue?")
                        .setMessage(R.string.my_queue_delete_conformation)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                q.deleteAllMovies();
                                emptyGrid();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        setSupportActionBar(myQueueToolbar);

        q = Queue.get();
    }

    private void restart() {
        finish();
        startActivity(getIntent());
    }

    private void emptyGrid(){
        Log.d(TAG, "emptyGrid - 1");
        mGridData.clear();
        findViewById(R.id.gridView).invalidate();
        Log.d(TAG, "emptyGrid - " + mGridData.toString());
        gridAdapter.notifyDataSetChanged();
        gridView.setAdapter(gridAdapter);
        Log.d(TAG, "emptyGrid - 2");
    }

    @Override
    protected void onStart() {
        super.onStart();

        results.clear();

        new QueueActivity.HttpRequestTask().execute();

        gridView = (GridView) findViewById(R.id.gridView);

        mGridData = new ArrayList<>();
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, mGridData);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                GridItem item = (GridItem) parent.getItemAtPosition(position);
                Result result = results.get(position);

                //Create intent
                Intent intent = new Intent(QueueActivity.this, MediaDetailsActivity.class);

                //Pass the image title and url to MediaDetailsActivity
                intent.putExtra("title", result.getTitle()).
                        putExtra("id", result.getId()).
                        putExtra("tMDBid", result.getThemoviedb()).
                        putExtra("selected_source", result.source);

                if (result.type.equals("movies")) {
                    intent.putExtra("media_type", result.type).
                            putExtra("image", result.getPoster120x171()).
                            putExtra("year", Long.toString(result.getReleaseYear()));
                } else {
                    intent.putExtra("media_type", result.type).
                            putExtra("image", result.getArtwork_304x171()).
                            putExtra("year",result.getReleaseDate());
                }

                startActivity(intent);
            }
        });

    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Movies> {
        @Override
        protected Movies doInBackground(Void... params) {
            try {

                String url = "";
                Result result= null;
                for(Long key : Queue.get().returnKeys()){
                    Log.d(TAG, "HttpRequestTask --> key = " + key);
                    String media_type = Queue.get().returnType(key);
                    url = "http://api-public.guidebox.com/v2/" + media_type + "/" + key +
                            "?api_key=c302491413726d93c00a4b0192f8bc55fdc56da4&movie_id=143441";
                    Log.d(TAG, "HttpRequestTask --> url = " + url);
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().add
                            (new MappingJackson2HttpMessageConverter());
                    result = restTemplate.getForObject(url, Result.class);
                    result.source = Queue.get().returnService(key);
                    result.type = media_type;

                    if(result != null) {
                        Log.d(TAG, "ADDALL --> " + result.toString());
                        results.add(result);
                    }
                    else
                        Log.d(TAG, "ADDALL --> NULL!!!");
                }

                GridItem item;
                if(results != null)
                    for (Result r : results) {
                        item = new GridItem();
                        item.setTitle(r.getTitle());
                        if (r.type.equals("movies")) {
                            item.setImage(r.getPoster120x171());
                        } else {
                            item.setImage(r.getArtwork_304x171());
                        }
                        mGridData.add(item);
                    }

                Movies movie = null;
                return movie;
            } catch (Exception e) {
                Log.e(TAG, "EXCEPTION::: " + e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Movies movies) {
            Log.d(TAG, "onPostExecute");
            int a = mGridData.size();
            Log.d(TAG, "mGridData size = " + Integer.toString(a));
            gridAdapter.setGridData(mGridData);
        }
    }

    //true if there's internet
    private boolean checkInternet(){
        Log.d(TAG, "checkInternet");
        if (InternetAccess.getInstance(this).isOnline()) {
            return true;
        } else {
            Toast.makeText(getBaseContext(), R.string.offline, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    //returns true if button is pressed
    private void getInternet() {

        Log.d(TAG, "getInternet");
        new AlertDialog.Builder(QueueActivity.this)
                .setTitle(R.string.no_connection)
                .setMessage(R.string.press_to_refresh)
                .setNeutralButton(R.string.refresh, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(!checkInternet()){
                            getInternet();
                        }
                        else
                            restart();
                    }
                })
                .show();
    }
}
