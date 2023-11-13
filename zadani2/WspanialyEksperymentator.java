import java.util.*;

public class WspanialyEksperymentator implements Eksperymentator {
    KostkaDoGry kostka;
    long czasEksperymentu;
    @Override
    public void użyjKostki(KostkaDoGry kostka) {
        this.kostka = kostka;
    }

    @Override
    public void czasJednegoEksperymentu(long czasEksperymentu) {
        this.czasEksperymentu = czasEksperymentu;
    }

    @Override
    public Map<Integer, Double> szansaNaWyrzucenieOczek(int liczbaKostek) {
        double total = 0;
        int s, i;
        Map<Integer, Double> wynik = new HashMap<>();
        for (i = 1; i <= 6 * liczbaKostek; ++i) wynik.put(i, 0.0);
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start < czasEksperymentu) {
            s = 0;
            total++;
            for (i = 0; i < liczbaKostek; ++i) s += kostka.rzut();
            wynik.put(s, wynik.get(s) + 1.0);
        }
        for (i = 1; i <= 6 * liczbaKostek; ++i) {
            wynik.put(i, wynik.get(i) / total);
        }
        return wynik;
    }

    @Override
    public double szansaNaWyrzucenieKolejno(List<Integer> sekwencja) {
        boolean _continue;
        double total = 0.0;
        double wynik = 0.0;
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start < czasEksperymentu) {
            total++;
            _continue = false;
            for (int n : sekwencja) if (n != kostka.rzut()) {
                _continue = true;
            };
            if (_continue) continue;
            wynik++;
        }
        return wynik / total;
    }

    @Override
    public double szansaNaWyrzucenieWDowolnejKolejności(Set<Integer> oczka) {
        int i;
        double total = 0.0;
        double wynik = 0.0;
        Set<Integer> wylosowaneOczka = new HashSet<>();
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start < czasEksperymentu) {
            total++;
            wylosowaneOczka.clear();
            for (i = 0; i < oczka.size(); ++i) wylosowaneOczka.add(kostka.rzut());
            if (wylosowaneOczka.size() != oczka.size()) continue;
            if (wylosowaneOczka.containsAll(oczka)) wynik++;
        }
        return wynik / total;
    }
}
