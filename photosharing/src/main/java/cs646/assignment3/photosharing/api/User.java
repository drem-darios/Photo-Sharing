package cs646.assignment3.photosharing.api;

import java.io.Serializable;

/**
 * Created by Drem on 3/12/14.
 */
public class User implements Serializable {

    private String id, name;

    public User() {

    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
