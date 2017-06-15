package info.juanmendez.fragmentnavigatordemo.models;

import android.support.v4.app.FragmentManager;

import java.util.HashMap;

import info.juanmendez.shoeboxes.adapters.ShoeFragment;
import info.juanmendez.shoeboxes.adapters.ShoeFragmentManager;


/**
 * Created by Juan Mendez on 6/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class NavFragmentManager implements ShoeFragmentManager {

    HashMap<String, ShoeFragment> hashTag = new HashMap<>();
    HashMap<Integer, ShoeFragment> hashId = new HashMap<>();
    FragmentManager fragmentManager;

    public NavFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

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