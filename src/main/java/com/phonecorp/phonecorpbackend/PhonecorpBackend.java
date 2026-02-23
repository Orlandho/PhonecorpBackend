package com.phonecorp.phonecorpbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicacion PhoneCorp Backend.
 * @SpringBootApplication activa el escaneo automatico de componentes
 * dentro del paquete raiz com.phonecorp.phonecorpbackend y sus subpaquetes.
 */
@SpringBootApplication
public class PhonecorpBackend {

    public static void main(String[] args) {
        SpringApplication.run(PhonecorpBackend.class, args);
    }
}

