package info.juanmendez.fragmentnavigator.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * This class keeps a hold of all branch nodes. Ideally each activity owns one FragmentTree
 */

public class FragmentTree {
    List<FragmentNode> fragmentNodes = new ArrayList<>();


    public List<FragmentNode> getFragmentNodes() {
        return fragmentNodes;
    }
}
