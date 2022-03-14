package com.valgen.atd.main;

//import ArticleDownload_latest.HtmlTagTypes;
//import ArticleDownload_latest.NodeContentScore;
//import ArticleDownload_latest.Regexps;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.w3c.tidy.Tidy;

public class PrepareDocument {
    String bodyCache = null;
    Document document = null;
    boolean isPaging = false;
    Document pageCacheHtml = null;
    int[] flags = new int[]{1, 2, 4};
    int flagStrip = 1;
    int flagWeight = 2;
    int flagClean = 4;
    int FLAG_STRIP_UNLIKELYS = 1;
    int FLAG_WEIGHT_CLASSES = 2;
    int FLAG_CLEAN_CONDITIONALLY = 4;

    public Element createDOMTree(String htmlContent) throws Exception {
        Element finalArticle = null;
        this.fluchValues();
        this.document = Jsoup.parse((String)htmlContent);
        this.removeScripts(this.document);
        this.prepDocument(this.document);
        finalArticle = this.grabArticle(this.document);
        Element overlay = this.document.createElement("DIV");
        Element innerDiv = this.document.createElement("DIV");
        overlay.attr("id", "readOverlay");
        innerDiv.attr("id", "readInner");
        try {
            if (finalArticle != null) {
                innerDiv.appendChild((Node)finalArticle);
                overlay.appendChild((Node)innerDiv);
            } else {
                overlay.append("<DIV></DIV>");
            }
        }
        catch (Exception e) {
            // empty catch block
        }
        return overlay;
    }

    private void cleanBefore(Document doc, String tag) {
        Elements ele = doc.getElementsByTag(tag);
        for (Element e : ele) {
            e.remove();
        }
    }

    private void fluchValues() {
        this.bodyCache = null;
        this.document = null;
        this.isPaging = false;
        this.pageCacheHtml = null;
        this.FLAG_STRIP_UNLIKELYS = 1;
        this.FLAG_WEIGHT_CLASSES = 2;
        this.FLAG_CLEAN_CONDITIONALLY = 4;
    }

    public Element getArticleTitle(Document doc) {
        Elements hOnes;
        String curTitle = "";
        String origTitle = "";
        try {
            curTitle = origTitle = doc.title();
            if (curTitle.equals("")) {
                Elements titles = doc.getElementsByTag("title");
                for (Element title : titles) {
                    curTitle = origTitle = this.getTitleText(title);
                }
            }
        }
        catch (Exception e) {
            System.out.println("getArticle Title Exception");
            e.printStackTrace();
        }
        Pattern pattern = Pattern.compile("[|-]", 194);
        Matcher regexMatcher = pattern.matcher(curTitle);
        boolean curTtileMatch = regexMatcher.find();
        if (curTtileMatch) {
            curTitle = origTitle.replaceAll("(.*?)[|-].*", "$1");
            if (curTitle.split(" ").length < 3) {
                curTitle = origTitle.replaceAll("[^|-]*[|-](.*)", "$1");
            }
        } else if (curTitle.indexOf(": ") != -1) {
            curTitle = origTitle.replaceAll(".*:(.*)", "$1");
            if (curTitle.split(" ").length < 3) {
                curTitle = origTitle.replaceAll("[^:]*[:](.*)", "$1");
            }
        } else if ((curTitle.length() > 150 || curTitle.length() < 15) && (hOnes = doc.getElementsByTag("h1")).size() == 1) {
            curTitle = this.getTitleText(hOnes.get(0));
        }
        if ((curTitle = curTitle.replace(Regexps.trim, "")).split(" ").length <= 4) {
            curTitle = origTitle;
        }
        Element articleTitle = doc.createElement("H1");
        articleTitle.html(curTitle);
        return articleTitle;
    }

    private String getTitleText(Element title) {
        String titleText = "";
        System.out.println("Title :" + (Object)title);
        if (title.text() == null && title.text().equalsIgnoreCase("")) {
            System.out.println("No Text");
            return "";
        }
        titleText = title.text().replaceAll(Regexps.trim, "");
        titleText = titleText.replaceAll(Regexps.normalize, " ");
        return titleText;
    }

