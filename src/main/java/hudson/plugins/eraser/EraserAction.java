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

    /**
     * Current project variable
     */
    final private AbstractProject<?, ?> project;

    /**
     * @param project
     */
    public EraserAction(AbstractProject project) {
        this.project = project;
    }

    /**
     * Current project
     * @return
     */
    public AbstractProject<?, ?> getProject() {
        return project;
    }

    /**
     * Name of plugin for side panel
     * @return
     */
    public String getDisplayName() {
        return "Erase History";
    }

    /**
     * Icon of plugin for side panel
     * @return
     */
    public String getIconFileName() {
        return hasPermission() ? "/plugin/eraser/img/icon.png" : null;
    }

    /**
     * URL of plugin for side panel
     * @return
     */
    public String getUrlName() {
        return "eraser";
    }

    /**
     * Check permission for delete
     * @return
     */
    private boolean hasPermission(){
        return Hudson.getInstance().hasPermission(project.DELETE);
    }

    /**
     * Provide list of builds for index.jelly
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
     * Delete chosen builds and redirect to back
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws NullPointerException
     */
    public void doDoDeleteChosenBuilds(StaplerRequest request, StaplerResponse response) throws IOException, NullPointerException {

        if ( hasPermission() ){

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
        }

        response.sendRedirect2(request.getContextPath());
        return;
    }


}
