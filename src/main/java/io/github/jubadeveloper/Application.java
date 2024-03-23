package io.github.jubadeveloper;

import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;
import java.util.EnumSet;
import java.util.IllegalFormatConversionException;

@Configuration
@PropertySource("classpath:/application.properties")
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private static final int DEFAULT_PORT = 8080;
    private static final String CONTEXT_PATH = "/";
    private static final String CONFIG_LOCATION = "io.github.jubadeveloper.configuration";
    private static final String MAPPING_URL = "/*";

    public static void main(String[] args) throws Exception {
        new Application()
                .startJetty(extractPortFromArgsOrFallToDefault(args));
    }

    private void startJetty(int serverPort) throws Exception {
        Server server = new Server(serverPort);
        server.setHandler(getServletContextHandler(getContext()));
        server.start();
        server.join();
    }


    private static ServletContextHandler getServletContextHandler(WebApplicationContext context) throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setErrorHandler(null);
        contextHandler.setContextPath(CONTEXT_PATH);
        contextHandler.addServlet(new ServletHolder(new DispatcherServlet(context)), MAPPING_URL);
        contextHandler.addEventListener(new ContextLoaderListener(context));
        // Delegates to Spring Security Chain Filter bean registered in the context
        DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy("springSecurityFilterChain", context);
        // Hold the DelegatingFilterProxy
        FilterHolder filterHolder = new FilterHolder(delegatingFilterProxy);
        // Delegates every request to Spring Security Filter Chain
        contextHandler.addFilter(filterHolder,"/*", EnumSet.of(DispatcherType.REQUEST));
        return contextHandler;
    }

    private static WebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION);
        return context;
    }

    private static int extractPortFromArgsOrFallToDefault (String[] args) {
        int serverPort = DEFAULT_PORT;
        for (String arg: args) {
            String[] splitArg = arg.split("=");
            if (splitArg[0].equals("--server.port")) {
                try {
                    serverPort = Integer.parseInt(splitArg[1]);
                } catch (IndexOutOfBoundsException | IllegalFormatConversionException ignore) {}
            }
        }
        return serverPort;
    }
}