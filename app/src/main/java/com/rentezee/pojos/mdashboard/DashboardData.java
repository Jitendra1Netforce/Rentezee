package com.rentezee.pojos.mdashboard;

import java.util.ArrayList;

/**
 * Created by JitendraSingh on 10/26/2016.
 */

public class DashboardData {
    private ArrayList<CategoryData> categories;
    private ArrayList<Slider> sliderData;
    private ArrayList<Trending> trendings;

    public ArrayList<CategoryData> getCategories() {
        return categories;
    }

    public ArrayList<Slider> getSliderData() {
        return sliderData;
    }

    public ArrayList<Trending> getTrendings() {
        return trendings;
    }

    @Override
    public String toString() {
        return "{" +
                "categories=" + categories +
                ", sliderData=" + sliderData +
                ", trendings=" + trendings +
                '}';
    }
}
