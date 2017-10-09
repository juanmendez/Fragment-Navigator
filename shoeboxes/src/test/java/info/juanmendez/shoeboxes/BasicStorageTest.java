package info.juanmendez.shoeboxes;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import info.juanmendez.shoeboxes.adapters.ShoeFragmentAdapter;
import info.juanmendez.shoeboxes.shoes.ShoeBox;
import info.juanmendez.shoeboxes.shoes.ShoeFlow;
import info.juanmendez.shoeboxes.shoes.ShoeModel;
import info.juanmendez.shoeboxes.shoes.ShoeRack;
import info.juanmendez.shoeboxes.shoes.ShoeStack;
import info.juanmendez.shoeboxes.shoes.TestShoeFragmentAdapter;

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

public class BasicStorageTest {

    String tagA = "fragmentA";
    String tagB = "fragmentB";
    String tagC = "fragmentC";
    String tagD = "fragmentD";
    String tagE = "fragmentE";
    String tagF = "fragmentF";

    ShoeFragmentAdapter fragmentA;
    ShoeFragmentAdapter fragmentB;
    ShoeFragmentAdapter fragmentC;
    ShoeFragmentAdapter fragmentD;
    ShoeFragmentAdapter fragmentE;
    ShoeFragmentAdapter fragmentF;
    ShoeRack shoeRack;

    @Before
    public void before(){
        shoeRack = ShoeStorage.getRack( BasicStorageTest.class.getSimpleName());
        fragmentA = new TestShoeFragmentAdapter(tagA);
        fragmentB = new TestShoeFragmentAdapter(tagB);
        fragmentC = new TestShoeFragmentAdapter(tagC);
        fragmentD = new TestShoeFragmentAdapter(tagD);
        fragmentE = new TestShoeFragmentAdapter(tagE);
        fragmentF = new TestShoeFragmentAdapter(tagF);
    }


    @Test
    public void shouldBuildFragmentNode(){

        ShoeBox right = new ShoeBox(fragmentA);
        ShoeBox left = new ShoeBox(fragmentB);
        ShoeStack root = ShoeStack.build( right, left );
        assertEquals( root.getNodes().size(), 2 );
    }

    @Test
    public void shouldDispatchRequest(){
        shoeRack.clearHistory();


        ShoeBox shoeBoxA, shoeBoxB;

        //lets draw the fragments
        shoeRack.populate(shoeBoxA = ShoeBox.build(fragmentA), shoeBoxB = ShoeBox.build(fragmentB));

        //so we are going to build a dual pane...
        shoeRack.request( tagA );

        shoeRack.asObservable().take(1).subscribe(navItems -> {
            assertEquals( "tag is A", navItems.get(navItems.size()-1), shoeBoxA);
        });
    }


    @Test
    public void shouldGetParentOfNode(){
        shoeRack.clearHistory();
        /*ShoeModel parentNode = navigator.getRoot().search( tagA );
        assertEquals( "it's the same root node", parentNode, navigator.getRoot() );*/

        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);


        ShoeBox shoeBoxC = ShoeBox.build(fragmentC);
        ShoeBox shoeBoxD = ShoeBox.build(fragmentD);

        
        shoeRack.populate(shoeBoxA, shoeBoxB.populate(shoeBoxC, shoeBoxD ));


        ShoeModel result;
        ShoeModel match = shoeRack.search( tagC );

