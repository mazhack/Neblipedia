package com.github.neblipedia.lucene;

/**
 * Created by juan on 6/09/16.
 */

import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class Reader {

    private final QueryParser queryMulti;

    public Reader() {
        String[] o = {"title", "text"};
        queryMulti = new MultiFieldQueryParser(o, new SpanishAnalyzer());
        // si esta en el titulo o en el texto
        queryMulti.setDefaultOperator(QueryParser.OR_OPERATOR);
    }

    /**
     * @param start page
     * @param total results by page
     * @param index
     * @param text  term to patternRouteSearch
     * @return
     */
    public JSONObject buscar(int start, int total, IndexSearcher index, String text) {
        try {
            Query query = queryMulti.parse(text);
            TopFieldDocs docs = index.search(query, start * total,
                    Sort.RELEVANCE);
            return buildResponse(docs, index, total);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject response = new JSONObject();
        response.put("total", 0);
        response.put("items", new JSONArray());
        return response;
    }

    public JSONObject buildResponse(TopDocs topDocs, IndexSearcher index, int total) {
        JSONObject response = new JSONObject();
        response.put("total", topDocs.totalHits);

        JSONArray items = new JSONArray();
        int c = 0;
        for (ScoreDoc s : topDocs.scoreDocs) {
            try {
                items.put(index.doc(s.doc).get("title"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            c++;
            if (c > total)
                break;
        }
        response.put("items", items);
        return response;
    }

//    public LinkedList<String> similar(String valor, IndexReader indice) {
//        try {
//            LinkedList<String> posible = new LinkedList<String>();
//            Query qe = queryMulti.parse(valor);
//            LinkedHashSet<Term> terms = new LinkedHashSet<Term>();
//            qe.extractTerms(terms);
//            for (Term t : terms) {
//
//                FuzzyTermEnum fuzzy = new FuzzyTermEnum(indice, t, 0.5f);
//                while (fuzzy.next()) {
//                    if (!posible.contains(fuzzy.term().text()))
//                        posible.add(fuzzy.term().text());
//                }
//            }
//            return posible;
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new LinkedList<String>();
//    }
}
