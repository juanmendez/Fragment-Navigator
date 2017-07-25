package info.juanmendez.fragmentnavigatordemo;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

import info.juanmendez.fragmentnavigatordemo.shoeboxes.ShoeBuilder;
import info.juanmendez.shoeboxes.ShoeStorage;
import info.juanmendez.shoeboxes.shoes.ShoeBox;
import info.juanmendez.shoeboxes.shoes.ShoeRack;
import info.juanmendez.shoeboxes.shoes.ShoeStack;


/**
 * Created by Juan Mendez on 6/19/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

@EActivity(R.layout.static_stack_and_flow)
@OptionsMenu(R.menu.activity_menu)
public class StaticFragmentsActivity extends AppCompatActivity {

    @InstanceState
    String shoeRackTag;

    @Override
    protected void onStart() {
        super.onStart();

        if( shoeRackTag == null ){
            shoeRackTag = FlowWithStackActivity.class.getSimpleName() + "_" + System.currentTimeMillis();
        }

        //Activity should retain a unique tagging corresponding to shoeRack
        //in case another instance of this activity is started by another activity.
        ShoeRack shoeRack = ShoeStorage.getRack( shoeRackTag );
        ShoeBox boxA = ShoeBuilder.create(includeFragment( "A", R.id.layoutA ) );
        ShoeBox boxB = ShoeBuilder.create(includeFragment( "B", R.id.layoutB ) );
        ShoeBox boxC = ShoeBuilder.create(includeFragment( "C", R.id.layoutC ) );

        if( usesPane() ){
            //equivalent to: shoeRack.populate( ShoeFlow.build(...) )
            shoeRack.populate( boxA, boxB, boxC );
        }else{
            shoeRack.populate( ShoeStack.build( boxA, boxB, boxC ));
        }

        //if parent of boxA has no child active, then suggest the first by its tag.
        shoeRack.suggest( R.id.layoutA );
    }


    StaticFragmentLetter includeFragment( String letter, int layoutId ){

        FragmentManager fm = getSupportFragmentManager();
        StaticFragmentLetter fragment = (StaticFragmentLetter) fm.findFragmentById(layoutId);
        fragment.setLetter( letter );

        return fragment;
    }

    @Override
    public void onBackPressed() {
        if (!ShoeStorage.getCurrentRack().goBack()) {
            super.onBackPressed();
        }
    }

    @OptionsItem(R.id.stack_to_flowstack)
    void goToStackToFlowStack() {

        startActivity( new Intent(this, FlowWithStackActivity_.class));
    }

    @OptionsItem(R.id.stack_to_flow)
    void goToStackAndFLow() {
        startActivity( new Intent(this, StackAndFlowActivity_.class));
    }

    Boolean usesPane(){
        return findViewById(R.id.fragment_container ) == null;
    }

}
