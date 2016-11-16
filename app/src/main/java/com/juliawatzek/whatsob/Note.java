package com.juliawatzek.whatsob;


public class Note {
    private String title, message, observer, comments;
    private long noteId;
    private Category category;
    private boolean wasFed, hadFoodInEnclosure;

    public enum Category {GRIFFIN, LIAM, MASON, NKIMA}

    public Note(String title, String message, Category category, long noteId) {
        this.title = title;
        this.message = message;
        this.category = category;
        this.noteId = noteId;
        this.observer = "";
        this.comments = "";
        this.wasFed = false;
        this.hadFoodInEnclosure = false;
    }

    public Note(String title, String message, Category category, long noteId, String observer,
                String comments, boolean wasFed, boolean hasFoodInEnclosure) {
        this.title = title;
        this.message = message;
        this.category = category;
        this.noteId = noteId;
        this.observer = observer;
        this.comments = comments;
        this.wasFed = wasFed;
        this.hadFoodInEnclosure = hasFoodInEnclosure;
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

    public String getObserver() {
        return "Observer: " + observer;
    }

    public String getComments() {
        return comments;
    }

    public boolean getWasFed() {
        return wasFed;
    }

    public boolean getHasFoodInEnclosure() {
        return wasFed;
    }

    public String toString() {
        String s = "";

        s += "# Date and Time: " + title;
        s += "\n# Observer: " + observer;
        s += "\n# Comments: " + comments;
        s += "\n# Fed: " + (wasFed? "Yes" : "No");
        s += "\n# Food in Enclosure: " + (hadFoodInEnclosure? "Yes" : "No");
        s += "\n# Group: " + category.name();
        s += "\n# Data: \n";
        s += message;

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
