package com.gmail.sandyakakom;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.gmail.sandyakakom.adapter.Adapter;
import com.gmail.sandyakakom.helper.DbHelper;
import com.gmail.sandyakakom.model.Data;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


    public class   MainActivity extends AppCompatActivity {

        ListView listView;
        AlertDialog.Builder dialog;
        List<Data> itemList = new ArrayList<Data>();
        Adapter adapter;
        DbHelper SQLite = new DbHelper(this);

        public static final String TAG_ID = "id";
        public static final String TAG_NAME = "name";
        public static final String TAG_ADDRESS = "address";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            SQLite = new DbHelper(getApplicationContext());

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            listView = (ListView) findViewById(R.id.list_view);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, AddEdit.class);
                    startActivity(intent);
                }
            });

            adapter = new Adapter(MainActivity.this, itemList);
            listView.setAdapter(adapter);

            // long press listview to show edit and delete
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView arg0, View arg1,
                                        final int position, long id) {
                    // TODO Auto-generated method stub
                    final String idx = itemList.get(position).getId();
                    final String name = itemList.get(position).getName();
                    final String address = itemList.get(position).getAddress();

                    final CharSequence[] dialogitem = {"Panggil", "Ubah", "Hapus"};
                    dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setCancelable(true);
                    dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                        @SuppressLint("MissingPermission")
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            switch (which) {
                                case 0:
                                    Intent call = new Intent(Intent.ACTION_CALL);
                                    call.setData(Uri.parse("tel:" + address));
                                    startActivity(call);
                                    break;
                                case 1:
                                    Intent intent = new Intent(MainActivity.this, AddEdit.class);
                                    intent.putExtra(TAG_ID, idx);
                                    intent.putExtra(TAG_NAME, name);
                                    intent.putExtra(TAG_ADDRESS, address);
                                    startActivity(intent);
                                    break;
                                case 2:
                                    SQLite.delete(Integer.parseInt(idx));
                                    itemList.clear();
                                    getAllData();
                                    break;

                            }
                        }}).show();

                }
            });

            getAllData();

        }

        private void getAllData() {
            ArrayList<HashMap<String, String>> row = SQLite.getAllData();

            for (int i = 0; i < row.size(); i++) {
                String id = row.get(i).get(TAG_ID);
                String poster = row.get(i).get(TAG_NAME);
                String title = row.get(i).get(TAG_ADDRESS);

                Data data = new Data();

                data.setId(id);
                data.setName(poster);
                data.setAddress(title);

                itemList.add(data);
            }

            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onResume() {
            super.onResume();
            itemList.clear();
            getAllData();
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
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }
