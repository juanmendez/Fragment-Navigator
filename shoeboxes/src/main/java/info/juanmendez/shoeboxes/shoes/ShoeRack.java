package info.juanmendez.shoeboxes.shoes;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import info.juanmendez.shoeboxes.utils.ShoeUtils;


/**
 * Created by Juan Mendez on 6/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class ShoeRack extends Observable {

    private ShoeModel shoeModel;
    private List<String> history = new ArrayList<>();
    /*private PublishSubject<List<ShoeModel>> publishSubject = PublishSubject.create();
    private CompositeSubscription compositeSubscription = new CompositeSubscription();*/

    public boolean  request( int requestId ){
        return request( Integer.toString( requestId) );
    }

    public boolean request(String route) {
        ShoeBox shoeBox = search( route );

        if( shoeBox != null ){

            String tag = shoeBox.getFragmentTag();
            /*
            going forward can mean also steping back to a previous shoeModel
            lets find the shoeModel within..
            */
            ShoeModel parent = shoeBox.getParent();

            /**
             * for flow, we need to add in history previous siblings even if they are never visited.
             * this helps when switching to stack, we have in history those other siblings in order to go back.
             */

            //going forward can mean also stepping back to a previous shoeModel
            if( ShoeUtils.isTagInRouteList(tag, history)){

                /**
                 * setting a shoeBox active twice means to refresh
                 */
                String lastRoute = history.get( history.size()-1);

                if( shoeBox.isActive() && ShoeUtils.isTagInRoute( tag, lastRoute ) ){

                    shoeBox.setActive(false);

                    //lets update the route
                    history.set( history.size()-1, route );
                    shoeBox.setActive(true);
                    return true;
                }

                for( int i = history.size()-1; i>= 0; i--){

                    if( !ShoeUtils.isTagInRoute( tag, history.get(i) ) ){
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

                            if( sibling != shoeBox && !ShoeUtils.isTagInRouteList(((ShoeBox) sibling).getFragmentTag(), history) ){
                                history.add( ((ShoeBox) sibling).getFragmentTag() );
                            }else{
                                break;
                            }
                        }

                    }
                }

                history.add(route);
            }

            //lets not update based on a shoeBox which has no shoeFragment.
            if( shoeBox.getShoeFragmentAdapter() == null ){
                return false;
            }

            setChanged();
            notifyObservers( ShoeUtils.getPath(shoeBox ) );
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

        ShoeBox shoeBox;
        ShoeModel shoeParent;
        Boolean anySuccess = false;

        for( String tag: tags ){

            shoeBox = search( tag );

            if( shoeBox != null  ){

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

    public List<String> getHistory() {
        return history;
    }

    /**
     * Android component provides the structure of a new layout.
     * In this way the component restore previous navigation after rotating,
     * or when returning from another component.
     *
     * @param nodes
     * @return
     */
    public ShoeRack populate(ShoeModel... nodes) {

        deleteObservers();
        boolean makeFlow = nodes.length > 1 || (nodes.length == 1 && nodes[0] instanceof ShoeBox );

        if( makeFlow ){
            shoeModel = ShoeFlow.build( nodes );
            shoeModel.setRack( this );
        }else{
            shoeModel = nodes[0];
            shoeModel.setRack( this );
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

    public ShoeBox search(String route) {
        return shoeModel.search( route );
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

            String latestTag = history.get( history.size()-1 );
            String previousTag = history.get( history.size()-2 );
            ShoeBox latestShoeBox = search(latestTag);
            ShoeModel parent = latestShoeBox.getParent();
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

                        if( search(history.get(i)).getParent() != parent ){
                            return request( history.get(i) );
                        }
                    }

                    //it never made the call
                    return false;
                }
                else
                if( grandParent != null ){

                    if( parent instanceof ShoeStack && grandParent instanceof ShoeFlow ){
                        if( ShoeUtils.anyOtherSiblingsInHistory(latestShoeBox)){
                            history.remove( latestTag );
                            return goBack();
                        }
                    }
                }
            }

            return request( previousTag );
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

    /**
     * Activity requests shoeRack to set all shoeModels to be set to inactive
     * because the activity has ended or device is about to rotate
     */
    public void onRotation(){
        shoeModel.onRotation();
    }

    public String getRouteParams(int id) {
        return getRouteParams( Integer.toString(id) );
    }

    public String getRouteParams( String tag ){
        return ShoeUtils.getRouteParams(tag, history);
    }

    public String getRouteParamsOnce( String tag ){
        return ShoeUtils.getRouteParamsOnce( tag, history );
    }
}