package com.juliawatzek.whatsob;


public class Note {
    private String title, message, observer, comments, preamble, newMsg;
    private long noteId;
    private Category category;
    private boolean wasFed, hadFoodInEnclosure;

    public enum Category {GRIFFIN, LIAM, MASON, NKIMA}

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
        return observer;
    }

    public String getComments() {
        return comments;
    }

    public boolean getWasFed() {
        return wasFed;
    }

    public boolean getHadFoodInEnclosure() {
        return hadFoodInEnclosure;
    }

    public void prepStrings() {
        preamble = "# Date and Time: " + title;
        preamble += "\n# Observer: " + observer;
        preamble += "\n# Comments: " + comments;
        preamble += "\n# Fed: " + (wasFed ? "Yes" : "No");
        preamble += "\n# Food in Enclosure: " + (hadFoodInEnclosure ? "Yes" : "No");
        preamble += "\n# Group: " + category.name();
        preamble += "\n# Data: \n#\n";

        newMsg = "Timestamp IndividualA Behavior IndividualB\n";
        newMsg += message.replace("\u00BB  ", "").replace(" \n", "\n");
    }

    public String toString() {
        prepStrings();
        return preamble + newMsg;
    }

    public String toCSVString() {
        prepStrings();
        return preamble + newMsg.replace(" ", ", ");
    }

    public int getAssociatedDrawable() {
        return categoryToDrawable(category);
    }

    public static int categoryToDrawable(Category noteCategory) {
        switch (noteCategory) {
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
