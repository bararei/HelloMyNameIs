package com.abercrombiealicia.hellomynameis;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * Created by Spheven on 3/6/2016.
 */


public class ProjectFragmentAdapter extends RecyclerView
        .Adapter<ProjectFragmentAdapter
        .DataObjectHolder> implements View.OnClickListener {

    private ArrayList<ProjectObject> mDataset;
   // private static MyClickListener myClickListener;
    protected AdapterView.OnItemClickListener listener;
    private GestureDetector gestureDetector;

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }


    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView description;

        public DataObjectHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.projectItem);
            description = (TextView) itemView.findViewById(R.id.projectDescription);
            Log.i("TEST", "Adding Project Listeners");
            //itemView.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }

        /**
         * Called when a view has been clicked and held.
         *
         * @param v The view that was clicked and held.
         * @return true if the callback consumed the long click, false otherwise.
         */
       /* @Override
        public boolean onLongClick(View v) {
            myClickListener.onItemLongClick();
            return true;
        }*/
    }

   /* public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }*/

    public ProjectFragmentAdapter(ArrayList<ProjectObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_project_item, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.name.setText(mDataset.get(position).getProjectName());
        holder.description.setText(mDataset.get(position).getProjectDescription());
    }

    public void addItem(ProjectObject projectObject, int index) {
        mDataset.add(projectObject);
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


}

