package com.example.kunal.actorsjson;

import android.app.DownloadManager;
import android.app.VoiceInteractor;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<String> {

    public final static String URL1 = "http://microblogging.wingnity.com/JSONParsingTutorial/jsonActors";


    ListView ActorlistView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActorlistView = (ListView) findViewById(R.id.listview_actorList);





        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start




    }


    public void onclickload(View view) {


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL1, this, this);
        requestQueue.add(stringRequest);


    }


    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {


        StringBuffer stringBuffer = new StringBuffer();
        ArrayList<ActorDetailsModel> listactorDetailsModels = new ArrayList<>();


        try {
            JSONObject parentObject = new JSONObject(response);
            JSONArray parentArray = parentObject.getJSONArray("actors");

            Gson gson=new Gson();


            for (int i = 0; i < parentArray.length(); i++) {

                JSONObject finalobject = parentArray.getJSONObject(i);

                ActorDetailsModel actorDetailsModel=gson.fromJson(finalobject.toString(),ActorDetailsModel.class);

/*
                ActorDetailsModel actorDetailsModel = new ActorDetailsModel();

                actorDetailsModel.setName(finalobject.getString("name"));
                actorDetailsModel.setDescription(finalobject.getString("description"));
                actorDetailsModel.setDob(finalobject.getString("dob"));
                actorDetailsModel.setCountry(finalobject.getString("country"));
                actorDetailsModel.setHeight(finalobject.getString("height"));
                actorDetailsModel.setSpouse(finalobject.getString("spouse"));
                actorDetailsModel.setChildren(finalobject.getString("children"));
                actorDetailsModel.setImage(finalobject.getString("image"));

*/
                listactorDetailsModels.add(actorDetailsModel);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

/*
        for (ActorDetailsModel list : listactorDetailsModels) {

            stringBuffer.append("Name : " + list.getName() +
                    "\nDescription : " + list.getDescription() +
                    "\nDOB : " + list.getDob() +
                    "\nCountry : " + list.getCountry() +
                    "\nHeight : " + list.getHeight() +
                    "\nspouse : " + list.getSpouse() +
                    "\nChildren : " + list.getChildren() +
                    "\nImage : " + list.getImage() +
                    "\n------------------------------\n"

            );
        }
        showData.setText(stringBuffer.toString());

*/
        myAdapter myAdapter = new myAdapter(this, listactorDetailsModels);
        ActorlistView.setAdapter(myAdapter);


    }


    class ViewHolder {

        public final TextView name, dob, country, height, spouse, children, description;
        ImageView actorIcon;

        public ViewHolder(View view) {

            actorIcon = (ImageView) view.findViewById(R.id.imageView_actor_icon);
            name = (TextView) view.findViewById(R.id.tvNameValue);
            dob = (TextView) view.findViewById(R.id.TextView_dateOfBirth);
            country = (TextView) view.findViewById(R.id.TextView_country_value);
            height = (TextView) view.findViewById(R.id.textView12);
            spouse = (TextView) view.findViewById(R.id.textView_spouse_value);
            children = (TextView) view.findViewById(R.id.textView_childern_value);
            description = (TextView) view.findViewById(R.id.textView_description_value);
        }
    }


    class myAdapter extends BaseAdapter {

        ArrayList<ActorDetailsModel> list = new ArrayList<>();
        Context context;

        public myAdapter(Context context, ArrayList<ActorDetailsModel> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(int postion, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder=null;
            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                view = layoutInflater.inflate(R.layout.acter_list_single_item,null);


                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
                viewHolder.name.setText(list.get(postion).name);
                viewHolder.dob.setText(list.get(postion).dob);
                viewHolder.country.setText(list.get(postion).country);
                viewHolder.height.setText(list.get(postion).height);
                viewHolder.spouse.setText(list.get(postion).spouse);
                viewHolder.children.setText(list.get(postion).children);
                viewHolder.description.setText(list.get(postion).description);

                String imageLink = list.get(postion).image;

                final ProgressBar progressBar= (ProgressBar) view.findViewById(R.id.progressBar);

                // image loading... add this in oncreate


                // Then later, when you want to display image
                ImageLoader.getInstance().displayImage(imageLink, viewHolder.actorIcon, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        progressBar.setVisibility(View.GONE);
                    }
                }); // Default options will be used



            return view;
        }
    }

}

