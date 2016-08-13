package com.example.katherine_qj.playgame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Chronometer chronometer;
    private Boolean isGameStart = false;
    /* 利用二维数组创建若干个游戏小方块*/
    private ImageView[][] game_arr = new ImageView[3][3];
    /*游戏主界面*/
    private GridLayout main_game;
    //当前空方块的实例
    private ImageView main_nullImage;
    private GestureDetector gestureDetector;
    private Button return_a;
    private Button tip_a;
    private TextView title;
    private int count;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                changeByDir( getDirByGes(e1.getX(), e1.getY(), e2.getX(), e2.getY()));
                //手势执行一瞬间的方法操作

                //   Toast.makeText(MainActivity.this,""+getDirByGes(e1.getX(),e1.getY(),e2.getX(),e2.getY()),Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        setContentView(R.layout.activity_main);
        title = (TextView)findViewById(R.id.title);
        Intent intent = getIntent();

        chronometer = (Chronometer)findViewById(R.id.chronometer);
        chronometer.start();
        String level = intent.getStringExtra("Level");
        //获取一张大图

      //  MyApp app = (MyApp)getApplication();
       Bitmap big = ((BitmapDrawable)getResources().getDrawable(R.drawable.ceshi)).getBitmap();
     //   Bitmap big = app.getBitmap();
        Bitmap BB =  zoomImg(big, 900, 900);
        int tuWandH = BB.getHeight()/3;
       /* 初始化游戏若干个小方块，利用双重循环*/
        for (int i =0;i<game_arr.length;i++){
            for (int j = 0;j<game_arr[0].length;j++){
                Bitmap bm = Bitmap.createBitmap(BB,j*tuWandH,i*tuWandH,tuWandH,tuWandH);
                //根据行列切成若干个小方块
                game_arr[i][j] = new ImageView(this);
                game_arr[i][j].setImageBitmap(bm);//设置每一个小方块的图案
                game_arr[i][j].setPadding(2, 2, 2, 2);//设置方块之间的间距
                game_arr[i][j].setTag(new GameDate(i, j, bm));//绑定自定义的数据
                game_arr[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean flag = ifHasNullImageView((ImageView) v);
                        if (flag) {//如果是真的相邻关系就交换
                            count++;
                            changeDataByImageView((ImageView) v);
                        }
                    }
                });
            }
        }
