package org.kelvin.webapp.standAlone;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class Launcher {
    private static int portNumber = 8080;

    public static void main(String[] args) throws Exception {
        new Launcher().start();
    }

    public void start() throws Exception {
        final ResourceConfig resourceConfig = new ResourceConfig(ServerGateway.class);
        resourceConfig.packages("org.kelvin.webapp");
        resourceConfig.register(MultiPartFeature.class);

        ServletHolder jerseyServlet
                = new ServletHolder(new ServletContainer(resourceConfig));

        Server jettyServer = new Server(portNumber);
        ServletContextHandler context = new ServletContextHandler(jettyServer, "/");
        context.addServlet(jerseyServlet, "/*");


        StringBuilder hostNameLink = new StringBuilder();
        String hostPath = "http://localhost:"+portNumber+ ServerGateway.path;
        hostNameLink.append("\n")
                .append("Server Launched at: ")
                .append(hostPath)
                .append("\n(Test Server: ")
                .append(hostPath)
                .append("/")
                .append("Server%20is%20running!");
        try {
            jettyServer.start();
            System.out.println(hostNameLink.toString());
            jettyServer.join();
        }catch (Exception e){
            System.out.println(e.toString());
        }finally {
            jettyServer.destroy();
        }
    }
}