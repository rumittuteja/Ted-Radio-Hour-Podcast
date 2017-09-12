package tedradiohourpodcast.hw08.rumit.uncc.com.tedradiohourpodcast;

import java.io.Serializable;

/**
 * Created by Rumit on 3/10/17.
 */

public class PodcastItem implements Serializable{

    String strTitle, strDate,strImgURL,strAudioPlaybackURL,strDescription;
    int duration;

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStrAudioPlaybackURL() {
        return strAudioPlaybackURL;
    }

    public void setStrAudioPlaybackURL(String strAudioPlaybackURL) {
        this.strAudioPlaybackURL = strAudioPlaybackURL;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        String[] words = strDate.split(":");
        String dateToTake = words[0].substring(0,words[0].length() - 3);
        this.strDate = dateToTake;
    }

    public String getStrImgURL() {
        return strImgURL;
    }

    public void setStrImgURL(String strImgURL) {
        this.strImgURL = strImgURL;
    }

    @Override
    public String toString() {
        return "PodcastItem{" +
                "strTitle='" + strTitle + '\'' +
                ", strDate='" + strDate + '\'' +
                ", strImgURL='" + strImgURL + '\'' +
                ", strAudioPlaybackURL='" + strAudioPlaybackURL + '\'' +
                ", strDescription='" + strDescription + '\'' +
                ", duration=" + duration +
                '}';
    }
}
