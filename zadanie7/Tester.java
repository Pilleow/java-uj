import java.util.Map;

public class Tester {

    public static void main(String[] args) {
        WatkowyEksperymentator eksperymentator = new WatkowyEksperymentator();

        eksperymentator.dozwolonaLiczbaDzialajacychWatkow(1);

        KostkaDoGry kostka = () -> {
            try {
                Thread.sleep(10); // TUTAJ OPÓŹNIENIE!!!
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return (int) (Math.random() * 6) + 1;
        };

        ThreadFactory fabryka = Thread::new;

        eksperymentator.fabrykaWatkow(fabryka);

        final int liczbaRzutow = 100;

        long startTime = System.currentTimeMillis();

        int taskId1 = eksperymentator.kostkaDoZbadania(kostka, liczbaRzutow);
        int taskId2 = eksperymentator.kostkaDoZbadania(kostka, liczbaRzutow);
        int taskId3 = eksperymentator.kostkaDoZbadania(kostka, liczbaRzutow);
        int taskId4 = eksperymentator.kostkaDoZbadania(kostka, liczbaRzutow);
        int taskId5 = eksperymentator.kostkaDoZbadania(kostka, liczbaRzutow);

        waitForCompletion(eksperymentator, taskId1);

        waitForCompletion(eksperymentator, taskId2);
        waitForCompletion(eksperymentator, taskId3);
        waitForCompletion(eksperymentator, taskId4);
        waitForCompletion(eksperymentator, taskId5);

        printHistogram(eksperymentator, taskId1, liczbaRzutow);
        printHistogram(eksperymentator, taskId2, liczbaRzutow);
        printHistogram(eksperymentator, taskId3, liczbaRzutow);
        printHistogram(eksperymentator, taskId4, liczbaRzutow);
        printHistogram(eksperymentator, taskId5, liczbaRzutow);

        long endTime = System.currentTimeMillis();
        System.out.println("# Czas operacji programu: " + (endTime - startTime) + "ms");
    }

    private static void waitForCompletion(WatkowyEksperymentator eksperymentator, int taskId) {
        while (!eksperymentator.badanieKostkiZakonczono(taskId)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void printHistogram(WatkowyEksperymentator eksperymentator, int taskId, int liczbaRzutow) {
        System.out.println("\n---- taskId=" + taskId + " ----");
        Map<Integer, Integer> histogram = eksperymentator.histogram(taskId);
        for (int i = 1; i <= 6; i++) {
            System.out.println("P(" + i + ") = " + ((double)histogram.get(i) / (double)liczbaRzutow));
        }
    }
}
