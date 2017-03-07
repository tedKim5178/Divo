package com.example.mk.mydramabulary.parsing;

/**
 * Created by mk on 2017-02-10.
 */

public class ParsingContract {

    // 단어
    public static String WORD_FIRST_BASE_URL = "http://endic.naver.com/search.nhn?sLn=kr&query=";
    public static String WORD_LAST_BASE_URL = "&searchOption=all&preQuery=&forceRedirect=N";
    public static String WORD_SEPARATION = "div[class=align_right]";
    public static String WORD_CLASSIFICATION_CLASS = "fnt_k09";
    public static String WORD_DEFINITION = "fnt_k05";

    // 예문
    public static String SENTENCE_FRIST_BASE_URL = "http://endic.naver.com/search_example.nhn?sLn=kr&query=";
    public static String SENTENCE_LAST_BASE_URL = "&preQuery=&searchOption=example&forceRedirect=N";
    public static String ENGLISH_PARSING_QUERY_DIV = "div[class=lineheight18 mar_top01]";
    public static String KOREAN_PARSING_QUERY_DIV = "div[class=mar_top1]";
    public static String ENGLISH_PARSING_QUERY_TAG = "input";
    public static String ENGLISH_PARSING_QUERY_ATTR = "value";

}
