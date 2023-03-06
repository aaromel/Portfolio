// Oma koodi ->
import harjoitustyo.kayttoliittyma.Kayttoliittyma;

/**
 * Ajoluokka, jossa luodaan Käyttöliittymä-olio. Tarkastetaan myös komentorivi-
 * parametrit.
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet I, kevät 2020.
 * <p>
 * @author Aaro Melchy (aaro.melchy@tuni.fi)
 */

public class Oope2HT {
   public static void main(String[] args) {
      // Tulostetaan tervehdys.
      System.out.println("Welcome to L.O.T.");
      
      // Tarkastetaan että oikea määrä komentoriviparametreja.
      if (args.length != 2) {
         // Tulostetaan virheilmoitus jos vääräm määrä.
         System.out.println("Wrong number of command-line arguments!");
      }
      
      // Muussa tapauksessa luodaan Käyttöliittymä-olio ja kutsutaan
      // pääsilmukka-metodia.
      else {
         Kayttoliittyma kayttoliittyma = new Kayttoliittyma(args[0], args[1]);
         kayttoliittyma.pääsilmukka();
      }
      
      // Tulostetaan ohjelman sulkeutuminen.
      System.out.println("Program terminated.");
   }
}
// <- Oma koodi