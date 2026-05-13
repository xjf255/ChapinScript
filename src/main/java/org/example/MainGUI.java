package org.example;

import org.example.lexer.Lexer;
import org.example.lexer.LexicalError;
import org.example.lexer.Token;
import org.example.lexer.TokenType;
import org.example.parser.LexerAdapter;
import org.example.parser.Parser;
import org.example.reports.HtmlReportsGenerator;
import org.example.reports.SymbolTableGenerator;
import org.example.utils.FileManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class MainGUI extends JFrame {
    private static final Color BG         = new Color(0x0D0D0D);
    private static final Color BG_EDITOR  = new Color(0x111111);
    private static final Color BG_PANEL   = new Color(0x1A1A1A);
    private static final Color BG_HEADER  = new Color(0x141414);
    private static final Color DIVIDER    = new Color(0x2A2A2A);
    private static final Color ACCENT     = new Color(0x22C55E);
    private static final Color ACCENT_DIM = new Color(0x16A34A);
    private static final Color FG_TEXT    = new Color(0xE2E8F0);
    private static final Color FG_DIM     = new Color(0x64748B);
    private static final Color FG_ERROR   = new Color(0xF87171);
    private static final Color FG_SUCCESS = new Color(0x4ADE80);
    private static final Color FG_WARN    = new Color(0xFBBF24);
    private static final Color FG_CPP     = new Color(0xA78BFA);

    private static final Font FONT_CODE = resolveFont("JetBrains Mono","Fira Code","Consolas","Monospaced");
    private static final Font FONT_UI   = resolveFont("SF Pro Text","Segoe UI","Helvetica Neue","SansSerif");

    private static Font resolveFont(String... names) {
        java.util.Set<String> avail = new java.util.HashSet<>(
                java.util.Arrays.asList(
                        GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
        for (String n : names) if (avail.contains(n)) return new Font(n, Font.PLAIN, 14);
        return new Font(Font.MONOSPACED, Font.PLAIN, 14);
    }

    private static final String OUTPUT_DIR = "output";
    private File   currentFile = null;

    private JTextArea editorArea;
    private JTextArea cppArea;
    private JTextArea consoleArea;
    private JLabel    cppLabel;
    private JLabel    statusLabel;
    private JLabel    fileLabel;
    private JButton   runBtn;

    public MainGUI() {
        super("ChapinScript");
        buildUI();
        redirectStreams();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildUI() {
        setBackground(BG);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());
        add(buildTopBar(),    BorderLayout.NORTH);
        add(buildMainSplit(), BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(BG_HEADER);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0,DIVIDER),
                new EmptyBorder(6,14,6,14)));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);
        JLabel logo = new JLabel("ChapinScript");
        logo.setFont(FONT_UI.deriveFont(Font.BOLD, 15f));
        logo.setForeground(FG_TEXT);
        JButton openBtn = ghostBtn("  Cargar archivo");
        openBtn.setForeground(Color.BLACK);
        openBtn.addActionListener(e -> openFile());

        JButton shwnBtn = ghostBtn(" Mostrar Tablas");
        shwnBtn.setForeground(Color.BLACK);
        shwnBtn.addActionListener(e -> openTable());

        left.add(logo);
        left.add(vSep());
        left.add(openBtn);
        left.add(shwnBtn);

        runBtn = new JButton("  Ejecutar");
        runBtn.setFont(FONT_UI.deriveFont(Font.BOLD, 13f));
        runBtn.setBackground(ACCENT);
        runBtn.setForeground(Color.WHITE);
        runBtn.setFocusPainted(false);
        runBtn.setBorderPainted(false);
        runBtn.setBorder(new EmptyBorder(7, 20, 7, 20));
        runBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        runBtn.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e){ runBtn.setBackground(ACCENT_DIM); }
            public void mouseExited (MouseEvent e){ runBtn.setBackground(ACCENT); }
        });
        runBtn.addActionListener(e -> compile());

        bar.add(left,   BorderLayout.WEST);
        bar.add(runBtn, BorderLayout.EAST);
        return bar;
    }

    private JSplitPane buildMainSplit() {
        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildEditorPanel(), buildRightPanel());
        sp.setDividerLocation(820);
        sp.setDividerSize(1);
        sp.setBorder(null);
        styleSplitter(sp, DIVIDER);
        return sp;
    }

    private JPanel buildEditorPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_EDITOR);

        editorArea = new JTextArea("// Escribe tu código aqui...");
        editorArea.setFont(FONT_CODE.deriveFont(14f));
        editorArea.setBackground(BG_EDITOR);
        editorArea.setForeground(FG_TEXT);
        editorArea.setCaretColor(ACCENT);
        editorArea.setSelectionColor(new Color(0x22,0xC5,0x5E,50));
        editorArea.setTabSize(4);
        editorArea.setLineWrap(false);
        editorArea.setBorder(new EmptyBorder(12,0,12,12));
        editorArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "run");
        editorArea.getActionMap().put("run", new AbstractAction(){ public void actionPerformed(ActionEvent e){ compile(); }});

        JScrollPane scroll = scroll(editorArea);
        LineNumbers ln = new LineNumbers(editorArea);
        scroll.setRowHeaderView(ln);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildRightPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_PANEL);
        p.setBorder(BorderFactory.createMatteBorder(0,1,0,0,DIVIDER));

        JSplitPane vs = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                buildCppSection(), buildConsoleSection());
        vs.setDividerLocation(480);
        vs.setDividerSize(1);
        vs.setBorder(null);
        styleSplitter(vs, DIVIDER);
        p.add(vs, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildCppSection() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_PANEL);

        JPanel hdr = sectionHeader();
        cppLabel = new JLabel("C++ Generado (0 líneas)");
        cppLabel.setFont(FONT_UI.deriveFont(13f));
        cppLabel.setForeground(FG_TEXT);
        hdr.add(cppLabel, BorderLayout.WEST);
        p.add(hdr, BorderLayout.NORTH);

        cppArea = new JTextArea("Los archivos C++ generados aparecerán aquí");
        cppArea.setEditable(false);
        cppArea.setFont(FONT_CODE.deriveFont(13f));
        cppArea.setBackground(BG_PANEL);
        cppArea.setForeground(FG_DIM);
        cppArea.setLineWrap(false);
        cppArea.setBorder(new EmptyBorder(10,14,10,14));
        p.add(scroll(cppArea), BorderLayout.CENTER);
        return p;
    }

    private JPanel buildConsoleSection() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_PANEL);
        p.setBorder(BorderFactory.createMatteBorder(1,0,0,0,DIVIDER));

        JPanel hdr = sectionHeader();
        JLabel title = new JLabel("⌥  Consola");
        title.setFont(FONT_UI.deriveFont(Font.BOLD, 13f));
        title.setForeground(FG_TEXT);
        JButton clrBtn = ghostBtn("Limpiar");
        clrBtn.setForeground(Color.BLACK);
        clrBtn.addActionListener(e -> consoleArea.setText(""));
        hdr.add(title,  BorderLayout.WEST);
        hdr.add(clrBtn, BorderLayout.EAST);
        p.add(hdr, BorderLayout.NORTH);

        consoleArea = new JTextArea("Consola vacía");
        consoleArea.setEditable(false);
        consoleArea.setFont(FONT_CODE.deriveFont(12.5f));
        consoleArea.setBackground(BG_PANEL);
        consoleArea.setForeground(FG_DIM);
        consoleArea.setLineWrap(true);
        consoleArea.setWrapStyleWord(true);
        consoleArea.setBorder(new EmptyBorder(8,14,8,14));
        p.add(scroll(consoleArea), BorderLayout.CENTER);
        return p;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(BG_HEADER);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1,0,0,0,DIVIDER),
                new EmptyBorder(3,14,3,14)));

        fileLabel = new JLabel("Sin archivo abierto");
        fileLabel.setFont(FONT_UI.deriveFont(11.5f));
        fileLabel.setForeground(FG_DIM);

        statusLabel = new JLabel("Listo");
        statusLabel.setFont(FONT_UI.deriveFont(11.5f));
        statusLabel.setForeground(FG_SUCCESS);

        bar.add(fileLabel,   BorderLayout.WEST);
        bar.add(statusLabel, BorderLayout.EAST);
        return bar;
    }

    private void compile() {
        setStatus("Compilando...", FG_WARN);
        runBtn.setEnabled(false);
        new SwingWorker<Void,Void>(){
            protected Void doInBackground(){ runCompiler(); return null; }
            protected void done(){ runBtn.setEnabled(true); }
        }.execute();
    }

    private void runCompiler() {
        String source = editorArea.getText();
        if (source.isBlank()) {
            log("⚠  El editor está vacío.\n", FG_WARN);
            setStatus("Editor vacío", FG_WARN);
            return;
        }
        SwingUtilities.invokeLater(()->{
            consoleArea.setForeground(FG_TEXT);
            consoleArea.setText("");
            cppArea.setForeground(FG_DIM);
            cppArea.setText("Los archivos C++ generados aparecerán aquí");
            cppLabel.setText("C++ Generado (0 líneas)");
        });

        log("▶  Iniciando compilación...\n", FG_DIM);
        try {
            Lexer lexer1 = new Lexer(new StringReader(source));
            List<Token> tokens = new ArrayList<>();
            Token t;
            while ((t = lexer1.yylex()) != null && t.getType() != TokenType.EOF) tokens.add(t);
            boolean hasLex = !lexer1.getErrors().isEmpty();
            log("   Tokens      : " + tokens.size() + "\n", FG_TEXT);
            log("   Err. léxicos: " + lexer1.getErrors().size() + "\n", hasLex ? FG_ERROR : FG_SUCCESS);

            String cppCode = null; Parser parser = null;
            if (!hasLex) {
                LexerAdapter adp = new LexerAdapter(new Lexer(new StringReader(source)));
                parser = new Parser(adp);
                try { Object r = parser.parse().value; if (r instanceof String) cppCode = (String)r; }
                catch (Exception ex){ log("   Excepción parseo: " + ex.getMessage() + "\n", FG_ERROR); }
            } else { log("   Análisis sint. omitido (errores léxicos)\n", FG_WARN); }

            List<Parser.SyntaxError> synErr = parser != null ? parser.getSyntaxErrors() : new ArrayList<>();
            boolean hasSyn = !synErr.isEmpty();
            if (!hasLex) {
                if (!hasSyn) log("   ✓ Parseo exitoso\n", FG_SUCCESS);
                else {
                    log("   ✗ " + synErr.size() + " error(es) sintácticos\n", FG_ERROR);
                    for (Parser.SyntaxError se : synErr) log("     → " + se + "\n", FG_ERROR);
                }
            }

            boolean ok = !hasLex && !hasSyn && cppCode != null;
            if (ok) {
                final String cpp = cppCode;
                final int lines = cpp.split("\n").length;
                SwingUtilities.invokeLater(()->{
                    cppArea.setForeground(FG_CPP);
                    cppArea.setText(cpp);
                    cppArea.setCaretPosition(0);
                    cppLabel.setText("C++ Generado (" + lines + " líneas)");
                });
                log("   ✓ C++ generado (" + lines + " líneas)\n", FG_SUCCESS);
            } else {
                SwingUtilities.invokeLater(()->{ cppArea.setText("// Sin código C++ (errores)"); cppLabel.setText("C++ Generado (0 líneas)"); });
                log("   ✗ No se generó código C++\n", FG_ERROR);
            }

            new File(OUTPUT_DIR).mkdirs();
            FileManager.writeFile(OUTPUT_DIR+"/output.cpp", ok ? cppCode : "// Sin código C++ válido.");
            HtmlReportsGenerator.generateErrorsReport(OUTPUT_DIR+"/errors_report.html", lexer1.getErrors(), synErr);
            HtmlReportsGenerator.generateTokensReport(OUTPUT_DIR+"/tokens_report.html", tokens);
            var sym = new SymbolTableGenerator().generate(tokens);
            HtmlReportsGenerator.generateSymbolTableReport(OUTPUT_DIR+"/symbol_table.html", sym);
            log("   Reportes en: " + OUTPUT_DIR + "/\n", FG_DIM);

            final int total = lexer1.getErrors().size() + synErr.size();
            SwingUtilities.invokeLater(()->
                    setStatus(ok ? "✓ Compilación exitosa" : "✗ "+total+" error(es)", ok ? FG_SUCCESS : FG_ERROR));

        } catch (IOException ex){ log("Error de archivo: "+ex.getMessage()+"\n", FG_ERROR); setStatus("Error de archivo", FG_ERROR);
        } catch (Exception   ex){ log("Error inesperado: "+ex.getMessage()+"\n", FG_ERROR); setStatus("Error inesperado", FG_ERROR); }
    }

    // ── Archivo ───────────────────────────────────────────────────────────────
    private void openFile() {
        JFileChooser fc = new JFileChooser(".");
        fc.setFileFilter(new FileNameExtensionFilter("ChapinScript (*.chs)","chs"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                editorArea.setText(FileManager.readFile(f.getAbsolutePath()));
                editorArea.setCaretPosition(0);
                currentFile = f;
                fileLabel.setText(f.getName());
                setStatus("Archivo abierto", FG_SUCCESS);
                setTitle("ChapinScript — " + f.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,"No se pudo abrir:\n"+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //SHWN BTN
    private void openTable() {
        try {
            File htmlSymbolsFile = new File(OUTPUT_DIR+"/symbol_table.html");
            File htmlErrorsFile = new File(OUTPUT_DIR+"/errors_report.html");
            File htmlTokensFile = new File(OUTPUT_DIR+"/tokens_report.html");
            Desktop.getDesktop().browse(htmlTokensFile.toURI());
            Desktop.getDesktop().browse(htmlErrorsFile.toURI());
            Desktop.getDesktop().browse(htmlSymbolsFile.toURI());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private void log(String msg, Color c) {
        SwingUtilities.invokeLater(()->{
            if (consoleArea.getText().equals("Consola vacía")) consoleArea.setText("");
            consoleArea.setForeground(FG_TEXT);
            consoleArea.append(msg);
            consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
        });
    }
    private void setStatus(String msg, Color c) {
        SwingUtilities.invokeLater(()->{ statusLabel.setText(msg); statusLabel.setForeground(c); });
    }
    private void redirectStreams() {
        System.setOut(new PrintStream(new OutputStream(){
            public void write(int b){log(String.valueOf((char)b),FG_TEXT);}
            public void write(byte[] b,int o,int l){log(new String(b,o,l),FG_TEXT);}},true));
        System.setErr(new PrintStream(new OutputStream(){
            public void write(int b){log(String.valueOf((char)b),FG_ERROR);}
            public void write(byte[] b,int o,int l){log(new String(b,o,l),FG_ERROR);}},true));
    }

    private JPanel sectionHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(BG_HEADER);
        h.setPreferredSize(new Dimension(0, 40));
        h.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0,DIVIDER), new EmptyBorder(0,14,0,14)));
        return h;
    }
    private JButton ghostBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(FONT_UI.deriveFont(12.5f));
        b.setBackground(new Color(0x262626));
        b.setForeground(FG_TEXT);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DIVIDER), new EmptyBorder(4,12,4,12)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e){b.setBackground(new Color(0x333333));}
            public void mouseExited (MouseEvent e){b.setBackground(new Color(0x262626));}
        });
        return b;
    }
    private Component vSep() {
        JSeparator s = new JSeparator(JSeparator.VERTICAL);
        s.setPreferredSize(new Dimension(1,20)); s.setForeground(DIVIDER); return s;
    }
    private JScrollPane scroll(Component c) {
        Color bg = (c instanceof JTextArea) ? ((JTextArea)c).getBackground() : BG_PANEL;
        JScrollPane s = new JScrollPane(c);
        s.setBorder(null);
        s.setBackground(bg); s.getViewport().setBackground(bg);
        return s;
    }
    private static void styleSplitter(JSplitPane sp, Color color) {
        sp.setUI(new javax.swing.plaf.basic.BasicSplitPaneUI(){
            public javax.swing.plaf.basic.BasicSplitPaneDivider createDefaultDivider(){
                return new javax.swing.plaf.basic.BasicSplitPaneDivider(this){
                    { setBackground(color); }
                    public void paint(Graphics g){ g.setColor(color); g.fillRect(0,0,getWidth(),getHeight()); }
                };
            }
        });
    }

    // ── Números de línea ──────────────────────────────────────────────────────
    private static class LineNumbers extends JPanel implements CaretListener, DocumentListener {
        private final JTextArea ta;
        LineNumbers(JTextArea ta) {
            this.ta = ta;
            setPreferredSize(new Dimension(46, Integer.MAX_VALUE));
            setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
            setBackground(new Color(0x111111));
            setForeground(new Color(0x3A3A3A));
            ta.addCaretListener(this); ta.getDocument().addDocumentListener(this);
        }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(getFont()); g2.setColor(getForeground());
            Rectangle clip = g.getClipBounds();
            int s0 = ta.viewToModel2D(new Point(0, clip.y));
            int s1 = ta.viewToModel2D(new Point(0, clip.y + clip.height));
            Element root = ta.getDocument().getDefaultRootElement();
            FontMetrics fm = g2.getFontMetrics();
            for (int ln = root.getElementIndex(s0); ln <= root.getElementIndex(s1); ln++) {
                try {
                    Rectangle r = ta.modelToView2D(root.getElement(ln).getStartOffset()).getBounds();
                    String num = String.valueOf(ln+1);
                    g2.drawString(num, getWidth()-fm.stringWidth(num)-6,
                            r.y + fm.getAscent() + (r.height-fm.getHeight())/2);
                } catch (BadLocationException ignored){}
            }
        }
        public void caretUpdate(CaretEvent e)    { repaint(); }
        public void insertUpdate(DocumentEvent e) { repaint(); }
        public void removeUpdate(DocumentEvent e) { repaint(); }
        public void changedUpdate(DocumentEvent e){ repaint(); }
    }

    // ── Entry point ───────────────────────────────────────────────────────────
    public static void main(String[] args) {
        UIManager.put("ScrollBar.thumb",   new Color(0x2A2A2A));
        UIManager.put("ScrollBar.track",   BG_PANEL);
        UIManager.put("OptionPane.background", BG_PANEL);
        UIManager.put("Panel.background",  BG_PANEL);
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored){}
        SwingUtilities.invokeLater(MainGUI::new);
    }
}