package info.juanmendez.fragmentnavigatordemo;

import android.app.Application;

import info.juanmendez.shoeboxes.models.ShoeRack;


/**
 * Created by Juan Mendez on 6/14/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class App extends Application {

    private static ShoeRack shoeRack;

    public static ShoeRack getShoeRack() {
        if( shoeRack == null ){
            shoeRack = new ShoeRack();
        }

        return shoeRack;
    }
}
