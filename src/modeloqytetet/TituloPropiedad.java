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
public class TituloPropiedad {
    private String nombre;
    private boolean hipotecada;
    private int precioCompra;
    private int alquilerBase;
    private float factorRevalorizacion;
    private int hipotecaBase;
    private int precioEdificar;
    private int numCasas;
    private int numHoteles;
    private Jugador propietario;

    public TituloPropiedad(String nombre, int precioCompra, int alquilerBase, float factorRevalorizacion, int hipotecaBase, int precioEdificar) {
        this.nombre = nombre;
        this.hipotecada = false;
        this.precioCompra = precioCompra;
        this.alquilerBase = alquilerBase;
        this.factorRevalorizacion = factorRevalorizacion;
        this.hipotecaBase = hipotecaBase;
        this.precioEdificar = precioEdificar;
        this.numCasas = 0;
        this.numHoteles = 0;
        this.propietario = null;
    }
    
    int calcularCosteCancelar() {
        return (int) (1.1 * calcularCosteHipotecar());
    }
    
    int calcularCosteHipotecar() {
        return (int) (hipotecaBase * (1 + (numCasas * 0.5 + numHoteles)));
    }
    
    int calcularImporteAlquiler() {
        return alquilerBase + (int) (numCasas * 0.5 + numHoteles * 2);
    } 

    int calcularPrecioVenta() {
        return (int) (precioCompra + (numCasas + numHoteles) * precioEdificar * factorRevalorizacion);
    }
    
    void cancelarHipoteca() {
        hipotecada = false;
    }
        
    void edificarCasa() {
        numCasas = numCasas + 1;
    }
    
    void edificarHotel() {
        numHoteles = numHoteles + 1;
        numCasas = 0;
    }
    
    int hipotecar() {
        int costeHipoteca = calcularCosteHipotecar();
        setHipotecada(true);
        
        return costeHipoteca;
    }
    
    int pagarAlquiler() {
        int costeAlquiler = calcularImporteAlquiler();
        propietario.modificarSaldo(costeAlquiler);
        
        return costeAlquiler;
    }
    
    boolean propietarioEncarcelado() {
        try {
            return propietario.getEncarcelado();
        }
        
        catch(Exception e) {
            throw new UnsupportedOperationException("Esta propiedad no tiene todavia asociado un propietario");
        }
    }
    
    boolean tengoPropietario() {
        return (propietario != null);
    }
    
    String getNombre() {
        return nombre;
    }

    boolean isHipotecada() {
        return hipotecada;
    }

    int getPrecioCompra() {
        return precioCompra;
    }

    int getAlquilerBase() {
        return alquilerBase;
    }

    float getFactorRevalorizacion() {
        return factorRevalorizacion;
    }

    int getHipotecaBase() {
        return hipotecaBase;
    }

    int getPrecioEdificar() {
        return precioEdificar;
    }

    int getNumCasas() {
        return numCasas;
    }

    int getNumHoteles() {
        return numHoteles;
    }

    void setHipotecada(boolean hipotecada) {
        this.hipotecada = hipotecada;
    }
    
    void setPropietario(Jugador propietario) {
        this.propietario = propietario;
    }

    @Override
    public String toString() {
        return "TituloPropiedad{" + "nombre=" + nombre + ", hipotecada=" + hipotecada + ", precioCompra=" + precioCompra + ", alquilerBase=" + alquilerBase + ", factorRevalorizacion=" + factorRevalorizacion + ", hipotecaBase=" + hipotecaBase + ", precioEdificar=" + precioEdificar + ", numCasas=" + numCasas + ", numHoteles=" + numHoteles + '}';
    }     
    
}
