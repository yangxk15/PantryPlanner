package edu.dartmouth.cs.pantryplanner.app.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.model.Recipe;
import edu.dartmouth.cs.pantryplanner.app.util.RequestCode;

/**
 * Created by Lucidity on 17/2/27.
 */

public class MyDialogFragment extends DialogFragment implements DialogInterface.OnClickListener,
                                    Button.OnClickListener {
    private int mId;
    private final static String dialogKey = "DIALOG_KEY";
    Activity container;

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

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View view;

        switch (mId) {
            case 0:
                view = container.getLayoutInflater().inflate(R.layout.fragment_dialog, null);
                mBuilder.setView(view);
                mBuilder.setTitle("Create an entry");
                mBuilder.setPositiveButton("Save", this);
                mBuilder.setNegativeButton("Cancel", null);
                return mBuilder.create();
            case 1:
                view = container.getLayoutInflater().inflate(R.layout.fragment_dialog_two_choice, null);
                mBuilder.setView(view);
                Button btnCreate = (Button) view.findViewById(R.id.button_dialog_create);
                btnCreate.setOnClickListener(this);
                Button btnImport = (Button) view.findViewById(R.id.button_dialog_import);
                btnImport.setOnClickListener(this);
                return mBuilder.create();
            case 2:
                view = container.getLayoutInflater().inflate(R.layout.fragment_dialog, null);
                mBuilder.setView(view);
                mBuilder.setTitle("Create an entry");
                mBuilder.setPositiveButton("Save", this);
                mBuilder.setNegativeButton("Cancel", null);
                return mBuilder.create();
            default:
                return null;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (getActivity() == null || isDetached() || isRemoving()) {
            this.dismiss();
        }
        switch (v.getId()) {
            case R.id.button_dialog_create:
                getActivity().startActivityForResult(
                        new Intent(getActivity(), CreateRecipeActivity.class),
                                RequestCode.CREATE_RECIPE.ordinal()
                        );
                this.dismiss();
                break;
            case R.id.button_dialog_import:
                getActivity().startActivityForResult(
                        new Intent(getActivity(), ExploreRecipeActivity.class),
                                RequestCode.IMPORT_RECIPE.ordinal()
                );
                this.dismiss();
                break;
        }
    }
}
