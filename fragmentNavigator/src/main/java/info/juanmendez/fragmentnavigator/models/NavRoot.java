package info.juanmendez.fragmentnavigator.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Juan Mendez on 6/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class NavRoot implements NavNode {

    Boolean visible = false;
    List<NavNode> nodes = new ArrayList<>();

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
    public NavNode searchParent(String tag) {
        NavNode nodeResult;

        for( NavNode node: nodes){
            nodeResult = node.searchParent( tag );

            if( nodeResult != null ){
                return nodeResult;
            }
        }

        return null;
    }

    @Override
    public NavNode searchParent(int id) {
        NavNode nodeResult;

        for( NavNode node: nodes){
            nodeResult = node.searchParent( id );

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
