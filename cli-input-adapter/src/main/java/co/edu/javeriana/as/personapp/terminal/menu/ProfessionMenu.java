package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfessionInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProfessionMenu {

    private static final int OPCION_REGRESAR_MODULOS = 0;
    private static final int PERSISTENCIA_MARIADB = 1;
    private static final int PERSISTENCIA_MONGODB = 2;

    private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
    private static final int OPCION_VER_TODO = 1;
    private static final int OPCION_CREAR_PROFESION = 2;
    private static final int OPCION_ACTUALIZAR_PROFESION = 3;
    private static final int OPCION_BORRAR_PROFESION = 4;

    public void iniciarMenu(ProfessionInputAdapterCli professionInputAdapterCli, Scanner keyboard) {
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
                        professionInputAdapterCli.setProfessionOutputPortInjection("MARIA");
                        menuOpciones(professionInputAdapterCli, keyboard);
                        break;
                    case PERSISTENCIA_MONGODB:
                        professionInputAdapterCli.setProfessionOutputPortInjection("MONGO");
                        menuOpciones(professionInputAdapterCli, keyboard);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!isValid);
    }

    private void menuOpciones(ProfessionInputAdapterCli professionInputAdapterCli, Scanner keyboard) {
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
                        professionInputAdapterCli.listarProfesiones();
                        break;
                    case OPCION_CREAR_PROFESION:
                        crearProfesion(professionInputAdapterCli, keyboard);
                        break;
                    case OPCION_ACTUALIZAR_PROFESION:
                        actualizarProfesion(professionInputAdapterCli, keyboard);
                        break;
                    case OPCION_BORRAR_PROFESION:
                        borrarProfesion(professionInputAdapterCli, keyboard);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InputMismatchException e) {
                log.warn("Solo se permiten números.");
            }
        } while (!isValid);
    }

    private void crearProfesion(ProfessionInputAdapterCli professionInputAdapterCli, Scanner keyboard) {
        System.out.print("Ingrese el ID de la profesión: ");
        Integer id = keyboard.nextInt();
        keyboard.nextLine();

        System.out.print("Ingrese el nombre de la profesión: ");
        String nombre = keyboard.nextLine();

        System.out.print("Ingrese la descripción de la profesión: ");
        String descripcion = keyboard.nextLine();

        professionInputAdapterCli.crearProfesion(id, nombre, descripcion);
    }

    private void actualizarProfesion(ProfessionInputAdapterCli professionInputAdapterCli, Scanner keyboard) {
        System.out.print("Ingrese el ID de la profesión a actualizar: ");
        Integer id = keyboard.nextInt();
        keyboard.nextLine();

        System.out.print("Ingrese el nuevo nombre de la profesión: ");
        String nombre = keyboard.nextLine();

        System.out.print("Ingrese la nueva descripción de la profesión: ");
        String descripcion = keyboard.nextLine();

        professionInputAdapterCli.actualizarProfesion(id, nombre, descripcion);
    }

    private void borrarProfesion(ProfessionInputAdapterCli professionInputAdapterCli, Scanner keyboard) {
        System.out.print("Ingrese el ID de la profesión a eliminar: ");
        Integer id = keyboard.nextInt();

        professionInputAdapterCli.borrarProfesion(id);
    }

    private void mostrarMenuOpciones() {
        System.out.println("----------------------");
        System.out.println(OPCION_VER_TODO + " para ver todas las profesiones");
        System.out.println(OPCION_CREAR_PROFESION + " para crear una nueva profesión");
        System.out.println(OPCION_ACTUALIZAR_PROFESION + " para actualizar una profesión existente");
        System.out.println(OPCION_BORRAR_PROFESION + " para eliminar una profesión");
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
            keyboard.nextLine(); // Clear invalid input
            return leerOpcion(keyboard);
        }
    }
}
