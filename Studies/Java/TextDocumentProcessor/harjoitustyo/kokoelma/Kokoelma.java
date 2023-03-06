// Own code ->
package harjoitustyo.kokoelma;

import harjoitustyo.dokumentit.*;
import harjoitustyo.omalista.*;
import harjoitustyo.apulaiset.*;
import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Kokoelma-luokka, joka toteuttaa Kokoava-rajapinnan.
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet I, kevät 2020.
 * <p>
 * @author Aaro Melchy (aaro.melchy@tuni.fi)
 */

public class Kokoelma implements Kokoava<Dokumentti> {
   
   /*
    * Attribuutit.
    *
    */

   /** Kokoelman dokumentit. */
   private OmaLista<Dokumentti> dokumentit;
   
   /** Kokoelman sulkusanat. */
   private OmaLista<String> sulkusanat;

   /*
    * Rakentajat.
    *
    */

   public Kokoelma() {
      dokumentit = new OmaLista<Dokumentti>();
      sulkusanat = new OmaLista<String>();
   }

   /*
    * Aksessorit.
    *
    */

   public OmaLista<Dokumentti> dokumentit() {
      return dokumentit;
   }
   
   public OmaLista<String> sulkusanat() {
      return sulkusanat;
   }
   
   /*
    * Metodit.
    *
    */
   
   /** 
    * Ladataan tiedoston sisältö ohjelman tietorakenteisiin.
    *
    * @param argumentti komentoriviparametrina annettu tiedoston nimi.
    * @param tyyppi kertoo onko tiedosto dokumenteista vai sulkusanoista 
    * koostuva.
    * @return true, jos lataus onnistui, false, jos tiedostoa ei löydetty.
    */
   public boolean lataa(String argumentti, String tyyppi) {
      Scanner tiedostonLukija = null;
      try {
         // Avataan tiedosto.
         File tiedosto = new File(argumentti);
         
         // Liitetään lukija tiedostoon.
         tiedostonLukija = new Scanner(tiedosto);
         
         // Tarkastetaan tyyppi.
         if (tyyppi.equals("dokumentit")) {
            
            // Käydään jokainen rivi läpi.
            while (tiedostonLukija.hasNext()) {
               
               // Jokainen rivi vastaa merkkijonoa.
               String rivi = tiedostonLukija.nextLine();
               
               // Kutsutaan lisää-metodia ja annetaan parametriksi rivi.
               lisää(rivi, argumentti);
            }
         }
         
         // Sulkusanat.
         else {
            while (tiedostonLukija.hasNext()) {
               String rivi = tiedostonLukija.nextLine();
               
               // Kutsutaan OmaLista-luokan lisää-metodia.
               sulkusanat.lisää(rivi);
            }
         }

         // Suljetaan lukija.
            tiedostonLukija.close();
            
         // Latauksen onnistuessa paluuarvo on true.
         return true;
      }
      
      // Jos tiedostoa ei löydetä, tulostetaan virheilmoitus ja palautetaan 
      // false.
      catch (FileNotFoundException e) {
         System.out.println("Missing file!");
         
         // Mikäli lukija luotiin, suljetaan se.
         if (tiedostonLukija != null) {
            tiedostonLukija.close();
         }
         return false;
      }
   }
   
   /** 
    * Metodi poistaa dokumentin kokoelmasta.
    *
    * Hyödynnetään hae-metodia.
    *
    * @param tunniste dokumentin yksilöivä tunniste.
    */
   public void poista(int tunniste) {
      dokumentit.remove(hae(tunniste));
   }

   /** 
    * Metodi lisää dokumentin kokoelmaan.
    *
    * Lisäys tehdään OmaLista-luokan metodin avulla.
    *
    * @param uusi kokoelmaan lisättävä dokumentti.
    * @throws IllegalArgumentException jos dokumentti on virheellinen.
    */
   @Override
   public void lisää(Dokumentti uusi) throws IllegalArgumentException {
      // Tarkastetaan, että dokumentti on oikeanlainen ja heitetään
      // tarvittaessa poikkeus.
      if (uusi == null || !(uusi instanceof Comparable)) {
         throw new IllegalArgumentException("Virheellinen dokumentti.");
      }
      int i = 0;
      
      // Käydään dokumentit läpi.
      while (i < dokumentit.size()) {
         
         // Jos uusi dokumentti vastaa jo olemassa olevaa, heitetään poikkeus.
         if (uusi.equals(dokumentit.get(i))) {
            throw new IllegalArgumentException("Sama dokumentti.");
         }
         i++;
      }

      // Kutsutaan OmaLista-luokan lisää-metodia.
      dokumentit.lisää(uusi);
   }
   
