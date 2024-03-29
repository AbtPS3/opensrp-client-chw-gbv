package org.smartregister.chw.gbv.model;

import org.json.JSONObject;
import org.smartregister.chw.gbv.contract.GbvRegisterContract;
import org.smartregister.chw.gbv.dao.GbvDao;
import org.smartregister.chw.gbv.util.GbvJsonFormUtils;

public class BaseGbvRegisterModel implements GbvRegisterContract.Model {

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
        JSONObject jsonObject = GbvJsonFormUtils.getFormAsJson(formName);


        if (jsonObject.has("global")) {
            jsonObject.getJSONObject("global").put("age", GbvDao.getClientAge(entityId));
            jsonObject.getJSONObject("global").put("gender", GbvDao.getClientSex(entityId));
        }

        GbvJsonFormUtils.getRegistrationForm(jsonObject, entityId, currentLocationId);

        return jsonObject;
    }

}