    public Element grabArticle(Document page) throws Exception {
        this.pageCacheHtml = page;
        Object pa = null;
        ArrayList<Element> scoringNodes = new ArrayList();
        scoringNodes = this.nodePrepping(page);
        ArrayList candtdateNodes = this.addNodeContentScore(scoringNodes);
        ArrayList topCandidate = this.findTopCandidateNode(candtdateNodes);
        Element finalArticleContent = this.topCandidateSibling(topCandidate);
        return finalArticleContent;
    }

    public ArrayList<Element> nodePrepping(Document page) throws Exception {
        ArrayList<Element> nodesToScore = new ArrayList<Element>();
        boolean stripUnlikelyCandidates = this.flagIsActive(this.FLAG_STRIP_UNLIKELYS, 0);
        Elements allElements = page.getAllElements();
        Element node = null;
        boolean removeEle = false;
        boolean elsePart = false;
        int nodeIndex = 0;
        int size = allElements.size();
        while (nodeIndex < allElements.size()) {
            node = allElements.get(nodeIndex);
            if (stripUnlikelyCandidates) {
                boolean unlikelyMatch;
                String unlikelyMatchString = node.className() + node.id();
                boolean bl = unlikelyMatch = Regexps.patternMatcher(Regexps.unlikelyCandidates, unlikelyMatchString) && !Regexps.patternMatcher(Regexps.okMaybeItsACandidate, unlikelyMatchString) && !node.tagName().equalsIgnoreCase("body");
                if (unlikelyMatch) {
                    removeEle = true;
                    allElements.remove((Object)node);
                    continue;
                }
            }
            if (node != null) {
                if (node.tagName().equalsIgnoreCase("P") || node.tagName().equalsIgnoreCase("TD") || node.tagName().equalsIgnoreCase("PRE")) {
                    nodesToScore.add(node);
                }
                if (node.tagName().equalsIgnoreCase("DIV")) {
                    String nodeHTML = "";
                    try {
                        nodeHTML = node.html();
                    }
                    catch (Exception ex) {
                        nodeHTML = null;
                    }
                    if (nodeHTML != null) {
                        boolean isDivToP = Regexps.patternMatcher(Regexps.divToPElements, nodeHTML);
                        if (!isDivToP) {
                            Element newNode = page.createElement("p");
                            try {
                                newNode.html(node.html());
                                allElements.set(nodeIndex, newNode);
                                nodesToScore.add(allElements.get(nodeIndex));
                            }
                            catch (Exception ex) {
                                System.out.println("Could not alter div to p, probably an IE restriction, reverting back to div.: ");
                                ex.printStackTrace();
                            }
                        } else {
                            int il = node.children().size();
                            for (int i = 0; i < il; ++i) {
                                Node childNode = node.childNode(i);
                                if (childNode == null || !(childNode instanceof TextNode)) continue;
                                Element p = page.createElement("p");
                                p.html(((TextNode)childNode).getWholeText());
                                p.attr("style", "inline");
                                p.attr("class", "readability-styled");
                                childNode.replaceWith((Node)p);
                            }
                        }
                    }
                }
            }
            ++nodeIndex;
        }
        return nodesToScore;
    }

