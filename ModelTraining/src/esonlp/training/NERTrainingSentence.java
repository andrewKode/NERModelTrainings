package esonlp.training;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.Span;

public class NERTrainingSentence {
    //Class for training sentence detector model which will later be using for ingesting text in ES DataBase
    public NERTrainingSentence() {
    }

    public String[] sentenceSplit(String sentence) {
        String sentenceDivider = "[.?!]";
        return sentence.split(sentenceDivider);
    }

    public boolean checkRefModel(String modelPath) {
        Path refModelPath = Paths.get(modelPath);
        return Files.exists(refModelPath);
    }

    public void prettyPrintInput(String[] inputText) {
        Stream<String> collectedSentences = Arrays.stream(inputText);
        collectedSentences.forEach(System.out::println);
    }

    public void learn(String modelPath, String probe) {

        SentenceModel model = null;
        if (checkRefModel(modelPath)) {
            try {
                InputStream inputStream = new FileInputStream(modelPath);
                model = new SentenceModel(inputStream);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            System.out.println("Invalid path for reference model.");
        }

        if (model != null) {
            SentenceDetectorME sentenceAnalyzer = new SentenceDetectorME(model);
            Span[] spans = sentenceAnalyzer.sentPosDetect(probe);
            for (Span span : spans)
                System.out.println(span);
        }

    }

    public static void main(String[] args) throws Exception {
        String inputText = "This would be the first sentence to test. This, well this is the other one. Also, I hate BSFI!";
        String openNLPRefModel = "/home/andrei/Seagate/Proiecte/NERModelTrainings/ModelTraining/ref_models/en-sent.bin";

        NERTrainingSentence nerModel = new NERTrainingSentence();

        nerModel.prettyPrintInput(nerModel.sentenceSplit(inputText));
        nerModel.learn(openNLPRefModel, inputText);
    }
}




