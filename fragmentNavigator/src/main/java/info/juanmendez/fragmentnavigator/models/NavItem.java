package info.juanmendez.fragmentnavigator.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.juanmendez.fragmentnavigator.adapters.CoreNavFragment;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * Represents a fragment having layoutNodes as a single branch.
 */

public class NavItem implements NavNode {
    NavNode parentNode;
    private CoreNavFragment coreNavFragment; //identifies Fragment with Tag or its Id
    List<NavNode> nodes = new ArrayList<>();

    public static NavItem build(CoreNavFragment coreNavFragment){
        return new NavItem(coreNavFragment);
    }

    public NavItem(CoreNavFragment coreNavFragment){
        this.coreNavFragment = coreNavFragment;
    }

    public CoreNavFragment getCoreNavFragment(){
        return coreNavFragment;
    }

    @Override
    public NavNode applyNodes(NavNode... nodes) {
        this.nodes = new ArrayList<>(Arrays.asList(nodes));

        for( NavNode node: nodes ){
            node.setParent(this);
        }

        return this;
    }

    public List<NavNode> getNodes() {
        return this.nodes;
    }

    @Override
    public void setParent(NavNode parentNode) {
        this.parentNode = parentNode;
    }

    @Override
    public NavNode getParent() {
        return parentNode;
    }

    @Override
    public NavNode search(String tag) {

        if( coreNavFragment.getTag().equals(tag))
            return this;

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

        if( coreNavFragment.getId()==id)
            return this;

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
        coreNavFragment.setVisible(show);
    }

    @Override
    public boolean getVisible() {
        return coreNavFragment.getVisible();
    }

    @Override
    public boolean goBack() {
        return false;
    }
}
