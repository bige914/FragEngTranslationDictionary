package edu.quinnipiac.ser210.fragengtranslationdictionary;
/**
 * HelpFragment fragment, offers instructions for how to use the app
 *
 * @authors Ellsworth Evarts IV, Ania Lightly
 * @date 4/01/2020
 */
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {

    private NavController navController = null;

    public HelpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            navController.navigateUp();
            return true;
        }
        else  return false;
    }
}
