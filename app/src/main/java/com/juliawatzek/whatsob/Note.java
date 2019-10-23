package com.juliawatzek.whatsob;


public class Note {
    private String title, message, observer, estrous, comments, preamble, newMsg;
    private long noteId;
    private Category category;
    private boolean wasFed, hadFoodInEnclosure;

    public enum Category {GABE, GRIFFIN, LIAM, LOGAN, MASON, NKIMA}

    public Note(String title, String message, Category category, long noteId, String observer,
                String estrous, String comments, boolean wasFed, boolean hasFoodInEnclosure) {
        this.title = title;
        this.message = message;
        this.category = category;
        this.noteId = noteId;
        this.observer = observer;
        this.estrous = estrous;
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

    public String getEstrous() { return estrous; }

    public String getComments() {
        return comments;
    }

    public boolean getWasFed() {
        return wasFed;
    }

    public boolean getHadFoodInEnclosure() {
        return hadFoodInEnclosure;
    }

    public String toString() {
        preamble = "# Date and Time: " + title;
        preamble += "\n# Observer: " + observer;
        preamble += "\n# Estrous: " + estrous;
        preamble += "\n# Comments: " + comments;
        preamble += "\n# Fed: " + (wasFed ? "Yes" : "No");
        preamble += "\n# Food in Enclosure: " + (hadFoodInEnclosure ? "Yes" : "No");
        preamble += "\n# Group: " + category.name();
        preamble += "\n# Data: \n#\n";

        newMsg = "Timestamp IndividualA Behavior IndividualB\n";
        newMsg += message.replace("\u00BB  ", "").replace(" \n", "\n");

        return preamble + newMsg;
    }

    public int getAssociatedDrawable() {
        return categoryToDrawable(category);
    }

    public static int categoryToDrawable(Category noteCategory) {
        switch (noteCategory) {
            case GABE:
                return R.drawable.ng;
            case GRIFFIN:
                return R.drawable.g;
            case LIAM:
                return R.drawable.nl;
            case LOGAN:
                return R.drawable.l;
            case MASON:
                return R.drawable.m;
            case NKIMA:
                return R.drawable.n;
        }

        return R.drawable.g;
    }

}
