package info.juanmendez.fragmentnavigatordemo;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

import info.juanmendez.fragmentnavigatordemo.shoeboxes.ShoeBuilder;
import info.juanmendez.shoeboxes.ShoeStorage;
import info.juanmendez.shoeboxes.models.ShoeBox;
import info.juanmendez.shoeboxes.models.ShoeRack;
import info.juanmendez.shoeboxes.models.ShoeStack;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.activity_menu)
public class StackAndFlow extends AppCompatActivity {


    @Override
    protected void onStart() {
        super.onStart();

        ShoeRack shoeRack = ShoeStorage.getRack( StackAndFlow.class.getSimpleName() );
        ShoeBox boxA = ShoeBuilder.create(includeFragment( "A", R.id.layoutA ) );
        ShoeBox boxB = ShoeBuilder.create(includeFragment( "B", R.id.layoutB ) );
        ShoeBox boxC = ShoeBuilder.create(includeFragment( "C", R.id.layoutC ) );

        if( usesPane() ){
            //equivalent to: shoeRack.populate( ShoeFlow.build(...) )
            shoeRack.populate( boxA, boxB, boxC );
        }else{
            shoeRack.populate( ShoeStack.build( boxA, boxB, boxC ));

            //we suggest to have boxA on display..
            shoeRack.suggest( "A" );
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
        if (!ShoeStorage.getLatestRack().goBack()) {
            super.onBackPressed();
        }
    }

    @OptionsItem(R.id.stack_to_flowstack)
    void goToStackToFlowStack() {
        startActivity( new Intent(this, FlowWithStackActivity_.class));
    }

    Boolean usesPane(){
        return findViewById(R.id.fragment_container ) == null;
    }
}