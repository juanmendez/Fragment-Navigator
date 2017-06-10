package info.juanmendez.fragmentnavigator.models;

import info.juanmendez.fragmentnavigator.adapters.CoreNavFragment;


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
    public void setActive(Boolean show) {
        visible = show;
    }

    @Override
    public boolean isActive() {
        return visible;
    }


}
