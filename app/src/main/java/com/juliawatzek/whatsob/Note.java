package com.juliawatzek.whatsob;


public class Note {
    private String title, message;
    private long noteId;
    private Category category;
    private boolean wasFed, hadFoodInEnclosure;

    public enum Category {GRIFFIN, LIAM, MASON, NKIMA}

    public Note(String title, String message, Category category, long noteId) {
        this.title = title;
        this.message = message;
        this.category = category;
        this.noteId = noteId;
        this.wasFed = false;
        this.hadFoodInEnclosure = false;
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

    public String toString() {
        String s = "";

        s += "# Date and Time: " + title;
        s += "\n# Observer: ";
        s += "\n# Comments: ";
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
