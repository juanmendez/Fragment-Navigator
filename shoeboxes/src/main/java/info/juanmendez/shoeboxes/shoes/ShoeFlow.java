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
 * Represents a flow of fragments having all visible yet one is active.
 */

public class ShoeFlow implements ShoeModel {

    ShoeModel parentNode;
    Boolean active = false;
    List<ShoeModel> nodes = new ArrayList<>();

    public static ShoeFlow build(ShoeModel... childNodeArray){
        ShoeFlow flow =  new ShoeFlow();
        flow.populate( childNodeArray );
        return flow;
    }


    @Override
    public ShoeModel populate(ShoeModel... nodes) {
        this.nodes = new ArrayList<>(Arrays.asList(nodes));
        return this;
    }

    public List<ShoeModel> getNodes() {
        return this.nodes;
    }

    @Override
    public void setRack(ShoeRack shoeRack) {

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
    public ShoeBox search(String tag) {

        ShoeBox nodeResult;

        for( ShoeModel node: nodes){
            nodeResult = node.search( tag );

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
        int len = navNodes.size();

        if( pos >= 0 && pos < len-1 ){
            for( ShoeModel node: nodes ){
                if( !node.isActive() ){
                    node.setActive( true );
                }
            }
        }else if( !isActive() ){
            for( ShoeModel node: nodes ){
                if( node.isActive() ){
                    node.setActive( false );
                }
            }
        }
    }
}
