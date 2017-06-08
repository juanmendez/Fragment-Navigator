package info.juanmendez.corenavigator;

import org.junit.Before;
import org.junit.Test;

import info.juanmendez.corenavigator.adapters.CoreNavFragment;
import info.juanmendez.corenavigator.models.NavItem;
import info.juanmendez.corenavigator.models.NavNode;
import info.juanmendez.corenavigator.models.NavStack;
import info.juanmendez.corenavigator.models.TestCoreNavFragment;
import info.juanmendez.corenavigator.models.TestCoreNavFragmentManager;

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

public class BasicRootNavigatorTest {

    String tagA = "fragmentA";
    String tagB = "fragmentB";
    String tagC = "fragmentC";
    String tagD = "fragmentD";
    String tagE = "fragmentE";
    String tagF = "fragmentF";

    TestCoreNavFragmentManager fragmentManagerShadow;
    CoreNavFragment fragmentA;
    CoreNavFragment fragmentB;
    CoreNavFragment fragmentC;
    CoreNavFragment fragmentD;
    CoreNavFragment fragmentE;
    CoreNavFragment fragmentF;

    RootNavigator navigator;

    @Before
    public void before(){
        navigator = RootNavigator.getInstance();

        fragmentA = new TestCoreNavFragment(tagA);
        fragmentB = new TestCoreNavFragment(tagB);
        fragmentC = new TestCoreNavFragment(tagC);
        fragmentD = new TestCoreNavFragment(tagD);
        fragmentE = new TestCoreNavFragment(tagE);
        fragmentF = new TestCoreNavFragment(tagF);
        fragmentManagerShadow = new TestCoreNavFragmentManager();
    }


    @Test
    public void shouldBuildFragmentNode(){

        NavItem right = new NavItem(fragmentA);
        NavItem left = new NavItem(fragmentB);
        NavStack root = NavStack.build( right, left );
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

        NavItem navItemA = NavItem.build(fragmentA);
        NavItem navItemB = NavItem.build(fragmentB);

        //lets draw the fragments
        navigator.applyNodes(navItemA, navItemB);

        //so we are going to build a dual pane...
        navigator.request( tagA );

        navigator.asObservable().subscribe(s -> {
            assertEquals( "tag is A", tagA, s );
        });
    }


    @Test
    public void shouldGetParentOfNode(){
        /*NavNode parentNode = navigator.getRoot().searchParent( tagA );
        assertEquals( "it's the same root node", parentNode, navigator.getRoot() );*/

        NavItem navItemA = NavItem.build(fragmentA);
        NavItem navItemB = NavItem.build(fragmentB);


        NavItem navItemC = NavItem.build(fragmentC);
        NavItem navItemD = NavItem.build(fragmentD);

        navigator.clear();
        navigator.applyNodes(navItemA, navItemB.applyNodes(navItemC, navItemD) );


        NavNode result;
        NavNode match = null;

        for( NavNode node: navigator.getNodes() ){

            result = node.searchParent( tagC);

            if( result != null ){
                match = result;
            }
        }

        assertNotNull( "not null", match);
    }


    @Test
    public void shouldStackGoForward(){
        navigator.clear();

        NavItem navItemA = NavItem.build(fragmentA);
        NavItem navItemB = NavItem.build(fragmentB);

        navigator.applyNodes( NavStack.build(navItemA, navItemB) );

        //lets go to first one!
        navigator.request( tagA );
        navigator.asObservable().subscribe(s -> {


            //if its in a stack then only show this fragment!
            NavNode parent = navigator.searchParent(s);

            if( parent instanceof NavStack ){
                parent.displayChild(s);
            }
        });

        //if its in a stack then only show this fragment!
        assertTrue( "navItemA is visible", navItemA.getVisible() );
        assertFalse( "navItemB is invisible", navItemB.getVisible() );

        navigator.request( tagB );

        assertFalse( "navItemA is invisible", navItemA.getVisible() );
        assertTrue( "navItemB is visibile", navItemB.getVisible() );
    }



