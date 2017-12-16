package com.fdwireless.trace.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.fdwireless.trace.httpclass.GlideImageLoader;
import com.fdwireless.trace.httpclass.my_StringRequest;
import com.fdwireless.trace.mapmodule.R;

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

public class RegisterActivity extends AppCompatActivity {

    private EditText accountEdit;

    private EditText passwordEdit;

    private Button signup;

    private ImageButton pic;
    private my_StringRequest request =new my_StringRequest(this);
    static final int REQUEST_CODE_GALLERY = 200;
    private RequestQueue queue;
    private List<PhotoInfo> mPhotoList = new ArrayList<PhotoInfo>();
    private  File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(RegisterActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        accountEdit = (EditText) findViewById(R.id.accountrig);
        passwordEdit = (EditText) findViewById(R.id.passwordrig);

        signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if((accountEdit.getText()!=null)&&(passwordEdit.getText()!=null)&&(file!=null)){
                    Map <String,String> token=new HashMap<String, String>();
                    token.put("name",accountEdit.getText().toString());
                    token.put("pass",passwordEdit.getText().toString());
                    Log.e("registertest",accountEdit.getText().toString());




//创建OkHttpClient实例
                    final OkHttpClient client = new OkHttpClient();

                    MultipartBody.Builder builder=  new MultipartBody.Builder().setType(MultipartBody.FORM);

//遍历map中所有参数到builder
                    for (String key :  token.keySet()) {
                        builder.addFormDataPart(key, token.get(key));
                    }

                    //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key


                    builder.addFormDataPart("file",file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));


                    //构建请求体
                    RequestBody requestBody = builder.build();

                    //构建请求
                    Request request = new Request.Builder()
                            .url("http://115.159.198.209/Tracing/register.php")//地址
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
                          Log.e("test",bodyStr);
                                    Toast.makeText(RegisterActivity.this,bodyStr,
                                            Toast.LENGTH_SHORT).show();


                                }
                            });
                        }


                    });



                }


                else{
                    if((accountEdit.getText().toString()==null)||(passwordEdit.getText().toString()==null))
                    Toast.makeText(RegisterActivity.this, "请填写用户名和密码",
                            Toast.LENGTH_SHORT).show();
                    else if(file==null){
                        Toast.makeText(RegisterActivity.this, "请选择头像",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        //signup.setBackgroundColor(Color.parseColor("#2196F3"));

        pic = (ImageButton) findViewById(R.id.pic_icon);
        pic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ThemeConfig themeConfig = ThemeConfig.DEFAULT;
                FunctionConfig functionConfig = new FunctionConfig.Builder().setEnablePreview(true).setEnableEdit(true)
                        .setEnableCrop(true).setEnableCamera(true).setEnableRotate(true).setMutiSelectMaxSize(1).build();
                GlideImageLoader imageLoader = new GlideImageLoader();
                CoreConfig coreConfig = new CoreConfig.Builder(RegisterActivity.this, imageLoader, themeConfig).setFunctionConfig(functionConfig).build();
                GalleryFinal.init(coreConfig);
                //GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
                GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY,functionConfig,mOnHanlderResultCallback);
              //  Toast.makeText(RegisterActivity.this, "choose a picture",
                     //   Toast.LENGTH_SHORT).show();
            }
        });
    }
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {

                mPhotoList.addAll(resultList);

//                for (int i = 0; i < mPhotoList.size(); i++) {
//                    files.add(new File(mPhotoList.get(i).getPhotoPath()));
//                }
              file=new File(mPhotoList.get(0).getPhotoPath());
              //  Bitmap bm = BitmapFactory.decodeFile(mPhotoList.get(0).getPhotoPath());
              // pic.setImageBitmap(bm);
                Glide.with(RegisterActivity.this).load(mPhotoList.get(0).getPhotoPath()).into(pic);


            }

        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };
}