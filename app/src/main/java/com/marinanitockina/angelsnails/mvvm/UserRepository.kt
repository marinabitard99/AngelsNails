package com.marinanitockina.angelsnails.mvvm

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.marinanitockina.angelsnails.mvvm.models.Record
import com.marinanitockina.angelsnails.mvvm.models.Service
import com.marinanitockina.angelsnails.mvvm.models.ServiceMaster
import com.marinanitockina.angelsnails.mvvm.models.User

class UserRepository {

    private val database: DatabaseReference = Firebase.database.reference

    // Database request for receiving user's info based on their email
    fun getUserInfo(email: String) {
        val query = database.child("staff")
            .orderByChild("email")
            .equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var user: User? = null
                if (snapshot.exists()) {
                    snapshot.children.forEach { child ->
                        user = child.getValue(User::class.java)?.copy(id = child.key)
                    }
                }
                userCallback(user)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Ohsnap", "loadPost:onCancelled", error.toException())
            }

        })
    }

    // Database request for getting all services
    fun getServices() {
        val serviceList = mutableListOf<Service?>()
        val query = database.child("services")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach { child ->
                        val service = child.getValue(Service::class.java)?.copy(id = child.key)
                        serviceList.add(service)
                    }
                }
                servicesCallback(serviceList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Ohsnap", "loadPost:onCancelled", error.toException())
            }

        })
    }

    // Database request for getting all masters
    fun getServiceMasters() {
        val mastersList = mutableMapOf<String, ServiceMaster?>()
        val query = database.child("staff")
            .orderByChild("role")
            .equalTo("master")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach { child ->
                        val master = child.getValue(ServiceMaster::class.java)
                        master?.let {
                            mastersList[child.key!!] = it
                        }
                    }
                }
                serviceMastersCallback(mastersList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Ohsnap", "loadPost:onCancelled", error.toException())
            }
        })
    }

    // Database request for getting records based on client's email
    fun getClientRecords(email: String) {
        val recordsList = mutableListOf<Record?>()
        val query = database.child("records").orderByChild("emailClient").equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach { child ->
                        val record = child.getValue(Record::class.java)?.copy(id = child.key)
                        recordsList.add(record)
                    }
                }
                recordsCallback(recordsList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Ohsnap", "loadPost:onCancelled", error.toException())
            }
        })


    }

    // Database request for saving new record
    fun saveRecord(record: Record) {
        val key = database.child("records").push().key
        if (key == null) {
            Log.w("Ohsnap", "Couldn't get push key for posts")
            return
        }

        val recordValues = record.toMap()

        val childUpdates = hashMapOf<String, Any>(
            "/records/$key" to recordValues,
        )

        database.updateChildren(childUpdates)
    }

    // Database request for getting records based on master's id
    fun getMasterRecords(masterId: String) {
        val recordsList = mutableListOf<Record?>()
        val query = database.child("records").orderByChild("idMaster").equalTo(masterId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach { child ->
                        val record = child.getValue(Record::class.java)?.copy(id = child.key)
                        recordsList.add(record)
                    }
                }
                recordsCallback(recordsList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Ohsnap", "loadPost:onCancelled", error.toException())
            }
        })
    }

    // Database request for getting all records
    fun getAllRecords() {
        val recordsList = mutableListOf<Record?>()
        val query = database.child("records")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach { child ->
                        val record = child.getValue(Record::class.java)?.copy(id = child.key)
                        recordsList.add(record)
                    }
                }
                recordsCallback(recordsList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Ohsnap", "loadPost:onCancelled", error.toException())
            }
        })
    }

    // Database request for deleting records by its id
    fun deleteRecord(recordId: String) {
        database.child("records").child(recordId).removeValue()
    }

    // Callback used when getting user info from database
    var userCallback: (User?) -> Unit = {}
    // Callback used when getting service info from database
    var servicesCallback: (List<Service?>) -> Unit = {}
    // Callback used when getting master info from database
    var serviceMastersCallback: (Map<String, ServiceMaster?>) -> Unit = {}
    // Callback used when getting records from database
    var recordsCallback: (List<Record?>) -> Unit = {}

}