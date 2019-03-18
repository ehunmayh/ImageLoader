package myh.com.imageloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import myh.com.imageloader.image.ImageLoader;

public class MainActivity extends AppCompatActivity {

    private ImageView mImgv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mImgv = findViewById(R.id.imgv);

        ImageLoader.getInstance(this).display("https://pic2.itrip.com/p/20150313165324-standard-596.jpg?imageView2/5/w/695/h/464/q/100",mImgv);
    }
}
