package info.juanmendez.fragmentnavigator.models;

import java.util.List;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface ShoeModel {
    ShoeModel applyNodes(ShoeModel... nodes);
    List<ShoeModel> getNodes();


    //connect with parent
    void setParent( ShoeModel parentNode );
    ShoeModel getParent();

    //search nodes by tag or id
    ShoeModel search(String tag);
    ShoeModel search(int id );

    void clear();

    //allow self node to display or hide
    void setActive(Boolean active);
    boolean isActive();
}