    public ArrayList addNodeContentScore(ArrayList<Element> nodesToScore) {
        ArrayList<NodeContentScore> candidates = new ArrayList<NodeContentScore>();
        ArrayList parentScore = new ArrayList();
        ArrayList grandParentScore = new ArrayList();
        Object pN = null;
        for (int pt = 0; pt < nodesToScore.size(); ++pt) {
            int i;
            NodeContentScore nodeContentScore;
            Element element = nodesToScore.get(pt);
            Element parentNode = element.parent();
            Element grandParentNode = null;
            grandParentNode = parentNode != null ? parentNode.parent() : null;
            String innerText = this.getInnerText(element, true);
            if (parentNode == null || innerText.length() < 25) continue;
            if (parentNode != null) {
                parentScore = this.initializeNode(parentNode);
            }
            if (grandParentNode != null) {
                grandParentScore = this.initializeNode(grandParentNode);
            }
            int contentScore = 0;
            ++contentScore;
            contentScore += innerText.split(",").length;
            contentScore = (int)((double)contentScore + Math.min(Math.floor(innerText.length() / 100), 3.0));
            for (i = 0; i < parentScore.size(); ++i) {
                NodeContentScore nodeP;
                nodeContentScore = nodeP = (NodeContentScore)parentScore.get(i);
                nodeContentScore.score = nodeContentScore.score + (double)contentScore;
                parentScore.set(i, nodeP);
                candidates.add(nodeP);
            }
            if (grandParentNode == null) continue;
            for (i = 0; i < grandParentScore.size(); ++i) {
                NodeContentScore nodeGP;
                nodeContentScore = nodeGP = (NodeContentScore)grandParentScore.get(i);
                nodeContentScore.score = nodeContentScore.score + (double)contentScore;
                nodeGP.score = nodeGP.score / 2.0;
                grandParentScore.set(i, nodeGP);
                candidates.add(nodeGP);
            }
        }
        return candidates;
    }

    public ArrayList findTopCandidateNode(ArrayList candidates) {
        ArrayList topCandidateList = new ArrayList();
        int cl = candidates.size();
        ArrayList tempEqualCandidates = new ArrayList();
        Object tempTopCandidateNode = null;
        Double tempTopCandidateScore = 0.0;
        ArrayList<NodeContentScore> candidatesScoreFinal = new ArrayList<NodeContentScore>();
        for (int c = 0; c < cl; ++c) {
            NodeContentScore candidateScore = (NodeContentScore)candidates.get(c);
            Double linkDensity = this.getLinkDensity(candidateScore.parent);
            candidateScore.score = candidateScore.score * (1.0 - linkDensity);
            candidatesScoreFinal.add(candidateScore);
        }
        for (int i = 0; i < candidatesScoreFinal.size(); ++i) {
            NodeContentScore candidateScoreAdded = (NodeContentScore)candidatesScoreFinal.get(i);
            int il = Double.compare(candidateScoreAdded.score, tempTopCandidateScore);
            if (il > 0) {
                tempTopCandidateScore = candidateScoreAdded.score;
                topCandidateList.clear();
                topCandidateList.add((NodeContentScore)candidateScoreAdded);
                continue;
            }
            if (il != 0) continue;
            NodeContentScore topEqualCandidate = null;
            try {
                if (topCandidateList.size() <= 0) continue;
                for (int t = 0; t < topCandidateList.size(); ++t) {
                    topEqualCandidate = (NodeContentScore)topCandidateList.get(t);
                }
                if (this.getInnerText(candidateScoreAdded.parent, true).length() <= this.getInnerText(topEqualCandidate.parent, true).length()) continue;
                topCandidateList.clear();
                topCandidateList.add(candidateScoreAdded);
                tempTopCandidateScore = candidateScoreAdded.score;
                continue;
            }
            catch (Exception e) {
                // empty catch block
            }
        }
        try {
            NodeContentScore topNode = (NodeContentScore)topCandidateList.get(0);
            if (topNode == null || topNode.parent.tagName().equalsIgnoreCase("BODY")) {
                Element topCandidate = this.document.createElement("DIV");
                topCandidate.html(this.document.html());
                this.document.html("");
                this.document.appendChild((Node)topCandidate);
                topCandidateList = this.initializeNode(topCandidate);
            }
        }
        catch (Exception e) {
            Element topCandidate = this.document.createElement("DIV");
            topCandidate.html(this.document.html());
            this.document.html("");
            this.document.appendChild((Node)topCandidate);
            topCandidateList = this.initializeNode(topCandidate);
        }
        return topCandidateList;
    }

