package info.juanmendez.shoeboxes.shoes;

import java.util.List;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface ShoeModel{
    ShoeModel populate(ShoeModel... nodes);
    List<ShoeModel> getNodes();


    //connect with parent
    void setParent( ShoeModel parentNode );
    ShoeModel getParent();

    //search nodes by tag or id
    ShoeBox search(String route);

    //allow self node to display or hide
    void setActive(Boolean active);
    boolean isActive();

    void onRotation();
}
