package com.example.shreyaskulkarni.weatherprofinal;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Contact implements Parcelable {
    private String min,max,type,weatherIconNo,avg,date,epochdate;
    public ArrayList<Contact> arrayList;
    //ArrayList<Contact> arrayList = new ArrayList<Contact>();

    public Contact(String min,String max,String type,String weatherIconNo,String date,String epochdate){
        this.max=max;
        this.min=min;
        this.type=type;
        this.weatherIconNo=weatherIconNo;
        avg=((Double.parseDouble(max)+Double.parseDouble(min))/2)+"";
        this.date=date;
        this.epochdate=epochdate;
    }

    protected Contact(Parcel in) {
        min = in.readString();
        max = in.readString();
        type = in.readString();
        weatherIconNo = in.readString();
        avg = in.readString();
        date = in.readString();
        epochdate = in.readString();
        arrayList = in.createTypedArrayList(Contact.CREATOR);
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    String getMin(){
        return min;
    }

    String getMax(){
        return max;
    }

    String getType(){
        return type;
    }

    String getWeatherIconNo() { return weatherIconNo; }

    public String getAvg() {
        return avg;
    }

    public String getDate() {
        return date;
    }

    public String getEpochdate() {
        return epochdate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(min);
        dest.writeString(max);
        dest.writeString(type);
        dest.writeString(weatherIconNo);
        dest.writeTypedList(arrayList);
        dest.writeString(date);
        dest.writeString(epochdate);
        dest.writeString(avg);
    }
}
