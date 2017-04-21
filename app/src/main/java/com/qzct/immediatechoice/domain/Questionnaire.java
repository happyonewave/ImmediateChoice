package com.qzct.immediatechoice.domain;

import java.util.List;

/**
 * Created by tsh2 on 2017/4/19.
 */

public class Questionnaire {
    String title;
    String hint;
    List<Entity> entities;

    public List<Entity> getEntities() {
        return entities;
    }

    public String getTitle() {
        return title;
    }

    public String getHint() {
        return hint;
    }

    public Questionnaire(String title, List<Entity> entities) {
        this.title = title;
        this.entities = entities;
    }

    public Questionnaire(String hint, String title) {
        this.hint = hint;
        this.title = title;
    }

    public Questionnaire(List<Entity> entities, String title, String hint) {
        this.entities = entities;
        this.title = title;
        this.hint = hint;
    }

   public static class Entity {
        String title;
        List<String> options;

       public String getTitle() {
           return title;
       }

       public List<String> getOptions() {
           return options;
       }

       public Entity(String title, List<String> options) {
           this.title = title;
           this.options = options;
       }
   }
}

