// Own code ->

package Harkka2020;

import java.util.LinkedList;

public class WeightedGraph {
    
    static class Kaari {
        int alku;
        int loppu;
        int paino;

        public Kaari(int alku, int loppu, int paino) {
            this.alku = alku;
            this.loppu = loppu;
            this.paino = paino;
        }
    }

    static class Graafi {
        
        int solmut;
        // Vierekkyyslista koostuen kaarista. Pohjautuu linkitettyyn listaan.
        LinkedList<Kaari> [] vierekkyyslista;

        Graafi(int solmut) {
            this.solmut = solmut;
            vierekkyyslista = new LinkedList[solmut];
            // Alustetaan vierekkyyslista jokaiselle solmulle.
            for (int i = 0; i < solmut; i++) {
                vierekkyyslista[i] = new LinkedList<>();
            }
        }

        // Metodi kaaren lisäämistä varten.
        public void lisääKaari(int alku, int loppu, int paino) {
            Kaari kaari = new Kaari(alku, loppu, paino);
            vierekkyyslista[alku].add(kaari);
        }
    }
}
// <- Own code