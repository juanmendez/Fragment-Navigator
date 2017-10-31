package info.juanmendez.shoeboxes;

import org.junit.Before;
import org.junit.Test;

import info.juanmendez.shoeboxes.adapters.ShoeFragmentAdapter;
import info.juanmendez.shoeboxes.shoes.ShoeBox;
import info.juanmendez.shoeboxes.shoes.ShoeRack;
import info.juanmendez.shoeboxes.shoes.ShoeStack;
import info.juanmendez.shoeboxes.shoes.TestShoeFragmentAdapter;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.spy;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class BasicChildFragmentTest {

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
        shoeRack = ShoeStorage.getRack( BasicChildFragmentTest.class.getSimpleName());
        fragmentA =  spy( new TestShoeFragmentAdapter(tagA));
        fragmentB =  spy( new TestShoeFragmentAdapter(tagB));
        fragmentC =  spy( new TestShoeFragmentAdapter(tagC));
        fragmentD =  spy( new TestShoeFragmentAdapter(tagD));
        fragmentE =  spy( new TestShoeFragmentAdapter(tagE));
        fragmentF =  spy( new TestShoeFragmentAdapter(tagF));
    }


    @Test
    public void testChildren(){

        shoeRack.clearHistory();
        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);
        ShoeBox shoeBoxC = ShoeBox.build(fragmentC);
        ShoeBox shoeBoxD = ShoeBox.build(fragmentD);


        shoeRack.populate( shoeBoxA.populate( shoeBoxB, shoeBoxC, shoeBoxD ));
        shoeRack.request( tagA );

        assertTrue( shoeBoxA.isActive() );
        assertFalse( shoeBoxB.isActive() );
        assertFalse( shoeBoxC.isActive() );
        assertFalse( shoeBoxD.isActive() );

        shoeRack.request( tagB );
        assertTrue( shoeBoxA.isActive() );
        assertTrue( shoeBoxB.isActive() );
        assertTrue( shoeBoxC.isActive() );
        assertTrue( shoeBoxC.isActive() );
        verify( fragmentA ).toChildVisit();

        shoeRack.goBack();

        assertTrue( shoeBoxA.isActive() );
        assertFalse( shoeBoxB.isActive() );
        assertFalse( shoeBoxC.isActive() );
        assertFalse( shoeBoxD.isActive() );

        //since we visited a child fragment and returned to its parent, then
        //we can assert we called on this method letting the parent know about it
        //even if it's still active.
        verify( fragmentA ).fromChildVisit();

        //device rotates
        shoeRack.onActivityPause();
        shoeRack.populate( shoeBoxA.populate(ShoeStack.build(shoeBoxB, shoeBoxC, shoeBoxD)));

        shoeRack.request( tagA );
        assertTrue( shoeBoxA.isActive() );
        assertFalse( shoeBoxB.isActive() );
        assertFalse( shoeBoxC.isActive() );
        assertFalse( shoeBoxD.isActive() );


        shoeRack.request( tagB );
        assertTrue( shoeBoxA.isActive() );
        assertTrue( shoeBoxB.isActive() );
        assertFalse( shoeBoxC.isActive() );
        assertFalse( shoeBoxD.isActive() );
        verify( fragmentA, times(2) ).toChildVisit();


        shoeRack.request( tagC );
        assertTrue( shoeBoxA.isActive() );
        assertFalse( shoeBoxB.isActive() );
        assertTrue( shoeBoxC.isActive() );
        assertFalse( shoeBoxD.isActive() );
        verify( fragmentA, times(2)).toChildVisit();

        shoeRack.goBack();
        assertTrue( shoeBoxA.isActive() );
        assertTrue( shoeBoxB.isActive() );
        assertFalse( shoeBoxC.isActive() );
        assertFalse( shoeBoxD.isActive() );
        verify( fragmentA, times(2)).toChildVisit();

        //checking once more..
        reset( fragmentA );
        shoeRack.goBack();
        assertTrue( shoeBoxA.isActive() );
        assertFalse( shoeBoxB.isActive() );
        assertFalse( shoeBoxC.isActive() );
        assertFalse( shoeBoxD.isActive() );
        verify( fragmentA ).fromChildVisit();
    }


    @Test
    public void testVerifyingCalls(){

        shoeRack.clearHistory();
        ShoeBox shoeBoxA = spy( ShoeBox.build(fragmentA) );
        ShoeBox shoeBoxB = spy( ShoeBox.build(fragmentB) );
        ShoeBox shoeBoxC = spy( ShoeBox.build(fragmentC) );
        ShoeBox shoeBoxD = spy( ShoeBox.build(fragmentD) );
        ShoeStack shoeStack = spy( ShoeStack.build( shoeBoxA, shoeBoxB, shoeBoxC, shoeBoxD) );

        shoeRack.populate(  shoeStack );

        verify( shoeBoxB, times(0) ).setActive( eq(false) );
        verify( shoeBoxC, times(0) ).setActive( eq(false) );
        verify( shoeBoxC, times(0) ).setActive( eq(false) );

        shoeRack.suggest( tagC );

        verify( shoeBoxC, times(1) ).setActive( eq(true) );

        shoeRack.request( tagD );
    }

    @Test
    public void refreshTest() {
        shoeRack.clearHistory();
        ShoeBox shoeBoxA = spy(ShoeBox.build(fragmentA));
        ShoeBox shoeBoxB = spy(ShoeBox.build(fragmentB));

        shoeRack.populate(shoeBoxA, shoeBoxB);
        shoeRack.suggest(tagB);
        shoeRack.request(tagB);

        verify(shoeBoxB, times(1)).setActive(eq(false));
        verify(shoeBoxB, times(2)).setActive(eq(true));
    }
}