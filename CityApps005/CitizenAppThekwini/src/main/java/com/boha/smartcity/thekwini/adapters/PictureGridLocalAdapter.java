package com.boha.smartcity.thekwini.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.boha.smartcity.thekwini.R;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;

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
    private Random random = new Random(System.currentTimeMillis());

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
                p =  ctx.getResources().getDrawable(R.drawable.dbn2);
                break;
            case 1:
                p =  ctx.getResources().getDrawable(R.drawable.dbn3);
                break;
            case 2:
                p =  ctx.getResources().getDrawable(R.drawable.dbn1);
                break;
            case 3:
                p =  ctx.getResources().getDrawable(R.drawable.dbn4);
                break;
            case 4:
                p =  ctx.getResources().getDrawable(R.drawable.dbn6);
                break;

            case 5:
                p =  ctx.getResources().getDrawable(R.drawable.dbn8);
                break;
            case 6:
                p =  ctx.getResources().getDrawable(R.drawable.dbn10);
                break;
            case 7:
                p =  ctx.getResources().getDrawable(R.drawable.dbn11);
                break;
            case 8:
                p =  ctx.getResources().getDrawable(R.drawable.dbn12);
                break;
            case 9:
                p =  ctx.getResources().getDrawable(R.drawable.dbn13);
                break;

            case 10:
                p =  ctx.getResources().getDrawable(R.drawable.dbn14);
                break;
            case 11:
                p =  ctx.getResources().getDrawable(R.drawable.dbn15);
                break;
            case 12:
                p =  ctx.getResources().getDrawable(R.drawable.dbn16);
                break;
            case 13:
                p =  ctx.getResources().getDrawable(R.drawable.dbn17);
                break;
            case 14:
                p =  ctx.getResources().getDrawable(R.drawable.dbn18);
                break;

            case 15:
                p =  ctx.getResources().getDrawable(R.drawable.dbn19);
                break;
            case 16:
                p =  ctx.getResources().getDrawable(R.drawable.dbn20);
                break;
            case 17:
                p =  ctx.getResources().getDrawable(R.drawable.dbn21);
                break;
            case 18:
                p =  ctx.getResources().getDrawable(R.drawable.dbn22);
                break;
            case 19:
                p =  ctx.getResources().getDrawable(R.drawable.dbn23);
                break;

            case 20:
                p =  ctx.getResources().getDrawable(R.drawable.dbn24);
                break;
            case 21:
                p =  ctx.getResources().getDrawable(R.drawable.dbn25);
                break;
            case 22:
                p =  ctx.getResources().getDrawable(R.drawable.dbn26);
                break;
            case 23:
                p =  ctx.getResources().getDrawable(R.drawable.dbn27);
                break;
            case 24:
                p =  ctx.getResources().getDrawable(R.drawable.dbn28);
                break;

            case 25:
                p =  ctx.getResources().getDrawable(R.drawable.dbn29);
                break;
            case 26:
                p =  ctx.getResources().getDrawable(R.drawable.dbn30);
                break;
            case 27:
                p =  ctx.getResources().getDrawable(R.drawable.dbn31);
                break;
            case 28:
                p =  ctx.getResources().getDrawable(R.drawable.dbn32);
                break;
            case 29:
                p =  ctx.getResources().getDrawable(R.drawable.dbn33);
                break;
            case 30:
                p =  ctx.getResources().getDrawable(R.drawable.dbn34);
                break;
            case 31:
                p =  ctx.getResources().getDrawable(R.drawable.dbn35);
                break;
            case 32:
                p =  ctx.getResources().getDrawable(R.drawable.dbn37);
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


//        String url = Statics.IMAGE_URL + p.getUri();
//        ImageLoader.getInstance().displayImage(url, holder.image, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String s, View view) {
//
//            }
//
//            @Override
//            public void onLoadingFailed(String s, View view, FailReason failReason) {
//                holder.image.setImageDrawable(ctx.getResources().getDrawable(R.drawable.under_construction));
//            }
//
//            @Override
//            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//
//            }
//
//            @Override
//            public void onLoadingCancelled(String s, View view) {
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return 33;
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

    private class ImageList {
    }
}
