package com.boha.library.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.dto.ComplaintImageDTO;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by aubreyM on 14/12/17.
 */
public class ComplaintPictureAdapter extends RecyclerView.Adapter<ComplaintPictureAdapter.PhotoViewHolder> {

   
    private List<ComplaintImageDTO> photoList;
    private Context ctx;

    public ComplaintPictureAdapter(List<ComplaintImageDTO> photos,
                                   Context context) {
        this.photoList = photos;
        this.ctx = context;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {

        final ComplaintImageDTO p = photoList.get(position);
        final int num = photoList.size() - (position);
        holder.number.setText("" + num);
        holder.caption.setVisibility(View.GONE);
        holder.number.setVisibility(View.GONE);
        holder.date.setText(sdf.format(p.getDateTaken()));
        holder.position = position;

        String url = p.getUrl();
        Picasso.with(ctx).load(url).into(holder.image);


    }

    @Override
    public int getItemCount() {
        return photoList == null ? 0 : photoList.size();
    }

    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", loc);

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

    static final String LOG = ComplaintPictureAdapter.class.getSimpleName();
}
