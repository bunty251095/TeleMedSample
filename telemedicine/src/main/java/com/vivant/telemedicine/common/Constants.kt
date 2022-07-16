package com.vivant.telemedicine.common

import android.os.Environment

object Constants {

    //for PRODUCTION
/*    const val environment: String = "PROD"
    const val JITSI_URL: String = "https://jitsi.vivant.me"
    const val strBaseUrl: String = "https://telecore.pmcare.my/"*/

    //for UAT
    const val environment: String = "UAT"
    const val JITSI_URL: String = "https://jitsi.vivant.me"
    const val strBaseUrl: String = "https://pmcareuatcore.vivant.me/"

    const val VERSION_NAME = "1.3.3"

    //Login Api
    const val strLoginUrl: String = "PHR/api/Person/SSOLogin"

    //Notification Api's
    const val LIST_NOTIFICATIONS_API : String = "MESSAGING/api/TeleNotification/List"
    const val UPDATE_NOTIFICATION_API : String = "MESSAGING/api/TeleNotification/Update"

    //Health Records Api's
    const val LIST_DOCUMENT_API : String = "PHR/api/Document/ListDocument"
    const val DOWNLOAD_DOCUMENT_API : String = "PHR/api/Document/GetByHealthDocumentID"
    const val DELETE_DOCUMENT_API : String = "PHR/api/Document/Delete"
    const val SAVE_DOCUMENT_API : String = "PHR/api/Document/SaveDocuments"
    const val SHARE_DOCUMENT_API : String = "CRM/api/Document/Share"
    const val REMOVE_SHARED_DOCUMENT_API : String = "CRM/api/Document/RemoveSharedDocument"

    const val GET_CONSULTATION_TEMPLATE_API : String = "CRM/api/TeleConsultation/GetConsultationTemplate"
    const val LIST_SEARCHED_DOCTORS_API = "CRM/api/DoctorLookup/ListSearchedDoctors"
    const val GET_DOCTOR_SLOTS_API : String = "CRM/api/DoctorProfile/GetDoctorSlots"

    //Appointment Api's
    const val GET_WALLET_API : String = "CRM/api/customer/GetWallet"
    const val UPDATE_WALLET_API : String = "CRM/api/customer/UpdateWallet"
    const val BOOK_APPOINTMENTS_API : String = "PHR/api/Patient/BookAppointment"
    const val GENERATE_ON_BOOKING_API : String = "CRM/api/Order/GenerateOnBooking"
    const val LIST_APPOINTMENTS_API : String = "CRM/api/Customer/ListAppointments"
    const val LIST_CONSULTATION_API : String = "PHR/api/Patient/ListConsultation"
    const val RESCHEDULE_APPOINTMENTS_API : String = "CRM/api/Customer/RescheduleAppointment"
    const val CANCEL_APPOINTMENTS_API : String = "CRM/api/Customer/UpdateStatus"
    const val DOWNLOAD_INVOICE_API : String = "proxy/Invoice/DownloadInvoice"
    const val DOWNLOAD_PRESCRIPTION_API : String = "proxy/Prescription/DownloadPrescription"
    const val GET_CONSULTATION_HEALTH_DOCUMENT_API : String = "PHR/api/Document/GetConsultationHealthDocument"
    const val SAVE_FEEDBACK_API : String = "CRM/api/DoctorProfile/SaveFeedback"
    const val SAVE_CONSULTATION_REQUEST_API : String = "CRM/api/TeleConsultation/SaveConsultationRequest"
    const val CHECKOUT_APPOINTMENT_API : String = "CRM/api/Payment/CheckoutAppointment"
    const val GET_CONSULTATION_REQUEST_API : String = "CRM/api/TeleConsultation/GetConsultationRequest"
    const val JOIN_ROOM_API : String = "CRM/api/TeleConsultation/JoinRoom"
    const val UPDATE_CONSULTATION_REQUEST_API : String = "CRM/api/TeleConsultation/UpdateConsultationRequest"
    const val GET_REQUEST_DETAILS_API : String = "CRM/api/DoctorProfile/GetRequestDetails"

