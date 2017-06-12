package info.juanmendez.fragmentnavigator.models;

import java.util.HashMap;

import info.juanmendez.fragmentnavigator.adapters.ShoeFragmentManager;
import info.juanmendez.fragmentnavigator.adapters.ShoeFragment;


/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestShoeFragmentManager implements ShoeFragmentManager {

    HashMap<String, ShoeFragment> hashTag = new HashMap<>();
    HashMap<Integer, ShoeFragment> hashId = new HashMap<>();

    public void add( String tag, ShoeFragment fragment ){
        hashTag.put( tag, fragment );
    }

    public void remove( String tag ){
        hashTag.remove( tag );
    }


    public void add( int id, ShoeFragment fragment ){
        hashId.put( id, fragment );
    }

    public void remove( int id ){
        hashId.remove( id );
    }


    @Override
    public ShoeFragment findFragment(String tag) {
        return hashTag.get(tag);
    }

    @Override
    public ShoeFragment findFragment(int id) {
        return hashId.get(id);
    }
}
