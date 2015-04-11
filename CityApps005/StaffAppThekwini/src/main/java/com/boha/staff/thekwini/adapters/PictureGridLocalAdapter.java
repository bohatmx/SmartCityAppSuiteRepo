package com.boha.staff.thekwini.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.boha.staff.thekwini.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by aubreyM on 14/12/17.
 */
public class PictureGridLocalAdapter extends RecyclerView.Adapter<PictureGridLocalAdapter.PhotoViewHolder> {

    public interface PictureGridListener {
        public void onPictureClicked(int position);
    }
    private PictureGridListener listener;
    private Context ctx;
    private int rowLayout;

    public PictureGridLocalAdapter(int rowLayout,
                                   Context context, PictureGridListener listener) {
        this.rowLayout = rowLayout;
        this.ctx = context;
        this.listener = listener;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {

        holder.number.setText("" + (position + 1));
        holder.caption.setVisibility(View.GONE);
        holder.position = position;
        holder.date.setVisibility(View.GONE);

        Drawable p = null;
        switch (position) {
            case 0:
                p =  ctx.getResources().getDrawable(R.drawable.dbn10);
                break;
            case 1:
                p =  ctx.getResources().getDrawable(R.drawable.dbn11);
                break;
            case 2:
                p =  ctx.getResources().getDrawable(R.drawable.dbn12);
                break;
            case 3:
                p =  ctx.getResources().getDrawable(R.drawable.dbn13);
                break;
            case 4:
                p =  ctx.getResources().getDrawable(R.drawable.dbn14);
                break;

            case 5:
                p =  ctx.getResources().getDrawable(R.drawable.dbn15);
                break;
            case 6:
                p =  ctx.getResources().getDrawable(R.drawable.dbn16);
                break;
            case 7:
                p =  ctx.getResources().getDrawable(R.drawable.dbn17);
                break;
            case 8:
                p =  ctx.getResources().getDrawable(R.drawable.dbn18);
                break;
            case 9:
                p =  ctx.getResources().getDrawable(R.drawable.dbn19);
                break;
            default:
                p =  ctx.getResources().getDrawable(R.drawable.dbn13);
                break;

        }
        holder.image.setImageDrawable(p);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPictureClicked(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", loc);

    public class PhotoViewHolder extends RecyclerView.ViewHolder  {
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

    static final String LOG = PictureGridLocalAdapter.class.getSimpleName();
}
