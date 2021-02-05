package com.kakapo.myproject.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kakapo.myproject.activity.SignInActivity
import com.kakapo.myproject.activity.SignUpActivity
import com.kakapo.myproject.models.User
import com.kakapo.myproject.utils.Constants

class FireStoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnCompleteListener {
                activity.userRegisteredSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error writing document $e")
            }
    }

    fun signInUser(activity: SignInActivity){
        mFireStore.collection(Constants.USERS)
                .document(getCurrentUserId())
                .get()
                .addOnSuccessListener { document ->
                    val  loggedUser = document.toObject(User::class.java)

                    if(loggedUser != null){
                        activity.signInSuccess(loggedUser)
                    }
                }
                .addOnFailureListener{ e ->
                    Log.e(activity.javaClass.simpleName, "Error get document $e")
                }
    }

    fun getCurrentUserId() : String{

        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if(currentUser != null){
            currentUserId = currentUser.uid
        }

        return currentUserId
    }

}