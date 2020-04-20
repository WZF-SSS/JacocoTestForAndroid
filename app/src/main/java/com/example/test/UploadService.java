package com.example.test;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.test.DeviceUtils;

import okhttp3.*;

/**
 *
 */
class UploadService extends Thread{

    void upload(String url, String filePath, String fileName,final UploadCallback uploadCallback) throws Exception {

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName,
                        RequestBody.create(MediaType.parse("multipart/form-data"), new File(filePath)))
                .build();

        final Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + java.util.UUID.randomUUID())
                .url(url)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (uploadCallback!=null){
                    uploadCallback.onSuccessful(result);
                }
            }
        });
    }


    public void start(File file, UploadCallback uploadCallback) {

        try {
            String fileName = "coverage.ec";
            String filePath = "/data/user/0/com.example.app1/files/coverage.ec";
            String url = "http://cover-server.xesv5.com/upload/up";
            upload(url, filePath, fileName,uploadCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface UploadCallback{
        void onSuccessful(String result);
    }


}