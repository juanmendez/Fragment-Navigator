package info.juanmendez.fragmentnavigator.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * Structure representation of a collection of nodes which visually correspond to a stack of fragments
 */

public class Stack implements NavNode {
    List<NavNode> nodes = new ArrayList<>();

    public static Stack build(NavNode... childNodeArray){
        Stack stack =  new Stack();
        stack.applyNodes( childNodeArray );

        return stack;
    }

    @Override
    public NavNode applyNodes(NavNode... nodes) {
        this.nodes = new ArrayList<>(Arrays.asList(nodes));
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
                if( nodeResult instanceof Node && nodes.contains( nodeResult ) ){
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
                if( nodeResult instanceof Node && nodes.contains( nodeResult ) ){
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
}
