package edu.dartmouth.cs.pantryplanner.app.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import edu.dartmouth.cs.pantryplanner.app.R;

/**
 * Created by Lucidity on 17/2/27.
 */

public class MyDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
    private int mId;
    private final static String dialogKey = "DIALOG_KEY";

    public static MyDialogFragment newInstance(int id) {
        MyDialogFragment dialogFragment = new MyDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(dialogKey, id);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        mId = getArguments().getInt(dialogKey);
        final Activity container = getActivity();

        AlertDialog.Builder mDialog = new AlertDialog.Builder(container);
        View view = container.getLayoutInflater().inflate(R.layout.fragment_dialog, null);
        mDialog.setView(view);

        mDialog.setTitle("Create an entry");
        mDialog.setPositiveButton("Save", this);
        mDialog.setNegativeButton("Cancel", null);

        switch (mId) {
            default:
                Log.d("dialog", "create");
                return mDialog.create();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                switch (mId) {
                    default:
                        return;
                }
        }
    }
}
