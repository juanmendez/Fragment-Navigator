package info.juanmendez.shoeboxes.models;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.shoeboxes.utils.ShoeUtils;
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

    public boolean  request( int requestId ){
        return request( Integer.toString( requestId) );
    }

    public boolean request(String requestedTag) {
        return request(search( requestedTag ));
    }

    public boolean request( ShoeModel shoeModel){
        if( shoeModel != null ){
            //going forward can mean also steping back to a previous shoeModel

            //lets find the shoeModel within..
            ShoeModel parent = shoeModel.getParent();

            /**
             * for flow, we need to add in history previous siblings even if they are never visited.
             * this helps when switching to stack, we have in history those other siblings in order to go back.
             */

            //going forward can mean also stepping back to a previous shoeModel
            if( history.contains(shoeModel)){

                for( int i = history.size()-1; i>= 0; i--){

                    if( history.get(i) != shoeModel ){
                        history.remove( i );
                    }else{
                        break;
                    }
                }

            }else{

                //if parent is a Flow, then also include previous siblings
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

            //lets not update based on a shoeBox which has no shoeFragment.
            if( shoeModel instanceof ShoeBox  && ((ShoeBox) shoeModel).getShoeFragment() == null ){
                return false;
            }

            publishSubject.onNext(ShoeUtils.getPath(shoeModel));
            return true;
        }

        return false;
    }

    public Observable<List<ShoeModel>> asObservable() {
        return publishSubject.hide();
    }

    public List<ShoeModel> getHistory() {
        return history;
    }


    public ShoeRack populate(ShoeModel... nodes) {

        if( nodes.length > 1 ){
            shoeModel = ShoeFlow.build( nodes );
        }else if( nodes.length == 1 ){
            shoeModel = nodes[0];
        }
        replaceHistory();

        return this;
    }

    /**
     * When an app is rotated shoeModel can update all shoeBox references.
     * This method takes care of updating all history items to work along with the
     * new shoeBoxes.
     */
    private void replaceHistory(){

        ShoeBox prevShoeBox; //old shoeBox reference
        ShoeBox nextShoeBox; //new shoeBox reference


        for( int i = 0; i < history.size(); i++ ){

            if( history.get(i) instanceof ShoeBox ){
                prevShoeBox = (ShoeBox) history.get(i);

                nextShoeBox = (ShoeBox) search( prevShoeBox.getFragmentTag() );

                if( nextShoeBox != null ){
                    history.set( i, nextShoeBox );
                }else{
                    prevShoeBox.setShoeFragment(null);
                }
            }
        }

        //lets make sure to call back again for the last history registered..
        if( !history.isEmpty() ){

            //publishSubject.onNext(ShoeUtils.getPath(history.get(history.size()-1)));
            request( history.get(history.size()-1));
        }
    }

    public ShoeModel search(String tag) {
        return shoeModel.search( tag );
    }

    /**
     * Requeset to go back to the previous sibling before the last in history.
     * Read what happens when the last element is part of a ShoeFlow.
     * @return true if it handled back navigation; otherwise, it returns false.
     */
    public boolean goBack(){

        /**
         * last navNode can have an internal history;therefore,
         * rather than just jumping to the previous element it can
         * return false if it is done with its history.
         */

        //we always go to the element before the last
        Boolean executed = false;
        if( history.size() >= 2 ){

            ShoeModel lastChild = history.get( history.size()-1 );
            ShoeModel previous = history.get( history.size()-2 );
            ShoeModel parent = lastChild.getParent();
            ShoeModel grandParent = parent.getParent();


            /**
             * If currently we are on a ShoeFlow, we need to move out of all other parent siblings
             * As we don't navigate through a ShoeFlow, but only through a ShoeStack.
             *
             * The first child of a stack whose parent is a flow should be skipped.. (second condition)
             * Otherwise we request to go to the element before the last.
             */
            if( parent != null ){

                if( parent instanceof ShoeFlow ){
                    for( int i = history.size()-2; i>= 0; i--){

                        if( history.get(i).getParent() != parent ){
                            return request( history.get(i));
                        }
                    }

                    //it never made the call
                    return false;
                }
                else
                if( grandParent != null ){

                    if( parent instanceof ShoeStack && grandParent instanceof ShoeFlow ){
                        if( ShoeUtils.isLastChildInHistory(lastChild)){
                            history.remove( lastChild );
                            return goBack();
                        }
                    }
                }
            }

            return request( history.get( history.size()-2) );
        }

        return false;
    }

    /**
     * While testing clearning has been convinient to start new tests.
     * This gives flexibility to an application to remove all history .
     */
    public void clearHistory(){
        history.clear();
    }

    public boolean isHistoryEmpty(){
        return history.isEmpty();
    }
}