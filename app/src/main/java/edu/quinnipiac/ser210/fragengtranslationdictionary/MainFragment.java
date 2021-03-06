package edu.quinnipiac.ser210.fragengtranslationdictionary;
/**
 * MainFragment fragment, accepts inputs from TextEdit and Spinner
 * to perform a function related to translation to other languages from English
 *
 * @authors Ellsworth Evarts IV, Ania Lightly
 * @date 4/01/2020
 */
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment{
    private NavController navController = null;
    private TargetLangHandler tLangHandler = new TargetLangHandler();
    private ShareActionProvider provider;
    private boolean userSelect = false;
    private String[] itemStr = new String[2];

    private String url1 = "https://systran-systran-platform-for-language-processing-v1";
    private String url2 = ".p.rapidapi.com/translation/text/translate?";
    private String source = "source=en&";
    private String baseUrl = url1 + url2 + source;
    private String inpt = "";

    private EditText word;
    private Spinner spinner;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = (Spinner) view.findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("onItemSelected", position+"");

                word = (EditText) Objects.requireNonNull(getView()).findViewById(R.id.editText);
                inpt=word.getText().toString();

                if(!inpt.equals("")){
                    userSelect = true;
                }
                if (userSelect) {

                    Log.d("input =", inpt);
                    itemStr[1]=inpt;

                    final String item = (String) parent.getItemAtPosition(position);
                    Log.i("onItemSelected :trgtLng", item);
                    System.out.println("onWordSelect :word " + itemStr[1]);
                    Log.i("onWordSelect :word", itemStr[1]);
                    itemStr[0]=item;

                    //TODO : call of async subclass goes here
                    new FetchTranslation().execute(itemStr);//possible cause of issue? had 'item' in it prior

                    userSelect = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        super.onCreateOptionsMenu(menu, inflater);
        provider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        if (provider == null)
            Log.d("MainFragment", "noshare provider");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home){
            this.navController.navigateUp();
        }

        if (id == R.id.action_settings) {
            /*
            switch (id){
                //case R.id.submenu1: root.setBackgroundColor(ContextCompat.getColor(getActivity)); break;
                //case R.id.submenu2: root.setBackgroundColor(getResources().getColor(android.R.color.background_dark)); break; //Color.parseColor("#222222") Color.BLACK
                default:
                    //return super.onOptionsItemSelected(item);
            }
            */
            return true;
        }

        if(id == R.id.action_help){
            navController.navigate(R.id.action_mainFragment_to_helpFragment);
            return true;
        }

        if (id == R.id.action_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Hi there");
            if (provider != null) {
                provider.setShareIntent(intent);
            } else
                //Toast.makeText(this, "no provider", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    //AsyncTask<Params, Progress, Result>
    private class FetchTranslation extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection =null;
            String translation = "";


            try{
                String target = "target="+params[0]+"&";
                String input = "input="+params[1];

                URL url = new URL(baseUrl + target + input);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("X-RapidAPI-Key","8db1d44a5emsh082121746e4b546p16eb75jsn4e81403eff20");

                urlConnection.connect();


                if (urlConnection.getInputStream() == null) {
                    Log.e("no connection", "no connection");
                    return null;
                }
                translation = getStringFromBuffer(
                        new BufferedReader(new InputStreamReader(urlConnection.getInputStream())));
                assert translation != null;
                Log.d("translation", translation);
            }catch (Exception e){
                Log.e("MainActivity","Error" + e.getMessage());
                return null;
            }finally {
                if(urlConnection !=null)
                    urlConnection.disconnect();
            }

            return translation;
        }

        private String getStringFromBuffer(BufferedReader bufferedReader) throws Exception{
            StringBuffer buffer = new StringBuffer();
            String line;

            while((line = bufferedReader.readLine()) != null){
                buffer.append(line + '\n');

            }
            if (bufferedReader!=null){
                try{
                    bufferedReader.close();
                }catch (IOException e){
                    Log.e("MainActivity","Error" + e.getMessage());
                    return null;
                }
            }
            Log.d("translation", buffer.toString());
            return  tLangHandler.getTranslation(buffer.toString());
        }

        @Override
        protected void onPostExecute(String result) {

            if(result != null){
                Bundle bundle = new Bundle();
                Log.d("onPostExecute",result);

                bundle.putString("translation", result);

                //reset edit text field
                word.getText().clear();
                //reset spinner to default
                spinner.setSelection(0);

                //pass along display data of translated word to resultFragment and move to said Fragment
                navController.navigate(R.id.action_mainFragment_to_resultFragment, bundle);

            }else
                Log.d("onPostExecute","null");
        }
    }

}
