package com.valgen.atd.main;

public class Cleaner {
    public static String dataclean(String desc) {
        desc = desc.replaceAll("(?s)(?i)<aside[^<>]*>.*?</aside>", "");
        desc = desc.replaceAll("(?s)(?i)<noscript[^<>]*>.*?</noscript>", "");
        desc = desc.replaceAll("(?sim)<script[^<>]*>.*?</script>", "").replaceAll("(?sim)<form[^<>]*>.*?</form>", "");
        desc = desc.replaceAll("(?sim)<style[^<>]*>.*?</style>", "").replaceAll("(?sim)<[^<>]*clear[-\\s]*fix[^<>]*>.*?<[^<>]*>", "");
        desc = desc.replaceAll("(?sim)<noscript[^<>]*>.*?</noscript>", "").replaceAll("<span[^<>]*pager[^<>]*>.*?</span>", "");
        desc = desc.replaceAll("(?sim)<a[^<>]*>.*?</a>", "").replaceAll("<(meta|link)[^<>]*>", "");
        desc = desc.replaceAll("(?sim)<!.*?-->", "").replaceAll("(?sim)(<br>\\s*){2,}", "");
        desc = desc.replaceAll("(?s)(?i)<script[^<>]*>.*?</script>", "");
        desc = desc.replaceAll("(?s)(?i)\\r", "<br>");
        desc = desc.replaceAll("(?s)(?i)\\n", "<br>");
        desc = desc.replaceAll("\\s+", " ");
        desc = desc.replaceAll("&#160;|&nbsp;", "\u00a0");
        desc = desc.replaceAll("&#161;|&iexcl;", "\u00a1");
        desc = desc.replaceAll("&#162;|&cent;", "\u00a2");
        desc = desc.replaceAll("&#163;|&pound;", "\u00a3");
        desc = desc.replaceAll("&#164;|&curren;", "\u00a4");
        desc = desc.replaceAll("&#165;|&yen;", "\u00a5");
        desc = desc.replaceAll("&#166;|&brvbar;", "\u00a6");
        desc = desc.replaceAll("&#167;|&sect;", "\u00a7");
        desc = desc.replaceAll("&#168;|&uml;", "\u00a8");
        desc = desc.replaceAll("&#169;|&copy;", "\u00a9");
        desc = desc.replaceAll("&#170;|&ordf;", "\u00aa");
        desc = desc.replaceAll("&#171;|&laquo;", "\u00ab");
        desc = desc.replaceAll("&#172;|&not;", "\u00ac");
        desc = desc.replaceAll("&#173;|&shy;", "\u00ad");
        desc = desc.replaceAll("&#174;|&reg;", "\u00ae");
        desc = desc.replaceAll("&#175;|&macr;", "\u00af");
        desc = desc.replaceAll("&#176;|&deg;", "\u00b0");
        desc = desc.replaceAll("&#177;|&plusmn;", "\u00b1");
        desc = desc.replaceAll("&#178;|&sup2;", "\u00b2");
        desc = desc.replaceAll("&#179;|&sup3;", "\u00b3");
        desc = desc.replaceAll("&#180;|&acute;", "\u00b4");
        desc = desc.replaceAll("&#181;|&micro;", "\u00b5");
        desc = desc.replaceAll("&#182;|&para;", "\u00b6");
        desc = desc.replaceAll("&#183;|&middot;", "\u00b7");
        desc = desc.replaceAll("&#184;|&cedil;", "\u00b8");
        desc = desc.replaceAll("&#185;|&sup1;", "\u00b9");
        desc = desc.replaceAll("&#186;|&ordm;", "\u00ba");
        desc = desc.replaceAll("&#187;|&raquo;", "\u00bb");
        desc = desc.replaceAll("&#188;|&frac14;", "\u00bc");
        desc = desc.replaceAll("&#189;|&frac12;", "\u00bd");
        desc = desc.replaceAll("&#190;|&frac34;", "\u00be");
        desc = desc.replaceAll("&#191;|&iquest;", "\u00bf");
        desc = desc.replaceAll("&#192;|&Agrave;", "\u00c0");
        desc = desc.replaceAll("&#193;|&Aacute;", "\u00c1");
        desc = desc.replaceAll("&#194;|&Acirc;", "\u00c2");
        desc = desc.replaceAll("&#195;|&Atilde;", "\u00c3");
        desc = desc.replaceAll("&#196;|&Auml;", "\u00c4");
        desc = desc.replaceAll("&#197;|&Aring;", "\u00c5");
        desc = desc.replaceAll("&#198;|&AElig;", "\u00c6");
        desc = desc.replaceAll("&#199;|&Ccedil;", "\u00c7");
        desc = desc.replaceAll("&#200;|&Egrave;", "\u00c8");
        desc = desc.replaceAll("&#201;|&Eacute;", "\u00c9");
        desc = desc.replaceAll("&#202;|&Ecirc;", "\u00ca");
        desc = desc.replaceAll("&#203;|&Euml;", "\u00cb");
        desc = desc.replaceAll("&#204;|&Igrave;", "\u00cc");
        desc = desc.replaceAll("&#205;|&Iacute;", "\u00cd");
        desc = desc.replaceAll("&#206;|&Icirc;", "\u00ce");
        desc = desc.replaceAll("&#207;|&Iuml;", "\u00cf");
        desc = desc.replaceAll("&#208;|&ETH;", "\u00d0");
        desc = desc.replaceAll("&#209;|&Ntilde;", "\u00d1");
        desc = desc.replaceAll("&#210;|&Ograve;", "\u00d2");
        desc = desc.replaceAll("&#211;|&Oacute;", "\u00d3");
        desc = desc.replaceAll("&#212;|&Ocirc;", "\u00d4");
        desc = desc.replaceAll("&#213;|&Otilde;", "\u00d5");
        desc = desc.replaceAll("&#214;|&Ouml;", "\u00d6");
        desc = desc.replaceAll("&#215;|&times;", "\u00d7");
        desc = desc.replaceAll("&#216;|&Oslash;", "\u00d8");
        desc = desc.replaceAll("&#217;|&Ugrave;", "\u00d9");
        desc = desc.replaceAll("&#218;|&Uacute;", "\u00da");
        desc = desc.replaceAll("&#219;|&Ucirc;", "\u00db");
        desc = desc.replaceAll("&#220;|&Uuml;", "\u00dc");
        desc = desc.replaceAll("&#221;|&Yacute;", "\u00dd");
        desc = desc.replaceAll("&#222;|&THORN;", "\u00de");
        desc = desc.replaceAll("&#223;|&szlig;", "\u00df");
        desc = desc.replaceAll("&#224;|&agrave;", "\u00e0");
        desc = desc.replaceAll("&#225;|&aacute;", "\u00e1");
        desc = desc.replaceAll("&#226;|&acirc;", "\u00e2");
        desc = desc.replaceAll("&#227;|&atilde;", "\u00e3");
        desc = desc.replaceAll("&#228;|&auml;", "\u00e4");
        desc = desc.replaceAll("&#229;|&aring;", "\u00e5");
        desc = desc.replaceAll("&#230;|&aelig;", "\u00e6");
        desc = desc.replaceAll("&#231;|&ccedil;", "\u00e7");
        desc = desc.replaceAll("&#232;|&egrave;", "\u00e8");
        desc = desc.replaceAll("&#233;|&eacute;", "\u00e9");
        desc = desc.replaceAll("&#234;|&ecirc;", "\u00ea");
        desc = desc.replaceAll("&#235;|&euml;", "\u00eb");
        desc = desc.replaceAll("&#236;|&igrave;", "\u00ec");
        desc = desc.replaceAll("&#237;|&iacute;", "\u00ed");
        desc = desc.replaceAll("&#238;|&icirc;", "\u00ee");
        desc = desc.replaceAll("&#239;|&iuml;", "\u00ef");
        desc = desc.replaceAll("&#240;|&eth;", "\u00f0");
        desc = desc.replaceAll("&#241;|&ntilde;", "\u00f1");
        desc = desc.replaceAll("&#242;|&ograve;", "\u00f2");
        desc = desc.replaceAll("&#243;|&oacute;", "\u00f3");
        desc = desc.replaceAll("&#244;|&ocirc;", "\u00f4");
        desc = desc.replaceAll("&#245;|&otilde;", "\u00f5");
        desc = desc.replaceAll("&#246;|&ouml;", "\u00f6");
        desc = desc.replaceAll("&#247;|&divide;", "\u00f7");
        desc = desc.replaceAll("&#248;|&oslash;", "\u00f8");
        desc = desc.replaceAll("&#249;|&ugrave;", "\u00f9");
        desc = desc.replaceAll("&#250;|&uacute;", "\u00fa");
        desc = desc.replaceAll("&#251;|&ucirc;", "\u00fb");
        desc = desc.replaceAll("&#252;|&uuml;", "\u00fc");
        desc = desc.replaceAll("&#253;|&yacute;", "\u00fd");
        desc = desc.replaceAll("&#254;|&thorn;", "\u00fe");
        desc = desc.replaceAll("&#255;|&yuml;", "\u00ff");
        desc = desc.replaceAll("&#034;|&#34;|&quot;", "\"");
        desc = desc.replaceAll("&#38;|&amp;", "&");
        desc = desc.replaceAll("&#60;|&lt;", "<");
        desc = desc.replaceAll("&#62;|&gt;", ">");
        desc = desc.replaceAll("&#39;|&#039;|&apos;|&#039", "'");
        desc = desc.replaceAll("&#338;|&OElig;", "\u0152");
        desc = desc.replaceAll("&#339;|&oelig;", "\u0153");
        desc = desc.replaceAll("&#352;|&Scaron;", "\u0160");
        desc = desc.replaceAll("&#353;|&scaron;", "\u0161");
        desc = desc.replaceAll("&#376;|&Yuml;", "\u0178");
        desc = desc.replaceAll("&#710;|&circ;", "\u02c6");
        desc = desc.replaceAll("&#732;|&tilde;", "\u02dc");
        desc = desc.replaceAll("&#8194;|&ensp;", "\u2002");
        desc = desc.replaceAll("&#8195;|&emsp;", "\u2003");
        desc = desc.replaceAll("&#8201;|&thinsp;", "\u2009");
        desc = desc.replaceAll("&#8204;|&zwnj;", "\u200c");
        desc = desc.replaceAll("&#8205;|&zwj;", "\u200d");
        desc = desc.replaceAll("&#8206;|&lrm;", "\u200e");
        desc = desc.replaceAll("&#8207;|&rlm;", "\u200f");
        desc = desc.replaceAll("&#8211;|&ndash;", "\u2013");
        desc = desc.replaceAll("&#8212;|&mdash;", "\u2014");
        desc = desc.replaceAll("&#8216;|&lsquo;", "\u2018");
        desc = desc.replaceAll("&#8217;|&rsquo;", "\u2019");
        desc = desc.replaceAll("&#8218;|&sbquo;", "\u201a");
        desc = desc.replaceAll("&#8220;|&ldquo;", "\u201c");
        desc = desc.replaceAll("&#8221;|&rdquo;", "\u201d");
        desc = desc.replaceAll("&#8222;|&bdquo;", "\u201e");
        desc = desc.replaceAll("&#8224;|&dagger;", "\u2020");
        desc = desc.replaceAll("&#8225;|&Dagger;", "\u2021");
        desc = desc.replaceAll("&#8240;|&permil;", "\u2030");
        desc = desc.replaceAll("&#8249;|&lsaquo;", "\u2039");
        desc = desc.replaceAll("&#8250;|&rsaquo;", "\u203a");
        desc = desc.replaceAll("&#8364;|&euro;", "\u20ac");
        desc = desc.replaceAll("&#402;|&fnof;", "\u0192");
        desc = desc.replaceAll("&#913;|&Alpha;", "\u0391");
        desc = desc.replaceAll("&#914;|&Beta;", "\u0392");
        desc = desc.replaceAll("&#915;|&Gamma;", "\u0393");
        desc = desc.replaceAll("&#916;|&Delta;", "\u0394");
        desc = desc.replaceAll("&#917;|&Epsilon;", "\u0395");
        desc = desc.replaceAll("&#918;|&Zeta;", "\u0396");
        desc = desc.replaceAll("&#919;|&Eta;", "\u0397");
        desc = desc.replaceAll("&#920;|&Theta;", "\u0398");
        desc = desc.replaceAll("&#921;|&Iota;", "\u0399");
        desc = desc.replaceAll("&#922;|&Kappa;", "\u039a");
        desc = desc.replaceAll("&#923;|&Lambda;", "\u039b");
        desc = desc.replaceAll("&#924;|&Mu;", "\u039c");
        desc = desc.replaceAll("&#925;|&Nu;", "\u039d");
        desc = desc.replaceAll("&#926;|&Xi;", "\u039e");
        desc = desc.replaceAll("&#927;|&Omicron;", "\u039f");
        desc = desc.replaceAll("&#928;|&Pi;", "\u03a0");
        desc = desc.replaceAll("&#929;|&Rho;", "\u03a1");
        desc = desc.replaceAll("&#931;|&Sigma;", "\u03a3");
        desc = desc.replaceAll("&#932;|&Tau;", "\u03a4");
        desc = desc.replaceAll("&#933;|&Upsilon;", "\u03a5");
        desc = desc.replaceAll("&#934;|&Phi;", "\u03a6");
        desc = desc.replaceAll("&#935;|&Chi;", "\u03a7");
        desc = desc.replaceAll("&#936;|&Psi;", "\u03a8");
        desc = desc.replaceAll("&#937;|&Omega;", "\u03a9");
        desc = desc.replaceAll("&#945;|&alpha;", "\u03b1");
        desc = desc.replaceAll("&#946;|&beta;", "\u03b2");
        desc = desc.replaceAll("&#947;|&gamma;", "\u03b3");
        desc = desc.replaceAll("&#948;|&delta;", "\u03b4");
        desc = desc.replaceAll("&#949;|&epsilon;", "\u03b5");
        desc = desc.replaceAll("&#950;|&zeta;", "\u03b6");
        desc = desc.replaceAll("&#951;|&eta;", "\u03b7");
        desc = desc.replaceAll("&#952;|&theta;", "\u03b8");
        desc = desc.replaceAll("&#953;|&iota;", "\u03b9");
        desc = desc.replaceAll("&#954;|&kappa;", "\u03ba");
        desc = desc.replaceAll("&#955;|&lambda;", "\u03bb");
        desc = desc.replaceAll("&#956;|&mu;", "\u03bc");
        desc = desc.replaceAll("&#957;|&nu;", "\u03bd");
        desc = desc.replaceAll("&#958;|&xi;", "\u03be");
        desc = desc.replaceAll("&#959;|&omicron;", "\u03bf");
        desc = desc.replaceAll("&#960;|&pi;", "\u03c0");
        desc = desc.replaceAll("&#961;|&rho;", "\u03c1");
        desc = desc.replaceAll("&#962;|&sigmaf;", "\u03c2");
        desc = desc.replaceAll("&#963;|&sigma;", "\u03c3");
        desc = desc.replaceAll("&#964;|&tau;", "\u03c4");
        desc = desc.replaceAll("&#965;|&upsilon;", "\u03c5");
        desc = desc.replaceAll("&#966;|&phi;", "\u03c6");
        desc = desc.replaceAll("&#967;|&chi;", "\u03c7");
        desc = desc.replaceAll("&#968;|&psi;", "\u03c8");
        desc = desc.replaceAll("&#969;|&omega;", "\u03c9");
        desc = desc.replaceAll("&#977;|&thetasym;", "\u03d1");
        desc = desc.replaceAll("&#978;|&upsih;", "\u03d2");
        desc = desc.replaceAll("&#982;|&piv;", "\u03d6");
        desc = desc.replaceAll("&#8226;|&bull;", "\u2022");
        desc = desc.replaceAll("&#8230;|&hellip;", "\u2026");
        desc = desc.replaceAll("&#8242;|&prime;", "\u2032");
        desc = desc.replaceAll("&#8243;|&Prime;", "\u2033");
        desc = desc.replaceAll("&#8254;|&oline;", "\u203e");
        desc = desc.replaceAll("&#8260;|&frasl;", "\u2044");
        desc = desc.replaceAll("&#8472;|&weierp;", "\u2118");
        desc = desc.replaceAll("&#8465;|&image;", "\u2111");
        desc = desc.replaceAll("&#8476;|&real;", "\u211c");
        desc = desc.replaceAll("&#8482;|&trade;", "\u2122");
        desc = desc.replaceAll("&#8501;|&alefsym;", "\u2135");
        desc = desc.replaceAll("&#8592;|&larr;", "\u2190");
        desc = desc.replaceAll("&#8593;|&uarr;", "\u2191");
        desc = desc.replaceAll("&#8594;|&rarr;", "\u2192");
        desc = desc.replaceAll("&#8595;|&darr;", "\u2193");
        desc = desc.replaceAll("&#8596;|&harr;", "\u2194");
        desc = desc.replaceAll("&#8629;|&crarr;", "\u21b5");
        desc = desc.replaceAll("&#8656;|&lArr;", "\u21d0");
        desc = desc.replaceAll("&#8657;|&uArr;", "\u21d1");
        desc = desc.replaceAll("&#8658;|&rArr;", "\u21d2");
        desc = desc.replaceAll("&#8659;|&dArr;", "\u21d3");
        desc = desc.replaceAll("&#8660;|&hArr;", "\u21d4");
        desc = desc.replaceAll("&#8704;|&forall;", "\u2200");
        desc = desc.replaceAll("&#8706;|&part;", "\u2202");
        desc = desc.replaceAll("&#8707;|&exist;", "\u2203");
        desc = desc.replaceAll("&#8709;|&empty;", "\u2205");
        desc = desc.replaceAll("&#8711;|&nabla;", "\u2207");
        desc = desc.replaceAll("&#8712;|&isin;", "\u2208");
        desc = desc.replaceAll("&#8713;|&notin;", "\u2209");
        desc = desc.replaceAll("&#8715;|&ni;", "\u220b");
        desc = desc.replaceAll("&#8719;|&prod;", "\u220f");
        desc = desc.replaceAll("&#8721;|&sum;", "\u2211");
        desc = desc.replaceAll("&#8722;|&minus;", "\u2212");
        desc = desc.replaceAll("&#8727;|&lowast;", "\u2217");
        desc = desc.replaceAll("&#8730;|&radic;", "\u221a");
        desc = desc.replaceAll("&#8733;|&prop;", "\u221d");
        desc = desc.replaceAll("&#8734;|&infin;", "\u221e");
        desc = desc.replaceAll("&#8736;|&ang;", "\u2220");
        desc = desc.replaceAll("&#8743;|&and;", "\u2227");
        desc = desc.replaceAll("&#8744;|&or;", "\u2228");
        desc = desc.replaceAll("&#8745;|&cap;", "\u2229");
        desc = desc.replaceAll("&#8746;|&cup;", "\u222a");
        desc = desc.replaceAll("&#8747;|&int;", "\u222b");
        desc = desc.replaceAll("&#8756;|&there4;", "\u2234");
        desc = desc.replaceAll("&#8764;|&sim;", "\u223c");
        desc = desc.replaceAll("&#8773;|&cong;", "\u2245");
        desc = desc.replaceAll("&#8776;|&asymp;", "\u2248");
        desc = desc.replaceAll("&#8800;|&ne;", "\u2260");
        desc = desc.replaceAll("&#8801;|&equiv;", "\u2261");
        desc = desc.replaceAll("&#8804;|&le;", "\u2264");
        desc = desc.replaceAll("&#8805;|&ge;", "\u2265");
        desc = desc.replaceAll("&#8834;|&sub;", "\u2282");
        desc = desc.replaceAll("&#8835;|&sup;", "\u2283");
        desc = desc.replaceAll("&#8836;|&nsub;", "\u2284");
        desc = desc.replaceAll("&#8838;|&sube;", "\u2286");
        desc = desc.replaceAll("&#8839;|&supe;", "\u2287");
        desc = desc.replaceAll("&#8853;|&oplus;", "\u2295");
        desc = desc.replaceAll("&#8855;|&otimes;", "\u2297");
        desc = desc.replaceAll("&#8869;|&perp;", "\u22a5");
        desc = desc.replaceAll("&#8901;|&sdot;", "\u22c5");
        desc = desc.replaceAll("&#8968;|&lceil;", "\u2308");
        desc = desc.replaceAll("&#8969;|&rceil;", "\u2309");
        desc = desc.replaceAll("&#8970;|&lfloor;", "\u230a");
        desc = desc.replaceAll("&#8971;|&rfloor;", "\u230b");
        desc = desc.replaceAll("&#9001;|&lang;", "\u27e8");
        desc = desc.replaceAll("&#9002;|&rang;", "\u27e9");
        desc = desc.replaceAll("&#9674;|&loz;", "\u25ca");
        desc = desc.replaceAll("&#9824;|&spades;", "\u2660");
        desc = desc.replaceAll("&#9827;|&clubs;", "\u2663");
        desc = desc.replaceAll("&#9829;|&hearts;", "\u2665");
        desc = desc.replaceAll("&#9830;|&diams;", "\u2666");
        desc = desc.replaceAll("&#968;", "\u03c8");
        desc = desc.replaceAll("&#255;", "y");
        desc = desc.replaceAll("&#253;", "y");
        desc = desc.replaceAll("&#221;", "Y");
        desc = desc.replaceAll("&#252;", "u");
        desc = desc.replaceAll("&#251;", "u");
        desc = desc.replaceAll("&#250;", "u");
        desc = desc.replaceAll("&#249;", "u");
        desc = desc.replaceAll("&#220;", "U");
        desc = desc.replaceAll("&#219;", "U");
        desc = desc.replaceAll("&#218;", "U");
        desc = desc.replaceAll("&#217;", "U");
        desc = desc.replaceAll("&#254;", "thorn");
        desc = desc.replaceAll("&#222;", "THORN");
        desc = desc.replaceAll("&#223;", "s");
        desc = desc.replaceAll("&#248;", "o");
        desc = desc.replaceAll("&#246;", "o");
        desc = desc.replaceAll("&#245;", "o");
        desc = desc.replaceAll("&#244;", "o");
        desc = desc.replaceAll("&#243;", "o");
        desc = desc.replaceAll("&#242;", "o");
        desc = desc.replaceAll("&#216;", "O");
        desc = desc.replaceAll("&#214;", "O");
        desc = desc.replaceAll("&#213;", "O");
        desc = desc.replaceAll("&#212;", "O");
        desc = desc.replaceAll("&#211;", "O");
        desc = desc.replaceAll("&#210;", "O");
        desc = desc.replaceAll("&#241;", "n");
        desc = desc.replaceAll("&#209;", "N");
        desc = desc.replaceAll("&#239;", "i");
        desc = desc.replaceAll("&#238;", "i");
        desc = desc.replaceAll("&#237;", "i");
        desc = desc.replaceAll("&#236;", "i");
        desc = desc.replaceAll("&#207;", "I");
        desc = desc.replaceAll("&#206;", "I");
        desc = desc.replaceAll("&#205;", "I");
        desc = desc.replaceAll("&#204;", "I");
        desc = desc.replaceAll("&#240;", "eth");
        desc = desc.replaceAll("&#208;", "ETH");
        desc = desc.replaceAll("&#235;", "e");
        desc = desc.replaceAll("&#234;", "e");
        desc = desc.replaceAll("&#233;", "e");
        desc = desc.replaceAll("&#232;", "e");
        desc = desc.replaceAll("&#203;", "E");
        desc = desc.replaceAll("&#202;", "E");
        desc = desc.replaceAll("&#201;", "E");
        desc = desc.replaceAll("&#200;", "E");
        desc = desc.replaceAll("&#231;", "c");
        desc = desc.replaceAll("&#199;", "C");
        desc = desc.replaceAll("&#230;", "ae");
        desc = desc.replaceAll("&#198;", "AE");
        desc = desc.replaceAll("&#229;", "a");
        desc = desc.replaceAll("&#228;", "a");
        desc = desc.replaceAll("&#227;", "a");
        desc = desc.replaceAll("&#226;", "a");
        desc = desc.replaceAll("&#225;", "a");
        desc = desc.replaceAll("&#224;", "a");
        desc = desc.replaceAll("&#197;", "A");
        desc = desc.replaceAll("&#196;", "A");
        desc = desc.replaceAll("&#195;", "A");
        desc = desc.replaceAll("&#194;", "A");
        desc = desc.replaceAll("&#193;", "A");
        desc = desc.replaceAll("&#192;", "A");
        desc = desc.replaceAll("&#9830;", "\u2666");
        desc = desc.replaceAll("&#9829;", "\u2665");
        desc = desc.replaceAll("&#9827;", "\u2663");
        desc = desc.replaceAll("&#9824;", "\u2660");
        desc = desc.replaceAll("&#9674;", "\u25ca");
        desc = desc.replaceAll("&#9658;", "\u25ba");
        desc = desc.replaceAll("&#982;", "\u03d6");
        desc = desc.replaceAll("&#978;", "\u03d2");
        desc = desc.replaceAll("&#977;", "\u03d1");
        desc = desc.replaceAll("&#969;", "\u03c9");
        desc = desc.replaceAll("&#967;", "\u03c7");
        desc = desc.replaceAll("&#966;", "\u03c6");
        desc = desc.replaceAll("&#965;", "\u03c5");
        desc = desc.replaceAll("&#964;", "\u03c4");
        desc = desc.replaceAll("&#963;", "\u03c3");
        desc = desc.replaceAll("&#962;", "\u03c2");
        desc = desc.replaceAll("&#961;", "\u03c1");
        desc = desc.replaceAll("&#960;", "\u03c0");
        desc = desc.replaceAll("&#959;", "\u03bf");
        desc = desc.replaceAll("&#958;", "\u03be");
        desc = desc.replaceAll("&#957;", "\u03bd");
        desc = desc.replaceAll("&#956;", "\u03bc");
        desc = desc.replaceAll("&#955;", "\u03bb");
        desc = desc.replaceAll("&#954;", "\u03ba");
        desc = desc.replaceAll("&#953;", "\u03b9");
        desc = desc.replaceAll("&#952;", "\u03b8");
        desc = desc.replaceAll("&#951;", "\u03b7");
        desc = desc.replaceAll("&#950;", "\u03b6");
        desc = desc.replaceAll("&#949;", "\u03b5");
        desc = desc.replaceAll("&#948;", "\u03b4");
        desc = desc.replaceAll("&#947;", "\u03b3");
        desc = desc.replaceAll("&#946;", "\u03b2");
        desc = desc.replaceAll("&#945;", "\u03b1");
        desc = desc.replaceAll("&#937;", "\u03a9");
        desc = desc.replaceAll("&#936;", "\u03a8");
        desc = desc.replaceAll("&#935;", "\u03a7");
        desc = desc.replaceAll("&#934;", "\u03a6");
        desc = desc.replaceAll("&#933;", "\u03a5");
        desc = desc.replaceAll("&#932;", "\u03a4");
        desc = desc.replaceAll("&#931;", "\u03a3");
        desc = desc.replaceAll("&#929;", "\u03a1");
        desc = desc.replaceAll("&#928;", "\u03a0");
        desc = desc.replaceAll("&#927;", "\u039f");
        desc = desc.replaceAll("&#926;", "\u039e");
        desc = desc.replaceAll("&#925;", "\u039d");
        desc = desc.replaceAll("&#924;", "\u039c");
        desc = desc.replaceAll("&#923;", "\u039b");
        desc = desc.replaceAll("&#922;", "\u039a");
        desc = desc.replaceAll("&#921;", "\u0399");
        desc = desc.replaceAll("&#920;", "\u0398");
        desc = desc.replaceAll("&#919;", "\u0397");
        desc = desc.replaceAll("&#918;", "\u0396");
        desc = desc.replaceAll("&#917;", "\u0395");
        desc = desc.replaceAll("&#916;", "\u0394");
        desc = desc.replaceAll("&#915;", "\u0393");
        desc = desc.replaceAll("&#914;", "\u0392");
        desc = desc.replaceAll("&#913;", "\u0391");
        desc = desc.replaceAll("&#8971;", "\u230b");
        desc = desc.replaceAll("&#8970;", "\u230a");
        desc = desc.replaceAll("&#8969;", "\u2309");
        desc = desc.replaceAll("&#8968;", "\u2308");
        desc = desc.replaceAll("&#8901;", "\u22c5");
        desc = desc.replaceAll("&#8869;", "\u22a5");
        desc = desc.replaceAll("&#8855;", "\u2297");
        desc = desc.replaceAll("&#8853;", "\u2295");
        desc = desc.replaceAll("&#8839;", "\u2287");
        desc = desc.replaceAll("&#8838;", "\u2286");
        desc = desc.replaceAll("&#8836;", "\u2284");
        desc = desc.replaceAll("&#8835;", "\u2283");
        desc = desc.replaceAll("&#8834;", "\u2282");
        desc = desc.replaceAll("&#8805;", "\u2265");
        desc = desc.replaceAll("&#8804;", "\u2264");
        desc = desc.replaceAll("&#8801;", "\u2261");
        desc = desc.replaceAll("&#8800;", "\u2260");
        desc = desc.replaceAll("&#8776;", "\u2248");
        desc = desc.replaceAll("&#8773;", "\u2245");
        desc = desc.replaceAll("&#8764;", "\u223c");
        desc = desc.replaceAll("&#8756;", "\u2234");
        desc = desc.replaceAll("&#8747;", "\u222b");
        desc = desc.replaceAll("&#8746;", "\u222a");
        desc = desc.replaceAll("&#8745;", "\u2229");
        desc = desc.replaceAll("&#8744;", "\u2228");
        desc = desc.replaceAll("&#8743;", "\u2227");
        desc = desc.replaceAll("&#8736;", "\u2220");
        desc = desc.replaceAll("&#8734;", "\u221e");
        desc = desc.replaceAll("&#8733;", "\u221d");
        desc = desc.replaceAll("&#8730;", "\u221a");
        desc = desc.replaceAll("&#8727;", "\u2217");
        desc = desc.replaceAll("&#8722;", "\u2212");
        desc = desc.replaceAll("&#8721;", "\u2211");
        desc = desc.replaceAll("&#8719;", "\u220f");
        desc = desc.replaceAll("&#8715;", "\u220b");
        desc = desc.replaceAll("&#8713;", "\u2209");
        desc = desc.replaceAll("&#8712;", "\u2208");
        desc = desc.replaceAll("&#8711;", "\u2207");
        desc = desc.replaceAll("&#8709;", "\u2205");
        desc = desc.replaceAll("&#8707;", "\u2203");
        desc = desc.replaceAll("&#8706;", "\u2202");
        desc = desc.replaceAll("&#8704;", "\u2200");
        desc = desc.replaceAll("&#8629;", "\u21b5");
        desc = desc.replaceAll("&#8596;", "\u2194");
        desc = desc.replaceAll("&#8595;", "\u2193");
        desc = desc.replaceAll("&#8594;", "\u2192");
        desc = desc.replaceAll("&#8593;", "\u2191");
        desc = desc.replaceAll("&#8592;", "\u2190");
        desc = desc.replaceAll("&#8482;", "\u2122");
        desc = desc.replaceAll("&#8364;", "\u20ac");
        desc = desc.replaceAll("&#8254;", "\u203e");
        desc = desc.replaceAll("&#8250;", "\u203a");
        desc = desc.replaceAll("&#8249;", "\u2039");
        desc = desc.replaceAll("&#8243;", "\u2033");
        desc = desc.replaceAll("&#8242;", "\u2032");
        desc = desc.replaceAll("&#8240;", "\u2030");
        desc = desc.replaceAll("&#8230;", "\u2026");
        desc = desc.replaceAll("&#8226;", "\u2022");
        desc = desc.replaceAll("&#8225;", "\u2021");
        desc = desc.replaceAll("&#8224;", "\u2020");
        desc = desc.replaceAll("&#8222;", "\u201e");
        desc = desc.replaceAll("&#8221;", "\u201d");
        desc = desc.replaceAll("&#8220;", "\u201c");
        desc = desc.replaceAll("&#8218;", "\u201a");
        desc = desc.replaceAll("&#8217;", "\u2019");
        desc = desc.replaceAll("&#8216;", "\u2018");
        desc = desc.replaceAll("&#8213;", "\u2015");
        desc = desc.replaceAll("&#8212;", "\u2014");
        desc = desc.replaceAll("&#8211;", "\u2013");
        desc = desc.replaceAll("&#8207;", "\u200f");
        desc = desc.replaceAll("&#8206;", "\u200e");
        desc = desc.replaceAll("&#8205;", "\u200d");
        desc = desc.replaceAll("&#8204;", "\u200c");
        desc = desc.replaceAll("&#732;", "\u02dc");
        desc = desc.replaceAll("&#710;", "\u02c6");
        desc = desc.replaceAll("&#402;", "\u0192");
        desc = desc.replaceAll("&#376;", "Y");
        desc = desc.replaceAll("&#353;", "s");
        desc = desc.replaceAll("&#352;", "S");
        desc = desc.replaceAll("&#351;", "S");
        desc = desc.replaceAll("&#339;", "oe");
        desc = desc.replaceAll("&#338;", "OE");
        desc = desc.replaceAll("&#304;", "i");
        desc = desc.replaceAll("&#287;", "G");
        desc = desc.replaceAll("&#247;", "\u00f7");
        desc = desc.replaceAll("&#215;", "\u00d7");
        desc = desc.replaceAll("&#191;", "\u00bf");
        desc = desc.replaceAll("&#190;", "\u00be");
        desc = desc.replaceAll("&#189;", "\u00bd");
        desc = desc.replaceAll("&#188;", "\u00bc");
        desc = desc.replaceAll("&#187;", "\u00bb");
        desc = desc.replaceAll("&#186;", "\u00ba");
        desc = desc.replaceAll("&#185;", "\u00b9");
        desc = desc.replaceAll("&#184;", "\u00b8");
        desc = desc.replaceAll("&#183;", "\u00b7");
        desc = desc.replaceAll("&#182;", "\u00b6");
        desc = desc.replaceAll("&#181;", "\u00b5");
        desc = desc.replaceAll("&#180;", "\u00b4");
        desc = desc.replaceAll("&#179;", "\u00b3");
        desc = desc.replaceAll("&#178;", "\u00b2");
        desc = desc.replaceAll("&#177;", "\u00b1");
        desc = desc.replaceAll("&#176;", "\u00b0");
        desc = desc.replaceAll("&#175;", "\u00af");
        desc = desc.replaceAll("&#174;", "\u00ae");
        desc = desc.replaceAll("&#172;", "\u00ac");
        desc = desc.replaceAll("&#171;", "\u00ab");
        desc = desc.replaceAll("&#170;", "\u00aa");
        desc = desc.replaceAll("&#169;", "\u00a9");
        desc = desc.replaceAll("&#168;", "\u00a8");
        desc = desc.replaceAll("&#167;", "\u00a7");
        desc = desc.replaceAll("&#166;", "\u00a6");
        desc = desc.replaceAll("&#165;", "\u00a5");
        desc = desc.replaceAll("&#164;", "\u00a4");
        desc = desc.replaceAll("&#163;", "\u00a3");
        desc = desc.replaceAll("&#162;", "\u00a2");
        desc = desc.replaceAll("&#161;", "\u00a1");
        desc = desc.replaceAll("&#1103;", "\u042f");
        desc = desc.replaceAll("&#1100;", "\u042c");
        desc = desc.replaceAll("&#1095;", "\u0427");
        desc = desc.replaceAll("&#1090;", "\u0422");
        desc = desc.replaceAll("&#1087;", "\u043f");
        desc = desc.replaceAll("&#1085;", "\u041d");
        desc = desc.replaceAll("&#1084;", "\u041c");
        desc = desc.replaceAll("&#1083;", "\u043b");
        desc = desc.replaceAll("&#1080;", "\u0438");
        desc = desc.replaceAll("&#1076;", "\u0434");
        desc = desc.replaceAll("&#1073;", "\u0431");
        desc = desc.replaceAll("&#1054;", "\u041e");
        desc = desc.replaceAll("&#1072;", "\u0430");
        desc = desc.replaceAll("&#1073;", "\u0431");
        desc = desc.replaceAll("&#1076;", "\u0434");
        desc = desc.replaceAll("&#1077;", "\u0435");
        desc = desc.replaceAll("&#1080;", "\u0438");
        desc = desc.replaceAll("&#1083;", "\u043b");
        desc = desc.replaceAll("&#1084;", "\u043c");
        desc = desc.replaceAll("&#1085;", "\u043d");
        desc = desc.replaceAll("&#1086;", "\u043e");
        desc = desc.replaceAll("&#1087;", "\u043f");
        desc = desc.replaceAll("&#1088;", "\u0440");
        desc = desc.replaceAll("&#1089;", "\u0441");
        desc = desc.replaceAll("&#1090;", "\u0442");
        desc = desc.replaceAll("&#1091;", "\u0443");
        desc = desc.replaceAll("&#1093;", "\u0445");
        desc = desc.replaceAll("&#1095;", "\u0447");
        desc = desc.replaceAll("&#1100;", "\u044c");
        desc = desc.replaceAll("&#1103;", "\u044f");
        desc = desc.replaceAll("&#215; ", "\u00d7");
        desc = desc.replaceAll("&#224;", "\u00e0");
        desc = desc.replaceAll("&#225;", "\u00e1");
        desc = desc.replaceAll("&#228;", "\u00e4");
        desc = desc.replaceAll("&#232; ", "\u00e8");
        desc = desc.replaceAll("&#237;", "\u00ed");
        desc = desc.replaceAll("&#239;", "\u00ef");
        desc = desc.replaceAll("&#241;", "\u00f1");
        desc = desc.replaceAll("&#243;", "\u00f3");
        desc = desc.replaceAll("&#244;", "\u00f4");
        desc = desc.replaceAll("&#246;", "\u00f6");
        desc = desc.replaceAll("&#252;", "\u00fc");
        desc = desc.replaceAll("&#287;", "\u011f");
        desc = desc.replaceAll("&#30000;", "\u7530");
        desc = desc.replaceAll("&#304;", "\u0130");
        desc = desc.replaceAll("&#305;", "I");
        desc = desc.replaceAll("&#351;", "\u015f");
        desc = desc.replaceAll("&#38272;", "\u9580");
        desc = desc.replaceAll("&#8212;", "\u2014");
        desc = desc.replaceAll("&#8216;", "\u2018");
        desc = desc.replaceAll("&#8230;", "\u2026");
        desc = desc.replaceAll("&#928;", "\u03a0");
        desc = desc.replaceAll("&#9829", "\u03d6");
        return desc;
    }
}

