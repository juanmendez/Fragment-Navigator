package info.juanmendez.fragmentnavigator;

import info.juanmendez.fragmentnavigator.models.ShoeContainer;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class ShoeStore {

    private static ShoeContainer shoeContainer;

    public static ShoeContainer getShoeContainer() {
        return shoeContainer;
    }

    public static void setShoeContainer(ShoeContainer shoeContainer) {
        ShoeStore.shoeContainer = shoeContainer;
    }
}