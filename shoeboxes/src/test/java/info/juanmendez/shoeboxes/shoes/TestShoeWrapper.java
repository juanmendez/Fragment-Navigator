package info.juanmendez.shoeboxes.shoes;

import info.juanmendez.shoeboxes.adapters.ShoeWrapper;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * our test version of ShoeWrapper
 */
public class TestShoeWrapper implements ShoeWrapper {

    Boolean visible = false;
    String tag;
    int id;

    public TestShoeWrapper(String tag ){
        this.tag = tag;
    }

    public TestShoeWrapper(int id) {
        this.id = id;
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