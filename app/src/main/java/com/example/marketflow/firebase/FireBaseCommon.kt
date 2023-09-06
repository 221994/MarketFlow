package com.example.marketflow.firebase

import com.example.marketflow.data.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class FireBaseCommon(
    private val fireBaseFireStore: FirebaseFirestore, private val auth: FirebaseAuth
) {
    private val cartCollection =
        fireBaseFireStore.collection("user").document(auth.uid!!).collection("cart")

    fun addProductToCart(cartProduct: CartProduct, result: (CartProduct?, Exception?) -> Unit) {
        cartCollection.document().set(cartProduct).addOnSuccessListener {
            result(cartProduct, null)
        }.addOnFailureListener {
            result(null, it)
        }
    }

    fun increaseQuantity(documentID: String, result: (String?, Exception?) -> Unit) {
        fireBaseFireStore.runTransaction { transaction ->
            val documentReference = cartCollection.document(documentID)
            val document = transaction.get(documentReference)
            val productObject = document.toObject(CartProduct::class.java)
            productObject?.let { cartProduct ->
                val newQuantity = cartProduct.quantity + 1
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transaction.set(documentReference, newProductObject)

            }
        }.addOnSuccessListener {
            result(documentID, null)
        }.addOnFailureListener {
            result(null, it)
        }
    }

    fun decreaseQuantity(documentID: String, result: (String?, Exception?) -> Unit) {
        fireBaseFireStore.runTransaction { transaction ->
            val documentReference = cartCollection.document(documentID)
            val document = transaction.get(documentReference)
            val productObject = document.toObject(CartProduct::class.java)
            productObject?.let { cartProduct ->
                val newQuantity = cartProduct.quantity - 1
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transaction.set(documentReference, newProductObject)

            }
        }.addOnSuccessListener {
            result(documentID, null)
        }.addOnFailureListener {
            result(null, it)
        }
    }

    enum class QuantityChange {
        INCREASE, DECREASE
    }
}