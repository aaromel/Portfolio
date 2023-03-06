// Own code ->
package harjoitustyo.kayttoliittyma;

import java.util.Scanner;
import harjoitustyo.kokoelma.*;
import harjoitustyo.dokumentit.*;
import harjoitustyo.omalista.*;

/**
 * Käyttöliittymä-luokka, johon kaikki ihmisen ja koneen vuorovaikutus 
 * pohjautuu.
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet I, kevät 2020.
 * <p>
 * @author Aaro Melchy (aaro.melchy@tuni.fi)
 */

public class Kayttoliittyma {
   
   /** Tiedoston nimi. */
   private String tiedostoNimi;

   /** Sulkusanatiedoston nimi. */
   private String sulkusanatiedosto;

   /** Dokumenteista koostuva kokoelma. */
   private Kokoelma kokoelma;

   /** Kertoo onko echo-komento päällä. */
   private boolean kaiutus;
   
   public Kayttoliittyma(String argumentti1, String argumentti2) {
      tiedostoNimi = argumentti1;
      sulkusanatiedosto = argumentti2;
      kokoelma = new Kokoelma();
      kaiutus = false;
   }
   
   /** 
    * Etsitään dokumentit jotka sisältävät käyttäjän määrittämät hakusanat.
    *
    * @param hakusanat Syötteenä annetut hakusanat.
    */
   public void etsi(OmaLista<String> hakusanat) {
      int z = 0;
      int koko = kokoelma.dokumentit().size();
      
      // Käydään läpi kaikki kokoelman dokumentit.
      while (z < koko) {
         
         // Liitetään haettavan dokumentin viite Dokumentti-olioon.
         Dokumentti dokumentti = kokoelma.dokumentit().get(z);
        
         // Kutsutaan Dokumentti-luokan sanatTäsmäävät-metodia ja mikäli
         // paluuarvo on true, tulostetaan dokumentin tunniste.
         if (dokumentti.sanatTäsmäävät(hakusanat) == true) {
            System.out.println(dokumentti.tunniste());
         }
         
         // Kasvatetaan laskuria.
         z++;
      }
   }
   
   /** 
    * Poistetaan tietty dokumentti kokoelmasta.
    *
    * @param tunniste dokumentin yksilöivä tunniste.
    */
   public void poista(int tunniste) {
      // Mikäli tunniste ei vastaa mitään dokumenttia, tulostetaan virheilmoitus.
      if (kokoelma.hae(tunniste) == null) {
         System.out.println("Error!");
      }
      
      // Jos tunniste vastaa jotain dokumenttia, kutsutaan Kokoelma-luokan
      // poista-metodia, joka poistaa dokumentin kokoelmasta.
      else {
         kokoelma.poista(tunniste);
      }
   }
   
   /** 
    * Suoritetaan kokoelman esikäsittely.
    *
    * @param välimerkit käyttäjän määrittämät merkit.
    */
   public void siivoa(String välimerkit) {
      int x = 0;
      int koko = kokoelma.dokumentit().size();
      
      // Käydään läpi jokainen kokoelman dokumentti.
      while (x < koko) {
         
         // Liitetään tietyn dokumentin viite Dokumentti-olioon.
         Dokumentti dokumentti = kokoelma.dokumentit().get(x);
         
         // Kutsutaan Dokumentti-luokan siivoa-metodia ja parametreiksi
         // annetaan sulkusanat ja välimerkit.
         dokumentti.siivoa(kokoelma.sulkusanat(), välimerkit);
         
         // Kasvatetaan laskuria.
         x++;
      }
   }
   
   /** 
    * Tulostetaan käyttäjän määrittämä dokumentti.
    *
    * @param tunniste dokumentin yksilöivä tunniste.
    */
   public void tulosta(int tunniste) {
      // Yritetään tulostaa dokumentti toString-metodin palauttamasta
      // merkkijonosta.
      try {
         System.out.println(kokoelma.hae(tunniste).toString());
      }
      
      // Mikäli törmätään poikkeukseen, tulostetaan virheilmoitus.
      catch (Exception e) {
         System.out.println("Error!");
      }
   }
   
   /** 
    * Tulostetaan kokoelman jokainen dokumentti.
    *
    */
   public void tulosta() {
      int v = 0;
      int koko = kokoelma.dokumentit().size();
      
      // Käydään läpi jokainen dokumentti.
      while (v < koko) {
         
         // Liitetään dokumentin viite Dokumentti-olioon.
         Dokumentti dokumentti = kokoelma.dokumentit().get(v);
         
         // Tulostetaan dokumentin toStringin palauttama merkkijono.
         System.out.println(dokumentti.toString());
         
         // Kasvatetaan laskuria.
         v++;
      }
   }
   
