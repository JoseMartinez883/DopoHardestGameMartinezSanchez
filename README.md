<div align="center">
  <h1>🟥 Dopo's Hardest Game V2 🟦</h1>
  <p><b>Un clon avanzado del clásico juego, construido con Arquitectura Limpia y Patrones de Diseño.</b></p>
  
  [![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
  [![OOP](https://img.shields.io/badge/OOP-Principles-blue?style=for-the-badge)]()
  [![Coverage](https://img.shields.io/badge/Coverage-93%25-brightgreen?style=for-the-badge)]()
  [![Code Quality](https://img.shields.io/badge/PMD-Passed-success?style=for-the-badge)]()
</div>

<br>

## 🎮 Autores
- Jose Alejandro Martinez Arias ( @JoseMartinez883 )
- Yazid Sanchez
  
## 🎮 Sobre el Proyecto
**Dopo's Hardest Game V2** es el Proyecto Final para la asignatura de **Programación Orientada a Objetos**. Más allá de ser un simple juego, es una demostración empírica de cómo aplicar ingeniería de software para resolver lógicas complejas (físicas, colisiones, persistencia y renderizado) manteniendo un código altamente mantenible y escalable.

El juego desafía al jugador a cruzar zonas llenas de enemigos patrullando y obstáculos geométricos, con mecánicas añadidas como inventarios, power-ups y modalidades competitivas.

---

## ✨ Características Principales
- **Motor de Físicas Propio:** Cálculos matemáticos precisos para `CircularMovement` y colisiones utilizando polígonos e hitboxes.
- **Sistema de Niveles Personalizado (`LevelLoader`):** Un analizador léxico que renderiza niveles completos desde archivos `.txt`. ¡Crea tus propios mapas escribiendo coordenadas!
- **Modo PvP Simétrico:** Soporte multijugador local donde dos jugadores compiten en tiempo real.
- **Doble Despacho y Polimorfismo:** Sistema avanzado de interacciones donde los escudos y las colisiones letales se calculan dinámicamente según el "Skin" del jugador.
- **Persistencia de Datos (Deep Serialization):** Sistema de guardado que congela y restaura el 100% de los objetos en memoria mediante `java.nio`.

---

## 📸 Funcionamiento del juego (Prueba de Aceptacion)
<img width="975" height="693" alt="image" src="https://github.com/user-attachments/assets/8162cbbd-7767-4243-97b4-f18010b4d5e3" />
<img width="966" height="694" alt="image" src="https://github.com/user-attachments/assets/ac082f9d-137f-445f-8147-ca4fb4c9c692" />
<img width="983" height="710" alt="image" src="https://github.com/user-attachments/assets/88175108-790b-436c-95d1-513058a690f0" />
<img width="978" height="685" alt="image" src="https://github.com/user-attachments/assets/821fd942-cbe2-4bd1-8420-b2873e3b3891" />
<img width="966" height="685" alt="image" src="https://github.com/user-attachments/assets/e9cc56fe-302f-43ea-ae20-11036f1b9e73" />
<img width="850" height="653" alt="image" src="https://github.com/user-attachments/assets/ca4ed42e-b4ef-4b0c-aedb-903beeecefa6" />



---

## 🏛️ Arquitectura y Patrones de Diseño
El código fuente fue estructurado evitando el código espagueti y garantizando un alto nivel de cohesión:

*   **Modelo-Vista-Controlador (MVC):** Separación total entre la lógica del `domain` y las interfaces de Swing en `presentation`.
*   **State Pattern:** Máquina de estados para manejar transiciones fluidas entre `PlayingState`, `GameOverState`, `PausedState`, etc.
*   **Strategy Pattern:** Implementado en las mecánicas de movimiento (`LinearMovement`, `CircularMovement`), permitiendo inyectar comportamientos al vuelo.
*   **Double Dispatch:** Interfaz `Interactable` usada para procesar colisiones en tiempo de ejecución sin depender del terrible uso de `instanceof`.

---

## 🧪 Pruebas y Calidad de Código (QA)
La fiabilidad del proyecto se demuestra con métricas estandarizadas de la industria:
- **Test Coverage (EclEmma/Jacoco):** `93.1%` de cobertura en el paquete `domain`. Las pruebas unitarias fueron divididas por comportamiento (`GameplayLogicTest`, `BoardElementTest`, `PersistenceLogTest`).
- **Análisis Estático:** `0` advertencias de alta prioridad. Código verificado y refactorizado bajo las estrictas normas de **PMD** (Cumplimiento de convenciones, herencia cerrada con clases finales e inmutabilidad).

---

## 🚀 Cómo Ejecutar el Proyecto
1. Clona este repositorio:
   ```bash
   git clone https://github.com/TU_USUARIO/TU_REPOSITORIO.git
   ```
2. Abre el proyecto en tu IDE favorito (Eclipse / IntelliJ).
3. Asegúrate de tener instalado **Java JDK 11 o superior**.
4. Ejecuta la clase principal ubicada en `src/presentation/V2GUI.java`.
5. ¡Juega y sufre intentando pasar los niveles!

---
<div align="center">
  <i>Desarrollado con mucha lógica y café ☕</i>
</div>
