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


    //connect with parent
    void setParent( NavNode parentNode );
    NavNode getParent();

    //search nodes by tag or id
    NavNode search(String tag);
    NavNode search(int id );

    void clear();

    //allow self node to display or hide
    void setVisible( Boolean show);
    boolean getVisible();

    boolean goBack();
}
