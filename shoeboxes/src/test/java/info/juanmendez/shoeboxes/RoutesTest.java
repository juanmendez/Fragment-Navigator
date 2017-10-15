package info.juanmendez.shoeboxes;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.juanmendez.shoeboxes.adapters.ShoeFragmentAdapter;
import info.juanmendez.shoeboxes.shoes.ShoeRack;
import info.juanmendez.shoeboxes.shoes.TestShoeFragmentAdapter;
import info.juanmendez.shoeboxes.utils.ShoeUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Juan Mendez on 10/14/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * We want to take navigation to the next level by introducing routes.
 * Prior to route we already have created the regex for this to work.
 * https://regexr.com/3gvcu
 */

public class RoutesTest {

    String tagA = "fragmentA";
    String tagB = "fragment_B";
    String tagC = "fragment-c";
    String tagD = "12345";

    String routeA = tagA + "/12";
    String routeB = tagB;
    String routeC = tagC + "/12/13/14";
    String routeD = tagD + "/123/12";

    ShoeFragmentAdapter fragmentA;
    ShoeFragmentAdapter fragmentB;
    ShoeFragmentAdapter fragmentC;
    ShoeFragmentAdapter fragmentD;

    //lets find the tag from the route
    String tagRegex = "^([\\w-]+)/?(.*)";
    Pattern tagPattern = Pattern.compile( tagRegex );

    ShoeRack shoeRack;

    @Before
    public void before(){
        fragmentA = new TestShoeFragmentAdapter(tagA);
        fragmentB = new TestShoeFragmentAdapter(tagB);
        fragmentC = new TestShoeFragmentAdapter(tagC);
        fragmentD = new TestShoeFragmentAdapter(tagD);
    }


    @Test
    public void testRouteRegex(){

        assertTrue( routeA.matches( tagRegex ));
        assertTrue( routeB.matches( tagRegex ));
        assertTrue( routeC.matches( tagRegex ));
        assertTrue( routeD.matches( tagRegex ));
    }

    @Test
    public void checkMatchRoute(){
        Matcher m = tagPattern.matcher(routeA);
        assertTrue( m.find() );
        assertEquals( m.group(1), tagA );

        m = tagPattern.matcher(routeB);
        assertTrue( m.find() );
        assertEquals( m.group(1), tagB );

        m = tagPattern.matcher(routeC);
        assertTrue( m.find() );
        assertEquals( m.group(1), tagC );

        m = tagPattern.matcher(routeD);
        assertTrue( m.find() );
        assertEquals( m.group(1), tagD );

        //with method generated from this test.
        assertTrue(ShoeUtils.isTagInRoute( tagA, routeA ));
        assertTrue(ShoeUtils.isTagInRoute( tagB, routeB ));
        assertTrue(ShoeUtils.isTagInRoute( tagC, routeC ));
        assertTrue(ShoeUtils.isTagInRoute( tagD, routeD ));

        assertFalse( ShoeUtils.isTagInRoute("address", "addresses/a"));
        assertFalse( ShoeUtils.isTagInRoute("addresses", "address/a"));
        assertFalse( ShoeUtils.isTagInRoute("addresses", "address"));
        assertFalse( ShoeUtils.isTagInRoute("address", "addresses"));
    }

    @Test
    public void getTheRouteParams(){
        Matcher m;

        m = tagPattern.matcher(routeA);
        assertTrue( m.find() );
        assertEquals( m.group(2).split("/").length, 1 );

        m = tagPattern.matcher(routeB);
        assertTrue( m.find() );
        assertEquals( m.group(2), "");
        assertEquals( m.group(2).split("/").length, 1 );

        m = tagPattern.matcher(routeC);
        assertTrue( m.find() );
        assertEquals( m.group(2).split("/").length, 3 );

        m = tagPattern.matcher(routeD);
        assertTrue( m.find() );
        assertEquals( m.group(2).split("/").length, 2 );
    }

    @Test
    public void tagInHistory(){
        List<String> history = new ArrayList();
        history.add( routeA );
        history.add( routeB );
        history.add( routeC );
        history.add( routeD );

        assertTrue( ShoeUtils.isTagInRouteList( tagA, history));
        assertTrue( ShoeUtils.isTagInRouteList( tagB, history));
        assertTrue( ShoeUtils.isTagInRouteList( tagC, history));
        assertTrue( ShoeUtils.isTagInRouteList( tagD, history));
        assertFalse( ShoeUtils.isTagInRouteList( "fragmentE", history));
    }

    @Test
    public void testGettingRoute(){
        assertEquals( ShoeUtils.getRouteParams(routeB), "");
        assertEquals( ShoeUtils.getRouteParams(routeC), "12/13/14");
    }

    @Test
    public void testGettingRouteFromHistory(){
        List<String> history = new ArrayList();
        history.add( routeA );
        history.add( routeB );
        history.add( routeC );
        history.add( routeD );

        assertEquals( ShoeUtils.getRouteParams(tagA, history), "12");
        assertEquals( ShoeUtils.getRouteParams(tagB, history), "");
        assertEquals( ShoeUtils.getRouteParams(tagC, history), "12/13/14");
    }
}