package net.chrissearle.web.helloworld;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class HelloWorld extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int income = Integer.parseInt(req.getParameter("income"));

        String url = "https://cfs-ws-itera.cicero.no/cfp/6/ws/rest/pension/calculateExpectedPension?calculateExpectedPension_0&pensionInput=";
        String json =   getJSONPayload(income);


        String reply = executeGet(url+json);

        int index = reply.indexOf("\"sum\":");
        int stop = reply.indexOf(",", index) - 1;
        String number =           reply.substring(index + "sum:".length() + 2, stop);
        double sum = Double.parseDouble(number);
        System.out.println(reply);
        resp.getWriter().print(sum);
    }

    private static String getJSONPayload(int income) {
        String payload =   "{\"birthYear\": 1980, \"incomePeriods\": [{\"endYear\": 2045, \"incomePerYear\": " +
                + income +
                ", \"startYear\": 2002}], \"obligatoriskTjenestePensjon\": {\"bidragFlat\": 0, \"bidragSteg1\": 0.02, \"bidragSteg2\": 0.02, \"forventetAvkastningProsent\": 0.04, \"innskuddFraAar\": 2006, \"otpType\": \"type1\", \"utbetalingFraAar\": 2047, \"utbetalingVarighet\": 10}, \"pensionPercentages\": [{\"aar\": 2047, \"verdi\": 1}]}";
           return URLEncoder.encode(payload);
    }

    public static String executeGet(String url)
    {
        StringBuilder sb = new StringBuilder();
        URL yahoo = null;
        try {
            yahoo = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection yc = null;
        try {
            yc = yahoo.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(
                            yc.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String inputLine;

        try {
            while ((inputLine = in.readLine()) != null)
               sb.append(inputLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new HelloWorld()), "/*");
        server.start();
        server.join();

    }
}