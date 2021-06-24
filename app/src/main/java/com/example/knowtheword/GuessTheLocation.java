package com.example.knowtheword;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class GuessTheLocation extends AppCompatActivity  implements OnMapReadyCallback {

    int correctChoosenAns=0;
    SupportMapFragment mapFragment;
    int NumOfQuestion =10;
    int currentQuesNum=0;
    LatLng ansLatLang = new LatLng(0,0);
    int CorrectAns=0;
    int NoOfOptions=4;
    Geocoder geocoder;
    private GoogleMap mMap;
    ArrayList<String> countries;
    Button score;

    TextView QuesTionText;
    Button []btn = new Button[4];
    Marker marker;



    public void nextQuestion(View v){

        if(currentQuesNum<NumOfQuestion) {
            CorrectAns = newQuestion();
        }else{
            Intent intent = new Intent(getApplicationContext(),ScoreSheet.class);
            try {
                intent.putExtra("Score", score.getText().toString());
                startActivity(intent);
            }catch (Exception e){
                Log.e("errree", "nextQuestion: " + e.getMessage() );
            }
        }
    }


    @SuppressLint("ResourceAsColor")
    int newQuestion(){
        currentQuesNum++;
        if(marker!=null)
        marker.remove();

        for(int i=0;i<4;i++){
            btn[i].setBackgroundColor(0x738b28);
        }

        ArrayList<String> Options = new ArrayList<>();
        Random rand = new Random();

        for(int i=0;i<NoOfOptions;i++){
            int choice = (rand.nextInt()%countries.size() + countries.size())%countries.size();

            if(choice< countries.size() && !Options.contains(countries.get(choice))){
                Options.add(countries.get(choice));
            }else{
                i--;
                continue;
            }

        }
        int ans = (rand.nextInt()%NoOfOptions +NoOfOptions)%NoOfOptions;


        for(String s : Options){
            Log.i("infooo",s);
        }

        for(int i=0;i<NoOfOptions;i++){
            Log.i("infoo","str = "+i);
            if(ans==i){


                try {

                    while(true){
                        List<Address> addressList = geocoder.getFromLocationName(Options.get(i),1);

                        if(addressList!=null && addressList.size()>0){
                            
                            ansLatLang = new LatLng(addressList.get(0).getLatitude(),addressList.get(0).getLongitude());
                            break;
                        }
                        Log.i("infooo","while true");
                    }



                }catch (Exception e){
                    e.printStackTrace();
                }
                //ansLatLang = new LatLng()
            }
        }

Log.i("infooo",ansLatLang.latitude +" "+ansLatLang.longitude);

        Log.i("info","mamp=== "+mMap);
        if(mMap!=null) {
            marker =  mMap.addMarker(new MarkerOptions().position(ansLatLang));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(ansLatLang));
        }

        for(int i=0;i<NoOfOptions;i++){
            btn[i].setText(Options.get(i));
        }

        return ans ;
    }



    @SuppressLint("ResourceAsColor")
    public void choosenOption(View v){
        Log.i("info = ","ans" + v.getTag());

        if(v.getTag().toString().equals(Integer.toString(CorrectAns))){
            correctChoosenAns++;
            v.setBackgroundColor(R.color.correctOption);
            for(int i=0;i<4;i++) if(i!=CorrectAns) btn[i].setBackgroundColor(R.color.wrongOption);
        }else{
            v.setBackgroundColor(R.color.wrongOption);
            btn[CorrectAns].setBackgroundColor(R.color.correctOption);
        }
        score.setText(correctChoosenAns+"/"+NumOfQuestion);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_the_location);
        geocoder = new Geocoder(getApplicationContext(),Locale.getDefault());

      //  QuesTionText = findViewById(R.id.textView);
        score = findViewById(R.id.score);
        btn[0] = findViewById(R.id.button1);
        btn[1] = findViewById(R.id.button2);
        btn[2] = findViewById(R.id.button3);
        btn[3] = findViewById(R.id.button4);

        Intent intent = getIntent();
        NumOfQuestion = intent.getIntExtra("Number",10);

   //      Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.guessthename", Context.MODE_PRIVATE);
        countries = new ArrayList<String>();


        HashSet<String> set= (HashSet<String>)sharedPreferences.getStringSet("countries",null);
        if(set==null){
            Locale[] locales = Locale.getAvailableLocales();

            for (Locale locale : locales) {

                String country = locale.getDisplayCountry();
                if (country.trim().length()>0 && !countries.contains(country)) {
                    countries.add(country);
                    Log.i("infoo download = ","countru = "+country);
                }
            }

            HashSet<String> hashSet = new HashSet<String>(countries);
            sharedPreferences.edit().putStringSet("countries",hashSet).apply();

        }else {
            countries = new ArrayList<>(set);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        CorrectAns = newQuestion();
//        mMap.addMarker(new MarkerOptions().position(ansLatLang));
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        try {

            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e("abcccc", "Style parsing failed.");
            }else{
                Log.e("abcccc", "Style sucess");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("abccc", "Can't find style. Error: ", e);
        }

    }
}





