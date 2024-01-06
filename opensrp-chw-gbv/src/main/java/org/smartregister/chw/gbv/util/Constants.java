package org.smartregister.chw.gbv.util;

public interface Constants {

    int REQUEST_CODE_GET_JSON = 2244;
    String ENCOUNTER_TYPE = "encounter_type";
    String STEP_ONE = "step1";
    String STEP_TWO = "step2";

    interface JSON_FORM_EXTRA {
        String JSON = "json";
        String ENCOUNTER_TYPE = "encounter_type";

        String DELETE_EVENT_ID = "deleted_event_id";

        String DELETE_FORM_SUBMISSION_ID = "deleted_form_submission_id";
    }

    interface EVENT_TYPE {
        String SBC_REGISTRATION = "SBC Registration";
        String SBC_FOLLOW_UP_VISIT = "SBC Follow-up Visit";

        String SBC_HEALTH_EDUCATION_MOBILIZATION = "SBC Health Education Mobilization";

        String SBC_MONTHLY_SOCIAL_MEDIA_REPORT = "Monthly Social Media Report";

        String VOID_EVENT = "Void Event";

        String DELETE_EVENT = "Delete Event";
    }

    interface FORMS {
        String GBV_VISIT_TYPE = "gbv_visit_type";

        String GBV_CONSENT_FORM = "gbv_consent";

        String GBV_CONSENT_FOLLOWUP_FORM = "gbv_consent_followup";

        String GBV_HISTORY_COLLECTION_FORM = "gbv_history_collection";

        String GBV_MEDICAL_EXAMINATION_FORM = "gbv_medical_examination";

        String GBV_PHYSICAL_EXAMINATION_FORM = "gbv_physical_examination";

        String GBV_FORENSIC_EXAMINATION_FORM = "gbv_forensic_examination";

        String GBV_LAB_INVESTIGATION_FORM = "gbv_lab_investigation";

        String GBV_EDUCATION_AND_COUNSELLING_FORM = "gbv_education_and_counselling";

        String GBV_SAFETY_PLAN = "gbv_safety_plan";

        String GBV_REFERRAL_AND_LINKAGE = "gbv_referral_and_linkage";

        String GBV_NEXT_APPOINTMENT_DATE = "gbv_next_appointment_date";
    }

    interface TABLES {
        String SBC_REGISTER = "ec_sbc_register";

        String SBC_FOLLOW_UP = "ec_sbc_follow_up_visit";

        String SBC_MOBILIZATION_SESSIONS = "ec_sbc_mobilization_session";

        String SBC_MONTHLY_SOCIAL_MEDIA_REPORT = "ec_sbc_monthly_social_media_report";

    }

    interface ACTIVITY_PAYLOAD {
        String BASE_ENTITY_ID = "BASE_ENTITY_ID";
        String FAMILY_BASE_ENTITY_ID = "FAMILY_BASE_ENTITY_ID";
        String ACTION = "ACTION";
        String SBC_FORM_NAME = "SBC_FORM_NAME";
        String EDIT_MODE = "editMode";
        String MEMBER_PROFILE_OBJECT = "MemberObject";

    }

    interface ACTIVITY_PAYLOAD_TYPE {
        String REGISTRATION = "REGISTRATION";
        String FOLLOW_UP_VISIT = "FOLLOW_UP_VISIT";
    }

    interface CONFIGURATION {
        String SBC_REGISTRATION_CONFIGURATION = "sbc_registration";
    }

}