package cs646.assignment3.photosharing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cs646.assignment3.photosharing.util.JsonParser;
import cs646.assignment3.photosharing.api.User;

public class MainActivity extends Activity {
    private String userListUrl = "http://bismarck.sdsu.edu/photoserver/userlist";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue userListRequestQueue = Volley.newRequestQueue(this);
        Response.Listener<JSONArray> success = new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                Log.d("MainActivity.onResponse", response.toString());
                try {
                    JSONArray userListArray = new JSONArray(response.toString());
                    ArrayList<User> usersList = (ArrayList<User>)JsonParser.parse(userListArray, User.class);
                    Intent userListActivity = new Intent(getApplicationContext(), UserListActivity.class);
                    Bundle bundle = new Bundle();

//                    User[] users = usersList.toArray(new User[usersList.size()]);
                    bundle.putSerializable("users", usersList);
                    userListActivity.putExtras(bundle);
                    startActivity(userListActivity);
                } catch (JSONException e) {
                    Log.e("MainActivity.onResponse", "Could not parse json from response.", e);
                }
            }
        };
        Response.ErrorListener failure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("MainActivity.onErrorResponse", error.toString());
            }
        };
        JsonArrayRequest getRequest = new JsonArrayRequest(userListUrl, success, failure);
        userListRequestQueue.add(getRequest);
    }
}
