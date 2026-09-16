import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        GestorPersistencia gestor = new GestorPersistencia();

        server.createContext("/contacto", exchange -> {
            String respuesta = "";
            int codigo = 200;
            try {
                String datos = exchange.getRequestURI().getQuery();
                if (datos == null || datos.isEmpty()) {
                    datos = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                }

                String nombre = "";
                String correo = "";
                String mensaje = "";

                String[] pares = datos.split("&");
                for (String par : pares) {
                    String[] partes = par.split("=", 2);
                    if (partes.length == 2) {
                        String clave = partes[0];
                        String valor = partes[1].replace("+", " ");

                        if (clave.equals("nombre")) {
                            nombre = valor;
                        } else if (clave.equals("correo") || clave.equals("email")) {
                            correo = valor;
                        } else if (clave.equals("mensaje")) {
                            mensaje = valor;
                        }
                    }
                }

                nombre = nombre.trim();
                correo = correo.trim();
                mensaje = mensaje.trim();

                if (nombre.isEmpty() || correo.isEmpty() || mensaje.isEmpty()) {
                    throw new IllegalArgumentException("Faltan nombre, correo o mensaje");
                }

                gestor.guardar(nombre, correo, mensaje);
                respuesta = "<h2>Mensaje enviado correctamente</h2>";
            } catch (Exception e) {
                respuesta = "<h2 style='color:red'>Error: " + e.getMessage() + "</h2>";
                codigo = 400;
            } finally {
                byte[] bytes = respuesta.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(codigo, bytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(bytes);
                os.close();
            }
        });

        server.start();
        System.out.println("Servidor iniciado en http://localhost:8080");
    }
}
