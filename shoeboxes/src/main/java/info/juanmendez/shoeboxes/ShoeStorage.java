package info.juanmendez.shoeboxes;

import java.util.HashMap;

import info.juanmendez.shoeboxes.models.ShoeRack;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class ShoeStorage {

    private static String latestTag;
    private static HashMap<String, ShoeRack> hashShoeRack = new HashMap<>();

    public static ShoeRack getLatestRack() {
        return hashShoeRack.get( latestTag );
    }

    public static ShoeRack setTag(String tag ) {

        latestTag = tag;

        if( hashShoeRack.get( tag ) == null ){
            hashShoeRack.put( tag, new ShoeRack() );
        }

        return hashShoeRack.get( tag );
    }

    public static void clear( String tag ){
        hashShoeRack.remove( tag );
    }
}