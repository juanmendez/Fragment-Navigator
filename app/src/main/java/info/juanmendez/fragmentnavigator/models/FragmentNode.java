package info.juanmendez.fragmentnavigator.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 * This is a representation of a fragment and can keep a hold of child fragmentNodes
 */

public class FragmentNode {
    private String id; //identifies Fragment with Tag or its Id
    List<FragmentNode> childNodeList = new ArrayList<>();
}
