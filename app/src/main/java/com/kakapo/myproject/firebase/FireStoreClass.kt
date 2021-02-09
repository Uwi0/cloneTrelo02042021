package com.kakapo.myproject.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kakapo.myproject.activity.*
import com.kakapo.myproject.models.Board
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

    fun createBoard(activity: CreateBoardActivity, board: Board){
        mFireStore.collection(Constants.BOARDS)
                .document()
                .set(board, SetOptions.merge())
                .addOnSuccessListener{
                    Log.i(activity.javaClass.simpleName, "Board Created successfully.")

                    Toast.makeText(
                            activity,
                            "Board created successfully",
                            Toast.LENGTH_SHORT
                    ).show()

                    activity.boardCreateSuccessFully()
                }
                .addOnFailureListener{ e ->
                    activity.hideProgressDialog()
                    Log.e(
                            activity.javaClass.name,
                            "Error while creating Board.",
                            e
                    )
                }
    }

    fun loadUserData(activity: Activity, readBoardList: Boolean = false){
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
                            activity.updateNavigationUserDetails(loggedUser, readBoardList)
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

    fun getBoardList(activity: MainActivity){
        mFireStore.collection(Constants.BOARDS)
                .whereArrayContains(Constants.ASSIGNED_TO, getCurrentUserId())
                .get()
                .addOnSuccessListener { document ->
                    Log.i(activity.javaClass.simpleName, document.documents.toString())
                    val boardList: ArrayList<Board> = ArrayList()
                    for (i in document.documents){
                        val board = i.toObject(Board::class.java)!!
                        board.documentId = i.id
                        boardList.add(board)
                    }

                    activity.populateBoardToUi(boardList)
                }
                .addOnFailureListener { e ->
                    activity.hideProgressDialog()
                    Log.e(activity.javaClass.simpleName, "Error while creating a board", e)
                }
    }

    fun getBoardDetails(activity: TaskListActivity, documentId: String) {
        mFireStore.collection(Constants.BOARDS)
                .document(documentId)
                .get()
                .addOnSuccessListener { document ->
                    Log.e(activity.javaClass.simpleName, document.toString())


                    val board = document.toObject(Board::class.java)!!
                    board.documentId = document.id

                    activity.boardDetails(board)

                }
                .addOnFailureListener { e ->
                    activity.hideProgressDialog()
                    Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
                }
    }

    fun addUpdateTaskList(activity: TaskListActivity, board: Board){
        val taskListHashMap = HashMap<String, Any>()
        taskListHashMap[Constants.TASK_LIST] = board.taskList

        mFireStore.collection(Constants.BOARDS)
                .document(board.documentId)
                .update(taskListHashMap)
                .addOnSuccessListener{
                    Log.i(activity.javaClass.simpleName, "TaskList updated successfully")

                    activity.addUpdateTaskListSuccess()
                }
                .addOnFailureListener { e ->
                    activity.hideProgressDialog()
                    Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
                }

    }

    fun getAssignedMembersListDetails(activity: MembersActivity, assignedTo: ArrayList<String>){
        mFireStore.collection(Constants.USERS)
            .whereIn(Constants.ID, assignedTo)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())

                val usersList: ArrayList<User> = ArrayList()

                for (i in document.documents){
                    val user = i.toObject(User::class.java)!!
                    usersList.add(user)
                }

                activity.setupMemberList(usersList)
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error try to get user", e)
            }
    }


}