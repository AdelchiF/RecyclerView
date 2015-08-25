package com.example.adelchi.recyclerview;

import android.content.res.Configuration;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<Persona> persone = new ArrayList<>();

    private boolean checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new LinearLayoutManager(this);
        } else {
            layoutManager = new GridLayoutManager(this, 2);
        }

        if (savedInstanceState != null) {
            Gson gsonPersone = new Gson();
            Type type = new TypeToken<List<Persona>>() {
            }.getType();
            persone = gsonPersone.fromJson(savedInstanceState.getString("persone"), type);
        } else {

            for (int i = 0; i <= 150; i++) {
                persone.add(i, new Persona("adelchi" + i + "@gmail.com", false));
            }

        }
        adapter = new MyAdapter(persone);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.select_all) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    for (Persona persona : persone
                            ) {
                        if (checked) {
                            persona.setChecked(false);
                            ((TextView) findViewById(R.id.select_all)).setText("Select all");
                        } else {
                            persona.setChecked(true);
                            ((TextView) findViewById(R.id.select_all)).setText("Deselect all");
                        }
                    }
                }
            });
            thread.run();
            checked = !checked;
            adapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Gson gsonPersone = new Gson();
        String elencoPersone = gsonPersone.toJson(persone);
        outState.putString("persone", elencoPersone);
    }

    public class Persona {

        private String email;
        private Boolean checked;

        public Persona(String email, Boolean checked) {
            this.email = email;
            this.checked = checked;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Boolean getChecked() {
            return checked;
        }

        public void setChecked(Boolean checked) {
            this.checked = checked;
        }

    }
}
