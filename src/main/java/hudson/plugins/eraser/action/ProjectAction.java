package hudson.plugins.eraser.action;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Run;
import hudson.security.Permission;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import java.io.IOException;
import java.util.*;

public final class ProjectAction implements Action {

    public final AbstractProject<?, ?> project;
    private String fieldsToVisualize;

    /**
     * @param project
     * @param fieldsToVisualize
     */
    public ProjectAction(AbstractProject project, String fieldsToVisualize) {
        this.project = project;
        this.fieldsToVisualize = fieldsToVisualize;
    }

    public AbstractProject<?, ?> getProject() {
        return project;
    }

    public String getDisplayName() {
        return "Erase History";
    }

    public String getIconFileName() {
        //project.checkPermission(Permission.DELETE);
        return "/plugin/eraser/img/icon.png";
    }

    public String getUrlName() {
        return "eraser";
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

        project.checkPermission(Permission.DELETE);

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
