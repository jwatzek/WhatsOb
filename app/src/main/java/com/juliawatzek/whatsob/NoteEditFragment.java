package com.juliawatzek.whatsob;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.R.id.message;


public class NoteEditFragment extends Fragment {

    private TextView title;
    private EditText message, observer, estrous, comments;
    private CheckBox wasFed, hadFoodInEnclosure;
    private ImageView icon;
    private Note.Category savedButtonCategory;
    private AlertDialog confirmDialogObject;

    private long noteId = 0;

    public NoteEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentLayout = inflater.inflate(R.layout.fragment_note_edit, container, false);

        // grab widget references from layout
        title = (TextView) fragmentLayout.findViewById(R.id.editNoteTitle);
        message = (EditText) fragmentLayout.findViewById(R.id.editNoteMessage);
        icon = (ImageView) fragmentLayout.findViewById(R.id.editNoteIcon);
        Button saveButton = (Button) fragmentLayout.findViewById(R.id.saveNote);
        observer = (EditText) fragmentLayout.findViewById(R.id.editNoteObserver);
        estrous = (EditText) fragmentLayout.findViewById(R.id.editNoteEstrous);
        comments = (EditText) fragmentLayout.findViewById(R.id.editNoteComments);
        wasFed = (CheckBox) fragmentLayout.findViewById(R.id.editWasFed);
        hadFoodInEnclosure = (CheckBox) fragmentLayout.findViewById(R.id.editHadFoodInEnclosure);

        // populate widgets with note data
        Intent intent = getActivity().getIntent();

        title.setText(intent.getExtras().getString(MainActivity.NOTE_TITLE_EXTRA, ""));
        message.setText(intent.getExtras().getString(MainActivity.NOTE_MESSAGE_EXTRA, ""));
        noteId = intent.getExtras().getLong(MainActivity.NOTE_ID_EXTRA, 0);
        observer.setText(intent.getExtras().getString(MainActivity.NOTE_OBSERVER_EXTRA));
        estrous.setText(intent.getExtras().getString(MainActivity.NOTE_ESTROUS_EXTRA));
        comments.setText(intent.getExtras().getString(MainActivity.NOTE_COMMENTS_EXTRA));
        wasFed.setChecked(intent.getExtras().getBoolean(MainActivity.NOTE_WAS_FED_EXTRA));
        hadFoodInEnclosure.setChecked(intent.getExtras().getBoolean(MainActivity.NOTE_HAD_FOOD_IN_ENCLOSURE_EXTRA));

        savedButtonCategory = (Note.Category) intent.getSerializableExtra(MainActivity.NOTE_CATEGORY_EXTRA);
        icon.setImageResource(Note.categoryToDrawable(savedButtonCategory));

        buildConfirmDialog();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialogObject.show();
            }
        });

        return fragmentLayout;
    }

    private void buildConfirmDialog() {

        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getActivity());
        confirmBuilder.setTitle("Are you sure?");
        confirmBuilder.setMessage("Are you sure you want to save the note?");

        confirmBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NotebookDbAdapter dbAdapter = new NotebookDbAdapter(getActivity().getBaseContext());
                dbAdapter.open();

                // update note in database
                dbAdapter.updateNote(noteId, title.getText() + "", message.getText() + "",
                        savedButtonCategory, observer.getText() + "", estrous.getText() + "", comments.getText() + "", wasFed.isChecked(),
                        hadFoodInEnclosure.isChecked());

                dbAdapter.close();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        confirmBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // do nothing here.
            }
        });

        confirmDialogObject = confirmBuilder.create();

    }
}
