package com.juliawatzek.whatsob;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class NoteCreateFragment extends Fragment {

    private TextView title, message, observer, estrous, comments;
    private CheckBox wasFed, hadFoodInEnclosure;
    private Button startButton;
    private LinearLayout quickTextButtons;
    private ScrollView scroll;
    private Note.Category noteCategory;
    private AlertDialog confirmDialogObject, shareDialogObject, feedDialogObject, aggressDialogObject, adlibDialogObject, noteDialogObject;
    private Note note;
    private Chronometer timer;
    private boolean timestampNext = true;
    private boolean isStart = true;
    private String share, feed, aggress, adlib;
    private InputMethodManager imm;
    private View fragmentLayout;

    public NoteCreateFragment() {
        // Required empty public constructor
    }


    private String milliToMinSec(long timeInMillies) {
        return String.format(Locale.getDefault(),"%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(timeInMillies),
                TimeUnit.MILLISECONDS.toSeconds(timeInMillies) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMillies))
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // get note category
        Intent intent = getActivity().getIntent();
        noteCategory = (Note.Category) intent.getSerializableExtra(MainActivity.NOTE_CATEGORY_EXTRA);

        // Depending on category, inflate the layout for this fragment
        switch (noteCategory) {
            case GABE:
                fragmentLayout = inflater.inflate(R.layout.fragment_note_create_gabe, container, false);
                break;
            case GRIFFIN:
                fragmentLayout = inflater.inflate(R.layout.fragment_note_create_griffin, container, false);
                break;
            case LIAM:
                fragmentLayout = inflater.inflate(R.layout.fragment_note_create_liam, container, false);
                break;
            case LOGAN:
                fragmentLayout = inflater.inflate(R.layout.fragment_note_create_logan, container, false);
                break;
            case MASON:
                fragmentLayout = inflater.inflate(R.layout.fragment_note_create_mason, container, false);
                break;
            case NKIMA:
                fragmentLayout = inflater.inflate(R.layout.fragment_note_create_nkima, container, false);
                break;

        }

        // grab widget references from layout
        title = fragmentLayout.findViewById(R.id.createNoteTitle);
        message = fragmentLayout.findViewById(R.id.createNoteMessage);
        ImageView icon = fragmentLayout.findViewById(R.id.createNoteIcon);
        observer = fragmentLayout.findViewById(R.id.createNoteObserver);
        estrous = fragmentLayout.findViewById(R.id.createNoteEstrous);
        comments = fragmentLayout.findViewById(R.id.createNoteComments);
        wasFed = fragmentLayout.findViewById(R.id.createWasFed);
        hadFoodInEnclosure = fragmentLayout.findViewById(R.id.createHadFoodInEnclosure);

        quickTextButtons = fragmentLayout.findViewById(R.id.quickTextButtons);
        Button enterButton = fragmentLayout.findViewById(R.id.createNote);
        Button shareButton = fragmentLayout.findViewById(R.id.shareButton);
        Button feedButton = fragmentLayout.findViewById(R.id.feedButton);
        Button aggressButton = fragmentLayout.findViewById(R.id.aggressButton);
        Button adlibButton = fragmentLayout.findViewById(R.id.adlibButton);
        Button noteButton = fragmentLayout.findViewById(R.id.otherButton);
        startButton = fragmentLayout.findViewById(R.id.startButton);
        timer = fragmentLayout.findViewById(R.id.timer);

        // get input manager for soft keyboard
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        // Set 'title' to current date and time
        String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
        title.setText(currentDateTime);
        message.setText("");
        icon.setImageResource(Note.categoryToDrawable(noteCategory));

        // Get scrollview
        scroll = fragmentLayout.findViewById(R.id.createScrollView);

        buildConfirmDialog();
        buildShareDialog();
        buildFeedDialog();
        buildAggressDialog();
        buildAdlibDialog();
        buildNoteDialog();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isStart) {
                    // hide soft keyboard
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    // start timer
                    timer.setBase(SystemClock.elapsedRealtime());
                    timer.start();

                    // change text on Start button to Stop.
                    startButton.setText(R.string.stop_button);
                    isStart = false;

                    // sound
                    final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.correct_a_tone);

                    // initial start
                    Toast.makeText(getActivity(), "SCAN TIME!", Toast.LENGTH_LONG).show();
                    mp.start();

                    // set 3min alarms
                    timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

                        @Override
                        public void onChronometerTick(Chronometer chronometer) {
                            long elapsedMillis = SystemClock.elapsedRealtime() - timer.getBase();

                            if ((elapsedMillis / 1000) % 180 == 0) {
                                Toast.makeText(getActivity(), "SCAN TIME!", Toast.LENGTH_LONG).show();
                                mp.start();
                            }
                        }

                    });

                    // first prompt
                    message.append("\u00BB  ");

                    // show quick-text buttons
                    quickTextButtons.setVisibility(View.VISIBLE);

                } else {
                    confirmDialogObject.show();
                }


            }
        });

        // attach onClickListeners to all ID buttons 
        GridLayout individuals = fragmentLayout.findViewById(R.id.layoutIds);
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
        GridLayout grid = fragmentLayout.findViewById(R.id.layoutBehavs);
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

        final MediaPlayer emp = MediaPlayer.create(getActivity(), R.raw.enter_blip);

        // this needs to be after grid onClickListeners in order to override them
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterDataRow();
                if (emp.isPlaying()) {
                    emp.seekTo(0);
                } else {
                    emp.start();
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDialogObject.show();
            }
        });

        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedDialogObject.show();
            }
        });

        aggressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aggressDialogObject.show();
            }
        });

        adlibButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adlibDialogObject.show();
            }
        });

        noteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteDialogObject.show();
            }
        });


        return fragmentLayout;
    }


    private void buildShareDialog() {

        final String[] sharing = new String[]{"Active-Share", "Passive-Share", "Cofeed", "Beg"};

        AlertDialog.Builder shareBuilder = new AlertDialog.Builder(getActivity());
        shareBuilder.setTitle("Choose Behavior");

        shareBuilder.setSingleChoiceItems(sharing, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                // dismisses dialog window
                shareDialogObject.cancel();

                switch (item) {
                    case 0:
                        share = "Active-Share";
                        break;
                    case 1:
                        share = "Passive-Share";
                        break;
                    case 2:
                        share = "Cofeed";
                        break;
                    case 3:
                        share = "Beg";
                        break;
                }

                if (timestampNext) {
                    long elapsedMillis = SystemClock.elapsedRealtime() - timer.getBase();

                    message.append(milliToMinSec(elapsedMillis) + " ");
                    timestampNext = false;
                }

                message.append(share + " ");

            }
        });
        shareDialogObject = shareBuilder.create();
    }


    private void buildFeedDialog() {

        final String[] feeding = new String[]{"Solo-Feed", "Proximity-Feed", "Contact-Feed", "Forage"};

        AlertDialog.Builder feedBuilder = new AlertDialog.Builder(getActivity());
        feedBuilder.setTitle("Choose Behavior");

        feedBuilder.setSingleChoiceItems(feeding, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                // dismisses dialog window
                feedDialogObject.cancel();

                switch (item) {
                    case 0:
                        feed = "Solo-Feed";
                        break;
                    case 1:
                        feed = "Proximity-Feed";
                        break;
                    case 2:
                        feed = "Contact-Feed";
                        break;
                    case 3:
                        feed = "Forage";
                        break;
                }

                if (timestampNext) {
                    long elapsedMillis = SystemClock.elapsedRealtime() - timer.getBase();

                    message.append(milliToMinSec(elapsedMillis) + " ");
                    timestampNext = false;
                }

                message.append(feed + " ");

            }
        });
        feedDialogObject = feedBuilder.create();
    }


    private void buildAggressDialog() {

        final String[] aggressing = new String[]{"Aggression", "Supplant"};

        AlertDialog.Builder aggressBuilder = new AlertDialog.Builder(getActivity());
        aggressBuilder.setTitle("Choose Behavior");

        aggressBuilder.setSingleChoiceItems(aggressing, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                // dismisses dialog window
                aggressDialogObject.cancel();

                switch (item) {
                    case 0:
                        aggress = "Aggress";
                        break;
                    case 1:
                        aggress = "Supplant";
                        break;
                }

                if (timestampNext) {
                    long elapsedMillis = SystemClock.elapsedRealtime() - timer.getBase();

                    message.append(milliToMinSec(elapsedMillis) + " ");
                    timestampNext = false;
                }

                message.append(aggress + " ");

            }
        });
        aggressDialogObject = aggressBuilder.create();
    }


    private void buildAdlibDialog() {

        final String[] adlibing = new String[]{"Non-contact Aggression", "Contact Aggression",
                "Intergroup Aggression", "Submissive", "Solicit", "Supplant", "Intervene",
                "Post-conflict Affiliation", "Sexual", "Intergroup Sexual", "Beg", "Food Share"};

        AlertDialog.Builder adlibBuilder = new AlertDialog.Builder(getActivity());
        adlibBuilder.setTitle("Choose Behavior");

        adlibBuilder.setSingleChoiceItems(adlibing, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                // dismisses dialog window
                adlibDialogObject.cancel();

                switch (item) {
                    case 0:
                        adlib = "NC-Aggress*";
                        break;
                    case 1:
                        adlib = "C-Aggress*";
                        break;
                    case 2:
                        adlib = "Aggress* Inter-G";
                        break;
                    case 3:
                        adlib = "Submissive*";
                        break;
                    case 4:
                        adlib = "Solicit*";
                        break;
                    case 5:
                        adlib = "Supplant*";
                        break;
                    case 6:
                        adlib = "Intervene*";
                        break;
                    case 7:
                        adlib = "PC-Affil*";
                        break;
                    case 8:
                        adlib = "Sexual*";
                        break;
                    case 9:
                        adlib = "Sexual* Inter-G";
                        break;
                    case 10:
                        adlib = "Beg*";
                        break;
                    case 11:
                        adlib = "Food-Share*";
                        break;
                }

                if (timestampNext) {
                    long elapsedMillis = SystemClock.elapsedRealtime() - timer.getBase();

                    message.append(milliToMinSec(elapsedMillis) + " ");
                    timestampNext = false;
                }

                message.append(adlib + " ");

            }
        });
        adlibDialogObject = adlibBuilder.create();
    }


    private void buildNoteDialog() {

        AlertDialog.Builder noteBuilder = new AlertDialog.Builder(getActivity());
        noteBuilder.setTitle("Write a Comment");

        final EditText input = new EditText(getActivity());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        input.setLayoutParams(lp);
        noteBuilder.setView(input);

        noteBuilder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                message.append(("# " + input.getText()).replace(" ", "\u00A0"));
                enterDataRow();

                input.setText("");

                // hide soft keyboard
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            }
        });

        noteBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                input.setText("");

                // hide soft keyboard
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            }
        });
        noteDialogObject = noteBuilder.create();

    }

    private void buildConfirmDialog() {

        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getActivity());
        confirmBuilder.setTitle("Are you sure?");
        confirmBuilder.setMessage("Are you sure you want to stop data collection?");

        confirmBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NotebookDbAdapter dbAdapter = new NotebookDbAdapter(getActivity().getBaseContext());
                dbAdapter.open();

                if (note != null) {
                    // if there is a note, update it in database
                    dbAdapter.updateNote(note.getNoteId(), note.getTitle() + "", message.getText() + "",
                            note.getCategory(), observer.getText() + "", estrous.getText() + "", comments.getText() + "",
                            wasFed.isChecked(), hadFoodInEnclosure.isChecked());
                } else {
                    // else, save as new note in our database
                    note = dbAdapter.createNote(title.getText() + "", message.getText() + "",
                            noteCategory, observer.getText() + "", estrous.getText() + "", comments.getText() + "",
                            wasFed.isChecked(), hadFoodInEnclosure.isChecked());
                }

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
            dbAdapter.updateNote(note.getNoteId(), note.getTitle() + "", message.getText() + "",
                    note.getCategory(), observer.getText() + "", estrous.getText() + "", comments.getText() + "",
                    wasFed.isChecked(), hadFoodInEnclosure.isChecked());
        } else {
            // else, save as new note in database
            note = dbAdapter.createNote(title.getText() + "", message.getText() + "",
                    noteCategory, observer.getText() + "", estrous.getText() + "", comments.getText() + "",
                    wasFed.isChecked(), hadFoodInEnclosure.isChecked());
        }
        dbAdapter.close();

    }

}
