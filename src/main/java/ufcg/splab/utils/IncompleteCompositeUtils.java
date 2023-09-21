package ufcg.splab.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import ufcg.splab.model.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ufcg.splab.utils.Constants.*;
import static ufcg.splab.utils.Constants.JSON_FILE_EXTENSION;

public class IncompleteCompositeUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public List<String> desiredCommits(CompositeRefactoring composite) {

        List<String> desiredCommits = new ArrayList<>();

        int previousStateNum = 100000;
        int lastCommitNum = 0;

        String previousState = "";
        String lastCommit = "";

        for (Refactoring refactoring : composite.getRefactorings()) {
            if(refactoring.getCurrentCommit().getOrderCommit() < previousStateNum) {
                previousStateNum = refactoring.getCurrentCommit().getOrderCommit();
                previousState = refactoring.getCurrentCommit().getPreviousCommit();
            }

            if (refactoring.getCurrentCommit().getOrderCommit() > lastCommitNum) {
                lastCommitNum = refactoring.getCurrentCommit().getOrderCommit();
                lastCommit = refactoring.getCurrentCommit().getCommit();
            }
        }

        desiredCommits.add(previousState);
        desiredCommits.add(lastCommit);

        return desiredCommits;
    }

    public List<String> getSmellyClasses(OrganicClass[] organicClasses) { //se a classe tem o code smell selecionado, adiciona à lista
        return Arrays.stream(organicClasses)
                .filter(organicClass -> Smell.stream(organicClass.getSmells())
                        .anyMatch(smell -> smell.getName().equals(classCodeSmell)))
                .map(OrganicClass::getFullyQualifiedName)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getSmellyMethods(Method[] organicMethods) {
        ArrayList<String> smellyMethods = new ArrayList<>();
        for (Method organicMethod : organicMethods) {
            for (Smell smell : organicMethod.getSmells()) {
                if (smell.getName().equals(methodCodeSmell)) {
                    smellyMethods.add(extractClassAndMethodNamesFromOrganicMethod(organicMethod.getFullyQualifiedName()));
                }
            }
        }
        return smellyMethods;
    }

    public String extractClassAndMethodNames(Refactoring refactoring, boolean isAfterRefactoring) {
        String methodName;
        String className;

        if(isAfterRefactoring && (refactoring.getElements().size() > 1)) {
            className = extractClassName(refactoring.getElements().get(1).getClassName());
            methodName = extractMethodName(refactoring.getElements().get(1).getMethodName());
        } else {
            methodName = extractMethodName(refactoring.getElements().get(0).getMethodName());
            className = extractClassName(refactoring.getElements().get(0).getClassName());
        }

        return className + "." + methodName;
    }

    public String extractMethodName(String inputString) {
        String methodName = "";

        if (inputString == null) {
            return "";
        }

        if (inputString.contains("(")) {
            // Extract method name from the first string
            int spaceIndex = inputString.indexOf(" ");
            int parenthesesIndex = inputString.indexOf("(");

            if (spaceIndex != -1 && parenthesesIndex != -1 && spaceIndex < parenthesesIndex) {
                methodName = inputString.substring(spaceIndex + 1, parenthesesIndex);
            }
        } else if (inputString.contains(".")) {
            // Extract method name from the second string
            int lastDotIndex = inputString.lastIndexOf(".");
            if (lastDotIndex != -1 && lastDotIndex < inputString.length() - 1) {
                methodName = inputString.substring(lastDotIndex + 1);
            }
        }

        return methodName;
    }

    public String extractClassName(String inputString) {
        if (inputString != null && !inputString.isEmpty()) {
            int lastDotIndex = inputString.lastIndexOf('.');
            if (lastDotIndex != -1 && lastDotIndex < inputString.length() - 1) {
                return inputString.substring(lastDotIndex + 1);
            }
        }
        return "";
    }

    public String extractClassAndMethodNamesFromOrganicMethod(String inputString) {
        if (inputString != null && !inputString.isEmpty()) {
            String[] parts = inputString.split("\\.");
            int length = parts.length;

            if (length >= 2) {
                return parts[length - 2] + "." + parts[length - 1];
            }
        }
        return "";
    }

    public List<Method> findSmellyMethodsByFullyQualifiedName(String inputString, Method[] methods) {
        String[] parts = inputString.split("\\.");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Input string must be in the format 'ClassName.MethodName'");
        }

        String inputClassName = parts[0];
        String methodName = parts[1];

        List<Method> foundMethods = new ArrayList<>();

        for (Method method : methods) {
            String fullyQualifiedName = method.getFullyQualifiedName();

            if(fullyQualifiedName == null) {
                continue;
            }

            String[] fqNameParts = fullyQualifiedName.split("\\.");
            int numParts = fqNameParts.length;
            if (numParts >= 2 &&
                    fqNameParts[numParts - 2].equals(inputClassName) &&
                    fqNameParts[numParts - 1].equals(methodName)) {

                // No need to create a new Method object; use the existing one
                if(Arrays.stream(method.getSmells()).toArray().length != 0) {
                    foundMethods.add(method);
                }
            }
        }

        return foundMethods;
    }

    public Method[] loadOrganicMethods(String commit) throws IOException {
        File smellsFile = new File(ROOT_FOLDER + SMELL_MINER_FOLDER + projectName + "\\" + commit + JSON_FILE_EXTENSION);
        if (!smellsFile.exists()) {
            return new Method[0];
        }
        OrganicClass[] organicClasses = mapper.readValue(smellsFile, OrganicClass[].class);
        return Arrays.stream(organicClasses)
                .flatMap(organicClass -> Arrays.stream(organicClass.getMethods()))
                .toArray(Method[]::new);
    }

    public OrganicClass[] loadOrganicClasses(String commit) throws IOException {
        File smellsFile = new File(ROOT_FOLDER + SMELL_MINER_FOLDER + projectName + "\\" + commit + JSON_FILE_EXTENSION);
        if (!smellsFile.exists()) {
            return new OrganicClass[0];
        }

        return mapper.readValue(smellsFile, OrganicClass[].class);
    }

    public List<String> retrieveInvolvedElements (List<String> elements, String refactoredMethod, String refactoredMethodAfter) {
        if(!elements.contains(refactoredMethod) && !elements.contains(refactoredMethodAfter)) {
            if(refactoredMethod.equals(refactoredMethodAfter)) {
                elements.add(refactoredMethod);
            } else {
                elements.add(refactoredMethodAfter);
            }
        }
        return elements;
    }
}
