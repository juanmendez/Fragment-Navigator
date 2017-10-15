package info.juanmendez.shoeboxes.shoes;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.shoeboxes.ShoeStorage;
import info.juanmendez.shoeboxes.adapters.ShoeFragmentAdapter;
import info.juanmendez.shoeboxes.utils.ShoeUtils;

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
    private List<ShoeModel> nodes = new ArrayList<>();
    private ShoeModel shoeModelChildNode;

    //lets keep a hold of the last child visited.
    ShoeModel prevChildVisited;
    Boolean childVisited=false;
    int childVisits = 0;

    private String fragmentTag;

    public static ShoeBox build(ShoeFragmentAdapter shoeFragmentAdapter){
        return new ShoeBox(shoeFragmentAdapter);
    }

    public ShoeBox(ShoeFragmentAdapter shoeFragmentAdapter){

        this.shoeFragmentAdapter = shoeFragmentAdapter;

        if( shoeFragmentAdapter.getTag() != null ){
            this.fragmentTag = shoeFragmentAdapter.getTag();
        }
        else if( shoeFragmentAdapter.getId() != 0 ){
            this.fragmentTag = Integer.toString( shoeFragmentAdapter.getId() );
        }

        //starts with a closed state
        this.shoeFragmentAdapter.setActive(false);

        ShoeStorage.getCurrentRack().subscribe(navNodes -> {

            int pos = navNodes.indexOf( this );

            //this parent must be active for this to work!
            if( pos >= 0 ){

                if( childVisited  ){
                    shoeFragmentAdapter.fromChildVisit();
                }

                childVisited = false;

                for (ShoeModel child: nodes ){
                    if( navNodes.indexOf(child) >= 0 ){
                        childVisited=true;
                        childVisits++;

                        if( childVisits == 1 )
                            shoeFragmentAdapter.toChildVisit();
                        break;
                    }
                }

                if( !childVisited )
                    childVisits = 0;

            }else{
                childVisited = false;
            }
        });
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
            shoeModelChildNode = ShoeFlow.build( shodeModels );
        }else{
            shoeModelChildNode = shodeModels[0];
        }

        if( shoeModelChildNode != null ){
            nodes.clear();
            nodes.add(shoeModelChildNode);

            shoeModelChildNode.setParent(this);
            shoeModelChildNode.setActive( false );
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
    public ShoeBox search(String route) {

        if(ShoeUtils.isTagInRoute(fragmentTag, route)){
            return this;
        }

        if( shoeModelChildNode != null )
            return shoeModelChildNode.search( route );

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

        if( shoeModelChildNode != null ){
            shoeModelChildNode.onRotation();
        }
    }
}