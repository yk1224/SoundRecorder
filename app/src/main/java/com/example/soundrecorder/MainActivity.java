package com.example.soundrecorder;

import android.Manifest;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.soundrecorder.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;


/*    //下のオーバーライドまで追加(setcontentviewの項) 2/2
    private SoundPool mSePlayer;
    private final static int MAX_STREAMS = 10;
    private int[] mSound = new int[1];
*/
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

/*        setContentView(R.layout.activity_main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mSePlayer = new SoundPool(MAX_STREAMS,AudioManager.STREAM_MUSIC,0);
        mSound[0] = mSePlayer.load(getApplicationContext(),R.raw.ultra,1);
        Button b1 = (Button) this.findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                mSePlayer.play(mSound[0],1.0f,1.0f,0,0,1.0f);
            }
        });
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }



}