package com.juliawatzek.whatsob;


public class Note {
    private String title, message;
    private long noteId, dateCreatedMilli;
    private Category category;

    public enum Category {PERSONAL, TECHNICAL, QUOTE, FINANCE}

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

        s += "ID: " + noteId;
        s += " Title: " + title;
        s += " Message: " + message;
        s += " IconID: " + category.name();
        s += " Note ID: " + noteId;
        s += " Date: " + dateCreatedMilli;

        return s;
    }

    public int getAssociatedDrawable(){
        return categoryToDrawable(category);
    }

    public static int categoryToDrawable(Category noteCategory) {
        switch (noteCategory){
            case PERSONAL:
                return R.drawable.p;
            case TECHNICAL:
                return R.drawable.t;
            case QUOTE:
                return R.drawable.q;
            case FINANCE:
                return R.drawable.f;
        }

        return R.drawable.f;
    }

}
