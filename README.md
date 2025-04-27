# Sickle Cell Simulation

This project is a research-informed, interactive Java simulation that models the inheritance and evolutionary dynamics of the sickle cell allele in human populations. It visualizes how factors like malaria prevalence, healthcare access, mutation, and environmental pressure shape the distribution of genotypes (AA, AS, SS) across generations.

## Features

- Dynamic population simulation with genotype tracking across generations
- Customizable settings:
  - Malaria region toggle (advantage for AS carriers)
  - Healthcare access toggle (affects SS survival)
  - Reproduction and death rate inputs
  - Population growth and mutation rate controls
- Real-time visual output:
  - Line graph showing genotype frequency over time
  - Pie chart showing population composition
  - Optional family tree view
- Integrated Learn tab:
  - Detailed explanation of biological principles behind the simulation
  - Cited scientific sources and historical context

## How It Works

Users can control:
- Starting population size and sickle cell frequency
- Mutation rate and mortality rate
- Whether the simulation takes place in a malaria-endemic region
- Whether healthcare is available to reduce SS mortality
- Region presets for West Africa, the United States, and Europe

The simulation runs generational cycles where individuals reproduce, mutate, and die. It models genotype inheritance using Mendelian rules and includes selection pressure from environmental and healthcare variables.

# Video 
https://youtu.be/QK33uys2c_c   

## Screenshots

### Main Simulation
![image](https://github.com/user-attachments/assets/30cc8b2d-d7a8-4f31-b59d-2896909f94ed)

### Tree View
![image](https://github.com/user-attachments/assets/64b51b37-b28f-49e0-bdf1-70e6a4b6be6f)

### Graph View
![image](https://github.com/user-attachments/assets/d2172262-190c-4c49-a8f0-36a5dff79006)

### Learn Tab
![image](https://github.com/user-attachments/assets/d6b3f912-5514-490c-b2aa-7a8fe3e29bc2)





## Scientific Sources

- Allison, A.C. (1954). *Protection afforded by sickle-cell trait against malaria*. British Medical Journal.
- Piel, F.B., et al. (2010). *Global distribution of the sickle cell gene and geographical confirmation of the malaria hypothesis*. Nature Communications.
- Centers for Disease Control and Prevention. *Sickle Cell Disease: Data & Statistics*. https://www.cdc.gov/ncbddd/sicklecell/data.html
- World Health Organization. *Sickle Cell Disease Fact Sheet*. https://www.who.int/news-room/fact-sheets/detail/sickle-cell-disease
- Tishkoff, S.A., et al. (2001). *Haplotype diversity and malarial resistance*. Science.

## Installation

1. Clone this repository:
   ```bash
   git clone https://github.com/your-username/sickle-cell-simulation.git
