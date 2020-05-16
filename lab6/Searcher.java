import opennlp.tools.parser.Cons;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Locale;

public class Searcher
{

    public static void main(String args[]) throws IOException, ParseException {
        // Load the previously generated index (DONE)
        IndexReader reader = getIndexReader();
        assert reader != null;

        // Construct index searcher (DONE)
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        // Standard analyzer - might be helpful
        Analyzer analyzer = new StandardAnalyzer();

        // TODO your task is to construct several queries and seek for relevant documents

        // TERM QUERY
        // A Query that matches documents containing a term.
        // This may be combined with other terms with a BooleanQuery.
        // TODO seek for documents that contain word mammal
        // as you may notice, this word is not normalized (but is should be normalized
        // in the same way as all documents were normalized when constructing the index.
        // For that reason you can use analyzer object (utf8TOString()!).
        // Then, build a Term object (seek in content - Constants.content) and TermQuery.
        // Lastly, invoke printResultsForQuery.
        String queryMammal = "MaMMal";
        TermQuery tq1;
        {
            // --------------------------------------
            // COMPLETE THE CODE HERE
            System.out.println("1) term query: mammal (CONTENT)");
            String normalizedMammal = analyzer.normalize("mammal", queryMammal).utf8ToString();
            Term term = new Term(Constants.content, normalizedMammal);
            tq1 = new TermQuery(term);
            printResultsForQuery(indexSearcher, tq1);
         
            // --------------------------------------
        } // DONE

        // TODO Repeat the previous step for a word "bird".
        // Compare the results for "mammal" and "bird".
        String queryBird = "bird";
        TermQuery tq2;
        {
            // --------------------------------------
            System.out.println("2) term query bird (CONTENT)");
            String normalizedBird = analyzer.normalize("bird", queryBird).utf8ToString();
            Term term = new Term(Constants.content, normalizedBird);
            tq2 = new TermQuery(term);
            printResultsForQuery(indexSearcher, tq2);
        
            // --------------------------------------
        } // DONE

        // TODO now, we seek for documents that contain "mammal" or "bird".
        // Construct two clauses: BooleanClause (use BooleanClause.Occur to set a proper flag).
        // The first concerns tq1 ("mammal") and the second concerns ("bird").
        // To construct BooleanQuery, Use static methods of BooleanQuery.Builder().
        // Additionally, use setMinimumNumberShouldMatch() with a proper parameter
        // to generate "mammal" or "bird" rule.

        // Boolean query
        {
            // --------------------------------------
            System.out.println("3) boolean query (CONTENT): mammal or bird");

            BooleanQuery.Builder builder = new BooleanQuery.Builder();

            String normalizedMammal = analyzer.normalize("mammal", queryMammal).utf8ToString();
            Term term = new Term(Constants.content, normalizedMammal);
            tq1 = new TermQuery(term);
            BooleanClause clause = new BooleanClause(tq1, BooleanClause.Occur.SHOULD);

            String normalizedBird = analyzer.normalize("bird", queryBird).utf8ToString();
            term = new Term(Constants.content, normalizedBird);
            tq2 = new TermQuery(term);
            BooleanClause clause2 = new BooleanClause(tq2, BooleanClause.Occur.SHOULD);

            builder.add(clause);
            builder.add(clause2);

            builder.setMinimumNumberShouldMatch(1);

            BooleanQuery booleanQuery = builder.build();

            printResultsForQuery(indexSearcher, booleanQuery);
       
            // --------------------------------------
        } // DONE

        // TODO now, your task is to find all documents which size is smaller than 1000bytes.
        // For this reason, construct Range query.
        // Use IntPoint.newRangeQuery.
        {
            // --------------------------------------
            System.out.println("4) range query: file size in [0b, 1000b]");

            Query rangeQuery = IntPoint.newRangeQuery(Constants.filesize, 0, 1000);
            printResultsForQuery(indexSearcher, rangeQuery);
          
            // --------------------------------------
        } // DONE

        // TODO let's find all documents which name starts with "ant".
        // For this reason, construct PrefixQuery.
        {
            // --------------------------------------
            System.out.println("5) Prefix query (FILENAME): ant");

            Term term = new Term(Constants.filename, "ant");
            Query prefixQuery = new PrefixQuery(term);

            printResultsForQuery(indexSearcher, prefixQuery);
         
            // --------------------------------------
        } // DONE

        // TODO let's build a wildcard query".
        // Construct a WildcardQuery object. Look for documents
        // which contain a term "eat?" "?" stand for any letter (* for a sequence of letters).
        {
            // --------------------------------------
            System.out.println("6) Wildcard query (CONTENT): eat?");
            Term term = new Term(Constants.content, "eat?");
            Query wildcardQuery = new WildcardQuery(term);

            printResultsForQuery(indexSearcher, wildcardQuery);
    
            // --------------------------------------
        } // DONE

        // TODO build a fuzzy query for a word "mamml" (use FuzzyQuerry).
        // Find all documents that contain words which are similar to "mamml".
        // Which documents have been found?
        {
            // --------------------------------------
            System.out.println("7) Fuzzy querry (CONTENT): mamml?");

            Term term = new Term(Constants.content, "mamml");
            Query fuzzyQuery = new FuzzyQuery(term);

            printResultsForQuery(indexSearcher, fuzzyQuery);
     
            // --------------------------------------
        } // DONE

        // TODO now, use QueryParser to parse human-entered query strings
        // and generate query object.
        // - use AND, OR , NOT, (, ), + (must), and - (must not) to construct boolean queries
        // - use * and ? to contstruct wildcard queries
        // - use ~ to construct fuzzy (one word, similarity) or proximity (at least two words) queries
        // - use - to construct proximity queries
        // - use \ as an escape character for: + - && || ! ( ) { } [ ] ^ " ~ * ? : \
        // Consider following 5 examples of queries:
        String queryP1 = "MaMMal AND bat";
        String queryP2 = "ante*";
        String queryP3 = "brd~ ";
        String queryP4 = "(\"nocturnal life\"~10) OR bat";
        String queryP5 = "(\"nocturnal life\"~10) OR (\"are nocturnal\"~10)";
        // Select some query:
        String selectedQuery = queryP5;
        // Complete the code here, i.e., build query parser object, parse selected query
        // to query object, and find relevant documents. Analyze the outcomes.
        {
            // --------------------------------------
            System.out.println("8) query parser = " + selectedQuery);

            QueryParser parser = new QueryParser(Constants.content, analyzer);
            Query query = parser.parse(selectedQuery);

            printResultsForQuery(indexSearcher, query);
            // --------------------------------------
        }


        try
        {
            reader.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void printResultsForQuery(IndexSearcher indexSearcher, Query q) throws IOException {
        // TODO finish this method
        // - use indexSearcher to search for documents that
        // are relevant according to the query q
        // - Get TopDocs object (number of derived documents = Constant.top_docs)
        // - Iterate over ScoreDocs (in in TopDocs) and print for each document (in separate lines):
        // a) score
        // b) filename
        // c) id
        // d) file size
        // You may use indexSearcher to get a Document object for some docId (ScoreDoc)
        // and use document.get(name of the field) to get the value of id, filename, etc.

        // --------------------------------
        TopDocs docs = indexSearcher.search(q, Constants.top_docs);
        for (int i = 0; i < docs.scoreDocs.length; i++)
        {
            ScoreDoc currDoc = docs.scoreDocs[i];
            Document document = indexSearcher.doc(currDoc.doc);
            System.out.println(currDoc.score + ": " +
                    document.getField(Constants.filename) +
                    " (Id=" + document.getField(Constants.id) +
                    ") (Content=" + document.getField(Constants.content) +
                    ") (Size=" + document.getField(Constants.filesize) + ")");
        } // DONE
        // --------------------------------
    }

    private static IndexReader getIndexReader()
    {
        try
        {
            Directory dir = FSDirectory.open(Paths.get(Constants.index_dir));
            return DirectoryReader.open(dir);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