    public Element topCandidateSibling(ArrayList topCandidateNode) {
        NodeContentScore topCandidateFinal = null;
        for (int i = 0; i < topCandidateNode.size(); ++i) {
            topCandidateFinal = (NodeContentScore)topCandidateNode.get(i);
        }
        Element articleContent = this.document.createElement("DIV");
        articleContent.attr("id", "readability-content");
        double siblingScoreThreshold = Math.max(10.0, topCandidateFinal.score * 0.2);
        Elements siblingNodes = null;
        try {
            siblingNodes = topCandidateFinal.parent.parent().children();
        }
        catch (Exception e) {
            articleContent = topCandidateFinal.parent;
        }
        if (siblingNodes != null) {
            int siblingLength = siblingNodes.size();
            for (int sib = 0; sib < siblingLength; ++sib) {
                Element siblingNode = siblingNodes.get(sib);
                ArrayList sibling = this.initializeNode(siblingNode);
                NodeContentScore siblingScore = (NodeContentScore)sibling.get(0);
                boolean append = false;
                if (siblingNode == null) continue;
                if (siblingNode.equals((Object)topCandidateFinal.parent)) {
                    append = true;
                }
                Double contentBonus = 0.0;
                if (siblingNode.className().equals(topCandidateFinal.parent.className()) && topCandidateFinal.parent.className() != "") {
                    contentBonus = contentBonus + topCandidateFinal.score * 0.2;
                }
                if (!siblingNode.className().equalsIgnoreCase("") && siblingScore.score + contentBonus >= siblingScoreThreshold) {
                    append = true;
                }
                if (siblingNode.nodeName().equalsIgnoreCase("P")) {
                    double linkDensity = this.getLinkDensity(siblingNode);
                    String nodeContent = this.getInnerText(siblingNode, true);
                    int nodeLength = nodeContent.length();
                    boolean nodeContentMatch = Regexps.patternMatcher("/\\.( |$)/", nodeContent);
                    if (nodeLength > 80 && linkDensity < 0.25) {
                        append = true;
                    } else if (nodeLength < 80 && linkDensity == 0.0 && nodeContentMatch) {
                        append = true;
                    }
                }
                if (!append) continue;
                Element nodeToAppend = null;
                if (siblingNode.nodeName() != "DIV" && siblingNode.nodeName() != "P") {
                    nodeToAppend = this.document.createElement("DIV");
                    try {
                        nodeToAppend.attr("id", siblingNode.id());
                        nodeToAppend.html(siblingNode.html());
                    }
                    catch (Exception er) {
                        nodeToAppend.append(siblingNode.html());
                        --sib;
                        --siblingLength;
                    }
                } else {
                    nodeToAppend.append(siblingNode.html());
                    --sib;
                    --siblingLength;
                }
                articleContent.appendChild((Node)nodeToAppend);
            }
        } else {
            articleContent = topCandidateFinal.parent;
        }
        this.prepareArticle(articleContent);
        String articleLength = this.getInnerText(articleContent, false);
        if (articleLength.length() < 250) {
            if (this.flagIsActive(this.FLAG_STRIP_UNLIKELYS, 0)) {
                this.removeFlags(this.FLAG_STRIP_UNLIKELYS, 0);
                try {
                    articleContent = this.grabArticle(this.pageCacheHtml);
                }
                catch (Exception e) {}
            } else if (this.flagIsActive(this.FLAG_WEIGHT_CLASSES, 1)) {
                this.removeFlags(this.FLAG_WEIGHT_CLASSES, 1);
                try {
                    articleContent = this.grabArticle(this.pageCacheHtml);
                }
                catch (Exception e) {}
            } else if (this.flagIsActive(this.FLAG_CLEAN_CONDITIONALLY, 2)) {
                this.removeFlags(this.FLAG_CLEAN_CONDITIONALLY, 2);
                try {
                    articleContent = this.grabArticle(this.pageCacheHtml);
                }
                catch (Exception e) {}
            } else {
                return null;
            }
        }
        return articleContent;
    }

