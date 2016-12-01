package com.boha.library.rssreader;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.CityApplication;
/*import com.boha.library.fragments.FaqFragment;*/
import com.boha.library.util.Statics;

/**
 * Created by Nkululeko on 2016/08/30.
 */
public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {

    //FaqListener listener;
    FaqListListener listener;

    String [] FAQ;
    String [] FAQ_NUMBER;
    LayoutInflater inflater;
    private Context ctx;
    private static final String TEXT = "text/html", UTF = "UTF-8";
    static final String LOG = FaqAdapter.class.getSimpleName();

    public FaqAdapter(Context context,String[] FAQ, String[] FAQ_NUMBER, FaqListListener listener){
        this.ctx = context;
        this.FAQ = FAQ;
        this.FAQ_NUMBER = FAQ_NUMBER;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFaqType, txtNumber;
        protected View mainView;

        public ViewHolder(View itemView) {
            super(itemView);
            txtFaqType = (TextView) itemView.findViewById(R.id.FTYPE_faqType);
            txtNumber = (TextView) itemView.findViewById(R.id.FTYPE_number);
            mainView = (itemView).findViewById(R.id.FAQTYPE_main);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
     View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faqtype_item, parent, false);

    //View view = LayoutInflater.from(ctx).inflate(R.layout.faqtype_item, parent, false);
    ViewHolder viewHolder = new ViewHolder(view);





    return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.txtFaqType.setText(FAQ[position]);
        holder.txtFaqType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFaqClicked(position);
            }
        });
       holder.txtNumber.setText(FAQ_NUMBER[position]);

        Statics.setRobotoFontLight(ctx, holder.txtFaqType);

        switch(position) {
            case CityApplication.THEME_INDIGO:
          //      holder.txtFaqType.setText("Accounts & Payments");
                holder.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xindigo_oval_small));
                break;
            case CityApplication.THEME_GREEN:
       //         holder.txtFaqType.setText("Water & Sanitation");
                holder.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xgreen_oval_small));
                break;
            case CityApplication.THEME_BROWN:
      //          holder.txtFaqType.setText("Cleansing & Solid Waste");
                holder.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xbrown_oval_small));
                break;
            case CityApplication.THEME_AMBER:
        //        holder.txtFaqType.setText("Rates & Taxes");
                holder.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xamber_oval_small));
                break;
            case CityApplication.THEME_PURPLE:
    //            holder.txtFaqType.setText("Building Plans");
                holder.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xpurple_oval_small));
                break;
            case CityApplication.THEME_LIME:
     //           holder.txtFaqType.setText("Electricity");
                holder.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xlime_oval_small));
                break;
            case CityApplication.THEME_GREY:
     //           holder.txtFaqType.setText("Social Services");
                holder.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xgrey_oval_small));
                break;
            case CityApplication.THEME_BLUE:
    //            holder.txtFaqType.setText("Health");
                holder.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xblue_oval_small));
                break;
            case CityApplication.THEME_BLUE_GRAY:
    //            holder.txtFaqType.setText("Metro Police");
                holder.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xblue_gray_oval_small));
                break;
            case CityApplication.THEME_TEAL:
                holder.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xteal_oval_small));
                break;
            case CityApplication.THEME_CYAN:
                holder.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xcyan_oval_small));
                break;
            case CityApplication.THEME_ORANGE:
                holder.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xorange_oval_small));
                break;
            case CityApplication.THEME_PINK:
                holder.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xpink_oval_small));
                break;
            case CityApplication.THEME_RED:
                holder.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xred_oval_small));
                break;
        }
        // item.txtNumber.setBackground(ContextCompat.getDrawable(ctx, R.drawable.xindigo_oval_small));
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFaqClicked(position);
            }

        });
        //   return (view);



    }




    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return FAQ.length;
    }

    public interface FaqListListener {
        public void onFaqClicked(int position);
    }
}
