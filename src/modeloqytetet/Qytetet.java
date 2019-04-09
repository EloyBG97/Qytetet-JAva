/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author eloy
 */
/**
 * Av. Martinez Catena / 1000 / 550 / 98 / 165 Paseo de la marina española / 975
 * / 275 / 79 / 510 Paseo Alcalde Sanchez Prados / 625 / 400 / 91 / 740 PAseo de
 * las palmeras / 950 / 250 / 50 / 900 Paseo de Colon / 600 / 550 / 62 / 700
 * Calle Real / 800 / 500 / 75 / 235 Calle Juan I de Portugal / 575 / 400 / 52 /
 * 920 Av. de Africa / 650 / 350 / 86 / 350 Calle José Rojas Feigespan / 850 /
 * 625 / 59 / 900 Calle Brull / 775 / 670 / 66 / 180 Calle Alfau / 500 / 300 /
 * 77 / 565 Calle Velarde / 800 / 550 / 55 / 685
 *
 */
public class Qytetet {

    private ArrayList<Sorpresa> mazo;
    private Tablero tablero;
    public static final int MAX_JUGADORES = 4;
    static final int NUM_SORPRESAS = 12;
    public static final int NUM_CASILLAS = 20;
    public static final int PRECIO_LIBERTAD = 200;
    static final int SALDO_SALIDA = 1000;

    private Sorpresa cartaActual;
    private ArrayList<Jugador> jugadores;
    private Jugador jugadorActual;
    private Dado dado;
    private EstadoJuego estadoJuego;

    private static Qytetet INSTANCE = new Qytetet();

    private Qytetet() {
        mazo = new ArrayList<>();
        cartaActual = null;
        jugadores = new ArrayList<>();
        jugadorActual = null;
        dado = Dado.getInstance();

        inicializarTablero();
        inicializarCartasSorpresa();
    }

    void actuarSiEnCasillaEdificable() {
        boolean deboPagar = jugadorActual.deboPagarAlquiler();

        if (deboPagar) {
            jugadorActual.pagarAlquiler();
        }

        Casilla casilla = obtenerCasillaJugadorActual();

        boolean tengoPropietario = ((Calle) casilla).tengoPropietario();

        if (tengoPropietario) {
            setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        } else {
            setEstadoJuego(EstadoJuego.JA_PUEDECOMPRAROGESTIONAR);
        }

    }

    void actuarSiEnCasillaNoEdificable() {
        setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);

        Casilla casillaActual = jugadorActual.getCasillaActual();

