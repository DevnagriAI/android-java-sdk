package com.translationsdk;

import static com.translationsdk.BaseApplication.devNagriTranslationSdk;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.devnagritranslationsdk.DevNagriTranslationSdk;
import com.devnagritranslationsdk.interfaces.GenericCallback;
import com.devnagritranslationsdk.interfaces.StringCallback;
import com.google.android.material.snackbar.Snackbar;
import com.translationsdk.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration.Builder appBarConfiguration;
    private ActivityMainBinding binding;
    NavController navController;
    Context ctx;

    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
         return devNagriTranslationSdk.fetchAppDelegate(this, super.getDelegate());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(this.getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment =
                (NavHostFragment) this.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        navController = navHostFragment.getNavController();
         appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph());

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                devNagriTranslationSdk.getTranslationOfString("Use your action", new StringCallback() {
                    @Override
                    public void onCallback(String it) {
                        Log.d("MainActivity_TAG", "onCallback translated String: "+it);
                       Snackbar.make(view, it, Snackbar.LENGTH_LONG)
                                .setAction("Action", null)
                                .show();
                    }
                });

                // Translated array tested
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("Hello");
                arrayList.add("World");

                devNagriTranslationSdk.getTranslationOfStrings(arrayList, new GenericCallback<List<String>>() {
                    @Override
                    public void onCallback(List<String> value) {
                        if (value != null && value.size() > 0){
                            Log.d("MainActivity_TAG", "onCallback translated array: "+value.toString());
                        }
                    }
                });

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("how_are_you1","How are you1");
                hashMap.put("i_am_good1","I am good1");
                hashMap.put("home1","Home1");
                devNagriTranslationSdk.getTranslationOfMap(hashMap, new GenericCallback<HashMap>() {
                    @Override
                    public void onCallback(HashMap value) {
                        if (value!=null && value.size() >0)
                        Log.d("MainActivity_TAG", "onCallback: "+value.toString());
                    }
                });


                try {
                    JSONObject json = new JSONObject();
                    json.put("Name", "Dakshita Mathur");

                    JSONObject subJson = new JSONObject();
                    subJson.put("FriendName1", "Daraksha Khan");
                    subJson.put("FriendName2", "Garima Nepaliya");
                    subJson.put("FriendName3", "Reenuka Purohit");
                    subJson.put("FriendName4", "Rishabh parihar");
                    subJson.put("FriendName5", "Himanshu Malviya");

                    json.put("Friend", subJson);

                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put("Hello");
                    jsonArray.put("Hi");
                    jsonArray.put("Hola");
                    jsonArray.put("Namaste");

                    json.put("type_of_greetings", jsonArray);

                    try {
                        ArrayList<String> ignoreKeys = new ArrayList<>();
                        ignoreKeys.add("customer_id");
                        devNagriTranslationSdk.getTranslationOfJSON(getJson(), ignoreKeys, new GenericCallback<JSONObject>() {
                            @Override
                            public void onCallback(JSONObject value) {
                                Log.d("translatedJson__tag", "result: "+value);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }catch (Exception exception) {
                    Log.d("check_json_values", "onClick json: "+exception);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
       // return  super.onSupportNavigateUp();
    }

    public JSONObject getJson() {
       try {
           InputStream is = getResources().openRawResource(com.devnagritranslationsdk.R.raw.test_json);
           Writer writer = new StringWriter();
           char[] buffer = new char[1024];
           try {
               Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
               int n;
               while ((n = reader.read(buffer)) != -1) {
                   writer.write(buffer, 0, n);
               }
           } finally {
               is.close();
           }
           String jsonString = writer.toString();
           return new JSONObject(jsonString);
       }catch (Exception exception) {
           Log.d("getJson_excep", "getJson exception: "+exception);
       }

        return new JSONObject();
    }
}