package tedradiohourpodcast.hw08.rumit.uncc.com.tedradiohourpodcast;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Rumit on 3/10/17.
 */

public class PodcastListAdapter extends ArrayAdapter<PodcastItem> {

    Context mContext;
    ArrayList<PodcastItem> arrListItems;
    public PodcastListAdapter(Context context, int resource, ArrayList<PodcastItem> objects) {
        super(context, resource, objects);
        this.mContext = context;
        arrListItems = objects;
    }

//    @NonNull
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        //return super.getView(position, convertView, parent);
////        ViewHolder viewHolder;
////        if(convertView == null){
////            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////            convertView = inflater.inflate(R.layout.podcast_item_cell,parent,false);
////            viewHolder = new ViewHolder();
////        }
//    }

    private class ViewHolder{

    }

}
