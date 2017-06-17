package info.juanmendez.shoeboxes;

import org.junit.Before;
import org.junit.Test;

import info.juanmendez.shoeboxes.adapters.ShoeFragment;
import info.juanmendez.shoeboxes.models.ShoeBox;
import info.juanmendez.shoeboxes.models.ShoeRack;
import info.juanmendez.shoeboxes.models.ShoeStack;
import info.juanmendez.shoeboxes.models.TestShoeFragment;
import info.juanmendez.shoeboxes.models.TestShoeFragmentManager;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class BackstackNavigationTest {

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
        shoeRack = ShoeStorage.getRack( BasicShoeStorageTest.class.getSimpleName());
        fragmentA = new TestShoeFragment(tagA);
        fragmentB = new TestShoeFragment(tagB);
        fragmentC = new TestShoeFragment(tagC);
        fragmentD = new TestShoeFragment(tagD);
        fragmentE = new TestShoeFragment(tagE);
        fragmentF = new TestShoeFragment(tagF);
        fragmentManagerShadow = new TestShoeFragmentManager();
    }

    @Test
    public void shouldStackGoBack(){

        shoeRack.clearHistory();
        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);
        ShoeBox shoeBoxC = ShoeBox.build(fragmentC);

        shoeRack.populate( ShoeStack.build(shoeBoxA, shoeBoxB, shoeBoxC) );

        //lets go to first one!
        shoeRack.request( tagA );
        shoeRack.request( tagB );
        shoeRack.request( tagC );

        //if its in a stack then only show this fragment!
        assertTrue( "shoeBoxC is active", shoeBoxC.isActive() );

        Boolean wentBack = shoeRack.goBack();
        assertTrue("able to go back", wentBack );
        assertTrue( "shoeBoxB is active", shoeBoxB.isActive() );
        assertFalse( shoeBoxC.isActive() );
        assertFalse( "shoeBoxA is inactive", shoeBoxA.isActive() );

        wentBack = shoeRack.goBack();
        assertTrue("able to go back", wentBack );
        assertFalse( "shoeBoxB is iactive", shoeBoxB.isActive() );
        assertFalse( shoeBoxC.isActive() );
        assertTrue( shoeBoxA.isActive() );

        wentBack = shoeRack.goBack();
        assertFalse("navigation ended when false", wentBack );
    }


    @Test
    public void shouldFlowGoBack(){

        shoeRack.clearHistory();
        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);
        ShoeBox shoeBoxC = ShoeBox.build(fragmentC);


        shoeRack.populate( ShoeStack.build(shoeBoxC, ShoeStack.build(shoeBoxA, shoeBoxB)) );
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
    public void shouldStackOfStacksGoForward(){
        shoeRack.clearHistory();
        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);
        ShoeBox shoeBoxC = ShoeBox.build(fragmentC);
        ShoeBox shoeBoxD = ShoeBox.build( fragmentD );

        ShoeStack shoeStack1;
        ShoeStack shoeStack2;

        shoeRack.populate( ShoeStack.build( shoeStack1=ShoeStack.build(shoeBoxA, shoeBoxB), shoeStack2=ShoeStack.build(shoeBoxC, shoeBoxD)) );


        shoeRack.request( tagA );

        assertTrue( shoeStack1.isActive() );
        assertFalse( shoeStack2.isActive() );


        shoeRack.request( tagC );

        assertFalse( shoeStack1.isActive() );
        assertTrue( shoeStack2.isActive() );
    }


    @Test
    public void mockingRotatingDevice(){

        shoeRack.clearHistory();

        //so initially we have a list of fragments, but then they change to a stack..
        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);
        ShoeBox shoeBoxC = ShoeBox.build(fragmentC);

        shoeRack.populate(shoeBoxA, shoeBoxB, shoeBoxC );

        assertTrue( shoeBoxA.isActive() );
        assertTrue( shoeBoxB.isActive() );
        assertTrue( shoeBoxC.isActive() );

        shoeRack.request( tagC );

        //ok,, now we rotate and we have a stack of fragments

        shoeBoxA = ShoeBox.build(fragmentA);
        shoeBoxB = ShoeBox.build(fragmentB);
        shoeBoxC = ShoeBox.build(fragmentC);
        shoeRack.populate( ShoeStack.build( shoeBoxA, shoeBoxB, shoeBoxC));

        assertEquals( "history has 3 requests", shoeRack.getHistory().size(), 3 );


        shoeBoxA = ShoeBox.build(fragmentA);
        shoeBoxB = ShoeBox.build(fragmentB);
        shoeBoxC = ShoeBox.build(fragmentC);
        shoeRack.populate(shoeBoxA, shoeBoxB, shoeBoxC );
        assertEquals( "history has 3 requests", shoeRack.getHistory().size(), 3 );

        shoeBoxA = ShoeBox.build(fragmentA);
        shoeBoxB = ShoeBox.build(fragmentB);
        shoeBoxC = ShoeBox.build(fragmentC);
        shoeRack.populate( ShoeStack.build( shoeBoxA, shoeBoxB, shoeBoxC));

        assertEquals( "history has 3 requests", shoeRack.getHistory().size(), 3 );

        assertFalse( shoeBoxA.isActive() );
        assertFalse( shoeBoxB.isActive() );
        assertTrue( shoeBoxC.isActive() );
    }

    @Test
    public void shouldUpdateHistoryWithSameStructure(){
        shoeRack.clearHistory();

        ShoeBox shoeBoxA, shoeBoxB, shoeBoxC;
        Boolean requestResult = false;

        shoeBoxA = ShoeBox.build(fragmentA);
        shoeBoxB = ShoeBox.build(fragmentB);
        shoeBoxC = ShoeBox.build(fragmentC);

        shoeRack.populate( ShoeStack.build(shoeBoxA, shoeBoxB, shoeBoxC) );

        //lets go to first one!
        shoeRack.request( tagA );
        shoeRack.request( tagB );
        shoeRack.request( tagC );


        //ok we are still having the shoeStack
        shoeRack.populate( ShoeStack.build(shoeBoxA, shoeBoxB, shoeBoxC) );
        assertEquals( "history remains having 3 ", shoeRack.getHistory().size(), 3 );
        
        shoeRack.goBack();
        shoeRack.goBack();

        assertTrue( shoeBoxA.isActive() );
        assertFalse(  shoeBoxB.isActive() );
        assertFalse( shoeBoxC.isActive() );

        assertFalse( "we can't go back anymore", shoeRack.goBack() );

        //lets remove one fragment from the structure..
        //one fragment can turn into a sliding nav which is no longer
        //applying the structure.. just saying..

        shoeRack.populate( ShoeStack.build(shoeBoxA, shoeBoxB) );
        requestResult = shoeRack.request( tagC );

        assertFalse( "can't be requested ", requestResult );
    }


    /**
     *  Portrait=> Stack(A,B,C)
     *  Landscape=> ( A, Stack(B,C))
     *
     */
    @Test
    public void shouldNavigateBackWithFlowAndStack(){
        shoeRack.clearHistory();

        ShoeBox shoeBoxA, shoeBoxB, shoeBoxC;

        //Portrait mode
        shoeBoxA = ShoeBox.build(fragmentA);
        shoeBoxB = ShoeBox.build(fragmentB);
        shoeBoxC = ShoeBox.build(fragmentC);

        shoeRack.populate( ShoeStack.build(shoeBoxA, shoeBoxB, shoeBoxC) );
        shoeRack.request( tagA );
        shoeRack.request( tagB );
        shoeRack.request( tagC );


        //Landscape mode
        shoeBoxA = ShoeBox.build(fragmentA);
        shoeBoxB = ShoeBox.build(fragmentB);
        shoeBoxC = ShoeBox.build(fragmentC);
        shoeRack.populate( shoeBoxA, ShoeStack.build(shoeBoxB, shoeBoxC));

        shoeRack.goBack();

        //end of back navigation within this structure
        assertFalse(shoeRack.goBack());
    }

    /**
     * We want to include childBoxes in any parent which can have its children all inactive
     * This is the case for ShoeStack
     */
    @Test
    public void shouldSuggestionsWork(){

        shoeRack.clearHistory();

        ShoeBox shoeBoxA, shoeBoxB, shoeBoxC;

        //Portrait mode
        shoeBoxA = ShoeBox.build(fragmentA);
        shoeBoxB = ShoeBox.build(fragmentB);
        shoeBoxC = ShoeBox.build(fragmentC);

        shoeRack.populate( ShoeStack.build(shoeBoxA, shoeBoxB, shoeBoxC) );
        assertFalse( shoeBoxA.isActive() );

        //lets see if suggestion works.
        assertTrue( shoeRack.suggest( tagA ));
        assertTrue( shoeBoxA.isActive() );

        //certainly we cannot suggest again.
        assertFalse( shoeRack.suggest( tagB) );
        assertFalse( shoeRack.suggest( tagA) );


        /**
         * ROTATION
         */
        shoeBoxA = ShoeBox.build(fragmentA);
        shoeBoxB = ShoeBox.build(fragmentB);
        shoeBoxC = ShoeBox.build(fragmentC);
        shoeRack.populate( shoeBoxA, shoeBoxB, shoeBoxC );

        //all must be active..
        assertTrue( shoeBoxA.isActive() );
        assertTrue(  shoeBoxB.isActive() );
        assertTrue( shoeBoxC.isActive() );

        //all our suggestions must fail..
        assertFalse( shoeRack.suggest( tagA) );
        assertFalse( shoeRack.suggest( tagB) );
        assertFalse( shoeRack.suggest( tagC) );

        shoeRack.request( tagC );

        /**
         * ROTATION
         */
        shoeBoxA = ShoeBox.build(fragmentA);
        shoeBoxB = ShoeBox.build(fragmentB);
        shoeBoxC = ShoeBox.build(fragmentC);
        shoeRack.populate( ShoeStack.build(shoeBoxA, shoeBoxB, shoeBoxC) );

        assertFalse( shoeBoxA.isActive() );
        assertFalse(  shoeBoxB.isActive() );
        assertTrue( shoeBoxC.isActive() );
    }
}