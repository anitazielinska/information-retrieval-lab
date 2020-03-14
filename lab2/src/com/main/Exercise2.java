package com.main;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageResult;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

//import org.apache.tika.mime.MimeType;

public class Exercise2
{

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private OptimaizeLangDetector langDetector;

    public static void main(String[] args)
    {
        Exercise2 exercise = new Exercise2();
        exercise.run();
    }

    private void run()
    {
        try
        {
            if (!new File("./outputDocuments").exists())
            {
                Files.createDirectory(Paths.get("./outputDocuments"));
            }

            initLangDetector();

            File directory = new File("./documents");
            File[] files = directory.listFiles();
            for (File file : files)
            {
                processFile(file);
            }
        } catch (IOException | SAXException | TikaException e)
        {
            e.printStackTrace();
        }

    }

    private void initLangDetector() throws IOException
    {
        // TODO initialize language detector (langDetector)
        this.langDetector = new OptimaizeLangDetector();
        this.langDetector.loadModels();
    }

    private void processFile(File file) throws IOException, SAXException, TikaException {
        // TODO: extract content, metadata and language from given file
        // call saveResult method to save the data

        InputStream stream = new FileInputStream(file);
        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        BodyContentHandler handler = new BodyContentHandler(-1);
        parser.parse(stream, handler, metadata);

        String text = handler.toString();

        LanguageResult detect;
        detect = this.langDetector.detect(text);
        String language = detect.toString();

        String author = metadata.get(TikaCoreProperties.CREATOR);
        Date date = metadata.getDate(TikaCoreProperties.CREATED);
        Date lastModification = metadata.getDate(TikaCoreProperties.MODIFIED);

        Tika tika = new Tika();
        String mediaType = tika.detect(stream, metadata);
        String content = handler.toString();
        saveResult(file.getName(), language, author, date, lastModification, mediaType, content); //TODO: fill with proper values
    }

    private void saveResult(String fileName, String language, String creatorName, Date creationDate,
                            Date lastModification, String mimeType, String content)
    {

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        int index = fileName.lastIndexOf(".");
        String outName = fileName.substring(0, index) + ".txt";
        try
        {
            PrintWriter printWriter = new PrintWriter("./outputDocuments/" + outName);
            printWriter.write("Name: " + fileName + "\n");
            printWriter.write("Language: " + (language != null ? language : "") + "\n");
            printWriter.write("Creator: " + (creatorName != null ? creatorName : "") + "\n");
            String creationDateStr = creationDate == null ? "" : dateFormat.format(creationDate);
            printWriter.write("Creation date: " + creationDateStr + "\n");
            String lastModificationStr = lastModification == null ? "" : dateFormat.format(lastModification);
            printWriter.write("Last modification: " + lastModificationStr + "\n");
            printWriter.write("MIME type: " + (mimeType != null ? mimeType : "") + "\n");
            printWriter.write("\n");
            printWriter.write(content + "\n");
            printWriter.close();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

}
