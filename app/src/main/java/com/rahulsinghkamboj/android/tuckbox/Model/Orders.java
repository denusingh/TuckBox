package com.rahulsinghkamboj.android.tuckbox.Model;

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Orders {
    public String mealName;
    public String mealOption;
    public String location;
    public String address;
    public String timings;
    public String cardDetails;

    public Orders(){}

    public Orders(String mealName, String mealOption, String location, String address, String timings, String cardDetails) {
        this.mealName = mealName;
        this.mealOption = mealOption;
        this.location = location;
        this.address = address;
        this.timings = timings;
        this.cardDetails = cardDetails;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealOption() {
        return mealOption;
    }

    public void setMealOption(String mealOption) {
        this.mealOption = mealOption;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }

    public String getCardDetails() {
        return cardDetails;
    }

    public void setCardDetails(String cardDetails) {
        this.cardDetails = cardDetails;
    }
}
