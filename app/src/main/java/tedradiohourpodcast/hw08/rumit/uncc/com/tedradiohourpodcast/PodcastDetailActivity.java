package tedradiohourpodcast.hw08.rumit.uncc.com.tedradiohourpodcast;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;

public class PodcastDetailActivity extends AppCompatActivity {
    PodcastItem item;
    ImageView imgView;
    TextView txtViewTitle;
    TextView txtViewPublicationDate;
    TextView txtViewDesc;
    TextView txtViewDuration;
    ImageButton btnPlayPause;
    ProgressBar progressBar;
    MediaPlayer mediaPlayer;
    boolean isRunning;
    ReportTime reportTime;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbarSecond);
        setSupportActionBar(toolbar);
        initToolBar();
        item = (PodcastItem) getIntent().getExtras().getSerializable("PodcastItem");
        instantiateUIElements();
        populateUI();
    }

    public void initToolBar() {
        toolbar.setTitle("Play!");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ted);
        toolbar.inflateMenu(R.menu.second_menu);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.second_menu, menu);
        return true;
    }

    private void instantiateUIElements(){
        txtViewTitle = (TextView)findViewById(R.id.txtViewTitle);
        imgView = (ImageView)findViewById(R.id.imageViewDetail);
        txtViewDesc = (TextView)findViewById(R.id.txtViewDescription);
        txtViewPublicationDate = (TextView)findViewById(R.id.txtViewPublicationDate);
        txtViewDuration = (TextView)findViewById(R.id.txtVewDuration);
        progressBar = (ProgressBar)findViewById(R.id.progBar);
        progressBar.setMax(item.getDuration());
        btnPlayPause = (ImageButton)findViewById(R.id.btnPlayPause);
        setupMediaPlayer();
//        progressBar.setMax(100);
//        progressBar.setProgress(50);
//        mediaPlayer.prepare();
//        mediaPlayer.start();
    }

    public void setupMediaPlayer(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(item.getStrAudioPlaybackURL());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void populateUI(){
        txtViewTitle.setText(item.getStrTitle());
        txtViewPublicationDate.setText("Publication Date: " + item.getStrDate());
        txtViewDesc.setText("Description: " +item.getStrDescription());
        Picasso.with(this).load(item.getStrImgURL()).into(imgView);
        txtViewDuration.setText("Duration: " + (item.getDuration() +"")+ " sec");
    }

    public void btnPlayPauseClicked(View v){
        if(isRunning){
            mediaPlayer.pause();
            isRunning = false;
            btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
            return;
        }
        isRunning = true;
        reportTime = new ReportTime();
        reportTime.execute(item.getDuration());
        btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
        mediaPlayer.start();
    }


    class ReportTime extends AsyncTask<Integer,Integer,Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            do{
                if(isCancelled()){
                    break;
                }
                int progress = mediaPlayer.getCurrentPosition()/1000;
                publishProgress(progress);
            } while((mediaPlayer.getCurrentPosition()/1000)<params[0]);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values[0]);
            Log.d("Progress : ",""+values[0]);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isRunning = false;
            mediaPlayer.reset();
            progressBar.setProgress(0);
            btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    private void killMediaPlayer() {
        if(mediaPlayer!=null) {
            try {
                mediaPlayer.release();
            }
            catch(Exception e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(reportTime != null){
            reportTime.cancel(true);
        }
        killMediaPlayer();
    }
}
