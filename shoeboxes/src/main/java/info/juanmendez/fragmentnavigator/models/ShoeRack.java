package info.juanmendez.fragmentnavigator.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.juanmendez.fragmentnavigator.utils.ShoeUtils;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Juan Mendez on 6/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class ShoeRack implements ShoeModel {

    Boolean active = true;
    List<ShoeModel> nodes = new ArrayList<>();
    List<ShoeModel> history = new ArrayList<>();

    private PublishSubject<List<ShoeModel>> publishSubject = PublishSubject.create();

    public void request(String requestedTag) {
        request(search( requestedTag ));
    }

    public void request(int id) {
        request(search( id ));
    }

    public void request( ShoeModel shoeModel){
        if( shoeModel != null ){

            //going forward can mean also steping back to a previous shoeModel
            if( history.contains(shoeModel)){
                int pos = history.indexOf(shoeModel);
                history = history.subList(0, pos+1);
            }else{
                history.add(shoeModel);
            }

            publishSubject.onNext(ShoeUtils.getPathToNode(shoeModel));
        }
    }

    public Observable<List<ShoeModel>> asObservable() {
        return publishSubject.hide();
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

    @Override
    public List<ShoeModel> getNodes() {
        return this.nodes;
    }

    @Override
    public void setParent(ShoeModel parentNode) {
    }

    @Override
    public ShoeModel getParent() {
        return null;
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

    /**
     * attempt to go back within all different navNodes visited.
     * @return true if it executed.
     */
    public boolean goBack(){

        /**
         * last navNode can have an internal history;therefore,
         * rather than just jumping to the previous element it can
         * return false if it is done with its history.
         */

        //we always go to the element before the last
        if( history.size() >= 2 ){

            request( history.get(history.size()-2));
            return true;
        }

        return false;
    }
}
