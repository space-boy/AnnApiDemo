package com.sunrise.ex.annapidemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/***********************************************************************************************\
 /* API provided by http://www.animenewsnetwork.com, please visit their website for full details */
/***********************************************************************************************/
public class AnnDataFetcher {

    private static final String TAG = "AnnDataFetcher";
    private static final Uri ENDPOINT = Uri.parse("http://www.animenewsnetwork.com/encyclopedia/").buildUpon().build();

    public byte[] getUrlBytes(String urlSpec) throws IOException {

        URL url = new URL(urlSpec);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + "with: " + urlSpec);
            }

            int bytesRead;
            byte[] buffer = new byte[1024];

            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();

            return out.toByteArray();

        } finally {
            connection.disconnect();
        }

    }

    public String buildUrl(String method, String query){

        String mEndUrl;
        Uri.Builder mUriBuilder;

        if(method.equals("anime") || method.equals("title")){

            mEndUrl = ENDPOINT + "api.xml";
            mUriBuilder = Uri.parse(mEndUrl).buildUpon().appendQueryParameter(method,query);

        } else {
            mEndUrl = ENDPOINT + "reports.xml";
            mUriBuilder = Uri.parse(mEndUrl).buildUpon().appendQueryParameter(method,query);
        }
        return mUriBuilder.build().toString();
    }

    public List<Anime> fetchAnime() throws IOException, XmlPullParserException {
        return parseAnimeXml(new String(getUrlBytes(buildUrl("id", "172"))));
    }

    public AnimeDetail fetchAnimeDetail(String urlSpec, String id) throws IOException, XmlPullParserException {
        return parseAnimeDetailXml(new String(getUrlBytes(buildUrl(urlSpec, id))));
    }

    public Bitmap downloadImage(String url) throws IOException {
        byte[] bitmapBytes = getUrlBytes(url);
        final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes,0,bitmapBytes.length);
        return bitmap;
    }

    public AnimeDetail parseAnimeDetailXml(String xmlString) throws XmlPullParserException, IOException{

        XmlPullParser xpp = Xml.newPullParser();
        xpp.setInput(new StringReader(xmlString));

        int eventType = xpp.next();
        AnimeDetail ad = new AnimeDetail();

        while(eventType != XmlPullParser.END_DOCUMENT){
            if(eventType == XmlPullParser.START_TAG){
                if("info".equals(xpp.getName())) {

                    String imgurl = xpp.getAttributeValue(null, "src");
                    if (imgurl != null)
                        ad.setImgUrl(imgurl);

                    String jaTit = xpp.getAttributeValue(null, "lang");
                    String altTit = xpp.getAttributeValue(null,"type");
                    if(jaTit != null && altTit != null) {
                        if (jaTit.equals("JA") && altTit.equals("Alternative title"))
                            ad.setJapanTitle(xpp.nextText());
                    }

                    String infoType = xpp.getAttributeValue(null, "type");
                    if (infoType != null) {

                       if(infoType.equals("Genres")){
                           String tempGenre;
                           tempGenre = ad.getGenres();
                           if(tempGenre == null) {
                               ad.setGenres(xpp.nextText());
                           }else {
                               ad.setGenres(ad.getGenres() + "," + xpp.nextText());
                           }

                        } else if(infoType.equals("Themes")){
                            String tempTheme;
                           tempTheme = ad.getThemes();
                            if(tempTheme == null) {
                                ad.setThemes(xpp.nextText());
                            }else{
                                ad.setThemes(ad.getThemes() + "," + xpp.nextText());
                            }

                        } else if(infoType.equals("Plot Summary")){
                            ad.setPlotSummary(xpp.nextText());
                        } else if(infoType.equals("Number of episodes")){
                            ad.setNoEps(Integer.valueOf(xpp.nextText()));
                        }

                    }
                }
            }
            eventType = xpp.next();
        }

        return ad;
    }

    public List<Anime> parseAnimeXml(String xmlString) throws XmlPullParserException, IOException {

        List<Anime> mAnimes = new ArrayList<>();
        XmlPullParser xpp = Xml.newPullParser();
        xpp.setInput(new StringReader(xmlString));
        int eventType = xpp.next();
        Anime mAnime = new Anime();

        while (eventType != XmlPullParser.END_DOCUMENT) {

            if(eventType == XmlPullParser.START_TAG){
                if("item".equals(xpp.getName()))
                  mAnime.setId(Integer.valueOf(xpp.getAttributeValue(null, "id")));
                else if("anime".equals(xpp.getName())) {
                    mAnime.setLink(xpp.getAttributeValue(null,"href"));
                    mAnime.setTitle(xpp.nextText());
                }
                else if("straight_average".equals(xpp.getName()))
                    mAnime.setScore(Float.valueOf(xpp.nextText()));
            } else if (eventType == XmlPullParser.END_TAG && "item".equals(xpp.getName())){
                mAnimes.add(mAnime);
                mAnime = new Anime();
            }
            eventType = xpp.next();
        }
        return mAnimes;
    }
}

