package com.qzct.immediatechoice.domain;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsh2 on 2017/4/19.
 */

public class Questionnaire implements Serializable {
    String title;
    String hint;
    int user_id;
    List<Question> entities;

    public int getUser_id() {
        return user_id;
    }

    public Questionnaire(String title, String hint, int user_id, List<Question> entities) {
        this.title = title;
        this.hint = hint;
        this.user_id = user_id;
        this.entities = entities;
    }


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

    public static Questionnaire jsonObjectToQuestionnaire(JSONObject object) throws JSONException {
        String title = object.optString("title");
        String hint = object.optString("hint");
        JSONArray questionArray = object.optJSONArray("questionArray");
        Log.d("qin", "questionArray: " + questionArray);
        List<Question> entities = new ArrayList<Question>();
        for (int i = 0; i < questionArray.length(); i++) {
            Question question = Question.jsonObjectToQuestion(questionArray.optJSONObject(i));
            entities.add(question);
        }

        return new Questionnaire(title, hint, entities);
    }

    public JSONObject questionnaireToJSONObject(Questionnaire questionnaire) throws JSONException {
        JSONObject questionnaireObject = new JSONObject();
        int user_id = questionnaire.getUser_id();
        String title = questionnaire.getTitle();
        String hint = questionnaire.getHint();
        List<Question> entities = questionnaire.getEntities();
        JSONArray questionArray = new JSONArray();
        for (Question question : entities) {
            questionArray.put(question.questionToJSONObject(question));
        }
        questionnaireObject.put("user_id", user_id);
        questionnaireObject.put("title", title);
        questionnaireObject.put("hint", hint);
        questionnaireObject.put("questionArray", questionArray);
        return questionnaireObject;
    }

    public static class Question implements Serializable {
        int questionnaire_question_id;
        String title;
        List<Option> options;

        public Question(int questionnaire_question_id, String title, List<Option> options) {
            this.questionnaire_question_id = questionnaire_question_id;
            this.title = title;
            this.options = options;
        }

        public String getTitle() {
            return title;
        }

        public List<Option> getOptions() {
            return options;
        }

        public Question(String title, List<Option> options) {
            this.title = title;
            this.options = options;
        }

        public int getQuestionnaire_question_id() {
            return questionnaire_question_id;
        }

        public static Question jsonObjectToQuestion(JSONObject object) throws JSONException {
            String title = object.optString("title");
            int questionnaire_question_id = object.optInt("questionnaire_question_id");
            List<Option> options = new ArrayList<Option>();
            JSONArray optionArray = object.optJSONArray("optionArray");
            Log.d("qin", "optionArray: " + optionArray);
            for (int i = 0; i < optionArray.length(); i++) {
                Option option = Option.jsonObjectToOption(optionArray.optJSONObject(i));
                if (option != null) {
                    options.add(option);
//                    options.add(option.optString("content"));
                }
            }
            return new Question(questionnaire_question_id, title, options);
        }

        public JSONObject questionToJSONObject(Question question) throws JSONException {
            JSONObject questionObject = new JSONObject();
            String title = question.getTitle();
            List<Option> options = question.getOptions();
            JSONArray optionArray = new JSONArray();
            for (Option option : options) {
                optionArray.put(Option.optionToJSONObject(option));
            }
            questionObject.put("title", title);
            questionObject.put("optionArray", optionArray);
            return questionObject;
        }

        public static class Option implements Serializable {
            int option_id;
            int questionnaire_question_id;
            String num;
            String content;

            public int getOption_id() {
                return option_id;
            }

            public int getQuestionnaire_question_id() {
                return questionnaire_question_id;
            }

            public String getNum() {
                return num;
            }

            public String getContent() {
                return content;
            }

            public Option() {
            }

            public Option(String num, String content) {
                this.num = num;
                this.content = content;
            }

            public Option(int option_id, int questionnaire_question_id, String num, String content) {
                this.option_id = option_id;
                this.questionnaire_question_id = questionnaire_question_id;
                this.num = num;
                this.content = content;
            }

            public static Option jsonObjectToOption(JSONObject object) {
                if (object != null) {
                    int option_id = object.optInt("option_id");
                    int questionnaire_question_id = object.optInt("questionnaire_question_id");
                    String num = object.optString("num");
                    String content = object.optString("content");
                    return new Option(option_id, questionnaire_question_id, num, content);
                }
                return new Option("", "");
            }

            public static JSONObject optionToJSONObject(Option option) throws JSONException {
                JSONObject optionObject = new JSONObject();
                int option_id = option.getOption_id();
                int questionnaire_question_id = option.getQuestionnaire_question_id();
                String num = option.getNum();
                String content = option.getContent();
                optionObject.put("option_id", option_id);
                optionObject.put("questionnaire_question_id", questionnaire_question_id);
                optionObject.put("num", num);
                optionObject.put("content", content);
                return optionObject;
            }
        }
    }

    public static class Choice {
        int choice_id;
        int questionnaire_question_id;
        int user_id;
        String num;

        public int getChoice_id() {
            return choice_id;
        }

        public int getQuestionnaire_question_id() {
            return questionnaire_question_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public String getNum() {
            return num;
        }

        public Choice() {
        }

        public Choice(String num, int user_id, int questionnaire_question_id) {
            this.num = num;
            this.user_id = user_id;
            this.questionnaire_question_id = questionnaire_question_id;
        }

        public Choice(int choice_id, int questionnaire_question_id, int user_id, String num) {
            this.choice_id = choice_id;
            this.questionnaire_question_id = questionnaire_question_id;
            this.user_id = user_id;
            this.num = num;
        }
    }
}

