package com.eugene.restapi_fatsecret.SearchRowAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eugene.restapi_fatsecret.R;

import java.util.ArrayList;


public class SearchAdapter extends ArrayAdapter<Item> {

    private final Context mContext;
    private final ArrayList<Item> mItem;

    public SearchAdapter(Context context, ArrayList<Item> itemsArrayList) {
        super(context, R.layout.search_row, itemsArrayList);
        this.mContext = context;
        this.mItem = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.search_row, parent, false);
        TextView mFoodName = (TextView) v.findViewById(R.id.food_name);
        TextView mBrand = (TextView) v.findViewById(R.id.food_brand);
        TextView mFoodDescription = (TextView) v.findViewById(R.id.food_description);
        mFoodName.setText(mItem.get(position).getTitle());
        mFoodDescription.setText(mItem.get(position).getFoodDescription());
        mBrand.setText(mItem.get(position).getBrand());
        return v;
    }

}
