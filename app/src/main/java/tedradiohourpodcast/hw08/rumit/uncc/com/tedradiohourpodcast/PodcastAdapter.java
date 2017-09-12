package tedradiohourpodcast.hw08.rumit.uncc.com.tedradiohourpodcast;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rumit on 3/11/17.
 */

public class PodcastAdapter extends RecyclerView.Adapter implements View.OnClickListener{
    ArrayList<PodcastItem> arrListPodcastItems;
    Context mCOntext;
    boolean isListView;
    MediaPlayer mediaPlayer;
    ProgressBar progressBar;
    ImageButton imgButtonPlayPause;
    boolean isRunning;
    ReportTime reportTimeTask;
    public PodcastAdapter(Context context, ArrayList<PodcastItem> arrayList, boolean isListView, ProgressBar progressBar, ImageButton btnPlayPause){
        arrListPodcastItems = arrayList;
        mCOntext = context;
        this.isListView = isListView;
        this.progressBar = progressBar;
        this.imgButtonPlayPause = btnPlayPause;
        this.progressBar.setVisibility(View.INVISIBLE);
        this.imgButtonPlayPause.setVisibility(View.INVISIBLE);
        this.progressBar.requestLayout();
        this.imgButtonPlayPause.requestLayout();
        imgButtonPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning){
                    mediaPlayer.pause();
                    imgButtonPlayPause.setImageResource(android.R.drawable.ic_media_play);
                    isRunning = false;
                }else{
                    mediaPlayer.start();
                    imgButtonPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                    isRunning = true;
                }
            }
        });
    }


    static public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView txtViewTitle;
        public TextView txtViewpublishedDate;
        public ImageButton playButton;
        public ImageView imgView;
        public LinearLayout playButtonCompound;
        public ListViewHolder(View itemView) {
            super(itemView);
            this.txtViewTitle = (TextView) itemView.findViewById(R.id.txtViewTitle);
            this.txtViewpublishedDate = (TextView)itemView.findViewById(R.id.txtViewPostedDate);
            this.imgView =  (ImageView)itemView.findViewById(R.id.imgViewSong);
            this.playButton = (ImageButton)itemView.findViewById(R.id.btnPlay);
            this.playButtonCompound = (LinearLayout)itemView.findViewById(R.id.viewPlayNow);
//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("Click","Received");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(isListView ? R.layout.podcast_item_cell : R.layout.podcast_grid_cell,parent,false);
        v.setClickable(true);
        Log.d("ViewType",viewType + "");
        ListViewHolder viewHolder = new ListViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListViewHolder viewHolder = (ListViewHolder) holder;
        viewHolder.txtViewTitle.setText(arrListPodcastItems.get(position).getStrTitle());
        if(isListView) {
            viewHolder.txtViewpublishedDate.setText("posted: "+arrListPodcastItems.get(position).getStrDate());
            viewHolder.playButtonCompound.setOnClickListener(this);
            viewHolder.playButtonCompound.setClickable(true);
            viewHolder.playButtonCompound.setTag(position);
        }
                viewHolder.playButton.setOnClickListener(this);
        viewHolder.playButton.setTag(position);
        View v = (View)viewHolder.imgView.getParent();
        v.setTag(arrListPodcastItems.get(position));
        v.setOnClickListener(this);
        Picasso.with(mCOntext).load(arrListPodcastItems.get(position).getStrImgURL()).into(viewHolder.imgView);

    }

    @Override
    public void onClick(View v) {
        if( reportTimeTask != null) { reportTimeTask.cancel(true);}
        if(v instanceof ImageButton || v.getId() == R.id.viewPlayNow) {
            int tag = (int) v.getTag();
            try {
                    playAudio(arrListPodcastItems.get(tag));
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }else{
            killMediaPlayer();
            PodcastItem item = (PodcastItem)v.getTag();
            Log.d("Demo","click recceived" + item);
            Intent intent = new Intent("tedradiohourpodcast.hw08.rumit.uncc.com.tedradiohourpodcast.intent.action.VIEW_DETAILS");
            intent.putExtra("PodcastItem",item);
            mCOntext.startActivity(intent);
        }
    }

    private void playAudio(final PodcastItem item) throws Exception
    {
        killMediaPlayer();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(item.getStrAudioPlaybackURL());
//        progressBar.setMax(100);
//        progressBar.setProgress(50);
        mediaPlayer.prepare();
//        mediaPlayer.prepare();
//        mediaPlayer.start();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d("Demo","ready to play ");
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                progressBar.setMax(item.getDuration());
                imgButtonPlayPause.setVisibility(View.VISIBLE);
                progressBar.requestLayout();
                imgButtonPlayPause.requestLayout();
                boolean running = true;
                final int duration = item.getDuration();
                reportTimeTask = new ReportTime();
                reportTimeTask.execute(duration);
                mp.start();
                imgButtonPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                isRunning = true;
            }
        });
    }


    class ReportTime extends AsyncTask<Integer,Integer,Void>{

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
            progressBar.setVisibility(View.INVISIBLE);
            progressBar.requestLayout();
            imgButtonPlayPause.setVisibility(View.INVISIBLE);
            imgButtonPlayPause.setImageResource(android.R.drawable.ic_media_play);
            imgButtonPlayPause.requestLayout();
            isRunning = false;
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
            if(reportTimeTask != null){
                reportTimeTask.cancel(true);
                reportTimeTask = null;
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrListPodcastItems.size();
    }
}
