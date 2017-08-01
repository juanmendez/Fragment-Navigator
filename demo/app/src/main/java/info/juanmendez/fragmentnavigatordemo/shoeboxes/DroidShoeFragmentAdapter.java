package info.juanmendez.fragmentnavigatordemo.shoeboxes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import info.juanmendez.shoeboxes.adapters.ShoeFragmentAdapter;


/**
 * Created by Juan Mendez on 6/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * You can make your own adapters. The term active can mean visibility, ducking to the bottom.
 * Through setActive, you can also apply transitions. You can have more than one adapter.
 */
public class DroidShoeFragmentAdapter implements ShoeFragmentAdapter {

    Fragment fragment;
    boolean active;

    public DroidShoeFragmentAdapter(Fragment fragment){
        this.fragment = fragment;
    }

    @Override
    public String getTag() {
        return fragment.getTag();
    }

    @Override
    public int getId() {
        return fragment.getId();
    }

    @Override
    public void setActive(Boolean active) {

        FragmentManager fm = fragment.getFragmentManager();
        this.active = active;

        if( fm != null ){
            FragmentTransaction ft = fm.beginTransaction();

            if( active ){
                ft.show( fragment );
            }else{
                ft.hide( fragment );
            }

            ft.commit();
        }
    }

    @Override
    public boolean isActive() {

        return active;
    }

    @Override
    public void onRotation() {

    }
}