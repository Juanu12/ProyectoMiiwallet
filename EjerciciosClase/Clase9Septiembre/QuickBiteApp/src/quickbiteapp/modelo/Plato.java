/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quickbiteapp.modelo;

/**
 *
 * @author CSU22
 */
public class Plato {
    
    // Atributos
    private String nombre;
    private double precio;
    private int porcionesDisponibles;
    
    // un unico constructor que reciba los 3 valores

    public Plato(String nombre, double precio, int porcionesDisponibles) {
        this.nombre = nombre;
        this.precio = precio;
        this.porcionesDisponibles = porcionesDisponibles;
    }
    
    // Metodos
    
    
    public void mostrarInformacion() {
        
        System.out.println("El nombre del plato es" + nombre +
                "y el precio es" + precio + "y quedan disponibles" +porcionesDisponibles + "platos");
        
        
    }
    
    public double calcularSubtotal ( int cantidad) {
        
        double valorSubtotal = this.precio * cantidad;
              
        return valorSubtotal;
        
    }
    
    public boolean hayDisponibilidad ( int cantidad) {
        if (cantidad > porcionesDisponibles) {
            
           return false;
           
        }else {
            
            return true;
            
        } 
    }
    
    
    public void despachar(int cantidad) {
        
        boolean hayDisponibilidad = hayDisponibilidad(cantidad);
        
        if( hayDisponibilidad == true) {
            
            this.porcionesDisponibles = this.porcionesDisponibles - cantidad;
            
            System.out.println("Actualizadas las porciones disponibles");
            
        } else {
        
            System.out.println("¡Advertencia! no hay disponibilidad");
        
        }
        
        
    }

    public String getNombre() {
        return nombre;
    }

    public int getPorcionesDisponibles() {
        return porcionesDisponibles;
    }
    
    
    
    
    
    
    
    
}
