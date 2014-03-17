package cs646.assignment3.photosharing;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;

import cs646.assignment3.photosharing.api.Photo;
import cs646.assignment3.photosharing.constants.PhotoSharingConstants;

public class PhotoDisplayActivity extends Activity {

    private ImageView imageView;
    private Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);
        this.imageView = (ImageView) this.findViewById(R.id.imageView);

        Bundle extra = getIntent().getExtras();
        this.photo = (Photo) extra.getSerializable(PhotoSharingConstants.PHOTO_KEY);

        RequestQueue imageRequestQueue = Volley.newRequestQueue(this);
        ImageRequest request = new ImageRequest(PhotoSharingConstants.HOST +
                PhotoSharingConstants.PHOTO_PATH + photo.getId(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                showPhoto(bitmap);
                try {
                    OutputStream file = new BufferedOutputStream(
                            openFileOutput(photo.getName(), MODE_PRIVATE));
                    // format, quality(1-100), stream
                    boolean compressed = bitmap.compress(Bitmap.CompressFormat.JPEG, 50, file);
                    file.close();
                    Log.i("PhotoDisplayActivity.onResponse", "Image saved locally. Compressed: " + compressed);
                } catch (Exception e) {
                    Log.w("PhotoDisplayActivity.onResponse", "Failed to save image locally.", e);
                }
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
        }
        );
        // try to read this photo from local storage
        Bitmap bitmap = BitmapFactory.decodeFile(getFilePath(photo.getName()), null);
        if (bitmap != null) {
            // display photo if exists
            Log.i("PhotoDisplayActivity.onCreate", "Loading image [" + photo.getName() + "] from local storage.");
            showPhoto(bitmap);
        } else {
            // retrieve photo from server
            imageRequestQueue.add(request);
        }
    }

    /**
     * Helper method to show the photo selected by the user. This will scale, center, and set the
     * image to the image view.
     *
     * @param thePhoto - The photo to display on screen.
     */
    private void showPhoto(Bitmap thePhoto) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(thePhoto);
    }

    /**
     * Helper method to get file path for the given file name.
     *
     * @param filename - Name of the file to get the path to.
     * @return path to this file or null if the file is not found.
     */
    private String getFilePath(String filename) {
        File photoFile = this.getFileStreamPath(filename);
        return photoFile.getAbsolutePath();
    }
}
