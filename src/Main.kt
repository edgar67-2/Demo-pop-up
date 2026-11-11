package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dibuja la app detrás de la barra de estado y la de navegación (pantalla completa)
        enableEdgeToEdge()

        // Aquí empieza la interfaz: la envolvemos en nuestro tema (colores y tipografía)
        setContent {
            MyApplicationTheme {
                PopupMenuScreen()
            }
        }
    }
}

// Lista de opciones que se muestran en el menú.
// Para agregar o quitar opciones, solo edita esta lista.
private val opciones = listOf("Opción 1", "Opción 2", "Opción 3")

/**
 * Pantalla principal. Contiene:
 *  1. Un título y una descripción.
 *  2. Un selector que abre un menú emergente al tocarlo.
 *  3. Una tarjeta que muestra la opción elegida.
 *  4. Un segundo menú que aparece donde hagas una pulsación larga.
 */
@Composable
fun PopupMenuScreen() {
    // Cuando una de estas variables cambia, Compose redibuja lo que dependa de ella.

    // Opción elegida actualmente (null = todavía no se eligió ninguna)
    var seleccion by remember { mutableStateOf<String?>(null) }

    // true = el menú del selector está abierto
    var menuAbierto by remember { mutableStateOf(false) }

    // Posición donde se hizo la pulsación larga (null = menú de pulsación larga cerrado)
    var posicionPulsacion by remember { mutableStateOf<DpOffset?>(null) }

    // Densidad de pantalla: sirve para convertir píxeles a dp
    val density = LocalDensity.current

    // Contenedor principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // color de fondo del tema
            // Detecta la pulsación larga en cualquier parte de la pantalla
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { offset ->
                        // 'offset' viene en píxeles; lo convertimos a dp para el menú
                        posicionPulsacion = with(density) {
                            DpOffset(offset.x.toDp(), offset.y.toDp())
                        }
                    }
                )
            }
    ) {
        // Columna con todo el contenido visible, uno debajo de otro
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding() // evita que el contenido quede tapado por las barras del sistema
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.Center // centrado verticalmente
        ) {
            // Título
            Text(
                text = "Elige una opción",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(8.dp))

            // Descripción
            Text(
                text = "Toca el selector, o mantén presionado en cualquier parte de la pantalla.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(32.dp))

            // ----- SELECTOR + MENÚ EMERGENTE -----
            // El Box agrupa el selector y su menú para que el menú se abra pegado al selector.
            Box {
                // El "campo" que se toca para abrir el menú
                Surface(
                    onClick = { menuAbierto = true }, // al tocar, abrimos el menú
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Textos a la izquierda (weight(1f) = ocupan todo el espacio sobrante)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Opción",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                // Si no hay selección, muestra "Seleccionar"
                                text = seleccion ?: "Seleccionar",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        // Flechita a la derecha que indica que es un desplegable
                        Text(
                            text = "▾",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // El menú emergente en sí (solo se ve cuando expanded = true)
                DropdownMenu(
                    expanded = menuAbierto,
                    onDismissRequest = { menuAbierto = false }, // se cierra al tocar fuera
                    shape = RoundedCornerShape(16.dp)
                ) {
                    OpcionesDelMenu(seleccion) { elegida ->
                        seleccion = elegida   // guardamos la opción elegida
                        menuAbierto = false   // y cerramos el menú
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Tarjeta de resultados
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Crossfade hace una transición suave cuando cambia la selección
                Crossfade(targetState = seleccion, label = "resultado") { actual ->
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = if (actual == null) "Aún no has elegido nada" else "Elegiste",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        // Solo mostramos el nombre de la opción si ya se eligió una
                        if (actual != null) {
                            Text(
                                text = actual,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }

        // Menú de la pulsación de larga
        // Es el equivalente Android del "clic derecho": aparece en el punto que tocaste.
        DropdownMenu(
            expanded = posicionPulsacion != null, // abierto solo si hay una posición guardada
            onDismissRequest = { posicionPulsacion = null },
            offset = posicionPulsacion ?: DpOffset.Zero, // lo desplazamos hasta el dedo
            shape = RoundedCornerShape(16.dp)
        ) {
            OpcionesDelMenu(seleccion) { elegida ->
                seleccion = elegida
                posicionPulsacion = null // cerramos el menú
            }
        }
    }
}

/**
 * Dibuja las opciones dentro de un menú.
 * Se reutiliza en los dos menús (selector y pulsación larga) para no repetir código.
 *
 * @param seleccionada la opción elegida ahora mismo (se resalta en el menú)
 * @param alElegir     función que se ejecuta al tocar una opción, recibe su texto
 */
@Composable
private fun OpcionesDelMenu(
    seleccionada: String?,
    alElegir: (String) -> Unit
) {
    // Por cada texto de la lista creamos un elemento del menú
    opciones.forEach { texto ->
        val activa = texto == seleccionada // ¿es la opción que ya está elegida?
        DropdownMenuItem(
            text = {
                Text(
                    text = texto,
                    // La opción activa va en negrita y con el color principal
                    fontWeight = if (activa) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (activa) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
            },
            onClick = { alElegir(texto) }
        )
    }
}

/**
 * Vista previa dentro de Android Studio (panel "Preview"),
 * sin necesidad de ejecutar la app en el emulador.
 */
@Preview(showBackground = true)
@Composable
private fun PopupMenuScreenPreview() {
    MyApplicationTheme(dynamicColor = false) {
        PopupMenuScreen()
    }
}