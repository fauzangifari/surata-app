package com.fauzangifari.data.utils

import com.fauzangifari.data.source.remote.response.Result
import com.fauzangifari.data.source.remote.response.ResultItem
import com.fauzangifari.domain.model.Letter

fun ResultItem.toDomain(): Letter {
    return Letter(
        id = this.id.orEmpty(),
        letterDate = this.letterDate.orEmpty(),
        endDate = this.endDate.orEmpty(),
        letterContent = this.letterContent.orEmpty(),
        isPrinted = this.isPrinted == true,
        beginDate = this.beginDate.orEmpty(),
        createdAt = this.createdAt.orEmpty(),
        attachment = this.attachment.orEmpty(),
        letterType = this.letterType.orEmpty(),
        status = this.status.orEmpty(),
        letterNumber = this.letterNumber.orEmpty(),
        applicantName = applicant?.name.orEmpty(),
        applicantEmail = applicant?.email.orEmpty()
    )
}

fun Result.toDomain(): Letter {
    return Letter(
        id = this.id.orEmpty(),
        letterDate = this.letterDate.orEmpty(),
        endDate = this.endDate.orEmpty(),
        letterContent = this.letterContent.orEmpty(),
        isPrinted = this.isPrinted == true,
        beginDate = this.beginDate.orEmpty(),
        createdAt = this.createdAt.orEmpty(),
        attachment = this.attachment.orEmpty(),
        letterType = this.letterType.orEmpty(),
        status = this.status.orEmpty(),
        letterNumber = this.letterNumber.orEmpty(),
        applicantName = applicant?.name.orEmpty(),
        applicantEmail = applicant?.email.orEmpty()
    )
}