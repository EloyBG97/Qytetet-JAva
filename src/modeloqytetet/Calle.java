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
public class Calle extends Casilla {
    private TituloPropiedad titulo;


    public Calle(int numeroCasilla, TituloPropiedad titulo) {
        super(numeroCasilla, titulo.getPrecioCompra());
        this.titulo = titulo;
    }
    
    TituloPropiedad asignarPropietario(Jugador jugador) {
        titulo.setPropietario(jugador);
        return titulo;
    }
    
    int pagarAlquiler() {
        return  titulo.pagarAlquiler();
    }
    
    @Override
    protected boolean soyEdificable() {
        return true;
    }
    
    public boolean tengoPropietario() {
        return titulo.tengoPropietario();
    }
    
    @Override
    protected TipoCasilla getTipo() {
        return TipoCasilla.CALLE;
    }

    @Override
    protected TituloPropiedad getTitulo() {
        return titulo;
    }

    public void setTitulo(TituloPropiedad titulo) {
        this.titulo = titulo;
    }

    @Override
    public String toString() {
        return super.toString() + ", titulo=" + titulo + '}';
    }
}
