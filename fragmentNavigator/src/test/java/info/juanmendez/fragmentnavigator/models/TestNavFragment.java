package info.juanmendez.fragmentnavigator.models;

import info.juanmendez.fragmentnavigator.simulators.NavFragment;


/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestNavFragment implements NavFragment {

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
}
