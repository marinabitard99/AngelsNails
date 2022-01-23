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

    fun getUserInfo(email: String) {
        val query = database.child("staff")
            .orderByChild("email")
            .equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var user: User? = null
                if (snapshot.exists()) {
                    snapshot.children.forEach { child ->
                        user = child.getValue(User::class.java)
                        user?.id = child.key
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
        val serviceList = linkedMapOf<String, Service>()
        val query = database.child("services")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach { child ->
                        val service = child.getValue(Service::class.java)
                        service?.let {
                            serviceList[child.key!!] = it
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
                    serviceMastersCallback(mastersList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Ohsnap", "loadPost:onCancelled", error.toException())
            }
        })
    }

    fun getClientRecords(email: String) {
        val recordsList = mutableMapOf<String, Record?>()
        val query = database.child("records").orderByChild("email").equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach { child ->
                        val record = child.getValue(Record::class.java)
                        record?.let {
                            recordsList[child.key!!] = it
                        }
                    }
                    recordsCallback(recordsList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Ohsnap", "loadPost:onCancelled", error.toException())
            }
        })


    }

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

    fun getMasterRecords(masterId: String) {
        val recordsList = mutableMapOf<String, Record?>()
        val query = database.child("records").orderByChild("idMaster").equalTo(masterId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach { child ->
                        val record = child.getValue(Record::class.java)
                        record?.let {
                            recordsList[child.key!!] = it
                        }
                    }
                    recordsCallback(recordsList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Ohsnap", "loadPost:onCancelled", error.toException())
            }
        })
    }

    var userCallback: (User?) -> Unit = {}
    var servicesCallback: (Map<String, Service?>) -> Unit = {}
    var serviceMastersCallback: (Map<String, ServiceMaster?>) -> Unit = {}
    var recordsCallback: (Map<String, Record?>) -> Unit = {}

}