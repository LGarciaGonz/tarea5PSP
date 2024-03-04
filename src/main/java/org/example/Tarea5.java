package org.example;

import javax.sound.midi.Soundbank;
import java.util.concurrent.Semaphore;

public class Tarea5 {

    public static void main(String[] args) {

        PuntoEnfoque l_PuntoEnfoque = new PuntoEnfoque();

        // Creación de los objetos.
        Extremo l_ObjRunnable1 = new Extremo(l_PuntoEnfoque, "Derecho");
        Extremo l_ObjRunnable2 = new Extremo(l_PuntoEnfoque, "Izquierdo");
        Control l_ObjRunnable3 = new Control(l_PuntoEnfoque);

        // Creación de los hilos.
        Thread l_ExtDer = new Thread(l_ObjRunnable1);
        Thread l_ExtIzq = new Thread(l_ObjRunnable2);
        Thread l_Control = new Thread(l_ObjRunnable3);

        // Inicio de la ejecución de los hilos.
        l_ExtDer.start();
        l_ExtIzq.start();
        l_Control.start();
    }
}

// Clase que controla el los puntos de enfoque.
class Control extends Thread {
    private PuntoEnfoque a_PuntoEnfoque;

    // Constructor.
    public Control(PuntoEnfoque a_PuntoEnfoque) {
        this.a_PuntoEnfoque = a_PuntoEnfoque;
    }

    @Override
    public void run() {
        // El hilo se ejecuta mientras la distancia entre los extremos sea mayor que la distancia mínima.
        while ((a_PuntoEnfoque.a_ExtremoIzquierdo - a_PuntoEnfoque.a_ExtremoDerecho) > a_PuntoEnfoque.a_DistanciaMinima) {

            try {
                // Adquirir 2 permisos del semáforo.
                a_PuntoEnfoque.l_Semaforo.acquire(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);      // Informar del error producido.
            }

            // Imprime información sobre los extremos y la distancia mínima.
            System.out.println("\n>>> Extremo derecho: " + a_PuntoEnfoque.a_ExtremoDerecho);
            System.out.println(">>> Extremo izquierdo: " + a_PuntoEnfoque.a_ExtremoIzquierdo);
            System.out.println(">>> Distancia mínima: " + a_PuntoEnfoque.a_DistanciaMinima);
            System.out.println("\n------------------------------------------");

            try {
                Thread.sleep(500); // Espera de 500 milisegundos entre las ejecuciones.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

// Clase que controla los extremos.
class Extremo extends Thread {

    private PuntoEnfoque a_PuntoEnfoque;
    private String a_Nombre;

    // Constructor.
    public Extremo(PuntoEnfoque a_PuntoEnfoque, String a_Nombre) {
        this.a_PuntoEnfoque = a_PuntoEnfoque;
        this.a_Nombre = a_Nombre;
    }

    @Override
    public void run() {
        // El hilo se ejecuta mientras la distancia entre los extremos sea mayor que la distancia mínima.
        while ((a_PuntoEnfoque.a_ExtremoIzquierdo - a_PuntoEnfoque.a_ExtremoDerecho) > a_PuntoEnfoque.a_DistanciaMinima) {

            // Si es el extremo derecho, se mueve reduciendo su posición en un quinto.
            if (a_Nombre.equals("Derecho")) {
                a_PuntoEnfoque.a_ExtremoDerecho -= (a_PuntoEnfoque.a_ExtremoDerecho / 5);
            }
            // Si es el extremo izquierdo, se mueve reduciendo su posición en un quinto.
            else if (a_Nombre.equals("Izquierdo")) {
                a_PuntoEnfoque.a_ExtremoIzquierdo -= (a_PuntoEnfoque.a_ExtremoIzquierdo / 5);
            }

            a_PuntoEnfoque.l_Semaforo.release(); // Libera el semáforo para que otro hilo pueda adquirirlo.

            try {
                Thread.sleep(1000); // Espera de 1 segundo.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// Clase que actúa como buzón.
class PuntoEnfoque {

    public double a_ExtremoDerecho = -5; // Posición del extremo derecho.
    public double a_ExtremoIzquierdo = 5; // Posición del extremo izquierdo.
    public double a_DistanciaMinima = 0.1; // Distancia mínima permitida entre los extremos.
    public Semaphore l_Semaforo = new Semaphore(1); // Semáforo para controlar los movimientos.

}
