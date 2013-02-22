package hudson.plugins.eraser.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Run;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import java.io.IOException;
import java.util.*;

public final class ProjectAction implements Action {

    public final AbstractProject<?, ?> project;
    private String fieldsToVisualize;

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
        return "/plugin/eraser/img/icon.png";
    }

    public String getUrlName() {
        return "eraser";
    }

    //index.jelly
    public Collection<AbstractBuild> getBuilds() {
        ArrayList<AbstractBuild> list = new ArrayList<AbstractBuild>();

        for (Object o : getProject().getBuilds()) {
            AbstractBuild<?, ?> build = (AbstractBuild<?, ?>) o;

            list.add(build);
            /*BuildAction action = build.getAction(BuildAction.class);
            if (action != null && action.getReports() != null) {
                set.addAll(action.getReports());
            }    */
        }
        return list;
    }

    public void doDeleteBuild(StaplerRequest request, StaplerResponse response) throws IOException {

        int number = Integer.parseInt(request.getParameter("number"));
        if (number <= 0) return;

        Map<String, Object> r = new HashMap<String, Object>();
        Run build = getProject().getBuildByNumber(number);

        try {
            build.delete();
            r.put("ok", true);
        } catch ( Exception e ) {
            r.put("error", e.getMessage());
        }

        Gson gson = new GsonBuilder().create();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(r));
    }


}
