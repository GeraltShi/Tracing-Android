package com.fdwireless.trace.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fdwireless.trace.httpclass.GlideImageLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by 汪励颢 on 2017/2/12.
 */

public class ImageSelect extends AppCompatActivity {
    private List<File> files = new ArrayList<File>();
    private List<PhotoInfo> mPhotoList = new ArrayList<PhotoInfo>();
    //private ChoosePhotoListAdapter mChoosePhotoListAdapter;
   // private Button mOpenGallery;
    static final int REQUEST_CODE_GALLERY = 200;
   private RequestQueue queue;
    //REQUEST_CODE_GALLERY

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         queue = Volley.newRequestQueue(ImageSelect.this);
        super.onCreate(savedInstanceState);

        ThemeConfig themeConfig = ThemeConfig.DEFAULT;
        FunctionConfig functionConfig = new FunctionConfig.Builder().setEnablePreview(true).setEnableEdit(true)
                .setEnableCrop(true).setEnableCamera(true).setEnableRotate(true).setMutiSelectMaxSize(9).build();
        GlideImageLoader imageLoader = new GlideImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageLoader, themeConfig).setFunctionConfig(functionConfig).build();
        GalleryFinal.init(coreConfig);
        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
    }




    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {

                mPhotoList.addAll(resultList);

                for (int i = 0; i < mPhotoList.size(); i++) {
                    files.add(new File(mPhotoList.get(i).getPhotoPath()));
                }
                           Map<String,String> params=new HashMap<>();
            params.put("id","1");
            params.put("x","1.7");
            params.put("y","1.8");
            params.put("text","test");


//创建OkHttpClient实例
            final OkHttpClient client = new OkHttpClient();

            MultipartBody.Builder builder=  new MultipartBody.Builder().setType(MultipartBody.FORM);

//遍历map中所有参数到builder
            for (String key :  params.keySet()) {
                builder.addFormDataPart(key, params.get(key));
            }

            //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
            for (File file : files) {

                builder.addFormDataPart("file[]",file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
            }

            //构建请求体
            RequestBody requestBody = builder.build();

            //构建请求
            Request request = new Request.Builder()
                    .url("http://115.159.198.209/Tracing/upload.php")//地址
                    .post(requestBody)//添加请求体
                    .build();

            //发送异步请求，同步会报错，Android4.0以后禁止在主线程中进行耗时操作
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final  String bodyStr = response.body().string();

                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(ImageSelect.this,bodyStr,
                                    Toast.LENGTH_SHORT).show();


                        }
                    });
                }


            });


        }

        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(ImageSelect.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

}

