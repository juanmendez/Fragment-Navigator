package info.juanmendez.fragmentnavigator.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * Structure representation of a collection of nodes which visually correspond to a stack of fragments.
 */

public class NavStack implements NavNode {

    NavNode parentNode;
    List<NavNode> nodes = new ArrayList<>();

    Boolean isRoot = false;

    public static NavStack build(NavNode... childNodeArray){
        NavStack navStack =  new NavStack();
        navStack.applyNodes( childNodeArray );

        return navStack;
    }

    @Override
    public NavNode applyNodes(NavNode... nodes) {
        this.nodes = new ArrayList<>(Arrays.asList(nodes));

        for( NavNode node: nodes ){
            node.setParent(this);
        }

        return this;
    }

    public NavStack applyRoot(Boolean isRoot ){
        this.isRoot = isRoot;
        return this;
    }

    public List<NavNode> getNodes() {
        return nodes;
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
        NavNode nodeResult;

        for( NavNode node: nodes){
            nodeResult = node.search( tag );

            if( nodeResult != null ){
                if( nodeResult instanceof NavItem && nodes.contains( nodeResult ) ){
                    return this;
                }else{
                    return nodeResult;
                }
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
                if( nodeResult instanceof NavItem && nodes.contains( nodeResult ) ){
                    return this;
                }else{
                    return nodeResult;
                }
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

        NavItem navItem;
        NavStack navStack;

        for( NavNode node: nodes){

            if( node instanceof NavItem ){
                navItem = (NavItem) node;

               if(  navItem.getNavFragment().getTag().equals(tag) ){
                    navItem.setVisible(true);
                   parentNode.displayChild(this);
               }else{
                   navItem.setVisible(false);
               }
            }
        }

    }

    @Override
    public void displayChild(int id) {

        NavItem navItem;
        NavStack navStack;

        for( NavNode node: nodes){

            if( node instanceof NavItem ){
                navItem = (NavItem) node;

                if(  navItem.getNavFragment().getId() == id ){
                    navItem.setVisible(true);
                }else{
                    navItem.setVisible(false);
                }
            }
        }

    }

    @Override
    public void displayChild(NavNode targetNode) {
        NavItem navItem;

        for( NavNode node: nodes){

            if( node == targetNode){
                node.setVisible(true);
                parentNode.displayChild(this);
            }else{
                node.setVisible(false);
            }
        }
    }

    @Override
    public void setVisible(Boolean show) {

    }

    @Override
    public boolean getVisible() {
        return false;
    }

    @Override
    public boolean goBack() {

        NavNode nodeDisplayed=null;
        int lastPos=0;

        //lets find item on display...
        for( NavNode node: nodes ){

            if( node.getVisible() ){
                nodeDisplayed = node;
                lastPos = nodes.indexOf(nodeDisplayed);
                break;
            }
        }

        if( nodeDisplayed != null && lastPos > 0){

            nodeDisplayed.setVisible(false);

            nodeDisplayed = nodes.get(--lastPos);
            nodeDisplayed.setVisible(true);

            return true;
        }

        return false;
    }
}
