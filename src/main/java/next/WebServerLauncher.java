package next;

import java.io.File;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServerLauncher {
    private static final Logger logger = LoggerFactory.getLogger(WebServerLauncher.class);

    public static void main(String[] args) throws Exception {
        String webappDirLocation = "webapp/";
        Tomcat tomcat = new Tomcat();

        String webPort = System.getenv("PORT");
        if(webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }

        tomcat.setPort(Integer.parseInt(webPort));

        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        File additionWebInfClasses = new File("build/classes/java/main");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));

        logger.info("configuring app with basedir: {}", new File("./" + webappDirLocation).getAbsolutePath());

        ctx.setResources(resources);
        tomcat.start();
        tomcat.getServer().await();
    }
}
