package com.martdev.chitchat.utils

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.google.firebase.firestore.DocumentChange.Type.*

abstract class FireStoreAdapter<VH : RecyclerView.ViewHolder>(
    private var query: Query?
) : RecyclerView.Adapter<VH>(), EventListener<QuerySnapshot> {

    private var registration: ListenerRegistration? = null
    private val snapshots = arrayListOf<DocumentSnapshot>()

    fun startListening() {
        if (query != null && registration == null) {
            registration = query?.addSnapshotListener(this)
        }
    }

    fun stopListening() {
        if (registration != null) {
            registration?.remove()
            registration = null
        }

        snapshots.clear()
        notifyDataSetChanged()
    }

    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if (error != null) {
            Log.w("FireStoreAdapter", "onEventError: $error")
            return
        }

        //Dispatch the event
        for (change in value?.documentChanges!!) {
            when (change.type) {
                ADDED -> onDocumentAdded(change)
                MODIFIED -> onDocumentModified(change)
                REMOVED -> onDocumentRemoved(change)
            }
        }
        dataChanged()
    }

    override fun getItemCount(): Int {
        return snapshots.size
    }

    lateinit var dataChanged: () -> Unit
    lateinit var errorFound:(FirebaseFirestoreException) -> Unit
    fun getSnapshot(index: Int) = snapshots[index]
    private fun onDocumentAdded(change: DocumentChange) {
        snapshots.add(change.newIndex, change.document)
        notifyItemInserted(change.newIndex)
    }

    private fun onDocumentModified(change: DocumentChange) {
        if (change.oldIndex == change.newIndex) {
            //item changed but remained in same position
            snapshots[change.oldIndex] = change.document
            notifyItemChanged(change.oldIndex)
        } else {
            snapshots.removeAt(change.oldIndex)
            snapshots.add(change.newIndex, change.document)
            notifyItemMoved(change.oldIndex, change.newIndex)
        }
    }

    private fun onDocumentRemoved(change: DocumentChange) {
        snapshots.removeAt(change.oldIndex)
        notifyItemRemoved(change.oldIndex)
    }
}