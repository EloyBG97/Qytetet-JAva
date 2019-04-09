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
public class Jugador implements Comparable{
    private boolean encarcelado;
    private String nombre;
    protected int saldo;
    private Sorpresa cartaLibertad;
    private ArrayList<TituloPropiedad> propiedades;
    private Casilla casillaActual;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.encarcelado = false;
        this.saldo = 7500;
        this.cartaLibertad = null;
        this.propiedades = new ArrayList<>();
    }
    
    protected Jugador(Jugador jugador) {
        this.nombre = jugador.nombre;
        this.encarcelado = jugador.encarcelado;
        this.saldo = jugador.saldo;
        this.cartaLibertad = jugador.cartaLibertad;
        this.propiedades = jugador.propiedades;
        this.casillaActual =  jugador.casillaActual;
    }
    
    boolean cancelarHipoteca(TituloPropiedad titulo) {
        int costeCancelarHipoteca = titulo.calcularCosteCancelar();
        boolean tengoSaldo = tengoSaldo(costeCancelarHipoteca);
        
        if(tengoSaldo) {
            modificarSaldo(-costeCancelarHipoteca);
            titulo.cancelarHipoteca();
        }
        
        return tengoSaldo;
    }
    
    boolean comprarTituloPropiedad() {
        int costeCompra = casillaActual.getCoste();
        boolean comprado = false;
        
        if(costeCompra < saldo) {
            TituloPropiedad titulo = ((Calle) casillaActual).asignarPropietario(this);
            propiedades.add(titulo);
            
            modificarSaldo(-costeCompra);
            comprado = true;
        }
        
        return comprado;
    }
    
    int cuantasCasasHotelesTengo() {
        int cuantasCasasHoteles = 0;
        
        for(TituloPropiedad propiedad : propiedades) {
            cuantasCasasHoteles = cuantasCasasHoteles + propiedad.getNumCasas();
            cuantasCasasHoteles = cuantasCasasHoteles + propiedad.getNumHoteles();
        }
        
        return cuantasCasasHoteles;
    }
    
    boolean deboPagarAlquiler() {
        TituloPropiedad titulo = casillaActual.getTitulo();
        boolean _encarcelado = false;
        boolean tienePropietario = false;
        boolean estaHipotecada = false;
        
        boolean esDeMiPropiedad = esDeMiPropiedad(titulo);
        
        if(!esDeMiPropiedad) {
            tienePropietario = titulo.tengoPropietario();
            
            if(tienePropietario) {
                _encarcelado = titulo.propietarioEncarcelado();
                
                if(!_encarcelado) {
                    estaHipotecada = titulo.isHipotecada();
                }
            }
            
            
        }
        
        return !esDeMiPropiedad && tienePropietario && !_encarcelado && !estaHipotecada;
    }
    
    Sorpresa devolverCartaLibertad() {
        Sorpresa carta = cartaLibertad;
        cartaLibertad = null;
        return carta;
    }
    
    boolean edificarCasa(TituloPropiedad titulo) {
       int numCasas = titulo.getNumCasas();
       boolean puedo = puedoEdificarCasa(titulo);
       
       if(puedo) {
           int costeEdificarCasa = titulo.getPrecioEdificar();
           titulo.edificarCasa();
           modificarSaldo(-costeEdificarCasa);
       }
           
       return puedo;
    }
    
    boolean edificarHotel(TituloPropiedad titulo) {
       int numHoteles = titulo.getNumHoteles();
       int numCasas = titulo.getNumCasas();
       
       boolean puedo = puedoEdificarHotel(titulo);
       
       if(puedo) {
            int costeEdificarHotel = titulo.getPrecioEdificar();
            titulo.edificarHotel();
            modificarSaldo(-costeEdificarHotel);
       }
           
       return puedo;
    }
    
    private void eliminarDeMisPropiedades(TituloPropiedad titulo) {
        propiedades.remove(titulo);
        titulo.setPropietario(null);
    }
    
    private boolean esDeMiPropiedad(TituloPropiedad titulo) {
        boolean finding = false;
        for(TituloPropiedad propiedad : propiedades) {
            finding = finding || propiedad == titulo;
        }
        
        return finding;
    }
    
    Sorpresa getCartaLibertad() {
        return cartaLibertad;
    }
    
    Casilla getCasillaActual() {
        return casillaActual;
    }
    
    boolean getEncarcelado() {
        return encarcelado;
    }
    
    String getNombre() {
        return nombre;
    }
    
    ArrayList<TituloPropiedad> getPropiedades() {
        return propiedades;
    }
    
    int getSaldo() {
        return saldo;
    }
    
    void hipotecarPropiedad(TituloPropiedad titulo) {
        int costeHipoteca = titulo.hipotecar();
        modificarSaldo(costeHipoteca);
    }
    
    void irACarcel(Casilla casilla) {
        setCasillaActual(casilla);
        setEncarcelado(true);
    }
    
    int modificarSaldo(int cantidad) {
        saldo += cantidad;

        return saldo;
    }
    
    int obtenerCapital(){
        int valorPropiedad = 0;
        int valorCasas = 0, valorHoteles = 0;
        int capitalJugador = 0;
        
        for(TituloPropiedad propiedad : propiedades) {
            valorCasas = propiedad.getNumCasas();
            valorHoteles = propiedad.getNumHoteles();
            valorPropiedad = (valorCasas + valorHoteles) * propiedad.getPrecioEdificar();
            
            if(propiedad.isHipotecada()) {
                capitalJugador += (valorPropiedad - propiedad.getHipotecaBase());
            }
            
            else {
                capitalJugador += valorPropiedad;
            }
        }
        
        return capitalJugador;
    }
    
    ArrayList<TituloPropiedad> obtenerPropiedades(boolean hipotecada) {
        ArrayList<TituloPropiedad>  tmp = new ArrayList<>();
        
        for(TituloPropiedad tp : propiedades) {
            if(tp.isHipotecada() == hipotecada) {
                tmp.add(tp);
            }
        }
        
        return tmp;
    }
    
    void pagarAlquiler() {
        int costeAlquiler = ((Calle) casillaActual).pagarAlquiler();
        modificarSaldo(-costeAlquiler);
    }
    
    protected void pagarImpuesto() {
        saldo -= casillaActual.getCoste();
    }
    
    void pagarLibertad(int cantidad) {
        boolean tengoSaldo = tengoSaldo(cantidad);
        
        if(tengoSaldo) {
            setEncarcelado(false);
            modificarSaldo(-cantidad);
        }
    }
    
    void setCartaLibertad(Sorpresa carta) {
        this.cartaLibertad = carta;
    }
    
    void setCasillaActual(Casilla casilla) {
        this.casillaActual = casilla;
    }
    
    void setEncarcelado(boolean encarcelado) {
        this.encarcelado = encarcelado;
    }
    
    boolean tengoCartaLibertad() {
        return (cartaLibertad != null);
    }
    
    protected boolean tengoSaldo(int cantidad) {
        return (saldo > cantidad);
    }
    
    boolean tengoPropiedades() {
        return !propiedades.isEmpty();
    }
    
    void venderPropiedad(Casilla casilla) {
        TituloPropiedad titulo = casilla.getTitulo();
        eliminarDeMisPropiedades(titulo);
        int precioVenta = titulo.calcularPrecioVenta();
        modificarSaldo(precioVenta);
        ((Calle) casilla).setTitulo(null);
    }
        
    protected  Especulador convertirme(int fianza) {
         return new Especulador(this, fianza);
    }
    
    protected boolean deboIrACarcel() {
        return tengoCartaLibertad();
    }
    
    protected boolean puedoEdificarCasa(TituloPropiedad titulo) {
        return (titulo.getNumCasas() < 4) && tengoSaldo(titulo.getPrecioEdificar());
    }
    
    protected boolean puedoEdificarHotel(TituloPropiedad titulo) {
        return (titulo.getNumHoteles() < 4 && titulo.getNumCasas() == 4 && 
                tengoSaldo(titulo.getPrecioEdificar()));
    }
    

    @Override
    public int compareTo(Object o) {
        int otroCapital = ((Jugador) o).obtenerCapital();
        return otroCapital - obtenerCapital();
    }
    
    @Override
   public String toString() {
       String str = "";
       String encarceladoStr = (this.encarcelado)?"(Encarcelado)":"";
       String cartaLibertadStr = (tengoCartaLibertad())?cartaLibertad.toString():"";
       str += nombre + " " + encarceladoStr + ": Saldo (" + Integer.toString(saldo)
               + ") Capital(" + Integer.toString(obtenerCapital()) + ")\n"
               + "Carta Libertad: " + cartaLibertadStr
               + "Propiedades: \n";
       
       for(TituloPropiedad tp : propiedades) {
           str += tp.toString() + "\n";
       }
       
           
       str += "Casilla Actual: " + casillaActual.toString();
       
       return str;
   }
}
