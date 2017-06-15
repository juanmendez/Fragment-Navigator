package info.juanmendez.shoeboxes;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.juanmendez.shoeboxes.adapters.ShoeFragment;
import info.juanmendez.shoeboxes.models.ShoeBox;
import info.juanmendez.shoeboxes.models.ShoeFlow;
import info.juanmendez.shoeboxes.models.ShoeModel;
import info.juanmendez.shoeboxes.models.ShoeStack;
import info.juanmendez.shoeboxes.models.ShoeRack;
import info.juanmendez.shoeboxes.models.TestShoeFragment;
import info.juanmendez.shoeboxes.models.TestShoeFragmentManager;

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

public class BasicShoeStorageTest {

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
    ShoeRack shoeRack;

    @Before
    public void before(){
        shoeRack = new ShoeRack();
        ShoeStorage.setShoeRack(shoeRack);
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
        shoeRack.applyNodes(shoeBoxA, shoeBoxB);

        //so we are going to build a dual pane...
        shoeRack.request( tagA );

        shoeRack.asObservable().subscribe(navItems -> {
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

        
        shoeRack.applyNodes(shoeBoxA, shoeBoxB.applyNodes(shoeBoxC, shoeBoxD ));


        ShoeModel result;
        ShoeModel match = shoeRack.search( tagC );

        assertNotNull( "not null", match);
    }


    @Test
    public void shouldStackGoForward(){
        

        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);

        shoeRack.applyNodes( ShoeStack.build(shoeBoxA, shoeBoxB) );

        //lets go to first one!
        shoeRack.request( tagA );

        //if its in a stack then only show this fragment!
        assertTrue( "shoeBoxA is visible", shoeBoxA.isActive() );
        assertFalse( "shoeBoxB is invisible", shoeBoxB.isActive() );

        shoeRack.request( tagB );

        assertFalse( "shoeBoxA is invisible", shoeBoxA.isActive() );
        assertTrue( "shoeBoxB is visibile", shoeBoxB.isActive() );
    }

    @Test
    public void shouldHistoryByFlowWorkFine(){
        

        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);
        ShoeBox shoeBoxC = ShoeBox.build(fragmentC);

        shoeRack.applyNodes( ShoeFlow.build(shoeBoxA, shoeBoxB, shoeBoxC) );

        //lets go to first one!
        shoeRack.request( tagC );
        assertEquals( "there are three elements in history!", shoeRack.getHistory().size(), 3 );
        assertFalse( "going back shouldn't execute, and then return false", shoeRack.goBack() );

    }


    @Test
    public void shouldStackGoBack(){
        

        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);
        ShoeBox shoeBoxC = ShoeBox.build(fragmentC);

        shoeRack.applyNodes( ShoeStack.build(shoeBoxA, shoeBoxB, shoeBoxC) );

        //lets go to first one!
        shoeRack.request( tagA );
        shoeRack.request( tagB );
        shoeRack.request( tagC );

        //if its in a stack then only show this fragment!
        assertTrue( "shoeBoxA is visible", shoeBoxC.isActive() );

        Boolean wentBack = shoeRack.goBack();
        assertTrue("able to go back", wentBack );
        assertTrue( "shoeBoxB is visible", shoeBoxB.isActive() );
        assertFalse( "shoeBoxC is invisible", shoeBoxC.isActive() );
        assertFalse( "shoeBoxA is invisible", shoeBoxA.isActive() );

        wentBack = shoeRack.goBack();
        assertTrue("able to go back", wentBack );
        assertFalse( "shoeBoxB is ivisible", shoeBoxB.isActive() );
        assertFalse( "shoeBoxC is invisible", shoeBoxC.isActive() );
        assertTrue( "shoeBoxA is visible", shoeBoxA.isActive() );

