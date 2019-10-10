package com.behradrvb.ppnclient.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Message {
    private String data, title, content, imageURL;
    private JSONObject parsedJson;

    public Message(String data) {
        this.data = data;
        try {
            parsedJson = new JSONObject(data);
            title = parsedJson.getString("title");
            content = parsedJson.getString("content");
            imageURL = parsedJson.getString("imageURL");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getParsedJson() {
        return parsedJson;
    }

    public void setParsedJson(JSONObject parsedJson) {
        this.parsedJson = parsedJson;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


}
