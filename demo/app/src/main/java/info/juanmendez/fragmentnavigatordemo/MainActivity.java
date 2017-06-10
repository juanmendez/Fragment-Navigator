package info.juanmendez.fragmentnavigatordemo;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import info.juanmendez.fragmentnavigator.FragmentNav;
import info.juanmendez.fragmentnavigator.models.NavItem;
import info.juanmendez.fragmentnavigator.models.NavRoot;
import info.juanmendez.fragmentnavigator.models.NavStack;
import info.juanmendez.fragmentnavigatordemo.models.NavFragment;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @AfterViews
    public void afterViews(){

        NavRoot navRoot = new NavRoot();
        FragmentNav.setNavRoot( navRoot );

        NavFragment navA = new NavFragment(includeFragment( "A", R.id.layoutA ) );
        NavFragment navB = new NavFragment(includeFragment( "B", R.id.layoutB ) );
        NavFragment navC = new NavFragment(includeFragment( "C", R.id.layoutC ) );

        if( usesPane() ){
            navRoot.applyNodes( NavItem.build(navA), NavItem.build(navB), NavItem.build(navC) );
        }else{
            navRoot.applyNodes(NavStack.build(NavItem.build(navA), NavItem.build(navB), NavItem.build(navC) ));
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
        if (!FragmentNav.getNavRoot().goBack()) {
            super.onBackPressed();
        }
    }

    Boolean usesPane(){
        return findViewById(R.id.fragment_container ) == null;
    }
}