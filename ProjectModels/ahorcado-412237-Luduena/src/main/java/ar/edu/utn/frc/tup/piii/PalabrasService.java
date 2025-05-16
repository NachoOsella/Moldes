package ar.edu.utn.frc.tup.piii;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PalabrasService {

    private List<String> palabras = Arrays.asList("abrir", "acero", "acto", "agua", "aire", "alma", "alta", "amigo",
            "amor", "angel", "animal", "ansia", "apoyo", "arena", "arte", "asado", "asistir", "atajo", "atlas",
            "aumento", "aviso", "ayuda", "baño", "barco", "bello", "besar", "blusa", "boda", "bolsa", "bomba",
            "brote", "bueno", "cable", "cabra", "cacao", "caída", "caja", "calma", "cama", "cambio", "campo",
            "canto", "capaz", "carga", "carne", "casa", "causa", "cebolla", "ceder", "celeste", "centro",
            "cerca", "cerdo", "cerrar", "cielo", "cifra", "cinco", "clave", "clima", "color", "comer",
            "concierto", "conexión", "conocer", "correo", "corte", "cosa", "crecer", "creer", "crisis",
            "cruzar", "cuarto", "cubrir", "cuerda", "cuidar", "culto", "curso", "darle", "dato", "debate",
            "decir", "dedo", "defensa", "delito", "demás", "dentro", "derecho", "desde", "desear", "diente",
            "difícil", "dinero", "directo", "disco", "divino", "doblar", "doctor", "dolor", "dominar", "dormir",
            "dosel", "drama", "ducha", "dulce", "durar", "echar", "edad", "efecto", "eje", "elevar", "elogio",
            "embudo", "empezar", "empleo", "enano", "encima", "encono", "enemigo", "energía", "enfermo", "enlace",
            "enorme", "entidad", "entrada", "enviar", "episodio", "equipo", "eres", "escena", "escolar", "escudo",
            "esfera", "espacio", "esperar", "espiar", "esquina", "estado", "este", "estructura", "estudio", "evitar",
            "examen", "exceso", "existir", "experto", "explicar", "expresar", "extra", "fácil", "falso", "familia",
            "famoso", "fantasía", "farmacia", "favor", "febrero", "felicidad", "femenino", "fiesta", "figura", "fijar",
            "flecha", "flor", "fondo", "forma", "fortuna", "fotografía", "frase", "frente", "frío", "fuego", "fuerte",
            "fumar", "funda", "furgón", "futuro", "ganar", "garaje", "gasolina", "gato", "gemelo", "generar", "galleta",
            "ganar", "garra", "gasolina", "gemelo", "generar", "genio", "gesto", "girar", "gloria", "gobernar", "golpe",
            "gordo", "gorila", "gota", "gozar", "gracia", "granja", "grano", "gratis", "gris", "gritar", "grupo", "guante",
            "guerra", "guía", "haber", "hacer", "hallar", "harina", "hasta", "haz", "helado", "hermano", "héroe", "hervir",
            "hielo", "hierro", "hígado", "hogar", "hoja", "hombre", "hongo", "hora", "hotel", "hueso", "humano", "humo",
            "iglesia", "igual", "imagen", "impulso", "incluir", "incómodo", "indio", "infinito", "ingenio", "inicio",
            "inmenso", "inquieto", "insistir", "instante", "inteligente", "interés", "interno", "inútil", "invierno",
            "ira", "isla", "jabón", "jamón", "jarabe", "jardín", "jefe", "jengibre", "jerga", "jornada", "joven",
            "juego", "jugoso", "julio", "junio", "juntar", "justo", "juzgar", "labio", "lado", "ladrón", "laguna",
            "lágrima", "lamentar", "lápiz", "largo", "lasaña", "leal", "lección", "leche", "lector", "legumbre",
            "lejos", "lengua", "león", "letra", "levantar", "leyenda", "libertad", "libro", "licor", "líder", "lienzo",
            "ligeramente", "limón", "lindo", "línea", "líquido", "listo", "llama", "llanto", "llegar", "lleno", "llevar",
            "lluvia", "lobo", "locura", "lomo", "lote", "luchar", "luego", "lujo", "luna", "lunes", "lupa", "lustro",
            "luz", "maceta", "madera", "maestro", "mafia", "magia", "mago", "maíz", "maldad", "maleta", "malla", "malo",
            "mamá", "mandar", "manejar", "manga", "mano", "mansión", "manta", "mañana", "mapa", "maravilla", "marcar",
            "marcha", "marfil", "mármol", "marrón", "martes", "marzo", "masa", "máscara", "masivo", "matar", "máximo",
            "mayo", "mayor", "mecha", "medalla", "medir", "meditar", "médula");

    private Random random = new Random();
    /**
     * Este metodo retorna una palabra de manera aleatoria para empezar el juego.
     *
     * @return
     */
    public String getPalabra() {
        /*
         * Escribir un codigo que seleccione una palabra de manera aleatoria de la lista "palabras"
         */
        int indice = random.nextInt(palabras.size());
        return palabras.get(indice);
    }

    /**
     * Este metodo permite agregar nuevas palabras al juego.
     *
     * @param palabra
     */
    public void addNuevaPalabra(String palabra) {
        if (palabra != null && !palabra.trim().isEmpty()) {
            palabras.add(palabra.trim().toLowerCase());
        }
    }
}
