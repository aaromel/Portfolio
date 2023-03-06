// Own code ->
package harjoitustyo.omalista;

// Otetaan käyttöön linkitetty lista ja Ooperoiva.
import java.util.LinkedList;
import harjoitustyo.apulaiset.Ooperoiva;

/**
 * OmaLista-luokka, joka on peritty LinkedList-luokasta. Toteuttaa
 * Ooperoiva-rajapinnan. Toimii tietorakenteena kokoelmalle, 
 * sulkusanalistalle sekä hakusanoille.
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet I, kevät 2020.
 * <p>
 * @author Aaro Melchy (aaro.melchy@tuni.fi)
 */

public class OmaLista<E> extends LinkedList<E> implements Ooperoiva<E> {
   
   /*
    * Toteutetut metodit.
    *
    */

   /** 
    * Metodi lisää listaan uuden alkion.
    *
    * @param uusi listaan lisättävä uusi alkio.
    * @throws IllegalArgumentException jos alkio on virheellinen.
    */
   @Override
   @SuppressWarnings({"unchecked"})
   public void lisää(E uusi) throws IllegalArgumentException {
      // Tarkastetaan, että alkio on oikeanlainen ja heitetään 
      // tarvittaessa poikkeus.
      if (uusi == null || !(uusi instanceof Comparable)) {
         throw new IllegalArgumentException("Virheellinen alkio");
      }
      
      // Kun lista on tyhjä, alkio lisätään siihen suoraan.
      if (size() == 0) {
         add(uusi);
      }
      
      else {
         int i = 0;
         // Käydään listan alkiot läpi ja lopetetaan tarkastelu kun
         // lisäys on tehty.
         while (i < size()) {
            
            // Lisätään apuviite nykyiseen alkioon jotta sitä voidaan verrata 
            // uuteen.
            Comparable nykyinen = (Comparable)get(i);
            // Jos nykyinen on suurempi kuin uusi, lisätään uusi nykyisen 
            // paikalle.
            if (nykyinen.compareTo(uusi) > 0) {
               add(i, uusi);
               // Muutetaan laskurin kokoa, jotta silmukka suljetaan.
               i = size();
            }
            else {
               // Jos uusi alkio on suurempi tai yhtä suuri kuin kaikki
               // listan alkiot, lisätään se listan loppuun.
               if (i == size() - 1) {
                  addLast(uusi);
                  i = size();
               }
            }
            i++;
         }
      }
   }
}
// <- Own code