package info.juanmendez.shoeboxes.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.juanmendez.shoeboxes.ShoeStorage;
import info.juanmendez.shoeboxes.adapters.ShoeFragment;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * Represents a fragment having layoutNodes as a single branch.
 */

public class ShoeBox implements ShoeModel {
    ShoeModel parentNode;
    private ShoeFragment shoeFragment; //identifies Fragment with Tag or its Id
    ShoeModel shoeModel;
    List<ShoeModel> nodes = new ArrayList<>();

    private String fragmentTag;

    public static ShoeBox build(ShoeFragment shoeFragment){
        return new ShoeBox(shoeFragment);
    }

    public ShoeBox() {

        ShoeStorage.getLatestRack().asObservable().subscribe(navNodes -> {
            int pos = navNodes.indexOf( this );
            int len = navNodes.size();

            if( pos >= 0 && pos < len-1 ){
                for( ShoeModel node: nodes ){
                    node.setActive( true );
                }
            }else{
                for( ShoeModel node: nodes ){
                    node.setActive( false );
                }
            }
        });
    }

    public ShoeBox(ShoeFragment shoeFragment){
        this();
        this.shoeFragment = shoeFragment;

        if( shoeFragment.getTag() != null ){
            this.fragmentTag = shoeFragment.getTag();
        }
        else if( shoeFragment.getId() != 0 ){
            this.fragmentTag = Integer.toString( shoeFragment.getId() );
        }
    }

    public ShoeFragment getShoeFragment(){
        return shoeFragment;
    }

    public void setShoeFragment( ShoeFragment shoeFragment) {
        this.shoeFragment = shoeFragment;
    }

    @Override
    public ShoeModel applyNodes(ShoeModel... nodes) {

        if( nodes.length > 1 ){
            shoeModel = ShoeFlow.build( nodes );
        }else if( nodes.length == 1){
            shoeModel = nodes[0];
        }


        this.nodes = new ArrayList<>(Arrays.asList(nodes));

        for( ShoeModel node: nodes ){
            node.setParent(this);
            node.setActive(true);
        }

        return this;
    }

    public List<ShoeModel> getNodes() {
        return this.nodes;
    }

    @Override
    public void setParent(ShoeModel parentNode) {
        this.parentNode = parentNode;
    }

    @Override
    public ShoeModel getParent() {
        return parentNode;
    }

    @Override
    public ShoeModel search(String tag) {

        if( shoeFragment.getTag().equals(tag))
            return this;

        if( shoeModel != null )
            return shoeModel.search( tag );

        return null;
    }

    @Override
    public void setActive(Boolean active) {
        shoeFragment.setActive(active);
    }

    @Override
    public boolean isActive() {
        return shoeFragment.isActive();
    }

    public String getFragmentTag() {
        return fragmentTag;
    }
}
