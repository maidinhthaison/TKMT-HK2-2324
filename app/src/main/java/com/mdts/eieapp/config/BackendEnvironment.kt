package com.mdts.eieapp.config

enum class BackendEnvironment (
    val baseUrl: String,
    val jwtToken: String
) {

    Dev(
        baseUrl = "https://api.themoviedb.org",
        jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjNWJhZDQwMDkzMzBkYjg4YTA3YjYyYjEwZTkxNzFlYSIsInN1YiI6IjY1ZTA1OGIwNTI5NGU3MDE2MzRlYTQ4ZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.QqlyfqCRjGD99wXHXgw0Ap2YW0rOfVRwUHX84-mvRqw"
    ),

    Staging(
        baseUrl = "https://api.themoviedb.org",
        jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjNWJhZDQwMDkzMzBkYjg4YTA3YjYyYjEwZTkxNzFlYSIsInN1YiI6IjY1ZTA1OGIwNTI5NGU3MDE2MzRlYTQ4ZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.QqlyfqCRjGD99wXHXgw0Ap2YW0rOfVRwUHX84-mvRqw"
    ),

    Prod(
        baseUrl = "https://api.themoviedb.org",
        jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjNWJhZDQwMDkzMzBkYjg4YTA3YjYyYjEwZTkxNzFlYSIsInN1YiI6IjY1ZTA1OGIwNTI5NGU3MDE2MzRlYTQ4ZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.QqlyfqCRjGD99wXHXgw0Ap2YW0rOfVRwUHX84-mvRqw"
    )

}
const val PATH_IMAGE_URL: String  = "https://image.tmdb.org/t/p/w500"
const val API_VERSION = "3"