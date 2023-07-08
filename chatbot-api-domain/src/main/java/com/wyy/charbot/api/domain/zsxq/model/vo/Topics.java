package com.wyy.charbot.api.domain.zsxq.model.vo;

public class Topics {
    private String topic_id;
    private Questions question;

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public Questions getQuestion() {
        return question;
    }

    public void setQuestion(Questions question) {
        this.question = question;
    }
}
