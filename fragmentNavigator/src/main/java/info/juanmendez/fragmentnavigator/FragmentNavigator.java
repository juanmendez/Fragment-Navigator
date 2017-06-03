package info.juanmendez.fragmentnavigator;

import info.juanmendez.fragmentnavigator.models.Stack;
import info.juanmendez.fragmentnavigator.models.NavNode;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class FragmentNavigator {

    Stack fragmentNodeRoot = Stack.build();
    private BehaviorSubject<String> publishSubject;
    private static FragmentNavigator fragmentNavigator = new FragmentNavigator();

    public static FragmentNavigator getInstance() {
        return fragmentNavigator;
    }

    public FragmentNavigator() {
        publishSubject = BehaviorSubject.create();
    }

    public Stack getFragmentNodeRoot() {
        return fragmentNodeRoot;
    }

    public void setFragmentNodeRoot(NavNode... childNodeArray) {
        this.fragmentNodeRoot = Stack.build(childNodeArray);
    }

    public void request(String requestedTag) {
        publishSubject.onNext( requestedTag );
    }

    public Observable<String> asObservable() {
        return publishSubject.hide();
    }

    public NavNode searchContainer(String s) {
        return null;
    }
}
