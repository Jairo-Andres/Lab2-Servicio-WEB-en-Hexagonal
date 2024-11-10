package co.edu.javeriana.as.personapp.terminal.menu;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.EstudioInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EstudioMenu {

    private static final int OPCION_REGRESAR_MODULOS = 0;
    private static final int PERSISTENCIA_MARIADB = 1;
    private static final int PERSISTENCIA_MONGODB = 2;

    private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
    private static final int OPCION_VER_TODO = 1;
    private static final int OPCION_CREAR_ESTUDIO = 2;
    private static final int OPCION_ACTUALIZAR_ESTUDIO = 3;
    private static final int OPCION_BORRAR_ESTUDIO = 4;

    public void iniciarMenu(EstudioInputAdapterCli estudioInputAdapterCli, Scanner keyboard) {
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
                        estudioInputAdapterCli.setStudyOutputPortInjection("MARIA");
                        menuOpciones(estudioInputAdapterCli, keyboard);
                        break;
                    case PERSISTENCIA_MONGODB:
                        estudioInputAdapterCli.setStudyOutputPortInjection("MONGO");
                        menuOpciones(estudioInputAdapterCli, keyboard);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!isValid);
    }

    private void menuOpciones(EstudioInputAdapterCli estudioInputAdapterCli, Scanner keyboard) {
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
                        estudioInputAdapterCli.listarEstudios();
                        break;
                    case OPCION_CREAR_ESTUDIO:
                        crearEstudio(estudioInputAdapterCli, keyboard);
                        break;
                    case OPCION_ACTUALIZAR_ESTUDIO:
                        actualizarEstudio(estudioInputAdapterCli, keyboard);
                        break;
                    case OPCION_BORRAR_ESTUDIO:
                        borrarEstudio(estudioInputAdapterCli, keyboard);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InputMismatchException e) {
                log.warn("Solo se permiten números.");
                keyboard.nextLine();
            }
        } while (!isValid);
    }

    private void crearEstudio(EstudioInputAdapterCli estudioInputAdapterCli, Scanner keyboard) {
        try {
            System.out.print("Ingrese el ID de la persona: ");
            Integer personId = keyboard.nextInt();

            System.out.print("Ingrese el ID de la profesión: ");
            Integer professionId = keyboard.nextInt();

            keyboard.nextLine(); // Limpiar el buffer
            System.out.print("Ingrese el nombre de la universidad: ");
            String university = keyboard.nextLine();

            System.out.print("Ingrese la fecha de graduación (YYYY-MM-DD): ");
            LocalDate graduationDate = leerFecha(keyboard);

            estudioInputAdapterCli.crearEstudio(personId, professionId, university, graduationDate);
        } catch (InputMismatchException e) {
            log.warn("Entrada inválida. Por favor, ingrese los datos correctamente.");
            keyboard.nextLine(); // Limpiar el buffer
        }
    }

    private void actualizarEstudio(EstudioInputAdapterCli estudioInputAdapterCli, Scanner keyboard) {
        try {
            System.out.print("Ingrese el ID de la persona: ");
            Integer personId = keyboard.nextInt();

            System.out.print("Ingrese el ID de la profesión: ");
            Integer professionId = keyboard.nextInt();

            keyboard.nextLine(); // Limpiar el buffer
            System.out.print("Ingrese el nuevo nombre de la universidad: ");
            String university = keyboard.nextLine();

            System.out.print("Ingrese la nueva fecha de graduación (YYYY-MM-DD): ");
            LocalDate graduationDate = leerFecha(keyboard);

            estudioInputAdapterCli.actualizarEstudio(personId, professionId, university, graduationDate);
        } catch (InputMismatchException e) {
            log.warn("Entrada inválida. Por favor, ingrese los datos correctamente.");
            keyboard.nextLine(); // Limpiar el buffer
        }
    }

    private void borrarEstudio(EstudioInputAdapterCli estudioInputAdapterCli, Scanner keyboard) {
        try {
            System.out.print("Ingrese el ID de la persona: ");
            Integer personId = keyboard.nextInt();

            System.out.print("Ingrese el ID de la profesión: ");
            Integer professionId = keyboard.nextInt();

            estudioInputAdapterCli.borrarEstudio(personId, professionId);
        } catch (InputMismatchException e) {
            log.warn("Entrada inválida. Por favor, ingrese los datos correctamente.");
            keyboard.nextLine(); // Limpiar el buffer
        }
    }

    private void mostrarMenuOpciones() {
        System.out.println("----------------------");
        System.out.println(OPCION_VER_TODO + " para ver todos los estudios");
        System.out.println(OPCION_CREAR_ESTUDIO + " para crear un nuevo estudio");
        System.out.println(OPCION_ACTUALIZAR_ESTUDIO + " para actualizar un estudio existente");
        System.out.println(OPCION_BORRAR_ESTUDIO + " para eliminar un estudio");
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

    private LocalDate leerFecha(Scanner keyboard) {
        while (true) {
            try {
                String dateInput = keyboard.nextLine();
                return LocalDate.parse(dateInput);
            } catch (DateTimeParseException e) {
                System.out.print("Formato de fecha inválido. Ingrese la fecha en formato YYYY-MM-DD: ");
            }
        }
    }
}
