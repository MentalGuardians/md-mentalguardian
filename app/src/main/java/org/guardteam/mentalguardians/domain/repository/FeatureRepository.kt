package org.guardteam.mentalguardians.domain.repository

import kotlinx.coroutines.flow.Flow
import org.guardteam.mentalguardians.common.utils.Result
import org.guardteam.mentalguardians.domain.model.Content
import org.guardteam.mentalguardians.domain.model.ContentById
import org.guardteam.mentalguardians.domain.model.Prediction
import org.guardteam.mentalguardians.domain.model.Response
import org.guardteam.mentalguardians.domain.model.Therapist
import org.guardteam.mentalguardians.domain.model.TherapistById

interface FeatureRepository {

    fun predict(text: String): Flow<Result<Prediction>>

    fun content(content: String): Flow<Result<Content>>

    fun contentById(contentId: String): Flow<Result<ContentById>>

    fun expert(expert: String): Flow<Result<Therapist>>

    fun expertById(therapistId: String): Flow<Result<TherapistById>>

    fun booking(
        therapistId: String,
        date: String,
        time: String,
        method: String
    ): Flow<Result<Response>>
}