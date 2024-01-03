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

public class HepsiBuradaManager {

    public void GetUrl() {
        String baseHtml = "https://www.hepsiburada.com";
        String[] productUrl = new String[300];
        int j = 0, sayac = 0, sayac2 = 0, k = 0;
        for (int i = 2; i < 15; i++) {

            String html = "https://www.hepsiburada.com/laptop-notebook-dizustu-bilgisayarlar-c-98?sayfa=" + i;

            try {

                Document doc = Jsoup.connect(html).get();
                Elements body = doc.select("div.MORIA-voltran-body.voltran-body.ProductList");
                for (Element e : body.select("li.productListContent-zAP0Y5msy8OHn5z7T_K_")) {

                    String name = e.select("a").attr("href");

                    productUrl[j] = baseHtml + name;
                    j++;
                    sayac++;


                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
            if(sayac2<=300)
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
                        Elements bodys = doc.select("div.productDetailRight.col.lg-2.md-2.sm-1.grid-demo-fluid");

                        for (Element c : bodys.select("div.product-information.col.lg-5.sm-1")) {

                            String attribute = c.select("h1.product-name.best-price-trick").text();
                            String[] arr = attribute.split(" ");

                            d1.append("Marka Adı:",arr[0]);

                            d1.append("Model Adı:",arr[1]);
                            d1.append("Model No:",arr[2]);
                            String price = c.select("div.product-price.price-container.big").text();
                            String[] priceSplit = price.split(" ");

                            d1.append("Fiyatı:",priceSplit[0]);
                            String star = c.select("span.rating-star").text();

                            d1.append("Puanı:",star);
                        }

                        Elements bodyx = doc.select("table.data-list.tech-spec");
                        for (Element e : bodyx.select("tbody")) {
                            String attribute = e.select("tr").text();
                            String[] arr = attribute.split(" ");
                            for (k = 0; k < arr.length; k++) {
                                if (arr[k].contains("Ekran") && arr[k + 1].contains("Boyutu")) {

                                    d1.append("Ekran Boyutu",arr[k+2]);
                                }
                                if (arr[k].contains("İşlemci") && arr[k + 1].contains("Tipi")) {

                                    d1.append("İşlemci Tipi:",arr[k+4]);
                                }
                                if (arr[k].contains("İşletim") && arr[k + 1].contains("Sistemi")) {

                                    d1.append("İşletim Sistemi:",arr[k+2]);
                                }
                                if (arr[k].contains("Belleği)")) {

                                    d1.append("Ram",arr[k+1]);
                                }
                                if (arr[k].contains("SSD") && arr[k + 1].contains("Kapasitesi")) {

                                    d1.append("SSD Kapasitesi:",arr[k+2]);
                                }


                            }

                        }

                        d1.append("Site İsmi:","Hepsi Burada");
                        sayac2++;


                        collection.insertOne(d1);


                    } catch (Exception e) {
                        System.out.println(e);
                    }


                }
        }
    }

