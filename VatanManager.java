import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class VatanManager {


    public void GetUrl() {
        String basehtml = "https://www.vatanbilgisayar.com";
        String[] productUrl = new String[300];
        String[] arr2;
        String[] arr;
        int j = 0, sayac = 0, sayac2 = 0, k = 0;

        for (int i = 1; i < 14; i++) {


            String html = "https://www.vatanbilgisayar.com/notebook/?page=" + i;

            try {
                Document doc = Jsoup.connect(html).get();
                Elements body = doc.select("div.wrapper-product-main");
                for (Element e : body.select("div.product-list.product-list--list-page")) {

                    String name = e.select("a").attr("href");
                    productUrl[j] = basehtml + name;
                    j++;
                    sayac++;

                }

            } catch (Exception e) {
                System.out.println(e);
            }


        }
        if (sayac2 <= 300) {
            for (int a = 0; a < sayac; a++) {

                try {

                    ConnectionString connectionString = new ConnectionString("mongodb+srv://Samet:Scraping@yazlab.gq1rjy8.mongodb.net/?retryWrites=true&w=majority");
                    MongoClientSettings settings = MongoClientSettings.builder()
                            .applyConnectionString(connectionString)
                            .build();
                    MongoClient mongoClient = MongoClients.create(settings);
                    MongoDatabase database = mongoClient.getDatabase("Test");
                    MongoCollection<org.bson.Document> collection= database.getCollection("Notebook");
                    org.bson.Document d1 = new org.bson.Document();


                    Document doc = Jsoup.connect(productUrl[a]).get();
                    Elements bodys = doc.select("div.tab-content.tab-pane--tab");
                    for (Element c : bodys.select("div.product-feature")) {

                        String attribute = c.select("table.product-table").text();
                        arr = attribute.split(" ");


                        for (k = 0; k < arr.length; k++) {
                            int bayrak2 = 0;
                            if (arr[k].contains("inch")) {

                                d1.append("Marka Adı:", arr[k - 1]);

                            }
                            if (arr[k].contains("Ram") && arr[k + 1].contains("(Sistem") && arr[k + 2].contains("Belleği)")) {

                                d1.append("Ram:", arr[k + 3]);

                            }

                            if (arr[k].contains("Sistemi")) {

                                d1.append("İşletim Sistemi:", arr[k + 1]);

                            }
                            if (arr[k].contains("İşlemci") && arr[k + 1].contains("Teknolojisi")) {

                                d1.append("İşlemci Tipi:", arr[k + 2] + " " + arr[k + 3]);

                            }
                            if (arr[k].contains("Kapasitesi")) {

                                d1.append("Disk Boyutu:", arr[k + 1]);

                            }
                            if (arr[k].contains("Türü")) {
                                if (arr[k + 1].contains("NVMe")) {

                                    d1.append("Disk Türü:", arr[k + 2]);
                                    bayrak2 = 1;
                                }
                                if (bayrak2 == 0)

                                    d1.append("Disk Türü:", arr[k + 1]);

                            }

                        }

                    }
                    Elements body = doc.select("div.row");
                    for (Element e : body.select("div.col-xs-12.col-sm-12.col-md-12.col-lg-12.product-detail")) {

                        String attribute2 = e.select("h1.product-list__product-name").text();
                        arr2 = attribute2.split(" ");

                        d1.append("Marka Adı:", arr2[0]);
                        d1.append("Model Adı:", arr2[1]);
                        String model = e.select("div.product-list__product-code.pull-left.product-id").text();
                        String[] arr3 = model.split(" ");

                        d1.append("Model Numarası:", arr3[0]);

                        String star = e.select("span.score").attr("style").replaceAll("[^\\d]", "");
                        int rank = Integer.parseInt(star) / 20;

                        d1.append("Puanı:", rank);

                        String price = e.select("span.product-list__price").text();

                        d1.append("Fiyatı", price);


                        d1.append("Site İsmi:", "Vatan");
                        d1.append("ürün id:", sayac2);

                        collection.insertOne(d1);
                        sayac2++;

                    }


                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }


    }
}
