package com.example.shreyaskulkarni.weatherprofinal;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static com.example.shreyaskulkarni.weatherprofinal.LessDetailedFragment.inputStream;

public class MoreDetailedFragment extends Fragment {
    TextView deg,mindeg,maxdeg,weatherType,dateV,epochdateV;
    String todaysMax="", todaysMin="", todaysWeatherType="", weatherIconNo="", dayname="", date="", epochdate="",todaysAvg="";
    ImageView imageView;
    Context c;

    public static MoreDetailedFragment newInstance(Contact contact) {
        MoreDetailedFragment more = new MoreDetailedFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putParcelable("weather", contact);
        more.setArguments(args);
        return more;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_more_detailed, container, false);
        Bundle args = getArguments();
        Contact weather = args.getParcelable("weather");
        todaysMax=weather.getMax();
        todaysMin=weather.getMin();
        todaysAvg=weather.getAvg();
        date=weather.getDate();
        epochdate=weather.getEpochdate();
        todaysWeatherType=weather.getType();
        weatherIconNo=weather.getWeatherIconNo();
        c=getActivity();

        deg = v.findViewById(R.id.textView5);
        weatherType = v.findViewById(R.id.textView7);
        maxdeg = v.findViewById(R.id.textView11);
        mindeg = v.findViewById(R.id.textView16);
        dateV = v.findViewById(R.id.textView19);
        epochdateV = v.findViewById(R.id.textView20);
        imageView = v.findViewById(R.id.imageView3);

        maxdeg.setText(todaysMax);
        mindeg.setText(todaysMin);
        dateV.setText(date);
        epochdateV.setText(epochdate);
        weatherType.setText(todaysWeatherType);
        //if(todaysAvg.length()>4)
        deg.setText(todaysAvg.substring(0,4));
        if (Integer.parseInt(weatherIconNo)<10)
            Glide.with(c).load("https://developer.accuweather.com/sites/default/files/0" + weatherIconNo + "-s.png").into(imageView);
        else
            Glide.with(c).load("https://developer.accuweather.com/sites/default/files/" + weatherIconNo + "-s.png").into(imageView);


        return v;
    }



}