    public void prepareArticle(Element articleContent) {
        this.cleanStyles(articleContent);
        this.killBreaks(articleContent);
        this.cleanConditionally(articleContent, "form");
        this.clean(articleContent, "object");
        this.clean(articleContent, "h1");
        if (articleContent.getElementsByTag("h2").size() == 1) {
            this.clean(articleContent, "h2");
        }
        this.clean(articleContent, "iframe");
        this.cleanHeaders(articleContent);
        this.cleanConditionally(articleContent, "table");
        this.cleanConditionally(articleContent, "ul");
        this.cleanConditionally(articleContent, "div");
        Elements articleParagraphs = articleContent.getElementsByTag("p");
        for (int i = articleParagraphs.size() - 1; i >= 0; --i) {
            int imgCount = articleParagraphs.get(i).getElementsByTag("img").size();
            int embedCount = articleParagraphs.get(i).getElementsByTag("embed").size();
            int objectCount = articleParagraphs.get(i).getElementsByTag("object").size();
            if (imgCount != 0 || embedCount != 0 || objectCount != 0 || !this.getInnerText(articleParagraphs.get(i), false).equalsIgnoreCase("")) continue;
            articleParagraphs.get(i).remove();
        }
        this.findUlLinks(articleContent, "ul");
        try {
            articleContent.html(articleContent.html().replaceAll("(?i)<br[^>]*>\\s*<p", "<p"));
        }
        catch (Exception e) {
            System.out.println("Final Replacement Exception");
            e.printStackTrace();
        }
    }

    public void cleanHeaders(Element content) {
        for (int headerIndex = 1; headerIndex <= 3; ++headerIndex) {
            Elements headers = content.getElementsByTag("h" + headerIndex);
            for (int i = headers.size() - 1; i >= 0; --i) {
                if (this.getClassWeight(headers.get(i)) >= 0 && this.getLinkDensity(headers.get(i)) <= 0.33) continue;
                headers.get(i).remove();
            }
        }
    }

    public void cleanStyles(Element content) {
        Elements current = content.getAllElements();
        int i = 0;
        try {
            while (i < current.size()) {
                Element element = current.get(i);
                if (element.children().size() > 0) {
                    ++i;
                    continue;
                }
                if (element != null && element instanceof Element && !element.className().equalsIgnoreCase("readability-styled")) {
                    element.removeAttr("style");
                }
                ++i;
            }
        }
        catch (Exception e) {
            System.out.println("Clean Styles exception");
            e.printStackTrace();
        }
    }

    public void killBreaks(Element content) {
        try {
            content.html(content.html().replaceAll(Regexps.killBreaks, "<br />"));
        }
        catch (Exception e) {
            System.out.println("KillBreaks failed - this is an IE bug. Ignoring.: ");
            e.printStackTrace();
        }
    }

    public void cleanConditionally(Element content, String tag) {
        if (!this.flagIsActive(this.FLAG_CLEAN_CONDITIONALLY, 2)) {
            return;
        }
        Elements tagsList = content.getElementsByTag(tag);
        int curTagsLength = tagsList.size();
        for (int i = 0; i < curTagsLength - 1; ++i) {
            int weight = this.getClassWeight(tagsList.get(i));
            int count = this.getCharCount(tagsList.get(i));
            if (count >= 10) continue;
            try {
                int p = tagsList.get(i).getElementsByTag("p").size();
                int img = tagsList.get(i).getElementsByTag("img").size();
                int li = tagsList.get(i).getElementsByTag("li").size() - 100;
                int input = tagsList.get(i).getElementsByTag("input").size();
                int embedCount = 0;
                Elements embeds = tagsList.get(i).getElementsByTag("embed");
                int il = embeds.size();
                for (int ei = 0; ei < il; ++ei) {
                    boolean embedMatch = Regexps.patternMatcher(Regexps.videos, embeds.get(ei).attr("src"));
                    System.out.println("Embed Match");
                    if (embedMatch) continue;
                    ++embedCount;
                }
                Double linkDensity = this.getLinkDensity(tagsList.get(i));
                int contentLength = this.getInnerText(tagsList.get(i), true).length();
                boolean toRemove = false;
                if (img > p) {
                    toRemove = true;
                } else if (li > p && !tag.equalsIgnoreCase("ul") && !tag.equalsIgnoreCase("ol")) {
                    toRemove = true;
                } else if ((double)input > Math.floor(p / 3)) {
                    toRemove = true;
                } else if (contentLength < 25 && (img == 0 || img > 2)) {
                    toRemove = true;
                } else if (weight < 25 && linkDensity > 0.2) {
                    toRemove = true;
                } else if (weight >= 25 && linkDensity > 0.5) {
                    toRemove = true;
                } else if (embedCount == 1 && contentLength < 75 || embedCount > 1) {
                    toRemove = true;
                }
                if (!toRemove) continue;
                tagsList.get(i).remove();
                continue;
            }
            catch (Exception e) {
                // empty catch block
            }
        }
    }

