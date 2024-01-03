
public class Main {

    public static void main(String[] args) {
        HepsiBuradaManager hepsiBuradaManager = new HepsiBuradaManager();
        hepsiBuradaManager.GetUrl();
        VatanManager vatanManager = new VatanManager();
        vatanManager.GetUrl();
        TrendyolManager trendyolManager = new TrendyolManager();
        trendyolManager.GetUrl();
        N11Manager n11Manager = new N11Manager();
        n11Manager.GetUrl();


    }
}
