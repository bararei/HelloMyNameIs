package com.firstandmiddleapp.firstandmiddle;

/**
 * Adapted from http://www.android4devs.com/2014/12/how-to-make-material-design-navigation-drawer.html
 * Creates the recyclerview for the navigation drawer. Also adds in header and home navigation depending
 * on the type of view.
 * @author Ali Abercrombie
 * @created 4/42016
 * @version 1.0.0
 */


import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firstandmiddleapp.firstandmiddle.R;

import java.util.ArrayList;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> implements AppStatics{


        // Declaring Variables to Understand which View is being worked on
        private static final int TYPE_HEADER = 0;

        private static final int TYPE_HOME = 1;

        private static final int TYPE_ITEM = 2;

        private ArrayList<ProjectObject> projectNamesArrayList;


    /**
     * Creates a ViewHolder which extends the RecyclerView View Holder.
     * ViewHolder are used to store the inflated views in order to recycle them
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
            int Holderid;

            TextView textView;
            TextView authorName;
            TextView mHome;


        /**
         * Viewholder constructor. Sets the appropriate view in accordance with the the view type as passed when the holder object is created
         * @param itemView the itemView
         * @param ViewType the ViewType
         */
        public ViewHolder(View itemView,int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);


            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.drawerText); // Creating TextView object with the id of textView from item_row.xml

                Holderid = 2;                                               // setting holder id as 1 as the object being populated are of type item row
            }
            else if (ViewType == TYPE_HOME) {

                mHome = (TextView) itemView.findViewById(R.id.drawerHomeText);

                Holderid = 1;
            } else {

                authorName = (TextView) itemView.findViewById(R.id.authorName);         // Creating Text View object from header.xml for name
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
        }


    }


    /**
     * Sets the local projectNamesArrayList equal to the projectNames ArrayList passed into the method.
     * @param projectNames the ArrayList being passed in
     */
    DrawerAdapter(ArrayList<ProjectObject> projectNames){

        projectNamesArrayList = projectNames;

    }


    /**
     * Inflates the proper View depending on the viewType. First we override the method onCreateViewHolder which is called when the ViewHolder is
     * created. Inflate the drawer_items.xml layout if the viewType is Type_ITEM, inflate the drawer_home
     * if the viewType is Type_HOME or else we inflate drawer_header.xml if the viewType is TYPE_HEADER and pass it to the view holder
     * @param parent the ViewGroup
     * @param viewType the viewType
     * @return
     */
    @Override
    public DrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_items,parent,false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header,parent,false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created


        } else if (viewType == TYPE_HOME) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_home,parent,false); //Inflating the layout
            ViewHolder vhHome = new ViewHolder(v, viewType);

            return vhHome;

        }
        return null;

    }

    /**
     * Next we override a method which is called when the item in a row is needed to be displayed, here the int position
     * Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
     * which view type is being created.
     * @param holder the ViewHolder
     * @param position the position of the listview
     */
    @Override
    public void onBindViewHolder(DrawerAdapter.ViewHolder holder, int position) {
        if(holder.Holderid == 2) {
            holder.textView.setText(projectNamesArrayList.get(position - 2).getProjectName());


        } else if (holder.Holderid == 1) {
            holder.mHome.setText("All Projects");
        }
        else{
            SharedPreferences sharedPreferences = holder.authorName.getContext().getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0);
            String name = sharedPreferences.getString(AUTHOR_NAME_KEY,"Your Name");
            holder.authorName.setText(name);
        }
    }

    /**
     * This method returns the number of items present in the list. Is +2 to include the header and home views.
     * @return
     */
    @Override
    public int getItemCount() {
        return projectNamesArrayList.size()+2; // the number of items in the list will be +2 the titles including the header  and home view.
    }


    /**
     * Checks what type of view is being passed and returns it to the OnCreateViewHolder
     * @param position the position of the listview
     * @return viewType
     */
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        else if (isPositionHome(position))
            return TYPE_HOME;

        return TYPE_ITEM;
    }

    /**
     * Checks if the position of the listview is the header
     * @param position position of the listview
     * @return position
     */
    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    /**
     * Checks if the position of the listview is the home row
     * @param position position of the listview
     * @return position
     */
    private boolean isPositionHome(int position) {
        return position == 1;
    }

}
