// Own code ->
package harjoitustyo.dokumentit;

/**
 * Vitsejä mallintava luokka, joka on peritty Dokumentti-luokasta.
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet I, kevät 2020.
 * <p>
 * @author Aaro Melchy (aaro.melchy@tuni.fi)
 */

public class Vitsi extends Dokumentti {
   
   /*
    * Attribuutit.
    *
    */
   
   /** Vitsin laji. */
   private String laji;

   /*
    * Rakentajat.
    *
    */
   
   public Vitsi(int nro, String ekatMerkit, String tokatMerkit) 
   throws IllegalArgumentException {
      super(nro, tokatMerkit);
      laji(ekatMerkit);
   }

   /*
    * Aksessorit.
    *
    */

   public String laji() {
      return laji;
   }

   public void laji(String uusiLaji) throws IllegalArgumentException {
      // Tarkastetaan ja heitetään poikkeus tarvittaessa.
      if (uusiLaji == null || uusiLaji.length() < 1) {
         throw new IllegalArgumentException("Virheellinen laji");
      }

      // Aseteaan attribuutille uusi arvo.
      laji = uusiLaji;
   }

   /*
    * Object-luokan korvatut metodit.
    *
    */

   /** 
    * Palauttaa tietyn vitsin sisällön ja tiedot merkkijonona.
    *
    * @return merkkijonoesitys, joka koostuu attribuuteista.
    */
   @Override
   public String toString() {
      // Muodostetaan merkkijonoesitys, joka sisältää myös vitsin lajin.
      return super.tunniste() + "///" + laji + "///" + super.teksti();
   }
}
// <- Own code