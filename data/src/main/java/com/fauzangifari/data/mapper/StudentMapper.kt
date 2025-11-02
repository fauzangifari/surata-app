package com.fauzangifari.data.mapper

import com.fauzangifari.data.source.remote.dto.response.Address
import com.fauzangifari.data.source.remote.dto.response.ResultItemStudent
import com.fauzangifari.domain.model.Student

fun ResultItemStudent.toDomain(): Student {
    return Student(
        id = this.id.orEmpty(),
        nik = this.nik.orEmpty(),
        nisn = this.nisn.orEmpty(),
        nipd = this.nipd.orEmpty(),
        name = this.name.orEmpty(),
        birthPlace = this.birthPlace.orEmpty(),
        birthDate = this.birthDate.orEmpty(),
        phoneNumber = this.phoneNumber.orEmpty(),
        gender = this.gender.orEmpty(),
        address = this.address?.toDomain()
    )
}

fun Address.toDomain(): com.fauzangifari.domain.model.Address {
    return com.fauzangifari.domain.model.Address(
        country = this.country.orEmpty(),
        rt = this.rt.orEmpty(),
        province = this.province.orEmpty(),
        rw = this.rw.orEmpty(),
        city = this.city.orEmpty(),
        street = this.street.orEmpty(),
        district = this.district.orEmpty(),
        postalCode = this.postalCode.orEmpty(),
        location = this.location?.toDomain(),
        subDistrict = this.subDistrict.orEmpty()
    )
}

fun com.fauzangifari.data.source.remote.dto.response.Location.toDomain(): com.fauzangifari.domain.model.Location {
    return com.fauzangifari.domain.model.Location(
        coordinate = this.coordinates.orEmpty(),
        type = this.type.orEmpty()
    )
}