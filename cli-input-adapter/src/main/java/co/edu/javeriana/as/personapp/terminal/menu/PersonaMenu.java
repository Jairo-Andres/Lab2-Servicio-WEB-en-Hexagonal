package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.adapter.PersonaInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonaMenu {

	private static final int OPCION_REGRESAR_MODULOS = 0;
	private static final int PERSISTENCIA_MARIADB = 1;
	private static final int PERSISTENCIA_MONGODB = 2;

	private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
	private static final int OPCION_VER_TODO = 1;
	private static final int OPCION_CREAR_PERSONA = 2;
	private static final int OPCION_ACTUALIZAR_PERSONA = 3;
	private static final int OPCION_BORRAR_PERSONA = 4;

	public void iniciarMenu(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
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
						personaInputAdapterCli.setPersonOutputPortInjection("MARIA");
						menuOpciones(personaInputAdapterCli, keyboard);
						break;
					case PERSISTENCIA_MONGODB:
						personaInputAdapterCli.setPersonOutputPortInjection("MONGO");
						menuOpciones(personaInputAdapterCli, keyboard);
						break;
					default:
						log.warn("La opción elegida no es válida.");
				}
			} catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
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
						personaInputAdapterCli.historial();
						break;
					case OPCION_CREAR_PERSONA:
						crearPersona(personaInputAdapterCli, keyboard);
						break;
					case OPCION_ACTUALIZAR_PERSONA:
						actualizarPersona(personaInputAdapterCli, keyboard);
						break;
					case OPCION_BORRAR_PERSONA:
						borrarPersona(personaInputAdapterCli, keyboard);
						break;
					default:
						log.warn("La opción elegida no es válida.");
				}
			} catch (InputMismatchException e) {
				log.warn("Solo se permiten números.");
			}
		} while (!isValid);
	}

	private void crearPersona(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
		System.out.print("Ingrese la cédula de la persona: ");
		Integer cc = keyboard.nextInt();
		keyboard.nextLine();

		System.out.print("Ingrese el nombre de la persona: ");
		String nombre = keyboard.nextLine();

		System.out.print("Ingrese el apellido de la persona: ");
		String apellido = keyboard.nextLine();

		System.out.print("Ingrese el género de la persona (M/F): ");
		String genero = keyboard.nextLine();

		System.out.print("Ingrese la edad de la persona: ");
		Integer edad = keyboard.nextInt();

		personaInputAdapterCli.crearPersona(cc, nombre, apellido, genero, edad);
	}

	// En el método actualizarPersona de PersonaMenu
	private void actualizarPersona(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
		System.out.print("Ingrese la cédula de la persona a actualizar: ");
		Integer cc = keyboard.nextInt();
		keyboard.nextLine();

		System.out.print("Ingrese el nuevo nombre de la persona: ");
		String nombre = keyboard.nextLine();

		System.out.print("Ingrese el nuevo apellido de la persona: ");
		String apellido = keyboard.nextLine();

		System.out.print("Ingrese el nuevo género de la persona (M/F): ");
		String genero = keyboard.nextLine();
		Gender genderEnum = null;
		if (genero.equalsIgnoreCase("M")) {
			genderEnum = Gender.MALE;
		} else if (genero.equalsIgnoreCase("F")) {
			genderEnum = Gender.FEMALE;
		} else {
			System.out.println("Género inválido. Debe ser 'M' o 'F'.");
			return; // Salir si el género es inválido
		}

		System.out.print("Ingrese la nueva edad de la persona: ");
		Integer edad = keyboard.nextInt();

		Person person = new Person(cc, nombre, apellido, genderEnum, edad, null, null);
		personaInputAdapterCli.actualizarPersona(cc, nombre, apellido, genero, edad);
	}

	private void borrarPersona(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
		System.out.print("Ingrese la cédula de la persona a eliminar: ");
		Integer cc = keyboard.nextInt();

		personaInputAdapterCli.borrarPersona(cc);
	}

	private void mostrarMenuOpciones() {
		System.out.println("----------------------");
		System.out.println(OPCION_VER_TODO + " para ver todas las personas");
		System.out.println(OPCION_CREAR_PERSONA + " para crear una nueva persona");
		System.out.println(OPCION_ACTUALIZAR_PERSONA + " para actualizar una persona existente");
		System.out.println(OPCION_BORRAR_PERSONA + " para eliminar una persona");
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
