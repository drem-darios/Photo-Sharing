package cs646.assignment3.photosharing;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import cs646.assignment3.photosharing.api.User;

public class UserListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        Bundle bundle = getIntent().getExtras();
        ArrayList<User> users = (ArrayList<User>)bundle.getSerializable("users");

        setListAdapter(new ArrayAdapter<User>(this,
                android.R.layout.simple_list_item_1, users));
    }
}