        assertNotNull( "not null", match);
    }


    @Test
    public void shouldStackGoForward(){

        shoeRack.clearHistory();
        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);

        shoeRack.populate( ShoeStack.build(shoeBoxA, shoeBoxB) );

        //lets go to first one!
        shoeRack.request( tagA );

        //if its in a stack then only show this fragment!
        assertTrue( "shoeBoxA is active", shoeBoxA.isActive() );
        assertFalse( "shoeBoxB is inactive", shoeBoxB.isActive() );

        shoeRack.request( tagB );

        assertFalse( "shoeBoxA is inactive", shoeBoxA.isActive() );
        assertTrue( "shoeBoxB is active", shoeBoxB.isActive() );
    }

    @Test
    public void shouldHistoryByFlowWorkFine(){

        shoeRack.clearHistory();
        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);
        ShoeBox shoeBoxC = ShoeBox.build(fragmentC);

        shoeRack.populate( ShoeFlow.build(shoeBoxA, shoeBoxB, shoeBoxC) );

        //lets go to first one!
        shoeRack.request( tagC );
        assertEquals( "there are three elements in history!", shoeRack.getHistory().size(), 3 );
        assertFalse( "going back shouldn't execute, and then return false", shoeRack.goBack() );

    }


    @Test
    public void testRequestAction(){

        shoeRack.clearHistory();
        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);
        ShoeBox shoeBoxC = ShoeBox.build(fragmentC);

        shoeRack.populate( ShoeStack.build(shoeBoxA, shoeBoxB, shoeBoxC) );


        shoeRack.request( tagB, "B" );

        //lets go to first one!
        shoeRack.request( tagC, "C" );

        assertEquals( shoeRack.getActionByTag( tagC ), "C" );
        assertNull( shoeRack.getActionByTag( tagA ));

        shoeRack.goBack();
        assertTrue( fragmentB.isActive() );
        assertEquals( shoeRack.getActionByTag(tagB), "B");
    }


    @Test
    public void testSuggestByTag(){
        shoeRack.clearHistory();
        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);
        ShoeBox shoeBoxC = ShoeBox.build(fragmentC);

        shoeRack.populate( ShoeStack.build(shoeBoxA, shoeBoxB, shoeBoxC) );

        HashMap<String,String> tagsWithActions = new HashMap<>();
        tagsWithActions.put( tagC, "C");
        shoeRack.suggest( tagsWithActions );
    }

    @Test
    public void testSuggestById(){
        shoeRack.clearHistory();

        int a=1,b=2,c=3,d=4,e=5,f=6;

        fragmentA = new TestShoeFragmentAdapter(a);
        fragmentB = new TestShoeFragmentAdapter(b);
        fragmentC = new TestShoeFragmentAdapter(c);

        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);
        ShoeBox shoeBoxC = ShoeBox.build(fragmentC);

        shoeRack.populate( shoeBoxA, shoeBoxB, shoeBoxC  );

        HashMap<Integer, String> idsWithActions = new HashMap<>();
        idsWithActions.put( a, "A");
        idsWithActions.put( b, "B");
        idsWithActions.put( c, "C");

        shoeRack.suggestIdsWithActions( idsWithActions );

        assertTrue( shoeBoxA.isActive() );
        assertTrue( shoeBoxC.isActive() );
        assertTrue( shoeBoxB.isActive() );

        assertEquals( shoeRack.getActionById(a), "A");
    }


    @Test
    public void slideArrayList(){
        List<String> strings = new ArrayList<>(Arrays.asList(new String[]{"a","b","c","d"}));
        strings = strings.subList(0, 2);
        assertEquals( "last is c", strings.get(strings.size()-1), "b"  );
    }

    @Test
    public void testByIDs(){
        ShoeRack shoeRack = ShoeStorage.getRack( "static_fragment_storage");
        int a=1,b=2,c=3,d=4,e=5,f=6;

        fragmentA = new TestShoeFragmentAdapter(a);
        fragmentB = new TestShoeFragmentAdapter(b);
        fragmentC = new TestShoeFragmentAdapter(c);

        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);
        ShoeBox shoeBoxC = ShoeBox.build(fragmentC);

        shoeRack.populate( ShoeStack.build( shoeBoxA, shoeBoxB, shoeBoxC ) );
        shoeRack.suggest( a );
        assertTrue( shoeBoxA.isActive() );
        assertFalse( shoeBoxC.isActive() );

        shoeRack.request( c );
        assertTrue( shoeBoxC.isActive() );

        shoeRack.goBack();
        assertTrue( shoeBoxA.isActive() );
        assertFalse( shoeBoxC.isActive() );
        assertFalse( shoeBoxB.isActive() );
    }
}