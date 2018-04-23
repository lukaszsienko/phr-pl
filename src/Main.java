import edu.stanford.nlp.mt.Phrasal;
import edu.stanford.nlp.mt.train.AlignmentGrid;
import edu.stanford.nlp.mt.train.PhraseExtract;
import edu.stanford.nlp.mt.util.IOTools;
import edu.stanford.nlp.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by lsienko on 12.04.18.
 */
public class Main {
    public static void main(String[] args) {
        // BUILDING TRANSLATION MODEL (PHRASE TABLE EXTRACTION)
        //File berkeley_aligner_folder = new File("resources/aligner_output");
        //String aligner_folder_path = berkeley_aligner_folder.getAbsolutePath();

        //File translation_model_folder = new File("resources/translation_model");
        //String translation_model_path = translation_model_folder.getAbsolutePath();

        String aligner_folder_path = args[0];
        String translation_model_path = args[1];

        String[] phrase_extract_args = new String[12];
        phrase_extract_args[0] = "-threads";
        phrase_extract_args[1] = "2";
        phrase_extract_args[2] = "-inputDir";
        phrase_extract_args[3] = aligner_folder_path;
        phrase_extract_args[4] = "-outputDir";
        phrase_extract_args[5] = translation_model_path;
        phrase_extract_args[6] = "-extractors";
        phrase_extract_args[7] = "edu.stanford.nlp.mt.train.MosesPharoahFeatureExtractor=phrase-table.gz:edu.stanford.nlp.mt.train.CountFeatureExtractor=phrase-table.gz:edu.stanford.nlp.mt.train.LexicalReorderingFeatureExtractor=lo-hier.msd2-bidirectional-fe.gz";
        phrase_extract_args[8] = "-hierarchicalOrientationModel";
        phrase_extract_args[9] = "true";
        phrase_extract_args[10] = "-orientationModelType";
        phrase_extract_args[11] = "msd2-bidirectional-fe";

        //TODO: check why it did not calculate itself
        //phrase_extract_args[12] = "-maxLen";
        //phrase_extract_args[13] = "256";

        System.setProperty("ShowPhraseRestriction", "true");

        try {
            PhraseExtract.main(phrase_extract_args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void decode(String[] args) throws Exception {
        final Properties options = StringUtils.argsToProperties(args);
        final String configFile = options.containsKey("") ? (String) options.get("") : null;

        final Map<String, List<String>> configuration = getConfigurationFrom(configFile, options);
        final Phrasal p = Phrasal.loadDecoder(configuration);

        //if (options.containsKey("text")) p.decode(new FileInputStream(new File(options.getProperty("text"))), true);
        //else p.decode(System.in, true);

        p.decode(System.in, true);
    }

    private static Map<String, List<String>> getConfigurationFrom(String configFile, Properties options)
            throws IOException {
        final Map<String, List<String>> config = configFile == null ? new HashMap<>()
                : IOTools.readConfigFile(configFile);
        // Command-line options supersede config file options
        options.entrySet().stream().forEach(e -> config.put(e.getKey().toString(),
                Arrays.asList(e.getValue().toString().split("\\s+"))));
        return config;
    }
}
