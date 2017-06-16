package info.juanmendez.fragmentnavigatordemo;

import android.support.v4.app.Fragment;

import info.juanmendez.fragmentnavigatordemo.models.NavFragment;
import info.juanmendez.shoeboxes.models.ShoeBox;

/**
 * Created by Juan Mendez on 6/15/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class ShoeBuilder {

    public static ShoeBox create(Fragment fragment ){
        return ShoeBox.build( new NavFragment(fragment));
    }
}