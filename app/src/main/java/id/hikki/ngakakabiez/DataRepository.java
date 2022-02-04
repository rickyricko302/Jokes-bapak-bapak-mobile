package id.hikki.ngakakabiez;

import android.content.Context;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataRepository {
    private Context c;
    private String result;
    public DataRepository(Context c){
        this.c=c;
    }

    public List<String> getData(){
        try{
            List<String> data = new ArrayList<>();
            BufferedReader br = new BufferedReader(new InputStreamReader(c.getAssets().open("jokes-text.json")));
            String g;
            StringBuilder sb = new StringBuilder();
            while ((g=br.readLine()) != null){
                sb.append(g);
            }
            JSONArray ja = new JSONArray(sb.toString());
            for(int i =0;i < ja.length();i++){
                data.add(ja.getString(i));
            }
            return data;
        }catch (Exception e){

        }
        return null;
    }
    public String getImg1(){
        AndroidNetworking.get("https://candaan-api.vercel.app/api/image/random")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.get("status").toString().equals("200")){
                        result = response.getJSONObject("data").getString("url");
                    }
                    else{
                        result = "Error code not 200";
                    }
                } catch (JSONException e) {
                    result = "Error : "+e.getMessage();
                }
            }

            @Override
            public void onError(ANError anError) {
                anError.getMessage();
            }
        });
        return result;
    }
}
