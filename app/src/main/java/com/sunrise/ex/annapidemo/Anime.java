package com.sunrise.ex.annapidemo;

/***********************************************************************************************\
 /* API provided by http://www.animenewsnetwork.com, please visit their website for full details */
/***********************************************************************************************/

//model class to store model data of anime from retrieved xml
public class Anime {

    private int mId;
    private String mTitle;
    private float mScore;
    private String mLink;

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public float getScore() {
        return mScore;
    }

    public void setScore(float score) {
        mScore = score;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
