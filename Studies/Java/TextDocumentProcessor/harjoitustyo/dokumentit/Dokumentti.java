// Own code ->
package harjoitustyo.dokumentit;

import harjoitustyo.apulaiset.*;
import java.util.*;
import java.util.stream.*;
import java.util.ArrayList;

/**
 * Dokumentteja mallintava abstrakti luokka, joka toteuttaa Tietoinen- ja
 * Comparable-rajapinnat.
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet I, kevät 2020.
 * <p>
 * @author Aaro Melchy (aaro.melchy@tuni.fi)
 */

public abstract class Dokumentti implements Tietoinen<Dokumentti>, Comparable<Dokumentti> {
   
   /*
    * Attribuutit.
    *
    */
   
   /** Dokumentin tunniste. */
   private int tunniste;
   
   /** Dokumentin teksti. */
   private String teksti;
   
   /*
    * Rakentajat.
    *
    */

   public Dokumentti(int nro, String merkit) throws IllegalArgumentException {
      tunniste(nro);
      teksti(merkit);
   }
   
   /*
    * Aksessorit.
    *
    */

   public int tunniste() {
      return tunniste;
   }
   
   public void tunniste(int uusiTunniste) throws IllegalArgumentException {
      // Tarkastetaan ja heitetään poikkeus tarvittaessa.
      if (uusiTunniste < 1) {
         throw new IllegalArgumentException("Virheellinen tunniste");
      }

      // Asetaan uusi arvo.
      tunniste = uusiTunniste;
   }

   public String teksti() {
      return teksti;
   }
   
   public void teksti(String uusiTeksti) throws IllegalArgumentException {
      // Tarkastetaan ja heitetään poikkeus tarvittaessa.
      if (uusiTeksti == null || uusiTeksti.length() < 1) {
         throw new IllegalArgumentException("Virheellinen teksti");
      }
      
      // Asetetaan uusi arvo.
      teksti = uusiTeksti;
   }

   /*
    * Toteutetut metodit.
    *
    */
   
   /** 
    * Käydään läpi dokumentin teksti ja tarkastetaan sisältääkö se 
    * määriteltyjä hakusanoja.
    *
    * @param hakusanat lista hakusanoista, joita etsitään dokumentista.
    * @return true, jos jokainen hakusana löytyi dokumentista, false , jos
    * jokaista hakusanaa ei löytynyt dokumentista.
    * @throws IllegalArgumentException jos hakusanat on tyhjä lista tai null.
    */
   @Override
   public boolean sanatTäsmäävät(LinkedList<String> hakusanat) 
   throws IllegalArgumentException {
      // Tarkastetaan hakusanat ja heitetään tarvittaessa poikkeus.
      if (hakusanat == null || hakusanat.size() == 0) {
         throw new IllegalArgumentException("Virheellinen hakusanalista");
      }
      
      // Muodostetaan tekstin avaimista taulukko.
      String[] osat = teksti.split("[ ]");
      
      // Käydään läpi jokainen hakusana.
      for (int i = 0; i < hakusanat.size(); i++) {
         
         // Muuttuja, joka kertoo löytyykö hakusanaa tekstistä.
         int loytyy  = 0;
         
         // Käydään läpi jokainen tekstin avain.
         for (int y = 0; y < osat.length; y++) {
            
            // Mikäli hakusana löytyy, muuttuja saa arvon 1.
            if (osat[y].equals(hakusanat.get(i))) {
               loytyy = 1;
            }
         }
         if (loytyy == 0) {
            return false;
         }    
      }

      // Jos jokainen hakusana löytyi, paluuarvo on true.
      return true;
   }
   
