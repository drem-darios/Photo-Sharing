package cs646.assignment3.photosharing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import cs646.assignment3.photosharing.api.Photo;

public class PhotoDisplayActivity extends ActionBarActivity {
    private String photoUrl = "http://bismarck.sdsu.edu/photoserver/photo/";
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);
        this.imageView =(ImageView) this.findViewById(R.id.imageView);
//        Bitmap thePhoto = BitmapFactory.decodeResource(getResources(), R.raw.blah);
//        showPhoto(thePhoto);

        Bundle extra = getIntent().getExtras();
        Photo photo = (Photo) extra.getSerializable("photo");

        RequestQueue imageRequestQueue = Volley.newRequestQueue(this);
        ImageRequest request = new ImageRequest(photoUrl + photo.getId(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                showPhoto(bitmap);
                Log.i("PhotoDisplayActivity.onResponse", "Image downloaded.");
            }
        }, 0, 0, null, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    String message = new String(error.networkResponse.data);
                    Log.e("PhotoDisplayActivity.onResponse", message);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageRequestQueue.add(request);
    }

    public void showPhoto(Bitmap thePhoto) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(thePhoto);
    }
}
