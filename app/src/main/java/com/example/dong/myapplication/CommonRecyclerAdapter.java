package com.example.dong.myapplication;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import java.util.List;
public class CommonRecyclerAdapter extends RecyclerView.Adapter<CommonRecyclerAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> mData;
    private int selectPosition = -1;

    public void setOnItemEditListener(OnItemEditListener onItemEditListener) {
        this.onItemEditListener = onItemEditListener;
    }

    private OnItemEditListener onItemEditListener;
    CommonRecyclerAdapter(Context context, List<String> data) {
        this.mContext = context;
        this.mData = data;
    }
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final TextView name = holder.name;
        name.setText(mData.get(position));

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setPosition(position);
            }
        });

        holder.deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //快速关闭item标签
                holder.swipeMenuLayout.quickClose();
                mData.remove(mData.get(position));
                notifyDataSetChanged();
            }
        });

        if(position == selectPosition){

            holder.name.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            holder.name.setTextColor(mContext.getResources().getColor(R.color.white));
        }else {


            holder.name.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            holder.name.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }

        holder.editBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemEditListener.onchange(position);
            }
        });

    }
    public int getItemCount() {
        return this.mData != null ? this.mData.size() : 0;
    }
    protected class MyViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder{
        Button deleteBt;
        Button editBt;
        SwipeMenuLayout swipeMenuLayout;
        LinearLayout linearLayout;
        TextView name;
        MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.content);
            linearLayout = itemView.findViewById(R.id.list_item);
            deleteBt = itemView.findViewById(R.id.delete_bt);
            editBt = itemView.findViewById(R.id.edit_bt);
            swipeMenuLayout = itemView.findViewById(R.id.swipemenu_Layout);
        }
    }

    private void setPosition(int position){
        this.selectPosition = position;
        notifyDataSetChanged();
    }
    interface OnItemEditListener{
        void onchange(int position);
    }


}
