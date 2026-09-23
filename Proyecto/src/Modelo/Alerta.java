/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.time.LocalDate;

/**
 * Objeto que representa un registro de la tabla ALERTA.
 * nivel_severidad debe ser uno de: "bajo", "medio", "alto", "critico".
 * id_perfil e id_presupuesto pueden quedar en null si no aplican.
 *
 * @author CSU22
 */
public class Alerta {

    public int id_alerta;
    public int id_usuario;
    public Integer id_perfil;
    public Integer id_presupuesto;
    public String nivel_severidad;
    public String mensaje;
    public LocalDate fecha;

    public Alerta() {
    }

    public Alerta(int id_usuario, Integer id_perfil, Integer id_presupuesto, String nivel_severidad, String mensaje, LocalDate fecha) {
        this.id_usuario = id_usuario;
        this.id_perfil = id_perfil;
        this.id_presupuesto = id_presupuesto;
        this.nivel_severidad = nivel_severidad;
        this.mensaje = mensaje;
        this.fecha = fecha;
    }

    public Alerta(int id_alerta, int id_usuario, Integer id_perfil, Integer id_presupuesto, String nivel_severidad, String mensaje, LocalDate fecha) {
        this.id_alerta = id_alerta;
        this.id_usuario = id_usuario;
        this.id_perfil = id_perfil;
        this.id_presupuesto = id_presupuesto;
        this.nivel_severidad = nivel_severidad;
        this.mensaje = mensaje;
        this.fecha = fecha;
    }
}
