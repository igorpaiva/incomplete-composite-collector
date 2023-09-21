package ufcg.splab.collector;

import com.fasterxml.jackson.databind.ObjectMapper;
import ufcg.splab.model.CompositeRefactoring;
import ufcg.splab.utils.CandidateUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ufcg.splab.utils.Constants.*;

public class CandidateCollector {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static CandidateUtils candidateUtils = new CandidateUtils();
    private static final File compositesFile = new File(ROOT_FOLDER + COMPOSITE_COLLECTOR_FOLDER + projectName + "-composites.json");
    private static List<CompositeRefactoring> candidateComposites = new ArrayList<>();

    public static void main(String[] args) {

        CandidateCollector candidateCollector = new CandidateCollector();

        try {
            List<CompositeRefactoring> testCandidates = candidateCollector.collectCandidates();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<CompositeRefactoring> collectCandidates() throws IOException {
        CompositeRefactoring[] compositeRefactorings = mapper.readValue(compositesFile, CompositeRefactoring[].class);
        int countCandidates = 0;

        for (CompositeRefactoring composite : compositeRefactorings) {

            if (!candidateUtils.detectDesiredRefactoringType(composite, singleRefactoring, secondSingleRefactoring)) {
                continue;
            }

            countCandidates++;
            System.out.println("Candidate composite id: " + composite.getId());

            candidateComposites.add(composite);
        }
        System.out.println("Number of incomplete composite candidates: " + countCandidates);
        return candidateComposites;
    }
}
