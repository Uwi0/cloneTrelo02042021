package com.kakapo.myproject.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kakapo.myproject.activity.MainActivity
import com.kakapo.myproject.activity.MyProfileActivity
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

    fun loadUserData(activity: Activity){
        mFireStore.collection(Constants.USERS)
                .document(getCurrentUserId())
                .get()
                .addOnSuccessListener { document ->
                    val  loggedUser = document.toObject(User::class.java)!!

                    when(activity){
                        is SignInActivity ->{
                            activity.signInSuccess(loggedUser)
                        }

                        is MainActivity ->{
                            activity.updateNavigationUserDetails(loggedUser)
                        }

                        is MyProfileActivity ->{
                            activity.setUserDataInUi(loggedUser)
                        }
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

    fun updateUserProfileData(activity: MyProfileActivity, userHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.USERS)
                .document(getCurrentUserId())
                .update(userHashMap)
                .addOnSuccessListener {
                    Log.i(activity.javaClass.simpleName, "ProfileData Update")
                    Toast.makeText(
                            activity,
                            "Profile updated successfully!",
                            Toast.LENGTH_SHORT
                    ).show()
                    activity.profileUpdateSuccess()
                }
                .addOnFailureListener{ e ->
                    Log.e(
                            activity.javaClass.simpleName,
                            "error while creating board",
                            e
                    )

                    Toast.makeText(
                            activity,
                            "Error while updating the profile!!",
                            Toast.LENGTH_SHORT
                    ).show()
                }
    }

}