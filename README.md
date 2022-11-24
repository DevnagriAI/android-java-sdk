# Android Java Production SDK
# SDK Integration Steps:
# Introduction

Devnagri Over the Air for Java Android lets you update translations in your Android app without having to release it every single time.

By including our SDK, your app will check for updated translations in Devnagri regularly and download them in the background.

# Include the SDK
As a first step a new maven repository needs to be added to your default dependency resolution file set by your project, it can be either your project build.gradle file or settings.gradle file:

        repositories {

            ...

            maven { url 'https://jitpack.io' }

        }
	

Add the below dependency in your app build.gradle file:

	dependencies {
	
	    ...
	    
	    implementation ('com.github.DevnagriAI:android-java-sdk:1.0.5@aar') { transitive(true) }
	    
	}
        
       

 
# Compatibility
 Use Gradle JDK version 11. This SDK support only Java language. Target/Compile SDK should be more then 30. This SDK minimum support is android 8
 
# Configuration

Initialise the SDK in your application class and add the API_KEY from DevNagri. 
  
    public class MainApplication extends Application {
    
    static DevNagriTranslationSdk devNagriTranslationSdk;
    
    public void onCreate() {
        super.onCreate();
            
        int sync_Time = 240;  //In Minutes
          
        Field[] strings = R.string.class.getFields();
        Field[] arrays = R.array.class.getFields();
        Field[] plurals = null;
	  
	      // passing arrays and plurals in init method is optional here, pass them only if defined in strings.xml file
        try {
               devNagriTranslationSdk = new DevNagriTranslationSdk();
               devNagriTranslationSdk.init(getApplicationContext(), API_KEY, sync_Time, strings, arrays, plurals, new ResponseListener() {
                 @Override
                 public void responseCallback(Boolean isCompleted, String error) {
                     Log.d(TAG, "DevNagri Sdk initialized");
                 }
               });
           } catch (Exception e) {
                  e.printStackTrace();
          }	  
      }
    }
 

Additionally, you need to inject the SDK in each activity, e.g. by creating a base activity which all other activities inherit from:

    public class BaseActivity extends AppCompatActivity
    {
        @NonNull
        @Override
        public AppCompatDelegate getDelegate() {
            return MainApplication.devNagriTranslationSdk.fetchAppDelegate(this, super.getDelegate());
        }
    }

# Default Localisation Override
   The SDK override the functionality of @string and getString by default. 

# Usage
Translations can be used as usual in layouts:

	<TextView android:text="@string/translation_key" />

And inside code:

	TextView text = (TextView)findViewById(R.id.text_id);
	text.setText(R.string.translation_key);

# Change Language

In case you don't want to use the system language, you can set a different language in the updateAppLocale method. The language code (locale) needs to be present in a release from Devnagri.


    Locale locale = new Locale("hi");
    BaseApplication.devNagriTranslationSdk.updateAppLocale(activity, locale, new ResponseListener() {
      @Override
      public void responseCallback(Boolean isCompleted, String error) {
	//callback received
      }
    });

Please note that you will get the english text back if your device language is english or you have not set any specific language for the SDK. To get the translation in Hindi, Please update app locale to Hindi as per above method.

# Get Supported Languages

You can get supported languages for the SDK using this method.
This will return hashmap of language name and language code

	BaseApplication.devNagriTranslationSdk.getAllSupportableLanguages(new ResultInterface() {
        @Override
        public void callBack(String resultString) {
           // Wil send any specific message if there is
        }

        @Override
        public void returnStringHash(HashMap<String, String> supportableLanguages) {
          // Do your stuff here, this will send hashmap of supported languages
          
        }
    });
 

# Translate String, List and Map on runtime

You can use these methods anywhere in your project and these will provide translation for current active locale in callback method.

# Get Translation of a Specific String.

    BaseApplication.devNagriTranslationSdk.getTranslationOfString("Use your action", new StringCallback() {
          @Override
          public void onCallback(String translation) {
        	   // use translated text here       
          }            
    });
 

# Get Translations of an Array of Strings.

    ArrayList<String> arrayList = new ArrayList<>();
    arrayList.add("Hello");
    arrayList.add("World");

    devNagriTranslationSdk.getTranslationOfStrings(arrayList, new GenericCallback<List<String>>() {
        @Override
        public void onCallback(List<String> value) {
            if (value != null && value.size() > 0){
                Log.d("TAG", "onCallback translated array: "+value.toString());
                // Do your stuff here.
            }
        }
      });

 
 
# Get Translations Of HashMap 

    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.put("how_are_you","How are you");
    hashMap.put("i_am_good","I am good");
    hashMap.put("home","Home");
    devNagriTranslationSdk.getTranslationOfMap(hashMap, new GenericCallback<HashMap>() {
        @Override
        public void onCallback(HashMap value) {
            if (value!=null && value.size() >0)
              Log.d("TAG", "onCallback: "+value.toString());
        }
    });
 
# Get Translations Of JSON Object

  	// This method is used to convert the entire JSON object to requested language 
  	// ignoreKeys -> Send a list of strings that you want to ignore during the conversion process 

  	JSONObject json = new JSONObject();
  	json.put("Name", "DevNagri SDK");
  	json.put("Language", "Java");
  
  	devNagriTranslationSdk.getTranslationOfJSON(jsonObjectValue, 
		ignoreKeys,
		new GenericCallback<JSONObject>() {
		@Override
		public void onCallback(JSONObject translatedJsonObject) {
			//Here you find translated JSON Object
		}
	});


 
 
