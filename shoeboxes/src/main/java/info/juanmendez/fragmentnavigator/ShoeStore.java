package info.juanmendez.fragmentnavigator;

import info.juanmendez.fragmentnavigator.models.ShoeRack;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class ShoeStore {

    private static ShoeRack shoeRack;

    public static ShoeRack getShoeRack() {
        return shoeRack;
    }

    public static void setShoeRack(ShoeRack shoeRack) {
        ShoeStore.shoeRack = shoeRack;
    }
}