package org.example;

public class Tarea5 {

    public static void main(String[] args) {


    }
    class Extremo implements Runnable{

        public double a_Extremo = 0.0;

        public Extremo(double a_Extremo) {
            this.a_Extremo = a_Extremo;
        }

        @Override
        public void run() {

        }

    }

    class PuntoEnfoque {

        public double a_ExtremoDerecho = -5;
        public double a_ExtremoIzquierdo = 5;
        public double a_DistanciaMinima = 0.1;

    }
}