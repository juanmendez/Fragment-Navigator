package info.juanmendez.fragmentnavigator.models;

import java.util.HashMap;

import info.juanmendez.fragmentnavigator.simulators.NavFragmentManager;
import info.juanmendez.fragmentnavigator.simulators.NavFragment;


/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestNavFragmentManager implements NavFragmentManager {

    HashMap<String, NavFragment> hashTag = new HashMap<>();
    HashMap<Integer, NavFragment> hashId = new HashMap<>();

    public void add( String tag, NavFragment fragment ){
        hashTag.put( tag, fragment );
    }

    public void remove( String tag ){
        hashTag.remove( tag );
    }


    public void add( int id, NavFragment fragment ){
        hashId.put( id, fragment );
    }

    public void remove( int id ){
        hashId.remove( id );
    }


    @Override
    public NavFragment findFragmentByTag(String tag) {
        return hashTag.get(tag);
    }

    @Override
    public NavFragment findFragmentById(int id) {
        return hashId.get(id);
    }
}
