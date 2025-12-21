package com.fauzangifari.data.repository


import com.fauzangifari.data.mapper.toDomain
import com.fauzangifari.data.source.remote.dto.request.UserRequest
import com.fauzangifari.data.source.remote.retrofit.UserApiService
import com.fauzangifari.domain.common.Resource
import com.fauzangifari.domain.model.User
import com.fauzangifari.domain.model.UserMe
import com.fauzangifari.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userApiService: UserApiService
) : UserRepository {
    override suspend fun getUsersMe(): Resource<UserMe> {
        return try {
            val response = userApiService.getUsersMe()

            if (response.success == true && response.result != null) {
                Resource.Success(response.toDomain())
            } else {
                Resource.Error(response.message ?: "Failed to get user data")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun getAllUsers(): Resource<List<User>> {
        return try {
            val response = userApiService.getAllUsers()

            if (response.success == true && response.result != null) {

                val mappedUsers = response.result.mapNotNull { userResponse ->
                    userResponse?.toDomain()
                }


                Resource.Success(mappedUsers)

            } else {
                Resource.Error(response.message ?: "Failed to get users list")
            }

        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun updateUser(
        usersId: String,
        name: String?,
        secondaryEmail: String?
    ): Resource<UserMe> {
        return try {
            val request = UserRequest(
                name = name,
                secondaryEmail = secondaryEmail
            )
            val response = userApiService.updateUser(usersId, request)

            if (response.success == true && response.result != null) {
                Resource.Success(response.toDomain())
            } else {
                Resource.Error(response.message ?: "Failed to update user data")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

}
