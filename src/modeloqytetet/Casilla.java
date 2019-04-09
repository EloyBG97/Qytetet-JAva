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
public abstract class Casilla {
    private int numeroCasilla;
    private int coste;

    public Casilla(int numeroCasilla, int coste) {
        this.numeroCasilla = numeroCasilla;
        this.coste = coste;
    }
    
    protected abstract boolean soyEdificable();
    
    int getNumeroCasilla() {
        return numeroCasilla;
    }

    int getCoste() {
        return coste;
    }

    void setCoste(int coste) {
        this.coste = coste;
    }
    
    protected abstract TipoCasilla getTipo();
    protected abstract TituloPropiedad getTitulo();
    
    @Override
    public String toString() {
        return "Casilla{" + "numeroCasilla=" + numeroCasilla + ", coste=" + coste + "}";
    }
}
