package cs371m.myqueue;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_layout);

        Toolbar settingsToolbar = (Toolbar)findViewById(R.id.settings_toolbar);
        settingsToolbar.setTitle("Settings");
        setSupportActionBar(settingsToolbar);
    }

    /**
     * This is the "top-level" fragment, showing a list of items that the
     * user can pick.  Upon picking an item, it takes care of displaying the
     * data to the user as appropriate based on the current UI layout.
     */

    public static class SettingsFragment extends ListFragment {

        int mCurCheckPosition = 0;

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            String[] rawData = getResources().getStringArray(R.array.settings_options);
            List<String> sources = new ArrayList(Arrays.asList(rawData));

            // Populate list with our static array of titles.
            setListAdapter(new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_activated_1,
                    sources));

            /*if (savedInstanceState != null) {
                // Restore last state for checked position.
                mCurCheckPosition
                        = savedInstanceState.getInt("curChoice", 0);
            }*/

            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        }

        @Override
        public void onStart() {
            super.onStart();
            getListView().setItemChecked(0, false);
            getListView().setItemChecked(1, false);
            getListView().setItemChecked(2, false);
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putInt("curChoice", mCurCheckPosition);
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            openSetting(position);
        }

        void openSetting(int index) {
            mCurCheckPosition = index;

            switch (index) {
                case 0:     //FAQ
                    startActivity(new Intent(getActivity(), FAQActivity.class));
                    break;
                case 1:     //About Us
                    startActivity(new Intent(getActivity(), AboutActivity.class));
                    break;
                case 2:     //Log Out

                    // clear all shared prefences
                    MenuActivity app = MenuActivity.get();
                    Map<String, ?> allEntries = HelperSharedPreferences.getAll
                            (app.getApplicationContext());;
                    for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                        Log.d("LogOut", entry.getKey());
                        if(!(entry.getKey().equals(getString(R.string.pref_previously_started)))) {
                            HelperSharedPreferences.deleteSharedPreferencesKey
                                    (app.getApplicationContext(), entry.getKey());
                        }
                    }

                    // clear queue
                    Queue.get().clearQueue();

                    // sign out of firebase
                    FirebaseAuth.getInstance().signOut();

                    // return to login page
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    getActivity().finish();
                    break;
            }
        }
    }
}
