/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;
import java.util.Random;

/**
 *
 * @author eloy
 */
class Dado {
    private int valor;
    private static final Dado INSTANCE = new Dado();
    
    private Dado() {}
    
    int tirar() {
        Random rnd = new Random();
        valor = rnd.nextInt(6) + 1;
        
        return valor;
        
    }

    public int getValor() {
        return valor;
    }

    public static Dado getInstance() {
        return INSTANCE;
    }
}
