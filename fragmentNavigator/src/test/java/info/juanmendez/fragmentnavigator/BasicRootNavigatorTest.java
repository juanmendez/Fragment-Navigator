package info.juanmendez.fragmentnavigator;

import org.junit.Before;
import org.junit.Test;

import info.juanmendez.fragmentnavigator.adapters.NavFragment;
import info.juanmendez.fragmentnavigator.models.NavItem;
import info.juanmendez.fragmentnavigator.models.NavNode;
import info.juanmendez.fragmentnavigator.models.NavStack;
import info.juanmendez.fragmentnavigator.models.TestNavFragment;
import info.juanmendez.fragmentnavigator.models.TestNavFragmentManager;

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

    TestNavFragmentManager fragmentManagerShadow;
    NavFragment fragmentA;
    NavFragment fragmentB;
    NavFragment fragmentC;

    RootNavigator navigator;

    @Before
    public void before(){
        navigator = RootNavigator.getInstance();

        fragmentA = new TestNavFragment(tagA);
        fragmentB = new TestNavFragment(tagB);
        fragmentC = new TestNavFragment(tagC);
        fragmentManagerShadow = new TestNavFragmentManager();
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
        /*NavNode parentNode = navigator.getRoot().search( tagA );
        assertEquals( "it's the same root node", parentNode, navigator.getRoot() );*/

        NavItem navItemA = NavItem.build(fragmentA);
        NavItem navItemB = NavItem.build(fragmentB);

        //lets have fragmentA has other fragments
        NavFragment fragmentC = new TestNavFragment("fragmentC");
        NavFragment fragmentD = new TestNavFragment("fragmentD");

        NavItem navItemC = NavItem.build(fragmentC);
        NavItem navItemD = NavItem.build(fragmentD);

        navigator.clear();
        navigator.applyNodes(navItemA, navItemB.applyNodes(navItemC, navItemD) );


        NavNode result;
        NavNode match = null;

        for( NavNode node: navigator.getNodes() ){

            result = node.search( "fragmentC");

            if( result != null ){
                match = result;
            }
        }

        assertNotNull( "not null", match);
    }


    @Test
    public void shouldForwardStack(){
        navigator.clear();

        NavItem navItemA = NavItem.build(fragmentA);
        NavItem navItemB = NavItem.build(fragmentB);

        navigator.applyNodes( NavStack.build(navItemA, navItemB) );

        //lets go to first one!
        navigator.request( tagA );
        navigator.asObservable().subscribe(s -> {


            //if its in a stack then only show this fragment!
            NavNode parent = navigator.search(s);

            if( parent instanceof NavStack ){
                parent.display(s);
            }
        });

        //if its in a stack then only show this fragment!
        assertTrue( "navItemA is visible", navItemA.getNavFragment().getVisible() );
        assertFalse( "navItemB is invisible", navItemB.getNavFragment().getVisible() );

        navigator.request( tagB );

        assertFalse( "navItemA is invisible", navItemA.getNavFragment().getVisible() );
        assertTrue( "navItemB is visibile", navItemB.getNavFragment().getVisible() );
    }



    @Test
    public void shouldDoBackStack(){
        navigator.clear();

        NavItem navItemA = NavItem.build(fragmentA);
        NavItem navItemB = NavItem.build(fragmentB);
        NavItem navItemC = NavItem.build(fragmentC);

        navigator.applyNodes( NavStack.build(navItemA, navItemB, navItemC) );

        navigator.asObservable().subscribe(s -> {

            //if its in a stack then only show this fragment!
            NavNode parent = navigator.search(s);

            if( parent != null )
                parent.display(s);
        });


        //lets go to first one!
        navigator.request( tagA );
        navigator.request( tagB );
        navigator.request( tagC );

        //if its in a stack then only show this fragment!
        assertTrue( "navItemA is visible", navItemC.getNavFragment().getVisible() );

        //we are going back now...
        NavNode parent = navigator.search(tagC);

        Boolean wentBack = parent.goBack();
        assertTrue("able to go back", wentBack );
        assertTrue( "navItemB is visible", navItemB.getNavFragment().getVisible() );
        assertFalse( "navItemC is invisible", navItemC.getNavFragment().getVisible() );
        assertFalse( "navItemA is invisible", navItemA.getNavFragment().getVisible() );

        wentBack = parent.goBack();
        assertTrue("able to go back", wentBack );
        assertFalse( "navItemB is ivisible", navItemB.getNavFragment().getVisible() );
        assertFalse( "navItemC is invisible", navItemC.getNavFragment().getVisible() );
        assertTrue( "navItemA is visible", navItemA.getNavFragment().getVisible() );

        wentBack = parent.goBack();
        assertFalse("navigation ended when false", wentBack );

    }
}