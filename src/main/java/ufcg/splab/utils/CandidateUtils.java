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

public class CandidateUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public boolean detectDesiredRefactoringType (CompositeRefactoring composite, String singleRefactoring) {
        return composite.getRefactorings() != null &&
                composite.getRefactorings().stream()
                    .anyMatch(refactoring ->
                        refactoring.getRefactoringType() != null &&
                            (refactoring.getRefactoringType().equals(singleRefactoring) || refactoring.getRefactoringType().equals("Pull Up Method") /*|| refactoring.getRefactoringType().equals("Extract Method")*/)
                    );
    }

    public boolean detectDesiredRefactoringType (CompositeRefactoring composite, String singleRefactoring, String secondSingleRefactoring) {
        return composite.getRefactorings() != null &&
                composite.getRefactorings().stream()
                        .anyMatch(refactoring ->
                                refactoring.getRefactoringType() != null &&
                                        (refactoring.getRefactoringType().equals(singleRefactoring)
                                                || refactoring.getRefactoringType().equals(secondSingleRefactoring) /*)*/
                                                || refactoring.getRefactoringType().equals("Pull Up Method"))
                        );
    }
}
