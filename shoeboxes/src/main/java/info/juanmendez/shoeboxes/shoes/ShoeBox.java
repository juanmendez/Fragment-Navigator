package info.juanmendez.shoeboxes.shoes;

import java.util.ArrayList;
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
    List<ShoeModel> nodes = new ArrayList<>();
    ShoeModel shoeModel;

    //lets keep a hold of the last child visited.
    ShoeModel prevChildVisited;

    private String fragmentTag;

    public static ShoeBox build(ShoeFragmentAdapter shoeFragmentAdapter){
        return new ShoeBox(shoeFragmentAdapter);
    }

    public ShoeBox() {

        ShoeStorage.getCurrentRack().asObservable().subscribe(navNodes -> {

            int pos = navNodes.indexOf( this );
            setActive( pos >= 0 );

            //this parent must be active for this to work!
            if( pos >= 0 ){
                if( prevChildVisited != null ){
                    shoeFragmentAdapter.returnFromChildVisit();
                }

                prevChildVisited = null;
                for (ShoeModel child: nodes ){
                    if( navNodes.indexOf(child) >= 0 ){
                        prevChildVisited = child;
                        break;
                    }
                }
            }else{
                prevChildVisited = null;
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
    public ShoeModel populate(ShoeModel... shodeModels) {

        boolean makeFlow = shodeModels.length > 1 || (shodeModels.length == 1 && shodeModels[0] instanceof ShoeBox );

        if( makeFlow ){
            shoeModel = ShoeFlow.build( shodeModels );
        }else{
            shoeModel = shodeModels[0];
        }

        if( shoeModel != null ){
            this.nodes.clear();
            this.nodes.add(shoeModel);

            shoeModel.setParent(this);
            shoeModel.setActive( false );
        }


        return this;
    }

    @Override
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

    @Override
    public void onRotation(){
        if( shoeFragmentAdapter != null ){
            shoeFragmentAdapter.onRotation();
        }

        if( shoeModel != null ){
            shoeModel.onRotation();
        }
    }
}