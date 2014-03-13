package cs646.assignment3.photosharing.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Drem on 3/12/14.
 */
public class JsonParser {

    public static <T> Collection<T> parse(JSONArray jsonArray, Class<T> clazz) {
        List<T> resultList = new ArrayList<T>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                T resultObj = parse(obj, clazz);
                if (obj == null) {
                    continue;
                } else {
                    resultList.add(resultObj);
                }
            } catch (Exception e) {
                Log.e("JsonParser.parse", "Something bad happened parsing json array.", e);
                return null;
            }
        }
        return resultList;
    }

    public static <T> T parse(JSONObject jsonObject, Class<T> clazz) {
        try {
            T resultObj = clazz.newInstance();
            Iterator it = jsonObject.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                for (Method method : clazz.getMethods()) {
                    if ((method.getName().startsWith("set")) && (method.getName().length() == (key.length() + 3)))
                    {
                        if (method.getName().toLowerCase().endsWith(key.toLowerCase()))
                        {
                            method.invoke(resultObj, jsonObject.getString(key));
                        }
                    }
                }
            }
            return resultObj;
        }catch (Exception e) {
            Log.e("JsonParser.parse", "Something bad happened parsing json object.", e);
            return null;
        }
    }
}
