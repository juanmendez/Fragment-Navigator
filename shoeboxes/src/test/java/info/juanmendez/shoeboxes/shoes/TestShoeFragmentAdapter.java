package info.juanmendez.shoeboxes.shoes;

import info.juanmendez.shoeboxes.adapters.ShoeFragmentAdapter;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * our test version of ShoeFragmentAdapter
 */
public class TestShoeFragmentAdapter implements ShoeFragmentAdapter {

    Boolean visible = false;
    String tag;
    int id;

    public TestShoeFragmentAdapter(String tag ){
        this.tag = tag;
    }

    public TestShoeFragmentAdapter(int id) {
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

    @Override
    public void onRotation() {

    }

    @Override
    public void fromChildVisit() {

    }

    @Override
    public void toChildVisit() {

    }
}