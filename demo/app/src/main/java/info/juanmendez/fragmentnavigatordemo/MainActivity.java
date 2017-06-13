package info.juanmendez.fragmentnavigatordemo;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import info.juanmendez.fragmentnavigator.ShoeStorage;
import info.juanmendez.fragmentnavigator.models.ShoeBox;
import info.juanmendez.fragmentnavigator.models.ShoeRack;
import info.juanmendez.fragmentnavigator.models.ShoeStack;
import info.juanmendez.fragmentnavigatordemo.models.NavFragment;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @AfterViews
    public void afterViews(){

        ShoeRack navRoot = new ShoeRack();
        ShoeStorage.setShoeRack( navRoot );

        NavFragment navA = new NavFragment(includeFragment( "A", R.id.layoutA ) );
        NavFragment navB = new NavFragment(includeFragment( "B", R.id.layoutB ) );
        NavFragment navC = new NavFragment(includeFragment( "C", R.id.layoutC ) );

        if( usesPane() ){
            navRoot.applyNodes( ShoeBox.build(navA), ShoeBox.build(navB), ShoeBox.build(navC) );
        }else{
            navRoot.applyNodes(ShoeStack.build(ShoeBox.build(navA), ShoeBox.build(navB), ShoeBox.build(navC) ));
        }
    }


    FragmentLetter includeFragment( String letter, int layoutId ){

        FragmentManager fm = getSupportFragmentManager();
        FragmentLetter fragment = (FragmentLetter) fm.findFragmentByTag(letter);

        if( fm.findFragmentByTag(letter) == null ){
            fragment = FragmentLetter_.builder().letter( letter ).build();
        }

        if( !fragment.isAdded() ){
            fm.beginTransaction().add( layoutId, fragment, letter ).commit();
        }


        return fragment;
    }

    @Override
    public void onBackPressed() {
        if (!ShoeStorage.getShoeRack().goBack()) {
            super.onBackPressed();
        }
    }

    Boolean usesPane(){
        return findViewById(R.id.fragment_container ) == null;
    }
}