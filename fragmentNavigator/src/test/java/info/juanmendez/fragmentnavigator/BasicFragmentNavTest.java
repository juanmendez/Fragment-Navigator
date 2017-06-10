package info.juanmendez.fragmentnavigator;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.juanmendez.fragmentnavigator.adapters.CoreNavFragment;
import info.juanmendez.fragmentnavigator.models.NavItem;
import info.juanmendez.fragmentnavigator.models.NavNode;
import info.juanmendez.fragmentnavigator.models.NavRoot;
import info.juanmendez.fragmentnavigator.models.NavStack;
import info.juanmendez.fragmentnavigator.models.TestCoreNavFragment;
import info.juanmendez.fragmentnavigator.models.TestCoreNavFragmentManager;

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

public class BasicFragmentNavTest {

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
    NavRoot navRoot;

    @Before
    public void before(){
        navRoot = new NavRoot();
        FragmentNav.setNavRoot( navRoot );
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
        navRoot.applyNodes(navItemA, navItemB);

        //so we are going to build a dual pane...
        navRoot.request( tagA );

        navRoot.asObservable().subscribe(navItems -> {
            assertEquals( "tag is A", navItems.get(navItems.size()-1), navItemA );
        });
    }


    @Test
    public void shouldGetParentOfNode(){
        /*NavNode parentNode = navigator.getRoot().search( tagA );
        assertEquals( "it's the same root node", parentNode, navigator.getRoot() );*/

        NavItem navItemA = NavItem.build(fragmentA);
        NavItem navItemB = NavItem.build(fragmentB);


        NavItem navItemC = NavItem.build(fragmentC);
        NavItem navItemD = NavItem.build(fragmentD);

        navRoot.clear();
        navRoot.applyNodes(navItemA, navItemB.applyNodes(navItemC, navItemD) );


        NavNode result;
        NavNode match = null;

        for( NavNode node: navRoot.getNodes() ){

            result = node.search( tagC);

            if( result != null ){
                match = result;
            }
        }

        assertNotNull( "not null", match);
    }


    @Test
    public void shouldStackGoForward(){
        navRoot.clear();

        NavItem navItemA = NavItem.build(fragmentA);
        NavItem navItemB = NavItem.build(fragmentB);

        navRoot.applyNodes( NavStack.build(navItemA, navItemB) );

        //lets go to first one!
        navRoot.request( tagA );

        //if its in a stack then only show this fragment!
        assertTrue( "navItemA is visible", navItemA.isActive() );
        assertFalse( "navItemB is invisible", navItemB.isActive() );

        navRoot.request( tagB );

        assertFalse( "navItemA is invisible", navItemA.isActive() );
        assertTrue( "navItemB is visibile", navItemB.isActive() );
    }

    @Test
    public void shouldStackGoBack(){
        navRoot.clear();

        NavItem navItemA = NavItem.build(fragmentA);
        NavItem navItemB = NavItem.build(fragmentB);
        NavItem navItemC = NavItem.build(fragmentC);

        navRoot.applyNodes( NavStack.build(navItemA, navItemB, navItemC) );

        //lets go to first one!
        navRoot.request( tagA );
        navRoot.request( tagB );
        navRoot.request( tagC );

        //if its in a stack then only show this fragment!
        assertTrue( "navItemA is visible", navItemC.isActive() );

        Boolean wentBack = navRoot.goBack();
        assertTrue("able to go back", wentBack );
        assertTrue( "navItemB is visible", navItemB.isActive() );
        assertFalse( "navItemC is invisible", navItemC.isActive() );
        assertFalse( "navItemA is invisible", navItemA.isActive() );

        wentBack = navRoot.goBack();
        assertTrue("able to go back", wentBack );
        assertFalse( "navItemB is ivisible", navItemB.isActive() );
        assertFalse( "navItemC is invisible", navItemC.isActive() );
        assertTrue( "navItemA is visible", navItemA.isActive() );

        wentBack = navRoot.goBack();
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
        navRoot.clear();

        NavItem navItemA = NavItem.build(fragmentA);
        NavItem navItemB = NavItem.build(fragmentB);
        NavItem navItemC = NavItem.build(fragmentC);
        NavItem navItemD = NavItem.build( fragmentD );

        navRoot.applyNodes( NavStack.build( NavStack.build(navItemA,navItemB), NavStack.build(navItemC,navItemD)) );

        NavStack navStack1 = (NavStack) navRoot.getNodes().get(0).getNodes().get(0);
        NavStack navStack2 = (NavStack) navRoot.getNodes().get(0).getNodes().get(1);

        navRoot.request( tagA );

        assertTrue( "first stack displayed", navStack1.isActive() );
        assertFalse( "second stack hides", navStack2.isActive() );


        navRoot.request( tagC );

        assertFalse( "first stack hides", navStack1.isActive() );
        assertTrue( "second stack displayed", navStack2.isActive() );
    }


    @Test
    public void mockingRotatingDevice(){
        //so initially we have a list of fragments, but then they change to a stack..
        NavItem navItemA = NavItem.build(fragmentA);
        NavItem navItemB = NavItem.build(fragmentB);

        navRoot.applyNodes( navItemA, navItemB );

        assertTrue( "visible", navItemA.isActive() );
        assertTrue( "visible", navItemB.isActive() );

        //ok,, now we rotate and we have a stack of fragments
        navRoot.applyNodes( NavStack.build(navItemA, navItemB));
        navRoot.request( tagA );
        assertTrue( "visible", navItemA.isActive() );
        assertFalse( "visible", navItemB.isActive() );

        //lets go to fragmentB
        navRoot.request( tagB );

        assertFalse( "visible a", navItemA.isActive() );
        assertTrue( "invisible b", navItemB.isActive() );

        //lets rotate again
        navRoot.applyNodes( navItemA, navItemB );

        assertTrue( "visible", navItemA.isActive() );
        assertTrue( "visible", navItemB.isActive() );

        //ok,, now we rotate and we have a stack of fragments
        navRoot.applyNodes( NavStack.build(navItemA, navItemB));
        navRoot.request( tagB );
        assertFalse( "visible", navItemA.isActive() );
        assertTrue( "visible", navItemB.isActive() );

        navRoot.goBack();

        assertTrue( "visible", navItemA.isActive() );
        assertFalse( "visible", navItemB.isActive() );

    }

}