/*


 int Question(){
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        double latitude;
        int QuesCnt=0;
        LatLng QuesOption = new LatLng(0,0);
        String []QuesLocationOption = new String[4];
        double longitude;
        Random rand = new Random();
        double rand_dub1 =0;
        double rand_dub2 =0;
        int cnt=1000;
        Log.i("val =  outside", String.valueOf(rand_dub1) + " "+ rand_dub2);
        while(cnt-- > 0){

            if(QuesCnt==4) break;

            while(true){
                rand_dub1 = rand.nextDouble();
                rand_dub2 = rand.nextDouble();
                rand_dub1 *= 100;
                rand_dub2 *=200;

                if(rand_dub1 < 90 && rand_dub2< 180) {

                    boolean b = rand.nextBoolean();
                    if(b) rand_dub1 = -1*rand_dub1;

                    b = rand.nextBoolean();
                    if(b) rand_dub2 = -1*rand_dub2;

                    latitude = rand_dub1;
                    longitude = rand_dub2;
                    break;
                }
            }
            Log.i("val = ", String.valueOf(rand_dub1) + " "+ rand_dub2);

            try {
                List<Address> listaddress = geocoder.getFromLocation(latitude,longitude,1);
                if(listaddress!=null && listaddress.size()>0){
                    Log.i("infoo ", String.valueOf(listaddress.get(0)));

                    if(listaddress.get(0).getCountryName()!=null){
                        String temp = listaddress.get(0).getCountryName();
                        boolean flag=true;
                        for(String s : QuesLocationOption){
                            if(s==temp) {
                                flag = false;
                                break;
                            }
                        }

                        if(!flag) continue;

                        QuesLocationOption[QuesCnt] = temp;

                        Log.i("infoo","strin g === "+ QuesLocationOption[QuesCnt]);

                        if(QuesCnt==0){
                            QuesOption = new LatLng(latitude,longitude);
                        }

                        QuesCnt++;
                    }else if(listaddress.get(0).getAddressLine(0)!=null){
                        QuesLocationOption[QuesCnt] = listaddress.get(0).getAddressLine(0);
                        Log.i("infoo","strin g === " + QuesLocationOption[QuesCnt]);
                        QuesCnt++;
                    }


              //      mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(listaddress.get(0).toString()));

                }else{
                    Log.i("info","  prob");
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.e("err", "Question: " + e.getMessage() );
            }

        }

        int ans = (rand.nextInt()%4+4)%4;

        for(int i=0;i<4;i++){
            if(i==ans){
                mMap.addMarker(new MarkerOptions().position(QuesOption));
            }
            btn[i].setText(QuesLocationOption[i]);
        }
        return ans;

















        //   @SuppressLint("ResourceAsColor")
    void choosenOption(View v){
        Log.i("info inside ", "ans = ");
//        CorrectAns = newQuestion();
//
//        if(v.getTag().toString().equals(CorrectAns)){
//            v.setBackgroundColor(android.R.color.holo_green_dark);
//        }else{
//            v.setBackgroundColor(android.R.color.holo_red_dark);
//            btn[CorrectAns].setBackgroundColor(android.R.color.holo_green_dark);
//        }

    }
    }


 */
