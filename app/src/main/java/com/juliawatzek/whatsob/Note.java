package com.juliawatzek.whatsob;


public class Note {
    private String title, message;
    private long noteId, dateCreatedMilli;
    private Category category;

    public enum Category {GRIFFIN, LIAM, MASON, NKIMA}

    public Note(String title, String message, Category category) {
        this.title = title;
        this.message = message;
        this.category = category;
        this.noteId = 0;
        this.dateCreatedMilli = 0;
    }

    public Note(String title, String message, Category category, long noteId, long dateCreatedMilli) {
        this.title = title;
        this.message = message;
        this.category = category;
        this.noteId = noteId;
        this.dateCreatedMilli = dateCreatedMilli;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Category getCategory() {
        return category;
    }

    public long getNoteId() {
        return noteId;
    }

    public long getDateCreatedMilli() {
        return dateCreatedMilli;
    }


    public String toString() {
        String s = "";

        s += "Note ID: " + noteId;
        s += "\nTitle: " + title;
        s += "\nMessage: " + message;
        s += "\nCategory: " + category.name();
        s += "\nDate: " + dateCreatedMilli;

        return s;
    }

    public int getAssociatedDrawable(){
        return categoryToDrawable(category);
    }

    public static int categoryToDrawable(Category noteCategory) {
        switch (noteCategory){
            case GRIFFIN:
                return R.drawable.g;
            case LIAM:
                return R.drawable.l;
            case MASON:
                return R.drawable.m;
            case NKIMA:
                return R.drawable.n;
        }

        return R.drawable.g;
    }

}
