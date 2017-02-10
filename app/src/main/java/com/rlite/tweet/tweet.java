package com.rlite.tweet;


/**
 * Created by le no vo on 04-02-2017.
 */

public class Tweet {

    private String DateCreated;

    private String Id;

    private String Text;

    public String getDateCreated() {
        return DateCreated;
    }

    public String getId() {
        return Id;
    }

    public String getText() {
        return Text;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public void setId(String id) {
        Id = id;
    }


    public void setText(String text) {
        Text = text;
    }


    @Override
    public String  toString(){
        return getText();
    }
}
