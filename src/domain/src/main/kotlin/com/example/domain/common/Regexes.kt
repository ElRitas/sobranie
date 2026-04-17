package com.example.domain.common

object Regexes {
    val EMAIL_ADDRESS: Regex =
        Regex("[\\w+._%\\-]{1,256}@[\\w\\-]{1,64}(\\.[\\w\\-]{1,25})+")

    val PHONE_NUMBER: Regex =
        Regex("(\\+\\d+[\\- \\.]*)?(\\(\\d+\\)[\\- \\.]*)?(\\d[\\d\\- \\.]+\\d)")
}
