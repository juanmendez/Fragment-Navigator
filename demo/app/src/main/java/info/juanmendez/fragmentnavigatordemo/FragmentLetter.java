package info.juanmendez.fragmentnavigatordemo;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import info.juanmendez.fragmentnavigator.FragmentNav;


/**
 * Created by Juan Mendez on 6/9/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@EFragment(value=R.layout.fragment_layout)
public class FragmentLetter extends Fragment {

    @FragmentArg
    String letter;

    @ViewById
    TextView txtLayoutName;

    @ViewById
    Button requestA, requestB, requestC;

    @AfterViews
    void afterViews(){
        txtLayoutName.setText( letter );

        switch( letter ){
            case "A": requestA.setVisibility(View.GONE); break;
            case "B": requestB.setVisibility(View.GONE); break;
            case "C": requestC.setVisibility(View.GONE); break;
        }
    }

    @Click
    void requestA(){
        FragmentNav.getNavRoot().request( "A" );
    }

    @Click
    void requestB(){
        FragmentNav.getNavRoot().request( "B" );
    }

    @Click
    void requestC(){
        FragmentNav.getNavRoot().request( "C" );
    }

}
