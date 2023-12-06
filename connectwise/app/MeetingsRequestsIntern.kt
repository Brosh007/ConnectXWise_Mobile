package com.example.connectwise

data class MeetingsRequestsIntern(val BusinessOwnerCompanyName: String = "",
                                  val BusinessOwnerID: String = "",
                                  val FirebaseKey: String = "",
                                  val InternFirstName: String = "",
                                  val InternID: String = "",
                                  val InternLastName: String = "",
                                  val MeetingDateTime: String = "",
                                  val MeetingPurpose: String = "",
                                  val MeetingRequestID: Int = 0,
                                  val Status: String = "")
