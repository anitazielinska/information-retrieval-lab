package com.main;

import opennlp.tools.chunker.Chunker;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.formats.ad.ADSentenceStream;
import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetector;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.ml.model.SequenceClassificationModel;
import opennlp.tools.namefind.NameFinderEventStream;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinder;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import opennlp.tools.util.TokenTag;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class OpenNLP {

    public static String LANG_DETECT_MODEL = "models/langdetect-183.bin";
    public static String TOKENIZER_MODEL = "models/en-token.bin";
    public static String SENTENCE_MODEL = "models/en-sent.bin";
    public static String POS_MODEL = "models/en-pos-maxent.bin";
    public static String CHUNKER_MODEL = "models/en-chunker.bin";
    public static String LEMMATIZER_DICT = "models/en-lemmatizer.dict";
    public static String NAME_MODEL = "models/en-ner-person.bin";
    public static String ENTITY_XXX_MODEL = "models/en-ner-xxx.bin";

	public static void main(String[] args) throws IOException
    {
		OpenNLP openNLP = new OpenNLP();
		openNLP.run();
	}

	public void run() throws IOException
    {
		//languageDetection();
		//tokenization();
        //sentenceDetection();
		//posTagging();
		//lemmatization();
		//stemming();
		//chunking();
		//nameFinding();
	}

	private void languageDetection() throws IOException
    {
		String text = "";

//		 text = "Many cats like milk because in some ways it reminds them of their mother's milk. Le lait n'est pas forc�ment mauvais pour les chats. "
//		 + "Der Normalfall ist allerdings der, dass Salonl�wen Milch weder brauchen noch gut verdauen k�nnen.";

		File modelFile = new File(LANG_DETECT_MODEL);
		LanguageDetectorModel model = new LanguageDetectorModel(modelFile);
		LanguageDetector languageDetector = new LanguageDetectorME(model);
		Language language = languageDetector.predictLanguage(text);
		System.out.println(language.toString());
	}

	private void tokenization() throws IOException
    {
		String text = "";

//		text = "Since cats were venerated in ancient Egypt, they were commonly believed to have been domesticated there, "
//				+ "but there may have been instances of domestication as early as the Neolithic from around 9500 years ago (7500 BC).";
//		text = "Since cats were venerated in ancient Egypt, they were commonly believed to have been domesticated there, "
//				+ "but there may have been instances of domestication as early as the Neolithic from around 9,500 years ago (7,500 BC).";
//		text = "Since cats were venerated in ancient Egypt, they were commonly believed to have been domesticated there, "
//		 + "but there may have been instances of domestication as early as the Neolithic from around 9 500 years ago ( 7 500 BC).";

		File modelFile = new File(TOKENIZER_MODEL);
		TokenizerModel model = new TokenizerModel(modelFile);
		Tokenizer tokenizer = new TokenizerME(model);
		String[] tokenize = tokenizer.tokenize(text);
		System.out.println(Arrays.toString(tokenize));
	}

	private void sentenceDetection() throws IOException
    {
		String text = "";

//		text = "Hi. How are you? Welcome to OpenNLP. "
//				+ "We provide multiple built-in methods for Natural Language Processing.";
//		text = "Hi. How are you?! Welcome to OpenNLP? "
//				+ "We provide multiple built-in methods for Natural Language Processing.";
//		text = "Hi. How are you? Welcome to OpenNLP.?? "
//				+ "We provide multiple . built-in methods for Natural Language Processing.";
//		text = "The interrobang, also known as the interabang (often represented by ?! or !?), "
//				+ "is a nonstandard punctuation mark used in various written languages. "
//				+ "It is intended to combine the functions of the question mark (?), or interrogative point, "
//				+ "and the exclamation mark (!), or exclamation point, known in the jargon of printers and programmers as a \"bang\". ";

		File modelFile = new File(SENTENCE_MODEL);
		SentenceModel model = new SentenceModel(modelFile);
		SentenceDetector sentenceDetector = new SentenceDetectorME(model);
		String[] strings = sentenceDetector.sentDetect(text);
		System.out.println(Arrays.toString(strings));
	}

	private void posTagging() throws IOException {
		String[] sentence = new String[0];

//		sentence = new String[] { "Cats", "like", "milk" };
//		sentence = new String[]{"Cat", "is", "white", "like", "milk"};
//		sentence = new String[] { "Hi", "How", "are", "you", "Welcome", "to", "OpenNLP", "We", "provide", "multiple",
//				"built-in", "methods", "for", "Natural", "Language", "Processing" };
//		sentence = new String[] { "She", "put", "the", "big", "knives", "on", "the", "table" };

		File modelFile = new File(POS_MODEL);
		POSModel model = new POSModel(modelFile);
		POSTagger posTagger = new POSTaggerME(model);
		String[] tag = posTagger.tag(sentence);
		System.out.println(Arrays.toString(tag));
	}

	private void lemmatization() throws IOException
    {
		String[] text = new String[0];
		String[] tags = new String[0];

//		text = new String[] { "Hi", "How", "are", "you", "Welcome", "to", "OpenNLP", "We", "provide", "multiple",
//				"built-in", "methods", "for", "Natural", "Language", "Processing" };

//		tags = new String[] { "NNP", "WRB", "VBP", "PRP", "VB", "TO", "VB", "PRP", "VB", "JJ", "JJ", "NNS", "IN", "JJ",
//				"NN", "VBG" };

		File lemmatizerFile = new File(LEMMATIZER_DICT);
		DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(lemmatizerFile);
		String[] lemmatize = lemmatizer.lemmatize(text, tags);
		System.out.println(Arrays.toString(lemmatize));
	}

	private void stemming()
    {
		String[] sentence = new String[0];
//		sentence = new String[] { "Hi", "How", "are", "you", "Welcome", "to", "OpenNLP", "We", "provide", "multiple",
//				"built-in", "methods", "for", "Natural", "Language", "Processing" };

		PorterStemmer porterStemmer = new PorterStemmer();
		String stem = porterStemmer.stem(Arrays.toString(sentence));
		System.out.println(stem);
	}
	
	private void chunking() throws IOException
    {
		String[] sentence = new String[0];
		String[] tags = new String[0];

//		sentence = new String[] { "She", "put", "the", "big", "knives", "on", "the", "table" };
//		tags = new String[] { "PRP", "VBD", "DT", "JJ", "NNS", "IN", "DT", "NN" };

		File modelFile = new File(CHUNKER_MODEL);
		ChunkerModel chunkerModel = new ChunkerModel(modelFile);
		Chunker chunker = new ChunkerME(chunkerModel);
		String[] chunk = chunker.chunk(sentence, tags);
		System.out.println(Arrays.toString(chunk));
	}

	private void nameFinding() throws IOException
    {
//		String text = "he idea of using computers to search for relevant pieces of information was popularized in the article "
//				+ "As We May Think by Vannevar Bush in 1945. It would appear that Bush was inspired by patents "
//				+ "for a 'statistical machine' - filed by Emanuel Goldberg in the 1920s and '30s - that searched for documents stored on film. "
//				+ "The first description of a computer searching for information was described by Holmstrom in 1948, "
//				+ "detailing an early mention of the Univac computer. Automated information retrieval systems were introduced in the 1950s: "
//				+ "one even featured in the 1957 romantic comedy, Desk Set. In the 1960s, the first large information retrieval research group "
//				+ "was formed by Gerard Salton at Cornell. By the 1970s several different retrieval techniques had been shown to perform "
//				+ "well on small text corpora such as the Cranfield collection (several thousand documents). Large-scale retrieval systems, "
//				+ "such as the Lockheed Dialog system, came into use early in the 1970s.";

		String[] strings = new String[1];
		File nameModelFile = new File(NAME_MODEL);

		TokenNameFinderModel tokenNameFinderModel = new TokenNameFinderModel(nameModelFile);
		TokenNameFinder nameFinder = new NameFinderME(tokenNameFinderModel);
		Span[] spans = nameFinder.find(strings);
		System.out.println(Arrays.toString(spans));

		File xxxModelFile = new File(ENTITY_XXX_MODEL);
		tokenNameFinderModel = new TokenNameFinderModel(xxxModelFile);
		nameFinder = new NameFinderME(tokenNameFinderModel);
		spans = nameFinder.find(strings);
		System.out.println(Arrays.toString(spans));

	}



}
