package info.juanmendez.fragmentnavigator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.juanmendez.fragmentnavigator.models.NavNode;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class RootNavigator implements NavNode {


    List<NavNode> nodes = new ArrayList<>();

    private BehaviorSubject<String> publishSubject;
    private static RootNavigator rootNavigator = new RootNavigator();

    public static RootNavigator getInstance() {
        return rootNavigator;
    }

    public RootNavigator() {
        publishSubject = BehaviorSubject.create();
    }

    public void request(String requestedTag) {
        publishSubject.onNext( requestedTag );
    }

    public Observable<String> asObservable() {
        return publishSubject.hide();
    }

    /**
     * if it executed the code it will return true.
     * @return
     */
    public boolean onBackNav(){

        return false;
    }


    @Override
    public NavNode applyNodes(NavNode... nodes) {
        this.nodes = new ArrayList<>(Arrays.asList(nodes));

        for( NavNode node: nodes ){
            node.setParent(this);
        }

        return this;
    }

    @Override
    public List<NavNode> getNodes() {
        return this.nodes;
    }

    @Override
    public void setParent(NavNode parentNode) {
    }

    @Override
    public NavNode getParent() {
        return null;
    }

    @Override
    public NavNode search(String tag) {
        NavNode nodeResult;

        for( NavNode node: nodes){
            nodeResult = node.search( tag );

            if( nodeResult != null ){
                return nodeResult;
            }
        }

        return null;
    }

    @Override
    public NavNode search(int id) {
        NavNode nodeResult;

        for( NavNode node: nodes){
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
    public void displayChild(String tag) {
    }

    @Override
    public void displayChild(int id) {

    }

    @Override
    public void displayChild(NavNode node) {}

    @Override
    public void setVisible(Boolean show) {

    }

    @Override
    public boolean getVisible() {
        return false;
    }

    @Override
    public boolean goBack() {
        return false;
    }
}