/* 初始化游戏主界面并添加若干个小方块，利用双重循环*/
        main_game = (GridLayout)findViewById(R.id.main_game);
        for (int i =0;i<game_arr.length;i++){
            for (int j = 0;j<game_arr[0].length;j++){
                main_game.addView(game_arr[i][j]);
                if (i==0&&j==0){
                    setNullImageView(game_arr[0][0]);
                }
            }

        }
        if (level.equals("esay")){
            randomMove(15);
            isGameStart = true;
            title.setText("ESAY");
        }
        if (level.equals("best")){
            randomMove(30);
            isGameStart = true;
            title.setText("BEST");

        }
        return_a= (Button)findViewById(R.id.return_a);
    //    tip_a =(Button) findViewById(R.id.tip_a);
        return_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    /*随机打乱顺序*/
    public void randomMove(int t){

        for (int i = 0;i<t;i++){
            int type =(int)(Math.random()*4)+1;
            changeByDir(type,false);
        }
    }
    /*设置某个方块为缺口方块*/
    public void setNullImageView(ImageView nullImageView){

        nullImageView.setImageBitmap(null);
        main_nullImage=nullImageView;
    }


   /*   判断点击的方块与空方块的关系 是否是相邻的*/
    public boolean ifHasNullImageView(ImageView clickImageView){
        //分别获取当前空方块的位置和点击方块的位置的实例
        GameDate nullImage = (GameDate) main_nullImage.getTag();
        GameDate gameDate = (GameDate)clickImageView.getTag();

        if (nullImage.y==gameDate.y&&nullImage.x==gameDate.x+1){//上面
            return true;

        }else if (nullImage.y==gameDate.y&&nullImage.x==gameDate.x-1){//下面
            return  true;

        }else if (nullImage.y==gameDate.y+1&&nullImage.x==gameDate.x){//左边
            return  true;

        }else if (nullImage.y==gameDate.y-1&&nullImage.x==gameDate.x){//右边
            return  true;

        }
        return  false;
    }

    /*手势判断 是向哪个方向滑动根据滑动的手势之间的坐标值来决定的 */
    public int getDirByGes(float start_x,float start_y,float end_x,float end_y){
        boolean isLeftorRight = (Math.abs(start_x-end_x)>Math.abs(start_y-end_y))?true:false;
        if(isLeftorRight){
            boolean isLeft = (start_x-end_x)>0?true:false;
            if(isLeft){
                return  3;

            }else{
                return  4;
            }

        }else{
            boolean isup = (start_y-end_y)>0?true:false;
            if(isup){
                return  1;

            }else{
                return  2;

            }
        }

    }
    //根据手势的方向，获取空方块相邻的位置是否存在方块 进行数据交换
    public void changeByDir(int type){
        changeByDir(type,true);
    }
    public void changeByDir(int type,Boolean ishas){
        GameDate nullGameDate = (GameDate)main_nullImage.getTag();
        int new_x = nullGameDate.x;
        int new_y = nullGameDate.y;
        if (type==1){
            new_x++;
        }else if (type==2){
            new_x--;
        }else if (type==3){
            new_y++;
        }else if (type==4){
            new_y--;
        }
        //判断这个新坐标是否存在
        if (new_x>=0&&new_x<game_arr.length&&new_y>=0&&new_y<game_arr[0].length){
            if (ishas) {
                changeDataByImageView(game_arr[new_x][new_y]);
            }else{
                changeDataByImageView(game_arr[new_x][new_y],false);

            }
        }
    }
    /*利用动画结束后交换两个方块的数据*/
    public  void changeDataByImageView( ImageView imageView){
        changeDataByImageView(imageView, true);
    }
    public  void changeDataByImageView(final ImageView imageView,Boolean istrue){

        if (!istrue){
            GameDate gameDate = (GameDate)imageView.getTag();
            main_nullImage.setImageBitmap(gameDate.bm);
            GameDate nullgamedata = (GameDate)main_nullImage.getTag();
            nullgamedata.bm = gameDate.bm;
            nullgamedata.p_x = gameDate.p_x;
            nullgamedata.p_y = gameDate.p_y;
            setNullImageView(imageView);
            if (isGameStart) {
                isGameOver();
            }
            return;
        }
        TranslateAnimation translateAnimation=null;
        if (imageView.getX()>main_nullImage.getX()){//根据点击位置和空方块的位置来设置动画
            translateAnimation = new TranslateAnimation(0.1f,-imageView.getHeight(),0.1f,0.1f);
        }else if(imageView.getX()<main_nullImage.getX()){
            translateAnimation = new TranslateAnimation(0.1f,imageView.getHeight(),0.1f,0.1f);
        }else if(imageView.getY()>main_nullImage.getY()){
            translateAnimation = new TranslateAnimation(0.1f,0.1f,0.1f,-imageView.getHeight());
        }else if(imageView.getY()<main_nullImage.getY()){
            translateAnimation = new TranslateAnimation(0.1f,0.1f,0.1f,imageView.getHeight());
        }
        translateAnimation.setDuration(70);//设置时长
        translateAnimation.setFillAfter(true);//设置是否停留
       /*
        设置动画监听*/
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束之后开始真正交换数据
                imageView.clearAnimation();
                GameDate gameDate = (GameDate)imageView.getTag();
                main_nullImage.setImageBitmap(gameDate.bm);
                GameDate nullgamedata = (GameDate)main_nullImage.getTag();
                nullgamedata.bm = gameDate.bm;
                nullgamedata.p_x = gameDate.p_x;
                nullgamedata.p_y = gameDate.p_y;
                setNullImageView(imageView);
                if (isGameStart) {
                    isGameOver();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(translateAnimation);
    }
    //判断游戏结束 在游戏开始之前是不判断的
    public void isGameOver(){
        Boolean isGameOver = true;
        for (int i = 0;i<game_arr.length;i++){
            for (int j = 0;j<game_arr[0].length;j++){
                if (game_arr[i][j]==main_nullImage){
                    continue;
                }
                GameDate gameDate = (GameDate)game_arr[i][j].getTag();
                if(!gameDate.isTrue()){
                    isGameOver = false;
                    break;
                }

            }
        }
        if (isGameOver){
            Toast.makeText(MainActivity.this,"成功啦",Toast.LENGTH_SHORT).show();
        }
    }
    /*  每个游戏小方块的数据
      实际位置x
      实际位置y
      每个方块的图片bm
      每个小方块图片的位置p_x
      每个小方块图片的位置 p_y*/
    class GameDate{
        public int x = 0;
        public  int y = 0;
        public Bitmap bm;
        public  int p_x=0;
        public int p_y=0;
        public GameDate(int x,int y,Bitmap bm){
            this.bm = bm;
            this.x=x;
            this.y = y;
            this.p_x = x;
            this.p_y = y;

        }
        //判断每个小方块的位置是否正确
        public Boolean isTrue(){
            if (x==p_x&&y==p_y){
                return  true;
            }
            return false;
        }


    }
    //手动改变图片大小来控制显示的大小
    public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片   www.2cto.com
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }
}