    public void findUlLinks(Element content, String tag) {
        Elements ulTags = content.getElementsByTag(tag);
        for (int ul = 0; ul < ulTags.size(); ++ul) {
            boolean ulMatch = false;
            Elements innerLink = ulTags.get(ul).getElementsByTag("a");
            for (int i = 0; i < innerLink.size(); ++i) {
                String linkText = this.getInnerText(innerLink.get(i), true);
                String className = innerLink.get(i).className() + innerLink.get(i).id();
                boolean extrMatch = Regexps.patternMatcher(Regexps.extraneous, linkText);
                boolean socialMatch = Regexps.patternMatcher(Regexps.social, linkText);
                if (linkText.length() < 25 && (extrMatch || socialMatch)) {
                    ulMatch = true;
                }
                try {
                    if (!ulMatch || innerLink.get(i).parent() == null) continue;
                    innerLink.get(i).parent().remove();
                    continue;
                }
                catch (Exception ex) {
                    // empty catch block
                }
            }
        }
    }

    public void clean(Element content, String tag) {
        Elements targetList = content.getElementsByTag(tag);
        for (int y = 0; y <= targetList.size() - 1; ++y) {
            targetList.get(y).remove();
        }
    }

    public int getCharCount(Element content) {
        int count = this.getInnerText(content, true).split(",").length - 1;
        return count;
    }

    public void findPageLink(Element articleContent) {
        Elements allLinks = articleContent.getElementsByTag("a");
        int il = allLinks.size();
        for (int i = 0; i < il; ++i) {
            Element link = allLinks.get(i);
            String linkText = this.getInnerText(link, true);
            boolean extrMatch = Regexps.patternMatcher(Regexps.extraneous, linkText);
            if (!extrMatch && linkText.length() <= 25) continue;
        }
    }

    public double getLinkDensity(Element linkNode) {
        Elements links = linkNode.getElementsByTag("a");
        int textLength = this.getInnerText(linkNode, true).length();
        Double linkLength = 0.0;
        if (textLength == 0) {
            textLength = 1;
        }
        int il = links.size();
        for (int i = 0; i < il; ++i) {
            linkLength = linkLength + (double)this.getInnerText(links.get(i), true).length();
        }
        Double result = linkLength / (double)textLength;
        return result;
    }

    public int getClassWeight(Element node) {
        if (!this.flagIsActive(this.FLAG_WEIGHT_CLASSES, 1)) {
            return 0;
        }
        int weight = 0;
        if (!node.className().equalsIgnoreCase("")) {
            boolean negativeMatch = Regexps.patternMatcher(Regexps.negative, node.className());
            boolean positiveMatch = Regexps.patternMatcher(Regexps.positive, node.className());
            if (negativeMatch) {
                weight -= 25;
            }
            if (positiveMatch) {
                weight += 25;
            }
        } else if (!node.id().equalsIgnoreCase("")) {
            boolean negativeId = Regexps.patternMatcher(Regexps.negative, node.id());
            boolean positiveId = Regexps.patternMatcher(Regexps.positive, node.id());
            if (negativeId) {
                weight -= 25;
            }
            if (positiveId) {
                weight += 25;
            }
        }
        return weight;
    }

