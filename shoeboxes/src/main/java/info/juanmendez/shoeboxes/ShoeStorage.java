package info.juanmendez.shoeboxes;

import info.juanmendez.shoeboxes.models.ShoeRack;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class ShoeStorage {

    private static ShoeRack shoeRack;

    public static ShoeRack getShoeRack() {
        return shoeRack;
    }

    public static void setShoeRack(ShoeRack shoeRack) {
        ShoeStorage.shoeRack = shoeRack;
    }
}