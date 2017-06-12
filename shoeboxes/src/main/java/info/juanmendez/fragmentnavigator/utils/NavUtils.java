package info.juanmendez.fragmentnavigator.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import info.juanmendez.fragmentnavigator.models.NavNode;

/**
 * Created by Juan Mendez on 6/9/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class NavUtils {

    public static List<NavNode> getPathToNode( NavNode navNode ){

        ArrayList<NavNode> nodes = new ArrayList<>();

        nodes.add( navNode );

        while (navNode.getParent() != null){
            navNode = navNode.getParent();
            nodes.add( navNode );
        }

        Collections.reverse(nodes);
        return nodes;
    }
}