   /** 
    * Lisätään dokumentti kokoelmaan.
    *
    * @param lisättävä kokoelmaan lisättävä dokumentti.
    * @param tiedostoNimi dokumenteista koostuvan tiedoston nimi.
    */
   public boolean lisää(String lisättävä, String tiedostoNimi) {
      // Luodaan taulukko merkkijonosta.
      String[] osat = lisättävä.split("///");
      
      // Muuttuja kertoo epäonnistuuko muunnos kokonaisluvuksi.
      int epäonnistuminen = 0;
      
      // Esitellään tunniste.
      int tunniste = 0;

      // Muodostetaan tunniste ja tarvittaessa tulostetaan virheilmoitus
      // ja muutetaan epäonnistumis-muuttujan arvoa.
      try {
         tunniste = Integer.parseInt(osat[0]);
      }
      catch (Exception e) {
         return false;
      }
      
      // Tarkastetaan onko kyseessä vitsi- vai uutistiedosto.
      if (tiedostoNimi.indexOf("jokes") != -1) {
        
         // Tarkastetaan, että ei anneta päivämäärää.
         if (osat[1].matches(".*\\d.*")) {
            return false;
         }
         
         // Lisättävä Vitsi-olio.
         Vitsi vitsi = new Vitsi(tunniste, osat[1], osat[2]);
         
         // Yritetään lisätä vitsiä kokoelmaan.
         try {
            lisää(vitsi);
            return true;
         }
         
         // Jos törmätään poikkeukseen, tulostetaan virheilmoitus.
         catch (Exception e) {
            return false;
         }
      }
      // Uutinen.
      else {
         // Luodaan taulukko päivämäärän osista.
         String[] pvmOsat = osat[1].split("[.]");
         
         // Esitellään muuttujat.
         int päivä = 0;
         int kuukausi = 0;
         int vuosi = 0;
         
         // Muutetaan päivämäärän osat kokonaisluvuiksi. Tarvittaessa
         // tulostetaan virheilmoitus ja muutetaan epäonnistumis-muuttujaa.
         try {
            päivä = Integer.parseInt(pvmOsat[0]);
            kuukausi = Integer.parseInt(pvmOsat[1]);
            vuosi = Integer.parseInt(pvmOsat[2]);
         }
         catch (Exception e) {
            return false;
         }

         // Muodostetaan LocalDate päivämäärän osista.
         LocalDate pvm = LocalDate.of(vuosi, kuukausi, päivä);
         
         // Luodaan uutinen parametrillisella rakentajalla.
         Uutinen uutinen = new Uutinen(tunniste, pvm, osat[2]);
         
         // Yritetään lisätä uutinen kokoelmaan.
         try {
            lisää(uutinen);
            return true;
         }
         
         // Törmätessä poikkeuksen, tulostetaan virheilmoitus.
         catch (Exception e) {
            return false;
         }
      }
   }

   /** 
    * Metodi hakee kokoelmasta tunnisteella yksilöidyn dokumentin.
    *
    * @param tunniste dokumentin yksilöivä tunniste.
    * @return dokumentit.get(y) (tietty dokumentti), jos dokumentti löydettiin
    * tunnisteen avulla, null, jos tunnistetta vastaavaa dokumenttia ei löydetty.
    */
   @Override
   public Dokumentti hae(int tunniste) {
      int y = 0;
      // Käydään dokumentteja läpi kunnes löydetään oikea.
      while (y < dokumentit.size()) {
         
         // Kun dokumentti löydetään palautetaan dokumentti.
         if (tunniste == dokumentit.get(y).tunniste()) {
            return dokumentit.get(y);
         }
         y++;
      }

      // Mikäli tunnistetta vastaavaa dokumenttia ei ole, palautetaan null.
      return null;
   }
}
// <- Own code