    const val PATIENT_STATE_LIST_API : String = "proxy/Patient/PatientStateList"
    const val SEND_EPHARMA_DETAILS_API : String = "proxy/Patient/SendEpharmaDetails"

    //const val JITSI_URL: String = "https://meet.jit.si"

    val primaryStorage = Environment.getExternalStorageDirectory().toString()

    var TOKEN = ""

    const val SSO = "SSO"
    const val NSSO = "NSSO"
    const val TRUE = "true"
    const val FALSE = "false"
    const val SUCCESS = "SUCCESS"
    const val FAILURE = "Failure"
    const val TAB = "tab"
    const val FROM = "from"
    const val TO = "to"
    const val VIEW = "View"
    const val DELETE = "Delete"

    const val MORNING = "Morning"
    const val AFTERNOON = "Afternoon"
    const val EVENING = "Evening"

/*    const val VIDEO_CALL = "Video Call"
    const val AUDIO_CALL = "Audio Call"
    const val TEXT_CHAT = "Text Chat"*/
    const val VIDEO_CALL = "VIDEO"
    const val AUDIO_CALL = "AUDIO"
    const val TEXT_CHAT = "CHAT"
    const val GENERAL_PRACTITIONER = "General Practitioner"

    const val MY_APPOINTMENTS = "My Appointments"
    const val MY_HISTORY = "MyHistory"

    const val LOGIN = "Login"
    const val DATA = "Data"
    const val MODE = "Mode"
    const val ROOM_NAME = "RoomName"
    const val ATTACH_DOCUMENT = "AttachDocument"
    const val OPTION = "Option"
    const val CLICK = "Click"
    const val DOWNLOAD = "Download"

    const val DASHBOARD = "Dashboard"
    const val UPCOMING = "Upcoming"
    const val PAST = "Past"
    const val CONSULTATION = "Consultation"

    const val RESCHEDULE = "Reschedule"
    const val CANCEL = "Cancel"
    const val VIEW_INVOICE = "ViewInvoice"
    const val SAVE_FEEDBACK = "SaveFeedback"
    const val BOOK_FOLLOW_UP = "BookFollowUp"
    const val ORDER_MEDICINE = "OrderMedicine"
    const val PRESCRIPTION = "PRE"
    const val LAB_REPORT = "LAB"

    const val STEP_ONE = 1
    const val STEP_TWO = 2
    const val STEP_THREE = 3
    const val STEP_FOUR = 4
    const val STEP_FIVE = 5
    const val STEP_SIX = 6

    const val GALLERY_SELECT_CODE = 2291
    const val FILE_SELECT_CODE = 2292

    const val CONSULT_NOW = "ConsultNow"
    const val SCHEDULE_APPOINTMENT = "ScheduleAppointment"

    object UserConstants {
        const val NAME = "NM"
        const val DOB = "DOB"
        const val GENDER = "G"
        const val PHONE_NUMBER = "PN"
        const val EMAIL_ADDRESS = "EA"
        const val PARTNER_CODE = "PC"

        const val MN = "MN"
        const val IC = "IC"
        const val LID = "LID"
        const val EID = "EID"
        const val SRC = "SRC"
        const val PCD = "PCD"
        const val CN = "CN"
        const val LOC = "LOC"
        const val BU = "BU"
        const val EN = "EN"
        const val DEPT = "DEPT"
        const val DEVICE_ID = "DEVICEID"
        const val LAUNCH_TYPE = "LAUNCHTYPE"
        const val FAMILY = "FAMILY"
        const val PAYMENT = "PAYMENT"

        const val FMID = "FMID"
        const val FMNA = "FMNA"
        const val FMDOB = "FMDOB"
        const val FMG = "FMG"
        const val FMRE = "FMRE"
        const val CRD = "CRD"
    }

}
