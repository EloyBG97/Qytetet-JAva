/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfazUsuarioQytetet;
import modeloqytetet.Qytetet;
import java.util.ArrayList;
import java.util.Scanner;
import modeloqytetet.EstadoJuego;
import modeloqytetet.MetodoSalirCarcel;
/**
 *
 * @author eloy
 */
public class InterfazUsuarioQytetet {
    private Qytetet modelo;
    
    public InterfazUsuarioQytetet(){
        modelo = Qytetet.getInstance();
    }
    
    public static void main(String args[]) {
        InterfazUsuarioQytetet ui = new InterfazUsuarioQytetet();
        int operacionElegida, casillaElegida=0;
        boolean necesitaElegirCasilla;

        do {
            operacionElegida = ui.elegirOperacion();           
            necesitaElegirCasilla = ui.necesitaElegirCasilla(operacionElegida);
            
            if (necesitaElegirCasilla) 
                casillaElegida = ui.elegirCasilla(operacionElegida);
            
            if (!necesitaElegirCasilla || casillaElegida >= 0) 
                ui.realizarOperacion(operacionElegida, casillaElegida); 
            
        } while (1==1);
    }
    
    public ArrayList<String> obtenerNombreJugador() {
        Scanner sc = new Scanner(System.in);
        Integer nJugadoresInteger;
        ArrayList<String> nombres;
        
        do {
            System.out.println("Introduzca el numero de jugadores: ");
            String nJugadores = sc.nextLine();


            nJugadoresInteger = Integer.parseInt(nJugadores);
        }while(nJugadoresInteger == null);
                
        nombres = new ArrayList<>();
        
        for(int i = 0; i < nJugadoresInteger; ++i) {
            System.out.println("Introduzca el nombre del jugador " + i + ": ");
            nombres.add(sc.nextLine());
        }
        
        return nombres;
    }
    
    public int obtenerOpcionMenu(ArrayList<String> opcionesJuegoValidas) {
        int i = 0;
        int optionInt;
        String option;
        Scanner sc = new Scanner(System.in);
        String escogida;
        
        for(String str : opcionesJuegoValidas) {
            System.out.println(Integer.toString(i) + ": "  + str);
            ++i;
        }
        
        do {
                System.out.println("Elija  la opcion deseada: ");
                option = sc.next();
                
                optionInt = Integer.parseInt(option);
        }while(optionInt < 0 || optionInt > i);
               
        escogida = opcionesJuegoValidas.get(optionInt);
        
        return OpcionMenu.valueOf(escogida).ordinal();
    }
    
    public int elegirCasilla(int opcionMenu) {
        ArrayList<Integer> casillas = obtenerCasillasValidas(opcionMenu);
        Integer optionInt;
        String option;
        Scanner sc = new Scanner(System.in);
        
        if(casillas.isEmpty())
            return -1;
        
        else {
            for(Integer i : casillas) {
                System.out.println("Elegir casilla " + i);
            }
            
            do {
                System.out.println("Elija  la opcion deseada: ");
                option = sc.nextLine();
                optionInt = Integer.parseInt(option);
            }while(casillas.indexOf(optionInt) == -1);
            
            return optionInt;
        }
        
    }

    public ArrayList<String> obtenerOperacionesJuegoValidas() {
        ArrayList<String> opcionesValidas = new ArrayList<>();
        
        if(modelo.getEstadoJuego() == EstadoJuego.ALGUNJUGADORENBANCARROTA) {
            opcionesValidas.add(OpcionMenu.OBTENERRANKING.toString());
        }
        
        else if(modelo.getEstadoJuego() == EstadoJuego.JA_CONSORPRESA) {
            opcionesValidas.add(OpcionMenu.APLICARSORPRESA.toString());
        }
        
        else if(modelo.getEstadoJuego() == EstadoJuego.JA_ENCARCELADO) {
            opcionesValidas.add(OpcionMenu.PASARTURNO.toString());
        }
        
        else if(modelo.getEstadoJuego() == EstadoJuego.JA_ENCARCELADOCONOPCIONDELIBERTAD) {
            opcionesValidas.add(OpcionMenu.INTENTARSALIRCARCELTIRANDODADO.toString());
            opcionesValidas.add(OpcionMenu.INTENTARSALIRCARELPAGANDOLIBERTA.toString());
        }
        
        else if(modelo.getEstadoJuego() == EstadoJuego.JA_PREPARADO) {
            opcionesValidas.add(OpcionMenu.JUGAR.toString());
        }
        
        else if(modelo.getEstadoJuego() == EstadoJuego.JA_PUEDECOMPRAROGESTIONAR) {
            opcionesValidas.add(OpcionMenu.PASARTURNO.toString());
            opcionesValidas.add(OpcionMenu.CANCELARHIPOTECA.toString());
            opcionesValidas.add(OpcionMenu.COMPRARTITULOPROPIEDAD.toString());
            opcionesValidas.add(OpcionMenu.EDIFICARCASA.toString());
            opcionesValidas.add(OpcionMenu.EDIFICARHOTEL.toString());
            opcionesValidas.add(OpcionMenu.HIPOTECARPROPIEDAD.toString());
            opcionesValidas.add(OpcionMenu.VENDERPROPIEDAD.toString());
        }
        
        else if(modelo.getEstadoJuego() == EstadoJuego.JA_PUEDEGESTIONAR) {
            opcionesValidas.add(OpcionMenu.PASARTURNO.toString());
            opcionesValidas.add(OpcionMenu.CANCELARHIPOTECA.toString());
            opcionesValidas.add(OpcionMenu.EDIFICARCASA.toString());
            opcionesValidas.add(OpcionMenu.EDIFICARHOTEL.toString());
            opcionesValidas.add(OpcionMenu.HIPOTECARPROPIEDAD.toString());
            opcionesValidas.add(OpcionMenu.VENDERPROPIEDAD.toString());
        }
        
        opcionesValidas.add(OpcionMenu.MOSTRARJUGADORACTUAL.toString());
        opcionesValidas.add(OpcionMenu.MOSTRARJUGADORES.toString());
        opcionesValidas.add(OpcionMenu.MOSTRARTABLERO.toString());
        
        if(modelo.getJugadorActual() == null) {
            opcionesValidas.add(OpcionMenu.INICIARJUEGO.toString());
        }
        
        return opcionesValidas;
        
    }
    
    
    public ArrayList<Integer> obtenerCasillasValidas(int opcionMenu) {
        ArrayList<Integer> casillas = new ArrayList<>();
        
        if(opcionMenu == OpcionMenu.CANCELARHIPOTECA.ordinal()) {
            casillas = modelo.obtenerPropiedadesJugadorSegunEstadoHipoteca(true);
        }
        
        else if(opcionMenu == OpcionMenu.HIPOTECARPROPIEDAD.ordinal()) {
            casillas = modelo.obtenerPropiedadesJugadorSegunEstadoHipoteca(false);
        }
        
        else if(opcionMenu == OpcionMenu.EDIFICARCASA.ordinal()) {
            casillas = modelo.obtenerPropiedadesJugadorSegunEstadoHipoteca(false);
        }
        
        else if(opcionMenu == OpcionMenu.EDIFICARHOTEL.ordinal()) {
            casillas = modelo.obtenerPropiedadesJugadorSegunEstadoHipoteca(false);
        }
        
        else if(opcionMenu == OpcionMenu.VENDERPROPIEDAD.ordinal()) {
            casillas = modelo.obtenerPropiedadesJugador();
        }
        
        return casillas;
    }
    
