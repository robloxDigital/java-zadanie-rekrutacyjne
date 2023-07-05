import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
public class Main {
    private static Map<Integer, Integer> dostepne_reszty_w_groszach = new LinkedHashMap<Integer, Integer>();
    private static float reszta_do_wydania;
    private static int reszta_do_wydania_w_groszach;
    private static float float_input(){
        /*
        * Funkcja sprawdzająca poprawność wpisanej reszty. Maksymalna liczba ponownych prób to 3.
        * */
        System.out.println("Podaj resztę do wydania w złotówkach (podaj zero aby przerwać): ");
        int max_tries = 3;
        int count = 0;
        float tmp;
        while(true){
            try{
                Scanner sc = new Scanner(System.in);
                tmp = sc.nextFloat();
                return tmp;
            } catch(InputMismatchException e){
                if(++count == max_tries) return 0;
                System.out.println("Nie podano liczby zmiennoprzecinkowej na wejściu. Proszę spróbować ponownie.");
            }
        }
    }
    public static void main(String[] args) {
        dostepne_reszty_w_groszach.put(500, 1);
        dostepne_reszty_w_groszach.put(200, 3);
        dostepne_reszty_w_groszach.put(100, 5);
        dostepne_reszty_w_groszach.put(50, 10);
        dostepne_reszty_w_groszach.put(20, 20);
        dostepne_reszty_w_groszach.put(10, 200);
        dostepne_reszty_w_groszach.put(5, 100);
        dostepne_reszty_w_groszach.put(2, 100);
        dostepne_reszty_w_groszach.put(1, 10000);

        while ((reszta_do_wydania = float_input()) != 0) {
            reszta_do_wydania_w_groszach = Math.round(reszta_do_wydania * 100);
            Map<Integer, Integer> nominaly_do_wydania = new LinkedHashMap<Integer, Integer>();
            boolean brak_reszt = true;
            for (Map.Entry<Integer, Integer> set : dostepne_reszty_w_groszach.entrySet()) {
                int nominal = set.getKey();
                int ilosc = set.getValue();

                if(ilosc > 0){
                    brak_reszt = false;
                }

                /*
                * Poniższy kod realizuje najważniejszą część programu - algorytm wyznaczania najmniejszej ilości monet.
                * Krótki opis działania:
                *       - dla posortowanych malejąco nominałów:
                *           - dopóki wartość nominału jest mniejsza bądz równa pozostałej do wydania reszcie oraz
                *             ilość nominału w kasie jest większa od zera:
                *               - od reszty odejmij wartość nominału i zmniejsz ilość nominałów w kasie
                * */
                while (nominal <= reszta_do_wydania_w_groszach && ilosc > 0) {
                    reszta_do_wydania_w_groszach -= nominal;
                    if (nominaly_do_wydania.containsKey(nominal)) {
                        nominaly_do_wydania.put(nominal, nominaly_do_wydania.get(nominal) + 1);
                    } else {
                        nominaly_do_wydania.put(nominal, 1);
                    }
                    dostepne_reszty_w_groszach.put(nominal, --ilosc);
                }
            }

            if(brak_reszt){
                System.out.println("Brak reszt do wydania.");
                break;
            }

            /*
            * W przypadku gdy nie wydano całości podanej na wejściu kwoty, wyświetlany jest odpowiedni komunikat a poszczególne nominały trafiają z powrotem do kasy.
            * */
            if(reszta_do_wydania_w_groszach > 0){
                System.out.println("Nie ma możliwości wydania " + reszta_do_wydania + " zł");
                for (Map.Entry<Integer, Integer> set : nominaly_do_wydania.entrySet()){
                    int k = set.getKey();
                    dostepne_reszty_w_groszach.put(k, dostepne_reszty_w_groszach.get(k) + set.getValue());
                }
                continue;
            }

            System.out.println("Dla reszty " + reszta_do_wydania + " zł:");
            for (Map.Entry<Integer, Integer> set : nominaly_do_wydania.entrySet()) {
                String format;
                if (set.getKey() >= 100) {
                    format = set.getKey() / 100 + " zł";
                } else {
                    format = set.getKey() + " gr";
                }
                System.out.println("Wydaj " + set.getValue() + " monet " + format);
            }
            System.out.println();
        }
    }
}