package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelefonoMenu {

    private static final int OPCION_REGRESAR_MODULOS = 0;
    private static final int PERSISTENCIA_MARIADB = 1;
    private static final int PERSISTENCIA_MONGODB = 2;

    private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
    private static final int OPCION_VER_TODO = 1;
    private static final int OPCION_CREAR_TELEFONO = 2;
    private static final int OPCION_ACTUALIZAR_TELEFONO = 3;
    private static final int OPCION_BORRAR_TELEFONO = 4;

    public void iniciarMenu(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do {
            try {
                mostrarMenuMotorPersistencia();
                int opcion = leerOpcion(keyboard);
                switch (opcion) {
                    case OPCION_REGRESAR_MODULOS:
                        isValid = true;
                        break;
                    case PERSISTENCIA_MARIADB:
                        telefonoInputAdapterCli.setPhoneOutputPortInjection("MARIA");
                        menuOpciones(telefonoInputAdapterCli, keyboard);
                        break;
                    case PERSISTENCIA_MONGODB:
                        telefonoInputAdapterCli.setPhoneOutputPortInjection("MONGO");
                        menuOpciones(telefonoInputAdapterCli, keyboard);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!isValid);
    }

    private void menuOpciones(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do {
            try {
                mostrarMenuOpciones();
                int opcion = leerOpcion(keyboard);
                switch (opcion) {
                    case OPCION_REGRESAR_MOTOR_PERSISTENCIA:
                        isValid = true;
                        break;
                    case OPCION_VER_TODO:
                        telefonoInputAdapterCli.listarTelefonos();
                        break;
                    case OPCION_CREAR_TELEFONO:
                        crearTelefono(telefonoInputAdapterCli, keyboard);
                        break;
                    case OPCION_ACTUALIZAR_TELEFONO:
                        actualizarTelefono(telefonoInputAdapterCli, keyboard);
                        break;
                    case OPCION_BORRAR_TELEFONO:
                        borrarTelefono(telefonoInputAdapterCli, keyboard);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InputMismatchException e) {
                log.warn("Solo se permiten números.");
            }
        } while (!isValid);
    }

    private void crearTelefono(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
        System.out.print("Ingrese el número del teléfono: ");
        String number = keyboard.next();
        keyboard.nextLine();

        System.out.print("Ingrese la compañía del teléfono: ");
        String company = keyboard.nextLine();

        System.out.print("Ingrese el ID del dueño: ");
        Integer ownerId = keyboard.nextInt();

        telefonoInputAdapterCli.crearTelefono(number, company, ownerId);
    }

    private void actualizarTelefono(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
        System.out.print("Ingrese el número del teléfono a actualizar: ");
        String number = keyboard.next();
        keyboard.nextLine();

        System.out.print("Ingrese la nueva compañía del teléfono: ");
        String company = keyboard.nextLine();

        System.out.print("Ingrese el nuevo ID del dueño: ");
        Integer ownerId = keyboard.nextInt();

        telefonoInputAdapterCli.actualizarTelefono(number, company, ownerId);
    }

    private void borrarTelefono(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
        System.out.print("Ingrese el número del teléfono a eliminar: ");
        String number = keyboard.next();

        telefonoInputAdapterCli.borrarTelefono(number);
    }

    private void mostrarMenuOpciones() {
        System.out.println("----------------------");
        System.out.println(OPCION_VER_TODO + " para ver todos los teléfonos");
        System.out.println(OPCION_CREAR_TELEFONO + " para crear un nuevo teléfono");
        System.out.println(OPCION_ACTUALIZAR_TELEFONO + " para actualizar un teléfono existente");
        System.out.println(OPCION_BORRAR_TELEFONO + " para eliminar un teléfono");
        System.out.println(OPCION_REGRESAR_MOTOR_PERSISTENCIA + " para regresar");
    }

    private void mostrarMenuMotorPersistencia() {
        System.out.println("----------------------");
        System.out.println(PERSISTENCIA_MARIADB + " para MariaDB");
        System.out.println(PERSISTENCIA_MONGODB + " para MongoDB");
        System.out.println(OPCION_REGRESAR_MODULOS + " para regresar");
    }

    private int leerOpcion(Scanner keyboard) {
        try {
            System.out.print("Ingrese una opción: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Solo se permiten números.");
            keyboard.nextLine(); // Limpia el buffer
            return leerOpcion(keyboard);
        }
    }
}
