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
import static org.mockito.Mockito.reset;
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
        fragmentA = spy( new TestShoeFragmentAdapter(tagA) );
        fragmentB = new TestShoeFragmentAdapter(tagB);
        fragmentC = new TestShoeFragmentAdapter(tagC);
        fragmentD = new TestShoeFragmentAdapter(tagD);
        fragmentE = new TestShoeFragmentAdapter(tagE);
        fragmentF = new TestShoeFragmentAdapter(tagF);
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

        shoeRack.goBack();

        assertTrue( shoeBoxA.isActive() );
        assertFalse( shoeBoxB.isActive() );
        assertFalse( shoeBoxC.isActive() );
        assertFalse( shoeBoxD.isActive() );

        //since we visited a child fragment and returned to its parent, then
        //we can assert we called on this method letting the parent know about it
        //even if it's still active.
        verify( fragmentA ).returnFromChildVisit();

        //device rotates
        shoeRack.onRotation();
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


        shoeRack.request( tagC );
        assertTrue( shoeBoxA.isActive() );
        assertFalse( shoeBoxB.isActive() );
        assertTrue( shoeBoxC.isActive() );
        assertFalse( shoeBoxD.isActive() );

        shoeRack.goBack();
        assertTrue( shoeBoxA.isActive() );
        assertTrue( shoeBoxB.isActive() );
        assertFalse( shoeBoxC.isActive() );
        assertFalse( shoeBoxD.isActive() );
        
        //checking once more..
        reset( fragmentA );
        shoeRack.goBack();
        assertTrue( shoeBoxA.isActive() );
        assertFalse( shoeBoxB.isActive() );
        assertFalse( shoeBoxC.isActive() );
        assertFalse( shoeBoxD.isActive() );
        verify( fragmentA ).returnFromChildVisit();
    }

}