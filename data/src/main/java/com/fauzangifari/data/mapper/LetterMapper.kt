package com.fauzangifari.data.mapper

import com.fauzangifari.data.source.local.room.entity.LetterEntity
import com.fauzangifari.data.source.remote.dto.request.LetterRequest
import com.fauzangifari.data.source.remote.dto.request.PresignedRequest
import com.fauzangifari.data.source.remote.dto.response.PresignedResponse
import com.fauzangifari.data.source.remote.dto.response.Result
import com.fauzangifari.data.source.remote.dto.response.ResultItem
import com.fauzangifari.domain.model.Letter
import com.fauzangifari.domain.model.Presigned
import com.fauzangifari.domain.model.ReqLetter
import com.fauzangifari.domain.model.ReqPresigned

// Remote
fun ResultItem.toDomain(): Letter {
    return Letter(
        id = this.id.orEmpty(),
        subject = this.subject.orEmpty(),
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
        applicantEmail = applicant?.email.orEmpty(),
        cc = emptyList(),
        reason = null
    )
}

fun Result.toDomain(): Letter {
    return Letter(
        id = this.id.orEmpty(),
        subject = this.subject.orEmpty(),
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
        applicantEmail = applicant?.email.orEmpty(),
        cc = this.cc?.filterNotNull() ?: emptyList(),
        reason = this.reason
    )
}

fun ReqLetter.toRequest(): LetterRequest {
    return LetterRequest(
        cc = this.cc ?: emptyList(),
        beginDate = this.beginDate,
        reason = this.reason,
        endDate = this.endDate,
        attachment = this.attachment,
        subject = this.subject,
        isPrinted = this.isPrinted,
        letterType = this.letterType
    )
}

fun ReqPresigned.toRequest(): PresignedRequest {
    return PresignedRequest(
        fileName = this.fileName,
        fileSize = this.fileSize,
        type = this.type,
        fileType = this.fileType
    )
}

fun PresignedResponse.toDomain(): Presigned {
    return Presigned(
        success = this.success,
        url = this.result?.url,
        message = this.message,
        errors = this.errors
    )
}

// Local
fun LetterEntity.toDomain(): Letter {
    return Letter(
        id = this.id,
        applicantName = this.applicantName,
        applicantEmail = this.applicantEmail,
        subject = this.subject,
        endDate = this.endDate,
        letterContent = this.letterContent,
        isPrinted = this.isPrinted,
        beginDate = this.beginDate,
        createdAt = this.createdAt,
        attachment = this.attachment,
        letterType = this.letterType,
        status = this.status,
        letterNumber = this.letterNumber
    )
}

fun Letter.toEntity() = LetterEntity(
    id = this.id,
    applicantName = this.applicantName,
    applicantEmail = this.applicantEmail,
    subject = this.subject,
    endDate = this.endDate,
    letterContent = this.letterContent,
    isPrinted = this.isPrinted,
    beginDate = this.beginDate,
    createdAt = this.createdAt,
    attachment = this.attachment,
    letterType = this.letterType,
    status = this.status,
    letterNumber = this.letterNumber
)