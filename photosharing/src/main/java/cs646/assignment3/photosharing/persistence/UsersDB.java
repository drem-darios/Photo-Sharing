package cs646.assignment3.photosharing.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cs646.assignment3.photosharing.api.User;
import cs646.assignment3.photosharing.constants.DBConstants;

/**
 * Created by Drem on 3/15/14.
 */
public class UsersDB {

    private DatabaseController dbController;
    private String usersTable;

    public UsersDB(DatabaseController dbController) {
        this.dbController = dbController;
        this.usersTable = dbController.getUsersTable();
    }

    public void addUsers(List<User> users) {
        SQLiteDatabase db = this.dbController.getWritableDatabase();
        for (User user : users) {
            ContentValues values = new ContentValues();
            values.put(DBConstants.ID_KEY, user.getId());
            values.put(DBConstants.NAME_KEY, user.getName());
            db.insertWithOnConflict(this.usersTable, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
        Log.d("UserDB.addUsers", "Added " + users.size() + " users to: "+ this.usersTable);
    }

    public List<User> getUsers() {
        Cursor cursor = this.dbController.getReadableDatabase().rawQuery("SELECT * FROM " + this.usersTable, null);
        return getUsers(cursor);
    }

    private List<User> getUsers(Cursor cursor) {
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            // id, name
            User user = new User(cursor.getString(0), cursor.getString(1));
            users.add(user);
        }

        return users;
    }
}
