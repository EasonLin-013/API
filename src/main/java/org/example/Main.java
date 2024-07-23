package org.example;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse.BodyHandlers;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.*;
import java.io.FileWriter;
import java.io.File;

// TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
        System.out.print("———————————————————————————————————————————————————\n");

        String url = "https://openapi.twse.com.tw/v1/exchangeReport/TWT48U_ALL";
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        // 發送GET請求
        HttpResponse<String> getResponse = client.send(getRequest, BodyHandlers.ofString());

        // 解析和處理回應
        int getStatusCode = getResponse.statusCode();
        HttpHeaders getHeaders = getResponse.headers();
        String getResponseBody = getResponse.body();

        Gson gson = new Gson();
//        gson.fromJson(getResponseBody);
        List<Data> list = gson.fromJson(getResponseBody,new TypeToken<List<Data>>(){}.getType());

        String Date;
        String Code;
        String Name;
        String Exdividend;
        double StockDividendRatio;
        double CashDividend;
        String[] words = {"00940","1101","1215","2881","2889","2356","2357","3706","4938"};
        String str = "";
        for (Data data : list) {
            Code = data.Code;
            if (containsWords(Code, words)) {
                Date = data.Date.substring(0, 3) + "/" + data.Date.substring(3, 5) + "/" + data.Date.substring(5, 7);
                Name = data.Name;
                Exdividend = data.Exdividend;
                StockDividendRatio = Double.parseDouble(data.StockDividendRatio.isEmpty() ? "0" : data.StockDividendRatio);
                CashDividend = Double.parseDouble(data.CashDividend.isEmpty() ? "0" : data.CashDividend);
                str += "Date:" + Date + "\tCode:" + Code + "\tName:" + Name + "\n" + "Exd:" + Exdividend + "\t\t\tStock:" + StockDividendRatio + "\tCash:" + CashDividend + "\n" + "———————————————————————————————————————————————————\n";

                FileWrite(str);   //寫入文件
                System.out.printf("Date:" + Date + " Code:" + Code + " Name:" + Name + "%n" + "Exd:" + Exdividend + "Stock:" + StockDividendRatio + " Cash:" + CashDividend + "%n" + "———————————————————————————————————————————————————%n");
            }
        }
    }

    private static class Data{
        String Date;
        String Code;
        String Name;
        String Exdividend;
        String StockDividendRatio;
        String CashDividend;
    }

    public static boolean containsWords(String inputString, String[] items) {
        boolean found = false;
        for (String item : items) {
            if (inputString.equals(item)) {
                found = true;
                break;
            }
        }
        return found;
    }

    public static void FileWrite(String argv) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date dateNew = new Date();
        String date = dateFormat.format(dateNew);

        String Year = date.substring(0,4);
//        String Month = date.substring(4,6);
        File path = new File("C:/Eason/" + Year);
        /*建立單一資料夾的方法  mkdir();    */
//        dir.mkdir();
        /*即使沒母資料夾也會一起創建的方法 mkdirs();*/
        path.mkdirs();

        String Filename = date + "_API.txt";

        FileWriter fw = new FileWriter(path + "/" + Filename);
        fw.write(argv);
        fw.flush();
        fw.close();
    }
}