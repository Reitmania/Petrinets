package mypackage.view;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Container;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

import javax.swing.text.DefaultCaret;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;

import mypackage.controller.ClickListener;
import mypackage.controller.Controller;

/** Die Klasse Myframe erweitert das JFrame und ist das Hauptfenster der GUI
*/
public class MyFrame extends JFrame {
	/** default serial version ID */
	private static final long serialVersionUID = 1L;
	
	private String example_folder = "../ProPra-WS20-Basis/Beispiele";

	/** Controller für die Steuerung */
	private Controller controller;

	/** In diesem Panel wird der Graph mittels GraphStream angezeigt. */
	private ViewPanel viewPanel;
	private ViewPanel viewPanelErr;
	
	/** In dieses JPanel wird das viewPanel eingebettet, da es ohne diese
	 * Einbettung zu unerwarteten Effekten kommen kann. (Beispielsweise wenn
	 * man beispielsweise mehrere viewPanels in eine JSplitPane packen
	 * möchte.)
	 */
	
	private JSplitPane splithor;
	private JSplitPane splitver;
	
	private JScrollPane scrollPane;
	
	private JPanel jpnlGraph;
	private JPanel jpnlGraphErr;
	
	private JTextArea textArea;

	/** (Statuszeile) */
	private JLabel statusLabel;
	
