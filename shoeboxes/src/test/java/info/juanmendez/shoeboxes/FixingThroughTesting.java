package info.juanmendez.shoeboxes;

import org.junit.Before;
import org.junit.Test;

import info.juanmendez.shoeboxes.adapters.ShoeFragmentAdapter;
import info.juanmendez.shoeboxes.shoes.ShoeBox;
import info.juanmendez.shoeboxes.shoes.ShoeRack;
import info.juanmendez.shoeboxes.shoes.ShoeStack;
import info.juanmendez.shoeboxes.shoes.TestShoeFragmentAdapter;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class FixingThroughTesting {

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
        fragmentA = new TestShoeFragmentAdapter(tagA);
        fragmentB = new TestShoeFragmentAdapter(tagB);
        fragmentC = new TestShoeFragmentAdapter(tagC);
        fragmentD = new TestShoeFragmentAdapter(tagD);
        fragmentE = new TestShoeFragmentAdapter(tagE);
        fragmentF = new TestShoeFragmentAdapter(tagF);
    }


    /**
     * there was an error when switching to another activity,
     * and returning back.
     */
    @Test
    public void shouldUpdateAfter(){
        shoeRack = ShoeStorage.getRack( "1");
        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);
        ShoeBox shoeBoxC = ShoeBox.build(fragmentC);
        shoeRack.populate( shoeBoxA, shoeBoxB, shoeBoxC );
        shoeRack.request(tagA);
        shoeRack.request(tagB);
        shoeRack.request(tagC);

        shoeRack = ShoeStorage.getRack( "2" );
        ShoeBox shoeBoxD = ShoeBox.build(fragmentD);
        ShoeBox shoeBoxE = ShoeBox.build(fragmentE);
        ShoeBox shoeBoxF = ShoeBox.build(fragmentF);
        shoeRack.populate( shoeBoxD, ShoeStack.build( shoeBoxE, shoeBoxF) );
        shoeRack.suggest( tagD, tagE );


        shoeRack = ShoeStorage.getRack( "1");
        shoeBoxA = ShoeBox.build(fragmentA);
        shoeBoxB = ShoeBox.build(fragmentB);
        shoeBoxC = ShoeBox.build(fragmentC);
        shoeRack.populate( shoeBoxA, shoeBoxB, shoeBoxC );
        shoeRack.suggest( tagA );


        assertTrue( shoeBoxA.isActive() );
        assertTrue( shoeBoxB.isActive() );
        assertTrue( shoeBoxC.isActive() );
    }
}