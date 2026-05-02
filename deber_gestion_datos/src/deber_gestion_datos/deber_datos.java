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
import javax.swing.JComboBox;
import java.util.ResourceBundle;
import java.util.Locale;


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
	private JLabel lblTotalContactos;
	
	private JComboBox<String> cmbIdioma;
	private ResourceBundle textos;
	private JLabel lblContactos;
	private JLabel lblBuscar;
	private JLabel lblCargando;
	private JButton btnSimularCarga;
	private JMenuItem itemEliminar;
	
	private JTextField txtNombre;
	private JTextField txtTelefono;
	private JTextField txtCorreo;
	private JButton btnGuardar;
	private JLabel lblNombre;
	private JLabel lblTelefono;
	private JLabel lblCorreo;
	

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
		setBounds(100, 100, 1200, 550);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new java.awt.BorderLayout());
		setContentPane(contentPane);
		
		tabbedPane = new JTabbedPane();
		contentPane.add(tabbedPane, java.awt.BorderLayout.CENTER);
		
		JPanel panelContactos = new JPanel();
		panelContactos.setLayout(new java.awt.BorderLayout());
		
		JPanel panelSuperior = new JPanel(new java.awt.FlowLayout());
		
		lblContactos = new JLabel("Información de los contactos");
		
		itemEliminar = new JMenuItem("Eliminar contacto");

		lblBuscar = new JLabel("Buscar");
		
		txtBuscar = new JTextField(10);
		
		lblNombre = new JLabel("Nombre");
		txtNombre = new JTextField(15);
		
		lblTelefono = new JLabel("Teléfono");
		txtTelefono = new JTextField(15);
		
		lblCorreo = new JLabel("Correo");
		txtCorreo = new JTextField(10);
		
		btnGuardar = new JButton("Guardar contacto");
		
		
		
		btnExportar = new JButton("Exportar CSV");
		
		cmbIdioma = new JComboBox<>();
		cmbIdioma.addItem("Español");
		cmbIdioma.addItem("English");
		cmbIdioma.addItem("Français");
		
		panelSuperior.add(lblContactos);
		
		panelSuperior.add(lblBuscar);
		panelSuperior.add(txtBuscar);

		panelSuperior.add(lblNombre);
		panelSuperior.add(txtNombre);

		panelSuperior.add(lblTelefono);
		panelSuperior.add(txtTelefono);

		panelSuperior.add(lblCorreo);
		panelSuperior.add(txtCorreo);

		panelSuperior.add(btnGuardar);
		panelSuperior.add(btnExportar);
		panelSuperior.add(cmbIdioma);
		
	    panelContactos.add(panelSuperior, java.awt.BorderLayout.NORTH);

	    cmbIdioma.addActionListener(e -> {
	        String idioma = (String) cmbIdioma.getSelectedItem();
	        cambiarIdioma(idioma);
	    });
		

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
		
		// SCROLL
		JScrollPane scroll = new JScrollPane(tabla);
		panelContactos.add(scroll, java.awt.BorderLayout.CENTER);
		
		// MENU
		JPopupMenu menu = new JPopupMenu();
		menu.add(itemEliminar);
		
		
		
		
		// ACCIÓN ELIMINAR
		
		tabla.setComponentPopupMenu(menu);
		itemEliminar.addActionListener(ev -> {
			int filaVista = tabla.getSelectedRow();

			if (filaVista >= 0) {
				int filaModelo = tabla.convertRowIndexToModel(filaVista);
				modelo.removeRow(filaModelo);
				actualizarEstadisticas();
				JOptionPane.showMessageDialog(null, textos.getString( "msg_eliminado"));
			} else {
				JOptionPane.showMessageDialog(null, textos.getString("msg_seleccione"));
			}
		});
		
	
		// BARRA DE PROGRESO
		
		barra = new JProgressBar();
		barra.setStringPainted(true);
		panelContactos.add(barra, java.awt.BorderLayout.SOUTH);
		
		// DATOS
		
		modelo.addRow(new Object[] {1, "Elly", "0982171575", "elly@gmail.com"});
		modelo.addRow(new Object[] {2, "Ana", "0994200199", "ana1982@gmail.com"});
		modelo.addRow(new Object[] {3, "Jaime", "0981837484", "jaime49@hotmail.com"});
		
		btnGuardar.addActionListener(e -> {
			String nombre = txtNombre.getText().trim();
			String telefono = txtTelefono.getText().trim();
			String correo = txtCorreo.getText().trim();
			if (nombre.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Debe llenar todos los campos");
				return;
			}
			int id = modelo.getRowCount() + 1;

			modelo.addRow(new Object[] {id, nombre, telefono, correo});

			txtNombre.setText("");
			txtTelefono.setText("");
			txtCorreo.setText("");

			actualizarEstadisticas();
		
	      JOptionPane.showMessageDialog(null, "Contacto registrado correctamente");
		});
		
		
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

		lblTotalContactos = new JLabel("Total de contactos: 0");
		lblTotalContactos.setBounds(30, 50, 250, 25);
		panelEstadisticas.add(lblTotalContactos);

		lblCargando = new JLabel("Cargando datos...");
		lblCargando.setBounds(30, 140, 250, 25);
		panelEstadisticas.add(lblCargando);

		btnSimularCarga = new JButton("Simular carga");
		btnSimularCarga.setBounds(250, 300, 150, 30);
		panelEstadisticas.add(btnSimularCarga);

		JProgressBar barraEstadisticas = new JProgressBar();
		barraEstadisticas.setBounds(30, 220, 550, 40);
		barraEstadisticas.setStringPainted(true);
		barraEstadisticas.setValue(100);
		barraEstadisticas.setString("¡Listo!");
		panelEstadisticas.add(barraEstadisticas);

		btnSimularCarga.addActionListener(e -> {
		    barraEstadisticas.setValue(0);
		    barraEstadisticas.setString("Cargando...");
		    
		    for (int i = 0; i <= 100; i++) {
		        barraEstadisticas.setValue(i);
		    }
		    
		    barraEstadisticas.setString("¡Listo!");
		});

		tabbedPane.addTab("Estadísticas", panelEstadisticas);

		cambiarIdioma("Español");
		actualizarEstadisticas();
	}

	private void actualizarEstadisticas() {
	    int total = modelo.getRowCount();

	    if (lblTotalContactos != null) {
	        lblTotalContactos.setText("Total de contactos: " + total);
	    }
	}
	
	private void cambiarIdioma(String idioma) {

	    if (idioma.equals("Español")) {
	        textos = ResourceBundle.getBundle("messages", Locale.forLanguageTag("es"));
	    } else if (idioma.equals("English")) {
	        textos = ResourceBundle.getBundle("messages", Locale.forLanguageTag("en"));
	    } else {
	        textos = ResourceBundle.getBundle("messages", Locale.forLanguageTag("fr"));
	    }

	    setTitle(textos.getString("titulo"));
	    lblContactos.setText("👤 " + textos.getString("contactos"));
	    lblBuscar.setText("🔍 " + textos.getString("buscar"));
	    btnExportar.setText("📄 " + textos.getString("exportar"));
	    itemEliminar.setText("🗑️ " + textos.getString("eliminar"));
	    tabbedPane.setTitleAt(0, textos.getString("tab_contactos"));
	    tabbedPane.setTitleAt(1, textos.getString("tab_estadisticas"));
	    

	    actualizarEstadisticas();

	}
}