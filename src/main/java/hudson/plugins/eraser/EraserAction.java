package hudson.plugins.eraser;

import hudson.model.Hudson;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Run;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import java.io.IOException;
import java.util.*;

public class EraserAction implements Action {

    final private AbstractProject<?, ?> project;

    /**
     * @param project
     */
    public EraserAction(AbstractProject project) {
        this.project = project;
    }

    public AbstractProject<?, ?> getProject() {
        return project;
    }

    public String getDisplayName() {
        return "Erase History";
    }

    public String getIconFileName() {
        return "/plugin/eraser/img/icon.png";
    }

    public String getUrlName() {
        return "eraser";
    }

    private boolean hasPermission(){
        return Hudson.getInstance().hasPermission(project.DELETE);
    }

    /**
     * index.jelly
     *
     * @return
     * @throws NullPointerException
     */
    public Collection<AbstractBuild> getBuilds() throws NullPointerException {

        ArrayList<AbstractBuild> list = new ArrayList<AbstractBuild>();

        for (Object o : getProject().getBuilds()) {
            AbstractBuild<?, ?> build = (AbstractBuild<?, ?>) o;
            list.add(build);
        }

        return list;
    }

    /**
     * POST form request
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws NullPointerException
     */
    public void doDoDeleteChosenBuilds(StaplerRequest request, StaplerResponse response) throws IOException, NullPointerException {

        Map<Integer, Object> chosen = new HashMap<Integer, Object>();

        for (String number : request.getParameterValues("builds") ) {
            chosen.put( Integer.parseInt(number), true );
        }

        for (Object o : getProject().getBuilds()) {
            AbstractBuild<?, ?> build = (AbstractBuild<?, ?>) o;
            if (chosen.get(build.getNumber()) != null ) {
                build.delete();
            }
        }

        response.sendRedirect2(request.getContextPath());
        return;
    }


}
