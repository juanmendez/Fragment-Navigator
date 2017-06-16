package info.juanmendez.fragmentnavigatordemo.models;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import info.juanmendez.shoeboxes.adapters.ShoeFragment;


/**
 * Created by Juan Mendez on 6/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class NavFragment implements ShoeFragment {

    Fragment fragment;

    public NavFragment(Fragment fragment){
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

        return fragment.isVisible();
    }
}