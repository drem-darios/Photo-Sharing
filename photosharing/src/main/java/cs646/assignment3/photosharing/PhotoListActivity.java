package cs646.assignment3.photosharing;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import cs646.assignment3.photosharing.constants.PhotoSharingConstants;
import cs646.assignment3.photosharing.persistence.DatabaseController;
import cs646.assignment3.photosharing.persistence.UserPhotosDB;
import cs646.assignment3.photosharing.util.JsonParser;

public class PhotoListActivity extends ListActivity {

    private List<Photo> photos = new ArrayList<Photo>();
    private UserPhotosDB photosDB;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);
        this.photosDB = new UserPhotosDB(new DatabaseController(this));

        Bundle extra = getIntent().getExtras();
        this.user = (User) extra.getSerializable(PhotoSharingConstants.USER_KEY);
        loadPhotos();

        setListAdapter(new ArrayAdapter<Photo>(this,
                android.R.layout.simple_list_item_1, photos));

    }


    @Override
    public void onListItemClick(ListView parent, View v, int position, long id)
    {
        Photo photo = (Photo)getListView().getItemAtPosition(position);
        Intent photoDisplayActivity = new Intent(getApplicationContext(), PhotoDisplayActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(PhotoSharingConstants.PHOTO_KEY, photo);
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

    private void loadPhotos() {
        // load users from db initially
        setPhotos(photosDB.getPhotosForUser(this.user.getId()));

        RequestQueue userListRequestQueue = Volley.newRequestQueue(this);
        Response.Listener<JSONArray> success = new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                Log.d("PhotoListActivity.onResponse", response.toString());
                try {
                    JSONArray photoList = new JSONArray(response.toString());
                    List<Photo> photos = (ArrayList<Photo>) JsonParser.parse(photoList, Photo.class);
                    setPhotos(photos);
                    // persist photo list
                    photosDB.addPhotos(user.getId(), photos);
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

        JsonArrayRequest getRequest = new JsonArrayRequest(PhotoSharingConstants.HOST +
                PhotoSharingConstants.PHOTO_LIST_PATH + user.getId(), success, failure);
        userListRequestQueue.add(getRequest);
    }

}
