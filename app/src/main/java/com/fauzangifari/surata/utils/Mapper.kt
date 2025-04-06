package com.fauzangifari.surata.utils

import com.fauzangifari.surata.data.source.remote.response.ResultItem
import com.fauzangifari.surata.domain.model.Letter

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
    )
}