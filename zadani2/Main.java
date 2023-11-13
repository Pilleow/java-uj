import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Eksperymentator e = new WspanialyEksperymentator();
        KostkaDoGry k = new K1();
        e.użyjKostki(k);
        e.czasJednegoEksperymentu(2000);

        List<Integer> l = new ArrayList<>();
        l.add(1);
        l.add(2);
        l.add(4);
        System.out.println(e.szansaNaWyrzucenieKolejno(l));
    }
}
