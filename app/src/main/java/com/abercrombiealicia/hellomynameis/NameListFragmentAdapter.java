package com.abercrombiealicia.hellomynameis;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Spheven on 3/6/2016.
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
        TextView middleName;
        TextView middleNameRegion;
        TextView middleNameTimePeriod;
        TextView middleNameGender;


        public DataObjectHolder(View itemView) {
            super(itemView);
            firstName = (TextView) itemView.findViewById(R.id.namelist_first_name);
            firstNameRegion = (TextView) itemView.findViewById(R.id.namelist_first_name_region);
            firstNameTimePeriod = (TextView) itemView.findViewById(R.id.namelist_first_name_time);
            firstNameGender = (TextView) itemView.findViewById(R.id.namelist_first_name_gender);
            middleName = (TextView) itemView.findViewById(R.id.namelist_middle_name);
            middleNameRegion = (TextView) itemView.findViewById(R.id.namelist_middle_name_region);
            middleNameTimePeriod = (TextView) itemView.findViewById(R.id.namelist_middle_name_time);
            middleNameGender = (TextView) itemView.findViewById(R.id.namelist_middle_name_gender);

            Log.i("TEST", "Adding NameList Listeners");
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
        holder.firstName.setText(mDataset.get(position).getmFirstName());
        holder.firstNameRegion.setText(mDataset.get(position).getmFirstNameRegion());
        holder.firstNameTimePeriod.setText(mDataset.get(position).getmFirstNameTimePeriod());
        holder.firstNameGender.setText(mDataset.get(position).getmFirstNameGender());
        holder.middleName.setText(mDataset.get(position).getmLastName());
        holder.middleNameRegion.setText(mDataset.get(position).getmLastNameRegion());
        holder.middleNameTimePeriod.setText(mDataset.get(position).getmLastNameTimePeriod());
        holder.middleNameGender.setText(mDataset.get(position).getmLastNameGender());
        Log.i("TEST", "Adding BindViewHolder NameList Listeners");
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
