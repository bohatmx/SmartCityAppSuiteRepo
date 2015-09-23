package com.boha.library.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.transfer.PhotoUploadDTO;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by aubreyM on 14/12/17.
 */
public class FullPictureAdapter extends RecyclerView.Adapter<FullPictureAdapter.PhotoViewHolder> {

    public interface PictureListener {
        public void onPictureClicked(int position);
    }

    private PictureListener listener;
    private List<PhotoUploadDTO> photoList;
    private Context ctx;
    private int rowLayout;

    public FullPictureAdapter(List<PhotoUploadDTO> photos, int rowLayout,
                              Context context, PictureListener listener) {
        this.photoList = photos;
        this.rowLayout = rowLayout;
        this.ctx = context;
        this.listener = listener;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.full_photo_item, parent, false);
        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {

        PhotoUploadDTO p = photoList.get(position);

        holder.number.setText("" + (position + 1));
        holder.caption.setVisibility(View.GONE);
       // holder.date.setText(sdf.format(p.getDateTaken()));
        holder.position = position;

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPictureClicked(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return photoList == null ? 0 : photoList.size();
    }

    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm", loc);

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        protected ImageView image;
        protected TextView caption, number, date;
        protected int position;


        public PhotoViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.PHOTO_image);
            caption = (TextView) itemView.findViewById(R.id.PHOTO_caption);
            number = (TextView) itemView.findViewById(R.id.PHOTO_number);
            date = (TextView) itemView.findViewById(R.id.PHOTO_date);
        }

    }

    static final String LOG = FullPictureAdapter.class.getSimpleName();
}
