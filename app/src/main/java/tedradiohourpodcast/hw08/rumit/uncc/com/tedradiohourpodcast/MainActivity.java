package tedradiohourpodcast.hw08.rumit.uncc.com.tedradiohourpodcast;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FetchTedPodcast.PodcastListReceived{
    ProgressDialog progressDialog;
    RecyclerView recycleView;
    RecyclerView.Adapter rvAdapter;
    RecyclerView.LayoutManager rvLayoutManager;
    ImageButton btnPlayPause;
    ProgressBar progBar;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    private MediaPlayer mediaPlayer;
    private int playbackPosition=0;
    ArrayList<PodcastItem> arrListPodcastListItems;
    boolean isLinear;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isLinear = true;
        instantiateUIElements();
        new FetchTedPodcast(this).execute();
    }

    private void instantiateUIElements(){
        recycleView = (RecyclerView)findViewById(R.id.recycleView);
        progBar = (ProgressBar)findViewById(R.id.progressBar);
        btnPlayPause = (ImageButton)findViewById(R.id.btnPlayPause);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Episodes...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        initToolBar();
        progressDialog.show();
    }

    public void initToolBar() {
        toolbar.setTitle("Ted Radio Hour PodCast");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ted);
        toolbar.inflateMenu(R.menu.main_menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_button:
                if(isLinear){
                    isLinear = false;
                }else{
                    isLinear = true;
                }
                if(arrListPodcastListItems != null){
                    populateUI(isLinear);
                }else{
                    Toast.makeText(this, "Please wait while the data loads.", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void populateUI(boolean isLinearLayout){
        if(isLinearLayout){
            if(this.linearLayoutManager != null){
                rvLayoutManager = linearLayoutManager;
            }else{
                rvLayoutManager = new LinearLayoutManager(this);
            }
        }else{
            if(this.gridLayoutManager != null){
                rvLayoutManager = gridLayoutManager;
            }else{
                rvLayoutManager = new GridLayoutManager(this,2);
            }
        }
        recycleView.setLayoutManager(rvLayoutManager);
        rvAdapter = new PodcastAdapter(this,arrListPodcastListItems,isLinearLayout,progBar,btnPlayPause);
        recycleView.setAdapter(rvAdapter);
        btnPlayPause = (ImageButton)findViewById(R.id.btnPlayPause);
        progBar = (ProgressBar)findViewById(R.id.progressBar);
        progressDialog.dismiss();
    }

    @Override
    public void podcastListReceived(ArrayList<PodcastItem> arrayList) {
        if(arrayList != null){
            arrListPodcastListItems = arrayList;
            populateUI(true);
        }else{
            // toast, for no data
        }
    }
}