        if (casillaActual.getTipo() == TipoCasilla.IMPUESTO) {
            jugadorActual.pagarImpuesto();
        } else if (casillaActual.getTipo() == TipoCasilla.JUEZ) {
            encarcelarJugador();
        } else if (casillaActual.getTipo() == TipoCasilla.SORPRESA) {
            cartaActual = mazo.remove(0);
            setEstadoJuego(EstadoJuego.JA_CONSORPRESA);
        }
    }

    public void aplicarSorpresa() {
        setEstadoJuego(EstadoJuego.JA_CONSORPRESA);
                
        if (cartaActual.getTipo() == TipoSorpresa.SALIRCARCEL) {
            jugadorActual.setCartaLibertad(cartaActual);
            
        }
        
        else {
            mazo.add(cartaActual);
            if (cartaActual.getTipo() == TipoSorpresa.IRACASILLA) {
                int valor = cartaActual.getValor();
                boolean casillaCarcel = tablero.esCasillaCarcel(valor);

                if (casillaCarcel) {
                    encarcelarJugador();
                } else {
                    mover(valor);
                }

            } else if (cartaActual.getTipo() == TipoSorpresa.PAGARCOBRAR) {
                jugadorActual.modificarSaldo(cartaActual.getValor());

                if (jugadorActual.getSaldo() < 0) {
                    setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
                }
            } else if (cartaActual.getTipo() == TipoSorpresa.PORCASAHOTEL) {
                int cantidad = cartaActual.getValor();
                int numeroTotal = jugadorActual.cuantasCasasHotelesTengo();

                jugadorActual.modificarSaldo(numeroTotal * cantidad);

                if (jugadorActual.getSaldo() < 0) {
                    setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
                }
            } else if (cartaActual.getTipo() == TipoSorpresa.PORJUGADOR) {
                jugadores.forEach((jugador) -> {
                    if (jugador != jugadorActual) {
                        jugador.modificarSaldo(-cartaActual.getValor());

                        if (jugador.getSaldo() <= 0) {
                            setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
                        }

                        jugadorActual.modificarSaldo(cartaActual.getValor());

                        if (jugadorActual.getSaldo() < 0) {
                            setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
                        }
                    }
                });
            }  else if (cartaActual.getTipo() == TipoSorpresa.CONVERTIRME) {
                int idx = jugadores.indexOf(jugadorActual);

                jugadorActual = jugadorActual.convertirme(cartaActual.getValor());
                jugadores.set(idx, jugadorActual);
                
                setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
            }
        }
    }

    public boolean cancelarHipoteca(int numeroCasilla) {
        setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        TituloPropiedad titulo = tablero.obtenerCasillaNumero(numeroCasilla).getTitulo();
        return jugadorActual.cancelarHipoteca(titulo);
    }

    public boolean comprarTituloPropiedad() {
        boolean comprado = jugadorActual.comprarTituloPropiedad();

        if (comprado) {
            setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        }

        return comprado;
    }

    public boolean edificarCasa(int numeroCasilla) {
        Casilla casilla = tablero.obtenerCasillaNumero(numeroCasilla);

        TituloPropiedad titulo = casilla.getTitulo();
        boolean edificada = jugadorActual.edificarCasa(titulo);

        if (edificada) {
            setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        }

        return edificada;
    }

    public boolean edificarHotel(int numeroCasilla) {
        Casilla casilla = tablero.obtenerCasillaNumero(numeroCasilla);

        TituloPropiedad titulo = casilla.getTitulo();
        boolean edificada = jugadorActual.edificarHotel(titulo);

        if (edificada) {
            setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        }

        return edificada;
    }

    private void encarcelarJugador() {
        if (jugadorActual.deboIrACarcel()) {
            Casilla casillaCarcel = tablero.getCarcel();

            jugadorActual.irACarcel(casillaCarcel);
            setEstadoJuego(EstadoJuego.JA_ENCARCELADO);
        } else {
            Sorpresa carta = jugadorActual.devolverCartaLibertad();
            mazo.add(carta);

            setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        }
    }

    public void hipotecarPropiedad(int numeroCasillas) {
        Casilla casilla = tablero.obtenerCasillaNumero(numeroCasillas);
        TituloPropiedad titulo = casilla.getTitulo();

        jugadorActual.hipotecarPropiedad(titulo);
        setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
    }

    private void inicializarCartasSorpresa() {
        mazo.add(new Sorpresa("Te conviertes en Especulador",
                5000, TipoSorpresa.CONVERTIRME));

        mazo.add(new Sorpresa("La venta de tus acciones te produce 50€",
                50, TipoSorpresa.PAGARCOBRAR));

        mazo.add(new Sorpresa("Paga la factura del médico",
                -50, TipoSorpresa.PAGARCOBRAR));

        mazo.add(new Sorpresa("Colocate en la casilla de salida",
                1, TipoSorpresa.IRACASILLA));

        mazo.add(new Sorpresa("Ve a la carcel",
                tablero.getCarcel().getNumeroCasilla(), TipoSorpresa.IRACASILLA));

        mazo.add(new Sorpresa("Retrocede hasta rotonda de Valencia",
                50, TipoSorpresa.IRACASILLA));

        mazo.add(new Sorpresa("Haz reparaciones en todos tus edificios",
                -25, TipoSorpresa.PORCASAHOTEL));

        mazo.add(new Sorpresa("Todos tus edificios han aumentado"
                + " la productividad de forma repentina",
                25, TipoSorpresa.PORCASAHOTEL));

        mazo.add(new Sorpresa("En tu cumpleaños, recibes de cada jugador 10€",
                10, TipoSorpresa.PORJUGADOR));

        mazo.add(new Sorpresa("En tu no cumpleaños, pagas a cada jugador 10€",
                -10, TipoSorpresa.PORJUGADOR));

        mazo.add(new Sorpresa("Quedas libre de la carcel",
                0, TipoSorpresa.SALIRCARCEL));

        mazo.add(new Sorpresa("Te conviertes en Especulador",
                3000, TipoSorpresa.CONVERTIRME));

    }

    public void inicializarJuego(ArrayList<String> nombres) {
        inicializarJugadores(nombres);
        inicializarTablero();
        inicializarCartasSorpresa();
        salidaJugadores();
        setEstadoJuego(EstadoJuego.JA_PREPARADO);
    }

    public void inicializarJugadores(ArrayList<String> nombres) {
        for (String nombre : nombres) {
            jugadores.add(new Jugador(nombre));
        }

        jugadorActual = jugadores.get(0);
    }

    private void inicializarTablero() {
        tablero = new Tablero();
    }

    public boolean intentarSalirCarcel(MetodoSalirCarcel metodo) {
        if (metodo == MetodoSalirCarcel.TIRANDODADO) {
            int resultado = dado.tirar();

            if (resultado >= 5) {
                jugadorActual.setEncarcelado(false);
            }
        } else if (metodo == MetodoSalirCarcel.PAGANDOLIBERTAD) {
            jugadorActual.pagarLibertad(PRECIO_LIBERTAD);
        }

        boolean libre = jugadorActual.getEncarcelado();

        if (libre) {
            setEstadoJuego(EstadoJuego.JA_PREPARADO);
        } else {
            setEstadoJuego(EstadoJuego.JA_ENCARCELADO);
        }

        return libre;
    }

    public void jugar() {
        int valor = tirarDado();
        Casilla casilla = tablero.obtenerCasillaFinal(jugadorActual.getCasillaActual(), valor);
        mover(casilla.getNumeroCasilla());
    }

    void mover(int numCasillaDestino) {
        Casilla casillaInicial = jugadorActual.getCasillaActual();
        Casilla casillaFinal = tablero.obtenerCasillaNumero(numCasillaDestino);

        jugadorActual.setCasillaActual(casillaFinal);

        if (numCasillaDestino < casillaInicial.getNumeroCasilla()) {
            jugadorActual.modificarSaldo(SALDO_SALIDA);
        }

        if (casillaFinal.soyEdificable()) {
            actuarSiEnCasillaEdificable();
        } else {
            actuarSiEnCasillaNoEdificable();
        }
    }

    public Casilla obtenerCasillaJugadorActual() {
        return jugadorActual.getCasillaActual();
    }

    public ArrayList<Casilla> obtenerCasillasTablero() {
        return tablero.getCasillas();
    }

    public ArrayList<Integer> obtenerPropiedadesJugador() {
        ArrayList<TituloPropiedad> propiedades = jugadorActual.getPropiedades();
        ArrayList<Integer> casillasPropiedades = new ArrayList<>();

        propiedades.forEach((propiedad) -> {
            tablero.getCasillas().forEach((casilla) -> {
                if (casilla.getTitulo() == propiedad) {
                    casillasPropiedades.add(casilla.getNumeroCasilla());
                }
            });
        });

        return casillasPropiedades;
    }

    public ArrayList<Integer> obtenerPropiedadesJugadorSegunEstadoHipoteca(boolean estadoHipoteca) {
        ArrayList<TituloPropiedad> propiedades = jugadorActual.obtenerPropiedades(estadoHipoteca);
        ArrayList<Integer> casillasPropiedades = new ArrayList<>();

        propiedades.forEach((propiedad) -> {
            tablero.getCasillas().forEach((casilla) -> {
                if (casilla.getTitulo() == propiedad) {
                    casillasPropiedades.add(casilla.getNumeroCasilla());
                }
            });
        });

        return casillasPropiedades;
    }

    public void obtenerRanking() {
        ArrayList<Jugador> copiaJugadores = new ArrayList<>(jugadores);
        Collections.sort(copiaJugadores);

        copiaJugadores.forEach((jugador) -> {
            System.out.println(jugador.toString());
        });
    }

    public int obtenerSaldoJugadorActual() {
        return jugadorActual.getSaldo();
    }

    private void salidaJugadores() {
        Casilla salida;

        salida = tablero.obtenerCasillaNumero(0);

        jugadores.forEach((jugador) -> {
            jugador.setCasillaActual(salida);
        });
    }

    private void setCartaActual(Sorpresa cartaActual) {
        this.cartaActual = cartaActual;
    }

    public void setEstadoJuego(EstadoJuego estadoJuego) {
        this.estadoJuego = estadoJuego;
    }

    public void siguienteJugador() {
        int siguienteJugador = (jugadores.indexOf(jugadorActual) + 1) % jugadores.size();

        jugadorActual = jugadores.get(siguienteJugador);
        
        setEstadoJuego(EstadoJuego.JA_PREPARADO);
    }

    int tirarDado() {
        return dado.tirar();
    }

    public void venderPropiedad(int numeroCasilla) {
        Casilla casilla = tablero.obtenerCasillaNumero(numeroCasilla);
        jugadorActual.venderPropiedad(casilla);
        setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
    }

    ArrayList<Sorpresa> getMazo() {
        return mazo;
    }

    public Sorpresa getCartaActual() {
        return cartaActual;
    }

    Dado getDado() {
        return dado;
    }

    public Jugador getJugadorActual() {
        return jugadorActual;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public int getValorDado() {
        return dado.getValor();
    }

    public Tablero getTablero() {
        return tablero;
    }

    public EstadoJuego getEstadoJuego() {
        return estadoJuego;
    }

    public static Qytetet getInstance() {
        return INSTANCE;
    }
}
