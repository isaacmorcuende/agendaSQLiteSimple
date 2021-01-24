package com.example.agendasqlitejava;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class MostrarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar);

        File f = getDatabasePath("agenda.sqlite");
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(f.getPath(), null);

        String query = "SELECT * FROM contactos";
        Cursor cursor = db.rawQuery(query,null);
        ArrayList<String> contactos = new ArrayList<>();

        while(cursor.moveToNext()){
            String contacto = cursor.getString(0) + " " + cursor.getString(1) + ", " + cursor.getString(3) +
                    "\n"+cursor.getString(2);
            contactos.add(contacto);
        }

        ListView lv = (ListView) findViewById(R.id.lista_mostrar);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactos);
        lv.setAdapter(adapter);
    }
}