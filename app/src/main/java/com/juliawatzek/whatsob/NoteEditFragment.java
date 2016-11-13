package com.juliawatzek.whatsob;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


public class NoteEditFragment extends Fragment {

    private EditText title, message;
    private ImageButton noteCatButton;
    private Note.Category savedButtonCategory;
    private AlertDialog categoryDialogObject, confirmDialogObject;
    private boolean newNote = false;

    private static final String MODIFIED_CATEGORY = "Modified Category";
    private long noteId = 0;

    public NoteEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            savedButtonCategory = (Note.Category) savedInstanceState.getSerializable(MODIFIED_CATEGORY);
        }

        // grab the bundle that sends along whether or not the noteEditFragment is creating a new note
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            newNote = bundle.getBoolean(NoteDetailActivity.NEW_NOTE_EXTRA, false);
        }

        // Inflate the layout for this fragment
        View fragmentLayout = inflater.inflate(R.layout.fragment_note_edit, container, false);

        // grab widget references from layout
        title = (EditText) fragmentLayout.findViewById(R.id.editNoteTitle);
        message = (EditText) fragmentLayout.findViewById(R.id.editNoteMessage);
        noteCatButton = (ImageButton) fragmentLayout.findViewById(R.id.editNoteButton);
        Button saveButton = (Button) fragmentLayout.findViewById(R.id.saveNote);

        // populate widgets with note data
        Intent intent = getActivity().getIntent();

        title.setText(intent.getExtras().getString(MainActivity.NOTE_TITLE_EXTRA, ""));
        message.setText(intent.getExtras().getString(MainActivity.NOTE_MESSAGE_EXTRA, ""));
        noteId = intent.getExtras().getLong(MainActivity.NOTE_ID_EXTRA, 0);

        // if we came from our list fragment, get category from intent
        // otherwise (i.e., if we changed screen orientation), skip this and just set the image to
        // the category info we retrieved from the bundle
        if (savedButtonCategory == null) {
            savedButtonCategory = (Note.Category) intent.getSerializableExtra(MainActivity.NOTE_CATEGORY_EXTRA);
        }

        if (!newNote) {
            noteCatButton.setImageResource(Note.categoryToDrawable(savedButtonCategory));
        }

        buildCategoryDialog();
        buildConfirmDialog();

        noteCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialogObject.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialogObject.show();
            }
        });

        return fragmentLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putSerializable(MODIFIED_CATEGORY, savedButtonCategory);
    }


    private void buildCategoryDialog() {

        final String[] categories = new String[]{"Griffin's Group", "Liam's Group", "Mason's Group", "Nkima's Group"};

        AlertDialog.Builder categoryBuilder = new AlertDialog.Builder(getActivity());
        categoryBuilder.setTitle("Choose Note Type");

        categoryBuilder.setSingleChoiceItems(categories, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                // dismisses dialog window
                categoryDialogObject.cancel();

                switch (item) {
                    case 0:
                        savedButtonCategory = Note.Category.GRIFFIN;
                        noteCatButton.setImageResource(R.drawable.g);
                        break;
                    case 1:
                        savedButtonCategory = Note.Category.LIAM;
                        noteCatButton.setImageResource(R.drawable.l);
                        break;
                    case 2:
                        savedButtonCategory = Note.Category.MASON;
                        noteCatButton.setImageResource(R.drawable.m);
                        break;
                    case 3:
                        savedButtonCategory = Note.Category.NKIMA;
                        noteCatButton.setImageResource(R.drawable.n);
                        break;
                }

            }
        });
        categoryDialogObject = categoryBuilder.create();
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

                if (newNote) {
                    // if it's a new note create it in our database
                    dbAdapter.createNote(title.getText() + "", message.getText() + "",
                            (savedButtonCategory == null)? Note.Category.GRIFFIN : savedButtonCategory);
                } else {
                    // otherwise it's an old not so update it in our database
                    dbAdapter.updateNote(noteId, title.getText() + "", message.getText() + "",
                            savedButtonCategory);
                }

                dbAdapter.close();
                Intent intent = new Intent(getActivity(), MainActivity.class);
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
