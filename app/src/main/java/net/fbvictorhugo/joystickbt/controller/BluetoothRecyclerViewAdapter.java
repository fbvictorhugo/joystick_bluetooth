package net.fbvictorhugo.joystickbt.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.fbvictorhugo.joystickbt.R;

import java.util.ArrayList;
import java.util.List;

public class BluetoothRecyclerViewAdapter extends RecyclerView.Adapter<BluetoothRecyclerViewAdapter.ViewHolder> {

    private List<BluetoothModel> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    public BluetoothRecyclerViewAdapter(Context context, List<BluetoothModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = mInflater.inflate(android.R.layout.simple_list_item_2, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        final BluetoothModel btmodel = mData.get(viewHolder.getAdapterPosition());

        viewHolder.textName.setText(btmodel.getName());
        viewHolder.textAddress.setText(btmodel.getAddress());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onClick(btmodel, viewHolder.getAdapterPosition());
                }
            }
        });

        if (btmodel.isBonded()) {
            viewHolder.textName.setTextColor(mContext.getResources().getColor(R.color.bluetooth_color));
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void Update(ArrayList<BluetoothModel> list) {
        mData = list;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textAddress;
        View view;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textName = itemView.findViewById(android.R.id.text1);
            textAddress = itemView.findViewById(android.R.id.text2);
        }
    }

    public interface OnItemClickListener {
        void onClick(BluetoothModel device, int position);
    }

}
