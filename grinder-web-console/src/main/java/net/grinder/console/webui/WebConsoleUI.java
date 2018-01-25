package net.grinder.console.webui;

import net.grinder.common.Test;
import net.grinder.console.ConsoleFoundation;
import net.grinder.console.common.ConsoleException;
import net.grinder.console.common.ErrorHandler;
import net.grinder.console.common.Resources;
import net.grinder.console.communication.ProcessControl;
import net.grinder.console.distribution.FileDistribution;
import net.grinder.console.model.*;
import org.slf4j.Logger;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import java.util.*;


public class WebConsoleUI implements ConsoleFoundation.UI {

    private static WebConsoleUI INSTANCE;

    protected Resources resources;
    protected ConsoleProperties consoleProperties;
    protected final SampleModel model;
    protected final SampleModelViews sampleModelViews;
    protected ProcessControl processControl;
    protected FileDistribution fileDistribution;
    protected ProcessControl.ProcessReports[] m_processReports;
    //protected Set<Test> testSet = null;
    protected ModelTestIndex testIndex = null;

    protected Logger logger;

    public static WebConsoleUI getInstance() {
        return INSTANCE;
    }

    public WebConsoleUI(Resources resources,
                        ConsoleProperties consoleProperties,
                        SampleModel modelParam,
                        SampleModelViews sampleModelViewsParam,
                        ProcessControl processControl,
                        FileDistribution fileDistribution,
                        Logger loggerParam)
            throws ConsoleException    {
        try {
            this.logger = loggerParam;
            this.resources = resources;
            this.consoleProperties = consoleProperties;
            this.model = modelParam;
            this.sampleModelViews = sampleModelViewsParam;
            this.processControl = processControl;
            this.fileDistribution = fileDistribution;

            this.processControl.addProcessStatusListener(
                    new ProcessControl.Listener() {
                        public void update(ProcessControl.ProcessReports[] processReports) {
                            m_processReports = processReports;
                        }
                    }
            );

            this.model.addModelListener(new SampleModel.AbstractListener() {
                public void newTests(Set<Test> newTests, ModelTestIndex modelTestIndex) {
                    //testSet   = newTests;
                    testIndex = modelTestIndex;
                }
            });
            INSTANCE=this;
            SpringApplication app = new SpringApplication(SpringBootApp.class);
            app.setBannerMode(Banner.Mode.OFF);
            app.run(new String[]{});
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ConsoleException(e.getMessage());
        }
    }


    @Override
    public ErrorHandler getErrorHandler() {
        return null;
    }
}