package domain;

/**
 * Representa el suelo 
 */
public class CheckeredFloor extends BoardElement {
    private double width, height;

    /**
     * Crea un nuevo suelo
     * @param position Coordenada superior izquierda (X, Y) donde empieza el suelo
     * @param width    El ancho total del rectángulo del suelo
     * @param height   La altura total del rectángulo del suelo
     */
    public CheckeredFloor(Position position, double width, double height) {
        super(position);
        this.width = width;
        this.height = height;
    }

    /** 
     * @return El ancho del suelo. 
     */
    public double getWidth() { return width; }
    
    /**
     *  @return La altura del suelo. 
     */
    public double getHeight() { return height; }

    @Override
    public java.util.Map<String, String> toVisualMap() {
        java.util.Map<String, String> data = new java.util.HashMap<>();
        if (isActive) {
            data.put("shape", "CHECKER");
            data.put("x", String.valueOf((int)position.getX()));
            data.put("y", String.valueOf((int)position.getY()));
            data.put("w", String.valueOf((int)width));
            data.put("h", String.valueOf((int)height));
            data.put("r", "255");
            data.put("g", "255");
            data.put("b", "255");
            data.put("br", "0");
            data.put("bg", "0");
            data.put("bb", "0");
        }
        return data;
    }
}

