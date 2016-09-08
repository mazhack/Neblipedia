package com.github.neblipedia.generator;

import com.github.neblipedia.db.DbWriter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Created by juan on 4/09/16.
 */
public class XmlManager {

    public void load() {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {

            File wikis = new File("wikis");
            File[] list = wikis.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".xml");
                }
            });

            for (File file : list) {
                Long time = System.currentTimeMillis();

                File mapDb = new File(file.getParentFile(), file.getName().replace(".xml", ".mapDb"));
                if (mapDb.exists()) {
                    System.out.println("db exists, skip file: " + file.getName());
                    continue;
                }
                System.out.println("start process file: " + file.getName());
                DbWriter db = new DbWriter(mapDb);
                SAXParser parser = factory.newSAXParser();
                PageHandler pageHandler = new PageHandler(db);
                parser.parse(file, pageHandler);

                System.out.println("articles: " + db.countArticles());
                System.out.println("templates: " + db.countTemplates());
                System.out.println("categories: " + db.countCategories());
                System.out.println("end process file: " + file.getName());
                db.close();
                System.out.println("time: " + ((System.currentTimeMillis() - time) / 1000) + " seg");
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
