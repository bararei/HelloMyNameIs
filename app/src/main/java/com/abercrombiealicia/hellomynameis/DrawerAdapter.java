package com.abercrombiealicia.hellomynameis;

/**
 * Created by Ali on 4/4/2016.
 * Adapted from http://www.android4devs.com/2014/12/how-to-make-material-design-navigation-drawer.html
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> implements AppStatics{


        private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
        // IF the view under inflation and population is header or Item

        private static final int TYPE_HOME = 1;

        private static final int TYPE_ITEM = 2;

        private ArrayList<ProjectObject> projectNamesArrayList;

        private String name; //String Resource for header View Name
        Context context;


        // Creating a ViewHolder which extends the RecyclerView View Holder
        // ViewHolder are used to to store the inflated views in order to recycle them

        public static class ViewHolder extends RecyclerView.ViewHolder {
            int Holderid;

            TextView textView;
            TextView authorName;
            TextView mHome;


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



        DrawerAdapter(ArrayList<ProjectObject> projectNames){

            projectNamesArrayList = projectNames;

        }



        //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
        //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
        // if the viewType is TYPE_HEADER
        // and pass it to the view holder

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

        //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
        // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
        // which view type is being created 1 for item row
        @Override
        public void onBindViewHolder(DrawerAdapter.ViewHolder holder, int position) {
            if(holder.Holderid == 2) {                              // as the list view is going to be called after the header view so we decrement the
                // position by 1 and pass it to the holder while setting the text and image
                holder.textView.setText(projectNamesArrayList.get(position - 2).getProjectName());


            } else if (holder.Holderid == 1) {
                holder.mHome.setText("All Projects");
            }
            else{
                // Similarly we set the resources for header view
                SharedPreferences sharedPreferences = holder.authorName.getContext().getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0);
                String name = sharedPreferences.getString(AUTHOR_NAME_KEY,"Your Name");
                holder.authorName.setText(name);
            }
        }

        // This method returns the number of items present in the list
        @Override
        public int getItemCount() {
            return projectNamesArrayList.size()+2; // the number of items in the list will be +1 the titles including the header view.
        }


        // Witht the following method we check what type of view is being passed
        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position))
                return TYPE_HEADER;
            else if (isPositionHome(position))
                return TYPE_HOME;

            return TYPE_ITEM;
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }

        private boolean isPositionHome(int position) {
            return position == 1;
        }

    }
