package com.valgen.atd.pojo;

/**
 *
 * @author Developer
 */
public class HtmlHeaderAttributes {

    private int responseCode = 0;
    private String responseMessage = "";
    private String pageSoure = "";
    private String parserType = "";
    private String domainUrl = "";
    private String landingUrl = "";
    private String errorDescription = "";
    private String contentType = "";
    private int errorCode = 0;
    private String Isredirected = "";
    private int pdf_download_status = 0;
    private int process_status = 0;
    private String pdf_lastmodifieddate = "";
    private String pdf_content_length = "";
    private String contentDisposition = "";
    private boolean is_newpdf = false;
    private String extractedParser = "";

    public HtmlHeaderAttributes() {
    }

    public HtmlHeaderAttributes(int responseCode, String responseMessage, String pageSoure, String parserType, String domainUrl, String landingUrl, String errorDescription, String contentType, int errorCode) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.pageSoure = pageSoure;
        this.parserType = parserType;
        this.domainUrl = domainUrl;
        this.landingUrl = landingUrl;
        this.errorDescription = errorDescription;
        this.contentType = contentType;
        this.errorCode = errorCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getPageSoure() {
        return pageSoure;
    }

    public void setPageSoure(String pageSoure) {
        this.pageSoure = pageSoure;
    }

    public String getParserType() {
        return parserType;
    }

    public void setParserType(String parserType) {
        this.parserType = parserType;
    }

    public String getDomainUrl() {
        return domainUrl;
    }

    public void setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
    }

    public String getLandingUrl() {
        return landingUrl;
    }

    public void setLandingUrl(String landingUrl) {
        this.landingUrl = landingUrl;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getIsredirected() {
        return Isredirected;
    }

    public void setIsredirected(String Isredirected) {
        this.Isredirected = Isredirected;
    }

    public int getPdf_download_status() {
        return pdf_download_status;
    }

    public void setPdf_download_status(int pdf_download_status) {
        this.pdf_download_status = pdf_download_status;
    }

    public String getPdf_lastmodifieddate() {
        return pdf_lastmodifieddate;
    }

    public void setPdf_lastmodifieddate(String pdf_lastmodifieddate) {
        this.pdf_lastmodifieddate = pdf_lastmodifieddate;
    }

    public String getPdf_content_length() {
        return pdf_content_length;
    }

    public void setPdf_content_length(String pdf_content_length) {
        this.pdf_content_length = pdf_content_length;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public int getProcess_status() {
        return process_status;
    }

    public void setProcess_status(int process_status) {
        this.process_status = process_status;
    }

    public boolean isIs_newpdf() {
        return is_newpdf;
    }

    public void setIs_newpdf(boolean is_newpdf) {
        this.is_newpdf = is_newpdf;
    }

    public String getExtractedParser() {
        return extractedParser;
    }

    public void setExtractedParser(String extractedParser) {
        this.extractedParser = extractedParser;
    }
}
