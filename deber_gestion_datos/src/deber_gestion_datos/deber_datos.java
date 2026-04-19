package deber_gestion_datos;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JProgressBar;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class deber_datos extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private JTable tabla;
	private DefaultTableModel modelo;
	private JTextField txtBuscar;
	private TableRowSorter<DefaultTableModel> sorter;
	private JButton btnExportar;
	private JProgressBar barra;



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					deber_datos frame = new deber_datos();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public deber_datos() {
		setTitle("Gestión de datos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(20, 20, 640, 400);
		contentPane.add(tabbedPane);
		
		JPanel panelContactos = new JPanel();
		panelContactos.setLayout(null);
		
		JLabel lblContactos = new JLabel("Información de los contactos");
		lblContactos.setBounds(20, 10, 250, 25);
		panelContactos.add(lblContactos);
		
		JLabel lblBuscar = new JLabel("Buscar");
		lblBuscar.setBounds(20, 40, 80, 25);
		panelContactos.add(lblBuscar);
		
		txtBuscar = new JTextField();
		txtBuscar.setBounds(80, 40, 200, 25);
		panelContactos.add(txtBuscar);

		btnExportar = new JButton("Exportar CVS");
		btnExportar.setBounds(300, 40, 130, 25);
		panelContactos.add(btnExportar);

		// CREAR MODELO DE TABLA
		modelo = new DefaultTableModel();
		modelo.addColumn("ID");
		modelo.addColumn("Nombre");
		modelo.addColumn("Teléfono");
		modelo.addColumn("Correo");

		// CREAR TABLA
		tabla = new JTable(modelo);
		sorter = new TableRowSorter<>(modelo);
		tabla.setRowSorter(sorter);
		tabla = new JTable(modelo);
		sorter = new TableRowSorter<>(modelo);
		tabla.setRowSorter(sorter);
		
		// SCROLL
		JScrollPane scroll = new JScrollPane(tabla);
		scroll.setBounds(20, 80, 580, 220);
		panelContactos.add(scroll);
		
		// MENU
		JPopupMenu menu = new JPopupMenu();
		JMenuItem itemEliminar = new JMenuItem("Eliminar contacto");
		menu.add(itemEliminar);
		
		
		// ACCIÓN ELIMINAR
		
		tabla.setComponentPopupMenu(menu);
		itemEliminar.addActionListener(ev -> {
			int filaVista = tabla.getSelectedRow();

			if (filaVista >= 0) {
				int filaModelo = tabla.convertRowIndexToModel(filaVista);
				modelo.removeRow(filaModelo);
				JOptionPane.showMessageDialog(null, "CONTACTO ELIMINADO");
			} else {
				JOptionPane.showMessageDialog(null, "SELECCIONE UN CONTACTO");
			}
		});
		
	
		// BARRA DE PROGRESO
		
		barra = new JProgressBar();
		barra.setBounds(20, 310, 580, 25);
		barra.setStringPainted(true);
		panelContactos.add(barra);
		
		// DATOS
		
		modelo.addRow(new Object[] {1, "Elly", "0982171575", "elly@gmail.com"});
		modelo.addRow(new Object[] {2, "Ana", "0994200199", "ana1982@gmail.com"});
		modelo.addRow(new Object[] {3, "Jaime", "0981837484", "jaime49@hotmail.com"});
		
		
		//SELECCIONAR FILA Y MOSTRAR MENU
		
		tabla.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int fila = tabla.rowAtPoint(e.getPoint());
				if (fila >= 0) {
					tabla.setRowSelectionInterval(fila, fila);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				int fila = tabla.rowAtPoint(e.getPoint());
				if (fila >= 0) {
					tabla.setRowSelectionInterval(fila, fila);
				}
			}
		});
		
		

		new Thread(() -> {
			for (int i = 0; i <= 100; i++) {
				try {
					Thread.sleep(30);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				barra.setValue(i);
			}
		}).start();
		
		
		txtBuscar.addKeyListener( new KeyAdapter() {
			
		@Override
		public void keyReleased(KeyEvent e) {
			String texto = txtBuscar.getText();
			
			if (texto.trim().isEmpty()) {
				sorter.setRowFilter(null);
			} else {
				sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
			}
		}
	});
		
		btnExportar.addActionListener(evt -> {
			try {
				FileWriter writer = new FileWriter("contactos.csv");

				writer.append("ID,Nombre,Teléfono,Correo\n");

				for (int i = 0; i < modelo.getRowCount(); i++) {
					writer.append(modelo.getValueAt(i, 0).toString()).append(",");
					writer.append(modelo.getValueAt(i, 1).toString()).append(",");
					writer.append(modelo.getValueAt(i, 2).toString()).append(",");
					writer.append(modelo.getValueAt(i, 3).toString()).append("\n");
				}

				writer.flush();
				writer.close();

				JOptionPane.showMessageDialog(null, "Archivo CSV exportado correctamente");
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null, "Error al exportar el archivo");
			}
		});

		tabbedPane.addTab("Contactos", panelContactos);

		JPanel panelEstadisticas = new JPanel();
		panelEstadisticas.setLayout(null);

		JLabel lblEstadisticas = new JLabel("Estadísticas");
		lblEstadisticas.setBounds(20, 20, 250, 25);
		panelEstadisticas.add(lblEstadisticas);
		
		tabbedPane.addTab("Estadísticas", panelEstadisticas);
		    }
	 }


	