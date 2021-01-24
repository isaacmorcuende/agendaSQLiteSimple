package com.example.agendasqlitejava;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        File f = getDatabasePath("agenda.sqlite");
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(f.getPath(), null);
        String query = "create table if not exists contactos(nombre text, apellido text primary key, telefono text, edad integer)";

        db.execSQL(query);
    }

    public void alta(View v){
        EditText nombre = (EditText)findViewById(R.id.et_nombre);
        EditText apellido = (EditText)findViewById(R.id.et_apellido);
        EditText telefono = (EditText)findViewById(R.id.et_telefono);
        EditText edad = (EditText)findViewById(R.id.et_edad);

        if(nombre.getText().length()>0 && apellido.getText().length()>0 && telefono.getText().length()>0 && edad.getText().length()>0){
            File f = getDatabasePath("agenda.sqlite");
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(f.getPath(), null);

            String query = "INSERT INTO contactos VALUES('"+nombre.getText()+"','"+apellido.getText()+"','"+ telefono.getText()+"',"+edad.getText()+ ")";
            Log.i("e",query);
            db.execSQL(query);
            Toast.makeText(this, "¡Contacto añadido!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "¡Rellena todos los campos!", Toast.LENGTH_SHORT).show();
        }
    }

    public void consultar(View v){
        EditText nombre = (EditText)findViewById(R.id.et_nombre);
        EditText apellido = (EditText)findViewById(R.id.et_apellido);
        EditText telefono = (EditText)findViewById(R.id.et_telefono);
        EditText edad = (EditText)findViewById(R.id.et_edad);

        File f = getDatabasePath("agenda.sqlite");
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(f.getPath(), null);

        String query = "SELECT nombre,telefono,edad FROM contactos WHERE apellido='"+apellido.getText()+"'";
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToNext()){
            nombre.setText(cursor.getString(0));
            telefono.setText(cursor.getString(1));
            edad.setText(cursor.getString(2));
        }else{
            Toast.makeText(this, "No hay ningun contacto con ese apellido", Toast.LENGTH_SHORT).show();
            nombre.setText("");
            telefono.setText("");
            edad.setText("");
        }
    }

    public void baja(View v){
        EditText apellido = (EditText)findViewById(R.id.et_apellido);

        if(apellido.getText().length()>0){
            File f = getDatabasePath("agenda.sqlite");
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(f.getPath(), null);
            String consulta = "SELECT * FROM contactos WHERE apellido='"+apellido.getText()+"'";
            Cursor cursor = db.rawQuery(consulta, null);
            if(cursor.moveToNext()){
                AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
                alertbox.setMessage("¿Estás seguro?");

                alertbox.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    //Funcion llamada cuando se pulsa el boton Si
                    public void onClick(DialogInterface arg0, int arg1) {
                        String query = "DELETE FROM contactos WHERE apellido='"+apellido.getText()+"'";
                        Log.i("e",query);
                        db.execSQL(query);
                        Toast.makeText(MainActivity.this, "¡Contacto eliminado!", Toast.LENGTH_SHORT).show();
                        apellido.setText("");
                    }
                });

                alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    //Funcion llamada cuando se pulsa el boton No
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(MainActivity.this, "No has eliminado ningún contacto", Toast.LENGTH_SHORT).show();
                    }
                });

                //mostramos el alertbox
                alertbox.show();
            }else{
                Toast.makeText(this, "No hay ningun contacto con ese apellido", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Introduce un apellido", Toast.LENGTH_SHORT).show();
        }
    }

    public void modificar(View v){
        EditText nombre = (EditText)findViewById(R.id.et_nombre);
        EditText apellido = (EditText)findViewById(R.id.et_apellido);
        EditText telefono = (EditText)findViewById(R.id.et_telefono);
        EditText edad = (EditText)findViewById(R.id.et_edad);

        if(nombre.getText().length()>0 && apellido.getText().length()>0 && telefono.getText().length()>0 && edad.getText().length()>0){
            File f = getDatabasePath("agenda.sqlite");
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(f.getPath(), null);
            String consulta = "SELECT * FROM contactos WHERE apellido='"+apellido.getText()+"'";
            Cursor cursor = db.rawQuery(consulta, null);

            if(cursor.moveToNext()){
                AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
                alertbox.setMessage("¿Estás seguro de que quieres modificarlo?");

                alertbox.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    //Funcion llamada cuando se pulsa el boton Si
                    public void onClick(DialogInterface arg0, int arg1) {
                        ContentValues contactoMod = new ContentValues();
                        contactoMod.put("nombre",nombre.getText().toString());
                        contactoMod.put("telefono",telefono.getText().toString());
                        contactoMod.put("edad",edad.getText().toString());

                        db.update("contactos",contactoMod,"apellido='"+apellido.getText()+"'",null);
                        Toast.makeText(MainActivity.this, "¡Contacto modificado!", Toast.LENGTH_SHORT).show();
                        nombre.setText("");
                        apellido.setText("");
                        telefono.setText("");
                        edad.setText("");
                    }
                });

                alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    //Funcion llamada cuando se pulsa el boton No
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(MainActivity.this, "No has modificado ningún contacto", Toast.LENGTH_SHORT).show();
                    }
                });

                //mostramos el alertbox
                alertbox.show();
            }else{
                Toast.makeText(this, "No hay ningun contacto con ese apellido", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Rellena todos los campos!", Toast.LENGTH_SHORT).show();
        }
    }

    public void mostrarTodo(View v){
        Intent intent = new Intent(this, MostrarActivity.class);
        startActivity(intent);
    }
}