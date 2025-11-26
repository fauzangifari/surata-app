package com.fauzangifari.data.mapper

import com.fauzangifari.data.source.remote.dto.response.ResultItemTeacher
import com.fauzangifari.domain.model.Teacher

fun ResultItemTeacher.toDomain(): Teacher {
    return Teacher(
        id = this.id,
        userId = this.userId,
        nik = this.nik,
        nip = this.nip,
        birthPlace = this.birthPlace,
        birthDate = this.birthDate,
        phone = this.phone,
        gender = this.gender,
        address = this.address?.toDomain(),
        religion = this.religion,
        nationality = this.nationality,
        employmentStatus = this.employmentStatus,
        maritalStatus = this.maritalStatus,
        taxNumber = this.taxNumber,
        taxName = this.taxName,
        familyCardNumber = this.familyCardNumber,
        spouseName = this.spouseName?.toString(),
        spouseNip = this.spouseNip,
        spouseOccupation = this.spouseOccupation
    )
}
