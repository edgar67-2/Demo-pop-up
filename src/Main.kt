
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.SwingUtilities
import javax.swing.WindowConstants
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

fun main() {

    // Ejecutar la interfaz grafica
    SwingUtilities.invokeLater {
        val frame = JFrame("Demo Popup Menu")
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(350, 200)
        frame.layout = FlowLayout(FlowLayout.CENTER, 20, 40)

        // Crear boton y etiqueta
        val button = JButton("Click para ver el menú")
        val label = JLabel("Aún no has elegido nada")

        // Crear el popup menu
        val popupMenu = JPopupMenu()
        val opciones = listOf("Opción 1", "Opción 2", "Opción 3")

        // Agregar las opciones al menu
        for (texto in opciones) {
            val item = JMenuItem(texto)
            item.addActionListener {
                label.text = "You Clicked $texto"
            }
            popupMenu.add(item)
        }

        // Mostrar el menu al presionar el boton
        button.addActionListener {
            popupMenu.show(button, 0, button.height)
        }

        // Agregar componentes a la ventana
        frame.add(button)
        frame.add(label)
        frame.setLocationRelativeTo(null) // centrar en pantalla

        // Detectar clic derecho para mostrar el menu
        frame.contentPane.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) { if (e.isPopupTrigger) popupMenu.show(e.component, e.x, e.y) }
            override fun mouseReleased(e: MouseEvent) { if (e.isPopupTrigger) popupMenu.show(e.component, e.x, e.y) }
        })

        // Mostrar ventana
        frame.isVisible = true
    }
}
