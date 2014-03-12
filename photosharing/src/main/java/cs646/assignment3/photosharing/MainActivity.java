package cs646.assignment3.photosharing;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        AsyncTask<String, Integer, Message> courseTask =
//                new UserListTask().execute("");

        // create list of users
        // pass list to list activity

        // Pass the login a connection.
        Intent login = new Intent(getApplicationContext(), UserListActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putSerializable("connection",connection);
//        login.putExtra("connection", connection);
        RequestQueue rq = Volley.newRequestQueue(this);
        startActivity(login);

    }

    /**
     *
     */
    private class UserListTask extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            // request course list
//            String message = new CourseListMessage();
//            manager.sendMessage(message);
//            Message reply = manager.getMessage();
//
//            return reply;
            return null;
        }

    }

}