   /** 
    * Tarkastetaan käyttäjän syöttämä komento ja kutsutaan sen avulla haluttua
    * metodia.
    *
    * @param komento käyttäjän syöttämä komento.
    */
   public void lueKomento(String komento) {
      // Muuttuja, joka kertoo, onko komento virheellinen vai ei.
      boolean onnistuminen = false;
      
      // Jaetaan komento osiin.
      String[] osat = komento.split("[ ]");
      
      // Kaksi-osainen komento.
      if (osat.length == 2) {
         // Tulostus-komento.
         if (osat[0].equals("print")) {
            // Luodaan tunniste komennon toisesta osasta ja kutsutaan 
            // tulosta-metodia
            try {
               int tunniste = Integer.parseInt(osat[1]);
               tulosta(tunniste);
               onnistuminen = true;
            }
            
            // Napataan poikkeus.
            catch (Exception e) {
            }
         }
         
         // Poista-komento.
         if (osat[0].equals("remove")) {
            // Muodostetaan tunniste ja kutsutaan poista-metodia.
            try {
               int tunniste = Integer.parseInt(osat[1]);
               poista(tunniste);
               onnistuminen = true;
            }
            
            // Napataan poikkeus.
            catch (Exception e) {
            }
         }
         
         // Siivoa-komento.
         if (osat[0].equals("polish")) {
            // Kutsutaan siivoa-metodia.
            siivoa(osat[1]);
            onnistuminen = true;
         }
      }
      
      if (osat.length >= 2) { 
         // Etsimis-komento.
         if (osat[0].equals("find")) {
            // Luodaan OmaLista hakusanoille.
            OmaLista<String> hakusanat = new OmaLista<String>();
            int y = 1;
         
            // Käydään läpi jokainen hakusana ja lisätään se listalle.
            while (y < osat.length) {
               hakusanat.lisää(osat[y]);
               y++;
            }
         
            // Kutsutaan etsi-metodia.
            etsi(hakusanat);
            onnistuminen = true;
         }
         
         // Lisäys-komento.
         if (osat[0].equals("add")) {
            // Muodostetaan merkkijono komennosta ilman add-osaa.
            String uusiDokumentti = komento.replace("add ","");
            
            // Kutsutaan lisää-metodia.
            boolean lisäys = kokoelma.lisää(uusiDokumentti, tiedostoNimi);
            if (lisäys == true) {
               onnistuminen = true;
            }         
         }
      }
      
      // Yksiosainen komento.
      if (osat.length == 1) {
         
         // Muutosten peruminen.
         if (osat[0].equals("reset")) {
            
            // Luodaan uusi kokoelma ja liitetään viite kokoelma-attribuuttiin.
            kokoelma = new Kokoelma();
            
            // Kutsutaan lataa-metodia.
            kokoelma.lataa(tiedostoNimi, "dokumentit");
            kokoelma.lataa(sulkusanatiedosto, "sulkusanat");
            onnistuminen = true;
         }
         
         // Kaiutus-komento.
         if (osat[0].equals("echo")) {
            // Muutetaan attribuutin arvoa.
            if (kaiutus == false) {
               kaiutus = true;
            }
            else {
               kaiutus = false;
            }
            onnistuminen = true;
         }
         
         // Tulostus-komento ilman toista osaa.
         if (osat[0].equals("print")) {
            
            // Kutsutaan parametritonta tulosta-metodia.
            tulosta();
            onnistuminen = true;
         }
      }
      
      // Tarkastetaan muuttujan arvo ja tulostetaan tarvittaessa virheilmoitus.
      if (onnistuminen == false) {
         System.out.println("Error!");
      }
   }
   
   /** 
    * Käyttöliittymän pääsilmukka, joka ohjaa syötteiden antamista.
    *
    * Main-luokassa kutsutaan pääsilmukkaa ja ohjelma lopettaa toimintansa
    * pääsilmukan sulkeutuessa.
    */
   public void pääsilmukka() {
      // Muuttuja, joka ohjaa silmukkaa.
      int jatketaan = 1;
      
      // Suoritetaan lataukset ennen syötteiden pyytämistä.
      boolean lataus1 = kokoelma.lataa(tiedostoNimi, "dokumentit");
      
      boolean lataus2 = false; 
      
      // Toista latausta ei suoriteta, jos ensimmäinen epäonnistui.
      if (lataus1 == true) {
         lataus2 = kokoelma.lataa(sulkusanatiedosto, "sulkusanat");
      }
      
      // Mikäli latauksista toinen tai molemmat epäonnistui, suljetaan ohjelma.
      if (lataus1 == false || lataus2 == false) {
         jatketaan = 0;
      }
      
      // Lukija syötteitä varten.
      Scanner lukija = new Scanner(System.in);
      
      // Jatketaan syötteiden antamista ja ottamista niin kauan kunnes käyttäjä
      // antaa "quit" -komennon.
      while (jatketaan == 1) {
         // Pyydetään komentoa.
         System.out.println("Please, enter a command:");
         
         // Luetaan komento.
         String komento = lukija.nextLine();
         
         // Tulostetaan komento jos kaiutus on päällä tai annettiin
         // komento sitä varten.
         if (kaiutus == true || komento.equals("echo")) {
            System.out.println(komento);
         }
         
         // Suljetaan silmukka kun käyttäjä antaa komennon sitä varten.
         if (komento.equals("quit")) {
            jatketaan = 0;
         }
         
         // Muussa tapauksessa kutsutaan lueKomento-metodia jossa käsitellään
         // muut komennot.
         else {
            lueKomento(komento);
         }
      }
   }
}
// <- Own code 