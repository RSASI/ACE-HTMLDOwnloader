package com.valgen.atd.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regexps {
    static String unlikelyCandidates = "(?i)(combx|comment|community|disqus|extra|foot|header|menu|remark|rss|shoutbox|sidebar|sponsor|ad-break|agegate|pagination|pager|popup|tweet|twitter)";
    static String okMaybeItsACandidate = "(?i)(and|article|body|column|main|shadow)";
    static String positive = "(?i)(article|body|content|entry|hentry|main|page|pagination|post|text|blog|story)";
    static String negative = "(?i)(combx|comment|com-|contact|foot|footer|footnote|masthead|media|meta|outbrain|promo|related|scroll|shoutbox|sidebar|sponsor|shopping|tags|tool|widget)";
    static String extraneous = "(?i)(print|archive|comment|discuss|e[\\-]?mail|share|reply|all|login|sign|single|Stumple It|Digg it|Retweet|Bebo)";
    static String divToPElements = "<(a|blockquote|dl|div|img|ol|p|pre|table|ul)";
    static String replaceBrs = "(?i)(<br[^>]*>[ \\n\\r\\t]*){2,}";
    static String replaceFonts = "(?i)<font[^>]*>";
    static String replacesFonts = "(?i)</font[^>]*>";
    static String trim = "(?i)^\\s+|\\s+$";
    static String normalize = "\\s{2,}";
    static String killBreaks = "(<br\\s*\\/?>(\\s|&nbsp;?)*){1,}";
    static String videos = "(?i)http:\\/\\/(www\\.)?(youtube|vimeo)\\.com";
    static String skipFootnoteLink = "(?i)^\\s*(\\[?[a-z0-9]{1,2}\\]?|^|edit|citation needed)\\s*$";
    static String nextLink = "(?i)(next|weiter|continue|>([^\\|]|$)|\ufffd([^\\|]|$))";
    static String prevLink = "(?i)(prev|earl|old|new|<|\ufffd)";
    static String social = "(?i)(Delicious|Digg|Digg it|Facebook|reddit|StumbleUpon|Stumple It|Bebo|Twitter|e[\\-]?mail|print)";
    static String singleBrs = "<br\\s*(/>|>)";

    public static boolean patternMatcher(String regex, String nodeHTML) {
        boolean matchStatus = false;
        Pattern pattern = Pattern.compile(regex, 194);
        Matcher regexMatcher = pattern.matcher(nodeHTML);
        matchStatus = regexMatcher.find();
        return matchStatus;
    }
}

