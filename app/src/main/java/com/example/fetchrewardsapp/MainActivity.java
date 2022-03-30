package com.example.fetchrewardsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import org.apache.commons.io.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json";
    public static final String TAG  = "MainActivity";
    ItemTouchHelper.SimpleCallback simpleCallback;
    LottieAnimationView lottieAnimationView;
    TextInputLayout textInputLayout;
    AutoCompleteTextView autoCompleteTextView;
    RecyclerView recyclerView;
    ItemAdapter adapter;
    Item deleted = null;
    List<Item> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lottieAnimationView  = findViewById(R.id.lottie_layer_name);
        textInputLayout = findViewById(R.id.drop_down);
        autoCompleteTextView = findViewById(R.id.sorts);

        String[] sorts  = {"by List ID", "by ID", "by Name"};
        ArrayAdapter<String> sortsAdapter = new ArrayAdapter<>(this, R.layout.sort_item, sorts);
        autoCompleteTextView.setAdapter(sortsAdapter);

        AsyncHttpClient client = new AsyncHttpClient();
        recyclerView = findViewById(R.id.recyclerView);
        list = new ArrayList<>();

        adapter = new ItemAdapter(list, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setQueryHint("Search");
        searchView.clearFocus();

        getJSONData(client);
        search(searchView);
        swipeDelete();
        sortItems();

    }

    private void search(SearchView view) {
        view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filerList(newText);
                return true;
            }
        });
    }

    private void swipeDelete() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            deleted = list.get(position);
            list.remove(position);
            adapter.notifyItemRemoved(position);
            Snackbar.make(recyclerView, "Undo", Snackbar.LENGTH_LONG)
                    .setAction("Undo Delete", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            list.add(position, deleted);
                            adapter.notifyItemInserted(position);
                        }
                    }).setActionTextColor(getResources().getColor(R.color.white)).show();

        }
    };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void sortItems() {
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String sort = (String) adapterView.getItemAtPosition(i);
                Toast.makeText(MainActivity.this, (String) adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();

                if(sort.equals("by List ID")){
                    Collections.sort(list, Item.ItemListIdSort);
                    adapter.notifyDataSetChanged();
                }
                if(sort.equals("by Name")){
                    Collections.sort(list, Item.ItemNameSort);
                    adapter.notifyDataSetChanged();
                }
                if(sort.equals("by ID")){
                    Collections.sort(list, Item.ItemIdSort);
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }


    private void getJSONData(AsyncHttpClient client){
        client.get(URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONArray jsonArray = json.jsonArray;

                try{
                    Log.i(TAG, "Array: " + jsonArray.toString());
                    list.addAll(Item.fromJSONArray(jsonArray));
                    adapter.notifyDataSetChanged();

                }catch(JSONException e){
                    Log.e(TAG, "hit json exception");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure")                                                                                              ;

            }
        });

    }

    private void filerList(String newText) {
        List<Item> filtered = new ArrayList<>();
        for(Item item : list){
            if(String.valueOf(item.getListId()).contains(newText)){
                filtered.add(item);
            }

            adapter.setFilteredList(filtered);

        }
    }

}