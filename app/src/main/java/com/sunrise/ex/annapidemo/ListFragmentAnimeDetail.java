package com.sunrise.ex.annapidemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/***********************************************************************************************\
 /* API provided by http://www.animenewsnetwork.com, please visit their website for full details */
/***********************************************************************************************/

public class ListFragmentAnimeDetail extends Fragment {

    private static final String TAG ="ListFragmnetAnimeDetail";
    private RecyclerView mRecyclerView;
    private List<Anime> mAnimeList = new ArrayList<>();

    public static final String ANIME_TITLE = "ANIME TITLE";
    public static final String ANIME_SCORE = "ANIME_SCORE";
    public static final String ANIME_ID = "ANIME_ID";
    public static final String ANIME_HREF = "ANIME_LINK";

    public static ListFragmentAnimeDetail newInstance(){
        return new ListFragmentAnimeDetail();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchAnimeData().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_view,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.anime_list_fragment_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setUpAdapter();
        return view;
    }

    private void setUpAdapter(){
        if(isAdded()){
            mRecyclerView.setAdapter(new AnimeItemAdapter(mAnimeList));
        }
    }


    private class AnimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Anime mAnime;
        private TextView mTitleTextView;

        public AnimeViewHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView.findViewById(android.R.id.text1);
            mTitleTextView.setOnClickListener(this);
        }

        public void bindAnimeItem(Anime anime){
            mAnime = anime;
            mTitleTextView.setText(anime.getTitle());
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(getActivity(),AnimeDetailActivity.class);
            i.putExtra(ANIME_ID, mAnime.getId());
            i.putExtra(ANIME_SCORE,mAnime.getScore());
            i.putExtra(ANIME_TITLE,mAnime.getTitle());
            i.putExtra(ANIME_HREF,mAnime.getLink());

            startActivity(i);
        }
    }

    private class AnimeItemAdapter extends RecyclerView.Adapter<AnimeViewHolder>{

        private List<Anime> mAnimeList;

        public AnimeItemAdapter(List<Anime> animeList){
            mAnimeList = animeList;
        }

        @Override
        public AnimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = getActivity().getLayoutInflater().inflate(android.R.layout.simple_list_item_2,parent,false);

            return new AnimeViewHolder(v);

        }

        @Override
        public void onBindViewHolder(AnimeViewHolder holder, int position) {
            Anime anime = mAnimeList.get(position);
            holder.bindAnimeItem(anime);
        }

        @Override
        public int getItemCount() {
            return mAnimeList.size();
        }

    }

    private class FetchAnimeData extends AsyncTask<Void,Void,List<Anime>>{

        @Override
        protected List<Anime> doInBackground(Void... params) {
            try {
               try {
                   return new AnnDataFetcher().fetchAnime();
                } catch (XmlPullParserException e){
                    e.printStackTrace();
                }
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Anime> animeList) {
            mAnimeList = animeList;
            setUpAdapter();
        }
    }
}