    /** Konstruktor
     * @param titel Names des Fensters
    */
	public MyFrame(String titel) {
		super(titel);
		// Prüfen, ob das Beispiel-Verzeichnis gefunden wird
		File bsp = new File(example_folder);
		System.out.println("Verzeichnis mit Beispielen: " + bsp);
		System.out.println("Verzeichnis existiert?: " + bsp.exists());
		
		// Renderer mit Unterstützung für Multigraphen und aller CSS Attribute
		// verwenden
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		// Textarea für Bottom Component mit AutoScrolling
		textArea = new JTextArea(5, 20);
	    DefaultCaret caret = (DefaultCaret)textArea.getCaret();
	    caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
		textArea.append("Stefan Reitmann, q4223870\n");		
		scrollPane = new JScrollPane(textArea); 
		textArea.setEditable(false);
		
		// Resize Event abfangen und Ratio des Splittings beibehalten
		this.addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent componentEvent) {
		    	restoreDefaults();
		    }
		});
		
		// Erzeuge Controller
		controller = new Controller(this);
		
		// Layout des JFrames setzen
		//this.setLayout(new BorderLayout());		
		this.getContentPane().setLayout(new BorderLayout());
		
		// PANEL ------------------------------------		
	
        // Erzeugung eines JSplitPane-Objektes mit horizontaler Trennung
        splitver = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splithor = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		// Einbetten des ViewPanels ins JPanel
		jpnlGraph = new JPanel(new BorderLayout());
		//jpnlGraph.add(BorderLayout.CENTER, viewPanel);
		// Füge das JPanel zum Haupt-Frame hinzu
		//this.add(jpnlGraph, BorderLayout.CENTER);
		
		// Erreichbarkeitsgraph
		jpnlGraphErr = new JPanel(new BorderLayout());
		//jpnlGraphErr.add(BorderLayout.CENTER, viewPanelErr);
		// Füge das JPanel zum Haupt-Frame hinzu
		//this.add(jpnlGraphErr, BorderLayout.WEST);
		
		// Füge Panels zu splitpane hinzu
        splithor.setLeftComponent(jpnlGraph);
        splithor.setRightComponent(jpnlGraphErr);        
        splitver.setTopComponent(splithor);
        splitver.setBottomComponent(scrollPane);
        
        // füge zu Hauptframe hinzu
        this.add(splitver, BorderLayout.CENTER);
		
		// WEITERE GUI ELEMENTE ----------------------------------
		// Erzeuge ein Labels, welches als Statuszeile dient, ...
		statusLabel = new JLabel("Hier können Statusmeldungen angezeigt werden");
		// ... und füge es zum Haupt-Frame hinzu
		this.add(statusLabel, BorderLayout.SOUTH);		
		// Erstelle Menu
		initMenu();		
		// Erstelle Toolbar
		initToolbar();
		
		// Konfiguriere weitere Parameter des Haupt-Frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(200, 200, 1200, 900);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		restoreDefaults();		
	}
	
    /** dient zur Einhaltung der Seitenverhältnisse auch bei Resize
    */
    private void restoreDefaults() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	splithor.setDividerLocation(splithor.getSize().width /2);
            	splitver.setDividerLocation(0.75);
            }
        });
    }
	
	private void initMenu() { 
		// Menu Bar		
		JMenuBar menubar = new JMenuBar();
		JMenu menu = new JMenu("Datei");
		JMenuItem mopen = new JMenuItem("Öffnen");
		mopen.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ev) {
		    	  JFileChooser chooser = new JFileChooser(example_folder);
		    	  FileNameExtensionFilter pnmlfilter = new FileNameExtensionFilter("pnml files (*.pnml)", "pnml");
		    	  chooser.setFileFilter(pnmlfilter);
		          int rueckgabeWert = chooser.showOpenDialog(null);	
		          if(rueckgabeWert == JFileChooser.APPROVE_OPTION)
		          {
		        	  controller.setFilename(chooser.getSelectedFile().getName(), example_folder);
		        	  textareaMessage("Die zu öffnende Datei ist: " + chooser.getSelectedFile().getName());
		              
		              // ab hier Erstellung und Hinzufügen der Graphen
		              initPanelGraph();
		              controller.buildGraph(true);
		              jpnlGraph.add(BorderLayout.CENTER, viewPanel);		              
		              initPanelGraphErr();
		              jpnlGraphErr.add(BorderLayout.CENTER, viewPanelErr);
		              restoreDefaults();
		          }
		      }
	    });
		menu.add(mopen);
		JMenuItem mreload = new JMenuItem("Neu laden");
		mreload.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ev) {
		    	  controller.reload();
		      }
	    });
		menu.add(mreload);
		JMenuItem manalyze = new JMenuItem("Analyse mehrerer Dateien");
		manalyze.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ev) {
		    	  JFileChooser chooser = new JFileChooser(example_folder);
		    	  chooser.setMultiSelectionEnabled(true);
		    	  FileNameExtensionFilter pnmlfilter = new FileNameExtensionFilter("pnml files (*.pnml)", "pnml");
		    	  chooser.setFileFilter(pnmlfilter);
		          int rueckgabeWert = chooser.showOpenDialog(null);	
		          if(rueckgabeWert == JFileChooser.APPROVE_OPTION)
		          {
		        	  File[] files = chooser.getSelectedFiles();		        	  
		        	  for(File file : files) {
		        		  controller.setFilename(file.getName(), example_folder);
		        		  textareaMessage("Die zu öffnende Datei ist: " + file.getName());
		        		  controller.buildGraph(false);
		        	  }
		        	  /*
		              // ab hier Erstellung und Hinzufügen der Graphen
		              initPanelGraph();
		              controller.buildGraph();
		              jpnlGraph.add(BorderLayout.CENTER, viewPanel);		              
		              initPanelGraphErr();
		              jpnlGraphErr.add(BorderLayout.CENTER, viewPanelErr);
		              restoreDefaults();
		              */
		          }
		      }
	    });
		menu.add(manalyze);
		JMenuItem mclose = new JMenuItem("Beenden");
	 	mclose.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ev) {
		    	  System.exit(0);
		      }
	    });
		menu.add(mclose);
		menubar.add(menu);
		JMenu menu2 = new JMenu("Info");
		menubar.add(menu2);
		this.setJMenuBar(menubar);
	}
	
	private void initToolbar() { 
        //Toolbar wird erstellt
        JToolBar tbar = new JToolBar();
        //Größe der Toolbar wird gesetzt
        tbar.setSize(230, 20); 
        // Schaltflächen werden erzeugt und der JToolBar hinzugefügt
        JButton bAddToken = new JButton("+ Marken");
        bAddToken.setToolTipText("Füge Marken / Tokens hinzu");
        bAddToken.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ev) {
		    	  controller.changeMarker(1);
		      }
	    });
        tbar.add(bAddToken);
        JButton bDeleteToken = new JButton("- Marken");
        bDeleteToken.setToolTipText("Entferne Marken / Tokens");
        bDeleteToken.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ev) {
		    	  controller.changeMarker(-1);
		      }
	    });
        tbar.add(bDeleteToken);
        JButton bDeleteEG = new JButton("Lösche EG");
        bDeleteEG.setToolTipText("Erklärung Lösche EG");
        bDeleteEG.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ev) {
		    	  controller.deletePEG();
		      }
	    });
        tbar.add(bDeleteEG);
        JButton bReset = new JButton("Reset");
        bReset.setToolTipText("Reset auf letzte aktive Anfangsmarkierung");
        bReset.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ev) {
		    	  controller.resetDefaults();
		      }
	    });
        tbar.add(bReset);	
        JButton bClearArea = new JButton("Lösche Textarea");
        bClearArea.setToolTipText("Erklärung Lösche Textarea");
        bClearArea.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ev) {
		    	  textArea.setText(null);
		      }
	    });
        tbar.add(bClearArea);
        Container contentPane = this.getContentPane();
        contentPane.add(tbar, BorderLayout.NORTH);
	}	
	
	private void initPanelGraphErr() {  
		Viewer viewer_err = new Viewer(controller.getGraphPEG(), Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer_err.enableAutoLayout();
		viewPanelErr = viewer_err.addDefaultView(false);
	} 
	
	private void initPanelGraph() {
		Viewer viewer = new Viewer(controller.getGraph(),
		Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		
		viewer.disableAutoLayout();
		
		viewPanel = viewer.addDefaultView(false);
		ViewerPipe viewerPipe = viewer.newViewerPipe();		
		ClickListener clickListener = new ClickListener(controller);		
		viewerPipe.addViewerListener(clickListener);
		viewPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				System.out.println("MyFrame - mousePressed: " + me);
				viewerPipe.pump();
			}
			@Override
			public void mouseReleased(MouseEvent me) {
				System.out.println("MyFrame - mouseReleased: " + me);
				viewerPipe.pump();
			}
		});
	}	
	
    /** Text für Statuslabel wird angepasst
     * @param text Text für Statuslabel
    */
	public void outputText(String text) {
		statusLabel.setText(text);
	}
    /** Text für Textarea wird angepasst
     * @param text Text für Textarea
    */
	public void textareaMessage(String text) {
		textArea.append(text + "\n");
	}

}
