package com.example.ponomarev.automobileguide.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ponomarev.automobileguide.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Andrey on 31.03.2018.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ItemViewHolder>{

    private static RVClickListener clickListener;
    String[] name;
    String[] image;
    Context context;
    String type;
    int layout;
    public RVAdapter(Context context, String[] name, String[] image,int layout){
        this.context=context;
        this.name=name;
        this.image=image;
        this.layout=layout;
    }
    public RVAdapter(Context context, String[] name, String[] image,int layout,String type){
        this.context=context;
        this.name=name;
        this.image=image;
        this.layout=layout;
        this.type=type;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ItemViewHolder pvh = new ItemViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.setIsRecyclable(true);
        if(type=="center")holder.itemImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        holder.itemName.setText(name[position]);
        InputStream inputStream = null;
        try{
            inputStream = context.getAssets().open(this.image[position]);
            Drawable d = Drawable.createFromStream(inputStream, null);
            holder.itemImage.setImageDrawable(d);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try{
                if(inputStream!=null)
                    inputStream.close();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView itemName;
        TextView itemDescription;
        ImageView itemImage;
        ItemViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            cv.setOnClickListener(this);
            itemName = (TextView)itemView.findViewById(R.id.name);
            itemDescription = (TextView)itemView.findViewById(R.id.description);
            itemImage = (ImageView)itemView.findViewById(R.id.image);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

    }
    public void setOnItemClickListener(RVClickListener clickListener) {
        RVAdapter.clickListener =  clickListener;
    }

    public interface RVClickListener {
        void onItemClick(int position, View v);
    }

}

