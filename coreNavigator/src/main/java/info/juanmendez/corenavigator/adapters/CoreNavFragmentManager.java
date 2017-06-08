package info.juanmendez.corenavigator.adapters;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface CoreNavFragmentManager {

    CoreNavFragment findFragment(String tag );
    CoreNavFragment findFragment(int id);
}
