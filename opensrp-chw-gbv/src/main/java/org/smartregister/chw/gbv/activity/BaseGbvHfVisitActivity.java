package org.smartregister.chw.gbv.activity;

import static com.vijay.jsonwizard.constants.JsonFormConstants.COUNT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.Form;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.chw.gbv.GbvLibrary;
import org.smartregister.chw.gbv.R;
import org.smartregister.chw.gbv.adapter.BaseGbvVisitAdapter;
import org.smartregister.chw.gbv.contract.BaseGbvVisitContract;
import org.smartregister.chw.gbv.dao.GbvDao;
import org.smartregister.chw.gbv.domain.MemberObject;
import org.smartregister.chw.gbv.interactor.BaseGbvHfVisitInteractor;
import org.smartregister.chw.gbv.model.BaseGbvVisitAction;
import org.smartregister.chw.gbv.presenter.BaseGbvVisitPresenter;
import org.smartregister.chw.gbv.util.Constants;
import org.smartregister.view.activity.SecuredActivity;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import timber.log.Timber;

public class BaseGbvHfVisitActivity extends SecuredActivity implements BaseGbvVisitContract.View, View.OnClickListener {

    private static final String TAG = BaseGbvHfVisitActivity.class.getCanonicalName();
    protected Map<String, BaseGbvVisitAction> actionList = new LinkedHashMap<>();
    protected BaseGbvVisitContract.Presenter presenter;
    protected MemberObject memberObject;
    protected String baseEntityID;
    protected Boolean isEditMode = false;
    protected RecyclerView.Adapter mAdapter;
    protected ProgressBar progressBar;
    protected TextView tvSubmit;
    protected TextView tvTitle;
    protected String current_action;
    protected String confirmCloseTitle;
    protected String confirmCloseMessage;

