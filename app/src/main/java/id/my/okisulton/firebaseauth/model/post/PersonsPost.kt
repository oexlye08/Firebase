package id.my.okisulton.firebaseauth.model.post


data class PersonsPost(
	val id: String?,
	val name: String,
	val address: String
){
	constructor(): this("","", ""){

	}
}
