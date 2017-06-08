package info.juanmendez.corenavigator.models;

import java.util.HashMap;

import info.juanmendez.corenavigator.adapters.CoreNavFragmentManager;
import info.juanmendez.corenavigator.adapters.CoreNavFragment;


/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestCoreNavFragmentManager implements CoreNavFragmentManager {

    HashMap<String, CoreNavFragment> hashTag = new HashMap<>();
    HashMap<Integer, CoreNavFragment> hashId = new HashMap<>();

    public void add( String tag, CoreNavFragment fragment ){
        hashTag.put( tag, fragment );
    }

    public void remove( String tag ){
        hashTag.remove( tag );
    }


    public void add( int id, CoreNavFragment fragment ){
        hashId.put( id, fragment );
    }

    public void remove( int id ){
        hashId.remove( id );
    }


    @Override
    public CoreNavFragment findFragment(String tag) {
        return hashTag.get(tag);
    }

    @Override
    public CoreNavFragment findFragment(int id) {
        return hashId.get(id);
    }
}
