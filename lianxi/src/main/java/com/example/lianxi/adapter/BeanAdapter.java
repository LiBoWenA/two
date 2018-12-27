package com.example.lianxi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lianxi.R;
import com.example.lianxi.bean.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BeanAdapter extends RecyclerView.Adapter<BeanAdapter.ViewHolder> {
    private Context context;
    private List<Bean.DataBean> list;
    private boolean isLorG;

    public BeanAdapter(Context context,boolean isLorG) {
        this.context = context;
        this.isLorG = isLorG;
        list = new ArrayList<>();
    }

    public void setData(List<Bean.DataBean> lists){
        list.clear();
        if (lists != null){
            list.addAll(lists);
        }
        notifyDataSetChanged();
    }

    public void addData(List<Bean.DataBean> lists){
        if (lists != null){
            list.addAll(lists);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_grid, viewGroup, false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.t_title.setText(list.get(i).getTitle());
        viewHolder.t_price.setText(list.get(i).getPrice());
        String images = list.get(i).getImages();
        String[] split = images.split("\\|");
        List<String> listurl = Arrays.asList(split);
        Glide.with(context).load(listurl.get(0)).into(viewHolder.img);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClick != null){
                    onClick.click(i);
                }
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClick != null){
                    longClick.click(i);
                }
                return true;
            }
        });
    }

    public int getData(int position){
        int pid = list.get(position).getPid();
        return pid;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView t_title,t_price;
        private ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            t_title = itemView.findViewById(R.id.t_title);
            t_price = itemView.findViewById(R.id.t_price);
            img = itemView.findViewById(R.id.img);
        }
    }

    //点击
    public OnClick onClick;

    public void setOnClick(OnClick onClick){
        this.onClick = onClick;
    }

    public interface OnClick{
        void click(int position);
    }

    //长按
    public OnLongClick longClick;

    public void setOnClick(OnLongClick onLongClick){
        this.longClick = onLongClick;
    }

    public interface OnLongClick{
        void click(int position);
    }

    //删除
    public void removeItem(int position){
        list.remove(position);
        notifyDataSetChanged();
    }
}
