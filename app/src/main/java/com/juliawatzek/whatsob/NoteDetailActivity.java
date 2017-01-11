package com.juliawatzek.whatsob;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NoteDetailActivity extends AppCompatActivity {

    boolean suppressBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        createAndAddFragment();
    }

    private void createAndAddFragment() {

        // grab intent and fragment to launch from main activity list fragment
        Intent intent = getIntent();
        MainActivity.FragmentToLaunch fragmentToLaunch =
                (MainActivity.FragmentToLaunch) intent.getSerializableExtra(MainActivity.NOTE_FRAGMENT_TO_LOAD_EXTRA);

        // grab fragment manager and fragment transaction so we can add our edit or view fragment dynamically
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // choose correct fragment to load
        switch (fragmentToLaunch) {
            case EDIT:
                // create and add new note edit fragment (with tag, in case we want to retrieve it)
                NoteEditFragment noteEditFragment = new NoteEditFragment();
                setTitle(R.string.edit_fragment_title);
                fragmentTransaction.add(R.id.note_container, noteEditFragment, "NOTE_EDIT_FRAGMENT");
                break;

            case VIEW:
                // create and add new note view fragment (with tag, in case we want to retrieve it)
                NoteViewFragment noteViewFragment = new NoteViewFragment();
                setTitle(R.string.view_fragment_title);
                fragmentTransaction.add(R.id.note_container, noteViewFragment, "NOTE_VIEW_FRAGMENT");
                break;

            case CREATE:
                // create and add new note edit fragment (with tag, in case we want to retrieve it)
                NoteCreateFragment noteCreateFragment = new NoteCreateFragment();
                setTitle(R.string.create_fragment_title);
                suppressBackPressed = true;
                fragmentTransaction.add(R.id.note_container, noteCreateFragment, "NOTE_CREATE_FRAGMENT");
                break;
        }

        // commit changes to make sure the above actually happens
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        if (!suppressBackPressed) {
            super.onBackPressed();
        }
    }

}
