package com.sungs.dorf.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


// globalWords 컬렉션 전담. 얇은 읽기/쓰기 계층.
class FirestoreWordDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    // 컬렉션 이름을 한 곳에 고정 → 오타로 딴 컬렉션 만드는 사고 방지
    private val collection get() = firestore.collection("globalWords")

    // 키에 해당하는 문서를 GlobalWordDto로. 문서 없으면 null.
    suspend fun getGlobalWord(key: String): GlobalWordDto? {
        val snapshot = collection.document(key).get().await()
        return if (snapshot.exists()) snapshot.toObject<GlobalWordDto>() else null
    }

    // dto를 그 키 문서에 통째로 저장(덮어쓰기).
    suspend fun saveGlobalWord(key: String, dto: GlobalWordDto) {
        collection.document(key).set(dto).await()
    }
}