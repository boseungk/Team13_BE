package com.theocean.fundering.global.utils;


import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

@Component
public class HTMLUtils {
    public String markdownToHTML(final String md) {
        final Parser parser = Parser.builder().build();
        final HtmlRenderer renderer = HtmlRenderer.builder().build();
        final Node content = parser.parse(md);
        return renderer.render(content);
    }

}
