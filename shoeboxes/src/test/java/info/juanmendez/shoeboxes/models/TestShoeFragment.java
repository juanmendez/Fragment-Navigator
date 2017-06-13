package info.juanmendez.shoeboxes.models;

import info.juanmendez.shoeboxes.adapters.ShoeFragment;


/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestShoeFragment implements ShoeFragment {

    Boolean visible = false;
    String tag;
    int id;

    public TestShoeFragment(String tag ){
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
