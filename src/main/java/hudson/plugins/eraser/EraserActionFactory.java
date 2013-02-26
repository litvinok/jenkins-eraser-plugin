package hudson.plugins.eraser;

import hudson.Extension;
import hudson.model.TransientProjectActionFactory;
import hudson.model.AbstractProject;

import java.util.ArrayList;
import java.util.Collection;

@Extension
public class EraserActionFactory extends TransientProjectActionFactory {

    /**
     * Add action of plugin to projects
     * @param target
     * @return
     */
    @Override
    public Collection<EraserAction> createFor(AbstractProject target){
        ArrayList<EraserAction> ta = new ArrayList<EraserAction>();
        ta.add(new EraserAction(target));
        return ta;
    }

}
