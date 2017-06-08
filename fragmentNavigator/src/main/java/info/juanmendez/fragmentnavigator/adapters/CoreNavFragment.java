package info.juanmendez.fragmentnavigator.adapters;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface CoreNavFragment {
    String getTag();
    int getId();
    void setVisible( Boolean show);
    boolean getVisible();
}
