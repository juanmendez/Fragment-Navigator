package info.juanmendez.shoeboxes.shoes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.juanmendez.shoeboxes.ShoeStorage;
import info.juanmendez.shoeboxes.adapters.ShoeWrapper;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * Wraps a shoeWrapper, and the shoeWrapper wraps an Android Fragment.
 * The shoeWrapper is an adapter. So this class proxies activating and deactivating the fragment.
 */

public class ShoeBox implements ShoeModel {
    ShoeModel parentNode;

    private ShoeWrapper shoeWrapper; //identifies Fragment with Tag or its Id
    ShoeModel shoeModel;

    List<ShoeModel> nodes = new ArrayList<>();

    private String fragmentTag;

    public static ShoeBox build(ShoeWrapper shoeWrapper){
        return new ShoeBox(shoeWrapper);
    }

    public ShoeBox() {

        ShoeStorage.getCurrentRag().asObservable().subscribe(navNodes -> {
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

    public ShoeBox(ShoeWrapper shoeWrapper){
        this();
        this.shoeWrapper = shoeWrapper;

        if( shoeWrapper.getTag() != null ){
            this.fragmentTag = shoeWrapper.getTag();
        }
        else if( shoeWrapper.getId() != 0 ){
            this.fragmentTag = Integer.toString( shoeWrapper.getId() );
        }
    }

    public ShoeWrapper getShoeWrapper(){
        return shoeWrapper;
    }

    public void setShoeWrapper(ShoeWrapper shoeWrapper) {
        this.shoeWrapper = shoeWrapper;
    }

    @Override
    public ShoeModel populate(ShoeModel... nodes) {

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

        if( fragmentTag.equals(tag))
            return this;

        if( shoeModel != null )
            return shoeModel.search( tag );

        return null;
    }

    @Override
    public void setActive(Boolean active) {

        shoeWrapper.setActive(active);
    }

    @Override
    public boolean isActive() {
        return shoeWrapper.isActive();
    }

    public String getFragmentTag() {
        return fragmentTag;
    }
}