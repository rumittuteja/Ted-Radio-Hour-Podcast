package tedradiohourpodcast.hw08.rumit.uncc.com.tedradiohourpodcast;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by Rumit on 3/10/17.
 */

public class FetchTedPodcast extends AsyncTask<Void,Void,ArrayList<PodcastItem>> {
    PodcastListReceived activity;

    public FetchTedPodcast(PodcastListReceived activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<PodcastItem> doInBackground(Void... params) {
        Request req = new Request("https://www.npr.org/rss/podcast.php","GET");
        req.addParams("id","510298");
        try {
            HttpURLConnection conn = (HttpURLConnection) req.getConnection();
            conn.connect();
            int statusCode = conn.getResponseCode();
            if(statusCode == HttpURLConnection.HTTP_OK){
                Log.d("Demo","This is cool.");
                InputStream in = conn.getInputStream();
                return PodcastListUtil.PodcastListPullParser.parsePodcastList(in);
            }else{
                Log.d("Demo","This is not cool.");
                // toast saying no connection
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(ArrayList<PodcastItem> podcastItems) {
        super.onPostExecute(podcastItems);
        activity.podcastListReceived(podcastItems);
    }

    static public interface PodcastListReceived{
        public void podcastListReceived(ArrayList<PodcastItem> arrList);
    }
}
