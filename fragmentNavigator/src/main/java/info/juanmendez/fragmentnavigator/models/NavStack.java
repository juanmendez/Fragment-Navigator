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
    List<NavNode> nodes = new ArrayList<>();
    List<NavNode> history = new ArrayList<>();

    Boolean isRoot = false;

    public static NavStack build(NavNode... childNodeArray){
        NavStack navStack =  new NavStack();
        navStack.applyNodes( childNodeArray );

        return navStack;
    }

    @Override
    public NavNode applyNodes(NavNode... nodes) {
        this.nodes = new ArrayList<>(Arrays.asList(nodes));
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
        history.clear();
    }


    @Override
    public void display(String tag) {

        NavItem navItem;
        NavStack navStack;

        for( NavNode node: nodes){

            if( node instanceof NavItem ){
                navItem = (NavItem) node;

               if(  navItem.getNavFragment().getTag().equals(tag) ){
                    history.add(navItem);
                    navItem.getNavFragment().setVisible(true);
               }else{
                   navItem.getNavFragment().setVisible(false);
               }
            }
        }

    }

    @Override
    public void display(int id) {

        NavItem navItem;
        NavStack navStack;

        for( NavNode node: nodes){

            if( node instanceof NavItem ){
                navItem = (NavItem) node;

                if(  navItem.getNavFragment().getId() == id ){
                    history.add(navItem);
                    navItem.getNavFragment().setVisible(true);
                }else{
                    navItem.getNavFragment().setVisible(false);
                }
            }
        }

    }

    @Override
    public void display(NavItem targetNode) {
        NavItem navItem;

        for( NavNode node: nodes){

            if( node instanceof NavItem ){
                navItem = (NavItem) node;

                if(  navItem == targetNode){
                    history.add(navItem);
                    navItem.getNavFragment().setVisible(true);
                }else{
                    navItem.getNavFragment().setVisible(false);
                }
            }
        }
    }

    @Override
    public boolean goBack() {

        NavItem navItem;
        NavStack navStack;
        int lastPos;

        if( history.size() > 1 ){

            lastPos = history.size()-1;

            //make top item invisible
            if( history.get(lastPos) instanceof NavItem ){
                navItem = (NavItem) history.get( history.size()-1);
                navItem.getNavFragment().setVisible(false);
                history.remove(lastPos);
            }


            lastPos = history.size()-1;
            if( history.get(lastPos) instanceof NavItem ){
                navItem = (NavItem) history.get( history.size()-1);
                navItem.getNavFragment().setVisible(true);
            }

            return true;
        }

        return false;
    }
}
