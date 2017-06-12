package info.juanmendez.fragmentnavigator.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import info.juanmendez.fragmentnavigator.models.ShoeModel;

/**
 * Created by Juan Mendez on 6/9/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class ShoeUtils {

    public static List<ShoeModel> getPathToNode(ShoeModel shoeModel){

        ArrayList<ShoeModel> nodes = new ArrayList<>();

        nodes.add(shoeModel);

        while (shoeModel.getParent() != null){
            shoeModel = shoeModel.getParent();
            nodes.add(shoeModel);
        }

        Collections.reverse(nodes);
        return nodes;
    }
}
