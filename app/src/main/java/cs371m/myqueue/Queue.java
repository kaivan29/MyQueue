package cs371m.myqueue;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//Singleton class, should be called from MediaDetailsActivity
//should it be called form anywhere because of the toolbar?
public class Queue {

    private static Queue instance;
    private Map<Long, List<String>> queue;
    //Long=id, String=title. SharedPreferences,
    // key = movie:::id. value = service

    private static final String TAG = "Queue";
    private MenuActivity app;

    private Queue(){
        queue = new HashMap<Long, List<String>>();

        app = MenuActivity.get();
        //cleanMovies();
        parseMovies();
    }

    protected static Queue get() {
        Log.d(TAG, "Queue get");
        if(instance == null)
            instance = getSync();
        return instance;
    }

    private static synchronized Queue getSync() {
        if(instance == null)
            instance = new Queue();
        return instance;
    }

    protected boolean movieExists(Long movie_id){
        return queue.containsKey(movie_id);
    }

    protected boolean addMovie(Long movie_id, String selected_source, String media_type){
        if(movieExists(movie_id))
            return false;

        List<String> movie= new ArrayList<String>();
        movie.add(selected_source);
        movie.add(media_type);

        // add movie to user's queue on firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String userId = user.getUid();
        mDatabase.child("users").child(userId).child("queue").child
                (movie_id.toString()).setValue(movie);

        queue.put(movie_id, movie);
        writeMovie(movie_id, movie);
        displayQueue();
        return true;
    }

    private void displayQueue(){
        for(Long x : queue.keySet()){
            Log.d(TAG, "displayQueue --> id = " + x + ", service = " + queue.get(x));
        }
    }

    private void writeMovie(Long movie_id, List<String> movie){
        HelperSharedPreferences.putSharedPreferencesString(app.getApplicationContext(),
                HelperSharedPreferences.key1_prefix + movie_id, movie.get(0));
        HelperSharedPreferences.putSharedPreferencesString(app.getApplicationContext(),
                HelperSharedPreferences.key2_prefix + movie_id, movie.get(1));
    }

    //must run when queue is started, to load it up from the sharedprefs
    private void parseMovies(){
        Map<String, ?> allEntries = returnMap();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("Queue", entry.getKey());
            if(entry.getKey().substring(0,3).equals("mov")) {
                Log.d(TAG, "parseMovies --> id = " +
                        entry.getKey().substring(entry.getKey().lastIndexOf(':') + 1)
                        + ", service = " + entry.getValue());
                Long movieId = new Long(entry.getKey().substring(entry.getKey().lastIndexOf(':') +1 ));
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences
                        (app.getBaseContext());
                String media_type = sharedPrefs.getString("type_media:::" + movieId, "");
                List<String> movie= new ArrayList<String>();
                movie.add(entry.getValue().toString());
                movie.add(media_type);
                queue.put(movieId, movie);
            }
        }
    }

    //returns a map of all what's in the sharedprefs
    private Map<String, ?> returnMap(){
        return HelperSharedPreferences.getAll(app.getApplicationContext());
    }

    protected Set<Long> returnKeys(){
        return queue.keySet();
    }

    protected String returnService(Long key){
        return queue.get(key).get(0).toString();
    }

    protected String returnType(Long key){
        return queue.get(key).get(1).toString();
    }

    protected boolean deleteMovie(Long movie_id){
        Object exist = queue.remove(movie_id);
        if(exist != null) {
            HelperSharedPreferences.deleteSharedPreferencesKey(app.getApplicationContext(),
                    HelperSharedPreferences.key1_prefix + movie_id);
            HelperSharedPreferences.deleteSharedPreferencesKey(app.getApplicationContext(),
                    HelperSharedPreferences.key2_prefix + movie_id);

            // remove movie from user's queue on firebase
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            String userId = user.getUid();
            mDatabase.child("users").child(userId).child("queue").child
                    (movie_id.toString()).removeValue();
        }
        return exist != null;
    }

    public void deleteAllMovies(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String userId = user.getUid();
        for (Long x : queue.keySet()){
            HelperSharedPreferences.deleteSharedPreferencesKey(app.getApplicationContext(),
                    HelperSharedPreferences.key1_prefix + x);
            HelperSharedPreferences.deleteSharedPreferencesKey(app.getApplicationContext(),
                    HelperSharedPreferences.key2_prefix + x);

            mDatabase.child("users").child(userId).child("queue").child
                    (x.toString()).removeValue();
        }
        queue.clear();
    }

    public void clearQueue() {
        queue.clear();
    }

    //only for debugging, will clean the sharedprefs of incorrect inputs
    public void cleanMovies(){
        Map<String, ?> allEntries = returnMap();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if(entry.getKey().substring(0,3).equals("mov") && !entry.getKey().contains(":::")) {
                Log.d(TAG, "cleanMovies --> deleting key " + entry.getKey());
                HelperSharedPreferences.deleteSharedPreferencesKey(app.getApplicationContext(),
                        entry.getKey());
            }
            if(entry.getKey().substring(0,3).equals("typ") && !entry.getKey().contains(":::")) {
                Log.d(TAG, "cleanMovies --> deleting key " + entry.getKey());
                HelperSharedPreferences.deleteSharedPreferencesKey(app.getApplicationContext(),
                        entry.getKey());
            }
        }
    }

    //only for debugging
    private void displayPrefs(){
        Map<String, ?> allEntries = returnMap();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d(TAG, "displayPrefs --> " + entry.getKey() + ": " + entry.getValue().toString());
        }
    }
}


