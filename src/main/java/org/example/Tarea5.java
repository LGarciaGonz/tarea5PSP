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

class Control extends Thread {

    private PuntoEnfoque a_PuntoEnfoque;

    public Control(PuntoEnfoque a_PuntoEnfoque) {
        this.a_PuntoEnfoque = a_PuntoEnfoque;
    }

    @Override
    public void run() {

        while ((a_PuntoEnfoque.a_ExtremoIzquierdo - a_PuntoEnfoque.a_ExtremoDerecho) > a_PuntoEnfoque.a_DistanciaMinima) {

            try {
                a_PuntoEnfoque.l_Semaforo.acquire(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("\n>>> Extremo derecho: " + a_PuntoEnfoque.a_ExtremoDerecho);
            System.out.println(">>> Extremo izquierdo: " + a_PuntoEnfoque.a_ExtremoIzquierdo);
            System.out.println(">>> Distancia mínima: " + a_PuntoEnfoque.a_DistanciaMinima);
            System.out.println("\n------------------------------------------");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

class Extremo extends Thread {

    private PuntoEnfoque a_PuntoEnfoque;
    private String a_Nombre;

    public Extremo(PuntoEnfoque a_PuntoEnfoque, String a_Nombre) {
        this.a_PuntoEnfoque = a_PuntoEnfoque;
        this.a_Nombre = a_Nombre;
    }

    @Override
    public void run() {

        while ((a_PuntoEnfoque.a_ExtremoIzquierdo - a_PuntoEnfoque.a_ExtremoDerecho) > a_PuntoEnfoque.a_DistanciaMinima) {

            if (a_Nombre.equals("Derecho")) {

                a_PuntoEnfoque.a_ExtremoDerecho = a_PuntoEnfoque.a_ExtremoDerecho - (a_PuntoEnfoque.a_ExtremoDerecho  / 5);

            } else if (a_Nombre.equals("Izquierdo")) {
                a_PuntoEnfoque.a_ExtremoIzquierdo = a_PuntoEnfoque.a_ExtremoIzquierdo - (a_PuntoEnfoque.a_ExtremoIzquierdo  / 5);
            }

            a_PuntoEnfoque.l_Semaforo.release();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// Clase que actúa como buzón.
class PuntoEnfoque {

    public double a_ExtremoDerecho = -5;
    public double a_ExtremoIzquierdo = 5;
    public double a_DistanciaMinima = 0.1;
    public Semaphore l_Semaforo = new Semaphore(1);

}