package com.example.shreyaskulkarni.weatherprofinal;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LessDetailedFragment.LessDetail {

    LessDetailedFragment lessfragment;
    boolean isTablet,isLand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        isTablet = getResources().getBoolean(R.bool.isTablet);
        isLand = getResources().getBoolean(R.bool.isLand);
        if (isTablet) {
            lessfragment = new LessDetailedFragment();
            fragmentTransaction.add(R.id.frame_container_1, lessfragment);
            fragmentTransaction.commit();
        } else {
            if(isLand){
                lessfragment = new LessDetailedFragment();
                fragmentTransaction.add(R.id.frameLayout2, lessfragment);
                fragmentTransaction.commit();
            }
            else {
                lessfragment = new LessDetailedFragment();
                fragmentTransaction.add(R.id.fragment_container, lessfragment);
                fragmentTransaction.commit();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, Settings_Activity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListClick(Contact contact) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        MoreDetailedFragment moreDetailedFragment = new MoreDetailedFragment().newInstance(contact);

        if (isTablet) {
            fragmentTransaction.replace(R.id.frame_container_2, moreDetailedFragment);
            fragmentTransaction.commit();
        } else {
            if(isLand){
                fragmentTransaction.replace(R.id.frameLayout, moreDetailedFragment);
                fragmentTransaction.commit();
            }
            else {
                fragmentTransaction.hide(lessfragment);
                fragmentTransaction.add(R.id.fragment_container, moreDetailedFragment).addToBackStack(null);

                fragmentTransaction.commit();
            }
        }


    }
}
