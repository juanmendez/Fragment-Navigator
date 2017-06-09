package info.juanmendez.fragmentnavigator.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.juanmendez.fragmentnavigator.utils.NavUtils;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;


/**
 * Created by Juan Mendez on 6/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class NavRoot implements NavNode {

    Boolean visible = false;
    List<NavNode> nodes = new ArrayList<>();

    private BehaviorSubject<List<NavNode>> publishSubject = BehaviorSubject.create();

    public void request(String requestedTag) {
        request(search( requestedTag ));
    }

    public void request(int id) {
        request(search( id ));
    }

    public void request( NavNode navNode ){
        if( navNode != null ){
            publishSubject.onNext(NavUtils.getPathToNode(navNode));
        }
    }

    public Observable<List<NavNode>> asObservable() {
        return publishSubject.hide();
    }

    @Override
    public NavNode applyNodes(NavNode... nodes) {
        this.nodes = new ArrayList<>(Arrays.asList(nodes));

        for( NavNode node: nodes ){
            node.setParent(this);
            node.setVisible(true);
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
    public void setVisible(Boolean show) {
        visible = show;
    }

    @Override
    public boolean getVisible() {
        return visible;
    }

    @Override
    public boolean goBack() {
        return false;
    }
}
