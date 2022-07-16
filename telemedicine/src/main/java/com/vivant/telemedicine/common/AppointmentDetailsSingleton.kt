package com.vivant.telemedicine.common

import com.vivant.telemedicine.model.ListAppointmentsModel
import com.vivant.telemedicine.model.ListConsultationModel

class AppointmentDetailsSingleton private constructor() {

    var appointment: ListAppointmentsModel.UpcomingAppointment = ListAppointmentsModel.UpcomingAppointment()
    var consultation: ListConsultationModel.Consultation = ListConsultationModel.Consultation()

    fun clearData() {
        instance = null
        Utilities.printLogError("Cleared AppointmentDetails Data")
    }

    companion object {
        var instance: AppointmentDetailsSingleton? = null
            get() {
                if (field == null) {
                    synchronized(AppointmentDetailsSingleton::class.java) {
                        if (field == null) {
                            field = AppointmentDetailsSingleton()
                        }
                    }
                }
                return field
            }
            private set
    }

}