    public static void startMe(Activity activity, String baseEntityID, Boolean isEditMode) {
        Intent intent = new Intent(activity, BaseGbvHfVisitActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityID);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.EDIT_MODE, isEditMode);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_gbv_visit);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isEditMode = getIntent().getBooleanExtra(Constants.ACTIVITY_PAYLOAD.EDIT_MODE, false);
            baseEntityID = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID);
            memberObject = getMemberObject(baseEntityID);
        }

        confirmCloseTitle = getString(R.string.confirm_form_close);
        confirmCloseMessage = getString(R.string.confirm_form_close_explanation);
        setUpView();
        displayProgressBar(true);
        registerPresenter();
        if (presenter != null) {
            if (StringUtils.isNotBlank(baseEntityID)) {
                presenter.reloadMemberDetails(baseEntityID);
            } else {
                presenter.initialize();
            }
        }
    }

    protected MemberObject getMemberObject(String baseEntityId) {
        return GbvDao.getMember(baseEntityId);
    }

    public void setUpView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        findViewById(R.id.close).setOnClickListener(this);
        tvSubmit = findViewById(R.id.customFontTextViewSubmit);
        tvSubmit.setOnClickListener(this);
        tvTitle = findViewById(R.id.customFontTextViewName);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new BaseGbvVisitAdapter(this, this, (LinkedHashMap) actionList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        redrawVisitUI();
    }

    protected void registerPresenter() {
        presenter = new BaseGbvVisitPresenter(memberObject, this, new BaseGbvHfVisitInteractor());
    }

    @Override
    public void initializeActions(LinkedHashMap<String, BaseGbvVisitAction> map) {
        actionList.clear();
        //Necessary evil to rearrange the actions according to a specific arrangement
        if (map.containsKey(getString(R.string.gbv_visit_type_action_title))) {
            actionList.put(getString(R.string.gbv_visit_type_action_title), map.get(getString(R.string.gbv_visit_type_action_title)));
        }

        if (map.containsKey(getString(R.string.gbv_consent_action_title))) {
            actionList.put(getString(R.string.gbv_consent_action_title), map.get(getString(R.string.gbv_consent_action_title)));
        }

        if (map.containsKey(getString(R.string.gbv_consent_followup_action_title))) {
            actionList.put(getString(R.string.gbv_consent_followup_action_title), map.get(getString(R.string.gbv_consent_followup_action_title)));
        }

        if (map.containsKey(getString(R.string.gbv_history_collection_title))) {
            actionList.put(getString(R.string.gbv_history_collection_title), map.get(getString(R.string.gbv_history_collection_title)));
        }

        if (map.containsKey(getString(R.string.gbv_medical_examination_title))) {
            actionList.put(getString(R.string.gbv_medical_examination_title), map.get(getString(R.string.gbv_medical_examination_title)));
        }

        if (map.containsKey(getString(R.string.gbv_physical_examination_title))) {
            actionList.put(getString(R.string.gbv_physical_examination_title), map.get(getString(R.string.gbv_physical_examination_title)));
        }

        if (map.containsKey(getString(R.string.gbv_forensic_examination_title))) {
            actionList.put(getString(R.string.gbv_forensic_examination_title), map.get(getString(R.string.gbv_forensic_examination_title)));
        }

        if (map.containsKey(getString(R.string.gbv_lab_investigation_title))) {
            actionList.put(getString(R.string.gbv_lab_investigation_title), map.get(getString(R.string.gbv_lab_investigation_title)));
        }

        if (map.containsKey(getString(R.string.gbv_provide_treatment_title))) {
            actionList.put(getString(R.string.gbv_provide_treatment_title), map.get(getString(R.string.gbv_provide_treatment_title)));
        }

        if (map.containsKey(getString(R.string.gbv_education_and_counselling_title))) {
            actionList.put(getString(R.string.gbv_education_and_counselling_title), map.get(getString(R.string.gbv_education_and_counselling_title)));
        }

        if (map.containsKey(getString(R.string.gbv_safety_plan_title))) {
            actionList.put(getString(R.string.gbv_safety_plan_title), map.get(getString(R.string.gbv_safety_plan_title)));
        }

        if (map.containsKey(getString(R.string.gbv_linkage_title))) {
            actionList.put(getString(R.string.gbv_linkage_title), map.get(getString(R.string.gbv_linkage_title)));
        }

        if (map.containsKey(getString(R.string.gbv_next_appointment_date_title))) {
            actionList.put(getString(R.string.gbv_next_appointment_date_title), map.get(getString(R.string.gbv_next_appointment_date_title)));
        }
        //====================End of Necessary evil ====================================


        for (Map.Entry<String, BaseGbvVisitAction> entry : map.entrySet()) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    actionList.putIfAbsent(entry.getKey(), entry.getValue());
                }
            } catch (Exception e) {
                Timber.e(e);
            }
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        displayProgressBar(false);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void displayToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Boolean getEditMode() {
        return isEditMode;
    }

    @Override
    public void setEditMode(boolean editMode) {
        this.isEditMode = editMode;
    }

    @Override
    public void onMemberDetailsReloaded(MemberObject memberObject) {
        this.memberObject = memberObject;
        presenter.initialize();
        redrawHeader(memberObject);
    }

    @Override
    protected void onCreation() {
        Timber.v("Empty onCreation");
    }

    @Override
    protected void onResumption() {
        Timber.v("Empty onResumption");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.close) {
            displayExitDialog(() -> close());
        } else if (v.getId() == R.id.customFontTextViewSubmit) {
            submitVisit(Constants.SaveType.SUBMIT_AND_CLOSE);
        }
    }

    @Override
    public BaseGbvVisitContract.Presenter presenter() {
        return presenter;
    }

    @Override
    public Form getFormConfig(JSONObject jsonObject) {
        Form form = new Form();
        try {
            if (jsonObject.getInt(COUNT) > 1) {
                form.setWizard(jsonObject.getInt(COUNT) > 1);
                form.setName(jsonObject.getString("encounter_type"));
            }
        } catch (JSONException e) {
            Timber.e(e);
            form.setWizard(false);
        }
        return form;
    }

    @Override
    public void startForm(BaseGbvVisitAction baseGbvVisitAction) {
        current_action = baseGbvVisitAction.getTitle();

        if (StringUtils.isNotBlank(baseGbvVisitAction.getJsonPayload())) {
            try {
                JSONObject jsonObject = new JSONObject(baseGbvVisitAction.getJsonPayload());
                startFormActivity(jsonObject);
            } catch (Exception e) {
                Timber.e(e);
                String locationId = GbvLibrary.getInstance().context().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID);
                presenter().startForm(baseGbvVisitAction.getFormName(), memberObject.getBaseEntityId(), locationId);
            }
        } else {
            String locationId = GbvLibrary.getInstance().context().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID);
            presenter().startForm(baseGbvVisitAction.getFormName(), memberObject.getBaseEntityId(), locationId);
        }
    }

    @Override
    public void startFormActivity(JSONObject jsonForm) {
        Intent intent = new Intent(this, JsonFormActivity.class);
        intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, jsonForm.toString());

        if (getFormConfig(jsonForm) != null) {
            intent.putExtra(JsonFormConstants.JSON_FORM_KEY.FORM, getFormConfig(jsonForm));
        }

        startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @Override
    public void startFragment(BaseGbvVisitAction baseGbvVisitAction) {
        current_action = baseGbvVisitAction.getTitle();

        if (baseGbvVisitAction.getDestinationFragment() != null)
            baseGbvVisitAction.getDestinationFragment().show(getSupportFragmentManager(), current_action);

    }

    @Override
    public void redrawHeader(MemberObject memberObject) {
        tvTitle.setText(MessageFormat.format("{0}, {1} \u00B7 {2}", memberObject.getFullName(), String.valueOf(memberObject.getAge()), getString(R.string.gbv_visit)));
    }

    @Override
    public void redrawVisitUI() {
        boolean valid = actionList.size() > 0;
        for (Map.Entry<String, BaseGbvVisitAction> entry : actionList.entrySet()) {
            BaseGbvVisitAction action = entry.getValue();
            if (
                    (!action.isOptional() && (action.getActionStatus() == BaseGbvVisitAction.Status.PENDING && action.isValid()))
                            || !action.isEnabled()
            ) {
                valid = false;
                break;
            }
        }

        int res_color = valid ? R.color.white : R.color.light_grey;
        tvSubmit.setTextColor(getResources().getColor(res_color));
        tvSubmit.setOnClickListener(valid ? this : null); // update listener to null

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayProgressBar(boolean state) {
        progressBar.setVisibility(state ? View.VISIBLE : View.GONE);
    }


    @Override
    public Map<String, BaseGbvVisitAction> getBaseGbvVisitActions() {
        return actionList;
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void submittedAndClose() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        close();
    }

    @Override
    public BaseGbvVisitContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void submitVisit(Constants.SaveType saveType) {
        getPresenter().submitVisit(saveType);
    }

    @Override
    public void onDialogOptionUpdated(String jsonString) {
        BaseGbvVisitAction baseGbvVisitAction = actionList.get(current_action);
        if (baseGbvVisitAction != null) {
            baseGbvVisitAction.setJsonPayload(jsonString);
        }

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            redrawVisitUI();
        }
    }

    @Override
    public Context getMyContext() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_GET_JSON) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String jsonString = data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON);
                    BaseGbvVisitAction baseGbvVisitAction = actionList.get(current_action);
                    if (baseGbvVisitAction != null) {
                        baseGbvVisitAction.setJsonPayload(jsonString);
                    }
                } catch (Exception e) {
                    Timber.e(e);
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {

                BaseGbvVisitAction baseGbvVisitAction = actionList.get(current_action);
                if (baseGbvVisitAction != null)
                    baseGbvVisitAction.evaluateStatus();
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        // update the adapter after every payload
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            redrawVisitUI();
        }
    }

    @Override
    public void onBackPressed() {
        displayExitDialog(BaseGbvHfVisitActivity.this::finish);
    }

    protected void displayExitDialog(final Runnable onConfirm) {
        AlertDialog dialog = new AlertDialog.Builder(this, com.vijay.jsonwizard.R.style.AppThemeAlertDialog).setTitle(confirmCloseTitle)
                .setMessage(confirmCloseMessage).setNegativeButton(com.vijay.jsonwizard.R.string.yes, (dialog1, which) -> {
                    if (onConfirm != null) {
                        onConfirm.run();
                    }
                }).setPositiveButton(com.vijay.jsonwizard.R.string.no, (dialog2, which) -> Timber.d("No button on dialog in %s", JsonFormActivity.class.getCanonicalName())).create();

        dialog.show();
    }

}