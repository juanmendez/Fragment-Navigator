package info.juanmendez.fragmentnavigator.utils;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import info.juanmendez.fragmentnavigator.models.ShoeFlow;
import info.juanmendez.fragmentnavigator.models.ShoeModel;

/**
 * Created by Juan Mendez on 6/9/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class ShoeUtils {

    public static List<ShoeModel> getPathToNode(ShoeModel shoeModel){

        ArrayList<ShoeModel> nodes = new ArrayList<>();

        nodes.add(shoeModel);

        while (shoeModel.getParent() != null){
            shoeModel = shoeModel.getParent();
            nodes.add(shoeModel);
        }

        Collections.reverse(nodes);
        return nodes;
    }

    @NonNull
    public static void updateHistory( ShoeModel shoeModel, List<ShoeModel> history ){
        //lets find the shoeModel within..
        ShoeModel parent = shoeModel.getParent();

        /**
         * for flow, we need to add in history previous siblings even if they are never visited.
         * this helps when switching to stack, we have in history those other siblings in order to go back.
         */

        if( parent != null && parent instanceof ShoeFlow){

            for( ShoeModel sibling: parent.getNodes() ){

                if( sibling != shoeModel && !history.contains( sibling ) ){
                    history.add( sibling );
                }else{
                    break;
                }
            }
        }

        //going forward can mean also steping back to a previous shoeModel
        if( history.contains(shoeModel)){
            int pos = history.indexOf(shoeModel);
            history = history.subList(0, pos+1);
        }else{

            history.add(shoeModel);
        }
    }
}
