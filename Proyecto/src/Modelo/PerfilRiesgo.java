/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 * Objeto que representa un registro de la tabla PERFIL_RIESGO.
 *
 * @author CSU22
 */
public class PerfilRiesgo {

    public int id_perfil;
    public int id_resumen;
    public int id_nivel;
    public double porcentaje_gasto_ingreso;

    public PerfilRiesgo() {
    }

    public PerfilRiesgo(int id_resumen, int id_nivel, double porcentaje_gasto_ingreso) {
        this.id_resumen = id_resumen;
        this.id_nivel = id_nivel;
        this.porcentaje_gasto_ingreso = porcentaje_gasto_ingreso;
    }

    public PerfilRiesgo(int id_perfil, int id_resumen, int id_nivel, double porcentaje_gasto_ingreso) {
        this.id_perfil = id_perfil;
        this.id_resumen = id_resumen;
        this.id_nivel = id_nivel;
        this.porcentaje_gasto_ingreso = porcentaje_gasto_ingreso;
    }
}
