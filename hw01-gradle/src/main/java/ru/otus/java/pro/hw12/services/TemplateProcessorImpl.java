package ru.otus.java.pro.hw12.services;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class TemplateProcessorImpl implements TemplateProcessor {

    private final Configuration configuration;

    public TemplateProcessorImpl(String templatesDir) throws IOException {
        configuration = new Configuration(Configuration.VERSION_2_3_32);
        configuration.setClassForTemplateLoading(getClass(), templatesDir);
        configuration.setDefaultEncoding("UTF-8");
    }

    @Override
    public String getPage(String templateName, Map<String, Object> data) throws IOException {
        try (Writer page = new StringWriter()) {
            Template template = configuration.getTemplate(templateName);
            template.process(data, page);
            return page.toString();
        } catch (TemplateException exception) {
            throw new IOException(exception);
        }
    }
}
