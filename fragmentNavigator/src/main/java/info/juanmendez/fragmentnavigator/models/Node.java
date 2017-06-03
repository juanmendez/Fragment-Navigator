package info.juanmendez.fragmentnavigator.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.juanmendez.fragmentnavigator.simulators.NavFragment;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * Represents a fragment having layoutNodes as a single branch.
 */

public class Node implements NavNode {
    private NavFragment navFragment; //identifies Fragment with Tag or its Id
    List<NavNode> nodes = new ArrayList<>();

    public static Node build(NavFragment navFragment){
        return new Node(navFragment);
    }

    public Node(NavFragment navFragment){
        this.navFragment = navFragment;
    }

    public NavFragment getNavFragment(){
        return navFragment;
    }

    @Override
    public NavNode applyNodes(NavNode... nodes) {
        this.nodes = new ArrayList<>(Arrays.asList(nodes));

        return this;
    }

    public List<NavNode> getNodes() {
        return this.nodes;
    }

    @Override
    public NavNode search(String tag) {

        if( navFragment.getTag().equals(tag))
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

        if( navFragment.getId()==id)
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
}
