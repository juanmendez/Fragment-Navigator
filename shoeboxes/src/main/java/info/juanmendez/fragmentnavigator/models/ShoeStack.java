package info.juanmendez.fragmentnavigator.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.juanmendez.fragmentnavigator.ShoeStore;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * Structure representation of a collection of nodes which visually correspond to a stack of fragments.
 */

public class ShoeStack implements ShoeModel {

    Boolean active = false;
    ShoeModel parentNode;
    List<ShoeModel> nodes = new ArrayList<>();

    public static ShoeStack build(ShoeModel... childNodeArray){
        ShoeStack shoeStack =  new ShoeStack();
        shoeStack.applyNodes( childNodeArray );

        return shoeStack;
    }

    public ShoeStack() {

        ShoeStore.getShoeRack().asObservable().subscribe(navNodes -> {
            int pos = navNodes.indexOf( this );
            int len = navNodes.size();

            if( pos >= 0 && pos < len-1 ){
                ShoeModel childFound = navNodes.get( pos+1);

                for( ShoeModel node: nodes ){
                    node.setActive( node == childFound );
                }
            }
        });

    }

    @Override
    public ShoeModel applyNodes(ShoeModel... nodes) {
        this.nodes = new ArrayList<>(Arrays.asList(nodes));

        //by default first fragment is active
        for( ShoeModel node: nodes ){
            node.setParent(this);
            node.setActive(false);
        }

        return this;
    }

    public List<ShoeModel> getNodes() {
        return nodes;
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
    public ShoeModel search(int id) {
        ShoeModel nodeResult;

        for( ShoeModel node: nodes){
            nodeResult = node.search( id );

            if( nodeResult != null ){
                if( nodeResult instanceof ShoeBox && nodes.contains( nodeResult ) ){
                    return this;
                }else{
                    return nodeResult;
                }
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
        this.active = active;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
