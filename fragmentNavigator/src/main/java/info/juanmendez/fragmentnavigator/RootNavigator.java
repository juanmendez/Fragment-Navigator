package info.juanmendez.fragmentnavigator;

import java.util.List;

import info.juanmendez.fragmentnavigator.models.NavNode;
import info.juanmendez.fragmentnavigator.models.NavRoot;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Juan Mendez on 6/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class RootNavigator implements NavNode {

    NavRoot root = new NavRoot();

    private BehaviorSubject<String> publishSubject;
    private static RootNavigator rootNavigator = new RootNavigator();

    public static RootNavigator getInstance() {
        return rootNavigator;
    }

    public RootNavigator() {
        publishSubject = BehaviorSubject.create();
    }

    public void request(String requestedTag) {
        publishSubject.onNext( requestedTag );
    }

    public Observable<String> asObservable() {
        return publishSubject.hide();
    }


    @Override
    public NavNode applyNodes(NavNode... nodes) {
        root.applyNodes(nodes);

        return this;
    }

    @Override
    public List<NavNode> getNodes() {
        return root.getNodes();
    }

    @Override
    public void setParent(NavNode parentNode) {
    }

    @Override
    public NavNode getParent() {
        return null;
    }

    @Override
    public NavNode searchParent(String tag) {
        return root.searchParent( tag );
    }

    @Override
    public NavNode searchParent(int id) {
        return root.searchParent( id );
    }

    @Override
    public void clear() {
        root.clear();
    }
    
    @Override
    public void setVisible(Boolean show) {
        root.setVisible(show);
    }

    @Override
    public boolean getVisible() {
        return root.getVisible();
    }

    @Override
    public boolean goBack() {
        return false;
    }
}