    public boolean necesitaElegirCasilla(int opcionMenu) {
        
        return (opcionMenu == OpcionMenu.CANCELARHIPOTECA.ordinal() ||
                opcionMenu == OpcionMenu.EDIFICARCASA.ordinal() ||
                opcionMenu == OpcionMenu.EDIFICARHOTEL.ordinal() ||
                opcionMenu == OpcionMenu.HIPOTECARPROPIEDAD.ordinal() ||
                opcionMenu == OpcionMenu.VENDERPROPIEDAD.ordinal());
    }
        
    
    
    public void realizarOperacion(int opcionElegida, int casillaElegida) {
        
        System.out.println(Integer.toString(opcionElegida));
        if(opcionElegida == OpcionMenu.INICIARJUEGO.ordinal()) {
            System.out.println("Inicializar");
            ArrayList<String> nombres = obtenerNombreJugador();
            modelo.inicializarJuego(nombres);
        }
        
        else if(opcionElegida == OpcionMenu.APLICARSORPRESA.ordinal()) {
            System.out.println("Aplicar sorpresa " + modelo.getCartaActual().toString());
            modelo.aplicarSorpresa();
        }
        
        else if(opcionElegida == OpcionMenu.CANCELARHIPOTECA.ordinal()) {
            modelo.cancelarHipoteca(casillaElegida);
        }
        
        else if(opcionElegida == OpcionMenu.COMPRARTITULOPROPIEDAD.ordinal()) {
            modelo.comprarTituloPropiedad();                    
        }
        
        else if(opcionElegida == OpcionMenu.EDIFICARCASA.ordinal()) {
            modelo.edificarCasa(casillaElegida);
        }
        
        else if(opcionElegida == OpcionMenu.EDIFICARHOTEL.ordinal()) {
            modelo.edificarHotel(casillaElegida);
        }
        
        else if(opcionElegida == OpcionMenu.HIPOTECARPROPIEDAD.ordinal()) {
            modelo.hipotecarPropiedad(casillaElegida);
        }
        
        else if(opcionElegida == OpcionMenu.INTENTARSALIRCARCELTIRANDODADO.ordinal()) {
            modelo.intentarSalirCarcel(MetodoSalirCarcel.TIRANDODADO);
        }
        
        else if(opcionElegida == OpcionMenu.INTENTARSALIRCARELPAGANDOLIBERTA.ordinal()) {
            modelo.intentarSalirCarcel(MetodoSalirCarcel.PAGANDOLIBERTAD);
        }
        
        else if(opcionElegida == OpcionMenu.JUGAR.ordinal()) {
            modelo.jugar();
        }
        
        else if(opcionElegida == OpcionMenu.MOSTRARJUGADORACTUAL.ordinal()) {
            System.out.println(modelo.getJugadorActual().toString());
        }
        
        else if(opcionElegida == OpcionMenu.MOSTRARJUGADORES.ordinal()) {
            System.out.println(modelo.getJugadores().toString());
        }
        
        else if(opcionElegida == OpcionMenu.MOSTRARTABLERO.ordinal()) {
            System.out.println(modelo.getTablero().toString());
        }
        
        else if(opcionElegida == OpcionMenu.OBTENERRANKING.ordinal()) {
            modelo.obtenerRanking();
        }
        
        else if(opcionElegida == OpcionMenu.PASARTURNO.ordinal()) {
            modelo.siguienteJugador();
            System.out.println("Turno de " + modelo.getJugadorActual().toString());
        }
        
        else if(opcionElegida == OpcionMenu.TERMINARJUEGO.ordinal()) {
        }
        
        else if(opcionElegida == OpcionMenu.VENDERPROPIEDAD.ordinal()) {
            modelo.venderPropiedad(casillaElegida);
        }
    }
        
    int elegirOperacion() {
        ArrayList<String> opciones = obtenerOperacionesJuegoValidas();
        return obtenerOpcionMenu(opciones);
    }
        
}
