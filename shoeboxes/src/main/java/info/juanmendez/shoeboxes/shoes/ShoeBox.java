package info.juanmendez.shoeboxes.shoes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.juanmendez.shoeboxes.ShoeStorage;
import info.juanmendez.shoeboxes.adapters.ShoeFragmentAdapter;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * Think that a Fragment can have other child fragments, well ShoeBox can have other shoeModels
 * but it also can retain one shoeFragmentAdapter.
 */

public class ShoeBox implements ShoeModel {
    ShoeModel parentNode;

    private ShoeFragmentAdapter shoeFragmentAdapter; //identifies Fragment with Tag or its Id
    ShoeModel shoeModel;

    List<ShoeModel> nodes = new ArrayList<>();

    private String fragmentTag;

    public static ShoeBox build(ShoeFragmentAdapter shoeFragmentAdapter){
        return new ShoeBox(shoeFragmentAdapter);
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

    public ShoeBox(ShoeFragmentAdapter shoeFragmentAdapter){
        this();
        this.shoeFragmentAdapter = shoeFragmentAdapter;

        if( shoeFragmentAdapter.getTag() != null ){
            this.fragmentTag = shoeFragmentAdapter.getTag();
        }
        else if( shoeFragmentAdapter.getId() != 0 ){
            this.fragmentTag = Integer.toString( shoeFragmentAdapter.getId() );
        }
    }

    public ShoeFragmentAdapter getShoeFragmentAdapter(){
        return shoeFragmentAdapter;
    }

    public void setShoeFragmentAdapter(ShoeFragmentAdapter shoeFragmentAdapter) {
        this.shoeFragmentAdapter = shoeFragmentAdapter;
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

        shoeFragmentAdapter.setActive(active);
    }

    @Override
    public boolean isActive() {
        return shoeFragmentAdapter.isActive();
    }

    public String getFragmentTag() {
        return fragmentTag;
    }
}