package info.juanmendez.fragmentnavigator.models;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.fragmentnavigator.utils.ShoeUtils;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Juan Mendez on 6/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class ShoeRack {

    ShoeModel shoeModel;
    List<ShoeModel> history = new ArrayList<>();

    private PublishSubject<List<ShoeModel>> publishSubject = PublishSubject.create();

    public void request( int requestId ){
        request( Integer.toString( requestId) );
    }

    public void request(String requestedTag) {
        request(search( requestedTag ));
    }

    public void request( ShoeModel shoeModel){
        if( shoeModel != null ){
            //going forward can mean also steping back to a previous shoeModel

            //lets find the shoeModel within..
            ShoeModel parent = shoeModel.getParent();

            /**
             * for flow, we need to add in history previous siblings even if they are never visited.
             * this helps when switching to stack, we have in history those other siblings in order to go back.
             */

            //going forward can mean also steping back to a previous shoeModel
            if( history.contains(shoeModel)){
                int pos = history.indexOf(shoeModel);
                history = history.subList(0, pos+1);
                ShoeModel sibling;

                //we have to move all the way out of all sibling if the parent is a ShoeFlow.
                if( parent != null && parent instanceof ShoeFlow ){
                    for( int i = history.size()-1; i >= 0; i-- ){
                        sibling = history.get(i);
                        if( sibling.getParent() == parent ){
                            history.remove( history.size()-1);
                        }
                    }
                }

            }else{

                if( parent != null && parent instanceof ShoeFlow){
                    for( ShoeModel sibling: parent.getNodes() ){
                        if( sibling != shoeModel && !history.contains( sibling ) ){
                            history.add( sibling );
                        }else{
                            break;
                        }
                    }
                }

                history.add(shoeModel);
            }


            publishSubject.onNext(ShoeUtils.getPathToNode(shoeModel));
        }
    }

    public Observable<List<ShoeModel>> asObservable() {
        return publishSubject.hide();
    }

    public List<ShoeModel> getHistory() {
        return history;
    }


    public ShoeRack applyNodes(ShoeModel... nodes) {

        if( nodes.length > 1 ){
            shoeModel = ShoeFlow.build( nodes );
        }else if( nodes.length == 1 ){
            shoeModel = nodes[0];
        }

        return this;
    }

    public ShoeModel getShoeModel() {
        return shoeModel;
    }

    public ShoeModel search(String tag) {
        return shoeModel.search( tag );
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

            ShoeModel current = history.get( history.size()-1 );
            ShoeModel parent = current.getParent();

            /**
             * if previous siblings have the same parent, and the parent is a ShoeFlow, then
             * we want to skip them and go to the one who no longer has that parent.
             */
            if( parent != null && parent instanceof ShoeFlow ){
                for( int i = history.size()-2; i>= 0; i--){
                    current = history.get(i);

                    if( current.getParent() != parent ){
                        request( history.get(i) );
                        return true;
                    }

                    return false;
                }
            }else{
                request( history.get(history.size()-2));
            }

            return true;
        }

        return false;
    }

    public void clearHistory(){
        history.clear();
    }
}
