package cs646.assignment3.photosharing.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cs646.assignment3.photosharing.api.Photo;
import cs646.assignment3.photosharing.constants.DBConstants;

/**
 * Created by Drem on 3/15/14.
 */
public class UserPhotosDB {

    private DatabaseController dbController;
    private String userPhotosTable;
    public UserPhotosDB(DatabaseController dbController) {
        this.dbController = dbController;
        this.userPhotosTable = dbController.getUserPhotosTable();
    }

    public void addPhotos(String userId, List<Photo> photos) {
        SQLiteDatabase database = this.dbController.getWritableDatabase();

        for (Photo photo : photos) {
            ContentValues values = new ContentValues();
            values.put(DBConstants.ID_KEY, photo.getId());
            values.put(DBConstants.NAME_KEY, photo.getName());
            values.put(DBConstants.USER_ID_KEY, userId);

            database.insertWithOnConflict(this.userPhotosTable, null, values,
                    SQLiteDatabase.CONFLICT_REPLACE);
        }
        database.close();
        Log.d("UserPhotosDB.addPhotos", "Added " + photos.size() + " photos for " + userId + " to: " +
                this.userPhotosTable);
    }

    public List<Photo> getPhotos() {
        Cursor cursor = this.dbController.getReadableDatabase().rawQuery("SELECT * FROM " + this.userPhotosTable, null);
        return getPhotos(cursor);
    }

    public List<Photo> getPhotosForUser(String userId) {
        Cursor cursor = this.dbController.getReadableDatabase().rawQuery("SELECT * FROM " +
                this.userPhotosTable + " WHERE user_id = ?", new String[]{userId});
        return getPhotos(cursor);
    }


    private List<Photo> getPhotos(Cursor cursor) {
        List<Photo> photos = new ArrayList<Photo>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            // id, name
            Photo photo = new Photo(cursor.getString(0), cursor.getString(1));
            photos.add(photo);
        }

        return photos;
    }
}
