package info.juanmendez.fragmentnavigator;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.juanmendez.fragmentnavigator.adapters.ShoeFragment;
import info.juanmendez.fragmentnavigator.models.ShoeBox;
import info.juanmendez.fragmentnavigator.models.ShoeModel;
import info.juanmendez.fragmentnavigator.models.ShoeStack;
import info.juanmendez.fragmentnavigator.models.ShoeContainer;
import info.juanmendez.fragmentnavigator.models.TestShoeFragment;
import info.juanmendez.fragmentnavigator.models.TestShoeFragmentManager;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class BasicShoeStoreTest {

    String tagA = "fragmentA";
    String tagB = "fragmentB";
    String tagC = "fragmentC";
    String tagD = "fragmentD";
    String tagE = "fragmentE";
    String tagF = "fragmentF";

    TestShoeFragmentManager fragmentManagerShadow;
    ShoeFragment fragmentA;
    ShoeFragment fragmentB;
    ShoeFragment fragmentC;
    ShoeFragment fragmentD;
    ShoeFragment fragmentE;
    ShoeFragment fragmentF;
    ShoeContainer shoeContainer;

    @Before
    public void before(){
        shoeContainer = new ShoeContainer();
        ShoeStore.setShoeContainer(shoeContainer);
        fragmentA = new TestShoeFragment(tagA);
        fragmentB = new TestShoeFragment(tagB);
        fragmentC = new TestShoeFragment(tagC);
        fragmentD = new TestShoeFragment(tagD);
        fragmentE = new TestShoeFragment(tagE);
        fragmentF = new TestShoeFragment(tagF);
        fragmentManagerShadow = new TestShoeFragmentManager();
    }


    @Test
    public void shouldBuildFragmentNode(){

        ShoeBox right = new ShoeBox(fragmentA);
        ShoeBox left = new ShoeBox(fragmentB);
        ShoeStack root = ShoeStack.build( right, left );
        assertEquals( root.getNodes().size(), 2 );
    }

    @Test
    public void shouldFMWork(){
        fragmentManagerShadow.add( tagA, fragmentA );
        fragmentManagerShadow.add( 0, fragmentB );

        assertEquals( "yes it has tagA", fragmentManagerShadow.findFragment(tagA), fragmentA );
        assertEquals( "yes it has tagB", fragmentManagerShadow.findFragment(0), fragmentB);

        fragmentManagerShadow.remove( tagA );
        fragmentManagerShadow.remove( 0 );

        assertNull( "tagA gone", fragmentManagerShadow.findFragment(tagA) );
        assertNull( "tagB gone", fragmentManagerShadow.findFragment(0) );
    }

    @Test
    public void shouldDispatchRequest(){
        fragmentManagerShadow.add( tagA, fragmentA );
        fragmentManagerShadow.add( tagB, fragmentB );

        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);

        //lets draw the fragments
        shoeContainer.applyNodes(shoeBoxA, shoeBoxB);

        //so we are going to build a dual pane...
        shoeContainer.request( tagA );

        shoeContainer.asObservable().subscribe(navItems -> {
            assertEquals( "tag is A", navItems.get(navItems.size()-1), shoeBoxA);
        });
    }


    @Test
    public void shouldGetParentOfNode(){
        /*ShoeModel parentNode = navigator.getRoot().search( tagA );
        assertEquals( "it's the same root node", parentNode, navigator.getRoot() );*/

        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);


        ShoeBox shoeBoxC = ShoeBox.build(fragmentC);
        ShoeBox shoeBoxD = ShoeBox.build(fragmentD);

        shoeContainer.clear();
        shoeContainer.applyNodes(shoeBoxA, shoeBoxB.applyNodes(shoeBoxC, shoeBoxD) );


        ShoeModel result;
        ShoeModel match = null;

        for( ShoeModel node: shoeContainer.getNodes() ){

            result = node.search( tagC);

            if( result != null ){
                match = result;
            }
        }

        assertNotNull( "not null", match);
    }


    @Test
    public void shouldStackGoForward(){
        shoeContainer.clear();

        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);

        shoeContainer.applyNodes( ShoeStack.build(shoeBoxA, shoeBoxB) );

        //lets go to first one!
        shoeContainer.request( tagA );

        //if its in a stack then only show this fragment!
        assertTrue( "shoeBoxA is visible", shoeBoxA.isActive() );
        assertFalse( "shoeBoxB is invisible", shoeBoxB.isActive() );

        shoeContainer.request( tagB );

        assertFalse( "shoeBoxA is invisible", shoeBoxA.isActive() );
        assertTrue( "shoeBoxB is visibile", shoeBoxB.isActive() );
    }

    @Test
    public void shouldStackGoBack(){
        shoeContainer.clear();

        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);
        ShoeBox shoeBoxC = ShoeBox.build(fragmentC);

        shoeContainer.applyNodes( ShoeStack.build(shoeBoxA, shoeBoxB, shoeBoxC) );

        //lets go to first one!
        shoeContainer.request( tagA );
        shoeContainer.request( tagB );
        shoeContainer.request( tagC );

        //if its in a stack then only show this fragment!
        assertTrue( "shoeBoxA is visible", shoeBoxC.isActive() );

        Boolean wentBack = shoeContainer.goBack();
        assertTrue("able to go back", wentBack );
        assertTrue( "shoeBoxB is visible", shoeBoxB.isActive() );
        assertFalse( "shoeBoxC is invisible", shoeBoxC.isActive() );
        assertFalse( "shoeBoxA is invisible", shoeBoxA.isActive() );

        wentBack = shoeContainer.goBack();
        assertTrue("able to go back", wentBack );
        assertFalse( "shoeBoxB is ivisible", shoeBoxB.isActive() );
        assertFalse( "shoeBoxC is invisible", shoeBoxC.isActive() );
        assertTrue( "shoeBoxA is visible", shoeBoxA.isActive() );

        wentBack = shoeContainer.goBack();
        assertFalse("navigation ended when false", wentBack );
    }

    @Test
    public void slideArrayList(){
        List<String> strings = new ArrayList<>(Arrays.asList(new String[]{"a","b","c","d"}));
        strings = strings.subList(0, 2);
        assertEquals( "last is c", strings.get(strings.size()-1), "b"  );
    }


    @Test
    public void shouldStackOfStacksGoForward(){
        shoeContainer.clear();

        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);
        ShoeBox shoeBoxC = ShoeBox.build(fragmentC);
        ShoeBox shoeBoxD = ShoeBox.build( fragmentD );

        shoeContainer.applyNodes( ShoeStack.build( ShoeStack.build(shoeBoxA, shoeBoxB), ShoeStack.build(shoeBoxC, shoeBoxD)) );

        ShoeStack shoeStack1 = (ShoeStack) shoeContainer.getNodes().get(0).getNodes().get(0);
        ShoeStack shoeStack2 = (ShoeStack) shoeContainer.getNodes().get(0).getNodes().get(1);

        shoeContainer.request( tagA );

        assertTrue( "first stack displayed", shoeStack1.isActive() );
        assertFalse( "second stack hides", shoeStack2.isActive() );


        shoeContainer.request( tagC );

        assertFalse( "first stack hides", shoeStack1.isActive() );
        assertTrue( "second stack displayed", shoeStack2.isActive() );
    }


    @Test
    public void mockingRotatingDevice(){
        //so initially we have a list of fragments, but then they change to a stack..
        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);

        shoeContainer.applyNodes(shoeBoxA, shoeBoxB);

        assertTrue( "visible", shoeBoxA.isActive() );
        assertTrue( "visible", shoeBoxB.isActive() );

        //ok,, now we rotate and we have a stack of fragments
        shoeContainer.applyNodes( ShoeStack.build(shoeBoxA, shoeBoxB));
        shoeContainer.request( tagA );
        assertTrue( "visible", shoeBoxA.isActive() );
        assertFalse( "visible", shoeBoxB.isActive() );

        //lets go to fragmentB
        shoeContainer.request( tagB );

        assertFalse( "visible a", shoeBoxA.isActive() );
        assertTrue( "invisible b", shoeBoxB.isActive() );

        //lets rotate again
        shoeContainer.applyNodes(shoeBoxA, shoeBoxB);

        assertTrue( "visible", shoeBoxA.isActive() );
        assertTrue( "visible", shoeBoxB.isActive() );

        //ok,, now we rotate and we have a stack of fragments
        shoeContainer.applyNodes( ShoeStack.build(shoeBoxA, shoeBoxB));
        shoeContainer.request( tagB );
        assertFalse( "visible", shoeBoxA.isActive() );
        assertTrue( "visible", shoeBoxB.isActive() );

        shoeContainer.goBack();

        assertTrue( "visible", shoeBoxA.isActive() );
        assertFalse( "visible", shoeBoxB.isActive() );

    }

}