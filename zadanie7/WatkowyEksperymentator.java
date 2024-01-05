import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class WatkowyEksperymentator implements BadaczKostekDoGry {

    private int idZadania;
    private final Semaphore liczbaAktywnychWatkow;
    private ThreadFactory fabrykaWatkow;
    private final Map<Integer, Boolean> mapaAktywnosciWatkow;
    private final Map<Integer, Map<Integer, Integer>> mapaWynikow;

    public WatkowyEksperymentator() {
        this.fabrykaWatkow = null;
        this.idZadania = 0;
        this.liczbaAktywnychWatkow = new Semaphore(0);
        this.mapaWynikow = new HashMap<>();
        this.mapaAktywnosciWatkow = new HashMap<>();
    }

    /**
     * Metoda ustala liczbę wątków, które jednocześnie mogą badać kostki do gry.
     * Ponieważ jedną kostkę badać może tylko jeden wątek metoda jednocześnie ustala
     * liczbę jednocześnie testowanych kostek.
     *
     * @param limitWatkow dozwolona liczba wątków
     */
    @Override
    public void dozwolonaLiczbaDzialajacychWatkow(int limitWatkow) {
        if (limitWatkow <= 0) throw new IllegalArgumentException("wartość limitWatkow musi być większa od 0.");
        liczbaAktywnychWatkow.release(limitWatkow);
    }

    /**
     * Metoda dostarcza obiektu, który będzie odpowiedzialny za produkcję wątków
     * niezbędnych do pracy programu. Tylko wyprodukowane przez fabrykę wątki mogą
     * używać kostek.
     *
     * @param fabryka referencja do obiektu produkującego wątki
     */
    @Override
    public void fabrykaWatkow(ThreadFactory fabryka) {
        this.fabrykaWatkow = fabryka;
    }

    /**
     * Metoda przekazuję kostkę do zbadania. Metoda nie blokuje wywołującego ją
     * wątku na czas badania kostki. Metoda zwraca unikalny identyfikator zadania.
     * Za pomocą tego identyfikatora użytkownik będzie mógł odebrać wynik zlecenia.
     *
     * @param kostka       kostka do zbadania
     * @param liczbaRzutow liczba rzutów, które należy wykonać w celu zbadania
     *                     kostki
     * @return unikalny identyfikator zadania.
     */
    @Override
    public int kostkaDoZbadania(KostkaDoGry kostka, int liczbaRzutow) {
        if (liczbaRzutow <= 0) throw new IllegalArgumentException("Wartość liczbaRzutow musi być większa od 0.");
        if (fabrykaWatkow == null) throw new IllegalArgumentException("Fabryka wątków nie jest ustawiona. Wywołaj metodę fabrykaWatkow(ThreadFactory fabryka).");

        // semafor tutaj
        liczbaAktywnychWatkow.acquireUninterruptibly();

        int taskId = ++idZadania;
        mapaAktywnosciWatkow.put(taskId, true);
        mapaWynikow.put(taskId, new HashMap<>());
        for (int i = 1; i <= 6; ++i) mapaWynikow.get(taskId).put(i, 0);


        fabrykaWatkow.getThread(() -> {
            try {
                int wynik;
                for (int i = 0; i < liczbaRzutow; ++i) {
                    wynik = kostka.rzut();
                    synchronized (mapaWynikow.get(taskId)) {
                        mapaWynikow.get(taskId).merge(wynik, 1, Integer::sum);
                    }
                }
            } finally {
                mapaAktywnosciWatkow.put(taskId, false);
                liczbaAktywnychWatkow.release();
            }
        }).start();

        return taskId;
    }

    /**
     * Metoda pozwala użytkownikowi sprawdzić, czy badanie kostki zostało zakończone.
     * Zaraz po zakończeniu badania kostki użytkownik powinien uzyskać prawdę.
     *
     * @param identyfikator identyfikator zadania zwrócony przez metodę
     *                      kostkaDoZbadania
     * @return true - analiza kostki zakończona, w każdym innym przypadku false.
     */
    @Override
    public boolean badanieKostkiZakonczono(int identyfikator) {
        return !mapaAktywnosciWatkow.get(identyfikator);
    }

    /**
     * Wynik badania kostki. Zaraz po potwierdzeniu, że wynik jest gotowy użytkownik
     * powinien uzyskać histogram zawierający wynik wszystkich rzutów kostką.
     *
     * @param identyfikator identyfikator zadania zwrócony przez metodę
     *                      kostkaDoZbadania
     * @return histogram - mapa, której kluczem jest liczba oczek, wartością liczba
     * rzutów, w których otrzymano tą liczbę oczek.
     */
    @Override
    public Map<Integer, Integer> histogram(int identyfikator) {
        return mapaWynikow.get(identyfikator);
    }
}
