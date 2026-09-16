import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;

public class GestorPersistencia {
    private static final String RUTA = Path.of(System.getProperty("user.dir"), "contactos.dat").toString();

    public void guardar(String nombre, String correo, String mensaje) {
        ArrayList<Contacto> contactos = cargar();
        contactos.add(new Contacto(nombre, correo, mensaje));

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(RUTA))) {
            out.writeObject(contactos);
            out.flush();
        } catch (Exception e) {
            System.err.println("[ERROR] Escritura fallida: " + e.getMessage());
        }
    }

    public ArrayList<Contacto> cargar() {
        File archivo = new File(RUTA);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(RUTA))) {
            Object obj = in.readObject();
            if (obj instanceof ArrayList<?>) {
                ArrayList<?> lista = (ArrayList<?>) obj;
                ArrayList<Contacto> contactos = new ArrayList<>();

                for (Object item : lista) {
                    if (item instanceof Contacto) {
                        contactos.add((Contacto) item);
                    }
                }

                return contactos;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Lectura fallida: " + e.getMessage());
        }

        return new ArrayList<>();
    }

    public void leerContactos() {
        ArrayList<Contacto> contactos = cargar();

        for (Contacto contacto : contactos) {
            System.out.println("Nombre: " + contacto.getNombre());
            System.out.println("Correo: " + contacto.getCorreo());
            System.out.println("Mensaje: " + contacto.getMensaje());
            System.out.println("---------------------");
        }
    }

    public static class Contacto implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String nombre;
        private final String correo;
        private final String mensaje;

        public Contacto(String nombre, String correo, String mensaje) {
            this.nombre = nombre;
            this.correo = correo;
            this.mensaje = mensaje;
        }

        public String getNombre() {
            return nombre;
        }

        public String getCorreo() {
            return correo;
        }

        public String getMensaje() {
            return mensaje;
        }
    }
}
