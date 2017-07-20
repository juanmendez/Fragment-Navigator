package info.juanmendez.fragmentnavigatordemo.shoeboxes;

import android.support.v4.app.Fragment;

import info.juanmendez.shoeboxes.shoes.ShoeBox;

/**
 * Created by Juan Mendez on 6/15/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class ShoeBuilder {

    /**
     * this is a shortcut to create shoeBoxes from fragments.
     * @param fragment
     * @return
     */
    public static ShoeBox create(Fragment fragment ){
        return ShoeBox.build( new DroidShoeWrapper(fragment));
    }
}