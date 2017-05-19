package cs371m.myqueue;

import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectSourcesActivity extends AppCompatActivity {

    private static SharedPreferences sharedPrefs;
    private SelectSourcesActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        setContentView(R.layout.select_source_layout);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        final Button continue_button = (Button) findViewById(R.id.sources_continue);
        continue_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!sharedPrefs.getBoolean(getString(R.string.netflix_source), false) &&
                        !sharedPrefs.getBoolean(getString(R.string.hulu_source), false) &&
                        !sharedPrefs.getBoolean(getString(R.string.hbo_source), false) &&
                        !sharedPrefs.getBoolean(getString(R.string.amazon_source), false)) {
                    Toast.makeText(getBaseContext(),
                            R.string.force_select_service, Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(activity, MenuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    SelectSourcesActivity.this.finish();
                }
            }
        });

        Toolbar selectSourcesToolbar = (Toolbar)findViewById(R.id.select_sources_toolbar);
        selectSourcesToolbar.setTitle(R.string.activity_select);
        setSupportActionBar(selectSourcesToolbar);

    }

    public static class SourcesFragment extends ListFragment {

        int mCurCheckPosition = 0;

        private DatabaseReference mDatabase;
        private String userId;

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            mDatabase = FirebaseDatabase.getInstance().getReference();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            userId = user.getUid();

            String[] rawData = getResources().getStringArray(R.array.sources);
            List<String> sources = new ArrayList(Arrays.asList(rawData));

            // Populate list with our static array of titles.
            setListAdapter(new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_activated_1,
                    sources));

            if (savedInstanceState != null) {
                // Restore last state for checked position.
                mCurCheckPosition
                        = savedInstanceState.getInt("curChoice", 0);
            }

            getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            getListView().setItemChecked(0, sharedPrefs.getBoolean(getString(R.string.netflix_source), false));
            getListView().setItemChecked(1, sharedPrefs.getBoolean(getString(R.string.hulu_source), false));
            getListView().setItemChecked(2, sharedPrefs.getBoolean(getString(R.string.hbo_source), false));
            getListView().setItemChecked(3, sharedPrefs.getBoolean(getString(R.string.amazon_source), false));
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putInt("curChoice", mCurCheckPosition);
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            addSource(position);
        }

        void addSource(final int index) {
            mCurCheckPosition = index;

            SharedPreferences.Editor edit = sharedPrefs.edit();
            boolean source_selected = false;
            String source = getString(R.string.netflix_source);
            switch (mCurCheckPosition) {
                case 0:
                    source = getString(R.string.netflix_source);
                    source_selected = sharedPrefs.getBoolean(source, false);
                    break;
                case 1:
                    source = getString(R.string.hulu_source);
                    source_selected = sharedPrefs.getBoolean(source, false);
                    break;
                case 2:
                    source = getString(R.string.hbo_source);
                    source_selected = sharedPrefs.getBoolean(source, false);
                    break;
                case 3:
                    source = getString(R.string.amazon_source);
                    source_selected = sharedPrefs.getBoolean(source, false);
                    break;
            }
            if(!source_selected) {
                mDatabase.child("users").child(userId).child("services").child
                        (source).setValue(true);
                edit.putBoolean(source, true);
            } else {
                mDatabase.child("users").child(userId).child("services").child
                        (source).setValue(false);
                edit.putBoolean(source, false);
            }
            edit.commit();
        }
    }
}