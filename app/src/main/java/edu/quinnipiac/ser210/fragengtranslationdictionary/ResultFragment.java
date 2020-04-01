package edu.quinnipiac.ser210.fragengtranslationdictionary;
/**
 * ResultFragment fragment, output area for converted word from English
 * to Chosen language
 *
 * @authors Ellsworth Evarts IV, Ania Lightly
 * @date 4/01/2020
 */
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment {
    private String translation;

    public ResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String result =  translation;
        ((TextView) view.findViewById(R.id.result)).setText(result);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        translation = getArguments().getString("translation");
    }
}
