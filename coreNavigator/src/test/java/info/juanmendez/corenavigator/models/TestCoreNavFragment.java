package info.juanmendez.corenavigator.models;

import info.juanmendez.corenavigator.adapters.CoreNavFragment;


/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestCoreNavFragment implements CoreNavFragment {

    Boolean visible = false;
    String tag;
    int id;

    public TestCoreNavFragment(String tag ){
        this.tag = tag;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public int getId(){
        return id;
    }

    @Override
    public void setVisible(Boolean show) {
        visible = show;
    }

    @Override
    public boolean getVisible() {
        return visible;
    }


}
