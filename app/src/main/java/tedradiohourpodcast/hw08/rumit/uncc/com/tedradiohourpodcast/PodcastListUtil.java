package tedradiohourpodcast.hw08.rumit.uncc.com.tedradiohourpodcast;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Rumit on 3/10/17.
 */

public class PodcastListUtil  {

    static public class PodcastListPullParser{
        static public ArrayList<PodcastItem> parsePodcastList(InputStream in) throws XmlPullParserException, IOException {
            Log.d("IN","In the parser.");
            XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
            xmlPullParser.setInput(in,"UTF-8");
            PodcastItem item = new PodcastItem();
            ArrayList<PodcastItem> arrList = new ArrayList<PodcastItem>();
            int event = xmlPullParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT){
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("item")) {
                            item = new PodcastItem();
                        } else if (xmlPullParser.getName().equals("title")) {
                            item.setStrTitle(xmlPullParser.nextText().trim());
                        } else if (xmlPullParser.getName().equals("pubDate")) {
                            item.setStrDate(xmlPullParser.nextText().trim());
                        } else if (xmlPullParser.getName().equals("itunes:image")) {
                            item.setStrImgURL(xmlPullParser.getAttributeValue(null, "href"));
//                            item.setStrImgURL(xmlPullParser.nextText().trim());
                        } else if (xmlPullParser.getName().equals("itunes:duration")) {
                            item.setDuration(Integer.parseInt(xmlPullParser.nextText().trim()));
//                            item.setStrImgURL(xmlPullParser.nextText().trim());
                        }else if (xmlPullParser.getName().equals("description")) {
                            item.setStrDescription(xmlPullParser.nextText().trim());
//                            item.setStrImgURL(xmlPullParser.nextText().trim());
                        } else if (xmlPullParser.getName().equals("enclosure")) {
                            item.setStrAudioPlaybackURL(xmlPullParser.getAttributeValue(null, "url"));
//                            item.setStrImgURL(xmlPullParser.nextText().trim());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xmlPullParser.getName().equals("item")) {
                            arrList.add(item);
                        }
                        break;
                }
                event = xmlPullParser.next();
            }
            Log.d("ListItemsCount",arrList.toString());
            return arrList;
        }
    }
}
