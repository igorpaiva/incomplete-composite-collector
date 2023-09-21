package ufcg.splab;

import ufcg.splab.collector.IncompleteCompositeCollector;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        IncompleteCompositeCollector incompleteCompositeCollector = new IncompleteCompositeCollector();

        boolean collectFeatureEnvyIncompleteComposites = false;

        boolean collectGodClassIncompleteComposites = true;

        if(collectFeatureEnvyIncompleteComposites) {
            incompleteCompositeCollector.collectFeatureEnvyIncompleteComposites();
        }

        if(collectGodClassIncompleteComposites) {
            incompleteCompositeCollector.collectGodClassIncompleteComposites();
        }

    }
}