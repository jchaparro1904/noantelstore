import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        GestorPersistencia gestor = new GestorPersistencia();
        ArrayList<GestorPersistencia.Contacto> contactos = gestor.cargar();

        if (contactos.isEmpty()) {
            System.out.println("No hay contactos guardados.");
            return;
        }

        for (GestorPersistencia.Contacto c : contactos) {
            System.out.println("Nombre: " + c.getNombre());
            System.out.println("Correo: " + c.getCorreo());
            System.out.println("Mensaje: " + c.getMensaje());
            System.out.println("---------------------");
        }
    }
}