/**
 * Guía Definitiva: Cómo implementar cualquier elemento nuevo en DopoHardestGame
Si necesitas añadir CUALQUIER COSA al juego (una moneda que te teletransporte, un enemigo que dispare, una pared que se mueva, o un jugador invisible) y necesitas que funcione física y visualmente, debes seguir rigurosamente estos 6 PASOS.

Esta guía es extremadamente específica y contempla cómo funciona toda la arquitectura de tu código.

PASO 1: Elegir de quién heredar (Capa de Dominio)
No puedes crear una clase de cero sin conectarla al ecosistema. Tienes que heredar de la clase base correcta en src/domain/:

¿Es un Enemigo? Debe extender de Enemy. (O crearle un nuevo MovementStrategy).
¿Es un Coleccionable (Moneda/Item)? Debe extender de Coin o CollectableElement.
¿Es un Obstáculo que bloquea el paso? Debe implementar la interfaz Solid (ej. como la clase Wall).
¿Es una apariencia/poder del Jugador? Debe implementar PlayerSkin.
IMPORTANT

Ejemplo: Si quieres hacer una Moneda de Fuego, creas public class FireCoin extends Coin. Si tu clase necesita actuar al ser recolectada, sobrescribes el método onCollect(Interactable collector).

PASO 2: La Física y las Colisiones (Hitbox)
Si tu objeto es atravesable como un fantasma y no interactúa, es porque le falta o tiene mal configurada su "caja de colisión" (Hitbox). Todo elemento interactuable debe tener un método getHitbox(). Si heredas de clases existentes, ya lo tienen, pero si creas algo muy diferente, debes asegurarte de definirlo.

Tienes dos opciones de física en tu juego:

RectangularHitbox: Para paredes y el jugador.
CircularHitbox: Para monedas y enemigos.
Regla de oro: El Hitbox siempre debe actualizarse si el objeto se mueve, por lo que el método getHitbox() casi siempre usa this.position en su cálculo.

PASO 3: Lograr que se vea en Pantalla (toVisualMap)
Tu juego está muy bien diseñado bajo el Patrón MVC. Por obligación de arquitectura, NO PUEDES usar librerías como java.awt.Color o Graphics dentro de la carpeta domain.

Para que la pantalla lo dibuje, debes usar el diccionario de datos. Debes sobrescribir el método toVisualMap() en la clase que creaste en el Paso 1.

java

@Override
public java.util.Map<String, String> toVisualMap() {
    // 1. Siempre llama al super para heredar las coordenadas básicas
    java.util.Map<String, String> data = super.toVisualMap();
    
    // 2. Si el objeto está activo en el tablero, configuras su aspecto:
    if (this.isActive()) {
        data.put("shape", "OVAL"); // o "RECT" si quieres que sea un cuadrado
        data.put("r", "255");      // Rojo (De 0 a 255)
        data.put("g", "100");      // Verde
        data.put("b", "0");        // Azul
        
        // Si quieres cambiar el tamaño (w = ancho, h = alto):
        // data.put("w", "30");
        // data.put("h", "30");
    }
    return data;
}
La capa de Presentación leerá este diccionario y pintará un óvalo naranja (255, 100, 0) automáticamente.

PASO 4: Darle vida en el Bucle Principal (Opcional: Tickable)
Si tu elemento se mueve solo (como un enemigo) o tiene una cuenta regresiva (como una bomba a punto de estallar), necesita ser notificado de que el tiempo está pasando.

Para eso, la clase debe implementar la interfaz Tickable.
Esto te obligará a escribir el método public void tick(Board board). Ahí dentro pondrás la lógica que quieres que ocurra 60 veces por segundo (ej. avanzar la posición o restar tiempo).
PASO 5: Enseñarle al Juego a leerlo (LevelLoader / Factory)
Si hiciste todo lo anterior, el objeto existe, pero el juego no sabe cómo sacarlo de un archivo .txt.

Si es un Enemigo: Abre EnemyFactory.java (que creamos en la refactorización) y añade un nuevo bloque en el static { }:

java

registry.put("FUEGO", parts -> {
    double x = Double.parseDouble(parts[2]);
    double y = Double.parseDouble(parts[3]);
    double speed = Double.parseDouble(parts[4]);
    return new FireEnemy(new Position(x, y), speed);
});
Si es una Moneda u otro elemento: Abre LevelLoader.java. Busca el método parseCoin() o parseItem(). Añade tu condición:

java

} else if (type.equals("FIRE")) {
    FireCoin coin = new FireCoin(new Position(x, y));
    board.addVisual(coin);       // <--- IMPORTANTE: Hace que se dibuje
    board.addInteractable(coin); // <--- IMPORTANTE: Hace que se pueda chocar con él
    board.addCollectable(coin);  // <--- IMPORTANTE: Permite que el Player lo recoja
}
WARNING

Es un error hiper-común olvidar los board.add...(). Si omites addVisual, será invisible. Si omites addInteractable, será un fantasma.

PASO 6: Ponerlo en el nivel de texto (.txt)
Finalmente, ve a la carpeta levels/, abre tu archivo level1.txt (o el que estés usando), y usa la palabra clave que programaste en el Paso 5 para invocarlo en las coordenadas que quieras.

Ejemplos basados en lo de arriba:

text

ENEMY FUEGO 150 200 1.5
COIN FIRE 300 400
¡Y listo! Ejecuta tu juego. El LevelLoader leerá el txt, instanciará tu clase de dominio, la añadirá al Board con su Hitbox correcta, y el motor la pintará usando los colores de tu toVisualMap().


PASO 7: Las Pruebas (Unitarias y de Aceptación)
En la sustentación o el parcial te pedirán demostrar que lo que hiciste funciona sin tener que abrir el juego manualmente. Para esto necesitas usar la carpeta src/test/.

1. Pruebas Unitarias (JUnit)
Las pruebas unitarias sirven para probar solo la clase que creaste, aislada del resto del juego. Debes crear o abrir un archivo de test (ej. GameplayLogicTest.java) y añadir un @Test:

Para probar una nueva Moneda (ej. BombCoin): Crea un "Mock" (objeto falso o simulado) de la clase Player. Instancia tu BombCoin. Llama manualmente al método coin.onCollect(playerMock) y usa assertEquals para verificar que el jugador efectivamente recibió el daño o el efecto esperado.
Para probar un nuevo Jugador (ej. HeavySkin): Instancia un Player asignándole tu nueva skin. Pásale un ataque simulado llamando a player.receiveHit(). Verifica con un assertEquals que el resultado sea SURVIVED_SHIELD si debía absorberlo, o DEAD si debía morir.
2. Pruebas de Aceptación (Integración)
Estas pruebas no prueban clases sueltas, sino flujos completos simulando lo que haría el usuario. Para hacer esto:

Crea un archivo .txt en la carpeta levels/ que contenga únicamente a tu nuevo personaje y a tu nuevo enemigo o moneda. (Un nivel de prueba).
En el test, invoca al LevelLoader.loadLevel("nivel_de_prueba", ...) y arranca el DOPOGame.
Usa game.movePlayer(...) para forzar programáticamente que el cuadrado toque tu nueva moneda o enemigo.
Llama a game.tick() un par de veces para que el motor de físicas corra.
Usa un assertTrue(game.isGameOver()) o revisa el contador de vidas para garantizar que toda la cadena (desde la carga, pasando por el dibujo, la física, hasta la regla de victoria/derrota) funcionó en conjunto.

**/
