package cs646.assignment3.photosharing;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class MainActivity extends Activity {
    private String userListUrl = "http://bismarck.sdsu.edu/photoserver/userlist";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Create user objects from reading in json and pass them to user list activity

//        Intent userListActivity = new Intent(getApplicationContext(), UserListActivity.class);
//        Bundle bundle = new Bundle();

        RequestQueue queue = Volley.newRequestQueue(this);
        Response.Listener<JSONArray> success = new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                Log.d("MainActivity.onResponse", response.toString());
            }
        };
        Response.ErrorListener failure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("MainActivity.onErrorResponse", error.toString());
            }
        };
        JsonArrayRequest getRequest = new JsonArrayRequest(userListUrl, success, failure);
        queue.add(getRequest);
//        startActivity(userListActivity);
    }
}
