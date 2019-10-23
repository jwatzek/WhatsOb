package com.juliawatzek.whatsob;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import static com.juliawatzek.whatsob.Note.Category.GABE;
import static com.juliawatzek.whatsob.Note.Category.GRIFFIN;
import static com.juliawatzek.whatsob.Note.Category.LIAM;
import static com.juliawatzek.whatsob.Note.Category.LOGAN;
import static com.juliawatzek.whatsob.Note.Category.MASON;
import static com.juliawatzek.whatsob.Note.Category.NKIMA;

public class MainActivity extends AppCompatActivity {

    public static final String NOTE_ID_EXTRA = "com.juliawatzek.whatsob.Identifier";
    public static final String NOTE_TITLE_EXTRA = "com.juliawatzek.whatsob.Title";
    public static final String NOTE_MESSAGE_EXTRA = "com.juliawatzek.whatsob.Message";
    public static final String NOTE_CATEGORY_EXTRA = "com.juliawatzek.whatsob.Category";
    public static final String NOTE_OBSERVER_EXTRA = "com.juliawatzek.whatsob.Observer";
    public static final String NOTE_ESTROUS_EXTRA = "com.juliawatzek.whatsob.Estrous";
    public static final String NOTE_COMMENTS_EXTRA = "com.juliawatzek.whatsob.Comments";
    public static final String NOTE_WAS_FED_EXTRA = "com.juliawatzek.whatsob.WasFed";
    public static final String NOTE_HAD_FOOD_IN_ENCLOSURE_EXTRA = "com.juliawatzek.whatsob.HadFoodInEnclosure";
    public static final String NOTE_FRAGMENT_TO_LOAD_EXTRA = "com.juliawatzek.whatsob.FragmentToLoad";

    public static int REQUEST_EXIT = 0;
    public static String population;

    public enum FragmentToLaunch {VIEW, EDIT, CREATE}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                population = sharedPreferences.getString("population", "no selection");

                Intent intent = new Intent(view.getContext(), NoteDetailActivity.class);
                intent.putExtra(MainActivity.NOTE_CATEGORY_EXTRA, stringToCategory(population));
                intent.putExtra(MainActivity.NOTE_FRAGMENT_TO_LOAD_EXTRA, MainActivity.FragmentToLaunch.CREATE);
                startActivity(intent);
            }
        });
    }

    private Note.Category stringToCategory(String population) {
        switch (population) {
            case "0":
                return LOGAN;
            case "1":
                return GRIFFIN;
            case "2":
                return NKIMA;
            case "3":
                return MASON;
            case "4":
                return GABE;
            case "5":
                return LIAM;
        }
        return GRIFFIN;
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
            Intent intent = new Intent(this, AppPreferences.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_EXIT)
        {
            finish();
        }
    }
}
