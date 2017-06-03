package info.juanmendez.fragmentnavigator.models;

import java.util.List;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface NavNode {
    NavNode applyNodes(NavNode... nodes);
    List<NavNode> getNodes();

    NavNode search(String tag);
    NavNode search(int id );

    void clear();
}
