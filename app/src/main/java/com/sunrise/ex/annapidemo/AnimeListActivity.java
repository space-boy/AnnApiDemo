package com.sunrise.ex.annapidemo;

import android.support.v4.app.Fragment;

/***********************************************************************************************\
 /* API provided by http://www.animenewsnetwork.com, please visit their website for full details */
/***********************************************************************************************/
public class AnimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return ListFragmentAnimeDetail.newInstance();
    }

}
