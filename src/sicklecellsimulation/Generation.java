package sicklecellsimulation;

import java.util.List;

public class Generation {
    private List<Individual> individuals;

    public Generation(List<Individual> individuals) {
        this.individuals = individuals;
    }

    public int[] countAlleles() {
        int aCount = 0;
        int sCount = 0;
        for (Individual ind : individuals) {
            String gt = ind.getGenotype();
            if (gt != null && gt.length() == 2) {
                char a1 = gt.charAt(0);
                char a2 = gt.charAt(1);
                if (a1 == 'A') aCount++; else if (a1 == 'S') sCount++;
                if (a2 == 'A') aCount++; else if (a2 == 'S') sCount++;
            }
        }
        return new int[]{aCount, sCount};
    }

    public int size() {
        return individuals.size();
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }
}


