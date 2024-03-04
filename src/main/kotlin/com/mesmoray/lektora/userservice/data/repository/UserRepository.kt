package com.mesmoray.lektora.userservice.data.repository

import com.mesmoray.lektora.userservice.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String>
