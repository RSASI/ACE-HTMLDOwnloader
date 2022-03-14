package com.valgen.atd.main;

public enum HtmlTagTypes {
    DIV,
    PRE,
    TD,
    BLOCKQUOTE,
    ADDRESS,
    OL,
    UL,
    DL,
    DD,
    DT,
    LI,
    FORM,
    H1,
    H2,
    H3,
    H4,
    H5,
    H6,
    TH,
    UNKNOWN;
    

    private HtmlTagTypes() {
    }

    public static HtmlTagTypes getTagName(String tag) {
        try {
            return HtmlTagTypes.valueOf(tag);
        }
        catch (Exception e) {
            return UNKNOWN;
        }
    }
}

