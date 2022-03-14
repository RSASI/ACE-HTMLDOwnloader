package com.valgen.atd.main;

import org.jsoup.nodes.Element;

public class NodeContentScore {
    Element parent;
    Double score;

    public NodeContentScore(Element parent, Double score) {
        this.parent = parent;
        this.score = score;
    }
}

