// Own code ->
package harjoitustyo.dokumentit;

// Otetaan käyttöön LocalDate.
import java.time.LocalDate;

/**
 * Uutisia mallintava luokka, joka on peritty Dokumentti-luokasta.
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet I, kevät 2020.
 * <p>
 * @author Aaro Melchy (aaro.melchy@tuni.fi)
 */

public class Uutinen extends Dokumentti {
   
   /*
    * Attribuutit.
    *
    */
   
   /** Uutisen päivämäärä. */
   private LocalDate päivämäärä;

   /*
    * Rakentajat.
    *
    */
   
   public Uutinen(int nro, LocalDate pvm, String merkit) 
   throws IllegalArgumentException {
      super(nro, merkit);
      päivämäärä(pvm);
   }

   /*
    * Aksessorit.
    *
    */

   public LocalDate päivämäärä() {
      return päivämäärä;
   }

   public void päivämäärä(LocalDate uusiPvm) throws IllegalArgumentException {
      // Tarkastetaan ja heitetään poikkeus tarvittaessa.
      if (uusiPvm == null) {
         throw new IllegalArgumentException("Virheellinen pvm");
      }

      // Asetetaan attribuutille uusi arvo.
      päivämäärä = uusiPvm;
   }

   /*
    * Object-luokan korvatut metodit.
    *
    */

   /** 
    * Palauttaa uutisen sisällön ja tiedot merkkijonona.
    *
    * @return merkkijono esitys, joka koostuu attribuuteista. Pvm muutetaan
    * suomalaiseen muotoon.
    */
   @Override
   public String toString() {
      // Muodostetaan merkkijonoesitys, joka sisältää myös päivämäärän.
      return super.tunniste() + "///" + päivämäärä.getDayOfMonth() + "." + 
      päivämäärä.getMonthValue() + "." + päivämäärä.getYear() + "///" + 
      super.teksti();
   }
}
// <- Own code