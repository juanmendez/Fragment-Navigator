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
 * Represents a flow of fragments having all visible yet one is active.
 */

public class ShoeFlow implements ShoeModel {
    ShoeModel parentNode;
    Boolean active = false;
    List<ShoeModel> nodes = new ArrayList<>();

    public ShoeFlow() {

        ShoeStore.getShoeRack().asObservable().subscribe(navNodes -> {
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
        this.active = active;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