   /** 
    * Poistetaan dokumentin tekstistä sulkusanoja ja välimerkkejä.
    *
    * @param sulkusanat lista sulkusanoista, jotka poistetaan dokumentista.
    * @param välimerkit merkkijono merkeistä, jotka poistetaan dokumentista.
    * @return true, jos jokainen hakusana löytyi dokumentista, false , jos
    * jokaista hakusanaa ei löytynyt dokumentista.
    * @throws IllegalArgumentException jos parametreissa oli yksikin virhe.
    */
   @Override
   public void siivoa(LinkedList<String> sulkusanat, String välimerkit)
   throws IllegalArgumentException {
      // Suoritetaan tarkastukset.
      if (sulkusanat == null || sulkusanat.size() == 0) {
         throw new IllegalArgumentException("Virheellinen sulkusanalista");
      }
      if (välimerkit == null || välimerkit.length() == 0) {
         throw new IllegalArgumentException("Virheelliset välimerkit");
      }
      
      // Käydään läpi jokainen poistettava välimerkki.
      for (int z = 0; z < välimerkit.length(); z++) {
         String poistettava = String.valueOf(välimerkit.charAt(z));
         
         // Mikäli tekstistä löytyy tietty välimerkki, korvataan ne tyhjällä
         // merkkijonolla.
         if (teksti.indexOf(välimerkit.charAt(z)) != -1) {
            teksti = teksti.replace(poistettava, "");
         }
      }
      
      // Muutetaan teksti kokonaan pieniksi kirjaimiksi.
      teksti = teksti.toLowerCase();

      // Muodostetaan taulukko tekstin avaimista.
      String[] osat = teksti.split("[ ]");

      // Luodaan taulukkolista.
      ArrayList<String> lista = new ArrayList<String>();

      // Täytetään taulukkolista avaimilla.
      for (int q = 0; q < osat.length; q++) {
         lista.add(osat[q]);
      }
      
      // Poistetaan tyhjät avaimet.
      lista.removeAll(Collections.singleton(""));
      
      // Käydään jokainen sulkusana läpi.
      for (int x = 0; x < sulkusanat.size(); x++) {

         // Apumuuttujat.
         int koko = lista.size();
         int g = 0;

         // Käydään jokainen avain läpi.
         while (g < koko) {
            
            // Mikäli avain täsmää tietyn sulkusanan kanssa, poistetaan avain.
            if (lista.get(g).equals(sulkusanat.get(x))) {
               lista.remove(g);
               koko--;
               g--;
            }
            
            // Kasvatetaan laskuria.
            g++;
         }
      }

      // Muodostetaan taulukkolistasta merkkijono tietovirran avulla.
      String väli = " ";
      teksti = lista.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(väli));
   }

   /*
    * Object-luokan korvatut metodit.
    *
    */
   
   /** 
    * Muodostetaan merkkijonoesitys dokumentista.
    *
    * @return merkkijono esitys, joka koostuu attribuuteista.
    */
   @Override
   public String toString() {
      return tunniste + "///" + teksti;
   }

   /** 
    * Verrataan dokumentteja keskenään ja määritellään ovatko ne samat.
    *
    * @param toinen dokumenttiin verrattava dokumentti-olio.
    * @return true, jos dokumenttien tunnisteet täsmäävät, false, mikäli
    * ne ovat eri tai kohdataan poikkeus.
    */
   @Override
   public boolean equals(Object toinen) {
      try {
         // Asetetaan olioon viite.
         Dokumentti toinenDokumentti = (Dokumentti)toinen;

         // Palautetaan vertauksen tulos.
         return tunniste == toinenDokumentti.tunniste();
      }
      catch (Exception e) {
         return false;
      }
   }
   
   /** 
    * Verrataan dokumenttien tunnisteita keskenään.
    *
    * @param verrattava dokumenttiin verrattava dokumentti.
    * @return -1, jos verrattavan tunniste suurempi, 0, jos tunnisteet
    * samankokoiset, 1, jos verrattavalla pienempi tunniste.
    */
   @Override
   public int compareTo(Dokumentti verrattava) {
      // Verrattavalla suurempi tunniste.
      if (tunniste < verrattava.tunniste()) {
         return -1;
      }
      // Tunnisteet ovat samat.
      else if (tunniste == verrattava.tunniste()) {
         return 0;
      }
      // Muussa tapauksessa, verrattavalla pienempi tunniste.
      else {
         return 1;
      }
   }
}
// <- Own code