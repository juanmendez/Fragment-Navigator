package info.juanmendez.fragmentnavigator;

import info.juanmendez.fragmentnavigator.models.NavRoot;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class FragmentNav {

    private static NavRoot navRoot;

    public static NavRoot getNavRoot() {
        return navRoot;
    }

    public static void setNavRoot(NavRoot navRoot) {
        FragmentNav.navRoot = navRoot;
    }
}