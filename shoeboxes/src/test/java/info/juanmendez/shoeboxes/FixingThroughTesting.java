package info.juanmendez.shoeboxes;

import org.junit.Before;
import org.junit.Test;

import info.juanmendez.shoeboxes.adapters.ShoeFragment;
import info.juanmendez.shoeboxes.models.ShoeBox;
import info.juanmendez.shoeboxes.models.ShoeRack;
import info.juanmendez.shoeboxes.models.ShoeStack;
import info.juanmendez.shoeboxes.models.TestShoeFragment;

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

    ShoeFragment fragmentA;
    ShoeFragment fragmentB;
    ShoeFragment fragmentC;
    ShoeFragment fragmentD;
    ShoeFragment fragmentE;
    ShoeFragment fragmentF;
    ShoeRack shoeRack;

    @Before
    public void before(){
        fragmentA = new TestShoeFragment(tagA);
        fragmentB = new TestShoeFragment(tagB);
        fragmentC = new TestShoeFragment(tagC);
        fragmentD = new TestShoeFragment(tagD);
        fragmentE = new TestShoeFragment(tagE);
        fragmentF = new TestShoeFragment(tagF);
    }


    /**
     * there was an error when switching to another activity,
     * and returning back.
     */
    @Test
    public void shouldUpdateAfter(){
        shoeRack = ShoeStorage.setTag( "1");
        ShoeBox shoeBoxA = ShoeBox.build(fragmentA);
        ShoeBox shoeBoxB = ShoeBox.build(fragmentB);
        ShoeBox shoeBoxC = ShoeBox.build(fragmentC);
        shoeRack.populate( shoeBoxA, shoeBoxB, shoeBoxC );
        shoeRack.request(tagA);
        shoeRack.request(tagB);
        shoeRack.request(tagC);

        shoeRack = ShoeStorage.setTag( "2" );
        ShoeBox shoeBoxD = ShoeBox.build(fragmentD);
        ShoeBox shoeBoxE = ShoeBox.build(fragmentE);
        ShoeBox shoeBoxF = ShoeBox.build(fragmentF);
        shoeRack.populate( shoeBoxD, ShoeStack.build( shoeBoxE, shoeBoxF) );
        shoeRack.suggest( tagD, tagE );


        shoeRack = ShoeStorage.setTag( "1");
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