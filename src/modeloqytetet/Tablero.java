/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

import java.util.ArrayList;
/**
 *
 * @author eloy
 */

/**
 * SALIDA
 * Av. Martinez Catena / 1000 / 550 / 98 / 165
 * IMPUESTO
 * Paseo de la marina española / 975 / 275 / 79 / 510
 * SORPRESA
 * Paseo Alcalde Sanchez Prados / 625 / 400 / 91 / 740
 * PAseo de las palmeras / 950 / 250 / 50 / 900
 * CARCEL
 * Paseo de Colon / 600 / 550 / 62 / 700
 * Calle Real / 800 / 500 / 75 / 850
 * SORPRESA
 * Calle Juan I de Portugal / 575 / 400 / 52 / 920
 * Av. de Africa / 650 / 350 / 86 / 350
 * PARKING
 * Calle José Rojas Feigespan / 850 / 625 / 59 / 900
 * Calle Brull / 775 / 670 / 66 / 180
 * SORPRESA
 * Calle Alfau / 500 / 300 / 77 / 565
 * Calle Velarde / 800 / 550 / 55 / 685
 * JUEZ
 **/

public class Tablero {
    private ArrayList<Casilla> casillas;
    private Casilla carcel;

    public Tablero() {
        inicializar();
    }
    
    private void inicializar() {
        casillas = new ArrayList<>(20);
        


        casillas.add(new OtraCasilla(0,0, TipoCasilla.SALIDA));
        casillas.add(new Calle(1, new TituloPropiedad("Av. Martinez Catena", 1000,550, 12 , 98, 165)));
        casillas.add(new OtraCasilla(2,0, TipoCasilla.IMPUESTO));
        casillas.add(new Calle(3, new TituloPropiedad("Paseo de la Marina Espanola", 975,275, -17 , 79, 510)));
        casillas.add(new OtraCasilla(4,0, TipoCasilla.SORPRESA));
        casillas.add(new Calle(5, new TituloPropiedad("Paseo Alcalde Sanchez Prados", 625,400, 10 , 91, 740)));
        casillas.add(new Calle(6, new TituloPropiedad("Paseo de las Palmeras", 950,250, -12 , 50, 900)));
        casillas.add(new OtraCasilla(7,0, TipoCasilla.CARCEL));
        casillas.add(new Calle(8, new TituloPropiedad("Paseo de Colon", 600,550, 13 , 62, 700)));
        casillas.add(new Calle(9, new TituloPropiedad("C/ Real", 800,500, 20 , 75, 850)));
        casillas.add(new OtraCasilla(10,0, TipoCasilla.SORPRESA));
        casillas.add(new Calle(11, new TituloPropiedad("C/ Juan I de Portugal", 575,40, -15 , 52, 920)));
        casillas.add(new Calle(12, new TituloPropiedad(" Av. de Africa", 650,350, -17 , 86, 350)));
        casillas.add(new OtraCasilla(13,0, TipoCasilla.PARKING));
        casillas.add(new Calle(14, new TituloPropiedad("C/ José Rojas Feigespan", 850,625, 16 , 59, 900)));
        casillas.add(new Calle(15, new TituloPropiedad("C/ Brull", 775,670, 11 , 66, 180)));
        casillas.add(new OtraCasilla(16,0, TipoCasilla.SORPRESA));
        casillas.add(new Calle(17, new TituloPropiedad("C/ Alfau", 500,300, -19 , 77, 565)));
        casillas.add(new Calle(18, new TituloPropiedad("C/ Velarde", 800,550, 14 , 55, 685)));
        casillas.add(new OtraCasilla(19,0, TipoCasilla.JUEZ));
        
        carcel = casillas.get(7);
        
        assert casillas.size() == Qytetet.NUM_CASILLAS : "Hay mas de " + Integer.toString(Qytetet.NUM_CASILLAS) + " casillas.";
    }
    
    boolean esCasillaCarcel(int numeroCasilla) {
        return (carcel.getNumeroCasilla() == numeroCasilla);
    }
    
    Casilla obtenerCasillaFinal(Casilla casilla, int desplazamiento) {
        int casillaFinal = casilla.getNumeroCasilla() + desplazamiento;
        return obtenerCasillaNumero(casillaFinal);
    }
    
    Casilla obtenerCasillaNumero(int numeroCasilla) {
        int i = 0;
        
        while(casillas.get(i).getNumeroCasilla() != numeroCasilla)
            ++i;
        
        return casillas.get(i);
    }

    ArrayList<Casilla> getCasillas() {
        return casillas;
    }

    Casilla getCarcel() {
        return carcel;
    }

    @Override
    public String toString() {
        return "Tablero{" + "casillas=" + casillas + ", carcel=" + carcel + '}';
    }
  
}
