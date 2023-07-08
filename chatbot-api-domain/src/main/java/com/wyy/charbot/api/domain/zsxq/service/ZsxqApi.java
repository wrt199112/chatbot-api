package com.wyy.charbot.api.domain.zsxq.service;

import com.alibaba.fastjson.JSON;
import com.wyy.charbot.api.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;
import com.wyy.charbot.api.domain.zsxq.IZsxqApi;
import com.wyy.charbot.api.domain.zsxq.model.req.AnswerReq;
import com.wyy.charbot.api.domain.zsxq.model.req.ReqData;
import com.wyy.charbot.api.domain.zsxq.model.res.AnswerRes;
import net.sf.json.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class ZsxqApi implements IZsxqApi {
    private Logger logger = LoggerFactory.getLogger(ZsxqApi.class);
    @Override
    public UnAnsweredQuestionsAggregates queryUnAnsweredQuestionsTopicId(String groupId, String cookie) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet("");
        get.addHeader("cookie",cookie);
        get.addHeader("contentType","application/Json;chartset=utf-8");
        CloseableHttpResponse response = httpClient.execute(get);
        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            String jsonStr = EntityUtils.toString(response.getEntity());
            logger.info("拉取问题数据。groupId:{} jsonStr:{}",groupId,jsonStr);
            return JSON.parseObject(jsonStr, UnAnsweredQuestionsAggregates.class);
        }else{
            System.out.println(response.getStatusLine().getStatusCode());
            throw new RuntimeException("queryUnAnsweredQuestionsTopicId Err code is " + response.getStatusLine().getStatusCode());
        }
    }

    @Override
    public boolean answer(String groupId,String cookie, String topicId, String text, boolean silenced) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("");
        post.addHeader("cookie",cookie);
        post.addHeader("contentType","application/Json;chartset=utf-8");
        post.addHeader("user-agent","Mozilla/5.0 (Macintosh; Intel Moc OS x 10_15_7) AppleWebkit/537.66 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36");
//        String paramJson = "{" +
//                "\"res_data\":{" +
//                "\"text\": \"我不会\"," +
//                "\"image_ids\": []," +
//                "\"silenced\":true" +
//                "} " +
//                "}";

        AnswerReq answerReq = new AnswerReq(new ReqData(text,silenced));
        String paramJson = JSONObject.fromObject(answerReq).toString();
        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);

        CloseableHttpResponse response = httpClient.execute(post);
        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            String jsonStr = EntityUtils.toString(response.getEntity());
            System.out.println(jsonStr);
            logger.info("回答问题结果。groupId:{} topicId:{} jsonStr:{}",groupId,topicId,jsonStr);
            AnswerRes answerRes = JSON.parseObject(jsonStr,AnswerRes.class);
            return answerRes.isSucceeded();
        }else{
            System.out.println(response.getStatusLine().getStatusCode());
            throw new RuntimeException("answer Err code is " + response.getStatusLine().getStatusCode());
        }
    }
}
