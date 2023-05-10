import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        Document document;
        List<String>urlList = new ArrayList<>();

        //Read from csv file
        Reader in = new FileReader("C://KC//20 MSc//Semester 3//DISSERTATION//" +
                "AAIB_pdf_Reports//2022//AAIB_report_urls_pages1-124.csv");
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader("url").parse(in);
        for (CSVRecord record : records) {
            String aaibUrl = record.get("url");
            aaibUrl = "https://www.gov.uk/" + aaibUrl;

            try {
                //Get Document object after parsing html from url
                document = Jsoup.connect(aaibUrl).get();

                //Get href links from Document object
                Elements links = document.select("a[href]");

                //filter links and save to arraylist (NEEDS TO BE .CSV FILE)
                for (Element link : links) {
                    if (link.attr("href")
                            .contains(".pdf")) {
                        urlList.add(link.attr("href"));
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        String header = "url";
        createCSVFile(header, urlList);
    }

    public static void createCSVFile(String header, List<String> urlList) throws IOException {

        String saveLocation = "C://KC//20 MSc//Semester 3//DISSERTATION//" +
                "AAIB_pdf_Reports//2022//pdf_urls.csv";
        FileWriter out = new FileWriter(saveLocation);
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
                .withHeader(header))) {
            urlList.forEach((url) -> {
                try {
                    printer.printRecord(url);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

}