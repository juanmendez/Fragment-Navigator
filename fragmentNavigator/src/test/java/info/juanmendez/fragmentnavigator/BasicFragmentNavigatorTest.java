package info.juanmendez.fragmentnavigator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import info.juanmendez.fragmentnavigator.models.Node;
import info.juanmendez.fragmentnavigator.models.Stack;
import info.juanmendez.fragmentnavigator.models.NavNode;
import info.juanmendez.fragmentnavigator.models.TestNavFragmentManager;
import info.juanmendez.fragmentnavigator.models.TestNavFragment;
import info.juanmendez.fragmentnavigator.simulators.NavFragment;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"org.mockito.*", "android.*"})
@PrepareForTest({})
public class BasicFragmentNavigatorTest {

    String tagA = "fragmentA";
    String tagB = "fragmentB";

    TestNavFragmentManager fragmentManagerShadow;
    NavFragment fragmentA;
    NavFragment fragmentB;

    FragmentNavigator navigator;

    @Before
    public void before(){
        navigator = FragmentNavigator.getInstance();

        fragmentA = new TestNavFragment(tagA);
        fragmentB = new TestNavFragment(tagB);
        fragmentManagerShadow = new TestNavFragmentManager();
    }


    @Test
    public void shouldBuildFragmentNode(){

        Node right = new Node(fragmentA);
        Node left = new Node(fragmentB);
        Stack root = Stack.build( right, left );
        assertEquals( root.getNodes().size(), 2 );
    }

    @Test
    public void shouldFMWork(){
        fragmentManagerShadow.add( tagA, fragmentA );
        fragmentManagerShadow.add( 0, fragmentB );

        assertEquals( "yes it has tagA", fragmentManagerShadow.findFragmentByTag(tagA), fragmentA );
        assertEquals( "yes it has tagB", fragmentManagerShadow.findFragmentById(0), fragmentB);

        fragmentManagerShadow.remove( tagA );
        fragmentManagerShadow.remove( 0 );

        assertNull( "tagA gone", fragmentManagerShadow.findFragmentByTag(tagA) );
        assertNull( "tagB gone", fragmentManagerShadow.findFragmentById(0) );
    }

    @Test
    public void shouldDispatchRequest(){
        fragmentManagerShadow.add( tagA, fragmentA );
        fragmentManagerShadow.add( tagB, fragmentB );

        Node nodeA = Node.build(fragmentA);
        Node nodeB = Node.build(fragmentB);

        //lets draw the fragments
        navigator.setFragmentNodeRoot( nodeA, nodeB );

        //so we are going to build a dual pane...
        navigator.request( tagA );

        navigator.asObservable().subscribe(s -> {
            assertEquals( "tag is A", tagA, s );
        });
    }


    @Test
    public void shouldGetParentOfNode(){
        NavNode parentNode = navigator.getFragmentNodeRoot().search( tagA );
        assertEquals( "it's the same root node", parentNode, navigator.getFragmentNodeRoot() );

        Node nodeA = Node.build(fragmentA);
        Node nodeB = Node.build(fragmentB);

        //lets have fragmentA has other fragments
        NavFragment fragmentC = new TestNavFragment("fragmentC");
        NavFragment fragmentD = new TestNavFragment("fragmentD");

        Node nodeC = Node.build(fragmentC);
        Node nodeD = Node.build(fragmentD);

        navigator.getFragmentNodeRoot().clear();
        navigator.setFragmentNodeRoot( nodeA, nodeB.applyNodes( nodeC, nodeD ) );

        assertSame( "we find nodeB", nodeC, navigator.getFragmentNodeRoot().search( "fragmentC" ));
    }

}
