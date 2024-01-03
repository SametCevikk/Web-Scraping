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

public class N11Manager {


    public  void GetUrl() {

        String[] productUrl = new String[300];
        String[] productPriceRate = new String[300];
        int j = 0, sayac = 0, sayac2 = 0, k = 0;

        for (int i = 2; i < 12; i++) {


            String html = "https://www.n11.com/bilgisayar/dizustu-bilgisayar?ipg=" + i;

            try {





                Document doc = Jsoup.connect(html).get();
                Elements body = doc.select("ul.list-ul");

                for (Element e : body.select("div.pro")) {

                    String name = e.select("a").attr("href");
                    String price = e.select("div.proDetail div.priceContainer span.newPrice").text();


                    productUrl[j] = name;
                    productPriceRate[j] = ( price);

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
                        Elements bodys1 = doc.select("div.container.productContainer > div.container");

                          //     String rate= c.select("div.unf-review-rating div.avarageText").text();

                       productPriceRate[a]+=(" "+doc.select("div.unf-p-left > div > div.unf-p-detail > div.unf-p-title > div.proRatingHolder > div.ratingCont > strong").text()) ;

                        //   String price = doc.select("div.unf-price-cover > div.price-cover > div.price > div.priceDetail > div.priceContainer > div.newPrice > ins").text();


                        System.out.println(productPriceRate[a]);
                        d1.append("Fiyatı Ve Puanı",productPriceRate[a]);



                        Elements bodys = doc.select("div.unf-prop");

                        for (Element c : bodys.select("div.unf-prop-context")) {

                            String attribute = c.select("li.unf-prop-list-item").text();
                            String[] arr = attribute.split(" ");

                            for (k = 0; k < arr.length; k++) {

                                if (arr[k].contains("Marka") ) {

                                    d1.append("Marka:",arr[k+1]);
                                }
                                if(arr[k].contains("Model")&&!arr[k-1].contains("Kartı")&&!arr[k-1].contains("İşlemci")){

                                    d1.append("Model Adı Ve Numarası:",arr[k+1]+" "+arr[k+2]);

                                }
                                if(arr[k].contains("İşletim")&&arr[k+1].contains("Sistemi")){

                                    d1.append("İşletim Sistemi:",arr[k+2]);
                                }
                                if(arr[k].contains("Ekran")&&arr[k+1].contains("Boyutu")){

                                    d1.append("Ekran Boyutu:",arr[k+2]);
                                }
                                if(arr[k].contains("İşlemci")&&!arr[k+1].contains("Modeli")&&!arr[k+1].contains("Çekirdek")&&!arr[k+1].contains("Hızı")){

                                    d1.append("İşlemci Tipi:",arr[k+1]+" "+arr[k+2]+" "+arr[k+3]);

                                }
                                if(arr[k].contains("Bellek")&&arr[k+1].contains("Kapasitesi")){

                                    d1.append("Ram:",arr[k+2]);
                                }
                                if(arr[k].contains("Disk")&&arr[k+1].contains("Türü")){

                                    d1.append("Disk Türü:",arr[k+2]);
                                }
                                if(arr[k].contains("Disk")&&arr[k+1].contains("Kapasitesi")){

                                    d1.append("Disk Kapasitesi:",arr[k+2]);
                                }





                            }
                            d1.append("Site İsmi:","n11");
                            collection.insertOne(d1);

                        }



                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }


            }

        }

    }




