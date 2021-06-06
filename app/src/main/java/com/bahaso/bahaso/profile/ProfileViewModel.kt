package com.bahaso.bahaso.profile

import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.bahaso.bahaso.core.data.LoadResult
import com.bahaso.bahaso.core.data.QuizRepository
import com.bahaso.bahaso.core.domain.Topic
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val quizRepository: QuizRepository) : ViewModel()  {

        fun editDataFromFireStore(userId: String, data: HashMap<String, String>) = callbackFlow<LoadResult<Boolean>> {
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(userId)
                .set(data)
                .addOnSuccessListener {
                    offer(LoadResult.Success(true))
                }
                .addOnFailureListener {
                    offer(LoadResult.Error("Gagal mengedit Biodata"))
                }
            awaitClose{db}
        }

}