package com.sunrise.ex.annapidemo;

/***********************************************************************************************\
 /* API provided by http://www.animenewsnetwork.com, please visit their website for full details */
/***********************************************************************************************/
public class AnimeDetail {

    private String mImgUrl;
    private String mJapanTitle;
    private int mNoEps;
    private String mGenres;
    private String mThemes;
    private String mPlotSummary;

    public String getGenres() {
        return mGenres;
    }

    public void setGenres(String genres) {
        mGenres = genres;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        mImgUrl = imgUrl;
    }

    public String getJapanTitle() {
        return mJapanTitle;
    }

    public void setJapanTitle(String japanTitle) {
        mJapanTitle = japanTitle;
    }

    public int getNoEps() {
        return mNoEps;
    }

    public void setNoEps(int noEps) {
        mNoEps = noEps;
    }

    public String getPlotSummary() {
        return mPlotSummary;
    }

    public void setPlotSummary(String plotSummary) {
        mPlotSummary = plotSummary;
    }

    public String getThemes() {
        return mThemes;
    }

    public void setThemes(String themes) {
        mThemes = themes;
    }
}
