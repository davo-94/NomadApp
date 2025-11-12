package cl.vasquez.nomadapp.model

import cl.vasquez.nomadapp.data.Post
import cl.vasquez.nomadapp.data.PostDao

/**
 * Este repositorio separa la capa de datos de la vista,
 * cumpliendo el principio de modularidad (MVVM)
 */
class PostRepository(private val postDao: PostDao) {

    suspend fun insert(post: Post) = postDao.insert(post)
    suspend fun getAll(): List<Post> = postDao.getAll()
    suspend fun delete(post: Post) = postDao.delete(post)
    suspend fun getPostById(id: Int): Post? = postDao.getById(id)
    suspend fun update(post: Post) = postDao.update(post)


}