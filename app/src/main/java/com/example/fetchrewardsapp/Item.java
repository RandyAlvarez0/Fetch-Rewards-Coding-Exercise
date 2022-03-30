package com.example.fetchrewardsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class Item {
    int id;
    int listId;
    String name;

    public Item(JSONObject jsonObject) throws JSONException{

            id = jsonObject.getInt("id");
            listId = jsonObject.getInt("listId");
            name = jsonObject.getString("name");
    }

    public static List<Item> fromJSONArray(JSONArray jsonArray) throws JSONException {
        List<Item> itemList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i ++) {
            String nameVal = jsonArray.getJSONObject(i).getString("name");
            if(nameVal.isEmpty() || nameVal.equals("null")){
                Log.i("Item", "name is null or empty");
            }else{
                itemList.add(new Item(jsonArray.getJSONObject(i)));
            }

        }

        return itemList;
    }

    public static Comparator<Item> ItemListIdSort = new Comparator<Item>() {
        @Override
        public int compare(Item t1, Item t2) {
            return t1.getListId() - t2.getListId();
        }
    };

    public static Comparator<Item> ItemIdSort = new Comparator<Item>() {
        @Override
        public int compare(Item t1, Item t2) {
            return t1.getId() - t2.getId();
        }
    };

    public static Comparator<Item> ItemNameSort = new Comparator<Item>() {
        @Override
        public int compare(Item t1, Item t2) {
            Pattern pattern = Pattern.compile("[^0-9]");
            return pattern.matcher(t1.getName()).replaceAll("")
                    .compareTo(pattern.matcher(t2.getName()).replaceAll(""));
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
