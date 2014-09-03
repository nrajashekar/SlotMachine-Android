package com.example.nrajashekar.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.nrajashekar.slotmachine.R;

import org.w3c.dom.Text;

/**
 * Created by nrajashekar on 9/2/14.
 */
public class PrizeDialogFragment extends android.support.v4.app.DialogFragment {

    public static final String DIALOG_ID = PrizeDialogFragment.class.getName();

    private static final String TextID = "textId";

    private int _textId;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        _textId = getArguments().getInt(TextID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prize_dialog, container);
        TextView prizeTextView = (TextView) view.findViewById(R.id.prize_text);
        prizeTextView.setText(getString(_textId));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    public static PrizeDialogFragment newInstance(int textId) {
        PrizeDialogFragment prizeDialogFragment = new PrizeDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(TextID, textId);
        prizeDialogFragment.setArguments(args);
        return prizeDialogFragment;
    }
}
