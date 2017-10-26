package info.juanmendez.shoeboxes.shoes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.juanmendez.shoeboxes.ShoeStorage;

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
        shoeStack.populate( childNodeArray );

        return shoeStack;
    }

    public ShoeStack() {

        /**
         * we find out if any of the stacks' children is active and so
         * the other get deactivated. There is now a new order.
         * Those which deactivate are notified before the one activated.
         */
        ShoeStorage.getCurrentRack().addObserver( (o, arg) -> {
            List<ShoeModel> navNodes = (List<ShoeModel>) arg;

            int pos = navNodes.indexOf( this );
            boolean isChildInPath;

            if( pos >= 0){
                ShoeModel childFound = navNodes.get( pos+1);

                for( ShoeModel node: nodes ){

                    if( childFound != node && node.isActive() ){
                        node.setActive(false);
                    }
                }

                if( nodes.indexOf(childFound) >= 0 && !childFound.isActive()){
                    childFound.setActive(true);
                }

            }else{
                for( ShoeModel node: nodes ){
                    if( node.isActive() ){
                        node.setActive( false );
                    }
                }
            }
        });
    }

    @Override
    public ShoeModel populate(ShoeModel... nodes) {
        this.nodes = new ArrayList<>(Arrays.asList(nodes));

        //by default first fragment is active
        for( ShoeModel node: nodes ){
            node.setParent(this);
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
    public ShoeBox search(String route) {
        ShoeBox nodeResult;

        for( ShoeModel node: nodes){
            nodeResult = node.search( route );

            if( nodeResult != null ){
                return nodeResult;
            }
        }

        return null;
    }

    @Override
    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void onRotation(){
        for(ShoeModel node: nodes ){
            node.onRotation();
        }
    }
}
