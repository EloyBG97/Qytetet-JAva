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
public class OtraCasilla extends Casilla {
    private int numeroCasilla;
    private int coste;
    private TipoCasilla tipo;

    public OtraCasilla(int numeroCasilla, int coste, TipoCasilla tipo) {
        super(numeroCasilla, coste);
        this.tipo = tipo;
    }

    @Override
    protected boolean soyEdificable() {
        return false;
    }

    @Override
    protected TipoCasilla getTipo() {
        return tipo;
    }
    
    @Override
    protected TituloPropiedad getTitulo() {
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + ", tipo=" + tipo +  '}';
    }
}
