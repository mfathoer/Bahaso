package com.bahaso.bahaso.profile

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.bahaso.bahaso.core.data.LoadResult
import com.bahaso.bahaso.core.data.QuizRepository
import com.bahaso.bahaso.core.domain.Topic
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import java.net.PasswordAuthentication
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

    fun changePassword(email: String, oldPassword: String, newPassword: String)  = callbackFlow<LoadResult<Boolean>> {
        val user = Firebase.auth.currentUser
        val credential = EmailAuthProvider
            .getCredential(email, oldPassword)

        user!!.reauthenticate(credential)
            .addOnCompleteListener{auth ->
                if(auth.isSuccessful){
                    user!!.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Timber.d("Edit Password Berhasil")
                                offer(LoadResult.Success(true))
                            }
                        }
                }
            }
            .addOnFailureListener{
                offer(LoadResult.Error("Password lama salah"))
            }
        awaitClose{user}
    }

    fun changeEmail(oldEmail: String, newEmail: String, password: String)  = callbackFlow<LoadResult<Boolean>> {
        val user = Firebase.auth.currentUser
        val credential = EmailAuthProvider
            .getCredential(oldEmail, password)

        user!!.reauthenticate(credential)
            .addOnCompleteListener{auth ->
                if(auth.isSuccessful){
                    user!!.updateEmail(newEmail)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Timber.d("Edit Password Berhasil")
                                offer(LoadResult.Success(true))
                            }
                        }
                }
            }
            .addOnFailureListener{
                offer(LoadResult.Error("Password anda salah"))
            }
        awaitClose{user}
    }
}