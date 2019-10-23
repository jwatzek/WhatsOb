package com.juliawatzek.whatsob;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


public class NoteViewFragment extends Fragment {


    public NoteViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentLayout = inflater.inflate(R.layout.fragment_note_view, container, false);

        TextView title = (TextView) fragmentLayout.findViewById(R.id.viewNoteTitle);
        TextView message = (TextView) fragmentLayout.findViewById(R.id.viewNoteMessage);
        ImageView icon = (ImageView) fragmentLayout.findViewById(R.id.viewNoteIcon);

        TextView observer = (TextView) fragmentLayout.findViewById(R.id.viewNoteObserver);
        TextView estrous = (TextView) fragmentLayout.findViewById(R.id.viewNoteEstrous);
        TextView comments = (TextView) fragmentLayout.findViewById(R.id.viewNoteComments);
        CheckBox wasFed = (CheckBox) fragmentLayout.findViewById(R.id.viewWasFed);
        CheckBox hadFoodInEnclosure = (CheckBox) fragmentLayout.findViewById(R.id.viewHadFoodInEnclosure);

        Intent intent = getActivity().getIntent();

        title.setText(intent.getExtras().getString(MainActivity.NOTE_TITLE_EXTRA));
        message.setText(intent.getExtras().getString(MainActivity.NOTE_MESSAGE_EXTRA));

        Note.Category noteCat = (Note.Category) intent.getSerializableExtra(MainActivity.NOTE_CATEGORY_EXTRA);
        icon.setImageResource(Note.categoryToDrawable(noteCat));

        observer.setText(intent.getExtras().getString(MainActivity.NOTE_OBSERVER_EXTRA));
        estrous.setText(intent.getExtras().getString(MainActivity.NOTE_ESTROUS_EXTRA));
        comments.setText(intent.getExtras().getString(MainActivity.NOTE_COMMENTS_EXTRA));
        wasFed.setChecked(intent.getExtras().getBoolean(MainActivity.NOTE_WAS_FED_EXTRA));
        hadFoodInEnclosure.setChecked(intent.getExtras().getBoolean(MainActivity.NOTE_HAD_FOOD_IN_ENCLOSURE_EXTRA));

        return fragmentLayout;
    }

}
