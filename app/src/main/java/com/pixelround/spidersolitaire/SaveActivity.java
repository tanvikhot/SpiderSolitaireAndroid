package com.pixelround.spidersolitaire;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.pixelround.spidersolitaire.messages.MessageEvent;

import org.greenrobot.eventbus.EventBus;

public class SaveActivity extends AppCompatActivity implements SaveFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);

    }

    @Override
    public void onListFragmentInteraction(String saveName) {
        Toast.makeText(getApplicationContext(), "Restarting " + saveName, Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String save = sharedPreferences.getString(saveName, "");
        EventBus.getDefault().post(new MessageEvent(save));
        finish();
    }
}
