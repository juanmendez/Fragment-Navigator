package info.juanmendez.fragmentnavigatordemo;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import info.juanmendez.shoeboxes.ShoeStorage;


/**
 * Created by Juan Mendez on 6/19/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@EFragment(value=R.layout.fragment_layout)
public class StaticFragmentLetter extends Fragment {

    @ViewById
    TextView txtLayoutName;

    @ViewById
    Button requestA, requestB, requestC;

    String letter;

    public void setLetter(String letter) {

        this.letter = letter;
        txtLayoutName.setText( letter );

        switch( letter ){
            case "A": requestA.setVisibility(View.GONE); break;
            case "B": requestB.setVisibility(View.GONE); break;
            case "C": requestC.setVisibility(View.GONE); break;
        }
    }

    @Click
    void requestA(){
        ShoeStorage.getCurrentRag().request( R.id.layoutA );
    }

    @Click
    void requestB(){
        ShoeStorage.getCurrentRag().request( R.id.layoutB  );
    }

    @Click
    void requestC(){
        ShoeStorage.getCurrentRag().request( R.id.layoutC  );
    }
}
