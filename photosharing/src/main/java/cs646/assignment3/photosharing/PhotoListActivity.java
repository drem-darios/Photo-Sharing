package cs646.assignment3.photosharing;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cs646.assignment3.photosharing.api.Photo;
import cs646.assignment3.photosharing.api.User;
import cs646.assignment3.photosharing.util.JsonParser;

public class PhotoListActivity extends ListActivity {
    private String photoListUrl = "http://bismarck.sdsu.edu/photoserver/userphotos/";
    private List<Photo> photos = new ArrayList<Photo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        RequestQueue userListRequestQueue = Volley.newRequestQueue(this);
        Response.Listener<JSONArray> success = new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                Log.d("PhotoListActivity.onResponse", response.toString());
                try {
                    JSONArray photoList = new JSONArray(response.toString());
                    List<Photo> photos = (ArrayList<Photo>) JsonParser.parse(photoList, Photo.class);
                    setPhotos(photos);
                } catch (JSONException e) {
                    Log.e("PhotoListActivity.onResponse", "Could not parse json from response.", e);
                }
            }
        };
        Response.ErrorListener failure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    String message = new String(error.networkResponse.data);
                    Log.e("PhotoListActivity.onErrorResponse", message);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        };

        Bundle extra = getIntent().getExtras();
        User user = (User) extra.getSerializable("user");

        JsonArrayRequest getRequest = new JsonArrayRequest(photoListUrl + user.getId(), success, failure);
        userListRequestQueue.add(getRequest);

        setListAdapter(new ArrayAdapter<Photo>(this,
                android.R.layout.simple_list_item_1, photos));

    }


    @Override
    public void onListItemClick(ListView parent, View v, int position, long id)
    {
        Photo photo = (Photo)getListView().getItemAtPosition(position);
        Intent photoDisplayActivity = new Intent(getApplicationContext(), PhotoDisplayActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("photo", photo);
        photoDisplayActivity.putExtras(bundle);

        startActivity(photoDisplayActivity);
    }

    private void setPhotos(List<Photo> photoList) {
        this.photos = photoList;
        ArrayAdapter adapter = new ArrayAdapter<Photo>(this,
                android.R.layout.simple_list_item_1, photos);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
