/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

/**
 *
 * @author eloy
 */
public class Especulador extends Jugador {
    private int fianza;
    
    Especulador(Jugador jugador, int fianza) {
        super(jugador);
        this.fianza = fianza;
    }
    
    protected void pagarImpuesto(int cantidad) {
       modificarSaldo(-(int) (getCasillaActual().getCoste() * 0.5));    
    }
        
    @Override
    protected  Especulador convertirme(int finanza) {
        return this;
    }
    
    @Override
    protected boolean deboIrACarcel() {
        return (super.deboIrACarcel() && !pagarFianza());
    }
    
    private boolean pagarFianza() {
        boolean puedoPagar= tengoSaldo(fianza);
        if(puedoPagar) {
            modificarSaldo(-fianza);
        }
        
        return puedoPagar;
    }
    
    @Override
    protected boolean puedoEdificarCasa(TituloPropiedad titulo) {
        return (titulo.getNumCasas() < 8) && tengoSaldo(titulo.getPrecioEdificar());
    }
    
    @Override
    protected boolean puedoEdificarHotel(TituloPropiedad titulo) {
        return (titulo.getNumHoteles() < 4 && titulo.getNumCasas() == 4 && 
                tengoSaldo(titulo.getPrecioEdificar()) && getPropiedades().size() < 8);
    }
    
    @Override
    public String toString() {
        return super.toString() + "\nPrecio Fianza: " + Integer.toString(fianza);
    }
}
