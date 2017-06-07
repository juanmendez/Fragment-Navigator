package info.juanmendez.fragmentnavigator.models;

import info.juanmendez.fragmentnavigator.adapters.NavFragment;


/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestNavFragment implements NavFragment {

    Boolean visible = false;
    String tag;
    int id;

    public TestNavFragment(String tag ){
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
