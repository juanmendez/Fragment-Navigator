package info.juanmendez.shoeboxes.shoes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    HashMap<String, String> requestActionMap = new HashMap<>();

    List<String> history = new ArrayList<>();

    private PublishSubject<List<ShoeModel>> publishSubject = PublishSubject.create();

    public boolean  request( int requestId ){
        return request( requestId, "" );
    }

    public boolean request( int requestId, String action ){
        return request( Integer.toString( requestId), action );
    }

    public boolean request(String requestedTag) {
        return request( requestedTag, "");
    }

    public boolean request(String requestedTag, String action ) {

        /**
         * keep a reference of the action based on the tag in the shoeBox
         */
        ShoeBox shoeBox = (ShoeBox) search( requestedTag );

        if( action == null || action.isEmpty() ){
            requestActionMap.remove( shoeBox.getFragmentTag() );
        }else{
            requestActionMap.put( shoeBox.getFragmentTag(), action );
        }

        return request( shoeBox );
    }

    public boolean request( ShoeBox thisShoeBox ){
        if( thisShoeBox != null ){

            String shoeTag = thisShoeBox.getFragmentTag();
            /*
            going forward can mean also steping back to a previous shoeModel
            lets find the shoeModel within..
            */
            ShoeModel parent = thisShoeBox.getParent();

            /**
             * for flow, we need to add in history previous siblings even if they are never visited.
             * this helps when switching to stack, we have in history those other siblings in order to go back.
             */

            //going forward can mean also stepping back to a previous shoeModel
            if( history.contains(shoeTag)){

                for( int i = history.size()-1; i>= 0; i--){

                    if( !history.get(i).equals( shoeTag ) ){
                        history.remove( i );
                    }else{
                        break;
                    }
                }

            }else{

                //if parent is a Flow, then also include previous siblings
                if( parent != null && parent instanceof ShoeFlow){

                    for( ShoeModel sibling: parent.getNodes() ){
                        if( sibling instanceof ShoeBox ){

                            if( sibling != thisShoeBox && !history.contains( ((ShoeBox) sibling).getFragmentTag() ) ){
                                history.add( ((ShoeBox) sibling).getFragmentTag() );
                            }else{
                                break;
                            }
                        }

                    }
                }

                history.add(shoeTag);
            }

            //lets not update based on a shoeBox which has no shoeFragment.
            if( thisShoeBox instanceof ShoeBox  && thisShoeBox.getShoeFragmentAdapter() == null ){
                return false;
            }

            publishSubject.onNext(ShoeUtils.getPath(thisShoeBox));
            return true;
        }

        return false;
    }

    /**
     * try to include children in their parents, if there aren't any.
     * @param ids
     */
    public boolean suggest( int... ids ){
        String[] stringIds = new String[ids.length];

        for( int i = 0; i < ids.length; i++ ){
            stringIds[i] = Integer.toString( ids[i] );
        }

        return suggest( stringIds );
    }

    /**
     * try to include children in their parents, if there aren't any.
     * @param tags
     * @return  true if any of the tag was successfully executed.
     */
    public boolean suggest (String... tags ){

        ShoeModel shoeModel, shoeParent;
        ShoeBox shoeBox;
        Boolean anySuccess = false;

        for( String tag: tags ){

            shoeModel = search( tag );

            if( shoeModel != null && shoeModel instanceof ShoeBox ){
                shoeBox = (ShoeBox) shoeModel;
                shoeParent = shoeBox.getParent();

                if( !ShoeUtils.anyChildActive( shoeParent ) ){
                    if( request( tag ) ){
                        anySuccess = true;
                    }
                }
            }
        }

        return anySuccess;
    }

    public boolean suggestIdsWithActions(HashMap<Integer, String> idsWithActions ){

        HashMap<String, String> tagsWithActions = new HashMap<>();

        for (Map.Entry<Integer, String> entry : idsWithActions.entrySet()) {
            tagsWithActions.put( Integer.toString(entry.getKey()), entry.getValue() );
        }

        return suggest( tagsWithActions );
    }

    public boolean suggest( HashMap<String,String> tagsWithActions ){

        ShoeModel shoeModel, shoeParent;
        ShoeBox shoeBox;
        Boolean anySuccess = false;

        for (Map.Entry<String, String> entry : tagsWithActions.entrySet()) {

            shoeModel = search( entry.getKey() );

            if( shoeModel != null && shoeModel instanceof ShoeBox ){
                shoeBox = (ShoeBox) shoeModel;
                shoeParent = shoeBox.getParent();

                if( !ShoeUtils.anyChildActive( shoeParent ) ){
                    if( request( entry.getKey(), entry.getValue() ) ){
                        anySuccess = true;
                    }
                }
            }
        }

        return  anySuccess;
    }

    public Observable<List<ShoeModel>> asObservable() {
        return publishSubject.hide();
    }

    public List<String> getHistory() {
        return history;
    }

    public ShoeRack populate(ShoeModel... nodes) {

        boolean makeFlow = nodes.length > 1 || (nodes.length == 1 && nodes[0] instanceof ShoeBox );

        if( makeFlow ){
            shoeModel = ShoeFlow.build( nodes );
        }else{
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

            ShoeModel lastChild = search(history.get( history.size()-1 ));
            ShoeModel previous = search(history.get( history.size()-2 ));
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

                        if( search(history.get(i)) != parent ){
                            return request( history.get(i));
                        }
                    }

                    //it never made the call
                    return false;
                }
                else
                if( grandParent != null ){

                    if( parent instanceof ShoeStack && grandParent instanceof ShoeFlow ){
                        if( ShoeUtils.anyOtherSiblingsInHistory(lastChild)){
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
        requestActionMap.clear();
    }


    public String getActionByTag( String tag ){
        return requestActionMap.get( tag );
    }

    public String getActionById( int id ){
        return getActionByTag( Integer.toString(id));
    }

    public boolean isHistoryEmpty(){
        return history.isEmpty();
    }

    /**
     * Activity requests shoeRack to set all shoeModels to be set to inactive
     * because the activity has ended or device is about to rotate
     */
    public void onRotation(){
        shoeModel.onRotation();
    }
}