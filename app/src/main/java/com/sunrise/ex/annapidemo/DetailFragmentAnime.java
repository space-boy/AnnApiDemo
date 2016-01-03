package com.sunrise.ex.annapidemo;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
/***********************************************************************************************\
/* API provided by http://www.animenewsnetwork.com, please visit their website for full details */
/***********************************************************************************************/

public class DetailFragmentAnime extends Fragment {

    private static final String TAG = "DetailFragmentAnime";
    private TextView mTitle;
    private TextView mPlotSummary;
    private TextView mScore;
    private TextView mAltTitle;
    private TextView mGenres;
    private TextView mThemes;
    private TextView mNumEps;
    private TextView mAnimeRefText;
    private ImageView mMainImg;
    private Toolbar mToolbar;
    private int mAnimeId;
    private float mAnimeScore;
    private String mAnimeTitle;
    private String mAnimeHref;

    public static DetailFragmentAnime newInstance(String title, int id,float score,String link){

        Bundle args = new Bundle();
        args.putString(ListFragmentAnimeDetail.ANIME_TITLE, title);
        args.putFloat(ListFragmentAnimeDetail.ANIME_SCORE, score);
        args.putInt(ListFragmentAnimeDetail.ANIME_ID, id);
        args.putString(ListFragmentAnimeDetail.ANIME_HREF,link);

        DetailFragmentAnime dfa = new DetailFragmentAnime();
        dfa.setArguments(args);
        return dfa;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAnimeTitle = getArguments().getString(ListFragmentAnimeDetail.ANIME_TITLE);
        mAnimeScore = getArguments().getFloat(ListFragmentAnimeDetail.ANIME_SCORE);
        mAnimeId = getArguments().getInt(ListFragmentAnimeDetail.ANIME_ID);
        mAnimeHref = getArguments().getString(ListFragmentAnimeDetail.ANIME_HREF);

        new FetchDetails().execute();
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_anime_detail_view,container,false);

        mTitle = (TextView) v.findViewById(R.id.title_textview);
        mToolbar = (Toolbar) v.findViewById(R.id.toolbar);
        mScore = (TextView) v.findViewById(R.id.score_textview);
        mPlotSummary = (TextView) v.findViewById(R.id.series_info_textview);
        mAltTitle = (TextView) v.findViewById(R.id.alt_title);
        mMainImg = (ImageView) v.findViewById(R.id.anime_main_imageview);
        mAnimeRefText = (TextView) v.findViewById(R.id.anime_link_textview);
        mGenres = (TextView) v.findViewById(R.id.genres_textview);
        mThemes = (TextView) v.findViewById(R.id.themes_textview);
        mNumEps = (TextView) v.findViewById(R.id.episodes_textview);

        mToolbar.setTitle(mAnimeTitle);
        mAnimeRefText.setClickable(true);
        mAnimeRefText.setText(mAnimeHref);
        mAnimeRefText.setMovementMethod(LinkMovementMethod.getInstance());

        String viewableLink = "http://www.animenewsnetwork.com" + mAnimeHref;
        String link = "<a href='http://www.animenewsnetwork.com" + mAnimeHref + "'>" + viewableLink + "</a>";

        mAnimeRefText.setText(Html.fromHtml(link));
        String temp = String.format("%.2f", mAnimeScore);
        mScore.setText(String.format(getResources().getString(R.string.score),temp));
        mTitle.setText(mAnimeTitle);

        return v;
    }

    private class FetchDetails extends AsyncTask<Void, Void, AnimeDetail> {

        private Bitmap tempBitmap;

        @Override
        protected AnimeDetail doInBackground(Void... params) {
            try {
                AnimeDetail ad = new AnnDataFetcher().fetchAnimeDetail("anime",String.valueOf(mAnimeId));
                tempBitmap = new AnnDataFetcher().downloadImage(ad.getImgUrl());
                return ad;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(AnimeDetail animeDetail) {

            mAltTitle.setText(animeDetail.getJapanTitle());
            mPlotSummary.setText(animeDetail.getPlotSummary());
            mMainImg.setImageBitmap(tempBitmap);
            mGenres.setText(String.format(getResources().getString(R.string.genres), animeDetail.getGenres()));
            mThemes.setText(String.format(getResources().getString(R.string.themes), animeDetail.getThemes()));
            mNumEps.setText(String.format(getResources().getString(R.string.episodes), String.valueOf(animeDetail.getNoEps())));
            mScore.setVisibility(View.VISIBLE);
            mNumEps.setVisibility(View.VISIBLE);
            mGenres.setVisibility(View.VISIBLE);
            mThemes.setVisibility(View.VISIBLE);
        }
    }
}
