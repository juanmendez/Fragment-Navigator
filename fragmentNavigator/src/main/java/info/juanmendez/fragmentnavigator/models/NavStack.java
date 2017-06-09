package info.juanmendez.fragmentnavigator.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.juanmendez.fragmentnavigator.RootNavigator;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * Structure representation of a collection of nodes which visually correspond to a stack of fragments.
 */

public class NavStack implements NavNode {

    Boolean active = false;
    NavNode parentNode;
    List<NavNode> nodes = new ArrayList<>();

    public static NavStack build(NavNode... childNodeArray){
        NavStack navStack =  new NavStack();
        navStack.applyNodes( childNodeArray );

        return navStack;
    }

    public NavStack() {

        RootNavigator.getNavRoot().asObservable().subscribe(navNodes -> {
            int pos = navNodes.indexOf( this );
            int len = navNodes.size();

            if( pos >= 0 && pos < len-1 ){
                NavNode childFound = navNodes.get( pos+1);

                for( NavNode node: nodes ){
                    node.setActive( node == childFound );
                }
            }
        });

    }

    @Override
    public NavNode applyNodes(NavNode... nodes) {
        this.nodes = new ArrayList<>(Arrays.asList(nodes));

        //by default first fragment is active
        for( NavNode node: nodes ){
            node.setParent(this);
            node.setActive( this.nodes.indexOf(node) == 0 );
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
    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
