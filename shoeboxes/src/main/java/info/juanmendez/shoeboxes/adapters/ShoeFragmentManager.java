package info.juanmendez.shoeboxes.adapters;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface ShoeFragmentManager {

    ShoeFragment findFragment(String tag );
    ShoeFragment findFragment(int id);
}
