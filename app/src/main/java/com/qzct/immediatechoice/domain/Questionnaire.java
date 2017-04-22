package com.qzct.immediatechoice.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tsh2 on 2017/4/19.
 */

public class Questionnaire implements Serializable {
    String title;
    String hint;
    List<Question> entities;

    public List<Question> getEntities() {
        return entities;
    }

    public String getTitle() {
        return title;
    }

    public String getHint() {
        return hint;
    }

    public Questionnaire(String title, List<Question> entities) {
        this.title = title;
        this.entities = entities;
    }

    public Questionnaire(String hint, String title) {
        this.hint = hint;
        this.title = title;
    }

    public Questionnaire(String title, String hint, List<Question> entities) {
        this.title = title;
        this.hint = hint;
        this.entities = entities;
    }

    public Questionnaire(List<Question> entities, String title, String hint) {
        this.entities = entities;
        this.title = title;
        this.hint = hint;
    }

    public static class Question  implements  Serializable{
        String title;
        List<String> options;

        public String getTitle() {
            return title;
        }

        public List<String> getOptions() {
            return options;
        }

        public Question(String title, List<String> options) {
            this.title = title;
            this.options = options;
        }
    }
}

