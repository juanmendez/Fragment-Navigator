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
import info.juanmendez.shoeboxes.models.ShoeBox;
import info.juanmendez.shoeboxes.models.ShoeRack;
import info.juanmendez.shoeboxes.models.ShoeStack;


/**
 * Created by Juan Mendez on 6/15/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

@EActivity(R.layout.flow_w_stack)
@OptionsMenu(R.menu.activity_menu)
public class FlowWithStackActivity extends AppCompatActivity {

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

        if( isSinglePane() ){
            //equivalent to: shoeRack.populate( ShoeFlow.build(...) )
            shoeRack.populate( ShoeStack.build( boxA, boxB, boxC ) );
            shoeRack.suggest( "A" );
        }else{
            shoeRack.populate( boxA, ShoeStack.build( boxB, boxC ));
            shoeRack.suggest( "A", "B" );
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
        if (!ShoeStorage.getCurrentRag().goBack()) {
            super.onBackPressed();
        }
    }

    Boolean isSinglePane(){
        return findViewById(R.id.fragment_container ) == null;
    }

    @OptionsItem(R.id.stack_to_flow)
    void goToStackToFlowStack() {
        startActivity( new Intent(this, StackAndFlowActivity_.class));
    }

    @OptionsItem(R.id.static_stack_to_flow)
    void goToStaticStackToFLow() {
        startActivity( new Intent(this, StaticFragmentsActivity_.class));
    }
}
