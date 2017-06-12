package info.juanmendez.fragmentnavigator.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.juanmendez.fragmentnavigator.ShoeStorage;
import info.juanmendez.fragmentnavigator.adapters.ShoeFragment;

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
    List<ShoeModel> nodes = new ArrayList<>();

    public static ShoeBox build(ShoeFragment shoeFragment){
        return new ShoeBox(shoeFragment);
    }

    public ShoeBox() {

        ShoeStorage.getShoeRack().asObservable().subscribe(navNodes -> {
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
    }

    public ShoeFragment getShoeFragment(){
        return shoeFragment;
    }

    @Override
    public ShoeModel applyNodes(ShoeModel... nodes) {
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

        ShoeModel nodeResult;

        for( ShoeModel node: nodes){
            nodeResult = node.search( tag );

            if( nodeResult != null ){
                return nodeResult;
            }
        }

        return null;
    }
    
    @Override
    public void clear() {
        nodes.clear();
    }

    @Override
    public void setActive(Boolean active) {
        shoeFragment.setActive(active);
    }

    @Override
    public boolean isActive() {
        return shoeFragment.isActive();
    }
}
