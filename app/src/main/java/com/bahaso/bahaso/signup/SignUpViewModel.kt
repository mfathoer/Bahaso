package com.bahaso.bahaso.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bahaso.bahaso.core.data.QuizRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

class SignUpViewModel @Inject constructor(private val quizRepository: QuizRepository) : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var user : FirebaseUser

    private var viewmodelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewmodelJob)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome : LiveData<Boolean>
        get() = _navigateToHome

    var name: String = ""
    var birth: String = ""
    var gender: String = ""
    var email: String = ""
    var password: String = ""

    fun signUp(
        name: String,
        birth: String,
        gender: String,
        email: String,
        password: String
    ){

        this.name = name
        this.birth = birth
        this.gender = gender
        this.email = email
        this.password = password

        uiScope.launch {
            _isLoading.value = true

            withContext(Dispatchers.IO){
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnFailureListener{
                        Timber.d("Di dalam on failure")
                    }
                    .addOnCompleteListener{ task ->
                        Timber.d("Di dalam on Complete")

                        if (task.isSuccessful){
                            Timber.d("User berhasil daftar")
                            user = auth.currentUser!!

                            // Menyimpan data ke RealTimedatabase
                            addDataToFirestore(user.uid)

                            _isLoading.postValue(false)
                            _navigateToHome.value = true

                        } else{
                            _navigateToHome.value = false
                        }
                    }
            }

        }

    }

    private fun addDataToFirestore(userId: String) {
        val data = hashMapOf(
            "name" to name,
            "birth_date" to birth,
            "gender" to gender
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(userId)
            .set(data)
            .addOnSuccessListener {
                Timber.d("Data berhasil disimpan di firestore")
            }
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }


}