        wentBack = shoeRack.goBack();
        assertFalse("navigation ended when false", wentBack );
    }


    @Test
    public void shouldFlowGoBack(){
        

        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);
        ShoeBox shoeBoxC = ShoeBox.build(fragmentC);


        shoeRack.applyNodes( ShoeStack.build(shoeBoxC, ShoeStack.build(shoeBoxA, shoeBoxB)) );
        shoeRack.request( tagC );
        shoeRack.request( tagA );
        shoeRack.request( tagB );

        assertTrue( "B is active", shoeBoxB.isActive() );
        assertFalse( "A is inactive", shoeBoxA.isActive() );
        assertFalse( "C is inactive", shoeBoxC.isActive() );

        shoeRack.goBack();

        assertTrue( "A is active", shoeBoxA.isActive() );
        assertFalse( "B is inactive", shoeBoxB.isActive() );
        assertFalse( "C is inactive", shoeBoxC.isActive() );

        assertTrue( "can go back", shoeRack.goBack());

        assertTrue( "C is inactive", shoeBoxC.isActive() );
        assertFalse( "B is inactive", shoeBoxB.isActive() );
        assertFalse( "A is inactive", shoeBoxA.isActive() );

    }

    @Test
    public void slideArrayList(){
        List<String> strings = new ArrayList<>(Arrays.asList(new String[]{"a","b","c","d"}));
        strings = strings.subList(0, 2);
        assertEquals( "last is c", strings.get(strings.size()-1), "b"  );
    }


    @Test
    public void shouldStackOfStacksGoForward(){

        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);
        ShoeBox shoeBoxC = ShoeBox.build(fragmentC);
        ShoeBox shoeBoxD = ShoeBox.build( fragmentD );

        ShoeStack shoeStack1;
        ShoeStack shoeStack2;

        shoeRack.applyNodes( ShoeStack.build( shoeStack1=ShoeStack.build(shoeBoxA, shoeBoxB), shoeStack2=ShoeStack.build(shoeBoxC, shoeBoxD)) );


        shoeRack.request( tagA );

        assertTrue( "first stack displayed", shoeStack1.isActive() );
        assertFalse( "second stack hides", shoeStack2.isActive() );


        shoeRack.request( tagC );

        assertFalse( "first stack hides", shoeStack1.isActive() );
        assertTrue( "second stack displayed", shoeStack2.isActive() );
    }


    @Test
    public void mockingRotatingDevice(){
        //so initially we have a list of fragments, but then they change to a stack..
        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);

        shoeRack.applyNodes(shoeBoxA, shoeBoxB);

        assertTrue( "visible", shoeBoxA.isActive() );
        assertTrue( "visible", shoeBoxB.isActive() );

        //ok,, now we rotate and we have a stack of fragments
        shoeRack.applyNodes( ShoeStack.build(shoeBoxA, shoeBoxB));
        shoeRack.request( tagA );
        assertTrue( "visible", shoeBoxA.isActive() );
        assertFalse( "visible", shoeBoxB.isActive() );

        //lets go to fragmentB
        shoeRack.request( tagB );

        assertFalse( "visible a", shoeBoxA.isActive() );
        assertTrue( "invisible b", shoeBoxB.isActive() );

        //lets rotate again
        shoeRack.applyNodes(shoeBoxA, shoeBoxB);

        assertTrue( "visible", shoeBoxA.isActive() );
        assertTrue( "visible", shoeBoxB.isActive() );

        //ok,, now we rotate and we have a stack of fragments
        shoeRack.applyNodes( ShoeStack.build(shoeBoxA, shoeBoxB));
        shoeRack.request( tagB );
        assertFalse( "visible", shoeBoxA.isActive() );
        assertTrue( "visible", shoeBoxB.isActive() );

        shoeRack.goBack();

        assertTrue( "visible", shoeBoxA.isActive() );
        assertFalse( "visible", shoeBoxB.isActive() );
    }


    /**
     * So we have a flow and then a Stack. We want to see ShoeRack's history keeps up
     * with the changes.
     */
    @Test
    public void testHistoryAfterRotation(){

    }

}