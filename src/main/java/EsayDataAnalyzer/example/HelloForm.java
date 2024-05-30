package EsayDataAnalyzer.example;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloForm extends HttpServlet {
    static String[] entity;
    static String[] row, line;
    static String[][] KT;
    static String s;
    private static final long serialVersionUID = 1L;

    public HelloForm() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        String fileinput = request.getParameter("fileinput");
        String graphtype = request.getParameter("graphtype");
        String data = request.getParameter("file-output");
        List<String[]> list = new ArrayList<>();
        String splitBy = ",";
        line = data.split("\\R");
        String[] labels = line[0].split(splitBy);

        try (Scanner scanner = new Scanner(data)) {
            while (scanner.hasNextLine()) {
                String row = scanner.nextLine();
                String[] values = row.split(",");
                for (int a = 0; a < values.length; a++) {
                    if (list.size() <= a) {
                        list.add(new String[0]);
                    }
                    list.set(a, Arrays.copyOf(list.get(a), list.get(a).length + 1));
                    list.get(a)[list.get(a).length - 1] = values[a];
                }
            }
        }

        String[][] d1 = new String[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            String[] arr = list.get(i);
            d1[i] = Arrays.copyOfRange(arr, 1, arr.length);
        }

        StringBuilder datasets = new StringBuilder();
        StringBuilder xaxis = new StringBuilder();

        for (int i = 0; i < d1[0].length; i++) {
            xaxis.append("'").append(d1[0][i]).append("'");
            if (i < d1[0].length - 1)
                xaxis.append(",");
        }

        Random random = new Random();
        int[] randomNumbers = new int[3];

        if (graphtype.equals("pie") || graphtype.equals("doughnut")) {
            datasets.append("{")
                    .append("label:");
            for (int o = 1; o < d1.length; o++) {
                datasets.append("'").append(labels[o]).append("',");
            }
            datasets.append("backgroundColor: [");
            for (int c = 0; c < d1[0].length; c++) {
                for (int i = 0; i < 3; i++) {
                    randomNumbers[i] = random.nextInt(256);
                }
                datasets.append("'rgb(").append(randomNumbers[0]).append(",")
                        .append(randomNumbers[1]).append(",").append(randomNumbers[2])
                        .append(")',");
            }
            datasets.append("],")
                    .append("data:").append(Arrays.deepToString(d1[1])).append(",")
                    .append("}");
        } else {
            for (int o = 1; o < d1.length; o++) {
                for (int i = 0; i < 3; i++) {
                    randomNumbers[i] = random.nextInt(256);
                }
                datasets.append("{")
                        .append("label: '").append(labels[o]).append("',")
                        .append("backgroundColor: 'rgba(").append(randomNumbers[0]).append(",")
                        .append(randomNumbers[1]).append(",").append(randomNumbers[2]).append(",0.5)',")
                        .append("borderColor: 'rgba(").append(randomNumbers[0]).append(",")
                        .append(randomNumbers[1]).append(",").append(randomNumbers[2]).append(",1)',")
                        .append("data:").append(Arrays.deepToString(d1[o])).append(",");
                if (o < d1.length - 1) {
                    datasets.append("},");
                } else {
                    datasets.append("}");
                }
            }
        }

        String htmlOutput = "<!DOCTYPE html>\r\n"
                + "<html>\r\n"
                + "    <head>\r\n"
                + "        <title>Landing Page</title>\r\n"
                + "        <link rel=\"stylesheet\" href=\"landing.css\">\r\n"
                + "    </head>\r\n"
                + "    <body>\r\n"
                + "        <h1>Thanks for using <span>EasyDataAnalyzer</span>!</h1>\r\n"
                + "        <p>Here is your data visualized</p>\r\n"
                + "        <div>\r\n"
                + "            <canvas id=\"myChart\"></canvas>\r\n"
                + "        </div>\r\n"
                + "        <script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>\r\n"
                + "        <script>\r\n"
                + "            const ctx = document.getElementById('myChart');\r\n"
                + "            new Chart(ctx, {\r\n"
                + "              type: '" + graphtype + "',\r\n"
                + "              data: {\r\n"
                + "                labels: [" + xaxis.toString() + "],\r\n"
                + "                datasets: [" + datasets.toString() + "]\r\n"
                + "              },\r\n"
                + "              options: {\r\n"
                + "                scales: {\r\n"
                + "                  y: {\r\n"
                + "                    beginAtZero: true\r\n"
                + "                  }\r\n"
                + "                }\r\n"
                + "              }\r\n"
                + "            });\r\n"
                + "        </script>\r\n"
                + "    </body>\r\n"
                + "</html>";

        out.print(htmlOutput);
    }
}
