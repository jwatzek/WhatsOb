package com.juliawatzek.whatsob;


import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static com.juliawatzek.whatsob.MainActivity.REQUEST_EXIT;
import static com.juliawatzek.whatsob.R.drawable.n;

public class MainActivityListFragment extends ListFragment {

    private ArrayList<Note> notes;
    private NoteAdapter noteAdapter;

    private AlertDialog deleteDialogObject;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // read notes from database
        NotebookDbAdapter dbAdapter = new NotebookDbAdapter(getActivity().getBaseContext());
        dbAdapter.open();
        notes = dbAdapter.getAllNotes();
        dbAdapter.close();

        noteAdapter = new NoteAdapter(getActivity(), notes);
        setListAdapter(noteAdapter);

        // register context menu for list view
        registerForContextMenu(getListView());

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        launchNoteDetailActivity(MainActivity.FragmentToLaunch.VIEW, position);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.long_menu_press, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // give me position of whatever note I long-pressed on
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int rowPosition = info.position;
        Note note = (Note) getListAdapter().getItem(rowPosition);

        // returns id of the selected menu item
        switch (item.getItemId()) {

            case R.id.share:

//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//                boolean isFormatCSV = sharedPreferences.getBoolean("file_format", false);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, note + "");

//                if (isFormatCSV) {
//                    intent.putExtra(Intent.EXTRA_TEXT, note.toCSVString());
//                } else { }

                startActivity(Intent.createChooser(intent, getResources().getText(R.string.send_to)));
                return true;

            case R.id.edit:
                launchNoteDetailActivity(MainActivity.FragmentToLaunch.EDIT, rowPosition);
                return true;

            case R.id.delete:
                buildDeleteDialog(note);
                deleteDialogObject.show();
                return true;
        }

        return super.onContextItemSelected(item);

    }

    private void buildDeleteDialog(final Note note) {

        AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(getActivity());
        deleteBuilder.setTitle("Are you sure?");
        deleteBuilder.setMessage("Are you sure you want to delete the note?");

        deleteBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NotebookDbAdapter dbAdapter = new NotebookDbAdapter(getActivity().getBaseContext());
                dbAdapter.open();
                dbAdapter.deleteNote(note.getNoteId());

                // clear all, add back in; notify for refresh
                notes.clear();
                notes.addAll(dbAdapter.getAllNotes());
                noteAdapter.notifyDataSetChanged();

                dbAdapter.close();
            }
        });

        deleteBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // do nothing here.
            }
        });

        deleteDialogObject = deleteBuilder.create();

    }

    private void launchNoteDetailActivity(MainActivity.FragmentToLaunch ftl, int position) {

        // grab the note information associated with whatever note item we clicked on
        Note note = (Note) getListAdapter().getItem(position);

        // create a new intent that launches our NoteDetailActivity
        Intent intent = new Intent(getActivity(), NoteDetailActivity.class);

        // pass along the info of the note we clicked on to our NoteDetailActivity
        intent.putExtra(MainActivity.NOTE_TITLE_EXTRA, note.getTitle());
        intent.putExtra(MainActivity.NOTE_MESSAGE_EXTRA, note.getMessage());
        intent.putExtra(MainActivity.NOTE_CATEGORY_EXTRA, note.getCategory());
        intent.putExtra(MainActivity.NOTE_ID_EXTRA, note.getNoteId());
        intent.putExtra(MainActivity.NOTE_OBSERVER_EXTRA, note.getObserver());
        intent.putExtra(MainActivity.NOTE_COMMENTS_EXTRA, note.getComments());
        intent.putExtra(MainActivity.NOTE_WAS_FED_EXTRA, note.getWasFed());
        intent.putExtra(MainActivity.NOTE_HAD_FOOD_IN_ENCLOSURE_EXTRA, note.getHadFoodInEnclosure());

        switch (ftl) {
            case VIEW:
                intent.putExtra(MainActivity.NOTE_FRAGMENT_TO_LOAD_EXTRA, MainActivity.FragmentToLaunch.VIEW);
                startActivity(intent);
                break;
            case EDIT:
                intent.putExtra(MainActivity.NOTE_FRAGMENT_TO_LOAD_EXTRA, MainActivity.FragmentToLaunch.EDIT);
                startActivity(intent);
                break;
        }


    }

}
