package cs646.assignment3.photosharing.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import cs646.assignment3.photosharing.constants.DBConstants;

/**
 * Created by Drem on 3/16/14.
 */
public class DatabaseController extends SQLiteOpenHelper {
    private String usersTable;
    private String userPhotosTable;

    public DatabaseController(Context context) {
        this(context, DBConstants.DEFAULT_DB, DBConstants.DEFAULT_VERSION);
    }
    public DatabaseController(Context context, String dbName, int dbVersion) {
        this(context, dbName, dbVersion, DBConstants.DEFAULT_USERS_TABLE, DBConstants.DEFAULT_USER_PHOTOS_TABLE);
    }

    public DatabaseController(Context context, String dbName, int dbVersion, String usersTable, String userPhotosTable) {
        super(context, dbName, null, dbVersion);
        this.usersTable = usersTable;
        this.userPhotosTable = userPhotosTable;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // create users table
        database.execSQL("CREATE TABLE IF NOT EXISTS " +
                this.usersTable + "(" + DBConstants.ID_KEY + " PRIMARY KEY NOT NULL UNIQUE, "
                + DBConstants.NAME_KEY + ")");

        // create photos table
        database.execSQL("CREATE TABLE IF NOT EXISTS " +
                DBConstants.DEFAULT_USER_PHOTOS_TABLE + "(" + DBConstants.ID_KEY +"  NOT NULL , " +
                DBConstants.USER_ID_KEY + "  NOT NULL , " + DBConstants.NAME_KEY +", " +
                "PRIMARY KEY (" + DBConstants.ID_KEY + " , " + DBConstants.USER_ID_KEY + "), " +
                "FOREIGN KEY("+ DBConstants.USER_ID_KEY +") " +
                "REFERENCES "+ DBConstants.DEFAULT_USERS_TABLE +
                "("+ DBConstants.ID_KEY + "))");

        Log.i("UserDB.onCreate", "Created table: " + this.usersTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int v1, int v2) {
        String query = "DROP TABLE IF EXISTS " + this.usersTable;
        database.execSQL(query);
        query = "DROP TABLE IF EXISTS " + this.userPhotosTable;
        database.execSQL(query);

        onCreate(database);
    }

    public void setUsersTable(String usersTable) {
        this.usersTable = usersTable;
    }

    public String getUsersTable() {
        return usersTable;
    }

    public void setUserPhotosTable(String userPhotosTable) {
        this.userPhotosTable = userPhotosTable;
    }

    public String getUserPhotosTable() {
        return userPhotosTable;
    }
}
