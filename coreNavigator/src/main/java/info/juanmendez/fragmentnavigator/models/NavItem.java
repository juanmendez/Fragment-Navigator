package info.juanmendez.fragmentnavigator.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.juanmendez.fragmentnavigator.adapters.NavFragment;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * Represents a fragment having layoutNodes as a single branch.
 */

public class NavItem implements NavNode {
    NavNode parentNode;
    private NavFragment navFragment; //identifies Fragment with Tag or its Id
    List<NavNode> nodes = new ArrayList<>();

    public static NavItem build(NavFragment navFragment){
        return new NavItem(navFragment);
    }

    public NavItem(NavFragment navFragment){
        this.navFragment = navFragment;
    }

    public NavFragment getNavFragment(){
        return navFragment;
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
    public NavNode searchParent(String tag) {

        if( navFragment.getTag().equals(tag))
            return this;

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

        if( navFragment.getId()==id)
            return this;

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
    public void displayChild(NavNode node) {
    }

    @Override
    public void setVisible(Boolean show) {
        navFragment.setVisible(show);
    }

    @Override
    public boolean getVisible() {
        return navFragment.getVisible();
    }

    @Override
    public boolean goBack() {
        return false;
    }
}
