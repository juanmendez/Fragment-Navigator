package info.juanmendez.fragmentnavigatordemo.models;

import android.support.v4.app.FragmentManager;

import java.util.HashMap;

import info.juanmendez.fragmentnavigator.adapters.CoreNavFragment;
import info.juanmendez.fragmentnavigator.adapters.CoreNavFragmentManager;


/**
 * Created by Juan Mendez on 6/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class NavFragmentManager implements CoreNavFragmentManager {

    HashMap<String, CoreNavFragment> hashTag = new HashMap<>();
    HashMap<Integer, CoreNavFragment> hashId = new HashMap<>();
    FragmentManager fragmentManager;

    public NavFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

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