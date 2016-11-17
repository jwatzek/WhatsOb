package com.juliawatzek.whatsob;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteAdapter extends ArrayAdapter<Note> {

    public static class ViewHolder {
        TextView noteTitle;
        TextView noteText;
        ImageView noteIcon;
    }

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        super(context, 0, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Note note = getItem(position);

        // Create a new view holder
        ViewHolder viewHolder;

        // Check if an existing view is being reused, otherwise inflate a new view from custom row layout
        if (convertView == null) {

            // if we don'n have a view that is being used, create one and make sure you create a
            // view holder along with it to save our view references to
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);

            // set our views to our view holder so that we no longer have to go back and use find view
            // by id every time we have a new row -- jw: save references to later populate them with
            // specific note row data
            viewHolder.noteTitle = (TextView) convertView.findViewById(R.id.listItemNoteDate);
            viewHolder.noteText = (TextView) convertView.findViewById(R.id.listItemNoteObserver);
            viewHolder.noteIcon = (ImageView) convertView.findViewById(R.id.listItemNoteImg);

            // use set tag to remember our view holder, which is holding the references to our widgets
            convertView.setTag(viewHolder);
        } else {
            // we already have a view so just go to our view holder and grab the widgets from it
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Fill each new referenced view with data associated with the note it's referencing
        viewHolder.noteTitle.setText(note.getTitle());
        viewHolder.noteText.setText("Observer: " + note.getObserver());
        viewHolder.noteIcon.setImageResource(note.getAssociatedDrawable());

        // now that we modified the view to display appropriate data,
        // return it so it will be displayed
        return convertView;
    }

}
