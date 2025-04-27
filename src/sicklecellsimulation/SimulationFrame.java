package sicklecellsimulation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class SimulationFrame extends JFrame {
    private final SimulationPanel simulationPanel;
    private final JTextField populationSizeField, sickleCellStartField, reproductionRateField, deathRateField, mutationRateField;
    private final JButton startButton, pauseButton, resetButton, showGraphButton, treeButton;
    private final JSlider speedSlider;
    private final JCheckBox growthToggle;
    private final JLabel populationLabel, healthyLabel, carrierLabel, sickleLabel, generationLabel;
    public GraphWindow graphWindow; //
    public final JCheckBox healthcareToggle;
    private final JCheckBox malariaToggle;
    final JCheckBox driftToggle;


    public SimulationFrame() {

        setTitle("Simulation of Human Sickle Cell Variation Across Generations");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());






        // Simulation Panel
        simulationPanel = new SimulationPanel();
        simulationPanel.setFrameReference(this);


        // Control Panel (Right Side)
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setPreferredSize(new Dimension(250, 700));
        //UI

        String[] regions = { "West Africa", "United States", "Europe", "Custom" };
        JComboBox<String> regionSelector = new JComboBox<>(regions);
        controlPanel.add(new JLabel("Region:"));
        controlPanel.add(regionSelector);


        controlPanel.add(new JLabel("Population Size:"));
        populationSizeField = new JTextField("100", 5);
        controlPanel.add(populationSizeField);

        controlPanel.add(new JLabel("\"Sickle Cell % (Initial Human Population):"));
        sickleCellStartField = new JTextField("30", 3);
        controlPanel.add(sickleCellStartField);

        controlPanel.add(new JLabel("Reproduction Rate (%):"));
        reproductionRateField = new JTextField("2", 3);
        controlPanel.add(reproductionRateField);

        controlPanel.add(new JLabel("Death Rate (%):"));
        deathRateField = new JTextField("1", 3);
        controlPanel.add(deathRateField);

        controlPanel.add(new JLabel("Mutation Rate (%):"));
        mutationRateField = new JTextField("0.5", 3);
        controlPanel.add(mutationRateField);


        healthcareToggle = new JCheckBox("Healthcare Available");
        healthcareToggle.setSelected(true); // default to ON
        controlPanel.add(healthcareToggle);

        malariaToggle = new JCheckBox("Malaria Region");
        malariaToggle.setSelected(true);
        controlPanel.add(malariaToggle);


        growthToggle = new JCheckBox("Enable Population Growth");
        controlPanel.add(growthToggle);

        driftToggle = new JCheckBox("Enable Genetic Drift");
        controlPanel.add(driftToggle);


        startButton = new JButton("Start");
        controlPanel.add(startButton);

        pauseButton = new JButton("Pause");
        controlPanel.add(pauseButton);

        resetButton = new JButton("Reset");
        controlPanel.add(resetButton);


        showGraphButton = new JButton("Show Graph");
        controlPanel.add(showGraphButton);
        treeButton = new JButton("Show Family Tree");
        controlPanel.add(treeButton);

        int buttonWidth = 200;
        Dimension buttonSize = new Dimension(buttonWidth, 35);

        startButton.setMaximumSize(buttonSize);
        pauseButton.setMaximumSize(buttonSize);
        resetButton.setMaximumSize(buttonSize);
        showGraphButton.setMaximumSize(buttonSize);
        treeButton.setMaximumSize(buttonSize);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        growthToggle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pauseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        showGraphButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        treeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        healthcareToggle.setAlignmentX(Component.CENTER_ALIGNMENT);
        malariaToggle.setAlignmentX(Component.CENTER_ALIGNMENT);
        driftToggle.setAlignmentX(Component.CENTER_ALIGNMENT);




        controlPanel.add(Box.createVerticalStrut(10));  // optional spacing
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
        legendPanel.setBorder(BorderFactory.createTitledBorder("Genotype Legend"));
        legendPanel.setBackground(Color.WHITE);
        legendPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // anchor to left

        legendPanel.add(createLegendItem("Does not carry sickle cell (AA)", Color.BLUE));
        legendPanel.add(createLegendItem("Carrier for sickle cell (AS)", new Color(128, 0, 128)));
        legendPanel.add(createLegendItem("Has sickle cell anemia (SS)", Color.RED));

        // Wrap the legend
        JPanel legendWrapper = new JPanel(new BorderLayout());
        legendWrapper.setMaximumSize(new Dimension(250, Integer.MAX_VALUE));
        legendWrapper.setPreferredSize(new Dimension(250, 160));  // optional boost
        legendWrapper.setBackground(Color.WHITE);
        legendWrapper.add(legendPanel, BorderLayout.CENTER);

        controlPanel.add(Box.createVerticalGlue()); // Push legend down
        controlPanel.add(legendWrapper);


        // Stats Panel (top)
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(1, 5));

        populationLabel = new JLabel("Total Population: 0");
        healthyLabel = new JLabel("Healthy: 0");
        carrierLabel = new JLabel("Carriers: 0");
        sickleLabel = new JLabel("Affected (Sickle Cell): 0");
        generationLabel = new JLabel("Generations: 0");

        statsPanel.add(populationLabel);
        statsPanel.add(healthyLabel);
        statsPanel.add(carrierLabel);
        statsPanel.add(sickleLabel);
        statsPanel.add(generationLabel);


        // Speed Slider (Bottom)
        JPanel speedPanel = new JPanel();
        speedPanel.add(new JLabel("Speed:"));

        speedSlider = new JSlider(1, 1000, 100);
        speedSlider.addChangeListener(e -> simulationPanel.setSpeed(510 - speedSlider.getValue()));

        speedPanel.add(speedSlider);

        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel simulationTab = new JPanel(new BorderLayout());
        JScrollPane scrollPanel = new JScrollPane(simulationPanel);
        simulationTab.add(scrollPanel, BorderLayout.CENTER);
        simulationTab.add(controlPanel, BorderLayout.EAST);
        simulationTab.add(statsPanel, BorderLayout.NORTH);

        simulationTab.add(speedPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Simulation", simulationTab);

        JPanel learnPanel = new JPanel();
        learnPanel.setLayout(new BorderLayout());

// Text section
        JTextArea learnText = new JTextArea();
        learnText.setEditable(false);
        learnText.setLineWrap(true);
        learnText.setWrapStyleWord(true);
        learnText.setFont(new Font("SansSerif", Font.PLAIN, 14));
        learnText.setFont(new Font("SansSerif", Font.PLAIN, 14)); // change 14 to whatever size you want
        learnText.setText(
                "What is Sickle Cell Disease? \n\n" +
                "Sickle cell disease (SCD) is a group of inherited red blood cell disorders. It affects the hemoglobin, the molecule in red blood cells that delivers oxygen to the body. \n " +
                        "In people with SCD, red blood cells are shaped like a crescent or “sickle”, instead of the normal round shape. These misshapen cells: \n" +
                        "- Don’t flow well through blood vessels\n" +
                        "\n" +
                        "- Clump together and block circulation\n" +
                        "\n" +
                        "- Break apart easily, causing a shortage of red blood cells (anemia) \n" +
                        "What is Sickle Cell Disease \n\n" +
                        "Sickle cell disease is caused by a mutation in the HBB gene, which provides instructions for making part of hemoglobin.\n" +
                        "\n" +
                        "- There are three key genotypes:\n" +
                        "\n" +
                        "- AA — Normal hemoglobin (no disease)\n" +
                        "\n" +
                        "- AS — Sickle cell trait (a carrier; usually healthy)\n" +
                        "\n" +
                        "- SS — Sickle cell anemia (disease present)\n" +
                        "\n" +
                        "To develop full sickle cell anemia, a person must inherit two sickle alleles (S) — one from each parent.\n\n" +

                        " Symptoms and Risks\n" +
                        "- Episodes of pain (called crises)\n" +
                        "\n" +
                        "- Fatigue, anemia\n" +
                        "\n" +
                        "- Swelling in hands and feet\n" +
                        "\n" +
                        "- Vision problems\n" +
                        "\n" +
                        "- Increased risk of infection and stroke\n" +
                        "\n" +
                        "Symptoms usually begin in early childhood and can be life-threatening without treatment. \n\n" +

                        "There is no universal cure, but treatments include:\n" +
                        "\n" +
                        "- Pain management\n" +
                        "\n" +
                        "- Blood transfusions\n" +
                        "\n" +
                        "- Antibiotics to prevent infection\n" +
                        "\n" +
                        "- Hydroxyurea (a medicine that helps reduce sickling)\n" +
                        "\n" +
                        "- Bone marrow transplant (only curative option, but rare)\n" +
                        "\n" +
                        "With good medical care, people with sickle cell disease can live into adulthood and manage symptoms.\n" +
                        "\n" +

                "Understanding Human Genetic Variation Through Sickle Cell Simulation \n\n" +
                        "This simulation demonstrates how genetic traits, like the sickle cell allele, are shaped by both biological inheritance and environmental factors across generations. It reflects a real-world example of balanced polymorphism, where a trait that may be harmful in one context can provide survival advantages in another. Let's break down what this simulation reveals.\n\n" +

                        "Sickle Cell and Genetics \n" +
                        "There are three genotypes related to the sickle cell gene:\n" +
                        "- AA — Normal hemoglobin (healthy, no sickle trait)\n" +
                        "- AS — Carrier (one sickle allele, one normal)\n" +
                        "- SS — Sickle cell anemia (two sickle alleles, affected)\n" +
                        "The sickle cell trait follows Mendelian inheritance, but its persistence in populations cannot be explained by genetics alone — environment plays a huge role.\n\n" +

                        "Regional Influence: The Malaria Link\n" +
                        "One of the most significant insights from the simulation is the selective advantage of the AS genotype in regions where malaria is prevalent, such as West Africa. Individuals with AS genotype are more resistant to malaria, which increases their survival and reproductive success in these environments — this is why sickle cell persists at high frequencies in some populations.\n\n" +
                        "Source: Allison, A.C. (1954). Protection afforded by sickle-cell trait against subtertian malarial infection. British Medical Journal.\n" +
                        "Source: CDC. https://www.cdc.gov/ncbddd/sicklecell/data.html\n\n" +

                        "Role of Healthcare Access\n" +
                        "In areas with limited access to healthcare, individuals with SS genotype (sickle cell anemia) often face severe health outcomes including pain, anemia, stroke, and early mortality. The simulation models this by increasing death rates when healthcare is unavailable.\n\n" +
                        "When healthcare is accessible, mortality drops significantly, which in turn can allow more SS individuals to survive and reproduce, slightly increasing the frequency of the allele — this is another real-world example of how social factors influence genetic variation.\n\n" +
                        "Source: Piel et al., 2010. Global distribution of the sickle cell gene and geographical confirmation of the malaria hypothesis. Nature Communications.\n\n" +

                        "Evolution in Action\n" +
                        "This simulation shows how evolution is not always about “survival of the fittest” in a universal sense — it’s about survival relative to local environments.\n\n" +
                        "- In malaria-endemic regions, the AS genotype is favored.\n" +
                        "- In regions with advanced healthcare, SS individuals can survive longer.\n" +
                        "- In non-malaria areas with no advantage, the sickle allele may gradually disappear.\n\n"

        );

        JScrollPane learnScroll = new JScrollPane(learnText);

// Image section
        JLabel imageLabel = new JLabel("", JLabel.CENTER);
        try {
            Image image = ImageIO.read(getClass().getResource("/sicklecellsimulation/sickle_cell.jpeg"));
            Image scaled = image.getScaledInstance(600, 250, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            imageLabel.setText("Image not found");
            e.printStackTrace();
        }


// Combine them
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, learnScroll, imageLabel);
        splitPane.setResizeWeight(0.7);  // 70% for text, 30% for image

        learnPanel.add(splitPane, BorderLayout.CENTER);
        tabbedPane.addTab("Learn", learnPanel);
        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);

        // Button Actions

        startButton.addActionListener(e -> {

            String selectedRegion = (String) regionSelector.getSelectedItem();

            switch (selectedRegion) {
                case "West Africa":
                    sickleCellStartField.setText("10");
                    mutationRateField.setText("0.1");
                    healthcareToggle.setSelected(false);
                    malariaToggle.setSelected(true);
                    break;
                case "United States":
                    sickleCellStartField.setText("1");
                    mutationRateField.setText("0.05");
                    healthcareToggle.setSelected(true);
                    malariaToggle.setSelected(false);
                    break;
                case "Europe":
                    sickleCellStartField.setText("0");
                    mutationRateField.setText("0.01");
                    healthcareToggle.setSelected(true);
                    malariaToggle.setSelected(false);
                    break;
                case "Custom":
                    break;

            }


            int populationSize = Integer.parseInt(populationSizeField.getText());
            int sickleStartPercent = Integer.parseInt(sickleCellStartField.getText());
            double reproductionRate = Double.parseDouble(reproductionRateField.getText()) / 100;
            double deathRate = Double.parseDouble(deathRateField.getText()) / 100;
            double mutationRate = Double.parseDouble(mutationRateField.getText()) / 100;
            boolean allowGrowth = growthToggle.isSelected();
            boolean healthcare = healthcareToggle.isSelected();
            boolean malaria = malariaToggle.isSelected();
            simulationPanel.startSimulation(populationSize, sickleStartPercent, allowGrowth, reproductionRate, deathRate, mutationRate, healthcare, malaria);
        });

        pauseButton.addActionListener(e -> {
            simulationPanel.togglePause();
            pauseButton.setText(simulationPanel.isPaused() ? "Resume" : "Pause");
        });

        resetButton.addActionListener(e -> simulationPanel.resetSimulation());

        showGraphButton.addActionListener(e -> {
            if (graphWindow == null) {
                graphWindow = new GraphWindow();
            } else {
                graphWindow.setVisible(true);
            }
        });
        treeButton.addActionListener(e -> {
            Individual root = simulationPanel.getRootAncestor();
            if (root == null) return;

            FamilyTreePanel panel = new FamilyTreePanel(root);
            JScrollPane scrollPane = new JScrollPane(panel);

            JFrame treeWindow = new JFrame("Family Tree Viewer");
            treeWindow.setSize(1000, 800);
            treeWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            treeWindow.add(scrollPane);
            treeWindow.setVisible(true);
        });

    }
    private JPanel createLegendItem(String description, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        item.setBackground(Color.WHITE);
        item.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel colorSwatch = new JLabel();
        colorSwatch.setPreferredSize(new Dimension(24, 24));
        colorSwatch.setOpaque(true);
        colorSwatch.setBackground(color);
        colorSwatch.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel text = new JLabel("<html><body style='width:180px'>" + description + "</body></html>");
        text.setFont(new Font("SansSerif", Font.PLAIN, 14));

        item.add(colorSwatch);
        item.add(text);
        return item;
    }
    public void clearGraphData() {
        if (graphWindow != null) {
            graphWindow.clearGraph();  
        }
    }



    public void updateStats(int generation, int healthy, int carriers, int sickle) {
        populationLabel.setText("Total Population: " + (healthy + carriers + sickle));
        healthyLabel.setText("Healthy: " + healthy);
        carrierLabel.setText("Carriers: " + carriers);
        sickleLabel.setText("Affected (Sickle Cell): " + sickle);
        generationLabel.setText("Generations: " + generation);

        if (graphWindow != null) {
            graphWindow.updateGraph(generation, healthy, carriers, sickle);
        }
    }
}
