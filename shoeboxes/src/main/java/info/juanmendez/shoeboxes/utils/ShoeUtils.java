package info.juanmendez.shoeboxes.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.juanmendez.shoeboxes.ShoeStorage;
import info.juanmendez.shoeboxes.shoes.ShoeBox;
import info.juanmendez.shoeboxes.shoes.ShoeFlow;
import info.juanmendez.shoeboxes.shoes.ShoeModel;
import info.juanmendez.shoeboxes.shoes.ShoeRack;
import info.juanmendez.shoeboxes.shoes.ShoeStack;

/**
 * Created by Juan Mendez on 6/9/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class ShoeUtils {

    public static List<ShoeModel> getPath(ShoeModel shoeModel){

        ArrayList<ShoeModel> nodes = new ArrayList<>();

        nodes.add(shoeModel);

        while (shoeModel.getParent() != null){
            shoeModel = shoeModel.getParent();
            nodes.add(shoeModel);
        }

        Collections.reverse(nodes);
        return nodes;
    }

    public static List<List<ShoeModel>> getPaths(){
        ShoeRack rack = ShoeStorage.getCurrentRack();
        List<String> history = rack.getHistory();
        List<List<ShoeModel>> historyPaths = new ArrayList<>();


        for (String tag: history ){
            historyPaths.add( getPath( rack.search(tag)) );
        }

        return historyPaths;
    }

    /**
     * provides child location within parent's children
      * @param shoeModel
     * @return location, if there is no parent then -1
     */
   public static int getParentIndex(ShoeModel shoeModel ){

       ShoeModel parent = shoeModel.getParent();

       if( parent != null ){
           return parent.getNodes().indexOf(shoeModel);
       }

       return -1;
   }

    /**
     * Check if there are other siblings of ShoeModel which are in history.
     * @param shoeModel
     * @return true if its the last one.
     */
   public static boolean anyOtherSiblingsInHistory(ShoeModel shoeModel ){

       ShoeModel parent = shoeModel.getParent();
       ShoeModel grandParent = parent.getParent();

       if( parent != null && grandParent != null ){

           if( parent instanceof ShoeStack && grandParent instanceof ShoeFlow ){

               Boolean lastInHistory = true;
               List<List<ShoeModel>> historyPaths = getPaths();

               for( ShoeModel childModel:parent.getNodes() ){

                   for( List<ShoeModel> path: historyPaths ){

                       //checking on siblings except shoeModel.
                       if( path.contains(childModel) && childModel != shoeModel ){
                           lastInHistory = false;
                           break;
                       }
                   }
               }

               return lastInHistory;
           }
       }

       return false;
   }


   public static boolean isInLastHistoryRequest(ShoeModel shoeModel ){
       ShoeRack rack = ShoeStorage.getCurrentRack();
       List<String> history = rack.getHistory();

       if( !history.isEmpty() ){
           List<ShoeModel> lastPath = getPath( rack.search(history.get(history.size()-1)) );

           return lastPath.contains(shoeModel);
       }

       return false;
   }

    /**
     * check if parent has any of its children which are shoeBoxes displayed.
     * @param shoeParent
     * @return
     */
    public static Boolean anyChildActive(ShoeModel shoeParent) {

        if( shoeParent != null ){

            for( ShoeModel child: shoeParent.getNodes() ){

                if( child instanceof ShoeBox && child.isActive() ){
                    return true;
                }
            }

        }

        return false;
    }

    private static final String tagRegex = "^([\\w-]+)/?(.*)";
    private static final Pattern tagPattern = Pattern.compile( tagRegex );

    public static Boolean isTagInRoute( String tag, String route ){
        Matcher m = tagPattern.matcher(route);

        if( m.find() ){
            return m.group(1).equals( tag );
        }else{
            return false;
        }
    }

    public static Boolean isTagInRouteList(String tag, List<String> routes ){
        for(String route:routes){
            if( isTagInRoute(tag, route))
                return true;
        }

        return false;
    }

    public static String getRouteParams(String route) {
        Matcher m = tagPattern.matcher(route);

        if( m.find() ){
            return m.group(2);
        }

        return "";
    }

    public static String getRouteParams(String tag, List<String> routes ){
        for(String route:routes){
            if( isTagInRoute(tag, route))
                return getRouteParams(route);
        }

        return "";
    }

    public static String getRouteParamsOnce(String tag, List<String> routes) {

        for(String route:routes){

            if( isTagInRoute(tag, route)){
                routes.set( routes.indexOf( route ), tag );
                return getRouteParams(route);
            }
        }

        return "";
    }
}
