package webserver;

import java.io.File;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServerLauncher {

	private static final Logger log = LoggerFactory.getLogger(WebServerLauncher.class);

	public static void main(String[] args) throws LifecycleException {
		String webappDirLocation = "webapp/";
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);
//		String webPort = System.getenv("PORT");
//		if (webPort == null || webPort.isEmpty()) {
//			webPort = "8080";
//		}

		Connector connector = tomcat.getConnector();
		connector.setURIEncoding("UTF-8");
		tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
		log.info("configuring app with basedir:  {}", new File("./" + webappDirLocation).getAbsolutePath());
		tomcat.start();
		tomcat.getServer().await();
	}

}
