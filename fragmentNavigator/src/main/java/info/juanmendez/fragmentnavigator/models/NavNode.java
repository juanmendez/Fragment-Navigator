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

    //search nodes by tag or id
    NavNode search(String tag);
    NavNode search(int id );

    void clear();

    //displayChild a child by its tag, id, or self reference
    void displayChild(String tag);
    void displayChild(int id);
    void displayChild(NavItem node );

    //allow self node to display or hide
    void setVisible( Boolean show);
    boolean getVisible();

    boolean goBack();
}
