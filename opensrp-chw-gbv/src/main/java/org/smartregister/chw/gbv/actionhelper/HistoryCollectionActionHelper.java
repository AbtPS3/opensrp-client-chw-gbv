package org.smartregister.chw.gbv.actionhelper;

import static org.smartregister.client.utils.constants.JsonFormConstants.GLOBAL;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.gbv.domain.MemberObject;
import org.smartregister.chw.gbv.domain.VisitDetail;
import org.smartregister.chw.gbv.model.BaseGbvVisitAction;
import org.smartregister.chw.gbv.util.JsonFormUtils;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

public abstract class HistoryCollectionActionHelper extends GbvVisitActionHelper {
    private MemberObject memberObject;

    private String numberOfWitnesses;

    private String currentPregnancyStatus;

    private String typeOfAssault;

    private String hivStatus;

    private JSONObject jsonForm;


    public HistoryCollectionActionHelper(MemberObject memberObject) {
        this.memberObject = memberObject;
    }

    @Override
    public void onJsonFormLoaded(String jsonString, Context context, Map<String, List<VisitDetail>> details) {
        super.onJsonFormLoaded(jsonString, context, details);
        try {
            jsonForm = new JSONObject(jsonString);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    /**
     * Update the preprocessed form to pass client age as global parameter
     *
     * @return the updated form
     */
    @Override
    public String getPreProcessed() {
        try {
            JSONObject global = jsonForm.getJSONObject(GLOBAL);
            global.put("age", memberObject.getAge());
            global.put("gender", memberObject.getGender());
            return jsonForm.toString();
        } catch (JSONException e) {
            Timber.e(e);
        }
        return null;
    }

    @Override
    public void onPayloadReceived(String jsonPayload) {
        JSONObject payload;
        try {
            payload = new JSONObject(jsonPayload);
            numberOfWitnesses = JsonFormUtils.getValue(payload, "no_of_witnesses");
            currentPregnancyStatus = JsonFormUtils.getValue(payload, "current_pregnancy_status");
            typeOfAssault = JsonFormUtils.getValue(payload, "type_of_assault");
            hivStatus = JsonFormUtils.getValue(payload, "hiv_status");
            processHistoryCollection(currentPregnancyStatus, typeOfAssault,hivStatus);
        } catch (JSONException e) {
            Timber.d(e);
        }
    }

    public abstract void processHistoryCollection(String currentPregnancyStatus, String typeOfAssault, String hivStatus);

    @Override
    public String evaluateSubTitle() {
        return null;
    }

    @Override
    public BaseGbvVisitAction.Status evaluateStatusOnPayload() {
        if (StringUtils.isNotBlank(numberOfWitnesses)) {
            return BaseGbvVisitAction.Status.COMPLETED;
        } else
            return BaseGbvVisitAction.Status.PENDING;
    }

    public void setMemberObject(MemberObject memberObject) {
        this.memberObject = memberObject;
    }
}
