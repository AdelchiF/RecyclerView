package com.example.adelchi.recyclerview;

import android.content.res.Configuration;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;

public class MainActivity extends AppCompatActivity implements RecyclerViewOnClickListner {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<Persona> persone = new ArrayList<>();
    private Toolbar toolbar;
    private List<String> drawerElem = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private ListView listViewNavDrawer;

    private boolean checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        NavigationDrawerFragment navigationDrawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.nav_draw_fragment);
        navigationDrawerFragment.setUp(R.id.nav_draw_fragment, drawerLayout, toolbar);

        /*
        drawerElem.add("Elemento 1");
        drawerElem.add("Elemento 2");
        drawerElem.add("Elemento 3");

        listViewNavDrawer = (ListView)findViewById(R.id.left_drawer);

        listViewNavDrawer.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerElem));

        listViewNavDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String elem_selected = ((TextView)view).getText().toString();
                Toast.makeText(getApplicationContext(), "Selezionato " + elem_selected, Toast.LENGTH_SHORT).show();
            }
        });
        */

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
        adapter = new MyAdapter(persone, this);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    public void getRecyclerViewPositionListner(int position) {
        persone.remove(position);
        adapter.notifyItemRemoved(position);
        Toast.makeText(this, "Prova posizione numero: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getRecyclerViewPositionListnerCheckBox(int position) {
        Persona persona = persone.get(position);
        if (!persona.getChecked())
            persona.setChecked(true);
        else
            persona.setChecked(false);
    }

    @Override
    public void getRecyclerViewPoistionLongClick(int position) {
        Persona persona = persone.get(position);
        Snackbar.make(getCurrentFocus(), persona.getEmail(), Snackbar.LENGTH_SHORT).show();
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
        switch (id){
            case R.id.select_all:
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
            case R.id.add_elem:
                persone.add(persone.size(), new Persona("adelchi" + persone.size() + "@gmail.com", false));
                adapter.notifyItemInserted(persone.size());
                recyclerView.smoothScrollToPosition(persone.size());
                return true;
        }
        /*if (id == R.id.select_all) {
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
        }*/

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
