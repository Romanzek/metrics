package io.dropwizard.metrics5.servlets;

import io.dropwizard.metrics5.MetricRegistry;
import io.dropwizard.metrics5.health.HealthCheckRegistry;
import static org.assertj.core.api.Assertions.assertThat;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.servlet.ServletTester;
import org.junit.Before;
import org.junit.Test;

public class AdminServletExclusionTest extends AbstractServletTest {
    private final MetricRegistry registry = new MetricRegistry();
    private final HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();

    @Override
    protected void setUp(ServletTester tester) {
        tester.setContextPath("/context");

        tester.setAttribute("io.dropwizard.metrics5.servlets.MetricsServlet.registry", registry);
        tester.setAttribute("io.dropwizard.metrics5.servlets.HealthCheckServlet.registry", healthCheckRegistry);
        tester.setInitParameter("threads-enabled", "false");
        tester.setInitParameter("cpu-profile-enabled", "false");
        tester.addServlet(AdminServlet.class, "/admin");
    }

    @Before
    public void setUp() {
        request.setMethod("GET");
        request.setURI("/context/admin");
        request.setVersion("HTTP/1.0");
    }

    @Test
    public void returnsA200() throws Exception {
        processRequest();

        assertThat(response.getStatus())
                .isEqualTo(200);
        assertThat(response.getContent())
                .isEqualTo(String.format(
                        "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"%n" +
                                "        \"http://www.w3.org/TR/html4/loose.dtd\">%n" +
                                "<html>%n" +
                                "<head>%n" +
                                "  <title>Metrics</title>%n" +
                                "</head>%n" +
                                "<body>%n" +
                                "  <h1>Operational Menu</h1>%n" +
                                "  <ul>%n" +
                                "    <li><a href=\"/context/admin/metrics?pretty=true\">Metrics</a></li>%n" +
                                "    <li><a href=\"/context/admin/ping\">Ping</a></li>%n" +
                                "    <li><a href=\"/context/admin/healthcheck?pretty=true\">Healthcheck</a></li>%n" +
                                "  </ul>%n" +
                                "</body>%n" +
                                "</html>%n"
                ));
        assertThat(response.get(HttpHeader.CONTENT_TYPE))
                .isEqualTo("text/html;charset=UTF-8");
    }
}
