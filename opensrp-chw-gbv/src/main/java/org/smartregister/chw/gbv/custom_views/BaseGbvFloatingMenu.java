package org.smartregister.chw.gbv.custom_views;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.smartregister.chw.gbv.R;
import org.smartregister.chw.gbv.domain.MemberObject;
import org.smartregister.chw.gbv.fragment.BaseGbvCallDialogFragment;

public class BaseGbvFloatingMenu extends LinearLayout implements View.OnClickListener {
    private MemberObject MEMBER_OBJECT;

    public BaseGbvFloatingMenu(Context context, MemberObject MEMBER_OBJECT) {
        super(context);
        initUi();
        this.MEMBER_OBJECT = MEMBER_OBJECT;
    }

    protected void initUi() {
        inflate(getContext(), R.layout.view_gbv_floating_menu, this);
        FloatingActionButton fab = findViewById(R.id.gbv_fab);
        if (fab != null)
            fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.gbv_fab) {
            Activity activity = (Activity) getContext();
            BaseGbvCallDialogFragment.launchDialog(activity, MEMBER_OBJECT);
        }  else if (view.getId() == R.id.gbv_refer_to_facility_layout) {
            Activity activity = (Activity) getContext();
            BaseGbvCallDialogFragment.launchDialog(activity, MEMBER_OBJECT);
        }
    }
}