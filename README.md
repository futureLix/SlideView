# SlideView
Modified sliding layout of ios-like camera


### SlideView sample
<img src="screen/6933a184-bee7-491b-94df-40231d6033f8.gif" width="400"> </img>

## Build

Add the following to your app's build.gradle:

```groovy
dependencies {
    implementation 'com.github.futureLix:SlideView:v1.0'
}
```
## How to use


#### Add the following XML:

```xml
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".ExampleActivity">

     <com.example.custom.CustomView
        android:id="@+id/custom"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:back_ground=""
        app:foreground=""
        app:text_size=""
        android:gravity="center"/>
  
</LinearLayout>
```


#### Create :

```java
public class ExampleActivity extends AppCompatActivity implements View.OnTouchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        bindView();
    }

    private void bindView() {
         String[] name = new String[]{"延时摄影", "慢动作", "视频", "照片", "正方形", "全景"};
        final CustomView customView = findViewById(R.id.custom);
        CustomGestureDetector releaseGestureDetector = new CustomGestureDetector();
        releaseGestureDetector.setCustomView(customView);
        mGestureDetector = new GestureDetector(this, releaseGestureDetector);
        customView.setBackGround(ContextCompat.getColor(this, android.R.color.holo_orange_light));
        customView.setForeGround(ContextCompat.getColor(this, android.R.color.white));
        customView.setTextSize(15);
        customView.addIndicator(name);
        customView.setOnTouchListener(this);
        customView.setOnClickText(new CustomView.onClickText() {
            @Override
            public void onClick(int i, int last) {
                customView.scrollTo(i, last);
            }
        });
    }
    
     @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }
}
```

### Thanks to the original author：

csblogs: https://www.cnblogs.com/xiaojianli/p/5689480.html
