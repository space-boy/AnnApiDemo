package com.sunrise.ex.annapidemo;

import android.support.v4.app.Fragment;

/***********************************************************************************************\
 /* API provided by http://www.animenewsnetwork.com, please visit their website for full details */
/***********************************************************************************************/

public class AnimeDetailActivity extends SingleFragmentActivity {

    private static final String TAG = "AnimeDetailActivity";

    @Override
    protected Fragment createFragment() {
        int mAnimeId = getIntent().getExtras().getInt(ListFragmentAnimeDetail.ANIME_ID);
        float mAnimeScore = getIntent().getExtras().getFloat(ListFragmentAnimeDetail.ANIME_SCORE);
        String mAnimeTitle = getIntent().getExtras().getString(ListFragmentAnimeDetail.ANIME_TITLE);
        String mAnimeLink = getIntent().getExtras().getString(ListFragmentAnimeDetail.ANIME_HREF);

        return DetailFragmentAnime.newInstance(mAnimeTitle, mAnimeId, mAnimeScore,mAnimeLink);
    }
}
