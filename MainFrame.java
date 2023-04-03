import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class MainFrame extends JFrame{
    private JFileChooser chooser = new JFileChooser(".");
    private JButton nextButton;
    private JButton previousButton;

    private JButton button1;
    private JPanel mainPanel;

    ArrayList<String> nazevHry = new ArrayList();
    ArrayList<String> vlastnimeHru = new ArrayList();
    ArrayList<Integer> oblibenostHry = new ArrayList();
    static String separator = "-";
    int aktualniDeskovka;
    public static MainFrame frame;

    public MainFrame(){
        initComponents();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        horizontalBox.add(previousButton);
        horizontalBox.add(button1);
        horizontalBox.add(nextButton);
        mainPanel.add(horizontalBox);

    }
    private void readFromFile() throws FileNotFoundException {
        int result = chooser.showOpenDialog(this);
        // Klikl uživatel na "Open"? Pokud ano, zpracuj událost
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();

            loadFile(selectedFile);
            zobrazAktualniDeskovku();
        }
    }
    String line;

    private void loadFile(File selectedFile) throws FileNotFoundException {
        try(Scanner scanner = new Scanner(new BufferedReader(new FileReader(selectedFile)))) {
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                String parametry[] = line.split(separator);
                nazevHry.add(parametry[0]);
                vlastnimeHru.add(parametry[1]);
                oblibenostHry.add(Integer.parseInt(parametry[2]));
            }
        }catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }

    }
    private void initComponents() {
        setTitle("Deskové hry");
        setContentPane(mainPanel);
        button1.addActionListener(e -> {
            try {
                readFromFile();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        nextButton.addActionListener(e -> {
            dalsiDeskovka(1);
        });

        previousButton.addActionListener(e -> {
            dalsiDeskovka(-1);
        });
    }



    private void dalsiDeskovka(int smer){
        int pocetDeskovek = nazevHry.size();
        if(pocetDeskovek == 0){
            aktualniDeskovka = 0;
            return;
        }

        aktualniDeskovka += smer;

        if(aktualniDeskovka >= pocetDeskovek){
            aktualniDeskovka = 0;
        }

        if(aktualniDeskovka <= -1){
            aktualniDeskovka = pocetDeskovek-1;
        }
        zobrazAktualniDeskovku();
    }

    Box horizontalBox = Box.createHorizontalBox();

JTable table = new JTable(3, 2);;
    JScrollPane scrollPane = new JScrollPane(table);
    private void zobrazAktualniDeskovku() {
        DefaultTableModel model = new DefaultTableModel(new Object[][]{{vlastnimeHru.get(aktualniDeskovka), nazevHry.get(aktualniDeskovka), oblibenostHry.get(aktualniDeskovka)}}, new Object[]{"Vlastníme hru", "Název hry", "Oblíbenost"});
        table.setModel(model);
        this.add(Box.createRigidArea(new Dimension(0, 35)));
        this.add(scrollPane);
        this.revalidate();
    }

    public  static  void main(String[] args){
        frame = new MainFrame();
        frame.setSize(400,150);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }
}