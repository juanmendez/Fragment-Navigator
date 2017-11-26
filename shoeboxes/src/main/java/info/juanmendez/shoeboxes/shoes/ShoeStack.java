package info.juanmendez.shoeboxes.shoes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

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

    @Override
    public ShoeModel populate(ShoeModel... nodes) {
        this.nodes = new ArrayList<>(Arrays.asList(nodes));

        return this;
    }

    public List<ShoeModel> getNodes() {
        return nodes;
    }

    @Override
    public void setRack(ShoeRack shoeRack) {
        //by default first fragment is active
        for( ShoeModel node: nodes ){
            node.setRack( shoeRack );
            node.setParent(this);
        }

        shoeRack.addObserver( this );
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

    @Override
    public void update(Observable o, Object arg) {
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

        }else if( !isActive() ){

            //in case the stack is active, but not in the path, leave it alone.
            for( ShoeModel node: nodes ){
                if( node.isActive() ){
                    node.setActive( false );
                }
            }
        }
    }
}
