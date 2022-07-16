package com.vivant.telemedicine.common

import com.vivant.telemedicine.model.RecordInSession

class ConsultAndScheduleSingleton private constructor() {

    var consultationMode = ""
    var speciality = ""
    var doctorId = 0
    var doctorImage = ""
    var supplierId = 0
    var doctorName = ""
    var doctorSpeciality = ""
    var doctorExperince = ""
    var doctorGender = ""
    var totalPrice = ""
    var walletAmount = ""
    var selfPay = "0"
    var updatedBalance = 0.0
    var appointmentDate = ""
    var appointmentTime = ""
    var appointmentDuration = ""
    var currentMedicalHistory = ""
    var currentMedication = ""
    val recordsList: MutableList<RecordInSession> = mutableListOf()

    fun addRecordInSession(item:RecordInSession) {
        recordsList.add(item)
        Utilities.printData("RecordsList",recordsList,true)
    }

    fun removeRecordInSession(item:RecordInSession) {
        recordsList.remove(item)
        Utilities.printData("RecordsList",recordsList,true)
    }

    fun clearData() {
        for ( record in recordsList ) {
            Utilities.deleteFileFromLocalSystem(record.Path+"/"+record.Name)
        }
        instance = null
        Utilities.printLogError("Cleared ConsultAndSchedule Data")
    }

    companion object {
        var instance: ConsultAndScheduleSingleton? = null
            get() {
                if (field == null) {
                    synchronized(ConsultAndScheduleSingleton::class.java) {
                        if (field == null) {
                            field = ConsultAndScheduleSingleton()
                        }
                    }
                }
                return field
            }
            private set
    }

/*    companion object {
        private var instance: ConsultAndScheduleSingleton? = null
        fun getInstance(): ConsultAndScheduleSingleton? {
            if (instance == null) {
                instance = ConsultAndScheduleSingleton()
            }
            return instance
        }
    }*/

}