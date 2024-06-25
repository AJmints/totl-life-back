package life.totl.totlback.backpack.helpers;

import java.util.*;

public class MapHelpers {

    public <Long, String> Map<Long, String> getKey(Map<Long, String> map, String value) {

        HashMap<Long, String> keyIDs = new HashMap<>();

        for (Map.Entry<Long, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
//                keyIDs.
            } else {
//                return entry.getValue();
            }
        }
        return null;
    }
}
