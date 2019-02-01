package ru.otus.hw14.servlet;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Map;


public class TemplateProcessor {
    private static final String HTML_DIR = "tml";

    private final Configuration configuration;

    public TemplateProcessor() throws IOException {
        configuration = new Configuration(Configuration.VERSION_2_3_28);
        URL url = this.getClass().getClassLoader().getResource("/");
        System.out.println(url.getPath());
        File file = new File(HTML_DIR);
        System.out.println(file.getAbsolutePath());

        configuration.setDirectoryForTemplateLoading(new File(url.getPath()+HTML_DIR));
        configuration.setDefaultEncoding("UTF-8");
    }

    String getPage(String filename, Map<String, Object> data) throws IOException {
        try (Writer stream = new StringWriter();) {
            Template template = configuration.getTemplate(filename);
            template.process(data, stream);
            return stream.toString();
        } catch (TemplateException e) {
            throw new IOException(e);
        }
    }
}
