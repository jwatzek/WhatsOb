package com.juliawatzek.whatsob;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NoteCreateFragment extends Fragment {

    private TextView title, message;
    private ImageButton noteCatButton;
    private LinearLayout quickTextButtons;
    private ScrollView scroll;
    private Note.Category savedButtonCategory;
    private AlertDialog categoryDialogObject;
    private Note note;
    private Chronometer timer;
    private boolean timestampNext = true;

    private static final String MODIFIED_CATEGORY = "Modified Category";

    public NoteCreateFragment() {
        // Required empty public constructor
    }


    private String milliToMinSec (long timeInMillies) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(timeInMillies),
                TimeUnit.MILLISECONDS.toSeconds(timeInMillies) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMillies))
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            savedButtonCategory = (Note.Category) savedInstanceState.getSerializable(MODIFIED_CATEGORY);
        }

        // Inflate the layout for this fragment
        View fragmentLayout = inflater.inflate(R.layout.fragment_note_create, container, false);

        // grab widget references from layout
        title = (TextView) fragmentLayout.findViewById(R.id.createNoteTitle);
        message = (TextView) fragmentLayout.findViewById(R.id.createNoteMessage);
        noteCatButton = (ImageButton) fragmentLayout.findViewById(R.id.createNoteButton);
        quickTextButtons = (LinearLayout) fragmentLayout.findViewById(R.id.quickTextButtons);
        Button enterButton = (Button) fragmentLayout.findViewById(R.id.createNote);
        Button startButton = (Button) fragmentLayout.findViewById(R.id.startButton);
        timer = (Chronometer) fragmentLayout.findViewById(R.id.timer);

        // populate widgets with note data
        Intent intent = getActivity().getIntent();

        // Set 'title' to current date and time
        String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
        title.setText(currentDateTime);
        message.setText("");

        // Get scrollview
        scroll = (ScrollView) fragmentLayout.findViewById(R.id.createScrollView);

        // if we came from our list fragment, get category from intent
        // otherwise (i.e., if we changed screen orientation), skip this and just set the image to
        // the category info we retrieved from the bundle
        if (savedButtonCategory == null) {
            savedButtonCategory = (Note.Category) intent.getSerializableExtra(MainActivity.NOTE_CATEGORY_EXTRA);
        }

        buildCategoryDialog();
        noteCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialogObject.show();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start timer
                timer.setBase(SystemClock.elapsedRealtime());
                timer.start();

                /*
                // set 3min alarms
                // TODO: add sounds, make snack bar
                // TODO: figure out how to stop timer when navigated out of activity
                new CountDownTimer(1800000 + 500, 180000) {

                    public void onTick(long millisUntilFinished) {
                            Toast.makeText(getActivity(), "SCAN TIME!", Toast.LENGTH_LONG).show();
                    }

                    public void onFinish() {
                        Toast.makeText(getActivity(), "LAST SCAN!", Toast.LENGTH_LONG).show();
                        this.cancel();
                    }
                }.start();
                */

                // TODO: On leaving screen, bring up confirm dialog. If really leaving, quit timer.

                // first prompt
                message.append("\u00BB  ");

                // show quick-text buttons
                quickTextButtons.setVisibility(View.VISIBLE);
            }
        });

        // attach onClickListeners to all ID buttons 
        LinearLayout individuals = (LinearLayout) fragmentLayout.findViewById(R.id.layoutIds);
        int idCount = individuals.getChildCount();

        for (int i = 0; i < idCount; i++) {
            View view = individuals.getChildAt(i);

            if (view instanceof Button) {
                final Button id = (Button) view;
                id.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (timestampNext) {
                            long elapsedMillis = SystemClock.elapsedRealtime() - timer.getBase();

                            message.append(milliToMinSec(elapsedMillis) + " ");
                            timestampNext = false;
                        }
                        message.append(id.getText() + " ");
                    }
                });
            }
        }

        // attach onClickListeners to all behavior buttons 
        GridLayout grid = (GridLayout) fragmentLayout.findViewById(R.id.layoutBehavs);
        int behavCount = grid.getChildCount();

        for (int i = 0; i < behavCount; i++) {
            View view = grid.getChildAt(i);

            if (view instanceof Button) {
                final Button behav = (Button) view;
                behav.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {

                        if (timestampNext) {
                            long elapsedMillis = SystemClock.elapsedRealtime() - timer.getBase();

                            message.append(milliToMinSec(elapsedMillis) + " ");
                            timestampNext = false;
                        }
                        message.append(behav.getText() + " ");
                    }
                });
            }
        }
        
        // this needs to be after grid onClickListeners in order to override them
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterDataRow();
            }
        });

        return fragmentLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putSerializable(MODIFIED_CATEGORY, savedButtonCategory);
        // TODO: Save visibility of quick-text buttons + message etc.! Everything that gets overwritten or reset on onCreate.
    }


    private void buildCategoryDialog() {

        final String[] categories = new String[]{"Griffin's Group", "Liam's Group", "Mason's Group", "Nkima's Group"};

        AlertDialog.Builder categoryBuilder = new AlertDialog.Builder(getActivity());
        categoryBuilder.setTitle("Choose Group");

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

    private void enterDataRow() {

        // add linebreak
        message.append("\n\u00BB  ");

        // add timestamp next time a button is pressed?
        timestampNext = true;

        // scroll to bottom?
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });

        NotebookDbAdapter dbAdapter = new NotebookDbAdapter(getActivity().getBaseContext());
        dbAdapter.open();

        if (note != null) {
            // if there is a note, update it in database
            dbAdapter.updateNote(note.getNoteId(), title.getText() + "", message.getText() + "", note.getCategory());
        } else {
            // else, save as new note in our database
            note = dbAdapter.createNote(title.getText() + "", message.getText() + "",
                    (savedButtonCategory == null) ? Note.Category.GRIFFIN : savedButtonCategory);
        }
        dbAdapter.close();

    }

}
