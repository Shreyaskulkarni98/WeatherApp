package com.example.shreyaskulkarni.weatherprofinal;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;



public class LessDetailedFragment extends Fragment {

    static String link1 = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/204108?apikey=jC32mWANHbHI03J1ARnpOQOsBalbwZok&language=en-in&details=false&metric=true";
    String link5 = "http://dataservice.accuweather.com/forecasts/v1/daily/5day/204108?apikey=jC32mWANHbHI03J1ARnpOQOsBalbwZok&language=en-in&details=false&metric=true";
    TextView minView, maxView, weatherTypeView, dayView;
    final String[] nameOfDaysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    int nameOfDaysOfWeekNumber;
    ImageView weatherIcon;
    String todaysMax, todaysMin, todaysWeatherType, weatherIconNo,dayname,date,epochname;
    private static final String TAG = "MA_Less Det Fragment";
    static InputStream inputStream;
    List<Contact> contacts = new ArrayList<>();        //ArrayList of contacts containing all the values for recycler view
    Context c;
    Context context;
    RecyclerView rvContacts;
    private LessDetail lessDetail;
    int z;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_less_detailed, container, false);
        c = getActivity();
        minView = view.findViewById(R.id.mintemp);
        maxView = view.findViewById(R.id.maxtemp);
        weatherTypeView = view.findViewById(R.id.weathertype);
        weatherIcon = view.findViewById(R.id.imageView);
        dayView = view.findViewById(R.id.textView);

        rvContacts = (RecyclerView) view.findViewById(R.id.recyclerView);


        new WeatherDataOneDay().execute(link1);
        new WeatherDataFiveDay().execute(link5);


        return view;


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LessDetail) {
            lessDetail = (LessDetail) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement LessDetail");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        lessDetail = null;
    }

    public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            public TextView minEachView, maxEachView, typeEachView, dayEachView;
            public ImageView weatherEachView;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);
                itemView.setOnClickListener(this);
                minEachView = (TextView) itemView.findViewById(R.id.textView14);
                maxEachView = (TextView) itemView.findViewById(R.id.textView15);
                typeEachView = (TextView) itemView.findViewById(R.id.textView9);
                dayEachView = (TextView) itemView.findViewById(R.id.dayname);
                weatherEachView = (ImageView) itemView.findViewById(R.id.imageView2);
            }

            @Override
            public void onClick(View v) {
                if (lessDetail != null) {
                        lessDetail.onListClick(mContacts.get(getLayoutPosition()));
                }
            }
        }

        private List<Contact> mContacts;

        // Pass in the contact array into the constructor
        public ContactsAdapter(List<Contact> arc) {
            mContacts = arc;
        }

        @Override
        public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.eachpartofrecyclerview, parent, false);

            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(contactView);
            return viewHolder;

        }


        // Involves populating data into the item through holder
        @Override
        public void onBindViewHolder(ContactsAdapter.ViewHolder viewHolder, int position) {
            // Get the data model based on position
            Contact contact = mContacts.get(position);

            // Set item views based on your views and data model
            TextView tv1 = viewHolder.maxEachView;
            tv1.setText(contact.getMax());

            TextView tv2 = viewHolder.minEachView;
            tv2.setText(contact.getMin());

            TextView tv3 = viewHolder.typeEachView;
            tv3.setText(contact.getType());

            TextView tv4 = viewHolder.dayEachView;
            tv4.setText(nameOfDaysOfWeek[((nameOfDaysOfWeekNumber + position) % 7)]);

            ImageView tv5 = viewHolder.weatherEachView;
            if (Integer.parseInt(contact.getWeatherIconNo()) < 10)
                Glide.with(c).load("https://developer.accuweather.com/sites/default/files/0" + contact.getWeatherIconNo() + "-s.png").into(tv5);
            else
                Glide.with(c).load("https://developer.accuweather.com/sites/default/files/" + contact.getWeatherIconNo() + "-s.png").into(tv5);
        }

        @Override
        public int getItemCount() {
            return mContacts.size();
        }
    }

    //API STUFF

    public class WeatherDataOneDay extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                URLConnection urlConn = url.openConnection();
                HttpURLConnection httpConn = (HttpURLConnection) urlConn;
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                int resCode = httpConn.getResponseCode();

                if (resCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpConn.getInputStream();
                }
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String read;

                while ((read = br.readLine()) != null) {
                    //System.out.println(read);
                    sb.append(read);
                }

                br.close();
                String result = sb.toString();
                Log.d(TAG, "doInBackground: " + result);

                try {
                    //JSON CODE
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject jsonHeadline = jsonObject.getJSONObject("Headline");
                    //todaysWeatherType = jsonHeadline.getString("Category");
                    String daynamestring = jsonHeadline.getString("Text");
                    String[] d = daynamestring.split(" ");
                    dayname = d[d.length-2];
                    JSONArray jsonForecast = jsonObject.getJSONArray("DailyForecasts");
                    JSONObject jsonForecastObject = jsonForecast.getJSONObject(0);
                    //date = jsonForecastObject.getString("Date");
                    //epochdate = jsonForecastObject.getString("EpochDate");
                    JSONObject jsonTemp = jsonForecastObject.getJSONObject("Temperature");
                    JSONObject jsonTempMin = jsonTemp.getJSONObject("Minimum");
                    JSONObject jsonTempMax = jsonTemp.getJSONObject("Maximum");
                    todaysMax = jsonTempMax.getString("Value");
                    todaysMin = jsonTempMin.getString("Value");
                    JSONObject jsonDay = jsonForecastObject.getJSONObject("Day");
                    weatherIconNo = jsonDay.getString("Icon");
                    todaysWeatherType = jsonDay.getString("IconPhrase");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            minView.setText(todaysMax);
            maxView.setText(todaysMin);
            dayView.setText("Today");
            weatherTypeView.setText(todaysWeatherType);

            switch (dayname) {
                case "Monday":
                    nameOfDaysOfWeekNumber = 0;
                    break;
                case "Tuesday":
                    nameOfDaysOfWeekNumber = 1;
                    break;
                case "Wednesday":
                    nameOfDaysOfWeekNumber = 2;
                    break;
                case "Thursday":
                    nameOfDaysOfWeekNumber = 3;
                    break;
                case "Friday":
                    nameOfDaysOfWeekNumber = 4;
                    break;
                case "Saturday":
                    nameOfDaysOfWeekNumber = 5;
                    break;
                case "Sunday":
                    nameOfDaysOfWeekNumber = 6;
                    break;
            }
            if (Integer.parseInt(weatherIconNo) < 10)
                Glide.with(c).load("https://developer.accuweather.com/sites/default/files/0" + weatherIconNo + "-s.png").into(weatherIcon);
            else
                Glide.with(c).load("https://developer.accuweather.com/sites/default/files/" + weatherIconNo + "-s.png").into(weatherIcon);
        }
    }


    public class WeatherDataFiveDay extends AsyncTask<String, Void, Void> {
        //Contact c=new Contact();
        String min, max, type, eachWeatherIconNumber;

        @Override
        protected Void doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                URLConnection urlConn = url.openConnection();
                HttpURLConnection httpConn = (HttpURLConnection) urlConn;
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                int resCode = httpConn.getResponseCode();

                if (resCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpConn.getInputStream();
                }
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String read;

                while ((read = br.readLine()) != null) {
                    //System.out.println(read);
                    sb.append(read);
                }

                br.close();
                String result = sb.toString();
                Log.d(TAG, "doInBackground: " + result);
                try {
                    //JSON CODE
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonDailyForecasts = jsonObject.getJSONArray("DailyForecasts");
                    for (int i = 0; i < jsonDailyForecasts.length(); i++) {
                        JSONObject jsonDailyForecastsObject = jsonDailyForecasts.getJSONObject(i);
                        date = jsonDailyForecastsObject.getString("Date");
                        epochname = jsonDailyForecastsObject.getString("EpochDate");
                        JSONObject jsonDay = jsonDailyForecastsObject.getJSONObject("Temperature");
                        JSONObject jsonMinTemp = jsonDay.getJSONObject("Minimum");
                        JSONObject jsonMaxTemp = jsonDay.getJSONObject("Maximum");
                        min = jsonMinTemp.getString("Value");
                        max = jsonMaxTemp.getString("Value");
                        JSONObject jsonday = jsonDailyForecastsObject.getJSONObject("Day");
                        type = jsonday.getString("IconPhrase");
                        eachWeatherIconNumber = jsonday.getString("Icon");
                        Contact con = new Contact(min, max, type, eachWeatherIconNumber,date,epochname);
                        contacts.add(con);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Initialize contacts
            //contacts = c.arrayList;
            // Create adapter passing in the sample user data
            ContactsAdapter adapter = new ContactsAdapter(contacts);
            // Attach the adapter to the recyclerview to populate items
            rvContacts.setAdapter(adapter);
            // Set layout manager to position the items
            rvContacts.setLayoutManager(new LinearLayoutManager(c));
            // That's all!
        }
    }


    public interface LessDetail {

        void onListClick(Contact contact);

    }
}
