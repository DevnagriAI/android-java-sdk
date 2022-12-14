package com.translationsdk;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.devnagritranslationsdk.interfaces.ResponseListener;
import com.devnagritranslationsdk.network.ResultInterface;
import com.translationsdk.databinding.FragmentFirstBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class FirstFragment extends Fragment {

    String TAG = "FirstFragment";
    private FragmentFirstBinding binding = null;
    static int userSelectedIndex = 0;
    Activity activity;
    Locale locale = null;
    HashMap<String,String> supportableLanguages;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = new NavController(getContext());

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) activity).navController.navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                try {

                    BaseApplication.devNagriTranslationSdk.getAllSupportableLanguages(new ResultInterface() {
                        @Override
                        public void callBack(String resultString) {
                        }

                        @Override
                        public void returnStringHash(HashMap<String, String> supportableLanguages) {

                            ArrayList<String> listOfKeys = new ArrayList();
                            if (supportableLanguages != null) {
                                for (String s : supportableLanguages.keySet()) {
                                    listOfKeys.add(s);
                                }
                                locale = new Locale(supportableLanguages.get(listOfKeys.get(userSelectedIndex)));
                            }

                            CharSequence[] language = listOfKeys.toArray(new CharSequence[listOfKeys.size()]);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(requireContext())
                                            .setTitle(getString(R.string.change_app_language))
                                            .setPositiveButton(getString(R.string.change), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                        Log.d("updateAppLocale_TAG", "updating app locale starting for language: " + locale.getLanguage());
                                                        BaseApplication.devNagriTranslationSdk.updateAppLocale(activity, locale, new ResponseListener() {
                                                            @Override
                                                            public void responseCallback(Boolean isCompleted, String error) {
                                                                Log.d("updateAppLocale_TAG", "updating app locale completed for language: " + locale.getLanguage() + " isCompleted = " + isCompleted + " error: [" + error + "]");
                                                            }
                                                        });
                                                    }
                                                    else
                                                        Log.d("updateAppLocale_TAG", "binding.buttonSecond click: build v is greater than Build.VERSION_CODES.N");
                                                }
                                            }).setSingleChoiceItems(language, userSelectedIndex, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            userSelectedIndex = i;
                                            locale = new Locale(supportableLanguages.get(listOfKeys.get(userSelectedIndex)));
                                        }
                                    }).setCancelable(true).show();
                                }
                            });
                        }
                    });
                }catch (Exception ex){
                    Log.d(TAG, "onClick exception: "+ex);
                }
            }
        });

        printattr();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void printattr(){

    }

    //Here is sample code for - getTranslationOfString() working
                            /*Log.d(TAG, "DevNagri - Need Translation of member");
                            BaseApplication.devNagriTranslationSdk.getTranslationOfString("Member", new StringCallback() {
        @Override
        public void onCallback(String translation) {
            Log.d(TAG, "DevNagri - Translated String: "+translation);
        }
    });

    // test again
    ArrayList<String> aa = new ArrayList<>();
                            aa.add("Family");
                            aa.add("Member");
                            BaseApplication.devNagriTranslationSdk.getTranslationOfStrings(aa, new GenericCallback<List<String>>() {
        @Override
        public void onCallback(List<String> value) {
            Log.d(TAG, "getTranslationOfStrings onCallback: "+value.get(0));
            Log.d(TAG, "getTranslationOfStrings onCallback: "+value.get(1));
        }
    });

                            BaseApplication.devNagriTranslationSdk.updateTranslations();*/
}
