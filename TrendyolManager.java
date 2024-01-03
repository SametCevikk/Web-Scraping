import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TrendyolManager {


    public void GetUrl() {
        String basehtml = "https://www.trendyol.com";
        String[] productUrl = new String[300];
        int j = 0, sayac = 0, sayac2 = 0, k = 0;

        for (int i = 2; i < 20; i++) {

            String html = "https://www.trendyol.com/laptop-x-c103108?pi=" + i;

            try {

                Document doc = Jsoup.connect(html).get();
                Elements body = doc.select("div.prdct-cntnr-wrppr");

                for (Element e : body.select("div.p-card-wrppr.with-campaign-view")) {

                    String name = e.select("a").attr("href");

                    productUrl[j] = basehtml + name;
                    j++;
                    sayac++;


                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
            if (sayac2 <= 300){
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
                        Elements bodys = doc.select("div.detail-border");

                        for (Element c : bodys.select("ul.detail-attr-container")) {

                            String attribute = c.select("li.detail-attr-item").text();
                            String[] arr = attribute.split(" ");

                            for (k = 0; k < arr.length; k++) {
                                int bayrak = 0;
                                if (arr[k].contains("İşlemci") && arr[k + 1].contains("Tipi")) {
                                    if (arr[k + 2].contains("Intel") && arr[k + 3].contains("Core")) {
                                        d1.append("İşlemci Tipi:",arr[k+4]);
                                        bayrak = 1;
                                    }
                                    if (arr[k + 2].contains("Intel") && bayrak != 1) {

                                        d1.append("İşlemci Tipi:",arr[k+3]);
                                        bayrak = 2;
                                    }
                                    if (arr[k + 2].contains("Apple")) {

                                        d1.append("İşlemci Tipi:",arr[k+3]);
                                        bayrak = 1;
                                    }
                                    if (arr[k + 2].contains("AMD")) {

                                        d1.append("İşlemci Tipi:",arr[k+3]);
                                        bayrak = 1;
                                    }

                                    if (bayrak == 0) {

                                        d1.append("İşlemci Tipi:",arr[k+2]);
                                    }
                                }
                                if (arr[k].contains("SSD") && arr[k + 1].contains("Kapasitesi")) {

                                    d1.append("SSD Kapasitesi:",arr[k+2]);
                                }
                                if (arr[k].contains("Sistemi")) {

                                        d1.append("İşletim Sistemi:",arr[k+1]);


                                }
                                if (arr[k].contains("Belleği)") && arr[k + 2].contains("GB")) {

                                    d1.append("Ram:",arr[k+1]);

                                }
                                if (arr[k].contains("Boyutu")) {

                                    d1.append("Ekran Boyutu:",arr[k+1]);
                                }


                            }
                        }
                        Elements bodyx = doc.select("div.pr-in-w");

                        for (Element e : bodyx.select("div.pr-in-cn")) {

                            String attribute2 = e.select("h1.pr-new-br").text();

                            String s = " &";
                            attribute2 = attribute2 + s;
                            String[] arr2 = attribute2.split(" ");

                            d1.append("Marka Adı:",arr2[0]);
                            d1.append("Model Adı:",arr2[1]);
                            for (int x = 0; x < arr2.length; x++) {
                                if (arr2[x].contains("&")) {

                                    d1.append("Model No:",arr2[x-1]);
                                }

                            }
                            String price = e.select("span.prc-dsc").text();

                            d1.append("Fiyatı:",price);
                            String star = e.select("div.full").attr("style").replaceAll("[^\\d]", "");
                            int rank = Integer.parseInt(star) / 20;

                            d1.append("Puanı:",rank);

                            d1.append("Site İsmi:","Trendyol");
                            sayac2++;



                            collection.insertOne(d1);

                        }


                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }



        }

    }
}