    @Test
    public void shouldStackGoBack(){
        navigator.clear();

        NavItem navItemA = NavItem.build(fragmentA);
        NavItem navItemB = NavItem.build(fragmentB);
        NavItem navItemC = NavItem.build(fragmentC);

        navigator.applyNodes( NavStack.build(navItemA, navItemB, navItemC) );

        navigator.asObservable().subscribe(s -> {

            //if its in a stack then only show this fragment!
            NavNode parent = navigator.searchParent(s);

            if( parent != null )
                parent.displayChild(s);
        });


        //lets go to first one!
        navigator.request( tagA );
        navigator.request( tagB );
        navigator.request( tagC );

        //if its in a stack then only show this fragment!
        assertTrue( "navItemA is visible", navItemC.getVisible() );

        //we are going back now...
        NavNode parent = navigator.searchParent(tagC);

        Boolean wentBack = parent.goBack();
        assertTrue("able to go back", wentBack );
        assertTrue( "navItemB is visible", navItemB.getVisible() );
        assertFalse( "navItemC is invisible", navItemC.getVisible() );
        assertFalse( "navItemA is invisible", navItemA.getVisible() );

        wentBack = parent.goBack();
        assertTrue("able to go back", wentBack );
        assertFalse( "navItemB is ivisible", navItemB.getVisible() );
        assertFalse( "navItemC is invisible", navItemC.getVisible() );
        assertTrue( "navItemA is visible", navItemA.getVisible() );

        wentBack = parent.goBack();
        assertFalse("navigation ended when false", wentBack );
    }


    @Test
    public void shouldStackOfStacksGoForward(){
        navigator.clear();

        NavItem navItemA = NavItem.build(fragmentA);
        NavItem navItemB = NavItem.build(fragmentB);
        NavItem navItemC = NavItem.build(fragmentC);
        NavItem navItemD = NavItem.build( fragmentD );

        navigator.applyNodes( NavStack.build( NavStack.build(navItemA,navItemB), NavStack.build(navItemC,navItemD)) );

        navigator.asObservable().subscribe(s -> {
            //if its in a stack then only show this fragment!
            NavNode parent = navigator.searchParent(s);

            if( parent != null )
                parent.displayChild(s);
        });


        NavStack navStack1 = (NavStack) navigator.getNodes().get(0).getNodes().get(0);
        NavStack navStack2 = (NavStack) navigator.getNodes().get(0).getNodes().get(1);

        navigator.request( tagA );

        assertTrue( "first stack displayed", navStack1.getVisible() );
        assertFalse( "second stack hides", navStack2.getVisible() );


        navigator.request( tagC );

        assertFalse( "first stack hides", navStack1.getVisible() );
        assertTrue( "second stack displayed", navStack2.getVisible() );
    }


    @Test
    public void mockingRotatingDevice(){
        //so initially we have a list of fragments, but then they change to a stack..

        navigator.asObservable().subscribe(s -> {
            //if its in a stack then only show this fragment!
            NavNode parent = navigator.searchParent(s);

            if( parent != null )
                parent.displayChild(s);
        });

        NavItem navItemA = NavItem.build(fragmentA);
        NavItem navItemB = NavItem.build(fragmentB);

        navigator.applyNodes( navItemA, navItemB );

        assertTrue( "visible", navItemA.getVisible() );
        assertTrue( "visible", navItemB.getVisible() );

        //ok,, now we rotate and we have a stack of fragments
        navigator.applyNodes( NavStack.build(navItemA, navItemB));

        assertTrue( "visible", navItemA.getVisible() );
        assertFalse( "visible", navItemB.getVisible() );

        //lets go to fragmentB
        navigator.request( tagB );

        assertFalse( "visible a", navItemA.getVisible() );
        assertTrue( "invisible b", navItemB.getVisible() );

        //lets rotate again
        navigator.applyNodes( navItemA, navItemB );

        assertTrue( "visible", navItemA.getVisible() );
        assertTrue( "visible", navItemB.getVisible() );

        //ok,, now we rotate and we have a stack of fragments
        navigator.applyNodes( NavStack.build(navItemA, navItemB));

        navigator.request( tagB );
        assertFalse( "visible", navItemA.getVisible() );
        assertTrue( "visible", navItemB.getVisible() );

        navigator.getNodes().get(0).goBack();

        assertTrue( "visible", navItemA.getVisible() );
        assertFalse( "visible", navItemB.getVisible() );

    }
}