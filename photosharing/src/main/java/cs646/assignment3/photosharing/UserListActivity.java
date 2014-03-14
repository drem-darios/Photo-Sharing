package cs646.assignment3.photosharing;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cs646.assignment3.photosharing.api.User;
import cs646.assignment3.photosharing.util.JsonParser;

public class UserListActivity extends ListActivity {
    private String userListUrl = "http://bismarck.sdsu.edu/photoserver/userlist";
    private List<User> users = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        RequestQueue userListRequestQueue = Volley.newRequestQueue(this);
        Response.Listener<JSONArray> success = new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                Log.d("UserListActivity.onResponse", response.toString());
                try {
                    JSONArray userListArray = new JSONArray(response.toString());
                    List<User> userList = (ArrayList<User>)JsonParser.parse(userListArray, User.class);
                    setUsers(userList);
                } catch (JSONException e) {
                    Log.e("UserListActivity.onResponse", "Could not parse json from response.", e);
                }
            }
        };
        Response.ErrorListener failure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("UserListActivity.onErrorResponse", error.toString());
            }
        };
        JsonArrayRequest getRequest = new JsonArrayRequest(userListUrl, success, failure);
        userListRequestQueue.add(getRequest);

        setListAdapter(new ArrayAdapter<User>(this,
                android.R.layout.simple_list_item_1, users));
    }

    private void setUsers(List<User> userList) {
        this.users = userList;
        ArrayAdapter adapter = new ArrayAdapter<User>(this,
                android.R.layout.simple_list_item_1, users);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView parent, View v, int position, long id)
    {
        User user = (User)getListView().getItemAtPosition(position);
        Intent userListActivity = new Intent(getApplicationContext(), PhotoListActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        userListActivity.putExtras(bundle);

        startActivity(userListActivity);
    }
}
