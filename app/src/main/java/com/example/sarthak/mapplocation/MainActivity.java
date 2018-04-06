package com.example.sarthak.mapplocation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String ADDRESS = "location.mapplocation.ADDRESS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void findOnMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String address = editText.getText().toString();
        intent.putExtra(ADDRESS, address);
        startActivity(intent);
    }
}
