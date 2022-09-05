package com.test;

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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.devnagritranslationsdk.network.ResultInterface;
import com.test.databinding.FragmentFirstBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    Activity activity;
    Locale locale = null;
    HashMap<String,String> supportableLanguages;
    static int userSelectedIndex = 0;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        activity = (MainActivity) getActivity();
    }
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
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
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                                                        BaseApplication.devNagriTranslationSdk.updateAppLocale(activity, locale);
                                                    else
                                                        Log.d("sohan", "binding.buttonSecond click: build v is greater than Build.VERSION_CODES.N");
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
                    Log.d("sohan", "onClick exception: "+ex);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}