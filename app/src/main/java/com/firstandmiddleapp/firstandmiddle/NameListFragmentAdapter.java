package com.firstandmiddleapp.firstandmiddle;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * The adapter for the NameListFragment. It takes an arrayList of NameListObjects.
 * @author Ali Abercrombie
 * Created on 3/6/2016.
 * @version 1.0.0
 */


public class NameListFragmentAdapter extends RecyclerView
        .Adapter<NameListFragmentAdapter
        .DataObjectHolder> {

    private ArrayList<NameListObject> mDataset;
    private static MyClickListener myClickListener;

    ContextMenu.ContextMenuInfo info;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView firstName;
        TextView firstNameRegion;
        TextView firstNameTimePeriod;
        TextView firstNameGender;


        public DataObjectHolder(View itemView) {
            super(itemView);
            firstName = (TextView) itemView.findViewById(R.id.namelist_first_name);
            firstNameRegion = (TextView) itemView.findViewById(R.id.namelist_first_name_region);
            firstNameTimePeriod = (TextView) itemView.findViewById(R.id.namelist_first_name_time);
            firstNameGender = (TextView) itemView.findViewById(R.id.namelist_first_name_gender);
            
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }


    public NameListFragmentAdapter(ArrayList<NameListObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_namelist_item, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        String name = mDataset.get(position).getmFirstName() + " " + mDataset.get(position).getmMiddleName();
        String region = mDataset.get(position).getmFirstNameRegion() + " / " + mDataset.get(position).getmMiddleNameRegion();
        String time = mDataset.get(position).getmFirstNameTimePeriod() + " / " + mDataset.get(position).getmMiddleNameTimePeriod();
        String gender = mDataset.get(position).getmFirstNameGender() + " / " + mDataset.get(position).getmMiddleNameGender();
        holder.firstName.setText(name);
        holder.firstNameRegion.setText(region);
        holder.firstNameTimePeriod.setText(time);
        holder.firstNameGender.setText(gender);
    }

    public void addItem(NameListObject nameListObject, int index) {
        mDataset.add(nameListObject);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }


}