    public ArrayList initializeNode(Element parent) {
        String parentTagName = parent.tagName().toUpperCase();
        HtmlTagTypes parentTag = HtmlTagTypes.getTagName(parentTagName);
        Double contentScore = 0.0;
        ArrayList<NodeContentScore> contentNodeScore = new ArrayList<NodeContentScore>();
        switch (parentTag) {
            case DIV: {
                contentScore = contentScore + 5.0;
                break;
            }
            case PRE: 
            case TD: 
            case BLOCKQUOTE: {
                contentScore = contentScore + 3.0;
                break;
            }
            case ADDRESS: 
            case OL: 
            case UL: 
            case DL: 
            case DD: 
            case DT: 
            case LI: 
            case FORM: {
                contentScore = contentScore - 3.0;
                break;
            }
            case H1: 
            case H2: 
            case H3: 
            case H4: 
            case H5: 
            case H6: 
            case TH: {
                contentScore = contentScore - 5.0;
                break;
            }
        }
        int weigh = 0;
        weigh = this.getClassWeight(parent);
        contentScore = contentScore + (double)weigh;
        NodeContentScore nodeScore = new NodeContentScore(parent, contentScore);
        contentNodeScore.add(nodeScore);
        return contentNodeScore;
    }

    public String getInnerText(Element element, boolean normalizeSpaces) {
        String textContent = "";
        if (element.text().equalsIgnoreCase("")) {
            return "";
        }
        textContent = element.text().replaceAll(Regexps.trim, "");
        if (normalizeSpaces) {
            textContent = textContent.replaceAll(Regexps.normalize, " ");
        }
        return textContent;
    }

    public void removeScripts(Document doc) {
        Elements scripts = doc.getElementsByTag("script");
        for (int i = scripts.size() - 1; i >= 0; --i) {
            Element element = scripts.get(i);
            element.remove();
        }
    }

    public void prepDocument(Document doc) {
        if (doc.getElementsByTag("body") == null) {
            Element body = doc.createElement("body");
            try {
                doc.appendChild((Node)body);
            }
            catch (Exception e) {
                doc.appendChild((Node)body);
            }
            doc.getElementsByTag("body").attr("id", "readabilityBody");
        }
        Elements styleTags = doc.getElementsByTag("style");
        for (int st = 0; st < styleTags.size(); ++st) {
            styleTags.get(st).text("");
        }
        doc.body().html(doc.body().html().replaceAll(Regexps.replaceBrs, "</p><p>").replaceAll(Regexps.replaceFonts, "<span>").replaceAll(Regexps.replacesFonts, "</span>"));
        doc.body().html(doc.body().html().replaceAll(Regexps.singleBrs, "</p><p>"));
    }

    private String GetTidyPage(String webPage) throws Exception {
        Tidy tidy = new Tidy();
        tidy.setQuiet(true);
        tidy.setShowWarnings(false);
        tidy.setShowErrors(0);
        tidy.setMakeClean(true);
        tidy.setMakeBare(false);
        tidy.setFixUri(true);
        tidy.setTrimEmptyElements(true);
        tidy.setWrapScriptlets(true);
        tidy.setWrapPhp(true);
        StringReader strIn = new StringReader(webPage);
        StringWriter strOut = new StringWriter();
        tidy.parse((Reader)strIn, (Writer)strOut);
        strIn.close();
        strOut.close();
        String tidyWebPage = strOut.toString();
        if (tidyWebPage == null || tidyWebPage.length() == 0) {
            return webPage;
        }
        return tidyWebPage;
    }

    private boolean flagIsActive(int flag, int count) {
        int bool = this.flags[count] & flag;
        return bool > 0;
    }

    private void removeFlags(int flag, int count) {
        this.flags[count] = this.flags[count] & ~ flag;
    }

    public static double round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, 0);
        return bd.doubleValue();
    }

}

