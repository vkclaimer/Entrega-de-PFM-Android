package com.app.daintybox.models

import com.google.firebase.firestore.QueryDocumentSnapshot

class CartItem(var cantidad: Int, var id_product: String) {
    var product: QueryDocumentSnapshot? = null
}