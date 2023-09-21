package ufcg.splab.collector;

import ufcg.splab.model.CompositeRefactoring;
import ufcg.splab.model.Method;
import ufcg.splab.model.OrganicClass;
import ufcg.splab.model.Refactoring;
import ufcg.splab.utils.IncompleteCompositeUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ufcg.splab.utils.Constants.*;

public class IncompleteCompositeCollector {
    public List<CompositeRefactoring> collectFeatureEnvyIncompleteComposites() throws IOException {

        CandidateCollector candidateCollector = new CandidateCollector();
        IncompleteCompositeUtils incompleteCompositeUtils = new IncompleteCompositeUtils();
        List<CompositeRefactoring> candidates = candidateCollector.collectCandidates();
        List<CompositeRefactoring> incompleteComposites = new ArrayList<>();
        int incompleteCompositesNumber = 0;

        for (CompositeRefactoring compositeRefactoring : candidates) {

            List<String> desiredCommits = incompleteCompositeUtils.desiredCommits(compositeRefactoring);

            String currentCommit = desiredCommits.get(1); //pega o commit após no REFACTORING

            Method[] organicMethodsAfterRefactor = incompleteCompositeUtils.loadOrganicMethods(currentCommit);
            List<String> smellyMethodsAfterRefactor = incompleteCompositeUtils.getSmellyMethods(organicMethodsAfterRefactor);
            List<String> elements = new ArrayList<>();

            for (Refactoring refactoring : compositeRefactoring.getRefactorings()) { //pega um refactoring desse composite

                String refactoredMethod = incompleteCompositeUtils.extractClassAndMethodNames(refactoring, false);
                String refactoredMethodAfter = incompleteCompositeUtils.extractClassAndMethodNames(refactoring, true);

                if (smellyMethodsAfterRefactor.contains(refactoredMethod) || smellyMethodsAfterRefactor.contains(refactoredMethodAfter)) {
                    elements = incompleteCompositeUtils.retrieveInvolvedElements(elements, refactoredMethod, refactoredMethodAfter);
                    if (!incompleteComposites.contains(compositeRefactoring)){
                        incompleteComposites.add(compositeRefactoring);
                        incompleteCompositesNumber++;
                    }
                }
            }
            /*debug only*/
            if(!elements.isEmpty()) {
                System.out.println("Incomplete composite ID: " + compositeRefactoring.getId());
                System.out.println("Involved methods: " + elements);
            }
            /*----------*/
        }
        System.out.println("Number of feature envy incomplete composites: " + incompleteCompositesNumber);
        return incompleteComposites;
    }

    public List<CompositeRefactoring> collectGodClassIncompleteComposites() throws IOException {

        int countIncompleteComposites = 0;
        IncompleteCompositeUtils incompleteCompositeUtils = new IncompleteCompositeUtils();
        CandidateCollector candidateCollector = new CandidateCollector();
        List<CompositeRefactoring> incompleteComposites = new ArrayList<>();

        List<CompositeRefactoring> candidates = candidateCollector.collectCandidates();
        String refactoredClass;

        for (CompositeRefactoring compositeRefactoring: candidates) {

            List<String> desiredCommits = incompleteCompositeUtils.desiredCommits(compositeRefactoring);
            String currentCommit = desiredCommits.get(1);
            OrganicClass[] organicClassesAfterRefactor = incompleteCompositeUtils.loadOrganicClasses(currentCommit);
            List<String> smellyClassesAfterRefactor = incompleteCompositeUtils.getSmellyClasses(organicClassesAfterRefactor);

            for (Refactoring refactoring : compositeRefactoring.getRefactorings()) {

                if(refactoring.getRefactoringType().equals("Rename Class")) {
                    refactoredClass = refactoring.getElements().get(1).getClassName();
                } else {
                    refactoredClass = refactoring.getElements().get(0).getClassName();
                }

                if (smellyClassesAfterRefactor.contains(refactoredClass) && !incompleteComposites.contains(compositeRefactoring)) {
                    incompleteComposites.add(compositeRefactoring);
                    System.out.println(refactoredClass);
                    System.out.println("God Class incomplete composite ID: " + compositeRefactoring.getId());
                    countIncompleteComposites++;
                }
            }
        }
        System.out.println("God Class Incomplete Composites: " + countIncompleteComposites);

        return incompleteComposites;
    }
}
