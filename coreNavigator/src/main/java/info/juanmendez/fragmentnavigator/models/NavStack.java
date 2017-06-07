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

    Boolean visible = false;
    NavNode parentNode;
    List<NavNode> nodes = new ArrayList<>();
    List<NavNode> history = new ArrayList<>();

    public static NavStack build(NavNode... childNodeArray){
        NavStack navStack =  new NavStack();
        navStack.applyNodes( childNodeArray );

        return navStack;
    }


    @Override
    public NavNode applyNodes(NavNode... nodes) {
        this.nodes = new ArrayList<>(Arrays.asList(nodes));

        //by default first fragment is visible
        for( NavNode node: nodes ){
            node.setParent(this);
            node.setVisible( this.nodes.indexOf(node) == 0 );
        }

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
    public NavNode searchParent(String tag) {
        NavNode nodeResult;

        for( NavNode node: nodes){
            nodeResult = node.searchParent( tag );

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
    public NavNode searchParent(int id) {
        NavNode nodeResult;

        for( NavNode node: nodes){
            nodeResult = node.searchParent( id );

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
        history.clear();
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
                    history.add( navItem );
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
                    history.add( navItem );
                    navItem.setVisible(true);
                }else{
                    navItem.setVisible(false);
                }
            }
        }

    }

    @Override
    public void displayChild(NavNode targetNode) {
        Boolean childFound = false;

        for( NavNode node: nodes){

            if( node == targetNode){
                childFound = true;
                history.add( node );
                node.setVisible(true);
            }else{
                node.setVisible(false);
            }
        }

        setVisible(childFound);
    }

    @Override
    public void setVisible(Boolean show) {
        visible = show;
        parentNode.setVisible(show);
    }

    @Override
    public boolean getVisible() {
        return visible;
    }

    @Override
    public boolean goBack() {

        NavNode nodeDisplayed=null;

        //suppose stack has three fragments
        //and it is now at the third one, but
        //the first two were never navigated
        //then history is based in the order of the list..
        if(history.size() <= 1){
            history.clear();
            for( NavNode node: nodes ){

                history.add( node );
                if( node.getVisible() ){
                    break;
                }

            }
        }

        if( history.size() > 1 ){

            nodeDisplayed = history.get(history.size()-1);
            nodeDisplayed.setVisible(false);

            history.remove( history.size()-1 );
            nodeDisplayed = nodes.get(history.size()-1);
            nodeDisplayed.setVisible(true);
            return true;
        }

        return false;
    }
}
