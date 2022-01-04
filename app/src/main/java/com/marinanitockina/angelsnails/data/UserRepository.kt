package com.marinanitockina.angelsnails.data

import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.marinanitockina.angelsnails.models.Service
import com.marinanitockina.angelsnails.models.User

class UserRepository {

    private val database: DatabaseReference = Firebase.database.reference

    fun getUserInfo(email: String) {
        val query = FirebaseDatabase.getInstance()
            .getReference("staff")
            .orderByChild("email")
            .equalTo(email)

        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var user: User? = null
                if (snapshot.exists()) {
                    snapshot.children.forEach { child ->
                        user = child.getValue(User::class.java)
                    }
                }
                userCallback(user)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Ohsnap", "loadPost:onCancelled", error.toException())
            }

        })
    }

    fun getServices() {
        val serviceList = mutableListOf<Service>()
        val query = FirebaseDatabase.getInstance().getReference("services")

        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach { child ->
                        val service = child.getValue(Service::class.java)
                        service?.let {
                            serviceList.add(it)
                        }
                    }
                    servicesCallback(serviceList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Ohsnap", "loadPost:onCancelled", error.toException())
            }

        })
    }

    var userCallback: (User?) -> Unit = {}
    var servicesCallback: (List<Service?>) -> Unit = {}

}